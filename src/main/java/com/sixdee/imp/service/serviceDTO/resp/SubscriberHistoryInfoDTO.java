package com.sixdee.imp.service.serviceDTO.resp;

public class SubscriberHistoryInfoDTO extends ResponseDTO 
{
	private SubscriberHistoryDTO[] subscriberHistory ;

	public SubscriberHistoryDTO[] getSubscriberHistory() {
		return subscriberHistory;
	}

	public void setSubscriberHistory(SubscriberHistoryDTO[] subscriberHistory) {
		this.subscriberHistory = subscriberHistory;
	}
}
