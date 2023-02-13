package com.sixdee.imp.dao;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.SubscriberTransactionHistoryDTO;

/**
 * 
 * @author Somesh
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
 * <td>April 23,2013 09:49:31 PM</td>
 * <td>Somesh</td>
 * </tr>
 * </table>
 * </p>
 */




public class ActivatePackDAO 
{
	Logger logger = Logger.getLogger(ActivatePackDAO.class);
	private TableInfoDAO tableInfoDAO = new TableInfoDAO();
	public boolean insertTransactionHistory(SubscriberTransactionHistoryDTO dto)
	{
		Session session = null;
		boolean flag;
		try
		{
			String entity = tableInfoDAO.getSubscriberTransactionHistoryTable(dto.getSubscriberNumber()+"");
			session = HiberanteUtil.getSessionFactory().openSession();
			session.beginTransaction();
			
			logger.info("Insert inot transaction history table "+ dto.getSubscriberNumber());
			session.save(entity,dto);
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
