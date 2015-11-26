/**
 * 
 */
package mapping_table;

import java.util.Iterator;
import java.util.List;

import aleduc.dao_handler.core.RowResult;
import aleduc.dao_handler.core.Context_DAO;
import aleduc.dao_handler.core.Field_DEF;

/**
 * @author Diego Duque
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Context_DAO context_db = new Context_DAO();
		try {
			context_db.start();
			Clientes clientesDAO= new Clientes(context_db);
			
			//Insertar en la tabla de clientes.
			RowResult clienteRow= clientesDAO.getIntance_classBase();
			clienteRow.setValue("id", "123");
			clienteRow.setValue("Nombre", "Carlos Mesa");			
			clientesDAO.insert(clienteRow);
			
			//Busqueda de los datos en la tabla Clientes
			Field_DEF codigo= clienteRow.camposDEF.get("id");
	        codigo.value ="123";
	        codigo.tipo_busqueda=RowResult.TIPO_BUSQUEDA_LIKE;
			List<RowResult> lista_r= clientesDAO.getFiltro(clienteRow);
	        
	        for (Iterator<RowResult> it = lista_r.iterator(); it.hasNext();) {
	            RowResult row = it.next();
	            System.out.println("id: " + row.getValue("id") + ", Nombre: " + row.getValue("Nombre"));
	        }
			
			
			
			//Actualizar en la tabla de clientes.
			
	        clienteRow.setValue("id", "123");
	        clienteRow.setValue("Nombre", "Carlos Mesa Agudelo");			
			clientesDAO.update(clienteRow);
			
			
			
			//Insertar en la tabla de clientes.
			RowResult clienteRow2= clientesDAO.getIntance_classBase();
			clienteRow2.setValue("id", "222");
			clienteRow2.setValue("Nombre", "Jose Miguel");			
			clientesDAO.insert(clienteRow2);
			
			//Busqueda de los datos en la tabla Clientes
			
	        codigo.value ="123";
	        codigo.tipo_busqueda=RowResult.TIPO_BUSQUEDA_LIKE;
			List<RowResult> lista_r2= clientesDAO.getFiltro(clienteRow);
	        
	        for (Iterator<RowResult> it = lista_r2.iterator(); it.hasNext();) {
	            RowResult row = it.next();
	            System.out.println("id: " + row.getValue("id") + ", Nombre: " + row.getValue("Nombre"));
	        }
			
			
			
			//Borrar en la tabla de clientes.									
			clientesDAO.delete(clienteRow2);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				context_db.stop();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}

}
