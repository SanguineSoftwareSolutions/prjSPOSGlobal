/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSGlobal.controller;

/**
 *
 * @author Shree
 */
public class clsBillDtl implements Cloneable
{

    String strItemCode;
    String strItemName;
    String strBillNo;
    String strAdvBookingNo;
    double dblRate;
    double dblQuantity;
    double dblModQuantity;
    double dblAmount;
    double dblTaxAmount;
    String dteBillDate;
    String strKOTNo;
    String strClientCode;
    String strCustomerCode;
    String tmeOrderProcessing;
    String strDataPostFlag;
    String strMMSDataPostFlag;
    String strManualKOTNo;
    String tdhYN;
    String strPromoCode;
    String strCounterCode;
    String strWaiterNo;
    double dblDiscountAmt;
    double dblDiscountPer;
    String sequenceNo;
    String subGrouName;
    String strGroupCode;
    String groupName;
    String strPosName;
    String strWShortName;
    String strReasonName;
    String strRemarks;
    String strPOSCode;
    String strTableName;
    String strTableNo;
    String strGroupName;
    String dteNCKOTDate;
    String strCustomerName;
    double dblIncentive;
    double dblIncentivePer;
    String strSubGroupCode;
    String strSubGroupName;
    String strDelBoyCode;
    String strDelBoyName;
    double dblCashTakenAmt;

    String tmeBillTime;
    String tmeBillSettleTime;
    String dteBillSettleDate;
    String strArea;
    String strTaxIndicator;
    String strUserCreated;

    double dblBillAmt;
    double DblBalanceAmt;
    double dblBalanceAmt;

    String StrReceiptNo;
    String dteReceiptDate;
    String strSettlementName;
    String strChequeNo;

    String strBankName;
    String strRemark;

    double dblComplQty;

    double dblDelCharges;

    String strOrderPickupTime;
    String strProcessTimeDiff;
    String strPickUpTimeDiff;
    String strWaiterFullName;

    private String dteOrderDate;

    String strItemProcessTime;
    String strItemTargetTime;

    String avgProcessingTime;
    String delayOrders;
    String totOrders;
    String totOrderesPer;
    String doAvg;
    String totAvg;
    String dotAvg;
    String dotAvgPer;
    long longMobileNo;
    String strWeightedAvgTarTme;
    String strWeightedAvgActualTme;
    private String strKOTToBillNote;
    int intPAXBillSeriesNo;

    public String getStrProcessTimeDiff()
    {
	return strProcessTimeDiff;
    }

    public void setStrProcessTimeDiff(String strProcessTimeDiff)
    {
	this.strProcessTimeDiff = strProcessTimeDiff;
    }

    public String getStrPickUpTimeDiff()
    {
	return strPickUpTimeDiff;
    }

    public void setStrPickUpTimeDiff(String strPickUpTimeDiff)
    {
	this.strPickUpTimeDiff = strPickUpTimeDiff;
    }

    public String getStrOrderPickupTime()
    {
	return strOrderPickupTime;
    }

    public void setStrOrderPickupTime(String strOrderPickupTime)
    {
	this.strOrderPickupTime = strOrderPickupTime;
    }

    public String getStrGroupCode()
    {
	return strGroupCode;
    }

    public void setStrGroupCode(String strGroupCode)
    {
	this.strGroupCode = strGroupCode;
    }

    public String getStrTableNo()
    {
	return strTableNo;
    }

    public void setStrTableNo(String strTableNo)
    {
	this.strTableNo = strTableNo;
    }

    public String getDteNCKOTDate()
    {
	return dteNCKOTDate;
    }

    public void setDteNCKOTDate(String dteNCKOTDate)
    {
	this.dteNCKOTDate = dteNCKOTDate;
    }

    public String getStrItemCode()
    {
	return strItemCode;
    }

    public void setStrItemCode(String strItemCode)
    {
	this.strItemCode = strItemCode;
    }

    public String getStrItemName()
    {
	return strItemName;
    }

    public void setStrItemName(String strItemName)
    {
	this.strItemName = strItemName;
    }

    public String getStrBillNo()
    {
	return strBillNo;
    }

    public void setStrBillNo(String strBillNo)
    {
	this.strBillNo = strBillNo;
    }

    public String getStrAdvBookingNo()
    {
	return strAdvBookingNo;
    }

    public void setStrAdvBookingNo(String strAdvBookingNo)
    {
	this.strAdvBookingNo = strAdvBookingNo;
    }

    public double getDblRate()
    {
	return dblRate;
    }

    public void setDblRate(double dblRate)
    {
	this.dblRate = dblRate;
    }

    public double getDblQuantity()
    {
	return dblQuantity;
    }

    public void setDblQuantity(double dblQuantity)
    {
	this.dblQuantity = dblQuantity;
    }

    public double getDblAmount()
    {
	return dblAmount;
    }

    public void setDblAmount(double dblAmount)
    {
	this.dblAmount = dblAmount;
    }

    public double getDblTaxAmount()
    {
	return dblTaxAmount;
    }

    public void setDblTaxAmount(double dblTaxAmount)
    {
	this.dblTaxAmount = dblTaxAmount;
    }

    public String getDteBillDate()
    {
	return dteBillDate;
    }

    public void setDteBillDate(String dteBillDate)
    {
	this.dteBillDate = dteBillDate;
    }

    public String getStrKOTNo()
    {
	return strKOTNo;
    }

    public void setStrKOTNo(String strKOTNo)
    {
	this.strKOTNo = strKOTNo;
    }

    public String getStrClientCode()
    {
	return strClientCode;
    }

    public void setStrClientCode(String strClientCode)
    {
	this.strClientCode = strClientCode;
    }

    public String getStrCustomerCode()
    {
	return strCustomerCode;
    }

    public void setStrCustomerCode(String strCustomerCode)
    {
	this.strCustomerCode = strCustomerCode;
    }

    public String getTmeOrderProcessing()
    {
	return tmeOrderProcessing;
    }

    public void setTmeOrderProcessing(String tmeOrderProcessing)
    {
	this.tmeOrderProcessing = tmeOrderProcessing;
    }

    public String getStrDataPostFlag()
    {
	return strDataPostFlag;
    }

    public void setStrDataPostFlag(String strDataPostFlag)
    {
	this.strDataPostFlag = strDataPostFlag;
    }

    public String getStrMMSDataPostFlag()
    {
	return strMMSDataPostFlag;
    }

    public void setStrMMSDataPostFlag(String strMMSDataPostFlag)
    {
	this.strMMSDataPostFlag = strMMSDataPostFlag;
    }

    public String getStrManualKOTNo()
    {
	return strManualKOTNo;
    }

    public void setStrManualKOTNo(String strManualKOTNo)
    {
	this.strManualKOTNo = strManualKOTNo;
    }

    public String getTdhYN()
    {
	return tdhYN;
    }

    public void setTdhYN(String tdhYN)
    {
	this.tdhYN = tdhYN;
    }

    public String getStrPromoCode()
    {
	return strPromoCode;
    }

    public void setStrPromoCode(String strPromoCode)
    {
	this.strPromoCode = strPromoCode;
    }

    public String getStrCounterCode()
    {
	return strCounterCode;
    }

    public void setStrCounterCode(String strCounterCode)
    {
	this.strCounterCode = strCounterCode;
    }

    public String getStrWaiterNo()
    {
	return strWaiterNo;
    }

    public void setStrWaiterNo(String strWaiterNo)
    {
	this.strWaiterNo = strWaiterNo;
    }

    public double getDblDiscountAmt()
    {
	return dblDiscountAmt;
    }

    public void setDblDiscountAmt(double dblDiscountAmt)
    {
	this.dblDiscountAmt = dblDiscountAmt;
    }

    public double getDblDiscountPer()
    {
	return dblDiscountPer;
    }

    public void setDblDiscountPer(double dblDiscountPer)
    {
	this.dblDiscountPer = dblDiscountPer;
    }

    public String getSequenceNo()
    {
	return sequenceNo;
    }

    public void setSequenceNo(String sequenceNo)
    {
	this.sequenceNo = sequenceNo;
    }

    public String getSubGrouName()
    {
	return subGrouName;
    }

    public void setSubGrouName(String subGrouName)
    {
	this.subGrouName = subGrouName;
    }

    public String getGroupName()
    {
	return groupName;
    }

    public void setGroupName(String groupName)
    {
	this.groupName = groupName;
    }

    public String getStrWShortName()
    {
	return strWShortName;
    }

    public void setStrWShortName(String strWShortName)
    {
	this.strWShortName = strWShortName;
    }

    public String getStrReasonName()
    {
	return strReasonName;
    }

    public void setStrReasonName(String strReasonName)
    {
	this.strReasonName = strReasonName;
    }

    public String getStrPOSCode()
    {
	return strPOSCode;
    }

    public void setStrPOSCode(String strPOSCode)
    {
	this.strPOSCode = strPOSCode;
    }

    public String getStrTableName()
    {
	return strTableName;
    }

    public void setStrTableName(String strTableName)
    {
	this.strTableName = strTableName;
    }

    public String getStrPosName()
    {
	return strPosName;
    }

    public void setStrPosName(String strPosName)
    {
	this.strPosName = strPosName;
    }

    public String getStrRemarks()
    {
	return strRemarks;
    }

    public void setStrRemarks(String strRemarks)
    {
	this.strRemarks = strRemarks;
    }

    public String getStrGroupName()
    {
	return strGroupName;
    }

    public void setStrGroupName(String strGroupName)
    {
	this.strGroupName = strGroupName;
    }

    public String getStrCustomerName()
    {
	return strCustomerName;
    }

    public void setStrCustomerName(String strCustomerName)
    {
	this.strCustomerName = strCustomerName;
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
	return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }

    public double getDblIncentive()
    {
	return dblIncentive;
    }

    public void setDblIncentive(double dblIncentive)
    {
	this.dblIncentive = dblIncentive;
    }

    public String getStrSubGroupCode()
    {
	return strSubGroupCode;
    }

    public void setStrSubGroupCode(String strSubGroupCode)
    {
	this.strSubGroupCode = strSubGroupCode;
    }

    public String getStrSubGroupName()
    {
	return strSubGroupName;
    }

    public void setStrSubGroupName(String strSubGroupName)
    {
	this.strSubGroupName = strSubGroupName;
    }

    public double getDblIncentivePer()
    {
	return dblIncentivePer;
    }

    public void setDblIncentivePer(double dblIncentivePer)
    {
	this.dblIncentivePer = dblIncentivePer;
    }

    public String getStrDelBoyCode()
    {
	return strDelBoyCode;
    }

    public void setStrDelBoyCode(String strDelBoyCode)
    {
	this.strDelBoyCode = strDelBoyCode;
    }

    public String getStrDelBoyName()
    {
	return strDelBoyName;
    }

    public void setStrDelBoyName(String strDelBoyName)
    {
	this.strDelBoyName = strDelBoyName;
    }

    public String getTmeBillTime()
    {
	return tmeBillTime;
    }

    public void setTmeBillTime(String tmeBillTime)
    {
	this.tmeBillTime = tmeBillTime;
    }

    public String getTmeBillSettleTime()
    {
	return tmeBillSettleTime;
    }

    public void setTmeBillSettleTime(String tmeBillSettleTime)
    {
	this.tmeBillSettleTime = tmeBillSettleTime;
    }

    public String getDteBillSettleDate()
    {
	return dteBillSettleDate;
    }

    public void setDteBillSettleDate(String dteBillSettleDate)
    {
	this.dteBillSettleDate = dteBillSettleDate;
    }

    public String getStrArea()
    {
	return strArea;
    }

    public void setStrArea(String strArea)
    {
	this.strArea = strArea;
    }

    public String getStrTaxIndicator()
    {
	return strTaxIndicator;
    }

    public void setStrTaxIndicator(String strTaxIndicator)
    {
	this.strTaxIndicator = strTaxIndicator;
    }

    public double getDblCashTakenAmt()
    {
	return dblCashTakenAmt;
    }

    public void setDblCashTakenAmt(double dblCashTakenAmt)
    {
	this.dblCashTakenAmt = dblCashTakenAmt;
    }

    public String getStrUserCreated()
    {
	return strUserCreated;
    }

    public void setStrUserCreated(String strUserCreated)
    {
	this.strUserCreated = strUserCreated;
    }

    public double getDblBillAmt()
    {
	return dblBillAmt;
    }

    public void setDblBillAmt(double dblBillAmt)
    {
	this.dblBillAmt = dblBillAmt;
    }

    public double getDblBalanceAmt()
    {
	return DblBalanceAmt;
    }

    public void setDblBalanceAmt(double DblBalanceAmt)
    {
	this.DblBalanceAmt = DblBalanceAmt;
    }

    public String getStrReceiptNo()
    {
	return StrReceiptNo;
    }

    public void setStrReceiptNo(String StrReceiptNo)
    {
	this.StrReceiptNo = StrReceiptNo;
    }

    public String getDteReceiptDate()
    {
	return dteReceiptDate;
    }

    public void setDteReceiptDate(String dteReceiptDate)
    {
	this.dteReceiptDate = dteReceiptDate;
    }

    public String getStrSettlementName()
    {
	return strSettlementName;
    }

    public void setStrSettlementName(String strSettlementName)
    {
	this.strSettlementName = strSettlementName;
    }

    public String getStrChequeNo()
    {
	return strChequeNo;
    }

    public void setStrChequeNo(String strChequeNo)
    {
	this.strChequeNo = strChequeNo;
    }

    public String getStrBankName()
    {
	return strBankName;
    }

    public void setStrBankName(String strBankName)
    {
	this.strBankName = strBankName;
    }

    public String getStrRemark()
    {
	return strRemark;
    }

    public void setStrRemark(String strRemark)
    {
	this.strRemark = strRemark;
    }

    public double getDblComplQty()
    {
	return dblComplQty;
    }

    public void setDblComplQty(double dblComplQty)
    {
	this.dblComplQty = dblComplQty;
    }

    public double getDblModQuantity()
    {
	return dblModQuantity;
    }

    public void setDblModQuantity(double dblModQuantity)
    {
	this.dblModQuantity = dblModQuantity;
    }

    public double getDblDelCharges()
    {
	return dblDelCharges;
    }

    public void setDblDelCharges(double dblDelCharges)
    {
	this.dblDelCharges = dblDelCharges;
    }

    public String getDteOrderDate()
    {
	return dteOrderDate;
    }

    public void setDteOrderDate(String dteOrderDate)
    {
	this.dteOrderDate = dteOrderDate;
    }

    public String getStrWaiterFullName()
    {
	return strWaiterFullName;
    }

    public void setStrWaiterFullName(String strWaiterFullName)
    {
	this.strWaiterFullName = strWaiterFullName;
    }

    public String getStrItemProcessTime()
    {
	return strItemProcessTime;
    }

    public void setStrItemProcessTime(String strItemProcessTime)
    {
	this.strItemProcessTime = strItemProcessTime;
    }

    public String getStrItemTargetTime()
    {
	return strItemTargetTime;
    }

    public void setStrItemTargetTime(String strItemTargetTime)
    {
	this.strItemTargetTime = strItemTargetTime;
    }

    public String getAvgProcessingTime()
    {
	return avgProcessingTime;
    }

    public void setAvgProcessingTime(String avgProcessingTime)
    {
	this.avgProcessingTime = avgProcessingTime;
    }

    public String getDelayOrders()
    {
	return delayOrders;
    }

    public void setDelayOrders(String delayOrders)
    {
	this.delayOrders = delayOrders;
    }

    public String getTotOrders()
    {
	return totOrders;
    }

    public void setTotOrders(String totOrders)
    {
	this.totOrders = totOrders;
    }

    public String getTotOrderesPer()
    {
	return totOrderesPer;
    }

    public void setTotOrderesPer(String totOrderesPer)
    {
	this.totOrderesPer = totOrderesPer;
    }

    public String getDoAvg()
    {
	return doAvg;
    }

    public void setDoAvg(String doAvg)
    {
	this.doAvg = doAvg;
    }

    public String getTotAvg()
    {
	return totAvg;
    }

    public void setTotAvg(String totAvg)
    {
	this.totAvg = totAvg;
    }

    public String getDotAvg()
    {
	return dotAvg;
    }

    public void setDotAvg(String dotAvg)
    {
	this.dotAvg = dotAvg;
    }

    public String getDotAvgPer()
    {
	return dotAvgPer;
    }

    public void setDotAvgPer(String dotAvgPer)
    {
	this.dotAvgPer = dotAvgPer;
    }

    public long getLongMobileNo()
    {
	return longMobileNo;
    }

    public void setLongMobileNo(long longMobileNo)
    {
	this.longMobileNo = longMobileNo;
    }

    public String getStrWeightedAvgTarTme()
    {
	return strWeightedAvgTarTme;
    }

    public void setStrWeightedAvgTarTme(String strWeightedAvgTarTme)
    {
	this.strWeightedAvgTarTme = strWeightedAvgTarTme;
    }

    public String getStrWeightedAvgActualTme()
    {
	return strWeightedAvgActualTme;
    }

    public void setStrWeightedAvgActualTme(String strWeightedAvgActualTme)
    {
	this.strWeightedAvgActualTme = strWeightedAvgActualTme;
    }

    public int getIntPAXBillSeriesNo()
    {
	return intPAXBillSeriesNo;
    }

    public void setIntPAXBillSeriesNo(int intPAXBillSeriesNo)
    {
	this.intPAXBillSeriesNo = intPAXBillSeriesNo;
    }

    public String getStrKOTToBillNote()
    {
	return strKOTToBillNote;
    }

    public void setStrKOTToBillNote(String strKOTToBillNote)
    {
	this.strKOTToBillNote = strKOTToBillNote;
    }

}
