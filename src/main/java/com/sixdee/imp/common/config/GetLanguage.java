/**
 * 
 */
package com.sixdee.imp.common.config;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sixdee.imp.common.dao.LanguageDAO;
import com.sixdee.imp.common.dto.Request;
import com.thoughtworks.xstream.XStream;


/**
 * 
 * @author Nithin Kunjappan
 * @version 1.0 
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%"><b>Date</b></td>
 * <td width="20%"><b>Author</b></td>
 * </tr>
 * <tr>
 * <td>JUL 05, 2013</td>
 * <td>Nithin Kunjappan</td>
 * </tr>
 * </table>
 * </p>
 */
public class GetLanguage extends HttpServlet{
	
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Logger logger=Logger.getLogger(GetLanguage.class);
	
	Date date = new Date();
	
	LanguageDAO langDAO= null;
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		execute(req, res);
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		execute(req, res);
	}
	
	
	public void execute(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException
	{
		XStream xstream= Cache.GetlanguageXstream;	
		InputStream inputStream = null;
		StringBuffer sb = null;
		String xml = null;
		Request requestDTO=null;
		String statusDesc="Processed Succesfully";
		try
		{
			langDAO=new LanguageDAO();
			requestDTO=new Request();
			inputStream = new BufferedInputStream(req.getInputStream(),32 * 1024);
			sb = new StringBuffer();

			int character = inputStream.read();

			while (character != -1) 
			{
				sb.append((char) character);
				character = inputStream.read();
			}			
			xml =sb.toString();
			
			if(xml!=null && xml.length()!=0)
			{	
				logger.info("REQUEST received "+xml);
				requestDTO=(Request) xstream.fromXML(xml);
				logger.info("MSISDN ::"+requestDTO.getMsisdn());
				
				String langIDandFlag=langDAO.getLanguageID(requestDTO.getMsisdn());
				
				logger.info("LANGUAGE ID From subscriber Base ::"+langIDandFlag);
				
				if(langIDandFlag==null||langIDandFlag.trim().equalsIgnoreCase(""))
				{
					langIDandFlag= Cache.getCacheMap().get("DEFAULT_LANGUAGEID");
					statusDesc="Subscriber Number Not Found,Sending Default Language";
				}
				
				
				logger.info("LANGUAGE ID ::"+langIDandFlag);
				
				SendRespMsg(res,"0000",statusDesc,requestDTO.getMsisdn(), langIDandFlag);
				
				
				/*if(Boolean.parseBoolean(langIDandFlag.split("::")[1]))
					SendRespMsg(res,"0000","Processed Succesfully",requestDTO.getMsisdn(), langIDandFlag.split("::")[0]);
				else
					SendRespMsg(res,"0000","Subscriber Number Not Found,Sending Default Language",requestDTO.getMsisdn(),langIDandFlag.split("::")[0]);					*/
				
				
				
			}
			else
			{
					logger.info("REQUEST XML is NULL...!!!");
					SendRespMsg(res,"0001","Request XML is NULL","NA","NA");
			}		
			
		
		}catch (Exception e) {		
			e.printStackTrace();		
			logger.error("Exception Occured in Execution:::"+xml);
			SendRespMsg(res,"0001","Exception...Try Again","NA","NA");
			
	
		} finally {
			if (inputStream != null) {
				inputStream.close();
				inputStream = null;
			}
			xstream= null;	
			sb = null;
			xml = null;
			requestDTO=null;
			langDAO = null;
		}
	}//execute

public void SendRespMsg(HttpServletResponse res, String respCode, String respMsg, String msisdn,String languageId) 
{
	BufferedWriter writer = null;
	try
	{
	    writer = new BufferedWriter(new OutputStreamWriter(res.getOutputStream()));	   
	    writer.write("<Response>");
	    writer.write("<timeStamp>"+date+"</timeStamp>");
	    writer.write("<msisdn>"+msisdn+"</msisdn>");
	    writer.write("<languageId>"+languageId+"</languageId>");	   
	    writer.write("<statusCode>"+respCode+"</statusCode>");
	    writer.write("<status>"+respMsg+"</status>");
	    writer.write("</Response>");
	    writer.flush();
	    logger.info("RESPONSE SEND :: <Response><timeStamp>"+date+"</timeStamp><msisdn>"+msisdn+"</msisdn><languageId>"+languageId+"</languageId><statusCode>"+respCode+"</statusCode><status>"+respMsg+"</status></Response>");
	}
	catch (Exception exp) 
	{
		logger.error("Exception Occured while Sending Response:::"+exp);
	}
	finally 
	{
		if(writer != null){
			try {
				writer.close();
				writer = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		msisdn = null;
	    languageId =null;
	    respCode=null;
	    respMsg=null;
	}
}
	


}
