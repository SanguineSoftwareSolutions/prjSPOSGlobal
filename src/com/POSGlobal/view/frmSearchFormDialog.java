package com.POSGlobal.view;

import com.POSGlobal.controller.clsAccountDtl;
import com.POSGlobal.controller.clsCreditBillReceipt;
import com.POSGlobal.controller.clsDebtorDtl;
import com.POSGlobal.controller.clsGlobalVarClass;
import static com.POSGlobal.controller.clsGlobalVarClass.gSearchFormName;
import static com.POSGlobal.controller.clsGlobalVarClass.gSearchMasterFormName;
import static com.POSGlobal.controller.clsGlobalVarClass.vArrSearchColumnSize;
import com.POSGlobal.controller.clsGuestRoomDtl;
import com.POSGlobal.controller.clsLinkupDtl;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class frmSearchFormDialog extends javax.swing.JDialog
{

    int currentRow = 0;
    String searchItem = "";
    List<clsAccountDtl> accountData;
    List<clsDebtorDtl> debtorData;
    List<String> listPrinters;
    List<clsLinkupDtl> listProductDtl;

    List<String> listProdDtlCol;
    List<String> listBrandDtlCol;
    Frame objFrame;
    boolean flgModel;
    private String CANCEL_ACTION = "cancel-search";
    private int rowSize = 14;
    private int tblPageNavigator = 0;
    private int rowCount;
    private List<clsCreditBillReceipt> listOfCreditBillsDtl;
    private List<String> listCreditBillDtlCol;

    public frmSearchFormDialog(java.awt.Frame parent, boolean modal)
    {

        super(parent, modal);
        initComponents();

        objFrame = parent;
        flgModel = modal;
        lblTitle.setText(clsGlobalVarClass.gSearchMasterFormName);
        searchItem = "SubGroup Code";
        this.setLocationRelativeTo(null);
        txtSearch.requestFocus();
        txtSearch.setText(clsGlobalVarClass.gSearchItem);
        clsGlobalVarClass.gQueryForSearch = clsGlobalVarClass.gQueryForSearch.trim();
        funShowTable(clsGlobalVarClass.gQueryForSearch);
        funSetShortCutKeys();
        //funSearchTextBoxClicked();

    }

    //This constuctor used for Searching Account list for webBooks
    public frmSearchFormDialog(java.awt.Frame parent, boolean modal, List<clsAccountDtl> accountList)
    {

        super(parent, "Account Master", modal);
        accountData = accountList;
        searchItem = "GL Code";
        initComponents();
        this.setLocationRelativeTo(null);
        txtSearch.requestFocus();
        txtSearch.setText(clsGlobalVarClass.gSearchItem);
        funShowAccountDtlTable(accountList);

    }

    //This constuctor used for Searching Debtor list for webBooks
    public frmSearchFormDialog(List<clsDebtorDtl> debtorList, boolean b,java.awt.Frame parent)
    {
	super(parent, "Debtor Master", b);
        debtorData = debtorList;
        searchItem = "Debt Code";
        initComponents();
        this.setLocationRelativeTo(null);
        txtSearch.requestFocus();
        txtSearch.setText(clsGlobalVarClass.gSearchItem);
        funShowDebtorDtlTable(debtorList);
    }
    
    //This constuctor used for Searching Account list for webBooks
    public frmSearchFormDialog(java.awt.Frame parent, List<clsLinkupDtl> listExciseLicense, boolean modal)
    {

        super(parent, "License Master", modal);
        listProductDtl = listExciseLicense;
        initComponents();
        this.setLocationRelativeTo(null);
        txtSearch.requestFocus();
        txtSearch.setText(clsGlobalVarClass.gSearchItem);
        funShowLicenceDtlTable(listExciseLicense);
    }

    //This constuctor used for Printer 
    public frmSearchFormDialog(java.awt.Frame parent, boolean modal, List<String> listObjects, String formName)
    {

        super(parent, formName, modal);
        listPrinters = listObjects;
        searchItem = "Printers";
        initComponents();
        this.setLocationRelativeTo(null);
        txtSearch.requestFocus();
        txtSearch.setText(clsGlobalVarClass.gSearchItem);
        funShowPrinters(listObjects);

    }

    //This constuctor used for webStock Product Master & webExcise Brand Master
    public frmSearchFormDialog(java.awt.Frame parent, boolean modal, List<clsLinkupDtl> listObjects, String formName, List<String> listColumns)
    {

        super(parent, formName, modal);
        listProductDtl = listObjects;
        listProdDtlCol = listColumns;
        searchItem = "Products";
        initComponents();
        this.setLocationRelativeTo(null);
        txtSearch.requestFocus();
        txtSearch.setText(clsGlobalVarClass.gSearchItem);
        funShowTable(listProductDtl, listColumns);
    }

    //This constuctor used for Searching PMS Guest Room Detail
    public frmSearchFormDialog(String formName, List<clsGuestRoomDtl> listOfGuestRoomDtl, java.awt.Frame parent, boolean modal)
    {

        super(parent, formName, modal);
        searchItem = "folioNo";
        initComponents();
        this.setLocationRelativeTo(null);
        txtSearch.requestFocus();
        txtSearch.setText(clsGlobalVarClass.gSearchItem);
        funShowGuestRoomDtlTable(listOfGuestRoomDtl);

    }

    //This constuctor used for credit bill receipts
    public frmSearchFormDialog(java.awt.Frame parent, boolean modal, List<clsCreditBillReceipt> listOfCreditBills, String formName, List<String> listColumns, boolean flag)
    {

        super(parent, formName, modal);
        this.listOfCreditBillsDtl = listOfCreditBills;
        this.listCreditBillDtlCol = listColumns;
        searchItem = "Ctedit Bills";
        initComponents();
        this.setLocationRelativeTo(null);
        txtSearch.requestFocus();
        txtSearch.setText(clsGlobalVarClass.gSearchItem);
        funShowCreditBillTable(listOfCreditBillsDtl, listCreditBillDtlCol);
    }

    private void funSetShortCutKeys()
    {
        btnCancel.setMnemonic('c');
        InputMap im = txtSearch.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = txtSearch.getActionMap();
        im.put(KeyStroke.getKeyStroke("ESCAPE"), CANCEL_ACTION);
        am.put(CANCEL_ACTION, new frmSearchFormDialog.CancelAction());
        //btnSearch.setMnemonic('s');

    }
    
    public frmSearchFormDialog(java.awt.Frame parent, List<clsLinkupDtl> listObjects, String formName, List<String> listColumns, boolean modal)
    {

        super(parent, formName, modal);
        listProductDtl = listObjects;
        listProdDtlCol = listColumns;
        searchItem = "SubGroup";
        initComponents();
        this.setLocationRelativeTo(null);
        txtSearch.requestFocus();
        txtSearch.setText(clsGlobalVarClass.gSearchItem);
        funShowTable(listProductDtl, listColumns);
    }

    private class CancelAction extends AbstractAction
    {

        @Override
        public void actionPerformed(ActionEvent ev)
        {

            if (txtSearch.hasFocus())
            {
                if (txtSearch.isFocusable() && txtSearch.getText().trim().length() == 0)
                {
                    dispose();
                }
                else
                {
                    txtSearch.setText("");
                }
            }
        }
    }

    public void funShowTable(String query)
    {
        try
        {
            //System.out.println("SHOW=  "+query);
            ResultSet rsSearch = clsGlobalVarClass.dbMysql.executeResultSet(query);
            DefaultTableModel menuTable = new DefaultTableModel();
            ResultSetMetaData rsSearchMetaData = rsSearch.getMetaData();
            int columnCount = rsSearchMetaData.getColumnCount();

            for (int colCount = 1; colCount <= columnCount; colCount++)
            {
                menuTable.addColumn(rsSearchMetaData.getColumnLabel(colCount).replaceAll("_", " "));
            }
            Object[] ob = new Object[columnCount];
            while (rsSearch.next())
            {
                for (int k = 0; k < columnCount; k++)
                {
                    ob[k] = rsSearch.getObject(k + 1);
                }
                menuTable.addRow(ob);
            }
            tblSearch.setModel(menuTable);
            for (int cntCol = 0; cntCol < clsGlobalVarClass.vArrSearchColumnSize.size(); cntCol++)
            {
                tblSearch.getColumnModel().getColumn(cntCol).setPreferredWidth(Integer.parseInt(vArrSearchColumnSize.elementAt(cntCol).toString()));
            }
            rsSearch.close();

            if (clsGlobalVarClass.gSearchFormName.equals("CustomerAddress"))
            {
                if (tblSearch.getRowCount() == 1)
                {
                    funRowSelection(0);
                    clsGlobalVarClass.gSearchItem = "";
                    dispose();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void funShowAccountDtlTable(List<clsAccountDtl> accountList)
    {
        try
        {
            DefaultTableModel accountTable = new DefaultTableModel()
            {
                @Override
                public boolean isCellEditable(int row, int column)
                {
                    //all cells false
                    return false;
                }
            };
            accountTable.getDataVector().removeAllElements();
            accountTable.addColumn("Account No.");
            accountTable.addColumn("Account Name");

            for (clsAccountDtl accDtl : accountList)
            {
                Object[] rows =
                {
                    accDtl.getStrAccountCode(), accDtl.getStrAccountName()
                };
                accountTable.addRow(rows);
            }

            tblSearch.setModel(accountTable);
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            tblSearch.getColumnModel().getColumn(0).setPreferredWidth(90);
            tblSearch.getColumnModel().getColumn(1).setPreferredWidth(70);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void funShowLicenceDtlTable(List<clsLinkupDtl> licenseList)
    {
        try
        {
            DefaultTableModel accountTable = new DefaultTableModel()
            {
                @Override
                public boolean isCellEditable(int row, int column)
                {
                    //all cells false
                    return false;
                }
            };
            accountTable.getDataVector().removeAllElements();
            accountTable.addColumn("License Code");
            accountTable.addColumn("License Name");

            for (clsLinkupDtl accDtl : licenseList)
            {
                Object[] rows =
                {
                    accDtl.getStrLinkupCode(), accDtl.getStrLinkupName()
                };
                accountTable.addRow(rows);
            }

            tblSearch.setModel(accountTable);
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            tblSearch.getColumnModel().getColumn(0).setPreferredWidth(90);
            tblSearch.getColumnModel().getColumn(1).setPreferredWidth(70);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void funShowGuestRoomDtlTable(List<clsGuestRoomDtl> listOfGuestRoomDtl)
    {
        try
        {
            DefaultTableModel guestRoomDtl = new DefaultTableModel()
            {
                @Override
                public boolean isCellEditable(int row, int column)
                {
                    //all cells false
                    return false;
                }
            };
            guestRoomDtl.getDataVector().removeAllElements();

            guestRoomDtl.addColumn("Guest Name");
            guestRoomDtl.addColumn("Room Desc.");
            guestRoomDtl.addColumn("Room No");
            guestRoomDtl.addColumn("Folio No.");
            guestRoomDtl.addColumn("Guest Code");

            for (clsGuestRoomDtl objGuestRoomDtl : listOfGuestRoomDtl)
            {
                Object[] rows =
                {
                    objGuestRoomDtl.getStrGuestName(), objGuestRoomDtl.getStrRoomDesc(), objGuestRoomDtl.getStrRoomNo(), objGuestRoomDtl.getStrFolioNo(), objGuestRoomDtl.getStrGuestCode()
                };
                guestRoomDtl.addRow(rows);
            }

            tblSearch.setModel(guestRoomDtl);
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            tblSearch.getColumnModel().getColumn(0).setPreferredWidth(90);
            tblSearch.getColumnModel().getColumn(1).setPreferredWidth(70);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void funShowPrinters(List<String> printerList)
    {
        try
        {
            DefaultTableModel accountTable = new DefaultTableModel()
            {
                @Override
                public boolean isCellEditable(int row, int column)
                {
                    //all cells false
                    return false;
                }
            };
            accountTable.getDataVector().removeAllElements();
            accountTable.addColumn("Printer Name");

            for (String printers : printerList)
            {
                Object[] rows =
                {
                    printers
                };
                accountTable.addRow(rows);
            }

            tblSearch.setModel(accountTable);
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            tblSearch.getColumnModel().getColumn(0).setPreferredWidth(90);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //This Function Used To Show WebStock Product Table & WebExcise Brand Table
    public void funShowTable(List<clsLinkupDtl> listObjects, List<String> listColumns)
    {
        try
        {
            DefaultTableModel accountTable = new DefaultTableModel()
            {
                @Override
                public boolean isCellEditable(int row, int column)
                {
                    //all cells false
                    return false;
                }
            };
            accountTable.getDataVector().removeAllElements();
            for (int cnt = 0; cnt < listColumns.size(); cnt++)
            {
                accountTable.addColumn(listColumns.get(cnt));
            }

            for (clsLinkupDtl object : listObjects)
            {
                Object[] rows =
                {
                    object.getStrLinkupCode(), object.getStrLinkupName()
                };
                accountTable.addRow(rows);
            }

            tblSearch.setModel(accountTable);
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            tblSearch.getColumnModel().getColumn(0).setPreferredWidth(90);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //This Function Used To Show credit bill receipts Table
    public void funShowCreditBillTable(List<clsCreditBillReceipt> listObjects, List<String> listColumns)
    {
        try
        {
            DefaultTableModel accountTable = new DefaultTableModel()
            {
                @Override
                public boolean isCellEditable(int row, int column)
                {
                    //all cells false
                    return false;
                }
            };
            accountTable.getDataVector().removeAllElements();
            for (int cnt = 0; cnt < listColumns.size(); cnt++)
            {
                accountTable.addColumn(listColumns.get(cnt));
            }

            for (clsCreditBillReceipt object : listObjects)
            {
                Object[] rows =
                {
                    object.getStrBillNo(), object.getDteBillDate(), object.getDblCreditAmount()
                };
                accountTable.addRow(rows);
            }

            tblSearch.setModel(accountTable);
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            tblSearch.getColumnModel().getColumn(0).setPreferredWidth(90);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void funRowSelection(int rowNo)
    {

        try
        {
            clsGlobalVarClass.gSearchItemClicked = true;
            clsGlobalVarClass.gArrListSearchData = new ArrayList();
            int colCount = tblSearch.getColumnCount();
            for (int cntCol = 0; cntCol < colCount; cntCol++)
            {
                clsGlobalVarClass.gArrListSearchData.add(tblSearch.getValueAt(rowNo, cntCol).toString());
            }
            clsGlobalVarClass.gSearchedItem = tblSearch.getValueAt(rowNo, 0).toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void funSetSearch(String text)
    {
        txtSearch.setText(text);
        txtSearch.requestFocus();
        if (searchItem.contains("SubGroup Code"))
        {
            funGenerateAdvQuery();
        }
        else if (searchItem.contains("GL Code"))
        {
            funSearchGLCode(text, accountData);
        }
        else if (searchItem.contains("Printers"))
        {
            funSearchPrinters(text, listPrinters);
        }
        else if (searchItem.contains("Products"))
        {
            funSearchProducts(text, listProductDtl);
        }

    }

    public String funCreateLikeQuery(String QueryForSearch, String AdvSearchItem)
    {
        String orderByClause = "", fromClauseQuery = "", searchFields = "", whereClause = "where", retQueryForAdvSearch = "";
        try
        {
            StringBuilder sbSearchFields = new StringBuilder(QueryForSearch.trim());
            sbSearchFields = sbSearchFields.delete(0, 6);
            int orderByIndex = sbSearchFields.indexOf("order by");
            //String orderByClause=sbSearchFields.substring(orderByIndex,sbSearchFields.length()).toString();
            if (orderByIndex > 0)
            {
                sbSearchFields = sbSearchFields.delete(orderByIndex, sbSearchFields.length());
            }
            //System.out.println("search field= "+sbSearchFields);
            int fromIndex = sbSearchFields.indexOf("from");
            if (sbSearchFields.toString().contains("where"))
            {
                fromClauseQuery = sbSearchFields.substring(fromIndex, sbSearchFields.indexOf("where")).toString();
            }
            else
            {
                fromClauseQuery = sbSearchFields.substring(fromIndex, sbSearchFields.length()).toString();
            }
            //System.out.println("From Clause="+fromClauseQuery);
            sbSearchFields = sbSearchFields.delete(fromIndex, sbSearchFields.length());
            //System.out.println("After Removing from="+sbSearchFields.toString().trim());
            String temSt = sbSearchFields.toString().trim();
            String[] arrSearchFields = temSt.split(",");
            for (int i = 0; i < arrSearchFields.length; i++)
            {
                String[] sp = arrSearchFields[i].split(" as ");
                if (i == 0)
                {
                    searchFields = sp[0];
                    whereClause = whereClause + " " + sp[0] + " like '%" + AdvSearchItem + "%'";
                }
                else
                {
                    searchFields = searchFields + "," + sp[0];
                }
                whereClause = whereClause + " or " + sp[0] + " like '%" + AdvSearchItem + "%'";
            }

            orderByClause = "order by strCustomerCode";
            retQueryForAdvSearch = "select " + searchFields + " " + fromClauseQuery + " " + whereClause + " " + orderByClause;
            //System.out.println(gQueryForAdvSearch);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return retQueryForAdvSearch;
    }

    public void funGenerateAdvQuery()
    {
        try
        {
            String searchFields = "", whereClause = "", groupByClause = "", orderByClause = "", fromClauseQuery = "";
            //System.out.println(clsGlobalVarClass.gQueryForSearch);
            if (clsGlobalVarClass.gSearchFormName.equals("Price") || clsGlobalVarClass.gSearchFormName.equals("MultiPrice"))
            {
                StringBuilder sbSearchFields = new StringBuilder(clsGlobalVarClass.gQueryForSearch);
                sbSearchFields = sbSearchFields.delete(0, 6);
                int orderByIndex = sbSearchFields.indexOf("order by");
                //String orderByClause=sbSearchFields.substring(orderByIndex,sbSearchFields.length()).toString();
                sbSearchFields = sbSearchFields.delete(orderByIndex, sbSearchFields.length());
                int fromIndex = sbSearchFields.indexOf("from");
                if (sbSearchFields.toString().contains("where"))
                {
                    fromClauseQuery = sbSearchFields.substring(fromIndex, sbSearchFields.indexOf("where")).toString();
                }
                else
                {
                    fromClauseQuery = sbSearchFields.substring(fromIndex, sbSearchFields.length()).toString();
                }
                if (sbSearchFields.toString().contains("where"))
                {
                    whereClause = sbSearchFields.substring(sbSearchFields.indexOf("where"), sbSearchFields.length());
                }
                //System.out.println("From Clause="+fromClauseQuery);.
                sbSearchFields = sbSearchFields.delete(fromIndex, sbSearchFields.length());
                //System.out.println("After Removing from="+sbSearchFields.toString().trim());
                String temSt = sbSearchFields.toString().trim();
                String[] arrSearchFields = temSt.split(",");

                if (whereClause.contains("where"))
                {
                    whereClause = whereClause + " and (a.strPosCode = b.strPosCode or a.strPOSCode='All') "
                            + "and a.strMenuCode = c.strMenuCode and a.strCostCenterCode = d.strCostCenterCode ";
                }
                else
                {
                    whereClause = whereClause + " where (a.strPosCode = b.strPosCode or a.strPOSCode='All') "
                            + "and a.strMenuCode = c.strMenuCode and a.strCostCenterCode = d.strCostCenterCode ";
                }
                for (int i = 0; i < arrSearchFields.length; i++)
                {
                    String[] sp = arrSearchFields[i].split(" as ");
                    if (i == 0)
                    {
                        if (sp.length > 1)
                        {
                            searchFields = sp[0] + " as " + sp[1];
                        }
                        else
                        {
                            searchFields = sp[0];
                        }
                        whereClause = whereClause + " and( " + sp[0] + " like '%" + clsGlobalVarClass.gAdvSearchItem + "%'";
                    }
                    else
                    {
                        if (sp.length > 1)
                        {
                            searchFields = searchFields + "," + sp[0] + " as " + sp[1];
                        }
                        else
                        {
                            searchFields = searchFields + "," + sp[0];
                        }
                    }
                    if (!(sp[0].startsWith("ifnull(") || sp[0].startsWith("IFNULL(") || sp[0].startsWith("'')") || sp[0].startsWith("'All')")))
                    {
                        whereClause = whereClause + " or " + sp[0] + " like '%" + clsGlobalVarClass.gAdvSearchItem + "%'";
                    }
                }
                whereClause = whereClause + ")";
                //System.out.println("Where clause="+whereClause);
                orderByClause = "order by '" + clsGlobalVarClass.gAdvSearchItem + "'";
                clsGlobalVarClass.gQueryForAdvSearch = "select " + searchFields + " " + fromClauseQuery + " " + whereClause + " " + orderByClause;
                //System.out.println("ADV Query 2=="+clsGlobalVarClass.gQueryForAdvSearch);
                funShowTable(clsGlobalVarClass.gQueryForAdvSearch);
            }
            else if (clsGlobalVarClass.gSearchFormName.equals("CustomerAddress"))
            {
                StringBuilder sbSearchFields = new StringBuilder(clsGlobalVarClass.gQueryForSearch);
                String[] splitUnion = clsGlobalVarClass.gQueryForSearch.split("union");
                String firstQuery = funCreateLikeQuery(splitUnion[0], clsGlobalVarClass.gAdvSearchItem);
                sbSearchFields = new StringBuilder(firstQuery);
                firstQuery = sbSearchFields.delete(sbSearchFields.indexOf("order by"), sbSearchFields.length()).toString();
                String secondQuery = funCreateLikeQuery(splitUnion[1], clsGlobalVarClass.gAdvSearchItem);
                clsGlobalVarClass.gQueryForAdvSearch = firstQuery + " union " + secondQuery;
                //System.out.println(clsGlobalVarClass.gQueryForAdvSearch);
                funShowTable(clsGlobalVarClass.gQueryForAdvSearch);
            }
            else
            {
                if (clsGlobalVarClass.gAdvSearchItem != null && clsGlobalVarClass.gAdvSearchItem.trim().length() > 0)
                {
                    StringBuilder sbSearchFields = new StringBuilder(clsGlobalVarClass.gQueryForSearch);
                    sbSearchFields = sbSearchFields.delete(0, 6);
                    int orderByIndex = sbSearchFields.indexOf("order by");
                    if (orderByIndex > 0)
                    {
                        sbSearchFields = sbSearchFields.delete(orderByIndex, sbSearchFields.length());
                    }
                    int groupByIndex = sbSearchFields.indexOf("group by");
                    if (groupByIndex > 0)
                    {
                        sbSearchFields = sbSearchFields.delete(groupByIndex, sbSearchFields.length());
                    }
                    //String orderByClause=sbSearchFields.substring(orderByIndex,sbSearchFields.length()).toString();
                    //sbSearchFields=sbSearchFields.delete(orderByIndex,sbSearchFields.length());
                    int fromIndex = sbSearchFields.indexOf("from");
                    int whereIndex = sbSearchFields.indexOf("where");
                    if (sbSearchFields.toString().contains("where"))
                    {
                        fromClauseQuery = sbSearchFields.substring(fromIndex, sbSearchFields.indexOf("where")).toString();
                    }
                    else
                    {
                        fromClauseQuery = sbSearchFields.substring(fromIndex, sbSearchFields.length()).toString();
                    }
                    //System.out.println("Where Index= "+whereIndex);
                    if (whereIndex > -1)
                    {
                        whereClause += sbSearchFields.substring(whereIndex, sbSearchFields.length()) + " and ( ";
                    }
                    else
                    {
                        whereClause += " where( ";
                    }
                    //System.out.println("From Clause="+fromClauseQuery);
                    sbSearchFields = sbSearchFields.delete(fromIndex, sbSearchFields.length());
                    //System.out.println("After Removing from="+sbSearchFields.toString().trim());
                    String temSt = sbSearchFields.toString().trim();
                    String[] arrSearchFields = temSt.split(",");
                    for (int i = 0; i < arrSearchFields.length; i++)
                    {
                        String[] sp = arrSearchFields[i].split(" as ");
                        if (i == 0)
                        {
                            if (sp.length > 1)
                            {
                                searchFields = sp[0] + " as " + sp[1];
                            }
                            else
                            {
                                searchFields = sp[0];
                            }
                            whereClause = whereClause + " " + sp[0] + " like '%" + clsGlobalVarClass.gAdvSearchItem + "%'";
                        }
                        else
                        {
                            if (sp.length > 1)
                            {
                                searchFields = searchFields + "," + sp[0] + " as " + sp[1];
                            }
                            else
                            {
                                searchFields = searchFields + "," + sp[0];
                            }
                        }
                        if (sp[0].trim().equalsIgnoreCase("'All')") && gSearchFormName.equalsIgnoreCase("TableMaster"))//Table Master
                        {
                            String replacedString = sp[0].replace("'All')", "'All'");
                            whereClause = whereClause + " or " + replacedString + " like '%" + clsGlobalVarClass.gAdvSearchItem + "%'";
                        }
                        else if (sp[0].trim().equalsIgnoreCase("'NO')") && gSearchFormName.equalsIgnoreCase("DeliveryPersonMaster"))
                        {
                            String replacedString = sp[0].replace(")", ",'YES','NO')");//Delivery Person Master
                            whereClause = whereClause + " or " + replacedString + " like '%" + clsGlobalVarClass.gAdvSearchItem + "%'";
                        }
                        else if (sp[0].trim().equalsIgnoreCase("0)") && gSearchFormName.equalsIgnoreCase("VoidAdvOrder"))
                        {
                            whereClause = whereClause;
                        }

                        else if (!(sp[0].startsWith("ifnull(") || sp[0].startsWith("IFNULL(") || sp[0].startsWith("'')") || sp[0].startsWith("sum")))
                        {
                            whereClause = whereClause + " or " + sp[0] + " like '%" + clsGlobalVarClass.gAdvSearchItem + "%'";
                        }
                        //whereClause=whereClause+" or "+sp[0]+" like '%"+clsGlobalVarClass.gAdvSearchItem+"%'";
                    }
                    whereClause += " )";
                    //System.out.println("Where clause="+whereClause);
                    clsGlobalVarClass.gQueryForAdvSearch = "select " + searchFields + " " + fromClauseQuery + " "
                            + whereClause + " " + orderByClause;
                    if (clsGlobalVarClass.gSearchFormName.equals("TableMasterForKOT"))
                    {
                        whereClause += " and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' ";
                        clsGlobalVarClass.gQueryForAdvSearch = "select " + searchFields + " " + fromClauseQuery + " "
                                + whereClause + " " + orderByClause;
                        clsGlobalVarClass.gQueryForAdvSearch += " Limit 10 ";
                    }
                    funShowTable(clsGlobalVarClass.gQueryForAdvSearch);
                }
                else
                {
                    funShowTable(clsGlobalVarClass.gQueryForSearch);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funSearchGLCode(String searchText, List<clsAccountDtl> accountData)
    {
        ArrayList arrayListtemp = new ArrayList();
        for (int cnt = 0; cnt < accountData.size(); cnt++)
        {
            clsAccountDtl obj = (clsAccountDtl) accountData.get(cnt);
            if (obj.getStrAccountName().toLowerCase().contains(searchText.toLowerCase()))
            {
                arrayListtemp.add(accountData.get(cnt));
            }
        }
        funShowAccountDtlTable(arrayListtemp);
    }

    private void funSearchPrinters(String searchText, List<String> printerList)
    {
        ArrayList arrayListtemp = new ArrayList();
        for (int cnt = 0; cnt < listPrinters.size(); cnt++)
        {
            String printer = listPrinters.get(cnt);
            if (printer.toLowerCase().contains(searchText.toLowerCase()))
            {
                arrayListtemp.add(listPrinters.get(cnt));
            }
        }
        funShowPrinters(arrayListtemp);
    }

    //Search WebStock Product
    private void funSearchProducts(String searchText, List<clsLinkupDtl> listProdDtl)
    {
        List<clsLinkupDtl> arrayListtemp = new ArrayList<clsLinkupDtl>();
        for (clsLinkupDtl objProdDtl : listProdDtl)
        {
            if (objProdDtl.getStrLinkupCode().toLowerCase().contains(searchText.toLowerCase())
                    || objProdDtl.getStrLinkupName().toLowerCase().contains(searchText.toLowerCase()))
            {
                arrayListtemp.add(objProdDtl);
            }
        }
        funShowTable(arrayListtemp, listProdDtlCol);
    }

    private void funSearchTextBoxClicked()
    {
        if (txtSearch.getText().length() == 0)
        {
            new frmAlfaNumericKeyBoard(null, true, "1", "Enter " + clsGlobalVarClass.gFormNameOnKeyBoard).setVisible(true);
            txtSearch.setText(clsGlobalVarClass.gKeyboardValue);
            clsGlobalVarClass.gAdvSearchItem = txtSearch.getText();
            funSetSearch(clsGlobalVarClass.gAdvSearchItem);
        }
        else
        {
            new frmAlfaNumericKeyBoard(null, true, txtSearch.getText(), "1", "Enter " + clsGlobalVarClass.gFormNameOnKeyBoard).setVisible(true);
            txtSearch.setText(clsGlobalVarClass.gKeyboardValue);
            clsGlobalVarClass.gAdvSearchItem = txtSearch.getText();
            funSetSearch(clsGlobalVarClass.gAdvSearchItem);
        }
    }

    private void funDownArrowPressed()
    {
        int rowCount = tblSearch.getRowCount() - 1;
        if (rowCount > currentRow)
        {
            currentRow++;
            tblSearch.changeSelection(currentRow, 0, false, false);
        }
        if (currentRow == rowCount)
        {
            currentRow = 0;
            tblSearch.changeSelection(currentRow, 0, false, false);
        }
    }

    private void funUpArrowPressed()
    {
        int rowCount = tblSearch.getRowCount() - 1;

        if (currentRow >= 0)
        {
            currentRow--;
            tblSearch.changeSelection(currentRow, 0, false, false);
        }
        if (currentRow < 0)
        {
            currentRow = rowCount;
            tblSearch.changeSelection(currentRow, 0, false, false);
        }
    }
    
    public void funShowDebtorDtlTable(List<clsDebtorDtl> debtorList)
    {
        try
        {
            DefaultTableModel debtorTable = new DefaultTableModel()
            {
                @Override
                public boolean isCellEditable(int row, int column)
                {
                    //all cells false
                    return false;
                }
            };
            debtorTable.getDataVector().removeAllElements();
            debtorTable.addColumn("Debtor No.");
            debtorTable.addColumn("Debtor Name");

            for (clsDebtorDtl debtDtl : debtorList)
            {
                Object[] rows =
                {
                    debtDtl.getStrDebtorCode(), debtDtl.getStrDebtorName()
                };
                debtorTable.addRow(rows);
            }

            tblSearch.setModel(debtorTable);
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            tblSearch.getColumnModel().getColumn(0).setPreferredWidth(90);
            tblSearch.getColumnModel().getColumn(1).setPreferredWidth(70);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        panelBody = new javax.swing.JPanel();
        scrollPane = new javax.swing.JScrollPane();
        tblSearch = new javax.swing.JTable();
        btnCancel = new javax.swing.JButton();
        txtSearch = new javax.swing.JTextField();
        btnDown = new javax.swing.JButton();
        btnUp = new javax.swing.JButton();
        lblBackground = new javax.swing.JLabel();
        panelHeader = new javax.swing.JPanel();
        lblModuleName = new javax.swing.JLabel();
        lblUserCode = new javax.swing.JLabel();
        lblDate = new javax.swing.JLabel();
        lblPosName = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        btnCancel1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(800, 700));
        setUndecorated(true);
        getContentPane().setLayout(null);

        panelBody.setBackground(new java.awt.Color(255, 255, 255));
        panelBody.setLayout(null);

        scrollPane.setMinimumSize(new java.awt.Dimension(452, 402));

        tblSearch.setForeground(new java.awt.Color(1, 1, 1));
        tblSearch.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {},
                {},
                {},
                {}
            },
            new String []
            {

            }
        ));
        tblSearch.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblSearch.setGridColor(new java.awt.Color(255, 255, 255));
        tblSearch.setRowHeight(35);
        tblSearch.setSelectionBackground(new java.awt.Color(9, 126, 233));
        tblSearch.setSelectionForeground(new java.awt.Color(254, 254, 254));
        tblSearch.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblSearchMouseClicked(evt);
            }
        });
        tblSearch.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                tblSearchKeyPressed(evt);
            }
        });
        scrollPane.setViewportView(tblSearch);

        panelBody.add(scrollPane);
        scrollPane.setBounds(0, 60, 800, 510);

        btnCancel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
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
        panelBody.add(btnCancel);
        btnCancel.setBounds(720, 10, 70, 40);

        txtSearch.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtSearchMouseClicked(evt);
            }
        });
        txtSearch.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtSearchActionPerformed(evt);
            }
        });
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtSearchKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt)
            {
                txtSearchKeyReleased(evt);
            }
        });
        panelBody.add(txtSearch);
        txtSearch.setBounds(110, 10, 610, 40);

        btnDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgDownArrow.png"))); // NOI18N
        btnDown.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDownActionPerformed(evt);
            }
        });
        panelBody.add(btnDown);
        btnDown.setBounds(60, 10, 40, 40);

        btnUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgUpArrow.png"))); // NOI18N
        btnUp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnUpActionPerformed(evt);
            }
        });
        panelBody.add(btnUp);
        btnUp.setBounds(10, 10, 40, 40);

        lblBackground.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgBGJPOS.png"))); // NOI18N
        lblBackground.setMinimumSize(new java.awt.Dimension(800, 600));
        lblBackground.setPreferredSize(new java.awt.Dimension(800, 600));
        panelBody.add(lblBackground);
        lblBackground.setBounds(-10, 0, 800, 590);

        getContentPane().add(panelBody);
        panelBody.setBounds(0, 30, 800, 570);

        panelHeader.setBackground(new java.awt.Color(69, 164, 238));
        panelHeader.setPreferredSize(new java.awt.Dimension(800, 30));

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        lblModuleName.setText("SPOS - Search");

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
        panelHeader.setBounds(0, 0, 800, 30);

        btnCancel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnCancel1.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn1.png"))); // NOI18N
        btnCancel1.setText("CLOSE");
        btnCancel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancel1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn2.png"))); // NOI18N
        btnCancel1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCancel1MouseClicked(evt);
            }
        });
        btnCancel1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCancel1ActionPerformed(evt);
            }
        });
        getContentPane().add(btnCancel1);
        btnCancel1.setBounds(720, 10, 70, 40);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSearchMouseClicked
        int rowNo = tblSearch.getSelectedRow();
        funRowSelection(rowNo);
        clsGlobalVarClass.gSearchItem = "";
        dispose();
    }//GEN-LAST:event_tblSearchMouseClicked

    private void tblSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblSearchKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblSearchKeyPressed

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        dispose();
    }//GEN-LAST:event_btnCancelMouseClicked

    private void txtSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSearchMouseClicked
        funSearchTextBoxClicked();
    }//GEN-LAST:event_txtSearchMouseClicked

    private void txtSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyPressed
        // TODO add your handling code here:        
        //System.out.println("Key Code= "+evt.getKeyCode());
        //System.out.println(rowCount);
        if (evt.getKeyCode() == 10) // Check for Enter Key Press
        {
            clsGlobalVarClass.gAdvSearchItem = txtSearch.getText();
            int rowNo = tblSearch.getSelectedRow();
            if (rowNo > -1)
            {
                funRowSelection(rowNo);
            }
            dispose();
        }
        if (evt.getKeyCode() == 40) // Check for Down Arrow Pressed
        {
            funDownArrowPressed();
        }
        if (evt.getKeyCode() == 38) // Check for Up Arrow Pressed
        {
            funUpArrowPressed();
        }
        //System.out.println("Current Row= "+currentRow);
    }//GEN-LAST:event_txtSearchKeyPressed

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() != 38 && evt.getKeyCode() != 40)
        {
            if (searchItem.equals("GL Code"))
            {
                clsGlobalVarClass.gAdvSearchItem = txtSearch.getText();
                funSetSearch(clsGlobalVarClass.gAdvSearchItem);
            }
            else
            {
                if (clsGlobalVarClass.gSearchFormName.equalsIgnoreCase("TableMasterForKOT"))
                {
                    if (txtSearch.getText().trim().length() > 2)
                    {
                        clsGlobalVarClass.gAdvSearchItem = txtSearch.getText();
                        funSetSearch(clsGlobalVarClass.gAdvSearchItem);
                    }
                }
                else
                {
                    currentRow = 0;
                    //System.out.println("After Type current row= "+currentRow);
                    clsGlobalVarClass.gAdvSearchItem = txtSearch.getText();
                    funSetSearch(clsGlobalVarClass.gAdvSearchItem);
                }
            }
        }
    }//GEN-LAST:event_txtSearchKeyReleased

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDownActionPerformed
        // TODO add your handling code here:
        txtSearch.requestFocus();
        int rowCount = tblSearch.getRowCount();
        rowCount = tblSearch.getRowCount();
        final JViewport scrollPaneViewPort = scrollPane.getViewport();
        if (rowCount > rowSize)
        {
            if (currentRow < rowCount)
            {
                tblPageNavigator++;
                currentRow = (rowSize * tblPageNavigator);
                if (currentRow < rowCount)
                {
                    Rectangle currentCellRectangle = tblSearch.getCellRect(currentRow, 0, true);
                    int scrollPaneExtendedHeight = scrollPaneViewPort.getExtentSize().height;
                    currentCellRectangle.y += scrollPaneExtendedHeight;
                    tblSearch.scrollRectToVisible(currentCellRectangle);
                    tblSearch.changeSelection(currentRow, 0, false, false);
                }
                else
                {
                    currentRow = rowCount - 1;
                    tblPageNavigator--;
                    Rectangle currentCellRectangle = tblSearch.getCellRect(currentRow, 0, true);
                    int scrollPaneExtendedHeight = scrollPaneViewPort.getExtentSize().height;
                    currentCellRectangle.y += scrollPaneExtendedHeight;
                    tblSearch.scrollRectToVisible(currentCellRectangle);
                    tblSearch.changeSelection(currentRow, 0, false, false);
                }
            }
        }
    }//GEN-LAST:event_btnDownActionPerformed

    private void btnUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpActionPerformed
        // TODO add your handling code here:
        txtSearch.requestFocus();
        rowCount = tblSearch.getRowCount();
        final JViewport scrollPaneViewPort = scrollPane.getViewport();
        if (rowCount > rowSize)
        {
            if (currentRow >= rowSize)
            {
                tblPageNavigator--;
                currentRow = (rowSize * tblPageNavigator);

                if (currentRow > 0)
                {
                    Rectangle currentCellRectangle = tblSearch.getCellRect(currentRow, 0, true);
                    int scrollPaneExtendedHeight = scrollPaneViewPort.getExtentSize().height;
                    currentCellRectangle.y -= scrollPaneExtendedHeight;
                    tblSearch.scrollRectToVisible(currentCellRectangle);
                    tblSearch.changeSelection(currentRow, 0, false, false);
                }
                else
                {
                    currentRow = 0;
                    tblPageNavigator = 0;
                    Rectangle currentCellRectangle = tblSearch.getCellRect(currentRow, 0, true);
                    int scrollPaneExtendedHeight = scrollPaneViewPort.getExtentSize().height;
                    currentCellRectangle.y -= scrollPaneExtendedHeight;
                    tblSearch.scrollRectToVisible(currentCellRectangle);
                    tblSearch.changeSelection(currentRow, 0, false, false);
                }
            }
        }
    }//GEN-LAST:event_btnUpActionPerformed

    private void btnCancel1MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnCancel1MouseClicked
    {//GEN-HEADEREND:event_btnCancel1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancel1MouseClicked

    private void btnCancel1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCancel1ActionPerformed
    {//GEN-HEADEREND:event_btnCancel1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancel1ActionPerformed

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtSearchActionPerformed
    {//GEN-HEADEREND:event_txtSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnCancel1;
    private javax.swing.JButton btnDown;
    private javax.swing.JButton btnUp;
    private javax.swing.JLabel lblBackground;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTable tblSearch;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
