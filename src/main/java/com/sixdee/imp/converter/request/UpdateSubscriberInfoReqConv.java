/**
 * 
 */
package com.sixdee.imp.converter.request;

import com.sixdee.fw.converter.request.ReqConvCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.dto.ProfileInformationUpdateDTO;

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
public class UpdateSubscriberInfoReqConv extends ReqConvCommon {

	/* (non-Javadoc)
	 * @see com.sixdee.fw.converter.request.ReqConvInfCommon#buildConvertReq(com.sixdee.fw.dto.GenericDTO)
	 */
	@Override
	public GenericDTO buildConvertReq(GenericDTO genericDTO) throws CommonException {
		// TODO Auto-generated method stub
		ProfileInformationUpdateDTO profileInformationUpdateDTO = (ProfileInformationUpdateDTO) genericDTO.getObj();
		genericDTO.setObj(profileInformationUpdateDTO);
		return genericDTO;
	}

}
