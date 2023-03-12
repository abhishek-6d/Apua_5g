package com.sixdee.service;

import com.sixdee.imp.dto.ResponseDTO;
import com.sixdee.imp.dto.parser.RERequestHeader;
import com.sixdee.imp.dto.parser.REResponseDataSet;
import com.sixdee.imp.dto.parser.REResponseHeader;


public interface PointExpiryService {

	public ResponseDTO pointExpiry(RERequestHeader request);

}
