package com.sixdee.imp.validator.format;
/**
 * 
 * @author Jiby Jose
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
 * <td>May 17,2013 01:19:06 PM</td>
 * <td>Jiby Jose</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.validator.format.FrmtValidatorGUICommon;

public class MergeAccountFrmtValidator extends FrmtValidatorGUICommon {
	
	/**
	 * This method is called from the framework class in Format Validator Layer.
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildValidateGUIReq(GenericDTO genericDTO) throws CommonException {
		logger.debug("Method =>buildValidateGUIReq()");
		return genericDTO;
	}

}
