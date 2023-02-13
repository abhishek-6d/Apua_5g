package com.sixdee.imp.request;

/**
 * 
 * @author Somesh
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
 * <td>April 23,2013 09:49:31 PM</td>
 * <td>Somesh</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmCommon;
import com.sixdee.imp.dto.SubscriberTransactionHistoryDTO;
import com.sixdee.imp.service.serviceDTO.req.SubscriberTransactionDTO;;




public class SubscriberTransactionHistoryReqAssm extends ReqAssmCommon 
{
	

	public GenericDTO buildAssembleReq(GenericDTO genericDTO) throws CommonException 
	{
		
		logger.info(" Class ==> SubscriberTransactionHistoryDTO :: Method ==> buildAssembleGUIReq ");
		
		SubscriberTransactionHistoryDTO subsTranHistDTO = null;
		SubscriberTransactionDTO subsTranServiceDTO = null;
		try{
			subsTranHistDTO = new SubscriberTransactionHistoryDTO();
			subsTranServiceDTO = (SubscriberTransactionDTO)genericDTO.getObj();
			subsTranHistDTO.setSubscriberNumber(subsTranServiceDTO.getSubscriberNumber());
			subsTranHistDTO.setServiceID(subsTranServiceDTO.getServiceID());
			subsTranHistDTO.setStartDate(subsTranServiceDTO.getStartDate());
			subsTranHistDTO.setEndDate(subsTranServiceDTO.getEndDate());
			subsTranHistDTO.setActivationDate(subsTranServiceDTO.getActivationDate());
			subsTranHistDTO.setServiceAmt(subsTranServiceDTO.getServiceAmt());
			subsTranHistDTO.setTransactionType(subsTranServiceDTO.getTransactionType());
			subsTranHistDTO.setServiceResponse(subsTranServiceDTO.getServiceResponse());		

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			genericDTO.setObj(subsTranHistDTO);
			subsTranHistDTO = null;
		}

		return genericDTO;
	}

}
