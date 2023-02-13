/**
 * SixDEE Telecom Solutions Pvt. Ltd.
 * Copyright 2008
 * All Rights Reserved.
 */

package com.sixdee.mgmnt.client.ver;

import com.sixdee.mgmnt.client.ApplicationSpecificCacheReld;
import com.sixdee.mgmnt.client.MgmntAddServiceJars;
import com.sixdee.mgmnt.client.MgmntDynLoadQueue;
import com.sixdee.mgmnt.client.MgmntFlushQueueClient;
import com.sixdee.mgmnt.client.MgmntForceShutDown;
import com.sixdee.mgmnt.client.MgmntFullCacheRld;
import com.sixdee.mgmnt.client.MgmntFullCacheRldWithDMN;
import com.sixdee.mgmnt.client.MgmntH2ConnPoolRld;
import com.sixdee.mgmnt.client.MgmntLogClient;

/**
 * @author Sumanth Kalyan
 * @version 1.0.0.0
 * @since Aug 11, 2008
 *
 * Development History
 * 
 * Date					Author				Description
 * ------------------------------------------------------------------------------------------
 * Aug 11, 2008			Sumanth Kalyan			
 * ------------------------------------------------------------------------------------------
 */
public class Version {

	public static void main(String[] args) {
		if (args.length == 0 || args.length<5) {
			new Version();
		}else {
			try {
				String []arg=new String[(args.length-1)];
				for(int i=1,k=0;i<args.length;i++,k++) {
					arg[k]=args[i];
				}
				if(args[0].equalsIgnoreCase("MgmntLogClient")) {
					MgmntLogClient.main(arg);
				}else if(args[0].equalsIgnoreCase("MgmntFlushQueueClient")) {
					MgmntFlushQueueClient.main(arg);
				}else if(args[0].equalsIgnoreCase("MgmntFullCacheRldWithDMN")) {
					MgmntFullCacheRldWithDMN.main(arg);
				}else if(args[0].equalsIgnoreCase("MgmntFullCacheRld")) {
					MgmntFullCacheRld.main(arg);
				}else if(args[0].equalsIgnoreCase("MgmntForceShutDown")) {
					MgmntForceShutDown.main(arg);
				}else if(args[0].equalsIgnoreCase("MgmntH2ConnPoolRld")) {
					MgmntH2ConnPoolRld.main(arg);
				}
				else if(args[0].equalsIgnoreCase("MgmntAddServiceJars"))
				{
					MgmntAddServiceJars.main(arg);
				}
				else if(args[0].equalsIgnoreCase("QueueLoad"))
				{
					MgmntDynLoadQueue.main(arg);
				}
				else if(args[0].equalsIgnoreCase("ApplnCacheRld"))
				{
					ApplicationSpecificCacheReld.main(arg);
				}
				else {
					new Version();
				}
			}catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}

	}

	Version() {
		Package p = this.getClass().getPackage();
		System.out.println("Implementation Title   : "+ p.getImplementationTitle());
		System.out.println("Implementation Version : "+ p.getImplementationVersion());
		System.out.println("Implementation Vendor  : "+ p.getImplementationVendor());
		System.out.println();
		System.out.println("Usage Info... ");
		System.out.println("USE 6677 Port for USSD/SMSGW & 6678 for Controller Port Or as Configured");
		
		System.out.println();
		System.err.println("Usage: java -jar SingMgmnt.jar MgmntLogClient IpAddress port LoginName LoginPassword LogLevel(FATAL/ERROR/WARN/INFO/DEBUG)... ");
		
		System.out.println();
		System.out.println("java -jar SingMgmnt.jar MgmntFlushQueueClient IpAddress port LoginName LoginPassword FlushQueue(BillingProcess/BillMsgExp/VouchProcess/VouchMsgExp)...");
		
		System.out.println();
		System.out.println("java -jar SingMgmnt.jar MgmntFullCacheRld IpAddress Port LoginName LoginPassword");
		
		System.out.println();
		System.out.println("java -jar SingMgmnt.jar MgmntFullCacheRldWithDMN IpAddress Port LoginName LoginPassword");
		
		System.out.println();
		System.err.println("Usage: java -jar SingMgmnt.jar MgmntH2ConnPoolRld IpAddress Port LoginName LoginPassword");
		
		System.out.println();
		System.err.println("Usage: java -jar SingMgmnt.jar MgmntForceShutDown IpAddress Port LoginName LoginPassword");
		
		System.out.println();
		System.err.println("Usage: java -jar SingMgmnt.jar MgmntAddServiceJars IpAddress Port LoginName LoginPassword Jarname (eg:- Subscription.jar");
		
		System.out.println();
		System.err.println("Usage: java -jar SingMgmnt.jar QueueLoad IpAddress Port LoginName LoginPassword QueueName (eg:-SPORTS_QUEUE");
		
		System.out.println();
		System.err.println("Usage: java -jar SingMgmnt.jar ApplnCacheRld IpAddress Port LoginName LoginPassword ApplnMgmntImpl");
		
	}

public void say(String s) {
		System.out.println(s);
	}

}
