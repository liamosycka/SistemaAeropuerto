/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author liamosycka
 */
public class Main {

    public static void main(String[] args) {
        AtomicInteger hora= new AtomicInteger(5);
        int cantTerminales = 4, cantPuestosEmbarque = 5, cantAerolineas = 5, cantMaxFila = 4, capacidadTren = 5, cantPasajeros = 25, capacidadFreeS = 5, cantMaxItemsCinta = 3;
        String[] nombresAerolineas = crearNombresAerolineas();
        Terminal[] arrTerminales = crearTerminales(cantTerminales, cantPuestosEmbarque, cantMaxItemsCinta, capacidadFreeS,hora);
        Aerolinea[] arrAerolineas = crearAerolineas(cantAerolineas, arrTerminales, cantMaxFila, nombresAerolineas,hora);
        TrenInterno tren = new TrenInterno(capacidadTren, arrTerminales);
        Thread hiloTren = new Thread(tren);
        hiloTren.start();
        Aeropuerto aeropuerto = new Aeropuerto(arrAerolineas, tren);
        ControlTiempo controlTiempo = new ControlTiempo(aeropuerto,hora,arrTerminales);
        Thread hiloTiempo = new Thread(controlTiempo);
        hiloTiempo.start();
        crearPasajeros(cantPasajeros, nombresAerolineas, aeropuerto, tren,cantAerolineas,hora);
        crearGuardias(arrAerolineas, cantAerolineas);

    }

    public static void crearGuardias(Aerolinea[] arrAerolineas, int cant) {
        Guardia[] arrGuardias = new Guardia[cant];
        for (int i = 0; i < arrGuardias.length; i++) {
            arrGuardias[i] = new Guardia(arrAerolineas[i]);
        }
        for (int j = 0; j < arrGuardias.length; j++) {
            Thread hiloGuardia = new Thread(arrGuardias[j]);
            hiloGuardia.start();
        }
    }

    public static void crearPasajeros(int cant, String[] nombresAerolineas, Aeropuerto aeropuerto, TrenInterno tren, int cantAerolineas,AtomicInteger hora) {
        Random rnd = new Random();
        boolean[] arrBool = new boolean[2];
        arrBool[0] = true;
        arrBool[1] = false;
        Pasajero[] arrPasajeros = new Pasajero[cant];
        for (int i = 0; i < arrPasajeros.length; i++) {
            Pasaje pasaje = new Pasaje(nombresAerolineas[rnd.nextInt(cantAerolineas)]);
            arrPasajeros[i] = new Pasajero(i, pasaje, aeropuerto, tren, arrBool[rnd.nextInt(2)], arrBool[rnd.nextInt(2)],hora);
        }
        for (int j = 0; j < arrPasajeros.length; j++) {
            Thread hilo = new Thread(arrPasajeros[j]);
            hilo.start();
        }
    }

    public static Aerolinea[] crearAerolineas(int cantAerolineas, Terminal[] arrTerminales, int cantMaxFila, String[] nombresAerolineas,AtomicInteger hora) {
        Aerolinea[] arrAerolineas = new Aerolinea[cantAerolineas];
        for (int i = 0; i < arrAerolineas.length; i++) {
            arrAerolineas[i] = new Aerolinea(nombresAerolineas[i], cantMaxFila, arrTerminales,hora);
        }
        return arrAerolineas;
    }

    public static Terminal[] crearTerminales(int cantTerminales, int cantPuestoEmb, int cantMaxItemsCinta, int capacidadFreeShop,AtomicInteger hora) {
        FreeShop[] arrFreeShops = new FreeShop[cantTerminales];
        crearFreeShops(arrFreeShops, cantMaxItemsCinta, capacidadFreeShop);
        char[] letrasTerminales = crearLetrasTerminales();
        Terminal[] arrTerminales = new Terminal[cantTerminales];
        int indice = 1;
        for (int i = 0; i < arrTerminales.length; i++) {
            int[] embarques = new int[cantPuestoEmb];
            for (int j = 0; j < embarques.length; j++) {
                embarques[j] = indice;
                indice++;
            }
            arrTerminales[i] = new Terminal(letrasTerminales[i], embarques, arrFreeShops[i],hora);
        }
        return arrTerminales;

    }

    public static void crearFreeShops(FreeShop[] arrFreeS, int cantMaxItemsCinta, int capacidadFreeS) {
        for (int i = 0; i < arrFreeS.length; i++) {
            CajaFreeShop caja1 = new CajaFreeShop(cantMaxItemsCinta);
            CajaFreeShop caja2 = new CajaFreeShop(cantMaxItemsCinta);
            CajaFreeShop[] arrCajas = new CajaFreeShop[2];
            arrCajas[0] = caja1;
            arrCajas[1] = caja2;
            CajeraFreeShop cajera1 = new CajeraFreeShop(arrCajas[0], i);
            CajeraFreeShop cajera2 = new CajeraFreeShop(arrCajas[1], i);
            Thread hiloCajera1 = new Thread(cajera1);
            hiloCajera1.start();
            Thread hiloCajera2 = new Thread(cajera2);
            hiloCajera2.start();
            arrFreeS[i] = new FreeShop(capacidadFreeS, arrCajas);
        }
    }

    public static char[] crearLetrasTerminales() {
        char[] arrLetras = {'A','B','C','D','E','F','G','H','I','J'};
        return arrLetras;
    }

    public static String[] crearNombresAerolineas() {
        String[] arrNombresAero = {"VOLARIS","AEROLINEAS ARG","AIR CANADA","CALAFIA","LAN","AIR QATAR","LATAM","AIR AZUL","SAFARILINK","AIR BOUTIQUE"};
        return arrNombresAero;
    }
}
