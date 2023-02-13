/**
 * 
 */
package com.sixdee.imp.dto.parser;

import java.util.HashMap;

import com.sixdee.lms.util.constant.ESBRequestType;


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
public class RERequestHeader {
	private String requestId   = null;
	private String timeStamp   = null;
	private String msisdn	   = null;
	private String keyWord	   = null;
	private String status 	   = null;
	private String statusDesc  = null;
	private Object obj = null;
	private String url = null;
	private HashMap<String, String> respMap = null;
	
	private RERequestDataSet dataSet    = null;
	
	//RE UPLOADER PARAMS
	private String respTimeStamp   = null;
	private String respCode 	   = null;
	private String respDesc  = null;
	private ESBRequestType esbRequestType = null;

	public ESBRequestType getEsbRequestType() {
		return esbRequestType;
	}

	public void setEsbRequestType(ESBRequestType esbRequestType) {
		this.esbRequestType = esbRequestType;
	}

	/**
	 * @return the respTimeStamp
	 */
	public String getRespTimeStamp() {
		return respTimeStamp;
	}

	/**
	 * @param respTimeStamp the respTimeStamp to set
	 */
	public void setRespTimeStamp(String respTimeStamp) {
		this.respTimeStamp = respTimeStamp;
	}

	/**
	 * @return the respCode
	 */
	public String getRespCode() {
		return respCode;
	}

	/**
	 * @param respCode the respCode to set
	 */
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	/**
	 * @return the respDesc
	 */
	public String getRespDesc() {
		return respDesc;
	}

	/**
	 * @param respDesc the respDesc to set
	 */
	public void setRespDesc(String respDesc) {
		this.respDesc = respDesc;
	}

	public HashMap<String, String> getRespMap() {
		return respMap;
	}

	public void setRespMap(HashMap<String, String> respMap) {
		this.respMap = respMap;
	}

	public Object getObj() {
		return obj;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public RERequestDataSet getDataSet() {
		return dataSet;
	}

	public void setDataSet(RERequestDataSet dataSet) {
		this.dataSet = dataSet;
	}


	
}
