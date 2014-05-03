package DBMS;
/**
 * @author mahmoud elmaghrabi
 */

public interface tableInterface 
{
	
	public void createColumn(String columnName , String type) throws Exception;
	
	public void deleteColumn(String columnName);
	
	public boolean contains (String columnName);
	

}
