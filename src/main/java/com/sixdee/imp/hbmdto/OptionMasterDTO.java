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


public class OptionMasterDTO  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int optionId ;
	private int optionType;
	private int questionId;
	private int flippingLogic;
	private int headerMsgId;
	private int footerMsgId;
	private int menuId;
	private Set<OptionLanguageMapperDTO> optionLanguageMapperDTO;
	
	
	public int getOptionId() {
		return optionId;
	}
	public void setOptionId(int optionId) {
		this.optionId = optionId;
	}
	public int getOptionType() {
		return optionType;
	}
	public void setOptionType(int optionType) {
		this.optionType = optionType;
	}
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
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
	public Set<OptionLanguageMapperDTO> getOptionLanguageMapperDTO() {
		return optionLanguageMapperDTO;
	}
	public void setOptionLanguageMapperDTO(Set<OptionLanguageMapperDTO> optionLanguageMapperDTO) {
		this.optionLanguageMapperDTO = optionLanguageMapperDTO;
	}
	
	

	
	
	
}
