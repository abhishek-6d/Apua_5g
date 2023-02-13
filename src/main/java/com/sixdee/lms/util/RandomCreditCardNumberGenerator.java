package com.sixdee.lms.util;


import java.util.List;
import java.util.Stack;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.sixdee.arch.exception.CommonException;
import com.sixdee.imp.dao.CreateAccountDAO;
import com.sixdee.imp.dao.TableDetailsDAO;

/**
 * See the license below. Obviously, this is not a Javascript credit card number
 * generator. However, The following class is a port of a Javascript credit card
 * number generator.
 *
 * @author robweber
 *
 */
public class RandomCreditCardNumberGenerator {
	/*
	 * Javascript credit card number generator Copyright (C) 2006-2012 Graham King
	 *
	 * This program is free software; you can redistribute it and/or modify it
	 * under the terms of the GNU General Public License as published by the
	 * Free Software Foundation; either version 2 of the License, or (at your
	 * option) any later version.
	 *
	 * This program is distributed in the hope that it will be useful, but
	 * WITHOUT ANY WARRANTY; without even the implied warranty of
	 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
	 * Public License for more details.
	 *
	 * You should have received a copy of the GNU General Public License along
	 * with this program; if not, write to the Free Software Foundation, Inc.,
	 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
	 *
	 * www.darkcoding.net
	 */

	private static final Logger logger = Logger.getLogger("RandomAccountGeneration");
	public static final String[] VISA_PREFIX_LIST = new String[] { "4539",
			"4556", "4916", "4532", "4929", "40240071", "4485", "4716", "4" };

	public static final String[] MASTERCARD_PREFIX_LIST = new String[] { "51",
			"52", "53", "54", "55", "2221", "2222", "2223", "2224", "2225", "2226", "2227", "2228", "2229", "223", "224", "225", "226", "227", "228", "229", "23", "24", "25", "26", "270", "271", "2720" };

	public static final String[] AMEX_PREFIX_LIST = new String[] { "34", "37" };

	public static final String[] DISCOVER_PREFIX_LIST = new String[] { "6011" };

	public static final String[] DINERS_PREFIX_LIST = new String[] { "300",
			"301", "302", "303", "36", "38" };

	public static final String[] ENROUTE_PREFIX_LIST = new String[] { "2014",
			"2149" };

	public static final String[] JCB_PREFIX_LIST = new String[] { "35" };

	public static final String[] VOYAGER_PREFIX_LIST = new String[] { "8699" };
	
	public static final String[] MAGNA_JAMAICA = new String[] { "25252509" };

	public static final String[] MAGNA_TRINIDAD = new String[] { "25252507" };

	public static final String[] DIGICEL_JAMAICA = new String[] { "25252607" };
	private static final String[] NojoomOoredoo_PREFIX_LIST = {"287"};
	
	static String strrev(String str) {
		if (str == null)
			return "";
		String revstr = "";
		for (int i = str.length() - 1; i >= 0; i--) {
			revstr += str.charAt(i);
		}

		return revstr;
	}

	/*
	 * 'prefix' is the start of the CC number as a string, any number of digits.
	 * 'length' is the length of the CC number to generate. Typically 13 or 16
	 */
	static String completed_number(String prefix, int length) {

		String ccnumber = prefix;

		// generate digits

		while (ccnumber.length() < (length - 1)) {
			ccnumber += new Double(Math.floor(Math.random() * 10)).intValue();
		}

		// reverse number and convert to int

		String reversedCCnumberString = strrev(ccnumber);

		List<Integer> reversedCCnumberList = new Vector<Integer>();
		for (int i = 0; i < reversedCCnumberString.length(); i++) {
			reversedCCnumberList.add(new Integer(String
					.valueOf(reversedCCnumberString.charAt(i))));
		}

		// calculate sum

		int sum = 0;
		int pos = 0;

		Integer[] reversedCCnumber = reversedCCnumberList
				.toArray(new Integer[reversedCCnumberList.size()]);
		while (pos < length - 1) {

			int odd = reversedCCnumber[pos] * 2;
			if (odd > 9) {
				odd -= 9;
			}

			sum += odd;

			if (pos != (length - 2)) {
				sum += reversedCCnumber[pos + 1];
			}
			pos += 2;
		}

		// calculate check digit

		int checkdigit = new Double(
				((Math.floor(sum / 10) + 1) * 10 - sum) % 10).intValue();
		ccnumber += checkdigit;

		return ccnumber;

	}

	public static String[] credit_card_number(String[] prefixList, int length,
			long howMany) throws CommonException {
		TableDetailsDAO createAccountDAO = null;
		Stack<String> result = null;
		int generators = 0;
		int iterationss = 0;
		long t1 = System.currentTimeMillis();
		try{
			createAccountDAO = new TableDetailsDAO();
			result = new Stack<String>();
			while ( generators < howMany) {
				int randomArrayIndex = (int) Math.floor(Math.random()
						* prefixList.length);
				String ccnumber = prefixList[randomArrayIndex];
				String generatedNumber = completed_number(ccnumber, length);
				//new CreateAccountDAO().isLoyaltyIdExists(generatedNumber)
				if(!createAccountDAO.isLoyaltyIdExists(Long.parseLong(generatedNumber))){
					result.push(generatedNumber);	
					generators++;
				}
				iterationss++;
			}
		}catch (Exception e) {
			throw new CommonException(e.getMessage());
		}finally{
			logger.info("Generation Summary :- Actual Generated : "+generators+" Iterations : "+iterationss+" Time Took "+(System.currentTimeMillis()-t1));
		}

		return result.toArray(new String[result.size()]);
	}

	public static String[] generateMasterCardNumbers(int howMany) throws CommonException {
		return credit_card_number(MASTERCARD_PREFIX_LIST, 16, howMany);
	}

	public static String[] generateMagnaJAMCardNumbers(long howMany) throws CommonException {
		return credit_card_number(MAGNA_JAMAICA, 16, howMany);
	}
	
	public static String[] generateMagnaTTCardNumbers(long howMany) throws CommonException {
		return credit_card_number(MAGNA_TRINIDAD, 16, howMany);
	}
	
	
	public static String generateMasterCardNumber() throws CommonException {
		return credit_card_number(MASTERCARD_PREFIX_LIST, 16, 1)[0];
	}

	public static String generateOoredooCardNumber() throws CommonException {
		return credit_card_number(NojoomOoredoo_PREFIX_LIST, 16, 1)[0];
	}
	public static boolean isValidCreditCardNumber(String creditCardNumber) {
		boolean isValid = false;

		try {
			String reversedNumber = new StringBuffer(creditCardNumber)
					.reverse().toString();
			int mod10Count = 0;
			for (int i = 0; i < reversedNumber.length(); i++) {
				int augend = Integer.parseInt(String.valueOf(reversedNumber
						.charAt(i)));
				if (((i + 1) % 2) == 0) {
					String productString = String.valueOf(augend * 2);
					augend = 0;
					for (int j = 0; j < productString.length(); j++) {
						augend += Integer.parseInt(String.valueOf(productString
								.charAt(j)));
					}
				}

				mod10Count += augend;
			}

			if ((mod10Count % 10) == 0) {
				isValid = true;
			}
		} catch (NumberFormatException e) {
		}

		return isValid;
	}

	public static void main(String[] args) {
		int howMany = 1;
		
		String[] creditcardnumbers = null;
		try {
			creditcardnumbers = generateMagnaJAMCardNumbers(howMany);
		} catch (CommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < creditcardnumbers.length; i++) {
			System.out.println(creditcardnumbers[i]
					+ ":"
					+ (isValidCreditCardNumber(creditcardnumbers[i]) ? "valid"
							: "invalid"));
					System.out.print(creditcardnumbers[i].substring(5)+"@magnarewards.com");
			
		}
	}

	public static String[] generateDigicelJAMCardNumbers(long howMany) throws CommonException {

		return credit_card_number(DIGICEL_JAMAICA, 16, howMany);
	
	}

	public static String[] generateCardNumbers(int howMany, String pattern) throws CommonException {
		logger.info(">>>>>pattern >>>>>>"+pattern);
		String[] PATTERN = new String[] { pattern };
		return credit_card_number(PATTERN, 16, howMany);
	}
}