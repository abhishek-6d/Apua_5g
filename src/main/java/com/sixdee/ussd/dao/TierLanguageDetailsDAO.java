/**
 * 
 */
package com.sixdee.ussd.dao	;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.ussd.dto.TierLanguageMappingDTO;

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
public class TierLanguageDetailsDAO {
	
	
	private static Logger logger = Logger.getLogger(TierLanguageDetailsDAO.class);

	public HashMap<String, TierLanguageMappingDTO> getTierLanguageDetails() {



		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		HashMap<String, TierLanguageMappingDTO> tierLangMap = new HashMap<String, TierLanguageMappingDTO>();
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			criteria = session.createCriteria(TierLanguageMappingDTO.class);
			for(TierLanguageMappingDTO tier : (List<TierLanguageMappingDTO>)criteria.list()){
				tierLangMap.put(tier.getTierId()+"_"+tier.getLangId(), tier);
			}
			
		}catch (HibernateException e) {
			logger.error("Exception in loading cache",e);
		}catch (Exception e) {
			logger.error("Exception in loading cache",e);
			
		}
		finally{
			if(session != null){
				session.close();
				session = null;
			}
		}
		return tierLangMap;



	}

}
