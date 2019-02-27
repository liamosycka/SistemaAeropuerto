/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ControlTiempo implements Runnable {

    private Aeropuerto aeropuerto;
    private AtomicInteger hora;
    private Terminal[] arrTerminales;

    public ControlTiempo(Aeropuerto aero,AtomicInteger hora,Terminal[] arrTerminales) {
        this.aeropuerto = aero;
        this.hora=hora;
        this.arrTerminales=arrTerminales;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(20000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ControlTiempo.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.hora.getAndAdd(1);
            for (int i = 0; i < arrTerminales.length; i++) {
                arrTerminales[i].pasarHora();
            }
            System.out.println("EN CONTROL TIEMPO HORA ACTUAL : "+this.hora.get());
            if (hora.get() == 6) {
                System.out.println("Comienzo de horario de atencion");
                this.aeropuerto.comenzarHorarioAtencion();
            }
            if (hora.get() == 24) {
                this.hora.set(0);
            }
            if (hora.get() == 22) {
                this.aeropuerto.terminarHorarioAtencion();
            }
        }
    }

}
