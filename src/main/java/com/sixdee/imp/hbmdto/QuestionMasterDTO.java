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


public class QuestionMasterDTO  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int questionId 			=0;
	private int questionType		=0;
	private int flippingLogic		=0;
	private int headerMsgId			=0;
	private int footerMsgId			=0;
	private int menuId				=0;
	private Set<QuestionLanguageMapperDTO> questionLanguageMapperDTOs;
	
	
	
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public int getQuestionType() {
		return questionType;
	}
	public void setQuestionType(int questionType) {
		this.questionType = questionType;
	}
	
	public int getFlippingLogic() {
		return flippingLogic;
	}
	public void setFlippingLogic(int flippingLogic) {
		this.flippingLogic = flippingLogic;
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
	public Set<QuestionLanguageMapperDTO> getQuestionLanguageMapperDTOs() {
		return questionLanguageMapperDTOs;
	}
	public void setQuestionLanguageMapperDTOs(Set<QuestionLanguageMapperDTO> questionLanguageMapperDTOs) {
		this.questionLanguageMapperDTOs = questionLanguageMapperDTOs;
	}
	
	
	
}
