package DBMS;
import junit.framework.TestCase;

public class ValidationTest extends TestCase {

	public void testGetSQL() {
		// fail("Not yet implemented");
		String[] input1 = { "asda", "sadas" };
		Validation m = new Validation(input1);
		m.validate();
		input1 = null;
		assertEquals(0, m.getSQL());
		// insert method
		String[] input2 = { " INSERT INTO tableName ( column1 , column2 ) ",
				" VALUES ( \"Ahmed\" ,\"Mohamed\" ) " };
		m = new Validation(input2);
		m.validate();
		assertEquals(1, m.getSQL());
		// delete all method
		String[] input3 = { " DELETE tableName" };
		m = new Validation(input3);
		m.validate();
		assertEquals(2, m.getSQL());
		// delete method
		String[] input4 = { " DELETE tableName ", " WHERE columnName = 899 " };
		m = new Validation(input4);
		m.validate();
		assertEquals(2, m.getSQL());
		// update All method
		String[] input5 = { " UPDATE tableName ",
				" SET column1 = \"value1\" , column2 = \"value2\" " };
		m = new Validation(input5);
		m.validate();
		assertEquals(3, m.getSQL());
		// update method
		String[] input6 = { " UPDATE tableName ",
				" SET column1 =\" value1\" , column2 = 67 ",
				" WHERE columnName = \"value\" " };
		m = new Validation(input6);
		m.validate();
		assertEquals(3, m.getSQL());
		// select all without conditions method
		String[] input7 = { "SELECT *", "FROM tableName" };
		m = new Validation(input7);
		m.validate();
		assertEquals(4, m.getSQL());
		// select all with conditions method
		String[] input8 = { "SELECT *", "FROM tableName",
				" WHERE columnName = \"value\" " };
		m = new Validation(input8);
		m.validate();
		assertEquals(4, m.getSQL());
		// select without conditions method
		String[] input9 = { "SELECT (column1,column2)", " FROM  tableName" };
		m = new Validation(input3);
		m.validate();
		m = new Validation(input9);
		m.validate();
		assertEquals(4, m.getSQL());
		// select with conditions method
		String[] input10 = { "SELECT (column1,column2)", "FROM  tableName",
				" WHERE columnName = \"oiiio\" " };
		m = new Validation(input10);
		m.validate();
		assertEquals(4, m.getSQL());
		// input length > 3 lines
		String[] input11 = { "SELECT (column1,column2)", "FROM  tableName",
				" WHERE columnNa    me = value ", "ahmed" };
		m = new Validation(input11);
		m.validate();
		assertEquals(0, m.getSQL());
	}

	public void testValidate() {
		Validation m ;
		String[] input1 = { "asda", "sadas" };
		m = new Validation(input1);
		assertEquals(false, m.validate());
		// insert method
		String[] input2 = { " INSERT INTO tableName ( column1 , column2 ) ",
				" VALUES ( \"Ahmed\" , \"Mohamed Hesham\" ) " };
		m = new Validation(input2);
		assertEquals(true, m.validate());
		// delete all method
		String[] input3 = { " DELETE tableName" };
		m = new Validation(input3);
		assertEquals(true, m.validate());
		// delete method
		String[] input4 = { " DELETE tableName ", " WHERE columnName = 2012 " };
		m = new Validation(input4);
		assertEquals(true, m.validate());
		// update All method
		String[] input5 = { " UPDATE tableName ",
				" SET column1 = \"value1\" , column2 = \"value2\" ,column3=78" };
		m = new Validation(input5);
		assertEquals(true, m.validate());
		// update method
		String[] input6 = { " UPDATE tableName ",
				" SET column1 = \"value1\" , column2 = \"value2\" ",
				" WHERE columnName <> 500 " };
		m = new Validation(input6);
		assertEquals(true, m.validate());
		// select all without conditions method
		String[] input7 = { "SELECT *", "FROM tableName" };
		m = new Validation(input7);
		assertEquals(true, m.validate());
		// select all with conditions method
		String[] input8 = { "SELECT *", "FROM tableName",
				" WHERE columnName >= \"ahmed\" " };
		m = new Validation(input8);
		assertEquals(true, m.validate());
		// select without conditions method
		String[] input9 = { "SELECT (column1,column2)", " FROM  tableName" };
		m = new Validation(input9);
		assertEquals(true, m.validate());
		// select with conditions method
		String[] input10 = { "SELECT (column1,column2)", "FROM  tableName",
				" WHERE columnName = \"value\" " };
		m = new Validation(input10);
		assertEquals(true, m.validate());
		// input length > 3 lines
		String[] input11 = { "SELECT (column1,column2)", "FROM  tableName"," WHERE columnName = \"value\" " };
		m = new Validation(input11);
		assertEquals(true, m.validate());
		// fail("Not yet implemented");
	}// 4269965

}
