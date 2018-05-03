package com.POSGlobal.view;

import com.POSGlobal.view.*;
import java.awt.Dimension;
import java.awt.Toolkit;

public class frmOkPopUp extends javax.swing.JDialog
{
    /** Creates new form FrmOkPopUp */
    public frmOkPopUp(java.awt.Frame parent,String message,String msgType,int imageType) 
    {      
        super(parent, message,true);
        //Dimension sz = Toolkit.getDefaultToolkit().getScreenSize();
        initComponents();
         
        //setBounds(sz.width/3-20,sz.height/3,576, 158);
        lblMessage.setText(message);
        lblMsgType.setText(msgType);
        if(imageType==0)                        
        {
            lblMessageType.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgError.jpeg")));
        }
        if(imageType==1)
        {
            lblMessageType.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgWarning.gif")));
        }
        setAlwaysOnTop(true);
        setLocationRelativeTo(null);
        funSetShortCutKeys();
    }
    
    private void funSetShortCutKeys() {
        btnOk.setMnemonic('o');
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        panelBody = new javax.swing.JPanel();
        lblMessage = new javax.swing.JLabel();
        btnOk = new javax.swing.JButton();
        lblMessageType = new javax.swing.JLabel();
        lblMsgType = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(null);
        setUndecorated(true);
        setResizable(false);

        panelBody.setBackground(new java.awt.Color(153, 204, 255));
        panelBody.setPreferredSize(new java.awt.Dimension(380, 179));
        panelBody.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblMessage.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        lblMessage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMessage.setText("Message");
        lblMessage.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        panelBody.add(lblMessage, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 38, 378, 70));

        btnOk.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnOk.setForeground(new java.awt.Color(255, 255, 255));
        btnOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCommonButtonDark.png"))); // NOI18N
        btnOk.setText("OK");
        btnOk.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOk.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCommonButtonLight.png"))); // NOI18N
        btnOk.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnOkActionPerformed(evt);
            }
        });
        btnOk.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnOkKeyPressed(evt);
            }
        });
        panelBody.add(btnOk, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 110, 91, 38));
        panelBody.add(lblMessageType, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 60, 50));

        lblMsgType.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        lblMsgType.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMsgType.setText("MessageType");
        panelBody.add(lblMsgType, new org.netbeans.lib.awtextra.AbsoluteConstraints(87, 0, 231, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBody, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBody, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
 
    
    
    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnOkActionPerformed

    private void btnOkKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnOkKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode()==10)
        {
            dispose();
        }
    }//GEN-LAST:event_btnOkKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOk;
    private javax.swing.JLabel lblMessage;
    private javax.swing.JLabel lblMessageType;
    private javax.swing.JLabel lblMsgType;
    private javax.swing.JPanel panelBody;
    // End of variables declaration//GEN-END:variables

}
