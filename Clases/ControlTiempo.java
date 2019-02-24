/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ControlTiempo implements Runnable {

    private Aeropuerto aeropuerto;
    private int horaActual;

    public ControlTiempo(Aeropuerto aero) {
        this.aeropuerto = aero;
        this.horaActual = 0;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ControlTiempo.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.horaActual++;
            aeropuerto.pasarHora();
            if (horaActual == 6) {
                this.aeropuerto.comenzarHorarioAtencion();
            }
            if (horaActual == 24) {
                this.horaActual = 0;
            }
            if (horaActual == 22) {
                this.aeropuerto.terminarHorarioAtencion();
            }
        }
    }

    public int getHora() {
        return this.horaActual;
    }
}
