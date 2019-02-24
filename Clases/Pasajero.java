/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Pasajero implements Runnable {

    private Pasaje pasaje;
    private Aeropuerto aeropuerto;
    private Aerolinea aerolinea;
    private TrenInterno tren;
    private int id;

    public Pasajero(int id,Pasaje pasaje, Aeropuerto aeropuerto,TrenInterno tren) {
        this.pasaje = pasaje;
        this.aeropuerto = aeropuerto;
        this.tren=tren;
        this.id=id;
    }

    public void run() {
        aerolinea=aeropuerto.entrarAeropuerto(this);
        if(aerolinea!=null){
            try {
                aerolinea.entrarFilaPuestoAtencion(this);
                aerolinea.obtenerAtencionPuesto(this);
                Thread.sleep(5000);
                aerolinea.salirPuestoAtencion(this);
                System.out.println("por llamar a subir de tren en pasajero");
                tren.esperarTren(this);
                tren.subir(this);
                tren.trasladarseATerminal(this, pasaje.getTerminal().getLetraTerminal());
                tren.bajar(this);
            } catch (InterruptedException ex) {
                Logger.getLogger(Pasajero.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }

    public Pasaje getPasaje(){
        return this.pasaje;
    }
    public int getId(){
        return this.id;
    }

}
