/**
 * 
 */
package com.sixdee.imp.dto;

import java.util.Date;

/**
 * 
 * @author Nithin Kunjappan
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
 * <td>May 29,2013 12:11:41 PM</td>
 * <td>Nithin Kunjappan</td>
 * </tr>
 * </table>
 * </p>
 */
public class VoucherOrderDetailsDTO {
	
	private Long sno;
	
	private Long loyalityID;
	
	private String orderId;
	
	private Date orderDate;
	
	private String  voucherNumber;
	
	private String voucherName;
	
	private int quantity;
	
	private int redeemPoints;
	
	private String orderStatus;
	
	private Date createDate;
	
	private Date expiryDate;
	
	private String subscriberNumber;
	
	//Sajith ks sts 618 ****start
	private String area;
	private String location;
	
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}

	//Sajith ks sts 618 ****end


	public String getSubscriberNumber() {
		return subscriberNumber;
	}

	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}

	/**
	 * @return the expiryDate
	 */
	public Date getExpiryDate() {
		return expiryDate;
	}

	/**
	 * @param expiryDate the expiryDate to set
	 */
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	/**
	 * @return the loyalityID
	 */
	public Long getLoyalityID() {
		return loyalityID;
	}

	/**
	 * @param loyalityID the loyalityID to set
	 */
	public void setLoyalityID(Long loyalityID) {
		this.loyalityID = loyalityID;
	}


	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * @return the orderDate
	 */
	public Date getOrderDate() {
		return orderDate;
	}

	/**
	 * @param orderDate the orderDate to set
	 */
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	/**
	 * @return the voucherNumber
	 */
	public String getVoucherNumber() {
		return voucherNumber;
	}

	/**
	 * @param voucherNumber the voucherNumber to set
	 */
	public void setVoucherNumber(String voucherNumber) {
		this.voucherNumber = voucherNumber;
	}

	/**
	 * @return the voucherName
	 */
	public String getVoucherName() {
		return voucherName;
	}

	/**
	 * @param voucherName the voucherName to set
	 */
	public void setVoucherName(String voucherName) {
		this.voucherName = voucherName;
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the redeemPoints
	 */
	public int getRedeemPoints() {
		return redeemPoints;
	}

	/**
	 * @param redeemPoints the redeemPoints to set
	 */
	public void setRedeemPoints(int redeemPoints) {
		this.redeemPoints = redeemPoints;
	}

	/**
	 * @return the orderStatus
	 */
	public String getOrderStatus() {
		return orderStatus;
	}

	/**
	 * @param orderStatus the orderStatus to set
	 */
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getSno() {
		return sno;
	}

	public void setSno(Long sno) {
		this.sno = sno;
	}
	
	

}
