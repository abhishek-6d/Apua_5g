package com.sixdee.imp.service.serviceDTO.resp;

public class EligibleSubscriberInfoDTO extends ResponseDTO
{
	private EligibleSubscriberDetails[] eligibleSubscriberDetails;

	public EligibleSubscriberDetails[] getEligibleSubscriberDetails() {
		return eligibleSubscriberDetails;
	}

	public void setEligibleSubscriberDetails(
			EligibleSubscriberDetails[] eligibleSubscriberDetails) {
		this.eligibleSubscriberDetails = eligibleSubscriberDetails;
	}
	

	
}
