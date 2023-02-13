package com.sixdee.imp.dto;

/**
 * 
 * @author Paramesh
 * @version 1.0
 * 
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%"><b>Date</b></td>
 * <td width="20%"><b>Author</b></td>
 * </tr>
 * <tr>
 * <td>July 24,2013 03:58:55 PM</td>
 * <td>Paramesh</td>
 * </tr>
 * </table>
 * </p>
 */





public class AccountTypeWisePackDTO  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int accountType;
	private String packId;
	private String packName;
	
	public AccountTypeWisePackDTO(int accountType, String packId, String packName) {
		this.accountType = accountType;
		this.packId = packId;
		this.packName = packName;
	}

	public int getAccountType() {
		return accountType;
	}

	public void setAccountType(int accountType) {
		this.accountType = accountType;
	}
 
	public String getPackId() {
		return packId;
	}

	public void setPackId(String packId) {
		this.packId = packId;
	}

	public String getPackName() {
		return packName;
	}

	public void setPackName(String packName) {
		this.packName = packName;
	}
	
	
	
	
	 
	
}
