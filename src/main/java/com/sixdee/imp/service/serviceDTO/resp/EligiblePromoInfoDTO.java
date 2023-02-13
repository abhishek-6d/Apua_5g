package com.sixdee.imp.service.serviceDTO.resp;

public class EligiblePromoInfoDTO extends ResponseDTO
{
	private EligiblePromoDetailsDTO[] eligiblePromoDetailsDTO;

	public EligiblePromoDetailsDTO[] getEligiblePromoDetailsDTO() {
		return eligiblePromoDetailsDTO;
	}

	public void setEligiblePromoDetailsDTO(
			EligiblePromoDetailsDTO[] eligiblePromoDetailsDTO) {
		this.eligiblePromoDetailsDTO = eligiblePromoDetailsDTO;
	} 
}
