package DBMS;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Table {

	private String filePath;
	private Node table;
	private Document tableDocument;
	public final static String DATA_INTEGER = "Integer";
	public final static String DATA_LONG = "Long";
	public final static String DATA_FLOAT = "Float";
	public final static String DATA_DOUBLE = "Double";
	public final static String DATA_STRING = "String";
	public final static String DATA_DATE = "Date";
	public final static String DATA_BOOLEAN = "Boolean";

	/**
	 * The Table Class constructor
	 * 
	 * @param directory
	 * @param tableName
	 */

	public Table(String filePath) {
		
		this.filePath = filePath ;

		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			tableDocument = docBuilder.parse(filePath);

			// Get the root element

			table = tableDocument.getFirstChild();
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (SAXException sae) {
			sae.printStackTrace();
		}

	}
	
	
	/**
	 * Method to get all columns names in the table
	 * @return String[] columns names
	 */
	
	public String[] getColumns()
	{
		NodeList columns = tableDocument.getElementsByTagName("column");
        String[] columnNames = new String[columns.getLength()];
		
        for (int i = 0; i < columns.getLength(); i++) 
		{
			Node node = columns.item(i);
			NamedNodeMap attr = node.getAttributes();
			Node nodeAttr = attr.getNamedItem("name");
			columnNames[i]=nodeAttr.getTextContent();
		}
        
        return columnNames;
	}

	/**
	 * Method to get columns Length
	 * @return
	 * @throws Exception
	 */
	public int getColumnsLength() throws Exception
	{
		return getAllCellsIn(getColumns()[0]).length;
	}
	/**
	 * Method to get Column Data type
	 * 
	 * @param columnName
	 * @return
	 * @throws Exception
	 */

	public String getColumnDataType(String columnName) throws Exception {
		NodeList columns = tableDocument.getElementsByTagName("column");
		String dataType = "";
		boolean isFound = false;

		for (int i = 0; i < columns.getLength(); i++) {
			Node node = columns.item(i);
			NamedNodeMap attr = node.getAttributes();
			Node nodeAttr = attr.getNamedItem("name");

			if (nodeAttr.getTextContent().equals(columnName)) {
				Node dataAttr = attr.getNamedItem("dataType");
				dataType = dataAttr.getTextContent();
				isFound = true;
				break;
			}
		}

		if (!isFound) {
			throw new Exception("Column Not Found");
		}

		return dataType;
	}

	/**
	 * Method to apply changes made to the actual XML file of the table
	 */

	public void applyChanges() {
		// write the content into xml file

		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(tableDocument);
			StreamResult result = new StreamResult(new File(filePath));
			transformer.transform(source, result);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Method to create and append a new column to the table
	 * 
	 * @param columnName
	 * @param type
	 * @throws Exception
	 */

	public void createColumn(String columnName, String type) throws Exception {

		// append a new node -column- to table
		if (!contains(columnName)) {
			Element newColumn = tableDocument.createElement("column");
			newColumn.setAttribute("dataType", type);
			newColumn.setAttribute("name", columnName);
			table.appendChild(newColumn);
		} else {
			throw new Exception("Column already exist");
		}
	}

	/**
	 * Method to delete a column from the table
	 * 
	 * @param columnName
	 * @throws Exception
	 */

	public void deleteColumn(String columnName) throws Exception {
		// TODO Auto-generated method stub

		NodeList columns = tableDocument.getElementsByTagName("column");

		boolean isFound = false;

		for (int i = 0; i < columns.getLength(); i++) {
			Node node = columns.item(i);
			NamedNodeMap attr = node.getAttributes();
			Node nodeAttr = attr.getNamedItem("name");

			if (nodeAttr.getTextContent().equals(columnName)) {
				table.removeChild(node);
				isFound = true;
				break;
			}
		}

		if (!isFound) {
			throw new Exception("Column Not Found");
		}

	}

	/**
	 * Method to determine whether the table contain a certain column or not
	 * 
	 * @param columnName
	 * @return true or false
	 */

	public boolean contains(String columnName) {
		NodeList columns = tableDocument.getElementsByTagName("column");

		boolean isFound = false;

		for (int i = 0; i < columns.getLength(); i++) {
			Node node = columns.item(i);
			NamedNodeMap attr = node.getAttributes();
			Node nodeAttr = attr.getNamedItem("name");

			if (nodeAttr.getTextContent().equals(columnName)) {
				isFound = true;
				break;
			}
		}
		return isFound;
	}

	/**
	 * insert a value in a specified column
	 * 
	 * @param columnName
	 * @param value
	 * @throws Exception
	 */

	public void insertCell(String columnName, String value) throws Exception {

		DataValidator validator = new DataValidator();
		String dataType = getColumnDataType(columnName);
		boolean isValid = validator.isValidData(dataType, value);

		if (isValid) {
			boolean isFound = false;

			NodeList columns = tableDocument.getElementsByTagName("column");

			for (int i = 0; i < columns.getLength(); i++) {
				Node node = columns.item(i);
				NamedNodeMap attr = node.getAttributes();
				Node nodeAttr = attr.getNamedItem("name");

				if (nodeAttr.getTextContent().equals(columnName)) {
					Element newElement = tableDocument.createElement(columnName
							+ "Element");
					newElement.setTextContent(value);
					node.appendChild(newElement);
					isFound = true;
					break;
				}
			}

			if (!isFound) {
				throw new Exception("Column not Found");
			}
		} else {
			throw new Exception("Wrong Data Type");
		}
	}

	/**
	 * delete a value from a specified column
	 * 
	 * @param columnName
	 * @param value
	 * @throws Exception
	 */

	public void deleteCell(String columnName, String value) throws Exception {

		boolean isFound = false;

		NodeList columns = tableDocument.getElementsByTagName("column");

		for (int i = 0; i < columns.getLength(); i++) {
			Node node = columns.item(i);
			NamedNodeMap attr = node.getAttributes();
			Node nodeAttr = attr.getNamedItem("name");

			if (nodeAttr.getTextContent().equals(columnName)) {
				NodeList values = tableDocument.getElementsByTagName(columnName
						+ "Element");

				boolean isValueFound = false;

				for (int j = 0; j < values.getLength(); j++) {
					Node valueNode = values.item(j);

					if (valueNode.getTextContent().equals(value)) {
						node.removeChild(valueNode);
						isValueFound = true;
						break;
					}
				}

				if (!isValueFound) {
					throw new Exception("Value Not Found");
				}

				isFound = true;
				break;
			}
		}

		if (!isFound) {
			throw new Exception("Column not Found");
		}
	}

	/**
	 * Method to set the value of a specified cell in the column
	 * 
	 * @param columnName
	 * @param index
	 * @throws Exception
	 */

	public void deleteCell(String columnName, int index) throws Exception {
		boolean isFound = false;

		NodeList columns = tableDocument.getElementsByTagName("column");

		for (int i = 0; i < columns.getLength(); i++) {
			Node node = columns.item(i);
			NamedNodeMap attr = node.getAttributes();
			Node nodeAttr = attr.getNamedItem("name");

			if (nodeAttr.getTextContent().equals(columnName)) {
				NodeList values = tableDocument.getElementsByTagName(columnName
						+ "Element");

				try {
					Node valueNode = values.item(index);
					node.removeChild(valueNode);
				} catch (Exception e) {
					throw new Exception("Invalid index");
				}
				isFound = true;
				break;
			}
		}

		if (!isFound) {
			throw new Exception("Column not Found");
		}
	}

	/**
	 * Method to set the value of a certain cell in the column by it's index
	 * 
	 * @param columnName
	 * @param index
	 * @param value
	 * @throws Exception
	 */

	public void setCell(String columnName, int index, String value)
			throws Exception {

		DataValidator validator = new DataValidator();
		String dataType = getColumnDataType(columnName);
		boolean isValid = validator.isValidData(dataType, value);

		if (isValid) {
			boolean isFound = false;

			NodeList columns = tableDocument.getElementsByTagName("column");

			for (int i = 0; i < columns.getLength(); i++) {
				Node node = columns.item(i);
				NamedNodeMap attr = node.getAttributes();
				Node nodeAttr = attr.getNamedItem("name");

				if (nodeAttr.getTextContent().equals(columnName)) {
					NodeList values = tableDocument
							.getElementsByTagName(columnName + "Element");

					try {
						Node valueNode = values.item(index);
						valueNode.setTextContent(value);
					} catch (Exception e) {
						throw new Exception("Invalid index");
					}
					isFound = true;
					break;
				}
			}

			if (!isFound) {
				throw new Exception("Column not Found");
			}
		} else {
			throw new Exception("Invalid Data Type");
		}
	}

	/**
	 * Method to set the value of a certain cell in the column
	 * 
	 * @param columnName
	 * @param oldValue
	 * @param newValue
	 * @throws Exception
	 */

	public void setCell(String columnName, String oldValue, String newValue)
			throws Exception {

		DataValidator validator = new DataValidator();
		String dataType = getColumnDataType(columnName);
		boolean isValid = validator.isValidData(dataType, oldValue);

		if (isValid) {

			boolean isFound = false;

			NodeList columns = tableDocument.getElementsByTagName("column");

			for (int i = 0; i < columns.getLength(); i++) {
				Node node = columns.item(i);
				NamedNodeMap attr = node.getAttributes();
				Node nodeAttr = attr.getNamedItem("name");

				if (nodeAttr.getTextContent().equals(columnName)) {
					NodeList values = tableDocument
							.getElementsByTagName(columnName + "Element");

					boolean isValueFound = false;

					for (int j = 0; j < values.getLength(); j++) {
						Node valueNode = values.item(j);

						if (valueNode.getTextContent().equals(oldValue)) {
							valueNode.setTextContent(newValue);
							isValueFound = true;
							break;
						}
					}

					if (!isValueFound) {
						throw new Exception("Value Not Found");
					}

					isFound = true;
					break;
				}
			}

			if (!isFound) {
				throw new Exception("Column not Found");
			}
		} else {
			throw new Exception("Invalid Data Type");
		}
	}

	/**
	 * Method to set all cells in the column to a certain value
	 * 
	 * @param columnName
	 * @param value
	 * @throws Exception
	 */

	public void setAllCellsTo(String columnName, String value) throws Exception {

		DataValidator validator = new DataValidator();
		String dataType = getColumnDataType(columnName);
		boolean isValid = validator.isValidData(dataType, value);

		if (isValid) {

			boolean isFound = false;

			NodeList columns = tableDocument.getElementsByTagName("column");

			for (int i = 0; i < columns.getLength(); i++) {
				Node node = columns.item(i);
				NamedNodeMap attr = node.getAttributes();
				Node nodeAttr = attr.getNamedItem("name");

				if (nodeAttr.getTextContent().equals(columnName)) {
					NodeList values = tableDocument
							.getElementsByTagName(columnName + "Element");

					for (int j = 0; j < values.getLength(); j++) {
						Node valueNode = values.item(j);
						valueNode.setTextContent(value);
					}

					isFound = true;
					break;
				}
			}

			if (!isFound) {
				throw new Exception("Column not Found");
			}
		} else {
			throw new Exception("Invalid Data Type");
		}
	}

	/**
	 * Method to get all cells in the column
	 * 
	 * @param columnName
	 * @param value
	 * @throws Exception
	 * @return array of Objects holding the values stored in the column 
	 */

	public Object[] getAllCellsIn(String columnName) throws Exception {

		boolean isFound = false;

		Object[] valuesObjects = null;

		NodeList columns = tableDocument.getElementsByTagName("column");

		for (int i = 0; i < columns.getLength(); i++) {
			Node node = columns.item(i);
			NamedNodeMap attr = node.getAttributes();
			Node nodeAttr = attr.getNamedItem("name");

			if (nodeAttr.getTextContent().equals(columnName)) {
				NodeList values = tableDocument.getElementsByTagName(columnName
						+ "Element");

				valuesObjects = new Object[values.getLength()];

				for (int j = 0; j < values.getLength(); j++) {
					Node valueNode = values.item(j);
					Object currentObject=null;
					String dataType = getColumnDataType(columnName);
					String value = valueNode.getTextContent();

					if(!value.equals("null"))
					{
					if (dataType.equals("Integer")) {
						currentObject = Integer.parseInt(value);
					}else if(dataType.equals("Long")) {
						currentObject = Long.parseLong(value);
					}
					else if (dataType.equals("Float")) {
						currentObject = Float.parseFloat(value);
					} else if (dataType.equals("Double")) {
						currentObject = Double.parseDouble(value);
					} else if (dataType.equals("Boolean")) {
						currentObject = Boolean.parseBoolean(value);
					} else if (dataType.equals("String")) {
						currentObject = value;
					} else if (dataType.equals("Date")) {
						currentObject = value;
					} else {
						currentObject = null;
					}
					}

					valuesObjects[j] = currentObject;
				}

				isFound = true;
				break;
			}
		}

		if (!isFound) {
			throw new Exception("Column not Found");
		}

		return valuesObjects;
	}

	/**
	 * Method to delete all values in a certain column
	 * 
	 * @param columnName
	 * @throws Exception
	 */

	public void deleteAllCells(String columnName) throws Exception {

		boolean isFound = false;

		NodeList columns = tableDocument.getElementsByTagName("column");

		for (int i = 0; i < columns.getLength(); i++) {
			Node node = columns.item(i);
			NamedNodeMap attr = node.getAttributes();
			Node nodeAttr = attr.getNamedItem("name");

			if (nodeAttr.getTextContent().equals(columnName)) {
				NodeList values = tableDocument.getElementsByTagName(columnName
						+ "Element");

				int length = values.getLength();
				for (int j = 0; j < length; j++) {
					Node valueNode = values.item(0);
					node.removeChild(valueNode);
				}

				isFound = true;
				break;
			}
		}

		if (!isFound) {
			throw new Exception("Column not Found");
		}

	}

	/**
	 * Method to get a certain value from a column
	 * 
	 * @param columnName
	 * @param index
	 * @return value
	 * @throws Exception
	 */

	public Object getValue(String columnName, int index) throws Exception {
		boolean isFound = false;

		NodeList columns = tableDocument.getElementsByTagName("column");
		String value = "";
		for (int i = 0; i < columns.getLength(); i++) {
			Node node = columns.item(i);
			NamedNodeMap attr = node.getAttributes();
			Node nodeAttr = attr.getNamedItem("name");

			if (nodeAttr.getTextContent().equals(columnName)) {
				NodeList values = tableDocument.getElementsByTagName(columnName
						+ "Element");

				try {
					Node valueNode = values.item(index);
					value = valueNode.getTextContent();
				} catch (Exception e) {
					throw new Exception("Invalid index");
				}
				isFound = true;
				break;
			}
		}

		if (!isFound) {
			throw new Exception("Column not Found");
		}

		String dataType = getColumnDataType(columnName);

		if(!value.equals("null"))
		{
		if (dataType.equals(DATA_INTEGER)) {
			return (Object) Integer.parseInt(value);
		} else if (dataType.equals(DATA_FLOAT)) {
			return (Object) Float.parseFloat(value);
		} else if (dataType.equals(DATA_DOUBLE)) {
			return (Object) Double.parseDouble(value);
		} else if (dataType.equals(DATA_BOOLEAN)) {
			return (Object) Boolean.parseBoolean(value);
		} else if (dataType.equals(DATA_STRING)) {
			return (Object) value;
		} else if (dataType.equals(DATA_DATE)) {
			return (Object) value;
		}else if (dataType.equals(DATA_LONG))
			return (Object) value ;
		else {
			return null;
		}
		}else
			return null ;
	}

	public static void main(String[] args) throws Exception

	{

		Table table1 = new Table("D:\\College Work\\3rd Semester\\OOP\\Assignments\\Assignment3\\XML DBMS\\table1.xml");
		String[]names = table1.getColumns();
		for(int i = 0 ; i<names.length ; i++)
		{
			System.out.println(names[i]);
		}
		table1.applyChanges();
	}

}
