package com.sixdee.imp.converter.request.custom;

/**
 * 
 * @author Somesh Soni
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
 * <td>September 04,2013 03:17:03 PM</td>
 * <td>Somesh Soni</td>
 * </tr>
 * </table>
 * </p>
 */


import com.sixdee.fw.converter.request.ReqConvCustom;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;

public class UpdatePSOReqConvCustom extends ReqConvCustom {

	/**
	 * This method is called from the framework class in Request Converter
	 * Layer. This is called before buildConvertGUIReq() in core implementation
	 * class (ConfigReqConv).
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO startConvertReqCustom(GenericDTO genericDTO)
			throws CommonException {
		// logger.info("Class => GUILoginReqConvCustom :: Method =>
		// startConvertReqCustom()");
		return genericDTO;
	}

	/**
	 * This method is called from the framework class in Request Converter
	 * Layer.This is called after buildConvertGUIReq() in core implementation
	 * class (ConfigReqConv).
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO endConvertReqCustom(GenericDTO genericDTO)
			throws CommonException {
		// logger.info("Class => GUILoginReqConvCustom :: Method =>
		// endConvertReqCustom()");
		return genericDTO;
	}
}
