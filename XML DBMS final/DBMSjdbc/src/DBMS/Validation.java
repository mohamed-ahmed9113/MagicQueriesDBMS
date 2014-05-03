package DBMS;

import java.io.Serializable;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@SuppressWarnings("serial")
public class Validation extends Object implements Serializable {
	private int currentSql = 0;
	private String[] input;
	private Schema schema;
	private String[] columnsNames;
	private Object[][] selectedRows;
	private String tabelName;

	public Validation(String[] input1) {
		input = input1;
	}

	public Validation(String[] input1, Schema schema) {
		input = input1;
		this.schema = schema;
	}

	public int getSQL() {
		return currentSql;
	}

	public String getTabelName() {
		return tabelName;
	}

	public String[] getColumnsNames() {
		return columnsNames;
	}

	public Object[][] getSelectedRows() {
		return selectedRows;
	}

	public boolean validate() {
		boolean result = false;
		int inputLength = input.length;
		if (inputLength > 3) {
			currentSql = 0;
			return false;
		} else if (inputLength == 1) {

			Matcher m;
			// DELETE ALL
			Pattern firstLine = Pattern
					.compile("\\p{Blank}*DELETE\\p{Blank}+[^\\p{Blank}]+\\p{Blank}*");
			m = firstLine.matcher(input[0]);
			boolean flag1 = m.matches();
			if (flag1) {
				currentSql = 2;
				return true;
			}
			// ==========================
			// Drop Table
			firstLine = Pattern
					.compile("\\p{Blank}*DROP\\p{Blank}+[^\\p{Blank}]+\\p{Blank}*");
			m = firstLine.matcher(input[0]);
			flag1 = m.matches();
			if (flag1) {
				currentSql = 6;
				return true;
			}

		} else if (inputLength == 2) {
			Matcher m;
			Pattern firstLine;
			Pattern secondLine;
			boolean flag1 = false, flag2 = false;
			// INSERT
			// ========================
			firstLine = Pattern
					.compile("\\p{Blank}*INSERT\\p{Blank}+INTO\\p{Blank}+[^\\p{Blank}]+\\p{Blank}*\\(\\p{Blank}*([^\\p{Blank},]+\\p{Blank}*\\,\\p{Blank}*)*[^\\p{Blank}]+\\p{Blank}*\\)\\p{Blank}*");
			m = firstLine.matcher(input[0]);
			flag1 = m.matches();
			secondLine = Pattern
					.compile("\\p{Blank}*VALUES\\p{Blank}*\\(\\p{Blank}*(((\"[^,]+\")|(true)|(false)|[(\\d).]+)\\p{Blank}*\\,\\p{Blank}*)*((\"[^,]+\")|(true)|(false)|[(\\d).]+)\\p{Blank}*\\)\\p{Blank}*");
			m = secondLine.matcher(input[1]);
			flag2 = m.matches();
			if (flag1 && flag2) {
				currentSql = 1;
				return true;
			}
			// DELETE+
			// ========================
			firstLine = Pattern
					.compile("\\p{Blank}*DELETE\\p{Blank}+[^\\p{Blank}]+\\p{Blank}*");
			m = firstLine.matcher(input[0]);
			flag1 = m.matches();
			secondLine = Pattern
					.compile("\\p{Blank}*WHERE\\p{Blank}+[^\\p{Blank}]+\\p{Blank}*(=|>|<|(<>)|(>=)|(<=))\\p{Blank}*((\"[^,]+\")|(true)|(false)|[(\\d).]+)\\p{Blank}*");
			m = secondLine.matcher(input[1]);
			flag2 = m.matches();
			if (flag1 && flag2) {
				currentSql = 2;
				return true;
			}
			// UPDATE ALL
			// ========================
			firstLine = Pattern
					.compile("\\p{Blank}*UPDATE\\p{Blank}+[^\\p{Blank}]+\\p{Blank}*");
			m = firstLine.matcher(input[0]);
			flag1 = m.matches();
			secondLine = Pattern
					.compile("\\p{Blank}*SET\\p{Blank}+([^\\p{Blank},]+\\p{Blank}*(=)\\p{Blank}*((\"[^,]+\")|(true)|(false)|[(\\d).]+)\\p{Blank}*,\\p{Blank}*)*\\p{Blank}*[^\\p{Blank},]+\\p{Blank}*(=)\\p{Blank}*((\"[^,]+\")|(true)|(false)|[(\\d).]+)\\p{Blank}*");
			m = secondLine.matcher(input[1]);
			flag2 = m.matches();
			if (flag1 && flag2) {
				currentSql = 3;
				return true;
			}
			// SELECT ALL without condition
			// ========================
			firstLine = Pattern
					.compile("\\p{Blank}*SELECT\\p{Blank}+\\*\\p{Blank}*");
			m = firstLine.matcher(input[0]);
			flag1 = m.matches();
			secondLine = Pattern
					.compile("\\p{Blank}*FROM\\p{Blank}+[^\\p{Blank}]+\\p{Blank}*");
			m = secondLine.matcher(input[1]);
			flag2 = m.matches();
			if (flag1 && flag2) {
				currentSql = 4;
				return true;
			}
			// SELECT without condition
			// ========================
			firstLine = Pattern
					.compile("\\p{Blank}*SELECT\\p{Blank}*\\(\\p{Blank}*([^\\p{Blank},]+\\p{Blank}*\\,\\p{Blank}*)*[^\\p{Blank}]+\\p{Blank}*\\)\\p{Blank}*");
			m = firstLine.matcher(input[0]);
			flag1 = m.matches();
			secondLine = Pattern
					.compile("\\p{Blank}*FROM\\p{Blank}+[^\\p{Blank}]+\\p{Blank}*");
			m = secondLine.matcher(input[1]);
			flag2 = m.matches();
			if (flag1 && flag2) {
				currentSql = 4;
				return true;
			}
			// Create Table
			// ========================
			firstLine = Pattern
					.compile("\\p{Blank}*CREATE\\p{Blank}+TABLE\\p{Blank}+[^\\p{Blank}]+\\p{Blank}*");
			m = firstLine.matcher(input[0]);
			flag1 = m.matches();
			secondLine = Pattern
					.compile("\\p{Blank}*\\(\\p{Blank}*([^\\p{Blank},]+\\p{Blank}+((Integer)|(Float)|(String)|(Long)|(Boolean)|(Date)|(Double))\\p{Blank}*\\,\\p{Blank}*)*([^\\p{Blank},]+\\p{Blank}+((Integer)|(Float)|(String)|(Long)|(Boolean)|(Date)|(Double))\\p{Blank}*)\\)\\p{Blank}*");
			m = secondLine.matcher(input[1]);
			flag2 = m.matches();
			if (flag1 && flag2) {
				currentSql = 5;
				return true;
			}
			// ========================
		} else if (inputLength == 3) {
			Matcher m;
			Pattern firstLine;
			Pattern secondLine;
			Pattern thirdLine;
			boolean flag1 = false, flag2 = false, flag3 = false;
			// UPDATE with condition
			// ========================
			firstLine = Pattern
					.compile("\\p{Blank}*UPDATE\\p{Blank}+[^\\p{Blank}]+\\p{Blank}*");
			m = firstLine.matcher(input[0]);
			flag1 = m.matches();
			secondLine = Pattern
					.compile("\\p{Blank}*SET\\p{Blank}+([^\\p{Blank},]+\\p{Blank}*(=)\\p{Blank}*((\"[^,]+\")|(true)|(false)|[(\\d).]+)\\p{Blank}*,)*\\p{Blank}*[^\\p{Blank},]+\\p{Blank}*(=)\\p{Blank}*((\"[^,]+\")|(true)|(false)|[(\\d).]+)\\p{Blank}*");
			m = secondLine.matcher(input[1]);
			flag2 = m.matches();
			thirdLine = Pattern
					.compile("\\p{Blank}*WHERE\\p{Blank}+[^\\p{Blank}]+\\p{Blank}*(=|>|<|(<>)|(>=)|(<=))\\p{Blank}*((\"[^,]+\")|(true)|(false)|[(\\d).]+)\\p{Blank}*");
			m = thirdLine.matcher(input[2]);
			flag3 = m.matches();
			if (flag1 && flag2 && flag3) {
				currentSql = 3;
				return true;
			}
			// SELECT ALL with condition
			// ========================
			firstLine = Pattern
					.compile("\\p{Blank}*SELECT\\p{Blank}+\\*\\p{Blank}*");
			m = firstLine.matcher(input[0]);
			flag1 = m.matches();
			secondLine = Pattern
					.compile("\\p{Blank}*FROM\\p{Blank}+[^\\p{Blank}]+\\p{Blank}*");
			m = secondLine.matcher(input[1]);
			flag2 = m.matches();
			thirdLine = Pattern
					.compile("\\p{Blank}*WHERE\\p{Blank}+[^\\p{Blank}]+\\p{Blank}*(=|>|<|(<>)|(>=)|(<=))\\p{Blank}*((\"[^,]+\")|(true)|(false)|[(\\d).]+)\\p{Blank}*");
			m = thirdLine.matcher(input[2]);
			flag3 = m.matches();
			if (flag1 && flag2 && flag3) {
				currentSql = 4;
				return true;
			}
			// SELECT with condition
			// ========================
			firstLine = Pattern
					.compile("\\p{Blank}*SELECT\\p{Blank}*\\(\\p{Blank}*([^\\p{Blank},]+\\p{Blank}*\\,\\p{Blank}*)*[^\\p{Blank}]+\\p{Blank}*\\)\\p{Blank}*");
			m = firstLine.matcher(input[0]);
			flag1 = m.matches();
			secondLine = Pattern
					.compile("\\p{Blank}*FROM\\p{Blank}+[^\\p{Blank}]+\\p{Blank}*");
			m = secondLine.matcher(input[1]);
			flag2 = m.matches();
			thirdLine = Pattern
					.compile("\\p{Blank}*WHERE\\p{Blank}+[^\\p{Blank}]+\\p{Blank}*(=|>|<|(<>)|(>=)|(<=))\\p{Blank}*((\"[^,]+\")|(true)|(false)|[(\\d).]+)\\p{Blank}*");
			m = thirdLine.matcher(input[2]);
			flag3 = m.matches();
			if (flag1 && flag2 && flag3) {
				currentSql = 4;
				return true;
			}
		}
		return result;
	}

	public boolean finalParsing() throws Exception {
		if (validate()) {
			queryParser parse = new queryParser(input, getSQL(), schema);
			tabelName = parse.getTabelName();
			if (getSQL() == 4) {
				columnsNames = parse.getColumns();
				selectedRows = parse.getTabel();
			}
			return true;
		} else {
			return false;
			
		}
	}

}
