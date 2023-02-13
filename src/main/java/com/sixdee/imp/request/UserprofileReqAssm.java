package com.sixdee.imp.request;

/**
 * 
 * @author Geevan
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
 * <td>May 11,2013 08:47:22 AM</td>
 * <td>Geevan</td>
 * </tr>
 * </table>
 * </p>
 */


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmGUICommon;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.CommonUtil;
import com.sixdee.imp.dto.UserprofileDTO;
import com.sixdee.imp.service.serviceDTO.common.Data;

public class UserprofileReqAssm extends ReqAssmGUICommon {
	
	
	
	public GenericDTO buildAssembleGUIReq(GenericDTO genericDTO)
			throws CommonException {

		logger
				.info(" Class ==> UserprofileReqAssm :: Method ==> buildAssembleGUIReq ");
		UserprofileDTO userprofileDTO = null;
		boolean accountNumber=false;
		CommonUtil commonUtil=null;
		try {
			commonUtil= new CommonUtil();
			userprofileDTO = new UserprofileDTO();

			// System.out.println("sdfjhskdfsdkjfhdskjhfkj");

			com.sixdee.imp.service.serviceDTO.req.UserProfileDTO userprofiledto = (com.sixdee.imp.service.serviceDTO.req.UserProfileDTO) genericDTO
					.getObj();
			userprofileDTO.setSubscriberNumber(userprofiledto
					.getSubscriberNumber());
			userprofileDTO.setPin(userprofiledto.getPin());
			userprofileDTO.setTimestamp(userprofiledto.getTimestamp());
			userprofileDTO.setTransactionId(userprofiledto.getTransactionID());
			userprofileDTO.setLanguageId(userprofiledto.getLanguageId());
			
			logger.info("SubscriberNumber::"+userprofileDTO.getSubscriberNumber());
			logger.info("Pin::"+userprofileDTO.getPin());
			logger.info("Timestamp::"+userprofileDTO.getTimestamp());
			logger.info("LanguageId::"+userprofileDTO.getLanguageId());
			logger.info("TransactionId::"+userprofileDTO.getTransactionId());
			if (userprofiledto.getChannel() != null) {
				userprofileDTO.setChannel(userprofiledto.getChannel());
				if (userprofileDTO.getChannel().equalsIgnoreCase("SMS"))
					userprofileDTO.setValidate(true);
			}
			if(userprofiledto.getData()!=null)
			{
				List<Data> list=new ArrayList<Data>(Arrays.asList(userprofiledto.getData()));
				for(int i=0;i<list.size();i++)
				{
					if(list.get(i)!=null&&list.get(i).getName()!=null&&list.get(i).getName().equalsIgnoreCase("IS_IDENTIFIER")){
						if(list.get(i)!=null&&list.get(i).getValue()!=null){
						userprofileDTO.setIdentifier(Boolean.valueOf(list.get(i).getValue()));
						}
					}						
					
					if(list.get(i)!=null&&list.get(i).getName()!=null&&list.get(i).getName().equalsIgnoreCase("CUST_ID"))
						userprofileDTO.setNationalID(true);
					
					
					if(list.get(i)!=null&&list.get(i).getName()!=null&&list.get(i).getName().equalsIgnoreCase("ACCOUNT_NUMBER"))
					{
						if(list.get(i)!=null&&list.get(i).getValue()!=null&&list.get(i).getValue().equalsIgnoreCase("TRUE")){
							accountNumber=true;
						}
					}
					if(list.get(i)!=null&&list.get(i).getName()!=null&&list.get(i).getName().equalsIgnoreCase("IS_DOD_ELIGIBLE")){
						if(list.get(i)!=null&&list.get(i).getValue()!=null){
						userprofileDTO.setDealOfDay(Boolean.valueOf(list.get(i).getValue()));
						}
					}
				}
				
			}
			logger.info("NATIONAL ID value::"+userprofileDTO.isNationalID());
			
			String subscriberCountryCode=Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue();
			Integer subscriberSize=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NUMBER_LENGTH").getParameterValue());
			
			if(!accountNumber){
				userprofileDTO.setSubscriberNumber(commonUtil.discardCountryCodeIfExists(userprofileDTO.getSubscriberNumber()));
			}
			logger.info("Final Subscriber Number::"+userprofileDTO.getSubscriberNumber());
		} catch (Exception e) {
			logger.info("",e);
		} finally {
			genericDTO.setObj(userprofileDTO);
			userprofileDTO = null;
			commonUtil=null;
		}

		return genericDTO;
	}

}
