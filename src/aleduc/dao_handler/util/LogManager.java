/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aleduc.dao_handler.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author Diego Duque
 */
public class LogManager {
    private static java.text.SimpleDateFormat formatoFecha = new java.text.SimpleDateFormat("dd/MM/ hh:mm:ss:SSS aaa");
    public static void writeLog(String datos, boolean DEBUG_MODE) throws Exception{
    		
    	
    	String propertiesL= DEBUG_MODE?"DEBUGconfig.properties":"config.properties";
        Map<String, String> config = Util_config.getMapParamsURL(new Handler_DB().getClass().getResource(propertiesL));
        String path_logs = (String)config.get("path_log");
        boolean show_log= (config.get("show_log")==null?"":(String)config.get("show_log")).equals("true");
        
    	if(show_log){
    		System.out.println(datos);
    	}
    	
    	
    	
           /* String propertiesL= DEBUG_MODE?"DEBUGconfig.properties":"config.properties";
            Map<String, String> config = Util_config.getMapParamsURL(new Handler_DB().getClass().getResource(propertiesL));
            String path_logs = (String)config.get("path_log");
        
            String sFichero = path_logs + "DAO_handler_log.txt"; 
            String contAnt = getContenidoArchivo(new File(sFichero));            

            try{
              BufferedWriter bw =   new BufferedWriter(new FileWriter(sFichero));
              bw.write(contAnt);
              bw.flush();
              bw.write(formatoFecha.format(new Date()) + ":" + datos);
              bw.flush();
              bw.close();
            } catch (Exception ioe){
                    ioe.printStackTrace();
            }        */
    }
    
    static public String getContenidoArchivo(File archivoAbierto) {

    StringBuilder contenido = new StringBuilder();

    try {
      BufferedReader entrada =  new BufferedReader(new FileReader(archivoAbierto));
      try {
        String line = null; 
        while (( line = entrada.readLine()) != null){
          contenido.append(line);
          contenido.append(System.getProperty("line.separator"));
        }
      }
      finally {
        entrada.close();
      }
    }
    catch (Exception ex){
      ex.printStackTrace();
    }

    return contenido.toString();
  }
}
