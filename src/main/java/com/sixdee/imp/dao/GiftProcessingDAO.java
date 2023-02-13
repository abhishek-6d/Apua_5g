/**
 * 
 */
package com.sixdee.imp.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.InstantPackDTO;
import com.sixdee.imp.dto.InstantReserveDTO;

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
 * <td>April 24, 2013</td>
 * <td>Rahul K K</td>
 * <td>Created this class</td>
 * </tr>
 * </table>
 * </p>
 */
public class GiftProcessingDAO {
	
	private static final Logger logger = Logger.getLogger(GiftProcessingDAO.class);

	public boolean reserveMyGift(String txnId, String msisdn,String tableName,
			long donor ,long reciever, InstantPackDTO giftPackDTO, String packName, int status) {
		Session session = null;
		Transaction transcation = null;
		boolean isGiftCreated = false;
		try{
			InstantReserveDTO giftReserve = getReserveDTO(donor,reciever,giftPackDTO,packName,status);
			session = HiberanteUtil.getSessionFactory().openSession();
			transcation = session.beginTransaction();
			session.save(tableName,giftReserve);
			transcation.commit();
			isGiftCreated = true;
		}catch(HibernateException e){
			logger.error("Transaction ID : "+txnId+" Msisdn : "+msisdn+" Message : Hibernate Exception occured ",e);
			if(transcation!=null){
				transcation.rollback();
			}
		}catch (Exception e) {
			logger.error("Transaction ID : "+txnId+" Msisdn : "+msisdn+" Message : Exception occured ",e);
			if(transcation!=null){
				transcation.rollback();
			}
			}
		finally{
			if(session != null){
				session.close();
			}
		}
		return isGiftCreated;
	}

	private InstantReserveDTO getReserveDTO(long donor,long reciever, InstantPackDTO giftPackDTO, String packName, int status) {
		String emptyString  = "";
		InstantReserveDTO giftReserve = new InstantReserveDTO();
		giftReserve.setStatus(status);
		giftReserve.setSubscriberNumber((donor));
		giftReserve.setOfferId(giftPackDTO.getPackId());
		giftReserve.setOfferName(packName);
		giftReserve.setbSubscriberNumber(reciever);
		Date d = new Date();
		giftReserve.setCreateDate(d);
		giftReserve.setUpdateDate(d);
		
		return giftReserve;
		
	}

	public InstantReserveDTO isGiftAvailable(String txnId, String msisdn,
			String donorTable, long donor, long reciever, String offerName,int validity,ArrayList<Integer> status, int nextStatus,boolean isDay,boolean isDirectTransfer) throws CommonException {
		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		Calendar cal = null;
		boolean isTransfered = false;
		InstantReserveDTO toTransferPack = null;
		try{
			cal = Calendar.getInstance();
			if(isDay)
				cal.add(Calendar.DATE, -validity);
			else
				cal.add(Calendar.HOUR, -validity);
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			criteria = session.createCriteria(donorTable);
			if(isDirectTransfer)
				criteria.add(Restrictions.gt("createDate", cal.getTime()));
			else{
				criteria.add(Restrictions.lt("updateDate", cal.getTime()));
				criteria.add(Restrictions.eq("bSubscriberNumber", reciever));
			}
				
			criteria.add(Restrictions.eq("subscriberNumber", donor)).add(Restrictions.eq("offerName", offerName)).add(Restrictions.in("status", status)).addOrder(Order.asc("createDate")).setMaxResults(1);
			for(InstantReserveDTO giftPack : (ArrayList<InstantReserveDTO>)criteria.list()){
				updateIntoTransfered(session,transaction,donorTable,txnId,msisdn,donor,reciever,giftPack,nextStatus);
				toTransferPack = giftPack;
			}
			
			logger.debug("Parameters : Donor "+donor+" offername "+offerName+" status in "+status+" create Date "+cal.getTime());
			
		//	criteria.add(Restrictions.eq("", value))
		}catch(HibernateException e){
			logger.error("Transaction ID : "+txnId+" Msisdn : "+msisdn+" Message : Hibernate Exception occured ",e);
			throw new CommonException(e.getMessage());
			
		}catch (Exception e) {
			logger.error("Transaction ID : "+txnId+" Msisdn : "+msisdn+" Message :  Exception occured ",e);
			throw new CommonException(e.getMessage());
				
		}
		finally{
			if(session != null){
				session.close();
			}
		}
		return toTransferPack;
	}

	private void updateIntoTransfered(Session session, Transaction transaction,String tableName, String txnId, String msisdn,

			long donor, long reciever, InstantReserveDTO giftPack, int status) throws CommonException {
		boolean isTransfered = false;
		Query sqlQuery = null;
		try{
			String sql = "Update "+tableName+" gift set gift.status=:newstatus , gift.bSubscriberNumber=:reciever where gift.status=:status and gift.id=:id";
			sqlQuery = session.createQuery(sql);
			sqlQuery.setParameter("newstatus", status);
			sqlQuery.setParameter("reciever", reciever);
			sqlQuery.setParameter("status", giftPack.getStatus());
			sqlQuery.setParameter("id", giftPack.getId());
			int i = sqlQuery.executeUpdate(); 
			transaction.commit();
			isTransfered = true;
		}catch(HibernateException e){
			logger.error("Transaction ID : "+txnId+" Msisdn : "+msisdn+" Message : Hibernate Exception occured ",e);
			throw new CommonException(e.getMessage());
		
		}catch (Exception e) {
			logger.error("Transaction ID : "+txnId+" Msisdn : "+msisdn+" Message :  Exception occured ",e);
			throw new CommonException(e.getMessage());
				
		}finally{
			if(!isTransfered && transaction!=null){
				transaction.rollback();
			}
		}
	}

	public boolean getBackMyGift(String txnId, String msisdn,
			String recieverTable, long donor, long reciever, long l,
			InstantPackDTO giftPackDTO, String offerName, int i) throws CommonException {
		Session session = null;
		Transaction transaction = null;
		boolean isGiftedBack = false;
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(recieverTable);
			criteria.add(Restrictions.eq("subscriberNumber", reciever)).add(Restrictions.eq("bSubscriberNumber", donor)).add(Restrictions.eq("status", 2)).add(Restrictions.eq("offerName", offerName)).addOrder(Order.desc("updateDate")).setMaxResults(1);
			
			for(InstantReserveDTO instantReserveDTO : (ArrayList<InstantReserveDTO>)criteria.list()){
				instantReserveDTO.setStatus(i);
				instantReserveDTO.setUpdateDate(new Date());
				session.update(instantReserveDTO);
			}
			transaction.commit();
			isGiftedBack = true;
		}catch(HibernateException e){
			logger.error("Transaction ID : "+txnId+" Msisdn : "+msisdn+" Message : Hibernate Exception occured ",e);
			if(transaction!=null){
				transaction.rollback();
			}
			throw new CommonException(e.getMessage());
			
		}catch (Exception e) {
			logger.error("Transaction ID : "+txnId+" Msisdn : "+msisdn+" Message :  Exception occured ",e);
			if(transaction!=null){
				transaction.rollback();
			}	
			throw new CommonException(e.getMessage());
			
		}finally{
			if(session != null){
				session.close();
			}
		}
		return isGiftedBack;
	}

}
