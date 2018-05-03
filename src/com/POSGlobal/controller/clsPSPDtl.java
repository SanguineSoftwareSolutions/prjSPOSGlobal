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
public class clsPSPDtl 
{
    private String strPSPCode;
    private String strItemCode;
    private String strItemName;
    private double dblPhyStk;
    private double dblCompStk;
    private double dblVariance;
    private double dblVairanceAmt;
    private String strClientCode;

    public String getStrItemName() {
        return strItemName;
    }

    public void setStrItemName(String strItemName) {
        this.strItemName = strItemName;
    }
    private String strDataPostFlag;

    public String getStrPSPCode() {
        return strPSPCode;
    }

    public void setStrPSPCode(String strPSPCode) {
        this.strPSPCode = strPSPCode;
    }

    public String getStrItemCode() {
        return strItemCode;
    }

    public void setStrItemCode(String strItemCode) {
        this.strItemCode = strItemCode;
    }

    public double getDblPhyStk() {
        return dblPhyStk;
    }

    public void setDblPhyStk(double dblPhyStk) {
        this.dblPhyStk = dblPhyStk;
    }

    public double getDblCompStk() {
        return dblCompStk;
    }

    public void setDblCompStk(double dblCompStk) {
        this.dblCompStk = dblCompStk;
    }

    public double getDblVariance() {
        return dblVariance;
    }

    public void setDblVariance(double dblVariance) {
        this.dblVariance = dblVariance;
    }

    public double getDblVairanceAmt() {
        return dblVairanceAmt;
    }

    public void setDblVairanceAmt(double dblVairanceAmt) {
        this.dblVairanceAmt = dblVairanceAmt;
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
    
    
}
