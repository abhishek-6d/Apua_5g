package com.sixdee.imp.simulator.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dao.TableInfoDAO;
import com.sixdee.imp.dto.NationalNumberTabDTO;
import com.sixdee.imp.simulator.dto.NationalIDMergingTabDTO;
import com.sixdee.imp.simulator.dto.NationalIDMergingTabDTONumbersFromOmantel;
import com.sixdee.imp.simulator.dto.OldAndNewAccNumTabDTO;
import com.sixdee.imp.simulator.dto.OldAndNewAccNumTabDTOFromOmantel;

public class NationalIdSyncDAO {
	Logger logger = Logger.getLogger(NationalIdSyncDAO.class);
	
	@SuppressWarnings("unchecked")
	public List<OldAndNewAccNumTabDTO> getSubscriberNumberFromTable(){
		logger.info("**** Inside getSubscriberNumberFromTable()");
	Session session = HiberanteUtil.getSessionFactory().openSession();
	Transaction tx = null;
	List<OldAndNewAccNumTabDTO> list = null;
	try{
		tx = session.beginTransaction();
		Criteria criteria = session.createCriteria(OldAndNewAccNumTabDTO.class);
		criteria.add(Restrictions.eq("status","NP"));
		list =criteria.list();
		tx.commit();
		
	}catch(Exception e){
		e.printStackTrace();
		logger.error("exception in getSubscriberNumberFromTable()====> "+e);
		if(tx!=null)
			tx.rollback();
	}
	finally{
		session.close();
	}
	return list;
}
	
	public boolean updateAccountNumberInTable(String subscriberNumber, Long accountNumber,String status){
		Session session = HiberanteUtil.getSessionFactory().openSession();
		Transaction tx = null;
		boolean result = false;
		try{
			logger.info("**** inside updateAccountNumberInTable()");
			logger.info("status====>"+status);
			logger.info("accountNumber====>"+accountNumber);
			logger.info("subscriberNumber====>"+subscriberNumber);
			tx = session.beginTransaction();
			String hql = "UPDATE OldAndNewAccNumTabDTO SET status =:status , newAccountNumber =:accountNumber WHERE subscriberNumber =:subscriberNumber ";
			Query query = session.createQuery(hql);
			query.setParameter("status", status);
			query.setParameter("accountNumber", accountNumber);
			query.setParameter("subscriberNumber", subscriberNumber);
			int rowsUpdated = query.executeUpdate();
			tx.commit();
			if(rowsUpdated>0){
				result = true;
			}
			
		}catch(Exception e){
			result =false;
			if(tx!=null)
				tx.rollback();
			logger.error("Exception in updateAccountNumberInTable"+e);
		}
		finally{
			session.close();
		}
		return result;
	}
	
	public List<NationalNumberTabDTO> getNationalNumberDetails(String tableName,long loyaltyId){
		logger.info("getNationalNumberDetails()");
		Session session = HiberanteUtil.getSessionFactory().openSession();
		Transaction tx = null;
		Criteria ctr  = null;
		List<NationalNumberTabDTO> list =null;
		try{
			tx = session.beginTransaction();
			ctr= session.createCriteria(tableName);
			ctr.add(Restrictions.eq("loyaltyID", loyaltyId));
			list=ctr.list();
			tx.commit();
		}catch(Exception e){
			if(tx!=null)
				tx.rollback();
			logger.error("Exception in getNationalNumberDetails()"+e);
		}
		finally{
			session.close();
		}
		return list;
	}
	
	public boolean updateNationalIdMerging(NationalIDMergingTabDTO idMergingTabDTO){
		boolean result = true;
		logger.info("updateNationalIdMerging()");
		Session session = HiberanteUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			session.save(idMergingTabDTO);
			tx.commit();
		}catch(Exception e){
			result = false;
			if(tx!=null)
				tx.rollback();
			logger.error("Exception in updateNationalIdMerging()"+e);
		}
		finally{
			session.close();
		}
		return result;
		
	}
	
	public List<NationalIDMergingTabDTO> getUpdatedData(){
		logger.info("**** getUpdatedData() ****");
		List<NationalIDMergingTabDTO> updatedInfoList = new ArrayList<NationalIDMergingTabDTO>();
		Session session =  HiberanteUtil.getSessionFactory().openSession();
		Transaction transaction =null;
		try{
			transaction = session.beginTransaction();
			String hql = "FROM NationalIDMergingTabDTO WHERE mismatchFlag =1";
			Query query = session.createQuery(hql);
			updatedInfoList = query.list();
			
			transaction.commit();
			
		}catch(Exception e){
			logger.error("Exception in getUpdatedData() " +e);
			if(transaction!=null)
				transaction.rollback();
		}
		finally{
			session.close();
		}
		return updatedInfoList;
	}
	
	
	
	
	
	
	public List<String> getNullrecords(){
		logger.info("**** getNullrecords() ****");
		List<String> nullIdList = new ArrayList<String>();
		Session session =  HiberanteUtil.getSessionFactory().openSession();
		Transaction transaction =null;
		Criteria ctr = null;
		try{
			transaction = session.beginTransaction();
			ctr = session.createCriteria(NationalIDMergingTabDTO.class);
			ctr.setProjection(Projections.distinct(Projections.property("subscriberNumber")));
			ctr.add(Restrictions.isNull("newNationalId"));
			nullIdList = ctr.list();
			
			transaction.commit();
			
		}catch(Exception e){
			logger.error("Exception in getNullrecords() " +e);
			if(transaction!=null)
				transaction.rollback();
		}
		finally{
			session.close();
		}
		return nullIdList;
		
	}
	
	public boolean checkNewIDInNewIdField(String newIdType,String subscriberNumber){
		logger.info("**** checkNewIDInNewIdField() ****");
		Session session =  HiberanteUtil.getSessionFactory().openSession();
		Transaction transaction =null;
		int size=0;
		boolean result =false;
		try{
			transaction = session.beginTransaction();
			String hql = "FROM NationalIDMergingTabDTO WHERE subscriberNumber =:subscriberNumber AND newNationalIdType =:newIdType";
			Query query = session.createQuery(hql);
			query.setParameter("subscriberNumber", subscriberNumber);
			query.setParameter("newIdType", newIdType);
			size = query.list().size();
			logger.info("checkNewIDInNewIdField(),the size of the list when checking for the entry in nationalIDmerging ====>" +size);
			if(size>0)
				result = true;
			transaction.commit();
			
		}catch(Exception e){
			result=false;
			logger.error("Exception in checkNewIDInNewIdField() " +e);
			if(transaction!=null)
				transaction.rollback();
		}
		finally{
			session.close();
		}
			return result;
		
	}
	
	public boolean deleteIdFromNullList(NationalIDMergingTabDTO nationalIDMergingTabDTO){
		logger.info("**** deleteIdFromNullList() ****");
		Session session =  HiberanteUtil.getSessionFactory().openSession();
		Transaction transaction =null;
		boolean result =false;
		try{
			transaction = session.beginTransaction();
			String hql = "UPDATE NationalIDMergingTabDTO SET  newNationalId =:deleteValue, newNationalIdType =:deleteValue  WHERE subscriberNumber =:subscriberNumber AND newNationalIdType =:newNationalIdType ";
			Query query = session.createQuery(hql);
			query.setParameter("deleteValue", null);
			query.setParameter("subscriberNumber", nationalIDMergingTabDTO.getSubscriberNumber());
			query.setParameter("newNationalIdType", nationalIDMergingTabDTO.getNewNationalIdType());
			int rowsUpdated = query.executeUpdate();
			logger.info("subnum in delete query ====>"+nationalIDMergingTabDTO.getSubscriberNumber());
			logger.info("newNationalIdType in delete query ====>"+nationalIDMergingTabDTO.getNewNationalIdType());
			logger.info("the no.of rows updated while deleting in nulllist ====>"+rowsUpdated);
			if(rowsUpdated>0)
				result = true;
			transaction.commit();
			
		}catch(Exception e){
			result =false;
			logger.error("Exception in deleteIdFromNullList() " +e);
			if(transaction!=null)
				transaction.rollback();
		}
		finally{
			session.close();
		}
		
		return result;
	}
	
	
	public boolean checkNewIDInOldIdField(String newIdType,String subscriberNumber){
		logger.info("**** checkNewIDInOldIdField() ****");
		Session session =  HiberanteUtil.getSessionFactory().openSession();
		Transaction transaction =null;
		int size=0;
		boolean result =false;
		try{
			transaction = session.beginTransaction();
			String hql = "FROM NationalIDMergingTabDTO WHERE subscriberNumber =:subscriberNumber AND oldNationalIdType =:newIdType";
			Query query = session.createQuery(hql);
			query.setParameter("subscriberNumber", subscriberNumber);
			query.setParameter("newIdType", newIdType);
			size = query.list().size();
			logger.info("checkNewIDInOldIdField() the size of the list when checking for the entry in nulllist ====>" +size);
			if(size>0)
				result = true;
			transaction.commit();
			
		}catch(Exception e){
			result=false;
			logger.error("Exception in checkNewIDInOldIdField() " +e);
			if(transaction!=null)
				transaction.rollback();
		}
		finally{
			session.close();
		}
			return result;
		
	}
	

	public boolean updateNullList(NationalIDMergingTabDTO nationalIDMergingTabDTO){
		logger.info("**** updateNullList() ****");
		Session session =  HiberanteUtil.getSessionFactory().openSession();
		Transaction transaction =null;
		boolean result =false;
		try{
			transaction = session.beginTransaction();
			String hql = "UPDATE NationalIDMergingTabDTO SET  newNationalId =:newNationalId, newNationalIdType =:newNationalIdType  WHERE subscriberNumber =:subscriberNumber AND oldNationalIdType =:newNationalIdType ";
			Query query = session.createQuery(hql);
			query.setParameter("newNationalId", nationalIDMergingTabDTO.getNewNationalId());
			query.setParameter("newNationalIdType", nationalIDMergingTabDTO.getNewNationalIdType());
			query.setParameter("subscriberNumber", nationalIDMergingTabDTO.getSubscriberNumber());
			int rowsUpdated = query.executeUpdate();
			logger.info("updateNullList ,the no.of rows updated in NationalIDMergingTab ====>"+rowsUpdated);
			if(rowsUpdated>0)
				result = true;
			transaction.commit();
			
		}catch(Exception e){
			result =false;
			logger.error("Exception in updateNullList() " +e);
			if(transaction!=null)
				transaction.rollback();
		}
		finally{
			session.close();
		}
		
		return result;
	}
	
	
	public long getLoyaltyIDBasedOnsubnum(String subscribeNumber){
		Session session = HiberanteUtil.getSessionFactory().openSession();
		Transaction tx = null;
		Criteria ctr  = null;
		List<OldAndNewAccNumTabDTO> list =null;
		OldAndNewAccNumTabDTO accNumTabDTOTmp =null;
		try{
			tx = session.beginTransaction();
			ctr= session.createCriteria(OldAndNewAccNumTabDTO.class);
			ctr.add(Restrictions.eq("subscriberNumber", subscribeNumber));
			list=ctr.list();
			accNumTabDTOTmp =list.get(0);
			tx.commit();
		}catch(Exception e){
			if(tx!=null)
				tx.rollback();
			logger.error("Exception in getNationalNumberDetails()"+e);
		}
		finally{
			session.close();
		}
		return accNumTabDTOTmp.getloyaltyID();
	}
	
	// to get the list of numbers for updation based on the numbers given by omantel
	public List<NationalIDMergingTabDTONumbersFromOmantel> getUpdatedListforUpdation(){
		logger.info("**** getUpdatedListforUpdation() ****");
		List<NationalIDMergingTabDTONumbersFromOmantel> updatedInfoList = new ArrayList<NationalIDMergingTabDTONumbersFromOmantel>();
		Session session =  HiberanteUtil.getSessionFactory().openSession();
		Transaction transaction =null;
		try{
			transaction = session.beginTransaction();
			String hql = "FROM NationalIDMergingTabDTONumbersFromOmantel";
			Query query = session.createQuery(hql);
			updatedInfoList = query.list();
			
			transaction.commit();
			
		}catch(Exception e){
			logger.error("Exception in getUpdatedListforUpdation() " +e);
			if(transaction!=null)
				transaction.rollback();
		}
		finally{
			session.close();
		}
		return updatedInfoList;
	}
	
	
	// to get the list of numbers for deletion based on the numbers given by omantel
		public List<OldAndNewAccNumTabDTOFromOmantel> getUpdatedListForDeletion(){
			logger.info("**** getUpdatedListForDeletion() ****");
			List<OldAndNewAccNumTabDTOFromOmantel> updatedInfoList = new ArrayList<OldAndNewAccNumTabDTOFromOmantel>();
			Session session =  HiberanteUtil.getSessionFactory().openSession();
			Transaction transaction =null;
			try{
				transaction = session.beginTransaction();
				String hql = "FROM OldAndNewAccNumTabDTOFromOmantel";
				Query query = session.createQuery(hql);
				updatedInfoList = query.list();
				
				transaction.commit();
				
			}catch(Exception e){
				logger.error("Exception in getUpdatedListForDeletion() " +e);
				if(transaction!=null)
					transaction.rollback();
			}
			finally{
				session.close();
			}
			return updatedInfoList;
		}

		//Updating SubscriberNumber table
	public boolean updateInSubscriberNumber(String subscriberNumber) {
		
		logger.info("****updateInSubscriberNumber*****");
		boolean flag = false;
		String tableName = null;
		TableInfoDAO infoDAO = new TableInfoDAO();
		Session session=null;
		Transaction transaction =null;
		Query query=null;
		try {
			logger.info("subscriberNumber ==>"+subscriberNumber);
			session=HiberanteUtil.getSessionFactory().openSession();
			logger.info("is session open ==>"+session.isOpen());
			transaction =session.beginTransaction();
			tableName = infoDAO.getSubscriberNumberTable(subscriberNumber+"");
			String hql ="UPDATE "+tableName+" SET statusID=7 WHERE subscriberNumber=:subNum";
			logger.info("hql "+hql);
			query = session.createQuery(hql);
			query.setParameter("subNum", Long.parseLong(subscriberNumber.trim()));
			int rowsUpdated = query.executeUpdate();
			transaction.commit();
			flag = true; 
			
		} catch (Exception e) {
			flag = false;
          logger.error("Exception in updateInSubscriberNumber() "+e);
          e.printStackTrace();
          if(transaction!=null)
				transaction.rollback();
		}
		finally{
			session.close();
		}
		return flag;

	}
	
	//Updating Loyaltyregisterednumber table	
public boolean updateLoyaltyRegisterNumber(String subscriberNumber) {
		
		logger.info("****updateLoyaltyRegisterNumber*****");
		boolean flag = false;
		String tableName = null;
		TableInfoDAO infoDAO = new TableInfoDAO();
		Session session=null;
		Transaction transaction =null;
		try {
			logger.info("subscriberNumber ==>"+subscriberNumber);
			session=HiberanteUtil.getSessionFactory().openSession();
			transaction =session.beginTransaction();
			tableName=infoDAO.getLoyaltyRegisteredNumberTable(subscriberNumber+"");
			String hql ="UPDATE "+tableName+" SET statusID=7 WHERE linkedNumber=:subNum";
			logger.info("hql"+hql);
			Query query = session.createQuery(hql);
			query.setParameter("subNum", subscriberNumber);
			int rowsUpdated = query.executeUpdate();
			transaction.commit();
			flag = true; 
			
		} catch (Exception e) {
			flag = false;
          logger.error("Exception in updateLoyaltyRegisterNumber() "+e);
          e.printStackTrace();
          if(transaction!=null)
				transaction.rollback();
		}
		finally{
			session.close();
		}
		return flag;

	}
	

//cache SubscriberNumberTable based on loyaltyID
@SuppressWarnings("unchecked")
public ArrayList<String> CacheSubscriberNumberTableUsingLoyaltyID(String subscriberNumber,long loyaltyID) {
	
	logger.info("****CacheSubscriberNumberTableUsingLoyaltyID*****");
	boolean flag = false;
	String tableName = null;
	TableInfoDAO infoDAO = new TableInfoDAO();
	Session session=null;
	Transaction transaction =null;
	Criteria ctr  = null;
	ArrayList<Long> resultList=null;
	ArrayList<String> subscribersList= new ArrayList<String>();
	try {
		
		session=HiberanteUtil.getSessionFactory().openSession();
		transaction =session.beginTransaction();
		tableName = infoDAO.getSubscriberNumberTable(subscriberNumber+ "");
		
		ctr= session.createCriteria(tableName);
		ctr.setProjection(Projections.property("subscriberNumber"));
		ctr.add(Restrictions.eq("loyaltyID", loyaltyID));
		resultList=(ArrayList<Long>)ctr.list();
		
		transaction.commit();
		flag = true; 
		
	} catch (Exception e) {
		flag = false;
      logger.error("Exception in CacheSubscriberNumberTableUsingLoyaltyID() "+e);
      if(transaction!=null)
			transaction.rollback();
	}
	finally{
		session.close();
	}
	
	if(resultList!=null && resultList.size()>0)
	{
	for(long tmp :resultList){
		subscribersList.add(tmp+"");
	}
	return subscribersList;
   }
	else
		return null;
	
	

}


//cache LoyaltyRegisteredNumberTable based on loyaltyID
@SuppressWarnings("unchecked")
public ArrayList<String> CacheLoyaltyRegisteredNumberTableUsingLoyaltyID(String subscriberNumber,long loyaltyID) {
	
	logger.info("****CacheLoyaltyRegisteredNumberTableUsingLoyaltyID*****");
	boolean flag = false;
	String tableName = null;
	TableInfoDAO infoDAO = new TableInfoDAO();
	Session session=null;
	Transaction transaction =null;
	Criteria ctr  = null;
	ArrayList<String> subscribersList=null;
	try {
		
		session=HiberanteUtil.getSessionFactory().openSession();
		transaction =session.beginTransaction();
		tableName=infoDAO.getLoyaltyRegisteredNumberTable(subscriberNumber+"");
		
		ctr= session.createCriteria(tableName);
		ctr.setProjection(Projections.property("linkedNumber"));
		ctr.add(Restrictions.eq("loyaltyID", loyaltyID));
		subscribersList=(ArrayList<String>)ctr.list();
		
		transaction.commit();
		flag = true; 
		
	} catch (Exception e) {
		flag = false;
    logger.error("Exception in CacheLoyaltyRegisteredNumberTableUsingLoyaltyID() "+e);
    if(transaction!=null)
			transaction.rollback();
	}
	finally{
		session.close();
	}
	return subscribersList;

}



}
