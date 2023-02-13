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
 * <td>May 14,2013 06:02:42 PM</td>
 * <td>Nithin Kunjappan</td>
 * </tr>
 * </table>
 * </p>
 */



import org.apache.log4j.Logger;

import com.sixdee.fw.arch.Globals;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmGUICommon;
import com.sixdee.imp.dto.SubscriberProfileDTO;
import com.sixdee.imp.service.httpcall.dto.SubscriberRequest;
import com.sixdee.imp.service.serviceDTO.req.AccountDTO;



public class SubscriberProfileReqAssm extends ReqAssmGUICommon {
	
	Logger logger=Logger.getLogger(SubscriberProfileReqAssm.class);
	
	public GenericDTO buildAssembleGUIReq(GenericDTO genericDTO) throws CommonException {
		
	//	logger.info(" Class ==> SubscriberProfileReqAssm :: Method ==> buildAssembleGUIReq ");
		///SubscriberRequest subscriberProfileDTO=(SubscriberRequest)genericDTO.getObj();
		try{			

		//	logger.debug("GOT REQUEST-> "+subscriberProfileDTO.getReqXML());
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occured ",e);
		} finally {
		//	genericDTO.setObj(subscriberProfileDTO);
		//	subscriberProfileDTO = null;
		}

		return genericDTO;
	}

}
