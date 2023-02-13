package com.sixdee.imp.request;

/**
 * 
 * @author Ananth
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
 * <td>April 20,2016 04:41:18 PM</td>
 * <td>Ananth</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmCommon;
import com.sixdee.imp.dto.AlufuqSendCodesDTO;



public class AlufuqSendCodesReqAssm extends ReqAssmCommon {
	
	
	
	

	@Override
	public GenericDTO buildAssembleReq(GenericDTO genericDTO) throws CommonException {
		
		logger.info(" Class ==> AlufuqSendCodesReqAssm :: Method ==> buildAssembleGUIReq ");
		AlufuqSendCodesDTO alufuqSendCodesDTO = null;
		try{			
			alufuqSendCodesDTO=(AlufuqSendCodesDTO) genericDTO.getObj();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			genericDTO.setObj(alufuqSendCodesDTO);
			alufuqSendCodesDTO = null;
		}

		return genericDTO;
	}

}
