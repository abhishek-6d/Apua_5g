/**
 * 
 */
package com.sixdee.imp.bo;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.sixdee.fw.bo.BOCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.util.LoyaltyTransactionStatus;
import com.sixdee.imp.dao.RestoreDAO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.LoyaltyTransactionTabDTO;
import com.sixdee.imp.dto.RestoreDTO;
import com.sixdee.imp.util.LoyalityCommonTransaction;
import com.sixdee.imp.util.LoyalityTransactionConstants;

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
public class RestoreBO extends BOCommon {

	
	private Logger logger = Logger.getLogger(RestoreBO.class);
	public GenericDTO buildProcess(GenericDTO genericDTO)
			throws CommonException {
		RestoreDTO restoreDTO = null;		
		LoyaltyProfileTabDTO loyaltyProfileTabDTO = null;
		RestoreDAO restoreDAO = null;
		double points = 0;
		LoyalityCommonTransaction loyalityCommonTransaction=new LoyalityCommonTransaction();
		HashMap<String, String> loyaltyTransactionMap=null;
		try{
			restoreDTO = (RestoreDTO) genericDTO.getObj();
			
			points = restoreDTO.getPoints();
				logger.info("Restoring points for Loyalty Id ["+restoreDTO.getLoyaltyId()+"] " +
						"& Subscriber Number ["+restoreDTO.getSubsNo()+"]");
				restoreDAO = new RestoreDAO();
				loyaltyProfileTabDTO = restoreDAO.getLoyaltyDetails(restoreDTO.getLoyaltyId(), (points));
				LoyaltyTransactionTabDTO loyaltyTransactionTabDTO = new LoyaltyTransactionTabDTO();
				loyaltyTransactionMap=new HashMap<String, String>();//added S
			/*	loyaltyTransactionTabDTO.setLoyaltyID(loyaltyProfileTabDTO.getLoyaltyID());
				loyaltyTransactionTabDTO.setSubscriberNumber(restoreDTO.getSubsNo());
				loyaltyTransactionTabDTO.setCurRewardPoints(restoreDTO.getPoints());
				loyaltyTransactionTabDTO.setCurStatusPoints(restoreDTO.getStatusPoints());
				loyaltyTransactionTabDTO.setPreRewardPoints(loyaltyProfileTabDTO.getRewardPoints());
				loyaltyTransactionTabDTO.setPreStatusPoints(loyaltyProfileTabDTO.getStatusPoints());
				loyaltyTransactionTabDTO.setRewardPoints(restoreDTO.getPoints()+loyaltyProfileTabDTO.getRewardPoints());
				loyaltyTransactionTabDTO.setStatusPoints(restoreDTO.getStatusPoints()+loyaltyProfileTabDTO.getStatusPoints());
				loyaltyTransactionTabDTO.setStatusID(LoyaltyTransactionStatus.rewardsPointsAdded);*/
				
				
	    		loyaltyTransactionMap.put(LoyalityTransactionConstants.loyaltyID,String.valueOf(loyaltyProfileTabDTO.getLoyaltyID()));//added S
	    		loyaltyTransactionMap.put(LoyalityTransactionConstants.subscriberNumber,restoreDTO.getSubsNo());//added S
	    		loyaltyTransactionMap.put(LoyalityTransactionConstants.curRewardPoints,String.valueOf(restoreDTO.getPoints()));//added S
	    		loyaltyTransactionMap.put(LoyalityTransactionConstants.curStatusPoints,String.valueOf(restoreDTO.getStatusPoints()));//added S
	    		loyaltyTransactionMap.put(LoyalityTransactionConstants.preRewardPoints,String.valueOf(loyaltyProfileTabDTO.getRewardPoints()));//added S
	    		loyaltyTransactionMap.put(LoyalityTransactionConstants.preStatusPoints,String.valueOf(loyaltyProfileTabDTO.getStatusPoints()));//added S
	    		loyaltyTransactionMap.put(LoyalityTransactionConstants.rewardPoints,String.valueOf(restoreDTO.getPoints()+loyaltyProfileTabDTO.getRewardPoints()));//added S
	    		loyaltyTransactionMap.put(LoyalityTransactionConstants.statusPoints,String.valueOf(restoreDTO.getStatusPoints()+loyaltyProfileTabDTO.getStatusPoints()));//added S
	    		loyaltyTransactionMap.put(LoyalityTransactionConstants.statusId,String.valueOf(LoyaltyTransactionStatus.rewardsPointsAdded));//added S
	    		loyaltyTransactionTabDTO=loyalityCommonTransaction.loyaltyTransactionSetter(loyaltyTransactionTabDTO, loyaltyTransactionMap);//added S
				
				restoreDAO.saveLoyaltyTransacation(loyaltyTransactionTabDTO);
			
		}catch (Exception e) {
			logger.error("Exception occured ",e);
		}finally{
			loyalityCommonTransaction=null;
		}
		return genericDTO;
	}

	
}
