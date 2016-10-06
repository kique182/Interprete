/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;
import paquete.Relacion;
import paquete.Token;
/**
 *
 * @author Javier Moreno
 */
public class NodoOpBi extends NodoOperacion
{

    
 

    public NodoOpBi(NodoBase HijoIzquierdo,Token Tipo, NodoBase HijoDerecho) {
        super(Tipo);
        this.setHijoIzquierdo(HijoIzquierdo);
        this.setHijoDerecho(HijoDerecho);
        relacion=null;
    }
    

}
