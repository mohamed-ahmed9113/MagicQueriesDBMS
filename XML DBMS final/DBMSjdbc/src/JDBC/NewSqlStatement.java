package JDBC;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.LinkedList;

import DBMS.Controller;
import DBMS.Table;
import DBMS.Validation;

public class NewSqlStatement implements Statement {
	private NewSqlConnection connection;
	private String tableName;
	private LinkedList<String>sqls=new LinkedList<String>();
	private boolean isClosed=false;
	private int timeOut=400;
	private  sqlParseObject[] theSqlParse;
	public final int SUCCESS_NO_INFO=-1;
	public final int EXECUTE_FAILED=-5;
	private ResultSet resultSet;
	int updateCount;
	/**
	 * Create new statement without controller
	 * to create the controller you must set the table name 
	 * @param con
	 * @throws Exception
	 */
	public NewSqlStatement(NewSqlConnection con) 
	{
		connection=con;
		intializeTheSqlParseArray();
		
	}
	public NewSqlStatement() 
	{
		intializeTheSqlParseArray();
		
	}
	private void intializeTheSqlParseArray()
	{
		
		theSqlParse=new sqlParseObject[6];
		String delete[]={"WHERE"};
		theSqlParse[0]=new sqlParseObject("DELETE",delete);
		String insert[]={"VALUES"};
		theSqlParse[1]=new sqlParseObject("INSERT",insert);
		String update[]={"SET","WHERE"};
		theSqlParse[2]=new sqlParseObject("UPDATE",update);
		String select[]={"FROM","WHERE"};
		theSqlParse[3]=new sqlParseObject("SELECT",select);
		String drop[]={""};
		theSqlParse[4]=new sqlParseObject("DROP",drop);
		String create[]={"("};
		theSqlParse[5]=new sqlParseObject("CREATE",create);
	}
	/**
	 * return array of strings to make the 
	 * statment in the form of ex:  select bla
	 *                              from bla
	 *                              where bla
	 * @param sql
	 * @return
	 * @throws SQLException
	 * ISTESTED
	 */
	public String[] parseSql(String sql) throws SQLException
	{
		sql=sql.trim(); // first remove spaces at the beginning
		if(sql.startsWith("CREATE"))
		{
			String[] result=new String[2];
			for(int i=0;i<sql.length();++i)
			{
				if(sql.charAt(i)=='(')
				{
					result[0]=sql.substring(0,i);
					result[1]=sql.substring(i);
					return result;
				}
			}
		}
		sqlParseObject current=null;
		/*
		 * determine the type of the query
		 */
		for(int i=0;i<theSqlParse.length;++i)
		{
			if(sql.startsWith(theSqlParse[i].mainString))
			{
				current=theSqlParse[i];
				break;
			}
		}
		/*
		 * if the type is not defined
		 * throw SQLEXception
		 */
		if(current==null)
			throw new SQLException("Not valid statmeny check first command ");
		int j=0;
		String[]theArray=current.subStrings;
		String[]temp;
		String[]result=new String[3]; // mo2kan
		int counter=0;
		
		for(j=0;j<theArray.length;j++)
		{
			try{
				theArray[j].charAt(0);
			}
			catch(Exception e){break;}
			if(sql.contains(theArray[j]))
			{
				temp=sql.split(theArray[j]);
				result[counter]=temp[0];
				++counter;
				sql=theArray[j]+temp[1];
			}
		}
		result[counter]=sql;
		++counter;
		String[] realResult=new String[counter];
		for(int i=0;i<counter;++i)
		{
			realResult[i]=result[i];
		}
		return realResult;
	}
	public String getTableName() {
		return tableName;
	}
	/**
	 * set the table name from the current sequel being executed
	 * then set controller to that table
	 * @param name
	 * @throws Exception
	 */
	public void setTableName(String name) throws Exception
	{
		tableName=name;
	}

	@Override
	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addBatch(String sql) throws SQLException {
		sqls.add(sql);

	}

	@Override
	public void cancel() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearBatch() throws SQLException {
		// TODO Auto-generated method stub
		while(!sqls.isEmpty())
		{
			sqls.remove();
		}

	}

	@Override
	public void clearWarnings() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() throws SQLException {
		// TODO Auto-generated method stub
		this.connection=null;
		this.sqls=null;
		this.tableName=null;
		this.theSqlParse=null;
		isClosed=true;
	}

	@Override
	/**
	 * since in our DBMS there
	 * is no probability to have two result sets
	 * this method return true only if ResultSet 
	 * is not null
	 */
	public boolean execute(String sql) throws SQLException {
		sql=sql.trim();
		boolean result;
		if(sql.startsWith("SELECT"))
		{
			executeQuery(sql);
			result=true;
		}
		else
		{
			executeUpdate(sql);
			result=false;
		}
		return result;
	}
//		ResultSet result;
//		try{
//			result=executeQuery(sql);
//		}
//		catch(Exception e)
//		{
//			return false;
//		}
//		if(result==null)
//			return false;
//		return true;
//	}

	@Override
	public boolean execute(String arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean execute(String arg0, int[] arg1) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean execute(String arg0, String[] arg1) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	/**
	 * excutes all statments in the batch 
	 * returns array of rowcounts of changes
	 * if statment fails to excute or returns resultset 
	 * add EXECUTE_FAILED to the array
	 */
	public int[] executeBatch() throws SQLException {
		int[]rowCounts = new int[sqls.size()];
		int i=0;
		while(!sqls.isEmpty()){
			String currentSql = sqls.removeFirst();
			try{
			executeUpdate(currentSql);
			rowCounts[i]=updateCount;
			}
			catch(Exception e){
				rowCounts[i]=EXECUTE_FAILED;
			}
			++i;
		}
		return rowCounts;
	}

	@Override
	/**
	 * THE VALIDATIONS CLASS CONTAINS 
	 * METHOD FINAL PARSE WHICH
	 * IS RESPONSIBLE FOR EXCUTING
	 * THE QUERE IT
	 * RETURN BOOLEAN
	 * missunderstod unresolved yet:
	 * it returns queries that returns single result set 
	 * if the query doesnt return aresult set the methos throws an exception
	 * never return null
	 */
	public ResultSet executeQuery(String sql) throws SQLException {
		sql=sql.trim();
		/*
		 * if the query is a data manipulating query 
		 * throw sqlException
		 */
		if(!sql.startsWith("SELECT"))
		{
			System.out.println("not start select");
			throw new SQLException("this query doesn't return aresultSet try:excuteUpdate()");
		}
		//parse the sql to the format
		//known to the validation class
		String[]sqlArray=parseSql(sql);
		boolean isSuccessful=false;
		NewSqlResultSet resultSet=null; //engez ya mezooo
		Validation validator;
		try {
		     validator =new Validation(sqlArray,this.connection.getSchema());
			 isSuccessful=validator.finalParsing();
		} catch (Exception e) {
			throw new SQLException("dataBase access error");
		}
		if(isSuccessful)
		{
			Object[][]result=validator.getSelectedRows(); //the result of the query
			resultSet=new NewSqlResultSet(this,validator.getSelectedRows(),validator.getColumnsNames(),validator.getTabelName());
			return resultSet; 
		}
		else
		throw new SQLException("not successful execution");
		
		
	}

	@Override
	/**
	 * execute queries that perform updates 
	 * on the database like: delete,update,insert
	 * or the DDL
	 * return the rowcount or 0 for the DDL statments 
	 * throws sqlexception if statment returns resultset or 
	 * incase of data base access error.
	 */
	public int executeUpdate(String sql) throws SQLException {
		sql=sql.trim();
		String[] sqlArray=parseSql(sql);
		if(sql.startsWith("SELECT"))
			throw new SQLException("This statment returns aResult set try: excuteQuery()");
		int rowCount;
		boolean succesful=false;
		try{
			Validation validator=new Validation(sqlArray,this.connection.getSchema());
			succesful=validator.finalParsing();
			if(!succesful)
				throw new SQLException();
			updateCount=SUCCESS_NO_INFO; // hn5odha men m3'rby---> number of rows undergone change 
		}
		catch(Exception e)
		{
			throw new SQLException("data base access error");
		}
		return updateCount;
		
//		rowCount=0; 
//		return rowCount;
//		}catch(Exception e) 
//		{
//			return 0; // returns zero if the sql is wrong
//		}
//		

	}

	@Override
	public int executeUpdate(String arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int executeUpdate(String arg0, int[] arg1) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int executeUpdate(String arg0, String[] arg1) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Connection getConnection() throws SQLException {
		return this.connection;
	}

	@Override
	public int getFetchDirection() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getFetchSize() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMaxFieldSize() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxRows() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean getMoreResults() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getMoreResults(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getQueryTimeout() throws SQLException {
		return timeOut;
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		return resultSet;
	}

	@Override
	public int getResultSetConcurrency() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getResultSetType() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getUpdateCount() throws SQLException {
		return updateCount;
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isClosed() throws SQLException {
		return(isClosed);
	}

	@Override
	public boolean isPoolable() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setCursorName(String arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setEscapeProcessing(boolean arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFetchDirection(int arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFetchSize(int arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMaxFieldSize(int arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMaxRows(int arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPoolable(boolean arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setQueryTimeout(int sec) throws SQLException {
		timeOut=sec;

	}
	private class sqlParseObject{
		String mainString;
		String[]subStrings;
		public sqlParseObject(String s,String[]st)
		{
			mainString=s;
			subStrings=st;
		}
	}
public static void main(String[] args) throws SQLException
{
	NewSqlStatement st=new NewSqlStatement();
	String sql="SELECT ALL FROM table";
	String[]x=st.parseSql(sql);
	for (int i = 0; i < x.length; i++) {
		System.out.println(x[i]);
	}
}
}
