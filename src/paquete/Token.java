/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package paquete;

/**
 *
 * @author Xmnz
 */
public class Token {
    
    
    int posicionX;
    int posicionY;
    int sym;
    String valor;
    String nombre;
    String tipo;
    

    public Token(String val, int x, int y,String nombre) {
        this.valor = val;
        this.posicionX = x;
        this.posicionY = y;
        this.nombre=nombre;
        tipo=null;
    }
    
    public Token(String val, int x, int y,String nombre,String tipo) {
        this.valor = val;
        this.posicionX = x;
        this.posicionY = y;
        this.nombre=nombre;
        this.tipo=tipo;
    }

    public int getX() {
        return this.posicionX;
    }

    public int getY() {
        return this.posicionY;
    }

    public String getValor() {
        return this.valor;
    }
    
    public String getNombre() {
        return this.nombre;
    }
    
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
}
