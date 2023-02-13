package com.sixdee.imp.dto;

import java.util.List;

public class DeleteAccountDTO extends CommonVO {

	private Long moNumber;
	private String ADSLNumber;
	private List<String> deleteNumbers;
	private boolean isAllAccounts;
	private boolean isDelete;
	private boolean isADSL;
	private String Status;
	private String StatusId;
	private String langId;
	private String pin;
	private String deleteKey;
	private int validate;
	private boolean isPrimary;
	private boolean isCdrReqd;
	private List<LoyaltyRegisteredNumberTabDTO> numberList;
	
	/**
	 * @return the isCdrReqd
	 */
	public boolean isCdrReqd() {
		return isCdrReqd;
	}

	/**
	 * @param isCdrReqd the isCdrReqd to set
	 */
	public void setCdrReqd(boolean isCdrReqd) {
		this.isCdrReqd = isCdrReqd;
	}

	/**
	 * @return the numberList
	 */
	public List<LoyaltyRegisteredNumberTabDTO> getNumberList() {
		return numberList;
	}

	/**
	 * @param numberList the numberList to set
	 */
	public void setNumberList(List<LoyaltyRegisteredNumberTabDTO> numberList) {
		this.numberList = numberList;
	}

	/**
	 * @return the isPrimary
	 */
	public boolean isPrimary() {
		return isPrimary;
	}

	/**
	 * @param isPrimary the isPrimary to set
	 */
	public void setPrimary(boolean isPrimary) {
		this.isPrimary = isPrimary;
	}

	/**
	 * @return the validate
	 */
	public int getValidate() {
		return validate;
	}

	/**
	 * @param validate the validate to set
	 */
	public void setValidate(int validate) {
		this.validate = validate;
	}

	/**
	 * @return the deletekey
	 */
	public String getDeleteKey() {
		return deleteKey;
	}

	/**
	 * @param deletekey the deletekey to set
	 */
	public void setDeleteKey(String deleteKey) {
		this.deleteKey = deleteKey;
	}

	/**
	 * @return the pin
	 */
	public String getPin() {
		return pin;
	}

	/**
	 * @param pin the pin to set
	 */
	public void setPin(String pin) {
		this.pin = pin;
	}

	/**
	 * @return the langId
	 */
	public String getLangId() {
		return langId;
	}

	/**
	 * @param langId the langId to set
	 */
	public void setLangId(String langId) {
		this.langId = langId;
	}

	public String getStatusId() {
		return StatusId;
	}

	public void setStatusId(String statusId) {
		StatusId = statusId;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getADSLNumber() {
		return ADSLNumber;
	}

	public void setADSLNumber(String aDSLNumber) {
		ADSLNumber = aDSLNumber;
	}

	public boolean isADSL() {
		return isADSL;
	}

	public void setADSL(boolean isADSL) {
		this.isADSL = isADSL;
	}

	/**
	 * @return the isAllAccounts
	 */
	public boolean isAllAccounts() {
		return isAllAccounts;
	}

	/**
	 * @param isAllAccounts the isAllAccounts to set
	 */
	public void setAllAccounts(boolean isAllAccounts) {
		this.isAllAccounts = isAllAccounts;
	}

	/**
	 * @return the moNumber
	 */
	public Long getMoNumber() {
		return moNumber;
	}

	/**
	 * @param moNumber the moNumber to set
	 */
	public void setMoNumber(Long moNumber) {
		this.moNumber = moNumber;
	}

	public List<String> getDeleteNumbers() {
		return deleteNumbers;
	}

	public void setDeleteNumbers(List<String> deleteNumbers) {
		this.deleteNumbers = deleteNumbers;
	}

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

	

	
	
}
