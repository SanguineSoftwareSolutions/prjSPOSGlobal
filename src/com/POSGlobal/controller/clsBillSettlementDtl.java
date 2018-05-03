/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.POSGlobal.controller;

/**
 *
 * @author Shree
 */
public class clsBillSettlementDtl {
    
    String strBillNo;
    
    String strSettlementCode;
    
    String strSettlementName;
    
    String strSettlementType;
    
    double dblSettlementAmt;
    
    double dblTipAmt;
    
    double dblPaidAmt;
    
    String strExpiryDate;
    
    String strCardName;
    
    String strRemark;
    
    String strClientCode;
    
    String strCustomerCode;
    
    double dblActualAmt;
    
    double dblRefundAmt;
    
    String strGiftVoucherCode;
    
    String strDataPostFlag;
    
    String posName;

    public clsBillSettlementDtl()
    {
    }

    
    
    public clsBillSettlementDtl(String settlementCode,String settlementName, double dblSettlementAmt, String posName, String settlementType)
    {
        this.strSettlementCode = settlementCode;
        this.strSettlementName=settlementName;
        this.dblSettlementAmt = dblSettlementAmt;
        this.posName = posName;
        this.strSettlementType=settlementType;
    }
    
    public clsBillSettlementDtl(String settlementCode,String settlementName, double dblSettlementAmt, String posName, String settlementType, double tipAmt)
    {
        this.strSettlementCode = settlementCode;
        this.strSettlementName=settlementName;
        this.dblSettlementAmt = dblSettlementAmt;
        this.posName = posName;
        this.strSettlementType=settlementType;
        this.dblTipAmt=tipAmt;
        
    }

    public String getStrSettlementName()
    {
        return strSettlementName;
    }

    public void setStrSettlementName(String strSettlementName)
    {
        this.strSettlementName = strSettlementName;
    }        
    
    public String getPosName()
    {
        return posName;
    }

    public void setPosName(String posName)
    {
        this.posName = posName;
    }               

    public String getStrBillNo() {
        return strBillNo;
    }

    public void setStrBillNo(String strBillNo) {
        this.strBillNo = strBillNo;
    }

    public String getStrSettlementCode() {
        return strSettlementCode;
    }

    public void setStrSettlementCode(String strSettlementCode) {
        this.strSettlementCode = strSettlementCode;
    }

    public double getDblSettlementAmt() {
        return dblSettlementAmt;
    }

    public void setDblSettlementAmt(double dblSettlementAmt) {
        this.dblSettlementAmt = dblSettlementAmt;
    }

    public double getDblPaidAmt() {
        return dblPaidAmt;
    }

    public void setDblPaidAmt(double dblPaidAmt) {
        this.dblPaidAmt = dblPaidAmt;
    }

    public String getStrExpiryDate() {
        return strExpiryDate;
    }

    public void setStrExpiryDate(String strExpiryDate) {
        this.strExpiryDate = strExpiryDate;
    }

    public String getStrCardName() {
        return strCardName;
    }

    public void setStrCardName(String strCardName) {
        this.strCardName = strCardName;
    }

    public String getStrRemark() {
        return strRemark;
    }

    public void setStrRemark(String strRemark) {
        this.strRemark = strRemark;
    }

    public String getStrClientCode() {
        return strClientCode;
    }

    public void setStrClientCode(String strClientCode) {
        this.strClientCode = strClientCode;
    }

    public String getStrCustomerCode() {
        return strCustomerCode;
    }

    public void setStrCustomerCode(String strCustomerCode) {
        this.strCustomerCode = strCustomerCode;
    }

    public double getDblActualAmt() {
        return dblActualAmt;
    }

    public void setDblActualAmt(double dblActualAmt) {
        this.dblActualAmt = dblActualAmt;
    }

    public double getDblRefundAmt() {
        return dblRefundAmt;
    }

    public void setDblRefundAmt(double dblRefundAmt) {
        this.dblRefundAmt = dblRefundAmt;
    }

    public String getStrGiftVoucherCode() {
        return strGiftVoucherCode;
    }

    public void setStrGiftVoucherCode(String strGiftVoucherCode) {
        this.strGiftVoucherCode = strGiftVoucherCode;
    }

    public String getStrDataPostFlag() {
        return strDataPostFlag;
    }

    public void setStrDataPostFlag(String strDataPostFlag) {
        this.strDataPostFlag = strDataPostFlag;
    }

    public String getStrSettlementType() {
        return strSettlementType;
    }

    public void setStrSettlementType(String strSettlementType) {
        this.strSettlementType = strSettlementType;
    }

    public double getDblTipAmt()
    {
        return dblTipAmt;
    }

    public void setDblTipAmt(double dblTipAmt)
    {
        this.dblTipAmt = dblTipAmt;
    }
    
    
    
    
}
