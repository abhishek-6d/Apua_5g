package com.sixdee.service.impl;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.imp.bo.GetTransactionDetailsBO;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.dao.LanguageDAO;
import com.sixdee.imp.common.dao.SubscriberListCheckDAO;
import com.sixdee.imp.common.util.CommonUtil;
import com.sixdee.imp.dto.GetTransactionDetailsDTO;
import com.sixdee.imp.dto.ResponseTransactionHistoryDTO;
import com.sixdee.imp.dto.TransactionResponse;
import com.sixdee.imp.dto.TransactionResponseDTO;
import com.sixdee.imp.service.LMSWebServiceAdapter;
import com.sixdee.imp.service.serviceDTO.req.TransactionDTO;
import com.sixdee.service.TransactionManagementService;

public class TransactionManagementServiceImpl implements TransactionManagementService{
	private Logger logger=LogManager.getLogger(TransactionManagementServiceImpl.class);
	public List<String> getTransactionDetails(String requestId, String language, String channel,
			String phoneNumber,HttpServletResponse servletResponse, String startDate, String endDate, Integer start, Integer limit, String transactionType,
			String loyaltyId)
	{
		
		TransactionResponseDTO infoDTO=new TransactionResponseDTO();
		CommonUtil commonUtil=new CommonUtil();
		LanguageDAO languageDAO=new LanguageDAO();
		SubscriberListCheckDAO subslistDAO=new SubscriberListCheckDAO();
		boolean check=false;
		String response = null;
		boolean flag = false;
		Gson gson = new Gson();
		List<String> responseList = null;
		LMSWebServiceAdapter adapter =null;
		ResponseTransactionHistoryDTO historyDTO=null;
		TransactionResponse transactionDetailsDTO2=null;
		GetTransactionDetailsDTO transactionDetailsDTO=null;
		String lanID=null;
		List<ResponseTransactionHistoryDTO> list=null;
		String txnId = null;
		GenericDTO genericDTO=null;
		TransactionResponse[] detailsDTO=null;
		long t1 = System.currentTimeMillis();
		GetTransactionDetailsDTO transactionDTO=new GetTransactionDetailsDTO();
		GetTransactionDetailsBO getTransactionDetailsBO=null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
		Date startdate = null;
		Date enddate = null;
		try
		{
			getTransactionDetailsBO=new GetTransactionDetailsBO();
			genericDTO=new GenericDTO();
		logger.info("SERVICE : GetTransaction History -- Transaction ID :- ["+txnId+"] MSISDN :- ["+transactionDTO.getSubscriberNumber()+"]  Request Recieved");
		
		
		if((phoneNumber != null)&&!(phoneNumber.equalsIgnoreCase("")) && (requestId != null)&&!(requestId.equalsIgnoreCase("")) && (channel!=null)&&!(channel.equalsIgnoreCase(""))) {
			
		
		
			transactionDTO.setMsisdn(phoneNumber);
			transactionDTO.setLangId(language);
			transactionDTO.setChannel(channel);
			transactionDTO.setStatusId(transactionType);
			transactionDTO.setLoyaltyId(loyaltyId);		
			transactionDTO.setSubscriberNumber(phoneNumber);
			transactionDTO.setTransactionId(requestId);
			if(startDate !=null && !startDate.equalsIgnoreCase("")) {
				startdate = sdf.parse(startDate);
				startDate = sdf1.format(startdate);
			}
			if(endDate !=null && !endDate.equalsIgnoreCase("")) {
				enddate = sdf.parse(endDate);
				endDate = sdf1.format(enddate);
			}
			transactionDTO.setFromDate(startDate);
			transactionDTO.setEndDate(endDate);
			if(start>0) {
				transactionDTO.setOffset(start);
			}else {
				transactionDTO.setOffset(0);
			}
			transactionDTO.setLimit(limit);
		
		
		
		
		
		
		
	
		if(transactionDTO.getLangId()==null||transactionDTO.getLangId().trim().equals("")) {
			lanID=Cache.cacheMap.get("DEFAULT_LANGUAGE_ID");
			transactionDTO.setLangId(lanID);
		}
			  
		
		
		
		
		genericDTO.setObj(transactionDTO);
		genericDTO=getTransactionDetailsBO.buildProcess(genericDTO);
		transactionDetailsDTO=(GetTransactionDetailsDTO)genericDTO.getObj();
		
		infoDTO.setTranscationId(txnId);
		
	//	transactionDTO=null;
		
		
		logger.info("**ResponseCode**:"+transactionDetailsDTO.getStatusCode());
		if(transactionDetailsDTO.getStatusCode().equalsIgnoreCase("SC0000") || transactionDetailsDTO.getStatusCode().equalsIgnoreCase("0"))
		{
			
			list=(List<ResponseTransactionHistoryDTO>)transactionDetailsDTO.getObj();
			infoDTO.setCode(genericDTO.getStatusCode());
			infoDTO.setReason(genericDTO.getStatus());
			servletResponse.setIntHeader("X_TOTAL_COUNT", transactionDetailsDTO.getRowCount());
			servletResponse.setIntHeader("X_RESULT_COUNT", list.size());
			
			
		
			
			
			detailsDTO=null;
			if(list!=null)
			{
				detailsDTO=new TransactionResponse[list.size()];
			
			for(int i=0;i<list.size();i++)
			{
				historyDTO=list.get(i);
				transactionDetailsDTO2=new TransactionResponse();
				transactionDetailsDTO2.setDate(historyDTO.getDate());
				transactionDetailsDTO2.setAccountLineNumber(historyDTO.getSubscriberNumber());
				transactionDetailsDTO2.setPoints(String.valueOf(historyDTO.getRewardPoint()));
				transactionDetailsDTO2.setType(historyDTO.getType());
				if(historyDTO.getExpiryDate()!=null)
				transactionDetailsDTO2.setExpiryDate(String.valueOf(historyDTO.getExpiryDate()));
				if(historyDTO.getExpiryPoints()!=null)
				transactionDetailsDTO2.setExpiryPoints(historyDTO.getExpiryPoints());
			    transactionDetailsDTO2.setActivity(historyDTO.getActivity());
				detailsDTO[i]=transactionDetailsDTO2;
				transactionDetailsDTO2=null;
				historyDTO=null;
			}
			
			
			}
			
			servletResponse.setHeader("response_code", "1");
			
			
			 flag = true;
		}else 
		{
			
			transactionDetailsDTO=(GetTransactionDetailsDTO)genericDTO.getObj();
			
			infoDTO.setCode(transactionDetailsDTO.getStatusCode());
			infoDTO.setReason(transactionDetailsDTO.getStatusDesc());
			infoDTO.setTotalcount("0");
			
			
		}
		/*detailsDTO=null;
		
		if(list!=null)
		{
			detailsDTO=new TransactionDetailsDTO[list.size()];
		
		for(int i=0;i<list.size();i++)
		{
			historyDTO=list.get(i);
			transactionDetailsDTO2=new TransactionDetailsDTO();
			transactionDetailsDTO2.setDate(historyDTO.getDate());
			transactionDetailsDTO2.setAccountLineNumber(historyDTO.getSubscriberNumber());
			transactionDetailsDTO2.setLoyaltyPoints((int)historyDTO.getRewardPoint());
			transactionDetailsDTO2.setType(historyDTO.getType());
			//String re = "[^\\x09\\x0A\\x0D\\x20-\\xD7FF\\xE000-\\xFFFD\\x10000-x10FFFF]";
		    transactionDetailsDTO2.setActivity(historyDTO.getActivity());
			detailsDTO[i]=transactionDetailsDTO2;
			transactionDetailsDTO2=null;
			historyDTO=null;
		}
		
	logger.info("No Of Transcation Giving back is ["+list.size()+"] Row Count "+transactionDetailsDTO.getRowCount()+detailsDTO+"]");
		//}
		infoDTO.setTransactionDetails(detailsDTO);
		
		infoDTO.setStatusCode("SC0000");
		infoDTO.setStatusDescription("Shared details for loyalty");
		}*/
		}else {
			infoDTO = new TransactionResponseDTO();
			infoDTO.setTranscationId(txnId);
			infoDTO.setMessage("Required parameter not passing");
			infoDTO.setCode("400");
			infoDTO.setReason("Missing Mandatory Parameters");
		}
		
		}
		catch (Exception e) {
			logger.error("Service : TransactionHistory - TransactionId"+txnId+" Exception=",e);
			
			
		}
		finally
		{
			long t2 = System.currentTimeMillis();
			logger.info("Transaction ID :"+txnId+" MoNumber "+transactionDTO.getSubscriberNumber()+" Request Leaving System , Processing Time "+(t2-t1) );
			responseList = new ArrayList<>();
			if(flag) 
				response = gson.toJson(detailsDTO);
			else
				response = gson.toJson(infoDTO);
			responseList.add(response);
			
			commonUtil=null;
			languageDAO=null;
			subslistDAO=null;
			
			adapter =null;
			historyDTO=null;
			transactionDetailsDTO2=null;
			transactionDetailsDTO=null;
			transactionDTO = null;
			lanID=null;
			list=null;
			
			detailsDTO=null;
		}
		
		return responseList;
	
	}
}
