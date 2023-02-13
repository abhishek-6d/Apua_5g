package com.sixdee.imp.simulator;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dao.TableInfoDAO;
import com.sixdee.imp.dto.CategoryDetailsDTO;
import com.sixdee.imp.dto.LoyaltyTransactionTabDTO;
import com.sixdee.imp.dto.TierInfoDTO;

public class ManageRewardPoints 
{
	DateFormat df1 = new SimpleDateFormat("MM");
	private Logger log = Logger.getLogger(ManageRewardPoints.class);
	
	public void start()
	{
		findDefectiveLoyaltyIds();
	}
	
	public void findDefectiveLoyaltyIds()
	{
		Session session = null;
		Query query = null;
		List<BigDecimal> list = new ArrayList<BigDecimal>();
		try
		{
			session= HiberanteUtil.getSessionFactory().openSession();
			for(int i =0 ;i<=9 ; i++)
			{
				query = session.createSQLQuery("select distinct loyalty_id from loyalty_transaction_"+i+" where CHANNEL='6'");
				list.addAll(query.list());
			}
			
		}
		catch (Exception e) {
			log.error("Error while finding defective loyalty ids ",e);
		}
		finally
		{
			if(session!=null)
				session.close();
			query=null;
		}
		
		for(BigDecimal lId : list)
		{
			try
			{
				findDefectiveRewards(lId);
			}
			catch (Exception e) {
				log.error("Exception for loyalty id ="+lId,e);
			}
		}
	}
	
	public void findDefectiveRewards(BigDecimal lId)
	{
		Session session = null;
		Query query = null;
		TableInfoDAO info = new TableInfoDAO();
		List<LoyaltyTransactionTabDTO> list = new ArrayList<LoyaltyTransactionTabDTO>();
		List<BigDecimal> ccGuiIds = null;
		ccGuiIds = findCCGuiIds(lId);
		log.info("for loyalty id = "+lId+" number of record found is = "+ccGuiIds.size());
		for(BigDecimal ccId : ccGuiIds)
		{
			try
			{
				
				session = HiberanteUtil.getSessionFactory().openSession();
				query = session.createQuery("from "+info.getLoyaltyTransactionTable(lId.toString())+" where loyaltyID=? and channel in ('5')" +
						" and id>? and statusID=5 " +
						" order by id ");
				query.setParameter(0, Long.parseLong(lId.toString()));
				query.setParameter(1, Long.parseLong(ccId.toString()));
				query.setMaxResults(1);
				list.addAll(query.list());
				
			}
			catch (Exception e) {
				log.error("Exception while finding defective rewards ",e);
			}
			finally
			{
				if(session!=null)
					session.close();
				query = null;
			}
		
		}
		
		
		
		
		double pointsToAdd = 0.0;
		double volume = 0.0;
		int tier =0;
		double reqRewardPoints = 0.0;
		CategoryDetailsDTO detailsDTO = null;
		for(LoyaltyTransactionTabDTO dto : list)
		//if(list.size()==2)
		{
			//LoyaltyTransactionTabDTO dto = list.get(1); 
			if(dto.getSubscriberNumber()!=null) // Prepaid
			{
				volume = dto.getStatusPoints()/0.01;
				tier = getTier(dto.getPreStatusPoints());
				
				List<CategoryDetailsDTO> list2=Cache.tierCategoryDetailsMap.get(tier).get(new Integer(3));
				
				detailsDTO=list2.get(0);
				log.info("volume = "+volume+" and tier = "+ tier);
				
				reqRewardPoints = detailsDTO.getRewardPoints()*volume;
			}
			else   //PostPaid
			{
				volume = dto.getStatusPoints()/10;
				tier = getTier(dto.getPreStatusPoints());
				
				List<CategoryDetailsDTO> list2=Cache.tierCategoryDetailsMap.get(tier).get(new Integer(2));
				detailsDTO=list2.get(0);
				log.info("volume = "+volume+" and tier = "+ tier);
				reqRewardPoints = detailsDTO.getRewardPoints()*volume;
				
			}
			
			double points = dto.getRewardPoints()-reqRewardPoints;
			pointsToAdd = pointsToAdd + points;
			log.info("updating "+reqRewardPoints+" reward point for loyalty id = "+dto.getLoyaltyID());
			updateBalance(dto, "REWARD_POINTS="+reqRewardPoints, info.getLoyaltyTransactionDBTable(dto.getLoyaltyID()+""));
			/*
			if(pointsToAdd>0)
			{
			//	updating transaction 
				List<LoyaltyTransactionTabDTO> listOfDTO = findingOpeningBalance(dto.getLoyaltyID());
				
				new LoyaltyPointsLevel().pointCalculation(listOfDTO, dto.getLoyaltyID(), pointsToAdd, 0, 0);
			}*/
		}
		
		if(pointsToAdd>0)
		{
		//	updating transaction 
			List<LoyaltyTransactionTabDTO> listOfDTO = findingOpeningBalance(Long.parseLong(lId.toString()));
			
			new LoyaltyPointsLevel().pointCalculation(listOfDTO, Long.parseLong(lId.toString()), pointsToAdd, 0, 0);
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
	
	public List<LoyaltyTransactionTabDTO> findingOpeningBalance(long loayaltyIds)
	{
		Session session = null;
		Query query = null;
		TableInfoDAO tableInfoDAO = new TableInfoDAO();
		List<LoyaltyTransactionTabDTO> list = null;
		try
		{
			int counter=0;
			
		//	for(ListIterator<BigDecimal> it = loayaltyIds.listIterator();it.hasNext();)
		//	{
		//		counter++;
				
				Long loyaltyID=loayaltyIds;
				
				try
				{
					session = HiberanteUtil.getSessionFactory().openSession();
					//loyaltyID =it.next().longValue();
					
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
				 
				
				log.info("Loyalty ID : " + loyaltyID+" Processing completed");// , Remaing Loyalty ID Count is "+(loayaltyIds.size()-counter));
		//	}
			
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
			//list=null;
		}
		
		
		return list;
		
	}
	
	public List<BigDecimal> findCCGuiIds(BigDecimal loyaltyId)
	{
		Session session = null;
		Query query = null;
		TableInfoDAO info = new TableInfoDAO();
		List<BigDecimal> list = null;
		try
		{
			session = HiberanteUtil.getSessionFactory().openSession();
			query = session.createSQLQuery("SELECT ID FROM "+info.getLoyaltyTransactionDBTable(loyaltyId.toString())+" WHERE LOYALTY_ID=? " +
					" and CHANNEL='6' ORDER BY ID" );
			query.setParameter(0, Long.parseLong(loyaltyId.toString()));
			
			list = query.list();
			
		}
		catch (Exception e) {
			log.error("Exception in findCCGuiIds",e);
		}
		finally
		{
			if(session!=null)
				session.close();
		}
		
		return list;
	}

}
