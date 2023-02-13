package com.sixdee.imp.dao;

/**
 * 
 * @author Nithin Kunjappan
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
 * <td>May 21,2015 07:10:21 PM</td>
 * <td>Nithin Kunjappan</td>
 * </tr>
 * </table>
 * </p>
 */

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import com.sixdee.imp.common.util.HiberanteUtilProfile;
import com.sixdee.imp.common.util.HiberanteUtilRule;
import com.sixdee.imp.dto.PrepaidInstantDTO;
import com.sixdee.imp.dto.PrepaidProfileDTO;
import com.sixdee.imp.dto.ReferralServiceDTO;

public class ReferralServiceDAO {
	
	Logger logger=Logger.getLogger(ReferralServiceDAO.class);
	
	SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy");
	SimpleDateFormat txnFormat=new SimpleDateFormat("ddMMyyHHmmSS");
	
	public boolean insertProfileDetails(ReferralServiceDTO refferalServiceDTO) {

		Session session = null;
		Transaction tx = null;
		boolean flag = false;
		PrepaidProfileDTO prepaidDTO = null;
		TableInfoDAO infoDAO = new TableInfoDAO();
		Date today=new Date();
		try {
			long start = System.currentTimeMillis();
			session = HiberanteUtilProfile.getSessionFactory().openSession();
			tx = session.beginTransaction();

			prepaidDTO = new PrepaidProfileDTO();
		
			prepaidDTO.setMsisdn(refferalServiceDTO.getReferee());
			prepaidDTO.setReferrer(refferalServiceDTO.getReferrer());
			prepaidDTO.setReferralDate(dateFormat.parse(refferalServiceDTO.getReferralDate()));
			prepaidDTO.setActivationDate(today);
			prepaidDTO.setLanguageId(refferalServiceDTO.getLanguageId());
			
			String tableName = infoDAO.getPrepaidProfileName(refferalServiceDTO.getReferee());
			logger.info("Values to Insert in ["+tableName+"] = REFEREE MSISDN:["+prepaidDTO.getMsisdn()+"],REFERRER MSISDN:["+prepaidDTO.getReferrer()+"],REFERRAL DATE:["+prepaidDTO.getReferralDate()+"],ACTIVATION DATE:["+prepaidDTO.getActivationDate()+"],LANGUAGE ID:["+prepaidDTO.getLanguageId()+"]");
			session.save(tableName, prepaidDTO);
			tx.commit();
			long finaltime = System.currentTimeMillis() - start;
			logger.info("Time taken for Inserting into Profile DB:" + finaltime);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception here ==>", e);
		} finally {
			if (session != null)
				session.close();
		}
		return flag;
	}

	public boolean checkProfileDetails(ReferralServiceDTO referralServiceDTO) {

		Session session = null;
		Criteria criteria = null;
		TableInfoDAO infoDAO = new TableInfoDAO();
		boolean flag = false;
		try {
			session = HiberanteUtilProfile.getSessionFactory().openSession();
			long t1 = System.currentTimeMillis();

			String tableName = infoDAO.getPrepaidProfileName(referralServiceDTO
					.getReferee());
			criteria = session.createCriteria(tableName);
			criteria.add(Restrictions.eq("msisdn",
					referralServiceDTO.getReferee()));
			criteria.setProjection(Projections.rowCount());
			Integer count = (Integer) criteria.uniqueResult();

			logger.info("Count:" + count);

			session.close();
			session = null;
			
			long t2 = System.currentTimeMillis();
			logger.info("Time Taken for Executing DB operation " + (t2 - t1));
			
			if (count > 0)
				flag = updateProfileDetails(referralServiceDTO);
			else
				flag = insertProfileDetails(referralServiceDTO);
			
		} catch (HibernateException e) {
			logger.error("Hibernate Exception occured ", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occured  ", e);
		} finally {
			if (session != null)
				session.close();
		}

		return flag;

	}

	private boolean updateProfileDetails(ReferralServiceDTO referralServiceDTO) {

		Session session = null;
		Transaction tx = null;
		boolean flag = false;
		TableInfoDAO infoDAO = new TableInfoDAO();
		Query query=null;
		String sql="";
		try {
			long start = System.currentTimeMillis();
			session = HiberanteUtilProfile.getSessionFactory().openSession();
			tx = session.beginTransaction();
			
			String tableName = infoDAO.getPrepaidProfileTableName(referralServiceDTO.getReferee());
			
			logger.info("Values to Update in ["+tableName+"] = REFEREE MSISDN:["+referralServiceDTO.getReferee()+"],REFERRER MSISDN:["+referralServiceDTO.getReferrer()+"],REFFERAL DATE:["+referralServiceDTO.getReferralDate()+"],ACTIVATION DATE:["+new Date()+"],LANGUAGE ID:["+referralServiceDTO.getLanguageId()+"]");
			
			sql="UPDATE "+tableName+" set FIELD44= :referrer,FIELD76= TO_DATE(:referralDate, 'DD-MM-YYYY'),FIELD36=:now, FIELD61=:languageId where FIELD2 = :subsNumber";
			query = session.createSQLQuery(sql);
			query.setParameter("referrer",referralServiceDTO.getReferrer());
			query.setParameter("referralDate",referralServiceDTO.getReferralDate());
			query.setParameter("now", new Date());
			query.setParameter("languageId",referralServiceDTO.getLanguageId());
			query.setParameter("subsNumber",referralServiceDTO.getReferee());

			int x=query.executeUpdate();
			logger.info("Rows Updated:"+x);
			tx.commit();
			long finaltime = System.currentTimeMillis() - start;
			logger.info("Time taken for Updating Profile DB:" + finaltime);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception here ==>", e);
		} finally {
			if (session != null)
				session.close();
		}
		return flag;
	}
	
	public boolean insertInstantDetails(ReferralServiceDTO referralServiceDTO) {

		Session session = null;
		Transaction tx = null;
		boolean flag = false;
		PrepaidInstantDTO prepaidDTO = null;
		Date now = new Date();
		try {
			long start = System.currentTimeMillis();
			session = HiberanteUtilRule.getSessionFactory().openSession();
			tx = session.beginTransaction();

			prepaidDTO = new PrepaidInstantDTO();
			prepaidDTO.setMsisdn(referralServiceDTO.getReferee());
			prepaidDTO.setReferrer(referralServiceDTO.getReferrer());
			prepaidDTO.setLanguageId(referralServiceDTO.getLanguageId());
			if(referralServiceDTO.getReferralDate()!=null && !referralServiceDTO.getReferralDate().equalsIgnoreCase(""))
				prepaidDTO.setReferralDate(dateFormat.parse(referralServiceDTO.getReferralDate()));
			else
				prepaidDTO.setReferralDate(now);
			prepaidDTO.setCreatedBy("LMS");
			prepaidDTO.setDate(now);
			prepaidDTO.setCreateDate(now);

			logger.info("Values to Insert in [PREPAID_INSTANT_CDR_0] = REFEREE MSISDN:["+prepaidDTO.getMsisdn()+"],REFERRER MSISDN:["+prepaidDTO.getReferrer()+"],REFERRAL DATE:["+prepaidDTO.getReferralDate()+"]");
			
			session.save(prepaidDTO);
			tx.commit();
			long finaltime = System.currentTimeMillis() - start;
			logger.info("Time taken for Inserting into DB:" + finaltime);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception here ==>", e);
		} finally {
			if (session != null)
				session.close();
		}
		return flag;
	
	}
}
