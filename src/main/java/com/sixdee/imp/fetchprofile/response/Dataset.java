package com.sixdee.imp.fetchprofile.response;

import java.util.List;

public class Dataset{
    public int totalrecordcnt;
    public int recordcnt;
    public List<ServiceInfo> service_info;
	public int getTotalrecordcnt() {
		return totalrecordcnt;
	}
	public void setTotalrecordcnt(int totalrecordcnt) {
		this.totalrecordcnt = totalrecordcnt;
	}
	public int getRecordcnt() {
		return recordcnt;
	}
	public void setRecordcnt(int recordcnt) {
		this.recordcnt = recordcnt;
	}
	public List<ServiceInfo> getService_info() {
		return service_info;
	}
	public void setService_info(List<ServiceInfo> service_info) {
		this.service_info = service_info;
	}
	@Override
	public String toString() {
		return "Dataset [totalrecordcnt=" + totalrecordcnt + ", recordcnt=" + recordcnt + ", service_info="
				+ service_info + "]";
	}
    
    
}
