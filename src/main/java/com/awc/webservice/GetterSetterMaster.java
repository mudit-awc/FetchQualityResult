package com.awc.webservice;

import java.util.ArrayList;
import java.util.List;

public class GetterSetterMaster {

	private String errorMessage;
	private String fromDate;
	private String isSuccess;
	private String itemId;
	private String itemName;
	private String orderStatus;
	private String qODate;
	private String qty;
	private String qualityOrderID;
	private String site;
	private String source;
	private String testGroup;
	private String toDate;
	private String transporterId;
	private String transporterName;
	private String vendorId;
	private String vendorName;
	List<GetterSetterSXAQORMContractList> ContractList = new ArrayList<GetterSetterSXAQORMContractList>();
	List<GetterSetterSXAQORMGRNContractList> GRNContractList = new ArrayList<GetterSetterSXAQORMGRNContractList>();

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(String isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getqODate() {
		return qODate;
	}

	public void setqODate(String qODate) {
		this.qODate = qODate;
	}

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	public String getQualityOrderID() {
		return qualityOrderID;
	}

	public void setQualityOrderID(String qualityOrderID) {
		this.qualityOrderID = qualityOrderID;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTestGroup() {
		return testGroup;
	}

	public void setTestGroup(String testGroup) {
		this.testGroup = testGroup;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getTransporterId() {
		return transporterId;
	}

	public void setTransporterId(String transporterId) {
		this.transporterId = transporterId;
	}

	public String getTransporterName() {
		return transporterName;
	}

	public void setTransporterName(String transporterName) {
		this.transporterName = transporterName;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public List<GetterSetterSXAQORMContractList> getSXAQORMContractList() {
		return ContractList;
	}

	public void setSXAQORMContractList(List<GetterSetterSXAQORMContractList> sXAQORMContractList) {
		ContractList = sXAQORMContractList;
	}

	public List<GetterSetterSXAQORMGRNContractList> getSXAQORMGRNContractList() {
		return GRNContractList;
	}

	public void setSXAQORMGRNContractList(List<GetterSetterSXAQORMGRNContractList> sXAQORMGRNContractList) {
		GRNContractList = sXAQORMGRNContractList;
	}

	

}
