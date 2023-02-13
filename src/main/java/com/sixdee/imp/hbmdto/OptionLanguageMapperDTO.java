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


public class OptionLanguageMapperDTO  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int optionId ;
	private int languageID;
	private int optionIndex;
	private String option;
	private int questionId;
	private OptionMasterDTO optionMasterDTO;
	
	
	public int getOptionId() {
		return optionId;
	}
	public void setOptionId(int optionId) {
		this.optionId = optionId;
	}
	public int getLanguageID() {
		return languageID;
	}
	public void setLanguageID(int languageID) {
		this.languageID = languageID;
	}
	public int getOptionIndex() {
		return optionIndex;
	}
	public void setOptionIndex(int optionIndex) {
		this.optionIndex = optionIndex;
	}
	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
	}
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public OptionMasterDTO getOptionMasterDTO() {
		return optionMasterDTO;
	}
	public void setOptionMasterDTO(OptionMasterDTO optionMasterDTO) {
		this.optionMasterDTO = optionMasterDTO;
	}

	
	
	
}
