/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSGlobal.controller;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ajjim
 */
public class clsSMSSender extends Thread
{

    private ArrayList<String> mobileNumberList = new ArrayList<String>();
    private String mainSms="";
    private clsUtility objUtility=new clsUtility();
    
    public clsSMSSender(ArrayList<String> mobileNumberList,String mainSMS)
    {
        this.mobileNumberList=mobileNumberList;
        this.mainSms=mainSMS;
    }

    @Override
    public void run()
    {       
        boolean isSend = objUtility.funSendBulkSMS(mobileNumberList, mainSms);
        System.out.println("SMS Sender->"+isSend);
    }
}
