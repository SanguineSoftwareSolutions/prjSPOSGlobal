/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSGlobal.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONObject;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import org.apache.commons.codec.binary.Base64;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.ImageIcon;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Monika
 */
public class clsBenowIntegration
{

    public String funGetDynamicQRString(String billNo, double amount)
    {
	String QRString = "NotFound";
	try
	{

	    String benowURL = "https://mobilepayments.benow.in/merchants/merchant/getDynamicQRString";

	    JSONObject jObjInnerPayLoad = new JSONObject();
	    jObjInnerPayLoad.put("merchantCode", clsGlobalVarClass.gBenowMerchantCode);//from property setup
	    jObjInnerPayLoad.put("amount", amount);
	    jObjInnerPayLoad.put("refNumber", billNo);
	    jObjInnerPayLoad.put("paymentMethod", "BHARAT_QR");//BHARAT_QR/UPI
	    jObjInnerPayLoad.put("remarks", "Payment for BillNo = " + billNo);
	    // salt from proprty setup "abcd"
	    String encString = encrypt(clsGlobalVarClass.gBenowSalt, jObjInnerPayLoad.toString());

	    JSONObject jObjPayLoad = new JSONObject();
	    jObjPayLoad.put("encryptedString", encString);
	    jObjPayLoad.put("jsonString", jObjInnerPayLoad.toString());

	    System.out.println(jObjPayLoad.toJSONString());

	    URL url = new URL(benowURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    //get value from property setup for authentication key and xemail
	    conn.setRequestProperty("AuthorizationKey", clsGlobalVarClass.gBenowAuthenticationKey);
	    conn.setRequestProperty("X-EMAIL", clsGlobalVarClass.gBenowXEmail);

	    OutputStream os = conn.getOutputStream();
	    os.write(jObjPayLoad.toJSONString().getBytes());
	    os.flush();
	    /*if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED)
            {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }*/
	    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	    String output = "", op = "";

	    while ((output = br.readLine()) != null)
	    {
		op += output;
	    }
	    System.out.println("Response=" + op);
	    QRString = op;
	    conn.disconnect();

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return QRString;
    }

    public JSONObject funCheckTransactionStatusForBenowPayment(String billNo)
    {
	JSONObject jsonTransStatus = null;
	try
	{

	    String benowURL = "https://mobilepayments.benow.in/payments/ecomm/getMerchantTransactionStatus";

	    JSONObject jObjInnerPayLoad = new JSONObject();
	    jObjInnerPayLoad.put("merchantCode", clsGlobalVarClass.gBenowMerchantCode);//from property setup
	    jObjInnerPayLoad.put("refNumber", billNo);
	    // salt from proprty setup "abcd"
	    String encString = encrypt(clsGlobalVarClass.gBenowSalt, jObjInnerPayLoad.toString());

	    JSONObject jObjPayLoad = new JSONObject();
	    jObjPayLoad.put("encryptedString", encString);
	    jObjPayLoad.put("jsonString", jObjInnerPayLoad.toString());

	    System.out.println(jObjPayLoad.toJSONString());

	    URL url = new URL(benowURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    //get value from property setup for authentication key and xemail
	    conn.setRequestProperty("AuthorizationKey", clsGlobalVarClass.gBenowAuthenticationKey);
	    conn.setRequestProperty("X-EMAIL", clsGlobalVarClass.gBenowXEmail);

	    OutputStream os = conn.getOutputStream();
	    os.write(jObjPayLoad.toJSONString().getBytes());
	    os.flush();
	    /*if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED)
            {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }*/
	    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	    String output = "", op = "";

	    while ((output = br.readLine()) != null)
	    {
		op += output;
	    }
	    System.out.println("Response=" + op);

	    JSONParser parser = new JSONParser();
	    JSONArray json = (JSONArray) parser.parse(op);
	    jsonTransStatus = (JSONObject) json.get(0);
	    conn.disconnect();

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return jsonTransStatus;
    }

    public JSONObject funGetOTP(String merchantCode, String superMerchantCode)
    {
	JSONObject jsonGetOTPDtls = new JSONObject();
	try
	{

	    String benowURL = "https://mobilepayments.benow.in/merchants/merchant/sendMerchantActivationOtp";

	    JSONObject jObjInnerPayLoad = new JSONObject();
	    jObjInnerPayLoad.put("merchantCode", merchantCode);//from property setup
	    jObjInnerPayLoad.put("superMerchantCode", superMerchantCode);//from property setup
	    // salt from proprty setup "abcd"
	    String encString = encrypt(clsGlobalVarClass.gBenowSalt, jObjInnerPayLoad.toString());

	    JSONObject jObjPayLoad = new JSONObject();
	    jObjPayLoad.put("encryptedString", encString);
	    jObjPayLoad.put("jsonString", jObjInnerPayLoad.toString());

	    System.out.println(jObjPayLoad.toJSONString());

	    URL url = new URL(benowURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    //get value from property setup for authentication key and xemail
	    conn.setRequestProperty("AuthorizationKey", clsGlobalVarClass.gBenowAuthenticationKey);
	    conn.setRequestProperty("X-EMAIL", clsGlobalVarClass.gBenowXEmail);

	    OutputStream os = conn.getOutputStream();
	    os.write(jObjPayLoad.toJSONString().getBytes());
	    os.flush();
	    /*if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED)
            {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }*/
	    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	    String output = "", op = "";

	    while ((output = br.readLine()) != null)
	    {
		op += output;
	    }
	    System.out.println("Response=" + op);
	    JSONParser parser = new JSONParser();
	    JSONArray json = (JSONArray) parser.parse(op);
	    jsonGetOTPDtls = (JSONObject) json.get(0);
	    conn.disconnect();

	}
	catch (Exception e)
	{
	    jsonGetOTPDtls.put("responseFromAPI", true);
	    e.printStackTrace();
	}
	return jsonGetOTPDtls;
    }

    public JSONObject funConfirmOTP(String merchantCode, String otpString, String superMerchantCode)
    {
	String OTPString = "True";
	JSONObject jsonGetConfirmOTPDtls = new JSONObject();
	try
	{

	    String benowURL = "https://mobilepayments.benow.in/merchants/merchant/getIntegrationCredential";

	    JSONObject jObjInnerPayLoad = new JSONObject();
	    jObjInnerPayLoad.put("merchantCode", merchantCode);//from property setup
	    jObjInnerPayLoad.put("superMerchantCode", superMerchantCode);//from property setup
	    jObjInnerPayLoad.put("otpString", otpString);
	    // salt from proprty setup "abcd"
	    String encString = encrypt(clsGlobalVarClass.gBenowSalt, jObjInnerPayLoad.toString());

	    JSONObject jObjPayLoad = new JSONObject();
	    jObjPayLoad.put("encryptedString", encString);
	    jObjPayLoad.put("jsonString", jObjInnerPayLoad.toString());

	    System.out.println(jObjPayLoad.toJSONString());

	    URL url = new URL(benowURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    //get value from property setup for authentication key and xemail
	    conn.setRequestProperty("AuthorizationKey", clsGlobalVarClass.gBenowAuthenticationKey);
	    conn.setRequestProperty("X-EMAIL", clsGlobalVarClass.gBenowXEmail);

	    OutputStream os = conn.getOutputStream();
	    os.write(jObjPayLoad.toJSONString().getBytes());
	    os.flush();
	    /*if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED)
            {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }*/
	    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	    String output = "", op = "";

	    while ((output = br.readLine()) != null)
	    {
		op += output;
	    }
	    System.out.println("Response=" + op);
	    OTPString = op;
	    JSONParser parser = new JSONParser();
	    JSONArray json = (JSONArray) parser.parse(op);
	    jsonGetConfirmOTPDtls = (JSONObject) json.get(0);
	    conn.disconnect();

	}
	catch (Exception e)
	{
	    jsonGetConfirmOTPDtls.put("mobileNumber", "");
	    e.printStackTrace();
	}
	return jsonGetConfirmOTPDtls;
    }

    public static String encrypt(String merchantkey, String value)
    {
	try
	{
	    byte[] key = getKey(merchantkey).getBytes();
	    String initVector = "xxxxyyyyzzzzwwww";
	    IvParameterSpec iv = new IvParameterSpec(initVector.getBytes());
	    SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
	    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	    cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
	    byte[] encrypted = cipher.doFinal(value.getBytes());
	    return Base64.encodeBase64String(encrypted);
	}
	catch (Exception ex)
	{
	    ex.printStackTrace();
	}
	return null;
    }

    public static String getKey(String token)
    {
	try
	{
	    MessageDigest digest = null;
	    digest = MessageDigest.getInstance("SHA-256");
	    byte[] key = digest.digest(token.getBytes());
	    String keyStr = bin2hex(key).toLowerCase();
	    digest = MessageDigest.getInstance("MD5");
	    key = digest.digest(keyStr.getBytes());
	    keyStr = bin2hex(key).toLowerCase();
	    if (keyStr.length() > 16)
	    {
		return keyStr.substring(0, 16);
	    }
	    else
	    {
		return keyStr;
	    }
	}
	catch (NoSuchAlgorithmException e)
	{
	    e.printStackTrace();
	}
	return "";
    }

    static String bin2hex(byte[] data)
    {
	return String.format("%0" + (data.length * 2) + "X", new BigInteger(1, data));
    }

    public String funGenerateQrCode(String QRString)
    {
	String filePath = "QRCode.png";

	try
	{
	    String charset = "UTF-8"; // or "ISO-8859-1"
	    Map hintMap = new HashMap();
	    hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
	    createQRCode(QRString, filePath, charset, hintMap, 135, 135);
	    System.out.println("QR Code image created successfully!");
	    System.out.println("Data read from QR Code: " + readQRCode(filePath, charset, hintMap));
	    funCopyImageIfPresent(new File(filePath));
	}
	catch (Exception e)
	{
	    filePath = "Error";
	    e.printStackTrace();
	}

	return filePath;
    }

    public void createQRCode(String qrCodeData, String filePath, String charset, Map hintMap, int qrCodeheight, int qrCodewidth) throws WriterException, IOException
    {
	BitMatrix matrix = new MultiFormatWriter().encode(new String(qrCodeData.getBytes(charset), charset), BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);
	MatrixToImageWriter.writeToFile(matrix, filePath.substring(filePath.lastIndexOf('.') + 1), new File(filePath));
    }

    public String readQRCode(String filePath, String charset, Map hintMap) throws FileNotFoundException, IOException, NotFoundException
    {
	BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(filePath)))));
	Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap, hintMap);
	return qrCodeResult.getText();
    }

    private void funCopyImageIfPresent(File tempFile) throws IOException
    {
	if (null != tempFile)
	{
	    String filePath = "StaticQRCode.png";

	    File file = new File(filePath);
	    if (!file.exists())
	    {
		if (file.mkdir())
		{
		}
		else
		{
		}
	    }

	    File destFile = new File(filePath);
	    if (destFile.exists())
	    {
		destFile.setExecutable(true);
		destFile.setWritable(true);
		destFile.delete();
	    }
	    copyImageFiles(tempFile, destFile);
	}
    }

    private void copyImageFiles(File source, File dest) throws IOException, SecurityException
    {
	String src = source.toString();
	String destination = dest.toString();
	try
	{
	    boolean bool = false;
	    bool = dest.delete();
	    Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
	}
	catch (FileAlreadyExistsException ex)
	{
	    ex.printStackTrace();
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
	System.gc();
    }

    private void funCreateitemImagesFolder()
    {
	try
	{
	    String filePath = "QRCode.png";
	    File file = new File(filePath);
	    if (!file.exists())
	    {
		if (file.mkdir())
		{
		}
		else
		{
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public String funGenerateStaticQRCodeString(String filePath)
    {
	String staticQRString = "NotFound";
	

	String blankBillNo = "";
	try
	{

	    String benowURL = "https://mobilepayments.benow.in/merchants/merchant/getDynamicQRString";

	    JSONObject jObjInnerPayLoad = new JSONObject();
	    jObjInnerPayLoad.put("merchantCode", clsGlobalVarClass.gBenowMerchantCode);//from property setup
	    jObjInnerPayLoad.put("amount", 0.00);
	    jObjInnerPayLoad.put("refNumber", blankBillNo);
	    jObjInnerPayLoad.put("paymentMethod", "UPI");
	    jObjInnerPayLoad.put("remarks", "Payment for static QR code");
	    // salt from proprty setup "abcd"
	    String encString = encrypt(clsGlobalVarClass.gBenowSalt, jObjInnerPayLoad.toString());

	    JSONObject jObjPayLoad = new JSONObject();
	    jObjPayLoad.put("encryptedString", encString);
	    jObjPayLoad.put("jsonString", jObjInnerPayLoad.toString());

	    System.out.println(jObjPayLoad.toJSONString());

	    URL url = new URL(benowURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    //get value from property setup for authentication key and xemail
	    conn.setRequestProperty("AuthorizationKey", clsGlobalVarClass.gBenowAuthenticationKey);
	    conn.setRequestProperty("X-EMAIL", clsGlobalVarClass.gBenowXEmail);

	    OutputStream os = conn.getOutputStream();
	    os.write(jObjPayLoad.toJSONString().getBytes());
	    os.flush();
	    /*if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED)
            {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }*/
	    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	    String output = "", op = "";

	    while ((output = br.readLine()) != null)
	    {
		op += output;
	    }
	    System.out.println("Response=" + op);
	    staticQRString = op;
	    conn.disconnect();

	    try
	    {
		String charset = "UTF-8"; // or "ISO-8859-1"
		Map hintMap = new HashMap();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

		createQRCode(staticQRString, filePath, charset, hintMap, 135, 135);

		System.out.println("Static QR Code image created successfully!");
		System.out.println("Data read from Static QR Code: " + readQRCode(filePath, charset, hintMap));

		//funCopyImageIfPresent(new File(filePath));
	    }
	    catch (Exception e)
	    {
		filePath = "Error";
		e.printStackTrace();
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return filePath;
    }

    public void funSendPaymenetLinkSMS(String billNo, double billAmount, String customerCode)
    {
	try
	{

	    

	    String sql = "select a.strCustomerCode,a.strCustomerName,a.longMobileNo from tblcustomermaster a where  a.strCustomerCode = '" + customerCode + "' ";
	    ResultSet rsCustomer = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsCustomer.next())
	    {
		String customerName = rsCustomer.getString(2);
		String mobileNo = rsCustomer.getString(3);

		String benowURL = "https://mobilepayments.benow.in/payments/paymentadapter/portablePaymentRequest";

		JSONObject jObjInnerPayLoad = new JSONObject();
		jObjInnerPayLoad.put("merchantCode", clsGlobalVarClass.gBenowMerchantCode);//from property setup
		jObjInnerPayLoad.put("customerName", customerName);
		jObjInnerPayLoad.put("mobileNumber", mobileNo);
		jObjInnerPayLoad.put("description", "Payment for " + billNo + " with bill amount " + billAmount);
		jObjInnerPayLoad.put("amount", billAmount);
		jObjInnerPayLoad.put("expiryDate", "");
		jObjInnerPayLoad.put("refNumber", billNo);
		jObjInnerPayLoad.put("till", "");

		// salt from proprty setup "abcd"
		String encString = encrypt(clsGlobalVarClass.gBenowSalt, jObjInnerPayLoad.toString());

		JSONObject jObjPayLoad = new JSONObject();
		jObjPayLoad.put("encryptedString", encString);
		jObjPayLoad.put("jsonString", jObjInnerPayLoad.toString());

		System.out.println(jObjPayLoad.toJSONString());

		URL url = new URL(benowURL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		//get value from property setup for authentication key and xemail
		conn.setRequestProperty("AuthorizationKey", clsGlobalVarClass.gBenowAuthenticationKey);
		conn.setRequestProperty("X-EMAIL", clsGlobalVarClass.gBenowXEmail);

		OutputStream os = conn.getOutputStream();
		os.write(jObjPayLoad.toJSONString().getBytes());
		os.flush();
		/*if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED)
            {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }*/
		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		String output = "", op = "";

		while ((output = br.readLine()) != null)
		{
		    op += output;
		}
		System.out.println("Response=" + op);
		String response = op;
		conn.disconnect();

		/**
		 * to send link from Sanguine SMS provider uncomment it
		 */
		
//		ArrayList<String> mobileNoList = new ArrayList<>();
//		mobileNoList.add(mobileNo);
//
//		StringBuilder smsData = new StringBuilder();
//		JSONParser parser = new JSONParser();
//		JSONObject jsonResponse = (JSONObject) parser.parse(response);
//
//		if (!jsonResponse.get("url").toString().isEmpty())
//		{
//		    smsData.append("Please click on link to complete your payment against bill no " + jsonResponse.get("refNumber").toString());
//		    smsData.append("Amount " + jsonResponse.get("amount").toString());
//		    smsData.append("Payment Req Number " + jsonResponse.get("paymentReqNumber").toString());
//		    smsData.append(jsonResponse.get("url").toString());
//		}
//
//		if (smsData.length() > 0)
//		{
//		    clsSMSSender objSMSSender = new clsSMSSender(mobileNoList, smsData.toString());
//		    objSMSSender.start();
//		}
	    }
	    rsCustomer.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }
    
    
    public List<clsUPIBean> funGetLastUPITransactionLIst()
    {
	List<clsUPIBean>listOfLastUPITransactions=new ArrayList<clsUPIBean>();
	
	try
	{
	    String billNo="";//for last 3 transactions
	    String benowURL = "https://mobilepayments.benow.in/payments/ecomm/getMerchantTransactionStatus";

	    JSONObject jObjInnerPayLoad = new JSONObject();
	    jObjInnerPayLoad.put("merchantCode", clsGlobalVarClass.gBenowMerchantCode);//from property setup
	    jObjInnerPayLoad.put("refNumber", billNo);
	    // salt from proprty setup "abcd"
	    String encString = encrypt(clsGlobalVarClass.gBenowSalt, jObjInnerPayLoad.toString());

	    JSONObject jObjPayLoad = new JSONObject();
	    jObjPayLoad.put("encryptedString", encString);
	    jObjPayLoad.put("jsonString", jObjInnerPayLoad.toString());

	    System.out.println(jObjPayLoad.toJSONString());

	    URL url = new URL(benowURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    //get value from property setup for authentication key and xemail
	    conn.setRequestProperty("AuthorizationKey", clsGlobalVarClass.gBenowAuthenticationKey);
	    conn.setRequestProperty("X-EMAIL", clsGlobalVarClass.gBenowXEmail);

	    OutputStream os = conn.getOutputStream();
	    os.write(jObjPayLoad.toJSONString().getBytes());
	    os.flush();
	    /*if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED)
            {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }*/
	    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	    String output = "", op = "";

	    while ((output = br.readLine()) != null)
	    {
		op += output;
	    }
	    System.out.println("Response=" + op);

	    JSONParser parser = new JSONParser();
	    JSONArray jSONArray = (JSONArray) parser.parse(op);
	    
	    
	    for(int i=0;i<jSONArray.size();i++)
	    {
		clsUPIBean objUPIBean=new clsUPIBean();
		
		JSONObject objJSON=(JSONObject)jSONArray.get(i);
		
		objUPIBean.setStrMerchantCode(objJSON.get("merchantCode").toString());
		objUPIBean.setStrReferenceNo(objJSON.get("refNumber").toString());
		objUPIBean.setStrPaymentStatus(objJSON.get("paymentStatus").toString());
		objUPIBean.setStrTransactionId(objJSON.get("txnId").toString());
		objUPIBean.setSttTransactionDate(objJSON.get("transactionDate").toString());		
		objUPIBean.setStrCustomerRefNo(objJSON.get("custRefNumber").toString());
		if(objJSON.get("isStatic")==null)
		{
		    objUPIBean.setIsStatic("NULL");
		}
		else
		{
		    objUPIBean.setIsStatic(objJSON.get("isStatic").toString());
		}
		objUPIBean.setPayer(objJSON.get("payer").toString());
		objUPIBean.setPaymentMethodType(objJSON.get("paymentMethodType").toString());
		objUPIBean.setDblAmount(objJSON.get("amount").toString());
		
		
		listOfLastUPITransactions.add(objUPIBean);
	    }
	    
	}
	catch(Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    return listOfLastUPITransactions;
	}
	
    }
}
