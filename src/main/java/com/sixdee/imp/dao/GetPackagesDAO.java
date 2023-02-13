package com.sixdee.imp.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.CustomerProfileTabDTO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.MerchantNomenclatureTabDto;
import com.sixdee.imp.dto.OfferAccountMappingTabDto;
import com.sixdee.imp.dto.OfferMasterTabDto;
import com.sixdee.imp.dto.OfferNomenclatureTabDto;
import com.sixdee.imp.dto.OfferTeleAttruibutes;
import com.sixdee.imp.dto.OfferTierMappingTabDto;

public class GetPackagesDAO 
{
    private static Logger logger = Logger.getLogger(GetPackagesDAO.class);
   
    public List<Integer> getOfferBasedOnChannelTypeDB(String channelType) throws Exception {

		Session session = HiberanteUtil.getSessionFactory().openSession();
		//Transaction tx = null;
		Query query = null;
		List<Integer> offerType = null;
		try {
			//tx = session.beginTransaction();
			query = session.createQuery("select offerId FROM OfferChannelTabDto where channelId=:channelId");
			query.setParameter("channelId", Integer.valueOf(channelType));
			offerType = query.list();
			logger.debug("List of offerType " + offerType.toString());
			//tx.commit();
		} catch (HibernateException e) {
			/*
			 * if (tx != null) tx.rollback();
			 */
			e.printStackTrace();
		} finally {
			if(session!=null)
			session.close();
			session=null;
		}
		return offerType;

	}
	
	public List<CustomerProfileTabDTO> getCustomerProfileInfo(String msisdn) throws Exception {

		Session session = HiberanteUtil.getSessionFactory().openSession();
		//Transaction tx = null;
		Query query = null;
		List<CustomerProfileTabDTO> customerProfileDto = null;
		try {
			//tx = session.beginTransaction();
			query = session.createQuery(" FROM CustomerProfileTabDTO where msisdn=:msisdn");
			query.setParameter("msisdn", msisdn);
			customerProfileDto = query.list();
			logger.debug("List of offerType " + customerProfileDto.size());
			//tx.commit();
		} catch (HibernateException e) {
			/*
			 * if (tx != null) tx.rollback();
			 */
			e.printStackTrace();
		} finally {
			if(session!=null)
				session.close();
				session=null;
		}
		return customerProfileDto;

	}

	public List<Integer> getOfferBasedOnAccountType(String accountTypeid,List<Integer> channelTypeOffer)
			throws Exception {

		Session session = HiberanteUtil.getSessionFactory().openSession();
		//Transaction tx = null;
		List<Integer> offerType = null;
		Criteria criteria = null;
		try {
			//tx = session.beginTransaction();
			criteria = session.createCriteria(OfferAccountMappingTabDto.class).setProjection(Projections.projectionList().add(Projections.property("offerId")));
			criteria.add(Restrictions.in("offerId", channelTypeOffer));
			criteria.add(Restrictions.eq("accountTypeId",Integer.valueOf(accountTypeid)));
			offerType = criteria.list();
			logger.debug("List of offerType " + offerType.toString());
			//tx.commit();
		} catch (HibernateException e) {
			/*
			 * if (tx != null) tx.rollback();
			 */
			e.printStackTrace();
		} finally {
			if(session!=null)
				session.close();
				session=null;
		}
		return offerType;

	}

	public List<Integer> getOfferBasedOnTierId(int tierId,List<Integer> accountTypeOffer)
			throws Exception {

		Session session = HiberanteUtil.getSessionFactory().openSession();
		//Transaction tx = null;
		Query query = null;
		List<Integer> offerType = null;
		Criteria criteria = null;
		try {
			//tx = session.beginTransaction();
			criteria = session.createCriteria(OfferTierMappingTabDto.class).setProjection(Projections.projectionList().add(Projections.property("offerId")));
			criteria.add(Restrictions.in("offerId", accountTypeOffer));
			criteria.add(Restrictions.eq("tierId",tierId));
			criteria.addOrder(Order.asc("offerId"));
			offerType = criteria.list();
			logger.info("List of offerType " + offerType.toString());
			//tx.commit();
		} catch (HibernateException e) {
			/*
			 * if (tx != null) tx.rollback();
			 */
			e.printStackTrace();
		} finally {
			if(session!=null)
				session.close();
				session=null;
		}
		return offerType;

	}

	
	public List<LoyaltyProfileTabDTO> getLoyaltyProfileInfo(String txnId, String tableName, Long loyaltyId)
			throws CommonException {
		Session session = null;
		//Transaction transaction = null;
		Criteria criteria = null;
		ArrayList<LoyaltyProfileTabDTO> loyaltyProfileDetails = null;
		try {
			logger.info("Transaction ID : " + txnId + " LoyaltyId : " + loyaltyId
					+ " Getting loyalty linked numbers from " + tableName);
			session = HiberanteUtil.getSessionFactory().openSession();
			//transaction = session.beginTransaction();
			criteria = session.createCriteria(tableName).add(Restrictions.eq("loyaltyID",loyaltyId));
			loyaltyProfileDetails = (ArrayList<LoyaltyProfileTabDTO>) criteria.list();
			//transaction.commit();
		} catch (HibernateException e) {
			logger.error("Transaction ID : " + txnId + " LoyaltyId : " + loyaltyId
					+ " Account Number :  Hibernate Exception occured ", e);
			/*
			 * if (transaction != null) { transaction.rollback(); }
			 */
			throw new CommonException(e.getMessage());

		} catch (Exception e) {
			logger.error("Transaction ID : " + txnId + " LoyaltyId : " + loyaltyId
					+ " Account Number :   Exception occured ", e);
			/*
			 * if (transaction != null) { transaction.rollback(); }
			 */
			throw new CommonException(e.getMessage());

		} finally {

			if (session != null) {
				session.close();
			}
		}
		return loyaltyProfileDetails;
	}
	
	public List<OfferMasterTabDto> getOfferMasterDetails(int offerId,Date currentDate) throws Exception {

		Session session = HiberanteUtil.getSessionFactory().openSession();
		//Transaction tx = null;
		Query query = null;
		List<OfferMasterTabDto> offerMasterTabDto = null;
		try {
			//tx = session.beginTransaction();
			query = session.createQuery(" FROM OfferMasterTabDto where offerId=:offerId and endDate>=:endDate");
			query.setParameter("offerId", offerId);
			query.setParameter("endDate", currentDate);
			offerMasterTabDto = query.list();
			logger.debug("List of offerType filted based on offerId and endDate " + offerMasterTabDto.size());
			//tx.commit();
		} catch (HibernateException e) {
			/*
			 * if (tx != null) tx.rollback();
			 */
			e.printStackTrace();
		} finally {
			if(session!=null)
				session.close();
				session=null;
		}
		return offerMasterTabDto;

	}
	
	  public List<OfferNomenclatureTabDto> getOfferDetailsNomenclatureTab(int offerId,int languageId) throws Exception {

			Session session = HiberanteUtil.getSessionFactory().openSession();
			//Transaction tx = null;
			Query query = null;
			List<OfferNomenclatureTabDto> offerType = null;
			try {
				//tx = session.beginTransaction();
				query = session.createQuery(" FROM OfferNomenclatureTabDto where offerId=:offerId and languageId=:languageId");
				query.setParameter("offerId", offerId);
				query.setParameter("languageId", languageId);
				offerType = query.list();
				logger.debug("List of offerType based on OfferNomenclatureTab size " + offerType.toString());
				//tx.commit();
			} catch (HibernateException e) {
				/*
				 * if (tx != null) tx.rollback();
				 */
				e.printStackTrace();
			} finally {
				if(session!=null)
					session.close();
					session=null;
			}
			return offerType;

		}
	  
	  public List<MerchantNomenclatureTabDto> getOfferDetailsMerchantNomenclatureTab(int merchantId,int languageId) throws Exception {

			Session session = HiberanteUtil.getSessionFactory().openSession();
			//Transaction tx = null;
			Query query = null;
			List<MerchantNomenclatureTabDto> offerType = null;
			try {
				//tx = session.beginTransaction();
				query = session.createQuery(" FROM MerchantNomenclatureTabDto where merchantId=:merchantId and languageId=:languageId");
				query.setParameter("merchantId", merchantId);
				query.setParameter("languageId", languageId);
				offerType = query.list();
				logger.info("MerchantNomenClatureTab of offerType " + offerType.toString());
				//tx.commit();
			} catch (HibernateException e) {
				/*
				 * if (tx != null) tx.rollback();
				 */
				e.printStackTrace();
			} finally {
				if(session!=null)
					session.close();
					session=null;
			}
			return offerType;

		}
	  
	  public List<Integer> getOfferMasterAndMerchantMapping(int offerId) throws Exception {

			Session session = HiberanteUtil.getSessionFactory().openSession();
			//Transaction tx = null;
			Query query = null;
			List<Integer> merchantId = null;
			try {
				//tx = session.beginTransaction();
				query = session.createQuery("select merchantId FROM OfferMasterTabDto where offerId=:offerId and merchantId IS NOT NULL");
				query.setParameter("offerId", offerId);
				merchantId = query.list();
				logger.debug("List of offerType " + merchantId.size());
				//tx.commit();
			} catch (HibernateException e) {
				/*
				 * if (tx != null) tx.rollback();
				 */
				e.printStackTrace();
			} finally {
				if(session!=null)
					session.close();
					session=null;
			}
			return merchantId;

		}
	  
	  public List<Integer> getOfferMasterTierMapping(int offerId) throws Exception {

			Session session = HiberanteUtil.getSessionFactory().openSession();
			//Transaction tx = null;
			Query query = null;
			List<Integer> discount = null;
			try {
				//tx = session.beginTransaction();
				query = session.createQuery("select discount FROM OfferTierMappingTabDto where offerId=:offerId");
				query.setParameter("offerId", offerId);
				discount = query.list();
				logger.info("List of offerType " + discount.size());
				//tx.commit();
			} catch (HibernateException e) {
				/*
				 * if (tx != null) tx.rollback();
				 */
				e.printStackTrace();
			} finally {
				if(session!=null)
					session.close();
					session=null;
			}
			return discount;

		}
	  
		public List<Integer> getOfferMasterDetailsBasedOfferTypeID(int offerTypeId) throws Exception {

			Session session = HiberanteUtil.getSessionFactory().openSession();
			//Transaction tx = null;
			Query query = null;
			List<Integer> offerMasterTabDto = null;
			try {
				//tx = session.beginTransaction();
				query = session.createQuery("select offerId FROM OfferMasterTabDto where offerTypeId=:offerTypeId ORDER BY offerId ASC");
				query.setParameter("offerTypeId", offerTypeId);
				offerMasterTabDto = query.list();
				logger.info("383 List of offerType " + offerMasterTabDto.size());
				//tx.commit();
			} catch (HibernateException e) {
				/*
				 * if (tx != null) tx.rollback();
				 */
				e.printStackTrace();
			} finally {
				if(session!=null)
					session.close();
					session=null;
			}
			return offerMasterTabDto;

		}
		
		public List<Integer> getOfferMasterDetailsBasedOfferTypeIDMerchantId(int offerTypeId , int MerchantId) throws Exception {

			Session session = HiberanteUtil.getSessionFactory().openSession();
			//Transaction tx = null;
			Query query = null;
			List<Integer> offerMasterTabDto = null;
			try {
				//tx = session.beginTransaction();
				logger.info("OfferTypeId "+offerTypeId +"MerchantId "+MerchantId);
				query = session.createQuery("select offerId FROM OfferMasterTabDto where offerTypeId=:offerTypeId and merchantId=:merchantId");
				query.setParameter("offerTypeId", offerTypeId);
				query.setParameter("merchantId", MerchantId);
				offerMasterTabDto = query.list();
				logger.debug("List of offerType " + offerMasterTabDto.size());
				//tx.commit();
			} catch (HibernateException e) {
				/*
				 * if (tx != null) tx.rollback();
				 */
				e.printStackTrace();
			} finally {
				if(session!=null)
					session.close();
					session=null;
			}
			return offerMasterTabDto;

		}
		
		  public List<OfferTeleAttruibutes> getOfferCategoryMapping(int offerId) throws Exception {

				Session session = HiberanteUtil.getSessionFactory().openSession();
				//Transaction tx = null;
				Query query = null;
				List<OfferTeleAttruibutes> offerTeleAttributeList = null;
				try {
					//tx = session.beginTransaction();
					query = session.createQuery(" FROM OfferTeleAttruibutes where offerId=:offerId");
					query.setParameter("offerId", offerId);
					offerTeleAttributeList = query.list();
					logger.info("List of offerType " + offerTeleAttributeList.size());
					//tx.commit();
				} catch (HibernateException e) {
					/*
					 * if (tx != null) tx.rollback();
					 */
					e.printStackTrace();
				} finally {
					if(session!=null)
						session.close();
						session=null;
				}
				return offerTeleAttributeList;

			}
		  
		  public List<OfferMasterTabDto> getTelecomOffersFromOfferMaster() throws Exception {

				Session session = HiberanteUtil.getSessionFactory().openSession();
				//Transaction tx = null;
				Query query = null;
				List<OfferMasterTabDto> telecomOffers = null;
				try {
					//tx = session.beginTransaction();
					query = session.createQuery(" FROM OfferMasterTabDto where offerTypeId=:offerTypeId");
					query.setParameter("offerTypeId", 5);
					telecomOffers = query.list();
					logger.info("List of offerType " + telecomOffers.size());
					//tx.commit();
				} catch (HibernateException e) {
					/*
					 * if (tx != null) tx.rollback();
					 */
					e.printStackTrace();
				} finally {
					if(session!=null)
						session.close();
						session=null;
				}
				return telecomOffers;

			}
		  
		  public List<Integer> getOfferMasterDetailsBasedOfferTypeIDAndChannelValidation(int offerTypeId,List<Integer> accountTypeOffer) throws Exception {

				Session session = HiberanteUtil.getSessionFactory().openSession();
				//Transaction tx = null;
				Query query = null;
				List<Integer> offerMasterTabDto = null;
				Criteria criteria = null;
				try {
					//tx = session.beginTransaction();
					criteria = session.createCriteria(OfferMasterTabDto.class).setProjection(Projections.projectionList().add(Projections.property("offerId")));
					criteria.add(Restrictions.in("offerId", accountTypeOffer));
					criteria.add(Restrictions.eq("offerTypeId",offerTypeId));
					criteria.addOrder(Order.asc("offerId"));
					offerMasterTabDto = criteria.list();
					logger.info("383 List of offerType " + offerMasterTabDto.size());
					//tx.commit();
				} catch (HibernateException e) {
					/*
					 * if (tx != null) tx.rollback();
					 */
					e.printStackTrace();
				} finally {
					if(session!=null)
						session.close();
						session=null;
				}
				return offerMasterTabDto;

			}
	  
}
