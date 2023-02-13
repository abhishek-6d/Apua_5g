/**
 * SixDEE Telecom Solutions Pvt. Ltd.
 * Copyright 2007
 * All Rights Reserved.
 */

package com.sixdee.mgmnt.client;

/**
 * @author Sumanth Kalyan
 * @version 1.7.0.0
 * @since Mar 9, 2007
 *
 * Development History
 * 
 * Date					Author				Description
 * ------------------------------------------------------------------------------------------
 * Mar 9, 2007			Sumanth Kalyan			
 * ------------------------------------------------------------------------------------------
 */
public class AlphaNumericCheck {

	public AlphaNumericCheck (){
		//
	}
	
	/**
	 * Checks if a supplied string contains only AlphaNumeric Characters. A character is considered
	 * to be a digit if it is not in the range '\u2000' <= ch <= '\u2FFF' and its
	 * Unicode name contains the word "DIGIT".
	 *	
	 * This method removes all the Specail charactes from the String.
	 *
	 * @param checkString The String to be checked
	 * @return true if the String contains only digits, false otherwise
	 */
	public String CheckString(String an){

		char[] CharArray=an.toCharArray();
		StringBuffer sb=new StringBuffer();

		for(int i=0;i<CharArray.length;i++){
			boolean isCheck=Character.isLetterOrDigit(CharArray[i]);
			if(isCheck){
				sb.append(CharArray[i]);
			}
		}
		return sb.toString();
	}

	/**
	 * Checks if a supplied string contains only digits. A character is considered
	 * to be a digit if it is not in the range '\u2000' <= ch <= '\u2FFF' and its
	 * Unicode name contains the word "DIGIT".
	 *
	 * @param checkString The String to be checked
	 * @return true if the String contains only digits, false otherwise
	 */
	public boolean isStringAllDigits(String checkString){
		for(int i=0; i<checkString.length(); i++){
			boolean check = Character.isDigit(checkString.charAt(i));

			if(!check){
				return false;
			}
		}
		return true;
	}


	/**
	 * @param args
	 */

	public static void main(String[] args) {
		
		


		
	}

}
