package com.sixdee.imp.simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.sixdee.NotificationModule.NotificationTokens;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dao.TableDetailsDAO;
import com.sixdee.imp.dao.TableInfoDAO;
import com.sixdee.imp.dto.AccountNumberTabDTO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.NationalNumberTabDTO;
import com.sixdee.imp.dto.SubscriberDetailsDTO;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;
import com.sixdee.imp.util.CRMCalling;

public class CorporateLineRemove {

	Logger logger = Logger.getLogger(CorporateLineRemove.class);

	Map<Long, Set<String>> loyaltyLineMap = new HashMap<Long, Set<String>>();

	Map<String, SubscriberNumberTabDTO> lineDetailsInformationMap = new HashMap<String, SubscriberNumberTabDTO>();

	Map<String, Double> accountPointsMap = new HashMap<String, Double>();


	public void lineRemove(String number) {

		TableDetailsDAO tableDetailsDAO = null;
		CRMCalling calling = null;
		LoyaltyAccountRemoveHistoryDTO loyaltyAccountRemoveHistoryDTO = null;
		Set<String> linesList = null;
		Map<String, SubscriberDetailsDTO> crmSubscriberList = null;
		Map<String,List<NationalNumberTabDTO>> crmSubCustIdMap = null;
		Set<String> lineRemoveList = null;
		LoyaltyProfileTabDTO crmProfileDTO = null;
		Object[] objects = null;
		List<NationalNumberTabDTO> lmsNationalList = null;
		try {

			getAllLoyaltyLines(number);
			logger.info(loyaltyLineMap);

			tableDetailsDAO = new TableDetailsDAO();
			calling = new CRMCalling();

			// Loop the loyalty IDs

			for (Long loyaltyId : loyaltyLineMap.keySet()) {

				try {

					logger.info("******************************** "+loyaltyId+"  ***********************************************");
					// Loop the lines for perticular loyalty ID
					linesList = loyaltyLineMap.get(loyaltyId);

					loyaltyAccountRemoveHistoryDTO = new LoyaltyAccountRemoveHistoryDTO();
					loyaltyAccountRemoveHistoryDTO.setLoyaltyId(loyaltyId);
					loyaltyAccountRemoveHistoryDTO.setLoyaltyLinesCount(linesList.size());

					//crmSubscriberList = new HashMap<String, SubscriberDetailsDTO>();
					crmSubCustIdMap = new HashMap<String, List<NationalNumberTabDTO>>();
					lineRemoveList = new HashSet<String>();

					for (String lineNumber : linesList) {
						
						crmProfileDTO = new LoyaltyProfileTabDTO();
						String templateID=calling.getBasicSubscriberInfo(crmProfileDTO, lineNumber, "Tran_" + lineNumber);
						lineDetailsInformationMap.get(lineNumber).setAccountCategory(crmProfileDTO.getCategory());

						// This is for corporate lines
						if (templateID!=null&&templateID.trim().equalsIgnoreCase(NotificationTokens.createLoyalty_loyalty_AccCategory_id+""))
						{
							lineRemoveList.add(lineNumber);
							crmProfileDTO = null;
							continue;
							
						}else if(crmProfileDTO.getCustIdList()==null||crmProfileDTO.getCustIdList().size()<1)
						{
							lineRemoveList.add(lineNumber);
							crmProfileDTO = null;
							continue;
						}
						
						if(crmProfileDTO.getCustIdList()!=null)
							crmSubCustIdMap.put(lineNumber,crmProfileDTO.getCustIdList());
						
						//objects = calling.getAllLineNumber(crmProfileDTO, "Tran_" + lineNumber);
						//crmSubscriberList.putAll((Map<String, SubscriberDetailsDTO>) objects[0]);
						//crmProfileDTO = null;

					}// Lines Loop


					logger.info("LMS Registered List " + linesList);
					//logger.info("CRM Lines List      " + crmSubscriberList.keySet());
					

					// Getting CustIds which all are registered
					lmsNationalList = tableDetailsDAO.getNationalNumberDetailsByLoyaltyId(loyaltyId);
					
					for(NationalNumberTabDTO tabDTO:lmsNationalList)
					{
						tabDTO.setNationalNumber(tabDTO.getNationalNumber().replaceFirst("^0+(?!$)", ""));
					}
					
					logger.info("LMS Cust Id List " + lmsNationalList);
					logger.info("CRM Line Cust List "+crmSubCustIdMap);
					
					
					
					////////////////////////////////////////
					
					/*List<NationalNumberTabDTO> list=new ArrayList<NationalNumberTabDTO>();
					NationalNumberTabDTO tabDTO=new NationalNumberTabDTO();
					tabDTO.setNationalNumber("1234");
					tabDTO.setIdType("pass");
					
					list.add(tabDTO);
					
					
					List<NationalNumberTabDTO> list1=new ArrayList<NationalNumberTabDTO>();
					tabDTO=new NationalNumberTabDTO();
					tabDTO.setNationalNumber("1234");
					tabDTO.setIdType("pass");
					
					list1.add(tabDTO);
					
					tabDTO=new NationalNumberTabDTO();
					tabDTO.setNationalNumber("12340_4543534");
					tabDTO.setIdType("National Number");
					
					list1.add(tabDTO);
					
					crmSubCustIdMap.put("96889634524", list);
					crmSubCustIdMap.put("96889634525", list1);*/
					/////////////////////////////////////////
					
					
					// Inserting mismatch lines
					insertLinesMismatch(loyaltyId, lmsNationalList, crmSubCustIdMap,lineRemoveList);
					
					logger.info("Remove List "+lineRemoveList);

					// check if any lines exist with LMS DB,it means some lines
					// need to remove from the loyalty account
					if (lineRemoveList.size() > 0) {
						
						removeLoyaltyLines(lineRemoveList, loyaltyId, loyaltyAccountRemoveHistoryDTO);

					}

				} catch (Exception e) {
					logger.info("", e);
					insertFailureLoyalty(loyaltyId,e.getLocalizedMessage());
				} finally {
					loyaltyAccountRemoveHistoryDTO = null;
					linesList = null;
					crmSubscriberList = null;
					crmSubCustIdMap = null;
					lineRemoveList = null;
					crmProfileDTO = null;
					objects = null;
					lmsNationalList = null;
					loyaltyLineMap.get(loyaltyId).clear();
				}

				logger.info("******************************** "+loyaltyId+"  *********************************************** END ");
				
			}// Loyalty Loop
			

		} catch (Exception e) {
			logger.info("", e);
		} finally {
			tableDetailsDAO = null;
			calling = null;
		}
		
		
		
		

	}// lineRemove

	public void removeLoyaltyLines(Set<String> linesList1, Long loyaltyId, LoyaltyAccountRemoveHistoryDTO accountRemoveHistoryDTO)throws Exception {

		Set<String> lineAccountList = new HashSet<String>();
		Map<String, LoyaltyLineRemoveHistoryDTO> lineRemoveDetailsMap = new HashMap<String, LoyaltyLineRemoveHistoryDTO>();
		TableDetailsDAO tableDetailsDAO = null;
		TableInfoDAO infoDAO = new TableInfoDAO();
		LoyaltyProfileTabDTO profileTabDTO = null;

		Session session = null;
		Transaction transaction = null;
		String sql = null;

		Double rewardPoints = 0.0;
		Double statusPoints=0.0;

		try {

			tableDetailsDAO = new TableDetailsDAO();

			// getting Loyaltyid
			profileTabDTO = tableDetailsDAO.getLoyaltyProfile(loyaltyId);

			accountRemoveHistoryDTO.setLoyaltyLinesFixedCount(linesList1.size());
			accountRemoveHistoryDTO.setLoyaltyRewardPoints(profileTabDTO.getRewardPoints());
			accountRemoveHistoryDTO.setLoyaltyStatusPoints(profileTabDTO.getStatusPoints());
			accountRemoveHistoryDTO.setPresentTierID(profileTabDTO.getTierId());

			Set<Long> linesList = new HashSet<Long>();

			for (String lineNumber : linesList1) {

				linesList.add(Long.parseLong(lineNumber.trim()));

				if(lineDetailsInformationMap.get(lineNumber).getAccountNumber()!=null)
				  lineAccountList.add(lineDetailsInformationMap.get(lineNumber).getAccountNumber());

				LoyaltyLineRemoveHistoryDTO historyDTO = new LoyaltyLineRemoveHistoryDTO();
				historyDTO.setLoyaltyId(loyaltyId);
				historyDTO.setLineNumber(lineNumber);
				historyDTO.setAccountNumber(lineDetailsInformationMap.get(lineNumber).getAccountNumber());
				historyDTO.setAccountCategory(lineDetailsInformationMap.get(lineNumber).getAccountCategory());
				historyDTO.setAccountType(lineDetailsInformationMap.get(lineNumber).getAccountTypeId());
				historyDTO.setLineRewardPoints(lineDetailsInformationMap.get(lineNumber).getPoints());
				historyDTO.setAccountRewardPoints((accountPointsMap.get(historyDTO.getAccountNumber())==null?0.0:accountPointsMap.get(historyDTO.getAccountNumber())));
				historyDTO.setLineAddedDate(lineDetailsInformationMap.get(lineNumber).getCreateDate());

				lineRemoveDetailsMap.put(lineNumber, historyDTO);

				// For Line Points
				rewardPoints = rewardPoints + lineDetailsInformationMap.get(lineNumber).getPoints();
			}

			// For Account Points
			for (String accountNumber : lineAccountList) {
				if(accountPointsMap.get(accountNumber)!=null)
				   rewardPoints = rewardPoints + accountPointsMap.get(accountNumber);
			}
			
			

			logger.info("Loyalty Id : " + loyaltyId + " Remove Lines List : " + linesList + " Crrosponding Accounts : " + lineAccountList + " Points " + rewardPoints);

			session = HiberanteUtil.getSessionFactory().openSession();

			/*
			 * // For Prepaid Number sql="SELECT SUM(points) FROM
			 * SUBSCRIBER_NUMNER_ENTITY_0 where subscriberNumber in (:list)";
			 * 
			 * Query
			 * query=session.createQuery(sql).setParameterList("list",linesList);
			 * 
			 * rewardPoints=(Double)query.uniqueResult();
			 *  // For Postpaid sql="SELECT SUM(points) FROM
			 * ACCOUNT_NUMBER_ENTITY_0 where accountNumber in (:list)";
			 * 
			 * query=session.createQuery(sql).setParameterList("list",lineAccountList);
			 * 
			 * rewardPoints=rewardPoints+(Double)query.uniqueResult();
			 */

			
			if(profileTabDTO.getTierId()>1)
			{
				statusPoints=accountRemoveHistoryDTO.getLoyaltyStatusPoints()-(Cache.tierInfoMap.get(profileTabDTO.getTierId()).getMinValue());
				accountRemoveHistoryDTO.setRefreshStatusPoints(Double.valueOf(Cache.tierInfoMap.get(profileTabDTO.getTierId()).getMinValue()));
			}else{
				accountRemoveHistoryDTO.setRefreshStatusPoints(profileTabDTO.getStatusPoints());
			}
			
			accountRemoveHistoryDTO.setRefreshRewardPoints(accountRemoveHistoryDTO.getLoyaltyRewardPoints() - rewardPoints);
			

			session.close();

			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();

			logger.info("Going to Remove this Line Number " + linesList);

			// Removing from the Subscriber Number Table
			sql = "DELETE from SUBSCRIBER_NUMNER_ENTITY_0 where subscriberNumber in (:list) and loyaltyID=:lid ";

			Query query = session.createQuery(sql).setParameter("lid", loyaltyId).setParameterList("list", linesList);

			logger.info("Delete Sunscriber Number " + query.executeUpdate());

			if(lineAccountList!=null&&lineAccountList.size()>0)
			{
				// checking which all account number exist
				sql = "SELECT accountNumber FROM SUBSCRIBER_NUMNER_ENTITY_0 where accountNumber in (:list) and loyaltyID=:lid ";
	
				query = session.createQuery(sql).setParameter("lid", loyaltyId).setParameterList("list", lineAccountList);
	
				List<Long> existAccountNumberList = query.list();
	
				lineAccountList.removeAll(existAccountNumberList);
			}

			if (lineAccountList != null && lineAccountList.size() > 0) {
				logger.info("Going to Remove this Accounts " + lineAccountList);

				// Removing from the account number Table
				sql = "DELETE from ACCOUNT_NUMBER_ENTITY_0 where accountNumber in (:list) and loyaltyID=:lid ";

				query = session.createQuery(sql).setParameter("lid", loyaltyId).setParameterList("list", lineAccountList);

				logger.info("Delete Account Number " + query.executeUpdate());
			}

			// Removing form the loyalty registered number table

			sql = "DELETE from LOYALTY_REG_NUMBER_ENTITY_0 where linkedNumber in (:list) and loyaltyID=:lid ";

			query = session.createQuery(sql).setParameter("lid", loyaltyId).setParameterList("list", linesList1);

			logger.info("Delete Loyalty Registered Number " + query.executeUpdate());

			// Deducting those many points form the loyalty table

			sql = " UPDATE LOYALTY_PROFILE_0 SET REWARD_POINTS=:rewardPoints,STATUS_POINTS=:statusPoints,COUNTER=:updateCounter " + " WHERE LOYALTY_ID=:lid AND COUNTER=:counter ";

			query = session.createSQLQuery(sql).setParameter("rewardPoints", (accountRemoveHistoryDTO.getRefreshRewardPoints() < 0 ? 0 : accountRemoveHistoryDTO.getRefreshRewardPoints())).setParameter("statusPoints", accountRemoveHistoryDTO.getRefreshStatusPoints()).setParameter("updateCounter",
					(profileTabDTO.getCounter() >= Integer.MAX_VALUE ? 1 : profileTabDTO.getCounter() + 1)).setParameter("lid", loyaltyId).setParameter("counter", profileTabDTO.getCounter());

			logger.info("Update Loyalty Profile Account " + query.executeUpdate());

			sql = " insert into LOYALTY_LINE_REMOVED_TRAN1 " + " select * from " + infoDAO.getLoyaltyTransactionDBTable("" + loyaltyId) + " where LOYALTY_ID=:lid and SUBSCRIBER_NUMBER in (:list)";

			query = session.createSQLQuery(sql).setParameter("lid", loyaltyId).setParameterList("list", linesList);

			logger.info("Insert History from Transaction " + query.executeUpdate());

			sql = "DELETE from  " + infoDAO.getLoyaltyTransactionDBTable("" + loyaltyId) + " where LOYALTY_ID=:lid and SUBSCRIBER_NUMBER in (:list)";

			query = session.createSQLQuery(sql).setParameter("lid", loyaltyId).setParameterList("list", linesList);

			logger.info("Delete History Transaction " + query.executeUpdate());
			
			if(rewardPoints>0)
			{
			  logger.info("Loaylty id "+loyaltyId+" Reward Points Expiry "+rewardPoints);
			  removeTranSummary(session,loyaltyId,rewardPoints,true);
			}
			if(statusPoints>0)
			{
				logger.info("Loaylty id "+loyaltyId+" Status Points Expiry "+rewardPoints);
				removeTranSummary(session,loyaltyId,statusPoints,false);
			}

			// inserting Loyalty Remove History

			session.save(accountRemoveHistoryDTO);

			for (String lineNumber : lineRemoveDetailsMap.keySet()) {
				session.save(lineRemoveDetailsMap.get(lineNumber));
			}

			transaction.commit();

		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();

			logger.info("", e);
			throw e;
		} finally {
			if (session != null) {
				session.close();
				session = null;
			}
			transaction = null;
			sql = null;
			lineAccountList = null;
			profileTabDTO = null;
			tableDetailsDAO = null;
			infoDAO = null;
			lineRemoveDetailsMap=null;
		}

	}// removeLoyaltyLines

	public void getAllLoyaltyLines(String number) {

		Session session = null;

		try {

			session = HiberanteUtil.getSessionFactory().openSession();
			String sql = "from SUBSCRIBER_NUMNER_ENTITY_0 ";

			if(number!=null&&!number.trim().equals(""))
				sql+=" where loyaltyID="+number;
			
			List<SubscriberNumberTabDTO> list = session.createQuery(sql).list();

			if (list != null && list.size() > 0) {
				for (SubscriberNumberTabDTO subscriberNumberTabDTO : list) {

					if (loyaltyLineMap.get(subscriberNumberTabDTO.getLoyaltyID()) != null) {
						loyaltyLineMap.get(subscriberNumberTabDTO.getLoyaltyID()).add("" + subscriberNumberTabDTO.getSubscriberNumber());
					} else {
						Set<String> set = new HashSet<String>();
						set.add("" + subscriberNumberTabDTO.getSubscriberNumber());
						loyaltyLineMap.put(subscriberNumberTabDTO.getLoyaltyID(), set);
						set = null;
					}

					lineDetailsInformationMap.put("" + subscriberNumberTabDTO.getSubscriberNumber(), subscriberNumberTabDTO);

				}
			}

			list.clear();
			list = null;

			sql = "from ACCOUNT_NUMBER_ENTITY_0";

			List<AccountNumberTabDTO> list2 = session.createQuery(sql).list();

			if (list2 != null && list2.size() > 0) {
				for (AccountNumberTabDTO accountNumberTabDTO : list2) {
					accountPointsMap.put(accountNumberTabDTO.getAccountNumber(), accountNumberTabDTO.getPoints());
				}
			}

			list2.clear();
			list2 = null;

		} catch (Exception e) {
			logger.info("", e);
		} finally {

			if (session != null)
				session.close();

			session = null;
		}

	}// getAllLoyaltyLines

	private void removeTranSummary(Session session, long loyaltyId, double points, boolean isRewardPoint) throws Exception {
		Criteria ctr = null;
		int offset = 0;
		int limit = 0;
		double inputPoints = points;
		List<LoyaltyTranSummarisedDTO> list = new ArrayList<LoyaltyTranSummarisedDTO>();

		try {
			while (inputPoints > 0) {
				
				//System.out.println(inputPoints);
				
				offset = 0;
				;
				limit = offset + 10;
				
				ctr = session.createCriteria(LoyaltyTranSummarisedDTO.class);
				if (isRewardPoint)
					ctr.add(Restrictions.gt("rewardPoint", 0.0));
				else
					ctr.add(Restrictions.gt("statusPoint", 0.0));
				
				ctr.add(Restrictions.eq("loyaltyId",loyaltyId));
				
				ctr.addOrder(Order.asc("createDate"));
				ctr.setFirstResult(offset);
				ctr.setMaxResults(limit);
				
				list = ctr.list();

				logger.info(ctr);
				
				if(list==null||list.size()==0)
					return;
				
				for (LoyaltyTranSummarisedDTO dto : list) {
					
					if (isRewardPoint) {
						if (dto.getRewardPoint() >= inputPoints) {
							updateTranSummary(session, dto, inputPoints, isRewardPoint);
							inputPoints = 0;
							break;
						} else {
							inputPoints = inputPoints - dto.getRewardPoint();
							updateTranSummary(session, dto, dto.getRewardPoint(), isRewardPoint);
						}
					} else {
						if (dto.getStatusPoint() >= inputPoints) {
							updateTranSummary(session, dto, inputPoints, isRewardPoint);
							inputPoints = 0;
							break;
						} else {
							inputPoints = inputPoints - dto.getStatusPoint();
							updateTranSummary(session, dto, dto.getStatusPoint(), isRewardPoint);
						}
					}

				}

			}
		} finally {
			ctr = null;
			list = null;

		}

	}

	private void updateTranSummary(Session session, LoyaltyTranSummarisedDTO dto, double points, boolean isRewards) throws Exception {
		Query query = null;
		try {
			if (isRewards)
				query = session.createQuery("update LoyaltyTranSummarisedDTO set rewardPoint=rewardPoint-? where loyaltyId=? and createDate=?");
			else
				query = session.createQuery("update LoyaltyTranSummarisedDTO set statusPoint=statusPoint-? where loyaltyId=? and createDate=?");

			query.setParameter(0, points);
			query.setParameter(1, dto.getLoyaltyId());
			query.setParameter(2, dto.getCreateDate());

			logger.info("Loyalty ID "+dto.getLoyaltyId()+"   "+points);
			
			query.executeUpdate();

		} finally {
			query = null;
		}

	}//updateTranSummary
	
	
	public void insertFailureLoyalty(Long loyaltyId,String msg)
	{
		
		Session session=null;
		Transaction transaction=null;
		
		try{
			
			session=HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			String sql="insert into LOYALTY_REMOVED_FAILURE (ID,LOYALTY_ID,MSG) values (LOYALTY_LINE_REMOVE_SEQ.nextVal,"+loyaltyId+",'"+msg+"')";
			
			session.createSQLQuery(sql).executeUpdate();
			
			transaction.commit();
			
			
		}catch (Exception e) {
			logger.info("",e);
			
			if(transaction!=null)
				transaction.rollback();
			
			
		}finally{
			if(session!=null)
				session.close();
			session=null;
			transaction=null;
		}
		
	}//insertFailureLoyalty
	
	
	public void insertLinesMismatch(Long loyaltyId,List<NationalNumberTabDTO> lmsCustIDList,Map<String,List<NationalNumberTabDTO>> crmCustIdList,Set<String> lineRemoveList)
	{
		
		Session session=null;
		Transaction transaction=null;
		String lmsCustIdMsg="";
		String crmCustIdmsg="";
		try{
			
			session=HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			
			
			for(NationalNumberTabDTO tabDTO:lmsCustIDList)
			{
				lmsCustIdMsg=lmsCustIdMsg+tabDTO.getNationalNumber()+"="+tabDTO.getIdType()+",";
			}
			
			if(lmsCustIdMsg.length()>1)
			  lmsCustIdMsg=lmsCustIdMsg.substring(0,lmsCustIdMsg.length()-1);
			
			
			
			for(String lineNumber:crmCustIdList.keySet())
			{
			    String commonCustIdMsg="";
			    
			    for(NationalNumberTabDTO tabDTO:lmsCustIDList)
			    {
			    	if(crmCustIdList.get(lineNumber).contains(tabDTO))
					{
			    		commonCustIdMsg=commonCustIdMsg+tabDTO.getNationalNumber()+"="+tabDTO.getIdType()+",";
						crmCustIdList.get(lineNumber).remove(tabDTO);
					}
			    }
			    
			    
			    if(crmCustIdList.get(lineNumber)!=null&&crmCustIdList.get(lineNumber).size()>0)
				{
					
					for(NationalNumberTabDTO numberTabDTO:crmCustIdList.get(lineNumber))
					{
						crmCustIdmsg=crmCustIdmsg+numberTabDTO.getNationalNumber()+"="+numberTabDTO.getIdType()+",";
					}
					
					if(commonCustIdMsg.length()>1)
						commonCustIdMsg=commonCustIdMsg.substring(0,commonCustIdMsg.length()-1);

					if(crmCustIdmsg.length()>1)
					{
						crmCustIdmsg=crmCustIdmsg.substring(0,crmCustIdmsg.length()-1);
						
						String sql="insert into LOYALTY_LINE_NEW_CUSTID1 (LOYALTY_ID,LINE_NUMBER,LMS_CUST_ID_LIST,CRM_CUST_ID_LIST,COMMON_CUST_ID_LIST) " +
								" values ("+loyaltyId+",'"+lineNumber+"','"+lmsCustIdMsg+"','"+crmCustIdmsg+"','"+commonCustIdMsg+"')";
						
						session.createSQLQuery(sql).executeUpdate();
						
						if(commonCustIdMsg.trim().length()<1)
     						lineRemoveList.add(lineNumber);
						
						crmCustIdmsg="";
					}
					
				}
				 
			}
			
			
			transaction.commit();
			
			
		}catch (Exception e) {
			logger.info("",e);
			
			if(transaction!=null)
				transaction.rollback();
			
			
		}finally{
			if(session!=null)
				session.close();
			session=null;
			transaction=null;
		}
		
	}//insertFailureLoyalty

}
