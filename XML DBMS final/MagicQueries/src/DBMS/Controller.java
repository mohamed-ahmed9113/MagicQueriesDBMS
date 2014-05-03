package DBMS;


public class Controller

{
	private Schema schema;
	private Table table;

	/**
	 * The Controller Constructor
	 * 
	 * @param schema
	 * @throws Exception
	 */
	public Controller(Schema schema, String tableName) throws Exception {
		this.schema = schema;
		tableIsExist(tableName);
		this.table = new Table(schema.getTableURL(tableName));
	}

	public Controller(Schema schema) {
		this.schema = schema;
	}

	public void setTabelName(String tableName) throws Exception {
		tableIsExist(tableName);
		this.table = new Table(schema.getTableURL(tableName));
	}

	/**
	 * Method to get the current Schema
	 * 
	 * @return current Schema
	 */
	public Schema getCurrentSchema() {
		return this.schema;
	}

	/**
	 * Method to set the Current Schema
	 * 
	 * @param schema
	 */
	public void setCurrentSchema(Schema schema) {
		this.schema = schema;
	}

	/**
	 * Method to check if a table exists
	 * 
	 * @param tableName
	 * @return true or false
	 * @throws Exception
	 */
	private boolean tableIsExist(String tableName) throws Exception {
		if (schema.contains(tableName)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Method to check whether the condition is valid or not according to the
	 * column data type
	 * 
	 * @param columnName
	 * @param operator
	 * @param operand
	 * @return true or false
	 * @throws Exception
	 */
	private boolean isValidCondition(String columnName, String operator,
			String operand) throws Exception {
		String dataType = table.getColumnDataType(columnName);
		boolean isValidNumOperator = (operator.equals("=")
				|| operator.equals("<") || operator.equals(">")
				|| operator.equals("<=") || operator.equals(">=") || operator
				.equals("<>"));
		boolean isValidStrOperator = (operator.equals("=") || operator
				.equals("<>"));
		boolean isValid = false;

		if (dataType.equals(Table.DATA_INTEGER)
				|| dataType.equals(Table.DATA_LONG)) {
			if (isValidNumOperator) {
				try {
					Long.parseLong(operand);
					isValid = true;
				} catch (Exception e) {
					isValid = false;
				}
			}
		} else if (dataType.equals(Table.DATA_FLOAT)
				|| dataType.equals(Table.DATA_DOUBLE)) {
			if (isValidNumOperator) {
				try {
					Double.parseDouble(operand);
					isValid = true;

				} catch (Exception e) {
					isValid = false;
				}
			}
		} else if (dataType.equals(Table.DATA_DATE)
				|| dataType.equals(Table.DATA_STRING)
				|| dataType.equals(Table.DATA_BOOLEAN)) {
			isValid = isValidStrOperator;
		}
		return isValid;
	}

	/**
	 * Method to apply the condition on the cells
	 * 
	 * @param operand1
	 * @param operand2
	 * @param operator
	 * @return true or false
	 */

	private boolean applyConditionToInteger(Object operand1obj, Long operand2,
			String operator) {
		long operand1;
		if (operand1obj instanceof Integer) {
			operand1 = (Integer) operand1obj;
		} else {
			operand1 = (Long) operand1obj;
		}
		if (operator.equals("=")) {
			return (operand1 == operand2);
		} else if (operator.equals("<=")) {
			return (operand1 <= operand2);
		} else if (operator.equals(">=")) {
			return (operand1 >= operand2);
		} else if (operator.equals("<")) {
			return (operand1 < operand2);
		} else if (operator.equals(">")) {
			return (operand1 > operand2);
		} else if (operator.equals("<>")) {
			return (operand1 != operand2);
		} else
			return false;
	}

	/**
	 * Method to apply the condition on the cells
	 * 
	 * @param operand1
	 * @param operand2
	 * @param operator
	 * @return true or false
	 */

	private boolean applyConditionToDouble(Object operand1obj, Double operand2,
			String operator) {
		double operand1;
		if (operand1obj instanceof Float) {
			operand1 = (Float) operand1obj;
		} else {
			operand1 = (Double) operand1obj;
		}
		if (operator.equals("=")) {
			return (Math.abs(operand1 - operand2) <= (double)0.000001);
		} else if (operator.equals("<=")) {
			return (operand1 <= operand2);
		} else if (operator.equals(">=")) {
			return (operand1 >= operand2);
		} else if (operator.equals("<")) {
			return (operand1 < operand2);
		} else if (operator.equals(">")) {
			return (operand1 > operand2);
		} else if (operator.equals("<>")) {
			return (Math.abs(operand1 - operand2) > (double)0.000001);
		} else
			return false;
	}

	/**
	 * Method to apply the condition on the cells
	 * 
	 * @param operand1
	 * @param operand2
	 * @param operator
	 * @return true or false
	 */

	private boolean applyConditionToString(Object operand1Obj, String operand2,
			String operator) {
		String operand1;
		if (operand1Obj instanceof Boolean) {
			operand1 = operand1Obj.toString();
		} else {
			operand1 = (String) operand1Obj;
		}
		if (operator.equals("=")) {
			return (operand1.equals(operand2));
		} else if (operator.equals("<>")) {
			return (!operand1.equals(operand2));
		} else
			return false;
	}

	/**
	 * Method to delete rows according to certain condition
	 * 
	 * @param condition
	 * @throws Exception
	 */

	public void delete(String[] condition) throws Exception {

		String columnName = condition[0];
		String conditionOperand = condition[2];
		String operator = condition[1];
		int k = 0;
		if (isValidCondition(columnName, operator, conditionOperand)) {
			String dataType = table.getColumnDataType(columnName);
			Object[] cells = table.getAllCellsIn(columnName);

			String[] columnNames = table.getColumns();
			if (dataType.equals(Table.DATA_INTEGER)
					|| dataType.equals(Table.DATA_LONG)) {
				Long conOperand = Long.parseLong(conditionOperand);
				for (int i = 0; i < cells.length; i++) {
					if (cells[i] != null
							&& applyConditionToInteger(cells[i], conOperand,
									operator)) {
						for (int j = 0; j < columnNames.length; j++) {
							table.deleteCell(columnNames[j], i - k);
						}
						k++;
					}
				}

			} else if (dataType.equals(Table.DATA_FLOAT)
					|| dataType.equals(Table.DATA_DOUBLE)) {

				Double conOperand = Double.parseDouble(conditionOperand);
				for (int i = 0; i < cells.length; i++) {
					if (cells[i] != null
							&& applyConditionToDouble(cells[i], conOperand,
									operator)) {
						for (int j = 0; j < columnNames.length; j++) {
							table.deleteCell(columnNames[j], i);
							k++;
						}
					}
				}

			} else if (dataType.equals(Table.DATA_DATE)
					|| dataType.equals(Table.DATA_STRING)
					|| dataType.equals(Table.DATA_BOOLEAN)) {

				String conOperand = (String) (conditionOperand);
				for (int i = 0; i < cells.length; i++) {
					if (cells[i] != null
							&& applyConditionToString(cells[i], conOperand,
									operator)) {
						for (int j = 0; j < columnNames.length; j++) {
							table.deleteCell(columnNames[j], i - k);
						}
						k++;

					}
				}
			}
		}
		table.applyChanges();
	}

	/**
	 * Method to insert certain values in certain columns
	 * 
	 * @param columnNames
	 * @param values
	 * @throws Exception
	 */

	public void insert(String[] columnNames, String[] values) throws Exception {
		for(int i = 0 ; i < columnNames.length ; i++)
		{
			table.insertCell(columnNames[i], values[i]);
		}
		table.applyChanges();
	}

	/**
	 * Method to delete rows according to certain condition
	 * 
	 * @param condition
	 * @throws Exception
	 */

	public void update(String[] columnNames, String[] newValues,
			String[] condition) throws Exception {

		String columnName = condition[0];
		String conditionOperand = condition[2];
		String operator = condition[1];

		if (isValidCondition(columnName, operator, conditionOperand)) {
			String dataType = table.getColumnDataType(columnName);
			Object[] cells = table.getAllCellsIn(columnName);

			if (dataType.equals(Table.DATA_INTEGER)
					|| dataType.equals(Table.DATA_LONG)) {
				Long conOperand = Long.parseLong(conditionOperand);
				for (int i = 0; i < cells.length; i++) {
					if (cells[i] != null
							&& applyConditionToInteger(cells[i], conOperand,
									operator)) {
						for (int j = 0; j < columnNames.length; j++) {
							table.setCell(columnNames[j], i, newValues[j]);
						}

					}
				}

			} else if (dataType.equals(Table.DATA_FLOAT)
					|| dataType.equals(Table.DATA_DOUBLE)) {
				Double conOperand = Double.parseDouble(conditionOperand);
				for (int i = 0; i < cells.length; i++) {
					if (cells[i] != null
							&& applyConditionToDouble(cells[i], conOperand,
									operator)) {
						for (int j = 0; j < columnNames.length; j++) {
							table.setCell(columnNames[j], i, newValues[j]);
						}
					}
				}

			} else if (dataType.equals(Table.DATA_DATE)
					|| dataType.equals(Table.DATA_STRING)
					|| dataType.equals(Table.DATA_BOOLEAN)) {
				String conOperand = (String) (conditionOperand);
				for (int i = 0; i < cells.length; i++) {
					if (cells[i] != null
							&& applyConditionToString(cells[i], conOperand,
									operator)) {
						for (int j = 0; j < columnNames.length; j++) {
							table.setCell(columnNames[j], i, newValues[j]);
						}
					}
				}
			}
		}
		table.applyChanges();
	}

	/**
	 * Method to update all specified Columns to certain values
	 * 
	 * @param columnName
	 * @param newValues
	 * @throws Exception
	 */
	public void updateAll(String[] columnName, String[] newValues)
			throws Exception {
		for (int i = 0; i < columnName.length; i++) {
			table.setAllCellsTo(columnName[i], newValues[i]);
		}
		table.applyChanges();
	}

	/**
	 * Method to delete all columns' cells in the table
	 * 
	 * @throws Exception
	 */

	public void deleteAll() throws Exception {
		String[] columnNames = table.getColumns();

		for (int i = 0; i < columnNames.length; i++) {
			table.deleteAllCells(columnNames[i]);
		}
		table.applyChanges();
	}

	/**
	 * Method to return all table columns
	 * 
	 * @return
	 * @throws Exception
	 */

	public Object[][] selectAll() throws Exception {
		String[] columnNames = table.getColumns();
		Object[][] tableColumns = new Object[columnNames.length][table
				.getAllCellsIn(columnNames[0]).length];

		for (int i = 0; i < columnNames.length; i++) {
			tableColumns[i] = table.getAllCellsIn(columnNames[i]);
		}

		return tableColumns;
	}

	/**
	 * Method to return all rows that satisfies certain condition
	 * 
	 * @param condition
	 * @throws Exception
	 */
	public Object[][] selectAll(String[] condition) throws Exception {

		String columnName = condition[0];
		String conditionOperand = condition[2];
		String operator = condition[1];
		Object[][] result = null;

		String[] columnNames = table.getColumns();

		if (isValidCondition(columnName, operator, conditionOperand)) {
			String dataType = table.getColumnDataType(columnName);
			Object[] cells = table.getAllCellsIn(columnName);

			if (dataType.equals(Table.DATA_INTEGER)
					|| dataType.equals(Table.DATA_LONG)) {
				Long conOperand = Long.parseLong(conditionOperand);
				int k = 0;
				for (int i = 0; i < cells.length; i++) {
					if (cells[i] != null
							&& applyConditionToInteger(cells[i], conOperand,
									operator)) {
						k++;
					}

				}
				result = new Object[columnNames.length][k];
				k = 0;
				for (int i = 0; i < cells.length; i++) {

					if (cells[i] != null
							&& applyConditionToInteger(cells[i], conOperand,
									operator)) {
						for (int j = 0; j < result.length; j++) {
							result[j][k] = table.getValue(columnNames[j], i);
						}
						k++;

					}
				}

			} else if (dataType.equals(Table.DATA_FLOAT)
					|| dataType.equals(Table.DATA_DOUBLE)) {
				Double conOperand = Double.parseDouble(conditionOperand);
				int k = 0;

				for (int i = 0; i < cells.length; i++) {
					if (cells[i] != null
							&& applyConditionToDouble(cells[i], conOperand,
									operator)) {
						k++;
					}
				}
				result = new Object[columnNames.length][k];
				k = 0;

				for (int i = 0; i < cells.length; i++) {
					if (cells[i] != null
							&& applyConditionToDouble(cells[i], conOperand,
									operator)) {
						for (int j = 0; j < columnNames.length; j++) {
							result[j][k] = table.getValue(columnNames[j], i);
						}
						k++;

					}
				}

			} else if (dataType.equals(Table.DATA_DATE)
					|| dataType.equals(Table.DATA_STRING)
					|| dataType.equals(Table.DATA_BOOLEAN)) {
				String conOperand = (String) (conditionOperand);
				int k = 0;

				for (int i = 0; i < cells.length; i++) {
					if (cells[i] != null
							&& applyConditionToString(cells[i], conOperand,
									operator)) {
						k++;

					}

				}
				result = new Object[columnNames.length][k];
				k = 0;
				for (int i = 0; i < cells.length; i++) {
					if (cells[i] != null
							&& applyConditionToString(cells[i], conOperand,
									operator)) {
						for (int j = 0; j < columnNames.length; j++) {
							result[j][k] = table.getValue(columnNames[j], i);
						}
						k++;

					}
				}
			}
		}
		return result;
	}

	/**
	 * Method to return rows in certain columns that satisfies certain condition
	 * 
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public Object[][] selectColumns(String[] columnNames, String[] condition)
			throws Exception {
		String columnName = condition[0];
		String conditionOperand = condition[2];
		String operator = condition[1];
		Object[][] result = null;

		if (isValidCondition(columnName, operator, conditionOperand)) {
			String dataType = table.getColumnDataType(columnName);
			Object[] cells = table.getAllCellsIn(columnName);

			if (dataType.equals(Table.DATA_INTEGER)
					|| dataType.equals(Table.DATA_LONG)) {
				Long conOperand = Long.parseLong(conditionOperand);
				int k = 0;
				for (int i = 0; i < cells.length; i++) {
					if (cells[i] != null
							&& applyConditionToInteger(cells[i], conOperand,
									operator)) {
						k++;
					}

				}
				result = new Object[columnNames.length][k];
				k = 0;
				for (int i = 0; i < cells.length; i++) {

					if (cells[i] != null
							&& applyConditionToInteger(cells[i], conOperand,
									operator)) {
						for (int j = 0; j < result.length; j++) {
							result[j][k] = table.getValue(columnNames[j], i);
						}
						k++;

					}
				}

			} else if (dataType.equals(Table.DATA_FLOAT)
					|| dataType.equals(Table.DATA_DOUBLE)) {
				Double conOperand = Double.parseDouble(conditionOperand);
				int k = 0;

				for (int i = 0; i < cells.length; i++) {
					if (cells[i] != null
							&& applyConditionToDouble(cells[i], conOperand,
									operator)) {
						k++;
					}
				}
				result = new Object[columnNames.length][k];
				k = 0;

				for (int i = 0; i < cells.length; i++) {
					if (cells[i] != null
							&& applyConditionToDouble(cells[i], conOperand,
									operator)) {
						for (int j = 0; j < columnNames.length; j++) {
							result[j][k] = table.getValue(columnNames[j], i);
						}
						k++;

					}
				}

			} else if (dataType.equals(Table.DATA_DATE)
					|| dataType.equals(Table.DATA_STRING)
					|| dataType.equals(Table.DATA_BOOLEAN)) {
				String conOperand = (String) (conditionOperand);
				int k = 0;

				for (int i = 0; i < cells.length; i++) {
					if (cells[i] != null
							&& applyConditionToString(cells[i], conOperand,
									operator)) {
						k++;

					}

				}
				result = new Object[columnNames.length][k];
				k = 0;
				for (int i = 0; i < cells.length; i++) {
					if (cells[i] != null
							&& applyConditionToString(cells[i], conOperand,
									operator)) {
						for (int j = 0; j < columnNames.length; j++) {
							result[j][k] = table.getValue(columnNames[j], i);
						}
						k++;

					}
				}
			}
		}
		return result;
	}

	/**
	 * Method to set columns by names
	 * 
	 * @param columnNames
	 * @return
	 * @throws Exception
	 */
	public Object[][] selectColumns(String[] columnNames) throws Exception {
		Object[] column = table.getAllCellsIn(columnNames[0]);
		Object[][] result = new Object[columnNames.length][column.length];

		for (int i = 0; i < columnNames.length; i++) {
			column = table.getAllCellsIn(columnNames[i]);
			for (int j = 0; j < column.length; j++) {
				result[i][j] = column[j];
			}
		}
		return result;
	}

	public void createColumn(String columnName, String type) throws Exception {
		if (table.getColumns().length == 0) {
			table.createColumn(columnName, type);
		} else {
			table.createColumn(columnName, type);
			int length = table.getColumnsLength();
			for (int i = 0; i < length; i++) {
				table.insertCell(columnName, "null");
			}
		}
		table.applyChanges();
	}

	public void deleteColumn(String columnName) throws Exception {
		table.deleteColumn(columnName);
		table.applyChanges();
	}

	public void createTable(String tableName) throws Exception {
		if (schema.contains(tableName)) {
			throw new Exception("Table Already Exist");
		} else {
			schema.createTable(tableName);
			schema.applyChanges();
			this.table = new Table(schema.getTableURL(tableName));
		}
	}
	
	public void dropTable (String tableName) throws Exception
	{
		schema.dropTable(tableName);
		schema.applyChanges();
	}
	
	public String[] getColumnNames()
	{
		return table.getColumns();
	}
}
