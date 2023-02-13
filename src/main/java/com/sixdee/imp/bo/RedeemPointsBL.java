package com.sixdee.imp.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sixdee.fw.bo.BOCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.CommonUtil;
import com.sixdee.imp.common.util.LoyaltyTransactionStatus;
import com.sixdee.imp.dao.GetPackagesDAO;
import com.sixdee.imp.dao.RedeemPointsDAO;
import com.sixdee.imp.dao.TableDetailsDAO;
import com.sixdee.imp.dao.TransferPointsDAO;
import com.sixdee.imp.dto.CustomerProfileTabDTO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.LoyaltyTransactionTabDTO;
import com.sixdee.imp.dto.OfferMasterTabDto;
import com.sixdee.imp.dto.OfferNomenclatureTabDto;
import com.sixdee.imp.dto.RedeemPointsDTO;
import com.sixdee.imp.dto.ServiceStatusDTO;
import com.sixdee.imp.service.ProcessExecute;
import com.sixdee.imp.service.ReServices.DAO.TierAndBonusCalculationDAO;
import com.sixdee.imp.service.serviceDTO.common.Data;
import com.sixdee.imp.util.CDRLoggerUtil;
import com.sixdee.imp.utill.DataSet;
import com.sixdee.imp.utill.Param;
import com.sixdee.imp.utill.Request;
import com.sixdee.imp.utill.Response;
import com.sixdee.lms.bo.OnlineRuleInitiatorBO;
import com.sixdee.lms.dto.CDRInformationDTO;
import com.sixdee.lms.util.constant.SystemConstants;
import com.sixdee.lms.util.selections.CDRCommandID;

public class RedeemPointsBL extends BOCommon {
	
	/**
	 * This method is called from BOCommon. Access the DAO object is to create CampaignDefntion
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	
	public GenericDTO buildProcess(GenericDTO genericDTO) throws CommonException 
	{

		logger.info("Class => RedeemPointsBL :: Method => buildProcess()");
		
		RedeemPointsDAO  redeemPointsDAO = null;
		RedeemPointsDTO redeemPointsDTO = null ;
		//TableDetailsDAO tableDetailsDAO;
		CommonUtil commonUtil = new CommonUtil();
		OfferMasterTabDto offerMasterTabDto=null;
		String className =null;
		ProcessExecute processExecute=null;
		ArrayList<String> al = null;
		long tierPoints =0;
		long bonusPoints=0;
		LoyaltyProfileTabDTO loyaltyProfileTabDTO = null;
		LoyaltyTransactionTabDTO loyaltyTransactionTabDTO = null;
		TierAndBonusCalculationDAO tierAndBonusCalculationDAO =null;
		TableDetailsDAO tableDetailsDAO = new TableDetailsDAO();
		boolean process = true;
		CDRInformationDTO cdrInformationDTO = null;
		String key;
		boolean isNotification=false;
		boolean isTierPointsOnly=false;
		GetPackagesDAO getPkgDao=null;
		List<OfferNomenclatureTabDto> offerDetails=null;
		CustomerProfileTabDTO customerProfileTabDTO = null;
		HashMap<String,String> offerAdditionalInfoMap=null;
		double balanceRewardPoints;
		String oldBalance=null;
		ConsumePointsBL consumePointsBL;
		long consumedPoints;
		try
		{
			consumePointsBL=new ConsumePointsBL();
			tierAndBonusCalculationDAO = new TierAndBonusCalculationDAO();
			redeemPointsDTO = (RedeemPointsDTO) genericDTO.getObj(); 
			redeemPointsDAO=new RedeemPointsDAO();
			TransferPointsDAO  transferPointsDAO=new TransferPointsDAO();
			getPkgDao= new GetPackagesDAO();
			logger.info("Transaction id "+redeemPointsDTO.getTransactionId()+" moNumber "+redeemPointsDTO.getMoNumber()+" line number "+redeemPointsDTO.getSubscriberNumber());
			if(redeemPointsDTO.getSubscriberNumber()!=null && !redeemPointsDTO.getSubscriberNumber().equalsIgnoreCase("")){
				if (redeemPointsDTO.getSubscriberNumber().length() > 10 && redeemPointsDTO.getSubscriberNumber().startsWith("1")) {
					String moNumber = redeemPointsDTO.getSubscriberNumber().replaceFirst("1", "");
					redeemPointsDTO.setMoNumber(moNumber);
				}else
				{
				redeemPointsDTO.setMoNumber(redeemPointsDTO.getSubscriberNumber());
				}
			}else
			{
				throw new CommonException("Please pass the subscriber number in the request");
			}
			
			if(process){
	    	Long loyaltyID= transferPointsDAO.getLoyaltyId(redeemPointsDTO.getMoNumber()+"");
	    	logger.info(" Loyalty id "+loyaltyID+" Transaction Id "+redeemPointsDTO.getTransactionId());
	    	if(loyaltyID!=null){
	    		
	    		customerProfileTabDTO = tableDetailsDAO.getCustomerProfile(redeemPointsDTO.getMoNumber()+"");
	    		if(customerProfileTabDTO!=null){
	    			logger.info(">>>status id>>"+customerProfileTabDTO.getStatusId());
	    			if(customerProfileTabDTO.getStatusId()!=null && !((customerProfileTabDTO.getStatusId().equalsIgnoreCase("5")) || (customerProfileTabDTO.getStatusId().equalsIgnoreCase("10")))){

	    		logger.info("Package id Coming "+redeemPointsDTO.getPackageId() +" Transaction id "+redeemPointsDTO.getTransactionId());
	    		offerMasterTabDto=redeemPointsDAO.getOfferDetails(Integer.parseInt(redeemPointsDTO.getPackageId()));
	    		
	    		if(offerMasterTabDto==null)
	    			throw new CommonException("Please pass valid Package");
	    		if(offerMasterTabDto.getRedeemLimit()!=0&&!(offerMasterTabDto.getRedeemLimit()<-1))
	    		{
	    		  
	    		offerAdditionalInfoMap=redeemPointsDAO.getOfferAdditionalInformation(offerMasterTabDto,redeemPointsDTO);
	    		if(offerAdditionalInfoMap==null || offerAdditionalInfoMap.size()==0)
	    			throw new CommonException("Additional offer information not available for this offer");
	    		
	    		redeemPointsDTO.setCustomerProfileTabDTO(customerProfileTabDTO);
	    		redeemPointsDTO.setOfferAddtionalInfoMap(offerAdditionalInfoMap);
			    loyaltyProfileTabDTO = tableDetailsDAO.getLoyaltyProfile(loyaltyID);
			     //For getting offerDetails
			     offerDetails=getPkgDao.getOfferDetailsNomenclatureTab(offerMasterTabDto.getOfferId(), redeemPointsDTO.getDefaultLanguage());
			     if(offerDetails!=null)
			     {
			    	 if(offerDetails.get(0).getOfferName()!=null)
			    	 redeemPointsDTO.setOfferName(offerDetails.get(0).getOfferName());
			    	 /*else
			    	  throw new CommonException("Offer name is not available for this offer");*/ 
			     }
			     logger.info(" Transaction id "+redeemPointsDTO.getTransactionId()+" Reward points"+loyaltyProfileTabDTO.getRewardPoints());
			     logger.info(" Transaction id "+redeemPointsDTO.getTransactionId()+"  offer points"+offerMasterTabDto.getPoints());
			     oldBalance=String.valueOf(loyaltyProfileTabDTO.getRewardPoints());
			     redeemPointsDTO.setOfferMasterTab(offerMasterTabDto);
			     redeemPointsDTO.setRedeemPoints(offerMasterTabDto.getPoints());
			     if(loyaltyProfileTabDTO.getRewardPoints()<offerMasterTabDto.getPoints())
			    	 throw new CommonException("Reward Points is not Enough");
			     
			    	 logger.info("Rule engine validation StatusCode "+redeemPointsDTO.getStatusCode() +"Transaction id "+redeemPointsDTO.getTransactionId());
			    	    logger.info("Transaction id "+redeemPointsDTO.getTransactionId()+" Interface id Coming "+offerMasterTabDto.getInterfaceId());
			    	    if(Cache.getInterfaceDetailsMap().get(offerMasterTabDto.getInterfaceId())!=null && !Cache.getInterfaceDetailsMap().get(offerMasterTabDto.getInterfaceId()).equalsIgnoreCase(""))
			    	    {
			    	    	className=Cache.getInterfaceDetailsMap().get(offerMasterTabDto.getInterfaceId());
			    	    	redeemPointsDTO=identifyUnitType(offerMasterTabDto,redeemPointsDTO);
							logger.info("Transaction id "+redeemPointsDTO.getTransactionId() +" Configured class Name  "+className);
							processExecute = (ProcessExecute)Class.forName(className).newInstance();
							redeemPointsDTO = processExecute.process(redeemPointsDTO);
						 }
			    	    else
			    	    {
			    	    	logger.info("Transaction id "+redeemPointsDTO.getTransactionId() +" Please configure the class name in InterfaceMaster table ");
			    	    	key = "REDEEM_FAIL";
							redeemPointsDTO = customValidation(key, redeemPointsDTO);
			    	    }
							loyaltyTransactionTabDTO = new LoyaltyTransactionTabDTO();
		 			    	loyaltyTransactionTabDTO.setLoyaltyID(loyaltyID);
		 			    	loyaltyTransactionTabDTO.setChannel(redeemPointsDTO.getChannel());
		 			    	loyaltyTransactionTabDTO.setReqTransactionID(redeemPointsDTO.getTransactionId());
		 			    	loyaltyTransactionTabDTO.setAccountNumber(customerProfileTabDTO.getAccountNo());
		 			    	loyaltyTransactionTabDTO.setPreRewardPoints(loyaltyProfileTabDTO.getRewardPoints());
		 			    	if(redeemPointsDTO.getOfferName()!=null)
		 			    	loyaltyTransactionTabDTO.setStatusDesc(Cache.getConfigParameterMap().get("REDEEM_SUCCESS_TXN_DESC").getParameterValue()+redeemPointsDTO.getOfferName());
		 			    	else
		 			    	loyaltyTransactionTabDTO.setStatusDesc(Cache.getConfigParameterMap().get("REDEEM_SUCCESS_TXN_DESC").getParameterValue());
		 			    	loyaltyTransactionTabDTO.setRedeemPoint(Double.parseDouble(offerMasterTabDto.getPoints()+""));
		 			    	loyaltyTransactionTabDTO.setPackageId(offerMasterTabDto.getOfferId());
		 			    	loyaltyTransactionTabDTO.setSubscriberNumber(redeemPointsDTO.getSubscriberNumber());
		 			    	loyaltyTransactionTabDTO.setVoucherOrderID(redeemPointsDTO.getVoucherCode()!=null?redeemPointsDTO.getVoucherCode():null);
			    		  
							logger.info("Transaction id "+redeemPointsDTO.getTransactionId() +" Response Code "+redeemPointsDTO.getStatusCode());
							if(redeemPointsDTO.getStatusCode()!=null&&redeemPointsDTO.getStatusCode().equalsIgnoreCase("SC0000")){
								
								  //String callMethod = "{call UpdateLoyaltyReward_Online(?,?,?,?,?)}";
					    		    //al =  commonUtil.callProcedure(callMethod,loyaltyID+"", offerMasterTabDto.getPoints()+"", 1);
					    		    consumedPoints=consumePointsBL.consumeMyPoints(redeemPointsDTO.getTransactionId(), loyaltyID+"", offerMasterTabDto.getPoints(), isTierPointsOnly);
					    			if(consumedPoints!=0){	
					    				tierPoints=consumedPoints;
					    				bonusPoints=0;
					    			   }else{
					    				   genericDTO.setStatusCode(Cache.getServiceStatusMap().get("REDEEM_FAIL_"+redeemPointsDTO.getDefaultLanguage()).getStatusCode());
							 		    	 genericDTO.setStatus(Cache.getServiceStatusMap().get("REDEEM_FAIL_"+redeemPointsDTO.getDefaultLanguage()).getStatusDesc());
							 		    	throw new CommonException("Redeem points failed");
					    			   }
					    			  if(tierPoints!=0 || bonusPoints!=0){
					 			    	 
					 			    	 if(redeemPointsDAO.updateLoyaltyProfileRedeem(loyaltyProfileTabDTO, Double.valueOf(offerMasterTabDto.getPoints()), Double.valueOf(tierPoints) , Double.valueOf(bonusPoints)))
					 			    	   {
					 			    		 if(offerMasterTabDto.getRedeemLimit()!=-1){
					 			    		redeemPointsDAO.updateOfferDetails(offerMasterTabDto.getOfferId(), offerMasterTabDto.getRedeemLimit());
					 			    		 }isNotification=true;
					 			    		 loyaltyTransactionTabDTO.setCurRewardPoints(loyaltyProfileTabDTO.getRewardPoints()-offerMasterTabDto.getPoints());
					 			    	     loyaltyTransactionTabDTO.setStatusID(LoyaltyTransactionStatus.pointRedeemedforActivationPackage);
					 			    	     logger.info("Transaction id "+redeemPointsDTO.getTransactionId() +"Tier Points "+tierPoints);
					 			    	     if(tierPoints!=0){
					 			    	    loyaltyTransactionTabDTO.setPointType(0);
					 			    	   loyaltyTransactionTabDTO.setRewardPoints(Double.parseDouble(tierPoints+""));
					 			    	    tierAndBonusCalculationDAO.inserTransaction(loyaltyTransactionTabDTO);
					 			    	     }
					 			    	    logger.info("Transaction id "+redeemPointsDTO.getTransactionId() +" Bonus Points "+bonusPoints);
					 			    	   if(bonusPoints!=0){
					 			    		  loyaltyTransactionTabDTO.setPointType(1);
					 			    		 loyaltyTransactionTabDTO.setRewardPoints(Double.parseDouble(bonusPoints+""));
					 			    	    tierAndBonusCalculationDAO.inserTransaction(loyaltyTransactionTabDTO);
					 			    	   }
					 			    	   }else{
					 			    		  loyaltyTransactionTabDTO.setRewardPoints(Double.parseDouble(offerMasterTabDto.getPoints()+""));
					 			    		   loyaltyTransactionTabDTO.setCurRewardPoints(loyaltyProfileTabDTO.getRewardPoints());
					 			    		   loyaltyTransactionTabDTO.setStatusID(LoyaltyTransactionStatus.redeemFailed);
					 			    		   genericDTO.setStatusCode(redeemPointsDTO.getStatusCode()!=null?redeemPointsDTO.getStatusCode():"SC0002");
					 			 		       genericDTO.setStatus(redeemPointsDTO.getStatusDesc()!=null?redeemPointsDTO.getStatusDesc():"Redeem points failed");
					 			 		     tierAndBonusCalculationDAO.inserTransaction(loyaltyTransactionTabDTO);
					 			    	   }
					 			     }else{
					 			    	 genericDTO.setStatusCode(Cache.getServiceStatusMap().get("NOT_ENOUGH_POINTS_"+redeemPointsDTO.getDefaultLanguage()).getStatusCode());
					 	 		    	 genericDTO.setStatus(Cache.getServiceStatusMap().get("NOT_ENOUGH_POINTS_"+redeemPointsDTO.getDefaultLanguage()).getStatusDesc());
					 	 		    	 if(redeemPointsDTO.getOfferName()!=null)
							 			    	loyaltyTransactionTabDTO.setStatusDesc(Cache.getConfigParameterMap().get("REDEEM_FAILED_TXN_DESC").getParameterValue()+redeemPointsDTO.getOfferName());
					 	 		    	 else
					 	 		    		loyaltyTransactionTabDTO.setStatusDesc(Cache.getConfigParameterMap().get("REDEEM_FAILED_TXN_DESC").getParameterValue());
					 	 		    	throw new CommonException("Reward Points is not Enough");
					 			     }
					    	    redeemPointsDTO.setStatusDesc(Cache.getServiceStatusMap().get("REDEEM_POINTS_SUCCESS_"+redeemPointsDTO.getDefaultLanguage()).getStatusDesc());
								genericDTO.setStatusCode(redeemPointsDTO.getStatusCode());
						    	genericDTO.setStatus(redeemPointsDTO.getStatusDesc());
						    	
							}
							else{
								   loyaltyTransactionTabDTO.setRewardPoints(Double.parseDouble(offerMasterTabDto.getPoints()+""));
					    		   loyaltyTransactionTabDTO.setStatusID(LoyaltyTransactionStatus.redeemFailed);
					    		   if(redeemPointsDTO.getOfferName()!=null)
					 			    	loyaltyTransactionTabDTO.setStatusDesc(Cache.getConfigParameterMap().get("REDEEM_FAILED_TXN_DESC").getParameterValue()+redeemPointsDTO.getOfferName());
					    		   else
					    			   loyaltyTransactionTabDTO.setStatusDesc(Cache.getConfigParameterMap().get("REDEEM_FAILED_TXN_DESC").getParameterValue());
					    		   genericDTO.setStatusCode(redeemPointsDTO.getStatusCode()!=null?redeemPointsDTO.getStatusCode():Cache.getServiceStatusMap().get("REDEEM_FAIL_"+redeemPointsDTO.getDefaultLanguage()).getStatusCode());
		 			 		       genericDTO.setStatus(redeemPointsDTO.getStatusDesc()!=null?redeemPointsDTO.getStatusDesc():Cache.getServiceStatusMap().get("REDEEM_FAIL_"+redeemPointsDTO.getDefaultLanguage()).getStatusDesc());
					 		       redeemPointsDTO.setStatusDesc(genericDTO.getStatus());
					 		       tierAndBonusCalculationDAO.inserTransaction(loyaltyTransactionTabDTO);	
							}}else{
								logger.info("Transaction id "+redeemPointsDTO.getTransactionId()+"Offer limit over insert in offermaster table");
								redeemPointsDTO.setStatusCode(Cache.getServiceStatusMap().get("OFFER_LIMIT_OVER_"+redeemPointsDTO.getDefaultLanguage()).getStatusCode());
					    		redeemPointsDTO.setStatusDesc(Cache.getServiceStatusMap().get("OFFER_LIMIT_OVER_"+redeemPointsDTO.getDefaultLanguage()).getStatusDesc());
					    	
							}
	    			}else{
	    				 if(customerProfileTabDTO.getStatusId().equalsIgnoreCase("5")){
	    					 redeemPointsDTO.setStatusCode(Cache.getServiceStatusMap().get("CUSTOMER_IN_SOFT_DELETE_"+redeemPointsDTO.getDefaultLanguage()).getStatusCode());
	    			         redeemPointsDTO.setStatusDesc(Cache.getServiceStatusMap().get("CUSTOMER_IN_SOFT_DELETE_"+redeemPointsDTO.getDefaultLanguage()).getStatusDesc());
	    			     }else if(customerProfileTabDTO.getStatusId().equalsIgnoreCase("10")){
	    			    	 redeemPointsDTO.setStatusCode(Cache.getServiceStatusMap().get("CUSTOMER_IS_IN_FRAUD_"+redeemPointsDTO.getDefaultLanguage()).getStatusCode());
	    			         redeemPointsDTO.setStatusDesc(Cache.getServiceStatusMap().get("CUSTOMER_IS_IN_FRAUD_"+redeemPointsDTO.getDefaultLanguage()).getStatusDesc());
	    			   }
	    			}
	    	     }	else{
	 	    		redeemPointsDTO.setStatusCode(Cache.getServiceStatusMap().get("USER_NOT_EXIST_"+redeemPointsDTO.getDefaultLanguage()).getStatusCode());
		    		redeemPointsDTO.setStatusDesc(Cache.getServiceStatusMap().get("USER_NOT_EXIST_"+redeemPointsDTO.getDefaultLanguage()).getStatusDesc());
		    	}  
				}
	    	else{
	    		redeemPointsDTO.setStatusCode(Cache.getServiceStatusMap().get("USER_NOT_EXIST_"+redeemPointsDTO.getDefaultLanguage()).getStatusCode());
	    		redeemPointsDTO.setStatusDesc(Cache.getServiceStatusMap().get("USER_NOT_EXIST_"+redeemPointsDTO.getDefaultLanguage()).getStatusDesc());
	    	}
			}
	    	genericDTO.setStatusCode(redeemPointsDTO.getStatusCode());
	    	genericDTO.setStatus(redeemPointsDTO.getStatusDesc());
	    	genericDTO.setObj(redeemPointsDTO);
		}	
		catch (Exception e) 
		{
			if(genericDTO.getStatusCode()==null)
				genericDTO.setStatusCode("SC0002");
			if(genericDTO.getStatus()==null)
				genericDTO.setStatus(e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			cdrInformationDTO=buildingRedemptionCdrDto(genericDTO,redeemPointsDTO,loyaltyProfileTabDTO,customerProfileTabDTO,loyaltyTransactionTabDTO,oldBalance);
			CDRLoggerUtil.flushFatalCDR(cdrInformationDTO);
			genericDTO.setObj(redeemPointsDTO);
			logger.info("Notification to bulk api");
			if(genericDTO!=null && genericDTO.getStatusCode()!=null && genericDTO.getStatusCode().equalsIgnoreCase("SC0000"))
			{
			notifyRedemption(redeemPointsDTO,loyaltyTransactionTabDTO);
			}
			  redeemPointsDAO = null;
			 redeemPointsDTO = null ;
			//TableDetailsDAO tableDetailsDAO;
			 commonUtil = null;
			 offerMasterTabDto=null;
			 className =null;
			 processExecute=null;
			 al = null;
			 tierPoints =0;
			 bonusPoints=0;
			 loyaltyProfileTabDTO = null;
			 loyaltyTransactionTabDTO = null;
			 tierAndBonusCalculationDAO = null;
			 tableDetailsDAO = null;
			 process = true;
			 cdrInformationDTO = null;
			 key=null;
			 isNotification=false;
			 getPkgDao=null;
			 offerDetails=null;
			 customerProfileTabDTO = null;
			offerAdditionalInfoMap=null;
			 balanceRewardPoints=0.0;
			 oldBalance=null;
				}
		return genericDTO;
	}
	
	private RedeemPointsDTO ruleEngineValidation(RedeemPointsDTO redeemPointsDTO, Long loyaltyID, OfferMasterTabDto masterTabDto,
			GenericDTO genericDTO) {

		Request request = new Request();
		String key;
		try {
			request.setRequestId(redeemPointsDTO.getTransactionId());
			request.setTimeStamp(redeemPointsDTO.getTimestamp());
			request.setKeyWord("Redeem");
			request.setMsisdn(loyaltyID + "");// loyalty_id in msisdn tag of req
			DataSet dataSet = new DataSet();
			ArrayList<Param> parameter1 = new ArrayList<>();
			parameter1.add(new Param(SystemConstants.MSISDN, redeemPointsDTO.getSubscriberNumber() + ""));
			parameter1.add(new Param(SystemConstants.LOYALTY_ID, loyaltyID + ""));
			parameter1.add(new Param(SystemConstants.POINTS, masterTabDto.getPoints() + ""));
			dataSet.setParameter1(parameter1);
			request.setDataSet(dataSet);
			genericDTO.setObj(request);
			genericDTO = new OnlineRuleInitiatorBO().executeBusinessProcess(genericDTO);
			Response response = (Response) genericDTO.getObj();
			int status = 0;
			if (response != null) {
				logger.info("Transaction id "+redeemPointsDTO.getTransactionId() +"Response code from RE " + response.getRespCode());
				if (response != null && response.getRespCode().equalsIgnoreCase("SC0000")) {
					if(response.getDataSets()!=null){
		    			ArrayList<DataSet> dataSet1 =response.getDataSets().getDataSet1();
		    			if(dataSet1!=null){
		    				ArrayList<Param> paramlist = dataSet1.get(0).getParameter1();
		    				for(Param p: paramlist){
		    					if(p.getId().equalsIgnoreCase("STATUS")){
		    						logger.info("Transaction id "+redeemPointsDTO.getTransactionId() +" status from Rule engine "+p.getValue());
		    						status = Integer.parseInt(p.getValue());
		    						break;
		    					}
		    				}
		    				if(status==1)
			    			{
		    					logger.info("Transaction id "+redeemPointsDTO.getTransactionId() +" validation success from Rule engine "+status);
		    					key="RE_SUCCESS";
		    					redeemPointsDTO=customValidation(key,redeemPointsDTO);
			    			}else{
		    					for(Param p: paramlist){
			    					if(p.getId().equalsIgnoreCase("DESCRIPTION")){
			    						logger.info("Transaction id "+redeemPointsDTO.getTransactionId() +" failure Description from Rule engine "+p.getValue());
			    						redeemPointsDTO=customValidation(p.getValue().trim(),redeemPointsDTO);
			    						break;
			    					}
			    				}
		    				}
		    			}
		    		}
					else
					{
						key="RE_FAILURE";
						logger.info("Transaction id "+redeemPointsDTO.getTransactionId() + " failure not getting DataSets response frome Rule engine");
						redeemPointsDTO=customValidation(key,redeemPointsDTO);
					}
				}else
				{
					key="RE_FAILURE";
					logger.info("Transaction id "+redeemPointsDTO.getTransactionId() + " getting failure response frome Rule engine "+response.getRespCode());
					redeemPointsDTO=customValidation(key,redeemPointsDTO);
				}
			}
			else
			{
				key="RE_FAILURE";
				logger.info("Transaction id "+redeemPointsDTO.getTransactionId() + " not getting response frome Rule engine");
				redeemPointsDTO=customValidation(key,redeemPointsDTO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Transaction id "+redeemPointsDTO.getTransactionId()+" exception "+e.getMessage());
		}
		return redeemPointsDTO;
	}
	
	public RedeemPointsDTO generatedVoucherResponse(RedeemPointsDTO redeemPointsDTO,String voucherCode,String expiryDate,RedeemPointsBL redeemPointBl) throws CommonException
	{
		String key=null;
		try {
			Data[] data = null;
			Data dataNameValue = null;
			if (redeemPointsDTO != null && voucherCode != null) {
				data = new Data[3];
				dataNameValue = new Data();
				dataNameValue.setName(SystemConstants.VOUCHER);
				dataNameValue.setValue(voucherCode);
				redeemPointsDTO.setVoucherCode(voucherCode);
				data[0] = dataNameValue;
				dataNameValue = new Data();
				dataNameValue.setName(SystemConstants.VOUCHER_EXPIRY_DATE);
				dataNameValue.setValue(expiryDate);
				data[1] = dataNameValue;
				dataNameValue = new Data();
				dataNameValue.setName(SystemConstants.MSG_CAUSE);
				dataNameValue.setValue(SystemConstants.msgCauseVoucherOffersSuccess);
				data[2] = dataNameValue;
				key = "REDEEM_SUCCESS";
				redeemPointsDTO = customValidation(key, redeemPointsDTO);
				redeemPointsDTO.setData(data);
			} else {
				key = "REDEEM_FAIL";
				redeemPointsDTO = customValidation(key, redeemPointsDTO);
			}

		} catch (Exception e) {
			key = "REDEEM_FAIL";
			redeemPointsDTO = customValidation(key, redeemPointsDTO);
			logger.info("Transaction id "+redeemPointsDTO.getTransactionId()+" exception "+e.getMessage());
			e.printStackTrace();
		}
		
		return redeemPointsDTO;
	}
	
	public CDRInformationDTO buildingRedemptionCdrDto(GenericDTO genericDTO,RedeemPointsDTO redeemPointDto,LoyaltyProfileTabDTO loyaltyProfileTabDTO,CustomerProfileTabDTO customerProfileTabDTOSubsNumber,LoyaltyTransactionTabDTO loyaltyTransactionTabDTO,String oldBalance)
	{
		CDRInformationDTO cdrInformationDTO = null;
		String key;
		try {
			cdrInformationDTO = new CDRInformationDTO();
			cdrInformationDTO.setCommandId(CDRCommandID.Redemption.getId());
			if (genericDTO != null) {
				if (genericDTO.getStatusCode() != null)
				{
					if(genericDTO.getStatusCode().equalsIgnoreCase("SC0000"))
					{
					key="REDEEM_POINTS_SUCCESS";
					redeemPointDto = customValidation(key, redeemPointDto);
					cdrInformationDTO.setStatusCode(redeemPointDto.getStatusCode());
					cdrInformationDTO.setStatusDescription(redeemPointDto.getStatusDesc());
					}else
					{
						key="REDEEM_FAIL";
						redeemPointDto = customValidation(key, redeemPointDto);
						cdrInformationDTO.setStatusCode(redeemPointDto.getStatusCode());
						cdrInformationDTO.setStatusDescription(redeemPointDto.getStatusDesc());
						
					}
				}
			} else {
				key="REDEEM_FAIL";
				redeemPointDto = customValidation(key, redeemPointDto);
				cdrInformationDTO.setStatusCode(redeemPointDto.getStatusCode());
				cdrInformationDTO.setStatusDescription(redeemPointDto.getStatusDesc());
			}
			if (redeemPointDto != null) {
				if (redeemPointDto.getTransactionId() != null)
					cdrInformationDTO.setTransactionId(redeemPointDto.getTransactionId());
				if (redeemPointDto.getVoucherCode() != null)
					cdrInformationDTO.setVoucherNumber(redeemPointDto.getVoucherCode());
				if (redeemPointDto.getPackageId() != null)
					cdrInformationDTO.setOfferId(redeemPointDto.getPackageId());
				if (redeemPointDto.getChannel() != null)
					cdrInformationDTO.setChannelID(redeemPointDto.getChannel());
				if (redeemPointDto.getMoNumber() != null)
					cdrInformationDTO.setSubscriberNumber(redeemPointDto.getMoNumber());
				if (redeemPointDto.getSubscriberNumber() != null)
					cdrInformationDTO.setDestSubscriberNumber(redeemPointDto.getSubscriberNumber());
				if (redeemPointDto.getLoyaltyId() != null)
					cdrInformationDTO.setLoyaltyId(String.valueOf(redeemPointDto.getLoyaltyId()));
				if (redeemPointDto.getCustAccountNumber() != null)
					cdrInformationDTO.setDestLoyaltyId(redeemPointDto.getCustAccountNumber());
				if (redeemPointDto.getOfferMasterTab().getMerchantId()!=0)
					cdrInformationDTO.setMerchantId(String.valueOf(redeemPointDto.getOfferMasterTab().getMerchantId()));
				if (redeemPointDto.getOfferName()!=null)
					cdrInformationDTO.setField9(redeemPointDto.getOfferName());
				
					cdrInformationDTO.setField10(String.valueOf(redeemPointDto.getOfferMasterTab().getPoints()));
			}
			if (loyaltyProfileTabDTO != null) {
				if (loyaltyProfileTabDTO.getBonusPoints() != null)
					cdrInformationDTO.setBonusPoints(loyaltyProfileTabDTO.getBonusPoints());
				if (loyaltyProfileTabDTO.getTierPoints() != null)
					cdrInformationDTO.setTierPoints(loyaltyProfileTabDTO.getTierPoints());
				if (loyaltyProfileTabDTO.getTierId() != null)
				{
					cdrInformationDTO.setPreviousTier(loyaltyProfileTabDTO.getTierId());
					cdrInformationDTO.setCurrentTier(loyaltyProfileTabDTO.getTierId());
				}
				if (loyaltyProfileTabDTO.getAccountNumber() != null)
					cdrInformationDTO.setAccountNumber(loyaltyProfileTabDTO.getAccountNumber());
					cdrInformationDTO.setLoyaltyId(String.valueOf(loyaltyProfileTabDTO.getLoyaltyID()));
					if(loyaltyProfileTabDTO.getAccountType()!=null)
					cdrInformationDTO.setSubscriberType(loyaltyProfileTabDTO.getAccountType());
					if(loyaltyProfileTabDTO.getCategory()!=null)
					cdrInformationDTO.setCategoryDesc(loyaltyProfileTabDTO.getCategory());
			}
			if(customerProfileTabDTOSubsNumber!=null)
			{
			if(customerProfileTabDTOSubsNumber.getAccountNo()!=null)
			cdrInformationDTO.setAccountNumber(customerProfileTabDTOSubsNumber.getAccountNo());
			}
			if(loyaltyTransactionTabDTO!=null)
			{
				if(loyaltyTransactionTabDTO.getCurRewardPoints()!=null)
				cdrInformationDTO.setCurrentBalance(String.valueOf(loyaltyTransactionTabDTO.getCurRewardPoints()));
				cdrInformationDTO.setOldBalance(oldBalance);
			}
		} catch (Exception e) {
			logger.info("Transaction id "+redeemPointDto.getTransactionId()+" exception "+e.getMessage());
			e.printStackTrace();
			
		}
		return cdrInformationDTO;
	}
	
	public RedeemPointsDTO customValidation(String key,RedeemPointsDTO redeemPointsDTO) throws CommonException
	{
		ServiceStatusDTO actionServiceDetailsDTO = null;
		try
		{
			logger.info(">>>>>>>>>>>>>>>>Language id"+redeemPointsDTO.getDefaultLanguage()+">>>>>>>>>>>>>jkey"+key);
		if((actionServiceDetailsDTO = Cache.getServiceStatusMap().get(key+"_"+redeemPointsDTO.getDefaultLanguage()))!=null){
			redeemPointsDTO.setStatusCode(actionServiceDetailsDTO.getStatusCode());
			redeemPointsDTO.setStatusDesc(actionServiceDetailsDTO.getStatusDesc());
		}else
		{
			logger.warn(" Service : CreateLoyalty -- Transaction ID : "+redeemPointsDTO.getTransactionId()+" MoNumber "+redeemPointsDTO.getMoNumber()+""+ " No Key Defined in Service Status table for Key :- "+key);
			redeemPointsDTO.setStatusCode("SC0001");
			redeemPointsDTO.setStatusDesc(key);	
		}
		}catch(Exception e)
		{
			logger.info("Transaction id "+redeemPointsDTO.getTransactionId()+" exception "+e.getMessage());
			e.printStackTrace();	
		}
		return redeemPointsDTO;
	}
	
	public void notifyRedemption(RedeemPointsDTO redeemPointsDTO,LoyaltyTransactionTabDTO loyaltyTransactionTabDTO)
	{
		CommonUtil commonUtil = null;
		HashMap<String, String> notificationMap = null;
		try {
			commonUtil = new CommonUtil();
			notificationMap = new HashMap<String, String>();
			if(redeemPointsDTO.getOfferMasterTab().getOfferTypeId()==Integer.parseInt(Cache.cacheMap.get("OFFER_TYPE"))){
			notificationMap.put(SystemConstants.MSG_CAUSE,SystemConstants.voucherRedemption);
			}
			else{	
				notificationMap.put(SystemConstants.MSG_CAUSE,SystemConstants.msgRedeemSuccessCause);
			}
			notificationMap.put(SystemConstants.MSISDN, redeemPointsDTO.getMoNumber());
			notificationMap.put(SystemConstants.cost, String.valueOf(redeemPointsDTO.getOfferMasterTab().getCost()));
			notificationMap.put(SystemConstants.redeemPoints,String.valueOf(redeemPointsDTO.getOfferMasterTab().getPoints()));
			notificationMap.put(SystemConstants.offerName, redeemPointsDTO.getOfferName());
			notificationMap.put(SystemConstants.remainingBalancePoints,(loyaltyTransactionTabDTO.getCurRewardPoints()+"").replaceAll(".0*$",""));
			notificationMap.put(SystemConstants.offerId,String.valueOf(redeemPointsDTO.getOfferMasterTab().getOfferId()));
			notificationMap.put(SystemConstants.langId,String.valueOf(redeemPointsDTO.getDefaultLanguage()));
			commonUtil.generateNotifyRequest(redeemPointsDTO.getTransactionId(), "NotifyCustomer",redeemPointsDTO.getMoNumber(), notificationMap);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Transaction ID " + redeemPointsDTO.getTransactionId() + "Exception " + e.getMessage());
		} finally {
			commonUtil = null;
			notificationMap = null;
		}
	}	
	
	public RedeemPointsDTO identifyUnitType(OfferMasterTabDto offerMasterTabDto,RedeemPointsDTO redeemPointsDTO)
	{
		String unitType=null;
		int interfaceId=offerMasterTabDto.getInterfaceId();
		try
		{
			if(interfaceId==1 || interfaceId==5)
			{
				unitType=Cache.getConfigParameterMap().get("REDEMPTION_UNIT_TYPE_DATA").getParameterValue();
				offerMasterTabDto.setCost(String.valueOf(Long.valueOf(offerMasterTabDto.getCost().trim())*Long.valueOf(Cache.getConfigParameterMap().get("REDEMPTION_MULTIPLIER_DATA_TYPE").getParameterValue())));  
			}
			else if(interfaceId==2 || interfaceId==6)
			{
				unitType=Cache.getConfigParameterMap().get("REDEMPTION_UNIT_TYPE_VOICE").getParameterValue();
			offerMasterTabDto.setCost(String.valueOf(Long.valueOf(offerMasterTabDto.getCost().trim())*Long.valueOf(Cache.getConfigParameterMap().get("REDEMPTION_MULTIPLIER_VOICE_TYPE").getParameterValue())));  
			}
			else if(interfaceId==3 || interfaceId==7)
			{
				unitType=Cache.getConfigParameterMap().get("REDEMPTION_UNIT_TYPE_INTERNATIONAL_VOICE").getParameterValue();
				offerMasterTabDto.setCost(String.valueOf(Long.valueOf(offerMasterTabDto.getCost().trim())*Long.valueOf(Cache.getConfigParameterMap().get("REDEMPTION_MULTIPLIER_INTERN_VOICE_TYPE").getParameterValue())));  
			}
			else if(interfaceId==4)
			{
				offerMasterTabDto.setCost(String.valueOf(Long.valueOf(offerMasterTabDto.getCost().trim())*Long.valueOf(Cache.getConfigParameterMap().get("PREPAID_MULTIPLIER_REDEMPTION_TYPE_MONEY").getParameterValue())));
				unitType=Cache.getConfigParameterMap().get("PREPAID_REDEMPTION_UNIT_TYPE_MONEY").getParameterValue();
				  
			}else if(interfaceId==8)
			{
				unitType=Cache.getConfigParameterMap().get("POSTPAID_REDEMPTION_UNIT_TYPE_MONEY").getParameterValue();
				offerMasterTabDto.setCost(String.valueOf(Long.valueOf(offerMasterTabDto.getCost().trim())*Long.valueOf(Cache.getConfigParameterMap().get("POSTPAID_MULTIPLIER_REDEMPTION_TYPE_MONEY").getParameterValue()))); 
			
			}
			if(unitType!=null)
			{
				redeemPointsDTO.setUnitType(unitType);
			}
			redeemPointsDTO.setOfferMasterTab(offerMasterTabDto);
		}catch(Exception e)
		{
			logger.info("Exception e "+e.getMessage());
			e.printStackTrace();
		}
		return redeemPointsDTO;
	}
	
	public static void main(String arg[])
	{
		int cost=2100;
		int multiplier=1048576;
		String costData="";
		costData=String.valueOf(Integer.valueOf(cost)*Integer.valueOf(multiplier));
		System.out.println(costData);
	}
}
