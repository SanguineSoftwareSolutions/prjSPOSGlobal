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
public class clsOperatorDtl implements Cloneable
{

    private String strUserCode;

    private String strVoidedUser;

    private String strUserName;

    private String strPOSCode;

    private String strPOSName;

    private String strSettlementDesc;

    private double discountAmt;

    private double settleAmt;

    private String strUser;

    private String reason;

    private String remark;

    private double dblBillAmount;

    private double dblSubTotal;

    private double dblTaxAmt;

    private double dblNetTotal;

    private double dblGrossAmt;

    private double dblThirdPartyComission;

    private double dblComission;

    private String strComissionOn;

    private String strComissionType;

    private double intBillSeriesPaxNo;

    public String getStrUserCode()
    {
        return strUserCode;
    }

    public void setStrUserCode(String strUserCode)
    {
        this.strUserCode = strUserCode;
    }

    public String getStrUserName()
    {
        return strUserName;
    }

    public void setStrUserName(String strUserName)
    {
        this.strUserName = strUserName;
    }

    public String getStrPOSCode()
    {
        return strPOSCode;
    }

    public void setStrPOSCode(String strPOSCode)
    {
        this.strPOSCode = strPOSCode;
    }

    public String getStrPOSName()
    {
        return strPOSName;
    }

    public void setStrPOSName(String strPOSName)
    {
        this.strPOSName = strPOSName;
    }

    public String getStrSettlementDesc()
    {
        return strSettlementDesc;
    }

    public void setStrSettlementDesc(String strSettlementDesc)
    {
        this.strSettlementDesc = strSettlementDesc;
    }

    public double getDiscountAmt()
    {
        return discountAmt;
    }

    public void setDiscountAmt(double discountAmt)
    {
        this.discountAmt = discountAmt;
    }

    public double getSettleAmt()
    {
        return settleAmt;
    }

    public void setSettleAmt(double settleAmt)
    {
        this.settleAmt = settleAmt;
    }

    public String getStrUser()
    {
        return strUser;
    }

    public void setStrUser(String strUser)
    {
        this.strUser = strUser;
    }

    public String getReason()
    {
        return reason;
    }

    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public double getDblBillAmount()
    {
        return dblBillAmount;
    }

    public void setDblBillAmount(double dblBillAmount)
    {
        this.dblBillAmount = dblBillAmount;
    }

    public String getStrVoidedUser()
    {
        return strVoidedUser;
    }

    public void setStrVoidedUser(String strVoidedUser)
    {
        this.strVoidedUser = strVoidedUser;
    }

    public double getDblNetTotal()
    {
        return dblNetTotal;
    }

    public void setDblNetTotal(double dblNetTotal)
    {
        this.dblNetTotal = dblNetTotal;
    }

    public double getDblComission()
    {
        return dblComission;
    }

    public void setDblComission(double dblComission)
    {
        this.dblComission = dblComission;
    }

    public double getIntBillSeriesPaxNo()
    {
        return intBillSeriesPaxNo;
    }

    public void setIntBillSeriesPaxNo(double intBillSeriesPaxNo)
    {
        this.intBillSeriesPaxNo = intBillSeriesPaxNo;
    }

    public double getDblThirdPartyComission()
    {
        return dblThirdPartyComission;
    }

    public void setDblThirdPartyComission(double dblThirdPartyComission)
    {
        this.dblThirdPartyComission = dblThirdPartyComission;
    }

    public String getStrComissionOn()
    {
        return strComissionOn;
    }

    public void setStrComissionOn(String strComissionOn)
    {
        this.strComissionOn = strComissionOn;
    }

    public String getStrComissionType()
    {
        return strComissionType;
    }

    public void setStrComissionType(String strComissionType)
    {
        this.strComissionType = strComissionType;
    }

    public double getDblGrossAmt()
    {
        return dblGrossAmt;
    }

    public void setDblGrossAmt(double dblGrossAmt)
    {
        this.dblGrossAmt = dblGrossAmt;
    }

    public double getDblSubTotal()
    {
        return dblSubTotal;
    }

    public void setDblSubTotal(double dblSubTotal)
    {
        this.dblSubTotal = dblSubTotal;
    }

    public double getDblTaxAmt()
    {
        return dblTaxAmt;
    }

    public void setDblTaxAmt(double dblTaxAmt)
    {
        this.dblTaxAmt = dblTaxAmt;
    }

}
