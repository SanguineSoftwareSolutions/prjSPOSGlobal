/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSGlobal.controller;

import com.POSGlobal.controller.*;
import java.util.Date;

/**
 *
 * @author sanguine02
 */
public class clsItemPriceDtl {

    private final String strItemCode;//1
    private final String strItemName;//2
    private final double strPriceMonday;//3
    private final double strPriceTuesday;//4
    private final double strPriceWednesday;//5
    private final double strPriceThursday;//6
    private final double strPriceFriday;//7
    private final double strPriceSaturday;//8
    private final double strPriceSunday;//9
    private final String tmeTimeFrom;//10
    private final String strAMPMFrom;//11
    private final String tmeTimeTo;//11
    private final String strAMPMTo;//12
    private final String strCostCenterCode;//13
    private final String strTextColor;//14
    private final String strHourlyPricing;//15
    private final String strSubMenuHeadCode;//16
    private final String strMenuHeadCode;//17
    private final String dteFromDate;
    private final String dteToDate;
    private final String strStockInEnable;
    private final double dblPurchaseRate;
    

   /**
    * 
    * @param strItemCode
    * @param strItemName
    * @param strPriceMonday
    * @param strPriceTuesday
    * @param strPriceWednesday
    * @param strPriceThursday
    * @param strPriceFriday
    * @param strPriceSaturday
    * @param strPriceSunday
    * @param tmeTimeFrom
    * @param strAMPMFrom
    * @param tmeTimeTo
    * @param strAMPMTo
    * @param strCostCenterCode
    * @param strTextColor
    * @param strHourlyPricing
    * @param strSubMenuHeadCode
    * @param dteFromDate
    * @param dteToDate 
    */
    public clsItemPriceDtl(String strItemCode, String strItemName, double strPriceMonday,
            double strPriceTuesday, double strPriceWednesday, double strPriceThursday, double strPriceFriday, double strPriceSaturday, double strPriceSunday,String tmeTimeFrom, String strAMPMFrom, String tmeTimeTo, String strAMPMTo, String strCostCenterCode,
            String strTextColor, String strHourlyPricing, String strSubMenuHeadCode,String dteFromDate,String dteToDate
            ,String strStockInEnable,double dblPurchaseRate,String menuHeadCode) 
    {
        this.strItemCode = strItemCode;
        this.strItemName = strItemName;
        this.strPriceMonday = strPriceMonday;
        this.strPriceTuesday = strPriceTuesday;
        this.strPriceWednesday = strPriceWednesday;
        this.strPriceThursday = strPriceThursday;
        this.strPriceFriday = strPriceFriday;
        this.strPriceSaturday = strPriceSaturday;
        this.strPriceSunday = strPriceSunday;
        this.tmeTimeFrom=tmeTimeFrom;
        this.strAMPMFrom = strAMPMFrom;
        this.tmeTimeTo = tmeTimeTo;
        this.strAMPMTo = strAMPMTo;
        this.strCostCenterCode = strCostCenterCode;
        this.strTextColor = strTextColor;
        this.strHourlyPricing = strHourlyPricing;
        this.strSubMenuHeadCode = strSubMenuHeadCode;
        this.dteFromDate=dteFromDate;
        this.dteToDate=dteToDate;
        this.strStockInEnable=strStockInEnable;
        this.dblPurchaseRate=dblPurchaseRate;
        this.strMenuHeadCode=menuHeadCode;

    }

    /**
     * @return the strItemCode
     */
    public String getStrItemCode() {
        return strItemCode;
    }

    /**
     * @return the strItemName
     */
    public String getStrItemName() {
        return strItemName;
    }

    /**
     * @return the strPriceMonday
     */
    public double getStrPriceMonday() {
        return strPriceMonday;
    }

    /**
     * @return the strPriceTuesday
     */
    public double getStrPriceTuesday() {
        return strPriceTuesday;
    }

    /**
     * @return the strPriceWednesday
     */
    public double getStrPriceWednesday() {
        return strPriceWednesday;
    }

    /**
     * @return the strPriceThursday
     */
    public double getStrPriceThursday() {
        return strPriceThursday;
    }

    /**
     * @return the strPriceFriday
     */
    public double getStrPriceFriday() {
        return strPriceFriday;
    }

    /**
     * @return the strPriceSaturday
     */
    public double getStrPriceSaturday() {
        return strPriceSaturday;
    }

    /**
     * @return the strPriceSunday
     */
    public double getStrPriceSunday() {
        return strPriceSunday;
    }

    /**
     * @return the strAMPMFrom
     */
    public String getStrAMPMFrom() {
        return strAMPMFrom;
    }

    /**
     * @return the tmeTimeTo
     */
    public String getTmeTimeTo() {
        return tmeTimeTo;
    }

    /**
     * @return the strAMPMTo
     */
    public String getStrAMPMTo() {
        return strAMPMTo;
    }

    /**
     * @return the strCostCenterCode
     */
    public String getStrCostCenterCode() {
        return strCostCenterCode;
    }

    /**
     * @return the strTextColor
     */
    public String getStrTextColor() {
        return strTextColor;
    }

    /**
     * @return the strHourlyPricing
     */
    public String getStrHourlyPricing() {
        return strHourlyPricing;
    }

    /**
     * @return the strSubMenuHeadCode
     */
    public String getStrSubMenuHeadCode() {
        return strSubMenuHeadCode;
    }

    /**
     * @return the tmeTimeFrom
     */
    public String getTmeTimeFrom() {
        return tmeTimeFrom;
    }

    /**
     * @return the dteFromDate
     */
    public String getDteFromDate() {
        return dteFromDate;
    }

    /**
     * @return the dteToDate
     */
    public String getDteToDate() {
        return dteToDate;
    }

    public String getStrStockInEnable()
    {
        return strStockInEnable;
    }

    public double getDblPurchaseRate() {
        return dblPurchaseRate;
    }

    public String getStrMenuHeadCode()
    {
        return strMenuHeadCode;
    }

    
    
}
