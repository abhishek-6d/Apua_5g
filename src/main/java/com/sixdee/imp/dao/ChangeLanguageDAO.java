package com.sixdee.imp.dao;

/**
 * 
 * @author Nithin Kunjappan
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
 * <td>September 05,2013 07:34:59 PM</td>
 * <td>Nithin Kunjappan</td>
 * </tr>
 * </table>
 * </p>
 */



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.vo.ChangeLanguageVO;
import com.sixdee.util.ConnectionPool5;

public class ChangeLanguageDAO {

	private static final Logger logger = Logger.getLogger(ChangeLanguageDAO.class);

	public boolean updateLanguageID(ChangeLanguageVO changeLangDto) 
	{
		TableInfoDAO tableInfoDAO = new TableInfoDAO();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		boolean flag=false;
		try {
			
			String subscriberCountryCode=Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue();
			Integer subscriberSize=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NUMBER_LENGTH").getParameterValue());
			
			if(changeLangDto.getSubscriberNumber()!=null&&changeLangDto.getSubscriberNumber().length()==subscriberSize)
				changeLangDto.setSubscriberNumber(subscriberCountryCode+changeLangDto.getSubscriberNumber());
			
			conn = ConnectionPool5.getConnection();
			
			sql = "UPDATE "+tableInfoDAO.getLanguagePreTableName(changeLangDto.getSubscriberNumber())+" SET FIELD61="+changeLangDto.getNewLanguageID()+" WHERE FIELD2='"+changeLangDto.getSubscriberNumber()+"' and FIELD61="+changeLangDto.getOldLanguageID()+"";
			
			pstmt = conn.prepareStatement(sql);
			//pstmt.setString(1, changeLangDto.getSubscriberNumber());
			logger.info("SQL::" + sql);
			int x = pstmt.executeUpdate();
			if (x>0){
				flag=true;
				logger.info("Succesfully Updated in Table ::"+tableInfoDAO.getLanguagePreTableName(changeLangDto.getSubscriberNumber()));
								
			}else{
				
				sql = "UPDATE "+tableInfoDAO.getLanguagePostTableName(changeLangDto.getSubscriberNumber())+" SET LANGUAGE_ID="+changeLangDto.getNewLanguageID()+" WHERE MSISDN='"+changeLangDto.getSubscriberNumber()+"' and LANGUAGE_ID="+changeLangDto.getOldLanguageID()+"";
				logger.info(sql);
				pstmt = conn.prepareStatement(sql);
				int y = pstmt.executeUpdate();
				if(y>0)
				{
					flag=true;
					logger.info("Succesfully Updated in Table ::"+tableInfoDAO.getLanguagePostTableName(changeLangDto.getSubscriberNumber()));
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
				}
			} catch (SQLException e) {
			}
		}
		
		/*if(langID==null||langID.trim().equals(""))
		{
			langID=Cache.defaultLanguageID;
			logger.info("Subscriber Number Not Found,Sending Default Language : MDN "+msisdn+" Language : "+langID);
		}*/
		
		return flag;
		
		
	}//getLanguageID
	
	
	 
	
	
}
