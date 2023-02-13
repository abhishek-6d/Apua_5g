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
 * <td>September 04,2013 07:46:17 PM</td>
 * <td>Somesh Soni</td>
 * </tr>
 * </table>
 * </p>
 */

import java.util.List;

import com.sixdee.fw.bo.BOCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.dao.EligibleSubscriberDAO;
import com.sixdee.imp.dto.EligibleSubscriberDTO;
import com.sixdee.imp.dto.UserDTO;
import com.sixdee.imp.service.serviceDTO.resp.EligibleSubscriberDetails;

public class EligibleSubscriberBL extends BOCommon {
	
	/**
	 * This method is called from BOCommon. Access the DAO object is to create CampaignDefntion
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildProcess(GenericDTO genericDTO) throws CommonException {

		logger.info("Class => EligibleSubscriberBL :: Method => buildProcess()");
		
		EligibleSubscriberDAO  eligibleSubscriberDAO;
		EligibleSubscriberDTO eligibleSubscriberDTO = (EligibleSubscriberDTO) genericDTO.getObj();
		List<EligibleSubscriberDetails> eligibleSubscriberList = null;
		try
		{
			
			eligibleSubscriberDAO = new EligibleSubscriberDAO();
			logger.info("Calling..........CMS ");
			UserDTO userDTO = eligibleSubscriberDAO.validateUser(eligibleSubscriberDTO);
			logger.info("Calling..........Done");
			if(userDTO==null)
			{
				genericDTO.setStatusCode("SC0001");
				genericDTO.setStatus("Invalid User !!");
				return genericDTO;
			}
				
				
			
			//get Marketing plan from Rule DB based on msisdn and month
			List<Object> marketingPlanIds = eligibleSubscriberDAO.getMarketingPlan(eligibleSubscriberDTO);
			
			if(marketingPlanIds!=null && marketingPlanIds.size()>0)
			{
				eligibleSubscriberList = eligibleSubscriberDAO.getMarketingPlanDetails(marketingPlanIds);
				eligibleSubscriberDTO.setEligibleSubscriberList(eligibleSubscriberList);
			}
			else
			{
				genericDTO.setStatusCode("SC0001");
				genericDTO.setStatus("Failure ! MSISDN not Found !!");
				return genericDTO;
			}
			
			if(eligibleSubscriberList==null || eligibleSubscriberList.size()==0)
			{
				genericDTO.setStatus("SC0001");
				genericDTO.setStatus("Marketing Plan not found!!");
				return genericDTO;
			}
			
			genericDTO.setStatus("SUCCESS");
			genericDTO.setStatusCode("SC0000");
			
		}
		catch (Exception e) 
		{
			genericDTO.setStatus("SC0001");
			genericDTO.setStatus("Failure");

			logger.info("Error in EligibleSubscriberBL");
			e.printStackTrace();
		}
		

		
					
		return genericDTO;
	}
}
