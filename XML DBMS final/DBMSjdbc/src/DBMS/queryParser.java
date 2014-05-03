package DBMS;

public class queryParser {
	private String tableName;
	private String[] columns;
	private String[] values;
	private String[] input;
	private String[] condition;
	private String[] dataTypes;
	private Object[][]table;
	private Controller controller;
	@SuppressWarnings("unused")
	private Schema z;
	
//	public queryParser(String [] s,int order) throws Exception
//	{
//		input = s;
//		for (int i = 0; i < input.length; i++) 
//		{
//			input[i] = checkSpaces(input[i]);
//		}
//		switch (order) {
//		case 1:
//			insert();
//			break;
//		case 2:
//			delete();
//			break;
//		case 3:
//			update();
//			break;
//		case 4:
//			select();
//			break;
//		case 5:
//			create();
//			break;
//		case 6:
//			create();
//			break;
//		default:
//			break;
//		}
//	}
	public queryParser(String[] s,int order , Schema schema) throws Exception 
	{
		z=schema;
		controller = new Controller(schema);
		input = s;
		for (int i = 0; i < input.length; i++) 
		{
			input[i] = checkSpaces(input[i]);
		}
		switch (order) {
		case 1:
			insert();
			break;
		case 2:
			delete();
			break;
		case 3:
			update();
			break;
		case 4:
			select();
			break;
		case 5:
			create();
			break;
		case 6:
			drop();
			break;
		default:
			break;
		}
	}
	
	public String[] getColumns() 
	{
		return columns;
	}
	
	public String getTabelName()
	{
		return tableName;
	}
	
	public Object [][] getTabel()
	{
		return table;
	}

	public void insert() throws Exception 															// Insert a row below table		
	{
		int j = 12;
		while (input[0].charAt(j) != ' ' && input[0].charAt(j) != '(') 
		{
			j++;
		}
		tableName = input[0].substring(12, j);
		controller.setTabelName(tableName);
		columns = getArray(input[0].substring(j));
		values = getArray(input[1].substring(6));
		for (int i=0;i<values.length;i++)
		{
			values[i]=valueString(values[i]);
		}
		if (columns.length!=values.length)
		{
			throw new Exception();
			
		}
		controller.insert(columns,values);
	}

	public void delete() throws Exception 									// Delete a value
	{															
		tableName = input[0].substring(7);
		controller.setTabelName(tableName);
		if (input.length == 1) 
		{
			controller.deleteAll(); 													//to delete the hole table
		} else 
		{
			condition = getCondition(input[1]);
			controller.delete(condition); 											//to delete a specific row
		}
	}

	public void update() throws Exception 															// update a value
	{
		tableName= input[0].substring(7);													// Get the name of the table
		controller.setTabelName(tableName);
		String [] temp=getArray(input[1].substring(3));
		columns=new String [temp.length];
		values=new String [temp.length];
		int j=0;
		for (int i=0;i<temp.length;i++)
		{
			j=0;
			while(temp[i].charAt(j)!='=')
			{
				j++;
			}
			columns[i]=temp[i].substring(0, j);												// Get the name of the columns
			values[i]=valueString(temp[i].substring(j+1));												// Get the new values to be updated
		}
		if (columns.length!=values.length)
		{
			throw new Exception();
		}
		if (input.length==2)
		{
			controller.updateAll(columns,values);									// update all the rows with new values 

		}else
		{
			condition=getCondition(input[2]);												// update specific rows with the new values
			controller.update(columns,values,condition);
		}
	}
	
	public void select() throws Exception															// select table or a specific row 
	{
		tableName=input[1].substring(5);
		controller.setTabelName(tableName);
		if (input[0].length()==8 && input.length==2)										// to select all the table
		{
			columns=controller.getColumnNames();
			table=controller.selectAll();
		}else if (input[0].length()==8 && input.length==3)									// to select some rows
		{
			columns=controller.getColumnNames();
			condition=getCondition(input[2]);
			table=controller.selectAll(condition);
		}else if (input .length==2)															// to select all the column chosen before
		{
			columns=getArray(input[0].substring(6));
			table=controller.selectColumns(columns);
		}else																				// to select some cells 
		{
			columns=getArray(input[0].substring(6));										
			condition=getCondition(input[2]);
			table=controller.selectColumns(columns,condition);
		}	
		
	}
	
	public void create() throws Exception									// Create new table
	{
		tableName=input[0].substring(13);
		controller.createTable(tableName);
		String [] temp=(input[1].substring(1, input[1].length())).split(",");
		for (int i=0;i<temp.length;i++)
		{
			temp[i]= checkSpaces(temp[i]);
		}
		columns=new String[temp.length];
		dataTypes=new String [temp.length];
		int j;
		for (int i=0;i<temp.length;i++)
		{
			j=0;
			while (temp[i].charAt(j)!=' ')
			{
				j++;
			}
			columns[i]=temp[i].substring(0, j);
			if (temp[i].charAt((temp[i].length())-1)==')' && temp[i].charAt((temp[i].length())-2)==' ')
			{
				dataTypes[i]=temp[i].substring(j+1,temp[i].length()-2);				
			}else if (temp[i].charAt((temp[i].length())-1)==')')
			{
				dataTypes[i]=temp[i].substring(j+1,temp[i].length()-1);				
			}else 
			{
				dataTypes[i]=temp[i].substring(j+1);				
			}
			controller.createColumn(columns[i], dataTypes[i]);
		}
	}
	
	public void drop() throws Exception														//  Drop exist table
	{
		tableName=input[0].substring(5);
		controller.dropTable(tableName);
	}
	
	public String[] getCondition(String s) 								// to parse the where condition
	{
		char c, c1;
		String[] cond = new String[3];
		String temp = "";
		s = s.substring(6);
		int j = 0;
		boolean found = false;
		for (int i = 0; i < s.length(); i++) 							// deleting the spaces in the string
		{
			if (s.charAt(i)=='"')
			{
				temp +=s.charAt(i);
				i++;
				while (s.charAt(i)!='"')
				{
					temp +=s.charAt(i);
					i++;
				}
				temp +=s.charAt(i);
			}else if (s.charAt(i) != ' ') 
			{
				temp += s.charAt(i);
			}
		}
		while (j < temp.length() && !found) 							// filling the condition array
		{
			c = temp.charAt(j);
			c1 = temp.charAt(j + 1);
			if ((c == '<' && c1 == '=') || (c == '>' && c1 == '=') || (c == '<' && c1 == '>')) 
			{
				cond[0] = temp.substring(0, j);
				cond[1] = temp.substring(j, j + 2);
				cond[2] = valueString(temp.substring(j + 2));
				found = true;
			} else if (c == '=' || c == '<' || c == '>') 
			{
				cond[0] = temp.substring(0, j);
				cond[1] = temp.substring(j, j + 1);
				cond[2] = valueString(temp.substring(j + 1));
				found = true;
			}
			j++;
		}
		return cond;
	}
	
	public String valueString(String s)
	{
		int i=0;
		boolean found =false;
		while(i<s.length() && !found)
		{
			if (s.charAt(i)=='"')
			{
				found =true;
			}
			i++;
		}
		if (found)
		{
			int j=i;
			while (s.charAt(j)!='"')
			{
				j++;
			}
			return s.substring(i, j);
		}else
		{
			return s;
		}
	}

	public String[] getArray(String s) 														// return array of Strings which is splited on ","
	{
		String temp = "";
		for (int i = 0; i < s.length(); i++) 
		{
			if (s.charAt(i)=='"')
			{
				i++;
				while (s.charAt(i)!='"')
				{
					temp +=s.charAt(i);
					i++;
				}
			}else if (s.charAt(i) != '(' && s.charAt(i) != ')' && s.charAt(i) != ' ') 			// Remove all the spaces and brackets 
			{
				temp = temp + s.charAt(i);
			}
		}
		return temp.split(",");
	}

	public String checkSpaces(String s) 									// to neglect all the unwanted spaces
	{
		boolean check = false;
		int length = s.length();
		String temp = "";
		int j = 0;
		while (j < length && s.charAt(j) == ' ') 
		{
			j++;
		}
		for (int i = j; i < s.length(); i++) 
		{
			if (s.charAt(i)=='"')
			{
				temp +=s.charAt(i);
				i++;
				while (i<s.length() && s.charAt(i)!='"')
				{
					temp +=s.charAt(i);
					i++;
				}
				temp +=s.charAt(i);
			}else
			{
			while (i < length && s.charAt(i) == ' ') 
			{
				check = true;
				i++;
			}
			if (check && i < length && s.charAt(i)!='"') 
			{
				temp += " " + s.charAt(i);
				check = false;
			} else if (i<length && s.charAt(i)=='"') {
				i--;
			}else if (i<s.length())
			{
				temp += s.charAt(i);				
			}
			}
		}
		return temp;
	}
//	public static void main (String [] args) throws Exception
//	{
//		String [] s={"CREATE TABLE id " , "(UHKG Boolean, kdhchbn Integer   ) "};
//		queryParser q=new queryParser(s, 5);
//	}
//	

}
