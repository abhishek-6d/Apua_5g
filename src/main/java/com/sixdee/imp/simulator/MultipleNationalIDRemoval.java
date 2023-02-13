/**
 * 
 */
package com.sixdee.imp.simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dao.TableDetailsDAO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.LoyaltyRegisteredNumberTabDTO;
import com.sixdee.imp.dto.LoyaltyTransactionDetailsDTO;
import com.sixdee.imp.service.serviceDTO.req.LoyaltyTransactionsDTO;
import com.sixdee.imp.simulator.dto.AlterationAccountNumber;
import com.sixdee.imp.simulator.dto.AlterationSubscriberNumber;
import com.sixdee.imp.simulator.dto.DummyLoyaltyTransSummarizedInfo;
import com.sixdee.imp.simulator.dto.DuplicateNationalIdInfo;
import com.sixdee.imp.simulator.dto.LoyaltyTransSummarizedInfo;
import com.sixdee.imp.simulator.dto.TierConversionInfo;

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
public class MultipleNationalIDRemoval extends Thread {

	private static final Logger logger = Logger.getLogger(MultipleNationalIDRemoval.class);
	public void run() {
		/*
		 * Steps :
		 * Get All Numbers with national id as 0
		 * 1. Update Points in loyalty profile for trimmed id
		 * 2. Update old loyalty id in Subscriber Number and Account Number table to new loyalty id
		 * 3. Update 10 Loyalty transaction table with new loyalty id
		 * 4. Update Loyalty Transaction Summary table with new loyalty id
		 * 5. Revert to proposed National Id
		 * 5. Update self status as updated
		 */

		//boolean flag = true;
		logger.info("Started Activity For Multiple National ids");
		try {
			String loyaltyTransaction = "LOYALTY_TRANSACTION_ENTITY_";
			ArrayList<DuplicateNationalIdInfo> duplicateInfoList = getDuplicateNationalIds(100);
			for(DuplicateNationalIdInfo dup : duplicateInfoList){
				
				String actNId = dup.getActualNationalId();
				String nId = dup.getProposedNationalId();
				long proposedLoyaltyId = dup.getProposedLoyaltyId();
				long actLoyaltyId = dup.getActualLoyaltyId();
				logger.info("Erradicating Preciding 0s from LMS "+actNId+" with "+nId);
				Session session = null;
				Transaction transaction = null;
				TableDetailsDAO tableDetailsDAO = null;
				try{
					long t1 =System.currentTimeMillis();
					session  = HiberanteUtil.getSessionFactory().openSession();
					transaction = session.beginTransaction();
					tableDetailsDAO = new TableDetailsDAO();
					LoyaltyProfileTabDTO 	loyaltyProfileTabDTO = getLoyaltyInfo(session,transaction,(proposedLoyaltyId));
					logger.info("Step1 : Loyalty Point updation "+actLoyaltyId+" ---> "+proposedLoyaltyId);
					int count = updatePointsForOriginalNationalId(session,transaction,loyaltyProfileTabDTO,dup);
					logger.info("Step1 : Loyalty Point updation "+actLoyaltyId+" ---> "+proposedLoyaltyId);
					
					long t2 =System.currentTimeMillis();
					 count = updateSubscriberInfo(session,transaction,actLoyaltyId,dup.getProposedLoyaltyId());
					 t1 = System.currentTimeMillis();
					 logger.info("Step2 : Subscriber numbers migrated to "+actLoyaltyId+" ---> "+proposedLoyaltyId+" Count is "+count+" Time : "+(t1-t2));
					 count = updateAccountInfo(session,transaction,actLoyaltyId,dup.getProposedLoyaltyId());
					 t2 = System.currentTimeMillis();
					 t1 = t2;
					 logger.info("Step3 : Account numbers migrated to "+actLoyaltyId+" ---> "+proposedLoyaltyId+" Count is "+count+" Time : "+(t2-t1));
					 count = updateLoyaltyRegisteredInfo(session,transaction,actLoyaltyId,dup.getProposedLoyaltyId());
					 t2 = System.currentTimeMillis();
					 logger.info("Step3b : Loyaly Registered numbers migrated to "+actLoyaltyId+" ---> "+proposedLoyaltyId+" Count is "+count+" Time : "+(t2-t1));
					
					 ArrayList<AlterationSubscriberNumber> alterationSubscriberInfo = getSubscriberInfo(session, transaction, actLoyaltyId);
					 HashSet<Long> counterSet = new HashSet<Long>();
			
					 for(AlterationSubscriberNumber alterationSubscriberNumber : alterationSubscriberInfo){
						 counterSet.add(alterationSubscriberNumber.getCounter());
					 }
					 ArrayList<AlterationAccountNumber> alterationAccountInfo = getAccountInfo(session, transaction, actLoyaltyId);
					 for(AlterationAccountNumber alterationSubscriberNumber : alterationAccountInfo){
						 counterSet.add(alterationSubscriberNumber.getCounter());
					 }

					 logger.info("Loyalty transaction Index to Update for Loyalty ID "+actLoyaltyId+" ---> "+proposedLoyaltyId+" are "+counterSet);
					 for(long i : counterSet){
						 updateLoyaltyTransaction(session,transaction,loyaltyTransaction+i,actLoyaltyId,proposedLoyaltyId);
					 }
					 t1 = System.currentTimeMillis();
					 logger.info("Step4 Finished For Completing Transaction Update "+(t1-t2));
/*					 LoyaltyTransSummarizedInfo LoyaltyTransSummarized = new LoyaltyTransSummarizedInfo(); */
					 
					 ArrayList<LoyaltyTransSummarizedInfo> summarizedInfoList = getAllDateBasedTransSummary(session,transaction,actLoyaltyId,proposedLoyaltyId);
					  DummyLoyaltyTransSummarizedInfo dummyLoyaltyTransSummarizedInfo = null;
					  if(summarizedInfoList!=null){
						for(LoyaltyTransSummarizedInfo lts : summarizedInfoList){
							dummyLoyaltyTransSummarizedInfo = new DummyLoyaltyTransSummarizedInfo();
							dummyLoyaltyTransSummarizedInfo.setLoyaltyId(lts.getLoyaltyId());
							dummyLoyaltyTransSummarizedInfo.setPoints(lts.getPoints());
							dummyLoyaltyTransSummarizedInfo.setStatusPoints(lts.getStatusPoints());
							dummyLoyaltyTransSummarizedInfo.setCreateTime(lts.getCreateTime());
							//logger.info(lts.getLoyaltyId()+"_"+lts.getCreateTime());
							session.save(dummyLoyaltyTransSummarizedInfo);
						}
					  }else{
						  logger.warn("Summarized List is null "+actLoyaltyId+" Prop "+proposedLoyaltyId);
					  }
						t2 = System.currentTimeMillis();
						logger.info("Step5 : Added Summary info tp duplicate Table  Time Taken "+(t2-t1));
						
					//count = revertToOriginalNationalId(session,transaction,actNId,nId);
					count = 1;
					t1= System.currentTimeMillis();
					logger.info("Step6 : Saving "+actNId+" New NationalId "+proposedLoyaltyId+" Time Take "+(t1-t2));
					
					if(count == 0){
						logger.info("Updation Failed "+actNId+" Time Take "+(t2-t1));
					}else{
						updateStatusAsCompleted(session,transaction,dup);
					}
					logger.info("Updating transaction ");;
					transaction.commit();
				}catch(Exception e){
					logger.error("Exception occured ",e);
					if(transaction != null){
						transaction.rollback();
					}
				}finally{
					if(session != null){
						session.close();
					}
				}
			}
			ArrayList<DuplicateNationalIdInfo> duplicateTransInfoList = getDuplicateNationalIds(500);
			HashSet<Long> counterSet = new HashSet<Long>();
			counterSet.add(0l);
			counterSet.add(1l);
			counterSet.add(2l);
			counterSet.add(3l);
			counterSet.add(4l);
			counterSet.add(5l);
			counterSet.add(6l);
			counterSet.add(7l);
			counterSet.add(8l);
			counterSet.add(9l);
			
			
			for(DuplicateNationalIdInfo dup : duplicateTransInfoList){
				Session session = null;
				Transaction transaction = null;
				try{
				 session = HiberanteUtil.getSessionFactory().openSession();
				 transaction = session.beginTransaction();
				
				long actLoyaltyId = dup.getActualLoyaltyId();
				long proposedLoyaltyId = dup.getProposedLoyaltyId();
				 logger.info("Loyalty transaction Index to Update for Loyalty ID "+actLoyaltyId+" ---> "+proposedLoyaltyId+" are "+counterSet);
				 for(long i : counterSet){
					 updateLoyaltyTransaction(session,transaction,loyaltyTransaction+i,actLoyaltyId,proposedLoyaltyId);
				 }
				 transaction.commit();
				}catch(Exception e){
					logger.error("Exception occured ",e);
					if(transaction != null){
						transaction.rollback();
					}
				}finally{
					if(session != null){
						session.close();
					}
				}
				
			}
			
			
			
			
		} catch (Exception e) {

			e.printStackTrace();
			logger.error("Exception ",e);
		}
	
	}
	
	private int updateLoyaltyRegisteredInfo(Session session,
			Transaction transaction, long actualLoyaltyId, long proposedLoyaltyId) throws Exception {


		//Session session = null;
	//	Transaction transaction = null;
		Query query = null;
		String sql = null;
		int count = 0;
		//LoyaltyRegisteredNumberTabDTO
		try{
			sql = "Update LOYALTY_REG_NUMBER_ENTITY_0 sb  set sb.loyaltyID =:proposedLoyaltyId where sb.loyaltyID = :actualLoyaltyId";
			logger.info(sql+" :actualLoyaltyId="+actualLoyaltyId+" :proposedLoyaltyId="+proposedLoyaltyId);
			query = session.createQuery(sql);
			query.setParameter("proposedLoyaltyId", proposedLoyaltyId);
			query.setParameter("actualLoyaltyId", actualLoyaltyId);
			count = query.executeUpdate();
	//		transaction.commit();
		}catch(Exception e){
			logger.error("Exception occured ",e);
		throw e;
		}
		return count;
	
	
	}

	private LoyaltyProfileTabDTO getLoyaltyInfo(Session session,
			Transaction transaction, long l) throws Exception {
		Criteria criteria = null;
		LoyaltyProfileTabDTO loyal = null;
		try{
			criteria = session.createCriteria("LOYALTY_PROFILE_ENTITY_0");
			criteria.add(Restrictions.eq("loyaltyID", l));
			loyal = (LoyaltyProfileTabDTO) criteria.list().get(0);
		}catch(Exception e){
			throw e;
		}
		return loyal;
	}

	private ArrayList<LoyaltyTransSummarizedInfo> getAllDateBasedTransSummary(
			Session session, Transaction transaction, long actLoyaltyId,
			long proposedLoyaltyId) {
		Criteria criteria = null;
		ArrayList<Long> al = new ArrayList<Long>();
		ArrayList<LoyaltyTransSummarizedInfo> transSummary = null;
		al.add(actLoyaltyId);
		al.add(proposedLoyaltyId);
		try{
			criteria = session.createCriteria(LoyaltyTransSummarizedInfo.class);
			criteria.add(Restrictions.in("loyaltyId", al));
			transSummary = (ArrayList<LoyaltyTransSummarizedInfo>) criteria.list();
		}catch(Exception w){
			logger.error("Exception "+actLoyaltyId+" prop "+proposedLoyaltyId+" ",w);
		}
		return transSummary;
	}

	private int updateLoyaltyTransaction(Session session, Transaction transaction, String entity, long actLoyaltyId,
			long proposedLoyaltyId) throws Exception {
		int count = 0;
		Query query = null;
		String sql = null;
		//LoyaltyTransactionDetailsDTO
		try{
			sql = "Update "+entity+" lt set lt.loyaltyID = :nl where lt.loyaltyID = :ol";
			query = session.createQuery(sql);
			query.setParameter("nl", proposedLoyaltyId);
			query.setParameter("ol", actLoyaltyId);
			count = query.executeUpdate();
		}catch(Exception e){
			throw e;
		}
		return count;
		
	}

	private int updateAccountInfo(Session session ,Transaction transaction ,long actualLoyaltyId,
			long proposedLoyaltyId) throws Exception {

		//Session session = null;
	//	Transaction transaction = null;
		Query query = null;
		String sql = null;
		int count = 0;
		
		try{
			sql = "Update ACCOUNT_NUMBER_ENTITY_0 sb  set sb.loyaltyID =:proposedLoyaltyId where sb.loyaltyID = :actualLoyaltyId";
			logger.info(sql+" :actualLoyaltyId="+actualLoyaltyId+" :proposedLoyaltyId="+proposedLoyaltyId);
			query = session.createQuery(sql);
			query.setParameter("proposedLoyaltyId", proposedLoyaltyId);
			query.setParameter("actualLoyaltyId", actualLoyaltyId);
			count = query.executeUpdate();
	//		transaction.commit();
		}catch(Exception e){
			logger.error("Exception occured ",e);
		throw e;
		}
		return count;
	
	}


	
	private int updateSubscriberInfo(Session session ,Transaction transaction ,long actualLoyaltyId,
			long proposedLoyaltyId) throws Exception {

		//Session session = null;
	//	Transaction transaction = null;
		Query query = null;
		String sql = null;
		int count = 0;
		
		try{
			sql = "Update SUBSCRIBER_NUMNER_ENTITY_0 sb  set sb.loyaltyID =:proposedLoyaltyId where sb.loyaltyID = :actualLoyaltyId";
			logger.info(sql+" :actualLoyaltyId="+actualLoyaltyId+" :proposedLoyaltyId="+proposedLoyaltyId);
			query = session.createQuery(sql);
			query.setParameter("proposedLoyaltyId", proposedLoyaltyId);
			query.setParameter("actualLoyaltyId", actualLoyaltyId);
			count = query.executeUpdate();
	//		transaction.commit();
		}catch(Exception e){
			logger.error("Exception occured ",e);
		throw e;
		}
		return count;
	
	}


	private int updatePointsForOriginalNationalId(Session session,
			Transaction transaction,LoyaltyProfileTabDTO loyaltyProfileTabDTO, DuplicateNationalIdInfo dup) throws Exception {

		//Session session = null;
	//	Transaction transaction = null;
		
		Query query = null;
		String sql = null;
		int count = 0;
		int newTier = 1;
		double additionalStatusPoints = 0;
		int type = 1;
		TierConversionInfo tierConversionInfo = null;
		try{
			loyaltyProfileTabDTO.setRewardPoints(dup.getPoints());
			additionalStatusPoints = loyaltyProfileTabDTO.getStatusPoints()+dup.getStatusPoints();
			loyaltyProfileTabDTO.setStatusPoints(additionalStatusPoints);
			
			type = loyaltyProfileTabDTO.getStatusID();
			logger.info("Type "+type+" Points "+additionalStatusPoints);
			if(type==1 && additionalStatusPoints>=3001){
				//log this status has to be converted
				tierConversionInfo= new TierConversionInfo();
				tierConversionInfo.setLoyaltyId(loyaltyProfileTabDTO.getLoyaltyID());
				tierConversionInfo.setTier(loyaltyProfileTabDTO.getTierId());
				tierConversionInfo.setNewTier((newTier=additionalStatusPoints>=12001?3:2));
				tierConversionInfo.setStatusPoints(loyaltyProfileTabDTO.getStatusPoints());
				tierConversionInfo.setNewStatusPoints(additionalStatusPoints);
				session.save(tierConversionInfo);
				logger.info("Inserting to Type Conversion "+loyaltyProfileTabDTO.getLoyaltyID());
				loyaltyProfileTabDTO.setTierId(newTier);
			}else if(type==2 && additionalStatusPoints >= 12001){

				//log this status has to be converted
				tierConversionInfo= new TierConversionInfo();
				tierConversionInfo.setLoyaltyId(loyaltyProfileTabDTO.getLoyaltyID());
				tierConversionInfo.setTier(loyaltyProfileTabDTO.getTierId());
				tierConversionInfo.setNewTier(3);
				tierConversionInfo.setStatusPoints(loyaltyProfileTabDTO.getStatusPoints());
				tierConversionInfo.setNewStatusPoints(additionalStatusPoints);
				session.save(tierConversionInfo);
				logger.info("Inserting to Type Conversion "+loyaltyProfileTabDTO.getLoyaltyID());
				loyaltyProfileTabDTO.setTierId(newTier);			
			}
			session.update(loyaltyProfileTabDTO);
		}catch(Exception e){
			logger.error("Exception occured ",e);
		throw e;
		}finally{
			
		}
		return count;
	
	}
	
	private ArrayList<AlterationSubscriberNumber> getSubscriberInfo(Session session , Transaction transaction ,long loyaltyId){
		Criteria criteria = null;
		ArrayList<AlterationSubscriberNumber> subscriberNumber = null;
		try{
			criteria = session.createCriteria(AlterationSubscriberNumber.class);
			criteria.add(Restrictions.eq("loyaltyID", loyaltyId));
			subscriberNumber = (ArrayList<AlterationSubscriberNumber>) criteria.list();
		}catch(Exception e){
			logger.error("Exception occured ",e);
		}
		return subscriberNumber;
	}
	private ArrayList<AlterationAccountNumber> getAccountInfo(Session session , Transaction transaction ,long loyaltyId){
		Criteria criteria = null;
		ArrayList<AlterationAccountNumber> subscriberNumber = null;
		try{
			criteria = session.createCriteria(AlterationAccountNumber.class);
			criteria.add(Restrictions.eq("loyaltyID", loyaltyId));
			subscriberNumber = (ArrayList<AlterationAccountNumber>) criteria.list();
		}catch(Exception e){
			logger.error("Exception occured ",e);
		}
		return subscriberNumber;
	}


	private ArrayList<DuplicateNationalIdInfo> getDuplicateNationalIds(int status) throws Exception {



		ArrayList<DuplicateNationalIdInfo> nationalNumberList = null;
		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		try{
		//	logger.info("Loyalty Id "+loyaltyId);
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			criteria = session.createCriteria(DuplicateNationalIdInfo.class);
			criteria.add(Restrictions.eq("status", status));
			criteria.add(Restrictions.gt("actualLines", 0));
		//	criteria.add(Restrictions.like("loyaltyID", loyaltyId));
			nationalNumberList = (ArrayList<DuplicateNationalIdInfo>) criteria.list();
			transaction.commit();
	
//			criteria.set
		}catch(Exception e){
			logger.error("Exception occured ",e);
			if(transaction!=null){
				transaction.rollback();
			}
			throw e;
		}
		finally{
			if(session != null){
				session.close();
			}
		}
		return nationalNumberList;
	
	
	
	}
	
	private void updateStatusAsCompleted(Session session, Transaction transaction, DuplicateNationalIdInfo dup) {

		String sql = null;
		int count = 0;
		try{
			dup.setStatus(200);
			session.update(dup);
	//		transaction.commit();
		}catch(Exception e){
			logger.error("Exception occured ",e);
			if(transaction != null){
				transaction.rollback();
			}
		}
	
	}
	private int revertToOriginalNationalId(Session session, Transaction transaction, String actNId, String nId) throws Exception {
		//Session session = null;
	//	Transaction transaction = null;
		Query query = null;
		String sql = null;
		int count = 0;
		try{
			sql = "Update NATIONAL_NUMBER_ENTITY_0 set NATIONAL_ID=:nId where NATIONAL_ID = :actNId";
			logger.info(sql+" :nId="+nId+" :actNId="+actNId);
			//transaction = session.beginTransaction();
			query = session.createQuery(sql);
			query.setParameter("nId", nId);
			query.setParameter("actNId", actNId);
			count = query.executeUpdate();
	//		transaction.commit();
		}catch(Exception e){
			logger.error("Exception occured ",e);
		throw e;
		}
		return count;
	}
	
}
