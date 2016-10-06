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
public class NodoNot extends NodoOperacion{
    

	public NodoNot(Token tipo, NodoBase hijoUnico) {
		super(tipo);
		this.setHijoIzquierdo(hijoUnico);
	}
    
}
