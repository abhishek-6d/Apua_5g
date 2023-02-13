package com.sixdee.imp.converter.response.custom;

/**
 * 
 * @author Somesh
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
 * <td>April 22,2014 11:36:42 AM</td>
 * <td>Somesh</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.converter.response.RespConvCustom;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;

public class ActDeactOfferRespConvCustom extends RespConvCustom {

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