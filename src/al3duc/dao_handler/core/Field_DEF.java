/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package al3duc.dao_handler.core;

/**
 *
 * @author Diego Duque
 */
public class Field_DEF {
    	public Object value;
	public String nombre;
	public int tipo;
	public boolean esPK;
	public boolean esAutoIncrement;
        public int tipo_busqueda; //1 = igual a, 2= deferente a, 3= contiene (LIKE)
        public int length;
        public int orden;
        public String descripcion;
        
	
	public Field_DEF() {}
	public Object getValue() {return value;}        
	public void setValue(Object value) {this.value = value;}
	public String getNombre() {return nombre;}        
	public void setNombre(String nombre) {this.nombre = nombre;}
	public int getTipo() {return tipo;}
	public void setTipo(int tipo) {this.tipo = tipo;}
	public boolean isEsPK() {return esPK;}
	public void setEsPK(boolean esPK) {this.esPK = esPK;}
	public boolean isEsAutoIncrement() {return esAutoIncrement;}
	public void setEsAutoIncrement(boolean esAutoIncrement) {this.esAutoIncrement = esAutoIncrement;}
        public int getLength() {return length;}
        public void setLength(int length) {this.length = length;}  
        public int getOrden() {return orden;}
        public void setOrden(int orden) {this.orden = orden;}
        public String getDescripcion() {return descripcion;}
        public void setDescripcion(String descripcion) {this.descripcion = descripcion;}        
}
