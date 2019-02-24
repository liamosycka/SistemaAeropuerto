/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

public class Pasajero implements Runnable {

    private Pasaje pasaje;
    private Aeropuerto aeropuerto;
    private Aerolinea aerolinea;
    private TrenInterno tren;

    public Pasajero(Pasaje pasaje, Aeropuerto aeropuerto,TrenInterno tren) {
        this.pasaje = pasaje;
        this.aeropuerto = aeropuerto;
        this.tren=tren;
    }

    public void run() {
        aerolinea=aeropuerto.entrarAeropuerto(this);
        if(aerolinea!=null){
            aerolinea.entrarFilaPuestoAtencion(this);
            aerolinea.obtenerAtencionPuesto(this);
            aerolinea.salirPuestoAtencion(this);
            tren.subir(this);
            tren.trasladarseATerminal(this, pasaje.getTerminal().getLetraTerminal());
            tren.bajar();
        }
        
    }

    public Pasaje getPasaje(){
        return this.pasaje;
    }

}
