/**
 * 
 */
package com.sixdee.imp.utill;

import java.util.ArrayList;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * @author AJith N
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
public class DataSet {
	
	@XStreamImplicit
	public ArrayList<Param> parameter1;

	public ArrayList<Param> getParameter1() {
		return parameter1;
	}

	public void setParameter1(ArrayList<Param> parameter1) {
		this.parameter1 = parameter1;
	}

}
