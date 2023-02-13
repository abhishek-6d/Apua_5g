package com.sixdee.imp.service;

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

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dto.Json.Data;
import com.sixdee.imp.dto.Json.EIFResponseJson;
import com.sixdee.imp.dto.Json.NotificationBox;
import com.sixdee.imp.dto.Json.RequestRealTimeTrigger;
import com.sixdee.imp.utill.DataSet;
import com.sixdee.imp.utill.Param;
import com.sixdee.imp.utill.Request;
import com.sixdee.imp.utill.RequestParseXML;
import com.sixdee.imp.utill.Response;
import com.sixdee.imp.utill.ResponseSender;
import com.sixdee.lms.bo.NotificationRequestProcessBO;
import com.sixdee.lms.util.parser.JsonParser;




public class JsonAdapter extends HttpServlet{
	private static final long serialVersionUID=1L;
	private static final Logger logger = Logger.getLogger(JsonAdapter.class);
	public void doGet(HttpServletRequest req,HttpServletResponse resp ) throws IOException, ServletException{
		logger.info("**** in doGet ****");
	}
public void doPost(HttpServletRequest req,HttpServletResponse resp ) throws IOException, ServletException{
	logger.info("**** in doPost ****");
	execute(req,resp);
	}

public HttpServletResponse execute(HttpServletRequest req,HttpServletResponse resp){
	logger.info("**** in execute ****");
	InputStream inputStream =null;
	StringBuilder sb =null;
	
	resp.setContentType("text/html");
	
    JsonParser jsonParser = new JsonParser();
    RequestRealTimeTrigger requestRealTimeTrigger = null;
    EIFResponseJson eifResponseJson = null;
    String respMsg =null;
    NotificationRequestProcessBO eifRequestProcess = null;
    GenericDTO genericDTO = null;
	try{
		inputStream= new BufferedInputStream(req.getInputStream(),32*1024);
		sb = new StringBuilder();
		int character =inputStream.read();
		while(character!=-1){
			sb.append((char)character);
			character =inputStream.read();
		}
		String request = sb.toString();
		logger.info(">>>>req>>>"+sb);
		requestRealTimeTrigger	= (RequestRealTimeTrigger) jsonParser.fromJson(request,RequestRealTimeTrigger.class);
		
		if(requestRealTimeTrigger!=null){
			if((requestRealTimeTrigger.getMsisdn()!=null && !requestRealTimeTrigger.getMsisdn().equalsIgnoreCase("")) || 
					(requestRealTimeTrigger.getAccountNumber()!=null && !requestRealTimeTrigger.getAccountNumber().equalsIgnoreCase("")) ||
					(requestRealTimeTrigger.getCustomerRefernceNumber()!=null && !requestRealTimeTrigger.getCustomerRefernceNumber().equalsIgnoreCase(""))){
			
			eifResponseJson = new EIFResponseJson();
			eifResponseJson.setStatusCode("SC0000");
			eifResponseJson.setStatusDescription("SUCCESS");
			eifResponseJson.setTimeStamp(requestRealTimeTrigger.getTimeStamp());
			eifResponseJson.setTransactionId(requestRealTimeTrigger.getTransactionID());
			respMsg = jsonParser.toJson(eifResponseJson);
			
			SendRespMsg(resp, "SC0000", respMsg);
			
			eifRequestProcess = new NotificationRequestProcessBO();
			genericDTO = new GenericDTO();
			requestRealTimeTrigger.setMsisdn(requestRealTimeTrigger.getMsisdn().replaceFirst("968", ""));
			genericDTO.setObj(requestRealTimeTrigger);
			eifRequestProcess.executeBusinessProcess(genericDTO);
			Response response = (Response) genericDTO.getObj();
			
			//logger.info(">>>response>>>"+);
			//sendRequestRE(requestRealTimeTrigger);
			}else{
			   logger.info(">>>>no msisdn , crn and acc num>>>>>");
			   eifResponseJson = new EIFResponseJson();
				eifResponseJson.setStatusCode("SC0001");
				eifResponseJson.setStatusDescription("FAILURE");
				eifResponseJson.setTimeStamp("");
				eifResponseJson.setTransactionId("");
				respMsg = jsonParser.toJson(eifResponseJson);
				SendRespMsg(resp, "SC0001", respMsg);
			}
			
		}else{
			eifResponseJson = new EIFResponseJson();
			eifResponseJson.setStatusCode("SC0001");
			eifResponseJson.setStatusDescription("FAILURE");
			eifResponseJson.setTimeStamp("");
			eifResponseJson.setTransactionId("");
			respMsg = jsonParser.toJson(eifResponseJson);
			SendRespMsg(resp, "SC0001", respMsg);
		}
		
		inputStream.close();
		
		
	}catch(Exception e){
		logger.info("Exception in execute "+e);
		eifResponseJson = new EIFResponseJson();
		eifResponseJson.setStatusCode("SC0001");
		eifResponseJson.setStatusDescription("FAILURE");
		eifResponseJson.setTimeStamp("");
		eifResponseJson.setTransactionId("");
		respMsg = jsonParser.toJson(eifResponseJson);
		SendRespMsg(resp, "SC0001", respMsg);
		e.printStackTrace();
	}finally{

		
	}
	return resp;
}



private void sendRequestRE(RequestRealTimeTrigger requestRealTimeTrigger) throws Exception {
	// TODO Auto-generated method stub
	Request request =null;
	Response responseRe = null;
	DataSet dataSet = null;
	NotificationBox notificationBox = null;
	ArrayList<Param> params= null;
	try{
		request = new Request();
		request.setMsisdn(requestRealTimeTrigger.getMsisdn());
		request.setRequestId(requestRealTimeTrigger.getTransactionID());
		request.setTimeStamp(requestRealTimeTrigger.getTimeStamp());
		request.setKeyWord(requestRealTimeTrigger.getKeyword());
		if(requestRealTimeTrigger.getNotificationBox()!=null){
			notificationBox = requestRealTimeTrigger.getNotificationBox();
			params = new  ArrayList<Param>();
			for(Data data: notificationBox.getData()){
				Param param = new Param();
				param.setId(data.getName());
				param.setValue(data.getValue());
				
				params.add(param);
			}
		}
			Param param = new Param();
			param.setId("channel");
			param.setValue(requestRealTimeTrigger.getChannel());
			
			params.add(param);
			
			 param = new Param();
			param.setId("msisdn");
			param.setValue(requestRealTimeTrigger.getMsisdn());
			
			params.add(param);
			
			 param = new Param();
			param.setId("fdn");
			param.setValue(requestRealTimeTrigger.getFdn());
			
			params.add(param);
			
			 param = new Param();
				param.setId("accountNumber");
				param.setValue(requestRealTimeTrigger.getAccountNumber());
				
				params.add(param);
			
		
		dataSet = new DataSet();
		dataSet.setParameter1(params);
		request.setDataSet(dataSet);
		
		String requestXml = RequestParseXML.getRequest().toXML(request);
		// calling rule engine
		ResponseSender sender = new ResponseSender();
		String response = sender.sendResponse(Cache.getConfigParameterMap().get("RULE_ENG_URL_EIF_REQ").getParameterValue(), requestXml);
		
		logger.info(">>>>RULE ENGINE response>>>>>"+response);
		responseRe = (Response) RequestParseXML.responseXstream().fromXML(response);
	}catch(Exception e){
		e.printStackTrace();
		throw new Exception(e.getMessage());
	}
		
		
}

public void SendRespMsg(HttpServletResponse res, String respCode,String respMsg) 

{
	
	logger.info(">>>>SendRespMsg>>>"+respMsg);
	BufferedWriter writer = null;
	try
	{
	    writer = new BufferedWriter(new OutputStreamWriter(res.getOutputStream()));
	    if (respMsg == null) 
	    {
	    	res.setHeader("4xx", "");
	    	
	    	throw new Exception("cannot send ack message");
	    }
	   // res.setStatus(535);
	    writer.write(respMsg);
	    writer.flush();
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
	    respCode = null;
	    respMsg =null;
	}
}

}
