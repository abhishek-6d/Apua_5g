/**
 * 
 */
package com.sixdee.lms.util.selections;

/**
 * @author rahul.kr
 *
 */
public enum CDRCommandID {
	

	
	Registration(1),UpdateAccount(2),PointAccumulation(3),Redemption(4),PointExpiry(6),TransferPoints(5),RewardPointCaluculation(7);
	
	private int id ;
	private CDRCommandID(int identifier) {
		// TODO Auto-generated constructor stub
		id = identifier;
	}
	
	public int getId() {
		return id;
	}
}
