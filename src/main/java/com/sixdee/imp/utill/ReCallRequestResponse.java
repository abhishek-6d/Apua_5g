package com.sixdee.imp.utill;

import java.util.ArrayList;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.sixdee.imp.common.config.Cache;

public class ReCallRequestResponse {

	private Logger log = LogManager.getLogger(ReCallRequestResponse.class);

	public Response sendRequestREandGetResponse(Request request1) throws Exception {
		// TODO Auto-generated method stub
		Response responseRe = null;
		Request request = null;
		DataSet dataSet = null;
		ArrayList<Param> params = null;
		try {
			request = new Request();
			request.setMsisdn(request1.getMsisdn());
			request.setRequestId(request1.getRequestId());
			request.setTimeStamp(request1.getTimeStamp());
			request.setKeyWord(request1.getKeyWord());
			if (request.getDataSet() != null) {
				dataSet = request1.getDataSet();
				params = new ArrayList<Param>();
				for (Param paramRe : dataSet.getParameter1()) {
					Param param = new Param();
					param.setId(paramRe.getId());
					param.setValue(paramRe.getValue());
					params.add(param);
				}

			}
			dataSet = new DataSet();
			dataSet.setParameter1(params);
			request.setDataSet(dataSet);

			String requestXml = RequestParseXML.getRequest().toXML(request);

			ResponseSender sender = new ResponseSender();
			String response = sender.sendResponse(Cache.getCacheMap().get("RULE_ENG_URL"), requestXml);

			log.info(">>>>RULE ENGINE response>>>>>" + response);
			responseRe = (Response) RequestParseXML.responseXstream().fromXML(response);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		return responseRe;
	}
}
