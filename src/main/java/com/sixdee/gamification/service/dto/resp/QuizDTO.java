/**
 * 
 */
package com.sixdee.gamification.service.dto.resp;

import com.sixdee.gamification.service.dto.common.Param;

/**
 * @author rahul.kr
 *
 */
public class QuizDTO {

	private int id = 0;
	private String question = null;
	private String hint = null;
	private OptionsDTO[] optionList = null;
	private boolean isEliminate = false;
	private Param parameters = null;
	
	
	public Param getParameters() {
		return parameters;
	}
	public void setParameters(Param parameters) {
		this.parameters = parameters;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getHint() {
		return hint;
	}
	public void setHint(String hint) {
		this.hint = hint;
	}
	
	
	public OptionsDTO[] getOptionList() {
		return optionList;
	}
	public void setOptionList(OptionsDTO[] optionList) {
		this.optionList = optionList;
	}
	public boolean isEliminate() {
		return isEliminate;
	}
	public void setEliminate(boolean isEliminate) {
		this.isEliminate = isEliminate;
	}
	
	
	
}
