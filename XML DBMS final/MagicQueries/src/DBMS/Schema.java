package DBMS;
import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Schema implements SchemaInterface {

	private String name;
	private ArrayList<String> tableNames;
	private String URL;
	private Node table;
	private Document tableDocument;

	/**
	 * The Constructor of the schema that takes two parameters.
	 * 
	 * @param schemaName
	 *            : the name of the schema.
	 * @param url
	 *            : the URL directory of the schema and it's tables.
	 * @throws Exception
	 */

	public Schema(String url, String schemaName, boolean isExist)
			throws Exception {
		try {
			name = schemaName;
			tableNames = new ArrayList<String>();
			URL = url;
			// creating the schema file in the URL directory.
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			tableDocument = docBuilder.parse(url + "\\" + schemaName + ".xml");
			table = tableDocument.getFirstChild();
			if (!table.getNodeName().equals("Schema")) {
				throw new Exception();
			}
			NodeList tables = (tableDocument.getElementsByTagName("Table"));

			for (int i = 0; i < tables.getLength(); i++) {
				tableNames.add(tables.item(i).getTextContent());
			}

			MagicQueriesLogger.logger.info("Connection set , schema loaded successfully ,");
		} catch (Exception e) {
			MagicQueriesLogger.logger.error("Connection failed , invalid schema file ,");
			throw new Exception("Invalid Schema File");
		}
	}

	public Schema(String url, String schemaName) throws Exception {

		name = schemaName;
		URL = url;
		tableNames = new ArrayList<String>();

		try {

			// creating the schema file in the URL directory.
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			tableDocument = docBuilder.newDocument();
			tableDocument = docBuilder.parse(url + "\\" + schemaName + ".xml");
			table = tableDocument.getFirstChild();
			if (!table.getNodeName().equals("Schema")) {
				throw new Exception();
			}
			throw new Exception("Schema Exists");
		} catch (Exception e) {
			if (!e.getMessage().equals("Schema Exists")) {
				Element rootElement = tableDocument.createElement("Schema");
				tableDocument.appendChild(rootElement);
				table = tableDocument.getFirstChild();
				MagicQueriesLogger.logger.info("Connection set , schema created successfully ,");
			} else {
				MagicQueriesLogger.logger.error("Connection failed");
				MagicQueriesLogger.logger.error("failed to create schema ,schema file name already exist,");
				throw new Exception("Schema Already Exisits");
			}
		}

	}

	/**
	 * @return this method returns the name if the schema.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            of the new schema.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return array List that contains the names of all the table in the
	 *         schema.
	 */
	public ArrayList<String> getTableNames() {
		return tableNames;
	}

	/**
	 * @param arrayList
	 *            contains the table's names.
	 */
	public void setTableNames(ArrayList<String> tableNames) {
		this.tableNames = tableNames;
	}

	/**
	 * @return get the URL of the schema directory file.
	 */
	public String getURL() {
		return URL;
	}

	/**
	 * @param uRL
	 *            to set the URL of the schema.
	 */
	public void setURL(String uRL) {
		URL = uRL + "/" + name + ".xml";
	}

	/**
	 * this method is used for creating new table in the schema.
	 * 
	 * @param tableName
	 *            : is the name of the new table.
	 * @throws Exception
	 */
	public void createTable(String tableName) throws Exception {

		if (!contains(tableName)) {
			try {
				// add the name of the table in the schema.
				tableNames.add(tableName);

				// add the tag of this table in the schema XML file.
				Node schema = table;
				Element newTable = tableDocument.createElement("Table");
				newTable.appendChild(tableDocument.createTextNode(tableName));
				schema.appendChild(newTable);
				table = tableDocument.getFirstChild();

				// create a new xml file for the new table.
				try {
					DocumentBuilderFactory docFactory = DocumentBuilderFactory
							.newInstance();
					DocumentBuilder builder = docFactory.newDocumentBuilder();
					Document doc = builder.newDocument();
					Element rootElement = doc.createElement("table");
					rootElement.setAttribute("Name", tableName);
					doc.appendChild(rootElement);
					DOMSource source = new DOMSource(doc);
					StreamResult result = new StreamResult(new File(
							getTableURL(tableName)));
					TransformerFactory transformerFactory = TransformerFactory
							.newInstance();
					Transformer transformer = transformerFactory
							.newTransformer();
					transformer.transform(source, result);
				} catch (Exception e) {
					MagicQueriesLogger.logger.error("Error in creating table");
					System.out.println("error");
				}
			} catch (Exception e) {
				MagicQueriesLogger.logger.error("Error in creating table");
				System.out
						.println("Error occured in adding a new table to the schema");
			}
		} else {
			MagicQueriesLogger.logger.error("Table Already Exists");
			throw new Exception("Table already exists");
		}
		MagicQueriesLogger.logger.info("Table Created Successfully tableName : "+ tableName );
	}

	@Override
	public void dropTable(String tableName) throws Exception {
		if (contains(tableName)) {
			try {
				Node schema = table;
				NodeList list = schema.getChildNodes();
				for (int i = 0; i < list.getLength(); i++) {
					Node node = list.item(i);
					if (tableName.equalsIgnoreCase(node.getTextContent())) {
						schema.removeChild(node);
					}
				}
				table = tableDocument.getFirstChild();

				// delete the XML file of the deleted table.
				File file = new File(getTableURL(tableName));
				file.delete();
				tableNames.remove((String)tableName);
			} catch (Exception e) 
			{
				MagicQueriesLogger.logger.error("Error in droping table");
				throw new Exception("Error creating the schema");
			}
		} else 
		{
			MagicQueriesLogger.logger.error("No Table To DROP");
		throw new Exception("Table not found");	
		}
		MagicQueriesLogger.logger.info("Table droped Successfully table name : "+ tableName);

	}

	public void applyChanges() {
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(tableDocument);
			String loc = URL + "\\" + name + ".xml";
			StreamResult result = new StreamResult(new File(loc));
			transformer.transform(source, result);
		} catch (Exception e) {
			System.out
					.println("Error in applynig the changes on the Schema file!!");
		}
	}

	/**
	 * This method is to know whether this schema contains this table or not.
	 * 
	 * @return true if if fin the name of this file in the tableNames List and
	 *         it is not case sensitive and it returns false other wise.
	 */
	public boolean contains(String tableName) {
		for (String s : tableNames) {
			if (s.equalsIgnoreCase(tableName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param the
	 *            name of the table which i want to know the URL of this table
	 * @return the URL or the directory of the XML File table of this name.
	 */
	public String getTableURL(String tableName) {
		if (contains(tableName)) {
			String result = URL + "\\" + tableName + ".xml";
			return result;
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		Schema schema = new Schema(
				"D:\\College Work\\3rd Semester\\OOP\\Assignments\\MyDataBase",
				"College", true);
		schema.createTable("Class2014");
		schema.applyChanges();
		Controller controller = new Controller(schema, "Class2014");
		controller.createColumn("Name", "String");
		controller.createColumn("Age", "Integer");
		controller.createColumn("Adress", "String");
		controller.createColumn("isMaried", "Boolean");

	}
}
