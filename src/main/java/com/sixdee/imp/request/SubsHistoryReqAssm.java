package com.sixdee.imp.request;

/**
 * 
 * @author Somesh Soni
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
 * <td>September 13,2013 12:46:40 PM</td>
 * <td>Somesh Soni</td>
 * </tr>
 * </table>
 * </p>
 */



import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmCommon;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dto.SubsHistoryDTO;
import com.sixdee.imp.service.serviceDTO.req.SubscriberHistoryReqDTO;



public class SubsHistoryReqAssm extends ReqAssmCommon {
	
	
	public GenericDTO buildAssembleReq(GenericDTO genericDTO) throws CommonException {
		
		logger.info(" Class ==> SubsHistoryReqAssm :: Method ==> buildAssembleGUIReq ");
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		SubsHistoryDTO subsHistoryDTO = null;
		SubscriberHistoryReqDTO reqDTO = null;
		try{
			subsHistoryDTO=new SubsHistoryDTO();
			reqDTO = (SubscriberHistoryReqDTO) genericDTO.getObj();
			
			subsHistoryDTO.setMonth(reqDTO.getMonth());
			subsHistoryDTO.setChannel(Cache.channelDetails.get(reqDTO.getChannel().toUpperCase()));
			
			if(reqDTO.getFromDate()!=null)
			subsHistoryDTO.setFromDate(df.parse(reqDTO.getFromDate()));
			
			if(reqDTO.getToDate()!=null)
			subsHistoryDTO.setToDate(df.parse(reqDTO.getToDate()));
			
			subsHistoryDTO.setMsisdn(reqDTO.getMsisdn());
			subsHistoryDTO.setTransactionId(reqDTO.getTransactionId());
			
			logger.info("Month ==        "+ subsHistoryDTO.getMonth());
			logger.info("Channel ==      "+ subsHistoryDTO.getChannel());
			logger.info("FromDate ==     "+ subsHistoryDTO.getFromDate());
			logger.info("ToDate ==       "+ subsHistoryDTO.getToDate());
			logger.info("Msisdn ==       "+ subsHistoryDTO.getMsisdn());
			logger.info("TransactionId== "+ subsHistoryDTO.getTransactionId());
			
			
			
		} catch (Exception e) {
			logger.error(subsHistoryDTO.getTransactionId()+" - ",e);
		} finally {
			genericDTO.setObj(subsHistoryDTO);
			subsHistoryDTO = null;
		}

		return genericDTO;
	}

}
