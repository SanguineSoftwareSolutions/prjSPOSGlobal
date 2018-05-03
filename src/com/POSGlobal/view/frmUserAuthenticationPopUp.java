package com.POSGlobal.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUtility2;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.ResultSet;

public class frmUserAuthenticationPopUp extends javax.swing.JDialog
{

    private int result, retResult;
    private String userName, password, frmName = "";
    private clsUtility2 objUtility2 = new clsUtility2();

    /**
     * Creates new form FrmOkPopUp
     */
    public frmUserAuthenticationPopUp(java.awt.Frame parent, String message, String formName)
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
    }

    private void funSetShortCutKeys()
    {
        btnYes.setMnemonic('y');
        btnNo.setMnemonic('n');

    }

    public String getUserName()
    {
        try
        {
            userName = txtUsername.getText();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            return userName;
        }
    }

    public String getPassword()
    {
        try
        {
            password = txtPassword.getText();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            return password;
        }
    }

    public int getResult()
    {
        try
        {
            retResult = result;
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        panelBody = new javax.swing.JPanel();
        btnYes = new javax.swing.JButton();
        btnNo = new javax.swing.JButton();
        lblUsername = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();

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
        btnYes.setBounds(90, 50, 90, 40);

        btnNo.setFont(new java.awt.Font("Tahoma", 1, 17)); // NOI18N
        btnNo.setForeground(new java.awt.Color(248, 248, 246));
        btnNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCommonBtn1.png"))); // NOI18N
        btnNo.setText("Cancel");
        btnNo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNo.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn2.png"))); // NOI18N
        btnNo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNoActionPerformed(evt);
            }
        });
        panelBody.add(btnNo);
        btnNo.setBounds(240, 50, 90, 40);

        lblUsername.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblUsername.setText("<html>User Cd /<br> Card Swipe</html>");
        panelBody.add(lblUsername);
        lblUsername.setBounds(0, 0, 90, 50);

        txtUsername.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtUsernameMouseClicked(evt);
            }
        });
        txtUsername.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtUsernameActionPerformed(evt);
            }
        });
        txtUsername.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtUsernameKeyPressed(evt);
            }
        });
        panelBody.add(txtUsername);
        txtUsername.setBounds(90, 10, 180, 30);

        txtPassword.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtPasswordMouseClicked(evt);
            }
        });
        txtPassword.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtPasswordKeyPressed(evt);
            }
        });
        panelBody.add(txtPassword);
        txtPassword.setBounds(280, 10, 50, 30);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBody, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBody, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNoActionPerformed
        // TODO add your handling code here:
        result = 0;
        this.dispose();
    }//GEN-LAST:event_btnNoActionPerformed

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

    private void txtUsernameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtUsernameMouseClicked
        if (clsGlobalVarClass.gTouchScreenMode)
        {
            if (txtUsername.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(null, true, "1", "Enter User Name.").setVisible(true);
                txtUsername.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(null, true, txtUsername.getText(), "1", "Enter User Name.").setVisible(true);
                txtUsername.setText(clsGlobalVarClass.gKeyboardValue);
            }
        }
    }//GEN-LAST:event_txtUsernameMouseClicked

    private void txtUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsernameActionPerformed

    private void txtUsernameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsernameKeyPressed
        try
        {
            if (evt.getKeyCode() == 10 && txtUsername.getText().equalsIgnoreCase("SANGUINE"))
            {
                txtPassword.requestFocus();
            }
            else
            {
                if (evt.getKeyCode() == 10)
                {
                    String sql = "select a.strUserCode,a.strUserName,a.strSuperType from tbluserhd a where a.strDebitCardString='" + txtUsername.getText() + "' ";
                    ResultSet rssql = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                    if (rssql.next())
                    {
                        String userCode = rssql.getString(1).toUpperCase();
                        txtUsername.setText(userCode);
                        funOKButtonClicked();
                    }
                    else
                    {
                        txtPassword.requestFocus();
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtUsernameKeyPressed

    private void txtPasswordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPasswordMouseClicked
        if (txtPassword.getPassword().length == 0)
        {
            new frmAlfaNumericKeyBoard(null, true, "1", "Enter  Password.").setVisible(true);
            txtPassword.setText(clsGlobalVarClass.gKeyboardValue);
        }
        else
        {
            new frmAlfaNumericKeyBoard(null, true, txtPassword.getPassword().toString(), "1", "Enter Password.").setVisible(true);
            txtPassword.setText(clsGlobalVarClass.gKeyboardValue);
        }
    }//GEN-LAST:event_txtPasswordMouseClicked

    private void txtPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPasswordKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            funOKButtonClicked();
        }
    }//GEN-LAST:event_txtPasswordKeyPressed
    public static void main(String[] args)
    {
        new frmUserAuthenticationPopUp(null, "dsggggggggggggggggggggggggg", "").setVisible(true);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNo;
    private javax.swing.JButton btnYes;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables

    private void funOKButtonClicked()
    {
        
        
        result = 1;
        this.dispose();
    }

}
