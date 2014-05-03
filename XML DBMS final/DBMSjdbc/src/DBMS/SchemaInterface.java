package DBMS;
/**
 * @author mahmoud elmaghrabi
 *
 */
public interface SchemaInterface 
{
	/**
	 * Method to create a new table in the schema
	 * @param tableName
	 * @throws Exception 
	 */
	public void createTable(String tableName) throws Exception;
	
	/**
	 * Method to delete the specified table from the schema
	 * @param tableName
	 * @throws Exception 
	 */
	public void dropTable(String tableName) throws Exception;
	
	/**
	 * Method to detect whether the schema contains the specified table or not
	 * @param tableName
	 * @return true if the schema contains the specified table and false otherwise
	 */
	public boolean contains(String tableName);
	
	/**
	 * Method to get the table URL
	 * @param tableName
	 * @return String : table URL
	 */
	public String getTableURL(String tableName);
		

}
