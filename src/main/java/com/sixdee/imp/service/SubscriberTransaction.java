package com.sixdee.imp.service;

import com.sixdee.imp.service.serviceDTO.req.SubscriberTransactionDTO;
import com.sixdee.imp.service.serviceDTO.resp.ResponseDTO;

public class SubscriberTransaction 
{

	public ResponseDTO insertTransaction(SubscriberTransactionDTO SubsTransDTO)
	{
		
		LMSWebServiceAdapter adapter = new LMSWebServiceAdapter();
		ResponseDTO responseDTO = null;
		
		try
		{	
			/*GenericDTO dto  = adapter.insertSubsTrans("SUBSCRIBER_TRANSACTION_HISTORY", SubsTransDTO);
			responseDTO = (ResponseDTO)dto.getObj();
			responseDTO.setStatusCode("SC0000");
			responseDTO.setStatusDescription("SUCCESS");
			responseDTO.setTranscationId(SubsTransDTO.getTransactionId());
			responseDTO.setTimestamp("16042013162344");
			System.out.println("here we are printing check response");*/
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return responseDTO;
	}
	
	
	

}
