package com.sixdee.imp.request.custom;

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


import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmCustom;

public class GetOfferCodeOnMarketingPlanReqAssmCustom extends ReqAssmCustom {

	/**
	 * This method is called from the framework class in Request Assembler
	 * Layer. This is called before buildAssembleGUIReq() in core implementation
	 * class (CampaignDetailsReqAssm).
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO startAssembleReqCustom(GenericDTO genericDTO)
			throws CommonException {
		// logger.info("Class => GUILoginReqAssmCustom :: Method =>
		// startAssembleReqCustom()");
		return genericDTO;
	}

	/**
	 * This method is called from the framework class in Request Assembler
	 * Layer.This is called after buildAssembleGUIReq() in core implementation
	 * class (CampaignDetailsReqAssm).
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO endAssembleReqCustom(GenericDTO genericDTO)
			throws CommonException {
		// logger.info("Class => GUILoginReqAssmCustom :: Method =>
		// endAssembleReqCustom()");
		return genericDTO;
	}

}
