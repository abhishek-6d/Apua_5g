package com.sixdee.imp.bo;

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
 * <td>April 24,2013 05:44:52 PM</td>
 * <td>Rahul K K</td>
 * </tr>
 * </table>
 * </p>
 */

import java.util.ArrayList;

import com.sixdee.fw.bo.BOCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dao.GetEligiblePromoDAO;
import com.sixdee.imp.dto.GetEligiblePromoDTO;
import com.sixdee.imp.dto.ServiceMappingDTO;
import com.sixdee.imp.util.GeneralProcesses;

public class GetEligiblePromoBO extends BOCommon {
	
	/**
	 * This method is called from BOCommon. Access the DAO object is to create CampaignDefntion
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildProcess(GenericDTO genericDTO) throws CommonException {

		logger.info("Class => GetEligiblePromoBL :: Method => buildProcess()");
		
		
		GetEligiblePromoDTO getEligiblePromoDTO = null;
		
		ArrayList<String> eligiblePromos = null;
		
		try{
			getEligiblePromoDTO = (GetEligiblePromoDTO) genericDTO.getObj();
			eligiblePromos = fetchEligiblePromos(getEligiblePromoDTO);
			if(eligiblePromos.size()==0){
				genericDTO.setStatus("SC0005");
			}
		} catch (Exception e) {
			genericDTO.setStatus("SC0004");
		}finally{
			
		}

		
					
			return genericDTO;
		}

	private ArrayList<String> fetchEligiblePromos(
			GetEligiblePromoDTO getEligiblePromoDTO) throws Exception {
		GetEligiblePromoDAO  getEligiblePromoDAO = null;
		GeneralProcesses generalProcesses = null;
		ServiceMappingDTO serviceMappingDTO = null;
		ArrayList<String> eligiblePromos = new ArrayList<String>();
		ArrayList<String> activePromos = null;
		String subScriber = null;
		String tableName = null;
		//String keySet = null;
		try{
			generalProcesses = new GeneralProcesses();
			getEligiblePromoDAO = new GetEligiblePromoDAO();
			subScriber = getEligiblePromoDTO.getSubsNumber();
			tableName = generalProcesses.identifyTable( Cache.cacheMap.get("TRANSACTION_TABLE_PREFIX"),subScriber);
			activePromos = getEligiblePromoDAO.getEligiblePromos(tableName,subScriber);
		//	System.out.println(activePromos);
			if(activePromos != null){
				for(int key:Cache.appMap.keySet()){
					if(!(activePromos.contains(key))){
						serviceMappingDTO = Cache.appMap.get(key);
						if(serviceMappingDTO.getServiceType().equalsIgnoreCase("P"))
							eligiblePromos.add(serviceMappingDTO.getServiceName());
					}
				}
			}else{
				eligiblePromos = activePromos;
			}
		
		}catch (Exception e) {
			logger.error("Exception has occured ",e);
			throw e;
		}
		System.out.println(eligiblePromos);
		return eligiblePromos;
	}
}
