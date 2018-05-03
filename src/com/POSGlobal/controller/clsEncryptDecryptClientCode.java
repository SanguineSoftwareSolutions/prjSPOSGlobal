/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSGlobal.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class clsEncryptDecryptClientCode 
{
    public static String encKey = "04081977";
    public static String encryptCode=""; 
    public static String decryptCode=""; 
  

    public static String funEncryptClientCode(String encryptString)
     {
        try 
        {
            encryptCode = clsGlobalSingleObject.getObjPasswordEncryptDecreat().encrypt(encKey,encryptString );
              
        }
        catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(clsEncryptDecryptClientCode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(clsEncryptDecryptClientCode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(clsEncryptDecryptClientCode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(clsEncryptDecryptClientCode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidAlgorithmParameterException ex) {
            Logger.getLogger(clsEncryptDecryptClientCode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(clsEncryptDecryptClientCode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(clsEncryptDecryptClientCode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(clsEncryptDecryptClientCode.class.getName()).log(Level.SEVERE, null, ex);
        }
        return encryptCode;
     }
    
    public static String funDecryptClientCode(String decryptString)
     {
        try 
        {
           decryptCode = clsGlobalSingleObject.getObjPasswordEncryptDecreat().decrypt(encKey, decryptString);
         } 
        catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(clsEncryptDecryptClientCode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(clsEncryptDecryptClientCode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(clsEncryptDecryptClientCode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(clsEncryptDecryptClientCode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidAlgorithmParameterException ex) {
            Logger.getLogger(clsEncryptDecryptClientCode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(clsEncryptDecryptClientCode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(clsEncryptDecryptClientCode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(clsEncryptDecryptClientCode.class.getName()).log(Level.SEVERE, null, ex);
        }
          return decryptCode;
     }
  
    
}
