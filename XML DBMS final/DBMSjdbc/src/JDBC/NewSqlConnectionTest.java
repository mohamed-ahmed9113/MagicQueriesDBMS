package JDBC;

import java.sql.SQLException;
import java.util.Properties;

import DBMS.Schema;

import junit.framework.TestCase;

public class NewSqlConnectionTest extends TestCase {

	/*
	 * we assume that the schema given is valid 
	 * because testing of the constructro is 
	 * already done in the NewSqlDriverTeat class.
	 */
	private String validSchema1="C:\\Users\\MO77A\\Desktop\\ahmed.xml";
	private NewSqlConnection connection;
	private String Schema1="ahmed";
	public void testNewSqlConnection() throws SQLException {
		connection=new NewSqlConnection(validSchema1, new Properties());

	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		connection=new NewSqlConnection(validSchema1, new Properties());
	}

	public void testCreateStatement() {
		/*
		 * the constructor of the NewSqlStatment Class
		 * will be tested later
		 */
	}

	public void testGetSchema() {
		Schema schema=connection.getSchema();
		assertEquals(Schema1,schema.getName());
	}

	public void testGetUrl() {
		assertEquals(validSchema1,connection.getUrl());
	}

	
	public void testClose() throws SQLException {
		connection.close();
		assertEquals(null,connection.getSchema());
		assertEquals(null,connection.getUrl());
	}
	public void testIsClosed() throws SQLException {
		assertFalse(connection.isClosed());
	}

}
