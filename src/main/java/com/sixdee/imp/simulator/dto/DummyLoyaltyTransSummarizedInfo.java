/**
 * 
 */
package com.sixdee.imp.simulator.dto;

import java.io.Serializable;
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
 * <td>April 24, 2013</td>
 * <td>Rahul K K</td>
 * <td>Created this class</td>
 * </tr>
 * </table>
 * </p>
 */
public class DummyLoyaltyTransSummarizedInfo implements Serializable {

	
	private long loyaltyId = 0;
	private double points = 0;
	private double statusPoints = 0;
	private Date createTime = null;
	private double redeemPoints = 0;
	
	
	public double getRedeemPoints() {
		return redeemPoints;
	}
	public void setRedeemPoints(double redeemPoints) {
		this.redeemPoints = redeemPoints;
	}
	public long getLoyaltyId() {
		return loyaltyId;
	}
	public void setLoyaltyId(long loyaltyId) {
		this.loyaltyId = loyaltyId;
	}
	public double getPoints() {
		return points;
	}
	public void setPoints(double points) {
		this.points = points;
	}
	public double getStatusPoints() {
		return statusPoints;
	}
	public void setStatusPoints(double statusPoints) {
		this.statusPoints = statusPoints;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	


}
