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
 * <td>May 29,2013 12:11:41 PM</td>
 * <td>Nithin Kunjappan</td>
 * </tr>
 * </table>
 * </p>
 */



import java.math.BigDecimal;
import java.math.BigInteger;
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

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.GetOrderDetailsDTO;
import com.sixdee.imp.dto.LoyaltyRegisteredNumberTabDTO;
import com.sixdee.imp.dto.LoyaltyTransactionTabDTO;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;
import com.sixdee.imp.dto.VoucherOrderDetailsDTO;

public class GetOrderDetailsDAO {


	private static final Logger logger = Logger.getLogger(GetOrderDetailsDAO.class);
	
	/**
	 * GetLoyaltyId is a function which will return the loyaltyId based on the subscriber number 
	 *
	 * @param String for which loyalty Id has to be find
	 * @return LoyaltyId of the subscriber
	 */
	/*public String getLoyaltyId(String tableName,String subscriberNumber) {
		Session session = null;
		Transaction transaction = null;
		String  sql = null;
		BigDecimal loyaltyID = null;
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			sql="SELECT LOYALTY_ID FROM "+tableName+" WHERE SUBSCRIBER_NUMBER=?";
			
			Query query=session.createSQLQuery(sql);
			query.setParameter(0,subscriberNumber);
			
			List<BigDecimal> list=query.list();
			if(list.size()>0)
			{
				loyaltyID=list.get(0);				
			}	
				
			transaction.commit();
			}catch (HibernateException e) {
				logger.error("Hibernate Exception occured ",e);
				if(transaction != null)
					transaction.rollback();
			}catch (Exception e) {
				logger.error("Exception occured  ",e);
			}finally{
				if(session != null)
					session.close();
			}
			return ""+loyaltyID;
	}*/
	
	public String getLoyaltyId(String tableName,String subscriberNumber) {
		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		String loyaltyID = null;
		Query query = null;
		try{
			long t1 = System.currentTimeMillis();
			session = HiberanteUtil.getSessionFactory().openSession();

			transaction = session.beginTransaction();
			criteria = session.createCriteria(tableName);
			criteria.add(Restrictions.or(Restrictions.eq("subscriberNumber", Long.parseLong(subscriberNumber)),Restrictions.eq("accountNumber", Long.parseLong(subscriberNumber))));
			//logger.info(Long.parseLong(subscriberNumber));
			criteria.setProjection(Projections.property("loyaltyID"));
			
			for(long lID : ((List<Long>)criteria.list()))
				loyaltyID = lID+"";
			transaction.commit();
			long t2 = System.currentTimeMillis();
			logger.info("Time Taken for Executing DB operation "+(t2-t1));
		
			}catch (HibernateException e) {
				logger.error("Hibernate Exception occured ",e);
				if(transaction != null)
					transaction.rollback();
			}catch (Exception e) {
				logger.error("Exception occured  ",e);
			}finally{
				if(session != null)
					session.close();
			}
			return loyaltyID;
	}
	public ArrayList<VoucherOrderDetailsDTO> getOrderDetails(String tableName,String loyaltyId,String langID, String fromDate,
			String endDate, int offSet, int limit) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		ArrayList<VoucherOrderDetailsDTO> orderList= null;
		Session session = null;
		Transaction transaction = null;
		String sql = null;
		VoucherOrderDetailsDTO voucherDTO=null;
		try{
			orderList= new ArrayList<VoucherOrderDetailsDTO>();
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();	
			
			fromDate+=" 00:00:00";
			endDate+=" 23:59:59";		
			logger.info("fromDate modified DATE"+fromDate);
			logger.info("endDate modified DATE"+endDate);
			
			sql="SELECT VD.ORDER_ID,VD.ORDER_DATE,VD.VOUCHER_NUMBER,PD.PACKAGE_NAME,VD.QUANTITY,VD.REDEEM_POINTS,VD.ORDER_STATUS,PD.EXPIRY_DATE "+ 
			"from "+tableName+" VD,PACKAGE_DETAILS PD WHERE VD.LOYALTY_ID=? AND VD.VOUCHER_NUMBER=PD.PACKAGE_ID AND PD.LANGUAGE_ID=? AND VD.ORDER_DATE ";
			if(Cache.isOracleDB)
				sql+=" Between TO_DATE('"+fromDate+"','dd-mm-yyyy HH24:MI:SS') and TO_DATE('"+endDate+"', 'dd-mm-yyyy HH24:MI:SS') ORDER BY VD.ORDER_DATE DESC";	
			else
				sql+=" Between STR_TO_DATE('"+fromDate+"','%d-%m-%Y %H:%i:%s') and STR_TO_DATE('"+endDate+"', '%d-%m-%Y %H:%i:%s') ORDER BY VD.ORDER_DATE DESC";

			Query query=session.createSQLQuery(sql);
			query.setParameter(0,loyaltyId);
			query.setParameter(1,langID);
			query.setFirstResult(offSet);
			query.setMaxResults(limit);
			
			
			logger.info("Query:>>"+sql);
			
			logger.info("LOYALTY ID"+loyaltyId);
			logger.info("LANG ID"+langID);
			logger.info("FROM DATE"+(Date)sdf.parse(fromDate));
			logger.info("END DATE"+(Date)sdf.parse(endDate));
			
		
			
			List<Object[]> list=query.list();
			
			for(Object[] obj:list)
			{
				voucherDTO= new VoucherOrderDetailsDTO();
				voucherDTO.setOrderId((String) (obj[0]==null?0:(obj[0].toString())));
				voucherDTO.setOrderDate((Date)(obj[1]));
				voucherDTO.setVoucherNumber(obj[2]==null?null:obj[2].toString());
				voucherDTO.setVoucherName(obj[3]==null?null:obj[3].toString());
				voucherDTO.setQuantity(obj[4]==null?0:Integer.parseInt(obj[4].toString()));
				voucherDTO.setRedeemPoints(obj[5]==null?0:Integer.parseInt(obj[5].toString()));
				voucherDTO.setOrderStatus(obj[6]==null?null:(obj[6].toString()));
				voucherDTO.setExpiryDate((Date)(obj[7]));
				orderList.add(voucherDTO);			
			}	
			transaction.commit();
			
		}
		catch (HibernateException e) {
			if(transaction!=null)
				transaction.rollback();
			logger.error("Exception occured on hibernate ",e);
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally{
			if(session != null)
				session.close();
			transaction = null;
		//	criteria = null;
		}
		return orderList ;
	}
	public ArrayList<VoucherOrderDetailsDTO> getOrderDetails(
			String tableName, String loyaltyId,String langID, Date previousDate, int offset, int limit) {

	//	SimpleDateFormat sdf = new SimpleDateFormat("MM");
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		ArrayList<VoucherOrderDetailsDTO> orderList= null;
		Session session = null;
		Transaction transaction = null;
		VoucherOrderDetailsDTO voucherDTO=null;
		String sql=null;
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			sql="SELECT VD.ORDER_ID,VD.ORDER_DATE,VD.VOUCHER_NUMBER,PD.PACKAGE_NAME,VD.QUANTITY,VD.REDEEM_POINTS,VD.ORDER_STATUS,PD.EXPIRY_DATE,PD.LANGUAGE_ID "+ 
			"from "+tableName+" VD ,PACKAGE_DETAILS PD WHERE VD.LOYALTY_ID=? AND VD.VOUCHER_NUMBER=PD.PACKAGE_ID AND PD.LANGUAGE_ID=? AND VD.ORDER_DATE >= ? ORDER BY VD.ORDER_DATE DESC";
			//" STR_TO_DATE(date_format(ORDER_DATE,'%d-%m-%Y'),'%d-%m-%Y') >= str_to_date(?,'%d-%m-%Y')";
			/*"STR_TO_DATE(date_format(ORDER_DATE,'%d-%m-%Y'),'%d-%m-%Y') Between STR_TO_DATE(?,'%d-%m-%Y') "+
			"AND STR_TO_DATE(?,'%d-%m-%Y')";
			*/
			//"ORDER_DATE Between ? "+
		//	"AND ?";
			logger.info("Query:>>"+sql);
			Query query=session.createSQLQuery(sql);
			query.setParameter(0,loyaltyId);
			query.setParameter(1,langID);
			if(Cache.isOracleDB)
				query.setParameter(2,(Date)previousDate);
			else
				query.setParameter(2,sdf.format(previousDate));
			query.setFirstResult(offset);
			query.setMaxResults(limit);
			logger.info("LOYALTY ID"+loyaltyId);
			logger.info("LANG ID"+langID);
			logger.info("PREVIOUS DATE"+(Date)previousDate);
			List<Object[]> list=query.list();
			orderList= new ArrayList<VoucherOrderDetailsDTO>();
			for(Object[] obj:list)
			{
				voucherDTO= new VoucherOrderDetailsDTO();
				voucherDTO.setOrderId((String) (obj[0]==null?0:(obj[0].toString())));
				voucherDTO.setOrderDate((Date)(obj[1]));
				voucherDTO.setVoucherNumber(obj[2]==null?null:obj[2].toString());
				voucherDTO.setVoucherName(obj[3]==null?null:obj[3].toString());
				voucherDTO.setQuantity(obj[4]==null?0:Integer.parseInt(obj[4].toString()));
				voucherDTO.setRedeemPoints(obj[5]==null?0:Integer.parseInt(obj[5].toString()));
				voucherDTO.setOrderStatus(obj[6]==null?null:(obj[6].toString()));
				voucherDTO.setExpiryDate((Date)(obj[7]));
				orderList.add(voucherDTO);			
			}	
			transaction.commit();
		}catch (HibernateException e) {
			if(transaction!=null)
				transaction.rollback();
			logger.error("Exception occured on hibernate ",e);
		} finally{
			if(session != null)
				session.close();
			transaction = null;
		//	criteria = null;
			sdf = null;
		}
		return orderList ;
	
	}
	
	
	public ArrayList<VoucherOrderDetailsDTO> getVoucherDetails(String tableName, String loyaltyId) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		ArrayList<VoucherOrderDetailsDTO> orderList= null;
		Session session = null;
		Transaction transaction = null;
		VoucherOrderDetailsDTO voucherDTO=null;
		String sql=null;
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			sql="SELECT VD.ORDER_ID,VD.ORDER_DATE,VD.VOUCHER_NUMBER,PD.PACKAGE_NAME,VD.QUANTITY,VD.REDEEM_POINTS,VD.ORDER_STATUS,PD.EXPIRY_DATE,PD.LANGUAGE_ID "+ 
			" from "+tableName+" VD ,PACKAGE_DETAILS PD WHERE VD.LOYALTY_ID=? AND VD.VOUCHER_NUMBER=PD.PACKAGE_ID AND PD.LANGUAGE_ID=? AND VD.ORDER_STATUS=1 "+
			" ORDER BY VD.ORDER_DATE DESC";
			logger.info("Query:>>"+sql);
			Query query=session.createSQLQuery(sql);
			query.setParameter(0,loyaltyId);
			query.setParameter(1,1);
			query.setFirstResult(0);
			//query.setMaxResults(limit);
			logger.info("LOYALTY ID"+loyaltyId);
			logger.info("LANG ID"+1);
			List<Object[]> list=query.list();
			orderList= new ArrayList<VoucherOrderDetailsDTO>();
			for(Object[] obj:list)
			{
				voucherDTO= new VoucherOrderDetailsDTO();
				voucherDTO.setOrderId((String) (obj[0]==null?0:(obj[0].toString())));
				voucherDTO.setOrderDate((Date)(obj[1]));
				voucherDTO.setVoucherNumber(obj[2]==null?null:obj[2].toString());
				voucherDTO.setVoucherName(obj[3]==null?null:obj[3].toString());
				voucherDTO.setQuantity(obj[4]==null?0:Integer.parseInt(obj[4].toString()));
				voucherDTO.setRedeemPoints(obj[5]==null?0:Integer.parseInt(obj[5].toString()));
				voucherDTO.setOrderStatus(obj[6]==null?null:(obj[6].toString()));
				voucherDTO.setExpiryDate((Date)(obj[7]));
				orderList.add(voucherDTO);	
			}	
			transaction.commit();
		}catch (HibernateException e) {
			if(transaction!=null)
				transaction.rollback();
			logger.error("Exception occured on hibernate ",e);
		} finally{
			if(session != null)
				session.close();
			transaction = null;
		//	criteria = null;
			sdf = null;
		}
		return orderList ;
	
	}
	
	
	public static void main(String[] args) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -2);
		System.out.println(cal.getTime());
	}
	public ArrayList<VoucherOrderDetailsDTO> getLastNTransactions(
			String tableName, String loyaltyId,String langID,  int noOfLastTransactions, int offset) {
		Session session = null;
		Transaction transaction = null;
		ArrayList<VoucherOrderDetailsDTO> orderList= null;
		VoucherOrderDetailsDTO voucherDTO=null;
		String sql=null;
		
		try{
			orderList= new ArrayList<VoucherOrderDetailsDTO>();
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			sql="SELECT VD.ORDER_ID,VD.ORDER_DATE,VD.VOUCHER_NUMBER,PD.PACKAGE_NAME,VD.QUANTITY,VD.REDEEM_POINTS,VD.ORDER_STATUS,PD.EXPIRY_DATE "+
			"from "+tableName+" VD, PACKAGE_DETAILS PD WHERE LOYALTY_ID=? AND VD.VOUCHER_NUMBER=PD.PACKAGE_ID AND PD.LANGUAGE_ID=?  ORDER BY VD.ORDER_DATE DESC";
			
			logger.info("Query:>>"+sql);
			
			Query query=session.createSQLQuery(sql);
			query.setParameter(0,loyaltyId);
			query.setParameter(1,langID);
			query.setFirstResult(offset);
			query.setMaxResults(noOfLastTransactions);
			
			logger.info("LOYALTY ID"+loyaltyId);
			logger.info("LANG ID"+langID);
			logger.info("NO OF TRANS"+noOfLastTransactions);
			
			List<Object[]> list=query.list();
			orderList= new ArrayList<VoucherOrderDetailsDTO>();
			for(Object[] obj:list)
			{			
				voucherDTO=new VoucherOrderDetailsDTO();
				voucherDTO.setOrderId((String) (obj[0]==null?0:(obj[0].toString())));
				voucherDTO.setOrderDate((Date)(obj[1]));
				voucherDTO.setVoucherNumber(obj[2]==null?null:obj[2].toString());
				voucherDTO.setVoucherName(obj[3]==null?null:obj[3].toString());
				voucherDTO.setQuantity(obj[4]==null?0:Integer.parseInt(obj[4].toString()));
				voucherDTO.setRedeemPoints(obj[5]==null?0:Integer.parseInt(obj[5].toString()));
				voucherDTO.setOrderStatus(obj[6]==null?null:(obj[6].toString()));
				voucherDTO.setExpiryDate((Date)(obj[7]));
				orderList.add(voucherDTO);			
			}	
			transaction.commit();
		}catch (HibernateException e) {
			if(transaction != null)
				transaction.rollback();
			logger.error("Exception occured on hibernate ",e);
			
		}catch (Exception e) {
			logger.error("Exception occured on hibernate ",e);
			
		}finally{
			if(session != null)
				session.close();
			transaction = null;
			session = null;
		}
		return orderList;
	}
	public ArrayList<VoucherOrderDetailsDTO> getOrderLists(String tableName,
			String langID, String fromDate, String endDate, int offSet, int limit) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		ArrayList<VoucherOrderDetailsDTO> orderList= null;
		Session session = null;
		Transaction transaction = null;
		String sql = null;
		VoucherOrderDetailsDTO voucherDTO=null;
		try{
			orderList= new ArrayList<VoucherOrderDetailsDTO>();
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();	
			
			fromDate+=" 00:00:00";
			endDate+=" 23:59:59";		
			logger.info("fromDate modified DATE"+fromDate);
			logger.info("endDate modified DATE"+endDate);
			
			sql="SELECT VD.ORDER_ID,VD.ORDER_DATE,VD.VOUCHER_NUMBER,PD.PACKAGE_NAME,VD.QUANTITY,VD.REDEEM_POINTS,VD.ORDER_STATUS,PD.EXPIRY_DATE "+ 
			"from "+tableName+" VD,PACKAGE_DETAILS PD WHERE VD.VOUCHER_NUMBER=PD.PACKAGE_ID AND PD.LANGUAGE_ID=? AND VD.ORDER_DATE ";
			
			if(Cache.isOracleDB)
				sql+=" Between TO_DATE('"+fromDate+"','dd-mm-yyyy HH24:MI:SS') and TO_DATE('"+endDate+"', 'dd-mm-yyyy HH24:MI:SS') ORDER BY VD.ORDER_DATE DESC";	
			else
				sql+=" Between STR_TO_DATE('"+fromDate+"','%d-%m-%Y %H:%i:%s') and STR_TO_DATE('"+endDate+"', '%d-%m-%Y %H:%i:%s') ORDER BY VD.ORDER_DATE DESC";
			Query query=session.createSQLQuery(sql);
			query.setParameter(0,langID);
			query.setFirstResult(offSet);
			query.setMaxResults(limit);
			
			logger.info("Query:>>"+sql);
			
			logger.info("LANG ID"+langID);
			logger.info("FROM DATE"+(Date)sdf.parse(fromDate));
			logger.info("END DATE"+(Date)sdf.parse(endDate));
			List<Object[]> list=query.list();
			
			for(Object[] obj:list)
			{
				voucherDTO= new VoucherOrderDetailsDTO();
				voucherDTO.setOrderId((String) (obj[0]==null?0:(obj[0].toString())));
				voucherDTO.setOrderDate((Date)(obj[1]));
				voucherDTO.setVoucherNumber(obj[2]==null?null:obj[2].toString());
				voucherDTO.setVoucherName(obj[3]==null?null:obj[3].toString());
				voucherDTO.setQuantity(obj[4]==null?0:Integer.parseInt(obj[4].toString()));
				voucherDTO.setRedeemPoints(obj[5]==null?0:Integer.parseInt(obj[5].toString()));
				voucherDTO.setOrderStatus(obj[6]==null?null:(obj[6].toString()));
				voucherDTO.setExpiryDate((Date)(obj[7]));
				orderList.add(voucherDTO);			
			}	
			transaction.commit();
			
		}
		catch (HibernateException e) {
			if(transaction!=null)
				transaction.rollback();
			logger.error("Exception occured on hibernate ",e);
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally{
			if(session != null)
				session.close();
			transaction = null;
		//	criteria = null;
		}
		return orderList ;
	}
	public ArrayList<VoucherOrderDetailsDTO> getOrderLists(String tableName,
			String langID, Date previousDate, int offset, int limit) {

		//	SimpleDateFormat sdf = new SimpleDateFormat("MM");
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			ArrayList<VoucherOrderDetailsDTO> orderList= null;
			Session session = null;
			Transaction transaction = null;
			VoucherOrderDetailsDTO voucherDTO=null;
			String sql=null;
			try{
				session = HiberanteUtil.getSessionFactory().openSession();
				transaction = session.beginTransaction();
				sql="SELECT VD.ORDER_ID,VD.ORDER_DATE,VD.VOUCHER_NUMBER,PD.PACKAGE_NAME,VD.QUANTITY,VD.REDEEM_POINTS,VD.ORDER_STATUS,PD.EXPIRY_DATE,PD.LANGUAGE_ID "+ 
				"from "+tableName+" VD ,PACKAGE_DETAILS PD WHERE VD.VOUCHER_NUMBER=PD.PACKAGE_ID AND PD.LANGUAGE_ID=? AND VD.ORDER_DATE >= ? ORDER BY VD.ORDER_DATE DESC";
				//" STR_TO_DATE(date_format(ORDER_DATE,'%d-%m-%Y'),'%d-%m-%Y') >= str_to_date(?,'%d-%m-%Y')";
				/*"STR_TO_DATE(date_format(ORDER_DATE,'%d-%m-%Y'),'%d-%m-%Y') Between STR_TO_DATE(?,'%d-%m-%Y') "+
				"AND STR_TO_DATE(?,'%d-%m-%Y')";
				*/
				//"ORDER_DATE Between ? "+
			//	"AND ?";
				
				logger.info("Query:>>"+sql);
				Query query=session.createSQLQuery(sql);
				query.setParameter(0,langID);
				query.setParameter(1,(Date)previousDate);
				query.setFirstResult(offset);
				query.setMaxResults(limit);
				logger.info("LANG ID"+langID);
				logger.info("PREVIOUS DATE"+(Date)previousDate);
				List<Object[]> list=query.list();
				orderList= new ArrayList<VoucherOrderDetailsDTO>();
				for(Object[] obj:list)
				{
					voucherDTO= new VoucherOrderDetailsDTO();
					voucherDTO.setOrderId((String) (obj[0]==null?0:(obj[0].toString())));
					voucherDTO.setOrderDate((Date)(obj[1]));
					voucherDTO.setVoucherNumber(obj[2]==null?null:obj[2].toString());
					voucherDTO.setVoucherName(obj[3]==null?null:obj[3].toString());
					voucherDTO.setQuantity(obj[4]==null?0:Integer.parseInt(obj[4].toString()));
					voucherDTO.setRedeemPoints(obj[5]==null?0:Integer.parseInt(obj[5].toString()));
					voucherDTO.setOrderStatus(obj[6]==null?null:(obj[6].toString()));
					voucherDTO.setExpiryDate((Date)(obj[7]));
					orderList.add(voucherDTO);			
				}	
				transaction.commit();
			}catch (HibernateException e) {
				if(transaction!=null)
					transaction.rollback();
				logger.error("Exception occured on hibernate ",e);
			} finally{
				if(session != null)
					session.close();
				transaction = null;
			//	criteria = null;
				sdf = null;
			}
			return orderList ;
		
		}
	public ArrayList<VoucherOrderDetailsDTO> getLastNOrderLists(
			String tableName, String langID, int noOfLastTransactions,
			int offset) {
		Session session = null;
		Transaction transaction = null;
		ArrayList<VoucherOrderDetailsDTO> orderList= null;
		VoucherOrderDetailsDTO voucherDTO=null;
		String sql=null;
		
		try{
			orderList= new ArrayList<VoucherOrderDetailsDTO>();
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			sql="SELECT VD.ORDER_ID,VD.ORDER_DATE,VD.VOUCHER_NUMBER,PD.PACKAGE_NAME,VD.QUANTITY,VD.REDEEM_POINTS,VD.ORDER_STATUS,PD.EXPIRY_DATE "+
			"from "+tableName+" VD, PACKAGE_DETAILS PD WHERE VD.VOUCHER_NUMBER=PD.PACKAGE_ID AND PD.LANGUAGE_ID=? ORDER BY VD.ORDER_DATE DESC";
			
			logger.info("Query:>>"+sql);
			
			Query query=session.createSQLQuery(sql);
			query.setParameter(0,langID);
			query.setFirstResult(offset);
			query.setMaxResults(noOfLastTransactions);
			
			logger.info("LANG ID"+langID);
			logger.info("NO OF TRANS"+noOfLastTransactions);
			
			List<Object[]> list=query.list();
			orderList= new ArrayList<VoucherOrderDetailsDTO>();
			for(Object[] obj:list)
			{			
				voucherDTO=new VoucherOrderDetailsDTO();
				voucherDTO.setOrderId((String) (obj[0]==null?0:(obj[0].toString())));
				voucherDTO.setOrderDate((Date)(obj[1]));
				voucherDTO.setVoucherNumber(obj[2]==null?null:obj[2].toString());
				voucherDTO.setVoucherName(obj[3]==null?null:obj[3].toString());
				voucherDTO.setQuantity(obj[4]==null?0:Integer.parseInt(obj[4].toString()));
				voucherDTO.setRedeemPoints(obj[5]==null?0:Integer.parseInt(obj[5].toString()));
				voucherDTO.setOrderStatus(obj[6]==null?null:(obj[6].toString()));
				voucherDTO.setExpiryDate((Date)(obj[7]));
				orderList.add(voucherDTO);			
			}	
			transaction.commit();
		}catch (HibernateException e) {
			if(transaction != null)
				transaction.rollback();
			logger.error("Exception occured on hibernate ",e);
			
		}catch (Exception e) {
			logger.error("Exception occured on hibernate ",e);
			
		}finally{
			if(session != null)
				session.close();
			transaction = null;
			session = null;
		}
		return orderList;
	}
}
