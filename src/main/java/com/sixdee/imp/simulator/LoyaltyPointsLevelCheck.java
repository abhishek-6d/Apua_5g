package com.sixdee.imp.simulator;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dao.TableDetailsDAO;
import com.sixdee.imp.dao.TableInfoDAO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.LoyaltyRegisteredNumberTabDTO;
import com.sixdee.imp.dto.LoyaltyTransactionTabDTO;
import com.sixdee.imp.dto.TierInfoDTO;
import com.sixdee.imp.util.LoyalityCommonTransaction;
import com.sixdee.imp.util.LoyalityTransactionConstants;

public class LoyaltyPointsLevelCheck 
{
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	DateFormat df1 = new SimpleDateFormat("MM");
	Logger log = Logger.getLogger(LoyaltyPointsLevelCheck.class);
	
	public void start()
	{
		List<Object[]> allLoyalty = getAllLoyaltyId();
		pointLevelCheck(allLoyalty);
		allLoyalty=null;
		List<BigDecimal> list=getAllMinusLoyaltyId();
		findingOpeningBalance(list);
	}
	
	
	private void pointLevelCheck(List<Object[]> allLoyaltyIds)
	{
		TableInfoDAO tableInfoDAO=new TableInfoDAO();
		for(Object[] objects:allLoyaltyIds)
		{
			Long loyaltyID=null;
			Double profileRewardPoints=0.0;
			Double profileStatusPoints=0.0;
			Session session=null;
			Query query=null;
			String sql=null;
			Double transactionRewardPoints=0.0;
			Double transactionStatusPoints=0.0;
			
			Double rewardPoints=0.0;
			Double statusPoints=0.0;
			Double subRewardPoints=0.0;
			Object object=null;
			try{
				
				loyaltyID=Long.parseLong(objects[0].toString());
				profileRewardPoints=Double.parseDouble(objects[1].toString());
				profileStatusPoints=new Double(objects[2].toString());
				
				log.info("Loyalty ID : "+loyaltyID+" From LoyaltyProfile Reward Points "+profileRewardPoints+" Status Points "+profileStatusPoints);
				
				session=HiberanteUtil.getSessionFactory().openSession();
				
				sql="select sum(REWARD_POINTS) FROM "+tableInfoDAO.getLoyaltyTransactionDBTable(loyaltyID+"")+" where LOYALTY_ID= "+loyaltyID +
					 " and STATUS_ID=5 and MONTH_INDEX in (9,10,11,12,1,2,3,4)";
				
				object=session.createSQLQuery(sql).uniqueResult();
				//Reward Count
				if(object!=null)
				  transactionRewardPoints=Double.parseDouble(object.toString());
				
				sql="select sum(case when OLD_POINTS<>0 then OLD_POINTS else REWARD_POINTS end) FROM "+tableInfoDAO.getLoyaltyTransactionDBTable(loyaltyID+"")+"" +
					" where LOYALTY_ID= "+loyaltyID +" and STATUS_ID=8 and MONTH_INDEX in (9,10,11,12,1,2,3,4)";
				
				object=session.createSQLQuery(sql).uniqueResult();
				//Redeem count,final remaing count
				if(object!=null)
				  transactionRewardPoints=transactionRewardPoints-Double.parseDouble(object.toString());
				
				sql="select sum(STATUS_POINTS) FROM "+tableInfoDAO.getLoyaltyTransactionDBTable(loyaltyID+"")+" where LOYALTY_ID= "+loyaltyID +
				    " and MONTH_INDEX in (9,10,11,12,1,2,3,4)";
				
				object=session.createSQLQuery(sql).uniqueResult();
				//Status Points
				if(object!=null)
				transactionStatusPoints=Double.parseDouble(object.toString());
				
				log.info("Loyalty ID : "+loyaltyID+" From  Transaction Reward Points "+transactionRewardPoints+" Status Points "+transactionStatusPoints);
				
				if(profileRewardPoints.longValue()>transactionRewardPoints.longValue())
					rewardPoints=profileRewardPoints-transactionRewardPoints;
				else if(transactionRewardPoints.longValue()>profileRewardPoints.longValue())
					subRewardPoints=transactionRewardPoints-profileRewardPoints;
				
				if(profileStatusPoints.longValue()>transactionStatusPoints.longValue())
					statusPoints=profileStatusPoints-transactionStatusPoints;
				
				if(rewardPoints>0||statusPoints>0)
				{
					log.info("Loyalty ID : "+loyaltyID+" Going to add Reward Points "+rewardPoints+" and Substract Reward Points  "+subRewardPoints+" Status Points "+statusPoints);
					List<LoyaltyTransactionTabDTO> list= findingOpeningBalance(loyaltyID);
					pointCalculation(list,loyaltyID,rewardPoints,subRewardPoints,statusPoints);
					
				}
				
				
				
			}catch (Exception e) {
				e.printStackTrace();
				log.info("Exception Loyalty ID "+loyaltyID,e);
			}finally{
				if(session!=null)
					session.close();
				
				session=null;
				query=null;
				sql=null;
				loyaltyID=null;
				profileRewardPoints=null;
				profileStatusPoints=null;
				object=null;
			}
			
		}//for All loyalty ids
		
		tableInfoDAO=null;
	}//pointLevelCheck
	 
	
	
	private List<Object[]> getAllLoyaltyId()
	{
		Session session = null;
		Query query = null;
		List<Object[]> list = null; 
		try
		{
			session = HiberanteUtil.getSessionFactory().openSession();
			query = session.createSQLQuery("SELECT LOYALTY_ID,REWARD_POINTS,STATUS_POINTS FROM LOYALTY_PROFILE_0");
			
			list = query.list();
			
			log.info("Got All Loyalty Ids from LOYALTY_PROFILE_0 .... Count is "+list.size());
			
		}
		catch (Exception e) 
		{
			log.error("Exception ",e);
		}
		finally
		{
			if(session!=null)
				session.close();
			
			query = null;
		}
		
		return list;
	}
	
	public void findingOpeningBalance(List<BigDecimal> loayaltyIds)
	{
		Session session = null;
		Query query = null;
		TableInfoDAO tableInfoDAO = new TableInfoDAO();
		List<LoyaltyTransactionTabDTO> list = null;
		try
		{
			int counter=0;
			
			for(ListIterator<BigDecimal> it = loayaltyIds.listIterator();it.hasNext();)
			{
				counter++;
				
				Long loyaltyID=null;
				
				try
				{
					session = HiberanteUtil.getSessionFactory().openSession();
					loyaltyID =it.next().longValue();
					
					log.info(" Loyalty ID : "+loyaltyID+" Start Processing ");
				 
					query = session.createQuery("FROM " + tableInfoDAO.getLoyaltyTransactionTable(loyaltyID + "") +
							" WHERE monthIndex in (9,10,11,12,1,2,3,4) AND loyaltyID=? and statusID IN (5,8,9)  ORDER BY createTime");

					query.setParameter(0, loyaltyID);

					list = query.list();
					
					log.info(" Loyalty ID : "+loyaltyID+" Transaction Count is "+list.size());
					
				}
				catch (Exception e) {
					log.error(" Exception Loyalty ID "+loyaltyID,e);
				}finally{
					if(session!=null)
						session.close();
					session=null;
					query = null;
				}
				try{
				if(list!=null&&list.size()>0)
					   findGenuineRedeem(list,loyaltyID);
				}catch (Exception e) {
					log.error(" Exception Loyalty ID "+loyaltyID,e);
				}
				
				log.info("Loyalty ID : " + loyaltyID+" Processing completed , Remaing Loyalty ID Count is "+(loayaltyIds.size()-counter));
			}
			
		}catch (Exception e) 
		{
			log.error("Exception while finding opening balance",e);
		}
		finally
		{
			if(session!=null)
				session.close();
			
			query = null;
			tableInfoDAO = null;
			list=null;
		}
		
		
		
		
	}
	
	
	public List<LoyaltyTransactionTabDTO> findingOpeningBalance(Long loyaltyID)
	{
		Session session = null;
		Query query = null;
		TableInfoDAO tableInfoDAO = new TableInfoDAO();
		List<LoyaltyTransactionTabDTO> list = null;
				
		try {
			session = HiberanteUtil.getSessionFactory().openSession();

			log.info(" Loyalty ID : " + loyaltyID + " Getting Records from Transaction ");

			query = session.createQuery("FROM " + tableInfoDAO.getLoyaltyTransactionTable(loyaltyID + "") +
					" WHERE monthIndex in (9,10,11,12,1,2,3,4) AND loyaltyID=? and statusID IN (5,8,9)  ORDER BY createTime");

			query.setParameter(0, loyaltyID);

			list = query.list();

			log.info(" Loyalty ID : " + loyaltyID + " Transaction Count is " + list.size());

		} catch (Exception e) {
			log.error(" Exception Loyalty ID " + loyaltyID, e);
		} finally {
			if (session != null)
				session.close();
			session = null;
			query = null;
			tableInfoDAO = null;
		}
		
		return list;
	}
	
	private void findGenuineRedeem(List<LoyaltyTransactionTabDTO> list,Long loyaltyId)
	{
		TableDetailsDAO tableDetailsDAO = new TableDetailsDAO();
		Map<Integer,Double> cdrMap = new HashMap<Integer,Double>();
		int tierId=0;
		
		TableInfoDAO infoDAO=new TableInfoDAO();

		Session session=null;
		Transaction transaction=null;
		
		try{
		
		
		cdrMap.put(3, 0.0);
		cdrMap.put(5, 0.0);
		 
			//cdrMap.put(1, list.get(0).getLoyaltyID());
			cdrMap.put(2, list.get(0).getPreRewardPoints()); // 2 -opening, 3-redeemed

			Double openingBalnce = 0.0;
			Double statusbalance = list.get(0).getPreStatusPoints();
			LoyaltyTransactionTabDTO dto = null;
			Iterator<LoyaltyTransactionTabDTO> iterator=list.iterator();
			while(iterator.hasNext())
			{
				
				dto =iterator.next();
				log.info("ID "+dto.getId()+"  REWARD POINTS "+dto.getRewardPoints()+"  "+openingBalnce);
				statusbalance +=(dto.getStatusPoints()!=null?dto.getStatusPoints():0);
				
				if(dto.getStatusID()==9)
				{
					iterator.remove();
					continue;
				}
				
				if (dto.getStatusID() == 5)
				{
					
					openingBalnce += dto.getRewardPoints();
					
				}else if (dto.getStatusID() == 8) {
					
					if(openingBalnce<=1&&(dto.getVoucherOrderID()==null||dto.getVoucherOrderID().trim().equals("")))//If opening Balance is zero,this redeem this not proper,remove from the transaction history
					{
						log.info("Before Removed loyalty ID "+dto.getLoyaltyID()+"   ID "+dto.getId()+"  Size "+list.size());
						
						session=HiberanteUtil.getSessionFactory().openSession();
						transaction=session.beginTransaction();
						session.delete(infoDAO.getLoyaltyTransactionTable(dto.getLoyaltyID() + ""),dto);
						transaction.commit();
						
						iterator.remove();
						log.info("Removed loyalty ID "+dto.getLoyaltyID()+"   ID "+dto.getId()+"  Size "+list.size());
						continue;
					}
					
					log.info("Loyalty ID : "+loyaltyId+" Reward Balance "+openingBalnce+"  Redeem points "+dto.getRewardPoints());
					
					if (openingBalnce >= dto.getRewardPoints()) {
						openingBalnce = openingBalnce - dto.getRewardPoints();

						cdrMap.put(3, cdrMap.get(3) + dto.getRewardPoints());
						LoyaltyRegisteredNumberTabDTO loyaltyRegisteredNumberTabDTO =tableDetailsDAO.getLoyaltyRegisteredNumberDetails(dto.getLoyaltyID(), dto.getSubscriberNumber());
						tierId = getTier(dto.getCurStatusPoints());
						log.warn(String.format("%s|%s|%s|||%s|||%s|%s|%s|'%s'|%s|%s|%s|%s|||||",dto.getLoyaltyID() ,df.format(dto.getCreateTime()),dto.getSubscriberNumber(),dto.getLoyaltyID(),tierId,tierId,"SC0000","SUCCESS",dto.getRewardPoints(),dto.getPackageId(),(loyaltyRegisteredNumberTabDTO!=null?loyaltyRegisteredNumberTabDTO.getAccountTypeId():""),dto.getChannel()));
					}else{
						if(dto.getVoucherOrderID()==null||dto.getVoucherOrderID().trim().equals(""))
						{
							log.info("Loyalty ID "+loyaltyId+"  Opening Balnec "+openingBalnce);
							dto.setRewardPoints(Double.parseDouble(""+openingBalnce.longValue()));
							
							session=HiberanteUtil.getSessionFactory().openSession();
							transaction=session.beginTransaction();
							session.update(infoDAO.getLoyaltyTransactionTable(dto.getLoyaltyID() + ""),dto);
							transaction.commit();
							
							openingBalnce =0.0;
							
							log.info("Loyalty ID "+loyaltyId+" After Opening Balnec "+openingBalnce+"  "+dto.getRewardPoints());
							
							cdrMap.put(3, cdrMap.get(3) + dto.getRewardPoints());
							LoyaltyRegisteredNumberTabDTO loyaltyRegisteredNumberTabDTO =tableDetailsDAO.getLoyaltyRegisteredNumberDetails(dto.getLoyaltyID(), dto.getSubscriberNumber());
							tierId = getTier(dto.getCurStatusPoints());
							log.warn(String.format("%s|%s|%s|||%s|||%s|%s|%s|'%s'|%s|%s|%s|%s|||||",dto.getLoyaltyID() ,df.format(dto.getCreateTime()),dto.getSubscriberNumber(),dto.getLoyaltyID(),tierId,tierId,"SC0000","SUCCESS",dto.getRewardPoints(),dto.getPackageId(),(loyaltyRegisteredNumberTabDTO!=null?loyaltyRegisteredNumberTabDTO.getAccountTypeId():""),dto.getChannel()));
						}else{
							updateBalance(dto," OLD_POINTS="+openingBalnce,new TableInfoDAO().getLoyaltyTransactionDBTable(dto.getLoyaltyID()+""));
							openingBalnce=0.0;
						}
					}

				}
			}
		
			
		cdrMap.put(4, openingBalnce); // closing
		cdrMap.put(5, statusbalance); // status point
			
		
		LoyaltyProfileTabDTO profileTabDTO = tableDetailsDAO.getLoyaltyProfile(loyaltyId);
		
		 
		log.info("Loaylty ID : "+loyaltyId+" Current Reward Points "+profileTabDTO.getRewardPoints()+"  From Transaction Points "+openingBalnce);
		log.info("Before, Loaylty ID : "+loyaltyId+" Current Status Points "+profileTabDTO.getStatusPoints()+"  From Transaction Points "+statusbalance);
		
		double statusPointsadd=0.0;
		
		if(profileTabDTO.getTierId()==getTier(cdrMap.get(5).doubleValue()))
		{
			cdrMap.put(6, profileTabDTO.getTierId().doubleValue());
		}
		else
		{
			TierInfoDTO infoDTO = Cache.tierInfoMap.get(profileTabDTO.getTierId());
			if(infoDTO.getTierId()!=1)
			{
				statusPointsadd=infoDTO.getMinValue()-cdrMap.get(5);
				cdrMap.put(5,infoDTO.getMinValue().doubleValue());
			}
			cdrMap.put(6, Double.parseDouble(profileTabDTO.getTierId()+""));
		}
		
		//updateLoyaltyProfileBalance(profileTabDTO," STATUS_POINTS="+cdrMap.get(5));
		
		log.info("After, Loaylty ID : "+loyaltyId+" Current Status Points "+cdrMap.get(5)+"  From Transaction Points "+statusbalance);
		
		double pointsToAdd = profileTabDTO.getRewardPoints()-openingBalnce;
		pointsToAdd=pointsToAdd>0?pointsToAdd:0;
		double pointToSubstract = openingBalnce-profileTabDTO.getRewardPoints();
		pointToSubstract=pointToSubstract>0?pointToSubstract:0;
		//double statusPointsSubstract=profileTabDTO.getStatusPoints()-cdrMap.get(5);
		
		log.info("Loyalty ID : "+loyaltyId+" Need to add extra reward points "+pointsToAdd);
		log.info("Loyalty ID : "+loyaltyId+" Need to substract reward points "+pointToSubstract);
		//log.info("Loyalty ID : "+loyaltyId+" Need to substract Status points "+statusPointsSubstract);
		log.info("Loyalty ID : "+loyaltyId+" Need to add Status points "+statusPointsadd);
		
		statusPointsadd=0.0;
		
		//Need to change some code
		pointCalculation(list,loyaltyId,pointsToAdd,pointToSubstract,statusPointsadd); // Start Point Calculation
		
		insertReport(loyaltyId, pointsToAdd>0?pointsToAdd:0, pointToSubstract>0?pointToSubstract:0, statusPointsadd>0?statusPointsadd:0,profileTabDTO.getRewardPoints(),cdrMap.get(5)); // insert report
		
		log.info("Loyalty ID : "+cdrMap);
		
		}catch (Exception e) {
			log.info("Exception Loyalty ID : "+loyaltyId,e);
			if(transaction!=null)
				transaction.rollback();
		}finally{
			if(session!=null)
				session.close();
			session=null;
			transaction=null;
		}
		
	}
	
	
	private void pointCalculation(List<LoyaltyTransactionTabDTO> list,Long loyaltyId,double pointToAdd,double pointToSubstract,double statusPointsAdd)
	{
		TableInfoDAO infoDAO = new TableInfoDAO();
		LoyaltyTransactionTabDTO loyaltyTransactionTabDTO = null;
		String tableName = infoDAO.getLoyaltyTransactionDBTable(loyaltyId.toString()) ;
		
		if(list==null||list.size()==0)
			return;
			
		
		double openingBalance = 0;
		
		double closingBalance = 0.0;
		
		double openingStatusPoints=0;
		
		double closingStatusPoints=0.0;
		
		boolean isRewardSub=true;
		boolean isStatusSub=true;
		
		int j=list.size()<10?list.size()-1:10;
		int k=list.size()<10?list.size()-2:10;;
		
		for(int i=0;i<list.size();i++)
		{
			loyaltyTransactionTabDTO = list.get(i);
			
			
			log.info("ID "+loyaltyTransactionTabDTO.getId()+"    "+openingBalance);
			
			isRewardSub=true;
			isStatusSub=true;
			
			
			if(i==0) //For First DTO
			{

				closingStatusPoints=openingStatusPoints+loyaltyTransactionTabDTO.getStatusPoints();
				
				if (loyaltyTransactionTabDTO.getStatusID() == 5)
					closingBalance = openingBalance + loyaltyTransactionTabDTO.getRewardPoints();
				else if (loyaltyTransactionTabDTO.getStatusID() == 8)
				{
					if(loyaltyTransactionTabDTO.getOldPoints()<=0)
					   closingBalance = openingBalance - loyaltyTransactionTabDTO.getRewardPoints();
					else
					   closingBalance = openingBalance - loyaltyTransactionTabDTO.getOldPoints();
				}
				
				
					String sql="";
					if(isRewardSub)
					sql=" PRE_REWARD_POINTS="+openingBalance+",CUR_REWARD_POINTS="+closingBalance;
					
					if(isStatusSub)
					{
						sql+=(isRewardSub)?",":"";
						sql+=" PRE_STATUS_POINTS="+openingStatusPoints+",CUR_STATUS_POINTS="+closingStatusPoints;
					}
					
					if(!sql.trim().equals(""))
					  updateBalance(loyaltyTransactionTabDTO,sql, tableName);
				
			}
			else
			{
				
				
				if(i==j && (pointToAdd>0||statusPointsAdd>0)) // Adding points, Extra Points
				{
					//Insert Transaction
					openingBalance = closingBalance;
					closingBalance = closingBalance + pointToAdd;
					
					openingStatusPoints=closingStatusPoints;
					closingStatusPoints=closingStatusPoints+statusPointsAdd;
					
					insertTransaction(loyaltyTransactionTabDTO, openingBalance, closingBalance, pointToAdd,openingStatusPoints,closingStatusPoints,statusPointsAdd); // Insert Transaction
					
					pointToAdd=0;
					statusPointsAdd=0;
					
				}else if(i>k &&pointToSubstract>0)
				{
					if(loyaltyTransactionTabDTO.getStatusID()==5)
					{
						String clause ="";
						
						if(pointToSubstract>0)
						{
							openingBalance = closingBalance;
							clause = " PRE_REWARD_POINTS="+openingBalance;
							if(loyaltyTransactionTabDTO.getRewardPoints()>=pointToSubstract)
							{
								clause = clause + " , REWARD_POINTS=REWARD_POINTS-"+pointToSubstract;
								pointToSubstract =0.0;
								closingBalance = openingBalance+(loyaltyTransactionTabDTO.getRewardPoints()-pointToSubstract);
							}
							else
							{
								pointToSubstract = pointToSubstract - loyaltyTransactionTabDTO.getRewardPoints();
								clause = clause + " , REWARD_POINTS="+0;
								closingBalance = openingBalance+0;
							}
								
							clause = clause+" , CUR_REWARD_POINTS="+closingBalance;
							
							isRewardSub=false;
						}
						/*if(statusPointsSubstract>0)
						{
							openingStatusPoints = closingStatusPoints;
							if(clause!=null&&!clause.equalsIgnoreCase(""))
								clause=clause+",";
							clause = clause+ " PRE_STATUS_POINTS="+openingStatusPoints;
							
							if(loyaltyTransactionTabDTO.getStatusPoints()>=statusPointsSubstract)
							{
								clause = clause + " , STATUS_POINTS=STATUS_POINTS-"+statusPointsSubstract;
								statusPointsSubstract =0.0;
								closingStatusPoints = openingStatusPoints+(loyaltyTransactionTabDTO.getStatusPoints()-statusPointsSubstract);
							}
							else
							{
								statusPointsSubstract = statusPointsSubstract - loyaltyTransactionTabDTO.getStatusPoints();
								clause = clause + " , STATUS_POINTS="+0;
								closingStatusPoints = openingStatusPoints+0;
							}
							
							isStatusSub=false;
							
							clause = clause+" , CUR_STATUS_POINTS="+closingStatusPoints;
						}*/
						
						 if(!clause.trim().equals(""))
						    updateBalance(loyaltyTransactionTabDTO, clause, tableName);
					}
					
					//continue;
					
				}
				
				//if(loyaltyTransactionTabDTO.getPreRewardPoints() != closingBalance||loyaltyTransactionTabDTO.getPreStatusPoints()!=closingStatusPoints )//if opening of current trans. is not same as closing Bal of last transaction then change
				//{
					if(isRewardSub)
					 openingBalance = closingBalance;
					if(isStatusSub)
					 openingStatusPoints=closingStatusPoints;
					
					if(isRewardSub)
					{
						if(loyaltyTransactionTabDTO.getStatusID()==5)
							closingBalance = openingBalance + loyaltyTransactionTabDTO.getRewardPoints();
						else if(loyaltyTransactionTabDTO.getStatusID()==8)
						{
							if(loyaltyTransactionTabDTO.getOldPoints()<=0)
							  closingBalance = openingBalance - loyaltyTransactionTabDTO.getRewardPoints();
							else
							  closingBalance=openingBalance-loyaltyTransactionTabDTO.getOldPoints();	
						}
					}
					
					if(isStatusSub)
					  closingStatusPoints=openingStatusPoints+loyaltyTransactionTabDTO.getStatusPoints();
					
					if(closingBalance<0)
						closingBalance=0;
					
					String sql="";
					if(isRewardSub)
					sql=" PRE_REWARD_POINTS="+openingBalance+",CUR_REWARD_POINTS="+closingBalance;
					
					if(isStatusSub)
					{
						sql+=(isRewardSub)?",":"";
						sql+=" PRE_STATUS_POINTS="+openingStatusPoints+",CUR_STATUS_POINTS="+closingStatusPoints;
					}
					
					if(!sql.trim().equals(""))
					  updateBalance(loyaltyTransactionTabDTO,sql, tableName);
					
					//updateBalance(loyaltyTransactionTabDTO, " CUR_REWARD_POINTS="+closingBalance, tableName);
				//}else{
				//	openingBalance=loyaltyTransactionTabDTO.getPreRewardPoints();
				//	closingBalance=openingBalance+loyaltyTransactionTabDTO.getRewardPoints();
				//}
			}
			
		}
		
	}
	
	
	public void updateBalance(LoyaltyTransactionTabDTO dto,String clause,String tableName)
	{
		Session session = null;
		Query query = null;
		try
		{
			session = HiberanteUtil.getSessionFactory().openSession();
			session.beginTransaction();
			query = session.createSQLQuery("UPDATE "+tableName+" SET "+clause+" WHERE MONTH_INDEX="+Integer.parseInt(df1.format(dto.getCreateTime()))+" AND LOYALTY_ID="+dto.getLoyaltyID()+" and ID="+dto.getId());
			
			int i=query.executeUpdate();
			
			log.info("Loyalty ID : "+dto.getLoyaltyID()+" Update Count is "+i+" For "+clause);
			
			session.getTransaction().commit();
			
		}
		catch (Exception e) 
		{
			log.error("Excepption Loyalty ID : "+dto.getLoyaltyID(),e);
		}
		finally
		{
			if(session!=null)
				session.close();
			
			query = null;
		}
	}

	public void updateLoyaltyProfileBalance(LoyaltyProfileTabDTO dto,String clause)
	{
		Session session = null;
		Query query = null;
		try
		{
			session = HiberanteUtil.getSessionFactory().openSession();
			session.beginTransaction();
			query = session.createSQLQuery("UPDATE LOYALTY_PROFILE_0 SET "+clause+" WHERE LOYALTY_ID="+dto.getLoyaltyID());
			
			query.executeUpdate();
			
			session.getTransaction().commit();
			
		}
		catch (Exception e) 
		{
			log.error("Excepption for Loyalty Id="+dto.getLoyaltyID(),e);
		}
		finally
		{
			if(session!=null)
				session.close();
			
			query = null;
		}
	}
	
	
	private void insertTransaction(LoyaltyTransactionTabDTO dto,double preBal , double currBal, double rewardBal,double openStatusPoints,double closingStatusPoints,double statusPoints)
	{
		TableInfoDAO tableInfoDAO = new TableInfoDAO();
		Session session = null;
		HashMap<String, String> loyaltyTransactionMap=null;
		LoyalityCommonTransaction loyalityCommonTransaction=null;
		try
		{
			session = HiberanteUtil.getSessionFactory().openSession();
			session.beginTransaction();
			loyalityCommonTransaction=new LoyalityCommonTransaction();
			loyaltyTransactionMap=new HashMap<String, String>();//added S
			LoyaltyTransactionTabDTO loyaltyTransactionTabDTO = new LoyaltyTransactionTabDTO();
			
	/*		loyaltyTransactionTabDTO.setChannel("5");
			loyaltyTransactionTabDTO.setLoyaltyID(dto.getLoyaltyID());
			loyaltyTransactionTabDTO.setCreateTime(dto.getCreateTime());
			loyaltyTransactionTabDTO.setPreRewardPoints(preBal);
			loyaltyTransactionTabDTO.setCurRewardPoints(currBal);
			loyaltyTransactionTabDTO.setRewardPoints(rewardBal);
			
			loyaltyTransactionTabDTO.setPreStatusPoints(openStatusPoints);
			loyaltyTransactionTabDTO.setCurStatusPoints(closingStatusPoints);
			loyaltyTransactionTabDTO.setStatusPoints(statusPoints);
			
			loyaltyTransactionTabDTO.setStatusID(5);*/
			
			
			loyaltyTransactionMap.put(LoyalityTransactionConstants.channel,"5");//added S
    		loyaltyTransactionMap.put(LoyalityTransactionConstants.loyaltyID, String.valueOf(dto.getLoyaltyID()));//added S
    		loyaltyTransactionMap.put(LoyalityTransactionConstants.createTime, String.valueOf(dto.getCreateTime()));//added S
    		loyaltyTransactionMap.put(LoyalityTransactionConstants.preRewardPoints, String.valueOf(preBal));//added S
    		loyaltyTransactionMap.put(LoyalityTransactionConstants.curRewardPoints, String.valueOf(currBal));//added S
    		loyaltyTransactionMap.put(LoyalityTransactionConstants.rewardPoints, String.valueOf(rewardBal));//added S
    		loyaltyTransactionMap.put(LoyalityTransactionConstants.preStatusPoints,String.valueOf(openStatusPoints));//added S
    		loyaltyTransactionMap.put(LoyalityTransactionConstants.curStatusPoints, String.valueOf(closingStatusPoints));//added S
    		loyaltyTransactionMap.put(LoyalityTransactionConstants.statusPoints, String.valueOf(statusPoints));//added S
    		loyaltyTransactionMap.put(LoyalityTransactionConstants.statusId, String.valueOf(5));//added S
    		
    		
    		loyaltyTransactionTabDTO=loyalityCommonTransaction.loyaltyTransactionSetter(loyaltyTransactionTabDTO, loyaltyTransactionMap);//added S
			
			
			session.save(tableInfoDAO.getLoyaltyTransactionTable(dto.getLoyaltyID().toString()),loyaltyTransactionTabDTO);
			session.getTransaction().commit();
			
			log.info("Loaylty ID "+dto.getLoyaltyID()+" Insert into Transaction Table for Reward Points "+rewardBal);
			
		}
		catch (Exception e) {
			log.error("Exception loyalty ID : "+dto.getLoyaltyID(),e);
			if(session.getTransaction()!=null)
				session.getTransaction().rollback();
		}
		finally
		{
			if(session!=null)
				session.close();
			tableInfoDAO=null;
		}
	}
	
	
	
	
	
	private void insertReport(long loyaltyId,double pointToAdd,double pointToSubs,double statusToSubs,double currentRewardPoint,double currentStatusPoints)
	{
		Session session = null;
		Query query = null;
		try
		{
			session = HiberanteUtil.getSessionFactory().openSession();
			session.beginTransaction();
			query = session.createSQLQuery("INSERT INTO LOYALTY_POINT_REPORT(LOYALTY_ID,CURR_REWARD_POINTS,REWARD_TO_ADD,REWARD_TO_SUBS,CURR_STATUS_POINTS,STATUS_TO_SUBS) VALUES(?,?,?,?,?,?)");
			query.setParameter(0, loyaltyId);
			query.setParameter(1, currentRewardPoint);
			query.setParameter(2, pointToAdd);
			query.setParameter(3, pointToSubs);
			query.setParameter(4, currentStatusPoints);
			query.setParameter(5, statusToSubs);
			
			 
			
			int i = query.executeUpdate();
			session.getTransaction().commit();
			log.info("Loyalty ID : "+loyaltyId+" Insert into LOYALTY_POINT_REPORT Count is "+i);
			
		}
		catch (Exception e) {
			log.info("Exception Loyalty ID :"+loyaltyId,e);
		}
		finally
		{
			if(session!=null)
				session.close();
			session=null;
			query = null;
		}
	}
	
	private int getTier(Double statusPoint)
	{
		Iterator<Integer> iterator = Cache.getTierInfoMap().keySet().iterator();
		int tierId = 1;

		Double statusPoints = statusPoint;

		while (iterator.hasNext()) {
			TierInfoDTO infoDTO = Cache.getTierInfoMap().get(iterator.next());

			if (infoDTO.getMinValue() == null && infoDTO.getMaxValue() == null)
				continue;

			if ((infoDTO.getMinValue() != null ? infoDTO.getMinValue() <= statusPoints : true) && (infoDTO.getMaxValue() != null ? statusPoints < infoDTO.getMaxValue() : true)) {
				log.info(" Got Tier For this Status Points :" + statusPoints + " Min Value :" + infoDTO.getMinValue() + " Max Value :" + infoDTO.getMaxValue() + " Tier ID : " + infoDTO.getTierId());
				//profileTabDTO.setTierId(infoDTO.getTierId());
				tierId = infoDTO.getTierId();
			}

		}
		
		return tierId;
	}
	
	
	private List<BigDecimal> getAllMinusLoyaltyId() {
		Session session = null;
		Query query = null;
		List<BigDecimal> list = new ArrayList<BigDecimal>();
		try {
			session = HiberanteUtil.getSessionFactory().openSession();

			query = session.createSQLQuery("select distinct loyalty_id from loyalty_transaction_0 where PRE_REWARD_POINTS<0 or CUR_REWARD_POINTS<0");
			list.addAll(query.list());

			query = session.createSQLQuery("select distinct loyalty_id from loyalty_transaction_1 where PRE_REWARD_POINTS<0 or CUR_REWARD_POINTS<0");
			list.addAll(query.list());

			query = session.createSQLQuery("select distinct loyalty_id from loyalty_transaction_2 where PRE_REWARD_POINTS<0 or CUR_REWARD_POINTS<0");
			list.addAll(query.list());

			query = session.createSQLQuery("select distinct loyalty_id from loyalty_transaction_3 where PRE_REWARD_POINTS<0 or CUR_REWARD_POINTS<0");
			list.addAll(query.list());

			query = session.createSQLQuery("select distinct loyalty_id from loyalty_transaction_4 where PRE_REWARD_POINTS<0 or CUR_REWARD_POINTS<0");
			list.addAll(query.list());

			query = session.createSQLQuery("select distinct loyalty_id from loyalty_transaction_5 where PRE_REWARD_POINTS<0 or CUR_REWARD_POINTS<0");
			list.addAll(query.list());

			query = session.createSQLQuery("select distinct loyalty_id from loyalty_transaction_6 where PRE_REWARD_POINTS<0 or CUR_REWARD_POINTS<0");
			list.addAll(query.list());

			query = session.createSQLQuery("select distinct loyalty_id from loyalty_transaction_7 where PRE_REWARD_POINTS<0 or CUR_REWARD_POINTS<0");
			list.addAll(query.list());

			query = session.createSQLQuery("select distinct loyalty_id from loyalty_transaction_8 where PRE_REWARD_POINTS<0 or CUR_REWARD_POINTS<0");
			list.addAll(query.list());

			query = session.createSQLQuery("select distinct loyalty_id from loyalty_transaction_9 where PRE_REWARD_POINTS<0 or CUR_REWARD_POINTS<0");
			list.addAll(query.list());

			log.info("Got All Loyalty Ids....");

		} catch (Exception e) {
			log.error("Exception ", e);
		} finally {
			if (session != null)
				session.close();

			query = null;
		}

		return list;
	}


}
