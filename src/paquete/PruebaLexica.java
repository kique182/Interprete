/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package paquete;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import AST.*;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;
import java.awt.FlowLayout;

/**
 *
 * @author Xmnz&
 */

public class PruebaLexica {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
       

        File arch= new File("prueba6.txt");
        
        FileReader fr=new FileReader(arch);
        
        AnalizadorLexico lex=new AnalizadorLexico(fr);
        //lex.yytext();
       
        /*try {
            while(true){
           
            System.out.print(((Token)lex.next_token().value).nombre+" ");
            
        }
        } catch (Exception ex) {
            Logger.getLogger(PruebaLexica.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
        parser pars = new parser(lex);
        try {
            pars.parse();
            NodoBase arbol=pars.action_obj.getASTroot();
            //AST.Util.ImprimirArbol(arbol);
            
            
            JFrame vent=new JFrame("Ventana");
            vent.setBounds(10,10,1500,768);
            vent.setVisible(true);
            vent.setLayout(null);
            AST.ArbolGrafico ag=new AST.ArbolGrafico(arbol);
            vent.add(ag);
            ag.setBounds(-200, 0, 1500,768);
            ag.setVisible(true);
            vent.setDefaultCloseOperation(2);
            
            AnalizadorSemantico ase= new AnalizadorSemantico(arbol);
            
            
            NodoOpBi a=(NodoOpBi)ase.arbol;
            int t=a.getRelacion().getAtributos().length;
            
                for(int i=0;i<a.getRelacion().getAtributos().length;i++)
                {
                    System.out.print(a.getRelacion().getAtributos()[i]+" ");
                
                }
                System.out.println();
            
                for (int i = 0; i < a.getRelacion().getDatos().size() / t; i++)
                {
                    for (int j = 0; j < t; j++) {
                    
                    System.out.print(a.getRelacion().getDatos().get(i, a.getRelacion().getAtributos()[j])+" ");
                    }
                    System.out.println();
                }
            
        } catch (Exception ex) {
            Logger.getLogger(PruebaLexica.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
