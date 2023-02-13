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
import com.sixdee.imp.dto.ChangeTierDTO;
import com.sixdee.imp.service.LMSWebServiceAdapter;
import com.sixdee.imp.service.httpcall.dto.DataSet;
import com.sixdee.imp.service.httpcall.dto.Parameters;
import com.sixdee.imp.service.httpcall.dto.Request;

public class ChangeTier extends HttpServlet {
	private Logger logger = Logger.getLogger(ChangeTier.class);

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
		BufferedInputStream bis = null;
		StringBuilder sb = null;
		String xml = null;

		LMSWebServiceAdapter adapter = null;
		String requestId = null;
		String msisdn = null;
		ChangeTierDTO changeTierDTO = null;
		GenericDTO genericDTO = null;

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
				changeTierDTO = validate(xml);

			} else {
				throw new CommonException("SC0001", "Request Xml is empty");
			}
			if (changeTierDTO != null) {
				requestId = changeTierDTO.getRequestId();
				msisdn = changeTierDTO.getMsisdn();
				logger.info("RequestId:" + requestId + " Msisdn:" + msisdn
						+ " TIER:" + changeTierDTO.getTier()
						+ " ISROYALFAMILY:" + changeTierDTO.getIsRoyalFamily()
						+ " calling feature:ChangeTier");

				genericDTO = new GenericDTO();
				genericDTO.setObj(changeTierDTO);

				adapter = new LMSWebServiceAdapter();
				genericDTO = adapter.callFeature("ChangeTier", changeTierDTO);
				// genericDTO=changeTierBL.buildProcess(genericDTO);
				SendRespMsg(resp, "SC0000", genericDTO.getStatus());

			}

		} catch (CommonException e) {
			logger.error("RequestId:" + requestId + " Msisdn:" + msisdn
					+ " Exception occured:" + e);
			e.printStackTrace();
			SendRespMsg(resp, "SC0001", e.getMessage());
		} catch (Exception e) {
			logger.error("RequestId:" + requestId + " Msisdn:" + msisdn
					+ " Exception occured:" + e);
			e.printStackTrace();
			SendRespMsg(resp, "SC0001", "FAILED");

		} finally {

			if (bis != null) {
				try {
					bis.close();
					bis = null;
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
			sb = null;
			changeTierDTO = null;
			adapter = null;
			genericDTO = null;
			xml = null;
			requestId = null;
			msisdn = null;
		}

	}

	private ChangeTierDTO validate(String xml) {
		ChangeTierDTO changeTierDTO = null;
		Request requestdto = null;
		try {
			requestdto = (Request) Cache.coomonReqXstream.fromXML(xml);
			if (requestdto != null) {
				changeTierDTO = new ChangeTierDTO();
				DataSet dataSet = requestdto.getDataSet();
				if (dataSet != null) {
					for (Parameters param : dataSet.getParams()) {
						if (param.getId().equalsIgnoreCase("TIER")) {
							changeTierDTO.setTier(param.getValue());

						} else if (param.getId().equalsIgnoreCase(
								"ISROYALFAMILY")) {
							changeTierDTO.setIsRoyalFamily(Integer
									.parseInt(param.getValue()));

						} else if (param.getId().equalsIgnoreCase("MSISDN")) {
							changeTierDTO.setMsisdn(param.getValue());
						}

					}
				}
			}
			changeTierDTO.setRequestId(requestdto.getRequestId());
			changeTierDTO.setTimeStamp(requestdto.getTimeStamp());
			logger.info("Parsing finished for " + changeTierDTO.getRequestId());
			changeTierDTO.setDataSet(requestdto.getDataSet());
			if (changeTierDTO.getTier() == null) {
				throw new CommonException("TIER tag is missing");
			}
			if (changeTierDTO.getIsRoyalFamily() == 3) {
				throw new CommonException("ISROYALFAMILY tag is missing");
			}
			if (changeTierDTO.getMsisdn() == null) {
				throw new CommonException("MSISDN param is missing");

			}
		} catch (Exception e) {
			logger.error("Exception occured" + e);
			e.printStackTrace();
		}
		return changeTierDTO;
	}

	public void SendRespMsg(HttpServletResponse res, String respCode,
			String respMsg) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					res.getOutputStream()));
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
					
					e.printStackTrace();
				}
			}
			respCode = null;
			respMsg = null;
		}
	}

}
