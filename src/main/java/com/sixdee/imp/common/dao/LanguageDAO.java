/**
 * 
 */
package com.sixdee.imp.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dao.TableInfoDAO;
import com.sixdee.util.ConnectionPool5;

/**
 * 
 * @author Nithin Kunjappan
 * @version 1.0
 *          <p>
 *          <b><u>Development History</u></b><br>
 *          <table border="1" width="100%">
 *          <tr>
 *          <td width="15%"><b>Date</b></td>
 *          <td width="20%"><b>Author</b></td>
 *          </tr>
 *          <tr>
 *          <td>JUL 05, 2013</td>
 *          <td>Nithin Kunjappan</td>
 *          </tr>
 *          </table>
 *          </p>
 */
public class LanguageDAO {

	private static final Logger logger = Logger.getLogger(LanguageDAO.class);

	public String getLanguageID(String msisdn) 
	{
		TableInfoDAO tableInfoDAO = new TableInfoDAO();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String langID=null;
		try {
			
			String subscriberCountryCode=Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue();
			Integer subscriberSize=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NUMBER_LENGTH").getParameterValue());
			
			if(msisdn!=null&&msisdn.length()==subscriberSize)
				msisdn=subscriberCountryCode+msisdn;
			
			
			sql = "SELECT FIELD61 FROM "+tableInfoDAO.getLanguagePreTableName(msisdn)+" WHERE FIELD2=?";

			conn = ConnectionPool5.getConnection();
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, msisdn);
			logger.info("SQL::" + sql);
			rs = pstmt.executeQuery();

			if (rs.next()){
				langID=rs.getString(1);
				logger.info("Lang ID exists in PREPAID_CDR table::" + langID);
								
			}else{
				
				sql = "SELECT LANGUAGE_ID FROM "+tableInfoDAO.getLanguagePostTableName(msisdn)+ " WHERE MSISDN=?";
				logger.info(sql);
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, msisdn);
				
				rs = pstmt.executeQuery();
				if(rs.next())
				{
					langID = rs.getString("LANGUAGE_ID");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sql = null;
			tableInfoDAO=null;
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
			}
			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
			}
			try {
				if (conn != null) {
					conn.close();
					conn = null;//sajith k s changed
				}
			} catch (SQLException e) {
			}
		}
		
		if(langID==null||langID.trim().equals(""))
		{
			langID=Cache.defaultLanguageID;
			logger.info("Subscriber Number Not Found,Sending Default Language : MDN "+msisdn+" Language : "+langID);
		}
		
		return langID;
		
		
	}//getLanguageID

	
	 
	
	
}
