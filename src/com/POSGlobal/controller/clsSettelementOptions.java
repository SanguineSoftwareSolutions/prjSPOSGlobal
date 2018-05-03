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

public class clsSettelementOptions {
    public static HashMap<String, clsSettelementOptions> hmSettelementOptionsDtl;
    public static List<String> listSettelmentOptions;
    private String strSettelmentCode;
    private String strSettelmentDesc;
    private String strSettelmentType;
    private double dblConvertionRatio;
    private double dblSettlementAmt;
    private double dblPaidAmt;
    private String strExpiryDate;
    private String strCardName;
    private String strRemark;
    private double dblActualAmt;
    private double dblRefundAmt;
    private String strGiftVoucherCode;
    private String strBillPrintOnSettlement;
    private String strFolioNo;
    private String strRoomNo;
    private String strGuestCode;
    private String strMerchantCode="";
    private String strQRString="";
    private String strTransStatus="";
    private String strRefNo="";
    private String strTransId="";
    private String strTransDate="";
    
    
    public clsSettelementOptions(){
        
    }
    /**
     * 
     * @param strSettlementCode
     * @param dblSettlementAmt
     * @param dblPaidAmt
     * @param strExpiryDate
     * @param settleName
     * @param strCardName
     * @param strRemark
     * @param dblActualAmt
     * @param dblRefundAmt
     * @param strGiftVoucherCode 
     */
    public clsSettelementOptions(String strSettlementCode,double dblSettlementAmt,double dblPaidAmt
        ,String strExpiryDate,String settleName,String strCardName,String strRemark
        ,double dblActualAmt,double dblRefundAmt,String strGiftVoucherCode
        ,String strSettelmentDesc,String strSettelmentType,String merchantCode,String qrString,String transStatus,String refNo,String transId,String transDate){
        this.strSettelmentCode=strSettlementCode;
        this.dblSettlementAmt=dblSettlementAmt;
        this.dblPaidAmt=dblPaidAmt;
        this.strExpiryDate=strExpiryDate;
        this.strSettelmentDesc=settleName;
        this.strCardName=strCardName;
        this.strRemark=strRemark;
        this.dblActualAmt=dblActualAmt;
        this.dblRefundAmt=dblRefundAmt;
        this.strGiftVoucherCode=strGiftVoucherCode;
        this.strSettelmentDesc=strSettelmentDesc;
        this.strSettelmentType=strSettelmentType;
        this.strMerchantCode=merchantCode;
        this.strQRString=qrString;
        this.strTransStatus=transStatus;
        this.strRefNo=refNo;
        this.strTransId=transId;
        this.strTransDate=transDate;
        
    }

    public String getStrBillPrintOnSettlement() {
        return strBillPrintOnSettlement;
    }

    public void setStrBillPrintOnSettlement(String strBillPrintOnSettlement) {
        this.strBillPrintOnSettlement = strBillPrintOnSettlement;
    }
    
    private clsSettelementOptions(String strSettelmentCode,String strSettelmentType,double dblConvertionRatio
        ,String strSettelmentDesc,String strBillPrintOnSettlement){
        this.strSettelmentCode=strSettelmentCode;
        this.strSettelmentType=strSettelmentType;
        this.dblConvertionRatio=dblConvertionRatio;
        this.strSettelmentDesc=strSettelmentDesc;
        this.strBillPrintOnSettlement=strBillPrintOnSettlement;
    }
    
    public void funAddSettelementOptions(){
        
        hmSettelementOptionsDtl= new HashMap<>();
        listSettelmentOptions=new ArrayList<>();
        String sqlSettlementModes = "";
        try {
            /*
            if ("Yes".trim().equalsIgnoreCase(clsGlobalVarClass.gDebitCardPayment)) {
                sqlSettlementModes = "select strSettelmentCode,strSettelmentDesc,strSettelmentType,dblConvertionRatio"
                    + " from tblsettelmenthd where strApplicable='Yes' and strBilling='Yes'";
            } else {
                sqlSettlementModes = "select strSettelmentCode,strSettelmentDesc,strSettelmentType,dblConvertionRatio "
                    + "from tblsettelmenthd where strApplicable='Yes' and strBilling='Yes' and strSettelmentType!='Debit Card'";
            }*/
            
            if(clsGlobalVarClass.gPickSettlementsFromPOSMaster)
            {
                sqlSettlementModes = "select b.strSettelmentCode,b.strSettelmentDesc,b.strSettelmentType"
                    + " ,b.dblConvertionRatio,b.strBillPrintOnSettlement "
                    + " from tblpossettlementdtl a,tblsettelmenthd b "
                    + " where a.strSettlementCode=b.strSettelmentCode and b.strApplicable='Yes' "
                    + " and b.strBilling='Yes' and a.strPOSCode='"+clsGlobalVarClass.gPOSCode+"'";
            }
            else
            {
                sqlSettlementModes = "select strSettelmentCode,strSettelmentDesc,strSettelmentType,dblConvertionRatio"
                    + " ,strBillPrintOnSettlement "
                    + " from tblsettelmenthd where strApplicable='Yes' and strBilling='Yes'";
            }
            ResultSet rsSettlement = clsGlobalVarClass.dbMysql.executeResultSet(sqlSettlementModes);
            while (rsSettlement.next()) 
            {
                if(clsGlobalVarClass.gEnablePMSIntegrationYN)
                {
                    if(clsGlobalVarClass.gSuperUser)
                    {
                        listSettelmentOptions.add(rsSettlement.getString("strSettelmentDesc"));
                        hmSettelementOptionsDtl.put(rsSettlement.getString("strSettelmentDesc")
                            ,new clsSettelementOptions(rsSettlement.getString("strSettelmentCode"),rsSettlement.getString("strSettelmentType")
                            ,rsSettlement.getDouble("dblConvertionRatio"),rsSettlement.getString("strSettelmentDesc"),rsSettlement.getString("strBillPrintOnSettlement")));
                    }
                    else
                    {
                        listSettelmentOptions.add(rsSettlement.getString("strSettelmentDesc"));
                            hmSettelementOptionsDtl.put(rsSettlement.getString("strSettelmentDesc")
                            ,new clsSettelementOptions(rsSettlement.getString("strSettelmentCode"),rsSettlement.getString("strSettelmentType")
                            ,rsSettlement.getDouble("dblConvertionRatio"),rsSettlement.getString("strSettelmentDesc"),rsSettlement.getString("strBillPrintOnSettlement")));
                    }
                }
                else
                {
                    listSettelmentOptions.add(rsSettlement.getString("strSettelmentDesc"));
                    hmSettelementOptionsDtl.put(rsSettlement.getString("strSettelmentDesc")
                        ,new clsSettelementOptions(rsSettlement.getString("strSettelmentCode"),rsSettlement.getString("strSettelmentType")
                        ,rsSettlement.getDouble("dblConvertionRatio"),rsSettlement.getString("strSettelmentDesc"),rsSettlement.getString("strBillPrintOnSettlement")));
                        
                    /*
                    if(rsSettlement.getString("strSettelmentType").equals("Room"))
                    {
                        //ignore room settlement
                    }
                    else
                    {
                        listSettelmentOptions.add(rsSettlement.getString("strSettelmentDesc"));
                        hmSettelementOptionsDtl.put(rsSettlement.getString("strSettelmentDesc")
                        ,new clsSettelementOptions(rsSettlement.getString("strSettelmentCode"),rsSettlement.getString("strSettelmentType")
                        ,rsSettlement.getDouble("dblConvertionRatio"),rsSettlement.getString("strSettelmentDesc"),rsSettlement.getString("strBillPrintOnSettlement")));
                    }*/
                }                                
            }
            rsSettlement.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getStrSettelmentCode() {
        return strSettelmentCode;
    }

    public void setStrSettelmentCode(String strSettelmentCode) {
        this.strSettelmentCode = strSettelmentCode;
    }

    public String getStrSettelmentDesc() {
        return strSettelmentDesc;
    }

    public void setStrSettelmentDesc(String strSettelmentDesc) {
        this.strSettelmentDesc = strSettelmentDesc;
    }

    public String getStrSettelmentType() {
        return strSettelmentType;
    }

    public void setStrSettelmentType(String strSettelmentType) {
        this.strSettelmentType = strSettelmentType;
    }

    public double getDblConvertionRatio() {
        return dblConvertionRatio;
    }

    public void setDblConvertionRatio(double dblConvertionRatio) {
        this.dblConvertionRatio = dblConvertionRatio;
    }

    public double getDblSettlementAmt() {
        return dblSettlementAmt;
    }

    public void setDblSettlementAmt(double dblSettlementAmt) {
        this.dblSettlementAmt = dblSettlementAmt;
    }

    public double getDblPaidAmt() {
        return dblPaidAmt;
    }

    public void setDblPaidAmt(double dblPaidAmt) {
        this.dblPaidAmt = dblPaidAmt;
    }

    public String getStrExpiryDate() {
        return strExpiryDate;
    }

    public void setStrExpiryDate(String strExpiryDate) {
        this.strExpiryDate = strExpiryDate;
    }

    public String getStrCardName() {
        return strCardName;
    }

    public void setStrCardName(String strCardName) {
        this.strCardName = strCardName;
    }

    public String getStrRemark() {
        return strRemark;
    }

    public void setStrRemark(String strRemark) {
        this.strRemark = strRemark;
    }

    public double getDblActualAmt() {
        return dblActualAmt;
    }

    public void setDblActualAmt(double dblActualAmt) {
        this.dblActualAmt = dblActualAmt;
    }

    public double getDblRefundAmt() {
        return dblRefundAmt;
    }

    public void setDblRefundAmt(double dblRefundAmt) {
        this.dblRefundAmt = dblRefundAmt;
    }

    public String getStrGiftVoucherCode() {
        return strGiftVoucherCode;
    }

    public void setStrGiftVoucherCode(String strGiftVoucherCode) {
        this.strGiftVoucherCode = strGiftVoucherCode;
    }

    public String getStrFolioNo()
    {
        return strFolioNo;
    }

    public void setStrFolioNo(String strFolioNo)
    {
        this.strFolioNo = strFolioNo;
    }

    public String getStrRoomNo()
    {
        return strRoomNo;
    }

    public void setStrRoomNo(String strRoomNo)
    {
        this.strRoomNo = strRoomNo;
    }

    public String getStrGuestCode()
    {
        return strGuestCode;
    }

    public void setStrGuestCode(String strGuestCode)
    {
        this.strGuestCode = strGuestCode;
    }

    public String getStrMerchantCode()
    {
        return strMerchantCode;
    }

    public void setStrMerchantCode(String strMerchantCode)
    {
        this.strMerchantCode = strMerchantCode;
    }

    public String getStrQRString()
    {
        return strQRString;
    }

    public void setStrQRString(String strQRString)
    {
        this.strQRString = strQRString;
    }

    public String getStrTransStatus()
    {
        return strTransStatus;
    }

    public void setStrTransStatus(String strTransStatus)
    {
        this.strTransStatus = strTransStatus;
    }

    public String getStrRefNo()
    {
        return strRefNo;
    }

    public void setStrRefNo(String strRefNo)
    {
        this.strRefNo = strRefNo;
    }

    public String getStrTransId()
    {
        return strTransId;
    }

    public void setStrTransId(String strTransId)
    {
        this.strTransId = strTransId;
    }

    public String getStrTransDate()
    {
        return strTransDate;
    }

    public void setStrTransDate(String strTransDate)
    {
        this.strTransDate = strTransDate;
    }

   
    
    
}
