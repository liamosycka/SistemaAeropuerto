package Clases;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Aerolinea {

    private Lock lock;
    private Condition esperaHall, esperaGuardia;
    private String nombreAerolinea;
    private int cantMaxPuesto, cantActual, cantEnEspera;
    private Semaphore semPuesto;
    private boolean pasar = false;

    public Aerolinea(String nombre, int cantMax) {
        this.nombreAerolinea = nombre;
        this.cantMaxPuesto = cantMax;
        this.cantActual = 0;
        this.cantEnEspera = 0;
        this.semPuesto = new Semaphore(1, true);
        this.lock = new ReentrantLock(true);
        this.esperaHall = lock.newCondition();
        this.esperaGuardia = lock.newCondition();
    }

    public boolean equals(String nombreAerolinea) {
        return this.nombreAerolinea.equals(nombreAerolinea);
    }

    public synchronized void entrarFilaPuestoAtencion(Pasajero pasajero) {
        this.cantEnEspera++;
        this.notifyAll();
        while (!pasar) {
            try {
                this.esperaHall.await();
            } catch (InterruptedException ex) {
                Logger.getLogger(Aerolinea.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.cantEnEspera--;
        cantActual++;
        this.pasar = false;
    }

    public void obtenerAtencionPuesto(Pasajero pasajero) {
        try {
            semPuesto.acquire();
            
        } catch (InterruptedException ex) {
            Logger.getLogger(Aerolinea.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void salirPuestoAtencion(Pasajero pasajero) {
        cantActual--;
        this.esperaGuardia.signal();
        semPuesto.release();
    }

    public synchronized void hacerPasarPasajero() {
        //modulo que ejecuta el guardia en su run para ir haciendo pasar a los pasajeros del hall
        while (cantActual == cantMaxPuesto || cantEnEspera == 0) {
            try {
                this.esperaGuardia.await();
            } catch (InterruptedException ex) {
                Logger.getLogger(Aerolinea.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.pasar = true;
        this.esperaHall.signal();
    }
}
