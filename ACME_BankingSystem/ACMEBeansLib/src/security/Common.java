/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package security;
/**
 *
 * @author s3287015
 */

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Common{
	private static Common instance;
	private MessageDigest encryption;

	public Common(){
		try{
			encryption= MessageDigest.getInstance("MD5");
		}catch(NoSuchAlgorithmException ex){ ex.printStackTrace(); }
	}

	public static Common getInstance(){
		if(instance!= null)
			instance= new Common();

		return instance;
	}

	public String md5(String password){
		encryption.update(password.getBytes());

		byte encrytedByte[]			= encryption.digest();
		StringBuffer encryptedString= new StringBuffer();

		for(int i= 0; i< encrytedByte.length; i++)
			encryptedString.append(
				Integer.toString((encrytedByte[i] & 0xff)+ 0x100, 16).substring(1));

		return encryptedString.toString();
	}
	// the source code came from http://stackoverflow.com/questions/11665360/convert-md5-into-string-in-java
}
