package com.sixdee.imp.utill;

import java.util.ArrayList;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class DataSets {
	
	@XStreamImplicit
	private ArrayList<DataSet> dataSet1;

	public ArrayList<DataSet> getDataSet1() {
		return dataSet1;
	}

	public void setDataSet1(ArrayList<DataSet> dataSet1) {
		this.dataSet1 = dataSet1;
	}

	
	

}
