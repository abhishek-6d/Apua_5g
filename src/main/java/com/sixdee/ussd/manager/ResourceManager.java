/**
 * 
 */
package com.sixdee.ussd.manager;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.sixdee.ussd.util.AppCache;
import com.sixdee.ussd.ver.Version;

/**
 * @author Rahul K K
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
 * <td>April 24, 2013</td>
 * <td>Rahul K K</td>
 * <td>Created this class</td>
 * </tr>
 * </table>
 * </p>
 */
public class ResourceManager extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		execute(req,resp);
	}

	private void execute(HttpServletRequest req, HttpServletResponse resp) {
		new Version();
		String request = req.getQueryString();
		String reqType = null;
		String reqMode = null;
		StringTokenizer stk = new StringTokenizer("=");
		if(request != null){
			reqType = stk.nextToken();
			reqMode = stk.hasMoreTokens()?stk.nextToken():"1";
		}
		if(reqType.equalsIgnoreCase("ReloadCache")){
			AppCache.reinitializeCache();
		}else if(reqType.equalsIgnoreCase("LogLevelChange")){
			int level = 1;
			Logger.getRootLogger().setLevel(Level.DEBUG);
		}else if(reqType.equalsIgnoreCase("RM")){
			AppCache app = new AppCache();
			app.reInitPackageTree();
			app = null;
		}
		
	}
}
