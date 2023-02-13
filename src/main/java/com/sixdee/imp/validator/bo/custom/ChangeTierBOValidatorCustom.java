package com.sixdee.imp.validator.bo.custom;

/**
 * 
 * @author Ananth
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
 * <td>December 08,2015 07:13:59 PM</td>
 * <td>Ananth</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.validator.bo.BOValidatorCustom;

public class ChangeTierBOValidatorCustom extends BOValidatorCustom {

	/**
	 * This method is called from Business Objects. This is called before buildValidate() in core implementation class .
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO startValidateCustom(GenericDTO genericDTO) throws CommonException {
		return genericDTO;
	}

	/**
	 * This method is called from Business Objects.This is called after buildValidate() in core implementation class .
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO endValidateCustom(GenericDTO genericDTO) throws CommonException {
		return genericDTO;
	}

}
