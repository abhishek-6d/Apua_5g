package com.sixdee.lms.dto.QuerySubscriber;

		public class FetchSubscriberResp {
		
			private String requestTimestamp;

		    private String responseId;

		    private String requestId;

		    private String sourceNode;

		    private String resultCode;

		    private String responseTimestamp;
		    
		    private DataSet dataSet;

			public String getRequestTimestamp() {
				return requestTimestamp;
			}

			public void setRequestTimestamp(String requestTimestamp) {
				this.requestTimestamp = requestTimestamp;
			}

			public String getResponseId() {
				return responseId;
			}

			public void setResponseId(String responseId) {
				this.responseId = responseId;
			}

			public String getRequestId() {
				return requestId;
			}

			public void setRequestId(String requestId) {
				this.requestId = requestId;
			}

			public String getSourceNode() {
				return sourceNode;
			}

			public void setSourceNode(String sourceNode) {
				this.sourceNode = sourceNode;
			}

			public String getResultCode() {
				return resultCode;
			}

			public void setResultCode(String resultCode) {
				this.resultCode = resultCode;
			}

			public String getResponseTimestamp() {
				return responseTimestamp;
			}

			public void setResponseTimestamp(String responseTimestamp) {
				this.responseTimestamp = responseTimestamp;
			}

			public DataSet getDataSet() {
				return dataSet;
			}

			public void setDataSet(DataSet dataSet) {
				this.dataSet = dataSet;
			}
		    
		    

		}
