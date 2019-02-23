/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

public class Pasajero implements Runnable {

    private Pasaje pasaje;
    private Aeropuerto aeropuerto;

    public Pasajero(Pasaje pasaje, Aeropuerto aeropuerto) {
        this.pasaje = pasaje;
        this.aeropuerto = aeropuerto;
    }

    public void run() {
    }

    public Pasaje getPasaje(){
        return this.pasaje;
    }

}
