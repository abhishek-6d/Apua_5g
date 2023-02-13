/**
 * 
 */
package com.sixdee.imp.simulator;

import java.math.BigDecimal;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dao.TableDetailsDAO;
import com.sixdee.imp.dto.AccountNumberTabDTO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.NationalNumberTabDTO;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;
import com.sixdee.imp.simulator.dto.AlterationAccountNumber;
import com.sixdee.imp.simulator.dto.AlterationSubscriberNumber;
import com.sixdee.imp.simulator.dto.DuplicateNationalIdInfo;

/**
 * @author Rahul K K
 * @version 1.0.0
 * 
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%">Date</td>
 * <td width="20%">Author</td>
 * <td>Description</td>
 * </tr>
 * <tr>
 * <td>Jan 17, 2014</td>
 * <td>Rahul K K</td>
 * <td>Created this class</td>
 * </tr>
 * </table>
 * </p>
 */
public class NationalIdRemove {
	
	private static final Logger logger = Logger.getLogger(NationalIdRemove.class);
	
	
	public void idMisMatchRemove(){
		HashMap<String,ArrayList<String>> idMisMatch = null;
		ArrayList<NationalNumberTabDTO> nationalNumbers = null;
		Set<String> lineRemovalList = null;
		LoyaltyAccountRemoveHistoryDTO loyaltyAccountRemoveHistoryDTO = null;
		TableDetailsDAO tableDetailsDAO = new TableDetailsDAO();
		DuplicateNationalIdInfo duplicateNationalId = null;
		try{
			nationalNumbers=getDuplicateNationalId(); 
			logger.info("National number starting with 0  :- "+nationalNumbers.size());
			for(NationalNumberTabDTO nationalNumber:nationalNumbers){
				try{
					long loyaltyId = nationalNumber.getLoyaltyID();
					String nationalId=nationalNumber.getNationalNumber();
					logger.info("Loyalty id "+loyaltyId);
					duplicateNationalId = new DuplicateNationalIdInfo();
					duplicateNationalId.setActualNationalId(nationalId);
					duplicateNationalId.setActualLoyaltyId(loyaltyId);
					String proposedNationalId = nationalId.replaceFirst("^0+(?!$)", "");
					duplicateNationalId.setProposedNationalId(proposedNationalId);
					
					NationalNumberTabDTO nationaNumberInfo = getNationalIdInfo(proposedNationalId);
					if(nationaNumberInfo != null){
						long proposedLoyaltyId = nationaNumberInfo.getLoyaltyID();
						duplicateNationalId.setProposedLoyaltyId(proposedLoyaltyId);
						
						LoyaltyProfileTabDTO 	loyaltyProfileTabDTO = tableDetailsDAO.getLoyaltyProfile((loyaltyId));
						ArrayList<SubscriberNumberTabDTO> subscriberNumber = getAllSubscriberLines(loyaltyId);
						int noOflines = subscriberNumber!=null ? subscriberNumber.size():0;
						ArrayList<AccountNumberTabDTO> accountNumberDTOList = getAllAccountInfo(loyaltyId);
						noOflines = noOflines+(accountNumberDTOList!=null?accountNumberDTOList.size():0);
						//int noOflines = subscriberNumber.size()+accountNumberDTOList.size();
						if(loyaltyProfileTabDTO != null){
							logger.info("Loyalty Information recvieved "+loyaltyId);
							duplicateNationalId.setPoints(loyaltyProfileTabDTO.getRewardPoints());
							duplicateNationalId.setStatusPoints(loyaltyProfileTabDTO.getStatusPoints());
							//int noOflines = getNoOfLines(loyaltyId);
							duplicateNationalId.setActualLines(noOflines);
							saveAlterationNationalIdInfo(duplicateNationalId);
						}
						
						for(SubscriberNumberTabDTO subscriberNumberTabDTO : subscriberNumber){
							AlterationSubscriberNumber alterationSubscriberNumber = new AlterationSubscriberNumber();
							alterationSubscriberNumber.convertSubsToThis(subscriberNumberTabDTO);
							saveAlterationSubscriber(alterationSubscriberNumber);
							//alterationSubscriberNumber.setLoyaltyID(subscriberNumberTabDTO.ge)
						}
						for(AccountNumberTabDTO account : accountNumberDTOList){
							AlterationAccountNumber altAccNo = new AlterationAccountNumber(account);
							saveAlterationAccountInfo(altAccNo);
						}
						logger.info("Step1 : Finished For NationalId "+nationalId);
					}else{
						logger.info("No mapping for proposed national id "+proposedNationalId);
						saveAlterationNationalIdInfo(duplicateNationalId);
					}
					
				}catch(Exception e){
					logger.error("Exception occured ",e);
				}
				finally{
					
				}
			}
		}finally{
			idMisMatch=null;
		}
		
	}
	
	
	

	private ArrayList<AccountNumberTabDTO> getAllAccountInfo(
			long loyaltyId) throws Exception {

		//com.sixdee.imp.simulator.dto.AlterationSubscriberNumber ablc = null;
		ArrayList<AccountNumberTabDTO> nationalNumberList = null;
		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		try{
			logger.info("Loyalty Id "+loyaltyId);
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			criteria = session.createCriteria("ACCOUNT_NUMBER_ENTITY_0");
			criteria.add(Restrictions.like("loyaltyID", loyaltyId));
			nationalNumberList = (ArrayList<AccountNumberTabDTO>) criteria.list();
			transaction.commit();
	
//			criteria.set
		}catch(Exception e){
			logger.error("Exception occured ",e);
			if(transaction!=null){
				transaction.rollback();
			}
			throw e;
		}
		finally{
			if(session != null){
				session.close();
			}
		}
		return nationalNumberList;
	
	
	}
	private ArrayList<SubscriberNumberTabDTO> getAllSubscriberLines(
			long loyaltyId) throws Exception {


		ArrayList<SubscriberNumberTabDTO> nationalNumberList = null;
		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		try{
			logger.info("Loyalty Id "+loyaltyId);
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			criteria = session.createCriteria("SUBSCRIBER_NUMNER_ENTITY_0");
			criteria.add(Restrictions.like("loyaltyID", loyaltyId));
			nationalNumberList = (ArrayList<SubscriberNumberTabDTO>) criteria.list();
			transaction.commit();
	
//			criteria.set
		}catch(Exception e){
			logger.error("Exception occured ",e);
			if(transaction!=null){
				transaction.rollback();
			}
			throw e;
		}
		finally{
			if(session != null){
				session.close();
			}
		}
		return nationalNumberList;
	
	
	}

	private void saveAlterationSubscriber(
			AlterationSubscriberNumber alterationInfo) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			session.save(alterationInfo);
			transaction.commit();
		}catch(Exception e){
			if(transaction != null){
				transaction.rollback();
			}
			throw e;
		}finally{
			if(session != null){
				session.close();
			}
		}
	}
	private void saveAlterationAccountInfo(
			AlterationAccountNumber alterationInfo) {
		Session session = null;
		Transaction transaction = null;
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			session.save(alterationInfo);
			transaction.commit();
		}catch(Exception e){
			if(transaction != null){
				transaction.rollback();
			}
		}finally{
			if(session != null){
				session.close();
			}
		}
	}

	private void saveAlterationNationalIdInfo(
			DuplicateNationalIdInfo duplicateNationalId) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			session.save(duplicateNationalId);
			transaction.commit();
		}catch(Exception e){
			if(transaction != null){
				transaction.rollback();
			}
			throw e;
		}finally{
			if(session != null){
				session.close();
			}
		}
	}

	private int getNoOfLines(long loyaltyId) throws Exception {

		ArrayList<BigDecimal> lineRemovalList = null;
		Session session = null;
		Transaction transaction = null;
		SQLQuery sqlQuery = null;
		String sql = null;
		try{
			sql = "select count(1) from SUBSCRIBER_NUMBER_0 where Loyalty_Id = ?";
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			sqlQuery = session.createSQLQuery(sql);
			sqlQuery.setLong(0, loyaltyId);
			lineRemovalList = (ArrayList<BigDecimal>) sqlQuery.list();
			
			transaction.commit();
			
		}catch(Exception e){
			logger.error("Exception occured ",e);
			if(transaction!=null){
				transaction.rollback();
			}
			throw e;
		}
		finally{
			if(session != null){
				session.close();
			}
			sqlQuery = null;
			
		}
		return lineRemovalList.get(0).intValue();
	
	}
	
	private NationalNumberTabDTO getNationalIdInfo(String loyaltyId) {

		ArrayList<NationalNumberTabDTO> nationalNumberList = null;
		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		NationalNumberTabDTO nationalNumberTabDTO = null;	
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			criteria = session.createCriteria("NATIONAL_NUMBER_ENTITY_0");
			criteria.add(Restrictions.eq("nationalNumber", loyaltyId));
			nationalNumberList = (ArrayList<NationalNumberTabDTO>) criteria.list();
			transaction.commit();
			if(nationalNumberList!=null && nationalNumberList.size()>=1)
				nationalNumberTabDTO = nationalNumberList.get(0);
//			criteria.set
		}catch(Exception e){
			logger.error("Exception occured ",e);
			if(transaction!=null){
				transaction.rollback();
			}
		}
		finally{
			if(session != null){
				session.close();
			}
		}
		return nationalNumberTabDTO;
	
	}

	private ArrayList<NationalNumberTabDTO> getDuplicateNationalId() {
		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		ArrayList<NationalNumberTabDTO> nationalNumberList = null;
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			criteria = session.createCriteria("NATIONAL_NUMBER_ENTITY_0");
			criteria.add(Restrictions.like("nationalNumber", "0%"));
			criteria.add(Restrictions.eq("statusId", 1));
			nationalNumberList = (ArrayList<NationalNumberTabDTO>) criteria.list();
			transaction.commit();
		}catch(Exception e){
			if(transaction != null){
				transaction.rollback();
			}
			logger.error("Exception occured ",e);
		}
		finally{
			if(session != null){
				session.close();
			}
		}
			return nationalNumberList;
	}

	private Set<String> getAllLines(String loyaltyId) {
		Set<String> lineRemovalList = null;
		Session session = null;
		Transaction transaction = null;
		SQLQuery sqlQuery = null;
		String sql = null;
		try{
			sql = "select LINE_NUMBER from LOYALTY_LINE_NEW_CUSTID where Loyalty_Id = ?";
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			sqlQuery = session.createSQLQuery(sql);
			lineRemovalList = (Set<String>) sqlQuery.list();
			transaction.commit();
			
		}catch(Exception e){
			logger.error("Exception occured ",e);
			if(transaction!=null){
				transaction.rollback();
			}
		}
		finally{
			if(session != null){
				session.close();
			}
			sqlQuery = null;
			
		}
		return lineRemovalList;
	}

	private ArrayList<String> getAllLoyaltyIdMisMatch() {
		Session session = null;
		Transaction transaction = null;
		ArrayList<String> loyaltyList = null;
		SQLQuery sqlQuery = null;
		String sql = "select distinct(Loyalty_Id) from LOYALTY_LINE_NEW_CUSTID";
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			sqlQuery = session.createSQLQuery(sql);
			loyaltyList = (ArrayList<String>) sqlQuery.list();
			transaction.commit();
		}catch(Exception e){
			logger.error("Exception occured ",e);
		}finally{
			if(transaction != null){
				transaction.rollback();
			}if(session != null){
				session.close();
			}
		}
		return loyaltyList;
	}
	
	
	
	public static void main(String[] args) {
		String nationalID = "00012313231";
		String actNId = nationalID.replaceFirst("^0+(?!$)", "");
		System.out.println(actNId);
		SubscriberNumberTabDTO subscriberNumberTabDTO = new SubscriberNumberTabDTO();
	}
	
	
	
}
