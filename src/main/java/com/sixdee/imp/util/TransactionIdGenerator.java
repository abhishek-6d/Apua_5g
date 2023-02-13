package com.sixdee.imp.util;

public class TransactionIdGenerator {

	
	public static Long transactionID=1001L;
	
	private static long currentDate=0;
	
	public  String genrateTransactionID() {
		long date=GetReqdDateFormat.getDateTimeInLong("yyyyMMddHHmmss");
		if(currentDate!=date){
			currentDate=date;
		}
		
		String finalTxnId;
		synchronized (transactionID) {
			transactionID++;
		if(transactionID == 10000)
			transactionID = 1001L;
		finalTxnId = currentDate+""+transactionID;
		}
	
		return (finalTxnId);
	}
	
}
