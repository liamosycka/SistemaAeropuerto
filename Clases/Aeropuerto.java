/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemaAeropuerto;

public class Aeropuerto {
    private Aerolinea[] arrAerolineas;
    private boolean esHorarioAtencion;
    
    public Aeropuerto(Aerolinea[] arrAerolineas) {
    	this.arrAerolineas=arrAerolineas;
    	this.esHorarioAtencion=false;
    }
    
    public synchronized void entrarAeropuerto(Pasajero pasajero) {
    	String reserva=pasajero.getReserva();
    	Aerolinea aeroIndicada;
    	boolean encontrado=false;
    	int i=0;
    	while(!encontrado) {
    		if(arrAerolineas[i].equals(reserva)) {
    			encontrado=true;
    			aeroIndicada=arrAerolineas[i];
    		}
    		i++;
    	}
    	while(!esHorarioAtencion) {
    		this.wait();
    	}
    	aeroIndicada.entrarFilaPuestoAtencion(pasajero);
    }
    public void comenzarHorarioAtencion() {
    	this.esHorarioAtencion=true;
    }
    public void terminarHorarioAtencion() {
    	this.esHorarioAtencion=false;
    }
}
