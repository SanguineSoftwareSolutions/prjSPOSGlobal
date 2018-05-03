package com.POSGlobal.controller;

import com.sanguine.forms.clsEncryptDecryptAlgorithm;
import com.sanguine.forms.frmConfigSettings;
import java.util.List;
import javax.swing.JOptionPane;

public class clsPosConfigFile
{
    public static String userId, password, ipAddress, dbPortNo, dbBackupPath, databaseName, serverName, portNo, gPrintWindowsOS, gPrintOS, gPrinter;
    public static String exportReportPath, modifiedDateForMaster, lblServerName, lblPortNo;
    public static String gPrinterType;
    public static String gServerFilePath;
    public static String gSelectWaiterFromCardSwipe;
    public static String gDatabaseBackupFilePath;
    public static String gHOCommunication;

    public clsPosConfigFile()
    {
        try
        {
            frmConfigSettings objConfigSettings = new frmConfigSettings();
            List<String> arrListConfigData=objConfigSettings.funReadConfigFile();
            
            serverName = arrListConfigData.get(0);
            databaseName = arrListConfigData.get(1);
            userId = clsEncryptDecryptAlgorithm.decrypt(arrListConfigData.get(2));
            password = clsEncryptDecryptAlgorithm.decrypt(arrListConfigData.get(3));
            ipAddress = arrListConfigData.get(4);
            portNo = arrListConfigData.get(5);
            dbBackupPath = arrListConfigData.get(6);
            exportReportPath = arrListConfigData.get(7);
            gPrintOS = arrListConfigData.get(8);
            gPrinter = arrListConfigData.get(9);
            gPrinterType = arrListConfigData.get(10);
            if (null != arrListConfigData.get(11))
            {
                clsGlobalVarClass.gTouchScreenMode = Boolean.parseBoolean(arrListConfigData.get(11));
            }
            else
            {
                clsGlobalVarClass.gTouchScreenMode = true;
            }
            gServerFilePath = arrListConfigData.get(12);
            gSelectWaiterFromCardSwipe = arrListConfigData.get(13);
            gDatabaseBackupFilePath = arrListConfigData.get(14);
            gHOCommunication = arrListConfigData.get(15);
            clsGlobalVarClass.gAdvReceiptPrinterPort = arrListConfigData.get(16);
            clsGlobalVarClass.gBillPrintPrinterPort = gPrinter;
            System.out.println("Bill Printer Name= " + clsGlobalVarClass.gBillPrintPrinterPort+"\t Adv Order Printer Name= "+clsGlobalVarClass.gAdvReceiptPrinterPort);
        }
        catch (Exception e)
        {
            new clsUtility().funWriteErrorLog(e);
            JOptionPane.showMessageDialog(null, "Please Check Config File!!!");
            e.printStackTrace();
            //System.exit(0);
        }
    }
}
