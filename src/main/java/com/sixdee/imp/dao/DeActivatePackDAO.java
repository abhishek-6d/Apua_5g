package com.sixdee.imp.dao;

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
 * <td>April 25,2013 06:34:23 PM</td>
 * <td>Somesh</td>
 * </tr>
 * </table>
 * </p>
 */



import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.SubscriberTransactionHistoryDTO;

public class DeActivatePackDAO 
{

	Logger logger = Logger.getLogger(DeActivatePackDAO.class);
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
