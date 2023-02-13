/**
 * 
 */
package com.sixdee.imp.service.serviceDTO.common;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%">Date</td>
 * <td width="20%">Author</td>
 * <td>Description</td>
 * </tr>
 * <tr>
 * <td>Jan 30, 2017</td>
 * <td>Rahul K K</td>
 * <td>Created this class</td>
 * </tr>
 * </table>
 * </p>
 */
@XmlType(name="PointAdjustmnetID")
@XmlEnum
public enum PointAdjustmentId {
	CHANGEOWNER_TRANSFER_FROM,CHANGEOWNER_TRANSFER_TO,ONDEMAND_TRANSFER_FROM,ONDEMAND_TRANSFER_TO,PRE2POST_TRANSFER_FROM,POST2PRE_TRANSFER_TO,OTHERS,BMC_HIGH,BMC_MED,
	BMC_LOW,MEMBER_GET_MEMBER,RETENTION_POINTS_LVC,RETENTION_POINTS_MVC,RETENTION_POINTS_HVC,CHANGEHierarchy_TRANSFER_FROM,CHANGEHierarchy_TRANSFER_TO,PURCHASE_LOYALTY_POINT,
	INCENTIVE_FOR_DIGITAL_CHANNEL,CAMPAIGN_MANAGEMENT_TOOL;
	
	 public String value() {
	        return name();
	    }

	    public static PointAdjustmentId fromValue(String v) {
	        return valueOf(v);
	    }
}
