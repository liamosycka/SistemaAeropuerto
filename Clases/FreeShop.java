/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

public class FreeShop {
    
    private int capacidad;
    private CajaFreeShop[] arrCajas;
    
    public FreeShop(int cap,CajaFreeShop[] arrCajas){
        this.capacidad=cap;
        this.arrCajas=arrCajas;
    }
}
