package com.awc.workitemprocessing;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.json.JSONObject;

import com.awc.commonmethods.Methods;
import com.awc.main.LoadConnections;
import com.awc.main.LoadLogs;
import com.awc.webservice.GetterSetterMaster;
import com.awc.webservice.ParseJson;

public class StartWorkitemProcessing {

	String SupplyPoInvoicesDefID = null;
	String CallRMWebserivceDefID = null;
	String GetQualityOrderForrm = null;

	public StartWorkitemProcessing() {
		try {
			SupplyPoInvoicesDefID = LoadConnections.getProperty("SupplyPoInvoicesDefID");
			CallRMWebserivceDefID = LoadConnections.getProperty("CallRMWebserivceDefID");
			GetQualityOrderForrm = LoadConnections.getProperty("GetQualityOrderForrm");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void completePendingWI() {
		try {
			Methods objMethods = new Methods();
			ArrayList<String> PendigPID = new ArrayList<String>();
			String Query_getPendingWI = "SELECT ProcessInstanceID FROM WFINSTRUMENTTABLE WHERE ProcessDefID = '"
					+ SupplyPoInvoicesDefID + "' AND ActivityId = '" + CallRMWebserivceDefID + "'";
			ResultSet RS_getPendingWI = objMethods.executeQuery(Query_getPendingWI);
			while (RS_getPendingWI.next()) {
				PendigPID.add(RS_getPendingWI.getString(1));
			}
			if (PendigPID.size() == 0) {
				LoadLogs.Summary.info("Zero pendig workitem in queue. Shutting down..");
				System.exit(0);
			} else {
				LoadLogs.Summary.info("Total pending WI in Queue... " + PendigPID.size()
						+ ". Pending Process Instance ID's are " + PendigPID);
				for (int i = 0; i < PendigPID.size(); i++) {
					String PID = PendigPID.get(i);
					String Query_pidDetails = "SELECT top 1 purchaseorderno,invoiceno FROM ext_supplypoinvoices WHERE processid ='"
							+ PID + "'";
					ResultSet RS_pidDetails = objMethods.executeQuery(Query_pidDetails);
					LoadLogs.Json.info("Process Instance ID... " + PID);
					while (RS_pidDetails.next()) {
						String PONumber = RS_pidDetails.getString(1);
						String PackingSlipId = RS_pidDetails.getString(2);
//						String Query_Countcmplx_qogrnmapping = "select count(*) from cmplx_qogrnmapping "
//								+ "where ponumber = '" + PONumber + "' and packingslipid = '" + PackingSlipId + "'";
//						ResultSet RS_Countcmplx_qogrnmapping = objMethods.executeQuery(Query_Countcmplx_qogrnmapping);
//						if (RS_Countcmplx_qogrnmapping.getInt(1) == 0) {
//
//						}

						JSONObject request_json = new JSONObject();
						request_json.put("PONumber", PONumber);
						request_json.put("PackingSlipId", PackingSlipId);
						String JSONOutput = objMethods.callWebService(GetQualityOrderForrm, request_json.toString());
						GetterSetterMaster objGetterSetterMaster = new ParseJson().ParseJsonOutput(JSONOutput);

						if (objGetterSetterMaster.getIsSuccess().equalsIgnoreCase("True")) {
							UploadWIData objUploadWIData = new UploadWIData();
							//objUploadWIData.InserDataIntoDB(objMethods, objGetterSetterMaster);
							String completeStatus = objUploadWIData.completeWI(PID, LoadConnections.sessionId,
									objMethods,objGetterSetterMaster);
						} else {
							LoadLogs.Summary.info("Process Instance ID... " + PID);
							LoadLogs.Summary
									.info("Status... Failure due to " + objGetterSetterMaster.getErrorMessage());
						}
					}
				}
			}
		} catch (Exception e) {
			LoadLogs.Error.info("Exception while fetching pending workitems... " + e.getMessage());
		}
	}

}
