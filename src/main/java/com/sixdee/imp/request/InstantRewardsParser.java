/**
 * 
 */
package com.sixdee.imp.request;

import java.io.Writer;

import com.sixdee.imp.dto.parser.DataSet;
import com.sixdee.imp.dto.parser.InstantRequestDTO;
import com.sixdee.imp.dto.parser.Parameters;
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
public class InstantRewardsParser {
		private static XStream instantRewardsXStream = null;
		
		private InstantRewardsParser(){
			
		}
		
		public static XStream getInstantXStreamInstance(){
			if(instantRewardsXStream == null){
				synchronized (XStream.class) {
					if(instantRewardsXStream == null){
						instantRewardsXStream = new XStream(new XppDriver(){
							public HierarchicalStreamWriter createWriter(Writer out) {
								return new PrettyPrintWriter(out) {
								boolean cdata = true;

								public void startNode(String name, Class clazz) {
								super.startNode(name, clazz);
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
						instantRewardsXStream = initiateInstantRequestParser();
					}
					
				}
			}
			return instantRewardsXStream;
		}

		private static XStream initiateInstantRequestParser() {
			instantRewardsXStream.alias("Request", InstantRequestDTO.class);
			instantRewardsXStream.aliasField("requestId", InstantRequestDTO.class, "requestId");
			instantRewardsXStream.aliasField("FEATURE", InstantRequestDTO.class, "featureId");
			instantRewardsXStream.aliasField("msisdn", InstantRequestDTO.class, "msisdn");
			instantRewardsXStream.aliasField("timeStamp", InstantRequestDTO.class, "timeStamp");
			instantRewardsXStream.aliasField("dataSet", InstantRequestDTO.class, "dataSet");
			instantRewardsXStream.addImplicitCollection(DataSet.class, "tagList");
			instantRewardsXStream.alias("param", Parameters.class);
			instantRewardsXStream.aliasField("id", Parameters.class, "id");
			instantRewardsXStream.aliasField("value", Parameters.class, "value");
			return instantRewardsXStream;
		}
}
