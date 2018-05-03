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
public class clsBillSeriesBillDtl
{

    public clsBillSeriesBillDtl()
    {
    }
    
    private String strHdBillNo;
    private String strDtlBillNos;
    private double dblGrandTotal;
    private String strBillSeries;
    boolean flgHomeDelPrint;

    public String getStrHdBillNo()
    {
        return strHdBillNo;
    }

    public void setStrHdBillNo(String strHdBillNo)
    {
        this.strHdBillNo = strHdBillNo;
    }

    public String getStrDtlBillNos()
    {
        return strDtlBillNos;
    }

    public void setStrDtlBillNos(String strDtlBillNos)
    {
        this.strDtlBillNos = strDtlBillNos;
    }

    public double getDblGrandTotal()
    {
        return dblGrandTotal;
    }

    public void setDblGrandTotal(double dblGrandTotal)
    {
        this.dblGrandTotal = dblGrandTotal;
    }

    public String getStrBillSeries()
    {
        return strBillSeries;
    }

    public void setStrBillSeries(String strBillSeries)
    {
        this.strBillSeries = strBillSeries;
    }

    public boolean isFlgHomeDelPrint()
    {
        return flgHomeDelPrint;
    }

    public void setFlgHomeDelPrint(boolean flgHomeDelPrint)
    {
        this.flgHomeDelPrint = flgHomeDelPrint;
    }
    
    
    
    
}
