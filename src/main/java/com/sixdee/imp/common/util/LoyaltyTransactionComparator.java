package com.sixdee.imp.common.util;

import java.util.Comparator;

import com.sixdee.imp.dto.LoyaltyTransactionTabDTO;

public class LoyaltyTransactionComparator implements Comparator<LoyaltyTransactionTabDTO> 
{

	public int compare(LoyaltyTransactionTabDTO arg0, LoyaltyTransactionTabDTO arg1) {
		// TODO Auto-generated method stub
		return arg0.getCreateTime().compareTo(arg1.getCreateTime());
	}

}
