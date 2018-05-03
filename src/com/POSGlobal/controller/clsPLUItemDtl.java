/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSGlobal.controller;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class clsPLUItemDtl
{

    public static Map<String, Map<String, clsItemPriceDtl>> hmPLUItemDtl;
    private Map<String, clsItemPriceDtl> allItem;

    public clsPLUItemDtl()
    {
    }

    public void funPLUHashMap()
    {
        try
        {
            if (clsGlobalVarClass.gPriceFrom.equals("Menu Pricing"))
            {
                if ("N".equalsIgnoreCase(clsGlobalVarClass.gAreaWisePricing))
                {
                    funGetItemOfAll("No", "");
                }
                else
                {
                    funGetItemAreaWise("No", "");
                }
            }
            else
            {
                funFillItemsForRetail();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void funPLUHashMap(String counterCode)
    {
        try
        {
            if (clsGlobalVarClass.gPriceFrom.equals("Menu Pricing"))
            {
                if ("N".equalsIgnoreCase(clsGlobalVarClass.gAreaWisePricing))
                {
                    funGetItemOfAll("Yes", counterCode);
                }
                else
                {
                    funGetItemAreaWise("Yes", counterCode);
                }
            }
            else
            {
                funFillItemsForRetail();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funFillItemsForRetail()
    {
        try
        {
            hmPLUItemDtl = new HashMap<>();
            allItem = new HashMap<>();
            String sqlItemDtl = "SELECT strItemCode,strItemName,'',0,0,0,0,0,0,0,'','','','','','','','','' "
                    + ",strStockInEnable,dblPurchaseRate,'' "
                    + "FROM tblitemmaster "
                    + "ORDER BY strItemName ASC";
            ResultSet rsItemInfo = clsGlobalVarClass.dbMysql.executeResultSet(sqlItemDtl);

            while (rsItemInfo.next())
            {
                clsItemPriceDtl ob = new clsItemPriceDtl(rsItemInfo.getString(1), rsItemInfo.getString(2),
                        rsItemInfo.getDouble(4), rsItemInfo.getDouble(5), rsItemInfo.getDouble(6), rsItemInfo.getDouble(7),
                        rsItemInfo.getDouble(8), rsItemInfo.getDouble(9), rsItemInfo.getDouble(10), rsItemInfo.getString(11),
                        rsItemInfo.getString(12), rsItemInfo.getString(13), rsItemInfo.getString(14), rsItemInfo.getString(15),
                        rsItemInfo.getString(3), rsItemInfo.getString(16), rsItemInfo.getString(17), rsItemInfo.getString(18),
                        rsItemInfo.getString(19), rsItemInfo.getString(20), rsItemInfo.getDouble(21), rsItemInfo.getString(22));
                allItem.put(rsItemInfo.getString(2), ob);
            }
            rsItemInfo.close();
            hmPLUItemDtl.put(clsGlobalVarClass.gAreaCodeForTrans, allItem);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funGetItemOfAll(String counterWiseBilling, String counterCode)
    {
        try
        {
            hmPLUItemDtl = new HashMap<>();
            allItem = new HashMap<>();
            String posDateForPrice = clsGlobalVarClass.gPOSDateForTransaction.split(" ")[0];
            String sqlItemDtl = "";

            if (counterWiseBilling.equals("Yes"))
            {
                sqlItemDtl = "SELECT a.strItemCode,b.strItemName,a.strTextColor,a.strPriceMonday,a.strPriceTuesday,"
                        + " a.strPriceWednesday,a.strPriceThursday,a.strPriceFriday, "
                        + " a.strPriceSaturday,a.strPriceSunday,a.tmeTimeFrom,a.strAMPMFrom,a.tmeTimeTo,a.strAMPMTo,"
                        + " a.strCostCenterCode,a.strHourlyPricing,a.strSubMenuHeadCode,a.dteFromDate,a.dteToDate"
                        + " ,b.strStockInEnable,b.dblPurchaseRate,a.strMenuCode "
                        + " FROM tblmenuitempricingdtl a ,tblitemmaster b ,tblcounterdtl c "
                        + " WHERE a.strItemCode=b.strItemCode "
                        + " and a.strHourlyPricing='NO' "
                        + " and a.strMenuCode=c.strMenuCode and c.strCounterCode='" + counterCode + "' "
                        + " and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='All') "
                        + " and date(a.dteFromDate)<='" + posDateForPrice + "' and date(a.dteToDate)>='" + posDateForPrice + "' "
                        + " ORDER BY b.strItemName ASC";
            }
            else if (clsGlobalVarClass.gPlayZonePOS.equals("Y"))
            {
                sqlItemDtl = "select a.strItemCode,c.strItemName,'Black',b.dblMemberPriceMonday,b.dblMemberPriceTuesday"
                        + ",b.dblMemberPriceWednesday,b.dblMemberPriceThursday\n"
                        + ",b.dblMemberPriceFriday,b.dblMemberPriceSaturday,b.dblMemberPriceSunday,'','','','',a.strCostCenterCode,''"
                        + ",'',a.dteFromDate,a.dteToDate,'N',0,a.strMenuCode "
                        + "from tblplayzonepricinghd a,tblplayzonepricingdtl b,tblitemmaster c\n"
                        + "where a.strPlayZonePricingCode=b.strPlayZonePricingCode \n"
                        + "and a.strItemCode=c.strItemCode \n"
                        + "and date(a.dteFromDate)<='" + posDateForPrice + "' and date(a.dteToDate)>='" + posDateForPrice + "' "
                        + "and Time(CURRENT_TIME()) between b.dteFromTime and b.dteToTime "
                        + "and a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' "
                        + "ORDER BY b.dteFromTime";
            }
            else
            {
                sqlItemDtl = "SELECT a.strItemCode,b.strItemName,a.strTextColor,a.strPriceMonday,a.strPriceTuesday,"
                        + " a.strPriceWednesday,a.strPriceThursday,a.strPriceFriday, "
                        + " a.strPriceSaturday,a.strPriceSunday,a.tmeTimeFrom,a.strAMPMFrom,a.tmeTimeTo,a.strAMPMTo,"
                        + " a.strCostCenterCode,a.strHourlyPricing,a.strSubMenuHeadCode,a.dteFromDate,a.dteToDate"
                        + " ,b.strStockInEnable,b.dblPurchaseRate,a.strMenuCode  "
                        + " FROM tblmenuitempricingdtl a ,tblitemmaster b "
                        + " WHERE  a.strItemCode=b.strItemCode "
                        + " and a.strHourlyPricing='NO' "
                        + " and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='All') "
                        + " and date(a.dteFromDate)<='" + posDateForPrice + "' and date(a.dteToDate)>='" + posDateForPrice + "' "
                        + " ORDER BY b.strItemName ASC";
            }

            ResultSet rsItemInfo = clsGlobalVarClass.dbMysql.executeResultSet(sqlItemDtl);

            while (rsItemInfo.next())
            {
                clsItemPriceDtl ob = new clsItemPriceDtl(rsItemInfo.getString(1), rsItemInfo.getString(2),
                        rsItemInfo.getDouble(4), rsItemInfo.getDouble(5), rsItemInfo.getDouble(6), rsItemInfo.getDouble(7),
                        rsItemInfo.getDouble(8), rsItemInfo.getDouble(9), rsItemInfo.getDouble(10), rsItemInfo.getString(11),
                        rsItemInfo.getString(12), rsItemInfo.getString(13), rsItemInfo.getString(14), rsItemInfo.getString(15),
                        rsItemInfo.getString(3), rsItemInfo.getString(16), rsItemInfo.getString(17), rsItemInfo.getString(18),
                        rsItemInfo.getString(19), rsItemInfo.getString(20), rsItemInfo.getDouble(21), rsItemInfo.getString(22));
                allItem.put(rsItemInfo.getString(2), ob);
            }
            rsItemInfo.close();
            hmPLUItemDtl.put(clsGlobalVarClass.gAreaCodeForTrans, allItem);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funGetItemAreaWise(String counterWiseBilling, String counterCode)
    {
        try
        {

            String posDateForPrice = clsGlobalVarClass.gPOSDateForTransaction.split(" ")[0];
            String sql_Area = "select strAreaCode from tblareamaster";
            ResultSet rsAreaInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql_Area);
            List<String> listAreaCode = new ArrayList<>();
            while (rsAreaInfo.next())
            {
                listAreaCode.add(rsAreaInfo.getString(1));
            }
            rsAreaInfo.close();
            if (listAreaCode.size() > 0)
            {
                hmPLUItemDtl = new HashMap<>();
                for (String areaCode : listAreaCode)
                {
                    allItem = new HashMap<>();
                    String sqlItemDtl = "";
                    if (counterWiseBilling.equals("Yes"))
                    {
                        sqlItemDtl = "SELECT a.strItemCode,b.strItemName,a.strTextColor,a.strPriceMonday,a.strPriceTuesday,"
                                + " a.strPriceWednesday,a.strPriceThursday,a.strPriceFriday, "
                                + " a.strPriceSaturday,a.strPriceSunday,a.tmeTimeFrom,a.strAMPMFrom,a.tmeTimeTo,a.strAMPMTo,"
                                + " a.strCostCenterCode,a.strHourlyPricing,a.strSubMenuHeadCode,a.dteFromDate,a.dteToDate"
                                + " ,b.strStockInEnable,b.dblPurchaseRate,a.strMenuCode "
                                + " FROM tblmenuitempricingdtl a ,tblitemmaster b ,tblcounterdtl c "
                                + " WHERE  a.strAreaCode='" + areaCode + "'   "
                                + " and a.strItemCode=b.strItemCode "
                                + " and a.strHourlyPricing='NO'  "
                                + " and a.strMenuCode=c.strMenuCode and c.strCounterCode='" + counterCode + "' "
                                + " and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='All')"
                                + " and date(a.dteFromDate)<='" + posDateForPrice + "' and date(a.dteToDate)>='" + posDateForPrice + "' "
                                + " ORDER BY b.strItemName ASC";
                    }
                    else if (clsGlobalVarClass.gPlayZonePOS.equals("Y"))
                    {
                        sqlItemDtl = "select a.strItemCode,c.strItemName,'Black',b.dblMemberPriceMonday,b.dblMemberPriceTuesday"
                                + ",b.dblMemberPriceWednesday,b.dblMemberPriceThursday\n"
                                + ",b.dblMemberPriceFriday,b.dblMemberPriceSaturday,b.dblMemberPriceSunday,'','','',''"
                                + ",a.strCostCenterCode,'','',a.dteFromDate,a.dteToDate,'N',0,a.strMenuCode "
                                + "from tblplayzonepricinghd a,tblplayzonepricingdtl b,tblitemmaster c\n"
                                + "where a.strPlayZonePricingCode=b.strPlayZonePricingCode \n"
                                + "and a.strItemCode=c.strItemCode \n"
                                + "and date(a.dteFromDate)<='" + posDateForPrice + "' and date(a.dteToDate)>='" + posDateForPrice + "' "
                                + "and Time(CURRENT_TIME()) between b.dteFromTime and b.dteToTime "
                                + "and a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' and a.strAreaCode='" + areaCode + "'"
                                + "ORDER BY b.dteFromTime";
                    }
                    else
                    {
                        sqlItemDtl = "SELECT a.strItemCode,b.strItemName,a.strTextColor,a.strPriceMonday,a.strPriceTuesday,"
                                + " a.strPriceWednesday,a.strPriceThursday,a.strPriceFriday, "
                                + " a.strPriceSaturday,a.strPriceSunday,a.tmeTimeFrom,a.strAMPMFrom,a.tmeTimeTo,a.strAMPMTo,"
                                + " a.strCostCenterCode,a.strHourlyPricing,a.strSubMenuHeadCode,a.dteFromDate,a.dteToDate"
                                + " ,b.strStockInEnable,b.dblPurchaseRate,a.strMenuCode "
                                + " FROM tblmenuitempricingdtl a ,tblitemmaster b "
                                + " WHERE  a.strAreaCode='" + areaCode + "'   "
                                + " and a.strItemCode=b.strItemCode "
                                + " and a.strHourlyPricing='NO'  "
                                + " and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='All')"
                                + " and date(a.dteFromDate)<='" + posDateForPrice + "' and date(a.dteToDate)>='" + posDateForPrice + "' "
                                + " ORDER BY b.strItemName ASC";
                    }

                    ResultSet rsItemInfo = clsGlobalVarClass.dbMysql.executeResultSet(sqlItemDtl);
                    while (rsItemInfo.next())
                    {
                        clsItemPriceDtl ob = new clsItemPriceDtl(rsItemInfo.getString(1), rsItemInfo.getString(2),
                                rsItemInfo.getDouble(4), rsItemInfo.getDouble(5), rsItemInfo.getDouble(6),
                                rsItemInfo.getDouble(7), rsItemInfo.getDouble(8), rsItemInfo.getDouble(9),
                                rsItemInfo.getDouble(10), rsItemInfo.getString(11), rsItemInfo.getString(12),
                                rsItemInfo.getString(13), rsItemInfo.getString(14), rsItemInfo.getString(15),
                                rsItemInfo.getString(3), rsItemInfo.getString(16), rsItemInfo.getString(17),
                                rsItemInfo.getString(18), rsItemInfo.getString(19), rsItemInfo.getString(20),
                                rsItemInfo.getDouble(21), rsItemInfo.getString(22));
                        allItem.put(rsItemInfo.getString(2), ob);
                    }
                    rsItemInfo.close();
                    hmPLUItemDtl.put(areaCode, allItem);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
