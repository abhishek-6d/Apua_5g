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
 * <td>June 28,2013 03:16:37 PM</td>
 * <td>Paramesh</td>
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
import com.sixdee.imp.common.dao.LanguageDAO;
import com.sixdee.imp.dto.ForgotPasswordDTO;
import com.sixdee.imp.service.serviceDTO.common.Data;
import com.sixdee.imp.service.serviceDTO.req.UserProfileDTO;



public class ForgotPasswordReqAssm extends ReqAssmGUICommon {
	
	
	
	public GenericDTO buildAssembleGUIReq(GenericDTO genericDTO) throws CommonException {
		
		logger.info(" Class ==> ForgotPasswordReqAssm :: Method ==> buildAssembleGUIReq ");
		ForgotPasswordDTO forgotPasswordDTO = null;
		LanguageDAO languageDAO = new LanguageDAO();
		String langID=null;
		try{
			forgotPasswordDTO=new ForgotPasswordDTO();
			
			UserProfileDTO userProfileDTO=(UserProfileDTO)genericDTO.getObj();
			
			String subscriberCountryCode=Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue();
			Integer subscriberSize=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NUMBER_LENGTH").getParameterValue());
			
			if(userProfileDTO.getSubscriberNumber().length()==subscriberSize)
				userProfileDTO.setSubscriberNumber(subscriberCountryCode+userProfileDTO.getSubscriberNumber());
			
			if(userProfileDTO.getData()!=null)
			{
				List<Data> list=new ArrayList<Data>(Arrays.asList(userProfileDTO.getData()));
				for(int i=0;i<list.size();i++)
				{
					if(list.get(i)!=null&&list.get(i).getName()!=null&&list.get(i).getName().equalsIgnoreCase("LINE_NUMBER")&& list.get(i).getValue()!=null)
					{
						if(list.get(i).getValue().length()==subscriberSize)
							forgotPasswordDTO.setLineNumber(subscriberCountryCode+list.get(i).getValue());
						else
							forgotPasswordDTO.setLineNumber(list.get(i).getValue());
						langID= languageDAO.getLanguageID(forgotPasswordDTO.getLineNumber());
						forgotPasswordDTO.setLineLanguageId(langID);
					}
					else if(list.get(i)!=null&&list.get(i).getName()!=null&&list.get(i).getName().equalsIgnoreCase("MOBILE_APP")&& list.get(i).getValue()!=null){
						forgotPasswordDTO.setMobileApp(true);
						
					}else if(list.get(i)!=null&&list.get(i).getName()!=null&&list.get(i).getName().equalsIgnoreCase("TRAVELLER_APP")&& list.get(i).getValue()!=null){
						forgotPasswordDTO.setTravellerApp(true);
					}
				}
				
			}
			
			forgotPasswordDTO.setSubscriberNumber(userProfileDTO.getSubscriberNumber());
			forgotPasswordDTO.setChannel(userProfileDTO.getChannel());
			forgotPasswordDTO.setDefaultLanguage(userProfileDTO.getLanguageId());
			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			genericDTO.setObj(forgotPasswordDTO);
			forgotPasswordDTO = null;
		}

		return genericDTO;
	}

}
