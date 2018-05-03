package com.POSGlobal.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Date;

public class clsGenerateErrorLogs implements Runnable
{

    Exception ex;
    String dateTime, date;
    private clsUtility2 objUtility2=new clsUtility2();

    public clsGenerateErrorLogs(Exception ex)
    {
        this.ex = ex;
        Date dt = new Date();
        dateTime = dt.getDate() + "-" + (dt.getMonth() + 1) + "-" + (dt.getYear() + 1900) + " " + dt.getHours() + ":" + dt.getMinutes() + ":" + dt.getSeconds();
        date = dt.getDate() + "-" + (dt.getMonth() + 1) + "-" + (dt.getYear() + 1900);
    }

    private void funCreateLogFolder()
    {
        try
        {
            String filePath = System.getProperty("user.dir");
            File errorLogsDir = new File(filePath + "/ErrorLogs");
            if (!errorLogsDir.exists())
            {
                errorLogsDir.mkdirs();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void run()
    {

        try
        {
            funCreateLogFolder();
            String filePath = System.getProperty("user.dir");
            File logFile = new File(filePath + "/ErrorLogs/err " + date + ".txt");
            Writer fileWr = new FileWriter(logFile, true);
            PrintWriter pw = new PrintWriter(fileWr);

            pw.println("Date Time= " + dateTime);
            ex.printStackTrace(pw);
            pw.println();

            /*
             StackTraceElement[] arrStackTraceElement=ex.getStackTrace();
             for(int cnt=0;cnt<arrStackTraceElement.length;cnt++)
             {
             pw.println(arrStackTraceElement[cnt]);
             }*/
            pw.flush();
            pw.close();
            
            
           //objUtility2.funSendErrorLogFileOnly(logFile);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
