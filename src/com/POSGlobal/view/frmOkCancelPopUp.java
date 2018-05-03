package com.POSGlobal.view;


import java.awt.Dimension;
import java.awt.Toolkit;

public class frmOkCancelPopUp extends javax.swing.JDialog 
{
    private int result,retResult;
    /** Creates new form FrmOkPopUp */
    public frmOkCancelPopUp(java.awt.Frame parent,String message) 
    {
      
        super(parent, message,true);
//        Dimension sz = Toolkit.getDefaultToolkit().getScreenSize();
        initComponents();
//         
//        setBounds(sz.width/3-20,sz.height/3,576, 158);        
        lblMessage.setText(message);
        setAlwaysOnTop(true);
        setLocationRelativeTo(null);
        funSetShortCutKeys();
    }
     private void funSetShortCutKeys() {
        btnYes.setMnemonic('y');
        btnNo.setMnemonic('n');

    }
    public int getResult()
    {
        try
        {
            retResult=result;
        }catch(Exception e)
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
    private void initComponents() {

        panelBody = new javax.swing.JPanel();
        lblMessage = new javax.swing.JLabel();
        btnYes = new javax.swing.JButton();
        btnNo = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(null);
        setUndecorated(true);
        setResizable(false);

        panelBody.setBackground(new java.awt.Color(153, 204, 255));
        panelBody.setPreferredSize(new java.awt.Dimension(600, 179));
        panelBody.setLayout(null);

        lblMessage.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblMessage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMessage.setText("Message");
        lblMessage.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        panelBody.add(lblMessage);
        lblMessage.setBounds(0, 0, 480, 70);

        btnYes.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnYes.setForeground(new java.awt.Color(248, 248, 246));
        btnYes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn1.png"))); // NOI18N
        btnYes.setText("YES");
        btnYes.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnYes.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn2.png"))); // NOI18N
        btnYes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnYesActionPerformed(evt);
            }
        });
        btnYes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnYesKeyPressed(evt);
            }
        });
        panelBody.add(btnYes);
        btnYes.setBounds(100, 80, 90, 40);

        btnNo.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnNo.setForeground(new java.awt.Color(248, 248, 246));
        btnNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn1.png"))); // NOI18N
        btnNo.setText("NO");
        btnNo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNo.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn2.png"))); // NOI18N
        btnNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNoActionPerformed(evt);
            }
        });
        panelBody.add(btnNo);
        btnNo.setBounds(290, 80, 90, 40);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBody, javax.swing.GroupLayout.PREFERRED_SIZE, 497, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBody, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNoActionPerformed
        // TODO add your handling code here:
        result=0;
        this.dispose();
    }//GEN-LAST:event_btnNoActionPerformed

    private void btnYesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnYesActionPerformed
        // TODO add your handling code here:
        result=1;
        this.dispose();
    }//GEN-LAST:event_btnYesActionPerformed

    private void btnYesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnYesKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode()==10)
        {
            result=1;
            this.dispose();
        }
    }//GEN-LAST:event_btnYesKeyPressed
public static void main(String[] args){
   new frmOkCancelPopUp(null, "dsggggggggggggggggggggggggg").setVisible(true);
}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNo;
    private javax.swing.JButton btnYes;
    private javax.swing.JLabel lblMessage;
    private javax.swing.JPanel panelBody;
    // End of variables declaration//GEN-END:variables

}
