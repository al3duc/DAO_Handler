/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aleduc.dao_handler.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Diego Duque
 */
public class RowResult {
    /**
     * 
     */
    public final static int TIPO_BUSQUEDA_IGUAL=1;
    /**
     * 
     */
    public final static int TIPO_BUSQUEDA_NO_IGUAL=2;
    /**
     * 
     */
    public final static int TIPO_BUSQUEDA_LIKE=3;
    
    /**
     * 
     */
    public String cb_name;	
    /**
     * 
     */
    public Map<String, Field_DEF> camposDEF = new HashMap<String, Field_DEF>();    
    
    /**
     * 
     * @throws Exception
     */
    public RowResult() throws Exception {}

    /**
     * 
     * @param map
     */
    public void asignar_camposDEF(Map<String, Field_DEF> map){        
        camposDEF.clear();       
        Collection<Field_DEF> valores = map.values();
        
        for (Iterator<Field_DEF> it = valores.iterator(); it.hasNext();) {
            Field_DEF field_DEF = it.next();
            
            Field_DEF campo = new Field_DEF();
            
            campo.setOrden(field_DEF.getOrden());
            campo.setDescripcion(field_DEF.getDescripcion());
            campo.setNombre(field_DEF.getNombre());
            campo.setTipo(field_DEF.getTipo());
            campo.setEsAutoIncrement(field_DEF.isEsAutoIncrement());
            campo.setEsPK(field_DEF.isEsPK());              
            campo.setLength(field_DEF.getLength());
            camposDEF.put(field_DEF.getNombre(), campo);
        }
    }
    
    /**
     * 
     * @param cb
     * @throws Exception
     */
    public void asignar_DEF(RowResult cb) throws Exception{
        if(cb!=null){
            cb_name=cb.cb_name;
            asignar_camposDEF(cb.camposDEF);
        }else{
            throw new Exception("El parametro de tipo ClassBase es NULL");
        }
    }
       
    /**
     * 
     * @param campo
     * @return
     * @throws Exception
     */
    public Object getValue(String campo) throws Exception{
            Field_DEF cd= (Field_DEF)camposDEF.get(campo);
            if(cd == null)
                throw new Exception("El campo " + campo + ", no existe.");
            return cd.getValue();
    }

    /**
     * 
     * @param campo
     * @param value
     * @throws Exception
     */
    public void setValue(String campo, Object value) throws Exception{
            Field_DEF cd= (Field_DEF)camposDEF.get(campo);
            if(cd!=null){			
                    cd.setValue(value);
            }else{
                throw new Exception("El campo " + campo + ", no existe.");
            }
    }
}
