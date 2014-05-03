package JDBC;

import java.sql.Connection;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;

public class NewSqlDriver implements java.sql.Driver {
	final String protocol="jdbc:MagicQueries:"; 
	final String format=".xml";
	@Override
	/**
	 * this method checks whether the url
	 * is a valid url to MagicQueries database
	 * valid url is in the form 
	 * jdbc:MagicQyeries:dataBasePath
	 * dataBasePath ,ust end with .xml
	 */
	public boolean acceptsURL(String url) throws SQLException {
		url=url.trim();
		if(url==null)
			return false;
		if(url.startsWith(protocol)&&url.endsWith(".xml")) 
			return true;
		return false;
	}

	@Override
	/**
	 * default pass 123 user mo7a temp
	 */
	public Connection connect(String url, Properties userInfo) throws SQLException {
		if(!acceptsURL(url))
			throw new SQLException("invalid url");
		url=parseURL(url);
		Connection con=new NewSqlConnection(url,userInfo);
		return con;
	}
	private String parseURL(String url)
	{
		url=url.trim();
		url=url.replace(protocol,"");
		return url;
	}

	@Override
	public int getMajorVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMinorVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(String arg0, Properties arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean jdbcCompliant() {
		// TODO Auto-generated method stub
		return false;
	}
	public static void main(String[]args) throws SQLException
	{
		NewSqlDriver d=new NewSqlDriver();
		System.out.println(d.acceptsURL("jdbc:MagicQueries:C:\\mo7a.xml"));
		System.out.println(new NewSqlConnection("jdbc:MagicQueries:C:\\mo7a.xml",new Properties()).getSchema());
	}

}
