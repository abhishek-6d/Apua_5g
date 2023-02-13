package com.sixdee.ussd.manager;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import com.sixdee.ussd.util.AppCache;
import com.sixdee.ussd.util.SetRootDir;

public class ManagerServlet extends HttpServlet {
 
	private static final Logger logger = Logger.getLogger(ManagerServlet.class);
	
	public void init() throws ServletException {
		logger.info("USSD BROWSER LOADING");
		ServletConfig config = getServletConfig();
		SetRootDir.userdir=config.getServletContext().getRealPath("/WEB-INF/classes/");//+config.getInitParameter("USER_DIR");
		AppCache.initCache();
		
	}
	@Override
	public void destroy() {
		ThreadInitiator.setStartFlag(false);
        AppCache.killTimerThreads();
	}
}
