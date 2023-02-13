package com.sixdee.imp.dto;

/**
 * 
 * @author Jiby Jose
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
 * <td>May 17,2013 01:19:06 PM</td>
 * <td>Jiby Jose</td>
 * </tr>
 * </table>
 * </p>
 */



import java.io.Serializable;


public class MergeAccountDTO extends CommonVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String SubscriberNum1;
	private String SubscriberNum2;
	
	
	public String getSubscriberNum1() {
		return SubscriberNum1;
	}
	public void setSubscriberNum1(String subscriberNum1) {
		SubscriberNum1 = subscriberNum1;
	}
	public String getSubscriberNum2() {
		return SubscriberNum2;
	}
	public void setSubscriberNum2(String subscriberNum2) {
		SubscriberNum2 = subscriberNum2;
	}
	
	
	

}
