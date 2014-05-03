
import java.sql.*;
import java.util.Properties;

import junit.framework.TestCase;

public class JDMCTest extends TestCase {
	private DriverManager manager;
	private Connection connection;
	private Statement statement;
	private ResultSet result;
	private final String validUrl="jdbc:MagicQueries:D:\\College Work\\3rd Semester\\OOP\\Assignments\\DataBase\\School.xml"; //change it according to the pc
	private final String validUrlButNotSchema="jdbc:MagicQueries:D:\\College Work\\3rd Semester\\OOP\\Assignments\\DataBase\\College.xml";
	private final String[] invalidUrl={"hthtyhyh","C:\\grg.xml","jdbc:MagicQueries:C:\\grg.xml"};
	public final String[] validBatch=
		{
			
			"INSERT INTO Teachers (id , Name ,Course) VALUES (20 , \"7amada\" , \"mlksh da3wa\")",
			"UPDATE Teachers SET Age = 5 WHERE Name = Mo7a",
			
		};
	public final String[] inValidBatch=
		{
				"SELECT Age FROM Teachers WHERE Name = \"ahmed\" ",
				"UPDATE Teachers SET id = 5 WHERE Name= \"ahmed\""
		};
	private String validSelect="SELECT (id) FROM Teachers WHERE id >1";
	private String validSelectAll="SELECT * FROM Teachers";
	private String validUpdate="UPDATE Teachers SET id = 1 WHERE Name = \"Salah\"";
	private String validInsert="INSERT INTO Teachers (id , Name , Course) VALUES (20 , \"7amada\" ,\" mlksh da3wa\")";
	private String validDeleteallTable="DELETE Teachers";
	private String validDeleteWithWhere="DELETE Teachers WHERE Name=\"7amada\" ";

	protected void setUp() throws Exception {
		super.setUp();
		try {
			Class.forName("JDBC.Driver");
		} catch (ClassNotFoundException e) {
			fail("Driver class loading and regestrating failed");
			e.printStackTrace();
		}
	}
	public void testConnection()
	{
		try
		{
			connection=manager.getConnection(validUrl, new Properties());
			connection.close();
		}
		catch(Exception e){
			fail("url is valid must connect");
		}
		try
		{
			connection=manager.getConnection(validUrlButNotSchema, new Properties());
			fail("not a schema must throw exception");
		}
		catch(Exception e){}
	/*
	 * testing connection 
	 * with invalid urls
	 */
		for(int i=0;i<invalidUrl.length;++i)
		{
			try
			{
				connection=manager.getConnection(invalidUrl[i], new Properties());
				fail("url is invalid must throw exception");
			}
			catch(Exception e){}
		}
	}
	/**
	 * testing statements and results
	 * @throws SQLException 
	 */
	public void testSelectStatementsAndResults() throws SQLException
	{
		connection=manager.getConnection(validUrl);
		/*
		 * test the valid select
		 */
		statement=connection.createStatement();
		try{
			result=statement.executeQuery(validSelect);
		}catch(Exception e){fail("select returns result sert mustnt throw exceptio");}
		/*
		 * test the selected results
		 * Expected 2,3,4
		 */
		result.absolute(0);
		assertEquals(2,result.getInt(1));
		result.absolute(1);
		assertEquals(3,result.getInt(1));
		result.absolute(2);
		assertEquals(4,result.getInt(1));
		/*
		 * testing another valid select statement 
		 */
		statement=connection.createStatement();
		try{
			result=statement.executeQuery("SELECT (id,Name,Course) FROM Teachers WHERE Name=\"Ahmed\"");
		}catch(Exception e){fail("valid select never throw exception");}
		/*
		 * test resutl
		 * expected:2,Ahmed,Science
		 */
		result.absolute(0);
		assertEquals(2,result.getInt(1));
		result.absolute(0);
		assertEquals("Ahmed",result.getString(2));
		result.absolute(0);
		assertEquals("Science",result.getString(3));
		connection.close();
		statement.close();
	}
	public void testInsertStatements() throws SQLException
	{
		connection=manager.getConnection(validUrl);
		statement=connection.createStatement();
		try{
			/*
			 * expected to insert 
			 * 20
			 * 7mada
			 * mlksh d3wa at the end of columns:id,name,course
			 */
			statement.executeUpdate(validInsert);
		}catch(Exception e){fail("must not throw exception");}
	}
	public void testUpdateStatement() throws SQLException
	{
		connection=manager.getConnection(validUrl);
		statement=connection.createStatement();
		try
		{
			statement .executeUpdate(validUpdate);
			/*
			 * change id of Salah to 478
			 */
		}catch(Exception e){fail();}
	}
	/**
	 * the scenario creat,insert,select,update,select,deleteTable
	 * @throws SQLException 
	 */
	public void testCreatScenario1() throws SQLException
	{
		connection=manager.getConnection(validUrl);
		statement=connection.createStatement();
		try{
			statement.executeUpdate("CREATE TABLE newtable (id Integer,Name String)");
		}catch(Exception e){fail();};
	}
	public void testInsertAndSelectScenario1() throws SQLException
	{
		connection=manager.getConnection(validUrl);
		statement=connection.createStatement();
		try{
			statement.executeUpdate("INSERT INTO newtable (id) VALUES (20)");
		}catch(Exception e){fail();}
		try{
			result=statement.executeQuery("SELECT (id) FROM newtable");
		}catch(Exception e){fail();}
		result.absolute(0);
		assertEquals(20,result.getInt(1));
	}
	public void testUpdateThenSelect() throws SQLException 
	{
		connection=manager.getConnection(validUrl);
		statement=connection.createStatement();
		try{
			statement.executeUpdate("UPDATE newtable SET id = 478");
		}catch(Exception e){fail();}
		try{
			result=statement.executeQuery("SELECT (id) FROM newtable");
		}catch(Exception e){fail();}
		result.absolute(0);
		assertEquals(478,result.getInt(1));
	}
	public void testDrop() throws SQLException
	{
		connection=manager.getConnection(validUrl);
		statement=connection.createStatement();
		try{
			statement.executeUpdate("DROP newtable");
		}catch(Exception e){fail();};
	}

}
