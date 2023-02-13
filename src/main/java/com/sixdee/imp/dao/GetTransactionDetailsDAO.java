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
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.LoyaltyTransactionTabDTO;
import com.sixdee.imp.dto.TierAndBonusPointDetailsDTO;
import com.sixdee.imp.dto.TxnChannelStatusMappingDTO;

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
 *          <td>May 06,2013 06:00:07 PM</td>
 *          <td>Rahul K K</td>
 *          </tr>
 *          </table>
 *          </p>
 */

public class GetTransactionDetailsDAO {

	private static final Logger logger = Logger
			.getLogger(GetTransactionDetailsDAO.class);

	/**
	 * GetLoyaltyId is a function which will return the loyaltyId based on the
	 * subscriber number
	 * 
	 * @param String
	 *            for which loyalty Id has to be find
	 * @return LoyaltyId of the subscriber
	 */
	public String getLoyaltyId(String tableName, String subscriberNumber) {
		Session session = null;
		//Transaction transaction = null;
		Criteria criteria = null;
		String loyaltyID = null;
		Query query = null;
		logger.info("th table naem---->"+tableName);
		try {
			long t1 = System.currentTimeMillis();
			session = HiberanteUtil.getSessionFactory().openSession();

			//transaction = session.beginTransaction();
			criteria = session.createCriteria(tableName);
			criteria.add(Restrictions.or(
					Restrictions.eq("subscriberNumber",
							Long.parseLong(subscriberNumber)),
					Restrictions.eq("accountNumber",
							subscriberNumber)));
			// logger.info(Long.parseLong(subscriberNumber));
			criteria.setProjection(Projections.property("loyaltyID"));

			for (long lID : ((List<Long>) criteria.list())) {
				loyaltyID = lID + "";
			}
			//transaction.commit();
			long t2 = System.currentTimeMillis();
			logger.info("Time Taken for Executing DB operation " + (t2 - t1));

		} catch (HibernateException e) {
			logger.error("Hibernate Exception occured ", e);
			/*
			 * if (transaction != null) { transaction.rollback(); }
			 */
		} catch (Exception e) {
			logger.error("Exception occured  ", e);
		} finally {
			if (session != null) {
				session.close();
				session = null;
			}
		}
		return loyaltyID;
	}

	public ArrayList<LoyaltyTransactionTabDTO> getTransactions(
			String tableName, String loyaltyId, String fromDate,
			String endDate, int offSet, int limit, ArrayList<Integer> statusIds) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:SS");
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:SS");
		ArrayList<LoyaltyTransactionTabDTO> transactionList = null;
		Session session = null;
		//Transaction transaction = null;
		Criteria criteria = null;
		Query query = null;
		boolean isMySql = false;
		int noOfRecords = 0;
		Date startDate = null;
		Date endingDate = null;
		String monthname = "";
		String[] monthIndexes = null;
		Integer[] monthIndex = null;
		try {
			// noOfRecords = countTransactions(tableName, loyaltyId,
			// fromDate,endDate);

			long t1 = System.currentTimeMillis();
			session = HiberanteUtil.getSessionFactory().openSession();
			//transaction = session.beginTransaction();
			/*
			 * if(isMySql){ query =
			 * session.createSQLQuery("Select * from "+tableName+
			 * " where LOYALTY_ID=? and date_format(CREATE_DATE,'%d-%m-%Y') between ? and ? limit ? offset ?"
			 * ); }else{ query =
			 * session.createSQLQuery("Select * from "+tableName
			 * +" where LOYALTY_ID=? and CREATE_DATE between ? and ? "); }
			 * query.setParameter(0, loyaltyId); query.setParameter(1,
			 * sdf.parse(fromDate)); query.setParameter(2, sdf.parse(endDate));
			 * //query.setParameter(3, limit); //query.setParameter(4, offSet);
			 */
			/*
			 * Integer fm=0; Integer tm=0; if(fromDate.contains("-") &&
			 * endDate.contains("-")) {
			 * fm=Integer.parseInt(fromDate.split("-")[1]);
			 * tm=Integer.parseInt(endDate.split("-")[1]); }
			 * 
			 * if(fromDate.contains("/") && endDate.contains("/")) {
			 * fm=Integer.parseInt(fromDate.split("/")[1]);
			 * tm=Integer.parseInt(endDate.split("/")[1]); }
			 */

			if (fromDate.contains("-") && endDate.contains("-")) {
				startDate = sdf.parse(fromDate + " 00:00:00");
				endingDate = sdf.parse(endDate + " 23:59:59");
			}

			if (fromDate.contains("/") && endDate.contains("/")) {
				startDate = sdf1.parse(fromDate + " 00:00:00");
				endingDate = sdf1.parse(endDate + " 23:59:59");
			}

			Calendar myCal = Calendar.getInstance();
			myCal.setTime(startDate);

			Calendar myCal2 = Calendar.getInstance();
			myCal2.setTime(endingDate);

			do {
				int month = myCal.get(Calendar.MONTH);
				month += 1;
				monthname += month + ",";
				myCal.add(Calendar.MONTH, 1);
			} while (myCal.getTime().equals(myCal2.getTime())
					|| myCal.getTime().before(myCal2.getTime()));

			monthIndexes = monthname.substring(0, monthname.length() - 1)
					.split(",");

			logger.info("MONTH INDEXES::" + monthIndexes);

			monthIndex = new Integer[monthIndexes.length];
			for (int i = 0; i < monthIndexes.length; i++) {
				monthIndex[i] = Integer.parseInt(monthIndexes[i]);
			}

			criteria = session.createCriteria(tableName);
			criteria.add((Restrictions.eq("loyaltyID",
					Long.parseLong(loyaltyId))));
			criteria.add(Restrictions.ge("createTime",
					sdf.parse(fromDate + " 00:00:00")));
			criteria.add(Restrictions.le("createTime",
					sdf.parse(endDate + " 23:59:59")));
			if (Cache.isOracleDB) {
				criteria.add(Restrictions.in("monthIndex", monthIndex));
			}
			criteria.add(Restrictions.ne("statusID", 9));
		/*	if (statusIds + "" != null && !(statusIds + "").equalsIgnoreCase("")
					&& statusIds != 0 && statusIds == 8) {
				criteria.add(Restrictions.eq("statusID", statusIds));
			}else if (statusIds + "" != null && !(statusIds + "").equalsIgnoreCase("")
					&& statusIds != 0 && statusIds == 5) {
				criteria.add(Restrictions.or(Restrictions.eq("statusID", 5),Restrictions.eq("statusID",6)));
			}*/
			if(statusIds!=null){
				criteria.add(Restrictions.in("statusID", statusIds));
			}
			criteria.addOrder(Order.desc("createTime"));

			// criteria.setProjection(Projections.rowCount());

			criteria.setFirstResult(offSet < 0 ? 0 : offSet);
			if (limit > 0) {
				criteria.setMaxResults(limit);
			}

			// criteria.setMaxResults(limit);
			// query.list();
			transactionList = (ArrayList<LoyaltyTransactionTabDTO>) criteria
					.list();

			// System.out.println(transactionList.get(0).getCreateTime());
			//transaction.commit();
			long t2 = System.currentTimeMillis();
			logger.info("Getting transactions for LoyaltyID [" + loyaltyId
					+ "] from Date [" + fromDate + "] endDate [" + endDate
					+ "] offset [" + offSet + "] limit [" + limit + "] @ ["
					+ (t2 - t1) + "]");

		} catch (HibernateException e) {
			/*
			 * if (transaction != null) { transaction.rollback(); }
			 */
			logger.error("Exception occured on hibernate ", e);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
			//transaction = null;
			criteria = null;
			monthname = null;
			monthIndexes = null;
			monthIndex = null;
		}
		return transactionList;
	}

	public int countTransactions(String tableName, String loyaltyId,
			String fromDate, String endDate, ArrayList<Integer> statusIds)
			throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:SS");
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:SS");
		ArrayList<LoyaltyTransactionTabDTO> transactionList = null;
		Session session = null;
		//Transaction transaction = null;
		Criteria criteria = null;
		Query query = null;
		boolean isMySql = false;
		int size = 0;
		Date startDate = null;
		Date endingDate = null;
		String monthname = "";
		String[] monthIndexes = null;
		Integer[] monthIndex = null;
		try {

			if (fromDate.contains("-") && endDate.contains("-")) {

				startDate = sdf.parse(fromDate + " 00:00:00");
				endingDate = sdf.parse(endDate + " 23:59:59");
			}

			if (fromDate.contains("/") && endDate.contains("/")) {
				startDate = sdf1.parse(fromDate + " 00:00:00");
				endingDate = sdf1.parse(endDate + " 23:59:59");
			}

			Calendar myCal = Calendar.getInstance();
			myCal.setTime(startDate);
			Calendar myCal2 = Calendar.getInstance();
			myCal2.setTime(endingDate);
			do {
				int month = myCal.get(Calendar.MONTH);
				month += 1;
				monthname += month + ",";
				myCal.add(Calendar.MONTH, 1);
			} while (myCal.getTime().equals(myCal2.getTime())
					|| myCal.getTime().before(myCal2.getTime()));

			monthIndexes = monthname.substring(0, monthname.length() - 1)
					.split(",");

			logger.info("MONTH INDEXES::" + monthIndexes);

			monthIndex = new Integer[monthIndexes.length];
			for (int i = 0; i < monthIndexes.length; i++) {
				monthIndex[i] = Integer.parseInt(monthIndexes[i]);
			}

			long t1 = System.currentTimeMillis();
			session = HiberanteUtil.getSessionFactory().openSession();
			//transaction = session.beginTransaction();
			criteria = session.createCriteria(tableName);
			criteria.add((Restrictions.eq("loyaltyID",
					Long.parseLong(loyaltyId))));
			criteria.add(Restrictions.ge("createTime",
					sdf.parse(fromDate + " 00:00:00")));
			criteria.add(Restrictions.le("createTime",
					sdf.parse(endDate + " 23:59:59")));
			if (Cache.isOracleDB) {
				criteria.add(Restrictions.in("monthIndex", monthIndex));
			}
			criteria.add(Restrictions.ne("statusID", 9));
			/*if (statusIds + "" != null && !(statusIds + "").equalsIgnoreCase("")
					&& statusIds != 0 && statusIds == 8) {
				criteria.add(Restrictions.eq("statusID", statusIds));
			}else if (statusIds + "" != null && !(statusIds + "").equalsIgnoreCase("")
					&& statusIds != 0 && statusIds == 5) {
				criteria.add(Restrictions.or(Restrictions.eq("statusID", 5),Restrictions.eq("statusID",6)));
			}*/
			if(statusIds!=null){
				criteria.add(Restrictions.in("statusID", statusIds));
			}

			// query.list();
			transactionList = (ArrayList<LoyaltyTransactionTabDTO>) criteria
					.list();
			// System.out.println(transactionList.get(0).getCreateTime());
			//transaction.commit();
			long t2 = System.currentTimeMillis();
			size = transactionList != null ? transactionList.size() : 0;
			// logger.info("Getting transactions for LoyaltyID ["+loyaltyId+"] from Date ["+fromDate+"] endDate ["+endDate+"] offset ["+offSet+"] limit ["+limit+"] @ ["+(t2-t1)+"]");
			logger.info("No of Transactions Made [" + size + "]");
		} catch (HibernateException e) {
			/*
			 * if (transaction != null) { transaction.rollback(); }
			 */
			logger.error("Exception occured on hibernate ", e);
		} finally {
			if (session != null) {
				session.close();
			}
			//transaction = null;
			criteria = null;
			monthname = null;
			monthIndexes = null;
			monthIndex = null;
		}
		return size;

	}

	public ArrayList<LoyaltyTransactionTabDTO> getTransactions(
			String tableName, String loyaltyId, Date previousDate, int offset,
			int limit, ArrayList<Integer> statusIds) {

		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		ArrayList<LoyaltyTransactionTabDTO> transactionList = null;
		Session session = null;
		//Transaction transaction = null;
		Criteria criteria = null;
		Query query = null;
		String monthname = "";
		String[] monthIndexes = null;
		Integer[] monthIndex = null;
		try {

			long t1 = System.currentTimeMillis();
			session = HiberanteUtil.getSessionFactory().openSession();
			//transaction = session.beginTransaction();
			criteria = session.createCriteria(tableName);

			Calendar myCal = Calendar.getInstance();
			myCal.setTime(previousDate);

			Calendar myCal2 = Calendar.getInstance();

			do {
				int month = myCal.get(Calendar.MONTH);
				month += 1;
				monthname += month + ",";
				myCal.add(Calendar.MONTH, 1);
			} while (myCal.getTime().equals(myCal2.getTime())
					|| myCal.getTime().before(myCal2.getTime()));

			monthIndexes = monthname.substring(0, monthname.length() - 1)
					.split(",");

			logger.info("MONTH INDEXES::" + monthIndexes);

			monthIndex = new Integer[monthIndexes.length];
			for (int i = 0; i < monthIndexes.length; i++) {
				monthIndex[i] = Integer.parseInt(monthIndexes[i]);
			}

			criteria.add((Restrictions.eq("loyaltyID",
					Long.parseLong(loyaltyId))));
			criteria.add(Restrictions.ge("createTime", previousDate));
			criteria.add(Restrictions.ne("statusID", 9));
			/*if (statusIds + "" != null && !(statusIds + "").equalsIgnoreCase("")
					&& statusIds != 0 && statusIds == 8) {
				criteria.add(Restrictions.eq("statusID", statusIds));
			}else if (statusIds + "" != null && !(statusIds + "").equalsIgnoreCase("")
					&& statusIds != 0 && statusIds == 5) {
				criteria.add(Restrictions.or(Restrictions.eq("statusID", 5),Restrictions.eq("statusID",6)));
			}*/
			if(statusIds!=null){
				criteria.add(Restrictions.in("statusID", statusIds));
			}
			if (Cache.isOracleDB) {
				criteria.add(Restrictions.in("monthIndex", monthIndex));
			}
			// criteria.setProjection(Projections.rowCount());
			criteria.addOrder(Order.desc("createTime"));

			criteria.setFirstResult(offset < 0 ? 0 : offset);
			if (limit > 0) {
				criteria.setMaxResults(limit);
			}
			logger.info("Getting transactions for LoyaltyID [" + loyaltyId
					+ "] from Date [" + previousDate + "] offset [" + offset
					+ "] limit [" + limit + "]");

			transactionList = (ArrayList<LoyaltyTransactionTabDTO>) criteria
					.list();
			//transaction.commit();

			long t2 = System.currentTimeMillis();
			logger.info("Time Taken for Executing DB operation " + (t2 - t1));

			/*
			 * long t1= System.currentTimeMillis(); session =
			 * HiberanteUtil.getSessionFactory().openSession(); transaction =
			 * session.beginTransaction(); criteria =
			 * session.createCriteria(tableName); if(Cache.isMySqlDB) query =
			 * session.createSQLQuery("Select * from "+tableName+
			 * " where LOYALTY_ID=? and date_format(CREATE_DATE,'%d-%m-%Y') >= ? limit ? offset ?"
			 * ); else query =
			 * session.createSQLQuery("Select * from "+tableName+
			 * " where LOYALTY_ID=? and CREATE_DATE >= ? limit ? offset ?");
			 * 
			 * query.setParameter(1, loyaltyId); query.setParameter(2,
			 * (previousDate)); query.setParameter(3, limit);
			 * query.setParameter(4, offset);
			 * 
			 * transactionList = (ArrayList<LoyaltyTransactionTabDTO>)
			 * query.list(); transaction.commit(); long t2=
			 * System.currentTimeMillis();
			 * 
			 * logger.info("Getting transactions for LoyaltyID ["+loyaltyId+
			 * "] from Date ["
			 * +previousDate+"] offset ["+offset+"] limit ["+limit
			 * +"] @ "+(t2-t1));
			 */} catch (HibernateException e) {
			/*
			 * if (transaction != null) { transaction.rollback(); }
			 */
			logger.error("Exception occured on hibernate ", e);
		} finally {
			if (session != null) {
				session.close();
			}
			//transaction = null;
			criteria = null;
			sdf = null;
			monthname = null;
			monthIndexes = null;
			monthIndex = null;
		}
		return transactionList;

	}

	public static void main(String[] args) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -2);
		System.out.println(cal.getTime());
	}

	@SuppressWarnings("unchecked")
	public ArrayList<LoyaltyTransactionTabDTO> getLastNTransactions(
			String tableName, String loyaltyId, int noOfLastTransactions,
			int offset, ArrayList<Integer> statusIds) {
		Session session = null;
		//Transaction transaction = null;
		Criteria criteria = null;
		ArrayList<LoyaltyTransactionTabDTO> transactionList = null;
		String months = "";
		String[] monthIndexes = null;
		Integer[] monthIndex = null;

		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			//transaction = session.beginTransaction();

			int currMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
			logger.info("Current Month:" + currMonth);

			months = currMonth + ",";
			Calendar cal = Calendar.getInstance();

			for (int i = 1; i < 8; i++) {
				cal.add(Calendar.MONTH, -1);
				int month = cal.get(Calendar.MONTH) + 1;
				months += month + ",";
			}

			monthIndexes = months.substring(0, months.length() - 1).split(",");

			logger.info("MONTH INDEXES::" + monthIndexes);

			monthIndex = new Integer[monthIndexes.length];
			for (int i = 0; i < monthIndexes.length; i++) {
				monthIndex[i] = Integer.parseInt(monthIndexes[i]);
			}

			criteria = session.createCriteria(tableName);
			criteria.add((Restrictions.eq("loyaltyID",Long.parseLong(loyaltyId))));
			criteria.addOrder(Order.desc("createTime"));
			criteria.add(Restrictions.ne("statusID", 9));
		
			if(statusIds!=null){
				criteria.add(Restrictions.in("statusID", statusIds));
			}

			if (Cache.isOracleDB) {
				criteria.add(Restrictions.in("monthIndex", monthIndex));
			}
			criteria.setFirstResult(offset);
			criteria.setMaxResults(noOfLastTransactions);
			logger.info("Getting transactions for LoyaltyID [" + loyaltyId
					+ "] offset [" + offset + "] limit ["
					+ noOfLastTransactions + "]");
			transactionList = (ArrayList<LoyaltyTransactionTabDTO>) criteria.list();
			logger.info("Getting transactions for LoyaltyID [" + loyaltyId
					+ "] and List Size is"+transactionList.size());
			//transaction.commit();
		} catch (HibernateException e) {
			/*
			 * if (transaction != null) { transaction.rollback(); }
			 */
			logger.error("Exception occured on hibernate ", e);

		} catch (Exception e) {
			logger.error("Exception occured on hibernate ", e);

		} finally {
			if (session != null) {
				session.close();
			}
			//transaction = null;
			session = null;
			criteria = null;
			months = null;
			monthIndexes = null;
			monthIndex = null;
		}
		return transactionList;
	}

	public ArrayList<Integer> getStatusIds(String statusId, String channel) {
		Session session = null;
		//Transaction trx = null;
		ArrayList<Integer> statusidList = null;
		try {
			logger.info("Inside getStatusIds >>>>statusId>>>>"+statusId+">>>>channel>>>"+channel);
			session=HiberanteUtil.getSessionFactory().openSession();
			//trx = session.beginTransaction();
			Criteria criteria=session.createCriteria(TxnChannelStatusMappingDTO.class);
			criteria.add(Restrictions.eq("type",statusId+""));
			criteria.add(Restrictions.eq("channel",channel));
			
			List<TxnChannelStatusMappingDTO> list =criteria.list();
			logger.info(">>>>list size>>>>>"+list.size());
			if(list.size()>0){
				statusidList= new ArrayList<Integer>();
				for(TxnChannelStatusMappingDTO statusMappingDTO:list){
					statusidList.add(statusMappingDTO.getStatusId());
				}
				logger.info(">>statusid list size>>>>>"+statusidList.size());
			}
			
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			if (session != null && session.isOpen())
				session.close();
			session = null;
		}
		return statusidList;
	}

	public int countTransactions(String tableName, String loyaltyId,
			Date previousDate, ArrayList<Integer> statusIds) {

		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		ArrayList<LoyaltyTransactionTabDTO> transactionList = null;
		Session session = null;
		//Transaction transaction = null;
		Criteria criteria = null;
		int size = 0;
		String monthname = "";
		String[] monthIndexes = null;
		Integer[] monthIndex = null;
		try {
			// countTransactions(tableName, loyaltyId, fromDate,endDate);
			long t1 = System.currentTimeMillis();
			session = HiberanteUtil.getSessionFactory().openSession();
			//transaction = session.beginTransaction();

			Calendar myCal = Calendar.getInstance();
			myCal.setTime(previousDate);

			Calendar myCal2 = Calendar.getInstance();

			do {
				int month = myCal.get(Calendar.MONTH);
				month += 1;
				monthname += month + ",";
				myCal.add(Calendar.MONTH, 1);
			} while (myCal.getTime().equals(myCal2.getTime())
					|| myCal.getTime().before(myCal2.getTime()));

			monthIndexes = monthname.substring(0, monthname.length() - 1)
					.split(",");

			logger.info("MONTH INDEXES::" + monthIndexes);

			monthIndex = new Integer[monthIndexes.length];
			for (int i = 0; i < monthIndexes.length; i++) {
				monthIndex[i] = Integer.parseInt(monthIndexes[i]);
			}

			criteria = session.createCriteria(tableName);
			criteria.add((Restrictions.eq("loyaltyID",
					Long.parseLong(loyaltyId))));
			criteria.add(Restrictions.ge("createTime", (previousDate)));
			criteria.add(Restrictions.ne("statusID", 9));
			/*if (statusIds + "" != null && !(statusIds + "").equalsIgnoreCase("")
					&& statusIds != 0 && statusIds == 8) {
				criteria.add(Restrictions.eq("statusID", statusIds));
			}else if (statusIds + "" != null && !(statusIds + "").equalsIgnoreCase("")
					&& statusIds != 0 && statusIds == 5) {
				criteria.add(Restrictions.or(Restrictions.eq("statusID", 5),Restrictions.eq("statusID",6)));
			}*/
			if(statusIds!=null){
				criteria.add(Restrictions.in("statusID", statusIds));
			}

			if (Cache.isOracleDB) {
				criteria.add(Restrictions.in("monthIndex", monthIndex));
				// criteria.add(Restrictions.le("createTime",sdf.parse(endDate)));
			}

			// criteria.setProjection(Projections.rowCount());

			// query.list();
			transactionList = (ArrayList<LoyaltyTransactionTabDTO>) criteria
					.list();
			// System.out.println(transactionList.get(0).getCreateTime());
			//transaction.commit();
			long t2 = System.currentTimeMillis();
			size = transactionList != null ? transactionList.size() : 0;
			// logger.info("Getting transactions for LoyaltyID ["+loyaltyId+"] from Date ["+fromDate+"] endDate ["+endDate+"] offset ["+offSet+"] limit ["+limit+"] @ ["+(t2-t1)+"]");

		} catch (HibernateException e) {
			/*
			 * if (transaction != null) { transaction.rollback(); }
			 */
			logger.error("Exception occured on hibernate ", e);
		} finally {
			if (session != null) {
				session.close();
			}
			//transaction = null;
			criteria = null;
			monthname = "";
			monthIndexes = null;
			monthIndex = null;
		}
		return size;

	}

	public String getPackageName(Integer packageId, String langID) {
		// TODO Auto-generated method stub
		Session session = null;
		//Transaction transaction = null;
		Criteria criteria = null;
		String loyaltyID = null;
		Query query = null;
		String sql = null;
		String details = "";
		try {
			long t1 = System.currentTimeMillis();
			session = HiberanteUtil.getSessionFactory().openSession();

			//transaction = session.beginTransaction();
			sql = "select PACKAGE_SYNONM,REDEEM_POINTS from PACKAGE_DETAILS where PACKAGE_ID=? AND LANGUAGE_ID=?";
			query = session.createSQLQuery(sql);
			query.setParameter(0, packageId);
			query.setParameter(1, Integer.parseInt(langID));
			logger.info("PACKAGE ID>>" + packageId);
			logger.info("LANG ID>>" + langID);
			List<Object[]> list = query.list();
			for (Object[] obj : list) {
				if (obj[0] != null && obj[1] != null) {
					details = obj[0].toString() + ":" + obj[1].toString();
				}
			}
			//transaction.commit();
		} catch (HibernateException e) {
			logger.error("Hibernate Exception occured ", e);
			/*
			 * if (transaction != null) { transaction.rollback(); }
			 */
		} catch (Exception e) {
			logger.error("Exception occured  ", e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return details;
	}
	
	public String getPackageNameDetails(Integer packageId, String langID) {
		// TODO Auto-generated method stub
		Session session = null;
		//Transaction transaction = null;
		Query query = null;
		String sql = null;
		String details = "";
		try {
			long t1 = System.currentTimeMillis();
			session = HiberanteUtil.getSessionFactory().openSession();

			//transaction = session.beginTransaction();
			sql = "select OFN.OFFERNAME from LMS_CNFG_OFFER_MASTER OFM,LMS_CNFG_OFFER_NOMENCLATURE OFN where OFM.OFFERID=? and OFN.LANGUAGEID=? and OFM.OFFERID=OFN.OFFERID";
			query = session.createSQLQuery(sql);
			query.setParameter(0, packageId);
			query.setParameter(1, Integer.parseInt(langID));
			logger.info("PACKAGE ID>>" + packageId);
			logger.info("LANG ID>>" + langID);
			Object list = query.uniqueResult();
				if (list != null ) {
					details =list.toString();
					logger.info("Record :"+details);
				}
			//transaction.commit();
		} catch (HibernateException e) {
			logger.error("Hibernate Exception occured ", e);
			/*
			 * if (transaction != null) { transaction.rollback(); }
			 */
		} catch (Exception e) {
			logger.error("Exception occured  ", e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return details;
	}

}
