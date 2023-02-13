/**
 * 
 */
package com.sixdee.imp.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.SubscriberProfileDTO;

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
 * <td>May 10,2013 09:00:31 PM</td>
 * <td>Nithin Kunjappan</td>
 * </tr>
 * </table>
 * </p>
 */

public class ActionServiceDAO {
	
	private static final Logger logger = Logger.getLogger(ActionServiceDAO.class);
	private TableInfoDAO tableInfoDAO = new TableInfoDAO();
	public List<SubscriberProfileDTO> getSubscriberProfileDetails(String tableName,int serviceID)
	{
		Session session=null;
		Transaction transaction=null;
		List<SubscriberProfileDTO> subscriberDTO=null;
		try{
			
			session=HiberanteUtil.getSessionFactory().openSession();
			
			transaction=session.beginTransaction();
			
			String sql="FROM "+tableName+" TAB WHERE TAB.serviceId=:serviceID ";
			
			Query query=session.createQuery(sql);
			query.setParameter("serviceID",serviceID);
			
			subscriberDTO=query.list();

			transaction.commit();
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				if(session!=null)
				{
					session.close();
					session=null;
				}
			}catch (Exception e) {}
		}
		
		return subscriberDTO;
		
	}//getLoyaltyRegister

	public boolean insertSubscriberDetails(SubscriberProfileDTO subscriberProfDTO) {
		Session session = null;
		boolean flag;
		try
		{
			String table = tableInfoDAO.getSubscriberProfileTable(subscriberProfDTO.getSubscriberNumber()+"");
			session = HiberanteUtil.getSessionFactory().openSession();
			session.beginTransaction();
			
			logger.info("Inserted into Subscriber_wise_profile table "+ subscriberProfDTO.getSubscriberNumber());
			session.save(table,subscriberProfDTO);
			session.getTransaction().commit();		
			flag =  true;		
		}
		catch (Exception e) 
		{
			flag = false;
			session.getTransaction().rollback();
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
