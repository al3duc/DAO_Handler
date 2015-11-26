/**
 * 
 */
package mapping_table;

import aleduc.dao_handler.core.RowResult;
import aleduc.dao_handler.core.Context_DAO;
import aleduc.dao_handler.core.DAO_Handler;
import aleduc.dao_handler.core.Interface_Instance_DAO;

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
