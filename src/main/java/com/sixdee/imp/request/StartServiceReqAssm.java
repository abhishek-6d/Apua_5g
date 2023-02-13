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
 * <td>January 02,2015 07:09:43 PM</td>
 * <td>Nithin Kunjappan</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmCommon;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dto.StartServiceDTO;
import com.sixdee.imp.service.serviceDTO.req.ServiceReqDTO;



public class StartServiceReqAssm extends ReqAssmCommon {
	
	
	
	public GenericDTO buildAssembleReq(GenericDTO genericDTO) throws CommonException {
		
		logger.info(" Class ==> StartServiceReqAssm :: Method ==> buildAssembleGUIReq ");
		StartServiceDTO startServiceDTO = null;
		try{
			startServiceDTO=new StartServiceDTO();
			ServiceReqDTO reqDTO=(ServiceReqDTO)genericDTO.getObj();
			startServiceDTO.setTransactionId(reqDTO.getTransactionId());
			startServiceDTO.setTimestamp(reqDTO.getTimestamp());
			startServiceDTO.setChannel(reqDTO.getChannel());
			startServiceDTO.setServiceName(reqDTO.getServiceName());
			String subscriberCountryCode=Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue();
			Integer subscriberSize=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NUMBER_LENGTH").getParameterValue());
			if(reqDTO.getSubscriberNumber()!=null && reqDTO.getSubscriberNumber().length()==subscriberSize)
				startServiceDTO.setSubscriberNumber(subscriberCountryCode+reqDTO.getSubscriberNumber().trim());
			else
				startServiceDTO.setSubscriberNumber(reqDTO.getSubscriberNumber().trim());
			
			if(reqDTO.getData()!=null && reqDTO.getData()[0]!=null)
			{
				if(reqDTO.getData()[0].getName()!=null && !reqDTO.getData()[0].getName().equalsIgnoreCase("") && reqDTO.getData()[0].getName().equalsIgnoreCase("LanguageID"))
				{
					try
					{
						startServiceDTO.setLanguageId(Integer.parseInt(reqDTO.getData()[0].getValue()));
						logger.info("Requested Language Id = "+reqDTO.getData()[0].getValue());
					}
					catch (NumberFormatException e) {
						logger.info("Got Exception Default Language Id = "+Cache.defaultLanguageID);
						startServiceDTO.setLanguageId(Integer.parseInt(Cache.defaultLanguageID));
					}
				}
				else
				{
					logger.info("Default Language Id = "+Cache.defaultLanguageID);
					startServiceDTO.setLanguageId(Integer.parseInt(Cache.defaultLanguageID));
				}
				if( reqDTO.getData()[1]!=null && reqDTO.getData()[1].getName()!=null && !reqDTO.getData()[1].getName().equalsIgnoreCase("") && reqDTO.getData()[1].getName().equalsIgnoreCase("AccountType"))
				{
					startServiceDTO.setAccountType(Integer.parseInt(reqDTO.getData()[1].getValue()));
				}

				if(reqDTO.getData()[2]!=null &&reqDTO.getData()[2].getName()!=null && !reqDTO.getData()[2].getName().equalsIgnoreCase("") && reqDTO.getData()[2].getName().equalsIgnoreCase("PortIn"))
				{
					if(reqDTO.getData()[2].getValue().toLowerCase().equalsIgnoreCase("true"))
					startServiceDTO.setPortLn(true);
				}
			}
			else
				startServiceDTO.setLanguageId(Integer.parseInt(Cache.defaultLanguageID));

			
			logger.info("Transaction ID:"+reqDTO.getTransactionId());
			logger.info("TimeStamp:"+reqDTO.getTimestamp());
			logger.info("Channel:"+reqDTO.getChannel());
			logger.info("Subscriber Number:"+reqDTO.getSubscriberNumber());
			logger.info("Service Name:"+reqDTO.getServiceName());
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception Occured:"+e.getMessage());
		} finally {
			genericDTO.setObj(startServiceDTO);
			startServiceDTO = null;
		}

		return genericDTO;
	}

}
