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
 * <td>April 22,2013 11:53:25 PM</td>
 * <td>Paramesh</td>
 * </tr>
 * </table>
 * </p>
 */



import java.io.Serializable;
import java.util.List;

import com.sixdee.imp.service.serviceDTO.common.Data;
import com.sixdee.imp.service.serviceDTO.resp.AccountStatusDTO;


public class ViewAccountDTO extends CommonVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String moNumber;
	private List<String> regsterNumber;
	private boolean isView;
	private boolean isRegisterLines;
	private String adslNumber;
	private String languageID;
	
	private List<AccountStatusDTO> accountStatusList;
	private boolean idFlag;
	private String idFlagValue;
	
	private List<Data> datas;
	
	
	
 
	 
	public List<Data> getDatas() {
		return datas;
	}
	public void setDatas(List<Data> datas) {
		this.datas = datas;
	}
	public String getIdFlagValue() {
		return idFlagValue;
	}
	public void setIdFlagValue(String idFlagValue) {
		this.idFlagValue = idFlagValue;
	}
	public boolean isIdFlag() {
		return idFlag;
	}
	public void setIdFlag(boolean idFlag) {
		this.idFlag = idFlag;
	}
	public String getLanguageID() {
		return languageID;
	}
	public void setLanguageID(String languageID) {
		this.languageID = languageID;
	}
	public List<String> getRegsterNumber() {
		return regsterNumber;
	}
	public void setRegsterNumber(List<String> regsterNumber) {
		this.regsterNumber = regsterNumber;
	}
	/**
	 * @return the isRegisterLines
	 */
	public boolean isRegisterLines() {
		return isRegisterLines;
	}
	/**
	 * @param isRegisterLines the isRegisterLines to set
	 */
	public void setRegisterLines(boolean isRegisterLines) {
		this.isRegisterLines = isRegisterLines;
	}
	/**
	 * @return the adslNumber
	 */
	public String getAdslNumber() {
		return adslNumber;
	}
	/**
	 * @param adslNumber the adslNumber to set
	 */
	public void setAdslNumber(String adslNumber) {
		this.adslNumber = adslNumber;
	}
	/**
	 * @return the accountStatusList
	 */
	public List<AccountStatusDTO> getAccountStatusList() {
		return accountStatusList;
	}
	/**
	 * @param accountStatusList the accountStatusList to set
	 */
	public void setAccountStatusList(List<AccountStatusDTO> accountStatusList) {
		this.accountStatusList = accountStatusList;
	}
	/**
	 * @return the isView
	 */
	public boolean isView() {
		return isView;
	}
	/**
	 * @param isView the isView to set
	 */
	public void setView(boolean isView) {
		this.isView = isView;
	}
	/**
	 * @return the moNumber
	 */
	public String getMoNumber() {
		return moNumber;
	}
	/**
	 * @param moNumber the moNumber to set
	 */
	public void setMoNumber(String moNumber) {
		this.moNumber = moNumber;
	}
	
	
	
	
	
	
	

}
