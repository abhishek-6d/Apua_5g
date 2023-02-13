package com.sixdee.imp.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sixdee.imp.utill.Request;
import com.sixdee.imp.utill.RequestParseXML;
import com.sixdee.imp.utill.Response;





public class ReAdapterServlet extends HttpServlet{

	private static final long serialVersionUID=1L;
	private static final Logger logger = Logger.getLogger(ReAdapterServlet.class);
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
	String reqXml =null;
	String responseXML="";
	String className =null;
	Response response=null;
    String featureID = null;
    Request request = null;
	try{
		inputStream= new BufferedInputStream(req.getInputStream(),32*1024);
		sb = new StringBuilder();
		int character =inputStream.read();
		while(character!=-1){
			sb.append((char)character);
			character =inputStream.read();
		}
		reqXml=sb.toString();
		 logger.info("REQ XML>>>>"+reqXml);
	    request = (Request) RequestParseXML.getRequest().fromXML(reqXml);
		featureID = request.getKeyWord();
		//className=Cache.getFeatureDeetailsMap().get(featureID);
		className= "com.sixdee.imp.service.ReServices.BL.TierAndBonusPointCalculationBL";
		ProcessExecute processExecute = (ProcessExecute)Class.forName(className).newInstance();
	//	response = processExecute.process(request);
		responseXML=RequestParseXML.responseXstream().toXML(response);
	}catch(Exception e){
		logger.info("Exception in execute "+e);
		e.printStackTrace();
		responseXML="<Response>  <ClientTxnId><![CDATA["+request.getRequestId()+"]]></ClientTxnId>   <Timestamp><![CDATA[" + System.currentTimeMillis() + "]]></Timestamp> " + "  <RespCode><![CDATA[SC1000]]></RespCode>  <RespDesc><![CDATA[Failure]]></RespDesc></Response>";
	}finally {
		if (inputStream != null) {
			try {
				inputStream.close();
				inputStream = null;
			} catch (Exception e) {
				inputStream = null;
				// TODO Auto-generated catch block
				logger.error(e);
			}
		}
		if (sb != null) {
			sb.setLength(0);
			sb = null;
		}
		sendResp(resp, responseXML);
		reqXml = null;
		response = null;
		featureID = null;
		request = null;
	}
	return resp;
}


private void sendResp(HttpServletResponse resp, String message) {
	OutputStreamWriter bufferWriter = null;
	try {
		logger.info(" : Response ==> " + message);
		bufferWriter = (new OutputStreamWriter(resp.getOutputStream()));
		if (message != null) {
			bufferWriter.write(message);
			bufferWriter.flush();
		}
	} catch (IOException e) {
		logger.error("Exception occured ",e);
	}finally{
		if(bufferWriter!=null){
			try {
				bufferWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//
	}

}



}
