/**
 * 
 */
package com.sixdee.imp.ver;



/**
 * @author Rahul K K
 * @version 1.0.0
 * 
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%">Date</td>
 * <td width="20%">Author</td>
 * <td>Description</td>
 * </tr>
 * <tr>
 * <td>April 24, 2013</td>
 * <td>Rahul K K</td>
 * <td>Created this class</td>
 * </tr>
 * </table>
 * </p>
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
