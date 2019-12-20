package com.awc.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;

import com.awc.workitemprocessing.DisconnectConnections;
import com.awc.workitemprocessing.StartWorkitemProcessing;

public class InitiateProcessing {

	public static Properties WCLProperty = new Properties();
	static String propertyFileDir = System.getProperty("user.dir") + File.separator + "conf" + File.separator
			+ "conf.properties";
	static String loggerFileDir = System.getProperty("user.dir") + File.separator + "conf" + File.separator
			+ "WCLLog4j.properties";

	static {
		InputStream IS_propertyFileDir;
		try {
			IS_propertyFileDir = new FileInputStream(propertyFileDir);
			WCLProperty.load(IS_propertyFileDir);
			Enumeration<?> e = WCLProperty.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				String val = WCLProperty.get(key) != null ? WCLProperty.get(key).toString() : null;

				if (val == null || val.trim().equals("")) {
					System.out.print("##Invalid conf file entry for key :" + key);
					System.exit(0);
				} else {
					WCLProperty.setProperty(key.trim().toLowerCase(), val.trim());
				}
			}
		} catch (IOException IOE) {
			System.out.println("##Error in loading property file...");
			IOE.printStackTrace();
			System.exit(0);
		}
		LoadLogs.settingLogFiles(loggerFileDir);
		LoadConnections.createDBConnection();
		LoadConnections.connectFlow();
	}

	public static void main(String[] args) {
		System.out.println("From Main");
		try {
			new StartWorkitemProcessing().completePendingWI();
		} catch (Exception e) {
			LoadLogs.Error.info("Exception while Start Workitem Processing... " + e.getMessage());
		} finally {
			try {
				new DisconnectConnections().disconnectFlow();
				LoadConnections.WonderDBConn.close();
				LoadConnections.WonderDBConn = null;
			} catch (SQLException e) {
				LoadLogs.Error.info("Error in closing the connections " + e.getMessage());
			}
		}
	}

}
