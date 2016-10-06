/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package paquete;
import com.google.common.base.Supplier;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;

/**
 *
 * @author Xmnz
 */
public class Relacion {

    String nombreRelacion;
    String atributos[];
    String tipos[];
    Table datos;
    
    public Relacion (String nombre, String atributos[], String tipos[],Table datos)
    {
    
        this.nombreRelacion=nombre;
        this.atributos=atributos;
        this.tipos=tipos;
        this.datos=datos;
    
    
    }
    
    public String getNombreRelacion() {
        return nombreRelacion;
    }

    public void setNombreRelacion(String nombreRelacion) {
        this.nombreRelacion = nombreRelacion;
    }

    public String[] getAtributos() {
        return atributos;
    }

    public void setAtributos(String[] atributos) {
        this.atributos = atributos;
    }

    public String[] getTipos() {
        return tipos;
    }

    public void setTipos(String[] tipos) {
        this.tipos = tipos;
    }
    
    public Table getDatos() {
        return datos;
    }

    public void setDatos(Table datos) {
        this.datos = datos;
    }
    
    
    
    
    
}
