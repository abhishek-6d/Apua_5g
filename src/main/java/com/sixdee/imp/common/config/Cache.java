
package com.sixdee.imp.common.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sixdee.imp.common.dto.INServiceDetailsDTO;
import com.sixdee.imp.common.dto.SecurityTokenDto;
import com.sixdee.imp.dto.AccountTypeWisePackDTO;
import com.sixdee.imp.dto.ActionServiceDetailsDTO;
import com.sixdee.imp.dto.CategoryDetailsDTO;
import com.sixdee.imp.dto.ConfigureParameterDTO;
import com.sixdee.imp.dto.EmailInfoDTO;
import com.sixdee.imp.dto.InstantRewardConfigDTO;
import com.sixdee.imp.dto.LanguageDTO;
import com.sixdee.imp.dto.RewardPointsCategoryDTO;
import com.sixdee.imp.dto.ServiceMappingDTO;
import com.sixdee.imp.dto.ServiceStatusDTO;
import com.sixdee.imp.dto.TierInfoDTO;
import com.sixdee.lms.dto.OnlineTriggerTableDTO;
import com.thoughtworks.xstream.XStream;

public class Cache 
{
	public static Map<String,String> cacheMap;
	public static Map<String,String> accountConfigMap ;
	public static Map<String,String> statusMap;
	
	public static Map<String,String> loyaltyStatusMap;
	public static Map<String,String> voucherStatusMap;
	public static HashMap<Integer, ServiceMappingDTO> appMap ;
	public static XStream moRouterXstream;
	public static XStream subscriberProfileXstream;
	public static XStream coomonReqXstream;
	public static XStream coomonResXstream;
	public static XStream TOLXstream;
	public static XStream GetlanguageXstream;
	public static Map<Integer,TierInfoDTO> tierInfoMap;
	public static Map<String,String> tierLanguageInfoMap;
	public static Map<String,ConfigureParameterDTO> configParameterMap;
	public static Map<String,ActionServiceDetailsDTO> serviceDetailsMap;
	public static Map<Integer,RewardPointsCategoryDTO> rewardPointsCategoryMap;
	public static Map<Integer,Map<Integer,List<CategoryDetailsDTO>>> tierCategoryDetailsMap;
	public static ArrayList<Integer> parentMap ;
	public static Map<String,String> accountTypeMap;
	public static Map<Integer,LanguageDTO> languageDetailsMap;
	public static boolean isMySqlDB;
	public static boolean isOracleDB;
	
	private static String SMTP_AUTH_USER  = null;
	private static String SMTP_AUTH_PWD  = null;
	private static String SMTP_HOST_NAME  = null;
	private static String SMTP_PORT  = null;
    private static String SSL_FACTORY  = null;
    private static String SMS  = null;
    private static String EMAIL  = null;
    public static String defaultLanguageID;
    private static Map<String,ServiceStatusDTO> serviceStatusMap;
	public static Map<String,InstantRewardConfigDTO> instantPacketsConfig;
	public static Map<String,String> instantOfferMapping;
	
	public static Map<Integer,INServiceDetailsDTO>  inServiceDetailsMap;
	public static Map<String,String> channelDetails;
	public static Map<String,String> channelFlowDetails;
	public static Map<String,String> channelDescDetails;
	public static Map<String,String> offerCategoryMapInfo;
	public static Map<String,String> TelecomofferCategoryMapInfo;
	public static Map<String,String> TelecomofferTypeIdMapInfo;
	public static Map<Integer, EmailInfoDTO> emails ;
    public static Map<Integer,AccountTypeWisePackDTO> loyaltyRegPackMap ;
    
    public static List<String> accountCategoryList=null;
    public static List<String> accountCategoryServiceList = null;
	public static int instantOfferCount;
	 public static String voucherPromoRetryCount=null;
	 public static boolean voucherLimit=false;
	 public static String voucherUserLimit=null;
	 public static String voucherAllLimit=null;
	 public static Map<String,String> featureDeetailsMap;
	 public static Map<Integer,String> interfaceDetailsMap;
	public static Map<Integer, ArrayList<OnlineTriggerTableDTO>> triggerTableMap = null; 
	public static Map<String,String> accountCategoryType;
	public static Map<String, SecurityTokenDto> securityTokenDetailsMap = null;
	
	
	
	


	public static Map<Integer, String> getInterfaceDetailsMap() {
		return interfaceDetailsMap;
	}

	public static void setInterfaceDetailsMap(
			Map<Integer, String> interfaceDetailsMap) {
		Cache.interfaceDetailsMap = interfaceDetailsMap;
	}

	public static Map<String, String> getFeatureDeetailsMap() {
		return featureDeetailsMap;
	}

	public static void setFeatureDeetailsMap(Map<String, String> featureDeetailsMap) {
		Cache.featureDeetailsMap = featureDeetailsMap;
	}

	public static String getSMS() {
		return SMS;
	}

	public static void setSMS(String sMS) {
		SMS = sMS;
	}

	public static String getEMAIL() {
		return EMAIL;
	}

	public static void setEMAIL(String eMAIL) {
		EMAIL = eMAIL;
	}

	
	/**
	 * @return the tierLanguageInfoMap
	 */
	public static Map<String, String> getTierLanguageInfoMap() {
		return tierLanguageInfoMap;
	}

	/**
	 * @param tierLanguageInfoMap the tierLanguageInfoMap to set
	 */
	public static void setTierLanguageInfoMap(
			Map<String, String> tierLanguageInfoMap) {
		Cache.tierLanguageInfoMap = tierLanguageInfoMap;
	}

	public static Map<String, ServiceStatusDTO> getServiceStatusMap() {
		return serviceStatusMap;
	}

	public static void setServiceStatusMap(
			Map<String, ServiceStatusDTO> serviceStatusMap) {
		Cache.serviceStatusMap = serviceStatusMap;
	}

	public static String getSSL_FACTORY() {
		return SSL_FACTORY;
	}

	public static void setSSL_FACTORY(String sSLFACTORY) {
		SSL_FACTORY = sSLFACTORY;
	}

	public static String getSMTP_AUTH_USER() {
		return SMTP_AUTH_USER;
	}

	public static void setSMTP_AUTH_USER(String sMTPAUTHUSER) {
		SMTP_AUTH_USER = sMTPAUTHUSER;
	}

	public static String getSMTP_AUTH_PWD() {
		return SMTP_AUTH_PWD;
	}

	public static void setSMTP_AUTH_PWD(String sMTPAUTHPWD) {
		SMTP_AUTH_PWD = sMTPAUTHPWD;
	}

	public static String getSMTP_HOST_NAME() {
		return SMTP_HOST_NAME;
	}

	public static void setSMTP_HOST_NAME(String sMTPHOSTNAME) {
		SMTP_HOST_NAME = sMTPHOSTNAME;
	}

	public static String getSMTP_PORT() {
		return SMTP_PORT;
	}

	public static void setSMTP_PORT(String sMTPPORT) {
		SMTP_PORT = sMTPPORT;
	}

	/**
	 * @return the configParameterMap
	 */
	public static Map<String, ConfigureParameterDTO> getConfigParameterMap() {
		return configParameterMap;
	}

	/**
	 * @param configParameterMap the configParameterMap to set
	 */
	public static void setConfigParameterMap(Map<String, ConfigureParameterDTO> configParameterMap) {
		Map<String, ConfigureParameterDTO> m=Cache.configParameterMap;
		Cache.configParameterMap = configParameterMap;
		if (m != null)
			m.clear();
		m=null;
	}

	/**
	 * @return the statusMap
	 */
	public static Map<String, String> getStatusMap() {
		return statusMap;
	}

	/**
	 * @param statusMap the statusMap to set
	 */
	public static void setStatusMap(Map<String, String> statusMap) {
		Cache.statusMap = statusMap;
	}

	public static Map<String, String> getCacheMap() {
		return cacheMap;
	}

	public static void setCacheMap(Map<String, String> cacheMap) {
		Cache.cacheMap = cacheMap;
		defaultLanguageID=cacheMap.get("DEFAULT_LANGUAGEID");
	}

	public static Map<Integer, TierInfoDTO> getTierInfoMap() {
		return tierInfoMap;
	}

	public static void setTierInfoMap(Map<Integer, TierInfoDTO> tierInfoMap) {
		Cache.tierInfoMap = tierInfoMap;
	}

	public static Map<String, String> getInstantOfferMapping() {
		return instantOfferMapping;
	}

	public static void setInstantOfferMapping(
			Map<String, String> instantOfferMapping) {
		Cache.instantOfferMapping = instantOfferMapping;
	}

	public static String getVoucherPromoRetryCount() {
		return voucherPromoRetryCount;
	}

	public static void setVoucherPromoRetryCount(String voucherPromoRetryCount) {
		Cache.voucherPromoRetryCount = voucherPromoRetryCount;
	}

	public static boolean isVoucherLimit() {
		return voucherLimit;
	}

	public static void setVoucherLimit(boolean voucherLimit) {
		Cache.voucherLimit = voucherLimit;
	}

	public static String getVoucherUserLimit() {
		return voucherUserLimit;
	}

	public static void setVoucherUserLimit(String voucherUserLimit) {
		Cache.voucherUserLimit = voucherUserLimit;
	}

	public static String getVoucherAllLimit() {
		return voucherAllLimit;
	}

	public static void setVoucherAllLimit(String voucherAllLimit) {
		Cache.voucherAllLimit = voucherAllLimit;
	}
	/**
	 * @return the triggerTableMap
	 */
	public static Map<Integer, ArrayList<OnlineTriggerTableDTO>> getTriggerTableMap() {
		return triggerTableMap;
	}

	/**
	 * @param triggerTableMap the triggerTableMap to set
	 */
	public static void setTriggerTableMap(Map<Integer, ArrayList<OnlineTriggerTableDTO>> triggerTableMap) {
		Cache.triggerTableMap = triggerTableMap;
	}

	public static Map<String, SecurityTokenDto> getSecurityTokenDetailsMap() {
		return securityTokenDetailsMap;
	}

	public static void setSecurityTokenDetailsMap(Map<String, SecurityTokenDto> securityTokenDetailsMap) {
		Cache.securityTokenDetailsMap = securityTokenDetailsMap;
	}
	
}
