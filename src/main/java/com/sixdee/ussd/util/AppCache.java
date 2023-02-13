/**
 * 
 */
package com.sixdee.ussd.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Timer;

import org.apache.log4j.Logger;

import com.sixdee.ussd.dao.ConfigParameterDAO;
import com.sixdee.ussd.dao.KeywordMappingDAO;
import com.sixdee.ussd.dao.MessageTemplateDAO;
import com.sixdee.ussd.dao.ReadPropertyFiles;
import com.sixdee.ussd.dao.ServiceMappingDAO;
import com.sixdee.ussd.dao.TierLanguageDetailsDAO;
import com.sixdee.ussd.dto.KeywordMappingDTO;
import com.sixdee.ussd.dto.MessageTemplateDTO;
import com.sixdee.ussd.dto.PackageTree;
import com.sixdee.ussd.dto.ServiceRequestDTO;
import com.sixdee.ussd.dto.TierLanguageMappingDTO;
import com.sixdee.ussd.manager.ThreadInitiator;
import com.sixdee.ussd.util.parser.INXmlParser;

/**
 * @author Rahul K K
 * @version 1.0.0
 * 
 *          <p>
 *          <b><u>Development History</u></b><br>
 *          <table border="1" width="100%">
 *          <tr>
 *          <td width="15%">Date</td>
 *          <td width="20%">Author</td>
 *          <td>Description</td>
 *          </tr>
 *          <tr>
 *          <td>April 24, 2013</td>
 *          <td>Rahul K K</td>
 *          <td>Created this class</td>
 *          </tr>
 *          </table>
 *          </p>
 */
public class AppCache {

	private static final Logger logger = Logger.getLogger(AppCache.class);
	public static HashMap<String, Serializable> util = new HashMap<String, Serializable>();
	public static int langId = 1;
	private static String ussdManagerKey = "USSD_MANAGER";
	private static String childRelationKey = "CHILD_RELATION";
	private static String serviceKey = "SERVICE_CONFIG";
	private static String keywordsKey = "KEYWORDS";
	private static Timer timer;
	public static String appName   	  = null;	
	private static String packageTree = "PACKAGE_TREE_1";
	private static String packageTreeArab = "PACKAGE_TREE_11";
	private static String serviceMapping = "SERVICE_MAPPING";
	private static String messageTemplates = "MESSAGE_TEMPLATE";
	private static String configParamKey = "CONFIG_DETAILS";
	private static String tierLanguage = "TIER_LANGUAGE";
	private static HashMap<String,TierLanguageMappingDTO> tierLangDetails = null;
	private static HashMap<String, ServiceRequestDTO> serviceMap = null;
	private static HashMap<String, String> serviceNameMap = null;
	
	private static HashMap<Integer, ArrayList<String>> childMap = null;
	private static HashMap<Integer, ArrayList<String>> _childMap = null;
	private static HashMap<String, String> _serviceNameMap = null;

	private static HashMap<String, KeywordMappingDTO> keywordMap = null;
	private static HashMap<String, ServiceRequestDTO> _serviceMap = null;
	private static HashMap<String, KeywordMappingDTO> _keywordMap = null;
	private static HashMap<String, MessageTemplateDTO> messageTemplateMap = null;
	private static HashMap<String, MessageTemplateDTO> _messageTemplateMap = null;
	
	private static HashMap<String, String> ussdManagerMap = null;
	private static HashMap<String, String> _ussdManagerMap = null;
	private static HashMap<Integer, LinkedList<PackageTree>> packageMap = null;
	private static HashMap<Integer, LinkedList<PackageTree>> _packageMap = null;
	private static HashMap<Integer, LinkedList<PackageTree>> packageMapAra = null;
	private static HashMap<Integer, LinkedList<PackageTree>> _packageMapAra = null;
	private static HashMap<String,TierLanguageMappingDTO> _tierLangDetails = null;	
	
	private static HashMap<String, String>    configParamMap = null;
	private static HashMap<String, String>    _configParamMap = null;
	private static Hashtable<String, Object>		userObjectSrv;
	private static Hashtable<String, Object>		userObjectCli;
	private static Hashtable<String, Object>		userObjectMgmt;

	
	public static void initCache() {

		ussdManagerMap = ReadPropertyFiles.getInstance().readProperties();
		serviceMap = new ServiceMappingDAO().getServices();
		logger.debug(serviceMap);
		// keywordMap = new KeywordMappingDAO().getKeyWords();
		ArrayList<Object> objList = (ArrayList<Object>) new KeywordMappingDAO()
				.getKeyWords();

		keywordMap = (HashMap<String, KeywordMappingDTO>) objList.get(0);
		logger.debug(keywordMap);
		childMap = (HashMap<Integer, ArrayList<String>>) objList.get(1);
		logger.debug(childMap);
		messageTemplateMap = new MessageTemplateDAO().getMessageTemplates();
		logger.debug(messageTemplateMap);
		configParamMap = new ConfigParameterDAO().getConfigDetails();
		tierLangDetails = new TierLanguageDetailsDAO().getTierLanguageDetails();	
		util.put(configParamKey, configParamMap);
		// childMap = new KeywordMappingDAO().getChildRelation(keywordMap);

		util.put(tierLanguage, tierLangDetails);
		util.put(serviceKey, serviceMap);
		

		util.put(keywordsKey, keywordMap);
		util.put(childRelationKey, childMap);
		util.put(ussdManagerKey, ussdManagerMap);
		util.put(messageTemplates, messageTemplateMap);
		ThreadInitiator.getInstance().initiateThreads();
		INXmlParser.getXstreamInstance();

	    userObjectSrv = new Hashtable<String, Object>();
	    userObjectCli = new Hashtable<String, Object>();
	    userObjectMgmt = new Hashtable<String, Object>();
		initateReloadCache();
	}
	
	
	public synchronized static void killTimerThreads() {
		try{
			timer.cancel();
		}catch (Exception e) {
			// TODO: handle exception
			logger.warn("Problem with killing timer "+e);
		}
	}

	private static void initateReloadCache() {

		ReloadCache reloadCache = new ReloadCache();
		timer = new Timer();

		try {
			long cacheReloadInterval = 100000L;// Long.parseLong((String)
												// ussdManagerMap.get("CacheReloadInterval"));
			timer.scheduleAtFixedRate(reloadCache,
					(cacheReloadInterval * 1000), (cacheReloadInterval * 1000));

		} finally {
			reloadCache = null;
		}

	}

	public static void reinitializeCache(){
		reloadCache();
		timer.cancel();
		// purge method for removing cancelled timer refferences from Timer
		// Queue
		timer.purge();

		timer = null;
		initateReloadCache();

	}
	public static void reloadCache() {
		
		_ussdManagerMap = ReadPropertyFiles.getInstance().readProperties();
		_serviceMap = new ServiceMappingDAO().getServices();
		ArrayList<Object> objList = (ArrayList<Object>) new KeywordMappingDAO()
				.getKeyWords();

		_keywordMap = (HashMap<String, KeywordMappingDTO>) objList.get(0);
		_childMap = (HashMap<Integer, ArrayList<String>>) objList.get(1);

		_messageTemplateMap = new MessageTemplateDAO().getMessageTemplates();
		_configParamMap = new ConfigParameterDAO().getConfigDetails();
	
		if(_configParamMap.size()!=0){
			configParamMap = _configParamMap;
		}
		if (_serviceMap.size() != 0) {
			serviceMap = _serviceMap;
		}
		if (_keywordMap.size() != 0) {
			keywordMap = _keywordMap;
		}
		if (_ussdManagerMap.size() != 0) {
			ussdManagerMap = _ussdManagerMap;
		}
		if (_childMap.size() != 0) {
			childMap = _childMap;
		}
		if(_messageTemplateMap.size() != 0){
			messageTemplateMap = _messageTemplateMap;
		}
		util.put(serviceKey, serviceMap);
		

		util.put(keywordsKey, keywordMap);
		util.put(childRelationKey, childMap);
		util.put(ussdManagerKey, ussdManagerMap);
		util.put(messageTemplates, messageTemplateMap);
	
	}
	
	
	public void reInitPackageTree(){
		AppCache.util.put("PACKAGE_TREE_11", null);
		AppCache.util.put("PACKAGE_TREE_1", null);
		
	}

	public static Hashtable<String, Object> getUserObjectSrv() {
		return userObjectSrv;
	}

	public static void setUserObjectSrv(Hashtable<String, Object> userObjectSrv) {
		AppCache.userObjectSrv = userObjectSrv;
	}

	public static Hashtable<String, Object> getUserObjectCli() {
		return userObjectCli;
	}

	public static void setUserObjectCli(Hashtable<String, Object> userObjectCli) {
		AppCache.userObjectCli = userObjectCli;
	}

	public static Hashtable<String, Object> getUserObjectMgmt() {
		return userObjectMgmt;
	}

	public static void setUserObjectMgmt(Hashtable<String, Object> userObjectMgmt) {
		AppCache.userObjectMgmt = userObjectMgmt;
	}

}
