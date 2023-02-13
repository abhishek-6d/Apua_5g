package com.sixdee.imp.request;
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
 * <td>APR 18, 2013</td>
 * <td>Nithin Kunjappan</td>
 * </tr>
 * </table>
 * </p>
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmCommon;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dto.DeleteAccountDTO;
import com.sixdee.imp.service.serviceDTO.req.AccountDTO;
import com.sixdee.imp.service.serviceDTO.common.Data;

public class DeleteAccountReqAssm extends ReqAssmCommon 
{
	 
	/**
	 * This method is called from the framework class in Request Assembler Layer. Converts request Object to Value Object
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	Logger logger=Logger.getLogger(DeleteAccountReqAssm.class);
	
	public GenericDTO buildAssembleReq(GenericDTO genericDTO) throws CommonException 
	{
		logger.info("====buildAssembleReq===");
		boolean selectAllAcounts=false;
		try {
			AccountDTO accountDTO=(AccountDTO)genericDTO.getObj();
			
			List<String> deleteNumber=null;
			List<String> finalNumbers=null;
			List<String> regNum=null;
			accountDTO.setRegisterNumbers(null);
			if(accountDTO.getRegisterNumbers()!=null)
			{
				regNum=Arrays.asList(accountDTO.getRegisterNumbers());			
				deleteNumber=new ArrayList<String>(regNum);
			} 
			
			
			logger.info("RegisterNumbers::"+accountDTO.getRegisterNumbers());
			logger.info("Mo Number::"+accountDTO.getMoNumber());
			
			if(deleteNumber!=null)
			{
				logger.info("RegisterNumbers LIST SIZE"+deleteNumber.size());
				if(deleteNumber.contains("All"))
				{
					selectAllAcounts=true;
					deleteNumber.remove("All");
				}
				if(deleteNumber.size()==1&&deleteNumber.contains(accountDTO.getMoNumber()))
				{
					deleteNumber.remove(accountDTO.getMoNumber());
				}
				
				finalNumbers= new ArrayList<String>();
				Iterator iterator = deleteNumber.iterator();
                while(iterator.hasNext()){
                	
                	String num=(String) iterator.next();
                	String subscriberCountryCode=Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue();
        			Integer subscriberSize=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NUMBER_LENGTH").getParameterValue());
        			if(num!=null&&num.length()==subscriberSize)
        				finalNumbers.add(subscriberCountryCode+num); 
        			else
        				finalNumbers.add(num);         			
                }
			}else{
				deleteNumber=new ArrayList<String>();
			}
			
			// Checking
			
			DeleteAccountDTO deleteAccountDTO=new DeleteAccountDTO();
			deleteAccountDTO.setTransactionId(accountDTO.getTransactionId());
			deleteAccountDTO.setDelete(true);
			Data data[] = accountDTO.getData();
			if(data!=null && data.length>0)
			{
				 		
				if(data[0].getName()!=null && data[0].getValue()!=null)
				if(data[0].getName().equalsIgnoreCase("status"))
				{
					deleteAccountDTO.setStatus(data[0].getValue());
					logger.info("REQ : Status is >>>"+deleteAccountDTO.getStatus());	
				}
			}
			
			//Condition for ADSL
			if(accountDTO.getMoNumber()!=null)
			{
				if(!isADSL(accountDTO.getMoNumber()))
				{
					deleteAccountDTO.setADSL(true);
				}
			}			
			if(regNum!=null && regNum.size()>0)
			{
				Iterator iterator = regNum.iterator();
				String adsl="";
				 while(iterator.hasNext()){
					 adsl=(String)iterator.next();
					 if(!isADSL(adsl))
					 { 
						 if(!adsl.equalsIgnoreCase("All"))
					 {
						 deleteAccountDTO.setADSL(true);	
					 }
					 }
					 else
					 {
					 }
				 }
			}	
			//Condition for ALL
			if(selectAllAcounts)
				deleteAccountDTO.setAllAccounts(true);
			if(accountDTO.getMoNumber()!=null)
			{	
				if(!deleteAccountDTO.isADSL())
				deleteAccountDTO.setMoNumber(Long.parseLong(accountDTO.getMoNumber()));
				else
				deleteAccountDTO.setADSLNumber(accountDTO.getMoNumber());	
			}
			
			deleteAccountDTO.setChannel(accountDTO.getChannel());
			deleteAccountDTO.setLangId(accountDTO.getLanguageID());
			deleteAccountDTO.setPin(""+accountDTO.getPin());
			//deleteAccountDTO.set
			if(finalNumbers!=null)
			deleteAccountDTO.setDeleteNumbers(finalNumbers);
			String subscriberCountryCode=Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue();
			Integer subscriberSize=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NUMBER_LENGTH").getParameterValue());
			if(accountDTO.getMoNumber()!=null && accountDTO.getMoNumber().length()==subscriberSize)
				deleteAccountDTO.setMoNumber(Long.parseLong(subscriberCountryCode+accountDTO.getMoNumber()));
			
			logger.info("FINAL MO Number is "+deleteAccountDTO.getMoNumber());
			logger.info("Language ID "+deleteAccountDTO.getLangId());
			genericDTO.setObj(deleteAccountDTO);
			
		}catch (Exception e) {
				e.printStackTrace();
		}finally {
		}
		
		return genericDTO;
	}
	
	private boolean isADSL(String s) {
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
