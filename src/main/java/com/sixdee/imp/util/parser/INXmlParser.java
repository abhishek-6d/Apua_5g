/**
 * 
 */
package com.sixdee.imp.util.parser;

import java.util.ArrayList;

import com.sixdee.imp.dto.parser.in.LoyaltyDiscountDTO;
import com.sixdee.imp.dto.parser.in.ParamList;
import com.sixdee.imp.dto.parser.in.Parameter;
import com.thoughtworks.xstream.XStream;

/**
 * @author Rahul K K
 * @version 1.0.0
 * 
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%">Date</td>
 * <td width="20%">Author</td>
 * <td>Description</td>
 * </tr>
 * <tr>
 * <td>April 24, 2013</td>
 * <td>Rahul K K</td>
 * <td>Created this class</td>
 * </tr>
 * </table>
 * </p>
 */
public class INXmlParser {
	
	private static XStream inXstreamInstance = null;

	public static XStream getXstreamInstance() {
		if(inXstreamInstance == null){
			synchronized (XStream.class) {
				if(inXstreamInstance == null){
					inXstreamInstance = new XStream();
					initINXStream();
				}
			}
		}
		return inXstreamInstance;	
	}

	private static void initINXStream() {
		inXstreamInstance.alias("Request", LoyaltyDiscountDTO.class);
		inXstreamInstance.aliasField("msisdn", LoyaltyDiscountDTO.class, "msisdn");
		inXstreamInstance.aliasField("feature", LoyaltyDiscountDTO.class, "feature");
		
		inXstreamInstance.aliasField("requestId", LoyaltyDiscountDTO.class, "requestId");
		inXstreamInstance.aliasField("accountNumber", LoyaltyDiscountDTO.class, "accountNumber");
		inXstreamInstance.aliasField("componentId", LoyaltyDiscountDTO.class, "componentId");
		inXstreamInstance.aliasField("packageId", LoyaltyDiscountDTO.class, "packageId");
		inXstreamInstance.aliasField("isRetryReqd", LoyaltyDiscountDTO.class, "isRetryReqd");
		inXstreamInstance.aliasField("parameters", LoyaltyDiscountDTO.class, "parameters");
		inXstreamInstance.addImplicitCollection(ParamList.class, "params");
		inXstreamInstance.alias("param", Parameter.class);
		inXstreamInstance.aliasField("id", Parameter.class, "id");
		inXstreamInstance.aliasField("value", Parameter.class, "value");
	}

	public static void main(String[] args) {
		LoyaltyDiscountDTO loyaltyDiscountDTO = new LoyaltyDiscountDTO();
		loyaltyDiscountDTO.setRequestId("1231238");
		loyaltyDiscountDTO.setAccountNumber(1231254);
		loyaltyDiscountDTO.setComponentId("1");
		loyaltyDiscountDTO.setPackageId(1231);
		loyaltyDiscountDTO.setFeature("TEST");
		loyaltyDiscountDTO.setRetryReqd(true);
		loyaltyDiscountDTO.setMsisdn(9902390213l);
		ParamList parameters = new ParamList();
		loyaltyDiscountDTO.setParameters(parameters);
		Parameter param = new Parameter();
		param.setId("1");
		param.setValue("2");
		Parameter param1 = new Parameter();
		param1.setId("1");
		param1.setValue("2");
		ArrayList<Parameter> paramList = new ArrayList<Parameter>();
		paramList.add(param);
		paramList.add(param1);
		parameters.setParams(paramList);
		System.out.println(INXmlParser.getXstreamInstance().toXML(loyaltyDiscountDTO));
	}
}
