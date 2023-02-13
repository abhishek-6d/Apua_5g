package com.sixdee.imp.simulator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.service.PointManagement;
import com.sixdee.imp.service.serviceDTO.common.Data;
import com.sixdee.imp.service.serviceDTO.req.RewardPointsDTO;
import com.sixdee.imp.service.serviceDTO.resp.ResponseDTO;

public class AddRewardPoint 
{
	
	
	@SuppressWarnings("unchecked")
	public void getThresholdData()
	{
		Session session = null;
		//Transaction trx = null;
		List<Object[]> list = null;
		Query query = null;
		try 
		{
			session = HiberanteUtil.getSessionFactory().openSession();
			//trx = session.beginTransaction();

			query = session.createSQLQuery("SELECT ID,LOYALTY_ID,SUBSCRIBER_NUMBER,ACCOUNT_NUMBER,VOLUME,PROCESSED FROM LOYALTY_THRESHOLD_DETAILS WHERE PROCESSED=0");
			
			list = query.list();
			
			callRewardFeature(list);
			
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
			//Transaction trx = null;
			list = null;
			query = null;
		}
	}
	
	private void callRewardFeature(List<Object[]> list)
	{
		DateFormat df = new SimpleDateFormat("ddMMyyyyhhmmss");
		RewardPointsDTO pointsDTO = null;
		ResponseDTO responseDTO = null;
		for(Object[] obj:list)
		{
			pointsDTO = new RewardPointsDTO();
			pointsDTO.setChannel("MANNUAL");
			pointsDTO.setVolume(Double.parseDouble(obj[4].toString()));
			if(obj[2]!=null)
			{
				pointsDTO.setSubscriberNumber(obj[2].toString());
				pointsDTO.setRewardPointsCategory(3);
			}
			else
			{
				pointsDTO.setSubscriberNumber(obj[3].toString());
				Data[] data = new Data[1];
				data[0] = new Data();
				data[0].setName("TYPE");
				data[0].setValue("9");
				pointsDTO.setData(data);
				
				data = null;
				pointsDTO.setRewardPointsCategory(2);
			}
			pointsDTO.setTimestamp(df.format(new Date()));
			pointsDTO.setTransactionID("MANUAL_"+System.currentTimeMillis());
			
			
			
			PointManagement pm = new PointManagement();
			responseDTO = pm.rewardPointsCalculation(pointsDTO);
			System.out.println(responseDTO.getStatusCode());
			System.out.println(responseDTO.getStatusDescription());
			
			updateThresholdRecord(Long.parseLong(obj[0].toString()));
		}
	}
	
	private void updateThresholdRecord(long id)
	{
		Session session = null;
		Transaction trx = null;
		Query query = null;
		try
		{
			session = HiberanteUtil.getSessionFactory().openSession();
			trx = session.beginTransaction();
			query = session.createSQLQuery("UPDATE LOYALTY_THRESHOLD_DETAILS SET PROCESSED=1 WHERE ID=?");
			
			query.setParameter(0, id);
			
			query.executeUpdate();
			
			trx.commit();
			
		}
		catch (Exception e) 
		{
			if(trx!=null)
				trx.rollback();
			e.printStackTrace();
		}
		finally
		{
			if(session!=null)
				session.close();
			
			session = null;
			trx = null;
			query = null;
		}
		
	}
}
