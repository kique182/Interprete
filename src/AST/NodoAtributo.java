/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package AST;
import paquete.Token;

/**
 *
 * @author Xmnz
 */
public class NodoAtributo extends NodoBase{
    
   
        Token alias;
    
	public NodoAtributo(Token valor,NodoBase hermano) {
		super(valor);
                this.setHijoIzquierdo(hermano);
	}
        
        public NodoAtributo(Token valor) {
		super(valor);
                this.setHijoIzquierdo(null);
	}
        
        public NodoAtributo(Token alias,Token valor) {
		super(valor);
                this.alias=alias;
                this.setHijoIzquierdo(null);
	}
	
        
    
}
