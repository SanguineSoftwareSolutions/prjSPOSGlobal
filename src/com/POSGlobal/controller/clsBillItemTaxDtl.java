/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.POSGlobal.controller;

public class clsBillItemTaxDtl {
    
    private String strBillNo;
    
    private String strItemCode;
    
    private String strKOTNo;
    
    private double dblTaxAmt;

    public String getStrBillNo() {
        return strBillNo;
    }

    public void setStrBillNo(String strBillNo) {
        this.strBillNo = strBillNo;
    }

    public String getStrItemCode() {
        return strItemCode;
    }

    public void setStrItemCode(String strItemCode) {
        this.strItemCode = strItemCode;
    }

    public String getStrKOTNo() {
        return strKOTNo;
    }

    public void setStrKOTNo(String strKOTNo) {
        this.strKOTNo = strKOTNo;
    }

    public double getDblTaxAmt() {
        return dblTaxAmt;
    }

    public void setDblTaxAmt(double dblTaxAmt) {
        this.dblTaxAmt = dblTaxAmt;
    }
}
