package com.POSGlobal.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUtility;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.ResultSet;
import javax.swing.JPanel;

public class frmHomeDeliveryAddress extends javax.swing.JDialog
{

    private String homeDeliveryAddressType;
    private String strMobileNo;
    private String strCustomerCode;
    private clsUtility objUtility;

    public frmHomeDeliveryAddress(java.awt.Frame parent, boolean modal, String mobileNo)
    {

	super(parent, modal);
	initComponents();

	this.objUtility=new clsUtility();
	this.setLocationRelativeTo(null);
	this.strMobileNo = mobileNo;

	lblTempLandmark.setVisible(false);
	txtTempLandmark.setVisible(false);
	lblTempStreetName.setVisible(false);
	txtTempStreetName.setVisible(false);

	funSetAddressDetail(mobileNo);
	tabbedPaneHomeDeliveryAddress2.setSelectedIndex(0);
	funSetHomeDeliveryAddress();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        panelHeader = new javax.swing.JPanel();
        lblModuleName = new javax.swing.JLabel();
        lblUserCode = new javax.swing.JLabel();
        lblDate = new javax.swing.JLabel();
        lblPosName = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        panelTabbedPane = 	new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSGlobal/images/imgBGJPOS.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  ;
        brnHomeAddress = new javax.swing.JButton();
        tabbedPaneHomeDeliveryAddress2 = new javax.swing.JTabbedPane();
        panelHomeAddress2 = new javax.swing.JPanel();
        lblCustAddress3 = new javax.swing.JLabel();
        txtHomeAddress = new javax.swing.JTextField();
        lblStreetName3 = new javax.swing.JLabel();
        txtHomeStreetName = new javax.swing.JTextField();
        lblLandmark3 = new javax.swing.JLabel();
        txtHomeLandmark = new javax.swing.JTextField();
        lblPinCode3 = new javax.swing.JLabel();
        txtHomePinCode = new javax.swing.JTextField();
        txtHomeCity = new javax.swing.JTextField();
        txtHomeState = new javax.swing.JTextField();
        panelOfficeAddress = new javax.swing.JPanel();
        lblHomelCustAddress = new javax.swing.JLabel();
        txtOfficeCustAddress = new javax.swing.JTextField();
        lblHomelStreetName = new javax.swing.JLabel();
        txtOfficeStreetName = new javax.swing.JTextField();
        lblHomelLandmark = new javax.swing.JLabel();
        txtOfficeLandmark = new javax.swing.JTextField();
        lblHomelPinCode = new javax.swing.JLabel();
        txtOfficePinCode = new javax.swing.JTextField();
        txtOfficeCity = new javax.swing.JTextField();
        txtOfficeState = new javax.swing.JTextField();
        panelTemporaryAddress = new javax.swing.JPanel();
        lblTempCustAddress = new javax.swing.JLabel();
        lblTempStreetName = new javax.swing.JLabel();
        txtTempStreetName = new javax.swing.JTextField();
        lblTempLandmark = new javax.swing.JLabel();
        txtTempLandmark = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtTempCustAddress = new javax.swing.JTextArea();
        btnHomeAddress = new javax.swing.JButton();
        btnOfficeAddress = new javax.swing.JButton();
        btnTempAddress = new javax.swing.JButton();
        lblCustomerName3 = new javax.swing.JLabel();
        txtHomeMobileNo = new javax.swing.JTextField();
        txtHomeCustomerName = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(605, 358));
        setResizable(false);
        getContentPane().setLayout(null);

        panelHeader.setBackground(new java.awt.Color(69, 164, 238));
        panelHeader.setPreferredSize(new java.awt.Dimension(800, 30));

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        lblModuleName.setText("SPOS - Home Delivery Address");

        lblUserCode.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblUserCode.setForeground(new java.awt.Color(255, 255, 255));

        lblTitle.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panelHeaderLayout = new javax.swing.GroupLayout(panelHeader);
        panelHeader.setLayout(panelHeaderLayout);
        panelHeaderLayout.setHorizontalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHeaderLayout.createSequentialGroup()
                .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelHeaderLayout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(lblUserCode, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(75, 75, 75)
                        .addComponent(lblPosName, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(311, 311, 311)
                        .addComponent(lblDate, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelHeaderLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(lblModuleName, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelHeaderLayout.setVerticalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHeaderLayout.createSequentialGroup()
                .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblModuleName, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                    .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(21, 21, 21)
                .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelHeaderLayout.createSequentialGroup()
                        .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblUserCode, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblPosName, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        getContentPane().add(panelHeader);
        panelHeader.setBounds(0, 0, 820, 30);

        brnHomeAddress.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        brnHomeAddress.setForeground(new java.awt.Color(255, 255, 255));
        brnHomeAddress.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCommonButtonDark.png"))); // NOI18N
        brnHomeAddress.setMnemonic('k');
        brnHomeAddress.setText("OK");
        brnHomeAddress.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        brnHomeAddress.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCommonButtonLight.png"))); // NOI18N
        brnHomeAddress.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                brnHomeAddressMouseClicked(evt);
            }
        });
        brnHomeAddress.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                brnHomeAddressActionPerformed(evt);
            }
        });
        brnHomeAddress.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                brnHomeAddressKeyPressed(evt);
            }
        });

        panelHomeAddress2.setOpaque(false);

        lblCustAddress3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCustAddress3.setText("Address/Flat No. :");

        txtHomeAddress.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtHomeAddressMouseClicked(evt);
            }
        });
        txtHomeAddress.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtHomeAddressActionPerformed(evt);
            }
        });
        txtHomeAddress.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtHomeAddressKeyPressed(evt);
            }
        });

        lblStreetName3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblStreetName3.setText("Street Name      :");

        txtHomeStreetName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtHomeStreetNameMouseClicked(evt);
            }
        });
        txtHomeStreetName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtHomeStreetNameKeyPressed(evt);
            }
        });

        lblLandmark3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblLandmark3.setText("Landmark          :");

        txtHomeLandmark.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtHomeLandmarkMouseClicked(evt);
            }
        });
        txtHomeLandmark.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtHomeLandmarkActionPerformed(evt);
            }
        });
        txtHomeLandmark.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtHomeLandmarkKeyPressed(evt);
            }
        });

        lblPinCode3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPinCode3.setText("Pin Code           :");

        txtHomePinCode.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtHomePinCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtHomePinCodeMouseClicked(evt);
            }
        });
        txtHomePinCode.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtHomePinCodeKeyPressed(evt);
            }
        });

        txtHomeCity.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtHomeCity.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtHomeCityMouseClicked(evt);
            }
        });
        txtHomeCity.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtHomeCityActionPerformed(evt);
            }
        });
        txtHomeCity.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtHomeCityKeyPressed(evt);
            }
        });

        txtHomeState.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtHomeState.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtHomeStateMouseClicked(evt);
            }
        });
        txtHomeState.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtHomeStateActionPerformed(evt);
            }
        });
        txtHomeState.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtHomeStateKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelHomeAddress2Layout = new javax.swing.GroupLayout(panelHomeAddress2);
        panelHomeAddress2.setLayout(panelHomeAddress2Layout);
        panelHomeAddress2Layout.setHorizontalGroup(
            panelHomeAddress2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHomeAddress2Layout.createSequentialGroup()
                .addComponent(lblPinCode3, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtHomePinCode, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(txtHomeCity, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtHomeState, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 50, Short.MAX_VALUE))
            .addGroup(panelHomeAddress2Layout.createSequentialGroup()
                .addGroup(panelHomeAddress2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelHomeAddress2Layout.createSequentialGroup()
                        .addGroup(panelHomeAddress2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblStreetName3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblCustAddress3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelHomeAddress2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtHomeAddress)
                            .addComponent(txtHomeStreetName)))
                    .addGroup(panelHomeAddress2Layout.createSequentialGroup()
                        .addComponent(lblLandmark3, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtHomeLandmark)))
                .addContainerGap())
        );
        panelHomeAddress2Layout.setVerticalGroup(
            panelHomeAddress2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHomeAddress2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelHomeAddress2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtHomeAddress)
                    .addComponent(lblCustAddress3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelHomeAddress2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtHomeStreetName, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelHomeAddress2Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(lblStreetName3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelHomeAddress2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtHomeLandmark, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelHomeAddress2Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(lblLandmark3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelHomeAddress2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelHomeAddress2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtHomePinCode, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtHomeCity, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtHomeState, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelHomeAddress2Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(lblPinCode3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(180, 180, 180))
        );

        tabbedPaneHomeDeliveryAddress2.addTab("Home Address", panelHomeAddress2);

        panelOfficeAddress.setOpaque(false);

        lblHomelCustAddress.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblHomelCustAddress.setText("Address/Flat No. :");

        txtOfficeCustAddress.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtOfficeCustAddressMouseClicked(evt);
            }
        });
        txtOfficeCustAddress.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtOfficeCustAddressKeyPressed(evt);
            }
        });

        lblHomelStreetName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblHomelStreetName.setText("Street Name      :");

        txtOfficeStreetName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtOfficeStreetNameMouseClicked(evt);
            }
        });
        txtOfficeStreetName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtOfficeStreetNameKeyPressed(evt);
            }
        });

        lblHomelLandmark.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblHomelLandmark.setText("Landmark          :");

        txtOfficeLandmark.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtOfficeLandmarkMouseClicked(evt);
            }
        });
        txtOfficeLandmark.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtOfficeLandmarkKeyPressed(evt);
            }
        });

        lblHomelPinCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblHomelPinCode.setText("Pin Code           :");

        txtOfficePinCode.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtOfficePinCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtOfficePinCodeMouseClicked(evt);
            }
        });
        txtOfficePinCode.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtOfficePinCodeKeyPressed(evt);
            }
        });

        txtOfficeCity.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtOfficeCity.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtOfficeCityMouseClicked(evt);
            }
        });
        txtOfficeCity.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtOfficeCityKeyPressed(evt);
            }
        });

        txtOfficeState.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtOfficeState.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtOfficeStateMouseClicked(evt);
            }
        });
        txtOfficeState.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtOfficeStateKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelOfficeAddressLayout = new javax.swing.GroupLayout(panelOfficeAddress);
        panelOfficeAddress.setLayout(panelOfficeAddressLayout);
        panelOfficeAddressLayout.setHorizontalGroup(
            panelOfficeAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelOfficeAddressLayout.createSequentialGroup()
                .addComponent(lblHomelPinCode, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtOfficePinCode, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtOfficeCity, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtOfficeState, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 54, Short.MAX_VALUE))
            .addGroup(panelOfficeAddressLayout.createSequentialGroup()
                .addGroup(panelOfficeAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelOfficeAddressLayout.createSequentialGroup()
                        .addGroup(panelOfficeAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblHomelStreetName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblHomelCustAddress, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelOfficeAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtOfficeCustAddress)
                            .addComponent(txtOfficeStreetName)))
                    .addGroup(panelOfficeAddressLayout.createSequentialGroup()
                        .addComponent(lblHomelLandmark, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtOfficeLandmark)))
                .addContainerGap())
        );
        panelOfficeAddressLayout.setVerticalGroup(
            panelOfficeAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelOfficeAddressLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelOfficeAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtOfficeCustAddress)
                    .addComponent(lblHomelCustAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelOfficeAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtOfficeStreetName, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelOfficeAddressLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(lblHomelStreetName, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelOfficeAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtOfficeLandmark, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelOfficeAddressLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(lblHomelLandmark, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelOfficeAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelOfficeAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtOfficePinCode, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtOfficeCity, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtOfficeState, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelOfficeAddressLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(lblHomelPinCode, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(184, 184, 184))
        );

        tabbedPaneHomeDeliveryAddress2.addTab("Office Address", panelOfficeAddress);

        panelTemporaryAddress.setOpaque(false);

        lblTempCustAddress.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTempCustAddress.setText("Temp Address    :");

        lblTempStreetName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTempStreetName.setText("Street Name      :");

        txtTempStreetName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtTempStreetNameMouseClicked(evt);
            }
        });
        txtTempStreetName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtTempStreetNameKeyPressed(evt);
            }
        });

        lblTempLandmark.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTempLandmark.setText("Landmark          :");

        txtTempLandmark.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtTempLandmarkMouseClicked(evt);
            }
        });
        txtTempLandmark.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtTempLandmarkKeyPressed(evt);
            }
        });

        txtTempCustAddress.setColumns(20);
        txtTempCustAddress.setRows(5);
        txtTempCustAddress.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtTempCustAddressMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(txtTempCustAddress);

        javax.swing.GroupLayout panelTemporaryAddressLayout = new javax.swing.GroupLayout(panelTemporaryAddress);
        panelTemporaryAddress.setLayout(panelTemporaryAddressLayout);
        panelTemporaryAddressLayout.setHorizontalGroup(
            panelTemporaryAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTemporaryAddressLayout.createSequentialGroup()
                .addGroup(panelTemporaryAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTemporaryAddressLayout.createSequentialGroup()
                        .addGroup(panelTemporaryAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblTempStreetName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblTempCustAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelTemporaryAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTempStreetName, javax.swing.GroupLayout.DEFAULT_SIZE, 475, Short.MAX_VALUE)
                            .addComponent(jScrollPane1)))
                    .addGroup(panelTemporaryAddressLayout.createSequentialGroup()
                        .addComponent(lblTempLandmark, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTempLandmark)))
                .addContainerGap())
        );
        panelTemporaryAddressLayout.setVerticalGroup(
            panelTemporaryAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTemporaryAddressLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTemporaryAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTempCustAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelTemporaryAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtTempStreetName, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelTemporaryAddressLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(lblTempStreetName, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelTemporaryAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtTempLandmark, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelTemporaryAddressLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(lblTempLandmark, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(263, 263, 263))
        );

        tabbedPaneHomeDeliveryAddress2.addTab("Temporary Address", panelTemporaryAddress);

        btnHomeAddress.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnHomeAddress.setForeground(new java.awt.Color(255, 255, 255));
        btnHomeAddress.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCommonButtonDark.png"))); // NOI18N
        btnHomeAddress.setMnemonic('h');
        btnHomeAddress.setText("HOME");
        btnHomeAddress.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnHomeAddress.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCommonButtonLight.png"))); // NOI18N
        btnHomeAddress.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnHomeAddressMouseClicked(evt);
            }
        });
        btnHomeAddress.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnHomeAddressActionPerformed(evt);
            }
        });
        btnHomeAddress.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnHomeAddressKeyPressed(evt);
            }
        });

        btnOfficeAddress.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnOfficeAddress.setForeground(new java.awt.Color(255, 255, 255));
        btnOfficeAddress.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCommonButtonDark.png"))); // NOI18N
        btnOfficeAddress.setMnemonic('o');
        btnOfficeAddress.setText("OFFICE");
        btnOfficeAddress.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOfficeAddress.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCommonButtonLight.png"))); // NOI18N
        btnOfficeAddress.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnOfficeAddressMouseClicked(evt);
            }
        });
        btnOfficeAddress.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnOfficeAddressActionPerformed(evt);
            }
        });
        btnOfficeAddress.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnOfficeAddressKeyPressed(evt);
            }
        });

        btnTempAddress.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnTempAddress.setForeground(new java.awt.Color(255, 255, 255));
        btnTempAddress.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCommonButtonDark.png"))); // NOI18N
        btnTempAddress.setMnemonic('t');
        btnTempAddress.setText("TEMP");
        btnTempAddress.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTempAddress.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCommonButtonLight.png"))); // NOI18N
        btnTempAddress.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTempAddressMouseClicked(evt);
            }
        });
        btnTempAddress.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnTempAddressActionPerformed(evt);
            }
        });
        btnTempAddress.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnTempAddressKeyPressed(evt);
            }
        });

        lblCustomerName3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCustomerName3.setText("Cust. No & Name");

        txtHomeMobileNo.setEditable(false);
        txtHomeMobileNo.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusLost(java.awt.event.FocusEvent evt)
            {
                txtHomeMobileNoFocusLost(evt);
            }
        });
        txtHomeMobileNo.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtHomeMobileNoMouseClicked(evt);
            }
        });
        txtHomeMobileNo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtHomeMobileNoActionPerformed(evt);
            }
        });
        txtHomeMobileNo.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtHomeMobileNoKeyPressed(evt);
            }
        });

        txtHomeCustomerName.setEditable(false);
        txtHomeCustomerName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtHomeCustomerNameMouseClicked(evt);
            }
        });
        txtHomeCustomerName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtHomeCustomerNameKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt)
            {
                txtHomeCustomerNameKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout panelTabbedPaneLayout = new javax.swing.GroupLayout(panelTabbedPane);
        panelTabbedPane.setLayout(panelTabbedPaneLayout);
        panelTabbedPaneLayout.setHorizontalGroup(
            panelTabbedPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPaneHomeDeliveryAddress2)
            .addGroup(panelTabbedPaneLayout.createSequentialGroup()
                .addGroup(panelTabbedPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTabbedPaneLayout.createSequentialGroup()
                        .addComponent(btnHomeAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addComponent(btnOfficeAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(btnTempAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addComponent(brnHomeAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelTabbedPaneLayout.createSequentialGroup()
                        .addComponent(lblCustomerName3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtHomeMobileNo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtHomeCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panelTabbedPaneLayout.setVerticalGroup(
            panelTabbedPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTabbedPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTabbedPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOfficeAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTempAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHomeAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(brnHomeAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelTabbedPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtHomeCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtHomeMobileNo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCustomerName3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabbedPaneHomeDeliveryAddress2, javax.swing.GroupLayout.PREFERRED_SIZE, 210, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(panelTabbedPane);
        panelTabbedPane.setBounds(0, 30, 600, 320);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void brnHomeAddressActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_brnHomeAddressActionPerformed
    {//GEN-HEADEREND:event_brnHomeAddressActionPerformed
	if (tabbedPaneHomeDeliveryAddress2.getSelectedIndex() == 2)//temp address
	{
	    if (txtTempCustAddress.getText().trim().isEmpty())
	    {
		new frmOkPopUp(null, "Enter Temp Address.", "Home Delivery Temp Address", 1).setVisible(true);
		return;
	    }
	    funSetHomeDeliveryAddress();

	    funUpdateCustomerHomeAddress();
	    funUpdateCustomerOfficeAddress();
	    funUpdateCustomerTempAddress();

	    this.dispose();
	}
	else
	{
	    funSetHomeDeliveryAddress();

	    funUpdateCustomerHomeAddress();
	    funUpdateCustomerOfficeAddress();
	    funUpdateCustomerTempAddress();

	    this.dispose();
	}
    }//GEN-LAST:event_brnHomeAddressActionPerformed

    private void brnHomeAddressKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_brnHomeAddressKeyPressed
    {//GEN-HEADEREND:event_brnHomeAddressKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    if (tabbedPaneHomeDeliveryAddress2.getSelectedIndex() == 2)//temp address
	    {
		if (txtTempCustAddress.getText().trim().isEmpty())
		{
		    new frmOkPopUp(null, "Enter Temp Address.", "Home Delivery Temp Address", 1).setVisible(true);
		    return;
		}
		funSetHomeDeliveryAddress();
		funUpdateCustomerTempAddress();
		this.dispose();
	    }
	    else
	    {
		funSetHomeDeliveryAddress();
		funUpdateCustomerTempAddress();
		this.dispose();
	    }
	}
    }//GEN-LAST:event_brnHomeAddressKeyPressed

    private void btnHomeAddressActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnHomeAddressActionPerformed
    {//GEN-HEADEREND:event_btnHomeAddressActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_btnHomeAddressActionPerformed

    private void btnHomeAddressKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnHomeAddressKeyPressed
    {//GEN-HEADEREND:event_btnHomeAddressKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_btnHomeAddressKeyPressed

    private void btnOfficeAddressActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOfficeAddressActionPerformed
    {//GEN-HEADEREND:event_btnOfficeAddressActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_btnOfficeAddressActionPerformed

    private void btnOfficeAddressKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnOfficeAddressKeyPressed
    {//GEN-HEADEREND:event_btnOfficeAddressKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_btnOfficeAddressKeyPressed

    private void btnTempAddressActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnTempAddressActionPerformed
    {//GEN-HEADEREND:event_btnTempAddressActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_btnTempAddressActionPerformed

    private void btnTempAddressKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnTempAddressKeyPressed
    {//GEN-HEADEREND:event_btnTempAddressKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_btnTempAddressKeyPressed

    private void brnHomeAddressMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_brnHomeAddressMouseClicked
    {//GEN-HEADEREND:event_brnHomeAddressMouseClicked

    }//GEN-LAST:event_brnHomeAddressMouseClicked

    private void btnOfficeAddressMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnOfficeAddressMouseClicked
    {//GEN-HEADEREND:event_btnOfficeAddressMouseClicked
	tabbedPaneHomeDeliveryAddress2.setSelectedIndex(1);//office address
	funSetHomeDeliveryAddress();
    }//GEN-LAST:event_btnOfficeAddressMouseClicked

    private void btnTempAddressMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnTempAddressMouseClicked
    {//GEN-HEADEREND:event_btnTempAddressMouseClicked
	tabbedPaneHomeDeliveryAddress2.setSelectedIndex(2);//temp address
	funSetHomeDeliveryAddress();
    }//GEN-LAST:event_btnTempAddressMouseClicked

    private void btnHomeAddressMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnHomeAddressMouseClicked
    {//GEN-HEADEREND:event_btnHomeAddressMouseClicked
	tabbedPaneHomeDeliveryAddress2.setSelectedIndex(0);//home address
	funSetHomeDeliveryAddress();
    }//GEN-LAST:event_btnHomeAddressMouseClicked

    private void txtTempLandmarkKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtTempLandmarkKeyPressed
    {//GEN-HEADEREND:event_txtTempLandmarkKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtTempLandmarkKeyPressed

    private void txtTempLandmarkMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtTempLandmarkMouseClicked
    {//GEN-HEADEREND:event_txtTempLandmarkMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_txtTempLandmarkMouseClicked

    private void txtTempStreetNameKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtTempStreetNameKeyPressed
    {//GEN-HEADEREND:event_txtTempStreetNameKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtTempStreetNameKeyPressed

    private void txtTempStreetNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtTempStreetNameMouseClicked
    {//GEN-HEADEREND:event_txtTempStreetNameMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_txtTempStreetNameMouseClicked

    private void txtOfficeStateKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtOfficeStateKeyPressed
    {//GEN-HEADEREND:event_txtOfficeStateKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtOfficeStateKeyPressed

    private void txtOfficeStateMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtOfficeStateMouseClicked
    {//GEN-HEADEREND:event_txtOfficeStateMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_txtOfficeStateMouseClicked

    private void txtOfficeCityKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtOfficeCityKeyPressed
    {//GEN-HEADEREND:event_txtOfficeCityKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtOfficeCityKeyPressed

    private void txtOfficeCityMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtOfficeCityMouseClicked
    {//GEN-HEADEREND:event_txtOfficeCityMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_txtOfficeCityMouseClicked

    private void txtOfficePinCodeKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtOfficePinCodeKeyPressed
    {//GEN-HEADEREND:event_txtOfficePinCodeKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtOfficePinCodeKeyPressed

    private void txtOfficePinCodeMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtOfficePinCodeMouseClicked
    {//GEN-HEADEREND:event_txtOfficePinCodeMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_txtOfficePinCodeMouseClicked

    private void txtOfficeLandmarkKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtOfficeLandmarkKeyPressed
    {//GEN-HEADEREND:event_txtOfficeLandmarkKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtOfficeLandmarkKeyPressed

    private void txtOfficeLandmarkMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtOfficeLandmarkMouseClicked
    {//GEN-HEADEREND:event_txtOfficeLandmarkMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_txtOfficeLandmarkMouseClicked

    private void txtOfficeStreetNameKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtOfficeStreetNameKeyPressed
    {//GEN-HEADEREND:event_txtOfficeStreetNameKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtOfficeStreetNameKeyPressed

    private void txtOfficeStreetNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtOfficeStreetNameMouseClicked
    {//GEN-HEADEREND:event_txtOfficeStreetNameMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_txtOfficeStreetNameMouseClicked

    private void txtOfficeCustAddressKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtOfficeCustAddressKeyPressed
    {//GEN-HEADEREND:event_txtOfficeCustAddressKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtOfficeCustAddressKeyPressed

    private void txtOfficeCustAddressMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtOfficeCustAddressMouseClicked
    {//GEN-HEADEREND:event_txtOfficeCustAddressMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_txtOfficeCustAddressMouseClicked

    private void txtHomeStateKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtHomeStateKeyPressed
    {//GEN-HEADEREND:event_txtHomeStateKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtHomeStateKeyPressed

    private void txtHomeStateMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtHomeStateMouseClicked
    {//GEN-HEADEREND:event_txtHomeStateMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_txtHomeStateMouseClicked

    private void txtHomeCityKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtHomeCityKeyPressed
    {//GEN-HEADEREND:event_txtHomeCityKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtHomeCityKeyPressed

    private void txtHomeCityMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtHomeCityMouseClicked
    {//GEN-HEADEREND:event_txtHomeCityMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_txtHomeCityMouseClicked

    private void txtHomePinCodeKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtHomePinCodeKeyPressed
    {//GEN-HEADEREND:event_txtHomePinCodeKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtHomePinCodeKeyPressed

    private void txtHomePinCodeMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtHomePinCodeMouseClicked
    {//GEN-HEADEREND:event_txtHomePinCodeMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_txtHomePinCodeMouseClicked

    private void txtHomeLandmarkKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtHomeLandmarkKeyPressed
    {//GEN-HEADEREND:event_txtHomeLandmarkKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtHomeLandmarkKeyPressed

    private void txtHomeLandmarkMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtHomeLandmarkMouseClicked
    {//GEN-HEADEREND:event_txtHomeLandmarkMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_txtHomeLandmarkMouseClicked

    private void txtHomeStreetNameKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtHomeStreetNameKeyPressed
    {//GEN-HEADEREND:event_txtHomeStreetNameKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtHomeStreetNameKeyPressed

    private void txtHomeStreetNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtHomeStreetNameMouseClicked
    {//GEN-HEADEREND:event_txtHomeStreetNameMouseClicked
	 try
        {
            if (txtHomeStreetName.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(null, true, "1", "Enter street name").setVisible(true);
                txtHomeStreetName.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(null, true, txtHomeStreetName.getText(), "1", "Enter street name").setVisible(true);
                txtHomeStreetName.setText(clsGlobalVarClass.gKeyboardValue);
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtHomeStreetNameMouseClicked

    private void txtHomeAddressKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtHomeAddressKeyPressed
    {//GEN-HEADEREND:event_txtHomeAddressKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtHomeAddressKeyPressed

    private void txtHomeAddressMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtHomeAddressMouseClicked
    {//GEN-HEADEREND:event_txtHomeAddressMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_txtHomeAddressMouseClicked

    private void txtHomeMobileNoKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtHomeMobileNoKeyPressed
    {//GEN-HEADEREND:event_txtHomeMobileNoKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtHomeMobileNoKeyPressed

    private void txtHomeMobileNoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtHomeMobileNoActionPerformed
    {//GEN-HEADEREND:event_txtHomeMobileNoActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtHomeMobileNoActionPerformed

    private void txtHomeMobileNoMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtHomeMobileNoMouseClicked
    {//GEN-HEADEREND:event_txtHomeMobileNoMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_txtHomeMobileNoMouseClicked

    private void txtHomeMobileNoFocusLost(java.awt.event.FocusEvent evt)//GEN-FIRST:event_txtHomeMobileNoFocusLost
    {//GEN-HEADEREND:event_txtHomeMobileNoFocusLost
	// TODO add your handling code here:
    }//GEN-LAST:event_txtHomeMobileNoFocusLost

    private void txtHomeCustomerNameKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtHomeCustomerNameKeyReleased
    {//GEN-HEADEREND:event_txtHomeCustomerNameKeyReleased
	// TODO add your handling code here:
    }//GEN-LAST:event_txtHomeCustomerNameKeyReleased

    private void txtHomeCustomerNameKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtHomeCustomerNameKeyPressed
    {//GEN-HEADEREND:event_txtHomeCustomerNameKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtHomeCustomerNameKeyPressed

    private void txtHomeCustomerNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtHomeCustomerNameMouseClicked
    {//GEN-HEADEREND:event_txtHomeCustomerNameMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_txtHomeCustomerNameMouseClicked

    private void txtTempCustAddressMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtTempCustAddressMouseClicked
    {//GEN-HEADEREND:event_txtTempCustAddressMouseClicked
	new frmAlfaNumericKeyBoard(null, true, "1", "Enter Address.", true).setVisible(true);
	String tempAddress = clsGlobalVarClass.gKeyboardValue.trim();
	if (tempAddress.length() > 0)
	{
	    txtTempCustAddress.setText(tempAddress);
	}
	clsGlobalVarClass.gKeyboardValue = "";
    }//GEN-LAST:event_txtTempCustAddressMouseClicked

    private void txtHomeAddressActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtHomeAddressActionPerformed
    {//GEN-HEADEREND:event_txtHomeAddressActionPerformed
         try
        {
            if (txtHomeAddress.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(null, true, "1", "Enter address").setVisible(true);
                txtHomeAddress.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(null, true, txtHomeAddress.getText(), "1", "Enter address").setVisible(true);
                txtHomeAddress.setText(clsGlobalVarClass.gKeyboardValue);
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtHomeAddressActionPerformed

    private void txtHomeLandmarkActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtHomeLandmarkActionPerformed
    {//GEN-HEADEREND:event_txtHomeLandmarkActionPerformed
        try
        {
            if (txtHomeLandmark.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(null, true, "1", "Enter landmark").setVisible(true);
                txtHomeLandmark.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(null, true, txtHomeLandmark.getText(), "1", "Enter landmark").setVisible(true);
                txtHomeLandmark.setText(clsGlobalVarClass.gKeyboardValue);
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtHomeLandmarkActionPerformed

    private void txtHomeCityActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtHomeCityActionPerformed
    {//GEN-HEADEREND:event_txtHomeCityActionPerformed
        try
        {
            if (txtHomeCity.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(null, true, "1", "Enter city").setVisible(true);
                txtHomeCity.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(null, true, txtHomeCity.getText(), "1", "Enter city").setVisible(true);
                txtHomeCity.setText(clsGlobalVarClass.gKeyboardValue);
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtHomeCityActionPerformed

    private void txtHomeStateActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtHomeStateActionPerformed
    {//GEN-HEADEREND:event_txtHomeStateActionPerformed
         try
        {
            if (txtHomeState.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(null, true, "1", "Enter state").setVisible(true);
                txtHomeState.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(null, true, txtHomeState.getText(), "1", "Enter state").setVisible(true);
                txtHomeState.setText(clsGlobalVarClass.gKeyboardValue);
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtHomeStateActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton brnHomeAddress;
    private javax.swing.JButton btnHomeAddress;
    private javax.swing.JButton btnOfficeAddress;
    private javax.swing.JButton btnTempAddress;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCustAddress3;
    private javax.swing.JLabel lblCustomerName3;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblHomelCustAddress;
    private javax.swing.JLabel lblHomelLandmark;
    private javax.swing.JLabel lblHomelPinCode;
    private javax.swing.JLabel lblHomelStreetName;
    private javax.swing.JLabel lblLandmark3;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPinCode3;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblStreetName3;
    private javax.swing.JLabel lblTempCustAddress;
    private javax.swing.JLabel lblTempLandmark;
    private javax.swing.JLabel lblTempStreetName;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelHomeAddress2;
    private javax.swing.JPanel panelOfficeAddress;
    private javax.swing.JPanel panelTabbedPane;
    private javax.swing.JPanel panelTemporaryAddress;
    private javax.swing.JTabbedPane tabbedPaneHomeDeliveryAddress2;
    private javax.swing.JTextField txtHomeAddress;
    private javax.swing.JTextField txtHomeCity;
    private javax.swing.JTextField txtHomeCustomerName;
    private javax.swing.JTextField txtHomeLandmark;
    public javax.swing.JTextField txtHomeMobileNo;
    private javax.swing.JTextField txtHomePinCode;
    private javax.swing.JTextField txtHomeState;
    private javax.swing.JTextField txtHomeStreetName;
    private javax.swing.JTextField txtOfficeCity;
    private javax.swing.JTextField txtOfficeCustAddress;
    private javax.swing.JTextField txtOfficeLandmark;
    private javax.swing.JTextField txtOfficePinCode;
    private javax.swing.JTextField txtOfficeState;
    private javax.swing.JTextField txtOfficeStreetName;
    private javax.swing.JTextArea txtTempCustAddress;
    private javax.swing.JTextField txtTempLandmark;
    private javax.swing.JTextField txtTempStreetName;
    // End of variables declaration//GEN-END:variables

    private void funSetHomeDeliveryAddress()
    {
	try
	{
	    int selectedIndex = tabbedPaneHomeDeliveryAddress2.getSelectedIndex();
	    if (selectedIndex == 0)//Home Address
	    {
		homeDeliveryAddressType = "Home";
		btnHomeAddress.setForeground(Color.BLACK);
		btnOfficeAddress.setForeground(Color.WHITE);
		btnTempAddress.setForeground(Color.WHITE);
	    }
	    else if (selectedIndex == 1)//Office Address
	    {
		homeDeliveryAddressType = "Office";
		btnHomeAddress.setForeground(Color.WHITE);
		btnOfficeAddress.setForeground(Color.BLACK);
		btnTempAddress.setForeground(Color.WHITE);
	    }
	    else if (selectedIndex == 2)//Temporary Address
	    {
		homeDeliveryAddressType = "Temporary";
		btnHomeAddress.setForeground(Color.WHITE);
		btnOfficeAddress.setForeground(Color.WHITE);
		btnTempAddress.setForeground(Color.BLACK);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funSetAddressDetail(String mobileNo)
    {
	try
	{
	    String sql = "select a.strCustomerCode,a.strCustomerName,a.longMobileNo,a.strBuldingCode,a.strCustAddress as strHomeAddress  "
		    + ",a.strStreetName,a.strLandmark,a.intPinCode,a.strCity,a.strState "
		    + ",a.strOfficeBuildingCode,a.strOfficeBuildingName as strOfficeAddress,a.strOfficeStreetName,a.strOfficeLandmark,a.intPinCode "
		    + ",a.strOfficeCity,a.strOfficeState "
		    + ",a.strTempAddress,a.strTempStreet,a.strTempLandmark "
		    + "from  tblcustomermaster a "
		    + "where longMobileNo like '%" + mobileNo + "%' ";
	    ResultSet rsCustomerDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsCustomerDtl.next())
	    {
		strCustomerCode = rsCustomerDtl.getString(1);
		txtHomeCustomerName.setText(rsCustomerDtl.getString(2));
		txtHomeMobileNo.setText(rsCustomerDtl.getString(3));

		txtHomeAddress.setText(rsCustomerDtl.getString(5));
		txtHomeStreetName.setText(rsCustomerDtl.getString(6));
		txtHomeLandmark.setText(rsCustomerDtl.getString(7));
		txtHomePinCode.setText(rsCustomerDtl.getString(8));
		txtHomeCity.setText(rsCustomerDtl.getString(9));
		txtHomeState.setText(rsCustomerDtl.getString(10));

		txtOfficeCustAddress.setText(rsCustomerDtl.getString(12));
		txtOfficeStreetName.setText(rsCustomerDtl.getString(13));
		txtOfficeLandmark.setText(rsCustomerDtl.getString(14));
		txtOfficePinCode.setText(rsCustomerDtl.getString(15));
		txtOfficeCity.setText(rsCustomerDtl.getString(16));
		txtOfficeState.setText(rsCustomerDtl.getString(17));

		txtTempCustAddress.setText(rsCustomerDtl.getString(18));
		txtTempStreetName.setText(rsCustomerDtl.getString(19));
		txtTempLandmark.setText(rsCustomerDtl.getString(20));

	    }
	    rsCustomerDtl.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funUpdateCustomerTempAddress()
    {
	try
	{
	    String sqlUpdate = "update tblcustomermaster  "
		    + "set strTempAddress='" + txtTempCustAddress.getText().trim() + "'"
		    + ",strTempStreet='" + txtTempStreetName.getText().trim() + "'"
		    + ",strTempLandmark='" + txtTempLandmark.getText().trim() + "' "
		    + "where longMobileNo like '%" + strMobileNo + "%' ";
	    clsGlobalVarClass.dbMysql.execute(sqlUpdate);

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funUpdateCustomerHomeAddress()
    {
	try
	{
	    String sqlUpdate = "update tblcustomermaster  "
		    + "set strCustAddress='" + txtHomeAddress.getText().trim() + "'"
		    + ",strStreetName='" + txtHomeStreetName.getText().trim() + "'"
		    + ",strLandmark='" + txtHomeLandmark.getText().trim() + "' "
		    + ",intPinCode='" + txtHomePinCode.getText().trim() + "'"
		    + ",strCity='" + txtHomeCity.getText().trim() + "'"
		    + ",strState='" + txtHomeState.getText().trim() + "' "
		    + "where longMobileNo like '%" + strMobileNo + "%' ";
	    clsGlobalVarClass.dbMysql.execute(sqlUpdate);

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funUpdateCustomerOfficeAddress()
    {
	try
	{
	    String sqlUpdate = "update tblcustomermaster  "
		    + "set strOfficeBuildingName='" + txtOfficeCustAddress.getText().trim() + "'"
		    + ",strOfficeStreetName='" + txtOfficeStreetName.getText().trim() + "'"
		    + ",strOfficeLandmark='" + txtOfficeLandmark.getText().trim() + "' "
		    + ",intPinCode='" + txtOfficePinCode.getText().trim() + "'"
		    + ",strOfficeCity='" + txtOfficeCity.getText().trim() + "'"
		    + ",strOfficeState='" + txtOfficeState.getText().trim() + "' "
		    + "where longMobileNo like '%" + strMobileNo + "%' ";
	    clsGlobalVarClass.dbMysql.execute(sqlUpdate);

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public String[] funGetCustomerAddressDetail()
    {
	String arrCustAddressDtl[] = new String[3];

	arrCustAddressDtl[0] = this.strCustomerCode;
	arrCustAddressDtl[1] = this.txtHomeCustomerName.getText();
	arrCustAddressDtl[2] = this.homeDeliveryAddressType;

	return arrCustAddressDtl;
    }
}
