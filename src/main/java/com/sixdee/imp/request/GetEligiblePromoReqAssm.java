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
 * <td>April 24,2013 05:44:52 PM</td>
 * <td>Rahul K K</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmCommon;
import com.sixdee.imp.service.serviceDTO.req.EligiblePromoDTO;
import com.sixdee.imp.vo.GetEligiblePromoVO;



public class GetEligiblePromoReqAssm extends ReqAssmCommon {
	
	
	
	public GenericDTO buildAssembleReq(GenericDTO genericDTO) throws CommonException {
		
		logger.info(" Class ==> GetEligiblePromoReqAssm :: Method ==> buildAssembleReq ");
		GetEligiblePromoVO getEligiblePromoVO = null;
		EligiblePromoDTO eligiblePromoDTO = null;
		try{
			//getEligiblePromoDTO=new GetEligiblePromoVO();
			eligiblePromoDTO = (EligiblePromoDTO) genericDTO.getObj();
			getEligiblePromoVO = new GetEligiblePromoVO();
			getEligiblePromoVO.setTransactionId(eligiblePromoDTO.getTransactionId());
			getEligiblePromoVO.setTimestamp(eligiblePromoDTO.getTimestamp());
			getEligiblePromoVO.setChannel(eligiblePromoDTO.getChannel());
			getEligiblePromoVO.setData(eligiblePromoDTO.getData());
			getEligiblePromoVO.setSubsNumber(eligiblePromoDTO.getSubscriberNumber());
			genericDTO.setTransactionId(getEligiblePromoVO.getTransactionId());
			logger.info("Request recieved for transaction id ["+genericDTO.getTransactionId()+"]");
			genericDTO.setObj(getEligiblePromoVO);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			genericDTO.setObj(getEligiblePromoVO);
			getEligiblePromoVO = null;
		}

		return genericDTO;
	}

	
	
}
