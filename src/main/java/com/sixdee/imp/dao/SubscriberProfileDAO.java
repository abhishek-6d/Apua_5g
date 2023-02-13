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
 * <td>May 14,2013 06:02:42 PM</td>
 * <td>Nithin Kunjappan</td>
 * </tr>
 * </table>
 * </p>
 */



import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.ActionServiceDetailsDTO;
import com.sixdee.imp.dto.SubscriberFailureDetailsDTO;
import com.sixdee.imp.dto.SubscriberProfileDTO;

public class SubscriberProfileDAO {
	
	private static final Logger logger = Logger.getLogger(ActionServiceDAO.class);
	Date todayDate=new Date();
	DateFormat dateformat=new SimpleDateFormat("ddMMyyyyHHmmss");	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
	
	private TableInfoDAO tableInfoDAO = new TableInfoDAO();
	
	public Object[] getSubscriberProfileDetails(String tableName,int serviceID,String subscriberNumber)
	{
		Session session=null;	
		//boolean flag=false;
		Transaction tx = null;
		Object[] createDate = null;
		int counter = 0;
		try{
			
			session=HiberanteUtil.getSessionFactory().openSession();
			tx=session.beginTransaction();
			long start=System.currentTimeMillis();
		//	logger.info("GOING TO CHECK SUBSCRIBER PROFILE TABLE#############"+System.currentTimeMillis());
			String sql="SELECT (START_DATE),COUNTER FROM "+tableName+" WHERE SERVICE_ID=:serviceID AND SUBSCRIBER_NUMBER=:subsNum ";			
			Query query=session.createSQLQuery(sql);
			query.setParameter("serviceID", serviceID);
			query.setParameter("subsNum", subscriberNumber);
			
			List<Object[]> list=query.list();
			for(Object[] subsInfo : list){
				createDate = new Object[2];
				createDate[0] = subsInfo[0].toString();
				createDate[1] = Integer.parseInt(subsInfo[1].toString());
			}
			
		//	logger.info("GOT DETAILS FROM SUBSCRIBER PROFILE TABLE##############"+System.currentTimeMillis());
			long finaltime=System.currentTimeMillis()-start;
			logger.info("getSubscriberProfileDetails#######################"+finaltime);
			
		
			
			tx.commit();			
			
		}catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}finally{
			try{
				if(session!=null)
				{
					session.close();
					session=null;
				}
			}catch (Exception e) {}
			/*if(!flag){
				flag = isNewPromo(tableName,serviceID,subscriberNumber,createDate);
				if(flag){
					updatePromosToExpiry(counter,serviceID,subscriberNumber);
				}
			}*/
		}
	
		return createDate;
	}//getLoyaltyRegister

	
	private void updatePromosToExpiry(int counter, int serviceID, String subscriberNumber) {

		Session session = null;
		boolean flag;
		String sql=null;
		Transaction tx = null;
		try
		{		
			long start=System.currentTimeMillis();
			String table = tableInfoDAO.getSubscriberProfileDBTable(subscriberNumber);
			session = HiberanteUtil.getSessionFactory().openSession();
			tx=session.beginTransaction();
			sql="UPDATE "+table+" SET STATUS = 2,COUNTER=COUNTER+1 where SUBSCRIBER_NUMBER=? and SERVICE_ID = ? and COUNTER = ?";
			Query query = session.createSQLQuery(sql);
			query.setParameter(0, subscriberNumber);
			query.setParameter(1, serviceID);
			query.setParameter(2, counter);
			query.executeUpdate();
			tx.commit();
			//session.flush();			
			long finaltime=System.currentTimeMillis()-start;
			//logger.info("addSubscriberDetails()#######################"+finaltime);
			flag =  true;		
		}
		catch (HibernateException e) 
		{
			flag = false;
			tx.rollback();
			e.printStackTrace();
		}
		finally
		{
			if(session!=null)
			{
				session.close();
				session = null;
			}
		}
		
	//	return flag;
	
	}


	public boolean isNewPromo(String tableName, int serviceID,
			String subscriberNumber, String createDate) {
		TableDetailsDAO tableDetailsDAO = null;
		ActionServiceDetailsDTO actionServiceDetailsDTO = null;
		
		boolean isNewPromo = false;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:SS");
		Date endPeriod = null;
		int validityPeriod = 0;
		Date startDate = null;
		Date curDate = null;
		Calendar cal = null;
		try{
			cal = Calendar.getInstance();
			curDate = cal.getTime();
			tableDetailsDAO = new TableDetailsDAO();
			actionServiceDetailsDTO =getServiceDetails(serviceID);
			if (actionServiceDetailsDTO.getValidityType() != null
					&& !(actionServiceDetailsDTO.getValidityType().trim()
							.equals(""))) {
				if (actionServiceDetailsDTO.getValidityType().equalsIgnoreCase(
						"DAYS"))
					validityPeriod = actionServiceDetailsDTO.getValidity() * 24;
				else
					validityPeriod = actionServiceDetailsDTO.getValidity();
				try {
					startDate = sdf.parse(createDate);
				} catch (Exception e) {
					createDate = createDate + " 00:00:00";
					startDate = sdf.parse(createDate);
				}
				cal.setTime(startDate);
				cal.add(Calendar.HOUR, validityPeriod);

				endPeriod = cal.getTime();

				if (logger.isDebugEnabled()) {
					logger.debug("Current Date " + curDate + " End Time "
							+ endPeriod);
				}
				if (curDate.compareTo(endPeriod) > 0) {
					isNewPromo = true;
				}
				
			}
		}catch (Exception e) {
			logger.error("Exception occured ",e);
		}finally{
			cal = null;
			startDate = null;
			endPeriod = null;
			curDate = null;
			tableDetailsDAO = null;
			actionServiceDetailsDTO = null;
		}
		return isNewPromo;
	}

	public boolean addSubscriberDetails(String msisdn,int serviceId,String serviceName,String ruleName,
			Date startDate,Date endDate) {
		
		Session session = null;
		Transaction tx = null;
		boolean flag;
		String sql=null;
		SubscriberProfileDTO subscriberProfileDTO = null;
		try
		{		
			long start=System.currentTimeMillis();
			
			String table = tableInfoDAO.getSubscriberProfileEntity(msisdn);
			session = HiberanteUtil.getSessionFactory().openSession();
			tx=session.beginTransaction();
			//session = HiberanteUtil.getSessionFactory().openSession();
			//session.beginTransaction();
			//sql="INSERT INTO "+table+"(SUBSCRIBER_NUMBER,SERVICE_ID,SERVICE_NAME,RULE_NAME,START_DATE,END_DATE) values (?,?,?,?,?,?)";
			subscriberProfileDTO = new SubscriberProfileDTO();
			subscriberProfileDTO.setSubscriberNumber(Long.parseLong(msisdn));
			subscriberProfileDTO.setServiceId(serviceId);
			subscriberProfileDTO.setServiceName(serviceName);
			subscriberProfileDTO.setRuleName(ruleName);
			subscriberProfileDTO.setStartDate(startDate);
			subscriberProfileDTO.setEndDate(endDate);
			/*Query query = session.createSQLQuery(sql);
			query.setParameter(0,msisdn);
			query.setParameter(1,serviceId);
			query.setParameter(2,serviceName);
			query.setParameter(3,ruleName);
			query.setParameter(4,startDate);
			query.setParameter(5,endDate);
			//query.setParameter(6, "R");
			query.executeUpdate();
			tx.commit();
			//session.flush();			
			*/
			session.save(table,subscriberProfileDTO);
			tx.commit();
			long finaltime=System.currentTimeMillis()-start;
			logger.info("addSubscriberDetails()#######################"+finaltime);
			flag =  true;		
		}
		catch (HibernateException e) 
		{
			flag = false;
			if(tx!= null)
				tx.rollback();
			e.printStackTrace();
		}
		finally
		{
			if(session!=null)
			{
				session.close();
				session = null;
			}
			tx = null;
			endDate = null;
			msisdn = null;
			ruleName = null;
			serviceName = null;
			sql = null;
			startDate = null;
			
		}
	
		
		return flag;
	}

	public boolean addFailureDetails(String requestId,String msisdn,String serviceName,String ruleName, String failureDesc) {
		Session session = null;
		SubscriberFailureDetailsDTO failureDTO=null;
		Transaction tx = null;
		boolean flag=false;
		try
		{
			failureDTO=new SubscriberFailureDetailsDTO();
			session = HiberanteUtil.getSessionFactory().openSession();
			tx=session.beginTransaction();
			if(requestId!=null)
				failureDTO.setTransactionID(requestId);			
			else
				failureDTO.setTransactionID(dateformat.format(todayDate));
			
			if(msisdn!=null)
				failureDTO.setSubscriberNumber(msisdn);	
			
			if(serviceName!=null)
				failureDTO.setServiceName(serviceName);
			
			if(ruleName!=null)
				failureDTO.setRuleName(ruleName);
			
			failureDTO.setFailureDesc(failureDesc);
			failureDTO.setFailureDate(todayDate);
			session.save(failureDTO);
			tx.commit();	
			flag=true;
		}
		catch (Exception e) 
		{
			flag=false;
			if(tx != null)
				tx.rollback();
			e.printStackTrace();
		}
		finally
		{
			if(session!=null)
			{
				session.close();
				session = null;
			}
			failureDTO = null;
			
		}
		return flag;
	}	

	private ActionServiceDetailsDTO getServiceDetails(Integer serviceId)
	{
		Session session=null;
		ActionServiceDetailsDTO actionServiceDetailsDTO=null;
		Transaction trx = null;
		try{
			
			session=HiberanteUtil.getSessionFactory().openSession();
			trx = session.beginTransaction();
			
			String sql="SELECT SERVICE_ID,SERVICE_NAME,ACTION_NAME,ACTION_TYPE,NO_OF_TIMES,VALIDITY,VALIDITY_TYPE,START_TIME,END_TIME," +
					   "IS_EVERY_DAY,ISREPEAT,PARENT_ID,START_DATE,END_DATE FROM ACTION_SERVICE_DETAILS WHERE  SERVICE_ID=? ";
			 
			Query query=session.createSQLQuery(sql)
						.setParameter(0,serviceId);
			
			List<Object[]> list=query.list();
			
			if(list.size()>0)
			{
				Object[] obj=list.get(0);
				
				actionServiceDetailsDTO=new ActionServiceDetailsDTO();
				
				actionServiceDetailsDTO.setServiceID(Integer.parseInt(obj[0].toString()));
				actionServiceDetailsDTO.setServiceName((String)obj[1]);
				actionServiceDetailsDTO.setActionName((String)obj[2]);
				actionServiceDetailsDTO.setActionType(Integer.parseInt(obj[3].toString()));
				actionServiceDetailsDTO.setNoOfTimes(Integer.parseInt(obj[4].toString()));
				actionServiceDetailsDTO.setValidity(obj[5]==null?null:Integer.parseInt(obj[5].toString()));
				actionServiceDetailsDTO.setValidityType((String)obj[6]);
				actionServiceDetailsDTO.setStartTime((String)obj[7]);
				actionServiceDetailsDTO.setEndTime((String)obj[8]);
				actionServiceDetailsDTO.setEveryDay((String)obj[9]);
				actionServiceDetailsDTO.setRepeat(""+(Character)obj[10]);
				actionServiceDetailsDTO.setParentID(obj[11]==null?null:Integer.parseInt(obj[11].toString()));
				actionServiceDetailsDTO.setStartDate((Date)obj[12]);
				actionServiceDetailsDTO.setEndDate((Date)obj[13]);
				
			}
			
			trx.commit();
			
		}catch (Exception e) {
			trx.rollback();
			e.printStackTrace();
		}finally{
			if(session!=null)
				session.close();
			session=null;
		}
		
		return actionServiceDetailsDTO;
		
	}//getServiceDetails

	public static void main(String[] args) throws ParseException {
		Date d = new SimpleDateFormat("dd-MM-yy HH:mm:SS").parse("2013-05-20 10:11:12");
		Date d1 = new SimpleDateFormat("dd-MM-yy HH:mm:SS").parse("2013-05-20 10:11:13");
		Calendar cal = Calendar.getInstance();
		int hour = 365*24;
		System.out.println(cal.getTime());
		
		cal.add(Calendar.HOUR, hour);
		System.out.println(cal.getTime());
		System.out.println(hour);
		System.out.println(d1.compareTo(d));
	//	cal.add(Calendar.HOUR, amount)
	}


	public boolean updateSubscriberDetails(String subscriberNumber,int serviceId, Integer counter) {

		Session session = null;
		boolean flag;
		String sql=null;
		Transaction tx = null;
		try
		{		
			long start=System.currentTimeMillis();
			String table = tableInfoDAO.getSubscriberProfileDBTable(subscriberNumber);
			session = HiberanteUtil.getSessionFactory().openSession();
			tx=session.beginTransaction();
			//session = HiberanteUtil.getSessionFactory().openSession();
			//session.beginTransaction();
			sql="UPDATE "+table+" SET STATUS = ? ,START_DATE= ?,COUNTER=COUNTER+1 where SUBSCRIBER_NUMBER=? and SERVICE_ID = ? and COUNTER=?";
			Query query = session.createSQLQuery(sql);
			query.setParameter(0,"R");
			query.setParameter(1,new Date());
			//query.setParameter(3, arg1)
			//query.setParameter(2,subscriberProfDTO.getServiceName());
			query.setParameter(2,subscriberNumber);
			
			query.setParameter(3,serviceId);
			query.setParameter(4, counter);
			//query.setParameter(6, "R");
			int i = query.executeUpdate();
			tx.commit();
			//session.flush();			
			long finaltime=System.currentTimeMillis()-start;
			//logger.info("addSubscriberDetails()#######################"+finaltime);
			if(i==0)
				flag = false;
			else
				flag =  true;		
		}
		catch (HibernateException e) 
		{
			flag = false;
			tx.rollback();
			e.printStackTrace();
		}
		finally
		{
			if(session!=null)
			{
				session.close();
				session = null;
			}
		}
		
		return flag;
	
	}
}
