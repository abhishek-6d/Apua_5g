package com.sixdee.imp.simulator;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dao.TableDetailsDAO;
import com.sixdee.imp.dao.TableInfoDAO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.LoyaltyTransactionTabDTO;
import com.sixdee.imp.dto.TierInfoDTO;

public class RedeemSimulator 
{
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	Logger log = Logger.getLogger(RedeemSimulator.class);
	
	public void start()
	{
		List<BigDecimal> allLoyalty = getAllLoyaltyId();
		findingOpeningBalance(allLoyalty);
	}
	
	
	private List<BigDecimal> getAllLoyaltyId()
	{
		Session session = null;
		Query query = null;
		List<BigDecimal> list = null; 
		try
		{
			session = HiberanteUtil.getSessionFactory().openSession();
			query = session.createSQLQuery("SELECT LOYALTY_ID FROM LOYALTY_PROFILE_0");
			
			list = query.list();
			log.info("Got All Loyalty Ids....");
			
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
	
	public Map<Long, Long> findingOpeningBalance(List<BigDecimal> loayaltyIds)
	{
		Session session = null;
		Query query = null;
		Map<Long, Long> loaltyOpeningMap = new HashMap<Long, Long>();
		TableInfoDAO tableInfoDAO = new TableInfoDAO();
		List<LoyaltyTransactionTabDTO> list = null;
		boolean flag = false;
		try
		{
			
			
		
			for(ListIterator<BigDecimal> it = loayaltyIds.listIterator();it.hasNext();)
			{
				flag = false;
				try
				{
					session = HiberanteUtil.getSessionFactory().openSession();
					long key =it.next().longValue();
				 
					query = session.createQuery("FROM " + tableInfoDAO.getLoyaltyTransactionTable(key + "") +
							" WHERE monthIndex=3 and loyaltyID=? and createTime>=? and createTime<=? AND statusID IN (5,8) " +
							" ORDER BY createTime");

					Calendar cal = Calendar.getInstance();
					cal.set(2014, 02, 1,0,0,0);
					
					Calendar cal1 = Calendar.getInstance();
					cal1.set(2014, 02, 31,23,59,59);

					query.setParameter(0, key);
					query.setParameter(1, cal.getTime());
					query.setParameter(2, cal1.getTime());

					list = query.list();
					
					if(list== null || list.size()==0)
					{
						query = session.createQuery("FROM " + tableInfoDAO.getLoyaltyTransactionTable(key + "") +
								" WHERE monthIndex in (9,10,11,12,1,2) and LOYALTY_ID=? and createTime<=? AND STATUS_ID IN (5,8)" +
								" AND rownum=1 ORDER BY CREATE_DATE DESC ");
						
						
						query.setParameter(0, key);
						query.setParameter(1, cal.getTime());

						list = query.list();
						flag = true;
					}

					if(list!=null&&list.size()>0)
					   findGenuineRedeem(session,list,flag,cal.getTime());
					
					log.info("Processing DONE for loyalty id = " + key);
				}
				catch (Exception e) {
					log.error("Exception Please check == ==!!!",e);
				}finally{
					if(session!=null)
						session.close();
					session=null;
					query = null;
				}
			}
		}
		catch (Exception e) 
		{
			log.error("Exception while finding opening balance",e);
		}
		finally
		{
			if(session!=null)
				session.close();
			
			query = null;
			tableInfoDAO = null;
		}
		
		return loaltyOpeningMap;
	}
	
	private void findGenuineRedeem(Session session ,List<LoyaltyTransactionTabDTO> list,boolean flag,Date cdrDate)
	{
		TableDetailsDAO tableDetailsDAO = new TableDetailsDAO();
		Map<Integer,Double> cdrMap = new HashMap<Integer,Double>();
		int tierId=0;
		Long loyaltyId=list.get(0).getLoyaltyID();
		
		cdrMap.put(3, 0.0);
		cdrMap.put(5, 0.0);
		cdrMap.put(7, 0.0);
		if(flag)
		{
			//cdrMap.put(1, list.get(0).getLoyaltyID());
			cdrMap.put(2, list.get(0).getCurRewardPoints());
			cdrMap.put(4, list.get(0).getCurRewardPoints());
			cdrMap.put(5, list.get(0).getCurStatusPoints());
			
		}
		else
		{
			//cdrMap.put(1, list.get(0).getLoyaltyID());
			cdrMap.put(2, list.get(0).getPreRewardPoints()); // 2 -opening, 3-redeemed

			Double rewardPoints = 0.0;
			Double openingBalnce = list.get(0).getPreRewardPoints();
			Double statusbalance = list.get(0).getPreStatusPoints();
			LoyaltyTransactionTabDTO dto = null;
			for(int i=0;i<list.size();i++)
			{
				
				dto =list.get(i);
				
				statusbalance+=dto.getStatusPoints();
				if (dto.getStatusID() == 5)
				{
					openingBalnce += dto.getRewardPoints();
					rewardPoints = rewardPoints + dto.getRewardPoints();
				}
				else if (dto.getStatusID() == 8) {
					if (openingBalnce >= dto.getRewardPoints()) {
						openingBalnce = openingBalnce - dto.getRewardPoints();

						cdrMap.put(3, cdrMap.get(3) + dto.getRewardPoints());
						//LoyaltyRegisteredNumberTabDTO loyaltyRegisteredNumberTabDTO =tableDetailsDAO.getLoyaltyRegisteredNumberDetails(dto.getLoyaltyID(), dto.getSubscriberNumber());
						//tierId = getTier(dto.getCurStatusPoints());
						//log.warn(String.format("%s|%s|%s|||%s|||%s|%s|%s|'%s'|%s|%s|%s|%s|||||",dto.getLoyaltyID() ,df.format(dto.getCreateTime()),dto.getSubscriberNumber(),dto.getLoyaltyID(),tierId,tierId,"SC0000","SUCCESS",dto.getRewardPoints(),dto.getPackageId(),loyaltyRegisteredNumberTabDTO.getAccountTypeId(),dto.getChannel()));
					}
					else
					{
						cdrMap.put(3, cdrMap.get(3) + openingBalnce);
						openingBalnce = 0.0;
					}

				}
			}
			cdrMap.put(4, openingBalnce); // closing
			cdrMap.put(5, statusbalance); // status point
			cdrMap.put(7, rewardPoints);// reward Points
			
		}
		
		LoyaltyProfileTabDTO profileTabDTO = tableDetailsDAO.getLoyaltyProfile(loyaltyId);
		
		if(profileTabDTO.getTierId()==getTier(cdrMap.get(5).doubleValue()))
		{
			cdrMap.put(6, profileTabDTO.getTierId().doubleValue());
		}
		else
		{
			int newTierId= profileTabDTO.getTierId();
			TierInfoDTO infoDTO = Cache.tierInfoMap.get(newTierId);
			cdrMap.put(5,infoDTO.getMinValue().doubleValue());
			cdrMap.put(6, Double.parseDouble(newTierId+""));
		}
		
		
		log.info(cdrMap);
		insertReport(session, cdrMap,loyaltyId,cdrDate);  // inserting report
		
	}
	
	
	private void insertReport(Session session,Map<Integer,Double> cdrMap,Long loayltyID,Date cdrDate)
	{
		
		Query query = null;
		try
		{
			session.beginTransaction();
			query = session.createSQLQuery("INSERT INTO LOYALTY_REPORT1(LOYALTY_ID,OPEN_BAL, CLOSE_BAL,REDEEMED_POINTS,STATUS_POINTS,TIER_ID,CDR_DATE,REWARD_POINTS) VALUES(?,?,?,?,?,?,?,?)");
			query.setParameter(0, loayltyID);
			query.setParameter(1, cdrMap.get(2));
			query.setParameter(2, cdrMap.get(4));
			query.setParameter(3, cdrMap.get(3));
			query.setParameter(4, cdrMap.get(5));
			query.setParameter(5, cdrMap.get(6).intValue());
			query.setParameter(6, cdrDate);
			query.setParameter(7, cdrMap.get(7));
			
			int i = query.executeUpdate();
			session.getTransaction().commit();
			log.info("Report inserted for loyaltyid = "+cdrMap.get(1)+" is ="+i);
			
		}
		catch (Exception e) {
			log.info("Exception while inserting report",e);
		}
		finally
		{
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

}
