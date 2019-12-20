package com.awc.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.newgen.dmsapi.DMSCallBroker;
import com.newgen.dmsapi.DMSXmlResponse;

public class LoadConnections {

	public static Connection WonderDBConn = null;
	public static String sessionId = null;
	public static String engineName = "";
	public static String dmsUserName = "";
	public static String dmsUserPswd = "";
	public static String serverIP = "";
	public static String serverPort = "";
	public static String serverType = "";
	public static String jtsPort = "";
	static String sid = "";
	static String dbIP = "";
	static String dbDriverClass = "";
	static String dbDriverSource = "";
	static String username = "";
	static String password = "";

	public static void createDBConnection() {
		try {
			LoadConnections.sid = LoadConnections.getProperty("DatabaseName");
			LoadConnections.dbIP = LoadConnections.getProperty("DatabaseIP");
			LoadConnections.dbDriverClass = LoadConnections.getProperty("DatabaseDriverClass");
			LoadConnections.dbDriverSource = LoadConnections.getProperty("DatabaseDriverSource");
			LoadConnections.username = LoadConnections.getProperty("UserName");
			LoadConnections.password = LoadConnections.getProperty("Password");
			Class.forName(dbDriverClass);

			// Reset DB connection if exists
			try {
				if (WonderDBConn != null) {
					WonderDBConn.close();
					WonderDBConn = null;
				}

			} catch (SQLException se) {
				LoadLogs.Error.info("Exception in reseting DB connection :" + se.getMessage());
				return;
			}

			// Creating new DB connection
			do {
				try {
					WonderDBConn = DriverManager.getConnection(dbDriverSource, username, password);
				} catch (SQLException sqle) {
					LoadLogs.Error.info("Error in connecting DB database. " + sqle.getMessage());
					WonderDBConn = null;
				}

				if (WonderDBConn == null) {
					LoadLogs.Error.info("Waiting for newgen connection...");
					Thread.sleep(5000);
				} else {
					LoadLogs.Error.info("Re-connected with newgen database. ");
				}
			} while (WonderDBConn == null);
		} catch (ClassNotFoundException cnfe) {
			LoadLogs.Error.info("Error While making connection with database." + cnfe.getMessage());
			return;
		} catch (Exception e) {
			LoadLogs.Error.info("Error While making connection with database." + e.getMessage());
			return;
		}
	}

	public static String connectFlow() {
		LoadLogs.Xml.info("Creating flow server connection... ");
		DMSXmlResponse xmlResponse = null;
		String outputXml = null;
		try {
			engineName = LoadConnections.getProperty("EngineName");
			dmsUserName = LoadConnections.getProperty("DMSUserName");
			dmsUserPswd = LoadConnections.getProperty("DMSPassword");
			serverIP = LoadConnections.getProperty("ServerIP");
			jtsPort = LoadConnections.getProperty("JTSPort");
//			serverPort = LoadConnections.getProperty("ServerPort");
//			serverType = LoadConnections.getProperty("ServerType");

			String inputXml = "<? xml version=\"1.0\"?>" + "<NGOConnectCabinet_Input>"
					+ "<Option>NGOConnectCabinet</Option><CabinetName>" + engineName + "</CabinetName><UserName>"
					+ dmsUserName + "</UserName><UserPassword>" + dmsUserPswd + "</UserPassword>"
					+ "<Scope>ADMIN</Scope>"
					+ "<UserExist>N</UserExist><CurrentDateTime></CurrentDateTime><UserType>U</UserType>"
					+ "<MainGroupIndex>0</MainGroupIndex><Locale></Locale></NGOConnectCabinet_Input>";
			LoadLogs.Xml.info("INPUT... " + inputXml);
			outputXml = DMSCallBroker.execute(inputXml.toString(), serverIP, Short.parseShort(jtsPort), 0);
			LoadLogs.Xml.info("OUTPUT... " + outputXml);
			xmlResponse = new DMSXmlResponse(outputXml);
			if (xmlResponse.getVal("Status").equalsIgnoreCase("0")) {
				sessionId = xmlResponse.getVal("UserDBId");
				return sessionId;
			} else {
				LoadLogs.Error.info("Error in making successful flow server connection");
				return null;
			}
		} catch (Exception e) {
			LoadLogs.Error.info("Error while executing product connection call. " + e);
			return null;
		} finally {
			if (xmlResponse != null) {
				xmlResponse = null;
			}
		}
	}

	public static String getProperty(String key) throws Exception {
		String val = null;
		val = InitiateProcessing.WCLProperty.getProperty(key.toLowerCase().trim());
		if (val == null) {
			throw new Exception("No value found for Key:" + key.toLowerCase().trim());
		} else {
			return val;
		}
	}
}
