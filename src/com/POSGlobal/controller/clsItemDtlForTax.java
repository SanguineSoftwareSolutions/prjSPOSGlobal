/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.POSGlobal.controller;

public class clsItemDtlForTax
{
    private String itemCode;
    
    private String itemName;
    
    private double amount;
    
    private double discAmt;
    
    private double discPer;
    
    private String modifierCode;
    
    private double modifierAmount;
    
    private double intQuantity;

    public String getItemCode()
    {
        return itemCode;
    }

    public void setItemCode(String itemCode)
    {
        this.itemCode = itemCode;
    }

    public String getItemName()
    {
        return itemName;
    }

    public void setItemName(String itemName)
    {
        this.itemName = itemName;
    }

    public double getAmount()
    {
        return amount;
    }

    public void setAmount(double amount)
    {
        this.amount = amount;
    }

    public double getDiscAmt()
    {
        return discAmt;
    }

    public void setDiscAmt(double discAmt)
    {
        this.discAmt = discAmt;
    }

    public double getDiscPer()
    {
        return discPer;
    }

    public void setDiscPer(double discPer)
    {
        this.discPer = discPer;
    }

    public String getModifierCode() {
        return modifierCode;
    }

    public void setModifierCode(String modifierCode) {
        this.modifierCode = modifierCode;
    }

    public double getModifierAmount() {
        return modifierAmount;
    }

    public void setModifierAmount(double modifierAmount) {
        this.modifierAmount = modifierAmount;
    }

    public double getIntQuantity() {
        return intQuantity;
    }

    public void setIntQuantity(double intQuantity) {
        this.intQuantity = intQuantity;
    }

    
    
    
    
}
