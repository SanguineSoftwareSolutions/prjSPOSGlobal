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
public class clsBillItemDtlBean
{

    private String strBillNo;
    private String dteBillDate;
    private String dteDateCreated;
    private String strPosName;
    private double dblSubTotal;
    private double dblGrandTotal;
    private String strItemCode;
    private String strItemName;
    private double dblQuantity;
    private double dblAmount;
    private double dblDiscountAmt;
    private double dblDiscountPer;
    private double dblBillDiscPer;
    private String strSettelmentMode;
    private double dblTaxAmt;
    private double dblSettlementAmt;
    private String strDiscValue;
    private String strDiscType;
    private String strDiscountRemark;
    private double dblTipAmount;
    private String strRemark;
    private String strReasonName;
    private String strSettelmentDesc;
    private String longMobileNo;
    private String strCustomerName;
    private String strCustomerCode;
    private String strPosCode;
    private String dteVoidedDate;
    private String strEntryTime;
    private String strVoidedTime;
    private double dblModifiedAmount;
    private double dblAmountTemp;
    private String strUserEdited;
    
    private String strMenuHeadName;
    private String strPopular;
    private String strExpire;
   
    private String strTaxIndicator;
    private String strSubGroupCode;
    private String strSubGroupName;
    private String strGroupName;
    
    private int noOfBills;
    
    private int intBillSeriesPaxNo;

    public String getStrDiscValue()
    {
        return strDiscValue;
    }

    public String getStrDiscType()
    {
        return strDiscType;
    }

    public void setStrDiscType(String strDiscType)
    {
        this.strDiscType = strDiscType;
    }

    public void setStrDiscValue(String strDiscValue)
    {
        this.strDiscValue = strDiscValue;
    }

    public clsBillItemDtlBean()
    {
    }

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

    public String getStrPosName()
    {
        return strPosName;
    }

    public void setStrPosName(String strPosName)
    {
        this.strPosName = strPosName;
    }

    public double getDblSubTotal()
    {
        return dblSubTotal;
    }

    public void setDblSubTotal(double dblSubTotal)
    {
        this.dblSubTotal = dblSubTotal;
    }

    public double getDblGrandTotal()
    {
        return dblGrandTotal;
    }

    public void setDblGrandTotal(double dblGrandTotal)
    {
        this.dblGrandTotal = dblGrandTotal;
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

    public double getDblQuantity()
    {
        return dblQuantity;
    }

    public void setDblQuantity(double dblQuantity)
    {
        this.dblQuantity = dblQuantity;
    }

    public double getDblAmount()
    {
        return dblAmount;
    }

    public void setDblAmount(double dblAmount)
    {
        this.dblAmount = dblAmount;
    }

    public double getDblDiscountAmt()
    {
        return dblDiscountAmt;
    }

    public void setDblDiscountAmt(double dblDiscountAmt)
    {
        this.dblDiscountAmt = dblDiscountAmt;
    }

    public double getDblDiscountPer()
    {
        return dblDiscountPer;
    }

    public void setDblDiscountPer(double dblDiscountPer)
    {
        this.dblDiscountPer = dblDiscountPer;
    }

    public double getDblBillDiscPer()
    {
        return dblBillDiscPer;
    }

    public void setDblBillDiscPer(double dblBillDiscPer)
    {
        this.dblBillDiscPer = dblBillDiscPer;
    }

    public String getStrSettelmentMode()
    {
        return strSettelmentMode;
    }

    public void setStrSettelmentMode(String strSettelmentMode)
    {
        this.strSettelmentMode = strSettelmentMode;
    }

    public double getDblTaxAmt()
    {
        return dblTaxAmt;
    }

    public void setDblTaxAmt(double dblTaxAmt)
    {
        this.dblTaxAmt = dblTaxAmt;
    }

    public double getDblSettlementAmt()
    {
        return dblSettlementAmt;
    }

    public void setDblSettlementAmt(double dblSettlementAmt)
    {
        this.dblSettlementAmt = dblSettlementAmt;
    }

    public String getStrTaxIndicator()
    {
        return strTaxIndicator;
    }

    public void setStrTaxIndicator(String strTaxIndicator)
    {
        this.strTaxIndicator = strTaxIndicator;
    }

    public String getStrSubGroupCode()
    {
        return strSubGroupCode;
    }

    public void setStrSubGroupCode(String strSubGroupCode)
    {
        this.strSubGroupCode = strSubGroupCode;
    }

    public String getStrSubGroupName()
    {
        return strSubGroupName;
    }

    public void setStrSubGroupName(String strSubGroupName)
    {
        this.strSubGroupName = strSubGroupName;
    }

    public String getStrGroupName()
    {
        return strGroupName;
    }

    public void setStrGroupName(String strGroupName)
    {
        this.strGroupName = strGroupName;
    }

    public String getStrDiscountRemark() {
        return strDiscountRemark;
    }

    public void setStrDiscountRemark(String strDiscountRemark) {
        this.strDiscountRemark = strDiscountRemark;
    }

    public double getDblTipAmount() {
        return dblTipAmount;
    }

    public void setDblTipAmount(double dblTipAmount) {
        this.dblTipAmount = dblTipAmount;
    }

    public String getStrRemark() {
        return strRemark;
    }

    public void setStrRemark(String strRemark) {
        this.strRemark = strRemark;
    }

    public String getStrReasonName() {
        return strReasonName;
    }

    public void setStrReasonName(String strReasonName) {
        this.strReasonName = strReasonName;
    }

    public String getDteDateCreated() {
        return dteDateCreated;
    }

    public void setDteDateCreated(String dteDateCreated) {
        this.dteDateCreated = dteDateCreated;
    }

    public String getStrSettelmentDesc() {
        return strSettelmentDesc;
    }

    public void setStrSettelmentDesc(String strSettelmentDesc) {
        this.strSettelmentDesc = strSettelmentDesc;
    }

    public String getLongMobileNo() {
        return longMobileNo;
    }

    public void setLongMobileNo(String longMobileNo) {
        this.longMobileNo = longMobileNo;
    }

    public String getStrCustomerName() {
        return strCustomerName;
    }

    public void setStrCustomerName(String strCustomerName) {
        this.strCustomerName = strCustomerName;
    }

    public String getStrCustomerCode() {
        return strCustomerCode;
    }

    public void setStrCustomerCode(String strCustomerCode) {
        this.strCustomerCode = strCustomerCode;
    }

    public int getIntBillSeriesPaxNo()
    {
        return intBillSeriesPaxNo;
    }

    public void setIntBillSeriesPaxNo(int intBillSeriesPaxNo)
    {
        this.intBillSeriesPaxNo = intBillSeriesPaxNo;
    }
     public String getStrPosCode() {
        return strPosCode;
    }

    public void setStrPosCode(String strPosCode) {
        this.strPosCode = strPosCode;
    }

    public String getDteVoidedDate() {
        return dteVoidedDate;
    }

    public void setDteVoidedDate(String dteVoidedDate) {
        this.dteVoidedDate = dteVoidedDate;
    }

    public String getStrEntryTime() {
        return strEntryTime;
    }

    public void setStrEntryTime(String strEntryTime) {
        this.strEntryTime = strEntryTime;
    }

    public String getStrVoidedTime() {
        return strVoidedTime;
    }

    public void setStrVoidedTime(String strVoidedTime) {
        this.strVoidedTime = strVoidedTime;
    }

    public double getDblModifiedAmount() {
        return dblModifiedAmount;
    }

    public void setDblModifiedAmount(double dblModifiedAmount) {
        this.dblModifiedAmount = dblModifiedAmount;
    }

    public double getDblAmountTemp() {
        return dblAmountTemp;
    }

    public void setDblAmountTemp(double dblAmountTemp) {
        this.dblAmountTemp = dblAmountTemp;
    }

    public String getStrUserEdited() {
        return strUserEdited;
    }

    public void setStrUserEdited(String strUserEdited) {
        this.strUserEdited = strUserEdited;
    }

    public int getNoOfBills()
    {
        return noOfBills;
    }

    public void setNoOfBills(int noOfBills)
    {
        this.noOfBills = noOfBills;
    }

    public String getStrMenuHeadName()
    {
        return strMenuHeadName;
    }

    public void setStrMenuHeadName(String strMenuHeadName)
    {
        this.strMenuHeadName = strMenuHeadName;
    }

    public String getStrPopular()
    {
        return strPopular;
    }

    public void setStrPopular(String strPopular)
    {
        this.strPopular = strPopular;
    }

    public String getStrExpire()
    {
        return strExpire;
    }

    public void setStrExpire(String strExpire)
    {
        this.strExpire = strExpire;
    }

    
    
}
