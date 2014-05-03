package DBMS;
public class DataValidator 
{
	/**
	 * Method to check the Integer data
	 * @param value
	 * @return true or false
	 */

	public boolean isInteger(String value) {
		if(value.equals("null"))
		{
			return true ;
		}
		else
		{
		try {
			Integer.parseInt(value);
			return true;
		} catch (Exception e) {
			return false;
		}
		}
	}

	/**
	 * Method to check the Integer data
	 * @param value
	 * @return true or false
	 */

	public boolean isLong(String value) {
		if(value.equals("null"))
		{
			return true ;
		}
		else
		{
		try {
			Long.parseLong(value);
			return true;
		} catch (Exception e) {
			return false;
		}
		}
	}
	/**
	 * Method to check the Double data
	 * @param value
	 * @return true or false
	 */
	
	public boolean isDouble(String value) {
		if(value.equals("null"))
		{
			return true ;
		}else
		{
		try {
			Double.parseDouble(value);
			return true;
		} catch (Exception e) {
			return false;
		}
		}
	}

	/**
	 * Method to check the Float Data
	 * @param value
	 * @return true or false
	 */
	
	public boolean isFloat(String value) {
		if(value.equals("null"))
		{
			return true ;
		}else
		{
		try {
			Float.parseFloat(value);
			return true;
		} catch (Exception e) {
			return false;
		}
		}
	}

	/**
	 * Method to check the String Data
	 * @param value
	 * @return true or false
	 */
	
	public boolean isString(String value) {
		return true;
	}

	
	/**
	 * Method to check the Boolean 
	 * @param value
	 * @return true or false
	 */
	
	public boolean isBoolean(String value) {
		if(value.equals("null"))
		{
			return true ;
		}else
		{
			if(value.equals("true")||value.equals("false"))
			return true;
			else
			return false ;
		}
	}

	
	/**
	 * Method to check the Data 
	 * @param value
	 * @return true or false
	 */
	
	
	public boolean isDate(String value) {
		if(value.equals("null"))
		{
			return true ;
		}else
		{
		String[] date = value.split("/");
		if (date.length != 3)
			return false;
		int day = Integer.parseInt(date[0]);
		int month = Integer.parseInt(date[1]);
		int year = Integer.parseInt(date[2]);

		if (day > 31 || day < 1)
			return false;
		if (month > 12 || month < 1)
			return false;
		if (year < 1900 || year > 2100)
			return false;
		return true;
		}
	}

	/**
	 * Method to validate the data according to the column data type
	 * @param dataType
	 * @param value
	 * @return true or false
	 */
	
	public boolean isValidData(String dataType, String value) {
		if (dataType.equals("Integer"))
			return isInteger(value);
		else if(dataType.equals("Long"))
			return isLong(value);
		else if (dataType.equals("Float"))
			return isFloat(value);
		else if (dataType.equals("Double"))
			return isDouble(value);
		else if (dataType.equals("Boolean"))
			return isBoolean(value);
		else if (dataType.equals("String"))
			return isString(value);
		else if (dataType.equals("Date"))
			return isDate(value);
		return false;

	}
}
