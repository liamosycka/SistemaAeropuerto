import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
package SistemaAeropuerto;

public class Aerolinea {
	private Lock lock;
	private Condition esperaHorario,esperaHall,esperaGuardia;
    private String nombreAerolinea;
    private int cantMaxPuesto,int cantActual,int cantEnEspera;
    private Semaphore semCantPuesto,semGuardia,semPuesto;
    private HallCentral hallCentral;
    private boolean pasar=false;
    
    public Aerolinea(String nombre,int cantMax,HallCentral hall) {
    	this.nombreAerolinea=nombre;
    	this.cantMaxPuesto=cantMax;
    	this.cantActual=0;
    	this.cantEnEspera=0;
    	this.semCantPuesto=new Semaphore(1,true);
    	this.semGuardia=new Semaphore(1,true);
    	this.semPuesto=new Semaphore(1,true);
    	this.hallCentral=hall;
    	this.lock=new ReentrantLock(true);
    	this.esperaHorario=lock.newCondition();
    	this.esperaHall=lock.newCondition();
    	this.esperaGuardia=lock.newCondition();
    }
    
    public boolean equals(String nombreAerolinea) {
    	return this.nombreAerolinea.equals(nombreAerolinea);
    }
    
    public synchronized void entrarFilaPuestoAtencion(Pasajero pasajero) {
    	this.cantEnEspera++;
    	this.notifyAll();
    	while(!pasar) {
    		this.esperaHall.await();
    	}
    	this.cantEnEspera--;
    	cantActual++;
    	this.pasar=false;
    }
    public void obtenerAtencionPuesto(Pasajero pasajero) {
    	semPuesto.acquire();
    }
    public synchronized void salirPuestoAtencion(Pasajero pasajero) {
    	cantActual--;
    	this.esperaGuardia.signal();
    	semPuesto.release();
    }
    
    public synchronized void hacerPasarPasajero() {
    	//modulo que ejecuta el guardia en su run para ir haciendo pasar a los pasajeros del hall
    	while(cantActual==cantMaxPuesto||cantEnEspera==0) {
    		this.esperaGuardia.await();
    	}
    	this.pasar=true;
    	this.esperaHall.signal();
    }
}
