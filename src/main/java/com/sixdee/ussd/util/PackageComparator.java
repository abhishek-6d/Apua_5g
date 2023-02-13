/**
 * 
 */
package com.sixdee.ussd.util;

import java.util.Comparator;

import com.sixdee.ussd.dto.PackageTree;

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
public class PackageComparator implements Comparator<PackageTree> {

	public int compare(PackageTree pack1, PackageTree pack2) {
		int retValue = 0;
		int aRedeem = 0;
		int bRedeem = 0;
		try{
			aRedeem = Integer.parseInt(pack1.getRedeemPoints());
			bRedeem = Integer.parseInt(pack2.getRedeemPoints());
			if(aRedeem>bRedeem){
				return 1;
			}else  if(aRedeem<bRedeem){
				return -1;
			}
		}catch (Exception e) {
		}
		return 0;
		
	}
	
	
}
