package com.sixdee.imp.dto;

import com.sixdee.imp.service.serviceDTO.resp.PackageDetailsDTO;

public class AreaLocationInfoDTO {
	
private String area;
private String [] location;

public String[] getLocation() {
	return location;
}

public void setLocation(String[] location) {
	this.location = location;
}

public String getArea() {
	return area;
}

public void setArea(String area) {
	this.area = area;
}

}
