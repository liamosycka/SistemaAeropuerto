/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TrenInterno implements Runnable {

    private Terminal terminalActual;
    private Terminal terminalInicio;
    private Terminal[] arrTerminales;
    private Lock lock;
    private Condition esperandoTren, enTren, esperandoParaSubir;
    private int cantMax, cantActual;
    private CyclicBarrier barrera;
    private Semaphore semContinuar;
    private boolean solicitarParada;
    private int cantPersonasQueBajan;

    public TrenInterno(int cantMax, Terminal[] arrTerminales) {
        this.terminalInicio = new Terminal('Z');
        this.terminalActual = terminalInicio;
        this.lock = new ReentrantLock(true);
        this.esperandoTren = lock.newCondition();
        this.enTren = lock.newCondition();
        this.esperandoParaSubir = lock.newCondition();
        this.cantMax = cantMax;
        this.barrera = new CyclicBarrier(cantMax + 1);
        this.arrTerminales = arrTerminales;
        this.semContinuar = new Semaphore(0, true);
        this.solicitarParada = false;
        this.cantPersonasQueBajan = 0;
        this.cantActual = 0;
    }

    public void run() {
        while (true) {
            int pos = 0;
            this.comenzarRecorrido();
            while (pos < arrTerminales.length) {
                try {
                    Thread.sleep(2000);
                    this.mover(pos);
                    Thread.sleep(1000);
                    this.continuar();

                } catch (InterruptedException ex) {
                    Logger.getLogger(TrenInterno.class.getName()).log(Level.SEVERE, null, ex);
                }
                pos++;

            }
            lock.lock();
            terminalActual = terminalInicio;
            System.out.println("El tren ha regresado al inicio");
            this.esperandoParaSubir.signalAll();
            this.esperandoTren.signalAll();
            lock.unlock();
        }
    }

    public void esperarTren(Pasajero pasajero) {
        this.lock.lock();
        try {
            while (terminalActual.getLetraTerminal() != 'Z') {
                try {
                    esperandoTren.await();
                } catch (InterruptedException ex) {
                    Logger.getLogger(TrenInterno.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        } finally {
            this.lock.unlock();
        }

    }

    public void subir(Pasajero pasajero) {
        lock.lock();
        while (cantActual == cantMax) {
            try {
                esperandoParaSubir.await();
            } catch (InterruptedException ex) {
                Logger.getLogger(TrenInterno.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println((char) 27 + "[34mSe ha subido el pasajero " + pasajero.getId() + " barrera: " + barrera.getNumberWaiting());
        this.cantActual++;
        lock.unlock();
        try {
            barrera.await();
        } catch (InterruptedException ex) {
            Logger.getLogger(TrenInterno.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BrokenBarrierException ex) {
            Logger.getLogger(TrenInterno.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void mover(int pos) {
        this.lock.lock();
        terminalActual = arrTerminales[pos];
        System.out.println((char) 27 + "[30;43mTren ha llegado a terminal: " + terminalActual.getLetraTerminal());
        enTren.signalAll();
        semContinuar.release();
        this.lock.unlock();
    }

    private void continuar() {
        try {
            semContinuar.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(TrenInterno.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void comenzarRecorrido() {
        try {
            barrera.await();
        } catch (InterruptedException ex) {
            Logger.getLogger(TrenInterno.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BrokenBarrierException ex) {
            Logger.getLogger(TrenInterno.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println((char) 27 + "[30;43mTren comenzo recorrido");

    }

    public void trasladarseATerminal(Pasajero pasajero, char terminalDeseada) {
        this.lock.lock();
        while (terminalActual.getLetraTerminal() != terminalDeseada) {
            try {
                enTren.await();
            } catch (InterruptedException ex) {
                Logger.getLogger(TrenInterno.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        cantPersonasQueBajan++;
        if (cantPersonasQueBajan == 1) {
            try {
                //si nadie pidió que frenen en esta terminal, lo debe hacer este hilo
                System.out.println("    pasajero " + pasajero.getId() + " avisa que va a bajar en terminal :" + terminalDeseada);
                semContinuar.acquire();
            } catch (InterruptedException ex) {
                Logger.getLogger(TrenInterno.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.lock.unlock();
    }

    public void bajar(Pasajero pasajero) {
        this.lock.lock();
        cantPersonasQueBajan--;
        this.cantActual--;
        System.out.println((char) 27 + "[34mse ha bajado el pasajero " + pasajero.getId() + "que queria ir a la terminal : " + pasajero.getPasaje().getTerminal().getLetraTerminal());
        if (cantPersonasQueBajan == 0) {
            //es la ultima persona que baja en esa terminal
            System.out.println("\n se ha bajado la ultima persona en la terminal " + this.terminalActual);
            semContinuar.release();
        }
        this.lock.unlock();
        this.terminalActual.ingresarTerminal(pasajero);
    }
}
