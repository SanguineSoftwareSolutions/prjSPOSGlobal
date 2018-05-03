package com.POSGlobal.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import javax.swing.JFrame;

public class frmNumberKeyPad extends javax.swing.JDialog
{

    private String strtemp;
    boolean flgTextSelection;

    public frmNumberKeyPad(java.awt.Frame parent, boolean modal, String str)
    {
        super(parent, modal);
        initComponents();
        funSetShortCutKeys();
        setLocationRelativeTo(parent);
        strtemp = str;
        if ("qty".equals(strtemp))
        {
            txtDisplayNumber.setText("1");
            txtDisplayNumber.selectAll();
            flgTextSelection = true;
        }
    }

    private void funSetShortCutKeys()
    {
        btnClose.setMnemonic('c');
    }

    public frmNumberKeyPad(java.awt.Frame parent, boolean modal, String str, String val)
    {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(parent);
        strtemp = str;
        txtDisplayNumber.setText(val);
        if ("qty".equals(strtemp))
        {
            txtDisplayNumber.setText("1");
            txtDisplayNumber.selectAll();
            flgTextSelection = true;
        }
    }

    public frmNumberKeyPad(java.awt.Frame parent, boolean modal, String str, Object ob)
    {        
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(parent);
        strtemp = str;
    }

    /*public Double getResult()
     {
     try
     {
     retResult = result;
        
     }catch(Exception e)
     {
     e.printStackTrace();
     }
     finally
     {
     return retResult;
     }
     }*/
    public double funGetNumericValue()
    {
        double retVal = 0;
        if (!txtDisplayNumber.getText().isEmpty())
        {
            retVal = Double.parseDouble(txtDisplayNumber.getText().trim());
        }

        return retVal;
    }

    private void funNumButtonPressed(String num)
    {
        if (txtDisplayNumber.getText().length() <= 6)
        {
            if (flgTextSelection)
            {
                txtDisplayNumber.setText(num);
                flgTextSelection = false;
            }
            else
            {
                txtDisplayNumber.setText(txtDisplayNumber.getText() + num);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jPanel1 = new javax.swing.JPanel();
        btnNum1 = new javax.swing.JButton();
        btnNum7 = new javax.swing.JButton();
        btnNum3 = new javax.swing.JButton();
        btnNum6 = new javax.swing.JButton();
        btnNum0 = new javax.swing.JButton();
        btnNum9 = new javax.swing.JButton();
        btnNum8 = new javax.swing.JButton();
        btnNum5 = new javax.swing.JButton();
        btnNum4 = new javax.swing.JButton();
        btnNum2 = new javax.swing.JButton();
        txtDisplayNumber = new javax.swing.JTextField();
        btnOK = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnBackspace = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        btnDot = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(153, 204, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        btnNum1.setBackground(new java.awt.Color(51, 102, 255));
        btnNum1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnNum1.setForeground(new java.awt.Color(255, 255, 255));
        btnNum1.setText("1");
        btnNum1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNum1MouseClicked(evt);
            }
        });

        btnNum7.setBackground(new java.awt.Color(51, 102, 255));
        btnNum7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnNum7.setForeground(new java.awt.Color(255, 255, 255));
        btnNum7.setText("7");
        btnNum7.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNum7MouseClicked(evt);
            }
        });

        btnNum3.setBackground(new java.awt.Color(51, 102, 255));
        btnNum3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnNum3.setForeground(new java.awt.Color(255, 255, 255));
        btnNum3.setText("3");
        btnNum3.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNum3MouseClicked(evt);
            }
        });
        btnNum3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNum3ActionPerformed(evt);
            }
        });

        btnNum6.setBackground(new java.awt.Color(51, 102, 255));
        btnNum6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnNum6.setForeground(new java.awt.Color(255, 255, 255));
        btnNum6.setText("6");
        btnNum6.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNum6MouseClicked(evt);
            }
        });

        btnNum0.setBackground(new java.awt.Color(51, 102, 255));
        btnNum0.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnNum0.setForeground(new java.awt.Color(255, 255, 255));
        btnNum0.setText("0");
        btnNum0.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNum0MouseClicked(evt);
            }
        });

        btnNum9.setBackground(new java.awt.Color(51, 102, 255));
        btnNum9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnNum9.setForeground(new java.awt.Color(255, 255, 255));
        btnNum9.setText("9");
        btnNum9.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNum9MouseClicked(evt);
            }
        });

        btnNum8.setBackground(new java.awt.Color(51, 102, 255));
        btnNum8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnNum8.setForeground(new java.awt.Color(255, 255, 255));
        btnNum8.setText("8");
        btnNum8.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNum8MouseClicked(evt);
            }
        });

        btnNum5.setBackground(new java.awt.Color(51, 102, 255));
        btnNum5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnNum5.setForeground(new java.awt.Color(255, 255, 255));
        btnNum5.setText("5");
        btnNum5.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNum5MouseClicked(evt);
            }
        });

        btnNum4.setBackground(new java.awt.Color(51, 102, 255));
        btnNum4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnNum4.setForeground(new java.awt.Color(255, 255, 255));
        btnNum4.setText("4");
        btnNum4.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNum4MouseClicked(evt);
            }
        });

        btnNum2.setBackground(new java.awt.Color(51, 102, 255));
        btnNum2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnNum2.setForeground(new java.awt.Color(255, 255, 255));
        btnNum2.setText("2");
        btnNum2.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNum2MouseClicked(evt);
            }
        });

        txtDisplayNumber.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtDisplayNumber.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtDisplayNumberMouseClicked(evt);
            }
        });
        txtDisplayNumber.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtDisplayNumberActionPerformed(evt);
            }
        });
        txtDisplayNumber.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtDisplayNumberKeyPressed(evt);
            }
        });

        btnOK.setBackground(new java.awt.Color(51, 102, 255));
        btnOK.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnOK.setForeground(new java.awt.Color(255, 255, 255));
        btnOK.setText("OK");
        btnOK.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnOKMouseClicked(evt);
            }
        });
        btnOK.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnOKKeyPressed(evt);
            }
        });

        btnReset.setBackground(new java.awt.Color(51, 102, 255));
        btnReset.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnReset.setForeground(new java.awt.Color(255, 255, 255));
        btnReset.setText("Clear");
        btnReset.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnResetMouseClicked(evt);
            }
        });

        btnBackspace.setBackground(new java.awt.Color(51, 102, 255));
        btnBackspace.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnBackspace.setForeground(new java.awt.Color(255, 255, 255));
        btnBackspace.setText("BackSpace");
        btnBackspace.setPreferredSize(new java.awt.Dimension(100, 55));
        btnBackspace.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBackspaceMouseClicked(evt);
            }
        });

        btnClose.setBackground(new java.awt.Color(51, 102, 255));
        btnClose.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCloseActionPerformed(evt);
            }
        });

        btnDot.setBackground(new java.awt.Color(51, 102, 255));
        btnDot.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnDot.setForeground(new java.awt.Color(255, 255, 255));
        btnDot.setText(".");
        btnDot.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnDotMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnNum1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnNum2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnNum3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnNum4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtDisplayNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnBackspace, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(60, 60, 60)
                                .addComponent(btnNum0, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnNum5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(btnNum6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnNum9, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnDot, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnNum7, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnNum8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtDisplayNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNum1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNum2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNum3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNum4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnNum5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnNum6, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnNum9, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnNum0, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnNum7, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(btnDot, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnNum8, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnBackspace, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNum1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNum1MouseClicked
        // TODO add your handling code here:

        funNumButtonPressed(btnNum1.getText().trim());
    }//GEN-LAST:event_btnNum1MouseClicked

    private void btnNum7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNum7MouseClicked

        funNumButtonPressed(btnNum7.getText().trim());
    }//GEN-LAST:event_btnNum7MouseClicked

    private void btnNum3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNum3MouseClicked

        funNumButtonPressed(btnNum3.getText().trim());
    }//GEN-LAST:event_btnNum3MouseClicked

    private void btnNum6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNum6MouseClicked

        funNumButtonPressed(btnNum6.getText().trim());
    }//GEN-LAST:event_btnNum6MouseClicked

    private void btnNum0MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNum0MouseClicked

        funNumButtonPressed(btnNum0.getText().trim());
    }//GEN-LAST:event_btnNum0MouseClicked

    private void btnNum9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNum9MouseClicked

        funNumButtonPressed(btnNum9.getText().trim());
    }//GEN-LAST:event_btnNum9MouseClicked

    private void btnNum8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNum8MouseClicked

        funNumButtonPressed(btnNum8.getText().trim());

    }//GEN-LAST:event_btnNum8MouseClicked

    private void btnNum5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNum5MouseClicked

        funNumButtonPressed(btnNum5.getText().trim());
    }//GEN-LAST:event_btnNum5MouseClicked

    private void btnNum4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNum4MouseClicked

        funNumButtonPressed(btnNum4.getText().trim());
    }//GEN-LAST:event_btnNum4MouseClicked

    private void btnNum2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNum2MouseClicked

        funNumButtonPressed(btnNum2.getText().trim());
    }//GEN-LAST:event_btnNum2MouseClicked

    private void btnOKMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOKMouseClicked

        if ("".equals(txtDisplayNumber.getText()))
        {
            txtDisplayNumber.setText("0");
            clsGlobalVarClass.gNumerickeyboardValue = txtDisplayNumber.getText();
            clsGlobalVarClass.gRateEntered = true;
            txtDisplayNumber.setText(null);
            this.dispose();
        }
        else
        {
            clsGlobalVarClass.gNumerickeyboardValue = txtDisplayNumber.getText();
            clsGlobalVarClass.gRateEntered = true;
            txtDisplayNumber.setText(null);
            this.dispose();
        }
    }//GEN-LAST:event_btnOKMouseClicked

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
        // TODO add your handling code here:        
        txtDisplayNumber.setText("");
    }//GEN-LAST:event_btnResetMouseClicked

    private void btnBackspaceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBackspaceMouseClicked
        // TODO add your handling code here:
        try
        {
            if (txtDisplayNumber.getText().length() > 0)
            {
                StringBuilder sb = new StringBuilder(txtDisplayNumber.getText());
                sb.delete(txtDisplayNumber.getText().length() - 1, txtDisplayNumber.getText().length());
                //textValue=sb.toString();
                txtDisplayNumber.setText(sb.toString());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnBackspaceMouseClicked

    private void btnNum3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNum3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnNum3ActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnDotMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDotMouseClicked
        // TODO add your handling code here:
        try
        {
            if (txtDisplayNumber.getText().length() < 4)
            {
                if (!txtDisplayNumber.getText().contains("."))
                {
                    txtDisplayNumber.setText(txtDisplayNumber.getText() + btnDot.getActionCommand());
                }
                else
                {
                    new frmOkPopUp(null, "Invalid input", "Warning", 1).setVisible(true);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnDotMouseClicked

    private void btnOKKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnOKKeyPressed
        // TODO add your handling code here:  
    }//GEN-LAST:event_btnOKKeyPressed

    private void txtDisplayNumberMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDisplayNumberMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDisplayNumberMouseClicked

    private void txtDisplayNumberKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDisplayNumberKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            if ("".equals(txtDisplayNumber.getText()))
            {
                txtDisplayNumber.setText("0");
                clsGlobalVarClass.gNumerickeyboardValue = txtDisplayNumber.getText();
                clsGlobalVarClass.gRateEntered = true;
                txtDisplayNumber.setText(null);
                this.dispose();

            }
            else
            {
                clsGlobalVarClass.gNumerickeyboardValue = txtDisplayNumber.getText();
                clsGlobalVarClass.gRateEntered = true;
                txtDisplayNumber.setText(null);
                this.dispose();
            }
        }
    }//GEN-LAST:event_txtDisplayNumberKeyPressed

    private void txtDisplayNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDisplayNumberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDisplayNumberActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBackspace;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDot;
    private javax.swing.JButton btnNum0;
    private javax.swing.JButton btnNum1;
    private javax.swing.JButton btnNum2;
    private javax.swing.JButton btnNum3;
    private javax.swing.JButton btnNum4;
    private javax.swing.JButton btnNum5;
    private javax.swing.JButton btnNum6;
    private javax.swing.JButton btnNum7;
    private javax.swing.JButton btnNum8;
    private javax.swing.JButton btnNum9;
    private javax.swing.JButton btnOK;
    private javax.swing.JButton btnReset;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtDisplayNumber;
    // End of variables declaration//GEN-END:variables
}
