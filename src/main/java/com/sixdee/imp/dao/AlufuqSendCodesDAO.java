package com.sixdee.imp.dao;

import org.apache.log4j.Logger;

public class AlufuqSendCodesDAO {
	private Logger logger = Logger.getLogger(AlufuqSendCodesDAO.class);
	public void writeCdr( String requestId, String msisdn, String timeStamp) {
		
		logger.fatal(requestId+"|"+msisdn+"|"+timeStamp);
		
	}


}
