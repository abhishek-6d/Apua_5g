package com.sixdee.imp.bo.custom;

/**
 * 
 * @author Rahul
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
 * <td>March 06,2015 11:46:47 AM</td>
 * <td>Rahul</td>
 * </tr>
 * </table>
 * </p>
 */

import com.sixdee.fw.bo.BOCustom;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;

public class GetOfferCodeOnMarketingPlanBLCustom extends BOCustom {
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
