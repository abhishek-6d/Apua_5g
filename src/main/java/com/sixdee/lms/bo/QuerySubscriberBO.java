package com.sixdee.lms.bo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dto.parser.RERequestHeader;
import com.sixdee.imp.dto.parser.REResponseDataSet;
import com.sixdee.imp.dto.parser.REResponseHeader;
import com.sixdee.imp.dto.parser.ReResponseParameter;
import com.sixdee.imp.utill.Param;
import com.sixdee.lms.dto.QuerySubscriber.DataSetQs;
import com.sixdee.lms.dto.QuerySubscriber.QuerySubscriberReq;
import com.sixdee.lms.dto.QuerySubscriber.QuerySubscriberResp;
import com.sixdee.lms.dto.QuerySubscriber.Request;
import com.sixdee.lms.serviceInterfaces.BusinessLogics;
import com.sixdee.lms.thirdPartyCall.CallThirdParty;
import com.sixdee.lms.util.ResponseHeaderUtil;
import com.sixdee.lms.util.constant.SystemConstants;
import com.sixdee.lms.util.parser.JsonParser;

public class QuerySubscriberBO implements BusinessLogics{
	private static final Logger logger = Logger.getLogger("QuerySubscriberBO");
	@Override
	public GenericDTO executeBusinessProcess(GenericDTO genericDTO) {
		String requestId = null;
		RERequestHeader requestHeader = null;
		REResponseHeader reResponseHeader = null;
		REResponseDataSet responseDataSet = null;
		QuerySubscriberReq querySubscriberReq = null;
		ResponseHeaderUtil responseHeaderUtil = new ResponseHeaderUtil();;
		Request request = null;
		CallThirdParty callThirdParty= null;
		QuerySubscriberResp querySubscriberResp = null;
		try{
			requestId = genericDTO.getRequestId();
			requestHeader = (RERequestHeader) genericDTO.getObj();
			
			querySubscriberReq	= generateReqJson(requestHeader);
			request = new Request();
			request.setRequest(querySubscriberReq);
			String req = JsonParser.toJson(request);
			
			logger.info("request>>>>>"+req);
			callThirdParty = new CallThirdParty();
			String respXML=callThirdParty.makeThirdPartyCall(req, Cache.getCacheMap().get("QUERY_SUBSCRIBER_SERVICE"), 5000);
			logger.info("Transaction id    "+requestId+"response>>>>>"+respXML);
			
			ArrayList<ReResponseParameter> parameterList  = generateParamlist(respXML);
			responseDataSet = new REResponseDataSet();
			responseDataSet.setParameterList(parameterList);
			reResponseHeader = new REResponseHeader();
			reResponseHeader = responseHeaderUtil.getResponseHeader(requestHeader, responseDataSet);
			
		}catch(Exception e){
			logger.error("Exception occured ",e);
		}finally{
			genericDTO.setObj(reResponseHeader);
		}
		return genericDTO;
	}

	private ArrayList<ReResponseParameter> generateParamlist(String respXML) {
		 JSONParser jsonParser = new JSONParser();
		 ArrayList<ReResponseParameter> reResponseParameters = null;
		try {
			ReResponseParameter parameter = null;
			
		     JSONObject josnObject = (JSONObject) jsonParser.parse(respXML);
		     System.out.println("Response : "+josnObject.get("Response"));
		     
		     JSONObject json = (JSONObject) josnObject.get("Response");
		     System.out.println("requestId : "+json.get("requestId"));
		     JSONObject obj = (JSONObject) json.get("dataSet");
		     if(obj != null){
		       JSONArray jsonArray = (JSONArray) obj.get("param");
		        if(jsonArray!=null){
		     for(int i=0;i<jsonArray.size();i++){
		      //System.out.println("para : "+jsonArray.get(i));
		      JSONObject temp = (JSONObject) jsonArray.get(i);
		      //System.out.println("id : "+temp.get("id"));
		      //System.out.println("value : "+temp.get("value"));
		      if(String.valueOf(temp.get("id")).equalsIgnoreCase("Subscribers")){
		       JSONArray temp1 = (JSONArray) temp.get("value");
		       for(int k=0;k<temp1.size();k++){
		    	   JSONArray array = (JSONArray) temp1.get(k);
		    	   for(int j=0;j<array.size();j++){
		              JSONObject temp2 = (JSONObject) array.get(j);
		       
		        if(String.valueOf(temp2.get("id")).equalsIgnoreCase("subscriber_parent")){
		         System.out.println("Temp Id : "+temp2.get("id").toString());
		         System.out.println("Temp Value : "+temp2.get("value"));
		         parameter = new ReResponseParameter();
		         parameter.setId(temp2.get("id").toString());
		         parameter.setValue(temp2.get("value").toString());
		         if(reResponseParameters==null)
		        	 reResponseParameters = new ArrayList<ReResponseParameter>();	
		         reResponseParameters.add(parameter);
		        }
		    	   }
		       }
		       
		      }
		      if(String.valueOf(temp.get("id")).equalsIgnoreCase("Accounts")){
		    	  JSONArray temp1 = (JSONArray) temp.get("value");
			       for(int k=0;k<temp1.size();k++){
			    	   JSONArray array = (JSONArray) temp1.get(k);
			    	   for(int j=0;j<array.size();j++){
			              JSONObject temp2 = (JSONObject) array.get(j);
			       
			        if(String.valueOf(temp2.get("id")).equalsIgnoreCase("subscriber_parent")){
			         System.out.println("Temp Id : "+temp2.get("id").toString());
			         System.out.println("Temp Value : "+temp2.get("value"));
			         parameter = new ReResponseParameter();
			         parameter.setId(temp2.get("id").toString());
			         parameter.setValue(temp2.get("value").toString());
			         if(reResponseParameters==null)
			        	 reResponseParameters = new ArrayList<ReResponseParameter>();	
			         reResponseParameters.add(parameter);
			        }
			    	   }
			       }
			       
			      }
		      if(String.valueOf(temp.get("id")).equalsIgnoreCase("Plans")){
		    	  JSONArray temp1 = (JSONArray) temp.get("value");
			       for(int k=0;k<temp1.size();k++){
			    	   JSONArray array = (JSONArray) temp1.get(k);
			    	   for(int j=0;j<array.size();j++){
			              JSONObject temp2 = (JSONObject) array.get(j);
			       
			        if(String.valueOf(temp2.get("id")).equalsIgnoreCase("subscriber_parent")){
			         System.out.println("Temp Id : "+temp2.get("id").toString());
			         System.out.println("Temp Value : "+temp2.get("value"));
			         parameter = new ReResponseParameter();
			         parameter.setId(temp2.get("id").toString());
			         parameter.setValue(temp2.get("value").toString());
			         if(reResponseParameters==null)
			        	 reResponseParameters = new ArrayList<ReResponseParameter>();	
			         reResponseParameters.add(parameter);
			        }
			    	   }
			       }
			       
			      }
		    
		     }
		     if(reResponseParameters==null)
	        	 reResponseParameters = new ArrayList<ReResponseParameter>();	
		     		reResponseParameters.add(new ReResponseParameter(SystemConstants.RESP_CODE, "SC0000"));
		     		reResponseParameters.add(new ReResponseParameter(SystemConstants.RESP_MESSAGE, "Success"));
		        }else{
		        	 if(reResponseParameters==null)
			        	 reResponseParameters = new ArrayList<ReResponseParameter>();	
				     reResponseParameters.add(new ReResponseParameter(SystemConstants.RESP_CODE, "SC0001"));
				     reResponseParameters.add(new ReResponseParameter(SystemConstants.RESP_MESSAGE, "Failure"));
		        }
		     }else{
		    	 if(reResponseParameters==null)
		        	 reResponseParameters = new ArrayList<ReResponseParameter>();	
			     reResponseParameters.add(new ReResponseParameter(SystemConstants.RESP_CODE, "SC0001"));
			     reResponseParameters.add(new ReResponseParameter(SystemConstants.RESP_MESSAGE, "Failure"));
		     }
		    
		    } catch (org.json.simple.parser.ParseException e) {
		     // TODO Auto-generated catch block
		     e.printStackTrace();
		     if(reResponseParameters==null)
	        	 reResponseParameters = new ArrayList<ReResponseParameter>();	
		     reResponseParameters.add(new ReResponseParameter(SystemConstants.RESP_CODE, "SC0001"));
		     reResponseParameters.add(new ReResponseParameter(SystemConstants.RESP_MESSAGE, "Failure"));
		    }
		logger.info("size >>>>"+reResponseParameters.size());
		return reResponseParameters;
	}

	private QuerySubscriberReq generateReqJson(RERequestHeader requestHeader) {
		QuerySubscriberReq querySubscriberReq = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Param[] params = null;
		try{
			querySubscriberReq = new QuerySubscriberReq();
			querySubscriberReq.setRequestId(requestHeader.getRequestId());
			if(requestHeader.getDataSet()!=null){
				ArrayList<ReResponseParameter> paramlist = requestHeader.getDataSet().getResponseParam();
				for(ReResponseParameter p : paramlist){
					if(p.getId().equalsIgnoreCase("FEATURE"))
						querySubscriberReq.setFeature(p.getValue());
					if(p.getId().equalsIgnoreCase("ACTION"))
						querySubscriberReq.setAction(p.getValue());
					if(p.getId().equalsIgnoreCase("SOURCE_NODE"))
						querySubscriberReq.setSourceNode(p.getValue());
				}
				querySubscriberReq.setTimeStamp(df.format(new Date()));
				DataSetQs dataSetQs = null;
				if(requestHeader.getMsisdn()!=null)
				{
					dataSetQs = new DataSetQs();
					Param param = new Param();
					param.setId("account_id");
					param.setValue(requestHeader.getMsisdn());
					params = new Param[1];
					params[0] = param;
					dataSetQs.setParam(params);
				}
				querySubscriberReq.setDataset(dataSetQs);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return querySubscriberReq;
	}
public static void main(String args[]){
    String respXML = "{\"Response\":{\"requestId\":\"201607291848021001\",\"requestTimestamp\":\"2016-08-0118:05:04\",\"responseId\":\"201607291848021001\",\"responseTimestamp\":\"29/07/201618:48:02\",\"sourceNode\":\"CRM\",\"resultCode\":\"0\",\"dataSet\":{\"param\":[{\"id\":\"Result-Code\",\"value\":\"SC0000\"},{\"id\":\"Result-Description\",\"value\":\"FetchSubscriberSuccessfully\"},{\"id\":\"Subscribers\",\"value\":[[{\"id\":\"TotalPrimaryOC\",\"value\":\"0\"},{\"id\":\"SIMState\",\"value\":\"A\"},{\"id\":\"PrimaryBalance\",\"value\":\"100.0000\"},{\"id\":\"currency\",\"value\":\"100.0000\"},{\"id\":\"creationDate\",\"value\":\"2016-10-0522:53:54\"},{\"id\":\"language\",\"value\":\"HINDI\"},{\"id\":\"planId\",\"value\":\"1000\"},{\"id\":\"planName\",\"value\":\"PrepaidBasePlan\"},{\"id\":\"ocExpiryDate\",\"value\":\"2020-10-0123:59:59\"},{\"id\":\"imsi\",\"value\":\"86537125634902\"},{\"id\":\"firstCallDate\",\"value\":\"2016-10-0122:49:31\"},{\"id\":\"areaName\",\"value\":\"0\"},{\"id\":\"pcnAllowed\",\"value\":\"true\"},{\"id\":\"hzAllowed\",\"value\":\"true\"},{\"id\":\"moAllowed\",\"value\":\"true\"},{\"id\":\"mtAllowed\",\"value\":\"true\"},{\"id\":\"dataAllowed\",\"value\":\"true\"},{\"id\":\"smsmoAllowed\",\"value\":\"true\"},{\"id\":\"smsmtAllowed\",\"value\":\"true\"},{\"id\":\"smsmoAllowed\",\"value\":\"true\"},{\"id\":\"nationalRoamingAllowed\",\"value\":\"true\"},{\"id\":\"internationalRoamingAllowed\",\"value\":\"true\"},{\"id\":\"stdAllowed\",\"value\":\"true\"},{\"id\":\"isdAllowed\",\"value\":\"true\"},{\"id\":\"fnfAllowed\",\"value\":\"false\"},{\"id\":\"cugEnabled\",\"value\":\"false\"},{\"id\":\"counterChangeFnFnoForFree\",\"value\":\"0\"},{\"id\":\"instanceId\",\"value\":\"1\"},{\"id\":\"rechargeFailureCount\",\"value\":\"0\"},{\"id\":\"totalRechargeCount\",\"value\":\"0\"},{\"id\":\"mvnoId\",\"value\":\"100\"},{\"id\":\"subscriberType\",\"value\":\"0\"}]]},{\"id\":\"Accounts\",\"value\":[[{\"id\":\"accountId\",\"value\":\"MA\"},{\"id\":\"accountName\",\"value\":\"MainAccount\"},{\"id\":\"accountStartDate\",\"value\":\"2016-10-0522:53:54\"},{\"id\":\"accountExpiryDate\",\"value\":\"2017-10-0523:59:59\"},{\"id\":\"accountBalance\",\"value\":\"1880.00\"},{\"id\":\"accountUnitType\",\"value\":\"MONEY\"},{\"id\":\"accountType\",\"value\":\"MainAccount\"}]]},{\"id\":\"Plans\",\"value\":[[{\"id\":\"planId\",\"value\":\"1004\"},{\"id\":\"planName\",\"value\":\"STVPlan\"},{\"id\":\"planStartDate\",\"value\":\"2016-10-0401:55:07\"},{\"id\":\"planExpiryDate\",\"value\":\"2016-12-0323:59:59\"},{\"id\":\"planServiceId\",\"value\":\"11\"},{\"id\":\"planServiceName\",\"value\":\"FNF\"}]]}]}}}";
    JSONParser jsonParser = new JSONParser();
    
    
    try {
     JSONObject josnObject = (JSONObject) jsonParser.parse(respXML);
     
     System.out.println("Response : "+josnObject.get("Response"));
     
     JSONObject json = (JSONObject) josnObject.get("Response");
     System.out.println("requestId : "+json.get("requestId"));
     JSONObject obj = (JSONObject) json.get("dataSet");
     JSONArray jsonArray = (JSONArray) obj.get("param");
     
     for(int i=0;i<jsonArray.size();i++){
      //System.out.println("para : "+jsonArray.get(i));
      JSONObject temp = (JSONObject) jsonArray.get(i);
      //System.out.println("id : "+temp.get("id"));
      //System.out.println("value : "+temp.get("value"));
      if(String.valueOf(temp.get("id")).equalsIgnoreCase("Subscribers")){
    	  JSONArray temp1 = (JSONArray) temp.get("value");
	       for(int k=0;k<temp1.size();k++){
	    	   JSONArray array = (JSONArray) temp1.get(k);
	    	   for(int j=0;j<array.size();j++){
	              JSONObject temp2 = (JSONObject) array.get(j);
	              System.out.println("Temp Id : "+temp2.get("id").toString());
        if(String.valueOf(temp2.get("id")).equalsIgnoreCase("accountBalance")){
         System.out.println("Temp Id : "+temp2.get("id").toString());
         System.out.println("Temp Value : "+temp2.get("value").toString());
        }
        
       }
	       }
      }
      if(String.valueOf(temp.get("id")).equalsIgnoreCase("Accounts")){
    	  JSONArray temp1 = (JSONArray) temp.get("value");
	       for(int k=0;k<temp1.size();k++){
	    	   JSONArray array = (JSONArray) temp1.get(k);
	    	   for(int j=0;j<array.size();j++){
	              JSONObject temp2 = (JSONObject) array.get(j);
	              System.out.println("Temp Id : "+temp2.get("id").toString());
        if(String.valueOf(temp2.get("id")).equalsIgnoreCase("accountBalance")){
         System.out.println("Temp Id : "+temp2.get("id").toString());
         System.out.println("Temp Value : "+temp2.get("value").toString());
        }
       }
	       }
      }
      if(String.valueOf(temp.get("id")).equalsIgnoreCase("Plans")){
    	  JSONArray temp1 = (JSONArray) temp.get("value");
	       for(int k=0;k<temp1.size();k++){
	    	   JSONArray array = (JSONArray) temp1.get(k);
	    	   for(int j=0;j<array.size();j++){
	              JSONObject temp2 = (JSONObject) array.get(j);
	              System.out.println("Temp Id : "+temp2.get("id").toString());
        if(String.valueOf(temp2.get("id")).equalsIgnoreCase("accountBalance")){
         System.out.println("Temp Id : "+temp2.get("id").toString());
         System.out.println("Temp Value : "+temp2.get("value").toString());
        }
       }
	       }
      }
      
     }
     
    
    } catch (org.json.simple.parser.ParseException e) {
     // TODO Auto-generated catch block
     e.printStackTrace();
    }
}
}
