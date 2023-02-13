package com.sixdee.lms.dto.QuerySubscriber;

import com.sixdee.imp.utill.DataSet;

public class QuerySubscriberReq {
	private String requestId;
	private String feature;
	private String action;
	private String sourceNode;
	private String timeStamp;
	private DataSetQs dataset;
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getFeature() {
		return feature;
	}
	public void setFeature(String feature) {
		this.feature = feature;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getSourceNode() {
		return sourceNode;
	}
	public void setSourceNode(String sourceNode) {
		this.sourceNode = sourceNode;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public DataSetQs getDataset() {
		return dataset;
	}
	public void setDataset(DataSetQs dataset) {
		this.dataset = dataset;
	} 

	
}
