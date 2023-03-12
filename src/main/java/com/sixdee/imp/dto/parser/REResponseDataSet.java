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
 *          <p>
 *          <b><u>Development History</u></b><br>
 *          <table border="1" width="100%">
 *          <tr>
 *          <td width="15%">Date</td>
 *          <td width="20%">Author</td>
 *          <td>Description</td>
 *          </tr>
 *          <tr>
 *          <td>April 24, 2013</td>
 *          <td>Rahul K K</td>
 *          <td>Created this class</td>
 *          </tr>
 *          </table>
 *          </p>
 */
public class REResponseDataSet {

	 private ArrayList<ReResponseParameter> parameterList = null;
	public ArrayList<Parameters> parameters = null;

	public ArrayList<Parameters> getParameters() {
		return parameters;
	}

	public void setParameters(ArrayList<Parameters> parameters) {
		this.parameters = parameters;
	}

	public ArrayList<ReResponseParameter> getParameterList() {
		return parameterList;
	}

	public void setParameterList(ArrayList<ReResponseParameter> parameterList) {
		this.parameterList = parameterList;
	}

}
