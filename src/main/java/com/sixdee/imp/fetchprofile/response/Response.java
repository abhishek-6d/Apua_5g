package com.sixdee.imp.fetchprofile.response;

public class Response{
    public Dataset dataset;

	public Dataset getDataset() {
		return dataset;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}

	@Override
	public String toString() {
		return "Response [dataset=" + dataset + "]";
	}
    
    
}
