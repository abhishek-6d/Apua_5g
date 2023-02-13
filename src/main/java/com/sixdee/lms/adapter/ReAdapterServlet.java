package com.sixdee.lms.adapter;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.imp.dto.parser.RERequestHeader;
import com.sixdee.imp.dto.parser.REResponseHeader;
import com.sixdee.imp.util.parser.ReRequestParser;
import com.sixdee.imp.util.parser.ReResponseParser;
import com.sixdee.lms.serviceInterfaces.BusinessLogics;
import com.sixdee.lms.util.REConstant;


public class ReAdapterServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(ReAdapterServlet.class);

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		logger.info("**** in doGet ****");
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		logger.info("**** in doPost ****");
		execute(req, resp);
	}

	public HttpServletResponse execute(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("**** in execute ****");
		InputStream inputStream = null;
		StringBuilder sb = null;
		String reqXml = null;
		String responseXML = "";
		REResponseHeader response = null;
		String featureID = null;
		RERequestHeader request = null;
		REConstant reConstant = null;
		GenericDTO genericDTO = null;
		try {
			inputStream = new BufferedInputStream(req.getInputStream(), 32 * 1024);
			sb = new StringBuilder();
			int character = inputStream.read();
			while (character != -1) {
				sb.append((char) character);
				character = inputStream.read();
			}
			reqXml = sb.toString();
			logger.debug("Request XML "+reqXml);
			request = (RERequestHeader) ReRequestParser.getInstanceReqStream().fromXML(reqXml);
			featureID = request.getKeyWord();
			if((reConstant=REConstant.valueOf(featureID))!=null){
				genericDTO = new GenericDTO();
				genericDTO.setNextFeatureId(featureID);
			
				genericDTO.setRequestId(request.getRequestId());
				genericDTO.setObj(request);
				//request.setKeyword(request.getKeyword());
				request.setEsbRequestType(reConstant.getEsbRequestType());
				genericDTO = ((BusinessLogics)Class.forName(reConstant.getClassName()).newInstance()).executeBusinessProcess(genericDTO);//buildBusinessProcess(genericDTO);
			}
			response = (REResponseHeader) genericDTO.getObj();
			responseXML = ReResponseParser.getInstanceReqStream().toXML(response);
			
		} catch (Exception e) {
			logger.info("Exception in execute " , e);
			responseXML = "<Response>  <ClientTxnId><![CDATA[" + request.getRequestId()
					+ "]]></ClientTxnId>   <Timestamp><![CDATA[" + System.currentTimeMillis() + "]]></Timestamp> "
					+ "  <RespCode><![CDATA[SC1000]]></RespCode>  <RespDesc><![CDATA[Failure]]></RespDesc></Response>";
		} finally {
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
			reConstant = null;
			genericDTO = null;
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
			logger.error("Exception occured ", e);
		} finally {
			if (bufferWriter != null) {
				try {
					bufferWriter.close();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				bufferWriter=null;
			}
			//
		}

	}

}
