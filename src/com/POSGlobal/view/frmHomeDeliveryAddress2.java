package com.POSGlobal.view;

public class frmHomeDeliveryAddress2 extends javax.swing.JDialog
{
    /** Creates new form FrmOkPopUp */
    public frmHomeDeliveryAddress2(java.awt.Frame parent) 
    {      
        super(parent,true);
        //Dimension sz = Toolkit.getDefaultToolkit().getScreenSize();
        initComponents();               
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
        lblCustomerName = new javax.swing.JLabel();
        btnOk = new javax.swing.JButton();
        lblHomeDeliveryAddress = new javax.swing.JLabel();
        lblCustomerNameValue = new javax.swing.JLabel();
        lblCustMobileNumber = new javax.swing.JLabel();
        lblCustMobileNumberValue = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(null);
        setUndecorated(true);
        setResizable(false);

        panelBody.setBackground(new java.awt.Color(153, 204, 255));
        panelBody.setPreferredSize(new java.awt.Dimension(380, 179));

        lblCustomerName.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        lblCustomerName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCustomerName.setText("Customer Name:");
        lblCustomerName.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

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

        lblHomeDeliveryAddress.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        lblHomeDeliveryAddress.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHomeDeliveryAddress.setText("Home Delivery Address");

        lblCustomerNameValue.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        lblCustomerNameValue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCustomerNameValue.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        lblCustMobileNumber.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        lblCustMobileNumber.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCustMobileNumber.setText("Mobile Number :");
        lblCustMobileNumber.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        lblCustMobileNumberValue.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        lblCustMobileNumberValue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCustMobileNumberValue.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblHomeDeliveryAddress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblCustomerName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblCustMobileNumber, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCustMobileNumberValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblCustomerNameValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGap(170, 170, 170)
                .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(194, Short.MAX_VALUE))
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addComponent(lblHomeDeliveryAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCustomerNameValue, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCustMobileNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCustMobileNumberValue, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 156, Short.MAX_VALUE)
                .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBody, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBody, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
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
    private javax.swing.JLabel lblCustMobileNumber;
    private javax.swing.JLabel lblCustMobileNumberValue;
    private javax.swing.JLabel lblCustomerName;
    private javax.swing.JLabel lblCustomerNameValue;
    private javax.swing.JLabel lblHomeDeliveryAddress;
    private javax.swing.JPanel panelBody;
    // End of variables declaration//GEN-END:variables

}
