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
 * <td>September 04,2013 11:45:30 AM</td>
 * <td>Somesh Soni</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmCommon;
import com.sixdee.imp.dto.ChangeServiceClassDTO;



public class ChangeServiceClassReqAssm extends ReqAssmCommon {
	
	
	
	public GenericDTO buildAssembleReq(GenericDTO genericDTO) throws CommonException {
		
		logger.info(" Class ==> ChangeServiceClassReqAssm :: Method ==> buildAssembleGUIReq ");
		ChangeServiceClassDTO changeServiceClassDTO = null;
		try{
			com.sixdee.imp.service.serviceDTO.req.ChangeServiceDTO reqDTO =(com.sixdee.imp.service.serviceDTO.req.ChangeServiceDTO)genericDTO.getObj();
			changeServiceClassDTO=new ChangeServiceClassDTO();
			
			changeServiceClassDTO.setMsisdn(reqDTO.getMsisdn());
			changeServiceClassDTO.setClassId(reqDTO.getClassId());
			changeServiceClassDTO.setChannel(reqDTO.getChannel());
			changeServiceClassDTO.setTransactionId(reqDTO.getTransactionId());
			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			genericDTO.setObj(changeServiceClassDTO);
			changeServiceClassDTO = null;
		}

		return genericDTO;
	}

}
