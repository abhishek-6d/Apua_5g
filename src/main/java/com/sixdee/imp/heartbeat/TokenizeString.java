/**
 * SixDEE Telecom Solutions Pvt. Ltd.
 * Copyright 2007
 * All Rights Reserved.
 */

package com.sixdee.imp.heartbeat;

import java.util.StringTokenizer;
import java.util.Vector;

/**
 * @author Raju Nair
 * @version 1.7.0.0.1
 * @since Mar 14, 2007
 *
 * Development History
 * 
 * Date					Author				Description
 * ------------------------------------------------------------------------------------------
 * Mar 14, 2007			Raju Nair			Wave 3
 * ------------------------------------------------------------------------------------------
 */
public class TokenizeString {
	private String	tokenizeString;
	private char	token;
	

	public TokenizeString() {
		//
	}
	public TokenizeString(String tokenizeString1, char token1) {
		this.tokenizeString = tokenizeString1;
		this.token = token1;
	}
	
	public void searchParam(String tokenizeString1, char token1) {
		this.tokenizeString = tokenizeString1;
		this.token =token1;
	}

	public int countToken() {
		int tokenCount = 0;
		if (tokenizeString == null) {
			return 0;
		}
		for (int i = 0; i < tokenizeString.length(); i++) {
			if (tokenizeString.charAt(i) == token) {
				tokenCount++;
			}

		}
		return tokenCount;
	}

	
	public String[] getTokens() {
		int numToken=0;
		String[] tokenArray=null;
		boolean check=false;
		numToken = countToken();
		
		if(numToken==0){
			check=true;
		}
		
		if (check){
			return tokenArray;
		}
		
		tokenArray=retriveTokens(numToken);
		

		return tokenArray;
	}

	private String[] retriveTokens(int numToken) {
		int tokenCount=0;
		String[] tokenArray=null;
		String temp = "";
		this.tokenizeString.trim();
		
		tokenArray = new String[numToken+1];
		//logger.info("numToken "+(numToken+1));
		for (int i = 0; i < tokenizeString.length(); i++) {
			
			if (!(tokenizeString.charAt(i) == token)) {
				temp = temp + tokenizeString.charAt(i);
				
			}
		
			if ((i != 0 && tokenizeString.charAt(i) == token) || (i == tokenizeString.length()-1)) {
				//logger.info("tokenCount "+tokenCount+":"+temp);
				tokenArray[tokenCount] = temp;
				tokenArray[tokenCount].trim();
				temp = "";
				tokenCount++;
			}

		}
		
	return tokenArray;
	}

	public void printTokens(String buf[]) {
		for (int i = 0; i < buf.length; i++) {
			System.out.println("Token[ " + i + "]:" + buf[i]);
		}
	}
	
//	 to parse the input string into an array of tokens using String as delimeter:
//	 String tokenizer with current behavior
    public static String [] tokenize(String input, char [] delimiters)
    {
        return tokenize(input, new String(delimiters), false);
    }
    public static String [] tokenize(String input, String delimiters, 
               boolean delimiterAsGroup)
    {
        Vector<String> v = new Vector<String>();
        String toks[] = null;
        if (!delimiterAsGroup)
        {
            StringTokenizer t = new StringTokenizer(input, delimiters);
            while (t.hasMoreTokens())
                v.addElement(t.nextToken());
        }
        else
        {
            int start = 0;
            int end = input.length();
            while (start < end)
            {
                    int delimIdx = input.indexOf(delimiters,start);
                    if (delimIdx < 0)
                    {
                            String tok = input.substring(start);
                            v.addElement(tok);
                            start = end;
                    }
                    else
                    {
                            String tok = input.substring(start, delimIdx);
                            v.addElement(tok);
                            start = delimIdx + delimiters.length();
                    }
            }
        }
        int cnt = v.size();
        if (cnt > 0)
        {
            toks = new String[cnt];
            v.copyInto(toks);
        }
        
        return toks;
    }
	
	/*public static void main(String args[]) {
		String ss="98868989?*";
		TokenizeString ts= new TokenizeString(ss,'?');
		TokenizeString ts1= new TokenizeString(ss,'*');
		logger.info("Count Tokens A "+ts.countToken());
		logger.info("Count Tokens B "+ts1.countToken());
		
	}*/

}
