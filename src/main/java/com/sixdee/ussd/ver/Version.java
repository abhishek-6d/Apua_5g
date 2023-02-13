/**
 * SixDEE Telecom Solutions Pvt. Ltd.
 * Copyright 2008
 * All Rights Reserved.
 */

package com.sixdee.ussd.ver;


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
				
					new Version();
				
			}catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}

	}

	public Version() {
		Package p = this.getClass().getPackage();
		System.out.println("Implementation Title   : "+ p.getImplementationTitle());
		System.out.println("Implementation Version : "+ p.getImplementationVersion());
		System.out.println("Implementation Vendor  : "+ p.getImplementationVendor());
			
	}

public void say(String s) {
		System.out.println(s);
	}

}
