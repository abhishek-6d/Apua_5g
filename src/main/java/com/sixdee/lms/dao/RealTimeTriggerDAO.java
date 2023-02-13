/**
 * 
 */
package com.sixdee.lms.dao;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.common.util.HiberanteUtilRule;
import com.sixdee.lms.dto.persistent.ExtNotificationDTO;

/**
 * @author rahul.kr
 *
 */
public class RealTimeTriggerDAO {
	
	private static final Logger logger = Logger.getLogger("RealTimeTriggerDAO");

	public ExtNotificationDTO getNotificationProperties(String keyword){
		Session session = null;
		ExtNotificationDTO extNotificationDTO = null;
		try{
			session=HiberanteUtil.getSessionFactory().openSession();
			extNotificationDTO = (ExtNotificationDTO) session.get(ExtNotificationDTO.class, keyword);
			
		}catch (Exception e) {
			logger.error("Exception occured ",e);
		}finally{
			if(session != null){
				session.close();
			}
		}
		return extNotificationDTO;
	}
	
	public Object getNotificationProperties(Class className,String key){
		Session session = null;
		Object response = null;
		try{
			session=HiberanteUtil.getSessionFactory().openSession();
			response = (ExtNotificationDTO) session.get(className, key);
			
		}catch (Exception e) {
			logger.error("Exception occured ",e);
		}finally{
			if(session != null){
				session.close();
			}
		}
		return response;
	}
	
	
	public Object getNotificationPropertiesRule(Class className,String key){
		Session session = null;
		Object response = null;
		try{
			session=HiberanteUtilRule.getSessionFactory().openSession();
			response = session.get(className, key);
			
		}catch (Exception e) {
			logger.error("Exception occured ",e);
		}finally{
			if(session != null){
				session.close();
			}
		}
		return response;
	}
}
