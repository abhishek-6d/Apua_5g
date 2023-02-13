package com.sixdee.imp.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sixdee.NotificationModule.NotificationTokens;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dto.ADSLTabDTO;
import com.sixdee.imp.dto.AccountNumberTabDTO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.LoyaltyRegisteredNumberTabDTO;
import com.sixdee.imp.dto.NationalNumberTabDTO;
import com.sixdee.imp.dto.SubscriberDetailsDTO;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;
import com.sixdee.imp.dto.ViewAccountDTO;
import com.sixdee.imp.service.serviceDTO.common.Data;
import com.sixdee.imp.service.serviceDTO.resp.AccountStatusDTO;
import com.sixdee.imp.util.CRMCalling;

/**
 * 
 * @author Paramesh
 * @version 1.0
 * 
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%"><b>Date</b></td>
 * <td width="20%"><b>Author</b></td>
 * </tr>
 * <tr>
 * <td>April 22,2013 11:53:25 PM</td>
 * <td>Paramesh</td>
 * </tr>
 * </table>
 * </p>
 */




public class ViewAccountDAO {

	Logger logger=Logger.getLogger(ViewAccountDAO.class);
	
	public void getAllAccountDetails(GenericDTO genericDTO)
	{
		
		ViewAccountDTO  viewAccountDTO=(ViewAccountDTO)genericDTO.getObj();
		
		TableDetailsDAO tableDetailsDAO=new TableDetailsDAO();
		
		List<AccountStatusDTO>  list=new ArrayList<AccountStatusDTO>();
		CRMCalling calling=null;
		
		Long number=null;
		
		Map<String,SubscriberDetailsDTO> subscriberAccountMaps=null;
		
		try{
			
			String subscriberCountryCode=Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue();
			Integer subscriberSize=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NUMBER_LENGTH").getParameterValue());
		
			Long loyaltyId=null;
			LoyaltyProfileTabDTO loyaltyProfileTabDTO1 = new LoyaltyProfileTabDTO();
			loyaltyProfileTabDTO1.setDefaultLanguage(viewAccountDTO.getLanguageID());
			
			List<AccountStatusDTO> CRMAccountStatusList=new ArrayList<AccountStatusDTO>();
			
			if(!viewAccountDTO.isIdFlag())
			{
			
				if(viewAccountDTO.getRegsterNumber()!=null&&viewAccountDTO.getRegsterNumber().size()>0)
					number=Long.parseLong(viewAccountDTO.getRegsterNumber().get(0));
				else
					number=Long.parseLong(viewAccountDTO.getMoNumber());
			
			}
			
			logger.info("CRM CALL  "+Cache.getCacheMap().get("CRM_CALL"));
			
			
			
			if(!viewAccountDTO.isRegisterLines())
			{
			
				
				Object[] objects=null;
				//	 Step 1   : Call CRM system for getting more account details
				
				if(Cache.getCacheMap().get("CRM_CALL").equalsIgnoreCase("true"))
				{
					String crmTransactionID=System.currentTimeMillis()+"";
					logger.info(viewAccountDTO.getTransactionId()+" : CRM Transaction ID is "+crmTransactionID);
					calling=new CRMCalling();
					
					// Request have nationl id tag
					if(viewAccountDTO.isIdFlag())
					{
						//For Nationl ID 
						
						List<NationalNumberTabDTO> custIdList=new ArrayList<NationalNumberTabDTO>();
						NationalNumberTabDTO tabDTO=new NationalNumberTabDTO();
						if(viewAccountDTO.getMoNumber()!=null)
						  tabDTO.setNationalNumber(viewAccountDTO.getMoNumber());
						else
						 tabDTO.setNationalNumber(viewAccountDTO.getRegsterNumber().get(0));
						
						tabDTO.setIdType(viewAccountDTO.getIdFlagValue());
						custIdList.add(tabDTO);
						
						loyaltyProfileTabDTO1.setCustIdList(custIdList);
						long start=System.currentTimeMillis();
						// Calling Service with National ID
						objects=calling.getAllLineNumber(loyaltyProfileTabDTO1,crmTransactionID);
						long start1=System.currentTimeMillis();
						logger.info(viewAccountDTO.getTransactionId()+" : MSISDN : "+number+"  1st Service Time(ms) "+(start1-start));
						if(objects!=null&&objects[0]!=null)
						{
							subscriberAccountMaps=(Map<String,SubscriberDetailsDTO>)objects[0];
							if(subscriberAccountMaps.size()>0)
							{
							  viewAccountDTO.setMoNumber(subscriberAccountMaps.keySet().iterator().next());	
							  number=Long.parseLong(viewAccountDTO.getMoNumber());
							  String notificationTemplate=calling.getBasicSubscriberInfo(loyaltyProfileTabDTO1,viewAccountDTO.getMoNumber(),crmTransactionID);
						      logger.info(viewAccountDTO.getTransactionId()+" : MSISDN : "+number+"  2nd Service Time(ms) "+(System.currentTimeMillis()-start1));
						      
						      if(notificationTemplate!=null&&(notificationTemplate.equals("6")||notificationTemplate.equals("7")))
							    {
							    	throw new CommonException(Cache.getServiceStatusMap().get("VIEW_ACCOUNT_NOTELIGBLE_"+viewAccountDTO.getLanguageID()).getStatusCode(),Cache.getServiceStatusMap().get("VIEW_ACCOUNT_NOTELIGBLE_"+viewAccountDTO.getLanguageID()).getStatusDesc());
							    }
						      
							}
						}
						logger.info(viewAccountDTO.getTransactionId()+" : MSISDN : "+number+"  All Service Time(ms) "+(System.currentTimeMillis()-start));
						
					}else{
						// For subscriber number
						long start=System.currentTimeMillis();
						String notificationTemplate=calling.getBasicSubscriberInfo(loyaltyProfileTabDTO1,number+"",crmTransactionID);
						long start1=System.currentTimeMillis();
						logger.info(viewAccountDTO.getTransactionId()+" : MSISDN : "+number+"  1st Service Time(ms) "+(start1-start));
						
						 if(notificationTemplate!=null&&(notificationTemplate.equals("6")||notificationTemplate.equals("7")))
						    {
						    	throw new CommonException(Cache.getServiceStatusMap().get("VIEW_ACCOUNT_NOTELIGBLE_"+viewAccountDTO.getLanguageID()).getStatusCode(),Cache.getServiceStatusMap().get("VIEW_ACCOUNT_NOTELIGBLE_"+viewAccountDTO.getLanguageID()).getStatusDesc());
						    }
						    
						
						objects=calling.getAllLineNumber(loyaltyProfileTabDTO1,crmTransactionID);
						logger.info(viewAccountDTO.getTransactionId()+" : MSISDN : "+number+"  2nd Service Time(ms) "+(System.currentTimeMillis()-start1));
						logger.info(viewAccountDTO.getTransactionId()+" : MSISDN : "+number+"  All Service Time(ms) "+(System.currentTimeMillis()-start));
					}
					
					
					
				}
				
				
				
				if(objects!=null&&objects[0]!=null)
				{
					subscriberAccountMaps=(Map<String,SubscriberDetailsDTO>)objects[0];
					
			
					if(subscriberAccountMaps!=null&&subscriberAccountMaps.size()>0)
					{
						for(String lineNumber:subscriberAccountMaps.keySet())
						{
							AccountStatusDTO accountStatusDTO=new AccountStatusDTO();
							String subscriberNumber = subscriberAccountMaps.get(lineNumber).getSubscriberNumber();
							logger.info("    CRM Number       "+subscriberNumber);
							accountStatusDTO.setSubscriberNumber((subscriberNumber.length()==subscriberCountryCode.length()+subscriberSize?subscriberNumber.substring(subscriberCountryCode.length()):subscriberNumber));
							logger.info("           "+accountStatusDTO.getSubscriberNumber());
							if(viewAccountDTO.getChannel()!=null&&viewAccountDTO.getChannel().trim().equalsIgnoreCase("WEB"))
							   accountStatusDTO.setStatus("5");
							else
								accountStatusDTO.setStatus(Cache.getStatusMap().get("5_"+viewAccountDTO.getLanguageID()));
									
							accountStatusDTO.setTypeId(subscriberAccountMaps.get(lineNumber).getAccountType());
							accountStatusDTO.setTypeName(Cache.accountTypeMap.get(subscriberAccountMaps.get(lineNumber).getAccountType()+"_"+viewAccountDTO.getLanguageID()));
							CRMAccountStatusList.add(accountStatusDTO);
						}
					}else{
						
						throw new CommonException(Cache.getServiceStatusMap().get("VIEW_ACCOUNT_FAIL_CRM_"+viewAccountDTO.getLanguageID()).getStatusCode(),Cache.getServiceStatusMap().get("VIEW_ACCOUNT_FAIL_CRM_"+viewAccountDTO.getLanguageID()).getStatusDesc());
						
					}
					
				}else{
					
					throw new CommonException(Cache.getServiceStatusMap().get("VIEW_ACCOUNT_FAIL_CRM_"+viewAccountDTO.getLanguageID()).getStatusCode(),Cache.getServiceStatusMap().get("VIEW_ACCOUNT_FAIL_CRM_"+viewAccountDTO.getLanguageID()).getStatusDesc());
					 
				}
				
				
			
				/*
				 *  Setp 2  : Find the LoyaltyId based on National ID which we got from CRM 
				 *  		  Find the corresponding loyalty ID for requested subscriber Number
				 */
				
				//	 checking National ID is present or not in LMS DB
				
				NationalNumberTabDTO nationalNumberTabDTO=null;
				
				if(loyaltyProfileTabDTO1.getCustIdList()!=null&&loyaltyProfileTabDTO1.getCustIdList().size()>0)
				{
				   for(NationalNumberTabDTO nationalNumberTabDTO2:loyaltyProfileTabDTO1.getCustIdList())	
				   {
				      nationalNumberTabDTO = tableDetailsDAO.getNationalNumberDetails(nationalNumberTabDTO2.getNationalNumber());
				   
					   if(nationalNumberTabDTO!=null)
						{
						   logger.info("Found Loyalty ID for Nation Id  : "+nationalNumberTabDTO2.getNationalNumber());	
						   loyaltyId=nationalNumberTabDTO.getLoyaltyID();
						   break;
						   
						}
				   }
				}
				
			
			}
			
			if(viewAccountDTO.isIdFlag())
			{
				if(subscriberAccountMaps!=null&&subscriberAccountMaps.size()>0)
				{
					for(String subscriberNumber:subscriberAccountMaps.keySet())
					{
						SubscriberNumberTabDTO subNumberDTO=tableDetailsDAO.getSubscriberNumberDetails(Long.parseLong(subscriberNumber));
						if(subNumberDTO!=null)
						{
							AccountStatusDTO accountStatusDTO=new AccountStatusDTO();
							
							//String subscriberNumber=subNumberDTO.getSubscriberNumber()+"";
							
							accountStatusDTO.setSubscriberNumber((subscriberNumber.length()==subscriberCountryCode.length()+subscriberSize?subscriberNumber.substring(subscriberCountryCode.length()):subscriberNumber));
							
							if(viewAccountDTO.getChannel()!=null&&viewAccountDTO.getChannel().trim().equalsIgnoreCase("WEB"))
							 accountStatusDTO.setStatus(""+subNumberDTO.getStatusID());
							else
							  accountStatusDTO.setStatus(Cache.getStatusMap().get(subNumberDTO.getStatusID()+"_"+viewAccountDTO.getLanguageID()));
							
							if(Cache.accountTypeMap.get(subNumberDTO.getAccountTypeId()+"_"+viewAccountDTO.getLanguageID())!=null)
							{
							  accountStatusDTO.setTypeId(subNumberDTO.getAccountTypeId());
							  accountStatusDTO.setTypeName(Cache.accountTypeMap.get(subNumberDTO.getAccountTypeId()+"_"+viewAccountDTO.getLanguageID()));
							}else{
								accountStatusDTO.setTypeId(0);
								accountStatusDTO.setTypeName("");
							}
							
							 
							list.add(accountStatusDTO);
							loyaltyId=subNumberDTO.getLoyaltyID();
						}
					}
				}
				
			}else if(loyaltyId==null){
				
				if(viewAccountDTO.getAdslNumber()!=null&&!viewAccountDTO.getAdslNumber().trim().equalsIgnoreCase(""))
				{
					ADSLTabDTO tabDTO=tableDetailsDAO.getADSLDetails(viewAccountDTO.getAdslNumber());
					
					if(tabDTO!=null)
					{
						logger.info("Requested ADSL Number  found in ADSL Table  ADSL Number : "+viewAccountDTO.getAdslNumber());
						loyaltyId=tabDTO.getLoyaltyID();
					}else{
						logger.info("Requested ADSL Number not found in LMS DB  ADSL Number : "+viewAccountDTO.getAdslNumber());
						//throw new CommonException("Requested Number not found. Number : "+viewAccountDTO.getAdslNumber());
					}
					
				}else{
					
						
						SubscriberNumberTabDTO subscriberNumberTabDTO=tableDetailsDAO.getSubscriberNumberDetails(number);
						if(subscriberNumberTabDTO==null)
						{
							AccountNumberTabDTO accountNumberTabDTO=tableDetailsDAO.getAccountNumberDetails(number+"");
							if(accountNumberTabDTO==null)
							{
								LoyaltyProfileTabDTO loyaltyProfileTabDTO=tableDetailsDAO.getLoyaltyProfileDetails(number);
								
								if(loyaltyProfileTabDTO==null)
								{
									 logger.info("Requested Subscriber Number not found in LMS DB  Subscriber Number : "+number);
									 //throw new CommonException("Requested Subscriber Number not found . Subscriber Number : "+viewAccountDTO.getMoNumber());
									 
								}else{
									logger.info("Requested Subscriber Number  found in Loyalty Profile Table  Subscriber Number : "+number);
									loyaltyId=loyaltyProfileTabDTO.getLoyaltyID();
								}
							}else{
								logger.info("Requested Subscriber Number  found in Account Number Table  Subscriber Number : "+number);
								loyaltyId=accountNumberTabDTO.getLoyaltyID();
							}
						}else{
							logger.info("Requested Subscriber Number  found in Subscriber Number Table  Subscriber Number : "+number);
							loyaltyId=subscriberNumberTabDTO.getLoyaltyID();
						}
					
					
				}
			 }
			
			
			
			
			
			
			
			
			// Step 3  :  Find the Attache Account Details for loayalty ID
			if(loyaltyId!=null)
			{
				List<Long> allLoyaltyIDs=tableDetailsDAO.getReleateIds(loyaltyId);
				System.out.println("Loyalty ID Relations IDS : " +allLoyaltyIDs);
				
				loyaltyProfileTabDTO1=tableDetailsDAO.getLoyaltyProfile(loyaltyId);
				
				for(Long relationLoyaltyID:allLoyaltyIDs)
				{
					List<LoyaltyRegisteredNumberTabDTO> loyaltyRegNumberTabList=tableDetailsDAO.getLoyaltyRegisteredNumberDetails(relationLoyaltyID);
					
					if(loyaltyRegNumberTabList!=null)
					{
						for(LoyaltyRegisteredNumberTabDTO tabDTO:loyaltyRegNumberTabList)
						{
							AccountStatusDTO accountStatusDTO=new AccountStatusDTO();
							
							String subscriberNumber=tabDTO.getLinkedNumber();
							
							accountStatusDTO.setSubscriberNumber((subscriberNumber.length()==subscriberCountryCode.length()+subscriberSize?subscriberNumber.substring(subscriberCountryCode.length()):subscriberNumber));
							
							if(viewAccountDTO.getChannel()!=null&&viewAccountDTO.getChannel().trim().equalsIgnoreCase("WEB"))
							 accountStatusDTO.setStatus(""+tabDTO.getStatusID());
							else
							  accountStatusDTO.setStatus(Cache.getStatusMap().get(tabDTO.getStatusID()+"_"+viewAccountDTO.getLanguageID()));
							
							if(Cache.accountTypeMap.get(tabDTO.getAccountTypeId()+"_"+viewAccountDTO.getLanguageID())!=null)
							{
							  accountStatusDTO.setTypeId(tabDTO.getAccountTypeId());
							  accountStatusDTO.setTypeName(Cache.accountTypeMap.get(tabDTO.getAccountTypeId()+"_"+viewAccountDTO.getLanguageID()));
							}else{
								accountStatusDTO.setTypeId(0);
								accountStatusDTO.setTypeName("");
							}
							
							if(!list.contains(accountStatusDTO))
     							list.add(accountStatusDTO);
						}
					}
					
					loyaltyRegNumberTabList=null;
				}
			}
			
			
			if(viewAccountDTO.isRegisterLines())
			{
				if(loyaltyId==null)
				{
					 logger.info("Requested Subscriber Number not found  Subscriber Number : "+number);
					 throw new CommonException(Cache.getServiceStatusMap().get("VIEW_SUB_INVALID_"+viewAccountDTO.getLanguageID()).getStatusCode(),Cache.getServiceStatusMap().get("VIEW_SUB_INVALID_"+viewAccountDTO.getLanguageID()).getStatusDesc());
					 
				} 
			}
			
			
			CRMAccountStatusList.removeAll(list);
			
			list.addAll(CRMAccountStatusList);
			
			
			
			for(AccountStatusDTO statusDTO:list)
			{
				logger.info("Number : "+statusDTO.getSubscriberNumber()+"    Status : "+statusDTO.getStatus()+"  Type : "+statusDTO.getTypeName());
			}
			
		    if(viewAccountDTO.getChannel().equalsIgnoreCase("USSD")&&list!=null)
		    {
		    	int configuredLength=Integer.parseInt(Cache.configParameterMap.get("VIEWACCOUNT_USSD_LENGTH").getParameterValue().trim());
		    	logger.info(" Configured Length For USSD "+configuredLength+"   Line Numbers Length  "+list.size());
		    	
		    	if(configuredLength<list.size()+1)
		    	{
		    		logger.info("length of Line Number is more than configured values,List going to be empty");
		    		list.clear();
		    		
		    		List<Data> dataSet=new ArrayList<Data>();
		    		Data data=new Data();
		    		data.setName("MESSAGE");
		    		dataSet.add(data);
		    		viewAccountDTO.setDatas(dataSet);
		    		
		    		if(!viewAccountDTO.isRegisterLines())
					{
						AccountStatusDTO allStatusDTO=new AccountStatusDTO();
						allStatusDTO.setSubscriberNumber(Cache.getServiceStatusMap().get("VIEW_ALLOPTION_"+viewAccountDTO.getLanguageID()).getStatusDesc());
						allStatusDTO.setStatus("");
						list.add(allStatusDTO);
						
						data.setValue(Cache.getServiceStatusMap().get("VIEW_USSD_LENGTH_MSG_"+viewAccountDTO.getLanguageID()).getStatusDesc());
					}else{
						data.setValue(Cache.getServiceStatusMap().get("VIEWREG_USSD_LENGTH_MSG_"+viewAccountDTO.getLanguageID()).getStatusDesc());
					}
		    		
		    	}else{
		    		if(!viewAccountDTO.isRegisterLines())
					{
						if(list.size()>1)
						{
							AccountStatusDTO allStatusDTO=new AccountStatusDTO();
							allStatusDTO.setSubscriberNumber(Cache.getServiceStatusMap().get("VIEW_ALLOPTION_"+viewAccountDTO.getLanguageID()).getStatusDesc());
							allStatusDTO.setStatus("");
							
							list.add(allStatusDTO);
						}
					}
		    	}
		    	
		    }else{
		    	
		    	if(!viewAccountDTO.isRegisterLines())
				{
					if(list.size()>1)
					{
						AccountStatusDTO allStatusDTO=new AccountStatusDTO();
						allStatusDTO.setSubscriberNumber(Cache.getServiceStatusMap().get("VIEW_ALLOPTION_"+viewAccountDTO.getLanguageID()).getStatusDesc());
						allStatusDTO.setStatus("");
						
						list.add(allStatusDTO);
					}
				}
		    }
			
			viewAccountDTO.setAccountStatusList(list);
			genericDTO.setStatusCode(Cache.getServiceStatusMap().get("VIEW_ACCOUNT_SCUESS_"+viewAccountDTO.getLanguageID()).getStatusCode());
			genericDTO.setStatus(Cache.getServiceStatusMap().get("VIEW_ACCOUNT_SCUESS_"+viewAccountDTO.getLanguageID()).getStatusDesc());
			
		}catch (CommonException e) {
			logger.info("Exception occured ",e);
			genericDTO.setStatusCode(e.getStatusCode());
			genericDTO.setStatus(e.getMessage());
		}
		catch (Exception e) {
			logger.info("Sever Exception occured ",e);
			genericDTO.setStatusCode(Cache.getServiceStatusMap().get("VIEW_FAIL_"+viewAccountDTO.getLanguageID()).getStatusCode());
			genericDTO.setStatus(Cache.getServiceStatusMap().get("VIEW_FAIL_"+viewAccountDTO.getLanguageID()).getStatusDesc());
			
		}finally{
			calling=null;
			list=null;
		}
		
		
	}//getAllAccountDetails
	
	
}
