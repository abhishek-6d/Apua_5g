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
 * <td>December 08,2015 07:13:59 PM</td>
 * <td>Ananth</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmCommon;
import com.sixdee.imp.dto.ChangeTierDTO;



public class ChangeTierReqAssm extends ReqAssmCommon {
	
	
	
	

	@Override
	public GenericDTO buildAssembleReq(GenericDTO genericDTO) throws CommonException {
		
		logger.info(" Class ==> ChangeTierReqAssm :: Method ==> buildAssembleGUIReq ");
		ChangeTierDTO changeTierDTO = null;
		try{
			changeTierDTO=new ChangeTierDTO();
			changeTierDTO=(ChangeTierDTO) genericDTO.getObj();
			logger.info(changeTierDTO.getMsisdn());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			genericDTO.setObj(changeTierDTO);
			changeTierDTO = null;
		}

		return genericDTO;
	}

}
