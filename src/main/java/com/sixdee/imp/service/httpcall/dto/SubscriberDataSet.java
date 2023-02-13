package com.sixdee.imp.service.httpcall.dto;

import java.util.ArrayList;

public class SubscriberDataSet {
	
	private ArrayList<SubscriberRequestParam> paramlist;

	public ArrayList<SubscriberRequestParam> getParam() {
		return paramlist;
	}

	public void setParam(ArrayList<SubscriberRequestParam> param) {
		this.paramlist = param;
	}

}
