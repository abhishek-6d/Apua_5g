/**
 * 
 */
package com.sixdee.imp.dto;

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
public class RestoreDTO {
	
	private String loyaltyId = null;
	private String subsNo = null;
	private double points = 0;
	private double statusPoints = 0;
	public String getLoyaltyId() {
		return loyaltyId;
	}
	public void setLoyaltyId(String loyaltyId) {
		this.loyaltyId = loyaltyId;
	}
	public String getSubsNo() {
		return subsNo;
	}
	public void setSubsNo(String subsNo) {
		this.subsNo = subsNo;
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
	
	
	
}
