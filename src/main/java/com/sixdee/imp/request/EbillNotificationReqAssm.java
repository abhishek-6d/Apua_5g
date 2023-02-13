package com.sixdee.imp.request;

/**
 * 
 * @author Himanshu chaudhary
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
 * <td>October 15,2015 05:08:47 PM</td>
 * <td>Himanshu chaudhary</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.arch.Globals;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmGUICommon;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dto.EbillNotificationDTO;
import com.sixdee.imp.dto.StartServiceDTO;
import com.sixdee.imp.dto.VoucherPromoTranverseDTO;
import com.sixdee.imp.service.serviceDTO.req.MerchantRedemptionDTO;
import com.sixdee.imp.service.serviceDTO.req.ServiceReqDTO;
import com.sixdee.imp.service.serviceDTO.req.EBillDTO;



public class EbillNotificationReqAssm extends ReqAssmGUICommon {
	
	
	
	public GenericDTO buildAssembleGUIReq(GenericDTO genericDTO) throws CommonException {
		
		logger.info(" Class ==> EbillNotificationReqAssm :: Method ==> buildAssembleGUIReq ");
		EbillNotificationDTO ebillNotificationDTO = null;
		try{
			ebillNotificationDTO=new EbillNotificationDTO();
			EBillDTO reqDTO= (EBillDTO)genericDTO.getObj();
			
			if(reqDTO.getMsisdn()!=null)
		       ebillNotificationDTO.setSubscriberNumber(reqDTO.getMsisdn());
			
			if(reqDTO.getAccountNo()!=null)
			   ebillNotificationDTO.setAccountNumber(reqDTO.getAccountNo());
			if(reqDTO.getCustomerNo()!=null)
			   ebillNotificationDTO.setCustomerNumber(reqDTO.getCustomerNo());
			
			if(reqDTO.getCustomerType()!=null)
			   ebillNotificationDTO.setCustomerType(reqDTO.getCustomerType());
			
			if(reqDTO.getRequestDate()!=null)
			   ebillNotificationDTO.setDate(reqDTO.getRequestDate());
			
			logger.info("SUBSCRIBER NUMBER :"+ebillNotificationDTO.getSubscriberNumber());
			logger.info("ACCOUNT NUMBER"+ebillNotificationDTO.getAccountNumber());
			logger.info("CUSTOMER TYPE"+ebillNotificationDTO.getCustomerType());
			logger.info("CUSTOMER NUMBER"+ebillNotificationDTO.getCustomerNumber());
			logger.info("DATE"+ebillNotificationDTO.getDate());
			
					} catch (Exception e) {
			e.printStackTrace();
		} finally {
			genericDTO.setObj(ebillNotificationDTO);
			ebillNotificationDTO = null;
		}

		return genericDTO;
	}

}
