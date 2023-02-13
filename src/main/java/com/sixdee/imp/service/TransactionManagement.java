package com.sixdee.imp.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.dao.LanguageDAO;
import com.sixdee.imp.common.dao.SubscriberListCheckDAO;
import com.sixdee.imp.common.util.CommonUtil;
import com.sixdee.imp.dto.GetOrderDetailsDTO;
import com.sixdee.imp.dto.GetTransactionDetailsDTO;
import com.sixdee.imp.dto.ResponseOrderDetailsDTO;
import com.sixdee.imp.dto.ResponseTransactionHistoryDTO;
import com.sixdee.imp.service.serviceDTO.req.OrderStatusDTO;
import com.sixdee.imp.service.serviceDTO.req.OrderTrackingDTO;
import com.sixdee.imp.service.serviceDTO.req.TransactionDTO;
import com.sixdee.imp.service.serviceDTO.resp.OrderTrackingDetailsDTO;
import com.sixdee.imp.service.serviceDTO.resp.OrderTrackingInfoDTO;
import com.sixdee.imp.service.serviceDTO.resp.ResponseDTO;
import com.sixdee.imp.service.serviceDTO.resp.TransactionDetailsDTO;
import com.sixdee.imp.service.serviceDTO.resp.TransactionInfoDTO;

public class TransactionManagement 
{

	private static final Logger logger=Logger.getLogger(TransactionManagement.class);
	
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//	CommonUtil commonUtil=new CommonUtil();
//	LanguageDAO languageDAO=new LanguageDAO();
//	SubscriberListCheckDAO subslistDAO=new SubscriberListCheckDAO();
//	boolean check=false;	
	public TransactionInfoDTO getTransactionDetails(TransactionDTO transactionDTO)
	{
		
		TransactionInfoDTO infoDTO=new TransactionInfoDTO();
		CommonUtil commonUtil=new CommonUtil();
		LanguageDAO languageDAO=new LanguageDAO();
		SubscriberListCheckDAO subslistDAO=new SubscriberListCheckDAO();
		boolean check=false;	
		LMSWebServiceAdapter adapter =null;
		ResponseTransactionHistoryDTO historyDTO=null;
		TransactionDetailsDTO transactionDetailsDTO2=null;
		GetTransactionDetailsDTO transactionDetailsDTO=null;
		String lanID=null;
		List<ResponseTransactionHistoryDTO> list=null;
		String txnId = null;
		TransactionDetailsDTO[] detailsDTO=null;
		long t1 = System.currentTimeMillis();
		try
		{
		txnId = transactionDTO.getTransactionId();
		logger.info("SERVICE : GetTransaction History -- Transaction ID :- ["+txnId+"] MSISDN :- ["+transactionDTO.getMoNumber()+"]  Request Recieved");
		
		
		/*logger.info("TRANSACTION ID::txnId());
		logger.info("SUBSCRIBER NUMBER::"+transactionDTO.getSubscriberNumber());
		logger.info("CHANNEL::"+transactionDTO.getChannel());
		logger.info("PIN::"+transactionDTO.getPin());
		logger.info("FROM DATE::"+transactionDTO.getFromDate());
		logger.info("TO DATE::"+transactionDTO.getToDate());
		logger.info("MO NUMBER::"+transactionDTO.getMoNumber());
		logger.info("LIMIT::"+transactionDTO.getLimit());
		logger.info("OFFSET::"+transactionDTO.getOffSet());
		logger.info("NO OF LAST TRANSACTION::"+transactionDTO.getNoOfLastTransaction());
		logger.info("NO OF MONTHS::"+transactionDTO.getNoOfMonths());
		*/
		lanID=(transactionDTO.getLanguageID()!=null&&!transactionDTO.getLanguageID().trim().equals("")?transactionDTO.getLanguageID().trim():Cache.defaultLanguageID);
		
		if(transactionDTO.getSubscriberNumber()==null||transactionDTO.getSubscriberNumber().trim().equalsIgnoreCase(""))
		{
			infoDTO.setStatusCode(Cache.getServiceStatusMap().get("SUB_REQ_"+lanID).getStatusCode());
			infoDTO.setStatusDescription(Cache.getServiceStatusMap().get("SUB_REQ_"+lanID).getStatusDesc());
			logger.info(Cache.getServiceStatusMap().get("SUB_REQ_"+lanID).getStatusDesc());
			return infoDTO;
		}
		
		if(commonUtil.isItChar(transactionDTO.getSubscriberNumber()))
		{
			infoDTO.setStatusCode(Cache.getServiceStatusMap().get("SUB_INVALID_"+lanID).getStatusCode());
			infoDTO.setStatusDescription(Cache.getServiceStatusMap().get("SUB_INVALID_"+lanID).getStatusDesc());
			logger.info(Cache.getServiceStatusMap().get("SUB_INVALID_"+lanID).getStatusDesc());
			return infoDTO;
		}
		
		if(transactionDTO.getLanguageID()==null||transactionDTO.getLanguageID().trim().equals(""))
			  lanID=languageDAO.getLanguageID(transactionDTO.getSubscriberNumber());
		
		transactionDTO.setLanguageID(lanID);
		
		logger.info("LANGUAGE ID::"+transactionDTO.getLanguageID());
		
		if(Cache.getCacheMap().get("IS_WHITELIST_REQD")!=null && Cache.getCacheMap().get("IS_WHITELIST_REQD").equalsIgnoreCase("true"))
		{
			if(transactionDTO.getChannel().equalsIgnoreCase("SMS") || transactionDTO.getChannel().equalsIgnoreCase("LMS_WEB") || transactionDTO.getChannel().equalsIgnoreCase("USSD"))
			{
				check=subslistDAO.checkSubscriber(transactionDTO.getSubscriberNumber());	
				if(check)
				{
				}
				else
				{
					infoDTO.setStatusCode(Cache.getServiceStatusMap().get("BLACKLIST_"+lanID).getStatusCode());
					infoDTO.setStatusDescription(Cache.getServiceStatusMap().get("BLACKLIST_"+lanID).getStatusDesc());
					logger.info(Cache.getServiceStatusMap().get("BLACKLIST_"+lanID).getStatusDesc());
					return infoDTO;
				}
				
			}
		}
		
		adapter=new LMSWebServiceAdapter();
		
		GenericDTO genericDTO=adapter.callFeature("GetTransactionDetails",transactionDTO);
		
	
		infoDTO.setTimestamp(transactionDTO.getTimestamp());
		infoDTO.setTranscationId(txnId);
		
	//	transactionDTO=null;
		
		if(genericDTO==null)
		{
			infoDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_TRANS_COMMON_FAIL_"+lanID).getStatusCode());
			infoDTO.setStatusDescription(Cache.getServiceStatusMap().get("GET_TRANS_COMMON_FAIL_"+lanID).getStatusDesc());			
			return infoDTO;
		}
		logger.info(genericDTO.getStatusCode());
		if(genericDTO.getStatusCode().equalsIgnoreCase("SC0000") || genericDTO.getStatusCode().equalsIgnoreCase("0"))
		{
			transactionDetailsDTO=(GetTransactionDetailsDTO)genericDTO.getObj();
			infoDTO.setOffSet(transactionDetailsDTO.getOffset());
			infoDTO.setLimit(transactionDetailsDTO.getLimit());
			infoDTO.setTotalCount(transactionDetailsDTO.getRowCount());
			infoDTO.setStatusCode("SC0000");
			infoDTO.setStatusDescription("SUCCESS");
			list=(List<ResponseTransactionHistoryDTO>)transactionDetailsDTO.getObj();
			
			detailsDTO=null;
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
				if(historyDTO.getExpiryDate()!=null)
				transactionDetailsDTO2.setExpiryDate(historyDTO.getExpiryDate());
				if(historyDTO.getExpiryPoints()!=null)
				transactionDetailsDTO2.setExpiryPoints(historyDTO.getExpiryPoints());
				//String re = "[^\\x09\\x0A\\x0D\\x20-\\xD7FF\\xE000-\\xFFFD\\x10000-x10FFFF]";
			    transactionDetailsDTO2.setActivity(historyDTO.getActivity());
				detailsDTO[i]=transactionDetailsDTO2;
				transactionDetailsDTO2=null;
				historyDTO=null;
			}
			
			logger.info("No Of Transcation Giving back is ["+list.size()+"] Row Count "+transactionDetailsDTO.getRowCount()+detailsDTO+"]");
			}
			infoDTO.setTransactionDetails(detailsDTO);
			
			
			list=(List<ResponseTransactionHistoryDTO>)transactionDetailsDTO.getObj();
			
		}else 
		{
			transactionDetailsDTO=(GetTransactionDetailsDTO)genericDTO.getObj();
			infoDTO.setStatusCode(genericDTO.getStatusCode());
			infoDTO.setStatusDescription(genericDTO.getStatus());
			infoDTO.setOffSet(transactionDetailsDTO.getOffset());
			infoDTO.setLimit(transactionDetailsDTO.getLimit());
			infoDTO.setTotalCount(0);
			
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
		}
		catch (Exception e) {
			logger.error("Service : TransactionHistory - TransactionId"+txnId+" Exception=",e);
			
			infoDTO.setStatusCode("SC0002");
			infoDTO.setStatusDescription("Failed due to exception");
		}
		finally
		{
			long t2 = System.currentTimeMillis();
			logger.info("Transaction ID :"+txnId+" MoNumber "+transactionDTO.getSubscriberNumber()+" Request Leaving System , Processing Time "+(t2-t1) );

			
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
		
		return infoDTO;
	
	}
	
	public OrderTrackingInfoDTO getOrderDetails(OrderTrackingDTO orderTrackingDTO)
	{
		OrderTrackingInfoDTO infoDTO=new OrderTrackingInfoDTO();
		LMSWebServiceAdapter adapter = null;
		GetOrderDetailsDTO orderDetailsDTO = null;
		CommonUtil commonUtil=new CommonUtil();
		LanguageDAO languageDAO=new LanguageDAO();
		SubscriberListCheckDAO subslistDAO=new SubscriberListCheckDAO();
		boolean check=false;	
		ResponseOrderDetailsDTO historyDTO=null;
		OrderTrackingDetailsDTO orderDetailsDTO1=null;
		String lanID=null;
		List<ResponseOrderDetailsDTO> list=null;
		
		OrderTrackingDetailsDTO[] detailsDTO=null;
		String txnId = null;
		long t1= System.currentTimeMillis();
		
		try
		{
			
		
		logger.info("Service : GetOrder - TRANSACTION ID::"+orderTrackingDTO.getTranscationId()+" SUBSCRIBER NUMBER::"+orderTrackingDTO.getSubscriberNumber()+" Request Recieved in System");
		logger.debug("CHANNEL::"+orderTrackingDTO.getChannel());
		logger.debug("PIN::"+orderTrackingDTO.getPin());
		logger.debug("FROM DATE::"+orderTrackingDTO.getFromDate());
		logger.debug("TO DATE::"+orderTrackingDTO.getToDate());
		logger.debug("MO NUMBER::"+orderTrackingDTO.getMoNumber());
		logger.debug("LIMIT::"+orderTrackingDTO.getLimit());
		logger.debug("OFFSET::"+orderTrackingDTO.getOffSet());
		logger.debug("NO OF LAST TRANSACTION::"+orderTrackingDTO.getNoOfLastTransaction());
		logger.debug("NO OF MONTHS::"+orderTrackingDTO.getNoOfMonths());
		
		
		lanID=(orderTrackingDTO.getLanguageID()!=null&&!orderTrackingDTO.getLanguageID().trim().equals("")?orderTrackingDTO.getLanguageID().trim():Cache.defaultLanguageID);
		
		if(orderTrackingDTO.getChannel()!=null && !orderTrackingDTO.getChannel().equalsIgnoreCase("LMS_WEB"))
		{
		if(orderTrackingDTO.getSubscriberNumber()==null||orderTrackingDTO.getSubscriberNumber().trim().equalsIgnoreCase(""))
		{
			infoDTO.setStatusCode(Cache.getServiceStatusMap().get("SUB_REQ_"+lanID).getStatusCode());
			infoDTO.setStatusDescription(Cache.getServiceStatusMap().get("SUB_REQ_"+lanID).getStatusDesc());
			logger.info(Cache.getServiceStatusMap().get("SUB_REQ_"+lanID).getStatusDesc());
			return infoDTO;
		}
		
		if(commonUtil.isItChar(orderTrackingDTO.getSubscriberNumber()))
		{
			infoDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_ORDER_INVALID_"+lanID).getStatusCode());
			infoDTO.setStatusDescription(Cache.getServiceStatusMap().get("GET_ORDER_INVALID_"+lanID).getStatusDesc());
			logger.info(Cache.getServiceStatusMap().get("GET_ORDER_INVALID_"+lanID).getStatusDesc());
			return infoDTO;
		}
		}
		
		if(orderTrackingDTO.getLanguageID()==null||orderTrackingDTO.getLanguageID().trim().equals(""))
		  lanID=languageDAO.getLanguageID(orderTrackingDTO.getSubscriberNumber());
		
		orderTrackingDTO.setLanguageID(lanID);		
		logger.info("LANGUAGE ID::"+orderTrackingDTO.getLanguageID());
		
		if(Cache.getCacheMap().get("IS_WHITELIST_REQD")!=null && Cache.getCacheMap().get("IS_WHITELIST_REQD").equalsIgnoreCase("true"))
		{
			if(orderTrackingDTO.getChannel().equalsIgnoreCase("SMS") || orderTrackingDTO.getChannel().equalsIgnoreCase("LMS_WEB") || orderTrackingDTO.getChannel().equalsIgnoreCase("USSD"))
			{
				check=subslistDAO.checkSubscriber(orderTrackingDTO.getSubscriberNumber());	
				if(check)
				{
				}
				else
				{
					infoDTO.setStatusCode(Cache.getServiceStatusMap().get("BLACKLIST_"+lanID).getStatusCode());
					infoDTO.setStatusDescription(Cache.getServiceStatusMap().get("BLACKLIST_"+lanID).getStatusDesc());
					logger.info(Cache.getServiceStatusMap().get("BLACKLIST_"+lanID).getStatusDesc());
					return infoDTO;
				}
				
			}
		}
		adapter=new LMSWebServiceAdapter();
		GenericDTO genericDTO=adapter.callFeature("GetOrderDetails",orderTrackingDTO);
		
		infoDTO.setTimestamp(orderTrackingDTO.getTimestamp());
		infoDTO.setTranscationId(orderTrackingDTO.getTranscationId());
		
		if(genericDTO==null)
		{
			infoDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_ORDER_FAILURE_"+lanID).getStatusCode());
			infoDTO.setStatusDescription(Cache.getServiceStatusMap().get("GET_ORDER_FAILURE_"+lanID).getStatusDesc());
			logger.info(Cache.getServiceStatusMap().get("GET_ORDER_FAILURE_"+lanID).getStatusDesc());
			
			return infoDTO;
		}
		else
		{

			orderDetailsDTO=(GetOrderDetailsDTO)genericDTO.getObj();
			infoDTO.setOffSet(orderDetailsDTO.getOffset());
			infoDTO.setLimit(orderDetailsDTO.getLimit());
			infoDTO.setTotalCount(orderDetailsDTO.getRowCount());
			infoDTO.setStatusCode(orderDetailsDTO.getStatusCode());
			infoDTO.setStatusDescription(orderDetailsDTO.getStatusDesc());
	
			list=(List<ResponseOrderDetailsDTO>)orderDetailsDTO.getObj();
			
			detailsDTO=null;
			if(list!=null)
			{
			detailsDTO=new OrderTrackingDetailsDTO[list.size()];
			
			for(int i=0;i<list.size();i++)
			{
				historyDTO=list.get(i);
				orderDetailsDTO1=new OrderTrackingDetailsDTO();
				orderDetailsDTO1.setOrderId(historyDTO.getOrderId());
				orderDetailsDTO1.setOrderStatus(historyDTO.getOrderStatus());
				orderDetailsDTO1.setOrderDate(historyDTO.getOrderDate());
				orderDetailsDTO1.setItemNumber(historyDTO.getItemNumber());
				orderDetailsDTO1.setItemName(historyDTO.getItemName());
				orderDetailsDTO1.setQuantity(historyDTO.getQuantity());
				orderDetailsDTO1.setRedeemPoints(historyDTO.getRedeemPoints());
				orderDetailsDTO1.setOrderStatus(historyDTO.getOrderStatus());	
				orderDetailsDTO1.setOrderExpiryDate(historyDTO.getExpiryDate());
				detailsDTO[i]=orderDetailsDTO1;
				orderDetailsDTO1=null;
				historyDTO=null;
			}
			}
			infoDTO.setOrderDetails(detailsDTO);
			
			
		}	
		}
		catch (Exception e) {
			logger.info("Exception =",e);
		}
		finally
		{
			long t2 = System.currentTimeMillis();
			logger.info("Transaction ID :"+txnId+" MoNumber "+orderDetailsDTO.getSubscriberNumber()+" Request Leaving System , Processing Time "+(t2-t1) );

			
			adapter = null;
			orderDetailsDTO = null;
			commonUtil=null;
			languageDAO=null;
			subslistDAO=null;
			historyDTO=null;
			orderDetailsDTO1=null;
			orderTrackingDTO=null;
			lanID=null;
			list=null;
			
			detailsDTO=null;
		}
		
		return infoDTO;

	}//getOrderDetails
	
	
	public ResponseDTO changeOrderStatus(OrderStatusDTO orderStatusDTO)
	{
		LMSWebServiceAdapter adapter = new LMSWebServiceAdapter();
		final DateFormat df = new SimpleDateFormat("ddmmyyyyHHMMSS");
		
		GenericDTO genericDTO = adapter.callFeature("UPDATEVOUCHERDETAILS", orderStatusDTO);

		ResponseDTO dto = new ResponseDTO();
		
		dto.setTranscationId(orderStatusDTO.getTransactionId());
		dto.setTimestamp(df.format(new Date()));
		dto.setStatusCode(genericDTO.getStatusCode());
		dto.setStatusDescription(genericDTO.getStatus());
		
		return dto;
	}//changeOrderStatus
	
	/*public static void main(String[] args) throws CommonException {
		GetTransactionDetailsDTO transactionDTO = new GetTransactionDetailsDTO();
		transactionDTO.setChannel("SMS");
		transactionDTO.setFromDate("2013-03-01");
		transactionDTO.setEndDate("2013-06-15");
		transactionDTO.setSubscriberNumber("6666");
		Globals.loadProperties();
		HiberanteUtil hibUtil = new HiberanteUtil();
		Globals.cacheMap();

		GetTransactionDetailsDAO getTransactionDetailsDAO = new GetTransactionDetailsDAO();
	//	GetTransactionDetailsBO  getTransactionDetailsBO = new GetTransactionDetailsBO();
		GenericDTO genericDTO = new GenericDTO();
		genericDTO.setObj(transactionDTO);
		System.out.println(getTransactionDetailsDAO.getLoyaltyId("SUBSCRIBER_NUMBER_6", "6666"));
		System.out.println(getTransactionDetailsDAO.getTransactions("LOYALTY_TRANSACTION_ENTITY_8", "7653744278", "2013-03-01", "2013-06-17", 0, 5));
	
	}*/
}
