/**
 * 
 */
package al3duc.dao_handler.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import al3duc.dao_handler.core.Context_DAO;

/**
 * @author Diego Duque
 *
 */
public class BuildDAOS {

	/**
	 * 
	 */
	public BuildDAOS() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String path_dest="D:/SVNRepositorios/aprocom_svn/aproweb_api/src/co/com/aprocomsxxi/aproweb_api/db";
		String pakage_classes="co.com.aprocomsxxi.aproweb_api.db";
		
		BuildDAOS.generar_daos(path_dest, pakage_classes);
	}
	
	public static void generar_daos(String path_dest, String pakage_classes ) throws Exception {
		

		Context_DAO cn = new Context_DAO();
        cn.setDEBUG_MODE(true);
        cn.start();
        
        List<String> tablas_ls= new ArrayList<String>();
        
        DatabaseMetaData md = cn.getConnection().getMetaData();
        ResultSet rs = md.getTables(null, null, "%", null);
        while (rs.next()) {
        	String table= rs.getString(3);
        	String table_type=rs.getString(4);
        	
        	if(table_type.equals("TABLE")){        		
        		tablas_ls.add(table);
        	}          
        }       
        cn.stop();
        
        
        //Se crean los DAOS a partir de la lista de las tablas.
        
        for (String tabla : tablas_ls) {
        	
        	tabla= String.valueOf(tabla.charAt(0)).toUpperCase() + tabla.substring(1);
        	String class_name=tabla +"_DAO";
        	
			String str_code_class="package "+pakage_classes+"; "+ "\n\n" +						
					"import co.com.aprocom.dao_handle.util.dao.ClassBase; "+ "\n" +
					"import co.com.aprocom.dao_handle.util.dao.Context_DAO; "+ "\n" +
					"import co.com.aprocom.dao_handle.util.dao.DAO_Handler; "+ "\n" +
					"import co.com.aprocom.dao_handle.util.dao.Interface_DAO; "+ "\n" +
					"import co.com.aprocom.dao_handle.util.dao.Interface_DAO_imp; "+ "\n\n" +
					
					"public class "+class_name+" extends DAO_Handler implements Interface_DAO, Interface_DAO_imp {" +"\n\n"+
					
					"	public "+class_name+"(Context_DAO _cn) {"+ "\n" +
					"		super(_cn);"+ "\n" +
					"		// TODO Auto-generated constructor stub"+ "\n" +
					"	}"+ "\n\n" +
					
					"	@Override"+ "\n" +
					"	public ClassBase getIntance_classBase() throws Exception {"+ "\n" +
					"		// TODO Auto-generated method stub"+ "\n" +
					"		return buildCB(\""+tabla+"\");"+ "\n" +
					"	}"+ "\n" +
					"}";
			 
			File file = new File(path_dest +"/"+class_name+".java");
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(str_code_class);
			bw.close();	
			
			System.out.println("DAO "+class_name+".... Generado OK.");
		}
	}

}
