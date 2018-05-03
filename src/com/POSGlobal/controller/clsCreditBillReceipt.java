/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSGlobal.controller;

/**
 *
 * @author ajjim
 */
public class clsCreditBillReceipt
{

    private String strBillNo;
    private String dteBillDate;
    private String strPOSCode;
    private String strCustomerCode;
    private String strCustomerName;
    private double dblBillAmount;
    private double dblCreditAmount;
    private double dblPaidAmount;
    private double dblBalanceAmount;
    private String strClientCode;

    public String getStrBillNo()
    {
        return strBillNo;
    }

    public void setStrBillNo(String strBillNo)
    {
        this.strBillNo = strBillNo;
    }

    public String getDteBillDate()
    {
        return dteBillDate;
    }

    public void setDteBillDate(String dteBillDate)
    {
        this.dteBillDate = dteBillDate;
    }

    public String getStrPOSCode()
    {
        return strPOSCode;
    }

    public void setStrPOSCode(String strPOSCode)
    {
        this.strPOSCode = strPOSCode;
    }

    public String getStrCustomerCode()
    {
        return strCustomerCode;
    }

    public void setStrCustomerCode(String strCustomerCode)
    {
        this.strCustomerCode = strCustomerCode;
    }

    public String getStrCustomerName()
    {
        return strCustomerName;
    }

    public void setStrCustomerName(String strCustomerName)
    {
        this.strCustomerName = strCustomerName;
    }

    public double getDblBillAmount()
    {
        return dblBillAmount;
    }

    public void setDblBillAmount(double dblBillAmount)
    {
        this.dblBillAmount = dblBillAmount;
    }

    public double getDblCreditAmount()
    {
        return dblCreditAmount;
    }

    public void setDblCreditAmount(double dblCreditAmount)
    {
        this.dblCreditAmount = dblCreditAmount;
    }

    public double getDblPaidAmount()
    {
        return dblPaidAmount;
    }

    public void setDblPaidAmount(double dblPaidAmount)
    {
        this.dblPaidAmount = dblPaidAmount;
    }

    public double getDblBalanceAmount()
    {
        return dblBalanceAmount;
    }

    public void setDblBalanceAmount(double dblBalanceAmount)
    {
        this.dblBalanceAmount = dblBalanceAmount;
    }

    public String getStrClientCode()
    {
        return strClientCode;
    }

    public void setStrClientCode(String strClientCode)
    {
        this.strClientCode = strClientCode;
    }

}
