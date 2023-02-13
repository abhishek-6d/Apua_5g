package com.sixdee.imp.bo;

/**
 * 
 * @author Somesh Soni
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
 * <td>September 13,2013 12:46:40 PM</td>
 * <td>Somesh Soni</td>
 * </tr>
 * </table>
 * </p>
 */

import java.util.List;
import java.util.Map;

import com.sixdee.fw.bo.BOCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.dao.SubsHistoryDAO;
import com.sixdee.imp.dto.MarketingPlanDTO;
import com.sixdee.imp.dto.SubsHistoryDTO;
import com.sixdee.imp.service.serviceDTO.resp.SubscriberHistoryDTO;

public class SubsHistoryBL extends BOCommon {
	
	/**
	 * This method is called from BOCommon. Access the DAO object is to create CampaignDefntion
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildProcess(GenericDTO genericDTO) throws CommonException {

		logger.info("Class => SubsHistoryBL :: Method => buildProcess()");
		
		SubsHistoryDAO  subsHistoryDAO;
		SubsHistoryDTO subsHistoryDTO = null;
		List<SubscriberHistoryDTO> subscriberList;
		Map<Integer, MarketingPlanDTO> maketingPlanMap =null;
		MarketingPlanDTO marketingPlanDTO = null;
		try
		{
			subsHistoryDTO =(SubsHistoryDTO) genericDTO.getObj();
			subsHistoryDAO = new SubsHistoryDAO();
			
			subscriberList = subsHistoryDAO.getTransactionHistory(subsHistoryDTO);
			
			if(subscriberList!=null && subscriberList.size()>0)
			{
				maketingPlanMap = subsHistoryDAO.getMarketingPlanDetails(subsHistoryDTO.getMarketingPlanIds());
			}
			else
			{
				genericDTO.setStatusCode("SC0001");
				genericDTO.setStatus("Details Not Found!!");
			}
			
			if(maketingPlanMap!=null && maketingPlanMap.size()>0)
			{
				for(SubscriberHistoryDTO dto: subscriberList)
				{
					if(dto.getMarketingPlan()==null)
						continue;
					else
					{
						marketingPlanDTO = maketingPlanMap.get(Integer.parseInt(dto.getMarketingPlan()));
						dto.setMarketingPlanId(Integer.parseInt(dto.getMarketingPlan()));
						dto.setMarketingPlan(marketingPlanDTO!=null?marketingPlanDTO.getMarketingPlanName():"");
						dto.setMarketingPlanDesc(marketingPlanDTO!=null?marketingPlanDTO.getMarketingPlanDesc():"");
					}
				}
				
			}
			
			subsHistoryDTO.setSubscriberList(subscriberList);
			
			genericDTO.setStatus("SUCCESS");
			genericDTO.setStatusCode("SC0000");

		}
		catch (Exception e) 
		{
			genericDTO.setStatusCode("SC0001");
			genericDTO.setStatus("Failure !!");
			logger.info("Exception in SubsHistoryBL");
			logger.error(subsHistoryDTO.getTransactionId() +" Transaction id ",e);
		}
		
		finally
		{
			subsHistoryDAO =null;;
			subsHistoryDTO = null;
			subscriberList = null;
			maketingPlanMap =null;
			marketingPlanDTO = null;
		}
					
		return genericDTO;
	}
}
