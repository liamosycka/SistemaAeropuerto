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

    private char terminalActual;
    private Terminal[] arrTerminales;
    private Pasajero[] arrPasajeros;
    private Lock lock;
    private Condition esperandoTren, enTren;
    private int cantMax;
    private CyclicBarrier barrera;
    private Semaphore semContinuar;
    private boolean solicitarParada;
    private int cantPersonasQueBajan;

    public TrenInterno(Pasajero[] arrPasajeros, int cantMax, Terminal[] arrTerminales) {
        this.terminalActual = 'Z';
        this.arrPasajeros = arrPasajeros;
        this.lock = new ReentrantLock(true);
        this.esperandoTren = lock.newCondition();
        this.enTren = lock.newCondition();
        this.cantMax = cantMax;
        this.barrera = new CyclicBarrier(cantMax + 1);
        this.arrTerminales = arrTerminales;
        this.semContinuar = new Semaphore(0, true);
        this.solicitarParada = false;
        this.cantPersonasQueBajan = 0;
    }

    public void run() {
        while (true) {
            int pos = 0;
            this.comenzarRecorrido();
            while (pos <= arrTerminales.length) {
                try {
                    Thread.sleep(5000);
                    this.mover(pos);
                    Thread.sleep(1000);
                    this.continuar();

                } catch (InterruptedException ex) {
                    Logger.getLogger(TrenInterno.class.getName()).log(Level.SEVERE, null, ex);
                }
                pos++;

            }
            terminalActual = 'Z';
        }
    }

    public void subir(Pasajero pasajero) {
        while (terminalActual != 'Z') {
            try {
                enTren.await();
                barrera.await();
            } catch (BrokenBarrierException ex) {
                Logger.getLogger(TrenInterno.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(TrenInterno.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    private void mover(int pos) {
        terminalActual = arrTerminales[pos].getLetraTerminal();
        enTren.signalAll();
        semContinuar.release();

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

    }

    public void trasladarseATerminal(Pasajero pasajero, char terminalDeseada) {
        while (terminalActual != terminalDeseada) {
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
                semContinuar.acquire();
            } catch (InterruptedException ex) {
                Logger.getLogger(TrenInterno.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public synchronized void bajar() {
        cantPersonasQueBajan--;
        if (cantPersonasQueBajan == 0) {
            //es la ultima persona que baja en esa terminal
            semContinuar.release();
        }
    }
}
