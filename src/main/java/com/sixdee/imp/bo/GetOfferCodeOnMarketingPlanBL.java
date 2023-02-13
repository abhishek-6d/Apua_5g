package com.sixdee.imp.bo;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.tomcat.modules.aaa.RealmBase;

import com.sixdee.fw.bo.BOCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.dao.EligibleSubscriberDAO;
import com.sixdee.imp.dao.GetEligiblePromoDAO;
import com.sixdee.imp.dao.GetOfferCodeOnMarketingPlanDAO;
import com.sixdee.imp.dao.SubsHistoryDAO;
import com.sixdee.imp.dto.EligibleSubscriberDTO;
import com.sixdee.imp.dto.GetOfferCodeOnMarketingPlanDTO;
import com.sixdee.imp.dto.MarketingPlanDTO;

public class GetOfferCodeOnMarketingPlanBL extends BOCommon {
	
	/**
	 * This method is called from BOCommon. Access the DAO object is to create CampaignDefntion
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	private static final Logger logger = Logger.getLogger(GetOfferCodeOnMarketingPlanBL.class);
	public GenericDTO buildProcess(GenericDTO genericDTO) throws CommonException {

		
		GetOfferCodeOnMarketingPlanDTO getOfferCodeOnMarketingPlanDTO = (GetOfferCodeOnMarketingPlanDTO) genericDTO.getObj();
		String loggingMessage = null;
		try{
			
			getOfferCodeOnMarketingPlanDTO = (GetOfferCodeOnMarketingPlanDTO) genericDTO.getObj();
			loggingMessage = getOfferCodeOnMarketingPlanDTO.getLoggingMessage();
			logger.info(loggingMessage+" Class => GetOfferCodeOnMarketingPlanBL :: Method => buildProcess()");
			
			HashMap<String, MarketingPlanDTO> offers = getCodesForOffers(loggingMessage,getOfferCodeOnMarketingPlanDTO);
			getOfferCodeOnMarketingPlanDTO.setMarketingPlanMap(offers);			
		}catch(CommonException ce){
			logger.error(loggingMessage+ " Exception occured ",ce);
			getOfferCodeOnMarketingPlanDTO.setStatusCode("SC0003");
			getOfferCodeOnMarketingPlanDTO.setStatusDesc(ce.getMessage());
			
		}finally{
			genericDTO.setObj(getOfferCodeOnMarketingPlanDTO);
		}
		
					
			return genericDTO;
		}
	private HashMap<String, MarketingPlanDTO> getCodesForOffers(String loggingMessage, GetOfferCodeOnMarketingPlanDTO getOfferCodeOnMarketingPlanDTO) throws CommonException{
		EligibleSubscriberDAO eligibleSubscriberDAO = null;
		SubsHistoryDAO subsHistoryDAO = null;
		GetOfferCodeOnMarketingPlanDAO  getOfferCodeOnMarketingPlanDAO;
		HashMap<String, MarketingPlanDTO> marketingPlanMap = null;
		try{
			EligibleSubscriberDTO eligibleSubscriberDTO = new EligibleSubscriberDTO();
			eligibleSubscriberDTO.setUserName(getOfferCodeOnMarketingPlanDTO.getUserName());
			eligibleSubscriberDTO.setPassword(getOfferCodeOnMarketingPlanDTO.getPassword());
			eligibleSubscriberDAO = new EligibleSubscriberDAO();
			if(eligibleSubscriberDAO.validateUser(eligibleSubscriberDTO,false)!=null){
				String marketingPlanId = getOfferCodeOnMarketingPlanDTO.getMarketingPlanId();
				String marketingPlanName = null;
				
				if(marketingPlanId==null||marketingPlanId.trim().equals("")){
					marketingPlanId = getOfferCodeOnMarketingPlanDTO.getMarketingPlanName();	
				}
				String[] stk = marketingPlanId.split(",");
				ArrayList<Integer> marketingPlanList = new ArrayList<Integer>();
				for(String s : stk){
					marketingPlanList.add(Integer.parseInt(s));
				}
				getOfferCodeOnMarketingPlanDAO = new GetOfferCodeOnMarketingPlanDAO();
				marketingPlanMap = getOfferCodeOnMarketingPlanDAO.getOfferCodes(loggingMessage,marketingPlanList);
				logger.debug(loggingMessage+" : "+marketingPlanMap);
				if(marketingPlanMap==null){
					throw new CommonException("No offers available for marketing plans "+marketingPlanList);
				}
			}else{
				
				throw new CommonException("User Validation failed");
			}
			
		}catch(Exception e){
			throw new CommonException(e.getMessage());
		}
		finally{
			
		}
		return marketingPlanMap;
	}
}
