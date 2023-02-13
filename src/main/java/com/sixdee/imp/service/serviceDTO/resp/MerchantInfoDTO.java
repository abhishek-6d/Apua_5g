package com.sixdee.imp.service.serviceDTO.resp;


public class MerchantInfoDTO extends ResponseDTO 
{
	
	private MerchantPackageDetailsDTO[] packages;

	public MerchantPackageDetailsDTO[] getPackages() {
		return packages;
	}

	public void setPackages(MerchantPackageDetailsDTO[] packages) {
		this.packages = packages;
	}


}
