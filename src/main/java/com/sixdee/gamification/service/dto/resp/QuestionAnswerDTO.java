/**
 * 
 */
package com.sixdee.gamification.service.dto.resp;

import com.sixdee.gamification.service.dto.common.Param;
import com.sixdee.imp.dto.parser.in.Parameter;

/**
 * @author rahul.kr
 *
 */
public class QuestionAnswerDTO {

	private int questionID = 0;
	private int optionID  = 0; 
	private String feedback = null;
	private Parameter parameter = null;
	
	
	

	public Parameter getParameter() {
		return parameter;
	}
	public void setParameter(Parameter parameter) {
		this.parameter = parameter;
	}
	public int getQuestionID() {
		return questionID;
	}
	public void setQuestionID(int questionID) {
		this.questionID = questionID;
	}
	public int getOptionID() {
		return optionID;
	}
	public void setOptionID(int optionID) {
		this.optionID = optionID;
	}
	public String getFeedback() {
		return feedback;
	}
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	
	
	
	
	
}
