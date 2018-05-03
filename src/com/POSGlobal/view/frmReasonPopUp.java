package com.POSGlobal.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUtility2;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class frmReasonPopUp extends javax.swing.JDialog
{

    private int result, retResult;
    private String reasonCode="",reasonName="",reasons="";
    private String userName, password, frmName = "";
    private clsUtility2 objUtility2 = new clsUtility2();
    private Map<String, String> hmReasons;

    /**
     * Creates new form FrmOkPopUp
     */
    public frmReasonPopUp(java.awt.Frame parent, String message, String formName)
    {

        super(parent, message, true);
        frmName = formName;
//        Dimension sz = Toolkit.getDefaultToolkit().getScreenSize();
        initComponents();
//         
//        setBounds(sz.width/3-20,sz.height/3,576, 158);        
//        lblMessage.setText(message);
        setAlwaysOnTop(true);
        setLocationRelativeTo(null);
        funSetShortCutKeys();
        hmReasons = new HashMap<String, String>();
        funLoadReason();
    }

    private void funSetShortCutKeys()
    {
        btnYes.setMnemonic('y');
        

    }
   public String getResult()
    {
        try
        {
            reasonName = cmdReasonName.getSelectedItem().toString();
            reasonCode=hmReasons.get(cmdReasonName.getSelectedItem().toString());
            
            reasons = reasonName+","+reasonCode;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return reasons;
    }

    public void funLoadReason()
    {
       try
       {
           String sql="select a.strReasonCode,a.strReasonName from tblreasonmaster a" ;
            ResultSet rssql = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rssql.next())
            {
                hmReasons.put(rssql.getString(2), rssql.getString(1));
                cmdReasonName.addItem(rssql.getString(2));
            }
            rssql.close();
           
       }
       catch(Exception e)
       {
           e.printStackTrace();
       }
        

    }    
    

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        panelBody = new javax.swing.JPanel();
        btnYes = new javax.swing.JButton();
        lblReasonname = new javax.swing.JLabel();
        cmdReasonName = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(null);
        setUndecorated(true);
        setResizable(false);

        panelBody.setBackground(new java.awt.Color(153, 204, 255));
        panelBody.setPreferredSize(new java.awt.Dimension(600, 179));
        panelBody.setLayout(null);

        btnYes.setFont(new java.awt.Font("Tahoma", 1, 17)); // NOI18N
        btnYes.setForeground(new java.awt.Color(248, 248, 246));
        btnYes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn1.png"))); // NOI18N
        btnYes.setText("Ok");
        btnYes.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnYes.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn2.png"))); // NOI18N
        btnYes.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnYesActionPerformed(evt);
            }
        });
        btnYes.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnYesKeyPressed(evt);
            }
        });
        panelBody.add(btnYes);
        btnYes.setBounds(240, 50, 90, 40);

        lblReasonname.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblReasonname.setText("<html>Reason Name:</html>");
        panelBody.add(lblReasonname);
        lblReasonname.setBounds(10, 10, 90, 30);

        panelBody.add(cmdReasonName);
        cmdReasonName.setBounds(110, 10, 220, 30);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBody, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBody, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnYesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnYesActionPerformed
        // TODO add your handling code here:
        funOKButtonClicked();
    }//GEN-LAST:event_btnYesActionPerformed

    private void btnYesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnYesKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            funOKButtonClicked();
        }
    }//GEN-LAST:event_btnYesKeyPressed
    public static void main(String[] args)
    {
        new frmReasonPopUp(null, "dsggggggggggggggggggggggggg", "").setVisible(true);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnYes;
    private javax.swing.JComboBox cmdReasonName;
    private javax.swing.JLabel lblReasonname;
    private javax.swing.JPanel panelBody;
    // End of variables declaration//GEN-END:variables

    private void funOKButtonClicked()
    {
      
        result = 1;
        this.dispose();
        
    }

}
