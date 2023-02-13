package com.sixdee.imp.validator.bo;

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
 * <td>May 14,2013 06:02:42 PM</td>
 * <td>Nithin Kunjappan</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.validator.bo.BOValidatorCommon;

public class SubscriberProfileBOValidator extends BOValidatorCommon {

	/**
	 * This method should be called from Business Objects.
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildValidate(GenericDTO genericDTO) throws CommonException {
		//logger.info("Class => SubscriberProfileBOValidator :: Method => buildValidate()");
		
		return genericDTO;
	}

}
