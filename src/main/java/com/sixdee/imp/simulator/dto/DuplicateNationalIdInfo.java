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
public class DuplicateNationalIdInfo {
	
	private String actualNationalId = null;
	private String proposedNationalId = null;
	private long actualLoyaltyId = 0l;
	private long proposedLoyaltyId = 0l;
	private double points = 0l;
	private double statusPoints = 0;
	private int actualLines = 0;
	private int status = 100;
	
	
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public double getStatusPoints() {
		return statusPoints;
	}
	public void setStatusPoints(double statusPoints) {
		this.statusPoints = statusPoints;
	}
	public String getActualNationalId() {
		return actualNationalId;
	}
	public void setActualNationalId(String actualNationalId) {
		this.actualNationalId = actualNationalId;
	}
	public String getProposedNationalId() {
		return proposedNationalId;
	}
	public void setProposedNationalId(String proposedNationalId) {
		this.proposedNationalId = proposedNationalId;
	}
	public long getActualLoyaltyId() {
		return actualLoyaltyId;
	}
	public void setActualLoyaltyId(long actualLoyaltyId) {
		this.actualLoyaltyId = actualLoyaltyId;
	}
	public long getProposedLoyaltyId() {
		return proposedLoyaltyId;
	}
	public void setProposedLoyaltyId(long proposedLoyaltyId) {
		this.proposedLoyaltyId = proposedLoyaltyId;
	}
	public double getPoints() {
		return points;
	}
	public void setPoints(double points) {
		this.points = points;
	}
	public int getActualLines() {
		return actualLines;
	}
	public void setActualLines(int actualLines) {
		this.actualLines = actualLines;
	}
	
	

}
