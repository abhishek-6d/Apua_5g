package com.sixdee.imp.bo.custom;

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
 * <td>June 20,2013 04:39:52 PM</td>
 * <td>Rahul K K</td>
 * </tr>
 * </table>
 * </p>
 */

import com.sixdee.fw.bo.BOCustom;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;

public class TransferLineBLCustom extends BOCustom {
	/**
	 * This method is called from the framework class in BO Layer. This is called before buildProcess() in core implementation class
	 * (ProcessTargetBL).
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO startProcessCustom(GenericDTO genericDTO) throws CommonException {
		logger.info("Method => startProcessCustom()");
		return genericDTO;
	}

	/**
	 * This method is called from the framework class in BO Layer.This is called after buildProcess() in core implementation class (ProcessTargetBL).
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO endProcessCustom(GenericDTO genericDTO) throws CommonException {
		logger.info("Method => endProcessCustom()");
		return genericDTO;
	}

}
