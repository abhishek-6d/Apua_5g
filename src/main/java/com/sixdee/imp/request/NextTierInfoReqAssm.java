package com.sixdee.imp.request;

/**
 * 
 * @author Rahul K K
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
 * <td>June 02,2013 11:26:13 AM</td>
 * <td>Rahul K K</td>
 * </tr>
 * </table>
 * </p>
 */



import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmCommon;
import com.sixdee.imp.common.dao.LanguageDAO;
import com.sixdee.imp.dto.NextTierInfoDTO;
import com.sixdee.imp.service.serviceDTO.req.AccountDTO;
import com.sixdee.imp.vo.NextTierInfoVO;



public class NextTierInfoReqAssm extends ReqAssmCommon {
	
	private Logger logger = Logger.getLogger(NextTierInfoReqAssm.class);
	
	public GenericDTO buildAssembleReq(GenericDTO genericDTO) throws CommonException {
		
		logger.info(" Class ==> NextTierInfoReqAssm :: Method ==> buildAssembleReq ");
		NextTierInfoVO nextTierInfoDTO = null;
		AccountDTO accountDTO = null;
		String subsNumber = null;
		
		try{
			nextTierInfoDTO=new NextTierInfoVO();
			
			accountDTO = (AccountDTO) genericDTO.getObj();
			
			subsNumber = accountDTO.getMoNumber();
			
			nextTierInfoDTO.setSubscriberNumber(subsNumber);
			nextTierInfoDTO.setLangId(accountDTO.getLanguageID());
			nextTierInfoDTO.setPin(accountDTO.getPin());
			nextTierInfoDTO.setChannel(accountDTO.getChannel());
			nextTierInfoDTO.setTransactionId(accountDTO.getTransactionId());
			nextTierInfoDTO.setTimestamp(accountDTO.getTimestamp());
			genericDTO.setStatusCode("SC0000");
		} catch (Exception e) {
			logger.error("Excption occured ",e);
			e.printStackTrace();
		} finally {
			genericDTO.setObj(nextTierInfoDTO);
			nextTierInfoDTO = null;
		}
		logger.info(genericDTO.getStatusCode());

		return genericDTO;
	}

}
