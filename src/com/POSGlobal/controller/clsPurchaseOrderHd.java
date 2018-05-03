/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.POSGlobal.controller;

import java.util.List;

/**
 *
 * @author prashant
 */
public class clsPurchaseOrderHd {
 
    private String POCode;
    
    private String supplierCode;
    
    private String PODate;
    
    private String deliveryDate;
    
    private double subTotal;
    
    private double taxAmt;
    
    private double grandTotal;
    
    private double extraCharges;
    
    private List<clsPurchaseOrderDtl> listPurchaseOrderDtl;

    public String getPOCode() {
        return POCode;
    }

    public void setPOCode(String POCode) {
        this.POCode = POCode;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getPODate() {
        return PODate;
    }

    public void setPODate(String PODate) {
        this.PODate = PODate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getTaxAmt() {
        return taxAmt;
    }

    public void setTaxAmt(double taxAmt) {
        this.taxAmt = taxAmt;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public double getExtraCharges() {
        return extraCharges;
    }

    public void setExtraCharges(double extraCharges) {
        this.extraCharges = extraCharges;
    }

    public List<clsPurchaseOrderDtl> getListPurchaseOrderDtl() {
        return listPurchaseOrderDtl;
    }

    public void setListPurchaseOrderDtl(List<clsPurchaseOrderDtl> listPurchaseOrderDtl) {
        this.listPurchaseOrderDtl = listPurchaseOrderDtl;
    }
    
    
}
