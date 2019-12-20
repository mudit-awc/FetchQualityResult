package com.awc.webservice;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class ParseJson {
	GetterSetterMaster objGetterSetterMaster = new GetterSetterMaster();

	public GetterSetterMaster ParseJsonOutput(String JSONOutput) {

		JSONObject objJSONObject = new JSONObject(JSONOutput);
		String IsSuccess = objJSONObject.optString("IsSuccess");
		String ErrorMessage = objJSONObject.optString("ErrorMessage");

		objGetterSetterMaster.setIsSuccess(IsSuccess);
		objGetterSetterMaster.setErrorMessage(ErrorMessage);

		if (IsSuccess.equalsIgnoreCase("True")) {
			objGetterSetterMaster.setFromDate(objJSONObject.optString("FromDate"));
			objGetterSetterMaster.setItemId(objJSONObject.optString("ItemId"));
			objGetterSetterMaster.setItemName(objJSONObject.optString("ItemName"));
			objGetterSetterMaster.setOrderStatus(objJSONObject.optString("OrderStatus"));
			objGetterSetterMaster.setqODate(objJSONObject.optString("QODate"));
			objGetterSetterMaster.setQty(objJSONObject.optString("Qty"));
			objGetterSetterMaster.setQualityOrderID(objJSONObject.optString("QualityOrderID"));
			objGetterSetterMaster.setSite(objJSONObject.optString("Site"));
			objGetterSetterMaster.setSource(objJSONObject.optString("Source"));
			objGetterSetterMaster.setTestGroup(objJSONObject.optString("TestGroup"));
			objGetterSetterMaster.setToDate(objJSONObject.optString("ToDate"));
			objGetterSetterMaster.setTransporterId(objJSONObject.optString("TransporterId"));
			objGetterSetterMaster.setTransporterName(objJSONObject.optString("TransporterName"));
			objGetterSetterMaster.setVendorId(objJSONObject.optString("VendorId"));
			objGetterSetterMaster.setVendorName(objJSONObject.optString("VendorName"));

			List<GetterSetterSXAQORMContractList> ContractList = new ArrayList<GetterSetterSXAQORMContractList>();
			org.json.JSONArray jaContractList = objJSONObject.getJSONArray("SXAQORMGRNContractList");
			for (int i = 0; i < jaContractList.length(); i++) {
				GetterSetterSXAQORMContractList objGetterSetterSXAQORMContractList = new GetterSetterSXAQORMContractList();
				objGetterSetterSXAQORMContractList.setPONumber(jaContractList.getJSONObject(i).optString("PONumber"));
				objGetterSetterSXAQORMContractList
						.setPackingSlipId(jaContractList.getJSONObject(i).optString("PackingSlipId"));
				ContractList.add(objGetterSetterSXAQORMContractList);
			}

			List<GetterSetterSXAQORMGRNContractList> GRNContractList = new ArrayList<GetterSetterSXAQORMGRNContractList>();
			org.json.JSONArray jaGRNContractList = objJSONObject.getJSONArray("SXAQORMGRNContractList");
			for (int i = 0; i < jaGRNContractList.length(); i++) {
				GetterSetterSXAQORMGRNContractList objGetterSetterGRNContractList = new GetterSetterSXAQORMGRNContractList();
				objGetterSetterGRNContractList.setMaxValue(jaGRNContractList.getJSONObject(i).optString("MaxValue"));
				objGetterSetterGRNContractList.setMinValue(jaGRNContractList.getJSONObject(i).optString("MinValue"));
				objGetterSetterGRNContractList
						.setStandardValue(jaGRNContractList.getJSONObject(i).optString("StandardValue"));
				objGetterSetterGRNContractList.setTestId(jaGRNContractList.getJSONObject(i).optString("TestId"));
				objGetterSetterGRNContractList.setTestQty(jaGRNContractList.getJSONObject(i).optString("TestQty"));

				GRNContractList.add(objGetterSetterGRNContractList);
			}
			objGetterSetterMaster.setSXAQORMContractList(ContractList);
			objGetterSetterMaster.setSXAQORMGRNContractList(GRNContractList);
		} 
//		else {
//			LoadLogs.Json.info("Error in JSONoutput..." + ErrorMessage);
//		}
		return objGetterSetterMaster;
	}
}
