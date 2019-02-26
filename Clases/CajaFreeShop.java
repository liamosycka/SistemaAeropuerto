/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author liamosycka
 */
public class CajaFreeShop {

    private Semaphore semCaja;
    private int cantMaxItemsCinta, cantActual;

    public CajaFreeShop(int cantItemsCinta) {
        this.cantMaxItemsCinta = cantItemsCinta;
        this.cantActual = 0;
        this.semCaja = new Semaphore(1, true);

    }

    public void entrarCaja(Pasajero pasajero) {
        try {
            semCaja.acquire();
        System.out.println("             Pasajero "+pasajero.getId()+" ESTA EN CAJA DE FREESHOP");
        } catch (InterruptedException ex) {
            Logger.getLogger(CajaFreeShop.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void salirCaja(Pasajero pasajero) {
         System.out.println("            Pasajero "+pasajero.getId()+"SALE DE CAJA DE FREESHOP");
        semCaja.release();
    }

    public synchronized void ponerProducto(Producto prod) {
        while (cantActual == cantMaxItemsCinta) {
            try {
                this.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(CajaFreeShop.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.cantActual++;
        this.notifyAll();

    }

    public synchronized void sacarProducto() {
        while (cantActual == 0) {
            try {
                this.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(CajaFreeShop.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.cantActual--;
        this.notifyAll();
    }

}
