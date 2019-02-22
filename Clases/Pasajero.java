/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemaAeropuerto;

public class Pasajero implements Runnable{
	private String reserva;
	private char terminal;
	private int puestoEmbarque;
	private Aeropuerto aeropuerto;
	
	public Pasajero(String reserva,Aeropuerto aeropuerto) {
		this.reserva=reserva;
		this.aeropuerto=aeropuerto;
	}
	public void run() {
	}
	
	
	public String getReserva() {
		return this.reserva;
	}
	public void setReserva(String reserva) {
		this.reserva=reserva;
	}
	public char getTerminal() {
		return this.terminal;
	}
	public void setTerminal(char terminal) {
		this.terminal=terminal;
	}
	public int1 getPuestoEmbarque() {
		return this.puestoEmbarque;
	}
	public void setPuestoEmbarque(int puestoE) {
		this.puestoEmbarque=puestoE;
	}
	
    
}
