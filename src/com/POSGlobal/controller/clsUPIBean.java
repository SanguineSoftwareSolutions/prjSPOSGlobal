/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSGlobal.controller;

/**
 *
 * @author Ajim
 */
public class clsUPIBean
{
    private String strMerchantCode;
    private String strReferenceNo;
    private String strCustomerRefNo;
    private String paymentMethodType;
    private String strPaymentStatus;
    private String strTransactionId;
    private String sttTransactionDate;
    private String dblAmount;
    private String isStatic;
    private String payer;

    public String getStrMerchantCode()
    {
	return strMerchantCode;
    }

    public void setStrMerchantCode(String strMerchantCode)
    {
	this.strMerchantCode = strMerchantCode;
    }

    public String getStrReferenceNo()
    {
	return strReferenceNo;
    }

    public void setStrReferenceNo(String strReferenceNo)
    {
	this.strReferenceNo = strReferenceNo;
    }

    public String getStrPaymentStatus()
    {
	return strPaymentStatus;
    }

    public void setStrPaymentStatus(String strPaymentStatus)
    {
	this.strPaymentStatus = strPaymentStatus;
    }

    public String getStrTransactionId()
    {
	return strTransactionId;
    }

    public void setStrTransactionId(String strTransactionId)
    {
	this.strTransactionId = strTransactionId;
    }

    public String getSttTransactionDate()
    {
	return sttTransactionDate;
    }

    public void setSttTransactionDate(String sttTransactionDate)
    {
	this.sttTransactionDate = sttTransactionDate;
    }

    public String getStrCustomerRefNo()
    {
	return strCustomerRefNo;
    }

    public void setStrCustomerRefNo(String strCustomerRefNo)
    {
	this.strCustomerRefNo = strCustomerRefNo;
    }

    public String getPaymentMethodType()
    {
	return paymentMethodType;
    }

    public void setPaymentMethodType(String paymentMethodType)
    {
	this.paymentMethodType = paymentMethodType;
    }

    public String getDblAmount()
    {
	return dblAmount;
    }

    public void setDblAmount(String dblAmount)
    {
	this.dblAmount = dblAmount;
    }

    public String getIsStatic()
    {
	return isStatic;
    }

    public void setIsStatic(String isStatic)
    {
	this.isStatic = isStatic;
    }

    public String getPayer()
    {
	return payer;
    }

    public void setPayer(String payer)
    {
	this.payer = payer;
    }
    
    
    
    
}
