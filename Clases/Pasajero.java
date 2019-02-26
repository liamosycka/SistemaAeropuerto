/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Pasajero implements Runnable {

    private Pasaje pasaje;
    private Aeropuerto aeropuerto;
    private Aerolinea aerolinea;
    private TrenInterno tren;
    private int id;
    private boolean entrarFreeShop,comprar;
    private ControlTiempo controlTiempo;
    private Random rnd;

    public Pasajero(int id,Pasaje pasaje, Aeropuerto aeropuerto,TrenInterno tren,boolean entrarFreeShop,boolean comprar,ControlTiempo controlTiempo) {
        this.pasaje = pasaje;
        this.aeropuerto = aeropuerto;
        this.tren=tren;
        this.id=id;
        this.entrarFreeShop=entrarFreeShop;
        this.comprar=comprar;
        this.controlTiempo=controlTiempo;
        rnd=new Random();
    }

    public void run() {
        aerolinea=aeropuerto.entrarAeropuerto(this);
        if(aerolinea!=null){
            try {
                aerolinea.entrarFilaPuestoAtencion(this);
                aerolinea.obtenerAtencionPuesto(this);
                Thread.sleep(5000);
                aerolinea.salirPuestoAtencion(this);
                tren.esperarTren(this);
                tren.subir(this);
                tren.trasladarseATerminal(this, pasaje.getTerminal().getLetraTerminal());
                tren.bajar(this);
                if(entrarFreeShop){
                    int horaEmbarque=pasaje.getHoraPartida();
                    if(horaEmbarque+2<=controlTiempo.getHora()){
                        //el pasajero tiene tiempo de entrar al free shop antes de que sea la hora de embarcar
                        FreeShop freeShop=pasaje.getTerminal().obtenerFreeShop();
                        freeShop.ingresarFreeShop(this);
                    System.out.println("Pasajero "+id+" ENTRA FREE SHOP");
                        this.paseandoEnFreeS();
                        if(comprar){
                            Producto[] carrito=freeShop.llenarCarrito(rnd.nextInt(19)+1);
                            this.llenandoCarrito();
                            CajaFreeShop caja=freeShop.obtenerCaja();
                            for (int i = 0; i <carrito.length; i++) {
                                caja.ponerProducto(carrito[i]);
                            }
                        }
                        freeShop.salirFreeShop(this);
                        System.out.println("Pasajero "+id+" SALE  FREE SHOP");
                    }
                    pasaje.getTerminal().esperarEmbarque(this);
                    this.abordarVuelo();
                    
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Pasajero.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    private void abordarVuelo(){
        System.out.println("EL PASAJERO "+id+" ha subido a su vuelo");
    }
    private void paseandoEnFreeS(){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Pasajero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void llenandoCarrito(){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Pasajero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Pasaje getPasaje(){
        return this.pasaje;
    }
    public int getId(){
        return this.id;
    }

}
