package al3duc.dao_handler.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Utilidad para leer parametros de un archivo plano con extención <br>
 * .properties directamente desde una fuente de datos externa.
 * 
 * @author      Diego Alexander Duque Mora (diegoduque@aprocom.com.co)
 * @version     %I%, %G%
 * @since       1.0
 */
public class Util_config {
    
    /**
     * Retorna el mapa de parametros de un archivo .properties a partir de un <code>String</code> que indica la ruta<br>
     * fisica del archivo que esta ubicado en un dispositivo de almacenamiento.
     *
     * @param  f    Valor de tipo <code>String</code> que contiene la ruta absoluta del archivo a procesar.
     * @return      Valor de tipo <code>Map</code> que contiene la lista de parámetros que contiene <br>el archivo .properties 
     * @throws      Exception 
     * @see         String
     * @see         Map
     */
    public static Map<String, String> getMapParamsFILE_str(String f) throws Exception{
            return getMapParams(new FileInputStream(f));
    }
    
    /**
     * Retorna el mapa de parametros de un archivo .properties a partir de un <code>FileInputStream</code> que contiene <br>
     * el flujo de datos de un archivo.
     *
     * @param  f    Valor de tipo <code>FileInputStream</code> que contiene el flujo de datos del archivo.
     * @return      Valor de tipo <code>Map</code> que contiene la lista de parámetros que contiene <br>el archivo .properties 
     * @throws      Exception 
     * @see         FileInputStream
     * @see         Map
     */
    public static Map<String, String> getMapParamsFILE(FileInputStream f) throws Exception{
            return getMapParams(f);
    }
    
    /**
     * Retorna el mapa de parametros de un archivo .properties a partir de un <code>URL</code> que contiene <br>
     * el acceso a los datos pormedio de una URL.
     *
     * @param  url  Valor de tipo <code>URL</code> que contiene el flujo de datos del archivo.
     * @return      Valor de tipo <code>Map</code> que contiene la lista de parámetros que contiene <br>el archivo .properties 
     * @throws      Exception 
     * @see         URL
     * @see         Map
     */
    public static Map<String, String> getMapParamsURL(URL url) throws Exception{
        if(url==null)
            throw new Exception("El archivo de configuración no esta definido.");
        
        return getMapParams(url.openStream());		
    }	

    
    /**
     * Retorna el mapa de parametros de un archivo .properties a partir de un <code>InputStream</code> que contiene <br>
     * el acceso a los datos por medio de un flujo de datos generico.
     *     
     * @param in    Valor de tipo <code>InputStream</code> que contiene el flujo de datos del archivo.
     * @return      Valor de tipo <code>Map</code> que contiene la lista de parámetros que contiene <br>el archivo .properties 
     * @throws      Exception 
     * @see         InputStream
     * @see         Map
     */
    public static Map<String, String> getMapParams(InputStream in) throws Exception{
        Map<String, String> r= new HashMap<String, String>();
        Properties p = new Properties();
        p.load(in);
        Enumeration<Object> e = p.keys();
        
        while(e.hasMoreElements()){
          String key= (String)e.nextElement();
          String value= p.getProperty(key);	      
          r.put(key, value);	      
        }

        return r;		
    }
}

