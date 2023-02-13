package com.sixdee.imp.service.serviceDTO.resp;

import java.util.Arrays;

public class PackageInfoDTO extends ResponseDTO 
{
	
	private PackageDetailsDTO[] packages;

	public PackageDetailsDTO[] getPackages() {
		return packages;
	}

	public void setPackages(PackageDetailsDTO[] packages) {
		this.packages = packages;
	}

	@Override
	public String toString() {
		return "PackageInfoDTO [packages=" + Arrays.toString(packages) + "]";
	}


}
