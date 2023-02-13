package com.sixdee.imp.request;

/**
 * 
 * @author @jith
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
 * <td>April 05,2018 04:42:20 PM</td>
 * <td>@jith</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.arch.Globals;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmGUICommon;
import com.sixdee.imp.dto.GeneralServiceDTO;



public class GeneralServiceReqAssm extends ReqAssmGUICommon {
	
	
	
	public GenericDTO buildAssembleGUIReq(GenericDTO genericDTO) throws CommonException {
		
		logger.info(" Class ==> GeneralServiceReqAssm :: Method ==> buildAssembleGUIReq ");
		GeneralServiceDTO generalServiceDTO = null;
		try{
			generalServiceDTO=new GeneralServiceDTO();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			genericDTO.setObj(generalServiceDTO);
			generalServiceDTO = null;
		}

		return genericDTO;
	}

}
