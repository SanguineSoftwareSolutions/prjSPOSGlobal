/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.POSGlobal.controller;

public class clsMakeKotItemDtl implements Comparable<Object>
{
    private String  sequenceNo;
    private String KOTNo;//1
    private String tableNo;//2
    private String waiterNo;//3
    private String itemName;//4
    private String itemCode;//5
    private double qty;//6
    private double amt;//7
    private int paxNo;//8
    private String printYN;//9
    private String tdhComboItemYN;//10
    private boolean isModifier;//11
    private String modifierCode;//12
    private String tdh_ComboItemCode;//13
    private String modifierGroupCode;
    private String strNCKotYN;
    private double itemRate;
    private String strDefaultModifierDeselectedYN;

    public clsMakeKotItemDtl()
    {
    }
    
    
    public clsMakeKotItemDtl(String sequenceNo,String KOTNo,String tableNo,String waiterNo,String itemName,String itemCode
            ,double qty,double amt,int paxNo,String printYN,String tdhComboItemYN,boolean isModifier,String modifierCode
            ,String tdh_ComboItemCode,String modifierGroupCode,String strNCKotYN,double itemRate){
        this.sequenceNo=sequenceNo;
        this.KOTNo = KOTNo;
        this.tableNo = tableNo;
        this.waiterNo = waiterNo;
        this.itemName = itemName;
        this.itemCode = itemCode;
        this.qty = qty;
        this.amt = amt;
        this.paxNo = paxNo;
        this.printYN = printYN;
        this.tdhComboItemYN = tdhComboItemYN;
        this.isModifier = isModifier;
        this.modifierCode = modifierCode;
        this.tdh_ComboItemCode = tdh_ComboItemCode;
        this.modifierGroupCode=modifierGroupCode;
        this.strNCKotYN=strNCKotYN;
        this.itemRate=itemRate;
        this.strDefaultModifierDeselectedYN="N";
    }

    public double getItemRate()
    {
        return itemRate;
    }

    public void setItemRate(double itemRate)
    {
        this.itemRate = itemRate;
    }
    
    public String getStrNCKotYN() {
        return strNCKotYN;
    }

    public void setStrNCKotYN(String strNCKotYN) {
        this.strNCKotYN = strNCKotYN;
    }

    /**
     * @return the KOTNo
     */
    public String getKOTNo() {
        return KOTNo;
    }

    /**
     * @param KOTNo the KOTNo to set
     */
    public void setKOTNo(String KOTNo) {
        this.KOTNo = KOTNo;
    }

    /**
     * @return the tableNo
     */
    public String getTableNo() {
        return tableNo;
    }

    /**
     * @param tableNo the tableNo to set
     */
    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    /**
     * @return the waiterNo
     */
    public String getWaiterNo() {
        return waiterNo;
    }

    /**
     * @param waiterNo the waiterNo to set
     */
    public void setWaiterNo(String waiterNo) {
        this.waiterNo = waiterNo;
    }

    /**
     * @return the itemName
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * @param itemName the itemName to set
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * @return the itemCode
     */
    public String getItemCode() {
        return itemCode;
    }

    /**
     * @param itemCode the itemCode to set
     */
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    /**
     * @return the qty
     */
    public double getQty() {
        return qty;
    }

    /**
     * @param qty the qty to set
     */
    public void setQty(double qty) {
        this.qty = qty;
    }

    /**
     * @return the amt
     */
    public double getAmt() {
        return amt;
    }

    /**
     * @param amt the amt to set
     */
    public void setAmt(double amt) {
        this.amt = amt;
    }

    /**
     * @return the paxNo
     */
    public int getPaxNo() {
        return paxNo;
    }

    /**
     * @param paxNo the paxNo to set
     */
    public void setPaxNo(int paxNo) {
        this.paxNo = paxNo;
    }

    /**
     * @return the printYN
     */
    public String getPrintYN() {
        return printYN;
    }

    /**
     * @param printYN the printYN to set
     */
    public void setPrintYN(String printYN) {
        this.printYN = printYN;
    }

    /**
     * @return the tdhComboItemYN
     */
    public String getTdhComboItemYN() {
        return tdhComboItemYN;
    }

    /**
     * @param tdhComboItemYN the tdhComboItemYN to set
     */
    public void setTdhComboItemYN(String tdhComboItemYN) {
        this.tdhComboItemYN = tdhComboItemYN;
    }

    /**
     * @return the isModifier
     */
    public boolean isIsModifier() {
        return isModifier;
    }

    /**
     * @param isModifier the isModifier to set
     */
    public void setIsModifier(boolean isModifier) {
        this.isModifier = isModifier;
    }

    /**
     * @return the modifierCode
     */
    public String getModifierCode() {
        return modifierCode;
    }

    /**
     * @param modifierCode the modifierCode to set
     */
    public void setModifierCode(String modifierCode) {
        this.modifierCode = modifierCode;
    }

    /**
     * @return the tdh_ComboItemCode
     */
    public String getTdh_ComboItemCode() {
        return tdh_ComboItemCode;
    }

    /**
     * @return the modifierGroupCode
     */
    public String getModifierGroupCode() {
        return modifierGroupCode;
    }

    /**
     * @return the sequenceNo
     */
    public String getSequenceNo() {
        return sequenceNo;
    }

    public String getStrDefaultModifierDeselectedYN()
    {
        return strDefaultModifierDeselectedYN;
    }

    public void setStrDefaultModifierDeselectedYN(String strDefaultModifierDeselectedYN)
    {
        this.strDefaultModifierDeselectedYN = strDefaultModifierDeselectedYN;
    }


    
    
    
    
    
    
    @Override
    public int compareTo(Object o) 
    {
        double d1=Double.parseDouble(sequenceNo);
        double d2=Double.parseDouble(((clsMakeKotItemDtl)o).sequenceNo);
        
        if(d1==d2)
        {
            return 0;
        }
        else if(d1 > d2)
        {
            return 1;
        }
        else 
        {
            return -1;
        }
    }

       
}
