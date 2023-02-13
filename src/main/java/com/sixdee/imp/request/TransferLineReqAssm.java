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
 * <td>June 20,2013 04:39:52 PM</td>
 * <td>Rahul K K</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmCommon;
import com.sixdee.imp.dto.TransferLineDTO;
import com.sixdee.imp.service.serviceDTO.req.TransferPointsDTO;



public class TransferLineReqAssm extends ReqAssmCommon {
	
	
	
	public GenericDTO buildAssembleReq(GenericDTO genericDTO) throws CommonException {
		
		logger.info(" Class ==> TransferLineReqAssm :: Method ==> buildAssembleReq ");
		TransferLineVO transferLineVO = null;
		TransferPointsDTO transferLineDTO = null;
		try{
			transferLineVO=new TransferLineVO();
			transferLineDTO =(TransferPointsDTO) genericDTO.getObj();
			transferLineVO.setTransactionId(transferLineDTO.getTransactionID());
			transferLineVO.setTimestamp(transferLineDTO.getTimestamp());
			transferLineVO.setDonorSubscriber(transferLineDTO.getSubscriberNumber());
			transferLineVO.setRecieverSubscriber(transferLineDTO.getDestSubscriberNumber());
			transferLineVO.setChannel(transferLineDTO.getChannel());
			transferLineVO.setPointsReqd(transferLineDTO.isPointsReqd());
			transferLineVO.setStatusCode("SC0000");
			transferLineVO.setStatusDesc("Line Transfered succesfully");
			//transferLineVO.setTransactionId("8319230")

		} catch (Exception e) {
			logger.error("TransactionId "+transferLineDTO.getTransactionID()+" Exception occured ",e);
		} finally {
			genericDTO.setObj(transferLineVO);
			transferLineVO = null;
		}

		return genericDTO;
	}

}
