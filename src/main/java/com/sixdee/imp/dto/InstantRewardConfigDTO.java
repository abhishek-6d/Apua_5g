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
public class InstantRewardConfigDTO {

	private int id = 0;
	private String packName = null;
	private long packCount = 0;
	private int inServiceId = 0;
	private int flushReqd = 0;
	private int type = 0;
	private String offerSynonym = null;
	private int validityType = 0;
	private long validity = 0;
	private String carryForward = null;
	private String arabicOfferName = null;
	private long units = 0;
	
	
	
	
	
	
	public String getArabicOfferName() {
		return arabicOfferName;
	}
	public void setArabicOfferName(String arabicOfferName) {
		this.arabicOfferName = arabicOfferName;
	}
	public int getValidityType() {
		return validityType;
	}
	public void setValidityType(int validityType) {
		this.validityType = validityType;
	}
	public long getValidity() {
		return validity;
	}
	public void setValidity(long validity) {
		this.validity = validity;
	}
	public String getCarryForward() {
		return carryForward;
	}
	public void setCarryForward(String carryForward) {
		this.carryForward = carryForward;
	}
	public long getUnits() {
		return units;
	}
	public void setUnits(long units) {
		this.units = units;
	}
	public String getOfferSynonym() {
		return offerSynonym;
	}
	public void setOfferSynonym(String offerSynonym) {
		this.offerSynonym = offerSynonym;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	
	public int getInServiceId() {
		return inServiceId;
	}
	public void setInServiceId(int inServiceId) {
		this.inServiceId = inServiceId;
	}
	public int getFlushReqd() {
		return flushReqd;
	}
	public void setFlushReqd(int flushReqd) {
		this.flushReqd = flushReqd;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPackName() {
		return packName;
	}
	public void setPackName(String packName) {
		this.packName = packName;
	}
	public long getPackCount() {
		return packCount;
	}
	public void setPackCount(long packCount) {
		this.packCount = packCount;
	}
	
	
}
