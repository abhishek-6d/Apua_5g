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
 * <td>September 03,2013 11:27:12 AM</td>
 * <td>Somesh Soni</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmCommon;
import com.sixdee.imp.dto.UpdateAccountDTO;



public class UpdateAccountReqAssm extends ReqAssmCommon {
	
	
	public GenericDTO buildAssembleReq(GenericDTO genericDTO) throws CommonException {
		
		logger.info(" Class ==> UpdateAccountReqAssm :: Method ==> buildAssembleGUIReq ");
		UpdateAccountDTO updateAccountDTO = null;
		try{
			com.sixdee.imp.service.serviceDTO.req.UpdateAccountDTO reqDTO = (com.sixdee.imp.service.serviceDTO.req.UpdateAccountDTO)genericDTO.getObj();
			updateAccountDTO=new UpdateAccountDTO();
			updateAccountDTO.setMsisdn(reqDTO.getMsisdn());
			updateAccountDTO.setAmountChanrged(reqDTO.getAmountChanrged());
			updateAccountDTO.setDedicatedActId(reqDTO.getDedicatedActId());
			updateAccountDTO.setExtendExpiry(reqDTO.getExtendExpiry());
			updateAccountDTO.setFactor(reqDTO.getFactor());
			updateAccountDTO.setFreeUnits(reqDTO.getFreeUnits());
			updateAccountDTO.setOperation(reqDTO.getOperation());
			updateAccountDTO.setValidity(reqDTO.getValidity());
			updateAccountDTO.setTransactionId(reqDTO.getTransactionId());

			logger.info("MSISDN == "+updateAccountDTO.getMsisdn());
			logger.info("Operation == "+updateAccountDTO.getOperation());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			genericDTO.setObj(updateAccountDTO);
			updateAccountDTO = null;
		}

		return genericDTO;
	}

}
