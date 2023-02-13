/**
 * 
 */
package com.sixdee.lms.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import com.sixdee.imp.dto.parser.ReResponseParameter;

/**
 * @author rahul.kr
 *
 */
public class CollectionsControllerUtil {

	public HashMap<String, String> convertListToMap(ArrayList<ReResponseParameter> parameterList) {
		HashMap<String, String> map = new HashMap<>();
		if(parameterList != null){
			map = (HashMap<String, String>) parameterList.stream().collect(Collectors.toMap(ReResponseParameter::getId, ReResponseParameter::getValue));
		}
		return map;
	}
}
