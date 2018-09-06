/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSGlobal.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import static com.POSGlobal.controller.clsGlobalVarClass.dbMysql;
import static com.POSGlobal.controller.clsGlobalVarClass.gHOPOSType;
import static com.POSGlobal.controller.clsGlobalVarClass.gPOSStartDate;
import static com.POSGlobal.controller.clsGlobalVarClass.gQueryForSearch;
import static com.POSGlobal.controller.clsGlobalVarClass.gSanguineWebServiceURL;
import static com.POSGlobal.controller.clsGlobalVarClass.vArrSearchColumnSize;
import com.POSGlobal.controller.clsBenowIntegration;
import com.POSGlobal.controller.clsOperatorDtl;
import com.POSGlobal.controller.clsSettelementOptions;
import com.POSGlobal.controller.clsTextFieldOnlyNumber;
import com.POSGlobal.controller.clsUtility;
import com.POSPrinting.clsQRCodePrinter;
import com.toedter.calendar.JDateChooser;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class frmPropertySetup extends javax.swing.JFrame
{

    private boolean longFooter, flagCmbPOSActionPerformed;
    private String dteCreated, cityName, stateName, countryName, showBill, pincode = "", HOServerTime, reportImagePath = "";
    private String dteEdited, userCode, code, dteEndDate, negBilling, multiBillPrint, areaWisePricing, KOTPrintingForDB;
    private String sql, effectOnPSP, printVatNo, printServiceTaxno, discount, kot, ManualBillNo, rfidInterface;
    private String dayEnd, ServerHostName, SmstextArea, selectedLikePOSCode = "NA", newPropertyPOSCode, JioDeviceIDFromDB = "";
    private ResultSet rs, countSet;
    private clsGlobalVarClass globelvalidate;

    private JDateChooser dteEndChooser;
    private FileInputStream reportImgInputStream = null;
    private File reportImageFile = null;
    private ImageIcon imageIcon = null;
    private clsUtility objUtility;

    private List<String> arrListPrinters;
    private Map<String, String> mapCostCenter;
    private Map<String, String> mapPOS;
    private Map<String, String> mapAreaNameWithCode;
    private Map<String, String> mapAreaCodeWithName;
    private Map<String, String> mapCodeWithName;
    private Map<String, String> mapNameWithCode;
    private Map<String, String> mapSelectedCodeWithName;
    private Map<Integer, List<String>> mapBillSeriesCodeList;
    private Map<Integer, List<String>> mapBillSeriesNameList;
    private boolean JioDeviceIDFound = false;
    private Map<String, String> mapTaxCodeWithName, mapTaxNameWithCode;

    public frmPropertySetup()
    {
	initComponents();
	dteEndChooser = new JDateChooser();
	lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
	objUtility = new clsUtility();
	try
	{
	    arrListPrinters = new ArrayList<String>();
	    Vector vPrinterNames = objUtility.funGetPrinterNames();
	    for (int cntPrinters = 0; cntPrinters < vPrinterNames.size(); cntPrinters++)
	    {
		arrListPrinters.add(vPrinterNames.elementAt(cntPrinters).toString());
	    }

//            String filePath = System.getProperty("user.dir");
//            reportImageFile = new File(filePath + "/ReportImage/imgClientImage.jpg");
//            if (reportImageFile.isFile())
//            {
//                funSetImage();
//            }
//            else
//            {
//                lblReportImageIcon.setText("           Report Image");
//            }
	    SmstextArea = "";
	    cmbPrintType.setSelectedItem(clsGlobalVarClass.gPrintType);
	    int col = clsGlobalVarClass.gColumnSize;
	    if (col == 40)
	    {
		cmbColumnSize.setSelectedIndex(0);
	    }
	    else
	    {
		cmbColumnSize.setSelectedIndex(1);
	    }
	    Double max = clsGlobalVarClass.gMaxDiscount;
	    txtMaxDiscount.setText(String.valueOf(Math.rint(max)));

	    if (clsGlobalVarClass.gAreaWisePricing.equalsIgnoreCase("Y"))
	    {
		chkAreaWisePricing.setSelected(true);
	    }
	    else
	    {
		chkAreaWisePricing.setSelected(false);
	    }
	    String theme = cmbChangeTheme.getSelectedItem().toString();

	    txtPincode.setDocument(new clsTextFieldOnlyNumber(6, 2).new JNumberFieldFilter());
	    rfidInterface = "N";
	    userCode = clsGlobalVarClass.gUserCode;
	    lblUserCode.setText(clsGlobalVarClass.gUserCode);
	    lblPosName.setText(clsGlobalVarClass.gPOSName);
	    discount = "10%";
	    java.util.Date dt = new java.util.Date();
	    int day = dt.getDate();
	    int month = dt.getMonth() + 1;
	    int year = dt.getYear() + 1900;
	    String dte = day + "-" + month + "-" + year;
	    lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
	    dteEndChooser.setDate(dt);
	    txtMaxDiscount.setDocument(new clsTextFieldOnlyNumber(2, 3).new JNumberFieldFilter());
	    flagCmbPOSActionPerformed = false;

	    mapPOS = new HashMap<String, String>();
	    cmbPOSForDayEnd.removeAllItems();
	    sql = "select strPOSCode,strPOSName from tblposmaster where strOperationalYN='Y' ";
	    ResultSet rsPOS = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    cmbPOS.removeAllItems();
	    cmbPOS.addItem("All                                                                        All");
	    while (rsPOS.next())
	    {
		mapPOS.put(rsPOS.getString(2), rsPOS.getString(1));
		cmbPOSForDayEnd.addItem(rsPOS.getString(2));
		cmbPOS.addItem(rsPOS.getString(2) + "                                                                        " + rsPOS.getString(1));
	    }
	    rsPOS.close();

	    sql = "select strPOSCode from tblsetup where strPOSCode<>'All' ";
	    ResultSet rsPSForAll = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsPSForAll.next())
	    {
		cmbPOS.setSelectedIndex(1);
	    }
	    rsPSForAll.close();

	    flagCmbPOSActionPerformed = true;

	    mapTaxCodeWithName = new HashMap<String, String>();
	    mapTaxNameWithCode = new HashMap<String, String>();

	    sql = "select a.strTaxCode,a.strTaxDesc from tbltaxhd a ";
	    ResultSet rsTax = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    cmbRemoveServiceChargeTaxCode.removeAllItems();
	    cmbRemoveServiceChargeTaxCode.addItem("");
	    mapTaxCodeWithName.put("", "");
	    mapTaxNameWithCode.put("", "");
	    while (rsTax.next())
	    {
		cmbRemoveServiceChargeTaxCode.addItem(rsTax.getString(2));
		mapTaxCodeWithName.put(rsTax.getString(1), rsTax.getString(2));
		mapTaxNameWithCode.put(rsTax.getString(2), rsTax.getString(1));
	    }
	    rsPOS.close();

	    funSetDataPOSWise();

	    txtOTP.setVisible(false);
	    btnOK.setVisible(false);

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funSetShortCutKeys()
    {
	btnNew.setMnemonic('u');
	btnExit.setMnemonic('c');
    }

    public void funFillArea()
    {
	try
	{
	    cmbDineInAreaForDirectBiller.removeAllItems();
	    cmbHomeDeliAreaForDirectBiller.removeAllItems();
	    cmbTakeAwayAreaForDirectBiller.removeAllItems();

	    mapAreaNameWithCode = new HashMap<String, String>();
	    mapAreaCodeWithName = new HashMap<String, String>();

	    String sqlDirectArea = "select * from tblareamaster where (strPOSCode='" + newPropertyPOSCode + "' or strPOSCode='All' )";
	    ResultSet rrs = clsGlobalVarClass.dbMysql.executeResultSet(sqlDirectArea);
	    while (rrs.next())
	    {
		mapAreaNameWithCode.put(rrs.getString(2), rrs.getString(1));
		mapAreaCodeWithName.put(rrs.getString(1), rrs.getString(2));

		cmbDineInAreaForDirectBiller.addItem(rrs.getString(2));
		cmbHomeDeliAreaForDirectBiller.addItem(rrs.getString(2));
		cmbTakeAwayAreaForDirectBiller.addItem(rrs.getString(2));
	    }
	    rrs.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funFillData()
    {
	JioDeviceIDFromDB = "";
	JioDeviceIDFound = false;
	try
	{
	    int count = 0;

	    cmbMenuItemDisSeq.removeAllItems();
	    cmbMenuItemDisSeq.addItem("Ascending");
	    cmbMenuItemDisSeq.addItem("As Entered");
	    sql = "select count(*) from tblsetup where strPOSCode='" + newPropertyPOSCode + "' ";
	    rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rs.next())
	    {
		count = rs.getInt(1);
	    }
	    rs.close();

	    if (count > 0)
	    {
		btnNew.setText("UPDATE");

		sql = "select * from  tblsetup where strPOSCode='" + newPropertyPOSCode + "' ";
		rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		while (rs.next())
		{
		    txtClientCode.setText(rs.getString(1));
		    txtClientName.setText(rs.getString(2));
		    txtShopAddressLine1.setText(rs.getString(3));
		    txtShopAddressLine2.setText(rs.getString(4));
		    txtShopAddressLine3.setText(rs.getString(5));
		    txtEmailAddress.setText(rs.getString(6));
		    txtBillFooter.setText(rs.getString(7));
		    cmbBillPaperSize.setSelectedItem(rs.getString(9));
		    if (rs.getString(10).equals("Y"))
		    {
			chkNegBilling.setSelected(true);
		    }
		    else
		    {
			chkNegBilling.setSelected(false);
		    }
		    if (rs.getString(11).equals("Y"))
		    {
			chkDayEnd.setSelected(true);
		    }
		    else
		    {
			chkDayEnd.setSelected(false);
		    }
		    cmbPrintMode.setSelectedItem(rs.getString(12));
		    cmbCity.setSelectedItem(rs.getString(14));
		    cmbState.setSelectedItem(rs.getString(15));
		    cmbCountry.setSelectedItem(rs.getString(16));
		    txtTelephone.setText(rs.getString(17));
		    dteEndChooser.setDate(rs.getDate(19));
		    cmbNatureOfBusiness.setSelectedItem(rs.getString(20));
		    if (rs.getString(21).equals("Y"))
		    {
			chkMultiBillPrint.setSelected(true);
		    }
		    else
		    {
			chkMultiBillPrint.setSelected(false);
		    }

		    if (rs.getString(22).equals("Y"))
		    {
			chkEnableKOT.setSelected(true);
		    }
		    else
		    {
			chkEnableKOT.setSelected(false);
		    }

		    if (rs.getString(23).equals("Y"))
		    {
			chkEffectOnPSP.setSelected(true);
		    }
		    else
		    {
			chkEffectOnPSP.setSelected(false);
		    }

		    if (rs.getString(24).equals("Y"))
		    {
			chkPrintVatNo.setSelected(true);
			txtVatNo.setText(rs.getString(25));
		    }
		    else
		    {
			chkPrintVatNo.setSelected(false);
			txtVatNo.setText(rs.getString(25));
		    }

		    if (rs.getString(26).equals("Y"))
		    {
			chkShowBill.setSelected(true);
		    }
		    else
		    {
			chkShowBill.setSelected(false);
		    }

		    if (rs.getString(27).equals("Y"))
		    {
			chkServiceTaxNo.setSelected(true);
			txtServiceTaxno.setText(rs.getString(28));
		    }
		    else
		    {
			chkServiceTaxNo.setSelected(false);
			txtServiceTaxno.setText(rs.getString(28));
		    }

		    if (rs.getString(29).equals("Y"))
		    {
			chkManualBillNo.setSelected(true);
		    }
		    else
		    {
			chkManualBillNo.setSelected(false);
		    }

		    if (rs.getString(30).equals("Ascending"))
		    {
			cmbMenuItemDisSeq.setSelectedIndex(0);
		    }
		    else
		    {
			cmbMenuItemDisSeq.setSelectedIndex(1);
		    }
		    txtSenderEmailId.setText(rs.getString(31));
		    txtEmailPassword.setText(rs.getString(32));
		    txtConfirmEmailPassword.setText(rs.getString(33));
		    txtBodyPart.setText(rs.getString(34));
		    if (rs.getString(35).equals("smtp.gmail.com"))
		    {
			cmbServerName.setSelectedIndex(0);
		    }
		    else
		    {
			cmbServerName.setSelectedIndex(1);
		    }

		    txtAreaSMSApi.setText(rs.getString(36));
		    strPOSType.setSelectedItem(rs.getString(41));
		    txtWebServiceLink.setText(rs.getString(42));
		    cmbDataSendFrequency.setSelectedItem(rs.getString(43));
		    cmbRFIDSetup.setSelectedItem("No");
		    funSetDatabaseCredentials(false);

		    if (!rs.getString(44).isEmpty())
		    {

			String HOServerDate = rs.getString(44);
			HOServerTime = HOServerDate.split(" ")[1].trim();
			HOServerDate = HOServerDate.split(" ")[0].trim();

			if (objUtility.funValidateDateFormat("yyyy-MM-dd", HOServerDate))
			{
			    HOServerDate = HOServerDate.split("-")[2] + "-" + HOServerDate.split("-")[1] + "-" + HOServerDate.split("-")[0];
			    System.out.println(HOServerDate);
			}
			else
			{
			    Date dt = new Date();
			    HOServerDate = dt.getDate() + "-" + (dt.getMonth() + 1) + "-" + (dt.getYear() + 1900);
			}
			java.util.Date date = new SimpleDateFormat("dd-MM-yyyy").parse(HOServerDate);
			dteHOServerDate.setDate(date);
		    }
		    else
		    {
			Date dt = new Date();
			String HOServerDate = dt.getDate() + "-" + (dt.getMonth() + 1) + "-" + (dt.getYear() + 1900);
			java.util.Date date = new SimpleDateFormat("dd-MM-yyyy").parse(HOServerDate);
			dteHOServerDate.setDate(date);
		    }

		    if (rs.getString(45).equals("Y"))
		    {
			cmbRFIDSetup.setSelectedItem("Yes");
			funSetDatabaseCredentials(true);
		    }
		    txtServerName.setText(rs.getString(46));
		    txtUserName.setText(rs.getString(47));
		    txtPassword.setText(rs.getString(48));
		    txtDatabaseName.setText(rs.getString(49));
		    if ("Y".equalsIgnoreCase(rs.getString(50)))
		    {
			chkPrintKotForDirectBiller.setSelected(true);
		    }
		    else
		    {
			chkPrintKotForDirectBiller.setSelected(false);
		    }
		    txtPincode.setText(rs.getString(51));

		    cmbChangeTheme.setSelectedItem(rs.getString(52));

		    txtMaxDiscount.setText(rs.getString(53));

		    if ("Y".equalsIgnoreCase(rs.getString(54)))
		    {
			chkAreaWisePricing.setSelected(true);
		    }
		    else
		    {
			chkAreaWisePricing.setSelected(false);
		    }

		    cmbMenuItemSortingOn.setSelectedItem(rs.getString(55));

		    String menuItemSortingOn = rs.getString(55);
		    if ("subgroupWise".equalsIgnoreCase(menuItemSortingOn))
		    {

			cmbMenuItemSortingOn.setSelectedIndex(1);
		    }
		    else if ("subMenuHeadWise".equalsIgnoreCase(menuItemSortingOn))
		    {

			cmbMenuItemSortingOn.setSelectedIndex(2);
		    }
		    else
		    {
			cmbMenuItemSortingOn.setSelectedIndex(0);
		    }
		    if (rs.getString(59).equals("Y"))
		    {
			chkEditHomeDelivery.setSelected(true);
		    }
		    else
		    {
			chkEditHomeDelivery.setSelected(false);
		    }

		    if (rs.getString(60).equals("Y"))
		    {
			chkSlabBasedHomeDelCharges.setSelected(true);
		    }
		    else
		    {
			chkSlabBasedHomeDelCharges.setSelected(false);
		    }

		    if (rs.getString(62).equals("Y"))
		    {
			chkSkip_Waiter_Selection.setSelected(true);
		    }
		    else
		    {
			chkSkip_Waiter_Selection.setSelected(false);
		    }

		    String directKOTPrintFromMakeKOT = rs.getString(63);

		    if (rs.getString(64).equals("Y"))
		    {
			chkSkip_pax_selection.setSelected(true);
		    }
		    else
		    {
			chkSkip_pax_selection.setSelected(false);
		    }
		    if (rs.getString(65).equals("SQY"))
		    {
			cmbCRMType.setSelectedIndex(0);
			txtGetWebservice.setText(rs.getString(66));
			txtPostWebservice.setText(rs.getString(67));
			txtOutletUID.setText(rs.getString(68));
			txtPOSID.setText(rs.getString(69));
		    }
		    else if (rs.getString(65).equals("PMAM"))
		    {
			cmbCRMType.setSelectedIndex(1);
			txtGetWebservice.setText(rs.getString(66));
			txtPostWebservice.setText(rs.getString(67));
			txtOutletUID.setText(rs.getString(68));
			txtPOSID.setText(rs.getString(69));
		    }
		    else if (rs.getString(65).equalsIgnoreCase("HASH TAG CRM Interface"))
		    {
			cmbCRMType.setSelectedItem("HASH TAG CRM Interface");
			txtGetWebservice.setText(rs.getString(66));
			txtPostWebservice.setText(rs.getString(67));
			txtOutletUID.setText(rs.getString(68));
			txtPOSID.setText(rs.getString(69));
		    }
		    else
		    {
			cmbCRMType.setSelectedIndex(2);
			txtGetWebservice.setText("");
			txtPostWebservice.setText("");
			txtOutletUID.setText("");
			txtPOSID.setText("");
		    }
		    funCheckCRMInterface();

		    if (rs.getString(70).equals("ItemWise"))
		    {
			cmbStockInOption.setSelectedIndex(0);
		    }
		    else if (rs.getString(70).equals("MenuHeadWise"))
		    {
			cmbStockInOption.setSelectedIndex(1);
		    }
		    txtCustSeries.setText(rs.getString(71));
		    txtAdvRecPrintCount.setText(rs.getString(72));

		    txtAreaSendHomeDeliverySMS.setText(rs.getString(73));
		    txtAreaBillSettlementSMS.setText(rs.getString(74));

		    cmbBillFormatType.setSelectedItem(rs.getString(75));

		    if (rs.getString(76).equalsIgnoreCase("Y"))
		    {
			chkActivePromotions.setSelected(true);
		    }
		    else
		    {
			chkActivePromotions.setSelected(false);
		    }

		    chkHomeDelSMS.setSelected(false);
		    chkBillSettlementSMS.setSelected(false);

		    if (rs.getString(77).equals("Y"))
		    {
			chkHomeDelSMS.setSelected(true);
		    }
		    else
		    {
			chkHomeDelSMS.setSelected(false);
		    }

		    if (rs.getString(78).equals("Y"))
		    {
			chkBillSettlementSMS.setSelected(true);
		    }
		    else
		    {
			chkBillSettlementSMS.setSelected(false);
		    }

		    cmbSMSType.setSelectedItem(rs.getString(79));

		    if (rs.getString(80).equalsIgnoreCase("Y"))
		    {
			chkPrintShortNameOnKOT.setSelected(true);
		    }
		    else
		    {
			chkPrintShortNameOnKOT.setSelected(false);
		    }

		    /*
                     if (rs.getString(81).equals("Y"))
                     {
                     chkShowCustHelp.setSelected(true);
                     }*/
		    if (rs.getString(82).equals("Y"))
		    {
			chkPrintForVoidBill.setSelected(true);
		    }
		    else
		    {
			chkPrintForVoidBill.setSelected(false);
		    }

		    if (rs.getString(83).equals("Y"))
		    {
			chkPostSalesDataToMMS.setSelected(true);
		    }
		    else
		    {
			chkPostSalesDataToMMS.setSelected(false);
		    }

		    if (rs.getString(84).equals("Y"))
		    {
			chkAreaMasterCompulsory.setSelected(true);
		    }
		    else
		    {
			chkAreaMasterCompulsory.setSelected(false);
		    }

		    if (rs.getString(85).equals("Item Master"))
		    {
			cmbPriceFrom.setSelectedIndex(1);
		    }

		    if (rs.getString(85).equals("Menu Pricing"))
		    {
			cmbPriceFrom.setSelectedIndex(0);
		    }

		    if (rs.getString(86).equals("Y"))
		    {
			chkPrinterErrorMessage.setSelected(true);
		    }
		    else
		    {
			chkPrinterErrorMessage.setSelected(false);
		    }

		    if (rs.getString(91).equals("Y"))
		    {
			chkChangeQtyForExternalCode.setSelected(true);
		    }
		    else
		    {
			chkChangeQtyForExternalCode.setSelected(false);
		    }

		    if (rs.getString(92).equals("Y"))
		    {
			chkPointsOnBillPrint.setSelected(true);
		    }
		    else
		    {
			chkPointsOnBillPrint.setSelected(false);
		    }

		    cmbCardIntfType.setSelectedItem(rs.getString(88));

		    if (rs.getString(89).equalsIgnoreCase("Y"))
		    {
			cmbCMSIntegrationYN.setSelectedIndex(1);
		    }

		    txtCMSWesServiceURL.setText(rs.getString(90));

		    if (rs.getString(91).equals("Y"))
		    {
			chkChangeQtyForExternalCode.setSelected(true);
		    }
		    else
		    {
			chkChangeQtyForExternalCode.setSelected(false);
		    }

		    if (rs.getString(92).equals("Y"))
		    {
			chkPointsOnBillPrint.setSelected(true);
		    }
		    else
		    {
			chkPointsOnBillPrint.setSelected(false);
		    }

		    if (rs.getString(94).equals("Y"))
		    {
			chkManualAdvOrderCompulsory.setSelected(true);
		    }
		    else
		    {
			chkManualAdvOrderCompulsory.setSelected(false);
		    }

		    if (rs.getString(95).equals("Y"))
		    {
			chkPrintManualAdvOrderOnBill.setSelected(true);
		    }
		    else
		    {
			chkPrintManualAdvOrderOnBill.setSelected(false);
		    }

		    if (rs.getString(96).equals("Y"))
		    {
			chkPrintModifierQtyOnKOT.setSelected(true);
		    }
		    else
		    {
			chkPrintModifierQtyOnKOT.setSelected(false);
		    }

		    txtNoOfLinesInKOTPrint.setText(rs.getString(97));

		    if (rs.getString(98).equalsIgnoreCase("Y"))
		    {
			chkMultiKOTPrint.setSelected(true);
		    }
		    else
		    {
			chkMultiKOTPrint.setSelected(false);
		    }
		    if (rs.getString(99).equals("Y"))
		    {
			chkItemQtyNumpad.setSelected(true);
		    }
		    else
		    {
			chkItemQtyNumpad.setSelected(false);
		    }
		    if (rs.getString(100).equals("Y"))
		    {
			chkMemberAsTable.setSelected(true);
		    }
		    else
		    {
			chkMemberAsTable.setSelected(false);
		    }

		    if (rs.getString(101).equals("Y"))
		    {
			chkPrintKOTToLocalPrinter.setSelected(true);
		    }
		    else
		    {
			chkPrintKOTToLocalPrinter.setSelected(false);
		    }

		    if (rs.getString(103).equals("Y"))
		    {
			chkEnableSettleBtnForDirectBillerBill.setSelected(true);
		    }
		    else
		    {
			chkEnableSettleBtnForDirectBillerBill.setSelected(false);
		    }

		    if (rs.getString(104).equals("Y"))
		    {
			chkDelBoyCompulsoryOnDirectBiller.setSelected(true);
		    }
		    else
		    {
			chkDelBoyCompulsoryOnDirectBiller.setSelected(false);
		    }

		    if (rs.getString(105).equals("Y"))
		    {
			chkMemberCodeForKOTJPOS.setSelected(true);
		    }
		    else
		    {
			chkMemberCodeForKOTJPOS.setSelected(false);
		    }

		    if (rs.getString(106).equals("Y"))
		    {
			chkMemberCodeForKOTMPOS.setSelected(true);
		    }
		    else
		    {
			chkMemberCodeForKOTMPOS.setSelected(false);
		    }

		    if (rs.getString(107).equals("Y"))
		    {
			chkDontShowAdvOrderInOtherPOS.setSelected(true);
		    }
		    else
		    {
			chkDontShowAdvOrderInOtherPOS.setSelected(false);
		    }

		    if (rs.getString(108).equals("Y"))
		    {
			chkPrintZeroAmtModifierInBill.setSelected(true);
		    }
		    else
		    {
			chkPrintZeroAmtModifierInBill.setSelected(false);
		    }

		    if (rs.getString(109).equals("Y"))
		    {
			chkPrintKOTYN.setSelected(true);
		    }
		    else
		    {
			chkPrintKOTYN.setSelected(false);
		    }

		    if (rs.getString(110).equals("Y"))
		    {
			chkSlipNoForCreditCardBillYN.setSelected(true);
		    }
		    else
		    {
			chkSlipNoForCreditCardBillYN.setSelected(false);
		    }

		    if (rs.getString(111).equals("Y"))
		    {
			chkExpDateForCreditCardBillYN.setSelected(true);
		    }
		    else
		    {
			chkExpDateForCreditCardBillYN.setSelected(false);
		    }

		    if (rs.getString(112).equals("Y"))
		    {
			chkSelectWaiterFromCardSwipe.setSelected(true);
		    }
		    else
		    {
			chkSelectWaiterFromCardSwipe.setSelected(false);
		    }

		    if (rs.getString(113).equals("Y"))
		    {
			chkMultipleWaiterSelectionOnMakeKOT.setSelected(true);
		    }
		    else
		    {
			chkMultipleWaiterSelectionOnMakeKOT.setSelected(false);
		    }

		    if (rs.getString(114).equals("Y"))
		    {
			chkMoveTableToOtherPOS.setSelected(true);
		    }
		    else
		    {
			chkMoveTableToOtherPOS.setSelected(false);
		    }

		    if (rs.getString(115).equals("Y"))
		    {
			chkMoveKOTToOtherPOS.setSelected(true);
		    }
		    else
		    {
			chkMoveKOTToOtherPOS.setSelected(false);
		    }

		    if (rs.getString(116).equals("Y"))
		    {
			chkCalculateTaxOnMakeKOT.setSelected(true);
		    }
		    else
		    {
			chkCalculateTaxOnMakeKOT.setSelected(false);
		    }

		    txtReceiverEmailId.setText(rs.getString(117));

		    if (rs.getString(118).equals("Y"))
		    {
			chkCalculateDiscItemWise.setSelected(true);
		    }
		    else
		    {
			chkCalculateDiscItemWise.setSelected(false);
		    }

		    if (rs.getString(119).equals("Y"))
		    {
			chkTakewayCustomerSelection.setSelected(true);
		    }
		    else
		    {
			chkTakewayCustomerSelection.setSelected(false);
		    }

		    if (rs.getString(120).equals("Y"))
		    {
			chkShowItemStkColumnInDB.setSelected(true);
		    }
		    else
		    {
			chkShowItemStkColumnInDB.setSelected(false);
		    }

		    cmbItemType.setSelectedItem(rs.getString(121));

		    if (rs.getString(122).equalsIgnoreCase("Y"))
		    {
			chkBoxAllowNewAreaMasterFromCustMaster.setSelected(true);
		    }
		    else
		    {
			chkBoxAllowNewAreaMasterFromCustMaster.setSelected(false);
		    }

		    if (rs.getString(123).equalsIgnoreCase("Y"))
		    {
			chkSelectCustAddressForBill.setSelected(true);
		    }
		    else
		    {
			chkSelectCustAddressForBill.setSelected(false);
		    }

		    if (rs.getString(124).equalsIgnoreCase("Y"))
		    {
			chkGenrateMI.setSelected(true);
		    }
		    else
		    {
			chkGenrateMI.setSelected(false);
		    }
		    txtFTPAddress.setText(rs.getString(125));
		    txtFTPServerUserName.setText(rs.getString(126));
		    txtFTPServerPass.setText(rs.getString(127));

		    if (rs.getString(128).equalsIgnoreCase("Y"))
		    {
			chkAllowToCalculateItemWeight.setSelected(true);
		    }
		    else
		    {
			chkAllowToCalculateItemWeight.setSelected(false);
		    }
		    if (rs.getString(129).equalsIgnoreCase("Table Detail Wise"))
		    {
			cmbShowBillsDtlType.setSelectedIndex(0);
		    }
		    else
		    {
			cmbShowBillsDtlType.setSelectedIndex(1);
		    }
		    if (rs.getString(130).equalsIgnoreCase("Y"))
		    {
			chkPrintInvoiceOnBill.setSelected(true);
		    }
		    else
		    {
			chkPrintInvoiceOnBill.setSelected(false);
		    }
		    if (rs.getString(131).equalsIgnoreCase("Y"))
		    {
			chkPrintInclusiveOfAllTaxesOnBill.setSelected(true);
		    }
		    else
		    {
			chkPrintInclusiveOfAllTaxesOnBill.setSelected(false);
		    }
		    cmbApplyDiscountOn.setSelectedItem(rs.getString(132));

		    if (rs.getString(133).equalsIgnoreCase("Y"))
		    {
			chkMemberCodeForKotInMposByCardSwipe.setSelected(true);
		    }
		    else
		    {
			chkMemberCodeForKotInMposByCardSwipe.setSelected(false);
		    }

		    if (rs.getString(134).equalsIgnoreCase("Y"))
		    {
			chkPrintBill.setSelected(true);
		    }
		    else
		    {
			chkPrintBill.setSelected(false);
		    }

		    if (rs.getString(135).equalsIgnoreCase("Y"))
		    {
			chkUseVatAndServiceNoFromPos.setSelected(true);
		    }
		    else
		    {
			chkUseVatAndServiceNoFromPos.setSelected(false);
		    }
		    if (rs.getString(136).equalsIgnoreCase("Y"))
		    {
			chkMemberCodeForMakeBillInMPOS.setSelected(true);
		    }
		    else
		    {
			chkMemberCodeForMakeBillInMPOS.setSelected(false);
		    }
		    if (rs.getString(137).equalsIgnoreCase("Y"))
		    {
			chkItemWiseKOTPrintYN.setSelected(true);
		    }
		    else
		    {
			chkItemWiseKOTPrintYN.setSelected(false);
		    }

		    sql = "select strPOSName from tblposmaster where strPOSCode='" + rs.getString(138) + "'";
		    ResultSet rsPOS = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		    if (rsPOS.next())
		    {
			cmbPOSForDayEnd.setSelectedItem(rsPOS.getString(1));
		    }
		    rsPOS.close();

		    String CMSPostingType = rs.getString(139);
		    if ("Sanguine CMS".equalsIgnoreCase(CMSPostingType))
		    {

			cmbCMSPostingType.setSelectedItem(CMSPostingType);
		    }
		    else
		    {
			cmbCMSPostingType.setSelectedItem("Others");
		    }

		    if (rs.getString(140).equalsIgnoreCase("Y"))
		    {
			chkPopUpToApplyPromotionsOnBill.setSelected(true);
		    }
		    else
		    {
			chkPopUpToApplyPromotionsOnBill.setSelected(false);
		    }

		    if (rs.getString(141).equalsIgnoreCase("Y"))
		    {
			chkSelectCustomerCodeFromCardSwipe.setSelected(true);
		    }
		    else
		    {
			chkSelectCustomerCodeFromCardSwipe.setSelected(false);
		    }

		    if (rs.getString(142).equalsIgnoreCase("Y"))
		    {
			chkCheckDebitCardBalOnTrans.setSelected(true);
		    }
		    else
		    {
			chkCheckDebitCardBalOnTrans.setSelected(false);
		    }

		    if (rs.getString(143).equalsIgnoreCase("Y"))
		    {
			chkSettlementsFromPOSMaster.setSelected(true);
		    }
		    else
		    {
			chkSettlementsFromPOSMaster.setSelected(false);
		    }

		    if (rs.getString(144).equalsIgnoreCase("Y"))
		    {
			chkShiftWiseDayEnd.setSelected(true);
		    }
		    else
		    {
			chkShiftWiseDayEnd.setSelected(false);
		    }

		    if (rs.getString(145).equalsIgnoreCase("Y"))
		    {
			chkProductionLinkup.setSelected(true);
		    }
		    else
		    {
			chkProductionLinkup.setSelected(false);
		    }

		    if (rs.getString(146).equalsIgnoreCase("Y"))
		    {
			chkLockDataOnShift.setSelected(true);
		    }
		    else
		    {
			chkLockDataOnShift.setSelected(false);
		    }

		    txtWSClientCode.setText(rs.getString(147));

		    if (rs.getString(149).equalsIgnoreCase("Y"))
		    {
			chkEnableBillSeries.setSelected(true);
		    }
		    else
		    {
			chkEnableBillSeries.setSelected(false);
		    }
		    if (rs.getString(150).equalsIgnoreCase("Y"))
		    {
			chkEnablePMSIntegration.setSelected(true);
		    }
		    else
		    {
			chkEnablePMSIntegration.setSelected(false);
		    }

		    if (rs.getString(151).equalsIgnoreCase("Y"))
		    {
			chkPrintTimeOnBill.setSelected(true);
		    }
		    else
		    {
			chkPrintTimeOnBill.setSelected(false);
		    }

		    if (rs.getString(152).equalsIgnoreCase("Y"))
		    {
			chkPrintTDHItemsInBill.setSelected(true);
		    }
		    else
		    {
			chkPrintTDHItemsInBill.setSelected(false);
		    }

		    if (rs.getString(153).equalsIgnoreCase("Y"))
		    {
			chkPrintRemarkAndReasonForReprint.setSelected(true);
		    }
		    else
		    {
			chkPrintRemarkAndReasonForReprint.setSelected(false);
		    }
		    txtDaysBeforeOrderToCancel.setText(rs.getString(154));
		    txtNoOfDelDaysForAdvOrder.setText(rs.getString(155));
		    txtNoOfDelDaysForUrgentOrder.setText(rs.getString(156));
		    if (rs.getString(157).equalsIgnoreCase("Y"))
		    {
			chkSetUpToTimeForAdvOrder.setSelected(true);
		    }
		    else
		    {
			chkSetUpToTimeForAdvOrder.setSelected(false);
		    }

		    if (rs.getString(158).equalsIgnoreCase("Y"))
		    {
			chkSetUpToTimeForUrgentOrder.setSelected(true);
		    }
		    else
		    {
			chkSetUpToTimeForUrgentOrder.setSelected(false);
		    }

		    String upToTimeForAdvOrder = rs.getString(159).split(" ")[0];
		    cmbHours.setSelectedItem(upToTimeForAdvOrder.split(":")[0].trim());
		    cmbMinutes.setSelectedItem(upToTimeForAdvOrder.split(":")[1].trim());
		    cmbAMPM.setSelectedItem(rs.getString(159).split(" ")[1]);

		    String upToTimeForUrgentOrder = rs.getString(160).split(" ")[0];
		    cmbHoursUrgentOrder.setSelectedItem(upToTimeForUrgentOrder.split(":")[0].trim());
		    cmbMinutesUrgentOrder.setSelectedItem(upToTimeForUrgentOrder.split(":")[1].trim());
		    cmbAMPMUrgent.setSelectedItem(rs.getString(160).split(" ")[1]);

		    if (rs.getString(161).equalsIgnoreCase("Y"))
		    {
			chkEnableBothPrintAndSettleBtnForDB.setSelected(true);
		    }
		    else
		    {
			chkEnableBothPrintAndSettleBtnForDB.setSelected(false);
		    }

		    if ("Y".equalsIgnoreCase(rs.getString(162)))
		    {
			cmbInrestoPOSIntegrationYN.setSelectedIndex(1);
		    }

		    txtInrestoPOSWesServiceURL.setText(rs.getString(163));
		    txtInrestoPOSId.setText(rs.getString(164));
		    txtInrestoPOSKey.setText(rs.getString(165));

		    if (rs.getString(166).equalsIgnoreCase("Y"))
		    {
			chkCarryForwardFloatAmtToNextDay.setSelected(true);
		    }
		    else
		    {
			chkCarryForwardFloatAmtToNextDay.setSelected(false);
		    }
		    if (rs.getString(167).equalsIgnoreCase("Y"))
		    {
			chkOpenCashDrawerAfterBillPrint.setSelected(true);
		    }
		    else
		    {
			chkOpenCashDrawerAfterBillPrint.setSelected(false);
		    }
		    if (rs.getString(168).equalsIgnoreCase("Y"))
		    {
			chkPropertyWiseSalesOrder.setSelected(true);
		    }
		    else
		    {
			chkPropertyWiseSalesOrder.setSelected(false);
		    }
		    if (rs.getString(170).equalsIgnoreCase("Y"))
		    {
			chkShowItemDtlsForChangeCustomerOnBill.setSelected(true);
		    }
		    else
		    {
			chkShowItemDtlsForChangeCustomerOnBill.setSelected(false);
		    }

		    if (rs.getString(171).equalsIgnoreCase("Y"))
		    {
			chkShowPopUpForNextItemQuantity.setSelected(true);
		    }
		    else
		    {
			chkShowPopUpForNextItemQuantity.setSelected(false);
		    }
		    if ("Y".equalsIgnoreCase(rs.getString(172)))
		    {
			cmbJioPOSIntegrationYN.setSelectedIndex(1);
		    }
		    txtJioPOSWesServiceURL.setText(rs.getString(173));
		    txtJioMoneyMID.setText(rs.getString(174));
		    txtJioMoneyTID.setText(rs.getString(175));
		    txtJioActivationCode.setText(rs.getString(176));
		    if (!(rs.getString(177).isEmpty()))
		    {
			txtJioDeviceID.setText(rs.getString(177));
			JioDeviceIDFound = true;
			JioDeviceIDFromDB = rs.getString(177);
		    }

		    if ("Y".equalsIgnoreCase(rs.getString(178)))
		    {
			chkNewBillSeriesForNewDay.setSelected(true);
		    }
		    else
		    {
			chkNewBillSeriesForNewDay.setSelected(false);
		    }

		    if ("Y".equalsIgnoreCase(rs.getString(179)))
		    {
			chkShowOnlyLoginPOSReports.setSelected(true);
		    }
		    else
		    {
			chkShowOnlyLoginPOSReports.setSelected(false);
		    }
		    if ("Y".equalsIgnoreCase(rs.getString(180)))
		    {
			chkEnableDineIn.setSelected(true);
		    }
		    else
		    {
			chkEnableDineIn.setSelected(false);
		    }

		    if ("Y".equalsIgnoreCase(rs.getString(181)))
		    {
			chkAutoAreaSelectionInMakeKOT.setSelected(true);
		    }
		    else
		    {
			chkAutoAreaSelectionInMakeKOT.setSelected(false);
		    }

		    txtConsolidatedKOTPrinterPort.setText(rs.getString(182));
		    txtRoundingOffTo.setText(rs.getString(183));
		    if (rs.getString(184).equalsIgnoreCase("Y"))
		    {
			chkShowUnSettlementForm.setSelected(true);
		    }
		    else
		    {
			chkShowUnSettlementForm.setSelected(false);
		    }

		    if (rs.getString(185).equalsIgnoreCase("Y"))
		    {
			chkPrintOpenItemsOnBill.setSelected(true);
		    }
		    else
		    {
			chkPrintOpenItemsOnBill.setSelected(false);
		    }

		    if (rs.getString(186).equalsIgnoreCase("Y"))
		    {
			chkPrintHomeDeliveryYN.setSelected(true);
		    }
		    else
		    {
			chkPrintHomeDeliveryYN.setSelected(false);
		    }

		    if (rs.getString(187).equalsIgnoreCase("Y"))
		    {
			chkScanQRYN.setSelected(true);
		    }
		    else
		    {
			chkScanQRYN.setSelected(false);
		    }

		    if (rs.getString(188).equalsIgnoreCase("Y"))
		    {
			chkAreaWIsePromotions.setSelected(true);
		    }
		    else
		    {
			chkAreaWIsePromotions.setSelected(false);
		    }

		    if (rs.getString(189).equalsIgnoreCase("Y"))
		    {
			chkPrintItemsOnMoveKOTMoveTable.setSelected(true);
		    }
		    else
		    {
			chkPrintItemsOnMoveKOTMoveTable.setSelected(false);
		    }

		    if (rs.getString(190).equalsIgnoreCase("Y"))
		    {
			chkShowPurchaseRateInDirectBiller.setSelected(true);
		    }
		    else
		    {
			chkShowPurchaseRateInDirectBiller.setSelected(false);
		    }

		    if (rs.getString(191).equalsIgnoreCase("Y"))
		    {
			chkTableReservationForCustomer.setSelected(true);
		    }
		    else
		    {
			chkTableReservationForCustomer.setSelected(false);
		    }

		    if (rs.getString(192).equalsIgnoreCase("Y"))
		    {
			chkAutoShowPopItems.setSelected(true);
		    }
		    else
		    {
			chkAutoShowPopItems.setSelected(false);
		    }

		    txtShowPopularItemsOfNDays.setText(rs.getString(193));

		    cmbPostMMSSalesEffectCostOrLoc.setSelectedItem(rs.getString(194));
		    cmbEffectOfSales.setSelectedItem(rs.getString(195));
		    if (rs.getString(196).equalsIgnoreCase("Y"))
		    {
			chkPOSWiseItemLinkeUpToMMSProduct.setSelected(true);
		    }
		    else
		    {
			chkPOSWiseItemLinkeUpToMMSProduct.setSelected(false);
		    }

		    if (rs.getString(197).equalsIgnoreCase("Y"))
		    {
			chkEnableMasterDiscount.setSelected(true);
		    }
		    else
		    {
			chkEnableMasterDiscount.setSelected(false);
		    }
		    if (rs.getString(198).equalsIgnoreCase("Y"))
		    {
			chkEnableNFCInterface.setSelected(true);
		    }
		    else
		    {
			chkEnableNFCInterface.setSelected(false);
		    }

		    if ("Y".equalsIgnoreCase(rs.getString(199)))
		    {
			cmbBenowPOSIntegrationYN.setSelectedIndex(1);
		    }

		    txtXEmail.setText(rs.getString(200));
		    txtMerchantCode.setText(rs.getString(201));
		    txtAuthenticationKey.setText(rs.getString(202));
		    txtSalt.setText(rs.getString(203));

		    if (rs.getString(204).equalsIgnoreCase("Y"))
		    {
			chkEnableLockTables.setSelected(true);
		    }
		    else
		    {
			chkEnableLockTables.setSelected(false);
		    }

		    String dineInAreaCodeForDirectBiller = rs.getString(56);
		    String homeDeliveryAreaCodeForDirectBiller = rs.getString(205);
		    String takeAwayAreaCodeForDirectBiller = rs.getString(206);

		    String dineInAreaNameForDirectBiller = mapAreaCodeWithName.get(dineInAreaCodeForDirectBiller);
		    String homeDeliveryAreaNameForDirectBiller = mapAreaCodeWithName.get(homeDeliveryAreaCodeForDirectBiller);
		    String takeAwayAreaNameForDirectBiller = mapAreaCodeWithName.get(takeAwayAreaCodeForDirectBiller);

		    cmbDineInAreaForDirectBiller.setSelectedItem(dineInAreaNameForDirectBiller);
		    cmbHomeDeliAreaForDirectBiller.setSelectedItem(homeDeliveryAreaNameForDirectBiller);
		    cmbTakeAwayAreaForDirectBiller.setSelectedItem(takeAwayAreaNameForDirectBiller);

		    String strRoundOffBillFinalAmt = rs.getString(207);
		    if (strRoundOffBillFinalAmt.equalsIgnoreCase("Y"))
		    {
			chkRoundOffBillAmount.setSelected(true);
		    }
		    else
		    {
			chkRoundOffBillAmount.setSelected(false);
		    }

		    String strNoOfDecimalPlace = rs.getString(208);
		    txtNoOfDecimalPlaces.setText(strNoOfDecimalPlace);

		    String sendDBBackupOnClientMail = rs.getString(209);
		    if (sendDBBackupOnClientMail.equalsIgnoreCase("Y"))
		    {
			chkSendDBBackupOnMail.setSelected(true);
		    }
		    else
		    {
			chkSendDBBackupOnMail.setSelected(false);
		    }

		    String strPrintOrderNoOnBillYN = rs.getString(210);
		    if (strPrintOrderNoOnBillYN.equalsIgnoreCase("Y"))
		    {
			chkPrintOrderNoOnBill.setSelected(true);
		    }
		    else
		    {
			chkSendDBBackupOnMail.setSelected(false);
		    }

		    String printDeviceAndUserDtlOnKOTYN = rs.getString(211);
		    if (printDeviceAndUserDtlOnKOTYN.equalsIgnoreCase("Y"))
		    {
			chkPrintDeviceUserDtlOnKOT.setSelected(true);
		    }
		    else
		    {
			chkPrintDeviceUserDtlOnKOT.setSelected(false);
		    }

		    String removeSCTaxCode = rs.getString(212);
		    String removeSCTaxName = mapTaxCodeWithName.get(removeSCTaxCode);
		    cmbRemoveServiceChargeTaxCode.setSelectedItem(removeSCTaxName);

		    String autoAddKOTToBill = rs.getString(213);
		    if (autoAddKOTToBill.equalsIgnoreCase("Y"))
		    {
			chkAutoAddKOTToBill.setSelected(true);
		    }
		    else
		    {
			chkAutoAddKOTToBill.setSelected(false);
		    }

		    String areaWiseCostCenterKOTPrinting = rs.getString(214);
		    if (areaWiseCostCenterKOTPrinting.equalsIgnoreCase("Y"))
		    {
			chkAreaWiseCostCenterKOTPrinting.setSelected(true);
		    }
		    else
		    {
			chkAreaWiseCostCenterKOTPrinting.setSelected(false);
		    }

		    String weraOnlineOrderIntegration = rs.getString(215);
		    if (weraOnlineOrderIntegration.equalsIgnoreCase("Y"))
		    {
			cmbWeraIntegrationYN.setSelectedItem("Yes");
		    }
		    else
		    {
			cmbWeraIntegrationYN.setSelectedItem("No");
		    }

		    txtWeraMerchantOutletId.setText(rs.getString(216));
		    txtWeraAuthenticationAPIKey.setText(rs.getString(217));

		    String fireCommunication = rs.getString(218);
		    if (fireCommunication.equalsIgnoreCase("Y"))
		    {
			chkFireCommunication.setSelected(true);
		    }
		    else
		    {
			chkFireCommunication.setSelected(false);
		    }

		    txtUSDCrrencyConverionRate.setText(rs.getString(219));
		    txtDBBackupReceiverEmailId.setText(rs.getString(220));

		    String printMoveTableMoveKOTYN = rs.getString(221);
		    if (printMoveTableMoveKOTYN.equalsIgnoreCase("Y"))
		    {
			chkPrintMoveTableMoveKOT.setSelected(true);
		    }
		    else
		    {
			chkPrintMoveTableMoveKOT.setSelected(false);
		    }

		    String printQtyTotal = rs.getString(222);
		    if (printQtyTotal.equalsIgnoreCase("Y"))
		    {
			chkPrintQtyTotal.setSelected(true);
		    }
		    else
		    {
			chkPrintQtyTotal.setSelected(false);
		    }

		}
		rs.close();
		dteEndChooser.setEnabled(false);

		//printer setup data
		funFillPrinterSetupData();

		//sms setup data
		funFillSMSSetupData();

	    }
	    else
	    {
		btnNew.setText("SAVE");
	    }
	}
	catch (Exception e)
	{

	    JOptionPane.showMessageDialog(null, "Structure Update Required");
	    e.printStackTrace();
	}
    }

    private void setData(Object[] data)
    {
	try
	{
	    btnNew.setText("UPDATE");
	    txtClientCode.setText(data[0].toString());
	    txtClientName.setText(data[1].toString());
	    txtShopAddressLine1.setText(data[2].toString());
	    txtShopAddressLine2.setText(data[3].toString());
	    txtShopAddressLine3.setText(data[4].toString());
	    txtEmailAddress.setText(data[5].toString());
	    txtBillFooter.setText(data[6].toString());

	    int count = cmbBillPaperSize.getItemCount();
	    String[] paperSize = new String[count];
	    for (int i = 0; i < count; i++)
	    {
		paperSize[i] = cmbBillPaperSize.getItemAt(i).toString();
	    }
	    fillCombo(paperSize, data[8].toString(), 4);
	    count = cmbPrintMode.getItemCount();
	    String[] paperM = new String[count];
	    for (int i = 0; i < count; i++)
	    {
		paperM[i] = cmbPrintMode.getItemAt(i).toString();
	    }

	    fillCombo(paperM, data[9].toString(), 5);
	    count = cmbState.getItemCount();
	    String[] state = new String[count];
	    for (int i = 0; i < count; i++)
	    {
		state[i] = cmbState.getItemAt(i).toString();
	    }

	    fillCombo(state, data[12].toString(), 2);
	    count = cmbCountry.getItemCount();
	    String[] country = new String[count];
	    for (int i = 0; i < count; i++)
	    {
		country[i] = cmbCountry.getItemAt(i).toString();
	    }

	    fillCombo(country, data[13].toString(), 3);
	    count = cmbCity.getItemCount();
	    String[] city = new String[count];
	    for (int i = 0; i < count; i++)
	    {
		city[i] = cmbCity.getItemAt(i).toString();
	    }

	    fillCombo(city, data[11].toString(), 1);
	    txtTelephone.setText(data[14].toString());
	    count = cmbNatureOfBusiness.getItemCount();
	    String[] NatureofBusiness = new String[count];
	    for (int i = 0; i < count; i++)
	    {
		NatureofBusiness[i] = cmbNatureOfBusiness.getItemAt(i).toString();
	    }
	    fillCombo(NatureofBusiness, data[17].toString(), 6);

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public void fillCombo(String[] array, String st, int val)
    {
	try
	{
	    if (val == 1)
	    {
		cmbCity.removeAllItems();
		cmbCity.addItem(st);
		for (int i = 0; i < array.length; i++)
		{
		    if (!array[i].equals(st))
		    {
			cmbCity.addItem(array[i]);
		    }
		}
	    }
	    else if (val == 2)
	    {
		cmbState.removeAllItems();
		cmbState.addItem(st);
		for (int i = 0; i < array.length; i++)
		{
		    if (!array[i].equals(st))
		    {
			cmbState.addItem(array[i]);
		    }
		}
	    }
	    else if (val == 3)
	    {
		cmbCountry.removeAllItems();
		cmbCountry.addItem(st);
		for (int i = 0; i < array.length; i++)
		{
		    if (!array[i].equals(st))
		    {
			cmbCountry.addItem(array[i]);
		    }
		}
	    }
	    else if (val == 4)
	    {
		cmbBillPaperSize.removeAllItems();
		cmbBillPaperSize.addItem(st);
		for (int i = 0; i < array.length; i++)
		{
		    if (!array[i].equals(st))
		    {
			cmbBillPaperSize.addItem(array[i]);
		    }
		}
	    }
	    else if (val == 5)
	    {
		cmbPrintMode.removeAllItems();
		cmbPrintMode.addItem(st);
		for (int i = 0; i < array.length; i++)
		{
		    if (!array[i].equals(st))
		    {
			cmbPrintMode.addItem(array[i]);
		    }
		}
	    }
	    else if (val == 6)
	    {
		cmbNatureOfBusiness.removeAllItems();
		cmbNatureOfBusiness.addItem(st);
		for (int i = 0; i < array.length; i++)
		{
		    if (!array[i].equals(st))
		    {
			cmbNatureOfBusiness.addItem(array[i]);
		    }
		}
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public void setShopCode(String text)
    {
	try
	{
	    txtClientCode.setText(text);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public void setShopName(String text)
    {
	try
	{
	    txtClientName.setText(text);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public void setAddress1(String text)
    {
	try
	{
	    txtShopAddressLine1.setText(text);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public void setAddress2(String text)
    {
	try
	{
	    txtShopAddressLine2.setText(text);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public void setAddress3(String text)
    {
	try
	{
	    txtShopAddressLine3.setText(text);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public void setEmailId(String text)
    {
	try
	{
	    txtEmailAddress.setText(text);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public void setBillFooter(String text)
    {
	try
	{
	    txtBillFooter.setText(text);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public void setTelephoneNo(String text)
    {
	try
	{
	    txtTelephone.setText(text);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public void setVatNo(String text)
    {
	try
	{
	    txtVatNo.setText(text);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public boolean funCheckRFIDValidations()
    {
	boolean flgRFID = true;
	if (txtServerName.getText().isEmpty())
	{
	    new frmOkPopUp(this, "Please Enter Server Name", "Error", 0).setVisible(true);
	    flgRFID = false;
	}
	else if (txtUserName.getText().isEmpty())
	{
	    new frmOkPopUp(this, "Please Enter User Name", "Error", 0).setVisible(true);
	    flgRFID = false;
	}
	else if (txtPassword.getText().isEmpty())
	{
	    new frmOkPopUp(this, "Please Enter Password", "Error", 0).setVisible(true);
	    flgRFID = false;
	}
	else if (txtDatabaseName.getText().isEmpty())
	{
	    new frmOkPopUp(this, "Please Enter DataBase Name", "Error", 0).setVisible(true);
	    flgRFID = false;
	}
	return flgRFID;
    }

    private void funCheckHomeDeliveryFields()
    {
	if (chkHomeDelSMS.isSelected())
	{
	    txtAreaSendHomeDeliverySMS.setEnabled(true);
	    btnShiftSendHomeDelievery.setEnabled(true);
	    cmbSendHomeDelivery.setEnabled(true);
	}
	else
	{
	    txtAreaSendHomeDeliverySMS.setEnabled(false);
	    btnShiftSendHomeDelievery.setEnabled(false);
	    cmbSendHomeDelivery.setEnabled(false);
	}
    }

    private void funCheckBillSettlementFields()
    {
	if (chkBillSettlementSMS.isSelected())
	{
	    txtAreaBillSettlementSMS.setEnabled(true);
	    btnShiftBillSettlement.setEnabled(true);
	    cmbBillSettlement.setEnabled(true);
	}
	else
	{
	    txtAreaBillSettlementSMS.setEnabled(false);
	    btnShiftBillSettlement.setEnabled(false);
	    cmbBillSettlement.setEnabled(false);
	}
    }

    private void funCheckCRMInterface()
    {
	funEnableLoyaltyOptions(true);
    }

    private void funEnableLoyaltyOptions(boolean flag)
    {
	if (cmbCRMType.getSelectedItem().toString().equals("SQY CRM Interface"))
	{
	    lblGetWebServiceURL.setText("SQY WebService URL(GET)  :");
	    lblOutletUID.setText("SQY Outlet UID         :");
	    lblPOSID.setText("SQY POS ID              :");
	    txtGetWebservice.setEnabled(flag);
	    txtPostWebservice.setEnabled(flag);
	    txtOutletUID.setEnabled(flag);
	    txtPOSID.setEnabled(flag);
	}
	else if (cmbCRMType.getSelectedItem().toString().equals("PMAM CRM Interface"))
	{
	    lblGetWebServiceURL.setText("PMAM WebService URL(GET)  :");
	    lblOutletUID.setText("User Token         :");
	    lblPOSID.setText("Access Token       :");
	    txtGetWebservice.setEnabled(flag);
	    txtPostWebservice.setEnabled(false);
	    txtOutletUID.setEnabled(true);
	    txtPOSID.setEnabled(true);
	}
	else if (cmbCRMType.getSelectedItem().toString().equalsIgnoreCase("HASH TAG CRM Interface"))
	{
	    lblGetWebServiceURL.setText("Web Service URL           :");
	    txtGetWebservice.setEnabled(true);
	    //txtGetWebservice.setText("http://app.hashtagloyalty.in/pos/api/v1/sanguine");

	    txtPostWebservice.setEnabled(false);

	    lblOutletUID.setText("User Token         :");
	    txtOutletUID.setEnabled(true);
	    //txtOutletUID.setText("vG3j9ozSKwXSe1G3kfCX");

	    lblPOSID.setText("Access Token       :");
	    txtPOSID.setEnabled(false);
	}
	else
	{
	    txtGetWebservice.setEnabled(false);
	    txtPostWebservice.setEnabled(false);
	    txtOutletUID.setEnabled(false);
	    txtPOSID.setEnabled(false);
	}

    }

    public void funSetDatabaseCredentials(boolean flag)
    {
	if (!flag)
	{
	    txtServerName.setText("");
	    txtUserName.setText("");
	    txtPassword.setText("");
	    txtDatabaseName.setText("");
	}
	else
	{
	    txtServerName.requestFocus();
	}
	txtServerName.setEnabled(flag);
	txtUserName.setEnabled(flag);
	txtPassword.setEnabled(flag);
	txtDatabaseName.setEnabled(flag);
    }

    private boolean funCheckInt(String text)
    {
	boolean flg = false;
	try
	{
	    int no = Integer.parseInt(text);
	    flg = true;
	}
	catch (NumberFormatException numEx)
	{
	    flg = false;
	}
	finally
	{
	    return flg;
	}
    }

    private void funBrowseImagePath()
    {
	JFileChooser jfc = new JFileChooser();
	if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
	{
	    reportImageFile = jfc.getSelectedFile();
	    String imagePath = reportImageFile.getAbsolutePath();
	    //txtReportImagePath.setText(reportImageFile.getAbsolutePath());            
	}
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelHeader = new javax.swing.JPanel();
        lblProductName = new javax.swing.JLabel();
        lblModuleName = new javax.swing.JLabel();
        lblformName = new javax.swing.JLabel();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 32767));
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        lblPosName = new javax.swing.JLabel();
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        lblUserCode = new javax.swing.JLabel();
        lblDate = new javax.swing.JLabel();
        lblHOSign = new javax.swing.JLabel();
        panelMainForm = new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSGlobal/images/imgBGJPOS.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  ;
        panelFormBody = new javax.swing.JPanel();
        tabbedPane = new javax.swing.JTabbedPane();
        panelClientSetup = new javax.swing.JPanel();
        lblShopCode = new javax.swing.JLabel();
        txtClientCode = new javax.swing.JTextField();
        txtClientName = new javax.swing.JTextField();
        lblShopAddress1 = new javax.swing.JLabel();
        lblShopAddress2 = new javax.swing.JLabel();
        txtShopAddressLine2 = new javax.swing.JTextField();
        txtShopAddressLine1 = new javax.swing.JTextField();
        lblShopAddress3 = new javax.swing.JLabel();
        txtShopAddressLine3 = new javax.swing.JTextField();
        lblEmail = new javax.swing.JLabel();
        txtEmailAddress = new javax.swing.JTextField();
        lblCityPin = new javax.swing.JLabel();
        cmbCity = new javax.swing.JComboBox();
        lblStateCountry = new javax.swing.JLabel();
        cmbState = new javax.swing.JComboBox();
        cmbCountry = new javax.swing.JComboBox();
        lblTelePhoneFax = new javax.swing.JLabel();
        txtTelephone = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        lblNatureOfBusiness = new javax.swing.JLabel();
        cmbNatureOfBusiness = new javax.swing.JComboBox();
        txtPincode = new javax.swing.JTextField();
        btnBrowseImagePath1 = new javax.swing.JButton();
        lblReportImageIcon = new javax.swing.JLabel();
        panelBillSetup = new javax.swing.JPanel();
        lblBillpaperSize = new javax.swing.JLabel();
        lblPrintMode = new javax.swing.JLabel();
        lblFooter = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtBillFooter = new javax.swing.JTextArea();
        cmbBillPaperSize = new javax.swing.JComboBox();
        cmbPrintMode = new javax.swing.JComboBox();
        chkNegBilling = new javax.swing.JCheckBox();
        chkDayEnd = new javax.swing.JCheckBox();
        chkMultiBillPrint = new javax.swing.JCheckBox();
        chkEnableKOT = new javax.swing.JCheckBox();
        chkEffectOnPSP = new javax.swing.JCheckBox();
        chkPrintVatNo = new javax.swing.JCheckBox();
        txtVatNo = new javax.swing.JTextField();
        chkShowBill = new javax.swing.JCheckBox();
        chkServiceTaxNo = new javax.swing.JCheckBox();
        txtServiceTaxno = new javax.swing.JTextField();
        chkManualBillNo = new javax.swing.JCheckBox();
        lblColSize = new javax.swing.JLabel();
        chkPrintKotForDirectBiller = new javax.swing.JCheckBox();
        lblReportType = new javax.swing.JLabel();
        cmbColumnSize = new javax.swing.JComboBox();
        cmbPrintType = new javax.swing.JComboBox();
        lblNoOfAdvReceiptPrint = new javax.swing.JLabel();
        txtAdvRecPrintCount = new javax.swing.JTextField();
        lblBillFormat = new javax.swing.JLabel();
        cmbBillFormatType = new javax.swing.JComboBox();
        chkPrintShortNameOnKOT = new javax.swing.JCheckBox();
        lblNoOfLinesInKOTPrint = new javax.swing.JLabel();
        txtNoOfLinesInKOTPrint = new javax.swing.JTextField();
        chkMultiKOTPrint = new javax.swing.JCheckBox();
        lblShowBillsDtlType = new javax.swing.JLabel();
        cmbShowBillsDtlType = new javax.swing.JComboBox();
        chkPrintInvoiceOnBill = new javax.swing.JCheckBox();
        chkPrintInclusiveOfAllTaxesOnBill = new javax.swing.JCheckBox();
        chkPrintTDHItemsInBill = new javax.swing.JCheckBox();
        panelPOSSetup1 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        strPOSType = new javax.swing.JComboBox();
        cmbDataSendFrequency = new javax.swing.JComboBox();
        txtWebServiceLink = new javax.swing.JTextField();
        lblChangeTheme = new javax.swing.JLabel();
        cmbChangeTheme = new javax.swing.JComboBox();
        txtMaxDiscount = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        chkEditHomeDelivery = new javax.swing.JCheckBox();
        chkSlabBasedHomeDelCharges = new javax.swing.JCheckBox();
        chkSkip_Waiter_Selection = new javax.swing.JCheckBox();
        chkSkip_pax_selection = new javax.swing.JCheckBox();
        cmbStockInOption = new javax.swing.JComboBox();
        lblStkInOptions = new javax.swing.JLabel();
        lblMaxDiscount = new javax.swing.JLabel();
        txtCustSeries = new javax.swing.JTextField();
        chkActivePromotions = new javax.swing.JCheckBox();
        chkPrintForVoidBill = new javax.swing.JCheckBox();
        chkPostSalesDataToMMS = new javax.swing.JCheckBox();
        chkAreaMasterCompulsory = new javax.swing.JCheckBox();
        jLabel19 = new javax.swing.JLabel();
        cmbPriceFrom = new javax.swing.JComboBox();
        chkPrinterErrorMessage = new javax.swing.JCheckBox();
        chkChangeQtyForExternalCode = new javax.swing.JCheckBox();
        chkPrintKOTYN = new javax.swing.JCheckBox();
        lblPrintKOTYN = new javax.swing.JLabel();
        chkShowItemStkColumnInDB = new javax.swing.JCheckBox();
        cmbItemType = new javax.swing.JComboBox();
        lblApplyDiscountOn = new javax.swing.JLabel();
        cmbApplyDiscountOn = new javax.swing.JComboBox();
        jLabel17 = new javax.swing.JLabel();
        chkPrintBill = new javax.swing.JCheckBox();
        jLabel18 = new javax.swing.JLabel();
        chkUseVatAndServiceNoFromPos = new javax.swing.JCheckBox();
        dteHOServerDate = new com.toedter.calendar.JDateChooser();
        lblHOServerDate = new javax.swing.JLabel();
        btnTestWebService = new javax.swing.JButton();
        lblPostMMSSalesEffect = new javax.swing.JLabel();
        cmbPostMMSSalesEffectCostOrLoc = new javax.swing.JComboBox();
        chkPOSWiseItemLinkeUpToMMSProduct = new javax.swing.JCheckBox();
        chkEnableMasterDiscount = new javax.swing.JCheckBox();
        chkEnableLockTables = new javax.swing.JCheckBox();
        panelPOSSetup2 = new javax.swing.JPanel();
        chkPrintManualAdvOrderOnBill = new javax.swing.JCheckBox();
        chkManualAdvOrderCompulsory = new javax.swing.JCheckBox();
        chkPrintModifierQtyOnKOT = new javax.swing.JCheckBox();
        lblMenuItemDisplaySeq = new javax.swing.JLabel();
        cmbMenuItemDisSeq = new javax.swing.JComboBox();
        cmbMenuItemSortingOn = new javax.swing.JComboBox();
        lblMenuItemSorting = new javax.swing.JLabel();
        chkItemQtyNumpad = new javax.swing.JCheckBox();
        chkPrintKOTToLocalPrinter = new javax.swing.JCheckBox();
        chkEnableSettleBtnForDirectBillerBill = new javax.swing.JCheckBox();
        chkDelBoyCompulsoryOnDirectBiller = new javax.swing.JCheckBox();
        lblDontShowAdvOrderInOtherPOS = new javax.swing.JLabel();
        chkDontShowAdvOrderInOtherPOS = new javax.swing.JCheckBox();
        lblPrintZeroAmtModifierInBill = new javax.swing.JLabel();
        chkPrintZeroAmtModifierInBill = new javax.swing.JCheckBox();
        chkPointsOnBillPrint = new javax.swing.JCheckBox();
        lblSlipNoForCreditCardBillYN = new javax.swing.JLabel();
        chkSlipNoForCreditCardBillYN = new javax.swing.JCheckBox();
        lblExpDateForCreditCardBillYN = new javax.swing.JLabel();
        chkExpDateForCreditCardBillYN = new javax.swing.JCheckBox();
        lblSelectWaiterFromCardSwipe = new javax.swing.JLabel();
        chkSelectWaiterFromCardSwipe = new javax.swing.JCheckBox();
        lblMultiWaiterSelection = new javax.swing.JLabel();
        chkMultipleWaiterSelectionOnMakeKOT = new javax.swing.JCheckBox();
        lblMoveTableToOtherPOS = new javax.swing.JLabel();
        chkMoveTableToOtherPOS = new javax.swing.JCheckBox();
        chkMoveKOTToOtherPOS = new javax.swing.JCheckBox();
        lblMoveKOTToOtherPOS1 = new javax.swing.JLabel();
        lblCalculateTaxOnMakeKOT = new javax.swing.JLabel();
        chkCalculateTaxOnMakeKOT = new javax.swing.JCheckBox();
        lblCalculateDiscItemWise = new javax.swing.JLabel();
        chkCalculateDiscItemWise = new javax.swing.JCheckBox();
        chkTakewayCustomerSelection = new javax.swing.JCheckBox();
        chkBoxAllowNewAreaMasterFromCustMaster = new javax.swing.JCheckBox();
        lblSelectCustAddressForBill = new javax.swing.JLabel();
        chkSelectCustAddressForBill = new javax.swing.JCheckBox();
        chkGenrateMI = new javax.swing.JCheckBox();
        chkAllowToCalculateItemWeight = new javax.swing.JCheckBox();
        chkItemWiseKOTPrintYN = new javax.swing.JCheckBox();
        chkShowPurchaseRateInDirectBiller = new javax.swing.JCheckBox();
        lblItemWiseKOTYN = new javax.swing.JLabel();
        chkAreaWIsePromotions = new javax.swing.JCheckBox();
        chkTableReservationForCustomer = new javax.swing.JCheckBox();
        chkPrintHomeDeliveryYN = new javax.swing.JCheckBox();
        chkScanQRYN = new javax.swing.JCheckBox();
        cmbEffectOfSales = new javax.swing.JComboBox();
        lblEffectOfSales = new javax.swing.JLabel();
        panelPOSSetup3 = new javax.swing.JPanel();
        chkPopUpToApplyPromotionsOnBill = new javax.swing.JCheckBox();
        lblDebitCardBalChkOnTrans = new javax.swing.JLabel();
        chkCheckDebitCardBalOnTrans = new javax.swing.JCheckBox();
        lblPickSettlementsFromPOSMaster = new javax.swing.JLabel();
        chkSettlementsFromPOSMaster = new javax.swing.JCheckBox();
        lblEnableShift = new javax.swing.JLabel();
        chkShiftWiseDayEnd = new javax.swing.JCheckBox();
        lblProductionLinkUp = new javax.swing.JLabel();
        chkProductionLinkup = new javax.swing.JCheckBox();
        lblLockDataOnShift = new javax.swing.JLabel();
        chkLockDataOnShift = new javax.swing.JCheckBox();
        lblWSClientCode = new javax.swing.JLabel();
        txtWSClientCode = new javax.swing.JTextField();
        lblEnableBillSeries = new javax.swing.JLabel();
        chkEnableBillSeries = new javax.swing.JCheckBox();
        lblEnablePMSIntegration = new javax.swing.JLabel();
        chkEnablePMSIntegration = new javax.swing.JCheckBox();
        lblPrintRemarkAndReasonForReprint = new javax.swing.JLabel();
        chkPrintRemarkAndReasonForReprint = new javax.swing.JCheckBox();
        lblShowItemDtlsForChangeCustomerOnBill = new javax.swing.JLabel();
        chkShowItemDtlsForChangeCustomerOnBill = new javax.swing.JCheckBox();
        lblDaysBeforeOrderToCancel = new javax.swing.JLabel();
        txtDaysBeforeOrderToCancel = new javax.swing.JTextField();
        lblDaysBeforeOrderToCancel1 = new javax.swing.JLabel();
        txtNoOfDelDaysForAdvOrder = new javax.swing.JTextField();
        lblDaysBeforeOrderToCancel2 = new javax.swing.JLabel();
        txtNoOfDelDaysForUrgentOrder = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        cmbHours = new javax.swing.JComboBox();
        cmbMinutes = new javax.swing.JComboBox();
        jLabel22 = new javax.swing.JLabel();
        cmbHoursUrgentOrder = new javax.swing.JComboBox();
        cmbMinutesUrgentOrder = new javax.swing.JComboBox();
        lbSetUpToTimeForAdvOrder = new javax.swing.JLabel();
        chkSetUpToTimeForAdvOrder = new javax.swing.JCheckBox();
        chkSetUpToTimeForUrgentOrder = new javax.swing.JCheckBox();
        lbSetUpToTimeForAdvOrder1 = new javax.swing.JLabel();
        cmbAMPMUrgent = new javax.swing.JComboBox();
        cmbAMPM = new javax.swing.JComboBox();
        lblEnableBothPrintAndSettleBtnForDB = new javax.swing.JLabel();
        chkEnableBothPrintAndSettleBtnForDB = new javax.swing.JCheckBox();
        lblCarryFwdFloatAmt = new javax.swing.JLabel();
        chkCarryForwardFloatAmtToNextDay = new javax.swing.JCheckBox();
        lblOpenCashDrawerAfterBillPrint = new javax.swing.JLabel();
        chkOpenCashDrawerAfterBillPrint = new javax.swing.JCheckBox();
        chkPropertyWiseSalesOrder = new javax.swing.JCheckBox();
        lblPropertyWiseSalesOrder = new javax.swing.JLabel();
        lblPrintTimeOnBill = new javax.swing.JLabel();
        chkPrintTimeOnBill = new javax.swing.JCheckBox();
        lblShowPopUpForNextItemQuantity = new javax.swing.JLabel();
        chkShowPopUpForNextItemQuantity = new javax.swing.JCheckBox();
        lblNewBillSeriesForNewDay = new javax.swing.JLabel();
        chkNewBillSeriesForNewDay = new javax.swing.JCheckBox();
        lbShowOnlyLoginPOSReports = new javax.swing.JLabel();
        chkShowOnlyLoginPOSReports = new javax.swing.JCheckBox();
        lblEnabledDineIn = new javax.swing.JLabel();
        chkEnableDineIn = new javax.swing.JCheckBox();
        chkAutoAreaSelectionInMakeKOT = new javax.swing.JCheckBox();
        lblRoundingOffTo = new javax.swing.JLabel();
        txtRoundingOffTo = new javax.swing.JTextField();
        chkShowUnSettlementForm = new javax.swing.JCheckBox();
        chkPrintOpenItemsOnBill = new javax.swing.JCheckBox();
        lblDays = new javax.swing.JLabel();
        txtShowPopularItemsOfNDays = new javax.swing.JTextField();
        lblAutoShowPopularItemsOf = new javax.swing.JLabel();
        chkAutoShowPopItems = new javax.swing.JCheckBox();
        panelPOSSetup4 = new javax.swing.JPanel();
        chkAreaWisePricing = new javax.swing.JCheckBox();
        lblDineInAreaForDirectBiller = new javax.swing.JLabel();
        cmbDineInAreaForDirectBiller = new javax.swing.JComboBox();
        lblHomeDeliAreaForDirectBiller = new javax.swing.JLabel();
        cmbHomeDeliAreaForDirectBiller = new javax.swing.JComboBox();
        lblTakeAwayAreaForDirectBiller = new javax.swing.JLabel();
        cmbTakeAwayAreaForDirectBiller = new javax.swing.JComboBox();
        chkRoundOffBillAmount = new javax.swing.JCheckBox();
        txtNoOfDecimalPlaces = new javax.swing.JTextField();
        lblNoodDecimalPlaces = new javax.swing.JLabel();
        chkSendDBBackupOnMail = new javax.swing.JCheckBox();
        chkPrintOrderNoOnBill = new javax.swing.JCheckBox();
        chkPrintDeviceUserDtlOnKOT = new javax.swing.JCheckBox();
        cmbRemoveServiceChargeTaxCode = new javax.swing.JComboBox();
        lblRemoveServiceChargeTaxCode = new javax.swing.JLabel();
        chkAutoAddKOTToBill = new javax.swing.JCheckBox();
        chkAreaWiseCostCenterKOTPrinting = new javax.swing.JCheckBox();
        chkFireCommunication = new javax.swing.JCheckBox();
        lblUSDCrrencyConverionRate = new javax.swing.JLabel();
        txtUSDCrrencyConverionRate = new javax.swing.JTextField();
        chkPrintItemsOnMoveKOTMoveTable = new javax.swing.JCheckBox();
        chkPrintMoveTableMoveKOT = new javax.swing.JCheckBox();
        chkPrintQtyTotal = new javax.swing.JCheckBox();
        panelEmailSetup = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtSenderEmailId = new javax.swing.JTextField();
        txtEmailPassword = new javax.swing.JPasswordField();
        txtConfirmEmailPassword = new javax.swing.JPasswordField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtBodyPart = new javax.swing.JTextArea();
        jLabel9 = new javax.swing.JLabel();
        cmbServerName = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        lblReceiverEmailId = new javax.swing.JLabel();
        txtReceiverEmailId = new javax.swing.JTextField();
        btnTestEmail = new javax.swing.JButton();
        lblDBBackupReceiverEmailId = new javax.swing.JLabel();
        txtDBBackupReceiverEmailId = new javax.swing.JTextField();
        panelRFIDSetup = new javax.swing.JPanel();
        lblRFIDYN = new javax.swing.JLabel();
        lblServerName = new javax.swing.JLabel();
        lblUserName = new javax.swing.JLabel();
        cmbRFIDSetup = new javax.swing.JComboBox();
        txtServerName = new javax.swing.JTextField();
        txtUserName = new javax.swing.JTextField();
        lblPassword = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        lblDBName = new javax.swing.JLabel();
        txtDatabaseName = new javax.swing.JTextField();
        lblCardIntfType = new javax.swing.JLabel();
        cmbCardIntfType = new javax.swing.JComboBox();
        panelPoints = new javax.swing.JPanel();
        txtGetWebservice = new javax.swing.JTextField();
        lblGetWebServiceURL = new javax.swing.JLabel();
        lblPostWebServiceURL = new javax.swing.JLabel();
        txtPostWebservice = new javax.swing.JTextField();
        lblOutletUID = new javax.swing.JLabel();
        txtOutletUID = new javax.swing.JTextField();
        txtPOSID = new javax.swing.JTextField();
        lblPOSID = new javax.swing.JLabel();
        cmbCRMType = new javax.swing.JComboBox();
        lblCRMInterface = new javax.swing.JLabel();
        panelSMSSetup = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtAreaSMSApi = new javax.swing.JTextArea();
        lblSMSApi = new javax.swing.JLabel();
        lblSendHomeDelivery = new javax.swing.JLabel();
        cmbSendHomeDelivery = new javax.swing.JComboBox();
        btnShiftSendHomeDelievery = new javax.swing.JButton();
        lblBillSettlementSMS = new javax.swing.JLabel();
        cmbBillSettlement = new javax.swing.JComboBox();
        btnShiftBillSettlement = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtAreaSendHomeDeliverySMS = new javax.swing.JTextArea();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtAreaBillSettlementSMS = new javax.swing.JTextArea();
        chkHomeDelSMS = new javax.swing.JCheckBox();
        chkBillSettlementSMS = new javax.swing.JCheckBox();
        cmbSMSType = new javax.swing.JComboBox();
        lblSMSType = new javax.swing.JLabel();
        txtSMSMobileNo = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        lblSMSApi1 = new javax.swing.JLabel();
        chkDayEndSMSYN = new javax.swing.JCheckBox();
        chkVoidKOTSMSYN = new javax.swing.JCheckBox();
        chkNCKOTSMSYN = new javax.swing.JCheckBox();
        chkVoidBillSMSYN = new javax.swing.JCheckBox();
        chkModifyBillSMSYN = new javax.swing.JCheckBox();
        chkUnsettleBillSMSYN = new javax.swing.JCheckBox();
        chkComplementaryBillSMSYN = new javax.swing.JCheckBox();
        chkVoidAdvOrderSMSYN = new javax.swing.JCheckBox();
        lblAuditSMS = new javax.swing.JLabel();
        panelFTPSetup = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtFTPAddress = new javax.swing.JTextField();
        lblFTPServerUserName = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtFTPServerPass = new javax.swing.JPasswordField();
        txtFTPServerUserName = new javax.swing.JTextField();
        panelCMSIntegration = new javax.swing.JPanel();
        cmbCMSIntegrationYN = new javax.swing.JComboBox();
        txtCMSWesServiceURL = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        chkMemberAsTable = new javax.swing.JCheckBox();
        chkMemberCodeForKOTJPOS = new javax.swing.JCheckBox();
        chkMemberCodeForKOTMPOS = new javax.swing.JCheckBox();
        jLabel14 = new javax.swing.JLabel();
        chkMemberCodeForKotInMposByCardSwipe = new javax.swing.JCheckBox();
        jLabel16 = new javax.swing.JLabel();
        chkMemberCodeForMakeBillInMPOS = new javax.swing.JCheckBox();
        lblCMSPosting = new javax.swing.JLabel();
        cmbCMSPostingType = new javax.swing.JComboBox();
        jLabel20 = new javax.swing.JLabel();
        chkSelectCustomerCodeFromCardSwipe = new javax.swing.JCheckBox();
        panelPrinterSetup = new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSGlobal/images/imgBGJPOS.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  ;
        printerSetupScrollPane = new javax.swing.JScrollPane();
        tblPrinterSetup = new javax.swing.JTable();
        lblConsolidatedKOTPrinterPort = new javax.swing.JLabel();
        cmbConsolidatedKOTPrinterPort = new javax.swing.JComboBox();
        txtConsolidatedKOTPrinterPort = new javax.swing.JTextField();
        btnTestConsolidatedKOTPrinterPort = new javax.swing.JButton();
        panelDebitCardSetup = new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSGlobal/images/imgBGJPOS.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  ;
        lblLastPOSForDayEnd = new javax.swing.JLabel();
        cmbPOSForDayEnd = new javax.swing.JComboBox();
        chkEnableNFCInterface = new javax.swing.JCheckBox();
        panelInrestoIntegration = new javax.swing.JPanel();
        cmbInrestoPOSIntegrationYN = new javax.swing.JComboBox();
        txtInrestoPOSWesServiceURL = new javax.swing.JTextField();
        lblInrestoIntegration = new javax.swing.JLabel();
        lblInrestoWebServiceURL = new javax.swing.JLabel();
        lblInrestoPOSId = new javax.swing.JLabel();
        lblInrestoPOSKey = new javax.swing.JLabel();
        txtInrestoPOSId = new javax.swing.JTextField();
        txtInrestoPOSKey = new javax.swing.JTextField();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        cmbJioPOSIntegrationYN = new javax.swing.JComboBox();
        lblInrestoIntegration1 = new javax.swing.JLabel();
        lblInrestoWebServiceURL1 = new javax.swing.JLabel();
        txtJioPOSWesServiceURL = new javax.swing.JTextField();
        lblJioMoneyTID = new javax.swing.JLabel();
        lblJioMoneyMID = new javax.swing.JLabel();
        txtJioMoneyTID = new javax.swing.JTextField();
        txtJioMoneyMID = new javax.swing.JTextField();
        txtJioActivationCode = new javax.swing.JTextField();
        lblJioActivationCode = new javax.swing.JLabel();
        lblJioMoneyDeviceID = new javax.swing.JLabel();
        txtJioDeviceID = new javax.swing.JTextField();
        btnFetch = new javax.swing.JButton();
        jLayeredPane2 = new javax.swing.JLayeredPane();
        lbBenowIntegration = new javax.swing.JLabel();
        cmbBenowPOSIntegrationYN = new javax.swing.JComboBox();
        lbXEmail = new javax.swing.JLabel();
        txtXEmail = new javax.swing.JTextField();
        lblMerchantCode = new javax.swing.JLabel();
        txtMerchantCode = new javax.swing.JTextField();
        lblAuthenticationKey = new javax.swing.JLabel();
        txtAuthenticationKey = new javax.swing.JTextField();
        lblSalt = new javax.swing.JLabel();
        txtSalt = new javax.swing.JTextField();
        lblSuperMerchantCode = new javax.swing.JLabel();
        txtSuperMerchantCode = new javax.swing.JTextField();
        btnAuthorize = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();
        txtOTP = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        lblWeraIntegration = new javax.swing.JLabel();
        cmbWeraIntegrationYN = new javax.swing.JComboBox();
        lblWeraMerchantCode = new javax.swing.JLabel();
        txtWeraMerchantOutletId = new javax.swing.JTextField();
        lblAuthenticationAPIKey = new javax.swing.JLabel();
        txtWeraAuthenticationAPIKey = new javax.swing.JTextField();
        btnNew = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        lblPOS = new javax.swing.JLabel();
        cmbPOS = new javax.swing.JComboBox();
        btnLikePOS = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setMinimumSize(new java.awt.Dimension(800, 600));
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        panelHeader.setBackground(new java.awt.Color(69, 164, 238));
        panelHeader.setLayout(new javax.swing.BoxLayout(panelHeader, javax.swing.BoxLayout.LINE_AXIS));

        lblProductName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblProductName.setForeground(new java.awt.Color(255, 255, 255));
        lblProductName.setText("SPOS - ");
        panelHeader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        lblModuleName.setText("Module Name");
        panelHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("- Property Setup");
        panelHeader.add(lblformName);
        panelHeader.add(filler4);
        panelHeader.add(filler5);

        lblPosName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPosName.setForeground(new java.awt.Color(255, 255, 255));
        lblPosName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPosName.setMaximumSize(new java.awt.Dimension(321, 30));
        lblPosName.setMinimumSize(new java.awt.Dimension(321, 30));
        lblPosName.setPreferredSize(new java.awt.Dimension(321, 30));
        panelHeader.add(lblPosName);
        panelHeader.add(filler6);

        lblUserCode.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblUserCode.setForeground(new java.awt.Color(255, 255, 255));
        lblUserCode.setMaximumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setMinimumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setPreferredSize(new java.awt.Dimension(90, 30));
        panelHeader.add(lblUserCode);

        lblDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDate.setForeground(new java.awt.Color(255, 255, 255));
        lblDate.setMaximumSize(new java.awt.Dimension(192, 30));
        lblDate.setMinimumSize(new java.awt.Dimension(192, 30));
        lblDate.setPreferredSize(new java.awt.Dimension(192, 30));
        panelHeader.add(lblDate);

        lblHOSign.setMaximumSize(new java.awt.Dimension(34, 30));
        lblHOSign.setMinimumSize(new java.awt.Dimension(34, 30));
        lblHOSign.setPreferredSize(new java.awt.Dimension(34, 30));
        panelHeader.add(lblHOSign);

        getContentPane().add(panelHeader, java.awt.BorderLayout.PAGE_START);

        panelMainForm.setOpaque(false);
        panelMainForm.setLayout(new java.awt.GridBagLayout());

        panelFormBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelFormBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelFormBody.setOpaque(false);

        tabbedPane.setBackground(new java.awt.Color(51, 102, 255));
        tabbedPane.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        tabbedPane.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        tabbedPane.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabbedPaneStateChanged(evt);
            }
        });

        panelClientSetup.setBackground(new java.awt.Color(153, 204, 255));
        panelClientSetup.setOpaque(false);
        panelClientSetup.setLayout(null);

        lblShopCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblShopCode.setText("Client Code & Name");
        panelClientSetup.add(lblShopCode);
        lblShopCode.setBounds(10, 15, 113, 29);

        txtClientCode.setEnabled(false);
        txtClientCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtClientCodeMouseClicked(evt);
            }
        });
        panelClientSetup.add(txtClientCode);
        txtClientCode.setBounds(200, 10, 87, 34);

        txtClientName.setEditable(false);
        txtClientName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtClientNameMouseClicked(evt);
            }
        });
        panelClientSetup.add(txtClientName);
        txtClientName.setBounds(290, 10, 230, 34);

        lblShopAddress1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblShopAddress1.setText("Address Line 1");
        panelClientSetup.add(lblShopAddress1);
        lblShopAddress1.setBounds(10, 60, 168, 29);

        lblShopAddress2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblShopAddress2.setText("Address Line 2");
        panelClientSetup.add(lblShopAddress2);
        lblShopAddress2.setBounds(10, 100, 189, 30);

        txtShopAddressLine2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtShopAddressLine2MouseClicked(evt);
            }
        });
        panelClientSetup.add(txtShopAddressLine2);
        txtShopAddressLine2.setBounds(200, 100, 321, 34);

        txtShopAddressLine1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtShopAddressLine1MouseClicked(evt);
            }
        });
        panelClientSetup.add(txtShopAddressLine1);
        txtShopAddressLine1.setBounds(200, 60, 321, 34);

        lblShopAddress3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblShopAddress3.setText("Address Line 3");
        panelClientSetup.add(lblShopAddress3);
        lblShopAddress3.setBounds(10, 140, 189, 40);

        txtShopAddressLine3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtShopAddressLine3MouseClicked(evt);
            }
        });
        panelClientSetup.add(txtShopAddressLine3);
        txtShopAddressLine3.setBounds(200, 140, 321, 34);

        lblEmail.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblEmail.setText("Email Address");
        panelClientSetup.add(lblEmail);
        lblEmail.setBounds(10, 350, 113, 29);

        txtEmailAddress.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtEmailAddressMouseClicked(evt);
            }
        });
        panelClientSetup.add(txtEmailAddress);
        txtEmailAddress.setBounds(200, 350, 320, 34);

        lblCityPin.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCityPin.setText("City & Pin Code ");
        panelClientSetup.add(lblCityPin);
        lblCityPin.setBounds(10, 200, 127, 32);

        cmbCity.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        cmbCity.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pune", "Secunderabad", "Hyderabad", "Delhi", "Gurgaon", "Mumbai", "Indore", "Nashik", "Nagpur", "Satara", "Ahmedabad", "Bangalore", "Chennai", "Chandigad", "Rajkot", "Gandhinagar", "Thane", "Vashi", "Kalyan", "Seawood", "Muscat", "Salalah" }));
        panelClientSetup.add(cmbCity);
        cmbCity.setBounds(200, 200, 190, 35);

        lblStateCountry.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblStateCountry.setText("State & Country");
        panelClientSetup.add(lblStateCountry);
        lblStateCountry.setBounds(10, 250, 127, 27);

        cmbState.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        cmbState.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Maharashtra", "Delhi", "Karnataka", "Telangana", "TamilNadu", "Gujrat", "Punjab", "Rajasthan", "Muscat", "Salalah" }));
        panelClientSetup.add(cmbState);
        cmbState.setBounds(200, 250, 190, 35);

        cmbCountry.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        cmbCountry.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "India", "China", "USA", "England", "Oman" }));
        panelClientSetup.add(cmbCountry);
        cmbCountry.setBounds(400, 250, 120, 35);

        lblTelePhoneFax.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTelePhoneFax.setText("Telephone");
        panelClientSetup.add(lblTelePhoneFax);
        lblTelePhoneFax.setBounds(10, 300, 127, 29);

        txtTelephone.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtTelephoneMouseClicked(evt);
            }
        });
        panelClientSetup.add(txtTelephone);
        txtTelephone.setBounds(200, 300, 320, 35);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("End Date");
        panelClientSetup.add(jLabel1);
        jLabel1.setBounds(10, 400, 103, 35);

        lblNatureOfBusiness.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblNatureOfBusiness.setText("Nature Of Business");
        panelClientSetup.add(lblNatureOfBusiness);
        lblNatureOfBusiness.setBounds(10, 450, 150, 30);

        cmbNatureOfBusiness.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Retail", "F&B" }));
        cmbNatureOfBusiness.setPreferredSize(new java.awt.Dimension(75, 35));
        panelClientSetup.add(cmbNatureOfBusiness);
        cmbNatureOfBusiness.setBounds(200, 450, 190, 35);

        txtPincode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPincodeMouseClicked(evt);
            }
        });
        panelClientSetup.add(txtPincode);
        txtPincode.setBounds(400, 200, 120, 34);

        btnBrowseImagePath1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnBrowseImagePath1.setForeground(new java.awt.Color(255, 255, 255));
        btnBrowseImagePath1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn1.png"))); // NOI18N
        btnBrowseImagePath1.setText("BROWSE");
        btnBrowseImagePath1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBrowseImagePath1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn2.png"))); // NOI18N
        btnBrowseImagePath1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBrowseImagePath1MouseClicked(evt);
            }
        });
        panelClientSetup.add(btnBrowseImagePath1);
        btnBrowseImagePath1.setBounds(540, 140, 90, 30);

        lblReportImageIcon.setToolTipText("Report Image");
        lblReportImageIcon.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panelClientSetup.add(lblReportImageIcon);
        lblReportImageIcon.setBounds(530, 10, 120, 110);

        tabbedPane.addTab("Property Setup", panelClientSetup);

        panelBillSetup.setBackground(new java.awt.Color(153, 204, 255));
        panelBillSetup.setOpaque(false);

        lblBillpaperSize.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblBillpaperSize.setText("Bill Paper Size");

        lblPrintMode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPrintMode.setText("Print Mode");

        lblFooter.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblFooter.setText("Bill Footer");

        txtBillFooter.setColumns(20);
        txtBillFooter.setRows(5);
        txtBillFooter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtBillFooterMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(txtBillFooter);

        cmbBillPaperSize.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbBillPaperSize.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2", "3" }));
        cmbBillPaperSize.setPreferredSize(new java.awt.Dimension(145, 35));

        cmbPrintMode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbPrintMode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Portrait", "Landscape" }));
        cmbPrintMode.setPreferredSize(new java.awt.Dimension(145, 35));
        cmbPrintMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbPrintModeActionPerformed(evt);
            }
        });

        chkNegBilling.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkNegBilling.setText("Allow Negative Billing  :");
        chkNegBilling.setToolTipText("");
        chkNegBilling.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkNegBilling.setOpaque(false);

        chkDayEnd.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkDayEnd.setText("Day End (Mandatory)                      ");
        chkDayEnd.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkDayEnd.setOpaque(false);

        chkMultiBillPrint.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkMultiBillPrint.setText("Multiple Bill Printing      :");
        chkMultiBillPrint.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkMultiBillPrint.setOpaque(false);
        chkMultiBillPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkMultiBillPrintActionPerformed(evt);
            }
        });

        chkEnableKOT.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkEnableKOT.setText("Enable KOT Printing      :");
        chkEnableKOT.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkEnableKOT.setOpaque(false);

        chkEffectOnPSP.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkEffectOnPSP.setText("Effect On PSP            :");
        chkEffectOnPSP.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkEffectOnPSP.setOpaque(false);
        chkEffectOnPSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkEffectOnPSPActionPerformed(evt);
            }
        });

        chkPrintVatNo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkPrintVatNo.setText("Print VAT No.              ");
        chkPrintVatNo.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkPrintVatNo.setOpaque(false);
        chkPrintVatNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkPrintVatNoActionPerformed(evt);
            }
        });

        txtVatNo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtVatNoMouseClicked(evt);
            }
        });

        chkShowBill.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkShowBill.setText("Show Docs                 : ");
        chkShowBill.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkShowBill.setOpaque(false);
        chkShowBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkShowBillActionPerformed(evt);
            }
        });

        chkServiceTaxNo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkServiceTaxNo.setText("Print Service Tax No     ");
        chkServiceTaxNo.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkServiceTaxNo.setOpaque(false);

        txtServiceTaxno.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtServiceTaxnoMouseClicked(evt);
            }
        });

        chkManualBillNo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkManualBillNo.setText("Manual Bill No.            :");
        chkManualBillNo.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkManualBillNo.setOpaque(false);
        chkManualBillNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkManualBillNoActionPerformed(evt);
            }
        });

        lblColSize.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblColSize.setText("Column Size");

        chkPrintKotForDirectBiller.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkPrintKotForDirectBiller.setText("Enable KOT Printing For Direct Biller   ");
        chkPrintKotForDirectBiller.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkPrintKotForDirectBiller.setOpaque(false);
        chkPrintKotForDirectBiller.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkPrintKotForDirectBillerActionPerformed(evt);
            }
        });

        lblReportType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblReportType.setText("KOT Printing");

        cmbColumnSize.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "40", "48" }));

        cmbPrintType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Text File", "Jasper" }));

        lblNoOfAdvReceiptPrint.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblNoOfAdvReceiptPrint.setText("No Of Advance Receipt Print  ");

        txtAdvRecPrintCount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        lblBillFormat.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblBillFormat.setText("Bill Format ");

        cmbBillFormatType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Text 1", "Text 2", "Text 3", "Text 4", "Text 5", "Text 6", "Text 7", "Text 8", "Text 9", "Text 10", "Text 11", "Text 12", "Text 13", "Text 14", "Text 15", "Text 16", "Text 17", "Text 18", "Text 19", "Text 20", "Text 21", "Text PlayZone", "Jasper 1", "Jasper 2", "Jasper 3", "Jasper 4", "Jasper 5", "Jasper 6", "Jasper 7", "Jasper 8", "Jasper 9", "Jasper 10", "Stationery 1", "Stationery 2", "Stock Transfer Note 1", "Saloon 1" }));

        chkPrintShortNameOnKOT.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkPrintShortNameOnKOT.setText("Print Short Name On KOT");
        chkPrintShortNameOnKOT.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkPrintShortNameOnKOT.setOpaque(false);
        chkPrintShortNameOnKOT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkPrintShortNameOnKOTActionPerformed(evt);
            }
        });

        lblNoOfLinesInKOTPrint.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblNoOfLinesInKOTPrint.setText("No Of Lines In KOT Print  ");

        txtNoOfLinesInKOTPrint.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        chkMultiKOTPrint.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkMultiKOTPrint.setText("Multiple KOT Printing    :");
        chkMultiKOTPrint.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkMultiKOTPrint.setOpaque(false);

        lblShowBillsDtlType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblShowBillsDtlType.setText("Show Bills Detail Type:");

        cmbShowBillsDtlType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Table Detail Wise", "Delivery Detail Wise" }));

        chkPrintInvoiceOnBill.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkPrintInvoiceOnBill.setText("Print TAX Invoice On Bill                  ");
        chkPrintInvoiceOnBill.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkPrintInvoiceOnBill.setOpaque(false);

        chkPrintInclusiveOfAllTaxesOnBill.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkPrintInclusiveOfAllTaxesOnBill.setText("Print Inclusive Of All Taxes On Bill      ");
        chkPrintInclusiveOfAllTaxesOnBill.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkPrintInclusiveOfAllTaxesOnBill.setOpaque(false);

        chkPrintTDHItemsInBill.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkPrintTDHItemsInBill.setText("Print TDH Items In Bill     ");
        chkPrintTDHItemsInBill.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkPrintTDHItemsInBill.setOpaque(false);
        chkPrintTDHItemsInBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkPrintTDHItemsInBillActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBillSetupLayout = new javax.swing.GroupLayout(panelBillSetup);
        panelBillSetup.setLayout(panelBillSetupLayout);
        panelBillSetupLayout.setHorizontalGroup(
            panelBillSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBillSetupLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBillSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelBillSetupLayout.createSequentialGroup()
                        .addGroup(panelBillSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chkManualBillNo)
                            .addComponent(chkEffectOnPSP))
                        .addGap(75, 75, 75)
                        .addGroup(panelBillSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(chkPrintInvoiceOnBill, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(chkPrintInclusiveOfAllTaxesOnBill, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(panelBillSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(chkEnableKOT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(chkPrintShortNameOnKOT, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chkPrintTDHItemsInBill, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chkShowBill, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelBillSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(panelBillSetupLayout.createSequentialGroup()
                            .addGroup(panelBillSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(panelBillSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(chkMultiKOTPrint, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(chkMultiBillPrint, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblBillpaperSize, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(panelBillSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(panelBillSetupLayout.createSequentialGroup()
                                        .addComponent(lblPrintMode)
                                        .addGap(18, 18, 18)
                                        .addComponent(cmbPrintMode, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelBillSetupLayout.createSequentialGroup()
                                        .addGroup(panelBillSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(panelBillSetupLayout.createSequentialGroup()
                                                .addGap(123, 123, 123)
                                                .addComponent(cmbBillPaperSize, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(chkNegBilling, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBillSetupLayout.createSequentialGroup()
                                                .addComponent(lblReportType, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cmbPrintType, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(28, 28, 28)
                                        .addGroup(panelBillSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(chkDayEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(panelBillSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(panelBillSetupLayout.createSequentialGroup()
                                                    .addComponent(lblBillFormat)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(cmbBillFormatType, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addComponent(chkPrintKotForDirectBiller))))))
                            .addGap(18, 18, 18)
                            .addComponent(lblColSize)
                            .addGap(29, 29, 29)
                            .addComponent(cmbColumnSize, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelBillSetupLayout.createSequentialGroup()
                            .addComponent(lblFooter, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 515, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelBillSetupLayout.createSequentialGroup()
                        .addGroup(panelBillSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelBillSetupLayout.createSequentialGroup()
                                .addComponent(lblNoOfLinesInKOTPrint)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtNoOfLinesInKOTPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelBillSetupLayout.createSequentialGroup()
                                .addComponent(lblShowBillsDtlType)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmbShowBillsDtlType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelBillSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNoOfAdvReceiptPrint)
                            .addComponent(chkPrintVatNo, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chkServiceTaxNo, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelBillSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtVatNo, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                            .addComponent(txtServiceTaxno)
                            .addComponent(txtAdvRecPrintCount))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelBillSetupLayout.setVerticalGroup(
            panelBillSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBillSetupLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBillSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblFooter, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(panelBillSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBillSetupLayout.createSequentialGroup()
                        .addGroup(panelBillSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelBillSetupLayout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addGroup(panelBillSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblBillpaperSize, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmbBillPaperSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(panelBillSetupLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panelBillSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cmbPrintMode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblPrintMode, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(16, 16, 16)
                        .addGroup(panelBillSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(cmbBillFormatType, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblBillFormat, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbPrintType, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblReportType, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelBillSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(chkPrintKotForDirectBiller, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chkNegBilling, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelBillSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chkDayEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chkMultiBillPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7)
                        .addGroup(panelBillSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(chkMultiKOTPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chkPrintInvoiceOnBill, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelBillSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(chkManualBillNo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chkPrintInclusiveOfAllTaxesOnBill, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelBillSetupLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(panelBillSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbColumnSize, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblColSize, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(chkShowBill, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(chkEnableKOT, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(chkPrintShortNameOnKOT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(7, 7, 7)
                        .addComponent(chkPrintTDHItemsInBill, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(45, 45, 45)))
                .addGroup(panelBillSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkEffectOnPSP, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkPrintVatNo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtVatNo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBillSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkServiceTaxNo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtServiceTaxno, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNoOfLinesInKOTPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNoOfLinesInKOTPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBillSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblShowBillsDtlType, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbShowBillsDtlType, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNoOfAdvReceiptPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAdvRecPrintCount, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(205, 205, 205))
        );

        panelBillSetupLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {chkPrintVatNo, chkServiceTaxNo, lblNoOfAdvReceiptPrint, txtAdvRecPrintCount, txtServiceTaxno, txtVatNo});

        tabbedPane.addTab("Bill Setup", panelBillSetup);

        panelPOSSetup1.setBackground(new java.awt.Color(153, 204, 255));
        panelPOSSetup1.setOpaque(false);
        panelPOSSetup1.setLayout(null);

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel11.setText("Property Type");
        panelPOSSetup1.add(jLabel11);
        jLabel11.setBounds(30, 20, 94, 30);

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel12.setText("Web Service Link");
        panelPOSSetup1.add(jLabel12);
        jLabel12.setBounds(30, 60, 100, 30);

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel13.setText("Data Posting Frequency");
        panelPOSSetup1.add(jLabel13);
        jLabel13.setBounds(340, 20, 130, 30);

        strPOSType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        strPOSType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Stand Alone-HOPOS", "Stand Alone", "HOPOS", "Client POS", "DebitCard POS" }));
        panelPOSSetup1.add(strPOSType);
        strPOSType.setBounds(138, 20, 160, 30);

        cmbDataSendFrequency.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbDataSendFrequency.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "After Every Bill", "After Day End", "Manual" }));
        panelPOSSetup1.add(cmbDataSendFrequency);
        cmbDataSendFrequency.setBounds(490, 20, 160, 30);

        txtWebServiceLink.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtWebServiceLink.setText(" ");
        txtWebServiceLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtWebServiceLinkMouseClicked(evt);
            }
        });
        panelPOSSetup1.add(txtWebServiceLink);
        txtWebServiceLink.setBounds(140, 60, 420, 30);

        lblChangeTheme.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblChangeTheme.setText("Change Theme");
        panelPOSSetup1.add(lblChangeTheme);
        lblChangeTheme.setBounds(30, 140, 90, 30);

        cmbChangeTheme.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbChangeTheme.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Default", "Tiles", "Color" }));
        cmbChangeTheme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbChangeThemeActionPerformed(evt);
            }
        });
        panelPOSSetup1.add(cmbChangeTheme);
        cmbChangeTheme.setBounds(140, 140, 160, 30);

        txtMaxDiscount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtMaxDiscount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtMaxDiscountMouseClicked(evt);
            }
        });
        panelPOSSetup1.add(txtMaxDiscount);
        txtMaxDiscount.setBounds(600, 100, 50, 31);

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel15.setText("Customer Series");
        panelPOSSetup1.add(jLabel15);
        jLabel15.setBounds(30, 180, 110, 30);

        chkEditHomeDelivery.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkEditHomeDelivery.setText("Edit Home Delivery         :  ");
        chkEditHomeDelivery.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkEditHomeDelivery.setOpaque(false);
        chkEditHomeDelivery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkEditHomeDeliveryActionPerformed(evt);
            }
        });
        panelPOSSetup1.add(chkEditHomeDelivery);
        chkEditHomeDelivery.setBounds(30, 220, 180, 31);

        chkSlabBasedHomeDelCharges.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkSlabBasedHomeDelCharges.setText("Slab Based Home Delivery Charges :");
        chkSlabBasedHomeDelCharges.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkSlabBasedHomeDelCharges.setOpaque(false);
        chkSlabBasedHomeDelCharges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSlabBasedHomeDelChargesActionPerformed(evt);
            }
        });
        panelPOSSetup1.add(chkSlabBasedHomeDelCharges);
        chkSlabBasedHomeDelCharges.setBounds(430, 190, 220, 31);

        chkSkip_Waiter_Selection.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkSkip_Waiter_Selection.setText("Skip Waiter Selection     :  ");
        chkSkip_Waiter_Selection.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkSkip_Waiter_Selection.setOpaque(false);
        chkSkip_Waiter_Selection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSkip_Waiter_SelectionActionPerformed(evt);
            }
        });
        panelPOSSetup1.add(chkSkip_Waiter_Selection);
        chkSkip_Waiter_Selection.setBounds(30, 260, 180, 30);

        chkSkip_pax_selection.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkSkip_pax_selection.setText("Skip Pax Selection   :");
        chkSkip_pax_selection.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkSkip_pax_selection.setOpaque(false);
        panelPOSSetup1.add(chkSkip_pax_selection);
        chkSkip_pax_selection.setBounds(220, 260, 160, 30);

        cmbStockInOption.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbStockInOption.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ItemWise", "MenuHeadWise" }));
        panelPOSSetup1.add(cmbStockInOption);
        cmbStockInOption.setBounds(180, 370, 200, 30);

        lblStkInOptions.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblStkInOptions.setText("Stock In Options");
        panelPOSSetup1.add(lblStkInOptions);
        lblStkInOptions.setBounds(30, 370, 100, 30);

        lblMaxDiscount.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMaxDiscount.setText("Max. Discount");
        panelPOSSetup1.add(lblMaxDiscount);
        lblMaxDiscount.setBounds(500, 100, 90, 31);

        txtCustSeries.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtCustSeriesMouseClicked(evt);
            }
        });
        panelPOSSetup1.add(txtCustSeries);
        txtCustSeries.setBounds(140, 180, 60, 30);

        chkActivePromotions.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkActivePromotions.setText("Active Promotions         :  ");
        chkActivePromotions.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkActivePromotions.setOpaque(false);
        chkActivePromotions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkActivePromotionsActionPerformed(evt);
            }
        });
        panelPOSSetup1.add(chkActivePromotions);
        chkActivePromotions.setBounds(30, 340, 180, 23);

        chkPrintForVoidBill.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkPrintForVoidBill.setText("Print For Void Bill     :");
        chkPrintForVoidBill.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkPrintForVoidBill.setOpaque(false);
        chkPrintForVoidBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkPrintForVoidBillActionPerformed(evt);
            }
        });
        panelPOSSetup1.add(chkPrintForVoidBill);
        chkPrintForVoidBill.setBounds(220, 220, 160, 31);

        chkPostSalesDataToMMS.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkPostSalesDataToMMS.setText("Post Sales Data to MMS  :  ");
        chkPostSalesDataToMMS.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkPostSalesDataToMMS.setOpaque(false);
        chkPostSalesDataToMMS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkPostSalesDataToMMSActionPerformed(evt);
            }
        });
        panelPOSSetup1.add(chkPostSalesDataToMMS);
        chkPostSalesDataToMMS.setBounds(30, 300, 175, 31);

        chkAreaMasterCompulsory.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkAreaMasterCompulsory.setText("Compulsory Customer Area Master :");
        chkAreaMasterCompulsory.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkAreaMasterCompulsory.setOpaque(false);
        chkAreaMasterCompulsory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkAreaMasterCompulsoryActionPerformed(evt);
            }
        });
        panelPOSSetup1.add(chkAreaMasterCompulsory);
        chkAreaMasterCompulsory.setBounds(430, 230, 220, 23);

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel19.setText("Pick Up Price From");
        panelPOSSetup1.add(jLabel19);
        jLabel19.setBounds(30, 410, 120, 30);

        cmbPriceFrom.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbPriceFrom.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Menu Pricing", "Item Master" }));
        panelPOSSetup1.add(cmbPriceFrom);
        cmbPriceFrom.setBounds(180, 410, 200, 30);

        chkPrinterErrorMessage.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkPrinterErrorMessage.setText("Show Printer Error Message         : ");
        chkPrinterErrorMessage.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkPrinterErrorMessage.setOpaque(false);
        chkPrinterErrorMessage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkPrinterErrorMessageActionPerformed(evt);
            }
        });
        panelPOSSetup1.add(chkPrinterErrorMessage);
        chkPrinterErrorMessage.setBounds(430, 270, 220, 23);

        chkChangeQtyForExternalCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkChangeQtyForExternalCode.setText("Change Quantity For External Code : ");
        chkChangeQtyForExternalCode.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkChangeQtyForExternalCode.setMaximumSize(new java.awt.Dimension(217, 23));
        chkChangeQtyForExternalCode.setMinimumSize(new java.awt.Dimension(217, 23));
        chkChangeQtyForExternalCode.setName(""); // NOI18N
        chkChangeQtyForExternalCode.setOpaque(false);
        chkChangeQtyForExternalCode.setPreferredSize(new java.awt.Dimension(217, 23));
        chkChangeQtyForExternalCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkChangeQtyForExternalCodeActionPerformed(evt);
            }
        });
        panelPOSSetup1.add(chkChangeQtyForExternalCode);
        chkChangeQtyForExternalCode.setBounds(420, 310, 230, 23);

        chkPrintKOTYN.setText("jCheckBox1");
        panelPOSSetup1.add(chkPrintKOTYN);
        chkPrintKOTYN.setBounds(340, 180, 20, 30);

        lblPrintKOTYN.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPrintKOTYN.setText("Print KOT           : ");
        panelPOSSetup1.add(lblPrintKOTYN);
        lblPrintKOTYN.setBounds(220, 180, 110, 30);

        chkShowItemStkColumnInDB.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkShowItemStkColumnInDB.setText("Show Item StK Column in DB       : ");
        chkShowItemStkColumnInDB.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chkShowItemStkColumnInDB.setOpaque(false);
        chkShowItemStkColumnInDB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkShowItemStkColumnInDBActionPerformed(evt);
            }
        });
        panelPOSSetup1.add(chkShowItemStkColumnInDB);
        chkShowItemStkColumnInDB.setBounds(426, 350, 221, 23);

        cmbItemType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Both", "Liquor", "Food" }));
        panelPOSSetup1.add(cmbItemType);
        cmbItemType.setBounds(230, 300, 150, 30);

        lblApplyDiscountOn.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblApplyDiscountOn.setText("Discount On");
        panelPOSSetup1.add(lblApplyDiscountOn);
        lblApplyDiscountOn.setBounds(30, 450, 120, 30);

        cmbApplyDiscountOn.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbApplyDiscountOn.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SubTotal", "SubTotalTax" }));
        panelPOSSetup1.add(cmbApplyDiscountOn);
        cmbApplyDiscountOn.setBounds(180, 450, 200, 30);

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel17.setText("Use Vat And Service No From POS :");
        panelPOSSetup1.add(jLabel17);
        jLabel17.setBounds(430, 430, 200, 20);

        chkPrintBill.setOpaque(false);
        panelPOSSetup1.add(chkPrintBill);
        chkPrintBill.setBounds(630, 390, 20, 20);

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel18.setText("Ask For Print Bill  Popup                :");
        panelPOSSetup1.add(jLabel18);
        jLabel18.setBounds(420, 390, 200, 20);

        chkUseVatAndServiceNoFromPos.setOpaque(false);
        panelPOSSetup1.add(chkUseVatAndServiceNoFromPos);
        chkUseVatAndServiceNoFromPos.setBounds(630, 430, 21, 21);

        dteHOServerDate.setToolTipText("Select From Date");
        dteHOServerDate.setPreferredSize(new java.awt.Dimension(119, 35));
        dteHOServerDate.addHierarchyListener(new java.awt.event.HierarchyListener() {
            public void hierarchyChanged(java.awt.event.HierarchyEvent evt) {
                dteHOServerDateHierarchyChanged(evt);
            }
        });
        dteHOServerDate.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dteHOServerDatePropertyChange(evt);
            }
        });
        panelPOSSetup1.add(dteHOServerDate);
        dteHOServerDate.setBounds(140, 100, 160, 30);

        lblHOServerDate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblHOServerDate.setText("HO Server Date");
        panelPOSSetup1.add(lblHOServerDate);
        lblHOServerDate.setBounds(30, 100, 110, 30);

        btnTestWebService.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnTestWebService.setForeground(new java.awt.Color(255, 255, 255));
        btnTestWebService.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgSpacebar1.png"))); // NOI18N
        btnTestWebService.setText("TEST");
        btnTestWebService.setToolTipText("Save Cost Center Master");
        btnTestWebService.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTestWebService.setInheritsPopupMenu(true);
        btnTestWebService.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyBoardBackspaceButtonDark.png"))); // NOI18N
        btnTestWebService.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTestWebServiceMouseClicked(evt);
            }
        });
        btnTestWebService.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTestWebServiceActionPerformed(evt);
            }
        });
        btnTestWebService.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnTestWebServiceKeyPressed(evt);
            }
        });
        panelPOSSetup1.add(btnTestWebService);
        btnTestWebService.setBounds(570, 60, 80, 30);

        lblPostMMSSalesEffect.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPostMMSSalesEffect.setText("Post MMS Sales Effect :");
        panelPOSSetup1.add(lblPostMMSSalesEffect);
        lblPostMMSSalesEffect.setBounds(30, 490, 150, 30);

        cmbPostMMSSalesEffectCostOrLoc.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbPostMMSSalesEffectCostOrLoc.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Cost Center", "WS Location" }));
        panelPOSSetup1.add(cmbPostMMSSalesEffectCostOrLoc);
        cmbPostMMSSalesEffectCostOrLoc.setBounds(180, 490, 200, 30);

        chkPOSWiseItemLinkeUpToMMSProduct.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkPOSWiseItemLinkeUpToMMSProduct.setText("POS Wise Item LinkeUp To MMS Product :");
        chkPOSWiseItemLinkeUpToMMSProduct.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkPOSWiseItemLinkeUpToMMSProduct.setMargin(new java.awt.Insets(0, 2, 2, 2));
        chkPOSWiseItemLinkeUpToMMSProduct.setOpaque(false);
        chkPOSWiseItemLinkeUpToMMSProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkPOSWiseItemLinkeUpToMMSProductActionPerformed(evt);
            }
        });
        panelPOSSetup1.add(chkPOSWiseItemLinkeUpToMMSProduct);
        chkPOSWiseItemLinkeUpToMMSProduct.setBounds(390, 490, 260, 30);

        chkEnableMasterDiscount.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkEnableMasterDiscount.setText("Enable Master Discount :");
        chkEnableMasterDiscount.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkEnableMasterDiscount.setOpaque(false);
        chkEnableMasterDiscount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkEnableMasterDiscountActionPerformed(evt);
            }
        });
        panelPOSSetup1.add(chkEnableMasterDiscount);
        chkEnableMasterDiscount.setBounds(311, 100, 180, 31);

        chkEnableLockTables.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkEnableLockTables.setSelected(true);
        chkEnableLockTables.setText("Enable Lock Tables   : ");
        chkEnableLockTables.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkEnableLockTables.setMargin(new java.awt.Insets(0, 2, 2, 2));
        chkEnableLockTables.setOpaque(false);
        chkEnableLockTables.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkEnableLockTablesActionPerformed(evt);
            }
        });
        panelPOSSetup1.add(chkEnableLockTables);
        chkEnableLockTables.setBounds(500, 460, 150, 30);

        tabbedPane.addTab("POS Setup 1", panelPOSSetup1);

        panelPOSSetup2.setBackground(new java.awt.Color(153, 204, 255));
        panelPOSSetup2.setOpaque(false);
        panelPOSSetup2.setLayout(null);

        chkPrintManualAdvOrderOnBill.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkPrintManualAdvOrderOnBill.setText("Print Manual Advance Order No On Bill");
        chkPrintManualAdvOrderOnBill.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        chkPrintManualAdvOrderOnBill.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkPrintManualAdvOrderOnBill.setOpaque(false);
        chkPrintManualAdvOrderOnBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkPrintManualAdvOrderOnBillActionPerformed(evt);
            }
        });
        panelPOSSetup2.add(chkPrintManualAdvOrderOnBill);
        chkPrintManualAdvOrderOnBill.setBounds(400, 10, 240, 23);

        chkManualAdvOrderCompulsory.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkManualAdvOrderCompulsory.setText("Manual Advance Order No Compulsory    :");
        chkManualAdvOrderCompulsory.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkManualAdvOrderCompulsory.setOpaque(false);
        chkManualAdvOrderCompulsory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkManualAdvOrderCompulsoryActionPerformed(evt);
            }
        });
        panelPOSSetup2.add(chkManualAdvOrderCompulsory);
        chkManualAdvOrderCompulsory.setBounds(0, 10, 260, 23);

        chkPrintModifierQtyOnKOT.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkPrintModifierQtyOnKOT.setText("Print Modifier Quantity On KOT              :");
        chkPrintModifierQtyOnKOT.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkPrintModifierQtyOnKOT.setOpaque(false);
        panelPOSSetup2.add(chkPrintModifierQtyOnKOT);
        chkPrintModifierQtyOnKOT.setBounds(0, 40, 260, 20);

        lblMenuItemDisplaySeq.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMenuItemDisplaySeq.setText("Menu Item Display Sequence");
        panelPOSSetup2.add(lblMenuItemDisplaySeq);
        lblMenuItemDisplaySeq.setBounds(0, 110, 159, 20);

        panelPOSSetup2.add(cmbMenuItemDisSeq);
        cmbMenuItemDisSeq.setBounds(160, 110, 190, 30);

        cmbMenuItemSortingOn.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "--SELECT--", "SUB GROUP WISE", "SUB MENU HEAD WISE" }));
        cmbMenuItemSortingOn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbMenuItemSortingOnActionPerformed(evt);
            }
        });
        panelPOSSetup2.add(cmbMenuItemSortingOn);
        cmbMenuItemSortingOn.setBounds(160, 70, 190, 30);

        lblMenuItemSorting.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMenuItemSorting.setText("Menu Item Sorting");
        panelPOSSetup2.add(lblMenuItemSorting);
        lblMenuItemSorting.setBounds(0, 80, 103, 15);

        chkItemQtyNumpad.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkItemQtyNumpad.setText("Item Quantiy Numeric Pad                      :");
        chkItemQtyNumpad.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chkItemQtyNumpad.setOpaque(false);
        chkItemQtyNumpad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkItemQtyNumpadActionPerformed(evt);
            }
        });
        panelPOSSetup2.add(chkItemQtyNumpad);
        chkItemQtyNumpad.setBounds(0, 150, 270, 23);

        chkPrintKOTToLocalPrinter.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkPrintKOTToLocalPrinter.setText("Print KOT To Local Printer                      :");
        chkPrintKOTToLocalPrinter.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chkPrintKOTToLocalPrinter.setOpaque(false);
        chkPrintKOTToLocalPrinter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkPrintKOTToLocalPrinterActionPerformed(evt);
            }
        });
        panelPOSSetup2.add(chkPrintKOTToLocalPrinter);
        chkPrintKOTToLocalPrinter.setBounds(0, 180, 261, 23);

        chkEnableSettleBtnForDirectBillerBill.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkEnableSettleBtnForDirectBillerBill.setText("Enable Settle Button For Direct Biller Bill    :");
        chkEnableSettleBtnForDirectBillerBill.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chkEnableSettleBtnForDirectBillerBill.setOpaque(false);
        chkEnableSettleBtnForDirectBillerBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkEnableSettleBtnForDirectBillerBillActionPerformed(evt);
            }
        });
        panelPOSSetup2.add(chkEnableSettleBtnForDirectBillerBill);
        chkEnableSettleBtnForDirectBillerBill.setBounds(0, 240, 270, 23);

        chkDelBoyCompulsoryOnDirectBiller.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkDelBoyCompulsoryOnDirectBiller.setText("Delivery Boy Compulsory On Direct Biller    :");
        chkDelBoyCompulsoryOnDirectBiller.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chkDelBoyCompulsoryOnDirectBiller.setOpaque(false);
        chkDelBoyCompulsoryOnDirectBiller.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkDelBoyCompulsoryOnDirectBillerActionPerformed(evt);
            }
        });
        panelPOSSetup2.add(chkDelBoyCompulsoryOnDirectBiller);
        chkDelBoyCompulsoryOnDirectBiller.setBounds(0, 210, 270, 23);

        lblDontShowAdvOrderInOtherPOS.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDontShowAdvOrderInOtherPOS.setText("Don't Show Advance Order In Other POS :");
        panelPOSSetup2.add(lblDontShowAdvOrderInOtherPOS);
        lblDontShowAdvOrderInOtherPOS.setBounds(0, 270, 237, 20);

        chkDontShowAdvOrderInOtherPOS.setOpaque(false);
        panelPOSSetup2.add(chkDontShowAdvOrderInOtherPOS);
        chkDontShowAdvOrderInOtherPOS.setBounds(240, 270, 20, 21);

        lblPrintZeroAmtModifierInBill.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPrintZeroAmtModifierInBill.setText("Print Zero Amount Modifiers In Bill            :");
        panelPOSSetup2.add(lblPrintZeroAmtModifierInBill);
        lblPrintZeroAmtModifierInBill.setBounds(0, 300, 240, 20);

        chkPrintZeroAmtModifierInBill.setOpaque(false);
        panelPOSSetup2.add(chkPrintZeroAmtModifierInBill);
        chkPrintZeroAmtModifierInBill.setBounds(240, 300, 20, 21);

        chkPointsOnBillPrint.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkPointsOnBillPrint.setText("Points On Bill Print                                 :");
        chkPointsOnBillPrint.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkPointsOnBillPrint.setOpaque(false);
        panelPOSSetup2.add(chkPointsOnBillPrint);
        chkPointsOnBillPrint.setBounds(0, 330, 260, 23);

        lblSlipNoForCreditCardBillYN.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblSlipNoForCreditCardBillYN.setText("Slip No Compulsory For Credit Card Bill         :");
        panelPOSSetup2.add(lblSlipNoForCreditCardBillYN);
        lblSlipNoForCreditCardBillYN.setBounds(370, 150, 250, 20);

        chkSlipNoForCreditCardBillYN.setText("jCheckBox1");
        chkSlipNoForCreditCardBillYN.setOpaque(false);
        panelPOSSetup2.add(chkSlipNoForCreditCardBillYN);
        chkSlipNoForCreditCardBillYN.setBounds(620, 150, 20, 23);

        lblExpDateForCreditCardBillYN.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblExpDateForCreditCardBillYN.setText("Expiry date Compulsory For Credit Card Bill   :");
        panelPOSSetup2.add(lblExpDateForCreditCardBillYN);
        lblExpDateForCreditCardBillYN.setBounds(370, 180, 250, 20);

        chkExpDateForCreditCardBillYN.setText("jCheckBox1");
        chkExpDateForCreditCardBillYN.setOpaque(false);
        panelPOSSetup2.add(chkExpDateForCreditCardBillYN);
        chkExpDateForCreditCardBillYN.setBounds(620, 180, 20, 23);

        lblSelectWaiterFromCardSwipe.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblSelectWaiterFromCardSwipe.setText("Select Waiter From Card Swipe                  :");
        panelPOSSetup2.add(lblSelectWaiterFromCardSwipe);
        lblSelectWaiterFromCardSwipe.setBounds(370, 210, 250, 20);

        chkSelectWaiterFromCardSwipe.setText("jCheckBox1");
        chkSelectWaiterFromCardSwipe.setOpaque(false);
        panelPOSSetup2.add(chkSelectWaiterFromCardSwipe);
        chkSelectWaiterFromCardSwipe.setBounds(620, 210, 20, 23);

        lblMultiWaiterSelection.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMultiWaiterSelection.setText("Multiple Waiter Selection On Make KOT       :");
        panelPOSSetup2.add(lblMultiWaiterSelection);
        lblMultiWaiterSelection.setBounds(370, 240, 250, 20);

        chkMultipleWaiterSelectionOnMakeKOT.setText("jCheckBox1");
        chkMultipleWaiterSelectionOnMakeKOT.setOpaque(false);
        panelPOSSetup2.add(chkMultipleWaiterSelectionOnMakeKOT);
        chkMultipleWaiterSelectionOnMakeKOT.setBounds(620, 240, 20, 23);

        lblMoveTableToOtherPOS.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMoveTableToOtherPOS.setText("Move Table From One POS To Other POS   :");
        panelPOSSetup2.add(lblMoveTableToOtherPOS);
        lblMoveTableToOtherPOS.setBounds(370, 270, 250, 20);

        chkMoveTableToOtherPOS.setText("jCheckBox1");
        chkMoveTableToOtherPOS.setOpaque(false);
        panelPOSSetup2.add(chkMoveTableToOtherPOS);
        chkMoveTableToOtherPOS.setBounds(620, 270, 20, 23);

        chkMoveKOTToOtherPOS.setText("jCheckBox1");
        chkMoveKOTToOtherPOS.setOpaque(false);
        panelPOSSetup2.add(chkMoveKOTToOtherPOS);
        chkMoveKOTToOtherPOS.setBounds(620, 300, 20, 23);

        lblMoveKOTToOtherPOS1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMoveKOTToOtherPOS1.setText("Move KOT From One POS To Other POS    :");
        panelPOSSetup2.add(lblMoveKOTToOtherPOS1);
        lblMoveKOTToOtherPOS1.setBounds(370, 300, 250, 20);

        lblCalculateTaxOnMakeKOT.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCalculateTaxOnMakeKOT.setText("Calculate Tax on Make KOT                       :");
        panelPOSSetup2.add(lblCalculateTaxOnMakeKOT);
        lblCalculateTaxOnMakeKOT.setBounds(370, 330, 250, 20);

        chkCalculateTaxOnMakeKOT.setText("jCheckBox1");
        chkCalculateTaxOnMakeKOT.setOpaque(false);
        panelPOSSetup2.add(chkCalculateTaxOnMakeKOT);
        chkCalculateTaxOnMakeKOT.setBounds(620, 330, 20, 23);

        lblCalculateDiscItemWise.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCalculateDiscItemWise.setText("Calculate Discount Item Wise                   :");
        panelPOSSetup2.add(lblCalculateDiscItemWise);
        lblCalculateDiscItemWise.setBounds(0, 360, 240, 20);

        chkCalculateDiscItemWise.setText("jCheckBox1");
        chkCalculateDiscItemWise.setOpaque(false);
        panelPOSSetup2.add(chkCalculateDiscItemWise);
        chkCalculateDiscItemWise.setBounds(240, 360, 20, 23);

        chkTakewayCustomerSelection.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkTakewayCustomerSelection.setText("Take Away Customer Selection                 :");
        chkTakewayCustomerSelection.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chkTakewayCustomerSelection.setOpaque(false);
        panelPOSSetup2.add(chkTakewayCustomerSelection);
        chkTakewayCustomerSelection.setBounds(370, 360, 270, 23);

        chkBoxAllowNewAreaMasterFromCustMaster.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkBoxAllowNewAreaMasterFromCustMaster.setText("Allow New Area Master From Customer Master ");
        chkBoxAllowNewAreaMasterFromCustMaster.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        chkBoxAllowNewAreaMasterFromCustMaster.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chkBoxAllowNewAreaMasterFromCustMaster.setOpaque(false);
        chkBoxAllowNewAreaMasterFromCustMaster.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkBoxAllowNewAreaMasterFromCustMasterActionPerformed(evt);
            }
        });
        panelPOSSetup2.add(chkBoxAllowNewAreaMasterFromCustMaster);
        chkBoxAllowNewAreaMasterFromCustMaster.setBounds(350, 50, 290, 20);

        lblSelectCustAddressForBill.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblSelectCustAddressForBill.setText("Customer Address Selection For Billing       :");
        panelPOSSetup2.add(lblSelectCustAddressForBill);
        lblSelectCustAddressForBill.setBounds(0, 390, 240, 20);

        chkSelectCustAddressForBill.setText("jCheckBox1");
        chkSelectCustAddressForBill.setOpaque(false);
        panelPOSSetup2.add(chkSelectCustAddressForBill);
        chkSelectCustAddressForBill.setBounds(240, 390, 20, 20);

        chkGenrateMI.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkGenrateMI.setText("Generate MI With DayEnd                        :");
        chkGenrateMI.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chkGenrateMI.setOpaque(false);
        panelPOSSetup2.add(chkGenrateMI);
        chkGenrateMI.setBounds(370, 390, 270, 23);

        chkAllowToCalculateItemWeight.setText("Allow To Calculate Item Weight");
        chkAllowToCalculateItemWeight.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        chkAllowToCalculateItemWeight.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chkAllowToCalculateItemWeight.setOpaque(false);
        panelPOSSetup2.add(chkAllowToCalculateItemWeight);
        chkAllowToCalculateItemWeight.setBounds(420, 80, 220, 20);

        chkItemWiseKOTPrintYN.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkItemWiseKOTPrintYN.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chkItemWiseKOTPrintYN.setOpaque(false);
        panelPOSSetup2.add(chkItemWiseKOTPrintYN);
        chkItemWiseKOTPrintYN.setBounds(620, 110, 20, 30);

        chkShowPurchaseRateInDirectBiller.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkShowPurchaseRateInDirectBiller.setText("Show Purchase Rate in Direct Biller        :  ");
        chkShowPurchaseRateInDirectBiller.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkShowPurchaseRateInDirectBiller.setOpaque(false);
        chkShowPurchaseRateInDirectBiller.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkShowPurchaseRateInDirectBillerActionPerformed(evt);
            }
        });
        panelPOSSetup2.add(chkShowPurchaseRateInDirectBiller);
        chkShowPurchaseRateInDirectBiller.setBounds(0, 450, 270, 30);

        lblItemWiseKOTYN.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblItemWiseKOTYN.setText("Item Wise KOT Y/N :");
        panelPOSSetup2.add(lblItemWiseKOTYN);
        lblItemWiseKOTYN.setBounds(500, 110, 120, 30);

        chkAreaWIsePromotions.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkAreaWIsePromotions.setText("Area Wise Promotions                          :  ");
        chkAreaWIsePromotions.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkAreaWIsePromotions.setOpaque(false);
        chkAreaWIsePromotions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkAreaWIsePromotionsActionPerformed(evt);
            }
        });
        panelPOSSetup2.add(chkAreaWIsePromotions);
        chkAreaWIsePromotions.setBounds(0, 420, 270, 30);

        chkTableReservationForCustomer.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkTableReservationForCustomer.setText("Enable Table Reservation For Customer   : ");
        chkTableReservationForCustomer.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkTableReservationForCustomer.setOpaque(false);
        panelPOSSetup2.add(chkTableReservationForCustomer);
        chkTableReservationForCustomer.setBounds(0, 480, 270, 30);

        chkPrintHomeDeliveryYN.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkPrintHomeDeliveryYN.setSelected(true);
        chkPrintHomeDeliveryYN.setText("Print Home Delivery Y/N                       : ");
        chkPrintHomeDeliveryYN.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkPrintHomeDeliveryYN.setOpaque(false);
        chkPrintHomeDeliveryYN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkPrintHomeDeliveryYNActionPerformed(evt);
            }
        });
        panelPOSSetup2.add(chkPrintHomeDeliveryYN);
        chkPrintHomeDeliveryYN.setBounds(0, 513, 260, 20);

        chkScanQRYN.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkScanQRYN.setSelected(true);
        chkScanQRYN.setText("Scan QR Y/N                                           :");
        chkScanQRYN.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkScanQRYN.setOpaque(false);
        chkScanQRYN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkScanQRYNActionPerformed(evt);
            }
        });
        panelPOSSetup2.add(chkScanQRYN);
        chkScanQRYN.setBounds(367, 460, 273, 20);

        cmbEffectOfSales.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "NO", "MMS", "POS" }));
        panelPOSSetup2.add(cmbEffectOfSales);
        cmbEffectOfSales.setBounds(566, 490, 70, 30);

        lblEffectOfSales.setText("Effect Of Sales :");
        panelPOSSetup2.add(lblEffectOfSales);
        lblEffectOfSales.setBounds(450, 490, 110, 30);

        tabbedPane.addTab("POS Setup 2", panelPOSSetup2);

        panelPOSSetup3.setOpaque(false);
        panelPOSSetup3.setLayout(null);

        chkPopUpToApplyPromotionsOnBill.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkPopUpToApplyPromotionsOnBill.setText("Pop Up to Apply Promotions on Bill       : ");
        chkPopUpToApplyPromotionsOnBill.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkPopUpToApplyPromotionsOnBill.setOpaque(false);
        chkPopUpToApplyPromotionsOnBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkPopUpToApplyPromotionsOnBillActionPerformed(evt);
            }
        });
        panelPOSSetup3.add(chkPopUpToApplyPromotionsOnBill);
        chkPopUpToApplyPromotionsOnBill.setBounds(0, 10, 251, 20);

        lblDebitCardBalChkOnTrans.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDebitCardBalChkOnTrans.setText("Check Debit Card Bal on Transactions    :");
        panelPOSSetup3.add(lblDebitCardBalChkOnTrans);
        lblDebitCardBalChkOnTrans.setBounds(0, 40, 230, 20);

        chkCheckDebitCardBalOnTrans.setOpaque(false);
        panelPOSSetup3.add(chkCheckDebitCardBalOnTrans);
        chkCheckDebitCardBalOnTrans.setBounds(230, 40, 20, 20);

        lblPickSettlementsFromPOSMaster.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPickSettlementsFromPOSMaster.setText("Pick Settlements from POS Master        : ");
        panelPOSSetup3.add(lblPickSettlementsFromPOSMaster);
        lblPickSettlementsFromPOSMaster.setBounds(0, 70, 230, 20);

        chkSettlementsFromPOSMaster.setOpaque(false);
        panelPOSSetup3.add(chkSettlementsFromPOSMaster);
        chkSettlementsFromPOSMaster.setBounds(230, 71, 20, 20);

        lblEnableShift.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblEnableShift.setText("Enable Shift                                       : ");
        panelPOSSetup3.add(lblEnableShift);
        lblEnableShift.setBounds(0, 100, 230, 20);

        chkShiftWiseDayEnd.setOpaque(false);
        panelPOSSetup3.add(chkShiftWiseDayEnd);
        chkShiftWiseDayEnd.setBounds(230, 100, 20, 21);

        lblProductionLinkUp.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblProductionLinkUp.setText("Production Link Up                             : ");
        panelPOSSetup3.add(lblProductionLinkUp);
        lblProductionLinkUp.setBounds(0, 130, 230, 20);

        chkProductionLinkup.setOpaque(false);
        panelPOSSetup3.add(chkProductionLinkup);
        chkProductionLinkup.setBounds(230, 130, 20, 21);

        lblLockDataOnShift.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblLockDataOnShift.setText("Lock Data On Shift                             : ");
        panelPOSSetup3.add(lblLockDataOnShift);
        lblLockDataOnShift.setBounds(0, 160, 230, 20);

        chkLockDataOnShift.setSelected(true);
        chkLockDataOnShift.setOpaque(false);
        panelPOSSetup3.add(chkLockDataOnShift);
        chkLockDataOnShift.setBounds(230, 160, 20, 21);

        lblWSClientCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblWSClientCode.setText("WebStock /HO Client Code   ");
        panelPOSSetup3.add(lblWSClientCode);
        lblWSClientCode.setBounds(290, 10, 250, 30);
        panelPOSSetup3.add(txtWSClientCode);
        txtWSClientCode.setBounds(540, 10, 90, 30);

        lblEnableBillSeries.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblEnableBillSeries.setText("Enable Bill Series                                 : ");
        panelPOSSetup3.add(lblEnableBillSeries);
        lblEnableBillSeries.setBounds(0, 370, 230, 20);

        chkEnableBillSeries.setOpaque(false);
        panelPOSSetup3.add(chkEnableBillSeries);
        chkEnableBillSeries.setBounds(230, 370, 20, 21);

        lblEnablePMSIntegration.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblEnablePMSIntegration.setText("Enable PMS Integration                       : ");
        panelPOSSetup3.add(lblEnablePMSIntegration);
        lblEnablePMSIntegration.setBounds(0, 220, 230, 20);

        chkEnablePMSIntegration.setOpaque(false);
        panelPOSSetup3.add(chkEnablePMSIntegration);
        chkEnablePMSIntegration.setBounds(230, 220, 20, 21);

        lblPrintRemarkAndReasonForReprint.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPrintRemarkAndReasonForReprint.setText("Print Remark And Reason For Reprint     : ");
        panelPOSSetup3.add(lblPrintRemarkAndReasonForReprint);
        lblPrintRemarkAndReasonForReprint.setBounds(0, 280, 230, 20);

        chkPrintRemarkAndReasonForReprint.setOpaque(false);
        panelPOSSetup3.add(chkPrintRemarkAndReasonForReprint);
        chkPrintRemarkAndReasonForReprint.setBounds(230, 280, 20, 21);

        lblShowItemDtlsForChangeCustomerOnBill.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblShowItemDtlsForChangeCustomerOnBill.setText("Show Item Details Grid For Change Customer On Bill  ");
        panelPOSSetup3.add(lblShowItemDtlsForChangeCustomerOnBill);
        lblShowItemDtlsForChangeCustomerOnBill.setBounds(290, 350, 300, 20);

        chkShowItemDtlsForChangeCustomerOnBill.setOpaque(false);
        panelPOSSetup3.add(chkShowItemDtlsForChangeCustomerOnBill);
        chkShowItemDtlsForChangeCustomerOnBill.setBounds(610, 350, 20, 20);

        lblDaysBeforeOrderToCancel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDaysBeforeOrderToCancel.setText("Days Before Order Can Be Cancelled                 :  ");
        panelPOSSetup3.add(lblDaysBeforeOrderToCancel);
        lblDaysBeforeOrderToCancel.setBounds(290, 50, 280, 30);

        txtDaysBeforeOrderToCancel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtDaysBeforeOrderToCancel.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        panelPOSSetup3.add(txtDaysBeforeOrderToCancel);
        txtDaysBeforeOrderToCancel.setBounds(570, 50, 60, 30);

        lblDaysBeforeOrderToCancel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDaysBeforeOrderToCancel1.setText("Dont allow Adv Order for next how many days  :  ");
        panelPOSSetup3.add(lblDaysBeforeOrderToCancel1);
        lblDaysBeforeOrderToCancel1.setBounds(290, 90, 280, 30);

        txtNoOfDelDaysForAdvOrder.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNoOfDelDaysForAdvOrder.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        panelPOSSetup3.add(txtNoOfDelDaysForAdvOrder);
        txtNoOfDelDaysForAdvOrder.setBounds(570, 90, 60, 30);

        lblDaysBeforeOrderToCancel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDaysBeforeOrderToCancel2.setText("Allow Urgent Order for next how many days     :  ");
        panelPOSSetup3.add(lblDaysBeforeOrderToCancel2);
        lblDaysBeforeOrderToCancel2.setBounds(290, 130, 280, 30);

        txtNoOfDelDaysForUrgentOrder.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNoOfDelDaysForUrgentOrder.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        panelPOSSetup3.add(txtNoOfDelDaysForUrgentOrder);
        txtNoOfDelDaysForUrgentOrder.setBounds(570, 130, 60, 30);

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel21.setText("UpTo Time To Punch Adv Order");
        panelPOSSetup3.add(jLabel21);
        jLabel21.setBounds(290, 200, 180, 30);

        cmbHours.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "HH", "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        panelPOSSetup3.add(cmbHours);
        cmbHours.setBounds(490, 200, 50, 30);

        cmbMinutes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "MM", "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        panelPOSSetup3.add(cmbMinutes);
        cmbMinutes.setBounds(540, 200, 60, 30);

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel22.setText("UpTo Time To Punch Urgent Order");
        panelPOSSetup3.add(jLabel22);
        jLabel22.setBounds(290, 270, 200, 30);

        cmbHoursUrgentOrder.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "HH", "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        panelPOSSetup3.add(cmbHoursUrgentOrder);
        cmbHoursUrgentOrder.setBounds(490, 270, 50, 30);

        cmbMinutesUrgentOrder.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "MM", "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        panelPOSSetup3.add(cmbMinutesUrgentOrder);
        cmbMinutesUrgentOrder.setBounds(540, 270, 60, 30);

        lbSetUpToTimeForAdvOrder.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbSetUpToTimeForAdvOrder.setText("Set UpTo Time For Adv Order           ");
        panelPOSSetup3.add(lbSetUpToTimeForAdvOrder);
        lbSetUpToTimeForAdvOrder.setBounds(290, 170, 280, 20);

        chkSetUpToTimeForAdvOrder.setOpaque(false);
        panelPOSSetup3.add(chkSetUpToTimeForAdvOrder);
        chkSetUpToTimeForAdvOrder.setBounds(610, 170, 20, 21);

        chkSetUpToTimeForUrgentOrder.setOpaque(false);
        panelPOSSetup3.add(chkSetUpToTimeForUrgentOrder);
        chkSetUpToTimeForUrgentOrder.setBounds(610, 240, 21, 21);

        lbSetUpToTimeForAdvOrder1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbSetUpToTimeForAdvOrder1.setText("Set UpTo Time For Urgent Order        ");
        panelPOSSetup3.add(lbSetUpToTimeForAdvOrder1);
        lbSetUpToTimeForAdvOrder1.setBounds(290, 240, 290, 20);

        cmbAMPMUrgent.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "AM", "PM" }));
        panelPOSSetup3.add(cmbAMPMUrgent);
        cmbAMPMUrgent.setBounds(600, 270, 50, 30);

        cmbAMPM.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "AM", "PM" }));
        panelPOSSetup3.add(cmbAMPM);
        cmbAMPM.setBounds(600, 200, 50, 30);

        lblEnableBothPrintAndSettleBtnForDB.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblEnableBothPrintAndSettleBtnForDB.setText("Enable Both Print And Settle Btn For DB");
        panelPOSSetup3.add(lblEnableBothPrintAndSettleBtnForDB);
        lblEnableBothPrintAndSettleBtnForDB.setBounds(0, 310, 221, 20);

        chkEnableBothPrintAndSettleBtnForDB.setOpaque(false);
        panelPOSSetup3.add(chkEnableBothPrintAndSettleBtnForDB);
        chkEnableBothPrintAndSettleBtnForDB.setBounds(230, 310, 20, 21);

        lblCarryFwdFloatAmt.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCarryFwdFloatAmt.setText("Carry Forward Float Amt to Next Day    :");
        panelPOSSetup3.add(lblCarryFwdFloatAmt);
        lblCarryFwdFloatAmt.setBounds(290, 320, 230, 20);

        chkCarryForwardFloatAmtToNextDay.setOpaque(false);
        panelPOSSetup3.add(chkCarryForwardFloatAmtToNextDay);
        chkCarryForwardFloatAmtToNextDay.setBounds(610, 320, 20, 21);

        lblOpenCashDrawerAfterBillPrint.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblOpenCashDrawerAfterBillPrint.setText("Open Cash Drawer After Bill Print          : ");
        panelPOSSetup3.add(lblOpenCashDrawerAfterBillPrint);
        lblOpenCashDrawerAfterBillPrint.setBounds(0, 340, 228, 20);

        chkOpenCashDrawerAfterBillPrint.setSelected(true);
        chkOpenCashDrawerAfterBillPrint.setOpaque(false);
        panelPOSSetup3.add(chkOpenCashDrawerAfterBillPrint);
        chkOpenCashDrawerAfterBillPrint.setBounds(230, 340, 20, 21);

        chkPropertyWiseSalesOrder.setOpaque(false);
        panelPOSSetup3.add(chkPropertyWiseSalesOrder);
        chkPropertyWiseSalesOrder.setBounds(230, 190, 20, 21);

        lblPropertyWiseSalesOrder.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPropertyWiseSalesOrder.setText("Property Wise Sales Order                   : ");
        panelPOSSetup3.add(lblPropertyWiseSalesOrder);
        lblPropertyWiseSalesOrder.setBounds(0, 190, 227, 20);

        lblPrintTimeOnBill.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPrintTimeOnBill.setText("Print Time On Bill                                : ");
        panelPOSSetup3.add(lblPrintTimeOnBill);
        lblPrintTimeOnBill.setBounds(0, 250, 229, 20);

        chkPrintTimeOnBill.setOpaque(false);
        panelPOSSetup3.add(chkPrintTimeOnBill);
        chkPrintTimeOnBill.setBounds(230, 250, 20, 21);

        lblShowPopUpForNextItemQuantity.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblShowPopUpForNextItemQuantity.setText("Show Pop Up For Next Item Quantity                    ");
        panelPOSSetup3.add(lblShowPopUpForNextItemQuantity);
        lblShowPopUpForNextItemQuantity.setBounds(290, 380, 300, 20);

        chkShowPopUpForNextItemQuantity.setOpaque(false);
        panelPOSSetup3.add(chkShowPopUpForNextItemQuantity);
        chkShowPopUpForNextItemQuantity.setBounds(610, 380, 20, 20);

        lblNewBillSeriesForNewDay.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblNewBillSeriesForNewDay.setText("New Bill Series For New Day                 :");
        panelPOSSetup3.add(lblNewBillSeriesForNewDay);
        lblNewBillSeriesForNewDay.setBounds(0, 395, 230, 20);

        chkNewBillSeriesForNewDay.setOpaque(false);
        panelPOSSetup3.add(chkNewBillSeriesForNewDay);
        chkNewBillSeriesForNewDay.setBounds(230, 390, 20, 30);

        lbShowOnlyLoginPOSReports.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbShowOnlyLoginPOSReports.setText("Show Only Login POS Reports              :");
        panelPOSSetup3.add(lbShowOnlyLoginPOSReports);
        lbShowOnlyLoginPOSReports.setBounds(0, 420, 230, 20);

        chkShowOnlyLoginPOSReports.setOpaque(false);
        panelPOSSetup3.add(chkShowOnlyLoginPOSReports);
        chkShowOnlyLoginPOSReports.setBounds(230, 420, 20, 21);

        lblEnabledDineIn.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblEnabledDineIn.setText("Enable Dine In");
        panelPOSSetup3.add(lblEnabledDineIn);
        lblEnabledDineIn.setBounds(290, 410, 300, 20);

        chkEnableDineIn.setSelected(true);
        chkEnableDineIn.setOpaque(false);
        panelPOSSetup3.add(chkEnableDineIn);
        chkEnableDineIn.setBounds(610, 410, 20, 21);

        chkAutoAreaSelectionInMakeKOT.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkAutoAreaSelectionInMakeKOT.setText("Auto Area Selection In Make KOT                                 ");
        chkAutoAreaSelectionInMakeKOT.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkAutoAreaSelectionInMakeKOT.setOpaque(false);
        chkAutoAreaSelectionInMakeKOT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkAutoAreaSelectionInMakeKOTActionPerformed(evt);
            }
        });
        panelPOSSetup3.add(chkAutoAreaSelectionInMakeKOT);
        chkAutoAreaSelectionInMakeKOT.setBounds(290, 440, 343, 20);

        lblRoundingOffTo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblRoundingOffTo.setText("Rounding Off To");
        panelPOSSetup3.add(lblRoundingOffTo);
        lblRoundingOffTo.setBounds(0, 450, 190, 30);

        txtRoundingOffTo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtRoundingOffTo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtRoundingOffTo.setText("0.00");
        panelPOSSetup3.add(txtRoundingOffTo);
        txtRoundingOffTo.setBounds(210, 450, 40, 30);

        chkShowUnSettlementForm.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkShowUnSettlementForm.setSelected(true);
        chkShowUnSettlementForm.setText("Show UnSettlement Form                                            ");
        chkShowUnSettlementForm.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkShowUnSettlementForm.setOpaque(false);
        chkShowUnSettlementForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkShowUnSettlementFormActionPerformed(evt);
            }
        });
        panelPOSSetup3.add(chkShowUnSettlementForm);
        chkShowUnSettlementForm.setBounds(290, 470, 345, 20);

        chkPrintOpenItemsOnBill.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkPrintOpenItemsOnBill.setSelected(true);
        chkPrintOpenItemsOnBill.setText("Print Open Items  On Bill                      ");
        chkPrintOpenItemsOnBill.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkPrintOpenItemsOnBill.setMargin(new java.awt.Insets(0, 2, 2, 2));
        chkPrintOpenItemsOnBill.setOpaque(false);
        chkPrintOpenItemsOnBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkPrintOpenItemsOnBillActionPerformed(evt);
            }
        });
        panelPOSSetup3.add(chkPrintOpenItemsOnBill);
        chkPrintOpenItemsOnBill.setBounds(0, 490, 250, 20);

        lblDays.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDays.setText("Days");
        panelPOSSetup3.add(lblDays);
        lblDays.setBounds(560, 500, 30, 30);

        txtShowPopularItemsOfNDays.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtShowPopularItemsOfNDays.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtShowPopularItemsOfNDays.setText("1");
        panelPOSSetup3.add(txtShowPopularItemsOfNDays);
        txtShowPopularItemsOfNDays.setBounds(510, 500, 40, 30);

        lblAutoShowPopularItemsOf.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblAutoShowPopularItemsOf.setText("Auto Show Popular Items For Last");
        panelPOSSetup3.add(lblAutoShowPopularItemsOf);
        lblAutoShowPopularItemsOf.setBounds(290, 500, 220, 30);

        chkAutoShowPopItems.setOpaque(false);
        panelPOSSetup3.add(chkAutoShowPopItems);
        chkAutoShowPopItems.setBounds(610, 500, 21, 30);

        tabbedPane.addTab("POS Setup 3", panelPOSSetup3);

        panelPOSSetup4.setBackground(new java.awt.Color(153, 204, 255));
        panelPOSSetup4.setOpaque(false);

        chkAreaWisePricing.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkAreaWisePricing.setText("Area Wise Pricing                   :");
        chkAreaWisePricing.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkAreaWisePricing.setOpaque(false);
        chkAreaWisePricing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkAreaWisePricingActionPerformed(evt);
            }
        });

        lblDineInAreaForDirectBiller.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDineInAreaForDirectBiller.setText("Dine In Area For Direct Biller");

        lblHomeDeliAreaForDirectBiller.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblHomeDeliAreaForDirectBiller.setText("Home Delivery Area For Direct Biller");

        lblTakeAwayAreaForDirectBiller.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTakeAwayAreaForDirectBiller.setText("Take Away Area For Direct Biller");

        chkRoundOffBillAmount.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkRoundOffBillAmount.setText("Round Off Bill Final Amount       :");
        chkRoundOffBillAmount.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkRoundOffBillAmount.setOpaque(false);
        chkRoundOffBillAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkRoundOffBillAmountActionPerformed(evt);
            }
        });

        txtNoOfDecimalPlaces.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtNoOfDecimalPlaces.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtNoOfDecimalPlaces.setText("2");
        txtNoOfDecimalPlaces.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNoOfDecimalPlacesActionPerformed(evt);
            }
        });

        lblNoodDecimalPlaces.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblNoodDecimalPlaces.setText(" No Of Decimal Places");

        chkSendDBBackupOnMail.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkSendDBBackupOnMail.setText("Send Database Backup On Mail :  ");
        chkSendDBBackupOnMail.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkSendDBBackupOnMail.setOpaque(false);
        chkSendDBBackupOnMail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSendDBBackupOnMailActionPerformed(evt);
            }
        });

        chkPrintOrderNoOnBill.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkPrintOrderNoOnBill.setText("Print Order No On Bill              :  ");
        chkPrintOrderNoOnBill.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkPrintOrderNoOnBill.setOpaque(false);
        chkPrintOrderNoOnBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkPrintOrderNoOnBillActionPerformed(evt);
            }
        });

        chkPrintDeviceUserDtlOnKOT.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkPrintDeviceUserDtlOnKOT.setText("Print Device,User Detail on KOT:  ");
        chkPrintDeviceUserDtlOnKOT.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkPrintDeviceUserDtlOnKOT.setOpaque(false);
        chkPrintDeviceUserDtlOnKOT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkPrintDeviceUserDtlOnKOTActionPerformed(evt);
            }
        });

        lblRemoveServiceChargeTaxCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblRemoveServiceChargeTaxCode.setText("Service Charge Tax");

        chkAutoAddKOTToBill.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkAutoAddKOTToBill.setText("Auto Add KOT To Bill              :  ");
        chkAutoAddKOTToBill.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkAutoAddKOTToBill.setOpaque(false);
        chkAutoAddKOTToBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkAutoAddKOTToBillActionPerformed(evt);
            }
        });

        chkAreaWiseCostCenterKOTPrinting.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkAreaWiseCostCenterKOTPrinting.setText("Area Wise Cost Center KOT Printing :");
        chkAreaWiseCostCenterKOTPrinting.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkAreaWiseCostCenterKOTPrinting.setOpaque(false);
        chkAreaWiseCostCenterKOTPrinting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkAreaWiseCostCenterKOTPrintingActionPerformed(evt);
            }
        });

        chkFireCommunication.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkFireCommunication.setText("Fire Communication                 :  ");
        chkFireCommunication.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkFireCommunication.setOpaque(false);
        chkFireCommunication.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkFireCommunicationActionPerformed(evt);
            }
        });

        lblUSDCrrencyConverionRate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblUSDCrrencyConverionRate.setText("USD Currency Convertion Rate  : ");

        txtUSDCrrencyConverionRate.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtUSDCrrencyConverionRate.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtUSDCrrencyConverionRate.setText("0.00");
        txtUSDCrrencyConverionRate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUSDCrrencyConverionRateActionPerformed(evt);
            }
        });

        chkPrintItemsOnMoveKOTMoveTable.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkPrintItemsOnMoveKOTMoveTable.setText("Print Items On Move KOT,Move Table,Move KOT Items  :");
        chkPrintItemsOnMoveKOTMoveTable.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkPrintItemsOnMoveKOTMoveTable.setOpaque(false);
        chkPrintItemsOnMoveKOTMoveTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkPrintItemsOnMoveKOTMoveTableActionPerformed(evt);
            }
        });

        chkPrintMoveTableMoveKOT.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkPrintMoveTableMoveKOT.setSelected(true);
        chkPrintMoveTableMoveKOT.setText("Print Move Table,Move KOT  :");
        chkPrintMoveTableMoveKOT.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkPrintMoveTableMoveKOT.setOpaque(false);
        chkPrintMoveTableMoveKOT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkPrintMoveTableMoveKOTActionPerformed(evt);
            }
        });

        chkPrintQtyTotal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkPrintQtyTotal.setSelected(true);
        chkPrintQtyTotal.setText("Print Quantity Total  :");
        chkPrintQtyTotal.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkPrintQtyTotal.setOpaque(false);
        chkPrintQtyTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkPrintQtyTotalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelPOSSetup4Layout = new javax.swing.GroupLayout(panelPOSSetup4);
        panelPOSSetup4.setLayout(panelPOSSetup4Layout);
        panelPOSSetup4Layout.setHorizontalGroup(
            panelPOSSetup4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPOSSetup4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPOSSetup4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelPOSSetup4Layout.createSequentialGroup()
                        .addGroup(panelPOSSetup4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(panelPOSSetup4Layout.createSequentialGroup()
                                .addComponent(lblNoodDecimalPlaces)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtNoOfDecimalPlaces, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelPOSSetup4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblHomeDeliAreaForDirectBiller, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblDineInAreaForDirectBiller, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(chkAreaWisePricing)
                                .addGroup(panelPOSSetup4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(chkRoundOffBillAmount, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblTakeAwayAreaForDirectBiller, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelPOSSetup4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelPOSSetup4Layout.createSequentialGroup()
                                .addGroup(panelPOSSetup4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmbTakeAwayAreaForDirectBiller, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(panelPOSSetup4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(cmbDineInAreaForDirectBiller, 0, 154, Short.MAX_VALUE)
                                        .addComponent(cmbHomeDeliAreaForDirectBiller, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(chkAreaWiseCostCenterKOTPrinting))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPOSSetup4Layout.createSequentialGroup()
                                .addGap(0, 90, Short.MAX_VALUE)
                                .addGroup(panelPOSSetup4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(chkPrintItemsOnMoveKOTMoveTable, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(chkPrintMoveTableMoveKOT, javax.swing.GroupLayout.Alignment.TRAILING)))))
                    .addGroup(panelPOSSetup4Layout.createSequentialGroup()
                        .addGroup(panelPOSSetup4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chkFireCommunication)
                            .addComponent(chkPrintOrderNoOnBill)
                            .addComponent(chkPrintDeviceUserDtlOnKOT)
                            .addComponent(chkAutoAddKOTToBill)
                            .addGroup(panelPOSSetup4Layout.createSequentialGroup()
                                .addComponent(lblRemoveServiceChargeTaxCode, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cmbRemoveServiceChargeTaxCode, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelPOSSetup4Layout.createSequentialGroup()
                                .addComponent(lblUSDCrrencyConverionRate)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtUSDCrrencyConverionRate, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelPOSSetup4Layout.createSequentialGroup()
                        .addComponent(chkSendDBBackupOnMail)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(chkPrintQtyTotal)))
                .addContainerGap())
        );
        panelPOSSetup4Layout.setVerticalGroup(
            panelPOSSetup4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPOSSetup4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPOSSetup4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkAreaWisePricing, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkAreaWiseCostCenterKOTPrinting, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelPOSSetup4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmbDineInAreaForDirectBiller, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(lblDineInAreaForDirectBiller, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelPOSSetup4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmbHomeDeliAreaForDirectBiller)
                    .addComponent(lblHomeDeliAreaForDirectBiller, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelPOSSetup4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmbTakeAwayAreaForDirectBiller)
                    .addComponent(lblTakeAwayAreaForDirectBiller, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelPOSSetup4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkRoundOffBillAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkPrintItemsOnMoveKOTMoveTable, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelPOSSetup4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNoodDecimalPlaces, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNoOfDecimalPlaces, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkPrintMoveTableMoveKOT, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelPOSSetup4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkSendDBBackupOnMail, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkPrintQtyTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chkPrintOrderNoOnBill, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chkPrintDeviceUserDtlOnKOT, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelPOSSetup4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmbRemoveServiceChargeTaxCode)
                    .addComponent(lblRemoveServiceChargeTaxCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chkAutoAddKOTToBill, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chkFireCommunication, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelPOSSetup4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblUSDCrrencyConverionRate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtUSDCrrencyConverionRate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(57, Short.MAX_VALUE))
        );

        tabbedPane.addTab("POS Setup 4", panelPOSSetup4);

        panelEmailSetup.setBackground(new java.awt.Color(153, 204, 255));
        panelEmailSetup.setOpaque(false);

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setText("Sender Email Id");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setText("Password");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setText("Confirm Password");

        txtSenderEmailId.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtSenderEmailIdMouseClicked(evt);
            }
        });

        txtEmailPassword.setText("jPasswordField1");
        txtEmailPassword.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtEmailPasswordMouseClicked(evt);
            }
        });

        txtConfirmEmailPassword.setText("jPasswordField2");
        txtConfirmEmailPassword.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtConfirmEmailPasswordMouseClicked(evt);
            }
        });

        txtBodyPart.setColumns(20);
        txtBodyPart.setRows(5);
        txtBodyPart.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtBodyPartMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(txtBodyPart);

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel9.setText("Mail Body ");

        cmbServerName.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Gmail", "Yahoo Mail" }));

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel10.setText("SMTP Server Name");

        lblReceiverEmailId.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblReceiverEmailId.setText("Receiver Email Id");

        txtReceiverEmailId.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtReceiverEmailIdMouseClicked(evt);
            }
        });

        btnTestEmail.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnTestEmail.setForeground(new java.awt.Color(255, 255, 255));
        btnTestEmail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgBackspaceBtn1.png"))); // NOI18N
        btnTestEmail.setText("<html>SEND TEST EMAIL</html>");
        btnTestEmail.setToolTipText("Save Cost Center Master");
        btnTestEmail.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTestEmail.setInheritsPopupMenu(true);
        btnTestEmail.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgBackspaceBtn2.png"))); // NOI18N
        btnTestEmail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTestEmailMouseClicked(evt);
            }
        });
        btnTestEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTestEmailActionPerformed(evt);
            }
        });
        btnTestEmail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnTestEmailKeyPressed(evt);
            }
        });

        lblDBBackupReceiverEmailId.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDBBackupReceiverEmailId.setText("DB Backup Receiver ");

        txtDBBackupReceiverEmailId.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtDBBackupReceiverEmailIdMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelEmailSetupLayout = new javax.swing.GroupLayout(panelEmailSetup);
        panelEmailSetup.setLayout(panelEmailSetupLayout);
        panelEmailSetupLayout.setHorizontalGroup(
            panelEmailSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelEmailSetupLayout.createSequentialGroup()
                .addGroup(panelEmailSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelEmailSetupLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnTestEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelEmailSetupLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(panelEmailSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(panelEmailSetupLayout.createSequentialGroup()
                                .addComponent(lblReceiverEmailId, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(lblDBBackupReceiverEmailId, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelEmailSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtDBBackupReceiverEmailId)
                            .addComponent(txtSenderEmailId, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEmailPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbServerName, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtConfirmEmailPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE)
                            .addComponent(txtReceiverEmailId))))
                .addContainerGap())
        );
        panelEmailSetupLayout.setVerticalGroup(
            panelEmailSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEmailSetupLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(panelEmailSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtSenderEmailId)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelEmailSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtEmailPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addGroup(panelEmailSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtConfirmEmailPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelEmailSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbServerName, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelEmailSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtReceiverEmailId)
                    .addComponent(lblReceiverEmailId, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelEmailSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblDBBackupReceiverEmailId, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(txtDBBackupReceiverEmailId))
                .addGroup(panelEmailSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelEmailSetupLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                        .addGap(199, 199, 199))
                    .addGroup(panelEmailSetupLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addComponent(btnTestEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabbedPane.addTab("Email Setup", panelEmailSetup);

        panelRFIDSetup.setBackground(new java.awt.Color(153, 204, 255));
        panelRFIDSetup.setOpaque(false);

        lblRFIDYN.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblRFIDYN.setText("Card Interface");

        lblServerName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblServerName.setText("Server Name");

        lblUserName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblUserName.setText("User Name");

        cmbRFIDSetup.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbRFIDSetup.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "No", "Yes" }));
        cmbRFIDSetup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbRFIDSetupActionPerformed(evt);
            }
        });

        txtServerName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtServerName.setText(" ");
        txtServerName.setEnabled(false);
        txtServerName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtServerNameMouseClicked(evt);
            }
        });
        txtServerName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtServerNameKeyPressed(evt);
            }
        });

        txtUserName.setEnabled(false);
        txtUserName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtUserNameMouseClicked(evt);
            }
        });
        txtUserName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUserNameKeyPressed(evt);
            }
        });

        lblPassword.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPassword.setText("Password");

        txtPassword.setEnabled(false);
        txtPassword.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPasswordMouseClicked(evt);
            }
        });
        txtPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPasswordKeyPressed(evt);
            }
        });

        lblDBName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDBName.setText("Database Name");

        txtDatabaseName.setEnabled(false);
        txtDatabaseName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtDatabaseNameMouseClicked(evt);
            }
        });
        txtDatabaseName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDatabaseNameKeyPressed(evt);
            }
        });

        lblCardIntfType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCardIntfType.setText("Card Interface Type");

        cmbCardIntfType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbCardIntfType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Customer Card", "Member Card" }));
        cmbCardIntfType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCardIntfTypeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelRFIDSetupLayout = new javax.swing.GroupLayout(panelRFIDSetup);
        panelRFIDSetup.setLayout(panelRFIDSetupLayout);
        panelRFIDSetupLayout.setHorizontalGroup(
            panelRFIDSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRFIDSetupLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(panelRFIDSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelRFIDSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelRFIDSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblServerName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblRFIDYN, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(lblUserName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblDBName, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCardIntfType, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelRFIDSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtServerName, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                    .addComponent(txtUserName, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                    .addComponent(txtPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                    .addComponent(txtDatabaseName, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                    .addComponent(cmbCardIntfType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbRFIDSetup, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(281, Short.MAX_VALUE))
        );
        panelRFIDSetupLayout.setVerticalGroup(
            panelRFIDSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRFIDSetupLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(panelRFIDSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCardIntfType, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbCardIntfType, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelRFIDSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRFIDYN, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbRFIDSetup, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelRFIDSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtServerName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblServerName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelRFIDSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelRFIDSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelRFIDSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDBName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDatabaseName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(402, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Card Interface", panelRFIDSetup);

        panelPoints.setBackground(new java.awt.Color(153, 204, 255));
        panelPoints.setOpaque(false);

        txtGetWebservice.setEnabled(false);

        lblGetWebServiceURL.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblGetWebServiceURL.setText("SQY WebService URL(GET)  :");

        lblPostWebServiceURL.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPostWebServiceURL.setText("SQY WebService URL(POST):");

        txtPostWebservice.setEnabled(false);

        lblOutletUID.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblOutletUID.setText("SQY Outlet UID                  :");

        txtOutletUID.setEnabled(false);

        txtPOSID.setEnabled(false);

        lblPOSID.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPOSID.setText("SQY POS ID :");

        cmbCRMType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SQY CRM Interface", "PMAM CRM Interface", "JPOS CRM Interface", "HASH TAG CRM Interface" }));
        cmbCRMType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCRMTypeActionPerformed(evt);
            }
        });

        lblCRMInterface.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCRMInterface.setText("CRM Inteface                      :");

        javax.swing.GroupLayout panelPointsLayout = new javax.swing.GroupLayout(panelPoints);
        panelPoints.setLayout(panelPointsLayout);
        panelPointsLayout.setHorizontalGroup(
            panelPointsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPointsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPointsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblOutletUID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblPostWebServiceURL, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblGetWebServiceURL, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblCRMInterface, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelPointsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelPointsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(panelPointsLayout.createSequentialGroup()
                            .addComponent(txtOutletUID, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(lblPOSID)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(txtPOSID))
                        .addComponent(txtPostWebservice, javax.swing.GroupLayout.PREFERRED_SIZE, 431, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtGetWebservice, javax.swing.GroupLayout.PREFERRED_SIZE, 431, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cmbCRMType, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(91, 91, 91))
        );
        panelPointsLayout.setVerticalGroup(
            panelPointsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPointsLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(panelPointsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCRMInterface, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelPointsLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(cmbCRMType, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(panelPointsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblGetWebServiceURL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtGetWebservice, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelPointsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtPostWebservice, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPostWebServiceURL, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelPointsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblOutletUID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtPOSID, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPointsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtOutletUID, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblPOSID, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(445, Short.MAX_VALUE))
        );

        tabbedPane.addTab("CRM Interface", panelPoints);

        panelSMSSetup.setBackground(new java.awt.Color(153, 204, 255));
        panelSMSSetup.setOpaque(false);

        txtAreaSMSApi.setColumns(20);
        txtAreaSMSApi.setRows(5);
        jScrollPane3.setViewportView(txtAreaSMSApi);

        lblSMSApi.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblSMSApi.setText("SMS API");

        lblSendHomeDelivery.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblSendHomeDelivery.setForeground(new java.awt.Color(0, 51, 204));
        lblSendHomeDelivery.setText("HOME DELIVERY SMS");

        cmbSendHomeDelivery.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "BILL NO", "CUSTOMER NAME", "DATE", "DELIVERY BOY", "ITEMS", "BILL AMT", "USER", "TIME" }));
        cmbSendHomeDelivery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSendHomeDeliveryActionPerformed(evt);
            }
        });

        btnShiftSendHomeDelievery.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnShiftSendHomeDelievery.setForeground(new java.awt.Color(255, 255, 255));
        btnShiftSendHomeDelievery.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn1.png"))); // NOI18N
        btnShiftSendHomeDelievery.setText(">>");
        btnShiftSendHomeDelievery.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnShiftSendHomeDelievery.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn2.png"))); // NOI18N
        btnShiftSendHomeDelievery.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnShiftSendHomeDelieveryMouseClicked(evt);
            }
        });

        lblBillSettlementSMS.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblBillSettlementSMS.setForeground(new java.awt.Color(0, 51, 204));
        lblBillSettlementSMS.setText("BILL SETTLEMENT SMS");

        cmbBillSettlement.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "BILL NO", "CUSTOMER NAME", "DATE", "DELIVERY BOY", "ITEMS", "BILL AMT", "USER", "TIME" }));
        cmbBillSettlement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbBillSettlementActionPerformed(evt);
            }
        });

        btnShiftBillSettlement.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnShiftBillSettlement.setForeground(new java.awt.Color(255, 255, 255));
        btnShiftBillSettlement.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn1.png"))); // NOI18N
        btnShiftBillSettlement.setText(">>");
        btnShiftBillSettlement.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnShiftBillSettlement.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn2.png"))); // NOI18N
        btnShiftBillSettlement.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnShiftBillSettlementMouseClicked(evt);
            }
        });
        btnShiftBillSettlement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShiftBillSettlementActionPerformed(evt);
            }
        });

        txtAreaSendHomeDeliverySMS.setColumns(20);
        txtAreaSendHomeDeliverySMS.setRows(5);
        txtAreaSendHomeDeliverySMS.setText("\n");
        jScrollPane4.setViewportView(txtAreaSendHomeDeliverySMS);

        txtAreaBillSettlementSMS.setColumns(20);
        txtAreaBillSettlementSMS.setRows(5);
        jScrollPane5.setViewportView(txtAreaBillSettlementSMS);

        chkHomeDelSMS.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkHomeDelSMS.setText("Home Delivery SMS  ");
        chkHomeDelSMS.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkHomeDelSMS.setOpaque(false);
        chkHomeDelSMS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkHomeDelSMSActionPerformed(evt);
            }
        });

        chkBillSettlementSMS.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkBillSettlementSMS.setText("Bill Settlement SMS  ");
        chkBillSettlementSMS.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkBillSettlementSMS.setOpaque(false);
        chkBillSettlementSMS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkBillSettlementSMSActionPerformed(evt);
            }
        });

        cmbSMSType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SANGUINE", "SINFINI", "CELLX", "INFYFLYER" }));
        cmbSMSType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSMSTypeActionPerformed(evt);
            }
        });

        lblSMSType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblSMSType.setText("SMS Type");

        txtSMSMobileNo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCommonBtnLong1.png"))); // NOI18N
        jButton1.setText("Send Test SMS");
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCommonBtnLong2.png"))); // NOI18N
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        lblSMSApi1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblSMSApi1.setText("Mobile Nos.");

        chkDayEndSMSYN.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkDayEndSMSYN.setText("Day End :");
        chkDayEndSMSYN.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkDayEndSMSYN.setOpaque(false);
        chkDayEndSMSYN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkDayEndSMSYNActionPerformed(evt);
            }
        });

        chkVoidKOTSMSYN.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkVoidKOTSMSYN.setText("Void KOT :");
        chkVoidKOTSMSYN.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkVoidKOTSMSYN.setOpaque(false);
        chkVoidKOTSMSYN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkVoidKOTSMSYNActionPerformed(evt);
            }
        });

        chkNCKOTSMSYN.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkNCKOTSMSYN.setText("NC KOT :");
        chkNCKOTSMSYN.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkNCKOTSMSYN.setOpaque(false);
        chkNCKOTSMSYN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkNCKOTSMSYNActionPerformed(evt);
            }
        });

        chkVoidBillSMSYN.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkVoidBillSMSYN.setText("Void Bill :");
        chkVoidBillSMSYN.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkVoidBillSMSYN.setOpaque(false);
        chkVoidBillSMSYN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkVoidBillSMSYNActionPerformed(evt);
            }
        });

        chkModifyBillSMSYN.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkModifyBillSMSYN.setText("Modify Bill :");
        chkModifyBillSMSYN.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkModifyBillSMSYN.setOpaque(false);
        chkModifyBillSMSYN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkModifyBillSMSYNActionPerformed(evt);
            }
        });

        chkUnsettleBillSMSYN.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkUnsettleBillSMSYN.setText("Unsettle Bill :");
        chkUnsettleBillSMSYN.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkUnsettleBillSMSYN.setOpaque(false);
        chkUnsettleBillSMSYN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkUnsettleBillSMSYNActionPerformed(evt);
            }
        });

        chkComplementaryBillSMSYN.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkComplementaryBillSMSYN.setText("Complemetary Bill :");
        chkComplementaryBillSMSYN.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkComplementaryBillSMSYN.setOpaque(false);
        chkComplementaryBillSMSYN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkComplementaryBillSMSYNActionPerformed(evt);
            }
        });

        chkVoidAdvOrderSMSYN.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkVoidAdvOrderSMSYN.setText("Void Advance Order :");
        chkVoidAdvOrderSMSYN.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkVoidAdvOrderSMSYN.setOpaque(false);
        chkVoidAdvOrderSMSYN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkVoidAdvOrderSMSYNActionPerformed(evt);
            }
        });

        lblAuditSMS.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblAuditSMS.setForeground(new java.awt.Color(0, 51, 204));
        lblAuditSMS.setText("AUDIT SMS");

        javax.swing.GroupLayout panelSMSSetupLayout = new javax.swing.GroupLayout(panelSMSSetup);
        panelSMSSetup.setLayout(panelSMSSetupLayout);
        panelSMSSetupLayout.setHorizontalGroup(
            panelSMSSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSMSSetupLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSMSSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelSMSSetupLayout.createSequentialGroup()
                        .addGroup(panelSMSSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelSMSSetupLayout.createSequentialGroup()
                                .addGroup(panelSMSSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblSMSApi, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblSMSType, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(52, 52, 52))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelSMSSetupLayout.createSequentialGroup()
                                .addComponent(lblSMSApi1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(panelSMSSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelSMSSetupLayout.createSequentialGroup()
                                .addComponent(cmbSMSType, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane3)
                            .addGroup(panelSMSSetupLayout.createSequentialGroup()
                                .addComponent(txtSMSMobileNo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(panelSMSSetupLayout.createSequentialGroup()
                        .addGroup(panelSMSSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblBillSettlementSMS, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbBillSettlement, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chkBillSettlementSMS)
                            .addComponent(btnShiftBillSettlement, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblAuditSMS, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbSendHomeDelivery, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnShiftSendHomeDelievery, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chkHomeDelSMS)
                            .addComponent(lblSendHomeDelivery, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelSMSSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4)
                            .addGroup(panelSMSSetupLayout.createSequentialGroup()
                                .addGroup(panelSMSSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(chkDayEndSMSYN)
                                    .addComponent(chkVoidBillSMSYN))
                                .addGap(18, 18, 18)
                                .addGroup(panelSMSSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelSMSSetupLayout.createSequentialGroup()
                                        .addComponent(chkModifyBillSMSYN)
                                        .addGap(18, 18, 18)
                                        .addComponent(chkUnsettleBillSMSYN)
                                        .addGap(18, 18, 18)
                                        .addComponent(chkComplementaryBillSMSYN))
                                    .addGroup(panelSMSSetupLayout.createSequentialGroup()
                                        .addComponent(chkVoidKOTSMSYN)
                                        .addGap(18, 18, 18)
                                        .addComponent(chkNCKOTSMSYN)
                                        .addGap(18, 18, 18)
                                        .addComponent(chkVoidAdvOrderSMSYN)))
                                .addGap(42, 42, 42))
                            .addComponent(jScrollPane5))))
                .addContainerGap())
        );
        panelSMSSetupLayout.setVerticalGroup(
            panelSMSSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSMSSetupLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSMSSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmbSMSType)
                    .addComponent(lblSMSType, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelSMSSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSMSApi, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelSMSSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSMSApi1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSMSMobileNo, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelSMSSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelSMSSetupLayout.createSequentialGroup()
                        .addComponent(lblSendHomeDelivery, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkHomeDelSMS, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbSendHomeDelivery, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnShiftSendHomeDelievery, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelSMSSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelSMSSetupLayout.createSequentialGroup()
                        .addComponent(lblBillSettlementSMS, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(chkBillSettlementSMS, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbBillSettlement, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addComponent(btnShiftBillSettlement, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelSMSSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkDayEndSMSYN, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkVoidKOTSMSYN, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkNCKOTSMSYN, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkVoidAdvOrderSMSYN, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAuditSMS, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelSMSSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkModifyBillSMSYN, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkUnsettleBillSMSYN, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkComplementaryBillSMSYN, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkVoidBillSMSYN, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13))
        );

        tabbedPane.addTab("SMS Setup", panelSMSSetup);

        panelFTPSetup.setOpaque(false);

        jLabel6.setText("FTP SERVER ADDRESS");

        txtFTPAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFTPAddressActionPerformed(evt);
            }
        });

        lblFTPServerUserName.setText("FTP SERVER USER NAME");

        jLabel7.setText("FTP SERVER PASSWORD");

        javax.swing.GroupLayout panelFTPSetupLayout = new javax.swing.GroupLayout(panelFTPSetup);
        panelFTPSetup.setLayout(panelFTPSetupLayout);
        panelFTPSetupLayout.setHorizontalGroup(
            panelFTPSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFTPSetupLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(panelFTPSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFTPSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(lblFTPServerUserName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE))
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelFTPSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtFTPAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFTPServerPass, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFTPServerUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(248, Short.MAX_VALUE))
        );
        panelFTPSetupLayout.setVerticalGroup(
            panelFTPSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFTPSetupLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(panelFTPSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFTPAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelFTPSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblFTPServerUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelFTPSetupLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(txtFTPServerUserName)))
                .addGap(18, 18, 18)
                .addGroup(panelFTPSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFTPServerPass, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(535, Short.MAX_VALUE))
        );

        tabbedPane.addTab("FTP Setup", panelFTPSetup);

        panelCMSIntegration.setOpaque(false);

        cmbCMSIntegrationYN.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbCMSIntegrationYN.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "No", "Yes" }));

        txtCMSWesServiceURL.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("CMS Integration                :");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("Web Service URL              : ");

        chkMemberAsTable.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkMemberAsTable.setText("Treat Member As Table     :");
        chkMemberAsTable.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkMemberAsTable.setOpaque(false);

        chkMemberCodeForKOTJPOS.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkMemberCodeForKOTJPOS.setText("Member Code For KOT In JPOS     :");
        chkMemberCodeForKOTJPOS.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkMemberCodeForKOTJPOS.setOpaque(false);

        chkMemberCodeForKOTMPOS.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkMemberCodeForKOTMPOS.setText("Member Code For KOT In MPOS     :");
        chkMemberCodeForKOTMPOS.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkMemberCodeForKOTMPOS.setOpaque(false);

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel14.setText("Member Code For KOT In MPOS By Card Swipe  :");

        chkMemberCodeForKotInMposByCardSwipe.setOpaque(false);

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel16.setText("Member Code For Make Bill In MPOS :");

        chkMemberCodeForMakeBillInMPOS.setOpaque(false);

        lblCMSPosting.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCMSPosting.setText("CMS Posting Type :");

        cmbCMSPostingType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Sanguine CMS", "Others" }));
        cmbCMSPostingType.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cmbCMSPostingTypeMouseClicked(evt);
            }
        });
        cmbCMSPostingType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCMSPostingTypeActionPerformed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel20.setText("Select Customer Code From Card Swipe  :");

        chkSelectCustomerCodeFromCardSwipe.setOpaque(false);

        javax.swing.GroupLayout panelCMSIntegrationLayout = new javax.swing.GroupLayout(panelCMSIntegration);
        panelCMSIntegration.setLayout(panelCMSIntegrationLayout);
        panelCMSIntegrationLayout.setHorizontalGroup(
            panelCMSIntegrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCMSIntegrationLayout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addGroup(panelCMSIntegrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCMSIntegrationLayout.createSequentialGroup()
                        .addGroup(panelCMSIntegrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelCMSIntegrationLayout.createSequentialGroup()
                                .addGroup(panelCMSIntegrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel3))
                                .addGap(37, 37, 37)
                                .addGroup(panelCMSIntegrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmbCMSIntegrationYN, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtCMSWesServiceURL, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(panelCMSIntegrationLayout.createSequentialGroup()
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chkMemberCodeForMakeBillInMPOS, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(chkMemberCodeForKOTMPOS, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelCMSIntegrationLayout.createSequentialGroup()
                                .addComponent(lblCMSPosting, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmbCMSPostingType, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelCMSIntegrationLayout.createSequentialGroup()
                                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chkSelectCustomerCodeFromCardSwipe))
                            .addComponent(chkMemberAsTable, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelCMSIntegrationLayout.createSequentialGroup()
                        .addComponent(chkMemberCodeForKOTJPOS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(32, 32, 32)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkMemberCodeForKotInMposByCardSwipe)
                        .addGap(68, 68, 68))))
        );
        panelCMSIntegrationLayout.setVerticalGroup(
            panelCMSIntegrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCMSIntegrationLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(panelCMSIntegrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbCMSIntegrationYN, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelCMSIntegrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCMSWesServiceURL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(chkMemberAsTable)
                .addGap(18, 18, 18)
                .addGroup(panelCMSIntegrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkMemberCodeForKOTJPOS)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkMemberCodeForKotInMposByCardSwipe))
                .addGap(18, 18, 18)
                .addGroup(panelCMSIntegrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelCMSIntegrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(chkMemberCodeForMakeBillInMPOS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(chkMemberCodeForKOTMPOS))
                .addGap(26, 26, 26)
                .addGroup(panelCMSIntegrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkSelectCustomerCodeFromCardSwipe, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(panelCMSIntegrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCMSPosting, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbCMSPostingType, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(368, Short.MAX_VALUE))
        );

        tabbedPane.addTab("CMS Integration", panelCMSIntegration);

        printerSetupScrollPane.setOpaque(false);

        tblPrinterSetup.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "<Html><b>Cost Center Name</html>", "<html><B>Primary Printer<html>", "<html><B>Secondary Printer</html>", "<html><B>Print On Both Printer Y/N<html>"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPrinterSetup.setFillsViewportHeight(true);
        tblPrinterSetup.setRowHeight(30);
        tblPrinterSetup.getTableHeader().setReorderingAllowed(false);
        tblPrinterSetup.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPrinterSetupMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblPrinterSetupMouseReleased(evt);
            }
        });
        printerSetupScrollPane.setViewportView(tblPrinterSetup);
        if (tblPrinterSetup.getColumnModel().getColumnCount() > 0) {
            tblPrinterSetup.getColumnModel().getColumn(0).setResizable(false);
            tblPrinterSetup.getColumnModel().getColumn(0).setPreferredWidth(150);
            tblPrinterSetup.getColumnModel().getColumn(1).setResizable(false);
            tblPrinterSetup.getColumnModel().getColumn(1).setPreferredWidth(200);
            tblPrinterSetup.getColumnModel().getColumn(2).setResizable(false);
            tblPrinterSetup.getColumnModel().getColumn(2).setPreferredWidth(200);
            tblPrinterSetup.getColumnModel().getColumn(3).setResizable(false);
            tblPrinterSetup.getColumnModel().getColumn(3).setPreferredWidth(150);
        }

        lblConsolidatedKOTPrinterPort.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblConsolidatedKOTPrinterPort.setText("Consolidated KOT Printer :");

        cmbConsolidatedKOTPrinterPort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbConsolidatedKOTPrinterPortActionPerformed(evt);
            }
        });
        cmbConsolidatedKOTPrinterPort.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbConsolidatedKOTPrinterPortKeyPressed(evt);
            }
        });

        txtConsolidatedKOTPrinterPort.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtConsolidatedKOTPrinterPortMouseClicked(evt);
            }
        });
        txtConsolidatedKOTPrinterPort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtConsolidatedKOTPrinterPortActionPerformed(evt);
            }
        });
        txtConsolidatedKOTPrinterPort.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtConsolidatedKOTPrinterPortKeyPressed(evt);
            }
        });

        btnTestConsolidatedKOTPrinterPort.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnTestConsolidatedKOTPrinterPort.setForeground(new java.awt.Color(255, 255, 255));
        btnTestConsolidatedKOTPrinterPort.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn1.png"))); // NOI18N
        btnTestConsolidatedKOTPrinterPort.setText("TEST");
        btnTestConsolidatedKOTPrinterPort.setToolTipText("Save Cost Center Master");
        btnTestConsolidatedKOTPrinterPort.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTestConsolidatedKOTPrinterPort.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn2.png"))); // NOI18N
        btnTestConsolidatedKOTPrinterPort.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTestConsolidatedKOTPrinterPortMouseClicked(evt);
            }
        });
        btnTestConsolidatedKOTPrinterPort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTestConsolidatedKOTPrinterPortActionPerformed(evt);
            }
        });
        btnTestConsolidatedKOTPrinterPort.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnTestConsolidatedKOTPrinterPortKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelPrinterSetupLayout = new javax.swing.GroupLayout(panelPrinterSetup);
        panelPrinterSetup.setLayout(panelPrinterSetupLayout);
        panelPrinterSetupLayout.setHorizontalGroup(
            panelPrinterSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(printerSetupScrollPane)
            .addGroup(panelPrinterSetupLayout.createSequentialGroup()
                .addComponent(lblConsolidatedKOTPrinterPort, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbConsolidatedKOTPrinterPort, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtConsolidatedKOTPrinterPort, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTestConsolidatedKOTPrinterPort, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 22, Short.MAX_VALUE))
        );
        panelPrinterSetupLayout.setVerticalGroup(
            panelPrinterSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPrinterSetupLayout.createSequentialGroup()
                .addComponent(printerSetupScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelPrinterSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblConsolidatedKOTPrinterPort, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbConsolidatedKOTPrinterPort, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtConsolidatedKOTPrinterPort, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTestConsolidatedKOTPrinterPort, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 89, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Printer Setup", panelPrinterSetup);

        panelDebitCardSetup.setOpaque(false);

        lblLastPOSForDayEnd.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblLastPOSForDayEnd.setText("Last POS For Day End       :");

        chkEnableNFCInterface.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkEnableNFCInterface.setText("Enable NFC Interface       :     ");
        chkEnableNFCInterface.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkEnableNFCInterface.setOpaque(false);
        chkEnableNFCInterface.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkEnableNFCInterfaceActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelDebitCardSetupLayout = new javax.swing.GroupLayout(panelDebitCardSetup);
        panelDebitCardSetup.setLayout(panelDebitCardSetupLayout);
        panelDebitCardSetupLayout.setHorizontalGroup(
            panelDebitCardSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDebitCardSetupLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDebitCardSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkEnableNFCInterface, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelDebitCardSetupLayout.createSequentialGroup()
                        .addComponent(lblLastPOSForDayEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbPOSForDayEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(251, Short.MAX_VALUE))
        );
        panelDebitCardSetupLayout.setVerticalGroup(
            panelDebitCardSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDebitCardSetupLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(panelDebitCardSetupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLastPOSForDayEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbPOSForDayEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(chkEnableNFCInterface, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(561, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Debit Card Setup", panelDebitCardSetup);

        panelInrestoIntegration.setOpaque(false);

        cmbInrestoPOSIntegrationYN.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbInrestoPOSIntegrationYN.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "No", "Yes" }));

        txtInrestoPOSWesServiceURL.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        lblInrestoIntegration.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblInrestoIntegration.setText("Inresto Integration           :");

        lblInrestoWebServiceURL.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblInrestoWebServiceURL.setText("Web Service URL              : ");

        lblInrestoPOSId.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblInrestoPOSId.setText("Inresto POS ID                 : ");

        lblInrestoPOSKey.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblInrestoPOSKey.setText("Inresto POS KEY              : ");

        txtInrestoPOSId.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtInrestoPOSKey.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtInrestoPOSKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtInrestoPOSKeyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelInrestoIntegrationLayout = new javax.swing.GroupLayout(panelInrestoIntegration);
        panelInrestoIntegration.setLayout(panelInrestoIntegrationLayout);
        panelInrestoIntegrationLayout.setHorizontalGroup(
            panelInrestoIntegrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInrestoIntegrationLayout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addGroup(panelInrestoIntegrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelInrestoIntegrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(lblInrestoIntegration, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblInrestoWebServiceURL))
                    .addComponent(lblInrestoPOSId)
                    .addComponent(lblInrestoPOSKey))
                .addGap(37, 37, 37)
                .addGroup(panelInrestoIntegrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtInrestoPOSKey, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtInrestoPOSId, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbInrestoPOSIntegrationYN, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtInrestoPOSWesServiceURL, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(77, Short.MAX_VALUE))
        );
        panelInrestoIntegrationLayout.setVerticalGroup(
            panelInrestoIntegrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInrestoIntegrationLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(panelInrestoIntegrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbInrestoPOSIntegrationYN, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblInrestoIntegration, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelInrestoIntegrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtInrestoPOSWesServiceURL, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                    .addComponent(lblInrestoWebServiceURL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(panelInrestoIntegrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblInrestoPOSId, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtInrestoPOSId, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelInrestoIntegrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblInrestoPOSKey, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtInrestoPOSKey, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(331, 331, 331))
        );

        tabbedPane.addTab("Inresto Integration", panelInrestoIntegration);

        cmbJioPOSIntegrationYN.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbJioPOSIntegrationYN.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "No", "Yes" }));

        lblInrestoIntegration1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblInrestoIntegration1.setText("JioMoney Integration    :");

        lblInrestoWebServiceURL1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblInrestoWebServiceURL1.setText("Web Service URL          : ");

        txtJioPOSWesServiceURL.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        lblJioMoneyTID.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblJioMoneyTID.setText("JioMoney Terminal ID     : ");

        lblJioMoneyMID.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblJioMoneyMID.setText("JioMoney Merchant ID   : ");

        txtJioMoneyTID.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtJioMoneyTID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtJioMoneyTIDActionPerformed(evt);
            }
        });

        txtJioMoneyMID.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtJioActivationCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtJioActivationCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtJioActivationCodeActionPerformed(evt);
            }
        });

        lblJioActivationCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblJioActivationCode.setText("Activation Code             : ");

        lblJioMoneyDeviceID.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblJioMoneyDeviceID.setText("Device ID                     : ");

        txtJioDeviceID.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtJioDeviceID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtJioDeviceIDActionPerformed(evt);
            }
        });

        btnFetch.setText("Fetch ID");
        btnFetch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnFetchMouseClicked(evt);
            }
        });
        btnFetch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFetchActionPerformed(evt);
            }
        });

        jLayeredPane1.setLayer(cmbJioPOSIntegrationYN, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(lblInrestoIntegration1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(lblInrestoWebServiceURL1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(txtJioPOSWesServiceURL, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(lblJioMoneyTID, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(lblJioMoneyMID, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(txtJioMoneyTID, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(txtJioMoneyMID, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(txtJioActivationCode, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(lblJioActivationCode, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(lblJioMoneyDeviceID, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(txtJioDeviceID, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(btnFetch, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane1Layout.createSequentialGroup()
                            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(lblInrestoIntegration1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblInrestoWebServiceURL1, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addGap(22, 22, 22))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane1Layout.createSequentialGroup()
                            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblJioMoneyMID)
                                .addComponent(lblJioMoneyTID))
                            .addGap(20, 20, 20))
                        .addGroup(jLayeredPane1Layout.createSequentialGroup()
                            .addComponent(lblJioActivationCode)
                            .addGap(6, 6, 6)))
                    .addGroup(jLayeredPane1Layout.createSequentialGroup()
                        .addComponent(lblJioMoneyDeviceID)
                        .addGap(20, 20, 20)))
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmbJioPOSIntegrationYN, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtJioPOSWesServiceURL, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtJioMoneyTID, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
                    .addComponent(txtJioMoneyMID, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
                    .addComponent(txtJioActivationCode, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane1Layout.createSequentialGroup()
                        .addComponent(txtJioDeviceID)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnFetch, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(98, Short.MAX_VALUE))
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbJioPOSIntegrationYN, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblInrestoIntegration1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtJioPOSWesServiceURL, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                    .addComponent(lblInrestoWebServiceURL1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblJioMoneyMID, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtJioMoneyMID, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblJioMoneyTID, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtJioMoneyTID, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtJioActivationCode, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblJioActivationCode, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtJioDeviceID, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnFetch, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblJioMoneyDeviceID, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(283, 283, 283))
        );

        tabbedPane.addTab("JioMoney Integration", jLayeredPane1);

        lbBenowIntegration.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbBenowIntegration.setText("Benow Integration Y/N    :");

        cmbBenowPOSIntegrationYN.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbBenowPOSIntegrationYN.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "No", "Yes" }));

        lbXEmail.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbXEmail.setText("X-Email                          : ");

        txtXEmail.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        lblMerchantCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMerchantCode.setText("Merchant Code              : ");

        txtMerchantCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtMerchantCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMerchantCodeActionPerformed(evt);
            }
        });

        lblAuthenticationKey.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblAuthenticationKey.setText("Authentication Key        : ");

        txtAuthenticationKey.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtAuthenticationKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAuthenticationKeyActionPerformed(evt);
            }
        });

        lblSalt.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblSalt.setText(" Salt                            : ");

        txtSalt.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtSalt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSaltActionPerformed(evt);
            }
        });

        lblSuperMerchantCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblSuperMerchantCode.setText("Super Merchant Code     : ");

        txtSuperMerchantCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        btnAuthorize.setText("Authenticate");
        btnAuthorize.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAuthorizeMouseClicked(evt);
            }
        });

        btnOK.setText("Ok");
        btnOK.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOKMouseClicked(evt);
            }
        });

        jLayeredPane2.setLayer(lbBenowIntegration, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane2.setLayer(cmbBenowPOSIntegrationYN, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane2.setLayer(lbXEmail, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane2.setLayer(txtXEmail, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane2.setLayer(lblMerchantCode, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane2.setLayer(txtMerchantCode, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane2.setLayer(lblAuthenticationKey, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane2.setLayer(txtAuthenticationKey, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane2.setLayer(lblSalt, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane2.setLayer(txtSalt, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane2.setLayer(lblSuperMerchantCode, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane2.setLayer(txtSuperMerchantCode, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane2.setLayer(btnAuthorize, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane2.setLayer(btnOK, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane2.setLayer(txtOTP, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane2Layout = new javax.swing.GroupLayout(jLayeredPane2);
        jLayeredPane2.setLayout(jLayeredPane2Layout);
        jLayeredPane2Layout.setHorizontalGroup(
            jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane2Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jLayeredPane2Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jLayeredPane2Layout.createSequentialGroup()
                                .addComponent(lblMerchantCode, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtMerchantCode, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jLayeredPane2Layout.createSequentialGroup()
                                .addGroup(jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblSalt, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblAuthenticationKey))
                                .addGap(18, 18, 18)
                                .addGroup(jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtSalt)
                                    .addComponent(txtAuthenticationKey, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jLayeredPane2Layout.createSequentialGroup()
                            .addComponent(lblSuperMerchantCode, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(txtSuperMerchantCode, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnAuthorize, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtOTP)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jLayeredPane2Layout.createSequentialGroup()
                            .addGroup(jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(lbBenowIntegration, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbXEmail))
                            .addGroup(jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jLayeredPane2Layout.createSequentialGroup()
                                    .addGap(20, 20, 20)
                                    .addComponent(cmbBenowPOSIntegrationYN, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 283, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jLayeredPane2Layout.createSequentialGroup()
                                    .addGap(18, 18, 18)
                                    .addComponent(txtXEmail))))))
                .addGap(213, 213, 213))
        );
        jLayeredPane2Layout.setVerticalGroup(
            jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane2Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbBenowPOSIntegrationYN, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbBenowIntegration, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtXEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbXEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtOTP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtSuperMerchantCode, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblSuperMerchantCode, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnAuthorize, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMerchantCode, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMerchantCode, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblAuthenticationKey, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAuthenticationKey, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSalt, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSalt, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(259, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Benow Integration", jLayeredPane2);

        jPanel1.setOpaque(false);

        lblWeraIntegration.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblWeraIntegration.setText("WERA Integration Y/N    :");

        cmbWeraIntegrationYN.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbWeraIntegrationYN.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "No", "Yes" }));

        lblWeraMerchantCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblWeraMerchantCode.setText("Outlet Id/WERA Merchant ");

        txtWeraMerchantOutletId.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtWeraMerchantOutletId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtWeraMerchantOutletIdActionPerformed(evt);
            }
        });

        lblAuthenticationAPIKey.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblAuthenticationAPIKey.setText("Authentication API Key   :");

        txtWeraAuthenticationAPIKey.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtWeraAuthenticationAPIKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtWeraAuthenticationAPIKeyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblWeraIntegration, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(cmbWeraIntegrationYN, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblWeraMerchantCode, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtWeraMerchantOutletId, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblAuthenticationAPIKey, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtWeraAuthenticationAPIKey, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(130, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbWeraIntegrationYN, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblWeraIntegration, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAuthenticationAPIKey, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtWeraAuthenticationAPIKey, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblWeraMerchantCode, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtWeraMerchantOutletId, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(394, Short.MAX_VALUE))
        );

        tabbedPane.addTab("<html>WERA Online<br>Order Integration</html>", jPanel1);

        btnNew.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnNew.setForeground(new java.awt.Color(255, 255, 255));
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn1.png"))); // NOI18N
        btnNew.setText("UPDATE");
        btnNew.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNew.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn2.png"))); // NOI18N
        btnNew.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNewMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnNewMouseEntered(evt);
            }
        });
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        btnExit.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnExit.setForeground(new java.awt.Color(255, 255, 255));
        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn1.png"))); // NOI18N
        btnExit.setText("CLOSE");
        btnExit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExit.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn2.png"))); // NOI18N
        btnExit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnExitMouseClicked(evt);
            }
        });
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        lblPOS.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPOS.setText("POS :");

        cmbPOS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbPOSActionPerformed(evt);
            }
        });

        btnLikePOS.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnLikePOS.setForeground(new java.awt.Color(255, 255, 255));
        btnLikePOS.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn1.png"))); // NOI18N
        btnLikePOS.setText("Like POS");
        btnLikePOS.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLikePOS.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn2.png"))); // NOI18N
        btnLikePOS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLikePOSMouseClicked(evt);
            }
        });
        btnLikePOS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLikePOSActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelFormBodyLayout = new javax.swing.GroupLayout(panelFormBody);
        panelFormBody.setLayout(panelFormBodyLayout);
        panelFormBodyLayout.setHorizontalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblPOS)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cmbPOS, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addComponent(btnLikePOS, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 155, Short.MAX_VALUE)
                .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(tabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        panelFormBodyLayout.setVerticalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormBodyLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnLikePOS, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblPOS, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbPOS, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 544, Short.MAX_VALUE))
        );

        panelMainForm.add(panelFormBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelMainForm, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewMouseClicked
	// TODO add your handling code here:
	funPropertySetupUpdate();
    }//GEN-LAST:event_btnNewMouseClicked

    private void funPropertySetupUpdate()
    {
	try
	{
	    String crmInterface = "SQY", getWebServiceURL = "", postWebServiceURL = "";
	    String outletUID = "", posID = "", stockInOption = "ItemWise";
	    Date dte = dteEndChooser.getDate();
	    int yy = dte.getYear() + 1900;
	    int mm = dte.getMonth() + 1;
	    int dd = dte.getDate();
	    dteEndDate = yy + "-" + mm + "-" + dd;
	    String theme = "";
	    double maxDiscount;
	    if (txtMaxDiscount.getText().trim().length() > 0)
	    {
		maxDiscount = Double.valueOf(txtMaxDiscount.getText());
	    }
	    else
	    {
		maxDiscount = 99.00;
		txtMaxDiscount.setText("99.00");
	    }
	    String editHomeDelivery = "N", slabBasedHomeDelCharges = "N";

	    if (cmbJioPOSIntegrationYN.getSelectedIndex() == 1)
	    {
		if (!(txtJioDeviceID.getText().toString().isEmpty()))
		{
		    if (JioDeviceIDFound && ((!txtJioDeviceID.getText().toString().equals(JioDeviceIDFromDB))))
		    {
			funSaveMapMyDevice();
		    }
		    else if (!JioDeviceIDFound)
		    {
			funSaveMapMyDevice();
		    }

		}
	    }

	    if (cmbCRMType.getSelectedIndex() == 0)
	    {
		crmInterface = "SQY";
		getWebServiceURL = txtGetWebservice.getText().trim();
		postWebServiceURL = txtPostWebservice.getText().trim();
		outletUID = txtOutletUID.getText().trim();
		posID = txtPOSID.getText().trim();
	    }
	    else if (cmbCRMType.getSelectedIndex() == 1)
	    {
		crmInterface = "PMAM";
		getWebServiceURL = txtGetWebservice.getText().trim();
		outletUID = txtOutletUID.getText().trim();
		posID = txtPOSID.getText().trim();
	    }
	    else if (cmbCRMType.getSelectedItem().toString().equalsIgnoreCase("HASH TAG CRM Interface"))
	    {
		crmInterface = "HASH TAG CRM Interface";
		getWebServiceURL = txtGetWebservice.getText().trim();
		outletUID = txtOutletUID.getText().trim();
	    }
	    else
	    {
		crmInterface = "JPOS";
	    }

	    String consolidatedKOTPrinterPort = txtConsolidatedKOTPrinterPort.getText().trim();
	    String roundOffTo = txtRoundingOffTo.getText();
	    if (!roundOffTo.matches("^[0-9]{1,18}(\\.[0-9]+)?$"))
	    {
		new frmOkPopUp(null, "Please Enter Valid Rounding Off Amount.", "Error", 1).setVisible(true);
		return;
	    }
	    String showUnSettlementForm = "Y";
	    if (chkShowUnSettlementForm.isSelected())
	    {
		showUnSettlementForm = "Y";
	    }
	    else
	    {
		showUnSettlementForm = "N";
	    }
	    String printOpenItemsOnBill = "Y";
	    if (chkPrintOpenItemsOnBill.isSelected())
	    {
		printOpenItemsOnBill = "Y";
	    }
	    else
	    {
		printOpenItemsOnBill = "N";
	    }

	    String printHomeDeliveryYN = "N";
	    if (chkPrintHomeDeliveryYN.isSelected())
	    {
		printHomeDeliveryYN = "Y";
	    }
	    else
	    {
		printHomeDeliveryYN = "N";
	    }

	    String scanQRYN = "N";
	    if (chkScanQRYN.isSelected())
	    {
		scanQRYN = "Y";
	    }
	    else
	    {
		scanQRYN = "N";
	    }

	    String areaWisePromotions = "N";
	    if (chkAreaWIsePromotions.isSelected())
	    {
		areaWisePromotions = "Y";
	    }

	    String printItemOnMoveKOTMoveTable = "N";
	    if (chkPrintItemsOnMoveKOTMoveTable.isSelected())
	    {
		printItemOnMoveKOTMoveTable = "Y";
	    }

	    String showPurRateInDirectBiller = "N";
	    if (chkShowPurchaseRateInDirectBiller.isSelected())
	    {
		showPurRateInDirectBiller = "Y";
	    }

	    String tableReservationForCustomer = "N";
	    if (chkTableReservationForCustomer.isSelected())
	    {
		tableReservationForCustomer = "Y";
	    }
	    else
	    {
		tableReservationForCustomer = "N";
	    }

	    String autoShowPopItems = "N";
	    if (chkAutoShowPopItems.isSelected())
	    {
		autoShowPopItems = "Y";
	    }
	    int intPOPItemsOfDays = 1;
	    try
	    {
		intPOPItemsOfDays = Integer.parseInt(txtShowPopularItemsOfNDays.getText());
	    }
	    catch (Exception e)
	    {
		e.printStackTrace();
		intPOPItemsOfDays = 1;
	    }

	    String postMMSDataCostCenterWiseOrLocWise = cmbPostMMSSalesEffectCostOrLoc.getSelectedItem().toString();
	    String effectOfSales = cmbEffectOfSales.getSelectedItem().toString();
	    String posWiseItemLinkedUpToMMSProduct = "N";
	    if (chkPOSWiseItemLinkeUpToMMSProduct.isSelected())
	    {
		posWiseItemLinkedUpToMMSProduct = "Y";
	    }

	    String enableMasterDiscount = "N";
	    if (chkEnableMasterDiscount.isSelected())
	    {
		enableMasterDiscount = "Y";
	    }

	    String enableNFCInterface = "N";
	    if (chkEnableNFCInterface.isSelected())
	    {
		enableNFCInterface = "Y";
	    }

	    String enableLockTables = "N";
	    if (chkEnableLockTables.isSelected())
	    {
		enableLockTables = "Y";
	    }

	    String sqlForAllArea = "select a.strAreaCode,a.strAreaName from tblareamaster a where strAreaName='All' ";
	    String allAreaCode = "A001";
	    ResultSet rsAllArea = clsGlobalVarClass.dbMysql.executeResultSet(sqlForAllArea);
	    if (rsAllArea.next())
	    {
		allAreaCode = rsAllArea.getString(1);
	    }
	    rsAllArea.close();

	    String dineInAreaForDirectBiller = allAreaCode;
	    if (mapAreaNameWithCode.get(cmbDineInAreaForDirectBiller.getSelectedItem()) != null)
	    {
		dineInAreaForDirectBiller = mapAreaNameWithCode.get(cmbDineInAreaForDirectBiller.getSelectedItem());
	    }

	    String homeDeliveryAreaForDirectBiller = allAreaCode;
	    if (mapAreaNameWithCode.get(cmbHomeDeliAreaForDirectBiller.getSelectedItem()) != null)
	    {
		homeDeliveryAreaForDirectBiller = mapAreaNameWithCode.get(cmbHomeDeliAreaForDirectBiller.getSelectedItem());
	    }

	    String takeAwayAreaForDirectBiller = allAreaCode;
	    if (mapAreaNameWithCode.get(cmbTakeAwayAreaForDirectBiller.getSelectedItem()) != null)
	    {
		takeAwayAreaForDirectBiller = mapAreaNameWithCode.get(cmbTakeAwayAreaForDirectBiller.getSelectedItem());
	    }
	    String roundOffFinalBillAmount = "N";
	    if (chkRoundOffBillAmount.isSelected())
	    {
		roundOffFinalBillAmount = "Y";
	    }

	    double dblNoOfDecimalPlace = 2;
	    if (!funCheckInt(txtNoOfDecimalPlaces.getText().trim()))
	    {
		new frmOkPopUp(this, "Please enter Numbers only!", "Error", 0).setVisible(true);
		return;
	    }
	    else
	    {
		dblNoOfDecimalPlace = Double.parseDouble(txtNoOfDecimalPlaces.getText());
	    }
	    String sendDBBackupOnClientMail = "N";
	    if (chkSendDBBackupOnMail.isSelected())
	    {
		sendDBBackupOnClientMail = "Y";
	    }

	    String printOrderNoOnBillYN = "N";
	    if (chkPrintOrderNoOnBill.isSelected())
	    {
		printOrderNoOnBillYN = "Y";
	    }

	    String printDeviceAndUserDtlOnKOTYN = "N";
	    if (chkPrintDeviceUserDtlOnKOT.isSelected())
	    {
		printDeviceAndUserDtlOnKOTYN = "Y";
	    }

	    String removeSCTaxCode = "";
	    if (cmbRemoveServiceChargeTaxCode.getSelectedItem() != null)
	    {
		String removeSCTaxName = cmbRemoveServiceChargeTaxCode.getSelectedItem().toString();
		removeSCTaxCode = mapTaxNameWithCode.get(removeSCTaxName);
	    }

	    String autoAddKOTToBill = "N";
	    if (chkAutoAddKOTToBill.isSelected())
	    {
		autoAddKOTToBill = "Y";
	    }

	    String areaWiseCostCenterKOTPrinting = "N";
	    if (chkAreaWiseCostCenterKOTPrinting.isSelected())
	    {
		areaWiseCostCenterKOTPrinting = "Y";
	    }

	    String weraOnlineOrderIntegrationYN = "N";
	    if (cmbWeraIntegrationYN.getSelectedItem().toString().equalsIgnoreCase("Yes"))
	    {
		weraOnlineOrderIntegrationYN = "Y";
	    }
	    String weraMerchantOutletId = txtWeraMerchantOutletId.getText().trim();

	    String weraAuthenticationAPIKey = txtWeraAuthenticationAPIKey.getText().trim();

	    String fireCommunication = "N";
	    if (chkFireCommunication.isSelected())
	    {
		fireCommunication = "Y";
	    }

	    String usdCurrencyConvertionRate = txtUSDCrrencyConverionRate.getText().trim();

	    String dbBackupMailReceiverMailIds = txtDBBackupReceiverEmailId.getText().trim();

	    String printMoveTableMoveKOTYN = "N";
	    if (chkPrintMoveTableMoveKOT.isSelected())
	    {
		printMoveTableMoveKOTYN = "Y";
	    }

	    String printQtyTotal = "N";
	    if (chkPrintQtyTotal.isSelected())
	    {
		printQtyTotal = "Y";
	    }

	    /**
	     *
	     *
	     *
	     *
	     * save logic also change the POS master form for property setup
	     * changes
	     *
	     *
	     *
	     *
	     *
	     *
	     *
	     */
	    if (btnNew.getText().equalsIgnoreCase("SAVE"))//save logic
	    {
		sql = "select min(strClientCode) from tblsetup limit 1";
		countSet = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		countSet.next();
		code = countSet.getString(1);
		txtClientCode.setText(String.valueOf(code));

		java.util.Date objDate = new java.util.Date();
		dteCreated = (objDate.getYear() + 1900) + "-" + (objDate.getMonth() + 1) + "-" + objDate.getDate()
			+ " " + objDate.getHours() + ":" + objDate.getMinutes() + ":" + objDate.getSeconds();

		dteEdited = (objDate.getYear() + 1900) + "-" + (objDate.getMonth() + 1) + "-" + objDate.getDate()
			+ " " + objDate.getHours() + ":" + objDate.getMinutes() + ":" + objDate.getSeconds();

		if (!globelvalidate.validateEmpty(txtClientName.getText()))
		{
		    new frmOkPopUp(this, "Please Enter Shop Name ", "Error", 0).setVisible(true);
		    txtClientName.requestFocus();
		    return;
		}

		if (!globelvalidate.validateEmpty(txtShopAddressLine1.getText()))
		{
		    new frmOkPopUp(this, "Please Enter Shop Address", "Error", 0).setVisible(true);
		    return;
		}

		if (!globelvalidate.validateEmpty(txtTelephone.getText()))
		{
		    new frmOkPopUp(this, "Please Enter Telephone Number", "Error", 0).setVisible(true);
		    return;
		}
		if (!globelvalidate.validateEmpty(txtEmailAddress.getText()))
		{
		    new frmOkPopUp(this, "Please Enter Email Address", "Error", 0).setVisible(true);
		    return;
		}

		if (!globelvalidate.validateEmail(txtEmailAddress.getText().trim()))
		{
		    new frmOkPopUp(this, "Please Enter valid Email Address", "Error", 0).setVisible(true);
		    txtEmailAddress.requestFocus();
		    return;
		}

		if (!globelvalidate.validateEmpty(txtBillFooter.getText()))
		{
		    new frmOkPopUp(this, "Please Enter Bill Footer", "Error", 0).setVisible(true);
		    return;
		}

		if (chkPrintVatNo.isSelected() && txtVatNo.getText().length() < 1)
		{
		    new frmOkPopUp(this, "Please Enter Vat No", "Error", 0).setVisible(true);
		    txtVatNo.requestFocus();
		    return;
		}
		if (maxDiscount > 99)
		{
		    new frmOkPopUp(this, "Max Discount Must Be Less Than 100%", "Error", 0).setVisible(true);
		    return;
		}

		if (txtCustSeries.getText().trim().length() == 0)
		{
		    new frmOkPopUp(this, "Please enter Customer Series!", "Error", 0).setVisible(true);
		    return;
		}

		if (!funCheckInt(txtCustSeries.getText().trim()))
		{
		    new frmOkPopUp(this, "Please enter Numbers only!", "Error", 0).setVisible(true);
		    return;
		}
		else
		{
		    if (cmbCRMType.getSelectedIndex() == 0)
		    {
			if (txtGetWebservice.getText().trim().length() == 0)
			{
			    JOptionPane.showMessageDialog(this, "Please Enter Get WebService URL Field!");
			    return;
			}
			else if (txtPostWebservice.getText().trim().length() == 0)
			{
			    JOptionPane.showMessageDialog(this, "Please Enter Post WebService URL Field!");
			    return;
			}
			else if (txtOutletUID.getText().trim().length() == 0)
			{
			    JOptionPane.showMessageDialog(this, "Please Enter Outlet UID Field!");
			    return;
			}
			else if (txtPOSID.getText().trim().length() == 0)
			{
			    JOptionPane.showMessageDialog(this, "Please Enter POSID Field!");
			    return;
			}
		    }

		    if (cmbCMSIntegrationYN.getSelectedIndex() == 1)
		    {
			if (txtCMSWesServiceURL.getText().trim().length() == 0)
			{
			    JOptionPane.showMessageDialog(this, "Please Enter CMS WEB Service URL!");
			    return;
			}
		    }
		    if ("Yes".trim().equalsIgnoreCase(strPOSType.getSelectedItem().toString()))
		    {
			if (!globelvalidate.validateEmpty(txtWebServiceLink.getText()))
			{
			    new frmOkPopUp(this, "Please Enter Web Service Link", "Error", 0).setVisible(true);
			    return;
			}
		    }

		    String csize = cmbColumnSize.getSelectedItem().toString();
		    int ColumnSize = Integer.valueOf(csize);
		    cityName = cmbCity.getSelectedItem().toString();
		    stateName = cmbState.getSelectedItem().toString();
		    countryName = cmbCountry.getSelectedItem().toString();
		    if (chkNegBilling.isSelected())
		    {
			negBilling = "Y";
		    }
		    else
		    {
			negBilling = "N";
		    }
		    if (chkDayEnd.isSelected())
		    {
			dayEnd = "Y";
		    }
		    else
		    {
			dayEnd = "N";
		    }
		    if (chkMultiBillPrint.isSelected())
		    {
			multiBillPrint = "Y";
		    }
		    else
		    {
			multiBillPrint = "N";
		    }
		    if (chkEnableKOT.isSelected())
		    {
			kot = "Y";
		    }
		    else
		    {
			kot = "N";
		    }
		    if (chkEffectOnPSP.isSelected())
		    {
			effectOnPSP = "Y";
		    }
		    else
		    {
			effectOnPSP = "N";
		    }
		    if (chkPrintVatNo.isSelected())
		    {
			printVatNo = "Y";
		    }
		    else
		    {
			printVatNo = "N";
		    }
		    if (chkShowBill.isSelected())
		    {
			showBill = "Y";
		    }
		    else
		    {
			showBill = "N";
		    }
		    if (chkServiceTaxNo.isSelected())
		    {
			printServiceTaxno = "Y";

		    }
		    else
		    {
			printServiceTaxno = "N";
		    }
		    if (chkManualBillNo.isSelected())
		    {
			ManualBillNo = "Y";
		    }
		    else
		    {
			ManualBillNo = "N";
		    }
		    if (chkPrintKotForDirectBiller.isSelected())
		    {
			KOTPrintingForDB = "Y";

		    }
		    else
		    {
			KOTPrintingForDB = "N";
		    }
		    if (cmbServerName.getSelectedItem().equals("Gmail"))
		    {
			ServerHostName = "smtp.gmail.com";
		    }
		    else
		    {
			ServerHostName = "smtp.mail.yahoo.com";
		    }
		    if (cmbRFIDSetup.getSelectedItem().equals("Yes"))
		    {
			rfidInterface = "Y";
			if (!funCheckRFIDValidations())
			{
			    return;
			}
		    }
		    theme = cmbChangeTheme.getSelectedItem().toString();

		    if (txtPincode.getText().isEmpty())
		    {
			pincode = "0";
		    }
		    else
		    {
			pincode = txtPincode.getText();
		    }

		    if (chkAreaWisePricing.isSelected())
		    {
			areaWisePricing = "Y";
		    }
		    else
		    {
			areaWisePricing = "N";
		    }

		    String menuItemSortingOn = "NA";
		    if (cmbMenuItemSortingOn.getSelectedIndex() == 1)
		    {
			menuItemSortingOn = "subgroupWise";
		    }
		    else if (cmbMenuItemSortingOn.getSelectedIndex() == 2)
		    {
			menuItemSortingOn = "subMenuHeadWise";
		    }

		    String printType = cmbPrintType.getSelectedItem().toString();
		    String smsAPI = txtAreaSMSApi.getText().trim();

		    if (chkEditHomeDelivery.isSelected())
		    {
			editHomeDelivery = "Y";
		    }
		    if (chkSlabBasedHomeDelCharges.isSelected())
		    {
			slabBasedHomeDelCharges = "Y";
		    }

		    String skipWaiterAndPax = "N";

		    String skipWaiterSelection = "N";
		    if (chkSkip_Waiter_Selection.isSelected())
		    {
			skipWaiterSelection = "Y";
		    }

		    String strDirectKOTPrintMakeKOT = "N";

		    String strSkipPaxSelection = "N";
		    if (chkSkip_pax_selection.isSelected())
		    {
			strSkipPaxSelection = "Y";
		    }

		    if (cmbStockInOption.getSelectedIndex() == 0)
		    {
			stockInOption = "ItemWise";
		    }
		    else if (cmbStockInOption.getSelectedIndex() == 1)
		    {
			stockInOption = "MenuHeadWise";
		    }

		    int advRePrintCount = (Integer.parseInt(txtAdvRecPrintCount.getText()));
		    String billFormatType = cmbBillFormatType.getSelectedItem().toString();

		    String activePromotions = "N";
		    if (chkActivePromotions.isSelected())
		    {
			activePromotions = "Y";
		    }

		    String sendHomeDel = "N";
		    if (chkHomeDelSMS.isSelected())
		    {
			sendHomeDel = "Y";
		    }

		    String sendBillSettlement = "N";
		    if (chkBillSettlementSMS.isSelected())
		    {
			sendBillSettlement = "Y";
		    }
		    String smsType = cmbSMSType.getSelectedItem().toString();

		    String printShortNameOnKOT = "N";
		    if (chkPrintShortNameOnKOT.isSelected())
		    {
			printShortNameOnKOT = "Y";
		    }

		    String showCustHelpOnTrans = "N";
		    String printOnVoidBill = "N";
		    if (chkPrintForVoidBill.isSelected())
		    {
			printOnVoidBill = "Y";
		    }

		    String cmsPOSCode = "NA";

		    String postSalesDataToMMS = "N";
		    if (chkPostSalesDataToMMS.isSelected())
		    {
			postSalesDataToMMS = "Y";
		    }

		    String custAreaMasterCompulsory = "N";
		    if (chkAreaMasterCompulsory.isSelected())
		    {
			custAreaMasterCompulsory = "Y";
		    }
		    String priceFrom = cmbPriceFrom.getSelectedItem().toString();

		    String printerErrorMessage = "N";
		    if (chkPrinterErrorMessage.isSelected())
		    {
			printerErrorMessage = "Y";
		    }

		    String ChangeQtyForExternalCode = "N";
		    if (chkChangeQtyForExternalCode.isSelected())
		    {
			ChangeQtyForExternalCode = "Y";
		    }

		    String PointsOnBillPrint = "N";
		    if (chkPointsOnBillPrint.isSelected())
		    {
			PointsOnBillPrint = "Y";
		    }

		    String touchScreenMode = "N";

		    String cardInterfaceType = cmbCardIntfType.getSelectedItem().toString().trim();
		    String strCMSIntegartionYN = "N";
		    if (cmbCMSIntegrationYN.getSelectedIndex() == 1)
		    {
			strCMSIntegartionYN = "Y";
		    }
		    String manualAdvOrderNo = "N";
		    if (chkManualAdvOrderCompulsory.isSelected())
		    {
			manualAdvOrderNo = "Y";
		    }

		    String printManualAdvOrderNoOnBill = "N";
		    if (chkPrintManualAdvOrderOnBill.isSelected())
		    {
			printManualAdvOrderNoOnBill = "Y";
		    }

		    String printModQtyOnKOT = "N";
		    if (chkPrintModifierQtyOnKOT.isSelected())
		    {
			printModQtyOnKOT = "Y";
		    }

		    String noOfLinesInKOTPrint = txtNoOfLinesInKOTPrint.getText().trim();
		    String multiKOTPrint = "N";
		    if (chkMultiKOTPrint.isSelected())
		    {
			multiKOTPrint = "Y";
		    }

		    String ItemQtyNumpad = "N";
		    if (chkItemQtyNumpad.isSelected())
		    {
			ItemQtyNumpad = "Y";
		    }

		    String memberAsTable = "N";
		    if (chkMemberAsTable.isSelected())
		    {
			memberAsTable = "Y";
		    }

		    String KOTToLocalPrinter = "N";
		    if (chkPrintKOTToLocalPrinter.isSelected())
		    {
			KOTToLocalPrinter = "Y";
		    }

		    String settleBtnForDirectBillerBill = "N";
		    if (chkEnableSettleBtnForDirectBillerBill.isSelected())
		    {
			settleBtnForDirectBillerBill = "Y";
		    }

		    String delBoySelCompulsoryOnDirectBiller = "N";
		    if (chkDelBoyCompulsoryOnDirectBiller.isSelected())
		    {
			delBoySelCompulsoryOnDirectBiller = "Y";
		    }

		    String memberCodeForKOTJPOS = "N";
		    if (chkMemberCodeForKOTJPOS.isSelected())
		    {
			memberCodeForKOTJPOS = "Y";
		    }

		    String memberCodeForKOTMPOS = "N";
		    if (chkMemberCodeForKOTMPOS.isSelected())
		    {
			memberCodeForKOTMPOS = "Y";
		    }

		    String dontShowAdvOrderOfOtherPOS = "N";
		    if (chkDontShowAdvOrderInOtherPOS.isSelected())
		    {
			dontShowAdvOrderOfOtherPOS = "Y";
		    }

		    String printZeroAmtModifierOnBill = "N";
		    if (chkPrintZeroAmtModifierInBill.isSelected())
		    {
			printZeroAmtModifierOnBill = "Y";
		    }

		    String printKOTYN = "Y";
		    if (chkPrintKOTYN.isSelected())
		    {
			printKOTYN = "Y";
		    }
		    else
		    {
			printKOTYN = "N";
		    }

		    String slipNoCompulsoryYN = "N";
		    if (chkSlipNoForCreditCardBillYN.isSelected())
		    {
			slipNoCompulsoryYN = "Y";
		    }

		    String expDateCompulsoryYN = "N";
		    if (chkExpDateForCreditCardBillYN.isSelected())
		    {
			expDateCompulsoryYN = "Y";
		    }

		    String selectWaiterFromCardSwipe = "N";
		    if (chkSelectWaiterFromCardSwipe.isSelected())
		    {
			selectWaiterFromCardSwipe = "Y";
		    }

		    String multiWaiterSelOnMakeKOT = "N";
		    if (chkMultipleWaiterSelectionOnMakeKOT.isSelected())
		    {
			multiWaiterSelOnMakeKOT = "Y";
		    }

		    String moveTableToOtherPOS = "N";
		    if (chkMoveTableToOtherPOS.isSelected())
		    {
			moveTableToOtherPOS = "Y";
		    }

		    String moveKOTToOtherPOS = "N";
		    if (chkMoveKOTToOtherPOS.isSelected())
		    {
			moveKOTToOtherPOS = "Y";
		    }

		    String calculateTaxOnMakeKOT = "N";
		    if (chkCalculateTaxOnMakeKOT.isSelected())
		    {
			calculateTaxOnMakeKOT = "Y";
		    }

		    String receiverEmailIds = txtReceiverEmailId.getText().trim();

		    String calculateDiscItemWise = "N";
		    if (chkCalculateDiscItemWise.isSelected())
		    {
			calculateDiscItemWise = "Y";
		    }

		    String takewayCustomerSelection = "N";
		    if (chkTakewayCustomerSelection.isSelected())
		    {
			takewayCustomerSelection = "Y";
		    }

		    String showItemStkColumnInDB = "N";
		    if (chkShowItemStkColumnInDB.isSelected())
		    {
			showItemStkColumnInDB = "Y";
		    }
		    String itemType = cmbItemType.getSelectedItem().toString();

		    String allowNewAreaMasterFromCustMaster = "N";
		    if (chkBoxAllowNewAreaMasterFromCustMaster.isSelected())
		    {
			allowNewAreaMasterFromCustMaster = "Y";
		    }

		    String custAddressSelectionForBill = "N";
		    if (chkSelectCustAddressForBill.isSelected())
		    {
			custAddressSelectionForBill = "Y";
		    }

		    String genrateMI = "N";
		    if (chkGenrateMI.isSelected())
		    {
			genrateMI = "Y";
		    }

		    String allowToCalculateItemWeight = "N";
		    if (chkAllowToCalculateItemWeight.isSelected())
		    {
			allowToCalculateItemWeight = "Y";
		    }
		    String showBillsDtlType = cmbShowBillsDtlType.getSelectedItem().toString();

		    String printTaxInvoiceOnBill = "Y";
		    if (!chkPrintInvoiceOnBill.isSelected())
		    {
			printTaxInvoiceOnBill = "N";
		    }
		    String printInclusiveOfAllTxesOnBill = "Y";
		    if (!chkPrintInclusiveOfAllTaxesOnBill.isSelected())
		    {
			printInclusiveOfAllTxesOnBill = "N";
		    }

		    String applyDiscountOn = cmbApplyDiscountOn.getSelectedItem().toString();

		    String memberCodeForKotInMposByCardSwipe = "N";
		    if (chkMemberCodeForKotInMposByCardSwipe.isSelected())
		    {
			memberCodeForKotInMposByCardSwipe = "Y";
		    }

		    String checkPrintBill = "N";
		    if (chkPrintBill.isSelected())
		    {
			checkPrintBill = "Y";
		    }

		    String useVatAndServiceNoFromPos = "N";
		    if (chkUseVatAndServiceNoFromPos.isSelected())
		    {
			useVatAndServiceNoFromPos = "Y";
		    }

		    String memberCodeForMakeBillInMPOS = "N";
		    if (chkMemberCodeForMakeBillInMPOS.isSelected())
		    {
			memberCodeForMakeBillInMPOS = "Y";
		    }

		    String HOServerDate = (dteHOServerDate.getDate().getYear() + 1900) + "-" + (dteHOServerDate.getDate().getMonth() + 1)
			    + "-" + (dteHOServerDate.getDate().getDate()) + " " + HOServerTime;

		    String itemWiseKOTYN = "N";
		    if (chkItemWiseKOTPrintYN.isSelected())
		    {
			itemWiseKOTYN = "Y";
		    }

		    String posCode = mapPOS.get(cmbPOSForDayEnd.getSelectedItem().toString());

		    String CMSPostingType = "";
		    if (cmbCMSPostingType.getSelectedItem().equals("Sanguine CMS"))
		    {
			CMSPostingType = "Sanguine CMS";
		    }
		    else
		    {
			CMSPostingType = "Others";
		    }

		    String popUpToApplyPromotionsOnBill = "N";
		    if (chkPopUpToApplyPromotionsOnBill.isSelected())
		    {
			popUpToApplyPromotionsOnBill = "Y";
		    }

		    String selectCustomerCodeByCardSwipe = "N";
		    if (chkSelectCustomerCodeFromCardSwipe.isSelected())
		    {
			selectCustomerCodeByCardSwipe = "Y";
		    }

		    String checkDebitCardBalOnTrans = "N";
		    if (chkCheckDebitCardBalOnTrans.isSelected())
		    {
			checkDebitCardBalOnTrans = "Y";
		    }

		    String pickSettlementsFromPOSMaster = "N";
		    if (chkSettlementsFromPOSMaster.isSelected())
		    {
			pickSettlementsFromPOSMaster = "Y";
		    }
		    String shiftWiseDayEnd = "N";
		    if (chkShiftWiseDayEnd.isSelected())
		    {
			shiftWiseDayEnd = "Y";
		    }

		    String productionLinkup = "N";
		    if (chkProductionLinkup.isSelected())
		    {
			productionLinkup = "Y";
		    }

		    String lockDataOnShift = "Y";
		    if (chkLockDataOnShift.isSelected())
		    {
			lockDataOnShift = "Y";
		    }
		    else
		    {
			lockDataOnShift = "N";
		    }

		    String wsClientCode = txtWSClientCode.getText().toString();

		    String enableBillSeries = "N";
		    if (chkEnableBillSeries.isSelected())
		    {
			enableBillSeries = "Y";
		    }
		    else
		    {
			enableBillSeries = "N";
		    }
		    String enablePMSIntegrationYN = "N";
		    if (chkEnablePMSIntegration.isSelected())
		    {
			enablePMSIntegrationYN = "Y";
		    }
		    else
		    {
			enablePMSIntegrationYN = "N";
		    }

		    String printTimeOnBillYN = "N";
		    if (chkPrintTimeOnBill.isSelected())
		    {
			printTimeOnBillYN = "Y";
		    }
		    else
		    {
			printTimeOnBillYN = "N";
		    }

		    String printTDHItemInBillYN = "N";
		    if (chkPrintTDHItemsInBill.isSelected())
		    {
			printTDHItemInBillYN = "Y";
		    }
		    else
		    {
			printTDHItemInBillYN = "N";
		    }

		    boolean flgPrintRemarkAndReason = false;
		    if (chkPrintRemarkAndReasonForReprint.isSelected())
		    {
			flgPrintRemarkAndReason = true;
		    }
		    else
		    {
			flgPrintRemarkAndReason = false;
		    }
		    String printRemarkAndReason = flgPrintRemarkAndReason ? "Y" : "N";

		    int daysBeforeOrderToCancle = Integer.parseInt(txtDaysBeforeOrderToCancel.getText().trim());
		    int noOfDelDaysForAdvOrder = Integer.parseInt(txtNoOfDelDaysForAdvOrder.getText().trim());
		    int noOfDelDaysForUrgentOrder = Integer.parseInt(txtNoOfDelDaysForUrgentOrder.getText().trim());

		    String setUpToTimeForAdvOrder = "N";
		    if (chkSetUpToTimeForAdvOrder.isSelected())
		    {
			setUpToTimeForAdvOrder = "Y";
		    }

		    String setUpToTimeForUrgentOrder = "N";
		    if (chkSetUpToTimeForUrgentOrder.isSelected())
		    {
			setUpToTimeForUrgentOrder = "Y";
		    }

		    String upToTimeForAdvOrder = "00:00 AM";
		    upToTimeForAdvOrder = cmbHours.getSelectedItem().toString() + ":" + cmbMinutes.getSelectedItem().toString() + " " + cmbAMPM.getSelectedItem().toString();

		    String upToTimeForUrgentOrder = "00:00 AM";
		    upToTimeForUrgentOrder = cmbHoursUrgentOrder.getSelectedItem().toString() + ":" + cmbMinutesUrgentOrder.getSelectedItem().toString() + " " + cmbAMPMUrgent.getSelectedItem().toString();

		    String enableBothPrintAndSettleBtnForDB = "N";
		    if (chkEnableBothPrintAndSettleBtnForDB.isSelected())
		    {
			enableBothPrintAndSettleBtnForDB = "Y";
		    }

		    String strInrestoPOSIntegartionYN = "N";
		    if (cmbInrestoPOSIntegrationYN.getSelectedIndex() == 1)
		    {
			strInrestoPOSIntegartionYN = "Y";
		    }

		    String carryForwardFloatAmtToNextDay = "N";
		    if (chkCarryForwardFloatAmtToNextDay.isSelected())
		    {
			carryForwardFloatAmtToNextDay = "Y";
		    }

		    String openCashDrawerAfterBillPrintYN = "N";
		    if (chkOpenCashDrawerAfterBillPrint.isSelected())
		    {
			openCashDrawerAfterBillPrintYN = "Y";
		    }

		    String propertyWiseSalesOrderYN = "N";
		    if (chkPropertyWiseSalesOrder.isSelected())
		    {
			propertyWiseSalesOrderYN = "Y";
		    }
		    String showItemDtlsForChangeCustomerOnBillYN = "N";
		    if (chkShowItemDtlsForChangeCustomerOnBill.isSelected())
		    {
			showItemDtlsForChangeCustomerOnBillYN = "Y";
		    }
		    String showPopUpForNextItemQuantityYN = "N";
		    if (chkShowPopUpForNextItemQuantity.isSelected())
		    {
			showPopUpForNextItemQuantityYN = "Y";
		    }
		    String strJioPOSIntegartionYN = "N";
		    if (cmbJioPOSIntegrationYN.getSelectedIndex() == 1)
		    {
			strJioPOSIntegartionYN = "Y";
		    }

		    String newBillSeriesForNewDay = "N";
		    if (chkNewBillSeriesForNewDay.isSelected())
		    {
			newBillSeriesForNewDay = "Y";
		    }
		    String showPOSWiseReports = "N";
		    if (chkShowOnlyLoginPOSReports.isSelected())
		    {
			showPOSWiseReports = "Y";
		    }
		    String enableDineIn = "N";
		    if (chkEnableDineIn.isSelected())
		    {
			enableDineIn = "Y";
		    }
		    String autoAreaSelectionInMakeKOT = "N";
		    if (chkAutoAreaSelectionInMakeKOT.isSelected())
		    {
			autoAreaSelectionInMakeKOT = "Y";
		    }

		    String strBenowIntegrationYN = "N";
		    if (cmbBenowPOSIntegrationYN.getSelectedIndex() == 1)
		    {
			strBenowIntegrationYN = "Y";
		    }

		    if (cmbBenowPOSIntegrationYN.getSelectedIndex() == 1)
		    {
			if (txtXEmail.getText().trim().length() == 0)
			{
			    JOptionPane.showMessageDialog(this, "Please Enter X-Email!!!");
			    return;
			}

			if (txtMerchantCode.getText().trim().length() == 0)
			{
			    JOptionPane.showMessageDialog(this, "Please Enter Benow Merchant Code!!!");
			    return;
			}

			if (txtAuthenticationKey.getText().trim().length() == 0)
			{
			    JOptionPane.showMessageDialog(this, "Please Enter Authentication Key For Benow!!!");
			    return;
			}

			if (txtSalt.getText().trim().length() == 0)
			{
			    JOptionPane.showMessageDialog(this, "Please Enter Salt For Benow!!!");
			    return;
			}
		    }

		    boolean isAuditSMS = isAuditSMSSelected();
		    if (isAuditSMS)
		    {
			String[] mobileNos = txtSMSMobileNo.getText().split(",");
			for (int i = 0; i < mobileNos.length; i++)
			{
			    if (!mobileNos[i].matches("\\d{10}"))
			    {
				new frmOkPopUp(null, "Please Enter Valid Mobile Number.", "Error", 1).setVisible(true);
				return;
			    }
			}
		    }
		    funSaveAuditSMS();

		    ResultSet rsPropSetupForAllPOS = clsGlobalVarClass.dbMysql.executeResultSet("select strClientCode from tblsetup where strPOSCode='All' ");
		    if (rsPropSetupForAllPOS.next())
		    {
			funSavePrinterSetupData();
			funSavePropertyImage(reportImgInputStream);

			clsGlobalVarClass.dbMysql.execute("update tblsetup set strPOSCode='" + newPropertyPOSCode + "' ");
			clsGlobalVarClass.dbMysql.execute("delete from tblsetup where strPOSCode='All' ");

			new frmOkPopUp(this, "Entry Added Successfully", "Successfull", 3).setVisible(true);
			funFillData();
		    }
		    else
		    {
			funSavePrinterSetupData();
			funSavePropertyImage(reportImgInputStream);

			if (!selectedLikePOSCode.equals("NA"))
			{
			    String sqlDelete = "delete from tblsetup where strPOSCode='" + newPropertyPOSCode + "' ";
			    clsGlobalVarClass.dbMysql.execute(sqlDelete);
			    String sqlLikePOS = "select * from  tblsetup where strPOSCode='" + selectedLikePOSCode + "' ";
			    ResultSet rsLikePOS = clsGlobalVarClass.dbMysql.executeResultSet(sqlLikePOS);
			    while (rsLikePOS.next())
			    {

				sql = "insert into tblsetup(strClientCode,strClientName,strAddressLine1,strAddressLine2 "//4
					+ ",strAddressLine3,strEmail,strBillFooter,strBillFooterStatus,intBillPaperSize "//9
					+ ",strNegativeBilling,strDayEnd,strPrintMode,strDiscountNote,strCityName "//14
					+ ",strState,strCountry,intTelephoneNo,dteStartDate,dteEndDate "//19
					+ ",strNatureOfBusinnes,strMultipleBillPrinting,strEnableKOT,strEffectOnPSP,strPrintVatNo "//24
					+ ",strVatNo,strShowBill,strPrintServiceTaxNo,strServiceTaxNo,strManualBillNo "//29
					+ ",strMenuItemDispSeq,strSenderEmailId,strEmailPassword,strConfirmEmailPassword,strBody "//34
					+ ",strEmailServerName,strSMSApi,strUserCreated,strUserEdited,dteDateCreated "//39
					+ ",dteDateEdited ,strPOSType,strWebServiceLink,strDataSendFrequency,dteHOServerDate "//44
					+ ",strRFID,strServerName,strDBUserName,strDBPassword,strDatabaseName "//49
					+ ",strEnableKOTForDirectBiller,intPinCode,strChangeTheme,dblMaxDiscount,strAreaWisePricing "//54
					+ ",strMenuItemSortingOn,strDirectAreaCode,intColumnSize,strPrintType,strEditHomeDelivery "//59
					+ ",strSlabBasedHDCharges,strSkipWaiterAndPax,strSkipWaiter,strDirectKOTPrintMakeKOT,strSkipPax "//64
					+ ",strCRMInterface,strGetWebserviceURL,strPostWebserviceURL,strOutletUID,strPOSID "//69
					+ ",strStockInOption,longCustSeries,intAdvReceiptPrintCount,strHomeDeliverySMS,strBillStettlementSMS "//74
					+ ",strBillFormatType,strActivePromotions,strSendHomeDelSMS,strSendBillSettlementSMS,strSMSType "//79
					+ ",strPrintShortNameOnKOT,strShowCustHelp,strPrintOnVoidBill,strPostSalesDataToMMS,strCustAreaMasterCompulsory "//84
					+ ",strPriceFrom,strShowPrinterErrorMessage,strTouchScreenMode,strCardInterfaceType,strCMSIntegrationYN "//89
					+ ",strCMSWebServiceURL,strChangeQtyForExternalCode,strPointsOnBillPrint,strCMSPOSCode,strManualAdvOrderNoCompulsory "//94
					+ ",strPrintManualAdvOrderNoOnBill,strPrintModifierQtyOnKOT,strNoOfLinesInKOTPrint,strMultipleKOTPrintYN,strItemQtyNumpad "//99
					+ ",strTreatMemberAsTable,strKOTToLocalPrinter,blobReportImage,strSettleBtnForDirectBillerBill,strDelBoySelCompulsoryOnDirectBiller "//104
					+ ",strCMSMemberForKOTJPOS,strCMSMemberForKOTMPOS,strDontShowAdvOrderInOtherPOS,strPrintZeroAmtModifierInBill,strPrintKOTYN "//109
					+ ",strCreditCardSlipNoCompulsoryYN,strCreditCardExpiryDateCompulsoryYN,strSelectWaiterFromCardSwipe,strMultiWaiterSelectionOnMakeKOT,strMoveTableToOtherPOS "//114
					+ ",strMoveKOTToOtherPOS,strCalculateTaxOnMakeKOT,strReceiverEmailId,strCalculateDiscItemWise,strTakewayCustomerSelection "//119
					+ ",StrShowItemStkColumnInDB,strItemType,strAllowNewAreaMasterFromCustMaster,strCustAddressSelectionForBill,strGenrateMI "//124
					+ ",strFTPAddress,strFTPServerUserName,strFTPServerPass,strAllowToCalculateItemWeight,strShowBillsDtlType "//129
					+ ",strPrintTaxInvoiceOnBill,strPrintInclusiveOfAllTaxesOnBill,strApplyDiscountOn,strMemberCodeForKotInMposByCardSwipe,strPrintBillYN "//134
					+ ",strVatAndServiceTaxFromPos,strMemberCodeForMakeBillInMPOS,strItemWiseKOTYN,strLastPOSForDayEnd,strCMSPostingType "//139
					+ ",strPopUpToApplyPromotionsOnBill,strSelectCustomerCodeFromCardSwipe,strCheckDebitCardBalOnTransactions,strSettlementsFromPOSMaster,strShiftWiseDayEndYN "//144
					+ ",strProductionLinkup,strLockDataOnShift,strWSClientCode,strPOSCode,strEnableBillSeries,strEnablePMSIntegrationYN,strPrintTimeOnBill"//151
					+ ",strPrintTDHItemsInBill,strPrintRemarkAndReasonForReprint,intDaysBeforeOrderToCancel,intNoOfDelDaysForAdvOrder,intNoOfDelDaysForUrgentOrder"//156
					+ ",strSetUpToTimeForAdvOrder,strSetUpToTimeForUrgentOrder,strUpToTimeForAdvOrder,strUpToTimeForUrgentOrder"//160
					+ ",strEnableBothPrintAndSettleBtnForDB,strInrestoPOSIntegrationYN,strInrestoPOSWebServiceURL,strInrestoPOSId"//164
					+ ",strInrestoPOSKey,strCarryForwardFloatAmtToNextDay,strOpenCashDrawerAfterBillPrintYN,strPropertyWiseSalesOrderYN" //168
					+ ",strDataPostFlag,strShowItemDetailsGrid,strShowPopUpForNextItemQuantity,strJioMoneyIntegration,strJioWebServiceUrl,strJioMID,strJioTID,strJioActivationCode,strJioDeviceID "//177
					+ ",strNewBillSeriesForNewDay,strShowReportsPOSWise,strEnableDineIn,strAutoAreaSelectionInMakeKOT,strConsolidatedKOTPrinterPort"//182
					+ ",dblRoundOff,strShowUnSettlementForm,strPrintOpenItemsOnBill,strPrintHomeDeliveryYN,strScanQRYN,strAreaWisePromotions "//188
					+ ",strPrintItemsOnMoveKOTMoveTable,strShowPurRateInDirectBiller,strEnableTableReservationForCustomer "//191
					+ ",strAutoShowPopItems,intShowPopItemsOfDays,strPostSalesCostOrLoc,strEffectOfSales,strPOSWiseItemToMMSProductLinkUpYN,strEnableMasterDiscount"//197
					+ ",strEnableNFCInterface,strBenowIntegrationYN,strXEmail,strMerchantCode,strAuthenticationKey,strSalt,strEnableLockTable"//204
					+ ",strHomeDeliveryAreaForDirectBiller,strTakeAwayAreaForDirectBiller,strRoundOffBillFinalAmt,dblNoOfDecimalPlace,strSendDBBackupOnClientMail,strPrintOrderNoOnBillYN,strPrintDeviceAndUserDtlOnKOTYN "//211
					+ ",strRemoveSCTaxCode,strAutoAddKOTToBill,strAreaWiseCostCenterKOTPrintingYN,strWERAOnlineOrderIntegration,strWERAMerchantOutletId,strWERAAuthenticationAPIKey"//217
					+ ",strFireCommunication,dblUSDConverionRate,strDBBackupMailReceiver,strPrintMoveTableMoveKOTYN,strPrintQtyTotal )"//222
					+ "values('" + txtClientCode.getText() + "','" + txtClientName.getText() + "','" + txtShopAddressLine1.getText() + "','" + txtShopAddressLine2.getText() + "' "
					+ ",'" + txtShopAddressLine3.getText() + "','" + txtEmailAddress.getText() + "','" + txtBillFooter.getText() + "','" + longFooter + "','" + cmbBillPaperSize.getSelectedItem().toString() + "' "
					+ ",'" + negBilling + "','" + dayEnd + "','" + cmbPrintMode.getSelectedItem().toString() + "','" + discount + "','" + cityName + "' "
					+ ",'" + stateName + "','" + countryName + "','" + txtTelephone.getText() + "','" + dteCreated + "','" + dteEndDate + "' "
					+ ",'" + cmbNatureOfBusiness.getSelectedItem().toString() + "','" + multiBillPrint + "','" + kot + "','" + effectOnPSP + "','" + printVatNo + "' "
					+ ",'" + txtVatNo.getText() + "','" + showBill + "','" + printServiceTaxno + "','" + txtServiceTaxno.getText() + "','" + ManualBillNo + "' "
					+ ",'" + cmbMenuItemDisSeq.getSelectedItem().toString() + "','" + txtSenderEmailId.getText() + "','" + txtEmailPassword.getText() + "','" + txtConfirmEmailPassword.getText() + "','" + txtBodyPart.getText() + "'"
					+ ",'" + ServerHostName + "','" + smsAPI + "','" + userCode + "','" + userCode + "','" + dteCreated + "' "
					+ ",'" + dteEdited + "','" + strPOSType.getSelectedItem().toString() + "','" + txtWebServiceLink.getText() + "','" + cmbDataSendFrequency.getSelectedItem().toString() + "','" + HOServerDate + "' "
					+ ",'" + rfidInterface + "','" + txtServerName.getText().trim() + "','" + txtUserName.getText().trim() + "','" + txtPassword.getText().trim() + "','" + txtDatabaseName.getText().trim() + "' "
					+ ",'" + KOTPrintingForDB + "','" + Long.parseLong(txtPincode.getText()) + "','" + theme + "','" + maxDiscount + "','" + areaWisePricing + "' "
					+ ",'" + menuItemSortingOn + "','" + dineInAreaForDirectBiller + "'," + "'" + ColumnSize + "','" + printType + "','" + editHomeDelivery + "' "
					+ ",'" + slabBasedHomeDelCharges + "','" + skipWaiterAndPax + "','" + skipWaiterSelection + "','" + strDirectKOTPrintMakeKOT + "'," + "'" + strSkipPaxSelection + "' "
					+ ",'" + crmInterface + "','" + getWebServiceURL + "'" + ",'" + postWebServiceURL + "','" + outletUID + "','" + posID + "' "
					+ ",'" + stockInOption + "'" + ",'" + txtCustSeries.getText().trim() + "','" + advRePrintCount + "','" + txtAreaSendHomeDeliverySMS.getText().trim() + "','" + txtAreaBillSettlementSMS.getText().trim() + "' "
					+ ",'" + billFormatType + "','" + activePromotions + "','" + sendHomeDel + "','" + sendBillSettlement + "','" + smsType + "' "
					+ ",'" + printShortNameOnKOT + "','" + showCustHelpOnTrans + "','" + printOnVoidBill + "','" + postSalesDataToMMS + "','" + custAreaMasterCompulsory + "' "
					+ ",'" + priceFrom + "','" + printerErrorMessage + "','" + touchScreenMode + "','" + cardInterfaceType + "','" + strCMSIntegartionYN + "'"
					+ ",'" + txtCMSWesServiceURL.getText().trim() + "','" + ChangeQtyForExternalCode + "','" + PointsOnBillPrint + "','" + cmsPOSCode + "','" + manualAdvOrderNo + "' "
					+ ",'" + printManualAdvOrderNoOnBill + "','" + printModQtyOnKOT + "','" + noOfLinesInKOTPrint + "','" + multiKOTPrint + "','" + ItemQtyNumpad + "' "
					+ ",'" + memberAsTable + "','" + KOTToLocalPrinter + "','','" + settleBtnForDirectBillerBill + "','" + delBoySelCompulsoryOnDirectBiller + "' "
					+ ",'" + memberCodeForKOTJPOS + "','" + memberCodeForKOTMPOS + "','" + dontShowAdvOrderOfOtherPOS + "','" + printZeroAmtModifierOnBill + "','" + printKOTYN + "'"
					+ ",'" + slipNoCompulsoryYN + "','" + expDateCompulsoryYN + "','" + selectWaiterFromCardSwipe + "','" + multiWaiterSelOnMakeKOT + "','" + moveTableToOtherPOS + "' "
					+ ",'" + moveKOTToOtherPOS + "','" + calculateTaxOnMakeKOT + "','" + receiverEmailIds + "','" + calculateDiscItemWise + "','" + takewayCustomerSelection + "' "
					+ ",'" + showItemStkColumnInDB + "','" + itemType + "','" + allowNewAreaMasterFromCustMaster + "','" + custAddressSelectionForBill + "','" + genrateMI + "' "
					+ ",'" + txtFTPAddress.getText().trim() + "','" + txtFTPServerUserName.getText().trim() + "','" + txtFTPServerPass.getText().trim() + "','" + allowToCalculateItemWeight + "',' " + showBillsDtlType + "' "
					+ ",'" + printTaxInvoiceOnBill + "','" + printInclusiveOfAllTxesOnBill + "','" + applyDiscountOn + "','" + memberCodeForKotInMposByCardSwipe + "','" + checkPrintBill + "' "
					+ ",'" + useVatAndServiceNoFromPos + "','" + memberCodeForMakeBillInMPOS + "','" + itemWiseKOTYN + "','" + posCode + "','" + CMSPostingType + "' "
					+ ",'" + popUpToApplyPromotionsOnBill + "','" + selectCustomerCodeByCardSwipe + "','" + checkDebitCardBalOnTrans + "','" + pickSettlementsFromPOSMaster + "','" + shiftWiseDayEnd + "' "
					+ ",'" + productionLinkup + "','" + lockDataOnShift + "','" + wsClientCode + "','" + newPropertyPOSCode + "','" + enableBillSeries + "','" + enablePMSIntegrationYN + "','" + printTimeOnBillYN + "'"
					+ ",'" + printTDHItemInBillYN + "','" + printRemarkAndReason + "'," + daysBeforeOrderToCancle + "," + noOfDelDaysForAdvOrder + "," + noOfDelDaysForUrgentOrder + ""
					+ ",'" + setUpToTimeForAdvOrder + "','" + setUpToTimeForUrgentOrder + "','" + upToTimeForAdvOrder + "','" + upToTimeForUrgentOrder + "'"
					+ ",'" + enableBothPrintAndSettleBtnForDB + "','" + strInrestoPOSIntegartionYN + "','" + txtInrestoPOSWesServiceURL.getText().trim() + "','" + txtInrestoPOSId.getText().trim() + "'"
					+ ",'" + txtInrestoPOSKey.getText().trim() + "','" + carryForwardFloatAmtToNextDay + "','" + openCashDrawerAfterBillPrintYN + "','" + propertyWiseSalesOrderYN + "','N','" + showItemDtlsForChangeCustomerOnBillYN + "'"
					+ ",'" + showPopUpForNextItemQuantityYN + "','" + strJioPOSIntegartionYN + "','" + txtJioPOSWesServiceURL.getText().trim() + "','" + txtJioMoneyMID.getText().trim() + "','" + txtJioMoneyTID.getText().trim() + "','" + txtJioActivationCode.getText().trim() + "','" + txtJioDeviceID.getText().trim() + "'"
					+ ",'" + newBillSeriesForNewDay + "','" + showPOSWiseReports + "','" + enableDineIn + "','" + autoAreaSelectionInMakeKOT + "','" + consolidatedKOTPrinterPort + "'"
					+ ",'" + roundOffTo + "','" + showUnSettlementForm + "','" + printOpenItemsOnBill + "','" + printHomeDeliveryYN + "','" + scanQRYN + "','" + areaWisePromotions + "' "
					+ ",'" + printItemOnMoveKOTMoveTable + "','" + showPurRateInDirectBiller + "','" + tableReservationForCustomer + "' "//191
					+ ",'" + autoShowPopItems + "','" + intPOPItemsOfDays + "','" + postMMSDataCostCenterWiseOrLocWise + "'"//194
					+ ",'" + effectOfSales + "','" + posWiseItemLinkedUpToMMSProduct + "','" + enableMasterDiscount + "','" + enableNFCInterface + "'" //198
					+ ",'" + strBenowIntegrationYN + "','" + txtXEmail.getText() + "','" + txtMerchantCode.getText() + "','" + txtAuthenticationKey.getText() + "','" + txtSalt.getText() + "','" + enableLockTables + "'"//204
					+ ",'" + homeDeliveryAreaForDirectBiller + "','" + takeAwayAreaForDirectBiller + "','" + roundOffFinalBillAmount + "','" + dblNoOfDecimalPlace + "','" + sendDBBackupOnClientMail + "','" + printOrderNoOnBillYN + "','" + printDeviceAndUserDtlOnKOTYN + "'" //211
					+ ",'" + removeSCTaxCode + "','" + autoAddKOTToBill + "','" + areaWiseCostCenterKOTPrinting + "','" + weraOnlineOrderIntegrationYN + "','" + weraMerchantOutletId + "','" + weraAuthenticationAPIKey + "' "
					+ ",'" + fireCommunication + "','" + usdCurrencyConvertionRate + "','" + dbBackupMailReceiverMailIds + "','" + printMoveTableMoveKOTYN + "','" + printQtyTotal + "')";//222
			    }

			    //You should change POS master for this
			}
			else
			{
			    sql = "insert into tblsetup(strClientCode,strClientName,strAddressLine1,strAddressLine2 "//4
				    + ",strAddressLine3,strEmail,strBillFooter,strBillFooterStatus,intBillPaperSize "//9
				    + ",strNegativeBilling,strDayEnd,strPrintMode,strDiscountNote,strCityName "//14
				    + ",strState,strCountry,intTelephoneNo,dteStartDate,dteEndDate "//19
				    + ",strNatureOfBusinnes,strMultipleBillPrinting,strEnableKOT,strEffectOnPSP,strPrintVatNo "//24
				    + ",strVatNo,strShowBill,strPrintServiceTaxNo,strServiceTaxNo,strManualBillNo "//29
				    + ",strMenuItemDispSeq,strSenderEmailId,strEmailPassword,strConfirmEmailPassword,strBody "//34
				    + ",strEmailServerName,strSMSApi,strUserCreated,strUserEdited,dteDateCreated "//39
				    + ",dteDateEdited ,strPOSType,strWebServiceLink,strDataSendFrequency,dteHOServerDate "//44
				    + ",strRFID,strServerName,strDBUserName,strDBPassword,strDatabaseName "//49
				    + ",strEnableKOTForDirectBiller,intPinCode,strChangeTheme,dblMaxDiscount,strAreaWisePricing "//54
				    + ",strMenuItemSortingOn,strDirectAreaCode,intColumnSize,strPrintType,strEditHomeDelivery "//59
				    + ",strSlabBasedHDCharges,strSkipWaiterAndPax,strSkipWaiter,strDirectKOTPrintMakeKOT,strSkipPax "//64
				    + ",strCRMInterface,strGetWebserviceURL,strPostWebserviceURL,strOutletUID,strPOSID "//69
				    + ",strStockInOption,longCustSeries,intAdvReceiptPrintCount,strHomeDeliverySMS,strBillStettlementSMS "//74
				    + ",strBillFormatType,strActivePromotions,strSendHomeDelSMS,strSendBillSettlementSMS,strSMSType "//79
				    + ",strPrintShortNameOnKOT,strShowCustHelp,strPrintOnVoidBill,strPostSalesDataToMMS,strCustAreaMasterCompulsory "//84
				    + ",strPriceFrom,strShowPrinterErrorMessage,strTouchScreenMode,strCardInterfaceType,strCMSIntegrationYN "//89
				    + ",strCMSWebServiceURL,strChangeQtyForExternalCode,strPointsOnBillPrint,strCMSPOSCode,strManualAdvOrderNoCompulsory "//94
				    + ",strPrintManualAdvOrderNoOnBill,strPrintModifierQtyOnKOT,strNoOfLinesInKOTPrint,strMultipleKOTPrintYN,strItemQtyNumpad "//99
				    + ",strTreatMemberAsTable,strKOTToLocalPrinter,blobReportImage,strSettleBtnForDirectBillerBill,strDelBoySelCompulsoryOnDirectBiller "//104
				    + ",strCMSMemberForKOTJPOS,strCMSMemberForKOTMPOS,strDontShowAdvOrderInOtherPOS,strPrintZeroAmtModifierInBill,strPrintKOTYN "//109
				    + ",strCreditCardSlipNoCompulsoryYN,strCreditCardExpiryDateCompulsoryYN,strSelectWaiterFromCardSwipe,strMultiWaiterSelectionOnMakeKOT,strMoveTableToOtherPOS "//114
				    + ",strMoveKOTToOtherPOS,strCalculateTaxOnMakeKOT,strReceiverEmailId,strCalculateDiscItemWise,strTakewayCustomerSelection "//119
				    + ",StrShowItemStkColumnInDB,strItemType,strAllowNewAreaMasterFromCustMaster,strCustAddressSelectionForBill,strGenrateMI "//124
				    + ",strFTPAddress,strFTPServerUserName,strFTPServerPass,strAllowToCalculateItemWeight,strShowBillsDtlType "//129
				    + ",strPrintTaxInvoiceOnBill,strPrintInclusiveOfAllTaxesOnBill,strApplyDiscountOn,strMemberCodeForKotInMposByCardSwipe,strPrintBillYN "//134
				    + ",strVatAndServiceTaxFromPos,strMemberCodeForMakeBillInMPOS,strItemWiseKOTYN,strLastPOSForDayEnd,strCMSPostingType "//139
				    + ",strPopUpToApplyPromotionsOnBill,strSelectCustomerCodeFromCardSwipe,strCheckDebitCardBalOnTransactions,strSettlementsFromPOSMaster,strShiftWiseDayEndYN "//144
				    + ",strProductionLinkup,strLockDataOnShift,strWSClientCode,strPOSCode,strEnableBillSeries,strEnablePMSIntegrationYN,strPrintTimeOnBill"
				    + ",strPrintTDHItemsInBill,strPrintRemarkAndReasonForReprint,intDaysBeforeOrderToCancel"
				    + ",intNoOfDelDaysForAdvOrder,intNoOfDelDaysForUrgentOrder,strSetUpToTimeForAdvOrder,strSetUpToTimeForUrgentOrder"
				    + ",strUpToTimeForAdvOrder,strUpToTimeForUrgentOrder,strEnableBothPrintAndSettleBtnForDB"
				    + ",strInrestoPOSIntegrationYN,strInrestoPOSWebServiceURL,strInrestoPOSId,strInrestoPOSKey"
				    + ",strCarryForwardFloatAmtToNextDay,strOpenCashDrawerAfterBillPrintYN,strPropertyWiseSalesOrderYN"
				    + ",strDataPostFlag,strShowItemDetailsGrid,strShowPopUpForNextItemQuantity,strJioMoneyIntegration,strJioWebServiceUrl,strJioMID,strJioTID,strJioActivationCode,strJioDeviceID "//177
				    + ",strNewBillSeriesForNewDay,strShowReportsPOSWise,strEnableDineIn,strAutoAreaSelectionInMakeKOT,strConsolidatedKOTPrinterPort "//182
				    + ",dblRoundOff,strShowUnSettlementForm,strPrintOpenItemsOnBill,strPrintHomeDeliveryYN,strScanQRYN,strAreaWisePromotions "//188
				    + ",strPrintItemsOnMoveKOTMoveTable,strShowPurRateInDirectBiller,strEnableTableReservationForCustomer "//191
				    + ",strAutoShowPopItems,intShowPopItemsOfDays,strPostSalesCostOrLoc,strEffectOfSales,strPOSWiseItemToMMSProductLinkUpYN,strEnableMasterDiscount"//197
				    + ",strEnableNFCInterface,strBenowIntegrationYN,strXEmail,strMerchantCode,strAuthenticationKey,strSalt,strEnableLockTable"//204
				    + ",strHomeDeliveryAreaForDirectBiller,strTakeAwayAreaForDirectBiller,strRoundOffBillFinalAmt,dblNoOfDecimalPlace,strSendDBBackupOnClientMail,strPrintOrderNoOnBillYN,strPrintDeviceAndUserDtlOnKOTYN "//211
				    + ",strRemoveSCTaxCode,strAutoAddKOTToBill,strAreaWiseCostCenterKOTPrintingYN,strWERAOnlineOrderIntegration,strWERAMerchantOutletId,strWERAAuthenticationAPIKey"//217
				    + ",strFireCommunication,dblUSDConverionRate,strDBBackupMailReceiver,strPrintMoveTableMoveKOTYN,strPrintQtyTotal )"//222
				    + "values('" + txtClientCode.getText() + "','" + txtClientName.getText().replace("'", "\\'") + "','" + txtShopAddressLine1.getText().replaceAll("'", "\\'") + "','" + txtShopAddressLine2.getText().replaceAll("'", "\\'") + "' "
				    + ",'" + txtShopAddressLine3.getText().replaceAll("'", "\\'") + "','" + txtEmailAddress.getText() + "','" + txtBillFooter.getText().replaceAll("'", "\\'") + "','" + longFooter + "','" + cmbBillPaperSize.getSelectedItem().toString() + "' "
				    + ",'" + negBilling + "','" + dayEnd + "','" + cmbPrintMode.getSelectedItem().toString() + "','" + discount + "','" + cityName + "' "
				    + ",'" + stateName + "','" + countryName + "','" + txtTelephone.getText() + "','" + dteCreated + "','" + dteEndDate + "' "
				    + ",'" + cmbNatureOfBusiness.getSelectedItem().toString() + "','" + multiBillPrint + "','" + kot + "','" + effectOnPSP + "','" + printVatNo + "' "
				    + ",'" + txtVatNo.getText() + "','" + showBill + "','" + printServiceTaxno + "','" + txtServiceTaxno.getText() + "','" + ManualBillNo + "' "
				    + ",'" + cmbMenuItemDisSeq.getSelectedItem().toString() + "','" + txtSenderEmailId.getText() + "','" + txtEmailPassword.getText() + "','" + txtConfirmEmailPassword.getText() + "','" + txtBodyPart.getText() + "'"
				    + ",'" + ServerHostName + "','" + smsAPI + "','" + userCode + "','" + userCode + "','" + dteCreated + "' "
				    + ",'" + dteEdited + "','" + strPOSType.getSelectedItem().toString() + "','" + txtWebServiceLink.getText() + "','" + cmbDataSendFrequency.getSelectedItem().toString() + "','" + HOServerDate + "' "
				    + ",'" + rfidInterface + "','" + txtServerName.getText().trim() + "','" + txtUserName.getText().trim() + "','" + txtPassword.getText().trim() + "','" + txtDatabaseName.getText().trim() + "' "
				    + ",'" + KOTPrintingForDB + "','" + Long.parseLong(txtPincode.getText()) + "','" + theme + "','" + maxDiscount + "','" + areaWisePricing + "' "
				    + ",'" + menuItemSortingOn + "','" + dineInAreaForDirectBiller + "'," + "'" + ColumnSize + "','" + printType + "','" + editHomeDelivery + "' "
				    + ",'" + slabBasedHomeDelCharges + "','" + skipWaiterAndPax + "','" + skipWaiterSelection + "','" + strDirectKOTPrintMakeKOT + "'," + "'" + strSkipPaxSelection + "' "
				    + ",'" + crmInterface + "','" + getWebServiceURL + "'" + ",'" + postWebServiceURL + "','" + outletUID + "','" + posID + "' "
				    + ",'" + stockInOption + "'" + ",'" + txtCustSeries.getText().trim() + "','" + advRePrintCount + "','" + txtAreaSendHomeDeliverySMS.getText().trim() + "','" + txtAreaBillSettlementSMS.getText().trim() + "' "
				    + ",'" + billFormatType + "','" + activePromotions + "','" + sendHomeDel + "','" + sendBillSettlement + "','" + smsType + "' "
				    + ",'" + printShortNameOnKOT + "','" + showCustHelpOnTrans + "','" + printOnVoidBill + "','" + postSalesDataToMMS + "','" + custAreaMasterCompulsory + "' "
				    + ",'" + priceFrom + "','" + printerErrorMessage + "','" + touchScreenMode + "','" + cardInterfaceType + "','" + strCMSIntegartionYN + "'"
				    + ",'" + txtCMSWesServiceURL.getText().trim() + "','" + ChangeQtyForExternalCode + "','" + PointsOnBillPrint + "','" + cmsPOSCode + "','" + manualAdvOrderNo + "' "
				    + ",'" + printManualAdvOrderNoOnBill + "','" + printModQtyOnKOT + "','" + noOfLinesInKOTPrint + "','" + multiKOTPrint + "','" + ItemQtyNumpad + "' "
				    + ",'" + memberAsTable + "','" + KOTToLocalPrinter + "','','" + settleBtnForDirectBillerBill + "','" + delBoySelCompulsoryOnDirectBiller + "' "
				    + ",'" + memberCodeForKOTJPOS + "','" + memberCodeForKOTMPOS + "','" + dontShowAdvOrderOfOtherPOS + "','" + printZeroAmtModifierOnBill + "','" + printKOTYN + "'"
				    + ",'" + slipNoCompulsoryYN + "','" + expDateCompulsoryYN + "','" + selectWaiterFromCardSwipe + "','" + multiWaiterSelOnMakeKOT + "','" + moveTableToOtherPOS + "' "
				    + ",'" + moveKOTToOtherPOS + "','" + calculateTaxOnMakeKOT + "','" + receiverEmailIds + "','" + calculateDiscItemWise + "','" + takewayCustomerSelection + "' "
				    + ",'" + showItemStkColumnInDB + "','" + itemType + "','" + allowNewAreaMasterFromCustMaster + "','" + custAddressSelectionForBill + "','" + genrateMI + "' "
				    + ",'" + txtFTPAddress.getText().trim() + "','" + txtFTPServerUserName.getText().trim() + "','" + txtFTPServerPass.getText().trim() + "','" + allowToCalculateItemWeight + "',' " + showBillsDtlType + "' "
				    + ",'" + printTaxInvoiceOnBill + "','" + printInclusiveOfAllTxesOnBill + "','" + applyDiscountOn + "','" + memberCodeForKotInMposByCardSwipe + "','" + checkPrintBill + "' "
				    + ",'" + useVatAndServiceNoFromPos + "','" + memberCodeForMakeBillInMPOS + "','" + itemWiseKOTYN + "','" + posCode + "','" + CMSPostingType + "' "
				    + ",'" + popUpToApplyPromotionsOnBill + "','" + selectCustomerCodeByCardSwipe + "','" + checkDebitCardBalOnTrans + "','" + pickSettlementsFromPOSMaster + "','" + shiftWiseDayEnd + "' "
				    + ",'" + productionLinkup + "','" + lockDataOnShift + "','" + wsClientCode + "','" + newPropertyPOSCode + "','" + enableBillSeries + "','" + enablePMSIntegrationYN + "','" + printTimeOnBillYN + "'"
				    + ",'" + printTDHItemInBillYN + "','" + printRemarkAndReason + "'," + daysBeforeOrderToCancle + "," + noOfDelDaysForAdvOrder + "," + noOfDelDaysForUrgentOrder + ""
				    + ",'" + setUpToTimeForAdvOrder + "','" + setUpToTimeForUrgentOrder + "','" + upToTimeForAdvOrder + "','" + upToTimeForUrgentOrder + "'"
				    + ",'" + enableBothPrintAndSettleBtnForDB + "','" + strInrestoPOSIntegartionYN + "','" + txtInrestoPOSWesServiceURL.getText().trim() + "','" + txtInrestoPOSId.getText().trim() + "'"
				    + ",'" + txtInrestoPOSKey.getText().trim() + "','" + carryForwardFloatAmtToNextDay + "','" + openCashDrawerAfterBillPrintYN + "','" + propertyWiseSalesOrderYN + "','N','" + showItemDtlsForChangeCustomerOnBillYN + "'"
				    + ",'" + showPopUpForNextItemQuantityYN + "','" + strJioPOSIntegartionYN + "','" + txtJioPOSWesServiceURL.getText().trim() + "','" + txtJioMoneyMID.getText().trim() + "','" + txtJioMoneyTID.getText().trim() + "','" + txtJioActivationCode.getText().trim() + "','" + txtJioDeviceID.getText().trim() + "'"
				    + ",'" + newBillSeriesForNewDay + "','" + showPOSWiseReports + "','" + enableDineIn + "','" + autoAreaSelectionInMakeKOT + "','" + consolidatedKOTPrinterPort + "'"
				    + ",'" + roundOffTo + "','" + showUnSettlementForm + "','" + printOpenItemsOnBill + "','" + printHomeDeliveryYN + "','" + scanQRYN + "','" + areaWisePromotions + "' "
				    + ",'" + printItemOnMoveKOTMoveTable + "','" + showPurRateInDirectBiller + "','" + tableReservationForCustomer + "' "//191
				    + ",'" + autoShowPopItems + "','" + intPOPItemsOfDays + "','" + postMMSDataCostCenterWiseOrLocWise + "','" + effectOfSales + "','" + posWiseItemLinkedUpToMMSProduct + "','" + enableMasterDiscount + "'"
				    + ",'" + enableNFCInterface + "','" + strBenowIntegrationYN + "','" + txtXEmail.getText() + "','" + txtMerchantCode.getText() + "','" + txtAuthenticationKey.getText() + "','" + txtSalt.getText() + "','" + enableLockTables + "'"//204
				    + ",'" + homeDeliveryAreaForDirectBiller + "','" + takeAwayAreaForDirectBiller + "','" + roundOffFinalBillAmount + "','" + dblNoOfDecimalPlace + "','" + sendDBBackupOnClientMail + "','" + printOrderNoOnBillYN + "','" + printDeviceAndUserDtlOnKOTYN + "' "//211
				    + ",'" + removeSCTaxCode + "','" + autoAddKOTToBill + "','" + areaWiseCostCenterKOTPrinting + "','" + weraOnlineOrderIntegrationYN + "','" + weraMerchantOutletId + "','" + weraAuthenticationAPIKey + "' "//217
				    + ",'" + fireCommunication + "','" + usdCurrencyConvertionRate + "','" + dbBackupMailReceiverMailIds + "','" + printMoveTableMoveKOTYN + "','" + printQtyTotal + "')";//222
			}
			//You should change POS master for this

			System.out.println("insert sql=>" + sql);
			int exc = clsGlobalVarClass.dbMysql.execute(sql);
			if (newPropertyPOSCode.equalsIgnoreCase("All"))
			{
			    clsGlobalVarClass.dbMysql.execute("delete from tblsetup where strPOSCode<>'All' ");
			}
			if (exc > 0)
			{
			    new frmOkPopUp(this, "Entry added Successfully", "Successfull", 3).setVisible(true);

			    funFillData();

			}
		    }
		}
	    }
	    else
	    {
		String csize = cmbColumnSize.getSelectedItem().toString();
		int ColumnSize = Integer.valueOf(csize);
		if (!globelvalidate.validateEmpty(txtClientName.getText()))
		{
		    new frmOkPopUp(this, "Please Enter Shop Name ", "Error", 0).setVisible(true);
		}
		else if (!globelvalidate.validateEmpty(txtShopAddressLine1.getText()))
		{
		    new frmOkPopUp(this, "Please Enter Shop Address", "Error", 0).setVisible(true);
		}
		else if (!globelvalidate.validateEmpty(txtTelephone.getText()))
		{
		    new frmOkPopUp(this, "Please Enter Telephone Number", "Error", 0).setVisible(true);
		}
		else if (!globelvalidate.validateEmpty(txtEmailAddress.getText()))
		{
		    new frmOkPopUp(this, "Please Enter Email Address", "Error", 0).setVisible(true);
		}
		else if (!globelvalidate.validateEmail(txtEmailAddress.getText()))
		{
		    new frmOkPopUp(this, "Please Enter valid Email Address", "Error", 0).setVisible(true);
		}
		else if (!globelvalidate.validateEmpty(txtBillFooter.getText()))
		{
		    new frmOkPopUp(this, "Please Enter Bill Footer", "Error", 0).setVisible(true);
		}
		else if (chkPrintVatNo.isSelected() && txtVatNo.getText().length() < 1)
		{
		    new frmOkPopUp(this, "Please Enter Vat No", "Error", 0).setVisible(true);
		}
		else if (maxDiscount > 99)
		{
		    new frmOkPopUp(this, "Max Discount Must Be Less Than 100%", "Error", 0).setVisible(true);
		}
		else if (txtCustSeries.getText().trim().length() == 0)
		{
		    new frmOkPopUp(this, "Please enter Customer Series!", "Error", 0).setVisible(true);
		}
		else if (!funCheckInt(txtCustSeries.getText().trim()))
		{
		    new frmOkPopUp(this, "Please enter Numbers only%", "Error", 0).setVisible(true);
		}
		else
		{

		    if (cmbCRMType.getSelectedIndex() == 0)
		    {
			if (txtGetWebservice.getText().trim().length() == 0)
			{
			    JOptionPane.showMessageDialog(this, "Please Enter Get WebService URL Field!");
			    return;
			}
			else if (txtPostWebservice.getText().trim().length() == 0)
			{
			    JOptionPane.showMessageDialog(this, "Please Enter Post WebService URL Field!");
			    return;
			}
			else if (txtOutletUID.getText().trim().length() == 0)
			{
			    JOptionPane.showMessageDialog(this, "Please Enter Outlet UID Field!");
			    return;
			}
			else if (txtPOSID.getText().trim().length() == 0)
			{
			    JOptionPane.showMessageDialog(this, "Please Enter POSID Field!");
			    return;
			}
		    }
		    if (cmbCMSIntegrationYN.getSelectedIndex() == 1)
		    {
			if (txtCMSWesServiceURL.getText().trim().length() == 0)
			{
			    JOptionPane.showMessageDialog(this, "Please Enter CMS WEB Service URL!");
			    return;
			}
		    }

		    if ("Yes".trim().equalsIgnoreCase(strPOSType.getSelectedItem().toString()))
		    {
			if (!globelvalidate.validateEmpty(txtWebServiceLink.getText()))
			{
			    new frmOkPopUp(this, "Please Enter Web Service Link", "Error", 0).setVisible(true);
			    return;
			}
		    }

		    java.util.Date objDate = new java.util.Date();
		    dteCreated = (objDate.getYear() + 1900) + "-" + (objDate.getMonth() + 1) + "-" + objDate.getDate()
			    + " " + objDate.getHours() + ":" + objDate.getMinutes() + ":" + objDate.getSeconds();

		    dteEdited = (objDate.getYear() + 1900) + "-" + (objDate.getMonth() + 1) + "-" + objDate.getDate()
			    + " " + objDate.getHours() + ":" + objDate.getMinutes() + ":" + objDate.getSeconds();

		    if (chkNegBilling.isSelected())
		    {
			negBilling = "Y";
		    }
		    else
		    {
			negBilling = "N";
		    }
		    if (chkDayEnd.isSelected())
		    {
			dayEnd = "Y";
		    }
		    else
		    {
			dayEnd = "N";
		    }
		    if (chkMultiBillPrint.isSelected())
		    {
			multiBillPrint = "Y";
		    }
		    else
		    {
			multiBillPrint = "N";
		    }
		    if (chkEnableKOT.isSelected())
		    {
			kot = "Y";
		    }
		    else
		    {
			kot = "N";
		    }
		    if (chkEffectOnPSP.isSelected())
		    {
			effectOnPSP = "Y";
		    }
		    else
		    {
			effectOnPSP = "N";
		    }
		    if (chkPrintVatNo.isSelected())
		    {
			printVatNo = "Y";
		    }
		    else
		    {
			printVatNo = "N";
		    }
		    if (chkShowBill.isSelected())
		    {
			showBill = "Y";

		    }
		    else
		    {
			showBill = "N";
		    }
		    if (chkServiceTaxNo.isSelected())
		    {
			printServiceTaxno = "Y";

		    }
		    else
		    {
			printServiceTaxno = "N";
		    }
		    if (chkManualBillNo.isSelected())
		    {
			ManualBillNo = "Y";
		    }
		    else
		    {
			ManualBillNo = "N";
		    }
		    if (chkPrintKotForDirectBiller.isSelected())
		    {
			KOTPrintingForDB = "Y";

		    }
		    else
		    {
			KOTPrintingForDB = "N";
		    }
		    if (cmbServerName.getSelectedItem().equals("Gmail"))
		    {
			ServerHostName = "smtp.gmail.com";
		    }
		    else
		    {
			ServerHostName = "smtp.mail.yahoo.com";
		    }
		    if (cmbRFIDSetup.getSelectedItem().equals("Yes"))
		    {
			rfidInterface = "Y";
			if (!funCheckRFIDValidations())
			{
			    return;
			}
		    }

		    theme = cmbChangeTheme.getSelectedItem().toString();

		    if (txtPincode.getText().isEmpty())
		    {
			pincode = "0";
		    }
		    else
		    {
			pincode = txtPincode.getText();
		    }

		    if (chkAreaWisePricing.isSelected())
		    {
			areaWisePricing = "Y";
		    }
		    else
		    {
			areaWisePricing = "N";
		    }

		    String menuItemSortingOn = "NA";
		    if (cmbMenuItemSortingOn.getSelectedIndex() == 1)
		    {
			menuItemSortingOn = "subgroupWise";
		    }
		    else if (cmbMenuItemSortingOn.getSelectedIndex() == 2)
		    {
			menuItemSortingOn = "subMenuHeadWise";
		    }

		    String printType = cmbPrintType.getSelectedItem().toString();
		    String smsAPI = txtAreaSMSApi.getText().trim();

		    if (chkEditHomeDelivery.isSelected())
		    {
			editHomeDelivery = "Y";
		    }
		    if (chkSlabBasedHomeDelCharges.isSelected())
		    {
			slabBasedHomeDelCharges = "Y";
		    }

		    String skipWaiterSelection = "N";
		    if (chkSkip_Waiter_Selection.isSelected())
		    {
			skipWaiterSelection = "Y";
		    }

		    String strDirectKOTPrintMakeKOT = "N";

		    String strSkipPaxSelection = "N";
		    if (chkSkip_pax_selection.isSelected())
		    {
			strSkipPaxSelection = "Y";
		    }

		    if (cmbStockInOption.getSelectedIndex() == 0)
		    {
			stockInOption = "ItemWise";
		    }
		    else if (cmbStockInOption.getSelectedIndex() == 1)
		    {
			stockInOption = "MenuHeadWise";
		    }

		    int advRePrintCount = (Integer.parseInt(txtAdvRecPrintCount.getText()));
		    String billFormatType = cmbBillFormatType.getSelectedItem().toString();

		    String activePromotions = "N";
		    if (chkActivePromotions.isSelected())
		    {
			activePromotions = "Y";
		    }

		    String sendHomeDel = "N";
		    if (chkHomeDelSMS.isSelected())
		    {
			sendHomeDel = "Y";
		    }

		    String sendBillSettlement = "N";
		    if (chkBillSettlementSMS.isSelected())
		    {
			sendBillSettlement = "Y";
		    }
		    String smsType = cmbSMSType.getSelectedItem().toString();

		    String printShortNameOnKOT = "N";
		    if (chkPrintShortNameOnKOT.isSelected())
		    {
			printShortNameOnKOT = "Y";
		    }

		    String showCustHelpOnTrans = "N";
		    /*if (chkShowCustHelp.isSelected())
                     {
                     showCustHelpOnTrans = "Y";
                     }*/

		    String printOnVoidBill = "N";
		    if (chkPrintForVoidBill.isSelected())
		    {
			printOnVoidBill = "Y";
		    }

		    String postSalesDataToMMS = "N";
		    if (chkPostSalesDataToMMS.isSelected())
		    {
			postSalesDataToMMS = "Y";
		    }

		    String custAreaMasterCompulsory = "N";
		    if (chkAreaMasterCompulsory.isSelected())
		    {
			custAreaMasterCompulsory = "Y";
		    }
		    String priceFrom = cmbPriceFrom.getSelectedItem().toString();

		    String printerErrorMessage = "N";
		    if (chkPrinterErrorMessage.isSelected())
		    {
			printerErrorMessage = "Y";
		    }

		    String ChangeQtyForExternalCode = "N";
		    if (chkChangeQtyForExternalCode.isSelected())
		    {
			ChangeQtyForExternalCode = "Y";
		    }

		    String PointsOnBillPrint = "N";
		    if (chkPointsOnBillPrint.isSelected())
		    {
			PointsOnBillPrint = "Y";
		    }

		    String touchScreenMode = "N";

		    String cardInterfaceType = cmbCardIntfType.getSelectedItem().toString().trim();
		    String strCMSIntegartionYN = "N";
		    if (cmbCMSIntegrationYN.getSelectedIndex() == 1)
		    {
			strCMSIntegartionYN = "Y";
		    }
		    String manualAdvOrderNo = "N";
		    if (chkManualAdvOrderCompulsory.isSelected())
		    {
			manualAdvOrderNo = "Y";
		    }

		    String printManualAdvOrderNoOnBill = "N";
		    if (chkPrintManualAdvOrderOnBill.isSelected())
		    {
			printManualAdvOrderNoOnBill = "Y";
		    }

		    String printModQtyOnKOT = "N";
		    if (chkPrintModifierQtyOnKOT.isSelected())
		    {
			printModQtyOnKOT = "Y";
		    }

		    String multiKOTPrint = "N";
		    if (chkMultiKOTPrint.isSelected())
		    {
			multiKOTPrint = "Y";
		    }

		    String ItemQtyNumpad = "N";
		    if (chkItemQtyNumpad.isSelected())
		    {
			ItemQtyNumpad = "Y";
		    }

		    String memberAsTable = "N";
		    if (chkMemberAsTable.isSelected())
		    {
			memberAsTable = "Y";
		    }

		    String KOTToLocalPrinter = "N";
		    if (chkPrintKOTToLocalPrinter.isSelected())
		    {
			KOTToLocalPrinter = "Y";
		    }

		    String settleBtnForDirectBillerBill = "N";
		    if (chkEnableSettleBtnForDirectBillerBill.isSelected())
		    {
			settleBtnForDirectBillerBill = "Y";
		    }

		    String delBoySelCompulsoryOnDirectBiller = "N";
		    if (chkDelBoyCompulsoryOnDirectBiller.isSelected())
		    {
			delBoySelCompulsoryOnDirectBiller = "Y";
		    }

		    String memberCodeForKOTJPOS = "N";
		    if (chkMemberCodeForKOTJPOS.isSelected())
		    {
			memberCodeForKOTJPOS = "Y";
		    }

		    String memberCodeForKOTMPOS = "N";
		    if (chkMemberCodeForKOTMPOS.isSelected())
		    {
			memberCodeForKOTMPOS = "Y";
		    }

		    String dontShowAdvOrderOfOtherPOS = "N";
		    if (chkDontShowAdvOrderInOtherPOS.isSelected())
		    {
			dontShowAdvOrderOfOtherPOS = "Y";
		    }

		    String printZeroAmtModifierOnBill = "N";
		    if (chkPrintZeroAmtModifierInBill.isSelected())
		    {
			printZeroAmtModifierOnBill = "Y";
		    }

		    String printKOTYN = "Y";
		    if (chkPrintKOTYN.isSelected())
		    {
			printKOTYN = "Y";
		    }
		    else
		    {
			printKOTYN = "N";
		    }

		    String slipNoCompulsoryYN = "N";
		    if (chkSlipNoForCreditCardBillYN.isSelected())
		    {
			slipNoCompulsoryYN = "Y";
		    }

		    String expDateCompulsoryYN = "N";
		    if (chkExpDateForCreditCardBillYN.isSelected())
		    {
			expDateCompulsoryYN = "Y";
		    }

		    String selectWaiterFromCardSwipe = "N";
		    if (chkSelectWaiterFromCardSwipe.isSelected())
		    {
			selectWaiterFromCardSwipe = "Y";
		    }

		    String multiWaiterSelOnMakeKOT = "N";
		    if (chkMultipleWaiterSelectionOnMakeKOT.isSelected())
		    {
			multiWaiterSelOnMakeKOT = "Y";
		    }

		    String moveTableToOtherPOS = "N";
		    if (chkMoveTableToOtherPOS.isSelected())
		    {
			moveTableToOtherPOS = "Y";
		    }

		    String moveKOTToOtherPOS = "N";
		    if (chkMoveKOTToOtherPOS.isSelected())
		    {
			moveKOTToOtherPOS = "Y";
		    }

		    String calculateTaxOnMakeKOT = "N";
		    if (chkCalculateTaxOnMakeKOT.isSelected())
		    {
			calculateTaxOnMakeKOT = "Y";
		    }

		    String calculateDiscItemWise = "N";
		    if (chkCalculateDiscItemWise.isSelected())
		    {
			calculateDiscItemWise = "Y";
		    }

		    /*
                     if(txtReportImagePath.getText().isEmpty())
                     {
                     File imgfile = new File(txtReportImagePath.getText().trim());
                     FileInputStream fin = new FileInputStream(imgfile);
                     }*/
		    String takewayCustomerSelection = "N";
		    if (chkTakewayCustomerSelection.isSelected())
		    {
			takewayCustomerSelection = "Y";
		    }

		    String showItemStkColumnInDB = "N";
		    if (chkShowItemStkColumnInDB.isSelected())
		    {
			showItemStkColumnInDB = "Y";
		    }
		    String itemType = cmbItemType.getSelectedItem().toString();

		    String allowNewAreaMasterFromCustMaster = "N";
		    if (chkBoxAllowNewAreaMasterFromCustMaster.isSelected())
		    {
			allowNewAreaMasterFromCustMaster = "Y";
		    }

		    String custAddressSelectionForBill = "N";
		    if (chkSelectCustAddressForBill.isSelected())
		    {
			custAddressSelectionForBill = "Y";
		    }

		    String genrateMI = "N";
		    if (chkGenrateMI.isSelected())
		    {
			genrateMI = "Y";
		    }

		    String allowToCalculateItemWeight = "N";
		    if (chkAllowToCalculateItemWeight.isSelected())
		    {
			allowToCalculateItemWeight = "Y";
		    }
		    String showBillsDtlType = cmbShowBillsDtlType.getSelectedItem().toString();

		    String printTaxInvoiceOnBill = "Y";
		    if (!chkPrintInvoiceOnBill.isSelected())
		    {
			printTaxInvoiceOnBill = "N";
		    }
		    String printInclusiveOfAllTxesOnBill = "Y";
		    if (!chkPrintInclusiveOfAllTaxesOnBill.isSelected())
		    {
			printInclusiveOfAllTxesOnBill = "N";
		    }

		    String applyDiscountOn = cmbApplyDiscountOn.getSelectedItem().toString();

		    String memberCodeForKotInMposByCardSwipe = "N";
		    if (chkMemberCodeForKotInMposByCardSwipe.isSelected())
		    {
			memberCodeForKotInMposByCardSwipe = "Y";
		    }

		    String checkPrintBill = "N";
		    if (chkPrintBill.isSelected())
		    {
			checkPrintBill = "Y";
		    }

		    String useVatAndServiceNoFromPos = "N";
		    if (chkUseVatAndServiceNoFromPos.isSelected())
		    {
			useVatAndServiceNoFromPos = "Y";
		    }

		    String memberCodeForMakeBillInMPOS = "N";
		    if (chkMemberCodeForMakeBillInMPOS.isSelected())
		    {
			memberCodeForMakeBillInMPOS = "Y";
		    }

		    //Date dt=new Date();
		    //String time=dt.getHours()+":"+dt.getMinutes()+":"+dt.getSeconds();
		    String HOServerDate = (dteHOServerDate.getDate().getYear() + 1900) + "-" + (dteHOServerDate.getDate().getMonth() + 1)
			    + "-" + (dteHOServerDate.getDate().getDate()) + " " + HOServerTime;

		    String itemWiseKOTYN = "N";
		    if (chkItemWiseKOTPrintYN.isSelected())
		    {
			itemWiseKOTYN = "Y";
		    }

		    String posCode = mapPOS.get(cmbPOSForDayEnd.getSelectedItem().toString());

		    String CMSPostingType = "";
		    if (cmbCMSPostingType.getSelectedItem().equals("Sanguine CMS"))
		    {
			CMSPostingType = "Sanguine CMS";
			// clsGlobalVarClass.gCMSPostingType=Sanguine CMS
		    }
		    else
		    {
			CMSPostingType = "Others";
		    }

		    String popUpToApplyPromotionsOnBill = "N";
		    if (chkPopUpToApplyPromotionsOnBill.isSelected())
		    {
			popUpToApplyPromotionsOnBill = "Y";
		    }

		    String selectCustomerCodeByCardSwipe = "N";
		    if (chkSelectCustomerCodeFromCardSwipe.isSelected())
		    {
			selectCustomerCodeByCardSwipe = "Y";
		    }

		    String checkDebitCardBalOnTrans = "N";
		    if (chkCheckDebitCardBalOnTrans.isSelected())
		    {
			checkDebitCardBalOnTrans = "Y";
		    }

		    String pickSettlementsFromPOSMaster = "N";
		    if (chkSettlementsFromPOSMaster.isSelected())
		    {
			pickSettlementsFromPOSMaster = "Y";
		    }
		    String shiftWiseDayEnd = "N";
		    if (chkShiftWiseDayEnd.isSelected())
		    {
			shiftWiseDayEnd = "Y";
		    }

		    String productionLinkup = "N";
		    if (chkProductionLinkup.isSelected())
		    {
			productionLinkup = "Y";
		    }

		    String lockDataOnShift = "Y";
		    if (chkLockDataOnShift.isSelected())
		    {
			lockDataOnShift = "Y";
		    }
		    else
		    {
			lockDataOnShift = "N";
		    }

		    String wsClientCode = txtWSClientCode.getText().toString();
		    String enableBillSeries = "N";
		    if (chkEnableBillSeries.isSelected())
		    {
			enableBillSeries = "Y";
		    }
		    else
		    {
			enableBillSeries = "N";
		    }
		    String enablePMSIntegrationYN = "N";
		    if (chkEnablePMSIntegration.isSelected())
		    {
			enablePMSIntegrationYN = "Y";
		    }
		    else
		    {
			enablePMSIntegrationYN = "N";
		    }

		    String printTimeOnBillYN = "N";
		    if (chkPrintTimeOnBill.isSelected())
		    {
			printTimeOnBillYN = "Y";
		    }
		    else
		    {
			printTimeOnBillYN = "N";
		    }

		    String printTDHItemInBillYN = "N";
		    if (chkPrintTDHItemsInBill.isSelected())
		    {
			printTDHItemInBillYN = "Y";
		    }
		    else
		    {
			printTDHItemInBillYN = "N";
		    }

		    boolean flgPrintRemarkAndReason = false;
		    if (chkPrintRemarkAndReasonForReprint.isSelected())
		    {
			flgPrintRemarkAndReason = true;
		    }
		    else
		    {
			flgPrintRemarkAndReason = false;
		    }

		    int daysBeforeOrderToCancel = Integer.parseInt(txtDaysBeforeOrderToCancel.getText());

		    String setUpToTimeForAdvOrder = "N";
		    if (chkSetUpToTimeForAdvOrder.isSelected())
		    {
			setUpToTimeForAdvOrder = "Y";
		    }

		    String setUpToTimeForUrgentOrder = "N";
		    if (chkSetUpToTimeForUrgentOrder.isSelected())
		    {
			setUpToTimeForUrgentOrder = "Y";
		    }

		    String upToTimeForAdvOrder = "00:00 AM";
		    upToTimeForAdvOrder = cmbHours.getSelectedItem().toString() + ":" + cmbMinutes.getSelectedItem().toString() + " " + cmbAMPM.getSelectedItem().toString();

		    String upToTimeForUrgentOrder = "00:00 AM";
		    upToTimeForUrgentOrder = cmbHoursUrgentOrder.getSelectedItem().toString() + ":" + cmbMinutesUrgentOrder.getSelectedItem().toString() + " " + cmbAMPMUrgent.getSelectedItem().toString();

		    String enableBothPrintAndSettleBtnForDB = "N";
		    if (chkEnableBothPrintAndSettleBtnForDB.isSelected())
		    {
			enableBothPrintAndSettleBtnForDB = "Y";
		    }

		    String strInrestoPOSIntegartionYN = "N";
		    if (cmbInrestoPOSIntegrationYN.getSelectedIndex() == 1)
		    {
			strInrestoPOSIntegartionYN = "Y";
		    }

		    String carryForwardFloatAmtToNextDay = "N";
		    if (chkCarryForwardFloatAmtToNextDay.isSelected())
		    {
			carryForwardFloatAmtToNextDay = "Y";
		    }
		    String openCashDrawerAfterBillPrintYN = "N";
		    if (chkOpenCashDrawerAfterBillPrint.isSelected())
		    {
			openCashDrawerAfterBillPrintYN = "Y";
		    }

		    String propertyWiseSalesOrderYN = "N";
		    if (chkPropertyWiseSalesOrder.isSelected())
		    {
			propertyWiseSalesOrderYN = "Y";
		    }
		    String showItemDtlsForChangeCustomerOnBillYN = "N";
		    if (chkShowItemDtlsForChangeCustomerOnBill.isSelected())
		    {
			showItemDtlsForChangeCustomerOnBillYN = "Y";
		    }

		    String showPopUpForNextItemQuantityYN = "N";
		    if (chkShowPopUpForNextItemQuantity.isSelected())
		    {
			showPopUpForNextItemQuantityYN = "Y";
		    }
		    String strJioPOSIntegartionYN = "N";
		    if (cmbJioPOSIntegrationYN.getSelectedIndex() == 1)
		    {
			strJioPOSIntegartionYN = "Y";
		    }
		    String newBillSeriesForNewDay = "N";
		    if (chkNewBillSeriesForNewDay.isSelected())
		    {
			newBillSeriesForNewDay = "Y";
		    }
		    String showPOSWiseReports = "N";
		    if (chkShowOnlyLoginPOSReports.isSelected())
		    {
			showPOSWiseReports = "Y";
		    }
		    String enableDineIn = "N";
		    if (chkEnableDineIn.isSelected())
		    {
			enableDineIn = "Y";
		    }
		    String autoAreaSelectionInMakeKOT = "N";
		    if (chkAutoAreaSelectionInMakeKOT.isSelected())
		    {
			autoAreaSelectionInMakeKOT = "Y";
		    }

		    String strBenowIntegrationYN = "N";
		    if (cmbBenowPOSIntegrationYN.getSelectedIndex() == 1)
		    {
			strBenowIntegrationYN = "Y";
		    }
		    if (cmbBenowPOSIntegrationYN.getSelectedIndex() == 1)
		    {
			if (txtXEmail.getText().trim().length() == 0)
			{
			    JOptionPane.showMessageDialog(this, "Please Enter X-Email!!!");
			    return;
			}

			if (txtMerchantCode.getText().trim().length() == 0)
			{
			    JOptionPane.showMessageDialog(this, "Please Enter Benow Merchant Code!!!");
			    return;
			}

			if (txtAuthenticationKey.getText().trim().length() == 0)
			{
			    JOptionPane.showMessageDialog(this, "Please Enter Authentication Key For Benow!!!");
			    return;
			}

			if (txtSalt.getText().trim().length() == 0)
			{
			    JOptionPane.showMessageDialog(this, "Please Enter Salt For Benow!!!");
			    return;
			}
		    }

		    boolean isAuditSMS = isAuditSMSSelected();
		    if (isAuditSMS)
		    {
			String[] mobileNos = txtSMSMobileNo.getText().split(",");
			for (int i = 0; i < mobileNos.length; i++)
			{
			    if (!mobileNos[i].matches("\\d{10}"))
			    {
				new frmOkPopUp(null, "Please Enter Valid Mobile Number.", "Error", 1).setVisible(true);
				return;
			    }
			}
		    }

		    funSaveAuditSMS();

		    funSavePrinterSetupData();
		    funSavePropertyImage(reportImgInputStream);
		    //funSaveDayEndReportsConfig();

		    if (!selectedLikePOSCode.equals("NA"))
		    {
			int cnt = 0;
			String sqlDelete = "delete from tblsetup where strPOSCode='" + newPropertyPOSCode + "' ";
			clsGlobalVarClass.dbMysql.execute(sqlDelete);
			String sqlLikePOS = "select * from  tblsetup where strPOSCode='" + selectedLikePOSCode + "' ";
			ResultSet rsLikePOS = clsGlobalVarClass.dbMysql.executeResultSet(sqlLikePOS);
			while (rsLikePOS.next())
			{
			    cnt++;
			    sql = "insert into tblsetup(strClientCode,strClientName,strAddressLine1,strAddressLine2 "//4
				    + ",strAddressLine3,strEmail,strBillFooter,strBillFooterStatus,intBillPaperSize "//9
				    + ",strNegativeBilling,strDayEnd,strPrintMode,strDiscountNote,strCityName "//14
				    + ",strState,strCountry,intTelephoneNo,dteStartDate,dteEndDate "//19
				    + ",strNatureOfBusinnes,strMultipleBillPrinting,strEnableKOT,strEffectOnPSP,strPrintVatNo "//24
				    + ",strVatNo,strShowBill,strPrintServiceTaxNo,strServiceTaxNo,strManualBillNo "//29
				    + ",strMenuItemDispSeq,strSenderEmailId,strEmailPassword,strConfirmEmailPassword,strBody "//34
				    + ",strEmailServerName,strSMSApi,strUserCreated,strUserEdited,dteDateCreated "//39
				    + ",dteDateEdited ,strPOSType,strWebServiceLink,strDataSendFrequency,dteHOServerDate "//44
				    + ",strRFID,strServerName,strDBUserName,strDBPassword,strDatabaseName "//49
				    + ",strEnableKOTForDirectBiller,intPinCode,strChangeTheme,dblMaxDiscount,strAreaWisePricing "//54
				    + ",strMenuItemSortingOn,strDirectAreaCode,intColumnSize,strPrintType,strEditHomeDelivery "//59
				    + ",strSlabBasedHDCharges,strSkipWaiterAndPax,strSkipWaiter,strDirectKOTPrintMakeKOT,strSkipPax "//64
				    + ",strCRMInterface,strGetWebserviceURL,strPostWebserviceURL,strOutletUID,strPOSID "//69
				    + ",strStockInOption,longCustSeries,intAdvReceiptPrintCount,strHomeDeliverySMS,strBillStettlementSMS "//74
				    + ",strBillFormatType,strActivePromotions,strSendHomeDelSMS,strSendBillSettlementSMS,strSMSType "//79
				    + ",strPrintShortNameOnKOT,strShowCustHelp,strPrintOnVoidBill,strPostSalesDataToMMS,strCustAreaMasterCompulsory "//84
				    + ",strPriceFrom,strShowPrinterErrorMessage,strTouchScreenMode,strCardInterfaceType,strCMSIntegrationYN "//89
				    + ",strCMSWebServiceURL,strChangeQtyForExternalCode,strPointsOnBillPrint,strCMSPOSCode,strManualAdvOrderNoCompulsory "//94
				    + ",strPrintManualAdvOrderNoOnBill,strPrintModifierQtyOnKOT,strNoOfLinesInKOTPrint,strMultipleKOTPrintYN,strItemQtyNumpad "//99
				    + ",strTreatMemberAsTable,strKOTToLocalPrinter,blobReportImage,strSettleBtnForDirectBillerBill,strDelBoySelCompulsoryOnDirectBiller "//104
				    + ",strCMSMemberForKOTJPOS,strCMSMemberForKOTMPOS,strDontShowAdvOrderInOtherPOS,strPrintZeroAmtModifierInBill,strPrintKOTYN "//109
				    + ",strCreditCardSlipNoCompulsoryYN,strCreditCardExpiryDateCompulsoryYN,strSelectWaiterFromCardSwipe,strMultiWaiterSelectionOnMakeKOT,strMoveTableToOtherPOS "//114
				    + ",strMoveKOTToOtherPOS,strCalculateTaxOnMakeKOT,strReceiverEmailId,strCalculateDiscItemWise,strTakewayCustomerSelection "//119
				    + ",StrShowItemStkColumnInDB,strItemType,strAllowNewAreaMasterFromCustMaster,strCustAddressSelectionForBill,strGenrateMI "//124
				    + ",strFTPAddress,strFTPServerUserName,strFTPServerPass,strAllowToCalculateItemWeight,strShowBillsDtlType "//129
				    + ",strPrintTaxInvoiceOnBill,strPrintInclusiveOfAllTaxesOnBill,strApplyDiscountOn,strMemberCodeForKotInMposByCardSwipe,strPrintBillYN "//134
				    + ",strVatAndServiceTaxFromPos,strMemberCodeForMakeBillInMPOS,strItemWiseKOTYN,strLastPOSForDayEnd,strCMSPostingType "//139
				    + ",strPopUpToApplyPromotionsOnBill,strSelectCustomerCodeFromCardSwipe,strCheckDebitCardBalOnTransactions,strSettlementsFromPOSMaster,strShiftWiseDayEndYN "//144
				    + ",strProductionLinkup,strLockDataOnShift,strWSClientCode,strPOSCode,strEnableBillSeries,strEnablePMSIntegrationYN,strPrintTimeOnBill"
				    + ",strPrintTDHItemsInBill,strPrintRemarkAndReasonForReprint,intDaysBeforeOrderToCancel "//154
				    + ",intNoOfDelDaysForAdvOrder,intNoOfDelDaysForUrgentOrder,strSetUpToTimeForAdvOrder,strSetUpToTimeForUrgentOrder "//158
				    + ",strUpToTimeForAdvOrder,strUpToTimeForUrgentOrder,strEnableBothPrintAndSettleBtnForDB,strInrestoPOSIntegrationYN "//162
				    + ",strInrestoPOSWebServiceURL,strInrestoPOSId,strInrestoPOSKey,strCarryForwardFloatAmtToNextDay,strOpenCashDrawerAfterBillPrintYN" //167
				    + ",strPropertyWiseSalesOrderYN,strDataPostFlag,strShowItemDetailsGrid,strShowPopUpForNextItemQuantity,strJioMoneyIntegration,strJioWebServiceUrl,strJioMID,strJioTID,strJioActivationCode,strJioDeviceID "//177
				    + ",strNewBillSeriesForNewDay,strShowReportsPOSWise,strEnableDineIn,strAutoAreaSelectionInMakeKOT,strConsolidatedKOTPrinterPort"//182
				    + ",dblRoundOff,strShowUnSettlementForm,strPrintOpenItemsOnBill,strPrintHomeDeliveryYN,strScanQRYN,strAreaWisePromotions"//188
				    + ",strPrintItemsOnMoveKOTMoveTable,strShowPurRateInDirectBiller,strEnableTableReservationForCustomer "//191
				    + ",strAutoShowPopItems,intShowPopItemsOfDays,strPostSalesCostOrLoc,strEffectOfSales,strPOSWiseItemToMMSProductLinkUpYN,strEnableMasterDiscount"//197
				    + ",strEnableNFCInterface,strBenowIntegrationYN,strXEmail,strMerchantCode,strAuthenticationKey,strSalt,strEnableLockTable"//206
				    + ",strHomeDeliveryAreaForDirectBiller,strTakeAwayAreaForDirectBiller,strRoundOffBillFinalAmt,dblNoOfDecimalPlace,strSendDBBackupOnClientMail,strPrintOrderNoOnBillYN,strPrintDeviceAndUserDtlOnKOTYN "//211
				    + ",strRemoveSCTaxCode,strAutoAddKOTToBill,strAreaWiseCostCenterKOTPrintingYN,strWERAOnlineOrderIntegration,strWERAMerchantOutletId,strWERAAuthenticationAPIKey"//217
				    + ",strFireCommunication,dblUSDConverionRate,strDBBackupMailReceiver,strPrintMoveTableMoveKOTYN,strPrintQtyTotal )"//222
				    + "values('" + rsLikePOS.getString(1) + "','" + rsLikePOS.getString(2) + "','" + rsLikePOS.getString(3) + "','" + rsLikePOS.getString(4) + "' "
				    + ",'" + rsLikePOS.getString(5) + "','" + rsLikePOS.getString(6) + "','" + rsLikePOS.getString(7) + "','" + rsLikePOS.getString(8) + "','" + rsLikePOS.getString(9) + "' "
				    + ",'" + rsLikePOS.getString(10) + "','" + rsLikePOS.getString(11) + "','" + rsLikePOS.getString(12) + "','" + rsLikePOS.getString(13) + "','" + rsLikePOS.getString(14) + "' "
				    + ",'" + rsLikePOS.getString(15) + "','" + rsLikePOS.getString(16) + "','" + rsLikePOS.getString(17) + "','" + rsLikePOS.getString(18) + "','" + rsLikePOS.getString(19) + "' "
				    + ",'" + rsLikePOS.getString(20) + "','" + rsLikePOS.getString(21) + "','" + rsLikePOS.getString(22) + "','" + rsLikePOS.getString(23) + "','" + rsLikePOS.getString(24) + "' "
				    + ",'" + rsLikePOS.getString(25) + "','" + rsLikePOS.getString(26) + "','" + rsLikePOS.getString(27) + "','" + rsLikePOS.getString(28) + "','" + rsLikePOS.getString(29) + "' "
				    + ",'" + rsLikePOS.getString(30) + "','" + rsLikePOS.getString(31) + "','" + rsLikePOS.getString(32) + "','" + rsLikePOS.getString(33) + "','" + rsLikePOS.getString(34) + "'"
				    + ",'" + rsLikePOS.getString(35) + "','" + rsLikePOS.getString(36) + "','" + rsLikePOS.getString(37) + "','" + rsLikePOS.getString(38) + "','" + rsLikePOS.getString(39) + "' "
				    + ",'" + rsLikePOS.getString(40) + "','" + rsLikePOS.getString(41) + "','" + rsLikePOS.getString(42) + "','" + rsLikePOS.getString(43) + "','" + rsLikePOS.getString(44) + "' "
				    + ",'" + rsLikePOS.getString(45) + "','" + rsLikePOS.getString(46) + "','" + rsLikePOS.getString(47) + "','" + rsLikePOS.getString(48) + "','" + rsLikePOS.getString(49) + "' "
				    + ",'" + rsLikePOS.getString(50) + "','" + rsLikePOS.getString(51) + "','" + rsLikePOS.getString(52) + "','" + rsLikePOS.getString(53) + "','" + rsLikePOS.getString(54) + "' "
				    + ",'" + rsLikePOS.getString(55) + "','" + rsLikePOS.getString(56) + "'," + "'" + rsLikePOS.getString(57) + "','" + rsLikePOS.getString(58) + "','" + rsLikePOS.getString(59) + "' "
				    + ",'" + rsLikePOS.getString(60) + "','" + rsLikePOS.getString(61) + "','" + rsLikePOS.getString(62) + "','" + rsLikePOS.getString(63) + "'," + "'" + rsLikePOS.getString(64) + "' "
				    + ",'" + rsLikePOS.getString(65) + "','" + rsLikePOS.getString(66) + "'" + ",'" + rsLikePOS.getString(67) + "','" + rsLikePOS.getString(68) + "','" + rsLikePOS.getString(69) + "' "
				    + ",'" + rsLikePOS.getString(70) + "'" + ",'" + rsLikePOS.getString(71) + "','" + rsLikePOS.getString(72) + "','" + rsLikePOS.getString(73) + "','" + rsLikePOS.getString(74) + "' "
				    + ",'" + rsLikePOS.getString(75) + "','" + rsLikePOS.getString(76) + "','" + rsLikePOS.getString(77) + "','" + rsLikePOS.getString(78) + "','" + rsLikePOS.getString(79) + "' "
				    + ",'" + rsLikePOS.getString(80) + "','" + rsLikePOS.getString(81) + "','" + rsLikePOS.getString(82) + "','" + rsLikePOS.getString(83) + "','" + rsLikePOS.getString(84) + "' "
				    + ",'" + rsLikePOS.getString(85) + "','" + rsLikePOS.getString(86) + "','" + rsLikePOS.getString(87) + "','" + rsLikePOS.getString(88) + "','" + rsLikePOS.getString(89) + "'"
				    + ",'" + rsLikePOS.getString(90) + "','" + rsLikePOS.getString(91) + "','" + rsLikePOS.getString(92) + "','" + rsLikePOS.getString(93) + "','" + rsLikePOS.getString(94) + "' "
				    + ",'" + rsLikePOS.getString(95) + "','" + rsLikePOS.getString(96) + "','" + rsLikePOS.getString(97) + "','" + rsLikePOS.getString(98) + "','" + rsLikePOS.getString(99) + "','" + rsLikePOS.getString(100) + "' "
				    + ",'" + rsLikePOS.getString(101) + "','" + rsLikePOS.getString(102) + "','" + rsLikePOS.getString(103) + "','" + rsLikePOS.getString(104) + "','" + rsLikePOS.getString(105) + "' "
				    + ",'" + rsLikePOS.getString(106) + "','" + rsLikePOS.getString(107) + "','" + rsLikePOS.getString(108) + "','" + rsLikePOS.getString(109) + "','" + rsLikePOS.getString(110) + "'"
				    + ",'" + rsLikePOS.getString(111) + "','" + rsLikePOS.getString(112) + "','" + rsLikePOS.getString(113) + "','" + rsLikePOS.getString(114) + "','" + rsLikePOS.getString(115) + "' "
				    + ",'" + rsLikePOS.getString(116) + "','" + rsLikePOS.getString(117) + "','" + rsLikePOS.getString(118) + "','" + rsLikePOS.getString(119) + "','" + rsLikePOS.getString(120) + "' "
				    + ",'" + rsLikePOS.getString(121) + "','" + rsLikePOS.getString(122) + "','" + rsLikePOS.getString(123) + "','" + rsLikePOS.getString(124) + "','" + rsLikePOS.getString(125) + "' "
				    + ",'" + rsLikePOS.getString(126) + "','" + rsLikePOS.getString(127) + "','" + rsLikePOS.getString(128) + "','" + rsLikePOS.getString(129) + "',' " + rsLikePOS.getString(130) + "' "
				    + ",'" + rsLikePOS.getString(131) + "','" + rsLikePOS.getString(132) + "','" + rsLikePOS.getString(133) + "','" + rsLikePOS.getString(134) + "','" + rsLikePOS.getString(135) + "' "
				    + ",'" + rsLikePOS.getString(136) + "','" + rsLikePOS.getString(137) + "','" + posCode + "','" + rsLikePOS.getString(139) + "' "
				    + ",'" + rsLikePOS.getString(140) + "','" + rsLikePOS.getString(141) + "','" + rsLikePOS.getString(142) + "','" + rsLikePOS.getString(143) + "','" + rsLikePOS.getString(144) + "' "
				    + ",'" + rsLikePOS.getString(145) + "','" + rsLikePOS.getString(146) + "','" + rsLikePOS.getString(147) + "','" + newPropertyPOSCode + "','" + rsLikePOS.getString(149) + "'"
				    + ",'" + rsLikePOS.getString(150) + "','" + rsLikePOS.getString(151) + "','" + rsLikePOS.getString(152) + "','" + rsLikePOS.getString(153) + "'"
				    + ",'" + rsLikePOS.getString(154) + "','" + rsLikePOS.getString(155) + "','" + rsLikePOS.getString(156) + "','" + rsLikePOS.getString(157) + "'"
				    + ",'" + rsLikePOS.getString(158) + "','" + rsLikePOS.getString(159) + "','" + rsLikePOS.getString(160) + "'"
				    + ",'" + rsLikePOS.getString(161) + "','" + rsLikePOS.getString(162) + "','" + rsLikePOS.getString(163) + "'"
				    + ",'" + rsLikePOS.getString(164) + "','" + rsLikePOS.getString(165) + "','" + rsLikePOS.getString(166) + "'"
				    + ",'" + rsLikePOS.getString(167) + "','" + rsLikePOS.getString(168) + "','" + rsLikePOS.getString(169) + "','" + rsLikePOS.getString(170) + "','" + rsLikePOS.getString(171) + "'"
				    + ",'" + rsLikePOS.getString(172) + "','" + rsLikePOS.getString(173) + "','" + rsLikePOS.getString(174) + "','" + rsLikePOS.getString(175) + "','" + rsLikePOS.getString(176) + "','" + rsLikePOS.getString(177) + "'"
				    + ",'" + rsLikePOS.getString(178) + "','" + rsLikePOS.getString(179) + "','" + rsLikePOS.getString(180) + "'"
				    + ",'" + rsLikePOS.getString(181) + "','" + rsLikePOS.getString(182) + "','" + rsLikePOS.getString(183) + "'"
				    + ",'" + rsLikePOS.getString(184) + "','" + rsLikePOS.getString(185) + "','" + rsLikePOS.getString(186) + "','" + rsLikePOS.getString(187) + "'"
				    + ", '" + rsLikePOS.getString(188) + "','" + rsLikePOS.getString(189) + "','" + rsLikePOS.getString(190) + "','" + rsLikePOS.getString(191) + "' "//191
				    + ",'" + rsLikePOS.getString(192) + "','" + rsLikePOS.getString(193) + "','" + rsLikePOS.getString(194) + "','" + rsLikePOS.getString(195) + "','" + rsLikePOS.getString(196) + "','" + rsLikePOS.getString(197) + "'"//197
				    + ",'" + rsLikePOS.getString(198) + "','" + rsLikePOS.getString(199) + "','" + rsLikePOS.getString(200) + "','" + rsLikePOS.getString(201) + "','" + rsLikePOS.getString(202) + "','" + rsLikePOS.getString(203) + "','" + rsLikePOS.getString(204) + "'"//204
				    + ",'" + rsLikePOS.getString(205) + "','" + rsLikePOS.getString(206) + "','" + rsLikePOS.getString(207) + "','" + rsLikePOS.getString(208) + "','" + rsLikePOS.getString(209) + "','" + rsLikePOS.getString(210) + "','" + rsLikePOS.getString(211) + "' "//211
				    + ",'" + rsLikePOS.getString(212) + "','" + rsLikePOS.getString(213) + "','" + rsLikePOS.getString(214) + "','" + rsLikePOS.getString(215) + "','" + rsLikePOS.getString(216) + "','" + rsLikePOS.getString(217) + "'"//217
				    + ",'" + rsLikePOS.getString(218) + "','" + rsLikePOS.getString(219) + "','" + rsLikePOS.getString(220) + "','" + rsLikePOS.getString(221) + "','" + rsLikePOS.getString(222) + "')";//222
			}

			if (cnt > 0)
			{
			    clsGlobalVarClass.dbMysql.execute(sql);
			    if (gHOPOSType.equals("Client POS"))
			    {
				funPostPropertySetupDataToHO();
				funPostBillSeriesDataHO();
			    }
			}
		    }
		    else
		    {
			String sqlUpdate = "UPDATE tblsetup "
				+ "SET strClientName = ?,strAddressLine1 = ?,strAddressLine2 = ?"
				+ ",strAddressLine3 = ?,strEmail = ?,strBillFooter = ?"
				+ ",strBillFooterStatus = ?,intBillPaperSize = ?,strNegativeBilling = ?"
				+ ",strDayEnd = ?,strPrintMode = ?,strDiscountNote = ?"
				+ ",strCityName = ?,strState = ?,strCountry = ?"
				+ ",intTelephoneNo = ?,dteEndDate=?,strNatureOfBusinnes=?"
				+ ",strMultipleBillPrinting=?,strEnableKOT=?,strEffectOnPSP=?"
				+ ",strPrintServiceTaxNo=?,strServiceTaxNo=?,strPrintVatNo=?"
				+ ",strVatNo=?,strShowBill=?,strManualBillNo=?"
				+ ",strMenuItemDispSeq=?,strSenderEmailId=?,strEmailPassword=?"
				+ ",strConfirmEmailPassword=?,strBody=?,strEmailServerName=?"
				+ ",strUserEdited=?,dteDateEdited=?,strPOSType=?"
				+ ",strWebServiceLink=?,strDataSendFrequency=?,strRFID=?"
				+ ",strServerName=?,strDBUserName=?,strDBPassword=?,strDatabaseName=?"
				+ ",strEnableKOTForDirectBiller=?,intPinCode=?,strSMSApi=?"
				+ ",strChangeTheme=?,dblMaxDiscount=?,strAreaWisePricing=?"
				+ ",strMenuItemSortingOn=?,strDirectAreaCode=?,intColumnSize=?"
				+ ",strPrintType=?,strEditHomeDelivery=?,strSlabBasedHDCharges=?"
				+ ",strSkipWaiter=?,strSkipPax=?,strDirectKOTPrintMakeKOT=?"
				+ ",strCRMInterface=?,strGetWebserviceURL=?,strPostWebserviceURL=?"
				+ ",strOutletUID=?,strPOSID=?,strStockInOption=?"
				+ ",longCustSeries=?,intAdvReceiptPrintCount=?,strHomeDeliverySMS=?"
				+ ",strBillStettlementSMS=?,strBillFormatType=?"
				+ ",strActivePromotions=?,strSendHomeDelSMS=?,strSendBillSettlementSMS=?"
				+ ",strSMSType=?,strPrintShortNameOnKOT=?,strShowCustHelp=?"
				+ ",strPrintOnVoidBill=?,strPostSalesDataToMMS=?"
				+ ",strCustAreaMasterCompulsory=?,strPriceFrom=?"
				+ ",strShowPrinterErrorMessage=?,strChangeQtyForExternalCode=?"
				+ ",strPointsOnBillPrint=?,strTouchScreenMode=?,strCardInterfaceType=?"
				+ ",strCMSIntegrationYN =?,strCMSWebServiceURL=?,strManualAdvOrderNoCompulsory=?"
				+ ",strPrintManualAdvOrderNoOnBill=?,strPrintModifierQtyOnKOT=?"
				+ ",strNoOfLinesInKOTPrint=?,strMultipleKOTPrintYN=?"
				+ ",strItemQtyNumpad=?,strTreatMemberAsTable=?,strKOTToLocalPrinter=? "
				+ ",strSettleBtnForDirectBillerBill=?,strDelBoySelCompulsoryOnDirectBiller=?"
				+ ",strCMSMemberForKOTJPOS=? ,strCMSMemberForKOTMPOS=? "
				+ ",blobReportImage=?,strDontShowAdvOrderInOtherPOS=?,strPrintZeroAmtModifierInBill=?"
				+ ",strPrintKOTYN=?,strCreditCardSlipNoCompulsoryYN=?,strCreditCardExpiryDateCompulsoryYN=?"
				+ ",strSelectWaiterFromCardSwipe=?,strMultiWaiterSelectionOnMakeKOT=?"
				+ ",strMoveTableToOtherPOS=?,strMoveKOTToOtherPOS=?"
				+ ",strCalculateTaxOnMakeKOT=?,strReceiverEmailId=?"
				+ ",strCalculateDiscItemWise=?, strTakewayCustomerSelection=?,StrShowItemStkColumnInDB=?"
				+ ",strItemType=? "
				+ ",strAllowNewAreaMasterFromCustMaster=?,strCustAddressSelectionForBill=?,strGenrateMI=?"
				+ ",strFTPAddress=?, strFTPServerUserName=?,strFTPServerPass=?  "
				+ ",strAllowToCalculateItemWeight=? ,strShowBillsDtlType=?,strPrintTaxInvoiceOnBill=?"
				+ ",strPrintInclusiveOfAllTaxesOnBill=?,strApplyDiscountOn=?,strMemberCodeForKotInMposByCardSwipe=?"
				+ ",strPrintBillYN=?,strVatAndServiceTaxFromPos=?,strMemberCodeForMakeBillInMPOS=?"
				+ ",dteHOServerDate=?,strItemWiseKOTYN=?,strLastPOSForDayEnd=?,strCMSPostingType=?"
				+ ",strPopUpToApplyPromotionsOnBill=?,strSelectCustomerCodeFromCardSwipe=?"
				+ ",strCheckDebitCardBalOnTransactions=?,strSettlementsFromPOSMaster=? "
				+ ",strShiftWiseDayEndYN=?,strProductionLinkup=?,strLockDataOnShift=?,strWSClientCode=?"
				+ ",strEnableBillSeries=?,strEnablePMSIntegrationYN=?,strPrintTimeOnBill=?"
				+ ",strPrintTDHItemsInBill=?,strPrintRemarkAndReasonForReprint=?,intDaysBeforeOrderToCancel=?"
				+ ",intNoOfDelDaysForAdvOrder=?,intNoOfDelDaysForUrgentOrder=?,strSetUpToTimeForAdvOrder=?"
				+ ",strSetUpToTimeForUrgentOrder=?,strUpToTimeForAdvOrder=?,strUpToTimeForUrgentOrder=?"
				+ ",strEnableBothPrintAndSettleBtnForDB=?,strInrestoPOSIntegrationYN=?,strInrestoPOSWebServiceURL=?"
				+ ",strInrestoPOSId=?,strInrestoPOSKey=?,strCarryForwardFloatAmtToNextDay=?"
				+ ",strOpenCashDrawerAfterBillPrintYN=?,strPropertyWiseSalesOrderYN=?,strDataPostFlag=?,strShowItemDetailsGrid=? "
				+ ",strShowPopUpForNextItemQuantity=?,strJioMoneyIntegration=?,strJioWebServiceUrl=?,strJioMID=?,strJioTID=?,strJioActivationCode=?,strJioDeviceID=?"
				+ ",strNewBillSeriesForNewDay=?,strShowReportsPOSWise=?,strEnableDineIn=?"
				+ ",strAutoAreaSelectionInMakeKOT=?,strConsolidatedKOTPrinterPort=? "
				+ ",dblRoundOff=?,strShowUnSettlementForm=?,strPrintOpenItemsOnBill=?,strPrintHomeDeliveryYN=?,strScanQRYN=?"
				+ ",strAreaWisePromotions=?,strPrintItemsOnMoveKOTMoveTable=?,strShowPurRateInDirectBiller=?,strEnableTableReservationForCustomer=? "
				+ ",strAutoShowPopItems=?,intShowPopItemsOfDays=?,strPostSalesCostOrLoc=?,strEffectOfSales=?,strPOSWiseItemToMMSProductLinkUpYN=?"
				+ ",strEnableMasterDiscount=?,strEnableNFCInterface=?  "
				+ ",strBenowIntegrationYN=?,strXEmail=?,strMerchantCode=?,strAuthenticationKey=?,strSalt=?,strEnableLockTable=? "
				+ ",strHomeDeliveryAreaForDirectBiller=?,strTakeAwayAreaForDirectBiller=?,strRoundOffBillFinalAmt=?,dblNoOfDecimalPlace=? "
				+ ",strSendDBBackupOnClientMail=?,strPrintOrderNoOnBillYN=?,strPrintDeviceAndUserDtlOnKOTYN=?,strRemoveSCTaxCode=? "
				+ ",strAutoAddKOTToBill=?,strAreaWiseCostCenterKOTPrintingYN=?"
				+ ",strWERAOnlineOrderIntegration=?,strWERAMerchantOutletId=?,strWERAAuthenticationAPIKey=? "
				+ ",strFireCommunication=?,dblUSDConverionRate=?,strDBBackupMailReceiver=?,strPrintMoveTableMoveKOTYN=?,strPrintQtyTotal=? "
				+ "WHERE strClientCode =? and strPOSCode=? ;";

			PreparedStatement objPstmt = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlUpdate);
			objPstmt.setString(1, txtClientName.getText().trim());
			objPstmt.setString(2, txtShopAddressLine1.getText().trim());
			objPstmt.setString(3, txtShopAddressLine2.getText().trim());
			objPstmt.setString(4, txtShopAddressLine3.getText().trim());
			objPstmt.setString(5, txtEmailAddress.getText().trim());
			objPstmt.setString(6, txtBillFooter.getText().trim());
			objPstmt.setBoolean(7, longFooter);
			objPstmt.setString(8, cmbBillPaperSize.getSelectedItem().toString());
			objPstmt.setString(9, negBilling);
			objPstmt.setString(10, dayEnd);

			objPstmt.setString(11, cmbPrintMode.getSelectedItem().toString());
			objPstmt.setString(12, discount);
			objPstmt.setString(13, cmbCity.getSelectedItem().toString());
			objPstmt.setString(14, cmbState.getSelectedItem().toString());
			objPstmt.setString(15, cmbCountry.getSelectedItem().toString());
			objPstmt.setString(16, txtTelephone.getText());
			objPstmt.setString(17, dteEndDate);
			objPstmt.setString(18, cmbNatureOfBusiness.getSelectedItem().toString());
			objPstmt.setString(19, multiBillPrint);
			objPstmt.setString(20, kot);

			objPstmt.setString(21, effectOnPSP);
			objPstmt.setString(22, printServiceTaxno);
			objPstmt.setString(23, txtServiceTaxno.getText());
			objPstmt.setString(24, printVatNo);
			objPstmt.setString(25, txtVatNo.getText());
			objPstmt.setString(26, showBill);
			objPstmt.setString(27, ManualBillNo);
			objPstmt.setString(28, cmbMenuItemDisSeq.getSelectedItem().toString());
			objPstmt.setString(29, txtSenderEmailId.getText());
			objPstmt.setString(30, txtEmailPassword.getText());

			objPstmt.setString(31, txtConfirmEmailPassword.getText());
			objPstmt.setString(32, txtBodyPart.getText());
			objPstmt.setString(33, ServerHostName);
			objPstmt.setString(34, userCode);
			objPstmt.setString(35, dteEdited);
			objPstmt.setString(36, strPOSType.getSelectedItem().toString());
			objPstmt.setString(37, txtWebServiceLink.getText());
			objPstmt.setString(38, cmbDataSendFrequency.getSelectedItem().toString());
			objPstmt.setString(39, rfidInterface);
			objPstmt.setString(40, txtServerName.getText().trim());

			objPstmt.setString(41, txtUserName.getText().trim());
			objPstmt.setString(42, txtPassword.getText().trim());
			objPstmt.setString(43, txtDatabaseName.getText().trim());
			objPstmt.setString(44, KOTPrintingForDB);
			objPstmt.setString(45, pincode);
			objPstmt.setString(46, smsAPI);
			objPstmt.setString(47, theme);
			objPstmt.setDouble(48, maxDiscount);
			objPstmt.setString(49, areaWisePricing);
			objPstmt.setString(50, menuItemSortingOn);

			objPstmt.setString(51, dineInAreaForDirectBiller);
			objPstmt.setInt(52, ColumnSize);
			objPstmt.setString(53, printType);
			objPstmt.setString(54, editHomeDelivery);
			objPstmt.setString(55, slabBasedHomeDelCharges);
			objPstmt.setString(56, skipWaiterSelection);
			objPstmt.setString(57, strSkipPaxSelection);
			objPstmt.setString(58, strDirectKOTPrintMakeKOT);
			objPstmt.setString(59, crmInterface);
			objPstmt.setString(60, getWebServiceURL);

			objPstmt.setString(61, postWebServiceURL);
			objPstmt.setString(62, outletUID);
			objPstmt.setString(63, posID);
			objPstmt.setString(64, stockInOption);
			objPstmt.setString(65, txtCustSeries.getText().trim());
			objPstmt.setInt(66, advRePrintCount);
			objPstmt.setString(67, txtAreaSendHomeDeliverySMS.getText().trim());
			objPstmt.setString(68, txtAreaBillSettlementSMS.getText().trim());
			objPstmt.setString(69, billFormatType);
			objPstmt.setString(70, activePromotions);

			objPstmt.setString(71, sendHomeDel);
			objPstmt.setString(72, sendBillSettlement);
			objPstmt.setString(73, smsType);
			objPstmt.setString(74, printShortNameOnKOT);
			objPstmt.setString(75, showCustHelpOnTrans);
			objPstmt.setString(76, printOnVoidBill);
			objPstmt.setString(77, postSalesDataToMMS);
			objPstmt.setString(78, custAreaMasterCompulsory);
			objPstmt.setString(79, priceFrom);
			objPstmt.setString(80, printerErrorMessage);

			objPstmt.setString(81, ChangeQtyForExternalCode);
			objPstmt.setString(82, PointsOnBillPrint);
			objPstmt.setString(83, touchScreenMode);
			objPstmt.setString(84, cardInterfaceType);
			objPstmt.setString(85, strCMSIntegartionYN);
			objPstmt.setString(86, txtCMSWesServiceURL.getText().trim());
			objPstmt.setString(87, manualAdvOrderNo);
			objPstmt.setString(88, printManualAdvOrderNoOnBill);
			objPstmt.setString(89, printModQtyOnKOT);
			objPstmt.setString(90, txtNoOfLinesInKOTPrint.getText().trim());

			objPstmt.setString(91, multiKOTPrint);
			objPstmt.setString(92, ItemQtyNumpad);
			objPstmt.setString(93, memberAsTable);
			objPstmt.setString(94, KOTToLocalPrinter);
			objPstmt.setString(95, settleBtnForDirectBillerBill);
			objPstmt.setString(96, delBoySelCompulsoryOnDirectBiller);
			objPstmt.setString(97, memberCodeForKOTJPOS);
			objPstmt.setString(98, memberCodeForKOTMPOS);

			/* if (!reportImagePath.isEmpty())
                        {
                            reportImgInputStream = new FileInputStream(reportImagePath);
                            objPstmt.setBlob(99, reportImgInputStream);

                            funCreateReportImagesFolder();
                            String userDirectory = System.getProperty("user.dir");
                            File fileReportImage = new File(userDirectory + "\\ReportImage\\imgClientImage.jpg");
                            if (fileReportImage.exists())
                            {
                                fileReportImage.delete();
                            }
                            Files.copy(reportImageFile.toPath(), fileReportImage.toPath());
                        }
                        else
                        {
                            objPstmt.setBlob(99, reportImgInputStream);
                        }
			 */
			objPstmt.setString(99, "");
			objPstmt.setString(100, dontShowAdvOrderOfOtherPOS);

			objPstmt.setString(101, printZeroAmtModifierOnBill);
			objPstmt.setString(102, printKOTYN);
			objPstmt.setString(103, slipNoCompulsoryYN);
			objPstmt.setString(104, expDateCompulsoryYN);
			objPstmt.setString(105, selectWaiterFromCardSwipe);
			objPstmt.setString(106, multiWaiterSelOnMakeKOT);
			objPstmt.setString(107, moveTableToOtherPOS);
			objPstmt.setString(108, moveKOTToOtherPOS);
			objPstmt.setString(109, calculateTaxOnMakeKOT);
			objPstmt.setString(110, txtReceiverEmailId.getText().trim());

			objPstmt.setString(111, calculateDiscItemWise);
			objPstmt.setString(112, takewayCustomerSelection);
			objPstmt.setString(113, showItemStkColumnInDB);
			objPstmt.setString(114, itemType);
			objPstmt.setString(115, allowNewAreaMasterFromCustMaster);
			objPstmt.setString(116, custAddressSelectionForBill);
			objPstmt.setString(117, genrateMI);
			objPstmt.setString(118, txtFTPAddress.getText().trim());

			objPstmt.setString(119, txtFTPServerUserName.getText().trim());
			objPstmt.setString(120, txtFTPServerPass.getText().trim());
			objPstmt.setString(121, allowToCalculateItemWeight);
			objPstmt.setString(122, showBillsDtlType);
			objPstmt.setString(123, printTaxInvoiceOnBill);
			objPstmt.setString(124, printInclusiveOfAllTxesOnBill);
			objPstmt.setString(125, applyDiscountOn);
			objPstmt.setString(126, memberCodeForKotInMposByCardSwipe);
			objPstmt.setString(127, checkPrintBill);
			objPstmt.setString(128, useVatAndServiceNoFromPos);
			objPstmt.setString(129, memberCodeForMakeBillInMPOS);
			objPstmt.setString(130, HOServerDate);
			objPstmt.setString(131, itemWiseKOTYN);
			objPstmt.setString(132, posCode);
			objPstmt.setString(133, CMSPostingType);
			objPstmt.setString(134, popUpToApplyPromotionsOnBill);
			objPstmt.setString(135, selectCustomerCodeByCardSwipe);
			objPstmt.setString(136, checkDebitCardBalOnTrans);
			objPstmt.setString(137, pickSettlementsFromPOSMaster);
			objPstmt.setString(138, shiftWiseDayEnd);
			objPstmt.setString(139, productionLinkup);
			objPstmt.setString(140, lockDataOnShift);
			objPstmt.setString(141, wsClientCode);
			//objPstmt.setString(142, dteCreated);
			objPstmt.setString(142, enableBillSeries);
			objPstmt.setString(143, enablePMSIntegrationYN);
			objPstmt.setString(144, printTimeOnBillYN);
			objPstmt.setString(145, printTDHItemInBillYN);
			objPstmt.setString(146, flgPrintRemarkAndReason ? "Y" : "N");
			objPstmt.setInt(147, daysBeforeOrderToCancel);
			objPstmt.setInt(148, Integer.parseInt(txtNoOfDelDaysForAdvOrder.getText()));
			objPstmt.setInt(149, Integer.parseInt(txtNoOfDelDaysForUrgentOrder.getText()));
			objPstmt.setString(150, setUpToTimeForAdvOrder);
			objPstmt.setString(151, setUpToTimeForUrgentOrder);
			objPstmt.setString(152, upToTimeForAdvOrder);
			objPstmt.setString(153, upToTimeForUrgentOrder);
			objPstmt.setString(154, enableBothPrintAndSettleBtnForDB);
			objPstmt.setString(155, strInrestoPOSIntegartionYN);
			objPstmt.setString(156, txtInrestoPOSWesServiceURL.getText().trim());
			objPstmt.setString(157, txtInrestoPOSId.getText().trim());
			objPstmt.setString(158, txtInrestoPOSKey.getText().trim());
			objPstmt.setString(159, carryForwardFloatAmtToNextDay);
			objPstmt.setString(160, openCashDrawerAfterBillPrintYN);
			objPstmt.setString(161, propertyWiseSalesOrderYN);
			objPstmt.setString(162, "N");
			objPstmt.setString(163, showItemDtlsForChangeCustomerOnBillYN);
			objPstmt.setString(164, showPopUpForNextItemQuantityYN);
			objPstmt.setString(165, strJioPOSIntegartionYN);
			objPstmt.setString(166, txtJioPOSWesServiceURL.getText());
			objPstmt.setString(167, txtJioMoneyMID.getText());
			objPstmt.setString(168, txtJioMoneyTID.getText());
			objPstmt.setString(169, txtJioActivationCode.getText());
			objPstmt.setString(170, txtJioDeviceID.getText());
			objPstmt.setString(171, newBillSeriesForNewDay);
			objPstmt.setString(172, showPOSWiseReports);
			objPstmt.setString(173, enableDineIn);
			objPstmt.setString(174, autoAreaSelectionInMakeKOT);
			objPstmt.setString(175, consolidatedKOTPrinterPort);
			objPstmt.setString(176, roundOffTo);
			objPstmt.setString(177, showUnSettlementForm);
			objPstmt.setString(178, printOpenItemsOnBill);
			objPstmt.setString(179, printHomeDeliveryYN);
			objPstmt.setString(180, scanQRYN);
			objPstmt.setString(181, areaWisePromotions);
			objPstmt.setString(182, printItemOnMoveKOTMoveTable);
			objPstmt.setString(183, showPurRateInDirectBiller);
			objPstmt.setString(184, tableReservationForCustomer);
			objPstmt.setString(185, autoShowPopItems);//show Pop items Y/N
			objPstmt.setInt(186, intPOPItemsOfDays);//how many of days to show pop items
			objPstmt.setString(187, postMMSDataCostCenterWiseOrLocWise);//Post MMS Data Effect cost centerLoc Wise
			objPstmt.setString(188, effectOfSales);
			objPstmt.setString(189, posWiseItemLinkedUpToMMSProduct);
			objPstmt.setString(190, enableMasterDiscount);
			objPstmt.setString(191, enableNFCInterface);
			objPstmt.setString(192, strBenowIntegrationYN);
			objPstmt.setString(193, txtXEmail.getText());
			objPstmt.setString(194, txtMerchantCode.getText());
			objPstmt.setString(195, txtAuthenticationKey.getText());
			objPstmt.setString(196, txtSalt.getText());
			objPstmt.setString(197, enableLockTables);
			objPstmt.setString(198, homeDeliveryAreaForDirectBiller);
			objPstmt.setString(199, takeAwayAreaForDirectBiller);
			objPstmt.setString(200, roundOffFinalBillAmount);
			objPstmt.setDouble(201, dblNoOfDecimalPlace);
			objPstmt.setString(202, sendDBBackupOnClientMail);
			objPstmt.setString(203, printOrderNoOnBillYN);
			objPstmt.setString(204, printDeviceAndUserDtlOnKOTYN);
			objPstmt.setString(205, removeSCTaxCode);
			objPstmt.setString(206, autoAddKOTToBill);
			objPstmt.setString(207, areaWiseCostCenterKOTPrinting);
			objPstmt.setString(208, weraOnlineOrderIntegrationYN);
			objPstmt.setString(209, weraMerchantOutletId);
			objPstmt.setString(210, weraAuthenticationAPIKey);
			objPstmt.setString(211, fireCommunication);
			objPstmt.setString(212, usdCurrencyConvertionRate);
			objPstmt.setString(213, dbBackupMailReceiverMailIds);
			objPstmt.setString(214, printMoveTableMoveKOTYN);
			objPstmt.setString(215, printQtyTotal);

			objPstmt.setString(216, txtClientCode.getText());
			objPstmt.setString(217, newPropertyPOSCode);

			int affected = objPstmt.executeUpdate();

			if (affected > 0)
			{
			    clsSettelementOptions objSettlmentOptions = new clsSettelementOptions();
			    objSettlmentOptions.funAddSettelementOptions();
			    objSettlmentOptions = null;
			    new frmOkPopUp(this, "Updated Successfully", "Successfull", 3).setVisible(true);
			    funFillData();
			}
		    }
		}
	    }

	    new clsGlobalVarClass(clsGlobalVarClass.gPOSCode);

	    if (gHOPOSType.equals("Client POS"))
	    {
		funPostPropertySetupDataToHO();
		funPostBillSeriesDataHO();
	    }
	    // new clsGlobalVarClass(clsGlobalVarClass.gPOSCode);

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private boolean funPostPropertySetupDataToHO()
    {
	boolean flgResult = false;
	StringBuilder sql = new StringBuilder();

	try
	{
	    JSONObject rootObject = new JSONObject();
	    JSONArray dataObjectArray = new JSONArray();
	    boolean flgAllPOS = false;

	    sql.append("select strClientCode from tblsetup where strPOSCode='All' ");
	    ResultSet rsSetupData = dbMysql.executeResultSet(sql.toString());
	    if (rsSetupData.next())
	    {
		flgAllPOS = true;
	    }
	    rsSetupData.close();

	    sql.setLength(0);
	    sql.append("select * from tblsetup where strDataPostFlag='N'");
	    rsSetupData = dbMysql.executeResultSet(sql.toString());
	    ResultSetMetaData rsMetaData = rsSetupData.getMetaData();
	    while (rsSetupData.next())
	    {
		if (!rsSetupData.getString(147).trim().isEmpty())
		{
		    if (flgAllPOS)
		    {
			sql.setLength(0);
			sql.append("select strPOSCode from tblposmaster ");
			ResultSet rsPOSMaster = clsGlobalVarClass.dbMysql.executeResultSet(sql.toString());
			while (rsPOSMaster.next())
			{
			    JSONObject dataObject = new JSONObject();
			    for (int cnt = 1; cnt <= rsMetaData.getColumnCount(); cnt++)
			    {
				if (rsMetaData.getColumnName(cnt).equalsIgnoreCase("strClientCode"))
				{
				    dataObject.put(rsMetaData.getColumnName(cnt), rsSetupData.getString(1));
				}

				if (rsMetaData.getColumnName(cnt).equalsIgnoreCase("strPOSCode"))
				{
				    dataObject.put(rsMetaData.getColumnName(cnt), rsPOSMaster.getString(1));
				}
				else
				{
				    dataObject.put(rsMetaData.getColumnName(cnt), rsSetupData.getString(rsMetaData.getColumnName(cnt)));
				}
			    }
			    dataObjectArray.add(dataObject);
			}
			rsPOSMaster.close();
		    }
		    else
		    {
			JSONObject dataObject = new JSONObject();
			for (int cnt = 1; cnt <= rsMetaData.getColumnCount(); cnt++)
			{
			    if (rsMetaData.getColumnName(cnt).equalsIgnoreCase("strClientCode"))
			    {
				dataObject.put(rsMetaData.getColumnName(cnt), rsSetupData.getString(1));
			    }
			    else
			    {
				dataObject.put(rsMetaData.getColumnName(cnt), rsSetupData.getString(rsMetaData.getColumnName(cnt)));
			    }
			}
			dataObjectArray.add(dataObject);
		    }
		}
	    }
	    rsSetupData.close();

	    rootObject.put("tblsetup", dataObjectArray);
	    String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostPropertySetup";

	    URL url = new URL(hoURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    OutputStream os = conn.getOutputStream();
	    os.write(rootObject.toString().getBytes());
	    os.flush();
	    if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED)
	    {
		throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
	    }
	    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	    String output = "", op = "";

	    while ((output = br.readLine()) != null)
	    {
		op += output;
	    }
	    System.out.println("Prop Setup flg=" + op);
	    conn.disconnect();
	    flgResult = Boolean.parseBoolean(op);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    sql = null;
	    return flgResult;
	}
    }

    private boolean funPostBillSeriesDataHO()
    {
	boolean flgResult = false;
	StringBuilder sql = new StringBuilder();
	ResultSet rsBillSeries = null;

	try
	{
	    JSONObject rootObject = new JSONObject();
	    JSONArray dataObjectArray = new JSONArray();

	    sql.append("select * from tblbillseries where strDataPostFlag='N'");
	    rsBillSeries = dbMysql.executeResultSet(sql.toString());
	    while (rsBillSeries.next())
	    {
		JSONObject dataObject = new JSONObject();
		dataObject.put("POSCode", rsBillSeries.getString("strPOSCode"));
		dataObject.put("Type", rsBillSeries.getString("strType"));
		dataObject.put("BillSeries", rsBillSeries.getString("strBillSeries"));
		dataObject.put("LastNo", rsBillSeries.getString("intLastNo"));
		dataObject.put("Codes", rsBillSeries.getString("strCodes"));
		dataObject.put("Names", rsBillSeries.getString("strNames"));
		dataObject.put("UserCreated", rsBillSeries.getString("strUserCreated"));
		dataObject.put("UserEdited", rsBillSeries.getString("strUserEdited"));
		dataObject.put("DateCreated", rsBillSeries.getString("dteCreatedDate"));
		dataObject.put("DateEdited", rsBillSeries.getString("dteEditedDate"));
		dataObject.put("DataPostFlag", rsBillSeries.getString("strDataPostFlag"));
		dataObject.put("ClientCode", rsBillSeries.getString("strClientCode"));
		dataObject.put("PropertyCode", rsBillSeries.getString("strPropertyCode"));
		dataObject.put("PrintGTOfOtherBills", rsBillSeries.getString("strPrintGTOfOtherBills"));
		dataObject.put("PrintIncOfTaxOnBill", rsBillSeries.getString("strPrintInclusiveOfTaxOnBill"));

		dataObjectArray.add(dataObject);
	    }
	    rsBillSeries.close();

	    rootObject.put("tblbillseries", dataObjectArray);
	    String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostPropertySetup";

	    URL url = new URL(hoURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    OutputStream os = conn.getOutputStream();
	    os.write(rootObject.toString().getBytes());
	    os.flush();
	    if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED)
	    {
		throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
	    }
	    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	    String output = "", op = "";

	    while ((output = br.readLine()) != null)
	    {
		op += output;
	    }
	    System.out.println("CustData flg=" + op);
	    conn.disconnect();
	    flgResult = Boolean.parseBoolean(op);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    sql = null;
	    return flgResult;
	}
    }

    private void funGetThePrinterHelp(int row, int col)
    {
	new frmSearchFormDialog(this, true, arrListPrinters, "Printers").setVisible(true);
	if (clsGlobalVarClass.gSearchItemClicked)
	{
	    Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
	    clsGlobalVarClass.gSearchItemClicked = false;
	    System.out.println(clsGlobalVarClass.gSearchedItem);
	    tblPrinterSetup.setValueAt(data[0], row, col);

	    List<clsPrinterSetup> arrListPrinterSetup = new ArrayList<clsPrinterSetup>();
	    for (int cnt = 0; cnt < tblPrinterSetup.getRowCount(); cnt++)
	    {
		clsPrinterSetup objPrinterSetup = new clsPrinterSetup();
		objPrinterSetup.setCostCenterName(tblPrinterSetup.getValueAt(cnt, 0).toString());
		objPrinterSetup.setPrimaryPrinter(tblPrinterSetup.getValueAt(cnt, 1).toString());
		objPrinterSetup.setSecondaryPrinter(tblPrinterSetup.getValueAt(cnt, 2).toString());
		objPrinterSetup.setPrintOnBothPrinters(Boolean.parseBoolean(tblPrinterSetup.getValueAt(cnt, 3).toString()));
		arrListPrinterSetup.add(objPrinterSetup);
	    }
	    funFillPrinterSetupTable(arrListPrinterSetup);
	    arrListPrinterSetup = null;
	}
    }

    private void funSetDataPOSWise()
    {
	try
	{
	    String posCodeName = cmbPOS.getSelectedItem().toString();
	    int lastIndex = posCodeName.lastIndexOf(" ");
	    newPropertyPOSCode = posCodeName.substring(lastIndex + 1, posCodeName.length());
	    // String posName=posCodeName.substring(0, lastIndex + 1);
	    String posName = posCodeName.split("                                         ")[0];

	    mapCodeWithName = new HashMap<String, String>();
	    mapNameWithCode = new HashMap<String, String>();
	    mapSelectedCodeWithName = new HashMap<String, String>();
	    mapBillSeriesCodeList = new HashMap<>();
	    mapBillSeriesNameList = new HashMap<>();

	    try
	    {
		if (newPropertyPOSCode.equalsIgnoreCase("All"))
		{
		    String filePath = System.getProperty("user.dir");
		    reportImageFile = new File(filePath + "/ReportImage/imgClientImage.jpg");

		}
		else
		{
		    String fileName = "/ReportImage/" + "img" + newPropertyPOSCode + ".jpg";
		    String filePath = System.getProperty("user.dir");
		    reportImageFile = new File(filePath + fileName);

		}

		if (reportImageFile.isFile())
		{
		    reportImgInputStream = new FileInputStream(reportImageFile);
		    funSetImage();
		}
		else
		{
		    sql = "select blobReportImage from tblpropertyimage where strPOSCode='" + newPropertyPOSCode + "' and strClientCode='" + clsGlobalVarClass.gClientCode + "' ";
		    ResultSet rsPOSImage = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		    if (rsPOSImage.next())
		    {
			Blob blob = rsPOSImage.getBlob(1);
			//InputStream inImg = blob.getBinaryStream();

			if (blob.length() > 0)
			{
			    InputStream inImg = blob.getBinaryStream(1, blob.length());
			    byte[] imageBytes = blob.getBytes(1, (int) blob.length());
			    //BufferedImage image = ImageIO.read(inImg);
			    OutputStream outImg = new FileOutputStream(reportImageFile);
			    int c = 0;
			    while ((c = inImg.read()) > -1)
			    {
				outImg.write(c);
			    }
			    outImg.close();
			    inImg.close();
			    reportImgInputStream = new FileInputStream(reportImageFile);

			    if (reportImageFile.exists())
			    {
				reportImagePath = reportImageFile.getAbsolutePath();
				imageIcon = new ImageIcon(reportImageFile.getAbsolutePath());
				lblReportImageIcon.setIcon(imageIcon);
			    }
			    else
			    {
				lblReportImageIcon.setText("           Report Image");
				lblReportImageIcon.setIcon(null);
				reportImagePath = "";
			    }
			}
			else
			{
			    lblReportImageIcon.setText("           Report Image");
			    lblReportImageIcon.setIcon(null);
			    reportImagePath = "";
			}
		    }
		    else
		    {
			lblReportImageIcon.setText("           Report Image");
			lblReportImageIcon.setIcon(null);
			reportImagePath = "";
		    }
		    rsPOSImage.close();

		}
	    }
	    catch (Exception e)
	    {
		e.printStackTrace();
	    }
	    funFillArea();
	    funFillData();
	    funSetShortCutKeys();

	    funFillPrinters();

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public void funLikePOSDetails()
    {
	try
	{

	    funCallForLikeSearchForm("LikePOSMaster");
	    new frmSearchFormDialog(this, true).setVisible(true);
	    List<clsOperatorDtl> arrUserList = new ArrayList<clsOperatorDtl>();
	    clsOperatorDtl objUser = null;
	    if (clsGlobalVarClass.gSearchItemClicked)
	    {
		Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
		clsGlobalVarClass.gSearchItemClicked = false;
		selectedLikePOSCode = data[0].toString();
		funSetDataPOSWise();
		clsGlobalVarClass.gSearchItemClicked = false;

	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public Vector funCallForLikeSearchForm(String searchFormName)
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
		case "LikePOSMaster":
		    clsGlobalVarClass.gSearchMasterFormName = "POS Master";
		    gQueryForSearch = "select a.strPosCode as POS_Code,a.strPosName as POS_Name ,a.strPOSType "
			    + " from tblposmaster a,tblsetup b "
			    + " where a.strPosCode=b.strPOSCode ";
		    vArrSearchColumnSize.add(30);
		    vArrSearchColumnSize.add(240);

		    break;

	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return vArrSearchColumnSize;

    }

    class clsPrinterSetup
    {

	private String costCenterName;

	private String primaryPrinter;

	private String secondaryPrinter;

	private boolean printOnBothPrinters;

	public String getCostCenterName()
	{
	    return costCenterName;
	}

	public void setCostCenterName(String costCenterName)
	{
	    this.costCenterName = costCenterName;
	}

	public String getPrimaryPrinter()
	{
	    return primaryPrinter;
	}

	public void setPrimaryPrinter(String primaryPrinter)
	{
	    this.primaryPrinter = primaryPrinter;
	}

	public String getSecondaryPrinter()
	{
	    return secondaryPrinter;
	}

	public void setSecondaryPrinter(String secondaryPrinter)
	{
	    this.secondaryPrinter = secondaryPrinter;
	}

	public boolean isPrintOnBothPrinters()
	{
	    return printOnBothPrinters;
	}

	public void setPrintOnBothPrinters(boolean printOnBothPrinters)
	{
	    this.printOnBothPrinters = printOnBothPrinters;
	}
    }

    public void funSaveMapMyDevice()
    {
	clsUtility obj = new clsUtility();
	obj.funStartSocketBat();
	try
	{
	    String mid = clsGlobalVarClass.gJioMoneyMID;
	    String tid = clsGlobalVarClass.gJioMoneyTID;
	    String RequestType = "1008";
	    String Amount = "0.00";
	    String deviceId = txtJioDeviceID.getText();
	    String manufacturer = "JioPayDevice";//JioPayDevice
	    String deviceStatus = "A";
	    String linkDate = getCurrentDate();
	    String deLinkDate = deLinkedDate();
	    String superMerchantId = mid;
	    String userName = "9820001759";
	    String businessLegalName = "Sanguine Software";

	    String requestData = "requestType=" + RequestType
		    + "&mid=" + mid
		    + "&deviceId=" + deviceId
		    + "&manufacturer=" + manufacturer
		    + "&deviceStatus=" + deviceStatus
		    + "&linkDate=" + linkDate
		    + "&deLinkDate=" + deLinkDate
		    + "&superMerchantId=" + superMerchantId
		    + "&userName=" + userName
		    + "&businessLegalName=" + businessLegalName
		    + "&tid=" + tid;

	    System.out.println("RequestData : " + requestData);
	    String Response = "";
	    Response = obj.funMakeTransaction(requestData, RequestType, mid, tid, Amount, "PRE_PROD", "localhost", "5150");
	    //System.out.println("Server Response: " + response);
	    System.out.println(Response);

	    String strRes = Response.trim();
	    JSONParser jsonParser = new JSONParser();
	    JSONObject jsonObject = (JSONObject) jsonParser.parse(strRes);
	    // String responseCode = (String) jsonObject.get("responseCode");
	    JSONArray lang = (JSONArray) jsonObject.get("result");
	    JSONParser jsonParser1 = new JSONParser();
	    JSONObject jsonObject1 = (JSONObject) jsonParser1.parse(lang.get(0).toString());
	    // String responseCode = (String) jsonObject.get("responseCode");
	    String responseCode = (String) jsonObject1.get("messageCode");

	    //String responseCode = lang.get(8).toString();
	    if (null != responseCode)
	    {
		JOptionPane.showMessageDialog(this, jsonObject1.get("message"));
	    }

	}
	catch (Exception e)
	{
	    System.out.println("Exception:" + e);
	}
    }

    public String funFetchDeviceID(String IP, String PORT)
    {
	clsUtility obj = new clsUtility();
	obj.funStartSocketBat();
	try
	{
	    String host = IP;	//IP address of the server
	    int port = Integer.parseInt(PORT);	//Port on which the socket is going to connect
	    String response = "";
	    StringBuilder Res = new StringBuilder();
	    String SendData = "getDongleId"; //getDongleId
	    System.out.println("Request String:" + SendData);
	    try (Socket s = new Socket(host, port)) //Creating socket class
	    {
		DataOutputStream dout = new DataOutputStream(s.getOutputStream());	//creating outputstream to send data to server
		DataInputStream din = new DataInputStream(s.getInputStream());	//creating inputstream to receive data from server
		//BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		byte[] str = SendData.getBytes("UTF-8");
		dout.write(str, 0, str.length);
		// System.out.println("Send data value = "+ SendData);
		dout.flush();	//Flush the streams

		byte[] bs = new byte[10024];
		din.read(bs);
		char c;
		for (byte b : bs)
		{
		    c = (char) b;
		    response = Res.append("").append(c).toString();
		}
		System.out.println("Device ID: " + response);
		dout.close();	//Closing the output stream
		din.close();	//Closing the input stream
	    } //creating outputstream to send data to server

	    return response.trim();
	}
	catch (Exception e)
	{
	    System.out.println("Exception:" + e);
	    return null;
	}
    }

    public String getCurrentDate()
    {
	Date currentDate = new Date();
	String strCurrentDate = (currentDate.getDate() + "/" + (currentDate.getMonth() + 1) + "/" + (currentDate.getYear() + 1900));
	return strCurrentDate;
    }

    public String deLinkedDate()
    {
	String currentDate = getCurrentDate();
	String[] date1 = currentDate.split("/");
	int year = 30 + Integer.parseInt(date1[2]);
	String nextDate = (date1[0] + "/" + date1[1] + "/" + String.valueOf(year));
	return nextDate;
    }


    private void btnExitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExitMouseClicked
	// TODO add your handling code here:
	dispose();
    }//GEN-LAST:event_btnExitMouseClicked

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
	// TODO add your handling code here:
	dispose();
	clsGlobalVarClass.hmActiveForms.remove("Property Setup");
    }//GEN-LAST:event_btnExitActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
	// TODO add your handling code here:
	funPropertySetupUpdate();
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnNewMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewMouseEntered
	// TODO add your handling code here:
    }//GEN-LAST:event_btnNewMouseEntered

    private void cmbPOSActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbPOSActionPerformed
    {//GEN-HEADEREND:event_cmbPOSActionPerformed
	if (flagCmbPOSActionPerformed)
	{
	    funSetPropertyPOSCode();
	}
    }//GEN-LAST:event_cmbPOSActionPerformed

    private void btnLikePOSMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLikePOSMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnLikePOSMouseClicked

    private void btnLikePOSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLikePOSActionPerformed
	// TODO add your handling code here:
	funLikePOSDetails();
    }//GEN-LAST:event_btnLikePOSActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
	// TODO add your handling code here:
	clsGlobalVarClass.hmActiveForms.remove("Property Setup");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
	// TODO add your handling code here:
	clsGlobalVarClass.hmActiveForms.remove("Property Setup");
    }//GEN-LAST:event_formWindowClosing

    private void tabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabbedPaneStateChanged

    }//GEN-LAST:event_tabbedPaneStateChanged

    private void txtInrestoPOSKeyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtInrestoPOSKeyActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtInrestoPOSKeyActionPerformed

    private void tblPrinterSetupMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPrinterSetupMouseReleased
	// TODO add your handling code here:
	System.out.println("Click Count=" + evt.getClickCount());
	if (evt.getClickCount() > 1)
	{
	    int row = tblPrinterSetup.getSelectedRow();
	    int col = tblPrinterSetup.getSelectedColumn();
	    if (col == 1 || col == 2)
	    {
		funGetThePrinterHelp(row, col);
	    }
	}
    }//GEN-LAST:event_tblPrinterSetupMouseReleased

    private void tblPrinterSetupMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPrinterSetupMouseClicked
	// TODO add your handling code here:
	//System.out.println("Click Count="+evt.getClickCount());
	//System.out.println("Row="+tblPrinterSetup.getSelectedRow()+"\tCol="+tblPrinterSetup.getSelectedColumn());
    }//GEN-LAST:event_tblPrinterSetupMouseClicked

    private void cmbCMSPostingTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCMSPostingTypeActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_cmbCMSPostingTypeActionPerformed

    private void cmbCMSPostingTypeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmbCMSPostingTypeMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_cmbCMSPostingTypeMouseClicked

    private void txtFTPAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFTPAddressActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtFTPAddressActionPerformed

    private void chkBillSettlementSMSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkBillSettlementSMSActionPerformed
	// TODO add your handling code here:
	funCheckBillSettlementFields();
    }//GEN-LAST:event_chkBillSettlementSMSActionPerformed

    private void chkHomeDelSMSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkHomeDelSMSActionPerformed
	// TODO add your handling code here:
	funCheckHomeDeliveryFields();
    }//GEN-LAST:event_chkHomeDelSMSActionPerformed

    private void btnShiftBillSettlementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShiftBillSettlementActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_btnShiftBillSettlementActionPerformed

    private void btnShiftBillSettlementMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnShiftBillSettlementMouseClicked
	// TODO add your handling code here:
	funShiftBtnFor_Bill_Settlement();
    }//GEN-LAST:event_btnShiftBillSettlementMouseClicked

    private void cmbBillSettlementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbBillSettlementActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_cmbBillSettlementActionPerformed

    private void btnShiftSendHomeDelieveryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnShiftSendHomeDelieveryMouseClicked
	// TODO add your handling code here:
	funShiftBtnFor_Send_Home_Delivery();
    }//GEN-LAST:event_btnShiftSendHomeDelieveryMouseClicked

    private void cmbSendHomeDeliveryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSendHomeDeliveryActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_cmbSendHomeDeliveryActionPerformed

    private void cmbCRMTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCRMTypeActionPerformed
	// TODO add your handling code here:
	funCheckCRMInterface();
    }//GEN-LAST:event_cmbCRMTypeActionPerformed

    private void cmbCardIntfTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCardIntfTypeActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_cmbCardIntfTypeActionPerformed

    private void txtDatabaseNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDatabaseNameKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    btnNew.requestFocus();
	}
    }//GEN-LAST:event_txtDatabaseNameKeyPressed

    private void txtDatabaseNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDatabaseNameMouseClicked
	// TODO add your handling code here:
	try
	{
	    if (txtDatabaseName.getText().length() == 0)
	    {
		new frmAlfaNumericKeyBoard(this, true, "1", "Enter Database Name").setVisible(true);
		txtDatabaseName.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	    else
	    {
		new frmAlfaNumericKeyBoard(this, true, txtDatabaseName.getText(), "1", "Enter Database Name").setVisible(true);
		txtDatabaseName.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtDatabaseNameMouseClicked

    private void txtPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPasswordKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    txtDatabaseName.requestFocus();
	}
    }//GEN-LAST:event_txtPasswordKeyPressed

    private void txtPasswordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPasswordMouseClicked
	// TODO add your handling code here:
	try
	{
	    if (txtPassword.getText().length() == 0)
	    {
		new frmAlfaNumericKeyBoard(this, true, "1", "Enter Password Name").setVisible(true);
		txtPassword.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	    else
	    {
		new frmAlfaNumericKeyBoard(this, true, txtPassword.getText(), "1", "Enter Password Name").setVisible(true);
		txtPassword.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtPasswordMouseClicked

    private void txtUserNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUserNameKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    txtPassword.requestFocus();
	}
    }//GEN-LAST:event_txtUserNameKeyPressed

    private void txtUserNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtUserNameMouseClicked
	// TODO add your handling code here:
	try
	{
	    if (txtUserName.getText().length() == 0)
	    {
		new frmAlfaNumericKeyBoard(this, true, "1", "Enter User Name").setVisible(true);
		txtUserName.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	    else
	    {
		new frmAlfaNumericKeyBoard(this, true, txtUserName.getText(), "1", "Enter User Name").setVisible(true);
		txtUserName.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtUserNameMouseClicked

    private void txtServerNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtServerNameKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    txtUserName.requestFocus();
	}
    }//GEN-LAST:event_txtServerNameKeyPressed

    private void txtServerNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtServerNameMouseClicked
	// TODO add your handling code here:
	try
	{
	    if (txtServerName.getText().length() == 0)
	    {
		new frmAlfaNumericKeyBoard(this, true, "1", "Enter Server Name").setVisible(true);
		txtServerName.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	    else
	    {
		new frmAlfaNumericKeyBoard(this, true, txtServerName.getText(), "1", "Enter Server Name").setVisible(true);
		txtServerName.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtServerNameMouseClicked

    private void cmbRFIDSetupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbRFIDSetupActionPerformed
	// TODO add your handling code here:
	if (cmbRFIDSetup.getSelectedItem().toString().equals("Yes"))
	{
	    funSetDatabaseCredentials(true);
	}
	else
	{
	    funSetDatabaseCredentials(false);
	}
    }//GEN-LAST:event_cmbRFIDSetupActionPerformed

    private void txtReceiverEmailIdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtReceiverEmailIdMouseClicked
	// TODO add your handling code here:
	if (txtReceiverEmailId.getText().length() == 0)
	{
	    new frmAlfaNumericKeyBoard(this, true, "1", "Enter EmailId").setVisible(true);
	    txtReceiverEmailId.setText(clsGlobalVarClass.gKeyboardValue);
	}
	else
	{
	    new frmAlfaNumericKeyBoard(this, true, txtReceiverEmailId.getText(), "1", "Enter EmailId").setVisible(true);
	    txtReceiverEmailId.setText(clsGlobalVarClass.gKeyboardValue);
	}
    }//GEN-LAST:event_txtReceiverEmailIdMouseClicked

    private void txtBodyPartMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtBodyPartMouseClicked
	// TODO add your handling code here:
	try
	{
	    if (txtBodyPart.getText().length() == 0)
	    {
		new frmAlfaNumericKeyBoard(this, true, "1", "Enter Message").setVisible(true);
		txtBodyPart.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	    else
	    {
		new frmAlfaNumericKeyBoard(this, true, txtBodyPart.getText(), "1", "Enter Message").setVisible(true);
		txtBodyPart.setText(clsGlobalVarClass.gKeyboardValue);
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtBodyPartMouseClicked

    private void txtConfirmEmailPasswordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtConfirmEmailPasswordMouseClicked
	// TODO add your handling code here:
	try
	{
	    if (txtConfirmEmailPassword.getText().length() == 0)
	    {
		new frmAlfaNumericKeyBoard(this, true, "2", "Enter Confirm Password").setVisible(true);
		txtConfirmEmailPassword.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	    else
	    {
		new frmAlfaNumericKeyBoard(this, true, txtConfirmEmailPassword.getText(), "2", "Enter Confirm Password").setVisible(true);
		txtConfirmEmailPassword.setText(clsGlobalVarClass.gKeyboardValue);
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtConfirmEmailPasswordMouseClicked

    private void txtEmailPasswordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtEmailPasswordMouseClicked
	// TODO add your handling code here:
	try
	{
	    if (txtEmailPassword.getText().length() == 0)
	    {
		new frmAlfaNumericKeyBoard(this, true, "2", "Enter Password").setVisible(true);
		txtEmailPassword.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	    else
	    {
		new frmAlfaNumericKeyBoard(this, true, txtEmailPassword.getText(), "2", "Enter Password").setVisible(true);
		txtEmailPassword.setText(clsGlobalVarClass.gKeyboardValue);
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtEmailPasswordMouseClicked

    private void txtSenderEmailIdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSenderEmailIdMouseClicked
	// TODO add your handling code here:
	try
	{
	    if (txtSenderEmailId.getText().length() == 0)
	    {
		new frmAlfaNumericKeyBoard(this, true, "1", "Enter EmailId").setVisible(true);
		txtSenderEmailId.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	    else
	    {
		new frmAlfaNumericKeyBoard(this, true, txtSenderEmailId.getText(), "1", "Enter EmailId").setVisible(true);
		txtSenderEmailId.setText(clsGlobalVarClass.gKeyboardValue);
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtSenderEmailIdMouseClicked

    private void chkBoxAllowNewAreaMasterFromCustMasterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkBoxAllowNewAreaMasterFromCustMasterActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkBoxAllowNewAreaMasterFromCustMasterActionPerformed

    private void chkDelBoyCompulsoryOnDirectBillerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkDelBoyCompulsoryOnDirectBillerActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkDelBoyCompulsoryOnDirectBillerActionPerformed

    private void chkEnableSettleBtnForDirectBillerBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkEnableSettleBtnForDirectBillerBillActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkEnableSettleBtnForDirectBillerBillActionPerformed

    private void chkPrintKOTToLocalPrinterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkPrintKOTToLocalPrinterActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkPrintKOTToLocalPrinterActionPerformed

    private void chkItemQtyNumpadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkItemQtyNumpadActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkItemQtyNumpadActionPerformed

    private void cmbMenuItemSortingOnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMenuItemSortingOnActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_cmbMenuItemSortingOnActionPerformed

    private void chkManualAdvOrderCompulsoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkManualAdvOrderCompulsoryActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkManualAdvOrderCompulsoryActionPerformed

    private void chkPrintManualAdvOrderOnBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkPrintManualAdvOrderOnBillActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkPrintManualAdvOrderOnBillActionPerformed

    private void dteHOServerDatePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dteHOServerDatePropertyChange

    }//GEN-LAST:event_dteHOServerDatePropertyChange

    private void dteHOServerDateHierarchyChanged(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_dteHOServerDateHierarchyChanged

    }//GEN-LAST:event_dteHOServerDateHierarchyChanged

    private void chkShowItemStkColumnInDBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkShowItemStkColumnInDBActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkShowItemStkColumnInDBActionPerformed

    private void chkChangeQtyForExternalCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkChangeQtyForExternalCodeActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkChangeQtyForExternalCodeActionPerformed

    private void chkPrinterErrorMessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkPrinterErrorMessageActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkPrinterErrorMessageActionPerformed

    private void chkAreaMasterCompulsoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkAreaMasterCompulsoryActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkAreaMasterCompulsoryActionPerformed

    private void chkPostSalesDataToMMSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkPostSalesDataToMMSActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkPostSalesDataToMMSActionPerformed

    private void chkPrintForVoidBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkPrintForVoidBillActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkPrintForVoidBillActionPerformed

    private void chkActivePromotionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkActivePromotionsActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkActivePromotionsActionPerformed

    private void txtCustSeriesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCustSeriesMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_txtCustSeriesMouseClicked

    private void chkSkip_Waiter_SelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSkip_Waiter_SelectionActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkSkip_Waiter_SelectionActionPerformed

    private void chkSlabBasedHomeDelChargesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSlabBasedHomeDelChargesActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkSlabBasedHomeDelChargesActionPerformed

    private void chkEditHomeDeliveryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkEditHomeDeliveryActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkEditHomeDeliveryActionPerformed

    private void chkAreaWisePricingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkAreaWisePricingActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkAreaWisePricingActionPerformed

    private void txtMaxDiscountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtMaxDiscountMouseClicked

	if (txtMaxDiscount.getText().length() == 0)
	{
	    new frmNumericKeyboard(this, true, "", "Double", "Enter Discount Amount").setVisible(true);
	    txtMaxDiscount.setText(clsGlobalVarClass.gNumerickeyboardValue);
	}
	else
	{
	    new frmNumericKeyboard(this, true, txtMaxDiscount.getText(), "Double", "Enter Discount Amount").setVisible(true);
	    txtMaxDiscount.setText(clsGlobalVarClass.gNumerickeyboardValue);
	}
    }//GEN-LAST:event_txtMaxDiscountMouseClicked

    private void cmbChangeThemeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbChangeThemeActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_cmbChangeThemeActionPerformed

    private void txtWebServiceLinkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtWebServiceLinkMouseClicked
	// TODO add your handling code here:
	try
	{
	    if (txtWebServiceLink.getText().length() == 0)
	    {
		new frmAlfaNumericKeyBoard(this, true, "1", "Enter Web-Service Link").setVisible(true);
		txtWebServiceLink.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	    else
	    {
		new frmAlfaNumericKeyBoard(this, true, txtWebServiceLink.getText(), "1", "Enter Web-Service Link").setVisible(true);
		txtWebServiceLink.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtWebServiceLinkMouseClicked

    private void chkPrintTDHItemsInBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkPrintTDHItemsInBillActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkPrintTDHItemsInBillActionPerformed

    private void chkPrintShortNameOnKOTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkPrintShortNameOnKOTActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkPrintShortNameOnKOTActionPerformed

    private void chkPrintKotForDirectBillerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkPrintKotForDirectBillerActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkPrintKotForDirectBillerActionPerformed

    private void chkManualBillNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkManualBillNoActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkManualBillNoActionPerformed

    private void txtServiceTaxnoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtServiceTaxnoMouseClicked
	// TODO add your handling code here:
	try
	{
	    if (txtServiceTaxno.getText().length() == 0)
	    {
		new frmAlfaNumericKeyBoard(this, true, "1", "Enter Service Tax No").setVisible(true);
		txtServiceTaxno.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	    else
	    {
		new frmAlfaNumericKeyBoard(this, true, txtServiceTaxno.getText(), "1", "Enter Service Tax No").setVisible(true);
		txtServiceTaxno.setText(clsGlobalVarClass.gKeyboardValue);
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtServiceTaxnoMouseClicked

    private void chkShowBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkShowBillActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkShowBillActionPerformed

    private void txtVatNoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtVatNoMouseClicked
	// TODO add your handling code here:
	try
	{
	    if (txtVatNo.getText().length() == 0)
	    {
		new frmAlfaNumericKeyBoard(this, true, "1", "Enter Vat No").setVisible(true);
		txtVatNo.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	    else
	    {
		new frmAlfaNumericKeyBoard(this, true, txtVatNo.getText(), "1", "Enter Vat No").setVisible(true);
		txtVatNo.setText(clsGlobalVarClass.gKeyboardValue);
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtVatNoMouseClicked

    private void chkPrintVatNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkPrintVatNoActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkPrintVatNoActionPerformed

    private void chkEffectOnPSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkEffectOnPSPActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkEffectOnPSPActionPerformed

    private void chkMultiBillPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkMultiBillPrintActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkMultiBillPrintActionPerformed

    private void cmbPrintModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbPrintModeActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_cmbPrintModeActionPerformed

    private void txtBillFooterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtBillFooterMouseClicked
	// TODO add your handling code here:
	try
	{
	    if (txtBillFooter.getText().length() == 0)
	    {
		new frmAlfaNumericKeyBoard(this, true, "1", "Enter Bill Footer").setVisible(true);
		txtBillFooter.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	    else
	    {
		new frmAlfaNumericKeyBoard(this, true, txtBillFooter.getText(), "1", "Enter Bill Footer").setVisible(true);
		txtBillFooter.setText(clsGlobalVarClass.gKeyboardValue);
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtBillFooterMouseClicked

    private void btnBrowseImagePath1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBrowseImagePath1MouseClicked

	try
	{
	    JFileChooser jfc = new JFileChooser();
	    if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
	    {
		reportImageFile = jfc.getSelectedFile();
		reportImagePath = reportImageFile.getAbsolutePath();
		imageIcon = new ImageIcon(reportImagePath);
		lblReportImageIcon.setIcon(imageIcon);
		lblReportImageIcon.setText("");
		reportImgInputStream = new FileInputStream(reportImageFile);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }//GEN-LAST:event_btnBrowseImagePath1MouseClicked

    private void txtPincodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPincodeMouseClicked
	// TODO add your handling code here:
	try
	{

	    if (txtPincode.getText().length() == 0)
	    {
		//new frmNumericKeyboard(this, true, "Prop Setup", "Enter Pin Code").setVisible(true);
		new frmNumericKeyboard(this, true, "", "Long", "Enter Pin Code").setVisible(true);
		txtPincode.setText(clsGlobalVarClass.gNumerickeyboardValue);
	    }
	    else
	    {
		new frmNumericKeyboard(this, true, txtPincode.getText(), "Long", "Enter Pin Code").setVisible(true);
		txtPincode.setText(clsGlobalVarClass.gNumerickeyboardValue);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtPincodeMouseClicked

    private void txtTelephoneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTelephoneMouseClicked
	// TODO add your handling code here:
	try
	{

	    if (txtTelephone.getText().length() == 0)
	    {
		new frmAlfaNumericKeyBoard(this, true, "1", "Enter Telephone Number").setVisible(true);
		txtTelephone.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	    else
	    {
		new frmAlfaNumericKeyBoard(this, true, txtTelephone.getText(), "1", "Enter Telephone Number").setVisible(true);
		txtTelephone.setText(clsGlobalVarClass.gKeyboardValue);
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtTelephoneMouseClicked

    private void txtEmailAddressMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtEmailAddressMouseClicked
	// TODO add your handling code here:
	try
	{
	    if (txtEmailAddress.getText().length() == 0)
	    {
		new frmAlfaNumericKeyBoard(this, true, "1", "Enter Email Address").setVisible(true);
		txtEmailAddress.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	    else
	    {
		new frmAlfaNumericKeyBoard(this, true, txtEmailAddress.getText(), "1", "Enter Email Address").setVisible(true);
		txtEmailAddress.setText(clsGlobalVarClass.gKeyboardValue);
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtEmailAddressMouseClicked

    private void txtShopAddressLine3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtShopAddressLine3MouseClicked
	// TODO add your handling code here:
	try
	{
	    if (txtShopAddressLine3.getText().length() == 0)
	    {
		new frmAlfaNumericKeyBoard(this, true, "1", "Enter Shop Address 3").setVisible(true);
		txtShopAddressLine3.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	    else
	    {
		new frmAlfaNumericKeyBoard(this, true, txtShopAddressLine3.getText(), "1", "Enter Shop Address 3").setVisible(true);
		txtShopAddressLine3.setText(clsGlobalVarClass.gKeyboardValue);
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtShopAddressLine3MouseClicked

    private void txtShopAddressLine1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtShopAddressLine1MouseClicked
	// TODO add your handling code here:
	try
	{
	    if (txtShopAddressLine1.getText().length() == 0)
	    {
		new frmAlfaNumericKeyBoard(this, true, "1", "Enter Shop Address 1").setVisible(true);
		txtShopAddressLine1.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	    else
	    {
		new frmAlfaNumericKeyBoard(this, true, txtShopAddressLine1.getText(), "1", "Enter Shop Address 1").setVisible(true);
		txtShopAddressLine1.setText(clsGlobalVarClass.gKeyboardValue);
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtShopAddressLine1MouseClicked

    private void txtShopAddressLine2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtShopAddressLine2MouseClicked
	// TODO add your handling code here:
	try
	{
	    if (txtShopAddressLine2.getText().length() == 0)
	    {
		new frmAlfaNumericKeyBoard(this, true, "1", "Enter Shop Address 2").setVisible(true);
		txtShopAddressLine2.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	    else
	    {
		new frmAlfaNumericKeyBoard(this, true, txtShopAddressLine2.getText(), "1", "Enter Shop Address 2").setVisible(true);
		txtShopAddressLine2.setText(clsGlobalVarClass.gKeyboardValue);
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtShopAddressLine2MouseClicked

    private void txtClientNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtClientNameMouseClicked
	// TODO add your handling code here:
	try
	{
	    if ("SAVE".equalsIgnoreCase(btnNew.getText()))
	    {
		if (txtClientName.getText().length() == 0)
		{
		    new frmAlfaNumericKeyBoard(this, true, "1", "Enter Client Name").setVisible(true);
		    txtClientName.setText(clsGlobalVarClass.gKeyboardValue);
		}
		else
		{
		    new frmAlfaNumericKeyBoard(this, true, txtClientName.getText(), "1", "Enter Client Name").setVisible(true);
		    txtClientName.setText(clsGlobalVarClass.gKeyboardValue);
		}
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtClientNameMouseClicked

    private void txtClientCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtClientCodeMouseClicked
	// TODO add your handling code here:
	try
	{
	    btnNew.setText("UPDATE");//Update
	    //new frmSearchForm(this, "FrmProperty").setVisible(true);
	    objUtility.funCallForSearchForm("Property");
	    new frmSearchFormDialog(this, true).setVisible(true);
	    if (clsGlobalVarClass.gSearchItemClicked)
	    {
		Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
		setData(data);
		clsGlobalVarClass.gSearchItemClicked = false;
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtClientCodeMouseClicked

    private void txtJioMoneyTIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtJioMoneyTIDActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtJioMoneyTIDActionPerformed

    private void txtJioActivationCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtJioActivationCodeActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtJioActivationCodeActionPerformed

    private void txtJioDeviceIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtJioDeviceIDActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtJioDeviceIDActionPerformed

    private void btnFetchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFetchMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnFetchMouseClicked

    private void btnFetchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFetchActionPerformed

	//Fetch device
	String DeviceID;
	DeviceID = funFetchDeviceID("localhost", "5150");
	txtJioDeviceID.setText(DeviceID);

    }//GEN-LAST:event_btnFetchActionPerformed

    private void chkAutoAreaSelectionInMakeKOTActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkAutoAreaSelectionInMakeKOTActionPerformed
    {//GEN-HEADEREND:event_chkAutoAreaSelectionInMakeKOTActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkAutoAreaSelectionInMakeKOTActionPerformed

    private void cmbConsolidatedKOTPrinterPortActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbConsolidatedKOTPrinterPortActionPerformed
    {//GEN-HEADEREND:event_cmbConsolidatedKOTPrinterPortActionPerformed
	// 31-03-2015
	if (!cmbConsolidatedKOTPrinterPort.getSelectedItem().toString().isEmpty())
	{
	    txtConsolidatedKOTPrinterPort.setText(cmbConsolidatedKOTPrinterPort.getSelectedItem().toString());
	    btnTestConsolidatedKOTPrinterPort.setVisible(true);
	}
	else
	{
	    if (!txtConsolidatedKOTPrinterPort.getText().isEmpty())
	    {
		btnTestConsolidatedKOTPrinterPort.setVisible(true);
	    }
	    else
	    {
		btnTestConsolidatedKOTPrinterPort.setVisible(false);
	    }

	}
    }//GEN-LAST:event_cmbConsolidatedKOTPrinterPortActionPerformed

    private void cmbConsolidatedKOTPrinterPortKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbConsolidatedKOTPrinterPortKeyPressed
    {//GEN-HEADEREND:event_cmbConsolidatedKOTPrinterPortKeyPressed

    }//GEN-LAST:event_cmbConsolidatedKOTPrinterPortKeyPressed

    private void txtConsolidatedKOTPrinterPortMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtConsolidatedKOTPrinterPortMouseClicked
    {//GEN-HEADEREND:event_txtConsolidatedKOTPrinterPortMouseClicked
	try
	{
	    if (txtConsolidatedKOTPrinterPort.getText().length() == 0)
	    {
		new frmAlfaNumericKeyBoard(this, true, "1", "Please Enter Printer Name.").setVisible(true);
		txtConsolidatedKOTPrinterPort.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	    else
	    {
		new frmAlfaNumericKeyBoard(this, true, txtConsolidatedKOTPrinterPort.getText(), "1", "Please Enter Printer Name.").setVisible(true);
		txtConsolidatedKOTPrinterPort.setText(clsGlobalVarClass.gKeyboardValue);
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtConsolidatedKOTPrinterPortMouseClicked

    private void txtConsolidatedKOTPrinterPortActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtConsolidatedKOTPrinterPortActionPerformed
    {//GEN-HEADEREND:event_txtConsolidatedKOTPrinterPortActionPerformed

    }//GEN-LAST:event_txtConsolidatedKOTPrinterPortActionPerformed

    private void txtConsolidatedKOTPrinterPortKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtConsolidatedKOTPrinterPortKeyPressed
    {//GEN-HEADEREND:event_txtConsolidatedKOTPrinterPortKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtConsolidatedKOTPrinterPortKeyPressed

    private void btnTestConsolidatedKOTPrinterPortMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnTestConsolidatedKOTPrinterPortMouseClicked
    {//GEN-HEADEREND:event_btnTestConsolidatedKOTPrinterPortMouseClicked
	objUtility.funTestPrint(txtConsolidatedKOTPrinterPort.getText().trim());
    }//GEN-LAST:event_btnTestConsolidatedKOTPrinterPortMouseClicked

    private void btnTestConsolidatedKOTPrinterPortActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnTestConsolidatedKOTPrinterPortActionPerformed
    {//GEN-HEADEREND:event_btnTestConsolidatedKOTPrinterPortActionPerformed

    }//GEN-LAST:event_btnTestConsolidatedKOTPrinterPortActionPerformed

    private void btnTestConsolidatedKOTPrinterPortKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnTestConsolidatedKOTPrinterPortKeyPressed
    {//GEN-HEADEREND:event_btnTestConsolidatedKOTPrinterPortKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_btnTestConsolidatedKOTPrinterPortKeyPressed

    private void btnTestEmailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTestEmailMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnTestEmailMouseClicked

    private void btnTestEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTestEmailActionPerformed
	// TODO add your handling code here:
	funTestEmailSetup();

    }//GEN-LAST:event_btnTestEmailActionPerformed

    private void btnTestEmailKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnTestEmailKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_btnTestEmailKeyPressed

    private void btnTestWebServiceMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnTestWebServiceMouseClicked
    {//GEN-HEADEREND:event_btnTestWebServiceMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnTestWebServiceMouseClicked

    private void btnTestWebServiceActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnTestWebServiceActionPerformed
    {//GEN-HEADEREND:event_btnTestWebServiceActionPerformed

	boolean flagResponse = objUtility.funGetWebServiceConnectionStatus(txtWebServiceLink.getText().trim());

    }//GEN-LAST:event_btnTestWebServiceActionPerformed

    private void btnTestWebServiceKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnTestWebServiceKeyPressed
    {//GEN-HEADEREND:event_btnTestWebServiceKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_btnTestWebServiceKeyPressed

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jButton1MouseClicked
    {//GEN-HEADEREND:event_jButton1MouseClicked

	if (txtAreaSMSApi.getText().trim().isEmpty())
	{
	    new frmOkPopUp(null, "Please Enter SMS API.", "Error", 1).setVisible(true);
	    return;
	}

	String[] mobileNos = txtSMSMobileNo.getText().split(",");
	for (int i = 0; i < mobileNos.length; i++)
	{
	    if (!mobileNos[i].matches("\\d{10}"))
	    {
		new frmOkPopUp(null, "Please Enter Valid Mobile Number.", "Error", 1).setVisible(true);
		return;
	    }
	}
//        if (!txtSMSMobileNo.getText().matches("\\d{10}"))
//        {
//            new frmOkPopUp(null, "Please Enter Valid Mobile Number.", "Error", 1).setVisible(true);
//            return;
//        }

	objUtility.funSendTestSMS(txtSMSMobileNo.getText(), cmbSMSType.getSelectedItem().toString().toUpperCase() + " Test SMS");
    }//GEN-LAST:event_jButton1MouseClicked

    private void chkDayEndSMSYNActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkDayEndSMSYNActionPerformed
    {//GEN-HEADEREND:event_chkDayEndSMSYNActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkDayEndSMSYNActionPerformed

    private void chkVoidKOTSMSYNActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkVoidKOTSMSYNActionPerformed
    {//GEN-HEADEREND:event_chkVoidKOTSMSYNActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkVoidKOTSMSYNActionPerformed

    private void chkNCKOTSMSYNActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkNCKOTSMSYNActionPerformed
    {//GEN-HEADEREND:event_chkNCKOTSMSYNActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkNCKOTSMSYNActionPerformed

    private void chkVoidBillSMSYNActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkVoidBillSMSYNActionPerformed
    {//GEN-HEADEREND:event_chkVoidBillSMSYNActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkVoidBillSMSYNActionPerformed

    private void chkModifyBillSMSYNActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkModifyBillSMSYNActionPerformed
    {//GEN-HEADEREND:event_chkModifyBillSMSYNActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkModifyBillSMSYNActionPerformed

    private void chkUnsettleBillSMSYNActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkUnsettleBillSMSYNActionPerformed
    {//GEN-HEADEREND:event_chkUnsettleBillSMSYNActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkUnsettleBillSMSYNActionPerformed

    private void chkComplementaryBillSMSYNActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkComplementaryBillSMSYNActionPerformed
    {//GEN-HEADEREND:event_chkComplementaryBillSMSYNActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkComplementaryBillSMSYNActionPerformed

    private void chkVoidAdvOrderSMSYNActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkVoidAdvOrderSMSYNActionPerformed
    {//GEN-HEADEREND:event_chkVoidAdvOrderSMSYNActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkVoidAdvOrderSMSYNActionPerformed

    private void cmbSMSTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbSMSTypeActionPerformed
    {//GEN-HEADEREND:event_cmbSMSTypeActionPerformed
	funSMSTypeSelect();
    }//GEN-LAST:event_cmbSMSTypeActionPerformed

    private void jButton1MouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jButton1MouseEntered
    {//GEN-HEADEREND:event_jButton1MouseEntered
	// TODO add your handling code here:
    }//GEN-LAST:event_jButton1MouseEntered

    private void chkShowUnSettlementFormActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkShowUnSettlementFormActionPerformed
    {//GEN-HEADEREND:event_chkShowUnSettlementFormActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkShowUnSettlementFormActionPerformed

    private void chkPrintOpenItemsOnBillActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkPrintOpenItemsOnBillActionPerformed
    {//GEN-HEADEREND:event_chkPrintOpenItemsOnBillActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkPrintOpenItemsOnBillActionPerformed

    private void chkScanQRYNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkScanQRYNActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkScanQRYNActionPerformed

    private void chkPrintHomeDeliveryYNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkPrintHomeDeliveryYNActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkPrintHomeDeliveryYNActionPerformed

    private void chkShowPurchaseRateInDirectBillerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkShowPurchaseRateInDirectBillerActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkShowPurchaseRateInDirectBillerActionPerformed

    private void chkPrintItemsOnMoveKOTMoveTableActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkPrintItemsOnMoveKOTMoveTableActionPerformed
    {//GEN-HEADEREND:event_chkPrintItemsOnMoveKOTMoveTableActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkPrintItemsOnMoveKOTMoveTableActionPerformed

    private void chkAreaWIsePromotionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkAreaWIsePromotionsActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkAreaWIsePromotionsActionPerformed

    private void chkTableReservationForCustomerActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkShowPurchaseRateInDirectBiller1ActionPerformed
    {//GEN-HEADEREND:event_chkShowPurchaseRateInDirectBiller1ActionPerformed

    }//GEN-LAST:event_chkShowPurchaseRateInDirectBiller1ActionPerformed

    private void chkPOSWiseItemLinkeUpToMMSProductActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkPOSWiseItemLinkeUpToMMSProductActionPerformed
    {//GEN-HEADEREND:event_chkPOSWiseItemLinkeUpToMMSProductActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkPOSWiseItemLinkeUpToMMSProductActionPerformed

    private void chkEnableMasterDiscountActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkEnableMasterDiscountActionPerformed
    {//GEN-HEADEREND:event_chkEnableMasterDiscountActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkEnableMasterDiscountActionPerformed

    private void chkEnableNFCInterfaceActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkEnableNFCInterfaceActionPerformed
    {//GEN-HEADEREND:event_chkEnableNFCInterfaceActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkEnableNFCInterfaceActionPerformed

    private void txtAuthenticationKeyActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtAuthenticationKeyActionPerformed
    {//GEN-HEADEREND:event_txtAuthenticationKeyActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtAuthenticationKeyActionPerformed

    private void txtSaltActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtSaltActionPerformed
    {//GEN-HEADEREND:event_txtSaltActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtSaltActionPerformed

    private void btnAuthorizeMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnAuthorizeMouseClicked
    {//GEN-HEADEREND:event_btnAuthorizeMouseClicked
	// TODO add your handling code here:
	if (txtSuperMerchantCode.getText().toString().length() > 0)
	{
	    clsBenowIntegration objBenowIntegration = new clsBenowIntegration();
	    JSONObject jsonOTPDtls = objBenowIntegration.funGetOTP(txtMerchantCode.getText().toString(), txtSuperMerchantCode.getText().toString());
	    String responseFromAPI = jsonOTPDtls.get("responseFromAPI").toString();
	    if (responseFromAPI.equalsIgnoreCase("true"))
	    {
		txtOTP.setVisible(true);
		btnOK.setVisible(true);
		new frmNumericKeyboard(this, true, "", "Long", "Enter OTP.").setVisible(true);
		txtOTP.setText(clsGlobalVarClass.gNumerickeyboardValue);
		clsGlobalVarClass.gNumerickeyboardValue = "";
	    }
	}
    }//GEN-LAST:event_btnAuthorizeMouseClicked

    private void txtMerchantCodeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtMerchantCodeActionPerformed
    {//GEN-HEADEREND:event_txtMerchantCodeActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtMerchantCodeActionPerformed

    private void btnOKMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnOKMouseClicked
    {//GEN-HEADEREND:event_btnOKMouseClicked
	// TODO add your handling code here:
	if (txtOTP.getText().toString().length() > 0)
	{
	    clsBenowIntegration objBenowIntegration = new clsBenowIntegration();
	    JSONObject jsonOTPDtls = objBenowIntegration.funConfirmOTP(txtMerchantCode.getText().toString(), txtOTP.getText().toString(), txtSuperMerchantCode.getText().toString());
	    String mobileNo = jsonOTPDtls.get("mobileNumber").toString();
	    if (mobileNo.isEmpty())
	    {

	    }
	}
    }//GEN-LAST:event_btnOKMouseClicked

    private void chkEnableLockTablesActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkEnableLockTablesActionPerformed
    {//GEN-HEADEREND:event_chkEnableLockTablesActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkEnableLockTablesActionPerformed

    private void chkPopUpToApplyPromotionsOnBillActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkPopUpToApplyPromotionsOnBillActionPerformed
    {//GEN-HEADEREND:event_chkPopUpToApplyPromotionsOnBillActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkPopUpToApplyPromotionsOnBillActionPerformed

    private void chkRoundOffBillAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkRoundOffBillAmountActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkRoundOffBillAmountActionPerformed

    private void txtNoOfDecimalPlacesActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtNoOfDecimalPlacesActionPerformed
    {//GEN-HEADEREND:event_txtNoOfDecimalPlacesActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtNoOfDecimalPlacesActionPerformed

    private void chkSendDBBackupOnMailActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkSendDBBackupOnMailActionPerformed
    {//GEN-HEADEREND:event_chkSendDBBackupOnMailActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkSendDBBackupOnMailActionPerformed

    private void chkPrintOrderNoOnBillActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkPrintOrderNoOnBillActionPerformed
    {//GEN-HEADEREND:event_chkPrintOrderNoOnBillActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkPrintOrderNoOnBillActionPerformed

    private void chkPrintDeviceUserDtlOnKOTActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkPrintDeviceUserDtlOnKOTActionPerformed
    {//GEN-HEADEREND:event_chkPrintDeviceUserDtlOnKOTActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkPrintDeviceUserDtlOnKOTActionPerformed

    private void chkAutoAddKOTToBillActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkAutoAddKOTToBillActionPerformed
    {//GEN-HEADEREND:event_chkAutoAddKOTToBillActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkAutoAddKOTToBillActionPerformed

    private void chkAreaWiseCostCenterKOTPrintingActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkAreaWiseCostCenterKOTPrintingActionPerformed
    {//GEN-HEADEREND:event_chkAreaWiseCostCenterKOTPrintingActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkAreaWiseCostCenterKOTPrintingActionPerformed

    private void txtWeraMerchantOutletIdActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtWeraMerchantOutletIdActionPerformed
    {//GEN-HEADEREND:event_txtWeraMerchantOutletIdActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtWeraMerchantOutletIdActionPerformed

    private void txtWeraAuthenticationAPIKeyActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtWeraAuthenticationAPIKeyActionPerformed
    {//GEN-HEADEREND:event_txtWeraAuthenticationAPIKeyActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtWeraAuthenticationAPIKeyActionPerformed

    private void chkFireCommunicationActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkFireCommunicationActionPerformed
    {//GEN-HEADEREND:event_chkFireCommunicationActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkFireCommunicationActionPerformed

    private void txtUSDCrrencyConverionRateActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtUSDCrrencyConverionRateActionPerformed
    {//GEN-HEADEREND:event_txtUSDCrrencyConverionRateActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtUSDCrrencyConverionRateActionPerformed

    private void txtDBBackupReceiverEmailIdMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtDBBackupReceiverEmailIdMouseClicked
    {//GEN-HEADEREND:event_txtDBBackupReceiverEmailIdMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_txtDBBackupReceiverEmailIdMouseClicked

    private void chkPrintMoveTableMoveKOTActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkPrintMoveTableMoveKOTActionPerformed
    {//GEN-HEADEREND:event_chkPrintMoveTableMoveKOTActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkPrintMoveTableMoveKOTActionPerformed

    private void chkPrintQtyTotalActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkPrintQtyTotalActionPerformed
    {//GEN-HEADEREND:event_chkPrintQtyTotalActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_chkPrintQtyTotalActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAuthorize;
    private javax.swing.JButton btnBrowseImagePath1;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnFetch;
    private javax.swing.JButton btnLikePOS;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnOK;
    private javax.swing.JButton btnShiftBillSettlement;
    private javax.swing.JButton btnShiftSendHomeDelievery;
    private javax.swing.JButton btnTestConsolidatedKOTPrinterPort;
    private javax.swing.JButton btnTestEmail;
    private javax.swing.JButton btnTestWebService;
    private javax.swing.JCheckBox chkActivePromotions;
    private javax.swing.JCheckBox chkAllowToCalculateItemWeight;
    private javax.swing.JCheckBox chkAreaMasterCompulsory;
    private javax.swing.JCheckBox chkAreaWIsePromotions;
    private javax.swing.JCheckBox chkAreaWiseCostCenterKOTPrinting;
    private javax.swing.JCheckBox chkAreaWisePricing;
    private javax.swing.JCheckBox chkAutoAddKOTToBill;
    private javax.swing.JCheckBox chkAutoAreaSelectionInMakeKOT;
    private javax.swing.JCheckBox chkAutoShowPopItems;
    private javax.swing.JCheckBox chkBillSettlementSMS;
    private javax.swing.JCheckBox chkBoxAllowNewAreaMasterFromCustMaster;
    private javax.swing.JCheckBox chkCalculateDiscItemWise;
    private javax.swing.JCheckBox chkCalculateTaxOnMakeKOT;
    private javax.swing.JCheckBox chkCarryForwardFloatAmtToNextDay;
    private javax.swing.JCheckBox chkChangeQtyForExternalCode;
    private javax.swing.JCheckBox chkCheckDebitCardBalOnTrans;
    private javax.swing.JCheckBox chkComplementaryBillSMSYN;
    private javax.swing.JCheckBox chkDayEnd;
    private javax.swing.JCheckBox chkDayEndSMSYN;
    private javax.swing.JCheckBox chkDelBoyCompulsoryOnDirectBiller;
    private javax.swing.JCheckBox chkDontShowAdvOrderInOtherPOS;
    private javax.swing.JCheckBox chkEditHomeDelivery;
    private javax.swing.JCheckBox chkEffectOnPSP;
    private javax.swing.JCheckBox chkEnableBillSeries;
    private javax.swing.JCheckBox chkEnableBothPrintAndSettleBtnForDB;
    private javax.swing.JCheckBox chkEnableDineIn;
    private javax.swing.JCheckBox chkEnableKOT;
    private javax.swing.JCheckBox chkEnableLockTables;
    private javax.swing.JCheckBox chkEnableMasterDiscount;
    private javax.swing.JCheckBox chkEnableNFCInterface;
    private javax.swing.JCheckBox chkEnablePMSIntegration;
    private javax.swing.JCheckBox chkEnableSettleBtnForDirectBillerBill;
    private javax.swing.JCheckBox chkExpDateForCreditCardBillYN;
    private javax.swing.JCheckBox chkFireCommunication;
    private javax.swing.JCheckBox chkGenrateMI;
    private javax.swing.JCheckBox chkHomeDelSMS;
    private javax.swing.JCheckBox chkItemQtyNumpad;
    private javax.swing.JCheckBox chkItemWiseKOTPrintYN;
    private javax.swing.JCheckBox chkLockDataOnShift;
    private javax.swing.JCheckBox chkManualAdvOrderCompulsory;
    private javax.swing.JCheckBox chkManualBillNo;
    private javax.swing.JCheckBox chkMemberAsTable;
    private javax.swing.JCheckBox chkMemberCodeForKOTJPOS;
    private javax.swing.JCheckBox chkMemberCodeForKOTMPOS;
    private javax.swing.JCheckBox chkMemberCodeForKotInMposByCardSwipe;
    private javax.swing.JCheckBox chkMemberCodeForMakeBillInMPOS;
    private javax.swing.JCheckBox chkModifyBillSMSYN;
    private javax.swing.JCheckBox chkMoveKOTToOtherPOS;
    private javax.swing.JCheckBox chkMoveTableToOtherPOS;
    private javax.swing.JCheckBox chkMultiBillPrint;
    private javax.swing.JCheckBox chkMultiKOTPrint;
    private javax.swing.JCheckBox chkMultipleWaiterSelectionOnMakeKOT;
    private javax.swing.JCheckBox chkNCKOTSMSYN;
    private javax.swing.JCheckBox chkNegBilling;
    private javax.swing.JCheckBox chkNewBillSeriesForNewDay;
    private javax.swing.JCheckBox chkOpenCashDrawerAfterBillPrint;
    private javax.swing.JCheckBox chkPOSWiseItemLinkeUpToMMSProduct;
    private javax.swing.JCheckBox chkPointsOnBillPrint;
    private javax.swing.JCheckBox chkPopUpToApplyPromotionsOnBill;
    private javax.swing.JCheckBox chkPostSalesDataToMMS;
    private javax.swing.JCheckBox chkPrintBill;
    private javax.swing.JCheckBox chkPrintDeviceUserDtlOnKOT;
    private javax.swing.JCheckBox chkPrintForVoidBill;
    private javax.swing.JCheckBox chkPrintHomeDeliveryYN;
    private javax.swing.JCheckBox chkPrintInclusiveOfAllTaxesOnBill;
    private javax.swing.JCheckBox chkPrintInvoiceOnBill;
    private javax.swing.JCheckBox chkPrintItemsOnMoveKOTMoveTable;
    private javax.swing.JCheckBox chkPrintKOTToLocalPrinter;
    private javax.swing.JCheckBox chkPrintKOTYN;
    private javax.swing.JCheckBox chkPrintKotForDirectBiller;
    private javax.swing.JCheckBox chkPrintManualAdvOrderOnBill;
    private javax.swing.JCheckBox chkPrintModifierQtyOnKOT;
    private javax.swing.JCheckBox chkPrintMoveTableMoveKOT;
    private javax.swing.JCheckBox chkPrintOpenItemsOnBill;
    private javax.swing.JCheckBox chkPrintOrderNoOnBill;
    private javax.swing.JCheckBox chkPrintQtyTotal;
    private javax.swing.JCheckBox chkPrintRemarkAndReasonForReprint;
    private javax.swing.JCheckBox chkPrintShortNameOnKOT;
    private javax.swing.JCheckBox chkPrintTDHItemsInBill;
    private javax.swing.JCheckBox chkPrintTimeOnBill;
    private javax.swing.JCheckBox chkPrintVatNo;
    private javax.swing.JCheckBox chkPrintZeroAmtModifierInBill;
    private javax.swing.JCheckBox chkPrinterErrorMessage;
    private javax.swing.JCheckBox chkProductionLinkup;
    private javax.swing.JCheckBox chkPropertyWiseSalesOrder;
    private javax.swing.JCheckBox chkRoundOffBillAmount;
    private javax.swing.JCheckBox chkScanQRYN;
    private javax.swing.JCheckBox chkSelectCustAddressForBill;
    private javax.swing.JCheckBox chkSelectCustomerCodeFromCardSwipe;
    private javax.swing.JCheckBox chkSelectWaiterFromCardSwipe;
    private javax.swing.JCheckBox chkSendDBBackupOnMail;
    private javax.swing.JCheckBox chkServiceTaxNo;
    private javax.swing.JCheckBox chkSetUpToTimeForAdvOrder;
    private javax.swing.JCheckBox chkSetUpToTimeForUrgentOrder;
    private javax.swing.JCheckBox chkSettlementsFromPOSMaster;
    private javax.swing.JCheckBox chkShiftWiseDayEnd;
    private javax.swing.JCheckBox chkShowBill;
    private javax.swing.JCheckBox chkShowItemDtlsForChangeCustomerOnBill;
    private javax.swing.JCheckBox chkShowItemStkColumnInDB;
    private javax.swing.JCheckBox chkShowOnlyLoginPOSReports;
    private javax.swing.JCheckBox chkShowPopUpForNextItemQuantity;
    private javax.swing.JCheckBox chkShowPurchaseRateInDirectBiller;
    private javax.swing.JCheckBox chkShowUnSettlementForm;
    private javax.swing.JCheckBox chkSkip_Waiter_Selection;
    private javax.swing.JCheckBox chkSkip_pax_selection;
    private javax.swing.JCheckBox chkSlabBasedHomeDelCharges;
    private javax.swing.JCheckBox chkSlipNoForCreditCardBillYN;
    private javax.swing.JCheckBox chkTableReservationForCustomer;
    private javax.swing.JCheckBox chkTakewayCustomerSelection;
    private javax.swing.JCheckBox chkUnsettleBillSMSYN;
    private javax.swing.JCheckBox chkUseVatAndServiceNoFromPos;
    private javax.swing.JCheckBox chkVoidAdvOrderSMSYN;
    private javax.swing.JCheckBox chkVoidBillSMSYN;
    private javax.swing.JCheckBox chkVoidKOTSMSYN;
    private javax.swing.JComboBox cmbAMPM;
    private javax.swing.JComboBox cmbAMPMUrgent;
    private javax.swing.JComboBox cmbApplyDiscountOn;
    private javax.swing.JComboBox cmbBenowPOSIntegrationYN;
    private javax.swing.JComboBox cmbBillFormatType;
    private javax.swing.JComboBox cmbBillPaperSize;
    private javax.swing.JComboBox cmbBillSettlement;
    private javax.swing.JComboBox cmbCMSIntegrationYN;
    private javax.swing.JComboBox cmbCMSPostingType;
    private javax.swing.JComboBox cmbCRMType;
    private javax.swing.JComboBox cmbCardIntfType;
    private javax.swing.JComboBox cmbChangeTheme;
    private javax.swing.JComboBox cmbCity;
    private javax.swing.JComboBox cmbColumnSize;
    private javax.swing.JComboBox cmbConsolidatedKOTPrinterPort;
    private javax.swing.JComboBox cmbCountry;
    private javax.swing.JComboBox cmbDataSendFrequency;
    private javax.swing.JComboBox cmbDineInAreaForDirectBiller;
    private javax.swing.JComboBox cmbEffectOfSales;
    private javax.swing.JComboBox cmbHomeDeliAreaForDirectBiller;
    private javax.swing.JComboBox cmbHours;
    private javax.swing.JComboBox cmbHoursUrgentOrder;
    private javax.swing.JComboBox cmbInrestoPOSIntegrationYN;
    private javax.swing.JComboBox cmbItemType;
    private javax.swing.JComboBox cmbJioPOSIntegrationYN;
    private javax.swing.JComboBox cmbMenuItemDisSeq;
    private javax.swing.JComboBox cmbMenuItemSortingOn;
    private javax.swing.JComboBox cmbMinutes;
    private javax.swing.JComboBox cmbMinutesUrgentOrder;
    private javax.swing.JComboBox cmbNatureOfBusiness;
    private javax.swing.JComboBox cmbPOS;
    private javax.swing.JComboBox cmbPOSForDayEnd;
    private javax.swing.JComboBox cmbPostMMSSalesEffectCostOrLoc;
    private javax.swing.JComboBox cmbPriceFrom;
    private javax.swing.JComboBox cmbPrintMode;
    private javax.swing.JComboBox cmbPrintType;
    private javax.swing.JComboBox cmbRFIDSetup;
    private javax.swing.JComboBox cmbRemoveServiceChargeTaxCode;
    private javax.swing.JComboBox cmbSMSType;
    private javax.swing.JComboBox cmbSendHomeDelivery;
    private javax.swing.JComboBox cmbServerName;
    private javax.swing.JComboBox cmbShowBillsDtlType;
    private javax.swing.JComboBox cmbState;
    private javax.swing.JComboBox cmbStockInOption;
    private javax.swing.JComboBox cmbTakeAwayAreaForDirectBiller;
    private javax.swing.JComboBox cmbWeraIntegrationYN;
    private com.toedter.calendar.JDateChooser dteHOServerDate;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JLayeredPane jLayeredPane2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel lbBenowIntegration;
    private javax.swing.JLabel lbSetUpToTimeForAdvOrder;
    private javax.swing.JLabel lbSetUpToTimeForAdvOrder1;
    private javax.swing.JLabel lbShowOnlyLoginPOSReports;
    private javax.swing.JLabel lbXEmail;
    private javax.swing.JLabel lblApplyDiscountOn;
    private javax.swing.JLabel lblAuditSMS;
    private javax.swing.JLabel lblAuthenticationAPIKey;
    private javax.swing.JLabel lblAuthenticationKey;
    private javax.swing.JLabel lblAutoShowPopularItemsOf;
    private javax.swing.JLabel lblBillFormat;
    private javax.swing.JLabel lblBillSettlementSMS;
    private javax.swing.JLabel lblBillpaperSize;
    private javax.swing.JLabel lblCMSPosting;
    private javax.swing.JLabel lblCRMInterface;
    private javax.swing.JLabel lblCalculateDiscItemWise;
    private javax.swing.JLabel lblCalculateTaxOnMakeKOT;
    private javax.swing.JLabel lblCardIntfType;
    private javax.swing.JLabel lblCarryFwdFloatAmt;
    private javax.swing.JLabel lblChangeTheme;
    private javax.swing.JLabel lblCityPin;
    private javax.swing.JLabel lblColSize;
    private javax.swing.JLabel lblConsolidatedKOTPrinterPort;
    private javax.swing.JLabel lblDBBackupReceiverEmailId;
    private javax.swing.JLabel lblDBName;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDays;
    private javax.swing.JLabel lblDaysBeforeOrderToCancel;
    private javax.swing.JLabel lblDaysBeforeOrderToCancel1;
    private javax.swing.JLabel lblDaysBeforeOrderToCancel2;
    private javax.swing.JLabel lblDebitCardBalChkOnTrans;
    private javax.swing.JLabel lblDineInAreaForDirectBiller;
    private javax.swing.JLabel lblDontShowAdvOrderInOtherPOS;
    private javax.swing.JLabel lblEffectOfSales;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblEnableBillSeries;
    private javax.swing.JLabel lblEnableBothPrintAndSettleBtnForDB;
    private javax.swing.JLabel lblEnablePMSIntegration;
    private javax.swing.JLabel lblEnableShift;
    private javax.swing.JLabel lblEnabledDineIn;
    private javax.swing.JLabel lblExpDateForCreditCardBillYN;
    private javax.swing.JLabel lblFTPServerUserName;
    private javax.swing.JLabel lblFooter;
    private javax.swing.JLabel lblGetWebServiceURL;
    private javax.swing.JLabel lblHOServerDate;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblHomeDeliAreaForDirectBiller;
    private javax.swing.JLabel lblInrestoIntegration;
    private javax.swing.JLabel lblInrestoIntegration1;
    private javax.swing.JLabel lblInrestoPOSId;
    private javax.swing.JLabel lblInrestoPOSKey;
    private javax.swing.JLabel lblInrestoWebServiceURL;
    private javax.swing.JLabel lblInrestoWebServiceURL1;
    private javax.swing.JLabel lblItemWiseKOTYN;
    private javax.swing.JLabel lblJioActivationCode;
    private javax.swing.JLabel lblJioMoneyDeviceID;
    private javax.swing.JLabel lblJioMoneyMID;
    private javax.swing.JLabel lblJioMoneyTID;
    private javax.swing.JLabel lblLastPOSForDayEnd;
    private javax.swing.JLabel lblLockDataOnShift;
    private javax.swing.JLabel lblMaxDiscount;
    private javax.swing.JLabel lblMenuItemDisplaySeq;
    private javax.swing.JLabel lblMenuItemSorting;
    private javax.swing.JLabel lblMerchantCode;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblMoveKOTToOtherPOS1;
    private javax.swing.JLabel lblMoveTableToOtherPOS;
    private javax.swing.JLabel lblMultiWaiterSelection;
    private javax.swing.JLabel lblNatureOfBusiness;
    private javax.swing.JLabel lblNewBillSeriesForNewDay;
    private javax.swing.JLabel lblNoOfAdvReceiptPrint;
    private javax.swing.JLabel lblNoOfLinesInKOTPrint;
    private javax.swing.JLabel lblNoodDecimalPlaces;
    private javax.swing.JLabel lblOpenCashDrawerAfterBillPrint;
    private javax.swing.JLabel lblOutletUID;
    private javax.swing.JLabel lblPOS;
    private javax.swing.JLabel lblPOSID;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblPickSettlementsFromPOSMaster;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblPostMMSSalesEffect;
    private javax.swing.JLabel lblPostWebServiceURL;
    private javax.swing.JLabel lblPrintKOTYN;
    private javax.swing.JLabel lblPrintMode;
    private javax.swing.JLabel lblPrintRemarkAndReasonForReprint;
    private javax.swing.JLabel lblPrintTimeOnBill;
    private javax.swing.JLabel lblPrintZeroAmtModifierInBill;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblProductionLinkUp;
    private javax.swing.JLabel lblPropertyWiseSalesOrder;
    private javax.swing.JLabel lblRFIDYN;
    private javax.swing.JLabel lblReceiverEmailId;
    private javax.swing.JLabel lblRemoveServiceChargeTaxCode;
    private javax.swing.JLabel lblReportImageIcon;
    private javax.swing.JLabel lblReportType;
    private javax.swing.JLabel lblRoundingOffTo;
    private javax.swing.JLabel lblSMSApi;
    private javax.swing.JLabel lblSMSApi1;
    private javax.swing.JLabel lblSMSType;
    private javax.swing.JLabel lblSalt;
    private javax.swing.JLabel lblSelectCustAddressForBill;
    private javax.swing.JLabel lblSelectWaiterFromCardSwipe;
    private javax.swing.JLabel lblSendHomeDelivery;
    private javax.swing.JLabel lblServerName;
    private javax.swing.JLabel lblShopAddress1;
    private javax.swing.JLabel lblShopAddress2;
    private javax.swing.JLabel lblShopAddress3;
    private javax.swing.JLabel lblShopCode;
    private javax.swing.JLabel lblShowBillsDtlType;
    private javax.swing.JLabel lblShowItemDtlsForChangeCustomerOnBill;
    private javax.swing.JLabel lblShowPopUpForNextItemQuantity;
    private javax.swing.JLabel lblSlipNoForCreditCardBillYN;
    private javax.swing.JLabel lblStateCountry;
    private javax.swing.JLabel lblStkInOptions;
    private javax.swing.JLabel lblSuperMerchantCode;
    private javax.swing.JLabel lblTakeAwayAreaForDirectBiller;
    private javax.swing.JLabel lblTelePhoneFax;
    private javax.swing.JLabel lblUSDCrrencyConverionRate;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblUserName;
    private javax.swing.JLabel lblWSClientCode;
    private javax.swing.JLabel lblWeraIntegration;
    private javax.swing.JLabel lblWeraMerchantCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelBillSetup;
    private javax.swing.JPanel panelCMSIntegration;
    private javax.swing.JPanel panelClientSetup;
    private javax.swing.JPanel panelDebitCardSetup;
    private javax.swing.JPanel panelEmailSetup;
    private javax.swing.JPanel panelFTPSetup;
    private javax.swing.JPanel panelFormBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelInrestoIntegration;
    private javax.swing.JPanel panelMainForm;
    private javax.swing.JPanel panelPOSSetup1;
    private javax.swing.JPanel panelPOSSetup2;
    private javax.swing.JPanel panelPOSSetup3;
    private javax.swing.JPanel panelPOSSetup4;
    private javax.swing.JPanel panelPoints;
    private javax.swing.JPanel panelPrinterSetup;
    private javax.swing.JPanel panelRFIDSetup;
    private javax.swing.JPanel panelSMSSetup;
    private javax.swing.JScrollPane printerSetupScrollPane;
    private javax.swing.JComboBox strPOSType;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JTable tblPrinterSetup;
    private javax.swing.JTextField txtAdvRecPrintCount;
    private javax.swing.JTextArea txtAreaBillSettlementSMS;
    private javax.swing.JTextArea txtAreaSMSApi;
    private javax.swing.JTextArea txtAreaSendHomeDeliverySMS;
    private javax.swing.JTextField txtAuthenticationKey;
    private javax.swing.JTextArea txtBillFooter;
    private javax.swing.JTextArea txtBodyPart;
    private javax.swing.JTextField txtCMSWesServiceURL;
    private javax.swing.JTextField txtClientCode;
    private javax.swing.JTextField txtClientName;
    private javax.swing.JPasswordField txtConfirmEmailPassword;
    private javax.swing.JTextField txtConsolidatedKOTPrinterPort;
    private javax.swing.JTextField txtCustSeries;
    private javax.swing.JTextField txtDBBackupReceiverEmailId;
    private javax.swing.JTextField txtDatabaseName;
    private javax.swing.JTextField txtDaysBeforeOrderToCancel;
    private javax.swing.JTextField txtEmailAddress;
    private javax.swing.JPasswordField txtEmailPassword;
    private javax.swing.JTextField txtFTPAddress;
    private javax.swing.JPasswordField txtFTPServerPass;
    private javax.swing.JTextField txtFTPServerUserName;
    private javax.swing.JTextField txtGetWebservice;
    private javax.swing.JTextField txtInrestoPOSId;
    private javax.swing.JTextField txtInrestoPOSKey;
    private javax.swing.JTextField txtInrestoPOSWesServiceURL;
    private javax.swing.JTextField txtJioActivationCode;
    private javax.swing.JTextField txtJioDeviceID;
    private javax.swing.JTextField txtJioMoneyMID;
    private javax.swing.JTextField txtJioMoneyTID;
    private javax.swing.JTextField txtJioPOSWesServiceURL;
    private javax.swing.JTextField txtMaxDiscount;
    private javax.swing.JTextField txtMerchantCode;
    private javax.swing.JTextField txtNoOfDecimalPlaces;
    private javax.swing.JTextField txtNoOfDelDaysForAdvOrder;
    private javax.swing.JTextField txtNoOfDelDaysForUrgentOrder;
    private javax.swing.JTextField txtNoOfLinesInKOTPrint;
    private javax.swing.JTextField txtOTP;
    private javax.swing.JTextField txtOutletUID;
    private javax.swing.JTextField txtPOSID;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtPincode;
    private javax.swing.JTextField txtPostWebservice;
    private javax.swing.JTextField txtReceiverEmailId;
    private javax.swing.JTextField txtRoundingOffTo;
    private javax.swing.JTextField txtSMSMobileNo;
    private javax.swing.JTextField txtSalt;
    private javax.swing.JTextField txtSenderEmailId;
    private javax.swing.JTextField txtServerName;
    private javax.swing.JTextField txtServiceTaxno;
    private javax.swing.JTextField txtShopAddressLine1;
    private javax.swing.JTextField txtShopAddressLine2;
    private javax.swing.JTextField txtShopAddressLine3;
    private javax.swing.JTextField txtShowPopularItemsOfNDays;
    private javax.swing.JTextField txtSuperMerchantCode;
    private javax.swing.JTextField txtTelephone;
    private javax.swing.JTextField txtUSDCrrencyConverionRate;
    private javax.swing.JTextField txtUserName;
    private javax.swing.JTextField txtVatNo;
    private javax.swing.JTextField txtWSClientCode;
    private javax.swing.JTextField txtWebServiceLink;
    private javax.swing.JTextField txtWeraAuthenticationAPIKey;
    private javax.swing.JTextField txtWeraMerchantOutletId;
    private javax.swing.JTextField txtXEmail;
    // End of variables declaration//GEN-END:variables

    void setServiceTaxNo(String text)
    {
	//To change body of generated methods, choose Tools | Templates.
	txtServiceTaxno.setText(text);
    }

    private void funSMSSetup()
    {

	cmbSendHomeDelivery.setVisible(true);
	btnShiftSendHomeDelievery.setVisible(true);
	txtAreaSendHomeDeliverySMS.setVisible(true);

	cmbBillSettlement.setVisible(true);
	btnShiftBillSettlement.setVisible(true);
	txtAreaBillSettlementSMS.setVisible(true);

    }

    private void funShiftBtnFor_Send_Home_Delivery()
    {
	String firstvalue = "", getvalue = "";

	if (txtAreaSendHomeDeliverySMS.getText().trim().length() == 0)
	{
	    firstvalue = cmbSendHomeDelivery.getSelectedItem().toString();
	    firstvalue = "%%" + firstvalue;
	    txtAreaSendHomeDeliverySMS.setText(firstvalue);

	}
	else
	{
	    firstvalue = cmbSendHomeDelivery.getSelectedItem().toString();
	    firstvalue = "%%" + firstvalue;
	    getvalue = txtAreaSendHomeDeliverySMS.getText();
	    SmstextArea = getvalue + firstvalue;
	    txtAreaSendHomeDeliverySMS.setText(SmstextArea);

	}
    }

    private void funShiftBtnFor_Bill_Settlement()
    {
	String firstvalue = "", getvalue = "";

	if (txtAreaBillSettlementSMS.getText().trim().length() == 0)
	{
	    firstvalue = cmbBillSettlement.getSelectedItem().toString();
	    firstvalue = "%%" + firstvalue;
	    txtAreaBillSettlementSMS.setText(firstvalue);

	}
	else
	{
	    firstvalue = cmbBillSettlement.getSelectedItem().toString();
	    firstvalue = "%%" + firstvalue;
	    getvalue = txtAreaBillSettlementSMS.getText();
	    SmstextArea = getvalue + firstvalue;
	    txtAreaBillSettlementSMS.setText(SmstextArea);
	}
    }

    private void funCreateReportImagesFolder()
    {

	try
	{
	    String filePath = System.getProperty("user.dir");
	    File file = new File(filePath + "/ReportImage");
	    if (!file.exists())
	    {
		if (file.mkdir())
		{
		    //System.out.println("Directory is created!");
		}
		else
		{
		    //System.out.println("Failed to create directory!");
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funSetImage()
    {
	try
	{
	    reportImagePath = reportImageFile.getAbsolutePath();
	    imageIcon = new ImageIcon(reportImageFile.getAbsolutePath());
	    lblReportImageIcon.setIcon(imageIcon);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}

    }

    private void funFillPrinterSetupData()
    {
	mapCostCenter = new HashMap<String, String>();
	StringBuilder sqlStringBuilder = new StringBuilder();
	List<clsPrinterSetup> arrListPrinterSetup = new ArrayList<clsPrinterSetup>();

	try
	{
	    sqlStringBuilder.append(" select a.strCostCenterCode,a.strCostCenterName,ifnull(b.strPrimaryPrinterPort,'')as PrimaryPrinter"
		    + " ,ifnull(b.strSecondaryPrinterPort,'')as SecondaryPrinter,ifnull(b.strPrintOnBothPrintersYN,'N')as PrintOnBothPrintersYN "
		    + " from tblcostcentermaster a "
		    + " left outer join tblprintersetup b on a.strCostCenterCode=b.strCostCenterCode");
	    //System.out.println("-->"+sqlStringBuilder);
	    ResultSet costCenterRS = clsGlobalVarClass.dbMysql.executeResultSet(sqlStringBuilder.toString());
	    while (costCenterRS.next())
	    {
		boolean printOnBothPrintersYN = false;
		if (costCenterRS.getString("PrintOnBothPrintersYN").equalsIgnoreCase("Y"))
		{
		    printOnBothPrintersYN = true;
		}
		mapCostCenter.put(costCenterRS.getString("strCostCenterName"), costCenterRS.getString("strCostCenterCode"));

		clsPrinterSetup objPrinterSetup = new clsPrinterSetup();
		objPrinterSetup.setCostCenterName(costCenterRS.getString("strCostCenterName"));
		objPrinterSetup.setPrimaryPrinter(costCenterRS.getString("PrimaryPrinter"));
		objPrinterSetup.setSecondaryPrinter(costCenterRS.getString("SecondaryPrinter"));
		objPrinterSetup.setPrintOnBothPrinters(printOnBothPrintersYN);
		arrListPrinterSetup.add(objPrinterSetup);
	    }

	    funFillPrinterSetupTable(arrListPrinterSetup);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    sqlStringBuilder = null;
	    arrListPrinterSetup = null;
	}
    }

    private int funFillPrinterSetupTable(List<clsPrinterSetup> arrListPrinterSetup)
    {
	DefaultTableModel printerSetupTableModel = (DefaultTableModel) tblPrinterSetup.getModel();
	printerSetupTableModel.setRowCount(0);

	for (clsPrinterSetup objPrinterSetup : arrListPrinterSetup)
	{

	    Object[] row =
	    {
		//costCenterRS.getString("strCostCenterName"), costCenterRS.getString("PrimaryPrinter"), costCenterRS.getString("SecondaryPrinter"), printOnBothPrintersYN
		objPrinterSetup.getCostCenterName(), objPrinterSetup.getPrimaryPrinter(), objPrinterSetup.getSecondaryPrinter(), objPrinterSetup.isPrintOnBothPrinters()
	    };
	    printerSetupTableModel.addRow(row);
	}
	tblPrinterSetup.setModel(printerSetupTableModel);

	return 1;
    }

    private void funSavePrinterSetupData()
    {
	try
	{
	    clsGlobalVarClass.dbMysql.execute("truncate table tblprintersetup");
	    StringBuilder sqlStringBuilder = new StringBuilder("insert into tblprintersetup values ");

	    for (int row = 0; row < tblPrinterSetup.getRowCount(); row++)
	    {
		String costCenterName = tblPrinterSetup.getValueAt(row, 0).toString();
		String costCenterCode = mapCostCenter.get(costCenterName);
		String primaryPrinter = tblPrinterSetup.getValueAt(row, 1).toString();
		String secondaryPrinter = tblPrinterSetup.getValueAt(row, 2).toString();
		String printOnBothPrintersYN = "N";
		if (Boolean.valueOf(tblPrinterSetup.getValueAt(row, 3).toString()))
		{
		    printOnBothPrintersYN = "Y";
		}
		if (row == 0)
		{
		    sqlStringBuilder.append("('" + costCenterCode + "','" + costCenterName + "','" + primaryPrinter + "','" + secondaryPrinter + "','" + printOnBothPrintersYN + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "','N')");
		}
		else
		{
		    sqlStringBuilder.append(",('" + costCenterCode + "','" + costCenterName + "','" + primaryPrinter + "','" + secondaryPrinter + "','" + printOnBothPrintersYN + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "','N')");
		}

		clsGlobalVarClass.dbMysql.execute("update tblcostcentermaster set strPrinterPort='" + primaryPrinter + "',strSecondaryPrinterPort='" + secondaryPrinter + "' "
			+ ",strPrintOnBothPrinters='" + printOnBothPrintersYN + "',strUserEdited='" + clsGlobalVarClass.gUserCode + "',dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "',strDataPostFlag='N' "
			+ " where strcostcentercode='" + costCenterCode + "' ");
	    }
	    //System.out.println("-->"+sqlStringBuilder);
	    if (sqlStringBuilder.toString().contains("("))
	    {
		clsGlobalVarClass.dbMysql.execute(sqlStringBuilder.toString());
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funSavePropertyImage(FileInputStream inputImage)
    {
	try
	{

	    if (!reportImagePath.isEmpty())
	    {
		if (newPropertyPOSCode.equalsIgnoreCase("All"))
		{
		    inputImage = new FileInputStream(reportImagePath);
		    funCreateReportImagesFolder();
		    String userDirectory = System.getProperty("user.dir");
		    File fileReportImage = new File(userDirectory + "\\ReportImage\\imgClientImage.jpg");
		    if (fileReportImage.exists())
		    {
			fileReportImage.delete();
		    }
		    Files.copy(reportImageFile.toPath(), fileReportImage.toPath());
		}
		else
		{
		    inputImage = new FileInputStream(reportImagePath);
		    funCreateReportImagesFolder();
		    String userDirectory = System.getProperty("user.dir");
		    String fileName = "\\ReportImage\\" + "img" + newPropertyPOSCode + ".jpg";
		    File fileReportImage = new File(userDirectory + fileName);
		    if (fileReportImage.exists())
		    {
			fileReportImage.delete();
		    }
		    Files.copy(reportImageFile.toPath(), fileReportImage.toPath());
		}
	    }

	    String query = "insert into tblpropertyimage values(?,?,?)";
	    PreparedStatement pre = clsGlobalVarClass.conPrepareStatement.prepareStatement(query);
	    pre.setString(1, newPropertyPOSCode);
	    if (reportImagePath.isEmpty())
	    {
		pre.setString(2, "");
	    }
	    else
	    {
		pre.setBinaryStream(2, (InputStream) reportImgInputStream, (int) reportImageFile.length());
	    }
	    pre.setString(3, clsGlobalVarClass.gClientCode);

	    // StringBuilder sqlStringBuilder = new StringBuilder("insert into tblpropertyimage values ");
	    //  sqlStringBuilder.append("('" + newPropertyPOSCode + "','" + inputImage + "','" + clsGlobalVarClass.gClientCode + "')");
	    sql = "delete from tblpropertyimage where strPOSCode='" + newPropertyPOSCode + "' ";
	    clsGlobalVarClass.dbMysql.execute(sql);

	    if (newPropertyPOSCode.equalsIgnoreCase("All"))
	    {
		sql = "delete from tblpropertyimage where strPOSCode<>'All' ";
		clsGlobalVarClass.dbMysql.execute(sql);
	    }
	    else
	    {
		sql = "delete from tblpropertyimage where strPOSCode='All' ";
		clsGlobalVarClass.dbMysql.execute(sql);
	    }

	    int exc = pre.executeUpdate();
	    pre.close();
	    // clsGlobalVarClass.dbMysql.execute(sqlStringBuilder.toString());
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funLoadPOS()
    {
	try
	{
	    cmbPOS.removeAllItems();
	    cmbPOS.addItem("All                                                                        All");
	    ResultSet rsPOSForBillSeries = clsGlobalVarClass.dbMysql.executeResultSet("select strPOSCode,strPOSName from tblposmaster order by strPosCode ");
	    while (rsPOSForBillSeries.next())
	    {
		cmbPOS.addItem(rsPOSForBillSeries.getString(2) + "                                                                        " + rsPOSForBillSeries.getString(1));
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funSetPropertyPOSCode()
    {
	funSetDataPOSWise();
    }

    private void funFillPrinters()
    {
	try
	{
	    Vector vPrinterNames;
	    vPrinterNames = objUtility.funGetPrinterNames();

	    for (int cntPrinters = 0; cntPrinters < vPrinterNames.size(); cntPrinters++)
	    {
		cmbConsolidatedKOTPrinterPort.addItem(vPrinterNames.elementAt(cntPrinters).toString());
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public boolean funIsValidEmailAddress(String email)
    {
	boolean result = true;
	try
	{

	    String emails[] = email.split(",");
	    for (int i = 0; i < emails.length; i++)
	    {
		InternetAddress emailAddr = new InternetAddress(emails[i]);
		emailAddr.validate();
	    }

	}
	catch (AddressException ex)
	{
	    result = false;
	}
	return result;
    }

    public void funTestEmailSetup()
    {
	try
	{
	    String to = txtReceiverEmailId.getText().toString();
	    final String from = txtSenderEmailId.getText().toString();
	    final String emailPassord;
	    if (!(from.trim().isEmpty()) && !(to.trim().isEmpty()))
	    {
		if (funIsValidEmailAddress(from) && funIsValidEmailAddress(to))
		{
		    if (Arrays.equals(txtEmailPassword.getPassword(), txtConfirmEmailPassword.getPassword()))
		    {
			emailPassord = String.valueOf(txtEmailPassword.getPassword());
			Properties props = new Properties();
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "465");

			Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator()
			{
			    protected PasswordAuthentication getPasswordAuthentication()
			    {
				return new PasswordAuthentication(from, emailPassord);//change accordingly
			    }
			});

			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));//change accordingly

			String emails[] = to.split(",");
			for (int i = 0; i < emails.length; i++)
			{
			    message.addRecipient(Message.RecipientType.TO, new InternetAddress(emails[i]));
			}

			message.setSubject("Test mail");
			BodyPart messageBodyPart = new MimeBodyPart();
			String data = "";

			data = txtBodyPart.getText();
			data += "\n\n\n\n\n\n\n\n";
			data += "\nThank You,";
			data += "\nTeam SANGUINE";

			// Fill the message
			messageBodyPart.setText(data);
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			// Send the complete message parts
			message.setContent(multipart);
			if (to.length() > 0)
			{
			    //send message  
			    Transport.send(message);
			    JOptionPane.showMessageDialog(this, "Email sent successfully");
			    System.out.println("message sent successfully");
			}

		    }
		    else
		    {
			JOptionPane.showMessageDialog(this, "Password does not match.");
		    }
		}
		else
		{
		    JOptionPane.showMessageDialog(this, "Email id not Valid.");
		}
	    }
	    else
	    {
		JOptionPane.showMessageDialog(this, "Enter  Email Id.");
	    }
	}
	catch (Exception e)
	{
	    JOptionPane.showMessageDialog(this, "Mail Sending Failed");// gmail Acc need  allow to access low secure apps
	    e.printStackTrace();

	}

    }

    private boolean isAuditSMSSelected()
    {
	boolean isAuditSMSSelected = false;

	if (chkDayEndSMSYN.isSelected())
	{
	    isAuditSMSSelected = true;
	}
	if (chkVoidKOTSMSYN.isSelected())
	{
	    isAuditSMSSelected = true;
	}
	if (chkNCKOTSMSYN.isSelected())
	{
	    isAuditSMSSelected = true;
	}
	if (chkVoidAdvOrderSMSYN.isSelected())
	{
	    isAuditSMSSelected = true;
	}
	if (chkVoidBillSMSYN.isSelected())
	{
	    isAuditSMSSelected = true;
	}
	if (chkModifyBillSMSYN.isSelected())
	{
	    isAuditSMSSelected = true;
	}
	if (chkUnsettleBillSMSYN.isSelected())
	{
	    isAuditSMSSelected = true;
	}
	if (chkComplementaryBillSMSYN.isSelected())
	{
	    isAuditSMSSelected = true;
	}

	return isAuditSMSSelected;
    }

    private void funSaveAuditSMS() throws Exception
    {
	ResultSet rsSMSSetup = clsGlobalVarClass.dbMysql.executeResultSet("select * from tblsmssetup where strPOSCode='" + newPropertyPOSCode + "' and strClientCode='" + clsGlobalVarClass.gClientCode + "' ");
	if (rsSMSSetup.next())
	{
	    //DayEnd
	    clsGlobalVarClass.dbMysql.execute("update tblsmssetup "
		    + "set strSendSMSYN='" + (chkDayEndSMSYN.isSelected() ? 'Y' : 'N') + "',longMobileNo='" + txtSMSMobileNo.getText() + "',strUserEdited='" + clsGlobalVarClass.gUserCode + "',dteDateEdited='" + clsGlobalVarClass.gPOSDateForTransaction + "',strDataPostFlag='N' "
		    + "where strPOSCode='" + newPropertyPOSCode + "' and strClientCode='" + clsGlobalVarClass.gClientCode + "' and strTransactionName='DayEnd' ");
	    //VoidKOT
	    clsGlobalVarClass.dbMysql.execute("update tblsmssetup "
		    + "set strSendSMSYN='" + (chkVoidKOTSMSYN.isSelected() ? 'Y' : 'N') + "',longMobileNo='" + txtSMSMobileNo.getText() + "',strUserEdited='" + clsGlobalVarClass.gUserCode + "',dteDateEdited='" + clsGlobalVarClass.gPOSDateForTransaction + "',strDataPostFlag='N' "
		    + "where strPOSCode='" + newPropertyPOSCode + "' and strClientCode='" + clsGlobalVarClass.gClientCode + "' and strTransactionName='VoidKOT' ");
	    //NCKOT
	    clsGlobalVarClass.dbMysql.execute("update tblsmssetup "
		    + "set strSendSMSYN='" + (chkNCKOTSMSYN.isSelected() ? 'Y' : 'N') + "',longMobileNo='" + txtSMSMobileNo.getText() + "',strUserEdited='" + clsGlobalVarClass.gUserCode + "',dteDateEdited='" + clsGlobalVarClass.gPOSDateForTransaction + "',strDataPostFlag='N' "
		    + "where strPOSCode='" + newPropertyPOSCode + "' and strClientCode='" + clsGlobalVarClass.gClientCode + "' and strTransactionName='NCKOT' ");
	    //VoidAdvOrder
	    clsGlobalVarClass.dbMysql.execute("update tblsmssetup "
		    + "set strSendSMSYN='" + (chkVoidAdvOrderSMSYN.isSelected() ? 'Y' : 'N') + "',longMobileNo='" + txtSMSMobileNo.getText() + "',strUserEdited='" + clsGlobalVarClass.gUserCode + "',dteDateEdited='" + clsGlobalVarClass.gPOSDateForTransaction + "',strDataPostFlag='N' "
		    + "where strPOSCode='" + newPropertyPOSCode + "' and strClientCode='" + clsGlobalVarClass.gClientCode + "' and strTransactionName='VoidAdvOrder' ");
	    //VoidBill
	    clsGlobalVarClass.dbMysql.execute("update tblsmssetup "
		    + "set strSendSMSYN='" + (chkVoidBillSMSYN.isSelected() ? 'Y' : 'N') + "',longMobileNo='" + txtSMSMobileNo.getText() + "',strUserEdited='" + clsGlobalVarClass.gUserCode + "',dteDateEdited='" + clsGlobalVarClass.gPOSDateForTransaction + "',strDataPostFlag='N' "
		    + "where strPOSCode='" + newPropertyPOSCode + "' and strClientCode='" + clsGlobalVarClass.gClientCode + "' and strTransactionName='VoidBill' ");
	    //ModifyBill
	    clsGlobalVarClass.dbMysql.execute("update tblsmssetup "
		    + "set strSendSMSYN='" + (chkModifyBillSMSYN.isSelected() ? 'Y' : 'N') + "',longMobileNo='" + txtSMSMobileNo.getText() + "',strUserEdited='" + clsGlobalVarClass.gUserCode + "',dteDateEdited='" + clsGlobalVarClass.gPOSDateForTransaction + "',strDataPostFlag='N' "
		    + "where strPOSCode='" + newPropertyPOSCode + "' and strClientCode='" + clsGlobalVarClass.gClientCode + "' and strTransactionName='ModifyBill' ");
	    //UnsettleBill
	    clsGlobalVarClass.dbMysql.execute("update tblsmssetup "
		    + "set strSendSMSYN='" + (chkUnsettleBillSMSYN.isSelected() ? 'Y' : 'N') + "',longMobileNo='" + txtSMSMobileNo.getText() + "',strUserEdited='" + clsGlobalVarClass.gUserCode + "',dteDateEdited='" + clsGlobalVarClass.gPOSDateForTransaction + "',strDataPostFlag='N' "
		    + "where strPOSCode='" + newPropertyPOSCode + "' and strClientCode='" + clsGlobalVarClass.gClientCode + "' and strTransactionName='UnsettleBill' ");
	    //ComplementaryBill
	    clsGlobalVarClass.dbMysql.execute("update tblsmssetup "
		    + "set strSendSMSYN='" + (chkComplementaryBillSMSYN.isSelected() ? 'Y' : 'N') + "',longMobileNo='" + txtSMSMobileNo.getText() + "',strUserEdited='" + clsGlobalVarClass.gUserCode + "',dteDateEdited='" + clsGlobalVarClass.gPOSDateForTransaction + "',strDataPostFlag='N' "
		    + "where strPOSCode='" + newPropertyPOSCode + "' and strClientCode='" + clsGlobalVarClass.gClientCode + "' and strTransactionName='ComplementaryBill' ");
	}
	else
	{
	    String sql = "insert into tblsmssetup(strPOSCode,strTransactionName,strSendSMSYN,longMobileNo,strUserCreated"
		    + ",strUserEdited,dteDateCreated,dteDateEdited,strClientCode,strDataPostFlag)"
		    + "values "
		    + "('" + newPropertyPOSCode + "','DayEnd','" + (chkDayEndSMSYN.isSelected() ? 'Y' : 'N') + "','" + txtSMSMobileNo.getText() + "','" + clsGlobalVarClass.gUserCode + "' "
		    + ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gPOSDateForTransaction + "','" + clsGlobalVarClass.gPOSDateForTransaction + "','" + clsGlobalVarClass.gClientCode + "','N' )"
		    + ",('" + newPropertyPOSCode + "','VoidKOT','" + (chkVoidKOTSMSYN.isSelected() ? 'Y' : 'N') + "','" + txtSMSMobileNo.getText() + "','" + clsGlobalVarClass.gUserCode + "' "
		    + ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gPOSDateForTransaction + "','" + clsGlobalVarClass.gPOSDateForTransaction + "','" + clsGlobalVarClass.gClientCode + "','N' )"
		    + ",('" + newPropertyPOSCode + "','NCKOT','" + (chkNCKOTSMSYN.isSelected() ? 'Y' : 'N') + "','" + txtSMSMobileNo.getText() + "','" + clsGlobalVarClass.gUserCode + "' "
		    + ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gPOSDateForTransaction + "','" + clsGlobalVarClass.gPOSDateForTransaction + "','" + clsGlobalVarClass.gClientCode + "','N' )"
		    + ",('" + newPropertyPOSCode + "','VoidAdvOrder','" + (chkVoidAdvOrderSMSYN.isSelected() ? 'Y' : 'N') + "','" + txtSMSMobileNo.getText() + "','" + clsGlobalVarClass.gUserCode + "' "
		    + ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gPOSDateForTransaction + "','" + clsGlobalVarClass.gPOSDateForTransaction + "','" + clsGlobalVarClass.gClientCode + "','N' )"
		    + ",('" + newPropertyPOSCode + "','VoidBill','" + (chkVoidBillSMSYN.isSelected() ? 'Y' : 'N') + "','" + txtSMSMobileNo.getText() + "','" + clsGlobalVarClass.gUserCode + "' "
		    + ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gPOSDateForTransaction + "','" + clsGlobalVarClass.gPOSDateForTransaction + "','" + clsGlobalVarClass.gClientCode + "','N' )"
		    + ",('" + newPropertyPOSCode + "','ModifyBill','" + (chkModifyBillSMSYN.isSelected() ? 'Y' : 'N') + "','" + txtSMSMobileNo.getText() + "','" + clsGlobalVarClass.gUserCode + "' "
		    + ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gPOSDateForTransaction + "','" + clsGlobalVarClass.gPOSDateForTransaction + "','" + clsGlobalVarClass.gClientCode + "','N' )"
		    + ",('" + newPropertyPOSCode + "','UnsettleBill','" + (chkUnsettleBillSMSYN.isSelected() ? 'Y' : 'N') + "','" + txtSMSMobileNo.getText() + "','" + clsGlobalVarClass.gUserCode + "' "
		    + ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gPOSDateForTransaction + "','" + clsGlobalVarClass.gPOSDateForTransaction + "','" + clsGlobalVarClass.gClientCode + "','N' )"
		    + ",('" + newPropertyPOSCode + "','ComplementaryBill','" + (chkComplementaryBillSMSYN.isSelected() ? 'Y' : 'N') + "','" + txtSMSMobileNo.getText() + "','" + clsGlobalVarClass.gUserCode + "' "
		    + ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gPOSDateForTransaction + "','" + clsGlobalVarClass.gPOSDateForTransaction + "','" + clsGlobalVarClass.gClientCode + "','N' ) ";
	    clsGlobalVarClass.dbMysql.execute(sql);
	}
	rsSMSSetup.close();
    }

    private void funFillSMSSetupData()
    {
	try
	{
	    boolean isSMSSetup = false;

	    ResultSet rsSMSSetup = clsGlobalVarClass.dbMysql.executeResultSet("select strTransactionName,strSendSMSYN,longMobileNo "
		    + "from tblsmssetup where strPOSCode='" + newPropertyPOSCode + "' and strClientCode='" + clsGlobalVarClass.gClientCode + "' ");
	    while (rsSMSSetup.next())
	    {
		isSMSSetup = true;

		String transName = rsSMSSetup.getString(1);
		String sendSMSYN = rsSMSSetup.getString(2);
		String mobileNo = rsSMSSetup.getString(3);
		txtSMSMobileNo.setText(mobileNo);

		switch (transName)
		{
		    case "DayEnd":
			if (sendSMSYN.equalsIgnoreCase("Y"))
			{
			    chkDayEndSMSYN.setSelected(true);
			}
			else
			{
			    chkDayEndSMSYN.setSelected(false);
			}
			break;
		    case "VoidKOT":
			if (sendSMSYN.equalsIgnoreCase("Y"))
			{
			    chkVoidKOTSMSYN.setSelected(true);
			}
			else
			{
			    chkVoidKOTSMSYN.setSelected(false);
			}
			break;

		    case "NCKOT":
			if (sendSMSYN.equalsIgnoreCase("Y"))
			{
			    chkNCKOTSMSYN.setSelected(true);
			}
			else
			{
			    chkNCKOTSMSYN.setSelected(false);
			}
			break;

		    case "VoidAdvOrder":
			if (sendSMSYN.equalsIgnoreCase("Y"))
			{
			    chkVoidAdvOrderSMSYN.setSelected(true);
			}
			else
			{
			    chkVoidAdvOrderSMSYN.setSelected(false);
			}
			break;

		    case "VoidBill":
			if (sendSMSYN.equalsIgnoreCase("Y"))
			{
			    chkVoidBillSMSYN.setSelected(true);
			}
			else
			{
			    chkVoidBillSMSYN.setSelected(false);
			}
			break;
		    case "ModifyBill":
			if (sendSMSYN.equalsIgnoreCase("Y"))
			{
			    chkModifyBillSMSYN.setSelected(true);
			}
			else
			{
			    chkModifyBillSMSYN.setSelected(false);
			}
			break;

		    case "UnsettleBill":
			if (sendSMSYN.equalsIgnoreCase("Y"))
			{
			    chkUnsettleBillSMSYN.setSelected(true);
			}
			else
			{
			    chkUnsettleBillSMSYN.setSelected(false);
			}
			break;

		    case "ComplementaryBill":
			if (sendSMSYN.equalsIgnoreCase("Y"))
			{
			    chkComplementaryBillSMSYN.setSelected(true);
			}
			else
			{
			    chkComplementaryBillSMSYN.setSelected(false);
			}
			break;
		}
	    }
	    rsSMSSetup.close();

	    if (!isSMSSetup)
	    {
		chkDayEndSMSYN.setSelected(false);
		chkVoidKOTSMSYN.setSelected(false);
		chkNCKOTSMSYN.setSelected(false);
		chkVoidAdvOrderSMSYN.setSelected(false);
		chkVoidBillSMSYN.setSelected(false);
		chkModifyBillSMSYN.setSelected(false);
		chkUnsettleBillSMSYN.setSelected(false);
		chkComplementaryBillSMSYN.setSelected(false);

	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funSMSTypeSelect()
    {
	if (cmbSMSType.getSelectedItem().toString().equalsIgnoreCase("Sanguine"))
	{
	    clsGlobalVarClass.gSMSType = "Sanguine";
	    txtAreaSMSApi.setText("http://login.bulksmsservice.net.in/api/mt/SendSMS?user=<USERNAME>&password=<PASSWORD>&senderid=<SENDERID>&channel=TRANS&DCS=0&flashsms=0&number=<PHONE>&text=<MSG>&route=1");
	}
	else if (cmbSMSType.getSelectedItem().toString().equalsIgnoreCase("INFYFLYER"))
	{
	    clsGlobalVarClass.gSMSType = "INFYFLYER";
	    txtAreaSMSApi.setText("http://sms.infiflyer.co.in/httpapi/httpapi?token=a10bad827db08a4eeec726da63813747&sender=IPREMS&number=<PHONE>&route=2&type=1&sms=<MSG>");
	}
	else
	{
	    clsGlobalVarClass.gSMSType = "";
	    txtAreaSMSApi.setText("");
	}
	clsGlobalVarClass.gSMSApi = txtAreaSMSApi.getText();

    }
}
