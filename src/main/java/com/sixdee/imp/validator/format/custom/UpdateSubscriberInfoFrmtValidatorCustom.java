/**
 * 
 */
package com.sixdee.imp.validator.format.custom;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.validator.format.FrmtValidatorCustom;

/**
 * @author Rahul K K
 * @version 1.0.0
 * 
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%">Date</td>
 * <td width="20%">Author</td>
 * <td>Description</td>
 * </tr>
 * <tr>
 * <td>April 24, 2013</td>
 * <td>Rahul K K</td>
 * <td>Created this class</td>
 * </tr>
 * </table>
 * </p>
 */
public class UpdateSubscriberInfoFrmtValidatorCustom extends
		FrmtValidatorCustom {

	/* (non-Javadoc)
	 * @see com.sixdee.fw.validator.format.FrmtValidatorInfCustom#endValidateCustom(com.sixdee.fw.dto.GenericDTO)
	 */
	@Override
	public GenericDTO endValidateCustom(GenericDTO genericDTO) throws CommonException {
		// TODO Auto-generated method stub
		return genericDTO;
	}

	/* (non-Javadoc)
	 * @see com.sixdee.fw.validator.format.FrmtValidatorInfCustom#startValidateCustom(com.sixdee.fw.dto.GenericDTO)
	 */
	@Override
	public GenericDTO startValidateCustom(GenericDTO genericDTO)
			throws CommonException {
		// TODO Auto-generated method stub
		return genericDTO;
	}

}
