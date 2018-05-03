
package com.POSGlobal.controller;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class clsCashManagement {

    public Map<String,clsCashManagementDtl> funGetCashManagement(String fromDate,String toDate, String POSCode) throws Exception
    {
        Map<String,clsCashManagementDtl> hmCashMgmtDtl=new HashMap<String,clsCashManagementDtl>();
        StringBuilder sbSql=new StringBuilder();
        sbSql.setLength(0);
        
        StringBuilder sbSqlSale=new StringBuilder();
        Set<String> setUsers=new HashSet<String>();
        sbSqlSale.setLength(0);
        sbSqlSale.append("select time(dteTransDate),a.strUserEdited "
            + " from tblcashmanagement a "
            + " where date(a.dteTransDate) between '"+fromDate+"' and '"+toDate+"' and a.strAgainst='Rolling' "
            + " and a.strPOSCode='"+POSCode+"' "
            + " order by a.strUserEdited ");
        ResultSet rsRollingEntry=clsGlobalVarClass.dbMysql.executeResultSet(sbSqlSale.toString());
        while(rsRollingEntry.next())
        {
            setUsers.add(rsRollingEntry.getString(2).trim());
            sbSqlSale.setLength(0);
            sbSqlSale.append("select a.strUserEdited,sum(b.dblSettlementAmt) "
                + " from tblbillhd a,tblbillsettlementdtl b,tblsettelmenthd c "
                + " where a.strBillNo=b.strBillNo and b.strSettlementCode=c.strSettelmentCode "
                + " and c.strSettelmentType='Cash' and date(a.dteBillDate) between '"+fromDate+"' and '"+toDate+"' "
                + " and time(a.dteBillDate) <  '"+rsRollingEntry.getString(1)+"' and a.strUserEdited='"+rsRollingEntry.getString(2)+"' "
                + " and a.strPOSCode='"+POSCode+"' "
                + " group by a.strUserEdited");
            ResultSet rsSalesAmt=clsGlobalVarClass.dbMysql.executeResultSet(sbSqlSale.toString());
            while(rsSalesAmt.next())
            {
                clsCashManagementDtl objCashMgmtDtl=new clsCashManagementDtl();
		String user=rsSalesAmt.getString(1).trim();
		
                if(hmCashMgmtDtl.containsKey(user))
                {
                    objCashMgmtDtl=hmCashMgmtDtl.get(user);
                    objCashMgmtDtl.setSaleAmt(objCashMgmtDtl.getSaleAmt()+rsSalesAmt.getDouble(2));
                    hmCashMgmtDtl.put(user,objCashMgmtDtl);
                }
                else
                {
                    objCashMgmtDtl.setSaleAmt(rsSalesAmt.getDouble(2));
                    hmCashMgmtDtl.put(user,objCashMgmtDtl);
                }
            }
            rsSalesAmt.close();


            sbSqlSale.setLength(0);
            sbSqlSale.append("select a.strUserEdited,sum(b.dblSettlementAmt) "
                + " from tblqbillhd a,tblqbillsettlementdtl b,tblsettelmenthd c "
                + " where a.strBillNo=b.strBillNo and b.strSettlementCode=c.strSettelmentCode "
                + " and c.strSettelmentType='Cash' and date(a.dteBillDate) between '"+fromDate+"' and '"+toDate+"' "
                + " and time(a.dteBillDate) < '"+rsRollingEntry.getString(1)+"' and a.strUserEdited='"+rsRollingEntry.getString(2).trim()+"' "
                + " and a.strPOSCode='"+POSCode+"' "
                + " group by a.strUserEdited");
            rsSalesAmt=clsGlobalVarClass.dbMysql.executeResultSet(sbSqlSale.toString());
            while(rsSalesAmt.next())
            {
                clsCashManagementDtl objCashMgmtDtl=new clsCashManagementDtl();
		String user=rsSalesAmt.getString(1).trim();
                if(hmCashMgmtDtl.containsKey(user))
                {
                    objCashMgmtDtl=hmCashMgmtDtl.get(user);
                    objCashMgmtDtl.setSaleAmt(objCashMgmtDtl.getSaleAmt()+rsSalesAmt.getDouble(2));
                    hmCashMgmtDtl.put(user,objCashMgmtDtl);
                }
                else
                {
                    objCashMgmtDtl.setSaleAmt(rsSalesAmt.getDouble(2));
                    hmCashMgmtDtl.put(user,objCashMgmtDtl);
                }
            }
            rsSalesAmt.close();
            
            Map<String,Double> hmPostRollingSalesAmt=null;
            sbSqlSale.setLength(0);
            sbSqlSale.append("select a.strUserEdited,sum(b.dblSettlementAmt) "
                + " from tblbillhd a,tblbillsettlementdtl b,tblsettelmenthd c "
                + " where a.strBillNo=b.strBillNo and b.strSettlementCode=c.strSettelmentCode "
                + " and c.strSettelmentType='Cash' and date(a.dteBillDate) between '"+fromDate+"' and '"+toDate+"' "
                + " and time(a.dteBillDate) > '"+rsRollingEntry.getString(1)+"' and a.strUserEdited='"+rsRollingEntry.getString(2)+"' "
                + " and a.strPOSCode='"+POSCode+"' "
                + " group by a.strUserEdited");
            rsSalesAmt=clsGlobalVarClass.dbMysql.executeResultSet(sbSqlSale.toString());
            while(rsSalesAmt.next())
            {
                clsCashManagementDtl objCashMgmtDtl=new clsCashManagementDtl();
		String user=rsSalesAmt.getString(1).trim();
		
		
                if(hmCashMgmtDtl.containsKey(user))
                {
                    objCashMgmtDtl=hmCashMgmtDtl.get(user);
                    hmPostRollingSalesAmt=new HashMap<String,Double>();
                    if(hmPostRollingSalesAmt.containsKey(rsRollingEntry.getString(1)))
                    {
                        hmPostRollingSalesAmt.put(rsRollingEntry.getString(1),hmPostRollingSalesAmt.get(rsRollingEntry.getString(1))+rsSalesAmt.getDouble(2));
                    }
                    else
                    {
                        hmPostRollingSalesAmt.put(rsRollingEntry.getString(1),rsSalesAmt.getDouble(2));
                    }
                    objCashMgmtDtl.setHmPostRollingSalesAmt(hmPostRollingSalesAmt);
                    hmCashMgmtDtl.put(user,objCashMgmtDtl);
                }
                else
                {
                    hmPostRollingSalesAmt=new HashMap<String,Double>();
                    hmPostRollingSalesAmt.put(rsRollingEntry.getString(1),rsSalesAmt.getDouble(2));
                    objCashMgmtDtl.setHmPostRollingSalesAmt(hmPostRollingSalesAmt);
                    hmCashMgmtDtl.put(user,objCashMgmtDtl);
                }
            }
            rsSalesAmt.close();


            sbSqlSale.setLength(0);
            sbSqlSale.append("select a.strUserEdited,sum(b.dblSettlementAmt) "
                + " from tblqbillhd a,tblqbillsettlementdtl b,tblsettelmenthd c "
                + " where a.strBillNo=b.strBillNo and b.strSettlementCode=c.strSettelmentCode "
                + " and c.strSettelmentType='Cash' and date(a.dteBillDate) between '"+fromDate+"' and '"+toDate+"' "
                + " and time(a.dteBillDate) > '"+rsRollingEntry.getString(1)+"' and a.strUserEdited='"+rsRollingEntry.getString(2)+"' "
                + " and a.strPOSCode='"+POSCode+"' "
                + " group by a.strUserEdited");
            rsSalesAmt=clsGlobalVarClass.dbMysql.executeResultSet(sbSqlSale.toString());
            while(rsSalesAmt.next())
            {
                clsCashManagementDtl objCashMgmtDtl=new clsCashManagementDtl();
		String user=rsSalesAmt.getString(1).trim();
		
                if(hmCashMgmtDtl.containsKey(user))
                {
                    objCashMgmtDtl=hmCashMgmtDtl.get(user);
                    hmPostRollingSalesAmt=new HashMap<String,Double>();
                    if(hmPostRollingSalesAmt.containsKey(rsRollingEntry.getString(1)))
                    {
                        hmPostRollingSalesAmt.put(rsRollingEntry.getString(1),hmPostRollingSalesAmt.get(rsRollingEntry.getString(1))+rsSalesAmt.getDouble(2));
                    }
                    else
                    {
                        hmPostRollingSalesAmt.put(rsRollingEntry.getString(1),rsSalesAmt.getDouble(2));
                    }
                    objCashMgmtDtl.setHmPostRollingSalesAmt(hmPostRollingSalesAmt);
                    hmCashMgmtDtl.put(user,objCashMgmtDtl);
                }
                else
                {
                    hmPostRollingSalesAmt=new HashMap<String,Double>();
                    hmPostRollingSalesAmt.put(rsRollingEntry.getString(1),rsSalesAmt.getDouble(2));
                    objCashMgmtDtl.setHmPostRollingSalesAmt(hmPostRollingSalesAmt);
                    hmCashMgmtDtl.put(user,objCashMgmtDtl);
                }
            }
            rsSalesAmt.close();
        }
        
        
        sbSqlSale.setLength(0);
        sbSqlSale.append("select a.strUserEdited,sum(b.dblSettlementAmt) "
            + " from tblbillhd a,tblbillsettlementdtl b,tblsettelmenthd c "
            + " where a.strBillNo=b.strBillNo and b.strSettlementCode=c.strSettelmentCode "
            + " and c.strSettelmentType='Cash' and date(a.dteBillDate) between '"+fromDate+"' and '"+toDate+"' "
            + " and a.strPOSCode='"+POSCode+"' "
            + " group by a.strUserEdited");
        ResultSet rsSalesAmt=clsGlobalVarClass.dbMysql.executeResultSet(sbSqlSale.toString());
        while(rsSalesAmt.next())
        {
            String user=rsSalesAmt.getString(1).trim();
            if(!setUsers.contains(user))
            {
                clsCashManagementDtl objCashMgmtDtl=new clsCashManagementDtl();
                if(hmCashMgmtDtl.containsKey(user))
                {
                    objCashMgmtDtl=hmCashMgmtDtl.get(user);
                    objCashMgmtDtl.setSaleAmt(objCashMgmtDtl.getSaleAmt()+rsSalesAmt.getDouble(2));
                    hmCashMgmtDtl.put(user,objCashMgmtDtl);
                }
                else
                {
                    objCashMgmtDtl.setSaleAmt(rsSalesAmt.getDouble(2));
                    hmCashMgmtDtl.put(user,objCashMgmtDtl);
                }
            }
        }
        rsSalesAmt.close();


        sbSqlSale.setLength(0);
        sbSqlSale.append("select a.strUserEdited,sum(b.dblSettlementAmt) "
            + " from tblqbillhd a,tblqbillsettlementdtl b,tblsettelmenthd c "
            + " where a.strBillNo=b.strBillNo and b.strSettlementCode=c.strSettelmentCode "
            + " and c.strSettelmentType='Cash' and date(a.dteBillDate) between '"+fromDate+"' and '"+toDate+"' "
            + " and a.strPOSCode='"+POSCode+"' "
            + " group by a.strUserEdited");
        rsSalesAmt=clsGlobalVarClass.dbMysql.executeResultSet(sbSqlSale.toString());
        while(rsSalesAmt.next())
        {
            String user=rsSalesAmt.getString(1).trim();
            if(!setUsers.contains(user))
            {
                clsCashManagementDtl objCashMgmtDtl=new clsCashManagementDtl();
                if(hmCashMgmtDtl.containsKey(user))
                {
                    objCashMgmtDtl=hmCashMgmtDtl.get(user);
                    objCashMgmtDtl.setSaleAmt(objCashMgmtDtl.getSaleAmt()+rsSalesAmt.getDouble(2));
                    hmCashMgmtDtl.put(user,objCashMgmtDtl);
                }
                else
                {
                    objCashMgmtDtl.setSaleAmt(rsSalesAmt.getDouble(2));
                    hmCashMgmtDtl.put(user,objCashMgmtDtl);
                }
            }
        }
        rsSalesAmt.close();
        
        /*
        sbSqlSale.setLength(0);
        sbSqlSale.append("select strUserEdited,sum(dblAdvDeposite) from tbladvancereceipthd "
            + " where strAdvBookingNo not in (select strAdvBookingNo from tblbillhd "
            + " where date(dteBillDate) between '"+fromDate+"' and '"+toDate+"') "
            + " and dtReceiptDate between '"+fromDate+"' and '"+toDate+"' "
            + " group by strUserEdited ");
        ResultSet rsAdvAmt=clsGlobalVarClass.dbMysql.executeResultSet(sbSqlSale.toString());
        while(rsAdvAmt.next())
        {
            clsCashManagementDtl objCashMgmtDtl=new clsCashManagementDtl();
            if(hmCashMgmtDtl.containsKey(rsAdvAmt.getString(1)))
            {
                objCashMgmtDtl=hmCashMgmtDtl.get(rsAdvAmt.getString(1));
                objCashMgmtDtl.setAdvanceAmt(objCashMgmtDtl.getAdvanceAmt()+rsAdvAmt.getDouble(2));
                hmCashMgmtDtl.put(rsAdvAmt.getString(1),objCashMgmtDtl);
            }
            else
            {
                objCashMgmtDtl.setAdvanceAmt(rsAdvAmt.getDouble(2));
                hmCashMgmtDtl.put(rsAdvAmt.getString(1),objCashMgmtDtl);
            }
        }
        rsAdvAmt.close();
        
        
        sbSqlSale.setLength(0);
        sbSqlSale.append("select a.strUserEdited,sum(b.dblAdvDeposite) "
            + " from tblbillhd a,tbladvancereceipthd b "
            + " where a.strAdvBookingNo=b.strAdvBookingNo and b.dtReceiptDate between '"+fromDate+"' and '"+toDate+"' "
            + " group by a.strUserEdited ");
        rsAdvAmt=clsGlobalVarClass.dbMysql.executeResultSet(sbSqlSale.toString());
        while(rsAdvAmt.next())
        {
            clsCashManagementDtl objCashMgmtDtl=new clsCashManagementDtl();
            if(hmCashMgmtDtl.containsKey(rsAdvAmt.getString(1)))
            {
                objCashMgmtDtl=hmCashMgmtDtl.get(rsAdvAmt.getString(1));
                objCashMgmtDtl.setAdvanceAmt(objCashMgmtDtl.getAdvanceAmt()+rsAdvAmt.getDouble(2));
                hmCashMgmtDtl.put(rsAdvAmt.getString(1),objCashMgmtDtl);
            }
            else
            {
                objCashMgmtDtl.setAdvanceAmt(rsAdvAmt.getDouble(2));
                hmCashMgmtDtl.put(rsAdvAmt.getString(1),objCashMgmtDtl);
            }
        }
        rsAdvAmt.close();
                */
        
        
        sbSqlSale.setLength(0);
        sbSqlSale.append("select strUserEdited,sum(dblAdvDeposite) from tbladvancereceipthd "
            + " where dtReceiptDate between '"+fromDate+"' and '"+toDate+"' and strPOSCode='"+POSCode+"' "
            + " group by strUserEdited ");
        ResultSet rsAdvAmt=clsGlobalVarClass.dbMysql.executeResultSet(sbSqlSale.toString());
        while(rsAdvAmt.next())
        {
            clsCashManagementDtl objCashMgmtDtl=new clsCashManagementDtl();
	    String user=rsAdvAmt.getString(1).trim();
	    
	    
            if(hmCashMgmtDtl.containsKey(user))
            {
                objCashMgmtDtl=hmCashMgmtDtl.get(user);
                objCashMgmtDtl.setAdvanceAmt(objCashMgmtDtl.getAdvanceAmt()+rsAdvAmt.getDouble(2));
                hmCashMgmtDtl.put(user,objCashMgmtDtl);
            }
            else
            {
                objCashMgmtDtl.setAdvanceAmt(rsAdvAmt.getDouble(2));
                hmCashMgmtDtl.put(user,objCashMgmtDtl);
            }
        }
        rsAdvAmt.close();
        
        
        sbSqlSale.setLength(0);
        sbSqlSale.append("select strUserEdited,sum(dblAdvDeposite) from tblqadvancereceipthd "
            + " where dtReceiptDate between '"+fromDate+"' and '"+toDate+"' and strPOSCode='"+POSCode+"' "
            + " group by strUserEdited ");
        rsAdvAmt=clsGlobalVarClass.dbMysql.executeResultSet(sbSqlSale.toString());
        while(rsAdvAmt.next())
        {
            clsCashManagementDtl objCashMgmtDtl=new clsCashManagementDtl();
	    
	    String user=rsAdvAmt.getString(1).trim();
	    
            if(hmCashMgmtDtl.containsKey(user))
            {
                objCashMgmtDtl=hmCashMgmtDtl.get(user);
                objCashMgmtDtl.setAdvanceAmt(objCashMgmtDtl.getAdvanceAmt()+rsAdvAmt.getDouble(2));
                hmCashMgmtDtl.put(user,objCashMgmtDtl);
            }
            else
            {
                objCashMgmtDtl.setAdvanceAmt(rsAdvAmt.getDouble(2));
                hmCashMgmtDtl.put(user,objCashMgmtDtl);
            }
        }
        rsAdvAmt.close();
        
        
        sbSql.setLength(0);
        sbSql.append("select strUserEdited,strTransType,sum(dblAmount),sum(dblRollingAmt) "
            + " from tblcashmanagement "
            + " where date(dteTransDate) between '"+fromDate+"' and '"+toDate+"' and strPOSCode='"+POSCode+"'  "
            + " group by strUserEdited,strTransType "
            + " order by strTransType");
        ResultSet rsCashMgmt=clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
        while(rsCashMgmt.next())
        {
            double balanceAmt=0;
            clsCashManagementDtl objCashMgmtDtl=new clsCashManagementDtl();
	    String user=rsCashMgmt.getString(1).trim();
	    
            if(hmCashMgmtDtl.containsKey(user))
            {
                objCashMgmtDtl=hmCashMgmtDtl.get(user);
                balanceAmt+=objCashMgmtDtl.getSaleAmt();
                balanceAmt+=objCashMgmtDtl.getAdvanceAmt();
                
                Map<String,Double> hmPostRollingSalesAmt = objCashMgmtDtl.getHmPostRollingSalesAmt();
                if(null!=hmPostRollingSalesAmt)
                {
                    for(Map.Entry<String,Double> entry : hmPostRollingSalesAmt.entrySet())
                    {
                        balanceAmt+=entry.getValue();
                    }
                }

                if(rsCashMgmt.getString(2).equalsIgnoreCase("Float"))
                {
                    objCashMgmtDtl.setFloatAmt(objCashMgmtDtl.getFloatAmt()+rsCashMgmt.getDouble(3));
                    balanceAmt+=objCashMgmtDtl.getFloatAmt();
                }
                else if(rsCashMgmt.getString(2).equalsIgnoreCase("Withdrawal"))
                {
                    objCashMgmtDtl.setWithdrawlAmt(objCashMgmtDtl.getWithdrawlAmt()+rsCashMgmt.getDouble(3));
                    balanceAmt-=objCashMgmtDtl.getWithdrawlAmt();
                    objCashMgmtDtl.setRollingAmt(objCashMgmtDtl.getRollingAmt()+rsCashMgmt.getDouble(4));
                }
                else if(rsCashMgmt.getString(2).equalsIgnoreCase("Refund"))
                {
                    objCashMgmtDtl.setRefundAmt(objCashMgmtDtl.getRefundAmt()+rsCashMgmt.getDouble(3));
                    balanceAmt-=objCashMgmtDtl.getRefundAmt();
                }
                else if(rsCashMgmt.getString(2).equalsIgnoreCase("Payments"))
                {
                    objCashMgmtDtl.setPaymentAmt(objCashMgmtDtl.getPaymentAmt()+rsCashMgmt.getDouble(3));
                    balanceAmt-=objCashMgmtDtl.getPaymentAmt();
                }
                else if(rsCashMgmt.getString(2).equalsIgnoreCase("Transfer In"))
                {
                    objCashMgmtDtl.setTransferInAmt(objCashMgmtDtl.getTransferInAmt()+rsCashMgmt.getDouble(3));
                    balanceAmt+=objCashMgmtDtl.getTransferInAmt();
                }
                else if(rsCashMgmt.getString(2).equalsIgnoreCase("Transfer Out"))
                {
                    objCashMgmtDtl.setTransferOutAmt(objCashMgmtDtl.getTransferOutAmt()+rsCashMgmt.getDouble(3));
                    balanceAmt-=objCashMgmtDtl.getTransferOutAmt();
                }
                objCashMgmtDtl.setBalanceAmt(balanceAmt);
                hmCashMgmtDtl.put(user,objCashMgmtDtl);
            }
            else
            {
                objCashMgmtDtl.setFloatAmt(0);
                objCashMgmtDtl.setWithdrawlAmt(0);
                objCashMgmtDtl.setRollingAmt(0);
                objCashMgmtDtl.setTransferInAmt(0);
                objCashMgmtDtl.setTransferOutAmt(0);
                objCashMgmtDtl.setPaymentAmt(0);
                objCashMgmtDtl.setRefundAmt(0);
                objCashMgmtDtl.setBalanceAmt(0);
                objCashMgmtDtl.setSaleAmt(0);
                balanceAmt+=objCashMgmtDtl.getSaleAmt();
                balanceAmt+=objCashMgmtDtl.getAdvanceAmt();
                Map<String,Double> hmPostRollingSalesAmt = objCashMgmtDtl.getHmPostRollingSalesAmt();
                
                if(null!=hmPostRollingSalesAmt)
                {
                    for(Map.Entry<String,Double> entry : hmPostRollingSalesAmt.entrySet())
                    {
                        balanceAmt+=entry.getValue();
                    }
                }

                if(rsCashMgmt.getString(2).equalsIgnoreCase("Float"))
                {
                    objCashMgmtDtl.setFloatAmt(rsCashMgmt.getDouble(3));
                    balanceAmt+=objCashMgmtDtl.getFloatAmt();
                }
                else if(rsCashMgmt.getString(2).equalsIgnoreCase("Withdrawal"))
                {
                    objCashMgmtDtl.setWithdrawlAmt(rsCashMgmt.getDouble(3));
                    balanceAmt-=objCashMgmtDtl.getWithdrawlAmt();
                    objCashMgmtDtl.setRollingAmt(rsCashMgmt.getDouble(4));
                }
                else if(rsCashMgmt.getString(2).equalsIgnoreCase("Refund"))
                {
                    objCashMgmtDtl.setRefundAmt(rsCashMgmt.getDouble(3));
                    balanceAmt-=objCashMgmtDtl.getRefundAmt();
                }
                else if(rsCashMgmt.getString(2).equalsIgnoreCase("Payments"))
                {
                    objCashMgmtDtl.setPaymentAmt(rsCashMgmt.getDouble(3));
                    balanceAmt-=objCashMgmtDtl.getPaymentAmt();
                }
                else if(rsCashMgmt.getString(2).equalsIgnoreCase("Transfer In"))
                {
                    objCashMgmtDtl.setTransferInAmt(rsCashMgmt.getDouble(3));
                    balanceAmt+=objCashMgmtDtl.getTransferInAmt();
                }
                else if(rsCashMgmt.getString(2).equalsIgnoreCase("Transfer Out"))
                {
                    objCashMgmtDtl.setTransferOutAmt(rsCashMgmt.getDouble(3));
                    balanceAmt-=objCashMgmtDtl.getTransferOutAmt();
                }
                objCashMgmtDtl.setBalanceAmt(balanceAmt);
                hmCashMgmtDtl.put(user,objCashMgmtDtl);
            }
        }
        rsCashMgmt.close();
        
        return hmCashMgmtDtl;
    }
    
    
    public double funGetBalanceUserWise(String fromDate,String toDate,Map<String,clsCashManagementDtl> hmCashMgmtDtl,String userCode) throws Exception
    {
        double balanceAmt=0;
        if(hmCashMgmtDtl.containsKey(userCode))
        {
            clsCashManagementDtl objCashMgmtDtl=hmCashMgmtDtl.get(userCode);
            balanceAmt=(objCashMgmtDtl.getSaleAmt()+objCashMgmtDtl.getAdvanceAmt()+objCashMgmtDtl.getFloatAmt()+objCashMgmtDtl.getTransferInAmt())-(objCashMgmtDtl.getWithdrawlAmt()+objCashMgmtDtl.getPaymentAmt()+objCashMgmtDtl.getRefundAmt()+objCashMgmtDtl.getTransferOutAmt());
            Map<String,Double> hmPostRollingSalesAmt = objCashMgmtDtl.getHmPostRollingSalesAmt();
            if(null!=hmPostRollingSalesAmt)
            {
                for(Map.Entry<String,Double> entry : hmPostRollingSalesAmt.entrySet())
                {
                    balanceAmt+=entry.getValue();
                }
            }
        }
        return balanceAmt;
    }
    
    
}


