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
    private boolean comprar;
    private ControlTiempo controlTiempo;

    public Pasajero(int id,Pasaje pasaje, Aeropuerto aeropuerto,TrenInterno tren,boolean comprar,ControlTiempo controlTiempo) {
        this.pasaje = pasaje;
        this.aeropuerto = aeropuerto;
        this.tren=tren;
        this.id=id;
        this.comprar=comprar;
        this.controlTiempo=controlTiempo;
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
                System.out.println("vuelve del esperar tren");
                tren.subir(this);
                tren.trasladarseATerminal(this, pasaje.getTerminal().getLetraTerminal());
                tren.bajar(this);
                if(comprar){
                    int horaEmbarque=pasaje.getHoraPartida();
                    if(horaEmbarque+2<=controlTiempo.getHora()){
                        //el pasajero tiene tiempo de entrar al free shop antes de que sea la hora de embarcar
                        System.out.println("el pasajero tiene tiempo de entrar al free shop");
                        pasaje.getTerminal().entrarFreeShop(this);
                    }
                    pasaje.getTerminal().esperarEmbarque(this);
                    
                }
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
