package com.sixdee.imp.service.serviceDTO.resp;

import java.util.Date;


public class ChurnPredictionSummaryResponseDTO extends ResponseDTO  
{
	
	private String rangeID;                          
	                     
	private String msisdn;                          
	private String churnChance;
	private String arpuBand ;
	private String churnStatus;
	private String churnCategory;
	
	
	
	public String getChurnStatus() {
		return churnStatus;
	}
	public void setChurnStatus(String churnStatus) {
		this.churnStatus = churnStatus;
	}
	public String getRangeID() {
		return rangeID;
	}
	public void setRangeID(String rangeID) {
		this.rangeID = rangeID;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getChurnChance() {
		return churnChance;
	}
	public void setChurnChance(String churnChance) {
		this.churnChance = churnChance;
	}
	public String getArpuBand() {
		return arpuBand;
	}
	public void setArpuBand(String arpuBand) {
		this.arpuBand = arpuBand;
	}
	public String getChurnCategory() {
		return churnCategory;
	}
	public void setChurnCategory(String churnCategory) {
		this.churnCategory = churnCategory;
	}

	
	
	

	
}
