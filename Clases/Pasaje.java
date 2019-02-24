/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

/**
 *
 * @author liamosycka
 */
public class Pasaje {
    private String nombreAerolinea;
    private int horaPartida;
    private Terminal terminal;
    private int puestoEmbarque;
    
    public Pasaje(String aerolinea){
        this.nombreAerolinea=aerolinea;
    }
    public void checkIn(int hora,Terminal terminal,int puestoEmb){
        this.horaPartida=hora;
        this.terminal=terminal;
        this.puestoEmbarque=puestoEmb;
    }
    public String getNombreAerolinea(){
        return this.nombreAerolinea;
    }
    public void setNombreAerolinea(String nombreAero){
        this.nombreAerolinea=nombreAero;
    }
    public int getHoraPartida(){
        return this.horaPartida;
    }
    public void setHoraPartida(int hora){
        this.horaPartida=hora;
    }
    public Terminal getTerminal(){
        return this.terminal;
    }
    public void setTerminal(Terminal terminal){
        this.terminal=terminal;
    }
    public int getPuestoEmbarque(){
        return this.puestoEmbarque;
    }
    public void setPuestoEmbarque(int puesto){
        this.puestoEmbarque=puesto;
    }
}
