package com.awc.workitemprocessing;

import com.awc.commonmethods.Methods;
import com.awc.main.LoadConnections;
import com.awc.main.LoadLogs;
import com.newgen.dmsapi.DMSInputXml;
import com.newgen.dmsapi.DMSXmlResponse;

public class DisconnectConnections {

	public boolean disconnectFlow() {
		LoadLogs.Xml.info("Disconnecting flow server connection... ");
		boolean disconnectflag = false;
		DMSInputXml xml = new DMSInputXml();
		String outputXml = null;
		DMSXmlResponse xmlResponse = null;
		Methods objMethods = new Methods();
		try {
			String inputXml = xml.getDisconnectCabinetXml(LoadConnections.engineName, LoadConnections.sessionId);
			objMethods.executeWithCallBroker(inputXml);
			xmlResponse = new DMSXmlResponse(outputXml);
			if (xmlResponse.getVal("Status").equalsIgnoreCase("0")) {
				LoadLogs.Summary.info("Disconnecting flow server connection Successful");
				disconnectflag = true;
			} else {
				LoadLogs.Error.info("Error in disconnecting flow server. Session: " + LoadConnections.sessionId);
				disconnectflag = false;
			}

		} catch (Exception e) {
			LoadLogs.Error.info("Error while executing product call. " + e);
			return false;
		} finally {
			if (xmlResponse != null) {
				xmlResponse = null;
			}
			if (xml != null) {
				xml = null;
			}
		}
		return disconnectflag;
	}

}
