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
 * <td>April 24, 2013</td>
 * <td>Rahul K K</td>
 * <td>Created this class</td>
 * </tr>
 * </table>
 * </p>
 */
public class TierDetails {

	
	private int tierId = 0;
	private String tierName = null;
	private Date tierUpdateDate = null;
	private Date tierExpiryDate = null;
	
	private int nextTier = 0;
	private String nextTierName ;
	private int pointsToNextTier = 0;
	private int statusPoints = 0;
	private long rewardPoints;
	
	
	
	
	
	
	public String getNextTierName() {
		return nextTierName;
	}
	public void setNextTierName(String nextTierName) {
		this.nextTierName = nextTierName;
	}
	public long getRewardPoints() {
		return rewardPoints;
	}
	public void setRewardPoints(long rewardPoints) {
		this.rewardPoints = rewardPoints;
	}
	public Date getTierExpiryDate() {
		return tierExpiryDate;
	}
	public void setTierExpiryDate(Date tierExpiryDate) {
		this.tierExpiryDate = tierExpiryDate;
	}
	public int getStatusPoints() {
		return statusPoints;
	}
	public void setStatusPoints(int statusPoints) {
		this.statusPoints = statusPoints;
	}
	public int getTierId() {
		return tierId;
	}
	public void setTierId(int tierId) {
		this.tierId = tierId;
	}
	public String getTierName() {
		return tierName;
	}
	public void setTierName(String tierName) {
		this.tierName = tierName;
	}
	public Date getTierUpdateDate() {
		return tierUpdateDate;
	}
	public void setTierUpdateDate(Date tierUpdateDate) {
		this.tierUpdateDate = tierUpdateDate;
	}
	public int getNextTier() {
		return nextTier;
	}
	public void setNextTier(int nextTier) {
		this.nextTier = nextTier;
	}
	public int getPointsToNextTier() {
		return pointsToNextTier;
	}
	public void setPointsToNextTier(int pointsToNextTier) {
		this.pointsToNextTier = pointsToNextTier;
	}
	
	
}
