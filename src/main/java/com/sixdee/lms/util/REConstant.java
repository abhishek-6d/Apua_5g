/**
 * 
 */
package com.sixdee.lms.util;

import com.sixdee.lms.util.constant.ESBRequestType;

/**
 * @author rahul.kr
 *
 */
public enum REConstant {


	GetAccountDetails("com.sixdee.lms.bo.GetAccountDetailsBO","GetAccountDetailsFromCSI",ESBRequestType.RetrieveAccountDetails),
	GetCustomerProfile("com.sixdee.lms.bo.GetAccountDetailsBO","GetCustomerDetailsFromCSI",ESBRequestType.RetrieveCustomerDetails),
	PointAccumulation("com.sixdee.imp.service.ReServices.BL.TierAndBonusPointCalculationBL","PointCalculation",ESBRequestType.Default),
	createAccount("com.sixdee.lms.bo.GenerateLoyaltyAccountBO","GenerateLoyaltyAccountBO",ESBRequestType.Default),
	QuerySubscriber("com.sixdee.lms.bo.QuerySubscriberBO","QuerySubscriber",ESBRequestType.Default),
	FetchProfile("com.sixdee.lms.bo.FetchProfileBO","FetchProfile",ESBRequestType.Default),
	notifyEIF("com.sixdee.lms.bo.NotifyEIFOnLoyaltyAccountBO","NotifyEIF",ESBRequestType.Default),
	PointExpiry("com.sixdee.lms.bo.PointExpiryBO","PointExpiry",ESBRequestType.Default);
	
	private String className,serviceName;
	private ESBRequestType esbRequestType ;
	REConstant(String className,String serviceName,ESBRequestType esbRequestType){
		
		this.className = className;
		this.serviceName = serviceName;
		this.esbRequestType = esbRequestType;
	}
	
	public String getServiceName(){
		return serviceName; 
	}
	
	public String getClassName(){
		return className; 
	}
	
	public ESBRequestType getEsbRequestType() {
		return esbRequestType;
	}
}
