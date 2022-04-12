
package org.scd.myspa.core.model;

/**
 *
 * @author DanielAbrahamSanchez
 */
public class Producto {
     int id;
    String nombre;
    String marca;
    double precioUso;
    int estatus;
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getMarca() {
        return marca;
    }
    
    public void setMarca(String marca) {
        this.marca = marca;
    }
    
    public double getPrecioUso() {
        return precioUso;
    }
    
    public void setPrecioUso(double precioUso) {
        this.precioUso = precioUso;
    }
    
    public int getEstatus() {
        return estatus;
    }
    
    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }
}
