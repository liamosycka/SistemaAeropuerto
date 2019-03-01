/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Pasajero implements Runnable {

    private Pasaje pasaje;
    private Aeropuerto aeropuerto;
    private Aerolinea aerolinea;
    private TrenInterno tren;
    private int id;
    private boolean entrarFreeShop, comprar;
    private Random rnd;
    private AtomicInteger hora;

    public Pasajero(int id, Pasaje pasaje, Aeropuerto aeropuerto, TrenInterno tren, boolean entrarFreeShop, boolean comprar, AtomicInteger hora) {
        this.pasaje = pasaje;
        this.aeropuerto = aeropuerto;
        this.tren = tren;
        this.id = id;
        this.entrarFreeShop = entrarFreeShop;
        this.comprar = comprar;
        rnd = new Random();
        this.hora = hora;
    }

    public void run() {
        aerolinea = aeropuerto.entrarAeropuerto(this);
        if (aerolinea != null) {
            try {
                aerolinea.entrarFilaPuestoAtencion(this);
                aerolinea.obtenerAtencionPuesto(this);
                Thread.sleep(5000);
                aerolinea.salirPuestoAtencion(this);
                tren.esperarTren(this);
                tren.subir(this);
                tren.trasladarseATerminal(this, pasaje.getTerminal().getLetraTerminal());
                tren.bajar(this);
                if (entrarFreeShop) {
                    int horaEmbarque = pasaje.getHoraPartida();
                    if (hora.get() + 3 <= horaEmbarque) {
                        //el pasajero tiene tiempo de entrar al free shop antes de que sea la hora de embarcar
                        FreeShop freeShop = pasaje.getTerminal().obtenerFreeShop();
                        freeShop.ingresarFreeShop(this);
                        this.paseandoEnFreeS();
                        if (comprar) {
                            System.out.println("    " + (char) 27 + "[35mPasajero " + this.id + " decide comprar en FreeShop");
                            Producto[] carrito = freeShop.llenarCarrito(rnd.nextInt(19) + 1);
                            this.llenandoCarrito();
                            CajaFreeShop caja = freeShop.obtenerCaja();
                            caja.entrarCaja(this);
                            for (int i = 0; i < carrito.length; i++) {
                                caja.ponerProducto(carrito[i]);
                                Thread.sleep(1000);
                            }
                            caja.salirCaja(this);
                        }else{
                            this.paseandoEnFreeS();
                        }
                        freeShop.salirFreeShop(this);
                    }

                }
                pasaje.getTerminal().esperarEmbarque(this);
                this.abordarVuelo();
            } catch (InterruptedException ex) {
                Logger.getLogger(Pasajero.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void abordarVuelo() {
        System.out.println((char) 27 + "[32mEL PASAJERO " + id + " HA SUBIDO A SU VUELO");
    }

    private void paseandoEnFreeS() {
        try {
            System.out.println("    " + (char) 27 + "[35mPasajero " + this.id + " paseando en freeShop");
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Pasajero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void llenandoCarrito() {
        try {
            System.out.println("    " + (char) 27 + "[35mPasajero " + this.id + "esta llenando su carrito");
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Pasajero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Pasaje getPasaje() {
        return this.pasaje;
    }

    public int getId() {
        return this.id;
    }

}
