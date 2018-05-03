/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSGlobal.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Sanguine
 */
public class clsCRMInterface
{

    public List<clsRewards> funGetCustomerRewards(String mobileNo)
    {
	List<clsRewards> listOfReward = new ArrayList<>();

	try
	{
	    //9039541197 for testing
	    String crmWebServiceURL = clsGlobalVarClass.gGetWebserviceURL + "/rewards?number=" + mobileNo + "&token=" + clsGlobalVarClass.gOutletUID;
	    URL url = new URL(crmWebServiceURL);
	    //HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            System.setProperty("javax.net.ssl.keyStoreType","pkcs12");
//            System.setProperty("javax.net.ssl.trustStoreType","jks");
//            System.setProperty("javax.net.ssl.keyStore","clientcertificate.p12");
//            System.setProperty("javax.net.ssl.trustStore","gridserver.keystore");
//            System.setProperty("javax.net.debug","ssl # debug");
//            System.setProperty("javax.net.ssl.keyStorePassword","$PASS");
//            System.setProperty("javax.net.ssl.trustStorePassword","$PASS");

	    //SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault(); 
	    /**
	     * Trust All certificates for https request
	     */
	    TrustManager[] trustAllCerts = new TrustManager[]
	    {
		new X509TrustManager()
		{
		    public java.security.cert.X509Certificate[] getAcceptedIssuers()
		    {
			return null;
		    }

		    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
		    {
			System.out.println("checkClientTrusted");
		    }

		    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
		    {
			System.out.println("checkServerTrusted");
		    }
		}
	    };

	    SSLContext sc = SSLContext.getInstance("SSL");
	    sc.init(null, trustAllCerts, new java.security.SecureRandom());
	    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

	    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
	    //conn.setSSLSocketFactory(sslsocketfactory);
	    conn.setRequestMethod("GET");
	    conn.setRequestProperty("Accept", "application/json");

	    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	    String output = "", jsonString = "";
	    while ((output = br.readLine()) != null)
	    {
		jsonString += output;
	    }
	    System.out.println("conn cer->" + conn.getServerCertificates());
	    conn.disconnect();

	    JSONParser parser = new JSONParser();
	    Object obj = parser.parse(jsonString);
	    JSONObject rootJSON = (JSONObject) obj;

	    JSONObject jsonUserInformation = (JSONObject) rootJSON.get("user_information");

	    //for "normal_rewards"
	    JSONArray jsonArrNormalRewards = (JSONArray) jsonUserInformation.get("normal_rewards");
	    for (int i = 0; i < jsonArrNormalRewards.size(); i++)
	    {
		JSONObject jsonReward = (JSONObject) jsonArrNormalRewards.get(i);
		System.out.println("" + jsonReward.toString());

		clsRewards objRewards = new clsRewards();
		objRewards.setStrRewardType("Normal Rewards");
		objRewards.setStrRewardId(jsonReward.get("id").toString());
		objRewards.setStrRewardName(jsonReward.get("name").toString());
		objRewards.setStrRewardCategory(jsonReward.get("category").toString());
		if (jsonReward.get("points") == null || jsonReward.get("points").toString().trim().isEmpty())
		{
		    objRewards.setStrPoints("NA");
		}
		else
		{
		    objRewards.setStrPoints(jsonReward.get("points").toString());
		}
		objRewards.setStrRewardPoints(jsonReward.get("reward_points").toString());
		objRewards.setStrRewardPOSItemCode(jsonReward.get("pos_item_id").toString());
		objRewards.setItemOff(Boolean.parseBoolean(jsonReward.get("is_item_off").toString()));
		listOfReward.add(objRewards);
	    }

	    //for "campaign"
	    JSONArray jsonArrCampaign = (JSONArray) jsonUserInformation.get("campaign");
	    for (int i = 0; i < jsonArrCampaign.size(); i++)
	    {
		JSONObject json = (JSONObject) jsonArrCampaign.get(i);
		System.out.println("" + json.toString());

		clsRewards objRewards = new clsRewards();
		objRewards.setStrRewardType("Campaign");
		objRewards.setStrRewardId(json.get("id").toString());
		objRewards.setStrRewardName(json.get("name").toString());
		objRewards.setStrRewardCategory(json.get("category").toString());
		if (json.get("points").toString().trim().isEmpty())
		{
		    objRewards.setStrRewardPoints("NA");
		}
		else
		{
		    objRewards.setStrRewardPoints(json.get("points").toString());
		}
		objRewards.setStrRewardPOSItemCode(json.get("pos_item_id").toString());

		listOfReward.add(objRewards);
	    }

	    //for "promotion"
	    JSONArray jsonArrPromotion = (JSONArray) jsonUserInformation.get("promotion");
	    for (int i = 0; i < jsonArrPromotion.size(); i++)
	    {
		JSONObject json = (JSONObject) jsonArrPromotion.get(i);
		System.out.println("" + json.toString());

		clsRewards objRewards = new clsRewards();
		objRewards.setStrRewardType("Promotion");
		objRewards.setStrRewardId(json.get("id").toString());
		objRewards.setStrRewardName(json.get("name").toString());
		objRewards.setStrRewardCategory(json.get("category").toString());
		if (json.get("points").toString().trim().isEmpty())
		{
		    objRewards.setStrRewardPoints("NA");
		}
		else
		{
		    objRewards.setStrRewardPoints(json.get("points").toString());
		}
		objRewards.setStrRewardPOSItemCode(json.get("pos_item_id").toString());

		listOfReward.add(objRewards);
	    }

	    //for "promo_codes"
	    JSONArray jsonArrPromoCodes = (JSONArray) jsonUserInformation.get("promo_codes");
	    for (int i = 0; i < jsonArrPromoCodes.size(); i++)
	    {
		JSONObject json = (JSONObject) jsonArrPromoCodes.get(i);
		System.out.println("" + json.toString());

		clsRewards objRewards = new clsRewards();
		objRewards.setStrRewardType("Promo Codes");
		objRewards.setStrRewardId(json.get("id").toString());
		objRewards.setStrRewardName(json.get("name").toString());
		objRewards.setStrRewardCategory(json.get("category").toString());
		if (json.get("points").toString().trim().isEmpty())
		{
		    objRewards.setStrRewardPoints("NA");
		}
		else
		{
		    objRewards.setStrRewardPoints(json.get("points").toString());
		}
		objRewards.setStrRewardPOSItemCode(json.get("pos_item_id").toString());

		listOfReward.add(objRewards);
	    }

	    //for "referral_rewards"
	    JSONArray jsonArrReferralRewards = (JSONArray) jsonUserInformation.get("referral_rewards");
	    for (int i = 0; i < jsonArrReferralRewards.size(); i++)
	    {
		JSONObject json = (JSONObject) jsonArrReferralRewards.get(i);
		System.out.println("" + json.toString());

		clsRewards objRewards = new clsRewards();
		objRewards.setStrRewardType("Referral Rewards");
		objRewards.setStrRewardId(json.get("id").toString());
		objRewards.setStrRewardName(json.get("name").toString());
		objRewards.setStrRewardCategory(json.get("category").toString());
		if (json.get("points").toString().trim().isEmpty())
		{
		    objRewards.setStrRewardPoints("NA");
		}
		else
		{
		    objRewards.setStrRewardPoints(json.get("points").toString());
		}
		objRewards.setStrRewardPOSItemCode(json.get("pos_item_id").toString());

		listOfReward.add(objRewards);
	    }

	    //for "reffered_rewards"
	    JSONArray jsonArrRefferedRewards = (JSONArray) jsonUserInformation.get("reffered_rewards");
	    for (int i = 0; i < jsonArrRefferedRewards.size(); i++)
	    {
		JSONObject json = (JSONObject) jsonArrRefferedRewards.get(i);
		System.out.println("" + json.toString());

		clsRewards objRewards = new clsRewards();
		objRewards.setStrRewardType("Reffered Rewards");
		objRewards.setStrRewardId(json.get("id").toString());
		objRewards.setStrRewardName(json.get("name").toString());
		objRewards.setStrRewardCategory(json.get("category").toString());
		if (json.get("points").toString().trim().isEmpty())
		{
		    objRewards.setStrRewardPoints("NA");
		}
		else
		{
		    objRewards.setStrRewardPoints(json.get("points").toString());
		}
		objRewards.setStrRewardPOSItemCode(json.get("pos_item_id").toString());

		listOfReward.add(objRewards);
	    }

	    //for "name"
	    String customerName = jsonUserInformation.get("name").toString();
	    System.out.println("name->" + customerName);

	}
	catch (MalformedURLException e)
	{
	    e.printStackTrace();
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
	catch (NoSuchAlgorithmException e)
	{
	    e.printStackTrace();
	}
	catch (KeyManagementException e)
	{
	    e.printStackTrace();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    return listOfReward;
	}

    }

    public void funPostBillDataCRM(String customerCode, String billNo, String billDate)
    {
	try
	{

	    JSONObject objData = new JSONObject();
	    JSONObject objRootJSON = new JSONObject();

	    //filling restaurant deatils
	    JSONObject objRestaurant = new JSONObject();

	    objRestaurant.put("res_name", clsGlobalVarClass.gClientName);
	    objRestaurant.put("restID", clsGlobalVarClass.gClientCode);
	    objRestaurant.put("eateryBranchCode", clsGlobalVarClass.gClientName);
	    objRestaurant.put("contact_information", clsGlobalVarClass.gClientTelNo);
	    objRestaurant.put("address", clsGlobalVarClass.gClientAddress1 + " " + clsGlobalVarClass.gClientAddress2 + " " + clsGlobalVarClass.gClientAddress3);

	    objRootJSON.put("Restaurant", objRestaurant);

	    //filling cUSTOMER deatils
	    JSONObject objCustomer = new JSONObject();

	    String sqlCustDtl = "select a.strCustomerCode,a.longMobileNo,a.strCustomerName,a.strCustAddress,a.strStreetName,a.strLandmark,a.strArea,a.strCity "
		    + ",a.strState,a.intPinCode,a.longAlternateMobileNo,a.dteDOB,a.strGender ,a.dteAnniversary,a.strCRMId,a.strTempAddress,a.strGSTNo   "
		    + "from tblcustomermaster a where a.strCustomerCode='" + customerCode + "' ";
	    ResultSet rsCust = clsGlobalVarClass.dbMysql.executeResultSet(sqlCustDtl);
	    if (rsCust.next())
	    {
		objCustomer.put("phone", rsCust.getString(2));
		objCustomer.put("name", rsCust.getString(3));
		objCustomer.put("loyaltyCode", "");
		objCustomer.put("address", rsCust.getString(4));
		objCustomer.put("strStreetName", rsCust.getString(5));
		objCustomer.put("strLandmark", rsCust.getString(6));
		objCustomer.put("strArea", rsCust.getString(7));
		objCustomer.put("strCity", rsCust.getString(8));
		objCustomer.put("strState", rsCust.getString(9));
		objCustomer.put("intPinCode", rsCust.getString(10));
		objCustomer.put("longAlternateMobileNo", rsCust.getString(11));
		objCustomer.put("dteDOB", rsCust.getString(12));
		objCustomer.put("strGender", rsCust.getString(13));
		objCustomer.put("dteAnniversary", rsCust.getString(14));
		objCustomer.put("strCRMId", rsCust.getString(15));
		objCustomer.put("strTempAddress", rsCust.getString(16));
		objCustomer.put("strGSTNo", rsCust.getString(17));

	    }
	    rsCust.close();
	    objRootJSON.put("Customer", objCustomer);

	    //filling order deatils
	    JSONObject objOrderDtl = new JSONObject();

	    String sqlBillHd = "select a.strBillNo,a.dteBillDate,a.strPOSCode,a.dblSubTotal,a.dblDiscountAmt,(a.dblSubTotal-a.dblDiscountAmt)dblNetTotal "
		    + ",a.dblTaxAmt,a.dblGrandTotal,a.strSettelmentMode,a.intBillSeriesPaxNo,a.strOperationType,a.dblRoundOff,a.strTableNo,a.dblDeliveryCharges   "
		    + "from tblbillhd a where a.strBillNo='" + billNo + "' and date(a.dtBillDate)='" + billDate + "' ";
	    ResultSet rsBillHd = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillHd);
	    if (rsBillHd.next())
	    {
		objOrderDtl.put("orderID", billNo);
		objOrderDtl.put("core_total", rsBillHd.getDouble(4));//subTotal
		objOrderDtl.put("created_on", clsGlobalVarClass.getCurrentDateTime());
		objOrderDtl.put("delivery_charges", rsBillHd.getDouble(14));
		objOrderDtl.put("discount_total", rsBillHd.getDouble(5));//discAmt
		objOrderDtl.put("no_of_persons", rsBillHd.getInt(10));
		objOrderDtl.put("order_type", rsBillHd.getString(11));
		objOrderDtl.put("payment_type", rsBillHd.getString(9));
		objOrderDtl.put("round_off", rsBillHd.getDouble(12));
		objOrderDtl.put("table_no", rsBillHd.getString(13));
		objOrderDtl.put("tax_total", rsBillHd.getDouble(7));//taxAmt
		objOrderDtl.put("total", rsBillHd.getDouble(8));//grandTotal
	    }
	    rsBillHd.close();
	    objRootJSON.put("Order", objOrderDtl);

	    //filling order item deatils
	    JSONArray jsonArrOrderItemDtl = new JSONArray();
	    String sqlBillDtl = "select a.strBillNo,a.strItemCode,a.strItemName,a.dblRate,a.dblQuantity,a.dblAmount,a.dblTaxAmount "
		    + "from tblbilldtl a where a.strBillNo='" + billNo + "' and date(a.dtBillDate)='" + billDate + "' ";
	    ResultSet rsBillDtl = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillDtl);
	    if (rsBillDtl.next())
	    {

		JSONObject objItemDtl = new JSONObject();

		objItemDtl.put("itemid", rsBillDtl.getString(2));
		objItemDtl.put("name", rsBillDtl.getString(3));
		objItemDtl.put("price", rsBillDtl.getDouble(4));
		objItemDtl.put("quantity", rsBillDtl.getDouble(5));
		objItemDtl.put("specialnotes", "");
		objItemDtl.put("total", rsBillDtl.getDouble(6));

		jsonArrOrderItemDtl.add(objItemDtl);

	    }
	    rsBillDtl.close();
	    objRootJSON.put("OrderItem", jsonArrOrderItemDtl);

	    //filling discount deatils
	    JSONArray jsonArrDiscount = new JSONArray();
	    objRootJSON.put("Discount", jsonArrDiscount);

	    //filling Tax deatils
	    JSONArray jsonArrTax = new JSONArray();
	    objRootJSON.put("Tax", jsonArrTax);

	    //merchant credentials
	    objData.put("token", clsGlobalVarClass.gOutletUID);//token
	    objData.put("data", objRootJSON);

	    String crmWebServiceURL = clsGlobalVarClass.gGetWebserviceURL + "/new";

	    /**
	     * Trust All certificates for https request
	     */
	    TrustManager[] trustAllCerts = new TrustManager[]
	    {
		new X509TrustManager()
		{
		    public java.security.cert.X509Certificate[] getAcceptedIssuers()
		    {
			return null;
		    }

		    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
		    {
			System.out.println("checkClientTrusted");
		    }

		    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
		    {
			System.out.println("checkServerTrusted");
		    }
		}
	    };

	    SSLContext sc = SSLContext.getInstance("SSL");
	    sc.init(null, trustAllCerts, new java.security.SecureRandom());
	    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

	    URL url = new URL(crmWebServiceURL);
	    //HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    OutputStream os = conn.getOutputStream();
	    os.write(objData.toString().getBytes());
	    os.flush();
	    if (conn.getResponseCode() != HttpsURLConnection.HTTP_CREATED)
	    {
		throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
	    }
	    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	    String output = "", op = "";

	    while ((output = br.readLine()) != null)
	    {
		op += output;
	    }
	    System.out.println("funPostBillDataCRM Hash Tag Loyalty CRM Interface Response=" + op);
	    conn.disconnect();
	}
	catch (MalformedURLException e)
	{
	    e.printStackTrace();
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
	catch (NoSuchAlgorithmException e)
	{
	    e.printStackTrace();
	}
	catch (KeyManagementException e)
	{
	    e.printStackTrace();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public void funPostRewardRedeemCRM(String mobileNo, String rewardId)
    {
	try
	{

	    JSONObject objRootJSON = new JSONObject();
	    objRootJSON.put("token", clsGlobalVarClass.gOutletUID);
	    objRootJSON.put("number", mobileNo);
	    objRootJSON.put("reward_id", rewardId);

	    String crmWebServiceURL = clsGlobalVarClass.gGetWebserviceURL + "/reward-redeem";

	    /**
	     * Trust All certificates for https request
	     */
	    TrustManager[] trustAllCerts = new TrustManager[]
	    {
		new X509TrustManager()
		{
		    public java.security.cert.X509Certificate[] getAcceptedIssuers()
		    {
			return null;
		    }

		    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
		    {
			System.out.println("checkClientTrusted");
		    }

		    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
		    {
			System.out.println("checkServerTrusted");
		    }
		}
	    };

	    SSLContext sc = SSLContext.getInstance("SSL");
	    sc.init(null, trustAllCerts, new java.security.SecureRandom());
	    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

	    URL url = new URL(crmWebServiceURL);
	    //HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    OutputStream os = conn.getOutputStream();
	    os.write(objRootJSON.toString().getBytes());
	    os.flush();
	    if (conn.getResponseCode() != HttpsURLConnection.HTTP_CREATED)
	    {
		throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
	    }
	    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	    String output = "", op = "";

	    while ((output = br.readLine()) != null)
	    {
		op += output;
	    }
	    System.out.println("funPostRewardRedeemCRM Hash Tag Loyalty CRM Interface Response=" + op);
	    conn.disconnect();
	}
	catch (MalformedURLException e)
	{
	    e.printStackTrace();
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
	catch (NoSuchAlgorithmException e)
	{
	    e.printStackTrace();
	}
	catch (KeyManagementException e)
	{
	    e.printStackTrace();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public clsRewards funGetCustomerRewards(String mobileNo, String rewardId)
    {
	clsRewards objRewards = null;

	try
	{
	    //9039541197 for testing
	    String crmWebServiceURL = clsGlobalVarClass.gGetWebserviceURL + "/rewards?number=" + mobileNo + "&token=" + clsGlobalVarClass.gOutletUID;
	    URL url = new URL(crmWebServiceURL);
	    //HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            System.setProperty("javax.net.ssl.keyStoreType","pkcs12");
//            System.setProperty("javax.net.ssl.trustStoreType","jks");
//            System.setProperty("javax.net.ssl.keyStore","clientcertificate.p12");
//            System.setProperty("javax.net.ssl.trustStore","gridserver.keystore");
//            System.setProperty("javax.net.debug","ssl # debug");
//            System.setProperty("javax.net.ssl.keyStorePassword","$PASS");
//            System.setProperty("javax.net.ssl.trustStorePassword","$PASS");

	    //SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault(); 
	    /**
	     * Trust All certificates for https request
	     */
	    TrustManager[] trustAllCerts = new TrustManager[]
	    {
		new X509TrustManager()
		{
		    public java.security.cert.X509Certificate[] getAcceptedIssuers()
		    {
			return null;
		    }

		    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
		    {
			System.out.println("checkClientTrusted");
		    }

		    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
		    {
			System.out.println("checkServerTrusted");
		    }
		}
	    };

	    SSLContext sc = SSLContext.getInstance("SSL");
	    sc.init(null, trustAllCerts, new java.security.SecureRandom());
	    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

	    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
	    //conn.setSSLSocketFactory(sslsocketfactory);
	    conn.setRequestMethod("GET");
	    conn.setRequestProperty("Accept", "application/json");

	    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	    String output = "", jsonString = "";
	    while ((output = br.readLine()) != null)
	    {
		jsonString += output;
	    }
	    System.out.println("conn cer->" + conn.getServerCertificates());
	    conn.disconnect();

	    JSONParser parser = new JSONParser();
	    Object obj = parser.parse(jsonString);
	    JSONObject rootJSON = (JSONObject) obj;

	    JSONObject jsonUserInformation = (JSONObject) rootJSON.get("user_information");

	    //for "normal_rewards"
	    JSONArray jsonArrNormalRewards = (JSONArray) jsonUserInformation.get("normal_rewards");
	    for (int i = 0; i < jsonArrNormalRewards.size(); i++)
	    {
		JSONObject jsonReward = (JSONObject) jsonArrNormalRewards.get(i);

		if (jsonReward.get("id").toString().equalsIgnoreCase(rewardId))
		{
		    System.out.println("" + jsonReward.toString());

		    objRewards = new clsRewards();
		    objRewards.setStrRewardType("Normal Rewards");
		    objRewards.setStrRewardId(jsonReward.get("id").toString());
		    objRewards.setStrRewardName(jsonReward.get("name").toString());
		    objRewards.setStrRewardCategory(jsonReward.get("category").toString());
		    if (jsonReward.get("points") == null || jsonReward.get("points").toString().trim().isEmpty())
		    {
			objRewards.setStrPoints("NA");
		    }
		    else
		    {
			objRewards.setStrPoints(jsonReward.get("points").toString());
		    }
		    objRewards.setStrRewardPoints(jsonReward.get("reward_points").toString());
		    objRewards.setStrRewardPOSItemCode(jsonReward.get("pos_item_id").toString());
		    objRewards.setItemOff(Boolean.parseBoolean(jsonReward.get("is_item_off").toString()));

		}
	    }

	    //for "campaign"
	    JSONArray jsonArrCampaign = (JSONArray) jsonUserInformation.get("campaign");
	    for (int i = 0; i < jsonArrCampaign.size(); i++)
	    {
		JSONObject json = (JSONObject) jsonArrCampaign.get(i);
		if (json.get("id").toString().equalsIgnoreCase(rewardId))
		{
		    System.out.println("" + json.toString());

		    objRewards = new clsRewards();
		    objRewards.setStrRewardType("Campaign");
		    objRewards.setStrRewardId(json.get("id").toString());
		    objRewards.setStrRewardName(json.get("name").toString());
		    objRewards.setStrRewardCategory(json.get("category").toString());
		    if (json.get("points").toString().trim().isEmpty())
		    {
			objRewards.setStrRewardPoints("NA");
		    }
		    else
		    {
			objRewards.setStrRewardPoints(json.get("points").toString());
		    }
		    objRewards.setStrRewardPOSItemCode(json.get("pos_item_id").toString());
		}
	    }

	    //for "promotion"
	    JSONArray jsonArrPromotion = (JSONArray) jsonUserInformation.get("promotion");
	    for (int i = 0; i < jsonArrPromotion.size(); i++)
	    {
		JSONObject json = (JSONObject) jsonArrPromotion.get(i);
		if (json.get("id").toString().equalsIgnoreCase(rewardId))
		{
		    System.out.println("" + json.toString());

		    objRewards = new clsRewards();
		    objRewards.setStrRewardType("Promotion");
		    objRewards.setStrRewardId(json.get("id").toString());
		    objRewards.setStrRewardName(json.get("name").toString());
		    objRewards.setStrRewardCategory(json.get("category").toString());
		    if (json.get("points").toString().trim().isEmpty())
		    {
			objRewards.setStrRewardPoints("NA");
		    }
		    else
		    {
			objRewards.setStrRewardPoints(json.get("points").toString());
		    }
		    objRewards.setStrRewardPOSItemCode(json.get("pos_item_id").toString());
		}
	    }

	    //for "promo_codes"
	    JSONArray jsonArrPromoCodes = (JSONArray) jsonUserInformation.get("promo_codes");
	    for (int i = 0; i < jsonArrPromoCodes.size(); i++)
	    {
		JSONObject json = (JSONObject) jsonArrPromoCodes.get(i);

		if (json.get("id").toString().equalsIgnoreCase(rewardId))
		{
		    System.out.println("" + json.toString());

		    objRewards = new clsRewards();
		    objRewards.setStrRewardType("Promo Codes");
		    objRewards.setStrRewardId(json.get("id").toString());
		    objRewards.setStrRewardName(json.get("name").toString());
		    objRewards.setStrRewardCategory(json.get("category").toString());
		    if (json.get("points").toString().trim().isEmpty())
		    {
			objRewards.setStrRewardPoints("NA");
		    }
		    else
		    {
			objRewards.setStrRewardPoints(json.get("points").toString());
		    }
		    objRewards.setStrRewardPOSItemCode(json.get("pos_item_id").toString());
		}
	    }

	    //for "referral_rewards"
	    JSONArray jsonArrReferralRewards = (JSONArray) jsonUserInformation.get("referral_rewards");
	    for (int i = 0; i < jsonArrReferralRewards.size(); i++)
	    {
		JSONObject json = (JSONObject) jsonArrReferralRewards.get(i);

		if (json.get("id").toString().equalsIgnoreCase(rewardId))
		{
		    System.out.println("" + json.toString());

		    objRewards = new clsRewards();
		    objRewards.setStrRewardType("Referral Rewards");
		    objRewards.setStrRewardId(json.get("id").toString());
		    objRewards.setStrRewardName(json.get("name").toString());
		    objRewards.setStrRewardCategory(json.get("category").toString());
		    if (json.get("points").toString().trim().isEmpty())
		    {
			objRewards.setStrRewardPoints("NA");
		    }
		    else
		    {
			objRewards.setStrRewardPoints(json.get("points").toString());
		    }
		    objRewards.setStrRewardPOSItemCode(json.get("pos_item_id").toString());
		}
	    }

	    //for "reffered_rewards"
	    JSONArray jsonArrRefferedRewards = (JSONArray) jsonUserInformation.get("reffered_rewards");
	    for (int i = 0; i < jsonArrRefferedRewards.size(); i++)
	    {
		JSONObject json = (JSONObject) jsonArrRefferedRewards.get(i);

		if (json.get("id").toString().equalsIgnoreCase(rewardId))
		{

		    System.out.println("" + json.toString());
		    objRewards = new clsRewards();
		    objRewards.setStrRewardType("Reffered Rewards");
		    objRewards.setStrRewardId(json.get("id").toString());
		    objRewards.setStrRewardName(json.get("name").toString());
		    objRewards.setStrRewardCategory(json.get("category").toString());
		    if (json.get("points").toString().trim().isEmpty())
		    {
			objRewards.setStrRewardPoints("NA");
		    }
		    else
		    {
			objRewards.setStrRewardPoints(json.get("points").toString());
		    }
		    objRewards.setStrRewardPOSItemCode(json.get("pos_item_id").toString());
		}

	    }

	    //for "name"
	    String customerName = jsonUserInformation.get("name").toString();
	    System.out.println("name->" + customerName);

	}
	catch (MalformedURLException e)
	{
	    e.printStackTrace();
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
	catch (NoSuchAlgorithmException e)
	{
	    e.printStackTrace();
	}
	catch (KeyManagementException e)
	{
	    e.printStackTrace();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    return objRewards;
	}

    }

}
