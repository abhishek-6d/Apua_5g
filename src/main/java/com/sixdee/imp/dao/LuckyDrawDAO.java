package com.sixdee.imp.dao;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.LuckyDrawDTO;

public class LuckyDrawDAO {
	Logger logger = Logger.getLogger(LuckyDrawDAO.class);

	public boolean insertIntoLuckyDraw(Long loyaltyID, String moNumber,
			int count) {
		Session session = null;
		Transaction transaction = null;
		LuckyDrawDTO luckyDrawDTO = null;
		boolean flag = false;
		try {
			luckyDrawDTO = new LuckyDrawDTO();
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			luckyDrawDTO.setLoyaltyId(loyaltyID);
			luckyDrawDTO.setMsisdn(moNumber);
			luckyDrawDTO.setCount(count);
			session.save(luckyDrawDTO);
			transaction.commit();
			flag = true;
		} catch (Exception e) {
			transaction.rollback();
			flag = false;
			logger.error(e);
			e.printStackTrace();
		} finally {
			if (session != null)
				session.close();
			session = null;
			transaction = null;
			luckyDrawDTO = null;
		}
		return flag;
	}

	public void writeCdr(Long loyaltyID, String moNumber) {
		logger.fatal(loyaltyID + "|" + moNumber + "|" + new Date());

	}

	public int getCount(Long loyaltyID) {

		Session session = null;
		String sql = null;
		Query query = null;
		int count = 0;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			sql = "select count(*) from LuckyDrawDTO where loyaltyId=:a";
			query = session.createQuery(sql);
			query.setParameter("a", loyaltyID);
			List l = query.list();
			count = Integer.parseInt(l.get(0).toString());
			count += 1;

		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return count;
	}

}
