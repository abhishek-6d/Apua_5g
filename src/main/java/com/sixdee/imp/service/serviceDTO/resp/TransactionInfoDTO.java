package com.sixdee.imp.service.serviceDTO.resp;


public class TransactionInfoDTO extends ResponseDTO 
{

	private int offSet;
	private int limit;
	private int totalCount;
	private TransactionDetailsDTO[] transactionDetails;

	public TransactionDetailsDTO[] getTransactionDetails() {
		return transactionDetails;
	}

	public void setTransactionDetails(TransactionDetailsDTO[] transactionDetails) {
		this.transactionDetails = transactionDetails;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getOffSet() {
		return offSet;
	}

	public void setOffSet(int offSet) {
		this.offSet = offSet;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
}
