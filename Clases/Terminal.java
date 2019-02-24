/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.util.Random;

public class Terminal {
    private char letraT;
    private int[]embarques;
    private Random rnd;
    public Terminal(char letra,int[]embarques){
        this.letraT=letra;
        this.embarques=embarques;
        rnd=new Random();
    }
    public char getLetraTerminal(){
        return this.letraT;
    }
    public void setLetraTerminal(char letra){
        this.letraT=letra;
    }
    public int obtenerPuestoEmbarqueAleatorio(){
        return embarques[rnd.nextInt(embarques.length-1)];
    }
}
