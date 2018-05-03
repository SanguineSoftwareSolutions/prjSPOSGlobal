package com.POSGlobal.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class frmShortCutKeys extends javax.swing.JFrame
{
    private final StringBuilder sql;

    public frmShortCutKeys()
    {
        initComponents();
        funSetShortCutKeys();
        Timer timer = new Timer(500, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Date date1 = new Date();
                String new_str = String.format("%tr", date1);
                String dateAndTime = clsGlobalVarClass.gPOSDateToDisplay + " " + new_str;
                lblDate.setText(dateAndTime);
            }
        });
        timer.setRepeats(true);
        timer.setCoalesce(true);
        timer.setInitialDelay(0);
        timer.start();

        lblUserCode.setText(clsGlobalVarClass.gUserCode);
        lblPosName.setText(clsGlobalVarClass.gPOSName);
        lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
        lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
        sql = new StringBuilder();

        funSetMastersCmb();
        funSetTransactionsCmb();
        funSetReportsCmb();
    }
    
    private JComboBox[] funInitJComboBoxForTrans()
    {
        JComboBox[] arrCmbTransactions = {cmbTransactionF1,cmbTransactionF2,cmbTransactionF3,cmbTransactionF4,cmbTransactionF5,cmbTransactionF6,cmbTransactionF7,cmbTransactionF8,cmbTransactionF9,cmbTransactionF10,cmbTransactionF11,cmbTransactionF12};
        return arrCmbTransactions;
    }
    
    private JComboBox[] funInitJComboBoxForMasters()
    {   
        JComboBox[] arrCmbMasters = {cmbMasterF1,cmbMasterF2,cmbMasterF3,cmbMasterF4,cmbMasterF5,cmbMasterF6,cmbMasterF7,cmbMasterF8,cmbMasterF9,cmbMasterF10,cmbMasterF11,cmbMasterF12};
        return arrCmbMasters;
    }
    
    private JComboBox[] funInitJComboBoxForReports()
    {
        JComboBox[] arrCmbReports = {cmbReportsF1,cmbReportsF2,cmbReportsF3,cmbReportsF4,cmbReportsF5,cmbReportsF6,cmbReportsF7,cmbReportsF8,cmbReportsF9,cmbReportsF10,cmbReportsF11,cmbReportsF12};
        return arrCmbReports;
    }
    
    private void funSetShortCutKeys()
    {
        btnCancel.setMnemonic('c');
        btnSave.setMnemonic('r');
    }
    
    private void funSetSelectedModule(String moduleType, JComboBox[] arrCmb) throws Exception
    {
        sql.setLength(0);
        sql.append("select strModuleName from tblshortcutkeysetup "
            + " where strModuleType='"+moduleType+"' order by strShortcutKey");
        ResultSet rs=clsGlobalVarClass.dbMysql.executeResultSet(sql.toString());
        int i=0;
        while(rs.next())
        {
            arrCmb[i].setSelectedItem(rs.getString(1));
            i++;
        }
        rs.close();
    }
    
    private void funSetMastersCmb()
    {
        try
        {
            sql.setLength(0);
            sql.append("select a.strModuleName from tblforms a "
                + " where a.strModuleType='M' ");

            ResultSet resultset = clsGlobalVarClass.dbMysql.executeResultSet(sql.toString());
            while (resultset.next())
            {
                cmbMasterF1.addItem(resultset.getString("strModuleName"));
                cmbMasterF2.addItem(resultset.getString("strModuleName"));
                cmbMasterF3.addItem(resultset.getString("strModuleName"));
                cmbMasterF4.addItem(resultset.getString("strModuleName"));
                cmbMasterF5.addItem(resultset.getString("strModuleName"));
                cmbMasterF6.addItem(resultset.getString("strModuleName"));
                cmbMasterF7.addItem(resultset.getString("strModuleName"));
                cmbMasterF8.addItem(resultset.getString("strModuleName"));
                cmbMasterF9.addItem(resultset.getString("strModuleName"));
                cmbMasterF10.addItem(resultset.getString("strModuleName"));
                cmbMasterF11.addItem(resultset.getString("strModuleName"));
                cmbMasterF12.addItem(resultset.getString("strModuleName"));
            }
            resultset.close();
            funSetSelectedModule("M",funInitJComboBoxForMasters());
        }
        catch (Exception e)
        {
            clsGlobalVarClass.gLog.error(e);
        }
    }

    private void funSetTransactionsCmb()
    {
        try
        {
            sql.setLength(0);
            sql.append("select a.strModuleName from tblforms a "
                + " where a.strModuleType='T' ");
            ResultSet resultset = clsGlobalVarClass.dbMysql.executeResultSet(sql.toString());
            while (resultset.next())
            {
                cmbTransactionF1.addItem(resultset.getString("strModuleName"));
                cmbTransactionF2.addItem(resultset.getString("strModuleName"));
                cmbTransactionF3.addItem(resultset.getString("strModuleName"));
                cmbTransactionF4.addItem(resultset.getString("strModuleName"));
                cmbTransactionF5.addItem(resultset.getString("strModuleName"));
                cmbTransactionF6.addItem(resultset.getString("strModuleName"));
                cmbTransactionF7.addItem(resultset.getString("strModuleName"));
                cmbTransactionF8.addItem(resultset.getString("strModuleName"));
                cmbTransactionF9.addItem(resultset.getString("strModuleName"));
                cmbTransactionF10.addItem(resultset.getString("strModuleName"));
                cmbTransactionF11.addItem(resultset.getString("strModuleName"));
                cmbTransactionF12.addItem(resultset.getString("strModuleName"));
            }
            resultset.close();
            funSetSelectedModule("T",funInitJComboBoxForTrans());
        }
        catch (Exception e)
        {
            clsGlobalVarClass.gLog.error(e);
        }
    }

    private void funSetReportsCmb()
    {
        try
        {
            sql.setLength(0);
            sql.append("select a.strModuleName from tblforms a "
                + " where a.strModuleType='R' ");
            ResultSet resultset = clsGlobalVarClass.dbMysql.executeResultSet(sql.toString());
            while (resultset.next())
            {
                cmbReportsF1.addItem(resultset.getString("strModuleName"));
                cmbReportsF2.addItem(resultset.getString("strModuleName"));
                cmbReportsF3.addItem(resultset.getString("strModuleName"));
                cmbReportsF4.addItem(resultset.getString("strModuleName"));
                cmbReportsF5.addItem(resultset.getString("strModuleName"));
                cmbReportsF6.addItem(resultset.getString("strModuleName"));
                cmbReportsF7.addItem(resultset.getString("strModuleName"));
                cmbReportsF8.addItem(resultset.getString("strModuleName"));
                cmbReportsF9.addItem(resultset.getString("strModuleName"));
                cmbReportsF10.addItem(resultset.getString("strModuleName"));
                cmbReportsF11.addItem(resultset.getString("strModuleName"));
                cmbReportsF12.addItem(resultset.getString("strModuleName"));
            }
            resultset.close();
            funSetSelectedModule("R",funInitJComboBoxForReports());
        }
        catch (Exception e)
        {
            clsGlobalVarClass.gLog.error(e);
        }
    }

    private void funSetMastersShotcutKeys()
    {
        try
        {
            String masterSql = "insert into tblshortcutkeysetup(strShortcutKey,strModuleName,strModuleType)values";
            for (int i = 1; i <= 12; i++)
            {
                switch (i)
                {
                    case 1:
                        masterSql = masterSql + "('112','" + cmbMasterF1.getSelectedItem().toString() + "','M') ";
                        break;
                    case 2:
                        masterSql = masterSql + ",('113','" + cmbMasterF2.getSelectedItem().toString() + "','M') ";
                        break;
                    case 3:
                        masterSql = masterSql + ",('114','" + cmbMasterF3.getSelectedItem().toString() + "','M') ";
                        break;
                    case 4:
                        masterSql = masterSql + ",('115','" + cmbMasterF4.getSelectedItem().toString() + "','M') ";
                        break;
                    case 5:
                        masterSql = masterSql + ",('116','" + cmbMasterF5.getSelectedItem().toString() + "','M') ";
                        break;
                    case 6:
                        masterSql = masterSql + ",('117','" + cmbMasterF6.getSelectedItem().toString() + "','M') ";
                        break;
                    case 7:
                        masterSql = masterSql + ",('118','" + cmbMasterF7.getSelectedItem().toString() + "','M') ";
                        break;
                    case 8:
                        masterSql = masterSql + ",('119','" + cmbMasterF8.getSelectedItem().toString() + "','M') ";
                        break;
                    case 9:
                        masterSql = masterSql + ",('120','" + cmbMasterF9.getSelectedItem().toString() + "','M') ";
                        break;
                    case 10:
                        masterSql = masterSql + ",('121','" + cmbMasterF10.getSelectedItem().toString() + "','M') ";
                        break;
                    case 11:
                        masterSql = masterSql + ",('122','" + cmbMasterF11.getSelectedItem().toString() + "','M') ";
                        break;
                    case 12:
                        masterSql = masterSql + ",('123','" + cmbMasterF12.getSelectedItem().toString() + "','M') ";
                        break;
                }
            }

            int i = clsGlobalVarClass.dbMysql.execute(masterSql);
            if (i > 0)
            {
                funSetTransactionsShotcutKeys();
            }
        }
        catch (Exception e)
        {
            clsGlobalVarClass.gLog.error(e);
        }
    }

    private void funSetTransactionsShotcutKeys()
    {
        try
        {
            String transactionSql = "insert into tblshortcutkeysetup(strShortcutKey,strModuleName,strModuleType)values";
            for (int i = 1; i <= 12; i++)
            {
                switch (i)
                {
                    case 1:
                        transactionSql = transactionSql + "('112','" + cmbTransactionF1.getSelectedItem().toString() + "','T') ";
                        break;
                    case 2:
                        transactionSql = transactionSql + ",('113','" + cmbTransactionF2.getSelectedItem().toString() + "','T') ";
                        break;
                    case 3:
                        transactionSql = transactionSql + ",('114','" + cmbTransactionF3.getSelectedItem().toString() + "','T') ";
                        break;
                    case 4:
                        transactionSql = transactionSql + ",('115','" + cmbTransactionF4.getSelectedItem().toString() + "','T') ";
                        break;
                    case 5:
                        transactionSql = transactionSql + ",('116','" + cmbTransactionF5.getSelectedItem().toString() + "','T') ";
                        break;
                    case 6:
                        transactionSql = transactionSql + ",('117','" + cmbTransactionF6.getSelectedItem().toString() + "','T') ";
                        break;
                    case 7:
                        transactionSql = transactionSql + ",('118','" + cmbTransactionF7.getSelectedItem().toString() + "','T') ";
                        break;
                    case 8:
                        transactionSql = transactionSql + ",('119','" + cmbTransactionF8.getSelectedItem().toString() + "','T') ";
                        break;
                    case 9:
                        transactionSql = transactionSql + ",('120','" + cmbTransactionF9.getSelectedItem().toString() + "','T') ";
                        break;
                    case 10:
                        transactionSql = transactionSql + ",('121','" + cmbTransactionF10.getSelectedItem().toString() + "','T') ";
                        break;
                    case 11:
                        transactionSql = transactionSql + ",('122','" + cmbTransactionF11.getSelectedItem().toString() + "','T') ";
                        break;
                    case 12:
                        transactionSql = transactionSql + ",('123','" + cmbTransactionF12.getSelectedItem().toString() + "','T') ";
                        break;
                }
            }
            //System.out.println("transactionSql="+transactionSql);
            int i = clsGlobalVarClass.dbMysql.execute(transactionSql);

            if (i > 0)
            {
                funSetReportsShotcutKeys();
            }
        }
        catch (Exception e)
        {
            clsGlobalVarClass.gLog.error(e);
        }
    }

    private void funSetReportsShotcutKeys()
    {
        try
        {
            String reportSql = "insert into tblshortcutkeysetup(strShortcutKey,strModuleName,strModuleType)values";
            for (int i = 1; i <= 12; i++)
            {
                switch (i)
                {
                    case 1:
                        reportSql = reportSql + "('112','" + cmbReportsF1.getSelectedItem().toString() + "','R') ";
                        break;
                    case 2:
                        reportSql = reportSql + ",('113','" + cmbReportsF2.getSelectedItem().toString() + "','R') ";
                        break;
                    case 3:
                        reportSql = reportSql + ",('114','" + cmbReportsF3.getSelectedItem().toString() + "','R') ";
                        break;
                    case 4:
                        reportSql = reportSql + ",('115','" + cmbReportsF4.getSelectedItem().toString() + "','R') ";
                        break;
                    case 5:
                        reportSql = reportSql + ",('116','" + cmbReportsF5.getSelectedItem().toString() + "','R') ";
                        break;
                    case 6:
                        reportSql = reportSql + ",('117','" + cmbReportsF6.getSelectedItem().toString() + "','R') ";
                        break;
                    case 7:
                        reportSql = reportSql + ",('118','" + cmbReportsF7.getSelectedItem().toString() + "','R') ";
                        break;
                    case 8:
                        reportSql = reportSql + ",('119','" + cmbReportsF8.getSelectedItem().toString() + "','R') ";
                        break;
                    case 9:
                        reportSql = reportSql + ",('120','" + cmbReportsF9.getSelectedItem().toString() + "','R') ";
                        break;
                    case 10:
                        reportSql = reportSql + ",('121','" + cmbReportsF10.getSelectedItem().toString() + "','R') ";
                        break;
                    case 11:
                        reportSql = reportSql + ",('122','" + cmbReportsF11.getSelectedItem().toString() + "','R') ";
                        break;
                    case 12:
                        reportSql = reportSql + ",('123','" + cmbReportsF12.getSelectedItem().toString() + "','R') ";
                        break;
                }
            }

            //System.out.println("reportSql="+reportSql);
            int i = clsGlobalVarClass.dbMysql.execute(reportSql);
            if (i > 0)
            {
                JOptionPane.showMessageDialog(null, "Shortcut Keys Updated Successfully.");
            }
        }
        catch (Exception e)
        {
            clsGlobalVarClass.gLog.error(e);
        }
    }

    private void funClearOldData()
    {
        try
        {
            clsGlobalVarClass.dbMysql.execute("delete from tblshortcutkeysetup");
        }
        catch (Exception e)
        {
            clsGlobalVarClass.gLog.error(e);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

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
        panelLayout = new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSGlobal/images/imgBGJPOS.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  ;
        panelBody = new javax.swing.JPanel();
        btnSave = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblKeys = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblMasters = new javax.swing.JLabel();
        lblTransactions = new javax.swing.JLabel();
        lblReports = new javax.swing.JLabel();
        cmbMasterF1 = new javax.swing.JComboBox();
        cmbTransactionF1 = new javax.swing.JComboBox();
        cmbReportsF1 = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        cmbMasterF2 = new javax.swing.JComboBox();
        cmbTransactionF2 = new javax.swing.JComboBox();
        cmbReportsF2 = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        cmbMasterF3 = new javax.swing.JComboBox();
        cmbTransactionF3 = new javax.swing.JComboBox();
        cmbReportsF3 = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        cmbMasterF4 = new javax.swing.JComboBox();
        cmbTransactionF4 = new javax.swing.JComboBox();
        cmbReportsF4 = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        cmbMasterF5 = new javax.swing.JComboBox();
        cmbTransactionF5 = new javax.swing.JComboBox();
        cmbReportsF5 = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        cmbMasterF6 = new javax.swing.JComboBox();
        cmbTransactionF6 = new javax.swing.JComboBox();
        cmbReportsF6 = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        cmbMasterF7 = new javax.swing.JComboBox();
        cmbTransactionF7 = new javax.swing.JComboBox();
        cmbReportsF7 = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        cmbMasterF8 = new javax.swing.JComboBox();
        cmbTransactionF8 = new javax.swing.JComboBox();
        cmbReportsF8 = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        cmbMasterF9 = new javax.swing.JComboBox();
        cmbTransactionF9 = new javax.swing.JComboBox();
        cmbReportsF9 = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        cmbMasterF10 = new javax.swing.JComboBox();
        cmbTransactionF10 = new javax.swing.JComboBox();
        cmbReportsF10 = new javax.swing.JComboBox();
        cmbReportsF11 = new javax.swing.JComboBox();
        cmbTransactionF11 = new javax.swing.JComboBox();
        cmbMasterF11 = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        cmbMasterF12 = new javax.swing.JComboBox();
        cmbTransactionF12 = new javax.swing.JComboBox();
        cmbReportsF12 = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setMinimumSize(new java.awt.Dimension(800, 600));
        setUndecorated(true);

        panelHeader.setBackground(new java.awt.Color(69, 164, 238));
        panelHeader.setLayout(new javax.swing.BoxLayout(panelHeader, javax.swing.BoxLayout.LINE_AXIS));

        lblProductName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblProductName.setForeground(new java.awt.Color(255, 255, 255));
        lblProductName.setText("SPOS - ");
        panelHeader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("- Shortcut Keys Setup");
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

        panelLayout.setBackground(new java.awt.Color(255, 255, 255));
        panelLayout.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelLayout.setMinimumSize(new java.awt.Dimension(800, 570));
        panelLayout.setOpaque(false);
        panelLayout.setPreferredSize(new java.awt.Dimension(800, 570));
        panelLayout.setLayout(new java.awt.GridBagLayout());

        panelBody.setBackground(new java.awt.Color(255, 255, 255));
        panelBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelBody.setMinimumSize(new java.awt.Dimension(800, 600));
        panelBody.setOpaque(false);

        btnSave.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn1.png"))); // NOI18N
        btnSave.setText("SAVE");
        btnSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSave.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn2.png"))); // NOI18N
        btnSave.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnSaveMouseClicked(evt);
            }
        });
        btnSave.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSaveActionPerformed(evt);
            }
        });
        btnSave.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnSaveKeyPressed(evt);
            }
        });

        btnCancel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn1.png"))); // NOI18N
        btnCancel.setText("CLOSE");
        btnCancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancel.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn2.png"))); // NOI18N
        btnCancel.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCancelMouseClicked(evt);
            }
        });
        btnCancel.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCancelActionPerformed(evt);
            }
        });

        lblKeys.setFont(new java.awt.Font("Trebuchet MS", 2, 16)); // NOI18N
        lblKeys.setForeground(new java.awt.Color(0, 102, 255));
        lblKeys.setText("Keys");

        jLabel2.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel2.setText("F1");

        lblMasters.setFont(new java.awt.Font("Trebuchet MS", 2, 16)); // NOI18N
        lblMasters.setForeground(new java.awt.Color(0, 102, 255));
        lblMasters.setText("Masters");

        lblTransactions.setFont(new java.awt.Font("Trebuchet MS", 2, 16)); // NOI18N
        lblTransactions.setForeground(new java.awt.Color(0, 102, 255));
        lblTransactions.setText("Transactions");

        lblReports.setFont(new java.awt.Font("Trebuchet MS", 2, 16)); // NOI18N
        lblReports.setForeground(new java.awt.Color(0, 102, 255));
        lblReports.setText("Reports");

        cmbMasterF1.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N

        cmbTransactionF1.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N

        cmbReportsF1.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel3.setText("F2");

        cmbMasterF2.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N

        cmbTransactionF2.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N

        cmbReportsF2.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel4.setText("F3");

        cmbMasterF3.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N

        cmbTransactionF3.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N

        cmbReportsF3.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel5.setText("F4");

        cmbMasterF4.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N

        cmbTransactionF4.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N

        cmbReportsF4.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel6.setText("F5");

        cmbMasterF5.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N

        cmbTransactionF5.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N

        cmbReportsF5.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel7.setText("F6");

        cmbMasterF6.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N

        cmbTransactionF6.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N

        cmbReportsF6.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel8.setText("F7");

        cmbMasterF7.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N

        cmbTransactionF7.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N

        cmbReportsF7.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel9.setText("F8");

        cmbMasterF8.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N

        cmbTransactionF8.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N

        cmbReportsF8.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N

        jLabel10.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel10.setText("F9");

        cmbMasterF9.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        cmbMasterF9.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbMasterF9ActionPerformed(evt);
            }
        });

        cmbTransactionF9.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N

        cmbReportsF9.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N

        jLabel11.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel11.setText("F10");

        cmbMasterF10.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N

        cmbTransactionF10.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N

        cmbReportsF10.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N

        cmbReportsF11.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N

        cmbTransactionF11.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N

        cmbMasterF11.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N

        jLabel12.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel12.setText("F11");

        jLabel13.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel13.setText("F12");

        cmbMasterF12.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N

        cmbTransactionF12.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N

        cmbReportsF12.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelBodyLayout.createSequentialGroup()
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(cmbMasterF5, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(cmbTransactionF5, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(cmbReportsF5, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelBodyLayout.createSequentialGroup()
                            .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(panelBodyLayout.createSequentialGroup()
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(cmbMasterF12, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(panelBodyLayout.createSequentialGroup()
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(cmbMasterF9, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(10, 10, 10)
                            .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(panelBodyLayout.createSequentialGroup()
                                    .addComponent(cmbTransactionF12, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(cmbReportsF12, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(panelBodyLayout.createSequentialGroup()
                                    .addComponent(cmbTransactionF9, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(cmbReportsF9, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(panelBodyLayout.createSequentialGroup()
                            .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cmbMasterF4, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbTransactionF4, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbReportsF4, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblKeys)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)))
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addGap(79, 79, 79)
                                .addComponent(lblMasters)
                                .addGap(161, 161, 161)
                                .addComponent(lblTransactions)
                                .addGap(153, 153, 153)
                                .addComponent(lblReports))
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addComponent(cmbMasterF1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(9, 9, 9)
                                .addComponent(cmbTransactionF1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmbReportsF1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cmbMasterF2, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)
                        .addComponent(cmbTransactionF2, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbReportsF2, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cmbMasterF3, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbTransactionF3, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbReportsF3, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cmbMasterF6, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbTransactionF6, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(cmbReportsF6, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cmbMasterF7, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbTransactionF7, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbReportsF7, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cmbMasterF8, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbTransactionF8, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbReportsF8, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(panelBodyLayout.createSequentialGroup()
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(cmbMasterF10, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(460, 460, 460))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelBodyLayout.createSequentialGroup()
                            .addGap(273, 273, 273)
                            .addComponent(cmbTransactionF10, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(10, 10, 10)
                            .addComponent(cmbReportsF10, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cmbMasterF11, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(cmbTransactionF11, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbReportsF11, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        panelBodyLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cmbMasterF1, cmbMasterF10, cmbMasterF11, cmbMasterF12, cmbMasterF2, cmbMasterF3, cmbMasterF4, cmbMasterF5, cmbMasterF6, cmbMasterF7, cmbMasterF8, cmbMasterF9, cmbReportsF1, cmbReportsF10, cmbReportsF2, cmbReportsF3, cmbReportsF4, cmbReportsF5, cmbReportsF6, cmbReportsF7, cmbReportsF8, cmbReportsF9, cmbTransactionF1, cmbTransactionF10, cmbTransactionF11, cmbTransactionF12, cmbTransactionF2, cmbTransactionF3, cmbTransactionF4, cmbTransactionF5, cmbTransactionF6, cmbTransactionF7, cmbTransactionF8, cmbTransactionF9});

        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblKeys, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMasters, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTransactions, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblReports, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbMasterF1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbTransactionF1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(cmbReportsF1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cmbTransactionF2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cmbMasterF2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cmbReportsF2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2, 2, 2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cmbMasterF3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbTransactionF3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbReportsF3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cmbMasterF4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbTransactionF4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbReportsF4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbMasterF5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbTransactionF5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbReportsF5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbMasterF6, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbTransactionF6, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbReportsF6, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cmbMasterF7, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                        .addComponent(cmbTransactionF7, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbReportsF7, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cmbMasterF8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbTransactionF8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbReportsF8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cmbMasterF9, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbTransactionF9, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbReportsF9, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cmbReportsF10, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbTransactionF10, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cmbMasterF10, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2, 2, 2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addComponent(cmbTransactionF11, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2))
                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                            .addComponent(cmbReportsF11, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(2, 2, 2))
                        .addComponent(cmbMasterF11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(1, 1, 1))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmbReportsF12, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cmbTransactionF12, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbMasterF12, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(45, Short.MAX_VALUE))
        );

        panelBodyLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cmbMasterF1, cmbMasterF10, cmbMasterF11, cmbMasterF12, cmbMasterF2, cmbMasterF3, cmbMasterF4, cmbMasterF5, cmbMasterF6, cmbMasterF7, cmbMasterF8, cmbMasterF9, cmbReportsF1, cmbReportsF10, cmbReportsF11, cmbReportsF12, cmbReportsF2, cmbReportsF3, cmbReportsF4, cmbReportsF5, cmbReportsF6, cmbReportsF7, cmbReportsF8, cmbReportsF9, cmbTransactionF1, cmbTransactionF10, cmbTransactionF11, cmbTransactionF12, cmbTransactionF2, cmbTransactionF3, cmbTransactionF4, cmbTransactionF5, cmbTransactionF6, cmbTransactionF7, cmbTransactionF8, cmbTransactionF9});

        panelLayout.add(panelBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked

    }//GEN-LAST:event_btnCancelMouseClicked

    private void btnSaveKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSaveKeyPressed
        
    }//GEN-LAST:event_btnSaveKeyPressed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        funClearOldData();
        funSetMastersShotcutKeys();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSaveMouseClicked

    }//GEN-LAST:event_btnSaveMouseClicked

    private void cmbMasterF9ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbMasterF9ActionPerformed
    {//GEN-HEADEREND:event_cmbMasterF9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbMasterF9ActionPerformed

    public static void main(String args[])
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch (ClassNotFoundException ex)
        {
            clsGlobalVarClass.gLog.error(ex);
        }
        catch (InstantiationException ex)
        {
            clsGlobalVarClass.gLog.error(ex);
        }
        catch (IllegalAccessException ex)
        {
            clsGlobalVarClass.gLog.error(ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            clsGlobalVarClass.gLog.error(ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new frmShortCutKeys().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox cmbMasterF1;
    private javax.swing.JComboBox cmbMasterF10;
    private javax.swing.JComboBox cmbMasterF11;
    private javax.swing.JComboBox cmbMasterF12;
    private javax.swing.JComboBox cmbMasterF2;
    private javax.swing.JComboBox cmbMasterF3;
    private javax.swing.JComboBox cmbMasterF4;
    private javax.swing.JComboBox cmbMasterF5;
    private javax.swing.JComboBox cmbMasterF6;
    private javax.swing.JComboBox cmbMasterF7;
    private javax.swing.JComboBox cmbMasterF8;
    private javax.swing.JComboBox cmbMasterF9;
    private javax.swing.JComboBox cmbReportsF1;
    private javax.swing.JComboBox cmbReportsF10;
    private javax.swing.JComboBox cmbReportsF11;
    private javax.swing.JComboBox cmbReportsF12;
    private javax.swing.JComboBox cmbReportsF2;
    private javax.swing.JComboBox cmbReportsF3;
    private javax.swing.JComboBox cmbReportsF4;
    private javax.swing.JComboBox cmbReportsF5;
    private javax.swing.JComboBox cmbReportsF6;
    private javax.swing.JComboBox cmbReportsF7;
    private javax.swing.JComboBox cmbReportsF8;
    private javax.swing.JComboBox cmbReportsF9;
    private javax.swing.JComboBox cmbTransactionF1;
    private javax.swing.JComboBox cmbTransactionF10;
    private javax.swing.JComboBox cmbTransactionF11;
    private javax.swing.JComboBox cmbTransactionF12;
    private javax.swing.JComboBox cmbTransactionF2;
    private javax.swing.JComboBox cmbTransactionF3;
    private javax.swing.JComboBox cmbTransactionF4;
    private javax.swing.JComboBox cmbTransactionF5;
    private javax.swing.JComboBox cmbTransactionF6;
    private javax.swing.JComboBox cmbTransactionF7;
    private javax.swing.JComboBox cmbTransactionF8;
    private javax.swing.JComboBox cmbTransactionF9;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblKeys;
    private javax.swing.JLabel lblMasters;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblReports;
    private javax.swing.JLabel lblTransactions;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    // End of variables declaration//GEN-END:variables

    
}
