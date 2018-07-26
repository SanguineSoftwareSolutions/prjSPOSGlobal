/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSGlobal.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsPosConfigFile;
import com.POSGlobal.controller.clsDatabaseConnection;
import com.POSGlobal.view.frmShowTextFile;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.Formatter;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;

public class frmStockFlash extends javax.swing.JFrame
{

    DefaultTableModel dm, dm1;
    clsDatabaseConnection db;
    private String exportFormName, rDate;
    private String sql, reportName;
    Date installDate;
    private Double dblStkIn, dblStkOut, dblSale, dblBal, dblOpening;
    private Vector vItemCode;
    private String groupWiseSql;
    private final DecimalFormat gDecimalFormat = clsGlobalVarClass.funGetGlobalDecimalFormatter();

    public frmStockFlash()
    {
	initComponents();
	funSetLookAndFeel();

	ResultSet rs = null;
	try
	{
	    clsPosConfigFile pc = new clsPosConfigFile();

	    if (clsGlobalVarClass.gShowOnlyLoginPOSReports)
	    {
		cmbPosCode.addItem(clsGlobalVarClass.gPOSName + "                     " + clsGlobalVarClass.gPOSCode);
	    }
	    else
	    {
		cmbPosCode.addItem("All");
		sql = "select strPosName,strPosCode from tblposmaster";
		rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		while (rs.next())
		{
		    cmbPosCode.addItem(rs.getString(1) + "                     " + rs.getString(2));
		}
		rs.close();
	    }

	    groupWiseSql = "select strGroupName from tblgrouphd group by strGroupName";
	    rs = clsGlobalVarClass.dbMysql.executeResultSet(groupWiseSql);
	    cmbGroupWise.addItem("All");
	    while (rs.next())
	    {
		cmbGroupWise.addItem(rs.getString(1));
	    }

	    lblUserCode.setText(clsGlobalVarClass.gUserCode);
	    lblPosName.setText(clsGlobalVarClass.gPOSName);
	    funSetShortCutKeys();
	    java.util.Date dt1 = new java.util.Date();
	    int day = dt1.getDate();
	    int month = dt1.getMonth() + 1;
	    int year = dt1.getYear() + 1900;
	    String dte = day + "-" + month + "-" + year;
	    lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
	    java.util.Date date = new SimpleDateFormat("dd-MM-yyyy").parse(clsGlobalVarClass.gPOSDateToDisplay);
	    rDate = dte;
	    dteFromDate.setDate(date);
	    dteToDate.setDate(date);
	    btnStockView.setText("<html> VIEW </html>");

	    dm = new javax.swing.table.DefaultTableModel(
		    new Object[][]
		    {

		    },
		    new String[]
		    {
			"Item Code", "Item Name", "Opg Stock", "Stock In", "Stock Out", "Sale", "Bal"
		    }
	    )
	    {
		boolean[] canEdit = new boolean[]
		{
		    false, false, false, false, false, false, false
		};

		public boolean isCellEditable(int rowIndex, int columnIndex)
		{
		    return canEdit[columnIndex];
		}
	    };

	    dm1 = new DefaultTableModel(
		    new Object[][]
		    {
		    },
		    new String[]
		    {
			"", "", "", "", "", "", ""
		    }
	    )
	    {
		boolean[] canEdit = new boolean[]
		{
		    false, false, false, false, false, false, false
		};

		public boolean isCellEditable(int rowIndex, int columnIndex)
		{
		    return canEdit[columnIndex];
		}
	    };

	    tblStock.setModel(dm);
	    tblTotal.setModel(dm1);

	    if (cmbReportType.getSelectedItem().toString().equals("ReOrder"))
	    {
		btnProductionOrder.setEnabled(true);
	    }
	    else
	    {
		btnProductionOrder.setEnabled(false);
	    }

	    if (clsGlobalVarClass.gNoOfDaysReportsView != 0)
	    {
		try
		{

		    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		    final Date userDateRange = dateFormat.parse(clsGlobalVarClass.gPOSOnlyDateForTransaction);
		    int days = userDateRange.getDate() - clsGlobalVarClass.gNoOfDaysReportsView;
		    userDateRange.setDate(days);

		    dteFromDate.getJCalendar().setMinSelectableDate(userDateRange);

		    dteFromDate.getDateEditor().addPropertyChangeListener(new PropertyChangeListener()
		    {
			@Override
			public void propertyChange(PropertyChangeEvent e)
			{
			    if ("date".equals(e.getPropertyName()))
			    {
				Date dateChooserValue = (Date) e.getNewValue();

				if (clsGlobalVarClass.gNoOfDaysReportsView != 0 && dateChooserValue.before(userDateRange))
				{
				    try
				    {
					java.util.Date date = new SimpleDateFormat("dd-MM-yyyy").parse(clsGlobalVarClass.gPOSDateToDisplay);
					dteFromDate.setDate(date);
				    }
				    catch (Exception ex)
				    {
					ex.printStackTrace();
				    }
				}
			    }
			}
		    });
		}
		catch (Exception e)
		{
		    e.printStackTrace();
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funSetShortCutKeys()
    {
	btnBack.setMnemonic('c');
	btnExport.setMnemonic('e');
	btnPrint.setMnemonic('p');
	btnStockView.setMnemonic('v');
    }

    /**
     * this method is used for truncate tblstktemp
     */
    public void truncate()
    {
	try
	{
	    String delete = "Delete from tblstktemp";
	    clsGlobalVarClass.dbMysql.execute(delete);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    /**
     * show stock Flash
     */
    private void funShowStockFlash()
    {
	try
	{
	    String balStockSign = cmbBalAmtSign.getSelectedItem().toString();
	    String showZeroBalStk = cmbShowZeroBalStk.getSelectedItem().toString();

	    btnProductionOrder.setEnabled(false);
	    dblStkIn = new Double("0.00");
	    dblStkOut = new Double("0.00");
	    dblBal = new Double("0.00");
	    dblOpening = new Double("0.00");
	    dblSale = new Double("0.00");
	    String posCode = cmbPosCode.getSelectedItem().toString();
	    String itemType = cmbItemType.getSelectedItem().toString();
	    String reportType = cmbReportType.getSelectedItem().toString();
	 
	    
	    
	    int cal = clsGlobalVarClass.funCalculateStock(dteFromDate.getDate(), dteToDate.getDate(), posCode, itemType, reportType);

	    dm = new javax.swing.table.DefaultTableModel(
		    new Object[][]
		    {

		    },
		    new String[]
		    {
			"Group", "SubGroup", "Item Name", "Purchase Rate", "Opg Stock", "Stock In", "Stock Out", "Sale", "Bal"
		    }
	    )
	    {
		boolean[] canEdit = new boolean[]
		{
		    false, false, false, false, false, false, false, false, false
		};

		public boolean isCellEditable(int rowIndex, int columnIndex)
		{
		    return canEdit[columnIndex];
		}
	    };

	    dm1 = new javax.swing.table.DefaultTableModel(
		    new Object[][]
		    {

		    },
		    new String[]
		    {
			"", "", "", "", "", "", "", "", ""
		    }
	    )
	    {
		boolean[] canEdit = new boolean[]
		{
		    false, false, false, false, false, false, false, false, false
		};

		public boolean isCellEditable(int rowIndex, int columnIndex)
		{
		    return canEdit[columnIndex];
		}
	    };

	    Object[] ob = new Object[9];

	    String sqlStock = "select strGroupName,strSubgroupName,strItemName,strPOSCode"
		    + " ,intOpening,intIn,intOut,intSale,intBalance,dblPurchaseRate "
		    + " from tblitemcurrentstk "
		    + " where strGroupName=if('All'='" + cmbGroupWise.getSelectedItem().toString().trim() + "'"
		    + ",strGroupName,'" + cmbGroupWise.getSelectedItem().toString().trim() + "') ";
	    if (balStockSign.equalsIgnoreCase("Positive"))
	    {
		if (showZeroBalStk.equals("Yes"))
		{
		    sqlStock += " and intBalance >= 0 ";
		}
		else
		{
		    sqlStock += " and intBalance > 0 ";
		}
	    }
	    else if (balStockSign.equalsIgnoreCase("Negative"))
	    {
		if (showZeroBalStk.equals("Yes"))
		{
		    sqlStock += " and intBalance <= 0 ";
		}
		else
		{
		    sqlStock += " and intBalance < 0 ";
		}
	    }
	    sqlStock += " order by strItemName";
	    System.out.println(sqlStock);
	    ResultSet stkRs = clsGlobalVarClass.dbMysql.executeResultSet(sqlStock);

	    while (stkRs.next())
	    {
		ob[0] = stkRs.getString(1);//Group
		ob[1] = stkRs.getString(2);//Subgroup
		ob[2] = stkRs.getString(3);//Item Name
		ob[3] = gDecimalFormat.format(stkRs.getDouble(10));//pur rate		
		ob[4] = stkRs.getString(5);//Op Stock
		ob[5] = stkRs.getString(6);//Stk In
		ob[6] = stkRs.getString(7);//Stk Out
		ob[7] = stkRs.getString(8);//Sale
		ob[8] = stkRs.getString(9);//Balance

		dblOpening = dblOpening + new Double(stkRs.getString(5));
		dblStkIn = dblStkIn + new Double(stkRs.getString(6));
		dblStkOut = dblStkOut + new Double(stkRs.getString(7));
		dblSale = dblSale + new Double(stkRs.getString(8));
		dblBal = dblBal + new Double(stkRs.getString(9));
		if (!ob[8].equals(0))
		{
		    dm.addRow(ob);
		}
	    }

	    DecimalFormat objDecFormat = new DecimalFormat("###.##");
	    Object[] ob1 =
	    {
		"TOTAL", "", "", "", objDecFormat.format(dblOpening), objDecFormat.format(dblStkIn), objDecFormat.format(dblStkOut), objDecFormat.format(dblSale), objDecFormat.format(dblBal)
	    };
	    dm1.addRow(ob1);
	    tblStock.setModel(dm);
	    tblTotal.setModel(dm1);

	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	    tblStock.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
	    tblStock.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
	    tblStock.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
	    tblStock.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
	    tblStock.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);
	    tblStock.getColumnModel().getColumn(8).setCellRenderer(rightRenderer);
	    tblStock.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    tblStock.getColumnModel().getColumn(0).setPreferredWidth(120);//Group
	    tblStock.getColumnModel().getColumn(1).setPreferredWidth(120);//Subgroup
	    tblStock.getColumnModel().getColumn(2).setPreferredWidth(185);//ItemName
	    tblStock.getColumnModel().getColumn(3).setPreferredWidth(70);//pur rate
	    tblStock.getColumnModel().getColumn(4).setPreferredWidth(70);//Op Stk
	    tblStock.getColumnModel().getColumn(5).setPreferredWidth(70);//Stk In
	    tblStock.getColumnModel().getColumn(6).setPreferredWidth(70);//Stk Out
	    tblStock.getColumnModel().getColumn(7).setPreferredWidth(70);//Sale
	    tblStock.getColumnModel().getColumn(8).setPreferredWidth(70);//Balance

	    DefaultTableCellRenderer rightRenderer1 = new DefaultTableCellRenderer();
	    rightRenderer1.setHorizontalAlignment(JLabel.RIGHT);
	    tblTotal.getColumnModel().getColumn(3).setCellRenderer(rightRenderer1);
	    tblTotal.getColumnModel().getColumn(4).setCellRenderer(rightRenderer1);
	    tblTotal.getColumnModel().getColumn(5).setCellRenderer(rightRenderer1);
	    tblTotal.getColumnModel().getColumn(6).setCellRenderer(rightRenderer1);
	    tblTotal.getColumnModel().getColumn(7).setCellRenderer(rightRenderer1);
	    tblTotal.getColumnModel().getColumn(8).setCellRenderer(rightRenderer1);
	    tblTotal.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    tblTotal.getColumnModel().getColumn(0).setPreferredWidth(120);//Group
	    tblTotal.getColumnModel().getColumn(1).setPreferredWidth(120);//Subgroup
	    tblTotal.getColumnModel().getColumn(2).setPreferredWidth(185);//ItemName
	    tblTotal.getColumnModel().getColumn(3).setPreferredWidth(70);//Op Stk
	    tblTotal.getColumnModel().getColumn(4).setPreferredWidth(70);//Stk In
	    tblTotal.getColumnModel().getColumn(5).setPreferredWidth(70);//Stk Out
	    tblTotal.getColumnModel().getColumn(6).setPreferredWidth(70);//Sale
	    tblTotal.getColumnModel().getColumn(7).setPreferredWidth(70);//Balance
	    tblTotal.getColumnModel().getColumn(8).setPreferredWidth(70);//Balance
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    /**
     * Show Re order Flash
     */
    private void funShowReOrderFlash()
    {
	try
	{
	    btnProductionOrder.setEnabled(false);
	    boolean flgReOrder = false;
	    dblStkIn = new Double("0.00");
	    dblStkOut = new Double("0.00");
	    dblBal = new Double("0.00");
	    dblOpening = new Double("0.00");
	    dblSale = new Double("0.00");
	    double reorderQty = 0, openProductionQty = 0;
	    String posCode = cmbPosCode.getSelectedItem().toString();
	    String itemType = cmbItemType.getSelectedItem().toString();
	    String reportType = cmbReportType.getSelectedItem().toString();
	    clsGlobalVarClass.funCalculateStock(dteFromDate.getDate(), dteToDate.getDate(), posCode, itemType, reportType);

	    dm = new javax.swing.table.DefaultTableModel(
		    new Object[][]
		    {

		    },
		    new String[]
		    {
			"Group", "SubGroup", "Item Name", "Opg Stock", "Stock In", "Stock Out", "Sale", "Bal", "Order Qty", "Min Level", "Max Level", "ReOrder Qty"
		    }
	    )
	    {
		boolean[] canEdit = new boolean[]
		{
		    false, false, false, false, false, false, false, false, false, false, false, false
		};

		public boolean isCellEditable(int rowIndex, int columnIndex)
		{
		    return canEdit[columnIndex];
		}
	    };

	    dm1 = new javax.swing.table.DefaultTableModel(
		    new Object[][]
		    {

		    },
		    new String[]
		    {
			"", "", "", "", "", "", "", "", "", "", "", ""
		    }
	    )
	    {
		boolean[] canEdit = new boolean[]
		{
		    false, false, false, false, false, false, false, false, false, false, false, false
		};

		public boolean isCellEditable(int rowIndex, int columnIndex)
		{
		    return canEdit[columnIndex];
		}
	    };
	    Object[] ob = new Object[12];

	    vItemCode = new java.util.Vector();
	    String sqlStock = "select a.strGroupName,a.strSubgroupName,a.strItemName,a.strPOSCode,a.intOpening"
		    + ",a.intIn,a.intOut,a.intSale,a.intBalance,b.dblMinLevel,b.dblMaxLevel"
		    + ",b.dblMaxLevel-a.intBalance-ifnull(c.dblOrderQty,0.00) as ReorderQty"
		    + ",a.strItemCode,ifnull(c.dblOrderQty,0.00) as OpenProductionQty "
		    + "from tblitemcurrentstk a "
		    + "left outer join tblitemmaster b on a.strItemCode=b.strItemCode "
		    + "left outer join tblproductiondtl c on a.strItemCode=c.strItemCode "
		    + "left outer join tblproductionhd d on c.strProductionCode=d.strProductionCode and d.strClose='N' "
		    + "where a.strGroupName=if('All'='" + cmbGroupWise.getSelectedItem().toString().trim() + "'"
		    + ",a.strGroupName,'" + cmbGroupWise.getSelectedItem().toString().trim() + "') and a.intBalance<=b.dblMinLevel "
		    + "and b.dblMaxLevel-a.intBalance-ifnull(c.dblOrderQty,0.00) > 0 "
		    + "and (b.dblMaxLevel > 0 or b.dblMinLevel>0) "
		    + "group by a.strGroupName,a.strSubgroupName,a.strItemCode "
		    + "order by strItemName ";

	    /*
             if(balStockSign.equalsIgnoreCase("Positive"))
             {
             if(showZeroBalStk.equals("Yes"))
             {
             sqlStock+=" and c.dblOrderQty >= 0 ";
             }
             else 
             {
             sqlStock+=" and c.dblOrderQty > 0 ";
             }
             }
             else if(balStockSign.equalsIgnoreCase("Negative"))
             {
             if(showZeroBalStk.equals("Yes"))
             {
             sqlStock+=" and c.dblOrderQty <= 0 ";
             }
             else 
             {
             sqlStock+=" and c.dblOrderQty < 0 ";
             }
             }
             sqlStock+= " group by a.strGroupName,a.strSubgroupName,a.strItemCode ";
             sqlStock+= " order by a.strItemName";*/
	    System.out.println(sqlStock);
	    ResultSet stkRs = clsGlobalVarClass.dbMysql.executeResultSet(sqlStock);

	    while (stkRs.next())
	    {
		flgReOrder = true;
		ob[0] = stkRs.getString(1);//Group
		ob[1] = stkRs.getString(2);//Subgroup
		ob[2] = stkRs.getString(3);//Item Name
		ob[3] = stkRs.getString(5);//Op Stock
		ob[4] = stkRs.getString(6);//Stk In
		ob[5] = stkRs.getString(7);//Stk Out
		ob[6] = stkRs.getString(8);//Sale
		ob[7] = stkRs.getString(9);//Balance
		ob[8] = stkRs.getString(14);//Order Qty
		ob[9] = stkRs.getString(10);//Min Level
		ob[10] = stkRs.getString(11);//Max Level
		ob[11] = stkRs.getString(12);//Reorder Qty

		if (Double.parseDouble(ob[11].toString()) > 0)
		{
		    dblOpening = dblOpening + new Double(stkRs.getString(5));
		    dblStkIn = dblStkIn + new Double(stkRs.getString(6));
		    dblStkOut = dblStkOut + new Double(stkRs.getString(7));
		    dblSale = dblSale + new Double(stkRs.getString(8));
		    dblBal = dblBal + new Double(stkRs.getString(9));
		    reorderQty += Double.parseDouble(stkRs.getString(12));
		    openProductionQty += Double.parseDouble(stkRs.getString(14));
		    vItemCode.add(stkRs.getString(13));
		    dm.addRow(ob);
		}
	    }
	    if (flgReOrder)
	    {
		btnProductionOrder.setEnabled(true);
	    }
	    Object[] ob1 =
	    {
		"TOTAL", "", "", Math.rint(dblOpening), Math.rint(dblStkIn), Math.rint(dblStkOut), Math.rint(dblSale), Math.rint(dblBal), Math.rint(openProductionQty), "", "", reorderQty
	    };
	    dm1.addRow(ob1);
	    tblStock.setModel(dm);
	    tblTotal.setModel(dm1);

	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	    tblStock.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
	    tblStock.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
	    tblStock.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
	    tblStock.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
	    tblStock.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);
	    tblStock.getColumnModel().getColumn(8).setCellRenderer(rightRenderer);
	    tblStock.getColumnModel().getColumn(9).setCellRenderer(rightRenderer);
	    tblStock.getColumnModel().getColumn(10).setCellRenderer(rightRenderer);
	    tblStock.getColumnModel().getColumn(11).setCellRenderer(rightRenderer);
	    tblStock.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    tblStock.getColumnModel().getColumn(0).setPreferredWidth(80);//Group
	    tblStock.getColumnModel().getColumn(1).setPreferredWidth(80);//Subgroup
	    tblStock.getColumnModel().getColumn(2).setPreferredWidth(90);//ItemName
	    tblStock.getColumnModel().getColumn(3).setPreferredWidth(60);//Op Stk
	    tblStock.getColumnModel().getColumn(4).setPreferredWidth(60);//Stk In
	    tblStock.getColumnModel().getColumn(5).setPreferredWidth(60);//Stk Out
	    tblStock.getColumnModel().getColumn(6).setPreferredWidth(60);//Sale
	    tblStock.getColumnModel().getColumn(7).setPreferredWidth(60);//Balance
	    tblStock.getColumnModel().getColumn(8).setPreferredWidth(60);//Order Qty
	    tblStock.getColumnModel().getColumn(9).setPreferredWidth(60);//Min Level
	    tblStock.getColumnModel().getColumn(10).setPreferredWidth(60);//Max level
	    tblStock.getColumnModel().getColumn(11).setPreferredWidth(60);//Reorder Qty

	    DefaultTableCellRenderer rightRenderer1 = new DefaultTableCellRenderer();
	    rightRenderer1.setHorizontalAlignment(JLabel.RIGHT);
	    tblTotal.getColumnModel().getColumn(3).setCellRenderer(rightRenderer1);
	    tblTotal.getColumnModel().getColumn(4).setCellRenderer(rightRenderer1);
	    tblTotal.getColumnModel().getColumn(5).setCellRenderer(rightRenderer1);
	    tblTotal.getColumnModel().getColumn(6).setCellRenderer(rightRenderer1);
	    tblTotal.getColumnModel().getColumn(7).setCellRenderer(rightRenderer1);
	    tblTotal.getColumnModel().getColumn(8).setCellRenderer(rightRenderer1);
	    tblTotal.getColumnModel().getColumn(9).setCellRenderer(rightRenderer1);
	    tblTotal.getColumnModel().getColumn(10).setCellRenderer(rightRenderer1);
	    tblTotal.getColumnModel().getColumn(11).setCellRenderer(rightRenderer1);
	    tblTotal.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    tblTotal.getColumnModel().getColumn(0).setPreferredWidth(80);//Group
	    tblTotal.getColumnModel().getColumn(1).setPreferredWidth(80);//Subgroup
	    tblTotal.getColumnModel().getColumn(2).setPreferredWidth(90);//ItemName
	    tblTotal.getColumnModel().getColumn(3).setPreferredWidth(60);//Op Stk
	    tblTotal.getColumnModel().getColumn(4).setPreferredWidth(60);//Stk In
	    tblTotal.getColumnModel().getColumn(5).setPreferredWidth(60);//Stk Out
	    tblTotal.getColumnModel().getColumn(6).setPreferredWidth(60);//Sale
	    tblTotal.getColumnModel().getColumn(7).setPreferredWidth(60);//Balance
	    tblTotal.getColumnModel().getColumn(8).setPreferredWidth(60);//Order Qty
	    tblTotal.getColumnModel().getColumn(9).setPreferredWidth(60);//Min Level
	    tblTotal.getColumnModel().getColumn(10).setPreferredWidth(60);//Max level
	    tblTotal.getColumnModel().getColumn(11).setPreferredWidth(60);//Reorder Qty
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    /**
     * Generate Production Code
     *
     * @return
     * @throws Exception
     */
    private long funGenerateProductionCode() throws Exception
    {
	long lastNo = 0;

	String sql_Internal = "select dblLastNo from tblinternal "
		+ "where strTransactionType='Production'";
	ResultSet rsInternal = clsGlobalVarClass.dbMysql.executeResultSet(sql_Internal);
	if (rsInternal.next())
	{
	    lastNo = rsInternal.getLong(1);
	    lastNo++;
	}
	String sql_InternalUpdate = "update tblinternal set dblLastNo=" + lastNo + " "
		+ "where strTransactionType='Production'";
	clsGlobalVarClass.dbMysql.execute(sql_InternalUpdate);

	return lastNo;
    }

    /**
     * Generate Production Entry
     *
     * @return String
     */
    private String funGenerateProductionEntry()
    {
	String productionCode = "";
	try
	{
	    if (tblStock.getRowCount() > 0)
	    {
		long lastNo = funGenerateProductionCode();
		productionCode = "PD" + String.format("%07d", lastNo);
		String remarks = "Production Entry";
		String sql_ProductionHd = "insert into tblproductionhd "
			+ "(strProductionCode,strPOSCode"
			+ ",dteProductionDate,strClose,strRemarks,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited"
			+ ",strClientCode,strDataPostFlag) values "
			+ "('" + productionCode + "','" + clsGlobalVarClass.gPOSCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
			+ ",'N','" + remarks + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
			+ ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "','N')";

		String sql_ProductionDtl = "insert into tblproductiondtl "
			+ "(strProductionCode,strItemCode,strItemName,dblStock,dblOrderQty"
			+ ",strClientCode,strDataPostFlag) values ";
		String sql_DtlData = "";
		for (int cnt = 0; cnt < tblStock.getRowCount(); cnt++)
		{
		    sql_DtlData += "('" + productionCode + "','" + vItemCode.elementAt(cnt).toString() + "'"
			    + ",'" + tblStock.getValueAt(cnt, 2).toString() + "'," + tblStock.getValueAt(cnt, 7).toString() + ""
			    + "," + tblStock.getValueAt(cnt, 11).toString() + ",'" + clsGlobalVarClass.gClientCode + "','N'),";
		}
		StringBuilder sb = new StringBuilder(sql_DtlData);
		sql_DtlData = sb.delete(sb.lastIndexOf(","), sb.length()).toString();
		sql_ProductionDtl += sql_DtlData;
		clsGlobalVarClass.dbMysql.execute(sql_ProductionHd);
		clsGlobalVarClass.dbMysql.execute(sql_ProductionDtl);
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    return productionCode;
	}
    }

    /**
     * Print Production Entry
     */
    private void funPrintProductionEntry(String productionEntry)
    {
	try
	{
	    String balStockSign = cmbBalAmtSign.getSelectedItem().toString();
	    String showZeroBalStk = cmbShowZeroBalStk.getSelectedItem().toString();
	    int count = 0;
	    fun_CreateTempFolder();
	    String filePath = System.getProperty("user.dir");
	    File file = new File(filePath + "/Temp/Temp_ReOrderReport.txt");
	    PrintWriter pw = new PrintWriter(file);
	    funPrintBlankLines(clsGlobalVarClass.gClientName, pw);
	    funPrintBlankLines(clsGlobalVarClass.gClientAddress1, pw);
	    if (clsGlobalVarClass.gClientAddress2.trim().length() > 0)
	    {
		funPrintBlankLines(clsGlobalVarClass.gClientAddress2, pw);
	    }
	    if (clsGlobalVarClass.gClientAddress3.trim().length() > 0)
	    {
		funPrintBlankLines(clsGlobalVarClass.gClientAddress3, pw);
	    }
	    funPrintBlankLines("Re-Order Report", pw);

	    pw.println();
	    pw.println("Production Code  :" + productionEntry);
	    pw.println();
	    pw.println("POS  :" + cmbPosCode.getSelectedItem().toString());
	    //pw.println("From :"+funGetCalenderDate("From")+"  To :"+funGetCalenderDate("To"));
	    pw.println("POS  :" + cmbPosCode.getSelectedItem().toString());
	    pw.println("-------------------------------------------------------");
	    pw.println("Group");
	    funDrawUnderLine("Group", pw);
	    pw.println(String.format("%-30s %10s %10s", "Item Name", "Stock", "OrderQty"));//"Item Name                Stock  OrderQty");
	    pw.println("-------------------------------------------------------");

	    int cnt = 0;
	    String groupName = "";
	    double stockQty = 0.00, reOrderQty = 0.00;
	    String fromDate = (dteFromDate.getDate().getYear() + 1900) + "-" + (dteFromDate.getDate().getMonth() + 1) + "-" + (dteFromDate.getDate().getDate());
	    String toDate = (dteToDate.getDate().getYear() + 1900) + "-" + (dteToDate.getDate().getMonth() + 1) + "-" + (dteToDate.getDate().getDate());
	    String sql = "select a.strItemCode,b.strItemName,a.dblStock,a.dblOrderQty,d.strGroupName "
		    + " from tblproductiondtl a left outer join tblitemmaster b on a.strItemCode=b.strItemCode "
		    + " left outer join tblsubgrouphd c on b.strSubGroupCode=c.strSubGroupCode "
		    + " left outer join tblgrouphd d on c.strGroupCode=d.strGroupCode "
		    + " where a.strProductionCode='" + productionEntry + "' ";
	    if (balStockSign.equalsIgnoreCase("Positive"))
	    {
		sql += " and a.dblStock >= 0 ";
		if (showZeroBalStk.equals("Yes"))
		{
		    sql += " and a.dblOrderQty >= 0 ";
		}
		else
		{
		    sql += " and a.dblOrderQty > 0 ";
		}
	    }
	    else if (balStockSign.equalsIgnoreCase("Negative"))
	    {
		sql += " and a.dblStock <= 0 ";
		if (showZeroBalStk.equals("Yes"))
		{
		    sql += " and a.dblOrderQty <= 0 ";
		}
		else
		{
		    sql += " and a.dblOrderQty < 0 ";
		}
	    }
	    else
	    {
		if (showZeroBalStk.equals("No"))
		{
		    sql += " and a.dblOrderQty > 0 ";
		}
	    }
	    sql += "group by a.strItemCode,d.strGroupCode";
	    System.out.println(sql);

	    ResultSet rsProductionEntry = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsProductionEntry.next())
	    {
		count++;
		if (!groupName.equals(rsProductionEntry.getString(5)))
		{
		    /*if(cnt>0)
                     {
                     pw.println("----------------------------------------");
                     pw.print(groupName+"REPEATED");
                     pw.println();
                     pw.println("----------------------------------------");
                     pw.println();
                     }*/
		    pw.println();
		    pw.println(rsProductionEntry.getString(5));
		    funDrawUnderLine(rsProductionEntry.getString(5), pw);
		    groupName = rsProductionEntry.getString(5);
		}
		//pw.print(rsProductionEntry.getString(2));
//                funPrintTextWithAlignment("left",rsProductionEntry.getString(2),20, pw);
//                funPrintTextWithAlignment("right",rsProductionEntry.getString(3),10, pw);
//                funPrintTextWithAlignment("right",rsProductionEntry.getString(4),10, pw);
//                pw.println();

		pw.println(String.format("%-30s %10.2f %10.2f", rsProductionEntry.getString(2), Double.parseDouble(rsProductionEntry.getString(3)), Double.parseDouble(rsProductionEntry.getString(4))));
		stockQty += Double.parseDouble(rsProductionEntry.getString(3));
		reOrderQty += Double.parseDouble(rsProductionEntry.getString(4));
		cnt++;

		/*if(rsProductionEntry.isLast())
                 {
                 pw.println("----------------------------------------");
                 pw.print(groupName);
                 pw.println();
                 }*/
	    }
	    rsProductionEntry.close();

	    /*
             pw.println("----------------------------------------");
             funPrintTextWithAlignment("left","TOTAL",20, pw);
             funPrintTextWithAlignment("right",String.valueOf(Math.rint(stockQty)),10, pw);
             funPrintTextWithAlignment("right",String.valueOf(Math.rint(reOrderQty)),10, pw);*/
	    pw.println();
	    pw.println("-------------------------------------------------------");

	    pw.println();
	    pw.println();
	    if ("linux".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
	    {
		pw.println("V");//Linux
	    }
	    else if ("windows".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
	    {
		if ("Inbuild".equalsIgnoreCase(clsPosConfigFile.gPrinterType))
		{
		    pw.println("V");
		}
		else
		{
		    pw.println("m");//windows
		}
	    }
	    pw.flush();
	    pw.close();

	    if (count > 0)
	    {
		fun_ShowTextFile(file, "Production Order Report");
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    /**
     * Show Text File
     *
     * @param file
     * @param reportName
     * @throws Exception
     */
    private void fun_ShowTextFile(File file, String reportName) throws Exception
    {

	String data = "";
	FileReader fread = new FileReader(file);
	BufferedReader KOTIn = new BufferedReader(fread);
	String line = "";
	while ((line = KOTIn.readLine()) != null)
	{
	    data = data + line + "\n";
	}
	new frmShowTextFile(data, reportName, file, "").setVisible(true);
	fread.close();
    }

    /**
     * Create temp folder
     */
    private void fun_CreateTempFolder()
    {
	String filePath = System.getProperty("user.dir");
	File Text_KOT = new File(filePath + "/Temp");
	if (!Text_KOT.exists())
	{
	    Text_KOT.mkdirs();
	}
    }

    /**
     * Print Blank Lines
     *
     * @param textToPrint
     * @param pw
     * @return
     */
    private int funPrintBlankLines(String textToPrint, PrintWriter pw)
    {
	pw.println();
	int len = 40 - textToPrint.length();
	len = len / 2;
	for (int cnt = 0; cnt < len; cnt++)
	{
	    pw.print(" ");
	}
	pw.print(textToPrint);
	return len;
    }

    /**
     * Print Text With Alignment
     *
     * @param align
     * @param textToPrint
     * @param totalLength
     * @param pw
     * @return
     */
    private int funPrintTextWithAlignment(String align, String textToPrint, int totalLength, PrintWriter pw)
    {
	if (align.equalsIgnoreCase("right"))
	{
	    DecimalFormat decFormat = new DecimalFormat("######.00");
	    textToPrint = decFormat.format(Double.parseDouble(textToPrint));
	    int len = totalLength - textToPrint.length();
	    for (int cnt = 0; cnt < len; cnt++)
	    {
		pw.print(" ");
	    }
	    pw.print(textToPrint);
	}
	else if (align.equalsIgnoreCase("left"))
	{
	    pw.print(textToPrint);
	    int len = totalLength - textToPrint.length();
	    for (int cnt = 0; cnt < len; cnt++)
	    {
		pw.print(" ");
	    }
	}
	return 1;
    }

    /**
     * Get Date
     *
     * @param format
     * @param date
     * @return
     */
    private String funGetDate(String format, String date)
    {
	String retDate = "";
	if (format.equalsIgnoreCase("dd-MM-yyyy"))
	{
	    String[] split = date.split("-");
	    retDate = split[2] + "-" + split[1] + "-" + split[0];
	}
	return retDate;
    }

    /**
     * Draw Under Line
     *
     * @param textToPrint
     * @param pw
     * @return
     */
    private int funDrawUnderLine(String textToPrint, PrintWriter pw)
    {
	for (int cnt = 0; cnt < textToPrint.length(); cnt++)
	{
	    pw.print("-");
	}
	pw.println();
	return 1;
    }

    /**
     * Export Report
     */
    private void funExportReport()
    {
	try
	{
	    String ExportReportPath = clsPosConfigFile.exportReportPath;
	    exportFormName = "StockFlash";
	    File theDir = new File(ExportReportPath);
	    File file = new File(ExportReportPath + "/" + exportFormName + rDate + ".xls");

	    if (!theDir.exists())
	    {
		theDir.mkdir();
		funExportFile(tblStock, file);
	    }
	    else
	    {
		funExportFile(tblStock, file);
	    }
	}
	catch (Exception ex)
	{
	    ex.printStackTrace();
	}
    }

    /**
     * Export File
     *
     * @param table
     * @param file
     */
    void funExportFile(JTable table, File file)
    {
	try
	{
	    WritableWorkbook workbook1 = Workbook.createWorkbook(file);
	    WritableSheet sheet1 = workbook1.createSheet("First Sheet", 0);
	    TableModel model = table.getModel();
	    sheet1.addCell(new Label(3, 0, "DSS Java Pos Bill Wise Report "));
	    for (int i = 0; i < model.getColumnCount() + 1; i++)
	    {
		Label column = new Label(i, 1, model.getColumnName(i));
		sheet1.addCell(column);
	    }
	    int i = 0, j = 0;
	    int k = 0;
	    for (i = 1; i < model.getRowCount() + 1; i++)
	    {
		for (j = 0; j < model.getColumnCount(); j++)
		{
		    Label row = new Label(j, i + 1, model.getValueAt(k, j).toString());
		    sheet1.addCell(row);
		}
		k++;
	    }
	    addLastOfExportReport(workbook1);
	    workbook1.write();
	    workbook1.close();
	    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + clsPosConfigFile.exportReportPath + "/" + exportFormName + rDate + ".xls");
	    //sendMail();
	}
	catch (FileNotFoundException ex)
	{
	    JOptionPane.showMessageDialog(this, "Please Check Export File Location! ");
	    ex.printStackTrace();
	}
	catch (Exception ex)
	{
	    ex.printStackTrace();
	}
    }

    /**
     * add Last Of Export Report
     *
     * @param workbook1
     */
    private void addLastOfExportReport(WritableWorkbook workbook1)
    {
	try
	{
	    int i = 0, j = 0, LastIndexReport = 1;

	    WritableSheet sheet2 = workbook1.getSheet(0);
	    int r = sheet2.getRows();
	    for (i = r; i < tblTotal.getRowCount() + r; i++)
	    {
		for (j = 0; j < tblTotal.getColumnCount(); j++)
		{
		    Label row = new Label(LastIndexReport + j, i + 1, tblTotal.getValueAt(0, j).toString());
		    sheet2.addCell(row);
		}
	    }
	    WritableSheet sheet3 = workbook1.getSheet(0);
	    r = sheet3.getRows();
	    Formatter fmt = new Formatter();
	    Calendar cal = Calendar.getInstance();
	    fmt.format("%tr", cal);
	    Label row = new Label(1, r + 1, " Created On : " + clsGlobalVarClass.gPOSDateToDisplay + " At : " + fmt + " By : " + clsGlobalVarClass.gUserCode + " ");
	    sheet2.addCell(row);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
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

        pnlhead = new javax.swing.JPanel();
        lblProductName = new javax.swing.JLabel();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 32767));
        lblUserCode = new javax.swing.JLabel();
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        lblPosName = new javax.swing.JLabel();
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        lblDate = new javax.swing.JLabel();
        lblHOSign = new javax.swing.JLabel();
        pnlbackground = new JPanel()
        {
            public void paintComponent(Graphics g)
            {
                Image img = Toolkit.getDefaultToolkit().getImage(
                    getClass().getResource("/com/POSGlobal/images/imgBGJPOS.png"));
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        };  ;
        pnlmain = new javax.swing.JPanel();
        lblToDate = new javax.swing.JLabel();
        pnlstockDetails = new javax.swing.JScrollPane();
        tblStock = new javax.swing.JTable();
        pnlTotalDetails = new javax.swing.JScrollPane();
        tblTotal = new javax.swing.JTable();
        btnBack = new javax.swing.JButton();
        btnStockView = new javax.swing.JButton();
        lblFromDate = new javax.swing.JLabel();
        lblItemType = new javax.swing.JLabel();
        cmbPosCode = new javax.swing.JComboBox();
        dteFromDate = new com.toedter.calendar.JDateChooser();
        dteToDate = new com.toedter.calendar.JDateChooser();
        btnPrint = new javax.swing.JButton();
        cmbItemType = new javax.swing.JComboBox();
        lblPOSName = new javax.swing.JLabel();
        btnExport = new javax.swing.JButton();
        cmbReportType = new javax.swing.JComboBox();
        lblReportType = new javax.swing.JLabel();
        btnProductionOrder = new javax.swing.JButton();
        lblGroupWise = new javax.swing.JLabel();
        cmbGroupWise = new javax.swing.JComboBox();
        lblBalStockSign = new javax.swing.JLabel();
        cmbBalAmtSign = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        cmbShowZeroBalStk = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setMinimumSize(new java.awt.Dimension(800, 600));
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter()
        {
            public void windowClosed(java.awt.event.WindowEvent evt)
            {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt)
            {
                formWindowClosing(evt);
            }
        });

        pnlhead.setBackground(new java.awt.Color(69, 164, 238));
        pnlhead.setLayout(new javax.swing.BoxLayout(pnlhead, javax.swing.BoxLayout.LINE_AXIS));

        lblProductName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblProductName.setForeground(new java.awt.Color(255, 255, 255));
        lblProductName.setText("JPOS -  Stock Flash");
        pnlhead.add(lblProductName);
        pnlhead.add(filler4);

        lblUserCode.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblUserCode.setForeground(new java.awt.Color(255, 255, 255));
        lblUserCode.setMaximumSize(new java.awt.Dimension(71, 30));
        lblUserCode.setMinimumSize(new java.awt.Dimension(71, 30));
        lblUserCode.setPreferredSize(new java.awt.Dimension(71, 30));
        pnlhead.add(lblUserCode);
        pnlhead.add(filler5);

        lblPosName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPosName.setForeground(new java.awt.Color(255, 255, 255));
        lblPosName.setMaximumSize(new java.awt.Dimension(321, 30));
        lblPosName.setMinimumSize(new java.awt.Dimension(321, 30));
        lblPosName.setPreferredSize(new java.awt.Dimension(321, 30));
        pnlhead.add(lblPosName);
        pnlhead.add(filler6);

        lblDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDate.setForeground(new java.awt.Color(255, 255, 255));
        lblDate.setMaximumSize(new java.awt.Dimension(192, 30));
        lblDate.setMinimumSize(new java.awt.Dimension(192, 30));
        lblDate.setPreferredSize(new java.awt.Dimension(192, 30));
        pnlhead.add(lblDate);

        lblHOSign.setMaximumSize(new java.awt.Dimension(34, 30));
        lblHOSign.setMinimumSize(new java.awt.Dimension(34, 30));
        lblHOSign.setPreferredSize(new java.awt.Dimension(34, 30));
        pnlhead.add(lblHOSign);

        getContentPane().add(pnlhead, java.awt.BorderLayout.PAGE_START);

        pnlbackground.setLayout(new java.awt.GridBagLayout());

        pnlmain.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        pnlmain.setMinimumSize(new java.awt.Dimension(800, 570));
        pnlmain.setOpaque(false);

        lblToDate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblToDate.setText("To Date");

        tblStock.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblStock.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String []
            {
                "Item Code", "Item Name", "Opening Stock", "Stock Out", "Stock In", "Sale", "Bal"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblStock.setRowHeight(25);
        pnlstockDetails.setViewportView(tblStock);

        tblTotal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        tblTotal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null, null, null, null, null}
            },
            new String []
            {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5"
            }
        ));
        tblTotal.setRowHeight(25);
        pnlTotalDetails.setViewportView(tblTotal);

        btnBack.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnBack.setForeground(new java.awt.Color(255, 255, 255));
        btnBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn1.png"))); // NOI18N
        btnBack.setText("CLOSE");
        btnBack.setToolTipText("Close Window");
        btnBack.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBack.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn2.png"))); // NOI18N
        btnBack.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBackMouseClicked(evt);
            }
        });
        btnBack.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnBackActionPerformed(evt);
            }
        });

        btnStockView.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnStockView.setForeground(new java.awt.Color(255, 255, 255));
        btnStockView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn1.png"))); // NOI18N
        btnStockView.setText("VIEW");
        btnStockView.setToolTipText("View Report");
        btnStockView.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnStockView.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn2.png"))); // NOI18N
        btnStockView.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnStockViewActionPerformed(evt);
            }
        });

        lblFromDate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblFromDate.setText("From Date");

        lblItemType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblItemType.setText("Type");

        cmbPosCode.setToolTipText("Select POS");
        cmbPosCode.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbPosCodeActionPerformed(evt);
            }
        });

        dteFromDate.setToolTipText("Select From Date ");

        dteToDate.setToolTipText("Select To Date ");

        btnPrint.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPrint.setForeground(new java.awt.Color(255, 255, 255));
        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn1.png"))); // NOI18N
        btnPrint.setText("PRINT");
        btnPrint.setToolTipText("Print Report");
        btnPrint.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrint.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn2.png"))); // NOI18N
        btnPrint.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnPrintMouseClicked(evt);
            }
        });
        btnPrint.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPrintActionPerformed(evt);
            }
        });

        cmbItemType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Raw Material", "Menu Item", "Both" }));
        cmbItemType.setToolTipText("Select Type");

        lblPOSName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPOSName.setText("POS Name");

        btnExport.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnExport.setForeground(new java.awt.Color(255, 255, 255));
        btnExport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn1.png"))); // NOI18N
        btnExport.setText("Export");
        btnExport.setToolTipText("Export File");
        btnExport.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExport.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn2.png"))); // NOI18N
        btnExport.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnExportMouseClicked(evt);
            }
        });
        btnExport.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnExportActionPerformed(evt);
            }
        });

        cmbReportType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Stock", "ReOrder" }));
        cmbReportType.setToolTipText("Select Report Type");
        cmbReportType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbReportTypeActionPerformed(evt);
            }
        });

        lblReportType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblReportType.setText("Report Type");

        btnProductionOrder.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnProductionOrder.setForeground(new java.awt.Color(255, 255, 255));
        btnProductionOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn1.png"))); // NOI18N
        btnProductionOrder.setText("Production");
        btnProductionOrder.setToolTipText("Production");
        btnProductionOrder.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnProductionOrder.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn2.png"))); // NOI18N
        btnProductionOrder.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnProductionOrderMouseClicked(evt);
            }
        });
        btnProductionOrder.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnProductionOrderActionPerformed(evt);
            }
        });

        lblGroupWise.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblGroupWise.setText("Group Wise");

        cmbGroupWise.setModel(new javax.swing.DefaultComboBoxModel(new String[] {  }));
        cmbGroupWise.setPreferredSize(new java.awt.Dimension(66, 20));
        cmbGroupWise.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbGroupWiseActionPerformed(evt);
            }
        });

        lblBalStockSign.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblBalStockSign.setText("Show Stock With");

        cmbBalAmtSign.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Positive", "Negative", "Both" }));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Show 0 Bal Stock");

        cmbShowZeroBalStk.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Yes", "No" }));

        javax.swing.GroupLayout pnlmainLayout = new javax.swing.GroupLayout(pnlmain);
        pnlmain.setLayout(pnlmainLayout);
        pnlmainLayout.setHorizontalGroup(
            pnlmainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlmainLayout.createSequentialGroup()
                .addGap(208, 208, 208)
                .addComponent(lblGroupWise)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbGroupWise, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblBalStockSign)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbBalAmtSign, 0, 124, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbShowZeroBalStk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlmainLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlmainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlmainLayout.createSequentialGroup()
                        .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(76, 76, 76)
                        .addComponent(btnExport, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(75, 75, 75)
                        .addComponent(btnProductionOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(60, 60, 60)
                        .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlmainLayout.createSequentialGroup()
                        .addComponent(lblItemType, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(118, 118, 118))))
            .addGroup(pnlmainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlmainLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addGroup(pnlmainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlmainLayout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(lblPOSName)
                            .addGap(2, 2, 2)
                            .addComponent(cmbPosCode, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(10, 10, 10)
                            .addComponent(lblFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, 0)
                            .addComponent(dteFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(10, 10, 10)
                            .addComponent(lblToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, 0)
                            .addComponent(dteToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(100, 100, 100)
                            .addComponent(cmbItemType, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(pnlmainLayout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(lblReportType, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, 0)
                            .addComponent(cmbReportType, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(pnlstockDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 810, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(pnlTotalDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 810, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(pnlmainLayout.createSequentialGroup()
                            .addGap(20, 20, 20)
                            .addComponent(btnStockView, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        pnlmainLayout.setVerticalGroup(
            pnlmainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlmainLayout.createSequentialGroup()
                .addGroup(pnlmainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlmainLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblItemType, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlmainLayout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addGroup(pnlmainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblBalStockSign, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblGroupWise, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbGroupWise, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbBalAmtSign, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlmainLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(cmbShowZeroBalStk, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 428, Short.MAX_VALUE)
                .addGroup(pnlmainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnProductionOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExport, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(pnlmainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlmainLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addGroup(pnlmainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblPOSName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbPosCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(dteFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(dteToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbItemType, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(10, 10, 10)
                    .addGroup(pnlmainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblReportType, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbReportType, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(10, 10, 10)
                    .addComponent(pnlstockDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, 0)
                    .addComponent(pnlTotalDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(8, 8, 8)
                    .addComponent(btnStockView, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pnlbackground.add(pnlmain, new java.awt.GridBagConstraints());

        getContentPane().add(pnlbackground, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
/**
     * Close Window
     *
     * @param evt
     */
    private void btnBackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBackMouseClicked
	// TODO add your handling code here:
	dispose();
    }//GEN-LAST:event_btnBackMouseClicked

    private void cmbPosCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbPosCodeActionPerformed

    }//GEN-LAST:event_cmbPosCodeActionPerformed
    /**
     * Print bill wise Reports
     *
     * @param evt
     */
    public void funbtnPrintPressed()
    {
	try
	{
	    String fromDate = dteFromDate.getDate().toString(), toDate = dteToDate.getDate().toString();

	    System.out.println("fromDate=" + fromDate + "   toDate=" + toDate);
	    com.mysql.jdbc.Connection con = null;
//            try
//            {
//
//                Class.forName("com.mysql.jdbc.Driver");
//                con = (com.mysql.jdbc.Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/jpos", "root", "root");
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }

	    String psCode = cmbPosCode.getSelectedItem().toString();
	    StringBuilder sb = new StringBuilder(psCode);
	    int ind = sb.lastIndexOf(" ");
	    psCode = sb.substring(ind + 1, psCode.length());
	    if (psCode.equals("All"))
	    {
		reportName = "Report/stockFlashPosAllReport.jasper";
	    }
	    else
	    {
		reportName = "Report/billWiseReport.jasper";
	    }
	    InputStream is = this.getClass().getClassLoader().getResourceAsStream(reportName);
	    HashMap hm = new HashMap();
	    hm.put("posCode", psCode);
	    hm.put("fromDate", fromDate);
	    hm.put("toDate", toDate);
	    hm.put("userName", clsGlobalVarClass.gUserName);
	    hm.put("posName", clsGlobalVarClass.gPOSName);
	    JasperPrint print = JasperFillManager.fillReport(is, hm, clsGlobalVarClass.conJasper);
	    JRViewer viewer = new JRViewer(print);
	    JFrame jf = new JFrame();
	    jf.getContentPane().add(viewer);
	    jf.validate();
	    jf.setVisible(true);
	    jf.setSize(new Dimension(850, 750));
	    jf.setLocation(300, 10);

	}
	catch (Exception ex)
	{
	    ex.printStackTrace();
	}
    }
    private void btnPrintMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPrintMouseClicked

	funbtnPrintPressed();
    }//GEN-LAST:event_btnPrintMouseClicked

    private void btnExportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExportMouseClicked
	// TODO add your handling code here:
	funExportReport();
    }//GEN-LAST:event_btnExportMouseClicked

    private void cmbReportTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbReportTypeActionPerformed
	// TODO add your handling code here:

    }//GEN-LAST:event_cmbReportTypeActionPerformed
    /**
     * View Report
     *
     * @param evt
     */
    private void btnProductionOrderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProductionOrderMouseClicked
	// TODO add your handling code here:
	String productionEntry = funGenerateProductionEntry();
	funPrintProductionEntry(productionEntry);
	funShowReOrderFlash();
    }//GEN-LAST:event_btnProductionOrderMouseClicked

    private void cmbGroupWiseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbGroupWiseActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_cmbGroupWiseActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
	funResetLookAndFeel();
	dispose();
	clsGlobalVarClass.hmActiveForms.remove("Stock Flash Report");

    }//GEN-LAST:event_btnBackActionPerformed

    private void btnProductionOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductionOrderActionPerformed
	// TODO add your handling code here:
	//String productionEntry = funGenerateProductionEntry();
	//funPrintProductionEntry(productionEntry);
	//funShowReOrderFlash();
    }//GEN-LAST:event_btnProductionOrderActionPerformed

    private void btnExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportActionPerformed
	// TODO add your handling code here:
	funExportReport();
    }//GEN-LAST:event_btnExportActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
	// TODO add your handling code here:
	funbtnPrintPressed();
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnStockViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStockViewActionPerformed
	// TODO add your handling code here:
	if (cmbReportType.getSelectedItem().toString().equals("Stock"))
	{
	    funShowStockFlash();
	}
	else
	{
	    funShowReOrderFlash();
	}
    }//GEN-LAST:event_btnStockViewActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
	// TODO add your handling code here:
	clsGlobalVarClass.hmActiveForms.remove("Stock Flash Report");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
	// TODO add your handling code here:
	clsGlobalVarClass.hmActiveForms.remove("Stock Flash Report");
    }//GEN-LAST:event_formWindowClosing

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
	    java.util.logging.Logger.getLogger(frmStockFlash.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (InstantiationException ex)
	{
	    java.util.logging.Logger.getLogger(frmStockFlash.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (IllegalAccessException ex)
	{
	    java.util.logging.Logger.getLogger(frmStockFlash.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (javax.swing.UnsupportedLookAndFeelException ex)
	{
	    java.util.logging.Logger.getLogger(frmStockFlash.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	//</editor-fold>
	//</editor-fold>

	/* Create and display the form */
	java.awt.EventQueue.invokeLater(new Runnable()
	{
	    public void run()
	    {
		new frmStockFlash().setVisible(true);
	    }
	});
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnExport;
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnProductionOrder;
    private javax.swing.JButton btnStockView;
    private javax.swing.JComboBox cmbBalAmtSign;
    private javax.swing.JComboBox cmbGroupWise;
    private javax.swing.JComboBox cmbItemType;
    private javax.swing.JComboBox cmbPosCode;
    private javax.swing.JComboBox cmbReportType;
    private javax.swing.JComboBox cmbShowZeroBalStk;
    private com.toedter.calendar.JDateChooser dteFromDate;
    private com.toedter.calendar.JDateChooser dteToDate;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblBalStockSign;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFromDate;
    private javax.swing.JLabel lblGroupWise;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblItemType;
    private javax.swing.JLabel lblPOSName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblReportType;
    private javax.swing.JLabel lblToDate;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JScrollPane pnlTotalDetails;
    private javax.swing.JPanel pnlbackground;
    private javax.swing.JPanel pnlhead;
    private javax.swing.JPanel pnlmain;
    private javax.swing.JScrollPane pnlstockDetails;
    private javax.swing.JTable tblStock;
    private javax.swing.JTable tblTotal;
    // End of variables declaration//GEN-END:variables

    private void funSetLookAndFeel()
    {
	try
	{
	    // Set System L&F
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    SwingUtilities.updateComponentTreeUI(this);
	}
	catch (UnsupportedLookAndFeelException e)
	{
	    // handle exception
	}
	catch (ClassNotFoundException e)
	{
	    // handle exception
	}
	catch (InstantiationException e)
	{
	    // handle exception
	}
	catch (IllegalAccessException e)
	{
	    // handle exception
	}
    }

    private void funResetLookAndFeel()
    {
	try
	{
	    for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
	    {
		System.out.println("lookandfeel" + info.getName());
		if ("Nimbus".equals(info.getName()))
		{
		    javax.swing.UIManager.setLookAndFeel(info.getClassName());
		    SwingUtilities.updateComponentTreeUI(this);
		    break;
		}
	    }
	}
	catch (ClassNotFoundException ex)
	{

	}
	catch (InstantiationException ex)
	{

	}
	catch (IllegalAccessException ex)
	{

	}
	catch (javax.swing.UnsupportedLookAndFeelException ex)
	{

	}
    }
}
