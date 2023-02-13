/**
 * 
 */
package com.sixdee.imp.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sixdee.lms.dto.CDRInformationDTO;

/**
 * @author Rahul K K
 * @version 1.0.0
 * 
 *          <p>
 *          <b><u>Development History</u></b><br>
 *          <table border="1" width="100%">
 *          <tr>
 *          <td width="15%">Date</td>
 *          <td width="20%">Author</td>
 *          <td>Description</td>
 *          </tr>
 *          <tr>
 *          <td>April 24, 2013</td>
 *          <td>Rahul K K</td>
 *          <td>Created this class</td>
 *          </tr>
 *          </table>
 *          </p>
 */
public class CDRLoggerUtil {

	private static final Logger logger = LogManager.getLogger("CDRLoggerUtil");

	public static void flushFatalCDR(CDRInformationDTO cdrInformationDTO) {
		logger.fatal(cdrInformationDTO.toString());

	}

	public static void flushFatalCDRPostpaid(String logfile) {
		logger.fatal(logfile);
	}

	public static void flushFatalCDR(Logger customLogger, String cdrString) {
		// TODO Auto-generated method stub
		if (customLogger == null)
			customLogger = logger;
		customLogger.fatal(cdrString);
	}

}
