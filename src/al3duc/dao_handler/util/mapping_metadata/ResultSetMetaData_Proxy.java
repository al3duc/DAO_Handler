package al3duc.dao_handler.util.mapping_metadata;

import java.sql.ResultSetMetaData;

public class ResultSetMetaData_Proxy {
	public ResultSetMetaData rsM;
	public String pks;
	
	public ResultSetMetaData_Proxy(ResultSetMetaData rsM, String pks) {
		super();
		this.rsM = rsM;
		this.pks = pks;
	}
}
