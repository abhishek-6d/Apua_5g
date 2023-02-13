package com.sixdee.imp.utill;

import java.io.Writer;


import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class RequestParseXML{
	
	public static XStream getRequest() {

		XStream xstream = new XStream(new XppDriver() {

			public HierarchicalStreamWriter createWriter(Writer out) {
				return new PrettyPrintWriter(out) {
					boolean cdata = false;
					boolean actStep = false;

					public void startNode(String name, Class clazz) {
						super.startNode(name, clazz);
						cdata = (name.equals("DATA") || name.equals("RESP-DESC"));
						actStep = (name.equalsIgnoreCase("actStep")) || (name.equalsIgnoreCase("ifId"));
					}

					protected void writeText(QuickWriter writer, String text) {
						if (!cdata) {
							if (!text.equals("")) {
								writer.write("<![CDATA[");
								writer.write(text);
								writer.write("]]>");
							} else {
								writer.write(text);
							}
						} else if (actStep) {

						} else {
							writer.write(text);
						}
					}
				};
			}// createWriter
		});

		xstream.alias("Request", Request.class);
		xstream.aliasField("requestId", Request.class, "requestId");
		xstream.aliasField("msisdn", Request.class, "msisdn");
		xstream.aliasField("timeStamp", Request.class, "timeStamp");
		xstream.aliasField("dataSet", Request.class, "dataSet");
		xstream.aliasField("keyWord", Request.class, "keyWord");
		
		xstream.alias("dataSet", DataSet.class);
		xstream.addImplicitCollection(DataSet.class, "parameter1");
		xstream.aliasField("param", DataSet.class, "param");

		xstream.alias("param", Param.class);
		xstream.aliasField("id", Param.class, "id");
		xstream.aliasField("value", Param.class, "value");

		xstream.setMode(XStream.XPATH_RELATIVE_REFERENCES);
		return xstream;
	}
	
	public static XStream responseXstream() {

		XStream xstream = new XStream(new XppDriver() {

			public HierarchicalStreamWriter createWriter(Writer out) {
				return new PrettyPrintWriter(out) {
					boolean cdata = false;
					boolean actStep = false;

					public void startNode(String name, Class clazz) {
						super.startNode(name, clazz);
						cdata = (name.equals("DATA") || name.equals("RESP-DESC"));
						actStep = (name.equalsIgnoreCase("actStep")) || (name.equalsIgnoreCase("ifId"));
					}

					protected void writeText(QuickWriter writer, String text) {
						if (!cdata) {
							if (!text.equals("")) {
								writer.write("<![CDATA[");
								writer.write(text);
								writer.write("]]>");
							} else {
								writer.write(text);
							}
						} else if (actStep) {

						} else {
							writer.write(text);
						}
					}
				};
			}// createWriter
		});

		xstream.alias("Response", Response.class);
		xstream.aliasField("ClientTxnId", Response.class, "requestId");
		xstream.aliasField("Msisdn", Response.class, "msisdn");
		xstream.aliasField("Timestamp", Response.class, "timeStamp");
		xstream.aliasField("RespCode", Response.class, "respCode");
		xstream.aliasField("RespDesc", Response.class, "respDesc");
		
		
		xstream.aliasField("dataSet", Response.class, "dataSet");
		xstream.alias("dataSet", DataSet.class);
		xstream.addImplicitCollection(DataSet.class, "parameter1");

		xstream.alias("param", Param.class);
		xstream.aliasField("id", Param.class, "id");
		xstream.aliasField("value", Param.class, "value");

		xstream.setMode(XStream.XPATH_RELATIVE_REFERENCES);
		return xstream;
	}
	public static XStream responseMultiDataXstream() {

		XStream xstream = new XStream(new XppDriver() {

			public HierarchicalStreamWriter createWriter(Writer out) {
				return new PrettyPrintWriter(out) {
					boolean cdata = false;
					boolean actStep = false;

					public void startNode(String name, Class clazz) {
						super.startNode(name, clazz);
						cdata = (name.equals("DATA") || name.equals("RESP-DESC"));
						actStep = (name.equalsIgnoreCase("actStep")) || (name.equalsIgnoreCase("ifId"));
					}

					protected void writeText(QuickWriter writer, String text) {
						if (!cdata) {
							if (!text.equals("")) {
								writer.write("<![CDATA[");
								writer.write(text);
								writer.write("]]>");
							} else {
								writer.write(text);
							}
						} else if (actStep) {

						} else {
							writer.write(text);
						}
					}
				};
			}// createWriter
		});

		xstream.alias("Response", Response.class);
		xstream.aliasField("ClientTxnId", Response.class, "requestId");
		xstream.aliasField("Timestamp", Response.class, "timeStamp");
		xstream.aliasField("Msisdn", Response.class, "msisdn");
		xstream.aliasField("RespCode", Response.class, "respCode");
		xstream.aliasField("RespDesc", Response.class, "respDesc");
		xstream.aliasField("dataSets", Response.class, "dataSets");
		
		xstream.alias("dataSets", DataSets.class);
		xstream.addImplicitCollection(DataSets.class, "dataSet1");
		xstream.aliasField("dataSet", DataSets.class, "dataSet");
		
		xstream.alias("dataSet", DataSet.class);
		xstream.addImplicitCollection(DataSet.class, "parameter1");
		xstream.aliasField("param", DataSet.class, "param");

		xstream.alias("param", Param.class);
		xstream.aliasField("id", Param.class, "id");
		xstream.aliasField("value", Param.class, "value");

		xstream.setMode(XStream.XPATH_RELATIVE_REFERENCES);
		return xstream;
	}
	
}
