/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemaAeropuerto;

public class ControlTiempo implements Runnable{
    private Aeropuerto aeropuerto;
    private int horaActual;
    
    public ControlTiempo(Aeropuerto aero) {
    	this.aeropuerto=aero;
    	this.horaActual=0;
    }
    
    public void run() {
    	while(true) {
    		Thread.sleep(10000);
    		this.horaActual++;
    		if(horaActual==6) {
    			this.aeropuerto.comenzarHorarioAtencion();
    		}
    		if(horaActual==24) {
    			this.horaActual=0;
    		}
    		if(horaActual==22) {
    			this.aeropuerto.finalizarHorarioAtencion();
    		}
    	}
    }
    public int getHora() {
    	return this.horaActual;
    }
}
