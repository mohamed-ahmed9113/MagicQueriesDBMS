package JDBC;

import java.sql.*;


public class JDMCexample {

	/**
	 * @param args
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws SQLException {
		try {
			Class.forName("JDBC.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Connection conn = DriverManager.getConnection(
				"jdbc:MagicQueries:C:\\Users\\MO77A\\Desktop\\ahmed.xml",
				"root", "root");
		// Driver d=DriverManager.getDriver("jdbc:mysql://Mo77a/examples");
		// System.out.println(d.getPropertyInfo("jdbc:mysql://Mo77a/Examples",
		// null));

		Statement stmt;
		String query = "INSERT INTO fat7y (Age , Name , Adress) VALUES (90 , 7amadua , mlksh da3wa)";
		stmt = conn.createStatement();
		int a=stmt.executeUpdate(query);
		System.out.println(a);
//		ResultSet b = stmt.executeQuery(query);
//		NewSqlResultSet r = (NewSqlResultSet) b;
//		Object[][] obj = r.getresObjects();
//		
//		b.absolute(1);
//		System.out.println(b.getString(1));
		stmt.close();
		conn.close();

	}

}
