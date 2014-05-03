package JDBC;

import java.sql.SQLException;
import java.util.Properties;

import junit.framework.TestCase;

public class NewSqlStatementTest extends TestCase {
	private NewSqlConnection connection;
	private NewSqlStatement statement;
	private final String validSchema="ahmed.xml";
	private String Schema="ahmed";
	private String validTable="fat7y";
	private String[] addedBatch;
	public final String[] validBatch=
	{
		/*
		 * put some valid sqls here no return
		 */
		"INSERT INTO fat7y (Age , Name , Adress) VALUES (20 , \"7amada\" , \"mlksh da3wa\")",
		"UPDATE fat7y SET Age = 5 WHERE Name = Mo7a",
		
	};
	public final String[] inValidBatch=
	{
		/*
		 * put someinvalid sqls here
		 */
			"SELECT Age From fat7y WHERE Name = \"Mo7a\" ",
			"UPDATE fat7y SET Age = 5 WHERE Nam= Mo7a"
	};
	public final String[] queriesReturnResult=null;
	public final String[] queriesDontReturnResult=null;
	

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		connection=new NewSqlConnection(validSchema, new Properties());
		statement=new NewSqlStatement(connection);
	}

	public void testAddBatch() {
		fail("Not yet implemented");
	}

	public void testClearBatch() {
		fail("Not yet implemented");
	}


	public void testExecuteString() {
		fail("Not yet implemented");
	}

	public void testExecuteBatch() throws SQLException {
		statement.clearBatch();
		for (int i = 0; i < validBatch.length; i++) {
			statement.addBatch(validBatch[i]);
		}
		int numberOfBatches=validBatch.length;
		int[]validBatchCount=statement.executeBatch();
		for(int i=0;i<numberOfBatches;++i)
		{
			assertEquals(validBatchCount[i],statement.SUCCESS_NO_INFO);
		}
		//--------------------------------------------
		//testing for invalid batch
		statement.clearBatch();
		for (int i = 0; i < inValidBatch.length; i++) {
			statement.addBatch(inValidBatch[i]);
		}
		int numberOfBatches2=inValidBatch.length;
		int[]invalidBatchCount2=statement.executeBatch();
		for(int i=0;i<numberOfBatches2;++i)
		{
			assertEquals(invalidBatchCount2[i],statement.EXECUTE_FAILED);
		}
	}

	public void testExecuteQuery() {
		for(int i=0;i<queriesDontReturnResult.length;++i)
		{
			try{
				statement.executeQuery(queriesDontReturnResult[i]);
				fail("the query doesnt return resultSet must throw an exception");
			}catch(Exception e){}
		}
		for(int i=0;i<queriesReturnResult.length;++i)
		{
			try{
				statement.executeQuery(queriesReturnResult[i]);
			}catch(Exception e)
			{
				fail("the query return resultSet mustnot throw an exception");
			}
		}
	}

	public void testExecuteUpdateString() {
		for(int i=0;i<queriesDontReturnResult.length;++i)
		{
			try{
				statement.executeQuery(queriesDontReturnResult[i]);
				
			}catch(Exception e){fail("the query doesnt return resultSet mustnot throw an exception");}
		}
		for(int i=0;i<queriesReturnResult.length;++i)
		{
			try{
				statement.executeQuery(queriesReturnResult[i]);
				fail("the query return resultSet must throw an exception");
			}catch(Exception e){}
		}
	}
	public void testClose() {
		fail("Not yet implemented");
	}

}
