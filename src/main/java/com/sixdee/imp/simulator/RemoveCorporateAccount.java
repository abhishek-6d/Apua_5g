package com.sixdee.imp.simulator;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import com.sixdee.imp.common.util.EncrptPassword;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.service.AccountManagement;
import com.sixdee.imp.service.serviceDTO.common.Data;
import com.sixdee.imp.service.serviceDTO.req.AccountDTO;
import com.sixdee.imp.service.serviceDTO.resp.ResponseDTO;

public class RemoveCorporateAccount 
{
	private Logger log = Logger.getLogger(RemoveCorporateAccount.class);
	
	
	public void start()
	{
		log.info("Getting all loyalty id from corporate table ");
		List<Object[]> loyaltyIds = getAllLoyaltyIds();
		log.info("Getting all loyalty id from corporate table DONEEEEEEEEEe");
		callDeleteAPI(loyaltyIds);
		log.info("<<<<<<<<<<<<completed>>>>>>>>>>>>>>>>>>>>>>");
		 
	}

	private void callDeleteAPI(List<Object[]> loyaltyIds)
	{
		AccountManagement am = new AccountManagement();
		AccountDTO dto = null;
		Object all[] = null;
		Data[] d = null;
		ResponseDTO resp = null;
		
		Iterator<Object[]> it = loyaltyIds.iterator();
		while(it.hasNext())
		{
			all = it.next();
			log.info(all[0]+" loyalty id processing stated");
			try
			{
				dto = new AccountDTO();
				dto.setTransactionId(System.currentTimeMillis() + "");
				dto.setChannel("WEB");
				dto.setMoNumber(all[0] + "");
				dto.setPin(Integer.parseInt(new EncrptPassword().decryptPassWord(all[1]+"")));
				d = new Data[1];
				d[0] = new Data();
				d[0].setName("status");
				d[0].setValue("6");
				dto.setData(d);

				resp = am.deleteLoyaltyAccount(dto);
				log.info(all[0] + " loyalty id rescode = " + resp.getStatusCode());
				log.info(all[0] + " loyalty id resdesc = " + resp.getStatusDescription());

				if (resp.getStatusCode().equalsIgnoreCase("SC0000"))
					createAccountAPI(all);
			}
			catch (Exception e) {
				log.info(all[0]+" loyalty id Exceptionnnnnnnnnnn =",e);
			}
			log.info(all[0]+" loyalty id processing completed **********");
		}
		
	}
	
	private void createAccountAPI(Object all[]) throws Exception
	{
		log.info("Calling create account for number = "+all[2]);
		AccountManagement ac = new AccountManagement();
		
		AccountDTO dto = new AccountDTO();
		dto.setChannel("WEB");
		dto.setTransactionId(System.currentTimeMillis()+"");
		dto.setMoNumber(all[2]+"");
		dto.setRegisterNumbers(new String[]{"ALL"});
		dto.setTimestamp(System.currentTimeMillis()+"");
		
		ac.createLoyaltyAccount(dto);
		
	}
	
	private List<Object[]> getAllLoyaltyIds()
	{
		Session session = null;
		Query query = null;
		List<Object[]> loyaltyIds = null;
		try
		{
			session = HiberanteUtil.getSessionFactory().openSession();
			
			query = session.createSQLQuery("SELECT DISTINCT LOYALTY_ID,PIN,CONTACT_NUMBER FROM CORPORATE_LINES");
			
			loyaltyIds = query.list();
		}
		catch (Exception e) 
		{
			log.error("Exception ",e);
		}
		finally
		{
			query = null;
			if(session!=null)
				session.close();
		}
		
		return loyaltyIds;
	}
	
}

