/**
 * SixDEE Telecom Solutions Pvt. Ltd.
 * Copyright 2006
 * All Rights Reserved.
 */

package com.sixdee.imp.threadpool;

import java.util.Calendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* @author Paramesh
 * @version 1.0.0.0
 * @since May 06, 2013
 * 
 * Development History
 * 
 */

import com.sixdee.imp.common.config.Cache;

public class ThreadInitiator {
	
	

	private static boolean startFlag = true;
	private static ThreadInitiator objThreadInitiator = null;
	public static RequestProcessThreadPool requestProcessThreadPool = null;
	public 	static RollbackThreadPool rollbackThreadPool	= null;
	/*
	 * public static InstantRewardPool instantRewardPool = null; public static
	 * ReCallerPool reCallerPool = null; public static ProvisionCallerPool
	 * provCallPool = null; public static ProvisionRespPool provRespPool = null;
	 * public static LoyaltyExpiryPool loyaltyExpiryPool = null; public static
	 * RewardExpiryPool rewardExpiryPool = null; public static TestThreadPool
	 * testThreadPool = null; // sajith ks added for simulator public static
	 * PromotionThreadPool promotionThreadPool = null; // sajith ks added for
	 * simulator promotion
	 */	/**
	 * REQUEST PROCESS THREADS
	 * 
	 */

	private int poolCount = 1;
	private int instantPoolCount = 1;
	private int thirdPartyCount = 1;
	private int						rollbackThreadPoolcount			= 1;

	/**
	 * @return the startFlag
	 */
	public static boolean isStartFlag() {
		return startFlag;
	}

	/**
	 * @param startFlag the startFlag to set
	 */
	public static void setStartFlag(boolean startFlag) {
		ThreadInitiator.startFlag = startFlag;
	}

	private ThreadInitiator() {
		//
	}

	public void initiateThreads() {
		

		if (requestProcessThreadPool == null) {

			/**
			 * Value taken from cache to get the Thread Pool Size and initialize the Thread
			 * Pool.
			 */
			if (Cache.getCacheMap().get("REQUEST_PROCESS_THREAD_POOL_SIZE") != null)
				poolCount = Integer.parseInt(Cache.getCacheMap().get("REQUEST_PROCESS_THREAD_POOL_SIZE"));

			requestProcessThreadPool = new RequestProcessThreadPool(poolCount);
		}
		if (Cache.getCacheMap().get("ROLLBACK_PROCESS_THREAD_POOL_SIZE") != null) {
			rollbackThreadPoolcount = Integer.parseInt(Cache.getCacheMap().get("ROLLBACK_PROCESS_THREAD_POOL_SIZE"));
		}

		rollbackThreadPool = new RollbackThreadPool(rollbackThreadPoolcount);

		/*
		 * if (Cache.getCacheMap().get("INSTANT_REWARD_THREAD_POOL_SIZE") != null) {
		 * instantPoolCount =
		 * Integer.parseInt(Cache.getCacheMap().get("INSTANT_REWARD_THREAD_POOL_SIZE"));
		 * }
		 * 
		 * instantRewardPool = new InstantRewardPool(instantPoolCount); if
		 * (Cache.getCacheMap().get("ThirdPartyCaller_THREAD_POOL_SIZE") != null) {
		 * thirdPartyCount =
		 * Integer.parseInt(Cache.getCacheMap().get("ThirdPartyCaller_THREAD_POOL_SIZE")
		 * ); } reCallerPool = new ReCallerPool(thirdPartyCount); thirdPartyCount = 1;
		 * if (Cache.getCacheMap().get("ProvisionCaller_THREAD_POOL_SIZE") != null) {
		 * thirdPartyCount =
		 * Integer.parseInt(Cache.getCacheMap().get("ProvisionCaller_THREAD_POOL_SIZE"))
		 * ; } provCallPool = new ProvisionCallerPool(thirdPartyCount); thirdPartyCount
		 * = 1; if (Cache.getCacheMap().get("ProvisionResp_THREAD_POOL_SIZE") != null) {
		 * thirdPartyCount =
		 * Integer.parseInt(Cache.getCacheMap().get("ProvisionResp_THREAD_POOL_SIZE"));
		 * } provRespPool = new ProvisionRespPool(thirdPartyCount);
		 * 
		 * poolCount = 0; if(Cache.getCacheMap().get("LOYALTY_EXPIRY_POOL")!=null) {
		 * poolCount =
		 * Integer.parseInt(Cache.getCacheMap().get("LOYALTY_EXPIRY_POOL").toString());
		 * } if(poolCount>0) loyaltyExpiryPool = new LoyaltyExpiryPool(poolCount);
		 * if(Cache.getCacheMap().get("REWARD_EXPIRY_POOL")!=null) { poolCount =
		 * Integer.parseInt(Cache.getCacheMap().get("REWARD_EXPIRY_POOL").toString()); }
		 * if(poolCount>0) rewardExpiryPool = new RewardExpiryPool(poolCount);
		 * 
		 * // sajith ks start if(Cache.getCacheMap().get("SIMULATOR_THREAD_POOL")!=null)
		 * { poolCount =
		 * Integer.parseInt(Cache.getCacheMap().get("SIMULATOR_THREAD_POOL").toString())
		 * ; } if(poolCount>0) testThreadPool = new TestThreadPool(10); //sajith ks
		 * added for simulator promotionThreadPool = new PromotionThreadPool(10);
		 * //sajith ks added for simulator promotion // sajith ks end
		 */ }

	public static ThreadInitiator getInstance() {

		if (objThreadInitiator == null) {
			objThreadInitiator = new ThreadInitiator();

		}

		return objThreadInitiator;
	}

}
