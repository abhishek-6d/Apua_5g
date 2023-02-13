package com.sixdee.imp.common.util;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.sixdee.imp.common.config.Cache;


public class ProfileDBHiberanteUtil 
{
	private static Logger logger = Logger.getLogger(ProfileDBHiberanteUtil.class);
	private static final SessionFactory sessionfactory = getbuildSessionFactory();
	
	private static SessionFactory getbuildSessionFactory()
	{
		try
		{
			
			String configFile = null;
			//logger.info(Cache.getCacheMap().get("DB_TYPE"));
			if(Cache.getCacheMap().get("PROFILE_DB_CALL")!=null&&Cache.getCacheMap().get("PROFILE_DB_CALL").trim().equalsIgnoreCase("true"))
			{
				Configuration configuration=new Configuration();
				configFile = "ProfileDB_hibernate.cfg.xml";
				logger.info("******************************* Profile DB is loading ... .... ******************************* ");
				return configuration.configure(configFile).buildSessionFactory();	
			}
		 	
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	public static SessionFactory getSessionFactory()
	{
		return sessionfactory;
	}
}