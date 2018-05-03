/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.POSGlobal.controller;

import java.util.List;
import java.util.Map;

public class clsCashManagementDtl
{
    private double saleAmt;
    
    private double advanceAmt;
    
    private double floatAmt;
    
    private double withdrawlAmt;
    
    private double transferInAmt;
    
    private double transferOutAmt;
    
    private double refundAmt;
    
    private double paymentAmt;
    
    private double rollingAmt;
    
    private double balanceAmt;
    
    private String userCode;
    
    private Map<String,Double> hmPostRollingSalesAmt;

    public double getFloatAmt() {
        return floatAmt;
    }

    public void setFloatAmt(double floatAmt) {
        this.floatAmt = floatAmt;
    }

    public double getWithdrawlAmt() {
        return withdrawlAmt;
    }

    public void setWithdrawlAmt(double withdrawlAmt) {
        this.withdrawlAmt = withdrawlAmt;
    }

    public double getTransferInAmt() {
        return transferInAmt;
    }

    public void setTransferInAmt(double transferInAmt) {
        this.transferInAmt = transferInAmt;
    }

    public double getTransferOutAmt() {
        return transferOutAmt;
    }

    public void setTransferOutAmt(double transferOutAmt) {
        this.transferOutAmt = transferOutAmt;
    }

    public double getRefundAmt() {
        return refundAmt;
    }

    public void setRefundAmt(double refundAmt) {
        this.refundAmt = refundAmt;
    }

    public double getPaymentAmt() {
        return paymentAmt;
    }

    public void setPaymentAmt(double paymentAmt) {
        this.paymentAmt = paymentAmt;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public double getSaleAmt() {
        return saleAmt;
    }

    public void setSaleAmt(double saleAmt) {
        this.saleAmt = saleAmt;
    }

    public double getBalanceAmt() {
        return balanceAmt;
    }

    public void setBalanceAmt(double balanceAmt) {
        this.balanceAmt = balanceAmt;
    }

    public double getAdvanceAmt() {
        return advanceAmt;
    }

    public void setAdvanceAmt(double advanceAmt) {
        this.advanceAmt = advanceAmt;
    }

    public double getRollingAmt() {
        return rollingAmt;
    }

    public void setRollingAmt(double rollingAmt) {
        this.rollingAmt = rollingAmt;
    }

    public Map<String, Double> getHmPostRollingSalesAmt() {
        return hmPostRollingSalesAmt;
    }

    public void setHmPostRollingSalesAmt(Map<String, Double> hmPostRollingSalesAmt) {
        this.hmPostRollingSalesAmt = hmPostRollingSalesAmt;
    }
    
}
