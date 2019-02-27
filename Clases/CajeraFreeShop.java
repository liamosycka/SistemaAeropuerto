/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author liamosycka
 */
public class CajeraFreeShop implements Runnable {

    private CajaFreeShop caja;
    private int id;

    public CajeraFreeShop(CajaFreeShop caja, int id) {
        this.id = id;
        this.caja = caja;
    }

    public void run() {
        while (true) {
            caja.sacarProducto();
            this.procesarProducto();
        }
    }

    private void procesarProducto() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(CajeraFreeShop.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
