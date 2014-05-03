package JDBC;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;

import DBMS.Schema;

public class NewSqlConnection implements Connection {
	private DBMS.Schema schema;
	private String url;
	private boolean isClosed;

	public NewSqlConnection(String url, Properties userInfo)
			throws SQLException {
		this.url = setUrl(url);
		String[]urlInformation=parseURL();

		try {
			schema = new Schema(urlInformation[0],urlInformation[1], true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new SQLException("Invalid Schema");
		}
		/*
		 * check whether connection can be done if not throw new SQLException
		 * else read the given schema in the url
		 */
	}
	private String setUrl(String url)
	{
		url = url.trim();
		String theUrl = url.substring(18);
		return url;
	}
	private String[] parseURL() throws SQLException {
		String[]result = new String[2];
		String theUrl = url;
		String nameInverted="";
		for(int i=theUrl.length()-1;i>=0;--i)
		{
			if(theUrl.charAt(i)=='\\')
			{
				theUrl=theUrl.substring(0,i+1);
				if(theUrl.charAt(i-1)=='\\')
					throw new SQLException("non valid url");
				break;
			}
			nameInverted+=theUrl.charAt(i);
		}
		result[0]=theUrl;
		String name = "";
		int lebs = nameInverted.length() - 1;
		for (int i = 0; i < nameInverted.length(); ++i) {
			name += nameInverted.charAt(lebs);
			--lebs;
		}
		name=name.replace(".xml","");
		result[1]=name;
		return result;
	}

	private String extractSchemaName(String s) {
		int length = s.length();
		String resultInverted = "";
		for (int i = length - 1; i >= 0; i--) {
			if (s.charAt(i) != '\\') {
				resultInverted += s.charAt(i);
			} else
				break;

		}
		String result = "";
		int lebs = resultInverted.length() - 1;
		for (int i = 0; i < resultInverted.length(); ++i) {
			result += resultInverted.charAt(lebs);
			--lebs;
		}
		return result;
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
	public void clearWarnings() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() throws SQLException {
		this.schema = null;
		this.url = null;
		isClosed = true;

	}

	@Override
	public void commit() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public Array createArrayOf(String arg0, Object[] arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Blob createBlob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Clob createClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NClob createNClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statement createStatement() throws SQLException {
		return new NewSqlStatement(this);
	}

	public DBMS.Schema getSchema() {
		return schema;
	}

	public String getUrl() {
		return url;
	}

	@Override
	public Statement createStatement(int arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statement createStatement(int arg0, int arg1, int arg2)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Struct createStruct(String arg0, Object[] arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getAutoCommit() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getCatalog() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getClientInfo(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getHoldability() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTransactionIsolation() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isClosed() throws SQLException {
		return (isClosed);
	}

	@Override
	public boolean isReadOnly() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isValid(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String nativeSQL(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CallableStatement prepareCall(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CallableStatement prepareCall(String arg0, int arg1, int arg2)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CallableStatement prepareCall(String arg0, int arg1, int arg2,
			int arg3) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PreparedStatement prepareStatement(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PreparedStatement prepareStatement(String arg0, int arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PreparedStatement prepareStatement(String arg0, int[] arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PreparedStatement prepareStatement(String arg0, String[] arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PreparedStatement prepareStatement(String arg0, int arg1, int arg2)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PreparedStatement prepareStatement(String arg0, int arg1, int arg2,
			int arg3) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void releaseSavepoint(Savepoint arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void rollback() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void rollback(Savepoint arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAutoCommit(boolean arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setCatalog(String arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setClientInfo(Properties arg0) throws SQLClientInfoException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setClientInfo(String arg0, String arg1)
			throws SQLClientInfoException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setHoldability(int arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setReadOnly(boolean arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public Savepoint setSavepoint() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Savepoint setSavepoint(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTransactionIsolation(int arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTypeMap(Map<String, Class<?>> arg0) throws SQLException {
		// TODO Auto-generated method stub

	}
//	public static void main (String args[])
//	{
//		String[]foo=NewSqlConnection.parseURL("C:\\Users\\MO77A\\Desktop\\foo.xml");
//		System.out.println(foo[0]);
//		System.out.println(foo[1]);
//	}

}
