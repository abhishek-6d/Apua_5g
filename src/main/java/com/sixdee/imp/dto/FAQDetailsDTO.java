package com.sixdee.imp.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FAQDetailsDTO extends CommonVO {

	List FAQList = new ArrayList<FAQQADetailsDTO>();

	public List getFAQList() {
		return FAQList;
	}

	public void setFAQList(List list) {
		FAQList = list;
	}
	
	
}
