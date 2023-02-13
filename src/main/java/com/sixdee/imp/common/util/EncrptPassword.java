package com.sixdee.imp.common.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;


import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public class EncrptPassword {
	
	public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
	public static final String DES_ENCRYPTION_SCHEME = "DES";
	public static final String DEFAULT_ENCRYPTION_KEY	= "This is a fairly long phrase used to encrypt";

	private KeySpec				keySpec;
	private SecretKeyFactory	keyFactory;
	private Cipher				cipher;
	private static final String encryptionKey = "Six Dee Telecoms Pvt Ltd Bangalore";
	private static final String	UNICODE_FORMAT			= "UTF8";

	public EncrptPassword()
	{
		
	}
	
	public EncrptPassword(String encryptionScheme, String encryptionKey) throws EncryptionException {

		if (encryptionKey == null)
			throw new IllegalArgumentException("encryption key was null");
		if (encryptionKey.trim().length() < 24)
			throw new IllegalArgumentException("encryption key was less than 24 characters");

		try {
			byte[] keyAsBytes = encryptionKey.getBytes(UNICODE_FORMAT);

			if (encryptionScheme.equals(DESEDE_ENCRYPTION_SCHEME)) {
				keySpec = new DESedeKeySpec(keyAsBytes);
			} else if (encryptionScheme.equals(DES_ENCRYPTION_SCHEME)) {
				keySpec = new DESKeySpec(keyAsBytes);
			} else {
				throw new IllegalArgumentException("Encryption scheme not supported: " + encryptionScheme);
			}

			keyFactory = SecretKeyFactory.getInstance(encryptionScheme);
			cipher = Cipher.getInstance(encryptionScheme);

		} catch (InvalidKeyException e) {
			throw new EncryptionException(e);
		} catch (UnsupportedEncodingException e) {
			throw new EncryptionException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new EncryptionException(e);
		} catch (NoSuchPaddingException e) {
			throw new EncryptionException(e);
		}

	}
	
	public class EncryptionException extends Exception {
		public EncryptionException(Throwable t) {
			super(t);
		} 
	}

	private String encrypt(String unencryptedString) throws EncryptionException {
		if (unencryptedString == null || unencryptedString.trim().length() == 0)
			throw new IllegalArgumentException(
					"unencrypted string was null or empty");

		try {
			SecretKey key = keyFactory.generateSecret(keySpec);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] cleartext = unencryptedString.getBytes(UNICODE_FORMAT);
			byte[] ciphertext = cipher.doFinal(cleartext);

			BASE64Encoder base64encoder = new BASE64Encoder();
			return base64encoder.encode(ciphertext);
		} catch (Exception e) {
			throw new EncryptionException(e);
		}
	}
	
	
	private String decrypt(String encryptedString) throws EncryptionException {
		if (encryptedString == null || encryptedString.trim().length() == 0)
			throw new IllegalArgumentException(
					"encrypted string was null or empty");

		try
		{
			SecretKey key = keyFactory.generateSecret( keySpec );
			cipher.init( Cipher.DECRYPT_MODE, key );
			BASE64Decoder base64decoder = new BASE64Decoder();
			byte[] cleartext = base64decoder.decodeBuffer( encryptedString );
			byte[] ciphertext = cipher.doFinal( cleartext );

			
			return bytes2String( ciphertext );
		}
		catch (Exception e)
		{
			throw new EncryptionException( e );
		}
	}
	
	private String bytes2String( byte[] bytes )
	{
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < bytes.length; i++)
		{
			stringBuffer.append( (char) bytes[i] );
		}
		return stringBuffer.toString();
	}

	public static void main(String a[]) {
		
		
		String encryptionScheme = null;
		EncrptPassword encrypter = null;
		String unencryptedString="meetu123456";
		//6dadmin6d
		String encriptString=null;
		
		try{
			
			System.out.println(new EncrptPassword().decryptPassWord("PQTVNU8W"));
			
		encryptionScheme = EncrptPassword.DESEDE_ENCRYPTION_SCHEME;
		encrypter = new EncrptPassword(encryptionScheme, encryptionKey);
		encriptString = encrypter.encrypt(unencryptedString);
		System.out.println(" encripted String "+encriptString);
		System.out.println(" Decripted String "+encrypter.decrypt(encriptString));
		
		}catch(Exception e){
			
			e.printStackTrace();
		}
		         

	}
	
	public String encryptPassword(String password)
	{
		try{
		EncrptPassword encrypter = new EncrptPassword(EncrptPassword.DESEDE_ENCRYPTION_SCHEME, encryptionKey);
		return encrypter.encrypt(password);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}//encriptPassword
	
	public String decryptPassWord(String encriptPassword)
	{
		try{
			EncrptPassword encrypter = new EncrptPassword(EncrptPassword.DESEDE_ENCRYPTION_SCHEME, encryptionKey);
			return encrypter.decrypt(encriptPassword);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}//decryptPassWord
}
