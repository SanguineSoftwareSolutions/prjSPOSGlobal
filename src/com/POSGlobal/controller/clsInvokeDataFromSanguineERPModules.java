/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSGlobal.controller;

import static com.POSGlobal.controller.clsGlobalVarClass.dbMysql;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class clsInvokeDataFromSanguineERPModules
{

    public void funPOSTRoomSettlementDtlToPMS(String voucherNo, double _grandTotal, HashMap<String, clsSettelementOptions> hmSettlemetnOptions)
    {
	try
	{
	    String updateBills = "";
	    JSONObject jObj = new JSONObject();

	    JSONArray arrObj = new JSONArray();
	    /*
             * Iterator<Map.Entry<String, clsSettelementOptions>> it =
             * hmSettlemetnOptions.entrySet().iterator(); if (it.hasNext()) {
             * clsSettelementOptions objSettlementOpt = it.next().getValue();
             * objFolioDtl.put("BillNo", voucherNo); objFolioDtl.put("POSCode",
             * ); objFolioDtl.put("BillDate", billDate);
             * objFolioDtl.put("FolioNo", objSettlementOpt.getStrFolioNo());
             * objFolioDtl.put("GuestCode", objSettlementOpt.getStrGuestCode());
             * objFolioDtl.put("RoomNo", objSettlementOpt.getStrRoomNo());
             * objFolioDtl.put("SettledAmt", _grandTotal);
             * objFolioDtl.put("ClientCode", clsGlobalVarClass.gWSClientCode);
             *
             * arrObj.add(objFolioDtl);
            }
	     */

	    String sql = "select * from tblpmspostingbilldtl where strPMSDataPostFlag='N'";
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rs.next())
	    {
		updateBills += ",'" + rs.getString(1) + "'";
		JSONObject objFolioDtl = new JSONObject();
		objFolioDtl.put("BillNo", rs.getString(1));
		objFolioDtl.put("POSCode", rs.getString(2));
		objFolioDtl.put("BillDate", rs.getString(3));

		String bSettleAmt=rs.getString(4);
		if (clsGlobalVarClass.gPOSToWebBooksPostingCurrency.equalsIgnoreCase("USD"))
		{
		    double settleAmt=rs.getDouble(4)/clsGlobalVarClass.gUSDConvertionRate;
		    bSettleAmt=String.valueOf(settleAmt);		    
		}
		objFolioDtl.put("SettledAmt",bSettleAmt);
		objFolioDtl.put("FolioNo", rs.getString(5));
		objFolioDtl.put("GuestCode", rs.getString(6));
		objFolioDtl.put("RoomNo", rs.getString(7));
		objFolioDtl.put("ClientCode", clsGlobalVarClass.gWSClientCode);
		objFolioDtl.put("BillType", rs.getString(11));
		arrObj.add(objFolioDtl);
	    }
	    rs.close();

	    jObj.put("FolioDtl", arrObj);
	    //System.out.println(jObj);

	    String cmsURL = clsGlobalVarClass.gSanguineWebServiceURL + "/PMSIntegration/funPOSTSettlementDtlToPMS";
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
	    while ((output = br.readLine()) != null)
	    {
		op += output;
	    }
	    conn.disconnect();

	    if (op.equals("true"))
	    {
		StringBuilder sbUpdate = new StringBuilder(updateBills);
		if (updateBills.length() > 0)
		{
		    updateBills = sbUpdate.delete(0, 1).toString();
		    //System.out.println("Billsss="+updateBills);
		    dbMysql.execute("update tblpmspostingbilldtl set strPMSDataPostFlag='Y' where strBillNo in (" + updateBills + ")");
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    JOptionPane.showMessageDialog(null, "POST POS Settlement Detail To PMS Error"); // there is this at null postion
	}
    }

    public List<clsGuestRoomDtl> funGetGuestRoomDtl()
    {
	List<clsGuestRoomDtl> listOfGuestRoomDtl = new ArrayList<>();
	try
	{
	    String cmsURL = clsGlobalVarClass.gSanguineWebServiceURL + "/PMSIntegration/funGetFolioDetails?ClientCode=" + clsGlobalVarClass.gWSClientCode;// + clsGlobalVarClass.gClientCode;
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
	    JSONArray mJsonArray = (JSONArray) jObj.get("FolioDtls");
	    JSONObject mJsonObject = new JSONObject();
	    for (int i = 0; i < mJsonArray.size(); i++)
	    {
		clsGuestRoomDtl objGuestRoomDtl = new clsGuestRoomDtl();
		mJsonObject = (JSONObject) mJsonArray.get(i);
		objGuestRoomDtl.setStrFolioNo(mJsonObject.get("FolioNo").toString());
		objGuestRoomDtl.setStrRoomNo(mJsonObject.get("RoomNo").toString());
		objGuestRoomDtl.setStrRoomDesc(mJsonObject.get("RoomDesc").toString());
		objGuestRoomDtl.setStrGuestName(mJsonObject.get("GuestName").toString());
		objGuestRoomDtl.setStrGuestCode(mJsonObject.get("GuestCode").toString());
		listOfGuestRoomDtl.add(objGuestRoomDtl);
	    }
	    conn.disconnect();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    return listOfGuestRoomDtl;
	}
    }

    public List<clsAccountDtl> funGetAccountDtl(String type, String clientCode) throws Exception
    {
	List<clsAccountDtl> accountInfo = new ArrayList<clsAccountDtl>();

	type = type.replaceAll(" ", "%20");
	String cmsURL = clsGlobalVarClass.gSanguineWebServiceURL + "/WebBooksIntegration/funGetAccountMaster?Type=" + type + "&ClientCode=" + clientCode;
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
	JSONArray mJsonArray = (JSONArray) jObj.get("AccountDtl");

	JSONObject mJsonObject = new JSONObject();
	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    if (mJsonObject.get("AccountCode").toString().equals(""))
	    {
		//accountInfo = "no data";
	    }
	    else
	    {
		clsAccountDtl objAccount = new clsAccountDtl();
		objAccount.setStrAccountCode(mJsonObject.get("AccountCode").toString());
		objAccount.setStrAccountName(mJsonObject.get("AccountName").toString());
		accountInfo.add(objAccount);
	    }
	}
	conn.disconnect();
	return accountInfo;
    }

    public List<clsLinkupDtl> funGetPropertyCodeDtl(String clientCode) throws Exception
    {
	List<clsLinkupDtl> propertyInfo = new ArrayList<clsLinkupDtl>();

	String cmsURL = clsGlobalVarClass.gSanguineWebServiceURL + "/MMSIntegration/funGetPropertyMaster?ClientCode=" + clientCode;
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
	JSONArray mJsonArray = (JSONArray) jObj.get("PropertyDtls");

	JSONObject mJsonObject = new JSONObject();
	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    if (mJsonObject.get("PropertyCode").toString().equals(""))
	    {
		//accountInfo = "no data";
	    }
	    else
	    {
		clsLinkupDtl objLinkup = new clsLinkupDtl();
		objLinkup.setStrLinkupCode(mJsonObject.get("PropertyCode").toString());
		objLinkup.setStrLinkupName(mJsonObject.get("PropertyName").toString());
		propertyInfo.add(objLinkup);
	    }
	}
	conn.disconnect();
	return propertyInfo;
    }

    public List<clsLinkupDtl> funGetProductCharDtl(String clientCode) throws Exception
    {
	List<clsLinkupDtl> productCharInfo = new ArrayList<clsLinkupDtl>();
	String cmsURL = clsGlobalVarClass.gSanguineWebServiceURL + "/MMSIntegration/funGetCharacteristicsMaster?ClientCode=" + clientCode;
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
	JSONArray mJsonArray = (JSONArray) jObj.get("ProductCharDtls");
	JSONObject mJsonObject = new JSONObject();
	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    if (mJsonObject.get("ProdcutCharCode").toString().equals(""))
	    {
		//accountInfo = "no data";
	    }
	    else
	    {
		clsLinkupDtl objLinkup = new clsLinkupDtl();
		objLinkup.setStrLinkupCode(mJsonObject.get("ProdcutCharCode").toString());
		objLinkup.setStrLinkupName(mJsonObject.get("ProdcutCharName").toString());
		productCharInfo.add(objLinkup);
	    }
	}
	conn.disconnect();
	return productCharInfo;
    }

    public List<clsLinkupDtl> funGetProductDtl(String clientCode) throws Exception
    {
	List<clsLinkupDtl> productInfo = new ArrayList<clsLinkupDtl>();

	String cmsURL = clsGlobalVarClass.gSanguineWebServiceURL + "/MMSIntegration/funGetProductMaster?ClientCode=" + clientCode;
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
	JSONArray mJsonArray = (JSONArray) jObj.get("ProductDtls");

	JSONObject mJsonObject = new JSONObject();
	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    if (mJsonObject.get("ProdcutCode").toString().equals(""))
	    {
		//accountInfo = "no data";
	    }
	    else
	    {
		clsLinkupDtl objLinkup = new clsLinkupDtl();
		objLinkup.setStrLinkupCode(mJsonObject.get("ProdcutCode").toString());
		objLinkup.setStrLinkupName(mJsonObject.get("ProdcutName").toString());
		productInfo.add(objLinkup);
	    }
	}
	conn.disconnect();
	return productInfo;
    }

    public List<clsLinkupDtl> funGetLocationDtls(String clientCode) throws Exception
    {
	List<clsLinkupDtl> locationInfo = new ArrayList<clsLinkupDtl>();

	String cmsURL = clsGlobalVarClass.gSanguineWebServiceURL + "/MMSIntegration/funGetLocationMaster?ClientCode=" + clientCode;
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
	JSONArray mJsonArray = (JSONArray) jObj.get("LocationDtls");

	JSONObject mJsonObject = new JSONObject();
	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    if (mJsonObject.get("LocationCode").toString().equals(""))
	    {
		//accountInfo = "no data";
	    }
	    else
	    {
		clsLinkupDtl objLocation = new clsLinkupDtl();
		objLocation.setStrLinkupCode(mJsonObject.get("LocationCode").toString());
		objLocation.setStrLinkupName(mJsonObject.get("LocationName").toString());
		locationInfo.add(objLocation);
	    }
	}
	conn.disconnect();
	return locationInfo;
    }

    public List<clsLinkupDtl> funGetBrandDtls(String clientCode) throws Exception
    {
	List<clsLinkupDtl> brandInfo = new ArrayList<clsLinkupDtl>();

	String cmsURL = clsGlobalVarClass.gSanguineWebServiceURL + "/ExciseIntegration/funGetBrandMaster?ClientCode=" + clientCode;
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
	JSONArray mJsonArray = (JSONArray) jObj.get("BrandDtls");

	JSONObject mJsonObject = new JSONObject();
	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    if (mJsonObject.get("BrandCode").toString().equals(""))
	    {
		//accountInfo = "no data";
	    }
	    else
	    {
		clsLinkupDtl objBrand = new clsLinkupDtl();
		objBrand.setStrLinkupCode(mJsonObject.get("BrandCode").toString());
		objBrand.setStrLinkupName(mJsonObject.get("BrandName").toString());
		brandInfo.add(objBrand);
	    }
	}
	conn.disconnect();
	return brandInfo;
    }

    public List<clsLinkupDtl> funGetLicenceDtls(String clientCode) throws Exception
    {
	List<clsLinkupDtl> licenceInfo = new ArrayList<clsLinkupDtl>();

	String cmsURL = clsGlobalVarClass.gSanguineWebServiceURL + "/ExciseIntegration/funGetLicenceMaster?ClientCode=" + clientCode;
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
	JSONArray mJsonArray = (JSONArray) jObj.get("LicenceDtls");

	JSONObject mJsonObject = new JSONObject();
	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    if (mJsonObject.get("LicenceCode").toString().equals(""))
	    {
		//accountInfo = "no data";
	    }
	    else
	    {
		clsLinkupDtl objLicence = new clsLinkupDtl();
		objLicence.setStrLinkupCode(mJsonObject.get("LicenceCode").toString());
		objLicence.setStrLinkupName(mJsonObject.get("LicenceName").toString());
		licenceInfo.add(objLicence);
	    }
	}
	conn.disconnect();
	return licenceInfo;
    }

    public String funCreateProductInMMS(String itemCode, String itemName, String clientCode, String createDate, String userCode,String wsProductCode,String wsSGCode) throws Exception
    {
	String productInfo = "";

	JSONObject rootObject = new JSONObject();
	rootObject.put("itemCode", itemCode);
	rootObject.put("itemName", itemName);
	rootObject.put("clientCode", clientCode);
	rootObject.put("createDate", createDate);
	rootObject.put("userCode", userCode);
	rootObject.put("wsProductCode", wsProductCode);
	rootObject.put("wsSGCode", wsSGCode);
	
	
	String hoURL = clsGlobalVarClass.gSanguineWebServiceURL + "/MMSIntegrationAuto/funCreateProductInMMS";

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
	productInfo = op.split(":")[1];
	//System.out.println("wsStockAdjustmentCode:"+WSStockAdjustmentCode);
	conn.disconnect();

	return productInfo;
    }


    public String funMMSConsectionEstablished() throws Exception
    {
	String strMMSConsectionEstablished = "";

	String hoURL = clsGlobalVarClass.gSanguineWebServiceURL + "/MMSIntegrationAuto/funInvokeMMSWebService";

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
	//System.out.println(op);
	strMMSConsectionEstablished = op;
	//System.out.println("wsStockAdjustmentCode:"+WSStockAdjustmentCode);
	conn.disconnect();

	return strMMSConsectionEstablished;

    }
    
    public List<clsDebtorDtl> funGetDebtorDtl(String gClientCode) throws Exception
    {
	List<clsDebtorDtl> debtorInfo = new ArrayList<clsDebtorDtl>();

	//type = type.replaceAll(" ", "%20");
	String cmsURL = clsGlobalVarClass.gSanguineWebServiceURL + "/WebBooksIntegration/funGetDebtorMaster?ClientCode=" + gClientCode;
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
	JSONArray mJsonArray = (JSONArray) jObj.get("DebtorDtl");

	JSONObject mJsonObject = new JSONObject();
	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    if (mJsonObject.get("DebtorCode").toString().equals(""))
	    {
		//accountInfo = "no data";
	    }
	    else
	    {
		clsDebtorDtl objDebtor = new clsDebtorDtl();
		objDebtor.setStrDebtorCode(mJsonObject.get("DebtorCode").toString());
		objDebtor.setStrDebtorName(mJsonObject.get("DebtorName").toString());
		debtorInfo.add(objDebtor);
	    }
	}
	conn.disconnect();
	return debtorInfo;
    }
    
     public List<clsLinkupDtl> funGetSubGroupDtl(String clientCode) throws Exception
    {
	List<clsLinkupDtl> productInfo = new ArrayList<clsLinkupDtl>();

	String cmsURL = clsGlobalVarClass.gSanguineWebServiceURL + "/MMSIntegration/funGetSubGroupMaster?ClientCode=" + clientCode;
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
	JSONArray mJsonArray = (JSONArray) jObj.get("SubGroupDtls");

	JSONObject mJsonObject = new JSONObject();
	for (int i = 0; i < mJsonArray.size(); i++)
	{
	    mJsonObject = (JSONObject) mJsonArray.get(i);
	    if (mJsonObject.get("SubGroupCode").toString().equals(""))
	    {
		//accountInfo = "no data";
	    }
	    else
	    {
		clsLinkupDtl objLinkup = new clsLinkupDtl();
		objLinkup.setStrLinkupCode(mJsonObject.get("SubGroupCode").toString());
		objLinkup.setStrLinkupName(mJsonObject.get("SubGroupName").toString());
		productInfo.add(objLinkup);
	    }
	}
	conn.disconnect();
	return productInfo;
    }
     
     
}
