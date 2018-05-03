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
public class clsCommonBeanDtl
{
    private String posCode;
    private String posName;
    private String waiterNo;
    private String waiterShortName;
    private String waiterFullName;
    private double qty;
    private double saleAmount;
    private double subTotal;
    private String dbCode;
    private String dbName;
    private double delCharges;
    private String costCenterCode;
    private String costCenterName;
    private double discAmount;
    private String tableNo;
    private String tableName;
    private String startHrs;
    private String endHrs;
    private double noOfBills;
    private String areaCode;
    private String areaName;
    private double taxAmount;
    private String date;
    private String modiCode;
    private String modiName;
    private String userCode;
    private String userName;
    private String payMode;
    private String monthCode;
    private String monthName;
    private String year;
    
    
    public clsCommonBeanDtl()
    {
    }
    
    //for waiterwise sales
    public clsCommonBeanDtl(String posCode, String posName, String waiterNo, String waiterShortName, String waiterFullName, double saleAmount,double noOfBIlls)
    {
        this.posCode = posCode;
        this.posName = posName;
        this.waiterNo = waiterNo;
        this.waiterShortName = waiterShortName;
        this.waiterFullName = waiterFullName;
        this.saleAmount = saleAmount;
        this.noOfBills=noOfBIlls;
        
    }

    //for delivery boy sales
    public clsCommonBeanDtl(String posCode, String posName, double saleAmount, String dbCode, String dbName, double delCharges)
    {
        this.posCode = posCode;
        this.posName = posName;
        this.saleAmount = saleAmount;
        this.dbCode = dbCode;
        this.dbName = dbName;
        this.delCharges = delCharges;
    }
    
    //for cost center wise sales
    public clsCommonBeanDtl(String posCode, String posName, double qty, double saleAmount, double subTotal, String costCenterCode, String costCenterName, double discAmount)
    {
        this.posCode = posCode;
        this.costCenterName=costCenterName;
        this.posName = posName;
        this.qty = qty;
        this.saleAmount = saleAmount;
        this.subTotal = subTotal;
        this.costCenterCode = costCenterCode;
        this.discAmount = discAmount;
    }
    
    //for table wise sales
    public clsCommonBeanDtl(String posCode, String posName, double saleAmount, String tableNo,double noOfBills, String tableName)
    {
        this.posCode = posCode;
        this.posName = posName;
        this.saleAmount = saleAmount;
        this.tableNo = tableNo;
        this.tableName = tableName;
        this.noOfBills= noOfBills;
    }
    //for hourly wise sales

    public clsCommonBeanDtl(double saleAmount, String startHrs, String endHrs, double noOfBills)
    {
        this.saleAmount = saleAmount;
        this.startHrs = startHrs;
        this.endHrs = endHrs;
        this.noOfBills = noOfBills;
    }

    //for area wise sales
    public clsCommonBeanDtl(String posCode, String posName, String areaCode, String areaName,  double saleAmount)
    {
        this.posCode = posCode;
        this.posName = posName;
        this.saleAmount = saleAmount;
        this.areaCode = areaCode;
        this.areaName = areaName;
    }
    //for day wise sales
    public clsCommonBeanDtl(double saleAmount, double subTotal, double discAmount, double noOfBills, double taxAmount, String date)
    {
        this.saleAmount = saleAmount;
        this.subTotal = subTotal;
        this.discAmount = discAmount;
        this.noOfBills = noOfBills;
        this.taxAmount = taxAmount;
        this.date = date;
    }
    
    //for modifier sales
    public clsCommonBeanDtl(String posCode, String posName, double qty, double saleAmount, String modiCode, String modiName)
    {
        this.posCode = posCode;
        this.posName = posName;
        this.qty = qty;
        this.saleAmount = saleAmount;
        this.modiCode = modiCode;
        this.modiName = modiName;
    }
    //for operator wise sales
    public clsCommonBeanDtl(String posCode, String posName, double saleAmount, double discAmount, String userCode, String userName, String payMode)
    {
        this.posCode = posCode;
        this.posName = posName;
        this.saleAmount = saleAmount;
        this.discAmount = discAmount;
        this.userCode = userCode;
        this.userName = userName;
        this.payMode = payMode;
    }
    
    //for month wise sales
    public clsCommonBeanDtl(double saleAmount,String posCode, String posName,String monthCode, String monthName,String year)
    {
        this.posCode = posCode;
        this.posName = posName;
        this.saleAmount = saleAmount;
        this.monthCode = monthCode;
        this.monthName = monthName;
        this.year=year;
    }

    public String getYear()
    {
        return year;
    }

    public void setYear(String year)
    {
        this.year = year;
    }       
    public String getMonthCode()
    {
        return monthCode;
    }

    public void setMonthCode(String monthCode)
    {
        this.monthCode = monthCode;
    }

    public String getMonthName()
    {
        return monthName;
    }

    public void setMonthName(String monthName)
    {
        this.monthName = monthName;
    }       
    public String getUserCode()
    {
        return userCode;
    }

    public void setUserCode(String userCode)
    {
        this.userCode = userCode;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getPayMode()
    {
        return payMode;
    }

    public void setPayMode(String payMode)
    {
        this.payMode = payMode;
    }
        
    public String getModiCode()
    {
        return modiCode;
    }

    public void setModiCode(String modiCode)
    {
        this.modiCode = modiCode;
    }

    public String getModiName()
    {
        return modiName;
    }

    public void setModiName(String modiName)
    {
        this.modiName = modiName;
    }        
    
    public double getTaxAmount()
    {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount)
    {
        this.taxAmount = taxAmount;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }            
    
    public String getAreaCode()
    {
        return areaCode;
    }

    public void setAreaCode(String areaCode)
    {
        this.areaCode = areaCode;
    }

    public String getAreaName()
    {
        return areaName;
    }

    public void setAreaName(String areaName)
    {
        this.areaName = areaName;
    }
            
    public String getStartHrs()
    {
        return startHrs;
    }

    public void setStartHrs(String startHrs)
    {
        this.startHrs = startHrs;
    }

    public String getEndHrs()
    {
        return endHrs;
    }

    public void setEndHrs(String endHrs)
    {
        this.endHrs = endHrs;
    }

    public double getNoOfBills()
    {
        return noOfBills;
    }

    public void setNoOfBills(double noOfBills)
    {
        this.noOfBills = noOfBills;
    }
       
    public String getTableNo()
    {
        return tableNo;
    }

    public void setTableNo(String tableNo)
    {
        this.tableNo = tableNo;
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }
        
    public double getQty()
    {
        return qty;
    }

    public void setQty(double qty)
    {
        this.qty = qty;
    }            
    public double getDiscAmount()
    {
        return discAmount;
    }

    public void setDiscAmount(double discAmount)
    {
        this.discAmount = discAmount;
    }
    
    public double getSubTotal()
    {
        return subTotal;
    }

    public void setSubTotal(double subTotal)
    {
        this.subTotal = subTotal;
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
    
    public String getDbCode()
    {
        return dbCode;
    }

    public void setDbCode(String dbCode)
    {
        this.dbCode = dbCode;
    }

    public String getDbName()
    {
        return dbName;
    }

    public void setDbName(String dbName)
    {
        this.dbName = dbName;
    }

    public double getDelCharges()
    {
        return delCharges;
    }

    public void setDelCharges(double delCharges)
    {
        this.delCharges = delCharges;
    }

    public String getPosCode()
    {
        return posCode;
    }

    public void setPosCode(String posCode)
    {
        this.posCode = posCode;
    }

    public String getPosName()
    {
        return posName;
    }

    public void setPosName(String posName)
    {
        this.posName = posName;
    }

    public String getWaiterNo()
    {
        return waiterNo;
    }

    public void setWaiterNo(String waiterNo)
    {
        this.waiterNo = waiterNo;
    }

    public String getWaiterShortName()
    {
        return waiterShortName;
    }

    public void setWaiterShortName(String waiterShortName)
    {
        this.waiterShortName = waiterShortName;
    }

    public String getWaiterFullName()
    {
        return waiterFullName;
    }

    public void setWaiterFullName(String waiterFullName)
    {
        this.waiterFullName = waiterFullName;
    }

    public double getSaleAmount()
    {
        return saleAmount;
    }

    public void setSaleAmount(double saleAmount)
    {
        this.saleAmount = saleAmount;
    }
    
    
}
