/**
 * 
 */
package com.sixdee.lms.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.sixdee.imp.dto.Json.Data;
import com.sixdee.imp.dto.Json.RequestRealTimeTrigger;
import com.sixdee.imp.utill.DataSet;
import com.sixdee.imp.utill.Param;
import com.sixdee.imp.utill.Request;
import com.sixdee.lms.util.constant.SystemConstants;


/**
 * @author rahul.kr
 *
 */
public class DTOConverterUtil {

	private static SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
	public Request convertTriggerObjectToOnlineRuleObject(Object object) {
		Request requestHeader = null;
		if(object instanceof RequestRealTimeTrigger){
			requestHeader =converRealTimeTrigggerRequest((RequestRealTimeTrigger) object);
		}
		return requestHeader;
	}

	private Request converRealTimeTrigggerRequest(RequestRealTimeTrigger object) {
		Request requestHeader = null;
		DataSet requestDataset = null;
		ArrayList<Param> paramList = null;
		try{
			requestHeader = new Request();
			requestHeader.setRequestId(System.currentTimeMillis()+"");
			requestHeader.setMsisdn(object.getMsisdn());
			requestHeader.setKeyWord(object.getKeyword());
			requestHeader.setTimeStamp(sdf.format(new Date()));
			requestDataset = new DataSet();
			paramList = new ArrayList<>();
			if(object.getNotificationBox()!=null){
				for(Data data : object.getNotificationBox().getData()){
					paramList.add(new Param(data.getName(), data.getValue()));
				}
				if(object.getMsisdn()!=null){
					paramList.add(new Param(SystemConstants.MSISDN,object.getMsisdn()));
				}if(object.getAccountNumber()!=null){			
					paramList.add(new Param(SystemConstants.MSISDN,object.getMsisdn()));
				}
			}
			requestDataset.setParameter1(paramList);
			requestHeader.setDataSet(requestDataset);
		}finally{
			
		}
		return requestHeader;
	}
	

	
}
