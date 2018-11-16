package com.POSGlobal.controller;

import static com.POSGlobal.controller.clsGlobalVarClass.ListTDHOnModifierItem;
import static com.POSGlobal.controller.clsGlobalVarClass.ListTDHOnModifierItemMaxQTY;
import static com.POSGlobal.controller.clsGlobalVarClass.dbMysql;
import static com.POSGlobal.controller.clsGlobalVarClass.gConnectionActive;
import static com.POSGlobal.controller.clsGlobalVarClass.gNoOfDiscountedBills;
import static com.POSGlobal.controller.clsGlobalVarClass.gPOSDateForTransaction;
import static com.POSGlobal.controller.clsGlobalVarClass.gPOSOnlyDateForTransaction;
import static com.POSGlobal.controller.clsGlobalVarClass.gPOSStartDate;
import static com.POSGlobal.controller.clsGlobalVarClass.gPosCodeForReprintDocs;
import static com.POSGlobal.controller.clsGlobalVarClass.gQueryForSearch;
import static com.POSGlobal.controller.clsGlobalVarClass.gTotalAdvanceAmt;
import static com.POSGlobal.controller.clsGlobalVarClass.gTotalBills;
import static com.POSGlobal.controller.clsGlobalVarClass.gTotalCashInHand;
import static com.POSGlobal.controller.clsGlobalVarClass.gTotalCashSales;
import static com.POSGlobal.controller.clsGlobalVarClass.gTotalDiscounts;
import static com.POSGlobal.controller.clsGlobalVarClass.gTotalPayments;
import static com.POSGlobal.controller.clsGlobalVarClass.gTotalReceipt;
import static com.POSGlobal.controller.clsGlobalVarClass.hmUserForms;
import static com.POSGlobal.controller.clsGlobalVarClass.vArrSearchColumnSize;
import com.POSGlobal.view.frmOkPopUp;
import com.POSLicence.controller.clsClientDetails;
import com.POSLicence.controller.clsSMSPackDtl;
import com.POSPrinting.Text.DayEnd.clsDayEndTextReport;
import com.POSPrinting.Utility.clsPrintingUtility;
import com.POSPrinting.clsBillGeneration;
import java.awt.Desktop;
import java.awt.Frame;
import static java.awt.Frame.ICONIFIED;
import java.awt.Window;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.Attribute;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.PrinterName;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.swing.JRViewer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class clsUtility implements Cloneable
{

    private String amtInWords = "";

    public Object clone() throws CloneNotSupportedException
    {
	return super.clone();
    }

    public List funCalculateTax(List<clsItemDtlForTax> arrListItemDtl, String POSCode, String dtPOSDate, String billAreaCode, String operationTypeForTax, double subTotal, double discountAmt, String transType, String settlementCode, String taxOnSP) throws Exception
    {
	return funCheckDateRangeForTax(arrListItemDtl, POSCode, dtPOSDate, billAreaCode, operationTypeForTax, subTotal, discountAmt, transType, settlementCode, taxOnSP);
    }

    private List funCheckDateRangeForTax(List<clsItemDtlForTax> arrListItemDtl, String POSCode, String dtPOSDate, String billAreaCode, String operationTypeForTax, double subTotal, double discountAmt, String transType, String settlementCode, String taxOnSP) throws Exception
    {
	List<clsTaxCalculationDtls> arrListTaxDtl = new ArrayList<clsTaxCalculationDtls>();
	String taxCode = "", taxName = "", taxOnGD = "", taxCal = "", taxIndicator = "", taxType = "Percent";
	String opType = "", taxAreaCodes = "", taxOnTax = "No", taxOnTaxCode = "";
	double taxPercent = 0.00, taxFixedAmount = 0.00, taxableAmount = 0.00, taxCalAmt = 0.00;
	clsGlobalVarClass.dbMysql.execute("truncate table tbltaxtemp;");// Empty Tax Temp Table

	StringBuilder sbSql = new StringBuilder();
	sbSql.setLength(0);
	sbSql.append("select a.strTaxCode,a.strTaxDesc,a.strTaxOnSP,a.strTaxType,a.dblPercent"
		+ ",a.dblAmount,a.strTaxOnGD,a.strTaxCalculation,a.strTaxIndicator,a.strAreaCode,a.strOperationType"
		+ ",a.strItemType,a.strTaxOnTax,a.strTaxOnTaxCode "
		+ "from tbltaxhd a,tbltaxposdtl b "
		+ "where a.strTaxCode=b.strTaxCode and b.strPOSCode='" + POSCode + "' ");
	if (transType.equals("Tax Regen"))
	{
	    sbSql.append(" and date(a.dteValidFrom) <='" + dtPOSDate + "' and date(a.dteValidTo)>='" + dtPOSDate + "' ");
	}
	else
	{
	    sbSql.append(" and date(a.dteValidFrom) <='" + dtPOSDate + "' and date(a.dteValidTo)>='" + dtPOSDate + "' ");
	}
	sbSql.append(" and a.strTaxOnSP='" + taxOnSP + "' "
		+ "order by a.strTaxOnTax,a.strTaxDesc");

	ResultSet rsTax = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
	while (rsTax.next())
	{
	    taxCode = rsTax.getString(1);
	    taxName = rsTax.getString(2);
	    taxOnGD = rsTax.getString(7);
	    taxCal = rsTax.getString(8);
	    taxIndicator = rsTax.getString(9);
	    taxOnTax = rsTax.getString(13);
	    taxOnTaxCode = rsTax.getString(14);
	    taxType = rsTax.getString(4);//taxType
	    taxPercent = Double.parseDouble(rsTax.getString(5));//percent
	    taxFixedAmount = Double.parseDouble(rsTax.getString(6));//fixes amount

	    taxableAmount = 0.00;
	    taxCalAmt = 0.00;

	    boolean flgTax = false;

	    if (taxOnSP.equals("Sales"))
	    {
		String sqlTaxOn = "select strAreaCode,strOperationType,strItemType "
			+ "from tbltaxhd where strTaxCode='" + taxCode + "'";
		ResultSet rsTaxOn = clsGlobalVarClass.dbMysql.executeResultSet(sqlTaxOn);
		if (rsTaxOn.next())
		{
		    taxAreaCodes = rsTaxOn.getString(1);
		    opType = rsTaxOn.getString(2);
		}

		if (funCheckAreaCode(taxAreaCodes, billAreaCode))
		{
		    if (funCheckOperationType(opType, operationTypeForTax))
		    {
			if (funFindSettlementForTax(taxCode, settlementCode))
			{
			    flgTax = true;
			}
		    }
		}
	    }
	    else // Tax on Purchase
	    {
		flgTax = true;
	    }

	    if (flgTax)
	    {
		boolean flgTaxOnGrpApplicable = false;
		taxableAmount = 0;
		clsTaxCalculationDtls objTaxDtls = new clsTaxCalculationDtls();

		if (taxOnGD.equals("Gross"))
		{
		    //to calculate tax on group of an item
		    for (int i = 0; i < arrListItemDtl.size(); i++)
		    {
			clsItemDtlForTax objItemDtl = arrListItemDtl.get(i);

			boolean isApplicable = isTaxApplicableOnItemGroup(taxCode, objItemDtl.getItemCode().substring(0, 7));
			if (isApplicable)
			{
			    flgTaxOnGrpApplicable = true;
			    taxableAmount = taxableAmount + objItemDtl.getAmount();

			    if (taxOnTax.equalsIgnoreCase("Yes")) // For tax On Tax Calculation new logic only for same group item
			    {
				taxableAmount = taxableAmount + funGetTaxableAmountForTaxOnTax(taxOnTaxCode, objItemDtl.getAmount(), arrListTaxDtl);
			    }
			}
		    }
		}
		else
		{
		    subTotal = 0;
		    double discAmt = 0;
		    for (clsItemDtlForTax objItemDtl : arrListItemDtl)
		    {
			boolean isApplicable = isTaxApplicableOnItemGroup(taxCode, objItemDtl.getItemCode().substring(0, 7));
			if (isApplicable)
			{
			    flgTaxOnGrpApplicable = true;
			    if (objItemDtl.getDiscAmt() > 0)
			    {
				discAmt += objItemDtl.getDiscAmt();
			    }
			    taxableAmount = taxableAmount + objItemDtl.getAmount();

			    if (taxOnTax.equalsIgnoreCase("Yes")) // For tax On Tax Calculation new logic only for same group item
			    {
				taxableAmount = taxableAmount + funGetTaxableAmountForTaxOnTax(taxOnTaxCode, objItemDtl.getAmount() - objItemDtl.getDiscAmt(), arrListTaxDtl);
			    }
			}
		    }
		    if (taxableAmount > 0)
		    {
			taxableAmount = taxableAmount - discAmt;
		    }
		}

		if (flgTaxOnGrpApplicable)
		{
		    if (taxCal.equals("Forward")) // Forward Tax Calculation
		    {
			if (taxType.equalsIgnoreCase("Percent"))
			{
			    taxCalAmt = taxableAmount * (taxPercent / 100);
			}
			else
			{
			    taxCalAmt = taxFixedAmount;
			}
		    }
		    else // Backward Tax Calculation
		    {
			taxCalAmt = taxableAmount - (taxableAmount * 100 / (100 + taxPercent));
		    }

		    objTaxDtls.setTaxCode(taxCode);
		    objTaxDtls.setTaxName(taxName);
		    objTaxDtls.setTaxableAmount(taxableAmount);
		    objTaxDtls.setTaxAmount(taxCalAmt);
		    objTaxDtls.setTaxCalculationType(taxCal);
		    objTaxDtls.setIsTaxOnTax(taxOnTax);
		    objTaxDtls.setStrTaxOnTaxCode(taxOnTaxCode);
		    arrListTaxDtl.add(objTaxDtls);
		}
	    }
	}

	return arrListTaxDtl;
    }

    private boolean funCheckAreaCode(String taxAreaCodes, String billAreaCode)
    {
	boolean flgTaxOn = false;
	String[] spAreaCode = taxAreaCodes.split(",");
	for (int cnt = 0; cnt < spAreaCode.length; cnt++)
	{
	    if (spAreaCode[cnt].equals(billAreaCode))
	    {
		flgTaxOn = true;
		break;
	    }
	}

	return flgTaxOn;
    }

    private double funGetTaxIndicatorBasedDiscAmtTotal(String indicator, List<clsItemDtlForTax> arrListItemDtl) throws Exception
    {
	String sql_Query = "";
	double discAmt = 0.00;
	for (int cnt = 0; cnt < arrListItemDtl.size(); cnt++)
	{
	    clsItemDtlForTax objItemDtl = arrListItemDtl.get(cnt);
	    sql_Query = "select strTaxIndicator from tblitemmaster "
		    + "where strItemCode='" + objItemDtl.getItemCode().substring(0, 7) + "' "
		    + "and strTaxIndicator='" + indicator + "'";
	    ResultSet rsTaxForDB = clsGlobalVarClass.dbMysql.executeResultSet(sql_Query);
	    if (rsTaxForDB.next())
	    {
		discAmt += objItemDtl.getDiscAmt();
	    }
	    rsTaxForDB.close();
	}
	return discAmt;
    }

    private boolean funCheckOperationType(String taxOpTypes, String operationTypeForTax)
    {
	boolean flgTaxOn = false;
	String[] spOpType = taxOpTypes.split(",");
	for (int cnt = 0; cnt < spOpType.length; cnt++)
	{
	    if (spOpType[cnt].equals("HomeDelivery") && operationTypeForTax.equalsIgnoreCase("HomeDelivery"))
	    {
		flgTaxOn = true;
		break;
	    }
	    if (spOpType[cnt].equals("HomeDelivery") && operationTypeForTax.equalsIgnoreCase("Home Delivery"))
	    {
		flgTaxOn = true;
		break;
	    }
	    if (spOpType[cnt].equals("DineIn") && operationTypeForTax.equalsIgnoreCase("DineIn"))
	    {
		flgTaxOn = true;
		break;
	    }
	    if (spOpType[cnt].equals("DineIn") && operationTypeForTax.equalsIgnoreCase("Dine In"))
	    {
		flgTaxOn = true;
		break;
	    }
	    if (spOpType[cnt].equals("TakeAway") && operationTypeForTax.equalsIgnoreCase("TakeAway"))
	    {
		flgTaxOn = true;
		break;
	    }
	    if (spOpType[cnt].equals("TakeAway") && operationTypeForTax.equalsIgnoreCase("Take Away"))
	    {
		flgTaxOn = true;
		break;
	    }
	}
	return flgTaxOn;
    }

    private double funGetTaxIndicatorTotal(String indicator, List<clsItemDtlForTax> arrListItemDtl) throws Exception
    {
	String sql_Query = "";
	double indicatorAmount = 0.00;
	for (int cnt = 0; cnt < arrListItemDtl.size(); cnt++)
	{
	    clsItemDtlForTax objItemDtl = arrListItemDtl.get(cnt);
	    sql_Query = "select strTaxIndicator from tblitemmaster "
		    + "where strItemCode='" + objItemDtl.getItemCode().substring(0, 7) + "' "
		    + "and strTaxIndicator='" + indicator + "'";
	    ResultSet rsTaxForDB = clsGlobalVarClass.dbMysql.executeResultSet(sql_Query);
	    if (rsTaxForDB.next())
	    {
		indicatorAmount += objItemDtl.getAmount();
	    }
	    rsTaxForDB.close();
	}
	return indicatorAmount;
    }

    private double funGetItemTypeTotal(String itemType, List<clsItemDtlForTax> arrListItemDtl) throws Exception
    {
	String sql_Query = "";
	double itemTypeAmount = 0.00;

	for (int cnt = 0; cnt < arrListItemDtl.size(); cnt++)
	{
	    clsItemDtlForTax objItemDtl = arrListItemDtl.get(cnt);
	    sql_Query = "select strTaxIndicator from tblitemmaster "
		    + "where strItemCode='" + objItemDtl.getItemCode() + "' "
		    + "and and strItemType='" + itemType + "'";
	    ResultSet rsTaxForDB = clsGlobalVarClass.dbMysql.executeResultSet(sql_Query);
	    if (rsTaxForDB.next())
	    {
		itemTypeAmount += objItemDtl.getAmount();
	    }
	    rsTaxForDB.close();
	}
	return itemTypeAmount;
    }

    private boolean funFindSettlementForTax(String taxCode, String settlementCode) throws Exception
    {
	boolean flgTaxSettlement = false;
	String sql_SettlementTax = "select strSettlementCode,strSettlementName "
		+ "from tblsettlementtax where strTaxCode='" + taxCode + "' "
		+ "and strApplicable='true' and strSettlementCode='" + settlementCode + "'";
	ResultSet rsTaxSettlement = clsGlobalVarClass.dbMysql.executeResultSet(sql_SettlementTax);
	if (rsTaxSettlement.next())
	{
	    flgTaxSettlement = true;
	}
	rsTaxSettlement.close();
	return flgTaxSettlement;
    }

    private double funGetTaxableAmountForTaxOnTax(String taxOnTaxCode, List<clsTaxCalculationDtls> arrListTaxCal) throws Exception
    {
	double taxAmt = 0;
	String[] spTaxOnTaxCode = taxOnTaxCode.split(",");
	for (int cnt = 0; cnt < arrListTaxCal.size(); cnt++)
	{
	    for (int t = 0; t < spTaxOnTaxCode.length; t++)
	    {
		clsTaxCalculationDtls objTaxDtls = arrListTaxCal.get(cnt);
		if (objTaxDtls.getTaxCode().equals(spTaxOnTaxCode[t]))
		{
		    taxAmt += objTaxDtls.getTaxAmount();
		}
	    }
	}
	return taxAmt;
    }

    //new logic for tax on tax
    private double funGetTaxableAmountForTaxOnTax(String taxOnTaxCode, double taxableAmt, List<clsTaxCalculationDtls> listTaxDtl) throws Exception
    {
	double taxAmt = 0;
	String[] spTaxOnTaxCode = taxOnTaxCode.split(",");
	for (clsTaxCalculationDtls objTaxCalDtl : listTaxDtl)
	{
	    for (int t = 0; t < spTaxOnTaxCode.length; t++)
	    {
		if (objTaxCalDtl.getTaxCode().equals(spTaxOnTaxCode[t]))
		{
		    taxAmt += funGetTaxOnTaxAmt(spTaxOnTaxCode[t], taxableAmt);
		}
	    }
	}

	return taxAmt;
    }

    private double funGetTaxOnTaxAmt(String taxCode, double taxableAmt) throws Exception
    {
	double taxAmt = 0;
	String sql = "select a.strTaxCode,a.strTaxType,a.dblPercent"
		+ " ,a.dblAmount,a.strTaxOnGD,a.strTaxCalculation "
		+ " from tbltaxhd a "
		+ " where a.strTaxOnSP='Sales' and a.strTaxCode='" + taxCode + "'";
	ResultSet rsTax = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	if (rsTax.next())
	{
	    double taxPercent = rsTax.getDouble(3);
	    if (rsTax.getString(6).equals("Forward")) // Forward Tax Calculation
	    {
		taxAmt = taxableAmt * (taxPercent / 100);
	    }
	    else // Backward Tax Calculation
	    {
		taxAmt = taxableAmt * 100 / (100 + taxPercent);
		taxAmt = taxableAmt - taxAmt;
	    }
	}
	rsTax.close();
	return taxAmt;
    }

    // Function to generate search queries.   
    public Vector funCallForSearchForm(String searchFormName)
    {
	try
	{
	    clsGlobalVarClass.gSearchMasterFormName = "";
	    clsGlobalVarClass.gSearchFormName = searchFormName;
	    vArrSearchColumnSize = new Vector();

	    SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
	    java.util.Date temDate = dFormat.parse(gPOSStartDate);
	    String todate = (temDate.getYear() + 1900) + "-" + (temDate.getMonth() + 1) + "-" + temDate.getDate();

	    switch (searchFormName)
	    {
		case "MenuItem":
		    clsGlobalVarClass.gSearchMasterFormName = "Item Master";
		    gQueryForSearch = " select a.strItemCode as Item_Code,a.strItemName as Item_Name,a.strItemType as Item_Type,a.strRevenueHead as Revenue_Head,a.strTaxIndicator as Tax_Id,a.strExternalCode as External_Code,b.strSubGroupName as SubGroup_Name  "
			    + " from tblitemmaster a,tblsubgrouphd b "
			    + " where a.strSubGroupCode=b.strSubGroupCode "
			    + " order by a.strItemName";
		    vArrSearchColumnSize.add(30);
		    vArrSearchColumnSize.add(240);
		    vArrSearchColumnSize.add(50);
		    vArrSearchColumnSize.add(30);
		    vArrSearchColumnSize.add(30);
		    vArrSearchColumnSize.add(30);
		    vArrSearchColumnSize.add(30);
		    break;

		case "MenuItemForPlaceOrder":
		    clsGlobalVarClass.gSearchMasterFormName = "Item Master";
		    gQueryForSearch = " select a.strItemCode as Item_Code,a.strItemName as Item_Name,a.strItemType as Item_Type"
			    + ",a.strRevenueHead as Revenue_Head,b.strSubGroupName as SubGroup_Name,c.strGroupName as Group_Name "
			    + " from tblitemmaster a,tblsubgrouphd b, tblgrouphd c "
			    + " where a.strSubGroupCode=b.strSubGroupCode and b.strGroupCode=c.strGroupCode "
			    + " order by a.strItemName";
		    vArrSearchColumnSize.add(30);
		    vArrSearchColumnSize.add(200);
		    vArrSearchColumnSize.add(40);
		    vArrSearchColumnSize.add(50);
		    vArrSearchColumnSize.add(50);
		    vArrSearchColumnSize.add(30);
		    break;

		case "MenuItemForPrice":
		    clsGlobalVarClass.gSearchMasterFormName = "Item Master";
		    gQueryForSearch = " select a.strItemCode as Item_Code,a.strItemName as Item_Name,a.strItemType as Item_Type,a.strRevenueHead as Revenue_Head,a.strTaxIndicator as Tax_Id,a.strExternalCode as External_Code,b.strSubGroupName as Sub_Group_Name  "
			    + " from tblitemmaster a,tblsubgrouphd b "
			    + " where a.strSubGroupCode=b.strSubGroupCode "
			    + " and (a.strRawMaterial='N' or a.strItemForSale='Y') "
			    + " order by a.strItemName";
		    vArrSearchColumnSize.add(30);
		    vArrSearchColumnSize.add(240);
		    vArrSearchColumnSize.add(50);
		    vArrSearchColumnSize.add(30);
		    vArrSearchColumnSize.add(30);
		    vArrSearchColumnSize.add(30);
		    vArrSearchColumnSize.add(30);
		    break;

		case "MenuItemForRecipeChild":
		    clsGlobalVarClass.gSearchMasterFormName = "Item Master";
		    gQueryForSearch = "select a.strItemCode as Item_Code,a.strItemName as Item_Name,a.strItemType as Item_Type,a.strRevenueHead as Revenue_Head,a.strTaxIndicator as Tax_Id,a.strExternalCode as External_Code,b.strSubGroupName as Sub_Group_Name "
			    + "from tblitemmaster a,tblsubgrouphd b "
			    + "where a.strSubGroupCode=b.strSubGroupCode "
			    + "and a.strRawMaterial='Y' "
			    + "order by a.strItemName;";
		    vArrSearchColumnSize.add(30);
		    vArrSearchColumnSize.add(240);
		    vArrSearchColumnSize.add(50);
		    vArrSearchColumnSize.add(30);
		    vArrSearchColumnSize.add(30);
		    vArrSearchColumnSize.add(30);
		    vArrSearchColumnSize.add(30);
		    break;

		case "Menu":
		    clsGlobalVarClass.gSearchMasterFormName = "Menu Head Master";
		    gQueryForSearch = "select strMenuCode as Menu_Code,strMenuName as Menu_Name,strOperational as Operational from tblmenuhd "
			    + "order by strMenuName";
		    vArrSearchColumnSize.add(50);
		    vArrSearchColumnSize.add(200);
		    vArrSearchColumnSize.add(100);
		    break;

		case "MenuForCounter":
		    clsGlobalVarClass.gSearchMasterFormName = "Menu Head Master";
		    gQueryForSearch = "select strMenuCode as Menu_Code,strMenuName as Menu_Name from tblmenuhd where strOperational='Y' "
			    + "order by strMenuName";
		    vArrSearchColumnSize.add(250);
		    vArrSearchColumnSize.add(250);
		    break;

		case "SubMenu":
		    clsGlobalVarClass.gSearchMasterFormName = "SubMenu Head Master";
		    gQueryForSearch = "select strSubMenuHeadCode as SubMenu_Code,strSubMenuHeadName as SubMenuHead_Name,strSubMenuHeadShortName as SubMenu_Name,strSubMenuOperational as Operational from tblsubmenuhead "
			    + "order by strSubMenuHeadName";
		    vArrSearchColumnSize.add(500);
		    vArrSearchColumnSize.add(200);
		    vArrSearchColumnSize.add(200);
		    vArrSearchColumnSize.add(100);
		    break;

		case "TDH":
		    clsGlobalVarClass.gSearchMasterFormName = "TDH Master";
		    gQueryForSearch = "SELECT a.strTDHCode AS TDH_Code,a.strDescription AS Description, c.strMenuName AS Menu_Name, b.strItemName AS Item_Name "
			    + ", a.intMaxQuantity AS Quantity,a.strApplicable,a.strItemCode,a.strMenuCode "
			    + "FROM tbltdhhd a,tblitemmaster b,tblmenuhd c "
			    + "WHERE  a.strItemCode=b.strItemCode "
			    + "and a.strMenuCode=c.strMenuCode "
			    + "and a.strComboItemYN='N' "
			    + "ORDER BY strTDHCode ";
		    vArrSearchColumnSize.add(0);
		    vArrSearchColumnSize.add(300);
		    vArrSearchColumnSize.add(200);
		    vArrSearchColumnSize.add(250);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(0);
		    vArrSearchColumnSize.add(0);
		    break;

		case "TDHOnItem":
		    clsGlobalVarClass.gSearchMasterFormName = "TDH Master";
		    gQueryForSearch = "SELECT a.strTDHCode AS TDH_Code,a.strDescription AS Description, a.strMenuCode AS Menu_Code, b.strItemName AS Item_Name,a.strItemCode AS Item_Code, a.intMaxQuantity AS Quantity "
			    + "FROM tbltdhhd a,tblitemmaster b,tblmenuhd c "
			    + "WHERE  a.strItemCode=b.strItemCode "
			    + "and a.strMenuCode=c.strMenuCode "
			    + "and a.strComboItemYN='Y' "
			    + "ORDER BY strTDHCode";
		    vArrSearchColumnSize.add(250);
		    vArrSearchColumnSize.add(250);
		    vArrSearchColumnSize.add(250);
		    vArrSearchColumnSize.add(250);
		    vArrSearchColumnSize.add(250);
		    vArrSearchColumnSize.add(250);
		    break;

		case "AreaMaster":
		    clsGlobalVarClass.gSearchMasterFormName = "Area Master";
		    gQueryForSearch = "select strAreaCode as Area_Code,strAreaName as Area_Name from tblareamaster "
			    + "order by strAreaName";
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(200);
		    break;

		case "PSPCode":
		    clsGlobalVarClass.gSearchMasterFormName = "PSP Master";
		    gQueryForSearch = "select strPSPCode as PSP_Code,strPOSCode AS POS,strStkInCode as StockIn_Code,strBillNo as BillNo,"
			    + "dblStkInAmt as StkIn_Amt,dblSaleAmt as Sale_Amt,strUserCreated as User,dteDateCreated as Date "
			    + "from tblpsphd order by strPSPCode";
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    break;

		case "Reason":
		    clsGlobalVarClass.gSearchMasterFormName = "Reason Master";
		    gQueryForSearch = "select strReasonCode as Reason_Code,strReasonName as Reason_Name,strStkIn as StockIn,"
			    + "strStkOut as StockOut,strVoidBill as Void_Bill,strModifyBill as Modify_Bill,"
			    + "strTransferEntry as Transfer_Entry,strTransferType as Transfer_Type,strPSP as PSPosting,"
			    + "strKot as KOT,strCashMgmt as Cash_Mgmt, strVoidStkIn as Void_StockIn,"
			    + "strVoidStkOut as Void_StockOut,strUnsettleBill as Unsettle_Bill "
			    + "from tblreasonmaster  order by strReasonName";
		    vArrSearchColumnSize.add(50);
		    vArrSearchColumnSize.add(200);
		    vArrSearchColumnSize.add(50);
		    vArrSearchColumnSize.add(50);
		    vArrSearchColumnSize.add(50);
		    vArrSearchColumnSize.add(50);
		    vArrSearchColumnSize.add(50);
		    vArrSearchColumnSize.add(50);
		    vArrSearchColumnSize.add(50);
		    vArrSearchColumnSize.add(50);
		    vArrSearchColumnSize.add(50);
		    vArrSearchColumnSize.add(50);
		    vArrSearchColumnSize.add(50);
		    vArrSearchColumnSize.add(50);
		    break;

		case "Group":
		    clsGlobalVarClass.gSearchMasterFormName = "Group Master";
		    gQueryForSearch = "select strGroupCode as Group_Code,strGroupName as Group_Name from tblgrouphd "
			    + "order by strGroupName";
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(200);
		    break;

		case "SubGroup":
		    clsGlobalVarClass.gSearchMasterFormName = "SubGroup Master";
		    gQueryForSearch = "select strSubGroupCode as SubGroup_Code,strSubGroupName as SubGroup_Name,"
			    + "strGroupCode as Group_Code ,strIncentives as Incentives from tblsubgrouphd order by strSubGroupName";
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(200);
		    break;

		case "CostCenter":
		    clsGlobalVarClass.gSearchMasterFormName = "Cost Center Master";
		    gQueryForSearch = "select strCostCenterCode as CostCenter_Code,strCostCenterName as CostCenter_Name "
			    + "from tblcostcentermaster order by strCostCenterName";
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(200);
		    break;

		case "Settlement":
		    clsGlobalVarClass.gSearchMasterFormName = "Settlement Master";
		    gQueryForSearch = "select strSettelmentCode as Settlement_Code,strSettelmentDesc as Settlement_Desc,"
			    + "strSettelmentType as Settlement_Type,strApplicable as Applicable,strBilling as DirectBiller,"
			    + "strAdvanceReceipt as Advance_Order,dblConvertionRatio as Currency_Rate "
			    + "from tblsettelmenthd order by strSettelmentDesc";
		    vArrSearchColumnSize.add(50);
		    vArrSearchColumnSize.add(200);
		    vArrSearchColumnSize.add(80);
		    vArrSearchColumnSize.add(80);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(80);
		    break;

		case "Property":
		    clsGlobalVarClass.gSearchMasterFormName = "Property Master";
		    gQueryForSearch = "select strClientCode as Client_Code,strClientName as Client_Name,strAddressLine1 as Address1,"
			    + "strAddressLine2 as Address2,strAddressLine3 as Address3,strEmail as Email_Id,"
			    + "strBillFooter as Bill_Footer,strBillFooterStatus as BillFooter_Status,intBillPaperSize as Paper_Size,"
			    + "strPrintMode as Print_Mode,strDiscountNote as Discount_Note,strCityName as City_Name,strState as State,"
			    + "strCountry as Country,intTelephoneNo as Tele_No,dteStartDate as Start_Date,dteEndDate as End_Date,"
			    + "strNturofBusinnes as NatureOfBusinnes from tblsetup order by strClientName";
		    break;

		case "Pos":
		    clsGlobalVarClass.gSearchMasterFormName = "POS Master";
		    gQueryForSearch = "select strPosCode as POS_Code,strPosName as POS_Name,strPosType as POS_Type "
			    + "from tblposmaster order by strPosName";
		    vArrSearchColumnSize.add(80);
		    vArrSearchColumnSize.add(200);
		    vArrSearchColumnSize.add(200);
		    break;

		case "Price":
		    clsGlobalVarClass.gSearchMasterFormName = "Item Price";
		    gQueryForSearch = "select a.strItemCode as Item_Code,a.strItemName ,ifnull(b.strPosName,'') as POS_Name,c.strMenuName as Menu_Name,\n"
			    + "a.strPopular as Popular,ifnull(d.strCostCenterName,'') as CostCenter_Name,ifnull(e.strAreaName,'') as Area"
			    + ",a.strHourlyPricing as Hourly_Price,longPricingId as ID"
			    + " from tblmenuitempricingdtl a left outer join tblareamaster e on a.strAreaCode=e.strAreaCode \n"
			    + " left outer join tblposmaster b on (a.strPosCode=b.strPosCode or a.strPosCode='All') \n"
			    + " left outer join tblmenuhd c  on a.strMenuCode=c.strMenuCode\n"
			    + " left outer join tblcostcentermaster d on a.strCostCenterCode=d.strCostCenterCode\n"
			    + " order by a.strItemName asc";
		    break;

		case "Tax":
		    clsGlobalVarClass.gSearchMasterFormName = "Tax Master";
		    gQueryForSearch = "select strTaxCode as Tax_Code,strTaxDesc as Tax_Desc,strTaxOnSP as TaxOn_SP,"
			    + "strTaxType as Tax_Type,dblPercent as Tax_Percent,dblAmount as Tax_Amount,"
			    + "dteValidFrom as Valid_From,dteValidTo as Valid_To,strTaxOnGD as TaxOn_GD,"
			    + "strTaxCalculation as Tax_Calculation,strTaxIndicator as Tax_Id,"
			    + "strTaxRounded as Rounded,strTaxOnTax as TaxOn_Tax,strTaxOnTaxCode as TaxOn_TaxCode "
			    + "from tbltaxhd order by strTaxDesc";
		    break;

		case "TaxOnTax":
		    clsGlobalVarClass.gSearchMasterFormName = "Tax Master";
		    gQueryForSearch = "select strTaxCode as Tax_Code,strTaxDesc as Tax_Desc,strTaxOnSP as TaxOnSP,"
			    + "strTaxType as Tax_Type,dblPercent as Tax_Percent,dblAmount as TaxAmount,"
			    + "dteValidFrom as Valid_From,dteValidTo as Valid_To,strTaxOnGD as TaxOn_GD,"
			    + "strTaxCalculation as Tax_Calculation,strTaxIndicator as Tax_Id,"
			    + "strTaxRounded as Rounded,strTaxOnTax as TaxOnTax,strTaxOnTaxCode as TaxOnTaxCode "
			    + "from tbltaxhd order by strTaxDesc";
		    break;

		case "UserMaster":
		    clsGlobalVarClass.gSearchMasterFormName = "User Master";
		    gQueryForSearch = "select strUserCode as User_Code,strUserName as User_Name,strSuperType as User_Type,dteValidDate as Valid_Date,strPOSAccess as POS from tbluserhd order by strUserName";
		    vArrSearchColumnSize.add(70);
		    vArrSearchColumnSize.add(250);
		    vArrSearchColumnSize.add(80);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    break;

		case "Modifier":
		    clsGlobalVarClass.gSearchMasterFormName = "Modifier Master";
		    gQueryForSearch = "select strModifierCode as Modifier_Code,strModifierName as Modifier_Name,"
			    + "strModifierDesc as Modifier_Desc from tblmodifiermaster order by strModifierName";
		    break;

		case "TableMaster":
		    clsGlobalVarClass.gSearchMasterFormName = "Table Master";
		    gQueryForSearch = "select a.strTableNo as Table_No,a.strTableName as Table_Name,"
			    + "IFNULL(b.strAreaName,'') as Area_Name,IFNULL(c.strWShortName,'') as Waiter_Name "
			    + ",ifnull(d.strPosName,'All') as POS_Name ,a.strStatus as Table_Status "
			    + "from tbltablemaster a left outer join tblareamaster b "
			    + "on a.strAreaCode=b.strAreaCode left outer join tblwaitermaster c "
			    + "on a.strWaiterNo=c.strWaiterNo "
			    + "left outer join tblposmaster d on a.strPOSCode=d.strPOSCode "
			    + "order by a.strTableName";
		    vArrSearchColumnSize.add(50);
		    vArrSearchColumnSize.add(150);
		    vArrSearchColumnSize.add(150);
		    vArrSearchColumnSize.add(150);
		    vArrSearchColumnSize.add(150);
		    vArrSearchColumnSize.add(150);
		    break;

		case "TableMasterForKOT":
		    clsGlobalVarClass.gSearchMasterFormName = "Table Master";
		    gQueryForSearch = "select a.strTableNo as Table_No,a.strTableName as Table_Name,a.strStatus as Status "
			    + "from tbltablemaster a "
			    + "where (a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPOSCode='All') "
			    + "order by a.strTableName  ";
		    vArrSearchColumnSize.add(150);
		    vArrSearchColumnSize.add(150);
		    vArrSearchColumnSize.add(150);
		    break;

		case "MakeBillTableSearch":
		    clsGlobalVarClass.gSearchMasterFormName = "Table Master";
		    gQueryForSearch = "select a.strTableNo as Table_No,a.strTableName as Table_Name,a.strStatus as Status "
			    + "from tbltablemaster a "
			    + "where (a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPOSCode='All') "
			    + "and a.strStatus='Occupied' "
			    + "order by a.strTableName ";
		    vArrSearchColumnSize.add(150);
		    vArrSearchColumnSize.add(150);
		    vArrSearchColumnSize.add(150);
		    break;

		case "WaiterMaster":
		    clsGlobalVarClass.gSearchMasterFormName = "Waiter Master";
		    gQueryForSearch = "select strWaiterNo as Waiter_No,strWShortName as Short_Name,"
			    + "strWFullName as Full_Name,strStatus as Status "
			    + "from tblwaitermaster where strOperational='Y'"
			    + " order by strWShortName";
		    vArrSearchColumnSize.add(50);
		    vArrSearchColumnSize.add(150);
		    vArrSearchColumnSize.add(150);
		    vArrSearchColumnSize.add(150);
		    break;

		case "TableReserveMaster":
		    clsGlobalVarClass.gSearchMasterFormName = "Table Reserve Master";
		    gQueryForSearch = "select a.strTableNo as Table_No,a.strTableName as Table_Name,"
			    + "IFNULL(b.strAreaName,'') as Area_Name,IFNULL(c.strWShortName,'') as Waiter_Name "
			    + ",ifnull(d.strPosName,'All') as POS_Name ,a.strStatus as Table_Status "
			    + " from tbltablemaster a left outer join tblareamaster b "
			    + " on a.strAreaCode=b.strAreaCode left outer join tblwaitermaster c "
			    + "on a.strWaiterNo=c.strWaiterNo "
			    + "left outer join tblposmaster d on a.strPOSCode=d.strPOSCode where a.strStatus!='Reserve' "
			    + "order by a.strTableName ";
		    vArrSearchColumnSize.add(50);
		    vArrSearchColumnSize.add(150);
		    vArrSearchColumnSize.add(150);
		    vArrSearchColumnSize.add(150);
		    vArrSearchColumnSize.add(150);
		    vArrSearchColumnSize.add(150);
		    break;

		case "DeliveryPersonMaster":
		    clsGlobalVarClass.gSearchMasterFormName = "Delivery Person Master";
		    gQueryForSearch = "select strDPCode as Person_Code,strDPName as Name, if(strOperational='Y','YES','NO') as Operational "
			    + "from tbldeliverypersonmaster order by strDPName";
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(200);
		    vArrSearchColumnSize.add(200);
		    break;

		case "DeliveryBoyMaster":
		    clsGlobalVarClass.gSearchMasterFormName = "Delivery Boy Master";
		    gQueryForSearch = "select strDPCode as Person_Code,strDPName as Name "
			    + "from tbldeliverypersonmaster "
			    + "where strOperational='Y' order by strDPName";
		    vArrSearchColumnSize.add(250);
		    vArrSearchColumnSize.add(250);
		    break;

		case "BuildingMaster":
		    clsGlobalVarClass.gSearchMasterFormName = "Building Master";
		    gQueryForSearch = "select strBuildingCode as Building_Code,strBuildingName as Building_Name,"
			    + "strAddress as Address from tblbuildingmaster order by strBuildingName";
		    vArrSearchColumnSize.add(250);
		    vArrSearchColumnSize.add(250);
		    break;

		case "AvdBookReceipt":
		    clsGlobalVarClass.gSearchMasterFormName = "Advance Booking Receipt Master";
		    gQueryForSearch = "select a.strAdvBookingNo as Booking_No,a.strReceiptNo as Receipt_No,c.strCustomerName as 'Customer_Name' \n"
			    + "from tbladvancereceipthd a, tbladvbookbillhd b ,tblcustomermaster c \n"
			    + "where a.strAdvBookingNo=b.strAdvBookingNo and b.strCustomerCode=c.strCustomerCode order by a.strAdvBookingNo desc";
		    vArrSearchColumnSize.add(80);
		    vArrSearchColumnSize.add(200);
		    vArrSearchColumnSize.add(200);
		    break;

		case "UnsettleBill":
		    clsGlobalVarClass.gSearchMasterFormName = "Unsettle Bill";
		    gQueryForSearch = "select ifnull(d.strTableName,'ND') as Table_Name, a.strBillNo as Bill_No "
			    + " ,a.dblGrandTotal as Total_Amount,c.strSettelmentDesc as Settle_Mode, a.strUserCreated as User "
			    + " , a.strRemarks as Remarks "
			    + " from tblbillhd a inner join tblbillsettlementdtl b on a.strbillno=b.strbillno "
			    + " inner join tblsettelmenthd c on b.strSettlementCode=c.strSettelmentCode "
			    + " left outer join tbltablemaster d on a.strTableNo=d.strTableNo "
			    + " where date(a.dteBillDate)='" + funGetOnlyPOSDateForTransaction() + "' "
			    + " and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' ";
		    if (!clsGlobalVarClass.gSuperUser)
		    {
			gQueryForSearch += " and a.strUserCreated='" + clsGlobalVarClass.gUserCode + "' ";
		    }
		    gQueryForSearch += " group by a.strbillno order by a.strbillno DESC";
		    //System.out.println(gQueryForSearch);
		    break;

		case "SplitBill":
		    clsGlobalVarClass.gSearchMasterFormName = "Split Bill";
		    gQueryForSearch = "select strBillNo as Bill_No,TIME_FORMAT(time(dteBillDate),'%h:%i')  as Bill_Time,dblGrandTotal as Total_Amount "
			    + " from tblbillhd "
			    + " where strBillNo NOT IN(select strBillNo from tblbillsettlementdtl where date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ) "
			    + " and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
			    + " and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' "
			    + " and strBillNo NOT LIKE '%-%' "
			    + " order by strbillno DESC ";
		    break;

		case "SalesReportBill":
		    clsGlobalVarClass.gSearchMasterFormName = "Sales Report";
		    gQueryForSearch = "select strbillno as Bill_No,dteBillDate as Bill_Date,strPOSCode As POS_Code,"
			    + "dblGrandTotal as Total_Amount from tblbillhd order by strbillno DESC";
		    break;

		case "ReprintBillAllPOS":
		    clsGlobalVarClass.gSearchMasterFormName = "Reprint Bill";
		    gQueryForSearch = "select strbillno as Bill_No,strTableNo as Table_No,dteBillDate as Bill_Date,strPOSCode as POS_Code,"
			    + " dblGrandTotal as Total_Amount "
			    + " from tblbillhd "
			    + "where date(dteBillDate)='" + todate + "' and strPOSCode='" + gPosCodeForReprintDocs + "' "
			    + " order by strbillno DESC";
		    break;

		case "CustomerMaster":
		    clsGlobalVarClass.gSearchMasterFormName = "Customer Master";
		    if (clsGlobalVarClass.gClientCode.equals("009.001"))
		    {
			gQueryForSearch = "select strExternalCode as Customer_Code,strCustomerName as Name,"
				+ "longMobileNo as Mobile_No,strCustomerCode as Customer_Code "
				+ "from tblcustomermaster order by strCustomerName";
		    }
		    else
		    {
			gQueryForSearch = "select strCustomerCode as Customer_Code,strCustomerName as Name,"
				+ "longMobileNo as Mobile_No,strBuildingName as Area,strStreetName as Street "
				+ "from tblcustomermaster order by strCustomerName";
			vArrSearchColumnSize.add(50);
			vArrSearchColumnSize.add(100);
			vArrSearchColumnSize.add(50);
			vArrSearchColumnSize.add(100);
			vArrSearchColumnSize.add(100);
		    }
		    break;

		case "PluForAdvanceOrder":
		    clsGlobalVarClass.gSearchMasterFormName = "Advance Order";
		    gQueryForSearch = "select strItemCode as Item_Code,strItemName as Item_Name,"
			    + "strSubGroupCode as SubGroup_Code,strTaxIndicator as Tax_Id,"
			    + "strStockInEnable as StkIn_Enable,dblPurchaseRate as Purchase_Rate,"
			    + "intProcTimeMin as Proc_Time,strExternalCode as External_Code,"
			    + "strItemDetails as Item_Details from tblitemmaster  order by strItemName";
		    break;

		case "Shift Master":
		    clsGlobalVarClass.gSearchMasterFormName = "Shift Master";
		    gQueryForSearch = "select intShiftCode as Shift_Code,strPOSCode as POS_Code,dteDateCreated as ShiftDate,"
			    + "tmeShiftStart as Shift_Start_Time,tmeShiftEnd as Shift_End_Time,"
			    + "strBillDateTimeType as Bill_Date_Type from tblshiftmaster order by dteDateCreated";
		    break;

		case "AreaMasterForMakeKOT":
		    clsGlobalVarClass.gSearchMasterFormName = "Area Master";
		    gQueryForSearch = "select strAreaCode as Area_Code,strAreaName as Area_Name "
			    + " from tblareamaster "
			    + " where (strPOSCode='" + clsGlobalVarClass.gPOSCode + "' or strPOSCode='All') "
			    + " order by strAreaName";
		    if (!clsGlobalVarClass.gSuperUser && clsGlobalVarClass.gAutoAreaSelectionInMakeKOT)
		    {
			clsUtility objUtility = new clsUtility();
			String hostName = objUtility.funGetHostName();
			String physicalAddress = objUtility.funGetCurrentMACAddress();

			gQueryForSearch = "select strAreaCode as Area_Code,strAreaName as Area_Name "
				+ " from tblareamaster "
				+ " where (strPOSCode='" + clsGlobalVarClass.gPOSCode + "' or strPOSCode='All') "
				+ "and strMACAddress='" + physicalAddress + "' "
				+ " order by strAreaName";
		    }
		    vArrSearchColumnSize.add(250);
		    vArrSearchColumnSize.add(250);
		    break;

		case "CardType":
		    clsGlobalVarClass.gSearchMasterFormName = "Cash Type Master";
		    gQueryForSearch = "select strCardTypeCode as Card_Code,strCardName as Card_Name "
			    + "from tbldebitcardtype order by strCardTypeCode";
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(200);
		    break;

		case "CardNo":
		    clsGlobalVarClass.gSearchMasterFormName = "Card Type Master";
		    gQueryForSearch = "select strCardNo as Card_Number,strCardTypeCode as Card_Code,"
			    + "strStatus as Status from tbldebitcardmaster order by strCardNo";
		    vArrSearchColumnSize.add(165);
		    vArrSearchColumnSize.add(165);
		    vArrSearchColumnSize.add(165);
		    break;

		case "ReprintKOT":
		    clsGlobalVarClass.gSearchMasterFormName = "Reprint KOT";
		    gQueryForSearch = "select a.strKOTNo as KOT_No,a.dteDateCreated as DateTime "
			    + " ,IFNULL(c.strWShortName,'NA') as Waiter_Name,b.strTableName as Table_Name"
			    + " ,a.intPaxNo as Pax_No,a.strUserEdited as User_Created "
			    + " from tblitemrtemp a left outer join tbltablemaster b on a.strTableNo=b.strTableNo "
			    + " left outer join tblwaitermaster c  on a.strWaiterNo=c.strWaiterNo "
			    + " where a.strPOSCode='" + clsGlobalVarClass.gPosCodeForReprintDocs + "' "
			    + " group by a.strKOTNo,a.strTableNo "
			    + " order by a.strKOTNo desc";
		    vArrSearchColumnSize.add(50);
		    vArrSearchColumnSize.add(165);
		    vArrSearchColumnSize.add(165);
		    vArrSearchColumnSize.add(165);
		    vArrSearchColumnSize.add(165);
		    vArrSearchColumnSize.add(165);
		    break;

		case "GiftVoucher":
		    clsGlobalVarClass.gSearchMasterFormName = "GiftVoucher Master";
		    gQueryForSearch = "select strGiftVoucherCode as Voucher_Code,strGiftVoucherName as Voucher_Name,"
			    + "strGiftVoucherSeries as Voucher_Series,intTotalGiftVouchers as Total_Vouchers,"
			    + "strGiftVoucherValueType as Voucher_Type,dblGiftVoucherValue as Voucher_Value,"
			    + "date(dteValidFrom) as Valid_From,date (dteValidTo) as Valid_To from tblgiftvoucher order by strGiftVoucherCode";
		    vArrSearchColumnSize.add(50);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    break;

		case "GiftVoucherName":
		    clsGlobalVarClass.gSearchMasterFormName = "GiftVoucher Master";
		    gQueryForSearch = "select strGiftVoucherName as GiftVoucher_Name, strGiftVoucherSeries as GiftVoucher_Series,"
			    + " strGiftVoucherValueType as GiftVoucher_Type from  tblgiftvoucher;";
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    break;

		case "CustomerAddress":
		    clsGlobalVarClass.gSearchMasterFormName = "Customer Master";
		    gQueryForSearch = "select strCustomerCode as Customer_Code ,strCustomerName as Customer_Name,strBuildingName as Building_Name,strStreetName as Street_Name ,strCity as City,'Home'  "
			    + "from tblcustomermaster  "
			    + "where longMobileNo='" + clsGlobalVarClass.gCustMBNo + "' "
			    + "union all "
			    + "select strCustomerCode as Customer_Code,strCustomerName as Customer_Name,strOfficeBuildingName as Building_Name "
			    + ",strOfficeStreetName  as Street_Name,strOfficeCity  as City,'Office'  "
			    + "from tblcustomermaster  "
			    + "where longMobileNo='" + clsGlobalVarClass.gCustMBNo + "' "
			    + "and  CHAR_LENGTH(strOfficeBuildingName)>0 ";
		    break;

		case "CustTypeMaster":
		    clsGlobalVarClass.gSearchMasterFormName = "Customer Type Master";
		    gQueryForSearch = "select strCustTypeCode as Customer_Type_Code,strCustType as Customer_Type from tblcustomertypemaster "
			    + "order by strCustTypeCode";
		    break;

		case "ExpireDebitCard":
		    clsGlobalVarClass.gSearchMasterFormName = "Card Details";
		    gQueryForSearch = "select a.strCardNo as Card_No,c.strCustomerName as CardHolder_Name,"
			    + "c.strExternalCode as External_Code,b.strCardTypeCode as Card_Type,"
			    + "b.strCardName as Card_Name,a.strStatus as Status  "
			    + "from tbldebitcardmaster a,tbldebitcardtype b,tblcustomermaster c "
			    + "where a.strCardTypeCode=b.strCardTypeCode and a.strCustomerCode=c.strCustomerCode";
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    break;

		case "StockIn":
		    clsGlobalVarClass.gSearchMasterFormName = "StockIn Master";
		    gQueryForSearch = "select a.strStkInCode as Stk_In_Code,c.strSupplierName as Supplier_Name,b.strReasonName as Reason_Name, "
			    + " a.dteDateCreated as Date_Created from tblstkinhd a left outer join tblreasonmaster b "
			    + " on a.strReasonCode=b.strReasonCode ,tblsuppliermaster c "
			    + " where a.strReasonCode=b.strReasonCode and a.strSupplierCode=c.strSupplierCode "
			    + " order by a.strStkInCode";
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    break;

		case "StockOut":
		    clsGlobalVarClass.gSearchMasterFormName = "StockOut Master";
		    gQueryForSearch = " select a.strStkOutCode as Stk_Out_Code,ifnull(c.strSupplierName,'') as Supplier_Name,b.strReasonName as Reason_Name , "
			    + " a.dteDateCreated as Date_Created  from tblstkouthd a left outer join tblreasonmaster b   on a.strReasonCode=b.strReasonCode "
			    + " left outer join  tblsuppliermaster c on a.strSupplierCode=c.strSupplierCode  "
			    + " where a.strReasonCode=b.strReasonCode  "
			    + " order by a.strStkOutCode";
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    break;

		case "Counter":
		    clsGlobalVarClass.gSearchMasterFormName = "Counter Master";
		    gQueryForSearch = "select a.strCounterCode as Counter_Code,a.strCounterName as Counter_Name"
			    + " ,a.strOperational as Operational,strUserCode as User,strPOSCode as POSCode "
			    + " from tblcounterhd a \n"
			    + " group by  a.strCounterCode";
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(5);
		    break;

		case "CounterForOperation":
		    clsGlobalVarClass.gSearchMasterFormName = "Counter Master";
		    gQueryForSearch = "select a.strCounterCode as Counter_Code,a.strCounterName as Counter_Name,a.strOperational as Operational"
			    + " from tblcounterhd a  where a.strOperational='Yes'"
			    + " group by  a.strCounterCode";
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    break;

		case "ModifierGroup":
		    clsGlobalVarClass.gSearchMasterFormName = "SModifier Group Master";
		    gQueryForSearch = "select strModifierGroupCode as Modifier_GroupCode,strModifierGroupName as Modifier_GroupName,"
			    + " strModifierGroupShortName as Modifier_GroupShortName,strOperational as Operational "
			    + " from tblmodifiergrouphd group by strModifierGroupCode";
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    break;

		case "LoyaltyMaster":
		    clsGlobalVarClass.gSearchMasterFormName = "Loyalty Master";
		    gQueryForSearch = "select strLoyaltyCode as Loyalty_Code,dblAmount as Amount,dblLoyaltyPoints as Loyalty_Points,dblLoyaltyPoints1 as Loyalty_Points1,dblLoyaltyValue  as Loyalty_Value "
			    + "from tblloyaltypoints";
		    break;

		case "Recipe":
		    clsGlobalVarClass.gSearchMasterFormName = "Recipe Master";
		    gQueryForSearch = "select a.strRecipeCode as Recipe_Code,a.strItemCode as Item_Code,b.strItemName as Item_Name "
			    + " from tblrecipehd a left outer join tblitemmaster b on a.strItemCode=b.strItemCode ";
		    break;

		case "CloseProductionOrder":
		    clsGlobalVarClass.gSearchMasterFormName = "Production Details";
		    gQueryForSearch = "select strProductionCode as Production_Code,dteProductionDate as Production_Date,strClose as Close,strRemarks as Remarks "
			    + "from tblproductionhd where strClose='N'";
		    break;

		case "AdvOrderTypeMaster":
		    clsGlobalVarClass.gSearchMasterFormName = "Advance Order Type Details";
		    gQueryForSearch = "select strAdvOrderTypeCode as Advance_Order_Code,strAdvOrderTypeName as Advance_Order_Name,strOperational as Operational "
			    + "from tbladvanceordertypemaster";
		    break;

		case "PromoCode":
		    clsGlobalVarClass.gSearchMasterFormName = "Promotion Master";
		    gQueryForSearch = "select strPromoCode as Promotion_Code,strPromoName as Promotion_Name,strPromotionOn as Promotion_On "
			    + "from tblpromotionmaster order by strPromoCode";
		    break;

		case "DeliveryBoyCategoryMaster":
		    clsGlobalVarClass.gSearchMasterFormName = "Delivery Boy Category Master";
		    gQueryForSearch = "select a.strDelBoyCategoryCode as DeliveryBoy_CategoryCode,a.strDelBoyCategoryName as DeliveryBoy_CategoryName "
			    + "from tbldeliveryboycategorymaster a order by a.strDelBoyCategoryCode ";
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(200);
		    break;

		case "AreaWiseDeliveryBoyCharges":
		    clsGlobalVarClass.gSearchMasterFormName = "Areawise Delivery Boy Charges Deatils";
		    gQueryForSearch = "select a.strCustAreaCode,b.strBuildingName,a.strDeliveryBoyCode,c.strDPName,a.dblValue from tblareawisedelboywisecharges a\n"
			    + "left outer join tblbuildingmaster b on a.strCustAreaCode=b.strBuildingCode\n"
			    + "left outer join tbldeliverypersonmaster c on a.strDeliveryBoyCode=c.strDPCode";
		    break;

		case "PhysicalStock":
		    clsGlobalVarClass.gSearchMasterFormName = "Physical Stock Details";
		    gQueryForSearch = "select a.strPSPCode ,b.strItemCode, c.strItemName,a.dteDateCreated "
			    + " from tblPSPhd a,tblPSPdtl b,tblItemMaster c "
			    + "  where a.strPSPCode=b.strPSPCode  and b.strItemCode=c.strItemCode "
			    + "  and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
			    + "  group by a.strPSPCode";
		    vArrSearchColumnSize.add(100);
		    break;

		case "ZoneMaster":
		    clsGlobalVarClass.gSearchMasterFormName = "Zone Master";
		    gQueryForSearch = "select a.strZoneCode as Zone_Code,a.strZoneName as Zone_Name "
			    + "from tblzonemaster a order by a.strZoneCode ";
		    vArrSearchColumnSize.add(250);
		    vArrSearchColumnSize.add(250);
		    break;

		case "VoidAdvOrder":
		    clsGlobalVarClass.gSearchMasterFormName = "Advance Order Details";
		    gQueryForSearch = "select a.strAdvBookingNo as Adv_Booking_No,c.strCustomerName as Customer,date(a.dteOrderFor) as Order_For "
			    + " ,ifnull(b.dblAdvDeposite,0) as Adv_Deposite,a.dblGrandTotal as Grand_Total,ifnull(b.strReceiptNo,'') as Receipt_No "
			    + " from tbladvbookbillhd a left outer join tbladvancereceipthd b on a.strAdvBookingNo=b.strAdvBookingNo "
			    + " inner join tblcustomermaster c on a.strCustomerCode=c.strCustomerCode "
			    + " where  a.strAdvBookingNo NOT IN(select strAdvBookingNo from tblbillhd) ";
		    vArrSearchColumnSize.add(120);
		    vArrSearchColumnSize.add(60);
		    vArrSearchColumnSize.add(70);
		    vArrSearchColumnSize.add(70);
		    vArrSearchColumnSize.add(60);
		    vArrSearchColumnSize.add(50);
		    break;

		case "ContactNo":
		    clsGlobalVarClass.gSearchMasterFormName = "Customer Master";
		    gQueryForSearch = "select strCustomerCode,strCustomerName,longMobileNo from tblcustomermaster";
		    vArrSearchColumnSize.add(250);
		    vArrSearchColumnSize.add(250);
		    vArrSearchColumnSize.add(250);
		    break;

		case "TableReservation":
		    clsGlobalVarClass.gSearchMasterFormName = "Table Reservation Master";
		    gQueryForSearch = "select a.strResCode as Reservation_Code,b.strCustomerName as Customer_Name,ifnull(b.strBuldingCode,'') as Bulding_Code "
			    + ",ifnull(b.strBuildingName,'') as Building_Name,b.strCity as City "
			    + "from tblreservation a "
			    + "left outer join tblcustomermaster b on  a.strCustomerCode=b.strCustomerCode "
			    + "left  outer join tblbuildingmaster c on b.strBuldingCode=c.strBuildingCode "
			    + "where a.strCustomerCode<>'' and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' ";
		    vArrSearchColumnSize.add(50);
		    vArrSearchColumnSize.add(350);
		    vArrSearchColumnSize.add(40);
		    vArrSearchColumnSize.add(50);
		    vArrSearchColumnSize.add(80);
		    break;

		case "DeliveryBoyForHD":
		    clsGlobalVarClass.gSearchMasterFormName = "Delivery Person Master";
		    gQueryForSearch = "select strDPCode as Person_Code,strDPName as Name, if(strOperational='Y','YES','NO') as Operational "
			    + "from tbldeliverypersonmaster order by strDPName";
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(200);
		    vArrSearchColumnSize.add(200);
		    break;

		case "OrderMaster":
		    clsGlobalVarClass.gSearchMasterFormName = "Order Master";
		    gQueryForSearch = "select strOrderCode as Order_Code,strOrderDesc as Order_Name from tblordermaster "
			    + "order by strOrderDesc";
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(200);
		    break;

		case "CharactersticsMaster":
		    clsGlobalVarClass.gSearchMasterFormName = "Characterstics Master";
		    gQueryForSearch = "select strCharCode as Char_Code,strCharName as Char_Name from tblcharactersticsmaster "
			    + "order by strCharName";
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(200);
		    break;

		case "CashManagement":
		    clsGlobalVarClass.gSearchMasterFormName = "Cash Management Master";
		    gQueryForSearch = "select strTransID as Trans_ID,strTransType as Trans_Type,"
			    + "date(dteTransDate) as Trans_Date,strReasonCode as Reason,strPOSCode as POS_Code,"
			    + "dblAmount as Amount,strRemarks as Remark,strUserEdited as User_Edited,"
			    + "dteDateCreated as Date_Created,dteDateEdited as Date_Edited,"
			    + "strCurrencyType as Currency_Type,intShiftCode as Shift_Code from tblcashmanagement";
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    break;

		case "FactoryMaster":
		    clsGlobalVarClass.gSearchMasterFormName = "Factory Master";
		    gQueryForSearch = "select a.strFactoryCode as Factory_Code,a.strFactoryName as Factory_Name,a.strUserCreated as User_Created,a.strUserEdited as User_Edited,a.dteDateCreated as Date_Created "
			    + "from tblfactorymaster a ";
		    vArrSearchColumnSize.add(50);
		    vArrSearchColumnSize.add(350);
		    vArrSearchColumnSize.add(50);
		    vArrSearchColumnSize.add(50);
		    vArrSearchColumnSize.add(50);
		    break;

		case "SettleBill":

		    clsGlobalVarClass.gSearchMasterFormName = "Settle Bill";
		    gQueryForSearch = "select a.strBillNo as Bill_No,a.strJioMoneyRRefNo as Reliance_Ref_No,b.dblSettlementAmt as Settle_Amount from tblbillhd a,tblbillsettlementdtl b,tblsettelmenthd c "
			    + "where a.strBillNo=b.strBillNo and b.strSettlementCode=c.strSettelmentCode and c.strSettelmentType='JioMoney'";
		    vArrSearchColumnSize.add(150);
		    vArrSearchColumnSize.add(150);
		    vArrSearchColumnSize.add(150);
		    break;

		case "ChangeSettlement":
		    clsGlobalVarClass.gSearchMasterFormName = "ChangeSettlement";

		    gQueryForSearch = "select a.strBillNo as Bill_No,a.strSettelmentMode as Settlement_Mode,sum(b.dblSettlementAmt) as Settlement_Amount "
			    + " from tblbillhd a,tblbillsettlementdtl b, tblsettelmenthd c "
			    + " where a.strBillNo=b.strBillNo and b.strSettlementCode=c.strSettelmentCode and c.strSettelmentType!='Complementary' "
			    + " and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
			    + " group by a.strBillNo ";
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    break;

		case "PromoGroupMaster":
		    clsGlobalVarClass.gSearchMasterFormName = "PromoGroupMaster";
		    gQueryForSearch = "select a.strPromoGroupCode as Promo_Group_Code,a.strPromoGroupName as Promo_Group_Name"
			    + " from tblpromogroupmaster a ";
		    break;

		case "SupplierMaster":
		    clsGlobalVarClass.gSearchMasterFormName = "Supplier Master";
		    gQueryForSearch = "select strSupplierCode as Supplier_Code,strSupplierName as Supplier_Name,strAddress1 as Address1,strAddress2 as Address2,intMobileNumber as Mobile_No,strContactPerson as Contact_Person,strEmailId as Email,strGSTNo as GST_No "
			    + "from tblSuppliermaster order by strSupplierName";
		    vArrSearchColumnSize.add(50);
		    vArrSearchColumnSize.add(200);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    break;
		case "MenuItemForPO":
		    clsGlobalVarClass.gSearchMasterFormName = "Item Master";
		    gQueryForSearch = " select a.strItemCode as Item_Code,a.strItemName as Item_Name,a.strItemType as Item_Type,a.strRevenueHead as Revenue_Head,a.strTaxIndicator as Tax_Id,a.strExternalCode as External_Code,b.strSubGroupName as Sub_Group_Name"
			    + ",a.dblPurchaseRate as Purchase_Rate "
			    + " from tblitemmaster a,tblsubgrouphd b "
			    + " where a.strSubGroupCode=b.strSubGroupCode  and a.strRawMaterial='Y' "
			    + " order by a.strItemName";
		    vArrSearchColumnSize.add(30);
		    vArrSearchColumnSize.add(240);
		    vArrSearchColumnSize.add(50);
		    vArrSearchColumnSize.add(30);
		    vArrSearchColumnSize.add(30);
		    vArrSearchColumnSize.add(30);
		    vArrSearchColumnSize.add(30);
		    break;

		case "PurchseOrder":
		    clsGlobalVarClass.gSearchMasterFormName = "Purchase Order";
		    gQueryForSearch = "select a.strPOCode as PO_Code,b.strSupplierName as Supplier,a.dtePODate as PO_Date "
			    + " from tblpurchaseorderhd a,tblsupplierMaster b "
			    + " where a.strSupplierCode=b.strSupplierCode "
			    + " order by a.strPOCode,a.dtePODate ";
		    vArrSearchColumnSize.add(30);
		    vArrSearchColumnSize.add(240);
		    vArrSearchColumnSize.add(50);
		    break;

		case "POForStockIn":
		    clsGlobalVarClass.gSearchMasterFormName = "Purchase Order";
		    gQueryForSearch = "select a.strPOCode as PO_Code,b.strSupplierName as Supplier,a.dtePODate as PO_Date "
			    + " from tblpurchaseorderhd a,tblsupplierMaster b "
			    + " where a.strSupplierCode=b.strSupplierCode and strClosePO='N' "
			    + " order by a.strPOCode,a.dtePODate ";
		    vArrSearchColumnSize.add(30);
		    vArrSearchColumnSize.add(240);
		    vArrSearchColumnSize.add(50);
		    break;

		case "UOMMaster":
		    clsGlobalVarClass.gSearchMasterFormName = "UOM Master";
		    gQueryForSearch = "select a.strUomName as UOM_Name "
			    + " from tbluommaster a"
			    + " order by a.strPosCode ";
		    vArrSearchColumnSize.add(50);
		    break;

		case "RawMenuItem":
		    clsGlobalVarClass.gSearchMasterFormName = "Item Master";
		    gQueryForSearch = " select a.strItemCode as Item_Code,a.strItemName as Item_Name,a.strExternalCode as External_Code, "
			    + " a.dblPurchaseRate as Purchase_Rate,a.strUOM as Raw_Material_UOM "
			    + " from tblitemmaster a "
			    + "  where a.strRawMaterial='Y' "
			    + " order by a.strItemName";
		    vArrSearchColumnSize.add(30);
		    vArrSearchColumnSize.add(240);
		    vArrSearchColumnSize.add(30);
		    vArrSearchColumnSize.add(40);
		    vArrSearchColumnSize.add(50);
		    break;

		case "DiscountMaster":
		    clsGlobalVarClass.gSearchMasterFormName = "Discount Master";
		    gQueryForSearch = " select a.strDiscCode as Discount_Code,a.strDiscName as Description,b.strPosName as POS,a.strDiscOn as Discount_On "
			    + "from tbldischd a ,tblposmaster b "
			    + "where (a.strPOSCode=b.strPosCode or a.strPOSCode='All') "
			    + "and date(a.dteToDate)>='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' "
			    + "order by a.strDiscCode ";
		    vArrSearchColumnSize.add(40);
		    vArrSearchColumnSize.add(150);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    break;

		case "Payment Interface Master":
		    clsGlobalVarClass.gSearchMasterFormName = "Payment Interface Master";
		    gQueryForSearch = " select a.strPGCode as PG_Code,a.strPGName  as PG_Name,a.strPosCode as Pos_Code "
			    + "from tblonlinepaymentconfighd a,tblposmaster b "
			    + "where (a.strPOSCode=b.strPosCode or a.strPOSCode='All') "
			    + " group by a.strPGCode"
			    + " order by a.strPGCode ";
		    vArrSearchColumnSize.add(40);
		    vArrSearchColumnSize.add(150);
		    vArrSearchColumnSize.add(100);
		    break;

		case "PlayZonePricingMaster":
		    clsGlobalVarClass.gSearchMasterFormName = "PlayZone Pricing Master";
		    gQueryForSearch = "select a.strPlayZonePricingCode as PlayZone_Pricing_Code , a.strPosCode as POS_Code,"
			    + "b.strItemName as Item_Name, a.intTimeStamp as Time_Stamp "
			    + " from  tblplayzonepricinghd a,tblitemmaster b "
			    + "where a.strItemCode=b.strItemCode and a.strClientCode='" + clsGlobalVarClass.gClientCode + "'";
		    vArrSearchColumnSize.add(40);
		    vArrSearchColumnSize.add(150);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    break;

		case "MenuItemNoRaw":
		    clsGlobalVarClass.gSearchMasterFormName = "Item Master";
		    gQueryForSearch = " select a.strItemCode as Item_Code,a.strItemName as Item_Name,a.strItemType as Item_Type,a.strRevenueHead as Revenue_Head,a.strTaxIndicator as Tax_Id,a.strExternalCode as External_Code,b.strSubGroupName as SubGroup_Name  "
			    + " from tblitemmaster a,tblsubgrouphd b "
			    + " where a.strSubGroupCode=b.strSubGroupCode and a.strRawMaterial='N'"
			    + " order by a.strItemName";
		    vArrSearchColumnSize.add(30);
		    vArrSearchColumnSize.add(240);
		    vArrSearchColumnSize.add(50);
		    vArrSearchColumnSize.add(30);
		    vArrSearchColumnSize.add(30);
		    vArrSearchColumnSize.add(30);
		    vArrSearchColumnSize.add(30);
		    break;
		    
		case "WaiterWiseTableSearch":
		    clsGlobalVarClass.gSearchMasterFormName = "Item Master";
		    gQueryForSearch = "select a.strWaiterNo AS Waiter_No,a.strWShortName AS Short_Name,a.strWFullName AS Full_Name,a.strStatus AS STATUS, ifnull(c.strTableName,'') AS Table_Name " 
				    + " from (SELECT strWaiterNo,strWShortName,strWFullName,strStatus " 
				    + " FROM tblwaitermaster " 
				    + " WHERE strOperational='Y') a left outer join " 
				    + "(select strwaiterno, strtableno from tblitemrtemp group by strwaiterno, strtableno) b " 
				    + " on a.strWaiterNo = b.strWaiterNo left outer join tbltablemaster c " 
				    + " on b.strtableno = c.strTableNo " 
				    + " ORDER BY a.strWShortName ";
		    vArrSearchColumnSize.add(30);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
	    }
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
	return vArrSearchColumnSize;
    }

    // Function to generate search queries.   
    public Vector funCallForSearchForm(String searchFormName, String... filters)
    {
	try
	{
	    String billhd = "tblbillhd";
	    String billSettlement = "tblbillsettlementdtl";
	    String creditBillReceipthd = "tblcreditbillreceipthd";

	    String filter0 = filters[0];//date
	    String filter1 = filters[1];//tableType
	    String filter2 = filters[2];//areaCode
	    //String customerCodeFilter = filters[3];//customer code

	    if (filter1.equalsIgnoreCase("QFile"))
	    {
		billhd = "tblqbillhd";
		billSettlement = "tblqbillsettlementdtl";
		creditBillReceipthd = "tblqcreditbillreceipthd";
	    }

	    clsGlobalVarClass.gSearchMasterFormName = "";
	    clsGlobalVarClass.gSearchFormName = searchFormName;
	    vArrSearchColumnSize = new Vector();

	    SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
	    java.util.Date temDate = dFormat.parse(gPOSStartDate);
	    String todate = (temDate.getYear() + 1900) + "-" + (temDate.getMonth() + 1) + "-" + temDate.getDate();

	    switch (searchFormName)
	    {
		case "CreditBills":
		    clsGlobalVarClass.gSearchMasterFormName = "Credit Bills";
		    gQueryForSearch = " select a.strBillNo as Bill_No,sum(b.dblSettlementAmt) as Credit_Amount,d.strCustomerName as Customer_Name "
			    + "from " + billhd + " a," + billSettlement + " b,tblsettelmenthd c,tblcustomermaster d "
			    + "where a.strBillNo=b.strBillNo "
			    + "and b.strSettlementCode=c.strSettelmentCode "
			    + "and a.strCustomerCode=d.strCustomerCode "
			    + "and c.strSettelmentType='Credit' "
			    + "and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
			    + "and date(a.dteBillDate)='" + filter0 + "' "
			    + "and (b.dblSettlementAmt>(SELECT sum(c.dblReceiptAmt) from " + creditBillReceipthd + " c where c.strBillNo=a.strBillNo ) or b.dblSettlementAmt>0) "
			    + "group by a.strBillNo";

		    vArrSearchColumnSize.add(50);
		    vArrSearchColumnSize.add(50);
		    vArrSearchColumnSize.add(100);
		    break;

		case "TableMasterForAutoAreaSelectionInMakeKOT":
		    clsGlobalVarClass.gSearchMasterFormName = "Table Master";
		    gQueryForSearch = "select a.strTableNo as Table_No,a.strTableName as Table_Name,a.strStatus as Status "
			    + "from tbltablemaster a "
			    + "where (a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPOSCode='All') "
			    + "and a.strAreaCode='" + filter2 + "' "
			    + "order by a.strTableName  ";
		    vArrSearchColumnSize.add(150);
		    vArrSearchColumnSize.add(150);
		    vArrSearchColumnSize.add(150);
		    break;

		case "ChangeSettlement":
		    clsGlobalVarClass.gSearchMasterFormName = "ChangeSettlement";

		    gQueryForSearch = "select a.strBillNo as Bill_No,a.strSettelmentMode as Settlement_Mode,FORMAT(sum(b.dblSettlementAmt)," + clsGlobalVarClass.gNoOfDecimalPlace + ") as Settlement_Amount "
			    + " from " + billhd + " a," + billSettlement + " b, tblsettelmenthd c "
			    + " where a.strBillNo=b.strBillNo and b.strSettlementCode=c.strSettelmentCode and c.strSettelmentType!='Complementary' "
			    + " and date(a.dteBillDate)=date(b.dteBillDate) "
			    + " and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
			    + " and date(a.dteBillDate)='" + filter0 + "' "
			    + " group by a.strBillNo ";
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    break;

		case "DiscountMaster":
		    
		    if (filter0 == null)
		    {
			filter0 = "";
		    }
		    String oprationType = filter0;
		    String operationTypeFilter = " and a.strDineIn='Y' ";
		    if (oprationType.equalsIgnoreCase("DineIn") || oprationType.equalsIgnoreCase("DirectBiller"))
		    {
			operationTypeFilter = " and a.strDineIn='Y' ";
		    }
		    else if (oprationType.equalsIgnoreCase("HomeDelivery"))
		    {
			operationTypeFilter = " and a.strHomeDelivery='Y' ";
		    }
		    else if (oprationType.equalsIgnoreCase("TakeAway"))
		    {
			operationTypeFilter = " and a.strTakeAway='Y' ";
		    }
		    clsGlobalVarClass.gSearchMasterFormName = "Discount Master";
		    gQueryForSearch = " select a.strDiscCode as Discount_Code,a.strDiscName as Description,b.strPosName as POS,a.strDiscOn as Discount_On "
			    + " from tbldischd a ,tblposmaster b "
			    + " where (a.strPOSCode=b.strPosCode or a.strPOSCode='All') "
			    + " and date(a.dteToDate)>='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' "
			    + " and (a.strPOSCode='"+clsGlobalVarClass.gPOSCode+"' or a.strPOSCode='All')"
			    + " " + operationTypeFilter + " "
			    + "order by a.strDiscCode ";
		    vArrSearchColumnSize.add(40);
		    vArrSearchColumnSize.add(150);
		    vArrSearchColumnSize.add(100);
		    vArrSearchColumnSize.add(100);
		    break;
	    }
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
	return vArrSearchColumnSize;
    }

    public String funGetRemark(String BillNo, String FormName)
    {
	String remark = "";
	String sql = "";
	ResultSet rs = null;
	try
	{
	    sql = "select strRemarks from  tblbillhd where strBillNo='" + BillNo + "'";
	    rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rs.next())
	    {
		remark = rs.getString(1);
	    }
	    else
	    {
		remark = " ";
	    }
	    rs.close();
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
	finally
	{
	    return remark;
	}
    }

    public String funReasonName(String BillNo)
    {
	String reasonName = "";
	String sql = "";
	ResultSet rs = null;
	try
	{
	    reasonName = " ";
	    sql = "select b.strReasonName from tblbillhd a,tblreasonmaster b where a.strReasonCode=b.strReasonCode "
		    + "and strBillNo='" + BillNo + "'";
	    rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rs.next())
	    {
		reasonName = rs.getString(1);
	    }
	    rs.close();
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
	finally
	{
	    return reasonName;
	}
    }

    public boolean funCheckComplementaryBill(String BillNo)
    {
	boolean flagComplementaryBill = false;
	String sql = "";
	ResultSet rs;
	try
	{
	    sql = "select strSettelmentMode from tblbillhd where strBillNo='" + BillNo + "';";
	    rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    rs.next();
	    String settlmentMode = rs.getString(1);
	    if ("Complementary".trim().equals(settlmentMode))
	    {
		flagComplementaryBill = true;
	    }
	    else
	    {
		flagComplementaryBill = false;
	    }
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
	finally
	{
	    return flagComplementaryBill;
	}
    }

    public boolean funCkeckBillWithoutWaiter(String BillNo)
    {
	boolean flagCkeckBillWithoutWaiter = false;
	try
	{
	    String sql = "";
	    sql = "select strWaiterNo  from tblbillhd where strBillNo='" + BillNo + "' group by strBillNo";
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rs.next())
	    {
		if ("null".equalsIgnoreCase(rs.getString(1)))
		{
		    flagCkeckBillWithoutWaiter = true;
		}
		else
		{
		    flagCkeckBillWithoutWaiter = false;
		}
	    }
	    rs.close();
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
	finally
	{
	    return flagCkeckBillWithoutWaiter;
	}
    }

    public boolean funCkeckKOTWithoutWaiter(String TableNo, String formName)
    {
	boolean flagCkeckKOTWithoutWaiter = false;
	try
	{
	    String sql = "";
	    if (formName.equals("KOT"))
	    {
		sql = "select strWaiterNo  from tblitemrtemp where strTableNo='" + TableNo + "' group by strTableNo";
	    }
	    if (formName.equals("ReprintKOT"))
	    {
		sql = "select strWaiterNo  from tblitemrtemp where strKOTNo='" + TableNo + "' group by strKOTNo";
	    }
	    if (formName.equals("SettleBill"))
	    {
		sql = "select strWaiterNo  from tblbillhd where strBillNo='" + TableNo + "' group by strBillNo";
	    }
	    if (formName.equals("VoidKOT"))
	    {
		sql = "select strWaiterNo  from tbltempvoidkot where strKOTNo='" + TableNo + "' group by strTableNo";
	    }
	    // System.out.println(sql);
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rs.next())
	    {
		if ("null".equalsIgnoreCase(rs.getString(1)))
		{
		    flagCkeckKOTWithoutWaiter = true;
		}
		else
		{
		    flagCkeckKOTWithoutWaiter = false;
		}
	    }
	    rs.close();
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
	finally
	{
	    return flagCkeckKOTWithoutWaiter;
	}
    }

    public boolean funCheckTakeAwayTable(String TableNo, String formName)
    {
	boolean flagTakeAway = false;
	try
	{
	    String sql = "";
	    if ("SaveKOT".equalsIgnoreCase(formName))
	    {
		sql = "select count(*) from tblitemrtemp where strTableNo='" + TableNo + "' and strTakeAwayYesNo='Yes'";
	    }
	    if ("ReprintKOT".equalsIgnoreCase(formName))
	    {
		sql = "select count(*) from tblitemrtemp where strKOTNo='" + TableNo + "'  and strTakeAwayYesNo='Yes' group by strKOTNo";
	    }
	    ResultSet rsTakeAway = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    rsTakeAway.next();
	    int exec = rsTakeAway.getInt(1);
	    rsTakeAway.close();
	    if (exec > 0)
	    {
		flagTakeAway = true;
	    }
	    else
	    {
		flagTakeAway = false;
	    }
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
	finally
	{
	    return flagTakeAway;
	}
    }

    public boolean funCheckLengthForContactNos(String text, int length)
    {
	boolean flagValidateLength = false;
	try
	{
	    if (text.trim().length() >= 6 && text.trim().length() <= length)
	    {
		flagValidateLength = true;
	    }
	    else
	    {
		flagValidateLength = false;
	    }
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
	finally
	{
	    return flagValidateLength;
	}
    }

    public boolean funCheckLength(String text, int length)
    {
	boolean flagValidateLength = false;
	try
	{
	    if (text.trim().length() <= length)
	    {
		flagValidateLength = true;
	    }
	    else
	    {
		flagValidateLength = false;
	    }
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
	finally
	{
	    return flagValidateLength;
	}
    }

    /**
     *
     * @param debitCardNo
     * @param compareType
     * @return card status
     *
     * This function accepts debit card no or debit card string and check the
     * status of that card no or card string and return the card status
     *
     */
    public String funGetDebitCardStatus(String debitCardNo, String compareType)
    {
	//System.out.println("Card= "+debitCardNo);
	String cardStatus = "Invalid Card";
	if (debitCardNo != null)
	{
	    try
	    {
		String sql = "";
		if (compareType.equals("CardString"))
		{
		    sql = "select b.strStatus,date(a.dteExpiryDt),a.strSetExpiryDt,DATE_ADD(date(b.dteDateCreated),interval a.intValidityDays day)dteExpiryDtForCard "
			    + ",a.intValidityDays,a.strSetExpiryTime,a.intExpiryTime "
			    + " from tbldebitcardtype a,tbldebitcardmaster b "
			    + " where a.strCardTypeCode=b.strCardTypeCode and b.strCardString='" + debitCardNo + "' ";
		    //+ " and date(a.dteExpiryDt) >= '"+clsGlobalVarClass.gPOSDateForTransaction+"'";
		}
		else
		{
		    sql = "select b.strStatus,date(a.dteExpiryDt),a.strSetExpiryDt,DATE_ADD(date(b.dteDateCreated),interval a.intValidityDays day)dteExpiryDtForCard  "
			    + ",a.intValidityDays,a.strSetExpiryTime,a.intExpiryTime "
			    + " from tbldebitcardtype a,tbldebitcardmaster b "
			    + " where a.strCardTypeCode=b.strCardTypeCode and b.strCardNo='" + debitCardNo + "' ";
		    //+ " and date(a.dteExpiryDt) >= '"+clsGlobalVarClass.gPOSDateForTransaction+"'";
		}
		//System.out.println(sql);
		ResultSet rsCardStatus = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		if (rsCardStatus.next())
		{
		    if (rsCardStatus.getString(3).equals("Y"))
		    {
			String cardTypeExpiryDate = rsCardStatus.getString(2).split("-")[0] + "-" + rsCardStatus.getString(2).split("-")[1] + "-" + rsCardStatus.getString(2).split("-")[2];

			String cardExpiryDate = rsCardStatus.getString(4).split("-")[0] + "-" + rsCardStatus.getString(4).split("-")[1] + "-" + rsCardStatus.getString(4).split("-")[2];

			String posDate = clsGlobalVarClass.gPOSDateForTransaction.split(" ")[0];
			String currentDate = posDate.split("-")[0] + "-" + posDate.split("-")[1] + "-" + posDate.split("-")[2];

			long diff = funCompareDate(currentDate, cardTypeExpiryDate);
			if (diff < 0)
			{
			    cardStatus = "Card Validity Is Expired";
			}
			else
			{
			    if (rsCardStatus.getString(1).equals("Active"))
			    {
				cardStatus = "Active";
			    }
			    else
			    {
				cardStatus = "Card is Not Active";
			    }
			}
			String isSetExpirayTime = rsCardStatus.getString(6);
			if (isSetExpirayTime.equalsIgnoreCase("Y"))
			{
			    cardStatus = funIsCardTimeExpire(debitCardNo);
			}

		    }
		    else
		    {
			if (rsCardStatus.getString(1).equals("Active"))
			{
			    cardStatus = "Active";
			}
			else
			{
			    cardStatus = "Card is Not Active";
			}

			String isSetExpirayTime = rsCardStatus.getString(6);
			if (isSetExpirayTime.equalsIgnoreCase("Y"))
			{
			    cardStatus = funIsCardTimeExpire(debitCardNo);
			}
		    }
		}
		else
		{
		    cardStatus = "Card Is Not Registered.";
		}
		rsCardStatus.close();
	    }
	    catch (Exception e)
	    {
		funShowDBConnectionLostErrorMessage(e);
		e.printStackTrace();
	    }
	}
	return cardStatus;
    }

    /**
     *
     * @param debitCardNo
     * @param compareType
     * @return card status
     *
     * This function accepts debit card no or debit card string and check the
     * status of that card no or card string and return the card status
     *
     */
    public String funGetDebitCardStatus(String debitCardNo, String compareType, String formName)
    {
	//System.out.println("Card= "+debitCardNo);
	String cardStatus = "Invalid Card";
	if (debitCardNo != null)
	{
	    try
	    {
		String sql = "";
		if (compareType.equals("CardString"))
		{
		    sql = "select b.strStatus,date(a.dteExpiryDt),a.strSetExpiryDt,DATE_ADD(date(b.dteDateCreated),interval a.intValidityDays day)dteExpiryDtForCard "
			    + " from tbldebitcardtype a,tbldebitcardmaster b "
			    + " where a.strCardTypeCode=b.strCardTypeCode and b.strCardString='" + debitCardNo + "' ";
		    //+ " and date(a.dteExpiryDt) >= '"+clsGlobalVarClass.gPOSDateForTransaction+"'";
		}
		else
		{
		    sql = "select b.strStatus,date(a.dteExpiryDt),a.strSetExpiryDt,DATE_ADD(date(b.dteDateCreated),interval a.intValidityDays day)dteExpiryDtForCard  "
			    + " from tbldebitcardtype a,tbldebitcardmaster b "
			    + " where a.strCardTypeCode=b.strCardTypeCode and b.strCardNo='" + debitCardNo + "' ";
		    //+ " and date(a.dteExpiryDt) >= '"+clsGlobalVarClass.gPOSDateForTransaction+"'";
		}
		//System.out.println(sql);
		ResultSet rsCardStatus = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		if (rsCardStatus.next())
		{
		    if (rsCardStatus.getString(3).equals("Y"))
		    {
			String cardTypeExpiryDate = rsCardStatus.getString(2).split("-")[0] + "-" + rsCardStatus.getString(2).split("-")[1] + "-" + rsCardStatus.getString(2).split("-")[2];

			String cardExpiryDate = rsCardStatus.getString(4).split("-")[0] + "-" + rsCardStatus.getString(4).split("-")[1] + "-" + rsCardStatus.getString(4).split("-")[2];

			String posDate = clsGlobalVarClass.gPOSDateForTransaction.split(" ")[0];
			String currentDate = posDate.split("-")[0] + "-" + posDate.split("-")[1] + "-" + posDate.split("-")[2];

			long diff = 0;
			if ("frmRegisterInOutPlayZone".trim().equalsIgnoreCase(formName))
			{
			    diff = funCompareDate(currentDate, cardExpiryDate);
			}
			else
			{
			    diff = funCompareDate(currentDate, cardTypeExpiryDate);
			}
			if (diff < 0)
			{
			    cardStatus = "Card Validity Is Expired";
			}
			else
			{
			    if (rsCardStatus.getString(1).equals("Active"))
			    {
				cardStatus = "Active";
			    }
			    else
			    {
				cardStatus = "Card is Not Active";
			    }
			}
		    }
		    else
		    {
			if (rsCardStatus.getString(1).equals("Active"))
			{
			    cardStatus = "Active";
			}
			else
			{
			    cardStatus = "Card is Not Active";
			}
		    }
		}
		else
		{
		    cardStatus = "Card Is Not Registered.";
		}
		rsCardStatus.close();
	    }
	    catch (Exception e)
	    {
		funShowDBConnectionLostErrorMessage(e);
		e.printStackTrace();
	    }
	}
	return cardStatus;
    }

    /**
     *
     * @param debitCardString
     * @param tableNo
     * @return debit card balance
     * @throws Exception
     *
     * function accepts debit card string and table no and calculate the card
     * balance based on card balance=card old balance-live bill amount-live KOT
     * amount
     */
    public double funGetDebitCardBalance(String debitCardString, String tableNo) throws Exception
    {
	double debitCardBalance = 0;
	String sql = "";

	sql = "select a.strCardNo,a.dblRedeemAmt,c.dblcardvaluefixed,c.dblDepositAmt "
		+ " from tbldebitcardmaster a,tbldebitcardtype c "
		+ " where a.strCardTypeCode=c.strCardTypeCode and a.strCardString='" + clsGlobalVarClass.gDebitCardNo + "'";

	ResultSet rsDebitCardNo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	if (rsDebitCardNo.next())
	{
	    String debitCardNo = rsDebitCardNo.getString(1);
	    debitCardBalance = rsDebitCardNo.getDouble(2) - rsDebitCardNo.getDouble(3);
	    debitCardBalance = debitCardBalance - rsDebitCardNo.getDouble(4);

	    sql = "select sum(dblAmount),dblTaxAmt "
		    + " from tblitemrtemp "
		    + " where strCardNo='" + debitCardNo + "' "
		    + " group by strTableNo;";
	    ResultSet rsOpenKOTs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsOpenKOTs.next())
	    {
		debitCardBalance -= (rsOpenKOTs.getDouble(1) + rsOpenKOTs.getDouble(2));
	    }
	    rsOpenKOTs.close();

	    sql = "select sum(dblGrandTotal) "
		    + " from tblbillhd where strCardNo='" + debitCardNo + "' "
		    + " and strBillNo not in (select strBillNo from tblbillsettlementdtl) "
		    + " group by strBillNo ";
	    ResultSet rsOpenBills = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsOpenBills.next())
	    {
		debitCardBalance -= rsOpenBills.getDouble(1);
	    }
	    rsOpenBills.close();
	}
	rsDebitCardNo.close();

	return debitCardBalance;
    }

    public double funGetDebitCardBalanceWithoutLiveBills(String debitCardString, String tableNo) throws Exception
    {
	double debitCardBalance = 0;
	String sql = "";

	ResultSet rsDebitCardNo = clsGlobalVarClass.dbMysql.executeResultSet("SELECT a.strCardNo,a.dblRedeemAmt,c.dblcardvaluefixed,c.dblDepositAmt "
		+ "FROM tbldebitcardmaster a,tbldebitcardtype c "
		+ "WHERE a.strCardTypeCode=c.strCardTypeCode "
		+ "AND a.strCardString='" + clsGlobalVarClass.gDebitCardNo + "' ");
	if (rsDebitCardNo.next())
	{
	    String debitCardNo = rsDebitCardNo.getString(1);
	    debitCardBalance = rsDebitCardNo.getDouble(2);
	    debitCardBalance = debitCardBalance - rsDebitCardNo.getDouble(4);

	    if (!tableNo.trim().isEmpty())
	    {
		sql = "select sum(dblAmount),dblTaxAmt "
			+ " from tblitemrtemp "
			+ " where strCardNo='" + debitCardNo + "' and strTableNo='" + tableNo + "' "
			+ " group by strTableNo;";
		ResultSet rsOpenKOTs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		if (rsOpenKOTs.next())
		{
		    debitCardBalance -= (rsOpenKOTs.getDouble(1) + rsOpenKOTs.getDouble(2));
		}
		rsOpenKOTs.close();
	    }
	}
	rsDebitCardNo.close();

	return debitCardBalance;
    }

    public int funShiftCardBalToRevenueTable(String posCode, String posDate) throws Exception
    {
	String sql = "select a.strCardTypeCode from tbldebitcardtype a "
		+ "where a.intValidityDays=1";
	ResultSet rsCardType = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	if (rsCardType.next())
	{
	    sql = "select a.strCardNo,a.dblRedeemAmt "
		    + " from tbldebitcardmaster a "
		    + " where a.strCardTypeCode='" + rsCardType.getString(1) + "' and a.dblRedeemAmt > 0";
	    ResultSet rsCardDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsCardDtl.next())
	    {
		sql = "insert into tbldebitcardrevenue (strCardNo,dblCardAmt,strPOSCode,dtePOSDate,dteDate"
			+ ",strClientCode,strDataPostFlag,strUserCreated) values"
			+ "('" + rsCardDtl.getString(1) + "','" + rsCardDtl.getString(2) + "','" + posCode + "','" + posDate + "'"
			+ ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "','N'"
			+ ",'" + clsGlobalVarClass.gUserCode + "')";
		//System.out.println(sql);
		clsGlobalVarClass.dbMysql.execute(sql);
		sql = "update tbldebitcardmaster set dblRedeemAmt=0 where strCardNo='" + rsCardDtl.getString(1) + "'";
		clsGlobalVarClass.dbMysql.execute(sql);
	    }
	    rsCardDtl.close();
	}
	rsCardType.close();

	return 1;
    }

    // Function to check Unsettled bills. Invoked from shift end/Day End form.
    public boolean funCheckPendingBills(String posCode)
    {
	boolean flgPendingBills = false;
	try
	{
	    String sqlPendingBill = "";
	    ResultSet rsPendingBills = null;
	    sqlPendingBill = "select count(*) "
		    + "from tblbillhd where date(dteBillDate)='" + funGetOnlyPOSDateForTransaction() + "' "
		    + "and strBillNo NOT IN(select strBillNo from tblbillsettlementdtl) "
		    + "and strPOSCode='" + posCode + "'";
	    //System.out.println(sql_PendingBill);
	    rsPendingBills = clsGlobalVarClass.dbMysql.executeResultSet(sqlPendingBill);
	    rsPendingBills.next();
	    if (rsPendingBills.getInt(1) > 0)
	    {
		flgPendingBills = true;
	    }
	    rsPendingBills.close();
	    sqlPendingBill = "select count(*) from tblbillhd "
		    + " where date(dteBillDate)='" + funGetOnlyPOSDateForTransaction() + "' and  strTableNo is not NULL and strBillNo"
		    + " NOT IN(select strBillNo from tblbillsettlementdtl) "
		    + " and strPOSCode='" + posCode + "'";
	    //System.out.println(sql_PendingBill);
	    rsPendingBills = clsGlobalVarClass.dbMysql.executeResultSet(sqlPendingBill);
	    rsPendingBills.next();
	    if (rsPendingBills.getInt(1) > 0)
	    {
		flgPendingBills = true;
	    }
	    rsPendingBills.close();
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
	return flgPendingBills;
    }

// Function to check Busy Tables which are not billed. Invoked from shift end/Day End form.    
    public boolean funCheckTableBusy(String posCode)
    {
	boolean flgPendingBills = false;
	try
	{
	    clsGlobalVarClass.dbMysql.execute("delete from tblitemrtemp where strNCKotYN='Y'");
	    clsGlobalVarClass.dbMysql.execute("delete from tblitemrtemp_bck where strNCKotYN='Y'");
	    String sql = "select count(*) from tblitemrtemp where strNCKotYN='N' "
		    + "and strPOSCode='" + posCode + "'";
	    ResultSet rsCheckTableBusy = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    rsCheckTableBusy.next();
	    if (rsCheckTableBusy.getInt(1) > 0)
	    {
		flgPendingBills = true;
	    }
	    rsCheckTableBusy.close();
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
	return flgPendingBills;
    }

//for day end
    public int funGetNextShiftNo(String posCode, int shiftNo)
    {
	int retvalue = 0;
	try
	{
	    String billDateSql = "select date(max(dtePOSDate)) from tbldayendprocess where strPOSCode='" + posCode + "'";
	    ResultSet rsDayEnd = clsGlobalVarClass.dbMysql.executeResultSet(billDateSql);
	    rsDayEnd.next();
	    String billDate = rsDayEnd.getString(1);
	    rsDayEnd.close();

	    retvalue = funShiftEndProcess("DayEnd", posCode, shiftNo, billDate);
	}
	catch (Exception e)
	{
	   funShowDBConnectionLostErrorMessage(e);
	     e.printStackTrace();
	}
	finally
	{
	    return retvalue;
	}
    }

//for shift end
    public int funGetNextShiftNoForShiftEnd(String posCode, int shiftNo)
    {
	int retvalue = 0;
	int shiftCount = 0;
	try
	{
	    String billDateSql = "select date(max(dtePOSDate)) from tbldayendprocess where strPOSCode='" + posCode + "'";
	    ResultSet rsDayEnd = clsGlobalVarClass.dbMysql.executeResultSet(billDateSql);
	    rsDayEnd.next();
	    String billDate = rsDayEnd.getString(1);
	    rsDayEnd.close();

	    String sql = "select count(intShiftCode) from tblshiftmaster where strPOSCode='" + posCode + "'";
	    ResultSet rsShiftNoCount = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    rsShiftNoCount.next();
	    shiftCount = rsShiftNoCount.getInt(1);
	    rsShiftNoCount.close();
	    if (shiftCount > 0)
	    {
		if (shiftNo == shiftCount)
		{
		    //clsGlobalVarClass.gShiftNo=1;
		    retvalue = funShiftEndProcess("DayEnd", posCode, shiftNo, billDate);
		}
		else
		{
		    retvalue = funShiftEndProcess("ShiftEnd", posCode, shiftNo, billDate);
		}
	    }
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
	finally
	{
	    return retvalue;
	}
    }

    public int funShiftEndProcess(String status, String posCode, int shiftNo, String billDate)
    {
	String newStartDate = "";
	int shiftEnd = 0;
	int retvalue = 1;
	try
	{

	    if (status.equalsIgnoreCase("DayEnd"))//for day end
	    {
		/**
		 * Transfer Card Balance To Debit Card Revenue Table.
		 *
		 */
		Date dt = new Date();
		String posDateTemp = billDate + " " + dt.getHours() + ":" + dt.getMinutes() + ":" + dt.getSeconds();

		if (clsGlobalVarClass.gLastPOSForDayEnd.equalsIgnoreCase(posCode))
		{
		    funShiftCardBalToRevenueTable(posCode, posDateTemp);
		}

		// Post Sales Data to CMS CL and RV Tables.  
		//commented for Poona Club
		if (clsGlobalVarClass.gCMSIntegrationYN)
		{
		    if (clsGlobalVarClass.gCMSPostingType.equals("Sanguine CMS"))
		    {
			if (funPostSanguineCMSData(posCode, billDate) == 0)
			{
			    return 0;
			}
		    }
		    else
		    {
			// Post Sales Data to CMS CL and RV Tables. 
			if (funPostBillDataToCMS(posCode, billDate) == 0)
			{
			    return 0;
			}
		    }
		}

		// Generate next POS Date / POS Shift Date   
		String sql = "select count(*) from tbldayendprocess where strPOSCode='" + posCode + "' and strDayEnd='N'";
		ResultSet rsDayEndRecord = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		rsDayEndRecord.next();
		if (rsDayEndRecord.getInt(1) > 0)
		{
		    String tempPOSDate = "";
		    rsDayEndRecord.close();
		    sql = "select date(max(dtePOSDate)) from tbldayendprocess "
			    + "where strPOSCode='" + posCode + "'";
		    rsDayEndRecord = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		    rsDayEndRecord.next();
		    tempPOSDate = rsDayEndRecord.getString(1);
		    Date startDate = rsDayEndRecord.getDate(1);
		    String shiftDate = rsDayEndRecord.getString(1);
		    if (status.equals("DayEnd"))
		    {
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(startDate);
			cal.add(Calendar.DATE, 1);
			newStartDate = (cal.getTime().getYear() + 1900) + "-" + (cal.getTime().getMonth() + 1) + "-" + (cal.getTime().getDate());
			rsDayEndRecord.close();
		    }
		    else
		    {
			newStartDate = shiftDate;
		    }

		    String dayEnd = "N";
		    int shift = 1;
		    //clsGlobalVarClass.dbMysql.funStartTransaction();
		    if (status.equals("DayEnd"))
		    {
			sql = "update tbldayendprocess set strDayEnd='Y',strShiftEnd='Y' "
				+ "where strPOSCode='" + posCode + "' and strDayEnd='N'";
			clsGlobalVarClass.dbMysql.execute(sql);
			dayEnd = "Y";
		    }
		    else
		    {
			sql = "update tbldayendprocess set strDayEnd='N',strShiftEnd='Y' "
				+ "where strPOSCode='" + posCode + "' and strDayEnd='N'";
			clsGlobalVarClass.dbMysql.execute(sql);
			shift = shiftNo;
		    }

		    if (clsGlobalVarClass.flgCarryForwardFloatAmtToNextDay)
		    {
			String reasonCode = "";
			sql = "select strReasonCode from tblreasonmaster where strCashMgmt='Y'";
			ResultSet rsReason = clsGlobalVarClass.dbMysql.executeResultSet(sql);
			if (rsReason.next())
			{
			    reasonCode = rsReason.getString(1);
			}
			rsReason.close();

			String transDateForCashMgmt = newStartDate.split(" ")[0];
			transDateForCashMgmt += " " + dt.getHours() + ":" + dt.getMinutes() + ":" + dt.getSeconds();
			clsCashManagement objCashMgmt = new clsCashManagement();
			Map<String, clsCashManagementDtl> hmCashMgmtDtl = objCashMgmt.funGetCashManagement(tempPOSDate, tempPOSDate, clsGlobalVarClass.gPOSCode);

			for (Map.Entry<String, clsCashManagementDtl> entry : hmCashMgmtDtl.entrySet())
			{
			    String transId = funGenerateNextCode();
			    clsCashManagementDtl objCashMgmtDtl = entry.getValue();
			    double balanceAmt = (objCashMgmtDtl.getSaleAmt() + objCashMgmtDtl.getAdvanceAmt() + objCashMgmtDtl.getFloatAmt() + objCashMgmtDtl.getTransferInAmt()) - (objCashMgmtDtl.getWithdrawlAmt() + objCashMgmtDtl.getPaymentAmt() + objCashMgmtDtl.getRefundAmt() + objCashMgmtDtl.getTransferOutAmt());
			    if (null != entry.getValue().getHmPostRollingSalesAmt())
			    {
				for (Map.Entry<String, Double> entryPostRollingSales : entry.getValue().getHmPostRollingSalesAmt().entrySet())
				{
				    balanceAmt += entryPostRollingSales.getValue();
				}
			    }

			    if (balanceAmt > 0)
			    {
				sql = "insert into tblcashmanagement(strTransID,strTransType,dteTransDate,strReasonCode,strPOSCode"
					+ ",dblAmount,strRemarks,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strCurrencyType"
					+ ",intShiftCode,strAgainst,dblRollingAmt,strClientCode,strDataPostFlag) "
					+ "values ('" + transId + "','Float','" + transDateForCashMgmt + "','" + reasonCode + "'"
					+ ",'" + clsGlobalVarClass.gPOSCode + "','" + balanceAmt + "','Carryforward Float Amt'"
					+ ",'" + entry.getKey() + "','" + entry.getKey() + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
					+ ",'" + clsGlobalVarClass.getCurrentDateTime() + "','Cash','" + clsGlobalVarClass.gShiftNo + "'"
					+ ",'Direct','0','" + clsGlobalVarClass.gClientCode + "','N')";
				clsGlobalVarClass.dbMysql.execute(sql);
			    }
			}
		    }

		    sql = "insert into tbldayendprocess(strPOSCode,dtePOSDate,strDayEnd,intShiftCode,strShiftEnd"
			    + ",strUserCreated,dteDateCreated,dteDayEndDateTime,strClientCode) "
			    + "values('" + posCode + "','" + newStartDate + "','N'," + shift + ",''"
			    + ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','"+clsGlobalVarClass.gClientCode+"')";
		    clsGlobalVarClass.dbMysql.execute(sql);
		    clsGlobalVarClass.gShiftEnd = "";
		    clsGlobalVarClass.gDayEnd = "N";
		    clsGlobalVarClass.setStartDate(newStartDate);
		    clsGlobalVarClass.funSetPOSDate();
		    System.out.println("Shift = " + clsGlobalVarClass.gShifts);

		    if (status.equals("ShiftEnd"))
		    {
			//shiftEnd=shiftNo-1;
			shiftEnd = shiftNo;
		    }
		    else
		    {
			shiftEnd = shiftNo;
		    }

		    //  Calculate Total Cash Amt, Total Advance Amt, Total Receipts , Total Payments, Ttoal Discount Amt
		    //  , No of Discounted Bills, No of Total bills.
		    funCalculateDayEndCash(shiftDate, shiftEnd, posCode);

		    // Update tbldayendprocess table fields     
		    funUpdateDayEndFields(shiftDate, shiftEnd, dayEnd, posCode);
		    String posDate = billDate;

		    // Post POS Item Sale Data to MMS.
		    if (clsGlobalVarClass.gEffectOfSales.equalsIgnoreCase("POS"))
		    {
			String WSStockAdjustmentCode = clsGlobalVarClass.funPostPOSItemSalesData(posCode, posDate, posDate, "Live");
		    }
		    else if (clsGlobalVarClass.gPostSalesDataToMMS)
		    {
			Calendar cal = Calendar.getInstance();
			cal.setTime(startDate);
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			String firstDate = format1.format(cal.getTime());
			sql = "select a.dtePOSDate from tbldayendprocess a where  a.strWSStockAdjustmentNo = '' " +
				" and a.dtePOSDate between '"+firstDate+"' and '"+posDate+"' and a.strPOSCode='"+posCode+"'";
			ResultSet rsDate = clsGlobalVarClass.dbMysql.executeResultSet(sql);
			if(rsDate.next()){    
			    while (rsDate.next())
			    {
				//POst All pending Data from first date of month to till
				boolean isPosted = clsGlobalVarClass.funPostItemSalesData(posCode, rsDate.getString(1), rsDate.getString(1));    
				String exbillGenCode = clsGlobalVarClass.funPostItemSalesDataExcise(posCode, rsDate.getString(1), rsDate.getString(1));
			    }
			}
			else{
			    boolean isPosted = clsGlobalVarClass.funPostItemSalesData(posCode, posDate, posDate);    
			    String exbillGenCode = clsGlobalVarClass.funPostItemSalesDataExcise(posCode, posDate, posDate);	
			}
			
		    }

		    // Transfer Billing Data from Live Tables To QFile Tables.
		    funInsertQBillData(posCode);

		    //clear non avail items for today
		    String deleteNONAvailItemsDataSql = "delete from tblnonavailableitems where strPOSCode='" + posCode + "';";
		    clsGlobalVarClass.dbMysql.execute(deleteNONAvailItemsDataSql);

		    //reset last order no
		    clsGlobalVarClass.dbMysql.execute("update tblinternal set dblLastNo=0 where strTransactionType='OrderNo' ");

		    // Post Sales Transaction Data, Inventory Transaction Data, Audit Transaction Data, Customer Masterok
		    // and Customer Area Master to HO.
		    if (clsGlobalVarClass.gConnectionActive.equals("Y") && clsGlobalVarClass.gDataSendFrequency.equalsIgnoreCase("After Day End"))
		    {
			clsGlobalVarClass.funInvokeHOWebserviceForTrans("All", "Day End");
			clsGlobalVarClass.funPostCustomerDataToHOPOS();
			clsGlobalVarClass.funPostCustomerAreaDataToHOPOS();
		    }

		    // Post Day End Table Data to HO.    
		    clsGlobalVarClass.funPostDayEndData(newStartDate, shift);

		    if (clsGlobalVarClass.gTransactionType != null && clsGlobalVarClass.gTransactionType.equalsIgnoreCase("ShiftEnd") ||clsGlobalVarClass.gTransactionType.equalsIgnoreCase("ShiftEndWithoutDetails") )
		    {
			retvalue = funDayEndflash(posCode, billDate, shiftNo);
		    }

		    if (clsGlobalVarClass.gEnableBillSeries)
		    {
			if (clsGlobalVarClass.gNewBillSeriesForNewDay)
			{
			    clsGlobalVarClass.dbMysql.execute("update tblbillseries "
				    + "set intLastNo=0 "
				    + "where (strPOSCode='" + clsGlobalVarClass.gPOSCode + "' or strPOSCode='All' ) "
				    + "and strClientCode='" + clsGlobalVarClass.gClientCode + "' ");
			}
		    }
		    else
		    {
			if (clsGlobalVarClass.gNewBillSeriesForNewDay)
			{
			    clsGlobalVarClass.dbMysql.execute("update tbllaststoreadvbookingbill set strAdvBookingNo=0  "
				    + "where strPOSCode='" + clsGlobalVarClass.gPOSCode + "' ;");

			    clsGlobalVarClass.dbMysql.execute("update tblstorelastbill set strBillNo=0  "
				    + "where strPOSCode='" + clsGlobalVarClass.gPOSCode + "' ;");
			}
		    }

		    //send dayend sms                    
		    sql = "select a.strSendSMSYN,a.longMobileNo "
			    + "from tblsmssetup a "
			    + "where (a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPOSCode='All') "
			    + "and a.strClientCode='" + clsGlobalVarClass.gClientCode + "' "
			    + "and a.strTransactionName='DayEnd' "
			    + "and a.strSendSMSYN='Y'; ";
		    ResultSet rsSendSMS = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		    if (rsSendSMS.next())
		    {
			String mobileNo = rsSendSMS.getString(2);//mobileNo

			funSendDayEndSMS(mobileNo, posCode, billDate, shiftNo);

		    }
		    rsSendSMS.close();
		}
	    }
	    else //for shift end
	    {
		// Transfer Card Balance To Debit Card Revenue Table.
		if (clsGlobalVarClass.gLastPOSForDayEnd.equals(posCode))
		{
		    Date dt = new Date();
		    String posDateTemp = billDate + " " + dt.getHours() + ":" + dt.getMinutes() + ":" + dt.getSeconds();
		    funShiftCardBalToRevenueTable(posCode, posDateTemp);
		}
		// Post Sales Data to CMS CL and RV Tables.    
		if (clsGlobalVarClass.gCMSIntegrationYN)
		{
		    if (clsGlobalVarClass.gCMSPostingType.equals("Sanguine CMS"))
		    {
			if (funPostSanguineCMSData(posCode, billDate) == 0)
			{
			    return 0;
			}
		    }
		    else
		    {
			// Post Sales Data to CMS CL and RV Tables. 
			if (funPostBillDataToCMS(posCode, billDate) == 0)
			{
			    return 0;
			}
		    }
		}

		// Generate next POS Date / POS Shift Date   
		String sql = "select count(*) from tbldayendprocess where strPOSCode='" + posCode + "' and strDayEnd='N'";
		ResultSet rsDayEndRecord = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		rsDayEndRecord.next();
		if (rsDayEndRecord.getInt(1) > 0)
		{
		    rsDayEndRecord.close();
		    sql = "select date(max(dtePOSDate)) from tbldayendprocess "
			    + "where strPOSCode='" + posCode + "'";
		    rsDayEndRecord = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		    rsDayEndRecord.next();
		    Date startDate = rsDayEndRecord.getDate(1);
		    String shiftDate = rsDayEndRecord.getString(1);
		    if (status.equals("DayEnd"))
		    {
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(startDate);
			cal.add(Calendar.DATE, 1);
			newStartDate = (cal.getTime().getYear() + 1900) + "-" + (cal.getTime().getMonth() + 1) + "-" + (cal.getTime().getDate());
			rsDayEndRecord.close();
		    }
		    else
		    {
			newStartDate = shiftDate;
		    }

		    String dayEnd = "N";
		    int shift = 0;
		    //clsGlobalVarClass.dbMysql.funStartTransaction();
		    if (status.equals("DayEnd"))
		    {
			sql = "update tbldayendprocess set strDayEnd='Y',strShiftEnd='Y' "
				+ "where strPOSCode='" + posCode + "' and strDayEnd='N'";
			clsGlobalVarClass.dbMysql.execute(sql);
			dayEnd = "Y";
		    }
		    else
		    {
			sql = "update tbldayendprocess set strDayEnd='N',strShiftEnd='Y' "
				+ "where strPOSCode='" + posCode + "' and strDayEnd='N'";
			clsGlobalVarClass.dbMysql.execute(sql);
			shift = shiftNo;
		    }
		    sql = "insert into tbldayendprocess(strPOSCode,dtePOSDate,strDayEnd,intShiftCode,strShiftEnd"
			    + ",strUserCreated,dteDateCreated,strClientCode) "
			    + "values('" + posCode + "','" + newStartDate + "','N'," + (shift + 1)
			    + ",'','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','"+clsGlobalVarClass.gClientCode+"')";
		    clsGlobalVarClass.dbMysql.execute(sql);
		    clsGlobalVarClass.gShiftEnd = "";
		    clsGlobalVarClass.gDayEnd = "N";
		    //clsGlobalVarClass.gShiftNo = (shift + 1);
		    clsGlobalVarClass.setStartDate(newStartDate);
		    clsGlobalVarClass.funSetPOSDate();
		    System.out.println("Shift = " + clsGlobalVarClass.gShifts);

		    if (status.equals("ShiftEnd"))
		    {
			//shiftEnd=shiftNo-1;
			shiftEnd = shiftNo;
		    }
		    else
		    {
			shiftEnd = shiftNo;
		    }

		    //  Calculate Total Cash Amt, Total Advance Amt, Total Receipts , Total Payments, Ttoal Discount Amt
		    //  , No of Discounted Bills, No of Total bills.
		    funCalculateDayEndCash(shiftDate, shiftEnd, posCode);

		    // Update tbldayendprocess table fields     
		    funUpdateDayEndFields(shiftDate, shiftEnd, dayEnd, posCode);
		    String posDate = billDate;

		    // Post POS Item Sale Data to MMS.
		    if (clsGlobalVarClass.gPostSalesDataToMMS)
		    {
			boolean isPosted = clsGlobalVarClass.funPostItemSalesData(posCode, posDate, posDate);
//                        sql = "update tbldayendprocess set strWSStockAdjustmentNo='" + WSStockAdjustmentCode + "'" + " where date(dtePOSDate)='" + posDate + "' and strPOSCode='" + posCode + "' and intShiftCode=" + shiftNo;
//                        clsGlobalVarClass.dbMysql.execute(sql);
			String exbillGenCode = clsGlobalVarClass.funPostItemSalesDataExcise(posCode, posDate, posDate);
		    }

		    // Transfer Billing Data from Live Tables To QFile Tables.
		    if (clsGlobalVarClass.gEnableShiftYN && clsGlobalVarClass.gLockDataOnShiftYN)
		    {
			funInsertQBillData(posCode);
		    }

		    // Post Sales Transaction Data, Inventory Transaction Data, Audit Transaction Data, Customer Masterok
		    // and Customer Area Master to HO.
		    if (clsGlobalVarClass.gConnectionActive.equals("Y"))
		    {
			clsGlobalVarClass.funInvokeHOWebserviceForTrans("All", "Day End");
			clsGlobalVarClass.funPostCustomerDataToHOPOS();
			clsGlobalVarClass.funPostCustomerAreaDataToHOPOS();
		    }

		    // Post Day End Table Data to HO.    
		    clsGlobalVarClass.funPostDayEndData(newStartDate, shift);

		    if (clsGlobalVarClass.gTransactionType.equalsIgnoreCase("ShiftEnd"))
		    {
			retvalue = funDayEndflash(posCode, billDate, shiftNo);
		    }

		    //send dayend sms                    
		    sql = "select a.strSendSMSYN,a.longMobileNo "
			    + "from tblsmssetup a "
			    + "where (a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPOSCode='All') "
			    + "and a.strClientCode='" + clsGlobalVarClass.gClientCode + "' "
			    + "and a.strTransactionName='DayEnd' "
			    + "and a.strSendSMSYN='Y'; ";
		    ResultSet rsSendSMS = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		    if (rsSendSMS.next())
		    {
			String mobileNo = rsSendSMS.getString(2);//mobileNo

			funSendDayEndSMS(mobileNo, posCode, billDate, shiftNo);

		    }
		    rsSendSMS.close();
		}
	    }
	}
	catch (Exception ex)
	{
	    //clsGlobalVarClass.dbMysql.funRollbackTransaction();
	    funShowDBConnectionLostErrorMessage(ex);
	    ex.printStackTrace();
	}
	finally
	{
	    return retvalue;
	}
    }

    // Function to calculate total settlement amount and assigns global variables, which are shown on day end/shift end form.
// This function calculate settlement amount from live tables.    
    public int funCalculateDayEndCash(String posDate, int shiftCode, String posCode)
    {
	double sales = 0.00, totalDiscount = 0.00, totalSales = 0.00, noOfDiscountedBills = 0.00;
	double advCash = 0.00, cashIn = 0.00, cashOut = 0.00, totalFloat = 0.00;
	try
	{
	    String sql = "SELECT c.strSettelmentDesc,sum(ifnull(b.dblSettlementAmt,0)),sum(a.dblDiscountAmt),c.strSettelmentType"
		    + " FROM tblbillhd a,tblbillsettlementdtl b,tblsettelmenthd c "
		    + " Where a.strBillNo = b.strBillNo and b.strSettlementCode = c.strSettelmentCode "
		    + " and date(a.dteBillDate ) ='" + posDate + "' and a.strPOSCode='" + posCode + "'"
		    + " and a.intShiftCode=" + shiftCode
		    + " GROUP BY c.strSettelmentDesc,a.strPosCode";
	    //System.out.println(sql);
	    ResultSet rsSettlementAmt = clsGlobalVarClass.dbMysql.executeResultSet(sql);

	    while (rsSettlementAmt.next())
	    {
		//records[1]=rsSettlementAmt.getString(2);
		if (rsSettlementAmt.getString(4).equalsIgnoreCase("Cash"))
		{
		    sales = sales + (Double.parseDouble(rsSettlementAmt.getString(2).toString()));
		}
		totalSales = totalSales + (Double.parseDouble(rsSettlementAmt.getString(2).toString()));
	    }

	    rsSettlementAmt.close();

	    sql = "SELECT count(strBillNo),sum(dblDiscountAmt) FROM tblbillhd "
		    + "Where date(dteBillDate ) ='" + posDate + "' and strPOSCode='" + posCode + "' "
		    + "and dblDiscountAmt > 0.00 and intShiftCode=" + shiftCode
		    + " GROUP BY strPosCode";
	    ResultSet rsTotalDiscountBills = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsTotalDiscountBills.next())
	    {
		gNoOfDiscountedBills = rsTotalDiscountBills.getInt(1);
		totalDiscount = totalDiscount + (Double.parseDouble(rsTotalDiscountBills.getString(2).toString()));

		gTotalDiscounts = totalDiscount;
	    }
	    rsTotalDiscountBills.close();

	    sql = "select count(strBillNo) from tblbillhd where date(dteBillDate ) ='" + posDate + "' and "
		    + "strPOSCode='" + posCode + "' and intShiftCode=" + shiftCode + " "
		    + " GROUP BY strPosCode";
	    ResultSet rsTotalBills = clsGlobalVarClass.dbMysql.executeResultSet(sql);

	    if (rsTotalBills.next())
	    {
		gTotalBills = rsTotalBills.getInt(1);
	    }
	    rsTotalBills.close();

	    gTotalCashSales = sales;
	    sql = "select count(dblAdvDeposite) from tbladvancereceipthd "
		    + "where dtReceiptDate='" + posDate + "' and intShiftCode=" + shiftCode;
	    ResultSet rsTotalAdvance = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    rsTotalAdvance.next();
	    int cntAdvDeposite = rsTotalAdvance.getInt(1);
	    if (cntAdvDeposite > 0)
	    {
		//sql="select sum(dblAdvDeposite) from tbladvancereceipthd where dtReceiptDate='"+posDate+"'";
		sql = "select sum(b.dblAdvDepositesettleAmt) from tbladvancereceipthd a,tbladvancereceiptdtl b,tblsettelmenthd c "
			+ "where date(a.dtReceiptDate)='" + posDate + "' and a.strPOSCode='" + posCode + "' "
			+ "and c.strSettelmentCode=b.strSettlementCode and a.strReceiptNo=b.strReceiptNo "
			+ "and c.strSettelmentType='Cash' and a.intShiftCode=" + shiftCode;
		rsTotalAdvance = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		rsTotalAdvance.next();
		advCash = Double.parseDouble(rsTotalAdvance.getString(1));
		gTotalAdvanceAmt = advCash;
	    }
	    rsTotalAdvance.close();

	    //sql="select strTransType,sum(dblAmount) from tblcashmanagement where dteTransDate='"+posDate+"'"
	    //    + " and strPOSCode='"+globalVarClass.gPOSCode+"' group by strTransType";
	    sql = "select strTransType,sum(dblAmount),strCurrencyType from tblcashmanagement "
		    + "where date(dteTransDate)='" + posDate + "' and strPOSCode='" + posCode + "' "
		    + "and intShiftCode=" + shiftCode + " group by strTransType,strCurrencyType";
	    ResultSet rsCashTransaction = clsGlobalVarClass.dbMysql.executeResultSet(sql);

	    while (rsCashTransaction.next())
	    {
		if (rsCashTransaction.getString(1).equals("Float"))
		{
		    cashIn = cashIn + (Double.parseDouble(rsCashTransaction.getString(2).toString()));
		}
		if (rsCashTransaction.getString(1).equals("Transfer In"))
		{
		    cashIn = cashIn + (Double.parseDouble(rsCashTransaction.getString(2).toString()));
		}

		if (rsCashTransaction.getString(1).equals("Withdrawal"))
		{
		    cashOut = cashOut + (Double.parseDouble(rsCashTransaction.getString(2).toString()));
		}
		if (rsCashTransaction.getString(1).equals("Transfer Out"))
		{
		    cashOut = cashOut + (Double.parseDouble(rsCashTransaction.getString(2).toString()));
		}
		if (rsCashTransaction.getString(1).equals("Payments"))
		{
		    cashOut = cashOut + (Double.parseDouble(rsCashTransaction.getString(2).toString()));
		}
		if (rsCashTransaction.getString(1).equals("Refund"))
		{
		    cashOut = cashOut + (Double.parseDouble(rsCashTransaction.getString(2).toString()));
		}
	    }
	    cashIn = cashIn + advCash + sales;
	    gTotalReceipt = cashIn;
	    gTotalPayments = cashOut;
	    double inHandCash = (cashIn) - cashOut;
	    gTotalCashInHand = inHandCash;
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
	return 1;
    }

// Function to calculate total settlement amount and assigns global variables, which are shown on day end/shift end form.
// This function calculate settlement amount from Q File tables.
    public int funCalculateDayEndCashForQFile(String posDate, int shiftCode)
    {
	double sales = 0.00, totalDiscount = 0.00, totalSales = 0.00, noOfDiscountedBills = 0.00;
	double advCash = 0.00, cashIn = 0.00, cashOut = 0.00;
	try
	{
	    String sql = "SELECT c.strSettelmentDesc,sum(b.dblSettlementAmt),sum(a.dblDiscountAmt),c.strSettelmentType"
		    + " FROM tblqbillhd a,tblqbillsettlementdtl b,tblsettelmenthd c "
		    + "Where a.strBillNo = b.strBillNo "
		    + " AND DATE(a.dteBillDate)=DATE(b.dteBillDate) "
		    + " and b.strSettlementCode = c.strSettelmentCode "
		    + " and date(a.dteBillDate ) ='" + posDate + "' and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "'"
		    + " and a.intShiftCode=" + shiftCode
		    + " GROUP BY c.strSettelmentDesc,a.strPosCode";
	    //System.out.println(sql);
	    ResultSet rsSettlementAmt = clsGlobalVarClass.dbMysql.executeResultSet(sql);

	    while (rsSettlementAmt.next())
	    {
		//records[1]=rsSettlementAmt.getString(2);
		if (rsSettlementAmt.getString(4).equalsIgnoreCase("Cash"))
		{
		    sales = sales + (Double.parseDouble(rsSettlementAmt.getString(2).toString()));
		}

		totalSales = totalSales + (Double.parseDouble(rsSettlementAmt.getString(2).toString()));
	    }

	    rsSettlementAmt.close();

	    sql = "SELECT count(strBillNo),sum(dblDiscountAmt) FROM tblqbillhd "
		    + "Where date(dteBillDate ) ='" + posDate + "' and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + "and dblDiscountAmt > 0.00 and intShiftCode=" + shiftCode
		    + " GROUP BY strPosCode";
	    ResultSet rsTotalDiscountBills = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsTotalDiscountBills.next())
	    {
		gNoOfDiscountedBills = rsTotalDiscountBills.getInt(1);

		totalDiscount = totalDiscount + (Double.parseDouble(rsTotalDiscountBills.getString(2).toString()));
		gTotalDiscounts = totalDiscount;

	    }
	    rsTotalDiscountBills.close();

	    sql = "select count(strBillNo) from tblqbillhd where date(dteBillDate ) ='" + posDate + "' and "
		    + "strPOSCode='" + clsGlobalVarClass.gPOSCode + " and intShiftCode=" + shiftCode + "' "
		    + "GROUP BY strPosCode";
	    ResultSet rsTotalBills = clsGlobalVarClass.dbMysql.executeResultSet(sql);

	    if (rsTotalBills.next())
	    {
		gTotalBills = rsTotalBills.getInt(1);
	    }
	    rsTotalBills.close();

	    gTotalCashSales = sales;
	    sql = "select count(dblAdvDeposite) from tbladvancereceipthd "
		    + "where dtReceiptDate='" + posDate + "' and intShiftCode=" + shiftCode;
	    ResultSet rsTotalAdvance = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    rsTotalAdvance.next();
	    int cntAdvDeposite = rsTotalAdvance.getInt(1);
	    if (cntAdvDeposite > 0)
	    {
		//sql="select sum(dblAdvDeposite) from tbladvancereceipthd where dtReceiptDate='"+posDate+"'";
		sql = "select sum(b.dblAdvDepositesettleAmt) "
			+ "from tbladvancereceipthd a,tbladvancereceiptdtl b,tblsettelmenthd c "
			+ "where date(a.dtReceiptDate)='" + posDate + "' and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
			+ "and c.strSettelmentCode=b.strSettlementCode and a.strReceiptNo=b.strReceiptNo "
			+ "and c.strSettelmentType='Cash' and a.intShiftCode=" + shiftCode;
		rsTotalAdvance = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		rsTotalAdvance.next();
		advCash = Double.parseDouble(rsTotalAdvance.getString(1));
		gTotalAdvanceAmt = advCash;
	    }
	    rsTotalAdvance.close();

	    //sql="select strTransType,sum(dblAmount) from tblcashmanagement where dteTransDate='"+posDate+"'"
	    //    + " and strPOSCode='"+globalVarClass.gPOSCode+"' group by strTransType";
	    sql = "select strTransType,sum(dblAmount),strCurrencyType from tblcashmanagement "
		    + "where dteTransDate='" + posDate + "' and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + "and intShiftCode=" + shiftCode + " group by strTransType,strCurrencyType";
	    ResultSet rsCashTransaction = clsGlobalVarClass.dbMysql.executeResultSet(sql);

	    while (rsCashTransaction.next())
	    {
		if (rsCashTransaction.getString(1).equals("Float") || rsCashTransaction.getString(1).equals("Transfer In"))
		{
		    cashIn = cashIn + (Double.parseDouble(rsCashTransaction.getString(2).toString()));
		}
		if (rsCashTransaction.getString(1).equals("Withdrawl") || rsCashTransaction.getString(1).equals("Transfer Out") || rsCashTransaction.getString(1).equals("Payments"))
		{
		    cashOut = cashOut + (Double.parseDouble(rsCashTransaction.getString(2).toString()));
		}
	    }
	    cashIn = cashIn + advCash + sales;
	    gTotalReceipt = cashIn;
	    gTotalPayments = cashOut;
	    double inHandCash = (cashIn) - cashOut;
	    gTotalCashInHand = inHandCash;
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
	return 1;
    }

// Function to update values in tbldayendprocess table.
// This function updates values from Live tables.    
    public int funUpdateDayEndFields(String posDate, int shiftNo, String dayEnd, String posCode)
    {
	try
	{
	    String sql = "update tbldayendprocess set dblTotalSale = IFNULL((select sum(b.dblSettlementAmt) "
		    + "TotalSale from tblbillhd a,tblbillsettlementdtl b "
		    + "where a.strBillNo=b.strBillNo and date(a.dteBillDate) = '" + posDate + "' and "
		    + "a.strPOSCode = '" + posCode + "' and a.intShiftCode=" + shiftNo + "),0)"
		    + " where date(dtePOSDate)='" + posDate + "' and strPOSCode = '" + posCode + "'"
		    + " and intShiftCode=" + shiftNo;
	    //System.out.println("UpdateDayEndQuery_1=="+sql);
	    clsGlobalVarClass.dbMysql.execute(sql);
	    sql = "update tbldayendprocess set dteDayEndDateTime='" + clsGlobalVarClass.getCurrentDateTime() + "'"
		    + " where date(dtePOSDate)='" + posDate + "' and strPOSCode='" + posCode + "' "
		    + "and intShiftCode=" + shiftNo;
	    //System.out.println("UpdateDayEndQuery_2=="+sql);

	    clsGlobalVarClass.dbMysql.execute(sql);
	    sql = "update tbldayendprocess set strUserEdited='" + clsGlobalVarClass.gUserCode + "'"
		    + " where date(dtePOSDate)='" + posDate + "' and strPOSCode='" + posCode + "' and intShiftCode=" + shiftNo;
	    //System.out.println("UpdateDayEndQuery_3=="+sql);

	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set dblNoOfBill = IFNULL((select count(*) NoOfBills "
		    + "from tblbillhd where Date(dteBillDate) = '" + posDate + "' and "
		    + "strPOSCode = '" + posCode + "' and intShiftCode=" + shiftNo + "),0)"
		    + " where date(dtePOSDate)='" + posDate + "' and strPOSCode = '" + posCode + "' "
		    + " and intShiftCode=" + shiftNo;
	    //System.out.println("UpdateDayEndQuery_4=="+sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set dblNoOfVoidedBill = IFNULL((select count(DISTINCT strBillNo) "
		    + "NoOfVoidBills from tblvoidbillhd where date(dteModifyVoidBill) = " + "'" + posDate + "'"
		    + " and strPOSCode = '" + posCode + "' and strTransType = 'VB'"
		    + " and intShiftCode=" + shiftNo + "),0)"
		    + " where date(dtePOSDate)='" + posDate + "' and strPOSCode = '" + posCode + "'"
		    + " and intShiftCode=" + shiftNo;
	    //System.out.println("UpdateDayEndQuery_5=="+sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set dblNoOfModifyBill = IFNULL((select count(DISTINCT b.strBillNo) "
		    + "NoOfModifiedBills from tblbillhd a,tblvoidbillhd b where a.strBillNo=b.strBillNo"
		    + " and Date(b.dteModifyVoidBill) = '" + posDate + "' and b.strPOSCode='" + posCode + "'"
		    + " and b.strTransType = 'MB' and a.intShiftCode=" + shiftNo + "),0)"
		    + " where date(dtePOSDate)='" + posDate + "' and strPOSCode = '" + posCode + "'"
		    + " and intShiftCode=" + shiftNo;
	    //System.out.println("UpdateDayEndQuery_6=="+sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set dblHDAmt=IFNULL((select sum(a.dblGrandTotal) HD from tblbillhd a,"
		    + "tblhomedelivery b where a.strBillNo=b.strBillNo and date(a.dteBillDate) = '" + posDate + "' and "
		    + "a.strPOSCode = '" + posCode + "' and a.intShiftCode=" + shiftNo + "), 0) "
		    + "where date(dtePOSDate)='" + posDate + "' and strPOSCode = '" + posCode + "'"
		    + " and intShiftCode=" + shiftNo;
	    //System.out.println("UpdateDayEndQuery_7=="+sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set dblDiningAmt=IFNULL(( select sum(dblGrandTotal) Dining"
		    + " from tblbillhd where strTakeAway='No' and date(dteBillDate) = '" + posDate + "' and strPOSCode = '" + posCode + "'"
		    + "  and strBillNo NOT IN (select strBillNo from tblhomedelivery where strBillNo is not NULL) and intShiftCode=" + clsGlobalVarClass.gShiftNo + "),0)"
		    + "  where date(dtePOSDate)='" + posDate + "' and strPOSCode = '" + posCode + "' "
		    + "and intShiftCode=" + shiftNo;
	    clsGlobalVarClass.dbMysql.execute(sql);
	    //System.out.println("UpdateDayEndQuery_8==" + sql);

	    sql = "update tbldayendprocess set dblTakeAway=IFNULL((select sum(dblGrandTotal) TakeAway from tblbillhd"
		    + " where strTakeAway='Yes' and date(dteBillDate) = '" + posDate + "' and strPOSCode = '" + posCode + "'"
		    + " and intShiftCode=" + shiftNo + "),0)"
		    + " where date(dtePOSDate)='" + posDate + "' and strPOSCode='" + posCode + "' and intShiftCode=" + shiftNo;

	    //System.out.println("UpdateDayEndQuery_9=="+sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set dblFloat=IFNULL((select sum(dblAmount) TotalFloats from tblcashmanagement "
		    + "where strTransType='Float' and date(dteTransDate) = '" + posDate + "' and strPOSCode = '" + posCode + "'"
		    + " and intShiftCode=" + shiftNo + ""
		    + " group by strTransType),0) "
		    + "where date(dtePOSDate)='" + posDate + "' and strPOSCode='" + posCode + "' and intShiftCode=" + shiftNo;
	    //System.out.println("UpdateDayEndQuery_10=="+sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set dblTransferIn=IFNULL((select sum(dblAmount) TotalTransferIn from tblcashmanagement "
		    + "where strTransType='Transfer In' and date(dteTransDate) = '" + posDate + "'"
		    + " and strPOSCode = '" + posCode + "' and intShiftCode=" + shiftNo
		    + " group by strTransType),0) "
		    + "where date(dtePOSDate)='" + posDate + "' and strPOSCode='" + posCode + "' and intShiftCode=" + shiftNo;
	    //System.out.println("UpdateDayEndQuery_11=="+sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set dblTransferOut=IFNULL((select sum(dblAmount) TotalTransferOut from tblcashmanagement "
		    + "where strTransType='Transfer Out' and date(dteTransDate) = '" + posDate + "'"
		    + " and strPOSCode = '" + posCode + "' and intShiftCode=" + shiftNo + ""
		    + " group by strTransType),0) "
		    + "where date(dtePOSDate)='" + posDate + "' and strPOSCode='" + posCode + "' and intShiftCode=" + shiftNo;
	    //System.out.println("UpdateDayEndQuery_12=="+sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set dblWithdrawal=IFNULL(( select sum(dblAmount) TotalWithdrawals from tblcashmanagement "
		    + "where strTransType='Withdrawal' and date(dteTransDate) = '" + posDate + "' "
		    + "and strPOSCode = '" + clsGlobalVarClass.gPOSCode + "' and intShiftCode=" + shiftNo + ""
		    + " group by strTransType),0) "
		    + "where date(dtePOSDate)='" + posDate + "' and strPOSCode='" + posCode + "' and intShiftCode=" + shiftNo;
	    //System.out.println("UpdateDayEndQuery_13=="+sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set dblRefund=IFNULL(( select sum(dblAmount) TotalRefunds from tblcashmanagement "
		    + " where strTransType='Refund' and date(dteTransDate) = '" + posDate + "' and strPOSCode = '" + posCode + "'"
		    + " and intShiftCode=" + shiftNo + " group by strTransType),0)"
		    + " where date(dtePOSDate)='" + posDate + "' and strPOSCode='" + posCode + "' and intShiftCode=" + shiftNo;
	    //System.out.println("UpdateDayEndQuery_14=="+sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set dblPayments=IFNULL(( select sum(dblAmount) TotalPayments from tblcashmanagement "
		    + "where strTransType='Payments' and date(dteTransDate) = '" + posDate + "'"
		    + " and strPOSCode = '" + posCode + "' and intShiftCode=" + shiftNo + ""
		    + " group by strTransType),0) "
		    + " where date(dtePOSDate)='" + posDate + "' and strPOSCode='" + posCode + "' and intShiftCode=" + shiftNo;
	    //System.out.println("UpdateDayEndQuery_15=="+sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set dblAdvance=IFNULL((select sum(b.dblAdvDepositesettleAmt) "
		    + "from tbladvancereceipthd a,tbladvancereceiptdtl b,tblsettelmenthd c "
		    + "where date(a.dtReceiptDate)='" + posDate + "' and a.strPOSCode='" + posCode + "' "
		    + "and c.strSettelmentCode=b.strSettlementCode and a.strReceiptNo=b.strReceiptNo "
		    + "and c.strSettelmentType='Cash' and intShiftCode=" + shiftNo + "),0)"
		    + "where date(dtePOSDate)='" + posDate + "' and strPOSCode='" + posCode + "' and intShiftCode=" + shiftNo;
	    //System.out.println("UpdateDayEndQuery_16=="+sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set dblTotalReceipt=" + gTotalReceipt
		    + " where date(dtePOSDate)='" + posDate + "' and strPOSCode='" + posCode + "' and intShiftCode=" + shiftNo;
	    //System.out.println("UpdateDayEndQuery_17=="+sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set dblTotalPay=" + gTotalPayments
		    + " where date(dtePOSDate)='" + posDate + "' and strPOSCode='" + posCode + "' and intShiftCode=" + shiftNo;
	    //System.out.println("UpdateDayEndQuery_18=="+sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set dblCashInHand=" + gTotalCashInHand
		    + " where date(dtePOSDate)='" + posDate + "' and strPOSCode='" + posCode + "' and intShiftCode=" + shiftNo;
	    //System.out.println("UpdateDayEndQuery_19=="+sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set dblCash=" + gTotalCashSales
		    + " where date(dtePOSDate)='" + posDate + "' and strPOSCode='" + posCode + "' and intShiftCode=" + shiftNo;
	    //System.out.println(sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set dblTotalDiscount=" + gTotalDiscounts
		    + " where date(dtePOSDate)='" + posDate + "' and strPOSCode='" + posCode + "' and intShiftCode=" + shiftNo;
	    //System.out.println("UpdateDayEndQuery_21=="+sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set dblNoOfDiscountedBill=" + gNoOfDiscountedBills
		    + " where date(dtePOSDate)='" + posDate + "' and strPOSCode='" + posCode + "' and intShiftCode=" + shiftNo;
	    //System.out.println("UpdateDayEndQuery_22=="+sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set intTotalPax=IFNULL((select sum(intBillSeriesPaxNo)"
		    + " from tblbillhd where date(dteBillDate ) ='" + posDate + "' and intShiftCode=" + shiftNo + ""
		    + " and strPOSCode='" + posCode + "'),0)"
		    + " where date(dtePOSDate)='" + posDate + "' "
		    + "and strPOSCode='" + posCode + "' and intShiftCode=" + shiftNo;
	    //System.out.println("UpdateDayEndQuery_23=="+sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set intNoOfTakeAway=(select count(strTakeAway)"
		    + "from tblbillhd where date(dteBillDate )='" + posDate + "' and intShiftCode=" + shiftNo + ""
		    + " and strPOSCode='" + posCode + "' and strTakeAway='Yes')"
		    + "where date(dtePOSDate)='" + posDate + "' and strPOSCode='" + posCode + "' and intShiftCode=" + shiftNo;
	    //System.out.println("update int takeawy==" + sql);
	    clsGlobalVarClass.dbMysql.execute(sql);
	    sql = "update tbldayendprocess set intNoOfHomeDelivery=(select COUNT(strBillNo)from tblhomedelivery where date(dteDate)='" + posDate + "' and strPOSCode='" + posCode + "' )"
		    + "where date(dtePOSDate)='" + posDate + "' and strPOSCode='" + posCode + "' and intShiftCode=" + shiftNo;
	    //System.out.println("update int homedelivry:==" + sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    // Update Day End Table with Used Card Balance    
	    double debitCardAmtUsed = 0;
	    sql = "select sum(b.dblSettlementAmt) "
		    + " from tblbillhd a,tblbillsettlementdtl b,tblsettelmenthd c "
		    + " where a.strBillNo=b.strBillNo and b.strSettlementCode=c.strSettelmentCode "
		    + " and date(a.dteBillDate)='" + posDate + "' and a.strPOSCode='" + posCode + "' "
		    + " and c.strSettelmentType='Debit Card' "
		    + " group by a.strPOSCode,date(a.dteBillDate),c.strSettelmentType;";
	    ResultSet rsUsedDCAmt = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsUsedDCAmt.next())
	    {
		debitCardAmtUsed = rsUsedDCAmt.getDouble(1);
	    }
	    rsUsedDCAmt.close();
	    sql = "update tbldayendprocess set dblUsedDebitCardBalance=" + debitCardAmtUsed + " "
		    + " where date(dtePOSDate)='" + posDate + "' and strPOSCode='" + posCode + "' "
		    + " and intShiftCode=" + shiftNo;
	    clsGlobalVarClass.dbMysql.execute(sql);

	    // Update Day End Table with UnUsed Card Balance    
	    double debitCardAmtUnUsed = 0;
	    sql = "select sum(dblCardAmt) from tbldebitcardrevenue "
		    + " where strPOSCode='" + posCode + "' and date(dtePOSDate)='" + posDate + "' "
		    + " group by strPOSCode,date(dtePOSDate);";
	    ResultSet rsUnUsedDCAmt = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsUnUsedDCAmt.next())
	    {
		debitCardAmtUnUsed = rsUnUsedDCAmt.getDouble(1);
	    }
	    rsUnUsedDCAmt.close();
	    sql = "update tbldayendprocess set dblUnusedDebitCardBalance=" + debitCardAmtUnUsed + " "
		    + " where date(dtePOSDate)='" + posDate + "' and strPOSCode='" + posCode + "' "
		    + " and intShiftCode=" + shiftNo;
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "UPDATE tbldayendprocess SET dblTipAmt= IFNULL(( "
		    + "SELECT SUM(dblTipAmount) "
		    + "FROM tblbillhd "
		    + "WHERE DATE(dteBillDate) ='" + posDate + "' AND intShiftCode='" + shiftNo + "' AND strPOSCode='" + posCode + "'),0) "
		    + "WHERE DATE(dtePOSDate)='" + posDate + "' AND strPOSCode='" + posCode + "' AND intShiftCode='" + shiftNo + "' ";
	    clsGlobalVarClass.dbMysql.execute(sql);

	    //update no. of complementary bills
	    sql = "update tbldayendprocess set intNoOfComplimentaryKOT=(select COUNT(distinct(a.strBillNo))"
		    + "from  tblbillhd a,tblbillcomplementrydtl b "
		    + "where a.strBillNo=b.strBillNo "
		    + "and date(b.dteBillDate)='" + posDate + "' and a.strPOSCode='" + posCode + "') "
		    + "where date(dtePOSDate)='" + posDate + "' and strPOSCode='" + posCode + "' and intShiftCode=" + shiftNo;
	    //System.out.println("intNoOfComplimentaryKOT:==" + sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    //update no. of void KOTs
	    sql = "update tbldayendprocess set intNoOfVoidKOT=(select count(distinct(a.strKOTNo)) "
		    + "from tblvoidkot a "
		    + "where a.strPOSCode='" + posCode + "' "
		    + "and date(a.dteVoidedDate)='" + posDate + "') "
		    + "where date(dtePOSDate)='" + posDate + "' and strPOSCode='" + posCode + "' and intShiftCode=" + shiftNo;
	    //System.out.println("intNoOfVoidKOT:==" + sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    //update no. of NC KOTs
	    sql = "update tbldayendprocess set intNoOfNCKOT=(select count(distinct(a.strKOTNo)) "
		    + "from tblnonchargablekot a "
		    + "where a.strPOSCode='" + posCode + "' "
		    + "and date(a.dteNCKOTDate)='" + posDate + "') "
		    + "where date(dtePOSDate)='" + posDate + "' and strPOSCode='" + posCode + "' and intShiftCode=" + shiftNo;
	    //System.out.println("intNoOfNCKOT:==" + sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "UPDATE tbldayendprocess SET dblNetSale = IFNULL((SELECT SUM(a.dblSubTotal)-SUM(a.dblDiscountAmt) as NetTotal "
		    + " FROM tblbillhd a "
		    + " WHERE DATE(a.dteBillDate) = '" + posDate + "' "
		    + " AND  a.strPOSCode = '" + posCode + "' AND a.intShiftCode='" + shiftNo + "'),0) "
		    + " WHERE DATE(dtePOSDate)='" + posDate + "' AND strPOSCode = '" + posCode + "' AND intShiftCode=" + shiftNo;
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set dblGrossSale = IFNULL((select sum(b.dblSettlementAmt) "
		    + "TotalSale from tblbillhd a,tblbillsettlementdtl b "
		    + "where a.strBillNo=b.strBillNo and date(a.dteBillDate) = '" + posDate + "' and "
		    + "a.strPOSCode = '" + posCode + "' and a.intShiftCode=" + shiftNo + "),0)"
		    + " where date(dtePOSDate)='" + posDate + "' and strPOSCode = '" + posCode + "'"
		    + " and intShiftCode=" + shiftNo;
	    //System.out.println("UpdateDayEndQuery_1=="+sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "UPDATE tbldayendprocess SET dblAPC = IFNULL(( "
		    + " SELECT SUM(a.dblGrandTotal)/SUM(a.intPaxNo) as APC"
		    + " FROM tblbillhd a "
		    + " WHERE  DATE(a.dteBillDate) = '" + posDate + "' "
		    + " AND  a.strPOSCode = '" + posCode + "' AND a.intShiftCode='" + shiftNo + "'),0) "
		    + " WHERE DATE(dtePOSDate)='" + posDate + "' AND strPOSCode = '" + posCode + "' AND intShiftCode=" + shiftNo;
	    clsGlobalVarClass.dbMysql.execute(sql);

	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
	return 1;
    }

// Function to update values in tbldayendprocess table.
// This function updates values from Q File tables.
    public int funUpdateDayEndFieldsForQFile(String posDate, int shiftNo, String dayEnd)
    {
	try
	{
	    String sql = "update tbldayendprocess set dblTotalSale = IFNULL((select sum(b.dblSettlementAmt) "
		    + "TotalSale from tblqbillhd a,tblqbillsettlementdtl b "
		    + " where a.strBillNo=b.strBillNo "
		    + " and date(a.dteBillDate)=date(b.dteBillDate)  "
		    + " and date(a.dteBillDate) = '" + posDate + "' and "
		    + " a.strPOSCode = '" + clsGlobalVarClass.gPOSCode + "' "
		    + " and a.intShiftCode=" + shiftNo + "),0)"
		    + " where date(dtePOSDate)='" + posDate + "' "
		    + " and strPOSCode = '" + clsGlobalVarClass.gPOSCode + "'"
		    + " and intShiftCode=" + shiftNo;
	    //System.out.println("UpdateDayEndQuery_1=="+sql);
	    clsGlobalVarClass.dbMysql.execute(sql);
	    sql = "update tbldayendprocess set dteDayEndDateTime='" + clsGlobalVarClass.getCurrentDateTime() + "'"
		    + " where date(dtePOSDate)='" + posDate + "' "
		    + " and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + " and intShiftCode=" + shiftNo;
	    //System.out.println("UpdateDayEndQuery_2=="+sql);

	    clsGlobalVarClass.dbMysql.execute(sql);
	    sql = "update tbldayendprocess set strUserEdited='" + clsGlobalVarClass.gUserCode + "'"
		    + " where date(dtePOSDate)='" + posDate + "' "
		    + " and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + " and intShiftCode=" + shiftNo;
	    //System.out.println("UpdateDayEndQuery_3=="+sql);

	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set dblNoOfBill = IFNULL((select count(*) NoOfBills "
		    + "from tblqbillhd "
		    + " where Date(dteBillDate) = '" + posDate + "' "
		    + " and strPOSCode = '" + clsGlobalVarClass.gPOSCode + "' "
		    + " and intShiftCode=" + shiftNo + "),0)"
		    + " where date(dtePOSDate)='" + posDate + "' "
		    + " and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + " and intShiftCode=" + shiftNo;
	    //System.out.println("UpdateDayEndQuery_4=="+sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set dblNoOfVoidedBill = IFNULL((select count(DISTINCT strBillNo) "
		    + "NoOfVoidBills from tblvoidbillhd "
		    + " where date(dteModifyVoidBill) = " + "'" + posDate + "'"
		    + " and strPOSCode = '" + clsGlobalVarClass.gPOSCode + "' "
		    + " and strTransType = 'VB'"
		    + " and intShiftCode=" + shiftNo + "),0)"
		    + " where date(dtePOSDate)='" + posDate + "' "
		    + " and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + " and intShiftCode=" + shiftNo;
	    //System.out.println("UpdateDayEndQuery_5=="+sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set dblNoOfModifyBill = IFNULL((select count(DISTINCT b.strBillNo) "
		    + "NoOfModifiedBills "
		    + " from tblqbillhd a,tblvoidbillhd b "
		    + " where a.strBillNo=b.strBillNo "
		    + " and date(a.dteBillDate)=date(b.dteBillDate) "
		    + " and Date(b.dteModifyVoidBill) = '" + posDate + "' "
		    + " and b.strPOSCode='" + clsGlobalVarClass.gPOSCode + "'"
		    + " and b.strTransType = 'MB' "
		    + " and a.intShiftCode=" + shiftNo + "),0)"
		    + " where date(dtePOSDate)='" + posDate + "' "
		    + " and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + " and intShiftCode=" + shiftNo;
	    //System.out.println("UpdateDayEndQuery_6=="+sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set dblHDAmt=IFNULL((select sum(a.dblGrandTotal) HD "
		    + " from tblqbillhd a,tblhomedelivery b "
		    + " where a.strBillNo=b.strBillNo "
		    + " and date(a.dteBillDate) = '" + posDate + "' and "
		    + " a.strPOSCode = '" + clsGlobalVarClass.gPOSCode + "' "
		    + " and a.intShiftCode=" + shiftNo + "), 0) "
		    + " where date(dtePOSDate)='" + posDate + "' "
		    + " and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + " and intShiftCode=" + shiftNo;
	    //System.out.println("UpdateDayEndQuery_7=="+sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set dblDiningAmt=IFNULL(( select sum(dblGrandTotal) Dining"
		    + " from tblqbillhd "
		    + " where strTakeAway='No' "
		    + " and date(dteBillDate) = '" + posDate + "' "
		    + " and strPOSCode = '" + clsGlobalVarClass.gPOSCode + "'"
		    + " and strBillNo NOT IN (select strBillNo from tblhomedelivery where strBillNo is not NULL) "
		    + " and intShiftCode=" + clsGlobalVarClass.gShiftNo + "),0)"
		    + " where date(dtePOSDate)='" + posDate + "' "
		    + " and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + " and intShiftCode=" + shiftNo;
	    clsGlobalVarClass.dbMysql.execute(sql);
	    //System.out.println("UpdateDayEndQuery_8=="+sql);

	    sql = "update tbldayendprocess set dblTakeAway=IFNULL((select sum(dblGrandTotal) TakeAway from tblqbillhd"
		    + " where strTakeAway='Yes' "
		    + " and date(dteBillDate) = '" + posDate + "' "
		    + " and strPOSCode = '" + clsGlobalVarClass.gPOSCode + "'"
		    + " and intShiftCode=" + shiftNo + "),0)"
		    + " where date(dtePOSDate)='" + posDate + "' "
		    + " and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + " and intShiftCode=" + shiftNo;

	    //System.out.println("UpdateDayEndQuery_9=="+sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set dblFloat=IFNULL((select sum(dblAmount) TotalFloats from tblcashmanagement "
		    + "where strTransType='Float' "
		    + " and date(dteTransDate) = '" + posDate + "' "
		    + " and strPOSCode = '" + clsGlobalVarClass.gPOSCode + "'"
		    + " and intShiftCode=" + shiftNo + ""
		    + " group by strTransType),0) "
		    + "where date(dtePOSDate)='" + posDate + "' "
		    + " and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + " and intShiftCode=" + shiftNo;
	    //System.out.println("UpdateDayEndQuery_10=="+sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set dblTransferIn=IFNULL((select sum(dblAmount) TotalTransferIn from tblcashmanagement "
		    + "where strTransType='Transfer In' and dteTransDate = '" + posDate + "'"
		    + " and strPOSCode = '" + clsGlobalVarClass.gPOSCode + "' "
		    + " and intShiftCode=" + shiftNo
		    + " group by strTransType),0) "
		    + " where date(dtePOSDate)='" + posDate + "' "
		    + " and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + " and intShiftCode=" + shiftNo;
	    //System.out.println("UpdateDayEndQuery_11=="+sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set dblTransferOut=IFNULL((select sum(dblAmount) TotalTransferOut from tblcashmanagement "
		    + " where strTransType='Transfer Out' "
		    + " and date(dteTransDate) = '" + posDate + "'"
		    + " and strPOSCode = '" + clsGlobalVarClass.gPOSCode + "' "
		    + " and intShiftCode=" + shiftNo + ""
		    + " group by strTransType),0) "
		    + " where date(dtePOSDate)='" + posDate + "' "
		    + " and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + " and intShiftCode=" + shiftNo;
	    //System.out.println("UpdateDayEndQuery_12=="+sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set dblWithdrawal=IFNULL(( select sum(dblAmount) TotalWithdrawals from tblcashmanagement "
		    + " where strTransType='Withdrawal' "
		    + " and date(dteTransDate) = '" + posDate + "' "
		    + " and strPOSCode = '" + clsGlobalVarClass.gPOSCode + "' "
		    + " and intShiftCode=" + shiftNo + ""
		    + " group by strTransType),0) "
		    + " where date(dtePOSDate)='" + posDate + "' "
		    + " and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + " and intShiftCode=" + shiftNo;
	    //System.out.println("UpdateDayEndQuery_13=="+sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set dblRefund=IFNULL(( select sum(dblAmount) TotalRefunds from tblcashmanagement "
		    + " where strTransType='Refund' "
		    + " and date(dteTransDate) = '" + posDate + "' "
		    + " and strPOSCode = '" + clsGlobalVarClass.gPOSCode + "'"
		    + " and intShiftCode=" + shiftNo + " group by strTransType),0)"
		    + " where date(dtePOSDate)='" + posDate + "' "
		    + " and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + " and intShiftCode=" + shiftNo;
	    //System.out.println("UpdateDayEndQuery_14=="+sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set dblPayments=IFNULL(( select sum(dblAmount) TotalPayments from tblcashmanagement "
		    + " where strTransType='Payments' "
		    + " and date(dteTransDate) = '" + posDate + "'"
		    + " and strPOSCode = '" + clsGlobalVarClass.gPOSCode + "' "
		    + " and intShiftCode=" + shiftNo + ""
		    + " group by strTransType),0) "
		    + " where date(dtePOSDate)='" + posDate + "' "
		    + " and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + " and intShiftCode=" + shiftNo;
	    //System.out.println("UpdateDayEndQuery_15=="+sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set dblAdvance=IFNULL((select sum(b.dblAdvDepositesettleAmt) "
		    + " from tbladvancereceipthd a,tbladvancereceiptdtl b,tblsettelmenthd c "
		    + " where date(a.dtReceiptDate)='" + posDate + "' "
		    + " and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + " and c.strSettelmentCode=b.strSettlementCode "
		    + " and a.strReceiptNo=b.strReceiptNo "
		    + " and c.strSettelmentType='Cash' "
		    + " and intShiftCode=" + shiftNo + "),0)"
		    + " where date(dtePOSDate)='" + posDate + "' "
		    + " and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + " and intShiftCode=" + shiftNo;
	    //System.out.println("UpdateDayEndQuery_16=="+sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set dblTotalReceipt=" + gTotalReceipt
		    + " where date(dtePOSDate)='" + posDate + "' "
		    + " and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + " and intShiftCode=" + shiftNo;
	    //System.out.println("UpdateDayEndQuery_17=="+sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set dblTotalPay=" + gTotalPayments
		    + " where date(dtePOSDate)='" + posDate + "' "
		    + " and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + " and intShiftCode=" + shiftNo;
	    //System.out.println("UpdateDayEndQuery_18=="+sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set dblCashInHand=" + gTotalCashInHand
		    + " where date(dtePOSDate)='" + posDate + "' "
		    + " and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + " and intShiftCode=" + shiftNo;
	    //System.out.println("UpdateDayEndQuery_19=="+sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set dblCash=" + gTotalCashSales
		    + " where date(dtePOSDate)='" + posDate + "' "
		    + " and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + " and intShiftCode=" + shiftNo;
	    //System.out.println(sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set dblTotalDiscount=" + gTotalDiscounts
		    + " where date(dtePOSDate)='" + posDate + "' "
		    + " and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + " and intShiftCode=" + shiftNo;
	    //System.out.println("UpdateDayEndQuery_21=="+sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set dblNoOfDiscountedBill=" + gNoOfDiscountedBills
		    + " where date(dtePOSDate)='" + posDate + "' "
		    + " and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + " and intShiftCode=" + shiftNo;
	    //System.out.println("UpdateDayEndQuery_22=="+sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set intTotalPax=IFNULL((select sum(intBillSeriesPaxNo)"
		    + " from tblqbillhd "
		    + " where date(dteBillDate ) ='" + posDate + "' "
		    + " and intShiftCode=" + shiftNo + ""
		    + " and strPOSCode='" + clsGlobalVarClass.gPOSCode + "'),0)"
		    + " where date(dtePOSDate)='" + posDate + "' "
		    + " and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + " and intShiftCode=" + shiftNo;
	    //System.out.println("UpdateDayEndQuery_23=="+sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set intNoOfTakeAway=(select count(strTakeAway)"
		    + " from tblqbillhd where date(dteBillDate )='" + posDate + "' "
		    + " and intShiftCode=" + shiftNo + ""
		    + " and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + " and strTakeAway='Yes')"
		    + " where date(dtePOSDate)='" + posDate + "' "
		    + " and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + " and intShiftCode=" + shiftNo;
	    //System.out.println("update int takeawy==" + sql);
	    clsGlobalVarClass.dbMysql.execute(sql);
	    sql = "update tbldayendprocess set intNoOfHomeDelivery=(select COUNT(strBillNo)from tblhomedelivery where date(dteDate)='" + posDate + "' and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' )"
		    + " where date(dtePOSDate)='" + posDate + "' "
		    + " and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + " and intShiftCode=" + shiftNo;
	    //System.out.println("update int homedelivry:==" + sql);
	    clsGlobalVarClass.dbMysql.execute(sql);

	    // Update Day End Table with Used Card Balance    
	    double debitCardAmtUsed = 0;
	    sql = "select sum(b.dblSettlementAmt) "
		    + " from tblqbillhd a,tblqbillsettlementdtl b,tblsettelmenthd c "
		    + " where a.strBillNo=b.strBillNo "
		    + " and b.strSettlementCode=c.strSettelmentCode "
		    + " and date(a.dteBillDate)=date(b.dteBillDate) "
		    + " and date(a.dteBillDate)='" + posDate + "' "
		    + " and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + " and c.strSettelmentType='Debit Card' "
		    + " group by a.strPOSCode,date(a.dteBillDate),c.strSettelmentType;";
	    ResultSet rsUsedDCAmt = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsUsedDCAmt.next())
	    {
		debitCardAmtUsed = rsUsedDCAmt.getDouble(1);
	    }
	    rsUsedDCAmt.close();
	    sql = "update tbldayendprocess set dblUsedDebitCardBalance=" + debitCardAmtUsed + " "
		    + " where date(dtePOSDate)='" + posDate + "' "
		    + " and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + " and intShiftCode=" + shiftNo;
	    clsGlobalVarClass.dbMysql.execute(sql);

	    // Update Day End Table with UnUsed Card Balance    
	    double debitCardAmtUnUsed = 0;
	    sql = "select sum(dblCardAmt) from tbldebitcardrevenue "
		    + " where strPOSCode='" + clsGlobalVarClass.gPOSCode + "'  "
		    + " and date(dtePOSDate)='" + posDate + "' "
		    + " group by strPOSCode,date(dtePOSDate);";
	    ResultSet rsUnUsedDCAmt = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsUnUsedDCAmt.next())
	    {
		debitCardAmtUnUsed = rsUnUsedDCAmt.getDouble(1);
	    }
	    rsUnUsedDCAmt.close();
	    sql = "update tbldayendprocess set dblUnusedDebitCardBalance=" + debitCardAmtUnUsed + " "
		    + " where date(dtePOSDate)='" + posDate + "' "
		    + " and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + " and intShiftCode=" + shiftNo;
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "UPDATE tbldayendprocess SET dblTipAmt= IFNULL(( "
		    + " SELECT SUM(dblTipAmount) "
		    + " FROM tblqbillhd "
		    + " WHERE DATE(dteBillDate) ='" + posDate + "' "
		    + " AND intShiftCode='" + shiftNo + "' "
		    + " AND strPOSCode='" + clsGlobalVarClass.gPOSCode + "'),0) "
		    + " WHERE DATE(dtePOSDate)='" + posDate + "' "
		    + " AND strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + " AND intShiftCode='" + shiftNo + "' ";
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "update tbldayendprocess set intNoOfComplimentaryKOT=(select COUNT(a.strBillNo)"
		    + " from  tblqbillhd a,tblqbillcomplementrydtl b "
		    + " where a.strBillNo=b.strBillNo "
		    + " and date(a.dteBillDate)=date(b.dteBillDate) "
		    + " and date(b.dteBillDate)='" + posDate + "' "
		    + " and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "') "
		    + " where date(dtePOSDate)='" + posDate + "' "
		    + " and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + " and intShiftCode=" + shiftNo;
//            System.out.println("intNoOfComplimentaryKOT:==" + sql);
	    clsGlobalVarClass.dbMysql.execute(sql);
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}

	return 1;
    }

    public int funDayEndflash(String posCode, String billDate, int shiftNo)
    {
	try
	{
	    String filePath = System.getProperty("user.dir");
	    filePath = filePath + "/Temp/Temp_DayEndReport.txt";

	    clsDayEndTextReport obj = new clsDayEndTextReport();
	    obj.funGenerateTextDayEndReport(clsGlobalVarClass.gPOSCode, billDate, "", shiftNo, "Y");

	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
	finally
	{
	    return 1;
	}
    }

// Function to send bill data to sanguine cms.
// Function to send bill data to sanguine cms.
    public int funPostSanguineCMSData(String posCode, String billDate)
    {
	int res = 0;
	String roundOffAccCode = "";
	double roundOff = 0, creditAmt = 0, debitAmt = 0;
	try
	{

	    String gAmount = "SUM(b.dblAmount)";
	    String gTaxAmount = "SUM(b.dblTaxAmount)";
	    String gDiscAmount = "sum(a.dblDiscountAmt)";

	    String settlementAmount = "IFNULL(SUM(b.dblSettlementAmt),0)";
	    String cashSettlementAmt = " IFNULL(SUM(b.dblSettlementAmt),0)";
	    String memberSettlementAmount = "b.dblSettlementAmt";
	    String customerAmt = "a.dblGrandTotal";

	    if (clsGlobalVarClass.gPOSToWebBooksPostingCurrency.equalsIgnoreCase("USD"))
	    {
		gAmount = "SUM(b.dblAmount/a.dblUSDConverionRate)";
		gTaxAmount = "SUM(b.dblTaxAmount/a.dblUSDConverionRate)";
		gDiscAmount = "sum(a.dblDiscountAmt/a.dblUSDConverionRate)";

		settlementAmount = "IFNULL(SUM(b.dblSettlementAmt/a.dblUSDConverionRate),0)";
		cashSettlementAmt = " IFNULL(SUM(b.dblSettlementAmt/a.dblUSDConverionRate),0)";
		memberSettlementAmount = "b.dblSettlementAmt/a.dblUSDConverionRate";
		customerAmt="a.dblGrandTotal/a.dblUSDConverionRate ";
	    }

	    JSONObject jObj = new JSONObject();

	    jObj.put("POSCode", posCode);
	    jObj.put("POSDate", billDate);
	    jObj.put("User", clsGlobalVarClass.gUserCode);

	    String sql_SubGroupWise = "SELECT a.strPOSCode, IFNULL(d.strSubGroupCode,'NA'), IFNULL(d.strSubGroupName,'NA')," + gAmount + ", DATE(a.dteBillDate),d.strAccountCode "
		    + "FROM tblbillhd a,tblbilldtl b,tblitemmaster c,tblsubgrouphd d  "
		    + "WHERE a.strPOSCode='" + posCode + "' "
		    + "and a.strBillNo=b.strBillNo "
		    + "and DATE(a.dteBillDate)=DATE(b.dteBillDate)  "
		    + "and b.strItemCode=c.strItemCode "
		    + "and c.strSubGroupCode=d.strSubGroupCode "
		    + "GROUP BY d.strSubGroupCode,d.strSubGroupName ";

	    JSONArray arrObjSubGroupwise = new JSONArray();
	    ResultSet rsSubGroupWise = clsGlobalVarClass.dbMysql.executeResultSet(sql_SubGroupWise);
	    while (rsSubGroupWise.next())
	    {
		JSONObject objSubGroupWise = new JSONObject();
		creditAmt += rsSubGroupWise.getDouble(4);
		objSubGroupWise.put("RVCode", rsSubGroupWise.getString(1) + "-" + rsSubGroupWise.getString(2));
		objSubGroupWise.put("RVName", clsGlobalVarClass.gPOSName + "-" + rsSubGroupWise.getString(3));
		objSubGroupWise.put("CRAmt", rsSubGroupWise.getDouble(4));
		objSubGroupWise.put("DRAmt", 0);
		objSubGroupWise.put("ClientCode", clsGlobalVarClass.gClientCode);
		objSubGroupWise.put("BillDate", rsSubGroupWise.getString(5));
		objSubGroupWise.put("CMSPOSCode", clsGlobalVarClass.gCMSPOSCode);
		objSubGroupWise.put("POSCode", posCode);
		objSubGroupWise.put("BillDateTo", rsSubGroupWise.getString(5));
		objSubGroupWise.put("AccountCode", rsSubGroupWise.getString(6));
		arrObjSubGroupwise.add(objSubGroupWise);
	    }
	    rsSubGroupWise.close();
	    jObj.put("SubGroupwise", arrObjSubGroupwise);

	    String sql_TaxWise = "SELECT a.strPOSCode,c.strTaxCode,c.strTaxDesc," + gTaxAmount + " , DATE(a.dteBillDate),c.strAccountCode "
		    + "FROM tblbillhd a,tblbilltaxdtl b , tbltaxhd c "
		    + "where a.strPOSCode='" + posCode + "' "
		    + "and a.strBillNo=b.strBillNo "
		    + "and DATE(a.dteBillDate)=DATE(b.dteBillDate) and c.strTaxCalculation<>'Backward' "
		    + "and b.strTaxCode=c.strTaxCode "
		    + " GROUP BY a.strPOSCode,c.strTaxCode ";

	    JSONArray arrObjTaxwise = new JSONArray();
	    ResultSet rsTaxWise = clsGlobalVarClass.dbMysql.executeResultSet(sql_TaxWise);
	    while (rsTaxWise.next())
	    {
		JSONObject objTaxWise = new JSONObject();
		creditAmt += rsTaxWise.getDouble(4);
		objTaxWise.put("RVCode", rsTaxWise.getString(1) + "-" + rsTaxWise.getString(2));
		objTaxWise.put("RVName", clsGlobalVarClass.gPOSName + "-" + rsTaxWise.getString(3));
		objTaxWise.put("CRAmt", rsTaxWise.getDouble(4));
		objTaxWise.put("DRAmt", 0);
		objTaxWise.put("ClientCode", clsGlobalVarClass.gClientCode);
		objTaxWise.put("BillDate", rsTaxWise.getString(5));
		objTaxWise.put("CMSPOSCode", clsGlobalVarClass.gCMSPOSCode);
		objTaxWise.put("POSCode", posCode);
		objTaxWise.put("BillDateTo", rsTaxWise.getString(5));
		objTaxWise.put("AccountCode", rsTaxWise.getString(6));
		arrObjTaxwise.add(objTaxWise);
	    }
	    rsTaxWise.close();
	    jObj.put("Taxwise", arrObjTaxwise);

	    String sql_Discount = "select a.strPOSCode," + gDiscAmount + ",date(a.dteBillDate),b.strRoundOff,b.strTip,b.strDiscount "
		    + "from tblbillhd a,tblposmaster b "
		    + "where a.strPOSCode='" + posCode + "' "
		    + " and a.strPOSCode=b.strPosCode "
		    + "group by a.strPOSCode";

	    JSONArray arrObjDiscountwise = new JSONArray();
	    ResultSet rsDiscount = clsGlobalVarClass.dbMysql.executeResultSet(sql_Discount);
	    while (rsDiscount.next())
	    {
		if (rsDiscount.getDouble(2) > 0)
		{
		    JSONObject objDiscount = new JSONObject();
		    debitAmt += rsDiscount.getDouble(2);
		    roundOffAccCode = rsDiscount.getString(4);
		    objDiscount.put("RVCode", rsDiscount.getString(1) + "-Discount");
		    objDiscount.put("RVName", "Discount");
		    objDiscount.put("CRAmt", 0);
		    objDiscount.put("DRAmt", rsDiscount.getDouble(2));
		    objDiscount.put("ClientCode", clsGlobalVarClass.gClientCode);
		    objDiscount.put("BillDate", rsDiscount.getString(3));
		    objDiscount.put("CMSPOSCode", clsGlobalVarClass.gCMSPOSCode);
		    objDiscount.put("POSCode", posCode);
		    objDiscount.put("BillDateTo", rsDiscount.getString(3));
		    objDiscount.put("AccountCode", rsDiscount.getString(6));
		    arrObjDiscountwise.add(objDiscount);
		}
	    }
	    rsDiscount.close();
	    jObj.put("Discountwise", arrObjDiscountwise);

	    String sql_Settlement = "SELECT a.strPOSCode, IFNULL(b.strSettlementCode,''), IFNULL(c.strSettelmentDesc,'')," + settlementAmount + " , DATE(a.dteBillDate),c.strAccountCode "
		    + "FROM tblbillhd a,tblbillsettlementdtl b ,tblsettelmenthd c  "
		    + "WHERE  a.strPOSCode='" + posCode + "' "
		    //     + " c.strSettelmentType='Member'  "
		    + "and a.strBillNo=b.strBillNo "
		    + "and b.strSettlementCode=c.strSettelmentCode "
		    + "GROUP BY a.strPOSCode, b.strSettlementCode, c.strSettelmentDesc ";
	    JSONArray arrObjMemberSettlewise = new JSONArray();
	    ResultSet rsCashSettlement = clsGlobalVarClass.dbMysql.executeResultSet(sql_Settlement);
	    while (rsCashSettlement.next())
	    {
		JSONObject objSettlementWise = new JSONObject();
		debitAmt += rsCashSettlement.getDouble(4);
		objSettlementWise.put("RVCode", rsCashSettlement.getString(1) + "-" + rsCashSettlement.getString(2));
		objSettlementWise.put("RVName", clsGlobalVarClass.gPOSName + "-" + rsCashSettlement.getString(3));
		objSettlementWise.put("CRAmt", 0);
		objSettlementWise.put("DRAmt", rsCashSettlement.getDouble(4));
		objSettlementWise.put("ClientCode", clsGlobalVarClass.gClientCode);
		objSettlementWise.put("BillDate", rsCashSettlement.getString(5));
		objSettlementWise.put("CMSPOSCode", clsGlobalVarClass.gCMSPOSCode);
		objSettlementWise.put("POSCode", posCode);
		objSettlementWise.put("BillDateTo", rsCashSettlement.getString(5));
		objSettlementWise.put("AccountCode", rsCashSettlement.getString(6));
		arrObjMemberSettlewise.add(objSettlementWise);
	    }
	    rsCashSettlement.close();
	    jObj.put("MemberSettlewise", arrObjMemberSettlewise);

	    
	    /*sql_Settlement = "SELECT a.strPOSCode, IFNULL(b.strSettlementCode,''), IFNULL(c.strSettelmentDesc,'')," + cashSettlementAmt + ", DATE(a.dteBillDate),c.strAccountCode "
		    + "FROM tblbillhd a,tblbillsettlementdtl b ,tblsettelmenthd c  "
		    + "WHERE "
//		    + "c.strSettelmentType='Cash' "
		    + "AND a.strPOSCode='" + posCode + "' "
		    + "and a.strBillNo=b.strBillNo "
		    + "and b.strSettlementCode=c.strSettelmentCode "
		    + "GROUP BY a.strPOSCode, b.strSettlementCode, c.strSettelmentDesc";
	    JSONArray arrObjCashSettlewise = new JSONArray();
	    ResultSet rsMemberSettlement = clsGlobalVarClass.dbMysql.executeResultSet(sql_Settlement);
	    while (rsMemberSettlement.next())
	    {
		JSONObject objSettlementWise = new JSONObject();
		debitAmt += rsMemberSettlement.getDouble(4);
		objSettlementWise.put("RVCode", rsMemberSettlement.getString(1) + "-" + rsMemberSettlement.getString(2));
		objSettlementWise.put("RVName", clsGlobalVarClass.gPOSName + "-" + rsMemberSettlement.getString(3));
		objSettlementWise.put("CRAmt", 0);
		objSettlementWise.put("DRAmt", rsMemberSettlement.getDouble(4));
		objSettlementWise.put("ClientCode", clsGlobalVarClass.gClientCode);
		objSettlementWise.put("BillDate", rsMemberSettlement.getString(5));
		objSettlementWise.put("CMSPOSCode", clsGlobalVarClass.gCMSPOSCode);
		objSettlementWise.put("POSCode", posCode);
		objSettlementWise.put("BillDateTo", rsMemberSettlement.getString(5));
		objSettlementWise.put("AccountCode", rsMemberSettlement.getString(6));
		arrObjCashSettlewise.add(objSettlementWise);
	    }
	    rsMemberSettlement.close();
	    jObj.put("Settlewise", arrObjCashSettlewise);
	    */
	    String sql_MemberCL = "select d.strDebtorCode,d.strCustomerName,a.strBillNo,date(a.dteBillDate)"
		    + "," + memberSettlementAmount + ",d.strAccountCode "
		    + "from tblbillhd a,tblbillsettlementdtl b,tblsettelmenthd c,tblcustomermaster d "
		    + "where a.strBillNo=b.strBillNo "
		    + "and b.strSettlementCode=c.strSettelmentCode "
		    + "and a.strCustomerCode=d.strCustomerCode "
		    + "and a.strPOSCode='" + posCode + "'   "
		    + "and c.strSettelmentType<>'Cash'";
	    JSONArray arrObjMemberClData = new JSONArray();
	    ResultSet rsMemeberCL = clsGlobalVarClass.dbMysql.executeResultSet(sql_MemberCL);
	    while (rsMemeberCL.next())
	    {
		JSONObject objMemeberCL = new JSONObject();
		objMemeberCL.put("DebtorCode", rsMemeberCL.getString(1).trim());
		objMemeberCL.put("DebtorName", rsMemeberCL.getString(2));
		objMemeberCL.put("BillNo", rsMemeberCL.getString(3));
		objMemeberCL.put("BillDate", rsMemeberCL.getString(4));
		objMemeberCL.put("BillAmt", rsMemeberCL.getDouble(5));
		objMemeberCL.put("ClientCode", clsGlobalVarClass.gClientCode);
		objMemeberCL.put("CMSPOSCode", clsGlobalVarClass.gCMSPOSCode);
		objMemeberCL.put("POSCode", posCode);
		objMemeberCL.put("POSName", clsGlobalVarClass.gPOSName);
		objMemeberCL.put("BillDateTo", rsMemeberCL.getString(4));
		objMemeberCL.put("AccountCode", rsMemeberCL.getString(6));
		arrObjMemberClData.add(objMemeberCL);
	    }
	    rsMemeberCL.close();
	    jObj.put("MemberCLData", arrObjMemberClData);

	    String posDate = billDate;

	    roundOffAccCode = "";
	    String sql = "select strRoundOff from tblposmaster where strPOSCode='" + clsGlobalVarClass.gPOSCode + "' ";
	    ResultSet rsRF = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsRF.next())
	    {
		roundOffAccCode = rsRF.getString(1);
	    }
	    rsRF.close();

	    JSONArray arrObjRoundOff = new JSONArray();
	    
	    if(roundOff!=0.0)
	    {
	    JSONObject objRoundOff = new JSONObject();
	    objRoundOff.put("RVCode", clsGlobalVarClass.gPOSCode + "-Roff");
	    objRoundOff.put("RVName", clsGlobalVarClass.gPOSName + "-Roff");
	    roundOff = debitAmt - creditAmt;
	    if (roundOff < 0)
	    {
		roundOff = roundOff * (-1);
		objRoundOff.put("DRAmt", roundOff);
		objRoundOff.put("CRAmt", 0);
	    }
	    else
	    {
		objRoundOff.put("DRAmt", 0);
		objRoundOff.put("CRAmt", roundOff);
	    }
	    objRoundOff.put("ClientCode", clsGlobalVarClass.gClientCode);
	    objRoundOff.put("BillDate", posDate);
	    objRoundOff.put("CMSPOSCode", clsGlobalVarClass.gCMSPOSCode);
	    objRoundOff.put("POSCode", posCode);
	    objRoundOff.put("BillDateTo", posDate);
	    objRoundOff.put("AccountCode", roundOffAccCode);
	    arrObjRoundOff.add(objRoundOff);

	    }

	    jObj.put("RoundOffDtl", arrObjRoundOff);
	    jObj.put("ClientCode", clsGlobalVarClass.gClientCode);
	    String cmsURL = clsGlobalVarClass.gWebBooksWebServiceURL + "/WebBooksIntegration/funPostRevenueToCMS";
	    URL url = new URL(cmsURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    OutputStream os = conn.getOutputStream();
	    os.write(jObj.toString().getBytes());
	    os.flush();

	    if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED)
	    {
		throw new RuntimeException("Failed : HTTP error code : "
			+ conn.getResponseCode());
	    }
	    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	    String output = "", op = "";
	    System.out.println("Output from Server .... \n");
	    while ((output = br.readLine()) != null)
	    {
		op += output;
	    }
	    System.out.println(op);
	    conn.disconnect();
	    if (op.equals("false"))
	    {
		res = 0;
	    }
	    else
	    {
		res = 1;
	    }
	}
	catch (Exception e)
	{
	    res = 0;
	    e.printStackTrace();
	    JOptionPane.showMessageDialog(null, "Check CMS Web Service URL and Internet Connection!!!"); // there is this at null postion
	}
	finally
	{
	    return res;
	}
    }

// Function to send bill data to others cms.
    public int funPostBillDataToCMS(String posCode, String billDate) throws Exception
    {
	int res = 0;
	double roundOff = 0, creditAmt = 0, debitAmt = 0;
	try
	{

	    String gAmount = "sum(b.dblAmount)";
	    String taxAmount = "sum(b.dblTaxAmount)";
	    String discAmount = "sum(dblDiscountAmt)";
	    String settlementAmount = "ifnull(sum(b.dblSettlementAmt),0)";
	    String cashSettlementAmount = "ifnull(sum(b.dblSettlementAmt),0)";
	    String memberSettlementAmount = "b.dblSettlementAmt";
	    if (clsGlobalVarClass.gPOSToWebBooksPostingCurrency.equalsIgnoreCase("USD"))
	    {
		gAmount = "sum(b.dblAmount/a.dblUSDConverionRate)";
		taxAmount = "sum(b.dblTaxAmount/a.dblUSDConverionRate)";
		discAmount = "sum(dblDiscountAmt/a.dblUSDConverionRate)";
		settlementAmount = "ifnull(sum(b.dblSettlementAmt/a.dblUSDConverionRate),0)";
		cashSettlementAmount = "ifnull(sum(b.dblSettlementAmt/a.dblUSDConverionRate),0)";
		memberSettlementAmount = "b.dblSettlementAmt/a.dblUSDConverionRate";
	    }

	    JSONObject jObj = new JSONObject();
	    JSONArray arrObj = new JSONArray();

	    String sql_SubGroupWise = "select a.strPOSCode,ifnull(d.strSubGroupCode,'NA'),ifnull(d.strSubGroupName,'NA')"
		    + "," + gAmount + ",date(a.dteBillDate) "
		    + "from tblbillhd a left outer join tblbilldtl b on a.strBillNo=b.strBillNo "
		    + "left outer join tblitemmaster c on b.strItemCode=c.strItemCode "
		    + "left outer join tblsubgrouphd d on c.strSubGroupCode=d.strSubGroupCode "
		    + "where a.strPOSCode='" + posCode + "' "
		    + "group by d.strSubGroupCode,d.strSubGroupName";
	    //System.out.println(sql_SubGroupWise);

	    ResultSet rsSubGroupWise = clsGlobalVarClass.dbMysql.executeResultSet(sql_SubGroupWise);
	    while (rsSubGroupWise.next())
	    {
		JSONObject objSubGroupWise = new JSONObject();
		objSubGroupWise.put("RVCode", rsSubGroupWise.getString(1) + "-" + rsSubGroupWise.getString(2));
		objSubGroupWise.put("RVName", clsGlobalVarClass.gPOSName + "-" + rsSubGroupWise.getString(3));
		objSubGroupWise.put("CRAmt", rsSubGroupWise.getDouble(4));
		objSubGroupWise.put("DRAmt", 0);
		objSubGroupWise.put("ClientCode", clsGlobalVarClass.gClientCode);
		objSubGroupWise.put("BillDate", rsSubGroupWise.getString(5));
		objSubGroupWise.put("CMSPOSCode", clsGlobalVarClass.gCMSPOSCode);
		objSubGroupWise.put("POSCode", posCode);
		objSubGroupWise.put("BillDateTo", rsSubGroupWise.getString(5));
		arrObj.add(objSubGroupWise);
	    }
	    rsSubGroupWise.close();

	    String sql_TaxWise = "select a.strPOSCode,c.strTaxCode,c.strTaxDesc," + taxAmount + ",date(a.dteBillDate) "
		    + "from tblbillhd a left outer join tblbilltaxdtl b on a.strBillNo=b.strBillNo "
		    + "left outer join tbltaxhd c on b.strTaxCode=c.strTaxCode "
		    + "where a.strPOSCode='" + posCode + "' "
		    + "and DATE(a.dteBillDate)=DATE(b.dteBillDate)  "
		    + "group by c.strTaxCode";
	    //System.out.println(sql_TaxWise);

	    ResultSet rsTaxWise = clsGlobalVarClass.dbMysql.executeResultSet(sql_TaxWise);
	    while (rsTaxWise.next())
	    {
		JSONObject objTaxWise = new JSONObject();
		objTaxWise.put("RVCode", rsTaxWise.getString(1) + "-" + rsTaxWise.getString(2));
		objTaxWise.put("RVName", clsGlobalVarClass.gPOSName + "-" + rsTaxWise.getString(3));
		objTaxWise.put("CRAmt", rsTaxWise.getDouble(4));
		objTaxWise.put("DRAmt", 0);
		objTaxWise.put("ClientCode", clsGlobalVarClass.gClientCode);
		objTaxWise.put("BillDate", rsTaxWise.getString(5));
		objTaxWise.put("CMSPOSCode", clsGlobalVarClass.gCMSPOSCode);
		objTaxWise.put("POSCode", posCode);
		objTaxWise.put("BillDateTo", rsTaxWise.getString(5));
		arrObj.add(objTaxWise);
	    }
	    rsTaxWise.close();

	    String sql_Discount = "select strPOSCode," + discAmount + ",date(dteBillDate) "
		    + "from tblbillhd "
		    + "where strPOSCode='" + posCode + "' "
		    + "group by strPOSCode";
	    ResultSet rsDiscount = clsGlobalVarClass.dbMysql.executeResultSet(sql_Discount);
	    while (rsDiscount.next())
	    {
		JSONObject objDiscount = new JSONObject();
		objDiscount.put("RVCode", rsDiscount.getString(1) + "-Discount");
		objDiscount.put("RVName", "Discount");
		objDiscount.put("CRAmt", 0);
		objDiscount.put("DRAmt", rsDiscount.getDouble(2));
		objDiscount.put("ClientCode", clsGlobalVarClass.gClientCode);
		objDiscount.put("BillDate", rsDiscount.getString(3));
		objDiscount.put("CMSPOSCode", clsGlobalVarClass.gCMSPOSCode);
		objDiscount.put("POSCode", posCode);
		objDiscount.put("BillDateTo", rsDiscount.getString(3));
		arrObj.add(objDiscount);
	    }
	    rsDiscount.close();

	    String sql_Settlement = "select a.strPOSCode,ifnull(b.strSettlementCode,'')"
		    + " ,ifnull(c.strSettelmentDesc,'')," + settlementAmount + ",date(a.dteBillDate) "
		    + " from tblbillhd a left outer join tblbillsettlementdtl b on a.strBillNo=b.strBillNo "
		    + " left outer join tblsettelmenthd c on b.strSettlementCode=c.strSettelmentCode "
		    + " where c.strSettelmentType='Member' and a.strPOSCode='" + posCode + "' "
		    + " group by a.strPOSCode, b.strSettlementCode, c.strSettelmentDesc";
	    //System.out.println(sql_Settlement);

	    ResultSet rsSettlement = clsGlobalVarClass.dbMysql.executeResultSet(sql_Settlement);
	    while (rsSettlement.next())
	    {
		JSONObject objSettlementWise = new JSONObject();
		objSettlementWise.put("RVCode", rsSettlement.getString(1) + "-" + rsSettlement.getString(2));
		objSettlementWise.put("RVName", clsGlobalVarClass.gPOSName + "-" + rsSettlement.getString(3));
		objSettlementWise.put("CRAmt", 0);
		objSettlementWise.put("DRAmt", rsSettlement.getDouble(4));
		objSettlementWise.put("ClientCode", clsGlobalVarClass.gClientCode);
		objSettlementWise.put("BillDate", rsSettlement.getString(5));
		objSettlementWise.put("CMSPOSCode", clsGlobalVarClass.gCMSPOSCode);
		objSettlementWise.put("POSCode", posCode);
		objSettlementWise.put("BillDateTo", rsSettlement.getString(5));
		arrObj.add(objSettlementWise);
	    }
	    rsSettlement.close();

	    sql_Settlement = "select a.strPOSCode,ifnull(b.strSettlementCode,'')"
		    + " ,ifnull(c.strSettelmentDesc,'')," + cashSettlementAmount + ",date(a.dteBillDate) "
		    + " from tblbillhd a left outer join tblbillsettlementdtl b on a.strBillNo=b.strBillNo "
		    + " left outer join tblsettelmenthd c on b.strSettlementCode=c.strSettelmentCode "
		    + " where c.strSettelmentType='Cash' and a.strPOSCode='" + posCode + "' "
		    + " group by a.strPOSCode, b.strSettlementCode, c.strSettelmentDesc";
	    //System.out.println(sql_Settlement);

	    rsSettlement = clsGlobalVarClass.dbMysql.executeResultSet(sql_Settlement);
	    while (rsSettlement.next())
	    {
		JSONObject objSettlementWise = new JSONObject();
		objSettlementWise.put("RVCode", rsSettlement.getString(1) + "-" + rsSettlement.getString(2));
		objSettlementWise.put("RVName", clsGlobalVarClass.gPOSName + "-" + rsSettlement.getString(3));
		objSettlementWise.put("CRAmt", 0);
		objSettlementWise.put("DRAmt", rsSettlement.getDouble(4));
		objSettlementWise.put("ClientCode", clsGlobalVarClass.gClientCode);
		objSettlementWise.put("BillDate", rsSettlement.getString(5));
		objSettlementWise.put("CMSPOSCode", clsGlobalVarClass.gCMSPOSCode);
		objSettlementWise.put("POSCode", posCode);
		objSettlementWise.put("BillDateTo", rsSettlement.getString(5));
		arrObj.add(objSettlementWise);
	    }
	    rsSettlement.close();

	    String posDate = billDate;
	    JSONObject objRoundOff = new JSONObject();
	    objRoundOff.put("RVCode", clsGlobalVarClass.gPOSCode + "-Roff");
	    objRoundOff.put("RVName", clsGlobalVarClass.gPOSName + "-Roff");
	    roundOff = debitAmt - creditAmt;
	    if (roundOff < 0)
	    {
		roundOff = roundOff * (-1);
		objRoundOff.put("DRAmt", roundOff);
		objRoundOff.put("CRAmt", 0);
	    }
	    else
	    {
		objRoundOff.put("DRAmt", 0);
		objRoundOff.put("CRAmt", roundOff);
	    }
	    objRoundOff.put("ClientCode", clsGlobalVarClass.gClientCode);
	    objRoundOff.put("BillDate", posDate);
	    objRoundOff.put("CMSPOSCode", clsGlobalVarClass.gCMSPOSCode);
	    objRoundOff.put("POSCode", posCode);
	    objRoundOff.put("BillDateTo", posDate);
	    arrObj.add(objRoundOff);

	    jObj.put("BillInfo", arrObj);
	    //System.out.println(jObj);

	    String cmsURL = clsGlobalVarClass.gCMSWebServiceURL + "/funPostRVDataToCMS";
	    //System.out.println(cmsURL);
	    URL url = new URL(cmsURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    OutputStream os = conn.getOutputStream();
	    os.write(jObj.toString().getBytes());
	    os.flush();

	    if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED)
	    {
		throw new RuntimeException("Failed : HTTP error code : "
			+ conn.getResponseCode());
	    }
	    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	    String output = "", op = "";
	    System.out.println("Output from Server .... \n");
	    while ((output = br.readLine()) != null)
	    {
		op += output;
	    }
	    System.out.println(op);
	    conn.disconnect();
	    if (op.equals("false"))
	    {
		res = 0;
	    }
	    else
	    {
		JSONObject jObjCL = new JSONObject();
		JSONArray arrObjCL = new JSONArray();
		/*
		 * String sql_MemberCL="select
		 * strCustomerCode,'',strBillNo,date(dteBillDate),dblGrandTotal
		 * " + "from tblbillhd " + "where
		 * strPOSCode='"+clsGlobalVarClass.gPOSCode+"' " + "and
		 * strSettelmentMode='Member'";
		 */
		String sql_MemberCL = "select left(a.strCustomerCode,8),d.strCustomerName,a.strBillNo,date(a.dteBillDate)," + memberSettlementAmount + " "
			+ "from tblbillhd a,tblbillsettlementdtl b,tblsettelmenthd c,tblcustomermaster d "
			+ "where a.strBillNo=b.strBillNo and b.strSettlementCode=c.strSettelmentCode "
			+ "and a.strCustomerCode=d.strCustomerCode "
			+ "and a.strPOSCode='" + posCode + "' "
			+ "and c.strSettelmentType='Member'";
		//System.out.println(sql_MemberCL);

		ResultSet rsMemeberCL = clsGlobalVarClass.dbMysql.executeResultSet(sql_MemberCL);
		while (rsMemeberCL.next())
		{
		    JSONObject objMemeberCL = new JSONObject();
		    objMemeberCL.put("DebtorCode", rsMemeberCL.getString(1).trim());
		    objMemeberCL.put("DebtorName", rsMemeberCL.getString(2));
		    objMemeberCL.put("BillNo", rsMemeberCL.getString(3));
		    objMemeberCL.put("BillDate", rsMemeberCL.getString(4));
		    objMemeberCL.put("BillAmt", rsMemeberCL.getDouble(5));
		    objMemeberCL.put("ClientCode", clsGlobalVarClass.gClientCode);
		    objMemeberCL.put("CMSPOSCode", clsGlobalVarClass.gCMSPOSCode);
		    objMemeberCL.put("POSCode", posCode);
		    objMemeberCL.put("POSName", clsGlobalVarClass.gPOSName);
		    objMemeberCL.put("BillDateTo", rsMemeberCL.getString(4));
		    arrObjCL.add(objMemeberCL);
		}
		rsMemeberCL.close();

		jObjCL.put("MemberCLInfo", arrObjCL);
		//System.out.println(jObjCL);
		String cmsURLCL = clsGlobalVarClass.gCMSWebServiceURL + "/funPostCLDataToCMS";
		//System.out.println(cmsURLCL);
		URL urlCL = new URL(cmsURLCL);
		HttpURLConnection connCL = (HttpURLConnection) urlCL.openConnection();
		connCL.setDoOutput(true);
		connCL.setRequestMethod("POST");
		connCL.setRequestProperty("Content-Type", "application/json");
		OutputStream osCL = connCL.getOutputStream();
		osCL.write(jObjCL.toString().getBytes());
		osCL.flush();

		if (connCL.getResponseCode() != HttpURLConnection.HTTP_CREATED)
		{
		    throw new RuntimeException("Failed : HTTP error code : "
			    + connCL.getResponseCode());
		}
		BufferedReader brCL = new BufferedReader(new InputStreamReader((connCL.getInputStream())));
		String output1 = "", op1 = "";
		System.out.println("Output from Server .... \n");
		while ((output1 = brCL.readLine()) != null)
		{
		    op1 += output1;
		}
		connCL.disconnect();
		System.out.println(op1);
		if (op1.equals("false"))
		{
		    res = 0;
		}
		else
		{
		    res = 1;
		}
	    }
	}
	catch (Exception e)
	{
	    res = 0;
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	    JOptionPane.showMessageDialog(null, "Check CMS Web Service URL and Internet Connection!!!"); // there is this at null postion
	}
	finally
	{
	    return res;
	}
    }

    public boolean funInsertQBillData(String posCode)
    {
	boolean flgResult = false;
	clsUtility2 objUtility2 = new clsUtility2();

	try
	{

	    funInsertQBillDataForNoBillSeries(posCode);
	    flgResult = true;

	}
	catch (Exception e)
	{
	    flgResult = false;
	    funShowDBConnectionLostErrorMessage(e);
	    JOptionPane.showMessageDialog(null, "Qfile Data Posting failed!!!");
	    e.printStackTrace();
	}
	finally
	{
	    return flgResult;
	}
    }

    public String funGetDayForPricing()
    {
	String day = "";
	String[] dayPrice
		=
		{
		    "strPriceSunday", "strPriceMonday", "strPriceTuesday", "strPriceWednesday", "strPriceThursday", "strPriceFriday", "strPriceSaturday"
		};

	String dayNames[] = new DateFormatSymbols().getWeekdays();
	Calendar date2 = Calendar.getInstance();
	date2.setTime(new Date(clsGlobalVarClass.gPOSDate));
	String tempday = dayNames[date2.get(Calendar.DAY_OF_WEEK)];
	switch (tempday)
	{
	    case "Sunday":
		day = "strPriceSunday";
		break;

	    case "Monday":
		day = "strPriceMonday";
		break;

	    case "Tuesday":
		day = "strPriceTuesday";
		break;

	    case "Wednesday":
		day = "strPriceWednesday";
		break;

	    case "Thursday":
		day = "strPriceThursday";
		break;

	    case "Friday":
		day = "strPriceFriday";
		break;

	    case "Saturday":
		day = "strPriceSaturday";
		break;

	    default:
		day = "strPriceSunday";
	}
	return day;
    }

    public String funGetCurrentTime()
    {
	String currentTime = "";
	Date dt = new Date();
	int hours = dt.getHours();
	int minutes = dt.getMinutes();
	if (hours > 12)
	{
	    //hours=hours-12;
	    currentTime = hours + ":" + minutes + " PM";
	}
	else
	{
	    currentTime = hours + ":" + minutes + " AM";
	}
	return currentTime;
    }

    public String funGetCurrentDate()
    {
	Calendar objDate = new GregorianCalendar();
	String currentDate = (objDate.getTime().getYear() + 1900) + "-" + (objDate.getTime().getMonth() + 1) + "-" + objDate.getTime().getDate();
	return currentDate;
    }

    public int funGetDeliveryCharges(String buildingCode, double totalBillAmount, String customerCode)
    {
	double billAmount = 0.00;
	String sqlBuilding = "";
	try
	{
	    if (clsGlobalVarClass.gSlabBasedHDCharges)
	    {
		billAmount = totalBillAmount;
		sqlBuilding = "select IFNULL(a.dblDeliveryCharges,0.00) "
			+ " from tblareawisedc a, tblcustomermaster b "
			+ " where a.strCustTypeCode=b.strCustomerType and a.strBuildingCode='" + buildingCode + "' "
			+ " and " + billAmount + " >=a.dblBillAmount and " + billAmount + " <= a.dblBillAmount1 "
			+ " and b.strCustomerCode='" + customerCode + "'";
	    }
	    else
	    {
		sqlBuilding = "select IFNULL(dblHomeDeliCharge,0.00) from tblbuildingmaster "
			+ "where strBuildingCode='" + buildingCode + "'";
	    }

	    //System.out.println(sqlBuilding);
	    ResultSet rsDelCharges = clsGlobalVarClass.dbMysql.executeResultSet(sqlBuilding);
	    if (rsDelCharges.next())
	    {
		clsGlobalVarClass.gDeliveryCharges = rsDelCharges.getDouble(1);
	    }
	    else
	    {
		clsGlobalVarClass.gDeliveryCharges = 0.00;
	    }
	    rsDelCharges.close();
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
	return 1;
    }

    public String funCheckMemeberBalance(String memCode) throws Exception
    {
	String memberInfo = "";
	double balance = 0, creditLimit = 0;

	memCode = memCode.replaceAll(" ", "%20");
	String cmsURL = clsGlobalVarClass.gCMSWebServiceURL + "/funGetCMSMember?strMemberCode=" + memCode;
	//System.out.println(cmsURL);
	URL url = new URL(cmsURL);
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setRequestMethod("GET");
	conn.setRequestProperty("Accept", "application/json");
	BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	String output = "", op = "";
	while ((output = br.readLine()) != null)
	{
	    op += output;
	}
	String jsonString = op;
	JSONParser parser = new JSONParser();
	Object obj = parser.parse(jsonString);
	JSONObject jObj = (JSONObject) obj;
	JSONArray mJsonArray = (JSONArray) jObj.get("MemberInfo");

	JSONObject mJsonObject = new JSONObject();
	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    if (mJsonObject.get("DebtorCode").toString().equals("no data"))
	    {
		memberInfo = "no data";
	    }
	    else
	    {
		memberInfo = mJsonObject.get("DebtorCode").toString() + "#" + mJsonObject.get("DebtorName").toString();
		balance = Double.parseDouble(mJsonObject.get("BalanceAmt").toString());
		creditLimit = Double.parseDouble(mJsonObject.get("CreditLimit").toString());
		String expired = mJsonObject.get("Expired").toString();
		String stopCredit = mJsonObject.get("StopCredit").toString();
		double settleBalance = creditLimit - balance;
		memberInfo += "#" + balance + "#" + settleBalance + "#" + expired + "#" + creditLimit + "#" + stopCredit;
	    }
	}
	conn.disconnect();
	return memberInfo;
    }

    public String funAuthoriseCMSMemberForRechargeUsingCard(String cardString) throws Exception
    {
	String memberInfo = "";

	String cmsURL = clsGlobalVarClass.gCMSWebServiceURL + "/funValidateCard?cardNo=" + cardString + "&UserCode=" + clsGlobalVarClass.gUserCode + "&ClientCode=" + clsGlobalVarClass.gClientCode + "&CardType=Member";
	//System.out.println(cmsURL);
	URL url = new URL(cmsURL);
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setRequestMethod("GET");
	conn.setRequestProperty("Accept", "application/json");
	BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	String output = "", op = "";
	while ((output = br.readLine()) != null)
	{
	    op += output;
	}
	String jsonString = op;
	JSONParser parser = new JSONParser();
	Object obj = parser.parse(jsonString);
	JSONObject jObj = (JSONObject) obj;
	JSONArray mJsonArray = (JSONArray) jObj.get("CardInfo");

	JSONObject mJsonObject = new JSONObject();
	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    if (mJsonObject.get("CustomerCode").toString().equals("Member Not Found"))
	    {
		JOptionPane.showMessageDialog(null, "Card Not Registered!!!");
	    }
	    else
	    {

		if (mJsonObject.get("Blocked").toString().equals("Y"))
		{
		    JOptionPane.showMessageDialog(null, "Member Is Blocked In CMS!!!");
		}
		else
		{
		    memberInfo = mJsonObject.get("MemberCode").toString();
		    memberInfo = memberInfo + "#" + mJsonObject.get("MemberName").toString();
		}
	    }
	}
	conn.disconnect();
	return memberInfo;
    }

    public boolean funCheckAlphabatesOnly(String text)
    {
	boolean flagAlphabatesOnly = false;
	try
	{
	    byte[] arrTextBytes = text.getBytes();
	    for (int i = 0; i < arrTextBytes.length; i++)
	    {
		if ((arrTextBytes[i] >= 65 && arrTextBytes[i] <= 90) || (arrTextBytes[i] >= 97 && arrTextBytes[i] <= 122))
		{
		    flagAlphabatesOnly = true;
		}
		else
		{
		    flagAlphabatesOnly = false;
		    break;
		}
	    }

	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
	finally
	{
	    return flagAlphabatesOnly;
	}
    }

    public boolean fun_isTDHItem(String tempItemCode, String type, String TableNo)
    {
	boolean flag_isComboItem = false;
	try
	{
	    String ItemType = type;
	    String sql = "";
	    if ("ComboItem".equalsIgnoreCase(ItemType))
	    {
		sql = "select strItemCode from tbltdhhd where strItemCode='" + tempItemCode + "' and strComboItemYN='Y' and strApplicable='Y'";
	    }
	    else if ("ComboItemKOT".equalsIgnoreCase(ItemType))
	    {
		sql = "select a.strItemCode from tbltdhhd a,tblitemrtemp b where a.strItemCode='" + tempItemCode + "' and a.strComboItemYN='Y' and a.strItemCode=b.strItemCode and b.strPrintYN='N' and b.strTableNo='" + TableNo + "'";
	    }
	    else
	    {
		sql = "select strItemCode from tbltdhhd where strItemCode='" + tempItemCode + "' and  strComboItemYN='N' and strApplicable='Y'  ; ";
	    }
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rs.next())
	    {
		flag_isComboItem = true;
	    }
	    rs.close();
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
	finally
	{
	    return flag_isComboItem;
	}
    }

    public void addTDHOnModifierItem()
    {
	String sql = "";
	try
	{
	    if (ListTDHOnModifierItem.isEmpty())
	    {
		sql = "select strItemCode,intMaxQuantity from tbltdhhd where strApplicable='Y' and strComboItemYN='N'; ";
		ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		while (rs.next())
		{
		    ListTDHOnModifierItem.add(rs.getString(1));
		    ListTDHOnModifierItemMaxQTY.add(rs.getDouble(2));
		}
		rs.close();
	    }
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
    }

    public String funGetSingleTrackData(String cardString)
    {
	String cardNo = "";

	if (cardString.contains("?") || cardString.contains(";"))
	{
	    if (cardString.length() > 0)
	    {
		StringBuilder sb = new StringBuilder(cardString);
		int percIndex = sb.indexOf("%");
		String allTracks = "";
		if (sb.toString().contains("?"))
		{
		    allTracks = sb.substring(percIndex, sb.lastIndexOf("?") + 1);
		}
		else
		{
		    allTracks = sb.toString();
		}
		String[] arrText = allTracks.split(";");
		String track1 = "", track2 = "", track3 = "";

		if (arrText.length > 0)
		{
		    if (sb.toString().contains("?"))
		    {
			track1 = arrText[0].substring(1, arrText[0].indexOf("?")).replaceAll("%", "");
			if (arrText.length > 1)
			{
			    track2 = arrText[1].substring(1, arrText[1].indexOf("?")).replaceAll("%", "");
			}
			if (arrText.length > 2)
			{
			    track3 = arrText[2].substring(1, arrText[2].indexOf("?")).replaceAll("%", "");
			}
		    }
		    else
		    {
			track1 = arrText[0].replaceAll("%", "");
			track2 = arrText[1].replaceAll("%", "");
			track3 = arrText[2].replaceAll("%", "");
		    }
		}

		if (!track1.isEmpty())
		{
		    cardNo = track1;
		}
		else if (!track2.isEmpty())
		{
		    cardNo = track2;
		}
		else if (!track3.isEmpty())
		{
		    cardNo = track2;
		}
		//System.out.println(cardNo);
		//cardNo=cardNo.substring(0,10);
	    }
	}
	else
	{
	    cardNo = cardString;
	}

	return cardNo;
    }

    public boolean funValidateDebitCardString(String cardNo)
    {
	boolean flgValidDebitCard = false;
	byte[] arrCard = cardNo.getBytes();
	for (int i = 0; i < arrCard.length; i++)
	{
	    if ((arrCard[i] > 47 && arrCard[i] < 58) || arrCard[i] == 66 || arrCard[i] == 98 || arrCard[i] == 63 || arrCard[i] == 37)
	    {
		flgValidDebitCard = true;
	    }
	    else
	    {
		flgValidDebitCard = false;
		break;
	    }
	    //System.out.println(card[i]);
	}
	return flgValidDebitCard;
    }

    public boolean funCheckDouble(String text)
    {
	boolean flg = false;
	try
	{
	    double num = Double.parseDouble(text);
	    flg = true;
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    flg = false;
	}
	finally
	{
	    return flg;
	}
    }

    public long funCompareTime(String fromDate, String toDate)
    {
	long diff = 0;
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Date d1 = null;
	Date d2 = null;

	try
	{
	    d1 = format.parse(fromDate);
	    d2 = format.parse(toDate);

	    diff = d2.getTime() - d1.getTime();
	    long diffSeconds = diff / 1000 % 60;
	    long diffMinutes = diff / (60 * 1000) % 60;
	    long diffHours = diff / (60 * 60 * 1000) % 24;
	    long diffDays = diff / (24 * 60 * 60 * 1000);
	    String time = diffHours + ":" + diffMinutes + ":" + diffSeconds;
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
	finally
	{
	    return diff;
	}
    }

    public long funCompareDate(String fromDate, String toDate)
    {
	long diff = 0;
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	Date d1 = null;
	Date d2 = null;
	try
	{
	    d1 = format.parse(fromDate);
	    d2 = format.parse(toDate);
	    diff = d2.getTime() - d1.getTime();
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
	return diff;
    }

    public String funGetCalculatedDate(Date dt, int reqdDays)
    {
	String date = "";
	GregorianCalendar cal = new GregorianCalendar();
	cal.setTime(dt);
	cal.add(Calendar.DATE, reqdDays);
	date = (cal.getTime().getYear() + 1900) + "-" + (cal.getTime().getMonth() + 1) + "-" + (cal.getTime().getDate());
	return date;
    }

    public void funPrintBill(String billno, String transType, String billDate, String posCode, String viewORPrint)
    {
	try
	{
	    String reprint = "", formName = "";
	    if (transType.equalsIgnoreCase("Void"))
	    {
		transType = "Void";
		reprint = "";
		formName = "";
	    }
	    else if (transType.equalsIgnoreCase("sales report"))
	    {
		transType = "sale";
		reprint = "reprint";
		formName = "sales report";
	    }
	    else
	    {
		transType = "sale";
		reprint = "reprint";
		formName = "sales report";
	    }

	    clsBillGeneration objBillGeneration = new clsBillGeneration();
	    objBillGeneration.funGenerateBill(billno, reprint, formName, transType, billDate, posCode, viewORPrint);
	}
	catch (Exception ex)
	{
	    funShowDBConnectionLostErrorMessage(ex);
	    ex.printStackTrace();
	}
	finally
	{
	    // clsPrintBill.fun_DeleteFrom_tbltempprintbill(billno);
	}
    }

    public void funPrintBill(String voucherNo, String billDate, boolean flgReprint, String posCode, String viewORPrint)
    {
	try
	{
	    String reprintYN = "";

	    if (flgReprint)
	    {
		reprintYN = "Reprint";
	    }

	    clsBillGeneration objBillGeneration = new clsBillGeneration();
	    objBillGeneration.funGenerateBill(voucherNo, reprintYN, "", "sale", billDate, posCode, viewORPrint);
	}
	catch (Exception e)
	{
	    clsGlobalVarClass.gLog.error(e);
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
	finally
	{

	}
    }

    public void funMinimizeAllWindows()
    {
	Window openedWindows[] = Window.getWindows();
	for (int i = 0; i < openedWindows.length; i++)
	{
	    //openedWindows[i].;
	}
    }

    public void funMinimizeWindow()
    {
	Frame[] arrFrames = Frame.getFrames();
	for (int i = 0; i < arrFrames.length; i++)
	{
	    arrFrames[i].setState(ICONIFIED);
	}
    }

    public void funShowErrorMessage(Exception ex)
    {
	if (ex.getMessage() == null)
	{
	    JOptionPane.showMessageDialog(null, "Please check config file for MySQL credential!!!");
	}
	else if (ex.getMessage().startsWith("Communications link failure"))
	{
	    JOptionPane.showMessageDialog(null, "Connection to Databse is Lost. Check Network Connectivity!!!");
	}
	else if (ex.getMessage().startsWith("Access denied for root password"))
	{
	    JOptionPane.showMessageDialog(null, "Access denied for root password. Please change the password!!!");
	}

    }
    
    public void funShowDBConnectionLostErrorMessage(Exception ex)
    {
	if (ex.getMessage().startsWith("Communications link failure") || ex.getMessage().startsWith("No operations allowed after connection closed"))
	{
	    JOptionPane.showMessageDialog(null, "Connection to Databse is Lost. Check Network Connectivity!!!");
	}
	
    }

    public String funGetAmtInWords(long amt)
    {
	funDisplayAmtInWords(amt);
	return amtInWords;
    }

    private int funDisplayAmtInWords(long amt)
    {
	String strAmt = String.valueOf(amt);

	switch (strAmt.length())
	{
	    case 1:
		amtInWords = amtInWords + " " + funZeroToNintyNineWords(amt);
		break;

	    case 2:
		amtInWords = amtInWords + " " + funZeroToNintyNineWords(amt);
		break;

	    case 3:
		long res = amt / 100;
		if (res > 0)
		{
		    String text = funZeroToNintyNineWords(res);
		    amtInWords = amtInWords + " " + text + " Hundred";
		    long rem = amt % 100;
		    funDisplayAmtInWords(rem);
		}
		break;

	    case 4:
		long res1 = amt / 1000;
		if (res1 > 0)
		{
		    String text = funZeroToNintyNineWords(res1);
		    amtInWords = amtInWords + " " + text + " Thousand";
		    long rem = amt % 1000;
		    funDisplayAmtInWords(rem);
		}
		break;

	    case 5:
		long res2 = amt / 1000;
		if (res2 > 0)
		{
		    String text = funZeroToNintyNineWords(res2);
		    amtInWords = amtInWords + " " + text + " Thousand";
		    long rem = amt % 1000;
		    funDisplayAmtInWords(rem);
		}
		break;

	    case 6:
		long res3 = amt / 100000;
		if (res3 > 0)
		{
		    String text = funZeroToNintyNineWords(res3);
		    amtInWords = amtInWords + " " + text + " Lac";
		    long rem = amt % 100000;
		    funDisplayAmtInWords(rem);
		}
		break;

	    case 7:
		long res4 = amt / 100000;
		if (res4 > 0)
		{
		    String text = funZeroToNintyNineWords(res4);
		    amtInWords = amtInWords + " " + text + " Lac";
		    long rem = amt % 100000;
		    funDisplayAmtInWords(rem);
		}
		break;
	}
	return 1;
    }

    private String funZeroToNintyNineWords(long index)
    {
	String words = "";
	int ind = (int) index;
	switch (ind)
	{
	    case 1:
		words = "One";
		break;

	    case 2:
		words = "Two";
		break;

	    case 3:
		words = "Three";
		break;

	    case 4:
		words = "Four";
		break;

	    case 5:
		words = "Five";
		break;

	    case 6:
		words = "Six";
		break;

	    case 7:
		words = "Seven";
		break;

	    case 8:
		words = "Eight";
		break;

	    case 9:
		words = "Nine";
		break;

	    case 10:
		words = "Ten";
		break;

	    case 12:
		words = "Twelve";
		break;

	    case 13:
		words = "Thirteen";
		break;

	    case 14:
		words = "Fourteen";
		break;

	    case 15:
		words = "Fifteen";
		break;

	    case 16:
		words = "Sixteen";
		break;

	    case 17:
		words = "Seventeen";
		break;

	    case 18:
		words = "Eighteen";
		break;

	    case 19:
		words = "Nineteen";
		break;

	    case 20:
		words = "Twenty";
		break;

	    case 21:
		words = "Twenty One";
		break;

	    case 22:
		words = "Twenty Two";
		break;

	    case 23:
		words = "Twenty Three";
		break;

	    case 24:
		words = "Twenty Four";
		break;

	    case 25:
		words = "Twenty Five";
		break;

	    case 26:
		words = "Twenty Six";
		break;

	    case 27:
		words = "Twenty Seven";
		break;

	    case 28:
		words = "Twenty Eight";
		break;

	    case 29:
		words = "Twenty Nine";
		break;

	    case 30:
		words = "Thirty";
		break;

	    case 31:
		words = "Thirty One";
		break;

	    case 32:
		words = "Thirty Two";
		break;

	    case 33:
		words = "Thirty Three";
		break;

	    case 34:
		words = "Thirty Four";
		break;

	    case 35:
		words = "Thirty Five";
		break;

	    case 36:
		words = "Thirty Six";
		break;

	    case 37:
		words = "Thirty Seven";
		break;

	    case 38:
		words = "Thirty Eight";
		break;

	    case 39:
		words = "Thirty Nine";
		break;

	    case 40:
		words = "Fourty";
		break;

	    case 41:
		words = "Fourty One";
		break;

	    case 42:
		words = "Fourty Two";
		break;

	    case 43:
		words = "Fourty Three";
		break;

	    case 44:
		words = "Fourty Four";
		break;

	    case 45:
		words = "Fourty Five";
		break;

	    case 46:
		words = "Fourty Six";
		break;

	    case 47:
		words = "Fourty Seven";
		break;

	    case 48:
		words = "Fourty Eight";
		break;

	    case 49:
		words = "Fourty Nine";
		break;

	    case 50:
		words = "Fifty";
		break;

	    case 51:
		words = "Fifty One";
		break;

	    case 52:
		words = "Fifty Two";
		break;

	    case 53:
		words = "Fifty Three";
		break;

	    case 54:
		words = "Fifty Four";
		break;

	    case 55:
		words = "Fifty Five";
		break;

	    case 56:
		words = "Fifty Six";
		break;

	    case 57:
		words = "Fifty Seven";
		break;

	    case 58:
		words = "Fifty Eight";
		break;

	    case 59:
		words = "Fifty Nine";
		break;

	    case 60:
		words = "Sixty";
		break;

	    case 61:
		words = "Sixty One";
		break;

	    case 62:
		words = "Sixty Two";
		break;

	    case 63:
		words = "Sixty Three";
		break;

	    case 64:
		words = "Sixty Four";
		break;

	    case 65:
		words = "Sixty Five";
		break;

	    case 66:
		words = "Sixty Six";
		break;

	    case 67:
		words = "Sixty Seven";
		break;

	    case 68:
		words = "Sixty Eight";
		break;

	    case 69:
		words = "Sixty Nine";
		break;

	    case 70:
		words = "Seventy";
		break;

	    case 71:
		words = "Seventy One";
		break;

	    case 72:
		words = "Seventy Two";
		break;

	    case 73:
		words = "Seventy Three";
		break;

	    case 74:
		words = "Seventy Four";
		break;

	    case 75:
		words = "Seventy Five";
		break;

	    case 76:
		words = "Seventy Six";
		break;

	    case 77:
		words = "Seventy Seven";
		break;

	    case 78:
		words = "Seventy Eight";
		break;

	    case 79:
		words = "Seventy Nine";
		break;

	    case 80:
		words = "Eighty";
		break;

	    case 81:
		words = "Eighty One";
		break;

	    case 82:
		words = "Eighty Two";
		break;

	    case 83:
		words = "Eighty Three";
		break;

	    case 84:
		words = "Eighty Four";
		break;

	    case 85:
		words = "Eighty Five";
		break;

	    case 86:
		words = "Eighty Six";
		break;

	    case 87:
		words = "Eighty Seven";
		break;

	    case 88:
		words = "Eighty Eight";
		break;

	    case 89:
		words = "Eighty Nine";
		break;

	    case 90:
		words = "Ninety";
		break;

	    case 91:
		words = "Ninety One";
		break;

	    case 92:
		words = "Ninety Two";
		break;

	    case 93:
		words = "Ninety Three";
		break;

	    case 94:
		words = "Ninety Four";
		break;

	    case 95:
		words = "Ninety Five";
		break;

	    case 96:
		words = "Ninety Six";
		break;

	    case 97:
		words = "Ninety Seven";
		break;

	    case 98:
		words = "Ninety Eight";
		break;

	    case 99:
		words = "Ninety Nine";
		break;
	}
	return words;
    }

    public void funWriteErrorLog(Exception ex)
    {
	System.out.println(ex.getMessage());
	clsGenerateErrorLogs obj = new clsGenerateErrorLogs(ex);
	Thread t = new Thread(obj);
	t.start();
    }

    public int funDebitCardTransaction(String billNo, String debitCardNo, double debitCardSettleAmt, String transType)
    {
	try
	{
	    String delete = "delete from tbldebitcardbilldetails "
		    + "where strBillNo='" + billNo + "' and strTransactionType='" + transType + "' ";
	    clsGlobalVarClass.dbMysql.execute(delete);
	    //System.out.println(delete);

	    String sqlDebitCardDetials = "insert into tbldebitcardbilldetails (strBillNo,strCardNo,"
		    + "dblTransactionAmt,strPOSCode,dteBillDate,strTransactionType)"
		    + "values ('" + billNo + "','" + debitCardNo + "','" + debitCardSettleAmt + "'"
		    + ",'" + clsGlobalVarClass.gPOSCode + "','" + funGetOnlyPOSDateForTransaction() + "'"
		    + ",'" + transType + "')";
	    clsGlobalVarClass.dbMysql.execute(sqlDebitCardDetials);
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(null, e.getMessage(), "Error Code: BS-69", JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	}
	return 1;
    }

    public int funUpdateDebitCardBalance(String debitCardNo, double debitCardSettleAmt, String transType)
    {
	try
	{
	    String sql = "select dblRedeemAmt from tbldebitcardmaster "
		    + "where strCardNo='" + debitCardNo + "'";
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rs.next())
	    {
		double amt = Double.parseDouble(rs.getString(1));
		double updatedBal = amt - debitCardSettleAmt;
		if (transType.equals("Unsettle"))
		{
		    updatedBal = amt + debitCardSettleAmt;
		}
		sql = "update tbldebitcardmaster set dblRedeemAmt='" + updatedBal + "' "
			+ "where strCardNo='" + debitCardNo + "'";
		clsGlobalVarClass.dbMysql.execute(sql);
	    }
	    rs.close();
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(null, e.getMessage(), "Error Code: BS-70", JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	}
	return 1;
    }

    public String funPrintTextWithAlignment(String text, int totalLength, String alignment)
    {
	StringBuilder sbText = new StringBuilder();
	if (alignment.equalsIgnoreCase("Center"))
	{
	    int textLength = text.length();
	    int totalSpace = (totalLength - textLength) / 2;

	    for (int i = 0; i < totalSpace; i++)
	    {
		sbText.append(" ");
	    }
	    sbText.append(text);
	}
	else if (alignment.equalsIgnoreCase("Left"))
	{
	    sbText.setLength(0);
	    int textLength = text.length();
	    int totalSpace = (totalLength - textLength);
	    sbText.append(text);
	    for (int i = 0; i < totalSpace; i++)
	    {
		sbText.append(" ");
	    }
	}
	else
	{
	    sbText.setLength(0);
	    int textLength = text.length();
	    int totalSpace = (totalLength - textLength);
	    for (int i = 0; i < totalSpace; i++)
	    {
		sbText.append(" ");
	    }
	    sbText.append(text);
	}

	return sbText.toString();
    }

// Function to print documents    
    public void funPrintReportToPrinter(String printerName, String fileName)
    {
	try
	{

	    if ("windows".equalsIgnoreCase(clsPosConfigFile.gPrintOS) && clsGlobalVarClass.gPrintType.equalsIgnoreCase("Text File"))
	    {

		PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
		DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;

		int printerIndex = 0;
		PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
		for (int i = 0; i < printService.length; i++)
		{

		    if (printerName.equalsIgnoreCase(printService[i].getName()))
		    {
			printerIndex = i;
			break;
		    }
		}

		DocPrintJob job = printService[printerIndex].createPrintJob();
		FileInputStream fis = new FileInputStream(fileName);
		DocAttributeSet das = new HashDocAttributeSet();
		Doc doc = new SimpleDoc(fis, flavor, das);
		job.print(doc, pras);
	    }
	    else if ("linux".equalsIgnoreCase(clsPosConfigFile.gPrintOS) && clsGlobalVarClass.gPrintType.equalsIgnoreCase("Text File"))
	    {
		Process process = Runtime.getRuntime().exec("lpr -P " + printerName + " " + fileName, null);
	    }
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
    }

    public int funBackupAndMailDB(String backupFilePath) throws Exception
    {
	String filePath = System.getProperty("user.dir") + "\\DBBackup\\" + backupFilePath + ".sql";
	//String filePath = System.getProperty("user.dir")+"/DBBackup/1.sql";
	File file = new File(filePath);
	double bytes = file.length();
	double kilobytes = (bytes / 1024);
	double megabytes = (kilobytes / 1024);

	if (megabytes < 25)
	{
	    new clsSendMail().funSendMail("sanguineapos@gmail.com", filePath);
	}
	return 1;
    }

    public boolean funValidateDateFormat(String format, String date)
    {
	boolean flgDateValidation = false;
	try
	{
	    java.util.Date dt = new SimpleDateFormat(format).parse(date);
	    flgDateValidation = true;
	}
	catch (Exception e)
	{
	    flgDateValidation = false;
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
	finally
	{
	    return flgDateValidation;
	}
    }

    public double funGetMinBillAmountForDelCharges(String buildingCode, String custTypeCode)
    {
	double minAmount = 0.00;
	try
	{
	    String sql = "select ifnull(min(dblBillAmount),0) from tblareawisedc "
		    + "where strBuildingCode='" + buildingCode + "' and strCustTypeCode='" + custTypeCode + "' ";
	    ResultSet rsAmount = dbMysql.executeResultSet(sql);
	    //System.out.println(sql);
	    if (rsAmount.next())
	    {
		minAmount = Double.parseDouble(rsAmount.getString(1));
	    }
	    rsAmount.close();
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	    minAmount = 0.00;
	}
	return minAmount;
    }

    /**
     * This method is used to get printer names
     *
     * @return vector vTemPrinterNames
     */
    public java.util.Vector funGetPrinterNames()
    {
	java.util.Vector vTemPrinterNames = new java.util.Vector();
	vTemPrinterNames.add("");
	try
	{
	    PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
	    DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE; // MY FILE IS .txt TYPE
	    PrintService[] printService = PrintServiceLookup.lookupPrintServices(flavor, pras);
	    for (int i = 0; i < printService.length; i++)
	    {
		//System.out.println("Printer Names= "+printService[i].getName());
		vTemPrinterNames.add(printService[i].getName());
	    }
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}

	//System.out.println("Size=="+vTemPrinterNames.size());
	return vTemPrinterNames;
    }

    public double funGetKOTAmtOnTable(String cardNo) throws Exception
    {
	double KOTAmt = 0;
	String tableNo = "";
	String sql = "select sum(dblAmount),strTableNo "
		+ " from tblitemrtemp "
		+ " where strCardNo='" + cardNo + "' and strNCKotYN='N' "
		+ " group by strTableNo;";
	ResultSet rsOpenKOTs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	if (rsOpenKOTs.next())
	{
	    KOTAmt += rsOpenKOTs.getDouble(1);
	    tableNo = rsOpenKOTs.getString(2);
	}
	rsOpenKOTs.close();

	if (!cardNo.isEmpty())
	{
	    sql = "select sum(dblTaxAmt) "
		    + " from tblkottaxdtl "
		    + " where strTableNo='" + tableNo + "' "
		    + " group by strTableNo;";
	    rsOpenKOTs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsOpenKOTs.next())
	    {
		KOTAmt += rsOpenKOTs.getDouble(1);
	    }
	    rsOpenKOTs.close();
	}
	return KOTAmt;
    }

    public void funCreateExcelSheet(List<String> parameterList, List<String> headerList, Map<Integer, List<String>> map, List<String> totalList, String fileName)
    {
	File file = new File(clsPosConfigFile.exportReportPath + "\\" + fileName + ".xls");
	try
	{
	    WritableWorkbook workbook1 = Workbook.createWorkbook(file);
	    WritableSheet sheet1 = workbook1.createSheet("First Sheet", 0);
	    WritableFont cellFont = new WritableFont(WritableFont.COURIER, 14);
	    cellFont.setBoldStyle(WritableFont.BOLD);
	    WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
	    WritableFont headerCellFont = new WritableFont(WritableFont.TIMES, 10);
	    headerCellFont.setBoldStyle(WritableFont.BOLD);
	    WritableCellFormat headerCell = new WritableCellFormat(headerCellFont);

	    for (int j = 0; j <= parameterList.size(); j++)
	    {
		Label l0 = new Label(2, 0, parameterList.get(0), cellFormat);
		Label l1 = new Label(0, 2, parameterList.get(1), headerCell);
		Label l2 = new Label(1, 2, parameterList.get(2), headerCell);
		Label l3 = new Label(2, 2, parameterList.get(3), headerCell);
		Label l4 = new Label(0, 3, parameterList.get(4), headerCell);
		Label l5 = new Label(1, 3, parameterList.get(5), headerCell);

		sheet1.addCell(l0);
		sheet1.addCell(l1);
		sheet1.addCell(l2);
		sheet1.addCell(l3);
		sheet1.addCell(l4);
		sheet1.addCell(l5);
	    }

	    for (int j = 0; j < headerList.size(); j++)
	    {
		Label lblHeader = new Label(j, 5, headerList.get(j), headerCell);

		sheet1.addCell(lblHeader);
	    }

	    int i = 7;
	    for (Map.Entry<Integer, List<String>> entry : map.entrySet())
	    {
		Label lbl0 = new Label(0, i, entry.getKey().toString());
		List<String> nameList = map.get(entry.getKey());
		for (int j = 0; j < nameList.size(); j++)
		{
		    int colIndex = j + 1;
		    Label lblData = new Label(colIndex, i, nameList.get(j));
		    sheet1.addCell(lblData);
		    sheet1.setColumnView(i, 15);
		}
		sheet1.addCell(lbl0);
		i++;
	    }

	    for (int j = 0; j < totalList.size(); j++)
	    {
		String[] l0 = new String[10];
		for (int c = 0; c < totalList.size(); c++)
		{
		    l0 = totalList.get(c).split("#");
		    int pos = Integer.parseInt(l0[1]);
		    Label lable0 = new Label(pos, i + 1, l0[0], headerCell);
		    sheet1.addCell(lable0);
		}
		Label labelTotal = new Label(0, i + 1, "TOTAL:", headerCell);
		sheet1.addCell(labelTotal);
	    }
	    workbook1.write();
	    workbook1.close();

	    Desktop dt = Desktop.getDesktop();
	    dt.open(file);

	}
	catch (Exception ex)
	{
	    JOptionPane.showMessageDialog(null, ex.getMessage());
	    funShowDBConnectionLostErrorMessage(ex);
	    ex.printStackTrace();
	}
    }

    public void funCreateTempFolder()
    {
	try
	{
	    String filePath = System.getProperty("user.dir");
	    File file = new File(filePath + File.separator + "Temp");
	    if (!file.exists())
	    {
		file.mkdirs();
	    }
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
    }

    public void funPrintShowCardDtlTextfile(List<List<String>> arrListShowCardDtlTemp, String cardNo, String customerName, double totalAmt)
    {
	clsUtility objUtility = new clsUtility();
	try
	{
	    double totalBalance = 0;
	    funCreateTempFolder();
	    String filPath = System.getProperty("user.dir");
	    File textFile = new File(filPath + File.separator + "Temp" + File.separator + "Temp_Card_Dtl.txt");
	    PrintWriter pw = new PrintWriter(textFile);
	    pw.println(objUtility.funPrintTextWithAlignment("Card Transaction Detail", 40, "Center"));
	    pw.println(" ");
	    pw.print(objUtility.funPrintTextWithAlignment(clsGlobalVarClass.gPOSName, 40, "Center"));
	    pw.println(" ");
	    pw.print(objUtility.funPrintTextWithAlignment(clsGlobalVarClass.gClientName, 40, "Center"));
	    pw.println(" ");
	    pw.println(" ");
	    pw.print(objUtility.funPrintTextWithAlignment("Date", 8, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment(":", 4, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment(clsGlobalVarClass.gPOSDate, 28, "Left"));
	    pw.println(" ");
	    pw.print(objUtility.funPrintTextWithAlignment("Card No", 8, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment(":", 4, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment(cardNo, 28, "Left"));
	    pw.println(" ");

	    pw.print(objUtility.funPrintTextWithAlignment("Customer Name", 15, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment(":", 4, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment(customerName.toString(), 21, "Left"));
	    pw.println(" ");

	    pw.println("----------------------------------------");
	    pw.print(objUtility.funPrintTextWithAlignment("POS ", 8, "Left"));
	    pw.println(" ");
	    pw.print(objUtility.funPrintTextWithAlignment("", 6, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment("Trans No", 10, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment("Date", 12, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment("Type", 5, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment("Amt", 7, "RIGHT"));
	    pw.println(" ");
	    pw.println("----------------------------------------");
	    for (int cnt = 0; cnt < arrListShowCardDtlTemp.size(); cnt++)
	    {
		List<String> items = arrListShowCardDtlTemp.get(cnt);
		String amt = items.get(4);
		Double rechargeAmt = Double.valueOf(amt);
		pw.print(objUtility.funPrintTextWithAlignment(items.get(0), 8, "Left"));
		pw.println(" ");
		pw.print(objUtility.funPrintTextWithAlignment("", 6, "Left"));
		pw.print(objUtility.funPrintTextWithAlignment(items.get(1), 10, "Left"));
		pw.print(objUtility.funPrintTextWithAlignment(items.get(2), 12, "Left"));
		if (items.get(3).equals("Recharge"))
		{
		    pw.print(objUtility.funPrintTextWithAlignment("RC", 5, "Left"));
		}
		else if (items.get(3).equals("Redeem"))
		{
		    pw.print(objUtility.funPrintTextWithAlignment("RD", 5, "Left"));
		}
		else
		{
		    pw.print(objUtility.funPrintTextWithAlignment("RF", 5, "Left"));
		}

		pw.print(objUtility.funPrintTextWithAlignment("" + Math.rint(rechargeAmt), 7, "RIGHT"));
		pw.println(" ");
	    }

	    pw.println(" ");
	    pw.println("----------------------------------------");
	    pw.println(" ");
	    pw.print(objUtility.funPrintTextWithAlignment("", 27, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment("Total", 6, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment("" + totalAmt, 7, "RIGHT"));
	    pw.flush();
	    pw.close();

	    clsPrintingUtility objPrintingUtility = new clsPrintingUtility();
	    if (clsGlobalVarClass.gShowBill)
	    {
		objPrintingUtility.funShowTextFile(textFile, "", "");
	    }
	    objUtility.funPrintReportToPrinter(clsGlobalVarClass.gBillPrintPrinterPort, filPath);
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
    }

    /**
     * This method is used for create textfile for not linked item to web stock
     * product code
     *
     */
    public void funGenerateLinkupTextfile(ArrayList<ArrayList<String>> arrUnLinkedItemDtl, String fromDate, String toDate, String posName)
    {

	clsUtility objUtility = new clsUtility();
	try
	{
	    funCreateTempFolder();
	    String filePath = System.getProperty("user.dir");
	    filePath += File.separator + "Temp" + File.separator + "Temp_ItemUnLinkedItems.txt";
	    File textFile = new File(filePath);
	    PrintWriter pw = new PrintWriter(textFile);
	    pw.println(objUtility.funPrintTextWithAlignment(" UnLinked Items ", 40, "Center"));
	    pw.println(objUtility.funPrintTextWithAlignment(clsGlobalVarClass.gClientName, 40, "Center"));
	    pw.println(objUtility.funPrintTextWithAlignment(posName, 40, "Center"));
	    pw.println(" ");
	    pw.print(objUtility.funPrintTextWithAlignment("FromDate:", 10, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment(fromDate, 10, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment("", 2, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment("ToDate:", 8, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment(toDate, 10, "Left"));
	    pw.println(" ");
	    pw.println("________________________________________");
	    pw.print(objUtility.funPrintTextWithAlignment("ItemCode ", 15, "Left"));
	    pw.print(objUtility.funPrintTextWithAlignment("ItemName", 25, "Left"));
	    pw.println(" ");
	    pw.println("________________________________________");
	    pw.println(" ");

	    if (arrUnLinkedItemDtl.size() > 0)
	    {
		for (int cnt = 0; cnt < arrUnLinkedItemDtl.size(); cnt++)
		{
		    ArrayList<String> items = arrUnLinkedItemDtl.get(cnt);
		    pw.print(objUtility.funPrintTextWithAlignment("" + items.get(0) + " ", 15, "Left"));
		    pw.print(objUtility.funPrintTextWithAlignment("" + items.get(1), 25, "Left"));
		    pw.println(" ");
		}
	    }

	    pw.println(" ");
	    pw.println(" ");
	    pw.println(" ");
	    pw.println(" ");
	    pw.println("m");

	    pw.flush();
	    pw.close();

	    clsPrintingUtility objPrintingUtility = new clsPrintingUtility();
	    if (clsGlobalVarClass.gShowBill)
	    {
		objPrintingUtility.funShowTextFile(textFile, "", "");
	    }
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
    }

    public int funUpdateBillHdWithTaxValues(String billNo, List<clsTaxCalculationDtls> arrListTaxCal) throws Exception
    {
	double taxTotal = 0;
	for (clsTaxCalculationDtls objTaxCalDetails : arrListTaxCal)
	{
	    taxTotal += objTaxCalDetails.getTaxAmount();
	}
	String sqlUpdateBillHd = "update tblbillhd set dblTaxAmt='" + taxTotal + "' "
		+ "where strBillNo='" + billNo + "' and strClientCode='" + clsGlobalVarClass.gClientCode + "' ";
	clsGlobalVarClass.dbMysql.execute(sqlUpdateBillHd);

	return 1;
    }

    public int funUpdateBillDtlWithTaxValues(String billNo, String billType, String filterBillDate) throws Exception
    {
	Map<String, clsBillItemTaxDtl> hmBillItemTaxDtl = new HashMap<String, clsBillItemTaxDtl>();
	Map<String, clsBillItemTaxDtl> hmBillTaxDtl = new HashMap<String, clsBillItemTaxDtl>();

	String billDtl = "tblbilldtl";
	String billTaxDtl = "tblbilltaxdtl";
	String billModifierDtl = "tblbillmodifierdtl";

	if (billType.equalsIgnoreCase("QFile"))
	{
	    billDtl = "tblqbilldtl";
	    billTaxDtl = "tblqbilltaxdtl";
	    billModifierDtl = "tblqbillmodifierdtl";
	}

	String sql = "update " + billDtl + " set dblTaxAmount=0.00  "
		+ " where strBillNo='" + billNo + "' "
		+ " and date(dteBillDate)='" + filterBillDate + "' ";
	clsGlobalVarClass.dbMysql.execute(sql);

	sql = "select a.strTaxCode,b.dblPercent,b.strTaxIndicator,b.strTaxCalculation,b.strTaxOnGD,b.strTaxOnTax "
		+ " ,b.strTaxOnTaxCode,a.dblTaxAmount,a.dblTaxableAmount "
		+ " from " + billTaxDtl + " a,tbltaxhd b "
		+ " where a.strTaxCode=b.strTaxCode "
		+ " and a.strBillNo='" + billNo + "' "
		+ " and date(a.dteBillDate)='" + filterBillDate + "' "
		+ " and a.dblTaxAmount>0 "
		+ " and a.dblTaxableAmount>0 "
		+ " order by b.strTaxOnTax,b.strTaxCode; ";
	ResultSet rsBillTaxDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	while (rsBillTaxDtl.next())
	{
	    String taxCode = rsBillTaxDtl.getString(1);
	    String taxIndicator = rsBillTaxDtl.getString(3);
	    double taxPercentage = rsBillTaxDtl.getDouble(2);
	    String taxCalculation = rsBillTaxDtl.getString(4);
	    String taxOnGD = rsBillTaxDtl.getString(5);
	    String taxOnTax = rsBillTaxDtl.getString(6);
	    String taxOnTaxCode = rsBillTaxDtl.getString(7);
	    double billTaxAmt = rsBillTaxDtl.getDouble(8);
	    double billTaxableAmt = rsBillTaxDtl.getDouble(9);

	    sql = "select a.strItemCode,a.dblAmount,b.strTaxIndicator,a.strKOTNo,a.dblDiscountAmt "
		    + " from " + billDtl + " a,tblitemmaster b "
		    + " where a.strItemCode=b.strItemCode "
		    + " and a.strBillNo='" + billNo + "'"
		    + " and date(a.dteBillDate)='" + filterBillDate + "'  ";
	    ResultSet rsBillDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsBillDtl.next())
	    {
		String itemCode = rsBillDtl.getString(1);
		double itemAmt = rsBillDtl.getDouble(2);
		double itemDiscAmt = rsBillDtl.getDouble(5);
		String KOTNo = rsBillDtl.getString(4);
		double taxAmt = 0;
		if (taxOnGD.equalsIgnoreCase("Discount"))
		{
		    itemAmt -= itemDiscAmt;
		}

		sql = "select sum(dblAmount),sum(dblDiscAmt) "
			+ " from tblbillmodifierdtl "
			+ " where strBillNo='" + billNo + "' "
			+ " and left(strItemCode,7)='" + itemCode + "' "
			+ " and date(dteBillDate)='" + filterBillDate + "' "
			+ " group by left(strItemCode,7)";
		//System.out.println(sql);
		ResultSet rsModifierAmt = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		if (rsModifierAmt.next())
		{
		    itemAmt += rsModifierAmt.getDouble(1);
		    if (taxOnGD.equalsIgnoreCase("Discount"))
		    {
			itemAmt -= rsModifierAmt.getDouble(2);
		    }

		}
		rsModifierAmt.close();

		boolean isApplicable = isTaxApplicableOnItemGroup(taxCode, itemCode);
		if (isApplicable)
		{

		    if (taxOnTax.equals("Yes"))
		    {
			String keyForTaxOnTax = itemCode + "," + KOTNo + "," + taxOnTaxCode;
			if (hmBillTaxDtl.containsKey(keyForTaxOnTax))
			{

			    clsBillItemTaxDtl objBillItemTaxDtl1 = hmBillTaxDtl.get(keyForTaxOnTax);

			    taxAmt = (billTaxAmt / billTaxableAmt) * (itemAmt + objBillItemTaxDtl1.getDblTaxAmt());

			}
			else
			{

			    taxAmt = (billTaxAmt / billTaxableAmt) * (itemAmt);

			}

			clsBillItemTaxDtl objItemTaxDtl = new clsBillItemTaxDtl();
			objItemTaxDtl.setStrItemCode(itemCode);
			objItemTaxDtl.setDblTaxAmt(taxAmt);
			objItemTaxDtl.setStrKOTNo(KOTNo);
			objItemTaxDtl.setStrBillNo(billNo);

			String key2 = itemCode + "," + KOTNo + "," + taxCode;
			String key1 = itemCode + "," + KOTNo;
			hmBillTaxDtl.put(key2, objItemTaxDtl);

			clsBillItemTaxDtl objBillItemTaxDtl = new clsBillItemTaxDtl();
			objBillItemTaxDtl.setStrItemCode(itemCode);
			objBillItemTaxDtl.setDblTaxAmt(taxAmt);
			objBillItemTaxDtl.setStrKOTNo(KOTNo);
			objBillItemTaxDtl.setStrBillNo(billNo);
			if (hmBillItemTaxDtl.containsKey(key1))
			{
			    objBillItemTaxDtl = hmBillItemTaxDtl.get(key1);
			    objBillItemTaxDtl.setDblTaxAmt(objBillItemTaxDtl.getDblTaxAmt() + taxAmt);
			}
			hmBillItemTaxDtl.put(key1, objBillItemTaxDtl);
		    }
		    else
		    {

			taxAmt = (billTaxAmt / billTaxableAmt) * itemAmt;

			clsBillItemTaxDtl objItemTaxDtl = new clsBillItemTaxDtl();
			objItemTaxDtl.setStrItemCode(itemCode);
			objItemTaxDtl.setDblTaxAmt(taxAmt);
			objItemTaxDtl.setStrKOTNo(KOTNo);
			objItemTaxDtl.setStrBillNo(billNo);

			String key2 = itemCode + "," + KOTNo + "," + taxCode;
			String key1 = itemCode + "," + KOTNo;
			hmBillTaxDtl.put(key2, objItemTaxDtl);

			clsBillItemTaxDtl objBillItemTaxDtl = new clsBillItemTaxDtl();
			objBillItemTaxDtl.setStrItemCode(itemCode);
			objBillItemTaxDtl.setDblTaxAmt(taxAmt);
			objBillItemTaxDtl.setStrKOTNo(KOTNo);
			objBillItemTaxDtl.setStrBillNo(billNo);

			if (hmBillItemTaxDtl.containsKey(key1))
			{
			    objBillItemTaxDtl = hmBillItemTaxDtl.get(key1);
			    objBillItemTaxDtl.setDblTaxAmt(objBillItemTaxDtl.getDblTaxAmt() + taxAmt);
			}
			hmBillItemTaxDtl.put(key1, objBillItemTaxDtl);
		    }
		}
	    }
	    rsBillDtl.close();
	}
	rsBillTaxDtl.close();

	for (Map.Entry<String, clsBillItemTaxDtl> entry : hmBillItemTaxDtl.entrySet())
	{
	    sql = "update " + billDtl + " set dblTaxAmount = " + entry.getValue().getDblTaxAmt() + " "
		    + " where strBillNo='" + billNo + "' "
		    + " and strItemCode='" + entry.getValue().getStrItemCode() + "' "
		    + " and strKOTNo='" + entry.getValue().getStrKOTNo() + "' "
		    + " and date(dteBillDate)='" + filterBillDate + "' ";
	    clsGlobalVarClass.dbMysql.execute(sql);
	    //System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue().getStrItemCode() + " " + entry.getValue().getDblTaxAmt());
	}
	return 1;
    }

    public int funFillUserForms(String userCode) throws Exception
    {
	hmUserForms = new HashMap<String, String>();
	int ret = 0;
	String sql = "select strFormName,intSequence "
		+ " from tbluserdtl where strUserCode='" + userCode + "' "
		+ "and strGrant='true' ";
	ResultSet rsUserForms = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	while (rsUserForms.next())
	{
	    hmUserForms.put(rsUserForms.getString(1), rsUserForms.getString(2));
	}
	rsUserForms.close();

	return ret;
    }

// Arguments date(yyyy-MM-dd), required format (dd-MM-yyyy or dd/MM/yyyy)    
// Returns formatted date       
    public String funGetDateInFormat(String date, String requiredFormat) // Pass date in yyyy-MM-dd format only.
    {
	String formattedDate = "";
	String[] spDate = date.split("-");
	if (requiredFormat.equalsIgnoreCase("dd-MM-yyyy"))
	{
	    formattedDate = spDate[2] + "-" + spDate[1] + "-" + spDate[0];
	}
	else if (requiredFormat.equalsIgnoreCase("dd-MM-yyyy"))
	{
	    formattedDate = spDate[2] + "/" + spDate[1] + "/" + spDate[0];
	}
	return formattedDate;
    }

    public String funGenerateNextCode()
    {
	String code = "", transId = "";
	try
	{
	    int cn = 0;
	    ResultSet rrs = clsGlobalVarClass.dbMysql.executeResultSet("select count(*) from tblcashmanagement");
	    if (rrs.next())
	    {
		cn = rrs.getInt(1);
	    }
	    if (cn > 0)
	    {
		rrs = clsGlobalVarClass.dbMysql.executeResultSet("select max(strTransID) from tblcashmanagement");
		if (rrs.next())
		{
		    code = rrs.getString(1);
		}
		int length = code.length();
		String nextCode = code.substring(2, length);
		int nextCount = Integer.parseInt(nextCode);
		nextCount++;
		transId = "TR" + String.format("%05d", nextCount);
	    }
	    else
	    {
		transId = "TR00001";
	    }
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
	return transId;
    }

    public double funGetTotalDiscOnAmt(List<clsBillItemDtl> listOfDiscApplicableItems)
    {
	double totalDiscOnAmt = 0.00;
	for (int i = 0; i < listOfDiscApplicableItems.size(); i++)
	{
	    totalDiscOnAmt += listOfDiscApplicableItems.get(i).getAmount();
	}
	return totalDiscOnAmt;
    }

    public Map<String, clsBillDiscountDtl> funGetDiscMapDtl(String discountOnType, String discontOnValue, String discountTypePerOrAmt, double discPer, double discAmt, double dblDiscountOnAmt, List<clsBillItemDtl> listOfDiscApplicableItems, Map<String, clsBillItemDtl> hmBillItemDtl, String reasonCode, String discountRemarks)
    {
	Map<String, clsBillDiscountDtl> mapBillDiscDtl = new HashMap<String, clsBillDiscountDtl>();
	try
	{
	    for (int cnt = 0; cnt < listOfDiscApplicableItems.size(); cnt++)
	    {
		clsBillItemDtl objDiscItem = listOfDiscApplicableItems.get(cnt);
		String key = objDiscItem.getItemCode();
		if (objDiscItem.isIsModifier())
		{
		    key = key + "!" + objDiscItem.getItemName();
		}

		clsBillItemDtl objBillItemDtl = hmBillItemDtl.get(key);
		if (discountTypePerOrAmt.equals("Percent"))
		{
		    discAmt = objBillItemDtl.getAmount() * (discPer / 100);
		    DecimalFormat objDeciFormat = new DecimalFormat("###.##");
		    discAmt = discAmt / objBillItemDtl.getQuantity();
		    objBillItemDtl.setDiscountAmount(Double.parseDouble(objDeciFormat.format(discAmt)));
		    objBillItemDtl.setDiscountPercentage(Double.parseDouble(objDeciFormat.format(discPer)));
		}
		else
		{
		    discPer = (discAmt / dblDiscountOnAmt) * 100;
		    double discAmtForItem = objBillItemDtl.getAmount() * (discPer / 100);
		    DecimalFormat objDeciFormat = new DecimalFormat("###.##");
		    discAmtForItem = discAmtForItem / objBillItemDtl.getQuantity();
		    objBillItemDtl.setDiscountAmount(Double.parseDouble(objDeciFormat.format(discAmtForItem)));
		    objBillItemDtl.setDiscountPercentage(Double.parseDouble(objDeciFormat.format(discPer)));
		}
		hmBillItemDtl.put(key, objBillItemDtl);
	    }

	    if (discountTypePerOrAmt.equals("Percent"))
	    {
		discAmt = dblDiscountOnAmt * (discPer / 100);
		mapBillDiscDtl.put(discountOnType + "!" + discontOnValue, new clsBillDiscountDtl(discountRemarks, reasonCode, discPer, discAmt, dblDiscountOnAmt));
	    }
	    else
	    {
		discPer = (discAmt / dblDiscountOnAmt) * 100;
		mapBillDiscDtl.put(discountOnType + "!" + discontOnValue, new clsBillDiscountDtl(discountRemarks, reasonCode, discPer, discAmt, dblDiscountOnAmt));
	    }
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
	finally
	{
	    return mapBillDiscDtl;
	}
    }

    public String funGetPOSDateForTransaction()
    {
	StringBuilder sb = new StringBuilder(gPOSDateForTransaction);
	gPOSDateForTransaction = sb.delete(sb.indexOf(" "), sb.length()).toString();
	String currentTime = "";
	try
	{
	    ResultSet rsServerTime = clsGlobalVarClass.dbMysql.executeResultSet("select right(SYSDATE(),8)");
	    if (rsServerTime.next())
	    {
		currentTime = rsServerTime.getString(1);
	    }
	    rsServerTime.close();

	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
	gPOSDateForTransaction += " " + currentTime;
	return gPOSDateForTransaction;
    }

    public String funGetOnlyPOSDateForTransaction()
    {
	StringBuilder sb = new StringBuilder(gPOSDateForTransaction);
	gPOSOnlyDateForTransaction = sb.delete(sb.indexOf(" "), sb.length()).toString();
	return gPOSOnlyDateForTransaction;
    }

    public void funInvokeSampleJasper() throws Exception
    {
	HashMap hm = new HashMap();
	String reportName = "com/POSGlobal/reports/rptSampleJasper.jasper";
	ArrayList arrList = new ArrayList();
	arrList.add("Test");
	JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(arrList);
	InputStream is = this.getClass().getClassLoader().getResourceAsStream(reportName);
	JasperPrint print = JasperFillManager.fillReport(is, hm, beanColDataSource);

	JRViewer viewer = new JRViewer(print);
	JFrame jf = new JFrame();
	jf.getContentPane().add(viewer);
	jf.validate();

	JRPrintServiceExporter exporter = new JRPrintServiceExporter();

	//--- Set print properties
	PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
	printRequestAttributeSet.add(MediaSizeName.ISO_A4);
	PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
	String billPrinterName = clsGlobalVarClass.gBillPrintPrinterPort;
	billPrinterName = billPrinterName.replaceAll("#", "\\\\");
	printServiceAttributeSet.add(new PrinterName(billPrinterName, null));

	//--- Set print parameters      
	exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
	exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
	exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, printServiceAttributeSet);
	exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
	exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);

	//--- Print the document
	try
	{
	    exporter.exportReport();
	}
	catch (JRException e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}

    }

    public String funGetDayOfWeek(int day)
    {
	String dayOfWeek = "";
	switch (day)
	{
	    case 0:
		dayOfWeek = "Sunday";
		break;

	    case 1:
		dayOfWeek = "Monday";
		break;

	    case 2:
		dayOfWeek = "Tuesday";
		break;

	    case 3:
		dayOfWeek = "Wednesday";
		break;

	    case 4:
		dayOfWeek = "Thursday";
		break;

	    case 5:
		dayOfWeek = "Friday";
		break;

	    case 6:
		dayOfWeek = "Saturday";
		break;
	}

	return dayOfWeek;
    }

    public String funGetAlphabet(int no)
    {
	String[] alphabets
		=
		{
		    "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
		};
	return alphabets[no];
    }

    public String funUpdateTableStatusToInrestoApp(String tableNo, String tableName, String tableSts) throws Exception
    {
	String retValue = "";
	JSONObject objJson = new JSONObject();
	int tableStatus = 1;//unavailable

	if (!tableNo.isEmpty())
	{
	    if (tableSts.equalsIgnoreCase("Normal"))
	    {
		tableStatus = 2;//free()Normal
	    }
	    else if (tableSts.equalsIgnoreCase("Occupied"))
	    {
		tableStatus = 3;//occupied
	    }
	    else if (tableSts.equalsIgnoreCase("Billed"))
	    {
		tableStatus = 4;//billing
	    }
	    objJson.put("tablename", tableName);
	    objJson.put("status", tableStatus);
	    objJson.put("restID", clsGlobalVarClass.gInrestoPOSId);

	    String hoURL = clsGlobalVarClass.gInrestoPOSWebServiceURL + "/updatetablefrompos";

	    URL url = new URL(hoURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    OutputStream os = conn.getOutputStream();
	    os.write(objJson.toString().getBytes());
	    os.flush();
	    if (conn.getResponseCode() != HttpURLConnection.HTTP_OK)
	    {
		throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
	    }
	    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	    String output = "";

	    while ((output = br.readLine()) != null)
	    {
		retValue += output;
	    }
	    conn.disconnect();
	}

	System.out.println("Table Status= " + tableSts + "\tRet Value= " + retValue);

	return retValue;
    }

    public String funGetFromToDate(Date date)
    {
	String rtDate = null;
	try
	{
	    int d = date.getDate();
	    int m = date.getMonth() + 1;
	    int y = date.getYear() + 1900;
	    rtDate = y + "-" + m + "-" + d;
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
	finally
	{
	    return rtDate;
	}
    }

    public String funGetPOSCodeFromPOSName(String posName)
    {
	String pos = null;
	try
	{
	    StringBuilder sb = new StringBuilder(posName);
	    int len = posName.length();
	    int lastInd = sb.lastIndexOf(" ");
	    pos = sb.substring(lastInd + 1, len).toString();
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
	finally
	{
	    return pos;
	}
    }

    public java.util.Date funGetDateToSetCalenderDate()
    {
	java.util.Date date = null;

	try
	{
	    date = new SimpleDateFormat("dd-MM-yyyy").parse(clsGlobalVarClass.gPOSDateToDisplay);
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();

	}
	return date;
    }

    public String funGetDateInString()
    {
	Date dt = new Date();
	String date = dt.getDate() + "-" + (dt.getMonth() + 1) + "-" + (dt.getYear() + 1900);
	return date;
    }

    public void funInsertIntoTblItemRTempBck(String tableNo, String kotNo)
    {
	try
	{

	    String sql = "select strSerialNo from tblitemrtemp ";
	    ResultSet rsIsExistsTable = clsGlobalVarClass.dbMysql.executeResultSet(sql);//to check is exists or not tblitemrtemp

	    sql = "delete from tblitemrtemp_bck where strTableNo='" + tableNo + "' and strKOTNo='" + kotNo + "' ";
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "insert into tblitemrtemp_bck (select * from tblitemrtemp where strTableNo='" + tableNo + "' and strKOTNo='" + kotNo + "' )";
	    clsGlobalVarClass.dbMysql.execute(sql);
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
    }

    public void funInsertIntoTblItemRTempBck(String tableNo)
    {
	try
	{

	    String sql = "select strSerialNo from tblitemrtemp ";
	    ResultSet rsIsExistsTable = clsGlobalVarClass.dbMysql.executeResultSet(sql);//to check is exists or not tblitemrtemp

	    sql = "delete from tblitemrtemp_bck where strTableNo='" + tableNo + "'  ";
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "insert into tblitemrtemp_bck (select * from tblitemrtemp where strTableNo='" + tableNo + "'  )";
	    clsGlobalVarClass.dbMysql.execute(sql);
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
    }

    public void funInsertIntoTblItemRTempBckForMergeKOTs(String posCode, String mergeKOTs)
    {
	try
	{

	    String sql = "select strSerialNo from tblitemrtemp ";
	    ResultSet rsIsExistsTable = clsGlobalVarClass.dbMysql.executeResultSet(sql);//to check is exists or not tblitemrtemp

	    sql = "delete from tblitemrtemp_bck where strPOSCode='" + posCode + "' and " + mergeKOTs + "  ";
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "insert into tblitemrtemp_bck (select * from tblitemrtemp where strPOSCode='" + posCode + "' and " + mergeKOTs + "  )";
	    clsGlobalVarClass.dbMysql.execute(sql);
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
    }

    public void funReCalculateDiscountForBill(String transactionName, String qOrLiveFile, String posCode, String clientCode, String oldBillNo, String newBillNo, List<clsBillDtl> listOfItems)
    {
	try
	{
	    clsUtility objUtility = new clsUtility();

	    String tblBillHd = "tblbillhd";
	    String tblBillDtl = "tblbilldtl";
	    String tblBillModifierDtl = "tblbillmodifierdtl";
	    String tblBillDiscDtl = "tblbilldiscdtl";
	    if (qOrLiveFile.equalsIgnoreCase("QFile"))
	    {
		tblBillHd = "tblqbillhd";
		tblBillDtl = "tblqbilldtl";
		tblBillModifierDtl = "tblqbillmodifierdtl";
		tblBillDiscDtl = "tblqbilldiscdtl";
	    }

	    StringBuilder sqlBuilder = new StringBuilder();
	    StringBuilder sqlFilter = new StringBuilder();

	    sqlBuilder.append("select a.strBillNo,a.strPOSCode,a.dblDiscAmt,a.dblDiscPer,a.dblDiscOnAmt,a.strDiscOnType,a.strDiscOnValue,a.strDiscReasonCode,a.strDiscRemarks "
		    + "from " + tblBillDiscDtl + " a "
		    + "where a.strBillNo='" + oldBillNo + "' "
		    + "and a.strPOSCode='" + posCode + "' "
		    + "and  a.strClientCode='" + clientCode + "' ");
	    ResultSet rsBillDiscDtl = clsGlobalVarClass.dbMysql.executeResultSet(sqlBuilder.toString());
	    while (rsBillDiscDtl.next())
	    {
		sqlBuilder.setLength(0);
		sqlFilter.setLength(0);

		double discPer = rsBillDiscDtl.getDouble(4);
		double oldDiscAmt = rsBillDiscDtl.getDouble(3);
		double oldDiscOnAmt = rsBillDiscDtl.getDouble(5);
		String discOnType = rsBillDiscDtl.getString(6);
		String discOnValue = rsBillDiscDtl.getString(7);
		String reasonCode = rsBillDiscDtl.getString(8);
		String remarks = rsBillDiscDtl.getString(9);

		String groupCode = null;
		String subGroupCode = null;
		String itemCode = null;

		if (discOnType.equalsIgnoreCase("GroupWise"))
		{
		    groupCode = funGetGroupCode(discOnValue);
		    sqlFilter.append("and c.strGroupCode='" + groupCode + "' ");
		}
		else if (discOnType.equalsIgnoreCase("SubGroupWise"))
		{
		    subGroupCode = funGetSubGroupCode(discOnValue);
		    sqlFilter.append("and b.strSubGroupCode='" + subGroupCode + "' ");
		}
		else if (discOnType.equalsIgnoreCase("ItemWise"))
		{
		    itemCode = funGetItemCode(discOnValue);
		    sqlFilter.append("and a.strItemCode='" + itemCode + "' ");
		}
		else if (discOnType.equalsIgnoreCase("Total"))
		{
		    //total                    
		}
		sqlBuilder.append("select a.strItemCode,a.strItemName "
			+ "from tblitemmaster a,tblsubgrouphd b,tblgrouphd c "
			+ "where a.strSubGroupCode=b.strSubGroupCode "
			+ "and b.strGroupCode=c.strGroupCode "
			+ "and a.strDiscountApply='Y' ");
		sqlFilter.append("and a.strItemCode IN " + funGetItemCodeList(listOfItems) + " ");
		sqlBuilder.append(sqlFilter);

		double newDiscOnAmt = 0.00, newDiscAmt = 0.00;

		ResultSet rsBillDtl = clsGlobalVarClass.dbMysql.executeResultSet(sqlBuilder.toString());
		while (rsBillDtl.next())
		{
		    for (int i = 0; i < listOfItems.size(); i++)
		    {
			if (rsBillDtl.getString(1).equals(listOfItems.get(i).getStrItemCode().substring(0, 7)))
			{
			    newDiscOnAmt += listOfItems.get(i).getDblAmount();
			    newDiscAmt += listOfItems.get(i).getDblDiscountAmt();
			}
		    }
		}
		rsBillDtl.close();

		if (transactionName.equalsIgnoreCase("MenuHeadWiseItemDeletion"))
		{
		    if (newDiscOnAmt > 0)
		    {
			clsGlobalVarClass.dbMysql.execute("update " + tblBillDiscDtl + " "
				+ "set dblDiscAmt='" + newDiscAmt + "',dblDiscOnAmt='" + newDiscOnAmt + "' "
				+ "where strBillNo='" + oldBillNo + "' "
				+ "and strDiscOnType='" + discOnType + "' "
				+ "and strDiscOnValue='" + discOnValue + "' ");
		    }
		    else
		    {
			clsGlobalVarClass.dbMysql.execute("delete  from " + tblBillDiscDtl + " "
				+ "where strBillNo='" + oldBillNo + "' "
				+ "and strDiscOnType='" + discOnType + "' "
				+ "and strDiscOnValue='" + discOnValue + "' ");
		    }
		}
		else if (transactionName.equalsIgnoreCase("SplitBill"))
		{
		    if (newDiscOnAmt > 0)
		    {
			clsGlobalVarClass.dbMysql.execute("insert into tblbilldiscdtl "
				+ "(select '" + newBillNo + "',a.strPOSCode,'" + newDiscAmt + "',a.dblDiscPer,'" + newDiscOnAmt + "' "
				+ ",a.strDiscOnType,a.strDiscOnValue,a.strDiscReasonCode,a.strDiscRemarks"
				+ ",a.strUserCreated,'" + clsGlobalVarClass.gUserCode + "',a.dteDateCreated,'" + clsGlobalVarClass.getPOSDateForTransaction() + "' "
				+ ",a.strClientCode,a.strDataPostFlag "
				+ "from " + tblBillDiscDtl + " a "
				+ "where a.strBillNo='" + oldBillNo + "' "
				+ "and a.strPOSCode='" + posCode + "' "
				+ "and a.strDiscOnType='" + discOnType + "' "
				+ "and a.strDiscOnValue='" + discOnValue + "' "
				+ "and a.strClientCode='" + clientCode + "') ");
		    }
		}
		else if (transactionName.equalsIgnoreCase("Void Bill"))
		{
		    String sql = "delete from tblvoidbilldiscdtl where strBillNo='" + oldBillNo + "' "
			    + "and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' "
			    + "and strDiscOnType='" + discOnType + "' "
			    + "and strDiscOnValue='" + discOnValue + "' "
			    + "and strTransType='VB' ";
		    clsGlobalVarClass.dbMysql.execute(sql);

		    newDiscAmt = newDiscOnAmt * (discPer / 100);

		    sql = "insert into tblvoidbilldiscdtl "
			    + "(select a.strBillNo,a.strPOSCode,'" + newDiscAmt + "',a.dblDiscPer,'" + newDiscOnAmt + "' "
			    + ",a.strDiscOnType,a.strDiscOnValue,a.strDiscReasonCode,a.strDiscRemarks"
			    + ",a.strUserCreated,'" + clsGlobalVarClass.gUserCode + "',a.dteDateCreated,'" + clsGlobalVarClass.getPOSDateForTransaction() + "' "
			    + ",a.strClientCode,a.strDataPostFlag,'" + clsGlobalVarClass.getPOSDateForTransaction() + "','VB' "
			    + "from " + tblBillDiscDtl + " a "
			    + "where a.strBillNo = '" + oldBillNo + "' "
			    + "and date(a.dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' "
			    + "and a.strPOSCode='" + posCode + "' "
			    + "and a.strDiscOnType='" + discOnType + "' "
			    + "and a.strDiscOnValue='" + discOnValue + "' "
			    + "and a.strClientCode='" + clientCode + "') ";

		    clsGlobalVarClass.dbMysql.execute(sql);
		    //System.out.println(sql);
		}
	    }

	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
    }

    private String funGetGroupCode(String groupName)
    {
	String groupCode = null;
	try
	{
	    ResultSet rsSubGroupCode = clsGlobalVarClass.dbMysql.executeResultSet("select a.strGroupCode from tblgrouphd a where a.strGroupName='" + groupName + "' ");
	    if (rsSubGroupCode.next())
	    {
		groupCode = rsSubGroupCode.getString(1);
	    }
	    rsSubGroupCode.close();
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
	finally
	{
	    return groupCode;
	}
    }

    private String funGetSubGroupCode(String subGroupName)
    {
	String subGroupCode = null;
	try
	{
	    ResultSet rsSubGroupCode = clsGlobalVarClass.dbMysql.executeResultSet("select a.strSubGroupCode from tblsubgrouphd a where a.strSubGroupName='" + subGroupName + "' ");
	    if (rsSubGroupCode.next())
	    {
		subGroupCode = rsSubGroupCode.getString(1);
	    }
	    rsSubGroupCode.close();
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
	finally
	{
	    return subGroupCode;
	}
    }

    private String funGetItemCode(String itemName)
    {
	String itemCode = null;
	try
	{
	    ResultSet rsSubGroupCode = clsGlobalVarClass.dbMysql.executeResultSet("select a.strItemCode from tblitemmaster a where a.strItemNAme='" + itemName + "' ");
	    if (rsSubGroupCode.next())
	    {
		itemCode = rsSubGroupCode.getString(1);
	    }
	    rsSubGroupCode.close();
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
	finally
	{
	    return itemCode;
	}
    }

    private String funGetItemCodeList(List<clsBillDtl> listOfItems)
    {
	StringBuilder itemCodeBuilder = new StringBuilder();
	try
	{
	    itemCodeBuilder.append("(");
	    for (int i = 0; i < listOfItems.size(); i++)
	    {
		if (i == 0)
		{
		    itemCodeBuilder.append("'" + listOfItems.get(i).getStrItemCode() + "'");
		}
		else
		{
		    itemCodeBuilder.append(",'" + listOfItems.get(i).getStrItemCode() + "'");
		}
	    }
	    itemCodeBuilder.append(")");
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
	finally
	{
	    return itemCodeBuilder.toString();
	}
    }

    public void funReCalculateTaxForBill(String billNo, String posCode, HashMap<String, clsSettelementOptions> hmSettlemetnOptions, String filterBillDate)
    {
	try
	{
	    List<clsItemDtlForTax> arrListItemDtls = new ArrayList<clsItemDtlForTax>();
	    double subTotal = 0.00, totalDiscAmt = 0.00;
	    String sql = "select  a.strItemCode,a.strItemName,a.dblAmount,a.dblDiscountAmt "
		    + "from tblbilldtl a "
		    + "where strBillNo='" + billNo + "' "
		    + "and date(dteBillDate)='" + filterBillDate + "' ";
	    ResultSet rsbillDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsbillDtl.next())
	    {
		subTotal = subTotal + rsbillDtl.getDouble(3);
		totalDiscAmt = totalDiscAmt + rsbillDtl.getDouble(4);

		clsItemDtlForTax objItemDtlForTax = new clsItemDtlForTax();

		objItemDtlForTax.setItemCode(rsbillDtl.getString(1));
		objItemDtlForTax.setItemName(rsbillDtl.getString(1));
		objItemDtlForTax.setAmount(rsbillDtl.getDouble(3));
		objItemDtlForTax.setDiscAmt(rsbillDtl.getDouble(4));

		arrListItemDtls.add(objItemDtlForTax);
	    }
	    rsbillDtl.close();

	    sql = "select  a.strItemCode,a.strModifierName,a.dblAmount,a.dblDiscAmt "
		    + "from tblbillmodifierdtl a "
		    + "where strBillNo='" + billNo + "' "
		    + "and date(dteBillDate)='" + filterBillDate + "' ";
	    rsbillDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsbillDtl.next())
	    {
		subTotal = subTotal + rsbillDtl.getDouble(3);
		totalDiscAmt = totalDiscAmt + rsbillDtl.getDouble(4);

		clsItemDtlForTax objItemDtlForTax = new clsItemDtlForTax();

		objItemDtlForTax.setItemCode(rsbillDtl.getString(1));
		objItemDtlForTax.setItemName(rsbillDtl.getString(1));
		objItemDtlForTax.setAmount(rsbillDtl.getDouble(3));
		objItemDtlForTax.setDiscAmt(rsbillDtl.getDouble(4));

		arrListItemDtls.add(objItemDtlForTax);
	    }
	    rsbillDtl.close();

	    sql = "select date(a.dteBillDate),a.strPOSCode,a.strOperationType,a.strAreaCode,a.strClientCode  "
		    + "from tblbillhd a "
		    + "where strBillNo='" + billNo + "' "
		    + "and date(dteBillDate)='" + filterBillDate + "' ";
	    rsbillDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    String billDate = "", opearationType = "", areaCode = "";
	    if (rsbillDtl.next())
	    {
		billDate = rsbillDtl.getString(1);
		posCode = rsbillDtl.getString(2);
		opearationType = rsbillDtl.getString(3);
		if (opearationType.equalsIgnoreCase("DirectBiller"))
		{
		    opearationType = "DineIn";
		}
		areaCode = rsbillDtl.getString(4);
		clsGlobalVarClass.gClientCode = rsbillDtl.getString(5);
	    }
	    String settlementCodeForTax = "S01";
	    int intMaxTaxCountForSettlement = 0;
	    for (clsSettelementOptions objSettelementOptions : hmSettlemetnOptions.values())
	    {
		String settlementCode = objSettelementOptions.getStrSettelmentCode();

		if (hmSettlemetnOptions.size() == 1)
		{
		    settlementCodeForTax = settlementCode;
		}

		sql = "select count(a.strTaxCode) "
			+ "from tblsettlementtax a "
			+ "where a.strSettlementCode='" + settlementCode + "' "
			+ "and a.strApplicable='true' ";
		ResultSet rsMaxTaxCountForSettlement = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		if (rsMaxTaxCountForSettlement.next())
		{
		    if (rsMaxTaxCountForSettlement.getInt(1) > intMaxTaxCountForSettlement)
		    {
			intMaxTaxCountForSettlement = rsMaxTaxCountForSettlement.getInt(1);
			settlementCodeForTax = settlementCode;
		    }
		}
		rsMaxTaxCountForSettlement.close();
	    }
	    List<clsTaxCalculationDtls> arrListTaxCal = funCalculateTax(arrListItemDtls, posCode, billDate, areaCode, opearationType, subTotal, totalDiscAmt, "", settlementCodeForTax, "Sales");

	    List<clsBillTaxDtl> listObjBillTaxBillDtls = new ArrayList<clsBillTaxDtl>();
	    double totalTaxAmt = 0.00;
	    for (clsTaxCalculationDtls objTaxCalculationDtls : arrListTaxCal)
	    {
		double dblTaxAmt = objTaxCalculationDtls.getTaxAmount();
		totalTaxAmt = totalTaxAmt + dblTaxAmt;
		clsBillTaxDtl objBillTaxDtl = new clsBillTaxDtl();
		objBillTaxDtl.setStrBillNo(billNo);
		objBillTaxDtl.setStrTaxCode(objTaxCalculationDtls.getTaxCode());
		objBillTaxDtl.setDblTaxableAmount(objTaxCalculationDtls.getTaxableAmount());
		objBillTaxDtl.setDblTaxAmount(dblTaxAmt);
		objBillTaxDtl.setStrClientCode(clsGlobalVarClass.gClientCode);

		listObjBillTaxBillDtls.add(objBillTaxDtl);
	    }

	    funInsertBillTaxDtlTable(billNo, listObjBillTaxBillDtls);
	    funUpdateBillDtlWithTaxValues(billNo, "Live", filterBillDate);

	    double dblGrandTotal = 0.00 + subTotal - totalDiscAmt + totalTaxAmt;

	    //start code to calculate roundoff amount and round off by amt
	    Map<String, Double> mapRoundOff = new clsUtility2().funCalculateRoundOffAmount(dblGrandTotal);
	    dblGrandTotal = mapRoundOff.get("roundOffAmt");
	    double _grandTotalRoundOffBy = mapRoundOff.get("roundOffByAmt");
	    //end code to calculate roundoff amount and round off by amt
	    sql = "update tblbillhd "
		    + "set dblDiscountAmt='" + totalDiscAmt + "' "
		    + ",dblTaxAmt='" + totalTaxAmt + "' "
		    + ",dblSubTotal='" + subTotal + "' "
		    + ",dblGrandTotal='" + dblGrandTotal + "' "
		    + ",dblRoundOff='" + _grandTotalRoundOffBy + "' "
		    + "where strBillNo='" + billNo + "' "
		    + "and date(dteBillDate)='" + filterBillDate + "' ";
	    clsGlobalVarClass.dbMysql.execute(sql);
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
    }

    private int funInsertBillTaxDtlTable(String billNo, List<clsBillTaxDtl> listObjBillTaxDtl) throws Exception
    {
	int rows = 0;
	String sqlDelete = "delete from tblbilltaxdtl where strBillNo='" + billNo + "'";
	clsGlobalVarClass.dbMysql.execute(sqlDelete);

	for (clsBillTaxDtl objBillTaxDtl : listObjBillTaxDtl)
	{
	    String sqlInsertTaxDtl = "insert into tblbilltaxdtl "
		    + "(strBillNo,strTaxCode,dblTaxableAmount,dblTaxAmount,strClientCode,dteBillDate) "
		    + "values('" + objBillTaxDtl.getStrBillNo() + "','" + objBillTaxDtl.getStrTaxCode() + "'"
		    + "," + objBillTaxDtl.getDblTaxableAmount() + "," + objBillTaxDtl.getDblTaxAmount() + ""
		    + ",'" + clsGlobalVarClass.gClientCode + "','" + clsGlobalVarClass.getPOSDateForTransaction() + "')";
	    rows += clsGlobalVarClass.dbMysql.execute(sqlInsertTaxDtl);
	}
	return rows;
    }

    public void funSendSMS(String billno, String smsData, String transType)
    {
	try
	{
	    //String smsData=clsGlobalVarClass.gBillSettlementSMS;
	    String result = "", result1 = "", result2 = "", result3 = "", result4 = "", result5 = "", result6 = "", result7 = "";
	    String mainSms = "", sql = "";

	    if (transType.equalsIgnoreCase("Home Delivery"))
	    {
		sql = "select c.strCustomerName,c.longMobileNo,a.dblGrandTotal "
			+ " ,DATE_FORMAT(a.dteBillDate,'%d-%m-%Y'),time(a.dteBillDate) "
			+ " ,a.strUserCreated,ifnull(d.strDPName,'') "
			+ " from tblbillhd a,tblcustomermaster c ,tblhomedelivery b "
			+ " left outer join tbldeliverypersonmaster d on b.strDPCode=d.strDPCode "
			+ " where a.strBillNo='" + billno + "' and a.strBillNo=b.strBillNo "
			+ " and a.strCustomerCode=c.strCustomerCode ";
	    }
	    else
	    {
		sql = "select ifnull(c.strCustomerName,''),ifnull(c.longMobileNo,'NA')"
			+ " ,a.dblGrandTotal ,DATE_FORMAT(a.dteBillDate,'%d-%m-%Y')"
			+ " ,time(a.dteBillDate),a.strUserCreated,ifnull(d.strDPName,'') "
			+ " from tblbillhd a left outer join tblhomedelivery b on a.strBillNo=b.strBillNo "
			+ " left outer join tbldeliverypersonmaster d on b.strDPCode=d.strDPCode "
			+ " left outer join tblcustomermaster c on a.strCustomerCode=c.strCustomerCode "
			+ " where a.strBillNo='" + billno + "'";
	    }
	    //System.out.println(sql);
	    ResultSet rs_SqlGetSMSData = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rs_SqlGetSMSData.next())
	    {
		int intIndex = smsData.indexOf("%%BILL NO");
		if (intIndex != - 1)
		{
		    result = smsData.replaceAll("%%BILL NO", billno);
		    mainSms = result;
		}
		int intIndex1 = mainSms.indexOf("%%CUSTOMER NAME");

		if (intIndex1 != - 1)
		{
		    result1 = mainSms.replaceAll("%%CUSTOMER NAME", rs_SqlGetSMSData.getString(1));
		    mainSms = result1;
		}
		int intIndex2 = mainSms.indexOf("%%BILL AMT");

		if (intIndex2 != - 1)
		{
		    result2 = mainSms.replaceAll("%%BILL AMT", rs_SqlGetSMSData.getString(3));
		    mainSms = result2;
		}
		int intIndex3 = mainSms.indexOf("%%DATE");

		if (intIndex3 != - 1)
		{
		    result3 = mainSms.replaceAll("%%DATE", rs_SqlGetSMSData.getString(4));
		    mainSms = result3;
		}
		int intIndex4 = mainSms.indexOf("%%DELIVERY BOY");

		if (intIndex4 != - 1)
		{
		    result4 = mainSms.replaceAll("%%DELIVERY BOY", rs_SqlGetSMSData.getString(7));
		    mainSms = result4;
		}
		int intIndex5 = mainSms.indexOf("%%ITEMS");

		if (intIndex5 != - 1)
		{
		    StringBuilder sbItems = new StringBuilder();
		    sbItems.append("");
		    if (clsGlobalVarClass.gClientCode.equals("117.001"))//prems
		    {
			sql = "select a.strItemName from tblbilldtl a where a.strBillNo='" + billno + "' ";
			ResultSet rsItems = clsGlobalVarClass.dbMysql.executeResultSet(sql);
			while (rsItems.next())
			{
			    sbItems.append(rsItems.getString(1));
			    sbItems.append(",");
			}
			rsItems.close();
			sbItems.deleteCharAt(sbItems.lastIndexOf(","));
		    }

		    result5 = mainSms.replaceAll("%%ITEMS", sbItems.toString());
		    mainSms = result5;
		}
		int intIndex6 = mainSms.indexOf("%%USER");

		if (intIndex6 != - 1)
		{
		    result6 = mainSms.replaceAll("%%USER", rs_SqlGetSMSData.getString(6));
		    mainSms = result6;
		}
		int intIndex7 = mainSms.indexOf("%%TIME");

		if (intIndex7 != - 1)
		{
		    result7 = mainSms.replaceAll("%%TIME", rs_SqlGetSMSData.getString(5));
		    mainSms = result7;
		}

		String fromTelNo = clsGlobalVarClass.gClientTelNo;
		String[] sp = fromTelNo.split(",");
		if (sp.length > 0)
		{
		    fromTelNo = sp[0];
		}

		if (clsGlobalVarClass.gSMSType.equalsIgnoreCase("Cellx"))
		{
		    if (!rs_SqlGetSMSData.getString(2).isEmpty())
		    {
			//System.out.println(clsGlobalVarClass.gSMSApi);
			//System.out.println(mainSms);
			String smsURL = clsGlobalVarClass.gSMSApi.replace("<to>", rs_SqlGetSMSData.getString(2)).replace("<from>", fromTelNo).replace("<MSG>", mainSms).replaceAll(" ", "%20");
			//System.out.println(smsURL);
			//System.out.println(clsGlobalVarClass.funSendSMS(smsURL));
		    }
		}
		else if (clsGlobalVarClass.gSMSType.equalsIgnoreCase("Sinfini"))
		{
		    String smsURL = clsGlobalVarClass.gSMSApi.replace("<PHONE>", rs_SqlGetSMSData.getString(2)).replace("<MSG>", mainSms).replaceAll(" ", "%20");
		    funSendSMS(smsURL);
		}
		else if (clsGlobalVarClass.gSMSType.equalsIgnoreCase("Infyflyer"))
		{
		    //http://sms.infiflyer.co.in/httpapi/httpapi?token=a10bad827db08a4eeec726da63813747&sender=IPREMS&number=<PHONE>&route=2&type=1&sms=<MSG>
		    String smsURL = clsGlobalVarClass.gSMSApi.replace("<PHONE>", rs_SqlGetSMSData.getString(2)).replace("<MSG>", mainSms).replaceAll(" ", "%20");
		    funSendSMS(smsURL);
		}
	    }
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	     e.printStackTrace();
	}
    }

    public String funMakeTransaction(String Data, String requestType, String mid, String tid, String Amount, String Environment, String IP, String PORT)
    {
	//Socket Base transaction start
	try
	{
	    String host = IP;	//IP address of the server
	    int port = Integer.parseInt(PORT);	//Port on which the socket is going to connect
	    String secretKey = clsGlobalVarClass.gJioMoneyActivationCode; //"activate code";	
	    //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	    clsJioMoneyEncryption objJio = new clsJioMoneyEncryption();
	    String response = "";
	    StringBuilder Res = new StringBuilder();
	    String encryptedData = objJio.encrypt(Data, secretKey);
	    /*
	     * String finalRequest = encryptedData.toString() + "|" +
	     * request.getMid() + "|" + request.getTid() + "|" +
	     * request.getAmount() + "|" + request.getRequestType() + "|" +
	     * "TESTING" + "|" + null;
	     */
	    String SendData = encryptedData + "|" + mid + "|" + tid + "|" + Amount + "|" + requestType + "|" + Environment + "|" + null;
	    System.out.println("Request String:" + SendData);
	    try (Socket s = new Socket(host, port)) //Creating socket class
	    {
		DataOutputStream dout = new DataOutputStream(s.getOutputStream());	//creating outputstream to send data to server
		DataInputStream din = new DataInputStream(s.getInputStream());	//creating inputstream to receive data from server
		dout.writeUTF(SendData);	//Sending the finalRequest String to server
		//System.out.println("Request sent to Server...");
		dout.flush();	//Flush the streams

		byte[] bs = new byte[10024];
		din.read(bs);

		char c;
		for (byte b : bs)
		{
		    c = (char) b;
		    response = Res.append("").append(c).toString();
		    //response = c+;
		    // System.out.println("Server Response:\n" + response);
		}

		dout.close();	//Closing the output stream
		din.close();	//Closing the input stream
	    } //creating outputstream to send data to server

	    String strRes = response.trim();
	    JSONParser jsonParser = new JSONParser();
	    JSONObject jsonObject = (JSONObject) jsonParser.parse(strRes);
	    String Token = (String) jsonObject.get("newToken");
	    if (Token != null)
	    {
		SetToken(Token, secretKey);
	    }
	    return response;
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    System.out.println("Exception:" + e);
	    return null;
	}
	////Socket Base transaction end
    }

    public void SetToken(String newToken, String OldToken)
    {
	// TODO add your handling code here:
	try
	{
	    clsJioMoneyEncryption objDecpt = new clsJioMoneyEncryption();
	    String getToken = objDecpt.Decrypt(newToken, OldToken);
	    clsGlobalVarClass.gJioMoneyActivationCode = getToken;
	    String sql = "update tblsetup set strJioActivationCode='" + getToken + "' "
		    + "where strPOSCode='" + clsGlobalVarClass.gPOSCode + "' and strJioMID='" + clsGlobalVarClass.gJioMoneyMID + "'";
	    clsGlobalVarClass.dbMysql.execute(sql);
	}
	catch (Exception ex)
	{
	    funShowDBConnectionLostErrorMessage(ex);
	    
	}
    }

    public void funStopSocket()
    {
	try
	{
	    Runtime.getRuntime().exec("taskkill /f /im cmd.exe");
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
    }

//For JioMoney Integration.....
    //Start Socket
    public void funStartSocketBat()
    {
	try
	{
	    funStopSocket();
	    BufferedWriter objToken = new BufferedWriter(new FileWriter("SocketServer.bat"));
	    String Path = System.getProperty("user.dir");
	    String Script = "\"" + Path + "\\JioMoneySocket.exe" + "\"" + " /Protocol IPv4 /Port " + "5150";
	    objToken.write(Script);
	    objToken.close();
	    Runtime.getRuntime().exec("cmd /c start SocketServer.bat");
	}
	catch (IOException e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
    }

    public String funGetHostName()
    {
	String hostName = "";
	try
	{
	    InetAddress ipAddress = InetAddress.getLocalHost();
	    hostName = ipAddress.getHostName();
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
	finally
	{
	    return hostName;
	}
    }

    public String funGetCurrentMACAddress()
    {
	StringBuilder currentMACAddress = new StringBuilder();
	try
	{
	    InetAddress ipAddress = InetAddress.getLocalHost();
	    String hostName = ipAddress.getHostName();

	    //System.out.println("hostName->" + hostName);
	    NetworkInterface network = NetworkInterface.getByInetAddress(ipAddress);

	    byte[] mac = network.getHardwareAddress();

	    //System.out.print("Current MAC address : ");
	    for (int i = 0; i < mac.length; i++)
	    {
		currentMACAddress.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
	    }
	    //System.out.println(currentMACAddress.toString());
	}
	catch (Exception e)
	{
	    //e.printStackTrace();
	    funShowDBConnectionLostErrorMessage(e);
	    System.out.println("No LocalArea Network(LAN) Available.");
	}
	finally
	{
	    return currentMACAddress.toString();
	}
    }

    private void funInsertQBillDataForNoBillSeries(String posCode)
    {
	try
	{
	    String sqlAdvRecDtl = "delete from tblqadvancereceiptdtl "
		    + " where strReceiptNo in (select strReceiptNo from tbladvancereceipthd "
		    + " where strAdvBookingNo in (select strAdvBookingNo from tblbillhd "
		    + " where strPOSCode='" + posCode + "' "
		    + " and strClientCode='" + clsGlobalVarClass.gClientCode + "'))";
	    clsGlobalVarClass.dbMysql.execute(sqlAdvRecDtl);
	    sqlAdvRecDtl = "insert into tblqadvancereceiptdtl "
		    + "(select * from tbladvancereceiptdtl "
		    + " where strReceiptNo in (select strReceiptNo from tbladvancereceipthd "
		    + " where strAdvBookingNo in (select strAdvBookingNo from tblbillhd "
		    + " where strPOSCode='" + posCode + "' "
		    + " and strClientCode='" + clsGlobalVarClass.gClientCode + "')))";
	    clsGlobalVarClass.dbMysql.execute(sqlAdvRecDtl);
	    sqlAdvRecDtl = "delete from tbladvancereceiptdtl "
		    + " where strReceiptNo in (select strReceiptNo from tbladvancereceipthd "
		    + " where strAdvBookingNo in (select strAdvBookingNo from tblbillhd "
		    + " where strPOSCode='" + posCode + "' "
		    + " and strClientCode='" + clsGlobalVarClass.gClientCode + "'))";
	    clsGlobalVarClass.dbMysql.execute(sqlAdvRecDtl);
	    //System.out.println("Adv Rec Dtl");

	    String sqlAdvRecHd = "delete from tblqadvancereceipthd where strReceiptNo in "
		    + " (select strReceiptNo from tbladvancereceipthd "
		    + " where strAdvBookingNo in "
		    + " (select strAdvBookingNo from tblbillhd "
		    + " where strPOSCode='" + posCode + "' "
		    + " and strClientCode='" + clsGlobalVarClass.gClientCode + "'))";
	    //System.out.println(sqlAdvRecHd);
	    clsGlobalVarClass.dbMysql.execute(sqlAdvRecHd);
	    sqlAdvRecHd = "insert into tblqadvancereceipthd "
		    + "(select * from tbladvancereceipthd "
		    + " where strAdvBookingNo in (select strAdvBookingNo from tblbillhd "
		    + " where strPOSCode='" + posCode + "' "
		    + " and strClientCode='" + clsGlobalVarClass.gClientCode + "'))";
	    //System.out.println(sqlAdvRecHd);
	    clsGlobalVarClass.dbMysql.execute(sqlAdvRecHd);
	    sqlAdvRecHd = "delete from tbladvancereceipthd where strAdvBookingNo in "
		    + " (select strAdvBookingNo from tblbillhd "
		    + " where strPOSCode='" + posCode + "' "
		    + " and strClientCode='" + clsGlobalVarClass.gClientCode + "')";
	    //System.out.println(sqlAdvRecHd);
	    clsGlobalVarClass.dbMysql.execute(sqlAdvRecHd);
	    //System.out.println("Adv Rec Hd");

	    String sqlAdvBookDtl = "delete from tblqadvbookbilldtl where strAdvBookingNo in "
		    + " (select strAdvBookingNo from tblbillhd "
		    + " where strPOSCode='" + posCode + "' "
		    + " and strClientCode='" + clsGlobalVarClass.gClientCode + "')";
	    clsGlobalVarClass.dbMysql.execute(sqlAdvBookDtl);
	    sqlAdvBookDtl = "insert into tblqadvbookbilldtl "
		    + " (select * from tbladvbookbilldtl where strAdvBookingNo in "
		    + " (select strAdvBookingNo from tblbillhd "
		    + " where strPOSCode='" + posCode + "' "
		    + " and strClientCode='" + clsGlobalVarClass.gClientCode + "'))";
	    clsGlobalVarClass.dbMysql.execute(sqlAdvBookDtl);
	    sqlAdvBookDtl = "delete from tbladvbookbilldtl where strAdvBookingNo in "
		    + " (select strAdvBookingNo from tblbillhd "
		    + " where strPOSCode='" + posCode + "' "
		    + " and strClientCode='" + clsGlobalVarClass.gClientCode + "')";
	    clsGlobalVarClass.dbMysql.execute(sqlAdvBookDtl);
	    //System.out.println("Adv Dtl");

	    String sqlAdvBookCharDtl = "delete from tbladvbookbillchardtl where strAdvBookingNo in "
		    + " (select strAdvBookingNo from tblbillhd "
		    + " where strPOSCode='" + posCode + "' "
		    + " and strClientCode='" + clsGlobalVarClass.gClientCode + "')";
	    clsGlobalVarClass.dbMysql.execute(sqlAdvBookCharDtl);
	    sqlAdvBookCharDtl = "insert into tblqadvbookbillchardtl "
		    + " (select * from tbladvbookbillchardtl where strAdvBookingNo in "
		    + " (select strAdvBookingNo from tblbillhd "
		    + " where strPOSCode='" + posCode + "' "
		    + " and strClientCode='" + clsGlobalVarClass.gClientCode + "'))";
	    clsGlobalVarClass.dbMysql.execute(sqlAdvBookCharDtl);
	    sqlAdvBookCharDtl = "delete from tbladvbookbillchardtl where strAdvBookingNo in "
		    + " (select strAdvBookingNo from tblbillhd "
		    + " where strPOSCode='" + posCode + "' "
		    + " and strClientCode='" + clsGlobalVarClass.gClientCode + "')";
	    clsGlobalVarClass.dbMysql.execute(sqlAdvBookCharDtl);
	    //System.out.println("Adv Char Dtl");

	    String sqlAdvBookModDtl = "delete from tblqadvordermodifierdtl where strAdvOrderNo in "
		    + " (select strAdvBookingNo from tblbillhd "
		    + " where strPOSCode='" + posCode + "' "
		    + " and strClientCode='" + clsGlobalVarClass.gClientCode + "')";
	    clsGlobalVarClass.dbMysql.execute(sqlAdvBookModDtl);
	    sqlAdvBookModDtl = "insert into tblqadvordermodifierdtl "
		    + " (select * from tbladvordermodifierdtl where strAdvOrderNo in "
		    + " (select strAdvBookingNo from tblbillhd "
		    + " where strPOSCode='" + posCode + "' "
		    + " and strClientCode='" + clsGlobalVarClass.gClientCode + "'))";
	    clsGlobalVarClass.dbMysql.execute(sqlAdvBookModDtl);
	    sqlAdvBookModDtl = "delete from tbladvordermodifierdtl where strAdvOrderNo in "
		    + " (select strAdvBookingNo from tblbillhd "
		    + " where strPOSCode='" + posCode + "' "
		    + " and strClientCode='" + clsGlobalVarClass.gClientCode + "')";
	    clsGlobalVarClass.dbMysql.execute(sqlAdvBookModDtl);
	    //System.out.println("Adv Mod Dtl");

	    String sqlAdvBookHd = "delete from tblqadvbookbillhd where strAdvBookingNo in "
		    + " (select strAdvBookingNo from tblbillhd "
		    + " where strPOSCode='" + posCode + "' "
		    + " and strClientCode='" + clsGlobalVarClass.gClientCode + "')";
	    clsGlobalVarClass.dbMysql.execute(sqlAdvBookHd);
	    sqlAdvBookHd = "insert into tblqadvbookbillhd "
		    + " (select * from tbladvbookbillhd where strAdvBookingNo in "
		    + " (select strAdvBookingNo from tblbillhd "
		    + " where strPOSCode='" + posCode + "' "
		    + " and strClientCode='" + clsGlobalVarClass.gClientCode + "'))";
	    clsGlobalVarClass.dbMysql.execute(sqlAdvBookHd);
	    sqlAdvBookHd = "delete from tbladvbookbillhd where strAdvBookingNo in "
		    + " (select strAdvBookingNo from tblbillhd "
		    + " where strPOSCode='" + posCode + "' "
		    + " and strClientCode='" + clsGlobalVarClass.gClientCode + "')";
	    clsGlobalVarClass.dbMysql.execute(sqlAdvBookHd);
	    //System.out.println("Adv Hd");

	    /**
	     * billing table
	     */
	    String qSqlBillDtl = "delete from tblqbilldtl where strClientCode='" + clsGlobalVarClass.gClientCode + "' "
		    + "and strBillNo in (select strBillNo from tblbillhd where strPOSCode = '" + posCode + "') "
		    + "and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	    clsGlobalVarClass.dbMysql.execute(qSqlBillDtl);
	    qSqlBillDtl = "insert into tblqbilldtl (select * from tblbilldtl "
		    + "where strClientCode='" + clsGlobalVarClass.gClientCode + "' "
		    + "and strBillNo in (select strBillNo from tblbillhd where strPOSCode = '" + posCode + "'))";
	    clsGlobalVarClass.dbMysql.execute(qSqlBillDtl);
	    qSqlBillDtl = "delete from tblbilldtl where strClientCode='" + clsGlobalVarClass.gClientCode + "' "
		    + "and strBillNo in (select strBillNo from tblbillhd where strPOSCode = '" + posCode + "')";
	    clsGlobalVarClass.dbMysql.execute(qSqlBillDtl);
	    //System.out.println("Bill Dtl");

	    String qSqlBillSettDtl = "delete from tblqbillsettlementdtl where strClientCode='" + clsGlobalVarClass.gClientCode + "' "
		    + "and strBillNo in (select strBillNo from tblbillhd where strPOSCode = '" + posCode + "' ) "
		    + "and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	    clsGlobalVarClass.dbMysql.execute(qSqlBillSettDtl);
	    qSqlBillSettDtl = "insert into tblqbillsettlementdtl (select * from tblbillsettlementdtl "
		    + "where strClientCode='" + clsGlobalVarClass.gClientCode + "' "
		    + "and strBillNo in (select strBillNo from tblbillhd where strPOSCode = '" + posCode + "'))";
	    clsGlobalVarClass.dbMysql.execute(qSqlBillSettDtl);
	    qSqlBillSettDtl = "delete from tblbillsettlementdtl where strClientCode='" + clsGlobalVarClass.gClientCode + "' "
		    + "and strBillNo in (select strBillNo from tblbillhd where strPOSCode = '" + posCode + "')";
	    clsGlobalVarClass.dbMysql.execute(qSqlBillSettDtl);
	    //System.out.println("Bill Sett Dtl");

	    String qSqlBillModDtl = "delete from tblqbillmodifierdtl where strClientCode='" + clsGlobalVarClass.gClientCode + "' "
		    + "and strBillNo in (select strBillNo from tblbillhd where strPOSCode = '" + posCode + "' ) "
		    + " and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	    clsGlobalVarClass.dbMysql.execute(qSqlBillModDtl);
	    qSqlBillModDtl = "insert into tblqbillmodifierdtl (select * from tblbillmodifierdtl "
		    + "where strClientCode='" + clsGlobalVarClass.gClientCode + "' "
		    + "and strBillNo in (select strBillNo from tblbillhd where strPOSCode = '" + posCode + "'))";
	    clsGlobalVarClass.dbMysql.execute(qSqlBillModDtl);
	    qSqlBillModDtl = "delete from tblbillmodifierdtl where strClientCode='" + clsGlobalVarClass.gClientCode + "' "
		    + "and strBillNo in (select strBillNo from tblbillhd where strPOSCode = '" + posCode + "')";
	    clsGlobalVarClass.dbMysql.execute(qSqlBillModDtl);
	    //System.out.println("Bill Mod Dtl");

	    String qSqlBillTaxDtl = "delete from tblqbilltaxdtl where strClientCode='" + clsGlobalVarClass.gClientCode + "' "
		    + "and strBillNo in (select strBillNo from tblbillhd where strPOSCode = '" + posCode + "') "
		    + "  and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "'  ";
	    clsGlobalVarClass.dbMysql.execute(qSqlBillTaxDtl);
	    qSqlBillTaxDtl = "insert into tblqbilltaxdtl (select * from tblbilltaxdtl "
		    + "where strClientCode='" + clsGlobalVarClass.gClientCode + "' "
		    + "and strBillNo in (select strBillNo from tblbillhd where strPOSCode = '" + posCode + "'))";
	    clsGlobalVarClass.dbMysql.execute(qSqlBillTaxDtl);
	    qSqlBillTaxDtl = "delete from tblbilltaxdtl where strClientCode='" + clsGlobalVarClass.gClientCode + "' "
		    + "and strBillNo in (select strBillNo from tblbillhd where strPOSCode = '" + posCode + "')";
	    clsGlobalVarClass.dbMysql.execute(qSqlBillTaxDtl);
	    //System.out.println("Bill Tax Dtl");

	    //discount dtl tables
	    String qSqlBillDiscDtl = "delete from tblqbilldiscdtl where strPOSCode='" + posCode + "'"
		    + "and strBillNo in (select strBillNo from tblbilldiscdtl where strPOSCode = '" + posCode + "' ) "
		    + "and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	    clsGlobalVarClass.dbMysql.execute(qSqlBillDiscDtl);
	    qSqlBillDiscDtl = "insert into tblqbilldiscdtl (select * from tblbilldiscdtl "
		    + "where strPOSCode='" + posCode + "') ";
	    clsGlobalVarClass.dbMysql.execute(qSqlBillDiscDtl);
	    qSqlBillDiscDtl = "delete from tblbilldiscdtl where strPOSCode='" + posCode + "'";
	    clsGlobalVarClass.dbMysql.execute(qSqlBillDiscDtl);

	    String qSqlBillPromoDtl = "delete from tblqbillpromotiondtl where strClientCode='" + clsGlobalVarClass.gClientCode + "' "
		    + "and strBillNo in (select strBillNo from tblbillhd where strPOSCode = '" + posCode + "' ) "
		    + "and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "'  ";
	    clsGlobalVarClass.dbMysql.execute(qSqlBillPromoDtl);
	    qSqlBillPromoDtl = "insert into tblqbillpromotiondtl (select * from tblbillpromotiondtl "
		    + "where strClientCode='" + clsGlobalVarClass.gClientCode + "' "
		    + "and strBillNo in (select strBillNo from tblbillhd where strPOSCode = '" + posCode + "'))";
	    clsGlobalVarClass.dbMysql.execute(qSqlBillPromoDtl);
	    qSqlBillPromoDtl = "delete from tblbillpromotiondtl where strClientCode='" + clsGlobalVarClass.gClientCode + "' "
		    + "and strBillNo in (select strBillNo from tblbillhd where strPOSCode = '" + posCode + "')";
	    clsGlobalVarClass.dbMysql.execute(qSqlBillPromoDtl);
	    //System.out.println("Bill Promo Dtl");

	    String qSqlBillComplementoryDtl = "delete from tblqbillcomplementrydtl where strClientCode='" + clsGlobalVarClass.gClientCode + "' "
		    + "and strBillNo in (select strBillNo from tblbillhd where strPOSCode = '" + posCode + "' ) "
		    + "and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	    clsGlobalVarClass.dbMysql.execute(qSqlBillComplementoryDtl);
	    qSqlBillComplementoryDtl = "insert into tblqbillcomplementrydtl (select * from tblbillcomplementrydtl "
		    + "where strClientCode='" + clsGlobalVarClass.gClientCode + "' "
		    + "and strBillNo in (select strBillNo from tblbillhd where strPOSCode = '" + posCode + "'))";
	    clsGlobalVarClass.dbMysql.execute(qSqlBillComplementoryDtl);
	    qSqlBillComplementoryDtl = "delete from tblbillcomplementrydtl where strClientCode='" + clsGlobalVarClass.gClientCode + "' "
		    + "and strBillNo in (select strBillNo from tblbillhd where strPOSCode = '" + posCode + "')";
	    clsGlobalVarClass.dbMysql.execute(qSqlBillComplementoryDtl);
	    //System.out.println("Bill Complementory Dtl");

	    String qSqlBillHd = "delete from tblqbillhd where strPOSCode='" + posCode + "'"
		    + "and strBillNo in (select strBillNo from tblbillhd where strPOSCode = '" + posCode + "') "
		    + "and date(dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
	    clsGlobalVarClass.dbMysql.execute(qSqlBillHd);
	    qSqlBillHd = "insert into tblqbillhd (select * from tblbillhd "
		    + "where strClientCode='" + clsGlobalVarClass.gClientCode + "' and strPOSCode='" + posCode + "') ";
	    clsGlobalVarClass.dbMysql.execute(qSqlBillHd);
	    qSqlBillHd = "delete from tblbillhd where strPOSCode='" + posCode + "'";
	    clsGlobalVarClass.dbMysql.execute(qSqlBillHd);
	    //System.out.println("Bill HD");

	    /**
	     * billing table
	     */
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private boolean isTaxApplicableOnItemGroup(String taxCode, String itemCode)
    {
	boolean isApplicable = false;
	try
	{
	    String sql = "select a.strItemCode,a.strItemName,b.strSubGroupCode,b.strSubGroupName,c.strGroupCode,c.strGroupName,d.strTaxCode,d.strApplicable "
		    + "from tblitemmaster a,tblsubgrouphd b,tblgrouphd c,tbltaxongroup d "
		    + "where a.strSubGroupCode=b.strSubGroupCode "
		    + "and b.strGroupCode=c.strGroupCode "
		    + "and c.strGroupCode=d.strGroupCode "
		    + "and a.strItemCode='" + itemCode + "' "
		    + "and d.strTaxCode='" + taxCode + "' "
		    + "and d.strApplicable='true' ";
	    ResultSet rsTaxApplicable = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsTaxApplicable.next())
	    {
		isApplicable = true;
	    }
	    rsTaxApplicable.close();
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
	finally
	{
	    return isApplicable;
	}
    }

    public void funTestPrint(String printerName)
    {
	funCreateTempFolder();
	String filePath = System.getProperty("user.dir");
	String filename = (filePath + "/Temp/TestCCPrinter.txt");
	try
	{
	    File file = new File(filename);
	    funCreateTestTextFile(file, printerName);
	    clsPrintingUtility objPrintingUtility = new clsPrintingUtility();
	    objPrintingUtility.funShowTextFile(file, "", "");

	    int printerIndex = 0;
	    String printerStatus = "Not Found";

	    PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
	    DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
	    printerName = printerName.replaceAll("#", "\\\\");

	    PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
	    for (int i = 0; i < printService.length; i++)
	    {
		String printerServiceName = printService[i].getName();
		if (printerName.equalsIgnoreCase(printerServiceName))
		{
		    System.out.println("Printer=" + printerName);
		    printerIndex = i;
		    printerStatus = "Found";
		    break;
		}
	    }

	    if (printerStatus.equals("Found"))
	    {
		DocPrintJob job = printService[printerIndex].createPrintJob();
		FileInputStream fis = new FileInputStream(filename);
		DocAttributeSet das = new HashDocAttributeSet();
		Doc doc = new SimpleDoc(fis, flavor, das);
		job.print(doc, pras);

		PrintServiceAttributeSet att = printService[printerIndex].getAttributes();
		for (Attribute a : att.toArray())
		{
		    String attributeName;
		    String attributeValue;
		    attributeName = a.getName();
		    attributeValue = att.get(a.getClass()).toString();
		    if (attributeName.trim().equalsIgnoreCase("queued-job-count"))
		    {
			System.out.println(attributeName + " : " + attributeValue);
		    }
		}
	    }
	    else
	    {
		JOptionPane.showMessageDialog(null, printerName + " Printer Not Found");
	    }

	}
	catch (Exception e)
	{

	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	    JOptionPane.showMessageDialog(null, e.getMessage(), "Error Code - TFG 01", JOptionPane.ERROR_MESSAGE);
	}
    }

    private void funCreateTestTextFile(File file, String printerName)
    {
	BufferedWriter fileWriter = null;
	try
	{
	    //File file=new File(filename);
	    fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF8"));

	    String fileHeader = "----------Print Testing------------";
	    String dottedLine = "-----------------------------------";
	    String newLine = "\n";
	    String blankLine = "                                   ";

	    fileWriter.write(fileHeader);
	    fileWriter.newLine();
	    fileWriter.write(dottedLine);
	    fileWriter.newLine();
	    fileWriter.write("User Name    : " + clsGlobalVarClass.gUserName);
	    fileWriter.newLine();
	    fileWriter.write("POS Name     : " + clsGlobalVarClass.gPOSName);
	    fileWriter.newLine();
	    fileWriter.write("Printer Name : " + printerName);
	    fileWriter.newLine();

	    fileWriter.write(dottedLine);

	}
	catch (FileNotFoundException ex)
	{
	    funShowDBConnectionLostErrorMessage(ex);
	    ex.printStackTrace();
	}
	catch (UnsupportedEncodingException ex)
	{
	    funShowDBConnectionLostErrorMessage(ex);
	    ex.printStackTrace();
	}
	catch (IOException ex)
	{
	    funShowDBConnectionLostErrorMessage(ex);
	    ex.printStackTrace();
	}
	finally
	{
	    try
	    {
		fileWriter.close();
	    }
	    catch (IOException ex)
	    {
		funShowDBConnectionLostErrorMessage(ex);
		ex.printStackTrace();
	    }
	}

    }

    public boolean isItemAvailableForTotay(String itemCode, String posCode, String clientCode)
    {
	boolean isItemAvailable = true;
	try
	{
	    ResultSet rsIsItemAvailable = clsGlobalVarClass.dbMysql.executeResultSet("select * "
		    + "from tblnonavailableitems a "
		    + "where a.strItemCode='" + itemCode + "' "
		    + "and a.strPOSCode='" + posCode + "' "
		    + "and a.strClientCode='" + clientCode + "';");
	    if (rsIsItemAvailable.next())
	    {
		isItemAvailable = false;
	    }
	    rsIsItemAvailable.close();
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
	finally
	{
	    return isItemAvailable;
	}
    }

    public String funGetSelectedPOSCodeString(String fieldName, Set selectedPOSCodeSet)
    {
	String selectedPOSCodeFilter = fieldName + "='" + clsGlobalVarClass.gPOSCode + "' ";
	if (selectedPOSCodeSet.size() == 0)
	{
	    selectedPOSCodeFilter = fieldName + "='" + clsGlobalVarClass.gPOSCode + "' ";
	}
	else if (selectedPOSCodeSet.size() == 1)
	{
	    selectedPOSCodeFilter = fieldName + "='" + selectedPOSCodeSet.iterator().next().toString() + "' ";
	}
	else
	{
	    Iterator it = selectedPOSCodeSet.iterator();
	    int i = 0;
	    selectedPOSCodeFilter = "(";
	    while (it.hasNext())
	    {
		String posCode = it.next().toString();
		if (i == 0)
		{
		    selectedPOSCodeFilter += " " + fieldName + "='" + posCode + "' ";
		}
		else
		{
		    selectedPOSCodeFilter += " or " + fieldName + "='" + posCode + "' ";
		}

		i++;
	    }
	    selectedPOSCodeFilter += ")";

	}

	return selectedPOSCodeFilter;
    }

    public void funCloseAllOpenWindows()
    {
	Window openedWindows[] = Window.getWindows();
	for (int i = 0; i < openedWindows.length; i++)
	{
	    openedWindows[i].dispose();
	}
    }

    public void funCloseAllOpenWindows(JFrame currentFrame)
    {
	Window openedWindows[] = Window.getWindows();
	for (int i = 0; i < openedWindows.length; i++)
	{
	    openedWindows[i].dispose();
	}
    }

    public boolean funGetWebServiceConnectionStatus(String webServiceURL)
    {
	boolean flgHOStatus = false;

	try
	{

	    String hoURL = webServiceURL + "/POSIntegration/funInvokeHOWebService";

	    URL url = new URL(hoURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setRequestMethod("GET");
	    conn.setRequestProperty("Accept", "application/json");

//            if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED)
//            {
//                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
//            }
	    //System.out.println("response->" + conn.getResponseCode());            
	    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

	    JOptionPane.showMessageDialog(null, conn.getResponseMessage() + ".", "" + conn.getResponseCode(), JOptionPane.INFORMATION_MESSAGE);

	    String output = "", op = "";
	    while ((output = br.readLine()) != null)
	    {
		op += output;
	    }
	    System.out.println("OP=" + op);
	    conn.disconnect();

	    flgHOStatus = Boolean.parseBoolean(op);
	    if (flgHOStatus)
	    {
		gConnectionActive = "Y";
	    }

	}
	catch (java.net.ConnectException ce)
	{
	    funShowDBConnectionLostErrorMessage(ce);
	    JOptionPane.showMessageDialog(null, "Please Check IP Address And Port And\nMake Sure Web Server Started.", "Connection Refused", JOptionPane.INFORMATION_MESSAGE);

	    flgHOStatus = false;
	    gConnectionActive = "N";
	    //ce.printStackTrace();
	}
	catch (java.io.FileNotFoundException fne)
	{
	    funShowDBConnectionLostErrorMessage(fne);
	    JOptionPane.showMessageDialog(null, "Please Check URL.", "Connection Refused", JOptionPane.INFORMATION_MESSAGE);

	    flgHOStatus = false;
	    gConnectionActive = "N";
	    //ce.printStackTrace();
	}
	catch (java.net.UnknownHostException ukhe)
	{
	    funShowDBConnectionLostErrorMessage(ukhe);
	    JOptionPane.showMessageDialog(null, "Please Check URL.", "Unknown HOST", JOptionPane.INFORMATION_MESSAGE);

	    flgHOStatus = false;
	    gConnectionActive = "N";
	    //ce.printStackTrace();
	}
	catch (Exception e)
	{
	    flgHOStatus = false;
	    gConnectionActive = "N";
	    e.printStackTrace();
	}
	finally
	{
	    return flgHOStatus;
	}
    }

    public void funSendTestSMS(String testMobileNumber, String testSMS)
    {
	try
	{
	    ArrayList<String> mobileNumberList = new ArrayList<String>();

	    String[] mobileNos = testMobileNumber.split(",");
	    for (int i = 0; i < mobileNos.length; i++)
	    {
		mobileNumberList.add(mobileNos[i]);
	    }

	    boolean isSend = funSendBulkSMS(mobileNumberList, testSMS);
	    if (isSend)
	    {
		new frmOkPopUp(null, "Test SMS Sent To :" + testMobileNumber + ".", "Message", 1).setVisible(true);
		return;
	    }
	    else
	    {
		new frmOkPopUp(null, "Unable To Send SMS To :" + testMobileNumber + ".", "Error", 1).setVisible(true);
		return;
	    }

	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
    }

    public boolean funSendBulkSMS(ArrayList<String> mobileNumberList, String testSMS)
    {
	boolean result = false;
	try
	{

	    if (mobileNumberList.size() < 1 || testSMS.length() < 1)
	    {
		return result;
	    }

	    String fromTelNo = clsGlobalVarClass.gClientTelNo;
	    String[] sp = fromTelNo.split(",");
	    if (sp.length > 0)
	    {
		fromTelNo = sp[0];
	    }

	    if (clsGlobalVarClass.gSMSType.equalsIgnoreCase("Sanguine"))
	    {
		clsClientDetails objClientDetails = clsClientDetails.hmClientDtl.get(clsGlobalVarClass.gClientCode);
		clsSMSPackDtl objSMSPackDtl = objClientDetails.getObjSMSPackDtl();

		String userId = clsEncryptDecryptClientCode.funDecryptClientCode(objSMSPackDtl.getStrUserId());
		String password = clsEncryptDecryptClientCode.funDecryptClientCode(objSMSPackDtl.getStrPassword());
		String smsPack = clsEncryptDecryptClientCode.funDecryptClientCode(objSMSPackDtl.getStrSMSPack());
		String senderId = clsEncryptDecryptClientCode.funDecryptClientCode(objSMSPackDtl.getStrSenderId());

		if (smsPack.equalsIgnoreCase("NOSMSPACK") || senderId.isEmpty())
		{
		    return result;
		}

		int noOfPhones = 100;
		if (mobileNumberList.size() < noOfPhones)
		{
		    noOfPhones = mobileNumberList.size();
		}
		StringBuilder mobileNoBuilder = new StringBuilder();
		int noOfSMSSends = noOfPhones;
		for (int i = 0; i < mobileNumberList.size();)
		{
		    boolean isFirstMobileNo = true;
		    for (int j = 0; j < noOfPhones && i < mobileNumberList.size(); j++, i++)
		    {
			String mobileNo = mobileNumberList.get(i);
			if (mobileNo.matches("\\d{10}"))
			{
			    if (isFirstMobileNo)
			    {
				mobileNoBuilder.append(mobileNo);
				isFirstMobileNo = false;
			    }
			    else
			    {
				mobileNoBuilder.append(",").append(mobileNo);
			    }
			}
			else
			{
			    System.out.println("Invalid mobile number-->" + mobileNo);
			}
		    }
		    if (mobileNoBuilder.length() > 0)
		    {
			String smsURL = clsGlobalVarClass.gSMSApi.replace("<USERNAME>", userId).replace("<PASSWORD>", password).replace("<SENDERID>", senderId).replace("<PHONE>", mobileNoBuilder.toString()).replace("<MSG>", testSMS).replaceAll(" ", "%20").replaceAll("\n", "%20");
			mobileNoBuilder.setLength(0);
			result = funSendSMS(smsURL);
			if (result)
			{
			    System.out.println("No of SMS sent-->" + noOfSMSSends);
			    noOfSMSSends = noOfSMSSends + noOfPhones;
			}
		    }
		}

		return result;
	    }
	    else if (clsGlobalVarClass.gSMSType.equalsIgnoreCase("Cellx"))
	    {
		for (int i = 0; i < mobileNumberList.size(); i++)
		{
		    if ((!mobileNumberList.get(i).isEmpty()))
		    {
			String smsURL = clsGlobalVarClass.gSMSApi.replace("<to>", mobileNumberList.get(i)).replace("<from>", fromTelNo).replace("<MSG>", testSMS).replaceAll(" ", "%20");
			result = funSendSMS(smsURL);
		    }
		}

		return result;
	    }
	    else if (clsGlobalVarClass.gSMSType.equalsIgnoreCase("Sinfini"))
	    {
		for (int i = 0; i < mobileNumberList.size(); i++)
		{
		    if (!mobileNumberList.get(i).isEmpty())
		    {
			String smsURL = clsGlobalVarClass.gSMSApi.replace("<PHONE>", mobileNumberList.get(i)).replace("<MSG>", testSMS).replaceAll(" ", "%20");
			result = funSendSMS(smsURL);
		    }
		}

		return result;
	    }
	    else if (clsGlobalVarClass.gSMSType.equalsIgnoreCase("Infyflyer"))
	    {

		for (int i = 0; i < mobileNumberList.size(); i++)
		{
		    if (!mobileNumberList.get(i).isEmpty())
		    {
			String smsURL = clsGlobalVarClass.gSMSApi.replace("<PHONE>", mobileNumberList.get(i)).replace("<MSG>", testSMS).replaceAll(" ", "%20");
			result = funSendSMS(smsURL);
		    }
		}

		return result;
	    }
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	    return result;
	}

	return result;
    }

    private void funSendDayEndSMS(String mobileNo, String posCode, String posDate, int shiftNo)
    {
	try
	{
	    clsUtility2 objUtility2 = new clsUtility2();
	    StringBuilder mainSMSBuilder = new StringBuilder();
	    //DecimalFormat decimalFormat = new DecimalFormat("0.##");
	    long netTotal = 0, grossTotal = 0, totalDisc = 0, totalAPC = 0;
	    int totalPax = 0;

	    String sqlSettelementBrkUP = "select SUM(a.dblSubTotal)-sum(a.dblDiscountAmt) as NetTotal,SUM(a.dblGrandTotal) as GrossSales "
		    + ",SUM(a.intBillSeriesPaxNo) as TotalPax,SUM(a.dblSubTotal)/SUM(a.intBillSeriesPaxNo) as APC  "
		    + "from  tblqbillhd a  "
		    + "where a.strPOSCode=?  "
		    + "and date(a.dteBillDate)=?  "
		    + "and a.intShiftCode=? ";
	    PreparedStatement pst_SettelementBrkUP = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlSettelementBrkUP);
	    pst_SettelementBrkUP.setString(1, posCode);
	    pst_SettelementBrkUP.setString(2, posDate);
	    pst_SettelementBrkUP.setString(3, String.valueOf(shiftNo));
	    ResultSet rsSettelementBrkUP = pst_SettelementBrkUP.executeQuery();
	    if (rsSettelementBrkUP.next())
	    {
		netTotal = rsSettelementBrkUP.getLong(1);//netTotal
		grossTotal = rsSettelementBrkUP.getLong(2);//grossTotal
		totalPax = rsSettelementBrkUP.getInt(3);//totalPax
		totalAPC = rsSettelementBrkUP.getLong(4);//totalAPC
	    }
	    rsSettelementBrkUP.close();

	    mainSMSBuilder.append("Day_End");
	    mainSMSBuilder.append(" ,Date:" + posDate);
	    mainSMSBuilder.append(" ,POS:" + clsGlobalVarClass.gPOSName);
	    mainSMSBuilder.append(" ,Shift:" + shiftNo);
	    mainSMSBuilder.append(" ,User:" + clsGlobalVarClass.gUserCode);
	    if (totalPax > 0)
	    {
		totalAPC = netTotal / totalPax;
	    }

	    mainSMSBuilder.append("     ");
	    mainSMSBuilder.append(" ,NET SALE:" + String.valueOf(Math.rint(netTotal)));
	    mainSMSBuilder.append(" ,GROSS SALE:" + String.valueOf(Math.rint(grossTotal)));
	    mainSMSBuilder.append(" ,PAX:" + String.valueOf(Math.rint(totalPax)));
	    mainSMSBuilder.append(" ,APC:" + String.valueOf(Math.rint(totalAPC)));

	    /**
	     * MTD sales
	     */
	    String[] arrDay = posDate.split("-");
	    String fromDate = "";
	    if (Integer.valueOf(arrDay[2]) > 1)
	    {
		fromDate = arrDay[0] + "-" + arrDay[1] + "-" + "01";
	    }
	    double monthTotalSales = 0.00, monthNetTotal = 0.00, monthGrossTotal = 0.00, monthTotalPax = 0, monthTotalAPC = 0;
	    String sqlUpToDateForCurrentMonthBrkUP = "select SUM(a.dblSubTotal) as NetTotal,SUM(a.dblGrandTotal) as GrossSales "
		    + ",SUM(a.intBillSeriesPaxNo) as TotalPax,SUM(a.dblSubTotal)/SUM(a.intBillSeriesPaxNo) as APC  "
		    + "from  tblqbillhd a  "
		    + "where a.strPOSCode='" + posCode + "'  "
		    + "and date(a.dteBillDate) between '" + fromDate + "' and '" + posDate + "'  "
		    + "and a.intShiftCode='" + shiftNo + "' ";
	    PreparedStatement pst_MonthBkp = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlUpToDateForCurrentMonthBrkUP);
	    ResultSet rsMonthWiseUP = pst_MonthBkp.executeQuery();
	    while (rsMonthWiseUP.next())
	    {
		monthNetTotal = rsMonthWiseUP.getDouble(1);//monthNetTotal
		monthGrossTotal = rsMonthWiseUP.getDouble(2);//monthGrossTotal
		monthTotalPax = rsMonthWiseUP.getInt(3);//monthTotalPax
		monthTotalAPC = rsMonthWiseUP.getDouble(4);//monthTotalAPC

//                /**
//                 * (x to y APC)/y days
//                 */
//                monthTotalAPC = monthTotalAPC / Integer.parseInt(arrDay[2]);
	    }
	    rsMonthWiseUP.close();

	    mainSMSBuilder.append("     ");
	    mainSMSBuilder.append(" ,MTD NET SALE:" + String.valueOf(Math.rint(monthNetTotal)));
	    mainSMSBuilder.append(" ,MTD GROSS SALE:" + String.valueOf(Math.rint(monthGrossTotal)));
	    mainSMSBuilder.append(" ,MTD PAX:" + String.valueOf(Math.rint(monthTotalPax)));
	    mainSMSBuilder.append(" ,MTD APC:" + String.valueOf(Math.rint(monthTotalAPC)));

	    /**
	     * group wise sales
	     */
	    StringBuilder sqlBuilder = new StringBuilder();
	    Map<String, Double> mapGroupSales = new HashMap<>();

	    //live
	    sqlBuilder.setLength(0);
	    sqlBuilder.append("SELECT c.strGroupCode,c.strGroupName, SUM(b.dblQuantity), SUM(b.dblAmount)- SUM(b.dblDiscountAmt) NetTotal "
		    + "FROM tblbillhd a,tblbilldtl b,tblgrouphd c,tblsubgrouphd d,tblitemmaster e,tblposmaster f "
		    + "WHERE a.strBillNo=b.strBillNo  "
		    + "AND a.strPOSCode=f.strPOSCode  "
		    + "AND a.strClientCode=b.strClientCode  "
		    + "AND b.strItemCode=e.strItemCode  "
		    + "AND c.strGroupCode=d.strGroupCode  "
		    + "AND d.strSubGroupCode=e.strSubGroupCode  "
		    + "AND DATE(a.dteBillDate) BETWEEN '" + posDate + "' AND '" + posDate + "' "
		    + "AND a.strPOSCode='" + posCode + "' "
		    + "GROUP BY c.strGroupCode, c.strGroupName ");
	    ResultSet rsGroupSales = clsGlobalVarClass.dbMysql.executeResultSet(sqlBuilder.toString());
	    while (rsGroupSales.next())
	    {
		String groupName = rsGroupSales.getString(2);//groupName
		double groupNetTotal = rsGroupSales.getDouble(4);//netTotal
		if (mapGroupSales.containsKey(groupName))
		{
		    mapGroupSales.put(groupName, mapGroupSales.get(groupName) + groupNetTotal);
		}
		else
		{
		    mapGroupSales.put(groupName, groupNetTotal);
		}
	    }
	    rsGroupSales.close();
	    //modifiers
	    sqlBuilder.setLength(0);
	    sqlBuilder.append("SELECT c.strGroupCode,c.strGroupName, SUM(b.dblQuantity), SUM(b.dblAmount)- SUM(b.dblDiscAmt) NetTotal "
		    + "FROM tblbillhd a,tblbillmodifierdtl b,tblgrouphd c,tblsubgrouphd d,tblitemmaster e,tblposmaster f "
		    + "WHERE a.strBillNo=b.strBillNo  "
		    + "AND a.strPOSCode=f.strPOSCode  "
		    + "AND a.strClientCode=b.strClientCode  "
		    + "AND left(b.strItemCode,7)=e.strItemCode  "
		    + "AND c.strGroupCode=d.strGroupCode  "
		    + "AND d.strSubGroupCode=e.strSubGroupCode  "
		    + "AND DATE(a.dteBillDate) BETWEEN '" + posDate + "' AND '" + posDate + "' "
		    + "AND a.strPOSCode='" + posCode + "' "
		    + "GROUP BY c.strGroupCode, c.strGroupName ");
	    rsGroupSales = clsGlobalVarClass.dbMysql.executeResultSet(sqlBuilder.toString());
	    while (rsGroupSales.next())
	    {
		String groupName = rsGroupSales.getString(2);//groupName
		double groupNetTotal = rsGroupSales.getDouble(4);//netTotal
		if (mapGroupSales.containsKey(groupName))
		{
		    mapGroupSales.put(groupName, mapGroupSales.get(groupName) + groupNetTotal);
		}
		else
		{
		    mapGroupSales.put(groupName, groupNetTotal);
		}
	    }
	    rsGroupSales.close();

	    //Q
	    sqlBuilder.setLength(0);
	    sqlBuilder.append("SELECT c.strGroupCode,c.strGroupName, SUM(b.dblQuantity), SUM(b.dblAmount)- SUM(b.dblDiscountAmt) NetTotal "
		    + "FROM tblqbillhd a,tblqbilldtl b,tblgrouphd c,tblsubgrouphd d,tblitemmaster e,tblposmaster f "
		    + "WHERE a.strBillNo=b.strBillNo  "
		    + "AND a.strPOSCode=f.strPOSCode  "
		    + "AND a.strClientCode=b.strClientCode  "
		    + "AND b.strItemCode=e.strItemCode  "
		    + "AND c.strGroupCode=d.strGroupCode  "
		    + "AND d.strSubGroupCode=e.strSubGroupCode  "
		    + "AND DATE(a.dteBillDate) BETWEEN '" + posDate + "' AND '" + posDate + "' "
		    + "AND a.strPOSCode='" + posCode + "' "
		    + "GROUP BY c.strGroupCode, c.strGroupName ");
	    rsGroupSales = clsGlobalVarClass.dbMysql.executeResultSet(sqlBuilder.toString());
	    while (rsGroupSales.next())
	    {
		String groupName = rsGroupSales.getString(2);//groupName
		double groupNetTotal = rsGroupSales.getDouble(4);//netTotal
		if (mapGroupSales.containsKey(groupName))
		{
		    mapGroupSales.put(groupName, mapGroupSales.get(groupName) + groupNetTotal);
		}
		else
		{
		    mapGroupSales.put(groupName, groupNetTotal);
		}
	    }
	    rsGroupSales.close();
	    //modifiers
	    sqlBuilder.setLength(0);
	    sqlBuilder.append("SELECT c.strGroupCode,c.strGroupName, SUM(b.dblQuantity), SUM(b.dblAmount)- SUM(b.dblDiscAmt) NetTotal "
		    + "FROM tblqbillhd a,tblqbillmodifierdtl b,tblgrouphd c,tblsubgrouphd d,tblitemmaster e,tblposmaster f "
		    + "WHERE a.strBillNo=b.strBillNo  "
		    + "AND a.strPOSCode=f.strPOSCode  "
		    + "AND a.strClientCode=b.strClientCode  "
		    + "AND left(b.strItemCode,7)=e.strItemCode  "
		    + "AND c.strGroupCode=d.strGroupCode  "
		    + "AND d.strSubGroupCode=e.strSubGroupCode  "
		    + "AND DATE(a.dteBillDate) BETWEEN '" + posDate + "' AND '" + posDate + "' "
		    + "AND a.strPOSCode='" + posCode + "' "
		    + "GROUP BY c.strGroupCode, c.strGroupName ");
	    rsGroupSales = clsGlobalVarClass.dbMysql.executeResultSet(sqlBuilder.toString());
	    while (rsGroupSales.next())
	    {
		String groupName = rsGroupSales.getString(2);//groupName
		double groupNetTotal = rsGroupSales.getDouble(4);//netTotal
		if (mapGroupSales.containsKey(groupName))
		{
		    mapGroupSales.put(groupName, mapGroupSales.get(groupName) + groupNetTotal);
		}
		else
		{
		    mapGroupSales.put(groupName, groupNetTotal);
		}
	    }
	    rsGroupSales.close();

	    mainSMSBuilder.append("     ");
	    mainSMSBuilder.append("Group Wise Net Sales:");
	    mainSMSBuilder.append("     ");
	    for (Map.Entry<String, Double> entry : mapGroupSales.entrySet())
	    {
		String groupName = entry.getKey();
		double groupNetTotal = entry.getValue();

		mainSMSBuilder.append(" ," + groupName + ":" + String.valueOf(Math.rint(groupNetTotal)));
	    }

	    System.out.println("Day end SMS-->\n" + mainSMSBuilder);

	    ArrayList<String> mobileNumberList = new ArrayList<String>();
	    String mobNos[] = mobileNo.split(",");
	    for (String mn : mobNos)
	    {
		mobileNumberList.add(mn);
	    }
	    boolean isSend = funSendBulkSMS(mobileNumberList, mainSMSBuilder.toString());
	    System.out.println("day end msg sent->" + isSend);
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
    }

    private Boolean funSendSMS(String url)
    {
	boolean result = false;
	StringBuilder output = new StringBuilder();
	try
	{
	    URL hp = new URL(url);
	    //System.out.println(url);
	    URLConnection hpCon = hp.openConnection();
	    BufferedReader in = new BufferedReader(new InputStreamReader(hpCon.getInputStream()));
	    String inputLine;
	    while ((inputLine = in.readLine()) != null)
	    {
		output.append(inputLine);
		result = true;
	    }
	    in.close();
	}
	catch (Exception e)
	{
	    result = false;
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
	return result;
    }

    public void funPrintBlankSpace(String printWord, BufferedWriter objBW) throws Exception
    {
	int wordSize = printWord.length();
	int actualPrintingSize = clsGlobalVarClass.gColumnSize;
	int availableBlankSpace = actualPrintingSize - wordSize;

	int leftSideSpace = availableBlankSpace / 2;
	if (leftSideSpace > 0)
	{
	    for (int i = 0; i < leftSideSpace; i++)
	    {
		objBW.write(" ");
	    }
	}
    }

    public Map<String, clsBillDtl> funGetComplimetaryItems(String billNo, String billDtl, String billHd, String POSCode, String billDate) throws Exception
    {
	Map<String, clsBillDtl> hmComplBillItemDtl = new HashMap<String, clsBillDtl>();
	StringBuilder sbSql = new StringBuilder();
	sbSql.append("select sum(a.dblQuantity),sum(a.dblAmount),a.strItemCode "
		+ " from " + billDtl + " a," + billHd + " b "
		+ " where a.strBillNo=b.strBillNo and a.strClientCode=b.strClientCode "
		+ " and date(a.dteBillDate)=date(b.dteBillDate) "
		+ " and a.strBillNo=? and b.strPOSCode=? and date(b.dteBillDate)=? ");
	if (!clsGlobalVarClass.gPrintTDHItemsInBill)
	{
	    sbSql.append("and a.tdhYN='N' ");
	}
	if (!clsGlobalVarClass.gPrintOpenItemsOnBill)
	{
	    sbSql.append("and a.dblAmount>0 ");
	}
	sbSql.append(" group by a.strItemCode ");
	PreparedStatement pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sbSql.toString());
	pst.setString(1, billNo);
	pst.setString(2, POSCode);
	pst.setString(3, billDate);

	ResultSet rs = pst.executeQuery();
	while (rs.next())
	{
	    clsBillDtl objBillDtl = new clsBillDtl();
	    objBillDtl.setStrItemCode(rs.getString(3));
	    objBillDtl.setDblAmount(rs.getDouble(2));
	    objBillDtl.setDblQuantity(rs.getDouble(1));
	    hmComplBillItemDtl.put(rs.getString(3), objBillDtl);
	}
	rs.close();

	return hmComplBillItemDtl;
    }

    public boolean funCheckTableStatusFromItemRTemp(String tblNo)
    {
	boolean flgStatusBillInProcess = false;
	try
	{
	    String sql = "select strTableStatus from tblitemrtemp where strTableNo='" + tblNo + "'";
	    ResultSet rsTblStatus = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsTblStatus.next())
	    {
		if (rsTblStatus.getString(1).equalsIgnoreCase("BillingInProgress"))
		{
		    flgStatusBillInProcess = true;
		}
	    }
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
	finally
	{
	    return flgStatusBillInProcess;
	}
    }

    public String funCheckSpecialCharacters(String inputString)
    {
	String outputString = inputString;
	try
	{
	    if (null != inputString)
	    {
		if (inputString.contains("\\"))
		{
		    outputString = inputString.replace("\\", "\\\\");
		}
		else if (inputString.contains("'"))
		{
		    outputString = inputString.replace("'", "''");
		}
	    }

	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
	finally
	{
	    return outputString;
	}

    }

    public String funIsCardTimeExpire(String debitCardNo)
    {
	String cardStatus = "Active";
	try
	{
	    String sqlExpiryTime = "select a.intRechargeNo,a.strCardTypeCode,a.strCardString,a.dblRechargeAmount,a.strPOSCode "
		    + ",TIME_FORMAT(time(a.dteDateCreated),'%h:%i %p')RechargeTime,CURRENT_TIME() CurrTime,TIMEDIFF(CURRENT_TIME(),time(a.dteDateCreated))TimeDifference "
		    + ",b.strSetExpiryTime,b.intExpiryTime,time(DATE_ADD(a.dteDateCreated,INTERVAL b.intExpiryTime minute)) ValidTillTime "
		    + ",if(CURRENT_TIME()>time(DATE_ADD(a.dteDateCreated,INTERVAL b.intExpiryTime minute)),true,false)IsExpire "
		    + "from tbldebitcardrecharge a,tbldebitcardtype b "
		    + "where a.strCardTypeCode=b.strCardTypeCode "
		    + "and a.strCardString='" + debitCardNo + "' "
		    + "order by a.dteDateCreated desc "
		    + "limit 1 ";
	    ResultSet rsExpiryTime = clsGlobalVarClass.dbMysql.executeResultSet(sqlExpiryTime);
	    if (rsExpiryTime.next())
	    {
		String lastRechargeNo = rsExpiryTime.getString(1);
		String lastRechargeAmt = rsExpiryTime.getString(4);
		String lastRechargeTime = rsExpiryTime.getString(6);
		int intIsExpire = rsExpiryTime.getInt(12);
		if (intIsExpire == 0)//not expire
		{

		}
		else//1 means expire
		{
		    cardStatus = "Card Time Expired" + "!" + lastRechargeNo + "!" + lastRechargeAmt + "!" + lastRechargeTime;
		    //new frmOkPopUp(null, "<html>Recharge No:" + lastRechargeNo + "<br>Recharge Amt:" + lastRechargeAmt + "<br>Recharge Time:" + lastRechargeTime + "</html>", "Card Time Expired", 3).setVisible(true);				    				   
		}

	    }
	    rsExpiryTime.close();
	}
	catch (Exception e)
	{
	    funShowDBConnectionLostErrorMessage(e);
	    e.printStackTrace();
	}
	finally
	{
	    return cardStatus;
	}
    }

}
