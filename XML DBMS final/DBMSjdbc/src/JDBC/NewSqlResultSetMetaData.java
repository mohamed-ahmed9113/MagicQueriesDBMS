package JDBC;

import java.sql.Date;
import java.sql.SQLException;

public class NewSqlResultSetMetaData implements java.sql.ResultSetMetaData {

	private Object[][] resultSet;
	private String[] columnNames;
	private String tableName;
	public final static int columnNoNulls = 0;
	public final static int columnNullable = 1;

	public NewSqlResultSetMetaData(Object[][] resultTable, String[] columnNames,
			String tableName) {
		this.resultSet = resultTable;
		this.columnNames = columnNames;
		this.tableName = tableName;
	}

	public int getColumnCount() throws SQLException {
		return columnNames.length;
	}

	public String getColumnLabel(int column) throws SQLException {
		return columnNames[column - 1];
	}

	public String getColumnName(int column) throws SQLException {
		return columnNames[column - 1];
	}

	public int getColumnType(int column) throws SQLException {
		Object obj = resultSet[0][column - 1];

		if (obj instanceof Integer) {
			return 4;
		} else if (obj instanceof Long) {
			return 6;
		} else if (obj instanceof Double) {
			return 8;
		} else if (obj instanceof Float) {
			return 6;
		} else if (obj instanceof String) {
			return 12;
		} else if (obj instanceof Date) {
			return 9;
		} else if (obj instanceof Boolean) {
			return 16;
		}
		return 0;
	}

	public String getColumnTypeName(int column) throws SQLException {
		Object obj = resultSet[0][column - 1];

		if (obj instanceof Integer) {
			return "Integer";
		} else if (obj instanceof Long) {
			return "Long";
		} else if (obj instanceof Double) {
			return "Double";
		} else if (obj instanceof Float) {
			return "Float";
		} else if (obj instanceof String) {
			return "String";
		} else if (obj instanceof Date) {
			return "Date";
		}
		return null;
	}

	public String getTableName(int column) throws SQLException {
		return tableName;
	}

	public boolean isAutoIncrement(int column) throws SQLException {
		return true;
	}

	public boolean isCaseSensitive(int column) throws SQLException {
		return true;
	}

	public int isNullable(int column) throws SQLException {
		try {
			if (column < columnNames.length) {
				Object obj = resultSet[0][column - 1];
				if ((obj instanceof String) || (obj instanceof Date)) {
					return columnNullable;
				} else {
					return columnNoNulls;
				}
			}
		} catch (Exception e) {
			try {
				throw new Exception("undefined data type");
			} catch (Exception e1) {
			}
		}
		return 0;
	}

	public boolean isReadOnly(int column) throws SQLException {
		return false;
	}

	public boolean isSearchable(int column) throws SQLException {
		return true;
	}

	public boolean isWritable(int column) throws SQLException {
		return true;
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
	public String getCatalogName(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnClassName(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getColumnDisplaySize(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPrecision(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getScale(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getSchemaName(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCurrency(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDefinitelyWritable(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSigned(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

}
