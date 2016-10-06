/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package AST;

import paquete.Relacion;
import paquete.Token;

/**
 *
 * @author Xmnz
 */
public class NodoRelacion extends NodoBase
{
     Token alias;

     
     public NodoRelacion(Token valor)
    {
        super(valor);
        alias=null;
        relacion=null;
    }
     
     public NodoRelacion(Token valor, Token alias)
    {
        super(valor);
        this.alias=alias;
        relacion=null;
    }

    
}
