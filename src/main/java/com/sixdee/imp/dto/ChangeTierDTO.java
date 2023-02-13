package com.sixdee.imp.dto;

/**
 * 
 * @author Ananth
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
 * <td>December 08,2015 07:13:59 PM</td>
 * <td>Ananth</td>
 * </tr>
 * </table>
 * </p>
 */



import java.io.Serializable;

import org.apache.log4j.Logger;

import com.sixdee.imp.dao.AlufuqSendCodesDAO;
import com.sixdee.imp.service.httpcall.dto.CommonAttributes;

	
public class ChangeTierDTO extends CommonAttributes {

	/**
	 * 
	 */
	Logger logger=Logger.getLogger(ChangeTierDTO.class);
	private String msisdn;
	private String tier;
	private int isRoyalFamily=3;
	
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getTier() {
		return tier;
	}
	public void setTier(String tier) {
		this.tier = tier;
	}
	public int getIsRoyalFamily() {
		return isRoyalFamily;
	}
	public void setIsRoyalFamily(int isRoyalFamily) {
		this.isRoyalFamily = isRoyalFamily;
	}
	public void writeCdr(String requestId, String msisdn2, String timeStamp) {
		logger.fatal(requestId+"|"+msisdn2+"|"+timeStamp);
		
	}
	
	
	

}
