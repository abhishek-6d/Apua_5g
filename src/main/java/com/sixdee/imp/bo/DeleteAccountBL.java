package com.sixdee.imp.bo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.sixdee.fw.bo.BOCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.CommonUtil;
import com.sixdee.imp.dao.DeleteAccountDAO;
import com.sixdee.imp.dao.TableDetailsDAO;
import com.sixdee.imp.dto.ADSLTabDTO;
import com.sixdee.imp.dto.AccountNumberTabDTO;
import com.sixdee.imp.dto.DeleteAccountDTO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.LoyaltyRegisteredNumberTabDTO;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;

/**
 * 
 * @author Nithin Kunjappan
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
 * <td>APR 19, 2013</td>
 * <td>Nithin Kunjappan</td>
 * </tr>
 * </table>
 * </p>
 */

public class DeleteAccountBL extends BOCommon {
	
	/**
	 * This method is called from BOCommon. Access the DAO object is to create CampaignDefntion
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildProcess(GenericDTO genericDTO) throws CommonException {

		logger.info("Class => DeleteAccountBL :: Method => buildProcess()");
		
			 DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		     DeleteAccountDTO deleteAccountDTO = (DeleteAccountDTO)genericDTO.getObj();
		     DeleteAccountDAO deleteAccntDAO= null;
		     TableDetailsDAO tabDAO=null;
		     SubscriberNumberTabDTO subscriberNumDTO=null;
		     AccountNumberTabDTO accntNumDTO=null;
		     LoyaltyProfileTabDTO loyaltyProfileDTO=null;
		     LoyaltyProfileTabDTO statusMessage=null;
		     ADSLTabDTO adslDTO=null;
		     boolean process=true;
		     CommonUtil commonUtil = new CommonUtil();
		     List<LoyaltyRegisteredNumberTabDTO> numberList=null;
			 String logFile="";
		  //   logger.info("ADSL Number>>>"+deleteAccountDTO.getADSLNumber());
		     logger.info("MO Number>>>"+deleteAccountDTO.getMoNumber());
		     logger.info("STATUS>>>"+deleteAccountDTO.getStatus()); 
		    
		    try{ 
		    	 
		    if(deleteAccountDTO.getStatus()!=null ){
			
		    if(deleteAccountDTO.getStatus()!=null && deleteAccountDTO.getStatus().equals("1")){
				deleteAccountDTO.setStatusId("4");
				deleteAccountDTO.setCdrReqd(true);
				logger.info("###########------Status changed to InActive and Status id is"+deleteAccountDTO.getStatusId());
				}
			if(deleteAccountDTO.getStatus()!=null && deleteAccountDTO.getStatus().equals("4")){
				deleteAccountDTO.setStatusId("1");
				logger.info("###########------Status changed to Active and Status id is"+deleteAccountDTO.getStatusId());
				}
			if(deleteAccountDTO.getStatus()!=null && deleteAccountDTO.getStatus().equals("6")){
				deleteAccountDTO.setStatusId("6");
				deleteAccountDTO.setCdrReqd(true);
				logger.info("###########------Hard Delete Status id::"+deleteAccountDTO.getStatusId());
				}
			if(deleteAccountDTO.getStatus()!=null && deleteAccountDTO.getStatus().equals("3")){
				deleteAccountDTO.setStatusId("3");
				logger.info("###########------Soft Delete Status id::"+deleteAccountDTO.getStatusId());
				}
			if(deleteAccountDTO.getStatus()!=null && !deleteAccountDTO.getStatus().equals("3") &&  !deleteAccountDTO.getStatus().equals("6") &&  !deleteAccountDTO.getStatus().equals("4") &&  !deleteAccountDTO.getStatus().equals("1")){
				logger.info("###########------Unknown Status::"+deleteAccountDTO.getStatus());
				process=false;
				}
		    }
		    else
		    	process=false;
		    
		     if(deleteAccountDTO.isDelete())
		     {
		    	 deleteAccntDAO= new DeleteAccountDAO(deleteAccountDTO.getTransactionId());	
		    	 tabDAO=new TableDetailsDAO();
		    	 subscriberNumDTO= new SubscriberNumberTabDTO();
		    	 accntNumDTO=new AccountNumberTabDTO();
		    	 loyaltyProfileDTO=new LoyaltyProfileTabDTO();
		    	 adslDTO=new ADSLTabDTO();
		    	 boolean done=false;	
		    	 statusMessage=new LoyaltyProfileTabDTO();
		    	 statusMessage.setDefaultLanguage(deleteAccountDTO.getLangId());
		    	 if(deleteAccountDTO.isADSL())
		    	 {
		          //For one ADSL Number	  
		    	  if(deleteAccountDTO.getADSLNumber()!=null && !checkNum(deleteAccountDTO.getADSLNumber()))
		    		 {
		    			 adslDTO=tabDAO.getADSLDetails(deleteAccountDTO.getADSLNumber());
		    			 if(adslDTO!=null)
		    			 {
		    				 done=deleteAccntDAO.deleteADSLTableDetails(deleteAccountDTO.getADSLNumber(),deleteAccountDTO.getStatusId(),null);	
		    				 int count=deleteAccntDAO.checkLoyaltyRegisteredNumberDetails(adslDTO.getLoyaltyID(),adslDTO.getAccountNumber(),deleteAccountDTO.getADSLNumber());
		    				 if(count>0)
		    					 done=deleteAccntDAO.deleteLoyaltyRegNumberTableDetails(deleteAccountDTO,null,null,adslDTO,count,0);
		    				 if(done)
			    			 { 
		    					 genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "SOFT_DELETE_SUCCESS_"+statusMessage.getDefaultLanguage(), deleteAccountDTO.getTransactionId());
		    					 deleteAccountDTO.setStatusCode(genericDTO.getStatusCode());
		    					 deleteAccountDTO.setStatusDesc(genericDTO.getStatus());
								 logger.info("Sucessfully deleted");    
			    			 }
		    				 else
			    			 { 
		    					 genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "SOFT_DELETE_NO_DATA_"+statusMessage.getDefaultLanguage(), deleteAccountDTO.getTransactionId());
		    					 deleteAccountDTO.setStatusCode(genericDTO.getStatusCode());
		    					 deleteAccountDTO.setStatusDesc(genericDTO.getStatus());
			    				/* deleteAccountDTO.setStatusCode(Cache.getServiceStatusMap().get("SOFT_DELETE_NO_DATA_"+statusMessage.getDefaultLanguage()).getStatusCode());
		    					 deleteAccountDTO.setStatusDesc(Cache.getServiceStatusMap().get("SOFT_DELETE_NO_DATA_"+statusMessage.getDefaultLanguage()).getStatusDesc());
								*/ logger.info("Failure-No data in Table");    	
			    			 }
		    			 }	    			 
		    			 else
		    			 { 
		    				 genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "SOFT_DELETE_NO_DATA_"+statusMessage.getDefaultLanguage(), deleteAccountDTO.getTransactionId());
	    					 deleteAccountDTO.setStatusCode(genericDTO.getStatusCode());
	    					 deleteAccountDTO.setStatusDesc(genericDTO.getStatus());
		    				/* deleteAccountDTO.setStatusCode(Cache.getServiceStatusMap().get("SOFT_DELETE_NO_DATA_"+statusMessage.getDefaultLanguage()).getStatusCode());
	    					 deleteAccountDTO.setStatusDesc(Cache.getServiceStatusMap().get("SOFT_DELETE_NO_DATA_"+statusMessage.getDefaultLanguage()).getStatusDesc());
							*/ logger.info("Failure-No data in Table");    	
		    			 }
		    		 }
		    	 }
		    	 else
		    	 {
		    	 //For particular number
		    		 if(deleteAccountDTO.getMoNumber()!=null && deleteAccountDTO.getMoNumber()>0)
		    		 {		    			
		    			 logger.info("SUBSCRIBER NUMBER::"+deleteAccountDTO.getMoNumber());
		    			 subscriberNumDTO=tabDAO.getSubscriberNumberDetails(deleteAccountDTO.getMoNumber());		    			 
		    			 if(subscriberNumDTO!=null)		    			
		    			 {
		    				 deleteAccountDTO.setDeleteKey("SUBSCRIBER_NUMBER"); 
		    				 logger.info("DELETEKEY IS SUBSCRIBER NUMBER");
		    				 //Need to take all Numbers for this Loyalty 
		    				 numberList=deleteAccntDAO.getLinkedNumbers(subscriberNumDTO.getLoyaltyID(),deleteAccountDTO.getMoNumber());
		    				 deleteAccountDTO.setNumberList(numberList);
		    				 
		    				 if(deleteAccountDTO.getMoNumber().equals(deleteAccntDAO.getPrimaryNumber(subscriberNumDTO.getLoyaltyID())))
		    					 deleteAccountDTO.setPrimary(true);		    				 
		    				 logger.info("IS PRIMARY NUMBER:::"+deleteAccountDTO.isPrimary());
		    				
		    				 int count=deleteAccntDAO.checkLoyaltyRegisteredNumberDetails(subscriberNumDTO.getLoyaltyID(), subscriberNumDTO.getAccountNumber(), ""+deleteAccountDTO.getMoNumber());
		    				 if(count>0)
		    					 done=deleteAccntDAO.deleteLoyaltyRegNumberTableDetails(deleteAccountDTO,subscriberNumDTO,null,null,count,1);
		    				 	 if(done)
		    				 	 {
		    				 		genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "SOFT_DELETE_SUCCESS_"+statusMessage.getDefaultLanguage(), deleteAccountDTO.getTransactionId());
			    					 deleteAccountDTO.setStatusCode(genericDTO.getStatusCode());
			    					 deleteAccountDTO.setStatusDesc(genericDTO.getStatus());
		    				 		 /*deleteAccountDTO.setStatusCode(Cache.getServiceStatusMap().get("SOFT_DELETE_SUCCESS_"+statusMessage.getDefaultLanguage()).getStatusCode());
		    					 	deleteAccountDTO.setStatusDesc(Cache.getServiceStatusMap().get("SOFT_DELETE_SUCCESS_"+statusMessage.getDefaultLanguage()).getStatusDesc());
		    					 	*/logger.info("Sucessfully deleted");
		    					 	logFile=deleteAccountDTO.getTransactionId()+"|"+dateFormat.format(new Date())+"|"+deleteAccountDTO.getMoNumber()+"|||"+deleteAccountDTO.getChannel()+"||'"+Cache.getServiceStatusMap().get("SOFT_DELETE_SUCCESS_"+statusMessage.getDefaultLanguage()).getStatusCode()+"'|'"+Cache.getServiceStatusMap().get("SOFT_DELETE_SUCCESS_"+statusMessage.getDefaultLanguage()).getStatusDesc()+"'|6|"+subscriberNumDTO.getLoyaltyID()+"||||";
		    					 	logger.warn(logFile);
		    				 	 }
		    					 else
		    					 {
		    						 genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "SOFT_DELETE_NO_DATA_"+statusMessage.getDefaultLanguage(), deleteAccountDTO.getTransactionId());
			    					 deleteAccountDTO.setStatusCode(genericDTO.getStatusCode());
			    					 deleteAccountDTO.setStatusDesc(genericDTO.getStatus());
		    						/*deleteAccountDTO.setStatusCode(Cache.getServiceStatusMap().get("SOFT_DELETE_NO_DATA_"+statusMessage.getDefaultLanguage()).getStatusCode());
			    					deleteAccountDTO.setStatusDesc(Cache.getServiceStatusMap().get("SOFT_DELETE_NO_DATA_"+statusMessage.getDefaultLanguage()).getStatusDesc());
			    					*/logger.info("Failure-No data in Loyalty Table");
			    					logFile=deleteAccountDTO.getTransactionId()+"|"+dateFormat.format(new Date())+"|"+deleteAccountDTO.getMoNumber()+"|||"+deleteAccountDTO.getChannel()+"||'"+Cache.getServiceStatusMap().get("SOFT_DELETE_NO_DATA_"+statusMessage.getDefaultLanguage()).getStatusCode()+"'|'"+Cache.getServiceStatusMap().get("SOFT_DELETE_NO_DATA_"+statusMessage.getDefaultLanguage()).getStatusDesc()+"'|6|"+subscriberNumDTO.getLoyaltyID()+"||||";
			    					logger.warn(logFile);
		    					 }	    					     			    					
		    			 }		    			 
		    			 else 
		    			 {
		    				 accntNumDTO=tabDAO.getAccountNumberDetails(deleteAccountDTO.getMoNumber()+"");
		    				 if(accntNumDTO !=null)		    				 
		    				 {
		    					 deleteAccountDTO.setDeleteKey("ACCOUNT_NUMBER");
		    					 logger.info("DELETEKEY IS ACCOUNT NUMBER");
		    					 numberList=deleteAccntDAO.getLinkedNumbers(accntNumDTO.getLoyaltyID(),deleteAccountDTO.getMoNumber());
			    				 deleteAccountDTO.setNumberList(numberList);
			    				 
			    				 if(deleteAccountDTO.getMoNumber().equals(deleteAccntDAO.getPrimaryNumber(accntNumDTO.getLoyaltyID())))
			    					 deleteAccountDTO.setPrimary(true);		    				 
			    				 logger.info("IS PRIMARY NUMBER:::"+deleteAccountDTO.isPrimary());
			    				 
		    					 int count=deleteAccntDAO.checkLoyaltyRegisteredNumberDetails(accntNumDTO.getLoyaltyID(), accntNumDTO.getAccountNumber(), ""+deleteAccountDTO.getMoNumber());
			    				 if(count>0)
			    					 done=deleteAccntDAO.deleteLoyaltyRegNumberTableDetails(deleteAccountDTO,null,accntNumDTO,null,count,2);
			    			     if(done)
								 {
			    			    	 genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "SOFT_DELETE_SUCCESS_"+statusMessage.getDefaultLanguage(), deleteAccountDTO.getTransactionId());
			    					 deleteAccountDTO.setStatusCode(genericDTO.getStatusCode());
			    					 deleteAccountDTO.setStatusDesc(genericDTO.getStatus());
			    					/* deleteAccountDTO.setStatusCode(Cache.getServiceStatusMap().get("SOFT_DELETE_SUCCESS_"+statusMessage.getDefaultLanguage()).getStatusCode());
			    					 deleteAccountDTO.setStatusDesc(Cache.getServiceStatusMap().get("SOFT_DELETE_SUCCESS_"+statusMessage.getDefaultLanguage()).getStatusDesc());
			    					*/ logger.info("Sucessfully deleted"); 
			    					 logFile=deleteAccountDTO.getTransactionId()+"|"+dateFormat.format(new Date())+"|"+deleteAccountDTO.getMoNumber()+"|||"+deleteAccountDTO.getChannel()+"||'"+Cache.getServiceStatusMap().get("SOFT_DELETE_SUCCESS_"+statusMessage.getDefaultLanguage()).getStatusCode()+"'|'"+Cache.getServiceStatusMap().get("SOFT_DELETE_SUCCESS_"+statusMessage.getDefaultLanguage()).getStatusDesc()+"'|6|"+accntNumDTO.getLoyaltyID()+"||||";
				    				 logger.warn(logFile);

								 }
		    					 else
								 {
		    						 if(deleteAccountDTO.getValidate()!=1)
		    						 {
		    							 genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "SOFT_DELETE_NO_DATA_"+statusMessage.getDefaultLanguage(), deleteAccountDTO.getTransactionId());
				    					 deleteAccountDTO.setStatusCode(genericDTO.getStatusCode());
				    					 deleteAccountDTO.setStatusDesc(genericDTO.getStatus());
		    						/* deleteAccountDTO.setStatusCode(Cache.getServiceStatusMap().get("SOFT_DELETE_NO_DATA_"+statusMessage.getDefaultLanguage()).getStatusCode());
			    					 deleteAccountDTO.setStatusDesc(Cache.getServiceStatusMap().get("SOFT_DELETE_NO_DATA_"+statusMessage.getDefaultLanguage()).getStatusDesc());
									 */logger.info("Failure-No data in Loyalty Table"); 
									 logFile=deleteAccountDTO.getTransactionId()+"|"+dateFormat.format(new Date())+"|"+deleteAccountDTO.getMoNumber()+"|||"+deleteAccountDTO.getChannel()+"||'"+Cache.getServiceStatusMap().get("SOFT_DELETE_NO_DATA_"+statusMessage.getDefaultLanguage()).getStatusCode()+"'|'"+Cache.getServiceStatusMap().get("SOFT_DELETE_NO_DATA_"+statusMessage.getDefaultLanguage()).getStatusDesc()+"'|6|"+accntNumDTO.getLoyaltyID()+"||||";
				    				 logger.warn(logFile);

		    						 }
								 }	   	
		    				 }
		    				 else
		    				 {
		    					 loyaltyProfileDTO=tabDAO.getLoyaltyProfileDetails(deleteAccountDTO.getMoNumber());		   
		    					 if(loyaltyProfileDTO!=null)
		    					 {
		    					 deleteAccountDTO.setDeleteKey("LOYALTY_ID");
		    					 logger.info("DELETEKEY IS LOYALTY ID");
		    					 //Need to take all Numbers for this LoyaltyID
		    					 
		    					 numberList=deleteAccntDAO.getLineNumbers(loyaltyProfileDTO.getLoyaltyID());
		    					 
		    					 int count=deleteAccntDAO.checkLoyaltyIDRegisteredNumberDetails(loyaltyProfileDTO.getLoyaltyID(),""+deleteAccountDTO.getMoNumber());
			    				 if(count>0)
			    					 done=deleteAccntDAO.deleteLoyaltyIDRegNumberTableDetails(numberList,deleteAccountDTO,loyaltyProfileDTO,count,3);
			    			     if(done)
								 {
			    			    	 genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "SOFT_DELETE_SUCCESS_"+statusMessage.getDefaultLanguage(), deleteAccountDTO.getTransactionId());
			    					 deleteAccountDTO.setStatusCode(genericDTO.getStatusCode());
			    					 deleteAccountDTO.setStatusDesc(genericDTO.getStatus());
		    						 /*deleteAccountDTO.setStatusCode(Cache.getServiceStatusMap().get("SOFT_DELETE_SUCCESS_"+statusMessage.getDefaultLanguage()).getStatusCode());
			    					 deleteAccountDTO.setStatusDesc(Cache.getServiceStatusMap().get("SOFT_DELETE_SUCCESS_"+statusMessage.getDefaultLanguage()).getStatusDesc());
									 */logger.info("Sucessfully deleted");    
									 for(LoyaltyRegisteredNumberTabDTO loyalDTO:numberList)
										{
											logFile=deleteAccountDTO.getTransactionId()+"|"+dateFormat.format(new Date())+"|"+loyalDTO.getLinkedNumber()+"|||"+deleteAccountDTO.getChannel()+"||'"+Cache.getServiceStatusMap().get("SOFT_DELETE_SUCCESS_"+statusMessage.getDefaultLanguage()).getStatusCode()+"'|'"+Cache.getServiceStatusMap().get("SOFT_DELETE_SUCCESS_"+statusMessage.getDefaultLanguage()).getStatusDesc()+"'|6|"+loyaltyProfileDTO.getLoyaltyID()+"||||";
					    					logger.warn(logFile);
										}
								 }
			    				 else	
		    					 { 
			    					 if(deleteAccountDTO.getValidate()!=1)
			    					 {
			    						 genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "SOFT_DELETE_NO_DATA_"+statusMessage.getDefaultLanguage(), deleteAccountDTO.getTransactionId());
				    					 deleteAccountDTO.setStatusCode(genericDTO.getStatusCode());
				    					 deleteAccountDTO.setStatusDesc(genericDTO.getStatus());
			    					/* deleteAccountDTO.setStatusCode(Cache.getServiceStatusMap().get("SOFT_DELETE_NO_DATA_"+statusMessage.getDefaultLanguage()).getStatusCode());
			    					 deleteAccountDTO.setStatusDesc(Cache.getServiceStatusMap().get("SOFT_DELETE_NO_DATA_"+statusMessage.getDefaultLanguage()).getStatusDesc());
									 */logger.info("Failure-No data in Table");  
									 for(LoyaltyRegisteredNumberTabDTO loyalDTO:numberList)
										{
											logFile=deleteAccountDTO.getTransactionId()+"|"+dateFormat.format(new Date())+"|"+loyalDTO.getLinkedNumber()+"|||"+deleteAccountDTO.getChannel()+"||'"+Cache.getServiceStatusMap().get("SOFT_DELETE_NO_DATA_"+statusMessage.getDefaultLanguage()).getStatusCode()+"'|'"+Cache.getServiceStatusMap().get("SOFT_DELETE_NO_DATA_"+statusMessage.getDefaultLanguage()).getStatusDesc()+"'|6|"+loyaltyProfileDTO.getLoyaltyID()+"||||";
					    					logger.warn(logFile);
										}
			    					 }
		    					 } 
		    					 }
		    					 else
		    					 {		
		    						 genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "SOFT_DEL_SUB_NT_REG_"+statusMessage.getDefaultLanguage(), deleteAccountDTO.getTransactionId());
			    					 deleteAccountDTO.setStatusCode(genericDTO.getStatusCode());
			    					 deleteAccountDTO.setStatusDesc(genericDTO.getStatus());
		    						/* deleteAccountDTO.setStatusCode(Cache.getServiceStatusMap().get("SOFT_DEL_SUB_NT_REG_"+statusMessage.getDefaultLanguage()).getStatusCode());
			    					 deleteAccountDTO.setStatusDesc(Cache.getServiceStatusMap().get("SOFT_DEL_SUB_NT_REG_"+statusMessage.getDefaultLanguage()).getStatusDesc());
									 */logger.info("Failure-Sub not Registered"); 
		    					 }
		    				 }		    				 
		    			 }		
		    		 }	
		    	 }		    	 
		     }
		     else
		     {
		    	 genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "SOFT_DELETE_UNKWN_STAT_"+deleteAccountDTO.getLangId(), deleteAccountDTO.getTransactionId());
				 deleteAccountDTO.setStatusCode(genericDTO.getStatusCode());
				 deleteAccountDTO.setStatusDesc(genericDTO.getStatus());
				 
		    	/* deleteAccountDTO.setStatusCode(Cache.getServiceStatusMap().get("SOFT_DELETE_UNKWN_STAT_"+deleteAccountDTO.getLangId()).getStatusCode());
				 deleteAccountDTO.setStatusDesc(Cache.getServiceStatusMap().get("SOFT_DELETE_UNKWN_STAT_"+deleteAccountDTO.getLangId()).getStatusDesc());*/
		     }
		    
		     logger.info("responseCode:"+deleteAccountDTO.getStatusCode()+"description:"+deleteAccountDTO.getStatusDesc());
		  }
		  catch (Exception e) {
			  e.printStackTrace();
			  logger.error(e.getMessage());
			// TODO: handle exception
		}finally{
			
			      deleteAccntDAO= null;
			      tabDAO=null;
			      subscriberNumDTO=null;
			      accntNumDTO=null;
			      loyaltyProfileDTO=null;
			      statusMessage=null;
			      adslDTO=null;
			      process=true;
			      commonUtil = null;
			      numberList=null;
		}
			return genericDTO;
		}
	//Checking number or character
	private boolean checkNum(String s) {
		boolean result=true;
		char[] c = s.toCharArray();		
	      for(int i=0; i < s.length(); i++)
	      {
	          if (Character.isDigit(c[i]))
	          {}
	          else
	          {
	        	  result=false; 
	          }
	     }
	     return result;
	}	

}