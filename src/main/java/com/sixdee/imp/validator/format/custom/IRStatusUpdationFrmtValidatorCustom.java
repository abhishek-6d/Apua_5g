package com.sixdee.imp.validator.format.custom;

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
 * <td>February 18,2014 05:36:06 PM</td>
 * <td>Rahul K K</td>
 * </tr>
 * </table>
 * </p>
 */


import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.validator.format.FrmtValidatorCustom;

public class IRStatusUpdationFrmtValidatorCustom extends FrmtValidatorCustom {

	/**
	 * This method is called from the framework class in Format Validator Layer.
	 * This is called before buildValidateGUIReq() in core implementation class
	 * (ConfigFrmtValidator).
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO startValidateCustom(GenericDTO genericDTO)
			throws CommonException {
		// logger.info("Class => GUILoginFrmtValidatorCustom :: Method =>
		// startValidateCustom()");
		return genericDTO;
	}

	/**
	 * This method is called from the framework class in Format Validator
	 * Layer.This is called after buildValidateGUIReq() in core implementation
	 * class (GUILoginFrmtValidator).
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO endValidateCustom(GenericDTO genericDTO)
			throws CommonException {
		// logger.info("Class => GUILoginFrmtValidatorCustom :: Method =>
		// endValidateCustom()");
		return genericDTO;
	}

}
