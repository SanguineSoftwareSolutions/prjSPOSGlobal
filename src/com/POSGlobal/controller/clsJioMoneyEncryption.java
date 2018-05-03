package com.POSGlobal.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
/**
 *
 * @author abhinendra.gautam
 */
public class clsJioMoneyEncryption 
{
    public String encrypt(String requestData, String secretKey)
    {
		
		String encryptedData = "";
		//String decryptedData = "";
		
		try {
			// Create cipher and key 
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                        SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), "AES");
            
                        //Encrypt the data
                        cipher.init(Cipher.ENCRYPT_MODE, key);
                        byte[] encryptedText = cipher.doFinal(requestData.getBytes("UTF-8"));
                        encryptedData = Base64.getEncoder().encodeToString(encryptedText);
                        System.out.println("Encrypted Data: " + encryptedData);
            
                        //Decrypt the data           
            
		} 
                catch (Exception e) 
                {    
			e.printStackTrace();
			System.out.println(e);
		}
		return encryptedData;
    }
     public String Decrypt(String requestData, String secretKey)
       {
		
		//String encryptedData = "";
		String decryptedData = "";
		
		try 
                {
		    // Create cipher and key 
		    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                    SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), "AES");
            
                    //Encrypt the data          
                    //Decrypt the data
                    cipher.init(Cipher.DECRYPT_MODE, key);
                    byte[] bytStr = Base64.getDecoder().decode(requestData);
                    decryptedData = new String(cipher.doFinal(bytStr), "UTF-8");
                    System.out.println("Decrypted Data: " + decryptedData);
            
		} 
                catch (Exception e) 
                {
			e.printStackTrace();
			System.out.println(e);
		}
		return decryptedData;
       }
}
