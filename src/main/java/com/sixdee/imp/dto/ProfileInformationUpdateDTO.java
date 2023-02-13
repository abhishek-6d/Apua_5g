/**
 * 
 */
package com.sixdee.imp.dto;

/**
 * @author Rahul K K
 * @version 1.0.0
 * 
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%">Date</td>
 * <td width="20%">Author</td>
 * <td>Description</td>
 * </tr>
 * <tr>
 * <td>April 24, 2013</td>
 * <td>Rahul K K</td>
 * <td>Created this class</td>
 * </tr>
 * </table>
 * </p>
 */
public class ProfileInformationUpdateDTO extends CommonVO{
	
	private String oldNationalId = null;
	private String oldNationalIdType = null;
	private String newNationalId = null;
	private String newNationalIdType = null;
	private String msisdn = null;
	private int opCode = 0;
	
	
	
	
	@Override
	public String toString() {
		return getTransactionId()+"|"+getTimestamp()+"|44|"+getChannel()+"|"+getStatusCode()+
				"|"+getStatusDesc()+"|"+msisdn+"|"+oldNationalId+"|"+oldNationalIdType+"|"+newNationalId+"|"+newNationalIdType+"|"+opCode;
	}
	
	
	public int getOpCode() {
		return opCode;
	}


	public void setOpCode(int opCode) {
		this.opCode = opCode;
	}


	public String getOldNationalId() {
		return oldNationalId;
	}
	public void setOldNationalId(String oldNationalId) {
		this.oldNationalId = oldNationalId;
	}
	
	public String getOldNationalIdType() {
		return oldNationalIdType;
	}
	public void setOldNationalIdType(String oldNationalIdType) {
		this.oldNationalIdType = oldNationalIdType;
	}
	public String getNewNationalId() {
		return newNationalId;
	}
	public void setNewNationalId(String newNationalId) {
		this.newNationalId = newNationalId;
	}
	public String getNewNationalIdType() {
		return newNationalIdType;
	}
	public void setNewNationalIdType(String newNationalIdType) {
		this.newNationalIdType = newNationalIdType;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	
	
	
}
