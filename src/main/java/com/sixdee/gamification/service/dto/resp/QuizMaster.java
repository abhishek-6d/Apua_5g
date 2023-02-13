/**
 * 
 */
package com.sixdee.gamification.service.dto.resp;

/**
 * @author rahul.kr
 *
 */
public class QuizMaster {

private boolean isListAllQuestions = false;
	
private QuizDTO[] quizList = null;
	

public boolean isListAllQuestions() {
	return isListAllQuestions;
}
public void setListAllQuestions(boolean isListAllQuestions) {
	this.isListAllQuestions = isListAllQuestions;
}
public QuizDTO[] getQuizList() {
	return quizList;
}
public void setQuizList(QuizDTO[] quizList) {
	this.quizList = quizList;
}


}
