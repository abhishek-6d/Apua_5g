package com.sixdee.imp.validator.bo;

/**
 * 
 * @author Athul Gopal
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
 * <td>April 21,2015 11:42:09 AM</td>
 * <td>Athul Gopal</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.validator.bo.BOValidatorCommon;

public class MerchantRedeemBOValidator extends BOValidatorCommon {

	/**
	 * This method should be called from Business Objects.
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildValidate(GenericDTO genericDTO) throws CommonException {
		logger.info("Class => MerchantRedeemBOValidator :: Method => buildValidate()");
		
		return genericDTO;
	}

}
