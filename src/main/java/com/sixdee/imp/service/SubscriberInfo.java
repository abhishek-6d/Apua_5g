package com.sixdee.imp.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.imp.dto.ActDeactOfferDTO;
import com.sixdee.imp.service.serviceDTO.req.ActivateDeactivateDTO;
import com.sixdee.imp.service.serviceDTO.req.AddOfferDTO;
import com.sixdee.imp.service.serviceDTO.req.ChangeServiceDTO;
import com.sixdee.imp.service.serviceDTO.req.EligibleSubscriberDTO;
import com.sixdee.imp.service.serviceDTO.req.GetPromoDTO;
import com.sixdee.imp.service.serviceDTO.req.SubscriberHistoryReqDTO;
import com.sixdee.imp.service.serviceDTO.req.UpdateAccountDTO;
import com.sixdee.imp.service.serviceDTO.req.UpdatePSODTO;
import com.sixdee.imp.service.serviceDTO.resp.EligibleSubscriberInfoDTO;
import com.sixdee.imp.service.serviceDTO.resp.PromoDTO;
import com.sixdee.imp.service.serviceDTO.resp.PromoInfoDTO;
import com.sixdee.imp.service.serviceDTO.resp.ResponseDTO;
import com.sixdee.imp.service.serviceDTO.resp.SubscriberHistoryInfoDTO;

public class SubscriberInfo 
{
	private Logger logger=Logger.getLogger(SubscriberInfo.class);
	/*private DateFormat df = new SimpleDateFormat("ddMMyyyyhhmmss");
	private DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");*/
	public SubscriberHistoryInfoDTO getSubscriberHistory(SubscriberHistoryReqDTO subscribeReqDTO)
	{
		DateFormat df = new SimpleDateFormat("ddMMyyyyhhmmss");
		DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
		logger.info("In getSubscriberHistory Service");
		logger.info("TRANSACTION ID::"+subscribeReqDTO.getTransactionId() +" Started processing");
		logger.info("MSISDN::"+subscribeReqDTO.getMsisdn());
		logger.info("CHANNEL::"+subscribeReqDTO.getChannel());
		logger.info("FROM DATE::"+subscribeReqDTO.getFromDate());
		logger.info("TO DATE::"+subscribeReqDTO.getToDate());
		logger.info("MONTH:"+subscribeReqDTO.getMonth());
		
		SubscriberHistoryInfoDTO historyInfoDTO = null;
		LMSWebServiceAdapter adapter = null;
		GenericDTO genericDTO = null;
		try
		{
			historyInfoDTO = new SubscriberHistoryInfoDTO();
			historyInfoDTO.setTranscationId(subscribeReqDTO.getTransactionId());
			if(subscribeReqDTO.getMsisdn()==null || subscribeReqDTO.getMsisdn().equalsIgnoreCase(""))
			{
				historyInfoDTO.setStatusCode("SC0001");
				historyInfoDTO.setStatusDescription("MSISDN can not be null");
				historyInfoDTO.setTimestamp(df.format(new Date()));

				return historyInfoDTO;
			}
			
			/*if(subscribeReqDTO.getFromDate()==null && subscribeReqDTO.getToDate()==null)
			{
				if(subscribeReqDTO.getMonth()==null)
				{
					historyInfoDTO.setStatusCode("SC0001");
					historyInfoDTO.setStatusDescription("From Date, To Date or Month mandatory.");
					historyInfoDTO.setTimestamp(df.format(new Date()));

					return historyInfoDTO;
				}
			}*/
			
/*			if(subscribeReqDTO.getFromDate()!=null && subscribeReqDTO.getToDate()!=null)
			{
				Date fromDate = df1.parse(subscribeReqDTO.getFromDate());
				Date toDate = df1.parse(subscribeReqDTO.getToDate());
				
				Calendar fromcal = Calendar.getInstance();
				fromcal.setTime(fromDate);

				Calendar toCal = Calendar.getInstance();
				toCal.setTime(toDate);
				logger.info(fromcal.get(Calendar.MONTH)+" "+toCal.get(Calendar.MONTH) +" "+fromcal.get(Calendar.YEAR)+" "+toCal.get(Calendar.YEAR));
				if( fromcal.get(Calendar.YEAR)!=toCal.get(Calendar.YEAR))
				{
					historyInfoDTO.setStatusCode("SC0001");
					historyInfoDTO.setStatusDescription("Failure ! Can not get History from two different months.");
					historyInfoDTO.setTimestamp(df.format(new Date()));
					
					return historyInfoDTO;
				}
				else if (fromcal.get(Calendar.MONTH)!=toCal.get(Calendar.MONTH))
				{
					historyInfoDTO.setStatusCode("SC0001");
					historyInfoDTO.setStatusDescription("Failure ! Can not get History from two different months.");
					historyInfoDTO.setTimestamp(df.format(new Date()));
					
					return historyInfoDTO;
					
				}
				else
				{
					subscribeReqDTO.setMonth((fromcal.get(Calendar.MONTH)+1)+"");
				}
					
			}
			else
			{
				historyInfoDTO.setStatusCode("SC0001");
				historyInfoDTO.setStatusDescription("From Date and To Date is mandatory.");
				historyInfoDTO.setTimestamp(df.format(new Date()));

				return historyInfoDTO;
			}*/
				
			
			adapter = new LMSWebServiceAdapter();
			genericDTO = adapter.callFeature("SubsHistory", subscribeReqDTO);
			
			historyInfoDTO = (SubscriberHistoryInfoDTO)genericDTO.getObj();
			
		}
		catch (Exception e) 
		{
			logger.error("Exception =",e);
		}
		finally
		{
			adapter = null;
			genericDTO = null;
		}
		logger.info(historyInfoDTO.getTranscationId()+" Transaction id processing completed..");
		return historyInfoDTO;
	
	}
	
	public EligibleSubscriberInfoDTO getEligibleSubscriber(EligibleSubscriberDTO eligibleSubscriberDTO)
	{
		LMSWebServiceAdapter adapter = null;
		GenericDTO genericDTO = null;
		logger.info("In getEligibleSubscriber Service");
		logger.info("TRANSACTION ID::"+eligibleSubscriberDTO.getTransactionId()+" started Processing.");
		logger.info("MSISDN::"+eligibleSubscriberDTO.getMsisdn());
		logger.info("CHANNEL::"+eligibleSubscriberDTO.getChannel());
		logger.info("MONTH::"+eligibleSubscriberDTO.getMonth());
		
		if(eligibleSubscriberDTO.getMonth()!=null && eligibleSubscriberDTO.getMonth().contains("-"))
			eligibleSubscriberDTO.setMonth(Integer.parseInt(eligibleSubscriberDTO.getMonth().split("-")[0])+"");
		
		logger.info("MONTH::"+eligibleSubscriberDTO.getMonth());
		
		EligibleSubscriberInfoDTO historyInfoDTO = null;
		try
		{

			if (eligibleSubscriberDTO.getMsisdn() == null || eligibleSubscriberDTO.getMsisdn().equalsIgnoreCase("")) {
				historyInfoDTO = new EligibleSubscriberInfoDTO();
				historyInfoDTO.setStatusCode("SC0001");
				historyInfoDTO.setStatusDescription("MSISDN can not be null");
				return historyInfoDTO;
			}
		/*	if (eligibleSubscriberDTO.getMonth() == null || eligibleSubscriberDTO.getMonth().equalsIgnoreCase("")) {
				historyInfoDTO = new EligibleSubscriberInfoDTO();
				historyInfoDTO.setStatusCode("SC0001");
				historyInfoDTO.setStatusDescription("Month can not be null");
				return historyInfoDTO;
			}
*/
			adapter = new LMSWebServiceAdapter();
		
			genericDTO = adapter.callFeature("EligibleSubscriber", eligibleSubscriberDTO);

			historyInfoDTO = (EligibleSubscriberInfoDTO) genericDTO.getObj();
		}
		catch (Exception e) {
			logger.error(historyInfoDTO.getTranscationId()+" Transaction Id ",e);
		}
		finally
		{
			adapter = null;
			genericDTO = null;
		}
		logger.info(historyInfoDTO.getTranscationId()+" Transaction Id processing complete .");
		return historyInfoDTO;
	
		
	}
	
	
	public ResponseDTO updateAccount(UpdateAccountDTO updateAccountDTO)
	{
		DateFormat df = new SimpleDateFormat("ddMMyyyyhhmmss");
		
		logger.info("In updateAccount Service");
		logger.info("TRANSACTION ID::"+updateAccountDTO.getTransactionId());
		logger.info("MSISDN::"+updateAccountDTO.getMsisdn());
		logger.info("CHANNEL::"+updateAccountDTO.getChannel());
		logger.info("DEDICATED ACCOUNT ID::"+updateAccountDTO.getDedicatedActId());
		logger.info("OPERATION::"+updateAccountDTO.getOperation());
		
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setTranscationId(updateAccountDTO.getTransactionId());
		responseDTO.setTimestamp(df.format(new Date()));

		if(updateAccountDTO.getChannel()==null || updateAccountDTO.getChannel().equalsIgnoreCase(""))
		{
			responseDTO.setStatusCode("SC0001");
			responseDTO.setStatusDescription("Channel can not be null");
			return responseDTO;
		}

		if(updateAccountDTO.getMsisdn()==null || updateAccountDTO.getMsisdn().equalsIgnoreCase(""))
		{
			responseDTO.setStatusCode("SC0001");
			responseDTO.setStatusDescription("MSISDN can not be null");
			return responseDTO;
		}
		
		if(updateAccountDTO.getDedicatedActId()==null || updateAccountDTO.getDedicatedActId().equalsIgnoreCase(""))
		{
			responseDTO.setStatusCode("SC0001");
			responseDTO.setStatusDescription("Dedicated Account ID can not be null");
			return responseDTO;
		}
		
		if(updateAccountDTO.getOperation()==0 || updateAccountDTO.getOperation()>5)
		{
			responseDTO.setStatusCode("SC0001");
			responseDTO.setStatusDescription("Unknown Operation !!");
			return responseDTO;
		}	
	
		
		LMSWebServiceAdapter adapter = new LMSWebServiceAdapter();
		GenericDTO genericDTO = adapter.callFeature("UpdateAccount", updateAccountDTO);
		
		responseDTO.setStatusCode(genericDTO.getStatusCode());
		responseDTO.setStatusDescription(genericDTO.getStatus());
		
		return responseDTO;
	
	}
	
	public ResponseDTO changeServiceClass(ChangeServiceDTO changeServiceDTO)
	{
		DateFormat df = new SimpleDateFormat("ddMMyyyyhhmmss");
		logger.info("In changeServiceClass Service");
		logger.info("TRANSACTION ID::"+changeServiceDTO.getTransactionId());
		logger.info("MSISDN::"+changeServiceDTO.getMsisdn());
		logger.info("CHANNEL::"+changeServiceDTO.getChannel());
		logger.info("CLASS ID::"+changeServiceDTO.getClassId());
		
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setTranscationId(changeServiceDTO.getTransactionId());
		responseDTO.setTimestamp(df.format(new Date()));

		if(changeServiceDTO.getMsisdn()==null || changeServiceDTO.getMsisdn().equalsIgnoreCase(""))
		{
			responseDTO.setStatusCode("SC0001");
			responseDTO.setStatusDescription("MSISDN can not be null");
			return responseDTO;
		}
		if(changeServiceDTO.getClassId()==null || changeServiceDTO.getClassId().equalsIgnoreCase(""))
		{
			responseDTO.setStatusCode("SC0001");
			responseDTO.setStatusDescription("ClassId can not be null");
			return responseDTO;
			
		}
		LMSWebServiceAdapter adapter = new LMSWebServiceAdapter();
		GenericDTO genericDTO = adapter.callFeature("ChangeServiceClass", changeServiceDTO);
		
		responseDTO.setStatusCode(genericDTO.getStatusCode());
		responseDTO.setStatusDescription(genericDTO.getStatus());
		
		return responseDTO;
	
		}
	
	public ResponseDTO addOffer(AddOfferDTO addOfferDTO)
	{
		DateFormat df = new SimpleDateFormat("ddMMyyyyhhmmss");
		
		logger.info("In addOffer Service");
		logger.info("TRANSACTION ID::"+addOfferDTO.getTransactionId());
		logger.info("MSISDN::"+addOfferDTO.getMsisdn());
		logger.info("CHANNEL::"+addOfferDTO.getChannel());
		logger.info("OPERATION::"+addOfferDTO.getOperation());
		logger.info("OFFER ID::"+addOfferDTO.getOfferId());
		
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setTranscationId(addOfferDTO.getTransactionId());
		responseDTO.setTimestamp(df.format(new Date()));
		if(addOfferDTO.getMsisdn()==null || addOfferDTO.getMsisdn().equalsIgnoreCase(""))
		{
			responseDTO.setStatusCode("SC0001");
			responseDTO.setStatusDescription("MSISDN can not be null");
			return responseDTO;
		}
		
		if(addOfferDTO.getOperation()==0 || addOfferDTO.getOperation()>3)
		{
			responseDTO.setStatusCode("SC0001");
			responseDTO.setStatusDescription("Unknow Operation !!");
			return responseDTO;
		}
		
		switch (addOfferDTO.getOperation()) {
		case 1:
			if(addOfferDTO.getOfferId()==0)
			{
				responseDTO.setStatusCode("SC0001");
				responseDTO.setStatusDescription("Offer Id can not be null !!");
				return responseDTO;
			}
			break;
		case 3:
			if(addOfferDTO.getOfferId()==0)
			{
				responseDTO.setStatusCode("SC0001");
				responseDTO.setStatusDescription("Offer Id can not be null !!");
				return responseDTO;
			}
			break;
		}
		
		
		LMSWebServiceAdapter adapter = new LMSWebServiceAdapter();
		GenericDTO genericDTO = adapter.callFeature("AddOffer", addOfferDTO);
		
		responseDTO.setTranscationId(addOfferDTO.getTransactionId());
		responseDTO.setStatusCode(genericDTO.getStatusCode());
		responseDTO.setStatusDescription(genericDTO.getStatus());
		
		return responseDTO;
	
	}
	
	public ResponseDTO updatePSO(UpdatePSODTO updatePSODTO)
	{
		DateFormat df = new SimpleDateFormat("ddMMyyyyhhmmss");
		
		logger.info("In updatePSO Service");
		logger.info("TRANSACTION ID::"+updatePSODTO.getTransactionId());
		logger.info("MSISDN::"+updatePSODTO.getMsisdn());
		logger.info("CHANNEL::"+updatePSODTO.getChannel());
		logger.info("OPEARATION::"+updatePSODTO.getOperation());
		logger.info("OFFER ID::"+updatePSODTO.getOfferId());
		
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setTranscationId(updatePSODTO.getTransactionId());
		responseDTO.setTimestamp(df.format(new Date()));
		
		if(updatePSODTO.getMsisdn()==null || updatePSODTO.getMsisdn().equalsIgnoreCase(""))
		{
			responseDTO.setStatusCode("SC0001");
			responseDTO.setStatusDescription("MSISDN can not be null");
			return responseDTO;
		}
		if(updatePSODTO.getOperation()==0 || updatePSODTO.getOperation()>3)
		{
			responseDTO.setStatusCode("SC0001");
			responseDTO.setStatusDescription("Unknow Operation !!");
			return responseDTO;
		}
		switch (updatePSODTO.getOperation()) {
		case 1:
			if(updatePSODTO.getOfferId()==0)
			{
				responseDTO.setStatusCode("SC0001");
				responseDTO.setStatusDescription("Offer Id can not be null !!");
				return responseDTO;
			}
			break;
		case 3:
			if(updatePSODTO.getOfferId()==0)
			{
				responseDTO.setStatusCode("SC0001");
				responseDTO.setStatusDescription("Offer Id can not be null !!");
				return responseDTO;
			}
			break;
		}
		
		LMSWebServiceAdapter adapter = new LMSWebServiceAdapter();
		GenericDTO genericDTO = adapter.callFeature("UpdatePSO", updatePSODTO);
		
		responseDTO.setStatusCode(genericDTO.getStatusCode());
		responseDTO.setStatusDescription(genericDTO.getStatus());
		
		return responseDTO;
	}
	
	public ResponseDTO activateDeactivateOffer(ActivateDeactivateDTO activateDeactivateDTO)
	{
		DateFormat df = new SimpleDateFormat("ddMMyyyyhhmmss");
		logger.info(activateDeactivateDTO.getTransactionId()+" Transaction Id "+ activateDeactivateDTO.getSubscriberNumber()+" Subscriber Number " +
				+activateDeactivateDTO.getMarketingPlanId()+" Offer "+activateDeactivateDTO.getDialCode()+" Dial Code "+activateDeactivateDTO.getSmsKeyword()+" Sms keyword "+activateDeactivateDTO.getKeyWord()+" Keyword");
		
		ActDeactOfferDTO actDeactOfferDTO = null;
		LMSWebServiceAdapter adapter = null;
		ResponseDTO responseDTO = new ResponseDTO();
		GenericDTO genericDTO = null;
		
		
		
		try
		{
			logger.info("TRANSACTION ID::"+activateDeactivateDTO.getTransactionId() +" Started processing");
			logger.info("MSISDN::"+activateDeactivateDTO.getSubscriberNumber());
			logger.info("CHANNEL::"+activateDeactivateDTO.getChannel());
			logger.info("DIAL CODE::"+activateDeactivateDTO.getDialCode());
			logger.info("SMS KEYWORD::"+activateDeactivateDTO.getSmsKeyword());
			logger.info("KEYWORD::"+activateDeactivateDTO.getKeyWord());
			logger.info("MARKETING PLAN::"+activateDeactivateDTO.getMarketingPlanId());
			
			if(activateDeactivateDTO.getSubscriberNumber()==null || activateDeactivateDTO.getSubscriberNumber().trim().equalsIgnoreCase(""))
			{
				responseDTO.setTranscationId(activateDeactivateDTO.getTransactionId());
				responseDTO.setStatusCode("SC0001");
				responseDTO.setStatusDescription("Subscriber Number is mandatory !!");
				responseDTO.setTimestamp(df.format(new Date()));
				return responseDTO;
			}
			if(activateDeactivateDTO.getChannel()==null || activateDeactivateDTO.getChannel().trim().equalsIgnoreCase(""))
			{
				responseDTO.setTranscationId(activateDeactivateDTO.getTransactionId());
				responseDTO.setStatusCode("SC0001");
				responseDTO.setStatusDescription("Channel is mandatory !!");
				responseDTO.setTimestamp(df.format(new Date()));
				return responseDTO;
			}
			
			
			
			if(activateDeactivateDTO.getKeyWord()==null || activateDeactivateDTO.getKeyWord().trim().equalsIgnoreCase(""))
			{
				responseDTO.setTranscationId(activateDeactivateDTO.getTransactionId());
				responseDTO.setStatusCode("SC0001");
				responseDTO.setStatusDescription("Keyword is mandatory !!");
				responseDTO.setTimestamp(df.format(new Date()));
				return responseDTO;
			}
			
			try
			{
				ConstantVariables.valueOf(activateDeactivateDTO.getKeyWord().toUpperCase());
			}
			catch (IllegalArgumentException e) 
			{
				logger.error("Exception ",e);
				responseDTO.setTranscationId(activateDeactivateDTO.getTransactionId());
				responseDTO.setStatusCode("SC0001");
				responseDTO.setStatusDescription("Invalid keyword !!");
				responseDTO.setTimestamp(df.format(new Date()));
				return responseDTO;
			}
			
			//if(activateDeactivateDTO.getKeyWord().equalsIgnoreCase("Active"))
			if(ConstantVariables.ACTIVE.getValue().equalsIgnoreCase(activateDeactivateDTO.getKeyWord()))
			{

				if ((activateDeactivateDTO.getDialCode() == null || activateDeactivateDTO.getDialCode().trim().equalsIgnoreCase(""))
						&& (activateDeactivateDTO.getSmsKeyword() == null || activateDeactivateDTO.getSmsKeyword().trim().equalsIgnoreCase(""))) {
					responseDTO.setTranscationId(activateDeactivateDTO.getTransactionId());
					responseDTO.setStatusCode("SC0001");
					responseDTO.setStatusDescription("Ussd dial code or sms keywrod is mandatory !!");
					responseDTO.setTimestamp(df.format(new Date()));
					return responseDTO;

				}
			}
			
			actDeactOfferDTO = new ActDeactOfferDTO();
			
			actDeactOfferDTO.setChannel(activateDeactivateDTO.getChannel());
			actDeactOfferDTO.setSmsKeyword(activateDeactivateDTO.getSmsKeyword());
			actDeactOfferDTO.setDialCode(activateDeactivateDTO.getDialCode());
			actDeactOfferDTO.setKeyWord(activateDeactivateDTO.getKeyWord());
			actDeactOfferDTO.setSubscriberNumber(activateDeactivateDTO.getSubscriberNumber());
			actDeactOfferDTO.setTransactionId(activateDeactivateDTO.getTransactionId());
			actDeactOfferDTO.setMarketingPlanId(activateDeactivateDTO.getMarketingPlanId());
			if(activateDeactivateDTO.getData()!=null)
				actDeactOfferDTO.setDatasList(Arrays.asList(activateDeactivateDTO.getData()));
			adapter = new LMSWebServiceAdapter();
			genericDTO = adapter.callFeature("ActDeactOffer", actDeactOfferDTO);
			
			responseDTO = (ResponseDTO)genericDTO.getObj();
			responseDTO.setTranscationId(activateDeactivateDTO.getTransactionId());
			responseDTO.setTimestamp(df.format(new Date()));
			
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception in activateDeactivateOffer",e);
		}
		
		return responseDTO;
	}
	
	public PromoInfoDTO getPromoList(GetPromoDTO getPromoDTO)
	{
		PromoInfoDTO promoInfoDTO = null;
		String requestId = getPromoDTO.getTransactionId();
		logger.info("Service : GetPromoList - Transaction ID :- "+requestId+" Request Recieved");
		LMSWebServiceAdapter adapter = new LMSWebServiceAdapter();
		GenericDTO genericDTO = adapter.callFeature("GetOfferCodeOnMarketingPlan",getPromoDTO);
		promoInfoDTO = (PromoInfoDTO)genericDTO.getObj();
		promoInfoDTO.setStatus(genericDTO.getStatusCode());
		promoInfoDTO.setStatusDesc(genericDTO.getStatus());
		return promoInfoDTO;
	}
	
	private enum ConstantVariables
	{
		ACTIVE("active"),DEACTIVE("deactive");
		private String value;
		private ConstantVariables(String s)
		{
			value = s;
		}
		public String getValue()
		{
			return value;
		}
	}
}
