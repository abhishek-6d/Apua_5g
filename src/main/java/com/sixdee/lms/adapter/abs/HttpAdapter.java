/**
 * 
 */
package com.sixdee.lms.adapter.abs;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;


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
public abstract class HttpAdapter extends HttpServlet {

	private static final Logger logger = Logger.getLogger("HttpAdapter");
	
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		execute(req,resp,false);
	}
	
	//@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		execute(req,resp,true);		
	}
	
	public void SendRespMsg(HttpServletResponse res,  String xmlStrSend) {
		//if (AppCache.isSendAcknowledgement()) {
			BufferedWriter writer = null;
			try {

				writer = new BufferedWriter(new OutputStreamWriter(res.getOutputStream()));

				if (xmlStrSend == null) {
					throw new Exception("cannot send ack message");
				}

				writer.write(xmlStrSend);
				writer.flush();

			} catch (Exception exp) {
				logger.error("Exception occured while sending ResponseServlet ", exp);

			} finally {
				if(writer!=null){
					try {
						writer.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				writer = null;
			}
		
	}

/*	public abstract void execute(HttpServletRequest req, HttpServletResponse res,boolean isGetMethod) throws ServletException, IOException;
*/	
	public void execute(HttpServletRequest req, HttpServletResponse res,boolean isGetMethod) {
		String reqXml = null;
		InputStream inputStream = null;
		StringBuffer sb = null;
		//RespDTO respDTO = null;
		long timeInSecs = 0;
		String requestId = null;
		ServletInputStream servletInputStream = null;
		String xml = null;
		String response = null;
		Object object = null;
		try{
			//reqXml = req.getParameter("XML");
			logger.error("TransactionId : {} Msisdn : {} Message : Request Recieved In System ");
			timeInSecs = System.currentTimeMillis();
			if(isGetMethod){
				xml = req.getQueryString();
			} else {
				servletInputStream = req.getInputStream();
				inputStream = new BufferedInputStream(servletInputStream,
						32 * 1024);
				sb = new StringBuffer();
				int character = inputStream.read();
				while (character != -1) {

					sb.append((char) character);
					character = inputStream.read();
				}

				reqXml = sb.toString();
				if (reqXml == null || reqXml.trim().equalsIgnoreCase("")) {
					logger.warn("Request Xml is empty");
					throw new Exception("Request Xml is empty");
				}
				
			}
			logger.debug("Request Xml "+reqXml);
			process(res, reqXml);
			response="SUCCESS";
		} catch (IOException e) {
			logger.error("IOException occured ",e);
			response = "Problem in Reading message";
		} catch (Exception e) {
			logger.error("IOException occured ",e);
			response = "Problem in Reading message";
		}finally{
			SendRespMsg(res, response);
			if(inputStream != null){
				try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(servletInputStream!=null){
				try {
					servletInputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
	

	public abstract void process(HttpServletResponse res,String requestRecieved)  ;
}
