/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package AST;
import paquete.*;


/**
 *
 * @author Xmnz
 */
public class Util {
	
	static int sangria = 0;
        
        public static void ImprimirArbol(NodoBase raiz)
        {
            ImprimirNodo(raiz);
            
        
        }
        
        
        public static void ImprimirNodo(NodoBase nodo)
        {
            
            if (nodo instanceof NodoFecha)
            {
               NodoFecha nf= (NodoFecha) nodo;
               
               System.out.print(nf.getToken().getValor());
            
            }
            
            if (nodo instanceof NodoCadena)
            {
               NodoCadena nc= (NodoCadena) nodo;
               
               System.out.print(nc.getToken().getValor());
            
            }
            
            if (nodo instanceof NodoNumero)
            {
               NodoNumero nn= (NodoNumero) nodo;
               
               System.out.print(nn.getToken().getValor());
            
            }
            
            if (nodo instanceof NodoAtributo)
            {
               NodoAtributo na= (NodoAtributo) nodo;
               
               System.out.print(na.getToken().getValor()+" ");
               
               if(na.getHijoIzquierdo()!=null)
               {
                   ImprimirNodo(na.getHijoIzquierdo());
               }
            
            }
            
            if(nodo instanceof NodoNot)
            {
               NodoNot nNot= (NodoNot) nodo;
               
               System.out.print(nNot.getToken().getValor());
               System.out.print("Hijo unico");
               //ImprimirNodo(nNot.getHijoUnico());

                
            }
                
            
            if(nodo instanceof NodoOpBi)
            {
               NodoOpBi nOpBi= (NodoOpBi) nodo;
               
               System.out.println(nOpBi.getToken().getValor());
               System.out.print("Izquierdo ");
               ImprimirNodo(nOpBi.getHijoIzquierdo());
               System.out.println();
               System.out.print("Derecho ");
               ImprimirNodo(nOpBi.getHijoDerecho());

                
            }
        
        }
        
        public static void ImprimirArbol()
        {
        
         
        
        }
	
}