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

    public Aeropuerto(Aerolinea[] arrAerolineas) {
        this.arrAerolineas = arrAerolineas;
        this.esHorarioAtencion = false;
    }

    public synchronized void entrarAeropuerto(Pasajero pasajero) {
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
            try {
                this.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (aeroIndicada != null) {
            aeroIndicada.entrarFilaPuestoAtencion(pasajero);
        } else {
            System.out.println("No existe la aerolínea indicada");
        }
    }

    public void comenzarHorarioAtencion() {
        this.esHorarioAtencion = true;
        this.notifyAll();
    }

    public void terminarHorarioAtencion() {
        this.esHorarioAtencion = false;
    }
}
