package com.sixdee.service.impl;



import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.imp.common.util.CommonUtil;
import com.sixdee.imp.dto.Dataset;
import com.sixdee.imp.dto.ResponseDTO;
import com.sixdee.imp.dto.parser.RERequestHeader;
import com.sixdee.imp.dto.parser.REResponseDataSet;
import com.sixdee.imp.dto.parser.REResponseHeader;
import com.sixdee.imp.utill.RuleEngine.Parameters;
import com.sixdee.lms.bo.PointExpiryBO;
import com.sixdee.service.PointExpiryService;

public class PointExpiryServiceImpl implements PointExpiryService {
	
	private Logger logger = LogManager.getLogger(PointExpiryServiceImpl.class);
	
	public ResponseDTO pointExpiry(RERequestHeader request) {
		
		 logger.info("REQ ID>>>>"+request.getRequestId());
		 GenericDTO genericDTO = null;
		 PointExpiryBO pointExpiryBO=null;
		 REResponseHeader responseDTO=null;
		 CommonUtil commonUtil = new CommonUtil();
		 ResponseDTO response=new ResponseDTO();
		 REResponseDataSet dataset=null;
		 ArrayList<Parameters>paramlist=null;
		 Dataset dataset2=new Dataset();
		 try {
			 pointExpiryBO=new PointExpiryBO();
			 genericDTO = new GenericDTO();
			 
			
			 genericDTO.setObj(request);
			 
			 genericDTO= pointExpiryBO.executeBusinessProcess(genericDTO);
			 responseDTO=(REResponseHeader)genericDTO.getObj();
			 
			 logger.info("responseDTO>>>>"+responseDTO.toString());
			 
			    response.setRequestId(responseDTO.getRequestId());
				response.setMsisdn(responseDTO.getMsisdn());
				response.setTimeStamp(responseDTO.getTimeStamp());
				response.setKeyWord(responseDTO.getKeyWord());
				response.setStatus(responseDTO.getStatus());
				response.setStatusDesc(responseDTO.getStatusDesc());
				dataset=responseDTO.getDataSet();
				paramlist=dataset.getParameters();
				dataset2.setParameters(paramlist);
				response.setDataSet(dataset2);
				
		 }catch(Exception ex) {
			 
		 }
		 
		 
		return response;
	}

}
