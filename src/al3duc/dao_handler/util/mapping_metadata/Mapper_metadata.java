/**
 * 
 */
package al3duc.dao_handler.util.mapping_metadata;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Map;

import al3duc.dao_handler.util.Handler_DB;

/**
 * @author Diego Duque
 *
 */
public final class Mapper_metadata {
	public static Map<String, ResultSetMetaData_Proxy> metadata_db;	
	
	static{
		metadata_db= new HashMap<String, ResultSetMetaData_Proxy>();		
	}
	
	
	public static ResultSetMetaData_Proxy getMetadata(Connection c, String cb_name) throws Exception{
		ResultSetMetaData_Proxy rsMP=null;
		 
		if(metadata_db==null){
			metadata_db= new HashMap<String, ResultSetMetaData_Proxy>();			
		}
		
		ResultSetMetaData_Proxy rsMProxy= metadata_db.get(cb_name);
		ResultSetMetaData rsMAux= rsMProxy!=null?rsMProxy.rsM:null;
		
		if(rsMAux==null){	
			PreparedStatement inst=c.prepareStatement("SELECT * FROM " + cb_name + " WHERE 1=2");	       	
		    ResultSet resultado = Handler_DB.execute_query(inst); 
		    ResultSetMetaData metadata= resultado.getMetaData();	
		    rsMP= new ResultSetMetaData_Proxy(metadata,  getPKs(c, cb_name));	    
		    
		    metadata_db.put(cb_name, rsMP);		    
		    Handler_DB.closePreparedStatement(inst);
		    
		}else{
			rsMP=rsMProxy;
			
		}
		
		return rsMP;		
	}
	
	public static void clearMapping(){
		metadata_db=null;
	}
	
	private static String getPKs(Connection conn, String cb_name) throws Exception{
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
