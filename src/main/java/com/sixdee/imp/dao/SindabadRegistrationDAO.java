package com.sixdee.imp.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.SindabadRegistrationDTO;

public class SindabadRegistrationDAO {

	Logger logger=Logger.getLogger(SindabadRegistrationDAO.class);
	public boolean registerSindabadNumber(
			SindabadRegistrationDTO sindabadRegistrationDTO) {		
		boolean flag=false;
		Session session=null;
		Transaction transaction=null;
		try{
			session=HiberanteUtil.getSessionFactory().openSession();			
			transaction=session.beginTransaction();			
			session.save(sindabadRegistrationDTO);
			transaction.commit();
			flag=true;
		}
		catch(Exception e){
			logger.error("Exception:"+e);
		}
		finally{
			if(session!=null)
				session.close();
			if(transaction.isActive())
				transaction.commit();
			session=null;
			transaction=null;
		}
		return flag;
		
		
	}
	public SindabadRegistrationDTO checkForRegistration(Long loyaltYid,String sindabadNumber) {
		Session session=null;		
		String sql=null;
		Query query=null;
		List<SindabadRegistrationDTO> list=null;
		SindabadRegistrationDTO sindabadRegistrationDTO=null;
		try{
			session=HiberanteUtil.getSessionFactory().openSession();
			sql = "from SindabadRegistrationDTO where loyaltyID=:id OR sindabadNumber=:sindabadNumber";
			query=session.createQuery(sql);
			query.setParameter("id", loyaltYid);
			query.setParameter("sindabadNumber", sindabadNumber);
			list= query.list();
			if(!list.isEmpty())
			sindabadRegistrationDTO=list.get(0);
					
		}
		catch(Exception e){
			logger.error("Exception:"+e);
		}
		finally{
			if(session!=null)
				session.close();
			session=null;
		}
		return sindabadRegistrationDTO;
	}
	public boolean updateSindabadNumber(
			SindabadRegistrationDTO sindabadRegistrationDTO) {

		Session session=null;		
		String sql=null;
		Query query=null;	
		Transaction transaction=null;
		boolean updateFlag=false;
		int i=0;
		try{
			session=HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			sql = "update SindabadRegistrationDTO set sindabadNumber=:sindNo where loyaltyID=:loyaltyid";
			query=session.createQuery(sql);
			query.setParameter("sindNo", sindabadRegistrationDTO.getSindabadNumber());
			query.setParameter("loyaltyid", sindabadRegistrationDTO.getLoyaltyID());
			i=query.executeUpdate();
			transaction.commit();
			if(i>0)
				updateFlag=true;
			else
				updateFlag=false;
		}
		catch(Exception e){
			logger.error("Exception:"+e);
		}
		finally{
			if(session!=null)
				session.close();
			if (transaction!=null && transaction.isActive()) {
				transaction.commit();
			}
			session=null;
			transaction=null;
		}
		return updateFlag;
		
	
		
	}

}
