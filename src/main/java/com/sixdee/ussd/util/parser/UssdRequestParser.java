/**
 * 
 */
package com.sixdee.ussd.util.parser;

import java.io.Writer;
import java.util.ArrayList;

import com.sixdee.ussd.dto.parser.ussdRequest.DataSet;
import com.sixdee.ussd.dto.parser.ussdRequest.Parameters;
import com.sixdee.ussd.dto.parser.ussdRequest.Service;
import com.sixdee.ussd.dto.parser.ussdRequest.ServiceList;
import com.sixdee.ussd.dto.parser.ussdRequest.UssdRequestDTO;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

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
public class UssdRequestParser {

	
	private static XStream ussdReqXStream = null;
	
	private UssdRequestParser(){
		
	}
	
	
	public static  XStream getUssdParser(){
		if(ussdReqXStream == null){
		    synchronized (XStream.class) {
			if (ussdReqXStream == null) {
			    ussdReqXStream = new XStream(new XppDriver() {
					public HierarchicalStreamWriter createWriter(Writer out) {
						return new PrettyPrintWriter(out) {
						boolean cdata = true;

						public void startNode(String name, Class clazz) {
						super.startNode(name, clazz);
						//System.out.println("name - "+name+" clazz - "+clazz);
						//cdata = (name.equalsIgnoreCase("Services")|| (name.equalsIgnoreCase("param")));
				}

						protected void writeText(QuickWriter writer, String text) {
						if (cdata) {
						//System.out.println("text- "+text);
						if (!text.equals("")) {
						writer.write("<![CDATA[");
						writer.write(text);
						writer.write("]]>");
						} else {
						writer.write(text);
						}
						} else {
						writer.write(text);
						}
						}
						};
						}
						}
	);
			    initUssdXstream();
			}
		}	
		}
		return ussdReqXStream ;
	    
}

	private static void initUssdXstream() {
		ussdReqXStream.alias("Request", UssdRequestDTO.class);
		ussdReqXStream.aliasField("TransactionId", UssdRequestDTO.class, "transactionId");
		ussdReqXStream.aliasField("TimeStamp", UssdRequestDTO.class, "timeStamp");
		ussdReqXStream.aliasField("SessionId", UssdRequestDTO.class, "sessionId");
		ussdReqXStream.aliasField("Msisdn", UssdRequestDTO.class, "msisdn");
		ussdReqXStream.aliasField("StarCode", UssdRequestDTO.class, "starCode");
		ussdReqXStream.aliasField("Application", UssdRequestDTO.class, "application");
		ussdReqXStream.aliasField("TraversalPath", UssdRequestDTO.class, "traversalPath");
		ussdReqXStream.aliasField("Channel", UssdRequestDTO.class, "channel");
		ussdReqXStream.aliasField("Services", UssdRequestDTO.class, "serviceList");
		ussdReqXStream.addImplicitCollection(ServiceList.class, "services");
		ussdReqXStream.alias("Service", Service.class);
		ussdReqXStream.aliasField("MessageText", Service.class, "messageText");
		ussdReqXStream.aliasField("NextService", Service.class, "nextService");
		ussdReqXStream.addImplicitCollection(Service.class, "paramList");
		
		ussdReqXStream.alias("Param", Parameters.class);
		
		ussdReqXStream.aliasField("id", Parameters.class, "id");
		ussdReqXStream.aliasField("value", Parameters.class, "value");
	}
	
	
	public static void main(String[] args) {
		UssdRequestDTO ussdRequestDTO = new UssdRequestDTO();
		ussdRequestDTO.setTransactionId("123123");
		ussdRequestDTO.setSessionId("2138791283");
		ussdRequestDTO.setTimeStamp("32423432");
		ussdRequestDTO.setMsisdn("9902390347");
		ussdRequestDTO.setStarCode("123");
		ussdRequestDTO.setApplication("USDDS");
		ussdRequestDTO.setTraversalPath("1/2");
		ussdRequestDTO.setChannel("USSD");
		ArrayList<Service> serviceList = new ArrayList<Service>();
		Service service = new Service();
		service.setMessageText("1.REG");
		service.setNextService("SREG");
		ArrayList<Parameters> params = new ArrayList<Parameters>();
		
		Parameters p = new Parameters();
		p.setId("K");
		p.setValue("KL");
		params.add(p);
		service.setParamList(params);
		serviceList.add(service);
		params = new ArrayList<Parameters>();
		
		Service service1 = new Service();
		service1.setMessageText("1.OREG");
		service1.setNextService("OREG");
		Parameters p1 = new Parameters();
		p.setId("O");
		p.setValue("KO");
		params.add(p1);
		service1.setParamList(params);
		serviceList.add(service1);
		ServiceList s = new ServiceList();
		s.setServices(serviceList);
		ussdRequestDTO.setServiceList(s);
		DataSet d = new DataSet();
/*		Parameters p1 = new Parameters();
		p.setId("1");
		p.setValue("ALL");
		ArrayList<Parameters> params = new ArrayList<Parameters>();
		
		params.add(p);
*/		/*Parameters p1 = new Parameters();
		
		p1.setId("2");
		p1.setValue("delete");
		params.add(p1);
		d.setParams(params);
		String dataset = UssdRequestParser.getUssdParser().toXML(d);
		System.out.println(dataset);
		ussdRequestDTO.setDataSet(dataset);*/
		String xml = UssdRequestParser.getUssdParser().toXML(ussdRequestDTO);
		System.out.println(xml);
		String ussdXml = "<USSDRequest><requestId>123123</requestId><msisdn>9902390347</msisdn><starCode>123</starCode>" +
		"<keyWord>LMSREG</keyWord><dataSet><param><id>1</id><value>ALL</value></param></dataSet>" +
		"<featureId>REG</featureId></USSDRequest>";
		UssdRequestDTO ussd = (UssdRequestDTO) UssdRequestParser.getUssdParser().fromXML(ussdXml);
	/*	System.out.println(ussd.getDataSet());
		DataSet dset = (DataSet)UssdRequestParser.getUssdParser().fromXML(ussd.getDataSet());
		System.out.println(dset);
	*/	//DataSet dset = (DataSet)UssdRequestParser.getUssdParser().fromXML(ussd.getDataSet());
	}
}
