package com.sixdee.imp.dao;

/**
 * 
 * @author Rahul
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
 * <td>March 06,2015 11:46:47 AM</td>
 * <td>Rahul</td>
 * </tr>
 * </table>
 * </p>
 */



import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.util.HiberanteUtilCMS;
import com.sixdee.imp.dto.GetOfferCodeOnMarketingPlanDTO;
import com.sixdee.imp.dto.MarketingPlanDTO;

public class GetOfferCodeOnMarketingPlanDAO {

	private static final Logger logger = Logger.getLogger(GetOfferCodeOnMarketingPlanDAO.class);
	public HashMap<String, MarketingPlanDTO> getOfferCodes(
			String loggingString,ArrayList<Integer> marketingPlanList) {
		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		HashMap<String, MarketingPlanDTO> marketingPlanMap = new HashMap<String, MarketingPlanDTO>();
		try{
			session = HiberanteUtilCMS.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			criteria = session.createCriteria(MarketingPlanDTO.class);
			criteria.add(Restrictions.in("marketingPlanId", marketingPlanList));
			for(MarketingPlanDTO marketingPlanDetails : (ArrayList<MarketingPlanDTO>)criteria.list()){
				logger.debug(loggingString+" "+marketingPlanDetails.getSmsKeyword()+ " DialCode "+marketingPlanDetails.getDialCode());
				marketingPlanMap.put(marketingPlanDetails.getMarketingPlanId()+"", marketingPlanDetails);
			}
			transaction.commit();
		}catch(Exception e){
			logger.error(loggingString+" Error occured ",e);
			if(transaction!=null){
				transaction.rollback();
			}
		}
		finally{
			if(session!=null){
				session.close();
			}

		}
		return marketingPlanMap;
	}


}
