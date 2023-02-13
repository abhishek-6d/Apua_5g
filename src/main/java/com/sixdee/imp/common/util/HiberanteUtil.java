package com.sixdee.imp.common.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.sixdee.imp.common.config.Cache;

public class HiberanteUtil {
	
	/** The single instance of hibernate SessionFactory */
	private static org.hibernate.SessionFactory	sessionFactory;

	private HiberanteUtil() {}

	static {
		// Annotation and XML
		// sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
		// XML only//
		try {
			if (Cache.getCacheMap().get("DB_TYPE") != null && Cache.getCacheMap().get("DB_TYPE").trim().equalsIgnoreCase("ORACLE")) {
				Cache.isOracleDB = true;
				Cache.isMySqlDB = false;
				sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
			} else {
				Cache.isMySqlDB = true;
				Cache.isOracleDB = false;
				sessionFactory = new Configuration().configure("Mysql_hibernate.cfg.xml").buildSessionFactory();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static SessionFactory getSessionFactory() {
		 
		return sessionFactory;
	}

	/**
	 * * Opens a session and will not bind it to a session context *
	 * 
	 * @return the session
	 */
	public Session openSession() {
		return sessionFactory.openSession();
	}

	/**
	 * * closes the session factory
	 */
	public static void close() {
		if (sessionFactory != null)
			sessionFactory.close();
		sessionFactory = null;
	}
}