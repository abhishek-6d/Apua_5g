/**
 * 
 */
package com.sixdee.imp.dto.parser;

import java.util.ArrayList;

import com.sixdee.imp.utill.RuleEngine.Parameters;

/**
 * @author Rahul K K
 * @version 1.0.0
 * 
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%">Date</td>
 * <td width="20%">Author</td>
 * <td>Description</td>
 * </tr>
 * <tr>
 * <td>April 24, 2013</td>
 * <td>Rahul K K</td>
 * <td>Created this class</td>
 * </tr>
 * </table>
 * </p>
 */
public class RERequestDataSet {
	
	private ArrayList<ReResponseParameter> responseParam = null;
	
	public ArrayList<Parameters> parameters=null;
	
	
	
	public ArrayList<Parameters> getParameters() {
		return parameters;
	}

	public void setParameters(ArrayList<Parameters> parameters) {
		this.parameters = parameters;
	}

	public ArrayList<ReResponseParameter> getResponseParam() {
		return responseParam;
	}

	public void setResponseParam(ArrayList<ReResponseParameter> responseParam) {
		this.responseParam = responseParam;
	}
	
	
}
