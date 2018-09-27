/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSGlobal.controller;

import com.POSGlobal.view.frmShowTextFile;
import com.POSGlobal.view.frmUserAuthenticationPopUp;
import com.POSPrinting.clsVoidBillAuditingGenerator;
import com.sanguine.forms.frmConfigSettings;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
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
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.PrinterName;
import javax.print.event.PrintJobEvent;
import javax.print.event.PrintJobListener;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;

/**
 *
 * @author ajjim
 */
public class clsUtility2
{

    public clsUtility2()
    {

    }

    public String funGetBillType(String billNo)
    {
	String billType = "FOOD";
	try
	{
	    String sql = "select b.strItemCode,b.strItemName,d.strGroupName "
		    + "from tblbilldtl a,tblitemmaster b,tblsubgrouphd c,tblgrouphd d "
		    + "where a.strItemCode=b.strItemCode "
		    + "and b.strSubGroupCode=c.strSubGroupCode "
		    + "and c.strGroupCode=d.strGroupCode "
		    + "and a.strBillNo='" + billNo + "' "
		    + "and date(a.dteBillDate)='" + clsGlobalVarClass.getOnlyPOSDateForTransaction() + "' "
		    + "limit 1";
	    ResultSet rsBillType = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsBillType.next())
	    {
		billType = rsBillType.getString(3);
	    }
	    rsBillType.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    return billType;
	}
    }

    public String funGetNextSettleBill(String hdBillNo)
    {
	String nextBillNo = "";
	String[] dtlBillNos = null;
	try
	{
	    String sql = "select a.strPOSCode,a.strHdBillNo,a.strDtlBillNos "
		    + "from tblbillseriesbilldtl a  "
		    + "where a.strHdBillNo='" + hdBillNo + "' "
		    + "and date(a.dteBillDate)='" + clsGlobalVarClass.getOnlyPOSDateForTransaction() + "' ";
	    ResultSet rsBillType = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsBillType.next())
	    {
		dtlBillNos = rsBillType.getString(3).split(",");
	    }
	    rsBillType.close();

	    if (dtlBillNos != null)
	    {
		for (int i = 0; i < dtlBillNos.length; i++)
		{
		    String newBillNo = dtlBillNos[i];

		    sql = "select a.strBillNo,a.strSettlementCode "
			    + "from tblbillsettlementdtl a "
			    + "where a.strBillNo='" + newBillNo + "' ";
		    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		    if (rs.next())
		    {
			continue;
		    }
		    else
		    {
			nextBillNo = newBillNo;
			break;
		    }
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    return nextBillNo;
	}
    }

    public String funGetTableName(String tableNo)
    {
	String tableName = "";
	try
	{
	    ResultSet rsTableName = clsGlobalVarClass.dbMysql.executeResultSet("select strTableName from tbltablemaster where strTableNo='" + tableNo + "'; ");
	    if (rsTableName.next())
	    {
		tableName = rsTableName.getString(1);
	    }
	    rsTableName.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    return tableName;
	}
    }

    public String funGetWaiterShortName(String waiterNo)
    {
	String waiterShortName = "";
	try
	{
	    ResultSet rsWaiterShortName = clsGlobalVarClass.dbMysql.executeResultSet("select strWShortName from tblwaitermaster where strWaiterNo='" + waiterNo + "';  ");
	    if (rsWaiterShortName.next())
	    {
		waiterShortName = rsWaiterShortName.getString(1);
	    }
	    rsWaiterShortName.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    return waiterShortName;
	}
    }

    public String funGetWaiterFullName(String waiterNo)
    {
	String waiterFullName = "";
	try
	{
	    ResultSet rsWaiterShortName = clsGlobalVarClass.dbMysql.executeResultSet("select strWFullName from tblwaitermaster where strWaiterNo='" + waiterNo + "';  ");
	    if (rsWaiterShortName.next())
	    {
		waiterFullName = rsWaiterShortName.getString(1);
	    }
	    rsWaiterShortName.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    return waiterFullName;
	}
    }

    public void funCreateTempFolder()
    {
	try
	{
	    String filePath = System.getProperty("user.dir");
	    File Text_KOT = new File(filePath + "/Temp");
	    if (!Text_KOT.exists())
	    {
		Text_KOT.mkdirs();
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public void funPrintBlankSpace(String printWord, BufferedWriter BWOut)
    {
	try
	{
	    int wordSize = printWord.length();
	    int actualPrintingSize = clsGlobalVarClass.gColumnSize;
	    int availableBlankSpace = actualPrintingSize - wordSize;

	    int leftSideSpace = availableBlankSpace / 2;
	    if (leftSideSpace > 0)
	    {
		for (int i = 0; i < leftSideSpace; i++)
		{
		    BWOut.write(" ");
		}
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public void funWriteTextWithBlankLines(String text, int len, BufferedWriter out) throws Exception
    {
	int remLen = len - text.trim().length();
	out.write(text);
	for (int cn = 0; cn < remLen; cn++)
	{
	    out.write(" ");
	}
    }

    public void funWriteToTextMemberNameForFormat5(BufferedWriter out, String memberName, String format)
    {
	try
	{
	    int counter = 0;
	    counter = counter + 2;
	    //Item Write 
	    String tempItemName = memberName;
	    int length = tempItemName.length();
	    if (length < 25)
	    {
		out.write(tempItemName);
		counter = counter + length;
	    }
	    else
	    {
		String partOne = tempItemName.substring(0, 24);
		out.write(partOne);
		counter = counter + partOne.length();
		String partTwo = tempItemName.substring(24, length - 1);
		out.newLine();
		out.write("                " + partTwo);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public int funPrintContentWithSpace(String align, String textToPrint, int totalLength, BufferedWriter pw) throws Exception
    {
	int len = totalLength - textToPrint.trim().length();
	if (align.equalsIgnoreCase("Left"))
	{
	    pw.write(textToPrint.trim());
	    for (int cnt = 0; cnt < len; cnt++)
	    {
		pw.write(" ");
	    }
	}
	else if (align.equalsIgnoreCase("Right"))
	{
	    for (int cnt = 0; cnt < len; cnt++)
	    {
		pw.write(" ");
	    }
	    pw.write(textToPrint.trim());
	}

	return 1;
    }

    public void funWriteToTextformat5(BufferedWriter out, String qyt, String ItemName, String Amount, String format)
    {
	try
	{
	    int counter = 0;
	    out.write("  ");
	    counter = counter + 2;
	    //Qty write 
	    String tempQty = qyt;
	    int length = tempQty.length();

	    switch (length)
	    {
		case 4:
		    out.write("   " + tempQty);//3space
		    counter = counter + 3 + length;
		    break;

		case 5:
		    out.write(" " + tempQty);//2space
		    counter = counter + 1 + length;
		    break;

		case 6:
		    out.write(" " + tempQty);//1space
		    counter = counter + 1 + length;
		    break;

		case 7:
		    out.write(tempQty);
		    counter = counter + length;
		    break;
	    }

	    //End of Qty write 
	    out.write("  ");
	    counter = counter + 2;

	    //Item Write 
	    String tempItemName = ItemName;
	    length = tempItemName.length();
	    if (length < 19)
	    {
		out.write(tempItemName);
		counter = counter + length;
		funWriteFormattedAmt(counter, Amount, out, format);
	    }
	    else
	    {
		String partOne = tempItemName.substring(0, 18);
		out.write(partOne);
		counter = counter + partOne.length();
		String partTwo = tempItemName.substring(19, length - 1);
		funWriteFormattedAmt(counter, Amount, out, format);
		out.newLine();
		out.write("           " + partTwo);
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public void funWriteFormattedAmt(int counter, String Amount, BufferedWriter out, String format)
    {
	try
	{
	    int space = 30;
	    if (format.equals("Format3"))
	    {
		space = 29;
	    }
	    if (format.equals("Format4"))
	    {
		space = 34;
	    }
	    if (format.equals("Format5"))
	    {
		space = 29;
	    }
	    if (format.equals("Format6"))
	    {
		space = 30;
	    }
	    if (format.equals("Format11"))
	    {
		space = 12;
	    }
	    if (format.equals("Format13"))
	    {
		space = 29;
	    }
	    int usedSpace = space - counter;
	    for (int i = 0; i < usedSpace; i++)
	    {
		out.write(" ");
	    }
	    out.write("  ");
	    String tempAmount = Amount;

	    int length = tempAmount.length();
	    switch (length)
	    {
		case 1:
		    out.write("        " + tempAmount);//8
		    break;
		case 2:
		    out.write("       " + tempAmount);//7
		    break;
		case 3:
		    out.write("      " + tempAmount);//6
		    break;
		case 4:
		    out.write("     " + tempAmount);//5
		    break;
		case 5:
		    out.write("    " + tempAmount);//4
		    break;
		case 6:
		    out.write("   " + tempAmount);//3
		    break;
		case 7:
		    out.write("  " + tempAmount);//2
		    break;
		case 8:
		    out.write(" " + tempAmount);//1
		    break;
		case 9:
		    out.write(tempAmount);//0
		    break;
		default:
		    out.write(tempAmount);//0
		    break;
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public void funShowTextFile(File file, String formName, String printerInfo)
    {
	try
	{
	    String data = "";
	    FileReader fread = new FileReader(file);
	    //BufferedReader KOTIn = new BufferedReader(fread);
	    FileInputStream fis = new FileInputStream(file);
	    BufferedReader KOTIn = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
	    String line = "";
	    while ((line = KOTIn.readLine()) != null)
	    {
		data = data + line + "\n";
	    }
	    String fileName = file.getName();
	    String name = "";
	    if (formName.trim().length() > 0)
	    {
		name = formName;
	    }
	    if ("Temp_DayEndReport.txt".equalsIgnoreCase(fileName))
	    {
		name = "DayEnd";
	    }
	    new frmShowTextFile(data, name, file, printerInfo).setVisible(true);
	    fread.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    /*
     * this is a customise function to calculate roundoff amount to X amount
     *
     * hash map returns roundoff amount and roundoff by amount
     */
    public Map funCalculateRoundOffAmount(double settlementAmt)
    {
	Map<String, Double> hm = new HashMap<>();

	double roundOffTo = clsGlobalVarClass.gRoundOffTo;

	if (roundOffTo == 0.00)
	{
	    roundOffTo = 1.00;
	}

	double roundOffSettleAmt = settlementAmt;
	double remainderAmt = (settlementAmt % roundOffTo);
	double roundOffToBy2 = roundOffTo / 2;
	double x = 0.00;

	if (remainderAmt <= roundOffToBy2)
	{
	    x = (-1) * remainderAmt;

	    roundOffSettleAmt = (Math.floor(settlementAmt / roundOffTo) * roundOffTo);

	    //System.out.println(settleAmt + " " + roundOffSettleAmt + " " + x);
	}
	else
	{
	    x = roundOffTo - remainderAmt;

	    roundOffSettleAmt = (Math.ceil(settlementAmt / roundOffTo) * roundOffTo);

	    // System.out.println(settleAmt + " " + roundOffSettleAmt + " " + x);
	}

	hm.put("roundOffAmt", roundOffSettleAmt);
	hm.put("roundOffByAmt", x);

	System.out.println("Original Settl Amt=" + settlementAmt + " RoundOff Settle Amt=" + roundOffSettleAmt + " RoundOff To=" + roundOffTo + " RoundOff By=" + x);

	return hm;

    }

    /*
     * this is a built in function to calculate roundoff amount to X amount
     *
     * hash map returns roundoff amount and roundoff by amount
     */
    public Map funCalculateRoundOffAmountByBuiltIn(double settlementAmt)
    {
	Map<String, Double> hm = new HashMap();
	double roundOffSettleAmt = settlementAmt;
	double roundOffTo = 5.0;

	double deic = (settlementAmt % roundOffTo);
	if (deic <= (roundOffTo / 2.0))
	{
	    roundOffSettleAmt = (Math.floor(settlementAmt / roundOffTo) * roundOffTo);
	}
	else
	{
	    roundOffSettleAmt = (Math.ceil(settlementAmt / roundOffTo) * roundOffTo);
	}

	hm.put("roundOffAmt", roundOffSettleAmt);

	return hm;

    }

    public void funSendDBBackupAndErrorLogFolder(String backupPath)
    {

	try
	{
	    boolean isValidPath = funCheckBackUpFilePath();
	    if (!isValidPath)
	    {
		return;
	    }

	    Date dtCurrentDate = new Date();
	    String date = dtCurrentDate.getDate() + "-" + (dtCurrentDate.getMonth() + 1) + "-" + (dtCurrentDate.getYear() + 1900);
	    String time = dtCurrentDate.getHours() + "-" + dtCurrentDate.getMinutes();
	    String fileName = date + "_" + time + "_JPOS";

	    String batchFilePath = System.getProperty("user.dir") + "\\mysqldbbackup.bat";
	    String filePath = backupPath;
	    File file = new File(filePath);
	    if (!file.exists())
	    {
		file.mkdir();
	    }

	    File batchFile = new File(batchFilePath);
	    if (!batchFile.exists())
	    {
		batchFile.createNewFile();
	    }
	    BufferedWriter objWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(batchFile), "UTF8"));
	    objWriter.write("@echo off");
	    objWriter.newLine();
	    objWriter.write("for /f \"tokens=1\" %%i in ('date /t') do set DATE_DOW=%%i");
	    objWriter.newLine();
	    objWriter.write("for /f \"tokens=2\" %%i in ('date /t') do set DATE_DAY=%%i");
	    objWriter.newLine();
	    objWriter.write("for /f %%i in ('echo %date_day:/=-%') do set DATE_DAY=%%i");
	    objWriter.newLine();

	    objWriter.write("for /f %%i in ('time /t') do set DATE_TIME=%%i");
	    objWriter.newLine();
	    objWriter.write("for /f %%i in ('echo %date_time::=-%') do set DATE_TIME=%%i");
	    objWriter.newLine();

	    String fileFullName = filePath + "\\" + fileName + ".sql";

	    //objWriter.write(clsPosConfigFile.gDatabaseBackupFilePath+" -u root -proot jpos>"+"\""+filePath+"/%DATE_DAY%_%DATE_TIME%_JPOS.sql\" ");
//            objWriter.write(clsPosConfigFile.gDatabaseBackupFilePath + " -u " + clsPosConfigFile.userId + " -p" + clsPosConfigFile.password + " -h " + clsPosConfigFile.ipAddress + " " + clsPosConfigFile.databaseName + ">" + "\"" + filePath + "/" + fileName + ".sql\" ");
//            System.out.println(clsPosConfigFile.gDatabaseBackupFilePath + " -u " + clsPosConfigFile.userId + " -p" + clsPosConfigFile.password + " -h " + clsPosConfigFile.ipAddress + " " + clsPosConfigFile.databaseName + ">" + "\"" + filePath + "/" + fileName + ".sql\" ");
	    objWriter.write(clsPosConfigFile.gDatabaseBackupFilePath + " --hex-blob " + " -u " + clsPosConfigFile.userId + " -p" + clsPosConfigFile.password + " -h " + clsPosConfigFile.ipAddress + " --default-character-set=utf8 --max_allowed_packet=64M --add-drop-table --skip-add-locks --skip-comments --add-drop-database --databases " + " " + clsPosConfigFile.databaseName + ">" + "\"" + fileFullName + "\" ");
	    System.out.println(clsPosConfigFile.gDatabaseBackupFilePath + " --hex-blob " + " -u " + clsPosConfigFile.userId + " -p" + clsPosConfigFile.password + " -h " + clsPosConfigFile.ipAddress + " --default-character-set=utf8 --max_allowed_packet=64M --add-drop-table --skip-add-locks --skip-comments --add-drop-database --databases " + " " + clsPosConfigFile.databaseName + ">" + "\"" + fileFullName + "\" ");

	    System.out.println(fileFullName);

	    objWriter.flush();
	    objWriter.close();

	    try
	    {
		Process p = Runtime.getRuntime().exec("cmd /c " + "\"" + batchFilePath + "\"");
		InputStream is = p.getInputStream();
		int i = 0;
		while ((i = is.read()) != -1)
		{
		    System.out.print((char) i);
		}
	    }
	    catch (IOException ioException)
	    {
		System.out.println(ioException.getMessage());
	    }

	    //mailed logic
	    int ret = 0;
	    //String to="ingaleprashant8@gmail.com";//change accordingly
	    String to = "sanguineauditing@gmail.com";//change accordingly
	    //Get the session object
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
		    //return new PasswordAuthentication("paritoshkumar112@gmail.com","singhparitosh123");//change accordingly  
		    return new PasswordAuthentication(clsGlobalVarClass.gSenderEmailId, clsGlobalVarClass.gSenderMailPassword);//change accordingly
		}
	    });
	    MimeMessage message = new MimeMessage(session);
	    //message.setFrom(new InternetAddress("paritoshkumar112@gmail.com"));//change accordingly
	    message.setFrom(new InternetAddress(clsGlobalVarClass.gSenderEmailId));//change accordingly
	    String[] arrRecipient = to.split(",");

	    if (to.trim().length() > 0)
	    {
		for (int cnt = 0; cnt < arrRecipient.length; cnt++)
		{
		    System.out.println(arrRecipient[cnt]);
		    message.addRecipient(Message.RecipientType.TO, new InternetAddress(arrRecipient[cnt]));
		}
	    }

	    message.setSubject("DB Backup And Error Log Folder Of '" + clsGlobalVarClass.gClientCode + "' '" + clsGlobalVarClass.gClientName + "' ");

	    String msgBody = "DB Backup And Error Log Folder Of '" + clsGlobalVarClass.gClientCode + "' '" + clsGlobalVarClass.gClientName + "' "
		    + " POS:-" + clsGlobalVarClass.gPOSName + " POS Date:-" + clsGlobalVarClass.gPOSDateForTransaction + " User:-" + clsGlobalVarClass.gUserCode;
	    //message.setText(msgBody);

	    // Create the message part 
	    BodyPart messageBodyPart = new MimeBodyPart();

	    File dbBackupFile = new File(fileFullName);
	    DataSource source = new FileDataSource(dbBackupFile);

	    // Fill the message
	    messageBodyPart.setText(msgBody);

	    Multipart multipart = new MimeMultipart();

	    // Set text message part
	    multipart.addBodyPart(messageBodyPart);

	    // Part two is attachment
	    messageBodyPart = new MimeBodyPart();
	    messageBodyPart.setDataHandler(new DataHandler(source));
	    messageBodyPart.setFileName(fileFullName);
	    multipart.addBodyPart(messageBodyPart);

	    //add error log folder
	    String rentDirectory = System.getProperty("user.dir");
	    File errorLogFolder = new File(rentDirectory + "/ErrorLogs");
	    if (errorLogFolder.exists())
	    {
		File[] filesPath = errorLogFolder.listFiles();
		for (int i = 0; i < filesPath.length; i++)
		{
		    messageBodyPart = new MimeBodyPart();
		    source = new FileDataSource(filesPath[i]);
		    messageBodyPart.setDataHandler(new DataHandler(source));
		    messageBodyPart.setFileName(filesPath[i].getAbsolutePath());
		    multipart.addBodyPart(messageBodyPart);
		}
	    }

	    // Send the complete message parts
	    message.setContent(multipart);

	    if (to.length() > 0)
	    {
		//send message  
		Transport.send(message);
		System.out.println("message sent successfully");
	    }
	    else
	    {
		System.out.println("Email has No Recipient");
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}

    }

    private boolean funCheckBackUpFilePath()
    {
	boolean isValidPath = true;
	try
	{
	    String p = clsPosConfigFile.gDatabaseBackupFilePath.replaceAll("\"", "");
	    File f = new File(p);
	    if (!f.getParentFile().exists())
	    {
		isValidPath = false;
		JOptionPane.showMessageDialog(null, "Invalid MySQL File Path!!!\nPlease Check DBConfig File.");
		//System.exit(0);
	    }
	    else if (!f.getPath().split("bin")[1].equals("\\mysqldump"))
	    {
		isValidPath = false;
		JOptionPane.showMessageDialog(null, "Invalid MySQL File Path!!!\nPlease Check DBConfig File.");
		//System.exit(0);
	    }
	}
	catch (Exception e)
	{
	    new clsUtility().funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(null, "Invalid MySQL File Path!!!\nPlease Check DBConfig File.");
	    e.printStackTrace();
	    isValidPath = false;
	    //System.exit(0);
	}

	return isValidPath;
    }

    public void funSendDBBackupAndErrorLogFileOnDayEnd(File errorLogFile, File dbBackupFile)
    {

	try
	{

	    //mailed logic
	    int ret = 0;
	    final String from = "sanguineauditing@gmail.com";//change accordingly
	    String to = "sanguineauditing@gmail.com";//change accordingly
	    //Get the session object
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
		    //return new PasswordAuthentication("paritoshkumar112@gmail.com","singhparitosh123");//change accordingly  
		    return new PasswordAuthentication(from, "Sanguine@2017");//change accordingly
		}
	    });
	    MimeMessage message = new MimeMessage(session);
	    //message.setFrom(new InternetAddress("paritoshkumar112@gmail.com"));//change accordingly
	    message.setFrom(new InternetAddress(from));//change accordingly
	    if (clsGlobalVarClass.gSendDBBackUpOnClientMail)
	    {
		to = to + "," + clsGlobalVarClass.gDBBackupReceiverEmailIds;
	    }

	    String[] arrRecipient = to.split(",");

	    if (to.trim().length() > 0)
	    {
		for (int cnt = 0; cnt < arrRecipient.length; cnt++)
		{
		    System.out.println(arrRecipient[cnt]);
		    message.addRecipient(Message.RecipientType.TO, new InternetAddress(arrRecipient[cnt]));
		}
	    }

	    message.setSubject("DB Backup and Error Log File Of '" + clsGlobalVarClass.gClientCode + "' '" + clsGlobalVarClass.gClientName + "' " + clsGlobalVarClass.gPOSDateForTransaction);

	    String msgBody = "Error Log File Of '" + clsGlobalVarClass.gClientCode + "' '" + clsGlobalVarClass.gClientName + "' "
		    + " POS:-" + clsGlobalVarClass.gPOSName + " POS Date:-" + clsGlobalVarClass.gPOSDateForTransaction + " User:-" + clsGlobalVarClass.gUserCode;
	    //message.setText(msgBody);

	    // Create the message part 
	    BodyPart messageBodyPart = new MimeBodyPart();

	    // Fill the message
	    messageBodyPart.setText(msgBody);

	    Multipart multipart = new MimeMultipart();
	    // Set text message part
	    multipart.addBodyPart(messageBodyPart);

	    if (errorLogFile.exists())
	    {
		DataSource errorLogSource = new FileDataSource(errorLogFile);
		// Part two is attachment
		messageBodyPart = new MimeBodyPart();
		messageBodyPart.setDataHandler(new DataHandler(errorLogSource));
		messageBodyPart.setFileName(errorLogFile.getName());
		multipart.addBodyPart(messageBodyPart);
	    }

	    if (dbBackupFile.exists())
	    {
		FileOutputStream fos = null;
		ZipOutputStream zipOut = null;
		FileInputStream fis = null;
		String destZIPFile = System.getProperty("user.dir") + "\\DBBackup\\" + dbBackupFile.getName() + ".zip";
		//clsPosConfigFile.dbBackupPath+File.pathSeparator+dbBackupFile.getName()+".zip";

		try
		{
		    fos = new FileOutputStream(destZIPFile);
		    zipOut = new ZipOutputStream(new BufferedOutputStream(fos));
		    File input = dbBackupFile;
		    fis = new FileInputStream(input);
		    ZipEntry ze = new ZipEntry(input.getName());
		    System.out.println("Zipping the file: " + input.getName());
		    zipOut.putNextEntry(ze);
		    byte[] tmp = new byte[4 * 1024];
		    int size = 0;
		    while ((size = fis.read(tmp)) != -1)
		    {
			zipOut.write(tmp, 0, size);
		    }
		    zipOut.flush();
		    zipOut.close();
		}
		catch (FileNotFoundException e)
		{
		    e.printStackTrace();
		}
		catch (IOException e)
		{
		    e.printStackTrace();
		}
		finally
		{
		    try
		    {
			if (fos != null)
			{
			    fos.close();
			}
			if (fis != null)
			{
			    fis.close();
			}
		    }
		    catch (Exception ex)
		    {

		    }
		}
		File destZipFile = new File(destZIPFile);
		if (destZipFile.exists())
		{
		    DataSource dbBackupSource = new FileDataSource(destZipFile);
		    messageBodyPart = new MimeBodyPart();
		    messageBodyPart.setDataHandler(new DataHandler(dbBackupSource));
		    messageBodyPart.setFileName(dbBackupSource.getName());
		    multipart.addBodyPart(messageBodyPart);
		}
	    }

	    // Send the complete message parts
	    message.setContent(multipart);

	    if (to.length() > 0)
	    {
		//send message  
		Transport.send(message);
		System.out.println("message sent successfully");
	    }
	    else
	    {
		System.out.println("Email has No Recipient");
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}

    }

    public void funPrintToPrinter(String primaryPrinterName, String secPrinterName, String fileType, String printOnBothPrinters, boolean isReprint)
    {
	try
	{
	    String filePath = System.getProperty("user.dir");
	    String reportname = "";
	    String fileName = "";
	    if (fileType.equalsIgnoreCase("kot") || fileType.equalsIgnoreCase("checkkot"))
	    {
		fileName = filePath + "/Temp/Temp_KOT.txt";
		//fileName = "Temp/Temp_KOT.rtf";
	    }
	    else if (fileType.equalsIgnoreCase("dayend"))
	    {
		fileName = filePath + "/Temp/Temp_DayEndReport.txt";
		reportname = "dayend";
	    }
	    else if (fileType.equalsIgnoreCase("Adv Receipt"))
	    {
		reportname = "Adv Receipt";
	    }
	    else if (fileType.equalsIgnoreCase("ItemWiseKOT"))
	    {
		fileName = filePath + "/Temp/" + fileName + ".txt";
	    }
	    else if (fileType.equalsIgnoreCase("MoveTable"))//move Table
	    {
		fileName = filePath + "/Temp/MoveTable.txt";
	    }
	    else if (fileType.equalsIgnoreCase("MoveKOT"))//move KOT
	    {
		fileName = filePath + "/Temp/MoveKOT.txt";
	    }
	    else if (fileType.equalsIgnoreCase("MoveKOTItems"))//move KOT items
	    {
		fileName = filePath + "/Temp/MoveKOTItems.txt";
	    }
	    else
	    {
		fileName = filePath + "/Temp/Temp_Bill.txt";
		reportname = "bill";
	    }

	    if ("windows".equalsIgnoreCase(clsPosConfigFile.gPrintOS))//&& clsGlobalVarClass.gPrintType.equalsIgnoreCase("Text File")
	    {
		if (fileType.equalsIgnoreCase("kot"))
		{
		    //System.out.println("G Print YN="+clsGlobalVarClass.gPrintKOTYN);
		    if (clsGlobalVarClass.gPrintKOTYN)
		    {
			funPrintFile(primaryPrinterName, secPrinterName, printOnBothPrinters, fileName);
			if (clsGlobalVarClass.gMultipleKOTPrint)
			{
			    if (!isReprint)
			    {
				funAppendDuplicate(fileName);
			    }
			    funPrintFile(primaryPrinterName, secPrinterName, printOnBothPrinters, fileName);
			}
		    }
		}
		else if (fileType.equalsIgnoreCase("checkkot"))
		{
		    String billPrinterName = clsGlobalVarClass.gBillPrintPrinterPort;
		    primaryPrinterName = billPrinterName;
		    funPrintFile(primaryPrinterName, "", "", fileName);
		}
		else if (fileType.equalsIgnoreCase("ItemWiseKOT"))
		{
		    String billPrinterName = clsGlobalVarClass.gBillPrintPrinterPort;
		    primaryPrinterName = billPrinterName;
		    funPrintFile(primaryPrinterName, secPrinterName, printOnBothPrinters, fileName);
		}
		else if (fileType.equalsIgnoreCase("MoveTable"))
		{
		    //System.out.println("G Print YN="+clsGlobalVarClass.gPrintKOTYN);
		    if (clsGlobalVarClass.gPrintKOTYN)
		    {
			funPrintFile(primaryPrinterName, secPrinterName, printOnBothPrinters, fileName);
		    }
		}
		else if (fileType.equalsIgnoreCase("MoveKOT"))
		{
		    //System.out.println("G Print YN="+clsGlobalVarClass.gPrintKOTYN);
		    if (clsGlobalVarClass.gPrintKOTYN)
		    {
			funPrintFile(primaryPrinterName, secPrinterName, printOnBothPrinters, fileName);
		    }
		}
		else if (fileType.equalsIgnoreCase("MoveKOTItems"))
		{
		    //System.out.println("G Print YN="+clsGlobalVarClass.gPrintKOTYN);
		    if (clsGlobalVarClass.gPrintKOTYN)
		    {
			funPrintFile(primaryPrinterName, secPrinterName, printOnBothPrinters, fileName);
		    }
		}
		else
		{
		    funPrintBillWindows(reportname, fileName);
		    //Avoid Muliple Bill Printing
		    if (!fileType.equalsIgnoreCase("dayend"))
		    {
			if (clsGlobalVarClass.gMultiBillPrint)
			{
			    if (!isReprint)
			    {
				funAppendDuplicate(fileName);
			    }
			    funPrintBillWindows(reportname, fileName);
			}
		    }
		}
	    }
	    else if ("linux".equalsIgnoreCase(clsPosConfigFile.gPrintOS) && clsGlobalVarClass.gPrintType.equalsIgnoreCase("Text File"))
	    {
		if (fileType.equalsIgnoreCase("kot"))
		{
		    //System.out.println("G Print YN="+clsGlobalVarClass.gPrintKOTYN);
		    if (clsGlobalVarClass.gPrintKOTYN)
		    {
			Process process = Runtime.getRuntime().exec("lpr -P " + primaryPrinterName + " " + fileName, null);

			if (clsGlobalVarClass.gMultipleKOTPrint)
			{
			    if (!isReprint)
			    {
				funAppendDuplicate(fileName);
			    }
			    process = Runtime.getRuntime().exec("lpr -P " + primaryPrinterName + " " + fileName, null);
			}
		    }
		}
		else if (fileType.equalsIgnoreCase("checkkot"))
		{
		    Process process = Runtime.getRuntime().exec("lpr -P " + primaryPrinterName + " " + fileName, null);
		}
		else if (fileType.equalsIgnoreCase("ItemWiseKOT"))
		{
		    Process process = Runtime.getRuntime().exec("lpr -P " + primaryPrinterName + " " + fileName, null);
		}
		else
		{
		    //Process process = Runtime.getRuntime().exec("lpr -P " + primaryPrinterName + " " + fileName, null);
		    Process process = Runtime.getRuntime().exec("lpr -P " + clsGlobalVarClass.gBillPrintPrinterPort + " " + fileName, null);
		    if (!fileType.equalsIgnoreCase("dayend"))
		    {
			if (clsGlobalVarClass.gMultiBillPrint)
			{
			    if (!isReprint)
			    {
				funAppendDuplicate(fileName);
			    }
			    process = Runtime.getRuntime().exec("lpr -P " + clsGlobalVarClass.gBillPrintPrinterPort + " " + fileName, null);
			}
		    }
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public void funPrintFile(String primaryPrinterName, String secPrinterName, String printOnBothPrinters, String fileName)
    {

	try
	{
	    int printerIndex = 0;
	    String printerStatus = "Not Found";
	    System.out.println("Primary =" + primaryPrinterName);
	    System.out.println("Secondary =" + secPrinterName);
	    System.out.println("print On Both Printers =" + printOnBothPrinters);

	    PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
	    DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
	    primaryPrinterName = primaryPrinterName.replaceAll("#", "\\\\");
	    secPrinterName = secPrinterName.replaceAll("#", "\\\\");

	    PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
	    for (int i = 0; i < printService.length; i++)
	    {
		System.out.println("Service=" + printService[i].getName() + "\tPrim P=" + primaryPrinterName);
		String printerServiceName = printService[i].getName();

		if (primaryPrinterName.equalsIgnoreCase(printerServiceName))
		{
		    System.out.println("Printer Found=" + primaryPrinterName);
		    printerIndex = i;
		    printerStatus = "Found";
		    break;
		}
	    }

	    if (printerStatus.equals("Found"))
	    {
		DocPrintJob job = printService[printerIndex].createPrintJob();
		FileInputStream fis = new FileInputStream(fileName);
		DocAttributeSet das = new HashDocAttributeSet();
		Doc doc = new SimpleDoc(fis, flavor, das);
		job.print(doc, pras);
		String printerInfo = "";

		PrintServiceAttributeSet att = printService[printerIndex].getAttributes();
		for (Attribute a : att.toArray())
		{
		    String attributeName;
		    String attributeValue;
		    attributeName = a.getName();
		    attributeValue = att.get(a.getClass()).toString();
		    if (attributeName.trim().equalsIgnoreCase("queued-job-count"))
		    {
			clsGlobalVarClass.gPrinterQueueStatus = attributeValue;
			printerInfo = primaryPrinterName + "!" + attributeValue;
			//System.out.println(attributeName + " : " + attributeValue);
		    }
		}
		if (printOnBothPrinters.equals("Y"))
		{
		    funPrintOnSecPrinter(secPrinterName, fileName);
		}
	    }
	    else
	    {
		funPrintOnSecPrinter(secPrinterName, fileName);
		//JOptionPane.showMessageDialog(null,primaryPrinterName+" Printer Not Found");
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    if (clsGlobalVarClass.gShowPrinterErrorMsg)
	    {
		try
		{
		    funPrintOnSecPrinter(secPrinterName, fileName);
		}
		catch (Exception ex)
		{
		    JOptionPane.showMessageDialog(null, "Secondary Printer Error= " + ex.getMessage());
		}
		JOptionPane.showMessageDialog(null, e.getMessage(), "Error Code - TFG 01", JOptionPane.ERROR_MESSAGE);
	    }
	}
    }

    private void funPrintOnSecPrinter(String secPrinterName, String fileName) throws Exception
    {
	String printerStatus = "Not Found";
	PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
	DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
	PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
	int printerIndex = 0;
	for (int i = 0; i < printService.length; i++)
	{
	    System.out.println("Service=" + printService[i].getName() + "\tSec P=" + secPrinterName);
	    String printerServiceName = printService[i].getName();

	    if (secPrinterName.equalsIgnoreCase(printerServiceName))
	    {
		System.out.println("Sec Printer Found=" + secPrinterName);
		printerIndex = i;
		printerStatus = "Found";
		break;
	    }
	}
	if (printerStatus.equals("Found"))
	{
	    String printerInfo = "";
	    DocPrintJob job = printService[printerIndex].createPrintJob();
	    FileInputStream fis = new FileInputStream(fileName);
	    DocAttributeSet das = new HashDocAttributeSet();
	    Doc doc = new SimpleDoc(fis, flavor, das);
	    job.addPrintJobListener(new clsUtility2.MyPrintJobListener());
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
		    clsGlobalVarClass.gPrinterQueueStatus = attributeValue;
		    printerInfo = secPrinterName + "!" + attributeValue;
		}
		System.out.println(attributeName + " : " + attributeValue);
	    }
	    if (clsGlobalVarClass.gShowBill)
	    {
		funShowTextFile(new File(fileName), "", printerInfo);
	    }
	}
	else
	{
	    JOptionPane.showMessageDialog(null, secPrinterName + " Printer Not Found");
	}
    }

    public void funPrintBillForAuditing(String billno, String reprint, String transType, String billDate, String posCode, String viewORPrint)
    {
	clsVoidBillAuditingGenerator objAuditingGenerator = new clsVoidBillAuditingGenerator();
	objAuditingGenerator.funGenerateBill(billno, reprint, "sales report", transType, billDate, posCode, viewORPrint);
    }

    public boolean isCheckedInMembers(String posCode)
    {
	boolean isCheckedInMembers = false;
	try
	{
	    String sql = "select a.strRegisterCode,a.strPOSCode,a.strIn,a.strOut "
		    + "from tblregisterinoutplayzone a "
		    + "where a.strOut='N' "
		    + "and a.strPOSCode='" + posCode + "' ";
	    ResultSet rsCheckedInMembers = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsCheckedInMembers.next())
	    {
		isCheckedInMembers = true;
	    }
	    rsCheckedInMembers.close();

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    return isCheckedInMembers;
	}
    }

    class MyPrintJobListener implements PrintJobListener
    {

	public void printDataTransferCompleted(PrintJobEvent pje)
	{
	    System.out.println("printDataTransferCompleted");
	}

	public void printJobCanceled(PrintJobEvent pje)
	{
	    System.out.println("The print job was cancelled");
	}

	public void printJobCompleted(PrintJobEvent pje)
	{
	    System.out.println("The print job was completed");
	}

	public void printJobFailed(PrintJobEvent pje)
	{
	    System.out.println("The print job has failed");
	}

	public void printJobNoMoreEvents(PrintJobEvent pje)
	{
	}

	public void printJobRequiresAttention(PrintJobEvent pje)
	{
	}
    }

    private void funAppendDuplicate(String fileName)
    {
	try
	{
	    File fileKOTPrint = new File(fileName);
//            RandomAccessFile f = new RandomAccessFile(fileKOTPrint, "rw");
//            f.seek(0); // to the beginning                  
//            BufferedWriter KotOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileKOTPrint), "UTF8"));            
//            funPrintBlankSpace("[DUPLICATE]", KotOut);            
//            KotOut.write("[DUPLICATE]");              
//            KotOut.newLine();            
//            KotOut.close();
//            f.close();                                    

	    String filePath = System.getProperty("user.dir");
	    filePath += "/Temp/Temp_KOT2.txt";
	    File fileKOTPrint2 = new File(filePath);
	    BufferedWriter KotOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileKOTPrint2), "UTF8"));
	    funPrintBlankSpace("[DUPLICATE]", KotOut);
	    KotOut.write("[DUPLICATE]");
	    KotOut.newLine();

	    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileKOTPrint)));
	    String line = null;
	    while ((line = br.readLine()) != null)
	    {
		KotOut.write(line);
		KotOut.newLine();
	    }
	    br.close();
	    KotOut.close();

	    String content = new String(Files.readAllBytes(Paths.get(filePath)));
	    Files.write(Paths.get(fileName), content.getBytes(), StandardOpenOption.CREATE);

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    /**
     * printBillWindows() method print to Default Printer. No Parameter required
     */
    private void funPrintBillWindows(String type, String file)
    {
	try
	{
	    clsUtility objUtility = new clsUtility();
	    //System.out.println("Print Bill");
	    String filePath = System.getProperty("user.dir");
	    String fileName = "";
	    String billPrinterNames[] = clsGlobalVarClass.gBillPrintPrinterPort.split(",");

	    for (int printer = 0; printer < billPrinterNames.length; printer++)
	    {
		String billPrinterName = billPrinterNames[printer];

		if (type.equalsIgnoreCase("bill"))
		{
		    fileName = (filePath + "/Temp/Temp_Bill.txt");
		}
		else if (type.equalsIgnoreCase("Adv Receipt"))
		{
		    fileName = (filePath + "/Temp/Temp_Bill.txt");
		    billPrinterName = clsGlobalVarClass.gAdvReceiptPrinterPort;
		}
		else if (type.equalsIgnoreCase("dayend"))
		{
		    fileName = (filePath + "/Temp/Temp_DayEndReport.txt");
		}

		billPrinterName = billPrinterName.replaceAll("#", "\\\\");
		int printerIndex = 0;
		PrintRequestAttributeSet printerReqAtt = new HashPrintRequestAttributeSet();
		DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
		PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, printerReqAtt);
		for (int i = 0; i < printService.length; i++)
		{
		    System.out.println("Sys=" + printService[i].getName() + "\tBill Printer=" + billPrinterName);
		    if (billPrinterName.equalsIgnoreCase(printService[i].getName()))
		    {
			System.out.println("Bill Printer Sel=" + billPrinterName);
			printerIndex = i;
			break;
		    }
		}
		PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
		//DocPrintJob job = defaultService.createPrintJob();
		DocPrintJob job = printService[printerIndex].createPrintJob();
		FileInputStream fis = new FileInputStream(fileName);
		DocAttributeSet das = new HashDocAttributeSet();
		Doc doc = new SimpleDoc(fis, flavor, das);
		job.print(doc, printerReqAtt);
	    }
	    if (clsGlobalVarClass.gOpenCashDrawerAfterBillPrintYN)
	    {
		objUtility.funInvokeSampleJasper();
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    if (clsGlobalVarClass.gShowPrinterErrorMsg)
	    {
		JOptionPane.showMessageDialog(null, e.getMessage(), "Error Code - TFG 02", JOptionPane.ERROR_MESSAGE);
	    }
	}
    }

    public Map<Integer, Integer> funGetPAXPerBill(double totalPAX, double totalBills)
    {
	Map<Integer, Integer> mapPAXPerBill = new HashMap<>();
	mapPAXPerBill.put(0, 0);

	double pax = totalPAX;

	double noOfBills = totalBills;
	for (int i = 0; i < noOfBills; i++)
	{
	    int noOfBillsToBeFloor = (int) (pax % noOfBills);

	    if (i < noOfBillsToBeFloor)
	    {
		int paxPerBill = (int) Math.ceil(pax / noOfBills);
		//System.out.println("PAX=" + pax + "\tNo of bills=" + noOfBills + " \tpax Per Bill=" + paxPerBill);

		mapPAXPerBill.put(i, paxPerBill);
	    }
	    else
	    {
		int paxPerBill = (int) Math.floor(pax / noOfBills);
		//System.out.println("PAX=" + pax + "\tNo of bills=" + noOfBills + " \tpax Per Bill=" + paxPerBill);
		mapPAXPerBill.put(i, paxPerBill);
	    }
	}

	return mapPAXPerBill;
    }

    public String funGetBillNoOnModifyBill(String hdBillNo)
    {
	String billToBeRePrint = "";
	try
	{
	    String sql = "select a.strPOSCode,a.strBillSeries,a.strHdBillNo,a.strDtlBillNos  "
		    + "from tblbillseriesbilldtl a,tblbillseries b "
		    + "where (a.strPOSCode=b.strPOSCode or b.strPOSCode='All') "
		    + "and a.strBillSeries=b.strBillSeries "
		    + "and a.strHdBillNo='" + hdBillNo + "' "
		    + "and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + "and a.strClientCode='" + clsGlobalVarClass.gClientCode + "' "
		    + "and date(a.dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' "
		    + "and LENGTH(a.strDtlBillNos)>0 ";
	    ResultSet rsBillToBeRePrint = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsBillToBeRePrint.next())
	    {
		String dtlBillNos = rsBillToBeRePrint.getString(4);//detail bill nos

		String[] dtlBills = dtlBillNos.split(",");

		for (int i = 0; i < dtlBills.length; i++)
		{
		    sql = "select a.strPOSCode,a.strBillSeries,a.strHdBillNo,a.strDtlBillNos  "
			    + "from tblbillseriesbilldtl a,tblbillseries b "
			    + "where (a.strPOSCode=b.strPOSCode or b.strPOSCode='All') "
			    + "and a.strBillSeries=b.strBillSeries "
			    + "and a.strHdBillNo='" + dtlBills[i] + "' "
			    + "and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
			    + "and a.strClientCode='" + clsGlobalVarClass.gClientCode + "' "
			    + "and date(a.dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' ";
		    ResultSet rsDtlBillToBeRePrint = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		    if (rsDtlBillToBeRePrint.next())
		    {
			String billSeries = rsDtlBillToBeRePrint.getString(2);//billseries

			sql = "select a.strPOSCode,a.strType,a.strBillSeries,a.strPrintGTOfOtherBills "
				+ "from tblbillseries a "
				+ "where a.strBillSeries='" + billSeries + "' "
				+ "and a.strPrintGTOfOtherBills='Y' ";
			ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
			if (rs.next())
			{
			    billToBeRePrint = dtlBills[i];
			    break;
			}
			rs.close();

		    }
		    rsDtlBillToBeRePrint.close();
		}
	    }
	    rsBillToBeRePrint.close();

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    return billToBeRePrint;
	}
    }

    public void funSaveReprintAudit(String formName, String transactionName, String reasonCode, String reprintRemarks, String selectedKOT, String selectedBill, String dayEndDate)
    {
	try
	{
	    String docNo = null;

	    if (transactionName.equalsIgnoreCase("KOT"))
	    {
		if (selectedKOT.trim().length() > 0)
		{
		    docNo = selectedKOT;
		}
		if (selectedBill.trim().length() > 0)
		{
		    docNo = selectedBill;
		}
	    }
	    else if (transactionName.equalsIgnoreCase("Bill"))
	    {
		docNo = selectedBill;
	    }
	    else if (transactionName.equalsIgnoreCase("DayEnd"))
	    {
		docNo = dayEndDate;
	    }

	    if (reprintRemarks == null)
	    {
		reprintRemarks = "";
	    }

	    String sqlInsertAudit = "insert into tblAudit(strDocNo,strFormName,strTransactionName,strReasonCode,strRemarks,dtePOSDate,dteCreatedDate,strUserCreated,strClientCode,strDataPostFlag)"
		    + "values('" + docNo + "','" + formName + "','" + transactionName + "','" + reasonCode + "','" + reprintRemarks + "','" + clsGlobalVarClass.getPOSDateForTransaction() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gClientCode + "','N')";

	    clsGlobalVarClass.dbMysql.execute(sqlInsertAudit);
	    selectedBill = "";
	    selectedKOT = "";
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public boolean funPrintJasperKOT(String printerName, JasperPrint print)
    {
	boolean printerFound = false;
	try
	{
	    JRPrintServiceExporter exporter = new JRPrintServiceExporter();
	    //--- Set print properties
	    PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
	    printRequestAttributeSet.add(MediaSizeName.ISO_A4);

	    //----------------------------------------------------     
	    //printRequestAttributeSet.add(new Destination(new java.net.URI("file:d:/output/report.ps")));
	    //----------------------------------------------------     
	    PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();

	    System.out.println("cost center printer= " + printerName);
	    DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
	    PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, printServiceAttributeSet);
	    for (int i = 0; i < printService.length; i++)
	    {
		System.out.println("Sys=" + printService[i].getName());
		if (printerName.equalsIgnoreCase(printService[i].getName()))
		{
		    printerFound = true;
		    printServiceAttributeSet.add(new PrinterName(printerName, null));
		    System.out.println("KOT Printer found=>" + printerName);
		    break;
		}
	    }

	    if (printerFound)
	    {
		//--- Set print parameters      
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
		exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
		exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, printServiceAttributeSet);
		exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
		exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);

		exporter.exportReport();
	    }
	}
	catch (JRException e)
	{
	    e.printStackTrace();
	}

	return printerFound;
    }

    public String funAutoCustomerSelectionForLiquorBill()
    {
	String customerCode = "";
	try
	{
	    String liqCustomers = "select count(*) "
		    + "from tblcustomermaster a,tblcustomertypemaster b "
		    + "where a.strCustomerType=b.strCustTypeCode "
		    + "and (b.strCustType='LIQOUR' or b.strCustType='LIQUOR') ";
	    ResultSet rsLiqCustomers = clsGlobalVarClass.dbMysql.executeResultSet(liqCustomers);
	    if (rsLiqCustomers.next())
	    {
		int liquorLicHolderCount = rsLiqCustomers.getInt(1);
		if (liquorLicHolderCount > 0)
		{
		    String sqlLastCustCount = "select dblLastNo from tblinternal where strTransactionType='LiquorBillCustomer' ";
		    ResultSet rsLastCustomerCount = clsGlobalVarClass.dbMysql.executeResultSet(sqlLastCustCount);
		    if (rsLastCustomerCount.next())
		    {
			int intLastCustomer = rsLastCustomerCount.getInt(1);

			String sqlLastCustomer = "select a.strCustomerCode,a.strCustomerName,a.longMobileNo,a.strExternalCode,b.strCustType "
				+ "from tblcustomermaster a,tblcustomertypemaster b "
				+ "where a.strCustomerType=b.strCustTypeCode "
				+ "and (b.strCustType='LIQOUR' or b.strCustType='LIQUOR')  "
				+ "limit " + intLastCustomer + ",1 ";
			ResultSet rsLastCustomer = clsGlobalVarClass.dbMysql.executeResultSet(sqlLastCustomer);
			if (rsLastCustomer.next())
			{
			    customerCode = rsLastCustomer.getString(1);
			}
			rsLastCustomer.close();

			if ((intLastCustomer + 1) == liquorLicHolderCount)
			{
			    intLastCustomer = 0;
			}
			else
			{
			    intLastCustomer = intLastCustomer + 1;
			}

			clsGlobalVarClass.dbMysql.execute("update tblinternal set dblLastNo='" + intLastCustomer + "' where strTransactionType='LiquorBillCustomer' ");
		    }
		    rsLastCustomerCount.close();
		}
		else
		{
		    customerCode = "";
		}
	    }
	    rsLiqCustomers.close();

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    return customerCode;
	}
    }

    public int funGetLastOrderNo()
    {
	int orderNo = 0;
	try
	{
	    String sqlLastOrderNo = "select * from tblinternal a  where a.strTransactionType='OrderNo' ";
	    ResultSet rsLastCustomer = clsGlobalVarClass.dbMysql.executeResultSet(sqlLastOrderNo);
	    if (rsLastCustomer.next())
	    {
		orderNo = rsLastCustomer.getInt(2);//llastOrderNo                             
		orderNo++;
		clsGlobalVarClass.dbMysql.execute("update tblinternal set dblLastNo=(dblLastNo+1) where strTransactionType='OrderNo' ");
	    }
	    rsLastCustomer.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    return orderNo;
	}
    }

    public boolean funHasTLA(String formName, String userCode)
    {
	StringBuilder sqlBuilder = new StringBuilder();
	boolean hasTLA = false;
	try
	{
	    sqlBuilder.setLength(0);
	    sqlBuilder.append("select a.strUserCode,a.strFormName,a.strGrant,a.strTLA "
		    + "from tbluserdtl a "
		    + "where a.strFormName='" + formName + "' "
		    + "and a.strTLA='true' "
		    + "and a.strUserCode='" + userCode + "' ");
	    ResultSet rsTLA = clsGlobalVarClass.dbMysql.executeResultSet(sqlBuilder.toString());
	    if (rsTLA.next())
	    {
		hasTLA = true;
	    }
	    else
	    {
		hasTLA = false;
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return hasTLA;
    }

    public boolean funHasGrant(String formName, String userCode)
    {
	boolean hasGrant = false;
	try
	{

	    if (userCode.equalsIgnoreCase("SANGUINE"))
	    {
		hasGrant = true;
	    }
	    else
	    {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.setLength(0);
		sqlBuilder.append("select a.strUserCode,a.strUserName,a.strSuperType from tbluserhd a "
			+ "where (a.strUserCode='" + userCode + "' or a.strDebitCardString='" + userCode + "') and a.strSuperType='Super' ");
		ResultSet rsTLA = clsGlobalVarClass.dbMysql.executeResultSet(sqlBuilder.toString());
		if (rsTLA.next())
		{
		    hasGrant = true;
		}
		else
		{
		    sqlBuilder.setLength(0);
		    sqlBuilder.append("select a.strUserCode,a.strFormName,a.strGrant,a.strTLA  "
			    + "from tbluserdtl a,tbluserhd b  "
			    + "where a.strUserCode=b.strUserCode "
			    + "and a.strFormName='" + formName + "'  "
			    + "and a.strGrant='true'  "
			    + "and (a.strUserCode='" + userCode + "' or b.strDebitCardString='" + userCode + "') ");
		    rsTLA = clsGlobalVarClass.dbMysql.executeResultSet(sqlBuilder.toString());
		    if (rsTLA.next())
		    {
			hasGrant = true;
		    }
		    else
		    {
			hasGrant = false;
		    }
		}
		rsTLA.close();
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return hasGrant;
    }

    public String funIsValidUser(String userCode, String passwd)
    {
	StringBuilder sqlBuilder = new StringBuilder();
	String retResult = "Invalid User";
	boolean isUserGranted = false;
	try
	{
	    Date objDate = new Date();
	    int day = objDate.getDate();
	    int month = objDate.getMonth() + 1;
	    int year = objDate.getYear() + 1900;
	    String currentDate = year + "-" + month + "-" + day;
	    if (userCode.trim().equalsIgnoreCase("SANGUINE"))
	    {
		int password = year + month + day + day;

		clsUtility objUtility = new clsUtility();

		String strpass = Integer.toString(password);
		char num1 = strpass.charAt(0);
		char num2 = strpass.charAt(1);
		char num3 = strpass.charAt(2);
		char num4 = strpass.charAt(3);
		String alph1 = objUtility.funGetAlphabet(Character.getNumericValue(num1));
		String alph2 = objUtility.funGetAlphabet(Character.getNumericValue(num2));
		String alph3 = objUtility.funGetAlphabet(Character.getNumericValue(num3));
		String alph4 = objUtility.funGetAlphabet(Character.getNumericValue(num4));

		String finalPassword = String.valueOf(password) + alph1 + alph2 + alph3 + alph4;

		String userPassword = passwd;
		if (finalPassword.equalsIgnoreCase(userPassword))
		{

		    String userName = "SANGUINE";
		    String userType = "Super";
		    String posAccessCode = "All POS";

		    retResult = "Valid User";
		}
		else
		{
		    retResult = "Login Failed";
		}
	    }
	    else
	    {
		if (passwd.length() == 0)
		{

		    String sql = "  select strDebitCardString from tbluserhd where strUserCode='" + userCode + "' ";
		    ResultSet rssql1 = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		    if (rssql1.next())
		    {
			String debitCardString = rssql1.getString(1);

			retResult = funCHeckLoginForDebitCardString(debitCardString);
			if (retResult.equalsIgnoreCase("Valid User"))
			{
			    retResult = "Valid User";
			}
			else
			{
			    retResult = "Invalid User";
			}
		    }
		    else
		    {
			retResult = "Invalid User";
		    }
		}
		else
		{

		    String encKey = "04081977";
		    String password = clsGlobalSingleObject.getObjPasswordEncryptDecreat().encrypt(encKey, passwd.trim().toUpperCase());
		    //System.out.println(password);
		    String selectQuery = "select count(*),strUserName,strSuperType,dteValidDate,strPOSAccess from tbluserhd "
			    + "where strUserCode='" + userCode + "' and strPassword='" + password + "'";
		    //System.out.println(selectQuery);
		    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
		    rs.next();
		    if (rs.getInt(1) == 1)
		    {
			String userName = rs.getString(2);
			String userType = rs.getString(3);
			String posAccessCode = rs.getString(5);

			selectQuery = "select count(*) from tbluserhd WHERE strUserCode = '" + userCode
				+ "' and strPassword='" + password + "'" + " AND dteValidDate>='" + currentDate + "'";

			rs = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
			rs.next();
			if (rs.getInt(1) == 0)
			{
			    rs.close();
			    retResult = "User Has Expired";
			}
			else
			{
			    retResult = "Valid User";
			}
		    }
		    else
		    {
			rs.close();
			retResult = "Login Failed";
		    }
		}
	    }
	}

	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    return retResult;
	}
    }

    public String funCHeckLoginForDebitCardString(String debitCardString) throws Exception
    {
	boolean flgLoginStatus = false;
	String retResult = "";
	StringBuilder sqlBuilder = new StringBuilder();
	String selectQuery = "select count(*),strUserName,strSuperType,dteValidDate,strPOSAccess,strUserCode from tbluserhd "
		+ "where strDebitCardString='" + debitCardString + "'";
	//System.out.println(selectQuery);
	ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
	rs.next();
	if (rs.getInt(1) == 1)
	{
	    String userCode = rs.getString(6);
	    String userName = rs.getString(2);
	    String userType = rs.getString(3);
	    String posAccessCode = rs.getString(5);

	    selectQuery = "select count(*) from tbluserhd WHERE strDebitCardString = '" + debitCardString + "' "
		    + " AND dteValidDate>='" + rs.getString(4) + "' ";
	    rs = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
	    rs.next();
	    if (rs.getInt(1) == 0)
	    {
		flgLoginStatus = false;
		rs.close();
		retResult = "User Has Expired";
	    }
	    else
	    {
		retResult = "Valid User";
	    }
	}
	return retResult;
    }

    public String funGetDefaultReasonCode(String defaultReasonFor)
    {

	String reasonCode = "";
	try
	{
	    String selectQuery = "select a.strReasonCode,a.strReasonName from tblreasonmaster a "
		    + "where " + defaultReasonFor + "='Y' "
		    + "limit 1 ";
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
	    if (rs.next())
	    {
		reasonCode = rs.getString(1);
	    }
	    rs.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    new clsUtility().funWriteErrorLog(e);
	}
	finally
	{
	    return reasonCode;
	}
    }

    public void funSavePropertySetup(String posCode) throws Exception
    {

	String sql = "select a.strClientCode,a.strClientName,a.strPOSCode from tblsetup a where a.strPOSCode!='All' ";
	ResultSet rsPOSWisePropertySetup = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	if (rsPOSWisePropertySetup.next())
	{
	    String sqlNewEntry = sql = "insert into tblsetup(strClientCode,strClientName,strAddressLine1,strAddressLine2 "//4
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
		    + ",strEnableNFCInterface,strBenowIntegrationYN,strXEmail,strMerchantCode,strAuthenticationKey,strSalt) "//196
		    //selection
		    + " select strClientCode,strClientName,strAddressLine1,strAddressLine2 "//4
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
		    + ",strProductionLinkup,strLockDataOnShift,strWSClientCode,'" + posCode + "',strEnableBillSeries,strEnablePMSIntegrationYN,strPrintTimeOnBill"//151
		    + ",strPrintTDHItemsInBill,strPrintRemarkAndReasonForReprint,intDaysBeforeOrderToCancel,intNoOfDelDaysForAdvOrder,intNoOfDelDaysForUrgentOrder"//156
		    + ",strSetUpToTimeForAdvOrder,strSetUpToTimeForUrgentOrder,strUpToTimeForAdvOrder,strUpToTimeForUrgentOrder"//160
		    + ",strEnableBothPrintAndSettleBtnForDB,strInrestoPOSIntegrationYN,strInrestoPOSWebServiceURL,strInrestoPOSId"//164
		    + ",strInrestoPOSKey,strCarryForwardFloatAmtToNextDay,strOpenCashDrawerAfterBillPrintYN,strPropertyWiseSalesOrderYN" //168
		    + ",strDataPostFlag,strShowItemDetailsGrid,strShowPopUpForNextItemQuantity,strJioMoneyIntegration,strJioWebServiceUrl,strJioMID,strJioTID,strJioActivationCode,strJioDeviceID "//177
		    + ",strNewBillSeriesForNewDay,strShowReportsPOSWise,strEnableDineIn,strAutoAreaSelectionInMakeKOT,strConsolidatedKOTPrinterPort"//182
		    + ",dblRoundOff,strShowUnSettlementForm,strPrintOpenItemsOnBill,strPrintHomeDeliveryYN,strScanQRYN,strAreaWisePromotions "//188
		    + ",strPrintItemsOnMoveKOTMoveTable,strShowPurRateInDirectBiller,strEnableTableReservationForCustomer "//191
		    + ",strAutoShowPopItems,intShowPopItemsOfDays,strPostSalesCostOrLoc,strEffectOfSales,strPOSWiseItemToMMSProductLinkUpYN ,strEnableMasterDiscount"//197
		    + ",strEnableNFCInterface,strBenowIntegrationYN,strXEmail,strMerchantCode,strAuthenticationKey,strSalt "
		    + " from tblsetup "
		    + " where strPOSCode!='All' "
		    + " limit 1 ";
	    clsGlobalVarClass.dbMysql.execute(sqlNewEntry);
	}
	rsPOSWisePropertySetup.close();
    }

    public int funUpdateDayEndReportsSent(String filterPOSCode, String filterPOSDate, String filterClientCode, int filterShift)
    {

	int affectedRows = 0;
	try
	{
	    affectedRows = clsGlobalVarClass.dbMysql.execute("update tbldayendprocess "
		    + "set dteDayEndReportsDateTime='" + clsGlobalVarClass.getCurrentDateTime() + "' "
		    + "where strPOSCode='" + filterPOSCode + "' "
		    + "and date(dtePOSDate)='" + filterPOSDate + "' "
		    + "and intShiftCode='" + filterShift + "' "
		    + "and strDayEnd='Y' "
		    + "and strShiftEnd='Y' ");
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    new clsUtility().funWriteErrorLog(e);
	}
	finally
	{
	    return affectedRows;
	}
    }

    public boolean isDayEndHappened(String toDate)
    {

	boolean isDayEndHappend = false;
	try
	{

	    String sql = "select a.strPOSCode,date(a.dtePOSDate),a.strDayEnd "
		    + "from tbldayendprocess a "
		    + "where a.strDayEnd='Y' "
		    + "and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + "and date(a.dtePOSDate)='" + toDate + "' ";
	    ResultSet rsDayEnd = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsDayEnd.next())
	    {
		isDayEndHappend = true;
	    }
	    rsDayEnd.close();

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    new clsUtility().funWriteErrorLog(e);
	}
	finally
	{
	    return isDayEndHappend;
	}
    }

    public boolean funIsAllItemFired(String tableNo)
    {

	boolean isAllItemsFired = true;
	try
	{
	    String sql = "select sum(a.dblItemQuantity)totalItemQty,sum(a.dblFiredQty)totalFireQty,sum(a.dblItemQuantity)-sum(a.dblFiredQty)pendingQty "
		    + "from tblitemrtemp a "
		    + "where a.strTableNo='" + tableNo + "' "
		    + "and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' ";
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rs.next())
	    {
		int pendingQty = rs.getInt(3);
		if (pendingQty > 0)
		{
		    isAllItemsFired = false;
		}
	    }
	    rs.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    return isAllItemsFired;
	}
    }

    /**
     * This method is used to get recharge no
     *
     * @return string
     */
    /*
     * private long funGetRechargeNo() throws Exception { long lastNo = 1; sql =
     * "select count(dblLastNo) from tblinternal where
     * strTransactionType='RechargeNo'"; ResultSet rsRechargeNo =
     * clsGlobalVarClass.dbMysql.executeResultSet(sql); rsRechargeNo.next(); int
     * cntRechargeNo = rsRechargeNo.getInt(1); rsRechargeNo.close(); if
     * (cntRechargeNo > 0) { sql = "select dblLastNo from tblinternal where
     * strTransactionType='RechargeNo'"; rsRechargeNo =
     * clsGlobalVarClass.dbMysql.executeResultSet(sql); if(rsRechargeNo.next())
     * { long code = rsRechargeNo.getLong(1); code = code + 1; lastNo = code;
     *
     * String updateSql = "update tblinternal set dblLastNo=" + lastNo + " " +
     * "where strTransactionType='RechargeNo'";
     * clsGlobalVarClass.dbMysql.execute(updateSql); } rsRechargeNo.close(); }
     * else { lastNo = 1; sql = "insert into tblinternal values('RechargeNo'," +
     * 1 + ")"; clsGlobalVarClass.dbMysql.execute(sql); } return lastNo; }
     */
    public String funGetRechargeNo() throws Exception
    {
	long lastNo = 0;
	String rechargeNo = "";
	String sql = "select right(max(intRechargeNo),7) from tbldebitcardrecharge where strClientCode='" + clsGlobalVarClass.gClientCode + "' ";
	ResultSet rsOrderCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	if (rsOrderCode.next())
	{
	    lastNo = rsOrderCode.getLong(1);
	}
	rsOrderCode.close();
	lastNo = lastNo + 1;
	rechargeNo = "RC" + String.format("%07d", lastNo);
	sql = "update tblinternal set dblLastNo='" + lastNo + "' where strTransactionType='RechargeNo'";
	clsGlobalVarClass.dbMysql.execute(sql);

	return rechargeNo;
    }

    /**
     * This method is used to get redeem no
     *
     * @return string
     */
    public String funGetRedeemNo() throws Exception
    {
	long lastNo = 1;
	String sql = "select count(dblLastNo) from tblinternal where strTransactionType='RedeemNo'";
	ResultSet rsCustTypeCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	rsCustTypeCode.next();
	int cntCustType = rsCustTypeCode.getInt(1);
	rsCustTypeCode.close();
	if (cntCustType > 0)
	{
	    sql = "select dblLastNo from tblinternal where strTransactionType='RedeemNo'";
	    rsCustTypeCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    rsCustTypeCode.next();
	    long code = rsCustTypeCode.getLong(1);
	    code = code + 1;
	    lastNo = code;
	    String updateSql = "update tblinternal set dblLastNo=" + lastNo + " "
		    + "where strTransactionType='RedeemNo'";
	    clsGlobalVarClass.dbMysql.execute(updateSql);
	    rsCustTypeCode.close();
	}
	else
	{
	    lastNo = 1;
	    sql = "insert into tblinternal values('RedeemNo'," + 1 + ")";
	    clsGlobalVarClass.dbMysql.execute(sql);
	}

	String redeemNoToInsert = "RD" + String.format("%07d", lastNo);

	return redeemNoToInsert;
    }

    /**
     * This method is used to get recharge slip no
     *
     * @return string
     */
    public String funGetRechargeSlipNo() throws Exception
    {
	long lastNo = 1;
	String sql = "select count(dblLastNo) from tblinternal where strTransactionType='SlipNo'";
	ResultSet rsRechargeSlip = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	rsRechargeSlip.next();
	int cntSlip = rsRechargeSlip.getInt(1);
	rsRechargeSlip.close();
	if (cntSlip > 0)
	{
	    sql = "select dblLastNo from tblinternal where strTransactionType='SlipNo'";
	    rsRechargeSlip = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    rsRechargeSlip.next();
	    long code = rsRechargeSlip.getLong(1);
	    code = code + 1;
	    lastNo = code;

	    String updateSql = "update tblinternal set dblLastNo=" + lastNo + " "
		    + "where strTransactionType='SlipNo'";
	    clsGlobalVarClass.dbMysql.execute(updateSql);
	    rsRechargeSlip.close();
	}
	else
	{
	    lastNo = 1;
	    sql = "insert into tblinternal values('SlipNo'," + 1 + ")";
	    clsGlobalVarClass.dbMysql.execute(sql);
	}

	String rechargeSlipNoToInsert = "SL" + String.format("%07d", lastNo);

	return rechargeSlipNoToInsert;
    }

}
