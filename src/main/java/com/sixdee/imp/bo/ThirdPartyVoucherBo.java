package com.sixdee.imp.bo;

import org.apache.log4j.Logger;

import com.sixdee.imp.dto.RedeemPointsDTO;
import com.sixdee.imp.service.ProcessExecute;

public class ThirdPartyVoucherBo implements ProcessExecute {
	private static final Logger logger = Logger.getLogger(ThirdPartyVoucherBo.class);
	public RedeemPointsDTO process(RedeemPointsDTO redeemPointsDTO) {
	try{
		logger.info(redeemPointsDTO.getTransactionId()+"INSIDE ThirdPartyVoucherBo");
		redeemPointsDTO.setStatusCode("SC0000");
		redeemPointsDTO.setStatusDesc("SUCCESS");
	}
	catch(Exception e){
		
	}finally{
		
	}
	return redeemPointsDTO;
	}
}
