package com.sixdee.imp.hbmdto;

/**
 * 
 * @author @jith
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
 * <td>March 08,2018 04:23:10 PM</td>
 * <td>@jith</td>
 * </tr>
 * </table>
 * </p>
 */



import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;


public class GameMileStoneMapperDTO  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int gameID ;
	private int mileStoneId;
	private int points;
	private int achievementId;
	private int headerMsgId;
	private int footerMsgId;
	private int menuId;
	public int getGameID() {
		return gameID;
	}
	public void setGameID(int gameID) {
		this.gameID = gameID;
	}
	public int getMileStoneId() {
		return mileStoneId;
	}
	public void setMileStoneId(int mileStoneId) {
		this.mileStoneId = mileStoneId;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	public int getAchievementId() {
		return achievementId;
	}
	public void setAchievementId(int achievementId) {
		this.achievementId = achievementId;
	}
	public int getHeaderMsgId() {
		return headerMsgId;
	}
	public void setHeaderMsgId(int headerMsgId) {
		this.headerMsgId = headerMsgId;
	}
	public int getFooterMsgId() {
		return footerMsgId;
	}
	public void setFooterMsgId(int footerMsgId) {
		this.footerMsgId = footerMsgId;
	}
	public int getMenuId() {
		return menuId;
	}
	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}
	
	
	
	
}
