/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Aeropuerto {

    private final Aerolinea[] arrAerolineas;
    private boolean esHorarioAtencion;
    private TrenInterno tren;

    public Aeropuerto(Aerolinea[] arrAerolineas,TrenInterno tren) {
        this.arrAerolineas = arrAerolineas;
        this.esHorarioAtencion = false;
        this.tren=tren;
    }

    public synchronized Aerolinea entrarAeropuerto(Pasajero pasajero) {
        String nombreAerolinea = pasajero.getPasaje().getNombreAerolinea();
        Aerolinea aeroIndicada = null;
        boolean encontrado = false;
        int i = 0;
        while (!encontrado) {
            if (arrAerolineas[i].equals(nombreAerolinea)) {
                encontrado = true;
                aeroIndicada = arrAerolineas[i];
            }
            i++;
        }
        while (!esHorarioAtencion) {
            // bloqueo a los hilos que ingresan hasta que sea el horario de atencion al publico
            try {
                this.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       return aeroIndicada;
    }

    public void comenzarHorarioAtencion() {
        this.esHorarioAtencion = true;
        this.notifyAll();
    }

    public void terminarHorarioAtencion() {
        this.esHorarioAtencion = false;
    }
}
