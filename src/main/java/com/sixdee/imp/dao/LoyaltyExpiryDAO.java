package com.sixdee.imp.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.LoyaltyExpiryDTO;
import com.sixdee.imp.dto.LoyaltyTransactionTabDTO;
import com.sixdee.imp.threadpool.ThreadInitiator;

public class LoyaltyExpiryDAO 
{
	private Logger log = Logger.getLogger(LoyaltyExpiryDAO.class);
	private static final DateFormat df = new SimpleDateFormat(
	"dd/MM/yyyy HH:mm:ss");
	
	public void startExpiry()
	{    
		log.debug("Testing the teir expiry based on harcoded values");
		//Calendar cal[] = getQuaterDetails(0); //testing sajith ks
		//Calendar toDateofQuater = cal[1]; //testing sajith ks
		
		//test code ****start sajith ks
		Calendar toDateofQuater = Calendar.getInstance();
		
		toDateofQuater.set(Calendar.YEAR, 2016);
		toDateofQuater.set(Calendar.MONTH, 6);
		toDateofQuater.set(Calendar.DATE, 10);
		//test code ****end sajith ks
		
		Calendar today = Calendar.getInstance(); 
		//Test code ** start
		today.set(Calendar.YEAR, 2016);
		today.set(Calendar.MONTH, 6);
		today.set(Calendar.DATE, 10);
		//Test code ** end
		
		log.info(today.get(Calendar.MONTH) +"="+ toDateofQuater.get(Calendar.MONTH) +"<<Checking the dates>>"+ today.get(Calendar.DATE) +"="+ toDateofQuater.get(Calendar.DATE));
		if(today.get(Calendar.MONTH) == toDateofQuater.get(Calendar.MONTH) && today.get(Calendar.DATE) == toDateofQuater.get(Calendar.DATE))  // Pick the data in the end of quater only and perform expiry
		//if(today.get(Calendar.MONTH) == toDateofQuater.get(Calendar.MONTH) && today.get(Calendar.DATE) == 26)  // Pick the data in the end of quater only and perform expiry
		//if(true)  // for testing
		{
			Calendar allCal[] = getQuaterDetails(Integer.parseInt(Cache.getConfigParameterMap().get("REWARD_POINTS_EXPIRY_PERIOD").getParameterValue().trim()));
			// Getting expired Reward points
			Map<Long, LoyaltyExpiryDTO> expiryMap = getRewardExpiryDetails(allCal);

			allCal = getQuaterDetails(Integer.parseInt(Cache.getConfigParameterMap().get("TIER_EXPIRY_PERIOD").getParameterValue().trim()));
			// Getting expired status points
			log.info("going to get the expiry status");
			getStatusExpiryDetails(expiryMap, allCal);
			log.info("received the expiry status");

			Set<Long> set = expiryMap.keySet();
			Iterator<Long> it = set.iterator();
			// Adding dtos to threadpool
			while (it.hasNext()) {
				Long loyaltyId = it.next();
				log.debug("printing the expirystatus points");
				log.debug("For Loyalty Id= " + loyaltyId + " Status point = " + expiryMap.get(loyaltyId).getExpiryStatusPoints() + " and reward point " + expiryMap.get(loyaltyId).getExpiryRewardPoints() + " going to expiry");
				//ThreadInitiator.loyaltyExpiryPool.addTask(expiryMap.get(loyaltyId));
				// expiryMap.remove(loyaltyId);
				it.remove();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private Map<Long,LoyaltyExpiryDTO> getRewardExpiryDetails(Calendar allCal[])
	{
		Session session = null;
		Query query = null;
		List<Object[]> list = null;
		
		Map<Long,LoyaltyExpiryDTO> expiryMap = new HashMap<Long,LoyaltyExpiryDTO>();
		
		LoyaltyExpiryDTO dto = null;
		
		//commented for testing sajith ks ***start
		/*try
		{
			
			
			log.info("From=>"+allCal[0].getTime()+" to"+allCal[1].getTime());
			
			session = HiberanteUtil.getSessionFactory().openSession();
			query = session.createSQLQuery("SELECT LOYALTY_ID,SUM(REWARD_POINTS) FROM LOYALTY_TRAN_SUMMARISED_01 " +
					" WHERE CREATE_DATE BETWEEN ? AND ? AND REWARD_POINTS>0 GROUP BY LOYALTY_ID ");
			
			query.setParameter(0, allCal[0].getTime());// From Date
			query.setParameter(1, allCal[1].getTime());// To Date
			
			list = query.list();
			log.info("is the list empty rewardpoints::::"+list.isEmpty());
			
			long loyaltyId;
			for(Object[] all : list)
			{
				dto = new LoyaltyExpiryDTO();
				dto.setRewardQuater(allCal);
				loyaltyId = Long.parseLong(all[0].toString());
				dto.setLoyaltyID(loyaltyId);
				dto.setExpiryRewardPoints(Double.parseDouble(all[1].toString()));
				
				expiryMap.put(loyaltyId, dto);
				dto = null;
			}
			
			list = null;
			
		}
		catch (Exception e) 
		{
			log.info("Exception while getting details =>",e);
		}
		finally
		{
			try
			{
				if(session!=null)
					session.close();
			}
			catch (Exception e) {
			}
		}*/
		//commented for testing sajith ks ****end
		
		
		return expiryMap;
	} //getRewardExpiryDetails
	
	@SuppressWarnings("unchecked")
	public void getStatusExpiryDetails(Map<Long,LoyaltyExpiryDTO> expiryMap,Calendar allCal[])
	{
		Session session = null;
		Query query = null;
		List<Object[]> list = null;
		ArrayList<Long> cahcheList = new ArrayList<Long>();
		
		LoyaltyExpiryDTO dto = null;
		//cahcheList=cachIt(); //test code sajithks
		
			
		try
		{
			
			
			log.debug("from date---->"+allCal[0].getTime()+"to date---->"+allCal[1].getTime());
			session = HiberanteUtil.getSessionFactory().openSession();
			/*query = session.createSQLQuery("SELECT LOYALTY_ID,SUM(STATUS_POINTS) FROM LOYALTY_TRAN_SUMMARISED_01 " +
					" WHERE CREATE_DATE BETWEEN ? AND ? AND STATUS_POINTS>0 GROUP BY LOYALTY_ID ");*/ //commented for testing sajith
			query = session.createSQLQuery("select LOYALTY_ID,sum(STATUS_POINTS) from LOYALTY_TRAN_SUMMARY_MONTHLY "
					
					+ "where PARTITION_INDEX in ("+Cache.getCacheMap().get("STATUS_POINTS_PARTITION_INDEX")+") AND LOYALTY_ID in (select LOYALTY_ID from STATUS_TABLE)  GROUP by LOYALTY_ID");
			
			log.debug("going to setparameter");
			//for testing ***start sajith ks
			//query.setParameter(0, tmployaltyID);
			//for testing ***end sajith ks
			
			//for testing Sajith ks **** start
			/*query.setParameter(0, allCal[0].getTime());// From Date
			query.setParameter(1, allCal[1].getTime());// To Date
*/	//for testing Sajith ks **** end
			log.debug("after setparameter");
			
			log.debug("going to querylist()");
			list = query.list();
			log.debug("after querylist()");
			
			
			/*
			log.info("whether the list empty==="+list.isEmpty());
			if(!list.isEmpty()){
			Object[] fst=list.get(0);
			log.info("the loyaltyid==="+fst[0]);
			log.info("the statuspoint==="+fst[1]);
			}*/
			
			long loyaltyId;
			for(Object[] all : list)
			{
				//log.info("in for for status");
				//log.info("getting the loyaltyid");
				loyaltyId = Long.parseLong(all[0].toString());
				//log.info("got the loyaltyid");
				if(expiryMap.containsKey(loyaltyId))
				{
					//log.info("status in if");
					//log.info("expiry status fetched from table in if ===>"+Double.parseDouble(all[1].toString()));
					expiryMap.get(loyaltyId).setExpiryStatusPoints(Double.parseDouble(all[1].toString()));
					expiryMap.get(loyaltyId).setStatusQuater(allCal);
				}
				else
				{
					//log.debug("status in else");
					dto = new LoyaltyExpiryDTO();
					dto.setLoyaltyID(loyaltyId);
					//log.info("expiry status fetched from table in else ===>"+Double.parseDouble(all[1].toString()));
					dto.setExpiryStatusPoints(Double.parseDouble(all[1].toString()));
					dto.setStatusQuater(allCal);
					expiryMap.put(loyaltyId, dto);
					dto = null;
				}
				//log.debug("status neither in  else nor in if");
				
			}
			
			list = null;
			
		}
		catch (Exception e) 
		{
			log.info("Exception while getting details =>",e);
		}
		finally
		{
			try
			{
				if(session!=null)
					session.close();
			}
			catch (Exception e) {
			}
		}
		
	
		
	}
	
	private Calendar[] getQuaterDetails(int expiryPeriodInMnth)
	{
		Calendar fromDate = Calendar.getInstance();
		Calendar toDate = Calendar.getInstance();
		
		//test code sajith ks ****start
		fromDate.set(Calendar.YEAR, 2016);
		fromDate.set(Calendar.MONTH, 3);
		fromDate.set(Calendar.DATE, 1);
		
		toDate.set(Calendar.YEAR, 2016);
		toDate.set(Calendar.MONTH, 6);
		toDate.set(Calendar.DATE, 10);
		//test code sajith ks ****end
		
        
		//commented for testing ****start 
		/*log.info("Expiry Period " + expiryPeriodInMnth);

		fromDate.set(Calendar.HOUR_OF_DAY, 0);
		fromDate.set(Calendar.MINUTE, 0);
		fromDate.set(Calendar.SECOND, 0);
		toDate.set(Calendar.HOUR_OF_DAY, 23);
		toDate.set(Calendar.MINUTE, 59);
		toDate.set(Calendar.SECOND, 59);

		Calendar date = Calendar.getInstance();
		
		//test code **start
		date.set(Calendar.YEAR, 2015);
		date.set(Calendar.MONTH, 11);
		date.set(Calendar.DATE, 31);
		//Test code **end
		
		date.add(Calendar.MONTH, -expiryPeriodInMnth);

		fromDate.add(Calendar.MONTH, -expiryPeriodInMnth);
		toDate.add(Calendar.MONTH, -expiryPeriodInMnth);

		int quater = (int) Math.ceil(date.get(Calendar.MONTH) / 3);
		log.info("Quater " + quater);

		switch (quater) {

		case 0:
			fromDate.set(Calendar.MONTH, 0);
			fromDate.set(Calendar.DATE, 1);
			toDate.set(Calendar.MONTH, 2);
			toDate.set(Calendar.DATE, 31);
			break;
		case 1:
			fromDate.set(Calendar.MONTH, 3);
			fromDate.set(Calendar.DATE, 1);
			toDate.set(Calendar.MONTH, 5);
			toDate.set(Calendar.DATE, 30);
			break;
		case 2:
			fromDate.set(Calendar.MONTH, 6);
			fromDate.set(Calendar.DATE, 1);
			toDate.set(Calendar.MONTH, 8);
			toDate.set(Calendar.DATE, 30);
			break;
		case 3:
			fromDate.set(Calendar.MONTH, 9);
			fromDate.set(Calendar.DATE, 1);
			toDate.set(Calendar.MONTH, 11);
			toDate.set(Calendar.DATE, 31);
			break;

		}
*/	//commented for testing ****end	
		Calendar allCal[] = new Calendar[2];
		allCal[0] = fromDate;
		allCal[1] = toDate;

		return allCal;
	}
	
	
	public boolean updateRewardSummary(LoyaltyExpiryDTO loyaltyExpiryDTO,LoyaltyTransactionTabDTO loyaltyTransactionTabDTO,Double rewardPoints , Double statusPoints,long counter,String appStr)
	{
		System.out.println("inside updateRewardSummary");
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		TableInfoDAO infoDAO = new TableInfoDAO();
		boolean flag = false;
		try
		{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			//Update Reward Records
			//Calendar allCal[] = getQuaterDetails(Integer.parseInt(Cache.getConfigParameterMap().get("REWARD_POINTS_EXPIRY_PERIOD").getParameterValue().trim()));
			if(loyaltyExpiryDTO.getExpiryRewardPoints()>0)
			{    
				//Commented for testing ***start
				/*query = session.createSQLQuery("UPDATE LOYALTY_TRAN_SUMMARISED_01 SET EXPIR_REWARD_POINTS=REWARD_POINTS,REWARD_POINTS=0,EXPIRY_DATE_REWARD=SYSDATE " +
						" WHERE CREATE_DATE BETWEEN ? AND ? AND LOYALTY_ID=? AND REWARD_POINTS>0");
				query.setParameter(0, loyaltyExpiryDTO.getRewardQuater()[0].getTime());
				query.setParameter(1, loyaltyExpiryDTO.getRewardQuater()[1].getTime());
				query.setParameter(2, loyaltyExpiryDTO.getLoyaltyID());
				query.executeUpdate();
				
				log.info("Reward Points updated in summary table for loyalty Id="+loyaltyExpiryDTO.getLoyaltyID());*/
				//Commented for testing ***end
			}
			//--------------------
			
			//Update Status Records
			//allCal = getQuaterDetails(Integer.parseInt(Cache.getConfigParameterMap().get("TIER_EXPIRY_PERIOD").getParameterValue().trim()));
			if(loyaltyExpiryDTO.getExpiryStatusPoints()>0)
			{    
				//Commented for testing ***start
				//commented for today's release ****start
				/*query = session.createSQLQuery("UPDATE LOYALTY_TRAN_SUMMARISED_01 SET EXPIRY_STATUS_POINTS=STATUS_POINTS,STATUS_POINTS=0,EXPIRY_DATE_STATUS=SYSDATE " +
						" WHERE CREATE_DATE BETWEEN ? AND ? AND LOYALTY_ID=? AND STATUS_POINTS>0");*/
				//commented for today's release *****end
						
						query = session.createSQLQuery("UPDATE LOYALTY_TRAN_SUMMARY_MONTHLY SET EXPIRY_STATUS_POINTS=STATUS_POINTS,STATUS_POINTS=0,EXPIRY_DATE_STATUS=SYSDATE " +
						" WHERE PARTITION_INDEX in ("+Cache.getCacheMap().get("STATUS_POINTS_PARTITION_INDEX")+") AND LOYALTY_ID=? AND STATUS_POINTS>0");
						
						//from LOYALTY_TRAN_SUMMARY_MONTHLY where PARTITION_INDEX in (201412)
					
				//commented for today's release ****start		
				/*query.setParameter(0, loyaltyExpiryDTO.getStatusQuater()[0].getTime());
				query.setParameter(1, loyaltyExpiryDTO.getStatusQuater()[1].getTime());
				query.setParameter(2, loyaltyExpiryDTO.getLoyaltyID());*/
						//commented for today's release ****end
						
						query.setParameter(0, loyaltyExpiryDTO.getLoyaltyID());// added for today's release
				query.executeUpdate(); 
				log.info("Status Points updated in summary table for loyalty Id="+loyaltyExpiryDTO.getLoyaltyID());
				//Commented for testing ***end
			}
			//Commented for testing ***start
			//------------------------------
			//Inserting transaction
			session.save(infoDAO.getLoyaltyTransactionTable(loyaltyExpiryDTO.getLoyaltyID().toString()),loyaltyTransactionTabDTO);
			
			//Update loyalty profile
			
			query = session.createSQLQuery("UPDATE "+infoDAO.getLoyaltyProfileDBTable(loyaltyExpiryDTO.getLoyaltyID().toString())+" SET STATUS_POINTS=? , REWARD_POINTS=? , TIER_ID=?"+appStr+" WHERE LOYALTY_ID=? AND COUNTER = ?");
			query.setParameter(0, statusPoints);
			query.setParameter(1, rewardPoints);
			query.setParameter(2, loyaltyExpiryDTO.getCurTierId());
			query.setParameter(3, loyaltyExpiryDTO.getLoyaltyID());
			query.setParameter(4, counter);
			
			int updateCount =query.executeUpdate(); //commented for cdr generation for testing
			//Commented for testing ***end
			//int updateCount=1;//testing
			
			if(updateCount>0)
			{
				if (Cache.getCacheMap().get("ACTUAL_REWARD_EXPIRY").equalsIgnoreCase("true")) {
					transaction.commit();
					flag = true;
				}
					writeCdr("SC0000", "SUCCESS", loyaltyExpiryDTO,loyaltyTransactionTabDTO);	
				loyaltyExpiryDTO.setStatus("SC0000");
			}
			else
			{
				transaction.rollback();
				flag = false;
				writeCdr("SC0001", "FAILURE", loyaltyExpiryDTO,loyaltyTransactionTabDTO);
			}
			
				
				//log.fatal(loyaltyExpiryDTO.toString());//CDR like
			
		}
		catch (Exception e) 
		{
			if(transaction!=null)
				transaction.rollback();
			log.info("Error => ",e);
			flag = false;
		}
		finally
		{
			try
			{
				if(session!=null)
					session.close();
			}
			catch (Exception e) {
			}
			
		}
		
		return flag;
	}
	//test code sajithks ***start
	public ArrayList<Long> cachIt(){
		Session session = null;
		Query query = null;
		List<Object[]> list = null;
		ArrayList<Long> cacheList = new ArrayList<Long>();
		long loyaltyId;
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			query = session.createSQLQuery("SELECT LOYALTY_ID FROM STATUS_TABLE WHERE STATUS =0");
			log.info("going to get the cacheLISt from DB");
			list = query.list();
			
			log.info("is cachelist from DB empty---->"+cacheList.isEmpty());
			for(Object [] all :list){
				loyaltyId = Long.parseLong(all[0].toString());
				cacheList.add(loyaltyId);
			}
			log.info("Cached the loyaltyIds from DB");
			
		}catch(Exception e){
			log.error("Exception in test caching---> "+e);
		}finally
		{
			try
			{
				if(session!=null)
					session.close();
			}
			catch (Exception e) {
			}
		}
		
		return cacheList;
	}
	
	
	public boolean updateStatusTable(LoyaltyExpiryDTO loyaltyExpiryDTO){
		log.info("****Inside updateStatusTable*****");
		Session session = null;
		Query query = null;
		Transaction transaction = null;
		int result;
		boolean flag = false;
		
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			query = session.createSQLQuery("UPDATE STATUS_TABLE SET STATUS=1 WHERE LOYALTY_ID =? AND STATUS=0 ");
			query.setParameter(0, loyaltyExpiryDTO.getLoyaltyID());
			log.info("executing status table updation query");
			//result=query.executeUpdate();//commented for testing
			result=1;
			if(result>0)
			{
				transaction.commit();
				flag = true;
			}
			else
			{
				transaction.rollback();
				flag = false;
			}
			
		}catch(Exception e){
			if(transaction!=null)
				transaction.rollback();
			log.info("Error while updating STATUS_TABLE ----> ",e);
			flag = false;
		}
		finally
		{
			try
			{
				if(session!=null)
					session.close();
			}
			catch (Exception e) {
			}
		}
		
		return flag;
	}
	//test code sajithks ***end
	
	public void writeCdr(String statusCode, String statusDescription,
			LoyaltyExpiryDTO dto,LoyaltyTransactionTabDTO transactionTabDTO) {		
		log.warn(String							         
				.format("%s|%s|||||||||%s|%s|%s|%s|%s||||||||||",
						dto.getLoyaltyID(),
						df.format(new Date()),									
						statusCode,
						statusDescription,
						dto.getExpiryStatusPoints(),
						transactionTabDTO.getPreTierId(),
						transactionTabDTO.getCurTierId()
						
						));		
		
	}
	
}
