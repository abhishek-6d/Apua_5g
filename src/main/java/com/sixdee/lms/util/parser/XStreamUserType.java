package com.sixdee.lms.util.parser;

import java.io.Writer;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;
import com.thoughtworks.xstream.io.xml.XppDriver;

public enum XStreamUserType {
	REQUEST_XSTREAM(1, "REQUEST", new XStream(new XppDriver() {
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				boolean cdata = false;

				public void startNode(String name, @SuppressWarnings("rawtypes") Class clazz) {
					super.startNode(name, clazz);
					cdata = (name.equals("DATA"));
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
					} else {
						writer.write(text);
					}
				}
			};
		}
	})), KTR_FILTER_XSTREAM(2, "REQUEST1", new XStream(new DomDriver("UTF-8", new XmlFriendlyReplacer("ddd", "_")))),
	KTR_ONLINE_FILTER_XSTREAM(8, "REQUEST1", new XStream(new DomDriver("UTF-8", new XmlFriendlyReplacer("ddd", "_")))),

	NG_XSTREAM(3, "NG", new XStream(new XppDriver() {
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				boolean cdata = false;

				public void startNode(String name, @SuppressWarnings("rawtypes") Class clazz) {
					super.startNode(name, clazz);
					cdata = (name.equals("dataSet"));
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
					} else {
						writer.write(text);
					}
				}
			};
		}

	})),

	NG_RESPONSE_XSTREAM(6, "NGRESPONSE", new XStream()),

	RESPONSE_XSTREAM(4, "RESPONSE", new XStream(new XppDriver() {
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				boolean cdata = false;

				public void startNode(String name, @SuppressWarnings("rawtypes") Class clazz) {
					super.startNode(name, clazz);
					cdata = (name.equals("DATA"));
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
					} else {
						writer.write(text);
					}
				}
			};
		}
	})), THIRD_PARTY_REQUEST_XSTREAM(5, "REQUEST", new XStream(new XppDriver() {
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				boolean cdata = false;

				public void startNode(String name, @SuppressWarnings("rawtypes") Class clazz) {
					super.startNode(name, clazz);
					cdata = (name.equals("DATA"));
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
					} else {
						writer.write(text);
					}
				}
			};
		}
	})), SIMPLE_REQUEST_XSTREAM(7, "REQUEST", new XStream(new XppDriver() {
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				boolean cdata = false;

				public void startNode(String name, @SuppressWarnings("rawtypes") Class clazz) {
					super.startNode(name, clazz);
					cdata = (name.equals("DATA"));
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
					} else {
						writer.write(text);
					}
				}
			};
		}
	}));
	;

	private int userTypeId;

	private String userType;

	private XStream xStream;

	private XStreamUserType(int userTypeId, String userType, XStream xStream) {
		this.userTypeId = userTypeId;
		this.userType = userType;
		this.xStream = xStream;

		switch (this.userTypeId) {
		/*case 1:
			setRequestAliases();
			break;
		case 2:
			setKtrAliases();
			break;
		case 3:
			setNGAliases();
			break;
		case 4:
			setResponseAliases();
			break;
		case 5:
			setActionAliases();
			break;*/
		case 6:
			//setNgResponseAliases();
		/*case 7:
			setSimpleRequestAliases();
		case 8:
			setOnlineKtrAliases();*/
		default:
		}
	}

	

	/*private void setNgResponseAliases() {

		xStream.alias("Response", NGResponse.class);
		xStream.aliasField("ClientTxnId", NGResponse.class, "ClientTxnId");
		xStream.aliasField("Msisdn", NGResponse.class, "msisdn");
		xStream.aliasField("Timestamp", NGResponse.class, "timeStamp");
		xStream.aliasField("dataSets", NGResponse.class, "dataSets");
		xStream.aliasField("RespDesc", NGResponse.class, "responseFlag");
		xStream.aliasField("RespCode", NGResponse.class, "responseCode");
		xStream.addImplicitCollection(NGDataSet.class, "parameters");
		xStream.alias("dataSet", NGDataSet.class);
		
		xStream.alias("param", NGParam.class);
		xStream.aliasField("id", NGParam.class, "name");
		xStream.aliasField("value", NGParam.class, "value");
	}
*/
	
	public int userTypeId() {
		return userTypeId;
	}

	public String userType() {
		return userType;
	}

	public synchronized XStream xStream() {
		return xStream;
	}

	/**
	 * Get the <tt>XStreamUserType</tt> based on the specified int value
	 * representation.
	 * 
	 * @param value
	 *            is the int value representation.
	 * @return is the enum const related to the specified int value.
	 * @throws IllegalArgumentException
	 *             if there is no enum const associated with specified int
	 *             value.
	 */
	public static XStreamUserType valueOf(int value) {
		for (XStreamUserType val : values()) {
			if (val.userTypeId == value)
				return val;
		}

		throw new IllegalArgumentException("No enum const XStreamUserType with value " + value);
	}

	
	

}
