package com.sixdee.lms.bo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.CommonUtil;
import com.sixdee.imp.common.util.LoyaltyTransactionStatus;
import com.sixdee.imp.dao.RedeemPointsDAO;
import com.sixdee.imp.dao.TableDetailsDAO;
import com.sixdee.imp.dto.CustomerProfileTabDTO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.LoyaltyTransactionTabDTO;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;
import com.sixdee.imp.dto.parser.RERequestDataSet;
import com.sixdee.imp.dto.parser.RERequestHeader;
import com.sixdee.imp.dto.parser.REResponseDataSet;
import com.sixdee.imp.dto.parser.REResponseHeader;
import com.sixdee.imp.dto.parser.ReResponseParameter;
import com.sixdee.imp.service.ReServices.BL.TierAndBonusPointCalculationBL;
import com.sixdee.imp.service.ReServices.DAO.TierAndBonusCalculationDAO;
import com.sixdee.imp.util.CDRLoggerUtil;
import com.sixdee.lms.dto.CDRInformationDTO;
import com.sixdee.lms.serviceInterfaces.BusinessLogics;
import com.sixdee.lms.util.constant.SystemConstants;
import com.sixdee.lms.util.selections.CDRCommandID;

public class PointExpiryBO implements BusinessLogics{

	private static final Logger logger = Logger.getLogger(TierAndBonusPointCalculationBL.class);
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	@Override
	public GenericDTO executeBusinessProcess(GenericDTO genericDTO) {
		TableDetailsDAO tableDetailsDAO = new TableDetailsDAO();
		String requestId = null;
		String timeStamp = null;
		RERequestDataSet dataSet = null;
		ArrayList<ReResponseParameter> paramlist = null;
		String msisdn = null;
		SubscriberNumberTabDTO subscriberNumberTabDTO = null;
		LoyaltyProfileTabDTO loyaltyProfileTabDTO = null;
		REResponseHeader response =null;
		RERequestHeader request = null;
		CommonUtil commonUtil = new CommonUtil();
		 String points = null;
		 String loyaltyId = null;
		 ArrayList<String> al = null;
		 String tierPoints= null;
		 String bonuspoints = null;
		 LoyaltyTransactionTabDTO loyaltyTransactionTabDTO = null;
		 String exp =null;
		 Date expiryDate = null;
		 CustomerProfileTabDTO customerProfileTabDTO = null;
		try{
			loyaltyTransactionTabDTO=new LoyaltyTransactionTabDTO();
			request = (RERequestHeader) genericDTO.getObj();
			if(request!=null){
				requestId = request.getRequestId();
				timeStamp = request.getTimeStamp();
				msisdn = request.getMsisdn();
				dataSet = request.getDataSet();
				if(dataSet!=null){
					paramlist = dataSet.getResponseParam();
					for(ReResponseParameter p : paramlist){
						if(p.getId().equalsIgnoreCase("MSISDN"))
							msisdn = p.getValue();
						
						if(p.getId().equalsIgnoreCase("LOYALTY_ID"))
							loyaltyId = p.getValue();
						
						if(p.getId().equalsIgnoreCase("POINTS")){
							points = p.getValue();
						}
						
					}
				}
				
			   logger.info("TransactionId "+requestId+" msisdn ::"+msisdn+" LoyaltyId::"+loyaltyId+" Points::"+points);
			   
			         if(loyaltyId!=null && !(loyaltyId.equalsIgnoreCase("")))
			        	 loyaltyProfileTabDTO = tableDetailsDAO.getLoyaltyProfile(Long.parseLong(loyaltyId));
			          
			         if(loyaltyProfileTabDTO==null)
			        	 if(msisdn!=null && !(msisdn.equalsIgnoreCase("")))
			        		 subscriberNumberTabDTO = tableDetailsDAO.getSubscriberNumberDetails(Long.parseLong(msisdn));
			         
			         if(subscriberNumberTabDTO!=null)
			        	 loyaltyProfileTabDTO = tableDetailsDAO.getLoyaltyProfile(subscriberNumberTabDTO.getLoyaltyID());
			         
			         if(loyaltyProfileTabDTO!=null && Double.parseDouble(points)>0){
			        	 if(loyaltyProfileTabDTO.getRewardPoints()>=Double.parseDouble(points)){
			        	 String callMethod = "{call UpdateLoyaltyReward_Online(?,?,?,?,?)}";
			    		    al =  commonUtil.callProcedure(callMethod, loyaltyProfileTabDTO.getLoyaltyID()+"", points, 1);
			    			if(al!=null && (!al.get(0).equalsIgnoreCase("0") || !al.get(1).equalsIgnoreCase("0"))){	
			    				tierPoints = al.get(0);
			    				bonuspoints = al.get(1);
			    				RedeemPointsDAO redeemPointsDAO = new RedeemPointsDAO();
			    			if(redeemPointsDAO.updateLoyaltyProfileRedeem(loyaltyProfileTabDTO,Double.valueOf(points), Double.valueOf(tierPoints), Double.valueOf(bonuspoints)))
				    			{
			    				customerProfileTabDTO = tableDetailsDAO.getCustomerProfile(msisdn);
			    				    response = new REResponseHeader();
									response.setRequestId(requestId);
									response.setMsisdn(msisdn);
									response.setTimeStamp(request.getTimeStamp());
									
									REResponseDataSet reResponseDataSet = new REResponseDataSet();
									reResponseDataSet.setParameterList(paramlist);
									response.setDataSet(reResponseDataSet);
									response.setStatus("SC0000");
									response.setStatusDesc("SUCCESS");
									logger.info("Capturing transaction for pointExpiry");
									loyaltyTransactionTabDTO.setReqTransactionID(requestId);
									loyaltyTransactionTabDTO.setSubscriberNumber(msisdn);
									loyaltyTransactionTabDTO.setChannel("-1");
									if(loyaltyProfileTabDTO.getLoyaltyID()!=null)
									loyaltyTransactionTabDTO.setLoyaltyID(loyaltyProfileTabDTO.getLoyaltyID());
									if(customerProfileTabDTO!=null && customerProfileTabDTO.getAccountNo()!=null)
									loyaltyTransactionTabDTO.setAccountNumber(customerProfileTabDTO.getAccountNo());
									loyaltyTransactionTabDTO.setExpiryPoints(Double.valueOf(points));
									exp = sdf.format(new Date());
								    expiryDate = sdf.parse(exp);
								    loyaltyTransactionTabDTO.setExpiryDate(expiryDate);
									loyaltyTransactionTabDTO.setCurRewardPoints(loyaltyProfileTabDTO.getRewardPoints()-Double.valueOf(points));
									loyaltyTransactionTabDTO.setStatusID(LoyaltyTransactionStatus.pointExpiry);
									loyaltyTransactionTabDTO.setPointType(0);
									loyaltyTransactionTabDTO.setRewardPoints(loyaltyProfileTabDTO.getRewardPoints());
									loyaltyTransactionTabDTO.setStatusDesc(Cache.getConfigParameterMap().get("POINT_EXPIRED_TXN_STATUS_DESC").getParameterValue());
									boolean isCaptured=captureTransactionPointExpiry(loyaltyTransactionTabDTO);
									logger.info("PointExpiryTransactionCaptured status "+isCaptured);
									notifyRedemption(msisdn,points,requestId);
				    			}
			    			}else{
			    				response = new REResponseHeader();
			    	    	    response.setRequestId(requestId);
			    				response.setMsisdn(msisdn);
			    				response.setTimeStamp(request.getTimeStamp());
			    				
			    				REResponseDataSet reResponseDataSet = new REResponseDataSet();
			    				reResponseDataSet.setParameterList(paramlist);
			    				response.setDataSet(reResponseDataSet);
			    				response.setStatus("SC0001");
			    				response.setStatusDesc("FAILED");
			    			}
			        	 }else{
			        		 logger.info("TransactionId "+requestId+" not Enough points for loyalty id::"+loyaltyId);
							    response = new REResponseHeader();
								response.setRequestId(requestId);
								response.setMsisdn(msisdn);
								response.setTimeStamp(request.getTimeStamp());
								REResponseDataSet reResponseDataSet = new REResponseDataSet();
								response.setDataSet(reResponseDataSet);
								response.setStatus("SC0001");
								response.setStatusDesc("USER DOESNOT EXIST");
			        	 }
			         }else{
			        	 logger.info("TransactionId "+requestId+" No such user with msisdn ::"+msisdn +"or loyalty id::"+loyaltyId);
						    response = new REResponseHeader();
							response.setRequestId(requestId);
							response.setMsisdn(msisdn);
							response.setTimeStamp(request.getTimeStamp());
							REResponseDataSet reResponseDataSet = new REResponseDataSet();
							response.setDataSet(reResponseDataSet);
							response.setStatus("SC0001");
							response.setStatusDesc("USER DOESNOT EXIST");
			         }
						   
			 }	
			}catch(Exception e){
				logger.info("TransactionId "+e.getMessage());
				e.printStackTrace();
				response = new REResponseHeader();
	    	    response.setRequestId(requestId);
				response.setMsisdn(msisdn);
				response.setTimeStamp(request.getTimeStamp());
				
				REResponseDataSet reResponseDataSet = new REResponseDataSet();
				reResponseDataSet.setParameterList(paramlist);
				response.setDataSet(reResponseDataSet);
				response.setStatus("SC0001");
				response.setStatusDesc("FAILED");
			}finally{
				genericDTO.setObj(response);
				buildingCdrDtoAndWritingCdr(response,msisdn,loyaltyId,points,requestId);
				
				
				 tableDetailsDAO = null;
				 requestId = null;
				 timeStamp = null;
				 dataSet = null;
				 paramlist = null;
				 msisdn = null;
				 subscriberNumberTabDTO = null;
				 loyaltyProfileTabDTO = null;
				 request = null;
				 commonUtil = null;
				  points = null;
				  loyaltyId = null;
				  al = null;
				  tierPoints= null;
				  bonuspoints = null;
				  loyaltyTransactionTabDTO = null;
				  expiryDate = null;
				  customerProfileTabDTO = null;
				
			}
		return genericDTO;
		
	}

	public void notifyRedemption(String msisdn,String points,String transactionId)
	{
		CommonUtil commonUtil = null;
		HashMap<String, String> notificationMap = null;
		DateFormat dateFormat = new SimpleDateFormat(Cache.getConfigParameterMap().get("NOTIFICATION_DATE_FORMAT").getParameterValue());
		Date date = new Date();
		try {
			commonUtil = new CommonUtil();
			notificationMap = new HashMap<String, String>();
			notificationMap.put(SystemConstants.MSG_CAUSE,SystemConstants.msgPointExpiry);
			notificationMap.put(SystemConstants.MSISDN,msisdn);
			notificationMap.put(SystemConstants.pointExpiryExpiredPoints,points.replaceAll(".0*$",""));
			notificationMap.put(SystemConstants.pointExpiryCurrentDate,dateFormat.format(date));
			commonUtil.generateNotifyRequest(transactionId, "NotifyCustomer",msisdn, notificationMap);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Transaction ID " + transactionId + "Exception " + e.getMessage());
		} finally {
			commonUtil = null;
			notificationMap = null;
		}
	}	
	public static void main(String arg[]) throws ParseException
	{
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		String exp = sdf1.format(new Date());
	    System.out.println(exp);
	}
	
	public CDRInformationDTO buildingCdrDtoAndWritingCdr(REResponseHeader response,String msisdn,String loyaltyId,String points,String transactionId)
	{
		CDRInformationDTO cdrInformationDTO=null;
		try {
		 
				cdrInformationDTO = new CDRInformationDTO();
				cdrInformationDTO.setCommandId(CDRCommandID.PointExpiry.getId());	
				cdrInformationDTO.setTransactionId(transactionId);
				cdrInformationDTO.setStatusCode(response.getStatus());
				cdrInformationDTO.setStatusDescription(response.getStatusDesc());
				cdrInformationDTO.setSubscriberNumber(msisdn);
				cdrInformationDTO.setLoyaltyId(loyaltyId);
				cdrInformationDTO.setTierPoints(Double.valueOf(points));
			
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
		finally{
			CDRLoggerUtil.flushFatalCDR(cdrInformationDTO);
		}
		return cdrInformationDTO;
	}
	
	public Boolean captureTransactionPointExpiry(LoyaltyTransactionTabDTO loyaltyTransactionTabDTO)
	{
		TierAndBonusCalculationDAO  tierAndBonusCalculationDAO=null;
		boolean isTransactionCapturedStatus=false;
		try{
			tierAndBonusCalculationDAO = new TierAndBonusCalculationDAO();
			isTransactionCapturedStatus=tierAndBonusCalculationDAO.inserTransaction(loyaltyTransactionTabDTO);
			logger.info("TransactionId "+loyaltyTransactionTabDTO.getReqTransactionID() +" PointExpiryTransaction caputred status "+isTransactionCapturedStatus);
		}catch(Exception e)
		{
			logger.info("TransactionId ");
			e.printStackTrace();
		}
		return isTransactionCapturedStatus;
	}
	
}
