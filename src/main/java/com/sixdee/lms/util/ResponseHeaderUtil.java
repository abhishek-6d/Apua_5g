/**
 * 
 */
package com.sixdee.lms.util;

import com.sixdee.imp.dto.parser.RERequestHeader;
import com.sixdee.imp.dto.parser.REResponseDataSet;
import com.sixdee.imp.dto.parser.REResponseHeader;
import com.sixdee.imp.dto.parser.ReResponseParameter;
import com.sixdee.lms.util.constant.SystemConstants;

/**
 * @author rahul.kr
 *
 */
public class ResponseHeaderUtil {

	public ResponseHeaderUtil() {
		// TODO Auto-generated constructor stub
	}
	
	public REResponseHeader getResponseHeader(RERequestHeader requestHeader, REResponseDataSet responseDataSet){
		REResponseHeader reResponseHeader = null;
		try{
			reResponseHeader = new REResponseHeader();
			reResponseHeader.setRequestId(requestHeader.getRequestId());
			reResponseHeader.setMsisdn(requestHeader.getMsisdn());
			reResponseHeader.setTimeStamp(requestHeader.getTimeStamp());
			reResponseHeader.setDataSet(responseDataSet);
			for(ReResponseParameter param : responseDataSet.getParameterList()){
				if(param.getId().equals(SystemConstants.RESP_CODE)){
					reResponseHeader.setStatus(param.getValue());
				}else if(param.getId().equals(SystemConstants.RESP_MESSAGE)){
					reResponseHeader.setStatusDesc(param.getValue());
				}
			}
		}finally{
			
		}
		return reResponseHeader;
	}

}
