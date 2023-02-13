package com.sixdee.imp.service.httpcall;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dto.AlufuqSendCodesDTO;
import com.sixdee.imp.service.LMSWebServiceAdapter;
import com.sixdee.imp.service.httpcall.dto.DataSet;
import com.sixdee.imp.service.httpcall.dto.Parameters;
import com.sixdee.imp.service.httpcall.dto.Request;

public class AlufuqSendCodes extends HttpServlet {
	private Logger logger = Logger.getLogger(AlufuqSendCodes.class);

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		execute(req, resp);
	
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		execute(req, resp);
	}

	private void execute(HttpServletRequest req, HttpServletResponse resp) {
		String requestId=null;
		String msisdn=null;
		BufferedInputStream bis = null;
		String xml = null;
		StringBuilder sb = null;		
		AlufuqSendCodesDTO alufuqSendCodesDTO = null;
		GenericDTO genericDTO = null;
		LMSWebServiceAdapter adapter = null;
		try {
			bis = new BufferedInputStream(req.getInputStream(), 32 * 1024);
			int i;
			sb = new StringBuilder();
			while ((i = bis.read()) != -1) {
				sb.append((char) i);
			}

			xml = sb.toString();
			logger.info("Request => " + xml);
			if (xml != null && !xml.equalsIgnoreCase("")) {
				alufuqSendCodesDTO = validate(xml);
			} else {
				throw new CommonException("Request Xml is empty");
			}
			if (alufuqSendCodesDTO != null) {
				requestId = alufuqSendCodesDTO.getRequestId();
				msisdn = alufuqSendCodesDTO.getMsisdn();
			}

			logger.info("RequestId:" + requestId + "Msisdn:" + msisdn
					+ "Calling feature : AlufuqSendCodes ");
			genericDTO = new GenericDTO();
			genericDTO.setObj(alufuqSendCodesDTO);
			adapter = new LMSWebServiceAdapter();
			genericDTO = adapter.callFeature("AlufuqSendCodes",
					alufuqSendCodesDTO);
			SendRespMsg(resp, genericDTO.getStatusCode(),
					genericDTO.getStatus());

		} catch (CommonException e) {
			logger.error("RequestId:" + requestId
					+ "Msisdn:" + msisdn
					+"Exception occured:" + e);
			e.printStackTrace();
			SendRespMsg(resp, "SC0001", e.getMessage());
		} catch (Exception e) {
			logger.error("RequestId:" + requestId
					+ "Msisdn:" + msisdn
					+"Exception occured:" + e);
			e.printStackTrace();
			SendRespMsg(resp, "SC0001", "FAILED");
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			xml=null;
			sb=null;
			genericDTO=null;
			alufuqSendCodesDTO=null;
		}
	}

	private AlufuqSendCodesDTO validate(String xml) throws CommonException {
		Request request = null;
		String msisdn = null;
		AlufuqSendCodesDTO alufuqSendCodesDTO=null;
		try {
			request = (Request) Cache.coomonReqXstream.fromXML(xml);
			logger.info("RequestId:"+request.getRequestId()+"Xml parsing finished");
			DataSet dataSet = request.getDataSet();
			if(request.getRequestId()==null)
			{
				throw new CommonException("Request id not recieved");
			}
			if (dataSet != null) {
				for (Parameters param : dataSet.getParams()) {
					if (param.getId().equalsIgnoreCase("MSISDN")) {
						msisdn = param.getValue();

					}
				}
			}
			if (msisdn == null) {
				throw new CommonException("MSISDN TAG IS MISSING");
			}
			if(request!=null)
			{
				alufuqSendCodesDTO=new AlufuqSendCodesDTO();
				alufuqSendCodesDTO.setRequestId(request.getRequestId());
				alufuqSendCodesDTO.setTimeStamp(request.getTimeStamp());
				alufuqSendCodesDTO.setMsisdn(msisdn);
			}


		} catch (Exception e) {
			logger.error("Exception occoured" + e + "request xml is :" + xml);
			e.printStackTrace();
		}
		return alufuqSendCodesDTO;
	}

	private void SendRespMsg(HttpServletResponse res, String respCode,
			String respMsg) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					res.getOutputStream()));
			//res.setStatus(HttpServletResponse.);
			if (respMsg == null) {
				throw new Exception("cannot send ack message");
			}
			// writer.write("<RSP><RESULT>SUCCESS</RESULT><ERR><NO>0</NO><DESC>SUCCESSFULLY PROCESSED</DESC></ERR></RSP>");
			writer.write(respMsg);

			writer.flush();
		} catch (Exception exp) {
			logger.error("Exception Occured while Sending Response:::" + exp);
		} finally {
			if (writer != null) {
				try {
					writer.close();
					writer = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			respCode = null;
			respMsg = null;
		}
	}

}
