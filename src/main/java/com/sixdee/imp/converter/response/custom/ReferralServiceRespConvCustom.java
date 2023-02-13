package com.sixdee.imp.converter.response.custom;

/**
 * 
 * @author Nithin Kunjappan
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
 * <td>May 21,2015 07:10:21 PM</td>
 * <td>Nithin Kunjappan</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.converter.response.RespConvCustom;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;

public class ReferralServiceRespConvCustom extends RespConvCustom {

	/**
	 * This method is called from the framework class in Response Converter Layer. This is called before buildConvertGUIResp() in core implementation
	 * class.
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO startConvertRespCustom(GenericDTO genericDTO) throws CommonException {
		return genericDTO;
	}

	/**
	 * This method is called from the framework class in Response Converter Layer.This is called after buildConvertGUIResp() in core implementation
	 * class.
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO endConvertRespCustom(GenericDTO genericDTO) throws CommonException {
		return genericDTO;
	}
}
