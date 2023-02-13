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
 * <td>May 27,2013 01:17:51 PM</td>
 * <td>Rahul K K</td>
 * </tr>
 * </table>
 * </p>
 */



import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.FailureActionDTO;

public class FailureActionDAO {

	private static final Logger logger = Logger.getLogger(FailureActionDAO.class);
	
	public void updateFailureSubscriber(String tableName,int serviceId, FailureActionDTO failureActionDTO) throws CommonException {
		
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		String updateParam = null;
	
		try{
			updateParam = getSubscriberProfileType(tableName,serviceId,failureActionDTO.getSubscriberNumber());
		
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction =session.beginTransaction();
			query =  session.createSQLQuery("update "+tableName+" set "+updateParam+"" +
					" and SUBSCRIBER_NUMBER=? and SERVICE_ID=?").setParameter(0, failureActionDTO.getSubscriberNumber()).setParameter(1, serviceId);
			logger.info("SQl Executed "+query.getQueryString()+" "+failureActionDTO.getSubscriberNumber());
			int updateCount = query.executeUpdate();
			transaction.commit();
			if(updateCount==0){
				throw new CommonException("No Count for "+failureActionDTO.getSubscriberNumber()+" for "+serviceId);
			}
			
		}catch (HibernateException e) {
			if(transaction!=null){
				transaction.rollback();
			}
			logger.error("Exception occured",e);
		}finally{
			if(session != null)
				session.close();
		}
	}
	private String getSubscriberProfileType(String tableName, int serviceId,
			String subscriberNumber) throws CommonException {
		Session session = null;
		Transaction transaction = null;
		SQLQuery query = null;
		String updateParam = null;
		String counter = null;
		try{
			updateParam = updateParam+"-1";
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction =session.beginTransaction();
			query = session.createSQLQuery("select DAILY_DATE,WEEKLY_DATE,MONTHLY_DATE,COUNTER from " +tableName+
					" where SUBSCRIBER_NUMBER="+subscriberNumber+" and SERVICE_ID="+serviceId+"");
			logger.info("SQl Executed "+query.getQueryString());
			for(Object[] obj : (List<Object[]>)query.list()){
				System.out.println(obj[0]+" "+obj[1]+" "+obj[2]+" "+obj[3] );
				if(obj[0]!=null){
					if(obj[0].toString().equals("0"))
						throw new CommonException("Daily Count is 0");
					updateParam="DAILY_COUNT=DAILY_COUNT-1 ,COUNTER=COUNTER+1 where DAILY_COUNT > 0 and COUNTER="+obj[3];
				}else if(obj[1]!=null){
					if(obj[1].toString().equals("0"))
						throw new CommonException("Weekly Count is 0");
					
					updateParam = "WEEKLY_COUNT=WEEKLY_COUNT-1,COUNTER=COUNTER+1 where WEEKLY_COUNT > 0 and COUNTER="+obj[3];
				}else if(obj[2] != null){
					if(obj[2].toString().equals("0"))
						throw new CommonException("Monthly Count is 0");
					
					updateParam = "MONTHLY_COUNT=MONTHLY_COUNT-1,COUNTER=COUNTER+1 where MONTHLY_COUNT > 0 and COUNTER="+obj[3];
				}
			}
			transaction.commit();
			if(updateParam==null)
				throw new CommonException("No Record for "+subscriberNumber+" for "+serviceId);
			
			
		}catch (HibernateException e) {
			if(transaction!=null){
				transaction.rollback();
			}
			logger.error("Exception occured",e);
		}finally{
			if(session!=null){
				session.close();
				session = null;
			}
		}
		return updateParam;
	}


}
