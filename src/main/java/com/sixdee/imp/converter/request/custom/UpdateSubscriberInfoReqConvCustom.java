/**
 * 
 */
package com.sixdee.imp.converter.request.custom;

import com.sixdee.fw.converter.request.ReqConvCustom;
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
public class UpdateSubscriberInfoReqConvCustom extends ReqConvCustom {

	/* (non-Javadoc)
	 * @see com.sixdee.fw.converter.request.ReqConvInfCustom#endConvertReqCustom(com.sixdee.fw.dto.GenericDTO)
	 */
	@Override
	public GenericDTO endConvertReqCustom(GenericDTO genericDTO)
			throws CommonException {
		// TODO Auto-generated method stub
		return genericDTO;
	}

	/* (non-Javadoc)
	 * @see com.sixdee.fw.converter.request.ReqConvInfCustom#startConvertReqCustom(com.sixdee.fw.dto.GenericDTO)
	 */
	@Override
	public GenericDTO startConvertReqCustom(GenericDTO genericDTO)
			throws CommonException {
		// TODO Auto-generated method stub
		return genericDTO;
	}

}
