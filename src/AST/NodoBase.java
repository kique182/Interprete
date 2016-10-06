package AST;
import paquete.*;
/**
 *
 * @author Javier Moreno
 */
public class NodoBase 
{
    private NodoBase HijoIzquierdo,HijoDerecho;
    private Token token;
    Relacion relacion;
    
    
        
        public NodoBase(Token t) {
		
		token=t;
              
                HijoIzquierdo=null;
                HijoDerecho=null;
	}
        
        public NodoBase() {
		
		token=null;
              
                HijoIzquierdo=null;
                HijoDerecho=null;
	}
        
	       
        public NodoBase getHijoIzquierdo() {
        return HijoIzquierdo;
    }

    public void setHijoIzquierdo(NodoBase HijoIzquierdo) {
        this.HijoIzquierdo = HijoIzquierdo;
    }

    public NodoBase getHijoDerecho() {
        return HijoDerecho;
    }

    public void setHijoDerecho(NodoBase HijoDerecho) {
        this.HijoDerecho = HijoDerecho;
    }
    
    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
    
     public Relacion getRelacion() {
        return relacion;
    }

    public void setRelacion(Relacion relacion) {
        this.relacion = relacion;
    }
}
