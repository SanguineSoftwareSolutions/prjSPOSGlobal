package com.POSGlobal.controller;

import java.io.IOException;
import java.sql.*;
import javax.swing.JOptionPane;

public class clsDatabaseConnection
{

    private Connection con = null;
    private int cnt;
    private String connectionURL, dbName, userId, password, ipAddress, portNo, serverName;
    static String url = "jdbc:mysql://localhost:3306/";
    static String dbName1 = "jpos";
    static String driver = "com.mysql.jdbc.Driver";
    static String userName = "root";
    static String password1 = "root";
    static String unicode = "?useUnicode=yes&characterEncoding=UTF-8";

    public clsDatabaseConnection() throws clsSPOSException
    {
        clsPosConfigFile pc = new clsPosConfigFile();
        dbName = clsPosConfigFile.databaseName;
        userId = clsPosConfigFile.userId;
        password = clsPosConfigFile.password;
        if (password.equalsIgnoreCase("root"))
        {
            throw new clsSPOSException("Access denied for root password.");
        }
        ipAddress = clsPosConfigFile.ipAddress;
        portNo = clsPosConfigFile.portNo;
        serverName = clsPosConfigFile.serverName;
        connectionURL = "jdbc:mysql://" + ipAddress + ":" + portNo + "/" + dbName + unicode;
        //connectionURL="jdbc:mysql://localhost:3306/jpos?user=root&password=root";
    }

    public void open(String dbType) throws Exception
    {
        if (dbType.equalsIgnoreCase("sqlite"))
        {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:/root/POS.db");
        }
        if (dbType.equalsIgnoreCase("mysql"))
        {
            try
            {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(connectionURL, userId, password);

                clsGlobalVarClass.conJasper = this.con;
                clsGlobalVarClass.conPrepareStatement = this.con;

                //con = DriverManager.getConnection(url+dbName1,userName,password1);
                //con = DriverManager.getConnection(connectionURL);
            }
            catch (Exception e)
            {

                e.printStackTrace();
                if (e.getMessage().startsWith("Communications link failure"))
                {

                    JOptionPane.showMessageDialog(null, "Unable To Connect IP Address."
                            + "\nPlease Check IP " + ipAddress, "Communications Link Failure", JOptionPane.ERROR_MESSAGE);
                    new clsUtility().funWriteErrorLog(e);

                    funPingToServer();

                    throw e;
                }
                else if (e.getMessage().startsWith("Unknown database"))
                {
                    JOptionPane.showMessageDialog(null, "Unable To Connect Database."
                            + "\nPlease Check Database " + dbName + " On IP " + ipAddress, e.getMessage(), JOptionPane.ERROR_MESSAGE);
                    new clsUtility().funWriteErrorLog(e);

                    throw e;
                }
                else if (e.getMessage().startsWith("Access denied for user"))
                {
                    JOptionPane.showMessageDialog(null, e.getMessage(), e.getMessage(), JOptionPane.ERROR_MESSAGE);
                    new clsUtility().funWriteErrorLog(e);

                    throw e;
                }
            }

        }
        if (dbType.equalsIgnoreCase("mysql1"))
        {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/jpos?user=root&password=root");
        }
    }

    private void funPingToServer() throws IOException
    {
        if (clsPosConfigFile.gPrintOS.equalsIgnoreCase("Windows"))
        {
            String[] command =
            {
                "cmd", "/K", "start", "ping", ipAddress
            };

            String[] command2 =
            {
                "cmd", "/K", "start", "cmd", "/K", "ping", ipAddress
            };

            Process process = Runtime.getRuntime().exec(command2);
        }
    }

    public int execute(String sql) throws Exception
    {
        Statement st = con.createStatement();
        cnt = st.executeUpdate(sql);
        return cnt;
    }

    public ResultSet executeResultSet(String sql) throws Exception
    {
        ResultSet rs = null;
        Statement st = con.createStatement();
        rs = st.executeQuery(sql);
        return rs;
    }

    public void funStartTransaction()
    {
        try
        {
            con.setAutoCommit(false);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void funRollbackTransaction()
    {
        try
        {
            con.rollback();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void funCommitTransaction()
    {
        try
        {
            con.commit();
            //  System.out.println("Committed");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void close() throws Exception
    {
        con.close();
    }

    public static void main(String args[])
    {
        try
        {
            clsDatabaseConnection db = new clsDatabaseConnection();
            db.open("mysql");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
