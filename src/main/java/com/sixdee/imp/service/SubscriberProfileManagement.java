package com.sixdee.imp.service;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.imp.service.serviceDTO.req.SubscriberProfileDTO;
import com.sixdee.imp.service.serviceDTO.resp.ResponseDTO;

public class SubscriberProfileManagement {

	public ResponseDTO validateSubscriber(SubscriberProfileDTO subscriberProfileDTO)
	{
		long start=System.currentTimeMillis();
		
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setTimestamp(subscriberProfileDTO.getTimestamp());
		responseDTO.setTranscationId(subscriberProfileDTO.getTransactionID());
		
		if(subscriberProfileDTO.getSubscriberNumber()==null||subscriberProfileDTO.getSubscriberNumber().trim().equalsIgnoreCase(""))
		{
			responseDTO.setStatusCode("SC1000");
			responseDTO.setStatusDescription("Subscriber Number is required");
			return responseDTO;
		}
		
		if(subscriberProfileDTO.getServiceName()==null||subscriberProfileDTO.getServiceName().trim().equalsIgnoreCase(""))
		{
			responseDTO.setStatusCode("SC1000");
			responseDTO.setStatusDescription("Service Name is required");
			return responseDTO;
		}
		
		
		
		LMSWebServiceAdapter adapter=new LMSWebServiceAdapter();
		GenericDTO genericDTO=(GenericDTO)adapter.callFeature("ValidateSubscriber",subscriberProfileDTO);
		
		if(genericDTO==null)
		{
			responseDTO.setStatusCode("SC1000");
			responseDTO.setStatusDescription("FAILURE");
		}
		
		else if(genericDTO.getStatusCode().equalsIgnoreCase("0"))
		{
			
			responseDTO.setStatusCode("SC0000");
			responseDTO.setStatusDescription("SUCCESS");
			
		}else if(genericDTO.getStatusCode().equalsIgnoreCase("1")&&genericDTO.getStatus()!=null&&!genericDTO.getStatus().trim().equalsIgnoreCase("")){
			responseDTO.setStatusCode("SC1000");
			responseDTO.setStatusDescription(genericDTO.getStatus());
		}else{
			responseDTO.setStatusCode("SC1000");
			responseDTO.setStatusDescription("FAILURE");
		}
		 long end=System.currentTimeMillis();
		 
		 System.out.println(end-start);
		
		return responseDTO;
	
		}
	
	
	public ResponseDTO deductBenefits(SubscriberProfileDTO subscriberProfileDTO)
	{
		long start=System.currentTimeMillis();
		
		
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setTimestamp(subscriberProfileDTO.getTimestamp());
		responseDTO.setTranscationId(subscriberProfileDTO.getTransactionID());
		
		if(subscriberProfileDTO.getSubscriberNumber()==null||subscriberProfileDTO.getSubscriberNumber().trim().equalsIgnoreCase(""))
		{
			responseDTO.setStatusCode("SC1000");
			responseDTO.setStatusDescription("Subscriber Number is required");
			return responseDTO;
		}
		
		if(subscriberProfileDTO.getServiceName()==null||subscriberProfileDTO.getServiceName().trim().equalsIgnoreCase(""))
		{
			responseDTO.setStatusCode("SC1000");
			responseDTO.setStatusDescription("Service Name is required");
			return responseDTO;
		}
		LMSWebServiceAdapter adapter=new LMSWebServiceAdapter();
		GenericDTO genericDTO=(GenericDTO)adapter.callFeature("FailureAction",subscriberProfileDTO);
		
		if(genericDTO==null)
		{
			responseDTO.setStatusCode("SC1000");
			responseDTO.setStatusDescription("FAILURE");
		}
		
		else if(genericDTO.getStatusCode().equalsIgnoreCase("0"))
		{
			
			responseDTO.setStatusCode("SC0000");
			responseDTO.setStatusDescription("SUCCESS");
			
		}else if(genericDTO.getStatusCode().equalsIgnoreCase("1")&&genericDTO.getStatus()!=null&&!genericDTO.getStatus().trim().equalsIgnoreCase("")){
			responseDTO.setStatusCode("SC1000");
			responseDTO.setStatusDescription(genericDTO.getStatus());
		}else{
			responseDTO.setStatusCode("SC1000");
			responseDTO.setStatusDescription("FAILURE");
		}
		 long end=System.currentTimeMillis();
		 
		 System.out.println(end-start);
		
		return responseDTO;
	}
	
}
