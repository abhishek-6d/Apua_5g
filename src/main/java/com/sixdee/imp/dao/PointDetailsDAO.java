package com.sixdee.imp.dao;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.Type;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.LoyaltyTransactionTabDTO;
import com.sixdee.imp.dto.PointDetailsDTO;
import com.sixdee.imp.dto.PointDetailsInfoDTO;

/**
 * 
 * @author Paramesh
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
 * <td>July 25,2013 03:12:10 PM</td>
 * <td>Paramesh</td>
 * </tr>
 * </table>
 * </p>
 */




public class PointDetailsDAO {

	Logger logger=Logger.getLogger(PointDetailsDAO.class);
	private static DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
	
	public void pointsExpiryRowCount(GenericDTO genericDTO,boolean isRewardPoints,boolean isStatusPoints)
	{
		PointDetailsDTO pointDetailsDTO=(PointDetailsDTO)genericDTO.getObj();
		Session session=null;
		Transaction transaction=null;
		TableInfoDAO infoDAO=null;
		try{
			
			infoDAO=new TableInfoDAO();
			session=HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			
			String table=infoDAO.getLoyaltyTransactionTable(pointDetailsDTO.getLoyaltyID()+"");
			long start=System.currentTimeMillis();
			Criteria criteria=session.createCriteria(table);
			criteria.add(Restrictions.eq("loyaltyID", pointDetailsDTO.getLoyaltyID()));
			if(isRewardPoints)
			  criteria.add(Restrictions.gt("rewardPoints",0.0));
			if(isStatusPoints)
			  criteria.add(Restrictions.gt("statusPoints",0.0));
			
			criteria.add(Restrictions.between("createTime",pointDetailsDTO.getCalculatedFromDate(),pointDetailsDTO.getCalculatedToDate()));
			
			logger.info("Loyalty ID "+pointDetailsDTO.getLoyaltyID());
			logger.info("From Date "+pointDetailsDTO.getCalculatedFromDate());
			logger.info("To Date "+pointDetailsDTO.getCalculatedToDate());
			
			
			ProjectionList projectionList= Projections.projectionList();
			
			if(Cache.isOracleDB)
			{
				/*
				 * projectionList.add(Projections.sqlGroupProjection(
				 * "to_char({alias}.CREATE_DATE,'DD/MM/YYYY') CT",
				 * "to_char({alias}.CREATE_DATE,'DD/MM/YYYY')", new String[]{"CT"}, new Type[]
				 * {Hibernate.STRING}));
				 */} else {
				/*
				 * projectionList.add(Projections.sqlGroupProjection(
				 * "date_format({alias}.CREATE_DATE,'%d/%m/%Y') CT",
				 * "date_format({alias}.CREATE_DATE,'%d/%m/%Y')", new String[]{"CT"}, new Type[]
				 * {Hibernate.STRING}));
				 */}
			
			projectionList.add(Projections.rowCount());
			
			criteria.setProjection(projectionList);
			
			
			//criteria.setCacheable(true);
			
			List<Object[]> list= criteria.list();
			System.out.println("_____________________- "+(System.currentTimeMillis()-start));
			if(list!=null)
			{
				for(Object[] obj:list)
				{
					pointDetailsDTO.setRowCount(pointDetailsDTO.getRowCount()+Integer.parseInt(obj[1].toString()));
				}
			}
			 
			logger.info(" ROW Count "+pointDetailsDTO.getRowCount());
			
			transaction.commit();
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(session!=null)
				session.close();
			session=null;
			transaction=null;
			infoDAO=null;
		}
		
		
	}//rewardPoints
	
	
	public void pointsExpiryDetails(PointDetailsDTO pointDetailsDTO,boolean isRewardPoints,boolean isStatusPoints)
	{
		Session session=null;
		Transaction transaction=null;
		TableInfoDAO infoDAO=null;
		SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
		String sql=null;
		BigDecimal rewardPoints=null;
		try{
			
			int rewardPointsExpirtPeriod=Integer.parseInt(Cache.getConfigParameterMap().get("REWARD_POINTS_EXPIRY_PERIOD").getParameterValue().trim());
			int statusPointsExpirtPeriod=Integer.parseInt(Cache.getConfigParameterMap().get("TIER_EXPIRY_PERIOD").getParameterValue().trim());
			 //logger.info(">>>>>>>>>>rewardpointexpiryperiod>>>>>>>"+rewardPointsExpirtPeriod);
			  Query query = null;
			  List<BigDecimal> list = null;
			  int RewardpointexpPeriod=rewardPointsExpirtPeriod;
			  rewardPointsExpirtPeriod=rewardPointsExpirtPeriod/12;
			//  logger.info(">>>rewardPointsExpirtPeriod>>>"+rewardPointsExpirtPeriod);
			  Calendar now = Calendar.getInstance();
			    now.add(Calendar.MONTH, 1);
			    now.add(Calendar.YEAR, -rewardPointsExpirtPeriod);
			    int n = now.get(Calendar.MONTH);
			    String b = now.get(Calendar.YEAR)+"";
			    Date myDate= new Date();
				int quarter = (myDate.getMonth() / 3) + 1;
			    String v1=null,v2=null,v3=null;
			    switch (quarter){
			    case 1:
			    	v1=b+"0"+1+"";
		            v2=b+"0"+2+"";
		            v3=b+"0"+3+"";
		            break;
			    case 2:
			    	v1=b+"0"+4+"";
		            v2=b+"0"+5+"";
		            v3=b+"0"+6+"";
		        break;
			    case 3:
			    	v1=b+"0"+7+"";
		            v2=b+"0"+8+"";
		            v3=b+"0"+9+"";
		        break;
			    case 4:
			    	v1=b+10+"";
			        v2=b+11+"";
			        v3=b+12+"";
			        break;
			     default:logger.info("");
			    }
			    session = HiberanteUtil.getSessionFactory().openSession();
			    transaction=session.beginTransaction();
			   // logger.info("loyality id is:>>>>>"+pointDetailsDTO.getLoyaltyID());
				   query = session.createSQLQuery("SELECT SUM(REWARD_POINTS) FROM  LOYALTY_TRAN_SUMMARY_MONTHLY "+
				     " WHERE PARTITION_INDEX in ('"+v1+"','"+v2+"','"+v3+"') AND REWARD_POINTS>0 AND LOYALTY_ID =?");
				   query.setParameter(0, pointDetailsDTO.getLoyaltyID());
				   logger.info(query);
				   rewardPoints= (BigDecimal) query.uniqueResult();
				  
				  
				   List<PointDetailsInfoDTO> expiryList=null;
					
					if(pointDetailsDTO.getExpiryPointsList()==null)
						expiryList=new ArrayList<PointDetailsInfoDTO>();
					else
						expiryList=pointDetailsDTO.getExpiryPointsList();
					
					PointDetailsInfoDTO infoDTO=new PointDetailsInfoDTO();
					//logger.info(">>>>>expiry date>11>>>>"+dateFormat.format(pointDetailsDTO.getCalculatedToDate()));
					pointDetailsDTO.getCalculatedToDate().setMonth(pointDetailsDTO.getCalculatedToDate().getMonth()+RewardpointexpPeriod);
					
					//logger.info(">>>>>expiry date>>>>>"+dateFormat.format(pointDetailsDTO.getCalculatedToDate()));
					infoDTO.setExpiryDate(dateFormat.format(pointDetailsDTO.getCalculatedToDate()));
					infoDTO.setRewardPoints("0");
					
					//logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>"+rewardPoints);
						   if(rewardPoints!=null)
							infoDTO.setRewardPoints(rewardPoints+"");
						   
					logger.info("Reward points EXPIRY:>>>>"+infoDTO.getRewardPoints());
					
					expiryList.add(infoDTO);
					
					pointDetailsDTO.setExpiryPointsList(expiryList);
				
			/*infoDAO=new TableInfoDAO();
			session=HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			
			String table=infoDAO.getLoyaltySummaryTransactionTable();
			long start=System.currentTimeMillis();
			
			if(Cache.isOracleDB)
			{
			sql="select ceil(to_char(CREATE_DATE,'mm')/3),sum(REWARD_POINTS) from "+table+" " +
					   " WHERE LOYALTY_ID=? and CREATE_DATE between ? and ? and REWARD_POINTS>0 " +
					   " group by ceil(to_char(CREATE_DATE,'mm')/3) "; 
			}
			else
			{
			sql="select ceil(DATE_FORMAT(CREATE_DATE,'%m')/3),sum(REWARD_POINTS) from "+table+" " +
					  " WHERE LOYALTY_ID=? and CREATE_DATE between ? and ? and REWARD_POINTS>0 " +
					  " group by ceil(DATE_FORMAT(CREATE_DATE,'%m')/3) "; 
			}
			
			sql=
			
			Query query=session.createSQLQuery(sql);
			int index=0;
			query.setParameter(index++,pointDetailsDTO.getLoyaltyID());
			query.setParameter(index++,pointDetailsDTO.getCalculatedFromDate());
			query.setParameter(index++,pointDetailsDTO.getCalculatedToDate());
			
			List<Object[]> list=query.list();
			
			
			List<PointDetailsInfoDTO> expiryList=null;
			
			if(pointDetailsDTO.getExpiryPointsList()==null)
				expiryList=new ArrayList<PointDetailsInfoDTO>();
			else
				expiryList=pointDetailsDTO.getExpiryPointsList();
			
			PointDetailsInfoDTO infoDTO=new PointDetailsInfoDTO();
			
			pointDetailsDTO.getCalculatedToDate().setMonth(pointDetailsDTO.getCalculatedToDate().getMonth()+rewardPointsExpirtPeriod);
			infoDTO.setExpiryDate(dateFormat.format(pointDetailsDTO.getCalculatedToDate()));
			infoDTO.setRewardPoints("0");
			
			if(list!=null)
			{
				for(Object[] obj:list)
				{
					infoDTO.setRewardPoints(obj[1].toString());
				}
			}
			
			expiryList.add(infoDTO);
			
			pointDetailsDTO.setExpiryPointsList(expiryList);*/
			
			transaction.commit();
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(session!=null)
				session.close();
			session=null;
			transaction=null;
			infoDAO=null;
		}
		
		
	}//rewardPoints
	
	
	public void pointsExpiryDetails1(GenericDTO genericDTO,boolean isRewardPoints,boolean isStatusPoints)
	{
		PointDetailsDTO pointDetailsDTO=(PointDetailsDTO)genericDTO.getObj();
		Session session=null;
		Transaction transaction=null;
		TableInfoDAO infoDAO=null;
		SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
		try{
			
			int rewardPointsExpirtPeriod=Integer.parseInt(Cache.getConfigParameterMap().get("REWARD_POINTS_EXPIRY_PERIOD").getParameterValue().trim());
			int statusPointsExpirtPeriod=Integer.parseInt(Cache.getConfigParameterMap().get("TIER_EXPIRY_PERIOD").getParameterValue().trim());
			
			infoDAO=new TableInfoDAO();
			session=HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			
			String table=infoDAO.getLoyaltySummaryTransactionTable();
			long start=System.currentTimeMillis();
			Criteria criteria=session.createCriteria(table);
			criteria.add(Restrictions.eq("loyaltyID", pointDetailsDTO.getLoyaltyID()));
			if(isRewardPoints)
			  criteria.add(Restrictions.gt("rewardPoints",0.0));
			if(isStatusPoints)
			  criteria.add(Restrictions.gt("statusPoints",0.0));
			
			criteria.add(Restrictions.between("createTime",pointDetailsDTO.getCalculatedFromDate(),pointDetailsDTO.getCalculatedToDate()));
			
			logger.info("Loyalty ID "+pointDetailsDTO.getLoyaltyID());
			logger.info("From Date "+pointDetailsDTO.getCalculatedFromDate());
			logger.info("To Date "+pointDetailsDTO.getCalculatedToDate());
			
			
			ProjectionList projectionList= Projections.projectionList();
			
			if(Cache.isOracleDB)
			{
				/*
				 * projectionList.add(Projections.sqlGroupProjection(
				 * "to_char({alias}.CREATE_DATE,'DD/MM/YYYY') CT",
				 * "to_char({alias}.CREATE_DATE,'DD/MM/YYYY')", new String[]{"CT"}, new Type[]
				 * {Hibernate.STRING}));
				 */} else {
				/*
				 * projectionList.add(Projections.sqlGroupProjection(
				 * "date_format({alias}.CREATE_DATE,'%d/%m/%Y') CT",
				 * "date_format({alias}.CREATE_DATE,'%d/%m/%Y')", new String[]{"CT"}, new Type[]
				 * {Hibernate.STRING}));
				 */}
			
			if(isRewardPoints)
				projectionList.add(Projections.sum("rewardPoints"));
			if(isStatusPoints)
				projectionList.add(Projections.sum("statusPoints"));
			
			criteria.setProjection(projectionList);
			
			
			//criteria.setCacheable(true);
			
			List<Object[]> list= criteria.list();
			System.out.println("_____________________- "+(System.currentTimeMillis()-start));
			
			/*String table=infoDAO.getLoyaltyTransactionDBTable(pointDetailsDTO.getLoyaltyID()+"");
			
			Query query=session.createSQLQuery("select * from "+table+" WHERE LOYALTY_ID=?");
			query.setParameter(0,pointDetailsDTO.getLoyaltyID());
			
			List<Object[]> list=query.list();*/
			
			List<PointDetailsInfoDTO> expiryList=null;
			
			if(pointDetailsDTO.getExpiryPointsList()==null)
				expiryList=new ArrayList<PointDetailsInfoDTO>();
			else
				expiryList=pointDetailsDTO.getExpiryPointsList();
			
			PointDetailsInfoDTO infoDTO=null;
			if(list!=null)
			{
				for(Object[] obj:list)
				{
					//System.out.println(obj[0].toString()+"  "+obj[1].toString()+"  "+obj[2].toString());
					int index=0;
					infoDTO=new PointDetailsInfoDTO();
					
					Date date=dateFormat.parse(obj[index++].toString());
					String expiryDate=null;
					
					if(isRewardPoints)
					  date.setMonth(date.getMonth()+rewardPointsExpirtPeriod);
					if(isStatusPoints)
					  date.setMonth(date.getMonth()+statusPointsExpirtPeriod);
					
					infoDTO.setExpiryDate(dateFormat.format(date));
					
					if(isRewardPoints)
						infoDTO.setRewardPoints(obj[index++].toString());
					if(isStatusPoints)
						infoDTO.setStatusPoints(obj[index++].toString());	
					
					expiryList.add(infoDTO);
					
				}
			}
			
			pointDetailsDTO.setExpiryPointsList(expiryList);
			
			transaction.commit();
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(session!=null)
				session.close();
			session=null;
			transaction=null;
			infoDAO=null;
		}
		
		
	}//rewardPoints
	
	
	
}
