package com.sixdee.imp.simulator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dao.TableInfoDAO;
import com.sixdee.imp.dto.LoyaltyTransactionTabDTO;
import com.sixdee.imp.dto.VoucherOrderDetailsDTO;

public class InsertVouchTransaction 
{
	private Logger log = Logger.getLogger(InsertVouchTransaction.class);
	
	public void getAllVoucher()
	{
		Session session = null;
		Criteria ctr = null;
		List<VoucherOrderDetailsDTO> allVouchers = null;
		try
		{
			log.info("Getting voucher Details ----");
			
			Calendar cal = Calendar.getInstance();
			cal.set(2014, 3, 1, 0, 0, 0);
			System.out.println(cal.getTime());
			
			session = HiberanteUtil.getSessionFactory().openSession();
			ctr = session.createCriteria(VoucherOrderDetailsDTO.class);
			ctr.add(Restrictions.ge("orderDate", cal.getTime()));
			
			allVouchers = ctr.list();
			
			log.info("Got All voucher Detail -----"+allVouchers.size());
		}
		catch (Exception e) {
			log.error("Exception while picking vouchers =",e);
		}
		finally
		{
			if(session!=null)
				session.close();
		}
		insertTransaction(allVouchers);
		
	}
	
	/*public void getAllNonZero(BigDecimal loyaltyId)
	{
		Session session = null;
		Criteria ctr = null;
		Query query = null;
		TableInfoDAO info = new TableInfoDAO();
		LoyaltyTransactionTabDTO loyaltyDTO = null;
		try
		{
			log.info("Getting getAllNonZero Details ----");
			session = HiberanteUtil.getSessionFactory().openSession();
			
			query = session.createQuery(" from "+info.getLoyaltyTransactionTable(loyaltyId+"")+" where loyaltyID=?"+
								" and statusID in (5,9) and monthIndex in (9,10,11,12,1,2,3,4) order by createTime");
			query.setParameter(0, Long.parseLong(loyaltyId+""));
			query.setMaxResults(1);
			
			loyaltyDTO = (LoyaltyTransactionTabDTO)query.uniqueResult();
			
			session.evict(loyaltyDTO);
			
			if(loyaltyDTO !=null && loyaltyDTO.getStatusID()==5 && loyaltyDTO.getPreStatusPoints()>0)
			{
				loyaltyDTO.setId(null);
				loyaltyDTO.setMonthIndex(null);
				loyaltyDTO.setStatusID(9);
				
				loyaltyDTO.setCurStatusPoints(loyaltyDTO.getPreStatusPoints());
				loyaltyDTO.setPreStatusPoints(0.0);
				loyaltyDTO.setStatusPoints(loyaltyDTO.getCurStatusPoints());
				if(loyaltyDTO.getPreRewardPoints()<=0)
				{
					loyaltyDTO.setCurRewardPoints(0.0);
				}
				loyaltyDTO.setRewardPoints(0.0);
				
				session.beginTransaction();
				session.save(info.getLoyaltyTransactionTable(loyaltyId+""),loyaltyDTO);
				session.getTransaction().commit();
			}
			
			
		}
		catch (Exception e) {
			log.error("Exception while NON zero =",e);
		}
		finally
		{
			if(session!=null)
				session.close();
			
			loyaltyDTO = null;
			info = null;
		}
		
	}*/
	
	private void insertTransaction(List<VoucherOrderDetailsDTO> allVouchers)
	{
		Session session = null;
		Transaction trx = null;
		LoyaltyTransactionTabDTO transactionTabDTO = null;
		TableInfoDAO info = new TableInfoDAO();
		try
		{
			for(VoucherOrderDetailsDTO dto : allVouchers)
			{
				try
				{
					session = HiberanteUtil.getSessionFactory().openSession();
					trx = session.beginTransaction();
					transactionTabDTO = new LoyaltyTransactionTabDTO();
					transactionTabDTO.setLoyaltyID(dto.getLoyalityID());
					transactionTabDTO.setChannel("2");
					transactionTabDTO.setCreateTime(dto.getCreateDate());
					transactionTabDTO.setCurRewardPoints(0.0);
					transactionTabDTO.setCurStatusPoints(0.0);
					transactionTabDTO.setPackageId(Integer.parseInt(dto.getVoucherNumber()));
					transactionTabDTO.setPreRewardPoints(0.0);
					transactionTabDTO.setPreStatusPoints(0.0);
					transactionTabDTO.setRewardPoints(Double.parseDouble(dto.getRedeemPoints()+""));
					transactionTabDTO.setStatusID(8);
					transactionTabDTO.setVoucherOrderID(dto.getOrderId().toString());
					
					session.save(info.getLoyaltyTransactionTable(dto.getLoyalityID().toString()),transactionTabDTO);
					
					trx.commit();
					log.info(dto.getLoyalityID()+" Loyalty Id Saved in table "+info.getLoyaltyTransactionTable(dto.getLoyalityID()+""));
					session.close();
				}
				catch (Exception e) {
					e.printStackTrace();
					if(trx!=null)
						trx.rollback();
					if(session!=null)
						session.close();
				}
			}
			
			
		}
		catch (Exception e) {
			log.error("Exception while transaction insertion ",e);
			if(trx!=null)
				trx.rollback();
		}
		finally
		{
		}
		
		log.info("OVER OVER OVER OVER");
		
	}
	
	/*public void getAllLoyaltyId()
	{
		Session session = null;
		Query query = null;
		List<BigDecimal> list = new ArrayList<BigDecimal>(); 
		try
		{
			session = HiberanteUtil.getSessionFactory().openSession();

			session = HiberanteUtil.getSessionFactory().openSession();
			query = session.createSQLQuery("SELECT LOYALTY_ID FROM LOYALTY_PROFILE_0");
			
			list = query.list();			
			log.info("Got All Loyalty Ids....");
			
		}
		catch (Exception e) 
		{
			log.error("Exception ",e);
		}
		finally
		{
			if(session!=null)
				session.close();
			
			query = null;
		}
		
		for(BigDecimal lid : list)
		{
			try
			{
				getAllNonZero(lid);
			}
			catch (Exception e) {
				log.error("Exception for loyalty Id ="+lid);
			}
		}
		
	}*/
}
