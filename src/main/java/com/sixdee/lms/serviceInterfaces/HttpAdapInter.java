/**
 * 
 */
package com.sixdee.lms.serviceInterfaces;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
public interface HttpAdapInter {



	public void SendRespMsg(HttpServletResponse res,  String xmlStrSend) ;
	
	//public  execute(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException ;
	
	public void execute(HttpServletRequest req, HttpServletResponse res,boolean isGetMethod) throws ServletException, IOException ;
	
	//
	//public void setResponseMsg(int adapterType);
	
	

}
