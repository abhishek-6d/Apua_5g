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
 * <td>July 25,2013 03:12:10 PM</td>
 * <td>Paramesh</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.arch.Globals;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmGUICommon;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dto.PointDetailsDTO;



public class PointDetailsReqAssm extends ReqAssmGUICommon {
	
	
	
	public GenericDTO buildAssembleGUIReq(GenericDTO genericDTO) throws CommonException {
		
		logger.info(" Class ==> PointDetailsReqAssm :: Method ==> buildAssembleGUIReq ");
		PointDetailsDTO pointDetailsDTO = null;
		try{
			pointDetailsDTO=new PointDetailsDTO();
			
			com.sixdee.imp.service.serviceDTO.req.PointDetailsDTO detailsDTO=(com.sixdee.imp.service.serviceDTO.req.PointDetailsDTO)genericDTO.getObj();
			
			String subscriberCountryCode=Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue();
			Integer subscriberSize=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NUMBER_LENGTH").getParameterValue());
			
			if(detailsDTO.getSubscriberNumber().length()==subscriberSize)
				detailsDTO.setSubscriberNumber(subscriberCountryCode+detailsDTO.getSubscriberNumber());

			pointDetailsDTO.setSubscriberNumber(detailsDTO.getSubscriberNumber());
			pointDetailsDTO.setPin(detailsDTO.getPin());
			pointDetailsDTO.setFromDate(detailsDTO.getFromDate());
			pointDetailsDTO.setToDate(detailsDTO.getToDate());
			pointDetailsDTO.setNoOfMonths(detailsDTO.getNoOfMonths());
			pointDetailsDTO.setOffSet(detailsDTO.getOffSet());
			pointDetailsDTO.setLimit(detailsDTO.getLimit());
			pointDetailsDTO.setChannel(Cache.channelDetails.get(detailsDTO.getChannel().toUpperCase()));
			pointDetailsDTO.setDefaultLanguage(detailsDTO.getLanguageID());
			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			genericDTO.setObj(pointDetailsDTO);
			pointDetailsDTO = null;
		}

		return genericDTO;
	}

}
