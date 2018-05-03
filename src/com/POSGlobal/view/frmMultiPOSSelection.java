package com.POSGlobal.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUtility;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.table.DefaultTableModel;

public class frmMultiPOSSelection extends javax.swing.JDialog
{

    private Set<String> selectedPOSNameSet;
    private final clsUtility objUtility;
    private Set<String> selectedPOSCodeSet;
   
  


    /**
     * Creates new form FrmOkPopUp
     */
    public frmMultiPOSSelection(java.awt.Frame parent)
    {
        super(parent, true);
        //Dimension sz = Toolkit.getDefaultToolkit().getScreenSize();
        initComponents();

        setAlwaysOnTop(true);
        setLocationRelativeTo(null);
        funSetShortCutKeys();

        objUtility = new clsUtility();
        selectedPOSNameSet = new HashSet<>();
        selectedPOSCodeSet= new HashSet<>();
        funFillPOSSelectionTable();

        setVisible(true);
    }

    private void funSetShortCutKeys()
    {
        btnOk.setMnemonic('o');
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        panelBody = new javax.swing.JPanel();
        btnOk = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblMultiPOSSelction = new javax.swing.JTable();
        lblMsgType = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(null);
        setResizable(false);

        panelBody.setBackground(new java.awt.Color(255, 255, 255));
        panelBody.setOpaque(false);
        panelBody.setPreferredSize(new java.awt.Dimension(380, 179));

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

        jScrollPane1.setOpaque(false);

        tblMultiPOSSelction.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "POS", "SELECT", "POSCode"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.String.class, java.lang.Boolean.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean []
            {
                false, true, false
            };

            public Class getColumnClass(int columnIndex)
            {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblMultiPOSSelction.setRowHeight(30);
        tblMultiPOSSelction.getTableHeader().setReorderingAllowed(false);
        tblMultiPOSSelction.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblMultiPOSSelctionMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblMultiPOSSelction);
        if (tblMultiPOSSelction.getColumnModel().getColumnCount() > 0)
        {
            tblMultiPOSSelction.getColumnModel().getColumn(2).setMinWidth(2);
            tblMultiPOSSelction.getColumnModel().getColumn(2).setPreferredWidth(2);
            tblMultiPOSSelction.getColumnModel().getColumn(2).setMaxWidth(2);
        }

        lblMsgType.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        lblMsgType.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMsgType.setText("Multiple POS Selection");

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGap(135, 135, 135)
                .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(lblMsgType, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                .addComponent(lblMsgType, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBody, javax.swing.GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBody, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed

        if (selectedPOSNameSet.size() == 0)
        {
            new frmOkPopUp(null, "Please Select POS.", "Warning", 1).setVisible(true);
            return;
        }
        else
        {
            this.dispose();
        }
    }//GEN-LAST:event_btnOkActionPerformed

    private void btnOkKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnOkKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            if (selectedPOSNameSet.size() == 0)
            {
                new frmOkPopUp(null, "Please Select POS.", "Warning", 1).setVisible(true);
                return;
            }
            else
            {
                this.dispose();
            }
        }
    }//GEN-LAST:event_btnOkKeyPressed

    private void tblMultiPOSSelctionMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tblMultiPOSSelctionMouseClicked
    {//GEN-HEADEREND:event_tblMultiPOSSelctionMouseClicked
        funSetSelectedPOS();
    }//GEN-LAST:event_tblMultiPOSSelctionMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOk;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblMsgType;
    private javax.swing.JPanel panelBody;
    private javax.swing.JTable tblMultiPOSSelction;
    // End of variables declaration//GEN-END:variables

    private void funFillPOSSelectionTable()
    {
        try
        {
            DefaultTableModel dtm = (DefaultTableModel) tblMultiPOSSelction.getModel();
            dtm.setRowCount(0);
            
            String sql = "select a.strPosCode,a.strPosName  "
                    + "from tblposmaster a "
                    + "where a.strOperationalYN='Y' "
                    + "order by a.strPosCode ";
            ResultSet rsPOS = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsPOS.next())
            {
                Object row[] =
                {
                    rsPOS.getString(2), false,rsPOS.getString(1)
                };

                dtm.addRow(row);
            }
            rsPOS.close();

            tblMultiPOSSelction.setModel(dtm);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funSetSelectedPOS()
    {
        try
        {
            for (int i = 0; i < tblMultiPOSSelction.getRowCount(); i++)
            {
                if (Boolean.parseBoolean(tblMultiPOSSelction.getValueAt(i, 1).toString()))
                {
                    selectedPOSNameSet.add(tblMultiPOSSelction.getValueAt(i, 0).toString());
                    selectedPOSCodeSet.add(tblMultiPOSSelction.getValueAt(i, 2).toString());
                }
                else
                {
                    selectedPOSNameSet.remove(tblMultiPOSSelction.getValueAt(i, 0).toString());
                    //selectedPOSCodeSet.add(tblMultiPOSSelction.getValueAt(i, 2).toString());
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public Set funGetSelectedPOSCode()
    {
        return this.selectedPOSCodeSet;
    }

}
