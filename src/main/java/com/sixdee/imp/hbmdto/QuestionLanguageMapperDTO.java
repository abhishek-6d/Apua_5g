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


public class QuestionLanguageMapperDTO  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int Id ;
	private int languageID;
	private String question;
	private int questionId;
	
	private QuestionMasterDTO questionMasterDTO;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public int getLanguageID() {
		return languageID;
	}

	public void setLanguageID(int languageID) {
		this.languageID = languageID;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public QuestionMasterDTO getQuestionMasterDTO() {
		return questionMasterDTO;
	}

	public void setQuestionMasterDTO(QuestionMasterDTO questionMasterDTO) {
		this.questionMasterDTO = questionMasterDTO;
	}
	
	
	
	
	
	
}
