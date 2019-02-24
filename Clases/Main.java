/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.util.Random;

/**
 *
 * @author liamosycka
 */
public class Main {

    public static void main(String[] args) {
        int cantTerminales=3,cantPuestosEmbarque=5,cantAerolineas=5,cantMaxFila=4,capacidadTren=5,cantPasajeros=15;
        String[] nombresAerolineas=crearNombresAerolineas();
        Terminal[]arrTerminales=crearTerminales(cantTerminales,cantPuestosEmbarque);
        Aerolinea[]arrAerolineas=crearAerolineas(cantAerolineas,arrTerminales,cantMaxFila,nombresAerolineas);
        TrenInterno tren=new TrenInterno(capacidadTren,arrTerminales);
        Thread hiloTren=new Thread(tren);
        hiloTren.start();
        Aeropuerto aeropuerto=new Aeropuerto(arrAerolineas,tren);
        ControlTiempo controlTiempo=new ControlTiempo(aeropuerto);
        Thread hiloTiempo= new Thread(controlTiempo);
        hiloTiempo.start();
        crearPasajeros(cantPasajeros,nombresAerolineas,aeropuerto,tren);
        crearGuardias(arrAerolineas,cantAerolineas);

    }
    public static void crearGuardias(Aerolinea[] arrAerolineas,int cant){
        Guardia[] arrGuardias=new Guardia[cant];
        for (int i = 0; i < arrGuardias.length; i++) {
            arrGuardias[i]=new Guardia(arrAerolineas[i]);
        }
        for (int j = 0; j < arrGuardias.length; j++) {
            Thread hiloGuardia=new Thread(arrGuardias[j]);
            hiloGuardia.start();
        }
    }
    public static void crearPasajeros(int cant,String[] nombresAerolineas,Aeropuerto aeropuerto,TrenInterno tren){
        Random rnd=new Random();
        Pasajero[] arrPasajeros=new Pasajero[cant];
        for (int i = 0; i < arrPasajeros.length; i++) {
            Pasaje pasaje=new Pasaje(nombresAerolineas[rnd.nextInt(3)]);
            arrPasajeros[i] = new Pasajero(i,pasaje, aeropuerto, tren);
        }
        for (int j = 0; j < arrPasajeros.length; j++) {
            Thread hilo = new Thread(arrPasajeros[j]);
            hilo.start();
        }
    }
    public static Aerolinea[] crearAerolineas(int cantAerolineas,Terminal[] arrTerminales,int cantMaxFila,String[] nombresAerolineas){
        Aerolinea[] arrAerolineas=new Aerolinea[cantAerolineas];
        for (int i = 0; i <arrAerolineas.length; i++) {
            arrAerolineas[i]=new Aerolinea(nombresAerolineas[i],cantMaxFila,arrTerminales);
        }
        return arrAerolineas;
    }

    public static Terminal[] crearTerminales(int cantTerminales, int cantPuestoEmb) {
        char[] letrasTerminales = crearLetrasTerminales();
        Terminal[] arrTerminales = new Terminal[cantTerminales];
        int indice = 1;
        for (int i = 0; i < arrTerminales.length; i++) {
            int[] embarques = new int[cantPuestoEmb];
            for (int j = 0; j < embarques.length; j++) {
                embarques[j] = indice;
                indice++;
            }
            System.out.println("Por enviar a terminal: "+embarques[0]+embarques[1]+embarques[2]+embarques[3]+embarques[4]);
            arrTerminales[i] = new Terminal(letrasTerminales[i], embarques);
        }
        return arrTerminales;

    }

    public static char[] crearLetrasTerminales() {
        char[] arrLetras = new char[10];
        arrLetras[0] = 'A';
        arrLetras[1] = 'B';
        arrLetras[2] = 'C';
        arrLetras[3] = 'D';
        arrLetras[4] = 'E';
        arrLetras[5] = 'F';
        arrLetras[6] = 'G';
        arrLetras[7] = 'H';
        arrLetras[8] = 'I';
        arrLetras[9] = 'J';
        return arrLetras;
    }
    public static String[] crearNombresAerolineas(){
        String[] arrNombresAero=new String [10];
        arrNombresAero[0]="VOLARIS";
        arrNombresAero[1]="AEROLINEAS ARG";
        arrNombresAero[2]="AIR CANADA";
        arrNombresAero[3]="CALAFIA";
        arrNombresAero[4]="LAN";
        arrNombresAero[5]="AIR QATAR";
        arrNombresAero[6]="LATAM";
        arrNombresAero[7]="AIR AZUL";
        arrNombresAero[8]="SAFARILINK";
        arrNombresAero[9]="AIR BOUTIQUE";
        return arrNombresAero;
    }
}
