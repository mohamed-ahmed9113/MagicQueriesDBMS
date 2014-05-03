package JDBC;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.RestoreAction;

public class NewSqlResultSet implements ResultSet {
	private Statement statment;
	public static int pointer;
	private String tableName;
	private Object[][] resultTable;
	private String[] columnNames;

	public NewSqlResultSet(Statement statement, Object[][] table,
			String[] columnNames, String tableName) {
		this.statment = statement;
		this.pointer = -1;
		this.tableName = tableName;
		resultTable = new Object[table[0].length][table.length];
		for (int i = 0; i < resultTable.length; i++) {
			for (int j = 0; j < resultTable[0].length; j++) {
				resultTable[i][j] = table[j][i];
			}
		}
		this.columnNames = columnNames;
	}

	public int getColumnIndex(String columnLabel) {
		for (int i = 0; i < columnNames.length; i++) {
			if (columnNames[i].equals(columnLabel)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * this method is to test whether this index is in the table or Not and
	 * update the pointer on the resultTable .
	 */
	public boolean absolute(int row) throws SQLException {
		int l = resultTable.length;
		if (row <= l && row >= -1) {
			if (row <= l) {
				pointer = row;
			} else if (row == 0) {
				pointer = -1;
			} else if (row < 0) {
				pointer = -1 + row;
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * this method set the pointer to length of the result table+1;
	 */
	public void afterLast() {
		try {
			int l = resultTable.length;
			if (!(resultTable[0].length == 0)) {
				pointer = resultTable.length + 1;
			}
		} catch (Exception e) {
		}
	}

	/**
	 * Moves the pointer to the front of this ResultSet object, just before the
	 * first row. This method has no effect if the result set contains no rows.
	 */
	public void beforeFirst() throws SQLException {
		try {
			int l = resultTable.length;
			if (!(resultTable.length == 0)) {
				pointer = -1;
			}
		} catch (Exception e) {
		}
	}

	/**
	 * Releases this ResultSet object's database and JDBC resources immediately
	 * instead of waiting for this to happen when it is automatically closed.
	 */
	public void close() throws SQLException {
		statment = null;
		pointer = -1;
		resultTable = null;
	}

	/**
	 *	 
	 */
	public int findColumn(String colName) throws SQLException {
		int i = 0;
		for (String s : columnNames) {
			if (s.equals(colName)) {
				return i + 1;
			}
			i++;
		}
		return 0;
	}

	/**
	 * Moves the cursor to the first row in this ResultSet object.
	 */
	public boolean first() {
		if (resultTable.length > 0) {
			pointer = 0;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * ResultSet object as an Array object in the Java programming language.
	 * 
	 * @Parameters: columnIndex - the first column is 1, the second is 2,...
	 *              etc.
	 */
	// Not implemented yet(Array!!!)
	public Array getArray(int columnIndex) throws SQLException {
		return null;
	}

	/**
	 * @param columnIndex
	 * @return the boolean value of the pointer index in this column index.
	 * @throws SQLException
	 */
	public boolean getBoolean(int columnIndex) throws SQLException {
		if (columnIndex >= 0 && columnIndex < columnNames.length + 1) {
			if (pointer >= 0 && pointer < resultTable.length) {
				try {
					return (Boolean) resultTable[pointer][columnIndex - 1];
				} catch (Exception e) {
					System.out.println("inavalid column");
				}
			} else {
				try {
					throw new Exception("inValid row Please update the pointer");
				} catch (Exception e) {
				}
			}
		}
		return false;
	}

	/**
	 * @param ColumnLabel
	 * @return the boolean value of the corresponding index.
	 * @throws SQLException
	 */
	public boolean getBoolean(String ColumnLabel) throws SQLException {
		return getBoolean(getColumnIndex(ColumnLabel) + 1);
	}

	/**
	 * 
	 * @param columnIndex
	 * @return the Date value of the corresponding index.
	 * @throws SQLException
	 */
	public Date getDate(int columnIndex) throws SQLException {
		if (columnIndex < columnNames.length + 1 && columnIndex > -1) {
			if (pointer >= 0 && pointer < resultTable.length) {
				try {
					String s = (String) resultTable[pointer][columnIndex - 1];
					java.util.Date date = new java.util.Date(s);
					Date d = new Date(date.getTime());
					return d;
				} catch (Exception e) {
				}
			}
		}
		return null;
	}

	/**
	 * @param columnLabel
	 * @return the Date value of the corresponding columnName and the current
	 *         pointer index.
	 * @throws SQLException
	 */
	public Date getDate(String columnLabel) throws SQLException {
		return getDate(getColumnIndex(columnLabel) + 1);
	}

	/**
	 * @param columnIndex
	 * @return the Double value of the corresponding index.
	 * @throws SQLException
	 */
	public double getDouble(int columnIndex) throws SQLException {
		if (columnIndex >= 0 && columnIndex < columnNames.length + 1) {
			if (pointer >= 0 && pointer < resultTable.length) {
				try {
					return (Double) resultTable[pointer][columnIndex - 1];
				} catch (Exception e) {
					System.out.println("inavalid column");
				}
			} else {
				try {
					throw new Exception("inValid row Please update the pointer");
				} catch (Exception e) {
				}
			}
		}
		return 0;
	}

	public double getDouble(String columnName) throws SQLException {
		return getDouble(getColumnIndex(columnName) + 1);
	}

	@Override
	public int getFetchDirection() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public float getFloat(int columnIndex) throws SQLException {
		if (columnIndex >= 0 && columnIndex < columnNames.length + 1) {
			if (pointer >= 0 && pointer < resultTable.length) {
				try {
					return (Float) resultTable[pointer][columnIndex - 1];
				} catch (Exception e) {
					System.out.println("inavalid column");
				}
			} else {
				try {
					throw new Exception("inValid row Please update the pointer");
				} catch (Exception e) {
				}
			}
		}
		return 0;
	}

	public float getFloat(String columnLabel) throws SQLException {
		return getFloat(getColumnIndex(columnLabel) + 1);
	}

	public int getInt(int columnIndex) throws SQLException {
		if (columnIndex >= 0 && columnIndex < columnNames.length + 1) {
			if (pointer >= 0 && pointer < resultTable.length) {
				try {
					return (Integer) resultTable[pointer][columnIndex - 1];
				} catch (Exception e) {
					System.out.println("inavalid column");
				}
			} else {
				try {
					throw new Exception("inValid row Please update the pointer");
				} catch (Exception e) {
				}
			}
		}

		return 0;
	}

	public int getInt(String columnLabel) throws SQLException {
		return getInt(getColumnIndex(columnLabel) + 1);
	}

	public long getLong(int columnIndex) throws SQLException {
		if (columnIndex >= 0 && columnIndex < columnNames.length + 1) {
			if (pointer >= 0 && pointer < resultTable.length) {
				try {
					return (Long) resultTable[pointer][columnIndex - 1];
				} catch (Exception e) {
					System.out.println("inavalid column");
				}
			} else {
				try {
					throw new Exception("inValid row Please update the pointer");
				} catch (Exception e) {
				}
			}
		}
		return 0;
	}

	public long getLong(String columnLabel) throws SQLException {
		return getLong(getColumnIndex(columnLabel) + 1);
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		NewSqlResultSetMetaData resultMetaData = new NewSqlResultSetMetaData(
				resultTable, columnNames, tableName);
		return (resultMetaData);
	}

	public Object getObject(int columnIndex) throws SQLException {
		if (columnIndex >= 0 && columnIndex < columnNames.length + 1) {
			if (pointer >= 0 && pointer < resultTable.length) {
				try {
					return resultTable[pointer][columnIndex - 1];
				} catch (Exception e) {
					System.out.println("inavalid column");
				}
			} else {
				try {
					throw new Exception("inValid row Please update the pointer");
				} catch (Exception e) {
				}
			}
		}
		return null;
	}

	public Statement getStatement() throws SQLException {
		return statment;
	}

	public String getString(int columnIndex) throws SQLException {
		if (columnIndex >= 0 && columnIndex < columnNames.length + 1) {
			if (pointer >= 0 && pointer <= resultTable[0].length) {
				try {
					return ((String) resultTable[pointer][columnIndex - 1]);
				} catch (Exception e) {
					System.out.println("inavalid column");
				}
			} else {
				try {
					throw new Exception("inValid row Please update the pointer");
				} catch (Exception e) {
				}
			}
		}
		return null;
	}

	public boolean isAfterLast() throws SQLException {
		if (pointer == resultTable.length + 1) {
			return true;
		}
		return false;
	}

	public boolean isBeforeFirst() throws SQLException {
		if (pointer == -1) {
			return true;
		}
		return false;
	}

	public boolean isClosed() throws SQLException {
		if ((statment == null) && (pointer == -1) && columnNames == null) {
			return true;
		}
		return false;
	}

	public boolean isFirst() throws SQLException {
		if (pointer == 0) {
			return true;
		}
		return false;
	}

	public boolean isLast() throws SQLException {
		if (pointer == resultTable.length - 1)
			return true;
		return false;
	}

	public boolean last() throws SQLException {
		int l = resultTable.length;
		if (l > 0) {
			pointer = l - 1;
			return true;
		} else
			return false;
	}

	public boolean next() throws SQLException {
		int l = resultTable.length;
		return false;
	}

	public boolean previous() throws SQLException {
		if (pointer > 0) {
			pointer--;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void refreshRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean relative(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean rowDeleted() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean rowInserted() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean rowUpdated() throws SQLException {
		// TODO Auto-generated method stub
		return false;
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
	public void updateArray(int arg0, Array arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateArray(String arg0, Array arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAsciiStream(int arg0, InputStream arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAsciiStream(String arg0, InputStream arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAsciiStream(int arg0, InputStream arg1, int arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAsciiStream(String arg0, InputStream arg1, int arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAsciiStream(int arg0, InputStream arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAsciiStream(String arg0, InputStream arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBigDecimal(int arg0, BigDecimal arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBigDecimal(String arg0, BigDecimal arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBinaryStream(int arg0, InputStream arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBinaryStream(String arg0, InputStream arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBinaryStream(int arg0, InputStream arg1, int arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBinaryStream(String arg0, InputStream arg1, int arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBinaryStream(int arg0, InputStream arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBinaryStream(String arg0, InputStream arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBlob(int arg0, Blob arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBlob(String arg0, Blob arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBlob(int arg0, InputStream arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBlob(String arg0, InputStream arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBlob(int arg0, InputStream arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBlob(String arg0, InputStream arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBoolean(int arg0, boolean arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBoolean(String arg0, boolean arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateByte(int arg0, byte arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateByte(String arg0, byte arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBytes(int arg0, byte[] arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBytes(String arg0, byte[] arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCharacterStream(int arg0, Reader arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCharacterStream(String arg0, Reader arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCharacterStream(int arg0, Reader arg1, int arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCharacterStream(String arg0, Reader arg1, int arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCharacterStream(int arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCharacterStream(String arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateClob(int arg0, Clob arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateClob(String arg0, Clob arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateClob(int arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateClob(String arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateClob(int arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateClob(String arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateDate(int arg0, Date arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateDate(String arg0, Date arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateDouble(int arg0, double arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateDouble(String arg0, double arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateFloat(int arg0, float arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateFloat(String arg0, float arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateInt(int arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateInt(String arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateLong(int arg0, long arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateLong(String arg0, long arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNCharacterStream(int arg0, Reader arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNCharacterStream(String arg0, Reader arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNCharacterStream(int arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNCharacterStream(String arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNClob(int arg0, NClob arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNClob(String arg0, NClob arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNClob(int arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNClob(String arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNClob(int arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNClob(String arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNString(int arg0, String arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNString(String arg0, String arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNull(int arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNull(String arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateObject(int arg0, Object arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateObject(String arg0, Object arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateObject(int arg0, Object arg1, int arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateObject(String arg0, Object arg1, int arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateRef(int arg0, Ref arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateRef(String arg0, Ref arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateRowId(int arg0, RowId arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateRowId(String arg0, RowId arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateSQLXML(int arg0, SQLXML arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateSQLXML(String arg0, SQLXML arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateShort(int arg0, short arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateShort(String arg0, short arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateString(int arg0, String arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateString(String arg0, String arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTime(int arg0, Time arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTime(String arg0, Time arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTimestamp(int arg0, Timestamp arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTimestamp(String arg0, Timestamp arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean wasNull() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cancelRowUpdates() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearWarnings() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public Array getArray(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getAsciiStream(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(int columnIndex, int scale)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(String columnLabel, int scale)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getBinaryStream(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Blob getBlob(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Blob getBlob(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte getByte(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public byte getByte(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public byte[] getBytes(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getBytes(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reader getCharacterStream(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reader getCharacterStream(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Clob getClob(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Clob getClob(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getConcurrency() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getCursorName() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getDate(int columnIndex, Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getDate(String columnLabel, Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getFetchSize() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHoldability() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Reader getNCharacterStream(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reader getNCharacterStream(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NClob getNClob(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NClob getNClob(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNString(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNString(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getObject(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getObject(int arg0, Map<String, Class<?>> arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getObject(String arg0, Map<String, Class<?>> arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ref getRef(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ref getRef(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRow() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public RowId getRowId(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowId getRowId(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLXML getSQLXML(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLXML getSQLXML(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public short getShort(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public short getShort(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getString(String columnName) throws SQLException {
		return getString(getColumnIndex(columnName) + 1);
	}

	@Override
	public Time getTime(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time getTime(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time getTime(int arg0, Calendar arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time getTime(String arg0, Calendar arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timestamp getTimestamp(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timestamp getTimestamp(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timestamp getTimestamp(int arg0, Calendar arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timestamp getTimestamp(String arg0, Calendar arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getType() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public URL getURL(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URL getURL(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getUnicodeStream(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getUnicodeStream(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void moveToCurrentRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void moveToInsertRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	public Object[][] getresObjects() {
		return resultTable;
	}

	public static void main(String[] args) throws Exception {
		/*
		 * String statement = "eeeeeee"; java.util.Date date = new
		 * java.util.Date(); date.setYear(2010); String string = "Motaz";
		 * boolean b = true; Long f = (long) 555555; Object[][] resultTable = {
		 * { string, f, b }, { "Mohamed", f, b }, { string, f, b }, { "Ahmed",
		 * f, b } }; String[] colNames = { "Names", "Age", "isMarried" };
		 * NewSqlResultSet result = new NewSqlResultSet(null, resultTable,
		 * colNames, "ee"); result.absolute(4);
		 * System.out.println(result.pointer); NewSqlResultSetMetaData rsmd =
		 * new NewSqlResultSetMetaData(resultTable, result.columnNames, string);
		 * System.out.println(result.getString(1));
		 */}

}
