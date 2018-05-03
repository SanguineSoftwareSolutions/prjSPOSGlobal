/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.POSGlobal.controller;

public class clsGetPromotionItemDtl {
    
    private String itemCode;
    
    private double getPromoItemQty;
    
    private double totalItemQty;
    
    private double totalAmount;

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public double getGetPromoItemQty() {
        return getPromoItemQty;
    }

    public void setGetPromoItemQty(double getPromoItemQty) {
        this.getPromoItemQty = getPromoItemQty;
    }

    public double getTotalItemQty() {
        return totalItemQty;
    }

    public void setTotalItemQty(double totalItemQty) {
        this.totalItemQty = totalItemQty;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
