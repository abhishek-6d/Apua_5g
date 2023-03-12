package com.sixdee.imp.dao;

import org.apache.log4j.Logger;

import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;

public class TableInfoDAO {

	
	private Logger logger = Logger.getLogger(TableInfoDAO.class);
	public String getSubscriberNumberDBTable(String subscriberNumber)
	{
		//String tablePrefix="SUBSCRIBER_NUMBER";
		String tablePrefix="SUBSCRIBER_NUMBER";
		int index=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NO_SUFFIX_LENGTH").getParameterValue());
		String tableName="_0";
		if(index>0)
		  tableName="_"+subscriberNumber.substring(subscriberNumber.length()-index);
		
		
		return tablePrefix+tableName;
		
	}//getSubscriberNumberTable
	
	
	public String getSubscriberNumberProfileDBTable(String subscriberNumber)
	{
		//String tablePrefix="SUBSCRIBER_NUMBER";
		String tablePrefix="PROFILE_CDR";
		int index=Integer.parseInt(Cache.getConfigParameterMap().get("PROFILE_DB_SUB_SUFFIX_LENGTH").getParameterValue());
		String tableName="_0";
		if(index>0)
		  tableName="_"+subscriberNumber.substring(subscriberNumber.length()-index);
		
		
		return tablePrefix+tableName;
		
	}//getSubscriberNumberTable
	
	public String getAccountNumberDBTable(String accountNumber)
	{
		String tablePrefix="ACCOUNT_NUMBER";
		int index=Integer.parseInt(Cache.getConfigParameterMap().get("ACCOUNT_NO_SUFFIX_LENGTH").getParameterValue());
		String tableName="_0";
		if(index>0)
		 tableName="_"+accountNumber.substring(accountNumber.length()-index);
		
		return tablePrefix+tableName;
		
	}//getAccountNumberTable
	
	public String getNationalNumberDBTable(String nationnalNumber)
	{
		String tablePrefix="NATIONAL_NUMBER";
		int index=Integer.parseInt(Cache.getConfigParameterMap().get("NATIONAL_NO_SUFFIX_LENGTH").getParameterValue());
		String tableName="_0";
		if(index>0)
		{
		  int number=0;
		  if(Character.isDigit(nationnalNumber.toUpperCase().charAt(nationnalNumber.length()-index))){
			  number=Integer.parseInt(nationnalNumber.toUpperCase().charAt(nationnalNumber.length()-index)+"");
		  }else{
			  number=(int)nationnalNumber.toUpperCase().charAt(nationnalNumber.length()-index);
			  if(number>=65&&number<=90)
			    number=(number-65)/3;
			  else
			    number=9;
		  }
		  tableName="_"+number;
		}
				
		return tablePrefix+tableName;
		
	}//getNationalNumberTable
	
	
	public String getLoyaltyProfileDBTable(String loyaltyNumber)
	{
		String tablePrefix="LOYALTY_PROFILE";
		int index=Integer.parseInt(Cache.getConfigParameterMap().get("LOYALTY_NO_SUFFIX_LENGTH").getParameterValue());
		String tableName="_0";
		if(index>0)
		 tableName="_"+loyaltyNumber.substring(loyaltyNumber.length()-index);
		
		return tablePrefix+tableName;
		
	}//getLoyaltyProfileTable
	
	
	public String getLoyaltyRegisteredNumberDBTable(String loyaltyRegisteredNumber)
	{
		String tablePrefix="LOYALTY_REGISTERED_NUMBER";
		int index=Integer.parseInt(Cache.getConfigParameterMap().get("LOYALTY_NO_SUFFIX_LENGTH").getParameterValue());
		String tableName="_0";
		if(index>0)
		 tableName="_"+loyaltyRegisteredNumber.substring(loyaltyRegisteredNumber.length()-index);
		
		return tablePrefix+tableName;
		
	}//getLoyaltyRegisteredNumberTable
	
	public String getADSLNumberDBTable(String ADSLNumberTable)
	{
		String tablePrefix="ADSL_NUMBER";
		int index=Integer.parseInt(Cache.getConfigParameterMap().get("ADSL_NO_SUFFIX_LENGTH").getParameterValue());
		String tableName="_0";
		if(index>0)
		{
		  int number=0;
		  if(Character.isDigit(ADSLNumberTable.toUpperCase().charAt(ADSLNumberTable.length()-index))){
			  number=Integer.parseInt(ADSLNumberTable.toUpperCase().charAt(ADSLNumberTable.length()-index)+"");
		  }else{
			  number=(int)ADSLNumberTable.toUpperCase().charAt(ADSLNumberTable.length()-index);
		      number=(number-65)/3;
		  }
		  tableName="_"+number;
		}
		
		return tablePrefix+tableName;
		
	}//getADSLNumberTable

	
	public String getSubscriberTransactionHistoryDBTable(String subscriberNumber)
	{
		String tablePrefix="SUB_TRA_HIS";
		int index=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NO_TAB_INDEX").getParameterValue());
		String tableName="_0";
		if(index>0)
		 tableName="_"+subscriberNumber.substring(subscriberNumber.length()-index);
		
		return tablePrefix+tableName;
		
	}//getLoyaltyRegisteredNumberTable
	
	
	public String getLoyaltyTransactionDBTable(String loyaltyID)
	{
		String tablePrefix="LOYALTY_TRANSACTION";
		int index=Integer.parseInt(Cache.getConfigParameterMap().get("LOYALTY_TRAN_SUFFIX_LENGTH").getParameterValue());
		String tableName="_0";
		if(index>0)
		 tableName="_"+loyaltyID.substring(loyaltyID.length()-index);
		
		return tablePrefix+tableName;
		
	}//getLoyaltyRegisteredNumberTable
	
	public String getSubscriberProfileEntity(String subscriberNumber)
	{
		String tablePrefix="SUBSCRIBER_PROFILE_ENTITY";
		int index=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_PROFILE_SUFFIX_LENGTH").getParameterValue());
		String tableName="_0";
		if(index>0)
		 tableName="_"+subscriberNumber.substring(subscriberNumber.length()-index,subscriberNumber.length());
		
		return tablePrefix+tableName;
		
	}
	
	public String getSubscriberProfileDBTable(String subscriberNumber)
	{
		String tablePrefix="SUBSCRIBER_WISE_PROFILE";
		int index=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_PROFILE_SUFFIX_LENGTH").getParameterValue());
		String tableName="_0";
		if(index>0)
		 tableName="_"+subscriberNumber.substring(subscriberNumber.length()-index);
		
		return tablePrefix+tableName;
		
	}//getSubscriberProfileTable
	
	
	
	
	public String getSubscriberNumberTable(String subscriberNumber)
	{
		//String tablePrefix="SUBSCRIBER_NUMBER";
		String tablePrefix="SUBSCRIBER_NUMNER_ENTITY";
		int index=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NO_SUFFIX_LENGTH").getParameterValue());
		String tableName="_0";
		if(index>0)
		  tableName="_"+subscriberNumber.substring(subscriberNumber.length()-index);
		
		
		return tablePrefix+tableName;
		
	}//getSubscriberNumberTable
	public String getAccountNumberTable(String accountNumber)
	{
		String tablePrefix="ACCOUNT_NUMBER_ENTITY";
		int index=Integer.parseInt(Cache.getConfigParameterMap().get("ACCOUNT_NO_SUFFIX_LENGTH").getParameterValue());
		String tableName="_0";
		if(index>0)
		 tableName="_"+accountNumber.substring(accountNumber.length()-index);
		
		return tablePrefix+tableName;
		
	}//getAccountNumberTable
	
	   public String getUserIdentifierTable(String loyaltyID)
	    {
	        String tablePrefix="USER_IDENTIFIER_ENTITY";
	        int index=Integer.parseInt(Cache.getConfigParameterMap().get("USER_IDENTIFIER_SUFFIX_LENGTH").getParameterValue());
	        String tableName="_0";
	        if(index>0)
	          tableName="_"+loyaltyID.substring(loyaltyID.length()-index);	        	        
	        return tablePrefix+tableName;
	        
	    }
	   
	   
	
	
	public String getNationalNumberTable(String nationnalNumber)
	{
		
		String tablePrefix="NATIONAL_NUMBER_ENTITY";
		int index=Integer.parseInt(Cache.getConfigParameterMap().get("NATIONAL_NO_SUFFIX_LENGTH").getParameterValue());
		String tableName="_0";
		if(index>0)
		{
		  int number=0;
		  if(Character.isDigit(nationnalNumber.toUpperCase().charAt(nationnalNumber.length()-index))){
			  number=Integer.parseInt(nationnalNumber.toUpperCase().charAt(nationnalNumber.length()-index)+"");
		  }else{
			  number=(int)nationnalNumber.toUpperCase().charAt(nationnalNumber.length()-index);
			  if(number>=65&&number<=90)
			    number=(number-65)/3;
			  else
			    number=9;
		  }
		  tableName="_"+number;
		}
				
		return tablePrefix+tableName;
		
	}//getNationalNumberTable
	
	
	public String getLoyaltyProfileTable(String loyaltyNumber)
	{
		String tablePrefix="LOYALTY_PROFILE_ENTITY";
		int index=Integer.parseInt(Cache.getConfigParameterMap().get("LOYALTY_NO_SUFFIX_LENGTH").getParameterValue());
		String tableName="_0";
		if(index>0)
		 tableName="_"+loyaltyNumber.substring(loyaltyNumber.length()-index);
		
		return tablePrefix+tableName;
		
	}//getLoyaltyProfileTable
	
	
	public String getLoyaltyRegisteredNumberTable(String loyaltyRegisteredNumber)
	{
		String tablePrefix="LOYALTY_REG_NUMBER_ENTITY";
		int index=Integer.parseInt(Cache.getConfigParameterMap().get("LOYALTY_NO_SUFFIX_LENGTH").getParameterValue());
		String tableName="_0";
		if(index>0)
		 tableName="_"+loyaltyRegisteredNumber.substring(loyaltyRegisteredNumber.length()-index);
		
		return tablePrefix+tableName;
		
	}//getLoyaltyRegisteredNumberTable
	
	public String getADSLNumberTable(String ADSLNumberTable)
	{
		String tablePrefix="ADSL_NUMBER_ENTITY";
		int index=Integer.parseInt(Cache.getConfigParameterMap().get("ADSL_NO_SUFFIX_LENGTH").getParameterValue());
		String tableName="_0";
		if(index>0)
		  tableName="_"+ADSLNumberTable.substring(ADSLNumberTable.length()-index);
		
		return tablePrefix+tableName;
		
	}//getADSLNumberTable

	
	public String getSubscriberTransactionHistoryTable(String subscriberNumber)
	{
		String tablePrefix="SUB_TRA_HIS";
		int index=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NO_TAB_INDEX").getParameterValue());
		String tableName="_0";
		if(index>0)
		 tableName="_"+subscriberNumber.substring(subscriberNumber.length()-index);
		
		return tablePrefix+tableName;
		
	}//getLoyaltyRegisteredNumberTable
	
	
	public String getLoyaltyTransactionTable(String loyaltyID)
	{
		String tablePrefix="LOYALTY_TRANSACTION_ENTITY";
		//logger.info(Cache.getConfigParameterMap().get("LOYALTY_TRAN_SUFFIX_LENGTH"));
		int index=Integer.parseInt(Cache.getConfigParameterMap().get("LOYALTY_TRAN_SUFFIX_LENGTH").getParameterValue());
		String tableName="_0";
		if(index>0)
		 tableName="_"+loyaltyID.substring(loyaltyID.length()-index);
		//logger.info(tablePrefix+tableName);
		return tablePrefix+tableName;
		
	}//getLoyaltyRegisteredNumberTable
	
	
	public String getLoyaltySummaryTransactionTable()
	{
		String tablePrefix="LOYALTY_TRAN_SUMMARISED";
		//int index=Integer.parseInt(Cache.getConfigParameterMap().get("LOYALTY_TRAN_SUFFIX_LENGTH").getParameterValue());
		String tableName="_20";
		/*if(index>0)
		 tableName="_"+loyaltyID.substring(loyaltyID.length()-index);*/
		
		return tablePrefix+tableName;
		
	}//getLoyaltyRegisteredNumberTable
	
	
	
	public String getSubscriberProfileTable(String subscriberNumber)
	{
		String tablePrefix="SUBSCRIBER_PROFILE_ENTITY";
		int index=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_PROFILE_SUFFIX_LENGTH").getParameterValue());
		String tableName="_0";
		if(index>0)
		 tableName="_"+subscriberNumber.substring(subscriberNumber.length()-index);
		
		return tablePrefix+tableName;
		
	}//getSubscriberProfileTable
	
	public String getVoucherOrderDBTable(String voucherTable)
	{
		String tablePrefix="VOUCHER_ORDER_0";
	//	int index=Integer.parseInt(Cache.getConfigParameterMap().get("VOUCHER_ORDER_NO_SUFFIX_LENGTH").getParameterValue());
	//	String tableName="_0";
	//	if(index>0)
	//	 tableName="_"+voucherTable.substring(voucherTable.length()-index);
		
		return tablePrefix;
		
	}//getSubscriberProfileTable
	public String getVoucherOrderTable(String voucherTable)
	{
		String tablePrefix="VOUCHER_ORDER_0";
		//	int index=Integer.parseInt(Cache.getConfigParameterMap().get("VOUCHER_ORDER_NO_SUFFIX_LENGTH").getParameterValue());
		//	String tableName="_0";
		//	if(index>0)
		//	 tableName="_"+voucherTable.substring(voucherTable.length()-index);
		
		return tablePrefix;
		
	}//getVoucherOrderTable

	public String getInstantTableEntity(String subscriberNumber) {
		Logger log = Logger.getLogger(TableInfoDAO.class);
		String tablePrefix="INSTANT_REWARD_ENTITY";
		int index=Integer.parseInt(Cache.getConfigParameterMap().get("INSTANT_SUBSCRIBER_PROFILE_SUFFIX_LENGTH").getParameterValue());
		
		String tableName="_0";
		log.debug("--"+index+"--"+subscriberNumber+" -- "+subscriberNumber.substring(subscriberNumber.length()-index));
		if(index>0)
		 tableName="_"+subscriberNumber.substring(subscriberNumber.length()-index);
		
		return tablePrefix+tableName;
		
	
	}
	
	public String getInstantTableName(String subscriberNumber) {

		String tablePrefix="REWARD_CUSTOMER";
		int index=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_PROFILE_SUFFIX_LENGTH").getParameterValue());
		String tableName="_0";
		if(index>0)
		 tableName="_"+subscriberNumber.substring(subscriberNumber.length()-index);
		
		return tablePrefix+tableName;
		
	
	}
	
	
	public String getSubscriberPromoTable(String subscriberNumber)
	{
		String tablePrefix="SUBSCRIBER_PROMO_CHECK";
		int index=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_PROMO_SUFFIX_LENGTH").getParameterValue());
		String tableName="_0";
		if(index>0)
		  tableName="_"+subscriberNumber.substring(subscriberNumber.length()-index);
		
		
		return tablePrefix+tableName;
		
	}//getSubscriberVoucherTable
	
	public String getLanguagePreTableName(String number)
	{
		String tablePrefix="PREPAID_PROFILE_CDR";
		int index=Integer.parseInt(Cache.getConfigParameterMap().get("LANGUAGE_PRE_TABLE_SUFFIX_LENGTH").getParameterValue());
		String tableName="_0";
		if(index>0)
		 tableName="_"+number.substring(number.length()-index);
		
		return tablePrefix+tableName;
		
	}//getLanguagePreTableName
	
	public String getSubscriberUsageDBTable(String number)
	{
		String tablePrefix="SUBSCRIBER_USAGE";
		int index=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_USAGE_SUFFIX_LENGTH").getParameterValue());
		String tableName="_0";
		if(index>0)
		 tableName="_"+number.substring(number.length()-index);
		
		return tablePrefix+tableName;
		
	}//getLanguagePreTableName
	
	public String getLanguagePostTableName(String number)
	{
		String tablePrefix="PROFILE_SUBSCRIBER_LANGUAGE";
/*		int index=Integer.parseInt(Cache.getConfigParameterMap().get("LANGUAGE_POST_TABLE_SUFFIX_LENGTH").getParameterValue());
		String tableName="_0";
		if(index>0)
			tableName="_"+number.substring(number.length()-index);
*/		
		return tablePrefix;//+tableName;
		
	}//getLanguagePreTableName
	
	
	public String getRuleTableName(String number)
	{
		String tablePrefix="RULE_CDR_"+number;
		return tablePrefix;
		
	}//getRuleTableName
	
	public String getPrepaidProfileName(String number)
	{
		String tablePrefix="PREPAID_PROFILE_"+number.substring(number.length()-1);
		return tablePrefix;
		
	}//getPrepaidProfileName
	
	public String getPrepaidProfileTableName(String number)
	{
		String tablePrefix="PREPAID_PROFILE_CDR_"+number.substring(number.length()-1);
		return tablePrefix;		
	}//getPrepaidProfileTableName
	
	
	public LoyaltyProfileTabDTO getLoyaltyProfileDetails(String requestId,String key,int keyType){
		LoyaltyProfileTabDTO loyaltyProfileTabDTO = null;
		
		return loyaltyProfileTabDTO;
	}
	
	public String getLoyaltyTransactionTableName(String loyaltyID)
	{
		String tablePrefix="LOYALTY_TRANSACTION";
		//logger.info(Cache.getConfigParameterMap().get("LOYALTY_TRAN_SUFFIX_LENGTH"));
		int index=Integer.parseInt(Cache.getConfigParameterMap().get("LOYALTY_TRAN_SUFFIX_LENGTH").getParameterValue());
		String tableName="_0";
		if(index>0)
		 tableName="_"+loyaltyID.substring(loyaltyID.length()-index);
		//logger.info(tablePrefix+tableName);
		return tablePrefix+tableName;
		
	}//getLoyaltyRegisteredNumberTable
}//TableInfoDAO
