package com.sixdee.imp.request;

/**
 * 
 * @author Himanshu
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
 * <td>October 05,2015 04:03:20 PM</td>
 * <td>Himanshu</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmGUICommon;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dto.TerminationServiceDTO;
import com.sixdee.imp.service.serviceDTO.req.ServiceReqDTO;




public class TerminationServiceReqAssm extends ReqAssmGUICommon {
	
	
	
	public GenericDTO buildAssembleGUIReq(GenericDTO genericDTO) throws CommonException {
		
		logger.info(" Class ==> TerminationServiceReqAssm :: Method ==> buildAssembleGUIReq ");
		TerminationServiceDTO terminationServiceDTO = null;
		try{
			terminationServiceDTO=new TerminationServiceDTO();
			ServiceReqDTO reqDTO=(ServiceReqDTO) genericDTO.getObj();
			
			/*terminationServiceDTO.setTransactionId(reqDTO.getTransactionId());
			terminationServiceDTO.setTimestamp(reqDTO.getTimestamp());
			terminationServiceDTO.setChannel(reqDTO.getChannel());
			terminationServiceDTO.setServiceName(reqDTO.getServiceName());*/
			
			String subscriberCountryCode=Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue();
			
			Integer subscriberSize=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NUMBER_LENGTH").getParameterValue());
			
			if(reqDTO.getSubscriberNumber()!=null && reqDTO.getSubscriberNumber().length()==subscriberSize)
				terminationServiceDTO.setSubscriberNumber(subscriberCountryCode+reqDTO.getSubscriberNumber());
			else
				terminationServiceDTO.setSubscriberNumber(reqDTO.getSubscriberNumber());	
			
			if(reqDTO.getData()!=null && reqDTO.getData()[0]!=null)
			{
				if(reqDTO.getData()[0].getName()!=null && !reqDTO.getData()[0].getName().equalsIgnoreCase("") && reqDTO.getData()[0].getName().equalsIgnoreCase("AccountType"))
				{
					try
					{
						terminationServiceDTO.setAccountType(Integer.parseInt(reqDTO.getData()[0].getValue()));
						logger.info("Requested Language Id = "+reqDTO.getData()[0].getValue());
					}
					catch (NumberFormatException e) {
						logger.info("Got Exception while setting Account Type = "+Cache.defaultLanguageID);
					}
				}
				
			}
			
			logger.info("SUBSCRIBER NO::"+terminationServiceDTO.getSubscriberNumber());
			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			genericDTO.setObj(terminationServiceDTO);
			terminationServiceDTO = null;
		}

		return genericDTO;
	}

}
