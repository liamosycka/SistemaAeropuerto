/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FreeShop {
    
    private int capacidad,cantStockProd;
    private CajaFreeShop[] arrCajas;
    private Producto[] stockProd;
    private Semaphore semFreeS;
    private Random rnd;
    
    public FreeShop(int cap,CajaFreeShop[] arrCajas){
        this.capacidad=cap;
        this.arrCajas=arrCajas;
        this.semFreeS=new Semaphore(capacidad,true);
        this.cantStockProd=20;
        this.stockProd=new Producto[cantStockProd];
        crearProductos();
        rnd=new Random();
    }
    private void crearProductos(){
        for (int i = 0; i <stockProd.length; i++) {
            stockProd[i]=new Producto(i);
        }
    }
    public void ingresarFreeShop(Pasajero pasajero){
        try {
            System.out.println("POR INTENTAR AGARRAR PERMISO DE SEMAFORO");
            semFreeS.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(FreeShop.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void salirFreeShop(Pasajero pasajero){
        semFreeS.release();
    }
    public Producto[] llenarCarrito(int cantProductos){
        System.out.println("LLENANDO CARRITO");
        Producto[] carrito=new Producto[cantProductos];
        for (int i = 0; i <carrito.length; i++) {
            carrito[i]=stockProd[rnd.nextInt(stockProd.length)];
        }
        return carrito;
    }
    public CajaFreeShop obtenerCaja(){
        return arrCajas[rnd.nextInt(arrCajas.length)];
    }
}
