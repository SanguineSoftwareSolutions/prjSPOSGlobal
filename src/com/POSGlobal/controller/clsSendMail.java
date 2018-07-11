/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSGlobal.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class clsSendMail
{

//     public static void main(String args[])
//     {
//     String data=" This is send mail profram to send mail on the server ";
//     final String filePath = System.getProperty("user.dir") + "/Temp/Temp_DayEndReport.txt";
//     new clsSendMail().funSendMail(0, 0, 0, filePath);
//     }
    private clsUtility2 objUtility2 = new clsUtility2();

    public int funSendMail(double totalSales, double totalDiscount, double totalPayment, String filePath,String posCode, String posName, String posDate, int shiftNo, String clientCode)
    {
	int ret = 0;
	//String to="ingaleprashant8@gmail.com";//change accordingly
	String to = clsGlobalVarClass.gReceiverEmailIds;//change accordingly
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
		return new PasswordAuthentication(clsGlobalVarClass.gSenderEmailId, clsGlobalVarClass.gSenderMailPassword);//change accordingly
	    }
	});

	//compose message
	try
	{
	    MimeMessage message = new MimeMessage(session);
	    message.setFrom(new InternetAddress(clsGlobalVarClass.gSenderEmailId));//change accordingly
	    String[] arrRecipient = to.split(",");

	    if (to.length() > 0)
	    {
		System.out.println(to);
		for (int cnt = 0; cnt < arrRecipient.length; cnt++)
		{
		    message.addRecipient(Message.RecipientType.TO, new InternetAddress(arrRecipient[cnt]));
		}
	    }
	    message.setSubject("DAY END REPORT " +posName + " " + posDate);
	    String amount1 = "Total Sales=";
	    amount1 = amount1 + String.valueOf(totalSales);
	    String amount2 = "\n" + "Total Discount=";
	    amount2 = amount2 + String.valueOf(totalDiscount);
	    String amount3 = "\n" + "Total Payment=";
	    amount3 = amount3 + String.valueOf(totalPayment);
	    String msgBody = amount1 + amount2 + amount3;
	    //message.setText(msgBody);

	    // Create the message part 
	    BodyPart messageBodyPart = new MimeBodyPart();

	    DataSource source = new FileDataSource(filePath);
	    String data = "";
	    File file = new File(filePath);
	    FileReader fread = new FileReader(file);
	    FileInputStream fis = fis = new FileInputStream(file);
	    BufferedReader KOTIn = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
	    String line = "";
	    while ((line = KOTIn.readLine()) != null)
	    {
		data = data + line + "\n";
	    }
	    // Fill the message
	    messageBodyPart.setText(data);

	    Multipart multipart = new MimeMultipart();

	    // Set text message part
	    //multipart.addBodyPart(messageBodyPart);
	    // Part two is attachment
	    messageBodyPart = new MimeBodyPart();
	    messageBodyPart.setDataHandler(new DataHandler(source));
	    messageBodyPart.setFileName(file.getName());
	    multipart.addBodyPart(messageBodyPart);

	    String dayEndReportFilePath = System.getProperty("user.dir");
	    File dayEndReportFile = new File(dayEndReportFilePath + "/Reports");
	    int dayEndReportsCount = 0;
	    if (dayEndReportFile.exists())
	    {
		File[] filesPath = dayEndReportFile.listFiles();
		for (int i = 0; i < filesPath.length; i++)
		{
		    messageBodyPart = new MimeBodyPart();
		    source = new FileDataSource(filesPath[i]);
		    messageBodyPart.setDataHandler(new DataHandler(source));
		    messageBodyPart.setFileName(filesPath[i].getName());
		    multipart.addBodyPart(messageBodyPart);

		    dayEndReportsCount++;
		}
	    }

	    // Send the complete message parts
	    message.setContent(multipart);

	    if (to.length() > 0)
	    {
		//send message  
		Transport.send(message);
		System.out.println("message sent successfully");

		objUtility2.funUpdateDayEndReportsSent(clsGlobalVarClass.gPOSCode, posDate, clsGlobalVarClass.gClientCode, clsGlobalVarClass.gShiftNo);
	    }
	    else
	    {
		System.out.println("Email has No Recipient");
	    }
	}
	catch (MessagingException e)
	{
	    e.printStackTrace();
	    //throw new RuntimeException(e);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	ret = 1;
	return ret;
    }

    /* Function to send email 
     Param 1 - Receivers amil Ids.
     Param 2 - Path of file to attach.
    
     */
    public int funSendMail(String receiverMailIds, String filePath)
    {
	int ret = 0;
	//String to="ingaleprashant8@gmail.com";//change accordingly
	String to = clsGlobalVarClass.gReceiverEmailIds;//change accordingly
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

	//compose message
	try
	{
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

	    message.setSubject("DAY END REPORT " + clsGlobalVarClass.gPOSName + " " + clsGlobalVarClass.gPOSDate);

	    String msgBody = clsGlobalVarClass.gClientName + " DB Backup for " + clsGlobalVarClass.getCurrentDateTime();
	    //message.setText(msgBody);

	    // Create the message part 
	    BodyPart messageBodyPart = new MimeBodyPart();
	    DataSource source = new FileDataSource(filePath);

	    /*String data="";
             File file=new File(filePath);
             FileInputStream fis=fis = new FileInputStream(file);
             BufferedReader KOTIn = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
             String line = "";
             while ((line = KOTIn.readLine()) != null) {
             data = data + line + "\n";
             }*/
	    // Fill the message
	    messageBodyPart.setText(msgBody);

	    Multipart multipart = new MimeMultipart();

	    // Set text message part
	    multipart.addBodyPart(messageBodyPart);

	    // Part two is attachment
	    messageBodyPart = new MimeBodyPart();
	    messageBodyPart.setDataHandler(new DataHandler(source));
	    messageBodyPart.setFileName(filePath);
	    multipart.addBodyPart(messageBodyPart);

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
	catch (MessagingException e)
	{
	    e.printStackTrace();
	    //throw new RuntimeException(e);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	ret = 1;
	return ret;
    }

    public int funSendMail(String receiverMailIds, File[] filesPath)
    {
	int ret = 0;
	//String to="ingaleprashant8@gmail.com";//change accordingly
	String to = receiverMailIds;//change accordingly
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

	//compose message
	try
	{
	    MimeMessage message = new MimeMessage(session);
	    //message.setFrom(new InternetAddress("paritoshkumar112@gmail.com"));//change accordingly
	    message.setFrom(new InternetAddress(clsGlobalVarClass.gSenderEmailId));//change accordingly
	    String[] arrRecipient = to.split(",");

	    for (int cnt = 0; cnt < arrRecipient.length; cnt++)
	    {
		System.out.println(arrRecipient[cnt]);
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(arrRecipient[cnt]));
	    }
	    message.setSubject("DAY END REPORT " + clsGlobalVarClass.gPOSName + " " + clsGlobalVarClass.gPOSDate);

	    String msgBody = clsGlobalVarClass.gClientName + " DB Backup for " + clsGlobalVarClass.getCurrentDateTime();
	    //message.setText(msgBody);

	    // Create the message part 
	    BodyPart messageBodyPart = new MimeBodyPart();

	    /*String data="";
             File file=new File(filePath);
             FileInputStream fis=fis = new FileInputStream(file);
             BufferedReader KOTIn = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
             String line = "";
             while ((line = KOTIn.readLine()) != null) {
             data = data + line + "\n";
             }*/
	    // Fill the message
	    messageBodyPart.setText(msgBody);

	    Multipart multipart = new MimeMultipart();

	    // Set text message part
	    multipart.addBodyPart(messageBodyPart);

	    // Part two is attachment
	    for (int i = 0; i < filesPath.length; i++)
	    {
		messageBodyPart = new MimeBodyPart();
		DataSource source = new FileDataSource(filesPath[i]);
		messageBodyPart.setDataHandler(new DataHandler(source));
		messageBodyPart.setFileName(filesPath[i].getAbsolutePath());
		multipart.addBodyPart(messageBodyPart);
	    }

	    // Send the complete message parts
	    message.setContent(multipart);

	    //send message  
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
	catch (MessagingException e)
	{
	    e.printStackTrace();
	    //throw new RuntimeException(e);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	ret = 1;
	return ret;
    }

    public int funSendMailForConsolodatePOS(double totalSales, double totalDiscount, double totalPayment, String filePath)
    {
	int ret = 0;
	//String to="ingaleprashant8@gmail.com";//change accordingly
	String to = clsGlobalVarClass.gReceiverEmailIds;//change accordingly
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

	//compose message
	try
	{
	    MimeMessage message = new MimeMessage(session);
	    //message.setFrom(new InternetAddress("paritoshkumar112@gmail.com"));//change accordingly
	    message.setFrom(new InternetAddress(clsGlobalVarClass.gSenderEmailId));//change accordingly
	    String[] arrRecipient = to.split(",");

	    for (int cnt = 0; cnt < arrRecipient.length; cnt++)
	    {
		System.out.println(arrRecipient[cnt]);
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(arrRecipient[cnt]));
	    }
	    message.setSubject("DAY END REPORT " + clsGlobalVarClass.gPOSName + " " + clsGlobalVarClass.gPOSDate);
	    String amount1 = "Total Sales=";
	    amount1 = amount1 + String.valueOf(totalSales);
	    String amount2 = "\n" + "Total Discount=";
	    amount2 = amount2 + String.valueOf(totalDiscount);
	    String amount3 = "\n" + "Total Payment=";
	    amount3 = amount3 + String.valueOf(totalPayment);
	    String msgBody = amount1 + amount2 + amount3;
	    //message.setText(msgBody);

	    // Create the message part 
	    BodyPart messageBodyPart = new MimeBodyPart();

	    DataSource source = new FileDataSource(filePath);
	    String data = "";
	    File file = new File(filePath);
	    FileReader fread = new FileReader(file);
	    FileInputStream fis = fis = new FileInputStream(file);
	    BufferedReader KOTIn = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
	    String line = "";
	    while ((line = KOTIn.readLine()) != null)
	    {
		data = data + line + "\n";
	    }
	    // Fill the message
	    messageBodyPart.setText(data);

	    Multipart multipart = new MimeMultipart();

	    // Set text message part
	    multipart.addBodyPart(messageBodyPart);

	    // Part two is attachment
	    messageBodyPart = new MimeBodyPart();

	    messageBodyPart.setDataHandler(new DataHandler(source));
	    messageBodyPart.setFileName(filePath);
	    multipart.addBodyPart(messageBodyPart);

	    // Send the complete message parts
	    message.setContent(multipart);

	    //send message  
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
	catch (MessagingException e)
	{
	    e.printStackTrace();
	    //throw new RuntimeException(e);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	ret = 1;
	return ret;

    }

    public int funSendMail(String posCode, String posName, String fromDate, String toDate, List listOfReports)
    {
	int ret = 0;
	final String from = "sanguineauditing@gmail.com";//change accordingly
	String sanguineAuditing = "sanguineauditing@gmail.com";//change accordingly
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
		return new PasswordAuthentication(from, "Sanguine@2017");//change accordingly
	    }
	});

	//compose message
	try
	{
	    MimeMessage message = new MimeMessage(session);
	    message.setFrom(new InternetAddress(from));//change accordingly

	    message.addRecipient(Message.RecipientType.TO, new InternetAddress(sanguineAuditing));

	    String to = clsGlobalVarClass.gReceiverEmailIds;
	    String[] arrRecipient = to.split(",");

	    if (to.length() > 0)
	    {
		System.out.println(to);
		for (int cnt = 0; cnt < arrRecipient.length; cnt++)
		{
		    message.addRecipient(Message.RecipientType.TO, new InternetAddress(arrRecipient[cnt]));
		}
	    }
	    message.setSubject("DB Backup Of '" + clsGlobalVarClass.gClientCode + "' '" + clsGlobalVarClass.gClientName + "' For " + clsGlobalVarClass.getCurrentDateTime() + ".");

	    // Create the message part 
	    BodyPart messageBodyPart = new MimeBodyPart();

	    String data = "";
	    for (int i = 0; i < listOfReports.size(); i++)
	    {
		data += listOfReports.get(i).toString();;
		data += "\n";
	    }
	    data += "\n\n\n\n\n\n\n\n";
	    data += "\nThank You,";
	    data += "\nTeam SANGUINE";

	    // Fill the message
	    messageBodyPart.setText(data);

	    Multipart multipart = new MimeMultipart();

	    FileDataSource source;

	    multipart.addBodyPart(messageBodyPart);

	    String dayEndReportFilePath = System.getProperty("user.dir");
	    File dayEndReportFile = new File(dayEndReportFilePath + "/Reports");
	    if (dayEndReportFile.exists())
	    {
		File[] filesPath = dayEndReportFile.listFiles();
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
	catch (MessagingException e)
	{
	    e.printStackTrace();
	    //throw new RuntimeException(e);
	    ret = 1;
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}

	return ret;
    }

    public int funSendMail(String posCode, String posName, String posDate, int shiftNo, String clientCode)
    {
	int ret = 0;
	//String to="ingaleprashant8@gmail.com";//change accordingly
	String to = clsGlobalVarClass.gReceiverEmailIds;//change accordingly
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
		return new PasswordAuthentication(clsGlobalVarClass.gSenderEmailId, clsGlobalVarClass.gSenderMailPassword);//change accordingly
	    }
	});

	//compose message
	try
	{
	    MimeMessage message = new MimeMessage(session);
	    message.setFrom(new InternetAddress(clsGlobalVarClass.gSenderEmailId));//change accordingly
	    String[] arrRecipient = to.split(",");

	    if (to.length() > 0)
	    {
		System.out.println(to);
		for (int cnt = 0; cnt < arrRecipient.length; cnt++)
		{
		    message.addRecipient(Message.RecipientType.TO, new InternetAddress(arrRecipient[cnt]));
		}
	    }
	    message.setSubject("DAY END REPORT " + posName + " " + posDate);

	    Multipart multipart = new MimeMultipart();

	    String dayEndReportFilePath = System.getProperty("user.dir");
	    File dayEndReportFile = new File(dayEndReportFilePath + "/Reports");
	    int dayEndReportsCount = 0;
	    if (dayEndReportFile.exists())
	    {
		File[] filesPath = dayEndReportFile.listFiles();
		for (int i = 0; i < filesPath.length; i++)
		{
		    MimeBodyPart messageBodyPart = new MimeBodyPart();
		    FileDataSource source = new FileDataSource(filesPath[i]);
		    messageBodyPart.setDataHandler(new DataHandler(source));
		    messageBodyPart.setFileName(filesPath[i].getName());
		    multipart.addBodyPart(messageBodyPart);

		    dayEndReportsCount++;
		}
	    }

	    // Send the complete message parts
	    message.setContent(multipart);

	    if (to.length() > 0)
	    {
		//send message  
		Transport.send(message);
		System.out.println("message sent successfully");

		objUtility2.funUpdateDayEndReportsSent(posCode,posDate,clientCode,shiftNo);
	    }
	    else
	    {
		System.out.println("Email has No Recipient");
	    }
	}
	catch (MessagingException e)
	{
	    e.printStackTrace();
	    //throw new RuntimeException(e);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	ret = 1;
	return ret;
    }

}
