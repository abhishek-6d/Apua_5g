package com.sixdee.imp.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.tomcat.modules.aaa.RealmBase;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.sixdee.imp.common.util.HiberanteUtilCMS;
import com.sixdee.imp.common.util.HiberanteUtilRule;
import com.sixdee.imp.dto.EligibleSubscriberDTO;
import com.sixdee.imp.dto.MarketingPlanDTO;
import com.sixdee.imp.dto.UserDTO;
import com.sixdee.imp.service.serviceDTO.resp.EligibleSubscriberDetails;

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
 * <td>September 04,2013 07:46:17 PM</td>
 * <td>Somesh Soni</td>
 * </tr>
 * </table>
 * </p>
 */




public class EligibleSubscriberDAO 
{
	private Logger LOG = Logger.getLogger(EligibleSubscriberDAO.class);
	
	public List<Object> getMarketingPlan(EligibleSubscriberDTO eligibleSubscriberDTO) throws Exception
	{
		LOG.info("getMarketingPlan() Method =>> ");
		TableInfoDAO dao = new TableInfoDAO();
		Session session = null;
		Criteria ctr = null;
		List<Object> marketingIds = null;
		List<Object> finalIds=null;
		int month =0;
		int count=0;
		try
		{
			session = HiberanteUtilRule.getSessionFactory().openSession();
			
			if(eligibleSubscriberDTO.getMonth()!=null)
			{
			month=Integer.parseInt(eligibleSubscriberDTO.getMonth());
			LOG.info("Seleted Month:"+month);
			ctr = session.createCriteria(dao.getRuleTableName(month+""));
			ctr.setProjection(Projections.distinct(Projections.property("marketingPlanId")));
			ctr.add(Restrictions.eq("da", eligibleSubscriberDTO.getMsisdn()));
			ctr.add(Restrictions.eq("type", "Promotion"));
			marketingIds = ctr.list();
			finalIds=marketingIds;
			}
			else
			{
			finalIds=new ArrayList<Object>();
			Calendar cal = Calendar.getInstance();
			int currMonth=Calendar.getInstance().get(Calendar.MONTH)+1;
			do
			{
			LOG.info("Month:"+currMonth);	
			ctr = session.createCriteria(dao.getRuleTableName(currMonth+""));
			ctr.setProjection(Projections.distinct(Projections.property("marketingPlanId")));
			ctr.add(Restrictions.eq("da", eligibleSubscriberDTO.getMsisdn()));
			ctr.add(Restrictions.eq("type", "Promotion"));
			marketingIds=ctr.list();
			for(Object o : marketingIds)
				finalIds.add(o);
			System.out.println("marketingIds"+finalIds);
			cal.add(Calendar.MONTH, -1);
			currMonth=cal.get(Calendar.MONTH)+1; 
			count+=1;
			}while (count<3);
			}
			LOG.info("ALL marketing plans == \n"+finalIds);
		}
		finally
		{
			if(session!=null)
				session.close();
			dao = null;
			session = null;
			ctr = null;
		}
		
		return finalIds;
	}
	
	public List<EligibleSubscriberDetails> getMarketingPlanDetails(List<Object> marketingIds) throws Exception
	{
		Session session = null;
		Criteria ctr = null;
		List<MarketingPlanDTO> list = null;
		List<EligibleSubscriberDetails> eligibleSubscriberList = new ArrayList<EligibleSubscriberDetails>();
		EligibleSubscriberDetails details = null;
		try
		{
			List<Integer> intListMKT = new ArrayList<Integer>();
			for(Object o : marketingIds)
				intListMKT.add(Integer.parseInt(o.toString()));
			
			session = HiberanteUtilCMS.getSessionFactory().openSession();
			
			ctr = session.createCriteria(MarketingPlanDTO.class);
			ctr.add(Restrictions.in("marketingPlanId", intListMKT));
			
			list = ctr.list();
			
			for(MarketingPlanDTO dto : list)
			{
				details = new EligibleSubscriberDetails();
				details.setMarketingPlanId(dto.getMarketingPlanId());
				details.setMarketingPlanName(dto.getMarketingPlanName());
				details.setMarketingPlanDesc(dto.getMarketingPlanDesc());
				details.setMarketingPlanObj(dto.getMarketingPlanObj());
				details.setSmsKeyword(dto.getSmsKeyword());
				details.setDialCode(dto.getDialCode());
				eligibleSubscriberList.add(details);
				LOG.info("MARKETING PLAN ID:"+details.getMarketingPlanId());
				LOG.info("MARKETING PLAN NAME:"+details.getMarketingPlanName());
				LOG.info("MARKETING PLAN DESC:"+details.getMarketingPlanDesc());
				LOG.info("MARKETING PLAN OBJ:"+details.getMarketingPlanObj());
				LOG.info("SMS KEYWORD:"+details.getSmsKeyword());
				LOG.info("DIAL CODE:"+details.getDialCode());
			}
			
		}
		finally
		{
			if(session!=null)
				session.close();
			session = null;
			ctr = null;
			list = null;
			details = null;
		}
		
		return eligibleSubscriberList;
	}
	
	public UserDTO validateUser(EligibleSubscriberDTO eligibleSubscriberDTO)
	{
		Session session = null;
		Criteria ctr = null;
		UserDTO userDTO = null;
		try
		{
			
			session = HiberanteUtilCMS.getSessionFactory().openSession();
			ctr = session.createCriteria(UserDTO.class);
			ctr.add(Restrictions.eq("userName", eligibleSubscriberDTO.getUserName()));
			//ctr.add(Restrictions.eq("password", RealmBase.digest(eligibleSubscriberDTO.getPassword(), "MD5")));
			ctr.add(Restrictions.eq("password", eligibleSubscriberDTO.getPassword()));
			
			userDTO = (UserDTO)ctr.uniqueResult();
			LOG.info("Username:"+eligibleSubscriberDTO.getUserName()+" Password:"+eligibleSubscriberDTO.getPassword()+" | UserDTO::"+userDTO);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			if(session!=null)
				session.close();
		}
		
		return userDTO;
	}

	public UserDTO validateUser(EligibleSubscriberDTO eligibleSubscriberDTO,
			boolean b) {

		Session session = null;
		Criteria ctr = null;
		UserDTO userDTO = null;
		try
		{
			
			session = HiberanteUtilCMS.getSessionFactory().openSession();
			ctr = session.createCriteria(UserDTO.class);
			ctr.add(Restrictions.eq("userName", eligibleSubscriberDTO.getUserName()));
			ctr.add(Restrictions.eq("password",eligibleSubscriberDTO.getPassword()));
			
			userDTO = (UserDTO)ctr.uniqueResult();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			if(session!=null)
				session.close();
		}
		
		return userDTO;
	}

}

