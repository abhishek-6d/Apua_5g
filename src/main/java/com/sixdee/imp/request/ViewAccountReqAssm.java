package com.sixdee.imp.request;

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



import java.util.ArrayList;
import java.util.List;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmGUICommon;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.CommonUtil;
import com.sixdee.imp.dto.ViewAccountDTO;
import com.sixdee.imp.service.serviceDTO.common.Data;
import com.sixdee.imp.service.serviceDTO.req.AccountDTO;



public class ViewAccountReqAssm extends ReqAssmGUICommon {
	
	
	
	public GenericDTO buildAssembleGUIReq(GenericDTO genericDTO) throws CommonException {
		
		logger.info(" Class ==> ViewAccountReqAssm :: Method ==> buildAssembleGUIReq ");
		ViewAccountDTO viewAccountDTO = null;
		boolean idFlag=false;
		
		try{
			viewAccountDTO=new ViewAccountDTO();
			CommonUtil commonUtil=new CommonUtil();
			String adslNumner=null;
			
			String subscriberCountryCode=Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue();
			Integer subscriberSize=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NUMBER_LENGTH").getParameterValue());
			
			
			
			AccountDTO accountDTO=(AccountDTO)genericDTO.getObj();
			
			
			if(accountDTO.getData()!=null&&accountDTO.getData().length>0)
			{
				for(Data data:accountDTO.getData())
				{
					if(data!=null&&data.getName()!=null&&data.getValue()!=null)
					{
						if(data.getName().equalsIgnoreCase("CUST_ID"))
						{
							idFlag=true;
							viewAccountDTO.setIdFlag(true);
							viewAccountDTO.setIdFlagValue(data.getValue().trim());
							
						}else if(data.getName().equalsIgnoreCase("isRegister"))
						{
							viewAccountDTO.setRegisterLines(Boolean.parseBoolean(data.getValue().trim()));
						}
					}
				}
			}
			
			
			
			viewAccountDTO.setTransactionId(accountDTO.getTransactionId());
			
			if(accountDTO.getMoNumber()!=null)
			{
				
				if(!idFlag&&accountDTO.getMoNumber().length()==subscriberSize)
					viewAccountDTO.setMoNumber((subscriberCountryCode+accountDTO.getMoNumber()).trim());
				else
					viewAccountDTO.setMoNumber(accountDTO.getMoNumber().trim());
			}
			
			List<String> registerNumber=new ArrayList<String>();
			
			if(accountDTO.getRegisterNumbers()!=null){
				for(String number:accountDTO.getRegisterNumbers())
				{
					logger.info(" Request Registered Number : "+number);
					
					if(!idFlag&&number.length()==subscriberSize)
					{
						registerNumber.add(subscriberCountryCode+number);
					}else{
						registerNumber.add(number);
					}
					
				}
			}
			
			
			logger.info("REQ : MO Number is "+accountDTO.getMoNumber());
			logger.info("REQ : Register Number is "+registerNumber);
			logger.info("REQ : Is Register "+viewAccountDTO.isRegisterLines());
			logger.info("REQ : Language ID "+accountDTO.getLanguageID());
			
			
			viewAccountDTO.setView(true);
			
			viewAccountDTO.setRegsterNumber(registerNumber);
			viewAccountDTO.setChannel(accountDTO.getChannel());
			viewAccountDTO.setAdslNumber(adslNumner);
			viewAccountDTO.setLanguageID(accountDTO.getLanguageID());
			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			genericDTO.setObj(viewAccountDTO);
		}

		return genericDTO;
	}

}
