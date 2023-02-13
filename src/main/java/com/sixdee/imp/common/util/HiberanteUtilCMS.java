package com.sixdee.imp.common.util;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.sixdee.imp.common.config.Cache;


public class HiberanteUtilCMS 
{
	private static Logger logger = Logger.getLogger(HiberanteUtilCMS.class);
	private static final SessionFactory sessionfactory = getbuildSessionFactory();
	
	private static SessionFactory getbuildSessionFactory()
	{
		try
		{
			Configuration configuration=new Configuration();
			String configFile = null;
			//logger.info(Cache.getCacheMap().get("DB_TYPE"));
			if(Cache.getCacheMap().get("DB_TYPE")!=null&&Cache.getCacheMap().get("DB_TYPE").trim().equalsIgnoreCase("ORACLE"))
			{
				configFile = "hibernate_CMS.cfg.xml";
				logger.info("******************************* Oracle DB is loading ... .... ******************************* ");
				
			}
			else
			{
				configFile = "Mysql_hibernate_CMS.cfg.xml";
				logger.info("*******************************  Mysql DB is loading ... .... ******************************* ");
			}
			
		 	return configuration.configure(configFile).buildSessionFactory();
		 	
		}catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		//return sessionfactory;
	}
	public static SessionFactory getSessionFactory()
	{
		return sessionfactory;
	}
}