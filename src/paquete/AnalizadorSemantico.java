/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package paquete;
import AST.*;
import com.google.common.base.Supplier;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Xmnz
 */
public class AnalizadorSemantico {
    
    NodoBase arbol;
    String TIPO, TIPOD;
    public AnalizadorSemantico(NodoBase raiz)
    {
        arbol=raiz;
        Comprobador_Ejecutor(arbol);
    }
    
    public NodoBase Comprobador_Ejecutor(NodoBase nodo)
    {
    
        if (nodo instanceof NodoFecha) {
            NodoFecha nf = (NodoFecha) nodo;

            return nf;

        }

        else if (nodo instanceof NodoCadena) {
            NodoCadena nc = (NodoCadena) nodo;

            return nc;

        }

        else if (nodo instanceof NodoNumero) {
            NodoNumero nn = (NodoNumero) nodo;

            return nn;

        }

        else if (nodo instanceof NodoRelacion) {
            NodoRelacion nr = (NodoRelacion) nodo;
            try {
                nr.setRelacion(cargarRelacion(nr.getToken().getValor()));
            } catch (IOException ex) {
                Logger.getLogger(AnalizadorSemantico.class.getName()).log(Level.SEVERE, null, ex);
            }
            return nr;

        }

        else if (nodo instanceof NodoAtributo) {
            NodoAtributo na = (NodoAtributo) nodo;

            if (na.getHijoIzquierdo() != null) {
                Comprobador_Ejecutor(na.getHijoIzquierdo());
            }
            return na;
        }

        else if (nodo instanceof NodoNot) {

            NodoBase ni = nodo.getHijoIzquierdo();

            if ((Comprobador_Ejecutor(ni) instanceof NodoOpBi) && (ni.getToken().getTipo().compareTo("booleano") == 0)) {
                //implementar operacion negar el hijo izquierdo
                return ni;
            }
               //ImprimirNodo(nNot.getHijoUnico());

        }

        else if (nodo instanceof NodoOpBi) {
            NodoOpBi nopBi = (NodoOpBi) nodo;
            NodoBase ni = nopBi.getHijoIzquierdo();
            NodoBase nd = nopBi.getHijoDerecho();

            if (nopBi.getToken().getNombre().compareTo("UNI") == 0) {
                  ///No es necesario preguntar de que tipo son los hijos ya que la gramatica asegura,1) que tenga hijos y
                // 2)que estos sean o bien un nodoRelacion o un nodoOperacionbinaria relacional

                
                nopBi.setRelacion(Union(Comprobador_Ejecutor(ni), Comprobador_Ejecutor(nd)));
                if (nopBi.getRelacion() != null) {
                    return nopBi;
                } else {
                    System.err.print("Error en operacion UNI");
                    System.exit(0);
                }

            }

            if (nopBi.getToken().getNombre().compareTo("DIF") == 0) {

               nopBi.setRelacion(Diferencia(Comprobador_Ejecutor(ni), Comprobador_Ejecutor(nd)));
                if (nopBi.getRelacion() != null) {
                    return nopBi;
                } else {
                    System.err.print("Error en operacion DIF");
                    System.exit(0);
                }

            }

            if (nopBi.getToken().getNombre().compareTo("PROC") == 0) {

               nopBi.setRelacion(ProductoCartesiano(Comprobador_Ejecutor(ni), Comprobador_Ejecutor(nd)));
                if (nopBi.getRelacion() != null) {
                    return nopBi;
                } else {
                    System.err.print("Error en operacion PROC");
                    System.exit(0);
                }

            }

            if (nopBi.getToken().getNombre().compareTo("INT") == 0) {

                nopBi.setRelacion(Interseccion(Comprobador_Ejecutor(ni), Comprobador_Ejecutor(nd)));
                if (nopBi.getRelacion() != null) {
                    return nopBi;
                } else {
                    System.err.print("Error en operacion INT");
                    System.exit(0);
                }

            }

            if (nopBi.getToken().getNombre().compareTo("PRO") == 0) {

                nopBi.setRelacion(Proyeccion(Comprobador_Ejecutor(ni), Comprobador_Ejecutor(nd)));
                if (nopBi.getRelacion() != null) {
                    return nopBi;
                } else {
                    System.err.print("Error en operacion PRO");
                    System.exit(0);
                }

            }

            if (nopBi.getToken().getNombre().compareTo("SEL") == 0) {

                Relacion r=Comprobador_Ejecutor(nd).getRelacion();
                ni.setRelacion(r);
                
                
                nopBi.setRelacion(Comprobador_Ejecutor(ni).getRelacion());
                
                if (nopBi.getRelacion() != null) {
                    return nopBi;
                } else {
                    System.err.print("Error en operacion SEL");
                    System.exit(0);
                }

            }

            if (nopBi.getToken().getNombre().compareTo("OR") == 0) {

                ni.setRelacion(nopBi.getRelacion());
                nd.setRelacion(nopBi.getRelacion());
                nopBi.setRelacion(Union(Comprobador_Ejecutor(ni), Comprobador_Ejecutor(nd)));
                if (nopBi.getRelacion() != null) {
                    return nopBi;
                } else {
                    System.err.print("Error en operacion OR");
                    System.exit(0);
                }

            }

            if (nopBi.getToken().getNombre().compareTo("AND") == 0) {

                ni.setRelacion(nopBi.getRelacion());
                nd.setRelacion(nopBi.getRelacion());
                nopBi.setRelacion(And(Comprobador_Ejecutor(ni), Comprobador_Ejecutor(nd)));
                if (nopBi.getRelacion() != null) {
                    return nopBi;
                } else {
                    System.err.print("Error en operacion AND");
                    System.exit(0);
                }

            }

            if (nopBi.getToken().getNombre().compareTo("EQ") == 0) {

                    nopBi.setRelacion(Igual(Comprobador_Ejecutor(ni), Comprobador_Ejecutor(nd),nopBi.getRelacion()));
                if (nopBi.getRelacion() != null) {
                    return nopBi;
                } else {
                    System.err.print("Error en operacion EQ");
                    System.exit(0);
                }

            }

            if (nopBi.getToken().getNombre().compareTo("DIFERENTE") == 0) {

                nopBi.setRelacion(Diferente(Comprobador_Ejecutor(ni), Comprobador_Ejecutor(nd),nopBi.getRelacion()));
                if (nopBi.getRelacion() != null) {
                    return nopBi;
                } else {
                    System.err.print("Error en operacion DIFERENTE");
                    System.exit(0);
                }

            }

            if (nopBi.getToken().getNombre().compareTo("MENOR") == 0) {

                nopBi.setRelacion(Menor(Comprobador_Ejecutor(ni), Comprobador_Ejecutor(nd),nopBi.getRelacion()));
                if (nopBi.getRelacion() != null) {
                    return nopBi;
                } else {
                    System.err.print("Error en operacion MENOR");
                    System.exit(0);
                }

            }

            if (nopBi.getToken().getNombre().compareTo("MAYOR") == 0) {

                nd.setRelacion(nopBi.getRelacion());
                nopBi.setRelacion(Mayor(Comprobador_Ejecutor(ni), Comprobador_Ejecutor(nd),nopBi.getRelacion()));
                if (nopBi.getRelacion() != null) {
                    return nopBi;
                } else {
                    System.err.print("Error en operacion Mayor");
                    System.exit(0);
                }

            }

            if (nopBi.getToken().getNombre().compareTo("MENORIGUAL") == 0) {

                if ((Comprobador_Ejecutor(ni) instanceof NodoNumero)
                        || ((Comprobador_Ejecutor(ni) instanceof NodoAtributo) && ni.getToken().getTipo().compareTo("numero") == 0)//Revisa tipos parte izquierda 
                        && (Comprobador_Ejecutor(nd) instanceof NodoNumero)
                        | ((Comprobador_Ejecutor(nd) instanceof NodoAtributo) && ni.getToken().getTipo().compareTo("numero") == 0))//Revisa tipos parte derecha 
                {

                    //Ejecutar_Operacion_UNI()// debe ser implementada
                    return nopBi;
                } else if ((Comprobador_Ejecutor(ni) instanceof NodoNumero)
                        || ((Comprobador_Ejecutor(ni) instanceof NodoAtributo) && ni.getToken().getTipo().compareTo("cadena") == 0)//Revisa tipos parte izquierda 
                        && (Comprobador_Ejecutor(nd) instanceof NodoNumero)
                        | ((Comprobador_Ejecutor(nd) instanceof NodoAtributo) && ni.getToken().getTipo().compareTo("cadena") == 0))//Revisa tipos parte derecha  
                {

                } else if ((Comprobador_Ejecutor(ni) instanceof NodoNumero)
                        || ((Comprobador_Ejecutor(ni) instanceof NodoAtributo) && ni.getToken().getTipo().compareTo("fecha") == 0)//Revisa tipos parte izquierda 
                        && (Comprobador_Ejecutor(nd) instanceof NodoNumero)
                        | ((Comprobador_Ejecutor(nd) instanceof NodoAtributo) && ni.getToken().getTipo().compareTo("fecha") == 0))//Revisa tipos parte derecha  
                {

                } else {
                    System.err.print("Tipos incompatibles");
                }

            }

            if (nopBi.getToken().getNombre().compareTo("MAYORIGUAL") == 0) {

                 nopBi.setRelacion(MayorIgual(Comprobador_Ejecutor(ni), Comprobador_Ejecutor(nd),nopBi.getRelacion()));
                if (nopBi.getRelacion() != null) {
                    return nopBi;
                } else {
                    System.err.print("Error en operacion MAYORIGUAL");
                    System.exit(0);
                }

            }

            if (nopBi.getToken().getNombre().compareTo("RESTA") == 0) {

                nopBi.getToken().valor= ""+Resta(ni,nd, nopBi.getRelacion());
                nopBi.getToken().tipo="numero";
                return nopBi;

            }

            if (nopBi.getToken().getNombre().compareTo("SUMA") == 0) {

                nopBi.getToken().valor= ""+Suma(ni,nd, nopBi.getRelacion());
                nopBi.getToken().tipo="numero";
                return nopBi;
                

            }

            if (nopBi.getToken().getNombre().compareTo("MULTI") == 0) {

                nopBi.getToken().valor= ""+Multiplicacion(ni,nd, nopBi.getRelacion());
                nopBi.getToken().tipo="numero";
                return nopBi;

            }

            if (nopBi.getToken().getNombre().compareTo("DIVI") == 0) {

                nopBi.getToken().valor= ""+Division(ni,nd, nopBi.getRelacion());
                nopBi.getToken().tipo="numero";
                return nopBi;

            }

        }
            
            
        
    return null;
    }
    
    
    
    public Relacion Seleccion(NodoBase na, NodoBase nre)
    {
       NodoBase ni;
        
        if(na instanceof NodoOpBi)
       {
           ni= (NodoOpBi) na;
           if(ni.getToken().nombre.compareTo("EQ")==0)
           {
               if ((Comprobador_Ejecutor(ni.getHijoIzquierdo()) instanceof NodoAtributo) && ni.getHijoIzquierdo().getToken().getTipo().compareTo("numero") == 0
                       
                       && (Comprobador_Ejecutor(ni.getHijoIzquierdo()) instanceof NodoAtributo && ni.getHijoIzquierdo().getToken().getTipo().compareTo("numero") == 0 ||
                            Comprobador_Ejecutor(ni.getHijoIzquierdo()) instanceof NodoNumero))
                       
               {
               
                  ni.setRelacion(Igual(ni.getHijoIzquierdo(),ni.getHijoDerecho(),nre.getRelacion()));
               
               }
               
               else
               {
               
                   System.err.println("Tipos incompatbles para el operador EQ");
                   System.exit(0);
               
               }
           }
       
       }
        
        
       return null;
        
    }
    
    public Relacion Igual(NodoBase ni, NodoBase nd, Relacion relacion) {
        
        Table<Integer, String, Object> relacionTemporal = HashBasedTable.create();
        int j = 0;
        if (checkTipos(ni, nd, relacion.getAtributos(), relacion.getTipos())) {

            if (ni instanceof NodoAtributo && nd instanceof NodoAtributo) {
                
                for (int i = 0; i < relacion.getDatos().size() / relacion.getAtributos().length; i++) {
                    
                    
                    if(relacion.getDatos().get(i,ni.getToken().getValor()).equals(relacion.getDatos().get(i,nd.getToken().getValor())))
                    {
                        
                        for(int col=0;col<relacion.getAtributos().length;col++)
                        {
                            relacionTemporal.put(j, relacion.getAtributos()[col],relacion.getDatos().get(i,relacion.getAtributos()[col]));
                            
                        }
                        j++;
                    }
                }

            }
            
            else
            {
            
            for (int i = 0; i < relacion.getDatos().size() / relacion.getAtributos().length; i++) {
                
                double temp;String tempS=nd.getToken().getValor();Date tempD=null;
                
                if(relacion.getDatos().get(i, ni.getToken().getValor()) instanceof Double)
                {
                   temp = Double.parseDouble(nd.getToken().getValor());
                   if (relacion.getDatos().get(i, ni.getToken().getValor()).equals(temp)) {
                    for (int a = 0; a < relacion.getAtributos().length; a++) {
                        relacionTemporal.put(j, relacion.getAtributos()[a], relacion.getDatos().get(i, relacion.getAtributos()[a]));
                        
                    }
                    j++;
                }
                }
                
                else if(relacion.getDatos().get(i, ni.getToken().getValor()) instanceof String)
                         if (relacion.getDatos().get(i, ni.getToken().getValor()).equals(tempS)) {
                    for (int a = 0; a < relacion.getAtributos().length; a++) {
                        relacionTemporal.put(j, relacion.getAtributos()[a], relacion.getDatos().get(i, relacion.getAtributos()[a]));
                        
                    }
                    j++;
                         }
                
               
                
                else if(relacion.getDatos().get(i, ni.getToken().getValor()) instanceof Date)
                {
                    DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
                    try {
                        tempD = df.parse(nd.getToken().getValor());
                    } catch (ParseException ex) {
                        Logger.getLogger(AnalizadorSemantico.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    if(relacion.getDatos().get(i, ni.getToken().getValor()) instanceof String)
                         if (relacion.getDatos().get(i, ni.getToken().getValor()).equals(tempD)) {
                    for (int a = 0; a < relacion.getAtributos().length; a++) {
                        relacionTemporal.put(j, relacion.getAtributos()[a], relacion.getDatos().get(i, relacion.getAtributos()[a]));
                        
                    }
                    j++;
                         }
                    
                    
                }

              

            }
            }

        }
        if(!relacionTemporal.isEmpty())
            return new Relacion("",relacion.getAtributos(),relacion.getTipos(),relacionTemporal);
        
        else return null;

       
    }
    
    public Relacion Diferente(NodoBase ni, NodoBase nd, Relacion relacion) {
        
        Table<Integer, String, Object> relacionTemporal = HashBasedTable.create();
        int j = 0;
        if (checkTipos(ni, nd, relacion.getAtributos(), relacion.getTipos())) {

            if (ni instanceof NodoAtributo && nd instanceof NodoAtributo) {
                
                for (int i = 0; i < relacion.getDatos().size() / relacion.getAtributos().length; i++) {
                    
                    
                    if(!relacion.getDatos().get(i,ni.getToken().getValor()).equals(relacion.getDatos().get(i,nd.getToken().getValor())))
                    {
                        
                        for(int col=0;col<relacion.getAtributos().length;col++)
                        {
                            relacionTemporal.put(j, relacion.getAtributos()[col],relacion.getDatos().get(i,relacion.getAtributos()[col]));
                            
                        }
                        j++;
                    }
                }

            }
            
            else
            {
            
            for (int i = 0; i < relacion.getDatos().size() / relacion.getAtributos().length; i++) {
                
                double temp;String tempS=nd.getToken().getValor();Date tempD=null;
                
                if(relacion.getDatos().get(i, ni.getToken().getValor()) instanceof Double)
                {
                   temp = Double.parseDouble(nd.getToken().getValor());
                   if (!relacion.getDatos().get(i, ni.getToken().getValor()).equals(temp)) {
                    for (int a = 0; a < relacion.getAtributos().length; a++) {
                        relacionTemporal.put(j, relacion.getAtributos()[a], relacion.getDatos().get(i, relacion.getAtributos()[a]));
                        
                    }
                    j++;
                }
                }
                
                else if(relacion.getDatos().get(i, ni.getToken().getValor()) instanceof String)
                         if (!relacion.getDatos().get(i, ni.getToken().getValor()).equals(tempS)) {
                    for (int a = 0; a < relacion.getAtributos().length; a++) {
                        relacionTemporal.put(j, relacion.getAtributos()[a], relacion.getDatos().get(i, relacion.getAtributos()[a]));
                        
                    }
                    j++;
                         }
                
               
                
                else if(relacion.getDatos().get(i, ni.getToken().getValor()) instanceof Date)
                {
                    DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
                    try {
                        tempD = df.parse(nd.getToken().getValor());
                    } catch (ParseException ex) {
                        Logger.getLogger(AnalizadorSemantico.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    if(relacion.getDatos().get(i, ni.getToken().getValor()) instanceof String)
                         if (!relacion.getDatos().get(i, ni.getToken().getValor()).equals(tempD)) {
                    for (int a = 0; a < relacion.getAtributos().length; a++) {
                        relacionTemporal.put(j, relacion.getAtributos()[a], relacion.getDatos().get(i, relacion.getAtributos()[a]));
                        
                    }
                    j++;
                         }
                    
                    
                }

              

            }
            }

        }
        if(!relacionTemporal.isEmpty())
            return new Relacion("",relacion.getAtributos(),relacion.getTipos(),relacionTemporal);
        
        else return null;

       
    }
    
     public Relacion Mayor(NodoBase ni, NodoBase nd, Relacion relacion) {
        
        Table<Integer, String, Object> relacionTemporal = HashBasedTable.create();
        int j = 0;
        if (checkTipos(ni, nd, relacion.getAtributos(), relacion.getTipos())) {

            if (ni instanceof NodoAtributo && nd instanceof NodoAtributo) {
                
                if(relacion.getDatos().column(ni.getToken().getValor()).get(0) instanceof Double)
                { 
                for (int i = 0; i < relacion.getDatos().size() / relacion.getAtributos().length; i++) {
                    
                    if((Double)relacion.getDatos().get(i,ni.getToken().getValor()) > (Double)(relacion.getDatos().get(i,nd.getToken().getValor())))
                    {
                        
                        for(int col=0;col<relacion.getAtributos().length;col++)
                        {
                            relacionTemporal.put(j, relacion.getAtributos()[col],relacion.getDatos().get(i,relacion.getAtributos()[col]));
                            
                        }
                        j++;
                    }
                }
                }
                
                if(relacion.getDatos().column(ni.getToken().getValor()).get(0) instanceof Date)
                { 
                for (int i = 0; i < relacion.getDatos().size() / relacion.getAtributos().length; i++) {
                    
                    if(((Date)relacion.getDatos().get(i,ni.getToken().getValor())).after((Date)(relacion.getDatos().get(i,nd.getToken().getValor()))))
                    {
                        
                        for(int col=0;col<relacion.getAtributos().length;col++)
                        {
                            relacionTemporal.put(j, relacion.getAtributos()[col],relacion.getDatos().get(i,relacion.getAtributos()[col]));
                            
                        }
                        j++;
                    }
                }
                }
                
                if(relacion.getDatos().column(ni.getToken().getValor()).get(0) instanceof String)
                { 
                for (int i = 0; i < relacion.getDatos().size() / relacion.getAtributos().length; i++) {
                    
                    if(((String)relacion.getDatos().get(i,ni.getToken().getValor())).compareTo((String)(relacion.getDatos().get(i,nd.getToken().getValor())))>0)
                    {
                        
                        for(int col=0;col<relacion.getAtributos().length;col++)
                        {
                            relacionTemporal.put(j, relacion.getAtributos()[col],relacion.getDatos().get(i,relacion.getAtributos()[col]));
                            
                        }
                        j++;
                    }
                }
                }

            }
            
            else
            {
            
            for (int i = 0; i < relacion.getDatos().size() / relacion.getAtributos().length; i++) {
                
                double temp;String tempS=nd.getToken().getValor();Date tempD=null;
                
                if(relacion.getDatos().get(i, ni.getToken().getValor()) instanceof Double)
                {
                   temp = Double.parseDouble(nd.getToken().getValor());
                   if ((Double)relacion.getDatos().get(i, ni.getToken().getValor()) > (temp)) {
                    for (int a = 0; a < relacion.getAtributos().length; a++) {
                        relacionTemporal.put(j, relacion.getAtributos()[a], relacion.getDatos().get(i, relacion.getAtributos()[a]));
                        
                    }
                    j++;
                }
                }
                
                else if(relacion.getDatos().get(i, ni.getToken().getValor()) instanceof String)
                         if (((String)relacion.getDatos().get(i, ni.getToken().getValor())).compareTo(tempS)>0) {
                    for (int a = 0; a < relacion.getAtributos().length; a++) {
                        relacionTemporal.put(j, relacion.getAtributos()[a], relacion.getDatos().get(i, relacion.getAtributos()[a]));
                        
                    }
                    j++;
                         }
                
               
                
                else if(relacion.getDatos().get(i, ni.getToken().getValor()) instanceof Date)
                {
                    DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
                    try {
                        tempD = df.parse(nd.getToken().getValor());
                    } catch (ParseException ex) {
                        Logger.getLogger(AnalizadorSemantico.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    if(relacion.getDatos().get(i, ni.getToken().getValor()) instanceof String)
                         if (((Date)relacion.getDatos().get(i, ni.getToken().getValor())).after(tempD)) {
                    for (int a = 0; a < relacion.getAtributos().length; a++) {
                        relacionTemporal.put(j, relacion.getAtributos()[a], relacion.getDatos().get(i, relacion.getAtributos()[a]));
                        
                    }
                    j++;
                         }
                    
                    
                }

              

            }
            }

        }
        if(!relacionTemporal.isEmpty())
            return new Relacion("",relacion.getAtributos(),relacion.getTipos(),relacionTemporal);
        
        else return null;

       
    }
      public Relacion MayorIgual(NodoBase ni, NodoBase nd, Relacion relacion) {
        
        Table<Integer, String, Object> relacionTemporal = HashBasedTable.create();
        int j = 0;
        if (checkTipos(ni, nd, relacion.getAtributos(), relacion.getTipos())) {

            if (ni instanceof NodoAtributo && nd instanceof NodoAtributo) {
                
                if(relacion.getDatos().column(ni.getToken().getValor()).get(0) instanceof Double)
                { 
                for (int i = 0; i < relacion.getDatos().size() / relacion.getAtributos().length; i++) {
                    
                    if((Double)relacion.getDatos().get(i,ni.getToken().getValor()) >= (Double)(relacion.getDatos().get(i,nd.getToken().getValor())))
                    {
                        
                        for(int col=0;col<relacion.getAtributos().length;col++)
                        {
                            relacionTemporal.put(j, relacion.getAtributos()[col],relacion.getDatos().get(i,relacion.getAtributos()[col]));
                            
                        }
                        j++;
                    }
                }
                }
                
                if(relacion.getDatos().column(ni.getToken().getValor()).get(0) instanceof Date)
                { 
                for (int i = 0; i < relacion.getDatos().size() / relacion.getAtributos().length; i++) {
                    
                    if(((Date)relacion.getDatos().get(i,ni.getToken().getValor())).after((Date)(relacion.getDatos().get(i,nd.getToken().getValor())))
                            ||((Date)relacion.getDatos().get(i,ni.getToken().getValor())).equals((Date)(relacion.getDatos().get(i,nd.getToken().getValor()))))
                    {
                        
                        for(int col=0;col<relacion.getAtributos().length;col++)
                        {
                            relacionTemporal.put(j, relacion.getAtributos()[col],relacion.getDatos().get(i,relacion.getAtributos()[col]));
                            
                        }
                        j++;
                    }
                }
                }
                
                if(relacion.getDatos().column(ni.getToken().getValor()).get(0) instanceof String)
                { 
                for (int i = 0; i < relacion.getDatos().size() / relacion.getAtributos().length; i++) {
                    
                    if(((String)relacion.getDatos().get(i,ni.getToken().getValor())).compareTo((String)(relacion.getDatos().get(i,nd.getToken().getValor())))>=0)
                    {
                        
                        for(int col=0;col<relacion.getAtributos().length;col++)
                        {
                            relacionTemporal.put(j, relacion.getAtributos()[col],relacion.getDatos().get(i,relacion.getAtributos()[col]));
                            
                        }
                        j++;
                    }
                }
                }

            }
            
            else
            {
            
            for (int i = 0; i < relacion.getDatos().size() / relacion.getAtributos().length; i++) {
                
                double temp;String tempS=nd.getToken().getValor();Date tempD=null;
                
                if(relacion.getDatos().get(i, ni.getToken().getValor()) instanceof Double)
                {
                   temp = Double.parseDouble(nd.getToken().getValor());
                   if ((Double)relacion.getDatos().get(i, ni.getToken().getValor()) >= (temp)) {
                    for (int a = 0; a < relacion.getAtributos().length; a++) {
                        relacionTemporal.put(j, relacion.getAtributos()[a], relacion.getDatos().get(i, relacion.getAtributos()[a]));
                        
                    }
                    j++;
                }
                }
                
                else if(relacion.getDatos().get(i, ni.getToken().getValor()) instanceof String)
                         if (((String)relacion.getDatos().get(i, ni.getToken().getValor())).compareTo(tempS)>=0) {
                    for (int a = 0; a < relacion.getAtributos().length; a++) {
                        relacionTemporal.put(j, relacion.getAtributos()[a], relacion.getDatos().get(i, relacion.getAtributos()[a]));
                        
                    }
                    j++;
                         }
                
               
                
                else if(relacion.getDatos().get(i, ni.getToken().getValor()) instanceof Date)
                {
                    DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
                    try {
                        tempD = df.parse(nd.getToken().getValor());
                    } catch (ParseException ex) {
                        Logger.getLogger(AnalizadorSemantico.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    if(relacion.getDatos().get(i, ni.getToken().getValor()) instanceof String)
                         if (((Date)relacion.getDatos().get(i, ni.getToken().getValor())).after(tempD)
                                 ||((Date)relacion.getDatos().get(i, ni.getToken().getValor())).equals(tempD)) {
                    for (int a = 0; a < relacion.getAtributos().length; a++) {
                        relacionTemporal.put(j, relacion.getAtributos()[a], relacion.getDatos().get(i, relacion.getAtributos()[a]));
                        
                    }
                    j++;
                         }
                    
                    
                }

              

            }
            }

        }
        if(!relacionTemporal.isEmpty())
            return new Relacion("",relacion.getAtributos(),relacion.getTipos(),relacionTemporal);
        
        else return null;

       
    }
      public Relacion Menor(NodoBase ni, NodoBase nd, Relacion relacion) {
        
        Table<Integer, String, Object> relacionTemporal = HashBasedTable.create();
        int j = 0;
        if (checkTipos(ni, nd, relacion.getAtributos(), relacion.getTipos())) {

            if (ni instanceof NodoAtributo && nd instanceof NodoAtributo) {
                
                if(relacion.getDatos().column(ni.getToken().getValor()).get(0) instanceof Double)
                { 
                for (int i = 0; i < relacion.getDatos().size() / relacion.getAtributos().length; i++) {
                    
                    if((Double)relacion.getDatos().get(i,ni.getToken().getValor()) < (Double)(relacion.getDatos().get(i,nd.getToken().getValor())))
                    {
                        
                        for(int col=0;col<relacion.getAtributos().length;col++)
                        {
                            relacionTemporal.put(j, relacion.getAtributos()[col],relacion.getDatos().get(i,relacion.getAtributos()[col]));
                            
                        }
                        j++;
                    }
                }
                }
                
                if(relacion.getDatos().column(ni.getToken().getValor()).get(0) instanceof Date)
                { 
                for (int i = 0; i < relacion.getDatos().size() / relacion.getAtributos().length; i++) {
                    
                    if(((Date)relacion.getDatos().get(i,ni.getToken().getValor())).before((Date)(relacion.getDatos().get(i,nd.getToken().getValor()))))
                    {
                        
                        for(int col=0;col<relacion.getAtributos().length;col++)
                        {
                            relacionTemporal.put(j, relacion.getAtributos()[col],relacion.getDatos().get(i,relacion.getAtributos()[col]));
                            
                        }
                        j++;
                    }
                }
                }
                
                if(relacion.getDatos().column(ni.getToken().getValor()).get(0) instanceof String)
                { 
                for (int i = 0; i < relacion.getDatos().size() / relacion.getAtributos().length; i++) {
                    
                    if(((String)relacion.getDatos().get(i,ni.getToken().getValor())).compareTo((String)(relacion.getDatos().get(i,nd.getToken().getValor())))<0)
                    {
                        
                        for(int col=0;col<relacion.getAtributos().length;col++)
                        {
                            relacionTemporal.put(j, relacion.getAtributos()[col],relacion.getDatos().get(i,relacion.getAtributos()[col]));
                            
                        }
                        j++;
                    }
                }
                }

            }
            
            else
            {
            
            for (int i = 0; i < relacion.getDatos().size() / relacion.getAtributos().length; i++) {
                
                double temp;String tempS=nd.getToken().getValor();Date tempD=null;
                
                if(relacion.getDatos().get(i, ni.getToken().getValor()) instanceof Double)
                {
                   temp = Double.parseDouble(nd.getToken().getValor());
                   if ((Double)relacion.getDatos().get(i, ni.getToken().getValor()) < (temp)) {
                    for (int a = 0; a < relacion.getAtributos().length; a++) {
                        relacionTemporal.put(j, relacion.getAtributos()[a], relacion.getDatos().get(i, relacion.getAtributos()[a]));
                        
                    }
                    j++;
                }
                }
                
                else if(relacion.getDatos().get(i, ni.getToken().getValor()) instanceof String)
                         if (((String)relacion.getDatos().get(i, ni.getToken().getValor())).compareTo(tempS)<0) {
                    for (int a = 0; a < relacion.getAtributos().length; a++) {
                        relacionTemporal.put(j, relacion.getAtributos()[a], relacion.getDatos().get(i, relacion.getAtributos()[a]));
                        
                    }
                    j++;
                         }
                
               
                
                else if(relacion.getDatos().get(i, ni.getToken().getValor()) instanceof Date)
                {
                    DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
                    try {
                        tempD = df.parse(nd.getToken().getValor());
                    } catch (ParseException ex) {
                        Logger.getLogger(AnalizadorSemantico.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    if(relacion.getDatos().get(i, ni.getToken().getValor()) instanceof String)
                         if (((Date)relacion.getDatos().get(i, ni.getToken().getValor())).before(tempD)) {
                    for (int a = 0; a < relacion.getAtributos().length; a++) {
                        relacionTemporal.put(j, relacion.getAtributos()[a], relacion.getDatos().get(i, relacion.getAtributos()[a]));
                        
                    }
                    j++;
                         }
                    
                    
                }

              

            }
            }

        }
        if(!relacionTemporal.isEmpty())
            return new Relacion("",relacion.getAtributos(),relacion.getTipos(),relacionTemporal);
        
        else return null;

       
    }
      public Relacion MenorIgual(NodoBase ni, NodoBase nd, Relacion relacion) {
        
        Table<Integer, String, Object> relacionTemporal = HashBasedTable.create();
        int j = 0;
        if (checkTipos(ni, nd, relacion.getAtributos(), relacion.getTipos())) {

            if (ni instanceof NodoAtributo && nd instanceof NodoAtributo) {
                
                if(relacion.getDatos().column(ni.getToken().getValor()).get(0) instanceof Double)
                { 
                for (int i = 0; i < relacion.getDatos().size() / relacion.getAtributos().length; i++) {
                    
                    if((Double)relacion.getDatos().get(i,ni.getToken().getValor()) <= (Double)(relacion.getDatos().get(i,nd.getToken().getValor())))
                    {
                        
                        for(int col=0;col<relacion.getAtributos().length;col++)
                        {
                            relacionTemporal.put(j, relacion.getAtributos()[col],relacion.getDatos().get(i,relacion.getAtributos()[col]));
                            
                        }
                        j++;
                    }
                }
                }
                
                if(relacion.getDatos().column(ni.getToken().getValor()).get(0) instanceof Date)
                { 
                for (int i = 0; i < relacion.getDatos().size() / relacion.getAtributos().length; i++) {
                    
                    if(((Date)relacion.getDatos().get(i,ni.getToken().getValor())).before((Date)(relacion.getDatos().get(i,nd.getToken().getValor())))
                            ||((Date)relacion.getDatos().get(i,ni.getToken().getValor())).equals((Date)(relacion.getDatos().get(i,nd.getToken().getValor()))))
                    {
                        
                        for(int col=0;col<relacion.getAtributos().length;col++)
                        {
                            relacionTemporal.put(j, relacion.getAtributos()[col],relacion.getDatos().get(i,relacion.getAtributos()[col]));
                            
                        }
                        j++;
                    }
                }
                }
                
                if(relacion.getDatos().column(ni.getToken().getValor()).get(0) instanceof String)
                { 
                for (int i = 0; i < relacion.getDatos().size() / relacion.getAtributos().length; i++) {
                    
                    if(((String)relacion.getDatos().get(i,ni.getToken().getValor())).compareTo((String)(relacion.getDatos().get(i,nd.getToken().getValor())))<=0)
                    {
                        
                        for(int col=0;col<relacion.getAtributos().length;col++)
                        {
                            relacionTemporal.put(j, relacion.getAtributos()[col],relacion.getDatos().get(i,relacion.getAtributos()[col]));
                            
                        }
                        j++;
                    }
                }
                }

            }
            
            else
            {
            
            for (int i = 0; i < relacion.getDatos().size() / relacion.getAtributos().length; i++) {
                
                double temp;String tempS=nd.getToken().getValor();Date tempD=null;
                
                if(relacion.getDatos().get(i, ni.getToken().getValor()) instanceof Double)
                {
                   temp = Double.parseDouble(nd.getToken().getValor());
                   if ((Double)relacion.getDatos().get(i, ni.getToken().getValor()) <= (temp)) {
                    for (int a = 0; a < relacion.getAtributos().length; a++) {
                        relacionTemporal.put(j, relacion.getAtributos()[a], relacion.getDatos().get(i, relacion.getAtributos()[a]));
                        
                    }
                    j++;
                }
                }
                
                else if(relacion.getDatos().get(i, ni.getToken().getValor()) instanceof String)
                         if (((String)relacion.getDatos().get(i, ni.getToken().getValor())).compareTo(tempS)<=0) {
                    for (int a = 0; a < relacion.getAtributos().length; a++) {
                        relacionTemporal.put(j, relacion.getAtributos()[a], relacion.getDatos().get(i, relacion.getAtributos()[a]));
                        
                    }
                    j++;
                         }
                
               
                
                else if(relacion.getDatos().get(i, ni.getToken().getValor()) instanceof Date)
                {
                    DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
                    try {
                        tempD = df.parse(nd.getToken().getValor());
                    } catch (ParseException ex) {
                        Logger.getLogger(AnalizadorSemantico.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    if(relacion.getDatos().get(i, ni.getToken().getValor()) instanceof String)
                         if (((Date)relacion.getDatos().get(i, ni.getToken().getValor())).before(tempD)
                                 ||((Date)relacion.getDatos().get(i, ni.getToken().getValor())).equals(tempD)) {
                    for (int a = 0; a < relacion.getAtributos().length; a++) {
                        relacionTemporal.put(j, relacion.getAtributos()[a], relacion.getDatos().get(i, relacion.getAtributos()[a]));
                        
                    }
                    j++;
                         }
                    
                    
                }

              

            }
            }

        }
        if(!relacionTemporal.isEmpty())
            return new Relacion("",relacion.getAtributos(),relacion.getTipos(),relacionTemporal);
        
        else return null;

       
    }
    
      public Integer Suma(NodoBase ni, NodoBase nd,Relacion relacion)
      {
      
          if(ni instanceof NodoAtributo)
          {
              
              
              checkTipos(ni,nd,relacion.getAtributos(),relacion.getTipos());
              
              if(ni.getToken().getTipo().equals("numero") && nd.getToken().getTipo().equals("numero"))
              {
                  return Integer.parseInt(ni.getToken().getValor())+Integer.parseInt(nd.getToken().getValor());
              
              }
              
              else
              {
              System.err.println("Tipos incompatibles");
              System.exit(0);
              }
          
          }
          
          else
          {
              
              if(ni.getToken().getTipo().equals("numero") && nd.getToken().getTipo().equals("numero"))
              {
                  return Integer.parseInt(ni.getToken().getValor())+Integer.parseInt(nd.getToken().getValor());
              
              }
              
              else
              {
              System.err.println("Tipos incompatibles");
              System.exit(0);
              }
          
          
          }

      return 1;
      }
      
      public Integer Resta(NodoBase ni, NodoBase nd,Relacion relacion)
      {
      
          if(ni instanceof NodoAtributo)
          {
              
              
              checkTipos(ni,nd,relacion.getAtributos(),relacion.getTipos());
              
              if(ni.getToken().getTipo().equals("numero") && nd.getToken().getTipo().equals("numero"))
              {
                  return Integer.parseInt(ni.getToken().getValor())-Integer.parseInt(nd.getToken().getValor());
              
              }
              
              else
              {
              System.err.println("Tipos incompatibles");
              System.exit(0);
              }
          
          }
          
          else
          {
              
              if(ni.getToken().getTipo().equals("numero") && nd.getToken().getTipo().equals("numero"))
              {
                  return Integer.parseInt(ni.getToken().getValor())-Integer.parseInt(nd.getToken().getValor());
              
              }
              
              else
              {
              System.err.println("Tipos incompatibles");
              System.exit(0);
              }
          
          
          }

      return 1;
      }
      
      public Integer Multiplicacion(NodoBase ni, NodoBase nd,Relacion relacion)
      {
      
          if(ni instanceof NodoAtributo)
          {
              
              
              checkTipos(ni,nd,relacion.getAtributos(),relacion.getTipos());
              
              if(ni.getToken().getTipo().equals("numero") && nd.getToken().getTipo().equals("numero"))
              {
                  return (Integer.parseInt(ni.getToken().getValor())*Integer.parseInt(nd.getToken().getValor()));
              
              }
              
              else
              {
              System.err.println("Tipos incompatibles");
              System.exit(0);
              }
          
          }
          
          else
          {
              
              if(ni.getToken().getTipo().equals("numero") && nd.getToken().getTipo().equals("numero"))
              {
                  return Integer.parseInt(ni.getToken().getValor())*Integer.parseInt(nd.getToken().getValor());
              
              }
              
              else
              {
              System.err.println("Tipos incompatibles");
              System.exit(0);
              }
          
          
          }

      return 1;
      }
      
      public Integer Division(NodoBase ni, NodoBase nd,Relacion relacion)
      {
      
          if(ni instanceof NodoAtributo)
          {
              
              
              checkTipos(ni,nd,relacion.getAtributos(),relacion.getTipos());
              
              if(ni.getToken().getTipo().equals("numero") && nd.getToken().getTipo().equals("numero"))
              {
                  return (int)(Integer.parseInt(ni.getToken().getValor())/Integer.parseInt(nd.getToken().getValor()));
              
              }
              
              else
              {
              System.err.println("Tipos incompatibles");
              System.exit(0);
              }
          
          }
          
          else
          {
              
              if(ni.getToken().getTipo().equals("numero") && nd.getToken().getTipo().equals("numero"))
              {
                  return Integer.parseInt(ni.getToken().getValor())/Integer.parseInt(nd.getToken().getValor());
              
              }
              
              else
              {
              System.err.println("Tipos incompatibles");
              System.exit(0);
              }
          
          
          }

      return 1;
      }
      
      public Relacion And(NodoBase ni, NodoBase nd)
      {
      
        NodoBase temp= new NodoBase();
        temp.setRelacion(Diferencia(ni,nd));
        Relacion r=Diferencia(ni,temp);
        String columna[]=r.getAtributos();
        String tipo[]=r.getTipos();
        String nombreRelacion="";
        
        return new Relacion(nombreRelacion, columna, tipo, r.getDatos());
      }
      
      public Relacion Or(NodoBase ni, NodoBase nd)
      {
          
          return null;
      }
      
      
    public Relacion Proyeccion(NodoBase na, NodoBase nre)
    {
        
        
        
        NodoAtributo temporal = (NodoAtributo) na;
        NodoBase nr;
        
        if(nre instanceof NodoRelacion)
            nr=(NodoRelacion) nre;
        else 
            nr=(NodoOpBi) nre;
        
        String nombreRelacion="";
        
        
        Table<Integer, String, Object> relacionTemporal = HashBasedTable.create();
        
        String atr="",tipo="";
        while(temporal!=null)
        {
            //obtener el atributo de la tabla que se desea proyectar
            String columna=temporal.getToken().getValor();
            String tip=temporal.getToken().getTipo();
            
            //artilugio para ir almacenando todos los atributos que tendra la nueva tabla
            atr=atr.concat(columna+",");
            tipo=tipo.concat(tip+",");
            //Pregunto si la columna existe, es decir aqui se detecta un error de semantica al saber si el atributo esta 
            //declarado en la relacion
            if(nr.getRelacion().datos.containsColumn(columna))
            {
                //agregar columna a la relacion temporal
                //Table c=(Table) nr.getRelacion().datos.column(columna);
                //relacionTemporal.putAll(c);
                
                for(int i=0;i<nr.getRelacion().datos.column(columna).size();i++)
                {
                    relacionTemporal.put(i,columna,nr.getRelacion().datos.get(i, columna));
                }
                
                //tipo=tipo.concat();
                temporal=(NodoAtributo)temporal.getHijoIzquierdo();
                
            }
            
            else
            {
                System.err.print("El atributo '"+columna+"', no se encuentra definido en la relacion '"+"', "+nr.getRelacion().getNombreRelacion());
                System.exit(0);
            }
            
            
            
        
        }
        atr=atr.substring(0, atr.length()-1);
        tipo=tipo.substring(0, tipo.length()-1);
        return new Relacion(nombreRelacion,atr.split(","),tipo.split(","),relacionTemporal);
        
    }
    
    public Relacion Union(NodoBase ni, NodoBase nd)
    {
        //Verifica que la cantidad de columnas de las tablas sean iguales
        String nombreRelacion="";
        int li=ni.getRelacion().getAtributos().length;
        int ld=nd.getRelacion().getAtributos().length;
        String tipo[]=ni.getRelacion().getTipos();
        Table<Integer, String, Object> relacionTemporal = HashBasedTable.create();
        String columna[]=null;
        
        if(li==ld)
        {
           
            //Verifica que los tipos de las columnas sean iguales
            columna=new String [ni.getRelacion().getAtributos().length];
            for(int i=0;i<ni.getRelacion().getAtributos().length;i++)
           {
               if(ni.getRelacion().getTipos()[i].compareTo(nd.getRelacion().getTipos()[i])!=0)
               {
                   System.err.print("La operacion union requiere que los atributos posean el mismo dominio");
                   System.exit(0);
               }
               //verifica que los atributos se llamen igual para asignar el nombre de la
               //columna de la nueva relacion, en caso contrario junta los dos numbres
               else if(ni.getRelacion().getAtributos()[i].compareTo(nd.getRelacion().getAtributos()[i])==0)
               {
                    columna[i]=ni.getRelacion().getAtributos()[i];
               }
               else
               {
                   columna[i]=ni.getRelacion().getAtributos()[i].concat(nd.getRelacion().getAtributos()[i]);
               
               }
           }
           
          
           int a=ni.getRelacion().getDatos().size()/ni.getRelacion().getAtributos().length;
           int b=nd.getRelacion().getDatos().size()/nd.getRelacion().getAtributos().length;
           
          
          int filaB;
          for(int j=0;j<ni.getRelacion().getAtributos().length;j++)
          { 
           filaB=0;   
           for(int fila=0;fila<a+b;fila++)
           {
               Object valor;
               
               if(fila<a)
               {
                valor=ni.getRelacion().getDatos().get(fila, ni.getRelacion().getAtributos()[j]);
                relacionTemporal.put(fila, columna[j],valor);
                
               }
               
               else
               {
                  
                  valor=nd.getRelacion().getDatos().get(filaB, nd.getRelacion().getAtributos()[j]);
                  relacionTemporal.put(fila, columna[j],valor);
                  filaB++;
               }
               
               
               
           }
           
              
           
          } 
        }
        else
        {
           System.err.print("La operacion union requiere que las relaciones involucradas posean el mismo numero de atributos");
           System.exit(0);
        }
        
        Relacion temp=new Relacion(nombreRelacion,columna,tipo,relacionTemporal);
        temp=eliminarDuplicados(temp);
        return temp;
    }
    
    public Relacion Diferencia(NodoBase ni, NodoBase nd)
    {
        //Verifica que la cantidad de columnas de las tablas sean iguales
        String nombreRelacion="";
        int li=ni.getRelacion().getAtributos().length;
        int ld=nd.getRelacion().getAtributos().length;
        String tipo[]=ni.getRelacion().getTipos();
        
        Table<Integer, String, Object> relacionTemporal = HashBasedTable.create();
        String columna[]=null;
        
        if(li==ld)
        {
           
            //Verifica que los tipos de las columnas sean iguales
            columna=new String [ni.getRelacion().getAtributos().length];
            for(int i=0;i<ni.getRelacion().getAtributos().length;i++)
           {
               if(ni.getRelacion().getTipos()[i].compareTo(nd.getRelacion().getTipos()[i])!=0)
               {
                   System.err.print("La operacion diferencia requiere que los atributos posean el mismo dominio");
                   System.exit(0);
               }
               //verifica que los atributos se llamen igual para asignar el nombre de la
               //columna de la nueva relacion, en caso contrario junta los dos numbres
               else if(ni.getRelacion().getAtributos()[i].compareTo(nd.getRelacion().getAtributos()[i])==0)
               {
                    columna[i]=ni.getRelacion().getAtributos()[i];
               }
               else
               {
                   columna[i]=ni.getRelacion().getAtributos()[i].concat(nd.getRelacion().getAtributos()[i]);
               
               }
           }
           
           Table<Integer, String, Object> r1 = HashBasedTable.create();
           Table<Integer, String, Object> r2 = HashBasedTable.create();
           
           for(int col=0;col<columna.length;col++)
           {
               for(int fila=0;fila<ni.getRelacion().getDatos().size()/ni.getRelacion().getAtributos().length;fila++)
               {
                   r1.put(fila, columna[col], ni.getRelacion().getDatos().get(fila, ni.getRelacion().getAtributos()[col]));
               
               }
           
           }
           
           for(int col=0;col<columna.length;col++)
           {
               for(int fila=0;fila<nd.getRelacion().getDatos().size()/nd.getRelacion().getAtributos().length;fila++)
               {
                   r2.put(fila, columna[col], nd.getRelacion().getDatos().get(fila, nd.getRelacion().getAtributos()[col]));
               
               }
           
           }
           boolean guardar=true;
           int j=0;
            for (int i = 0; i < r1.size() / columna.length; i++) {

                Map<String, Object> t = r1.row(i);

                for (int h = 0; h < r2.size() / columna.length; h++) {
                    
                    Map<String, Object> t2 = r2.row(h);
                    
                    if (t.equals(t2)) {
                        guardar = false;
                        break;
                    }
                    guardar = true;
                }

                if (guardar) {
                    for (int h = 0; h < columna.length; h++) {
                        relacionTemporal.put(j, columna[h], r1.get(i, columna[h]));
                    }

                    j++;

                }

            }
           
           
            

            
    }
        
         else
        {
           System.err.print("La operacion Diferencia requiere que las relaciones involucradas posean el mismo numero de atributos");
           System.exit(0);
        }
        
        return new Relacion(nombreRelacion, columna, tipo, relacionTemporal);
            
        
    }
    
    
    
    public Relacion Interseccion(NodoBase ni, NodoBase nd)
    {
        NodoBase temp= new NodoBase();
        temp.setRelacion(Diferencia(ni,nd));
        Relacion r=Diferencia(ni,temp);
        String columna[]=r.getAtributos();
        String tipo[]=r.getTipos();
        String nombreRelacion="";
        
        return new Relacion(nombreRelacion, columna, tipo, r.getDatos());
            
        
    }
    
    
    public Relacion ProductoCartesiano(NodoBase ni, NodoBase nd)
     
    {
        
        String columna[];
        String tipo[];
        String columnad[];
        String tipod[];
        String nombreRelacion="";
        
        columna=ni.getRelacion().getAtributos().clone();
        tipo=ni.getRelacion().getTipos().clone();
        
        String nr1[] =ni.getRelacion().getAtributos();
        
        
        
        columnad=nd.getRelacion().getAtributos().clone();
        tipod=nd.getRelacion().getTipos().clone();
        
        Table<Integer, String, Object> relacionTemporal = HashBasedTable.create();
        
        int f1=ni.getRelacion().getDatos().size()/ni.getRelacion().getAtributos().length;
        int f2=nd.getRelacion().getDatos().size()/nd.getRelacion().getAtributos().length;
        
        
       for (int i = 0; i < columna.length; i++) {
            
            for(int j=0;j<columnad.length;j++)
            {
                if(columna[i].compareTo(columnad[j])==0)
                {
                   
                        
                        columna[i]="relacionI"+columna[i];
                        
                        columnad[j]="relacionD"+columnad[j];
                                
                   
                }
            }
            
        }
       
       Table<Integer, String, Object> r1 = HashBasedTable.create();
       Table<Integer, String, Object> r2 = HashBasedTable.create();
           
           for(int col=0;col<columna.length;col++)
           {
               for(int fila=0;fila<ni.getRelacion().getDatos().size()/ni.getRelacion().getAtributos().length;fila++)
               {
                   r1.put(fila, columna[col], ni.getRelacion().getDatos().get(fila, ni.getRelacion().getAtributos()[col]));
               
               }
           
           }
           
           for(int col=0;col<columnad.length;col++)
           {
               for(int fila=0;fila<nd.getRelacion().getDatos().size()/nd.getRelacion().getAtributos().length;fila++)
               {
                   r2.put(fila, columnad[col], nd.getRelacion().getDatos().get(fila, nd.getRelacion().getAtributos()[col]));
               
               }
           
           }
       
       
        
        
        int j=0;
        int filad=0;
        for (int filai = 0; filai < f1; filai++) {
            int i=0;
            while(i<f2)
            {
                
                for (int col = 0; col < columna.length; col++) {
                    
                 relacionTemporal.put(filad,columna[col], r1.get(filai,columna[col])); 
                }
                
                for (int col = 0; col < columnad.length; col++) {
                    
                 relacionTemporal.put(filad,columnad[col], r2.get(i,columnad[col])); 
                }
            filad++;
            i++;
            }
            
            
        }
        
        int b=0;
        String columna2[]= new String [columna.length+columnad.length];
        for(int i=0;i<columna.length+columnad.length;i++)
        {
            
            if(i<columna.length)
            {
                columna2[i]=columna[i];
            }
            else
            {
                columna2[i]=columnad[b];
                b++;
            }
           
        }
        
        int c=0;
        String tipo2[]= new String [tipo.length+tipod.length];
        for(int i=0;i<tipo.length+tipod.length;i++)
        {
            
            if(i<tipo.length)
            {
                tipo2[i]=tipo[i];
            }
            else
            {
                tipo2[i]=tipod[c];
                c++;
            }
           
        }
        
        
        
        return new Relacion(nombreRelacion,columna2,tipo2,relacionTemporal);
            
        
    }
    
    public Relacion cargarRelacion(String nombreRelacion) throws IOException{
    
       
        BufferedReader br=null;
        FileReader fr;
        File f;
        
        
        try {
            f  =new File("Relaciones\\"+nombreRelacion+".txt");
            fr = new FileReader(f);
            br = new BufferedReader(fr);
        } catch (FileNotFoundException ex) {
            System.err.print("NO existe la relacion llamada, '"+nombreRelacion+"'");
            System.exit(0);
            //Logger.getLogger(AnalizadorSemantico.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String [] atributos=br.readLine().split(";");
        
        String [] tipos=new String [atributos.length];
        for(int i=0;i<atributos.length;i++)
        {
            
            tipos[i]=atributos[i].substring(atributos[i].indexOf("(")+1,atributos[i].indexOf(")"));
            atributos[i]=atributos[i].substring(0,atributos[i].indexOf("("));
        }
        
        Table<Integer, String, Object> Relacion = HashBasedTable.create();
        
        String valores [];
        String linea;
        int i=0;
        DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
        
        while((linea=br.readLine())!=null)
        {
            
            
                valores = linea.split(";");
                for (int j = 0; j < valores.length; j++) 
                {
                    valores[j] = valores[j].replaceAll("\"", "");
                    if(tipos[j].compareTo("cadena")==0)
                    {    Relacion.put(i, atributos[j],(String) valores[j]);
                    
                    }
                    
                    else if(tipos[j].compareTo("numero")==0)
                    {    Relacion.put(i, atributos[j],(Double) Double.parseDouble(valores[j]));
                    
                    }
                    
                    else if(tipos[j].compareTo("fecha")==0)
                    {    
                        try {
                            Relacion.put(i, atributos[j],(Date) df.parse(valores[j]));
                        } catch (ParseException ex) {
                            System.err.print("El formato de la fecha en el archivo de relacion no es aceptado");
                        }
                    
                    }
                }
                
                
               
                
                i++;
            
        }
        
        
        return new Relacion(nombreRelacion,atributos,tipos,Relacion);
        
        
        
        
    }
    
    public Relacion eliminarDuplicados(Relacion relacion)
    {
        Table<Integer, String, Object> relacionTemporal = HashBasedTable.create();
        
        int j=0;
        boolean guardar=true;
        for(int i=0;i<relacion.getDatos().size()/relacion.getAtributos().length;i++)
        {
            
            Map <String, Object> t =  relacion.getDatos().row(i);
            
            for(int h=0;h<j;h++)
            {
                Map <String, Object> t2= relacionTemporal.row(h);
                if(t.equals(t2))
                {
                   guardar=false;
                   break;
                }
                guardar=true;
            }
            
            if(guardar)
            {
                for(int h=0;h<relacion.getAtributos().length;h++)
                {
                    relacionTemporal.put(j,relacion.getAtributos()[h],relacion.getDatos().get(i,relacion.getAtributos()[h]));
                }
                
                j++;
            
            }
           
            
        
        }
        relacion.setDatos(relacionTemporal);
        return relacion;
    
    }
    
    public boolean checkTipos(NodoBase ni, NodoBase nd,String atributos[], String tipos[] )
    {
    
        boolean EstatribI=false,EstatribD=false, mismoTipo=false;
        
        if(perteneceYtipo(atributos,tipos,ni.getToken().getValor(),ni.getToken().getTipo()))
            {
                EstatribI=true;
                ni.getToken().setTipo(TIPO);
            
            }
         else
            {
                System.err.println("El atributo '"+ni.getToken().getValor()+"' no esta declarado en la relacion dada");
                System.exit(0);
            }

        if(nd instanceof NodoAtributo)
        {
            
            
            if(perteneceYtipoD(atributos,tipos,nd.getToken().getValor(),nd.getToken().getTipo()))
            {
                EstatribD=true;
                nd.getToken().setTipo(TIPOD);
            
            }
            
            else
            {
                System.err.println("El atributo '"+nd.getToken().getValor()+"' no esta declarado en la relacion dada");
                System.exit(0);
            }
            
            if(ni.getToken().getTipo().equals(nd.getToken().getTipo()))
            {
                mismoTipo=true;
            
            }
            
            else
            {
                System.err.println("Tipos incompatibles");
                System.exit(0);
            }
            
            return EstatribI && EstatribD && mismoTipo;
 
        }
        
        if(ni.getToken().getTipo().equals(nd.getToken().getTipo()))
            {
                mismoTipo=true;
            
            }
        else
            {
                System.err.println("Tipos incompatibles");
                System.exit(0);
            }
        return EstatribI && mismoTipo;
    
           
    }
    
    public boolean perteneceYtipo(String atributos[],String tipos[], String atr, String tipo)
    {
        for(int i=0;i<atributos.length;i++)
        {
            if(atributos[i].equals(atr))
            {
                TIPO=tipos[i];
                return true;
            }
        
        }
        
        return false;
    
    }
    
     public boolean perteneceYtipoD(String atributos[],String tipos[], String atr, String tipo)
    {
        for(int i=0;i<atributos.length;i++)
        {
            if(atributos[i].equals(atr))
            {
                TIPOD=tipos[i];
                return true;
            }
        
        }
        
        return false;
    
    }
    
    
}
