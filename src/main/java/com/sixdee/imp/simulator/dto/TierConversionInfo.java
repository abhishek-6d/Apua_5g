/**
 * 
 */
package com.sixdee.imp.simulator.dto;

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
public class TierConversionInfo {

	private long loyaltyId = 0;
	private double statusPoints = 0;
	private double newStatusPoints = 0;
	private int tier = 0;
	private int newTier = 0;
	public long getLoyaltyId() {
		return loyaltyId;
	}
	public void setLoyaltyId(long loyaltyId) {
		this.loyaltyId = loyaltyId;
	}
	public double getStatusPoints() {
		return statusPoints;
	}
	public void setStatusPoints(double statusPoints) {
		this.statusPoints = statusPoints;
	}
	public double getNewStatusPoints() {
		return newStatusPoints;
	}
	public void setNewStatusPoints(double newStatusPoints) {
		this.newStatusPoints = newStatusPoints;
	}
	public int getTier() {
		return tier;
	}
	public void setTier(int tier) {
		this.tier = tier;
	}
	public int getNewTier() {
		return newTier;
	}
	public void setNewTier(int newTier) {
		this.newTier = newTier;
	}
	
	
}
