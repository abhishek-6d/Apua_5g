/**
 * 
 */
package com.sixdee.imp.dao;

import java.util.ArrayList;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.ResultTransformer;

import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.InstantPackDTO;
import com.sixdee.imp.dto.InstantRewardConfigDTO;

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
 * <td>Feb 12, 2014</td>
 * <td>Rahul K K</td>
 * <td>Created this class</td>
 * </tr>
 * </table>
 * </p>
 */
public class GiftSelectionDAO {

	
	private static final Logger logger = Logger.getLogger(GiftSelectionDAO.class);
	
	public InstantPackDTO selectGift(String txnId,String msisdn){
		InstantPackDTO gift = null;
		Session session = null;
		Transaction transaction = null;
		int giftCaptured = 0;
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(InstantPackDTO.class);
			criteria.add(Restrictions.gt("packReleaseCount", 0l)).addOrder(Order.desc("packReleaseCount"));
			criteria.setMaxResults(1);
			ArrayList<InstantPackDTO> giftList =  (ArrayList<InstantPackDTO>) criteria.list();
			for(InstantPackDTO instDTO : giftList){
				giftCaptured = updateGiftPack(txnId,msisdn,session,transaction,instDTO);
				if(giftCaptured != 0){
					gift = instDTO;
				}
			}
		
		}catch(Exception e){
			logger.error(" GiftSelectionDAO : Transaction ID : "+txnId+" Msisdn : "+msisdn+" Message : Exception occured ",e );
		}
		
		finally{
			if(session != null){
				session.close();
			}
			//if(gift==null)
		}
	
		return gift;
	}

	
	
	public InstantPackDTO selectGift(String txnId,String msisdn,ArrayList<Integer> statusList){
		InstantPackDTO gift = null;
		Session session = null;
		Transaction transaction = null;
		int giftCaptured = 0;
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(InstantPackDTO.class);
			criteria.add(Restrictions.gt("packReleaseCount", 0l)).add(Restrictions.not(Restrictions.in("packId",statusList))).addOrder(Order.desc("packReleaseCount"));
			criteria.setMaxResults(1);
			ArrayList<InstantPackDTO> giftList =  (ArrayList<InstantPackDTO>) criteria.list();
			for(InstantPackDTO instDTO : giftList){
				giftCaptured = updateGiftPack(txnId,msisdn,session,transaction,instDTO);
				if(giftCaptured != 0){
					gift = instDTO;
				}
			}
			//transaction.commit();
		}catch(Exception e){
			logger.error(" GiftSelectionDAO : Transaction ID : "+txnId+" Msisdn : "+msisdn+" Message : Exception occured ",e );
			}
		finally{
			if(session != null){
				session.close();
			}
			//if(gift==null)
		}
	
		return gift;
	}

	
	
	private int updateGiftPack(String txnId, String msisdn, Session session, Transaction transaction,
			InstantPackDTO instDTO)  {
		//boolean giftCaptured = false;
		Query sqlQuery = null;
		int giftCaptured = 0;
		try{
			String sql = "Update InstantPackDTO gift set gift.packReleaseCount=:releaseCount where gift.packReleaseCount=:origReleaseCount and gift.packId=:packId";
			sqlQuery = session.createQuery(sql);
			sqlQuery.setParameter("releaseCount", instDTO.getPackReleaseCount()-1);
			sqlQuery.setParameter("origReleaseCount", instDTO.getPackReleaseCount());
			sqlQuery.setParameter("packId", instDTO.getPackId());
			giftCaptured = sqlQuery.executeUpdate();
			logger.debug(giftCaptured+" "+instDTO.getPackId());
			transaction.commit();
		}catch(HibernateException e){
			//throw e;
			logger.error(" GiftSelectionDAO : Transaction ID : "+txnId+" Msisdn : "+msisdn+" Message : Exception occured ",e);
		}
		finally{
			transaction = null;
			
		}
		return giftCaptured;
	}
	
	
	
	
	public ArrayList<Long> findAvailableGifts(String txnId,String msisdn,String tableName,long subscriberNumber){
		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		ArrayList<Long> statusList = null;
		Calendar cal = null;
		try{
			cal = Calendar.getInstance();
			
			cal.add(Calendar.DATE,
					-(Integer.parseInt(Cache.configParameterMap.get(
							"INSTANT_REWARD_VALIDITY").getParameterValue())));
		
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			criteria = session.createCriteria(tableName);
			criteria.add(Restrictions.eq("subscriberNumber", subscriberNumber)).add(Restrictions.gt("createDate", cal.getTime())).addOrder(Order.desc("createDate")).setMaxResults(Cache.instantOfferCount);
			//criteria.setProjection(Projections.distinct(Projections.projectionList().add(Projections.property("offerId"),"offerId")));
			criteria.setProjection(Projections.property("offerId"));
			//criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
			
			//criteria.list();
			statusList = (ArrayList<Long>) criteria.list();
			logger.debug(statusList);
		}finally{
			if(session != null){
				session.close();
			}
		}
		return statusList;
				
	}



	public boolean isBucketEmpty() {
		Session session = null;
		Transaction transaction = null;
		SQLQuery sqlQuery = null;
		long count = 0;
		boolean isEmpty = false;
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			//Criteria criteria = session.createCriteria(InstantReward);
			sqlQuery = session.createSQLQuery("select count(1) from INSTANT_REWARD_BUCKET where PACKAGE_RELEASE_COUNT > 0");
			count = ((java.math.BigDecimal) sqlQuery.uniqueResult()).longValue();
			transaction.commit();
		}catch(HibernateException e){
			//throw e;
			logger.error(" GiftSelectionDAO : Message : Exception occured ",e);
		}
		finally{
			logger.debug("Count in DB"+count);
			if(count==0){
				isEmpty = true;
			}
		}
		return isEmpty;
	}

	public boolean fillBucket() {
		boolean isFilled = false;
		ArrayList<InstantRewardConfigDTO> instantRewardConfigDTOs = null;
		InstantPackDTO instantPackDTO = null;
		try {
			int i = repopulateInstantRewards();
			instantRewardConfigDTOs = checkFlushReqd();
			if (instantRewardConfigDTOs != null) {
				updateInstantRewardCount(instantRewardConfigDTOs);
			}
			isFilled = true;
			// instantPackDTO = selectPacktoGift();
		} finally {
		}
		return isFilled;
	}
	
	public ArrayList<InstantRewardConfigDTO> checkFlushReqd() {

		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		ArrayList<InstantRewardConfigDTO> instantRewardConfigList = null;
		ArrayList<Integer> typeList = new ArrayList<Integer>();
		try {
			typeList.add(1);
			typeList.add(2);
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			criteria = session.createCriteria(InstantRewardConfigDTO.class);
			criteria.add(Restrictions.in("type", typeList));
			instantRewardConfigList = (ArrayList<InstantRewardConfigDTO>) criteria
					.list();
			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
				transaction = null;
			}
		} catch (ClassCastException e) {
			logger.info("No data found for flushing");
		} finally {
			if (session != null) {
				session.close();
				session = null;
			}
			criteria = null;
		}
		return instantRewardConfigList;

	}

	
	
	private int repopulateInstantRewards() {
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		String sql = null;
		int updateCount = 0;
		try {
			sql = "update INSTANT_REWARD_BUCKET set PACKAGE_RELEASE_COUNT=PACKAGE_COUNT,TOTAL_PACKAGE_COUNT=TOTAL_PACKAGE_COUNT+(select sum(PACKAGE_COUNT) from INSTANT_REWARD_BUCKET) , RELOAD_COUNT = RELOAD_COUNT+1";
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			query = session.createSQLQuery(sql);
			updateCount = query.executeUpdate();
			transaction.commit();
			logger.info("Update Count " + updateCount);
		} catch (HibernateException e) {
			logger.error("Exception occured ", e);
			if (transaction != null) {
				transaction.rollback();
				transaction = null;
			}
		} catch (Exception e) {
			logger.error("Exception occured ", e);
		} finally {
			if (session != null) {
				session.close();
				session = null;
			}
			query = null;
			sql = null;
			if (updateCount == 0) {
				populateFromConfig();
			}
		}
		return updateCount;
	}
	
	private void updateInstantRewardCount(
			ArrayList<InstantRewardConfigDTO> instantRewardConfigDTOs) {
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		String updateSql = null;
		InstantPackDTO instantPackDTO = null;
		// InstantRewardConfigDTO instantRewardConfigDTO = null;
		try {
			updateSql = "update InstantPackDTO set PACKAGE_COUNT = :packageCount ,PACKAGE_RELEASE_COUNT = :packageCount where packId = :packId";
			// insertSql =
			// "insert into InstantPackDTO(packId,packCount,packReleaseCount,IS_LOCKED) values()";
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();

			for (InstantRewardConfigDTO instantRewardConfigDTO : instantRewardConfigDTOs) {
				if (instantRewardConfigDTO.getType() == 2) {
					query = session.createQuery(updateSql);
					query.setParameter("packageCount",
							instantRewardConfigDTO.getPackCount());
					query.setParameter("packId", instantRewardConfigDTO.getId());
					query.executeUpdate();
				} else {
					instantPackDTO = new InstantPackDTO();
					instantPackDTO.setPackId(instantRewardConfigDTO.getId());
					instantPackDTO.setPackCount(instantRewardConfigDTO
							.getPackCount());
/*					instantPackDTO.setPackReleaseCount(instantRewardConfigDTO
							.getReleasePackCount());*/
					instantPackDTO.setPackReleaseCount(0);
					instantPackDTO.setLock(0);
					session.save(instantPackDTO);
				}
			}
			transaction.commit();

		} catch (HibernateException e) {
			logger.error("Exception occured ", e);
			if (transaction != null) {
				transaction.rollback();
				transaction = null;
			}
		} catch (Exception e) {
			logger.error("Exception occured ", e);

		} finally {
			if (session != null) {
				session.close();
				session = null;
			}
			instantPackDTO = null;
			instantRewardConfigDTOs = null;
			updateSql = null;
		}
	}


	private void populateFromConfig() {

		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		ArrayList<InstantRewardConfigDTO> instantRewardConfigList = null;
		int counter = 0;
		try {
			long startTime = System.currentTimeMillis();
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			criteria = session.createCriteria(InstantRewardConfigDTO.class);
			instantRewardConfigList = (ArrayList<InstantRewardConfigDTO>) criteria
					.list();
			for (InstantRewardConfigDTO instConfigDTO : instantRewardConfigList) {
				InstantPackDTO instantPackDTO = new InstantPackDTO();
				instantPackDTO.setPackId(instConfigDTO.getId());
				instantPackDTO.setPackCount(instConfigDTO.getPackCount());
				instantPackDTO
						.setPackReleaseCount(instConfigDTO.getPackCount());
				instantPackDTO.setLock(0);
				
				session.save(instantPackDTO);
				counter++;
			}
			transaction.commit();
			long endTime = System.currentTimeMillis();
			logger.info("Time taken for Repopulating [" + (endTime - startTime)
					+ "] Records [" + counter + "]");
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
				transaction = null;
			}
		} catch (ClassCastException e) {
			logger.info("No data found for flushing");
		} finally {
			if (session != null) {
				session.close();
				session = null;
			}
			criteria = null;
		}
		// return instantRewardConfigList;

	}


}