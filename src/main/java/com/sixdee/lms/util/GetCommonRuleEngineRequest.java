package com.sixdee.lms.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import org.apache.log4j.Logger;
import com.sixdee.imp.utill.DataSet;
import com.sixdee.imp.utill.Param;
import com.sixdee.imp.utill.Request;
import com.sixdee.imp.utill.RequestParseXML;

public class GetCommonRuleEngineRequest {
	
	private static final Logger logger = Logger.getLogger(GetCommonRuleEngineRequest.class);

	public Request getRuleRequestParametersConversion(Request request, HashMap<String, String> parameters) {
		//request = new Request();
		String requestXml = null;
		DataSet dataSet = null;
		ArrayList<Param> reParameters = null;
		try {
			dataSet = new DataSet();
			reParameters = new ArrayList<>();
			for (Entry<String, String> paramEntry : parameters.entrySet()) {
				reParameters.add(new Param(paramEntry.getKey(), paramEntry.getValue()));
			}
			dataSet.setParameter1(reParameters);
			request.setDataSet(dataSet);
			requestXml = RequestParseXML.getRequest().toXML(request);
			logger.info("LMS BL to Provisioning Request XML " + requestXml);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
		}finally
		{
			requestXml=null;
		}
		return request;
	}

	public Request getRuleRequest(String transactionId, String timeStamp, String keyWord ,String msisdn) {
		Request request = null;
		try {
			request = new Request();
			request.setRequestId(transactionId);
			request.setTimeStamp(timeStamp);
			request.setKeyWord(keyWord);
			request.setMsisdn(msisdn);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
		}
		return request;
	}
}
