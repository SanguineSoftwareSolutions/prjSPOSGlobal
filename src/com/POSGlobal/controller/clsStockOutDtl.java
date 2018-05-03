/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSGlobal.controller;

/**
 *
 * @author Prashant
 */
public class clsStockOutDtl 
{
    private String strItemName;

    public String getStrItemName() {
        return strItemName;
    }

    public void setStrItemName(String strItemName) {
        this.strItemName = strItemName;
    }
    private String strStkOutCode;
    private String strItemCode;
    private double dblQuantity;
    private double dblAmount;
    private double dblPurchaseRate;
    private String strClientCode;
    private String strDataPostFlag;
    private String strRemark;
    private String strDisplayQty;
    private String strParentCode;
    

    public String getStrStkOutCode() {
        return strStkOutCode;
    }

    public void setStrStkOutCode(String strStkOutCode) {
        this.strStkOutCode = strStkOutCode;
    }

    public String getStrItemCode() {
        return strItemCode;
    }

    public void setStrItemCode(String strItemCode) {
        this.strItemCode = strItemCode;
    }

    public double getDblQuantity() {
        return dblQuantity;
    }

    public void setDblQuantity(double dblQuantity) {
        this.dblQuantity = dblQuantity;
    }

    public double getDblAmount() {
        return dblAmount;
    }

    public void setDblAmount(double dblAmount) {
        this.dblAmount = dblAmount;
    }

    public double getDblPurchaseRate() {
        return dblPurchaseRate;
    }

    public void setDblPurchaseRate(double dblPurchaseRate) {
        this.dblPurchaseRate = dblPurchaseRate;
    }

    public String getStrClientCode() {
        return strClientCode;
    }

    public void setStrClientCode(String strClientCode) {
        this.strClientCode = strClientCode;
    }

    public String getStrDataPostFlag() {
        return strDataPostFlag;
    }

    public void setStrDataPostFlag(String strDataPostFlag) {
        this.strDataPostFlag = strDataPostFlag;
    }

    public String getStrRemark()
    {
        return strRemark;
    }

    public void setStrRemark(String strRemark)
    {
        this.strRemark = strRemark;
    }

    public String getStrDisplayQty()
    {
        return strDisplayQty;
    }

    public void setStrDisplayQty(String strDisplayQty)
    {
        this.strDisplayQty = strDisplayQty;
    }

    public String getStrParentCode()
    {
        return strParentCode;
    }

    public void setStrParentCode(String strParentCode)
    {
        this.strParentCode = strParentCode;
    }

    
}
