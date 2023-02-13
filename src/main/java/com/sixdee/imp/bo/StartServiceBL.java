package com.sixdee.imp.bo;

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
 * <td>January 02,2015 07:09:43 PM</td>
 * <td>Nithin Kunjappan</td>
 * </tr>
 * </table>
 * </p>
 */

import com.sixdee.fw.bo.BOCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.CommonUtil;
import com.sixdee.imp.dao.StartServiceDAO;
import com.sixdee.imp.dto.StartServiceDTO;

public class StartServiceBL extends BOCommon {
	
	/**
	 * This method is called from BOCommon. Access the DAO object is to create CampaignDefntion
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildProcess(GenericDTO genericDTO) throws CommonException {

		logger.info("Class => StartServiceBL :: Method => buildProcess()");
		
		StartServiceDAO  startServiceDAO;
		StartServiceDTO startServiceDTO = (StartServiceDTO) genericDTO.getObj();
		CommonUtil commonUtil = new CommonUtil();
		try{
			startServiceDAO=new StartServiceDAO();	
			logger.info("TXN ID: "+startServiceDTO.getTransactionId()+" for Request with MSISDN:"+startServiceDTO.getSubscriberNumber()+" Account Type:"+startServiceDTO.getAccountType());
			if(startServiceDTO.getAccountType()==9){
				if(startServiceDAO.checkPostpaidProfileDetails(startServiceDTO))
				{
						genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "SERVICE_SUCCESS_"+Cache.defaultLanguageID, startServiceDTO.getTransactionId());
						startServiceDTO.setStatusCode(genericDTO.getStatusCode());
						startServiceDTO.setStatusDesc(genericDTO.getStatus());
						logger.info(startServiceDTO.getStatusDesc());
					
				}
				else
				{
					genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "SERVICE_FAILURE_"+Cache.defaultLanguageID, startServiceDTO.getTransactionId());
					startServiceDTO.setStatusCode(genericDTO.getStatusCode());
					startServiceDTO.setStatusDesc(genericDTO.getStatus());
					logger.info(startServiceDTO.getStatusDesc());
				}
			}
			else {
				startServiceDTO.setAccountType(14);
			if(startServiceDAO.checkProfileDetails(startServiceDTO))
			{
				if(startServiceDAO.insertInstantDetails(startServiceDTO))
				{
					genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "SERVICE_SUCCESS_"+Cache.defaultLanguageID, startServiceDTO.getTransactionId());
					startServiceDTO.setStatusCode(genericDTO.getStatusCode());
					startServiceDTO.setStatusDesc(genericDTO.getStatus());
					logger.info(startServiceDTO.getStatusDesc());
				}
				else
				{
					genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "SERVICE_FAILURE_"+Cache.defaultLanguageID, startServiceDTO.getTransactionId());
					startServiceDTO.setStatusCode(genericDTO.getStatusCode());
					startServiceDTO.setStatusDesc(genericDTO.getStatus());
					logger.info(startServiceDTO.getStatusDesc());
				}
			}
			else
			{
				genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "SERVICE_FAILURE_"+Cache.defaultLanguageID, startServiceDTO.getTransactionId());
				startServiceDTO.setStatusCode(genericDTO.getStatusCode());
				startServiceDTO.setStatusDesc(genericDTO.getStatus());
				logger.info(startServiceDTO.getStatusDesc());
				
			}
			}
			 
				
				
			
		}
		catch (Exception e) {
			logger.error("Exception Occured:"+e.getMessage());
		}
		
			return genericDTO;
		}
}
