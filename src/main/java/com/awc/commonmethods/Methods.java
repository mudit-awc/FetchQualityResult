package com.awc.commonmethods;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.awc.main.LoadConnections;
import com.awc.main.LoadLogs;
import com.newgen.dmsapi.DMSCallBroker;

public class Methods {

	Statement stmt;

	public Methods() {
		try {
			stmt = LoadConnections.WonderDBConn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ResultSet executeQuery(String queryToExecute) throws SQLException {
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery(queryToExecute);
		} catch (SQLException sqle) {
			LoadLogs.Error.info(queryToExecute, sqle);
			LoadLogs.Error.info("Error While executing query :" + queryToExecute + " . Error:" + sqle.getMessage());
			throw new SQLException("Error While executing query :" + queryToExecute + " . Error:" + sqle.getMessage());
		}
		return rs;
	}

	public int executeUpdate(String queryToExecute) throws SQLException {
		int count = -1;
		try {
			count = stmt.executeUpdate(queryToExecute);
		} catch (SQLException sqle) {
			LoadLogs.Error.info(queryToExecute, sqle);
			LoadLogs.Error.info("Error While executing query :" + queryToExecute + " . Error:" + sqle.getMessage());
			throw new SQLException("Error While executing query :" + queryToExecute + " . Error:" + sqle.getMessage());
		}
		return count;
	}

	public String callWebService(String serviceURL, String inputJSON) {
		String outputJSON = "";
		try {
			// LoadLogs.Json.info("serviceURL.. " + serviceURL);
			LoadLogs.Json.info("InputJSON... " + inputJSON);
			URL url = new URL(serviceURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			OutputStream os = conn.getOutputStream();
			os.write(inputJSON.getBytes());
			os.flush();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			outputJSON = br.readLine();
			LoadLogs.Json.info("OutputJSON... " + outputJSON);
		} catch (MalformedURLException ex) {
			LoadLogs.Error.info("Exception in calling web service... " + ex.getMessage());
		} catch (IOException ex) {

		}
		return outputJSON;
	}

	public String executeWithCallBroker(String inputXml) throws NumberFormatException, IOException{
		String outputXml = "";
		LoadLogs.Xml.info("INPUT... " + inputXml);
		outputXml = DMSCallBroker.execute(inputXml, LoadConnections.serverIP, Short.parseShort(LoadConnections.jtsPort), 0);
		LoadLogs.Xml.info("OUTPUT... " + outputXml);
		return outputXml;
	}

	public String getAttribute(String attribute, String tagName, String tagValue) {

		if (tagValue == null) {
			tagValue = "";
		}
		attribute = attribute + "<" + tagName + ">" + tagValue + "</" + tagName + ">";

		return attribute;
	}

}
