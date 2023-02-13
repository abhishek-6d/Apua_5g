package com.sixdee.imp.response.custom;

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
 * <td>September 03,2013 11:27:12 AM</td>
 * <td>Somesh Soni</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.response.RespAssmCustom;


public class UpdateAccountRespAssmCustom extends RespAssmCustom {

	/**
	 * This method is called from the framework class in Response Assembler Layer. This is called before buildAssembleGUIResp() in core implementation
	 * class .
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO startAssembleRespCustom(GenericDTO genericDTO) throws CommonException {
		return genericDTO;
	}

	/**
	 * This method is called from the framework class in Response Assembler Layer.This is called after buildAssembleGUIResp() in core implementation
	 * class .
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO endAssembleRespCustom(GenericDTO genericDTO) throws CommonException {
		logger.info("Class => UpdateAccountRespAssmCustom :: Method => endAssembleRespCustom()");
		return genericDTO;
	}

}