/**
 * 
 */
package com.sixdee.ussd.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.ussd.dto.MessageTemplateDTO;

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
public class MessageTemplateDAO {

	private static Logger logger  = Logger.getLogger(MessageTemplateDAO.class);
	public HashMap<String, MessageTemplateDTO> getMessageTemplates(){
		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		List<MessageTemplateDTO> templateList = null;
		HashMap<String, MessageTemplateDTO> templateMap = new HashMap<String, MessageTemplateDTO>();
		
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			criteria = session.createCriteria(MessageTemplateDTO.class);
			templateList = criteria.list();
			for(MessageTemplateDTO messageTemplateDTO:templateList){
				templateMap.put(messageTemplateDTO.getTemplateId()+"_"+messageTemplateDTO.getServiceId()+"_"+messageTemplateDTO.getLangId(), messageTemplateDTO);
			}
		}catch (Exception e) {
			logger.error("Exception occured at ",e);
		}finally{
			if(session != null){
				session.close();
				session = null;
			}
			templateList = null;
			criteria = null;
			transaction = null;
		}
		return templateMap;
	}
	
}
