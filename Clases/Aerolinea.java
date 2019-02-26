package Clases;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Aerolinea {

    private Lock lock;
    private Condition esperaHall, esperaGuardia;
    private String nombreAerolinea;
    private int cantMaxPuesto, cantActual, cantEnEspera;
    private Semaphore semPuesto;
    private int horaActual;
    private Random rnd;
    private Terminal[] arrTerminales;
    public Aerolinea(String nombre, int cantMax,Terminal[] arrTerminales) {
        this.nombreAerolinea = nombre;
        this.cantMaxPuesto = cantMax;
        this.cantActual = 0;
        this.cantEnEspera = 0;
        this.semPuesto = new Semaphore(1, true);
        this.lock = new ReentrantLock(true);
        this.esperaHall = lock.newCondition();
        this.esperaGuardia = lock.newCondition();
        this.horaActual=0;
        this.rnd=new Random();
        this.arrTerminales=arrTerminales;
    }

    public boolean equals(String nombreAerolinea) {
        return this.nombreAerolinea.equals(nombreAerolinea);
    }

    public  void entrarFilaPuestoAtencion(Pasajero pasajero) {
        this.lock.lock();
        this.cantEnEspera++;
        if(cantEnEspera==1){
         this.esperaGuardia.signal();   
        }
        while (cantActual==cantMaxPuesto) {
            System.out.println((char)27 + "[31mPasajero : "+pasajero.getId()+" en hall espera");
            try {
                this.esperaHall.await();
            } catch (InterruptedException ex) {
                Logger.getLogger(Aerolinea.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println((char)27 + "[34mPasajero : "+pasajero.getId()+" ha entrado a la fila de Aerolinea "+this.nombreAerolinea);
        this.cantEnEspera--;
        cantActual++;
        this.lock.unlock();
    }

    public void obtenerAtencionPuesto(Pasajero pasajero) {
        try {
            semPuesto.acquire();
            System.out.println("    Pasajero : "+pasajero.getId()+" ESTA SIENDO ATENDIDO");
            Pasaje pasaje=pasajero.getPasaje();
            int horaPasaje=this.horaActual+(rnd.nextInt(5))%24;
            Terminal terminalPasaje=arrTerminales[rnd.nextInt(arrTerminales.length-1)];
            int embarquePasaje=terminalPasaje.obtenerPuestoEmbarqueAleatorio();
            pasaje.checkIn(horaPasaje,terminalPasaje ,embarquePasaje);
            
        } catch (InterruptedException ex) {
            Logger.getLogger(Aerolinea.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void salirPuestoAtencion(Pasajero pasajero) {
        this.lock.lock();
        System.out.println((char)27 + "[34Ha salido el pasajero "+pasajero.getId()+" del puesto de atencion de : "+this.nombreAerolinea);
        cantActual--;
        this.esperaGuardia.signal();
        semPuesto.release();
        this.lock.unlock();
    }

    public void hacerPasarPasajero() {
        this.lock.lock();
        //modulo que ejecuta el guardia en su run para ir haciendo pasar a los pasajeros del hall
        while (cantEnEspera == 0||cantActual==cantMaxPuesto) {
            try {
                this.esperaGuardia.await();
            } catch (InterruptedException ex) {
                Logger.getLogger(Aerolinea.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("    Guardia va a hacer pasar a un pasajero del hall al puesto de la aerolinea: "+this.nombreAerolinea);
        this.esperaHall.signal();
        this.lock.unlock();
    }
    public void pasarHora(){
        this.horaActual+=1;
        for (int i = 0; i < arrTerminales.length; i++) {
            arrTerminales[i].pasarHora();
        }
    }
    
}
