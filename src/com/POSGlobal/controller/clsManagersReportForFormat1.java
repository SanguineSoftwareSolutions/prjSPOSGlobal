/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSGlobal.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class clsManagersReportForFormat1
{

    clsUtility objUtility1 = new clsUtility();
    clsUtility2 objUtility2 = new clsUtility2();
    DecimalFormat decFormatter = new DecimalFormat("0.00");
    private final DecimalFormat gDecimalFormat = clsGlobalVarClass.funGetGlobalDecimalFormatter();

    private int funSettlementWiseData(String fromDate, String toDate, String POSCode, PrintWriter pw) throws Exception
    {
	String sqlTip = "", sqlNoOfBill = "", sqlDiscount = "";
	StringBuilder sbSqlLiveFile = new StringBuilder();
	StringBuilder sbSqlQFile = new StringBuilder();

	Map<String, clsManagerReportBean> hmSettlementWiseData = new HashMap<String, clsManagerReportBean>();

	Map<Integer, String> mapTaxHeaders = new TreeMap<Integer, String>();
	Map<String, Double> mapTaxWiseData = new HashMap<String, Double>();

	int cntTax = 1;
	double totalTaxAmt = 0.00, totalSettleAmt = 0.00, totalDiscAmt = 0.00, totalTipAmt = 0.00, totalRoundOffAmt = 0.00, totalNetTotal = 0.00;
	int totalBills = 0;

	sbSqlLiveFile.setLength(0);
	sbSqlLiveFile.append(" select c.strSettelmentCode,c.strSettelmentDesc,sum(b.dblSettlementAmt) "
		+ " from tblbillhd a,tblbillsettlementdtl b,tblsettelmenthd c "
		+ " where a.strBillNo=b.strBillNo "
		+ " and date(a.dteBillDate)=date(b.dteBillDate) "
		+ " and b.strSettlementCode=c.strSettelmentCode "
		+ " and a.strClientCode=b.strClientCode "//and a.strSettelmentMode!='MultiSettle'
		+ " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		+ " and c.strSettelmentType!='Complementary' ");
	if (!POSCode.equalsIgnoreCase("All"))
	{
	    sbSqlLiveFile.append(" and a.strPOSCode='" + POSCode + "' ");
	}
	sbSqlLiveFile.append(" group by c.strSettelmentDesc ");
	System.out.println(sbSqlLiveFile);

	ResultSet rsSettleManager = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlLiveFile.toString());
	while (rsSettleManager.next())
	{

	    String settlementCode = rsSettleManager.getString(1);
	    String settlementDesc = rsSettleManager.getString(2);
	    double settleAmt = rsSettleManager.getDouble(3);

	    totalSettleAmt = totalSettleAmt + settleAmt;

	    if (hmSettlementWiseData.containsKey(settlementCode))
	    {
		clsManagerReportBean objManagerReportBean = hmSettlementWiseData.get(settlementCode);
		objManagerReportBean.setStrSettlementDesc(settlementDesc);
		objManagerReportBean.setDblSettlementAmt(objManagerReportBean.getDblSettlementAmt() + settleAmt);

		hmSettlementWiseData.put(settlementCode, objManagerReportBean);
	    }
	    else
	    {
		clsManagerReportBean objManagerReportBean = new clsManagerReportBean();
		objManagerReportBean.setStrSettlementCode(settlementCode);
		objManagerReportBean.setStrSettlementDesc(settlementDesc);
		objManagerReportBean.setDblSettlementAmt(settleAmt);

		hmSettlementWiseData.put(settlementCode, objManagerReportBean);
	    }
	}
	rsSettleManager.close();

	sbSqlQFile.setLength(0);
	sbSqlQFile.append(" select c.strSettelmentCode,c.strSettelmentDesc,sum(b.dblSettlementAmt) "
		+ " from tblqbillhd a,tblqbillsettlementdtl b,tblsettelmenthd c "
		+ " where a.strBillNo=b.strBillNo "
		+ " and date(a.dteBillDate)=date(b.dteBillDate) "
		+ " and b.strSettlementCode=c.strSettelmentCode "
		+ " and a.strClientCode=b.strClientCode "//and a.strSettelmentMode!='MultiSettle' 
		+ " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		+ " and c.strSettelmentType!='Complementary' ");
	if (!POSCode.equalsIgnoreCase("All"))
	{
	    sbSqlQFile.append(" and a.strPOSCode='" + POSCode + "' ");
	}
	sbSqlQFile.append(" group by c.strSettelmentDesc ");
	rsSettleManager = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlQFile.toString());

	while (rsSettleManager.next())
	{
	    String settlementCode = rsSettleManager.getString(1);
	    String settlementDesc = rsSettleManager.getString(2);
	    double settleAmt = rsSettleManager.getDouble(3);

	    totalSettleAmt = totalSettleAmt + settleAmt;

	    if (hmSettlementWiseData.containsKey(settlementCode))
	    {
		clsManagerReportBean objManagerReportBean = hmSettlementWiseData.get(settlementCode);
		objManagerReportBean.setStrSettlementDesc(settlementDesc);
		objManagerReportBean.setDblSettlementAmt(objManagerReportBean.getDblSettlementAmt() + settleAmt);

		hmSettlementWiseData.put(settlementCode, objManagerReportBean);
	    }
	    else
	    {
		clsManagerReportBean objManagerReportBean = new clsManagerReportBean();
		objManagerReportBean.setStrSettlementCode(settlementCode);
		objManagerReportBean.setStrSettlementDesc(settlementDesc);
		objManagerReportBean.setDblSettlementAmt(settleAmt);

		hmSettlementWiseData.put(settlementCode, objManagerReportBean);
	    }
	}
	rsSettleManager.close();

	String sqlTax = "select c.strTaxCode,sum(dblTaxAmount) "
		+ " from tblbillhd a,tblbilltaxdtl b,tbltaxhd c "
		+ " where a.strBillNo=b.strBillNo "
		+ " and date(a.dteBillDate)=date(b.dteBillDate) "
		+ " and b.strTaxCode=c.strTaxCode "
		+ " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "'"
		+ " and a.strClientCode=b.strClientCode ";
	if (!POSCode.equalsIgnoreCase("All"))
	{
	    sqlTax += " and a.strPOSCode='" + POSCode + "' ";
	}
	sqlTax += " group by c.strTaxCode";
	ResultSet rsTaxDtl1 = clsGlobalVarClass.dbMysql.executeResultSet(sqlTax);
	while (rsTaxDtl1.next())
	{
	    String taxCode = rsTaxDtl1.getString(1);
	    double taxAmt = rsTaxDtl1.getDouble(2);

	    totalTaxAmt = totalTaxAmt + taxAmt;

	    mapTaxHeaders.put(cntTax, taxCode);
	    cntTax++;

	    if (mapTaxWiseData.containsKey(taxCode))//taxCode
	    {
		mapTaxWiseData.put(taxCode, mapTaxWiseData.get(taxCode) + taxAmt);
	    }
	    else
	    {
		mapTaxWiseData.put(taxCode, taxAmt);
	    }
	}
	rsTaxDtl1.close();

	sqlTax = "select c.strTaxCode,sum(dblTaxAmount) "
		+ " from tblqbillhd a,tblqbilltaxdtl b,tbltaxhd c "
		+ " where a.strBillNo=b.strBillNo "
		+ " and date(a.dteBillDate)=date(b.dteBillDate) "
		+ " and b.strTaxCode=c.strTaxCode "
		+ " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		+ " and a.strClientCode=b.strClientCode ";
	if (!POSCode.equalsIgnoreCase("All"))
	{
	    sqlTax += " and a.strPOSCode='" + POSCode + "' ";
	}
	sqlTax += " group by c.strTaxCode";
	rsTaxDtl1 = clsGlobalVarClass.dbMysql.executeResultSet(sqlTax);
	while (rsTaxDtl1.next())
	{

	    String taxCode = rsTaxDtl1.getString(1);
	    double taxAmt = rsTaxDtl1.getDouble(2);

	    totalTaxAmt = totalTaxAmt + taxAmt;

	    if (!mapTaxHeaders.containsValue(taxCode))
	    {
		mapTaxHeaders.put(cntTax, taxCode);
		cntTax++;
	    }

	    if (mapTaxWiseData.containsKey(taxCode))//taxCode
	    {
		mapTaxWiseData.put(taxCode, mapTaxWiseData.get(taxCode) + taxAmt);
	    }
	    else
	    {
		mapTaxWiseData.put(taxCode, taxAmt);
	    }
	}
	rsTaxDtl1.close();

	//set discount,roundoff,tip
	sbSqlLiveFile.setLength(0);
	sbSqlLiveFile.append(" SELECT sum(a.dblDiscountAmt),sum(a.dblRoundOff),sum(a.dblTipAmount),count(*),sum(a.dblSubTotal),sum(a.dblTaxAmt),sum(a.dblSubTotal)-sum(a.dblDiscountAmt)as netTotal "
		+ " from tblbillhd a "
		+ " where date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		+ " ");
	if (!POSCode.equalsIgnoreCase("All"))
	{
	    sbSqlLiveFile.append(" and a.strPOSCode='" + POSCode + "' ");
	}
	System.out.println(sbSqlLiveFile);

	rsSettleManager = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlLiveFile.toString());
	while (rsSettleManager.next())
	{
	    totalDiscAmt = totalDiscAmt + rsSettleManager.getDouble(1);//discAmt
	    totalRoundOffAmt = totalRoundOffAmt + rsSettleManager.getDouble(2);//roundOff
	    totalTipAmt = totalTipAmt + rsSettleManager.getDouble(3);//tipAmt
	    totalBills = totalBills + rsSettleManager.getInt(4);//bill count
	    totalNetTotal = totalNetTotal + rsSettleManager.getInt(7);//netTotal
	}
	rsSettleManager.close();

	sbSqlQFile.setLength(0);
	sbSqlQFile.append(" SELECT sum(a.dblDiscountAmt),sum(a.dblRoundOff),sum(a.dblTipAmount),count(*),sum(a.dblSubTotal),sum(a.dblTaxAmt),sum(a.dblSubTotal)-sum(a.dblDiscountAmt)as netTotal "
		+ " from tblqbillhd a "
		+ " where date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		+ " ");
	if (!POSCode.equalsIgnoreCase("All"))
	{
	    sbSqlQFile.append(" and a.strPOSCode='" + POSCode + "' ");
	}
	System.out.println(sbSqlQFile);

	rsSettleManager = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlQFile.toString());
	while (rsSettleManager.next())
	{
	    totalDiscAmt = totalDiscAmt + rsSettleManager.getDouble(1);//discAmt
	    totalRoundOffAmt = totalRoundOffAmt + rsSettleManager.getDouble(2);//roundOff
	    totalTipAmt = totalTipAmt + rsSettleManager.getDouble(3);//tipAmt
	    totalBills = totalBills + rsSettleManager.getInt(4);//bill count
	    totalNetTotal = totalNetTotal + rsSettleManager.getInt(7);//netTotal
	}
	rsSettleManager.close();

	if (hmSettlementWiseData.size() > 0)
	{
	    clsUtility objUtility = new clsUtility();
	    int cntLine = 0;

	    Map<String, Double> mapVerticalTotal = new HashMap<String, Double>();
	    //Map<Integer ,String> mapSequence=new TreeMap<Integer,String>();
	    double settleAmt = 0, roundOff = 0, tipAmt = 0, discountAmt = 0;
	    //int noOfBills=0;
	    double totalVertSettleAmt = 0, totalVertTipAmt = 0, totalVertTotalAmt = 0, totalVerNoOfBill = 0, totalVerRndOff = 0, totalVertDiscAmt = 0, totalVertNetTotalAmt = 0;

	    cntLine += 20;
	    cntLine += 20;
	    cntLine += 27;
	    pw.print(objUtility.funPrintTextWithAlignment("Settle Name|", 20, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("Net Total|", 20, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("Settle Amt|", 27, "Right"));

	    for (Map.Entry<Integer, String> entry : mapTaxHeaders.entrySet())
	    {
		String sql = "select strTaxDesc,strTaxShortName from tbltaxhd where strTaxCode='" + entry.getValue() + "' ";
		ResultSet rsTaxDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		while (rsTaxDtl.next())
		{
		    pw.print(objUtility.funPrintTextWithAlignment(rsTaxDtl.getString(2) + "|", 27, "Right"));
		    cntLine += 27;
		}
	    }

	    cntLine += 20;
	    cntLine += 10;
	    cntLine += 27;
	    cntLine += 10;
	    cntLine += 10;

	    pw.print(objUtility.funPrintTextWithAlignment("Discount|", 20, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("Rnd.Off|", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("Total|", 27, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("Tip|", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("No Of Bill|", 10, "Right"));
	    pw.println();

	    //pw.println(headerText+"      "+"Rnd.Off"+"      "+"Total"+"      "+"Tip"+"      "+"No Of Bill");
	    //pw.println("--------------------------------------------------------------------------------------------------------");
	    for (int cn = 0; cn < cntLine; cn++)
	    {
		pw.print("-");
	    }
	    pw.println();

	    for (Map.Entry<String, clsManagerReportBean> entry : hmSettlementWiseData.entrySet())
	    {
		clsManagerReportBean objSettlementRow = entry.getValue();

		String settlemetCode = objSettlementRow.getStrSettlementCode();
		String settlemetDesc = objSettlementRow.getStrSettlementDesc();
		double settlementAmt = objSettlementRow.getDblSettlementAmt();

		//write settlements
		pw.print(objUtility.funPrintTextWithAlignment(settlemetDesc + "|", 20, "Right"));

		double settlementNetTotal = (totalNetTotal / totalSettleAmt) * settlementAmt;
		pw.print(objUtility.funPrintTextWithAlignment(String.valueOf(decFormatter.format(settlementNetTotal)) + "|", 20, "Right"));

		pw.print(objUtility.funPrintTextWithAlignment(String.valueOf(decFormatter.format(settlementAmt)) + "|", 27, "Right"));

		//write taxes
		for (Map.Entry<Integer, String> taxEntry : mapTaxHeaders.entrySet())
		{
		    String taxCode = taxEntry.getValue();
		    double taxAmt = mapTaxWiseData.get(taxCode);

		    double settlementTaxAmt = (settlementAmt / totalSettleAmt) * taxAmt;

		    pw.print(objUtility.funPrintTextWithAlignment(String.valueOf(decFormatter.format(settlementTaxAmt)) + "|", 27, "Right"));

		    objSettlementRow.setDblTaxAmt(settlementTaxAmt);

		}

		double settlementDiscAmt = (settlementAmt / totalSettleAmt) * totalDiscAmt;
		double settlementRoundOffAmt = (settlementAmt / totalSettleAmt) * totalRoundOffAmt;
		double settlementTipAmt = (settlementAmt / totalSettleAmt) * totalTipAmt;
		double settlementBillCount = (settlementAmt / totalSettleAmt) * totalBills;

		pw.print(objUtility.funPrintTextWithAlignment(String.valueOf(decFormatter.format(settlementDiscAmt)) + "|", 20, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(String.valueOf(decFormatter.format(settlementRoundOffAmt)) + "|", 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(String.valueOf(decFormatter.format(settlementAmt)) + "|", 27, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(String.valueOf(decFormatter.format(settlementTipAmt)) + "|", 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(String.valueOf(decFormatter.format(settlementBillCount)) + "|", 10, "Right"));
		pw.println();
	    }

	    for (int cn = 0; cn < cntLine; cn++)
	    {
		pw.print("-");
	    }
	    pw.println();

	    pw.print(objUtility.funPrintTextWithAlignment("Total|", 20, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment(String.valueOf(decFormatter.format(totalNetTotal)) + "|", 20, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment(String.valueOf(decFormatter.format(totalSettleAmt)) + "|", 27, "Right"));

	    for (Map.Entry<Integer, String> entry : mapTaxHeaders.entrySet())
	    {
		System.out.println(entry.getKey() + "   " + entry.getValue());
		String taxCode = entry.getValue();
		pw.print(objUtility.funPrintTextWithAlignment(String.valueOf(decFormatter.format(mapTaxWiseData.get(taxCode))) + "|", 27, "Right"));
	    }

	    pw.print(objUtility.funPrintTextWithAlignment(String.valueOf(decFormatter.format(totalDiscAmt)) + "|", 20, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment(String.valueOf(decFormatter.format(totalRoundOffAmt)) + "|", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment(String.valueOf(decFormatter.format(totalSettleAmt)) + "|", 27, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment(String.valueOf(decFormatter.format(totalTipAmt)) + "|", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment(String.valueOf(Math.rint(totalBills)) + "|", 10, "Right"));

	    pw.println();
	    pw.println();
	}

	sbSqlQFile = null;

	return 1;
    }

// Operation Type Wise Data
    private int funOperationTypeWiseData(String fromDate, String toDate, String POSCode, PrintWriter pw) throws Exception
    {
	pw.println();
	pw.println();
	pw.println("SALE BY OPEARTION TYPE");
	pw.println("---------------------------");

	clsUtility objUtility = new clsUtility();
	int cntLine = 0;
	StringBuilder sbSqlLiveFile = new StringBuilder();
	StringBuilder sbSqlQFile = new StringBuilder();
	sbSqlLiveFile.setLength(0);
	sbSqlQFile.setLength(0);
	StringBuilder sbSql = new StringBuilder();
	sbSql.setLength(0);

	Map<Integer, String> mapTaxHeaders = new TreeMap<Integer, String>();
	int cntTax = 1;
	String sqlTax = "select c.strTaxCode "
		+ " from tblbillhd a,tblbilltaxdtl b,tbltaxhd c "
		+ " where a.strBillNo=b.strBillNo "
		+ " and date(a.dteBillDate)=date(b.dteBillDate) "
		+ " and b.strTaxCode=c.strTaxCode "
		+ " and a.strClientCode=b.strClientCode "
		+ " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' ";
	if (!POSCode.equalsIgnoreCase("All"))
	{
	    sqlTax += " and a.strPOSCode='" + POSCode + "' ";
	}
	sqlTax += " group by c.strTaxCode";
	ResultSet rsTaxDtl1 = clsGlobalVarClass.dbMysql.executeResultSet(sqlTax);
	while (rsTaxDtl1.next())
	{
	    mapTaxHeaders.put(cntTax, rsTaxDtl1.getString(1));
	    cntTax++;
	}
	rsTaxDtl1.close();

	sqlTax = "select c.strTaxCode "
		+ " from tblqbillhd a,tblqbilltaxdtl b,tbltaxhd c "
		+ " where a.strBillNo=b.strBillNo "
		+ " and date(a.dteBillDate)=date(b.dteBillDate) "
		+ " and b.strTaxCode=c.strTaxCode "
		+ " and a.strClientCode=b.strClientCode "
		+ " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' ";
	if (!POSCode.equalsIgnoreCase("All"))
	{
	    sqlTax += " and a.strPOSCode='" + POSCode + "' ";
	}
	sqlTax += " group by c.strTaxCode";
	rsTaxDtl1 = clsGlobalVarClass.dbMysql.executeResultSet(sqlTax);
	while (rsTaxDtl1.next())
	{
	    if (!mapTaxHeaders.containsValue(rsTaxDtl1.getString(1)))
	    {
		mapTaxHeaders.put(cntTax, rsTaxDtl1.getString(1));
		cntTax++;
	    }
	}
	rsTaxDtl1.close();

	Map<String, Map<String, Double>> hmOperationWiseData = new HashMap<String, Map<String, Double>>();

	sbSqlLiveFile.append(" select a.strOperationType,sum(b.dblSettlementAmt),sum(a.dblSubTotal)-sum(a.dblDiscountAmt) "
		+ " from tblbillhd a,tblbillsettlementdtl b,tblsettelmenthd c "
		+ " where a.strBillNo=b.strBillNo "
		+ " and date(a.dteBillDate)=date(b.dteBillDate) "
		+ " and b.strSettlementCode=c.strSettelmentCode "
		+ " and a.strClientCode=b.strClientCode "
		+ " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "'  ");//and a.strSettelmentMode!='MultiSettle'
	if (!POSCode.equalsIgnoreCase("All"))
	{
	    sbSqlLiveFile.append(" and a.strPOSCode='" + POSCode + "' ");
	}
	sbSqlLiveFile.append(" group by a.strOperationType ");
	System.out.println(sbSqlLiveFile);
	ResultSet rsOpTypeWise = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlLiveFile.toString());
	while (rsOpTypeWise.next())
	{
	    double totalAmt = 0, totalTaxAmt = 0;
	    Map<String, Double> hmOperationWiseDtlData = new HashMap<String, Double>();
	    String operationType = rsOpTypeWise.getString(1);

	    sbSql.setLength(0);
	    sbSql.append("select a.strOperationType,sum(b.dblTaxAmount),c.strTaxCode,c.strTaxDesc "
		    + " from tblbillhd a,tblbilltaxdtl b,tbltaxhd c,tblbillsettlementdtl d "
		    + " where a.strBillNo=b.strBillNo "
		    + " and date(a.dteBillDate)=date(b.dteBillDate) "
		    + " and a.strBillNo=d.strBillNo "
		    + " and b.strTaxCode=c.strTaxCode "
		    + " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and a.strClientCode=b.strClientCode and a.strClientCode=d.strClientCode "
		    + " and a.strOperationType='" + operationType + "'  ");//and a.strSettelmentMode!='MultiSettle'
	    if (!POSCode.equalsIgnoreCase("All"))
	    {
		sbSql.append(" and a.strPOSCode='" + POSCode + "' ");
	    }
	    sbSql.append(" group by c.strTaxCode");
	    System.out.println(sbSql);
	    ResultSet rsOpTypeTaxWise = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
	    while (rsOpTypeTaxWise.next())
	    {
		hmOperationWiseDtlData.put(rsOpTypeTaxWise.getString(3), rsOpTypeTaxWise.getDouble(2));
		totalTaxAmt += rsOpTypeTaxWise.getDouble(2);
		//mapTaxHeaders.put(cntTax,rsOpTypeTaxWise.getString(3));
		//cntTax++;
	    }
	    rsOpTypeTaxWise.close();

	    double discAmt = 0;
	    sbSql.setLength(0);
	    sbSql.append("select sum(a.dblDiscountAmt) "
		    + " from tblbillhd a "
		    + " where date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and a.strSettelmentMode!='MultiSettle' and a.strOperationType='" + operationType + "'");
	    if (!POSCode.equalsIgnoreCase("All"))
	    {
		sbSql.append(" and a.strPOSCode='" + POSCode + "' ");
	    }
	    ResultSet rsDiscount = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
	    while (rsDiscount.next())
	    {
		discAmt = rsDiscount.getDouble(1);
		hmOperationWiseDtlData.put("Discount", rsDiscount.getDouble(1));
	    }
	    rsDiscount.close();

	    sbSql.setLength(0);
	    sbSql.append("select count(*),strOperationType "
		    + " from tblbillhd a "
		    + " where date(dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and strOperationType='" + operationType + "' ");//and a.strSettelmentMode!='MultiSettle' 
	    if (!POSCode.equalsIgnoreCase("All"))
	    {
		sbSql.append(" and a.strPOSCode='" + POSCode + "' ");
	    }
	    sbSql.append(" group by a.strOperationType");
	    ResultSet rsOpTypeNoOfBills = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
	    while (rsOpTypeNoOfBills.next())
	    {
		hmOperationWiseDtlData.put("No Of Bills", rsOpTypeNoOfBills.getDouble(1));
	    }
	    rsOpTypeNoOfBills.close();

	    double operationTypeAmt = rsOpTypeWise.getDouble(3);
	    totalAmt = operationTypeAmt + totalTaxAmt;
	    totalAmt = (operationTypeAmt + totalTaxAmt);

	    hmOperationWiseDtlData.put("OpTypeAmt", operationTypeAmt);
	    hmOperationWiseDtlData.put("RoundOff", 0.00);
	    hmOperationWiseDtlData.put("TotalAmt", totalAmt);

	    hmOperationWiseData.put(operationType, hmOperationWiseDtlData);
	}
	rsOpTypeWise.close();

	sbSqlQFile.append(" select a.strOperationType,sum(a.dblSubTotal)-sum(a.dblDiscountAmt) "
		+ " from tblqbillhd a "
		+ " where date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		+ "  ");//and a.strSettelmentMode!='MultiSettle'
	if (!POSCode.equalsIgnoreCase("All"))
	{
	    sbSqlQFile.append(" and a.strPOSCode='" + POSCode + "' ");
	}
	sbSqlQFile.append(" group by a.strOperationType ");
	rsOpTypeWise = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlQFile.toString());
	while (rsOpTypeWise.next())
	{
	    double totalHorizontalAmt = 0, totalTaxAmt = 0;
	    Map<String, Double> hmOperationWiseDtlData = new HashMap<String, Double>();
	    String operationType = rsOpTypeWise.getString(1);
	    if (hmOperationWiseData.containsKey(operationType))
	    {
		hmOperationWiseDtlData = hmOperationWiseData.get(operationType);
	    }

	    sbSql.setLength(0);
	    sbSql.append("select a.strOperationType,sum(b.dblTaxAmount),c.strTaxCode,c.strTaxDesc "
		    + " from tblqbillhd a,tblqbilltaxdtl b,tbltaxhd c "
		    + " where a.strBillNo=b.strBillNo "
		    + " and date(a.dteBillDate)=date(b.dteBillDate) "
		    + " and b.strTaxCode=c.strTaxCode "
		    + " and a.strClientCode=b.strClientCode "
		    + " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and a.strOperationType='" + operationType + "' ");// and a.strSettelmentMode!='MultiSettle'
	    if (!POSCode.equalsIgnoreCase("All"))
	    {
		sbSql.append(" and a.strPOSCode='" + POSCode + "' ");
	    }
	    sbSql.append(" group by c.strTaxCode");
	    System.out.println(sbSql);
	    ResultSet rsOpTypeTaxWise = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
	    while (rsOpTypeTaxWise.next())
	    {
		if (hmOperationWiseData.containsKey(operationType))
		{
		    if (hmOperationWiseDtlData.containsKey(rsOpTypeTaxWise.getString(3)))
		    {
			hmOperationWiseDtlData.put(rsOpTypeTaxWise.getString(3), hmOperationWiseDtlData.get(rsOpTypeTaxWise.getString(3)) + Math.rint(rsOpTypeTaxWise.getDouble(2)));
		    }
		    else
		    {
			hmOperationWiseDtlData.put(rsOpTypeTaxWise.getString(3), Math.rint(rsOpTypeTaxWise.getDouble(2)));
		    }
		}
		else
		{
		    hmOperationWiseDtlData.put(rsOpTypeTaxWise.getString(3), Math.rint(rsOpTypeTaxWise.getDouble(2)));
		}
		totalTaxAmt += rsOpTypeTaxWise.getDouble(2);
		//mapTaxHeaders.put(cntTax,rsOpTypeTaxWise.getString(3));
		//cntTax++;
	    }
	    rsOpTypeTaxWise.close();

	    double discAmt = 0;
	    sbSql.setLength(0);
	    sbSql.append("select sum(a.dblDiscountAmt) "
		    + " from tblqbillhd a "
		    + " where date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + "  and a.strOperationType='" + operationType + "'");//and a.strSettelmentMode!='MultiSettle'
	    if (!POSCode.equalsIgnoreCase("All"))
	    {
		sbSql.append(" and a.strPOSCode='" + POSCode + "' ");
	    }
	    ResultSet rsDiscount = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
	    while (rsDiscount.next())
	    {
		discAmt = rsDiscount.getDouble(1);
		if (hmOperationWiseData.containsKey(operationType))
		{
		    hmOperationWiseDtlData = hmOperationWiseData.get(operationType);
		    hmOperationWiseDtlData.put("Discount", hmOperationWiseDtlData.get("Discount") + rsDiscount.getDouble(1));
		}
		else
		{
		    hmOperationWiseDtlData.put("Discount", rsDiscount.getDouble(1));
		}
	    }
	    rsDiscount.close();

	    sbSql.setLength(0);
	    sbSql.append("select count(*),strOperationType "
		    + " from tblqbillhd "
		    + " where date(dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and strOperationType='" + operationType + "' ");
	    if (!POSCode.equalsIgnoreCase("All"))
	    {
		sbSql.append(" and strPOSCode='" + POSCode + "' ");
	    }
	    sbSql.append(" group by strOperationType");
	    ResultSet rsOpTypeNoOfBills = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
	    while (rsOpTypeNoOfBills.next())
	    {
		hmOperationWiseDtlData.put("No Of Bills", rsOpTypeNoOfBills.getDouble(1));
	    }
	    rsOpTypeNoOfBills.close();

	    System.out.println("Op=" + rsOpTypeWise.getDouble(2) + "\tTax=" + totalTaxAmt);
	    double operationTypeAmt = rsOpTypeWise.getDouble(2);
	    totalHorizontalAmt = (operationTypeAmt + totalTaxAmt);

	    if (hmOperationWiseDtlData.containsKey("OpTypeAmt"))
	    {
		hmOperationWiseDtlData.put("OpTypeAmt", Math.rint(operationTypeAmt + hmOperationWiseDtlData.get("OpTypeAmt")));
	    }
	    else
	    {
		hmOperationWiseDtlData.put("OpTypeAmt", Math.rint(operationTypeAmt));
	    }

	    if (hmOperationWiseDtlData.containsKey("TotalAmt"))
	    {
		hmOperationWiseDtlData.put("TotalAmt", Math.rint(totalHorizontalAmt + hmOperationWiseDtlData.get("TotalAmt")));
	    }
	    else
	    {
		hmOperationWiseDtlData.put("TotalAmt", Math.rint(totalHorizontalAmt));
	    }

	    hmOperationWiseDtlData.put("RoundOff", 0.00);

	    hmOperationWiseData.put(operationType, hmOperationWiseDtlData);
	}
	rsOpTypeWise.close();

	Map<String, Double> mapVerticalTotal = new HashMap<String, Double>();
	//Map<Integer ,String> mapSequence=new TreeMap<Integer,String>();
	cntLine += 20;
	cntLine += 27;
	pw.print(objUtility.funPrintTextWithAlignment("Operation Type|", 20, "Right"));
	pw.print(objUtility.funPrintTextWithAlignment("Amount|", 27, "Right"));

	for (Map.Entry<Integer, String> entry : mapTaxHeaders.entrySet())
	{
	    String sql = "select strTaxShortName from tbltaxhd where strTaxCode='" + entry.getValue() + "' ";
	    ResultSet rsTaxDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsTaxDtl.next())
	    {
		pw.print(objUtility.funPrintTextWithAlignment(rsTaxDtl.getString(1) + "|", 27, "Right"));
		cntLine += 27;
	    }
	}

	cntLine += 20;
	cntLine += 10;
	cntLine += 27;
	cntLine += 10;

	pw.print(objUtility.funPrintTextWithAlignment("Discount|", 20, "Right"));
	pw.print(objUtility.funPrintTextWithAlignment("Rnd.Off|", 10, "Right"));
	pw.print(objUtility.funPrintTextWithAlignment("Total|", 27, "Right"));
	pw.print(objUtility.funPrintTextWithAlignment("No Of Bill|", 10, "Right"));
	pw.println();

	for (int cn = 0; cn < cntLine; cn++)
	{
	    pw.print("-");
	}
	pw.println();

	double totalOpTypeAmt = 0, totalNoOfBills = 0, totalRndOffAmt = 0, totalAmount = 0, totalDiscAmt = 0, finalTotalAmt = 0;
	for (Map.Entry<String, Map<String, Double>> entry : hmOperationWiseData.entrySet())
	{
	    System.out.println(entry.getKey());
	    pw.print(objUtility.funPrintTextWithAlignment(entry.getKey() + "|", 20, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment(decFormatter.format(entry.getValue().get("OpTypeAmt")) + "|", 27, "Right"));
	    totalOpTypeAmt += entry.getValue().get("OpTypeAmt");

	    double totalTaxAmt = 0;
	    int count = 1;
	    for (Map.Entry<Integer, String> entryTax : mapTaxHeaders.entrySet())
	    {
		if (entry.getValue().containsKey(entryTax.getValue()))
		{
		    pw.print(objUtility.funPrintTextWithAlignment(decFormatter.format(entry.getValue().get(entryTax.getValue())) + "|", 27, "Right"));
		    totalTaxAmt += entry.getValue().get(entryTax.getValue());

		    if (mapVerticalTotal.containsKey(entryTax.getValue()))
		    {
			double tempTaxAmt = mapVerticalTotal.get(entryTax.getValue());
			mapVerticalTotal.put(entryTax.getValue(), entry.getValue().get(entryTax.getValue()) + tempTaxAmt);
		    }
		    else
		    {
			mapVerticalTotal.put(entryTax.getValue(), entry.getValue().get(entryTax.getValue()));
		    }
		    //mapSequence.put(count,entryTax.getValue());
		    count++;
		}
		else
		{
		    pw.print(objUtility.funPrintTextWithAlignment("0.00|", 27, "Right"));
		}
	    }

	    totalDiscAmt += entry.getValue().get("Discount");
	    totalNoOfBills += entry.getValue().get("No Of Bills");
	    totalRndOffAmt += entry.getValue().get("RoundOff");
	    //totalAmount+=entry.getValue().get("TotalAmt");
	    totalAmount = (entry.getValue().get("OpTypeAmt") + totalTaxAmt);
	    finalTotalAmt += totalAmount;

	    pw.print(objUtility.funPrintTextWithAlignment(decFormatter.format(entry.getValue().get("Discount")) + "|", 20, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment(decFormatter.format(entry.getValue().get("RoundOff")) + "|", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment(decFormatter.format(totalAmount) + "|", 27, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment(entry.getValue().get("No Of Bills") + "|", 10, "Right"));
	    pw.println();
	}

	mapVerticalTotal.put("TotalDiscAmt", totalDiscAmt);
	mapVerticalTotal.put("TotalOpTypeAmt", totalOpTypeAmt);
	mapVerticalTotal.put("TotalNoOfBills", totalNoOfBills);
	mapVerticalTotal.put("TotalRndOffAmt", totalRndOffAmt);
	mapVerticalTotal.put("TotalAmount", finalTotalAmt);

	for (int cn = 0; cn < cntLine; cn++)
	{
	    pw.print("-");
	}
	pw.println();

	pw.print(objUtility.funPrintTextWithAlignment("Total|", 20, "Right"));
	pw.print(objUtility.funPrintTextWithAlignment(String.valueOf(decFormatter.format(mapVerticalTotal.get("TotalOpTypeAmt"))) + "|", 27, "Right"));

	for (Map.Entry<Integer, String> entry : mapTaxHeaders.entrySet())
	{
	    System.out.println(entry.getKey() + "   " + entry.getValue());
	    pw.print(objUtility.funPrintTextWithAlignment(String.valueOf(decFormatter.format(mapVerticalTotal.get(entry.getValue()))) + "|", 27, "Right"));
	}

	pw.print(objUtility.funPrintTextWithAlignment(String.valueOf(decFormatter.format(mapVerticalTotal.get("TotalDiscAmt"))) + "|", 20, "Right"));
	pw.print(objUtility.funPrintTextWithAlignment(String.valueOf(decFormatter.format(mapVerticalTotal.get("TotalRndOffAmt"))) + "|", 10, "Right"));
	pw.print(objUtility.funPrintTextWithAlignment(String.valueOf(decFormatter.format(mapVerticalTotal.get("TotalAmount"))) + "|", 27, "Right"));
	pw.print(objUtility.funPrintTextWithAlignment(String.valueOf(decFormatter.format(mapVerticalTotal.get("TotalNoOfBills"))) + "|", 10, "Right"));

	pw.println();
	pw.println();

	return 1;
    }

// Operation Type Wise Group Wise Taxation Data
    private int funOpTypeWiseGroupWiseTaxationData(String fromDate, String toDate, String POSCode, PrintWriter pw) throws Exception
    {
	pw.println();
	pw.println();
	pw.println("SALE BY OPERATION TYPE TAXATION BIFURCATION");
	pw.println("---------------------------------------------");

	clsUtility objUtility = new clsUtility();
	int cntLine = 0;
	StringBuilder sbSqlLiveFile = new StringBuilder();
	StringBuilder sbSqlQFile = new StringBuilder();
	sbSqlLiveFile.setLength(0);
	sbSqlQFile.setLength(0);
	StringBuilder sbSql = new StringBuilder();

	Map<Integer, String> mapTaxHeaders = new TreeMap<Integer, String>();

	int cntTax = 1;
	String sqlTax = "select c.strTaxCode "
		+ " from tblbillhd a,tblbilltaxdtl b,tbltaxhd c "
		+ " where a.strBillNo=b.strBillNo "
		+ " and date(a.dteBillDate)=date(b.dteBillDate) "
		+ " and b.strTaxCode=c.strTaxCode "
		+ " and a.strClientCode=b.strClientCode "
		+ " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' ";
	if (!POSCode.equalsIgnoreCase("All"))
	{
	    sqlTax += " and a.strPOSCode='" + POSCode + "' ";
	}
	sqlTax += " group by c.strTaxCode";
	ResultSet rsTaxDtl1 = clsGlobalVarClass.dbMysql.executeResultSet(sqlTax);
	while (rsTaxDtl1.next())
	{
	    mapTaxHeaders.put(cntTax, rsTaxDtl1.getString(1));
	    cntTax++;
	}
	rsTaxDtl1.close();

	sqlTax = "select c.strTaxCode "
		+ " from tblqbillhd a,tblqbilltaxdtl b,tbltaxhd c "
		+ " where a.strBillNo=b.strBillNo "
		+ " and date(a.dteBillDate)=date(b.dteBillDate) "
		+ " and b.strTaxCode=c.strTaxCode "
		+ " and a.strClientCode=b.strClientCode "
		+ " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' ";
	if (!POSCode.equalsIgnoreCase("All"))
	{
	    sqlTax += " and a.strPOSCode='" + POSCode + "' ";
	}
	sqlTax += " group by c.strTaxCode";
	rsTaxDtl1 = clsGlobalVarClass.dbMysql.executeResultSet(sqlTax);
	while (rsTaxDtl1.next())
	{
	    if (!mapTaxHeaders.containsValue(rsTaxDtl1.getString(1)))
	    {
		mapTaxHeaders.put(cntTax, rsTaxDtl1.getString(1));
		cntTax++;
	    }
	}
	rsTaxDtl1.close();

	Map<String, Map<String, Map<String, Double>>> hmOpTypeWiseGroupWiseData = new HashMap<String, Map<String, Map<String, Double>>>();

	sbSql.setLength(0);
	sbSql.append(" select a.strOperationType,sum(b.dblSettlementAmt),sum(a.dblSubTotal)-sum(a.dblDiscountAmt) "
		+ " from tblbillhd a,tblbillsettlementdtl b,tblsettelmenthd c "
		+ " where a.strBillNo=b.strBillNo "
		+ " and date(a.dteBillDate)=date(b.dteBillDate) "
		+ " and b.strSettlementCode=c.strSettelmentCode "
		+ " and a.strClientCode=b.strClientCode "
		+ " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' ");
	if (!POSCode.equalsIgnoreCase("All"))
	{
	    sbSql.append(" and a.strPOSCode='" + POSCode + "' ");
	}
	sbSql.append(" group by a.strOperationType ");
	ResultSet rsOpType = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
	while (rsOpType.next())
	{
	    String operationType = rsOpType.getString(1);
	    Map<String, Map<String, Double>> hmGroupWiseData = new HashMap<String, Map<String, Double>>();
	    if (hmOpTypeWiseGroupWiseData.containsKey(operationType))
	    {
		hmGroupWiseData = hmOpTypeWiseGroupWiseData.get(operationType);
	    }

	    sbSqlLiveFile.setLength(0);
	    sbSqlLiveFile.append(" select e.strGroupName,sum(b.dblAmount)-sum(b.dblDiscountAmt),e.strGroupCode, SUM(b.dblDiscountAmt) "
		    + " from tblbillhd a,tblbilldtl b,tblitemmaster c,tblsubgrouphd d,tblgrouphd e "
		    + " where a.strBillNo=b.strBillNo "
		    + " and date(a.dteBillDate)=date(b.dteBillDate) "
		    + " and b.strItemCode=c.strItemCode "
		    + " and c.strSubGroupCode=d.strSubGroupCode "
		    + " and d.strGroupCode=e.strGroupCode "
		    + " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and a.strClientCode=b.strClientCode "
		    + " and a.strOperationType='" + operationType + "' "
		    + "  ");
	    if (!POSCode.equalsIgnoreCase("All"))
	    {
		sbSqlLiveFile.append(" and a.strPOSCode='" + POSCode + "' ");
	    }
	    sbSqlLiveFile.append(" group by e.strGroupCode ");
	    System.out.println(sbSqlLiveFile);
	    ResultSet rsGroupWise = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlLiveFile.toString());
	    while (rsGroupWise.next())
	    {
		Map<String, Double> hmGroupWiseDtlData = new HashMap<String, Double>();
		double groupAmt = rsGroupWise.getDouble(2);
		double groupDiscAmt = rsGroupWise.getDouble(4);

		sbSql.setLength(0);
		sbSql.append("select f.strGroupName,sum(c.dblAmount)-sum(c.dblDiscAmt),f.strGroupCode, SUM(c.dblDiscAmt) "
			+ " from tblbillhd a,tblbillmodifierdtl c,tblitemmaster d,tblsubgrouphd e,tblgrouphd f "
			+ " where a.strBillNo=c.strBillNo "
			+ " and date(a.dteBillDate)=date(c.dteBillDate) "
			+ " and left(c.strItemCode,7)=d.strItemCode "
			+ " and d.strSubGroupCode=e.strSubGroupCode "
			+ " and e.strGroupCode=f.strGroupCode "
			+ " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
			+ " and f.strGroupCode='" + rsGroupWise.getString(3) + "' "
			+ " and a.strOperationType='" + operationType + "' "
			+ " and a.strClientCode=c.strClientCode "
			+ "   ");
		if (!POSCode.equalsIgnoreCase("All"))
		{
		    sbSql.append(" and a.strPOSCode='" + POSCode + "' ");
		}
		sbSql.append(" group by f.strGroupCode");
		ResultSet rsGroupModData = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
		if (rsGroupModData.next())
		{
		    groupAmt += rsGroupModData.getDouble(2);
		    groupDiscAmt += rsGroupModData.getDouble(2);
		}
		rsGroupModData.close();

		if (hmGroupWiseData.containsKey(rsGroupWise.getString(1)))
		{
		    hmGroupWiseDtlData.put("Amount", hmGroupWiseDtlData.get("Amount") + groupAmt);
		}
		else
		{
		    hmGroupWiseDtlData.put("Amount", groupAmt);
		}

		if (hmGroupWiseData.containsKey(rsGroupWise.getString(1)))
		{
		    hmGroupWiseDtlData.put("Discount", hmGroupWiseDtlData.get("Discount") + groupDiscAmt);
		}
		else
		{
		    hmGroupWiseDtlData.put("Discount", groupDiscAmt);
		}

		Map<String, String> hmTaxDtl = new HashMap<String, String>();
		sbSql.setLength(0);
		sbSql.append("select sum(b.dblTaxAmount),sum(b.dblTaxableAmount),c.strTaxCode,c.strTaxIndicator"
			+ ",c.strTaxOnTax,c.strTaxOnTaxCode,c.strOperationType,c.dblPercent "
			+ " from tblbillhd a,tblbilltaxdtl b,tbltaxhd c "
			+ " where a.strBillNo=b.strBillNo "
			+ " and date(a.dteBillDate)=date(b.dteBillDate) "
			+ " and b.strTaxCode=c.strTaxCode "
			+ " and a.strOperationType='" + operationType + "' "
			+ " and a.strClientCode=b.strClientCode "
			+ " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
			+ "  ");
		if (!POSCode.equalsIgnoreCase("All"))
		{
		    sbSql.append(" and a.strPOSCode='" + POSCode + "' ");
		}
		sbSql.append(" group by b.strTaxCode order by c.strTaxOnTax");
		ResultSet rsTax = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
		while (rsTax.next())
		{
		    String taxCode = rsTax.getString(3);
		    double taxPer = rsTax.getDouble(8);

		    String sql = "select a.strTaxCode,a.strTaxDesc,b.strGroupCode,b.strGroupName,b.strApplicable "
			    + "from tbltaxhd a,tbltaxongroup b,tbltaxposdtl c "
			    + "where a.strTaxCode=b.strTaxCode "
			    + "and a.strTaxCode=c.strTaxCode ";
		    if (!POSCode.equalsIgnoreCase("All"))
		    {
			sql = sql + " and c.strPOSCode='" + POSCode + "' ";
		    }
		    sql += "and b.strGroupCode='" + rsGroupWise.getString(3) + "' "
			    + "and a.strTaxCode='" + rsTax.getString(3) + "' "
			    + "and b.strApplicable='true' "
			    + "group by a.strTaxCode,b.strGroupCode";
		    ResultSet rsTaxOnGroup = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		    if (rsTaxOnGroup.next())
		    {
			double taxAmt = rsTax.getDouble(1);
			double taxableAmt = rsTax.getDouble(2);
			if (!rsTax.getString(4).isEmpty())
			{
			    double groupWiseTaxIndAmt = 0.00;

			    sbSql.setLength(0);
			    sbSql.append("select e.strGroupName,e.strGroupCode,sum(b.dblAmount)-sum(b.dblDiscountAmt) "
				    + " from tblbillhd a,tblbilldtl b,tblitemmaster c,tblsubgrouphd d,tblgrouphd e "
				    + " where a.strBillNo=b.strBillNo "
				    + " and date(a.dteBillDate)=date(b.dteBillDate) "
				    + " and b.strItemCode=c.strItemCode "
				    + " and c.strSubGroupCode=d.strSubGroupCode "
				    + " and d.strGroupCode=e.strGroupCode "
				    + " and c.strTaxIndicator='" + rsTax.getString(4) + "' "
				    + " and e.strGroupCode='" + rsGroupWise.getString(3) + "' "
				    + " and a.strClientCode=b.strClientCode "
				    + " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
				    + " and a.strAreaCode in " + funGetAreaCodes(rsTax.getString(3)) + " "
				    + " and a.strBillNo in (select strBillNo from tblbilltaxdtl) ");
			    if (!POSCode.equalsIgnoreCase("All"))
			    {
				sbSql.append(" and a.strPOSCode='" + POSCode + "' ");
			    }
			    sbSql.append(" group by e.strGroupCode");
			    ResultSet rsGrpWiseTax = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
			    if (rsGrpWiseTax.next())
			    {
				double tempGroupWiseTaxIndAmt = rsGrpWiseTax.getDouble(3);
				groupWiseTaxIndAmt = rsGrpWiseTax.getDouble(3);

				if (rsTax.getString(5).equalsIgnoreCase("Yes"))
				{
				    String taxOnTaxCode = rsTax.getString(6);
				    String[] arrSpTaxonTaxCodes = taxOnTaxCode.split(",");
				    for (int cntTot = 0; cntTot < arrSpTaxonTaxCodes.length; cntTot++)
				    {
					if (hmTaxDtl.containsKey(arrSpTaxonTaxCodes[cntTot]))
					{
					    String taxDtl = hmTaxDtl.get(arrSpTaxonTaxCodes[cntTot]);
					    double totTaxAmt = Double.parseDouble(taxDtl.split("!")[1]);
					    double totTaxableAmt = Double.parseDouble(taxDtl.split("!")[0]);
					    if (totTaxableAmt > 0)
					    {
						double totTaxableIndAmt = (totTaxAmt / totTaxableAmt) * tempGroupWiseTaxIndAmt;
						groupWiseTaxIndAmt += totTaxableIndAmt;
					    }
					}
				    }
				}
			    }
			    rsGrpWiseTax.close();

			    //modifiers
			    sbSql.setLength(0);
			    sbSql.append("select e.strGroupName,e.strGroupCode,sum(b.dblAmount)-sum(b.dblDiscAmt) "
				    + " from tblbillhd a,tblbillmodifierdtl b,tblitemmaster c,tblsubgrouphd d,tblgrouphd e "
				    + " where a.strBillNo=b.strBillNo "
				    + " and date(a.dteBillDate)=date(b.dteBillDate) "
				    + " AND left(b.strItemCode,7)=c.strItemCode  "
				    + " and c.strSubGroupCode=d.strSubGroupCode "
				    + " and d.strGroupCode=e.strGroupCode "
				    + " and c.strTaxIndicator='" + rsTax.getString(4) + "' "
				    + " and e.strGroupCode='" + rsGroupWise.getString(3) + "' "
				    + " and a.strClientCode=b.strClientCode "
				    + " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
				    + " and a.strAreaCode in " + funGetAreaCodes(rsTax.getString(3)) + " "
				    + " and a.strBillNo in (select strBillNo from tblbilltaxdtl) ");
			    if (!POSCode.equalsIgnoreCase("All"))
			    {
				sbSql.append(" and a.strPOSCode='" + POSCode + "' ");
			    }
			    sbSql.append(" group by e.strGroupCode");
			    rsGrpWiseTax = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
			    if (rsGrpWiseTax.next())
			    {
				double tempGroupWiseTaxIndAmt = rsGrpWiseTax.getDouble(3);
				groupWiseTaxIndAmt += rsGrpWiseTax.getDouble(3);

				if (rsTax.getString(5).equalsIgnoreCase("Yes"))
				{
				    String taxOnTaxCode = rsTax.getString(6);
				    String[] arrSpTaxonTaxCodes = taxOnTaxCode.split(",");
				    for (int cntTot = 0; cntTot < arrSpTaxonTaxCodes.length; cntTot++)
				    {
					if (hmTaxDtl.containsKey(arrSpTaxonTaxCodes[cntTot]))
					{
					    String taxDtl = hmTaxDtl.get(arrSpTaxonTaxCodes[cntTot]);
					    double totTaxAmt = Double.parseDouble(taxDtl.split("!")[1]);
					    double totTaxableAmt = Double.parseDouble(taxDtl.split("!")[0]);
					    if (totTaxableAmt > 0)
					    {
						double totTaxableIndAmt = (totTaxAmt / totTaxableAmt) * tempGroupWiseTaxIndAmt;
						groupWiseTaxIndAmt += totTaxableIndAmt;
					    }
					}
				    }
				}
			    }
			    rsGrpWiseTax.close();

			    double groupWiseTaxAmt = (taxPer / 100) * groupWiseTaxIndAmt;

			    System.out.println("Tax=" + rsTax.getString(3) + "\tGrp Wise Taxable Amt=" + groupWiseTaxIndAmt + "\tTax Amt=" + taxAmt + "\tTaxable Amt=" + taxableAmt + "\tGrp Tax Amt=" + groupWiseTaxAmt + "\tOperation Type=" + operationType + "\tGroup=" + rsGroupWise.getString(1));

			    hmTaxDtl.put(rsTax.getString(3), groupWiseTaxIndAmt + "!" + groupWiseTaxAmt);
			    if (hmGroupWiseData.containsKey(rsGroupWise.getString(1)))
			    {
				hmGroupWiseDtlData = hmGroupWiseData.get(rsGroupWise.getString(1));
				hmGroupWiseDtlData.put(rsTax.getString(3), hmGroupWiseDtlData.get(rsTax.getString(3)) + groupWiseTaxAmt);
			    }
			    else
			    {
				hmGroupWiseDtlData.put(rsTax.getString(3), groupWiseTaxAmt);
			    }
			}
			else
			{
			    sbSql.setLength(0);
			    sbSql.append("select e.strGroupName,e.strGroupCode,sum(b.dblAmount)-sum(b.dblDiscountAmt),sum(b.dblDiscountAmt) "
				    + " from tblbillhd a,tblbilldtl b,tblitemmaster c,tblsubgrouphd d,tblgrouphd e "
				    + " where a.strBillNo=b.strBillNo "
				    + " and date(a.dteBillDate)=date(b.dteBillDate) "
				    + " and b.strItemCode=c.strItemCode "
				    + " and c.strSubGroupCode=d.strSubGroupCode "
				    + " and d.strGroupCode=e.strGroupCode "
				    + " and a.strClientCode=b.strClientCode "
				    + " and e.strGroupCode='" + rsGroupWise.getString(3) + "' "
				    + " and a.strOperationType='" + operationType + "' "
				    + " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
				    + " and a.strAreaCode in " + funGetAreaCodes(rsTax.getString(3)) + " "
				    + " and a.strBillNo in (select strBillNo from tblbilltaxdtl)");
			    if (!POSCode.equalsIgnoreCase("All"))
			    {
				sbSql.append(" and a.strPOSCode='" + POSCode + "' ");
			    }
			    sbSql.append(" group by e.strGroupCode");
			    ResultSet rsGrpWiseTax = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());

			    double groupWiseTaxIndAmt = 0.00;
			    while (rsGrpWiseTax.next())
			    {
				groupWiseTaxIndAmt = rsGrpWiseTax.getDouble(3);
			    }
			    rsGrpWiseTax.close();

			    //modifiers                            
			    sbSql.setLength(0);
			    sbSql.append("select e.strGroupName,e.strGroupCode,sum(b.dblAmount)-sum(b.dblDiscAmt),sum(b.dblDiscAmt) "
				    + " from tblbillhd a,tblbillmodifierdtl b,tblitemmaster c,tblsubgrouphd d,tblgrouphd e "
				    + " where a.strBillNo=b.strBillNo "
				    + " and date(a.dteBillDate)=date(b.dteBillDate) "
				    + " AND left(b.strItemCode,7)=c.strItemCode  "
				    + " and c.strSubGroupCode=d.strSubGroupCode "
				    + " and d.strGroupCode=e.strGroupCode "
				    + " and a.strClientCode=b.strClientCode "
				    + " and e.strGroupCode='" + rsGroupWise.getString(3) + "' "
				    + " and a.strOperationType='" + operationType + "' "
				    + " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
				    + " and a.strAreaCode in " + funGetAreaCodes(rsTax.getString(3)) + " "
				    + " and a.strBillNo in (select strBillNo from tblbilltaxdtl)");
			    if (!POSCode.equalsIgnoreCase("All"))
			    {
				sbSql.append(" and a.strPOSCode='" + POSCode + "' ");
			    }
			    sbSql.append(" group by e.strGroupCode");
			    rsGrpWiseTax = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());

			    while (rsGrpWiseTax.next())
			    {
				groupWiseTaxIndAmt += rsGrpWiseTax.getDouble(3);
			    }
			    rsGrpWiseTax.close();

			    if (rsTax.getString(5).equalsIgnoreCase("Yes"))
			    {
				String taxOnTaxCode = rsTax.getString(6);
				String[] arrSpTaxonTaxCodes = taxOnTaxCode.split(",");
				for (int cntTot = 0; cntTot < arrSpTaxonTaxCodes.length; cntTot++)
				{
				    if (hmTaxDtl.containsKey(arrSpTaxonTaxCodes[cntTot]))
				    {
					String taxDtl = hmTaxDtl.get(arrSpTaxonTaxCodes[cntTot]);
					double totTaxAmt = Double.parseDouble(taxDtl.split("!")[1]);
					groupWiseTaxIndAmt += totTaxAmt;
				    }
				}
			    }

			    double groupWiseTaxAmt = (taxPer / 100) * groupWiseTaxIndAmt;
//                            if (taxableAmt > 0)
//                            {
//                                groupWiseTaxAmt = ((taxAmt / taxableAmt) * groupWiseTaxIndAmt);
//                            }
			    System.out.println("Tax=" + rsTax.getString(3) + "\tGrp Wise Taxable Amt=" + groupWiseTaxIndAmt + "\tTax Amt=" + taxAmt + "\tTaxable Amt=" + taxableAmt + "\tGrp Tax Amt=" + groupWiseTaxAmt + "\tOperation Type=" + operationType + "\tGroup=" + rsGroupWise.getString(1));

			    hmTaxDtl.put(rsTax.getString(3), groupWiseTaxIndAmt + "!" + groupWiseTaxAmt);
			    if (hmGroupWiseData.containsKey(rsGroupWise.getString(1)))
			    {
				hmGroupWiseDtlData = hmGroupWiseData.get(rsGroupWise.getString(1));
				hmGroupWiseDtlData.put(rsTax.getString(3), hmGroupWiseDtlData.get(rsTax.getString(3)) + groupWiseTaxAmt);
			    }
			    else
			    {
				hmGroupWiseDtlData.put(rsTax.getString(3), groupWiseTaxAmt);
			    }
			}
		    }
		}
		rsTax.close();

		hmGroupWiseData.put(rsGroupWise.getString(1), hmGroupWiseDtlData);
	    }
	    rsGroupWise.close();

	    hmOpTypeWiseGroupWiseData.put(operationType, hmGroupWiseData);
	}
	rsOpType.close();

	sbSql.setLength(0);
	sbSql.append(" select a.strOperationType,sum(b.dblSettlementAmt),sum(a.dblSubTotal)-sum(a.dblDiscountAmt) "
		+ " from tblqbillhd a,tblqbillsettlementdtl b,tblsettelmenthd c "
		+ " where a.strBillNo=b.strBillNo "
		+ " and date(a.dteBillDate)=date(b.dteBillDate) "
		+ " and b.strSettlementCode=c.strSettelmentCode "
		+ " and a.strClientCode=b.strClientCode "
		+ " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' ");
	if (!POSCode.equalsIgnoreCase("All"))
	{
	    sbSql.append(" and a.strPOSCode='" + POSCode + "' ");
	}
	sbSql.append(" group by a.strOperationType ");
	//System.out.println(sbSql);
	rsOpType = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
	while (rsOpType.next())
	{
	    String operationType = rsOpType.getString(1);
	    Map<String, Map<String, Double>> hmGroupWiseData = new HashMap<String, Map<String, Double>>();
	    if (hmOpTypeWiseGroupWiseData.containsKey(operationType))
	    {
		hmGroupWiseData = hmOpTypeWiseGroupWiseData.get(operationType);
	    }

	    sbSqlQFile.setLength(0);
	    sbSqlQFile.append(" select e.strGroupName,sum(b.dblAmount)-sum(b.dblDiscountAmt),e.strGroupCode, SUM(b.dblDiscountAmt) "
		    + " from tblqbillhd a,tblqbilldtl b,tblitemmaster c,tblsubgrouphd d,tblgrouphd e "
		    + " where a.strBillNo=b.strBillNo "
		    + " and date(a.dteBillDate)=date(b.dteBillDate) "
		    + " and b.strItemCode=c.strItemCode "
		    + " and c.strSubGroupCode=d.strSubGroupCode "
		    + " and d.strGroupCode=e.strGroupCode "
		    + " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and a.strClientCode=b.strClientCode "
		    + " and a.strOperationType='" + operationType + "' "
		    + "  ");
	    if (!POSCode.equalsIgnoreCase("All"))
	    {
		sbSqlQFile.append(" and a.strPOSCode='" + POSCode + "' ");
	    }
	    sbSqlQFile.append(" group by e.strGroupCode ");
	    //System.out.println(sbSqlQFile);
	    ResultSet rsGroupWise = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlQFile.toString());
	    while (rsGroupWise.next())
	    {
		String groupCode = rsGroupWise.getString(3);

		Map<String, Double> hmGroupWiseDtlData = new HashMap<String, Double>();
		if (hmGroupWiseData.containsKey(rsGroupWise.getString(1)))
		{
		    hmGroupWiseDtlData = hmGroupWiseData.get(rsGroupWise.getString(1));
		}
		double groupAmt = rsGroupWise.getDouble(2);
		double groupDiscAmt = 0;

		/**
		 *
		 */
		groupDiscAmt += rsGroupWise.getDouble(4);

		sbSql.setLength(0);
		sbSql.append("select f.strGroupName,sum(c.dblAmount)-sum(c.dblDiscAmt),e.strGroupCode,SUM(c.dblDiscAmt) "
			+ " from tblqbillhd a,tblqbillmodifierdtl c,tblitemmaster d,tblsubgrouphd e,tblgrouphd f "
			+ " where a.strBillNo=c.strBillNo "
			+ " and date(a.dteBillDate)=date(c.dteBillDate) "
			+ " and left(c.strItemCode,7)=d.strItemCode "
			+ " and d.strSubGroupCode=e.strSubGroupCode "
			+ " and e.strGroupCode=f.strGroupCode "
			+ " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
			+ " and a.strClientCode=c.strClientCode "
			+ " and f.strGroupCode='" + rsGroupWise.getString(3) + "' "
			+ " and a.strOperationType='" + operationType + "' "
			+ "  ");
		if (!POSCode.equalsIgnoreCase("All"))
		{
		    sbSql.append(" and a.strPOSCode='" + POSCode + "' ");
		}
		sbSql.append(" group by f.strGroupCode");
		//System.out.println(sbSql);
		ResultSet rsGroupModData = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
		if (rsGroupModData.next())
		{
		    groupAmt += rsGroupModData.getDouble(2);
		    groupDiscAmt += rsGroupModData.getDouble(4);
		}
		rsGroupModData.close();

		if (hmGroupWiseData.containsKey(rsGroupWise.getString(1)))
		{
		    if (hmGroupWiseDtlData.containsKey("Amount"))
		    {
			hmGroupWiseDtlData.put("Amount", hmGroupWiseDtlData.get("Amount") + groupAmt);
		    }
		    else
		    {
			hmGroupWiseDtlData.put("Amount", groupAmt);
		    }
		}
		else
		{
		    hmGroupWiseDtlData.put("Amount", groupAmt);
		}

		if (hmGroupWiseData.containsKey(rsGroupWise.getString(1)))
		{
		    if (hmGroupWiseDtlData.containsKey("Discount"))
		    {
			hmGroupWiseDtlData.put("Discount", hmGroupWiseDtlData.get("Discount") + groupDiscAmt);
		    }
		    else
		    {
			hmGroupWiseDtlData.put("Discount", groupDiscAmt);
		    }
		}
		else
		{
		    hmGroupWiseDtlData.put("Discount", groupDiscAmt);
		}

		Map<String, String> hmTaxDtl = new HashMap<String, String>();
		sbSql.setLength(0);
		sbSql.append("select sum(b.dblTaxAmount),sum(b.dblTaxableAmount),c.strTaxCode,c.strTaxIndicator "
			+ ",c.strTaxOnTax,c.strTaxOnTaxCode,c.strTaxOnGD,c.strOperationType,c.dblPercent "
			+ " from tblqbillhd a,tblqbilltaxdtl b,tbltaxhd c "
			+ " where a.strBillNo=b.strBillNo "
			+ " and date(a.dteBillDate)=date(b.dteBillDate) "
			+ " and b.strTaxCode=c.strTaxCode "
			+ " and a.strClientCode=b.strClientCode "
			+ " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
			+ " and a.strOperationType='" + operationType + "' "
			+ " ");
		if (!POSCode.equalsIgnoreCase("All"))
		{
		    sbSql.append(" and a.strPOSCode='" + POSCode + "' ");
		}
		sbSql.append(" group by b.strTaxCode order by c.strTaxOnTax");
		ResultSet rsTax = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
		while (rsTax.next())
		{
		    String taxCode = rsTax.getString(3);
		    double taxPer = rsTax.getDouble(9);

		    String sql = "select a.strTaxCode,a.strTaxDesc,b.strGroupCode,b.strGroupName,b.strApplicable "
			    + "from tbltaxhd a,tbltaxongroup b,tbltaxposdtl c "
			    + "where a.strTaxCode=b.strTaxCode "
			    + "and a.strTaxCode=c.strTaxCode ";
		    if (!POSCode.equalsIgnoreCase("All"))
		    {
			sql = sql + " and c.strPOSCode='" + POSCode + "' ";
		    }
		    sql += "and b.strGroupCode='" + rsGroupWise.getString(3) + "' "
			    + "and a.strTaxCode='" + rsTax.getString(3) + "' "
			    + "and b.strApplicable='true' "
			    + "group by a.strTaxCode,b.strGroupCode";
		    ResultSet rsTaxOnGroup = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		    if (rsTaxOnGroup.next())
		    {
			double taxAmt = rsTax.getDouble(1);
			double taxableAmt = rsTax.getDouble(2);
			if (!rsTax.getString(4).isEmpty())
			{
			    double groupWiseTaxIndAmt = 0.00;

			    sbSql.setLength(0);
			    sbSql.append("select e.strGroupName,e.strGroupCode,sum(b.dblAmount)-sum(b.dblDiscountAmt),sum(b.dblDiscountAmt) "
				    + " from tblqbillhd a,tblqbilldtl b,tblitemmaster c,tblsubgrouphd d,tblgrouphd e "
				    + " where a.strBillNo=b.strBillNo "
				    + " and date(a.dteBillDate)=date(b.dteBillDate) "
				    + " and b.strItemCode=c.strItemCode "
				    + " and c.strSubGroupCode=d.strSubGroupCode "
				    + " and d.strGroupCode=e.strGroupCode "
				    + " and c.strTaxIndicator='" + rsTax.getString(4) + "' "
				    + " and a.strClientCode=b.strClientCode "
				    + " and e.strGroupCode='" + rsGroupWise.getString(3) + "' "
				    + " and a.strOperationType='" + operationType + "' "
				    + " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
				    + " and a.strAreaCode in " + funGetAreaCodes(rsTax.getString(3)) + " "
				    + " and a.strBillNo in (select strBillNo from tblqbilltaxdtl) ");
			    if (!POSCode.equalsIgnoreCase("All"))
			    {
				sbSql.append(" and a.strPOSCode='" + POSCode + "' ");
			    }
			    sbSql.append(" group by e.strGroupCode");
			    ResultSet rsGrpWiseTax = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
			    if (rsGrpWiseTax.next())
			    {
				double tempGroupWiseTaxIndAmt = rsGrpWiseTax.getDouble(3);
				groupWiseTaxIndAmt += rsGrpWiseTax.getDouble(3);

				if (rsTax.getString(5).equalsIgnoreCase("Yes"))
				{
				    String taxOnTaxCode = rsTax.getString(6);
				    String[] arrSpTaxonTaxCodes = taxOnTaxCode.split(",");
				    for (int cntTot = 0; cntTot < arrSpTaxonTaxCodes.length; cntTot++)
				    {
					if (hmTaxDtl.containsKey(arrSpTaxonTaxCodes[cntTot]))
					{
					    String taxDtl = hmTaxDtl.get(arrSpTaxonTaxCodes[cntTot]);
					    double totTaxAmt = Double.parseDouble(taxDtl.split("!")[1]);
					    double totTaxableAmt = Double.parseDouble(taxDtl.split("!")[0]);
					    if (totTaxableAmt > 0)
					    {
						double totTaxableIndAmt = (totTaxAmt / totTaxableAmt) * tempGroupWiseTaxIndAmt;
						groupWiseTaxIndAmt += totTaxableIndAmt;
					    }
					}
				    }
				}

			    }
			    rsGrpWiseTax.close();

			    //modifiers
			    sbSql.setLength(0);
			    sbSql.append("select e.strGroupName,e.strGroupCode,sum(b.dblAmount)-sum(b.dblDiscAmt),sum(b.dblDiscAmt) "
				    + " from tblqbillhd a,tblqbillmodifierdtl b,tblitemmaster c,tblsubgrouphd d,tblgrouphd e "
				    + " where a.strBillNo=b.strBillNo "
				    + " and date(a.dteBillDate)=date(b.dteBillDate) "
				    + " AND left(b.strItemCode,7)=c.strItemCode  "
				    + " and c.strSubGroupCode=d.strSubGroupCode "
				    + " and d.strGroupCode=e.strGroupCode "
				    + " and c.strTaxIndicator='" + rsTax.getString(4) + "' "
				    + " and a.strClientCode=b.strClientCode "
				    + " and e.strGroupCode='" + rsGroupWise.getString(3) + "' "
				    + " and a.strOperationType='" + operationType + "' "
				    + " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
				    + " and a.strAreaCode in " + funGetAreaCodes(rsTax.getString(3)) + " "
				    + " and a.strBillNo in (select strBillNo from tblqbilltaxdtl) ");
			    if (!POSCode.equalsIgnoreCase("All"))
			    {
				sbSql.append(" and a.strPOSCode='" + POSCode + "' ");
			    }
			    sbSql.append(" group by e.strGroupCode");
			    rsGrpWiseTax = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
			    if (rsGrpWiseTax.next())
			    {
				double tempGroupWiseTaxIndAmt = rsGrpWiseTax.getDouble(3);
				groupWiseTaxIndAmt += rsGrpWiseTax.getDouble(3);

				if (rsTax.getString(5).equalsIgnoreCase("Yes"))
				{
				    String taxOnTaxCode = rsTax.getString(6);
				    String[] arrSpTaxonTaxCodes = taxOnTaxCode.split(",");
				    for (int cntTot = 0; cntTot < arrSpTaxonTaxCodes.length; cntTot++)
				    {
					if (hmTaxDtl.containsKey(arrSpTaxonTaxCodes[cntTot]))
					{
					    String taxDtl = hmTaxDtl.get(arrSpTaxonTaxCodes[cntTot]);
					    double totTaxAmt = Double.parseDouble(taxDtl.split("!")[1]);
					    double totTaxableAmt = Double.parseDouble(taxDtl.split("!")[0]);
					    if (totTaxableAmt > 0)
					    {
						double totTaxableIndAmt = (totTaxAmt / totTaxableAmt) * tempGroupWiseTaxIndAmt;
						groupWiseTaxIndAmt += totTaxableIndAmt;
					    }
					}
				    }
				}

			    }
			    rsGrpWiseTax.close();

			    double groupWiseTaxAmt = (taxPer / 100) * groupWiseTaxIndAmt;

			    System.out.println("Grp Wise Taxable Amt=" + groupWiseTaxIndAmt + "\tTax Amt=" + taxAmt + "\tTaxable Amt=" + taxableAmt + "\tGrp Tax Amt=" + groupWiseTaxAmt);
			    hmTaxDtl.put(rsTax.getString(3), groupWiseTaxIndAmt + "!" + groupWiseTaxAmt);
			    if (hmGroupWiseData.containsKey(rsGroupWise.getString(1)))
			    {
				hmGroupWiseDtlData = hmGroupWiseData.get(rsGroupWise.getString(1));
				if (hmGroupWiseDtlData.containsKey(rsTax.getString(3)))
				{
				    hmGroupWiseDtlData.put(rsTax.getString(3), hmGroupWiseDtlData.get(rsTax.getString(3)) + groupWiseTaxAmt);
				}
				else
				{
				    hmGroupWiseDtlData.put(rsTax.getString(3), groupWiseTaxAmt);
				}
			    }
			    else
			    {
				hmGroupWiseDtlData.put(rsTax.getString(3), groupWiseTaxAmt);
			    }
			}
			else
			{

			    sbSql.setLength(0);
			    sbSql.append("select e.strGroupName,e.strGroupCode,sum(b.dblAmount)-sum(b.dblDiscountAmt),sum(b.dblDiscountAmt) "
				    + " from tblqbillhd a,tblqbilldtl b,tblitemmaster c,tblsubgrouphd d,tblgrouphd e "
				    + " where a.strBillNo=b.strBillNo "
				    + " and date(a.dteBillDate)=date(b.dteBillDate) "
				    + " and b.strItemCode=c.strItemCode "
				    + " and c.strSubGroupCode=d.strSubGroupCode "
				    + " and d.strGroupCode=e.strGroupCode "
				    + " and a.strClientCode=b.strClientCode "
				    + " and e.strGroupCode='" + rsGroupWise.getString(3) + "' "
				    + " and a.strOperationType='" + operationType + "' "
				    + " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
				    + " and a.strAreaCode in " + funGetAreaCodes(rsTax.getString(3)) + " "
				    + " and a.strBillNo in (select strBillNo from tblqbilltaxdtl) ");
			    if (!POSCode.equalsIgnoreCase("All"))
			    {
				sbSql.append(" and a.strPOSCode='" + POSCode + "' ");
			    }
			    sbSql.append(" group by e.strGroupCode");
			    ResultSet rsGrpWiseTax = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());

			    double groupWiseTaxIndAmt = 0.00;
			    while (rsGrpWiseTax.next())
			    {
				groupWiseTaxIndAmt = rsGrpWiseTax.getDouble(3);
			    }
			    rsGrpWiseTax.close();

			    //modifiers
			    sbSql.setLength(0);
			    sbSql.append("select e.strGroupName,e.strGroupCode,sum(b.dblAmount)-sum(b.dblDiscAmt),SUM(b.dblDiscAmt) "
				    + " from tblqbillhd a,tblqbillmodifierdtl b,tblitemmaster c,tblsubgrouphd d,tblgrouphd e "
				    + " where a.strBillNo=b.strBillNo "
				    + " and date(a.dteBillDate)=date(b.dteBillDate) "
				    + " AND left(b.strItemCode,7)=c.strItemCode  "
				    + " and c.strSubGroupCode=d.strSubGroupCode "
				    + " and d.strGroupCode=e.strGroupCode "
				    + " and a.strClientCode=b.strClientCode "
				    + " and e.strGroupCode='" + rsGroupWise.getString(3) + "' "
				    + " and a.strOperationType='" + operationType + "' "
				    + " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
				    + " and a.strAreaCode in " + funGetAreaCodes(rsTax.getString(3)) + " "
				    + " and a.strBillNo in (select strBillNo from tblqbilltaxdtl) ");
			    if (!POSCode.equalsIgnoreCase("All"))
			    {
				sbSql.append(" and a.strPOSCode='" + POSCode + "' ");
			    }
			    sbSql.append(" group by e.strGroupCode");
			    rsGrpWiseTax = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
			    while (rsGrpWiseTax.next())
			    {
				groupWiseTaxIndAmt += rsGrpWiseTax.getDouble(3);
			    }
			    rsGrpWiseTax.close();

			    if (rsTax.getString(5).equalsIgnoreCase("Yes"))
			    {
				String taxOnTaxCode = rsTax.getString(6);
				String[] arrSpTaxonTaxCodes = taxOnTaxCode.split(",");
				for (int cntTot = 0; cntTot < arrSpTaxonTaxCodes.length; cntTot++)
				{
				    if (hmTaxDtl.containsKey(arrSpTaxonTaxCodes[cntTot]))
				    {
					String taxDtl = hmTaxDtl.get(arrSpTaxonTaxCodes[cntTot]);
					double totTaxAmt = Double.parseDouble(taxDtl.split("!")[1]);
					groupWiseTaxIndAmt += totTaxAmt;
				    }
				}
			    }

			    double groupWiseTaxAmt = (taxPer / 100) * groupWiseTaxIndAmt;
//                            if (taxableAmt > 0)
//                            {
//                                groupWiseTaxAmt = ((taxAmt / taxableAmt) * groupWiseTaxIndAmt);
//                            }
			    hmTaxDtl.put(rsTax.getString(3), groupWiseTaxIndAmt + "!" + groupWiseTaxAmt);
			    if (hmGroupWiseData.containsKey(rsGroupWise.getString(1)))
			    {
				hmGroupWiseDtlData = hmGroupWiseData.get(rsGroupWise.getString(1));
				if (hmGroupWiseDtlData.containsKey(rsTax.getString(3)))
				{
				    hmGroupWiseDtlData.put(rsTax.getString(3), hmGroupWiseDtlData.get(rsTax.getString(3)) + groupWiseTaxAmt);
				}
				else
				{
				    hmGroupWiseDtlData.put(rsTax.getString(3), groupWiseTaxAmt);
				}
			    }
			    else
			    {
				hmGroupWiseDtlData.put(rsTax.getString(3), groupWiseTaxAmt);
			    }
			}
		    }
		}
		rsTax.close();

		hmGroupWiseData.put(rsGroupWise.getString(1), hmGroupWiseDtlData);
	    }
	    rsGroupWise.close();

	    hmOpTypeWiseGroupWiseData.put(operationType, hmGroupWiseData);
	}
	rsOpType.close();

	Map<String, Double> mapVerticalTotal = new HashMap<String, Double>();
	//Map<Integer ,String> mapSequence=new TreeMap<Integer,String>();
	cntLine += 20;
	//cntLine+=20;
	cntLine += 27;

	pw.print(objUtility.funPrintTextWithAlignment("Operation Type|", 20, "Right"));
	//pw.print(objUtility.funPrintTextWithAlignment("Group Name|",20,"Right"));
	pw.print(objUtility.funPrintTextWithAlignment("Amount|", 27, "Right"));

	String sql = "";
	for (Map.Entry<Integer, String> entry : mapTaxHeaders.entrySet())
	{
	    sql = "select strTaxDesc,strTaxShortName from tbltaxhd where strTaxCode='" + entry.getValue() + "' ";
	    ResultSet rsTaxDescDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsTaxDescDtl.next())
	    {
		pw.print(objUtility.funPrintTextWithAlignment(rsTaxDescDtl.getString(2) + "|", 27, "Right"));
		cntLine += 27;
	    }
	    rsTaxDescDtl.close();
	}

	pw.print(objUtility.funPrintTextWithAlignment("Discount|", 20, "Right"));
	pw.print(objUtility.funPrintTextWithAlignment("Total|", 27, "Right"));
	pw.println();

	cntLine += 20;
	cntLine += 27;
	cntLine += 27;
	for (int cn = 0; cn < cntLine; cn++)
	{
	    pw.print("-");
	}
	pw.println();

	double totalGroupAmt = 0, totalDiscAmt = 0;

	for (Map.Entry<String, Map<String, Map<String, Double>>> entryOp : hmOpTypeWiseGroupWiseData.entrySet())
	{
	    Map<String, Map<String, Double>> hmGroupWiseData = entryOp.getValue();
	    pw.println();
	    pw.println(objUtility.funPrintTextWithAlignment(entryOp.getKey() + "|", 20, "Right"));
	    int len = 30;
	    for (int cn = 0; cn < len; cn++)
	    {
		pw.print("=");
	    }
	    pw.println();

	    for (Map.Entry<String, Map<String, Double>> entry : hmGroupWiseData.entrySet())
	    {
		double totalVerticalAmt = 0;
		System.out.println(entry.getKey());
		pw.print(objUtility.funPrintTextWithAlignment(entry.getKey() + "|", 20, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(decFormatter.format(entry.getValue().get("Amount")) + "|", 27, "Right"));
		totalGroupAmt += entry.getValue().get("Amount");
		totalVerticalAmt += entry.getValue().get("Amount");

		for (Map.Entry<Integer, String> entryTax : mapTaxHeaders.entrySet())
		{
		    if (entry.getValue().containsKey(entryTax.getValue()))
		    {
			pw.print(objUtility.funPrintTextWithAlignment(decFormatter.format(entry.getValue().get(entryTax.getValue())) + "|", 27, "Right"));
			totalVerticalAmt += entry.getValue().get(entryTax.getValue());

			if (mapVerticalTotal.containsKey(entryTax.getValue()))
			{
			    double tempTaxAmt = mapVerticalTotal.get(entryTax.getValue());
			    mapVerticalTotal.put(entryTax.getValue(), entry.getValue().get(entryTax.getValue()) + tempTaxAmt);
			}
			else
			{
			    mapVerticalTotal.put(entryTax.getValue(), entry.getValue().get(entryTax.getValue()));
			}
			//mapSequence.put(count,entryTax.getValue());
		    }
		    else
		    {
			if (mapVerticalTotal.containsKey(entryTax.getValue()))
			{
			    mapVerticalTotal.put(entryTax.getValue(), mapVerticalTotal.get(entryTax.getValue()));
			}
			else
			{
			    mapVerticalTotal.put(entryTax.getValue(), 0.00);
			}
			pw.print(objUtility.funPrintTextWithAlignment("0.0|", 27, "Right"));
		    }
		}

		pw.print(objUtility.funPrintTextWithAlignment(decFormatter.format(entry.getValue().get("Discount")) + "|", 20, "Right"));
		totalDiscAmt += entry.getValue().get("Discount");

		if (mapVerticalTotal.containsKey("TotalHorizontalAmt"))
		{
		    mapVerticalTotal.put("TotalHorizontalAmt", Double.parseDouble(decFormatter.format(mapVerticalTotal.get("TotalHorizontalAmt") + totalVerticalAmt)));
		}
		else
		{
		    mapVerticalTotal.put("TotalHorizontalAmt", Double.parseDouble(decFormatter.format(totalVerticalAmt)));
		}

		pw.print(objUtility.funPrintTextWithAlignment(decFormatter.format(totalVerticalAmt) + "|", 27, "Right"));
		pw.println();
	    }
	}

	mapVerticalTotal.put("TotalAmount", totalGroupAmt);

	for (int cn = 0; cn < cntLine; cn++)
	{
	    pw.print("-");
	}
	pw.println();

	pw.print(objUtility.funPrintTextWithAlignment("Total|", 20, "Right"));
	pw.print(objUtility.funPrintTextWithAlignment(String.valueOf(decFormatter.format(mapVerticalTotal.get("TotalAmount"))) + "|", 27, "Right"));

	for (Map.Entry<Integer, String> entry : mapTaxHeaders.entrySet())
	{
	    System.out.println(entry.getKey() + "   " + entry.getValue());
	    pw.print(objUtility.funPrintTextWithAlignment(String.valueOf(decFormatter.format(mapVerticalTotal.get(entry.getValue()))) + "|", 27, "Right"));
	}

	pw.print(objUtility.funPrintTextWithAlignment(decFormatter.format(totalDiscAmt) + "|", 20, "Right"));
	if (mapVerticalTotal.get("TotalHorizontalAmt") == null)
	{
	    mapVerticalTotal.put("TotalHorizontalAmt", 0.00);
	}
	pw.print(objUtility.funPrintTextWithAlignment(String.valueOf(decFormatter.format(mapVerticalTotal.get("TotalHorizontalAmt"))) + "|", 27, "Right"));

	pw.println();
	pw.println();

	return 1;
    }

// Group Wise Taxation
    private int funGroupWiseTaxationData(String fromDate, String toDate, String POSCode, PrintWriter pw) throws Exception
    {
	pw.println();
	pw.println();
	pw.println("SALE BY TAX CATEGORY");
	pw.println("---------------------------------------------");

	clsUtility objUtility = new clsUtility();
	int cntLine = 0;
	StringBuilder sbSqlLiveFile = new StringBuilder();
	StringBuilder sbSqlQFile = new StringBuilder();
	sbSqlLiveFile.setLength(0);
	sbSqlQFile.setLength(0);
	StringBuilder sbSql = new StringBuilder();
	sbSql.setLength(0);

	Map<Integer, String> mapTaxHeaders = new TreeMap<Integer, String>();
	Map<String, Map<String, Double>> hmGroupWiseData = new HashMap<String, Map<String, Double>>();

	int cntTax = 1;
	String sqlTax = "select c.strTaxCode "
		+ " from tblbillhd a,tblbilltaxdtl b,tbltaxhd c "
		+ " where a.strBillNo=b.strBillNo "
		+ " and b.strTaxCode=c.strTaxCode "
		+ " and date(a.dteBillDate)=date(b.dteBillDate) "
		+ " and a.strClientCode=b.strClientCode "
		+ " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' ";
	if (!POSCode.equalsIgnoreCase("All"))
	{
	    sqlTax += " and a.strPOSCode='" + POSCode + "' ";
	}
	sqlTax += " group by c.strTaxCode";
	ResultSet rsTaxDtl1 = clsGlobalVarClass.dbMysql.executeResultSet(sqlTax);
	while (rsTaxDtl1.next())
	{
	    mapTaxHeaders.put(cntTax, rsTaxDtl1.getString(1));
	    cntTax++;
	}
	rsTaxDtl1.close();

	sqlTax = "select c.strTaxCode "
		+ " from tblqbillhd a,tblqbilltaxdtl b,tbltaxhd c "
		+ " where a.strBillNo=b.strBillNo "
		+ " and b.strTaxCode=c.strTaxCode "
		+ " and date(a.dteBillDate)=date(b.dteBillDate) "
		+ " and a.strClientCode=b.strClientCode "
		+ " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' ";
	if (!POSCode.equalsIgnoreCase("All"))
	{
	    sqlTax += " and a.strPOSCode='" + POSCode + "' ";
	}
	sqlTax += " group by c.strTaxCode";
	rsTaxDtl1 = clsGlobalVarClass.dbMysql.executeResultSet(sqlTax);
	while (rsTaxDtl1.next())
	{
	    if (!mapTaxHeaders.containsValue(rsTaxDtl1.getString(1)))
	    {
		mapTaxHeaders.put(cntTax, rsTaxDtl1.getString(1));
		cntTax++;
	    }
	}
	rsTaxDtl1.close();

	sbSqlLiveFile.append(" select e.strGroupName,sum(b.dblAmount)-sum(b.dblDiscountAmt),e.strGroupCode,sum(b.dblDiscountAmt) "
		+ " from tblbillhd a,tblbilldtl b,tblitemmaster c,tblsubgrouphd d,tblgrouphd e "
		+ " where a.strBillNo=b.strBillNo "
		+ " and date(a.dteBillDate)=date(b.dteBillDate) "
		+ " and b.strItemCode=c.strItemCode "
		+ " and c.strSubGroupCode=d.strSubGroupCode "
		+ " and d.strGroupCode=e.strGroupCode "
		+ " and a.strClientCode=b.strClientCode "
		+ " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		+ "  ");
	if (!POSCode.equalsIgnoreCase("All"))
	{
	    sbSqlLiveFile.append(" and a.strPOSCode='" + POSCode + "' ");
	}
	sbSqlLiveFile.append(" group by e.strGroupCode ");
	System.out.println(sbSqlLiveFile);
	ResultSet rsGroupWise = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlLiveFile.toString());
	while (rsGroupWise.next())
	{
	    Map<String, Double> hmGroupWiseDtlData = new HashMap<String, Double>();
	    double groupAmt = rsGroupWise.getDouble(2);
	    double groupDiscAmt = rsGroupWise.getDouble(4);

	    sbSql.setLength(0);
	    sbSql.append("select f.strGroupName,sum(c.dblAmount)-sum(c.dblDiscAmt),e.strGroupCode, SUM(c.dblDiscAmt) "
		    + " from tblbillhd a,tblbillmodifierdtl c,tblitemmaster d,tblsubgrouphd e,tblgrouphd f "
		    + " where a.strBillNo=c.strBillNo "
		    + " and date(a.dteBillDate)=date(c.dteBillDate) "
		    + " and left(c.strItemCode,7)=d.strItemCode  "
		    + " and d.strSubGroupCode=e.strSubGroupCode "
		    + " and e.strGroupCode=f.strGroupCode "
		    + " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and a.strClientCode=c.strClientCode "
		    + " and f.strGroupCode='" + rsGroupWise.getString(3) + "' "
		    + "  ");
	    if (!POSCode.equalsIgnoreCase("All"))
	    {
		sbSql.append(" and a.strPOSCode='" + POSCode + "' ");
	    }
	    sbSql.append(" group by f.strGroupCode");
	    ResultSet rsGroupModData = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
	    if (rsGroupModData.next())
	    {
		groupAmt += rsGroupModData.getDouble(2);
		groupDiscAmt += rsGroupModData.getDouble(4);
	    }
	    rsGroupModData.close();

	    if (hmGroupWiseData.containsKey(rsGroupWise.getString(1)))
	    {
		hmGroupWiseDtlData.put("Amount", hmGroupWiseDtlData.get("Amount") + groupAmt);
	    }
	    else
	    {
		hmGroupWiseDtlData.put("Amount", groupAmt);
	    }

	    if (hmGroupWiseData.containsKey(rsGroupWise.getString(1)))
	    {
		hmGroupWiseDtlData.put("Discount", hmGroupWiseDtlData.get("Discount") + groupDiscAmt);
	    }
	    else
	    {
		hmGroupWiseDtlData.put("Discount", groupDiscAmt);
	    }

	    Map<String, String> hmTaxDtl = new HashMap<String, String>();
	    sbSql.setLength(0);
	    sbSql.append("select sum(b.dblTaxAmount),sum(b.dblTaxableAmount),c.strTaxCode,c.strTaxIndicator"
		    + ",c.strTaxOnTax,c.strTaxOnTaxCode,c.strTaxOnGD,c.strOperationType,dblPercent "
		    + " from tblbillhd a,tblbilltaxdtl b,tbltaxhd c "
		    + " where a.strBillNo=b.strBillNo "
		    + " and date(a.dteBillDate)=date(b.dteBillDate) "
		    + " and b.strTaxCode=c.strTaxCode "
		    + " and a.strClientCode=b.strClientCode "
		    + " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + "  ");
	    if (!POSCode.equalsIgnoreCase("All"))
	    {
		sbSql.append(" and a.strPOSCode='" + POSCode + "' ");
	    }
	    sbSql.append(" group by b.strTaxCode order by c.strTaxOnTax");
	    ResultSet rsTax = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
	    while (rsTax.next())
	    {
		String tacCode = rsTax.getString(3);
		double taxPer = rsTax.getDouble(9);

		String sql = "select a.strTaxCode,a.strTaxDesc,b.strGroupCode,b.strGroupName,b.strApplicable "
			+ "from tbltaxhd a,tbltaxongroup b,tbltaxposdtl c "
			+ "where a.strTaxCode=b.strTaxCode "
			+ "and a.strTaxCode=c.strTaxCode ";
		if (!POSCode.equalsIgnoreCase("All"))
		{
		    sql = sql + " and c.strPOSCode='" + POSCode + "' ";
		}
		sql += "and b.strGroupCode='" + rsGroupWise.getString(3) + "' "
			+ "and a.strTaxCode='" + rsTax.getString(3) + "' "
			+ "and b.strApplicable='true' "
			+ "group by a.strTaxCode,b.strGroupCode";
		ResultSet rsTaxOnGroup = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		if (rsTaxOnGroup.next())
		{
		    double taxAmt = rsTax.getDouble(1);
		    double taxableAmt = rsTax.getDouble(2);

		    String[] arrTaxOperationTypes = rsTax.getString(8).split(",");
		    String taxOpTypes = "'DirectBiller'";
		    for (int cn = 0; cn < arrTaxOperationTypes.length; cn++)
		    {
			String opType = "";
			if (arrTaxOperationTypes[cn].equals("DineIn"))
			{
			    opType = "DineIn";
			}
			else if (arrTaxOperationTypes[cn].equals("HomeDelivery"))
			{
			    opType = "HomeDelivery";
			}
			else if (arrTaxOperationTypes[cn].equals("TakeAway"))
			{
			    opType = "TakeAway";
			}

			taxOpTypes += ",'" + opType + "'";
		    }

		    if (!rsTax.getString(4).isEmpty())
		    {
			sbSql.setLength(0);
			sbSql.append("select e.strGroupName,e.strGroupCode,sum(b.dblAmount)-sum(b.dblDiscountAmt) "
				+ " from tblbillhd a,tblbilldtl b,tblitemmaster c,tblsubgrouphd d,tblgrouphd e "
				+ " where a.strBillNo=b.strBillNo "
				+ " and date(a.dteBillDate)=date(b.dteBillDate) "
				+ " and b.strItemCode=c.strItemCode "
				+ " and c.strSubGroupCode=d.strSubGroupCode "
				+ " and d.strGroupCode=e.strGroupCode  "
				+ " and c.strTaxIndicator='" + rsTax.getString(4) + "' "
				+ " and a.strClientCode=b.strClientCode "
				+ " and e.strGroupCode='" + rsGroupWise.getString(3) + "' "
				+ " and a.strOperationType in (" + taxOpTypes + ") "
				+ " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
				+ " and a.strAreaCode in " + funGetAreaCodes(rsTax.getString(3)) + " "
				+ " and a.strBillNo in (select strBillNo from tblbilltaxdtl) ");
			if (!POSCode.equalsIgnoreCase("All"))
			{
			    sbSql.append(" and a.strPOSCode='" + POSCode + "' ");
			}
			sbSql.append(" group by e.strGroupCode");
			ResultSet rsGrpWiseTax = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
			if (rsGrpWiseTax.next())
			{
			    double tempGroupWiseTaxIndAmt = rsGrpWiseTax.getDouble(3);
			    double groupWiseTaxIndAmt = rsGrpWiseTax.getDouble(3);

			    sbSql.setLength(0);
			    sbSql.append("select e.strGroupName,e.strGroupCode,sum(b.dblAmount)-sum(b.dblDiscAmt) "
				    + " from tblbillhd a,tblbillmodifierdtl b,tblitemmaster c,tblsubgrouphd d,tblgrouphd e "
				    + " where a.strBillNo=b.strBillNo "
				    + " and date(a.dteBillDate)=date(b.dteBillDate) "
				    + " and left(b.strItemCode,7)=c.strItemCode "
				    + " and c.strSubGroupCode=d.strSubGroupCode "
				    + " and d.strGroupCode=e.strGroupCode "
				    + " and c.strTaxIndicator='" + rsTax.getString(4) + "' "
				    + " and e.strGroupCode='" + rsGroupWise.getString(3) + "' "
				    + " and a.strOperationType in (" + taxOpTypes + ") "
				    + " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
				    + " and a.strClientCode=b.strClientCode "
				    + " and e.strGroupCode='" + rsGrpWiseTax.getString(2) + "' "
				    + " ");
			    if (!POSCode.equalsIgnoreCase("All"))
			    {
				sbSql.append(" and a.strPOSCode='" + POSCode + "' ");
			    }
			    sbSql.append(" group by e.strGroupCode");
			    ResultSet rsGrpModWise = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
			    if (rsGrpModWise.next())
			    {
				tempGroupWiseTaxIndAmt += rsGrpModWise.getDouble(3);
				groupWiseTaxIndAmt += rsGrpModWise.getDouble(3);
			    }
			    rsGrpModWise.close();

			    if (rsTax.getString(5).equalsIgnoreCase("Yes"))
			    {
				String taxOnTaxCode = rsTax.getString(6);
				String[] arrSpTaxonTaxCodes = taxOnTaxCode.split(",");
				for (int cntTot = 0; cntTot < arrSpTaxonTaxCodes.length; cntTot++)
				{
				    if (hmTaxDtl.containsKey(arrSpTaxonTaxCodes[cntTot]))
				    {
					String taxDtl = hmTaxDtl.get(arrSpTaxonTaxCodes[cntTot]);
					double totTaxAmt = Double.parseDouble(taxDtl.split("!")[1]);
					double totTaxableAmt = Double.parseDouble(taxDtl.split("!")[0]);
					if (totTaxableAmt > 0)
					{
					    double totTaxableIndAmt = (totTaxAmt / totTaxableAmt) * tempGroupWiseTaxIndAmt;
					    groupWiseTaxIndAmt += totTaxableIndAmt;
					}
				    }
				}
			    }
			    double groupWiseTaxAmt = (taxPer / 100) * groupWiseTaxIndAmt;
//                            if (taxableAmt > 0)
//                            {
//                                groupWiseTaxAmt = ((taxAmt / taxableAmt) * groupWiseTaxIndAmt);
//                            }

			    System.out.println("Grp Wise Taxable Amt=" + groupWiseTaxIndAmt + "\tTax Amt=" + taxAmt + "\tTaxable Amt=" + taxableAmt + "\tGrp Tax Amt=" + groupWiseTaxAmt);

			    hmTaxDtl.put(rsTax.getString(3), groupWiseTaxIndAmt + "!" + groupWiseTaxAmt);
			    if (hmGroupWiseData.containsKey(rsGroupWise.getString(1)))
			    {
				hmGroupWiseDtlData = hmGroupWiseData.get(rsGroupWise.getString(1));
				hmGroupWiseDtlData.put(rsTax.getString(3), hmGroupWiseDtlData.get(rsTax.getString(3)) + groupWiseTaxAmt);
			    }
			    else
			    {
				hmGroupWiseDtlData.put(rsTax.getString(3), groupWiseTaxAmt);
			    }
			}
			rsGrpWiseTax.close();
		    }
		    else
		    {
			sbSql.setLength(0);
			sbSql.append("select e.strGroupName,e.strGroupCode,sum(b.dblAmount)-sum(b.dblDiscountAmt) "
				+ " from tblbillhd a,tblbilldtl b,tblitemmaster c,tblsubgrouphd d,tblgrouphd e "
				+ " where a.strBillNo=b.strBillNo "
				+ " and date(a.dteBillDate)=date(b.dteBillDate) "
				+ " and b.strItemCode=c.strItemCode "
				+ " and c.strSubGroupCode=d.strSubGroupCode "
				+ " and d.strGroupCode=e.strGroupCode "
				+ " and a.strClientCode=b.strClientCode "
				+ " and e.strGroupCode='" + rsGroupWise.getString(3) + "' "
				+ " and a.strOperationType in (" + taxOpTypes + ") "
				+ " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
				+ " and a.strAreaCode in " + funGetAreaCodes(rsTax.getString(3)) + " "
				+ " and a.strBillNo in (select strBillNo from tblbilltaxdtl) ");
			if (!POSCode.equalsIgnoreCase("All"))
			{
			    sbSql.append(" and a.strPOSCode='" + POSCode + "' ");
			}
			sbSql.append(" group by e.strGroupCode");
			System.out.println(sbSql);
			ResultSet rsGrpWiseTax = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
			if (rsGrpWiseTax.next())
			{
			    double groupWiseTaxIndAmt = rsGrpWiseTax.getDouble(3);

			    sbSql.setLength(0);
			    sbSql.append("select e.strGroupName,e.strGroupCode,sum(b.dblAmount)-sum(b.dblDiscAmt) "
				    + " from tblbillhd a,tblbillmodifierdtl b,tblitemmaster c,tblsubgrouphd d,tblgrouphd e "
				    + " where a.strBillNo=b.strBillNo "
				    + " and date(a.dteBillDate)=date(b.dteBillDate) "
				    + " and left(b.strItemCode,7)=c.strItemCode "
				    + " and c.strSubGroupCode=d.strSubGroupCode "
				    + " and d.strGroupCode=e.strGroupCode "
				    + " and e.strGroupCode='" + rsGroupWise.getString(3) + "' "
				    + " and a.strOperationType in (" + taxOpTypes + ") "
				    + " and a.strClientCode=b.strClientCode "
				    + " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
				    + " and e.strGroupCode='" + rsGrpWiseTax.getString(2) + "' "
				    + " and a.strAreaCode in " + funGetAreaCodes(rsTax.getString(3)) + " "
				    + " and a.strBillNo in (select strBillNo from tblbilltaxdtl) ");
			    if (!POSCode.equalsIgnoreCase("All"))
			    {
				sbSql.append(" and a.strPOSCode='" + POSCode + "' ");
			    }
			    sbSql.append(" group by e.strGroupCode");
			    ResultSet rsGrpModWise = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
			    if (rsGrpModWise.next())
			    {
				groupWiseTaxIndAmt += rsGrpModWise.getDouble(3);
			    }
			    rsGrpModWise.close();

			    if (rsTax.getString(5).equalsIgnoreCase("Yes"))
			    {
				String taxOnTaxCode = rsTax.getString(6);
				String[] arrSpTaxonTaxCodes = taxOnTaxCode.split(",");
				for (int cntTot = 0; cntTot < arrSpTaxonTaxCodes.length; cntTot++)
				{
				    if (hmTaxDtl.containsKey(arrSpTaxonTaxCodes[cntTot]))
				    {
					String taxDtl = hmTaxDtl.get(arrSpTaxonTaxCodes[cntTot]);
					double totTaxAmt = Double.parseDouble(taxDtl.split("!")[1]);
					groupWiseTaxIndAmt += totTaxAmt;
				    }
				}
			    }

			    double groupWiseTaxAmt = (taxPer / 100) * groupWiseTaxIndAmt;
//                            if (taxableAmt > 0)
//                            {
//                                groupWiseTaxAmt = ((taxAmt / taxableAmt) * groupWiseTaxIndAmt);
//                            }
			    hmTaxDtl.put(rsTax.getString(3), groupWiseTaxIndAmt + "!" + groupWiseTaxAmt);
			    if (hmGroupWiseData.containsKey(rsGroupWise.getString(1)))
			    {
				hmGroupWiseDtlData = hmGroupWiseData.get(rsGroupWise.getString(1));
				if (hmGroupWiseDtlData.containsKey(rsTax.getString(3)))
				{
				    hmGroupWiseDtlData.put(rsTax.getString(3), hmGroupWiseDtlData.get(rsTax.getString(3)) + groupWiseTaxAmt);
				}
				else
				{
				    hmGroupWiseDtlData.put(rsTax.getString(3), groupWiseTaxAmt);
				}
			    }
			    else
			    {
				hmGroupWiseDtlData.put(rsTax.getString(3), groupWiseTaxAmt);
			    }
			}
		    }
		}
	    }
	    rsTax.close();

	    hmGroupWiseData.put(rsGroupWise.getString(1), hmGroupWiseDtlData);
	}
	rsGroupWise.close();

	sbSqlQFile.append(" select e.strGroupName,sum(b.dblAmount)-sum(b.dblDiscountAmt),e.strGroupCode, SUM(b.dblDiscountAmt) "
		+ " from tblqbillhd a,tblqbilldtl b,tblitemmaster c,tblsubgrouphd d,tblgrouphd e "
		+ " where a.strBillNo=b.strBillNo "
		+ " and date(a.dteBillDate)=date(b.dteBillDate) "
		+ " and b.strItemCode=c.strItemCode "
		+ " and a.strClientCode=b.strClientCode "
		+ " and c.strSubGroupCode=d.strSubGroupCode "
		+ " and d.strGroupCode=e.strGroupCode "
		+ " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		+ "  ");
	if (!POSCode.equalsIgnoreCase("All"))
	{
	    sbSqlQFile.append(" and a.strPOSCode='" + POSCode + "' ");
	}
	sbSqlQFile.append(" group by e.strGroupCode ");
	System.out.println(sbSqlQFile);
	rsGroupWise = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlQFile.toString());
	while (rsGroupWise.next())
	{
	    Map<String, Double> hmGroupWiseDtlData = new HashMap<String, Double>();
	    if (hmGroupWiseData.containsKey(rsGroupWise.getString(1)))
	    {
		hmGroupWiseDtlData = hmGroupWiseData.get(rsGroupWise.getString(1));
	    }
	    double groupAmt = rsGroupWise.getDouble(2);
	    double groupDiscAmt = rsGroupWise.getDouble(4);

	    sbSql.setLength(0);
	    sbSql.append("select f.strGroupName,sum(c.dblAmount)-sum(c.dblDiscAmt),e.strGroupCode, SUM(c.dblDiscAmt) "
		    + " from tblqbillhd a,tblqbillmodifierdtl c,tblitemmaster d,tblsubgrouphd e,tblgrouphd f "
		    + " where a.strBillNo=c.strBillNo "
		    + " and date(a.dteBillDate)=date(c.dteBillDate) "
		    + " and left(c.strItemCode,7)=d.strItemCode "
		    + " and d.strSubGroupCode=e.strSubGroupCode "
		    + " and e.strGroupCode=f.strGroupCode "
		    + " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and a.strClientCode=c.strClientCode "
		    + " and f.strGroupCode='" + rsGroupWise.getString(3) + "' "
		    + "  ");
	    if (!POSCode.equalsIgnoreCase("All"))
	    {
		sbSql.append(" and a.strPOSCode='" + POSCode + "' ");
	    }
	    sbSql.append(" group by f.strGroupCode");
	    ResultSet rsGroupModData = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
	    if (rsGroupModData.next())
	    {
		groupAmt += rsGroupModData.getDouble(2);
		groupDiscAmt += rsGroupModData.getDouble(4);
	    }
	    rsGroupModData.close();

	    if (hmGroupWiseData.containsKey(rsGroupWise.getString(1)))
	    {
		if (hmGroupWiseDtlData.containsKey("Amount"))
		{
		    hmGroupWiseDtlData.put("Amount", hmGroupWiseDtlData.get("Amount") + groupAmt);
		}
		else
		{
		    hmGroupWiseDtlData.put("Amount", groupAmt);
		}
	    }
	    else
	    {
		hmGroupWiseDtlData.put("Amount", groupAmt);
	    }

	    if (hmGroupWiseData.containsKey(rsGroupWise.getString(1)))
	    {
		if (hmGroupWiseDtlData.containsKey("Discount"))
		{
		    hmGroupWiseDtlData.put("Discount", hmGroupWiseDtlData.get("Discount") + groupDiscAmt);
		}
		else
		{
		    hmGroupWiseDtlData.put("Discount", groupDiscAmt);
		}
	    }
	    else
	    {
		hmGroupWiseDtlData.put("Discount", groupDiscAmt);
	    }

	    Map<String, String> hmTaxDtl = new HashMap<String, String>();
	    sbSql.setLength(0);
	    sbSql.append("select sum(b.dblTaxAmount),sum(b.dblTaxableAmount),c.strTaxCode,c.strTaxIndicator"
		    + ",c.strTaxOnTax,c.strTaxOnTaxCode,c.strTaxOnGD,c.strOperationType,dblPercent "
		    + " from tblqbillhd a,tblqbilltaxdtl b,tbltaxhd c "
		    + " where a.strBillNo=b.strBillNo "
		    + " and date(a.dteBillDate)=date(b.dteBillDate) "
		    + " and b.strTaxCode=c.strTaxCode "
		    + "and a.strClientCode=b.strClientCode "
		    + " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' ");
	    if (!POSCode.equalsIgnoreCase("All"))
	    {
		sbSql.append(" and a.strPOSCode='" + POSCode + "' ");
	    }
	    sbSql.append(" group by b.strTaxCode order by c.strTaxOnTax");
	    ResultSet rsTax = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
	    while (rsTax.next())
	    {
		String taxCode = rsTax.getString(3);
		double taxPer = rsTax.getDouble(9);

		String sql = "select a.strTaxCode,a.strTaxDesc,b.strGroupCode,b.strGroupName,b.strApplicable "
			+ "from tbltaxhd a,tbltaxongroup b,tbltaxposdtl c "
			+ "where a.strTaxCode=b.strTaxCode "
			+ "and a.strTaxCode=c.strTaxCode ";
		if (!POSCode.equalsIgnoreCase("All"))
		{
		    sql = sql + " and c.strPOSCode='" + POSCode + "' ";
		}
		sql += "and b.strGroupCode='" + rsGroupWise.getString(3) + "' "
			+ "and a.strTaxCode='" + rsTax.getString(3) + "' "
			+ "and b.strApplicable='true' "
			+ "group by a.strTaxCode,b.strGroupCode";
		ResultSet rsTaxOnGroup = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		if (rsTaxOnGroup.next())
		{
		    double taxAmt = rsTax.getDouble(1);
		    double taxableAmt = rsTax.getDouble(2);
		    String[] arrTaxOperationTypes = rsTax.getString(8).split(",");
		    String taxOpTypes = "'DirectBiller'";
		    for (int cn = 0; cn < arrTaxOperationTypes.length; cn++)
		    {
			String opType = "";
			if (arrTaxOperationTypes[cn].equals("DineIn"))
			{
			    opType = "DineIn";
			}
			else if (arrTaxOperationTypes[cn].equals("HomeDelivery"))
			{
			    opType = "HomeDelivery";
			}
			else if (arrTaxOperationTypes[cn].equals("TakeAway"))
			{
			    opType = "TakeAway";
			}
			taxOpTypes += ",'" + opType + "'";
		    }

		    if (!rsTax.getString(4).isEmpty())
		    {
			sbSql.setLength(0);
			sbSql.append("select e.strGroupName,e.strGroupCode,sum(b.dblAmount)-sum(b.dblDiscountAmt) "
				+ " from tblqbillhd a,tblqbilldtl b,tblitemmaster c,tblsubgrouphd d,tblgrouphd e "
				+ " where a.strBillNo=b.strBillNo "
				+ " and date(a.dteBillDate)=date(b.dteBillDate) "
				+ " and b.strItemCode=c.strItemCode "
				+ " and c.strSubGroupCode=d.strSubGroupCode "
				+ " and d.strGroupCode=e.strGroupCode "
				+ " and c.strTaxIndicator='" + rsTax.getString(4) + "' "
				+ " and a.strClientCode=b.strClientCode "
				+ " and e.strGroupCode='" + rsGroupWise.getString(3) + "' "
				+ " and a.strOperationType in (" + taxOpTypes + ") "
				+ " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
				+ " and a.strAreaCode in " + funGetAreaCodes(rsTax.getString(3)) + " "
				+ " and a.strBillNo in (select strBillNo from tblqbilltaxdtl) ");
			if (!POSCode.equalsIgnoreCase("All"))
			{
			    sbSql.append(" and a.strPOSCode='" + POSCode + "' ");
			}
			sbSql.append(" group by e.strGroupCode");
			System.out.println(sbSql);
			ResultSet rsGrpWiseTax = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
			if (rsGrpWiseTax.next())
			{
			    double tempGroupWiseTaxIndAmt = rsGrpWiseTax.getDouble(3);
			    double groupWiseTaxIndAmt = rsGrpWiseTax.getDouble(3);

			    sbSql.setLength(0);
			    sbSql.append("select e.strGroupName,e.strGroupCode,sum(b.dblAmount)-sum(b.dblDiscAmt) "
				    + " from tblqbillhd a,tblqbillmodifierdtl b,tblitemmaster c,tblsubgrouphd d,tblgrouphd e "
				    + " where a.strBillNo=b.strBillNo "
				    + " and date(a.dteBillDate)=date(b.dteBillDate) "
				    + " and left(b.strItemCode,7)=c.strItemCode "
				    + " and c.strSubGroupCode=d.strSubGroupCode "
				    + " and d.strGroupCode=e.strGroupCode "
				    + " and c.strTaxIndicator='" + rsTax.getString(4) + "' "
				    + " and a.strClientCode=b.strClientCode "
				    + " and e.strGroupCode='" + rsGroupWise.getString(3) + "' "
				    + " and a.strOperationType in (" + taxOpTypes + ") "
				    + " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
				    + " and e.strGroupCode='" + rsGrpWiseTax.getString(2) + "' "
				    + " and a.strAreaCode in " + funGetAreaCodes(rsTax.getString(3)) + " "
				    + " and a.strBillNo in (select strBillNo from tblqbilltaxdtl) ");
			    if (!POSCode.equalsIgnoreCase("All"))
			    {
				sbSql.append(" and a.strPOSCode='" + POSCode + "' ");
			    }
			    sbSql.append(" group by e.strGroupCode");
			    ResultSet rsGrpModWise = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
			    if (rsGrpModWise.next())
			    {
				tempGroupWiseTaxIndAmt += rsGrpModWise.getDouble(3);
				groupWiseTaxIndAmt += rsGrpModWise.getDouble(3);
			    }
			    rsGrpModWise.close();

			    if (rsTax.getString(5).equalsIgnoreCase("Yes"))
			    {
				String taxOnTaxCode = rsTax.getString(6);
				String[] arrSpTaxonTaxCodes = taxOnTaxCode.split(",");
				for (int cntTot = 0; cntTot < arrSpTaxonTaxCodes.length; cntTot++)
				{
				    if (hmTaxDtl.containsKey(arrSpTaxonTaxCodes[cntTot]))
				    {
					String taxDtl = hmTaxDtl.get(arrSpTaxonTaxCodes[cntTot]);
					double totTaxAmt = Double.parseDouble(taxDtl.split("!")[1]);
					double totTaxableAmt = Double.parseDouble(taxDtl.split("!")[0]);
					if (totTaxableAmt > 0)
					{
					    double totTaxableIndAmt = (totTaxAmt / totTaxableAmt) * tempGroupWiseTaxIndAmt;
					    groupWiseTaxIndAmt += totTaxableIndAmt;
					}
				    }
				}
			    }

			    double groupWiseTaxAmt = (taxPer / 100) * groupWiseTaxIndAmt;
//                            if (taxableAmt > 0)
//                            {
//                                groupWiseTaxAmt = ((taxAmt / taxableAmt) * groupWiseTaxIndAmt);
//                            }
			    System.out.println("Grp Wise Taxable Amt=" + groupWiseTaxIndAmt + "\tTax Amt=" + taxAmt + "\tTaxable Amt=" + taxableAmt + "\tGrp Tax Amt=" + groupWiseTaxAmt);

			    hmTaxDtl.put(rsTax.getString(3), groupWiseTaxIndAmt + "!" + groupWiseTaxAmt);
			    if (hmGroupWiseData.containsKey(rsGroupWise.getString(1)))
			    {
				hmGroupWiseDtlData = hmGroupWiseData.get(rsGroupWise.getString(1));
				if (hmGroupWiseDtlData.containsKey(rsTax.getString(3)))
				{
				    hmGroupWiseDtlData.put(rsTax.getString(3), hmGroupWiseDtlData.get(rsTax.getString(3)) + groupWiseTaxAmt);
				}
				else
				{
				    hmGroupWiseDtlData.put(rsTax.getString(3), groupWiseTaxAmt);
				}
			    }
			    else
			    {
				hmGroupWiseDtlData.put(rsTax.getString(3), groupWiseTaxAmt);
			    }
			}
			rsGrpWiseTax.close();
		    }
		    else
		    {
			sbSql.setLength(0);
			sbSql.append("select e.strGroupName,e.strGroupCode,sum(b.dblAmount)-sum(b.dblDiscountAmt) "
				+ " from tblqbillhd a,tblqbilldtl b,tblitemmaster c,tblsubgrouphd d,tblgrouphd e "
				+ " where a.strBillNo=b.strBillNo "
				+ " and date(a.dteBillDate)=date(b.dteBillDate) "
				+ " and b.strItemCode=c.strItemCode "
				+ " and c.strSubGroupCode=d.strSubGroupCode "
				+ " and d.strGroupCode=e.strGroupCode "
				+ " and a.strClientCode=b.strClientCode "
				+ " and e.strGroupCode='" + rsGroupWise.getString(3) + "' "
				+ " and a.strOperationType in (" + taxOpTypes + ") "
				+ " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
				+ " and a.strAreaCode in " + funGetAreaCodes(rsTax.getString(3)) + " "
				+ " and a.strBillNo in (select strBillNo from tblqbilltaxdtl) ");
			if (!POSCode.equalsIgnoreCase("All"))
			{
			    sbSql.append(" and a.strPOSCode='" + POSCode + "' ");
			}
			sbSql.append(" group by e.strGroupCode");
			System.out.println(sbSql);
			ResultSet rsGrpWiseTax = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
			if (rsGrpWiseTax.next())
			{
			    double groupWiseTaxIndAmt = rsGrpWiseTax.getDouble(3);
			    sbSql.setLength(0);
			    sbSql.append("select e.strGroupName,e.strGroupCode,sum(b.dblAmount)-sum(b.dblDiscAmt) "
				    + " from tblqbillhd a,tblqbillmodifierdtl b,tblitemmaster c,tblsubgrouphd d,tblgrouphd e "
				    + " where a.strBillNo=b.strBillNo "
				    + " and date(a.dteBillDate)=date(b.dteBillDate) "
				    + " and left(b.strItemCode,7)=c.strItemCode "
				    + " and c.strSubGroupCode=d.strSubGroupCode "
				    + " and d.strGroupCode=e.strGroupCode "
				    + " and e.strGroupCode='" + rsGroupWise.getString(3) + "' "
				    + " and a.strOperationType in (" + taxOpTypes + ") "
				    + " and a.strClientCode=b.strClientCode "
				    + " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
				    + " and e.strGroupCode='" + rsGrpWiseTax.getString(2) + "' "
				    + " and a.strAreaCode in " + funGetAreaCodes(rsTax.getString(3)) + " "
				    + " and a.strBillNo in (select strBillNo from tblqbilltaxdtl) ");
			    if (!POSCode.equalsIgnoreCase("All"))
			    {
				sbSql.append(" and a.strPOSCode='" + POSCode + "' ");
			    }
			    sbSql.append(" group by e.strGroupCode");
			    ResultSet rsGrpModWise = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
			    if (rsGrpModWise.next())
			    {
				groupWiseTaxIndAmt += rsGrpModWise.getDouble(3);
			    }
			    rsGrpModWise.close();

			    if (rsTax.getString(5).equalsIgnoreCase("Yes"))
			    {
				String taxOnTaxCode = rsTax.getString(6);
				String[] arrSpTaxonTaxCodes = taxOnTaxCode.split(",");
				for (int cntTot = 0; cntTot < arrSpTaxonTaxCodes.length; cntTot++)
				{
				    if (hmTaxDtl.containsKey(arrSpTaxonTaxCodes[cntTot]))
				    {
					String taxDtl = hmTaxDtl.get(arrSpTaxonTaxCodes[cntTot]);
					double totTaxAmt = Double.parseDouble(taxDtl.split("!")[1]);
					groupWiseTaxIndAmt += totTaxAmt;
				    }
				}
			    }

			    double groupWiseTaxAmt = (taxPer / 100) * groupWiseTaxIndAmt;
//                            if (taxableAmt > 0)
//                            {
//                                groupWiseTaxAmt = ((taxAmt / taxableAmt) * groupWiseTaxIndAmt);
//                            }
			    hmTaxDtl.put(rsTax.getString(3), groupWiseTaxIndAmt + "!" + groupWiseTaxAmt);
			    if (hmGroupWiseData.containsKey(rsGroupWise.getString(1)))
			    {
				hmGroupWiseDtlData = hmGroupWiseData.get(rsGroupWise.getString(1));
				if (hmGroupWiseDtlData.containsKey(rsTax.getString(3)))
				{
				    hmGroupWiseDtlData.put(rsTax.getString(3), hmGroupWiseDtlData.get(rsTax.getString(3)) + groupWiseTaxAmt);
				}
				else
				{
				    hmGroupWiseDtlData.put(rsTax.getString(3), groupWiseTaxAmt);
				}
			    }
			    else
			    {
				hmGroupWiseDtlData.put(rsTax.getString(3), groupWiseTaxAmt);
			    }
			}
		    }
		}
	    }
	    rsTax.close();

	    hmGroupWiseData.put(rsGroupWise.getString(1), hmGroupWiseDtlData);
	}
	rsGroupWise.close();

	Map<String, Double> mapVerticalTotal = new HashMap<String, Double>();
	//Map<Integer ,String> mapSequence=new TreeMap<Integer,String>();
	cntLine += 20;
	cntLine += 27;
	pw.print(objUtility.funPrintTextWithAlignment("Group Name|", 20, "Right"));
	pw.print(objUtility.funPrintTextWithAlignment("Amount|", 27, "Right"));

	String sql = "";
	for (Map.Entry<Integer, String> entry : mapTaxHeaders.entrySet())
	{
	    sql = "select strTaxDesc,strTaxShortName from tbltaxhd where strTaxCode='" + entry.getValue() + "' ";

	    ResultSet rsTaxDescDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsTaxDescDtl.next())
	    {
		pw.print(objUtility.funPrintTextWithAlignment(rsTaxDescDtl.getString(2) + "|", 27, "Right"));
		cntLine += 27;
	    }
	    rsTaxDescDtl.close();
	}

	pw.print(objUtility.funPrintTextWithAlignment("Discount|", 20, "Right"));
	pw.print(objUtility.funPrintTextWithAlignment("Total|", 27, "Right"));
	pw.println();

	cntLine += 20;
	cntLine += 27;
	cntLine += 27;
	for (int cn = 0; cn < cntLine; cn++)
	{
	    pw.print("-");
	}
	pw.println();

	double totalGroupAmt = 0, totalDiscAmt = 0;
	for (Map.Entry<String, Map<String, Double>> entry : hmGroupWiseData.entrySet())
	{
	    double totalVerticalAmt = 0;
	    System.out.println(entry.getKey());
	    pw.print(objUtility.funPrintTextWithAlignment(entry.getKey() + "|", 20, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment(decFormatter.format(entry.getValue().get("Amount")) + "|", 27, "Right"));
	    totalGroupAmt += entry.getValue().get("Amount");
	    totalVerticalAmt += entry.getValue().get("Amount");

	    int count = 1;
	    for (Map.Entry<Integer, String> entryTax : mapTaxHeaders.entrySet())
	    {
		if (entry.getValue().containsKey(entryTax.getValue()))
		{
		    pw.print(objUtility.funPrintTextWithAlignment(decFormatter.format(entry.getValue().get(entryTax.getValue())) + "|", 27, "Right"));
		    totalVerticalAmt += entry.getValue().get(entryTax.getValue());

		    if (mapVerticalTotal.containsKey(entryTax.getValue()))
		    {
			double tempTaxAmt = mapVerticalTotal.get(entryTax.getValue());
			mapVerticalTotal.put(entryTax.getValue(), entry.getValue().get(entryTax.getValue()) + tempTaxAmt);
		    }
		    else
		    {
			mapVerticalTotal.put(entryTax.getValue(), entry.getValue().get(entryTax.getValue()));
		    }
		    //mapSequence.put(count,entryTax.getValue());
		    count++;
		}
		else
		{
		    //mapVerticalTotal.put(entryTax.getValue(), 0.00);
		    pw.print(objUtility.funPrintTextWithAlignment("0.0|", 27, "Right"));
		}
	    }

	    pw.print(objUtility.funPrintTextWithAlignment(decFormatter.format(entry.getValue().get("Discount")) + "|", 20, "Right"));
	    totalDiscAmt += entry.getValue().get("Discount");

	    if (mapVerticalTotal.containsKey("TotalHorizontalAmt"))
	    {
		mapVerticalTotal.put("TotalHorizontalAmt", mapVerticalTotal.get("TotalHorizontalAmt") + totalVerticalAmt);
	    }
	    else
	    {
		mapVerticalTotal.put("TotalHorizontalAmt", totalVerticalAmt);
	    }

	    pw.print(objUtility.funPrintTextWithAlignment(decFormatter.format(totalVerticalAmt) + "|", 27, "Right"));
	    pw.println();
	}

	mapVerticalTotal.put("TotalAmount", totalGroupAmt);

	for (int cn = 0; cn < cntLine; cn++)
	{
	    pw.print("-");
	}
	pw.println();

	pw.print(objUtility.funPrintTextWithAlignment("Total|", 20, "Right"));
	pw.print(objUtility.funPrintTextWithAlignment(String.valueOf(decFormatter.format(mapVerticalTotal.get("TotalAmount"))) + "|", 27, "Right"));

	for (Map.Entry<Integer, String> entry : mapTaxHeaders.entrySet())
	{
	    System.out.println(entry.getKey() + "   " + entry.getValue());
	    if (mapVerticalTotal.containsKey(entry.getValue()))
	    {
		pw.print(objUtility.funPrintTextWithAlignment(String.valueOf(decFormatter.format(mapVerticalTotal.get(entry.getValue()))) + "|", 27, "Right"));
	    }
	}

	pw.print(objUtility.funPrintTextWithAlignment(decFormatter.format(totalDiscAmt) + "|", 20, "Right"));
	if (mapVerticalTotal.get("TotalHorizontalAmt") == null)
	{
	    mapVerticalTotal.put("TotalHorizontalAmt", 0.00);
	}
	pw.print(objUtility.funPrintTextWithAlignment(String.valueOf(decFormatter.format(mapVerticalTotal.get("TotalHorizontalAmt"))) + "|", 27, "Right"));

	pw.println();
	pw.println();

	return 1;
    }

// Discounted Bill Data
    private int funDiscountedBills(String fromDate, String toDate, String POSCode, PrintWriter pw) throws Exception
    {

	try
	{
	    clsUtility objUtility = new clsUtility();
	    SimpleDateFormat yyyyMMddDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    SimpleDateFormat dateFormat = new SimpleDateFormat("E dd-MMM-yyyy");
	    SimpleDateFormat dateFormat24Hrs = new SimpleDateFormat("E dd-MMM-yyyy HH:mm:ss");
	    SimpleDateFormat ddMMyyDateFormat = new SimpleDateFormat("dd/MM/yy");

	    pw.println();
	    pw.println("DISCOUNT BILLS");
	    pw.println("**************");
	    pw.print(objUtility.funPrintTextWithAlignment("Date", 10, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment("Tbl", 10, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment("Bill No", 10, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment("Amount", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("%", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("Disc.", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("VAT/GST", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("Tip", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("Adv.", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("Total", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("  Settle", 12, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment(" Remark", 20, "left"));
	    pw.println();

	    int noOfColumns = 120;
	    String DASHLINE = "";
	    for (int i = 0; i < noOfColumns; i++)
	    {
		DASHLINE = DASHLINE + "-";
	    }
	    pw.print(DASHLINE);

	    StringBuilder sqlVoidBillBuilder = new StringBuilder();

	    double totalSubTotal = 0, totalAmt = 0.00, totalDiscAmt = 0, totalTaxAmt = 0, totalGTTotal = 0, advTotal = 0, totalTipAmt = 0;
	    boolean noDiscountedBilled = true;

	    sqlVoidBillBuilder.setLength(0);
	    sqlVoidBillBuilder.append(" select DATE_FORMAT(a.dteBillDate,'%d/%m/%y')dteBillDate,ifnull(b.strTableName,'')strTableName,a.strBillNo "
		    + ",a.dblSubTotal,a.dblDiscountPer,a.dblDiscountAmt,a.dblTaxAmt,a.dblTipAmount,0.00 Adv,a.dblGrandTotal,ifnull(a.strDiscountRemark,'') "
		    + "from tblbillhd a "
		    + "left outer join tbltablemaster b on a.strTableNo=b.strTableNo "
		    + "WHERE DATE(a.dteBillDate) BETWEEN '" + fromDate + "' AND '" + toDate + "'    "
		    + " and a.dblDiscountAmt>0 ");
	    if (!POSCode.equalsIgnoreCase("All"))
	    {
		sqlVoidBillBuilder.append("AND a.strPosCode='" + POSCode + "' ");
	    }
	    sqlVoidBillBuilder.append(" group by a.strBillNo "
		    + " order by a.strBillNo ");

	    ResultSet rsVoidBill = clsGlobalVarClass.dbMysql.executeResultSet(sqlVoidBillBuilder.toString());
	    StringBuilder sbSettle = new StringBuilder();
	    ResultSet rsSettlemnet = null;
	    while (rsVoidBill.next())
	    {
		pw.println();
		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(1), 10, "left"));
		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(2), 10, "left"));
		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(3), 10, "left"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(rsVoidBill.getDouble(4)), 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(rsVoidBill.getDouble(5)), 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(rsVoidBill.getDouble(6)), 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(rsVoidBill.getDouble(7)), 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(rsVoidBill.getDouble(8)), 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(rsVoidBill.getDouble(9)), 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(rsVoidBill.getDouble(10)), 10, "Right"));

		sbSettle.setLength(0);
		sbSettle.append("select a.strSettlementCode,a.dblSettlementAmt,  b.strSettelmentDesc  from tblbillsettlementdtl a "
			+ "left outer join tblsettelmenthd b on a.strSettlementCode=b.strSettelmentCode "
			+ " where a.strBillNo='" + rsVoidBill.getString(3) + "';");
		rsSettlemnet = clsGlobalVarClass.dbMysql.executeResultSet(sbSettle.toString());
		String strSettlement = "";
		while (rsSettlemnet.next())
		{
		    if (strSettlement.length() == 0)
		    {
			strSettlement = rsSettlemnet.getString(3);
		    }
		    else
		    {
			strSettlement = strSettlement + "," + rsSettlemnet.getString(3);
		    }

		}
		pw.print(objUtility.funPrintTextWithAlignment("  " + strSettlement, 12, "left"));
		pw.print(objUtility.funPrintTextWithAlignment(" " + rsVoidBill.getString(11), 20, "left"));

		totalSubTotal += rsVoidBill.getDouble(4);
		totalDiscAmt += rsVoidBill.getDouble(6);
		totalTaxAmt += rsVoidBill.getDouble(7);
		advTotal += rsVoidBill.getDouble(9);
		totalGTTotal += rsVoidBill.getDouble(10);

		noDiscountedBilled = false;
	    }
	    rsVoidBill.close();

	    sqlVoidBillBuilder.setLength(0);
	    sqlVoidBillBuilder.append(" select DATE_FORMAT(a.dteBillDate,'%d/%m/%y')dteBillDate,ifnull(b.strTableName,'')strTableName,a.strBillNo "
		    + ",a.dblSubTotal,a.dblDiscountPer,a.dblDiscountAmt,a.dblTaxAmt,a.dblTipAmount,0.00 Adv,a.dblGrandTotal,ifnull(a.strDiscountRemark,'') "
		    + "from tblqbillhd a "
		    + "left outer join tbltablemaster b on a.strTableNo=b.strTableNo "
		    //+ "left outer join tblreasonmaster c on a.strReasonCode=c.strReasonCode "
		    + "WHERE DATE(a.dteBillDate) BETWEEN '" + fromDate + "' AND '" + toDate + "'    "
		    + " and a.dblDiscountAmt>0 ");
	    if (!POSCode.equalsIgnoreCase("All"))
	    {
		sqlVoidBillBuilder.append("AND a.strPosCode='" + POSCode + "' ");
	    }
	    sqlVoidBillBuilder.append(" group by a.strBillNo "
		    + " order by a.strBillNo ");

	    rsVoidBill = clsGlobalVarClass.dbMysql.executeResultSet(sqlVoidBillBuilder.toString());
	    while (rsVoidBill.next())
	    {
		pw.println();
		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(1), 10, "left"));
		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(2), 10, "left"));
		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(3), 10, "left"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(rsVoidBill.getDouble(4)), 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(rsVoidBill.getDouble(5)), 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(rsVoidBill.getDouble(6)), 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(rsVoidBill.getDouble(7)), 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(rsVoidBill.getDouble(8)), 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(rsVoidBill.getDouble(9)), 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(rsVoidBill.getDouble(10)), 10, "Right"));

		sbSettle.setLength(0);
		sbSettle.append("select a.strSettlementCode,a.dblSettlementAmt,  b.strSettelmentDesc  from tblqbillsettlementdtl a "
			+ "left outer join tblsettelmenthd b on a.strSettlementCode=b.strSettelmentCode "
			+ " where a.strBillNo='" + rsVoidBill.getString(3) + "';");
		rsSettlemnet = clsGlobalVarClass.dbMysql.executeResultSet(sbSettle.toString());
		String strSettlement = "";
		while (rsSettlemnet.next())
		{
		    if (strSettlement.length() == 0)
		    {
			strSettlement = rsSettlemnet.getString(3);
		    }
		    else
		    {
			strSettlement = strSettlement + "," + rsSettlemnet.getString(3);
		    }
		}

		pw.print(objUtility.funPrintTextWithAlignment("  " + strSettlement, 12, "left"));
		pw.print(objUtility.funPrintTextWithAlignment(" " + rsVoidBill.getString(11), 20, "left"));

		totalSubTotal += rsVoidBill.getDouble(4);
		totalDiscAmt += rsVoidBill.getDouble(6);
		totalTaxAmt += rsVoidBill.getDouble(7);
		totalTipAmt += rsVoidBill.getDouble(8);
		advTotal += rsVoidBill.getDouble(9);
		totalGTTotal += rsVoidBill.getDouble(10);

		noDiscountedBilled = false;
	    }
	    rsVoidBill.close();

	    pw.println();
	    pw.print(DASHLINE);

	    pw.println();
	    pw.print(objUtility.funPrintTextWithAlignment("Total", 10, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment("", 10, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment("", 10, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(totalSubTotal), 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(totalDiscAmt), 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(totalTaxAmt), 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(totalTipAmt), 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(advTotal), 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(totalGTTotal), 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment(" ", 20, "left"));

	    pw.println();
	    pw.print(DASHLINE);
	    if (noDiscountedBilled)
	    {
		pw.println();
		pw.print("No Discounted Bills");
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}

	return 1;
    }

// Complimentary Bill Data
    private int funComplimentryBills(String fromDate, String toDate, String POSCode, PrintWriter pw) throws Exception
    {
	pw.println();
	pw.println();
	pw.println();
	pw.println("COMPLIMENTARY BILLS");
	pw.println("---------------------------------------------");

	//clsUtility objUtility=new clsUtility();
	clsUtility objUtility = (clsUtility) objUtility1.clone();
	int cntLine = 0;
	StringBuilder sbSqlLiveFile = new StringBuilder();
	StringBuilder sbSqlQFile = new StringBuilder();
	sbSqlLiveFile.setLength(0);
	sbSqlQFile.setLength(0);
	StringBuilder sbSql = new StringBuilder();
	sbSql.setLength(0);

	Map<String, Map<Integer, List<String>>> hmSettlementDtl = new HashMap<String, Map<Integer, List<String>>>();
	String sql = "select strSettelmentCode,strSettelmentDesc "
		+ " from tblsettelmenthd where strSettelmentType='Complementary'";
	ResultSet rsSettlement = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	while (rsSettlement.next())
	{
	    Map<Integer, List<String>> hmComplimentaryBills = new HashMap<Integer, List<String>>();

	    int cntBill = 1;
	    sbSqlLiveFile.setLength(0);
	    sbSqlLiveFile.append("select d.strSettelmentDesc,a.strBillNo,(sum(b.dblAmount)+sum(b.dblTaxAmount))"
		    + " ,a.strUserCreated,c.strRemark,strReasonName "
		    + " from tblbillhd a,tblbillcomplementrydtl b,tblbillsettlementdtl c,tblsettelmenthd d,tblreasonmaster e "
		    + " where a.strBillNo=b.strBillNo "
		    + " and a.strBillNo=c.strBillNo  "
		    + " and c.strSettlementCode=d.strSettelmentCode "
		    + " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and a.strClientCode=b.strClientCode "
		    + " and a.strClientCode=c.strClientCode "
		    + " and a.strReasonCode=e.strReasonCode "
		    + " and c.strSettlementCode='" + rsSettlement.getString(1) + "' ");
	    if (!POSCode.equalsIgnoreCase("All"))
	    {
		sbSqlLiveFile.append(" AND a.strPosCode='" + POSCode + "' ");
	    }
	    sbSqlLiveFile.append(" group by a.strBillNo,d.strSettelmentDesc,d.strSettelmentCode,a.strUserCreated "
		    + " order by d.strSettelmentDesc,a.strBillNo ");
	    ResultSet rsCompBills = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlLiveFile.toString());
	    while (rsCompBills.next())
	    {
		List<String> arrListBillDtl = new ArrayList<String>();
		arrListBillDtl.add(rsCompBills.getString(1));   // Settlement Desc
		arrListBillDtl.add(rsCompBills.getString(2));   // Bill No
		arrListBillDtl.add(rsCompBills.getString(3));   // Bill Amt
		arrListBillDtl.add(rsCompBills.getString(4));   // Generated By
		arrListBillDtl.add(rsCompBills.getString(5));   // Remarks
		arrListBillDtl.add(rsCompBills.getString(6));   // Reason

		hmComplimentaryBills.put(cntBill, arrListBillDtl);
		cntBill++;
	    }
	    rsCompBills.close();

	    sbSqlQFile.setLength(0);
	    sbSqlQFile.append("select d.strSettelmentDesc,a.strBillNo,(sum(b.dblAmount)+sum(b.dblTaxAmount))"
		    + " ,a.strUserCreated,c.strRemark,e.strReasonName "
		    + " from tblqbillhd a,tblqbillcomplementrydtl b,tblqbillsettlementdtl c,tblsettelmenthd d,tblreasonmaster e "
		    + " where a.strBillNo=b.strBillNo "
		    + " and a.strBillNo=c.strBillNo "
		    + " and c.strSettlementCode=d.strSettelmentCode "
		    + " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and a.strClientCode=b.strClientCode "
		    + " and a.strClientCode=c.strClientCode "
		    + " and a.strReasonCode=e.strReasonCode "
		    + " and c.strSettlementCode='" + rsSettlement.getString(1) + "' ");
	    if (!POSCode.equalsIgnoreCase("All"))
	    {
		sbSqlQFile.append(" AND a.strPosCode='" + POSCode + "' ");
	    }
	    sbSqlQFile.append(" group by a.strBillNo,d.strSettelmentDesc,d.strSettelmentCode,a.strUserCreated "
		    + " order by d.strSettelmentDesc,a.strBillNo ");
	    rsCompBills = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlQFile.toString());
	    while (rsCompBills.next())
	    {
		List<String> arrListBillDtl = new ArrayList<String>();
		arrListBillDtl.add(rsCompBills.getString(1));   // Settlement Desc
		arrListBillDtl.add(rsCompBills.getString(2));   // Bill No
		arrListBillDtl.add(rsCompBills.getString(3));   // Bill Amt
		arrListBillDtl.add(rsCompBills.getString(4));   // Generated By
		arrListBillDtl.add(rsCompBills.getString(5));   // Remarks
		arrListBillDtl.add(rsCompBills.getString(6));   // Reason

		hmComplimentaryBills.put(cntBill, arrListBillDtl);
		cntBill++;
	    }
	    rsCompBills.close();
	    hmSettlementDtl.put(rsSettlement.getString(2), hmComplimentaryBills);
	}

	cntLine = 100;

	for (Map.Entry<String, Map<Integer, List<String>>> entry : hmSettlementDtl.entrySet())
	{
	    pw.println(objUtility.funPrintTextWithAlignment(entry.getKey(), entry.getKey().length(), "Left"));
	    for (int cn = 0; cn < entry.getKey().length(); cn++)
	    {
		pw.print("-");
	    }
	    pw.println();

	    pw.print(objUtility.funPrintTextWithAlignment("Bill Nos|", 20, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("Bill Amount|", 20, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("Generated By|", 20, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("Reason|", 20, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("Remark|", 20, "Right"));
	    pw.println();

	    for (int cn = 0; cn < cntLine; cn++)
	    {
		pw.print("-");
	    }
	    pw.println();

	    int billCount = 0;
	    double totalBillAmt = 0;

	    for (Map.Entry<Integer, List<String>> entryBillDtl : entry.getValue().entrySet())
	    {
		pw.print(objUtility.funPrintTextWithAlignment(entryBillDtl.getValue().get(1) + "|", 20, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(entryBillDtl.getValue().get(2) + "|", 20, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(entryBillDtl.getValue().get(3) + "|", 20, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(entryBillDtl.getValue().get(5) + "|", 20, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(entryBillDtl.getValue().get(4) + "|", 20, "Right"));
		pw.println();

		totalBillAmt += Double.parseDouble(entryBillDtl.getValue().get(2));
		billCount++;
	    }

	    for (int cn = 0; cn < cntLine; cn++)
	    {
		pw.print("-");
	    }
	    pw.println();

	    pw.print(objUtility.funPrintTextWithAlignment("Total(" + billCount + ")", 20, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment(totalBillAmt + "|", 20, "Right"));

	    pw.println();
	}

	return 1;
    }

// Shift Wise Taxation
    private int funShiftWiseTaxationData(String fromDate, String toDate, String POSCode, PrintWriter pw) throws Exception
    {
	pw.println();
	pw.println();
	pw.println("SALE BY SHIFT");
	pw.println("---------------------------------------------");

	clsUtility objUtility = new clsUtility();
	int cntLine = 0;
	StringBuilder sbSqlLiveFile = new StringBuilder();
	StringBuilder sbSqlQFile = new StringBuilder();
	sbSqlLiveFile.setLength(0);
	sbSqlQFile.setLength(0);
	StringBuilder sbSql = new StringBuilder();
	sbSql.setLength(0);

	Map<Integer, String> mapTaxHeaders = new TreeMap<Integer, String>();
	Map<String, Map<String, Double>> hmShiftWiseData = new HashMap<String, Map<String, Double>>();

	int cntTax = 1;
	String sqlTax = "select c.strTaxCode "
		+ " from tblbillhd a,tblbilltaxdtl b,tbltaxhd c "
		+ " where a.strBillNo=b.strBillNo and b.strTaxCode=c.strTaxCode "
		+ " and a.strClientCode=b.strClientCode "
		+ " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' ";
	if (!POSCode.equalsIgnoreCase("All"))
	{
	    sqlTax += " and a.strPOSCode='" + POSCode + "' ";
	}
	sqlTax += " group by c.strTaxCode";
	ResultSet rsTaxDtl1 = clsGlobalVarClass.dbMysql.executeResultSet(sqlTax);
	while (rsTaxDtl1.next())
	{
	    mapTaxHeaders.put(cntTax, rsTaxDtl1.getString(1));
	    cntTax++;
	}
	rsTaxDtl1.close();

	sqlTax = "select c.strTaxCode "
		+ " from tblqbillhd a,tblqbilltaxdtl b,tbltaxhd c "
		+ " where a.strBillNo=b.strBillNo and b.strTaxCode=c.strTaxCode "
		+ " and a.strClientCode=b.strClientCode "
		+ " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' ";
	if (!POSCode.equalsIgnoreCase("All"))
	{
	    sqlTax += " and a.strPOSCode='" + POSCode + "' ";
	}
	sqlTax += " group by c.strTaxCode";
	rsTaxDtl1 = clsGlobalVarClass.dbMysql.executeResultSet(sqlTax);
	while (rsTaxDtl1.next())
	{
	    if (!mapTaxHeaders.containsValue(rsTaxDtl1.getString(1)))
	    {
		mapTaxHeaders.put(cntTax, rsTaxDtl1.getString(1));
		cntTax++;
	    }
	}
	rsTaxDtl1.close();

	sbSqlLiveFile.append(" select a.intShiftCode,sum(a.dblSubTotal)-sum(a.dblDiscountAmt),sum(a.dblDiscountAmt) "
		+ " from tblbillhd a "
		+ " where date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' ");
	if (!POSCode.equalsIgnoreCase("All"))
	{
	    sbSqlLiveFile.append(" and a.strPOSCode='" + POSCode + "' ");
	}
	sbSqlLiveFile.append(" group by a.intShiftCode ");
	System.out.println(sbSqlLiveFile);
	ResultSet rsShiftWise = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlLiveFile.toString());
	while (rsShiftWise.next())
	{
	    Map<String, Double> hmShiftWiseDtlData = new HashMap<String, Double>();
	    double shiftSubTotal = rsShiftWise.getDouble(2);
	    double shiftDiscAmt = rsShiftWise.getDouble(3);

	    if (hmShiftWiseData.containsKey(rsShiftWise.getString(1)))
	    {
		hmShiftWiseDtlData.put("Discount", hmShiftWiseDtlData.get("Discount") + shiftDiscAmt);
	    }
	    else
	    {
		hmShiftWiseDtlData.put("Discount", shiftDiscAmt);
	    }

	    if (hmShiftWiseData.containsKey(rsShiftWise.getString(1)))
	    {
		hmShiftWiseDtlData.put("Amount", hmShiftWiseDtlData.get("Amount") + shiftSubTotal);
	    }
	    else
	    {
		hmShiftWiseDtlData.put("Amount", shiftSubTotal);
	    }

	    Map<String, String> hmTaxDtl = new HashMap<String, String>();
	    sbSql.setLength(0);
	    sbSql.append("select sum(b.dblTaxAmount),sum(b.dblTaxableAmount),c.strTaxCode,c.strTaxIndicator"
		    + " ,c.strTaxOnTax,c.strTaxOnTaxCode,c.strTaxOnGD,c.strOperationType "
		    + " from tblbillhd a,tblbilltaxdtl b,tbltaxhd c "
		    + " where a.strBillNo=b.strBillNo and b.strTaxCode=c.strTaxCode "
		    + " and a.intShiftCode='" + rsShiftWise.getString(1) + "' and a.strClientCode=b.strClientCode "
		    + " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' ");
	    if (!POSCode.equalsIgnoreCase("All"))
	    {
		sbSql.append(" and a.strPOSCode='" + POSCode + "' ");
	    }
	    sbSql.append(" group by b.strTaxCode order by c.strTaxOnTax");
	    ResultSet rsTax = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
	    while (rsTax.next())
	    {
		double taxAmt = rsTax.getDouble(1);
		double taxableAmt = rsTax.getDouble(2);

		if (hmShiftWiseData.containsKey(rsShiftWise.getString(1)))
		{
		    hmShiftWiseDtlData = hmShiftWiseData.get(rsShiftWise.getString(1));
		    if (hmShiftWiseDtlData.containsKey(rsTax.getString(3)))
		    {
			hmShiftWiseDtlData.put(rsTax.getString(3), hmShiftWiseDtlData.get(rsTax.getString(3)) + taxAmt);
		    }
		    else
		    {
			hmShiftWiseDtlData.put(rsTax.getString(3), taxAmt);
		    }
		}
		else
		{
		    hmShiftWiseDtlData.put(rsTax.getString(3), taxAmt);
		}
	    }
	    rsTax.close();

	    sbSql.setLength(0);
	    sbSql.append("select count(strBillNo) from tblbillhd a "
		    + " where a.intShiftCode='" + rsShiftWise.getString(1) + "' "
		    + " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' ");
	    if (!POSCode.equalsIgnoreCase("All"))
	    {
		sbSql.append(" and a.strPOSCode='" + POSCode + "' ");
	    }
	    ResultSet rsBillCount = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
	    if (rsBillCount.next())
	    {
		hmShiftWiseDtlData.put("NoOfBills", rsBillCount.getDouble(1));
	    }
	    rsBillCount.close();

	    hmShiftWiseData.put(rsShiftWise.getString(1), hmShiftWiseDtlData);
	}
	rsShiftWise.close();

	sbSqlQFile.append(" select a.intShiftCode,sum(a.dblSubTotal)-sum(a.dblDiscountAmt),sum(a.dblDiscountAmt) "
		+ " from tblqbillhd a "
		+ " where date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' ");
	if (!POSCode.equalsIgnoreCase("All"))
	{
	    sbSqlQFile.append(" and a.strPOSCode='" + POSCode + "' ");
	}
	sbSqlQFile.append(" group by a.intShiftCode ");
	System.out.println(sbSqlQFile);
	rsShiftWise = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlQFile.toString());
	while (rsShiftWise.next())
	{
	    Map<String, Double> hmShiftWiseDtlData = new HashMap<String, Double>();
	    double shiftSubTotal = rsShiftWise.getDouble(2);
	    double shiftDiscAmt = rsShiftWise.getDouble(3);

	    if (hmShiftWiseData.containsKey(rsShiftWise.getString(1)))
	    {
		if (hmShiftWiseDtlData.containsKey("Discount"))
		{
		    hmShiftWiseDtlData.put("Discount", hmShiftWiseDtlData.get("Discount") + shiftDiscAmt);
		}
		else
		{
		    hmShiftWiseDtlData.put("Discount", shiftDiscAmt);
		}
	    }
	    else
	    {
		hmShiftWiseDtlData.put("Discount", shiftDiscAmt);
	    }

	    if (hmShiftWiseData.containsKey(rsShiftWise.getString(1)))
	    {
		if (hmShiftWiseDtlData.containsKey("Amount"))
		{
		    hmShiftWiseDtlData.put("Amount", hmShiftWiseDtlData.get("Amount") + shiftSubTotal);
		}
		else
		{
		    hmShiftWiseDtlData.put("Amount", shiftSubTotal);
		}
	    }
	    else
	    {
		hmShiftWiseDtlData.put("Amount", shiftSubTotal);
	    }

	    Map<String, String> hmTaxDtl = new HashMap<String, String>();
	    sbSql.setLength(0);
	    sbSql.append("select sum(b.dblTaxAmount),sum(b.dblTaxableAmount),c.strTaxCode,c.strTaxIndicator"
		    + " ,c.strTaxOnTax,c.strTaxOnTaxCode,c.strTaxOnGD,c.strOperationType "
		    + " from tblqbillhd a,tblqbilltaxdtl b,tbltaxhd c "
		    + " where a.strBillNo=b.strBillNo and b.strTaxCode=c.strTaxCode "
		    + " and a.intShiftCode='" + rsShiftWise.getString(1) + "' and a.strClientCode=b.strClientCode "
		    + " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' ");
	    if (!POSCode.equalsIgnoreCase("All"))
	    {
		sbSql.append(" and a.strPOSCode='" + POSCode + "' ");
	    }
	    sbSql.append(" group by b.strTaxCode order by c.strTaxOnTax");
	    ResultSet rsTax = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
	    while (rsTax.next())
	    {
		double taxAmt = rsTax.getDouble(1);
		double taxableAmt = rsTax.getDouble(2);

		if (hmShiftWiseData.containsKey(rsShiftWise.getString(1)))
		{
		    hmShiftWiseDtlData = hmShiftWiseData.get(rsShiftWise.getString(1));
		    if (hmShiftWiseDtlData.containsKey(rsTax.getString(3)))
		    {
			hmShiftWiseDtlData.put(rsTax.getString(3), hmShiftWiseDtlData.get(rsTax.getString(3)) + taxAmt);
		    }
		    else
		    {
			hmShiftWiseDtlData.put(rsTax.getString(3), taxAmt);
		    }
		}
		else
		{
		    hmShiftWiseDtlData.put(rsTax.getString(3), taxAmt);
		}
	    }
	    rsTax.close();

	    sbSql.setLength(0);
	    sbSql.append("select count(strBillNo) from tblqbillhd a "
		    + " where a.intShiftCode='" + rsShiftWise.getString(1) + "' "
		    + " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' ");
	    if (!POSCode.equalsIgnoreCase("All"))
	    {
		sbSql.append(" and a.strPOSCode='" + POSCode + "' ");
	    }
	    ResultSet rsBillCount = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
	    if (rsBillCount.next())
	    {
		if (hmShiftWiseData.containsKey("NoOfBills"))
		{
		    hmShiftWiseDtlData = hmShiftWiseData.get("NoOfBills");
		    hmShiftWiseDtlData.put("NoOfBills", hmShiftWiseDtlData.get("NoOfBills") + rsBillCount.getDouble(1));
		}
		else
		{
		    hmShiftWiseDtlData.put("NoOfBills", rsBillCount.getDouble(1));
		}
	    }
	    rsBillCount.close();

	    hmShiftWiseData.put(rsShiftWise.getString(1), hmShiftWiseDtlData);
	}
	rsShiftWise.close();

	Map<String, Double> mapVerticalTotal = new HashMap<String, Double>();
	//Map<Integer ,String> mapSequence=new TreeMap<Integer,String>();
	cntLine += 20;
	cntLine += 27;
	pw.print(objUtility.funPrintTextWithAlignment("Group Name|", 20, "Right"));
	pw.print(objUtility.funPrintTextWithAlignment("Amount|", 27, "Right"));

	String sql = "";
	for (Map.Entry<Integer, String> entry : mapTaxHeaders.entrySet())
	{
	    sql = "select strTaxDesc,strTaxShortName from tbltaxhd where strTaxCode='" + entry.getValue() + "' ";

	    ResultSet rsTaxDescDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsTaxDescDtl.next())
	    {
		pw.print(objUtility.funPrintTextWithAlignment(rsTaxDescDtl.getString(2) + "|", 27, "Right"));
		cntLine += 27;
	    }
	    rsTaxDescDtl.close();
	}

	pw.print(objUtility.funPrintTextWithAlignment("Discount|", 20, "Right"));
	pw.print(objUtility.funPrintTextWithAlignment("Total|", 27, "Right"));
	pw.print(objUtility.funPrintTextWithAlignment("No Of Bills|", 11, "Right"));
	pw.println();

	cntLine += 20;
	cntLine += 27;
	cntLine += 27;
	for (int cn = 0; cn < cntLine; cn++)
	{
	    pw.print("-");
	}
	pw.println();

	double totalShiftAmt = 0, totalDiscAmt = 0, totalNoOfBills = 0;
	for (Map.Entry<String, Map<String, Double>> entry : hmShiftWiseData.entrySet())
	{
	    double totalVerticalAmt = 0;
	    System.out.println(entry.getKey());
	    pw.print(objUtility.funPrintTextWithAlignment(entry.getKey() + "|", 20, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment(decFormatter.format(entry.getValue().get("Amount")) + "|", 27, "Right"));
	    totalShiftAmt += entry.getValue().get("Amount");
	    totalVerticalAmt += entry.getValue().get("Amount");

	    int count = 1;
	    for (Map.Entry<Integer, String> entryTax : mapTaxHeaders.entrySet())
	    {
		if (entry.getValue().containsKey(entryTax.getValue()))
		{
		    pw.print(objUtility.funPrintTextWithAlignment(decFormatter.format(entry.getValue().get(entryTax.getValue())) + "|", 27, "Right"));
		    totalVerticalAmt += entry.getValue().get(entryTax.getValue());

		    if (mapVerticalTotal.containsKey(entryTax.getValue()))
		    {
			double tempTaxAmt = mapVerticalTotal.get(entryTax.getValue());
			mapVerticalTotal.put(entryTax.getValue(), entry.getValue().get(entryTax.getValue()) + tempTaxAmt);
		    }
		    else
		    {
			mapVerticalTotal.put(entryTax.getValue(), entry.getValue().get(entryTax.getValue()));
		    }
		    //mapSequence.put(count,entryTax.getValue());
		    count++;
		}
		else
		{
		    //mapVerticalTotal.put(entryTax.getValue(), 0.00);
		    pw.print(objUtility.funPrintTextWithAlignment("0.0|", 27, "Right"));
		}
	    }

	    pw.print(objUtility.funPrintTextWithAlignment(decFormatter.format(entry.getValue().get("Discount")) + "|", 20, "Right"));
	    totalDiscAmt += entry.getValue().get("Discount");

	    if (mapVerticalTotal.containsKey("TotalHorizontalAmt"))
	    {
		mapVerticalTotal.put("TotalHorizontalAmt", mapVerticalTotal.get("TotalHorizontalAmt") + totalVerticalAmt);
	    }
	    else
	    {
		mapVerticalTotal.put("TotalHorizontalAmt", totalVerticalAmt);
	    }

	    pw.print(objUtility.funPrintTextWithAlignment(decFormatter.format(totalVerticalAmt) + "|", 27, "Right"));

	    pw.print(objUtility.funPrintTextWithAlignment(decFormatter.format(entry.getValue().get("NoOfBills")) + "|", 10, "Right"));
	    totalNoOfBills += entry.getValue().get("NoOfBills");

	    pw.println();
	}

	mapVerticalTotal.put("TotalAmount", totalShiftAmt);

	for (int cn = 0; cn < cntLine; cn++)
	{
	    pw.print("-");
	}
	pw.println();

	pw.print(objUtility.funPrintTextWithAlignment("Total|", 20, "Right"));
	pw.print(objUtility.funPrintTextWithAlignment(String.valueOf(decFormatter.format(mapVerticalTotal.get("TotalAmount"))) + "|", 27, "Right"));

	for (Map.Entry<Integer, String> entry : mapTaxHeaders.entrySet())
	{
	    System.out.println(entry.getKey() + "   " + entry.getValue());
	    if (mapVerticalTotal.containsKey(entry.getValue()))
	    {
		pw.print(objUtility.funPrintTextWithAlignment(String.valueOf(decFormatter.format(mapVerticalTotal.get(entry.getValue()))) + "|", 27, "Right"));
	    }
	}

	pw.print(objUtility.funPrintTextWithAlignment(decFormatter.format(totalDiscAmt) + "|", 20, "Right"));
	if (mapVerticalTotal.get("TotalHorizontalAmt") == null)
	{
	    mapVerticalTotal.put("TotalHorizontalAmt", 0.00);
	}
	pw.print(objUtility.funPrintTextWithAlignment(String.valueOf(decFormatter.format(mapVerticalTotal.get("TotalHorizontalAmt"))) + "|", 27, "Right"));

	pw.print(objUtility.funPrintTextWithAlignment(String.valueOf(decFormatter.format(totalNoOfBills)) + "|", 10, "Right"));

	pw.println();
	pw.println();

	return 1;
    }

// Menu Head Wise Bill Data
    private int funMenuHeadWiseSales(String fromDate, String toDate, String POSCode, PrintWriter pw) throws Exception
    {
	pw.println();
	pw.println();
	pw.println("MENU HEAD ANALYSIS");
	pw.println("---------------------------------------------");

	//clsUtility objUtility=new clsUtility();
	clsUtility objUtility = (clsUtility) objUtility1.clone();
	int cntLine = 0;
	StringBuilder sbSqlLiveFile = new StringBuilder();
	StringBuilder sbSqlQFile = new StringBuilder();
	sbSqlLiveFile.setLength(0);
	sbSqlQFile.setLength(0);
	StringBuilder sbSql = new StringBuilder();
	sbSql.setLength(0);

	Map<String, List<String>> hmMenuHeadWiseTemp = new HashMap<String, List<String>>();
	Map<String, List<String>> hmMenuHeadWise = new HashMap<String, List<String>>();

	double totalAmount = 0;
	int cntBill = 1;
	sbSqlLiveFile.append("select d.strMenuCode,d.strMenuName,sum(b.dblAmount)+sum(b.dblTaxAmount),sum(b.dblQuantity) "
		+ " from tblbillhd a,tblbilldtl b,tblmenuitempricingdtl c,tblmenuhd d "
		+ " where a.strBillNo=b.strBillNo and b.strItemCode=c.strItemCode and c.strMenuCode=d.strMenuCode "
		+ " and a.strClientCode=b.strClientCode and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		+ " and a.strposcode =c.strposcode ");
	if (clsGlobalVarClass.gAreaWisePricing.equals("Y"))
	{
	    sbSqlLiveFile.append(" and a.strAreaCode=c.strAreaCode ");
	}
	if (!POSCode.equalsIgnoreCase("All"))
	{
	    sbSqlLiveFile.append(" AND a.strPosCode='" + POSCode + "' ");
	}
	sbSqlLiveFile.append(" group by d.strMenuCode,d.strMenuName order by d.strMenuName ");
	ResultSet rsMenuHead = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlLiveFile.toString());
	while (rsMenuHead.next())
	{
	    List<String> arrListBillDtl = new ArrayList<String>();
	    arrListBillDtl.add(rsMenuHead.getString(1));   // Menu Head Code
	    arrListBillDtl.add(rsMenuHead.getString(2));   // Menu Head Name
	    arrListBillDtl.add(rsMenuHead.getString(3));   // Menu Head Amt
	    arrListBillDtl.add(rsMenuHead.getString(4));   // Menu Head Qty

	    hmMenuHeadWiseTemp.put(rsMenuHead.getString(1), arrListBillDtl);
	    cntBill++;

	    totalAmount += rsMenuHead.getDouble(3);
	}
	rsMenuHead.close();

	sbSqlQFile.append("select d.strMenuCode,d.strMenuName,sum(b.dblAmount)+sum(b.dblTaxAmount),sum(b.dblQuantity) "
		+ " from tblqbillhd a,tblqbilldtl b,tblmenuitempricingdtl c,tblmenuhd d "
		+ " where a.strBillNo=b.strBillNo and b.strItemCode=c.strItemCode and c.strMenuCode=d.strMenuCode "
		+ " and a.strClientCode=b.strClientCode and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		+ " and a.strposcode =c.strposcode ");
	if (clsGlobalVarClass.gAreaWisePricing.equals("Y"))
	{
	    sbSqlQFile.append(" and a.strAreaCode=c.strAreaCode ");
	}
	if (!POSCode.equalsIgnoreCase("All"))
	{
	    sbSqlQFile.append(" AND a.strPosCode='" + POSCode + "' ");
	}
	sbSqlQFile.append(" group by d.strMenuCode,d.strMenuName order by d.strMenuName ");
	rsMenuHead = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlQFile.toString());
	while (rsMenuHead.next())
	{
	    List<String> arrListBillDtl = new ArrayList<String>();

	    if (hmMenuHeadWiseTemp.containsKey(rsMenuHead.getString(1)))
	    {
		arrListBillDtl = hmMenuHeadWiseTemp.get(rsMenuHead.getString(1));
		double amt = Double.parseDouble(arrListBillDtl.get(2));
		double qty = Double.parseDouble(arrListBillDtl.get(3));
		arrListBillDtl.add(2, String.valueOf(amt + rsMenuHead.getDouble(3)));   // Menu Head Amt
		arrListBillDtl.add(3, String.valueOf(amt + rsMenuHead.getDouble(4)));   // Menu Head Qty
	    }
	    else
	    {
		arrListBillDtl.add(rsMenuHead.getString(1));   // Menu Head Code
		arrListBillDtl.add(rsMenuHead.getString(2));   // Menu Head Name
		arrListBillDtl.add(rsMenuHead.getString(3));   // Menu Head Amt
		arrListBillDtl.add(rsMenuHead.getString(4));   // Menu Head Qty
	    }
	    hmMenuHeadWiseTemp.put(rsMenuHead.getString(1), arrListBillDtl);
	    cntBill++;

	    totalAmount += rsMenuHead.getDouble(3);
	}
	rsMenuHead.close();

	DecimalFormat objDecFormat = new DecimalFormat("####0.00");
	for (Map.Entry<String, List<String>> entry : hmMenuHeadWiseTemp.entrySet())
	{
	    List<String> arrListBillDtl = entry.getValue();
	    double amt = Double.parseDouble(arrListBillDtl.get(2));
	    double per = ((amt / totalAmount) * 100);
	    System.out.println("Amt= " + amt + "\ttotalAmount= " + totalAmount + "\tPer" + objDecFormat.format(per));

	    arrListBillDtl.add(String.valueOf(objDecFormat.format(per)));
	    hmMenuHeadWise.put(entry.getKey(), arrListBillDtl);
	}

	cntLine = 80;
	pw.print(objUtility.funPrintTextWithAlignment("Menu Head Name|", 30, "Right"));
	pw.print(objUtility.funPrintTextWithAlignment("Amount|", 20, "Right"));
	pw.print(objUtility.funPrintTextWithAlignment("Qty|", 20, "Right"));
	pw.print(objUtility.funPrintTextWithAlignment("Section|", 10, "Right"));
	pw.println();

	for (int cn = 0; cn < cntLine; cn++)
	{
	    pw.print("-");
	}
	pw.println();

	double totalMenuHeadAmt = 0, totalMenuHeadQty = 0;

	for (Map.Entry<String, List<String>> entry : hmMenuHeadWise.entrySet())
	{
	    pw.print(objUtility.funPrintTextWithAlignment(entry.getValue().get(1) + "|", 30, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment(entry.getValue().get(2) + "|", 20, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment(entry.getValue().get(3) + "|", 20, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment(entry.getValue().get(4) + "|", 10, "Right"));
	    pw.println();

	    totalMenuHeadAmt += Double.parseDouble(entry.getValue().get(2));
	    totalMenuHeadQty += Double.parseDouble(entry.getValue().get(3));
	}

	for (int cn = 0; cn < cntLine; cn++)
	{
	    pw.print("-");
	}
	pw.println();

	pw.print(objUtility.funPrintTextWithAlignment("Total|", 30, "Right"));
	pw.print(objUtility.funPrintTextWithAlignment(totalMenuHeadAmt + "|", 20, "Right"));
	pw.print(objUtility.funPrintTextWithAlignment(totalMenuHeadQty + "|", 20, "Right"));

	pw.println();

	return 1;
    }

// Average Per Bill Ananlysis    
    private int funAvgPerBillAnalysis(String fromDate, String toDate, String POSCode, PrintWriter pw) throws Exception
    {
	clsUtility objUtility = new clsUtility();

	pw.println();
	pw.println();
	pw.println("AVG.PER BILL ANALYSIS");
	pw.println("---------------------------------------------");

	StringBuilder sbSql = new StringBuilder();

	double netSale = 0;
	sbSql.setLength(0);
	sbSql.append("select sum(a.dblSubTotal)-sum(a.dblDiscountAmt) "
		+ " from tblbillhd a where date(a.dteBillDate) between '" + fromDate + "' AND '" + toDate + "' ");
	if (!POSCode.equalsIgnoreCase("All"))
	{
	    sbSql.append(" AND a.strPosCode='" + POSCode + "' ");
	}
	ResultSet rsNetAmt = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
	if (rsNetAmt.next())
	{
	    netSale = rsNetAmt.getDouble(1);
	}
	rsNetAmt.close();

	double grossSale = 0;
	sbSql.setLength(0);
	sbSql.append("select sum(b.dblSettlementAmt) "
		+ " from tblbillhd a,tblbillsettlementdtl b "
		+ " where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + fromDate + "' AND '" + toDate + "' "
		+ " and a.strClientCode=b.strClientCode ");
	if (!POSCode.equalsIgnoreCase("All"))
	{
	    sbSql.append(" AND a.strPosCode='" + POSCode + "' ");
	}
	ResultSet rsGrossAmt = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
	if (rsGrossAmt.next())
	{
	    grossSale = rsGrossAmt.getDouble(1);
	}
	rsGrossAmt.close();

	int noOfBills = 0;
	sbSql.setLength(0);
	sbSql.append("select count(a.strBillNo) "
		+ " from tblbillhd a where date(a.dteBillDate) between '" + fromDate + "' AND '" + toDate + "' ");
	if (!POSCode.equalsIgnoreCase("All"))
	{
	    sbSql.append(" AND a.strPosCode='" + POSCode + "' ");
	}
	ResultSet rsBillCount = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
	if (rsBillCount.next())
	{
	    noOfBills = rsBillCount.getInt(1);
	}
	rsBillCount.close();

	sbSql.setLength(0);
	sbSql.append("select sum(a.dblSubTotal)-sum(a.dblDiscountAmt) "
		+ " from tblqbillhd a where date(a.dteBillDate) between '" + fromDate + "' AND '" + toDate + "' ");
	if (!POSCode.equalsIgnoreCase("All"))
	{
	    sbSql.append(" AND a.strPosCode='" + POSCode + "' ");
	}
	rsNetAmt = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
	if (rsNetAmt.next())
	{
	    netSale += rsNetAmt.getDouble(1);
	}
	rsNetAmt.close();

	sbSql.setLength(0);
	sbSql.append("select sum(b.dblSettlementAmt) "
		+ " from tblqbillhd a,tblqbillsettlementdtl b "
		+ " where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + fromDate + "' AND '" + toDate + "' "
		+ " and a.strClientCode=b.strClientCode ");
	if (!POSCode.equalsIgnoreCase("All"))
	{
	    sbSql.append(" AND a.strPosCode='" + POSCode + "' ");
	}
	rsGrossAmt = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
	if (rsGrossAmt.next())
	{
	    grossSale += rsGrossAmt.getDouble(1);
	}
	rsGrossAmt.close();

	sbSql.setLength(0);
	sbSql.append("select count(a.strBillNo) "
		+ " from tblqbillhd a where date(a.dteBillDate) between '" + fromDate + "' AND '" + toDate + "' ");
	if (!POSCode.equalsIgnoreCase("All"))
	{
	    sbSql.append(" AND a.strPosCode='" + POSCode + "' ");
	}
	rsBillCount = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
	if (rsBillCount.next())
	{
	    noOfBills += rsBillCount.getInt(1);
	}
	rsBillCount.close();

	for (int cnt = 0; cnt < 20; cnt++)
	{
	    pw.print(" ");
	}
	pw.print(objUtility.funPrintTextWithAlignment("|No.Of Bills|", 20, "Right"));
	pw.print(objUtility.funPrintTextWithAlignment("Basic Amt|", 30, "Right"));
	pw.print(objUtility.funPrintTextWithAlignment("APB|", 10, "Right"));
	pw.println();
	for (int cnt = 0; cnt < 80; cnt++)
	{
	    pw.print("-");
	}
	pw.println();

	double apbNetSalePer = Math.rint(netSale / noOfBills);
	pw.print(objUtility.funPrintTextWithAlignment("Net Sale|", 20, "Right"));
	pw.print(objUtility.funPrintTextWithAlignment(noOfBills + "|", 20, "Right"));
	pw.print(objUtility.funPrintTextWithAlignment(netSale + "|", 30, "Right"));
	pw.print(objUtility.funPrintTextWithAlignment(apbNetSalePer + "|", 10, "Right"));
	pw.println();
	for (int cnt = 0; cnt < 80; cnt++)
	{
	    pw.print("-");
	}
	pw.println();

	double apbGrossSalePer = Math.rint(grossSale / noOfBills);
	pw.print(objUtility.funPrintTextWithAlignment("Gross Sale|", 20, "Right"));
	pw.print(objUtility.funPrintTextWithAlignment(noOfBills + "|", 20, "Right"));
	pw.print(objUtility.funPrintTextWithAlignment(grossSale + "|", 30, "Right"));
	pw.print(objUtility.funPrintTextWithAlignment(apbGrossSalePer + "|", 10, "Right"));
	pw.println();

	return 1;
    }

    private void funModifiedKOTs(String fromDate, String toDate, String POSCode, PrintWriter pw)
    {

	try
	{
	    clsUtility objUtility = new clsUtility();
	    SimpleDateFormat yyyyMMddDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    SimpleDateFormat dateFormat = new SimpleDateFormat("E dd-MMM-yyyy");
	    SimpleDateFormat dateFormat24Hrs = new SimpleDateFormat("E dd-MMM-yyyy HH:mm:ss");
	    SimpleDateFormat ddMMyyDateFormat = new SimpleDateFormat("dd/MM/yy");

	    pw.println();
	    pw.println("MODIFIED KOTs");
	    pw.println("************");

	    boolean noModifiedKOTs = true;

//	    pw.print(objUtility.funPrintTextWithAlignment("A/c No", 10, "left"));
//	    pw.print(objUtility.funPrintTextWithAlignment("Item", 40, "left"));
//	    pw.print(objUtility.funPrintTextWithAlignment("Modify Time", 10, "left"));
//	    pw.print(objUtility.funPrintTextWithAlignment("  Modify By", 10, "left"));
//	    pw.println();
//
//	    int noOfColumns = 120;
//	    String DASHLINE = "";
//	    for (int i = 0; i < noOfColumns; i++)
//	    {
//		DASHLINE = DASHLINE + "-";
//	    }
//	    pw.print(DASHLINE);
//
//	    StringBuilder sqlVoidBillBuilder = new StringBuilder();
//
//	    double totalSubTotal = 0, totalAmt = 0.00, totalDiscAmt = 0, totalTaxAmt = 0, totalGTTotal = 0, advTotal = 0, totalTipAmt = 0;
//	    boolean noModifiedKOTs = true;
//	    int noOfBills = 0;
//
//	    sqlVoidBillBuilder.setLength(0);
//	    sqlVoidBillBuilder.append(" select b.strTableName,a.strItemName,time(a.dteVoidedDate),a.strUserCreated "
//		    + "from tblvoidkot a,tbltablemaster b "
//		    + "where date(a.dteVoidedDate) between '" + fromDate + "' and '" + toDate + "' "
//		    + "and a.strTableNo=b.strTableNo "
//		    + "and a.strVoidBillType='Item Void' ");
//	    if (!POSCode.equalsIgnoreCase("All"))
//	    {
//		sqlVoidBillBuilder.append("AND a.strPosCode='" + POSCode + "' ");
//	    }
//	    sqlVoidBillBuilder.append("  "
//		    + " order by b.strTableName ");
//	    ResultSet rsVoidBill = clsGlobalVarClass.dbMysql.executeResultSet(sqlVoidBillBuilder.toString());
//	    while (rsVoidBill.next())
//	    {
//		pw.println();
//		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(1), 10, "left"));
//		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(2), 40, "left"));
//		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(3), 10, "left"));
//		pw.print(objUtility.funPrintTextWithAlignment("  " + rsVoidBill.getString(4), 10, "left"));
//
//		noModifiedKOTs = false;
//	    }
//	    rsVoidBill.close();
//
//	    pw.println();
//	    pw.print(DASHLINE);
	    /**
	     * format 2
	     */
	    StringBuilder sqlVoidKOTBuilder = new StringBuilder();
	    sqlVoidKOTBuilder.append("SELECT a.strKOTNo,a.strItemCode,a.strItemName,e.strTableName,sum(a.dblItemQuantity),sum(a.dblAmount),a.strRemark "
		    + " ,b.strPosCode,b.strPosName,a.strUserCreated,a.dteVoidedDate,d.strReasonName "
		    + " FROM tblvoidkot a "
		    + " LEFT OUTER JOIN tblposmaster b ON a.strPOSCode=b.strPosCode "
		    + " LEFT OUTER JOIN tblreasonmaster d ON a.strReasonCode=d.strReasonCode "
		    + " LEFT OUTER JOIN tbltablemaster e ON a.strTableNo=e.strTableNo "
		    + " WHERE DATE(a.dteVoidedDate) BETWEEN '" + fromDate + "' AND '" + toDate + "'  "
		    + " and a.strVoidBillType='Item Void'  ");
	    if (!POSCode.equalsIgnoreCase("All"))
	    {
		sqlVoidKOTBuilder.append("AND b.strPosCode='" + POSCode + "' ");
	    }
	    sqlVoidKOTBuilder.append("GROUP BY a.strkotno,a.strItemCode "
		    + "ORDER BY a.strkotno,a.strItemCode ");

	    ResultSet rsVoidKOT = clsGlobalVarClass.dbMysql.executeResultSet(sqlVoidKOTBuilder.toString());
	    String kotNo = "";
	    while (rsVoidKOT.next())
	    {
		noModifiedKOTs = false;

		if (rsVoidKOT.isFirst())
		{
		    kotNo = rsVoidKOT.getString(1);
		    pw.println();
		    pw.println("TABLE NO :" + rsVoidKOT.getString(4));
		    pw.println("KOT NO   :" + kotNo);
		    //pw.println("REASON   :" + rsVoidKOT.getString(12));
		    pw.println("REMARKS  :" + rsVoidKOT.getString(7));
		    pw.println("---------------------------------------------");
		    pw.println("ITEMS   ");
		    pw.println("--------");
		    pw.println("        QTY |PARTICULARS");
		    pw.println("        " + rsVoidKOT.getString(5) + "|" + rsVoidKOT.getString(3));
		}
		else
		{
		    if (kotNo.equalsIgnoreCase(rsVoidKOT.getString(1)))
		    {
			pw.println("        " + rsVoidKOT.getString(5) + "|" + rsVoidKOT.getString(3));
		    }
		    else
		    {
			kotNo = rsVoidKOT.getString(1);
			pw.println();
			pw.println("TABLE NO :" + rsVoidKOT.getString(4));
			pw.println("KOT NO   :" + kotNo);
			//pw.println("REASON   :" + rsVoidKOT.getString(12));
			pw.println("REMARKS  :" + rsVoidKOT.getString(7));
			pw.println("---------------------------------------------");
			pw.println("ITEMS   ");
			pw.println("--------");
			pw.println("        QTY |PARTICULARS");
			pw.println("        " + rsVoidKOT.getString(5) + "|" + rsVoidKOT.getString(3));
		    }
		}
	    }
	    rsVoidKOT.close();
	    pw.println();

//	    pw.println();
//	    pw.print(DASHLINE);
	    if (noModifiedKOTs)
	    {
		pw.println();
		pw.print("No Modified KOTs");
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}

    }

    private void funVoidBills(String fromDate, String toDate, String POSCode, PrintWriter pw)
    {
	try
	{
	    clsUtility objUtility = new clsUtility();
	    SimpleDateFormat yyyyMMddDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    SimpleDateFormat dateFormat = new SimpleDateFormat("E dd-MMM-yyyy");
	    SimpleDateFormat dateFormat24Hrs = new SimpleDateFormat("E dd-MMM-yyyy HH:mm:ss");
	    SimpleDateFormat ddMMyyDateFormat = new SimpleDateFormat("dd/MM/yy");

	    pw.println();
	    pw.println("CANCELLED BILLS");
	    pw.println("***************");
	    pw.print(objUtility.funPrintTextWithAlignment("Date", 10, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment("CASHIER", 10, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment("Tbl No", 10, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment("Bill No", 10, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment("Amount", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("Disc.", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("VAT/GST", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("Total", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment(" Reason", 20, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment("Auth By", 10, "left"));
	    pw.println();

	    int noOfColumns = 110;
	    String DASHLINE = "";
	    for (int i = 0; i < noOfColumns; i++)
	    {
		DASHLINE = DASHLINE + "-";
	    }
	    pw.print(DASHLINE);

	    StringBuilder sqlVoidBillBuilder = new StringBuilder();
	    sqlVoidBillBuilder.append(" SELECT date(a.dteModifyVoidBill),a.strUserCreated,ifnull(c.strTableName,''),a.strBillNo "
		    + ",sum(b.dblAmount)Amount,0.00 DiscAmt,sum(b.dblTaxAmount),sum(b.dblAmount)+sum(b.dblTaxAmount)Total "
		    + ",a.strReasonName,a.strUserEdited "
		    + "FROM tblvoidbillhd a  "
		    + "inner join tblvoidbilldtl b on a.strBillNo=b.strBillNo "
		    + "left outer join tbltablemaster c on a.strTableNo=c.strTableNo "
		    + "WHERE DATE(a.dteModifyVoidBill) BETWEEN '" + fromDate + "' AND '" + toDate + "'   "
		    + "AND a.strTransType='VB' "
		    + "and a.strVoidBillType='Bill Void' ");

	    if (!POSCode.equalsIgnoreCase("All"))
	    {
		sqlVoidBillBuilder.append("AND a.strPosCode='" + POSCode + "' ");
	    }
	    sqlVoidBillBuilder.append(" group by a.strBillNo "
		    + " order by a.strBillNo ");

	    ResultSet rsVoidBill = clsGlobalVarClass.dbMysql.executeResultSet(sqlVoidBillBuilder.toString());
	    double totalAmt = 0.00, totalDiscAmt = 0, totalTaxAmt = 0, totalTotal = 0;
	    boolean noCancelledBilled = true;
	    while (rsVoidBill.next())
	    {
		Date dteModifyDate = rsVoidBill.getDate(1);
		String modifyDate = ddMMyyDateFormat.format(dteModifyDate);

		pw.println();
		pw.print(objUtility.funPrintTextWithAlignment(modifyDate, 10, "left"));//date
		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(2), 10, "left"));//cashier
		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(3), 10, "left"));//tbl
		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(4), 10, "left"));//bill
		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(5), 10, "Right"));//amt
		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(6), 10, "Right"));//disc
		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(7), 10, "Right"));//tax
		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(8), 10, "Right"));//total
		pw.print(objUtility.funPrintTextWithAlignment(" " + rsVoidBill.getString(9), 20, "left"));//reason
		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(10), 10, "left"));//auth by

		totalAmt += rsVoidBill.getDouble(5);
		totalDiscAmt += rsVoidBill.getDouble(6);
		totalTaxAmt += rsVoidBill.getDouble(7);
		totalTotal += rsVoidBill.getDouble(8);

		noCancelledBilled = false;
	    }
	    pw.println();
	    pw.print(DASHLINE);

	    pw.println();
	    pw.print(objUtility.funPrintTextWithAlignment("Total", 10, "left"));//date
	    pw.print(objUtility.funPrintTextWithAlignment("", 10, "left"));//cashier
	    pw.print(objUtility.funPrintTextWithAlignment("", 10, "left"));//tbl
	    pw.print(objUtility.funPrintTextWithAlignment("", 10, "left"));//bill
	    pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(totalAmt), 10, "Right"));//amt
	    pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(totalDiscAmt), 10, "Right"));//disc
	    pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(totalTaxAmt), 10, "Right"));//tax
	    pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(totalTotal), 10, "Right"));//total
	    pw.print(objUtility.funPrintTextWithAlignment(" ", 20, "left"));//reason
	    pw.print(objUtility.funPrintTextWithAlignment("", 10, "left"));//auth by

	    pw.println();
	    pw.print(DASHLINE);
	    if (noCancelledBilled)
	    {
		pw.println();
		pw.print("No cancelled Bills");
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funModifyBills(String fromDate, String toDate, String POSCode, PrintWriter pw)
    {
	try
	{
	    clsUtility objUtility = new clsUtility();
	    SimpleDateFormat yyyyMMddDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    SimpleDateFormat dateFormat = new SimpleDateFormat("E dd-MMM-yyyy");
	    SimpleDateFormat dateFormat24Hrs = new SimpleDateFormat("E dd-MMM-yyyy HH:mm:ss");
	    SimpleDateFormat ddMMyyDateFormat = new SimpleDateFormat("dd/MM/yy");

	    pw.println();
	    pw.println("MODIFIED BILLS");
	    pw.println("***************");
	    pw.print(objUtility.funPrintTextWithAlignment("Tbl No", 10, "left"));//A/C
	    pw.print(objUtility.funPrintTextWithAlignment("Bill Nos", 10, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment("Original Amt.", 13, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("  Modified Amt.", 15, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("Diff.", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("  Settle", 10, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment("Modify Time", 11, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment("  Operator", 10, "left"));
	    pw.println();

	    int noOfColumns = 100;
	    String DASHLINE = "";
	    for (int i = 0; i < noOfColumns; i++)
	    {
		DASHLINE = DASHLINE + "-";
	    }
	    pw.print(DASHLINE);

	    StringBuilder sqlVoidBillBuilder = new StringBuilder();
	    sqlVoidBillBuilder.append(" SELECT IFNULL(c.strTableName,''),a.strBillNo,a.dblActualAmount,a.dblModifiedAmount,(a.dblActualAmount-a.dblModifiedAmount)Diff "
		    + ",''Settle,time(a.dteModifyVoidBill),a.strUserEdited "
		    + "FROM tblvoidbillhd a "
		    + "LEFT OUTER JOIN tbltablemaster c ON a.strTableNo=c.strTableNo "
		    + "WHERE DATE(a.dteModifyVoidBill) BETWEEN '" + fromDate + "' AND '" + toDate + "'   "
		    + "AND ( (a.strTransType='VB' AND a.strVoidBillType='Item Void' )OR a.strTransType='MB')  ");
	    if (!POSCode.equalsIgnoreCase("All"))
	    {
		sqlVoidBillBuilder.append("AND a.strPosCode='" + POSCode + "' ");
	    }
	    sqlVoidBillBuilder.append(" group by a.strBillNo "
		    + " order by a.strBillNo ");

	    ResultSet rsVoidBill = clsGlobalVarClass.dbMysql.executeResultSet(sqlVoidBillBuilder.toString());
	    double totalAmt = 0.00, totalDiscAmt = 0, totalTaxAmt = 0, totalTotal = 0;
	    boolean noCancelledBilled = true;
	    while (rsVoidBill.next())
	    {
		pw.println();
		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(1), 10, "left"));//A/C
		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(2), 10, "left"));
		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(3), 13, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(4), 15, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(5), 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(" " + rsVoidBill.getString(6), 10, "left"));
		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(7), 10, "left"));
		pw.print(objUtility.funPrintTextWithAlignment("  " + rsVoidBill.getString(8), 10, "left"));

		noCancelledBilled = false;
	    }
	    if (noCancelledBilled)
	    {
		pw.println();
		pw.print("No Bills were modified.");
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

//Create Temp folder
    private void funCreateTempFolder()
    {
	String filePath = System.getProperty("user.dir");
	File textReport = new File(filePath + "/Temp");
	if (!textReport.exists())
	{
	    textReport.mkdirs();
	}
    }

    private String funGetAreaCodes(String taxCode)
    {
	String areaCodes = "(";

	try
	{
	    String sql = "select a.strAreaCode from tbltaxhd a where a.strTaxCode='" + taxCode + "' ";
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rs.next())
	    {
		String[] arrAreaCode = rs.getString(1).split(",");
		for (int i = 0; i < arrAreaCode.length; i++)
		{
		    if (i == 0)
		    {
			areaCodes += "'" + arrAreaCode[i] + "'";
		    }
		    else
		    {
			areaCodes += ",'" + arrAreaCode[i] + "'";
		    }
		}
	    }
	    areaCodes += ")";
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    return areaCodes;
	}
    }

    public int funGenerateManagersReportFormat1(Date dateFromDate, Date dateToDate, String POSCode) throws Exception
    {
	funCreateTempFolder();
	String filePath = System.getProperty("user.dir");
	File file = new File(filePath + "/Temp/ManagersReport.txt");

	Date currentDate = new Date();

	SimpleDateFormat yyyyMMddDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat dateFormat = new SimpleDateFormat("E dd-MMM-yyyy");
	SimpleDateFormat dateFormat24Hrs = new SimpleDateFormat("E dd-MMM-yyyy HH:mm:ss");

	String fromDayAndDate = dateFormat.format(dateFromDate);
	String toDayAndDate = dateFormat.format(dateToDate);
	String todaysDate = dateFormat24Hrs.format(currentDate);
	String fromDate = yyyyMMddDateFormat.format(dateFromDate);
	String toDate = yyyyMMddDateFormat.format(dateToDate);

	PrintWriter pw = new PrintWriter(file);

	boolean isDayEndHappend = objUtility2.isDayEndHappened(toDate);
	if (!isDayEndHappend)
	{
	    pw.println();
	    pw.print(objUtility1.funPrintTextWithAlignment("DAY END NOT DONE.", 100, "center"));
	    pw.println();
	}

	pw.println(clsGlobalVarClass.gClientName);
	pw.println("Managers Report");
	pw.println("Reporting For :" + "  " + fromDayAndDate + " - " + " " + toDayAndDate);
	pw.println("--------------------------------------------------------------------------------------------------------");

	//VOIDED BILLS
	funVoidBills(fromDate, toDate, POSCode, pw);
	pw.println("");
	//MODIFIED BILLS
	funModifyBills(fromDate, toDate, POSCode, pw);
	pw.println("");
	//Reprinted BILLS
	funReprintedBills(fromDate, toDate, POSCode, pw);
	pw.println("");
	//Reprinted BILLS
	funResettleBills(fromDate, toDate, POSCode, pw);
	pw.println("");
	//DISCOUNT BILLS
	funDiscountedBills(fromDate, toDate, POSCode, pw);
	pw.println("");
	//Parcel/take away
	funHomeDeliveryAndTakeAwayBills(fromDate, toDate, POSCode, pw);
	pw.println("");
	pw.println("");
	//Modified  KOTS(parcial void)
	funModifiedKOTs(fromDate, toDate, POSCode, pw);
	pw.println("");
	pw.println("");
	//cancelled  KOTS(full void)
	funCancelledKOTs(fromDate, toDate, POSCode, pw);
	pw.println("");
	pw.println("");
	//credit payment received
	funPaymentReceived(fromDate, toDate, POSCode, pw);
	pw.println("");
	pw.println("");
	//credit bills
	funCreditBills(fromDate, toDate, POSCode, pw);
	pw.println("");
	pw.println("");
	pw.println("END OF Managers Report");

	//funOperationTypeWiseData(fromDate, toDate, POSCode, pw);
//	//SETTLEMENT wise breakup
//	funSettlementWiseData(fromDate, toDate, POSCode, pw);
//
//
//	//SALE BY OPERATION TYPE TAXATION BIFURCATION
//	funOpTypeWiseGroupWiseTaxationData(fromDate, toDate, POSCode, pw);
//
//	//SALE BY TAX CATEGORY
//	funGroupWiseTaxationData(fromDate, toDate, POSCode, pw);
//
//	//SALE BY SHIFT
//	funShiftWiseTaxationData(fromDate, toDate, POSCode, pw);
//
//
//	//COMPLIMENTARY BILLS
//	funComplimentryBills(fromDate, toDate, POSCode, pw);
//
//	//MENU HEAD ANALYSIS
//	funMenuHeadWiseSales(fromDate, toDate, POSCode, pw);
//
//	//AVG.PER BILL ANALYSIS 
//	funAvgPerBillAnalysis(fromDate, toDate, POSCode, pw);
//
//	//VOIDED KOTS
//	funVoidKOTs(fromDate, toDate, POSCode, pw);
//	
//
	pw.flush();
	pw.close();

	Desktop dt = Desktop.getDesktop();
	dt.open(file);

	return 1;
    }

    private void funReprintedBills(String fromDate, String toDate, String POSCode, PrintWriter pw)
    {
	try
	{
	    clsUtility objUtility = new clsUtility();
	    SimpleDateFormat yyyyMMddDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    SimpleDateFormat dateFormat = new SimpleDateFormat("E dd-MMM-yyyy");
	    SimpleDateFormat dateFormat24Hrs = new SimpleDateFormat("E dd-MMM-yyyy HH:mm:ss");
	    SimpleDateFormat ddMMyyDateFormat = new SimpleDateFormat("dd/MM/yy");

	    pw.println();
	    pw.println("REPRINTED BILLS");
	    pw.println("***************");
	    pw.print(objUtility.funPrintTextWithAlignment("Date", 10, "left"));//A/C
	    pw.print(objUtility.funPrintTextWithAlignment("Tbl", 10, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment("Bill No", 10, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment("Amount", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("Disc", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("Total", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("  Operator", 10, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment("  Reason", 50, "left"));
	    pw.println();

	    int noOfColumns = 120;
	    String DASHLINE = "";
	    for (int i = 0; i < noOfColumns; i++)
	    {
		DASHLINE = DASHLINE + "-";
	    }
	    pw.print(DASHLINE);

	    ////For Settlement details of Live and Q data
	    StringBuilder sqlQData = new StringBuilder();

	    List<clsReprintDocs> listOfReprintTextData = new ArrayList<>();
	    sqlQData.setLength(0);
	    sqlQData.append("select DATE_FORMAT(a.dteBillDate,'%d/%m/%y')dteBillDate,ifnull(d.strTableName,''),a.strBillNo "
		    + ",a.dblSubTotal,a.dblDiscountAmt,a.dblGrandTotal,b.strUserCreated"
		    + ",b.strRemarks "
		    + "from tblbillhd a "
		    + "inner join tblaudit b  on a.strBillNo=b.strDocNo   "
		    + "left outer join tblreasonmaster c  on b.strReasonCode=c.strReasonCode "
		    + "left outer join tbltablemaster d on a.strTableNo=d.strTableNo "
		    + "where date(b.dtePOSDate) between '" + fromDate + "' and '" + toDate + "' ");
	    ResultSet rsSettlementWiseQData = clsGlobalVarClass.dbMysql.executeResultSet(sqlQData.toString());
	    while (rsSettlementWiseQData.next())
	    {
		clsReprintDocs objReprint = new clsReprintDocs();

		objReprint.setDate(rsSettlementWiseQData.getString(1));
		objReprint.setStrTableName(rsSettlementWiseQData.getString(2));
		objReprint.setBillNo(rsSettlementWiseQData.getString(3));
		objReprint.setDblAmount(rsSettlementWiseQData.getDouble(4));
		objReprint.setDblDsicAmt(rsSettlementWiseQData.getDouble(5));
		objReprint.setTotal(rsSettlementWiseQData.getDouble(6));
		objReprint.setStrOperator(rsSettlementWiseQData.getString(7));
		objReprint.setReason(rsSettlementWiseQData.getString(8));

		listOfReprintTextData.add(objReprint);
	    }
	    rsSettlementWiseQData.close();

	    sqlQData.setLength(0);
	    sqlQData.append("select DATE_FORMAT(a.dteBillDate,'%d/%m/%y')dteBillDate,ifnull(d.strTableName,''),a.strBillNo "
		    + ",a.dblSubTotal,a.dblDiscountAmt,a.dblGrandTotal,b.strUserCreated"
		    + ",b.strRemarks "
		    + "from tblqbillhd a "
		    + "inner join tblaudit b  on a.strBillNo=b.strDocNo   "
		    + "left outer join tblreasonmaster c  on b.strReasonCode=c.strReasonCode "
		    + "left outer join tbltablemaster d on a.strTableNo=d.strTableNo "
		    + "where date(b.dtePOSDate) between '" + fromDate + "' and '" + toDate + "' ");
	    rsSettlementWiseQData = clsGlobalVarClass.dbMysql.executeResultSet(sqlQData.toString());
	    while (rsSettlementWiseQData.next())
	    {
		clsReprintDocs objReprint = new clsReprintDocs();

		objReprint.setDate(rsSettlementWiseQData.getString(1));
		objReprint.setStrTableName(rsSettlementWiseQData.getString(2));
		objReprint.setBillNo(rsSettlementWiseQData.getString(3));
		objReprint.setDblAmount(rsSettlementWiseQData.getDouble(4));
		objReprint.setDblDsicAmt(rsSettlementWiseQData.getDouble(5));
		objReprint.setTotal(rsSettlementWiseQData.getDouble(6));
		objReprint.setStrOperator(rsSettlementWiseQData.getString(7));
		objReprint.setReason(rsSettlementWiseQData.getString(8));

		listOfReprintTextData.add(objReprint);
	    }
	    rsSettlementWiseQData.close();
	    boolean noCancelledBilled = true;
	    for (clsReprintDocs objReprintDocs : listOfReprintTextData)
	    {
		pw.println();
		pw.print(objUtility.funPrintTextWithAlignment(objReprintDocs.getDate(), 10, "left"));//A/C
		pw.print(objUtility.funPrintTextWithAlignment(objReprintDocs.getStrTableName(), 10, "left"));
		pw.print(objUtility.funPrintTextWithAlignment(objReprintDocs.getBillNo(), 10, "left"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(objReprintDocs.getDblAmount()), 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(objReprintDocs.getDblDsicAmt()), 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(objReprintDocs.getTotal()), 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment("  " + objReprintDocs.getStrOperator(), 10, "left"));
		pw.print(objUtility.funPrintTextWithAlignment("  " + objReprintDocs.getReason(), 50, "left"));

		noCancelledBilled = false;
	    }
	    if (noCancelledBilled)
	    {
		pw.println();
		pw.print("No Bills were reprinted.");
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funResettleBills(String fromDate, String toDate, String POSCode, PrintWriter pw)
    {
	try
	{
	    clsUtility objUtility = new clsUtility();
	    SimpleDateFormat yyyyMMddDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    SimpleDateFormat dateFormat = new SimpleDateFormat("E dd-MMM-yyyy");
	    SimpleDateFormat dateFormat24Hrs = new SimpleDateFormat("E dd-MMM-yyyy HH:mm:ss");
	    SimpleDateFormat ddMMyyDateFormat = new SimpleDateFormat("dd/MM/yy");

	    pw.println();
	    pw.println("RESETTLED BILLS");
	    pw.println("***************");
	    pw.print(objUtility.funPrintTextWithAlignment("Date", 10, "left"));//A/C
	    pw.print(objUtility.funPrintTextWithAlignment("Cashier", 10, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment("Bill No", 10, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment("Tbl No", 10, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment("Amount", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("Disc", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("VAT/GST", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("Tip", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("Adv.", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("  Last Resettlement", 20, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment("  Last Settlement", 20, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment("  Current Settlement", 20, "left"));
	    pw.println();

	    int noOfColumns = 150;
	    String DASHLINE = "";
	    for (int i = 0; i < noOfColumns; i++)
	    {
		DASHLINE = DASHLINE + "-";
	    }
	    pw.print(DASHLINE);

	    boolean noCancelledBilled = true;

	    pw.println();
	    pw.print(DASHLINE);

	    pw.println();
	    pw.print(objUtility.funPrintTextWithAlignment("Total", 10, "left"));//A/C
	    pw.print(objUtility.funPrintTextWithAlignment("", 10, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment("", 10, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment("", 10, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment("", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("  ", 20, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment("  ", 20, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment("  ", 20, "left"));

	    pw.println();
	    pw.print(DASHLINE);
	    if (noCancelledBilled)
	    {
		pw.println();
		pw.print("No Resettled Bills.");
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private int funHomeDeliveryAndTakeAwayBills(String fromDate, String toDate, String POSCode, PrintWriter pw)
    {

	try
	{
	    clsUtility objUtility = new clsUtility();
	    SimpleDateFormat yyyyMMddDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    SimpleDateFormat dateFormat = new SimpleDateFormat("E dd-MMM-yyyy");
	    SimpleDateFormat dateFormat24Hrs = new SimpleDateFormat("E dd-MMM-yyyy HH:mm:ss");
	    SimpleDateFormat ddMMyyDateFormat = new SimpleDateFormat("dd/MM/yy");

	    pw.println();
	    pw.println("PARCEL BILLS");
	    pw.println("************");
	    pw.print(objUtility.funPrintTextWithAlignment("Bill No", 10, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment("Amount", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("Disc.", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("VAT/GST", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("Tip", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("Adv.", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("Total", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("  Settle", 15, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment(" Remark", 10, "left"));
	    pw.println();

	    int noOfColumns = 100;
	    String DASHLINE = "";
	    for (int i = 0; i < noOfColumns; i++)
	    {
		DASHLINE = DASHLINE + "-";
	    }
	    pw.print(DASHLINE);
	    pw.println();
	    pw.print("Delivery");

	    StringBuilder sqlVoidBillBuilder = new StringBuilder();

	    double totalSubTotal = 0, totalAmt = 0.00, totalDiscAmt = 0, totalTaxAmt = 0, totalGTTotal = 0, advTotal = 0, totalTipAmt = 0;
	    boolean noParcelBills = true;
	    int noOfBills = 0;

	    sqlVoidBillBuilder.setLength(0);
	    sqlVoidBillBuilder.append(" select a.strBillNo,a.dblSubTotal,a.dblDiscountAmt,a.dblTaxAmt,a.dblTipAmount,0.00 Adv,a.dblGrandTotal,a.strSettelmentMode,IFNULL(b.strCustomerName,'') "
		    + "from  tblbillhd a "
		    + "left outer join tblcustomermaster b on a.strCustomerCode=b.strCustomerCode "
		    + "where date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + "and (a.strOperationType='HomeDelivery' or a.strOperationType='TakeAway') ");
	    if (!POSCode.equalsIgnoreCase("All"))
	    {
		sqlVoidBillBuilder.append("AND a.strPosCode='" + POSCode + "' ");
	    }
	    sqlVoidBillBuilder.append(" group by a.strBillNo "
		    + " order by a.strBillNo ");
	    ResultSet rsVoidBill = clsGlobalVarClass.dbMysql.executeResultSet(sqlVoidBillBuilder.toString());
	    while (rsVoidBill.next())
	    {
		pw.println();
		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(1), 10, "left"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(rsVoidBill.getDouble(2)), 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(rsVoidBill.getDouble(3)), 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(rsVoidBill.getDouble(4)), 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(rsVoidBill.getDouble(5)), 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(rsVoidBill.getDouble(6)), 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(rsVoidBill.getDouble(7)), 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment("  " + rsVoidBill.getString(8), 15, "left"));
		if (rsVoidBill.getString(8).equalsIgnoreCase("CREDIT"))
		{
		    pw.print(objUtility.funPrintTextWithAlignment(" " + rsVoidBill.getString(9), 10, "left"));
		}

		totalSubTotal += rsVoidBill.getDouble(2);
		totalDiscAmt += rsVoidBill.getDouble(3);
		totalTaxAmt += rsVoidBill.getDouble(4);
		totalTipAmt += rsVoidBill.getDouble(5);
		advTotal += rsVoidBill.getDouble(6);
		totalGTTotal += rsVoidBill.getDouble(7);

		noOfBills++;

		noParcelBills = false;
	    }
	    rsVoidBill.close();

	    sqlVoidBillBuilder.setLength(0);
	    sqlVoidBillBuilder.append(" select a.strBillNo,a.dblSubTotal,a.dblDiscountAmt,a.dblTaxAmt,a.dblTipAmount,0.00 Adv,a.dblGrandTotal,a.strSettelmentMode ,IFNULL(b.strCustomerName,'')"
		    + " from  tblqbillhd a "
		    + "left outer join tblcustomermaster b on a.strCustomerCode=b.strCustomerCode "
		    + "where date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + "and (a.strOperationType='HomeDelivery' or a.strOperationType='TakeAway') ");
	    if (!POSCode.equalsIgnoreCase("All"))
	    {
		sqlVoidBillBuilder.append("AND a.strPosCode='" + POSCode + "' ");
	    }
	    sqlVoidBillBuilder.append(" group by a.strBillNo "
		    + " order by a.strBillNo ");
	    rsVoidBill = clsGlobalVarClass.dbMysql.executeResultSet(sqlVoidBillBuilder.toString());
	    while (rsVoidBill.next())
	    {
		pw.println();
		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(1), 10, "left"));
		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(2), 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(3), 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(rsVoidBill.getDouble(4)), 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(rsVoidBill.getDouble(5)), 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(rsVoidBill.getDouble(6)), 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(rsVoidBill.getDouble(7)), 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment("  " + rsVoidBill.getString(8), 15, "left"));
		if (rsVoidBill.getString(8).contains("CREDIT"))
		{
		    pw.print(objUtility.funPrintTextWithAlignment(" " + rsVoidBill.getString(9), 10, "left"));
		}
		totalSubTotal += rsVoidBill.getDouble(2);
		totalDiscAmt += rsVoidBill.getDouble(3);
		totalTaxAmt += rsVoidBill.getDouble(4);
		totalTipAmt += rsVoidBill.getDouble(5);
		advTotal += rsVoidBill.getDouble(6);
		totalGTTotal += rsVoidBill.getDouble(7);

		noOfBills++;
		noParcelBills = false;
	    }
	    rsVoidBill.close();
	    rsVoidBill.close();

	    pw.println();
	    pw.print(DASHLINE);

	    if (noOfBills > 0)
	    {
		double avgPerBill = totalSubTotal / noOfBills;

		pw.println();
		pw.print(objUtility.funPrintTextWithAlignment("Total Amount : ", 10, "left"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(totalSubTotal), 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment("  No Of Bills : ", 10, "left"));
		pw.print(objUtility.funPrintTextWithAlignment(String.valueOf(noOfBills), 4, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment("  AVG PER BILL : ", 10, "left"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(avgPerBill), 10, "Right"));
	    }

//	    pw.println();
//	    pw.print(DASHLINE);
	    if (noParcelBills)
	    {
		pw.println();
		pw.print("No Parceled Bills");
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}

	return 1;
    }

    private void funCancelledKOTs(String fromDate, String toDate, String POSCode, PrintWriter pw)
    {
	try
	{
	    clsUtility objUtility = new clsUtility();
	    SimpleDateFormat yyyyMMddDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    SimpleDateFormat dateFormat = new SimpleDateFormat("E dd-MMM-yyyy");
	    SimpleDateFormat dateFormat24Hrs = new SimpleDateFormat("E dd-MMM-yyyy HH:mm:ss");
	    SimpleDateFormat ddMMyyDateFormat = new SimpleDateFormat("dd/MM/yy");

	    pw.println();
	    pw.println("CANCELLED KOTs");
	    pw.println("************");
	    boolean noModifiedKOTs = true;

//	    pw.print(objUtility.funPrintTextWithAlignment("A/c No", 10, "left"));
//	    pw.print(objUtility.funPrintTextWithAlignment("Item", 40, "left"));
//	    pw.print(objUtility.funPrintTextWithAlignment("Modify Time", 10, "left"));
//	    pw.print(objUtility.funPrintTextWithAlignment("  Modify By", 10, "left"));
//	    pw.println();
//
//	    int noOfColumns = 120;
//	    String DASHLINE = "";
//	    for (int i = 0; i < noOfColumns; i++)
//	    {
//		DASHLINE = DASHLINE + "-";
//	    }
//	    pw.print(DASHLINE);
//
//	    StringBuilder sqlVoidBillBuilder = new StringBuilder();
//
//	    double totalSubTotal = 0, totalAmt = 0.00, totalDiscAmt = 0, totalTaxAmt = 0, totalGTTotal = 0, advTotal = 0, totalTipAmt = 0;
//	    boolean noModifiedKOTs = true;
//	    int noOfBills = 0;
//
//	    sqlVoidBillBuilder.setLength(0);
//	    sqlVoidBillBuilder.append(" select b.strTableName,a.strItemName,time(a.dteVoidedDate),a.strUserCreated "
//		    + "from tblvoidkot a,tbltablemaster b "
//		    + "where date(a.dteVoidedDate) between '" + fromDate + "' and '" + toDate + "' "
//		    + "and a.strTableNo=b.strTableNo "
//		    + "and a.strVoidBillType='Full KOT Void' ");
//	    if (!POSCode.equalsIgnoreCase("All"))
//	    {
//		sqlVoidBillBuilder.append("AND a.strPosCode='" + POSCode + "' ");
//	    }
//	    sqlVoidBillBuilder.append("  "
//		    + " order by b.strTableName ");
//	    ResultSet rsVoidBill = clsGlobalVarClass.dbMysql.executeResultSet(sqlVoidBillBuilder.toString());
//	    while (rsVoidBill.next())
//	    {
//		pw.println();
//		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(1), 10, "left"));
//		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(2), 40, "left"));
//		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(3), 10, "left"));
//		pw.print(objUtility.funPrintTextWithAlignment("  " + rsVoidBill.getString(4), 10, "left"));
//
//		noModifiedKOTs = false;
//	    }
//	    rsVoidBill.close();
//
//	    pw.println();
//	    pw.print(DASHLINE);
	    /**
	     * format 2
	     */
	    StringBuilder sqlVoidKOTBuilder = new StringBuilder();
	    sqlVoidKOTBuilder.append("SELECT a.strKOTNo,a.strItemCode,a.strItemName,e.strTableName,sum(a.dblItemQuantity),sum(a.dblAmount),a.strRemark "
		    + " ,b.strPosCode,b.strPosName,a.strUserCreated,a.dteVoidedDate,d.strReasonName "
		    + " FROM tblvoidkot a "
		    + " LEFT OUTER JOIN tblposmaster b ON a.strPOSCode=b.strPosCode "
		    + " LEFT OUTER JOIN tblreasonmaster d ON a.strReasonCode=d.strReasonCode "
		    + " LEFT OUTER JOIN tbltablemaster e ON a.strTableNo=e.strTableNo "
		    + " WHERE DATE(a.dteVoidedDate) BETWEEN '" + fromDate + "' AND '" + toDate + "'  "
		    + " and a.strVoidBillType='Full KOT Void' ");
	    if (!POSCode.equalsIgnoreCase("All"))
	    {
		sqlVoidKOTBuilder.append("AND b.strPosCode='" + POSCode + "' ");
	    }
	    sqlVoidKOTBuilder.append("GROUP BY a.strkotno,a.strItemCode "
		    + "ORDER BY a.strkotno,a.strItemCode ");

	    ResultSet rsVoidKOT = clsGlobalVarClass.dbMysql.executeResultSet(sqlVoidKOTBuilder.toString());
	    String kotNo = "";
	    while (rsVoidKOT.next())
	    {
		noModifiedKOTs = false;

		if (rsVoidKOT.isFirst())
		{
		    kotNo = rsVoidKOT.getString(1);
		    pw.println();
		    pw.println("TABLE NO :" + rsVoidKOT.getString(4));
		    pw.println("KOT NO   :" + kotNo);
		    // pw.println("REASON   :" + rsVoidKOT.getString(12));
		    pw.println("REMARKS  :" + rsVoidKOT.getString(7));
		    pw.println("---------------------------------------------");
		    pw.println("ITEMS   ");
		    pw.println("--------");
		    pw.println("        QTY |PARTICULARS");
		    pw.println("        " + rsVoidKOT.getString(5) + "|" + rsVoidKOT.getString(3));
		}
		else
		{
		    if (kotNo.equalsIgnoreCase(rsVoidKOT.getString(1)))
		    {
			pw.println("        " + rsVoidKOT.getString(5) + "|" + rsVoidKOT.getString(3));
		    }
		    else
		    {
			kotNo = rsVoidKOT.getString(1);
			pw.println();
			pw.println("TABLE NO :" + rsVoidKOT.getString(4));
			pw.println("KOT NO   :" + kotNo);
			//pw.println("REASON   :" + rsVoidKOT.getString(12));
			pw.println("REMARKS  :" + rsVoidKOT.getString(7));
			pw.println("---------------------------------------------");
			pw.println("ITEMS   ");
			pw.println("--------");
			pw.println("        QTY |PARTICULARS");
			pw.println("        " + rsVoidKOT.getString(5) + "|" + rsVoidKOT.getString(3));
		    }
		}
	    }
	    rsVoidKOT.close();
	    pw.println();

//	    pw.println();
//	    pw.print(DASHLINE);
	    if (noModifiedKOTs)
	    {
		pw.println();
		pw.print("No Cancelled KOTs");
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}

    }

    private void funPaymentReceived(String fromDate, String toDate, String POSCode, PrintWriter pw)
    {
	try
	{
	    clsUtility objUtility = new clsUtility();
	    SimpleDateFormat yyyyMMddDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    SimpleDateFormat dateFormat = new SimpleDateFormat("E dd-MMM-yyyy");
	    SimpleDateFormat dateFormat24Hrs = new SimpleDateFormat("E dd-MMM-yyyy HH:mm:ss");
	    SimpleDateFormat ddMMyyDateFormat = new SimpleDateFormat("dd/MM/yy");

	    pw.println();
	    pw.println("PAYMENT RECEIVED");
	    pw.println("****************");
	    pw.print(objUtility.funPrintTextWithAlignment("Date", 10, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment("Ledger", 30, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment("Receipt No", 10, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment("Amount", 10, "right"));
	    pw.print(objUtility.funPrintTextWithAlignment("  Mode Of Payment", 15, "left"));
	    pw.println();

	    int noOfColumns = 80;
	    String DASHLINE = "";
	    for (int i = 0; i < noOfColumns; i++)
	    {
		DASHLINE = DASHLINE + "-";
	    }
	    pw.print(DASHLINE);

	    StringBuilder sqlVoidBillBuilder = new StringBuilder();

	    double totalSubTotal = 0, totalAmt = 0.00, totalDiscAmt = 0, totalTaxAmt = 0, totalGTTotal = 0, advTotal = 0, totalTipAmt = 0;
	    boolean noPaymentReceved = true;
	    int noOfBills = 0;

	    sqlVoidBillBuilder.setLength(0);
	    sqlVoidBillBuilder.append(" select DATE_FORMAT(a.dteReceiptDate,'%d/%m/%y'),c.strCustomerName,a.strReceiptNo,a.dblReceiptAmt,a.strSettlementName "
		    + "from tblqcreditbillreceipthd a,tblbillhd b,tblcustomermaster c "
		    + "where a.strBillNo=b.strBillNo "
		    + "and b.strCustomerCode=c.strCustomerCode "
		    + "and DATE(a.dteReceiptDate) BETWEEN '" + fromDate + "' AND '" + toDate + "' ");
	    if (!POSCode.equalsIgnoreCase("All"))
	    {
		sqlVoidBillBuilder.append("AND a.strPosCode='" + POSCode + "' ");
	    }
	    ResultSet rsVoidBill = clsGlobalVarClass.dbMysql.executeResultSet(sqlVoidBillBuilder.toString());
	    while (rsVoidBill.next())
	    {
		pw.println();
		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(1), 10, "left"));
		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(2), 30, "left"));
		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(3), 10, "left"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(rsVoidBill.getDouble(4)), 10, "right"));
		pw.print(objUtility.funPrintTextWithAlignment("  " + rsVoidBill.getString(5), 15, "left"));

		noPaymentReceved = false;

		totalAmt += rsVoidBill.getDouble(4);
	    }
	    rsVoidBill.close();

	    sqlVoidBillBuilder.setLength(0);
	    sqlVoidBillBuilder.append(" select DATE_FORMAT(a.dteReceiptDate,'%d/%m/%y'),c.strCustomerName,a.strReceiptNo,a.dblReceiptAmt,a.strSettlementName "
		    + "from tblqcreditbillreceipthd a,tblqbillhd b,tblcustomermaster c "
		    + "where a.strBillNo=b.strBillNo "
		    + "and b.strCustomerCode=c.strCustomerCode "
		    + "and DATE(a.dteReceiptDate) BETWEEN '" + fromDate + "' AND '" + toDate + "' ");
	    if (!POSCode.equalsIgnoreCase("All"))
	    {
		sqlVoidBillBuilder.append("AND a.strPosCode='" + POSCode + "' ");
	    }
	    rsVoidBill = clsGlobalVarClass.dbMysql.executeResultSet(sqlVoidBillBuilder.toString());
	    while (rsVoidBill.next())
	    {
		pw.println();
		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(1), 10, "left"));
		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(2), 30, "left"));
		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(3), 10, "left"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(rsVoidBill.getDouble(4)), 10, "right"));
		pw.print(objUtility.funPrintTextWithAlignment("  " + rsVoidBill.getString(5), 15, "left"));

		noPaymentReceved = false;

		totalAmt += rsVoidBill.getDouble(4);
	    }
	    rsVoidBill.close();

	    pw.println();
	    pw.print(DASHLINE);

	    pw.println();
	    pw.print(objUtility.funPrintTextWithAlignment("Total", 10, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment("", 30, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment("", 10, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(totalAmt), 10, "right"));
	    pw.print(objUtility.funPrintTextWithAlignment("  ", 10, "left"));

//	    pw.println();
//	    pw.print(DASHLINE);
	    if (noPaymentReceved)
	    {
		pw.println();
		pw.print("No Payment Received");
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}

    }

    private int funCreditBills(String fromDate, String toDate, String POSCode, PrintWriter pw)
    {

	try
	{
	    clsUtility objUtility = new clsUtility();
	    SimpleDateFormat yyyyMMddDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    SimpleDateFormat dateFormat = new SimpleDateFormat("E dd-MMM-yyyy");
	    SimpleDateFormat dateFormat24Hrs = new SimpleDateFormat("E dd-MMM-yyyy HH:mm:ss");
	    SimpleDateFormat ddMMyyDateFormat = new SimpleDateFormat("dd/MM/yy");

	    pw.println();
	    pw.println("CREDIT BILLS");
	    pw.println("**************");
	    pw.print(objUtility.funPrintTextWithAlignment("Date", 10, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment("Sr. No", 10, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment("Tbl No", 10, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment("Bill No", 10, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment("Amount", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("Disc.", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("VAT/GST", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("Tip", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("Adv.", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("Total", 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment("  Settle", 10, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment("  Customer", 30, "left"));
	    pw.println();

	    int noOfColumns = 140;
	    String DASHLINE = "";
	    for (int i = 0; i < noOfColumns; i++)
	    {
		DASHLINE = DASHLINE + "-";
	    }
	    pw.print(DASHLINE);

	    StringBuilder sqlVoidBillBuilder = new StringBuilder();

	    double totalSubTotal = 0, totalAmt = 0.00, totalDiscAmt = 0, totalTaxAmt = 0, totalGTTotal = 0, advTotal = 0, totalTipAmt = 0;
	    boolean noCreditBills = true;

	    sqlVoidBillBuilder.setLength(0);
	    sqlVoidBillBuilder.append(" select DATE_FORMAT(a.dteBillDate,'%d/%m/%y'),a.strBillNo SrNo,ifnull(d.strTableName,''),a.strBillNo "
		    + ",a.dblSubTotal,a.dblDiscountAmt,a.dblTaxAmt,a.dblTipAmount,0.00 Adv,a.dblGrandTotal,c.strSettelmentDesc"
		    + ",ifnull(e.strCustomerName,'')strCustomerName "
		    + "from  tblbillhd a "
		    + "inner join tblbillsettlementdtl b on a.strBillNo=b.strBillNo and date(a.dteBillDate)=date(b.dteBillDate) "
		    + "inner join tblsettelmenthd c on b.strSettlementCode=c.strSettelmentCode "
		    + "left outer join tbltablemaster d on a.strTableNo=d.strTableNo "
		    + "left outer join tblcustomermaster e on a.strCustomerCode=e.strCustomerCode "
		    + "where date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + "and c.strSettelmentType='Credit' ");
	    if (!POSCode.equalsIgnoreCase("All"))
	    {
		sqlVoidBillBuilder.append("AND a.strPosCode='" + POSCode + "' ");
	    }
	    sqlVoidBillBuilder.append(" group by a.strBillNo "
		    + " order by a.strBillNo ");

	    ResultSet rsVoidBill = clsGlobalVarClass.dbMysql.executeResultSet(sqlVoidBillBuilder.toString());
	    while (rsVoidBill.next())
	    {
		pw.println();
		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(1), 10, "left"));
		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(2), 10, "left"));
		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(3), 10, "left"));
		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(4), 10, "left"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(rsVoidBill.getDouble(5)), 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(rsVoidBill.getDouble(6)), 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(rsVoidBill.getDouble(7)), 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(rsVoidBill.getDouble(8)), 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(rsVoidBill.getDouble(9)), 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(rsVoidBill.getDouble(10)), 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment("  " + rsVoidBill.getString(11), 10, "left"));
		pw.print(objUtility.funPrintTextWithAlignment("  " + rsVoidBill.getString(12), 30, "left"));

		totalSubTotal += rsVoidBill.getDouble(5);
		totalDiscAmt += rsVoidBill.getDouble(6);
		totalTaxAmt += rsVoidBill.getDouble(7);
		advTotal += rsVoidBill.getDouble(9);
		totalGTTotal += rsVoidBill.getDouble(10);

		noCreditBills = false;
	    }
	    rsVoidBill.close();

	    sqlVoidBillBuilder.setLength(0);
	    sqlVoidBillBuilder.append(" select DATE_FORMAT(a.dteBillDate,'%d/%m/%y'),a.strBillNo SrNo,ifnull(d.strTableName,''),a.strBillNo "
		    + ",a.dblSubTotal,a.dblDiscountAmt,a.dblTaxAmt,a.dblTipAmount,0.00 Adv,a.dblGrandTotal,c.strSettelmentDesc"
		    + ",ifnull(e.strCustomerName,'')strCustomerName "
		    + "from  tblqbillhd a "
		    + "inner join tblqbillsettlementdtl b on a.strBillNo=b.strBillNo and date(a.dteBillDate)=date(b.dteBillDate) "
		    + "inner join tblsettelmenthd c on b.strSettlementCode=c.strSettelmentCode "
		    + "left outer join tbltablemaster d on a.strTableNo=d.strTableNo "
		    + "left outer join tblcustomermaster e on a.strCustomerCode=e.strCustomerCode "
		    + "where date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + "and c.strSettelmentType='Credit' ");
	    if (!POSCode.equalsIgnoreCase("All"))
	    {
		sqlVoidBillBuilder.append("AND a.strPosCode='" + POSCode + "' ");
	    }
	    sqlVoidBillBuilder.append(" group by a.strBillNo "
		    + " order by a.strBillNo ");

	    rsVoidBill = clsGlobalVarClass.dbMysql.executeResultSet(sqlVoidBillBuilder.toString());
	    while (rsVoidBill.next())
	    {
		pw.println();
		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(1), 10, "left"));
		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(2), 10, "left"));
		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(3), 10, "left"));
		pw.print(objUtility.funPrintTextWithAlignment(rsVoidBill.getString(4), 10, "left"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(rsVoidBill.getDouble(5)), 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(rsVoidBill.getDouble(6)), 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(rsVoidBill.getDouble(7)), 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(rsVoidBill.getDouble(8)), 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(rsVoidBill.getDouble(9)), 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(rsVoidBill.getDouble(10)), 10, "Right"));
		pw.print(objUtility.funPrintTextWithAlignment("  " + rsVoidBill.getString(11), 10, "left"));
		pw.print(objUtility.funPrintTextWithAlignment("  " + rsVoidBill.getString(12), 30, "left"));

		totalSubTotal += rsVoidBill.getDouble(5);
		totalDiscAmt += rsVoidBill.getDouble(6);
		totalTaxAmt += rsVoidBill.getDouble(7);
		advTotal += rsVoidBill.getDouble(9);
		totalGTTotal += rsVoidBill.getDouble(10);

		noCreditBills = false;
	    }
	    rsVoidBill.close();

	    pw.println();
	    pw.print(DASHLINE);

	    pw.println();
	    pw.print(objUtility.funPrintTextWithAlignment("Total", 10, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment("", 10, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment("", 10, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment("", 10, "left"));
	    pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(totalSubTotal), 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(totalDiscAmt), 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(totalTaxAmt), 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(totalTipAmt), 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(advTotal), 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment(gDecimalFormat.format(totalGTTotal), 10, "Right"));
	    pw.print(objUtility.funPrintTextWithAlignment(" ", 20, "left"));

	    pw.println();
	    pw.print(DASHLINE);
	    if (noCreditBills)
	    {
		pw.println();
		pw.print("No Credit Bills");
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}

	return 1;
    }

}
