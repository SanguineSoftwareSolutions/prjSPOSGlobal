/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSGlobal.controller;

/**
 *
 * @author Sanguine
 */
public class clsRewards
{

    private String strRewardId;
    private String strRewardName;
    private String strRewardCategory;
    private String strRewardPoints;
    private String strPoints;
    private String strRewardPOSItemCode;
    private boolean itemOff;
    
    private String strRewardType;

    public String getStrRewardId()
    {
        return strRewardId;
    }

    public void setStrRewardId(String strRewardId)
    {
        this.strRewardId = strRewardId;
    }

    public String getStrRewardName()
    {
        return strRewardName;
    }

    public void setStrRewardName(String strRewardName)
    {
        this.strRewardName = strRewardName;
    }

    public String getStrRewardCategory()
    {
        return strRewardCategory;
    }

    public void setStrRewardCategory(String strRewardCategory)
    {
        this.strRewardCategory = strRewardCategory;
    }

    public String getStrRewardPoints()
    {
        return strRewardPoints;
    }

    public void setStrRewardPoints(String strRewardPoints)
    {
        this.strRewardPoints = strRewardPoints;
    }

    public String getStrRewardPOSItemCode()
    {
        return strRewardPOSItemCode;
    }

    public void setStrRewardPOSItemCode(String strRewardPOSItemCode)
    {
        this.strRewardPOSItemCode = strRewardPOSItemCode;
    }

    public String getStrRewardType()
    {
        return strRewardType;
    }

    public void setStrRewardType(String strRewardType)
    {
        this.strRewardType = strRewardType;
    }

    public String getStrPoints()
    {
        return strPoints;
    }

    public void setStrPoints(String strPoints)
    {
        this.strPoints = strPoints;
    }

    public boolean isItemOff()
    {
        return itemOff;
    }

    public void setItemOff(boolean itemOff)
    {
        this.itemOff = itemOff;
    }

   
    
    
    

    @Override
    public String toString()
    {
        return "clsRewards{" + "strRewardId=" + strRewardId + ", strRewardName=" + strRewardName + ", strRewardCategory=" + strRewardCategory + ", strRewardPoints=" + strRewardPoints + ",strPoints="+strPoints+", strRewardPOSItemCode=" + strRewardPOSItemCode + ", strRewardType=" + strRewardType +",itemOff="+itemOff+"}";
    }
    
    
    

}
