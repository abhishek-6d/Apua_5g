/**
 * 
 */
package com.sixdee.imp.dao;

import java.util.ArrayList;
import java.util.Date;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.LmsOnmDayWiseBean;


/**
 * @author rahul.krishnan
 *
 */
public class ExpiryPointsDAO {

	public ArrayList<LmsOnmDayWiseBean> getFirstExpiringPoint(String loyaltyId) {
		String hql = null;
		ArrayList<LmsOnmDayWiseBean> lmsOnmDayWiseList = null;
		Query q = null;
		Session session = null;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			hql = " from LmsOnmDayWiseBean where loyaltyId=:loyaltyId and " + "tierPoints > 0 and DATE(expiryDate)>=DATE(now()) order by expiryDate asc";
			q = session.createQuery(hql).setString("loyaltyId", loyaltyId).setMaxResults(20);

			lmsOnmDayWiseList = (ArrayList<LmsOnmDayWiseBean>) q.list();
			System.out.println("Daywise points info List Size "+lmsOnmDayWiseList.size());
		} catch (Exception e) {
			System.out.println("RequestId : Exception occured " + e);
			e.printStackTrace();
		} finally {
			 if(session != null) {
				session.close();
			}
		}
		return lmsOnmDayWiseList;
	}

	public int updateExpiringPoint(LmsOnmDayWiseBean lmsOnmDayWiseBean,long usedPoints) {
		String hql = null;
		Query q = null;
		int updatedCount = 0;
		Session session = null;
		Transaction txn=null;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			txn = session.beginTransaction();
			System.out.println("lmsOnmDayWiseBean "+lmsOnmDayWiseBean.toString());
			//hql = "Update LmsOnmDayWiseBean set tierPoints = tierPoints-:consumedPoints,totalPoints=tierPoints+bonusPoints,"
					//+ "usedTierPoints=usedTierPoints+:consumedPoints where loyaltyId = :loyaltyId and expiryDate=:date";
					hql = "Update LmsOnmDayWiseBean set tierPoints = tierPoints-:consumedPoints,totalPoints=tierPoints+bonusPoints"
							 +" where loyaltyId = :loyaltyId and expiryDate=:date and msisdn=:msisdn";
			q = session.createQuery(hql).setLong("consumedPoints", usedPoints)
					.setString("loyaltyId", lmsOnmDayWiseBean.getLoyaltyId())
					.setDate("date", lmsOnmDayWiseBean.getExpiryDate()).setString("msisdn",lmsOnmDayWiseBean.getMsisdn());
			updatedCount = q.executeUpdate();
			System.out.println("Updated Count "+updatedCount);
		} catch (Exception e) {
			System.out.println("RequestId : Exception occured " + e);
			e.printStackTrace();
		} finally {
			if(txn != null) {
				txn.commit();
			} if(session != null) {
				session.close();
			}
		}
		return updatedCount;
	}
	
	public static void main(String[] args) {
		Transaction txn = null;
		Session session = null;
		LmsOnmDayWiseBean lmsOnmDayWiseBean=new LmsOnmDayWiseBean();
		lmsOnmDayWiseBean.setTierPoints(120);
		lmsOnmDayWiseBean.setBonusPoints(0);
		lmsOnmDayWiseBean.setLoyaltyId("2877792549058830");
		Date date=new Date();
		lmsOnmDayWiseBean.setExpiryDate(null);
		
		//session = HiberanteUtilDemo.getSessionFactory().openSession();
		txn = session.beginTransaction();
	}

}
