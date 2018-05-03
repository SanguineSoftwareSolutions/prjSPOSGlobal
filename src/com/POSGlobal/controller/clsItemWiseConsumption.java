/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSGlobal.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

/**
 *
 * @author SanguineERP
 */
public class clsItemWiseConsumption
{

    private String itemCode;

    private String itemName;

    private String subGroupName;

    private String groupName;

    private String POSName;

    private double saleQty;

    private double complimentaryQty;

    private double ncQty;

    private double totalQty;

    private double saleAmt;

    private double subTotal;

    private double discAmt;

    private int seqNo;

    private String costCenterCode;

    private String costCenterName;
    
    private String menuHead;
    
    private double menuHeadPer;
    
    private double subTotalPer;
    
    private double complimentaryAmt;

    public double getComplimentaryAmt()
    {
        return complimentaryAmt;
    }

    public void setComplimentaryAmt(double complimentaryAmt)
    {
        this.complimentaryAmt = complimentaryAmt;
    }
    
    public double getItemRate()
    {
        return itemRate;
    }

    public void setItemRate(double itemRate)
    {
        this.itemRate = itemRate;
    }
    
    private double itemRate;

    public String getExternalCode()
    {
        return externalCode;
    }

    public void setExternalCode(String externalCode)
    {
        this.externalCode = externalCode;
    }

    public static Comparator<clsItemWiseConsumption> getComparatorItemConsumptionColumnDtl()
    {
        return comparatorItemConsumptionColumnDtl;
    }

    public static void setComparatorItemConsumptionColumnDtl(Comparator<clsItemWiseConsumption> comparatorItemConsumptionColumnDtl)
    {
        clsItemWiseConsumption.comparatorItemConsumptionColumnDtl = comparatorItemConsumptionColumnDtl;
    }

    private double promoQty;

    private String externalCode;

    public clsItemWiseConsumption()
    {
        this.promoQty = 0;
    }

    public int getSeqNo()
    {
        return seqNo;
    }

    public void setSeqNo(int seqNo)
    {
        this.seqNo = seqNo;
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

    public String getPOSName()
    {
        return POSName;
    }

    public void setPOSName(String POSName)
    {
        this.POSName = POSName;
    }

    public double getSaleQty()
    {
        return saleQty;
    }

    public void setSaleQty(double saleQty)
    {
        this.saleQty = saleQty;
    }

    public double getComplimentaryQty()
    {
        return complimentaryQty;
    }

    public void setComplimentaryQty(double complimentaryQty)
    {
        this.complimentaryQty = complimentaryQty;
    }

    public double getNcQty()
    {
        return ncQty;
    }

    public void setNcQty(double ncQty)
    {
        this.ncQty = ncQty;
    }

    public double getTotalQty()
    {
        return totalQty;
    }

    public void setTotalQty(double totalQty)
    {
        this.totalQty = totalQty;
    }

    public double getSaleAmt()
    {
        return saleAmt;
    }

    public void setSaleAmt(double saleAmt)
    {
        this.saleAmt = saleAmt;
    }

    public double getSubTotal()
    {
        return subTotal;
    }

    public void setSubTotal(double subTotal)
    {
        this.subTotal = subTotal;
    }

    public double getDiscAmt()
    {
        return discAmt;
    }

    public void setDiscAmt(double discAmt)
    {
        this.discAmt = discAmt;
    }

    public String getSubGroupName()
    {
        return subGroupName;
    }

    public void setSubGroupName(String subGroupName)
    {
        this.subGroupName = subGroupName;
    }

    public String getGroupName()
    {
        return groupName;
    }

    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    public double getPromoQty()
    {
        return promoQty;
    }

    public void setPromoQty(double promoQty)
    {
        this.promoQty = promoQty;
    }

    public String getCostCenterCode()
    {
        return costCenterCode;
    }

    public void setCostCenterCode(String costCenterCode)
    {
        this.costCenterCode = costCenterCode;
    }

    public String getCostCenterName()
    {
        return costCenterName;
    }
    
    public void setCostCenterName(String costCenterName)
    {
        this.costCenterName = costCenterName;
    }
     public String getMenuHead()
    {
        return menuHead;
    }

    public void setMenuHead(String menuHead)
    {
        this.menuHead = menuHead;
    }

    public double getMenuHeadPer()
    {
        return menuHeadPer;
    }

    public void setMenuHeadPer(double menuHeadPer)
    {
        this.menuHeadPer = menuHeadPer;
    }

    public double getSubTotalPer()
    {
        return subTotalPer;
    }

    public void setSubTotalPer(double subTotalPer)
    {
        this.subTotalPer = subTotalPer;
    }

     
    public static Comparator<clsItemWiseConsumption> comparatorItemConsumptionColumnDtl = new Comparator<clsItemWiseConsumption>()
    {
        public int compare(clsItemWiseConsumption s1, clsItemWiseConsumption s2)
        {
            int seqNo1 = s1.getSeqNo();
            int seqNo2 = s2.getSeqNo();

            if (seqNo1 == seqNo2)
            {
                return 0;
            }
            else if (seqNo1 > seqNo2)
            {
                return 1;
            }
            else
            {
                return -1;
            }

//            if(s1.itemCode.substring(0, 7).equalsIgnoreCase(s2.itemCode.substring(0, 7)))
//            {
//                return 0;
//            }
//            else
//            {
//                return 1;
//            }
        }
    };

    @Override
    public String toString()
    {
        return "clsItemWiseConsumption{" + "itemName=" + itemName + ", subGroupName=" + subGroupName + ", groupName=" + groupName + ", POSName=" + POSName + ", costCenterName=" + costCenterName + '}';
    }

   
    
    

}
