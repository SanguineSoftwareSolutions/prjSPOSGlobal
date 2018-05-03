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
public class clsBillItemDtl
{

    private String billNo;

    private String billDateTime;

    private String itemCode;

    private String itemName;

    private double quantity;

    private double rate;

    private double amount;

    private double discountPercentage;

    private double discountAmount;

    private double taxAmount;

    private String posName;

    private double subTotal;
    
    private String menuCode;
    
    private String menuName;
    
    private String groupCode;
    
    private String groupName;
    
    private String subGroupCode;
    
    private String subGroupName;
    
    private boolean isModifier;
    
    private double complItemQty;

    //for KOT and direct biller
    public clsBillItemDtl()
    {
        //default
    }

    //for item wise sales flash
    public clsBillItemDtl(String billDateTime, String itemCode, String itemName, double quantity, double amount, double discountAmount, String posName, double subTotal)
    {
        this.billDateTime = billDateTime;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.quantity = quantity;
        this.amount = amount;
        this.discountAmount = discountAmount;
        this.posName = posName;
        this.subTotal = subTotal;
    }

    //for menuHead sales flash
    public clsBillItemDtl(double quantity, double amount, double discountAmount, String posName, double subTotal, String menuCode, String menuName)
    {
        this.quantity = quantity;
        this.amount = amount;
        this.discountAmount = discountAmount;
        this.posName = posName;
        this.subTotal = subTotal;
        this.menuCode = menuCode;
        this.menuName = menuName;
    }
    
    
    
    

    public String getMenuCode()
    {
        return menuCode;
    }

    public void setMenuCode(String menuCode)
    {
        this.menuCode = menuCode;
    }

    public String getMenuName()
    {
        return menuName;
    }

    public void setMenuName(String menuName)
    {
        this.menuName = menuName;
    }

    
    public double getSubTotal()
    {
        return subTotal;
    }

    public void setSubTotal(double subTotal)
    {
        this.subTotal = subTotal;
    }

    public String getPosName()
    {
        return posName;
    }

    public void setPosName(String posName)
    {
        this.posName = posName;
    }

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

    public double getQuantity()
    {
        return quantity;
    }

    public void setQuantity(double quantity)
    {
        this.quantity = quantity;
    }

    public double getRate()
    {
        return rate;
    }

    public void setRate(double rate)
    {
        this.rate = rate;
    }

    public double getAmount()
    {
        return amount;
    }

    public void setAmount(double amount)
    {
        this.amount = amount;
    }

    public double getDiscountPercentage()
    {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage)
    {
        this.discountPercentage = discountPercentage;
    }

    public double getDiscountAmount()
    {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount)
    {
        this.discountAmount = discountAmount;
    }

    public double getTaxAmount()
    {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount)
    {
        this.taxAmount = taxAmount;
    }

    public String getBillNo()
    {
        return billNo;
    }

    public void setBillNo(String billNo)
    {
        this.billNo = billNo;
    }

    public String getBillDateTime()
    {
        return billDateTime;
    }

    public void setBillDateTime(String billDateTime)
    {
        this.billDateTime = billDateTime;
    }

    public String getGroupCode()
    {
        return groupCode;
    }

    public void setGroupCode(String groupCode)
    {
        this.groupCode = groupCode;
    }

    public String getGroupName()
    {
        return groupName;
    }

    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    public String getSubGroupCode()
    {
        return subGroupCode;
    }

    public void setSubGroupCode(String subGroupCode)
    {
        this.subGroupCode = subGroupCode;
    }

    public String getSubGroupName()
    {
        return subGroupName;
    }

    public void setSubGroupName(String subGroupName)
    {
        this.subGroupName = subGroupName;
    }

    public boolean isIsModifier()
    {
        return isModifier;
    }

    public void setIsModifier(boolean isModifier)
    {
        this.isModifier = isModifier;
    }

    public double getComplItemQty() {
        return complItemQty;
    }

    public void setComplItemQty(double complItemQty) {
        this.complItemQty = complItemQty;
    }
    
    

}
