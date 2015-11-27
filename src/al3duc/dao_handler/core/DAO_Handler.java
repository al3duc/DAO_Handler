/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package al3duc.dao_handler.core;

import al3duc.dao_handler.util.Handler_DB;
import al3duc.dao_handler.util.LogManager;
import al3duc.dao_handler.util.mapping_metadata.Mapper_metadata;
import al3duc.dao_handler.util.mapping_metadata.ResultSetMetaData_Proxy;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Diego Duque
 */
public class DAO_Handler implements Interface_DAO{
    public String catalog_db=null;
    private Context_DAO cn;
    public boolean RETORNAR_IDENTITY_AUTO_INC=false;
    
    public DAO_Handler(Context_DAO _cn){
        cn=_cn;        
    }
    
    public Connection getConnection() throws Exception{
        Connection con= cn.getConnection();    
         if(con==null)
          throw new Exception("No se pudo conectar a la base de datos. Verifique que el config.properties este bien configurado en el paquete co.com.aprocom.dao_handle.util");
        if(catalog_db!=null){// && !catalog_db.equals("")){
            con.setCatalog(catalog_db);            
            LogManager.writeLog("con.getCatalog(): " + con.getCatalog(),Handler_DB.DEBUG_MODE);
        }else{
            con.setCatalog(cn.catalog_name_default); 
            LogManager.writeLog("DEFAULT: con.getCatalog(): " + con.getCatalog(),Handler_DB.DEBUG_MODE);
        }         
        return con;
    }   
    
    @Override
    public int insert(RowResult m) throws Exception {
        Connection c=getConnection();			
        Map<Integer, Object> valuesSQL = new HashMap<Integer, Object>();
        String sql="INSERT INTO " + m.cb_name +" (";
        String camposSQL="";
        String camposIntSQL="";
        int r=0;

        int cont=1;
        for (Iterator iterator = m.camposDEF.values().iterator(); iterator.hasNext();) {
                Field_DEF campo = (Field_DEF) iterator.next();	

                if(!campo.esAutoIncrement){				
                        camposSQL= camposSQL + campo.getNombre() + ",";				
                        camposIntSQL=camposIntSQL + "?,";				
                        valuesSQL.put(cont, campo.getValue());
                        cont++;
                }
        }

        camposSQL= camposSQL.substring(0,camposSQL.length()-1);
        camposIntSQL= camposIntSQL.substring(0,camposIntSQL.length()-1);
        sql=sql + camposSQL + ") ";
        
        boolean pkAutiInc=false; // Si se tiene la pk auto incrementada.
        
        if(RETORNAR_IDENTITY_AUTO_INC){
	        for (Iterator iterator = m.camposDEF.values().iterator(); iterator.hasNext();) {
	            Field_DEF campo = (Field_DEF) iterator.next();	
	
	            if(campo.esAutoIncrement){				
	            	sql=sql+"OUTPUT Inserted."+campo.getNombre();
	            	pkAutiInc=true;
	            	break;
	            }
	        }    
        }
        
        sql=sql+" VALUES (" + camposIntSQL + ")";
        
        LogManager.writeLog(sql,Handler_DB.DEBUG_MODE);

        PreparedStatement inst=c.prepareStatement(sql);

        for (int i = 1; i < cont; i++) {
                Object obj = valuesSQL.get(i);
                if(obj instanceof String){
                        inst.setString(i,(String)valuesSQL.get(i));
                }else if(obj instanceof java.sql.Timestamp){	
            	    java.sql.Timestamp d= (java.sql.Timestamp)valuesSQL.get(i);
                    if(d!=null){
                            inst.setTimestamp(i,d);                            
                    }else{
                            inst.setNull(i,java.sql.Types.TIMESTAMP);
                    }
                }else if(obj instanceof java.util.Date){	
                        java.util.Date d= (java.util.Date)valuesSQL.get(i);
                        if(d!=null){
                                inst.setDate(i,new java.sql.Date(d.getTime()));
                        }else{
                                inst.setNull(i,java.sql.Types.DATE);
                        }
                        
                }else  if(obj instanceof byte[]){				
                        if(obj!=null){	
                                byte[] datosImage =(byte[])obj;		
                                InputStream in=new ByteArrayInputStream(datosImage);		
                                inst.setBinaryStream(i, in, (int) datosImage.length);
                        }else{
                                inst.setNull(i, java.sql.Types.BLOB);		
                        }
                }else{				
                        inst.setObject(i,valuesSQL.get(i));
                }
        }
        
        if(pkAutiInc && RETORNAR_IDENTITY_AUTO_INC){
        	ResultSet result=Handler_DB.execute_query(inst);
        	if(result.next()){
        		r= result.getInt(1);
        	}else{
        		r= 0;
        	}
        }else{
        	r=Handler_DB.execute_update(inst);
        }
        
        Handler_DB.closePreparedStatement(inst);
        //Handler_DB.closeConnection(c);        
        return r;
    }

    @Override
    public int update(RowResult m) throws Exception {
        Connection c=getConnection();			
        Map<Integer, Object> valuesSQL = new HashMap<Integer, Object>();
        String sql="UPDATE " + m.cb_name +" SET ";
        String camposSQL="";        
        int r=0;

        int cont=1;
        for (Iterator iterator = m.camposDEF.values().iterator(); iterator.hasNext();) {
            Field_DEF campo = (Field_DEF) iterator.next();	

            if(!campo.esPK){				
                camposSQL= camposSQL + campo.getNombre() + "= ?, ";				
                valuesSQL.put(cont, campo.getValue());
                cont++;
            }
        }

        camposSQL= camposSQL.substring(0,camposSQL.length()-2);      
        
        sql=sql + camposSQL + " WHERE ";    
        
        for (Iterator iterator = m.camposDEF.values().iterator(); iterator.hasNext();) {
            Field_DEF campo = (Field_DEF) iterator.next();	

            if(campo.esPK){				
                sql= sql + campo.getNombre() + "= ? AND ";				
                valuesSQL.put(cont, campo.getValue());
                cont++;
            }
        }
                
        sql= sql.substring(0,sql.length()-5);  
        
        LogManager.writeLog(sql,Handler_DB.DEBUG_MODE);

        PreparedStatement inst=c.prepareStatement(sql);

        for (int i = 1; i < cont; i++) {
                Object obj = valuesSQL.get(i);
                if(obj instanceof String){
                        inst.setString(i,(String)valuesSQL.get(i));
                }else if(obj instanceof java.sql.Timestamp){	
               	 java.sql.Timestamp d= (java.sql.Timestamp)valuesSQL.get(i);
                 if(d!=null){
                         inst.setTimestamp(i,d);                         
                    }else{
                         inst.setNull(i,java.sql.Types.TIMESTAMP);
                    }
                }else if(obj instanceof java.util.Date){	
                        java.util.Date d= (java.util.Date)valuesSQL.get(i);
                        if(d!=null){
                                inst.setDate(i,new java.sql.Date(d.getTime()));
                        }else{
                                inst.setNull(i,java.sql.Types.DATE);
                        }
                }else if(obj instanceof byte[]){				
                        if(obj!=null){	
                                byte[] datosImage =(byte[])obj;		
                                InputStream in=new ByteArrayInputStream(datosImage);		
                                inst.setBinaryStream(i, in, (int) datosImage.length);
                        }else{
                                inst.setNull(i, java.sql.Types.BLOB);		
                        }
                }else{				
                        inst.setObject(i,valuesSQL.get(i));
                }
        }

        r=Handler_DB.execute_update(inst);
        Handler_DB.closePreparedStatement(inst);
        //Handler_DB.closeConnection(c);        
        return r;
    }

    @Override
    public RowResult getPrimaryKey(RowResult cb_pks) throws Exception {              
        RowResult cb = cb_pks;       
        Connection c=getConnection();
        Map<Integer, Object> valuesSQL = new HashMap<Integer, Object>();
        
        String sql="SELECT * FROM " + cb.cb_name + " WHERE ";
        
        int cont=1;
        for (Iterator iterator = cb.camposDEF.values().iterator(); iterator.hasNext();) {
            Field_DEF campo = (Field_DEF) iterator.next();	

            if(campo.esPK){				
                sql= sql + campo.getNombre() + "= ? AND ";				
                valuesSQL.put(cont, campo.getValue());
                cont++;
            }
        }
        
        sql= sql.substring(0,sql.length()-5); 
        
        LogManager.writeLog(sql,Handler_DB.DEBUG_MODE);
        
        PreparedStatement inst=c.prepareStatement(sql);        
        
        for (int i = 1; i < cont; i++) {
                Object obj = valuesSQL.get(i);
                if(obj instanceof String){
                        inst.setString(i,(String)valuesSQL.get(i));
                }else if(obj instanceof java.sql.Timestamp){	
               	    java.sql.Timestamp d= (java.sql.Timestamp)obj;
                    if(d!=null){
                            inst.setTimestamp(i,d);                            
                       }else{
                               inst.setNull(i,java.sql.Types.TIMESTAMP);
                       }
               
                }else if(obj instanceof java.util.Date){	
                        java.util.Date d= (java.util.Date)valuesSQL.get(i);
                        if(d!=null){
                                inst.setDate(i,new java.sql.Date(d.getTime()));
                        }else{
                                inst.setNull(i,java.sql.Types.DATE);
                        }   
                }else {				
                        inst.setObject(i,valuesSQL.get(i));
                }
        }
	
        ResultSet resultado = Handler_DB.execute_query(inst);
                
        if(resultado.next()){
                ResultSetMetaData metadata= resultado.getMetaData();
                for (int i = 1; i <= metadata.getColumnCount(); i++) {
                        String column_name=metadata.getColumnName(i);
                        cb.setValue(column_name, resultado.getObject(column_name));                      
                }			
        }else{
            cb=null;
        }		
        
        //Handler_DB.closeResultSet(resultado);
        Handler_DB.closePreparedStatement(inst);
        //Handler_DB.closeConnection(c);        
        return cb;
    }
    
    /**
     * Metodo getRow: Se utiliza para optener un registro de una tabla de acuerdo a los campos por el cual se desea buscar. 
     * 
     * @param cb Especificacion de los campos de la tabla.
     * @param params Mapa <String,Object> campo, valor
     * @return
     * @throws Exception
     */
    
    public RowResult getRow(RowResult cb, Map<String,Object> params) throws Exception {              
   
        Connection c=getConnection();
        Map<Integer, Object> valuesSQL = new HashMap<Integer, Object>();
        
        String sql="SELECT * FROM " + cb.cb_name + " WHERE ";
        
        int cont=1;
        
               
        for (Iterator iterator = cb.camposDEF.values().iterator(); iterator.hasNext();) {
            Field_DEF campo = (Field_DEF) iterator.next();	
            
            Object valor_campo= params.get(campo.getNombre());

            if(valor_campo!=null){				
                sql= sql + campo.getNombre() + "= ? AND ";				
                valuesSQL.put(cont, valor_campo);
                cont++;
            }
        }
        
        sql= sql.substring(0,sql.length()-5); 
        
        LogManager.writeLog(sql,Handler_DB.DEBUG_MODE);
        
        PreparedStatement inst=c.prepareStatement(sql);        
        
        for (int i = 1; i < cont; i++) {
                Object obj = valuesSQL.get(i);
                if(obj instanceof String){
                        inst.setString(i,(String)valuesSQL.get(i));
                }else if(obj instanceof java.sql.Timestamp){	
               	    java.sql.Timestamp d= (java.sql.Timestamp)obj;
                    if(d!=null){
                            inst.setTimestamp(i,d);                            
                       }else{
                               inst.setNull(i,java.sql.Types.TIMESTAMP);
                       }
               
                }else if(obj instanceof java.util.Date){	
                        java.util.Date d= (java.util.Date)valuesSQL.get(i);
                        if(d!=null){
                                inst.setDate(i,new java.sql.Date(d.getTime()));
                        }else{
                                inst.setNull(i,java.sql.Types.DATE);
                        }   
                }else {				
                        inst.setObject(i,valuesSQL.get(i));
                }
        }
	
        ResultSet resultado = Handler_DB.execute_query(inst);
                
        if(resultado.next()){
                ResultSetMetaData metadata= resultado.getMetaData();
                for (int i = 1; i <= metadata.getColumnCount(); i++) {
                        String column_name=metadata.getColumnName(i);
                        cb.setValue(column_name, resultado.getObject(column_name));                      
                }			
        }else{
            cb=null;
        }		
        
        //Handler_DB.closeResultSet(resultado);
        Handler_DB.closePreparedStatement(inst);
        //Handler_DB.closeConnection(c);        
        return cb;
    }

    /**
     * 
     * @deprecated La busqueda por este metodo es muy lenta y se recomienda en la implementacion que se haga de forma personalizada.
     * 
     */
    
    @Override
    public List<RowResult> getFiltro(RowResult cb_criterio) throws Exception {
        List<RowResult> lstRet= new ArrayList<RowResult>();
        RowResult cb = cb_criterio;       
        Connection c=getConnection();
        Map<Integer, Object> valuesSQL = new HashMap<Integer, Object>();
        String aux_where="";
        String sql="SELECT * FROM " + cb.cb_name;
        
        int cont=1;
        for (Iterator iterator = cb.camposDEF.values().iterator(); iterator.hasNext();) {
            Field_DEF campo = (Field_DEF) iterator.next();	

            if(campo.tipo_busqueda!=0){	
                String operador_b="";
                
                if(campo.tipo_busqueda==RowResult.TIPO_BUSQUEDA_IGUAL){
                    operador_b=" = ?";
                }else if(campo.tipo_busqueda==RowResult.TIPO_BUSQUEDA_NO_IGUAL){
                    operador_b=" <> ?";
                }else if(campo.tipo_busqueda==RowResult.TIPO_BUSQUEDA_LIKE){
                    operador_b=" LIKE ?";
                }
                
                
                /*
                switch(campo.tipo_busqueda){
                    case ClassBase.TIPO_BUSQUEDA_IGUAL :{operador_b=" = ?";break;}                        
                    case ClassBase.TIPO_BUSQUEDA_NO_IGUAL :{operador_b=" <> ?";break;}                        
                    case ClassBase.TIPO_BUSQUEDA_LIKE :{operador_b=" LIKE ?";break;}
                }               
                */
                
                aux_where= aux_where + campo.getNombre() + operador_b + " AND ";
                                       
                if(campo.tipo_busqueda==RowResult.TIPO_BUSQUEDA_LIKE){
                    valuesSQL.put(cont, "%" + campo.getValue() + "%");
                }else{
                    valuesSQL.put(cont, campo.getValue());
                }
                cont++;
            }
        }        
        
        if(!aux_where.equals("")){
            aux_where= aux_where.substring(0,aux_where.length()-5);
            sql=sql + " WHERE " + aux_where;
        }
        
        LogManager.writeLog(sql,Handler_DB.DEBUG_MODE);
        
        PreparedStatement inst=c.prepareStatement(sql);	
        
        for (int i = 1; i < cont; i++) {
                Object obj = valuesSQL.get(i);
                if(obj instanceof String){
                        String val= (String)valuesSQL.get(i);
                        inst.setString(i,val);
                }else if(obj instanceof java.util.Date){	
                        java.util.Date d= (java.util.Date)valuesSQL.get(i);
                        if(d!=null){
                                inst.setDate(i,new java.sql.Date(d.getTime()));
                        }else{
                                inst.setNull(i,java.sql.Types.DATE);
                        }
                }else{				
                        inst.setObject(i,valuesSQL.get(i));
                }
        }
        
        ResultSet resultado = Handler_DB.execute_query(inst);

        while(resultado.next()){
                RowResult cbTemp = new RowResult();
                
                cbTemp.asignar_camposDEF(cb.camposDEF);
                
                ResultSetMetaData metadata= resultado.getMetaData();
                for (int i = 1; i <= metadata.getColumnCount(); i++) {
                        String column_name=metadata.getColumnName(i);
                        cbTemp.setValue(column_name, resultado.getObject(column_name));                      
                }
                lstRet.add(cbTemp);
        }
        
        //Handler_DB.closeResultSet(resultado);
        Handler_DB.closePreparedStatement(inst);
        //Handler_DB.closeConnection(c);        
        return lstRet;
    }   

    @Override
    public int delete(RowResult cb_pk) throws Exception {
        RowResult cb = cb_pk;       
        Connection c=getConnection();
        Map<Integer, Object> valuesSQL = new HashMap<Integer, Object>();
        
        String sql="DELETE " + cb.cb_name + " WHERE ";
        
        int cont=1;
        for (Iterator iterator = cb.camposDEF.values().iterator(); iterator.hasNext();) {
            Field_DEF campo = (Field_DEF) iterator.next();	

            if(campo.esPK){				
                sql= sql + campo.getNombre() + "= ? AND ";				
                valuesSQL.put(cont, campo.getValue());
                cont++;
            }
        }
        
        sql= sql.substring(0,sql.length()-5);  
        
        LogManager.writeLog(sql,Handler_DB.DEBUG_MODE);
        
        PreparedStatement inst=c.prepareStatement(sql);	
        
        for (int i = 1; i < cont; i++) {
                Object obj = valuesSQL.get(i);
                if(obj instanceof String){
                        inst.setString(i,(String)valuesSQL.get(i));
                }else if(obj instanceof java.util.Date){	
                        java.util.Date d= (java.util.Date)valuesSQL.get(i);
                        if(d!=null){
                                inst.setDate(i,new java.sql.Date(d.getTime()));
                        }else{
                                inst.setNull(i,java.sql.Types.DATE);
                        }
                }else{				
                        inst.setObject(i,valuesSQL.get(i));
                }
        }
        
        int resultado = Handler_DB.execute_update(inst);

        //Handler_DB.closeConnection(c);        
        return resultado;
    }   
    
    public RowResult buildCB_resultset(ResultSet resultado) throws Exception{
        RowResult cb = null;
        if(resultado!=null){  
            cb = new RowResult();            
            ResultSetMetaData metadata= resultado.getMetaData();       
            for (int i = 1; i <= metadata.getColumnCount(); i++) {
                    Field_DEF campo = new Field_DEF();                
                    campo.setOrden(i);//(Integer)getPropiedad_campo(cb_name, metadata.getColumnName(i), "ORDINAL_POSITION"));
                    campo.setDescripcion(null);//(String)getPropiedad_campo(cb_name, metadata.getColumnName(i), "REMARKS"));
                    campo.setNombre(metadata.getColumnName(i));
                    campo.setTipo(metadata.getColumnType(i));
                    campo.setEsAutoIncrement(metadata.isAutoIncrement(i));
                    campo.setEsPK(false);
                    campo.setLength(metadata.getColumnDisplaySize(i));
                    cb.camposDEF.put(metadata.getColumnName(i), campo);
            }
        }
        return cb;
    }
    
    public RowResult buildCB(String cb_name) throws Exception{
        RowResult cb = new RowResult();        
        Connection c=getConnection();   
        cb.cb_name= cb_name;
        
        ResultSetMetaData_Proxy metadataProxy=Mapper_metadata.getMetadata(c, cb_name);
        ResultSetMetaData metadata= metadataProxy.rsM;
        
        for (int i = 1; i <= metadata.getColumnCount(); i++) {
                Field_DEF campo = new Field_DEF();                
                campo.setOrden(i);//(Integer)getPropiedad_campo(cb_name, metadata.getColumnName(i), "ORDINAL_POSITION"));
                campo.setDescripcion(null);//(String)getPropiedad_campo(cb_name, metadata.getColumnName(i), "REMARKS"));
                campo.setNombre(metadata.getColumnName(i));
                campo.setTipo(metadata.getColumnType(i));
                campo.setEsAutoIncrement(metadata.isAutoIncrement(i));
                
                String pks= metadataProxy.pks;
                if(pks==null)
                    throw new Exception("La Entidad '" + cb_name + "' no tiene definida la clave primaria.");
                
                if(pks.equals(""))
                    throw new Exception("La Entidad '" + cb_name + "' no tiene definida la clave primaria.");
                
                campo.setEsPK(pks!=null && pks.indexOf(metadata.getColumnName(i))!=-1);
                campo.setLength(metadata.getColumnDisplaySize(i));
                cb.camposDEF.put(metadata.getColumnName(i), campo);
        }       
        
        return cb;
    }
    
    private Object getPropiedad_campo(String cb_name, String str_campo, String propiedad) throws Exception{
        Object resp=null;
        Connection c = getConnection();
        DatabaseMetaData metadata = c.getMetaData();
        ResultSet resultSet = metadata.getColumns(null, null, cb_name, null);
        while (resultSet.next()) {
            String COLUMN_NAME = resultSet.getString("COLUMN_NAME");           
            Object valor=resultSet.getObject(propiedad);
            
            if(valor!=null && COLUMN_NAME!=null && COLUMN_NAME.equals(str_campo)){
                resp=valor;
                break;
            }                      
        }
        
        return resp;
    }
    
    private String getPKs(String cb_name) throws Exception{
        Connection conn = getConnection();
        ResultSet rs = null;
        DatabaseMetaData meta = conn.getMetaData();
        rs = meta.getPrimaryKeys(null, null, cb_name);
        String r="";

        while (rs.next()) {
          String columnName = rs.getString("COLUMN_NAME");
          r= r + columnName + ",";
        }

        //conn.close();
        return r;
    }    
}
