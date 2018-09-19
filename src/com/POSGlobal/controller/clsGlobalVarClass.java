package com.POSGlobal.controller;

import com.POSGlobal.view.frmTools;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;

public class clsGlobalVarClass
{

    private String sql;

    static boolean funSetMailServerProperties(double totalSales, double totalWithdrawl, double totalPayments)
    {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static Connection conPrepareStatement, conJasper;
    public static Connection conRMS;
    public static Statement stmtRMS;
    public static clsDatabaseConnection dbMysql;

    public static boolean gTouchScreenMode = false, gPrintRemarkAndReasonForReprint = false, flgReprintView, gSanguneUser;
    public static boolean gflagforSubItem, gNewCustomerForHomeDel, gNewCustForAdvOrder, flgCarryForwardFloatAmtToNextDay;
    public static boolean gSuperUser, gPriceItem, gUpdatekot, gPrintNewBill, gSearchItemClicked;
    public static boolean gRateEntered, gShifts, gHomeDeliveryForMakeKOT, gHomeDeliveryForSelectedBillNo;
    public static boolean gNegBilling, gMultiBillPrint, gEnableKOT, gEffectOnPSP, gPrintVatNo, gPrintServiceTaxNo, gShowBill;
    public static boolean gKOTPrintingEnableForDirectBiller, gEditHDCharges, gSlabBasedHDCharges, gHomeDeliveryTable;
    public static boolean gSkipWaiter, gSkipPax, gActivePromotions, gSettleBill;
    public static boolean gHomeDelSMSYN, gBillSettleSMSYN, gPrintShortNameOnKOT, gCustHelpOnTrans;
    public static boolean gPrintOnVoidBill, gPostSalesDataToMMS, gCustAreaCompulsory, gShowPrinterErrorMsg;
    public static boolean gChangeQtyForExternalCode, gPointsOnBillPrint, gCompulsoryManualAdvOrderNo, gPrintManualAdvOrderNoOnBill;
    public static boolean gPrintModQtyOnKOT, gMultipleKOTPrint, gItemQtyNumpad, gTreatMemberAsTable, gPrintKotToLocaPrinter;
    public static boolean gEnableSettleBtnForDirectBiller, gDelBoyCompulsoryOnDirectBiller, gDontShowAdvOrderInOtherPOS, gPrintZeroAmtModifierOnBill;
    public static boolean gPrintKOTYN, gCreditCardSlipNo, gCreditCardExpiryDate, gSelectWaiterFromCardSwipe;
    public static boolean gMoveTableToOtherPOS, gMoveKOTToOtherPOS, gCalculateTaxOnMakeKOT, gMultiWaiterSelOnMakeKOT;
    public static boolean gItemWiseDiscount, gRemarksOnTakeAway, gShowItemStkColumnInDB, gAllowNewAreaMasterFromCustMaster;
    public static boolean gCustAddressSelectionForBill, gGenrateMI, gPrintBillYN, gUseVatAndServiceTaxFromPos, gMemberCodeForMakeBillInMPOS;
    public static boolean gItemWiseKOTPrintYN, gPopUpToApplyPromotionsOnBill, gSelectCustomerCodeFromCardSwipe;
    public static boolean gCheckDebitCardBalanceOnTrans, gPickSettlementsFromPOSMaster, validateFlag;
    public static boolean gEnableShiftYN, gProductionLinkup, gLockDataOnShiftYN, gEnableBillSeries;
    public static boolean gEnablePMSIntegrationYN, gPrintTimeOnBillYN, gPrintTDHItemsInBill, gEnablePrintAndSettleBtnForDB, gShowItemDetailsGrid;
    public static boolean gSetUpToTimeForAdvOrder, gSetUpToTimeForUrgentOrder;
    public static boolean gInrestoPOSIntegrationYN, gCMSIntegrationYN, gOpenCashDrawerAfterBillPrintYN;
    public static boolean gPropertyWiseSalesOrderYN, gShowPopUpForNextItemQuantity;

    public static int gtblRowcount = 0, gItemCount = 0, gNoOfDelDaysForAdvOrder, gNoOfDelDaysForUrgentOrder;
    public static int gShiftNo, gColumnSize;

    public static double gTotalCashSales, gTotalCashInHand, gTotalPayments, gTotalReceipt, gTotalDiscounts, gMaxDiscount;
    public static double gNoOfDiscountedBills, gTotalAdvanceAmt, gTotalBills, gDeliveryCharges, gTotalBillAmount;

    public static Date gMaxBillDate, gStartDate, gEndDate, gCalendarDate;
    public static Time gEndTime;

    public static long gKOTCode, gNewCustomerMobileNo;

    public static Pattern pattern;
    public static Matcher matcher;

    public static String gUserName, gUserCode, gPOSCode, gPOSName, gClientCode, gClientName, gClientAddress1, gKeyboardValue, gPOSDateToDisplay;
    public static String gClientAddress2, gPrintType, gClientAddress3, gClientEmail, gBillFooter, gCityName, gCountryName;
    public static String AdvReceiptNo, gPOSDate, gPOSDateForTransaction, gPOSOnlyDateForTransaction;
    public static String gStateName, gUserType, gPOSStartDate, gNatureOfBusinnes, AdvOrderNo, gSystemDate, gTakeAway = "No";
    public static String gBillPaperSize, AdvAmt, OrderForDate, gVatNo, gServiceTaxNo, gPOSType, gCounterWise;
    public static String gBillingType, gItemCodeforPricing, gItemDetails = " ", gFormHeader, gManualBillNo, gMenuItemSequence;
    public static String gSearchedItem, gQueryForSearch, gQueryForAdvSearch, gNumerickeyboardValue, gAdvSearchItem, gSearchFormName;
    public static String gEmailServerName, gSenderEmailId, gSenderMailPassword, gEmailMessage, gBillDateTimeType, gShiftEnd;
    public static String gFormNameOnKeyBoard, gCounterCode, gCounterName, gCustomerCode, gSanguineWebServiceURL;
    public static String gAreaCodeForTrans, gDeliveryBoyCode, gDeliveryTime, gHomeDeliverySMS, gBillSettlementSMS, gDebitCardNo;
    public static String gcardStatus, gLastModifiedDate, gPosCodeForReprintDocs, gPOSCodeForPricing, gHomeDelivery;
    public static String gTableNoForHomeDelivery, gHOPOSType, gPropertyCode, gCustMBNo, gFavoritereason, gReasoncode;
    public static String gDebitCardPayment, gTransactionType, gTheme = "Default", gAreaWisePricing, gMenuItemSortingOn;
    public static String gCustomerAddress1, gCustomerAddress2, gCustomerAddress3, gCustomerAddress4, gCustomerCity;
    public static String gSearchItem, gRFIDInterface, gRFIDDBServerName, gRFIDDBUserName, gRFIDDBPassword, gRFIDDBName;
    public static String gSMSApi, gDeliveryBoyName, gConnectionActive, gCustomerName, gBuildingCodeForHD, gCustCodeForAdvOrder;
    public static String gDayEndReportForm, gAdvOrderNoForBilling, gFlgPoints, gCRMInterface, gCustMobileNoForCRM;
    public static String gGetWebserviceURL, gPostWebserviceURL, gOutletUID, gPOSID, gStockInOption, gDelayedSettlementForDB;
    public static String gHomeDeliveryClick = "N", gAdvRecPrintCount, gBillFormatType, gSMSType, gDataSendFrequency;
    public static String gPriceFrom, gMobileNoForSMS, gCardIntfType, gNoOfLinesInKOTPrint, gBillPrintPrinterPort;
    public static String gCMSWebServiceURL = "", gWebBooksWebServiceURL = "", gWebMMSWebServiceURL = "", gWebExciseWebServiceURL = "";
    public static String gCMSPOSCode, gWSClientCode, gCMSPostingType, gDayEnd;
    public static String gSelectedModule, gUserPOSCode, gChangeModule, gCMSMemberCodeForKOTJPOS, gCMSMemberCodeForKOTMPOS;
    public static String gAdvReceiptPrinterPort, gClientTelNo, gPrinterQueueStatus, gReceiverEmailIds, gDBBackupReceiverEmailIds;
    public static String gItemType, gFTPAddress, gFTPServerUserName, gFTPServerPass, gAllowToCalculateItemWeight, gShowBillsType;
    public static String gPrintTaxInvoice, gPrintInclusiveOfAllTaxes, gApplyDiscountOn, gMemberCodeForKotInMposByCardSwipe;
    public static String gPrintVatNoPOS, gPrintServiceTaxNoPOS, gPOSVatNo, gPOSServiceTaxNo;
    public static String gSearchMasterFormName, gLastPOSForDayEnd, gUpToTimeForAdvOrder, gUpToTimeForUrgentOrder;
    public static String gInrestoPOSWebServiceURL = "", gInrestoPOSId = "", gInrestoPOSKey = "", gPOSVerion;
    public static String gBenowXEmail = "", gBenowMerchantCode = "", gBenowAuthenticationKey = "", gBenowSalt = "";

    /**
     * ******************* 6th feb 2017
     * *****************************************
     */
    public static String gJioMoneyWebServiceURL = "", gJioMoneyMID = "", gJioMoneyTID = "", gJioMoneyActivationCode = "";
    public static boolean gJioMoneyIntegrationYN, gBenowIntegrationYN;
    /**
     * ******************* 6th feb 2017
     * *****************************************
     */

    private static final String EMAIL_PATTERN
	    = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
	    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static ArrayList<String> ListTDHOnModifierItem = new ArrayList<>();
    public static ArrayList<Double> ListTDHOnModifierItemMaxQTY = new ArrayList<>();
    public static ArrayList gArrListBillDetails, gArrListSearchData;
    public static List<Map> gListWeightPerBox = new ArrayList<Map>();

    public static Map<String, String> hmTakeAway;
    public static Map<String, String> hmUserForms;
    public static Map<String, String> hmActiveForms;
    public static Map gMapWeightPerBox = new LinkedHashMap();
    public static Map<String, ArrayList<String>> hmAdvOrder;
    public static Map<String, ArrayList<String>> mapHomeDeliveryForKOT;

    public static Vector vArrSearchColumnSize;

    public static Logger gLog;
    public static boolean gNewBillSeriesForNewDay, gAreaWisePromotions, gPrintItemsOnMoveKOTMoveTable,gPrintMoveTableMoveKOT,gPrintQtyTotal;
    public static boolean gShowOnlyLoginPOSReports, gEnableDineIn;
    public static boolean gAutoAreaSelectionInMakeKOT, gShowUnSettletmentForm, gPrintOpenItemsOnBill, gPrintHomeDeliveryYN, gWERAOnlineOrderIntegration;
    public static boolean gShowPurRateInDirectBiller,gLockTableForWaiter;
    public static String gConsolidatedKOTPrinterPort = "", gMMSSalesDataPostEffectCostOrLoc, gEffectOfSales = "No";
    public static double gRoundOffTo = 0.00;
    public static boolean gAutoShowPopItems, gPOSWiseItemToMMSProductLinkUpYN, gEnableMasterDiscount, gEnableNFCInterface, gEnableLockTables, gFireCommunication;
    public static String gPlayZonePOS = "", gRemoveSCTaxCode, gWERAMerchantOutletId, gWERAAuthenticationAPIKey;
    public static String gDineInAreaForDirectBiller, gHomeDeliveryAreaForDirectBiller, gTakeAwayAreaForDirectBiller;
    public static boolean gRoundOffBillFinalAmount, gSendDBBackUpOnClientMail, gPrintOrderNoOnBillYN, gPrintDeviceAndUserDtlOnKOTYN, gAutoAddKOTToBill, gAreaWiseCostCenterKOTPrinting;
    public static int gNoOfDecimalPlace = 2, gNoOfDaysReportsView = 0;
    public static double gUSDConvertionRate;
    public static String gShowReportsInCurrency,gPOSToMMSPostingCurrency,gPOSToWebBooksPostingCurrency;
    

// Constructor to initialize property setup parameters.
    public clsGlobalVarClass(String posCode) throws Exception
    {
	funSetGlobalParameters(posCode);
    }

    public static void funOpenRMSDBCon()
    {
	try
	{
	    String rmsConURL = "jdbc:sqlserver://" + clsGlobalVarClass.gRFIDDBServerName + ":1433;user=" + clsGlobalVarClass.gRFIDDBUserName + ";password=" + clsGlobalVarClass.gRFIDDBPassword + ";database=" + clsGlobalVarClass.gRFIDDBName + "";
	    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	    conRMS = DriverManager.getConnection(rmsConURL);
	    stmtRMS = conRMS.createStatement();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public static boolean funCheckUserRights(String formName)
    {
	boolean flgUserRight = false;
	try
	{
	    String sql = "";
	    if (clsGlobalVarClass.gSuperUser)
	    {
		sql = "select strUserCode from tblsuperuserdtl "
			+ "where strFormName='" + formName + "' and strUserCode='" + clsGlobalVarClass.gUserCode + "'";
	    }
	    else
	    {
		sql = "select strUserCode from tbluserdtl "
			+ "where strFormName='" + formName + "' and strUserCode='" + clsGlobalVarClass.gUserCode + "'";
	    }
	    ResultSet rsUserRights = dbMysql.executeResultSet(sql);
	    if (rsUserRights.next())
	    {
		flgUserRight = true;
	    }
	    rsUserRights.close();
	}
	catch (Exception e)
	{
	    flgUserRight = false;
	    e.printStackTrace();
	}
	finally
	{
	    return flgUserRight;
	}
    }

// Function to fill hashmap with tables which are used for take away operations.This function is invoked after login.    
    private void funFillTakeAwayMap() throws Exception
    {
	hmTakeAway = new HashMap<String, String>();
	sql = "select strTableNo from tblitemrtemp where strTakeAwayYesNo='Yes'";
	ResultSet rsTakeAway = dbMysql.executeResultSet(sql);
	while (rsTakeAway.next())
	{
	    hmTakeAway.put(rsTakeAway.getString(1), "Yes");
	}
	rsTakeAway.close();
    }

// Function to fill hashmap with req information used for Home Delivery operations.This function is invoked after login.    
    private void funFillCustInfoForHomeDelivery() throws Exception
    {
	/*mapHomeDeliveryForKOT = new HashMap<String, ArrayList<String>>();
         sql = "select a.strTableNo,a.strCustomerCode,b.strCustomerName,b.strBuldingCode,ifnull(a.strDelBoyCode,'NA')"
         + " ,ifnull(c.strDPName,'NA') "
         + " from tblitemrtemp a left outer join tblcustomermaster b on a.strCustomerCode=b.strCustomerCode "
         + " left outer join tbldeliverypersonmaster c on a.strDelBoyCode=c.strDPCode "
         + " where a.strHomeDelivery='Yes' and a.strPOSCode='"+clsGlobalVarClass.gPOSCode+"' ";
         ResultSet rsCustInfo = dbMysql.executeResultSet(sql);
         while (rsCustInfo.next())
         {
         funSetCustInfoForHD(rsCustInfo.getString(1), rsCustInfo.getString(2), rsCustInfo.getString(3), rsCustInfo.getString(4), rsCustInfo.getString(5), rsCustInfo.getString(6));
         }*/
    }

    /*
     // This function is called from make kot and direct biller forms.
     public static int funSetCustInfoForHD(String tableNo, String custCode, String custName, String buildingCode, String dpCode, String dpName)
     {
     ArrayList<String> arrListCustInfo = new ArrayList<String>();
     arrListCustInfo.add(custCode);//0 cust code
     arrListCustInfo.add(custName);//1 cust name
     arrListCustInfo.add(buildingCode);//2 building code
     arrListCustInfo.add("HomeDelivery");//3 home delivery
     if (dpCode.trim().length() > 0)
     {
     arrListCustInfo.add(dpCode);//4 del person code
     arrListCustInfo.add(dpName);//5 del person name
     }
     else
     {
     arrListCustInfo.add("");//4 del person code
     arrListCustInfo.add("");//5 del person name
     }
     if (mapHomeDeliveryForKOT.get(tableNo) != null)
     {
     mapHomeDeliveryForKOT.remove(tableNo);
     }
     mapHomeDeliveryForKOT.put(tableNo, arrListCustInfo);
     return 1;
     }

    
     public static double funGetMinBillAmountForDelCharges(String buildingCode, String custTypeCode)
     {
     double minAmount = 0.00;
     try
     {
     String sql = "select min(dblBillAmount) from tblareawisedc "
     + "where strBuildingCode='" + buildingCode + "' and strCustTypeCode='"+custTypeCode+"' ";
     ResultSet rsAmount = dbMysql.executeResultSet(sql);
     if (rsAmount.next())
     {
     minAmount = Double.parseDouble(rsAmount.getString(1));
     }
     rsAmount.close();
     }
     catch (Exception e)
     {
     e.printStackTrace();
     minAmount = 0.00;
     }
     return minAmount;
     }*/
// Function to check Stock of selected item is negative or positive. Invoked from direct biller and make kot based on global parameter value.
// Parameters:
    // itemCode-Selected item code.
    // billQty-Item qty for selected item.
    public static boolean funCheckNegativeStock(String itemCode, double billQty) throws Exception
    {
	boolean flgAvailStk = false;
	String sqlRecipe = "select strItemCode from tblrecipehd where strItemCode='" + itemCode + "'";
	ResultSet rsRecipe = clsGlobalVarClass.dbMysql.executeResultSet(sqlRecipe);
	if (rsRecipe.next())
	{
	    sqlRecipe = "select a.strItemCode,ifnull(c.strChildItemCode,''),ifnull(c.dblQuantity,0) "
		    + "from tblitemmaster a left outer join tblrecipehd b on a.strItemCode=b.strItemCode "
		    + "left outer join tblrecipedtl c on b.strRecipeCode=c.strRecipeCode "
		    + "where a.strItemCode='" + itemCode + "'";
	    ResultSet rsRecipeDtl = clsGlobalVarClass.dbMysql.executeResultSet(sqlRecipe);
	    while (rsRecipeDtl.next())
	    {
		if (funCheckNegativeStock(rsRecipeDtl.getString(2), rsRecipeDtl.getDouble(3) * billQty))
		{
		    flgAvailStk = true;
		}
		else
		{
		    flgAvailStk = false;
		}
	    }
	    rsRecipeDtl.close();
	}
	else
	{
	    double availableStk = clsGlobalVarClass.funGetStock(itemCode, "Item");
	    availableStk = availableStk - billQty;
	    if (availableStk < 0)
	    {
		JOptionPane.showMessageDialog(null, "Available Stock is " + availableStk);
	    }
	    else
	    {
		flgAvailStk = true;
	    }
	}
	rsRecipe.close();
	return flgAvailStk;
    }

// Function to calculate Stock of selected item.
// Parameters:
    // itemCode-Selected item code.
    // itemType-Item type.
    public static double funGetStock(String itemCode, String itemType) throws Exception
    {
	double saleQty = 0, rawMaterialSale = 0, stkInQty = 0, stkOutQty = 0, availableStock = 0;
	double qBillSaleQty = 0, qBillRawMaterialSale = 0;

	String selectQuery = "select sum(b.dblQuantity),a.strStkInCode from tblstkinhd a,tblstkindtl b "
		+ "where b.strItemCode='" + itemCode + "' and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		+ "and a.strStkInCode=b.strStkInCode";
	ResultSet rsItemCode = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
	if (rsItemCode.next())
	{
	    stkInQty = rsItemCode.getDouble(1);
	}

	selectQuery = "select sum(b.dblQuantity),a.strStkOutCode from tblstkouthd a,tblstkoutdtl b "
		+ "where b.strItemCode='" + itemCode + "' and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		+ "and a.strStkOutCode=b.strStkOutCode";
	rsItemCode = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
	if (rsItemCode.next())
	{
	    stkOutQty = rsItemCode.getDouble(1);
	}

	selectQuery = "select ifnull(sum(a.dblQuantity*c.dblQuantity),0) "
		+ "from tblrecipedtl a,tblrecipehd b,tblbilldtl c "
		+ "where a.strRecipeCode=b.strRecipeCode and b.strItemCode=c.strItemCode "
		+ "and a.strChildItemCode='" + itemCode + "'";
	rsItemCode = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
	if (rsItemCode.next())
	{
	    saleQty = rsItemCode.getDouble(1);
	}
	rsItemCode.close();

	selectQuery = "select ifnull(sum(a.dblQuantity*c.dblQuantity),0) "
		+ "from tblrecipedtl a,tblrecipehd b,tblqbilldtl c "
		+ "where a.strRecipeCode=b.strRecipeCode and b.strItemCode=c.strItemCode "
		+ "and a.strChildItemCode='" + itemCode + "'";
	rsItemCode = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
	if (rsItemCode.next())
	{
	    qBillSaleQty = rsItemCode.getDouble(1);
	}
	rsItemCode.close();

	String sqlRawMatSale = "select sum(b.dblQuantity) from tblbillhd a "
		+ " inner join tblbilldtl b on a.strBillNo=b.strBillNo "
		+ " and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		+ " and b.strItemCode='" + itemCode + "'";
	ResultSet rsRaw = clsGlobalVarClass.dbMysql.executeResultSet(sqlRawMatSale);
	if (rsRaw.next())
	{
	    rawMaterialSale = rsRaw.getDouble(1);
	}
	rsRaw.close();

	sqlRawMatSale = "select sum(b.dblQuantity) from tblqbillhd a "
		+ " inner join tblqbilldtl b on a.strBillNo=b.strBillNo "
		+ " and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		+ " and b.strItemCode='" + itemCode + "'";
	rsRaw = clsGlobalVarClass.dbMysql.executeResultSet(sqlRawMatSale);
	if (rsRaw.next())
	{
	    qBillRawMaterialSale = rsRaw.getDouble(1);
	}
	rsRaw.close();

	availableStock = stkInQty - (rawMaterialSale + qBillRawMaterialSale + saleQty + qBillSaleQty + stkOutQty);
	return availableStock;
    }


    /*
     // Function to generate search queries.   
     public static void funCallForSearchForm(String searchFormName)
     {
     try
     {
     gSearchFormName = searchFormName;
     vArrSearchColumnSize = new Vector();
     SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
     java.util.Date temDate = dFormat.parse(gPOSStartDate);
     String todate = (temDate.getYear() + 1900) + "-" + (temDate.getMonth() + 1) + "-" + temDate.getDate();
     switch (searchFormName)
     {
     case "MenuItem":
     gSearchMasterFormName="Item Master";
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

     case "MenuItemForPrice":
     gSearchMasterFormName="Item Master";
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
     gSearchMasterFormName="Item Master";
     gQueryForSearch = "select a.strItemCode as Item_Code,a.strItemName as Item_Name,a.strItemType as Item_Type,a.strRevenueHead as Revenue_Head,a.strTaxIndicator as Tax_Id,a.strExternalCode as External_Code,b.strSubGroupName as Sub_Group_Name "
     + "from tblitemmaster a,tblsubgrouphd b"
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
     gSearchMasterFormName="Menu Head Master";
     gQueryForSearch = "select strMenuCode as Menu_Code,strMenuName as Menu_Name,strOperational as Operational from tblmenuhd "
     + "order by strMenuName";
     vArrSearchColumnSize.add(50);
     vArrSearchColumnSize.add(200);
     vArrSearchColumnSize.add(100);
     break;

     case "MenuForCounter":
     gSearchMasterFormName="Menu Head Master";
     gQueryForSearch = "select strMenuCode as Menu_Code,strMenuName as Menu_Name from tblmenuhd where strOperational='Y' "
     + "order by strMenuName";
     vArrSearchColumnSize.add(250);
     vArrSearchColumnSize.add(250);
     break;

     case "SubMenu Master":
     gSearchMasterFormName="SubMenu Head Master";
     gQueryForSearch = "select strSubMenuHeadCode as SubMenu_Code,strSubMenuHeadName as SubMenuHead_Name,strSubMenuHeadShortName as SubMenu_Name,strSubMenuOperational as Operational from tblsubmenuhead "
     + "order by strSubMenuHeadName";
     vArrSearchColumnSize.add(500);
     vArrSearchColumnSize.add(200);
     vArrSearchColumnSize.add(200);
     vArrSearchColumnSize.add(100);
     break;

     case "TDH":
     gSearchMasterFormName="TDH Master";
     gQueryForSearch = "select a.strTDHCode as TDH_Code,a.strDescription as Description , a.strMenuCode as Menu_Code, b.strItemName as Item_Name,a.strItemCode  as Item_Code, a.intMaxQuantity as Quantity from tbltdhhd a,tblmenuitempricingdtl b where a.strItemCode=b.strItemCode and a.strComboItemYN='N' "
     + "order by strTDHCode";
     vArrSearchColumnSize.add(250);
     vArrSearchColumnSize.add(250);
     vArrSearchColumnSize.add(250);
     vArrSearchColumnSize.add(250);
     vArrSearchColumnSize.add(250);
     vArrSearchColumnSize.add(250);
     break;

     case "TDHOnItem":
     gSearchMasterFormName="TDH Master";
     gQueryForSearch = "select a.strTDHCode as TDH_Code,a.strDescription as Description , a.strMenuCode as Menu_Code, b.strItemName as Item_Name,a.strItemCode  as Item_Code, a.intMaxQuantity as Quantity from tbltdhhd a,tblmenuitempricingdtl b where a.strItemCode=b.strItemCode and a.strComboItemYN='Y' "
     + "order by strTDHCode";
     vArrSearchColumnSize.add(250);
     vArrSearchColumnSize.add(250);
     vArrSearchColumnSize.add(250);
     vArrSearchColumnSize.add(250);
     vArrSearchColumnSize.add(250);
     vArrSearchColumnSize.add(250);
     break;

     case "AreaMaster":
     gSearchMasterFormName="Area Master";
     gQueryForSearch = "select strAreaCode as Area_Code,strAreaName as Area_Name from tblareamaster "
     + "order by strAreaName";
     vArrSearchColumnSize.add(100);
     vArrSearchColumnSize.add(200);
     break;

     case "PSPCode":
     gSearchMasterFormName="PSP Master";
     gQueryForSearch = "select strPSPCode as PSP_Code,strStkInCode as StockIn_Code,strBillNo as BillNo,"
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
     gSearchMasterFormName="Reason Master";
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
     gSearchMasterFormName="Group Master";
     gQueryForSearch = "select strGroupCode as Group_Code,strGroupName as Group_Name from tblgrouphd "
     + "order by strGroupName";
     vArrSearchColumnSize.add(100);
     vArrSearchColumnSize.add(200);
     break;

     case "SubGroup":
     gSearchMasterFormName="SubGroup Master";
     gQueryForSearch = "select strSubGroupCode as SubGroup_Code,strSubGroupName as SubGroup_Name,"
     + "strGroupCode as Group_Code ,strIncentives as Incentives from tblsubgrouphd order by strSubGroupName";
     vArrSearchColumnSize.add(100);
     vArrSearchColumnSize.add(200);
     break;

     case "CostCenter":
     gSearchMasterFormName="Cost Center Master";
     gQueryForSearch = "select strCostCenterCode as CostCenter_Code,strCostCenterName as CostCenter_Name "
     + "from tblcostcentermaster order by strCostCenterName";
     vArrSearchColumnSize.add(100);
     vArrSearchColumnSize.add(200);
     break;

     case "Settlement":
     gSearchMasterFormName="Settlement Master";
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
     gSearchMasterFormName="Property Master";
     gQueryForSearch = "select strClientCode as Client_Code,strClientName as Client_Name,strAddressLine1 as Address1,"
     + "strAddressLine2 as Address2,strAddressLine3 as Address3,strEmail as Email_Id,"
     + "strBillFooter as Bill_Footer,strBillFooterStatus as BillFooter_Status,intBillPaperSize as Paper_Size,"
     + "strPrintMode as Print_Mode,strDiscountNote as Discount_Note,strCityName as City_Name,strState as State,"
     + "strCountry as Country,intTelephoneNo as Tele_No,dteStartDate as Start_Date,dteEndDate as End_Date,"
     + "strNturofBusinnes as NatureOfBusinnes from tblsetup order by strClientName";
     break;

     case "Pos":
     gSearchMasterFormName="POS Master";
     gQueryForSearch = "select strPosCode as POS_Code,strPosName as POS_Name,strPosType as POS_Type "
     + "from tblposmaster order by strPosName";
     vArrSearchColumnSize.add(80);
     vArrSearchColumnSize.add(200);
     vArrSearchColumnSize.add(200);
     break;

     case "Price":
     gSearchMasterFormName="Item Price";
     gQueryForSearch = "select a.strItemCode as Item_Code,a.strItemName ,ifnull(b.strPosName,'') as POS_Name,c.strMenuName as Menu_Name,\n"
     + "a.strPopular as Popular,ifnull(d.strCostCenterName,'') as CostCenter_Name,ifnull(e.strAreaName,'') as Area"
     + ",a.strHourlyPricing as Hourly_Price"
     + " from tblmenuitempricingdtl a left outer join tblareamaster e on a.strAreaCode=e.strAreaCode \n"
     + " left outer join tblposmaster b on (a.strPosCode=b.strPosCode or a.strPosCode='All') \n"
     + " left outer join tblmenuhd c  on a.strMenuCode=c.strMenuCode\n"
     + " left outer join tblcostcentermaster d on a.strCostCenterCode=d.strCostCenterCode\n"
     + " order by a.strItemName asc";
     break;

     case "Tax":
     gSearchMasterFormName="Tax Master";
     gQueryForSearch = "select strTaxCode as Tax_Code,strTaxDesc as Tax_Desc,strTaxOnSP as TaxOn_SP,"
     + "strTaxType as Tax_Type,dblPercent as Tax_Percent,dblAmount as Tax_Amount,"
     + "dteValidFrom as Valid_From,dteValidTo as Valid_To,strTaxOnGD as TaxOn_GD,"
     + "strTaxCalculation as Tax_Calculation,strTaxIndicator as Tax_Id,"
     + "strTaxRounded as Rounded,strTaxOnTax as TaxOn_Tax,strTaxOnTaxCode as TaxOn_TaxCode "
     + "from tbltaxhd order by strTaxDesc";
     break;

     case "TaxOnTax":
     gSearchMasterFormName="Tax Master";
     gQueryForSearch = "select strTaxCode as Tax_Code,strTaxDesc as Tax_Desc,strTaxOnSP as TaxOnSP,"
     + "strTaxType as Tax_Type,dblPercent as Tax_Percent,dblAmount as TaxAmount,"
     + "dteValidFrom as Valid_From,dteValidTo as Valid_To,strTaxOnGD as TaxOn_GD,"
     + "strTaxCalculation as Tax_Calculation,strTaxIndicator as Tax_Id,"
     + "strTaxRounded as Rounded,strTaxOnTax as TaxOnTax,strTaxOnTaxCode as TaxOnTaxCode "
     + "from tbltaxhd order by strTaxDesc";
     break;

     case "UserMaster":
     gSearchMasterFormName="User Master";
     gQueryForSearch = "select strUserCode as User_Code,strUserName as User_Name,strSuperType as User_Type,dteValidDate as Valid_Date,strPOSAccess as POS from tbluserhd order by strUserName";
     vArrSearchColumnSize.add(70);
     vArrSearchColumnSize.add(250);
     vArrSearchColumnSize.add(80);
     vArrSearchColumnSize.add(100);
     vArrSearchColumnSize.add(100);
     break;

     case "Modifier":
     gSearchMasterFormName="Modifier Master";
     gQueryForSearch = "select strModifierCode as Modifier_Code,strModifierName as Modifier_Name,"
     + "strModifierDesc as Modifier_Desc from tblmodifiermaster order by strModifierName";
     break;

     case "TableMaster":
     gSearchMasterFormName="Table Master";
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
     gSearchMasterFormName="Table Master";
     gQueryForSearch = "select a.strTableNo as Table_No,a.strTableName as Table_Name,a.strStatus as Status "
     + "from tbltablemaster a "
     + "where (a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPOSCode='All') "
     + "order by a.strTableName limit 10 ";

     vArrSearchColumnSize.add(150);
     vArrSearchColumnSize.add(150);
     vArrSearchColumnSize.add(150);
     break;

     case "WaiterMaster":
     gSearchMasterFormName="Waiter Master";
     gQueryForSearch = "select strWaiterNo as Waiter_No,strWShortName as Short_Name,"
     + "strWFullName as Full_Name,strStatus as Status "
     + "from tblwaitermaster where strOperational='Y'"
     + " order by strWShortName";
     vArrSearchColumnSize.add(50);
     vArrSearchColumnSize.add(150);
     vArrSearchColumnSize.add(150);
     vArrSearchColumnSize.add(150);
     break;

     case "DeliveryPersonMaster":
     gSearchMasterFormName="Delivery Person Master";
     gQueryForSearch = "select strDPCode as Person_Code,strDPName as Name, if(strOperational='Y','YES','NO') as Operational "
     + "from tbldeliverypersonmaster order by strDPName";
     vArrSearchColumnSize.add(100);
     vArrSearchColumnSize.add(200);
     vArrSearchColumnSize.add(200);
     break;

     case "DeliveryBoyMaster":
     gSearchMasterFormName="Delivery Boy Master";
     gQueryForSearch = "select strDPCode as Person_Code,strDPName as Name "
     + "from tbldeliverypersonmaster "
     + "where strOperational='Y' order by strDPName";
     vArrSearchColumnSize.add(250);
     vArrSearchColumnSize.add(250);
     break;

     case "BuildingMaster":
     gSearchMasterFormName="Building Master";
     gQueryForSearch = "select strBuildingCode as Bulding_Code,strBuildingName as Bulding_Name,"
     + "strAddress as Address from tblbuildingmaster order by strBuildingName";
     vArrSearchColumnSize.add(250);
     vArrSearchColumnSize.add(250);
     break;

     case "AvdBookReceipt":
     gSearchMasterFormName="Advance Booking Receipt Master";
     gQueryForSearch = "select a.strAdvBookingNo as Booking_No,a.strReceiptNo as Receipt_No,c.strCustomerName as 'Customer_Name' \n"
     + "from tbladvancereceipthd a, tbladvbookbillhd b ,tblcustomermaster c \n"
     + "where a.strAdvBookingNo=b.strAdvBookingNo and b.strCustomerCode=c.strCustomerCode order by a.strAdvBookingNo desc";
     vArrSearchColumnSize.add(80);
     vArrSearchColumnSize.add(200);
     vArrSearchColumnSize.add(200);
     break;

     case "UnsettleBill":
     gSearchMasterFormName="Unsettle Bill";
     gQueryForSearch = "select ifnull(d.strTableName,'ND') as Table_Name, a.strBillNo as Bill_No "
     + " ,a.dblGrandTotal as Total_Amount,c.strSettelmentDesc as Settle_Mode, a.strUserCreated as User "
     + " , a.strRemarks as Remarks "
     + " from tblbillhd a inner join tblbillsettlementdtl b on a.strbillno=b.strbillno "
     + " inner join tblsettelmenthd c on b.strSettlementCode=c.strSettelmentCode "
     + " left outer join tbltablemaster d on a.strTableNo=d.strTableNo "
     + " where date(a.dteBillDate)='" + clsGlobalVarClass.getOnlyPOSDateForTransaction() + "' "
     + " and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' ";
                    
     if (!clsGlobalVarClass.gSuperUser)
     {
     gQueryForSearch += " and a.strUserCreated='" + clsGlobalVarClass.gUserCode + "' ";
     }
     gQueryForSearch += " group by a.strbillno order by a.strbillno DESC";
     //System.out.println(gQueryForSearch);
     break;

     case "SplitBill":
     gSearchMasterFormName="Split Bill";
     gQueryForSearch = "select strBillNo as Bill_No,dteBillDate as Bill_Date,dblGrandTotal as Total_Amount "
     + " from tblbillhd "
     + " where strBillNo NOT IN(select strBillNo from tblbillsettlementdtl) and strBillNo NOT LIKE '%-%'"
     + " order by strbillno DESC";
     break;

     case "SalesReportBill":
     gSearchMasterFormName="Sales Report";
     gQueryForSearch = "select strbillno as Bill_No,dteBillDate as Bill_Date,strPOSCode As POS_Code,"
     + "dblGrandTotal as Total_Amount from tblbillhd order by strbillno DESC";
     break;

     case "ReprintBillAllPOS":
     gSearchMasterFormName="Reprint Bill";
     gQueryForSearch = "select strbillno as Bill_No,strTableNo as Table_No,dteBillDate as Bill_Date,strPOSCode as POS_Code,"
     + " dblGrandTotal as Total_Amount "
     + " from tblbillhd "
     + "where date(dteBillDate)='" + todate + "' and strPOSCode='" + gPosCodeForReprintDocs + "' "
     + " order by strbillno DESC";
     break;

     case "CustomerMaster":
     gSearchMasterFormName="Customer Master";
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
     }
     break;

     case "PluForAdvanceOrder":
     gSearchMasterFormName="Advance Order";
     gQueryForSearch = "select strItemCode as Item_Code,strItemName as Item_Name,"
     + "strSubGroupCode as SubGroup_Code,strTaxIndicator as Tax_Id,"
     + "strStockInEnable as StkIn_Enable,dblPurchaseRate as Purchase_Rate,"
     + "intProcTimeMin as Proc_Time,strExternalCode as External_Code,"
     + "strItemDetails as Item_Details from tblitemmaster  order by strItemName";
     break;

     case "Shift Master":
     gSearchMasterFormName="Shift Master";
     gQueryForSearch = "select intShiftCode,strPOSCode,dteDateCreated as ShiftDate,"
     + "tmeShiftStart as Shift_StartTime,tmeShiftEnd as Shift_EndTime,"
     + "strBillDateTimeType as BillDate_Type from tblshiftmaster order by dteDateCreated";
     break;

     case "CashManagement":
     gSearchMasterFormName="Cash Management Master";
     gQueryForSearch = "select strTransID as Trans_ID,strTransType as Trans_Type,"
     + "date(dteTransDate) as Trans-Date,strReasonCode as Reason,strPOSCode as POS_Code,"
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

     case "AreaMasterForMakeKOT":
     gSearchMasterFormName="Area Master";
     gQueryForSearch = "select strAreaCode as Area_Code,strAreaName as Area_Name "
     + " from tblareamaster "
     + " where (strPOSCode='" + clsGlobalVarClass.gPOSCode + "' or strPOSCode='All') "
     + " order by strAreaName";
     vArrSearchColumnSize.add(250);
     vArrSearchColumnSize.add(250);
     break;

     case "CardType":
     gSearchMasterFormName="Cash Type Master";
     gQueryForSearch = "select strCardTypeCode as Card_Code,strCardName as Card_Name "
     + "from tbldebitcardtype order by strCardTypeCode";
     vArrSearchColumnSize.add(100);
     vArrSearchColumnSize.add(200);
     break;

     case "CardNo":
     gSearchMasterFormName="Card Type Master";
     gQueryForSearch = "select strCardNo as Card_Number,strCardTypeCode as Card_Code,"
     + "strStatus as Status from tbldebitcardmaster order by strCardNo";
     vArrSearchColumnSize.add(165);
     vArrSearchColumnSize.add(165);
     vArrSearchColumnSize.add(165);
     break;

     case "ReprintKOT":
     gSearchMasterFormName="Reprint KOT";
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
     gSearchMasterFormName="GiftVoucher Master";
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
     gSearchMasterFormName="GiftVoucher Master";
     gQueryForSearch = "select strGiftVoucherName as GiftVoucher_Name, strGiftVoucherSeries as GiftVoucher_Series,"
     + " strGiftVoucherValueType as GiftVoucher_Type from  tblgiftvoucher;";
     vArrSearchColumnSize.add(100);
     vArrSearchColumnSize.add(100);
     vArrSearchColumnSize.add(100);
     break;

     case "CustomerAddress": 
     gSearchMasterFormName="Customer Master";
     gQueryForSearch = "select strCustomerCode,strCustomerName,strBuildingName,strStreetName"
     + ",strCity,'Home' "
     + "from tblcustomermaster where longMobileNo=" + clsGlobalVarClass.gCustMBNo + " "
     + "union all "
     + "select strCustomerCode,strCustomerName,strOfficeBuildingName,strOfficeStreetName"
     + ",strOfficeCity,'Office' "
     + "from tblcustomermaster where longMobileNo=" + clsGlobalVarClass.gCustMBNo + " and "
     + " CHAR_LENGTH(strOfficeBuildingName)>0 "
     + "order by strCustomerCode";                    
     break;

     case "CustTypeMaster":
     gSearchMasterFormName="Customer Type Master";
     gQueryForSearch = "select strCustTypeCode,strCustType from tblcustomertypemaster "
     + "order by strCustTypeCode";
     break;

     case "ExpireDebitCard":
     gSearchMasterFormName="Card Details";
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
     gSearchMasterFormName="StockIn Master";
     gQueryForSearch = "select a.strStkInCode,a.strReasonCode,b.strReasonName,a.dteDateCreated "
     + "from tblstkinhd a left outer join tblreasonmaster b "
     + "on a.strReasonCode=b.strReasonCode "
     + "where a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' and "
     + "a.strReasonCode=b.strReasonCode order by a.strStkInCode";
     vArrSearchColumnSize.add(100);
     vArrSearchColumnSize.add(100);
     vArrSearchColumnSize.add(100);
     vArrSearchColumnSize.add(100);
     break;

     case "StockOut":
     gSearchMasterFormName="StockOut Master";
     gQueryForSearch = "select a.strStkOutCode,a.strReasonCode, b.strReasonName ,a.dteDateCreated "
     + "from tblstkouthd a left outer join tblreasonmaster b "
     + "on a.strReasonCode=b.strReasonCode "
     + "where a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' and "
     + "a.strReasonCode=b.strReasonCode order by a.strStkOutCode";
     vArrSearchColumnSize.add(100);
     vArrSearchColumnSize.add(100);
     vArrSearchColumnSize.add(100);
     vArrSearchColumnSize.add(100);
     break;

     case "Counter":
     gSearchMasterFormName="Counter Master";
     gQueryForSearch = "select a.strCounterCode as Counter_Code,a.strCounterName as Counter_Name,a.strOperational as Operational from tblcounterhd a \n"
     + " group by  a.strCounterCode";
     vArrSearchColumnSize.add(100);
     vArrSearchColumnSize.add(100);
     vArrSearchColumnSize.add(100);
     break;

     case "CounterForOperation":
     gSearchMasterFormName="Counter Master";
     gQueryForSearch = "select a.strCounterCode as Counter_Code,a.strCounterName as Counter_Name,a.strOperational as Operational"
     + " from tblcounterhd a  where a.strOperational='Yes'"
     + " group by  a.strCounterCode";
     vArrSearchColumnSize.add(100);
     vArrSearchColumnSize.add(100);
     vArrSearchColumnSize.add(100);
     break;

     case "ModifierGroup":
     gSearchMasterFormName="SModifier Group Master";
     gQueryForSearch = "select strModifierGroupCode as Modifier_GroupCode,strModifierGroupName as Modifier_GroupName,"
     + " strModifierGroupShortName as Modifier_GroupShortName,strOperational as Operational "
     + " from tblmodifiergrouphd group by strModifierGroupCode";
     vArrSearchColumnSize.add(100);
     vArrSearchColumnSize.add(100);
     vArrSearchColumnSize.add(100);
     vArrSearchColumnSize.add(100);
     break;

     case "LoyaltyMaster":
     gSearchMasterFormName="Loyalty Master";
     gQueryForSearch = "select strLoyaltyCode,dblAmount,dblLoyaltyPoints,dblLoyaltyPoints1,dblLoyaltyValue "
     + "from tblloyaltypoints";
     break;

     case "Recipe":
     gSearchMasterFormName="Recipe Master";
     gQueryForSearch = "select a.strRecipeCode,a.strItemCode,b.strItemName "
     + " from tblrecipehd a left outer join tblitemmaster b on a.strItemCode=b.strItemCode ";
     break;

     case "CloseProductionOrder":
     gSearchMasterFormName="Production Details";
     gQueryForSearch = "select strProductionCode,dteProductionDate,strClose,strRemarks "
     + "from tblproductionhd where strClose='N'";
     break;

     case "AdvOrderTypeMaster":
     gSearchMasterFormName="Advance Order Type Details";
     gQueryForSearch = "select strAdvOrderTypeCode,strAdvOrderTypeName,strOperational "
     + "from tbladvanceordertypemaster";
     break;

     case "PromoCode":
     gSearchMasterFormName="Promotion Master";
     gQueryForSearch = "select strPromoCode,strPromoName,strPromotionOn "
     + "from tblpromotionmaster order by strPromoCode";
     break;

     case "DeliveryBoyCategoryMaster":
     gSearchMasterFormName="Delivery Boy Category Master";
     gQueryForSearch = "select a.strDelBoyCategoryCode as DeliveryBoy_CategoryCode,a.strDelBoyCategoryName as DeliveryBoy_CategoryName "
     + "from tbldeliveryboycategorymaster a order by a.strDelBoyCategoryCode ";
     vArrSearchColumnSize.add(100);
     vArrSearchColumnSize.add(200);
     break;

     case "AreaWiseDeliveryBoyCharges":
     gSearchMasterFormName="Areawise Delivery Boy Charges Deatils";
     gQueryForSearch = "select a.strCustAreaCode,b.strBuildingName,a.strDeliveryBoyCode,c.strDPName,a.dblValue from tblareawisedelboywisecharges a\n"
     + "left outer join tblbuildingmaster b on a.strCustAreaCode=b.strBuildingCode\n"
     + "left outer join tbldeliverypersonmaster c on a.strDeliveryBoyCode=c.strDPCode";
     break;

     case "PhysicalStock":
     gSearchMasterFormName="Physical Stock Details";
     gQueryForSearch = "select a.strPSPCode ,b.strItemCode, c.strItemName,a.dteDateCreated "
     + " from tblPSPhd a,tblPSPdtl b,tblItemMaster c "
     + "  where a.strPSPCode=b.strPSPCode  and b.strItemCode=c.strItemCode "
     + "  and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
     + "  group by a.strPSPCode";
     vArrSearchColumnSize.add(100);
     break;

     case "ZoneMaster":
     gSearchMasterFormName="Zone Master";
     gQueryForSearch = "select a.strZoneCode as ZoneCode,a.strZoneName as ZoneName "
     + "from tblzonemaster a order by a.strZoneCode ";
     vArrSearchColumnSize.add(250);
     vArrSearchColumnSize.add(250);
     break;

     case "VoidAdvOrder":
     gSearchMasterFormName="Advance Order Deatil";
     gQueryForSearch = "select a.strAdvBookingNo,c.strCustomerName,date(a.dteOrderFor),"
     + "sum(b.dblAdvDeposite),a.dblGrandTotal,b.strReceiptNo "
     + " from tbladvbookbillhd a, tbladvancereceipthd b,tblcustomermaster c "
     + " where a.strAdvBookingNo=b.strAdvBookingNo and "
     + "a.strCustomerCode=c.strCustomerCode and a.strAdvBookingNo "
     + " NOT IN(select strAdvBookingNo from tblbillhd) GROUP by a.strAdvBookingNo";
     System.out.println(gQueryForSearch);
     vArrSearchColumnSize.add(120);
     vArrSearchColumnSize.add(60);
     vArrSearchColumnSize.add(70);
     vArrSearchColumnSize.add(70);
     vArrSearchColumnSize.add(60);
     vArrSearchColumnSize.add(50);
     break;

     case "ContactNo":
     gSearchMasterFormName="Customer Master";
     gQueryForSearch = "select strCustomerCode,strCustomerName,longMobileNo from tblcustomermaster";
     vArrSearchColumnSize.add(250);
     vArrSearchColumnSize.add(250);
     vArrSearchColumnSize.add(250);
     break;

     case "TableReservation":
     gSearchMasterFormName="Table Reservation Master";
     gQueryForSearch = "select a.strResCode,b.strCustomerName,b.strBuldingCode,b.strBuildingName"
     + ",b.strCity from tblreservation a,tblcustomermaster b,tblbuildingmaster c\n"
     + "where a.strCustomerCode=b.strCustomerCode and b.strBuldingCode=c.strBuildingCode ";
     vArrSearchColumnSize.add(50);
     vArrSearchColumnSize.add(350);
     vArrSearchColumnSize.add(40);
     vArrSearchColumnSize.add(50);
     vArrSearchColumnSize.add(80);
     break;
     }
     }
     catch (Exception e)
     {
     e.printStackTrace();
     }
     }
     */
    public static void funLoadDB() throws Exception, clsSPOSException
    {

	gLog = Logger.getLogger(clsGlobalVarClass.class.getName());
	dbMysql = new clsDatabaseConnection();
	dbMysql.open("mysql");

    }

    public static void funCloseDB()
    {
	try
	{
	    dbMysql.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public static void setFormHeader()
    {
	try
	{
	    int userCodeLength = 15 - gUserCode.length();
	    int posNameLength = 45 - (15 + gPOSName.length());
	    gFormHeader = gUserCode;
	    for (int i = 0; i < userCodeLength; i++)
	    {
		gFormHeader = gFormHeader + " ";
	    }
	    gFormHeader = gFormHeader + gPOSName;
	    for (int i = 0; i < posNameLength; i++)
	    {
		gFormHeader = gFormHeader + " ";
	    }
	    gFormHeader = gFormHeader + gPOSDateToDisplay;

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public static void setPOSDateForCalender(String pDate)
    {
	try
	{
	    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
	    gCalendarDate = format.parse(pDate);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public static void setKeyboardValue(String value)
    {
	gKeyboardValue = value;
    }

    public static void setUserName(String uName)
    {
	gUserName = uName;
    }

    public static void setUserCode(String uCode)
    {
	gUserCode = uCode;
    }

    public static void setCounterWiseBilling(String counter)
    {
	gCounterWise = counter;
    }

    public static void setUserType(String uType)
    {
	gUserType = uType;
    }

    public static void setPOSCode(String pCode)
    {
	gPOSCode = pCode;
    }

    public static void setPropertyCode(String propertyCode)
    {
	if (gHOPOSType.equals("HOPOS"))
	{
	    gPropertyCode = propertyCode;
	}
	else
	{
	    gPropertyCode = gClientCode + "." + gPOSCode;
	}
    }

    public static void setPOSName(String pName)
    {
	gPOSName = pName;
    }

    public static void setPOSDateForTransaction(String pDate)
    {
	gPOSDateForTransaction = pDate;
    }

    public static String getPOSDateForTransaction()
    {
	StringBuilder sb = new StringBuilder(gPOSDateForTransaction);
	gPOSDateForTransaction = sb.delete(sb.indexOf(" "), sb.length()).toString();
	Date dtCurrent = new Date();
	String currentTime = dtCurrent.getHours() + ":" + dtCurrent.getMinutes() + ":" + dtCurrent.getSeconds();
	/*String currentTime="";
         try
         {
         ResultSet rsServerTime=clsGlobalVarClass.dbMysql.executeResultSet("select right(SYSDATE(),8)");
         if(rsServerTime.next())
         {
         currentTime=rsServerTime.getString(1);
         }
         rsServerTime.close();
            
         }catch(Exception e)
         {
         e.printStackTrace();
         }*/
	gPOSDateForTransaction += " " + currentTime;
	return gPOSDateForTransaction;
    }

    public static String getOnlyPOSDateForTransaction()
    {
	StringBuilder sb = new StringBuilder(gPOSDateForTransaction);
	gPOSOnlyDateForTransaction = sb.delete(sb.indexOf(" "), sb.length()).toString();
	return gPOSOnlyDateForTransaction;
    }

    public static void setPOSDateToDisplay(String pDate)
    {
	gPOSDateToDisplay = pDate;
    }

    public static String getCurrentDateTime()
    {
	Date currentDate = new Date();
	String strCurrentDate = ((currentDate.getYear() + 1900) + "-" + (currentDate.getMonth() + 1) + "-" + currentDate.getDate())
		+ " " + currentDate.getHours() + ":" + currentDate.getMinutes() + ":" + currentDate.getSeconds();
	return strCurrentDate;
    }

    public static String convertString(String ItemName)
    {
	try
	{
	    if (ItemName.contains(" "))
	    {
		StringBuilder sb1 = new StringBuilder(ItemName);
		int len = sb1.length();
		int seq = sb1.lastIndexOf(" ");
		String split = sb1.substring(0, seq);
		String last = sb1.substring(seq + 1, len);
		ItemName = "<html>" + split + "<br>" + last + "</html>";
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return ItemName;
    }

    public static boolean validateEmpty(String text)
    {
	validateFlag = true;
	try
	{
	    if (text.trim().length() == 0)
	    {
		validateFlag = false;
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return validateFlag;
    }

    public static boolean validateEmail(final String hex)
    {
	boolean flgEmailId = true;
	String[] spEmailId = hex.split(",");
	for (int i = 0; i < spEmailId.length; i++)
	{
	    pattern = Pattern.compile(EMAIL_PATTERN);
	    matcher = pattern.matcher(spEmailId[i]);
	    if (!matcher.matches())
	    {
		flgEmailId = false;
		break;
	    }
	}
	return flgEmailId;
    }

    public static void setStartDate(String sDate)
    {
	try
	{
	    gPOSStartDate = sDate;
	    String bdte = clsGlobalVarClass.gPOSStartDate;
	    SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
	    Date bDate = dFormat.parse(bdte);
	    gPOSDate = DateFormat.getDateTimeInstance().format(bDate);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public static void funSetPOSDate()
    {
	try
	{
	    String bdte = clsGlobalVarClass.gPOSStartDate;
	    SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
	    Date bDate = dFormat.parse(bdte);
	    String date1 = bDate.getDate() + "-" + (bDate.getMonth() + 1) + "-" + (bDate.getYear() + 1900);
	    clsGlobalVarClass.setPOSDateToDisplay(date1);
	    String date2 = (bDate.getYear() + 1900) + "-" + (bDate.getMonth() + 1) + "-" + bDate.getDate();
	    //String time2 = bDate.getHours() + ":" + bDate.getMinutes() + ":" + bDate.getSeconds();
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
		e.printStackTrace();
	    }

	    clsGlobalVarClass.setPOSDateForTransaction(date2 + " " + currentTime);
	    clsGlobalVarClass.setPOSDateForCalender(date1);
	    clsGlobalVarClass.setFormHeader();
	    clsGlobalVarClass.gPOSOnlyDateForTransaction = date2;

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public static String funConvertDateToSimpleFormat(String dateToConvert)
    {
	String[] spDateTime = dateToConvert.split(" ");
	String[] spDate = spDateTime[0].split("-");
	return spDate[2] + "-" + spDate[1] + "-" + spDate[0];
    }

    public static void setAdvanceOrderForDate(String advOderDate)
    {
	OrderForDate = advOderDate;
    }

    public static String getAdvanceOrderForDate()
    {
	return OrderForDate;
    }

    public static String getAdvReceiptNo()
    {
	return AdvReceiptNo;
    }

    public static void setAdvanceAmt(String advAtmt)
    {
	AdvAmt = advAtmt;
    }

    public static String getAdvanceAmt()
    {
	return AdvAmt;
    }

    public static void setAdvOrderNo(String advOrderNo)
    {
	AdvOrderNo = advOrderNo;
    }

    public static String getAdvOrderNo()
    {
	return AdvOrderNo;
    }

    public static boolean validateNumbers(String text)
    {
	validateFlag = true;
	try
	{
	    double num = Double.parseDouble(text);
	}
	catch (NumberFormatException numFormatEx)
	{
	    validateFlag = false;
	}
	finally
	{
	    return validateFlag;
	}
    }

    public static boolean validateIntegers(String text)
    {
	validateFlag = true;
	try
	{
	    int num = Integer.parseInt(text);
	}
	catch (NumberFormatException numFormatEx)
	{
	    validateFlag = false;
	}
	finally
	{
	    return validateFlag;
	}
    }

// Function to check Unsettled bills.
    public static boolean funCheckBillForSettle(String billNo)
    {
	boolean flgSettledBill = false;
	try
	{
	    String sql = "select count(*) from tblbillhd where strTableNo is not NULL and strBillNo"
		    + " IN(select strBillNo from tblbillsettlementdtl where strBillNo='" + billNo + "')";
	    //System.out.println(sql);
	    ResultSet rsSettledBill = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    rsSettledBill.next();
	    if (rsSettledBill.getInt(1) > 0)
	    {
		flgSettledBill = true;
	    }
	    rsSettledBill.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return flgSettledBill;
    }

    /* changes made in this method on Date 12 Sept 2014 : Ritesh*/
    public static void funCheckHomeDelivery(String BillNo)
    {
	try
	{
	    String sql = "select count(strBillNo),strCustomerCode "
		    + "from tblhomedelivery where strBillNo='" + BillNo + "'";
	    //System.out.println("Home Del\t"+sql);
	    ResultSet rsHomeDelBillNo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsHomeDelBillNo.next())
	    {
		if (rsHomeDelBillNo.getInt(1) > 0)
		{
		    clsGlobalVarClass.gHomeDeliveryForSelectedBillNo = true;
		    clsGlobalVarClass.gCustomerCode = rsHomeDelBillNo.getString(2);
		}
		else
		{
		    clsGlobalVarClass.gHomeDeliveryForSelectedBillNo = false;
		    clsGlobalVarClass.gCustomerCode = null;
		}
	    }
	    rsHomeDelBillNo.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

//    public static double funGetDebitCardBalance(String debitCardNo)
//    {
//
//        double cardBalance = 0;
//        if (debitCardNo != null)
//        {
//            try
//            {
//                //to get card balance
//                String sql1 = "select dblRedeemAmt from tbldebitcardmaster "
//                              + " where strCardNo='" + debitCardNo + "' ";
//                //System.out.println("sql1:" + sql1);
//                ResultSet rsCardBal = clsGlobalVarClass.dbMysql.executeResultSet(sql1);
//                rsCardBal.next();
//                cardBalance = rsCardBal.getDouble(1);
//
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//        }
//        return cardBalance;
//    }
//
//    public static String funGetDebitCardStatus(String debitCardNo)
//    {
//
//        String cardStatus = "Deactive";
//        if (debitCardNo != null)
//        {
//            try
//            {
//                String sql2 = "select count(*) from tbldebitcardmaster "
//                              + "where strCardNo='" + debitCardNo + "' and strStatus='Active' ";
//
//                ResultSet rsCardStatus = clsGlobalVarClass.dbMysql.executeResultSet(sql2);
//                if (rsCardStatus.next())
//                {
//                    if (rsCardStatus.getInt(1) > 0)
//                    {
//                        cardStatus = "Active";
//                    }
//                }
//                rsCardStatus.close();
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//        }
//        return cardStatus;
//    }
//
//    public static boolean funCheckLength(String text, int length)
//    {
//        boolean flagValidateLength = false;
//        try
//        {
//            if (text.trim().length() <= length)
//            {
//                flagValidateLength = true;
//            }
//            else
//            {
//                flagValidateLength = false;
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//
//        }
//        finally
//        {
//            return flagValidateLength;
//        }
//    }
//
//    public static boolean funCheckLengthForContactNos(String text, int length)
//    {
//        boolean flagValidateLength = false;
//        try
//        {
//            if (text.trim().length() >= 6 && text.trim().length() <= length)
//            {
//                flagValidateLength = true;
//            }
//            else
//            {
//                flagValidateLength = false;
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//
//        }
//        finally
//        {
//            return flagValidateLength;
//        }
//    }
////
//    public static boolean funCheckTakeAwayTable(String TableNo, String formName)
//    {
//        boolean flagTakeAway = false;
//        try
//        {
//            String sql = "";
//            if ("SaveKOT".equalsIgnoreCase(formName))
//            {
//                sql = "select count(*) from tblitemrtemp where strTableNo='" + TableNo + "' and strTakeAwayYesNo='Yes'";
//            }
//            if ("ReprintKOT".equalsIgnoreCase(formName))
//            {
//                sql = "select count(*) from tblitemrtemp where strKOTNo='" + TableNo + "'  and strTakeAwayYesNo='Yes' group by strKOTNo";
//            }
//            ResultSet rsTakeAway = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//            rsTakeAway.next();
//            int exec = rsTakeAway.getInt(1);
//            rsTakeAway.close();
//            if (exec > 0)
//            {
//                flagTakeAway = true;
//            }
//            else
//            {
//                flagTakeAway = false;
//            }
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//
//        }
//        finally
//        {
//            return flagTakeAway;
//        }
//    }
//
//    public static boolean funCkeckKOTWithoutWaiter(String TableNo, String formName)
//    {
//        boolean flagCkeckKOTWithoutWaiter = false;
//        try
//        {
//            String sql = "";
//            if (formName.equals("KOT"))
//            {
//                sql = "select strWaiterNo  from tblitemrtemp where strTableNo='" + TableNo + "' group by strTableNo";
//            }
//            if (formName.equals("ReprintKOT"))
//            {
//                sql = "select strWaiterNo  from tblitemrtemp where strKOTNo='" + TableNo + "' group by strKOTNo";
//            }
//            if (formName.equals("SettleBill"))
//            {
//                sql = "select strWaiterNo  from tblbillhd where strBillNo='" + TableNo + "' group by strBillNo";
//            }
//            if (formName.equals("VoidKOT"))
//            {
//                sql = "select strWaiterNo  from tbltempvoidkot where strKOTNo='" + TableNo + "' group by strTableNo";
//            }
//            // System.out.println(sql);
//            ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//            rs.next();
//            String waiterNo = rs.getString(1);
//            rs.close();
//            if ("null".equalsIgnoreCase(waiterNo))
//            {
//                flagCkeckKOTWithoutWaiter = true;
//            }
//            else
//            {
//                flagCkeckKOTWithoutWaiter = false;
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        finally
//        {
//            return flagCkeckKOTWithoutWaiter;
//        }
//    }
//
//    public static boolean funCkeckBillWithoutWaiter(String BillNo)
//    {
//        boolean flagCkeckBillWithoutWaiter = false;
//        try
//        {
//            String sql = "";
//            sql = "select strWaiterNo  from tblbillhd where strBillNo='" + BillNo + "' group by strBillNo";
//            ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//            rs.next();
//            String waiterNo = rs.getString(1);
//            rs.close();
//            if ("null".equalsIgnoreCase(waiterNo))
//            {
//                flagCkeckBillWithoutWaiter = true;
//            }
//            else
//            {
//                flagCkeckBillWithoutWaiter = false;
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        finally
//        {
//            return flagCkeckBillWithoutWaiter;
//        }
//    }
//    public static boolean funCheckComplementaryBill(String BillNo)
//    {
//        boolean flagComplementaryBill = false;
//        String sql = "";
//        ResultSet rs;
//        try
//        {
//            sql = "select strSettelmentMode from tblbillhd where strBillNo='" + BillNo + "';";
//            rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//            rs.next();
//            String settlmentMode = rs.getString(1);
//            if ("Complementary".trim().equals(settlmentMode))
//            {
//                flagComplementaryBill = true;
//            }
//            else
//            {
//                flagComplementaryBill = false;
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        finally
//        {
//            return flagComplementaryBill;
//        }
//    }
//    public static String funReasonName(String BillNo)
//    {
//        String ReasonName = "";
//        String sql = "";
//        ResultSet rs = null;
//        try
//        {
//            ReasonName = " ";
//            sql = "select b.strReasonName from tblbillhd a,tblreasonmaster b where a.strReasonCode=b.strReasonCode "
//                  + "and strBillNo='" + BillNo + "'";
//            rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//            if (rs.next())
//            {
//                ReasonName = rs.getString(1);
//            }
//            rs.close();
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        finally
//        {
//            return ReasonName;
//        }
//    }
//    public static String funGetRemark(String BillNo, String FormName)
//    {
//        String Remark = "";
//        String sql = "";
//        ResultSet rs = null;
//        try
//        {
//            sql = "select strRemarks from  tblbillhd where strBillNo='" + BillNo + "'";
//            rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//            if (rs.next())
//            {
//                Remark = rs.getString(1);
//            }
//            else
//            {
//                Remark = " ";
//            }
//            rs.close();
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        finally
//        {
//
//            return Remark;
//        }
//    }
    public static String funSendSMS(String url)
    {
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
	    }
	    in.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return output.toString();
    }

//    public static String funGetDayForPricing()
//    {
//        String day = "";
//        String[] dayPrice =
//        {
//            "strPriceSunday", "strPriceMonday", "strPriceTuesday", "strPriceWednesday", "strPriceThursday", "strPriceFriday", "strPriceSaturday"
//        };
//
//        String dayNames[] = new DateFormatSymbols().getWeekdays();
//        Calendar date2 = Calendar.getInstance();
//        String tempday = dayNames[date2.get(Calendar.DAY_OF_WEEK)];
//        switch (tempday)
//        {
//            case "Sunday":
//                day = "strPriceSunday";
//                break;
//
//            case "Monday":
//                day = "strPriceMonday";
//                break;
//
//            case "Tuesday":
//                day = "strPriceTuesday";
//                break;
//
//            case "Wednesday":
//                day = "strPriceWednesday";
//                break;
//
//            case "Thursday":
//                day = "strPriceThursday";
//                break;
//
//            case "Friday":
//                day = "strPriceFriday";
//                break;
//
//            case "Saturday":
//                day = "strPriceSaturday";
//                break;
//
//            default:
//                day = "strPriceSunday";
//
//        }
//        return day;
//    }
//    public static String funGetCurrentTime()
//    {
//        String currentTime = "";
//        Date dt = new Date();
//        int hours = dt.getHours();
//        int minutes = dt.getMinutes();
//        if (hours > 12)
//        {
//            //hours=hours-12;
//            currentTime = hours + ":" + minutes + " PM";
//        }
//        else
//        {
//            currentTime = hours + ":" + minutes + " AM";
//        }
//        return currentTime;
//    }
//
//    public static String funGetCurrentDate()
//    {
//
//        Calendar objDate = new GregorianCalendar();
//        String currentDate = (objDate.getTime().getYear() + 1900) + "-" + (objDate.getTime().getMonth() + 1) + "-" + objDate.getTime().getDate();
//        return currentDate;
//    }
//
//    public static long funCompareTime(String fromDate, String toDate)
//    {
//        long diff = 0;
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date d1 = null;
//        Date d2 = null;
//
//        try
//        {
//            d1 = format.parse(fromDate);
//            d2 = format.parse(toDate);
//
//            diff = d2.getTime() - d1.getTime();
//            long diffSeconds = diff / 1000 % 60;
//            long diffMinutes = diff / (60 * 1000) % 60;
//            long diffHours = diff / (60 * 60 * 1000) % 24;
//            long diffDays = diff / (24 * 60 * 60 * 1000);
//            String time = diffHours + ":" + diffMinutes + ":" + diffSeconds;
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        finally
//        {
//            return diff;
//        }
//    }
//    public static int funGetDeliveryCharges(String buildingCode, double totalBillAmount)
//    {
//        double billAmount = 0.00;
//        String sqlBuilding = "";
//        try
//        {
//            if (clsGlobalVarClass.gSlabBasedHDCharges.equals("Y"))
//            {
//                billAmount = totalBillAmount;
//                /*
//                sqlBuilding = "select IFNULL(dblDeliveryCharges,0.00) from tblareawisedc "
//                    + "where strBuildingCode='" + buildingCode + "' "
//                    + "and " + billAmount + " >=dblBillAmount and " + billAmount + " <= dblBillAmount1";*/
//                
//                sqlBuilding = "select IFNULL(a.dblDeliveryCharges,0.00) "
//                    + " from tblareawisedc a, tblcustomermaster b "
//                    + " where a.strCustTypeCode=b.strCustomerType and a.strBuildingCode='"+buildingCode+"' "
//                    + " and "+billAmount+" >=a.dblBillAmount and "+billAmount+" <= a.dblBillAmount1 "
//                    + " and b.strCustomerCode='"+clsGlobalVarClass.gCustCodeForAdvOrder+"'";
//            }
//            else
//            {
//                sqlBuilding = "select IFNULL(dblHomeDeliCharge,0.00) from tblbuildingmaster "
//                    + "where strBuildingCode='" + buildingCode + "'";
//            }
//
//            //System.out.println(sqlBuilding);
//            ResultSet rsDelCharges = clsGlobalVarClass.dbMysql.executeResultSet(sqlBuilding);
//            if (rsDelCharges.next())
//            {
//                clsGlobalVarClass.gDeliveryCharges = rsDelCharges.getDouble(1);
//            }
//            else
//            {
//                clsGlobalVarClass.gDeliveryCharges = 0.00;
//            }
//            rsDelCharges.close();
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        return 1;
//    }
//
//    public static String funCheckMemeberBalance(String memCode) throws Exception
//    {
//        String memberInfo = "";
//        double balance = 0, creditLimit = 0;
//
//        String cmsURL = clsGlobalVarClass.gCMSWebServiceURL + "/funGetCMSMember?strMemberCode=" + memCode;
//        URL url = new URL(cmsURL);
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
//        conn.setRequestProperty("Accept", "application/json");
//        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
//        String output = "", op = "";
//        while ((output = br.readLine()) != null)
//        {
//            op += output;
//        }
//        String jsonString = op;
//        JSONParser parser = new JSONParser();
//        Object obj = parser.parse(jsonString);
//        JSONObject jObj = (JSONObject) obj;
//        JSONArray mJsonArray = (JSONArray) jObj.get("MemberInfo");
//
//        JSONObject mJsonObject = new JSONObject();
//        for (int i = 0; i < mJsonArray.size(); i++)
//        {
//            mJsonObject = (JSONObject) mJsonArray.get(i);
//            if (mJsonObject.get("DebtorCode").toString().equals("no data"))
//            {
//                memberInfo = "no data";
//            }
//            else
//            {
//                memberInfo = mJsonObject.get("DebtorCode").toString() + "#" + mJsonObject.get("DebtorName").toString();
//                balance = Double.parseDouble(mJsonObject.get("BalanceAmt").toString());
//                creditLimit = Double.parseDouble(mJsonObject.get("CreditLimit").toString());
//                String expired = mJsonObject.get("Expired").toString();
//                double settleBalance = creditLimit - balance;
//                memberInfo += "#" + balance + "#" + settleBalance + "#" + expired;
//            }
//        }
//        conn.disconnect();
//
//        return memberInfo;
//    }
    public static boolean funGetConnectionStatus()
    {
	boolean flgHOStatus = false;
	gConnectionActive = "N";
	if (gHOPOSType.equals("Stand Alone") || gHOPOSType.equals("HOPOS"))
	{
	    return false;
	}
	try
	{
	    if (clsPosConfigFile.gHOCommunication.equals("true"))
	    {
		String hoURL = gSanguineWebServiceURL + "/POSIntegration/funInvokeHOWebService";
		URL url = new URL(hoURL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");

		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		String output = "", op = "";
		while ((output = br.readLine()) != null)
		{
		    op += output;
		}
		System.out.println("HO Conn=" + op);
		conn.disconnect();

		flgHOStatus = Boolean.parseBoolean(op);
		if (flgHOStatus)
		{
		    gConnectionActive = "Y";
		}
	    }
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

// Function to post data to ho from transactions ie on bill punch or day end    
    public static void funInvokeHOWebserviceForTrans(String transType, String formName) throws Exception
    {
	if (gHOPOSType.equals("Stand Alone") || gHOPOSType.equals("HOPOS"))
	{
	    return;
	}

	if (clsPosConfigFile.gHOCommunication.equals("true"))
	{
	    if (transType.equalsIgnoreCase("Sales"))
	    {
		clsSynchronizePOSDataToHO objSynchData = new clsSynchronizePOSDataToHO();
		//objSynchData.funPostSaleDataToHO(formName);
		objSynchData.funPostSalesDataToHOInBulk(formName);
	    }
	    else if (transType.equalsIgnoreCase("PlaceOrder"))
	    {
		clsSynchronizePOSDataToHO objSynchData = new clsSynchronizePOSDataToHO();
		objSynchData.funPostPlaceOrderDataToHO(formName);
	    }
	    else if (transType.equalsIgnoreCase("Audit"))
	    {
		clsSynchronizePOSDataToHO objSynchData = new clsSynchronizePOSDataToHO();
		objSynchData.funPostAuditDataToHO(formName);
	    }
	    else if (transType.equalsIgnoreCase("Inventory"))
	    {
		clsSynchronizePOSDataToHO objSynchData = new clsSynchronizePOSDataToHO();
		objSynchData.funPostInventoryDataToHO(formName);
	    }
	    else if (transType.equalsIgnoreCase("AdvanceOrder"))
	    {
		clsSynchronizePOSDataToHO objSynchData = new clsSynchronizePOSDataToHO();
		objSynchData.funPostAdvOrderDataToHO(formName);
	    }
	    else if (transType.equalsIgnoreCase("Credit Bill Receipts"))
	    {
		clsSynchronizePOSDataToHO objSynchData = new clsSynchronizePOSDataToHO();
		objSynchData.funPostCreditBillReceiptDtlData(formName);
	    }
	    else if (transType.equalsIgnoreCase("All"))
	    {
		clsSynchronizePOSDataToHO objSynchData = new clsSynchronizePOSDataToHO();
		objSynchData.funPostAuditDataToHO(formName);
		objSynchData.funPostSaleDataToHO(formName);
		objSynchData.funPostInventoryDataToHO(formName);
		clsGlobalVarClass.funPostCustomerDataToHOPOS();
		clsGlobalVarClass.funPostCustomerAreaDataToHOPOS();
	    }
	}
    }

// Function to post data to ho manually    
    public static void funPostDataToHOManually(String tableName, String fromDate, String toDate, String tableType)
    {
	if (gHOPOSType.equals("Stand Alone") || gHOPOSType.equals("HOPOS"))
	{
	    return;
	}

	if (clsPosConfigFile.gHOCommunication.equals("true"))
	{
	    clsSynchronizePOSDataToHO objSynchData = new clsSynchronizePOSDataToHO();
	    objSynchData.funPostSalesDataToHOManually(tableName, fromDate, toDate, tableType);
	}
    }

    public static boolean funPostItemSalesData(String posCode, String fromDate, String toDate)
    {
	String WSStockAdjustmentCode = "";
	clsSynchronizePOSDataToHO objSynchSalesData = new clsSynchronizePOSDataToHO();
	boolean isPosted = objSynchSalesData.funPostPOSItemSalesDataAuto(gItemType, posCode, fromDate, toDate);
	return isPosted;
    }

    public static void funPostDayEndData(String newStartDate, int shiftCode)
    {
	if (gHOPOSType.equals("Stand Alone") || gHOPOSType.equals("HOPOS"))
	{
	    return;
	}

	try
	{
	    if (clsPosConfigFile.gHOCommunication.equals("true"))
	    {
		clsSynchronizePOSDataToHO objSynchData = new clsSynchronizePOSDataToHO();
		if (objSynchData.funPostDayEndData(newStartDate, shiftCode))
		{
		    dbMysql.execute("update tbldayendprocess set strDataPostFlag='Y' where strDayEnd='Y'");
		}
		objSynchData.funPostCashManagementData();
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public static void funPostCustomerDataToHOPOS()
    {
	if (gHOPOSType.equals("Stand Alone") || gHOPOSType.equals("HOPOS"))
	{
	    return;
	}

	try
	{
	    if (clsPosConfigFile.gHOCommunication.equals("true"))
	    {
		clsSynchronizePOSDataToHO objSynchData = new clsSynchronizePOSDataToHO();
		if (objSynchData.funPostCustomerMasterDataToHO())
		{
		    dbMysql.execute("update tblcustomermaster set strDataPostFlag='Y'");
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	}
    }

    public static void funPostCustomerAreaDataToHOPOS()
    {
	if (gHOPOSType.equals("Stand Alone") || gHOPOSType.equals("HOPOS"))
	{
	    return;
	}

	try
	{
	    if (clsPosConfigFile.gHOCommunication.equals("true"))
	    {
		clsSynchronizePOSDataToHO objSynchData = new clsSynchronizePOSDataToHO();
		boolean flgCustAreaMaster = objSynchData.funPostCustomerAreaMaster();
		if (flgCustAreaMaster)
		{
		    dbMysql.execute("update tblbuildingmaster set strDataPostFlag='Y'");
		    System.out.println("cust area master flg=" + flgCustAreaMaster);
		}
		boolean flgDelCharges = objSynchData.funPostDelChargesMaster();
		if (flgDelCharges)
		{
		    dbMysql.execute("update tblareawisedc set strDataPostFlag='Y'");
		    System.out.println("cust area Del charges flg=" + flgDelCharges);
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public static void funFetchMasterDataFromHO()
    {
	try
	{
	    if (!gHOPOSType.equals("Client POS"))
	    {
		return;
	    }

	    if (clsPosConfigFile.gHOCommunication.equals("true"))
	    {
		String sqlLastUpdatedDateTime = "select a.dteHOServerDate from tblsetup a ";
		ResultSet rs = dbMysql.executeResultSet(sqlLastUpdatedDateTime);
		if (rs.next())
		{
		    gLastModifiedDate = rs.getString(1);
		}
		rs.close();

		clsSynchronizePOSDataToHO objSynch = new clsSynchronizePOSDataToHO();
		//objSynch.funFetchMasterDataFromHO();
		objSynch.funFetchUpdatedMasterDataFromHO();
		System.out.println("Data Fetching Complted!!!");
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public static void proCalculateStock(String fromDate, String toDate, String psCode)
    {
	try
	{

	    //STOCK IN UPDATE
	    //deleting from Temp Item Stk
	    String sql = "delete from tbltempitemstk";
	    dbMysql.execute(sql);

	    //Inserting into Temp Item Stk from Stk IN table
	    sql = "insert into tbltempitemstk select b.strItemCode,sum(b.dblQuantity) "
		    + "from tblstkindtl b,tblstkinhd a where a.strStkInCode=b.strStkInCode ";
	    sql = sql + "and date(a.dteStkInDate) between '" + fromDate + "' and '" + toDate + "' ";
	    if (!psCode.equals("All"))
	    {
		sql = sql + "and a.strPOSCode='" + psCode + "' ";
	    }
	    sql = sql + "group by b.strItemCode";
	    System.out.println(sql);
	    dbMysql.execute(sql);

	    //Join Update from Temp Table into Stock Table
	    sql = "update tblitemcurrentstk a Set a.intIn =a.intIn + IFNULL((select b.dblQuantity ";
	    sql = sql + "from tbltempitemstk b ";
	    sql = sql + "where a.strItemCode = b.strItemCode and b.dblQuantity),0)";
	    //System.out.println(sql);
	    dbMysql.execute(sql);

	    //STOCK OUT UPDATE 
	    //deleting from Temp Item Stk
	    sql = "delete from tbltempitemstk";
	    dbMysql.execute(sql);

	    //Inserting into Temp Item Stk from Stk OUT table
	    sql = "insert into tbltempitemstk select b.strItemCode,sum(b.dblQuantity) "
		    + "from tblstkoutdtl b,tblstkouthd a "
		    + "where a.strStkOutCode=b.strStkOutCode ";
	    sql = sql + " and date(a.dteStkOutDate) between '" + fromDate + "' and '" + toDate + "' ";
	    if (!psCode.equals("All"))
	    {
		sql = sql + " and a.strPOSCode='" + psCode + "'";
	    }
	    sql = sql + " group by b.strItemCode";
	    dbMysql.execute(sql);

	    //Join Update from Temp Table into Stock Table
	    sql = "update tblitemcurrentstk a Set a.intOut =a.intOut + IFNULL((select b.dblQuantity ";
	    sql = sql + "from tbltempitemstk b ";
	    sql = sql + "where a.strItemCode = b.strItemCode),0)";
	    dbMysql.execute(sql);

	    //SALE UPDATE 
	    //deleting from Temp Item Stk
	    sql = "delete from tbltempitemstk";
	    dbMysql.execute(sql);

	    //Inserting into Temp Item Stk from Sale table
	    sql = "insert into tbltempitemstk select c.strChildItemCode,sum((a.dblQuantity * c.dblQuantity )) "
		    + "from tblbilldtl a,tblrecipehd b, tblrecipedtl c,tblbillhd d "
		    + "where a.strItemCode=b.strItemCode and b.strRecipeCode=c.strRecipeCode "
		    + "and a.strBillNo=d.strBillNo ";
	    sql = sql + "and date(d.dteBillDate) between '" + fromDate + "' and '" + toDate + "' ";
	    if (!psCode.equals("All"))
	    {
		sql = sql + " and d.strPOSCode='" + psCode + "' ";
	    }
	    sql += "group by c.strChildItemCode";
	    System.out.println(sql);
	    dbMysql.execute(sql);

	    sql = "insert into tbltempitemstk select c.strChildItemCode,sum((a.dblQuantity * c.dblQuantity )) "
		    + "from tblqbilldtl a,tblrecipehd b, tblrecipedtl c,tblqbillhd d  "
		    + "where a.strItemCode=b.strItemCode and b.strRecipeCode=c.strRecipeCode "
		    + "and a.strBillNo=d.strBillNo ";
	    sql = sql + "and date(d.dteBillDate) between '" + fromDate + "' and '" + toDate + "' ";
	    if (!psCode.equals("All"))
	    {
		sql = sql + " and d.strPOSCode='" + psCode + "' ";
	    }
	    sql += "group by c.strChildItemCode";
	    System.out.println(sql);
	    dbMysql.execute(sql);

	    sql = "insert into tbltempitemstk select a.strItemCode,sum(a.dblQuantity) from tblbilldtl a,tblbillhd b "
		    + "where a.strBillNo=b.strBillNo "
		    + "and a.strItemCode NOT IN (select strItemCode from tblrecipehd) ";
	    sql += "and date(b.dteBillDate) between '" + fromDate + "' and '" + toDate + "' ";
	    if (!psCode.equals("All"))
	    {
		sql = sql + " and b.strPOSCode='" + psCode + "' ";
	    }
	    sql += "group by a.strItemCode";
	    System.out.println(sql);
	    dbMysql.execute(sql);

	    sql = "insert into tbltempitemstk select a.strItemCode,sum(a.dblQuantity) from tblqbilldtl a,tblqbillhd b "
		    + "where a.strBillNo=b.strBillNo "
		    + "and a.strItemCode NOT IN (select strItemCode from tblrecipehd) ";
	    sql += "and date(b.dteBillDate) between '" + fromDate + "' and '" + toDate + "' ";
	    if (!psCode.equals("All"))
	    {
		sql = sql + " and b.strPOSCode='" + psCode + "' ";
	    }
	    sql += "group by a.strItemCode";
	    System.out.println(sql);
	    dbMysql.execute(sql);

	    //Join Update from Temp Table into Stock Table
	    sql = "update tblitemcurrentstk a Set a.intSale =a.intSale + IFNULL((select sum(b.dblQuantity) ";
	    sql = sql + "from tbltempitemstk b ";
	    sql = sql + "where a.strItemCode = b.strItemCode group by b.strItemCode),0)";
	    System.out.println(sql);
	    dbMysql.execute(sql);

	    //Update Balance
	    sql = "update tblitemcurrentstk Set intBalance = intOpening + intIn - intOut - intSale";
	    dbMysql.execute(sql);

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public static int funCalculateStock(Date dt1, Date dt2, String posCode, String itemType, String reportType)
    {
	try
	{
	    String fromDate, toDate;
	    Date installDate;
	    String sql1 = "";
	    if ((dt2.getTime() - dt1.getTime()) < 0)
	    {
		JOptionPane.showMessageDialog(null, "Invalid Date");
	    }
	    else
	    {
		int d = dt1.getDate();
		int m = dt1.getMonth() + 1;
		int y = dt1.getYear() + 1900;
		fromDate = y + "-" + m + "-" + d;
		d = dt2.getDate();
		m = dt2.getMonth() + 1;
		y = dt2.getYear() + 1900;
		toDate = y + "-" + m + "-" + d;
		String psCode = posCode;
		StringBuilder sb = new StringBuilder(psCode);
		int ind = sb.lastIndexOf(" ");
		psCode = sb.substring(ind + 1, psCode.length());
		installDate = clsGlobalVarClass.gStartDate;
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(dt1);
		cal.add(Calendar.DATE, -1);
		String newFromDate = (cal.getTime().getYear() + 1900) + "-" + (cal.getTime().getMonth() + 1) + "-" + (cal.getTime().getDate());
		d = installDate.getDate();
		m = installDate.getMonth() + 1;
		y = installDate.getYear() + 1900;
		String startDate = y + "-" + m + "-" + d;

		//Deleting from Temp Stk table
		sql1 = "delete from tblitemcurrentstk";
		clsGlobalVarClass.dbMysql.execute(sql1);

		//Inserting into Temp table from Item Master
		sql1 = "insert into tblitemcurrentstk "
			+ "(strGroupName,strSubgroupName,strItemCode,strItemName,dblPurchaseRate) "
			+ "select c.strGroupName,b.strSubGroupName,a.strItemCode,a.strItemName,a.dblPurchaseRate "
			+ "from tblitemmaster a,tblsubgrouphd b,tblgrouphd c "
			+ "where a.strSubGroupCode=b.strSubGroupCode "
			+ "and b.strGroupCode=c.strGroupCode ";
		if (itemType.equals("Raw Material"))
		{
		    sql1 += "and strRawMaterial='Y' ";
		}
		else if (itemType.equals("Menu Item"))
		{
		    sql1 += "and strItemForSale='Y' ";
		}
		System.out.println(sql1);
		clsGlobalVarClass.dbMysql.execute(sql1);
		//System.out.println("Start Date="+startDate);
		//System.out.println("From Date="+fromDate);
		//System.out.println("New From Date="+newFromDate);
		if (!startDate.equals(fromDate))
		{
		    clsGlobalVarClass.proCalculateStock(startDate, newFromDate, psCode);
		    sql1 = "Update tblitemcurrentstk Set intOpening = intBalance , intIn = 0, intOut = 0, intSale = 0 ";
		    clsGlobalVarClass.dbMysql.execute(sql1);
		    //System.out.println(sql1);
		}
		clsGlobalVarClass.proCalculateStock(fromDate, toDate, psCode);
		if (reportType.equalsIgnoreCase("Stock"))
		{
		    //clsGlobalVarClass.dbMysql.execute("delete from tblitemcurrentstk where intOpening=0 and intOut=0 and intIn=0 and  intSale=0 and intBalance=0");
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return 0;
    }

    /**
     * To check duplicate item name
     *
     * @param from
     * @param colmnname
     * @param colCodeName
     * @param text
     * @param code
     * @param opType
     * @return
     */
    public static boolean funCheckItemName(String tableName, String colmnname, String colCodeName, String text, String code, String opType, String posCode)
    {
	boolean flgItemDuplicate = false;
	try
	{
	    String sql = "select " + colmnname + " from " + tableName + "  where " + colmnname + "='" + text + "'";
	    if ("update".equalsIgnoreCase(opType))
	    {
		sql += " and " + colCodeName + "!='" + code + "'";
	    }
	    if (tableName.equals("tbltablemaster") || tableName.equals("tblordermaster"))
	    {
		sql += " and strPOSCode='" + posCode + "' ";
	    }
	    System.out.println(sql);
	    ResultSet rsItemName = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsItemName.next())
	    {
		flgItemDuplicate = true;
	    }
	    rsItemName.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    return flgItemDuplicate;
	}
    }

    public static Date funGetCalenderToDate(int years)
    {
	Date toCalenderDate = null;
	try
	{
	    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
	    Date dt = new Date();
	    int toYear = dt.getYear() + 1900 + years;
	    String toDate = dt.getDate() + "-" + (dt.getMonth() + 1) + "-" + toYear;
	    toCalenderDate = format.parse(toDate);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return toCalenderDate;
    }

    public static void tickTock(JLabel lblclock)
    {
	lblclock.setText(DateFormat.getDateTimeInstance().format(new Date()));
    }

    public static void tickTockPosDate(JLabel lblclock)
    {
	Date date1 = new Date();
	String new_str = String.format("%tr", date1);
	String dateAndTime = clsGlobalVarClass.gPOSDateToDisplay + " " + new_str;
	lblclock.setText(dateAndTime);
    }

    public static <K extends Comparable, V extends Comparable> Map<K, V> funSortMapOnValues(Map<K, V> map)
    {
	List<Map.Entry<K, V>> entries = new LinkedList<Map.Entry<K, V>>(map.entrySet());

	Collections.sort(entries, new Comparator<Map.Entry<K, V>>()
	{
	    @Override
	    public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2)
	    {
		return o1.getValue().compareTo(o2.getValue());
	    }
	});

	//LinkedHashMap will keep the keys in the order they are inserted
	//which is currently sorted on natural ordering
	Map<K, V> sortedMap = new LinkedHashMap<K, V>();

	for (Map.Entry<K, V> entry : entries)
	{
	    sortedMap.put(entry.getKey(), entry.getValue());
	}
	return sortedMap;
    }

    public static String funBackupDatabase() throws Exception
    {
	clsBackupDatabase objDBBackup = new clsBackupDatabase();
	return objDBBackup.funTakeBackUpDB();
    }

    public static String funPostItemSalesDataExcise(String posCode, String fromDate, String toDate)
    {
	String ExciseBillGen = "";
	clsSynchronizePOSDataToHO objSynchSalesData = new clsSynchronizePOSDataToHO();
	ExciseBillGen = objSynchSalesData.funPostPOSSalesDataToExciseAuto(gItemType, posCode, fromDate, toDate);
	return ExciseBillGen;
    }

    public static String funPostPOSItemSalesData(String posCode, String fromDate, String toDate, String dayEndType)
    {
	String WSStockAdjustmentCode = "";
	clsSynchronizePOSDataToHO objSynchSalesData = new clsSynchronizePOSDataToHO();
	WSStockAdjustmentCode = objSynchSalesData.funPostPOSItemSalesDataInPOS(gItemType, posCode, fromDate, toDate, dayEndType);
	return WSStockAdjustmentCode;
    }

    private void funSetGlobalParameters(String posCode) throws Exception
    {
	flgReprintView = false;
	sql = "select * from tblsetup where (strPOSCode='" + posCode + "'  OR strPOSCode='All') ";
	ResultSet rs = dbMysql.executeResultSet(sql);
	if (rs.next())
	{
	    gClientCode = rs.getString(1);
	    gClientName = rs.getString(2);
	    gClientAddress1 = rs.getString(3);
	    gClientAddress2 = rs.getString(4);
	    gClientAddress3 = rs.getString(5);
	    gClientEmail = rs.getString(6);
	    gBillFooter = rs.getString(7);
	    gBillPaperSize = rs.getString(9);

	    gNegBilling = false;
	    if (rs.getString(10).equals("Y"))
	    {
		gNegBilling = true;
	    }
	    gDayEnd = "N";
	    gCityName = rs.getString(14);
	    gStateName = rs.getString(15);
	    gCountryName = rs.getString(16);
	    gClientTelNo = rs.getString(17);
	    gMobileNoForSMS = rs.getString(17);
	    gStartDate = rs.getDate(18);
	    gEndDate = rs.getDate(19);
	    gEndTime = rs.getTime(19);
	    gNatureOfBusinnes = rs.getString(20);
	    gMultiBillPrint = false;
	    if (rs.getString(21).equals("Y"))
	    {
		gMultiBillPrint = true;
	    }
	    gEnableKOT = false;
	    if (rs.getString(22).equals("Y"))
	    {
		gEnableKOT = true;
	    }
	    gEffectOnPSP = false;
	    if (rs.getString(23).equals("Y"))
	    {
		gEffectOnPSP = true;
	    }
	    gPrintVatNo = false;
	    if (rs.getString(24).equals("Y"))
	    {
		gPrintVatNo = true;
	    }
	    gVatNo = rs.getString(25);
	    gShowBill = false;
	    if (rs.getString(26).equals("Y"))
	    {
		gShowBill = true;
	    }
	    gPrintServiceTaxNo = false;
	    if (rs.getString(27).equals("Y"))
	    {
		gPrintServiceTaxNo = true;
	    }
	    gServiceTaxNo = rs.getString(28);
	    gManualBillNo = rs.getString(29);
	    gMenuItemSequence = rs.getString(30);
	    gSenderEmailId = rs.getString(31);
	    gSenderMailPassword = rs.getString(32);
	    gEmailMessage = rs.getString(34);
	    gEmailServerName = rs.getString(35);
	    gSMSApi = rs.getString(36);
	    gHOPOSType = rs.getString(41);
	    gSanguineWebServiceURL = rs.getString(42);
	    gDataSendFrequency = rs.getString(43);
	    gLastModifiedDate = rs.getString(44);
	    gRFIDInterface = rs.getString(45);
	    gRFIDDBServerName = rs.getString(46);
	    gRFIDDBUserName = rs.getString(47);
	    gRFIDDBPassword = rs.getString(48);
	    gRFIDDBName = rs.getString(49);

	    gKOTPrintingEnableForDirectBiller = false;
	    if (rs.getString(50).equals("Y"))
	    {
		gKOTPrintingEnableForDirectBiller = true;
	    }

	    gTheme = rs.getString(52);
	    gMaxDiscount = Double.parseDouble(rs.getString(53));
	    gAreaWisePricing = rs.getString(54);
	    gMenuItemSortingOn = rs.getString(55);
	    gDineInAreaForDirectBiller = rs.getString(56);
	    gColumnSize = rs.getInt(57);
	    gPrintType = rs.getString(58);

	    gEditHDCharges = false;
	    if (rs.getString(59).equals("Y"))
	    {
		gEditHDCharges = true;
	    }
	    gSlabBasedHDCharges = false;
	    if (rs.getString(60).equals("Y"))
	    {
		gSlabBasedHDCharges = true;
	    }
	    gSkipWaiter = false;
	    if (rs.getString(62).equals("Y"))
	    {
		gSkipWaiter = true;
	    }
	    String gDirectKOTPrintingFromMakeKOT = rs.getString(63);

	    gSkipPax = false;
	    if (rs.getString(64).equals("Y"))
	    {
		gSkipPax = true;
	    }
	    gCRMInterface = rs.getString(65);
	    gGetWebserviceURL = rs.getString(66);
	    gPostWebserviceURL = rs.getString(67);
	    gOutletUID = rs.getString(68);
	    gPOSID = rs.getString(69);
	    gStockInOption = rs.getString(70);
	    gAdvRecPrintCount = rs.getString(72);
	    gHomeDeliverySMS = rs.getString(73);
	    gBillSettlementSMS = rs.getString(74);
	    gBillFormatType = rs.getString(75);
	    gActivePromotions = false;
	    if (rs.getString(76).equals("Y"))
	    {
		gActivePromotions = true;
	    }
	    gHomeDelSMSYN = false;
	    if (rs.getString(77).equals("Y"))
	    {
		gHomeDelSMSYN = true;
	    }
	    gBillSettleSMSYN = false;
	    if (rs.getString(78).equals("Y"))
	    {
		gBillSettleSMSYN = true;
	    }
	    gSMSType = rs.getString(79);
	    gPrintShortNameOnKOT = false;
	    if (rs.getString(80).equals("Y"))
	    {
		gPrintShortNameOnKOT = true;
	    }
	    gCustHelpOnTrans = false;
	    if (rs.getString(81).equals("Y"))
	    {
		gCustHelpOnTrans = true;
	    }
	    gPrintOnVoidBill = false;
	    if (rs.getString(82).equals("Y"))
	    {
		gPrintOnVoidBill = true;
	    }
	    gPostSalesDataToMMS = false;
	    if (rs.getString(83).equals("Y"))
	    {
		gPostSalesDataToMMS = true;
	    }
	    gCustAreaCompulsory = false;
	    if (rs.getString(84).equals("Y"))
	    {
		gCustAreaCompulsory = true;
	    }
	    gPriceFrom = rs.getString(85);
	    gShowPrinterErrorMsg = false;
	    if (rs.getString(86).equals("Y"))
	    {
		gShowPrinterErrorMsg = true;
	    }
	    gCMSIntegrationYN = false;
	    if (rs.getString(89).equals("Y"))
	    {
		gCMSIntegrationYN = true;
	    }
	    gCardIntfType = rs.getString(88);
	    gCMSWebServiceURL = rs.getString(90);
	    gChangeQtyForExternalCode = false;
	    if (rs.getString(91).equals("Y"))
	    {
		gChangeQtyForExternalCode = true;
	    }
	    gPointsOnBillPrint = false;
	    if (rs.getString(92).equals("Y"))
	    {
		gPointsOnBillPrint = true;
	    }
	    gCMSPOSCode = rs.getString(93);
	    gCompulsoryManualAdvOrderNo = false;
	    if (rs.getString(94).equals("Y"))
	    {
		gCompulsoryManualAdvOrderNo = true;
	    }
	    gPrintManualAdvOrderNoOnBill = false;
	    if (rs.getString(95).equals("Y"))
	    {
		gPrintManualAdvOrderNoOnBill = true;
	    }
	    gPrintModQtyOnKOT = false;
	    if (rs.getString(96).equals("Y"))
	    {
		gPrintModQtyOnKOT = true;
	    }
	    gNoOfLinesInKOTPrint = rs.getString(97);
	    gMultipleKOTPrint = false;
	    if (rs.getString(98).equals("Y"))
	    {
		gMultipleKOTPrint = true;
	    }
	    gItemQtyNumpad = false;
	    if (rs.getString(99).equals("Y"))
	    {
		gItemQtyNumpad = true;
	    }
	    gTreatMemberAsTable = false;
	    if (rs.getString(100).equals("Y"))
	    {
		gTreatMemberAsTable = true;
	    }

	    gPrintKotToLocaPrinter = false;
	    if (rs.getString(101).equals("Y"))
	    {
		gPrintKotToLocaPrinter = true;
	    }

	    gEnableSettleBtnForDirectBiller = false;
	    if (rs.getString(103).equals("Y"))
	    {
		gEnableSettleBtnForDirectBiller = true;
	    }

	    gDelBoyCompulsoryOnDirectBiller = false;
	    if (rs.getString(104).equals("Y"))
	    {
		gDelBoyCompulsoryOnDirectBiller = true;
	    }
	    gCMSMemberCodeForKOTJPOS = rs.getString(105);
	    gCMSMemberCodeForKOTMPOS = rs.getString(106);

	    gDontShowAdvOrderInOtherPOS = false;
	    if (rs.getString(107).equals("Y"))
	    {
		gDontShowAdvOrderInOtherPOS = true;
	    }

	    gPrintZeroAmtModifierOnBill = false;
	    if (rs.getString(108).equals("Y"))
	    {
		gPrintZeroAmtModifierOnBill = true;
	    }

	    gPrintKOTYN = false;
	    if (rs.getString(109).equals("Y"))
	    {
		gPrintKOTYN = true;
	    }

	    gCreditCardSlipNo = false;
	    if (rs.getString(110).equals("Y"))
	    {
		gCreditCardSlipNo = true;
	    }

	    gCreditCardExpiryDate = false;
	    if (rs.getString(111).equals("Y"))
	    {
		gCreditCardExpiryDate = true;
	    }

	    gSelectWaiterFromCardSwipe = false;
	    if (rs.getString(112).equals("Y"))
	    {
		gSelectWaiterFromCardSwipe = true;
	    }

	    gMultiWaiterSelOnMakeKOT = false;
	    if (rs.getString(113).equals("Y"))
	    {
		gMultiWaiterSelOnMakeKOT = true;
	    }

	    gMoveTableToOtherPOS = false;
	    if (rs.getString(114).equals("Y"))
	    {
		gMoveTableToOtherPOS = true;
	    }

	    gMoveKOTToOtherPOS = false;
	    if (rs.getString(115).equals("Y"))
	    {
		gMoveKOTToOtherPOS = true;
	    }

	    gCalculateTaxOnMakeKOT = false;
	    if (rs.getString(116).equals("Y"))
	    {
		gCalculateTaxOnMakeKOT = true;
	    }
	    gReceiverEmailIds = rs.getString(117);

	    gItemWiseDiscount = false;
	    if (rs.getString(118).equals("Y"))
	    {
		gItemWiseDiscount = true;
	    }

	    gRemarksOnTakeAway = false;
	    if (rs.getString(119).equals("Y"))
	    {
		gRemarksOnTakeAway = true;
	    }

	    gShowItemStkColumnInDB = false;
	    if (rs.getString(120).equals("Y"))
	    {
		gShowItemStkColumnInDB = true;
	    }
	    gItemType = rs.getString(121);

	    gAllowNewAreaMasterFromCustMaster = false;
	    if (rs.getString(122).equals("Y"))
	    {
		gAllowNewAreaMasterFromCustMaster = true;
	    }

	    gCustAddressSelectionForBill = false;
	    if (rs.getString(123).equals("Y"))
	    {
		gCustAddressSelectionForBill = true;
	    }

	    gGenrateMI = false;
	    if (rs.getString(124).equals("Y"))
	    {
		gGenrateMI = true;
	    }
	    gFTPAddress = rs.getString(125);
	    gFTPServerUserName = rs.getString(126);
	    gFTPServerPass = rs.getString(127);
	    gAllowToCalculateItemWeight = rs.getString(128);
	    gShowBillsType = rs.getString(129);
	    gPrintTaxInvoice = rs.getString(130);
	    gPrintInclusiveOfAllTaxes = rs.getString(131);
	    gApplyDiscountOn = rs.getString(132);
	    gMemberCodeForKotInMposByCardSwipe = rs.getString(133);

	    gPrintBillYN = false;
	    if (rs.getString(134).equals("Y"))
	    {
		gPrintBillYN = true;
	    }

	    gUseVatAndServiceTaxFromPos = false;
	    if (rs.getString(135).equals("Y"))
	    {
		gUseVatAndServiceTaxFromPos = true;
	    }

	    gMemberCodeForMakeBillInMPOS = false;
	    if (rs.getString(136).equals("Y"))
	    {
		gMemberCodeForMakeBillInMPOS = true;
	    }

	    gItemWiseKOTPrintYN = false;
	    if (rs.getString(137).equals("Y"))
	    {
		gItemWiseKOTPrintYN = true;
	    }
	    gLastPOSForDayEnd = rs.getString(138);
	    gCMSPostingType = rs.getString(139);

	    gPopUpToApplyPromotionsOnBill = false;
	    if (rs.getString(140).equals("Y"))
	    {
		gPopUpToApplyPromotionsOnBill = true;
	    }

	    gSelectCustomerCodeFromCardSwipe = false;
	    if (rs.getString(141).equals("Y"))
	    {
		gSelectCustomerCodeFromCardSwipe = true;
	    }

	    gCheckDebitCardBalanceOnTrans = false;
	    if (rs.getString(142).equals("Y"))
	    {
		gCheckDebitCardBalanceOnTrans = true;
	    }

	    gPickSettlementsFromPOSMaster = false;
	    if (rs.getString(143).equals("Y"))
	    {
		gPickSettlementsFromPOSMaster = true;
	    }

	    gEnableShiftYN = false;
	    if (rs.getString(144).equals("Y"))
	    {
		gEnableShiftYN = true;
	    }

	    gProductionLinkup = false;
	    if (rs.getString(145).equals("Y"))
	    {
		gProductionLinkup = true;
	    }

	    gLockDataOnShiftYN = false;
	    if (rs.getString(146).equals("Y"))
	    {
		gLockDataOnShiftYN = true;
	    }
	    gWSClientCode = rs.getString(147);

	    gEnableBillSeries = false;
	    if (rs.getString(149).equals("Y"))
	    {
		gEnableBillSeries = true;
	    }

	    gEnablePMSIntegrationYN = false;
	    if (rs.getString(150).equals("Y"))
	    {
		gEnablePMSIntegrationYN = true;
	    }

	    gPrintTimeOnBillYN = false;
	    if (rs.getString(151).equals("Y"))
	    {
		gPrintTimeOnBillYN = true;
	    }

	    gPrintTDHItemsInBill = false;
	    if (rs.getString(152).equals("Y"))
	    {
		gPrintTDHItemsInBill = true;
	    }

	    gPrintRemarkAndReasonForReprint = false;
	    if (rs.getString(153).equals("Y"))
	    {
		gPrintRemarkAndReasonForReprint = true;
	    }
	    int daysBeforeVoidAdvOrder = rs.getInt(154);
	    gNoOfDelDaysForAdvOrder = rs.getInt(155);
	    gNoOfDelDaysForUrgentOrder = rs.getInt(156);

	    gSetUpToTimeForAdvOrder = false;
	    if (rs.getString(157).equals("Y"))
	    {
		gSetUpToTimeForAdvOrder = true;
	    }

	    gSetUpToTimeForUrgentOrder = false;
	    if (rs.getString(158).equals("Y"))
	    {
		gSetUpToTimeForUrgentOrder = true;
	    }
	    gUpToTimeForAdvOrder = rs.getString(159);
	    gUpToTimeForUrgentOrder = rs.getString(160);

	    gEnablePrintAndSettleBtnForDB = false;
	    if (rs.getString(161).equals("Y"))
	    {
		gEnablePrintAndSettleBtnForDB = true;
	    }

	    gInrestoPOSIntegrationYN = false;
	    if (rs.getString(162).equals("Y"))
	    {
		gInrestoPOSIntegrationYN = true;
	    }
	    gInrestoPOSWebServiceURL = rs.getString(163);
	    gInrestoPOSId = rs.getString(164);
	    gInrestoPOSKey = rs.getString(165);

	    flgCarryForwardFloatAmtToNextDay = false;
	    if (rs.getString(166).equals("Y"))
	    {
		flgCarryForwardFloatAmtToNextDay = true;
	    }

	    if (rs.getString(167).equalsIgnoreCase("Y"))
	    {
		gOpenCashDrawerAfterBillPrintYN = true;
	    }
	    else
	    {
		gOpenCashDrawerAfterBillPrintYN = false;
	    }

	    if (rs.getString(168).equalsIgnoreCase("Y"))
	    {
		gPropertyWiseSalesOrderYN = true;
	    }
	    else
	    {
		gPropertyWiseSalesOrderYN = false;
	    }

	    if (rs.getString(170).equalsIgnoreCase("Y"))
	    {
		gShowItemDetailsGrid = true;
	    }
	    else
	    {
		gShowItemDetailsGrid = false;
	    }

	    if (rs.getString(171).equalsIgnoreCase("Y"))
	    {
		gShowPopUpForNextItemQuantity = true;
	    }
	    else
	    {
		gShowPopUpForNextItemQuantity = false;
	    }
	    //============================= 6th feb 2017 = ========================================//

	    gJioMoneyIntegrationYN = false;
	    if (rs.getString(172).equals("Y"))
	    {
		gJioMoneyIntegrationYN = true;
	    }
	    gJioMoneyWebServiceURL = rs.getString(173);
	    gJioMoneyMID = rs.getString(174);
	    gJioMoneyTID = rs.getString(175);
	    gJioMoneyActivationCode = rs.getString(176);
	    //============================= 6th feb 2017 =========================================//

	    gNewBillSeriesForNewDay = false;
	    if (rs.getString(178).equalsIgnoreCase("Y"))
	    {
		gNewBillSeriesForNewDay = true;
	    }

	    gShowOnlyLoginPOSReports = false;
	    if (rs.getString(179).equalsIgnoreCase("Y"))
	    {
		gShowOnlyLoginPOSReports = true;
	    }
	    gEnableDineIn = false;
	    if (rs.getString(180).equalsIgnoreCase("Y"))
	    {
		gEnableDineIn = true;
	    }
	    gAutoAreaSelectionInMakeKOT = false;
	    if (rs.getString(181).equalsIgnoreCase("Y"))
	    {
		gAutoAreaSelectionInMakeKOT = true;
	    }

	    gConsolidatedKOTPrinterPort = rs.getString(182);
	    gRoundOffTo = rs.getDouble(183);
	    gShowUnSettletmentForm = false;
	    if (rs.getString(184).equalsIgnoreCase("Y"))
	    {
		gShowUnSettletmentForm = true;
	    }
	    gPrintOpenItemsOnBill = false;
	    if (rs.getString(185).equalsIgnoreCase("Y"))
	    {
		gPrintOpenItemsOnBill = true;
	    }

	    gPrintHomeDeliveryYN = false;
	    if (rs.getString(186).equalsIgnoreCase("Y"))
	    {
		gPrintHomeDeliveryYN = true;
	    }

	    gAreaWisePromotions = false;
	    if (rs.getString(188).equalsIgnoreCase("Y"))
	    {
		gAreaWisePromotions = true;
	    }

	    gPrintItemsOnMoveKOTMoveTable = false;
	    if (rs.getString(189).equalsIgnoreCase("Y"))
	    {
		gPrintItemsOnMoveKOTMoveTable = true;
	    }

	    gShowPurRateInDirectBiller = false;
	    if (rs.getString(190).equalsIgnoreCase("Y"))
	    {
		gShowPurRateInDirectBiller = true;
	    }

	    //191 for APOS
	    gAutoShowPopItems = false;
	    if (rs.getString(192).equalsIgnoreCase("Y"))
	    {
		gAutoShowPopItems = true;
	    }

	    //rs.getString(193) is for no. of days of popular items
	    gMMSSalesDataPostEffectCostOrLoc = rs.getString(194);
	    gEffectOfSales = rs.getString(195);
	    gPOSWiseItemToMMSProductLinkUpYN = false;
	    if (rs.getString(196).equalsIgnoreCase("Y"))
	    {
		gPOSWiseItemToMMSProductLinkUpYN = true;
	    }

	    gEnableMasterDiscount = false;
	    if (rs.getString(197).equalsIgnoreCase("Y"))
	    {
		gEnableMasterDiscount = true;
	    }

	    gEnableNFCInterface = false;
	    if (rs.getString(198).equalsIgnoreCase("Y"))
	    {
		gEnableNFCInterface = true;
	    }

	    gBenowIntegrationYN = false;
	    if (rs.getString(199).equals("Y"))
	    {
		gBenowIntegrationYN = true;
	    }
	    gBenowXEmail = rs.getString(200);
	    gBenowMerchantCode = rs.getString(201);
	    gBenowAuthenticationKey = rs.getString(202);
	    gBenowSalt = rs.getString(203);

	    gEnableLockTables = false;
	    if (rs.getString(204).equals("Y"))
	    {
		gEnableLockTables = true;
	    }

	    //gDineInAreaForDirectBiller = rs.getString(56);
	    gHomeDeliveryAreaForDirectBiller = rs.getString(205);
	    gTakeAwayAreaForDirectBiller = rs.getString(206);
	    if (rs.getString(207).equals("Y"))
	    {
		gRoundOffBillFinalAmount = true;
	    }
	    else
	    {
		gRoundOffBillFinalAmount = false;
	    }

	    gNoOfDecimalPlace = rs.getInt(208);

	    if (rs.getString(209).equals("Y"))
	    {
		gSendDBBackUpOnClientMail = true;
	    }
	    else
	    {
		gSendDBBackUpOnClientMail = false;
	    }

	    gPrintOrderNoOnBillYN = false;
	    if (rs.getString(210).equals("Y"))
	    {
		gPrintOrderNoOnBillYN = true;
	    }

	    gPrintDeviceAndUserDtlOnKOTYN = false;
	    if (rs.getString(211).equals("Y"))
	    {
		gPrintDeviceAndUserDtlOnKOTYN = true;
	    }
	    gRemoveSCTaxCode = rs.getString(212);

	    gWebBooksWebServiceURL = gCMSWebServiceURL;
	    gWebMMSWebServiceURL = gCMSWebServiceURL;
	    gWebExciseWebServiceURL = gCMSWebServiceURL;
	    gWebBooksWebServiceURL = gWebBooksWebServiceURL.replaceAll("CMSIntegration", "WebBooksIntegration");
	    gWebMMSWebServiceURL = gWebMMSWebServiceURL.replaceAll("CMSIntegration", "MMSIntegration");
	    gWebExciseWebServiceURL = gWebExciseWebServiceURL.replaceAll("CMSIntegration", "ExciseIntegration");

	    gAutoAddKOTToBill = false;
	    if (rs.getString(213).equalsIgnoreCase("Y"))
	    {
		gAutoAddKOTToBill = true;
	    }

	    gAreaWiseCostCenterKOTPrinting = false;
	    if (rs.getString(214).equalsIgnoreCase("Y"))
	    {
		gAreaWiseCostCenterKOTPrinting = true;
	    }

	    gWERAOnlineOrderIntegration = false;
	    if (rs.getString(215).equalsIgnoreCase("Y"))
	    {
		gWERAOnlineOrderIntegration = true;
	    }
	    gWERAMerchantOutletId = rs.getString(216);
	    gWERAAuthenticationAPIKey = rs.getString(217);

	    gFireCommunication = false;
	    if (rs.getString(218).equalsIgnoreCase("Y"))
	    {
		gFireCommunication = true;
	    }

	    gUSDConvertionRate = rs.getDouble(219);
	    gDBBackupReceiverEmailIds = rs.getString(220);

	    gPrintMoveTableMoveKOT=false; 
	    if(rs.getString(221).equalsIgnoreCase("Y"))
	    {
		  gPrintMoveTableMoveKOT=true; 
	    }
	    
	    gPrintQtyTotal=false; 
	    if(rs.getString(222).equalsIgnoreCase("Y"))
	    {
		  gPrintQtyTotal=true; 
	    }
	    
	    gShowReportsInCurrency=rs.getString(223);
	    gPOSToMMSPostingCurrency=rs.getString(224);
	    gPOSToWebBooksPostingCurrency=rs.getString(225);
	    
	    gLockTableForWaiter=false; 
	    if(rs.getString(226).equalsIgnoreCase("Y"))
	    {
		  gLockTableForWaiter=true; 
	    }
	    
	    
	    
	    
	    
	    
	    
	    
	    

	}
	else
	{
	    new clsUtility2().funSavePropertySetup(posCode);
	    funSetGlobalParameters(posCode);
	}
	rs.close();
	gChangeModule = "N";

	String sqlArea = "select strAreaCode from tblareamaster where strAreaName='All'";
	ResultSet rsArea = clsGlobalVarClass.dbMysql.executeResultSet(sqlArea);
	if (rsArea.next())
	{
	    gAreaCodeForTrans = rsArea.getString(1);
	}
	rsArea.close();

	gConnectionActive = "Y";
	Date sysDate = new Date();
	gSystemDate = (sysDate.getYear() + 1900) + "-" + (sysDate.getMonth() + 1) + "-" + sysDate.getDate();
	clsGlobalVarClass.gFlgPoints = "";
	//funInitTables();
	funFillCustInfoForHomeDelivery();
	funFillTakeAwayMap();
	hmActiveForms = new HashMap<String, String>();
    }

    public static DecimalFormat funGetGlobalDecimalFormatter()
    {

	DecimalFormat gDecimalFormat = new DecimalFormat(funGetGlobalDecimalFormatString());

	return gDecimalFormat;
    }

    public static String funGetGlobalDecimalFormatString()
    {

	StringBuilder decimalFormatBuilderForDoubleValue = new StringBuilder("0");
	for (int i = 0; i < clsGlobalVarClass.gNoOfDecimalPlace; i++)
	{
	    if (i == 0)
	    {
		decimalFormatBuilderForDoubleValue.append(".0");
	    }
	    else
	    {
		decimalFormatBuilderForDoubleValue.append("0");
	    }
	}
	return decimalFormatBuilderForDoubleValue.toString();
    }
}
