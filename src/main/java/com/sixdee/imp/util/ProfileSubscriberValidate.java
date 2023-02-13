package com.sixdee.imp.util;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.imp.common.util.ProfileDBHiberanteUtil;
import com.sixdee.imp.dao.TableInfoDAO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;

public class ProfileSubscriberValidate {

	public boolean validateSubscriberInProfileDB(LoyaltyProfileTabDTO loyaltyProfileTabDTO,String subscriberNumber)
	{
		
		Session session=null;
		Transaction transaction=null;
		TableInfoDAO infoDAO=null;
		boolean flag=false;
		try{
			infoDAO=new TableInfoDAO();
			session=ProfileDBHiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			String tableName=infoDAO.getSubscriberNumberProfileDBTable(subscriberNumber);
			String sql="SELECT FIELD2 FROM "+tableName+" WHERE FIELD2=?";
			
			Query query=session.createSQLQuery(sql);
			query.setParameter(0,subscriberNumber);
			
			List<String> list=query.list();
			
			if(list!=null&&list.size()>0)
				flag=true;
			
			transaction.commit();
			
		}catch (Exception e) {
			flag=false;
			e.printStackTrace();
			if(transaction!=null)
			  transaction.rollback();
		}finally{
			if(session!=null)
				session.close();
			session=null;
			transaction=null;
			infoDAO=null;
		}
		
		return flag;
		
		
		
	}//validateSubscriberInProfileDB
	
}
