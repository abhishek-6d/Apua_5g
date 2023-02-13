package com.sixdee.imp.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.dao.LanguageDAO;
import com.sixdee.imp.common.dao.SubscriberListCheckDAO;
import com.sixdee.imp.dto.RequestProcessDTO;
import com.sixdee.imp.dto.TransferLineDTO;
import com.sixdee.imp.service.serviceDTO.common.Data;
import com.sixdee.imp.service.serviceDTO.req.AddPointsDTO;
import com.sixdee.imp.service.serviceDTO.req.PackageDTO;
import com.sixdee.imp.service.serviceDTO.req.PointDetailsDTO;
import com.sixdee.imp.service.serviceDTO.req.RedeemDTO;
import com.sixdee.imp.service.serviceDTO.req.RewardPointsDTO;
import com.sixdee.imp.service.serviceDTO.req.TransferPointsDTO;
import com.sixdee.imp.service.serviceDTO.resp.PackageInfoDTO;
import com.sixdee.imp.service.serviceDTO.resp.PointInfoDTO;
import com.sixdee.imp.service.serviceDTO.resp.PointInfoDetailsDTO;
import com.sixdee.imp.service.serviceDTO.resp.ResponseCodeDTO;
import com.sixdee.imp.service.serviceDTO.resp.ResponseDTO;
import com.sixdee.imp.threadpool.ThreadInitiator;
public class PointManagement
{

	private final static Logger logger=Logger.getLogger(PointManagement.class);
	
	boolean check=false;
	public ResponseDTO redeemPoints(RedeemDTO redeemDTO)
	{
		ResponseDTO responseDTO = null;
		LMSWebServiceAdapter adapter = null;
		GenericDTO genericDTO = null;
		LanguageDAO languageDAO=null;
		SubscriberListCheckDAO subslistDAO=null;
		String txnId = null;
		long t1 = System.currentTimeMillis();
		
		try
		{
			
			txnId = redeemDTO.getTransactionID();
			languageDAO=new LanguageDAO();
			subslistDAO=new SubscriberListCheckDAO();
			
		logger.info("Service : RedeemPoints -- Transaction id "+txnId+" channel "+redeemDTO.getChannel()+" line number" +
					" "+redeemDTO.getLineNumber()+" mo number "+redeemDTO.getMoNumber()+" pin "+redeemDTO.getPin()+" " +
							"package id "+redeemDTO.getPackID()+" Request Recieved in System");
			responseDTO = new ResponseDTO();
			responseDTO.setTranscationId(txnId);
			responseDTO.setTimestamp(redeemDTO.getTimestamp());
			
			int langId = redeemDTO.getLanguageId();
			logger.info("channel"+redeemDTO.getChannel());
			if((redeemDTO.getMoNumber()==null||redeemDTO.getMoNumber().trim().equalsIgnoreCase("")))
			{
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("SUB_REQ_"+langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("SUB_REQ_"+langId).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("SUB_REQ_"+langId).getStatusDesc());
				return responseDTO;
			}
			
			if(langId==0 && redeemDTO.getMoNumber()!=null)
				langId = Integer.parseInt(languageDAO.getLanguageID(redeemDTO.getMoNumber()));
			//else if (langId==0 && redeemDTO.getMoNumber()!=null)
			//	langId = Integer.parseInt(languageDAO.getLanguageID(redeemDTO.getMoNumber()));
			
			redeemDTO.setLanguageId(langId);
			
			if(redeemDTO.getChannel()==null || redeemDTO.getChannel().equalsIgnoreCase("") )
			{responseDTO.setStatusCode(Cache.getServiceStatusMap().get("CHN_REQ_"+langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("CHN_REQ_"+langId).getStatusDesc());
				return responseDTO;
			}
			logger.info("channel"+redeemDTO.getChannel());
			if(Cache.getCacheMap().get("IS_WHITELIST_REQD")!=null && Cache.getCacheMap().get("IS_WHITELIST_REQD").equalsIgnoreCase("true"))
			{
				if(redeemDTO.getChannel().equalsIgnoreCase("SMS") || redeemDTO.getChannel().equalsIgnoreCase("LMS_WEB") || redeemDTO.getChannel().equalsIgnoreCase("USSD"))
				{
					check=subslistDAO.checkSubscriber(redeemDTO.getMoNumber());	
					if(check)
					{
					}
					else
					{
						responseDTO.setStatusCode(Cache.getServiceStatusMap().get("BLACKLIST_"+langId).getStatusCode());
						responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("BLACKLIST_"+langId).getStatusDesc());
						logger.info(Cache.getServiceStatusMap().get("BLACKLIST_"+langId).getStatusDesc());
						return responseDTO;
					}
					
				}
			}
			
			if((redeemDTO.getLineNumber()!=null && !redeemDTO.getLineNumber().equalsIgnoreCase("")) || (redeemDTO.getMoNumber()!=null && !redeemDTO.getMoNumber().equalsIgnoreCase("")))
			{
			}
			else
			{	responseDTO.setStatusCode(Cache.getServiceStatusMap().get("SUB_REQ_"+langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("SUB_REQ_"+langId).getStatusDesc());
				return responseDTO;
			}
			if(redeemDTO.getPackID()==0)
			{	responseDTO.setStatusCode(Cache.getServiceStatusMap().get("PACKG_REQ_"+langId).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("PACKG_REQ_"+langId).getStatusDesc());
				return responseDTO;
			}
			
			adapter = new LMSWebServiceAdapter();
			genericDTO = adapter.callFeature("RedeemPoints", redeemDTO);
			responseDTO = (ResponseDTO)genericDTO.getObj();
			responseDTO.setStatusCode(responseDTO.getStatusCode());
			responseDTO.setStatusDescription(responseDTO.getStatusDescription());
			logger.info("Transaction ID :"+txnId+" MoNumber "+redeemDTO.getMoNumber()+"RESPONSE COMING IS"+responseDTO.getStatusDescription());
			logger.info("Transaction ID :"+txnId+" MoNumber "+redeemDTO.getMoNumber()+"RESPONSE COMING IS"+responseDTO.getStatusCode());
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			logger.info("",e);
			if(responseDTO==null)
				responseDTO=new ResponseDTO(); 
			responseDTO.setStatusCode("SC0000");
			responseDTO.setStatusDescription("Redeemed successfully");
			
		}finally{
			long t2 = System.currentTimeMillis();
			logger.info("Transaction ID :"+txnId+" MoNumber "+redeemDTO.getMoNumber()+" Request Leaving System , Processing Time "+(t2-t1) );

			languageDAO=null;
			adapter=null;
			genericDTO=null;
			subslistDAO=null;
			redeemDTO=null;
			
			
		}
		return responseDTO;
	
	}
	
	public ResponseDTO transferPoints(TransferPointsDTO transferPointsDTO)
	{

		LanguageDAO languageDAO=null;
		SubscriberListCheckDAO subslistDAO=null;
		LMSWebServiceAdapter adapter=null;
		GenericDTO genericDTO=null;
		String lanID=Cache.defaultLanguageID;
		ResponseDTO responseDTO=null;
		String txnId = null;
		long t1 = System.currentTimeMillis();
		try{
		
			languageDAO=new LanguageDAO();
			subslistDAO=new SubscriberListCheckDAO();
			/*
			logger.info("Service :TransferPoints - Transaction ID :"+transferPointsDTO.getTransactionID()+" Pin : "+transferPointsDTO.getPin()+"" +
					" Subscriber Number : "+transferPointsDTO.getSubscriberNumber()+" Dest Subscriber Number : "+transferPointsDTO.getDestSubscriberNumber()+
					" Transfer Points : "+transferPointsDTO.getPoints()+" Language ID "+transferPointsDTO.getLanguageID());*/
			txnId = transferPointsDTO.getTransactionID();
			logger.info("Service : Transfer_Points - Transaction ID :- ["+txnId+"] S_MSISDN :- ["+transferPointsDTO.getSubscriberNumber()+"] D_MSISDN :- ["+transferPointsDTO.getDestSubscriberNumber()+"] Request Recieved in System");
			
			lanID=(transferPointsDTO.getLanguageID()!=null&&!transferPointsDTO.getLanguageID().trim().equals("")?transferPointsDTO.getLanguageID().trim():lanID);
			
			responseDTO=new ResponseDTO();
			responseDTO.setTimestamp(transferPointsDTO.getTimestamp());
			responseDTO.setTranscationId(txnId);
			
			
			
			if(transferPointsDTO.getSubscriberNumber()==null||transferPointsDTO.getSubscriberNumber().trim().equalsIgnoreCase(""))
			{
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("SUB_REQ_"+lanID).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("SUB_REQ_"+lanID).getStatusDesc());
				return responseDTO;
			}
			
			if(transferPointsDTO.getLanguageID()==null||transferPointsDTO.getLanguageID().trim().equals(""))
			{
			   lanID=languageDAO.getLanguageID(transferPointsDTO.getSubscriberNumber());
	    	   transferPointsDTO.setLanguageID(lanID);
			}
			
			
			if(transferPointsDTO.getChannel()==null||transferPointsDTO.getChannel().trim().equalsIgnoreCase(""))
			{
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("CHN_REQ_"+lanID).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("CHN_REQ_"+lanID).getStatusDesc());
				return responseDTO;
			}
			
			if(Cache.getCacheMap().get("IS_WHITELIST_REQD")!=null && Cache.getCacheMap().get("IS_WHITELIST_REQD").equalsIgnoreCase("true"))
			{
				if(transferPointsDTO.getChannel().equalsIgnoreCase("SMS") || transferPointsDTO.getChannel().equalsIgnoreCase("LMS_WEB") || transferPointsDTO.getChannel().equalsIgnoreCase("USSD"))
				{
					check=subslistDAO.checkSubscriber(transferPointsDTO.getSubscriberNumber());	
					if(check)
					{
					}
					else
					{
						responseDTO.setStatusCode(Cache.getServiceStatusMap().get("BLACKLIST_"+lanID).getStatusCode());
						responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("BLACKLIST_"+lanID).getStatusDesc());
						logger.info(Cache.getServiceStatusMap().get("BLACKLIST_"+lanID).getStatusDesc());
						return responseDTO;
					}
					
				}
			}
			
			if(transferPointsDTO.getDestSubscriberNumber()==null||transferPointsDTO.getDestSubscriberNumber().trim().equalsIgnoreCase(""))
			{
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("SUB_DEST_REQ_"+lanID).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("SUB_DEST_REQ_"+lanID).getStatusDesc());
				return responseDTO;
			}
			
			if(transferPointsDTO.getSubscriberNumber().trim().equalsIgnoreCase(transferPointsDTO.getDestSubscriberNumber().trim()))
			{
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("TRAN_POINT_SAME_SUB_"+lanID).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("TRAN_POINT_SAME_SUB_"+lanID).getStatusDesc());
				return responseDTO;
			}
			
			if(transferPointsDTO.getPoints()<=0)
			{
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("TRAN_POINTS_REQ_"+lanID).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("TRAN_POINTS_REQ_"+lanID).getStatusDesc());
				return responseDTO;
			}
			
			if(transferPointsDTO.getChannel().trim().equalsIgnoreCase("SMS"))
			{
				
				if(transferPointsDTO.getPin()==null||transferPointsDTO.getPin()<=0)
				{
					responseDTO.setStatusCode(Cache.getServiceStatusMap().get("PIN_REQ_"+lanID).getStatusCode());
					responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("PIN_REQ_"+lanID).getStatusDesc());
					return responseDTO;
				}
			}
			
			
			adapter=new LMSWebServiceAdapter();
			
			genericDTO=adapter.callFeature("TransferPoints", transferPointsDTO);
			
			
			if(genericDTO==null)
			{
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("TRAN_POINT_FAIL_"+lanID).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("TRAN_POINT_FAIL_"+lanID).getStatusDesc());
				
				return responseDTO;
			}
			
			 if(genericDTO.getStatusCode()!=null){
				
				responseDTO.setStatusCode(genericDTO.getStatusCode());
				responseDTO.setStatusDescription(genericDTO.getStatus());
				
			}else{
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("TRAN_POINT_FAIL_"+lanID).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("TRAN_POINT_FAIL_"+lanID).getStatusDesc());
			}

		/*	if(responseDTO==null)
				responseDTO= new ResponseDTO();
			 responseDTO.setStatusCode("SC0000");
			 responseDTO.setStatusDescription("Transferred Points Successfully"); 
			 responseDTO.setTimestamp(transferPointsDTO.getTimestamp());
			 responseDTO.setTranscationId(txnId);*/
			
		}catch (Exception e) {
			if(responseDTO==null)
				responseDTO=new ResponseDTO();
			 responseDTO.setStatusCode("SC0001");
			 responseDTO.setStatusDescription("Transfer Points failed");
			
			logger.error("",e);
		}finally{
			long t2 = System.currentTimeMillis();
			logger.info("Transaction ID :"+txnId+" MoNumber "+transferPointsDTO.getSubscriberNumber()+" Request Leaving System , Processing Time "+(t2-t1) );

			languageDAO=null;
			subslistDAO=null;
			adapter=null;
			genericDTO=null;
			lanID=null;
		}
		
		return responseDTO;
	
	}
	
	public PackageInfoDTO getPackages(PackageDTO packageDTO)
	{
		DateFormat df = new SimpleDateFormat("ddmmyyyyHHMMSS");
		
		LanguageDAO languageDAO=null;
		SubscriberListCheckDAO subslistDAO=null;
		LMSWebServiceAdapter adapter=null;
		GenericDTO genericDTO=null;
		String langId=null;
		PackageInfoDTO packageInfoDTO = null;
		String txnId = null;
		long t1 = System.currentTimeMillis();
		Boolean checkFlag=true;
		try{
		
			languageDAO=new LanguageDAO();
			subslistDAO=new SubscriberListCheckDAO();
			
			logger.info("Service : GetPackages - Transaction ID : "+packageDTO.getTranscationId()+" subscriber number : "+packageDTO.getSubscriberNumber()+" Request Recieved in System");
			//logger.info("transaction id "+packageDTO.getTranscationId());
			logger.debug("channel "+packageDTO.getChannel());
			if(packageDTO.getData()!=null && packageDTO.getData()[0]!=null)
			{
				if(packageDTO.getData()[0].getName()!=null && !packageDTO.getData()[0].getName().equalsIgnoreCase("") && packageDTO.getData()[0].getName().equalsIgnoreCase("FEATURE")){
					if(packageDTO.getData()[0].getValue()!=null && !packageDTO.getData()[0].getValue().equalsIgnoreCase("") && packageDTO.getData()[0].getValue().equalsIgnoreCase("PRODUCT_CATALOG")){
						logger.debug("It's product catalog");
					checkFlag=false;
					}
				}
			}
			if(checkFlag){
			//start no need to excute this for product catalog
				logger.debug("This is getpackages");
			if(packageDTO.getSubscriberNumber() == null || packageDTO.getSubscriberNumber().equalsIgnoreCase(""))
			{
				packageInfoDTO = new PackageInfoDTO();
				packageInfoDTO.setTimestamp(packageDTO.getTimestamp());
				packageInfoDTO.setTranscationId(packageDTO.getTranscationId());
				packageInfoDTO.setStatusCode(Cache.getServiceStatusMap().get("SUB_REQ_"+Cache.cacheMap.get("DEFAULT_LANGUAGEID")).getStatusCode());
				packageInfoDTO.setStatusDescription(Cache.getServiceStatusMap().get("SUB_REQ_"+Cache.cacheMap.get("DEFAULT_LANGUAGEID")).getStatusDesc());
				return packageInfoDTO;
			}
			langId=packageDTO.getLanguageId();
			if(langId==null && packageDTO.getSubscriberNumber()!=null)
				//langId = languageDAO.getLanguageID(packageDTO.getSubscriberNumber());
				langId="1";
			packageDTO.setLanguageId(langId); //end  no need to excute this for product catalog
			
		    }
	
			if (packageDTO.getOfferType() == null || packageDTO.getOfferType().equalsIgnoreCase("")) {
				/*packageInfoDTO = new PackageInfoDTO();
				packageInfoDTO.setTimestamp(packageDTO.getTimestamp());
				packageInfoDTO.setTranscationId(packageDTO.getTranscationId());
				packageInfoDTO.setStatusCode(Cache.getServiceStatusMap().get("OFFER_TYPE_REQ_"+langId).getStatusCode());
				packageInfoDTO.setStatusDescription(Cache.getServiceStatusMap().get("OFFER_TYPE_REQ_"+langId).getStatusDesc());
				return packageInfoDTO;*/
				packageDTO.setOfferType("-1");
			}else if (packageDTO.getChannel() == null || packageDTO.getChannel().equalsIgnoreCase("")) {
				packageInfoDTO = new PackageInfoDTO();
				packageInfoDTO.setTimestamp(packageDTO.getTimestamp());
				packageInfoDTO.setTranscationId(packageDTO.getTranscationId());
				packageInfoDTO.setStatusCode(Cache.getServiceStatusMap().get("CHN_REQ_"+langId).getStatusCode());
				packageInfoDTO.setStatusDescription(Cache.getServiceStatusMap().get("CHN_REQ_"+langId).getStatusDesc());
				return packageInfoDTO;
			}
			
			if(Cache.getCacheMap().get("IS_WHITELIST_REQD")!=null && Cache.getCacheMap().get("IS_WHITELIST_REQD").equalsIgnoreCase("true"))
			{
				if(packageDTO.getChannel().equalsIgnoreCase("SMS") || packageDTO.getChannel().equalsIgnoreCase("LMS_WEB") || packageDTO.getChannel().equalsIgnoreCase("USSD"))
				{
					check=subslistDAO.checkSubscriber(packageDTO.getSubscriberNumber());	
					if(check)
					{
					}
					else
					{
						packageInfoDTO = new PackageInfoDTO();
						packageInfoDTO.setStatusCode(Cache.getServiceStatusMap().get("BLACKLIST_"+langId).getStatusCode());
						packageInfoDTO.setStatusDescription(Cache.getServiceStatusMap().get("BLACKLIST_"+langId).getStatusDesc());
						logger.info(Cache.getServiceStatusMap().get("BLACKLIST_"+langId).getStatusDesc());
						return packageInfoDTO;
					}
					
				}
			}
			
			adapter = new LMSWebServiceAdapter();
		if(checkFlag){
			//Calling feature 
				logger.debug("This is getpackages 2");
			genericDTO = adapter.callFeature("GetPackages", packageDTO);//when product catalog comes don't call this feature
			}
			else{
				genericDTO = adapter.callFeature("GetPackages", packageDTO);
				logger.info("This is ProductCatalaog");
			}
		
		packageInfoDTO=(PackageInfoDTO)genericDTO.getObj();
		packageInfoDTO.setTimestamp(packageDTO.getTimestamp());
		packageInfoDTO.setTranscationId(packageInfoDTO.getTranscationId());
		txnId=packageInfoDTO.getTranscationId();
		logger.info("Status code "+packageInfoDTO.getStatusCode());
		if (packageInfoDTO.getStatusCode().equalsIgnoreCase("SC0000")) {
			logger.info("Status code "+genericDTO.getStatusCode());
			packageInfoDTO.setStatusCode(packageInfoDTO.getStatusCode());
			packageInfoDTO.setStatusDescription(packageInfoDTO.getStatusDescription());
		}
		else {
			logger.info("Status code "+packageInfoDTO.getStatusCode());
			logger.info("Transaction id {} failure "+txnId);
			packageInfoDTO.setStatusCode(packageInfoDTO.getStatusCode());
			packageInfoDTO.setStatusDescription(packageInfoDTO.getStatusDescription());
			logger.info("Transaction id {} Description{} "+txnId+packageInfoDTO.getStatusDescription());
		}
		}catch (Exception e) {
			logger.error("Service : GetPackage - Transaction ID :"+txnId,e);
		}finally{
			long t2 = System.currentTimeMillis();
			logger.info("Transaction ID :"+txnId+" MoNumber "+packageDTO.getSubscriberNumber()+" Request Leaving System , Processing Time "+(t2-t1) );

			languageDAO=null;
			adapter=null;
			genericDTO=null;
			subslistDAO=null;
			packageDTO=null;
		}
		
		return packageInfoDTO;
	
	}
	
	public ResponseCodeDTO addPoints(AddPointsDTO addPointsDTO)
	{

		ResponseCodeDTO responseDTO=null;
		//AddPointsBO addPointsBO = null;
		String txnId = null;
		long t1 = System.currentTimeMillis();
		GenericDTO genericDTO = null;
		//Response response = null;
		try{
			genericDTO = new GenericDTO();
			genericDTO.setObj(addPointsDTO);
		//addPointsBO = new AddPointsBO();
		//genericDTO = addPointsBO.buildProcess(genericDTO);
			responseDTO = (ResponseCodeDTO) genericDTO.getObj();
		}catch (Exception e) {
			logger.error("",e);
		}finally{
			long t2 = System.currentTimeMillis();
			logger.info("Transaction ID :"+txnId+" MoNumber "+addPointsDTO.getAccountNumber()+" "
					+ "Request Leaving System , Processing Time "+(t2-t1) );

		}
		
		return responseDTO;
	
	}
	public ResponseDTO rewardPointsCalculation(RewardPointsDTO rewardPointsDTO)
	{
		LMSWebServiceAdapter adapter=null;
		GenericDTO genericDTO=null;
		SubscriberListCheckDAO subslistDAO=null;
		ResponseDTO responseDTO = new ResponseDTO();
		String txnId = null;
		long t1 = System.currentTimeMillis();
		try{
			
			subslistDAO=new SubscriberListCheckDAO();
			txnId = rewardPointsDTO.getTransactionID();
			logger.info("Service : PointCalculation -- Transaction ID :"+txnId+" Channel : " +
					""+rewardPointsDTO.getChannel()+" Subscriber Number : "+rewardPointsDTO.getSubscriberNumber()+": RewardPointsCategory : "+rewardPointsDTO.getRewardPointsCategory()+" Volume : "+rewardPointsDTO.getVolume()
					+" RewardPoints : "+rewardPointsDTO.getRewardPoints()+" StausPoints : "+rewardPointsDTO.getStatusPoints()+": isStatus Point only :"+rewardPointsDTO.isStatusPointsOnly()+" - Request Recieved in System");
			
			
			
			responseDTO.setTranscationId(txnId);
			responseDTO.setTimestamp(rewardPointsDTO.getTimestamp());
			
			if(rewardPointsDTO.getSubscriberNumber()==null||rewardPointsDTO.getSubscriberNumber().trim().equalsIgnoreCase(""))
			{
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("SUB_REQ_"+Cache.defaultLanguageID).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("SUB_REQ_"+Cache.defaultLanguageID).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("SUB_REQ_"+Cache.defaultLanguageID).getStatusDesc());
				return responseDTO;
			}
			
			if(rewardPointsDTO.getChannel()==null||rewardPointsDTO.getChannel().trim().equalsIgnoreCase(""))
			{
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("CHN_REQ_"+Cache.defaultLanguageID).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("CHN_REQ_"+Cache.defaultLanguageID).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("CHN_REQ_"+Cache.defaultLanguageID).getStatusDesc());
				return responseDTO;
			}
			
			if(Cache.getCacheMap().get("IS_WHITELIST_REQD")!=null && Cache.getCacheMap().get("IS_WHITELIST_REQD").equalsIgnoreCase("true"))
			{
				if(rewardPointsDTO.getChannel().equalsIgnoreCase("SMS") || rewardPointsDTO.getChannel().equalsIgnoreCase("LMS_WEB") || rewardPointsDTO.getChannel().equalsIgnoreCase("USSD"))
				{
					check=subslistDAO.checkSubscriber(rewardPointsDTO.getSubscriberNumber());	
					if(check)
					{
					}
					else
					{
						responseDTO.setStatusCode(Cache.getServiceStatusMap().get("BLACKLIST_"+Cache.defaultLanguageID).getStatusCode());
						responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("BLACKLIST_"+Cache.defaultLanguageID).getStatusDesc());
						logger.info(Cache.getServiceStatusMap().get("BLACKLIST_"+Cache.defaultLanguageID).getStatusDesc());
						return responseDTO;
					}
					
				}
			}
			
			
			if(rewardPointsDTO.getRewardPointsCategory()==null||rewardPointsDTO.getRewardPointsCategory()<=0)
			{
				if((rewardPointsDTO.getRewardPoints()==null||rewardPointsDTO.getRewardPoints()<=0)&&(rewardPointsDTO.getStatusPoints()==null||rewardPointsDTO.getStatusPoints()<=0))
				{
					responseDTO.setStatusCode(Cache.getServiceStatusMap().get("REW_CAT_REQ_"+Cache.defaultLanguageID).getStatusCode());
					responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("REW_CAT_REQ_"+Cache.defaultLanguageID).getStatusDesc());
					logger.info(Cache.getServiceStatusMap().get("REW_CAT_REQ_"+Cache.defaultLanguageID).getStatusDesc());
					return responseDTO;
				}
			}
			
			
			if(Cache.rewardPointsCategoryMap.get(rewardPointsDTO.getRewardPointsCategory())==null)
			{
				if((rewardPointsDTO.getRewardPoints()==null||rewardPointsDTO.getRewardPoints()<=0)&&(rewardPointsDTO.getStatusPoints()==null||rewardPointsDTO.getStatusPoints()<=0))
				{
					responseDTO.setStatusCode(Cache.getServiceStatusMap().get("REW_CAT_INVALID_"+Cache.defaultLanguageID).getStatusCode());
					responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("REW_CAT_INVALID_"+Cache.defaultLanguageID).getStatusDesc());
					logger.info(Cache.getServiceStatusMap().get("REW_CAT_INVALID_"+Cache.defaultLanguageID).getStatusDesc());
					
					return responseDTO;
				}
			}
			
			/*if(rewardPointsDTO.isStatusPointsOnly())
			{
				if(rewardPointsDTO.getStatusPoints()==null)
				{
					responseDTO.setStatusCode(Cache.getServiceStatusMap().get("REW_STATUSPOINTS_REQ_"+Cache.defaultLanguageID).getStatusCode());
					responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("REW_STATUSPOINTS_REQ_"+Cache.defaultLanguageID).getStatusDesc());
					logger.info(Cache.getServiceStatusMap().get("REW_STATUSPOINTS_REQ_"+Cache.defaultLanguageID).getStatusDesc());
					
					return responseDTO;
				}
			}*/
			
			/*if(rewardPointsDTO.isStatusPointsOnly()==false&&rewardPointsDTO.getRewardPoints()!=null&&rewardPointsDTO.getStatusPoints()==null)
			{
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("REW_STPOINTS_REQ_"+Cache.defaultLanguageID).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("REW_STPOINTS_REQ_"+Cache.defaultLanguageID).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("REW_STPOINTS_REQ_"+Cache.defaultLanguageID).getStatusDesc());
				
				return responseDTO;
			}
			
			if(rewardPointsDTO.isStatusPointsOnly()==false&&rewardPointsDTO.getRewardPoints()==null&&rewardPointsDTO.getStatusPoints()!=null)
			{
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("REW_RWPOINTS_REQ_"+Cache.defaultLanguageID).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("REW_RWPOINTS_REQ_"+Cache.defaultLanguageID).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("REW_STPOINTS_REQ_"+Cache.defaultLanguageID).getStatusDesc());
				
				return responseDTO;
			}*/
			
			
			if(rewardPointsDTO.getChannel()!=null)
			{
				
				adapter=new LMSWebServiceAdapter();
				
				genericDTO=adapter.callFeature("RewardPointsCalculation",rewardPointsDTO);
				
				if(genericDTO==null)
				{
					responseDTO.setStatusCode(Cache.getServiceStatusMap().get("REW_FAIL_"+Cache.defaultLanguageID).getStatusCode());
					responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("REW_FAIL_"+Cache.defaultLanguageID).getStatusDesc());
					
					return responseDTO;
				}
				
				 if(genericDTO.getStatusCode()!=null){
					
					responseDTO.setStatusCode(genericDTO.getStatusCode());
					responseDTO.setStatusDescription(genericDTO.getStatus());
					
				}else{
					responseDTO.setStatusCode(Cache.getServiceStatusMap().get("REW_FAIL_"+Cache.defaultLanguageID).getStatusCode());
					responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("REW_FAIL_"+Cache.defaultLanguageID).getStatusDesc());
				}
				
				
			}else{
				
				RequestProcessDTO processDTO=new RequestProcessDTO();
				processDTO.setFeatureName("RewardPointsCalculation");
				processDTO.setObject(rewardPointsDTO);
			
				// Adding to Thread pool
				ThreadInitiator.requestProcessThreadPool.addTask(processDTO);
				
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("REW_SUCCESS_"+Cache.defaultLanguageID).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("REW_SUCCESS_"+Cache.defaultLanguageID).getStatusDesc());
			}
		
		}catch (Exception e) {
			logger.error("Service : PointCalculation , Transaction ID : "+txnId
					,e);
		}finally{
			long t2 = System.currentTimeMillis();
			logger.info("Transaction ID :"+txnId+" MoNumber "+rewardPointsDTO.getSubscriberNumber()+" Request Leaving System , Processing Time "+(t2-t1) );

			adapter=null;
			genericDTO=null;
			subslistDAO=null;
			rewardPointsDTO=null;
		}
		return responseDTO;
		
	
	}//rewardsPonitsCalculation
	
	public PointInfoDTO getPointDetails(PointDetailsDTO pointDetailsDTO)
	{
		LanguageDAO languageDAO=null;
		SubscriberListCheckDAO subslistDAO=null;
		PointInfoDTO pointIfnoDTO = new PointInfoDTO();
		LMSWebServiceAdapter adapter=null;
		GenericDTO genericDTO=null;
		String txnId = null;
		long t1 = System.currentTimeMillis();
		try{
			
			languageDAO=new LanguageDAO();
			subslistDAO=new SubscriberListCheckDAO();
			txnId = pointDetailsDTO.getTransactionId();
			
			pointIfnoDTO.setTranscationId(txnId);
			pointIfnoDTO.setTimestamp(pointDetailsDTO.getTimestamp());
			
			logger.info("Service : GetPointDetails  - Transaction ID : "+txnId+" Subscriber Number : "+pointDetailsDTO.getSubscriberNumber()+" Request Recieved in System");
			//logger.info("PIN : "+pointDetailsDTO.getPin());
			//logger.info("From Date : "+pointDetailsDTO.getFromDate()+"  To Date : "+pointDetailsDTO.getToDate());
			//logger.info("No Of Months : "+pointDetailsDTO.getNoOfMonths());
			//logger.info("Offset : "+pointDetailsDTO.getOffSet());
			//logger.info("Limit : "+pointDetailsDTO.getLimit());
//			logger.info("Timestamp : "+pointDetailsDTO.getTimestamp());
					//			logger.info("Channel : "+pointDetailsDTO.getChannel());
					//	logger.info("Language ID : "+pointDetailsDTO.getLanguageID());
					
			String lanID=(pointDetailsDTO.getLanguageID()!=null&&!pointDetailsDTO.getLanguageID().trim().equals("")?pointDetailsDTO.getLanguageID().trim():Cache.defaultLanguageID);
			
			if(pointDetailsDTO.getSubscriberNumber()==null||pointDetailsDTO.getSubscriberNumber().trim().equalsIgnoreCase(""))
			{
				pointIfnoDTO.setStatusCode(Cache.getServiceStatusMap().get("SUB_REQ_"+lanID).getStatusCode());
				pointIfnoDTO.setStatusDescription(Cache.getServiceStatusMap().get("SUB_REQ_"+lanID).getStatusDesc());
				return pointIfnoDTO;
			}
			
			if(pointDetailsDTO.getLanguageID()==null||pointDetailsDTO.getLanguageID().trim().equals(""))
				  lanID=languageDAO.getLanguageID(pointDetailsDTO.getSubscriberNumber());
			
			pointDetailsDTO.setLanguageID(lanID);
			
			if(pointDetailsDTO.getChannel()==null||pointDetailsDTO.getChannel().trim().equalsIgnoreCase(""))
			{
				pointIfnoDTO.setStatusCode(Cache.getServiceStatusMap().get("CHN_REQ_"+lanID).getStatusCode());
				pointIfnoDTO.setStatusDescription(Cache.getServiceStatusMap().get("CHN_REQ_"+lanID).getStatusDesc());
				return pointIfnoDTO;
			}
			
			if(Cache.getCacheMap().get("IS_WHITELIST_REQD")!=null && Cache.getCacheMap().get("IS_WHITELIST_REQD").equalsIgnoreCase("true"))
			{
				if(pointDetailsDTO.getChannel().equalsIgnoreCase("SMS") || pointDetailsDTO.getChannel().equalsIgnoreCase("LMS_WEB") || pointDetailsDTO.getChannel().equalsIgnoreCase("USSD"))
				{
					check=subslistDAO.checkSubscriber(pointDetailsDTO.getSubscriberNumber());	
					if(check)
					{
					}
					else
					{
						pointIfnoDTO.setStatusCode(Cache.getServiceStatusMap().get("BLACKLIST_"+lanID).getStatusCode());
						pointIfnoDTO.setStatusDescription(Cache.getServiceStatusMap().get("BLACKLIST_"+lanID).getStatusDesc());
						logger.info(Cache.getServiceStatusMap().get("BLACKLIST_"+lanID).getStatusDesc());
						return pointIfnoDTO;
					}
					
				}
			}
			
			
			
			adapter=new LMSWebServiceAdapter();
			genericDTO=adapter.callFeature("PointDetails",pointDetailsDTO);
			
			if(genericDTO==null)
			{
				pointIfnoDTO.setStatusCode(Cache.getServiceStatusMap().get("POINTDETAILS_FAILURE_"+lanID).getStatusCode());
				pointIfnoDTO.setStatusDescription(Cache.getServiceStatusMap().get("POINTDETAILS_FAILURE_"+lanID).getStatusDesc());
				
				return pointIfnoDTO;
			}
			
			 if(genericDTO.getStatusCode()!=null){
				
				 pointIfnoDTO.setStatusCode(genericDTO.getStatusCode());
				 pointIfnoDTO.setStatusDescription(genericDTO.getStatus());
				 
				 com.sixdee.imp.dto.PointDetailsDTO detailsDTO=(com.sixdee.imp.dto.PointDetailsDTO)genericDTO.getObj();
				 
				 if(detailsDTO.getExpiryPointsList()!=null)
				 {
					 PointInfoDetailsDTO[] all = new PointInfoDetailsDTO[detailsDTO.getExpiryPointsList().size()];
					 for(int i=0;i<detailsDTO.getExpiryPointsList().size();i++)
					 {
						 PointInfoDetailsDTO detailsDTO2=new PointInfoDetailsDTO();
						 detailsDTO2.setExpiryDate(detailsDTO.getExpiryPointsList().get(i).getExpiryDate());
						 if(detailsDTO.getExpiryPointsList().get(i).getRewardPoints()!=null)
							 detailsDTO2.setPoints(detailsDTO.getExpiryPointsList().get(i).getRewardPoints());
						 else if(detailsDTO.getExpiryPointsList().get(i).getStatusPoints()!=null)
							 detailsDTO2.setPoints(detailsDTO.getExpiryPointsList().get(i).getStatusPoints());
						 
						 all[i]=detailsDTO2;
						 
					 }
					 pointIfnoDTO.setRewardPoints(all);
					 all=null;
				 }
				 
				 if(detailsDTO.getDataSet()!=null&&detailsDTO.getDataSet().size()>0)
				 {
					 pointIfnoDTO.setData(detailsDTO.getDataSet().toArray(new Data[detailsDTO.getDataSet().size()]));
				 }
				 
				
			}else{
				pointIfnoDTO.setStatusCode(Cache.getServiceStatusMap().get("POINTDETAILS_FAILURE_"+lanID).getStatusCode());
				pointIfnoDTO.setStatusDescription(Cache.getServiceStatusMap().get("POINTDETAILS_FAILURE_"+lanID).getStatusDesc());
			}
		}catch (Exception e) {
			logger.error("Service : PointManageMent Transaction ID "+txnId,e);
		}finally{
			long t2 = System.currentTimeMillis();
			logger.info("Transaction ID :"+txnId+" MoNumber "+pointDetailsDTO.getSubscriberNumber()+" Request Leaving System , Processing Time "+(t2-t1) );

			
			languageDAO=null;
			subslistDAO=null;
			adapter=null;
			genericDTO=null;
			pointDetailsDTO=null;
			
		}
		
		return pointIfnoDTO;
		
	}
	
	
	public ResponseDTO transferLine(TransferPointsDTO transferLineDTO){
		ResponseDTO responeDTO =  null;
		String txnId = null;
 		String subscriberNumber= null;
		long t1 = System.currentTimeMillis();
		LMSWebServiceAdapter lmsWebServiceAdapter = null;
		GenericDTO genericDTO = null;
		TransferLineDTO respTransferLineDTO =null;
		String failureStatusCode = "SC1000";
		//String failureDesc		 = "System is busy , Please check after c"
		try{
			txnId = transferLineDTO.getTransactionID();
			subscriberNumber = transferLineDTO.getSubscriberNumber();
			responeDTO = new ResponseDTO();
			responeDTO.setTranscationId(txnId);
			responeDTO.setTimestamp(transferLineDTO.getTimestamp());
			logger.info("Service : TransferLine -- Transaction ID : "+txnId+" SubscriberNumber : "+subscriberNumber+" Request Recieved in System");
			lmsWebServiceAdapter = new LMSWebServiceAdapter();
			
			genericDTO = lmsWebServiceAdapter.callFeature("TransferLine", transferLineDTO);
			if(genericDTO != null){
				respTransferLineDTO = (TransferLineDTO) genericDTO.getObj();
				responeDTO.setStatusCode(respTransferLineDTO.getStatusCode());
				responeDTO.setStatusDescription(respTransferLineDTO.getStatusDesc());
			}else{
				responeDTO.setStatusCode(failureStatusCode);
			}
		
		}catch(Exception e){
			logger.error("Service : TransferLine -- Transaction ID : "+txnId+" Exception occured ",e);
			responeDTO.setStatusCode(failureStatusCode);
		}finally{
			long t2 = System.currentTimeMillis();
			logger.info("Transaction ID :"+txnId+" MoNumber "+transferLineDTO.getSubscriberNumber()+" Request Leaving System , Processing Time "+(t2-t1) );

		}
		return responeDTO;
	}
}
