package com.sixdee.imp.dao;

/**
 * 
 * @author Ananth
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
 * <td>December 08,2015 07:13:59 PM</td>
 * <td>Ananth</td>
 * </tr>
 * </table>
 * </p>
 */



import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.service.httpcall.GetAccountNumber;

public class ChangeTierDAO {
	
	private Logger logger = Logger.getLogger(GetAccountNumber.class);
	/*public ChangeTierDTO changeTier(ChangeTierDTO changeTierDTO) {
		Session session=null;
		String sql=null;
		Transaction transaction=null;
		String msisdn=changeTierDTO.getMsisdn();
		String Tier=changeTierDTO.getTier();
		int isRoyalFamily=changeTierDTO.getIsRoyalFamily();
		try
		{
			logger.info("Request reached in BL with Msisdn:"+msisdn+"RequestId"+changeTierDTO.getRequestId());			
			session=HiberanteUtil.getSessionFactory().openSession();					
			sql="SELECT TIER_ID,LOYALTY_ID from LOYALTY_PROFILE_0 where CONTACT_NUMBER=?";			
			Query query=session.createSQLQuery(sql);
			query.setParameter(0,msisdn);
			Object[] objects=(Object[])query.uniqueResult();
			
			if(objects!=null&&objects.length>0)
			{
				logger.info("currenttierid"+Integer.parseInt(objects[0].toString()));
				logger.info("loyalityId"+objects[1].toString());
				int currentTierId=Integer.parseInt(objects[0].toString());
				String loyalityId=objects[1].toString();
				String sql1=" SELECT ID from TIER_INFO WHERE TIER_NAME=?";
				Query query1=session.createSQLQuery(sql1);
				query1.setParameter(0, Tier.toUpperCase());
				List<Object[]> list=query1.list();
//				TierInfoDTO tierInfoDTO=new TierInfoDTO();
//				tierInfoDTO=list.get(0);
				logger.info(list.size());
				if(list.size()>0)
				{
				String curretnTierId1=list.get(0)+"";
				int tierId=Integer.parseInt(curretnTierId1);
				
				 transaction=session.beginTransaction();
				String sql2="UPDATE LOYALTY_PROFILE_0 SET TIER_ID=?,ISROYALFAMILY=? WHERE LOYALTY_ID=? ";
				Query query2=session.createSQLQuery(sql2);
				query2.setParameter(0, tierId);
				query2.setParameter(1, changeTierDTO.getIsRoyalFamily());
				query2.setParameter(2, loyalityId);
				logger.info("loyalityid   "+loyalityId+"tierid"+tierId);
				query2.executeUpdate();	
				if(tierId<currentTierId && isRoyalFamily==1)
				{
					logger.debug("belongs to royal family so cannot downgrade tier RequestId:"+changeTierDTO.getRequestId());
					transaction.rollback();
					changeTierDTO.setStatus("SC0000");
					changeTierDTO.setStatusDesc("Tier can not be downgraded for this customer");
				}
				else
				{
					transaction.commit();
					logger.info("sucessfully updated tierId for RequestId: "+changeTierDTO.getRequestId() );
					changeTierDTO.setStatus("SC0000");
					changeTierDTO.setStatusDesc("sucessfully updated tierId");
					logger.info("sucess");
				}
				
				}
				else
				{
					logger.info("Tier name "+changeTierDTO.getTier()+"  is not present in TierInfo table for RequestID:"+changeTierDTO.getRequestId());
					changeTierDTO.setStatusDesc("Tier name "+changeTierDTO.getTier()+"  is not Vaild Tier ");
				}
			}
			else
			{
				logger.info("subscriber with msisdn "+changeTierDTO.getMsisdn()+" is not registered in LMS");
				changeTierDTO.setStatusDesc("subscriber with msisdn "+changeTierDTO.getMsisdn()+" is not registered in LMS");
			}
		}
		catch(Exception e)
		{
			if(transaction!=null)
				transaction.rollback();
			e.printStackTrace();
		}
		finally{

			if(session!=null)
				session.close();		
			sql=null;
			
		
		}
		
		return changeTierDTO;
		
	}*/
	public int getTierId(String tier) {
		Session session=null;
		String sql=null;
		int tierId=0;
		try{
			session=HiberanteUtil.getSessionFactory().openSession();
			 sql=" SELECT ID from TIER_INFO WHERE TIER_NAME=?";
			Query query1=session.createSQLQuery(sql);
			query1.setParameter(0, tier.toUpperCase());
			List<Object[]> list=query1.list();
			logger.info(list.size());
			if(list.size()>0)
			{
			String curretnTierId1=list.get(0)+"";
			tierId=Integer.parseInt(curretnTierId1);
			}
			
		}
		catch(Exception e)
		{
			logger.error("Exception occured:"+e);
			e.printStackTrace();
		}
		finally{

			if(session!=null)
				session.close();		
			sql=null;
			
		
		}
		return tierId;
	}
	public void updateTier(int tierId, Long loyaltyID, int isRoyalFamily) {
		Session session=null;
		String sql=null;
		Transaction transaction=null;
		try{
			session=HiberanteUtil.getSessionFactory().openSession();
			 transaction=session.beginTransaction();
				sql="UPDATE LOYALTY_PROFILE_0 SET TIER_ID=?,ISROYALFAMILY=? WHERE LOYALTY_ID=? ";
				Query query=session.createSQLQuery(sql);
				query.setParameter(0, tierId);
				query.setParameter(1, isRoyalFamily);
				query.setParameter(2, loyaltyID);
				logger.info("loyalityid   "+loyaltyID+"tierid"+tierId);				
				query.executeUpdate();
				transaction.commit();
		}
		catch(Exception e)
		{
			transaction.rollback();
			logger.error("Exception occured:"+e);
			e.printStackTrace();
		}
		finally{

			if(session!=null)
				session.close();	
			transaction=null;
			sql=null;
			
		
		}
		
	}


}
