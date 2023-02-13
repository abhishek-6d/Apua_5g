package com.sixdee.imp.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.InstantPackDTO;
import com.sixdee.imp.dto.InstantReserveDTO;
import com.sixdee.imp.dto.InstantRewardConfigDTO;
import com.sixdee.imp.dto.InstantRewardsDTO;

/**
 * 
 * @author Rahul K K
 * @version 1.0
 * 
 *          <p>
 *          <b><u>Development History</u></b><br>
 *          <table border="1" width="100%">
 *          <tr>
 *          <td width="15%"><b>Date</b></td>
 *          <td width="20%"><b>Author</b></td>
 *          </tr>
 *          <tr>
 *          <td>May 30,2013 08:26:31 PM</td>
 *          <td>Rahul K K</td>
 *          </tr>
 *          </table>
 *          </p>
 */

public class InstantRewardsDAO {/*

	private Logger logger = Logger.getLogger(InstantRewardsDAO.class);
	private int retryCount = 0;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	
	
	
	
	@SuppressWarnings("unchecked")
	public InstantPackDTO selectPacktoGift() {
		Session session = null;
		Transaction transaction = null;
		Query sqlQuery = null;
		Criteria criteria = null;
		List<InstantPackDTO> instantPackDTOList = null;
		String sql = null;
		List<Object[]> results = null;
		InstantPackDTO instantRewardsDTO = null;
		boolean isBlocked = false;
		try {

			// sql =
			// " select PACKAGE_ID,IS_LOCKED from INSTANT_REWARDS where PACKAGE_RELEASED=(select max(PACKAGE_RELEASE_COUNT) from INSTANT_REWARDS) and PACKAGE_RELEASE_COUNT >0 limit 1 ";
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			criteria = session.createCriteria(InstantPackDTO.class);
			criteria.add(Restrictions.gt("packReleaseCount", 0l));
			criteria.addOrder(Order.desc("packReleaseCount"));
			criteria.setMaxResults(1);
			instantPackDTOList = criteria.list();
			for (InstantPackDTO inst : instantPackDTOList) {
				if (inst != null && inst.getPackId()!=0) {
					logger.info("instant Reward "+inst.getPackId());
					instantRewardsDTO = new InstantPackDTO();
					instantRewardsDTO.setPackId(inst.getPackId());
					instantRewardsDTO.setPackReleaseCount(inst
							.getPackReleaseCount());
					instantRewardsDTO.setLock(inst.getLock());
					instantRewardsDTO.setPackCount(inst.getPackCount());
				}
			}
			
			 * sqlQuery = session.createSQLQuery(sql); results =
			 * sqlQuery.list(); for(Object[] instants : results){
			 * instantRewardsDTO = new InstantPackDTO();
			 * instantRewardsDTO.setPackId((Integer)instants[0]);
			 * instantRewardsDTO.setLock((Integer)instants[2]); }
			 
			
			transaction.commit();
		} catch (Exception e) {
			logger.error("Exception occured ", e);
			if (transaction != null)
				transaction.rollback();
		} finally {
			if (session != null) {
				session.close();
				session = null;
			}
			transaction = null;
			sqlQuery = null;
			if (instantRewardsDTO != null)
				isBlocked = blockInstantReward(instantRewardsDTO);
			if (!isBlocked && retryCount < 5) {
				retryCount++;
				instantRewardsDTO = selectPacktoGift();
			}
			retryCount = 0;
		}
		return instantRewardsDTO;
	}

	private boolean blockInstantReward(InstantPackDTO instantRewardsDTO) {

		Session session = null;
		Transaction transaction = null;
		Query sqlQuery = null;
		String sql = null;
		List<String[]> results = null;
		boolean isBlocked = false;
		try {
			sql = "update INSTANT_REWARD_BUCKET set IS_LOCKED = IS_LOCKED + 1 , PACKAGE_RELEASE_COUNT = PACKAGE_RELEASE_COUNT-1  where IS_LOCKED = ? and PACKAGE_ID=? and PACKAGE_RELEASE_COUNT > 0";

			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			sqlQuery = session.createSQLQuery(sql);
			sqlQuery.setParameter(0, instantRewardsDTO.getLock());
			sqlQuery.setParameter(1, instantRewardsDTO.getPackId());
			if (sqlQuery.executeUpdate() != 0)
				isBlocked = true;
			logger.debug("Offer is blocked for Package id "+instantRewardsDTO.getPackId());
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
				transaction = null;
			}
		} finally {
			if (session != null) {
				session.close();
				session = null;
			}
			transaction = null;
			sqlQuery = null;

		}
		return isBlocked;
	}

	public boolean unBlockPackage(InstantPackDTO instantRewardsDTO) {

		Session session = null;
		Transaction transaction = null;
		Query sqlQuery = null;
		String sql = null;
		List<String[]> results = null;
		boolean isBlocked = false;
		try {
			sql = "update INSTANT_REWARDS set PACKAGE_RELEASED = PACKAGE_RELEASED-1 where  PACKAGE_ID=?";

			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			sqlQuery = session.createSQLQuery(sql);
			// sqlQuery.setParameter(0, instantRewardsDTO.getCounter());
			sqlQuery.setParameter(0, instantRewardsDTO.getPackId());
			if (sqlQuery.executeUpdate() != 0)
				isBlocked = true;
			transaction.commit();
		} catch (Exception e) {
			logger.error("Exception occured ", e);
			if (transaction != null) {
				transaction.rollback();
				transaction = null;
			}
		} finally {
			if (session != null) {
				session.close();
				session = null;
			}
			transaction = null;
			sqlQuery = null;

		}
		return isBlocked;

	}

	
	 * public InstantRewards blockInstantRewards(){ Session session = null;
	 * Transaction transaction = null; SQLQuery sqlQuery = null; //List<String>
	 * try{ session = HiberanteUtil.getSessionFactory().openSession();
	 * transaction = session.beginTransaction(); sqlQuery =
	 * session.createSQLQuery
	 * ("SELECT max(PACKAGE_COUNT-PACKAGE_RELEASED) from INSTANT_REWARDS limit 1"
	 * ); //while() }catch (HibernateException e) {
	 * 
	 * } }
	 

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
					instantPackDTO.setPackReleaseCount(instantRewardConfigDTO
							.getReleasePackCount());
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

	private int repopulateInstantRewards() {
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		String sql = null;
		int updateCount = 0;
		try {
			sql = "update INSTANT_REWARD_BUCKET set PACKAGE_RELEASE_COUNT=PACKAGE_COUNT";
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

	public void reserveMyPack(String tableName, InstantRewardsDTO instRewardsDTO,int status) {
		Session session = null;
		Transaction transaction = null;
		InstantReserveDTO instantReserveDTO = null;
		String date = null;
		try {
			instantReserveDTO = new InstantReserveDTO();
			instantReserveDTO.setOfferId(instRewardsDTO.getOfferId());
			instantReserveDTO.setSubscriberNumber(instRewardsDTO
					.getSubscriberNumber());
			if(status != 1){
				instantReserveDTO.setbSubscriberNumber(instRewardsDTO
						.getbSubscriberNumber());
				
			}
			instantReserveDTO.setOfferName(instRewardsDTO.getOfferName().toLowerCase());
			//instantReserveDTO.setOfferSynonym(instRewardsDTO.getOfferSynonym().toLowerCase());
			//instantReserveDTO.setPackId(instantReserveDTO.getPackId());
			instantReserveDTO.setStatus(status);
			date= sdf.format(new Date());
			try {
				instantReserveDTO.setCreateDate(sdf.parse(date));

				instantReserveDTO.setUpdateDate(sdf.parse(date));
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.info("Instant Reserve for "+instRewardsDTO.getSubscriberNumber()+" offerId "+instRewardsDTO.getOfferId()+" Offer ["+instantReserveDTO.getOfferName()+"] "+tableName);
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			session.save(tableName, instantReserveDTO);
			transaction.commit();
			transaction = null;
		} catch (HibernateException he) {
			logger.error("Exception occured ", he);
			if (transaction != null) {
				transaction.rollback();
				transaction = null;
			}
		} finally {
			if (session != null) {
				session.close();
				session = null;
			}
		}
	}

	public InstantRewardsDTO updateRewardToTransfer(String tableName,
			InstantRewardsDTO instRewardsDTO, int status) throws CommonException {
		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		Calendar cal = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		ArrayList<Integer> statusList = null;
		int i = 0;
		try {
			
			cal = Calendar.getInstance();
			
			cal.add(Calendar.DATE,
					-(Integer.parseInt(Cache.configParameterMap.get(
							"INSTANT_REWARD_VALIDITY").getParameterValue())));
			statusList = new ArrayList<Integer>();
			statusList.add(1);
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			criteria = session.createCriteria(tableName);
			
			criteria.add(Restrictions.and(
					Restrictions.eq("subscriberNumber",
							instRewardsDTO.getbSubscriberNumber()),
					Restrictions.eq("offerName", instRewardsDTO.getOfferId().toLowerCase())));
			criteria.add(Restrictions.and(
					Restrictions.in("status", statusList),
					Restrictions.gt("createDate", (cal.getTime()))));
			criteria.addOrder(Order.asc("createDate"));
			criteria.setMaxResults(1);
			logger.info("Subscriber Number ["+instRewardsDTO.getbSubscriberNumber()+"] offerName ["+instRewardsDTO.getOfferId().toLowerCase()+"] CreateDate ["+cal.getTime()+"]");
			for (InstantReserveDTO instantReserveDTO : (List<InstantReserveDTO>) criteria
					.list()) {
					++i;
				
					instantReserveDTO.setbSubscriberNumber(instRewardsDTO
							.getSubscriberNumber());
					instantReserveDTO.setUpdateDate(new Date());
					logger.info("Got record for Id ["+instantReserveDTO.getId()+"] OfferName ["+instantReserveDTO.getOfferName()+"] A Subscriber ["+instantReserveDTO.getSubscriberNumber()+"]");
		//			instantReserveDTO.setUpdateDate(new Date());
					
						instantReserveDTO.setStatus(status);
						
					session.update(tableName,instantReserveDTO);
					instRewardsDTO.setOfferId(instantReserveDTO.getOfferId());
					instRewardsDTO.setOfferName(instantReserveDTO.getOfferName());
					instRewardsDTO.setPackId(instantReserveDTO.getPackId()+"");
					instRewardsDTO.setId(instantReserveDTO.getId());
					break;
			}
			if(i==0){
				throw new CommonException(
						"Your Offer Already Transfered to Another Customer ["
								+ instRewardsDTO.getbSubscriberNumber()
								+ "]");
			}
			transaction.commit();
			transaction = null;
		} catch (HibernateException he) {
			logger.error("Exception occured",he);
			if (transaction != null) {
				transaction.rollback();
				transaction = null;
			}
			throw new CommonException("Critical issue please check");

		}catch (Exception he) {
			logger.error("Exception occured",he);
			if (transaction != null) {
				transaction.rollback();
				transaction = null;
			}
			throw new CommonException("Critical issue please check");
		} finally {
			if (session != null) {
				session.close();
				session = null;
			}
		}
		return instRewardsDTO;
	}

	public boolean takeBackOffer(String tableName,
			InstantRewardsDTO instRewardsDTO, int status) throws CommonException {

		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		Calendar cal = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		ArrayList<Integer> statusList = null;
		int i = 0;
		boolean isReverted = false;
		try {
			
			cal = Calendar.getInstance();
			
			cal.add(Calendar.HOUR,
					-(Integer.parseInt(Cache.configParameterMap.get(
							"TRANSFER_REWARD_VALIDITY").getParameterValue())));
			statusList = new ArrayList<Integer>();
			statusList.add(3);
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			criteria = session.createCriteria(tableName);
			criteria.add(Restrictions.and(Restrictions.eq("subscriberNumber", instRewardsDTO.getSubscriberNumber()),Restrictions.and(
					Restrictions.eq("bSubscriberNumber",
							instRewardsDTO.getbSubscriberNumber()),
					Restrictions.eq("offerName", instRewardsDTO.getOfferId().toLowerCase()))));
			criteria.add(Restrictions.and(
					Restrictions.in("status", statusList),
					Restrictions.gt("createDate", (cal.getTime()))));
			criteria.addOrder(Order.asc("createDate"));
			criteria.setMaxResults(1);
			logger.info("Subscriber Number ["+instRewardsDTO.getbSubscriberNumber()+"] offerName ["+instRewardsDTO.getOfferId().toLowerCase()+"] CreateDate ["+cal.getTime()+"]");
			for (InstantReserveDTO instantReserveDTO : (List<InstantReserveDTO>) criteria
					.list()) {
				++i;

				logger.info("Got record for Id [" + instantReserveDTO.getId()
						+ "] OfferName [" + instantReserveDTO.getOfferName()
						+ "] A Subscriber ["
						+ instantReserveDTO.getSubscriberNumber() + "]");
				instantReserveDTO.setUpdateDate(new Date());

				instantReserveDTO.setStatus(status);

				session.update(tableName, instantReserveDTO);
				isReverted = true;
				break;

			}
			if(i==0){
				throw new CommonException(
						"No Offer "+instRewardsDTO.getOfferId().toLowerCase()+" To Revert");
			}
			transaction.commit();
			transaction = null;
		} catch (HibernateException he) {
			logger.error("Exception occured",he);
			if (transaction != null) {
				transaction.rollback();
				transaction = null;
			}
			throw new CommonException("Critical issue please check");

		}catch (Exception he) {
			logger.error("Exception occured",he);
			if (transaction != null) {
				transaction.rollback();
				transaction = null;
			}
			throw new CommonException("Critical issue please check");
		} finally {
			if (session != null) {
				session.close();
				session = null;
			}
		}
		return isReverted;
	
	}

	public boolean revertReward(String tableName,
			InstantRewardsDTO instRewardsDTO, int i) throws CommonException {
		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		String donor = null;
		String reciever = null;
		String offer = null;
		String transferValidity = null;
		ArrayList<Integer> statusList = new ArrayList<Integer>();
		boolean isReverted = false;
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			donor = instRewardsDTO.getSubscriberNumber();
			reciever = instRewardsDTO.getbSubscriberNumber();
			offer = instRewardsDTO.getOfferId();
			transferValidity = Cache.configParameterMap.get("INSTANT_REWARD_VALIDITY")!=null?(Cache.configParameterMap.get("INSTANT_REWARD_VALIDITY").getParameterValue()):"14";
			statusList.add(2);
			InstantReserveDTO instantReserveDTO = getMatchingReward(tableName,session,transaction,reciever,donor,offer,Integer.parseInt(transferValidity),statusList);
			if(instantReserveDTO != null){
				instantReserveDTO.setStatus(6);
//				instantReserveDTO.setbSubscriberNumber("");
				instantReserveDTO.setUpdateDate(new Date());

				session.update(instantReserveDTO);
				transaction.commit();
				isReverted = true;
			}else{
				logger.info("No Offer is avaialble for this person ");
				throw new CommonException("Offer Not Available");
			}
			
		}catch(Exception e){
			logger.error("Exception occured ",e);
		//	isReverted = "002";
			if(transaction!=null){
				transaction.rollback();
			}
			throw new CommonException(e.getMessage());
		}
		
		finally{
			if(session != null){
				session.close();
			}
		}
		return isReverted;
	}


	private InstantReserveDTO getMatchingReward(String tableName,Session session,
			Transaction transaction, String donor, String reciever,
			String offer, int validity,ArrayList status) {
		Criteria criteria = null;
		Calendar cal = null;
		InstantReserveDTO instantReserveDTO = null;
		try{
			cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -validity);
			criteria = session.createCriteria(tableName);
			criteria.add(Restrictions.eq("subscriberNumber", donor)).add(Restrictions.eq("bSubscriberNumber", reciever)).add(Restrictions.eq("offerName", offer)).add(Restrictions.in("status", status)).add(Restrictions.gt("updateDate", cal.getTime())).addOrder(Order.asc("updateDate"));
			criteria.setMaxResults(1);
			logger.debug("subscriberNumber : "+donor+" bSubscriberNumber : "+reciever+" offerName : "+offer+" status : "+status+" createDate : "+cal.getTime());
			ArrayList<InstantReserveDTO> instantReserveList = (ArrayList<InstantReserveDTO>) criteria.list();
			for(InstantReserveDTO instantDTO : instantReserveList){
				instantReserveDTO = instantDTO;
			}
			
		}catch(Exception e){
			logger.error("Exception occured ",e);
		}
		
		finally{
			
		}
		return instantReserveDTO;
	}

	
	
*/}
