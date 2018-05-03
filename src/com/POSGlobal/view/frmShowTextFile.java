package com.POSGlobal.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsPosConfigFile;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.controller.clsUtility2;
import java.io.File;
import java.io.FileInputStream;
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
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.swing.JOptionPane;

public class frmShowTextFile extends javax.swing.JFrame
{

    private String transType, formName, KOTPrinterName;
    private boolean flgReprintKOT;
    private File fName;
    private String printerName;
    private clsUtility2 objUtility2;

    /**
     *
     * @param Data
     * @param formName take form name blank for all reports except Day End
     */
    public frmShowTextFile(String Data, String formName, File file, String printerInfo)
    {
        initComponents();
        setLocationRelativeTo(null);
        this.formName = formName;
        fName = file;
        transType = formName;
        txtAreaData.setText(Data);
        btnPrint.setVisible(false);
        flgReprintKOT = false;
	
	objUtility2=new clsUtility2();

        String[] spPrinterInfo = printerInfo.split("!");

        if (!printerInfo.isEmpty())
        {
            printerName = spPrinterInfo[0].split(",")[0];

            if (spPrinterInfo.length == 2)
            {
                lblPrinterName.setText(spPrinterInfo[0]);

                lblPrinterQueueStatus.setText("KOT Printed : " + spPrinterInfo[1]);
            }

            if (formName.equalsIgnoreCase("sales report"))
            {
                btnPrint.setVisible(true);
            }
            if (formName.equalsIgnoreCase("Text Sales Report"))
            {
                btnPrint.setVisible(true);
            }

            if (formName.equalsIgnoreCase("Production Order Report"))
            {
                btnPrint.setVisible(true);
            }

            if (formName.equalsIgnoreCase("AdvanceOrderFlash"))
            {
                btnPrint.setVisible(true);
            }

            if (formName.equalsIgnoreCase("Item Wise Consumption"))
            {
                btnPrint.setVisible(true);
            }

            if (formName.equalsIgnoreCase("Settlement Wise Sales"))
            {
                btnPrint.setVisible(true);
            }
//        billPrinterName=printerInfo
            funSetShortCutKeys();
        }

    }

    private void funSetShortCutKeys()
    {
        btnClose.setMnemonic('c');
        btnPrint.setMnemonic('p');
    }

    private void funPrintKOTWindows(String printerName)
    {
        String filePath = System.getProperty("user.dir");
        String filename = (filePath + "/Temp/Temp_KOT.txt");
        try
        {
            int printerIndex = 0;
            String printerStatus = "Not Found";

            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
            printerName = printerName.replaceAll("#", "\\\\");

            PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
            for (int i = 0; i < printService.length; i++)
            {
                String printerServiceName = printService[i].getName();
                if (printerName.equalsIgnoreCase(printerServiceName))
                {
                    System.out.println("Printer=" + printerName);
                    printerIndex = i;
                    printerStatus = "Found";
                    break;
                }
            }

            if (printerStatus.equals("Found"))
            {
                DocPrintJob job = printService[printerIndex].createPrintJob();
                FileInputStream fis = new FileInputStream(filename);
                DocAttributeSet das = new HashDocAttributeSet();
                Doc doc = new SimpleDoc(fis, flavor, das);
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
                        System.out.println(attributeName + " : " + attributeValue);
                    }
                }
            }
        }
        catch (Exception e)
        {

            e.printStackTrace();
            if (clsGlobalVarClass.gShowPrinterErrorMsg)
            {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error Code - TFG 01", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void funPrintReportToPrinter(String printerName, String type)
    {
        try
        {
            String filePath = System.getProperty("user.dir");
            String fileName = "/Temp";
            fileName += "/" + fName.getName();
            System.out.println("Print= " + fileName);
            fileName = filePath + fileName;
            System.out.println("Print= " + fileName);

            if ("windows".equalsIgnoreCase(clsPosConfigFile.gPrintOS) && clsGlobalVarClass.gPrintType.equalsIgnoreCase("Text File"))
            {
                //printBillWindows(type);

                PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
                DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;

                int printerIndex = 0;
                PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
                for (int i = 0; i < printService.length; i++)
                {

                    if (clsGlobalVarClass.gBillPrintPrinterPort.equalsIgnoreCase(printService[i].getName()))
                    {
                        printerIndex = i;
                        break;
                    }
                }
                DocPrintJob job = printService[printerIndex].createPrintJob();

                FileInputStream fis = new FileInputStream(fileName);
                DocAttributeSet das = new HashDocAttributeSet();
                Doc doc = new SimpleDoc(fis, flavor, das);
                job.print(doc, pras);

                new clsUtility().funInvokeSampleJasper();

            }
            else if ("linux".equalsIgnoreCase(clsPosConfigFile.gPrintOS) && clsGlobalVarClass.gPrintType.equalsIgnoreCase("Text File"))
            {
                Process process = Runtime.getRuntime().exec("lpr -P " + printerName + " " + fileName, null);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * printBillWindows() method print to Default Printer. No Parameter required
     */
    private void printBillWindows(String type)
    {
        try
        {
            String filePath = System.getProperty("user.dir");
            String filename = "";

            if (type.equalsIgnoreCase("bill"))
            {
                filename = (filePath + "/Temp/Temp_Bill.txt");
            }
            else if (type.equalsIgnoreCase("dayend"))
            {
                filename = (filePath + "/Temp/Temp_DayEndReport.txt");
            }
            else if (type.equalsIgnoreCase("Text Sales Report"))
            {
                filename = (filePath + "/Temp/Temp_ItemWiseReport.txt");
            }
            else if (type.equalsIgnoreCase("Sales Report"))
            {
                filename = (filePath + "/Temp/Temp_Bill.txt");
            }

            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
            //PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
            //PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
            //DocPrintJob job = defaultService.createPrintJob();            

            int printerIndex = 0;
            PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
            for (int i = 0; i < printService.length; i++)
            {

                if (clsGlobalVarClass.gBillPrintPrinterPort.equalsIgnoreCase(printService[i].getName()))
                {
                    printerIndex = i;
                    break;
                }
            }
            DocPrintJob job = printService[printerIndex].createPrintJob();
            FileInputStream fis = new FileInputStream(filename);
            DocAttributeSet das = new HashDocAttributeSet();
            Doc doc = new SimpleDoc(fis, flavor, das);
            job.print(doc, pras);

        }
        catch (Exception e)
        {
            //e.printStackTrace();
            if (clsGlobalVarClass.gShowPrinterErrorMsg)
            {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error Code - TFG 02", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public frmShowTextFile()
    {

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jPanel1 = new javax.swing.JPanel();
        panelBody = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnPrint = new javax.swing.JButton();
        scrollPane = new javax.swing.JScrollPane();
        txtAreaData = new javax.swing.JTextArea();
        lblPrinterName = new javax.swing.JLabel();
        lblPrinterQueueStatus = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        panelBody.setBackground(new java.awt.Color(216, 238, 254));
        panelBody.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 3, true));

        btnClose.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn1.png"))); // NOI18N
        btnClose.setText("CLOSE");
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn2.png"))); // NOI18N
        btnClose.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCloseMouseClicked(evt);
            }
        });
        btnClose.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCloseActionPerformed(evt);
            }
        });
        btnClose.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnCloseKeyPressed(evt);
            }
        });

        btnPrint.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPrint.setForeground(new java.awt.Color(255, 255, 255));
        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn1.png"))); // NOI18N
        btnPrint.setText("PRINT");
        btnPrint.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrint.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn2.png"))); // NOI18N
        btnPrint.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPrintActionPerformed(evt);
            }
        });

        txtAreaData.setEditable(false);
        txtAreaData.setColumns(20);
        txtAreaData.setFont(new java.awt.Font("Lucida Console", 0, 13)); // NOI18N
        txtAreaData.setRows(5);
        txtAreaData.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtAreaDataKeyPressed(evt);
            }
        });
        scrollPane.setViewportView(txtAreaData);

        lblPrinterName.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblPrinterName.setForeground(new java.awt.Color(51, 51, 255));

        lblPrinterQueueStatus.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblPrinterQueueStatus.setForeground(new java.awt.Color(51, 51, 255));

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addComponent(lblPrinterName, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblPrinterQueueStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnPrint, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 459, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPrinterName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPrinterQueueStatus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBody, javax.swing.GroupLayout.PREFERRED_SIZE, 517, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseClicked
        if ("DayEnd".equalsIgnoreCase(transType))
        {
            dispose();
        }
        else
        {
            dispose();
        }

    }//GEN-LAST:event_btnCloseMouseClicked

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:

        if (!printerName.trim().isEmpty())
        {
            objUtility2.funPrintFile(printerName, "", "N", fName.getAbsolutePath());	    	    
        }
        else
        {
//            if (formName.equalsIgnoreCase("sales report") || formName.equalsIgnoreCase("Text Sales Report") || formName.equalsIgnoreCase("Production Order Report") || formName.equalsIgnoreCase("AdvanceOrderFlash"))
//            {
//                funPrintReportToPrinter("POS", formName);
//            }
        }
	
	
	
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnCloseKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnCloseKeyPressed
        // TODO add your handling code here:
        // System.out.println("key code of esc=="+evt.getKeyCode());
        if (evt.getKeyCode() == 27)
        {
            this.dispose();
        }
    }//GEN-LAST:event_btnCloseKeyPressed

    private void txtAreaDataKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAreaDataKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 27)
        {
            this.dispose();
        }
    }//GEN-LAST:event_txtAreaDataKeyPressed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        if ("DayEnd".equalsIgnoreCase(transType))
        {
            dispose();
        }
        else
        {
            dispose();
        }
    }//GEN-LAST:event_btnCloseActionPerformed

    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(frmShowTextFile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(frmShowTextFile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(frmShowTextFile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(frmShowTextFile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new frmShowTextFile().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnPrint;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblPrinterName;
    private javax.swing.JLabel lblPrinterQueueStatus;
    private javax.swing.JPanel panelBody;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTextArea txtAreaData;
    // End of variables declaration//GEN-END:variables
}
