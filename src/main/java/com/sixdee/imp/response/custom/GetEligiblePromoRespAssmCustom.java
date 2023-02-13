package com.sixdee.imp.response.custom;

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
 * <td>April 24,2013 05:44:52 PM</td>
 * <td>Rahul K K</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.response.RespAssmCustom;


public class GetEligiblePromoRespAssmCustom extends RespAssmCustom {

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
		logger.info("Class => GetEligiblePromoRespAssmCustom :: Method => endAssembleRespCustom()");
		return genericDTO;
	}

}
