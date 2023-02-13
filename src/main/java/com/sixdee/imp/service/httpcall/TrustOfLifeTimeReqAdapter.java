/**
 * 
 */
package com.sixdee.imp.service.httpcall;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dao.TOLSubscriberCheckDAO;
import com.sixdee.imp.dto.TOLSubscriberCheckDTO;
import com.sixdee.imp.service.httpcall.dto.SubscriberDataSet;
import com.sixdee.imp.service.httpcall.dto.SubscriberRequestParam;
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
 * <td>OCT 29, 2013</td>
 * <td>Nithin Kunjappan</td>
 * </tr>
 * </table>
 * </p>
 */
public class TrustOfLifeTimeReqAdapter extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	private Logger logger = Logger.getLogger(TrustOfLifeTimeReqAdapter.class);

	public void doGet(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException {
		execute(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException {
		execute(req, res);
	}

	private void execute(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException {
		
		InputStream inputStream = null;
		StringBuffer sb = null;
		String xml = null;
		TOLSubscriberCheckDTO tolReqDTO = null;
		ArrayList<SubscriberRequestParam> param = null;
		SubscriberDataSet subscriberDataSet = null;
		XStream xstream= Cache.TOLXstream;	
		int i = 0;
		String scheduleID = null;
		String interval = null;
		String action = null;
		try {

			inputStream = new BufferedInputStream(req.getInputStream(),32 * 1024);
			sb = new StringBuffer();

			int character = inputStream.read();

			while (character != -1) {
				sb.append((char) character);
				character = inputStream.read();
			
			}			
			xml =sb.toString();
			
			if(xml!=null && xml.length()!=0)
			{
				logger.info("Request Received >>"+xml);
				
				tolReqDTO= (TOLSubscriberCheckDTO) xstream.fromXML(xml);				
				
				subscriberDataSet = tolReqDTO.getDataSet();
				param = subscriberDataSet.getParam();
				for(SubscriberRequestParam subscriberRequestParam:param){
					if(subscriberRequestParam.getId().equalsIgnoreCase("SERVICE_ID")){
						scheduleID = subscriberRequestParam.getValue();
						tolReqDTO.setScheduleID(Long.parseLong(scheduleID.trim()));
						i++;
					}/*else if(subscriberRequestParam.getId().equalsIgnoreCase("INTERVAL")){
						interval = subscriberRequestParam.getValue();
						tolReqDTO.setInterval(Integer.parseInt(interval.trim()));
						i++;
					}*/
					else if(subscriberRequestParam.getId().equalsIgnoreCase("ACTION_TAG")){
						action = subscriberRequestParam.getValue();
						tolReqDTO.setActionType(action.trim());
						i++;
					}
					if(i==2)
						break;
				}
				
				logger.info(tolReqDTO.getRequestId()+" : SUBSCRIBER NUMBER::"+tolReqDTO.getSubscriberNumber());
				logger.info(tolReqDTO.getRequestId()+" : SERVICE ID::"+scheduleID);
			//	logger.info(tolReqDTO.getRequestId()+" : INTERVAL::"+interval);
				logger.info(tolReqDTO.getRequestId()+" : ACTION::"+action);
				
				//calling dao & setting status
				
				if(new TOLSubscriberCheckDAO().checkTOL(tolReqDTO))
				{
					tolReqDTO.setStatus("SC0000");
					tolReqDTO.setStatusDesc("SUCCESS");
				}else{
					tolReqDTO.setStatus("SC1000");
					tolReqDTO.setStatusDesc("FAILURE");
				}
				
				SendRespMsg(res,tolReqDTO);
				
			}
			else
			{
				tolReqDTO= new TOLSubscriberCheckDTO(); 
				logger.info("REQUEST XML is NULL...!!!");
				tolReqDTO.setStatus("SC0001");
				tolReqDTO.setStatusDesc("REQUEST XML is NULL");
				SendRespMsg(res,tolReqDTO);
			}		
		}catch (Exception e) {		
			e.printStackTrace();		
			logger.error("Exception Occured in Execution:::"+xml);
				
		} finally {
			if (inputStream != null) {
				inputStream.close();
				inputStream = null;
			}
			xml = null;
			sb = null;
			scheduleID = null;
			interval = null;
			action = null;
		}
	}
	
	public void SendRespMsg(HttpServletResponse res,TOLSubscriberCheckDTO tolReqDTO) 
	{
		BufferedWriter writer = null;
		StringBuilder resp=null;
		try
		{
		    writer = new BufferedWriter(new OutputStreamWriter(res.getOutputStream()));
		    if (tolReqDTO.getStatusDesc() == null) 
		    {
		    	throw new Exception("cannot send ack message");
		    }
		   // writer.write("<RSP><STATUS_CODE>"+respCode+"</STATUS_CODE><STATUS_DESC>"+respMsg+"</STATUS_DESC></RSP>");
		    if(tolReqDTO.getRequestId()==null)
		    {
		    	resp=new StringBuilder();
		    	resp.append("<Response>");
		    	resp.append("<ClientTxnId><![CDATA[NA]]></ClientTxnId>");
		    	resp.append("<Timestamp><![CDATA[NA]]></Timestamp>");
		    	resp.append("<Msisdn><![CDATA[NA]]></Msisdn>");
		    	resp.append("<RespCode><![CDATA["+tolReqDTO.getStatus()+"]]></RespCode>");
		    	resp.append("<RespDesc><![CDATA["+tolReqDTO.getStatusDesc()+"]]></RespDesc>");
		    	resp.append("</Response>");
		    	writer.write(resp.toString());
			    writer.flush();
			    logger.info("Response Send >>"+resp);
		    }
		    else 
		    {
		    	resp=new StringBuilder();
		    	resp.append("<Response>");
		    	resp.append("<ClientTxnId><![CDATA["+tolReqDTO.getRequestId()+"]]></ClientTxnId>");
		    	resp.append("<Timestamp><![CDATA["+tolReqDTO.getTimeStamp()+"]]></Timestamp>");
		    	resp.append("<Msisdn><![CDATA["+""+tolReqDTO.getSubscriberNumber()+"]]></Msisdn>");
		    	resp.append("<RespCode><![CDATA["+tolReqDTO.getStatus()+"]]></RespCode>");
		    	resp.append("<RespDesc><![CDATA["+tolReqDTO.getStatusDesc()+"]]></RespDesc>");
		    	resp.append("</Response>");
		    	writer.write(resp.toString());
			    writer.flush();
			    logger.info("Response Send >>"+resp);
		    }
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
			tolReqDTO=null;
			resp=null;
		}
	}

}
