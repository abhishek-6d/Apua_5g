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
public class InstantPackDTO {
	
	private int packId= 0;
	private long packReleaseCount = 0;
	private long packCount = 0;
	private int lock = 0;
	
	
	
	
	public int getPackId() {
		return packId;
	}
	public void setPackId(int packId) {
		this.packId = packId;
	}
	public long getPackReleaseCount() {
		return packReleaseCount;
	}
	public void setPackReleaseCount(long packReleaseCount) {
		this.packReleaseCount = packReleaseCount;
	}
	public long getPackCount() {
		return packCount;
	}
	public void setPackCount(long packCount) {
		this.packCount = packCount;
	}
	public int getLock() {
		return lock;
	}
	public void setLock(int lock) {
		this.lock = lock;
	}
	
	
}
