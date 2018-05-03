/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSGlobal.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author sanguine
 */
public class clsAdvOrderItemDtl
{

    private String strAdvOrderNo;

    private String strItemCode;

    private String strItemName;

    private Double dblQty;

    private Double dblAmount;

    private Double dblWeight;

    private String strCustomerCode;

    private String strCharNameValuePair;
    
    private String strAdvBookingNo;
    private String dteAdvBookingDate;
    private String dteOrderFor;
    private String strCustomerName;
    private String longMobileNo;
    private String strCharCode;
    private String strCharName;
    private String strCharValues;
    private double dblQuantity;
    private double dblRate;
    private double dblAdvDeposite;  
    private Double dblTotalAmount;
    private String dteVoidedDate;
    private String strReasonCode;
    private String strRemarks;
    
    
    

    public String getStrAdvOrderNo()
    {
        return strAdvOrderNo;
    }
    
    public static Collection<clsAdvOrderItemDtl>listOdAdvOrderItemDtl;

    public void setStrAdvOrderNo(String strAdvOrderNo)
    {
        this.strAdvOrderNo = strAdvOrderNo;
    }

    public String getStrItemCode()
    {
        return strItemCode;
    }

    public void setStrItemCode(String strItemCode)
    {
        this.strItemCode = strItemCode;
    }

    public String getStrItemName()
    {
        return strItemName;
    }

    public void setStrItemName(String strItemName)
    {
        this.strItemName = strItemName;
    }

    public Double getDblQty()
    {
        return dblQty;
    }

    public void setDblQty(Double dblQty)
    {
        this.dblQty = dblQty;
    }

    public Double getDblAmount()
    {
        return dblAmount;
    }

    public void setDblAmount(Double dblAmount)
    {
        this.dblAmount = dblAmount;
    }

    public Double getDblWeight()
    {
        return dblWeight;
    }

    public void setDblWeight(Double dblWeight)
    {
        this.dblWeight = dblWeight;
    }

    public String getStrCustomerCode()
    {
        return strCustomerCode;
    }

    public void setStrCustomerCode(String strCustomerCode)
    {
        this.strCustomerCode = strCustomerCode;
    }

    public String getStrCharNameValuePair()
    {
        return strCharNameValuePair;
    }

    public void setStrCharNameValuePair(String strCharNameValuePair)
    {
        this.strCharNameValuePair = strCharNameValuePair;
    }

    public static Collection<clsAdvOrderItemDtl> getListOdAdvOrderItemDtl()
    {
        
        List<clsAdvOrderItemDtl> listOfAdvOrderItemDtls=new ArrayList<>();
        
        return listOfAdvOrderItemDtls;
    }

    public static void setListOdAdvOrderItemDtl(Collection<clsAdvOrderItemDtl> listOdAdvOrderItemDtl)
    {
        clsAdvOrderItemDtl.listOdAdvOrderItemDtl = listOdAdvOrderItemDtl;
    }

    public String getStrAdvBookingNo()
    {
        return strAdvBookingNo;
    }

    public void setStrAdvBookingNo(String strAdvBookingNo)
    {
        this.strAdvBookingNo = strAdvBookingNo;
    }

    public String getDteAdvBookingDate()
    {
        return dteAdvBookingDate;
    }

    public void setDteAdvBookingDate(String dteAdvBookingDate)
    {
        this.dteAdvBookingDate = dteAdvBookingDate;
    }

    public String getDteOrderFor()
    {
        return dteOrderFor;
    }

    public void setDteOrderFor(String dteOrderFor)
    {
        this.dteOrderFor = dteOrderFor;
    }

    public String getStrCustomerName()
    {
        return strCustomerName;
    }

    public void setStrCustomerName(String strCustomerName)
    {
        this.strCustomerName = strCustomerName;
    }

    public String getLongMobileNo()
    {
        return longMobileNo;
    }

    public void setLongMobileNo(String longMobileNo)
    {
        this.longMobileNo = longMobileNo;
    }

    public String getStrCharCode()
    {
        return strCharCode;
    }

    public void setStrCharCode(String strCharCode)
    {
        this.strCharCode = strCharCode;
    }

    public String getStrCharName()
    {
        return strCharName;
    }

    public void setStrCharName(String strCharName)
    {
        this.strCharName = strCharName;
    }

    public String getStrCharValues()
    {
        return strCharValues;
    }

    public void setStrCharValues(String strCharValues)
    {
        this.strCharValues = strCharValues;
    }

    public double getDblQuantity()
    {
        return dblQuantity;
    }

    public void setDblQuantity(double dblQuantity)
    {
        this.dblQuantity = dblQuantity;
    }

    public double getDblRate()
    {
        return dblRate;
    }

    public void setDblRate(double dblRate)
    {
        this.dblRate = dblRate;
    }

    public double getDblAdvDeposite()
    {
        return dblAdvDeposite;
    }

    public void setDblAdvDeposite(double dblAdvDeposite)
    {
        this.dblAdvDeposite = dblAdvDeposite;
    }

    public Double getDblTotalAmount()
    {
        return dblTotalAmount;
    }

    public void setDblTotalAmount(Double dblTotalAmount)
    {
        this.dblTotalAmount = dblTotalAmount;
    }

    public String getDteVoidedDate()
    {
        return dteVoidedDate;
    }

    public void setDteVoidedDate(String dteVoidedDate)
    {
        this.dteVoidedDate = dteVoidedDate;
    }

    public String getStrReasonCode()
    {
        return strReasonCode;
    }

    public void setStrReasonCode(String strReasonCode)
    {
        this.strReasonCode = strReasonCode;
    }

    public String getStrRemarks()
    {
        return strRemarks;
    }

    public void setStrRemarks(String strRemarks)
    {
        this.strRemarks = strRemarks;
    }
    
    
    
    

}
