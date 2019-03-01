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

    private char letraT;
    private int[] embarques;
    private Random rnd;
    private AtomicInteger hora;
    private FreeShop freeShop;

    public Terminal(char letra) {
        this.letraT = letra;
    }

    public Terminal(char letra, int[] embarques, FreeShop freeShop, AtomicInteger hora) {
        this.letraT = letra;
        this.embarques = embarques;
        rnd = new Random();
        this.freeShop = freeShop;
        this.hora = hora;
    }

    public void ingresarTerminal(Pasajero pasajero) {
        System.out.println((char) 27 + "[31mHa ingresado el Pasajero : " + pasajero.getId() + " en la TERMINAL : " + this.letraT + " hora embarque : " + pasajero.getPasaje().getHoraPartida());
    }

    public synchronized void esperarEmbarque(Pasajero pasajero) {
            while (pasajero.getPasaje().getHoraPartida() != hora.get()) {
                System.out.println((char) 27 + "[31mPasajero " + pasajero.getId() + " ESPERANDO EMBARQUE");
                try {
                    this.wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Terminal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.out.println((char) 27 + "[31mEs la hora de embarcar del pasajero  " + pasajero.getId());
    }

    public FreeShop obtenerFreeShop() {
        return this.freeShop;
    }

    public synchronized void pasarHora() {
        this.notifyAll();
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
