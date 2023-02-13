package com.sixdee.imp.dao;

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
 * <td>July 10,2013 04:00:18 PM</td>
 * <td>Rahul K K</td>
 * </tr>
 * </table>
 * </p>
 */



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.InstantReserveDTO;
import com.sixdee.imp.dto.InstantRewardConfigDTO;
import com.sixdee.imp.dto.RedeemIRDTO;

public class RedeemIRDAO {int i = 0; 

	Logger logger = Logger.getLogger(RedeemIRDAO.class);
	public InstantReserveDTO isRewardAvailable(String tableName,
			RedeemIRDTO redeemIRDTO) throws CommonException {

		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		Calendar cal = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		ArrayList<Integer> statusList = null;
		String instantReward = null;
		InstantReserveDTO instantReserveDTOAwarded = null;
		boolean isConcurrentUpdateSusccess = false;
		int i = 0;
		try {

			cal = Calendar.getInstance();
			cal.add(Calendar.DATE,
					-(Integer.parseInt(Cache.configParameterMap.get(
							"INSTANT_REWARD_VALIDITY").getParameterValue())));
			statusList = new ArrayList<Integer>();
			statusList.add(1);
			statusList.add(5);
			statusList.add(3);
			
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			criteria = session.createCriteria(tableName);
			criteria.add(Restrictions.and(
					Restrictions.eq("subscriberNumber",
							Long.parseLong(redeemIRDTO.getSubscriberNumber())),
					Restrictions.eq("offerName", redeemIRDTO.getOfferId().toLowerCase())));
			criteria.add(Restrictions.and(
					Restrictions.in("status", statusList),
					Restrictions.gt("createDate", (cal.getTime()))));
			criteria.setMaxResults(1);
			logger.debug(redeemIRDTO.getSubscriberNumber()+" "+redeemIRDTO.getOfferId());
			for (InstantReserveDTO instantReserveDTO : (List<InstantReserveDTO>) criteria
					.list()) {
				if (instantReserveDTO != null) {
					isConcurrentUpdateSusccess = updateConcurrency(tableName,instantReserveDTO,9);
					instantReserveDTOAwarded = instantReserveDTO;
					break;
				}
			}
			transaction.commit();
			transaction = null;
		} catch (HibernateException he) {
			if (transaction != null) {
				transaction.rollback();
				transaction = null;
			}
		} catch (CommonException e) {
			throw e;
		} finally {
			if (session != null) {
				session.close();
				session = null;
			}
			/*if(!isConcurrentUpdateSusccess && i < 3){
				i++;
				isRewardAvailable(tableName, redeemIRDTO);
			}*/
			
			i=0;
		}
		return instantReserveDTOAwarded;
	}


	private boolean updateConcurrency(String tableName, InstantReserveDTO instantReserveDTO,int status) throws CommonException {
		Session session = null;
		Transaction transaction = null;
		boolean isTransfered = false;
		Query sqlQuery = null;
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			String sql = "Update "+tableName+" gift set gift.status=:newstatus where gift.status=:status and gift.id=:id";
			sqlQuery = session.createQuery(sql);
			sqlQuery.setParameter("newstatus",status);
			sqlQuery.setParameter("status", instantReserveDTO.getStatus());
			sqlQuery.setParameter("id", instantReserveDTO.getId());
			int i = sqlQuery.executeUpdate(); 
			transaction.commit();
			isTransfered = true;
		}catch(HibernateException e){
			logger.error(" Message : Hibernate Exception occured ",e);
			throw new CommonException(e.getMessage());
		
		}catch (Exception e) {
			logger.error(" Message :  Exception occured ",e);
			throw new CommonException(e.getMessage());
				
		}finally{
			if(!isTransfered && transaction!=null){
				transaction.rollback();
			}
		}
		return isTransfered;
	}


	public boolean grantReward(String tableName,InstantReserveDTO instantReserveDTO2) {


		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		Calendar cal = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		ArrayList<Integer> statusList = null;
		boolean isEligible = false;
		try {

			statusList = new ArrayList<Integer>();
			statusList.add(1);
			statusList.add(3);
			statusList.add(5);
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			criteria = session.createCriteria(tableName);
			criteria.add(Restrictions.and(
					Restrictions.eq("subscriberNumber",
							instantReserveDTO2.getSubscriberNumber()),
					Restrictions.eq("id", instantReserveDTO2.getId())));
				logger.info("Making reward as granted ["+instantReserveDTO2.getSubscriberNumber()+" And id "+instantReserveDTO2.getId()+"]");
			for (InstantReserveDTO instantReserveDTO : (List<InstantReserveDTO>) criteria
					.list()) {
				if (instantReserveDTO != null) {
					instantReserveDTO.setStatus(10);
					logger.info("Upating status to granted for Id ["+instantReserveDTO.getId()+"]");
				//	instantReserveDTO.setUpdateDate(new Date());
					session.update(tableName,instantReserveDTO);
					break;
				}
			}
			transaction.commit();
			transaction = null;
		} catch (HibernateException he) {
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
		return isEligible;
	
	}


	public void revertReward(String tableName,InstantReserveDTO instantReserveDTO,int status)  {
		Session  session = null;
		Transaction transaction = null;
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			InstantReserveDTO instDto = (InstantReserveDTO) session.get(tableName, instantReserveDTO.getId());
			instDto.setStatus(instantReserveDTO.getStatus());
			session.update(instDto);
					transaction.commit();
		} catch (HibernateException he) {
					if (transaction != null) {
						transaction.rollback();
						transaction = null;
					}
				} finally {
					if (session != null) {
						session.close();
						session = null;
					}
					/*if(!isConcurrentUpdateSusccess && i < 3){
						i++;
						isRewardAvailable(tableName, redeemIRDTO);
					}*/
					
					i=0;
				}
	}


	
	public InstantRewardConfigDTO getRewards(String offerId) {
		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		ArrayList<InstantRewardConfigDTO> instRewardConfigList = null;
		InstantRewardConfigDTO instantRewardConfigDTO = null;
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			criteria = session.createCriteria(InstantRewardConfigDTO.class);
			criteria.add(Restrictions.eq("packName", offerId));
			instRewardConfigList = (ArrayList<InstantRewardConfigDTO>) criteria.list();
			for(InstantRewardConfigDTO irConfig:instRewardConfigList){
				instantRewardConfigDTO = irConfig;
			}
		}catch(Exception e){
			logger.error("Exception occured ",e);
		}
		finally{
			if(transaction!=null){
				transaction.commit();
			}if(session != null){
				session.close();
			}
		}
		return instantRewardConfigDTO;
	}
	

}
