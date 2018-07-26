package com.POSGlobal.controller;

import static com.POSGlobal.controller.clsGlobalVarClass.dbMysql;
import static com.POSGlobal.controller.clsGlobalVarClass.gClientCode;
import static com.POSGlobal.controller.clsGlobalVarClass.gHOPOSType;
import static com.POSGlobal.controller.clsGlobalVarClass.gLastModifiedDate;
import static com.POSGlobal.controller.clsGlobalVarClass.gPropertyCode;
import static com.POSGlobal.controller.clsGlobalVarClass.gSanguineWebServiceURL;
import com.POSGlobal.view.frmOkPopUp;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class clsSynchronizePOSDataToHO
{

    private String fromDate, toDate;
    private clsUtility objUtility;

    clsSynchronizePOSDataToHO()
    {
	fromDate = "";
	toDate = "";
	objUtility = new clsUtility();

    }

    public int funPostSaleDataToHO(String formName)
    {
	try
	{
	    funPostBillHdData(formName);
	    funPostBillDtlData(formName);
	    funPostBillSettlementDtlData(formName);
	    funPostBillModifierDtlData(formName);
	    funPostBillTaxDtlData(formName);
	    funPostBillPromotionDtlData(formName);
	    funPostBillDiscountDtlData(formName);
	    funPostBillCRMPointsData(formName);
	    funPostBillComplimentryDtlData(formName);
	    funPostBillSeriesDtlData(formName);
	    funPostAdvOrderHdData(formName);
	    funPostAdvOrderDtlData(formName);
	    funPostAdvOrderCharDtlData(formName);
	    funPostAdvOrderModifierDtlData(formName);
	    funPostAdvReceiptHdData(formName);
	    funPostAdvReceiptDtlData(formName);
	    funPostHomeDeliveryData(formName);
	    funPostHomeDeliveryDtlData(formName);
	    //funPostPlaceOrderHdData(formName);
	    //funPostPlaceOrderDtlData(formName);
	    //funPostPlaceAdvanceOrderDtlData(formName);
	}
	catch (Exception e)
	{
	    System.out.println(e.getMessage());
	    if (e.getMessage().equals("Connection refused: connect"))
	    {
		clsGlobalVarClass.gConnectionActive = "N";
		JOptionPane.showMessageDialog(null, "Connection is lost to HO please check!!!");
	    }
	    else
	    {
		JOptionPane.showMessageDialog(null, "Error while posting data to HO!!!");
	    }
	    e.printStackTrace();
	}
	return 1;
    }

    public int funPostAdvOrderDataToHO(String formName)
    {
	try
	{
	    funPostAdvOrderHdData(formName);
	    funPostAdvOrderDtlData(formName);
	    funPostAdvOrderCharDtlData(formName);
	    funPostAdvOrderModifierDtlData(formName);
	    funPostAdvReceiptHdData(formName);
	    funPostAdvReceiptDtlData(formName);
	    funPostHomeDeliveryData(formName);
	    funPostHomeDeliveryDtlData(formName);
	}
	catch (Exception e)
	{
	    System.out.println(e.getMessage());
	    if (e.getMessage().equals("Connection refused: connect"))
	    {
		clsGlobalVarClass.gConnectionActive = "N";
		JOptionPane.showMessageDialog(null, "Connection is lost to HO please check!!!");
	    }
	    else
	    {
		JOptionPane.showMessageDialog(null, "Error while posting data to HO!!!");
	    }
	    e.printStackTrace();
	}
	return 1;
    }

    public int funPostSalesDataToHOInBulk(String formName)
    {
	try
	{
	    funPostSalesDataPart1();
	    funPostBillSeriesDtlData(formName);
	    funPostAdvOrderHdData(formName);
	    funPostAdvOrderDtlData(formName);
	    funPostAdvOrderCharDtlData(formName);
	    funPostAdvOrderModifierDtlData(formName);
	    funPostAdvReceiptHdData(formName);
	    funPostAdvReceiptDtlData(formName);
	    funPostHomeDeliveryData(formName);
	    funPostHomeDeliveryDtlData(formName);
	}
	catch (Exception e)
	{
	    System.out.println(e.getMessage());
	    if (e.getMessage().equals("Connection refused: connect"))
	    {
		clsGlobalVarClass.gConnectionActive = "N";
		JOptionPane.showMessageDialog(null, "Connection is lost to HO please check!!!");
	    }
	    else
	    {
		JOptionPane.showMessageDialog(null, "Error while posting data to HO!!!");
	    }
	    e.printStackTrace();
	}
	return 1;
    }

    public int funPostSalesDataPart1() throws Exception
    {
	boolean flgDataForPosting = false;
	String query = "select * from tblbillhd where strDataPostFlag='N' "
		+ "and strBillNo IN(select strBillNo from tblbillsettlementdtl) limit 2000";

	ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	JSONObject objJson = new JSONObject();
	JSONArray arrObjBillHd = new JSONArray();
	String updateBills = "";

	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateBills += ",'" + rs.getString("strBillNo") + "'";

	    objRows.put("BillNo", rs.getString("strBillNo"));
	    objRows.put("AdvBookingNo", rs.getString("strAdvBookingNo"));
	    objRows.put("BillDate", rs.getString("dteBillDate"));
	    objRows.put("POSCode", rs.getString("strPOSCode"));
	    objRows.put("SettelmentMode", rs.getString("strSettelmentMode"));
	    objRows.put("DiscountAmt", rs.getString("dblDiscountAmt"));
	    objRows.put("DiscountPer", rs.getString("dblDiscountPer"));
	    objRows.put("TaxAmt", rs.getString("dblTaxAmt"));
	    objRows.put("SubTotal", rs.getString("dblSubTotal"));
	    objRows.put("GrandTotal", rs.getString("dblGrandTotal"));
	    objRows.put("TakeAway", rs.getString("strTakeAway"));

	    objRows.put("OperationType", rs.getString("strOperationType"));
	    objRows.put("UserCreated", rs.getString("strUserCreated"));
	    objRows.put("UserEdited", rs.getString("strUserEdited"));
	    objRows.put("DateCreated", rs.getString("dteDateCreated"));
	    objRows.put("DateEdited", rs.getString("dteDateEdited"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));

	    objRows.put("TableNo", rs.getString("strTableNo"));
	    objRows.put("WaiterNo", rs.getString("strWaiterNo"));
	    objRows.put("CustomerCode", rs.getString("strCustomerCode"));
	    objRows.put("ManualBillNo", rs.getString("strManualBillNo"));
	    objRows.put("ShiftCode", rs.getString("intShiftCode"));
	    objRows.put("PaxNo", rs.getString("intPaxNo"));

	    objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));
	    objRows.put("ReasonCode", rs.getString("strReasonCode"));
	    objRows.put("Remarks", rs.getString("strRemarks"));
	    objRows.put("TipAmount", rs.getString("dblTipAmount"));
	    objRows.put("SettleDate", rs.getString("dteSettleDate"));
	    objRows.put("CounterCode", rs.getString("strCounterCode"));
	    objRows.put("DeliveryCharges", rs.getString("dblDeliveryCharges"));
	    objRows.put("CouponCode", rs.getString("strCouponCode"));
	    objRows.put("AreaCode", rs.getString("strAreaCode"));
	    objRows.put("DiscountRemark", rs.getString("strDiscountRemark"));
	    objRows.put("TakeAwayRemark", rs.getString("strTakeAwayRemarks"));
	    objRows.put("DiscountOn", rs.getString("strDiscountOn"));
	    objRows.put("CardNo", rs.getString("strCardNo"));
	    objRows.put("TransType", rs.getString("strTransactionType"));
	    objRows.put("strJioMoneyRRefNo", rs.getString("strJioMoneyRRefNo"));
	    objRows.put("strJioMoneyAuthCode", rs.getString("strJioMoneyAuthCode"));
	    objRows.put("strJioMoneyTxnId", rs.getString("strJioMoneyTxnId"));
	    objRows.put("strJioMoneyTxnDateTime", rs.getString("strJioMoneyTxnDateTime"));
	    objRows.put("strJioMoneyCardNo", rs.getString("strJioMoneyCardNo"));
	    objRows.put("strJioMoneyCardType", rs.getString("strJioMoneyCardType"));

	    objRows.put("dblRoundOff", rs.getString("dblRoundOff"));
	    objRows.put("intBillSeriesPaxNo", rs.getString("intBillSeriesPaxNo"));
	    objRows.put("dtBillDate", rs.getString("dtBillDate"));
	    objRows.put("intOrderNo", rs.getString("intOrderNo"));

	    arrObjBillHd.add(objRows);
	    flgDataForPosting = true;
	}
	rs.close();
	objJson.put("BillHdInfo", arrObjBillHd);

	query = "select * from tblbilldtl where strDataPostFlag='N' "
		+ "and strBillNo IN(select strBillNo from tblbillsettlementdtl) limit 2000";
	rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	JSONArray arrObjBillDtl = new JSONArray();
	String updateBillDtl = "";
	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateBillDtl += ",'" + rs.getString("strBillNo") + "'";

	    objRows.put("ItemCode", rs.getString("strItemCode"));
	    objRows.put("ItemName", rs.getString("strItemName"));
	    objRows.put("BillNo", rs.getString("strBillNo"));
	    objRows.put("AdvBookingNo", rs.getString("strAdvBookingNo"));
	    objRows.put("Rate", rs.getString("dblRate"));
	    objRows.put("Quantity", rs.getString("dblQuantity"));
	    objRows.put("Amount", rs.getString("dblAmount"));
	    objRows.put("TaxAmount", rs.getString("dblTaxAmount"));
	    objRows.put("BillDate", rs.getString("dteBillDate"));
	    objRows.put("KOTNo", rs.getString("strKOTNo"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("CustomerCode", rs.getString("strCustomerCode"));
	    objRows.put("OrderProcessing", rs.getString("tmeOrderProcessing"));
	    objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));
	    objRows.put("MMSDataPostFlag", rs.getString("strMMSDataPostFlag"));
	    objRows.put("ManualKOTNo", rs.getString("strManualKOTNo"));
	    objRows.put("tdhYN", rs.getString("tdhYN"));
	    objRows.put("PromoCode", rs.getString("strPromoCode"));
	    objRows.put("CounterCode", rs.getString("strCounterCode"));
	    objRows.put("WaiterNo", rs.getString("strWaiterNo"));
	    objRows.put("DiscountAmt", rs.getString("dblDiscountAmt"));
	    objRows.put("DiscountPer", rs.getString("dblDiscountPer"));
	    objRows.put("strSequenceNo", rs.getString("strSequenceNo"));
	    objRows.put("dtBillDate", rs.getString("dtBillDate"));
	    objRows.put("tmeOrderPickup", rs.getString("tmeOrderPickup"));

	    //System.out.println("qBill=" + rs.getString("strBillNo"));
	    arrObjBillDtl.add(objRows);
	    flgDataForPosting = true;
	}
	rs.close();
	objJson.put("BillDtlInfo", arrObjBillDtl);

	String updateBillModDtl = "";
	query = "select * from tblbillmodifierdtl where strDataPostFlag='N' "
		+ "and strBillNo IN(select strBillNo from tblbillsettlementdtl)";
	rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	JSONArray arrObjBillModifierDtl = new JSONArray();

	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateBillModDtl += ",'" + rs.getString("strBillNo") + "'";
	    objRows.put("BillNo", rs.getString("strBillNo"));
	    objRows.put("ItemCode", rs.getString("strItemCode"));
	    objRows.put("ModifierCode", rs.getString("strModifierCode"));
	    objRows.put("ModifierName", rs.getString("strModifierName"));
	    objRows.put("Rate", rs.getString("dblRate"));
	    objRows.put("Quantity", rs.getString("dblQuantity"));
	    objRows.put("Amount", rs.getString("dblAmount"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("CustomerCode", rs.getString("strCustomerCode"));
	    objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));
	    objRows.put("MMSDataPostFlag", rs.getString("strMMSDataPostFlag"));
	    objRows.put("DefaultModifierDeselectedYN", rs.getString("strDefaultModifierDeselectedYN"));
	    objRows.put("strSequenceNo", rs.getString("strSequenceNo"));
	    objRows.put("dblDiscPer", rs.getString("dblDiscPer"));
	    objRows.put("dblDiscAmt", rs.getString("dblDiscAmt"));
	    objRows.put("dteBillDate", rs.getString("dteBillDate"));

	    arrObjBillModifierDtl.add(objRows);
	    flgDataForPosting = true;
	}
	rs.close();
	objJson.put("BillModifierDtl", arrObjBillModifierDtl);

	query = "select * from tblbilldiscdtl where strDataPostFlag='N' "
		+ "and strBillNo IN(select strBillNo from tblbillsettlementdtl)";
	rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	JSONArray arrObjBillDiscDtl = new JSONArray();

	String updateBillDiscDtl = "";
	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateBillDiscDtl += ",'" + rs.getString("strBillNo") + "'";
	    objRows.put("BillNo", rs.getString("strBillNo"));
	    objRows.put("POSCode", rs.getString("strPOSCode"));
	    objRows.put("DiscAmt", rs.getString("dblDiscAmt"));
	    objRows.put("DiscPer", rs.getString("dblDiscPer"));
	    objRows.put("DiscOnAmt", rs.getString("dblDiscOnAmt"));
	    objRows.put("DiscOnType", rs.getString("strDiscOnType"));
	    objRows.put("DiscOnValue", rs.getString("strDiscOnValue"));
	    objRows.put("DiscOnReasonCode", rs.getString("strDiscReasonCode"));
	    objRows.put("DiscRemarks", rs.getString("strDiscRemarks"));
	    objRows.put("UserCreated", rs.getString("strUserCreated"));
	    objRows.put("UserEdited", rs.getString("strUserEdited"));
	    objRows.put("DateCreated", rs.getString("dteDateCreated"));
	    objRows.put("DateEdited", rs.getString("dteDateEdited"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("dteBillDate", rs.getString("dteBillDate"));

	    arrObjBillDiscDtl.add(objRows);
	    flgDataForPosting = true;
	}
	rs.close();
	objJson.put("BillDiscountDtl", arrObjBillDiscDtl);

	String updateBillTaxDtl = "";
	query = "select * from tblbilltaxdtl where strDataPostFlag='N' "
		+ "and strBillNo IN(select strBillNo from tblbillsettlementdtl)";
	rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	JSONArray arrObjBillTaxDtl = new JSONArray();
	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateBillTaxDtl += ",'" + rs.getString("strBillNo") + "'";

	    objRows.put("BillNo", rs.getString("strBillNo"));
	    objRows.put("TaxCode", rs.getString("strTaxCode"));
	    objRows.put("TaxableAmount", rs.getString("dblTaxableAmount"));
	    objRows.put("TaxAmount", rs.getString("dblTaxAmount"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));
	    objRows.put("dteBillDate", rs.getString("dteBillDate"));

	    arrObjBillTaxDtl.add(objRows);
	    flgDataForPosting = true;
	}
	rs.close();
	objJson.put("BillTaxDtl", arrObjBillTaxDtl);

	String updateBillComplDtl = "";
	query = "select * from tblbillcomplementrydtl "
		+ "where strDataPostFlag='N' and strBillNo IN(select strBillNo from tblbillsettlementdtl)";
	rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	JSONArray arrObjBillComplDtl = new JSONArray();
	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateBillComplDtl += ",'" + rs.getString("strBillNo") + "'";
	    objRows.put("ItemCode", rs.getString("strItemCode"));
	    objRows.put("ItemName", rs.getString("strItemName"));
	    objRows.put("BillNo", rs.getString("strBillNo"));
	    objRows.put("AdvBookingNo", rs.getString("strAdvBookingNo"));
	    objRows.put("Rate", rs.getString("dblRate"));
	    objRows.put("Quantity", rs.getString("dblQuantity"));
	    objRows.put("Amount", rs.getString("dblAmount"));
	    objRows.put("TaxAmount", rs.getString("dblTaxAmount"));
	    objRows.put("BillDate", rs.getString("dteBillDate"));
	    objRows.put("KOTNo", rs.getString("strKOTNo"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("CustomerCode", rs.getString("strCustomerCode"));
	    objRows.put("OrderProcessing", rs.getString("tmeOrderProcessing"));
	    objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));
	    objRows.put("MMSDataPostFlag", rs.getString("strMMSDataPostFlag"));
	    objRows.put("ManualKOTNo", rs.getString("strManualKOTNo"));
	    objRows.put("tdhYN", rs.getString("tdhYN"));
	    objRows.put("PromoCode", rs.getString("strPromoCode"));
	    objRows.put("CounterCode", rs.getString("strCounterCode"));
	    objRows.put("WaiterNo", rs.getString("strWaiterNo"));
	    objRows.put("DiscountAmt", rs.getString("dblDiscountAmt"));
	    objRows.put("DiscountPer", rs.getString("dblDiscountPer"));

	    objRows.put("strSequenceNo", rs.getString("strSequenceNo"));
	    objRows.put("strType", rs.getString("strType"));
	    objRows.put("dtBillDate", rs.getString("dtBillDate"));
	    objRows.put("tmeOrderPickup", rs.getString("tmeOrderPickup"));

	    arrObjBillComplDtl.add(objRows);
	    flgDataForPosting = true;
	}
	rs.close();
	objJson.put("BillComplimentryDtl", arrObjBillComplDtl);

	query = "select * from tblbillsettlementdtl where strDataPostFlag='N' limit 2000 ";
	rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	JSONArray arrObjBillSettleDtl = new JSONArray();
	String updateBillSettlementDtl = "";

	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateBillSettlementDtl += ",'" + rs.getString("strBillNo") + "'";
	    objRows.put("BillNo", rs.getString("strBillNo"));
	    objRows.put("SettlementCode", rs.getString("strSettlementCode"));
	    objRows.put("SettlementAmt", rs.getString("dblSettlementAmt"));
	    objRows.put("PaidAmt", rs.getString("dblPaidAmt"));
	    objRows.put("ExpiryDate", rs.getString("strExpiryDate"));
	    objRows.put("CardName", rs.getString("strCardName"));
	    objRows.put("Remark", rs.getString("strRemark"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("CustomerCode", rs.getString("strCustomerCode"));
	    objRows.put("ActualAmt", rs.getString("dblActualAmt"));
	    objRows.put("RefundAmt", rs.getString("dblRefundAmt"));
	    objRows.put("GiftVoucherCode", rs.getString("strGiftVoucherCode"));
	    objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));
	    objRows.put("FolioNo", rs.getString("strFolioNo"));
	    objRows.put("RoomNo", rs.getString("strRoomNo"));
	    objRows.put("dteBillDate", rs.getString("dteBillDate"));

	    arrObjBillSettleDtl.add(objRows);
	    flgDataForPosting = true;
	}
	rs.close();
	objJson.put("BillSettlementDtl", arrObjBillSettleDtl);

	if (flgDataForPosting)
	{
	    String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostSalesDataToHOPOS";

	    URL url = new URL(hoURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    OutputStream os = conn.getOutputStream();
	    os.write(objJson.toString().getBytes());
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
	    System.out.println("BillData=" + op);
	    conn.disconnect();

	    String[] arrSalesTable = op.split(",");
	    Map<String, String> hm = new HashMap<String, String>();
	    for (int cn = 0; cn < arrSalesTable.length; cn++)
	    {
		if (!arrSalesTable[cn].trim().isEmpty())
		{
		    hm.put(arrSalesTable[cn].trim(), arrSalesTable[cn].trim());
		}
	    }

	    if (hm.size() > 0)
	    {
		StringBuilder sbUpdate = new StringBuilder(updateBills);
		if (updateBills.length() > 0)
		{
		    if (hm.containsKey("BillHd"))
		    {
			updateBills = sbUpdate.delete(0, 1).toString();
			//System.out.println("Billsss="+updateBills);
			dbMysql.execute("update tblbillhd set strDataPostFlag='Y' where strBillNo in (" + updateBills + ")");
		    }
		}
		sbUpdate = new StringBuilder(updateBillDtl);
		if (updateBillDtl.length() > 0)
		{
		    if (hm.containsKey("BillDtl"))
		    {
			updateBillDtl = sbUpdate.delete(0, 1).toString();
			//System.out.println("Billsss="+updateBills);
			dbMysql.execute("update tblbilldtl set strDataPostFlag='Y' where strBillNo in (" + updateBillDtl + ")");
		    }
		}
		sbUpdate = new StringBuilder(updateBillModDtl);
		if (updateBillModDtl.length() > 0)
		{
		    if (hm.containsKey("BillModDtl"))
		    {
			updateBillModDtl = sbUpdate.delete(0, 1).toString();
			//System.out.println("Billsss="+updateBills);
			dbMysql.execute("update tblbillmodifierdtl set strDataPostFlag='Y' where strBillNo in (" + updateBillModDtl + ")");
		    }
		}
		sbUpdate = new StringBuilder(updateBillDiscDtl);
		if (updateBillDiscDtl.length() > 0)
		{
		    if (hm.containsKey("BillDiscDtl"))
		    {
			updateBillDiscDtl = sbUpdate.delete(0, 1).toString();
			//System.out.println("Billsss="+updateBills);
			dbMysql.execute("update tblbilldiscdtl set strDataPostFlag='Y' where strBillNo in (" + updateBillDiscDtl + ")");
		    }
		}
		sbUpdate = new StringBuilder(updateBillTaxDtl);
		if (updateBillTaxDtl.length() > 0)
		{
		    if (hm.containsKey("BillTaxDtl"))
		    {
			updateBillTaxDtl = sbUpdate.delete(0, 1).toString();
			//System.out.println("Billsss="+updateBills);
			dbMysql.execute("update tblbilltaxdtl set strDataPostFlag='Y' where strBillNo in (" + updateBillTaxDtl + ")");
		    }
		}
		sbUpdate = new StringBuilder(updateBillComplDtl);
		if (updateBillComplDtl.length() > 0)
		{
		    if (hm.containsKey("BillComplDtl"))
		    {
			updateBillComplDtl = sbUpdate.delete(0, 1).toString();
			//System.out.println("Billsss="+updateBills);
			dbMysql.execute("update tblbillcomplementrydtl set strDataPostFlag='Y' where strBillNo in (" + updateBillComplDtl + ")");
		    }
		}
		sbUpdate = new StringBuilder(updateBillSettlementDtl);
		if (updateBillSettlementDtl.length() > 0)
		{
		    if (hm.containsKey("BillSettleDtl"))
		    {
			updateBillSettlementDtl = sbUpdate.delete(0, 1).toString();
			//System.out.println("Billsss="+updateBills);
			dbMysql.execute("update tblbillsettlementdtl set strDataPostFlag='Y' where strBillNo in (" + updateBillSettlementDtl + ")");
		    }
		}
	    }
	}

	return 1;
    }

    public int funPostPlaceOrderDataToHO(String formName)
    {
	try
	{
	    funPostPlaceOrderData(formName);
	}
	catch (Exception e)
	{
	    System.out.println(e.getMessage());
	    if (e.getMessage().equals("Connection refused: connect"))
	    {
		clsGlobalVarClass.gConnectionActive = "N";
		JOptionPane.showMessageDialog(null, "Connection is lost to HO please check!!!");
	    }
	    else
	    {
		JOptionPane.showMessageDialog(null, "Error while posting data to HO!!!");
	    }
	    e.printStackTrace();
	}
	return 1;
    }

// Function to post data manually
    public int funPostSalesDataToHOManually(String tableName, String fromDate, String toDate, String formName)
    {
	this.fromDate = fromDate;
	this.toDate = toDate;
	try
	{
	    switch (tableName)
	    {
		case "billhd":
		    funPostBillHdData(formName);
		    break;

		case "billdtl":
		    funPostBillDtlData(formName);
		    break;

		case "billdiscdtl":
		    funPostBillDiscountDtlData(formName);
		    break;

		case "billsettlementdtl":
		    funPostBillSettlementDtlData(formName);
		    break;

		case "billtaxdtl":
		    funPostBillTaxDtlData(formName);
		    break;

		case "billmodifierdtl":
		    funPostBillModifierDtlData(formName);
		    break;

		case "billpromotiondtl":
		    funPostBillPromotionDtlData(formName);
		    break;

		case "billcomplementrydtl":
		    funPostBillComplimentryDtlData(formName);
		    break;

		case "billcrmpoints":
		    funPostBillCRMPointsData(formName);
		    break;

		case "advorderhd":
		    funPostAdvOrderHdData(formName);
		    break;

		case "advorderdtl":
		    funPostAdvOrderDtlData(formName);
		    break;

		case "advordermodifierdtl":
		    funPostAdvOrderModifierDtlData(formName);
		    break;

		case "advorderchardtl":
		    funPostAdvOrderCharDtlData(formName);
		    break;

		case "advreceipthd":
		    funPostAdvReceiptHdData(formName);
		    break;

		case "advreceiptdtl":
		    funPostAdvReceiptDtlData(formName);
		    break;

		case "homedelivery":
		    funPostHomeDeliveryData(formName);
		    break;

		case "homedeliverydtl":
		    funPostHomeDeliveryDtlData(formName);
		    break;

		case "placeOrderHd":
		    funPostPlaceOrderHdData(formName);
		    break;

		case "placeOrderDtl":
		    funPostPlaceOrderDtlData(formName);
		    break;

		case "tbldayendprocess":
		    funPostDayEndDtlData(formName);
		    break;

		case "Credit Bill Receipts":
		    funPostCreditBillReceiptDtlData(formName);
		    break;
	    }
	}
	catch (Exception e)
	{
	    System.out.println(e.getMessage());
	    if (e.getMessage().equals("Connection refused: connect"))
	    {
		JOptionPane.showMessageDialog(null, e.getMessage());
	    }
	    e.printStackTrace();
	}
	return 1;
    }

    private int funPostBillHdData(String formName) throws Exception
    {
	String updateBills = "";
	String query = "", queryForCount = "";

	if (formName.equals("Bill"))
	{
	    query = "select * from tblbillhd where strDataPostFlag='N' "
		    + "and strBillNo IN(select strBillNo from tblbillsettlementdtl) limit 2000";

	    queryForCount = "select count(strBillNo) from tblbillhd where strDataPostFlag='N' "
		    + "and strBillNo IN(select strBillNo from tblbillsettlementdtl) limit 2000";
	}
	else if (formName.equals("Day End"))
	{
	    query = "select * from tblqbillhd where strDataPostFlag='N' limit 2000";

	    queryForCount = "select count(strBillNo) from tblqbillhd where strDataPostFlag='N' limit 2000";
	}
	else if (formName.equals("ManuallyLive"))
	{
	    query = "select * from tblbillhd "
		    + "where date(dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and strDataPostFlag='N' limit 2000";

	    queryForCount = "select count(strBillNo) from tblbillhd "
		    + "where date(dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and strDataPostFlag='N' limit 2000";
	}
	else if (formName.equals("ManuallyQFile"))
	{
	    query = "select * from tblqbillhd "
		    + "where date(dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and strDataPostFlag='N' limit 2000";

	    queryForCount = "select count(strBillNo) from tblqbillhd "
		    + "where date(dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and strDataPostFlag='N' limit 2000";
	}

	int count = 0;
	ResultSet rsCount = clsGlobalVarClass.dbMysql.executeResultSet(queryForCount);
	if (rsCount.next())
	{
	    count = rsCount.getInt(1);
	}
	rsCount.close();

	if (count > 2000)
	{
	    count = count / 2000;
	    count = count + 1;
	}
	else
	{
	    count = 1;
	}
	System.out.println("Bill Hd Count=" + count);

	boolean flgDataPosting = false;
	for (int cnt = 0; cnt < count; cnt++)
	{
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	    JSONObject objJson = new JSONObject();
	    JSONArray arrObj = new JSONArray();
	    updateBills = "";

	    while (rs.next())
	    {
		JSONObject objRows = new JSONObject();
		updateBills += ",'" + rs.getString("strBillNo") + "'";

		objRows.put("BillNo", rs.getString("strBillNo"));
		objRows.put("AdvBookingNo", rs.getString("strAdvBookingNo"));
		objRows.put("BillDate", rs.getString("dteBillDate"));
		objRows.put("POSCode", rs.getString("strPOSCode"));
		objRows.put("SettelmentMode", rs.getString("strSettelmentMode"));
		objRows.put("DiscountAmt", rs.getString("dblDiscountAmt"));
		objRows.put("DiscountPer", rs.getString("dblDiscountPer"));
		objRows.put("TaxAmt", rs.getString("dblTaxAmt"));
		objRows.put("SubTotal", rs.getString("dblSubTotal"));
		objRows.put("GrandTotal", rs.getString("dblGrandTotal"));
		objRows.put("TakeAway", rs.getString("strTakeAway"));

		objRows.put("OperationType", rs.getString("strOperationType"));
		objRows.put("UserCreated", rs.getString("strUserCreated"));
		objRows.put("UserEdited", rs.getString("strUserEdited"));
		objRows.put("DateCreated", rs.getString("dteDateCreated"));
		objRows.put("DateEdited", rs.getString("dteDateEdited"));
		objRows.put("ClientCode", rs.getString("strClientCode"));

		objRows.put("TableNo", rs.getString("strTableNo"));
		objRows.put("WaiterNo", rs.getString("strWaiterNo"));
		objRows.put("CustomerCode", rs.getString("strCustomerCode"));
		objRows.put("ManualBillNo", rs.getString("strManualBillNo"));
		objRows.put("ShiftCode", rs.getString("intShiftCode"));
		objRows.put("PaxNo", rs.getString("intPaxNo"));

		objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));
		objRows.put("ReasonCode", rs.getString("strReasonCode"));
		objRows.put("Remarks", rs.getString("strRemarks"));
		objRows.put("TipAmount", rs.getString("dblTipAmount"));
		objRows.put("SettleDate", rs.getString("dteSettleDate"));
		objRows.put("CounterCode", rs.getString("strCounterCode"));
		objRows.put("DeliveryCharges", rs.getString("dblDeliveryCharges"));
		objRows.put("CouponCode", rs.getString("strCouponCode"));
		objRows.put("AreaCode", rs.getString("strAreaCode"));
		objRows.put("DiscountRemark", rs.getString("strDiscountRemark"));
		objRows.put("TakeAwayRemark", rs.getString("strTakeAwayRemarks"));
		objRows.put("DiscountOn", rs.getString("strDiscountOn"));
		objRows.put("CardNo", rs.getString("strCardNo"));
		objRows.put("TransType", rs.getString("strTransactionType"));
		objRows.put("strJioMoneyRRefNo", rs.getString("strJioMoneyRRefNo"));
		objRows.put("strJioMoneyAuthCode", rs.getString("strJioMoneyAuthCode"));
		objRows.put("strJioMoneyTxnId", rs.getString("strJioMoneyTxnId"));
		objRows.put("strJioMoneyTxnDateTime", rs.getString("strJioMoneyTxnDateTime"));
		objRows.put("strJioMoneyCardNo", rs.getString("strJioMoneyCardNo"));
		objRows.put("strJioMoneyCardType", rs.getString("strJioMoneyCardType"));

		objRows.put("dblRoundOff", rs.getString("dblRoundOff"));
		objRows.put("intBillSeriesPaxNo", rs.getString("intBillSeriesPaxNo"));
		objRows.put("dtBillDate", rs.getString("dtBillDate"));
		objRows.put("intOrderNo", rs.getString("intOrderNo"));

		arrObj.add(objRows);
	    }
	    rs.close();
	    objJson.put("BillHdInfo", arrObj);

	    String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostTransactionDataToHOPOS";

	    URL url = new URL(hoURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    OutputStream os = conn.getOutputStream();
	    os.write(objJson.toString().getBytes());
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
	    System.out.println("BillHd=" + op);
	    conn.disconnect();

	    if (op.equals("true"))
	    {
		StringBuilder sbUpdate = new StringBuilder(updateBills);
		if (updateBills.length() > 0)
		{
		    updateBills = sbUpdate.delete(0, 1).toString();
		    //System.out.println("Billsss="+updateBills);
		    dbMysql.execute("update tblbillhd set strDataPostFlag='Y' where strBillNo in (" + updateBills + ")");
		    dbMysql.execute("update tblqbillhd set strDataPostFlag='Y' where strBillNo in (" + updateBills + ")");
		}
		flgDataPosting = true;
	    }
	    else
	    {
		flgDataPosting = false;
	    }
	}
	if (formName.equals("ManuallyLive") || formName.equals("ManuallyQFile"))
	{
	    if (flgDataPosting)
	    {
		JOptionPane.showMessageDialog(null, "Data Posted Successfully!!!");
	    }
	}

	return 1;
    }

    private int funPostBillDtlData(String formName) throws Exception
    {
	String updateBills = "";
	String query = "", queryForCount = "";

	if (formName.equals("Bill"))
	{
	    query = "select * from tblbilldtl where strDataPostFlag='N' "
		    + "and strBillNo IN (select strBillNo from tblbillsettlementdtl) limit 2000 ";

	    queryForCount = "select count(strBillNo) from tblbilldtl where strDataPostFlag='N' "
		    + "and strBillNo IN (select strBillNo from tblbillsettlementdtl)";
	}
	else if (formName.equals("Day End"))
	{
	    query = "select * from tblqbilldtl where strDataPostFlag='N' limit 2000 ";

	    queryForCount = "select count(strBillNo) from tblqbilldtl where strDataPostFlag='N'";
	}
	else if (formName.equals("ManuallyLive"))
	{
	    query = "select b.* from tblbillhd a,tblbilldtl b "
		    + " where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and b.strDataPostFlag='N' limit 2000 ";

	    queryForCount = "select count(b.strBillNo) from tblbillhd a,tblbilldtl b "
		    + " where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and b.strDataPostFlag='N'";
	}
	else if (formName.equals("ManuallyQFile"))
	{
	    query = "select b.* from tblqbillhd a,tblqbilldtl b "
		    + " where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and b.strDataPostFlag='N' limit 2000 ";

	    queryForCount = "select count(b.strBillNo) from tblqbillhd a,tblqbilldtl b "
		    + " where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and b.strDataPostFlag='N'";
	}
	//System.out.println(query);

	int count = 0;
	ResultSet rsCount = clsGlobalVarClass.dbMysql.executeResultSet(queryForCount);
	if (rsCount.next())
	{
	    count = rsCount.getInt(1);
	}
	rsCount.close();

	if (count > 2000)
	{
	    count = count / 2000;
	    count = count + 1;
	}
	else
	{
	    count = 1;
	}
	System.out.println("Bill Dtl Count=" + count);

	boolean flgDataPosting = false;

	int rowCount = 0;
	for (int cnt = 0; cnt < count; cnt++)
	{
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	    JSONObject objJson = new JSONObject();
	    JSONArray arrObj = new JSONArray();

	    updateBills = "";
	    String updateItemCodes = "";
	    String updateKOTNos = "";

	    while (rs.next())
	    {
		JSONObject objRows = new JSONObject();
		updateBills += ",'" + rs.getString("strBillNo") + "'";
		updateItemCodes += ",'" + rs.getString("strItemCode") + "'";
		updateKOTNos += ",'" + rs.getString("strKOTNo") + "'";

		objRows.put("ItemCode", rs.getString("strItemCode"));
		objRows.put("ItemName", rs.getString("strItemName"));
		objRows.put("BillNo", rs.getString("strBillNo"));
		objRows.put("AdvBookingNo", rs.getString("strAdvBookingNo"));
		objRows.put("Rate", rs.getString("dblRate"));
		objRows.put("Quantity", rs.getString("dblQuantity"));
		objRows.put("Amount", rs.getString("dblAmount"));
		objRows.put("TaxAmount", rs.getString("dblTaxAmount"));
		objRows.put("BillDate", rs.getString("dteBillDate"));
		objRows.put("KOTNo", rs.getString("strKOTNo"));
		objRows.put("ClientCode", rs.getString("strClientCode"));
		objRows.put("CustomerCode", rs.getString("strCustomerCode"));
		objRows.put("OrderProcessing", rs.getString("tmeOrderProcessing"));
		objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));
		objRows.put("MMSDataPostFlag", rs.getString("strMMSDataPostFlag"));
		objRows.put("ManualKOTNo", rs.getString("strManualKOTNo"));
		objRows.put("tdhYN", rs.getString("tdhYN"));
		objRows.put("PromoCode", rs.getString("strPromoCode"));
		objRows.put("CounterCode", rs.getString("strCounterCode"));
		objRows.put("WaiterNo", rs.getString("strWaiterNo"));
		objRows.put("DiscountAmt", rs.getString("dblDiscountAmt"));
		objRows.put("DiscountPer", rs.getString("dblDiscountPer"));

		objRows.put("strSequenceNo", rs.getString("strSequenceNo"));
		objRows.put("dtBillDate", rs.getString("dtBillDate"));
		objRows.put("tmeOrderPickup", rs.getString("tmeOrderPickup"));
		//System.out.println("qBill=" + rs.getString("strBillNo"));
		arrObj
			.add(objRows);

		rowCount++;
	    }
	    rs.close();

	    objJson.put("BillDtlInfo", arrObj);
	    String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostTransactionDataToHOPOS";

	    URL url = new URL(hoURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    OutputStream os = conn.getOutputStream();
	    os.write(objJson.toString().getBytes());
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
	    System.out.println("BillDtl= " + op);
	    conn.disconnect();
	    if (op.equals("true"))
	    {
		StringBuilder sbUpdate = new StringBuilder(updateBills);
		StringBuilder sbUpdateItemCode = new StringBuilder(updateItemCodes);
		StringBuilder sbUpdateKOTNos = new StringBuilder(updateKOTNos);

		if (updateBills.length() > 0)
		{
		    updateBills = sbUpdate.delete(0, 1).toString();
		    updateItemCodes = sbUpdateItemCode.delete(0, 1).toString();
		    updateKOTNos = sbUpdateKOTNos.delete(0, 1).toString();

		    //System.out.println("Billsss="+updateBills);
		    //System.out.println("update tblbilldtl set strDataPostFlag='Y' where strBillNo in (" + updateBills + ") and strItemCode in ("+updateItemCodes+")");
		    //System.out.println("update tblqbilldtl set strDataPostFlag='Y' where strBillNo in (" + updateBills + ") and strItemCode in ("+updateItemCodes+")");
		    String q = "update tblqbilldtl set strDataPostFlag='Y' "
			    + " where strBillNo in (" + updateBills + ") and strItemCode in (" + updateItemCodes + ") "
			    + " and strKOTNo in (" + updateKOTNos + ") ";
		    dbMysql.execute("update tblbilldtl set strDataPostFlag='Y' "
			    + " where strBillNo in (" + updateBills + ") and strItemCode in (" + updateItemCodes + ") "
			    + " and strKOTNo in (" + updateKOTNos + ") limit 2000");
		    dbMysql.execute("update tblqbilldtl set strDataPostFlag='Y' "
			    + " where strBillNo in (" + updateBills + ") and strItemCode in (" + updateItemCodes + ") "
			    + " and strKOTNo in (" + updateKOTNos + ") limit 2000");
		}
		flgDataPosting = true;
	    }
	    else
	    {
		flgDataPosting = false;
	    }

	    System.out.println("Row Count= " + rowCount);
	}

	if (formName.equals("ManuallyLive") || formName.equals("ManuallyQFile"))
	{
	    if (flgDataPosting)
	    {
		JOptionPane.showMessageDialog(null, "Data Posted Successfully!!!");
	    }
	}

	return 1;
    }

    private int funPostBillModifierDtlData(String formName) throws Exception
    {
	String updateBills = "";
	String query = "";

	if (formName.equals("Bill"))
	{
	    query = "select * from tblbillmodifierdtl where strDataPostFlag='N' "
		    + "and strBillNo IN(select strBillNo from tblbillsettlementdtl)";
	}
	else if (formName.equals("Day End"))
	{
	    query = "select * from tblqbillmodifierdtl where strDataPostFlag='N' ";
	}
	else if (formName.equals("ManuallyLive"))
	{
	    query = "select b.* from tblbillhd a,tblbillmodifierdtl b "
		    + " where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and b.strDataPostFlag='N'";
	}
	else if (formName.equals("ManuallyQFile"))
	{
	    query = "select b.* from tblqbillhd a,tblqbillmodifierdtl b "
		    + " where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and b.strDataPostFlag='N'";
	}

	ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	JSONObject objJson = new JSONObject();
	JSONArray arrObj = new JSONArray();

	boolean flgDataForPosting = false;
	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateBills += ",'" + rs.getString("strBillNo") + "'";
	    objRows.put("BillNo", rs.getString("strBillNo"));
	    objRows.put("ItemCode", rs.getString("strItemCode"));
	    objRows.put("ModifierCode", rs.getString("strModifierCode"));
	    objRows.put("ModifierName", rs.getString("strModifierName"));
	    objRows.put("Rate", rs.getString("dblRate"));
	    objRows.put("Quantity", rs.getString("dblQuantity"));
	    objRows.put("Amount", rs.getString("dblAmount"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("CustomerCode", rs.getString("strCustomerCode"));
	    objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));
	    objRows.put("MMSDataPostFlag", rs.getString("strMMSDataPostFlag"));
	    objRows.put("DefaultModifierDeselectedYN", rs.getString("strDefaultModifierDeselectedYN"));
	    objRows.put("strSequenceNo", rs.getString("strSequenceNo"));
	    objRows.put("dblDiscPer", rs.getString("dblDiscPer"));
	    objRows.put("dblDiscAmt", rs.getString("dblDiscAmt"));
	    objRows.put("dteBillDate", rs.getString("dteBillDate"));

	    arrObj.add(objRows);
	    flgDataForPosting = true;
	}
	rs.close();

	if (flgDataForPosting)
	{
	    objJson.put("BillModifierDtl", arrObj);
	    String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostTransactionDataToHOPOS";

	    URL url = new URL(hoURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    OutputStream os = conn.getOutputStream();
	    os.write(objJson.toString().getBytes());
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
	    System.out.println("BillMod= " + op);
	    conn.disconnect();
	    if (op.equals("true"))
	    {
		StringBuilder sbUpdate = new StringBuilder(updateBills);
		if (updateBills.length() > 0)
		{
		    updateBills = sbUpdate.delete(0, 1).toString();
		    //System.out.println("Billsss="+updateBills);
		    dbMysql.execute("update tblbillmodifierdtl set strDataPostFlag='Y' where strBillNo in (" + updateBills + ")");
		    dbMysql.execute("update tblqbillmodifierdtl set strDataPostFlag='Y' where strBillNo in (" + updateBills + ")");
		}
		if (formName.equals("ManuallyLive") || formName.equals("ManuallyQFile"))
		{
		    JOptionPane.showMessageDialog(null, "Data Posted Successfully!!!");
		}
	    }
	}

	return 1;
    }

    private int funPostBillSettlementDtlData(String formName) throws Exception
    {
	String updateBills = "";
	String query = "", queryForCount = "";

	if (formName.equals("Bill"))
	{
	    query = "select * from tblbillsettlementdtl where strDataPostFlag='N' limit 2000 ";

	    queryForCount = "select count(strBillNo) from tblbillsettlementdtl where strDataPostFlag='N' ";
	}
	else if (formName.equals("Day End"))
	{
	    query = "select * from tblqbillsettlementdtl where strDataPostFlag='N' limit 2000 ";

	    queryForCount = "select count(strBillNo) from tblqbillsettlementdtl where strDataPostFlag='N' ";
	}
	else if (formName.equals("ManuallyLive"))
	{
	    query = "select b.* from tblbillhd a,tblbillsettlementdtl b "
		    + " where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and b.strDataPostFlag='N' limit 2000 ";

	    queryForCount = "select count(b.strBillNo) from tblbillhd a,tblbillsettlementdtl b "
		    + " where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and b.strDataPostFlag='N'";
	}
	else if (formName.equals("ManuallyQFile"))
	{
	    query = "select b.* from tblqbillhd a,tblqbillsettlementdtl b "
		    + " where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and b.strDataPostFlag='N' limit 2000 ";

	    queryForCount = "select count(b.strBillNo) from tblqbillhd a,tblqbillsettlementdtl b "
		    + " where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and b.strDataPostFlag='N'";
	}

	int count = 0;
	ResultSet rsCount = clsGlobalVarClass.dbMysql.executeResultSet(queryForCount);
	if (rsCount.next())
	{
	    count = rsCount.getInt(1);
	}
	rsCount.close();

	if (count > 2000)
	{
	    count = count / 2000;
	    count = count + 1;
	}
	else
	{
	    count = 1;
	}
	System.out.println("Bill Sett Dtl Count=" + count);

	boolean flgDataPosting = false;

	for (int cnt = 0; cnt < count; cnt++)
	{
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	    JSONObject objJson = new JSONObject();
	    JSONArray arrObj = new JSONArray();

	    updateBills = "";
	    String updateSettlementCodes = "";

	    while (rs.next())
	    {
		JSONObject objRows = new JSONObject();
		updateBills += ",'" + rs.getString("strBillNo") + "'";
		updateSettlementCodes += ",'" + rs.getString("strSettlementCode") + "'";
		objRows.put("BillNo", rs.getString("strBillNo"));
		objRows.put("SettlementCode", rs.getString("strSettlementCode"));
		objRows.put("SettlementAmt", rs.getString("dblSettlementAmt"));
		objRows.put("PaidAmt", rs.getString("dblPaidAmt"));
		objRows.put("ExpiryDate", rs.getString("strExpiryDate"));
		objRows.put("CardName", rs.getString("strCardName"));
		objRows.put("Remark", rs.getString("strRemark"));
		objRows.put("ClientCode", rs.getString("strClientCode"));
		objRows.put("CustomerCode", rs.getString("strCustomerCode"));
		objRows.put("ActualAmt", rs.getString("dblActualAmt"));
		objRows.put("RefundAmt", rs.getString("dblRefundAmt"));
		objRows.put("GiftVoucherCode", rs.getString("strGiftVoucherCode"));
		objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));
		objRows.put("FolioNo", rs.getString("strFolioNo"));
		objRows.put("RoomNo", rs.getString("strRoomNo"));
		objRows.put("dteBillDate", rs.getString("dteBillDate"));

		arrObj.add(objRows);
	    }
	    rs.close();

	    objJson.put("BillSettlementDtl", arrObj);
	    String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostTransactionDataToHOPOS";

	    URL url = new URL(hoURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    OutputStream os = conn.getOutputStream();
	    os.write(objJson.toString().getBytes());
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
	    System.out.println("BillSettle= " + op);
	    conn.disconnect();
	    if (op.equals("true"))
	    {
		StringBuilder sbUpdate = new StringBuilder(updateBills);
		StringBuilder sbUpdateSettlements = new StringBuilder(updateSettlementCodes);

		if (updateBills.length() > 0)
		{
		    updateBills = sbUpdate.delete(0, 1).toString();
		    updateSettlementCodes = sbUpdateSettlements.delete(0, 1).toString();
		    //System.out.println("Billsss="+updateBills);

		    dbMysql.execute("update tblbillsettlementdtl set strDataPostFlag='Y' "
			    + " where strBillNo in (" + updateBills + ") and strSettlementCode in (" + updateSettlementCodes + ") ");
		    dbMysql.execute("update tblqbillsettlementdtl set strDataPostFlag='Y' "
			    + " where strBillNo in (" + updateBills + ") and strSettlementCode in (" + updateSettlementCodes + ") ");
		}
		flgDataPosting = true;
	    }
	    else
	    {
		flgDataPosting = true;
	    }
	}

	if (formName.equals("ManuallyLive") || formName.equals("ManuallyQFile"))
	{
	    if (flgDataPosting)
	    {
		JOptionPane.showMessageDialog(null, "Data Posted Successfully!!!");
	    }
	}

	return 1;
    }

    private int funPostBillTaxDtlData(String formName) throws Exception
    {
	String updateBills = "", updateTaxCodes = "";
	String query = "";

	if (formName.equals("Bill"))
	{
	    query = "select * from tblbilltaxdtl where strDataPostFlag='N' "
		    + "and strBillNo IN(select strBillNo from tblbillsettlementdtl)";
	}
	else if (formName.equals("Day End"))
	{
	    query = "select * from tblqbilltaxdtl where strDataPostFlag='N' ";
	}
	else if (formName.equals("ManuallyLive"))
	{
	    query = "select b.* from tblbillhd a,tblbilltaxdtl b "
		    + " where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and b.strDataPostFlag='N'";
	}
	else if (formName.equals("ManuallyQFile"))
	{
	    query = "select b.* from tblqbillhd a,tblqbilltaxdtl b "
		    + " where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and b.strDataPostFlag='N'";
	}
	ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	JSONObject objJson = new JSONObject();
	JSONArray arrObj = new JSONArray();

	boolean flgDataForPosting = false;
	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateBills += ",'" + rs.getString("strBillNo") + "'";
	    updateTaxCodes += ",'" + rs.getString("strTaxCode") + "'";

	    objRows.put("BillNo", rs.getString("strBillNo"));
	    objRows.put("TaxCode", rs.getString("strTaxCode"));
	    objRows.put("TaxableAmount", rs.getString("dblTaxableAmount"));
	    objRows.put("TaxAmount", rs.getString("dblTaxAmount"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));
	    objRows.put("dteBillDate", rs.getString("dteBillDate"));
	    arrObj.add(objRows);
	    flgDataForPosting = true;
	}
	rs.close();

	if (flgDataForPosting)
	{
	    objJson.put("BillTaxDtl", arrObj);
	    String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostTransactionDataToHOPOS";

	    URL url = new URL(hoURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    OutputStream os = conn.getOutputStream();
	    os.write(objJson.toString().getBytes());
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
	    System.out.println("BillTax= " + op);
	    conn.disconnect();
	    if (op.equals("true"))
	    {
		StringBuilder sbUpdate = new StringBuilder(updateBills);
		StringBuilder sbUpdateTax = new StringBuilder(updateTaxCodes);
		if (updateBills.length() > 0)
		{
		    updateBills = sbUpdate.delete(0, 1).toString();
		    updateTaxCodes = sbUpdateTax.delete(0, 1).toString();
		    //System.out.println("Billsss="+updateBills);
		    dbMysql.execute("update tblbilltaxdtl set strDataPostFlag='Y' "
			    + " where strBillNo in (" + updateBills + ") and strTaxCode in (" + updateTaxCodes + ")");
		    dbMysql.execute("update tblqbilltaxdtl set strDataPostFlag='Y' "
			    + " where strBillNo in (" + updateBills + ") and strTaxCode in (" + updateTaxCodes + ")");
		}
		if (formName.equals("ManuallyLive") || formName.equals("ManuallyQFile"))
		{
		    JOptionPane.showMessageDialog(null, "Data Posted Successfully!!!");
		}
	    }
	}

	return 1;
    }

    private int funPostBillDiscountDtlData(String formName) throws Exception
    {
	String updateBills = "";
	String query = "", queryForCount = "";

	if (formName.equals("Bill"))
	{
	    query = "select * from tblbilldiscdtl where strDataPostFlag='N' limit 2000 ";

	    queryForCount = "select count(strBillNo) from tblbilldiscdtl where strDataPostFlag='N' ";
	}
	else if (formName.equals("Day End"))
	{
	    query = "select * from tblqbilldiscdtl where strDataPostFlag='N' limit 2000 ";

	    queryForCount = "select count(strBillNo) from tblqbilldiscdtl where strDataPostFlag='N' ";
	}
	else if (formName.equals("ManuallyLive"))
	{
	    query = "select b.* from tblbillhd a,tblbilldiscdtl b "
		    + " where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and b.strDataPostFlag='N' limit 2000 ";

	    queryForCount = "select count(b.strBillNo) from tblbillhd a,tblbilldiscdtl b "
		    + " where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and b.strDataPostFlag='N'";
	}
	else if (formName.equals("ManuallyQFile"))
	{
	    query = "select b.* from tblqbillhd a,tblqbilldiscdtl b "
		    + " where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and b.strDataPostFlag='N' limit 2000 ";

	    queryForCount = "select count(b.strBillNo) from tblqbillhd a,tblqbilldiscdtl b "
		    + " where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and b.strDataPostFlag='N'";
	}

	int count = 0;
	ResultSet rsCount = clsGlobalVarClass.dbMysql.executeResultSet(queryForCount);
	if (rsCount.next())
	{
	    count = rsCount.getInt(1);
	}
	rsCount.close();

	if (count > 2000)
	{
	    count = count / 2000;
	    count = count + 1;
	}
	else
	{
	    count = 1;
	}
	System.out.println("Bill Disc Dtl Count=" + count);
	boolean flgDataPosting = false;

	for (int cnt = 0; cnt < count; cnt++)
	{
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	    JSONObject objJson = new JSONObject();
	    JSONArray arrObj = new JSONArray();

	    updateBills = "";
	    while (rs.next())
	    {
		JSONObject objRows = new JSONObject();
		updateBills += ",'" + rs.getString("strBillNo") + "'";
		objRows.put("BillNo", rs.getString("strBillNo"));
		objRows.put("POSCode", rs.getString("strPOSCode"));
		objRows.put("DiscAmt", rs.getString("dblDiscAmt"));
		objRows.put("DiscPer", rs.getString("dblDiscPer"));
		objRows.put("DiscOnAmt", rs.getString("dblDiscOnAmt"));
		objRows.put("DiscOnType", rs.getString("strDiscOnType"));
		objRows.put("DiscOnValue", rs.getString("strDiscOnValue"));
		objRows.put("DiscOnReasonCode", rs.getString("strDiscReasonCode"));
		objRows.put("DiscRemarks", rs.getString("strDiscRemarks"));
		objRows.put("UserCreated", rs.getString("strUserCreated"));
		objRows.put("UserEdited", rs.getString("strUserEdited"));
		objRows.put("DateCreated", rs.getString("dteDateCreated"));
		objRows.put("DateEdited", rs.getString("dteDateEdited"));
		objRows.put("ClientCode", rs.getString("strClientCode"));
		objRows.put("dteBillDate", rs.getString("dteBillDate"));

		arrObj.add(objRows);
	    }
	    rs.close();

	    objJson.put("BillDiscountDtl", arrObj);
	    String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostTransactionDataToHOPOS";

	    URL url = new URL(hoURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    OutputStream os = conn.getOutputStream();
	    os.write(objJson.toString().getBytes());
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
	    System.out.println("BillDisc= " + op);
	    conn.disconnect();
	    if (op.equals("true"))
	    {
		StringBuilder sbUpdate = new StringBuilder(updateBills);
		if (updateBills.length() > 0)
		{
		    updateBills = sbUpdate.delete(0, 1).toString();
		    //System.out.println("Billsss="+updateBills);
		    dbMysql.execute("update tblbilldiscdtl set strDataPostFlag='Y' where strBillNo in (" + updateBills + ")");
		    dbMysql.execute("update tblqbilldiscdtl set strDataPostFlag='Y' where strBillNo in (" + updateBills + ")");
		}
		flgDataPosting = true;
	    }
	    else
	    {
		flgDataPosting = true;
	    }
	}

	if (formName.equals("ManuallyLive") || formName.equals("ManuallyQFile"))
	{
	    if (flgDataPosting)
	    {
		JOptionPane.showMessageDialog(null, "Data Posted Successfully!!!");
	    }
	}

	return 1;
    }

    private int funPostBillSeriesDtlData(String formName) throws Exception
    {
	String updateBills = "";
	String query = "", queryForCount = "";

	if (formName.equals("Bill"))
	{
	    query = "select * from tblbillseriesbilldtl where strDataPostFlag='N' "
		    + "and strHdBillNo IN (select strBillNo from tblbillsettlementdtl) limit 2000 ";

	    queryForCount = "select count(strHdBillNo) from tblbillseriesbilldtl where strDataPostFlag='N' "
		    + "and strHdBillNo IN (select strBillNo from tblbillsettlementdtl)";
	}
	else if (formName.equals("Day End"))
	{
	    query = "select * from tblbillseriesbilldtl where strDataPostFlag='N' limit 2000 ";

	    queryForCount = "select count(strHdBillNo) from tblbillseriesbilldtl where strDataPostFlag='N'";
	}
	else if (formName.equals("ManuallyLive"))
	{
	    query = "select b.* from tblbillhd a,tblbillseriesbilldtl b "
		    + " where a.strBillNo=b.strHdBillNo and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and b.strDataPostFlag='N' limit 2000 ";

	    queryForCount = "select count(b.strBillNo) from tblbillhd a,tblbillseriesbilldtl b "
		    + " where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and b.strDataPostFlag='N'";
	}
	else if (formName.equals("ManuallyQFile"))
	{
	    query = "select b.* from tblqbillhd a,tblbillseriesbilldtl b "
		    + " where a.strBillNo=b.strHdBillNo and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and b.strDataPostFlag='N' limit 2000 ";

	    queryForCount = "select count(b.strBillNo) from tblqbillhd a,tblbillseriesbilldtl b "
		    + " where a.strBillNo=b.strHdBillNo and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and b.strDataPostFlag='N'";
	}
	//System.out.println(query);

	int count = 0;
	ResultSet rsCount = clsGlobalVarClass.dbMysql.executeResultSet(queryForCount);
	if (rsCount.next())
	{
	    count = rsCount.getInt(1);
	}
	rsCount.close();

	if (count > 2000)
	{
	    count = count / 2000;
	    count = count + 1;
	}
	else
	{
	    count = 1;
	}
	System.out.println("Bill Series Dtl Count=" + count);
	boolean flgDataPosting = false;
	int rowCount = 0;
	for (int cnt = 0; cnt < count; cnt++)
	{
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	    JSONObject objJson = new JSONObject();
	    JSONArray arrObj = new JSONArray();

	    updateBills = "";

	    while (rs.next())
	    {
		JSONObject objRows = new JSONObject();
		updateBills += ",'" + rs.getString("strHdBillNo") + "'";

		objRows.put("POSCode", rs.getString("strPOSCode"));
		objRows.put("BillSeries", rs.getString("strBillSeries"));
		objRows.put("HdBillNo", rs.getString("strHdBillNo"));
		objRows.put("DtlBillNo", rs.getString("strDtlBillNos"));
		objRows.put("GrandTotal", rs.getString("dblGrandTotal"));
		objRows.put("ClientCode", rs.getString("strClientCode"));
		objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));
		objRows.put("UserCreated", rs.getString("strUserCreated"));
		objRows.put("CreatedDate", rs.getString("dteCreatedDate"));
		objRows.put("UserEdited", rs.getString("strUserEdited"));
		objRows.put("EditedDate", rs.getString("dteEditedDate"));
		objRows.put("dteBillDate", rs.getString("dteBillDate"));

		//System.out.println("qBill=" + rs.getString("strBillNo"));
		arrObj.add(objRows);

		rowCount++;
	    }
	    rs.close();

	    objJson.put("BillSeriesDtlInfo", arrObj);
	    String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostTransactionDataToHOPOS";

	    URL url = new URL(hoURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    OutputStream os = conn.getOutputStream();
	    os.write(objJson.toString().getBytes());
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
	    System.out.println("BillSeriesDtl= " + op);
	    conn.disconnect();
	    if (op.equals("true"))
	    {
		StringBuilder sbUpdate = new StringBuilder(updateBills);

		if (updateBills.length() > 0)
		{
		    updateBills = sbUpdate.delete(0, 1).toString();

		    String qry = "update tblbillseriesbilldtl set strDataPostFlag='Y' where strHdBillNo in (" + updateBills + ") ";
		    dbMysql.execute(qry);

		    //qry="update tblbillseriesbilldtl set strDataPostFlag='Y' where strBillNo in (" + updateBills + ") ";
		    //dbMysql.execute(qry);
		}
		flgDataPosting = true;
	    }
	    else
	    {
		flgDataPosting = false;
	    }

	    System.out.println("Row Count= " + rowCount);
	}

	if (formName.equals("ManuallyLive") || formName.equals("ManuallyQFile"))
	{
	    if (flgDataPosting)
	    {
		JOptionPane.showMessageDialog(null, "Data Posted Successfully!!!");
	    }
	}

	return 1;
    }

    private int funPostBillCRMPointsData(String formName) throws Exception
    {
	String updateBills = "";
	String query = "";

	if (formName.equals("Bill"))
	{
	    query = "select * from tblcrmpoints "
		    + "where strDataPostFlag='N' "
		    + "and strBillNo IN(select strBillNo from tblbillsettlementdtl)";
	}
	else if (formName.equals("Day End"))
	{
	    query = "select * from tblcrmpoints "
		    + "where strDataPostFlag='N' "
		    + "and strBillNo IN(select strBillNo from tblqbillsettlementdtl)";
	}
	else if (formName.equals("ManuallyLive"))
	{
	    query = "select b.* from tblbillhd a,tblcrmpoints b "
		    + " where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and b.strDataPostFlag='N'";
	}
	else if (formName.equals("ManuallyQFile"))
	{
	    query = "select b.* from tblqbillhd a,tblcrmpoints b "
		    + " where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and b.strDataPostFlag='N'";
	}

	ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	JSONObject objJson = new JSONObject();
	JSONArray arrObj = new JSONArray();

	boolean flgDataForPosting = false;
	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateBills += ",'" + rs.getString("strBillNo") + "'";
	    objRows.put("BillNo", rs.getString("strBillNo"));
	    objRows.put("CRMPoints", rs.getString("dblPoints"));
	    objRows.put("TransactionId", rs.getString("strTransactionId"));
	    objRows.put("OutletUID", rs.getString("strOutletUID"));
	    objRows.put("RedeemedAmt", rs.getString("dblRedeemedAmt"));
	    objRows.put("CustomerMobileNo", rs.getString("longCustMobileNo"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));
	    objRows.put("Value", rs.getString("dblValue"));
	    objRows.put("CustomerId", rs.getString("strCustomerId"));
	    objRows.put("BillDate", rs.getString("dteBillDate"));

	    arrObj.add(objRows);
	    flgDataForPosting = true;
	}
	rs.close();

	if (flgDataForPosting)
	{
	    objJson.put("CRMBillPoints", arrObj);
	    String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostTransactionDataToHOPOS";

	    URL url = new URL(hoURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    OutputStream os = conn.getOutputStream();
	    os.write(objJson.toString().getBytes());
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
	    System.out.println("BillCRM= " + op);
	    conn.disconnect();
	    if (op.equals("true"))
	    {
		StringBuilder sbUpdate = new StringBuilder(updateBills);
		if (updateBills.length() > 0)
		{
		    updateBills = sbUpdate.delete(0, 1).toString();
		    //System.out.println("Billsss="+updateBills);
		    dbMysql.execute("update tblcrmpoints set strDataPostFlag='Y' where strBillNo in (" + updateBills + ")");
		}
		if (formName.equals("ManuallyLive") || formName.equals("ManuallyQFile"))
		{
		    JOptionPane.showMessageDialog(null, "Data Posted Successfully!!!");
		}
	    }
	}

	return 1;
    }

    private int funPostBillPromotionDtlData(String formName) throws Exception
    {
	String updateBills = "", updateItemCodes = "", updatePromoCodes = "";
	String query = "";

	if (formName.equals("Bill"))
	{
	    query = "select * from tblbillpromotiondtl "
		    + "where strDataPostFlag='N' "
		    + "and strBillNo IN(select strBillNo from tblbillsettlementdtl)";
	}
	else if (formName.equals("Day End"))
	{
	    query = "select * from tblqbillpromotiondtl "
		    + "where strDataPostFlag='N' "
		    + "and strBillNo IN(select strBillNo from tblqbillsettlementdtl)";
	}
	else if (formName.equals("ManuallyLive"))
	{
	    query = "select b.* from tblbillhd a,tblbillpromotiondtl b "
		    + " where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and b.strDataPostFlag='N'";
	}
	else if (formName.equals("ManuallyQFile"))
	{
	    query = "select b.* from tblqbillhd a,tblqbillpromotiondtl b "
		    + " where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and b.strDataPostFlag='N'";
	}
	System.out.println(query);
	ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	JSONObject objJson = new JSONObject();
	JSONArray arrObj = new JSONArray();

	boolean flgDataForPosting = false;
	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateBills += ",'" + rs.getString("strBillNo") + "'";
	    updateItemCodes += ",'" + rs.getString("strItemCode") + "'";
	    updatePromoCodes += ",'" + rs.getString("strPromotionCode") + "'";

	    objRows.put("BillNo", rs.getString("strBillNo"));
	    objRows.put("ItemCode", rs.getString("strItemCode"));
	    objRows.put("PromotionCode", rs.getString("strPromotionCode"));
	    objRows.put("Quantity", rs.getString("dblQuantity"));
	    objRows.put("Rate", rs.getString("dblRate"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));
	    objRows.put("PromoType", rs.getString("strPromoType"));
	    objRows.put("Amount", rs.getString("dblAmount"));
	    objRows.put("DiscPer", rs.getString("dblDiscountPer"));
	    objRows.put("DiscAmount", rs.getString("dblDiscountAmt"));
	    objRows.put("dteBillDate", rs.getString("dteBillDate"));

	    arrObj.add(objRows);
	    flgDataForPosting = true;
	}
	rs.close();

	if (flgDataForPosting)
	{
	    objJson.put("BillPromotionDtl", arrObj);
	    String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostTransactionDataToHOPOS";

	    URL url = new URL(hoURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    OutputStream os = conn.getOutputStream();
	    os.write(objJson.toString().getBytes());
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
	    System.out.println("BillPromo= " + op);
	    conn.disconnect();
	    if (op.equals("true"))
	    {
		StringBuilder sbUpdate = new StringBuilder(updateBills);
		StringBuilder sbUpdateItems = new StringBuilder(updateItemCodes);
		StringBuilder sbUpdatePromoCodes = new StringBuilder(updatePromoCodes);

		if (updateBills.length() > 0)
		{
		    updateBills = sbUpdate.delete(0, 1).toString();
		    updateItemCodes = sbUpdateItems.delete(0, 1).toString();
		    updatePromoCodes = sbUpdatePromoCodes.delete(0, 1).toString();

		    dbMysql.execute("update tblbillpromotiondtl set strDataPostFlag='Y' "
			    + " where strBillNo in (" + updateBills + ") and strItemCode in (" + updateItemCodes + ") "
			    + " and strPromotionCode in (" + updatePromoCodes + ")");
		    dbMysql.execute("update tblqbillpromotiondtl set strDataPostFlag='Y' "
			    + " where strBillNo in (" + updateBills + ") and strItemCode in (" + updateItemCodes + ") "
			    + " and strPromotionCode in (" + updatePromoCodes + ")");
		    //dbMysql.execute("update tblbillpromotiondtl set strDataPostFlag='Y' where strBillNo in (" + updateBills + ")");
		}
		if (formName.equals("ManuallyLive") || formName.equals("ManuallyQFile"))
		{
		    JOptionPane.showMessageDialog(null, "Data Posted Successfully!!!");
		}
	    }
	}

	return 1;
    }

    private int funPostBillComplimentryDtlData(String formName) throws Exception
    {
	String updateBills = "";
	String query = "";

	if (formName.equals("Bill"))
	{
	    query = "select * from tblbillcomplementrydtl "
		    + "where strDataPostFlag='N' "
		    + "and strBillNo IN(select strBillNo from tblbillsettlementdtl)";
	}
	else if (formName.equals("Day End"))
	{
	    query = "select * from tblqbillcomplementrydtl "
		    + "where strDataPostFlag='N' "
		    + "and strBillNo IN(select strBillNo from tblqbillsettlementdtl)";
	}
	else if (formName.equals("ManuallyLive"))
	{
	    query = "select b.* from tblbillhd a,tblbillcomplementrydtl b "
		    + " where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and b.strDataPostFlag='N'";
	}
	else if (formName.equals("ManuallyQFile"))
	{
	    query = "select b.* from tblqbillhd a,tblqbillcomplementrydtl b "
		    + " where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and b.strDataPostFlag='N'";
	}
	ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	JSONObject objJson = new JSONObject();
	JSONArray arrObj = new JSONArray();

	boolean flgDataForPosting = false;
	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateBills += ",'" + rs.getString("strBillNo") + "'";
	    objRows.put("ItemCode", rs.getString("strItemCode"));
	    objRows.put("ItemName", rs.getString("strItemName"));
	    objRows.put("BillNo", rs.getString("strBillNo"));
	    objRows.put("AdvBookingNo", rs.getString("strAdvBookingNo"));
	    objRows.put("Rate", rs.getString("dblRate"));
	    objRows.put("Quantity", rs.getString("dblQuantity"));
	    objRows.put("Amount", rs.getString("dblAmount"));
	    objRows.put("TaxAmount", rs.getString("dblTaxAmount"));
	    objRows.put("BillDate", rs.getString("dteBillDate"));
	    objRows.put("KOTNo", rs.getString("strKOTNo"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("CustomerCode", rs.getString("strCustomerCode"));
	    objRows.put("OrderProcessing", rs.getString("tmeOrderProcessing"));
	    objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));
	    objRows.put("MMSDataPostFlag", rs.getString("strMMSDataPostFlag"));
	    objRows.put("ManualKOTNo", rs.getString("strManualKOTNo"));
	    objRows.put("tdhYN", rs.getString("tdhYN"));
	    objRows.put("PromoCode", rs.getString("strPromoCode"));
	    objRows.put("CounterCode", rs.getString("strCounterCode"));
	    objRows.put("WaiterNo", rs.getString("strWaiterNo"));
	    objRows.put("DiscountAmt", rs.getString("dblDiscountAmt"));
	    objRows.put("DiscountPer", rs.getString("dblDiscountPer"));

	    objRows.put("strSequenceNo", rs.getString("strSequenceNo"));
	    objRows.put("strType", rs.getString("strType"));
	    objRows.put("dtBillDate", rs.getString("dtBillDate"));
	    objRows.put("tmeOrderPickup", rs.getString("tmeOrderPickup"));

	    arrObj.add(objRows);
	    flgDataForPosting = true;
	}
	rs.close();

	if (flgDataForPosting)
	{
	    objJson.put("BillComplimentryDtl", arrObj);
	    String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostTransactionDataToHOPOS";

	    URL url = new URL(hoURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    OutputStream os = conn.getOutputStream();
	    os.write(objJson.toString().getBytes());
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
	    System.out.println("BillPromo= " + op);
	    conn.disconnect();
	    if (op.equals("true"))
	    {
		StringBuilder sbUpdate = new StringBuilder(updateBills);
		if (updateBills.length() > 0)
		{
		    updateBills = sbUpdate.delete(0, 1).toString();
		    dbMysql.execute("update tblbillcomplementrydtl set strDataPostFlag='Y' where strBillNo in (" + updateBills + ")");
		    dbMysql.execute("update tblqbillcomplementrydtl set strDataPostFlag='Y' where strBillNo in (" + updateBills + ")");
		    //dbMysql.execute("update tblbillpromotiondtl set strDataPostFlag='Y' where strBillNo in (" + updateBills + ")");
		}
		if (formName.equals("ManuallyLive") || formName.equals("ManuallyQFile"))
		{
		    JOptionPane.showMessageDialog(null, "Data Posted Successfully!!!");
		}
	    }
	}

	return 1;
    }

    private int funPostHomeDeliveryData(String formName) throws Exception
    {
	String updateBills = "";
	String query = "select * from tblhomedelivery where strDataPostFlag='N'";
	if (formName.equals("ManuallyLive"))
	{
	    query = "select b.* from tblbillhd a,tblhomedelivery b "
		    + " where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and b.strDataPostFlag='N'";
	}
	if (formName.equals("ManuallyQFile"))
	{
	    query = "select b.* from tblqbillhd a,tblhomedelivery b "
		    + " where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and b.strDataPostFlag='N'";
	}
	ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	JSONObject objJson = new JSONObject();
	JSONArray arrObj = new JSONArray();

	boolean flgDataForPosting = false;
	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateBills += ",'" + rs.getString("strBillNo") + "'";

	    objRows.put("BillNo", rs.getString("strBillNo"));
	    objRows.put("CustomerCode", rs.getString("strCustomerCode"));
	    objRows.put("DPCode", rs.getString("strDPCode"));
	    objRows.put("POSCode", rs.getString("strPOSCode"));
	    objRows.put("Date", rs.getString("dteDate"));
	    objRows.put("Time", rs.getString("tmeTime"));
	    objRows.put("CustAddress1", rs.getString("strCustAddressLine1"));
	    objRows.put("CustAddress2", rs.getString("strCustAddressLine2"));
	    objRows.put("CustAddress3", rs.getString("strCustAddressLine3"));
	    objRows.put("CustAddress4", rs.getString("strCustAddressLine4"));
	    objRows.put("CustCity", rs.getString("strCustCity"));
	    objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("dblHomeDeliCharge", rs.getString("dblHomeDeliCharge"));
	    objRows.put("dblLooseCashAmt", rs.getString("dblLooseCashAmt"));

	    arrObj.add(objRows);
	    flgDataForPosting = true;
	}
	rs.close();

	if (flgDataForPosting)
	{
	    objJson.put("HomeDelivery", arrObj);
	    String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostTransactionDataToHOPOS";

	    URL url = new URL(hoURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    OutputStream os = conn.getOutputStream();
	    os.write(objJson.toString().getBytes());
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
	    System.out.println("Home Del= " + op);
	    conn.disconnect();

	    if (op.equals("true"))
	    {
		StringBuilder sbUpdate = new StringBuilder(updateBills);
		if (updateBills.length() > 0)
		{
		    updateBills = sbUpdate.delete(0, 1).toString();
		    //System.out.println("Billsss="+updateBills);
		    dbMysql.execute("update tblhomedelivery set strDataPostFlag='Y' where strBillNo in (" + updateBills + ")");
		}
		if (formName.equals("ManuallyLive") || formName.equals("ManuallyQFile"))
		{
		    JOptionPane.showMessageDialog(null, "Data Posted Successfully!!!");
		}
	    }
	}

	return 1;
    }

    private int funPostHomeDeliveryDtlData(String formName) throws Exception
    {
	String updateBills = "";
	String query = "select * from tblhomedeldtl where strDataPostFlag='N'";
	if (formName.equals("ManuallyLive"))
	{
	    query = "select b.* from tblbillhd a,tblhomedeldtl b "
		    + " where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and b.strDataPostFlag='N'";
	}
	if (formName.equals("ManuallyQFile"))
	{
	    query = "select b.* from tblqbillhd a,tblhomedeldtl b "
		    + " where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' "
		    + " and b.strDataPostFlag='N'";
	}
	ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	JSONObject objJson = new JSONObject();
	JSONArray arrObj = new JSONArray();

	boolean flgDataForPosting = false;
	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateBills += ",'" + rs.getString("strBillNo") + "'";

	    objRows.put("BillNo", rs.getString("strBillNo"));
	    objRows.put("DPCode", rs.getString("strDPCode"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));
	    objRows.put("strSettleYN", rs.getString("strSettleYN"));
	    objRows.put("dblDBIncentives", rs.getString("dblDBIncentives"));
	    objRows.put("dteBillDate", rs.getString("dteBillDate"));

	    arrObj.add(objRows);
	    flgDataForPosting = true;
	}
	rs.close();

	if (flgDataForPosting)
	{
	    objJson.put("HomeDeliveryDtl", arrObj);
	    String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostTransactionDataToHOPOS";

	    URL url = new URL(hoURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    OutputStream os = conn.getOutputStream();
	    os.write(objJson.toString().getBytes());
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
	    System.out.println("Home Del= " + op);
	    conn.disconnect();

	    if (op.equals("true"))
	    {
		StringBuilder sbUpdate = new StringBuilder(updateBills);
		if (updateBills.length() > 0)
		{
		    updateBills = sbUpdate.delete(0, 1).toString();
		    //System.out.println("Billsss="+updateBills);
		    dbMysql.execute("update tblhomedeldtl set strDataPostFlag='Y' "
			    + "where strBillNo in (" + updateBills + ")");
		}
		if (formName.equals("ManuallyLive") || formName.equals("ManuallyQFile"))
		{
		    JOptionPane.showMessageDialog(null, "Data Posted Successfully!!!");
		}
	    }
	}

	return 1;
    }

    private int funPostAdvReceiptHdData(String formName) throws Exception
    {
	String updateBills = "";
	String query = "select * from tblqadvancereceipthd where strDataPostFlag='N'";

	ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	JSONObject objJson = new JSONObject();
	JSONArray arrObj = new JSONArray();

	boolean flgDataForPosting = false;
	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateBills += ",'" + rs.getString("strReceiptNo") + "'";

	    objRows.put("ReceiptNo", rs.getString("strReceiptNo"));
	    objRows.put("AdvBookingNo", rs.getString("strAdvBookingNo"));
	    objRows.put("POSCode", rs.getString("strPOSCode"));
	    objRows.put("SettelmentMode", rs.getString("strSettelmentMode"));
	    objRows.put("ReceiptDate", rs.getString("dtReceiptDate"));
	    objRows.put("AdvDeposite", rs.getString("dblAdvDeposite"));
	    objRows.put("ShiftCode", rs.getString("intShiftCode"));
	    objRows.put("UserCreated", rs.getString("strUserCreated"));
	    objRows.put("UserEdited", rs.getString("strUserEdited"));
	    objRows.put("DateCreated", rs.getString("dtDateCreated"));
	    objRows.put("DateEdited", rs.getString("dtDateEdited"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));

	    arrObj.add(objRows);
	    flgDataForPosting = true;
	}
	rs.close();

	query = "select * from tbladvancereceipthd where strDataPostFlag='N'";
	rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateBills += ",'" + rs.getString("strReceiptNo") + "'";

	    objRows.put("ReceiptNo", rs.getString("strReceiptNo"));
	    objRows.put("AdvBookingNo", rs.getString("strAdvBookingNo"));
	    objRows.put("POSCode", rs.getString("strPOSCode"));
	    objRows.put("SettelmentMode", rs.getString("strSettelmentMode"));
	    objRows.put("ReceiptDate", rs.getString("dtReceiptDate"));
	    objRows.put("AdvDeposite", rs.getString("dblAdvDeposite"));
	    objRows.put("ShiftCode", rs.getString("intShiftCode"));
	    objRows.put("UserCreated", rs.getString("strUserCreated"));
	    objRows.put("UserEdited", rs.getString("strUserEdited"));
	    objRows.put("DateCreated", rs.getString("dtDateCreated"));
	    objRows.put("DateEdited", rs.getString("dtDateEdited"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));

	    arrObj.add(objRows);
	    flgDataForPosting = true;
	}
	rs.close();

	if (flgDataForPosting)
	{
	    objJson.put("AdvanceReceiptHd", arrObj);
	    String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostTransactionDataToHOPOS";

	    URL url = new URL(hoURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    OutputStream os = conn.getOutputStream();
	    os.write(objJson.toString().getBytes());
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
	    System.out.println("Adv Rec Hd= " + op);
	    conn.disconnect();

	    if (op.equals("true"))
	    {
		StringBuilder sbUpdate = new StringBuilder(updateBills);
		if (updateBills.length() > 0)
		{
		    updateBills = sbUpdate.delete(0, 1).toString();
		    //System.out.println("Billsss="+updateBills);
		    dbMysql.execute("update tbladvancereceipthd set strDataPostFlag='Y' where strReceiptNo in (" + updateBills + ")");
		    dbMysql.execute("update tblqadvancereceipthd set strDataPostFlag='Y' where strReceiptNo in (" + updateBills + ")");
		}
		if (formName.equals("ManuallyLive") || formName.equals("ManuallyQFile"))
		{
		    JOptionPane.showMessageDialog(null, "Data Posted Successfully!!!");
		}
	    }
	}

	return 1;
    }

    private int funPostAdvReceiptDtlData(String formName) throws Exception
    {
	String updateBills = "";
	String query = "select * from tblqadvancereceiptdtl where strDataPostFlag='N'";
	ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	JSONObject objJson = new JSONObject();
	JSONArray arrObj = new JSONArray();

	boolean flgDataForPosting = false;
	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateBills += ",'" + rs.getString("strReceiptNo") + "'";

	    objRows.put("ReceiptNo", rs.getString("strReceiptNo"));
	    objRows.put("SettlementCode", rs.getString("strSettlementCode"));
	    objRows.put("CardNo", rs.getString("strCardNo"));
	    objRows.put("Expirydate", rs.getString("strExpirydate"));
	    objRows.put("ChequeNo", rs.getString("strChequeNo"));
	    objRows.put("ChequeDate", rs.getString("dteCheque"));
	    objRows.put("BankName", rs.getString("strBankName"));
	    objRows.put("AdvDepositesettleAmt", rs.getString("dblAdvDepositesettleAmt"));
	    objRows.put("Remark", rs.getString("strRemark"));
	    objRows.put("PaidAmt", rs.getString("dblPaidAmt"));
	    objRows.put("Installment", rs.getString("dteInstallment"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));
	    objRows.put("dteInstallment", rs.getString("dteInstallment"));
	    objRows.put("dteInstallment", rs.getString("dteInstallment"));

	    arrObj.add(objRows);
	    flgDataForPosting = true;
	}
	rs.close();

	query = "select * from tbladvancereceiptdtl where strDataPostFlag='N'";
	rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateBills += ",'" + rs.getString("strReceiptNo") + "'";

	    objRows.put("ReceiptNo", rs.getString("strReceiptNo"));
	    objRows.put("SettlementCode", rs.getString("strSettlementCode"));
	    objRows.put("CardNo", rs.getString("strCardNo"));
	    objRows.put("Expirydate", rs.getString("strExpirydate"));
	    objRows.put("ChequeNo", rs.getString("strChequeNo"));
	    objRows.put("ChequeDate", rs.getString("dteCheque"));
	    objRows.put("BankName", rs.getString("strBankName"));
	    objRows.put("AdvDepositesettleAmt", rs.getString("dblAdvDepositesettleAmt"));
	    objRows.put("Remark", rs.getString("strRemark"));
	    objRows.put("PaidAmt", rs.getString("dblPaidAmt"));
	    objRows.put("Installment", rs.getString("dteInstallment"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));
	    objRows.put("dteInstallment", rs.getString("dteInstallment"));

	    arrObj.add(objRows);
	    flgDataForPosting = true;
	}
	rs.close();

	if (flgDataForPosting)
	{
	    objJson.put("AdvanceReceiptDtl", arrObj);
	    String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostTransactionDataToHOPOS";

	    URL url = new URL(hoURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    OutputStream os = conn.getOutputStream();
	    os.write(objJson.toString().getBytes());
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
	    System.out.println("Adv Rec Dtl= " + op);
	    conn.disconnect();

	    if (op.equals("true"))
	    {
		StringBuilder sbUpdate = new StringBuilder(updateBills);
		if (updateBills.length() > 0)
		{
		    updateBills = sbUpdate.delete(0, 1).toString();
		    //System.out.println("Billsss="+updateBills);
		    dbMysql.execute("update tbladvancereceiptdtl set strDataPostFlag='Y' where strReceiptNo in (" + updateBills + ")");
		    dbMysql.execute("update tblqadvancereceiptdtl set strDataPostFlag='Y' where strReceiptNo in (" + updateBills + ")");
		}
		if (formName.equals("ManuallyLive") || formName.equals("ManuallyQFile"))
		{
		    JOptionPane.showMessageDialog(null, "Data Posted Successfully!!!");
		}
	    }
	}

	return 1;
    }

    private int funPostAdvOrderHdData(String formName) throws Exception
    {
	String updateBills = "";
	String query = "select * from tblqadvbookbillhd where strDataPostFlag='N'";
	ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	JSONObject objJson = new JSONObject();
	JSONArray arrObj = new JSONArray();

	boolean flgDataForPosting = false;
	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateBills += ",'" + rs.getString("strAdvBookingNo") + "'";

	    objRows.put("AdvBookingNo", rs.getString("strAdvBookingNo"));
	    objRows.put("AdvBookingDate", rs.getString("dteAdvBookingDate"));
	    objRows.put("OrderFor", rs.getString("dteOrderFor"));
	    objRows.put("POSCode", rs.getString("strPOSCode"));
	    objRows.put("SettelmentMode", rs.getString("strSettelmentMode"));
	    objRows.put("DiscountAmt", rs.getString("dblDiscountAmt"));
	    objRows.put("DiscountPer", rs.getString("dblDiscountPer"));
	    objRows.put("TaxAmt", rs.getString("dblTaxAmt"));
	    objRows.put("SubTotal", rs.getString("dblSubTotal"));
	    objRows.put("GrandTotal", rs.getString("dblGrandTotal"));

	    objRows.put("UserCreated", rs.getString("strUserCreated"));
	    objRows.put("UserEdited", rs.getString("strUserEdited"));
	    objRows.put("DateCreated", rs.getString("dteDateCreated"));
	    objRows.put("DateEdited", rs.getString("dteDateEdited"));

	    objRows.put("CustomerCode", rs.getString("strCustomerCode"));
	    objRows.put("ShiftCode", rs.getString("intShiftCode"));
	    objRows.put("Message", rs.getString("strMessage"));
	    objRows.put("Shape", rs.getString("strShape"));

	    objRows.put("Note", rs.getString("strNote"));
	    objRows.put("DeliveryTime", rs.getString("strDeliveryTime"));
	    objRows.put("WaiterNo", rs.getString("strWaiterNo"));
	    objRows.put("HomeDelivery", rs.getString("strHomeDelivery"));
	    objRows.put("HomeDelCharges", rs.getString("dblHomeDelCharges"));
	    objRows.put("OrderType", rs.getString("strOrderType"));
	    objRows.put("ManualAdvOrderNo", rs.getString("strManualAdvOrderNo"));
	    objRows.put("ImageName", rs.getString("strImageName"));
	    objRows.put("SpecialsymbolImage", rs.getString("strSpecialsymbolImage"));
	    objRows.put("UrgentOrderYN", rs.getString("strUrgentOrder"));

	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));

	    arrObj.add(objRows);
	    flgDataForPosting = true;
	}
	rs.close();

	query = "select * from tbladvbookbillhd where strDataPostFlag='N'";
	rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateBills += ",'" + rs.getString("strAdvBookingNo") + "'";

	    objRows.put("AdvBookingNo", rs.getString("strAdvBookingNo"));
	    objRows.put("AdvBookingDate", rs.getString("dteAdvBookingDate"));
	    objRows.put("OrderFor", rs.getString("dteOrderFor"));
	    objRows.put("POSCode", rs.getString("strPOSCode"));
	    objRows.put("SettelmentMode", rs.getString("strSettelmentMode"));
	    objRows.put("DiscountAmt", rs.getString("dblDiscountAmt"));
	    objRows.put("DiscountPer", rs.getString("dblDiscountPer"));
	    objRows.put("TaxAmt", rs.getString("dblTaxAmt"));
	    objRows.put("SubTotal", rs.getString("dblSubTotal"));
	    objRows.put("GrandTotal", rs.getString("dblGrandTotal"));

	    objRows.put("UserCreated", rs.getString("strUserCreated"));
	    objRows.put("UserEdited", rs.getString("strUserEdited"));
	    objRows.put("DateCreated", rs.getString("dteDateCreated"));
	    objRows.put("DateEdited", rs.getString("dteDateEdited"));

	    objRows.put("CustomerCode", rs.getString("strCustomerCode"));
	    objRows.put("ShiftCode", rs.getString("intShiftCode"));
	    objRows.put("Message", rs.getString("strMessage"));
	    objRows.put("Shape", rs.getString("strShape"));

	    objRows.put("Note", rs.getString("strNote"));
	    objRows.put("DeliveryTime", rs.getString("strDeliveryTime"));
	    objRows.put("WaiterNo", rs.getString("strWaiterNo"));
	    objRows.put("HomeDelivery", rs.getString("strHomeDelivery"));
	    objRows.put("HomeDelCharges", rs.getString("dblHomeDelCharges"));
	    objRows.put("OrderType", rs.getString("strOrderType"));
	    objRows.put("ManualAdvOrderNo", rs.getString("strManualAdvOrderNo"));
	    objRows.put("ImageName", rs.getString("strImageName"));
	    objRows.put("SpecialsymbolImage", rs.getString("strSpecialsymbolImage"));

	    objRows.put("UrgentOrderYN", rs.getString("strUrgentOrder"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));

	    arrObj.add(objRows);
	    flgDataForPosting = true;
	}
	rs.close();

	if (flgDataForPosting)
	{
	    objJson.put("AdvBookBillHd", arrObj);
	    String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostTransactionDataToHOPOS";

	    URL url = new URL(hoURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    OutputStream os = conn.getOutputStream();
	    os.write(objJson.toString().getBytes());
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
	    System.out.println("Adv Order Hd= " + op);
	    conn.disconnect();

	    if (op.equals("true"))
	    {
		StringBuilder sbUpdate = new StringBuilder(updateBills);
		if (updateBills.length() > 0)
		{
		    updateBills = sbUpdate.delete(0, 1).toString();
		    //System.out.println("Billsss="+updateBills);
		    dbMysql.execute("update tbladvbookbillhd set strDataPostFlag='Y' "
			    + "where strAdvBookingNo in (" + updateBills + ")");
		    dbMysql.execute("update tblqadvbookbillhd set strDataPostFlag='Y' "
			    + "where strAdvBookingNo in (" + updateBills + ")");
		}
		if (formName.equals("ManuallyLive") || formName.equals("ManuallyQFile"))
		{
		    JOptionPane.showMessageDialog(null, "Data Posted Successfully!!!");
		}
	    }
	}

	return 1;
    }

    private int funPostAdvOrderDtlData(String formName) throws Exception
    {
	String updateBills = "";
	String query = "select * from tblqadvbookbilldtl where strDataPostFlag='N'";
	ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	JSONObject objJson = new JSONObject();
	JSONArray arrObj = new JSONArray();

	boolean flgDataForPosting = false;
	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateBills += ",'" + rs.getString("strAdvBookingNo") + "'";

	    objRows.put("AdvBookingNo", rs.getString("strAdvBookingNo"));
	    objRows.put("ItemCode", rs.getString("strItemCode"));
	    objRows.put("ItemName", rs.getString("strItemName"));
	    objRows.put("Quantity", rs.getString("dblQuantity"));
	    objRows.put("Weight", rs.getString("dblWeight"));
	    objRows.put("Amount", rs.getString("dblAmount"));
	    objRows.put("TaxAmount", rs.getString("dblTaxAmount"));
	    objRows.put("AdvBookingDate", rs.getString("dteAdvBookingDate"));
	    objRows.put("OrderFor", rs.getString("dteOrderFor"));
	    objRows.put("CustomerCode", rs.getString("strCustomerCode"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));

	    arrObj.add(objRows);
	    flgDataForPosting = true;
	}
	rs.close();

	query = "select * from tbladvbookbilldtl where strDataPostFlag='N'";
	rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateBills += ",'" + rs.getString("strAdvBookingNo") + "'";

	    objRows.put("AdvBookingNo", rs.getString("strAdvBookingNo"));
	    objRows.put("ItemCode", rs.getString("strItemCode"));
	    objRows.put("ItemName", rs.getString("strItemName"));
	    objRows.put("Quantity", rs.getString("dblQuantity"));
	    objRows.put("Weight", rs.getString("dblWeight"));
	    objRows.put("Amount", rs.getString("dblAmount"));
	    objRows.put("TaxAmount", rs.getString("dblTaxAmount"));
	    objRows.put("AdvBookingDate", rs.getString("dteAdvBookingDate"));
	    objRows.put("OrderFor", rs.getString("dteOrderFor"));
	    objRows.put("CustomerCode", rs.getString("strCustomerCode"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));

	    arrObj.add(objRows);
	    flgDataForPosting = true;
	}
	rs.close();

	if (flgDataForPosting)
	{
	    objJson.put("AdvBookBillDtl", arrObj);
	    String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostTransactionDataToHOPOS";

	    URL url = new URL(hoURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    OutputStream os = conn.getOutputStream();
	    os.write(objJson.toString().getBytes());
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
	    System.out.println("Adv Order Dtl= " + op);
	    conn.disconnect();

	    if (op.equals("true"))
	    {
		StringBuilder sbUpdate = new StringBuilder(updateBills);
		if (updateBills.length() > 0)
		{
		    updateBills = sbUpdate.delete(0, 1).toString();
		    //System.out.println("Billsss="+updateBills);
		    dbMysql.execute("update tbladvbookbilldtl set strDataPostFlag='Y' where strAdvBookingNo in (" + updateBills + ")");
		    dbMysql.execute("update tblqadvbookbilldtl set strDataPostFlag='Y' where strAdvBookingNo in (" + updateBills + ")");
		}
		if (formName.equals("ManuallyLive") || formName.equals("ManuallyQFile"))
		{
		    JOptionPane.showMessageDialog(null, "Data Posted Successfully!!!");
		}
	    }
	}

	return 1;
    }

    private int funPostAdvOrderModifierDtlData(String formName) throws Exception
    {
	String updateBills = "";
	String query = "select * from tblqadvordermodifierdtl where strDataPostFlag='N'";
	ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	JSONObject objJson = new JSONObject();
	JSONArray arrObj = new JSONArray();

	boolean flgDataForPosting = false;
	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateBills += ",'" + rs.getString("strAdvOrderNo") + "'";

	    objRows.put("AdvBookingNo", rs.getString("strAdvOrderNo"));
	    objRows.put("ItemCode", rs.getString("strItemCode"));
	    objRows.put("ModifierCode", rs.getString("strModifierCode"));
	    objRows.put("ModifierName", rs.getString("strModifierName"));
	    objRows.put("Quantity", rs.getString("dblQuantity"));
	    objRows.put("Amount", rs.getString("dblAmount"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("CustomerCode", rs.getString("strCustomerCode"));
	    objRows.put("UserCreated", rs.getString("strUserCreated"));
	    objRows.put("UserEdited", rs.getString("strUserEdited"));
	    objRows.put("DateCreated", rs.getString("dteDateCreated"));
	    objRows.put("DateEdited", rs.getString("dteDateEdited"));
	    objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));

	    arrObj.add(objRows);
	    flgDataForPosting = true;
	}
	rs.close();

	query = "select * from tbladvordermodifierdtl where strDataPostFlag='N'";
	rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateBills += ",'" + rs.getString("strAdvOrderNo") + "'";

	    objRows.put("AdvBookingNo", rs.getString("strAdvOrderNo"));
	    objRows.put("ItemCode", rs.getString("strItemCode"));
	    objRows.put("ModifierCode", rs.getString("strModifierCode"));
	    objRows.put("ModifierName", rs.getString("strModifierName"));
	    objRows.put("Quantity", rs.getString("dblQuantity"));
	    objRows.put("Amount", rs.getString("dblAmount"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("CustomerCode", rs.getString("strCustomerCode"));
	    objRows.put("UserCreated", rs.getString("strUserCreated"));
	    objRows.put("UserEdited", rs.getString("strUserEdited"));
	    objRows.put("DateCreated", rs.getString("dteDateCreated"));
	    objRows.put("DateEdited", rs.getString("dteDateEdited"));
	    objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));

	    arrObj.add(objRows);
	    flgDataForPosting = true;
	}
	rs.close();

	if (flgDataForPosting)
	{
	    objJson.put("AdvBookBillModifierDtl", arrObj);
	    String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostTransactionDataToHOPOS";

	    URL url = new URL(hoURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    OutputStream os = conn.getOutputStream();
	    os.write(objJson.toString().getBytes());
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
	    System.out.println("Adv Order Mod Dtl= " + op);
	    conn.disconnect();

	    if (op.equals("true"))
	    {
		StringBuilder sbUpdate = new StringBuilder(updateBills);
		if (updateBills.length() > 0)
		{
		    updateBills = sbUpdate.delete(0, 1).toString();
		    //System.out.println("Billsss="+updateBills);
		    dbMysql.execute("update tbladvordermodifierdtl set strDataPostFlag='Y' where strAdvOrderNo in (" + updateBills + ")");
		    dbMysql.execute("update tblqadvordermodifierdtl set strDataPostFlag='Y' where strAdvOrderNo in (" + updateBills + ")");
		}
		if (formName.equals("ManuallyLive") || formName.equals("ManuallyQFile"))
		{
		    JOptionPane.showMessageDialog(null, "Data Posted Successfully!!!");
		}
	    }
	}

	return 1;
    }

    private int funPostAdvOrderCharDtlData(String formName) throws Exception
    {
	String updateBills = "";
	String query = "select * from tbladvbookbillchardtl where strDataPostFlag='N'";
	ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	JSONObject objJson = new JSONObject();
	JSONArray arrObj = new JSONArray();

	boolean flgDataForPosting = false;
	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateBills += ",'" + rs.getString("strAdvBookingNo") + "'";

	    objRows.put("AdvBookingNo", rs.getString("strAdvBookingNo"));
	    objRows.put("ItemCode", rs.getString("strItemCode"));
	    objRows.put("CharCode", rs.getString("strCharCode"));
	    objRows.put("CharValues", rs.getString("strCharValues"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));

	    arrObj.add(objRows);
	    flgDataForPosting = true;
	}
	rs.close();

	query = "select * from tblqadvbookbillchardtl where strDataPostFlag='N'";
	rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateBills += ",'" + rs.getString("strAdvBookingNo") + "'";

	    objRows.put("AdvBookingNo", rs.getString("strAdvBookingNo"));
	    objRows.put("ItemCode", rs.getString("strItemCode"));
	    objRows.put("CharCode", rs.getString("strCharCode"));
	    objRows.put("CharValues", rs.getString("strCharValues"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));

	    arrObj.add(objRows);
	    flgDataForPosting = true;
	}
	rs.close();

	if (flgDataForPosting)
	{
	    objJson.put("AdvBookBillCharDtl", arrObj);
	    String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostTransactionDataToHOPOS";

	    URL url = new URL(hoURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    OutputStream os = conn.getOutputStream();
	    os.write(objJson.toString().getBytes());
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
	    System.out.println("Adv Order Char Dtl= " + op);
	    conn.disconnect();

	    if (op.equals("true"))
	    {
		StringBuilder sbUpdate = new StringBuilder(updateBills);
		if (updateBills.length() > 0)
		{
		    updateBills = sbUpdate.delete(0, 1).toString();
		    //System.out.println("Billsss="+updateBills);
		    dbMysql.execute("update tbladvbookbillchardtl set strDataPostFlag='Y' where strAdvBookingNo in (" + updateBills + ")");
		    dbMysql.execute("update tblqadvbookbillchardtl set strDataPostFlag='Y' where strAdvBookingNo in (" + updateBills + ")");
		}
		if (formName.equals("ManuallyLive") || formName.equals("ManuallyQFile"))
		{
		    JOptionPane.showMessageDialog(null, "Data Posted Successfully!!!");
		}
	    }
	}

	return 1;
    }

    private int funPostPlaceOrderData(String formName) throws Exception
    {
	String updateOrders = "";
	JSONObject objJson = new JSONObject();
	JSONArray arrObjPlaceOrderHd = new JSONArray();
	String query = "select * from tblplaceorderhd where strDataPostFlag='N'";
	ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);

	boolean flgDataForPosting = false;
	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateOrders += ",'" + rs.getString("strOrderCode") + "'";

	    objRows.put("OrderCode", rs.getString("strOrderCode"));
	    objRows.put("SOCode", rs.getString("strSOCode"));
	    objRows.put("SODate", rs.getString("dteSODate"));
	    objRows.put("OrderDate", rs.getString("dteOrderDate"));
	    objRows.put("UserCreated", rs.getString("strUserCreated"));
	    objRows.put("DateCreated", rs.getString("dteDateCreated"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("CloseSO", rs.getString("strCloseSO"));
	    objRows.put("DCCode", rs.getString("strDCCode"));
	    objRows.put("OrderTypeCode", rs.getString("strOrderTypeCode"));
	    objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));
	    objRows.put("OrderType", rs.getString("strOrderType"));

	    arrObjPlaceOrderHd.add(objRows);
	    flgDataForPosting = true;
	}
	rs.close();

	String updateOrderDtl = "";
	query = " select * from tblplaceorderdtl where strDataPostFlag='N' ";
	rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	JSONArray arrObjPlaceOrderDtl = new JSONArray();

	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateOrderDtl += ",'" + rs.getString("strOrderCode") + "'";

	    objRows.put("OrderCode", rs.getString("strOrderCode"));
	    objRows.put("ProductCode", rs.getString("strProductCode"));
	    objRows.put("ItemCode", rs.getString("strItemCode"));
	    objRows.put("Quantity", rs.getString("dblQty"));
	    objRows.put("StockQty", rs.getString("dblStockQty"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));
	    objRows.put("AdvOrderNo", rs.getString("strAdvOrderNo"));

	    arrObjPlaceOrderDtl.add(objRows);
	    flgDataForPosting = true;
	}
	rs.close();

	String updateAdvOrderDtl = "";
	query = " select * from tblplaceorderadvorderdtl where strDataPostFlag='N' ";
	rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	JSONArray arrObjPlaceOrderAdvOrderDtl = new JSONArray();

	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateAdvOrderDtl += ",'" + rs.getString("strAdvOrderNo") + "'";

	    objRows.put("AdvOrderNo", rs.getString("strAdvOrderNo"));
	    objRows.put("OrderDate", rs.getString("dteOrderDate"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));
	    objRows.put("OrderType", rs.getString("strOrderType"));

	    arrObjPlaceOrderAdvOrderDtl.add(objRows);
	    flgDataForPosting = true;
	}
	rs.close();

	if (flgDataForPosting)
	{
	    objJson.put("PlaceOrderHd", arrObjPlaceOrderHd);
	    objJson.put("PlaceOrderDtl", arrObjPlaceOrderDtl);
	    objJson.put("PlaceAdvOrderDtl", arrObjPlaceOrderAdvOrderDtl);

	    String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostPlaceOrderDataToHOPOS";

	    URL url = new URL(hoURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    OutputStream os = conn.getOutputStream();
	    os.write(objJson.toString().getBytes());
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
	    System.out.println("Place Order Hd= " + op);
	    conn.disconnect();

	    if (op.equals("true"))
	    {
		StringBuilder sbUpdate = new StringBuilder(updateOrders);
		if (updateOrders.length() > 0)
		{
		    updateOrders = sbUpdate.delete(0, 1).toString();
		    //System.out.println("Billsss="+updateBills);
		    dbMysql.execute("update tblplaceorderhd set strDataPostFlag='Y' where strOrderCode in (" + updateOrders + ")");
		}
		sbUpdate = new StringBuilder(updateOrderDtl);
		if (updateOrderDtl.length() > 0)
		{
		    updateOrderDtl = sbUpdate.delete(0, 1).toString();
		    //System.out.println("Billsss="+updateBills);
		    dbMysql.execute("update tblplaceorderdtl set strDataPostFlag='Y' where strOrderCode in (" + updateOrderDtl + ")");
		}
		sbUpdate = new StringBuilder(updateAdvOrderDtl);
		if (updateAdvOrderDtl.length() > 0)
		{
		    updateAdvOrderDtl = sbUpdate.delete(0, 1).toString();
		    //System.out.println("Billsss="+updateBills);
		    dbMysql.execute("update tblplaceorderadvorderdtl set strDataPostFlag='Y' where strAdvOrderNo in (" + updateAdvOrderDtl + ")");
		}

		if (formName.equals("ManuallyLive") || formName.equals("ManuallyQFile"))
		{
		    JOptionPane.showMessageDialog(null, "Data Posted Successfully!!!");
		}
	    }
	}

	return 1;
    }

    private int funPostPlaceOrderHdData(String formName) throws Exception
    {
	String updateOrders = "";
	JSONObject objJson = new JSONObject();
	JSONArray arrObj = new JSONArray();
	String query = "select * from tblplaceorderhd where strDataPostFlag='N'";
	ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);

	boolean flgDataForPosting = false;
	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateOrders += ",'" + rs.getString("strOrderCode") + "'";

	    objRows.put("OrderCode", rs.getString("strOrderCode"));
	    objRows.put("SOCode", rs.getString("strSOCode"));
	    objRows.put("SODate", rs.getString("dteSODate"));
	    objRows.put("OrderDate", rs.getString("dteOrderDate"));
	    objRows.put("UserCreated", rs.getString("strUserCreated"));
	    objRows.put("DateCreated", rs.getString("dteDateCreated"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("CloseSO", rs.getString("strCloseSO"));
	    objRows.put("DCCode", rs.getString("strDCCode"));
	    objRows.put("OrderTypeCode", rs.getString("strOrderTypeCode"));
	    objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));
	    objRows.put("OrderType", rs.getString("strOrderType"));

	    arrObj.add(objRows);
	    flgDataForPosting = true;
	}
	rs.close();

	if (flgDataForPosting)
	{
	    objJson.put("PlaceOrderHd", arrObj);
	    String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostPlaceOrderDataToHOPOS";

	    URL url = new URL(hoURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    OutputStream os = conn.getOutputStream();
	    os.write(objJson.toString().getBytes());
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
	    System.out.println("Place Order Hd= " + op);
	    conn.disconnect();

	    if (op.equals("true"))
	    {
		StringBuilder sbUpdate = new StringBuilder(updateOrders);
		if (updateOrders.length() > 0)
		{
		    updateOrders = sbUpdate.delete(0, 1).toString();
		    //System.out.println("Billsss="+updateBills);
		    dbMysql.execute("update tblplaceorderhd set strDataPostFlag='Y' where strOrderCode in (" + updateOrders + ")");

		}
		if (formName.equals("ManuallyLive") || formName.equals("ManuallyQFile"))
		{
		    JOptionPane.showMessageDialog(null, "Data Posted Successfully!!!");
		}
	    }
	}

	return 1;
    }

    private int funPostPlaceOrderDtlData(String formName) throws Exception
    {
	String updateOrders = "";
	String query = " select * from tblplaceorderdtl where strDataPostFlag='N' ";
	ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	JSONObject objJson = new JSONObject();
	JSONArray arrObj = new JSONArray();

	boolean flgDataForPosting = false;
	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateOrders += ",'" + rs.getString("strOrderCode") + "'";

	    objRows.put("OrderCode", rs.getString("strOrderCode"));
	    objRows.put("ProductCode", rs.getString("strProductCode"));
	    objRows.put("ItemCode", rs.getString("strItemCode"));
	    objRows.put("Quantity", rs.getString("dblQty"));
	    objRows.put("StockQty", rs.getString("dblStockQty"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));
	    objRows.put("AdvOrderNo", rs.getString("strAdvOrderNo"));

	    arrObj.add(objRows);
	    flgDataForPosting = true;
	}
	rs.close();

	if (flgDataForPosting)
	{
	    objJson.put("PlaceOrderDtl", arrObj);
	    String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostPlaceOrderDataToHOPOS";

	    URL url = new URL(hoURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    OutputStream os = conn.getOutputStream();
	    os.write(objJson.toString().getBytes());
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

	    conn.disconnect();

	    if (op.equals("true"))
	    {

		StringBuilder sbUpdate = new StringBuilder(updateOrders);
		if (updateOrders.length() > 0)
		{
		    updateOrders = sbUpdate.delete(0, 1).toString();
		    //System.out.println("Billsss="+updateBills);
		    dbMysql.execute("update tblplaceorderdtl set strDataPostFlag='Y' where strOrderCode in (" + updateOrders + ")");

		}
		if (formName.equals("ManuallyLive") || formName.equals("ManuallyQFile"))
		{
		    JOptionPane.showMessageDialog(null, "Data Posted Successfully!!!");
		}
	    }
	}

	return 1;
    }

    private int funPostPlaceAdvanceOrderDtlData(String formName) throws Exception
    {
	String updateOrders = "";
	String query = " select * from tblplaceorderadvorderdtl where strDataPostFlag='N' ";
	ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	JSONObject objJson = new JSONObject();
	JSONArray arrObj = new JSONArray();

	boolean flgDataForPosting = false;
	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateOrders += ",'" + rs.getString("strAdvOrderNo") + "'";

	    objRows.put("AdvOrderNo", rs.getString("strAdvOrderNo"));
	    objRows.put("OrderDate", rs.getString("dteOrderDate"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));
	    objRows.put("OrderType", rs.getString("strOrderType"));

	    arrObj.add(objRows);
	    flgDataForPosting = true;
	}
	rs.close();

	if (flgDataForPosting)
	{
	    objJson.put("PlaceAdvOrderDtl", arrObj);
	    String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostPlaceOrderDataToHOPOS";

	    URL url = new URL(hoURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    OutputStream os = conn.getOutputStream();
	    os.write(objJson.toString().getBytes());
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
	    conn.disconnect();

	    if (op.equals("true"))
	    {

		StringBuilder sbUpdate = new StringBuilder(updateOrders);
		if (updateOrders.length() > 0)
		{
		    updateOrders = sbUpdate.delete(0, 1).toString();
		    //System.out.println("Billsss="+updateBills);
		    dbMysql.execute("update tblplaceorderadvorderdtl set strDataPostFlag='Y' where strAdvOrderNo in (" + updateOrders + ")");

		}
		if (formName.equals("ManuallyLive") || formName.equals("ManuallyQFile"))
		{
		    JOptionPane.showMessageDialog(null, "Data Posted Successfully!!!");
		}
	    }
	}

	return 1;
    }

    public int funPostAuditDataToHO(String formName)
    {
	try
	{
	    funPostVoidBillHdData();
	    funPostVoidBillDtlData();
	    funPostVoidBillDiscDtlData();
	    funPostVoidBillTaxDtlData();
	    funPostVoidKOTData();
	    funPostVoidBillModifierDtlData();
	    //
	    int i = funPostVoidAdvOrderBillHdData();
	    i = funPostVoidAdvOrderBillDtlData();
	    i = funPostVoidAdvOrderModifierDtlData();
	    i = funPostVoidAdvOrderBillCharDtlData();
	    i = funPostVoidAdvOrderReceiptHdData();
	    i = funPostVoidAdvOrderReceiptDtlData();

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return 1;
    }

    private int funPostVoidBillHdData() throws Exception
    {
	String updateBills = "";
	String query = "select * from tblvoidbillhd where strDataPostFlag='N' ";
	ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	JSONObject objJson = new JSONObject();
	JSONArray arrObj = new JSONArray();

	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateBills += ",'" + rs.getString("strBillNo") + "'";
	    objRows.put("BillNo", rs.getString("strBillNo"));
	    objRows.put("PosCode", rs.getString("strPosCode"));
	    objRows.put("ReasonCode", rs.getString("strReasonCode"));
	    objRows.put("ReasonName", rs.getString("strReasonName"));
	    objRows.put("ActualAmount", rs.getString("dblActualAmount"));
	    objRows.put("ModifiedAmount", rs.getString("dblModifiedAmount"));
	    objRows.put("BillDate", rs.getString("dteBillDate"));
	    objRows.put("TransType", rs.getString("strTransType"));
	    objRows.put("ModifyVoidBill", rs.getString("dteModifyVoidBill"));
	    objRows.put("TableNo", rs.getString("strTableNo"));
	    objRows.put("WaiterNo", rs.getString("strWaiterNo"));
	    objRows.put("ShiftCode", rs.getString("intShiftCode"));
	    objRows.put("UserCreated", rs.getString("strUserCreated"));
	    objRows.put("UserEdited", rs.getString("strUserEdited"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));
	    objRows.put("Remark", rs.getString("strRemark"));
	    objRows.put("strVoidBillType", rs.getString("strVoidBillType"));

	    arrObj.add(objRows);
	}

	rs.close();
	objJson.put("VoidBillHd", arrObj);
	String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostTransactionDataToHOPOS";

	URL url = new URL(hoURL);
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setDoOutput(true);
	conn.setRequestMethod("POST");
	conn.setRequestProperty("Content-Type", "application/json");
	OutputStream os = conn.getOutputStream();
	os.write(objJson.toString().getBytes());
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
	System.out.println("Void Bill Hd= " + op);
	conn.disconnect();
	if (op.equals("true"))
	{
	    StringBuilder sbUpdate = new StringBuilder(updateBills);
	    if (updateBills.length() > 0)
	    {
		updateBills = sbUpdate.delete(0, 1).toString();
		//System.out.println("Billsss="+updateBills);
		dbMysql.execute("update tblvoidbillhd set strDataPostFlag='Y' where strBillNo in (" + updateBills + ")");
	    }
	}
	return 1;
    }

    private int funPostVoidBillDtlData() throws Exception
    {
	String updateBills = "";
	String query = "select * from tblvoidbilldtl where strDataPostFlag='N' ";
	ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	JSONObject objJson = new JSONObject();
	JSONArray arrObj = new JSONArray();

	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateBills += ",'" + rs.getString("strBillNo") + "'";
	    objRows.put("BillNo", rs.getString("strBillNo"));
	    objRows.put("PosCode", rs.getString("strPosCode"));
	    objRows.put("ReasonCode", rs.getString("strReasonCode"));
	    objRows.put("ReasonName", rs.getString("strReasonName"));
	    objRows.put("ItemCode", rs.getString("strItemCode"));
	    objRows.put("ItemName", rs.getString("strItemCode"));
	    objRows.put("Quantity", rs.getString("intQuantity"));
	    objRows.put("Amount", rs.getString("dblAmount"));
	    objRows.put("TaxAmount", rs.getString("dblTaxAmount"));
	    objRows.put("BillDate", rs.getString("dteBillDate"));
	    objRows.put("TransType", rs.getString("strTransType"));
	    objRows.put("ModifyVoidBill", rs.getString("dteModifyVoidBill"));
	    objRows.put("SettlementCode", rs.getString("strSettlementCode"));
	    objRows.put("SettlementAmt", rs.getString("dblSettlementAmt"));
	    objRows.put("PaidAmt", rs.getString("dblPaidAmt"));
	    objRows.put("TableNo", rs.getString("strTableNo"));
	    objRows.put("WaiterNo", rs.getString("strWaiterNo"));
	    objRows.put("ShiftCode", rs.getString("intShiftCode"));
	    objRows.put("UserCreated", rs.getString("strUserCreated"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));
	    objRows.put("KOTNo", rs.getString("strKOTNo"));
	    objRows.put("strRemarks", rs.getString("strRemarks"));

	    arrObj.add(objRows);
	}

	rs.close();
	objJson.put("VoidBillDtl", arrObj);
	String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostTransactionDataToHOPOS";

	URL url = new URL(hoURL);
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setDoOutput(true);
	conn.setRequestMethod("POST");
	conn.setRequestProperty("Content-Type", "application/json");
	OutputStream os = conn.getOutputStream();
	os.write(objJson.toString().getBytes());
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
	System.out.println("Void Bill Dtl= " + op);
	conn.disconnect();
	if (op.equals("true"))
	{
	    StringBuilder sbUpdate = new StringBuilder(updateBills);
	    if (updateBills.length() > 0)
	    {
		updateBills = sbUpdate.delete(0, 1).toString();
		//System.out.println("Billsss="+updateBills);
		dbMysql.execute("update tblvoidbilldtl set strDataPostFlag='Y' where strBillNo in (" + updateBills + ")");
	    }
	}
	return 1;
    }

    private int funPostVoidBillModifierDtlData() throws Exception
    {
	String updateBills = "";
	String query = "select * from tblvoidmodifierdtl where strDataPostFlag='N' limit 1000 ";
	ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	JSONObject objJson = new JSONObject();
	JSONArray arrObj = new JSONArray();

	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateBills += ",'" + rs.getString("strBillNo") + "'";

	    objRows.put("BillNo", rs.getString("strBillNo"));
	    objRows.put("ItemCode", rs.getString("strItemCode"));
	    objRows.put("ModifierCode", rs.getString("strModifierCode"));
	    objRows.put("ModifierName", rs.getString("strModifierName"));
	    objRows.put("Quantity", rs.getString("dblQuantity"));
	    objRows.put("Amount", rs.getString("dblAmount"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("CustomerCode", rs.getString("strCustomerCode"));
	    objRows.put("Remarks", rs.getString("strRemarks"));
	    objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));
	    objRows.put("ReasonCode", rs.getString("strReasonCode"));
	    objRows.put("dteBillDate", rs.getString("dteBillDate"));
	    arrObj.add(objRows);
	}

	rs.close();
	objJson.put("VoidBillModifierDtl", arrObj);
	String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostTransactionDataToHOPOS";

	URL url = new URL(hoURL);
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setDoOutput(true);
	conn.setRequestMethod("POST");
	conn.setRequestProperty("Content-Type", "application/json");
	OutputStream os = conn.getOutputStream();
	os.write(objJson.toString().getBytes());
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
	System.out.println("Void Mod Bill Dtl= " + op);
	conn.disconnect();
	if (op.equals("true"))
	{
	    StringBuilder sbUpdate = new StringBuilder(updateBills);
	    if (updateBills.length() > 0)
	    {
		updateBills = sbUpdate.delete(0, 1).toString();
		//System.out.println("Billsss="+updateBills);
		dbMysql.execute("update tblvoidmodifierdtl set strDataPostFlag='Y' "
			+ " where strBillNo in (" + updateBills + ")");
	    }
	}
	return 1;
    }

    private int funPostVoidKOTData() throws Exception
    {
	String updateKOT = "";
	String query = "select * from tblvoidkot where strDataPostFlag='N' ";
	ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	JSONObject objJson = new JSONObject();
	JSONArray arrObj = new JSONArray();

	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateKOT += ",'" + rs.getString("strKOTNo") + "'";
	    objRows.put("KOTNo", rs.getString("strKOTNo"));
	    objRows.put("TableNo", rs.getString("strTableNo"));
	    objRows.put("POSCode", rs.getString("strPOSCode"));
	    objRows.put("ItemCode", rs.getString("strItemCode"));
	    objRows.put("ItemName", rs.getString("strItemCode"));
	    objRows.put("ItemQuantity", rs.getString("dblItemQuantity"));
	    objRows.put("Amount", rs.getString("dblAmount"));
	    objRows.put("WaiterNo", rs.getString("strWaiterNo"));
	    objRows.put("PaxNo", rs.getString("intPaxNo"));
	    objRows.put("Type", rs.getString("strType"));
	    objRows.put("ReasonCode", rs.getString("strReasonCode"));
	    objRows.put("UserCreated", rs.getString("strUserCreated"));
	    objRows.put("DateCreated", rs.getString("dteDateCreated"));
	    objRows.put("VoidedDate", rs.getString("dteVoidedDate"));
	    objRows.put("DataPostFlag", rs.getString("strTableNo"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("ManualKOTNo", rs.getString("strManualKOTNo"));
	    objRows.put("PrintKOT", rs.getString("strPrintKOT"));

	    objRows.put("strRemark", rs.getString("strRemark"));
	    objRows.put("strItemProcessed", rs.getString("strItemProcessed"));
	    objRows.put("strVoidBillType", rs.getString("strVoidBillType"));

	    arrObj.add(objRows);
	}

	rs.close();
	objJson.put("VoidKot", arrObj);
	String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostTransactionDataToHOPOS";

	URL url = new URL(hoURL);
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setDoOutput(true);
	conn.setRequestMethod("POST");
	conn.setRequestProperty("Content-Type", "application/json");
	OutputStream os = conn.getOutputStream();
	os.write(objJson.toString().getBytes());
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
	System.out.println("Void KOT= " + op);
	conn.disconnect();
	if (op.equals("true"))
	{
	    StringBuilder sbUpdate = new StringBuilder(updateKOT);
	    if (updateKOT.length() > 0)
	    {
		updateKOT = sbUpdate.delete(0, 1).toString();
		//System.out.println("Billsss="+updateBills);
		dbMysql.execute("update tblvoidkot set strDataPostFlag='Y' where strKOTNo in (" + updateKOT + ")");
	    }
	}
	return 1;
    }

    private int funPostVoidAdvOrderBillHdData()
    {
	try
	{
	    String updateBills = "";
	    String query = "select * from tblvoidadvbookbillhd where strDataPostFlag='N'";
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	    JSONObject objJson = new JSONObject();

	    JSONArray arrObj = new JSONArray();

	    while (rs.next())
	    {
		JSONObject objRows = new JSONObject();
		updateBills += ",'" + rs.getString("strAdvBookingNo") + "'";

		objRows.put("AdvBookingNo", rs.getString("strAdvBookingNo"));
		objRows.put("AdvBookingDate", rs.getString("dteAdvBookingDate"));
		objRows.put("OrderFor", rs.getString("dteOrderFor"));
		objRows.put("POSCode", rs.getString("strPOSCode"));
		objRows.put("SettelmentMode", rs.getString("strSettelmentMode"));
		objRows.put("DiscountAmt", rs.getString("dblDiscountAmt"));
		objRows.put("DiscountPer", rs.getString("dblDiscountPer"));
		objRows.put("TaxAmt", rs.getString("dblTaxAmt"));
		objRows.put("SubTotal", rs.getString("dblSubTotal"));
		objRows.put("GrandTotal", rs.getString("dblGrandTotal"));

		objRows.put("UserCreated", rs.getString("strUserCreated"));
		objRows.put("UserEdited", rs.getString("strUserEdited"));
		objRows.put("DateCreated", rs.getString("dteDateCreated"));
		objRows.put("DateEdited", rs.getString("dteDateEdited"));

		objRows.put("CustomerCode", rs.getString("strCustomerCode"));
		objRows.put("ShiftCode", rs.getString("intShiftCode"));
		objRows.put("Message", rs.getString("strMessage"));
		objRows.put("Shape", rs.getString("strShape"));

		objRows.put("Note", rs.getString("strNote"));
		objRows.put("DeliveryTime", rs.getString("strDeliveryTime"));
		objRows.put("WaiterNo", rs.getString("strWaiterNo"));
		objRows.put("HomeDelivery", rs.getString("strHomeDelivery"));
		objRows.put("HomeDelCharges", rs.getString("dblHomeDelCharges"));
		objRows.put("OrderType", rs.getString("strOrderType"));
		objRows.put("ManualAdvOrderNo", rs.getString("strManualAdvOrderNo"));
		objRows.put("ImageName", rs.getString("strImageName"));
		objRows.put("SpecialsymbolImage", rs.getString("strSpecialsymbolImage"));

		objRows.put("ClientCode", rs.getString("strClientCode"));
		objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));
		objRows.put("UrgentOrder", rs.getString("strUrgentOrder"));
		objRows.put("ReasonCode", rs.getString("strReasonCode"));
		objRows.put("Remark", rs.getString("strRemark"));

		arrObj.add(objRows);
	    }
	    rs.close();

	    objJson.put("VoidAdvOrderBillHd", arrObj);
	    String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostTransactionDataToHOPOS";

	    URL url = new URL(hoURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    OutputStream os = conn.getOutputStream();
	    os.write(objJson.toString().getBytes());
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
	    System.out.println("Void Adv Order Bill Hd= " + op);
	    conn.disconnect();

	    if (op.equals("true"))
	    {
		StringBuilder sbUpdate = new StringBuilder(updateBills);
		if (updateBills.length() > 0)
		{
		    updateBills = sbUpdate.delete(0, 1).toString();
		    //System.out.println("Billsss="+updateBills);
		    dbMysql.execute("update tblvoidadvbookbillhd set strDataPostFlag='Y' "
			    + "where strAdvBookingNo in (" + updateBills + ")");
		}
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    return 1;
	}
    }

    private int funPostVoidAdvOrderBillDtlData()
    {
	try
	{
	    String updateBills = "";
	    String query = "select * from tblvoidadvbookbilldtl where strDataPostFlag='N'";
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	    JSONObject objJson = new JSONObject();

	    JSONArray arrObj = new JSONArray();

	    while (rs.next())
	    {
		JSONObject objRows = new JSONObject();
		updateBills += ",'" + rs.getString("strAdvBookingNo") + "'";

		objRows.put("AdvBookingNo", rs.getString("strAdvBookingNo"));
		objRows.put("ItemCode", rs.getString("strItemCode"));
		objRows.put("ItemName", rs.getString("strItemName"));
		objRows.put("Quantity", rs.getString("dblQuantity"));
		objRows.put("Weight", rs.getString("dblWeight"));
		objRows.put("Amount", rs.getString("dblAmount"));
		objRows.put("TaxAmount", rs.getString("dblTaxAmount"));
		objRows.put("AdvBookingDate", rs.getString("dteAdvBookingDate"));
		objRows.put("OrderFor", rs.getString("dteOrderFor"));
		objRows.put("CustomerCode", rs.getString("strCustomerCode"));
		objRows.put("ClientCode", rs.getString("strClientCode"));
		objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));

		arrObj.add(objRows);
	    }
	    rs.close();

	    objJson.put("VoidAdvOrderBillDtl", arrObj);
	    String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostTransactionDataToHOPOS";

	    URL url = new URL(hoURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    OutputStream os = conn.getOutputStream();
	    os.write(objJson.toString().getBytes());
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
	    System.out.println("Void Adv Order Bill Dtl= " + op);
	    conn.disconnect();

	    if (op.equals("true"))
	    {
		StringBuilder sbUpdate = new StringBuilder(updateBills);
		if (updateBills.length() > 0)
		{
		    updateBills = sbUpdate.delete(0, 1).toString();
		    //System.out.println("Billsss="+updateBills);
		    dbMysql.execute("update tblvoidadvbookbilldtl set strDataPostFlag='Y' where strAdvBookingNo in (" + updateBills + ")");
		}
	    }

	    return 1;
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    return 1;
	}
    }

    private int funPostVoidAdvOrderModifierDtlData()
    {
	try
	{
	    String updateBills = "";
	    String query = "select * from tblvoidadvordermodifierdtl where strDataPostFlag='N'";
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	    JSONObject objJson = new JSONObject();
	    JSONArray arrObj = new JSONArray();

	    while (rs.next())
	    {
		JSONObject objRows = new JSONObject();
		updateBills += ",'" + rs.getString("strAdvOrderNo") + "'";

		objRows.put("AdvBookingNo", rs.getString("strAdvOrderNo"));
		objRows.put("ItemCode", rs.getString("strItemCode"));
		objRows.put("ModifierCode", rs.getString("strModifierCode"));
		objRows.put("ModifierName", rs.getString("strModifierName"));
		objRows.put("Quantity", rs.getString("dblQuantity"));
		objRows.put("Amount", rs.getString("dblAmount"));
		objRows.put("ClientCode", rs.getString("strClientCode"));
		objRows.put("CustomerCode", rs.getString("strCustomerCode"));
		objRows.put("UserCreated", rs.getString("strUserCreated"));
		objRows.put("UserEdited", rs.getString("strUserEdited"));
		objRows.put("DateCreated", rs.getString("dteDateCreated"));
		objRows.put("DateEdited", rs.getString("dteDateEdited"));
		objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));

		arrObj.add(objRows);
	    }
	    rs.close();

	    objJson.put("VoidAdvOrderModifierDtl", arrObj);
	    String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostTransactionDataToHOPOS";

	    URL url = new URL(hoURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    OutputStream os = conn.getOutputStream();
	    os.write(objJson.toString().getBytes());
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
	    System.out.println("Void Adv Order Mod Dtl= " + op);
	    conn.disconnect();

	    if (op.equals("true"))
	    {
		StringBuilder sbUpdate = new StringBuilder(updateBills);
		if (updateBills.length() > 0)
		{
		    updateBills = sbUpdate.delete(0, 1).toString();
		    //System.out.println("Billsss="+updateBills);
		    dbMysql.execute("update tblvoidadvordermodifierdtl set strDataPostFlag='Y' where strAdvOrderNo in (" + updateBills + ")");
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    return 1;
	}
    }

    private int funPostVoidAdvOrderReceiptHdData()
    {
	try
	{
	    String updateBills = "";
	    String query = "select * from tblvoidadvancereceipthd where strDataPostFlag='N'";

	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	    JSONObject objJson = new JSONObject();

	    JSONArray arrObj = new JSONArray();

	    while (rs.next())
	    {
		JSONObject objRows = new JSONObject();
		updateBills += ",'" + rs.getString("strReceiptNo") + "'";

		objRows.put("ReceiptNo", rs.getString("strReceiptNo"));
		objRows.put("AdvBookingNo", rs.getString("strAdvBookingNo"));
		objRows.put("POSCode", rs.getString("strPOSCode"));
		objRows.put("SettelmentMode", rs.getString("strSettelmentMode"));
		objRows.put("ReceiptDate", rs.getString("dtReceiptDate"));
		objRows.put("AdvDeposite", rs.getString("dblAdvDeposite"));
		objRows.put("ShiftCode", rs.getString("intShiftCode"));
		objRows.put("UserCreated", rs.getString("strUserCreated"));
		objRows.put("UserEdited", rs.getString("strUserEdited"));
		objRows.put("DateCreated", rs.getString("dtDateCreated"));
		objRows.put("DateEdited", rs.getString("dtDateEdited"));
		objRows.put("ClientCode", rs.getString("strClientCode"));
		objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));

		arrObj.add(objRows);
	    }
	    rs.close();

	    objJson.put("VoidAdvOrderReceiptHd", arrObj);
	    String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostTransactionDataToHOPOS";

	    URL url = new URL(hoURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    OutputStream os = conn.getOutputStream();
	    os.write(objJson.toString().getBytes());
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
	    System.out.println("Void Adv Order Rec Hd= " + op);
	    conn.disconnect();

	    if (op.equals("true"))
	    {
		StringBuilder sbUpdate = new StringBuilder(updateBills);
		if (updateBills.length() > 0)
		{
		    updateBills = sbUpdate.delete(0, 1).toString();
		    //System.out.println("Billsss="+updateBills);
		    dbMysql.execute("update tblvoidadvancereceipthd set strDataPostFlag='Y' where strReceiptNo in (" + updateBills + ")");
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    return 1;
	}
    }

    private int funPostVoidAdvOrderReceiptDtlData()
    {
	try
	{
	    String updateBills = "";
	    String query = "select * from tblvoidadvancereceiptdtl where strDataPostFlag='N'";
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	    JSONObject objJson = new JSONObject();
	    JSONArray arrObj = new JSONArray();

	    while (rs.next())
	    {
		JSONObject objRows = new JSONObject();
		updateBills += ",'" + rs.getString("strReceiptNo") + "'";

		objRows.put("ReceiptNo", rs.getString("strReceiptNo"));
		objRows.put("SettlementCode", rs.getString("strSettlementCode"));
		objRows.put("CardNo", rs.getString("strCardNo"));
		objRows.put("Expirydate", rs.getString("strExpirydate"));
		objRows.put("ChequeNo", rs.getString("strChequeNo"));
		objRows.put("ChequeDate", rs.getString("dteCheque"));
		objRows.put("BankName", rs.getString("strBankName"));
		objRows.put("AdvDepositesettleAmt", rs.getString("dblAdvDepositesettleAmt"));
		objRows.put("Remark", rs.getString("strRemark"));
		objRows.put("PaidAmt", rs.getString("dblPaidAmt"));
		objRows.put("Installment", rs.getString("dteInstallment"));
		objRows.put("ClientCode", rs.getString("strClientCode"));
		objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));

		arrObj.add(objRows);
	    }
	    rs.close();

	    objJson.put("VoidAdvOrderReceiptDtl", arrObj);
	    String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostTransactionDataToHOPOS";

	    URL url = new URL(hoURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    OutputStream os = conn.getOutputStream();
	    os.write(objJson.toString().getBytes());
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
	    System.out.println("Void Adv Order Rec Dtl= " + op);
	    conn.disconnect();

	    if (op.equals("true"))
	    {
		StringBuilder sbUpdate = new StringBuilder(updateBills);
		if (updateBills.length() > 0)
		{
		    updateBills = sbUpdate.delete(0, 1).toString();
		    //System.out.println("Billsss="+updateBills);
		    dbMysql.execute("update tblvoidadvancereceiptdtl set strDataPostFlag='Y' where strReceiptNo in (" + updateBills + ")");
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    return 1;
	}
    }

    private int funPostVoidAdvOrderBillCharDtlData()
    {
	try
	{
	    String updateItems = "", updateAdvOrderNo = "";
	    String query = "select * from tblvoidadvbookbillchardtl where strDataPostFlag='N'";
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	    JSONObject objJson = new JSONObject();
	    JSONArray arrObj = new JSONArray();

	    while (rs.next())
	    {
		JSONObject objRows = new JSONObject();
		updateItems += ",'" + rs.getString("strItemCode") + "'";
		updateAdvOrderNo += ",'" + rs.getString("strAdvBookingNo") + "'";

		objRows.put("ItemCode", rs.getString("strItemCode"));
		objRows.put("AdvBookingNo", rs.getString("strAdvBookingNo"));
		objRows.put("CharCode", rs.getString("strCharCode"));
		objRows.put("CharValues", rs.getString("strCharValues"));
		objRows.put("ClientCode", rs.getString("strClientCode"));
		objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));

		arrObj.add(objRows);
	    }
	    rs.close();

	    objJson.put("VoidAdvOrderBillCharDtl", arrObj);
	    String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostTransactionDataToHOPOS";

	    URL url = new URL(hoURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    OutputStream os = conn.getOutputStream();
	    os.write(objJson.toString().getBytes());
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
	    System.out.println("Void Adv Order Bill Char Dtl= " + op);
	    conn.disconnect();

	    if (op.equals("true"))
	    {
		StringBuilder sbUpdate = new StringBuilder(updateItems);
		StringBuilder sbUpdateAdvOrderNo = new StringBuilder(updateAdvOrderNo);
		if (updateItems.length() > 0)
		{
		    updateItems = sbUpdate.delete(0, 1).toString();
		    updateAdvOrderNo = sbUpdateAdvOrderNo.delete(0, 1).toString();
		    //System.out.println("Billsss="+updateBills);
		    dbMysql.execute("update tblvoidadvbookbillchardtl set strDataPostFlag='Y' "
			    + " where strItemCode in (" + updateItems + ") and strAdvBookingNo in(" + updateAdvOrderNo + ") ");
		    System.out.println("update tblvoidadvbookbillchardtl set strDataPostFlag='Y' "
			    + " where strItemCode in (" + updateItems + ") and strAdvBookingNo in(" + updateAdvOrderNo + ") ");
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    return 1;
	}
    }

    public int funPostInventoryDataToHO(String formName)
    {
	try
	{
	    funPostStockInHdData();
	    funPostStockInDtlData();
	    funPostStockOutHdData();
	    funPostStockOutDtlData();
	    funPostPhysicalStockHdData();
	    funPostPhysicalStockDtlData();

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return 1;
    }

    private int funPostStockInHdData() throws Exception
    {
	String updateStockNo = "";
	String query = "select * from tblstkinhd where strDataPostFlag='N'";
	ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	JSONObject objJson = new JSONObject();

	JSONArray arrObj = new JSONArray();

	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateStockNo += ",'" + rs.getString("strStkInCode") + "'";

	    objRows.put("StkInCode", rs.getString("strStkInCode"));
	    objRows.put("POSCode", rs.getString("strPOSCode"));
	    objRows.put("StkInDate", rs.getString("dteStkInDate"));
	    objRows.put("ReasonCode", rs.getString("strReasonCode"));
	    objRows.put("PurchaseBillNo", rs.getString("strPurchaseBillNo"));
	    objRows.put("PurchaseBillDate", rs.getString("dtePurchaseBillDate"));
	    objRows.put("ShiftCode", rs.getString("intShiftCode"));
	    objRows.put("UserCreated", rs.getString("strUserCreated"));
	    objRows.put("UserEdited", rs.getString("strUserEdited"));
	    objRows.put("DateCreated", rs.getString("dteDateCreated"));
	    objRows.put("DateEdited", rs.getString("dteDateEdited"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));

	    arrObj.add(objRows);
	}

	rs.close();
	objJson.put("StkInHd", arrObj);
	String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostTransactionDataToHOPOS";

	URL url = new URL(hoURL);
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setDoOutput(true);
	conn.setRequestMethod("POST");
	conn.setRequestProperty("Content-Type", "application/json");
	OutputStream os = conn.getOutputStream();
	os.write(objJson.toString().getBytes());
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
	System.out.println("Stk In Hd= " + op);
	conn.disconnect();

	if (op.equals("true"))
	{
	    StringBuilder sbUpdate = new StringBuilder(updateStockNo);
	    if (updateStockNo.length() > 0)
	    {
		updateStockNo = sbUpdate.delete(0, 1).toString();
		//System.out.println("Billsss="+updateBills);
		dbMysql.execute("update tblstkinhd set strDataPostFlag='Y' where strStkInCode in (" + updateStockNo + ")");
	    }
	}

	return 1;
    }

    private int funPostStockInDtlData() throws Exception
    {
	String updateBills = "";
	String query = "select * from tblstkindtl where strDataPostFlag='N'";
	ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	JSONObject objJson = new JSONObject();

	JSONArray arrObj = new JSONArray();

	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateBills += ",'" + rs.getString("strStkInCode") + "'";

	    objRows.put("StkInCode", rs.getString("strStkInCode"));
	    objRows.put("ItemCode", rs.getString("strItemCode"));
	    objRows.put("Quantity", rs.getString("dblQuantity"));
	    objRows.put("PurchaseRate", rs.getString("dblPurchaseRate"));
	    objRows.put("Amount", rs.getString("dblAmount"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));

	    arrObj.add(objRows);
	}

	rs.close();
	objJson.put("StkInDtl", arrObj);
	String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostTransactionDataToHOPOS";

	URL url = new URL(hoURL);
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setDoOutput(true);
	conn.setRequestMethod("POST");
	conn.setRequestProperty("Content-Type", "application/json");
	OutputStream os = conn.getOutputStream();
	os.write(objJson.toString().getBytes());
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
	System.out.println("Stk In Dtl= " + op);
	conn.disconnect();

	if (op.equals("true"))
	{
	    StringBuilder sbUpdate = new StringBuilder(updateBills);
	    if (updateBills.length() > 0)
	    {
		updateBills = sbUpdate.delete(0, 1).toString();
		//System.out.println("Billsss="+updateBills);
		dbMysql.execute("update tblstkindtl set strDataPostFlag='Y' where strStkInCode in (" + updateBills + ")");
	    }
	}

	return 1;
    }

    private int funPostStockOutHdData() throws Exception
    {
	String updateStockNo = "";
	String query = "select * from tblstkouthd where strDataPostFlag='N'";
	ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	JSONObject objJson = new JSONObject();

	JSONArray arrObj = new JSONArray();

	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateStockNo += ",'" + rs.getString("strStkOutCode") + "'";

	    objRows.put("StkOutCode", rs.getString("strStkOutCode"));
	    objRows.put("POSCode", rs.getString("strPOSCode"));
	    objRows.put("StkOutDate", rs.getString("dteStkOutDate"));
	    objRows.put("ReasonCode", rs.getString("strReasonCode"));
	    objRows.put("PurchaseBillNo", rs.getString("strPurchaseBillNo"));
	    objRows.put("PurchaseBillDate", rs.getString("dtePurchaseBillDate"));
	    objRows.put("ShiftCode", rs.getString("intShiftCode"));
	    objRows.put("UserCreated", rs.getString("strUserCreated"));
	    objRows.put("UserEdited", rs.getString("strUserEdited"));
	    objRows.put("DateCreated", rs.getString("dteDateCreated"));
	    objRows.put("DateEdited", rs.getString("dteDateEdited"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));

	    arrObj.add(objRows);
	}

	rs.close();
	objJson.put("StkOutHd", arrObj);
	String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostTransactionDataToHOPOS";

	URL url = new URL(hoURL);
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setDoOutput(true);
	conn.setRequestMethod("POST");
	conn.setRequestProperty("Content-Type", "application/json");
	OutputStream os = conn.getOutputStream();
	os.write(objJson.toString().getBytes());
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
	System.out.println("Stk Out Hd= " + op);
	conn.disconnect();

	if (op.equals("true"))
	{
	    StringBuilder sbUpdate = new StringBuilder(updateStockNo);
	    if (updateStockNo.length() > 0)
	    {
		updateStockNo = sbUpdate.delete(0, 1).toString();
		//System.out.println("Billsss="+updateBills);
		dbMysql.execute("update tblstkouthd set strDataPostFlag='Y' where strStkOutCode in (" + updateStockNo + ")");
	    }
	}

	return 1;
    }

    private int funPostStockOutDtlData() throws Exception
    {
	String updateStockNo = "";
	String query = "select * from tblstkoutdtl where strDataPostFlag='N'";
	ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	JSONObject objJson = new JSONObject();
	JSONArray arrObj = new JSONArray();

	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateStockNo += ",'" + rs.getString("strStkOutCode") + "'";

	    objRows.put("StkOutCode", rs.getString("strStkOutCode"));
	    objRows.put("ItemCode", rs.getString("strItemCode"));
	    objRows.put("Quantity", rs.getString("dblQuantity"));
	    objRows.put("PurchaseRate", rs.getString("dblPurchaseRate"));
	    objRows.put("Amount", rs.getString("dblAmount"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));

	    arrObj.add(objRows);
	}

	rs.close();
	objJson.put("StkOutDtl", arrObj);
	String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostTransactionDataToHOPOS";

	URL url = new URL(hoURL);
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setDoOutput(true);
	conn.setRequestMethod("POST");
	conn.setRequestProperty("Content-Type", "application/json");
	OutputStream os = conn.getOutputStream();
	os.write(objJson.toString().getBytes());
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
	System.out.println("Stk Out Dtl= " + op);
	conn.disconnect();

	if (op.equals("true"))
	{
	    StringBuilder sbUpdate = new StringBuilder(updateStockNo);
	    if (updateStockNo.length() > 0)
	    {
		updateStockNo = sbUpdate.delete(0, 1).toString();
		//System.out.println("Billsss="+updateBills);
		dbMysql.execute("update tblstkoutdtl set strDataPostFlag='Y' where strStkOutCode in (" + updateStockNo + ")");
	    }
	}

	return 1;
    }

    private int funPostPhysicalStockHdData() throws Exception
    {
	String updatePhyStkNo = "";
	String query = "select * from tblpsphd where strDataPostFlag='N'";
	ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	JSONObject objJson = new JSONObject();
	JSONArray arrObj = new JSONArray();
	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updatePhyStkNo += ",'" + rs.getString("strPSPCode") + "'";

	    objRows.put("PSPCode", rs.getString("strPSPCode"));
	    objRows.put("POSCode", rs.getString("strPOSCode"));
	    objRows.put("StkInCode", rs.getString("strStkInCode"));
	    objRows.put("StkOutCode", rs.getString("strStkOutCode"));
	    objRows.put("BillNo", rs.getString("strBillNo"));
	    objRows.put("StkInAmt", rs.getString("dblStkInAmt"));
	    objRows.put("SaleAmt", rs.getString("dblSaleAmt"));
	    objRows.put("ShiftCode", rs.getString("intShiftCode"));
	    objRows.put("UserCreated", rs.getString("strUserCreated"));
	    objRows.put("UserEdited", rs.getString("strUserEdited"));
	    objRows.put("DateCreated", rs.getString("dteDateCreated"));
	    objRows.put("DateEdited", rs.getString("dteDateEdited"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));
	    objRows.put("ReasonCode", rs.getString("strReasonCode"));
	    objRows.put("Remarks", rs.getString("strRemarks"));

	    arrObj.add(objRows);
	}

	rs.close();
	objJson.put("PspHd", arrObj);
	String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostTransactionDataToHOPOS";

	URL url = new URL(hoURL);
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setDoOutput(true);
	conn.setRequestMethod("POST");
	conn.setRequestProperty("Content-Type", "application/json");
	OutputStream os = conn.getOutputStream();
	os.write(objJson.toString().getBytes());
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
	System.out.println("PSP Hd= " + op);
	conn.disconnect();

	if (op.equals("true"))
	{
	    StringBuilder sbUpdate = new StringBuilder(updatePhyStkNo);
	    if (updatePhyStkNo.length() > 0)
	    {
		updatePhyStkNo = sbUpdate.delete(0, 1).toString();
		//System.out.println("Billsss="+updateBills);
		dbMysql.execute("update tblpsphd set strDataPostFlag='Y' where strPSPCode in (" + updatePhyStkNo + ")");
	    }
	}

	return 1;
    }

    private int funPostPhysicalStockDtlData() throws Exception
    {
	String updatePhyStkNo = "";
	String query = "select * from tblpspdtl where strDataPostFlag='N'";
	ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	JSONObject objJson = new JSONObject();
	JSONArray arrObj = new JSONArray();

	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updatePhyStkNo += ",'" + rs.getString("strPSPCode") + "'";

	    objRows.put("PSPCode", rs.getString("strPSPCode"));
	    objRows.put("ItemCode", rs.getString("strItemCode"));
	    objRows.put("PhyStk", rs.getString("dblPhyStk"));
	    objRows.put("CompStk", rs.getString("dblCompStk"));
	    objRows.put("Variance", rs.getString("dblVariance"));
	    objRows.put("VarianceAmt", rs.getString("dblVairanceAmt"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));

	    arrObj.add(objRows);
	}

	rs.close();
	objJson.put("PspDtl", arrObj);
	String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostTransactionDataToHOPOS";

	URL url = new URL(hoURL);
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setDoOutput(true);
	conn.setRequestMethod("POST");
	conn.setRequestProperty("Content-Type", "application/json");
	OutputStream os = conn.getOutputStream();
	os.write(objJson.toString().getBytes());
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
	System.out.println("PSP Dtl= " + op);
	conn.disconnect();

	if (op.equals("true"))
	{
	    StringBuilder sbUpdate = new StringBuilder(updatePhyStkNo);
	    if (updatePhyStkNo.length() > 0)
	    {
		updatePhyStkNo = sbUpdate.delete(0, 1).toString();
		//System.out.println("Billsss="+updateBills);
		dbMysql.execute("update tblpspdtl set strDataPostFlag='Y' where strPSPCode in (" + updatePhyStkNo + ")");
	    }
	}

	return 1;
    }

    public void funFetchUpdatedMasterDataFromHO()
    {
	int val = 0;
	try
	{
	    if (!gHOPOSType.equals("Client POS"))
	    {
		return;
	    }

	    System.out.println("Last Mod Date=" + gLastModifiedDate);
	    gLastModifiedDate = gLastModifiedDate.replaceAll(" ", "%20");

	    String fetchMasterURL = gSanguineWebServiceURL + "/POSIntegration/funGetEditedMasterList?strLastModifiedDate=" + gLastModifiedDate;
	    URL url = new URL(fetchMasterURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setRequestMethod("GET");
	    conn.setRequestProperty("Accept", "application/json");
	    BufferedReader br = new BufferedReader(new InputStreamReader(
		    (conn.getInputStream())));
	    String output = "", jsonString = "";
	    while ((output = br.readLine()) != null)
	    {
		jsonString += output;
	    }

	    JSONParser parser = new JSONParser();
	    Object obj = parser.parse(jsonString);
	    JSONObject jObj = (JSONObject) obj;
	    JSONArray mJsonArray = (JSONArray) jObj.get("updatedmasterlist");
	    for (int i = 0; i < mJsonArray.size(); i++)
	    {
		JSONObject mJsonObject = (JSONObject) mJsonArray.get(i);
		String masterName = mJsonObject.get("TableName").toString();

		switch (masterName)
		{
		    case "Area":
			val += funFetchMasterData("tblareamaster");
			System.out.println("tblareamaster= " + val);
			break;

		    case "AreaWiseDC":
			val += funFetchMasterData("tblareawisedelboywisecharges");
			System.out.println("tblareawisedelboywisecharges= " + val);
			break;

		    case "Building":
			val += funFetchMasterData("tblbuildingmaster");
			System.out.println("tblbuildingmaster= " + val);
			val += funFetchMasterData("tblareawisedc");
			System.out.println("tblareawisedc= " + val);
			break;

		    case "Counter":

			val += funFetchMasterData("tblcounterhd");
			System.out.println("tblcounterhd= " + val);
			val += funFetchMasterData("tblcounterdtl");
			System.out.println("tblcounterdtl= " + val);
			break;

		    case "MenuItemPricing":
			val += funFetchMasterData("tblmenuitempricingdtl");
			System.out.println("tblmenuitempricingdtl= " + val);
			val += funFetchMasterData("tblmenuitempricinghd");
			System.out.println("tblmenuitempricinghd= " + val);
			break;

		    case "MenuItem":

			val += funFetchMasterData("tblitemmaster");
			System.out.println("tblitemmaster= " + val);

			val += funFetchMasterData("tblitemorderingdtl");
			System.out.println("tblitemorderingdtl= " + val);
			val += funFetchMasterData("tblitemcharctersticslinkupdtl");
			System.out.println("tblitemcharctersticslinkupdtl= " + val);
			val += funFetchMasterData("tblitemmasterlinkupdtl");
			System.out.println("tblitemmasterlinkupdtl= " + val);
			break;

		    case "Menu":
			val += funFetchMasterData("tblmenuhd");
			System.out.println("tblmenuhd= " + val);
			break;

		    case "Modifier":
			val += funFetchMasterData("tblmodifiermaster");
			System.out.println("tblmodifiermaster= " + val);
			val += funFetchMasterData("tblmodifiergrouphd");
			System.out.println("tblmodifiergrouphd= " + val);
			val += funFetchMasterData("tblitemmodofier");
			System.out.println("tblitemmodofier= " + val);
			break;

		    case "Group":
			val += funFetchMasterData("tblgrouphd");
			System.out.println("tblgrouphd= " + val);
			break;

		    case "CostCenter":
			val += funFetchMasterData("tblcostcentermaster");
			System.out.println("tblcostcentermaster= " + val);
			break;

		    case "Settlement":
			val += funFetchMasterData("tblsettelmenthd");
			System.out.println("tblsettelmenthd= " + val);
			break;

		    case "Tax":
			val += funFetchMasterData("tbltaxhd");
			System.out.println("tbltaxhd= " + val);
			val += funFetchMasterData("tbltaxposdtl");
			System.out.println("tbltaxposdtl= " + val);
			val += funFetchMasterData("tblsettlementtax");
			System.out.println("tblsettlementtax= " + val);
			val += funFetchMasterData("tbltaxongroup");
			System.out.println("tbltaxongroup= " + val);
			break;

		    case "Table":
			val += funFetchMasterData("tbltablemaster");
			System.out.println("tbltablemaster= " + val);
			break;

		    case "Waiter":
			val += funFetchMasterData("tblwaitermaster");
			System.out.println("tblwaitermaster= " + val);
			break;

		    case "Reason":
			val += funFetchMasterData("tblreasonmaster");
			System.out.println("tblreasonmaster= " + val);
			break;

		    case "Customer":
			val += funFetchMasterData("tblcustomermaster");
			System.out.println("tblcustomermaster= " + val);
			break;

		    case "CustomerType":
			val += funFetchMasterData("tblcustomertypemaster");
			System.out.println("tblcustomertypemaster= " + val);
			break;

		    case "DeliveryBoy":
			val += funFetchMasterData("tbldeliverypersonmaster");
			System.out.println("tbldeliverypersonmaster= " + val);
			break;

		    case "DelBoyCat":

			break;

		    case "AdvanceOrderType":
			val += funFetchMasterData("tbladvanceordertypemaster");
			System.out.println("tbladvanceordertypemaster= " + val);
			break;

		    case "User":
			val += funFetchMasterData("tbluserhd");
			System.out.println("tbluserhd= " + val);
			val += funFetchMasterData("tbluserdtl");
			System.out.println("tbluserdtl= " + val);
			val += funFetchMasterData("tblsuperuserdtl");
			System.out.println("tblsuperuserdtl= " + val);
			break;

		    case "Promotion":
			val += funFetchMasterData("tblpromotionmaster");
			System.out.println("tblpromotionmaster= " + val);
			val += funFetchMasterData("tblpromotiondtl");
			System.out.println("tblpromotiondtl= " + val);
			val += funFetchMasterData("tblbuypromotiondtl");
			System.out.println("tblbuypromotiondtl= " + val);
			val += funFetchMasterData("tblpromotiondaytimedtl");
			System.out.println("tblpromotiondaytimedtl= " + val);
			break;

		    case "Order":
			val += funFetchMasterData("tblordermaster");
			System.out.println("tblordermaster= " + val);
			break;

		    case "Characteristics":
			val += funFetchMasterData("tblcharactersticsmaster");
			System.out.println("tblcharactersticsmaster= " + val);
			val += funFetchMasterData("tblcharvalue");
			System.out.println("tblcharvalue= " + val);
			break;

		    case "SubGroup":
			val += funFetchMasterData("tblsubgrouphd");
			System.out.println("tblsubgrouphd= " + val);
			break;

		    case "Factory":
			val += funFetchMasterData("tblfactorymaster");
			System.out.println("tblfactorymaster= " + val);
			break;

		    case "PosWiseItemWiseIncentive":
			val += funFetchMasterData("tblposwiseitemwiseincentives");
			System.out.println("tblposwiseitemwiseincentives= " + val);
			break;

		    case "DiscountMaster":
			val = funFetchMasterData("tbldischd");
			System.out.println("Discount Master tbldischd= " + val);
			val = funFetchMasterData("tbldiscdtl");
			System.out.println("Discount Master tbldiscdtl= " + val);
			break;

		}
	    }

	    if (val > 0)
	    {
		funUpdateSystemTime(val);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public void funFetchMasterDataFromHO()
    {
	int val = 0;
	try
	{
	    if (!gHOPOSType.equals("Client POS"))
	    {
		return;
	    }

	    System.out.println("Last Mod Date=" + gLastModifiedDate);
	    gLastModifiedDate = gLastModifiedDate.replaceAll(" ", "%20");

	    val += funFetchMasterData("tblmenuitempricingdtl");
	    System.out.println("tblmenuitempricingdtl= " + val);

	    val += funFetchMasterData("tblmenuitempricinghd");
	    System.out.println("tblmenuitempricinghd= " + val);

	    val += funFetchMasterData("tblitemmaster");
	    System.out.println("tblitemmaster= " + val);

	    val += funFetchMasterData("tblsettelmenthd");
	    System.out.println("tblsettelmenthd= " + val);

	    val += funFetchMasterData("tbltaxhd");
	    System.out.println("tbltaxhd= " + val);

	    val += funFetchMasterData("tbltaxposdtl");
	    System.out.println("tbltaxposdtl= " + val);

	    val += funFetchMasterData("tblsettlementtax");
	    System.out.println("tblsettlementtax= " + val);

	    val += funFetchMasterData("tblgrouphd");
	    System.out.println("tblgrouphd= " + val);

	    val += funFetchMasterData("tblsubgrouphd");
	    System.out.println("tblsubgrouphd= " + val);

	    val += funFetchMasterData("tblreasonmaster");
	    System.out.println("tblreasonmaster= " + val);

	    val += funFetchMasterData("tblcostcentermaster");
	    System.out.println("tblcostcentermaster= " + val);

	    val += funFetchMasterData("tblmenuhd");
	    System.out.println("tblmenuhd= " + val);

	    val += funFetchMasterData("tblmodifiermaster");
	    System.out.println("tblmodifiermaster= " + val);

	    val += funFetchMasterData("tblmodifiergrouphd");
	    System.out.println("tblmodifiergrouphd= " + val);

	    val += funFetchMasterData("tblitemmodofier");
	    System.out.println("tblitemmodofier= " + val);

	    val += funFetchMasterData("tbltablemaster");
	    System.out.println("tbltablemaster= " + val);

	    val += funFetchMasterData("tblwaitermaster");
	    System.out.println("tblwaitermaster= " + val);

	    val += funFetchMasterData("tblareamaster");
	    System.out.println("tblareamaster= " + val);

	    val += funFetchMasterData("tblgiftvoucher");
	    System.out.println("tblgiftvoucher= " + val);

	    val += funFetchMasterData("tblbuildingmaster");
	    System.out.println("tblgiftvoucher= " + val);

	    val += funFetchMasterData("tblareawisedc");
	    System.out.println("tblareawisedc= " + val);

	    val += funFetchMasterData("tblcustomermaster");
	    System.out.println("tblcustomermaster= " + val);

	    val += funFetchMasterData("tblcustomertypemaster");
	    System.out.println("tblcustomertypemaster= " + val);

	    val += funFetchMasterData("tbldeliverypersonmaster");
	    System.out.println("tbldeliverypersonmaster= " + val);

	    val += funFetchMasterData("tblareawisedelboywisecharges");
	    System.out.println("tblareawisedelboywisecharges= " + val);

	    val += funFetchMasterData("tblcounterhd");
	    System.out.println("tblcounterhd= " + val);

	    val += funFetchMasterData("tblcounterdtl");
	    System.out.println("tblcounterdtl= " + val);

	    val += funFetchMasterData("tbladvanceordertypemaster");
	    System.out.println("tbladvanceordertypemaster= " + val);

	    val += funFetchMasterData("tbluserhd");
	    System.out.println("tbluserhd= " + val);

	    val += funFetchMasterData("tbluserdtl");
	    System.out.println("tbluserdtl= " + val);

	    val += funFetchMasterData("tblsuperuserdtl");
	    System.out.println("tblsuperuserdtl= " + val);

	    val += funFetchMasterData("tblpromotionmaster");
	    System.out.println("tblpromotionmaster= " + val);

	    val += funFetchMasterData("tblpromotiondtl");
	    System.out.println("tblpromotiondtl= " + val);

	    val += funFetchMasterData("tblbuypromotiondtl");
	    System.out.println("tblbuypromotiondtl= " + val);

	    val += funFetchMasterData("tblpromotiondaytimedtl");
	    System.out.println("tblpromotiondaytimedtl= " + val);

	    val += funFetchMasterData("tblordermaster");
	    System.out.println("tblordermaster= " + val);

	    val += funFetchMasterData("tblitemorderingdtl");
	    System.out.println("tblitemorderingdtl= " + val);

	    val += funFetchMasterData("tblcharactersticsmaster");
	    System.out.println("tblcharactersticsmaster= " + val);

	    val += funFetchMasterData("tblitemcharctersticslinkupdtl");
	    System.out.println("tblitemcharctersticslinkupdtl= " + val);

	    val += funFetchMasterData("tblitemmasterlinkupdtl");
	    System.out.println("tblitemmasterlinkupdtl= " + val);

	    val += funFetchMasterData("tblcharvalue");
	    System.out.println("tblcharvalue= " + val);

	    val += funFetchMasterData("tblrecipehd");
	    System.out.println("tblrecipehd= " + val);

	    val += funFetchMasterData("tblrecipedtl");
	    System.out.println("tblrecipedtl= " + val);

	    val += funFetchMasterData("tblloyaltypointcustomerdtl");
	    System.out.println("tblloyaltypointcustomerdtl= " + val);

	    val += funFetchMasterData("tblloyaltypointmenuhddtl");
	    System.out.println("tblloyaltypointmenuhddtl= " + val);

	    val += funFetchMasterData("tblloyaltypointposdtl");
	    System.out.println("tblloyaltypointposdtl= " + val);

	    val += funFetchMasterData("tblloyaltypointsubgroupdtl");
	    System.out.println("tblloyaltypointsubgroupdtl= " + val);

	    val += funFetchMasterData("tblloyaltypoints");
	    System.out.println("tblloyaltypoints= " + val);

	    val += funFetchMasterData("tblfactorymaster");
	    System.out.println("tblfactorymaster= " + val);

	    if (val > 0)
	    {
		funUpdateSystemTime(val);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private int funInsertMenuItemPricingHdData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblmenuitempricinghd values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tblmenuitempricinghd where strMenuCode='" + mJsonObject.get("MenuCode") + "'";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("PropertyPOSCode") + "','" + mJsonObject.get("MenuCode") + "'"
		    + ",'" + mJsonObject.get("MenuName") + "','" + mJsonObject.get("UserCreated") + "'"
		    + ",'" + mJsonObject.get("UserEdited") + "','" + mJsonObject.get("DateCreated") + "'"
		    + ",'" + mJsonObject.get("DateEdited") + "'),";
	}

	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}

	return retVal;
    }

    private int funInsertMenuItemPricingDtlData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "INSERT INTO tblmenuitempricingdtl (strItemCode,strItemName,strPosCode,strMenuCode,strPopular,strPriceMonday,strPriceTuesday "
		+ ",strPriceWednesday,strPriceThursday,strPriceFriday,strPriceSaturday,strPriceSunday,dteFromDate,dteToDate,tmeTimeFrom "
		+ ",strAMPMFrom,tmeTimeTo,strAMPMTo,strCostCenterCode,strTextColor,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited "
		+ ",strAreaCode,strSubMenuHeadCode,strHourlyPricing,strClientCode) values ";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tblmenuitempricingdtl "
		    + " where strItemCode='" + mJsonObject.get("ItemCode") + "' and strPosCode='" + mJsonObject.get("PropertyPOSCode") + "' "
		    + " and strHourlyPricing='" + mJsonObject.get("HourlyPricing") + "' and strAreaCode='" + mJsonObject.get("AreaCode") + "' and strClientCode='" + gClientCode + "'";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("ItemCode") + "','" + mJsonObject.get("ItemName") + "'"
		    + ",'" + mJsonObject.get("PropertyPOSCode") + "','" + mJsonObject.get("MenuCode") + "'"
		    + ",'" + mJsonObject.get("Popular") + "','" + mJsonObject.get("PriceMonday") + "'"
		    + ",'" + mJsonObject.get("PriceTuesday") + "','" + mJsonObject.get("PriceWenesday") + "'"
		    + ",'" + mJsonObject.get("PriceThursday") + "','" + mJsonObject.get("PriceFriday") + "'"
		    + ",'" + mJsonObject.get("PriceSaturday") + "','" + mJsonObject.get("PriceSunday") + "'"
		    + ",'" + mJsonObject.get("FromDate") + "','" + mJsonObject.get("ToDate") + "'"
		    + ",'" + mJsonObject.get("TimeFrom") + "','" + mJsonObject.get("AMPMFrom") + "'"
		    + ",'" + mJsonObject.get("TimeTo") + "','" + mJsonObject.get("AMPMTo") + "'"
		    + ",'" + mJsonObject.get("CostCenterCode") + "','" + mJsonObject.get("TextColor") + "'"
		    + ",'" + mJsonObject.get("UserCreated") + "','" + mJsonObject.get("UserEdited") + "'"
		    + ",'" + mJsonObject.get("DateCreated") + "','" + mJsonObject.get("DateEdited") + "'"
		    + ",'" + mJsonObject.get("AreaCode") + "','" + mJsonObject.get("SubMenuHeadCode") + "'"
		    + ",'" + mJsonObject.get("HourlyPricing") + "','" + gClientCode + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}
	return retVal;
    }

    private int funInsertItemMasterData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblitemmaster"
		+ "(strItemCode,strItemName,strSubGroupCode,strItemImage,strTaxIndicator,"
		+ " strStockInEnable,dblPurchaseRate,intProcTimeMin,strExternalCode,strItemDetails,"
		+ " strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strClientCode,"
		+ " strDataPostFlag,strItemType,strDiscountApply,strShortName,dblMinLevel,"
		+ " dblMaxLevel,intProcDay,strRawMaterial,dblSalePrice,strItemForSale,"
		+ " strRevenueHead,strItemWeight,strOpenItem,strItemWiseKOTYN,strWSProdCode,"
		+ " strExciseBrandCode,strNoDeliveryDays,intDeliveryDays,dblIncrementalWeight,dblMinWeight,"
		+ " strUrgentOrder,strUOM,imgImage,tmeTargetMiss,strRecipeUOM,dblReceivedConversion,dblRecipeConversion)"
		+ " values ";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tblitemmaster where strItemCode='" + mJsonObject.get("Column1") + "'";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("Column1") + "','" + mJsonObject.get("Column2") + "'"
		    + ",'" + mJsonObject.get("Column3") + "','" + mJsonObject.get("Column4") + "'"
		    + ",'" + mJsonObject.get("Column5") + "','" + mJsonObject.get("Column6") + "'"
		    + ",'" + mJsonObject.get("Column7") + "','" + mJsonObject.get("Column8") + "'"
		    + ",'" + mJsonObject.get("Column9") + "','" + mJsonObject.get("Column10") + "'"
		    + ",'" + mJsonObject.get("Column11") + "','" + mJsonObject.get("Column12") + "'"
		    + ",'" + mJsonObject.get("Column13") + "','" + mJsonObject.get("Column14") + "'"
		    + ",'" + gClientCode + "','" + mJsonObject.get("Column16") + "'"
		    + ",'" + mJsonObject.get("Column17") + "','" + mJsonObject.get("Column18") + "'"
		    + ",'" + mJsonObject.get("Column19") + "','" + mJsonObject.get("Column20") + "'"
		    + ",'" + mJsonObject.get("Column21") + "','" + mJsonObject.get("Column22") + "'"
		    + ",'" + mJsonObject.get("Column23") + "','" + mJsonObject.get("Column24") + "'"
		    + ",'" + mJsonObject.get("Column25") + "','" + mJsonObject.get("Column26") + "'"
		    + ",'" + mJsonObject.get("Column27") + "','" + mJsonObject.get("Column28") + "'"
		    + ",'" + mJsonObject.get("Column29") + "','" + mJsonObject.get("Column30") + "'"
		    + ",'" + mJsonObject.get("Column31") + "','" + mJsonObject.get("Column32") + "'"
		    + ",'" + mJsonObject.get("Column33") + "','" + mJsonObject.get("Column34") + "'"
		    + ",'" + mJsonObject.get("Column35") + "','" + mJsonObject.get("Column36") + "'"
		    + ",'" + mJsonObject.get("Column37") + "','','" + mJsonObject.get("Column39") + "'"//38 for item image ''
		    + ",'" + mJsonObject.get("Column40") + "','" + mJsonObject.get("Column41") + "'"
		    + ",'" + mJsonObject.get("Column42") + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}

	return retVal;
    }

    private int funInsertTaxHdData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tbltaxhd values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tbltaxhd where strTaxCode='" + mJsonObject.get("TaxCode") + "'";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("TaxCode") + "','" + mJsonObject.get("TaxDesc") + "'"
		    + ",'" + mJsonObject.get("TaxOnSP") + "','" + mJsonObject.get("TaxType") + "'"
		    + ",'" + mJsonObject.get("Percent") + "','" + mJsonObject.get("Amount") + "'"
		    + ",'" + mJsonObject.get("ValidFrom") + "','" + mJsonObject.get("ValidTo") + "'"
		    + ",'" + mJsonObject.get("TaxOnGD") + "','" + mJsonObject.get("TaxCalculation") + "'"
		    + ",'" + mJsonObject.get("TaxIndicator") + "','" + mJsonObject.get("TaxRounded") + "'"
		    + ",'" + mJsonObject.get("TaxOnTax") + "','" + mJsonObject.get("TaxOnTaxCode") + "'"
		    + ",'" + mJsonObject.get("UserCreated") + "','" + mJsonObject.get("UserEdited") + "'"
		    + ",'" + mJsonObject.get("DateCreated") + "','" + mJsonObject.get("DateEdited") + "'"
		    + ",'" + mJsonObject.get("AreaCode") + "','" + mJsonObject.get("OperationType") + "'"
		    + ",'" + mJsonObject.get("ItemType") + "','" + gClientCode + "'"
		    + ",'" + mJsonObject.get("DataPostFlag") + "','" + mJsonObject.get("AccountCode") + "'"
		    + ",'" + mJsonObject.get("TaxShortName") + "','" + mJsonObject.get("strBillNote") + "'),";
	}

	if (mJsonArray.size() > 0)
	{
	    System.out.println(insertValues);
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}

	return retVal;
    }

    private int funInsertTaxPOSDtlData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tbltaxposdtl values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    /*deleteSql = "delete from tbltaxposdtl "
             + "where strTaxCode='" + mJsonObject.get("TaxCode") + "' and strPOSCode='"+mJsonObject.get("POSCode")+"' ";
	     */
	    deleteSql = "delete from tbltaxposdtl where strPOSCode='" + mJsonObject.get("POSCode") + "' ";
	    System.out.println(deleteSql);
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("TaxCode") + "'"
		    + ",'" + mJsonObject.get("POSCode") + "'"
		    + ",'" + mJsonObject.get("TaxDesc") + "','" + clsGlobalVarClass.gClientCode + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}

	return retVal;
    }

    private int funInsertTaxSettlementDtlData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblsettlementtax values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tblsettlementtax "
		    + "where strTaxCode='" + mJsonObject.get("TaxCode") + "' and strSettlementCode='" + mJsonObject.get("SettlementCode") + "' ";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("TaxCode") + "','" + mJsonObject.get("SettlementCode") + "'"
		    + ",'" + mJsonObject.get("SettlementName") + "','" + mJsonObject.get("Applicable") + "'"
		    + ",'" + mJsonObject.get("FromDate") + "','" + mJsonObject.get("ToDate") + "'"
		    + ",'" + mJsonObject.get("UserCreated") + "','" + mJsonObject.get("UserEdited") + "'"
		    + ",'" + mJsonObject.get("DateCreated") + "','" + mJsonObject.get("DateEdited") + "','" + clsGlobalVarClass.gClientCode + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    System.out.println(sb);
	    retVal = dbMysql.execute(query);
	}

	return retVal;
    }

    private int funInsertTaxGroupDtlData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tbltaxongroup values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tbltaxongroup "
		    + "where strTaxCode='" + mJsonObject.get("strTaxCode") + "' and strGroupCode='" + mJsonObject.get("strGroupCode") + "' ";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("strTaxCode") + "','" + mJsonObject.get("strGroupCode") + "'"
		    + ",'" + mJsonObject.get("strGroupName") + "','" + mJsonObject.get("strApplicable") + "'"
		    + ",'" + mJsonObject.get("dteFrom") + "','" + mJsonObject.get("dteTo") + "'"
		    + ",'" + mJsonObject.get("strUserCreated") + "','" + mJsonObject.get("strUserEdited") + "'"
		    + ",'" + mJsonObject.get("dteDateCreated") + "','" + mJsonObject.get("dteDateEdited") + "'"
		    + ",'" + clsGlobalVarClass.gClientCode + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    System.out.println(sb);
	    retVal = dbMysql.execute(query);
	}

	return retVal;
    }

    private int funInsertGroupMasterData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblgrouphd values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tblgrouphd where strGroupCode='" + mJsonObject.get("Column1") + "'";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("Column1") + "','" + mJsonObject.get("Column2") + "'"
		    + ",'" + mJsonObject.get("Column3") + "','" + mJsonObject.get("Column4") + "'"
		    + ",'" + mJsonObject.get("Column5") + "','" + mJsonObject.get("Column6") + "'"
		    + ",'" + gClientCode + "','" + mJsonObject.get("Column8") + "','" + mJsonObject.get("Column9") + "'"
		    + ",'" + mJsonObject.get("Column10") + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}

	return retVal;
    }

    private int funInsertSubGroupMasterData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblsubgrouphd values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tblsubgrouphd "
		    + "where strSubGroupCode='" + mJsonObject.get("Column1") + "'";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("Column1") + "','" + mJsonObject.get("Column2") + "'"
		    + ",'" + mJsonObject.get("Column3") + "','" + mJsonObject.get("Column4") + "'"
		    + ",'" + mJsonObject.get("Column5") + "','" + mJsonObject.get("Column6") + "'"
		    + ",'" + mJsonObject.get("Column7") + "','" + gClientCode + "'"
		    + ",'" + mJsonObject.get("Column9") + "','" + mJsonObject.get("Column10") + "'"
		    + ",'" + mJsonObject.get("Column11") + "','" + mJsonObject.get("Column12") + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}

	return retVal;
    }

    private int funInsertSettlementHdData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblsettelmenthd values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tblsettelmenthd "
		    + "where strSettelmentCode='" + mJsonObject.get("Column1") + "'";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("Column1") + "','" + mJsonObject.get("Column2") + "'"
		    + ",'" + mJsonObject.get("Column3") + "','" + mJsonObject.get("Column4") + "'"
		    + ",'" + mJsonObject.get("Column5") + "','" + mJsonObject.get("Column6") + "'"
		    + ",'" + mJsonObject.get("Column7") + "','" + mJsonObject.get("Column8") + "'"
		    + ",'" + mJsonObject.get("Column9") + "','" + mJsonObject.get("Column10") + "'"
		    + ",'" + mJsonObject.get("Column11") + "','" + gClientCode + "'"
		    + ",'" + mJsonObject.get("Column13") + "','" + mJsonObject.get("Column14") + "'"
		    + ",'" + mJsonObject.get("Column15") + "','" + mJsonObject.get("Column16") + "'"
		    + ",'" + mJsonObject.get("Column17") + "','" + mJsonObject.get("Column18") + "','" + mJsonObject.get("Column19") + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}

	return retVal;
    }

    private int funInsertReasonMasterData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblreasonmaster values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tblreasonmaster "
		    + "where strReasonCode='" + mJsonObject.get("Column1") + "'";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("Column1") + "','" + mJsonObject.get("Column2") + "'"
		    + ",'" + mJsonObject.get("Column3") + "','" + mJsonObject.get("Column4") + "'"
		    + ",'" + mJsonObject.get("Column5") + "','" + mJsonObject.get("Column6") + "'"
		    + ",'" + mJsonObject.get("Column7") + "','" + mJsonObject.get("Column8") + "'"
		    + ",'" + mJsonObject.get("Column9") + "','" + mJsonObject.get("Column10") + "'"
		    + ",'" + mJsonObject.get("Column11") + "','" + mJsonObject.get("Column12") + "'"
		    + ",'" + mJsonObject.get("Column13") + "','" + mJsonObject.get("Column14") + "'"
		    + ",'" + mJsonObject.get("Column15") + "','" + mJsonObject.get("Column16") + "'"
		    + ",'" + mJsonObject.get("Column17") + "','" + mJsonObject.get("Column18") + "'"
		    + ",'" + mJsonObject.get("Column19") + "','" + mJsonObject.get("Column20") + "'"
		    + ",'" + gClientCode + "','" + mJsonObject.get("Column22") + "'"
		    + ",'" + mJsonObject.get("Column23") + "','" + mJsonObject.get("Column24") + "'"
		    + ",'" + mJsonObject.get("Column25") + "','" + mJsonObject.get("Column26") + "'"
		    + ",'" + mJsonObject.get("Column27") + "','" + mJsonObject.get("Column28") + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //System.out.println(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}

	return retVal;
    }

    private int funInsertCostCenterMasterData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblcostcentermaster values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tblcostcentermaster "
		    + "where strCostCenterCode='" + mJsonObject.get("Column1") + "'";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("Column1") + "','" + mJsonObject.get("Column2") + "'"
		    + ",'" + mJsonObject.get("Column3") + "','" + mJsonObject.get("Column4") + "'"
		    + ",'" + mJsonObject.get("Column5") + "','" + mJsonObject.get("Column6") + "'"
		    + ",'" + mJsonObject.get("Column7") + "','" + mJsonObject.get("Column8") + "'"
		    + ",'" + mJsonObject.get("Column9") + "','" + gClientCode + "'"
		    + ",'" + mJsonObject.get("Column11") + "','" + mJsonObject.get("Column12") + "'"
		    + ",'" + mJsonObject.get("Column13") + "','" + mJsonObject.get("Column14") + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}

	return retVal;
    }

    private int funInsertMenuHdData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblmenuhd(strMenuCode,strMenuName,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strClientCode"
		+ ",strDataPostFlag,intSequence,strOperational,strImagePath,imgImage)"
		+ " values ";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tblmenuhd "
		    + "where strMenuCode='" + mJsonObject.get("Column1") + "'";
	    dbMysql.execute(deleteSql);

	    byte[] imageBytes = Base64.getMimeDecoder().decode(mJsonObject.get("Column12").toString());

	    //byte[] imageBytes=DatatypeConverter.parseBase64Binary(mJsonObject.get("Column12").toString());
	    insertValues += "('" + mJsonObject.get("Column1") + "','" + mJsonObject.get("Column2") + "'"
		    + ",'" + mJsonObject.get("Column3") + "','" + mJsonObject.get("Column4") + "'"
		    + ",'" + mJsonObject.get("Column5") + "','" + mJsonObject.get("Column6") + "'"
		    + ",'" + gClientCode + "','" + mJsonObject.get("Column8") + "'"
		    + ",'" + mJsonObject.get("Column9") + "','" + mJsonObject.get("Column10") + "'"
		    + ",'" + mJsonObject.get("Column11") + "','" + imageBytes + "'),";

	    String query2 = "insert into tblmenuhd values(?,?,?,?,?,?,?,?,?,?,?,? )";
	    PreparedStatement pre = clsGlobalVarClass.conPrepareStatement.prepareStatement(query2);
	    pre.setString(1, mJsonObject.get("Column1").toString());
	    pre.setString(2, mJsonObject.get("Column2").toString());
	    pre.setString(3, mJsonObject.get("Column3").toString());
	    pre.setString(4, mJsonObject.get("Column4").toString());
	    pre.setString(5, mJsonObject.get("Column5").toString());
	    pre.setString(6, mJsonObject.get("Column6").toString());
	    pre.setString(7, gClientCode);
	    pre.setString(8, mJsonObject.get("Column8").toString());
	    pre.setInt(9, Integer.parseInt(mJsonObject.get("Column9").toString()));
	    pre.setString(10, mJsonObject.get("Column10").toString());
	    pre.setString(11, mJsonObject.get("Column11").toString());
	    pre.setBytes(12, imageBytes);

	    retVal = pre.executeUpdate();
	    pre.close();

	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();

	    //retVal = dbMysql.execute(query);
	}

	return retVal;
    }

    private int funInsertItemModifierData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblitemmodofier values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";
	Map<String, String> hmItemModifierData = new HashMap<String, String>();

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tblitemmodofier "
		    + "where strModifierCode='" + mJsonObject.get("ModifierCode") + "' "
		    + "and strItemCode='" + mJsonObject.get("ItemCode") + "' ";
	    dbMysql.execute(deleteSql);

	    String key = mJsonObject.get("ModifierCode") + "," + mJsonObject.get("ItemCode");
	    if (null == hmItemModifierData.get(key))
	    {
		insertValues += "('" + mJsonObject.get("ItemCode") + "','" + mJsonObject.get("ModifierCode") + "'"
			+ ",'" + mJsonObject.get("Chargable") + "','" + mJsonObject.get("Rate") + "'"
			+ ",'" + mJsonObject.get("Applicable") + "','" + mJsonObject.get("DefaultModifier") + "'),";
		hmItemModifierData.put(key, mJsonObject.get("ItemCode").toString());
	    }
	}
	hmItemModifierData = null;
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    System.out.println(query);
	    retVal = dbMysql.execute(query);
	}

	return retVal;
    }

    private int funInsertModifierMasterData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblmodifiermaster values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tblmodifiermaster "
		    + "where strModifierCode='" + mJsonObject.get("Column1") + "'";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("Column1") + "','" + mJsonObject.get("Column2") + "'"
		    + ",'" + mJsonObject.get("Column3") + "','" + mJsonObject.get("Column4") + "'"
		    + ",'" + mJsonObject.get("Column5") + "','" + mJsonObject.get("Column6") + "'"
		    + ",'" + mJsonObject.get("Column7") + "','" + gClientCode + "'"
		    + ",'" + mJsonObject.get("Column9") + "','" + mJsonObject.get("Column10") + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}

	return retVal;
    }

    private int funInsertItemModifierGroupHdData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblmodifiergrouphd values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tblmodifiergrouphd "
		    + "where strModifierGroupCode='" + mJsonObject.get("Column1") + "'";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("Column1") + "','" + mJsonObject.get("Column2") + "'"
		    + ",'" + mJsonObject.get("Column3") + "','" + mJsonObject.get("Column4") + "'"
		    + ",'" + mJsonObject.get("Column5") + "','" + mJsonObject.get("Column6") + "'"
		    + ",'" + mJsonObject.get("Column7") + "','" + mJsonObject.get("Column8") + "'"
		    + ",'" + mJsonObject.get("Column9") + "','" + mJsonObject.get("Column10") + "'"
		    + ",'" + gClientCode + "','" + mJsonObject.get("Column12") + "'"
		    + ",'" + mJsonObject.get("Column13") + "','" + mJsonObject.get("Column14") + "'"
		    + ",'" + mJsonObject.get("Column15") + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}
	return retVal;
    }

    private int funInsertTableMasterData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tbltablemaster values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tbltablemaster "
		    + "where strTableNo='" + mJsonObject.get("TableNo") + "'";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("TableNo") + "','" + mJsonObject.get("TableName") + "'"
		    + ",'" + mJsonObject.get("Status") + "','" + mJsonObject.get("AreaCode") + "'"
		    + ",'" + mJsonObject.get("WaiterNo") + "','" + mJsonObject.get("PaxNo") + "'"
		    + ",'" + mJsonObject.get("Operational") + "','" + mJsonObject.get("UserCreated") + "'"
		    + ",'" + mJsonObject.get("UserEdited") + "','" + mJsonObject.get("DateCreated") + "'"
		    + ",'" + mJsonObject.get("DateEdited") + "','" + gClientCode + "'"
		    + ",'" + mJsonObject.get("DataPostFlag") + "','" + mJsonObject.get("Sequence") + "'"
		    + ",'" + mJsonObject.get("POSCode") + "','" + mJsonObject.get("strNCTable") + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}
	return retVal;
    }

    private int funInsertWaiterMasterData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblwaitermaster values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tblwaitermaster "
		    + "where strWaiterNo='" + mJsonObject.get("WaiterNo") + "'";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("WaiterNo") + "','" + mJsonObject.get("WaiterShortName") + "'"
		    + ",'" + mJsonObject.get("WaiterFullName") + "','" + mJsonObject.get("Status") + "'"
		    + ",'" + mJsonObject.get("Operational") + "','" + mJsonObject.get("DebitCardString") + "'"
		    + ",'" + mJsonObject.get("UserCreated") + "','" + mJsonObject.get("UserEdited") + "'"
		    + ",'" + mJsonObject.get("DateCreated") + "','" + mJsonObject.get("DateEdited") + "'"
		    + ",'" + gClientCode + "','" + mJsonObject.get("DataPostFlag") + "','" + mJsonObject.get("POSCode") + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}
	return retVal;
    }

    private int funInsertAreaMasterData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblareamaster values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tblareamaster "
		    + "where strAreaCode='" + mJsonObject.get("Column1") + "'";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("Column1") + "','" + mJsonObject.get("Column2") + "'"
		    + ",'" + mJsonObject.get("Column3") + "','" + mJsonObject.get("Column4") + "'"
		    + ",'" + mJsonObject.get("Column5") + "','" + mJsonObject.get("Column6") + "'"
		    + ",'" + gClientCode + "','" + mJsonObject.get("Column8") + "'"
		    + ",'" + mJsonObject.get("Column9") + "','" + mJsonObject.get("Column10") + "' ),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}
	return retVal;
    }

    private int funInsertGiftVoucherMasterData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblgiftvoucher values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tblgiftvoucher "
		    + "where strGiftVoucherCode='" + mJsonObject.get("Column1") + "'";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("Column1") + "','" + mJsonObject.get("Column2") + "'"
		    + ",'" + mJsonObject.get("Column3") + "','" + mJsonObject.get("Column4") + "'"
		    + ",'" + mJsonObject.get("Column5") + "','" + mJsonObject.get("Column6") + "'"
		    + ",'" + mJsonObject.get("Column7") + "','" + mJsonObject.get("Column8") + "'"
		    + ",'" + mJsonObject.get("Column9") + "','" + mJsonObject.get("Column10") + "'"
		    + ",'" + mJsonObject.get("Column11") + "','" + mJsonObject.get("Column12") + "'"
		    + ",'" + mJsonObject.get("Column13") + "','" + mJsonObject.get("Column14") + "'"
		    + ",'" + mJsonObject.get("Column15") + "','" + mJsonObject.get("Column16") + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}
	return retVal;
    }

    private int funInsertBuildingMasterData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblbuildingmaster values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tblbuildingmaster "
		    + "where strBuildingCode='" + mJsonObject.get("Column1") + "'";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("Column1") + "','" + mJsonObject.get("Column2") + "'"
		    + ",'" + mJsonObject.get("Column3") + "','" + mJsonObject.get("Column4") + "'"
		    + ",'" + mJsonObject.get("Column5") + "','" + mJsonObject.get("Column6") + "'"
		    + ",'" + mJsonObject.get("Column7") + "','" + mJsonObject.get("Column8") + "'"
		    + ",'" + gClientCode + "','" + mJsonObject.get("Column10") + "'"
		    + ",'" + mJsonObject.get("Column11") + "','" + mJsonObject.get("Column12") + "'"
		    + ",'" + mJsonObject.get("Column13") + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}
	return retVal;
    }

    private int funInsertCustAreaWiseDCMasterData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblareawisedc values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tblareawisedc "
		    + "where strBuildingCode='" + mJsonObject.get("Column1") + "'";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("Column1") + "','" + mJsonObject.get("Column2") + "'"
		    + ",'" + mJsonObject.get("Column3") + "','" + mJsonObject.get("Column4") + "'"
		    + ",'" + mJsonObject.get("Column5") + "','" + mJsonObject.get("Column6") + "'"
		    + ",'" + mJsonObject.get("Column7") + "','" + mJsonObject.get("Column8") + "'"
		    + ",'" + mJsonObject.get("Column9") + "','" + mJsonObject.get("Column10") + "'"
		    + ",'" + gClientCode + "','" + mJsonObject.get("Column12") + "','" + mJsonObject.get("Column13") + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}
	return retVal;
    }

    private int funInsertCustomerMasterData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblcustomermaster values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tblcustomermaster "
		    + "where strCustomerCode='" + mJsonObject.get("customerCode") + "'";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("customerCode") + "','" + mJsonObject.get("customerName") + "'"
		    + ",'" + mJsonObject.get("buldingCode") + "','" + mJsonObject.get("buldingName") + "'"
		    + ",'" + mJsonObject.get("streetName") + "','" + mJsonObject.get("landmark") + "'"
		    + ",'" + mJsonObject.get("area") + "','" + mJsonObject.get("city") + "'"
		    + ",'" + mJsonObject.get("state") + "','" + mJsonObject.get("pinCode") + "'"
		    + ",'" + mJsonObject.get("mobileNo") + "','" + mJsonObject.get("alternateMobileNo") + "'"
		    + ",'" + mJsonObject.get("officeBuildingCode") + "','" + mJsonObject.get("officeBuildingName") + "'"
		    + ",'" + mJsonObject.get("officeStreetName") + "','" + mJsonObject.get("officeLandmark") + "'"
		    + ",'" + mJsonObject.get("officeArea") + "','" + mJsonObject.get("officeCity") + "'"
		    + ",'" + mJsonObject.get("officePinCode") + "','" + mJsonObject.get("officeState") + "'"
		    + ",'" + mJsonObject.get("officeNo") + "','" + mJsonObject.get("userCreated") + "'"
		    + ",'" + mJsonObject.get("userEdited") + "','" + mJsonObject.get("dateCreated") + "'"
		    + ",'" + mJsonObject.get("dateEdited") + "','" + mJsonObject.get("dataPostFlag") + "'"
		    + ",'" + gClientCode + "','" + mJsonObject.get("officeAddress") + "'"
		    + ",'" + mJsonObject.get("externalCode") + "','" + mJsonObject.get("customerType") + "'"
		    + ",'" + mJsonObject.get("dob") + "','" + mJsonObject.get("gender") + "'"
		    + ",'" + mJsonObject.get("anniversary") + "','" + mJsonObject.get("emailId") + "'"
		    + ",'" + mJsonObject.get("CRMId") + "','" + mJsonObject.get("CustAdress") + "'"
		    + ",'" + mJsonObject.get("strTempAddress") + "','" + mJsonObject.get("strTempStreet") + "'"
		    + ",'" + mJsonObject.get("strTempLandmark") + "','" + mJsonObject.get("strGSTNo") + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}
	return retVal;
    }

    private int funInsertCustomerTypeMasterData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblcustomertypemaster values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tblcustomertypemaster "
		    + "where strCustTypeCode='" + mJsonObject.get("Column1") + "'";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("Column1") + "','" + mJsonObject.get("Column2") + "'"
		    + ",'" + mJsonObject.get("Column3") + "','" + mJsonObject.get("Column4") + "'"
		    + ",'" + mJsonObject.get("Column5") + "','" + mJsonObject.get("Column6") + "'"
		    + ",'" + mJsonObject.get("Column7") + "','" + gClientCode + "'"
		    + ",'" + mJsonObject.get("Column9") + "','" + mJsonObject.get("Column10") + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}
	return retVal;
    }

    private int funInsertDelBoyMasterData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tbldeliverypersonmaster values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tbldeliverypersonmaster "
		    + "where strDPCode='" + mJsonObject.get("Column1") + "'";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("Column1") + "','" + mJsonObject.get("Column2") + "'"
		    + ",'" + mJsonObject.get("Column3") + "','" + mJsonObject.get("Column4") + "'"
		    + ",'" + mJsonObject.get("Column5") + "','" + mJsonObject.get("Column6") + "'"
		    + ",'" + mJsonObject.get("Column7") + "','" + mJsonObject.get("Column8") + "'"
		    + ",'" + mJsonObject.get("Column9") + "','" + mJsonObject.get("Column10") + "'"
		    + ",'" + mJsonObject.get("Column11") + "','" + mJsonObject.get("Column12") + "'"
		    + ",'" + mJsonObject.get("Column13") + "','" + mJsonObject.get("Column14") + "'"
		    + ",'" + mJsonObject.get("Column15") + "','" + gClientCode + "'"
		    + ",'" + mJsonObject.get("Column17") + "','" + mJsonObject.get("Column18") + "'"
		    + ",'" + mJsonObject.get("Column19") + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}
	return retVal;
    }

    private int funInsertAreaWiseDelBoyWiseChargesData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblareawisedelboywisecharges values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tblareawisedelboywisecharges "
		    + "where strCustAreaCode='" + mJsonObject.get("Column1") + "' "
		    + "and strDeliveryBoyCode='" + mJsonObject.get("Column2") + "'";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("Column1") + "','" + mJsonObject.get("Column2") + "'"
		    + ",'" + mJsonObject.get("Column3") + "','" + mJsonObject.get("Column4") + "'"
		    + ",'" + mJsonObject.get("Column5") + "','" + mJsonObject.get("Column6") + "'"
		    + ",'" + mJsonObject.get("Column7") + "','" + gClientCode + "'"
		    + ",'" + mJsonObject.get("Column9") + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}
	return retVal;
    }

    private int funInsertRecipeHdData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblrecipehd values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tblrecipehd "
		    + "where strRecipeCode='" + mJsonObject.get("Column1") + "'";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("Column1") + "','" + mJsonObject.get("Column2") + "'"
		    + ",'" + mJsonObject.get("Column3") + "','" + mJsonObject.get("Column4") + "'"
		    + ",'" + mJsonObject.get("Column5") + "','" + mJsonObject.get("Column6") + "'"
		    + ",'" + mJsonObject.get("Column7") + "','" + mJsonObject.get("Column8") + "'"
		    + ",'" + mJsonObject.get("Column9") + "','" + gClientCode + "'"
		    + ",'" + mJsonObject.get("Column11") + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}
	return retVal;
    }

    private int funInsertRecipeDtlData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblrecipedtl values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tblrecipedtl "
		    + "where strRecipeCode='" + mJsonObject.get("CounterCode") + "'";
	    dbMysql.execute(deleteSql);
	    insertValues += "('" + mJsonObject.get("RecipeCode") + "','" + mJsonObject.get("ChildItemCode") + "'"
		    + ",'" + mJsonObject.get("Quantity") + "','" + mJsonObject.get("POSCode") + "'"
		    + ",'" + gClientCode + "','" + mJsonObject.get("DataPostFlag") + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}
	return retVal;
    }

    private int funInsertCounterHdData(JSONArray mJsonArray) throws Exception
    {
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblcounterhd values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tblcounterhd where strCounterCode='" + mJsonObject.get("strCounterCode") + "'";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("strCounterCode") + "','" + mJsonObject.get("strCounterName") + "'"
		    + ",'" + mJsonObject.get("strPOSCode") + "','" + mJsonObject.get("strUserCreated") + "'"
		    + ",'" + mJsonObject.get("strUserEdited") + "','" + mJsonObject.get("dteDateCreated") + "'"
		    + ",'" + mJsonObject.get("dteDateEdited") + "','" + gClientCode + "','" + mJsonObject.get("strDataPostFlag") + "'"
		    + ",'" + mJsonObject.get("strOperational") + "','" + mJsonObject.get("strUserCode") + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    dbMysql.execute(query);
	}

	return 1;
    }

    private int funInsertCounterDtlData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblcounterdtl values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tblcounterdtl "
		    + "where strCounterCode='" + mJsonObject.get("CounterCode") + "'";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("CounterCode") + "','" + mJsonObject.get("MenuCode") + "'"
		    + ",'" + gClientCode + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}
	return retVal;
    }

    private int funInsertAdvOrderTypeData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tbladvanceordertypemaster values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tbladvanceordertypemaster "
		    + "where strAdvOrderTypeCode='" + mJsonObject.get("Column1") + "'";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("Column1") + "','" + mJsonObject.get("Column2") + "'"
		    + ",'" + mJsonObject.get("Column3") + "','" + mJsonObject.get("Column4") + "'"
		    + ",'" + mJsonObject.get("Column5") + "','" + mJsonObject.get("Column6") + "'"
		    + ",'" + mJsonObject.get("Column7") + "','" + mJsonObject.get("Column8") + "'"
		    + ",'" + gClientCode + "','" + mJsonObject.get("Column10") + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}
	return retVal;
    }

    private int funInsertUserHdData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tbluserhd values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tbluserhd "
		    + "where strUserCode='" + mJsonObject.get("UserCode") + "'";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("UserCode") + "','" + mJsonObject.get("UserName") + "'"
		    + ",'" + mJsonObject.get("Password") + "','" + mJsonObject.get("SuperType") + "'"
		    + ",'" + mJsonObject.get("ValidDate") + "','" + mJsonObject.get("POSAccess") + "'"
		    + ",'" + mJsonObject.get("UserCreated") + "','" + mJsonObject.get("UserEdited") + "'"
		    + ",'" + mJsonObject.get("DateCreated") + "','" + mJsonObject.get("DateEdited") + "'"
		    + ",'" + gClientCode + "','" + mJsonObject.get("DataPostFlag") + "'"
		    + ",'" + mJsonObject.get("ImageIcon") + "','" + mJsonObject.get("ImagePath") + "'"
		    + ",'" + mJsonObject.get("DebitCardString") + "','" + mJsonObject.get("strWaiterNo") + "'"
		    + ",'" + mJsonObject.get("strUserType") + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}
	return retVal;
    }

    private int funInsertUserDtlData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tbluserdtl values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tbluserdtl "
		    + "where strUserCode='" + mJsonObject.get("UserCode") + "' ";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("UserCode") + "','" + mJsonObject.get("FormName") + "'"
		    + ",'" + mJsonObject.get("ButtonName") + "','" + mJsonObject.get("Sequence") + "'"
		    + ",'" + mJsonObject.get("Add") + "','" + mJsonObject.get("Edit") + "'"
		    + ",'" + mJsonObject.get("Delete") + "','" + mJsonObject.get("View") + "'"
		    + ",'" + mJsonObject.get("Print") + "','" + mJsonObject.get("Save") + "'"
		    + ",'" + mJsonObject.get("Grant") + "','" + mJsonObject.get("TLA") + "','" + mJsonObject.get("Auditing") + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}
	return retVal;
    }

    private int funInsertSuperUserDtlData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblsuperuserdtl values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tblsuperuserdtl "
		    + "where strUserCode='" + mJsonObject.get("UserCode") + "'";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("UserCode") + "','" + mJsonObject.get("FormName") + "'"
		    + ",'" + mJsonObject.get("ButtonName") + "','" + mJsonObject.get("Sequence") + "'"
		    + ",'" + mJsonObject.get("Add") + "','" + mJsonObject.get("Edit") + "'"
		    + ",'" + mJsonObject.get("Delete") + "','" + mJsonObject.get("View") + "'"
		    + ",'" + mJsonObject.get("Print") + "','" + mJsonObject.get("Save") + "'"
		    + ",'" + mJsonObject.get("Grant") + "','" + mJsonObject.get("TLA") + "','" + mJsonObject.get("Auditing") + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}
	return retVal;
    }

    private int funInsertPromotionDtlData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblpromotiondtl values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tblpromotiondtl "
		    + "where strPromoCode='" + mJsonObject.get("PromoCode") + "'";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("PromoCode") + "','" + mJsonObject.get("GetPromoOn") + "'"
		    + ",'" + mJsonObject.get("GetPromoItemCode") + "','" + mJsonObject.get("GetItemQty") + "'"
		    + ",'" + mJsonObject.get("DiscountType") + "','" + mJsonObject.get("Discount") + "'"
		    + ",'" + gClientCode + "','" + mJsonObject.get("DataPostFlag") + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}
	return retVal;
    }

    private int funInsertBuyPromotionDtlData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblbuypromotiondtl values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tblbuypromotiondtl "
		    + "where strPromoCode='" + mJsonObject.get("PromoCode") + "'";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("PromoCode") + "','" + mJsonObject.get("BuyPromoItemCode") + "'"
		    + ",'" + mJsonObject.get("BuyItemQty") + "','" + mJsonObject.get("Operator") + "'"
		    + ",'" + gClientCode + "','" + mJsonObject.get("DataPostFlag") + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}
	return retVal;
    }

    private int funInsertPromotionDayTimeData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblpromotiondaytimedtl values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tblpromotiondaytimedtl "
		    + "where strPromoCode='" + mJsonObject.get("PromoCode") + "'";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("PromoCode") + "','" + mJsonObject.get("strDay") + "'"
		    + ",'" + mJsonObject.get("tmeFromTime") + "','" + mJsonObject.get("tmeToTime") + "'"
		    + ",'" + gClientCode + "','" + mJsonObject.get("DataPostFlag") + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}
	return retVal;
    }

    private int funInsertPromotionMasterData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblpromotionmaster values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tblpromotionmaster "
		    + "where strPromoCode='" + mJsonObject.get("PromoCode") + "'";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("PromoCode") + "','" + mJsonObject.get("PromoName") + "'"
		    + ",'" + mJsonObject.get("BuyPromoOn") + "','" + mJsonObject.get("BuyPromoItemCode") + "'"
		    + ",'" + mJsonObject.get("Operator") + "','" + mJsonObject.get("BuyItemQty") + "'"
		    + ",'" + mJsonObject.get("FromDate") + "','" + mJsonObject.get("ToDate") + "'"
		    + ",'" + mJsonObject.get("FromTime") + "','" + mJsonObject.get("ToTime") + "'"
		    + ",'" + mJsonObject.get("Days") + "','" + mJsonObject.get("Type") + "'"
		    + ",'" + mJsonObject.get("PromoNote") + "','" + mJsonObject.get("UserCreated") + "'"
		    + ",'" + mJsonObject.get("UserEdited") + "','" + mJsonObject.get("DateCreated") + "'"
		    + ",'" + mJsonObject.get("DateEdited") + "','" + gClientCode + "' "
		    + ",'" + mJsonObject.get("DataPostFlag") + "','" + mJsonObject.get("POSCode") + "'"
		    + ",'" + mJsonObject.get("GetItemCode") + "','" + mJsonObject.get("GetPromoOn") + "'"
		    + ",'" + mJsonObject.get("strAreaCode") + "','" + mJsonObject.get("strPromoGroupType") + "'"
		    + ",'" + mJsonObject.get("longKOTTimeBound") + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}
	return retVal;
    }

    private int funInsertLoyaltyPointsPOSDtlData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblloyaltypointposdtl values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tblloyaltypointposdtl "
		    + "where strLoyaltyCode='" + mJsonObject.get("PromoCode") + "'";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("LoyaltyCode") + "','" + mJsonObject.get("POSCode") + "'"
		    + ",'" + gClientCode + "','" + mJsonObject.get("DataPostFlag") + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}
	return retVal;
    }

    private int funInsertLoyaltyPointsCustomerDtlData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblloyaltypointcustomerdtl values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tblloyaltypointcustomerdtl "
		    + "where strLoyaltyCode='" + mJsonObject.get("PromoCode") + "'";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("LoyaltyCode") + "','" + mJsonObject.get("CustTypeCode") + "'"
		    + ",'" + gClientCode + "','" + mJsonObject.get("DataPostFlag") + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}
	return retVal;
    }

    private int funInsertLoyaltyPointsMenuHdDtl(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblloyaltypointmenuhddtl values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tblloyaltypointmenuhddtl "
		    + "where strLoyaltyCode='" + mJsonObject.get("PromoCode") + "'";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("LoyaltyCode") + "','" + mJsonObject.get("MenuCode") + "'"
		    + ",'" + gClientCode + "','" + mJsonObject.get("DataPostFlag") + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}
	return retVal;
    }

    private int funInsertLoyaltyPointsSubGroupDtl(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblloyaltypointsubgroupdtl values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tblloyaltypointsubgroupdtl "
		    + "where strLoyaltyCode='" + mJsonObject.get("PromoCode") + "'";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("LoyaltyCode") + "','" + mJsonObject.get("SGCode") + "'"
		    + ",'" + gClientCode + "','" + mJsonObject.get("DataPostFlag") + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}
	return retVal;
    }

    private int funInsertLoyaltyPointsMaster(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblloyaltypoints values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tblloyaltypoints where strLoyaltyCode='" + mJsonObject.get("Column1") + "'";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("Column1") + "','" + mJsonObject.get("Column2") + "'"
		    + ",'" + mJsonObject.get("Column3") + "','" + mJsonObject.get("Column4") + "'"
		    + ",'" + mJsonObject.get("Column5") + "','" + mJsonObject.get("Column6") + "'"
		    + ",'" + mJsonObject.get("Column7") + "','" + mJsonObject.get("Column8") + "'"
		    + ",'" + mJsonObject.get("Column9") + "','" + gClientCode + "'"
		    + ",'" + mJsonObject.get("Column11") + "','" + mJsonObject.get("Column12") + "'"
		    + ",'" + mJsonObject.get("Column13") + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}

	return retVal;
    }

    private int funInsertOrderMasterData(JSONArray mJsonArray) throws Exception
    {
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblordermaster values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tblordermaster where strOrderCode='" + mJsonObject.get("strOrderCode") + "'";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("strOrderCode") + "','" + mJsonObject.get("strOrderDesc") + "'"
		    + ",'" + mJsonObject.get("tmeUpToTime") + "','" + mJsonObject.get("strUserCreated") + "'"
		    + ",'" + mJsonObject.get("strUserEdited") + "','" + mJsonObject.get("dteDateCreated") + "'"
		    + ",'" + mJsonObject.get("dteDateEdited") + "','" + gClientCode + "'"
		    + ",'" + mJsonObject.get("strDataPostFlag") + "','" + mJsonObject.get("strPOSCode") + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    dbMysql.execute(query);
	}

	return 1;
    }

    private int funInsertItemOrderingDtlData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblitemorderingdtl values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = " delete from tblitemorderingdtl where strItemCode='" + mJsonObject.get("ItemCode") + "' ";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("ItemCode") + "','" + mJsonObject.get("POSCode") + "'"
		    + ",'" + mJsonObject.get("OrderCode") + "','" + gClientCode + "','" + mJsonObject.get("DataPostFlag") + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}
	return retVal;
    }

    private int funInsertcharactersticsMasterData(JSONArray mJsonArray) throws Exception
    {
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblcharactersticsmaster values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tblcharactersticsmaster where strCharCode='" + mJsonObject.get("strCharCode") + "' ";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("strCharCode") + "','" + mJsonObject.get("strCharName") + "'"
		    + ",'" + mJsonObject.get("strCharType") + "','" + mJsonObject.get("strWSCharCode") + "'"
		    + ",'" + mJsonObject.get("strValue") + "','" + mJsonObject.get("strUserCreated") + "'"
		    + ",'" + mJsonObject.get("strUserEdited") + "','" + mJsonObject.get("dteDateCreated") + "'"
		    + ",'" + mJsonObject.get("dteDateEdited") + "','" + gClientCode + "'"
		    + ",'" + mJsonObject.get("strDataPostFlag") + "','" + mJsonObject.get("strPOSCode") + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    dbMysql.execute(query);
	}

	return 1;
    }

    private int funInsertItemCharctersticsLinkupDtlData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblitemcharctersticslinkupdtl values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);

	    deleteSql = " delete from tblitemcharctersticslinkupdtl where strItemCode='" + mJsonObject.get("ItemCode") + "' ";
	    dbMysql.execute(deleteSql);

	    insertValues += "('" + mJsonObject.get("ItemCode") + "','" + mJsonObject.get("CharCode") + "'"
		    + ",'" + mJsonObject.get("CharValue") + "','" + mJsonObject.get("POSCode") + "'"
		    + ",'" + gClientCode + "','" + mJsonObject.get("DataPostFlag") + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}
	return retVal;
    }

    private int funInsertItemWSProductLinkupDtlData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblitemmasterlinkupdtl values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tblitemmasterlinkupdtl where strItemCode='" + mJsonObject.get("ItemCode") + "' ";
	    dbMysql.execute(deleteSql);
	    insertValues += "('" + mJsonObject.get("ItemCode") + "','" + mJsonObject.get("POSCode") + "'"
		    + ",'" + mJsonObject.get("WSProductCode") + "','" + mJsonObject.get("WSProductName") + "'"
		    + ",'" + mJsonObject.get("strExciseBrandCode") + "','" + mJsonObject.get("strExciseBrandName") + "' "
		    + ",'" + gClientCode + "','" + mJsonObject.get("DataPostFlag") + "'),";
	    //System.out.println(insertValues);
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}
	return retVal;
    }

    private int funInsertCharValueDtlData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblcharvalue values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tblcharvalue where strCharCode='" + mJsonObject.get("CharCode") + "' ";
	    dbMysql.execute(deleteSql);
	    insertValues += "('" + mJsonObject.get("CharCode") + "','" + mJsonObject.get("CharName") + "'"
		    + ",'" + mJsonObject.get("CharValue") + "','" + gClientCode + "'"
		    + ",'" + mJsonObject.get("POSCode") + "','" + mJsonObject.get("DataPostFlag") + "'),";
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    System.out.println(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}
	return retVal;
    }

    private int funInsertFactoryMasterData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblfactorymaster values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tblfactorymaster where strFactoryCode='" + mJsonObject.get("FactoryCode") + "' ";
	    dbMysql.execute(deleteSql);
	    insertValues += "('" + mJsonObject.get("Column1") + "','" + mJsonObject.get("Column2") + "'"
		    + ",'" + mJsonObject.get("Column3") + "','" + mJsonObject.get("Column4") + "'"
		    + ",'" + mJsonObject.get("Column5") + "','" + mJsonObject.get("Column6") + "'"
		    + ",'" + gClientCode + "','" + mJsonObject.get("Column8") + "' ),";
	    //System.out.println(insertValues);
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}
	return retVal;
    }

    //Insert PosWise ItemWise Incentive Data
    private int funInsertPosWiseItemWiseIncentiveData(JSONArray mJsonArray) throws Exception
    {
	int retVal = 0;
	StringBuilder sb = null;
	String insertValues = "";
	String query = "insert into tblposwiseitemwiseincentives values";
	JSONObject mJsonObject = new JSONObject();
	String deleteSql = "";

	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    deleteSql = "delete from tblposwiseitemwiseincentives where strItemCode='" + mJsonObject.get("ItemCode") + "' and strPOSCode='" + mJsonObject.get("PropertyPOSCode") + "' ";
	    dbMysql.execute(deleteSql);
	    insertValues += "('" + mJsonObject.get("PropertyPOSCode") + "','" + mJsonObject.get("ItemCode") + "'"
		    + ",'" + mJsonObject.get("ItemName") + "','" + mJsonObject.get("IncentiveType") + "'"
		    + ",'" + mJsonObject.get("IncentiveValue") + "','" + gClientCode + "'"
		    + ",'" + mJsonObject.get("DataPostFlag") + "','" + mJsonObject.get("DateCreated") + "','" + mJsonObject.get("DateEdited") + "' ),";
	    //System.out.println(insertValues);
	}
	if (mJsonArray.size() > 0)
	{
	    sb = new StringBuilder(insertValues);
	    insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    query = query + insertValues;
	    sb = new StringBuilder(query);
	    //query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
	    retVal = dbMysql.execute(query);
	}
	return retVal;
    }

    private int funFetchMasterData(String masterTableName) throws Exception
    {
	int retValue = 0;

	String fetchMasterURL = gSanguineWebServiceURL + "/POSIntegration/funGetMasterData"
		+ "?strMasterName=" + masterTableName + "&strPropertyPOSCode=" + gPropertyCode + "&strLastModifiedDate=" + gLastModifiedDate;
	URL url = new URL(fetchMasterURL);
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setRequestMethod("GET");
	conn.setRequestProperty("Accept", "application/json");
	BufferedReader br = new BufferedReader(new InputStreamReader(
		(conn.getInputStream())));
	String output = "", jsonString = "";
	while ((output = br.readLine()) != null)
	{
	    jsonString += output;
	}

	JSONParser parser = new JSONParser();
	Object obj = parser.parse(jsonString);
	JSONObject jObj = (JSONObject) obj;

	Iterator<Object> it = jObj.keySet().iterator();
	if (it.hasNext())//while(it.hasNext())
	{
	    JSONArray mJsonArray = null;
	    String masterName = it.next().toString();
	    System.out.println("masterName=  " + masterName + "\n\n");

	    switch (masterName)
	    {
		case "tblmenuitempricingdtl":
		    mJsonArray = (JSONArray) jObj.get("tblmenuitempricingdtl");
		    retValue = funInsertMenuItemPricingDtlData(mJsonArray);
		    break;

		case "tblmenuitempricinghd":
		    mJsonArray = (JSONArray) jObj.get("tblmenuitempricinghd");
		    retValue = funInsertMenuItemPricingHdData(mJsonArray);
		    break;

		case "tblitemmaster":
		    mJsonArray = (JSONArray) jObj.get("tblitemmaster");
		    retValue = funInsertItemMasterData(mJsonArray);
		    break;

		case "tbltaxhd":
		    mJsonArray = (JSONArray) jObj.get("tbltaxhd");
		    retValue = funInsertTaxHdData(mJsonArray);
		    break;

		case "tbltaxposdtl":
		    mJsonArray = (JSONArray) jObj.get("tbltaxposdtl");
		    retValue = funInsertTaxPOSDtlData(mJsonArray);
		    break;

		case "tblsettlementtax":
		    mJsonArray = (JSONArray) jObj.get("tblsettlementtax");
		    retValue = funInsertTaxSettlementDtlData(mJsonArray);
		    break;

		case "tbltaxongroup":
		    mJsonArray = (JSONArray) jObj.get("tbltaxongroup");
		    retValue = funInsertTaxGroupDtlData(mJsonArray);
		    break;

		case "tblgrouphd":
		    mJsonArray = (JSONArray) jObj.get("tblgrouphd");
		    retValue = funInsertGroupMasterData(mJsonArray);
		    break;

		case "tblsubgrouphd":
		    mJsonArray = (JSONArray) jObj.get("tblsubgrouphd");
		    retValue = funInsertSubGroupMasterData(mJsonArray);
		    break;

		case "tblsettelmenthd":
		    mJsonArray = (JSONArray) jObj.get("tblsettelmenthd");
		    retValue = funInsertSettlementHdData(mJsonArray);
		    break;

		case "tblreasonmaster":
		    mJsonArray = (JSONArray) jObj.get("tblreasonmaster");
		    retValue = funInsertReasonMasterData(mJsonArray);
		    break;

		case "tblcostcentermaster":
		    mJsonArray = (JSONArray) jObj.get("tblcostcentermaster");
		    retValue = funInsertCostCenterMasterData(mJsonArray);
		    break;

		case "tblmenuhd":
		    mJsonArray = (JSONArray) jObj.get("tblmenuhd");
		    retValue = funInsertMenuHdData(mJsonArray);
		    break;

		case "tblmodifiermaster":
		    mJsonArray = (JSONArray) jObj.get("tblmodifiermaster");
		    retValue = funInsertModifierMasterData(mJsonArray);
		    break;

		case "tblmodifiergrouphd":
		    mJsonArray = (JSONArray) jObj.get("tblmodifiergrouphd");
		    retValue = funInsertItemModifierGroupHdData(mJsonArray);
		    break;

		case "tblitemmodofier":
		    mJsonArray = (JSONArray) jObj.get("tblitemmodofier");
		    retValue = funInsertItemModifierData(mJsonArray);
		    break;

		case "tbltablemaster":
		    mJsonArray = (JSONArray) jObj.get("tbltablemaster");
		    retValue = funInsertTableMasterData(mJsonArray);
		    break;

		case "tblwaitermaster":
		    mJsonArray = (JSONArray) jObj.get("tblwaitermaster");
		    retValue = funInsertWaiterMasterData(mJsonArray);
		    break;

		case "tblareamaster":
		    mJsonArray = (JSONArray) jObj.get("tblareamaster");
		    retValue = funInsertAreaMasterData(mJsonArray);
		    break;

		case "tblgiftvoucher":
		    mJsonArray = (JSONArray) jObj.get("tblgiftvoucher");
		    retValue = funInsertGiftVoucherMasterData(mJsonArray);
		    break;

		case "tblbuildingmaster":
		    mJsonArray = (JSONArray) jObj.get("tblbuildingmaster");
		    retValue = funInsertBuildingMasterData(mJsonArray); // Customer Area Master
		    break;

		case "tblareawisedc":
		    mJsonArray = (JSONArray) jObj.get("tblareawisedc");
		    retValue = funInsertCustAreaWiseDCMasterData(mJsonArray);
		    break;

		case "tblcustomermaster":
		    mJsonArray = (JSONArray) jObj.get("tblcustomermaster");
		    retValue = funInsertCustomerMasterData(mJsonArray);
		    break;

		case "tblcustomertypemaster":
		    mJsonArray = (JSONArray) jObj.get("tblcustomertypemaster");
		    retValue = funInsertCustomerTypeMasterData(mJsonArray);
		    break;

		case "tbldeliverypersonmaster":
		    mJsonArray = (JSONArray) jObj.get("tbldeliverypersonmaster");
		    retValue = funInsertDelBoyMasterData(mJsonArray);
		    break;

		case "tblareawisedelboywisecharges":
		    mJsonArray = (JSONArray) jObj.get("tblareawisedelboywisecharges");
		    retValue = funInsertAreaWiseDelBoyWiseChargesData(mJsonArray);
		    break;

		case "tblrecipehd":
		    mJsonArray = (JSONArray) jObj.get("tblrecipehd");
		    retValue = funInsertRecipeHdData(mJsonArray);
		    break;

		case "tblrecipedtl":
		    mJsonArray = (JSONArray) jObj.get("tblrecipedtl");
		    retValue = funInsertRecipeDtlData(mJsonArray);
		    break;

		case "tblcounterhd":
		    mJsonArray = (JSONArray) jObj.get("tblcounterhd");
		    retValue = funInsertCounterHdData(mJsonArray);
		    break;

		case "tblcounterdtl":
		    mJsonArray = (JSONArray) jObj.get("tblcounterdtl");
		    retValue = funInsertCounterDtlData(mJsonArray);
		    break;

		case "tbladvanceordertypemaster":
		    mJsonArray = (JSONArray) jObj.get("tbladvanceordertypemaster");
		    retValue = funInsertAdvOrderTypeData(mJsonArray);
		    break;

		case "tbluserhd":
		    mJsonArray = (JSONArray) jObj.get("tbluserhd");
		    retValue = funInsertUserHdData(mJsonArray);
		    break;

		case "tbluserdtl":
		    mJsonArray = (JSONArray) jObj.get("tbluserdtl");
		    retValue = funInsertUserDtlData(mJsonArray);
		    break;

		case "tblsuperuserdtl":
		    mJsonArray = (JSONArray) jObj.get("tblsuperuserdtl");
		    retValue = funInsertSuperUserDtlData(mJsonArray);
		    break;

		case "tblpromotionmaster":
		    mJsonArray = (JSONArray) jObj.get("tblpromotionmaster");
		    retValue = funInsertPromotionMasterData(mJsonArray);
		    break;

		case "tblpromotiondtl":
		    mJsonArray = (JSONArray) jObj.get("tblpromotiondtl");
		    retValue = funInsertPromotionDtlData(mJsonArray);
		    break;

		case "tblbuypromotiondtl":
		    mJsonArray = (JSONArray) jObj.get("tblbuypromotiondtl");
		    retValue = funInsertBuyPromotionDtlData(mJsonArray);
		    break;

		case "tblpromotiondaytimedtl":
		    mJsonArray = (JSONArray) jObj.get("tblpromotiondaytimedtl");
		    retValue = funInsertPromotionDayTimeData(mJsonArray);
		    break;

		case "tblloyaltypointcustomerdtl":
		    mJsonArray = (JSONArray) jObj.get("tblloyaltypointcustomerdtl");
		    retValue = funInsertLoyaltyPointsCustomerDtlData(mJsonArray);
		    break;

		case "tblloyaltypointmenuhddtl":
		    mJsonArray = (JSONArray) jObj.get("tblloyaltypointmenuhddtl");
		    retValue = funInsertLoyaltyPointsMenuHdDtl(mJsonArray);
		    break;

		case "tblloyaltypointposdtl":
		    mJsonArray = (JSONArray) jObj.get("tblloyaltypointposdtl");
		    retValue = funInsertLoyaltyPointsPOSDtlData(mJsonArray);
		    break;

		case "tblloyaltypointsubgroupdtl":
		    mJsonArray = (JSONArray) jObj.get("tblloyaltypointsubgroupdtl");
		    retValue = funInsertLoyaltyPointsSubGroupDtl(mJsonArray);
		    break;

		case "tblloyaltypoints":
		    mJsonArray = (JSONArray) jObj.get("tblloyaltypoints");
		    retValue = funInsertLoyaltyPointsMaster(mJsonArray);
		    break;

		case "tblordermaster":
		    mJsonArray = (JSONArray) jObj.get("tblordermaster");
		    retValue = funInsertOrderMasterData(mJsonArray);
		    break;

		case "tblitemorderingdtl":
		    mJsonArray = (JSONArray) jObj.get("tblitemorderingdtl");
		    retValue = funInsertItemOrderingDtlData(mJsonArray);
		    break;

		case "tblcharactersticsmaster":
		    mJsonArray = (JSONArray) jObj.get("tblcharactersticsmaster");
		    retValue = funInsertcharactersticsMasterData(mJsonArray);
		    break;

		case "tblitemcharctersticslinkupdtl":
		    mJsonArray = (JSONArray) jObj.get("tblitemcharctersticslinkupdtl");
		    retValue = funInsertItemCharctersticsLinkupDtlData(mJsonArray);
		    break;

		case "tblcharvalue":
		    mJsonArray = (JSONArray) jObj.get("tblcharvalue");
		    retValue = funInsertCharValueDtlData(mJsonArray);
		    break;

		case "tblitemmasterlinkupdtl":
		    mJsonArray = (JSONArray) jObj.get("tblitemmasterlinkupdtl");
		    retValue = funInsertItemWSProductLinkupDtlData(mJsonArray);
		    break;

		case "tblfactorymaster":
		    mJsonArray = (JSONArray) jObj.get("tblfactorymaster");
		    retValue = funInsertFactoryMasterData(mJsonArray);
		    break;

		case "tblposwiseitemwiseincentives":
		    mJsonArray = (JSONArray) jObj.get("tblposwiseitemwiseincentives");
		    retValue = funInsertPosWiseItemWiseIncentiveData(mJsonArray);
		    break;

		case "tbldischd":
		    mJsonArray = (JSONArray) jObj.get("tbldischd");
		    retValue = funInsertDiscMasterHdData(mJsonArray);
		    break;

		case "tbldiscdtl":
		    mJsonArray = (JSONArray) jObj.get("tbldiscdtl");
		    retValue = funInsertDiscMasterDtlData(mJsonArray);
		    break;

	    }
	}
	return retValue;
    }

    private void funUpdateSystemTime(int status)
    {
	try
	{
	    String fetchMasterURL = gSanguineWebServiceURL + "/POSIntegration/funGetSystemTime";
	    URL url = new URL(fetchMasterURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setRequestMethod("GET");
	    conn.setRequestProperty("Accept", "application/json");
	    BufferedReader br = new BufferedReader(new InputStreamReader(
		    (conn.getInputStream())));
	    String output = "", jsonString = "";
	    while ((output = br.readLine()) != null)
	    {
		jsonString += output;
	    }

	    JSONParser parser = new JSONParser();
	    Object obj = parser.parse(jsonString);
	    JSONObject jObj = (JSONObject) obj;

	    String serverSystemDate = "No";
	    if (null != jObj.get("SystemTime"))
	    {
		serverSystemDate = jObj.get("SystemTime").toString();
	    }

	    System.out.println("HO Time= " + serverSystemDate + "\tStatus=" + status);
	    if (!serverSystemDate.equalsIgnoreCase("No") && status > 0)
	    {
		dbMysql.execute("update tblsetup set dteHOServerDate='" + serverSystemDate + "'");
		JOptionPane.showMessageDialog(null, "Data updated from HO upto " + serverSystemDate);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

// Function to Post Customer Master Data to HO
    public boolean funPostCustomerMasterDataToHO()
    {
	boolean flgResult = false;
	StringBuilder sql = new StringBuilder();
	ResultSet rsCustInfo = null;

	try
	{
	    JSONObject rootObject = new JSONObject();
	    JSONArray dataObjectArray = new JSONArray();

	    sql.append("select * from tblcustomermaster where strDataPostFlag='N'");
	    rsCustInfo = dbMysql.executeResultSet(sql.toString());
	    while (rsCustInfo.next())
	    {
		JSONObject dataObject = new JSONObject();
		dataObject.put("CustomerCode", rsCustInfo.getString("strCustomerCode"));
		dataObject.put("CustomerName", rsCustInfo.getString("strCustomerName"));
		dataObject.put("BuldingCode", rsCustInfo.getString("strBuldingCode"));
		dataObject.put("BuildingName", rsCustInfo.getString("strBuildingName"));
		dataObject.put("StreetName", rsCustInfo.getString("strStreetName"));
		dataObject.put("Landmark", rsCustInfo.getString("strLandmark"));
		dataObject.put("Area", rsCustInfo.getString("strArea"));
		dataObject.put("City", rsCustInfo.getString("strCity"));
		dataObject.put("State", rsCustInfo.getString("strState"));
		dataObject.put("PinCode", rsCustInfo.getString("intPinCode"));
		dataObject.put("MobileNo", rsCustInfo.getString("longMobileNo"));
		dataObject.put("AlternateMobileNo", rsCustInfo.getString("longAlternateMobileNo"));
		dataObject.put("OfficeBuildingCode", rsCustInfo.getString("strOfficeBuildingCode"));
		dataObject.put("OfficeBuildingName", rsCustInfo.getString("strOfficeBuildingName"));
		dataObject.put("OfficeStreetName", rsCustInfo.getString("strOfficeStreetName"));
		dataObject.put("OfficeLandmark", rsCustInfo.getString("strOfficeLandmark"));
		dataObject.put("OfficeArea", rsCustInfo.getString("strOfficeArea"));
		dataObject.put("OfficeCity", rsCustInfo.getString("strOfficeCity"));
		dataObject.put("OfficePinCode", rsCustInfo.getString("strOfficePinCode"));
		dataObject.put("OfficeState", rsCustInfo.getString("strOfficeState"));
		dataObject.put("OfficeNo", rsCustInfo.getString("strOfficeNo"));
		dataObject.put("UserCreated", rsCustInfo.getString("strUserCreated"));
		dataObject.put("UserEdited", rsCustInfo.getString("strUserEdited"));
		dataObject.put("DateCreated", rsCustInfo.getString("dteDateCreated"));
		dataObject.put("DateEdited", rsCustInfo.getString("dteDateEdited"));
		dataObject.put("ClientCode", rsCustInfo.getString("strClientCode"));
		dataObject.put("DataPostFlag", rsCustInfo.getString("strDataPostFlag"));
		dataObject.put("OfficeAddress", rsCustInfo.getString("strOfficeAddress"));
		dataObject.put("ExternalCode", rsCustInfo.getString("strExternalCode"));
		dataObject.put("CustomerType", rsCustInfo.getString("strCustomerType"));
		dataObject.put("DOB", rsCustInfo.getString("dteDOB"));
		dataObject.put("Gender", rsCustInfo.getString("strGender"));
		dataObject.put("Anniversary", rsCustInfo.getString("dteAnniversary"));
		dataObject.put("EmailId", rsCustInfo.getString("strEmailId"));
		dataObject.put("CRMId", rsCustInfo.getString("strCRMId"));

		dataObject.put("strCustAddress", rsCustInfo.getString("strCustAddress"));
		dataObject.put("strTempAddress", rsCustInfo.getString("strTempAddress"));
		dataObject.put("strTempStreet", rsCustInfo.getString("strTempStreet"));
		dataObject.put("strTempLandmark", rsCustInfo.getString("strTempLandmark"));
		dataObject.put("strGSTNo", rsCustInfo.getString("strGSTNo"));

		dataObjectArray.add(dataObject);
	    }
	    rootObject.put("tblcustomermaster", dataObjectArray);
	    String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostMasterData";

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
	    try
	    {
		rsCustInfo.close();
	    }
	    catch (SQLException ex)
	    {
		ex.printStackTrace();
	    }
	    sql = null;
	    return flgResult;
	}
    }

    public boolean funPostCustomerAreaMaster()
    {
	boolean flgResult = false;
	StringBuilder sql = new StringBuilder();
	ResultSet rsCustAreaMaster = null;
	try
	{
	    JSONObject rootObject = new JSONObject();
	    JSONArray dataObjectArray = new JSONArray();

	    sql.setLength(0);
	    sql.append("select * from tblbuildingmaster where strDataPostFlag='N'");
	    rsCustAreaMaster = dbMysql.executeResultSet(sql.toString());
	    while (rsCustAreaMaster.next())
	    {
		JSONObject dataObject = new JSONObject();

		dataObject.put("BuildingCode", rsCustAreaMaster.getString("strBuildingCode"));
		dataObject.put("BuildingName", rsCustAreaMaster.getString("strBuildingName"));
		dataObject.put("Address", rsCustAreaMaster.getString("strAddress"));
		dataObject.put("UserCreated", rsCustAreaMaster.getString("strUserCreated"));
		dataObject.put("UserEdited", rsCustAreaMaster.getString("strUserEdited"));
		dataObject.put("DateCreated", rsCustAreaMaster.getString("dteDateCreated"));
		dataObject.put("DateEdited", rsCustAreaMaster.getString("dteDateEdited"));
		dataObject.put("HomeDeliCharge", rsCustAreaMaster.getString("dblHomeDeliCharge"));
		dataObject.put("ClientCode", rsCustAreaMaster.getString("strClientCode"));
		dataObject.put("DataPostFlag", rsCustAreaMaster.getString("strDataPostFlag"));

		dataObjectArray.add(dataObject);
	    }
	    rootObject.put("tblbuildingmaster", dataObjectArray);

	    String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostMasterData";
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
	    String output = "", op = "Updated successfully: ";

	    while ((output = br.readLine()) != null)
	    {
		op += output;
	    }
	    conn.disconnect();
	    flgResult = true;

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    try
	    {
		rsCustAreaMaster.close();
	    }
	    catch (SQLException ex)
	    {
		ex.printStackTrace();
	    }
	    sql = null;
	    return flgResult;
	}
    }

    public boolean funPostDelChargesMaster()
    {
	boolean flgResult = false;
	StringBuilder sql = new StringBuilder();
	ResultSet rsCustAreaDelCharges = null;

	try
	{
	    sql.setLength(0);
	    sql.append("select * from tblareawisedc where strDataPostFlag='N'");
	    rsCustAreaDelCharges = dbMysql.executeResultSet(sql.toString());
	    JSONObject rootObject = new JSONObject();
	    JSONArray dataObjectArray = new JSONArray();

	    while (rsCustAreaDelCharges.next())
	    {
		JSONObject dataObject = new JSONObject();

		dataObject.put("BuildingCode", rsCustAreaDelCharges.getString("strBuildingCode"));
		dataObject.put("Kilometers", rsCustAreaDelCharges.getString("dblKilometers"));
		dataObject.put("Symbol", rsCustAreaDelCharges.getString("strSymbol"));
		dataObject.put("BillAmount", rsCustAreaDelCharges.getString("dblBillAmount"));
		dataObject.put("BillAmount1", rsCustAreaDelCharges.getString("dblBillAmount1"));
		dataObject.put("DeliveryCharges", rsCustAreaDelCharges.getString("dblDeliveryCharges"));
		dataObject.put("UserCreated", rsCustAreaDelCharges.getString("strUserCreated"));
		dataObject.put("UserEdited", rsCustAreaDelCharges.getString("strUserEdited"));
		dataObject.put("DateCreated", rsCustAreaDelCharges.getString("dteDateCreated"));
		dataObject.put("DateEdited", rsCustAreaDelCharges.getString("dteDateEdited"));
		dataObject.put("ClientCode", rsCustAreaDelCharges.getString("strClientCode"));
		dataObject.put("DataPostFlag", rsCustAreaDelCharges.getString("strDataPostFlag"));

		dataObjectArray.add(dataObject);
	    }
	    rootObject.put("tblareawisedc", dataObjectArray);

	    String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostMasterData";
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
		throw new RuntimeException("Failed : HTTP error code : "
			+ conn.getResponseCode());
	    }
	    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	    String output = "", op = "Updated successfully: ";

	    while ((output = br.readLine()) != null)
	    {
		op += output;
	    }
	    conn.disconnect();
	    flgResult = true;
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    try
	    {
		rsCustAreaDelCharges.close();
	    }
	    catch (SQLException ex)
	    {
		ex.printStackTrace();
	    }
	    sql = null;
	    return flgResult;
	}
    }

    public boolean funPostDayEndData(String newStartDate, int shiftCode)
    {
	boolean flgResult = false;
	StringBuilder sql = new StringBuilder();
	ResultSet rsDayEnd = null;
	Map<String, String> hmDayEndData = new HashMap<String, String>();
	try
	{
	    sql.setLength(0);
	    sql.append("select * from tbldayendprocess where strDayEnd='Y' and strDataPostFlag='N' "
		    + " and strPOSCode='" + clsGlobalVarClass.gPOSCode + "' ");
	    rsDayEnd = dbMysql.executeResultSet(sql.toString());
	    JSONObject rootObject = new JSONObject();
	    JSONArray dataObjectArray = new JSONArray();

	    while (rsDayEnd.next())
	    {
		JSONObject dataObject = new JSONObject();

		hmDayEndData.put(rsDayEnd.getString("dtePOSDate"), rsDayEnd.getString("strPOSCode"));
		dataObject.put("POSCode", rsDayEnd.getString("strPOSCode"));
		dataObject.put("POSDate", rsDayEnd.getString("dtePOSDate"));
		dataObject.put("DayEnd", rsDayEnd.getString("strDayEnd"));
		dataObject.put("TotalSale", rsDayEnd.getString("dblTotalSale"));
		dataObject.put("NoOfBill", rsDayEnd.getString("dblNoOfBill"));
		dataObject.put("NoOfVoidedBill", rsDayEnd.getString("dblNoOfVoidedBill"));
		dataObject.put("NoOfModifyBill", rsDayEnd.getString("dblNoOfModifyBill"));
		dataObject.put("HDAmt", rsDayEnd.getString("dblHDAmt"));
		dataObject.put("DiningAmt", rsDayEnd.getString("dblDiningAmt"));
		dataObject.put("TakeAway", rsDayEnd.getString("dblTakeAway"));
		dataObject.put("Float", rsDayEnd.getString("dblFloat"));
		dataObject.put("Cash", rsDayEnd.getString("dblCash"));
		dataObject.put("Advance", rsDayEnd.getString("dblAdvance"));
		dataObject.put("TransferIn", rsDayEnd.getString("dblTransferIn"));
		dataObject.put("TotalReceipt", rsDayEnd.getString("dblTotalReceipt"));
		dataObject.put("Payments", rsDayEnd.getString("dblPayments"));
		dataObject.put("Withdrawal", rsDayEnd.getString("dblWithdrawal"));
		dataObject.put("TransferOut", rsDayEnd.getString("dblTransferOut"));
		dataObject.put("TotalPay", rsDayEnd.getString("dblTotalPay"));
		dataObject.put("CashInHand", rsDayEnd.getString("dblCashInHand"));
		dataObject.put("Refund", rsDayEnd.getString("dblRefund"));
		dataObject.put("TotalDiscount", rsDayEnd.getString("dblTotalDiscount"));
		dataObject.put("NoOfDiscountedBill", rsDayEnd.getString("dblNoOfDiscountedBill"));
		dataObject.put("ShiftCode", rsDayEnd.getString("intShiftCode"));
		dataObject.put("ShiftEnd", rsDayEnd.getString("strShiftEnd"));
		dataObject.put("TotalPax", rsDayEnd.getString("intTotalPax"));
		dataObject.put("NoOfTakeAway", rsDayEnd.getString("intNoOfTakeAway"));
		dataObject.put("NoOfHomeDelivery", rsDayEnd.getString("intNoOfHomeDelivery"));
		dataObject.put("UserCreated", rsDayEnd.getString("strUserCreated"));
		dataObject.put("DateCreated", rsDayEnd.getString("dteDateCreated"));
		dataObject.put("DayEndDateTime", rsDayEnd.getString("dteDayEndDateTime"));
		dataObject.put("UserEdited", rsDayEnd.getString("strUserEdited"));
		dataObject.put("DataPostFlag", rsDayEnd.getString("strDataPostFlag"));
		dataObject.put("NoOfNCKOT", rsDayEnd.getString("intNoOfNCKOT"));
		dataObject.put("NoOfComplimentaryKOT", rsDayEnd.getString("intNoOfComplimentaryKOT"));
		dataObject.put("NoOfVoidKOT", rsDayEnd.getString("intNoOfVoidKOT"));
		dataObject.put("UsedDebitCardBalance", rsDayEnd.getString("dblUsedDebitCardBalance"));
		dataObject.put("UnusedDebitCardBalance", rsDayEnd.getString("dblUnusedDebitCardBalance"));
		dataObject.put("WSStockAdjustmentNo", rsDayEnd.getString("strWSStockAdjustmentNo"));
		dataObject.put("TipAmt", rsDayEnd.getString("dblTipAmt"));
		dataObject.put("strExciseBillGeneration", rsDayEnd.getString("strExciseBillGeneration"));
		dataObject.put("dblNetSale", rsDayEnd.getString("dblNetSale"));
		dataObject.put("dblGrossSale", rsDayEnd.getString("dblGrossSale"));
		dataObject.put("dblAPC", rsDayEnd.getString("dblAPC"));

		dataObject.put("StartDate", newStartDate);
		dataObject.put("NewShiftCode", shiftCode);
		dataObject.put("ClientCode", clsGlobalVarClass.gClientCode);
		dataObjectArray.add(dataObject);
	    }
	    rootObject.put("tbldayendprocess", dataObjectArray);
	    String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostMasterData";
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
	    System.out.println("Day End Posting= " + op);
	    if (op.equalsIgnoreCase("true"))
	    {
		StringBuilder sbSql = new StringBuilder();
		for (Map.Entry<String, String> entry : hmDayEndData.entrySet())
		{
		    sbSql.setLength(0);
		    sbSql.append("update tbldayendprocess set strDataPostFlag='Y' "
			    + " where strPOSCode='" + entry.getValue() + "' and dtePOSDate='" + entry.getKey() + "' ");
		    clsGlobalVarClass.dbMysql.execute(sbSql.toString());
		}
		sbSql = null;
	    }
	    conn.disconnect();
	    flgResult = true;
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    try
	    {
		rsDayEnd.close();
	    }
	    catch (SQLException ex)
	    {
		ex.printStackTrace();
	    }
	    sql = null;
	    return flgResult;
	}
    }

    public void funPostDayEndDtlData(String formName) throws Exception
    {
	boolean flgDataPosting = false;
	String query = " select *,if(dteDayEndDateTime='0000-00-00 00:00:00','NA',dteDayEndDateTime) as dayEndDateTime "
		+ " from tbldayendprocess where "
		+ "   strDataPostFlag='N' ";//strDayEnd='Y'
	ResultSet rsDayEnd = clsGlobalVarClass.dbMysql.executeResultSet(query);
	JSONObject rootObject = new JSONObject();
	JSONArray dataObjectArray = new JSONArray();

	while (rsDayEnd.next())
	{
	    JSONObject dataObject = new JSONObject();

	    dataObject.put("POSCode", rsDayEnd.getString("strPOSCode"));
	    dataObject.put("POSDate", rsDayEnd.getString("dtePOSDate"));
	    dataObject.put("DayEnd", rsDayEnd.getString("strDayEnd"));
	    dataObject.put("TotalSale", rsDayEnd.getString("dblTotalSale"));
	    dataObject.put("NoOfBill", rsDayEnd.getString("dblNoOfBill"));
	    dataObject.put("NoOfVoidedBill", rsDayEnd.getString("dblNoOfVoidedBill"));
	    dataObject.put("NoOfModifyBill", rsDayEnd.getString("dblNoOfModifyBill"));
	    dataObject.put("HDAmt", rsDayEnd.getString("dblHDAmt"));
	    dataObject.put("DiningAmt", rsDayEnd.getString("dblDiningAmt"));
	    dataObject.put("TakeAway", rsDayEnd.getString("dblTakeAway"));
	    dataObject.put("Float", rsDayEnd.getString("dblFloat"));
	    dataObject.put("Cash", rsDayEnd.getString("dblCash"));
	    dataObject.put("Advance", rsDayEnd.getString("dblAdvance"));
	    dataObject.put("TransferIn", rsDayEnd.getString("dblTransferIn"));
	    dataObject.put("TotalReceipt", rsDayEnd.getString("dblTotalReceipt"));
	    dataObject.put("Payments", rsDayEnd.getString("dblPayments"));
	    dataObject.put("Withdrawal", rsDayEnd.getString("dblWithdrawal"));
	    dataObject.put("TransferOut", rsDayEnd.getString("dblTransferOut"));
	    dataObject.put("TotalPay", rsDayEnd.getString("dblTotalPay"));
	    dataObject.put("CashInHand", rsDayEnd.getString("dblCashInHand"));
	    dataObject.put("Refund", rsDayEnd.getString("dblRefund"));
	    dataObject.put("TotalDiscount", rsDayEnd.getString("dblTotalDiscount"));
	    dataObject.put("NoOfDiscountedBill", rsDayEnd.getString("dblNoOfDiscountedBill"));
	    dataObject.put("ShiftCode", rsDayEnd.getString("intShiftCode"));
	    dataObject.put("ShiftEnd", rsDayEnd.getString("strShiftEnd"));
	    dataObject.put("TotalPax", rsDayEnd.getString("intTotalPax"));
	    dataObject.put("NoOfTakeAway", rsDayEnd.getString("intNoOfTakeAway"));
	    dataObject.put("NoOfHomeDelivery", rsDayEnd.getString("intNoOfHomeDelivery"));
	    dataObject.put("UserCreated", rsDayEnd.getString("strUserCreated"));
	    dataObject.put("DateCreated", rsDayEnd.getString("dteDateCreated"));
	    //dataObject.put("DayEndDateTime", rsDayEnd.getString("dteDayEndDateTime"));

	    String dayEndDateTime = rsDayEnd.getString("dayEndDateTime");
	    if (null == dayEndDateTime || dayEndDateTime.equals("NA"))
	    {
		dataObject.put("DayEndDateTime", null);
	    }
	    else
	    {
		dataObject.put("DayEndDateTime", dayEndDateTime);
	    }
	    dataObject.put("UserEdited", rsDayEnd.getString("strUserEdited"));
	    dataObject.put("DataPostFlag", rsDayEnd.getString("strDataPostFlag"));
	    dataObject.put("NoOfNCKOT", rsDayEnd.getString("intNoOfNCKOT"));
	    dataObject.put("NoOfComplimentaryKOT", rsDayEnd.getString("intNoOfComplimentaryKOT"));
	    dataObject.put("NoOfVoidKOT", rsDayEnd.getString("intNoOfVoidKOT"));
	    dataObject.put("UsedDebitCardBalance", rsDayEnd.getString("dblUsedDebitCardBalance"));
	    dataObject.put("UnusedDebitCardBalance", rsDayEnd.getString("dblUnusedDebitCardBalance"));
	    dataObject.put("WSStockAdjustmentNo", rsDayEnd.getString("strWSStockAdjustmentNo"));
	    dataObject.put("TipAmt", rsDayEnd.getString("dblTipAmt"));
	    dataObject.put("ClientCode", clsGlobalVarClass.gClientCode);

	    dataObject.put("strExciseBillGeneration", rsDayEnd.getString("strExciseBillGeneration"));
	    dataObject.put("dblNetSale", rsDayEnd.getString("dblNetSale"));
	    dataObject.put("dblGrossSale", rsDayEnd.getString("dblGrossSale"));
	    dataObject.put("dblAPC", rsDayEnd.getString("dblAPC"));

	    dataObjectArray.add(dataObject);
	}
	rootObject.put("tbldayendprocess", dataObjectArray);
	String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostTransactionDataToHOPOS";

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
	conn.disconnect();

	System.out.println("Manual day end post = " + op);
	if (op.equals("true"))
	{
	    dbMysql.execute("update tbldayendprocess set strDataPostFlag='Y' where strDataPostFlag='N' and strDayEnd='Y' ");
	    flgDataPosting = true;
	}
	else
	{
	    flgDataPosting = false;
	}

	if (formName.equals("ManuallyLive") || formName.equals("ManuallyQFile"))
	{
	    if (flgDataPosting)
	    {
		JOptionPane.showMessageDialog(null, "Data Posted Successfully!!!");
	    }
	}
    }

    public int funPostCashManagementData() throws Exception
    {
	String transId = "";
	String query = "";

	query = "select * from tblcashmanagement where strDataPostFlag='N' limit 2000";

	ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	JSONObject objJson = new JSONObject();
	JSONArray arrObj = new JSONArray();
	transId = "";

	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    transId += ",'" + rs.getString("strTransID") + "'";

	    objRows.put("TransId", rs.getString("strTransID"));
	    objRows.put("TransType", rs.getString("strTransType"));
	    objRows.put("TransDate", rs.getString("dteTransDate"));
	    objRows.put("ReasonCode", rs.getString("strReasonCode"));
	    objRows.put("POSCode", rs.getString("strPOSCode"));
	    objRows.put("Amount", rs.getString("dblAmount"));
	    objRows.put("Remarks", rs.getString("strRemarks"));
	    objRows.put("UserCreated", rs.getString("strUserCreated"));
	    objRows.put("UserEdited", rs.getString("strUserEdited"));
	    objRows.put("DateCreated", rs.getString("dteDateCreated"));
	    objRows.put("DateEdited", rs.getString("dteDateEdited"));
	    objRows.put("ClientCode", rs.getString("strClientCode"));
	    objRows.put("CurrencyType", rs.getString("strCurrencyType"));
	    objRows.put("ShiftCode", rs.getString("intShiftCode"));
	    objRows.put("Against", rs.getString("strAgainst"));
	    objRows.put("RollingAmt", rs.getString("dblRollingAmt"));
	    objRows.put("DataPostFlag", rs.getString("strDataPostFlag"));

	    arrObj.add(objRows);
	}
	rs.close();
	objJson.put("CashManagementData", arrObj);

	String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostTransactionDataToHOPOS";

	URL url = new URL(hoURL);
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setDoOutput(true);
	conn.setRequestMethod("POST");
	conn.setRequestProperty("Content-Type", "application/json");
	OutputStream os = conn.getOutputStream();
	os.write(objJson.toString().getBytes());
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
	System.out.println("Cash management=" + op);
	conn.disconnect();

	boolean flgDataPosting = false;
	if (op.equals("true"))
	{
	    StringBuilder sbUpdate = new StringBuilder(transId);
	    if (transId.length() > 0)
	    {
		transId = sbUpdate.delete(0, 1).toString();
		//System.out.println("Billsss="+updateBills);
		dbMysql.execute("update tblcashmanagement set strDataPostFlag='Y' where strTransId in (" + transId + ")");
	    }
	    flgDataPosting = true;
	}
	else
	{
	    flgDataPosting = false;
	}
	if (flgDataPosting)
	{
	    //JOptionPane.showMessageDialog(null, "Data Posted Successfully!!!");
	}

	return 1;
    }

    public boolean funPostPOSItemSalesData(String itemType, String posCode, String dateFrom, String dateTo)
    {
	boolean flgResult = false;
	try
	{
	    JSONObject rootObject = new JSONObject();
	    JSONArray dataObjectArray = new JSONArray();
	    String sql = "", qFileSql = "";

	    String filter = "";
	    if (posCode.equalsIgnoreCase("All"))
	    {
		filter = " and date(a.dteBillDate) between '" + dateFrom + "' and '" + dateTo + "' ";
	    }
	    else
	    {
		filter = " and a.strPOSCode='" + posCode + "' and date(a.dteBillDate) between '" + dateFrom + "' and '" + dateTo + "' ";
	    }

	    if (itemType.equals("Both"))
	    {
		sql = "select b.strItemCode,b.strItemName,sum(b.dblQuantity),b.dblRate,a.strPOSCode"
			+ ",date(a.dteBillDate),'" + gClientCode + "' "
			+ "from tblbillhd a,tblbilldtl b "
			+ "where a.strBillNo=b.strBillNo and b.strMMSDataPostFlag='N' "
			+ filter
			+ "group by b.strItemCode";

		qFileSql = "select b.strItemCode,b.strItemName,sum(b.dblQuantity),b.dblRate,a.strPOSCode"
			+ ",date(a.dteBillDate),'" + gClientCode + "' "
			+ "from tblqbillhd a,tblqbilldtl b "
			+ "where a.strBillNo=b.strBillNo and b.strMMSDataPostFlag='N' "
			+ filter
			+ "group by b.strItemCode";
	    }
	    else if (itemType.equals("Liquor"))
	    {
		sql = "select b.strItemCode,b.strItemName,sum(b.dblQuantity),b.dblRate,a.strPOSCode"
			+ ",date(a.dteBillDate),'" + gClientCode + "' "
			+ "from tblbillhd a,tblbilldtl b, tblitemmaster c "
			+ "where a.strBillNo=b.strBillNo and b.strItemCode=c.strItemCode "
			+ "and c.strItemType='Liquor' and b.strMMSDataPostFlag='N' "
			+ filter
			+ "group by b.strItemCode";

		qFileSql = "select b.strItemCode,b.strItemName,sum(b.dblQuantity),b.dblRate,a.strPOSCode"
			+ ",date(a.dteBillDate),'" + gClientCode + "' "
			+ "from tblqbillhd a,tblqbilldtl b, tblitemmaster c "
			+ "where a.strBillNo=b.strBillNo and b.strItemCode=c.strItemCode "
			+ "and c.strItemType='Liquor' and b.strMMSDataPostFlag='N' "
			+ filter
			+ "group by b.strItemCode";
	    }
	    else
	    {
		sql = "select b.strItemCode,b.strItemName,sum(b.dblQuantity),b.dblRate,a.strPOSCode"
			+ ",date(a.dteBillDate),'" + gClientCode + "' "
			+ "from tblbillhd a,tblbilldtl b, tblitemmaster c "
			+ "where a.strBillNo=b.strBillNo and b.strItemCode=c.strItemCode "
			+ "and c.strItemType='Food' and b.strMMSDataPostFlag='N' "
			+ filter
			+ "group by b.strItemCode";

		qFileSql = "select b.strItemCode,b.strItemName,sum(b.dblQuantity),b.dblRate,a.strPOSCode"
			+ ",date(a.dteBillDate),'" + gClientCode + "' "
			+ "from tblqbillhd a,tblqbilldtl b, tblitemmaster c "
			+ "where a.strBillNo=b.strBillNo and b.strItemCode=c.strItemCode "
			+ "and c.strItemType='Food' and b.strMMSDataPostFlag='N' "
			+ filter
			+ "group by b.strItemCode";
	    }

	    //System.out.println(sql);
	    //System.out.println(qFileSql);
	    ResultSet rsItemSales = dbMysql.executeResultSet(sql);
	    while (rsItemSales.next())
	    {
		JSONObject dataObject = new JSONObject();
		dataObject.put("posItemCode", rsItemSales.getString(1));
		dataObject.put("posItemName", rsItemSales.getString(2));
		dataObject.put("quantity", rsItemSales.getString(3));
		dataObject.put("rate", rsItemSales.getString(4));
		dataObject.put("posCode", rsItemSales.getString(5));
		dataObject.put("billDate", rsItemSales.getString(6));
		dataObject.put("clientCode", rsItemSales.getString(7));
		dataObjectArray.add(dataObject);
	    }
	    rsItemSales.close();

	    rsItemSales = dbMysql.executeResultSet(qFileSql);
	    while (rsItemSales.next())
	    {
		JSONObject dataObject = new JSONObject();
		dataObject.put("posItemCode", rsItemSales.getString(1));
		dataObject.put("posItemName", rsItemSales.getString(2));
		dataObject.put("quantity", rsItemSales.getString(3));
		dataObject.put("rate", rsItemSales.getString(4));
		dataObject.put("posCode", rsItemSales.getString(5));
		dataObject.put("billDate", rsItemSales.getString(6));
		dataObject.put("clientCode", rsItemSales.getString(7));
		dataObjectArray.add(dataObject);
	    }
	    rsItemSales.close();
	    rootObject.put("MemberPOSSalesInfo", dataObjectArray);

	    String hoURL = gSanguineWebServiceURL + "/MMSIntegration/funPostPOSSaleData";
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
		throw new RuntimeException("Failed : HTTP error code : "
			+ conn.getResponseCode());
	    }
	    BufferedReader br = new BufferedReader(new InputStreamReader(
		    (conn.getInputStream())));

	    String output = "", op = "Updated successfully: ";
	    while ((output = br.readLine()) != null)
	    {
		op += output;
	    }
	    System.out.println(op);
	    conn.disconnect();

	    if (op.split(":")[1] != null && op.split(":")[1].trim().equalsIgnoreCase("true"))
	    {
		flgResult = true;
	    }
	    if (flgResult)
	    {
		String updateLiveBillDtl = "update tblbilldtl a join "
			+ " ( select b.strBillNo as BillNo,b.strPOSCode as POSCode"
			+ ", b.dteBillDate as BillDate from tblbillhd b "
			+ " where date(b.dteBillDate) between '" + dateFrom + "' and '" + dateTo + "' ) c "
			+ " on a.strbillno=c.BillNo "
			+ " set a.strMMSDataPostFlag='Y' "
			+ " where date(c.BillDate) between '" + dateFrom + "' and '" + dateTo + "' ";
		if (!posCode.equalsIgnoreCase("All"))
		{
		    updateLiveBillDtl += " and c.POSCode='" + posCode + "' ";
		}
		dbMysql.execute(updateLiveBillDtl);

		String updateQFileBillDtl = "update tblqbilldtl a join "
			+ " ( select b.strBillNo as BillNo,b.strPOSCode as POSCode"
			+ ", b.dteBillDate as BillDate from tblqbillhd b "
			+ " where date(b.dteBillDate) between '" + dateFrom + "' and '" + dateTo + "' ) c "
			+ " on a.strbillno=c.BillNo "
			+ " set a.strMMSDataPostFlag='Y' "
			+ " where date(c.BillDate) between '" + dateFrom + "' and '" + dateTo + "' ";
		if (!posCode.equalsIgnoreCase("All"))
		{
		    updateQFileBillDtl += " and c.POSCode='" + posCode + "' ";
		}
		dbMysql.execute(updateQFileBillDtl);
	    }
	}
	catch (Exception e)
	{
	    flgResult = false;
	    e.printStackTrace();
	}
	finally
	{
	    return flgResult;
	}
    }
    //*****For MMS*******//  

    public boolean funPostPOSItemSalesDataAuto(String itemType, String posCode, final String dateFrom, final String dateTo)
    {
	boolean flgResult = false;
	String WSStockAdjustmentCode = "";
	String sqlLiveLinkupFile = "", sqlQLinkupFile = "";
	final ArrayList<ArrayList<String>> arrUnLinkedItemDtl = new ArrayList<ArrayList<String>>();
	try
	{
	    String costCenterCode = "";
	    String costCenterName = "";
	    String WSLocationCode = "";
	    String WSLocationName = "";
	    final String posName = "";

	    String filter1 = "";
	    if (posCode.equalsIgnoreCase("All"))
	    {
		filter1 = " and date(a.dteBillDate) between '" + dateFrom + "' and '" + dateTo + "' ";
	    }
	    else
	    {
		filter1 = " and a.strPOSCode='" + posCode + "' and date(a.dteBillDate) between '" + dateFrom + "' and '" + dateTo + "' ";
	    }

	    //delete currupted data
	    String sqlDelete = "delete from tblitemmasterlinkupdtl where strItemCode='' ";
	    clsGlobalVarClass.dbMysql.execute(sqlDelete);

	    //insert items data in linkup table whoes are not linkedup
	    //Q
	    String sqlInsert = "insert into tblitemmasterlinkupdtl(select a.strItemCode,'" + posCode + "','','','','','" + clsGlobalVarClass.gClientCode + "','N' "
		    + "from tblqbilldtl  a "
		    + "left outer join tblitemmasterlinkupdtl b on a.strItemCode=b.strItemCode "
		    + "where date(a.dteBillDate) between '" + dateFrom + "' and '" + dateTo + "' "
		    + "and b.strItemCode is null "
		    + "group by a.strItemCode )";
	    clsGlobalVarClass.dbMysql.execute(sqlInsert);
	    //live
	    sqlInsert = "insert into tblitemmasterlinkupdtl(select a.strItemCode,'" + posCode + "','','','','','" + clsGlobalVarClass.gClientCode + "','N' "
		    + "from tblbilldtl  a "
		    + "left outer join tblitemmasterlinkupdtl b on a.strItemCode=b.strItemCode "
		    + "where date(a.dteBillDate) between '" + dateFrom + "' and '" + dateTo + "' "
		    + "and b.strItemCode is null "
		    + "group by a.strItemCode) ";
	    clsGlobalVarClass.dbMysql.execute(sqlInsert);

	    sqlLiveLinkupFile = " SELECT b.strItemCode,c.strItemName,d.strPosName,e.strWSProductCode,e.strWSProductName,e.strExciseBrandCode,e.strExciseBrandName "
		    + "FROM tblbillhd a,tblbilldtl b,tblitemmaster c,tblposmaster d,tblitemmasterlinkupdtl e "
		    + "WHERE a.strBillNo=b.strBillNo  "
		    + "AND b.strItemCode=c.strItemCode "
		    + "AND a.strPOSCode=d.strPosCode  "
		    + "and b.strItemCode=e.strItemCode "
		    + "and (a.strPOSCode=e.strPOSCode or e.strPOSCode='All') "
		    + "and LENGTH(e.strWSProductCode)<3   "
		    + filter1;
	    if (!itemType.equals("Both"))
	    {
		sqlLiveLinkupFile += " and c.strItemType='" + itemType + "' ";
	    }
	    sqlLiveLinkupFile += " group by b.strItemCode,d.strPosCode ";

	    sqlQLinkupFile = " SELECT b.strItemCode,c.strItemName,d.strPosName,e.strWSProductCode,e.strWSProductName,e.strExciseBrandCode,e.strExciseBrandName "
		    + "FROM tblqbillhd a,tblqbilldtl b,tblitemmaster c,tblposmaster d,tblitemmasterlinkupdtl e "
		    + "WHERE a.strBillNo=b.strBillNo  "
		    + "AND b.strItemCode=c.strItemCode "
		    + "AND a.strPOSCode=d.strPosCode  "
		    + "and b.strItemCode=e.strItemCode "
		    + "and (a.strPOSCode=e.strPOSCode or e.strPOSCode='All') "
		    + "and LENGTH(e.strWSProductCode)<3   "
		    + filter1;
	    if (!itemType.equals("Both"))
	    {
		sqlQLinkupFile += " and c.strItemType='" + itemType + "' ";
	    }
	    sqlQLinkupFile += " group by b.strItemCode,d.strPosCode ";

	    //System.out.println(sqlLiveLinkupFile);
	    //System.out.println(sqlQLinkupFile);
	    ResultSet rsLiveLinkupDtl = dbMysql.executeResultSet(sqlLiveLinkupFile);
	    while (rsLiveLinkupDtl.next())
	    {
		ArrayList<String> arrUnLinkedItem = new ArrayList<String>();
		arrUnLinkedItem.add(rsLiveLinkupDtl.getString(1));
		arrUnLinkedItem.add(rsLiveLinkupDtl.getString(2));
//                posName = rsLiveLinkupDtl.getString(3);
		arrUnLinkedItemDtl.add(arrUnLinkedItem);
	    }
	    rsLiveLinkupDtl.close();

	    ResultSet rsQLinkupDtl = dbMysql.executeResultSet(sqlQLinkupFile);
	    while (rsQLinkupDtl.next())
	    {
		ArrayList<String> arrUnLinkedItem = new ArrayList<String>();
		arrUnLinkedItem.add(rsQLinkupDtl.getString(1));
		arrUnLinkedItem.add(rsQLinkupDtl.getString(2));
//                posName = rsQLinkupDtl.getString(3);
		arrUnLinkedItemDtl.add(arrUnLinkedItem);
	    }
	    rsQLinkupDtl.close();

	    if (arrUnLinkedItemDtl.size() > 0)
	    {
		flgResult = false;

		new Thread()
		{

		    @Override
		    public void run()
		    {
			JOptionPane.showMessageDialog(null, "some Items needs to be linkup with web stock product code");
			new clsUtility().funGenerateLinkupTextfile(arrUnLinkedItemDtl, dateFrom, dateTo, posName);
		    }

		}.start();
	    }
	    else
	    {
		String queryDayEnd = "select a.strPOSCode,a.dtePOSDate,a.intShiftCode,ifnull(a.strWSStockAdjustmentNo,'') strWSStockAdjustmentNo,a.strDayEnd,a.strShiftEnd "
			+ "from tbldayendprocess a "
			+ "where date(a.dtePOSDate) between '" + dateFrom + "' and '" + dateTo + "' "
			+ "and a.strDayEnd='Y' "
			+ "and a.strShiftEnd='Y' "
			+ "and a.strPOSCode='" + posCode + "' ";

		ResultSet rsDayEndData = clsGlobalVarClass.dbMysql.executeResultSet(queryDayEnd);
		while (rsDayEndData.next())
		{
		    JSONArray rootArr = new JSONArray();
		    String dayEndPOSCode = rsDayEndData.getString(1);//posCode
		    String posDayEndDate = rsDayEndData.getString(2);//posDayEndDate
		    String posShiftEndCode = rsDayEndData.getString(3);//posShiftEndCode
		    String posDayEndWSStockAdjNo = rsDayEndData.getString(4);//posWSStockAdjNo
		    //*****************************************************************//

		    String sqlLocationNCostCenter = " select a.strCostCenterCode,a.strCostCenterName,a.strWSLocationCode,a.strWSLocationName  "
			    + " from tblcostcentermaster a "
			    + " where LENGTH(a.strWSLocationCode)>0 "
			    + " and LENGTH(a.strWSLocationName)>0 "
			    + " and a.strClientCode='" + gClientCode + "' "
			    //+ " and a.strCostCenterCode='C03'   "
			    + " ";
		    ResultSet rsLoc = dbMysql.executeResultSet(sqlLocationNCostCenter);

		    List<clsPOSLinkupDtl> listCostcenterLoc = new ArrayList<clsPOSLinkupDtl>();

		    if (clsGlobalVarClass.gMMSSalesDataPostEffectCostOrLoc.equalsIgnoreCase("Cost Center"))//cost center wise posting
		    {
			while (rsLoc.next())
			{
			    clsPOSLinkupDtl objCostcenterLoc = new clsPOSLinkupDtl();
			    objCostcenterLoc.setStrCostCenterCode(rsLoc.getString(1));
			    objCostcenterLoc.setStrCostCenterName(rsLoc.getString(2));
			    objCostcenterLoc.setStrWSLocationCode(rsLoc.getString(3));
			    objCostcenterLoc.setStrWSLocationName(rsLoc.getString(4));
			    listCostcenterLoc.add(objCostcenterLoc);
			}
		    }
		    else //Location  wise posting
		    {
			HashMap<String, clsPOSLinkupDtl> hmCostcenterLoc = new HashMap<String, clsPOSLinkupDtl>();
			while (rsLoc.next())
			{
			    if (hmCostcenterLoc.containsKey(rsLoc.getString(3)))
			    {
				clsPOSLinkupDtl objCostcenterLoc = hmCostcenterLoc.get(rsLoc.getString(3));
				objCostcenterLoc.setStrCostCenterCode(objCostcenterLoc.getStrCostCenterCode() + "," + rsLoc.getString(1));
				objCostcenterLoc.setStrCostCenterName(objCostcenterLoc.getStrCostCenterName() + "," + rsLoc.getString(2));
				objCostcenterLoc.setStrWSLocationCode(rsLoc.getString(3));
				objCostcenterLoc.setStrWSLocationName(rsLoc.getString(4));
				hmCostcenterLoc.put(rsLoc.getString(3), objCostcenterLoc);
			    }
			    else
			    {
				clsPOSLinkupDtl objCostcenterLoc = new clsPOSLinkupDtl();
				objCostcenterLoc.setStrCostCenterCode(rsLoc.getString(1));
				objCostcenterLoc.setStrCostCenterName(rsLoc.getString(2));
				objCostcenterLoc.setStrWSLocationCode(rsLoc.getString(3));
				objCostcenterLoc.setStrWSLocationName(rsLoc.getString(4));
				hmCostcenterLoc.put(rsLoc.getString(3), objCostcenterLoc);
			    }
			}
			for (Map.Entry<String, clsPOSLinkupDtl> entry : hmCostcenterLoc.entrySet())
			{
			    clsPOSLinkupDtl obj = entry.getValue();
			    listCostcenterLoc.add(obj);
			}
		    }

		    for (clsPOSLinkupDtl obj : listCostcenterLoc)
		    {
			costCenterCode = obj.getStrCostCenterCode();
			costCenterName = obj.getStrCostCenterName();
			WSLocationCode = obj.getStrWSLocationCode();
			WSLocationName = obj.getStrWSLocationName();

			JSONArray dataObjectArray = new JSONArray();
			String sql = "", qFileSql = "";
			String filter = "";

			String[] costcenterCodes = costCenterCode.split(",");
			String costcenterCriteria = "";
			for (String strcc : costcenterCodes)
			{
			    costcenterCriteria += " f.strCostCenterCode='" + strcc + "' or";
			}
			costcenterCriteria = costcenterCriteria.substring(0, costcenterCriteria.length() - 2);

			if (dayEndPOSCode.equalsIgnoreCase("All"))
			{
			    filter = " and date(a.dteBillDate) between '" + posDayEndDate + "' and '" + posDayEndDate + "' and "
				    + " ( " + costcenterCriteria + " )  ";
			}
			else
			{
			    filter = " and a.strPOSCode='" + posCode + "' "
				    + " and c.strPOSCode='" + posCode + "'  "
				    + " and (f.strPosCode='" + posCode + "' or f.strPosCode='All') "
				    + " and a.intShiftCode='" + posShiftEndCode + "' "
				    + " and date(a.dteBillDate) between '" + posDayEndDate + "' and '" + posDayEndDate + "' and "
				    + " ( " + costcenterCriteria + " )  ";
			}

			if (itemType.equals("Both"))
			{
			    sql = "select b.strItemCode,b.strItemName,sum(b.dblQuantity),b.dblRate,a.strPOSCode"
				    + ",date(a.dteBillDate),'" + gClientCode + "', c.strWSProductCode,d.strPosName"
				    + ",sum(b.dblAmount),sum(b.dblDiscountPer),sum(b.dblDiscountAmt),h.strItemType "
				    + "from tblbillhd a,tblbilldtl b, tblitemmasterlinkupdtl c,tblposmaster d, "
				    + "tblmenuitempricingdtl f,tblcostcentermaster g,tblitemmaster h "
				    + "where a.strBillNo=b.strBillNo  "
				    + "and b.strItemCode=c.strItemCode "
				    + "and b.strItemCode=h.strItemCode "
				    + "and a.strPOSCode=d.strPOSCode "
				    + "and b.strItemCode=f.strItemCode "
				    + "and f.strCostCenterCode=g.strCostCenterCode  "
				    + "and f.strHourlyPricing='NO'  "
				    + filter
				    + "group by f.strCostCenterCode,b.strItemCode order by f.strCostCenterCode,a.dteBillDate ";

			    qFileSql = "select b.strItemCode,b.strItemName,sum(b.dblQuantity),b.dblRate,a.strPOSCode"
				    + ",date(a.dteBillDate),'" + gClientCode + "', c.strWSProductCode,d.strPosName "
				    + ",sum(b.dblAmount),sum(b.dblDiscountPer),sum(b.dblDiscountAmt),h.strItemType "
				    + "from tblqbillhd a,tblqbilldtl b, tblitemmasterlinkupdtl c,tblposmaster d,  "
				    + "tblmenuitempricingdtl f,tblcostcentermaster g,tblitemmaster h "
				    + "where a.strBillNo=b.strBillNo  "
				    + "and b.strItemCode=c.strItemCode "
				    + "and b.strItemCode=h.strItemCode "
				    + "and a.strPOSCode=d.strPOSCode "
				    + "and b.strItemCode=f.strItemCode "
				    + "and f.strCostCenterCode=g.strCostCenterCode  "
				    + filter
				    + "group by f.strCostCenterCode,b.strItemCode order by f.strCostCenterCode,a.dteBillDate ";
			}
			else if (itemType.equals("Liquor"))
			{
			    sql = "select b.strItemCode,b.strItemName,sum(b.dblQuantity),b.dblRate,a.strPOSCode"
				    + ",date(a.dteBillDate),'" + gClientCode + "' ,c.strWSProductCode,e.strPosName, "
				    + " f.strCostCenterCode,g.strCostCenterName "
				    + ",sum(b.dblAmount),sum(b.dblDiscountPer),sum(b.dblDiscountAmt),d.strItemType "
				    + "from tblbillhd a,tblbilldtl b, tblitemmasterlinkupdtl c,tblitemmaster d,tblposmaster e,  "
				    + "tblmenuitempricingdtl f,tblcostcentermaster g "
				    + "where a.strBillNo=b.strBillNo "
				    + "and b.strItemCode=c.strItemCode "
				    + "and b.strItemCode=d.strItemCode "
				    + "and a.strPOSCode=e.strPOSCode "
				    + "and d.strItemType='Liquor'  "
				    + "and b.strItemCode=c.strItemCode "
				    + "and d.strItemCode=f.strItemCode "
				    + "and f.strCostCenterCode=g.strCostCenterCode  "
				    + "and f.strHourlyPricing='NO' "				    
				    + filter
				    + "group by f.strCostCenterCode,b.strItemCode order by f.strCostCenterCode,a.dteBillDate ";

			    qFileSql = "select b.strItemCode,b.strItemName,sum(b.dblQuantity),b.dblRate,a.strPOSCode"
				    + ",date(a.dteBillDate),'" + gClientCode + "' ,c.strWSProductCode,e.strPosName, "
				    + " f.strCostCenterCode,g.strCostCenterName "
				    + ",sum(b.dblAmount),sum(b.dblDiscountPer),sum(b.dblDiscountAmt),d.strItemType "
				    + "from tblqbillhd a,tblqbilldtl b, tblitemmasterlinkupdtl c,tblitemmaster d,tblposmaster e, "
				    + "tblmenuitempricingdtl f,tblcostcentermaster g "
				    + "where a.strBillNo=b.strBillNo "
				    + "and b.strItemCode=c.strItemCode "
				    + "and b.strItemCode=d.strItemCode "
				    + "and a.strPOSCode=e.strPOSCode "
				    + "and d.strItemType='Liquor'  "
				    + "and b.strItemCode=c.strItemCode "
				    + "and d.strItemCode=f.strItemCode "
				    + "and f.strCostCenterCode=g.strCostCenterCode  "
				    + "and f.strHourlyPricing='NO'  "
				    //+ "and b.strItemCode='I000185' "
				    + filter
				    + "group by f.strCostCenterCode,b.strItemCode order by f.strCostCenterCode,a.dteBillDate ";
			}
			else
			{
			    sql = "select b.strItemCode,b.strItemName,sum(b.dblQuantity),b.dblRate,a.strPOSCode"
				    + ",date(a.dteBillDate),'" + gClientCode + "' ,c.strWSProductCode,e.strPosName, "
				    + " f.strCostCenterCode,g.strCostCenterName "
				    + ",sum(b.dblAmount),sum(b.dblDiscountPer),sum(b.dblDiscountAmt),d.strItemType "
				    + "from tblbillhd a,tblbilldtl b, tblitemmasterlinkupdtl c,tblitemmaster d,tblposmaster e,  "
				    + "tblmenuitempricingdtl f,tblcostcentermaster g "
				    + "where a.strBillNo=b.strBillNo "
				    + "and b.strItemCode=c.strItemCode "
				    + "and b.strItemCode=d.strItemCode "
				    + "and a.strPOSCode=e.strPOSCode "
				    + "and d.strItemType='Food'  "
				    + "and b.strItemCode=c.strItemCode "
				    + "and d.strItemCode=f.strItemCode "
				    + "and f.strCostCenterCode=g.strCostCenterCode  "
				    + "and f.strHourlyPricing='NO'  "
				    + filter
				    + "group by f.strCostCenterCode,b.strItemCode order by f.strCostCenterCode,a.dteBillDate ";

			    qFileSql = "select b.strItemCode,b.strItemName,sum(b.dblQuantity),b.dblRate,a.strPOSCode"
				    + ",date(a.dteBillDate),'" + gClientCode + "' ,c.strWSProductCode,e.strPosName, "
				    + " f.strCostCenterCode,g.strCostCenterName "
				    + ",sum(b.dblAmount),sum(b.dblDiscountPer),sum(b.dblDiscountAmt),d.strItemType "
				    + "from tblqbillhd a,tblqbilldtl b, tblitemmasterlinkupdtl c,tblitemmaster d,tblposmaster e,   "
				    + "tblmenuitempricingdtl f,tblcostcentermaster g "
				    + "where a.strBillNo=b.strBillNo "
				    + "and b.strItemCode=c.strItemCode "
				    + "and b.strItemCode=d.strItemCode "
				    + "and a.strPOSCode=e.strPOSCode "
				    + "and d.strItemType='Food'  "
				    + "and b.strItemCode=c.strItemCode "
				    + "and d.strItemCode=f.strItemCode "
				    + "and f.strCostCenterCode=g.strCostCenterCode  "
				    + "and f.strHourlyPricing='NO'  "
				    + filter
				    + "group by f.strCostCenterCode,b.strItemCode order by f.strCostCenterCode,a.dteBillDate ";
			}
			//System.out.println(sql);
			//System.out.println(qFileSql);

			ResultSet rsItemSales = dbMysql.executeResultSet(sql);
			while (rsItemSales.next())
			{
			    JSONObject dataObject = new JSONObject();
			    dataObject.put("posItemCode", rsItemSales.getString(1));
			    dataObject.put("posItemName", rsItemSales.getString(2));
			    dataObject.put("quantity", rsItemSales.getString(3));
			    dataObject.put("rate", rsItemSales.getString(4));
			    dataObject.put("posCode", rsItemSales.getString(5));
			    dataObject.put("billDate", rsItemSales.getString(6));
			    dataObject.put("clientCode", rsItemSales.getString(7));
			    dataObject.put("wsProdCode", rsItemSales.getString(8));
			    dataObject.put("posName", rsItemSales.getString(9));

			    if (itemType.equals("Both"))
			    {
				dataObject.put("Amount", rsItemSales.getString(10));
				dataObject.put("DiscPer", rsItemSales.getString(11));
				dataObject.put("DiscAmt", rsItemSales.getString(12));
				dataObject.put("ItemType", rsItemSales.getString(13));
			    }
			    else if (itemType.equals("Liquor"))
			    {
				dataObject.put("Amount", rsItemSales.getString(12));
				dataObject.put("DiscPer", rsItemSales.getString(13));
				dataObject.put("DiscAmt", rsItemSales.getString(14));
				dataObject.put("ItemType", rsItemSales.getString(15));
			    }
			    else
			    {
				dataObject.put("Amount", rsItemSales.getString(12));
				dataObject.put("DiscPer", rsItemSales.getString(13));
				dataObject.put("DiscAmt", rsItemSales.getString(14));
				dataObject.put("ItemType", rsItemSales.getString(15));
			    }

			    dataObjectArray.add(dataObject);
			}
			rsItemSales.close();

			rsItemSales = dbMysql.executeResultSet(qFileSql);
			while (rsItemSales.next())
			{
			    JSONObject dataObject = new JSONObject();
			    dataObject.put("posItemCode", rsItemSales.getString(1));
			    dataObject.put("posItemName", rsItemSales.getString(2));
			    dataObject.put("quantity", rsItemSales.getString(3));
			    dataObject.put("rate", rsItemSales.getString(4));
			    dataObject.put("posCode", rsItemSales.getString(5));
			    dataObject.put("billDate", rsItemSales.getString(6));
			    dataObject.put("clientCode", rsItemSales.getString(7));
			    dataObject.put("wsProdCode", rsItemSales.getString(8));
			    dataObject.put("posName", rsItemSales.getString(9));

			     if (itemType.equals("Both"))
			    {
				dataObject.put("Amount", rsItemSales.getString(10));
				dataObject.put("DiscPer", rsItemSales.getString(11));
				dataObject.put("DiscAmt", rsItemSales.getString(12));
				dataObject.put("ItemType", rsItemSales.getString(13));
			    }
			    else if (itemType.equals("Liquor"))
			    {
				dataObject.put("Amount", rsItemSales.getString(12));
				dataObject.put("DiscPer", rsItemSales.getString(13));
				dataObject.put("DiscAmt", rsItemSales.getString(14));
				dataObject.put("ItemType", rsItemSales.getString(15));
			    }
			    else
			    {
				dataObject.put("Amount", rsItemSales.getString(12));
				dataObject.put("DiscPer", rsItemSales.getString(13));
				dataObject.put("DiscAmt", rsItemSales.getString(14));
				dataObject.put("ItemType", rsItemSales.getString(15));
			    }

			    dataObjectArray.add(dataObject);
			}
			rsItemSales.close();

			if (dataObjectArray.size() > 0)
			{
			    JSONObject rootObject = new JSONObject();
			    rootObject.put("MemberPOSSalesInfo", dataObjectArray);
			    rootObject.put("WSLocation", WSLocationCode);
			    rootObject.put("costCenterCode", costCenterCode);
			    rootObject.put("costCenterName", costCenterName);

			    rootArr.add(rootObject);
			}

		    }
		    rsLoc.close();
		    if (rootArr.size() > 0)
		    {
			JSONObject rootMMS = new JSONObject();
			rootMMS.put("RootMMS", rootArr);
			rootMMS.put("DayEndWSStockAdjNo", posDayEndWSStockAdjNo);

			//localhost:8080/prjSanguineWebService/ExciseIntegration/funPostPOSSaleData
			String hoURL = gSanguineWebServiceURL + "/MMSIntegrationAuto/funPostPOSSaleDataAuto";

			URL url = new URL(hoURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			OutputStream os = conn.getOutputStream();
			os.write(rootMMS.toString().getBytes());
			os.flush();

			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED)
			{
			    throw new RuntimeException("Failed : HTTP error code : "
				    + conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

			String output = "", op = "Updated successfully:";
			while ((output = br.readLine()) != null)
			{
			    op += output;
			}

			//System.out.println(op);
			WSStockAdjustmentCode = op.split(":")[1];
			//System.out.println("wsStockAdjustmentCode:"+WSStockAdjustmentCode);
			conn.disconnect();
			//***********************update day end WebStocks Adjustment*******************************//

			if (WSStockAdjustmentCode != null && !WSStockAdjustmentCode.isEmpty() && !WSStockAdjustmentCode.equalsIgnoreCase("NA"))
			{
			    String queryUpdateDayEndWSAdjNo = "update tbldayendprocess "
				    + "set strWSStockAdjustmentNo='" + WSStockAdjustmentCode + "' "
				    + "where strPOSCode='" + posCode + "' "
				    + "and dtePOSDate='" + posDayEndDate + "' "
				    + "and intShiftCode='" + posShiftEndCode + "' ";
			    int i = dbMysql.execute(queryUpdateDayEndWSAdjNo);
			    WSStockAdjustmentCode = "";

			    flgResult = true;
			}
		    }
		}

	    }
	}
	catch (Exception e)
	{
	    flgResult = false;
	    WSStockAdjustmentCode = "";
	    e.printStackTrace();
	}
	finally
	{
	    return flgResult;
	}
    }

    //*****For Excise*******//  
    public String funPostPOSSalesDataToExciseAuto(String itemType, String posCode, final String dateFrom, final String dateTo)
    {
	boolean flgResult = false;
	String exciseBillNo = "";
	String sqlLiveLinkupFile = "", sqlQLinkupFile = "";
	final ArrayList<ArrayList<String>> arrUnLinkedItemDtl = new ArrayList<ArrayList<String>>();
	try
	{
	    String exciseLicencceCode = "";
	    final String posName = clsGlobalVarClass.gPOSName;

	    String sqlLocation = " Select strExciseLicenceCode from tblPOSMaster where strPOSCode='" + posCode + "' ; ";
	    ResultSet rsLoc = dbMysql.executeResultSet(sqlLocation);
	    while (rsLoc.next())
	    {
		exciseLicencceCode = rsLoc.getString(1);
	    }
	    rsLoc.close();

	    String filter1 = "";
	    if (posCode.equalsIgnoreCase("All"))
	    {
		filter1 = " and date(a.dteBillDate) between '" + dateFrom + "' and '" + dateTo + "' ";
	    }
	    else
	    {
		filter1 = " and a.strPOSCode='" + posCode + "' and date(a.dteBillDate) between '" + dateFrom + "' and '" + dateTo + "' ";
	    }

	    sqlLiveLinkupFile = " SELECT b.strItemCode,c.strItemName,d.strPosName,e.strWSProductCode,e.strWSProductName,e.strExciseBrandCode,e.strExciseBrandName "
		    + "FROM tblbillhd a,tblbilldtl b,tblitemmaster c,tblposmaster d,tblitemmasterlinkupdtl e "
		    + "WHERE a.strBillNo=b.strBillNo  "
		    + "AND b.strItemCode=c.strItemCode "
		    + "AND a.strPOSCode=d.strPosCode  "
		    + "and b.strItemCode=e.strItemCode "
		    + "and a.strPOSCode=e.strPOSCode "
		    + "and LENGTH(e.strExciseBrandCode)<3   "
		    + filter1;
	    if (!itemType.equals("Both"))
	    {
		sqlLiveLinkupFile += " and c.strItemType='Liquor' ";
	    }
	    sqlLiveLinkupFile += " group by b.strItemCode,d.strPosCode ";

	    sqlQLinkupFile = " SELECT b.strItemCode,c.strItemName,d.strPosName,e.strWSProductCode,e.strWSProductName,e.strExciseBrandCode,e.strExciseBrandName "
		    + "FROM tblqbillhd a,tblqbilldtl b,tblitemmaster c,tblposmaster d,tblitemmasterlinkupdtl e "
		    + "WHERE a.strBillNo=b.strBillNo  "
		    + "AND b.strItemCode=c.strItemCode "
		    + "AND a.strPOSCode=d.strPosCode  "
		    + "and b.strItemCode=e.strItemCode "
		    + "and a.strPOSCode=e.strPOSCode "
		    + "and LENGTH(e.strExciseBrandCode)<3   "
		    + filter1;
	    if (!itemType.equals("Both"))
	    {
		sqlQLinkupFile += " and c.strItemType='Liquor' ";
	    }
	    sqlQLinkupFile += " group by b.strItemCode,d.strPosCode ";

	    //System.out.println(sqlLiveLinkupFile);
	    //System.out.println(sqlQLinkupFile);
	    ResultSet rsLiveLinkupDtl = dbMysql.executeResultSet(sqlLiveLinkupFile);
	    while (rsLiveLinkupDtl.next())
	    {
		ArrayList<String> arrUnLinkedItem = new ArrayList<String>();
		arrUnLinkedItem.add(rsLiveLinkupDtl.getString(1));
		arrUnLinkedItem.add(rsLiveLinkupDtl.getString(2));
//                posName = rsLiveLinkupDtl.getString(3);
		arrUnLinkedItemDtl.add(arrUnLinkedItem);
	    }
	    rsLiveLinkupDtl.close();

	    ResultSet rsQLinkupDtl = dbMysql.executeResultSet(sqlQLinkupFile);
	    while (rsQLinkupDtl.next())
	    {
		ArrayList<String> arrUnLinkedItem = new ArrayList<String>();
		arrUnLinkedItem.add(rsQLinkupDtl.getString(1));
		arrUnLinkedItem.add(rsQLinkupDtl.getString(2));
//                posName = rsQLinkupDtl.getString(3);
		arrUnLinkedItemDtl.add(arrUnLinkedItem);
	    }
	    rsQLinkupDtl.close();

	    if (arrUnLinkedItemDtl.size() > 0)
	    {
		new Thread()
		{

		    @Override
		    public void run()
		    {
			JOptionPane.showMessageDialog(null, "some Items needs to be linkup with web stock product code","Excise",JOptionPane.WARNING_MESSAGE);
			new clsUtility().funGenerateLinkupTextfile(arrUnLinkedItemDtl, dateFrom, dateTo, posName);
		    }

		}.start();

	    }
	    else
	    {

		String queryDayEnd = "select a.strPOSCode,a.dtePOSDate,a.intShiftCode,ifnull(a.strExciseBillGeneration,'') strExciseBillGeneration,a.strDayEnd,a.strShiftEnd "
			+ "from tbldayendprocess a "
			+ "where date(a.dtePOSDate) between '" + dateFrom + "' and '" + dateTo + "' "
			+ "and a.strDayEnd='Y' "
			+ "and a.strShiftEnd='Y' "
			+ "and a.strPOSCode='" + posCode + "' ";

		ResultSet rsDayEndData = clsGlobalVarClass.dbMysql.executeResultSet(queryDayEnd);
		while (rsDayEndData.next())
		{
		    String dayEndPOSCode = rsDayEndData.getString(1);//posCode
		    String posDayEndDate = rsDayEndData.getString(2);//posDayEndDate
		    String posShiftEndCode = rsDayEndData.getString(3);//posShiftEndCode
		    String posDayEndExciseBillGen = rsDayEndData.getString(4);//posExciseBillGeneration
		    //*****************************************************************//

		    JSONObject rootObject = new JSONObject();
		    JSONArray dataObjectArray = new JSONArray();
		    String sql = "", qFileSql = "";
		    String filter = "";
		    if (dayEndPOSCode.equalsIgnoreCase("All"))
		    {
			filter = " and date(a.dteBillDate) between '" + posDayEndDate + "' and '" + posDayEndDate + "' ";
		    }
		    else
		    {
			filter = " and a.strPOSCode='" + posCode + "' "
				+ " and c.strPOSCode='" + posCode + "' "
				+ " and a.intShiftCode='" + posShiftEndCode + "' "
				+ " and date(a.dteBillDate) between '" + posDayEndDate + "' and '" + posDayEndDate + "' ";
		    }

		    sql = "select b.strItemCode,b.strItemName,sum(b.dblQuantity),b.dblRate,a.strPOSCode"
			    + ",date(a.dteBillDate),'" + gClientCode + "' ,c.strExciseBrandCode "
			    + "from tblbillhd a,tblbilldtl b, tblitemmasterlinkupdtl c,tblitemmaster d "
			    + "where a.strBillNo=b.strBillNo "
			    + "and b.strItemCode=c.strItemCode "
			    + "and b.strItemCode=d.strItemCode "
			    + "and a.strPOSCode=c.strPOSCode "
			    + "and d.strItemType='Liquor'  "
			    + "and b.strItemCode=c.strItemCode "
			    + filter
			    + "group by b.strItemCode order by a.dteBillDate ";

		    qFileSql = "select b.strItemCode,b.strItemName,sum(b.dblQuantity),b.dblRate,a.strPOSCode"
			    + ",date(a.dteBillDate),'" + gClientCode + "' ,c.strExciseBrandCode "
			    + "from tblqbillhd a,tblqbilldtl b, tblitemmasterlinkupdtl c,tblitemmaster d "
			    + "where a.strBillNo=b.strBillNo "
			    + "and b.strItemCode=c.strItemCode "
			    + "and b.strItemCode=d.strItemCode "
			    + "and a.strPOSCode=c.strPOSCode "
			    + "and d.strItemType='Liquor'  "
			    + "and b.strItemCode=c.strItemCode "
			    + filter
			    + "group by b.strItemCode order by a.dteBillDate ";

//                    //System.out.println(sql);
		    //System.out.println(qFileSql);
		    ResultSet rsItemSales = dbMysql.executeResultSet(sql);
		    while (rsItemSales.next())
		    {
			JSONObject dataObject = new JSONObject();
			dataObject.put("posItemCode", rsItemSales.getString(1));
			dataObject.put("posItemName", rsItemSales.getString(2));
			dataObject.put("quantity", rsItemSales.getString(3));
			dataObject.put("rate", rsItemSales.getString(4));
			dataObject.put("posCode", rsItemSales.getString(5));
			dataObject.put("billDate", rsItemSales.getString(6));
			dataObject.put("clientCode", rsItemSales.getString(7));
			dataObject.put("exciseBrandCode", rsItemSales.getString(8));

			dataObjectArray.add(dataObject);
		    }
		    rsItemSales.close();

		    rsItemSales = dbMysql.executeResultSet(qFileSql);
		    while (rsItemSales.next())
		    {
			JSONObject dataObject = new JSONObject();
			dataObject.put("posItemCode", rsItemSales.getString(1));
			dataObject.put("posItemName", rsItemSales.getString(2));
			dataObject.put("quantity", rsItemSales.getString(3));
			dataObject.put("rate", rsItemSales.getString(4));
			dataObject.put("posCode", rsItemSales.getString(5));
			dataObject.put("billDate", rsItemSales.getString(6));
			dataObject.put("clientCode", rsItemSales.getString(7));
			dataObject.put("exciseBrandCode", rsItemSales.getString(8));
			dataObjectArray.add(dataObject);
		    }
		    rsItemSales.close();
		    if (dataObjectArray.size() > 0)
		    {
			rootObject.put("MemberPOSSalesInfo", dataObjectArray);
			rootObject.put("exciseLicencceCode", exciseLicencceCode);
			rootObject.put("DayEndExciseBillGen", posDayEndExciseBillGen);

			//localhost:8080/prjSanguineWebService/ExciseIntegration/funPostPOSSaleData
			String hoURL = gSanguineWebServiceURL + "/ExciseIntegrationAuto/funPostExciseSaleDataAuto";

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
			    throw new RuntimeException("Failed : HTTP error code : "
				    + conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

			String output = "", op = "Updated successfully:";
			while ((output = br.readLine()) != null)
			{
			    op += output;
			}

			//System.out.println(op);
			exciseBillNo = op.split(":")[1];
			//System.out.println("wsStockAdjustmentCode:"+WSStockAdjustmentCode);
			conn.disconnect();
			//***********************update day end WebStocks Adjustment*******************************//
			String queryUpdateDayEndWSAdjNo = "update tbldayendprocess "
				+ "set strExciseBillGeneration='" + exciseBillNo + "' "
				+ "where strPOSCode='" + posCode + "' "
				+ "and dtePOSDate='" + posDayEndDate + "' "
				+ "and intShiftCode='" + posShiftEndCode + "' ";
			int i = dbMysql.execute(queryUpdateDayEndWSAdjNo);

			if (!(op.equals("NA")))
			{
			    flgResult = true;
			}

		    }

		}
	    }

	}
	catch (Exception e)
	{
	    flgResult = false;
	    exciseBillNo = "";
	    e.printStackTrace();
	}
	finally
	{
	    return exciseBillNo;
	}
    }

    private int funPostVoidBillDiscDtlData() throws Exception
    {

	String updateBills = "";
	String query = "select * from tblvoidbilldiscdtl where strDataPostFlag='N' ";
	ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	JSONObject objJson = new JSONObject();
	JSONArray arrObj = new JSONArray();

	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateBills += ",'" + rs.getString("strBillNo") + "'";

	    objRows.put("strBillNo", rs.getString("strBillNo"));
	    objRows.put("strPOSCode", rs.getString("strPOSCode"));
	    objRows.put("dblDiscAmt", rs.getString("dblDiscAmt"));
	    objRows.put("dblDiscPer", rs.getString("dblDiscPer"));
	    objRows.put("dblDiscOnAmt", rs.getString("dblDiscOnAmt"));
	    objRows.put("strDiscOnType", rs.getString("strDiscOnType"));
	    objRows.put("strDiscOnValue", rs.getString("strDiscOnValue"));
	    objRows.put("strDiscReasonCode", rs.getString("strDiscReasonCode"));
	    objRows.put("strDiscRemarks", rs.getString("strDiscRemarks"));
	    objRows.put("strUserCreated", rs.getString("strUserCreated"));
	    objRows.put("strUserEdited", rs.getString("strUserEdited"));
	    objRows.put("dteDateCreated", rs.getString("dteDateCreated"));
	    objRows.put("dteDateEdited", rs.getString("dteDateEdited"));
	    objRows.put("strClientCode", rs.getString("strClientCode"));
	    objRows.put("strDataPostFlag", rs.getString("strDataPostFlag"));
	    objRows.put("dteBillDate", rs.getString("dteBillDate"));
	    objRows.put("strTransType", rs.getString("strTransType"));

	    arrObj.add(objRows);
	}

	rs.close();
	objJson.put("VoidBillDiscDtl", arrObj);
	String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostTransactionDataToHOPOS";

	URL url = new URL(hoURL);
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setDoOutput(true);
	conn.setRequestMethod("POST");
	conn.setRequestProperty("Content-Type", "application/json");
	OutputStream os = conn.getOutputStream();
	os.write(objJson.toString().getBytes());
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
	System.out.println("Void Bill Disc Dtl= " + op);
	conn.disconnect();
	if (op.equals("true"))
	{
	    StringBuilder sbUpdate = new StringBuilder(updateBills);
	    if (updateBills.length() > 0)
	    {
		updateBills = sbUpdate.delete(0, 1).toString();
		//System.out.println("Billsss="+updateBills);
		dbMysql.execute("update tblvoidbilldiscdtl set strDataPostFlag='Y' where strBillNo in (" + updateBills + ")");
	    }
	}
	return 1;

    }

    private int funPostVoidBillTaxDtlData() throws Exception
    {

	String updateBills = "";
	String query = "select * from tblvoidbilltaxdtl where strDataPostFlag='N' ";
	ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	JSONObject objJson = new JSONObject();
	JSONArray arrObj = new JSONArray();

	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateBills += ",'" + rs.getString("strBillNo") + "'";

	    objRows.put("strBillNo", rs.getString("strBillNo"));
	    objRows.put("strTaxCode", rs.getString("strTaxCode"));
	    objRows.put("dblTaxableAmount", rs.getString("dblTaxableAmount"));
	    objRows.put("dblTaxAmount", rs.getString("dblTaxAmount"));
	    objRows.put("strClientCode", rs.getString("strClientCode"));
	    objRows.put("strDataPostFlag", rs.getString("strDataPostFlag"));
	    objRows.put("dteBillDate", rs.getString("dteBillDate"));
	    objRows.put("strTransType", rs.getString("strTransType"));

	    arrObj.add(objRows);
	}

	rs.close();
	objJson.put("VoidBillTaxDtl", arrObj);
	String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostTransactionDataToHOPOS";

	URL url = new URL(hoURL);
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setDoOutput(true);
	conn.setRequestMethod("POST");
	conn.setRequestProperty("Content-Type", "application/json");
	OutputStream os = conn.getOutputStream();
	os.write(objJson.toString().getBytes());
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
	System.out.println("Void Bill Tax Dtl= " + op);
	conn.disconnect();
	if (op.equals("true"))
	{
	    StringBuilder sbUpdate = new StringBuilder(updateBills);
	    if (updateBills.length() > 0)
	    {
		updateBills = sbUpdate.delete(0, 1).toString();
		//System.out.println("Billsss="+updateBills);
		dbMysql.execute("update tblvoidbilltaxdtl set strDataPostFlag='Y' where strBillNo in (" + updateBills + ")");
	    }
	}
	return 1;

    }

    public String funGenerateStockInCode()
    {
	long lastNo = 0;
	String sql = "", stockInCode = "";

	try
	{
	    //obj_List_clsStockInTemp.clear();  ///Ask
	    sql = "select strTransactionType,dblLastNo from tblinternal where strTransactionType='stockInNo'";
	    ResultSet rsStockinno = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsStockinno.next())
	    {
		lastNo = rsStockinno.getLong(2);
		lastNo = lastNo + 1;
		rsStockinno.close();
		stockInCode = "SI" + String.format("%07d", lastNo);
		if (stockInCode.trim().length() <= 7)
		{
		    new frmOkPopUp(null, "StockInCode Not Generated  Please Try Again", "Error", 1).setVisible(true);

		}
		System.out.println(stockInCode);
		sql = "update tblinternal set dblLastNo='" + lastNo + "' where strTransactionType='stockinNo'";
		clsGlobalVarClass.dbMysql.execute(sql);
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return stockInCode;
    }

    public String funPostPOSItemSalesDataInPOS(String itemType, String posCode, final String dateFrom, final String dateTo, String dayEndType)
    {
	String stkOutCodes = "";
	try
	{
	    String dayEndPOSCode = posCode;

	    // sql get Day end stk cosdes
	    List<String> listWSCodeInDayEnd = new ArrayList<String>();
	    boolean flgOldDayEnd = false;
	    StringBuilder sbSql = new StringBuilder();

	    if (dayEndType.equalsIgnoreCase("QFile"))
	    {
		sbSql.append("select a.strPOSCode,a.dtePOSDate,a.intShiftCode,ifnull(a.strWSStockAdjustmentNo,'') strWSStockAdjustmentNo,a.strDayEnd,a.strShiftEnd "
			+ "from tbldayendprocess a "
			+ "where date(a.dtePOSDate) between '" + dateFrom + "' and '" + dateTo + "' "
			+ "and a.strDayEnd='Y' "
			+ "and a.strShiftEnd='Y' "
			+ "and a.strPOSCode='" + posCode + "' ");

		ResultSet rsSelectDayEndData = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
		while (rsSelectDayEndData.next())
		{
		    flgOldDayEnd = true;
		    dayEndPOSCode = rsSelectDayEndData.getString(1);//posCode
		    String posDayEndDate = rsSelectDayEndData.getString(2);//posDayEndDate
		    String posShiftEndCode = rsSelectDayEndData.getString(3);//posShiftEndCode
		    String posDayEndWSStockAdjNo = rsSelectDayEndData.getString(4);//posWSStockAdjNo

		    if (!posDayEndWSStockAdjNo.equalsIgnoreCase(""))
		    {
			String[] arrStkCodes = posDayEndWSStockAdjNo.split(",");
			for (String stkcode : arrStkCodes)
			{
			    String sqlHdDelete = "delete from tblstkouthd where strStkOutCode='" + stkcode + "'";
			    dbMysql.execute(sqlHdDelete);

			    String sqlDtlDelete = "delete from tblstkoutdtl  where strStkOutCode='" + stkcode + "'";
			    dbMysql.execute(sqlDtlDelete);
			}
		    }

		    funPostData(itemType, "tblqbillhd", "tblqbilldtl", posDayEndDate, dayEndPOSCode, Integer.parseInt(posShiftEndCode));
		}
	    }
	    else // Fresh Day End    
	    {
		if (!flgOldDayEnd)
		{
		    funPostData(itemType, "tblbillhd", "tblbilldtl", dateFrom, posCode, clsGlobalVarClass.gShiftNo);
		}
	    }
	}
	catch (Exception e)
	{
	    stkOutCodes = "";
	    e.printStackTrace();
	}
	finally
	{
	    return stkOutCodes;
	}
    }

    private int funPostData(String itemType, String billHdTableName, String billDtlTableName, String POSDate, String POSCode, int shiftCode) throws Exception
    {
	StringBuilder sbSql = new StringBuilder();
	StringBuilder sqlRecipeData = new StringBuilder();
	StringBuffer sqlStockOutHdBuilder = new StringBuffer();
	StringBuffer sqlStockOutDtlBuilder = new StringBuffer();

	String itemTypeFilter = "";
	if (itemType.equalsIgnoreCase("Food"))
	{
	    itemTypeFilter = " and e.strItemType='Food'";
	}
	else if (itemType.equalsIgnoreCase("Food"))
	{
	    itemTypeFilter = " and e.strItemType='Liquor'";
	}

	sqlRecipeData.append("select a.strPOSCode,b.strItemCode,b.strItemName,sum(b.dblQuantity),e.dblPurchaseRate,sum(b.dblAmount)"
		+ ",date(a.dteBillDate),b.dblRate "
		+ " from " + billHdTableName + " a," + billDtlTableName + " b,tblitemmaster e"
		+ " where a.strBillNo=b.strBillNo and date(a.dteBillDate)=date(b.dteBillDate) "
		+ " and b.strItemCode=e.strItemCode "
		+ " and date(a.dteBillDate) = '" + POSDate + "' and a.strPOSCode='" + POSCode + "' "
		+ " " + itemTypeFilter
		+ " group by a.strPOSCode,b.strItemCode,b.strItemName,e.dblPurchaseRate");

	List<clsPostPOSItemSalesDataInPOS> listItemDataToPost = new ArrayList<clsPostPOSItemSalesDataInPOS>();
	List<clsTaxCalculationDtls> arrListTaxCal;
	double qty = 0.0, rate = 0.0;
	List<clsItemDtlForTax> arrListItemDtls = new ArrayList<clsItemDtlForTax>();
	ResultSet rsItemSales = dbMysql.executeResultSet(sqlRecipeData.toString());
	while (rsItemSales.next())
	{
	    qty = rsItemSales.getDouble(4);
	    rate = rsItemSales.getDouble(8);
	    StringBuilder sqlChild = new StringBuilder();
	    sqlChild.append("select b.strChildItemCode,b.dblQuantity,c.dblPurchaseRate "
		    + ",c.strRecipeUOM,c.strUOM,c.dblRecipeConversion,c.dblReceivedConversion"
		    + " from tblrecipehd a,tblrecipedtl b,tblitemmaster c "
		    + " where a.strRecipeCode = b.strRecipeCode and b.strChildItemCode= c.strItemCode "
		    + " and a.strClientCode=b.strClientCode and a.strItemCode='" + rsItemSales.getString(2) + "' ");
	    ResultSet rsChildItemData = clsGlobalVarClass.dbMysql.executeResultSet(sqlChild.toString());
	    while (rsChildItemData.next())
	    {
		double recipeConversion = rsChildItemData.getDouble(6);
		double quantity = (rsItemSales.getDouble(4) * rsChildItemData.getDouble(2) / recipeConversion);
		double amt = quantity * rsChildItemData.getDouble(3);
		String recipeUOM = rsChildItemData.getString(4);
		String recvUOM = rsChildItemData.getString(5);
		double dblRecipeConversion = rsChildItemData.getDouble(6);
		double dblReceivedConversion = rsChildItemData.getDouble(7);

		Double qtyChild = quantity;
		String tempDisQty[] = qtyChild.toString().split("\\.");
		String displayqty = tempDisQty[0] + " " + recvUOM + " " + Math.round(Float.parseFloat("0." + tempDisQty[1]) * (dblRecipeConversion)) + " " + recipeUOM;

		clsPostPOSItemSalesDataInPOS objBean = new clsPostPOSItemSalesDataInPOS();
		objBean.setStrPosItemCode(rsChildItemData.getString(1));
		objBean.setDblQuantity(quantity);
		objBean.setDblRate(rsChildItemData.getDouble(3));
		objBean.setDblAmount(amt);
		objBean.setStrPosCode(rsItemSales.getString(1));
		objBean.setDteBillDate(rsItemSales.getString(7));
		objBean.setStrClientCode(clsGlobalVarClass.gClientCode);
		objBean.setStrParentCode(rsItemSales.getString(2));
		objBean.setDblParentCodeQty(qty);
		objBean.setDblParentCodeRate(rate);
		objBean.setStrDisplayQty(displayqty);
		listItemDataToPost.add(objBean);
	    }
	    rsChildItemData.close();
	}
	rsItemSales.close();

	String stkOutCodes = "";
	if (listItemDataToPost.size() > 0)
	{
	    String stkOutCode = funGenerateStockOutCode();
	    stkOutCodes = stkOutCodes + stkOutCode + ",";
	    int j = 0;
	    double subTotal = 0.0, taxAmt = 0.0, grandTotal = 0.0;

	    sqlStockOutDtlBuilder.setLength(0);
	    sqlStockOutDtlBuilder.append("insert into tblstkoutdtl"
		    + "(strStkOutCode,strItemCode,dblQuantity,dblPurchaseRate,dblAmount,strClientCode, "
		    + " strDataPostFlag,strRemark,strDisplayQty,strParentCode,dblParentItemQty,dblParentItemRate)"
		    + "");

	    for (clsPostPOSItemSalesDataInPOS objStockOutDtl : listItemDataToPost)
	    {
		if (objStockOutDtl.getStrParentCode().equals("I000573"))
		{
		    System.out.println(objStockOutDtl.getStrParentCode());
		}
		String remark = "ParentCode:" + objStockOutDtl.getStrParentCode() + " Qty:" + objStockOutDtl.getDblParentCodeQty() + " Rate:" + objStockOutDtl.getDblParentCodeRate();
		subTotal += objStockOutDtl.getDblAmount();
		clsItemDtlForTax objItemDtl = new clsItemDtlForTax();
		objItemDtl.setItemCode(objStockOutDtl.getStrPosItemCode());
		objItemDtl.setItemName(objStockOutDtl.getStrPosItemName());
		objItemDtl.setAmount(objStockOutDtl.getDblAmount());
		objItemDtl.setDiscAmt(0);

		arrListItemDtls.add(objItemDtl);
		if (j == 0)
		{
		    sqlStockOutDtlBuilder.append("values('" + stkOutCode + "','" + objStockOutDtl.getStrPosItemCode() + "','" + objStockOutDtl.getDblQuantity() + "','" + objStockOutDtl.getDblRate() + "','" + objStockOutDtl.getDblAmount() + "','" + objStockOutDtl.getStrClientCode() + "',"
			    + " 'N','" + remark + "','" + objStockOutDtl.getStrDisplayQty() + "','" + objStockOutDtl.getStrParentCode() + "','" + objStockOutDtl.getDblParentCodeQty() + "','" + objStockOutDtl.getDblParentCodeRate() + "')");
		}
		else
		{
		    sqlStockOutDtlBuilder.append(",('" + stkOutCode + "','" + objStockOutDtl.getStrPosItemCode() + "','" + objStockOutDtl.getDblQuantity() + "','" + objStockOutDtl.getDblRate() + "','" + objStockOutDtl.getDblAmount() + "','" + objStockOutDtl.getStrClientCode() + "',"
			    + " 'N','" + remark + "','" + objStockOutDtl.getStrDisplayQty() + "','" + objStockOutDtl.getStrParentCode() + "','" + objStockOutDtl.getDblParentCodeQty() + "','" + objStockOutDtl.getDblParentCodeRate() + "')");
		}

		j++;
	    }

	    if (sqlStockOutDtlBuilder.toString().contains("values"))
	    {
		int intDtl = dbMysql.execute(sqlStockOutDtlBuilder.toString());
	    }

	    if (arrListItemDtls.size() > 0)
	    {
		arrListTaxCal = objUtility.funCalculateTax(arrListItemDtls, clsGlobalVarClass.gPOSCode, clsGlobalVarClass.gPOSOnlyDateForTransaction, "", "", subTotal, 0.00, "", "", "Purchase");
		for (clsTaxCalculationDtls objTaxDtl : arrListTaxCal)
		{
		    if (objTaxDtl.getTaxCalculationType().equalsIgnoreCase("Forward"))
		    {
			taxAmt = taxAmt + objTaxDtl.getTaxAmount();
			Object[] taxTotalRow =
			{
			    objTaxDtl.getTaxName(), "", Math.rint(objTaxDtl.getTaxAmount())
			};
		    }
		}
	    }
	    grandTotal = subTotal + taxAmt;

	    sqlStockOutHdBuilder.setLength(0);
	    sqlStockOutHdBuilder.append("insert into tblstkouthd"
		    + "(strStkOutCode,strPOSCode,dteStkOutDate,strReasonCode,strPurchaseBillNo,dtePurchaseBillDate, "
		    + " intShiftCode,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strClientCode,"
		    + "strDataPostFlag,strNarration,dblTaxAmt,dblExtraAmt,dblGrandTotal,strSupplierCode)"
		    + "");

	    sqlStockOutHdBuilder.append("values('" + stkOutCode + "','" + POSCode + "','" + POSDate + "','R001','','" + POSDate + "',"
		    + "'" + clsGlobalVarClass.gShiftNo + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "',"
		    + "'N','','" + taxAmt + "','0.00','" + grandTotal + "','')");

	    if (sqlStockOutHdBuilder.toString().contains("values"))
	    {
		int inthd = dbMysql.execute(sqlStockOutHdBuilder.toString());
	    }
	}

	if (stkOutCodes.length() > 0)
	{
	    stkOutCodes = stkOutCodes.substring(0, stkOutCodes.length() - 1);
	}
	sbSql.append("update tbldayendprocess "
		+ "set strWSStockAdjustmentNo='" + stkOutCodes + "' "
		+ "where strPOSCode='" + POSCode + "' "
		+ "and dtePOSDate='" + POSDate + "' "
		+ "and intShiftCode='" + shiftCode + "' ");
	int i = dbMysql.execute(sbSql.toString());

	return 1;
    }

    //Function to generate stockOut code
    public String funGenerateStockOutCode()
    {
	long lastNo1 = 0;
	String sql = "", stockOutCode = "", sql1;
	try
	{
	    sql1 = "select strTransactionType,dblLastNo from tblinternal where strTransactionType='stockOutNo'";
	    ResultSet rsStockOutno = clsGlobalVarClass.dbMysql.executeResultSet(sql1);
	    if (rsStockOutno.next())
	    {
		lastNo1 = rsStockOutno.getLong(2);
		lastNo1 = lastNo1 + 1;
		stockOutCode = "SO" + String.format("%07d", lastNo1);
		sql = "update tblinternal set dblLastNo='" + lastNo1 + "' where strTransactionType='stockOutNo'";
		clsGlobalVarClass.dbMysql.execute(sql);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return stockOutCode;
    }

    private int funInsertDiscMasterHdData(JSONArray mJsonArray)
    {
	int retVal = 0;
	try
	{
	    StringBuilder sb = null;
	    String insertValues = "";
	    String query = "insert into tbldischd values";
	    JSONObject mJsonObject = new JSONObject();
	    String deleteSql = "";

	    for (int i = 0; i < mJsonArray.size(); i++)
	    {
		mJsonObject = (JSONObject) mJsonArray.get(i);
		deleteSql = "delete from tbldischd where strDiscCode='" + mJsonObject.get("Column1") + "' ";
		dbMysql.execute(deleteSql);
		insertValues += "('" + mJsonObject.get("Column1") + "','" + mJsonObject.get("Column2") + "'"
			+ ",'" + mJsonObject.get("Column3") + "','" + gClientCode + "'"
			+ ",'" + mJsonObject.get("Column5") + "','" + mJsonObject.get("Column6") + "' "
			+ ",'" + mJsonObject.get("Column7") + "','" + mJsonObject.get("Column8") + "'"
			+ ",'" + mJsonObject.get("Column9") + "','" + mJsonObject.get("Column10") + "'"
			+ ",'" + mJsonObject.get("Column11") + "','" + mJsonObject.get("Column12") + "' ),";
		//System.out.println(insertValues);
	    }
	    if (mJsonArray.size() > 0)
	    {
		sb = new StringBuilder(insertValues);
		insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
		query = query + insertValues;
		sb = new StringBuilder(query);
		//query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
		retVal = dbMysql.execute(query);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return retVal;
    }

    private int funInsertDiscMasterDtlData(JSONArray mJsonArray)
    {
	int retVal = 0;
	try
	{
	    StringBuilder sb = null;
	    String insertValues = "";
	    String query = "insert into tbldiscdtl values";
	    JSONObject mJsonObject = new JSONObject();
	    String deleteSql = "";

	    for (int i = 0; i < mJsonArray.size(); i++)
	    {
		mJsonObject = (JSONObject) mJsonArray.get(i);
		deleteSql = "delete from tbldiscdtl where strDiscCode='" + mJsonObject.get("Column1") + "' ";
		dbMysql.execute(deleteSql);
		insertValues += "('" + mJsonObject.get("Column1") + "','" + mJsonObject.get("Column2") + "'"
			+ ",'" + mJsonObject.get("Column3") + "','" + mJsonObject.get("Column4") + "'"
			+ ",'" + mJsonObject.get("Column5") + "','" + gClientCode + "' "
			+ ",'" + mJsonObject.get("Column7") + "','" + mJsonObject.get("Column8") + "'"
			+ ",'" + mJsonObject.get("Column9") + "','" + mJsonObject.get("Column10") + "'"
			+ ",'" + mJsonObject.get("Column11") + "' ),";
		//System.out.println(insertValues);
	    }
	    if (mJsonArray.size() > 0)
	    {
		sb = new StringBuilder(insertValues);
		insertValues = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
		query = query + insertValues;
		sb = new StringBuilder(query);
		//query = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
		retVal = dbMysql.execute(query);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return retVal;
    }

    public int funPostCreditBillReceiptDtlData(String formName) throws Exception
    {
	String updateBills = "";
	String query = "select * from tblqcreditbillreceipthd where strDataPostFlag='N'";
	ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(query);
	JSONObject objJson = new JSONObject();
	JSONArray arrObj = new JSONArray();

	boolean flgDataForPosting = false;
	while (rs.next())
	{
	    JSONObject objRows = new JSONObject();
	    updateBills += ",'" + rs.getString("strReceiptNo") + "'";

	    objRows.put("strReceiptNo", rs.getString("strReceiptNo"));
	    objRows.put("strBillNo", rs.getString("strBillNo"));
	    objRows.put("strPOSCode", rs.getString("strPOSCode"));
	    objRows.put("dteReceiptDate", rs.getString("dteReceiptDate"));
	    objRows.put("dblReceiptAmt", rs.getString("dblReceiptAmt"));
	    objRows.put("intShiftCode", rs.getString("intShiftCode"));
	    objRows.put("strUserCreated", rs.getString("strUserCreated"));
	    objRows.put("strUserEdited", rs.getString("strUserEdited"));
	    objRows.put("dteDateCreated", rs.getString("dteDateCreated"));
	    objRows.put("dteDateEdited", rs.getString("dteDateEdited"));
	    objRows.put("strClientCode", rs.getString("strClientCode"));
	    objRows.put("strDataPostFlag", rs.getString("strDataPostFlag"));
	    objRows.put("dteBillDate", rs.getString("dteBillDate"));
	    objRows.put("strSettlementCode", rs.getString("strSettlementCode"));
	    objRows.put("strSettlementName", rs.getString("strSettlementName"));
	    objRows.put("strChequeNo", rs.getString("strChequeNo"));
	    objRows.put("strBankName", rs.getString("strBankName"));
	    objRows.put("dteChequeDate", rs.getString("dteChequeDate"));
	    objRows.put("strRemarks", rs.getString("strRemarks"));

	    arrObj.add(objRows);
	    flgDataForPosting = true;
	}
	rs.close();

	if (flgDataForPosting)
	{
	    objJson.put("Credit Bill Receipts", arrObj);
	    String hoURL = gSanguineWebServiceURL + "/POSIntegration/funPostTransactionDataToHOPOS";

	    URL url = new URL(hoURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    OutputStream os = conn.getOutputStream();
	    os.write(objJson.toString().getBytes());
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
	    System.out.println("Adv Rec Dtl= " + op);
	    conn.disconnect();

	    if (op.equals("true"))
	    {
		StringBuilder sbUpdate = new StringBuilder(updateBills);
		if (updateBills.length() > 0)
		{
		    updateBills = sbUpdate.delete(0, 1).toString();
		    //System.out.println("Billsss="+updateBills);
		    dbMysql.execute("update tblqcreditbillreceipthd set strDataPostFlag='Y' where strReceiptNo in (" + updateBills + ")");
		}
		if (formName.equals("ManuallyLive") || formName.equals("ManuallyQFile"))
		{
		    JOptionPane.showMessageDialog(null, "Data Posted Successfully!!!");
		}
	    }
	}

	return 1;
    }

}
