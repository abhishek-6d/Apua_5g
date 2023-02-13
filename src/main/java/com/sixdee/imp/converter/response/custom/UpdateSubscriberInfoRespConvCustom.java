/**
 * 
 */
package com.sixdee.imp.converter.response.custom;

import com.sixdee.fw.converter.response.RespConvCustom;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;

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
public class UpdateSubscriberInfoRespConvCustom extends RespConvCustom {

	/* (non-Javadoc)
	 * @see com.sixdee.fw.converter.response.RespConvInfCustom#endConvertRespCustom(com.sixdee.fw.dto.GenericDTO)
	 */
	@Override
	public GenericDTO endConvertRespCustom(GenericDTO genericDTO)
			throws CommonException {
		// TODO Auto-generated method stub
		return genericDTO;
	}

	/* (non-Javadoc)
	 * @see com.sixdee.fw.converter.response.RespConvInfCustom#startConvertRespCustom(com.sixdee.fw.dto.GenericDTO)
	 */
	@Override
	public GenericDTO startConvertRespCustom(GenericDTO genericDTO)
			throws CommonException {
		// TODO Auto-generated method stub
		return genericDTO;
	}

}
