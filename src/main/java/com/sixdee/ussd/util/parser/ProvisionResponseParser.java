/**
 * 
 */
package com.sixdee.ussd.util.parser;

import java.io.Writer;

import com.sixdee.ussd.dto.parser.REResponseDataSet;
import com.sixdee.ussd.dto.parser.REResponseHeader;
import com.sixdee.ussd.dto.parser.ReResponseParameter;
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
public class ProvisionResponseParser {

	private static XStream instReqXStreamParser = null;
	
	private ProvisionResponseParser(){
		
	}
	
	public static XStream getInstanceReqStream(){
		if(instReqXStreamParser == null){
			synchronized (XStream.class) {
				if(instReqXStreamParser == null){
					instReqXStreamParser  =new XStream(new XppDriver(){
					public HierarchicalStreamWriter createWriter(Writer out) {
						return new PrettyPrintWriter(out) {
						boolean cdata = true;

						public void startNode(String name, Class clazz) {
						super.startNode(name, clazz);
						//System.out.println("name - "+name+" clazz - "+clazz);
						//cdata = (name.equalsIgnoreCase("Dat") );
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
					});
					initInstanceXStream();
				}
			}
		}
		return instReqXStreamParser;
	}

	private static void initInstanceXStream() {

		
		//log.info("setQuickPushReqXStream process starting up");
		instReqXStreamParser.alias("Response", REResponseHeader.class);
		instReqXStreamParser.aliasField("ClientTxnId", REResponseHeader.class, "requestId");
		instReqXStreamParser.aliasField("Msisdn", REResponseHeader.class, "msisdn");
		instReqXStreamParser.aliasField("Timestamp", REResponseHeader.class, "timeStamp");
		instReqXStreamParser.aliasField("RespCode", REResponseHeader.class, "status");
		instReqXStreamParser.aliasField("RespDesc", REResponseHeader.class, "statusDesc");
		instReqXStreamParser.aliasField("keyWord", REResponseHeader.class, "keyWord");
		instReqXStreamParser.aliasField("dataSet", REResponseHeader.class, "dataSet");
		
		instReqXStreamParser.addImplicitCollection(REResponseDataSet.class, "responseParam");
		instReqXStreamParser.alias("param", ReResponseParameter.class);
		instReqXStreamParser.aliasField("id", ReResponseParameter.class, "id");
		instReqXStreamParser.aliasField("value", ReResponseParameter.class, "value");
		
		
			
	}
	
	
	public static void main(String[] args) {/*
		SimpleDateFormat sdFormat = new SimpleDateFormat("ddMMyyyyHHmmSS");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		REResponseHeader instDto = new REResponseHeader();
		instDto.setKeyWord("123");
		instDto.setStatus("0");
		instDto.setStatusDesc("SUC");
		instDto.setTimeStamp(sdFormat.format(new Date()));
		DataSet tagList = new DataSet();
		ArrayList<Tag> arrayList = new ArrayList<Tag>();
		Tag tag = new Tag();
		tag.setId("InstanceTag");
		tag.setValue("Dummy testing");
		arrayList.add(tag);
		tagList.setTagList(arrayList);
		instDto.setDataSet(tagList);
		//instancePushReqParameterDTO.setTagList(tagList);
		System.out.println(InstanceResponseParser.getInstanceReqStream().toXML(instDto));
	*/}
}
