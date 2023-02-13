package com.sixdee.imp.service.serviceDTO.resp;

public class TransactionHistoryInfoDTO extends ResponseDTO
{
	public TransactionHistoryDetails[] transactionHistoryDetails;

	public TransactionHistoryDetails[] getTransactionHistoryDetails() {
		return transactionHistoryDetails;
	}

	public void setTransactionHistoryDetails(
			TransactionHistoryDetails[] transactionHistoryDetails) {
		this.transactionHistoryDetails = transactionHistoryDetails;
	}
}
