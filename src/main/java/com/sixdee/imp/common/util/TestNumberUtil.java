package com.sixdee.imp.common.util;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

public class TestNumberUtil 
{
	private static Logger log = Logger.getLogger(TestNumberUtil.class);
	public static int isTestNumber(String number)
	{
		int flag =0;
		Session session = null;
		Query query = null;
		List list = null;
		try
		{
			session = HiberanteUtil.getSessionFactory().openSession();
			query = session.createSQLQuery("SELECT SUBSCRIBER_NUMBER FROM TEST_NUMBERS WHERE SUBSCRIBER_NUMBER=?");
			query.setParameter(0, number);
			
			list = query.list();
			if(list.size()>0)
				flag = 1;
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally
		{
			if(session!=null)
				session.close();
			query = null;
			list = null;
		}
		log.info("Subscriber Number = "+number+" is  = "+flag);
		return flag;
	}
}
