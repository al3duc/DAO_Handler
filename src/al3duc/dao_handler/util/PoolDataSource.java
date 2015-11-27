/**
 * 
 */
package al3duc.dao_handler.util;

import java.sql.Connection;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;



/**
 * @author Diego Duque
 *
 */
public class PoolDataSource {
	/** Pool de conexiones */
    private static DataSource dataSource;
    private static BasicDataSource basicDataSource;
    public static boolean DEBUG_MODE=false;
    

	/**
	 * 
	 */
	public PoolDataSource() {
		// TODO Auto-generated constructor stub
	}
	
	/**
     * Inicializacion de BasicDataSource
     */
    private static void inicializaDataSource() {
        basicDataSource = new BasicDataSource();
        
        Map<String, String> config=null;
		try {
			 String propertiesL= DEBUG_MODE?"DEBUGconfig.properties":"config.properties";
		     config = Util_config.getMapParamsURL(new Handler_DB().getClass().getResource(propertiesL));
		
			String driver = (String)config.get("driver");
			String stringConexion = (String)config.get("stringCon");
			String usuario =(String)config.get("usr");
			String password = (String)config.get("psw");
			int minIdle =Integer.parseInt(config.get("minIdle"));
			int maxIdle = Integer.parseInt(config.get("maxIdle"));
			int maxOpenPreparedStatements= Integer.parseInt(config.get("maxOpenPreparedStatements"));
	
	        basicDataSource.setDriverClassName(driver);
	        basicDataSource.setUsername(usuario);
	        basicDataSource.setPassword(password);
	        basicDataSource.setUrl(stringConexion);
	        basicDataSource.setMinIdle(minIdle);
	        basicDataSource.setMaxIdle(maxIdle);        
	        basicDataSource.setMaxOpenPreparedStatements(maxOpenPreparedStatements);
	
	        // Opcional. Sentencia SQL que le puede servir a BasicDataSource
	        // para comprobar que la conexion es correcta.
	       //basicDataSource.setValidationQuery("select 1");
	
	        dataSource = basicDataSource;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static Connection getConnection() throws Exception{
    	if(dataSource==null){
    		inicializaDataSource();
    	}
    	//System.out.println("POOL");
    	return dataSource.getConnection();
    }
    
    public static void free(){
    	if(dataSource!=null && basicDataSource!=null){
    		dataSource=null;
    		basicDataSource=null;
    	}
    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

