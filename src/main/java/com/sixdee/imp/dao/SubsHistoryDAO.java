package com.sixdee.imp.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;


import com.sixdee.imp.common.util.HiberanteUtilCMS;
import com.sixdee.imp.common.util.HiberanteUtilRule;
import com.sixdee.imp.dto.MarketingPlanDTO;
import com.sixdee.imp.dto.RuleCdrDTO;
import com.sixdee.imp.dto.SubsHistoryDTO;
import com.sixdee.imp.service.serviceDTO.resp.SubscriberHistoryDTO;

/**
 * 
 * @author Somesh Soni
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
 * <td>September 13,2013 12:46:40 PM</td>
 * <td>Somesh Soni</td>
 * </tr>
 * </table>
 * </p>
 */




public class SubsHistoryDAO 
{
	private Logger log = Logger.getLogger(SubsHistoryDAO.class);
	public List<SubscriberHistoryDTO> getTransactionHistory(SubsHistoryDTO historyDTO)
	{
		log.info(historyDTO.getTransactionId()+" Transaction id processing");
		DateFormat df = new SimpleDateFormat("ddMMyyyy");
		Session session = null;
		Criteria ctr = null;
		TableInfoDAO dao = new TableInfoDAO();
		List<RuleCdrDTO> list = null;
		List<RuleCdrDTO> finallist = null;
		SubscriberHistoryDTO subscriberHistoryDTO = null;
		List<SubscriberHistoryDTO> subscriberList = new ArrayList<SubscriberHistoryDTO>();
		Set<Integer> marketingPlanId = new HashSet<Integer>();
		int count=0;
		try
		{
			session = HiberanteUtilRule.getSessionFactory().openSession();
			if(historyDTO.getFromDate()!=null && historyDTO.getToDate()!=null)
			{
				finallist=new ArrayList<RuleCdrDTO>();
				Calendar fromcal = Calendar.getInstance();
				fromcal.setTime(historyDTO.getFromDate());

				Calendar toCal = Calendar.getInstance();
				toCal.setTime(historyDTO.getToDate());
				do
				{
				int month=fromcal.get(Calendar.MONTH);
				month+=1;
				ctr = session.createCriteria(dao.getRuleTableName(month+""));
				ctr.add(Restrictions.eq("da", historyDTO.getMsisdn()));
				ctr.add(Restrictions.eq("cdrStatus", "FINAL"));
				list = ctr.list();
				for(RuleCdrDTO l : list)
					finallist.add(l);
				fromcal.add(Calendar.MONTH, 1);
				}while(fromcal.getTime().equals(toCal.getTime()) || fromcal.getTime().before(toCal.getTime()));
				
				log.info("FINAL LIST:::"+finallist);
				
			}
			else if(historyDTO.getMonth()!=null)
			{
				ctr = session.createCriteria(dao.getRuleTableName(historyDTO.getMonth()));
				ctr.add(Restrictions.eq("da", historyDTO.getMsisdn()));
				ctr.add(Restrictions.eq("cdrStatus", "FINAL"));
				finallist = ctr.list();
				log.info("FINAL LIST:::"+finallist);
			}
			else
			{
				finallist=new ArrayList<RuleCdrDTO>();
				Calendar cal = Calendar.getInstance();
				int currMonth=Calendar.getInstance().get(Calendar.MONTH)+1;
				do
				{
				log.info("Month:"+currMonth);	
				ctr = session.createCriteria(dao.getRuleTableName(currMonth+""));
				ctr.add(Restrictions.eq("da", historyDTO.getMsisdn()));
				ctr.add(Restrictions.eq("cdrStatus", "FINAL"));
				list = ctr.list();
				for(RuleCdrDTO l : list)
					finallist.add(l);
				cal.add(Calendar.MONTH, -1);
				currMonth=cal.get(Calendar.MONTH)+1; 
				count+=1;
				}while (count<3);
				
				log.info("FINAL LIST:::"+finallist);
			}
			for(RuleCdrDTO dto:finallist)
			{
				log.info("dto.getMarketingPlanId()"+dto.getMarketingPlanId());
				subscriberHistoryDTO = new SubscriberHistoryDTO();
				subscriberHistoryDTO.setMsisdn(dto.getDa());
				subscriberHistoryDTO.setActionKey(dto.getActionkey());
				subscriberHistoryDTO.setMarketingPlan(dto.getMarketingPlanId());
				
				if(dto.getMarketingPlanId()!=null)
					marketingPlanId.add(Integer.parseInt(dto.getMarketingPlanId()));
				
				subscriberHistoryDTO.setPromotionName(dto.getScheduleName());
				subscriberHistoryDTO.setPromotionDate(df.format(dto.getPromotiondate()));
				subscriberHistoryDTO.setMessage(dto.getMessageID());
				subscriberHistoryDTO.setAmount(dto.getFreebiesAmnt());
				subscriberHistoryDTO.setValidity(dto.getFreebieValdity());
				subscriberHistoryDTO.setBonus(dto.getBonus());
				subscriberHistoryDTO.setChargingIndicator(dto.getChargingIndicator());
				subscriberHistoryDTO.setTypeOfAction(dto.getType());
				subscriberHistoryDTO.setMicroSegmentId(dto.getMicroSegment());
				
				if(dto.getStatus()!=null && dto.getStatus().equals("0"))
					subscriberHistoryDTO.setBalanceDeduction(dto.getBalanceDeduction());
				
				subscriberList.add(subscriberHistoryDTO);
			}
			
			historyDTO.setMarketingPlanIds(marketingPlanId);
			
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally
		{
			if(session!=null)
				session.close();
			session = null;
			ctr = null;
			dao = null;
			list = null;
			subscriberHistoryDTO = null;
			marketingPlanId = null;
		}
		
		return subscriberList;
	}
	
	
	public Map<Integer, MarketingPlanDTO> getMarketingPlanDetails(Set<Integer> marketingPlanIds)
	{
		Session session = null;
		Criteria ctr = null;
		List<MarketingPlanDTO> list = null;
		Map<Integer, MarketingPlanDTO> maketingPlanMap = new HashMap<Integer, MarketingPlanDTO>();
		try
		{
			session = HiberanteUtilCMS.getSessionFactory().openSession();
			ctr = session.createCriteria(MarketingPlanDTO.class);
			ctr.add(Restrictions.in("marketingPlanId", marketingPlanIds));
			
			list = ctr.list();
			
			for(MarketingPlanDTO dto : list)
				maketingPlanMap.put(dto.getMarketingPlanId(), dto);
			
			
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally
		{
			if(session!=null)
				session.close();
			session = null;
			ctr = null;
			list = null;
			
		}
		
		return maketingPlanMap;
	}

}
