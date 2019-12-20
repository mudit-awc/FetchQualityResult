package com.awc.main;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LoadLogs {

	public static Logger Error = null;
	public static Logger Summary = null;
	public static Logger Xml = null;
	public static Logger Json = null;

	public static void settingLogFiles(String loggerFileDir) {
		InputStream is = null;
		try {			
			is = new BufferedInputStream(new FileInputStream(loggerFileDir));
			Properties ps = new Properties();
			ps.load(is);
			is.close();

			// proper shutdown all nested loggers if already exists
			org.apache.log4j.LogManager.shutdown();

			// configure log property
			PropertyConfigurator.configure(ps);

			Error = Logger.getLogger("Error");
			Summary = Logger.getLogger("Summary");
			Xml = Logger.getLogger("Xml");
			Json = Logger.getLogger("Json");
			dumpInitialLogs();

		} catch (Exception e) {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException te) {
				Error.info("Error in setting Logger : " + te);
			}
			e.printStackTrace();
		}
	}

	public static void dumpInitialLogs() {
		Error.info("==================================Error Log Initialized==================================");
		Summary.info("==================================Summary Log Initialized==================================");
		Xml.info("==================================XML Log Initialized==================================");
		Json.info("==================================Json Log Initialized==================================");
	}

	public static void dumpFinalLogs() {
		Error.info("==================================Error Log Ends==================================");
		Summary.info("==================================Summary Log Ends==================================");
		Xml.info("==================================XML Log Ends==================================");
		Json.info("==================================Json Log Ends==================================");
	}
}
