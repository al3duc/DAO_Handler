/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package al3duc.dao_handler.core;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Diego Duque
 */
public interface Interface_DAO {
	
    /**
     * 
     * @param cb
     * @return
     * @throws Exception
     */
    public int insert(RowResult cb) throws Exception;
    /**
     * 
     * @param cb
     * @return
     * @throws Exception
     */
    public int update(RowResult cb) throws Exception;
    /**
     * 
     * @param cb
     * @return
     * @throws Exception
     */
    public RowResult getPrimaryKey(RowResult cb) throws Exception;
    /**
     * 
     * @param params
     * @return
     * @throws Exception
     */
    public List<RowResult> getFiltro(RowResult cb) throws Exception;
    /**
     * 
     * @param params
     * @return
     * @throws Exception
     */
    public int delete(RowResult cb_criterio) throws Exception;
}
