/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSGlobal.controller;

import java.util.Comparator;

/**
 *
 * @author Prashant
 */
public class clsPlaceOrderDtl 
{

    private String itemName;
    
    private String subGroupName;
   
    private String saleQty;
    
    private String groupName;
    
    private String orderCode;
    
    private String soDate;
    
    private String SOCode;

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
    
    private String orderDate;

    public String getItemName() {
        return itemName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getSoDate() {
        return soDate;
    }

    public void setSoDate(String soDate) {
        this.soDate = soDate;
    }

    public String getSOCode() {
        return SOCode;
    }

    public void setSOCode(String SOCode) {
        this.SOCode = SOCode;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getSubGroupName() {
        return subGroupName;
    }

    public void setSubGroupName(String subGroupName) {
        this.subGroupName = subGroupName;
    }

    public String getSaleQty() {
        return saleQty;
    }

    public void setSaleQty(String saleQty) {
        this.saleQty = saleQty;
    }

   
    
}
