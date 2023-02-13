package com.sixdee.imp.dao;

import org.apache.log4j.Logger;

import com.sixdee.imp.common.util.EncrptPassword;
import com.sixdee.imp.common.util.EncrptPassword.EncryptionException;

/**
 * 
 * @author Nithin Kunjappan
 * @version 1.0
 * 
 *          <p>
 *          <b><u>Development History</u></b><br>
 *          <table border="1" width="100%">
 *          <tr>
 *          <td width="15%"><b>Date</b></td>
 *          <td width="20%"><b>Author</b></td>
 *          </tr>
 *          <tr>
 *          <td>April 24,2013 05:54:40 PM</td>
 *          <td>Nithin Kunjappan</td>
 *          </tr>
 *          </table>
 *          </p>
 */

public class UserAuthenticationDAO {

	Logger logger = Logger.getLogger(UserAuthenticationDAO.class);
	int result = 0;

	public int checkStatus(String loyaltyPin, int status, String dtoPIN) {
		if (loyaltyPin.equals(dtoPIN)) {
			if (status == 3)
				result = 2;
			else if (status == 2 || status == 4) {
				result = 3;
			} else if (status == 1)
				result = 0;
			else
				result = 2;
		} else
			result = 1;
		return result;
	}
}
