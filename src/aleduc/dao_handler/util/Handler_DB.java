package aleduc.dao_handler.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * Utilidad para realizar las operaciones de ejecución de sentencias SQL en un <br>
 * objeto tipo <code>java.sql.Connection</code> <br>
 *  
 * @author      Diego Alexander Duque Mora (diegoduque@aprocom.com.co)
 * @version     %I%, %G%
 * @since       1.0
 */

public class Handler_DB {
    public static boolean DEBUG_MODE=false;
    /**
     * 
     * @param ps Valor de tipo <code>PreparedStatement</code> que contiene la informacion de la <br>
     *           consulta SQL a ejecutar
     * @return Retorna un objeto tipo <code>ResultSet</code>
     * @throws Exception
     * @see    ResultSet
     * @see    PreparedStatement
     */
    
    public static ResultSet execute_query(PreparedStatement ps) throws Exception{
        ResultSet rs = ps.executeQuery(); 
        return rs;
    }
    
    /**
     * 
     * @param ps
     * @return
     * @throws Exception
     * @see    PreparedStatement
     */
    
    public static int execute_update(PreparedStatement ps) throws Exception{
        int r = ps.executeUpdate();        
        return r;
    }
    
    
    /**
     * 
     * @param JNDI_Name
     * @return
     * @throws Exception
     */
    public static Connection getConnection(String JNDI_Name) throws Exception{
        //InitialContext initialContext = new InitialContext ();
        //Context envContext = (Context) initialContext.lookup ("java:comp/env");
        //DataSource dataSource = (DataSource) envContext.lookup (JNDI_Name);
        
        InitialContext ctx = new InitialContext();
        DataSource dataSource = (DataSource)ctx.lookup(JNDI_Name);
        return dataSource.getConnection();
    }
    

    /**
     * 
     * @return
     * @throws Exception
     */
    public static Connection getConnection() throws Exception{
        String propertiesL= DEBUG_MODE?"DEBUGconfig.properties":"config.properties";
        Map<String, String> config = Util_config.getMapParamsURL(new Handler_DB().getClass().getResource(propertiesL));
        String JNDI_Name = (String)config.get("JNDI_Name");     
        boolean pool_conections = ((String)config.get("pool_conections")).equals("true");
        
        Connection conR=null;
        
        if(JNDI_Name!=null && !JNDI_Name.equals("")){
            conR= getConnection(JNDI_Name);
        }else if(pool_conections){
        	 PoolDataSource.DEBUG_MODE=DEBUG_MODE;
        	 conR= PoolDataSource.getConnection();
        }else{
            String driver = (String)config.get("driver");
            String stringCon = (String)config.get("stringCon");
            String usr =(String)config.get("usr");
            String psw = (String)config.get("psw");
                
           conR= getConnection(driver,stringCon, usr, psw);
        }        
        return conR;
    }
    
    
    /**
     * 
     * @param driver
     * @param stringCon
     * @param usr
     * @param psw
     * @return
     * @throws Exception
     */
    public static Connection getConnection(String driver,String stringCon, String usr, String psw) throws Exception{
        Connection conexion=null;		
        Class.forName(driver);		
        conexion=DriverManager.getConnection(stringCon,usr,psw);
        return conexion;
   }

    /**
     * 
     * @param rs
     * @throws Exception
     * @see    ResultSet
     */
    public static void closeResultSet(ResultSet rs) throws Exception{
       if(rs!=null && !rs.isClosed())
            rs.close();           
    }
 
    /**
     * 
     * @param inst
     * @throws Exception
     * @see    PreparedStatement
     */
    public static void closePreparedStatement(PreparedStatement inst) throws Exception{
         //if(inst!=null && !inst.isClosed())
                inst.close();			
    }

    /**
     * 
     * @param c
     * @throws Exception
     * @see    Connection
     */
    public static void closeConnection(Connection c) throws Exception{
         if(c!=null && !c.isClosed())
                c.close();
    }
}
