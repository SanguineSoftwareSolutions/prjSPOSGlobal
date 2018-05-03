/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSGlobal.controller;

public class clsBillDiscountDtl
{   
    private String remark;
    private String reason;
    private double discPer;
    private double discAmt;
    private double discOnAmt;
    private String billNo;
    private String POSCode;
    private String discOnType;
    private String discOnValue;
    private String userCreated;
    private String userEdited;
    private String dateCreated;
    private String dateEdited;
    private String clientCode;
    

    public clsBillDiscountDtl()
    {        
    }    

    public clsBillDiscountDtl(String remark, String reason, double discPer, double discAmt, double discOnAmt)
    {
        this.remark = remark;
        this.reason = reason;
        this.discAmt=discAmt;
        this.discPer=discPer;
        this.discOnAmt = discOnAmt;
    }
   
  

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public String getReason()
    {
        return reason;
    }

    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public double getDiscPer()
    {
        return discPer;
    }

    public void setDiscPer(double discPer)
    {
        this.discPer = discPer;
    }

    public double getDiscAmt()
    {
        return discAmt;
    }

    public void setDiscAmt(double discAmt)
    {
        this.discAmt = discAmt;
    }

    public double getDiscOnAmt()
    {
        return discOnAmt;
    }

    public void setDiscOnAmt(double discOnAmt)
    {
        this.discOnAmt = discOnAmt;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getPOSCode() {
        return POSCode;
    }

    public void setPOSCode(String POSCode) {
        this.POSCode = POSCode;
    }

    public String getDiscOnType() {
        return discOnType;
    }

    public void setDiscOnType(String discOnType) {
        this.discOnType = discOnType;
    }

    public String getDiscOnValue() {
        return discOnValue;
    }

    public void setDiscOnValue(String discOnValue) {
        this.discOnValue = discOnValue;
    }

    public String getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(String userCreated) {
        this.userCreated = userCreated;
    }

    public String getUserEdited() {
        return userEdited;
    }

    public void setUserEdited(String userEdited) {
        this.userEdited = userEdited;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateEdited() {
        return dateEdited;
    }

    public void setDateEdited(String dateEdited) {
        this.dateEdited = dateEdited;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }
    
    
}
