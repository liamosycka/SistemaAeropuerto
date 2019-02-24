/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

public class Guardia implements Runnable {

    private Aerolinea aerolinea;

    public Guardia(Aerolinea aerolinea) {
        this.aerolinea = aerolinea;
    }

    public void run() {
        while (true) {
            aerolinea.hacerPasarPasajero();

        }
    }

}
