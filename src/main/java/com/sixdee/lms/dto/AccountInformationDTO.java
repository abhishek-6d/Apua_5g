/**
 * 
 */
package com.sixdee.lms.dto;

/**
 * @author rahul.kr
 *
 */
public class AccountInformationDTO {

	private String msisdn = "";
	private String asa = "";
	private String startDate = null;
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getAsa() {
		return asa;
	}
	public void setAsa(String asa) {
		this.asa = asa;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	@Override
	public String toString() {
		StringBuilder sbuild = new StringBuilder("");
		
		sbuild.append(msisdn).append("#&")
			.append(asa).append("#&").append(startDate);
		return sbuild.toString();
	}
}
