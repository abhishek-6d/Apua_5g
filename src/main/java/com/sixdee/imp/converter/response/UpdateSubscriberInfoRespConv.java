/**
 * 
 */
package com.sixdee.imp.converter.response;

import com.sixdee.fw.converter.response.RespConvCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.vo.ProfileInformationUpdateVO;

/**
 * @author Rahul K K
 * @version 1.0.0
 * 
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%">Date</td>
 * <td width="20%">Author</td>
 * <td>Description</td>
 * </tr>
 * <tr>
 * <td>April 24, 2013</td>
 * <td>Rahul K K</td>
 * <td>Created this class</td>
 * </tr>
 * </table>
 * </p>
 */
public class UpdateSubscriberInfoRespConv extends RespConvCommon {

	/* (non-Javadoc)
	 * @see com.sixdee.fw.converter.response.RespConvInfCommon#buildConvertResp(com.sixdee.fw.dto.GenericDTO)
	 */
	@Override
	public GenericDTO buildConvertResp(GenericDTO genericDTO) throws CommonException {
		// TODO Auto-generated method stub
		ProfileInformationUpdateVO profileInformationUpdateVO = (ProfileInformationUpdateVO) genericDTO.getObj();
		genericDTO.setObj(profileInformationUpdateVO);
		return genericDTO;
	}

}
