/**
 * 
 */
package com.sixdee.ussd.util.parser;

import com.sixdee.ussd.dto.parser.ussdRequest.Parameters;
import com.sixdee.ussd.dto.parser.ussdRequest.Service;
import com.sixdee.ussd.dto.parser.ussdRequest.ServiceList;
import com.sixdee.ussd.dto.parser.ussdResponse.UssdResponseDTO;
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
public class UssdResponseParser {

	
	private static XStream ussdResXStream = null;
	
	private UssdResponseParser(){
		
	}
	
/*	public static XStream getUssdRespXStream(){
		if(ussdResXStream == null){
			synchronized (XStream.class) {
				if(ussdResXStream == null){
					ussdResXStream = new XStream();
					initUssdRespXStream();
				}
			}
		}
		return ussdResXStream;
	}
*/
	
	public static XStream getUssdRespXStream(){
		if(ussdResXStream == null){
			synchronized (XStream.class) {
				if(ussdResXStream == null){
					ussdResXStream = new XStream();
					initUssdRespXStream();
				}
			}
		}
		return ussdResXStream;
	}
	private static void initUssdRespXStream() {
		ussdResXStream.alias("Response", UssdResponseDTO.class);
		ussdResXStream.aliasField("TransactionId", UssdResponseDTO.class, "transactionId");
		ussdResXStream.aliasField("SessionId", UssdResponseDTO.class, "sessionId");
		ussdResXStream.aliasField("TimeStamp", UssdResponseDTO.class,"timeStamp");
		ussdResXStream.aliasField("Msisdn", UssdResponseDTO.class, "msisdn");
		ussdResXStream.aliasField("Application", UssdResponseDTO.class, "application");
		ussdResXStream.aliasField("TraversalPath", UssdResponseDTO.class, "traversalPath");
		ussdResXStream.aliasField("EOS", UssdResponseDTO.class, "isEos");
		ussdResXStream.aliasField("Services",UssdResponseDTO.class,"serviceList");
		ussdResXStream.addImplicitCollection(ServiceList.class, "services");
		ussdResXStream.alias("Service", Service.class);
		ussdResXStream.aliasField("MenuIndex", Service.class, "menuIndex");
		ussdResXStream.aliasField("MessageText", Service.class, "messageText");
		ussdResXStream.aliasField("NextService", Service.class, "nextService");
		ussdResXStream.aliasField("default", Service.class, "defaultOption");
		ussdResXStream.addImplicitCollection(Service.class, "paramList");
		
		ussdResXStream.alias("Param", Parameters.class);
		
		ussdResXStream.aliasField("id", Parameters.class, "id");
		ussdResXStream.aliasField("value", Parameters.class, "value");
		ussdResXStream.aliasField("StatusCode", UssdResponseDTO.class,"status");
		ussdResXStream.aliasField("StatusDesc", UssdResponseDTO.class, "statusDesc");
	}
	
	public static void main(String[] args) {/*
		UssdResponseDTO ussdResponseDTO = new UssdResponseDTO();
		ussdResponseDTO.setTransactionId("123");
		reccomendations reccomendations  =new Reccomendations();
		//reccomendations.setAbc("res");
		ArrayList<Reccomendation> recoList = new ArrayList<Reccomendation>();
		Reccomendation reccomendation = new Reccomendation();
		reccomendation.setApplication("USSD");
		recoList.add(reccomendation);
		Reccomendation reccomendation1 = new Reccomendation();
		reccomendation1.setApplication("USSD");
		recoList.add(reccomendation1);
		reccomendations.setReccomendationList(recoList);
		ussdResponseDTO.setReccomendations(reccomendations);
		String xml = UssdResponseParser.getUssdRespXStream().toXML(ussdResponseDTO);
		System.out.println(xml);
		System.out.println(UssdResponseParser.getUssdRespXStream().fromXML(xml));
	*/}
}
