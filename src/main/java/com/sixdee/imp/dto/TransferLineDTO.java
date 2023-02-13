package com.sixdee.imp.dto;

/**
 * 
 * @author Rahul K K
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
 * <td>June 20,2013 04:39:52 PM</td>
 * <td>Rahul K K</td>
 * </tr>
 * </table>
 * </p>
 */



import java.io.Serializable;


public class TransferLineDTO extends CommonVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String donorSubscriber   = null;
	private String recieverSubscriber= null;
	private String donorADSL 	= null;
	private String recieverADSL = null;
	private boolean isPointsReqd     = false;
	
	

	
	
	public String getDonorSubscriber() {
		return donorSubscriber;
	}
	public void setDonorSubscriber(String donorSubscriber) {
		this.donorSubscriber = donorSubscriber;
	}
	public String getRecieverSubscriber() {
		return recieverSubscriber;
	}
	public void setRecieverSubscriber(String recieverSubscriber) {
		this.recieverSubscriber = recieverSubscriber;
	}
	public String getDonorADSL() {
		return donorADSL;
	}
	public void setDonorADSL(String donorADSL) {
		this.donorADSL = donorADSL;
	}
	public String getRecieverADSL() {
		return recieverADSL;
	}
	public void setRecieverADSL(String recieverADSL) {
		this.recieverADSL = recieverADSL;
	}
	public boolean isPointsReqd() {
		return isPointsReqd;
	}
	public void setPointsReqd(boolean isPointsReqd) {
		this.isPointsReqd = isPointsReqd;
	}
	
	
}
