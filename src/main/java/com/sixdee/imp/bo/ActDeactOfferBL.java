package com.sixdee.imp.bo;

/**
 * 
 * @author Somesh
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
 * <td>April 22,2014 11:36:41 AM</td>
 * <td>Somesh</td>
 * </tr>
 * </table>
 * </p>
 */

import java.util.List;

import com.sixdee.fw.bo.BOCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
//import com.sixdee.imp.dao.ActDeactOfferDAO;
import com.sixdee.imp.dto.ActDeactOfferDTO;

public class ActDeactOfferBL extends BOCommon {
	
	/**
	 * This method is called from BOCommon. Access the DAO object is to create CampaignDefntion
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildProcess(GenericDTO genericDTO) throws CommonException
	{

		logger.info("Class => ActDeactOfferBL :: Method => buildProcess()");

		//ActDeactOfferDAO actDeactOfferDAO;
		ActDeactOfferDTO actDeactOfferDTO = (ActDeactOfferDTO) genericDTO.getObj();
		
		try
		{/*
			//actDeactOfferDAO = new ActDeactOfferDAO();
			if(actDeactOfferDTO.getKeyWord().equalsIgnoreCase("active"))
			{
				logger.info("-----------------Writing CDR---------");
			//	boolean flag = actDeactOfferDAO.writeCDR(actDeactOfferDTO);

				if (flag) {
					actDeactOfferDTO.setStatusCode("SC0000");
					actDeactOfferDTO.setStatusDesc(Cache.cacheMap.get("ACTIVATE_SUCCESS_MESSAGE"));
				} else {
					actDeactOfferDTO.setStatusCode("SC0001");
					actDeactOfferDTO.setStatusDesc(Cache.cacheMap.get("ACTIVATE_FAILURE_MESSAGE"));
				}
			}
			else
			{
				//Deactivation 
				//Get all segment id for particualr subscriber number and offer , call delete segment API one by one 
				
				List<String> allSegments = actDeactOfferDAO.getSegmentIds(actDeactOfferDTO);
				
				logger.info("Segments:::"+allSegments);
				if(allSegments ==null || allSegments.size()==0)
				{
					actDeactOfferDTO.setStatusCode("SC0002");
					actDeactOfferDTO.setStatusDesc("Not Eligible for any Services");
					return genericDTO;
				}
				
				for(String segId:allSegments)
					actDeactOfferDAO.callSegmentAPI(actDeactOfferDTO, segId);
				
				//SC0000-Records Deleted Successfully 
				//SC0001-ALREADY DEACTIVTED
				
				if(actDeactOfferDTO.isSuccess())
				{
					actDeactOfferDTO.setStatusCode("SC0000");
					actDeactOfferDTO.setStatusDesc(Cache.cacheMap.get("DEACTIVATE_SUCCESS_MESSAGE"));
				}
				else
				{
					actDeactOfferDTO.setStatusCode("SC0001");
					actDeactOfferDTO.setStatusDesc(Cache.cacheMap.get("DEACTIVATE_FAILURE_MESSAGE"));	
				}
				
				logger.info("Response Send to Subscriber::"+actDeactOfferDTO.getStatusDesc());
				
			}*/
		}
		catch (Exception e) {
			logger.error("Exception ",e);
		}
		return genericDTO;
	}
}
