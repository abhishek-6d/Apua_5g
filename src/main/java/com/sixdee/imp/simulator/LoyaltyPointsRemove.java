package com.sixdee.imp.simulator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dao.TableInfoDAO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.LoyaltyTransactionTabDTO;
import com.sixdee.imp.dto.TierInfoDTO;

public class LoyaltyPointsRemove {

	Logger logger=Logger.getLogger(LoyaltyPointsRemove.class);

	public void pointsRemove()
	{
		
		TableInfoDAO infoDAO=new TableInfoDAO();
		int thresholdRewardPoints=1000;
		
		List<LoyaltyTransactionTabDTO> list=null;
			try{
				
				list=getLoyalty22FebTransaction();

				if(list!=null)
				{
					for(LoyaltyTransactionTabDTO tabDTO:list)
					{
						
						Session session=null;
						Transaction transaction=null;
						String tableName=null;
						String sql=null;
						Query query=null;
						logger.info("************** # "+tabDTO.getLoyaltyID()+" # ******************");
						try{
							
							session=HiberanteUtil.getSessionFactory().openSession();
							transaction=session.beginTransaction();
							
							logger.info("ID : "+tabDTO.getId()+" Loyalty ID : "+tabDTO.getLoyaltyID()+" Reward Points : "+tabDTO.getRewardPoints()+" Status Points : "+tabDTO.getStatusPoints());
							
							// For loyalty Profile
							tableName=infoDAO.getLoyaltyProfileTable(""+tabDTO.getLoyaltyID());
							
							sql=" from "+tableName+" where loyaltyID=:lid";
							query=session.createQuery(sql)
								  .setParameter("lid",tabDTO.getLoyaltyID());
							
							LoyaltyProfileTabDTO profileTabDTO=(LoyaltyProfileTabDTO)query.uniqueResult();
							
							session.evict(profileTabDTO);
							
							logger.info("Before Loyalty ID, Reward Points : "+profileTabDTO.getRewardPoints()+" Sttaus Points "+profileTabDTO.getStatusPoints());
							
							
							
							if((profileTabDTO.getRewardPoints()-tabDTO.getRewardPoints())<thresholdRewardPoints)
							{
								String lid=profileTabDTO.getLoyaltyID()+"";
								profileTabDTO.setRewardPoints(Double.parseDouble(""+(thresholdRewardPoints+Integer.parseInt(lid.substring(lid.length()-3)))));
								
							}else{
								profileTabDTO.setRewardPoints(profileTabDTO.getRewardPoints()-tabDTO.getRewardPoints());
							}
							
							
							if(profileTabDTO.getRewardPoints()<=0) // For safe side :)
							{
								profileTabDTO.setRewardPoints(0.0);
								
							}
							logger.info("After Loyalty ID, Reward Points : "+profileTabDTO.getRewardPoints()+" Sttaus Points "+profileTabDTO.getStatusPoints());
							
							sql="Update "+infoDAO.getLoyaltyProfileDBTable(""+tabDTO.getLoyaltyID())+" set REWARD_POINTS=:points Where LOYALTY_ID=:lid";
							
							query=session.createSQLQuery(sql)
								  .setParameter("points",profileTabDTO.getRewardPoints())
								  .setParameter("lid",tabDTO.getLoyaltyID());
							
							int loyaltyUpdateCount=query.executeUpdate();
							
							
							logger.info("Loyalty Profile Update Count : "+loyaltyUpdateCount);
							
							if(loyaltyUpdateCount>0)
							{
								transaction.commit();
							}else{
								transaction.rollback();
							}
							
						}catch (Exception e) {
							logger.info(" Loyalty ID: "+tabDTO.getLoyaltyID(),e);
							if(transaction!=null)
								transaction.rollback();
						}finally{
							if(session!=null)
								session.close();
							
							session=null;
							transaction=null;
						}
						
						logger.info("=============== # "+tabDTO.getLoyaltyID()+" END # =====================");
						
						
					}//LoyaltyTransactionTabDTO
				}
				
				
			}catch (Exception e) {
				e.printStackTrace();
				logger.info("Got error : ",e);
			}finally{
				list.clear();
				list=null;
			}
			
		
		infoDAO=null;
		
	}

	public List<LoyaltyTransactionTabDTO> getLoyalty22FebTransaction()
	{
		
		Session session=null;
		List<LoyaltyTransactionTabDTO> list=new ArrayList<LoyaltyTransactionTabDTO>();
		
		List<Object[]> objList=null;
		
		try{
			
			session=HiberanteUtil.getSessionFactory().openSession();
			
			String sql="select loyalty_id,sum(reward_points),sum(status_points) from LOYALTY_POINTS_22FEB_TRAN group by loyalty_id";
			
			Query query=session.createSQLQuery(sql);
			
			objList=query.list();
			
			LoyaltyTransactionTabDTO tabDTO=null;
			
			if(list!=null)
			{
				for(Object[] objects:objList)
				{
				  tabDTO=new LoyaltyTransactionTabDTO();
				  tabDTO.setLoyaltyID(Long.parseLong(objects[0].toString()));
				  tabDTO.setRewardPoints(Double.parseDouble(objects[1].toString()));
				  tabDTO.setStatusPoints(Double.parseDouble(objects[2].toString()));
				  list.add(tabDTO);
				}
				
			}
						
			
		}catch (Exception e) {
			logger.info(e);
			
		}finally{
			if(session!=null)
				session.close();
			
			session=null;
		}
		
		return list;
		
		
	}//getLoyaltyTransaction

	
	
	public List<LoyaltyTransactionTabDTO> getLoyaltyTransaction(String index)
	{
		
		Session session=null;
		Transaction transaction=null;
		TableInfoDAO infoDAO=null;
		List<LoyaltyTransactionTabDTO> list=null;
		try{
			
			infoDAO=new TableInfoDAO();
			
			session=HiberanteUtil.getSessionFactory().openSession();
			
			String tableName=infoDAO.getLoyaltyTransactionTable("0"+index);
			
			String sql="from "+tableName+" tab where tab.statusID=5 and tab.accountNumber is not null and to_char(tab.createTime,'dd-mm-yyyy')='22-02-2014'";
			
			Query query=session.createQuery(sql);
			
			list=query.list();
			
			if(list!=null)
			{
			   logger.info("Table Name "+tableName+" Count is "+list.size());
			}
			transaction=session.beginTransaction();
			
			sql = " insert into LOYALTY_POINTS_22FEB_TRAN " + " select * from " + infoDAO.getLoyaltyTransactionDBTable("0"+index) + "" +
					" where STATUS_ID=5 and ACCOUNT_NUMBER is not null and to_char(CREATE_DATE,'dd-mm-yyyy')='22-02-2014' ";

			query = session.createSQLQuery(sql);

			logger.info(infoDAO.getLoyaltyTransactionDBTable("0"+index)+" : Insert History from Transaction " + query.executeUpdate());
			
			transaction.commit();
			
			
		}catch (Exception e) {
			logger.info(index+" For Tran ",e);
			if(transaction!=null)
				transaction.rollback();
			
		}finally{
			if(session!=null)
				session.close();
			
			session=null;
			infoDAO=null;
			transaction=null;
		}
		
		return list;
		
		
	}//getLoyaltyTransaction

}
