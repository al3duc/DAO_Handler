/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aleduc.dao_handler.core;

import aleduc.dao_handler.util.Handler_DB;
import java.sql.Connection;
import java.sql.SQLException;


/**
 *
 * @author Diego Duque
 */
public class Context_DAO {
    private Connection con;
    public String catalog_name_default="";
    
    public Connection getConnection() throws Exception {
        if(con==null)
            throw new Exception("No se ha iniciado el contexto del DAO.");
        return con;
    }
    
    public Context_DAO(){}
    
    public void setDEBUG_MODE(boolean s){
        Handler_DB.DEBUG_MODE=s;
    }
    
    public void start() throws Exception{
        con = Handler_DB.getConnection(); 
        catalog_name_default= con.getCatalog();
    }
    
    
    public void stop() throws Exception{        
        if(con!=null)
            con.close();         
    }

    @Override
    protected void finalize() throws Throwable {
        
        stop();
        super.finalize();
    }
}
