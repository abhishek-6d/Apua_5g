package com.sixdee.imp.converter.response;

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
 * <td>May 30,2013 08:26:31 PM</td>
 * <td>Rahul K K</td>
 * </tr>
 * </table>
 * </p>
 */


import com.sixdee.fw.converter.response.RespConvCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.util.GeneralProcesses;
import com.sixdee.imp.vo.InstantRewardsVO;


public class InstantRewardsRespConv extends RespConvCommon {

	/**
	 * This method is called from the framework class in Response Converter Layer. Converts internal Data Transfer Object to Value Object
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildConvertResp(GenericDTO genericDTO) throws CommonException {
		logger.debug(" Method => buildConvertResp()");
		InstantRewardsVO instantRewardsVO  = null;
	//	GeneralProcesses generalProcesses = null;
		/*try{
			instantRewardsVO = (InstantRewardsVO) genericDTO.getObj();
			generalProcesses = new GeneralProcesses();
			instantRewardsVO.setSubscriberNumber(generalProcesses.appendCountryCode(instantRewardsVO.getSubscriberNumber()));
		}catch (CommonException e) {
			logger.error("Exception occured in request Converter ",e);
			genericDTO.setStatusCode("SC0004");
			genericDTO.setStatus(e.getMessage());
		}finally{
			genericDTO.setObj(instantRewardsVO);
		}*/
		return genericDTO;
	}


}
