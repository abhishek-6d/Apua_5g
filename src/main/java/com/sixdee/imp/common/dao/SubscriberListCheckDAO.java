/**
 * 
 */
package com.sixdee.imp.common.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.HiberanteUtil;

/**
 * @author Nithin Kunjappan
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
 * <td>Nov 07, 2013</td>
 * <td>Nithin Kunjappan</td>
 * <td>Created this class</td>
 * </tr>
 * </table>
 * </p>
 */
public class SubscriberListCheckDAO {
	
	static Logger logger=Logger.getLogger(SubscriberListCheckDAO.class);
	
	public boolean checkSubscriber(String subNum)
	{
		boolean flag=false;
		Session session=null;
		Transaction transaction=null;
		
		try{
		
			session=HiberanteUtil.getSessionFactory().openSession();
			
			String subscriberCountryCode=Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue();
			Integer subscriberSize=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NUMBER_LENGTH").getParameterValue());

			if(subNum!=null&&subNum.length()==subscriberSize)
				subNum=subscriberCountryCode+subNum;
				
			String sql="SELECT MSISDN from SUBSCRIBER_WHITELIST where MSISDN=? " ;
			
			Query query=session.createSQLQuery(sql);
			
			query.setParameter(0,subNum);
			
			List<Object[]> list=query.list();
			
			if(list!=null&&list.size()>0)
			{
				flag=true;
			}
			
			
		}catch (Exception e) {
			logger.info("",e);
		}finally{
			if(session!=null)
				session.close();
			session=null;
		}
		logger.info("IS "+subNum+" WHITELISTED NUMBER ::"+flag);
		return flag;
	}//checkSubscriber

}


