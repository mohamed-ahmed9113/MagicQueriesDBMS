package JDBC;

import java.sql.SQLException;
import java.util.Properties;

import junit.framework.TestCase;
/**
 * 
 * @author MO77A
 * Warnning: working with relative pathes 
 */
public class NewSqlDriverTest extends TestCase {
	private NewSqlDriver driver=new NewSqlDriver();
	final String[] inValidUrls={"eifbibrebugbeg",
								"jdbc:wewefff",
								"jdbc:magicQueries:wwfwwf"
								,"jdbc:MagicQueries:C:koko\\kkk"
								,"JDBC:MagicQueries:C:\\gogtol.xml"
								};
	final String AcceptedButNotAvailable[]={
										"jdbc:MagicQueries:fat7y.xml"
										,"jdbc:MagicQueries:\\fat7y.xml"
										,"jdbc:MagicQueries:C:\\Users\\MO77A\\Desktop\\foo.xml"
										,"jdbc:MagicQueries:C:\\Users\\MO77A\\Desktop\\\fat7y.xml"
										,"jdbc:MagicQueries:C:\\Users\\MO77A\\Desktop\\\\fat7y.xml"
										};
	final String Valid="jdbc:MagicQueries:C:\\Users\\MO77A\\Desktop\\ahmed.xml";

	public void testAcceptsURL() throws SQLException {
		/*
		 * loop on the invalid urls
		 * apply accepturl()on each of them
		 * if the result is true 
		 * there is a problem
		 * ie:expected result is false
		 */
		for(int i=0;i<inValidUrls.length;++i)
		{
//			System.out.println("index of ivalid path is "+i);
//			System.out.println(i+" "+inValidUrls[i]);
			assertFalse(driver.acceptsURL(inValidUrls[i]));
		}
		/*
		 * acceptUrl()doesn't depend on the path itself
		 * it tests whether the format of the url is 
		 * accepted or not.
		 */
		for(int i=0;i<AcceptedButNotAvailable.length;++i)
		{
			assertTrue(driver.acceptsURL(AcceptedButNotAvailable[i]));
		}
		assertTrue(driver.acceptsURL(Valid));
	}

	public void testConnect() {
		for(int i=0;i<inValidUrls.length;++i)
		{
			try{
				driver.connect(inValidUrls[i],new Properties());
				fail("invalid urls must throw SqlException");
			}catch(Exception e)
			{
				continue;
			}
		}
		for(int i=0;i<AcceptedButNotAvailable.length;++i)
		{
		try{
			driver.connect(AcceptedButNotAvailable[i], new Properties());
			fail("must throw sqlException if it cant connect to the schema");
		}catch(Exception e){}
		}
		try{
			driver.connect(Valid, new Properties());
		}
		catch(Exception e)
		{
			fail("this is avalid url connection must be done");
		}
	}

}
