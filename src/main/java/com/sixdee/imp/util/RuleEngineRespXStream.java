/**
 * 
 */
package com.sixdee.imp.util;

import com.sixdee.imp.dto.RERespone;
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
public class RuleEngineRespXStream {

	private static XStream reRespXStream = null;
	private RuleEngineRespXStream(){
		
	}
	
	public static XStream getRERespXStream() {
		if (reRespXStream == null) {
			synchronized (XStream.class) {
				if (reRespXStream == null) {
					reRespXStream = new XStream();
					initreRespXStream();
				}

			}
		}
		return reRespXStream;
	}

	
	private static void initreRespXStream() {
		reRespXStream.alias("Response", RERespone.class);
		reRespXStream.aliasField("ClientTxnId", RERespone.class, "cliTransactionId");
		reRespXStream.aliasField("Timestamp", RERespone.class, "timeStamp");
		reRespXStream.aliasField("RespCode", RERespone.class, "respCode");
		reRespXStream.aliasField("respDesc", RERespone.class, "respDesc");
		reRespXStream.aliasField("Msisdn", RERespone.class, "msisdn");
		
	}
}
