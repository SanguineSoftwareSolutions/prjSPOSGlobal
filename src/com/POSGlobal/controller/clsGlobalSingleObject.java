/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSGlobal.controller;

import com.POSGlobal.controller.*;

/**
 *
 * @author ritesh
 */
public class clsGlobalSingleObject {
   private static clsPasswordEncryptDecreat objPasswordEncryptDecreat=null;
   
   private clsGlobalSingleObject(){}
   
   /**
     * @return the objPasswordEncryptDecreat
     */
    public static clsPasswordEncryptDecreat getObjPasswordEncryptDecreat() 
    {
        if (objPasswordEncryptDecreat == null) 
        {
            objPasswordEncryptDecreat = new clsPasswordEncryptDecreat();
        }
        return objPasswordEncryptDecreat;
    }
}
