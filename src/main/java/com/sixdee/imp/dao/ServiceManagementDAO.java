package com.sixdee.imp.dao;

import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.CustomerProfileTabDTO;
import com.sixdee.imp.dto.GeneralManagementCdrDto;
import com.sixdee.imp.dto.ServiceManagementDTO;

public class ServiceManagementDAO {
	private final static Logger logger = Logger.getLogger(ServiceManagementDAO.class);
	public boolean updateMsisdnChangeSubsriberTab(String oldSubscriberNumber, String newSubscriberNumber,
			String tabName,ServiceManagementDTO serviceManagementDTO) throws Exception {
		Query query = null;
		String sql = "";
		Session session = null;
		boolean updateStatus = false;
		Transaction transaction=null;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			sql = "UPDATE " + tabName + " SET subscriberNumber=? WHERE subscriberNumber=?";
			query = session.createQuery(sql);
			query.setParameter(0, Long.valueOf(newSubscriberNumber));
			query.setParameter(1, Long.valueOf(oldSubscriberNumber));
			long l1 = System.currentTimeMillis();
			int i = query.executeUpdate();
			if (i > 0) {
				updateStatus = true;
			}
			transaction.commit();
			logger.info("TransactionId "+serviceManagementDTO.getTransactionId()+" Total time to update = "+ (System.currentTimeMillis() - l1));
		} catch (Exception e) {
			logger.error("TransactionID  "+serviceManagementDTO.getTransactionId() +" Exception occured "+ e);
			if (transaction != null)
				transaction.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			query = null;
		}
		return updateStatus;

	}

	public boolean updateMsisdnChangeloyaltyUpdateTab(String oldSubscriberNumber, String newSubscriberNumber,
			String tabName,ServiceManagementDTO serviceManagementDTO) throws Exception {
		Query query = null;
		String sql = "";
		Session session = null;
		boolean updateStatus = false;
		Transaction transaction=null;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			sql = "UPDATE " + tabName + " SET contactNumber=? WHERE contactNumber=?";
			query = session.createQuery(sql);
			query.setParameter(0, newSubscriberNumber);
			query.setParameter(1, oldSubscriberNumber);
			long l1 = System.currentTimeMillis();
			int i = query.executeUpdate();
			if (i > 0) {
				updateStatus = true;
			}
			logger.info("TransactionId "+serviceManagementDTO.getTransactionId()+" Total time to update = "+ (System.currentTimeMillis() - l1));
			transaction.commit();
		} catch (Exception e) {
			logger.error("TransactionID  "+serviceManagementDTO.getTransactionId() +" Exception occured "+ e);
			if (transaction != null)
				transaction.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			query = null;
			session=null;
		}
		return updateStatus;
	}

	public boolean updateMsisdnChangeloyaltyRegisteredTab(String oldSubscriberNumber, String newSubscriberNumber,
			String tabName,ServiceManagementDTO serviceManagementDTO) throws Exception {
		Query query = null;
		String sql = "";
		Session session = null;
		boolean updateStatus = false;
		Transaction transaction=null;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			sql = "UPDATE " + tabName + " SET linkedNumber=? WHERE linkedNumber=?";
			query = session.createQuery(sql);
			query.setParameter(0, newSubscriberNumber);
			query.setParameter(1, oldSubscriberNumber);
			long l1 = System.currentTimeMillis();
			int i = query.executeUpdate();
			if (i > 0) {
				updateStatus = true;
			}
			transaction.commit();
			logger.info("TransactionId "+serviceManagementDTO.getTransactionId()+" Total time to update = "+ (System.currentTimeMillis() - l1));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			query = null;
			session=null;
		}
		return updateStatus;
	}
	
	public boolean updateCommonLoyaltyProfileSubscirberOperation(int statusId,String tabName,Date expiryDate,long loyaltyId) throws Exception {
		Query query = null;
		String sql = "";
		Session session = null;
		boolean updateStatus = false;
		Transaction transaction=null;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			sql = "UPDATE " + tabName + " SET statusID=? , expiry_Date=? WHERE loyaltyID=?";
			query = session.createQuery(sql);
			query.setParameter(0, statusId);
			query.setParameter(1, expiryDate);
			query.setParameter(2, loyaltyId);
			long l1 = System.currentTimeMillis();
			int i = query.executeUpdate();
			if (i > 0) {
				updateStatus = true;
				logger.info("updateLoyaltyProfileOperation updated succesfully"+" StatusID "+statusId+" expiryDate "+expiryDate+" loyaltyId "+loyaltyId);
			}
			else
			{
				logger.info("updateLoyaltyProfileOperation updation failed");
			}
			logger.info("Total time to update = " + (System.currentTimeMillis() - l1));
			
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			query = null;
			session=null;
		}
		return updateStatus;
	}
	
	public boolean updateNewRatePlanTab(ServiceManagementDTO serviceManagementDTO) throws Exception {
		Query query = null;
		String sql = "";
		Session session = null;
		boolean updateStatus = false;
		Transaction transaction=null;
		String tabName="CustomerProfileTabDTO";
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			sql = "UPDATE " + tabName + " SET baseRatePlan=? WHERE msisdn=?";
			query = session.createQuery(sql);
			query.setParameter(0, serviceManagementDTO.getNewRatePlan());
			query.setParameter(1, serviceManagementDTO.getMoNumber());
			long l1 = System.currentTimeMillis();
			int i = query.executeUpdate();
			if (i > 0) {
				updateStatus = true;
			}
			transaction.commit();
			logger.info("TransactionId "+serviceManagementDTO.getTransactionId()+" Total time to update = "+ (System.currentTimeMillis() - l1));
		} catch (Exception e) {
			logger.error("TransactionID  "+serviceManagementDTO.getTransactionId() +" Exception occured "+ e);
			if (transaction != null)
				transaction.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			query = null;
		}
		return updateStatus;

	}
	
	public boolean updateMobileAppIntegrationSubscriberTab(String referalNumber,String tabName,ServiceManagementDTO serviceManagementDTO,String moNumber) throws Exception {
		Query query = null;
		String sql = "";
		Session session = null;
		boolean updateStatus = false;
		Transaction transaction=null;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			sql = "UPDATE " + tabName + " SET ref_Number=? WHERE subscriberNumber=?";
			query = session.createQuery(sql);
			query.setParameter(0, Long.valueOf(referalNumber));
			query.setParameter(1, Long.valueOf(moNumber));
			long l1 = System.currentTimeMillis();
			int i = query.executeUpdate();
			if (i > 0) {
				updateStatus = true;
			}
			transaction.commit();
			logger.info("TransactionId "+serviceManagementDTO.getTransactionId()+" Total time to update = "+ (System.currentTimeMillis() - l1));
		} catch (Exception e) {
			logger.error("TransactionID  "+serviceManagementDTO.getTransactionId() +" Exception occured "+ e);
			if (transaction != null)
				transaction.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			query = null;
		}
		return updateStatus;

	}
	
	public boolean updateMobileAppIntegrationCustomerProfileTab(ServiceManagementDTO serviceManagementDTO) throws Exception {
		Query query = null;
		String sql = "";
		Session session = null;
		boolean updateStatus = false;
		Transaction transaction=null;
		String tabName="CustomerProfileTabDTO";
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			sql = "UPDATE " + tabName + " SET ref_Number=? WHERE msisdn=?";
			query = session.createQuery(sql);
			query.setParameter(0, serviceManagementDTO.getReferreeNumber());
			query.setParameter(1, serviceManagementDTO.getMoNumber());
			long l1 = System.currentTimeMillis();
			int i = query.executeUpdate();
			if (i > 0) {
				updateStatus = true;
			}
			transaction.commit();
			logger.info("TransactionId "+serviceManagementDTO.getTransactionId()+" Total time to update = "+ (System.currentTimeMillis() - l1));
		} catch (Exception e) {
			logger.error("TransactionID  "+serviceManagementDTO.getTransactionId() +" Exception occured "+ e);
			if (transaction != null)
				transaction.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			query = null;
		}
		return updateStatus;

	}

	
	public boolean updateTelecoDetailsCustomerProfileTab(ServiceManagementDTO serviceManagementDTO) throws Exception {
		Query query = null;
		String sql = "";
		Session session = null;
		boolean updateStatus = false;
		String tabName="CustomerProfileTabDTO";
		Transaction transaction=null;
		long l1 = System.currentTimeMillis();
		logger.info(">>>>>>>>>>>>>>Telco admin came"+serviceManagementDTO.getNewTelecoAdmin()+">>>>>>>>>>>>>>Old "+serviceManagementDTO.getOldTelecoAdmin());
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			if(serviceManagementDTO.getOldTelecoAdmin()!=null &&serviceManagementDTO.getNewTelecoAdmin()!=null)
			{
			sql = "UPDATE " + tabName + " SET telecoAdmin=? WHERE telecoAdmin=?";
			query = session.createQuery(sql);
			query.setParameter(0, serviceManagementDTO.getNewTelecoAdmin());
			query.setParameter(1, serviceManagementDTO.getOldTelecoAdmin());
			
			int i = query.executeUpdate();
			if (i > 0) {
				updateStatus = true;
			}
			transaction.commit();
			}
			/*else
			{
				sql = "UPDATE " + tabName + " SET telecoAdmin=? WHERE msisdn=?";
				query = session.createQuery(sql);
				query.setParameter(0, serviceManagementDTO.getNewTelecoAdmin());
				query.setParameter(1, serviceManagementDTO.getMoNumber());
			}*/
			
			
			logger.info("TransactionId "+serviceManagementDTO.getTransactionId()+" Total time to update = "+ (System.currentTimeMillis() - l1));
		} catch (Exception e) {
			logger.error("TransactionID  "+serviceManagementDTO.getTransactionId() +" Exception occured "+ e);
			if (transaction != null)
				transaction.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			query = null;
			logger.info("Update flag value is "+updateStatus);
		}
		return updateStatus;

	}
	
	
	public boolean updateContactNumberCustomerProfileTab(ServiceManagementDTO serviceManagementDTO) throws Exception {
		Query query = null;
		String sql = "";
		Session session = null;
		boolean updateStatus = false;
		String tabName="CustomerProfileTabDTO";
		Transaction transaction=null;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			sql = "UPDATE " + tabName + " SET contactNumber=? WHERE contactNumber=? and  msisdn=?";
			query = session.createQuery(sql);
			query.setParameter(0, serviceManagementDTO.getNewContactNumber());
			query.setParameter(1, serviceManagementDTO.getOldContactNumber());
			query.setParameter(2, serviceManagementDTO.getMoNumber());
			long l1 = System.currentTimeMillis();
			int i = query.executeUpdate();
			if (i > 0) {
				updateStatus = true;
			}
			transaction.commit();
			logger.info("Total time to update = " + (System.currentTimeMillis() - l1));
		} catch (Exception e) {
			logger.error("TransactionID Exception occured "+ e);
			if (transaction != null)
				transaction.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			query = null;
		}
		return updateStatus;

	}
	public boolean updateFraudStatusCustomerProfileTab(CustomerProfileTabDTO customerProfileTabDTO) throws Exception {
		Query query = null;
		String sql = "";
		Session session = null;
		boolean updateStatus = false;
		String tabName="CustomerProfileTabDTO";
		Transaction transaction=null;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			sql = "UPDATE " + tabName + " SET statusId=? WHERE customerRefNumber=?";
			query = session.createQuery(sql);
			query.setParameter(0, customerProfileTabDTO.getStatusId());
			query.setParameter(1, customerProfileTabDTO.getCustomerRefNumber());
			long l1 = System.currentTimeMillis();
			int i = query.executeUpdate();
			if (i > 0) {
				updateStatus = true;
			}
			transaction.commit();
			logger.info("Total time to update = " + (System.currentTimeMillis() - l1));
		} catch (Exception e) {
			logger.error("TransactionID Exception occured "+ e);
			if (transaction != null)
				transaction.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			query = null;
		}
		return updateStatus;

	}
	
	public boolean updateBlacklistStatusCustomerProfileTab(CustomerProfileTabDTO customerProfileTabDTO) throws Exception {
		Query query = null;
		String sql = "";
		Session session = null;
		boolean updateStatus = false;
		String tabName="CustomerProfileTabDTO";
		Transaction transaction=null;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			sql = "UPDATE " + tabName + " SET statusId=? WHERE msisdn=?";
			query = session.createQuery(sql);
			query.setParameter(0, customerProfileTabDTO.getStatusId());
			query.setParameter(1, customerProfileTabDTO.getMsisdn());
			long l1 = System.currentTimeMillis();
			int i = query.executeUpdate();
			if (i > 0) {
				updateStatus = true;
			}
			transaction.commit();
			logger.info("Total time to update = " + (System.currentTimeMillis() - l1));
		} catch (Exception e) {
			logger.error("TransactionID Exception occured "+ e);
			if (transaction != null)
				transaction.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			query = null;
		}
		return updateStatus;

	}
	
	
	public boolean updateServiceMigrationDetailsCustomerProfile(ServiceManagementDTO serviceManagement) throws Exception {
		Query query = null;
		String sql = "";
		Session session = null;
		boolean updateStatus = false;
		String tabName="CustomerProfileTabDTO";
		Transaction transaction=null;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			sql = "UPDATE " + tabName + " SET accountNo=? , accountType=? , accountCategoryType=? WHERE accountNo=? and msisdn=?";
			query = session.createQuery(sql);
			query.setParameter(0, serviceManagement.getNewAccountNumber());
			if(serviceManagement.getNewContractType().equalsIgnoreCase("PREPAID")){
				if(serviceManagement.getAccountType().equals("DIGITAL")){
					query.setParameter(1, "DIGITAL PREPAID");
					query.setParameter(2, "3");
				}else{
					query.setParameter(1, "TRADITIONAL PREPAID");
					query.setParameter(2, "1");
				}
			}else if(serviceManagement.getNewContractType().equalsIgnoreCase("POSTPAID")){
				query.setParameter(1, "TRADITIONAL POSTPAID");
				query.setParameter(2, "2");
			}
			
			query.setParameter(3, serviceManagement.getOldAccountNumber());
			query.setParameter(4, serviceManagement.getMoNumber());
			
			long l1 = System.currentTimeMillis();
			int i = query.executeUpdate();
			if (i > 0) {
				updateStatus = true;
			}
			transaction.commit();
			logger.info("TransactionId "+serviceManagement.getTransactionId()+" Total time to update = "+ (System.currentTimeMillis() - l1));
		} catch (Exception e) {
			logger.error("TransactionID  "+serviceManagement.getTransactionId() +" Exception occured "+ e);
			if (transaction != null)
				transaction.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			query = null;
		}
		return updateStatus;

	}
	
	public boolean insertNewActicationDetails(CustomerProfileTabDTO customerProfileTabDTO) throws Exception {
		Session session = null;
		boolean updateStatus = false;
		Transaction transaction=null;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			long l1 = System.currentTimeMillis();
			session.saveOrUpdate(customerProfileTabDTO);
			transaction.commit();
			updateStatus=true;
			logger.info("Total time to insert or update = " + (System.currentTimeMillis() - l1));
		} catch (HibernateException e) {
			e.printStackTrace();
			logger.error("Hibernate Exception occured ",e);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occured  ",e);
		}finally{
			if(session != null)
				session.close();
		}
		return updateStatus;
	}
	
	
	public boolean updateloyaltyRegisteredTab(int status,
			String tabName,Date date,Long loyaltyId) throws Exception {
		Query query = null;
		String sql = "";
		Session session = null;
		boolean updateStatus = false;
		Transaction transaction=null;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			sql = "UPDATE " + tabName + " SET statusID=? , expiry_Date=? WHERE loyaltyID=?";
			query = session.createQuery(sql);
			query.setParameter(0, status);
			query.setParameter(1, date);
			query.setParameter(2, loyaltyId);
			long l1 = System.currentTimeMillis();
			int i = query.executeUpdate();
			if (i > 0) {
				updateStatus = true;
			}
			transaction.commit();
			logger.info("Total time to update = " + (System.currentTimeMillis() - l1));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			query = null;
			session=null;
		}
		return updateStatus;
	}
	
	
	public boolean updateSubscirberChangeOfOwnderShip(String subscriberNumber,
			String tabName,ServiceManagementDTO serviceManagementDTO) throws Exception {
		Query query = null;
		String sql = "";
		Session session = null;
		boolean updateStatus = false;
		Transaction transaction=null;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			sql = "Delete from " + tabName + " WHERE subscriberNumber=?";
			query = session.createQuery(sql);
			query.setParameter(0, Long.valueOf(subscriberNumber));
			long l1 = System.currentTimeMillis();
			int i = query.executeUpdate();
			if (i > 0) {
				updateStatus = true;
			}
			transaction.commit();
			logger.info("TransactionId "+serviceManagementDTO.getTransactionId()+" Total time to update = "+ (System.currentTimeMillis() - l1));
		} catch (Exception e) {
			logger.error("TransactionID  "+serviceManagementDTO.getTransactionId() +" Exception occured "+ e);
			if (transaction != null)
				transaction.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			query = null;
		}
		return updateStatus;

	}
	
	public boolean updateLoyaltyChangeOfOwnderShip(String subscriberNumber,
			String tabName,ServiceManagementDTO serviceManagementDTO,long loyaltyId) throws Exception {
		Query query = null;
		String sql = "";
		Session session = null;
		boolean updateStatus = false;
		Transaction transaction=null;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			
			sql = "Delete from " + tabName + " WHERE contactNumber=? and loyaltyID=?";
			query = session.createQuery(sql);
			query.setParameter(0, subscriberNumber);
			query.setParameter(1, Long.valueOf(loyaltyId));
			long l1 = System.currentTimeMillis();
			int i = query.executeUpdate();
			if (i > 0) {
				updateStatus = true;
			}
			transaction.commit();
			logger.info("TransactionId "+serviceManagementDTO.getTransactionId()+" Total time to update = "+ (System.currentTimeMillis() - l1));
		} catch (Exception e) {
			logger.error("TransactionID  "+serviceManagementDTO.getTransactionId() +" Exception occured "+ e);
			if (transaction != null)
				transaction.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			query = null;
		}
		return updateStatus;

	}
	
	public boolean updateLoyaltyRegisterChangeOfOwnderShip(String subscriberNumber,
			String tabName,ServiceManagementDTO serviceManagementDTO,long loyaltyId) throws Exception {
		Query query = null;
		String sql = "";
		Session session = null;
		boolean updateStatus = false;
		Transaction transaction=null;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			sql = "Delete from " + tabName + " WHERE linkedNumber=?";
			query = session.createQuery(sql);
			query.setParameter(0,subscriberNumber);
			long l1 = System.currentTimeMillis();
			int i = query.executeUpdate();
			if (i > 0) {
				updateStatus = true;
				logger.info("Entry has been deleted for transaction id "+serviceManagementDTO.getTransactionId());
			}
			transaction.commit();
			logger.info("TransactionId "+serviceManagementDTO.getTransactionId()+" Total time to update = "+ (System.currentTimeMillis() - l1));
		} catch (Exception e) {
			logger.error("TransactionID  "+serviceManagementDTO.getTransactionId() +" Exception occured "+ e);
			if (transaction != null)
				transaction.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			query = null;
		}
		return updateStatus;

	}
	
	public boolean updateNumberTerminationCustomerProfileTab(int status,Date expiryDate, String msisdn) throws Exception {
		Query query = null;
		String sql = "";
		Session session = null;
		boolean updateStatus = false;
		String tabName="CustomerProfileTabDTO";
		Transaction transaction=null;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			sql = "UPDATE " + tabName + " SET expiry_Date=? , statusId=? WHERE msisdn=?";
			query = session.createQuery(sql);
			query.setParameter(0, expiryDate);
			query.setParameter(1, status+"");
			query.setParameter(2, msisdn);
			long l1 = System.currentTimeMillis();
			int i = query.executeUpdate();
			if (i > 0) {
				updateStatus = true;
			}
			transaction.commit();
			logger.info("Total time to update = " + (System.currentTimeMillis() - l1));
		} catch (Exception e) {
			logger.error("TransactionID Exception occured "+ e);
			if (transaction != null)
				transaction.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			query = null;
		}
		return updateStatus;

	}
	
	public boolean cdrGeneralManagementServices(GeneralManagementCdrDto generalManagementCdrDto) throws Exception {
		Session session = null;
		boolean updateStatus = false;
		Transaction transaction=null;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			long l1 = System.currentTimeMillis();
			session.save(generalManagementCdrDto);
			transaction.commit();
			updateStatus=true;
			logger.info("TransactionId "+generalManagementCdrDto.getTransactionId()+" Total time to update = "+ (System.currentTimeMillis() - l1));
		} catch (HibernateException e) {
			logger.error("Hibernate Exception occured ",e);
		}catch (Exception e) {
			logger.error("Exception occured  ",e);
		}finally{
			if(session != null)
				session.close();
		}
		return updateStatus;
	}
	
	
	public boolean deleteChangeOfOwnderShipCustomerProfileTab(ServiceManagementDTO serviceManagementDTO,long loyaltyId) throws Exception {
		Query query = null;
		String sql = "";
		Session session = null;
		boolean updateStatus = false;
		String tabName="CustomerProfileTabDTO";
		Transaction transaction=null;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			sql = "DELETE FROM " + tabName + " WHERE msisdn=?";
			query = session.createQuery(sql);
			query.setParameter(0, serviceManagementDTO.getMoNumber());
			long l1 = System.currentTimeMillis();
			int i = query.executeUpdate();
			if (i > 0) {
				updateStatus = true;
			}
			transaction.commit();
			logger.info("TransactionId "+serviceManagementDTO.getTransactionId()+" Total time to update = "+ (System.currentTimeMillis() - l1));
		} catch (Exception e) {
			logger.error("TransactionID Exception occured "+ e);
			if (transaction != null)
				transaction.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			query = null;
		}
		return updateStatus;

	}
	
	public boolean updateTelecoDetailsLoyaltyProfileTab(ServiceManagementDTO serviceManagementDTO,String tabName) throws Exception {
		Query query = null;
		String sql = "";
		Session session = null;
		boolean updateStatus = false;
		Transaction transaction=null;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			if(serviceManagementDTO.getOldTelecoAdmin()!=null)
			{
			sql = "UPDATE " + tabName + " SET telecomAdmin=? WHERE telecomAdmin=? and contactNumber=?";
			query = session.createQuery(sql);
			query.setParameter(0, serviceManagementDTO.getNewTelecoAdmin());
			query.setParameter(1, serviceManagementDTO.getOldTelecoAdmin());
			query.setParameter(2, serviceManagementDTO.getMoNumber());
			}
			else
			{
				sql = "UPDATE " + tabName + " SET telecomAdmin=? WHERE contactNumber=?";
				query = session.createQuery(sql);
				query.setParameter(0, serviceManagementDTO.getNewTelecoAdmin());
				query.setParameter(1, serviceManagementDTO.getMoNumber());
			}
			long l1 = System.currentTimeMillis();
			int i = query.executeUpdate();
			if (i > 0) {
				updateStatus = true;
			}
			transaction.commit();
			logger.info("TransactionId "+serviceManagementDTO.getTransactionId()+" Total time to update = "+ (System.currentTimeMillis() - l1));
		} catch (Exception e) {
			logger.error("TransactionID  "+serviceManagementDTO.getTransactionId() +" Exception occured "+ e);
			if (transaction != null)
				transaction.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			query = null;
			logger.info("Update flag value is "+updateStatus);
		}
		return updateStatus;

	}
	
	public boolean updateloyaltyProfileBaseRatePlan(String tabName,Long loyaltyId,ServiceManagementDTO serviceManagementDTO) throws Exception {
		Query query = null;
		String sql = "";
		Session session = null;
		boolean updateStatus = false;
		Transaction transaction=null;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			sql = "UPDATE " + tabName + " SET ratePlan=?  WHERE loyaltyID=?";
			query = session.createQuery(sql);
			query.setParameter(0, serviceManagementDTO.getNewRatePlan());
			query.setParameter(1, loyaltyId);
			long l1 = System.currentTimeMillis();
			int i = query.executeUpdate();
			if (i > 0) {
				updateStatus = true;
			}
			transaction.commit();
			logger.info("Total time to update = " + (System.currentTimeMillis() - l1));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			query = null;
			session=null;
		}
		return updateStatus;
	}
	
	public boolean updateCustomerProfileMsisdnChange(String oldSubscriberNumber, String newSubscriberNumber,ServiceManagementDTO serviceManagementDTO) throws Exception {
		Query query = null;
		String sql = "";
		Session session = null;
		boolean updateStatus = false;
		Transaction transaction=null;
		String tabName="CustomerProfileTabDTO";
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			sql = "UPDATE " + tabName + " SET msisdn=? WHERE msisdn=?";
			query = session.createQuery(sql);
			query.setParameter(0, newSubscriberNumber);
			query.setParameter(1, oldSubscriberNumber);
			long l1 = System.currentTimeMillis();
			int i = query.executeUpdate();
			if (i > 0) {
				updateStatus = true;
			}
			transaction.commit();
			logger.info("TransactionId "+serviceManagementDTO.getTransactionId()+" Total time to update = "+ (System.currentTimeMillis() - l1));
		} catch (Exception e) {
			logger.error("TransactionID  "+serviceManagementDTO.getTransactionId() +" Exception occured "+ e);
			if (transaction != null)
				transaction.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			try {
				query=null;
				if (session != null) {
					session.close();
					session = null;
				}
			} catch (Exception e) {
			}
		}
		return updateStatus;

	}
	
	public boolean deleteTransactionsFromTable(ServiceManagementDTO serviceManagementDTO,Long loyaltyID) {
		Transaction transaction = null;
		Session session = null;
		String sql = null;
		TableInfoDAO tableInfoDao =null;
		String tableName=null;
		boolean transactionDeleteStatus=false;
		try {
			tableInfoDao=new TableInfoDAO();
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			tableName=tableInfoDao.getLoyaltyTransactionTableName(""+loyaltyID);
			sql = "DELETE FROM " + tableName + " where  LOYALTY_ID =:loyaltyID";
			Query query = session.createSQLQuery(sql);
			query.setParameter("loyaltyID", loyaltyID);
			int rowCount = query.executeUpdate();
			logger.info("Hard Deleted ::Rows affected: " + rowCount);
			
			
			if (rowCount > 0)
			{
				logger.info("TransactionId "+serviceManagementDTO.getTransactionId() +" RowsCount "+rowCount);
				transactionDeleteStatus=true;
			}
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Transaction id " + serviceManagementDTO.getTransactionId() + " Exception " + e.getMessage());
		} finally {
			try {
				if (session != null) {
					session.close();
					session = null;
				}
			} catch (Exception e) {
			}
		}
		return transactionDeleteStatus;

	}
	
	public boolean deleteDayWisePointInfoTable(ServiceManagementDTO serviceManagementDTO,Long loyaltyID) {
		Transaction transaction = null;
		Session session = null;
		boolean deleteDayWisePointsStatus = false;
		String sql = null;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			sql = "DELETE FROM LMS_ONM_DAY_WISE_POINTS_INFO where LOYALTY_ID =:loyaltyID";
			Query query = session.createSQLQuery(sql);
			query.setParameter("loyaltyID", loyaltyID);
			int rowCount = query.executeUpdate();
			logger.info("Hard Deleted ::Rows affected: " + rowCount);
			if (rowCount > 0)
				deleteDayWisePointsStatus = true;
			logger.info("TransactionId "+serviceManagementDTO.getTransactionId()+" no of rows deleted is >>" + rowCount);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (session != null) {
					session.close();
					session = null;
				}
			} catch (Exception e) {
			}
		}
		return deleteDayWisePointsStatus;

	}
	
	public boolean deleteAccountTableDetails(ServiceManagementDTO serviceManagementDTO, Long loyaltyID) {
		Transaction transaction = null;
		Session session = null;
		boolean done = false;
		String sql = null;
		TableInfoDAO tableInfoDAO = null;
		try {
			tableInfoDAO = new TableInfoDAO();
			session = HiberanteUtil.getSessionFactory().openSession();
			String tableName = tableInfoDAO.getAccountNumberDBTable(serviceManagementDTO.getMoNumber());
			transaction = session.beginTransaction();
			sql = "DELETE FROM " + tableName + " where LOYALTY_ID =:loyaltyID";
			Query query = session.createSQLQuery(sql);
			query.setParameter("loyaltyID", loyaltyID);
			int rowCount = query.executeUpdate();
			logger.info("Transaction id "+serviceManagementDTO.getTransactionId()+" Hard Deleted ::Rows affected: " + rowCount);
			if (rowCount > 0)
				done = true;
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (session != null) {
					session.close();
					session = null;
				}
			} catch (Exception e) {
			}
		}
		return done;
	}
	
	public boolean updateChildToParentSubscriberTab(String tabName,String parentMsisdn,String moNumber,String transactionId) throws Exception {
		Query query = null;
		String sql = "";
		Session session = null;
		boolean updateStatus = false;
		Transaction transaction=null;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			sql = "UPDATE " + tabName + " SET subscriberNumber=? WHERE subscriberNumber=?";
			query = session.createQuery(sql);
			query.setParameter(0, Long.valueOf(parentMsisdn));
			query.setParameter(1, Long.valueOf(moNumber));
			long l1 = System.currentTimeMillis();
			int i = query.executeUpdate();
			if (i > 0) {
				updateStatus = true;
				logger.info("TransactionId "+transactionId+" updateChildToParentSubscriberTab updated succesfully"+" subscriberNumber "+moNumber+"Parent Msisdn "+parentMsisdn);
			}
			else
			{
				logger.info("TransactionId "+transactionId+"updateLoyaltyProfileOperation updation failed");
			}
			logger.info("Total time to update = " + (System.currentTimeMillis() - l1));
			
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			query = null;
			if(session!=null)
				session.close();
			session=null;
			
		}
		return updateStatus;
	}
	
	public boolean updateChildToParentLoyaltyProfileTab(long loyaltyId,String tabName,String parentMsisdn,String moNumber,String transactionId) throws Exception {
		Query query = null;
		String sql = "";
		Session session = null;
		boolean updateStatus = false;
		Transaction transaction=null;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			sql = "UPDATE " + tabName + " SET contactNumber=? WHERE loyaltyID=? and contactNumber=?";
			query = session.createQuery(sql);
			query.setParameter(0, parentMsisdn);
			query.setParameter(1, loyaltyId);
			query.setParameter(2, moNumber);
			long l1 = System.currentTimeMillis();
			int i = query.executeUpdate();
			if (i > 0) {
				updateStatus = true;
				logger.info("TransactionId "+transactionId+" updateChildToParentLoyaltyProfileTab subscriberNumber "+moNumber+"loyaltyId "+loyaltyId +" ParentMsisdn "+parentMsisdn);
			}
			else
			{
				logger.info("TransactionId "+transactionId+" updateChildToParentLoyaltyProfileTab updation failed");
			}
			logger.info("TransactionId "+transactionId+"Total time to update = " + (System.currentTimeMillis() - l1));
			
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			query = null;
			if(session!=null)
				session.close();
			session=null;
		}
		return updateStatus;
	}
	
	public boolean updateChildToParentCustomerProfileTab(ServiceManagementDTO serviceManagementDTO) throws Exception {
		Query query = null;
		String sql = "";
		Session session = null;
		boolean updateStatus = false;
		String tabName="CustomerProfileTabDTO";
		Transaction transaction=null;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			sql = "UPDATE " + tabName + " SET msisdn=? WHERE msisdn=?";
			query = session.createQuery(sql);
			query.setParameter(0,serviceManagementDTO.getParentMsisdn());
			query.setParameter(1, serviceManagementDTO.getMoNumber());
			long l1 = System.currentTimeMillis();
			int i = query.executeUpdate();
			if (i > 0) {
				updateStatus = true;
			}
			transaction.commit();
			logger.info("TransactionId"+serviceManagementDTO.getTransactionId()+" Total time to update = " + (System.currentTimeMillis() - l1));
		} catch (Exception e) {
			logger.error("TransactionID Exception occured "+ e);
			if (transaction != null)
				transaction.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			if(session!=null)
				session.close();
			session=null;
		}
		return updateStatus;
	}
	
	public boolean updateChildToParentLoyaltyRegisteredNumberTab(long loyaltyId,String tabName,String parentMsisdn,String moNumber,String TransactionId) throws Exception {
		Query query = null;
		String sql = "";
		Session session = null;
		boolean updateStatus = false;
		Transaction transaction=null;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			sql = "UPDATE " + tabName + " SET linkedNumber=? WHERE linkedNumber=? and loyaltyID=?";
			query = session.createQuery(sql);
			query.setParameter(0,parentMsisdn);
			query.setParameter(1,moNumber);
			query.setParameter(2,loyaltyId);
			long l1 = System.currentTimeMillis();
			int i = query.executeUpdate();
			if (i > 0) {
				updateStatus = true;
			}
			transaction.commit();
			logger.info("Total time to update = " + (System.currentTimeMillis() - l1));
		} catch (Exception e) {
			logger.error("TransactionID Exception occured "+ e);
			if (transaction != null)
				transaction.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			query = null;
			if(session!=null)
				session.close();
		}
		return updateStatus;
	}
}
