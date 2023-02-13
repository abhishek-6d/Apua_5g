/**
 * 
 */
package com.sixdee.imp.dto;

import java.util.Date;

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
 * <td>June 30, 2013</td>
 * <td>Rahul K K</td>
 * <td>Created this class</td>
 * </tr>
 * </table>
 * </p>
 */
public class InstantReserveDTO {

  /*
   * Subscriber who is eligible for reward
   */
  private long id = 0;
  private long subscriberNumber = 0;
  /*
   * Subscriber to whom reward is transfered
   */
  private long bSubscriberNumber = 0;
  private long offerId = 0;
  private long packId = 0;
  private String offerName=null;
  /*
   * Status
   * 1 - Alloted
   * 2 - Transfered 
   * 3 - Expired
   * 4 - Given 
   */
  private Date createDate = null;
  private Date updateDate = null;
  private int status = 0;
  
  
  
  
  public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
}
public Date getCreateDate() {
	return createDate;
}
public void setCreateDate(Date createDate) {
	this.createDate = createDate;
}
public Date getUpdateDate() {
	return updateDate;
}
public void setUpdateDate(Date updateDate) {
	this.updateDate = updateDate;
}
public String getOfferName() {
	return offerName;
}
public void setOfferName(String offerName) {
	this.offerName = offerName;
}
public long getPackId() {
	return packId;
}
public void setPackId(long packId) {
	this.packId = packId;
}
public int getStatus() {
	return status;
}
public void setStatus(int status) {
	this.status = status;
}
public long getSubscriberNumber() {
	return subscriberNumber;
}
public void setSubscriberNumber(long subscriberNumber) {
	this.subscriberNumber = subscriberNumber;
}
public long getbSubscriberNumber() {
	return bSubscriberNumber;
}
public void setbSubscriberNumber(long bSubscriberNumber) {
	this.bSubscriberNumber = bSubscriberNumber;
}
public long getOfferId() {
	return offerId;
}
public void setOfferId(long offerId) {
	this.offerId = offerId;
}


  
  
  
}
