package com.sixdee.imp.common.config;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.sixdee.imp.common.dao.StartupLoggerDAO;
import com.sixdee.imp.dto.startup.StartupLoggerDTO;
import com.sixdee.imp.threadpool.ThreadInitiator;
import com.sixdee.imp.threadpool.ThreadRollBack;
import com.sixdee.imp.ver.Version;

public class ContextLoaderServlet extends HttpServlet {
	
	
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	protected final Logger		log					= Logger.getLogger(getClass().getName());

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		try {
			
			new Version();
			Globals.loadProperties();
			ThreadInitiator.getInstance().initiateThreads();
			Globals.cacheMap();
			Globals.reloadCache();
			startupAudit();
			log.debug("CRM_FAILED_RETRY_STATUS    :::    " + Cache.getCacheMap().get("CRM_FAILED_RETRY_ENABLED"));
		} catch (Exception e) {
			log.fatal("Unexpected error loading configs:", e);
			this.destroy();
			throw new ServletException();
		}

	}

	protected void startupAudit() {

		StartupLoggerDTO startupLogger = null;
		StartupLoggerDAO startupLoggerDAO = null;
		SimpleDateFormat sdf = null;
		try {
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			startupLogger = new StartupLoggerDTO();
			startupLogger.setAppId(Cache.cacheMap.get("APP_ID"));
			startupLogger.setInstanceId(Cache.cacheMap.get("INSTANCE_ID"));
			startupLogger.setServerId(Cache.cacheMap.get("SERVER_ID"));
			startupLogger.setStartTime(sdf.format(new Date()));
			startupLoggerDAO = new StartupLoggerDAO();
			startupLoggerDAO.auditStartup(startupLogger);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			startupLogger = null;
			startupLoggerDAO = null;
			sdf = null;
		}
	}

	@Override
	public void destroy() {

		// TODO :please write method to kill ThreadInitiator.getInstance().initiateThreads()
	}
}
