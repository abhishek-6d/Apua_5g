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
 * <td>September 04,2013 03:17:03 PM</td>
 * <td>Somesh Soni</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmCommon;
import com.sixdee.imp.dto.UpdatePSODTO;



public class UpdatePSOReqAssm extends ReqAssmCommon {
	
	
	
	public GenericDTO buildAssembleReq(GenericDTO genericDTO) throws CommonException {
		
		logger.info(" Class ==> UpdatePSOReqAssm :: Method ==> buildAssembleGUIReq ");
		UpdatePSODTO updatePSODTO = null;
		try{
			com.sixdee.imp.service.serviceDTO.req.UpdatePSODTO reqDTO = (com.sixdee.imp.service.serviceDTO.req.UpdatePSODTO)genericDTO.getObj();
			updatePSODTO=new UpdatePSODTO();
			updatePSODTO.setChannel(reqDTO.getChannel());
			updatePSODTO.setMsisdn(reqDTO.getMsisdn());
			updatePSODTO.setOfferId(reqDTO.getOfferId());
			updatePSODTO.setOperation(reqDTO.getOperation());
			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			genericDTO.setObj(updatePSODTO);
			updatePSODTO = null;
		}

		return genericDTO;
	}

}
