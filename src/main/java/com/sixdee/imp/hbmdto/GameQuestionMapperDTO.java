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


public class GameQuestionMapperDTO  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int gameID ;
	private int questionType;
	private int questionId ;
	private int isEliminate ;
	private int isRewarded ;
	private int pointsToAward ;
	private int answerId ;
	private int successMsgId ;
	private int mileStoneId ;
	private int failureMsgId ;
	private int orderId ;
	
	
	public int getGameID() {
		return gameID;
	}
	public void setGameID(int gameID) {
		this.gameID = gameID;
	}
	public int getQuestionType() {
		return questionType;
	}
	public void setQuestionType(int questionType) {
		this.questionType = questionType;
	}
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public int getIsEliminate() {
		return isEliminate;
	}
	public void setIsEliminate(int isEliminate) {
		this.isEliminate = isEliminate;
	}
	public int getIsRewarded() {
		return isRewarded;
	}
	public void setIsRewarded(int isRewarded) {
		this.isRewarded = isRewarded;
	}
	public int getPointsToAward() {
		return pointsToAward;
	}
	public void setPointsToAward(int pointsToAward) {
		this.pointsToAward = pointsToAward;
	}
	public int getAnswerId() {
		return answerId;
	}
	public void setAnswerId(int answerId) {
		this.answerId = answerId;
	}
	public int getSuccessMsgId() {
		return successMsgId;
	}
	public void setSuccessMsgId(int successMsgId) {
		this.successMsgId = successMsgId;
	}
	public int getMileStoneId() {
		return mileStoneId;
	}
	public void setMileStoneId(int mileStoneId) {
		this.mileStoneId = mileStoneId;
	}
	public int getFailureMsgId() {
		return failureMsgId;
	}
	public void setFailureMsgId(int failureMsgId) {
		this.failureMsgId = failureMsgId;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	
	
	
	
}
