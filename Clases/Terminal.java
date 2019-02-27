/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Terminal {

    private Lock lock;
    private Condition esperaEmbarque;
    private char letraT;
    private int[] embarques;
    private Random rnd;
    private AtomicInteger hora;
    private FreeShop freeShop;

    public Terminal(char letra) {
        this.letraT = letra;
    }

    public Terminal(char letra, int[] embarques, FreeShop freeShop,AtomicInteger hora) {
        this.letraT = letra;
        this.embarques = embarques;
        rnd = new Random();
        this.lock = new ReentrantLock(true);
        this.esperaEmbarque = lock.newCondition();
        this.freeShop = freeShop;
        this.hora=hora;
    }

    public void ingresarTerminal(Pasajero pasajero) {
        System.out.println("Ha ingresado el Pasajero : " + pasajero.getId() + " en la TERMINAL : " + this.letraT);
    }

    public void esperarEmbarque(Pasajero pasajero) {
        lock.lock();
        System.out.println("                    PASAJERO : "+pasajero.getId()+" HORA EMBARQUE : "+pasajero.getPasaje().getHoraPartida());
        try {
            while (pasajero.getPasaje().getHoraPartida() != hora.get()) {
                try {
                    esperaEmbarque.await();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Terminal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.out.println("Es la hora de embarcar del pasajero  " + pasajero.getId());
        } finally {
            lock.unlock();
        }
    }

    public FreeShop obtenerFreeShop() {
        return this.freeShop;
    }

    public void pasarHora() {
        lock.lock();
        System.out.println("                            HORA ACTUAL "+this.hora.get());
        esperaEmbarque.signalAll();
        lock.unlock();
    }

    public char getLetraTerminal() {
        return this.letraT;
    }

    public void setLetraTerminal(char letra) {
        this.letraT = letra;
    }

    public int obtenerPuestoEmbarqueAleatorio() {
        return embarques[rnd.nextInt(embarques.length)];
    }
}
