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
 * <td>February 18,2014 05:36:06 PM</td>
 * <td>Rahul K K</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmCommon;



public class IRStatusUpdationReqAssm extends ReqAssmCommon {
	
	
	
	public GenericDTO buildAssembleReq(GenericDTO genericDTO) throws CommonException {
		
		logger.info(" Class ==> IRStatusUpdationReqAssm :: Method ==> buildAssembleGUIReq ");
	//	IRStatusUpdationDTO iRStatusUpdationDTO = null;
		try{
	//		iRStatusUpdationDTO=new IRStatusUpdationDTO();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {/*
			genericDTO.setObj(iRStatusUpdationDTO);
			iRStatusUpdationDTO = null;
		*/}

		return genericDTO;
	}

}
