package com.sixdee.imp.service;

import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import com.sixdee.arch.exception.CommonException;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.dao.LanguageDAO;
import com.sixdee.imp.common.dao.SubscriberListCheckDAO;
import com.sixdee.imp.common.util.CommonUtil;
import com.sixdee.imp.dto.NextTierInfoDTO;
import com.sixdee.imp.dto.TierDetails;
import com.sixdee.imp.dto.ViewAccountDTO;
import com.sixdee.imp.service.serviceDTO.common.Data;
import com.sixdee.imp.service.serviceDTO.req.AccountDTO;
import com.sixdee.imp.service.serviceDTO.req.AccountMergingDTO;
import com.sixdee.imp.service.serviceDTO.req.AccountTypeDTO;
import com.sixdee.imp.service.serviceDTO.req.AuthenticateDTO;
import com.sixdee.imp.service.serviceDTO.req.ChangeStatusDTO;
import com.sixdee.imp.service.serviceDTO.req.UpdateAccountInfoRequestDTO;
import com.sixdee.imp.service.serviceDTO.req.VoucherManagementRequestDTO;
import com.sixdee.imp.service.serviceDTO.resp.AccountLineDTO;
import com.sixdee.imp.service.serviceDTO.resp.AccountStatusDTO;
import com.sixdee.imp.service.serviceDTO.resp.ResponseDTO;
import com.sixdee.imp.service.serviceDTO.resp.TierDetailsDTO;
import com.sixdee.imp.vo.AccountConversionVO;

public class AccountManagement 
{

	private final static Logger logger = Logger.getLogger(AccountManagement.class);
	
	boolean check=false;
	
	public ResponseDTO createLoyaltyAccount(AccountDTO accountDTO)

	{
		ResponseDTO responseDTO = new ResponseDTO();
		CommonUtil commonUtil=null;
		LanguageDAO languageDAO=null;
		LMSWebServiceAdapter adapter=null;
		String lanID=Cache.defaultLanguageID;
		GenericDTO genericDTO=null;
		SubscriberListCheckDAO subslistDAO=null;
		String txnId = null;
		long t1 = System.currentTimeMillis();
		try{
			txnId = accountDTO.getTransactionId();
			commonUtil=new CommonUtil();
			languageDAO=new LanguageDAO();
			subslistDAO=new SubscriberListCheckDAO();
			
			logger.info(" Service : CreateLoyalty -- Transaction ID :"+txnId+" MoNumber "+accountDTO.getMoNumber()+" Request Recieved In System" );
			//logger.info(accountDTO.getTransactionId()+"MO Number : "+accountDTO.getMoNumber());
			String regNumber="";
			if(accountDTO.getRegisterNumbers()!=null)
			{
			  for(String number:accountDTO.getRegisterNumbers())
				  regNumber+=number+": :";
			}
			logger.info(accountDTO.getTransactionId()+"Register Number : "+regNumber);
			
			logger.info(accountDTO.getTransactionId()+"Channel : "+accountDTO.getChannel());
			logger.info(accountDTO.getTransactionId()+"Data  "+accountDTO.getData()+" "+(accountDTO.getData()!=null?accountDTO.getData().length:""));
			logger.info(accountDTO.getTransactionId()+"Language ID :"+accountDTO.getLanguageID());
			
			lanID=(accountDTO.getLanguageID()!=null&&!accountDTO.getLanguageID().trim().equals("")?accountDTO.getLanguageID().trim():lanID);
			
			responseDTO.setTranscationId(accountDTO.getTransactionId());
			responseDTO.setTimestamp(accountDTO.getTimestamp());
			
			if((accountDTO.getMoNumber()==null||accountDTO.getMoNumber().trim().equalsIgnoreCase(""))&&(accountDTO.getRegisterNumbers()==null||accountDTO.getRegisterNumbers().length<=0))
			{
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("SUB_REQ_"+lanID).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("SUB_REQ_"+lanID).getStatusDesc());
				logger.info(accountDTO.getTransactionId()+": "+Cache.getServiceStatusMap().get("SUB_REQ_"+lanID).getStatusDesc());
				return responseDTO;
			}
			
			if(accountDTO.getMoNumber()!=null&&commonUtil.isItChar(accountDTO.getMoNumber()))
			{
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("SUB_INVALID_"+lanID).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("SUB_INVALID_"+lanID).getStatusDesc());
				logger.info(accountDTO.getTransactionId()+": "+Cache.getServiceStatusMap().get("SUB_INVALID_"+lanID).getStatusDesc());
				return responseDTO;
			}
			
			if(accountDTO.getMoNumber()!=null&&!accountDTO.getMoNumber().trim().equals(""))
			{
			 if(accountDTO.getLanguageID()==null||accountDTO.getLanguageID().trim().equals(""))	
			   lanID=languageDAO.getLanguageID(accountDTO.getMoNumber());
			}
			
			if(accountDTO.getChannel()==null||accountDTO.getChannel().trim().equalsIgnoreCase(""))
			{
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("CHN_REQ_"+lanID).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("CHN_REQ_"+lanID).getStatusDesc());
				logger.info(accountDTO.getTransactionId()+": "+Cache.getServiceStatusMap().get("CHN_REQ_"+lanID).getStatusDesc());
				return responseDTO;
			}
			
			if(Cache.getCacheMap().get("IS_WHITELIST_REQD")!=null && Cache.getCacheMap().get("IS_WHITELIST_REQD").equalsIgnoreCase("true"))
			{
				if(accountDTO.getChannel().equalsIgnoreCase("SMS") || accountDTO.getChannel().equalsIgnoreCase("LMS_WEB") || accountDTO.getChannel().equalsIgnoreCase("USSD"))
				{
					check=subslistDAO.checkSubscriber(accountDTO.getMoNumber());	
					if(check)
					{
					}
					else
					{
						responseDTO.setStatusCode(Cache.getServiceStatusMap().get("BLACKLIST_"+lanID).getStatusCode());
						responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("BLACKLIST_"+lanID).getStatusDesc());
						logger.info(accountDTO.getTransactionId()+": "+Cache.getServiceStatusMap().get("BLACKLIST_"+lanID).getStatusDesc());
						return responseDTO;
					}
					
				}
			}
			 
			
			
			if(accountDTO.getRegisterNumbers()!=null)
			{
				boolean flag=false;
				boolean flag1=true;
				boolean invalid=false;
				for(String number:accountDTO.getRegisterNumbers())
				{
					if(number!=null&&number.equalsIgnoreCase("ALL"))
						continue;
					
					if(number==null||number.trim().equalsIgnoreCase("")||commonUtil.isItChar(number))
						flag=true;
					
					if(Cache.getCacheMap().get("IS_WHITELIST_REQD")!=null && Cache.getCacheMap().get("IS_WHITELIST_REQD").equalsIgnoreCase("true"))
						if(accountDTO.getChannel().equalsIgnoreCase("SMS") || accountDTO.getChannel().equalsIgnoreCase("LMS_WEB") || accountDTO.getChannel().equalsIgnoreCase("USSD"))
							if(!subslistDAO.checkSubscriber(number))
								invalid=true;
					
					
					if(flag1&&(accountDTO.getMoNumber()==null||accountDTO.getMoNumber().trim().equals("")))
					{
						if(accountDTO.getLanguageID()==null||accountDTO.getLanguageID().trim().equals(""))	
						 lanID=languageDAO.getLanguageID(number);						
						if(lanID!=null)
							flag1=false;
					}
					
				}
				
				if(invalid)
				{
					responseDTO.setStatusCode(Cache.getServiceStatusMap().get("BLACKLIST_"+lanID).getStatusCode());
					responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("BLACKLIST_"+lanID).getStatusDesc());
					logger.info(accountDTO.getTransactionId()+": "+Cache.getServiceStatusMap().get("BLACKLIST_"+lanID).getStatusDesc());
					return responseDTO;
					
				}
				
				if(flag)
				{
					responseDTO.setStatusCode(Cache.getServiceStatusMap().get("SUB_INVALID_"+lanID).getStatusCode());
					responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("SUB_INVALID_"+lanID).getStatusDesc());
					logger.info(accountDTO.getTransactionId()+": "+Cache.getServiceStatusMap().get("SUB_INVALID_"+lanID).getStatusDesc());
					return responseDTO;
				}
			}
			
			
			accountDTO.setLanguageID(lanID);
			
			logger.info("CHANNEL COMING IS"+accountDTO.getChannel());
			logger.info("CHANNEL COMING IS"+Cache.channelFlowDetails.get(accountDTO.getChannel()));
			// For WEB channel,it is synchronous
			/*if(Cache.channelFlowDetails.get(accountDTO.getChannel()) !=null && Cache.channelFlowDetails.get(accountDTO.getChannel()).equalsIgnoreCase("SYNC"))
			{*/
				logger.info("INSIDE SYNC");
				adapter=new LMSWebServiceAdapter();
				genericDTO=(GenericDTO)adapter.callFeature("CREATEACCOUNT", accountDTO);
				
				if(genericDTO==null)
				{
					responseDTO.setStatusCode(Cache.getServiceStatusMap().get("LOYALTY_FAIL_"+lanID).getStatusCode());
					responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("LOYALTY_FAIL_"+lanID).getStatusDesc());
					logger.info(accountDTO.getTransactionId()+": "+Cache.getServiceStatusMap().get("LOYALTY_FAIL_"+lanID).getStatusDesc());
					return responseDTO;
				}
				
				if(genericDTO.getStatusCode().equalsIgnoreCase("SC0000"))
				{
					responseDTO.setStatusCode(Cache.getServiceStatusMap().get("LOYALTY_SUCCESS_"+lanID).getStatusCode());
					responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("LOYALTY_SUCCESS_"+lanID).getStatusDesc());
					
				}else if(genericDTO.getStatus()!=null&&!genericDTO.getStatus().trim().equalsIgnoreCase("")){
					logger.info(accountDTO.getTransactionId()+": "+" "+genericDTO.getStatusCode()+" "+genericDTO.getStatus());
					responseDTO.setStatusCode(genericDTO.getStatusCode());
					responseDTO.setStatusDescription(genericDTO.getStatus());
					
				}else{
					responseDTO.setStatusCode(Cache.getServiceStatusMap().get("LOYALTY_FAIL_"+lanID).getStatusCode());
					responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("LOYALTY_FAIL_"+lanID).getStatusDesc());
					logger.info(accountDTO.getTransactionId()+": "+Cache.getServiceStatusMap().get("LOYALTY_FAIL_"+lanID).getStatusDesc());
				}
				
			/*}else{
				logger.info("INSIDE ASYNC");
				RequestProcessDTO processDTO=new RequestProcessDTO();
				processDTO.setFeatureName("CREATEACCOUNT");
				processDTO.setObject(accountDTO);
			
				// Adding to Thread pool
				ThreadInitiator.requestProcessThreadPool.addTask(processDTO);
				
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("LOYALTY_SUCCESS_"+lanID).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("LOYALTY_SUCCESS_"+lanID).getStatusDesc());
				//logger.info("")
			}*/
		
		}catch (Exception e) {
			logger.info(accountDTO.getTransactionId()+": ",e);
			responseDTO.setStatusCode(Cache.getServiceStatusMap().get("LOYALTY_FAIL_"+lanID).getStatusCode());
			responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("LOYALTY_FAIL_"+lanID).getStatusDesc());
		}finally{
			long t2 = System.currentTimeMillis();
			logger.info("Transaction ID :"+txnId+" MoNumber "+accountDTO.getMoNumber()+" Request Leaving System , Processing Time "+(t2-t1) );
		
			commonUtil=null;
			languageDAO=null;
			adapter=null;
			lanID=null;
			accountDTO=null;
			genericDTO=null;
			subslistDAO=null;
		}
		
		return responseDTO;
		
	
	}//createLoyaltyAccount
	
	
	public ResponseDTO deleteLoyaltyAccount(AccountDTO accountDTO)
	{
		CommonUtil commonUtil = null;
		String lanId = Cache.defaultLanguageID;
		ResponseDTO responseDTO = null;
		LMSWebServiceAdapter adapter = null;
		GenericDTO genericDTO = null;
		long t1 = System.currentTimeMillis();
		String txnId = null;
		try {
			responseDTO = new ResponseDTO();
			commonUtil = new CommonUtil();
			if(accountDTO!=null && accountDTO.getLanguageID()==null && accountDTO.getLanguageID().equalsIgnoreCase(""))
			{
			//setting Default languageID
				accountDTO.setLanguageID(lanId);
			}
			if(accountDTO!=null && accountDTO.getTransactionId()!=null && !accountDTO.getTransactionId().equalsIgnoreCase(""))
			{
				if(accountDTO.getChannel()!=null && !accountDTO.getChannel().equalsIgnoreCase(""))
				{
					if(accountDTO.getMoNumber()!=null && !accountDTO.getMoNumber().equalsIgnoreCase(""))
					{
						logger.info("TransactionId " + txnId + " MO Number "+ accountDTO.getMoNumber() +" Channel : " + accountDTO.getChannel() + " Request Recieved in System ");
						adapter = new LMSWebServiceAdapter();
						genericDTO = (GenericDTO) adapter.callFeature("DELETEACCOUNT", accountDTO);
						responseDTO = (ResponseDTO) genericDTO.getObj();
						logger.info("TransactionId " + txnId +" Delete Account Status Code" + responseDTO.getStatusCode());
						logger.info("TransactionId " + txnId +" Delete Account Status Description" + responseDTO.getStatusDescription());
					}else
					{
						throw new CommonException();
					}
					
				}else
				{
					throw new CommonException();
				}
				
			}else
			{
				throw new CommonException();
			}
		} catch (Exception e) {
			logger.error("TransactionId"+txnId +" Exception e"+e.getMessage());
			e.printStackTrace();
		} finally {
			long t2 = System.currentTimeMillis();
			logger.info("Transaction ID :" + txnId + " MoNumber " + accountDTO.getMoNumber() + " Request Leaving System , Processing Time " + (t2 - t1));
			commonUtil = null;
			adapter = null;
			accountDTO = null;
			genericDTO = null;
		}
		return responseDTO;

	}
	
	public AccountLineDTO viewAccounts(AccountDTO accountDTO)
	{
		CommonUtil commonUtil=null;
		LanguageDAO languageDAO=null;
		String lanID=Cache.defaultLanguageID;
		LMSWebServiceAdapter adapter=null;
		AccountLineDTO accountLineDTO = new AccountLineDTO();
		GenericDTO genericDTO=null;
		SubscriberListCheckDAO subslistDAO=null;
		String txnId = null;
		long t1 = System.currentTimeMillis();
		try{
			
			commonUtil=new CommonUtil();
			languageDAO=new LanguageDAO();
			subslistDAO=new SubscriberListCheckDAO();
			txnId = accountDTO.getTransactionId();
			boolean idFlag=false;
			String idFlagValue="";
			logger.info("Service : ViewAccount -- Transaction ID :"+txnId+" MO Number : "+accountDTO.getMoNumber()+" Request Recieved in System");
			String regNumber="";
			if(accountDTO.getRegisterNumbers()!=null)
			{
			  for(String number:accountDTO.getRegisterNumbers())
				  regNumber+=number+": :";
			}
			logger.info("Register Number : "+regNumber);
			logger.info("Channel : "+accountDTO.getChannel());
			logger.info("Pin : "+accountDTO.getPin());
			logger.info("Data Tag Size : "+(accountDTO.getData()==null?"0":accountDTO.getData().length));
			
			
			accountLineDTO.setTimestamp(accountDTO.getTimestamp());
			accountLineDTO.setTranscationId(txnId);
			
			lanID=(accountDTO.getLanguageID()!=null&&!accountDTO.getLanguageID().trim().equals("")?accountDTO.getLanguageID().trim():lanID);
			
			if(accountDTO.getData()!=null&&accountDTO.getData().length>0)
			{
				for(Data data:accountDTO.getData())
				{
					if(data!=null&&data.getName()!=null&&data.getValue()!=null)
					{
						logger.info("Data Set Tag name  "+data.getName()+" Value "+data.getValue());
						
						if(data.getName().equalsIgnoreCase("CUST_ID"))
						{
							idFlag=true;
							idFlagValue=data.getValue();
						}
					}
				}
			}
			
			logger.info("ID Flag : "+idFlag);
			logger.info("ID Flag Value : "+idFlagValue);
			
		
			if((accountDTO.getMoNumber()==null||accountDTO.getMoNumber().trim().equalsIgnoreCase(""))&&(accountDTO.getRegisterNumbers()==null||accountDTO.getRegisterNumbers().length<=0))
			{
				accountLineDTO.setStatusCode(Cache.getServiceStatusMap().get("SUB_REQ_"+lanID).getStatusCode());
				accountLineDTO.setStatusDescription(Cache.getServiceStatusMap().get("SUB_REQ_"+lanID).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("SUB_REQ_"+lanID).getStatusDesc());
				return accountLineDTO;
			}
			
			if(!idFlag&&accountDTO.getMoNumber()!=null&&commonUtil.isItChar(accountDTO.getMoNumber()))
			{
				accountLineDTO.setStatusCode(Cache.getServiceStatusMap().get("SUB_INVALID_"+lanID).getStatusCode());
				accountLineDTO.setStatusDescription(Cache.getServiceStatusMap().get("SUB_INVALID_"+lanID).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("SUB_INVALID_"+lanID).getStatusDesc());
				return accountLineDTO;
			}
			
			if(!idFlag&&accountDTO.getMoNumber()!=null&&!accountDTO.getMoNumber().trim().equals(""))
			{
				if(accountDTO.getLanguageID()==null||accountDTO.getLanguageID().trim().equals(""))
			        lanID=languageDAO.getLanguageID(accountDTO.getMoNumber()); 
			}
			
			if(accountDTO.getChannel()==null||accountDTO.getChannel().trim().equalsIgnoreCase(""))
			{
				accountLineDTO.setStatusCode(Cache.getServiceStatusMap().get("CHN_REQ_"+lanID).getStatusCode());
				accountLineDTO.setStatusDescription(Cache.getServiceStatusMap().get("CHN_REQ_"+lanID).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("CHN_REQ_"+lanID).getStatusDesc());
				return accountLineDTO;
			}
			
			if(!idFlag&&Cache.getCacheMap().get("IS_WHITELIST_REQD")!=null && Cache.getCacheMap().get("IS_WHITELIST_REQD").equalsIgnoreCase("true"))
			{
				if(accountDTO.getChannel().equalsIgnoreCase("SMS") || accountDTO.getChannel().equalsIgnoreCase("LMS_WEB") || accountDTO.getChannel().equalsIgnoreCase("USSD"))
				{
					check=subslistDAO.checkSubscriber(accountDTO.getMoNumber());	
					if(check)
					{
					}
					else
					{
						accountLineDTO.setStatusCode(Cache.getServiceStatusMap().get("BLACKLIST_"+lanID).getStatusCode());
						accountLineDTO.setStatusDescription(Cache.getServiceStatusMap().get("BLACKLIST_"+lanID).getStatusDesc());
						logger.info(Cache.getServiceStatusMap().get("BLACKLIST_"+lanID).getStatusDesc());
						return accountLineDTO;
					}
					
				}
			}
			
			if(accountDTO.getRegisterNumbers()!=null)
			{
				boolean flag=false;
				boolean flag1=true;
				boolean invalid=false;
				for(String number:accountDTO.getRegisterNumbers())
				{
					if(number==null||number.trim().equalsIgnoreCase(""))
						flag=true;
					
					if(!idFlag&&commonUtil.isItChar(number))
						flag=true;
					
					if(!idFlag&&Cache.getCacheMap().get("IS_WHITELIST_REQD")!=null && Cache.getCacheMap().get("IS_WHITELIST_REQD").equalsIgnoreCase("true"))
						if(accountDTO.getChannel().equalsIgnoreCase("SMS") || accountDTO.getChannel().equalsIgnoreCase("LMS_WEB") || accountDTO.getChannel().equalsIgnoreCase("USSD"))
							if(!subslistDAO.checkSubscriber(number))
								invalid=true;
					
					
					if(!idFlag&&flag1&&(accountDTO.getMoNumber()==null||accountDTO.getMoNumber().trim().equals("")))
					{
						if(accountDTO.getLanguageID()==null||accountDTO.getLanguageID().trim().equals(""))
						  lanID=languageDAO.getLanguageID(number);
						
						if(lanID!=null)
							flag1=false;
					}
					
				}
				
				if(invalid)
				{
					accountLineDTO.setStatusCode(Cache.getServiceStatusMap().get("BLACKLIST_"+lanID).getStatusCode());
					accountLineDTO.setStatusDescription(Cache.getServiceStatusMap().get("BLACKLIST_"+lanID).getStatusDesc());
					logger.info(Cache.getServiceStatusMap().get("BLACKLIST_"+lanID).getStatusDesc());
					return accountLineDTO;
					
				}
				
				if(flag)
				{
					accountLineDTO.setStatusCode(Cache.getServiceStatusMap().get("SUB_INVALID_"+lanID).getStatusCode());
					accountLineDTO.setStatusDescription(Cache.getServiceStatusMap().get("SUB_INVALID_"+lanID).getStatusDesc());
					logger.info(Cache.getServiceStatusMap().get("SUB_INVALID_"+lanID).getStatusDesc());
					return accountLineDTO;
				}
			}
			
			accountDTO.setLanguageID(lanID);
			
			adapter=new LMSWebServiceAdapter();
			
			genericDTO=(GenericDTO)adapter.callFeature("VIEWACCOUNT", accountDTO);
			
			
			if(genericDTO==null)
			{
				accountLineDTO.setStatusCode(Cache.getServiceStatusMap().get("VIEW_FAIL_"+lanID).getStatusCode());
				accountLineDTO.setStatusDescription(Cache.getServiceStatusMap().get("VIEW_FAIL_"+lanID).getStatusDesc());
				
				return accountLineDTO;
			}
			
			ViewAccountDTO viewAccountDTO=(ViewAccountDTO)genericDTO.getObj();
			if(viewAccountDTO.getAccountStatusList()!=null)
			{
				AccountStatusDTO[] statusDTOs=viewAccountDTO.getAccountStatusList().toArray(new  AccountStatusDTO[viewAccountDTO.getAccountStatusList().size()]);
				accountLineDTO.setSubscriberNumbers(statusDTOs);
				statusDTOs=null;
			}
			
			if(viewAccountDTO.getDatas()!=null)
			{
				
				Data[] datas=viewAccountDTO.getDatas().toArray(new Data[viewAccountDTO.getDatas().size()]);
				accountLineDTO.setData(datas);
				datas=null;
				
			}
			
			if(genericDTO.getStatusCode()!=null){
				
				accountLineDTO.setStatusCode(genericDTO.getStatusCode());
				accountLineDTO.setStatusDescription(genericDTO.getStatus());
				
			}else{
				accountLineDTO.setStatusCode(Cache.getServiceStatusMap().get("VIEW_FAIL_"+lanID).getStatusCode());
				accountLineDTO.setStatusDescription(Cache.getServiceStatusMap().get("VIEW_FAIL_"+lanID).getStatusDesc());
			}
		
		}catch (Exception e) {
			logger.error("",e);
			accountLineDTO.setStatusCode(Cache.getServiceStatusMap().get("VIEW_FAIL_"+lanID).getStatusCode());
			accountLineDTO.setStatusDescription(Cache.getServiceStatusMap().get("VIEW_FAIL_"+lanID).getStatusDesc());
		}finally{
			long t2 = System.currentTimeMillis();
			logger.info("Transaction ID :"+txnId+" MoNumber "+accountDTO.getMoNumber()+" Request Leaving System , Processing Time "+(t2-t1) );

			commonUtil=null;
			languageDAO=null;
			lanID=null;
			adapter=null;
			accountDTO=null;
			genericDTO=null;
			subslistDAO=null;
		}
		 
		return accountLineDTO;
		
		
	
	}//viewAccounts
	
	public ResponseDTO authenticateSubscriber(AuthenticateDTO authenticateDTO)
	{
		CommonUtil commonUtil=null;
		LanguageDAO languageDAO=null;
		String lanID=Cache.defaultLanguageID;
		LMSWebServiceAdapter adapter=null;
		ResponseDTO responseDTO = new ResponseDTO();
		GenericDTO genericDTO=null;
		SubscriberListCheckDAO subslistDAO=null;
		String txnId = null;
		long t1 = System.currentTimeMillis();
		try{
			
			commonUtil=new CommonUtil();
			languageDAO=new LanguageDAO();
			subslistDAO=new SubscriberListCheckDAO();
			txnId = authenticateDTO.getTransactionId();
			
			logger.info("Service : Authentication -- Transaction ID :"+txnId+" Subscriber Number :"+authenticateDTO.getSubscriberNumber()+" Channel :"+authenticateDTO.getChannel()+" PIN :"+authenticateDTO.getPin()+" Request Recieved in System");
			
			lanID=(authenticateDTO.getLanguageID()!=null&&!authenticateDTO.getLanguageID().trim().equals("")?authenticateDTO.getLanguageID().trim():lanID);
			
			
			responseDTO.setTranscationId(txnId);
			responseDTO.setTimestamp(authenticateDTO.getTimestamp());
			
			if(authenticateDTO.getSubscriberNumber()==null||authenticateDTO.getSubscriberNumber().trim().equalsIgnoreCase(""))
			{
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("SUB_REQ_"+lanID).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("SUB_REQ_"+lanID).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("SUB_REQ_"+lanID).getStatusDesc());
				return responseDTO;
			}
			
			if(authenticateDTO.getLanguageID()==null||authenticateDTO.getLanguageID().trim().equals(""))
			  lanID=languageDAO.getLanguageID(authenticateDTO.getSubscriberNumber());
			
			
			if(commonUtil.isItChar(authenticateDTO.getSubscriberNumber()))
			{
				logger.info("ADSL@@@@@@@@@@@@@@@@@@@"+authenticateDTO.getSubscriberNumber());
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("USER_AUTH_INVALID_"+lanID).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("USER_AUTH_INVALID_"+lanID).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("USER_AUTH_INVALID_"+lanID).getStatusDesc());
				return responseDTO;
			}
			
			authenticateDTO.setLanguageID(lanID);
			
			if(authenticateDTO.getChannel()==null||authenticateDTO.getChannel().trim().equalsIgnoreCase(""))
			{
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("CHN_REQ_"+lanID).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("CHN_REQ_"+lanID).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("CHN_REQ_"+lanID).getStatusDesc());
				return responseDTO;
			}
			
			
			
			if(Cache.getCacheMap().get("IS_WHITELIST_REQD")!=null && Cache.getCacheMap().get("IS_WHITELIST_REQD").equalsIgnoreCase("true"))
			{
				if(authenticateDTO.getChannel().equalsIgnoreCase("SMS") || authenticateDTO.getChannel().equalsIgnoreCase("LMS_WEB") || authenticateDTO.getChannel().equalsIgnoreCase("USSD"))
				{
					check=subslistDAO.checkSubscriber(authenticateDTO.getSubscriberNumber());	
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
			
			
			if(authenticateDTO.getPin()<=0)
			{
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("PIN_REQ_"+lanID).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("PIN_REQ_"+lanID).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("PIN_REQ_"+lanID).getStatusDesc());
				return responseDTO;
			}
			
			
			
			
			 
				adapter=new LMSWebServiceAdapter();
				genericDTO=(GenericDTO)adapter.callFeature("UserAuthentication", authenticateDTO);
				responseDTO = (ResponseDTO)genericDTO.getObj();		
		
		}catch (Exception e) {
			logger.error("",e);
		}finally{
			long t2 = System.currentTimeMillis();
			logger.info("Transaction ID :"+txnId+" MoNumber "+authenticateDTO.getSubscriberNumber()+" Request Leaving System , Processing Time "+(t2-t1) );

			commonUtil=null;
			languageDAO=null;
			lanID=null;
			adapter=null;
			authenticateDTO=null;
			genericDTO=null;
			subslistDAO=null;
		}
			
		return responseDTO;
		
		
		 
	}
	
	public AccountLineDTO getRegisteredLines(AccountDTO accountDTO)
	{
		Data data=new Data();
		data.setName("isRegister");
		data.setValue("true");
		Data[] datas={data};
		accountDTO.setData(datas);
		
		return viewAccounts(accountDTO);
		
		
	}

	public ResponseDTO changeAccountStatus(ChangeStatusDTO changeStatusDTO)
	{
		AccountDTO accountDTO = new AccountDTO();
		ResponseDTO responseDTO = null;
		String txnId = null;
		long t1 = System.currentTimeMillis();
		try{
			txnId = changeStatusDTO.getTransactionId();
			logger.info("Service : ChangeStatus -- Transaction ID : "+txnId+ " MONumber "+changeStatusDTO.getMoNumber()+" Request Recieved in System");
			accountDTO.setChannel(changeStatusDTO.getChannel());
			accountDTO.setTransactionId(txnId);
			accountDTO.setTimestamp(changeStatusDTO.getTimestamp());
			accountDTO.setMoNumber(changeStatusDTO.getMoNumber());
			accountDTO.setPin(changeStatusDTO.getPin());
			accountDTO.setRegisterNumbers(changeStatusDTO.getRegisterNumbers());
		
		

		
		Data[] allData = null;
		Data data = null;
		if(changeStatusDTO.getData()!=null)
		{
			allData = new Data[changeStatusDTO.getData().length];
			System.arraycopy(changeStatusDTO.getData(), 0, allData, 0, changeStatusDTO.getData().length);
			data = new Data();
			data.setName("status");
			data.setValue(changeStatusDTO.getStatus());
			allData[changeStatusDTO.getData().length] = data;
			data = null;
		}
		else
		{
			data = new Data();
			data.setName("status");
			data.setValue(changeStatusDTO.getStatus());
			allData = new Data[1];
			allData[0] = data;
			data = null;
		}
		accountDTO.setData(allData);
		//accountDTO.set
		responseDTO = deleteLoyaltyAccount(accountDTO);
		allData = null;
		}catch(Exception e){
			logger.error("Transaction ID "+txnId+" Exception occured ",e);
		}finally{
			long t2 = System.currentTimeMillis();
			logger.info("Transaction ID :"+txnId+" MoNumber "+accountDTO.getMoNumber()+" Request Leaving System , Processing Time "+(t2-t1) );

		}
		return responseDTO;
	
	}
	
/*	public TierDetailsDTO getTierDetails(AccountDTO accountDTO)
	{
		DateFormat format = new SimpleDateFormat("dd-mm-yyyy");
		TierDetailsDTO tierDetailsDTO = new TierDetailsDTO();
		tierDetailsDTO.setTimestamp(new SimpleDateFormat("ddmmyyyyHHMMSS").format(new Date()));
		tierDetailsDTO.setTranscationId(txnId);
		
		LMSWebServiceAdapter adapter = new LMSWebServiceAdapter();
		
		GenericDTO genericDTO = adapter.callFeature("NEXTTIERINFO", accountDTO);
		
		TierDetails details = (TierDetails)genericDTO.getObj();
		
		if(details!=null)
		{
			tierDetailsDTO.setCurrentTierName(details.getTierName());
			tierDetailsDTO.setPointsToUpradeTier(details.getPointsToNextTier()+"");
			tierDetailsDTO.setValidTill(format.format(details.getTierExpiryDate()));
			tierDetailsDTO.setStatusCode("SC0000");
			tierDetailsDTO.setStatusDescription("SUCCESS");
		}
		else
		{
			tierDetailsDTO.setStatusCode("SC1000");
			tierDetailsDTO.setStatusDescription("FAILURE");
			
		}
		
		
		
		return tierDetailsDTO;
	}
*/
	
	public ResponseDTO accountMerging(AccountMergingDTO accountMergingDTO)
	{
		ResponseDTO responseDTO = new ResponseDTO();
		String txnId = null;
		long t1 = System.currentTimeMillis();
		
		
		try {
			logger.info("Service : AccountMerge -- Transaction ID :"+txnId+" Request Recieved in System ");

			txnId = accountMergingDTO.getTransactionId();
			responseDTO.setTranscationId(txnId);
			responseDTO.setTimestamp(accountMergingDTO.getTimestamp());
			
			
			if(accountMergingDTO.getRegisterNumbers()==null||accountMergingDTO.getRegisterNumbers().length<2)
			{
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("SUB_REQ_"+Cache.defaultLanguageID).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("SUB_REQ_"+Cache.defaultLanguageID).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("SUB_REQ_"+Cache.defaultLanguageID).getStatusDesc());
				return responseDTO;
			}
			LMSWebServiceAdapter adapter = new LMSWebServiceAdapter();
			GenericDTO genericDTO = (GenericDTO) adapter.callFeature("MergeAccount", accountMergingDTO);
			
			if (genericDTO == null) {
				responseDTO.setStatusCode(Cache.getServiceStatusMap().get("LOYALTY_MERGE_FAIL_"+Cache.defaultLanguageID).getStatusCode());
				responseDTO.setStatusDescription(Cache.getServiceStatusMap().get("LOYALTY_MERGE_FAIL_"+Cache.defaultLanguageID).getStatusDesc());
				return responseDTO;
			} else if(genericDTO.getStatusCode()!=null&&genericDTO.getStatus()!=null){
				
				responseDTO.setStatusCode(genericDTO.getStatusCode());
				responseDTO.setStatusDescription(genericDTO.getStatus());
				
			}else{
				genericDTO.setStatusCode(Cache.getServiceStatusMap().get("LOYALTY_MERGE_FAIL_"+Cache.defaultLanguageID).getStatusCode());
			    genericDTO.setStatus(Cache.getServiceStatusMap().get("LOYALTY_MERGE_FAIL_"+Cache.defaultLanguageID).getStatusDesc());
			} 
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}finally{
			long t2 = System.currentTimeMillis();
			logger.info("Transaction ID :"+txnId+" Request Leaving System , Processing Time "+(t2-t1) );

		}
		return responseDTO;
		
		
	}
	public TierDetailsDTO getTierDetails(AccountDTO accountDTO)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		
		TierDetailsDTO dto = null;
		LMSWebServiceAdapter adapter=null;
		GenericDTO genericDTO = null;
		NextTierInfoDTO nextTierInfoDTO = null;
		TierDetails tierDetails = null;
		
		LanguageDAO languageDAO=null;
		String lanID=Cache.defaultLanguageID;
		SubscriberListCheckDAO subslistDAO=null;
		String txnId = null;
		long t1 = System.currentTimeMillis();
		try{
			txnId = accountDTO.getTransactionId();
			languageDAO=new LanguageDAO();
			subslistDAO=new SubscriberListCheckDAO();
			
			lanID=(accountDTO.getLanguageID()!=null&&!accountDTO.getLanguageID().trim().equals("")?accountDTO.getLanguageID().trim():lanID);
			
			logger.info("SERVICE :- NextTierInfo -- Transaction ID :- ["+txnId+"] MSISDN :- ["+accountDTO.getMoNumber()+"] Request Recieved in System");
			dto = new TierDetailsDTO();
			if(accountDTO.getMoNumber()==null || accountDTO.getMoNumber().equals("")){
				logger.info("No msisdn with request "+txnId);
				dto.setStatusCode(Cache.getServiceStatusMap().get("NT_MAND_MISSING_"+lanID).getStatusCode());
				dto.setStatusDescription(Cache.getServiceStatusMap().get("NT_MAND_MISSING_"+lanID).getStatusDesc());
				//throw new CommonException("MSISDN IS MISSING FOR REQUEST");
				return dto;
			}
			
			if(accountDTO.getMoNumber()!=null&&!accountDTO.getMoNumber().trim().equals(""))
			{
			 if(accountDTO.getLanguageID()==null||accountDTO.getLanguageID().trim().equals(""))	
			   lanID=languageDAO.getLanguageID(accountDTO.getMoNumber());
			}
			
			accountDTO.setLanguageID(lanID);
			
			if(accountDTO.getChannel()==null||accountDTO.getChannel().trim().equalsIgnoreCase(""))
			{
				dto.setStatusCode(Cache.getServiceStatusMap().get("CHN_REQ_"+lanID).getStatusCode());
				dto.setStatusDescription(Cache.getServiceStatusMap().get("CHN_REQ_"+lanID).getStatusDesc());
				logger.info(Cache.getServiceStatusMap().get("CHN_REQ_"+lanID).getStatusDesc());
				return dto;
			}
			
			if(Cache.getCacheMap().get("IS_WHITELIST_REQD")!=null && Cache.getCacheMap().get("IS_WHITELIST_REQD").equalsIgnoreCase("true"))
			{
				if(accountDTO.getChannel().equalsIgnoreCase("SMS") || accountDTO.getChannel().equalsIgnoreCase("LMS_WEB") || accountDTO.getChannel().equalsIgnoreCase("USSD"))
				{
					check=subslistDAO.checkSubscriber(accountDTO.getMoNumber());	
					if(check)
					{
					}
					else
					{
						dto.setStatusCode(Cache.getServiceStatusMap().get("BLACKLIST_"+lanID).getStatusCode());
						dto.setStatusDescription(Cache.getServiceStatusMap().get("BLACKLIST_"+lanID).getStatusDesc());
						logger.info(Cache.getServiceStatusMap().get("BLACKLIST_"+lanID).getStatusDesc());
						return dto;
					}
					
				}
			}
			if(accountDTO.getChannel().trim().equalsIgnoreCase("SMS"))
			{
				
				if(accountDTO.getPin()<=0)
				{
					dto.setStatusCode(Cache.getServiceStatusMap().get("PIN_REQ_"+lanID).getStatusCode());
					dto.setStatusDescription(Cache.getServiceStatusMap().get("PIN_REQ_"+lanID).getStatusDesc());
					return dto;
				}
			}
			
			
			adapter = new LMSWebServiceAdapter();
			
			genericDTO=(GenericDTO)adapter.callFeature("NEXTTIERINFO", accountDTO);
			if(genericDTO==null){
				dto.setStatusCode(Cache.getServiceStatusMap().get("NT_GEN_INFO_"+lanID).getStatusCode());
				dto.setStatusDescription(Cache.getServiceStatusMap().get("NT_GEN_INFO_"+lanID).getStatusDesc());
			}else if(genericDTO.getStatusCode().equals("SC0000") || genericDTO.getStatusCode().trim().equals("0")){
					
				nextTierInfoDTO = (NextTierInfoDTO) genericDTO.getObj();
				tierDetails = nextTierInfoDTO.getTierDetails();
				dto.setTranscationId(txnId);
				dto.setTimestamp(accountDTO.getTimestamp()); 	
				dto.setPointsToUpradeTier(tierDetails.getPointsToNextTier()+"");
				dto.setValidTill(sdf.format(tierDetails.getTierExpiryDate()));
				dto.setCurrentTierName(tierDetails.getTierName());
				
				if(nextTierInfoDTO.getDataSets()!=null&&nextTierInfoDTO.getDataSets().size()>0)
				{
					Data[] datas=nextTierInfoDTO.getDataSets().toArray(new Data[nextTierInfoDTO.getDataSets().size()]);
					dto.setData(datas);
					datas=null;
				}
				
				dto.setStatusCode(Cache.getServiceStatusMap().get("NT_SUCCESS_"+lanID).getStatusCode());
				dto.setStatusDescription(Cache.getServiceStatusMap().get("NT_SUCCESS_"+lanID).getStatusDesc());
			}else{
				dto.setTranscationId(txnId);
				dto.setTimestamp(accountDTO.getTimestamp()); 	
				dto.setStatusCode(genericDTO.getStatusCode());
				dto.setStatusDescription(genericDTO.getStatus());
			}
		

		
			}
			catch (Exception e) {
				logger.error("Transaction ID :"+txnId+" Exception occured ",e);
				dto.setTranscationId(txnId);
				dto.setTimestamp(accountDTO.getTimestamp()); 	
				dto.setStatusCode("SC0004");
				dto.setStatusDescription("System Error");
			}finally{
				long t2 = System.currentTimeMillis();
				logger.info("Transaction ID :"+txnId+" MoNumber "+accountDTO.getMoNumber()+" Request Leaving System , Processing Time "+(t2-t1) );

				accountDTO=null;
				adapter=null;
				genericDTO = null;
				nextTierInfoDTO = null;
				tierDetails = null;
				languageDAO=null;
				lanID=null;
				subslistDAO=null;
				
			}
		
		return dto;
	}
	
	
	public ResponseDTO changeSubscriberAccountType(AccountTypeDTO accountTypeDTO)

	{
		GenericDTO genericDTO = null;
		LMSWebServiceAdapter lmsWebServiceAdapter = null;
		ResponseDTO responseDTO = null;
		AccountConversionVO accountConversionVO = null;
		long t1 = System.currentTimeMillis();
		String txnId = null;
		try{
			txnId = accountTypeDTO.getTransactionId();
			logger.info("Service : ChangeSubscriberType -- Transaction ID : "+txnId+" Request recieved in System ");
			lmsWebServiceAdapter = new LMSWebServiceAdapter();
			
			genericDTO = lmsWebServiceAdapter.callFeature("AccountConversion", accountTypeDTO);
			accountConversionVO = (AccountConversionVO) genericDTO.getObj();
			responseDTO = new ResponseDTO();
			responseDTO.setStatusCode(accountConversionVO.getStatusCode());
			responseDTO.setStatusDescription(accountConversionVO.getStatusDesc());
			responseDTO.setTranscationId(accountConversionVO.getTransactionId());
			responseDTO.setTimestamp(accountConversionVO.getTimestamp());
		}catch(Exception e){
			logger.error("Exception occured ",e);
			if(responseDTO==null){
				responseDTO = new ResponseDTO();
			}
			responseDTO.setStatusCode("SC1000");
			responseDTO.setTranscationId(txnId);
			responseDTO.setTimestamp(accountTypeDTO.getTimestamp());
		}
		finally{
			long t2 = System.currentTimeMillis();
			logger.info("Transaction ID :"+txnId+" MoNumber "+accountTypeDTO.getSubscriberNumber()+" Request Leaving System , Processing Time "+(t2-t1) );

			genericDTO = null;
		}
		return responseDTO;
	}//changeSubscriberAccountType

	public UpdateAccountInfoResponseDTO updateSubscriberAccountInfo(UpdateAccountInfoRequestDTO updateAccountInfo){
		String txnId = null;
		String msisdn = null;
		LMSWebServiceAdapter adapter = null;
		GenericDTO genericDTO = null;
		UpdateAccountInfoResponseDTO updateAccountInfoResponseDTO = null;
		long t1 = System.currentTimeMillis();
		try{
			txnId = updateAccountInfo.getTransactionId();
			msisdn = updateAccountInfo.getSubscriberNumber();
			logger.info("SERVICE :- UpdateSubscriberAccount - Transaction ID :- ["+txnId+"] MSISDN :- ["+msisdn+"] Request Recieved in System for update Subscriber Account");
			adapter = new LMSWebServiceAdapter();			
			genericDTO=(GenericDTO)adapter.callFeature("UpdateSubscriberInfo", updateAccountInfo);
			updateAccountInfoResponseDTO = (UpdateAccountInfoResponseDTO) genericDTO.getObj();
		}catch(NullPointerException e){
			logger.error("SERVICE :- UpdateSubscriberAccount - Transaction ID :- ["+txnId+"] MSISDN :- ["+msisdn+"] Exception occured ",e);
			updateAccountInfoResponseDTO = new UpdateAccountInfoResponseDTO();
			updateAccountInfoResponseDTO.setStatusCode("SC0004");
//			updateAccountInfoResponseDTO.s
		}catch (Exception e) {
			logger.error("SERVICE :- UpdateSubscriberAccount - Transaction ID :- ["+txnId+"] MSISDN :- ["+msisdn+"] Exception occured ",e);
			updateAccountInfoResponseDTO.setStatusCode("SC0004");
		}
		finally{
			logger.info("SERVICE :- UpdateSubscriberAccount - Transaction ID :- ["+txnId+"] MSISDN :- ["+msisdn+"] Time taken for the service "+(System.currentTimeMillis()-t1));
		/*	if(updateAccountInfoResponseDTO==null){
				updateAccountInfoResponseDTO = new UpdateAccountInfoResponseDTO();
				updateAccountInfoResponseDTO.setTranscationId(updateAccountInfo.getTransactionId());
				updateAccountInfoResponseDTO.setTimestamp(updateAccountInfo.getTimestamp());
				updateAccountInfoResponseDTO.setSubscriberNumber(updateAccountInfo.getSubscriberNumber());
				updateAccountInfoResponseDTO.setStatusCode("SC0000");
				updateAccountInfoResponseDTO.setStatusDescription("Success");
			}*/
			updateAccountInfo = null;
			
		}
		return updateAccountInfoResponseDTO;
	}
	
	public GenericDTO customeValidation(String key,VoucherManagementRequestDTO voucherManagementReqDTO,GenericDTO genericDTO) throws CommonException
	{
		logger.info("TransactionId "+voucherManagementReqDTO.getTransactionId()+" Key "+key);
		CommonUtil commonUtil = new CommonUtil();
		return genericDTO = commonUtil.getStatusCodeDescription(genericDTO, key + voucherManagementReqDTO.getLanguageID(),voucherManagementReqDTO.getTransactionId());
	}
}
