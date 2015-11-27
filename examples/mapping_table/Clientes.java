/**
 * 
 */
package mapping_table;

import al3duc.dao_handler.core.Context_DAO;
import al3duc.dao_handler.core.DAO_Handler;
import al3duc.dao_handler.core.Interface_Instance_DAO;
import al3duc.dao_handler.core.RowResult;

/**
 * @author Diego Duque
 *
 */
public class Clientes extends DAO_Handler implements Interface_Instance_DAO {

	/**
	 * @param _cn
	 */
	public Clientes(Context_DAO _cn) {
		super(_cn);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see aleduc.dao_handler.core.Interface_Instance_DAO#getIntance_classBase()
	 */
	@Override
	public RowResult getIntance_classBase() throws Exception {
		// TODO Auto-generated method stub
		return buildCB("Clientes");
	}

}
