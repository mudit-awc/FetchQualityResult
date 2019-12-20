package com.awc.workitemprocessing;

import java.sql.SQLException;
import java.util.List;
import com.awc.commonmethods.Methods;
import com.awc.main.LoadConnections;
import com.awc.main.LoadLogs;
import com.awc.webservice.GetterSetterMaster;
import com.awc.webservice.GetterSetterSXAQORMContractList;
import com.awc.webservice.GetterSetterSXAQORMGRNContractList;
import com.newgen.dmsapi.DMSXmlResponse;

public class UploadWIData {

	public void InserDataIntoDB(Methods objMethods, GetterSetterMaster objGetterSetterMaster) {

		String Query_cmplx_qomaster = "INSERT INTO cmplx_qomaster (fromdate,itemid,itemname,orderstatus,qodate,qty,"
				+ "qualityorderid,site,source,testgroup,todate,transporterid,transportername,vendorid,vendorname) "
				+ "VALUES (" + objGetterSetterMaster.getFromDate() + "," + objGetterSetterMaster.getItemId() + ","
				+ objGetterSetterMaster.getItemName() + "," + objGetterSetterMaster.getOrderStatus() + ","
				+ objGetterSetterMaster.getqODate() + "," + objGetterSetterMaster.getQty() + ","
				+ objGetterSetterMaster.getQualityOrderID() + "," + objGetterSetterMaster.getSite() + ","
				+ objGetterSetterMaster.getSource() + "," + objGetterSetterMaster.getTestGroup() + ","
				+ objGetterSetterMaster.getToDate() + "," + objGetterSetterMaster.getTransporterId() + ","
				+ objGetterSetterMaster.getTransporterName() + "," + objGetterSetterMaster.getVendorId() + ","
				+ objGetterSetterMaster.getVendorName() + ")";
		try {
			objMethods.executeUpdate(Query_cmplx_qomaster);
		} catch (SQLException e) {
			LoadLogs.Error.info("Exception in inserting row in cmplx_qomaster... " + e.getMessage());
		}

		List<GetterSetterSXAQORMContractList> ContractList = objGetterSetterMaster.getSXAQORMContractList();
		if (ContractList.size() > 0) {
			String Query_cmplx_qogrnmapping = "INSERT INTO cmplx_qogrnmapping (ponumber,packingslipid,qualityorderid) VALUES (";
			for (int i = 0; i < ContractList.size(); i++) {
				Query_cmplx_qogrnmapping = Query_cmplx_qogrnmapping + ContractList.get(i).getPONumber() + ","
						+ ContractList.get(i).getPackingSlipId() + "," + objGetterSetterMaster.getQualityOrderID()
						+ ")";
				try {
					objMethods.executeUpdate(Query_cmplx_qogrnmapping);
				} catch (SQLException e) {
					LoadLogs.Error.info("Exception in inserting row in cmplx_qogrnmapping... " + e.getMessage());
				}
			}
		}

		List<GetterSetterSXAQORMGRNContractList> GRNContractList = objGetterSetterMaster.getSXAQORMGRNContractList();
		if (GRNContractList.size() > 0) {
			String Query_cmplx_qatestresultmapping = "INSERT INTO cmplx_qatestresultmapping "
					+ "(maxvalue,minvalue,standardvalue,testid,testqty,qualityorderid) VALUES (";
			for (int i = 0; i < GRNContractList.size(); i++) {
				Query_cmplx_qatestresultmapping = Query_cmplx_qatestresultmapping + GRNContractList.get(i).getMaxValue()
						+ "," + GRNContractList.get(i).getMinValue() + "," + GRNContractList.get(i).getStandardValue()
						+ "," + GRNContractList.get(i).getTestId() + "," + GRNContractList.get(i).getTestQty() + ","
						+ objGetterSetterMaster.getQualityOrderID() + ")";
				try {
					objMethods.executeUpdate(Query_cmplx_qatestresultmapping);
				} catch (SQLException e) {
					LoadLogs.Error.info("Exception in inserting row in cmplx_qatestresultmapping... " + e.getMessage());
				}
			}
		}
	}

	public String completeWI(String PID, String SessionId, Methods objMethods, GetterSetterMaster objGetterSetterMaster)
			throws Exception {

		String completeStatus = null;
		DMSXmlResponse xmlResponse = null;
		StringBuffer inputXml = new StringBuffer();
		String outputXml = null;
		String mainCode = null;
		String Attributes = prepareAttributes(objGetterSetterMaster);
		System.out.println(Attributes);

		try {
			inputXml.append("<WMGetWorkItem_Input>");
			inputXml.append("<Option>WMGetWorkItem</Option>");
			inputXml.append("<EngineName>" + LoadConnections.engineName + "</EngineName>");
			inputXml.append("<SessionId>" + SessionId + "</SessionId>");
			inputXml.append("<ProcessInstanceId>" + PID + "</ProcessInstanceId>");
			inputXml.append("<WorkItemId>1</WorkItemId>");
			inputXml.append("</WMGetWorkItem_Input>");

			objMethods.executeWithCallBroker(inputXml.toString());

			xmlResponse = new DMSXmlResponse(outputXml);
			mainCode = xmlResponse.getVal("MainCode");

			if (mainCode.equals("0")) {
				mainCode = null;
				inputXml.delete(0, inputXml.length());
				inputXml.append("<WMAssignWorkItemAttributes_Input>");
				inputXml.append("<Option>WMAssignWorkItemAttributes</Option>");
				inputXml.append("<EngineName>demo</EngineName>");
				inputXml.append("<SessionId>" + SessionId + "</SessionId>");
				inputXml.append("<ProcessInstanceId>" + PID + "</ProcessInstanceId>");
				inputXml.append("<WorkItemId>1</WorkItemId>");
				inputXml.append("<ActivityId>9</ActivityId>");
				inputXml.append(
						"<ProcessDefId>" + LoadConnections.getProperty("SupplyPoInvoicesDefID") + "</ProcessDefId>");
				inputXml.append("<LastModifiedTime></LastModifiedTime>");
				inputXml.append("<ActivityType>10</ActivityType>");
				inputXml.append("<complete>D</complete>");
				inputXml.append("<UserDefVarFlag>Y</UserDefVarFlag>");
				inputXml.append("<Attributes>" + Attributes + "</Attributes>");
				inputXml.append("</WMAssignWorkItemAttributes_Input>");

				objMethods.executeWithCallBroker(inputXml.toString());
				xmlResponse = new DMSXmlResponse(outputXml);
				mainCode = xmlResponse.getVal("MainCode");

				if (mainCode.equals("0")) {

				} else {
					return completeStatus;
				}
			} else {
				return completeStatus;
			}
		} catch (Exception e) {
			LoadLogs.Error.info("Error in WMGetWorkItem..." + e.getMessage());
		}
		return completeStatus;
	}

	private String prepareAttributes(GetterSetterMaster obj) throws Exception {
		String attributes = "";

		// list view
		List<GetterSetterSXAQORMGRNContractList> qrm_getter = obj.getSXAQORMGRNContractList();
		for (int i = 0; i < qrm_getter.size(); i++) {
			attributes = attributes + "<q_qatestresultmapping>" + "<maxvalue>" + qrm_getter.get(i).getMaxValue()
					+ "</maxvalue>" + "<minvalue>" + qrm_getter.get(i).getMinValue() + "</minvalue>" + "<standardvalue>"
					+ qrm_getter.get(i).getStandardValue() + "</standardvalue>" + "<testid>"
					+ qrm_getter.get(i).getTestId() + "</testid>" + "<testqty>" + qrm_getter.get(i).getTestQty()
					+ "</testqty>";
			attributes = getAttribute(attributes, "qualityorderid", obj.getQualityOrderID());
			attributes = attributes + "</q_qatestresultmapping>";
		}
		// attributes = getAttribute(attributes, "tagName", "tagValue");
		return attributes;
	}

	private String getAttribute(String attribute, String tagName, String tagValue) {
		if (tagValue == null) {
			tagValue = "";
		}
		attribute = attribute + "<" + tagName + ">" + tagValue + "</" + tagName + ">";
		return attribute;
	}

}
