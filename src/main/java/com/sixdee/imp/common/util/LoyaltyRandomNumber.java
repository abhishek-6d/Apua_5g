package com.sixdee.imp.common.util;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dao.VoucherPromoDAO;
import com.sixdee.imp.dto.RedeemPointsDTO;
import com.sixdee.imp.dto.VoucherPromoTranverseDTO;

public class LoyaltyRandomNumber 
{
	private Logger logger=Logger.getLogger(LoyaltyRandomNumber.class);
	Random rnd = new Random();
	StringBuilder sb = null;
	private SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yy");
	Date date = new Date();
	
	public String getPIN()
	{
		return getRandomNumber(Integer.parseInt((Cache.configParameterMap.get("LOYALTY_PASSWORD_SIZE")==null?"4":Cache.configParameterMap.get("LOYALTY_PASSWORD_SIZE").getParameterValue())));
		
	}//getPIN
	
	public Long getLoyaltyID()
	{
		return Long.parseLong(getRandomNumber(Integer.parseInt(Cache.configParameterMap.get("LOYALTY_ID_SIZE").getParameterValue())));
		
	}//getLoyaltyID
	
	
	public String getOrderNumber(int size)
	{
	return (getRandomNumber(size));
	}//getOrderNumber
	
	public boolean getPackIdBoolean(int packId)throws Exception
	{
		
		
		
			Session session=null;
			Transaction transaction=null;
			String promoCode=null;
			boolean flowFlag=false;
			
			try{
				
				session=HiberanteUtil.getSessionFactory().openSession();
				transaction=session.beginTransaction();
				
				
				String sql=" SELECT PACK_ID FROM  REDEMPTION_MERCHANT  WHERE PACK_ID=?";
				
				logger.info("");
				
				Query query=session.createSQLQuery(sql)
							.setParameter(0,packId);
				
			
				List<Object[]> list=query.list();
				
				if(list!=null && list.size()>0){
					flowFlag=true;
					
				}
				
			}catch (Exception e) {
				e.printStackTrace();
				throw e;
			}finally{
				if(session!=null)
					session.close();
				session=null;
			}
			
			
		
		
		
		return flowFlag;
	}//getOrderNumber
	
	
	
	
	public String getOrderNumberRedeem(RedeemPointsDTO redeemPointsDTO)throws Exception
	{
		
		
		
			Session session=null;
			Transaction transaction=null;
			String promoCode=null;
			
			try{
				
				session=HiberanteUtil.getSessionFactory().openSession();
				transaction=session.beginTransaction();
				
				
				String sql=" SELECT PROMO_CODE,EXPIRY_DATE FROM  REDEMPTION_MERCHANT  WHERE PACK_ID=?  AND ASSIGNED=0 ";
				
				logger.info("");
				
				Query query=session.createSQLQuery(sql)
							.setParameter(0,redeemPointsDTO.getPackageId());
				
			
				List<Object[]> list=query.list();
				
				if(list!=null && list.size()>0){
					for(Object[] obj:list)
					{
						//logger.info("========obj[0].toString()======"+obj[1]);
						if(obj[1]!=null){
							logger.info("========(new Date()))======"+dateFormat.format(new Date()));
							logger.info("========dateFormat.parse(obj[1].toString())======"+dateFormat.format(obj[1]));
							int result =(((Date)obj[1]).compareTo(new Date()));
							logger.info("========RESULT ID======"+result);
							if( result>=0){
								
								promoCode=obj[0].toString();
								//logger.info("========PROMOCODE in DATE CHAECK======"+obj[0].toString());
								break;
							}
						}else{
							promoCode=obj[0].toString();
								//logger.info("========PROMOCODE in NORMAL======"+obj[0].toString());
								break;
						}
						
					}		
					logger.info(" Service : VoucherPromo -- Transaction ID :"+promoCode+" MerchantID "+redeemPointsDTO.getPackageId()+"PromoCode is"+promoCode);
					
					
				}/*else{
					logger.info("Promocode is exhausted for the Pack with id:"+redeemPointsDTO.getPackageId());
					redeemPointsDTO.setStatusCode(Cache.getServiceStatusMap().get("VOUCHER_PROMO_EXHAUST_"+redeemPointsDTO.getDefaultLanguage()).getStatusCode());
					redeemPointsDTO.setStatusDesc(Cache.getServiceStatusMap().get("VOUCHER_PROMO_EXHAUST_"+redeemPointsDTO.getDefaultLanguage()).getStatusDesc());
					throw new CommonException(Cache.getServiceStatusMap().get("VOUCHER_PROMO_EXHAUST_"+redeemPointsDTO.getDefaultLanguage()).getStatusCode(),Cache.getServiceStatusMap().get("VOUCHER_PROMO_EXHAUST_"+redeemPointsDTO.getDefaultLanguage()).getStatusDesc());
				
				}*/
				
			}catch (Exception e) {
				e.printStackTrace();
				throw e;
			}finally{
				if(session!=null)
					session.close();
				session=null;
			}
			
			
		
		
		
		return promoCode;
	}//getOrderNumber
	
	

	public void blockReddemPackage(String oderID,RedeemPointsDTO redeemPointsDTO)throws Exception
	{
		
		
		
			Session session=null;
			Transaction transaction=null;
			String promoCode=null;
			
			try{
				
				session=HiberanteUtil.getSessionFactory().openSession();
				transaction=session.beginTransaction();
				int loopCounter=0;
				while(loopCounter<Integer.parseInt(Cache.getCacheMap().get("RETRY_COUNT")))
				{
								
				String sql=" UPDATE REDEMPTION_MERCHANT SET ASSIGNED=1 WHERE PACK_ID=?  AND ASSIGNED=0 AND PROMO_CODE=?";
				
				logger.info("");
				
				Query query=session.createSQLQuery(sql)
							.setParameter(0,redeemPointsDTO.getPackageId())
							.setParameter(1,oderID);
				
			
				int i=query.executeUpdate();
				
				
				if(i>0){
					transaction.commit();
					break;
					}
					else{
					transaction.rollback();
					}
					loopCounter++;
				
				}//while
				if(loopCounter>=5)
				{
					//logger.info(redeemPointsDTO.getTranscationId()+": System is busy.");
					//throw new CommonException(Cache.getServiceStatusMap().get("REW_FAIL_"+calculationDTO.getDefaultLanguage()).getStatusCode(),Cache.getServiceStatusMap().get("REW_FAIL_"+calculationDTO.getDefaultLanguage()).getStatusDesc());
					
				}
				
			}catch (Exception e) {
				e.printStackTrace();
				throw e;
			}finally{
				if(session!=null)
					session.close();
				session=null;
			}
		
	}//blockRedeemNumber
	
	private String getRandomNumber(int size) 
	{
	    sb = new StringBuilder(size);
	    for(int i=0; i < size; i++)
	        sb.append((char)('0' + rnd.nextInt(10)));
	    
	    String number = sb.toString();
	    if(number.startsWith("0"))
	    {
	    	sb = null;
	    	number = getRandomNumber(size);
	    }
	    sb = null;
	    return number;
	}//getRandomNumber
	
	
	
	
}
