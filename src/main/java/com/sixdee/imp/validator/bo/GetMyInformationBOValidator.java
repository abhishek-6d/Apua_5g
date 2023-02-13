package com.sixdee.imp.validator.bo;

/**
 * 
 * @author @jith
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
 * <td>March 08,2018 04:26:05 PM</td>
 * <td>@jith</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.validator.bo.BOValidatorCommon;

public class GetMyInformationBOValidator extends BOValidatorCommon {

	/**
	 * This method should be called from Business Objects.
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildValidate(GenericDTO genericDTO) throws CommonException {
		logger.info("Class => GetMyInformationBOValidator :: Method => buildValidate()");
		
		return genericDTO;
	}

}
