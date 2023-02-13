package com.sixdee.imp.dao;

import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.EncrptPassword;
import com.sixdee.imp.common.util.LoyaltyRandomNumber;
import com.sixdee.imp.dto.ADSLTabDTO;
import com.sixdee.imp.dto.AccountNumberTabDTO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.LoyaltyRegisteredNumberTabDTO;
import com.sixdee.imp.dto.LoyaltyTransactionTabDTO;
import com.sixdee.imp.dto.NationalNumberTabDTO;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;

public class RegisterAcctDAO {

	Logger logger=Logger.getLogger(RegisterAcctDAO.class);
	
	
	public boolean insertSubscriberNumberDetails(Session session,SubscriberNumberTabDTO subscriberNumberTabDTO)throws Exception
	{
		boolean flag=false;
		String tableName=null;
		TableInfoDAO infoDAO=new TableInfoDAO();
		try{
			
			tableName=infoDAO.getSubscriberNumberTable(subscriberNumberTabDTO.getSubscriberNumber()+"");
			
			/*String sql=" INSERT INTO "+tableName+" (SUBSCRIBER_NUMBER,ACCOUNT_NO,LOYALTY_ID,POINTS) VALUES (?,?,?,?)" ;
			
			Query query=session.createSQLQuery(sql);
			query.setLong(0,subscriberNumberTabDTO.getSubscriberNumber());
			
			if(subscriberNumberTabDTO.getAccountNumber()==null)
			  query.setBigInteger(1,null);
			else
			 query.setLong(1,subscriberNumberTabDTO.getAccountNumber());
			
			query.setLong(2,subscriberNumberTabDTO.getLoyaltyID());
			query.setInteger(3,0);*/
			long start=System.currentTimeMillis();
			
			session.save(tableName,subscriberNumberTabDTO);
			
			System.out.println("Insert Subcriber "+(System.currentTimeMillis()-start));
			
			flag=true;
			
		}catch (Exception e) {
			flag=false;
			e.printStackTrace();
			throw e;
			
		}finally{ 
			infoDAO=null;
		}
		
		return flag;
		
	}//insertSubscriberNumberDetails
	
	
	
	public boolean insertAccountNumberDetails(Session session,AccountNumberTabDTO accountNumberTabDTO)throws Exception
	{
		boolean flag=false;
		String tableName=null;
		TableInfoDAO infoDAO=new TableInfoDAO();
		try{
			
			tableName=infoDAO.getAccountNumberTable(accountNumberTabDTO.getAccountNumber()+"");
			
			/*String sql=" INSERT INTO "+tableName+" (ACCOUNT_NO,LOYALTY_ID,POINTS) VALUES (?,?,?)" ;
			
			Query query=session.createSQLQuery(sql);
			
			query.setLong(0,accountNumberTabDTO.getAccountNumber());
			query.setLong(1,accountNumberTabDTO.getLoyaltyID());
			query.setInteger(2,0);*/
			
			session.save(tableName,accountNumberTabDTO);
			
			flag=true;
			
		}catch (Exception e) {
			flag=false;
			e.printStackTrace();
			throw e;
		}finally{ 
			infoDAO=null;
		}
		
		return flag;
		
	}//insertAccountNumberDetails
	
	
	
	public boolean insertNationalNumberDetails(Session session,NationalNumberTabDTO nationalNumberTabDTO)throws Exception
	{
		boolean flag=false;
		String tableName=null;
		TableInfoDAO infoDAO=new TableInfoDAO();
		try{
			
			tableName=infoDAO.getNationalNumberTable(nationalNumberTabDTO.getNationalNumber()+"");
			
			
			/*String sql=" INSERT INTO "+tableName+" (NATIONAL_ID,LOYALTY_ID) VALUES (?,?)" ;
			
			Query query=session.createSQLQuery(sql);
			
			query.setLong(0,nationalNumberTabDTO.getNationalNumber());
			query.setLong(1,nationalNumberTabDTO.getLoyaltyID());*/
			
			session.save(tableName,nationalNumberTabDTO);
			
				flag=true;

			
			
		}catch (Exception e) {
			flag=false;
			e.printStackTrace();
			throw e;
		}finally{
			infoDAO=null;
		}
		
		return flag;
		
	}//insertNationalNumberDetails
	
	
	
	public boolean insertLoyaltyRegisteredNumberDetails(Session session,LoyaltyRegisteredNumberTabDTO loyaltyRegisteredNumberTabDTO)throws Exception
	{
		boolean flag=false;
		String tableName=null;
		TableInfoDAO infoDAO=new TableInfoDAO();
		try{
			
			tableName=infoDAO.getLoyaltyRegisteredNumberTable(loyaltyRegisteredNumberTabDTO.getLoyaltyID()+"");
			
			
			/*String sql=" INSERT INTO "+tableName+" (LOYALTY_ID,LINKED_NO,ACCOUNT_NO) VALUES (?,?,?)" ;
			
			Query query=session.createSQLQuery(sql);
			
			query.setLong(0,loyaltyRegisteredNumberTabDTO.getLoyaltyID());
			query.setString(1,loyaltyRegisteredNumberTabDTO.getLinkedNumber());
			if(loyaltyRegisteredNumberTabDTO.getAccountNumber()==null)
				  query.setBigInteger(2,null);
			else
				 query.setLong(2,loyaltyRegisteredNumberTabDTO.getAccountNumber());
			*/
			
			session.save(tableName,loyaltyRegisteredNumberTabDTO);
			
				flag=true;

			
			
		}catch (Exception e) {
			flag=false;
			e.printStackTrace();
			throw e;
		}finally{
			infoDAO=null;
		}
		
		return flag;
		
	}//insertLoyaltyRegisteredNumberDetails
	
	
	
	public boolean insertLoyaltyProfileDetails(Session session,LoyaltyProfileTabDTO loyaltyProfileTabDTO)throws Exception
	{
		boolean flag=false;
		SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
		String tableName=null;
		TableDetailsDAO tableDetailsDAO=new TableDetailsDAO();
		TableInfoDAO infoDAO=new TableInfoDAO();
		try{
			
			int loopCount=1;
			while(loopCount<=5)
			{
				
				// Generating Loyalty ID
				long loyaltyId=new LoyaltyRandomNumber().getLoyaltyID();
				logger.info("Generated Loyalty ID "+loyaltyId);
			    loopCount++;
				if(tableDetailsDAO.isLoyaltyIdExists(loyaltyId))
				{
					continue;
				}
				
				//loyaltyId=Long.parseLong(String.format("%010d",loyaltyId).substring(0,10));
				loyaltyProfileTabDTO.setLoyaltyID(loyaltyId);
				break;
			
			}
			
			
			if(loyaltyProfileTabDTO.getLoyaltyID()==null||loopCount>5)
			{
				logger.info("Loyalty ID Unique Generation Failed ");
				throw new CommonException(Cache.getServiceStatusMap().get("LOYALTY_FAIL_01").getStatusCode(),Cache.getServiceStatusMap().get("LOYALTY_FAIL_01").getStatusDesc());
			}
			
			
			
			tableName=infoDAO.getLoyaltyProfileTable(loyaltyProfileTabDTO.getLoyaltyID()+"");
			
			
			if(loyaltyProfileTabDTO.getPin()==null||loyaltyProfileTabDTO.getPin().trim().equals(""))
			{
				// Generating Four Digit PIN
				loyaltyProfileTabDTO.setPin(new LoyaltyRandomNumber().getPIN());
			}
			loyaltyProfileTabDTO.setEncryptPin(new EncrptPassword().encryptPassword(loyaltyProfileTabDTO.getPin()));
			
			/*String sql=" INSERT INTO "+tableName+" (LOYALTY_ID,PIN,CONTACT_NUMBER,FIRSTNAME,LASTNAME,DEFAULTLANGUAGE,EMAIL_ID,DOB,ADDRESS," +
					"OCCUPATION,VIP_CODE,CATEGORY,NATIONAL_ID) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)" ;
			
			Query query=session.createSQLQuery(sql);
			
			int index=0;
			query.setLong(index++,loyaltyProfileTabDTO.getLoyaltyID());
			query.setLong(index++,loyaltyProfileTabDTO.getPin());
			query.setString(index++,loyaltyProfileTabDTO.getContactNumber());
			query.setString(index++,loyaltyProfileTabDTO.getFirstName());
			query.setString(index++,loyaltyProfileTabDTO.getLastName());
			query.setString(index++,loyaltyProfileTabDTO.getDefaultLanguage());
			query.setString(index++,loyaltyProfileTabDTO.getEmailID());
			query.setDate(index++,dateFormat.parse(loyaltyProfileTabDTO.getDateOfBirth()));
			query.setString(index++,loyaltyProfileTabDTO.getAddress());
			query.setString(index++,loyaltyProfileTabDTO.getOccupation());
			query.setString(index++,loyaltyProfileTabDTO.getVipCode());
			query.setString(index++,loyaltyProfileTabDTO.getCategory());
			query.setString(index++,loyaltyProfileTabDTO.getNationalID());*/
			
			session.save(tableName,loyaltyProfileTabDTO);
			
				flag=true;

			
			
		}
		catch (Exception e) {
			flag=false;
			logger.info(" "+Cache.getServiceStatusMap().get("LOYALTY_FAIL_01").getStatusDesc(),e);
			
			throw e;
			
			/*if(e instanceof CommonException)
			{
				throw (CommonException)e;
				
			}else{
				CommonException commonException=new CommonException(Cache.getServiceStatusMap().get("LOYALTY_FAIL_01").getStatusCode(),Cache.getServiceStatusMap().get("LOYALTY_FAIL_01").getStatusDesc());
				throw commonException;
			}*/
		}finally{ 
			tableDetailsDAO=null;
			infoDAO=null;
		}
		
		return flag;
		
	}//insertLoyaltyProfileDetails
	
	
	
	public boolean insertADSLDetails(Session session,ADSLTabDTO tabDTO)throws Exception
	{
		boolean flag=false;
		String tableName=null;
		TableInfoDAO infoDAO=new TableInfoDAO();
		try{
			
			tableName=infoDAO.getADSLNumberTable(tabDTO.getADSLNumber());
			
			/*String sql=" INSERT INTO "+tableName+" (ADSL_NO,ACCOUNT_NO,LOYALTY_ID) VALUES (?,?,?)" ;
			
			Query query=session.createSQLQuery(sql);
			query.setString(0,tabDTO.getADSLNumber());
			
			if(tabDTO.getAccountNumber()==null)
			  query.setBigInteger(1,null);
			else
			 query.setLong(1,tabDTO.getAccountNumber());
			
			query.setLong(2,tabDTO.getLoyaltyID());*/
			
			session.save(tableName,tabDTO);
			
				flag=true;

			
			
		}catch (Exception e) {
			flag=false;
			e.printStackTrace();
			throw e;
			
		}finally{ 
			infoDAO=null;
		}
		
		return flag;
		
	}//insertADSLDetails
	
	
	public boolean insertLoyaltyTransactionDetails(Session session,LoyaltyTransactionTabDTO tabDTO)throws Exception
	{
		boolean flag=false;
		String tableName=null;
		TableInfoDAO infoDAO=new TableInfoDAO();
		try{
			
			tableName=infoDAO.getLoyaltyTransactionTable(""+tabDTO.getLoyaltyID());
			session.save(tableName,tabDTO);
			flag=true;
			
		}catch (Exception e) {
			flag=false;
			e.printStackTrace();
			throw e;
			
		}finally{ 
			infoDAO=null;
		}
		
		return flag;
		
	}//insertLoyaltyTransactionDetails
	
	
	
}
