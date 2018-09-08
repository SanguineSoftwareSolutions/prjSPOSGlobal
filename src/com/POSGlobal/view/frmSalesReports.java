/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSGlobal.view;

import com.POSGlobal.controller.clsBillItemDtl;
import com.POSGlobal.controller.clsBillSettlementDtl;
import com.POSGlobal.controller.clsCommonBeanDtl;
import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsGroupSubGroupWiseSales;
import com.POSGlobal.controller.clsItemWiseConsumption;
import com.POSGlobal.controller.clsOperatorDtl;
import com.POSGlobal.controller.clsPosConfigFile;
import com.POSGlobal.controller.clsSalesFlashColumns;
import com.POSGlobal.controller.clsSalesFlashReport;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.controller.clsUtility2;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class frmSalesReports extends javax.swing.JFrame
{

    private String fromDate, toDate, reportName;
    private Object[] records;
    private DefaultTableModel dm, totalDm;
    private BigDecimal totalAmount, temp, temp1, Disc;
    private Double totalQty;
    private Double subTotal = 0.00;
    private Double discountTotal = 0.00;
    private String exportFormName, rDate, ExportReportPath, sql;
    private int navigate;
    private java.util.Vector vSettlementMode;
    private java.util.Vector vSalesReportExcelColLength;
//    private DecimalFormat decimalFormat;
    private double salePer;
    private clsUtility objUtility;
    private clsUtility2 objUtility2;
    private Map<String, List<Map<String, clsGroupSubGroupWiseSales>>> mapPOSDtlForGroupSubGroup;
    private Map<String, List<Map<String, clsBillSettlementDtl>>> mapPOSDtlForSettlement;
    private Map<String, Map<String, clsBillItemDtl>> mapPOSItemDtl;
    private Map<String, Map<String, clsBillItemDtl>> mapPOSMenuHeadDtl;
    private Map<String, Map<String, clsCommonBeanDtl>> mapPOSWaiterWiseSales;
    private Map<String, Map<String, clsCommonBeanDtl>> mapPOSDeliveryBoyWise;
    private Map<String, Map<String, clsCommonBeanDtl>> mapPOSCostCenterWiseSales;
    private Map<String, Map<String, clsCommonBeanDtl>> mapPOSTableWiseSales;
    private Map<String, Map<String, clsCommonBeanDtl>> mapPOSHourlyWiseSales;
    private Map<String, Map<String, clsCommonBeanDtl>> mapPOSAreaWiseSales;
    private Map<String, clsCommonBeanDtl> mapPOSDayWiseSales;
    private Map<String, Map<String, clsCommonBeanDtl>> mapPOSModifierWiseSales;
    //private Map<String, Map<String, Map<String, clsCommonBeanDtl>>> mapPOSOperaterWiseSales;
    private Map<String, Map<String, clsCommonBeanDtl>> mapPOSMonthWiseSales;
    private Map<String, List<clsOperatorDtl>> mapOperatorDtls;
    private String selectedPOSCodeFilter = clsGlobalVarClass.gPOSCode;
    private Set selectedPOSCodeSet;
    private Map<String, String> mapAreaNameCode;
    private DecimalFormat gDecimalFormat;

    public frmSalesReports()
    {
	initComponents();
	ButtonGroup bg1 = new ButtonGroup();
	try
	{
	    Timer timer = new Timer(500, new ActionListener()
	    {
		@Override
		public void actionPerformed(ActionEvent e)
		{
		    Date date1 = new Date();
		    String new_str = String.format("%tr", date1);
		    String dateAndTime = clsGlobalVarClass.gPOSDateToDisplay + " " + new_str;
		    lblDate.setText(dateAndTime);
		}
	    });
	    timer.setRepeats(true);
	    timer.setCoalesce(true);
	    timer.setInitialDelay(0);
	    timer.start();

//            decimalFormat = new DecimalFormat("#.##");
	    navigate = 0;
	    btnPrevious.setEnabled(false);
	    btnExport.setVisible(true);
	    rDate = clsGlobalVarClass.gPOSDateToDisplay;
	    clsPosConfigFile pc = new clsPosConfigFile();
	    ExportReportPath = clsPosConfigFile.exportReportPath;
	    lblUserCode.setText(clsGlobalVarClass.gUserCode);
	    lblPosName.setText(clsGlobalVarClass.gPOSName);
	    lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
	    lblModuleName.setText(clsGlobalVarClass.gSelectedModule);

	    objUtility = new clsUtility();
	    objUtility2 = new clsUtility2();

	    selectedPOSCodeSet = new HashSet();

	    gDecimalFormat = clsGlobalVarClass.funGetGlobalDecimalFormatter();

	    dm = new DefaultTableModel();
	    dm.addColumn("Bill No");
	    dm.addColumn("Date");
	    dm.addColumn("Operator");
	    dm.addColumn("Settlement Mode");
	    dm.addColumn("Discount");
	    dm.addColumn("Tax");
	    dm.addColumn("Grand Total");

	    if (clsGlobalVarClass.gUSDConvertionRate == 0)
	    {
		lblCurrency.setVisible(false);
		cmbCurrency.setVisible(false);
	    }
	    else
	    {
		lblCurrency.setVisible(true);
		cmbCurrency.setVisible(true);
	    }

	    funFillComboBox();
	    funFillComboBoxPayMode();
	    funSetFormToInDateChosser();
	    funBillWise();

	    fillShiftCombo();

	    funSetShortCutKeys();

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

    /**
     * Filling Form ComboBox for POSCode ,Operator
     *
     * @throws Exception
     */
    private void funSetShortCutKeys()
    {
	btnBack.setMnemonic('c');
	btnExport.setMnemonic('e');

    }

    private void funFillComboBox() throws Exception
    {
	ResultSet rs = null;

	if (clsGlobalVarClass.gShowOnlyLoginPOSReports)
	{
	    cmbPosCode.addItem(clsGlobalVarClass.gPOSName + " " + clsGlobalVarClass.gPOSCode);
	}
	else
	{
	    cmbPosCode.addItem("All");
	    cmbPosCode.addItem("Multiple");
	    String sql = "select strPosName,strPosCode from tblposmaster";
	    rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rs.next())
	    {
		cmbPosCode.addItem(rs.getString(1) + " " + rs.getString(2));
	    }
	    rs.close();
	}

	cmbOperator.addItem("All");
	sql = "Select strUserCode,strUserName from tbluserhd";
	rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	while (rs.next())
	{
	    cmbOperator.addItem(rs.getString("strUserCode"));
	}
	rs.close();

	mapAreaNameCode = new HashMap<String, String>();
	cmbArea.addItem("All");
	sql = "Select strAreaCode,strAreaName from tblareamaster";
	rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	while (rs.next())
	{
//            if(rs.getString("strAreaName").equalsIgnoreCase("All"))
//            {
//                cmbArea.addItem("All");
//                mapAreaNameCode.put("All", rs.getString("strAreaCode"));
//            }
//            else{
	    cmbArea.addItem(rs.getString("strAreaName"));
	    mapAreaNameCode.put(rs.getString("strAreaName"), rs.getString("strAreaCode"));
	    //     }

	}
	rs.close();

    }

    /**
     * pay Mode ComboBox
     *
     * @throws Exception
     */
    private void funFillComboBoxPayMode() throws Exception
    {
	vSettlementMode = new java.util.Vector();
	cmbPayMode.addItem("All");
	vSettlementMode.add("All");

	cmbPayMode.addItem("MultiSettle");
	vSettlementMode.add("MultiSettle");

	String sql = "select strSettelmentDesc,strSettelmentCode from tblsettelmenthd";
	ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	while (rs.next())
	{
	    cmbPayMode.addItem(rs.getString(1));
	    vSettlementMode.add(rs.getString(2));
	}
	rs.close();
    }

    /**
     * get Selected Pos Code
     *
     * @return
     * @throws Exception
     */
    private String funGetSelectedPosCode() throws Exception
    {
	String pos = null;

	String posCode = cmbPosCode.getSelectedItem().toString();
	if (posCode.equalsIgnoreCase("Multiple"))
	{
	    pos = posCode;
	}
	else
	{
	    StringBuilder sb = new StringBuilder(posCode);
	    int len = posCode.length();
	    int lastInd = sb.lastIndexOf(" ");
	    pos = sb.substring(lastInd + 1, len).toString();
	}

	return pos;
    }

    /**
     * set Form To in data Chosser
     *
     * @throws Exception
     */
    private void funSetFormToInDateChosser() throws Exception
    {
	java.util.Date date = new SimpleDateFormat("dd-MM-yyyy").parse(clsGlobalVarClass.gPOSDateToDisplay);
	dteFromDate.setDate(date);
	dteToDate.setDate(date);
    }

    /**
     * fun get From Date
     *
     * @return
     * @throws Exception
     */
    private String funGetFromDate() throws Exception
    {
	String fromDate = null;
	java.util.Date dt1 = new java.util.Date();
	dt1 = dteFromDate.getDate();
	int d = dt1.getDate();
	int m = dt1.getMonth() + 1;
	int y = dt1.getYear() + 1900;
	fromDate = y + "-" + m + "-" + d;
	return fromDate;
    }

    /**
     * Get To Date
     *
     * @return
     * @throws Exception
     */
    private String funGetToDate() throws Exception
    {
	String toDate = null;
	Date dt2 = dteToDate.getDate();
	int d = dt2.getDate();
	int m = dt2.getMonth() + 1;
	int y = dt2.getYear() + 1900;
	toDate = y + "-" + m + "-" + d;
	return toDate;
    }

    /**
     * Get From Time
     *
     *
     * @return
     * @throws Exception
     */
    private String funGetFromTime() throws Exception
    {
	String fromTime = null;
	if (!cmbhour.getSelectedItem().equals("HH") && !cmbMinute.getSelectedItem().equals("MM"))
	{
	    SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
	    SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
	    String Hour = cmbhour.getSelectedItem().toString();
	    String Minute = cmbMinute.getSelectedItem().toString();
	    String Ampm = cmbampmFrom.getSelectedItem().toString();
	    String Time = Hour + ":" + Minute + " " + Ampm;
	    Date date = parseFormat.parse(Time);
	    fromTime = displayFormat.format(date);
	}
	return fromTime;
    }

    /**
     * Get To Time
     *
     * @return
     * @throws Exception
     */
    private String funGetToTime() throws Exception
    {
	String ToTime = null;
	if (!cmbhour1.getSelectedItem().equals("HH") && !cmbMinute1.getSelectedItem().equals("MM"))
	{
	    SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
	    SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
	    String Hour = cmbhour1.getSelectedItem().toString();
	    String Minute = cmbMinute1.getSelectedItem().toString();
	    String Ampm = cmbampmTo.getSelectedItem().toString();
	    String Time = Hour + ":" + Minute + " " + Ampm;
	    Date date = parseFormat.parse(Time);
	    ToTime = displayFormat.format(date);
	}
	return ToTime;
    }

    /**
     * Day Wise Sales
     */
    private void funDayWiseSales()
    {
	StringBuilder sbSql = new StringBuilder();
	StringBuilder sbSqlForDiscount = new StringBuilder();
	try
	{
	    reportName = "Day Wise Sales Report";
	    double totalDiscount = 0, totalSubTotal = 0, totalAmount = 0, totalTaxAmt = 0;
	    int totalNoOfBills = 0;

	    temp = new BigDecimal("0.00");
	    temp1 = new BigDecimal("0.00");
	    dm = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    return false;
		}
	    };
	    dm.addColumn("Bill Date");
	    dm.addColumn("No Of Bills");
	    dm.addColumn("Sub Total");
	    dm.addColumn("Discount");
	    dm.addColumn("Tax Amount");
	    dm.addColumn("Grand Amount");

	    vSalesReportExcelColLength = new java.util.Vector();
	    vSalesReportExcelColLength.add("6#Left"); //date
	    vSalesReportExcelColLength.add("15#Left"); //no of bills
	    vSalesReportExcelColLength.add("6#Right"); //subtotal
	    vSalesReportExcelColLength.add("6#Right"); //disc
	    vSalesReportExcelColLength.add("6#Right"); //tax
	    vSalesReportExcelColLength.add("6#Right"); //salestotal

	    DefaultTableModel dm1 = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    //all cells false
		    return false;
		}
	    };
	    dm1.addColumn("");
	    dm1.addColumn("TotalBills");
	    dm1.addColumn("Total Sub Total");
	    dm1.addColumn("Total Discount");
	    dm1.addColumn("Total Tax");
	    dm1.addColumn("Total Amount");
	    Date dt1 = dteFromDate.getDate();
	    Date dt2 = dteToDate.getDate();
	    if ((dt2.getTime() - dt1.getTime()) < 0)
	    {
		new frmOkPopUp(null, "Invalid date", "Error", 1).setVisible(true);
	    }
	    else
	    {
		fromDate = funGetFromDate();
		toDate = funGetToDate();
		String posCode = funGetSelectedPosCode();
		records = new Object[6];
		boolean flgRecords = false;
		DecimalFormat decFormat = new DecimalFormat("0");

		if (!flgRecords)
		{
		    dm.setRowCount(0);
		    dm1.setRowCount(0);
		}
		sbSql.setLength(0);
		sbSql.append("select  DATE_FORMAT(date(a.dteBillDate),'%d-%m-%Y'),count(a.strBillNo),sum(a.dblSubTotal)"
			+ ",sum(a.dblDiscountAmt),sum(a.dblTaxAmt),'" + clsGlobalVarClass.gUserCode + "',date(a.dteBillDate) "
			+ " from tblbillhd a "
			+ " where date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' ");
		if (!posCode.equals("All"))
		{
		    sbSql.append(" and " + objUtility.funGetSelectedPOSCodeString("a.strPOSCode", selectedPOSCodeSet) + " ");
		}
		if (clsGlobalVarClass.gEnableShiftYN && (!cmbShift.getSelectedItem().toString().equalsIgnoreCase("All")))
		{
		    sbSql.append(" AND a.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ");
		}
		if (!cmbArea.getSelectedItem().equals("All"))
		{
		    sbSql.append(" and a.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ");
		}
		if (!cmbOperationType.getSelectedItem().toString().equalsIgnoreCase("All"))
		{
		    sbSql.append(" and a.strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ");
		}
		if (!txtCustomerCode.getText().equalsIgnoreCase(""))
		{
		    sbSql.append(" and a.strCustomerCode='" + txtCustomerCode.getText() + "' ");

		}
		sbSql.append(" group by date(a.dteBillDate)");

		ResultSet rsDayWiseSales = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
		while (rsDayWiseSales.next())
		{
		    double settlementAmt = 0;
		    sbSqlForDiscount.setLength(0);
		    sbSqlForDiscount.append("select sum(b.dblSettlementAmt) "
			    + " from tblbillhd a, tblbillsettlementdtl b "
			    + " where a.strBillNo=b.strBillNo "
			    + " and date(a.dteBillDate)=date(b.dteBillDate) "
			    + " and date(a.dteBillDate) = '" + rsDayWiseSales.getString(7) + "' ");
		    if (!posCode.equals("All"))
		    {
			sbSqlForDiscount.append(" and " + objUtility.funGetSelectedPOSCodeString("a.strPOSCode", selectedPOSCodeSet) + " ");
		    }
		    if (clsGlobalVarClass.gEnableShiftYN && (!cmbShift.getSelectedItem().toString().equalsIgnoreCase("All")))
		    {
			sbSqlForDiscount.append(" AND a.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ");
		    }
		    if (!cmbArea.getSelectedItem().equals("All"))
		    {
			sbSqlForDiscount.append(" and a.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ");
		    }
		    if (!cmbOperationType.getSelectedItem().toString().equalsIgnoreCase("All"))
		    {
			sbSqlForDiscount.append(" and a.strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ");
		    }
		    sbSqlForDiscount.append(" group by date(a.dteBillDate)");

		    ResultSet rsSettllementAmt = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlForDiscount.toString());
		    if (rsSettllementAmt.next())
		    {
			settlementAmt = rsSettllementAmt.getDouble(1);
		    }
		    rsSettllementAmt.close();

		    records[0] = rsDayWiseSales.getString(1);       //day
		    records[1] = decFormat.format(Double.parseDouble(rsDayWiseSales.getString(2)));       //noOfBills
		    records[2] = gDecimalFormat.format(Double.parseDouble(rsDayWiseSales.getString(3)));       //subTotal
		    records[3] = gDecimalFormat.format(Double.parseDouble(rsDayWiseSales.getString(4)));       //disc
		    records[4] = gDecimalFormat.format(Double.parseDouble(rsDayWiseSales.getString(5)));       //tax
		    records[5] = gDecimalFormat.format(settlementAmt);     //sales

		    totalNoOfBills = totalNoOfBills + Integer.parseInt(rsDayWiseSales.getString(2));
		    totalSubTotal = totalSubTotal + Double.parseDouble(rsDayWiseSales.getString(3));
		    totalDiscount = totalDiscount + Double.parseDouble(rsDayWiseSales.getString(4));
		    totalTaxAmt = totalTaxAmt + Double.parseDouble(rsDayWiseSales.getString(5));
		    totalAmount = totalAmount + settlementAmt;
		    dm.addRow(records);
		}
		rsDayWiseSales.close();

		sbSql.setLength(0);
		sbSql.append("select DATE_FORMAT(date(a.dteBillDate),'%d-%m-%Y'),count(a.strBillNo),sum(a.dblSubTotal)"
			+ ",sum(a.dblDiscountAmt),sum(a.dblTaxAmt),'" + clsGlobalVarClass.gUserCode + "',date(a.dteBillDate) "
			+ " from tblqbillhd a "
			+ " where date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' ");
		if (!posCode.equals("All"))
		{
		    sbSql.append(" and " + objUtility.funGetSelectedPOSCodeString("a.strPOSCode", selectedPOSCodeSet) + " ");
		}
		if (clsGlobalVarClass.gEnableShiftYN && (!cmbShift.getSelectedItem().toString().equalsIgnoreCase("All")))
		{
		    sbSql.append(" AND a.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ");
		}
		if (!cmbArea.getSelectedItem().equals("All"))
		{
		    sbSql.append(" and a.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ");
		}
		if (!cmbOperationType.getSelectedItem().toString().equalsIgnoreCase("All"))
		{
		    sbSql.append(" and a.strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ");
		}
		sbSql.append(" group by date(a.dteBillDate)");

		rsDayWiseSales = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
		while (rsDayWiseSales.next())
		{
		    double settlementAmt = 0;
		    sbSqlForDiscount.setLength(0);
		    sbSqlForDiscount.append("select sum(b.dblSettlementAmt) "
			    + " from tblqbillhd a, tblqbillsettlementdtl b "
			    + " where a.strBillNo=b.strBillNo "
			    + " and date(a.dteBillDate)=date(b.dteBillDate) "
			    + " and date(a.dteBillDate) = '" + rsDayWiseSales.getString(7) + "' ");
		    if (!posCode.equals("All"))
		    {
			sbSqlForDiscount.append(" and " + objUtility.funGetSelectedPOSCodeString("a.strPOSCode", selectedPOSCodeSet) + " ");
		    }
		    if (clsGlobalVarClass.gEnableShiftYN && (!cmbShift.getSelectedItem().toString().equalsIgnoreCase("All")))
		    {
			sbSqlForDiscount.append(" AND a.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ");
		    }
		    if (!cmbArea.getSelectedItem().equals("All"))
		    {
			sbSqlForDiscount.append(" and a.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ");
		    }
		    if (!cmbOperationType.getSelectedItem().toString().equalsIgnoreCase("All"))
		    {
			sbSqlForDiscount.append(" and a.strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ");
		    }
		    sbSqlForDiscount.append(" group by date(a.dteBillDate)");

		    ResultSet rsSettllementAmt = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlForDiscount.toString());
		    if (rsSettllementAmt.next())
		    {
			settlementAmt = rsSettllementAmt.getDouble(1);
		    }
		    rsSettllementAmt.close();

		    records[0] = rsDayWiseSales.getString(1);       //day
		    records[1] = decFormat.format(rsDayWiseSales.getDouble(2));       //noOfBills
		    records[2] = gDecimalFormat.format(Double.parseDouble(rsDayWiseSales.getString(3)));       //subTotal
		    records[3] = gDecimalFormat.format(Double.parseDouble(rsDayWiseSales.getString(4)));       //disc
		    records[4] = gDecimalFormat.format(Double.parseDouble(rsDayWiseSales.getString(5)));       //tax
		    records[5] = gDecimalFormat.format(settlementAmt);     //sales

		    totalNoOfBills = totalNoOfBills + Integer.parseInt(rsDayWiseSales.getString(2));
		    totalSubTotal = totalSubTotal + Double.parseDouble(rsDayWiseSales.getString(3));
		    totalDiscount = totalDiscount + Double.parseDouble(rsDayWiseSales.getString(4));
		    totalTaxAmt = totalTaxAmt + Double.parseDouble(rsDayWiseSales.getString(5));
		    totalAmount = totalAmount + settlementAmt;
		    dm.addRow(records);
		}
		rsDayWiseSales.close();

		Object[] arrObjRows =
		{
		    "Total", decFormat.format(totalNoOfBills), gDecimalFormat.format(totalSubTotal), gDecimalFormat.format(totalDiscount), gDecimalFormat.format(totalTaxAmt), gDecimalFormat.format(totalAmount)
		};
		dm1.addRow(arrObjRows);

		tblTotal.setModel(dm1);
		tblSales.setModel(dm);
		tblSales.setSize(400, 400);
		tblSales.setRowHeight(25);
		tblTotal.setRowHeight(40);
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		tblSales.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
		tblSales.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
		tblSales.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		tblSales.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
		tblSales.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
		tblSales.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblSales.getColumnModel().getColumn(0).setPreferredWidth(180);
		tblSales.getColumnModel().getColumn(1).setPreferredWidth(120);
		tblSales.getColumnModel().getColumn(2).setPreferredWidth(122);
		tblSales.getColumnModel().getColumn(3).setPreferredWidth(120);
		tblSales.getColumnModel().getColumn(4).setPreferredWidth(120);
		tblSales.getColumnModel().getColumn(5).setPreferredWidth(120);

		DefaultTableCellRenderer rightRenderer1 = new DefaultTableCellRenderer();
		rightRenderer1.setHorizontalAlignment(JLabel.RIGHT);
		tblTotal.getColumnModel().getColumn(1).setCellRenderer(rightRenderer1);
		tblTotal.getColumnModel().getColumn(2).setCellRenderer(rightRenderer1);
		tblTotal.getColumnModel().getColumn(3).setCellRenderer(rightRenderer1);
		tblTotal.getColumnModel().getColumn(4).setCellRenderer(rightRenderer1);
		tblTotal.getColumnModel().getColumn(5).setCellRenderer(rightRenderer1);
		tblTotal.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblTotal.getColumnModel().getColumn(0).setPreferredWidth(180);
		tblTotal.getColumnModel().getColumn(1).setPreferredWidth(120);
		tblTotal.getColumnModel().getColumn(2).setPreferredWidth(120);
		tblTotal.getColumnModel().getColumn(3).setPreferredWidth(120);
		tblTotal.getColumnModel().getColumn(4).setPreferredWidth(120);
		tblTotal.getColumnModel().getColumn(5).setPreferredWidth(120);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    sbSql = null;
	}
    }

    private static Comparator<clsSalesFlashColumns> COMPARATOR = new Comparator<clsSalesFlashColumns>()
    {
	// This is where the sorting happens.
	public int compare(clsSalesFlashColumns o1, clsSalesFlashColumns o2)
	{
	    return (int) (o2.getSeqNo() - o1.getSeqNo());
	}
    };

    /**
     * Bill Wise Report
     */
    private void funBillWise()
    {
	StringBuilder sbSqlBillWise = new StringBuilder();
	StringBuilder sbSqlBillWiseQFile = new StringBuilder();

	try
	{
	    exportFormName = "BillWise";
	    reportName = "Bill Wise Sales Report";
	    totalAmount = new BigDecimal("0.00");
	    Disc = new BigDecimal("0.00");
	    temp = new BigDecimal("0.00");
	    fromDate = funGetFromDate();
	    toDate = funGetToDate();
	    funGetFromTime();
	    funGetToTime();
	    String dateFrom = "", field = null, dateTo = "";
	    if (funGetFromTime() != null)
	    {
		dateFrom = fromDate + " " + funGetFromTime();
		field = "a.dteBillDate";
	    }
	    else
	    {
		dateFrom = fromDate;
		field = "date(a.dteBillDate)";
	    }
	    if (funGetToTime() != null)
	    {
		dateTo = toDate + " " + funGetToTime();
	    }
	    else
	    {
		dateTo = toDate;
	    }

	    dm = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    //all cells false
		    return false;
		}
	    };
	    dm.addColumn("Bill No");//1
	    dm.addColumn("Date");//2
	    dm.addColumn("Bill Time");//3
	    dm.addColumn("Table Name");//4
	    if (clsGlobalVarClass.gCMSIntegrationYN)//
	    {
		dm.addColumn("Member Code");//5
	    }
	    else
	    {
		dm.addColumn("Cust Name");//5
	    }
	    dm.addColumn("POS");//6
	    dm.addColumn("Shift");//7
	    dm.addColumn("Pay Mode");//8
	    dm.addColumn("Delivery Charges");//9
	    dm.addColumn("SubTotal");//10
	    dm.addColumn("Disc %");//11
	    dm.addColumn("Disc Amt");//12
	    dm.addColumn("TAX Amt");//13
	    dm.addColumn("Adv. Amt");//14
	    dm.addColumn("Sales Amount");//15
	    dm.addColumn("Round Off");//16
	    dm.addColumn("Remarks");//17
	    dm.addColumn("Tip");//18
	    dm.addColumn("Discount Remarks");//19
	    dm.addColumn("Reason");//20
	    dm.addColumn("PAX");//21
	    dm.addColumn("Order Type");//22

	    vSalesReportExcelColLength = new java.util.Vector();
	    vSalesReportExcelColLength.add("6#Left"); //Bill No
	    vSalesReportExcelColLength.add("6#Left"); //Bill Date
	    vSalesReportExcelColLength.add("6#Left"); //Bill Time
	    vSalesReportExcelColLength.add("10#Left"); //Table Name
	    vSalesReportExcelColLength.add("10#Left"); //Cust/Member Code
	    vSalesReportExcelColLength.add("6#Left"); //POS 
	    vSalesReportExcelColLength.add("6#Right"); //SHIFT
	    vSalesReportExcelColLength.add("10#Left"); //Pay Mode
	    vSalesReportExcelColLength.add("6#Right"); //Del Charges
	    vSalesReportExcelColLength.add("6#Right"); //Sub Total
	    vSalesReportExcelColLength.add("6#Right"); //Disc Per
	    vSalesReportExcelColLength.add("6#Right"); //Disc Amt
	    vSalesReportExcelColLength.add("6#Right"); //Tax Amt
	    vSalesReportExcelColLength.add("6#Right"); //Adv Amt
	    vSalesReportExcelColLength.add("6#Right"); //Sales Amt
	    vSalesReportExcelColLength.add("6#Right"); //roundOff
	    vSalesReportExcelColLength.add("15#Left"); //Remarks
	    vSalesReportExcelColLength.add("15#Right"); //Tip Amt
	    vSalesReportExcelColLength.add("15#Right"); //Disc Remarks
	    vSalesReportExcelColLength.add("15#Right"); //Reason
	    vSalesReportExcelColLength.add("15#Right"); //PAX
	    vSalesReportExcelColLength.add("15#Left"); //Order Type

	    DefaultTableModel dm1 = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    //all cells false
		    return false;
		}
	    };
	    dm1.addColumn("");//1
	    dm1.addColumn("SubTotal");//2
	    dm1.addColumn("");//3
	    dm1.addColumn("Disc");//4
	    dm1.addColumn("Tax Total");//5
	    dm1.addColumn("Adv. Total");//6
	    dm1.addColumn("Sales Amount");//7
	    dm1.addColumn("Round Off");//8
	    dm1.addColumn("");//9
	    dm1.addColumn("Tip Amount");//10
	    dm1.addColumn("");//11
	    dm1.addColumn("");//12
	    dm1.addColumn("PAX");//13            
	    tblTotal.setModel(dm1);
	    records = new Object[22];
	    String pos = funGetSelectedPosCode();
	    String payMode = vSettlementMode.elementAt(cmbPayMode.getSelectedIndex()).toString();

	    String settlementMode = cmbPayMode.getSelectedItem().toString();
	    DecimalFormat decFormat = new DecimalFormat("0");

	    String subTotal = "ifnull(a.dblSubTotal,0.00)";
	    String discountAmt = "IFNULL(a.dblDiscountAmt,0.00)";
	    String taxAmt = "a.dblTaxAmt";
	    String settlementAmt = "ifnull(c.dblSettlementAmt,0.00)";
	    String deliveryCharges = "a.dblDeliveryCharges";
	    String tipAmt = "a.dblTipAmount";
	    String roundOffAmt = "a.dblRoundOff";
	    if (cmbCurrency.getSelectedItem().toString().equalsIgnoreCase("USD"))
	    {
		subTotal = "ifnull(a.dblSubTotal/a.dblUSDConverionRate,0.00)";
		discountAmt = "IFNULL(a.dblDiscountAmt/a.dblUSDConverionRate,0.00)";
		taxAmt = "a.dblTaxAmt/a.dblUSDConverionRate";
		settlementAmt = "ifnull(c.dblSettlementAmt/a.dblUSDConverionRate,0.00)";
		deliveryCharges = "a.dblDeliveryCharges/a.dblUSDConverionRate";
		tipAmt = "a.dblTipAmount/a.dblUSDConverionRate";
		roundOffAmt = "a.dblRoundOff/a.dblUSDConverionRate";
	    }

	    sbSqlBillWise.setLength(0);
	    sbSqlBillWise.append("select a.strBillNo,left(a.dteBillDate,10),left(right(a.dteDateCreated,8),5) as BillTime "
		    + " ,ifnull(b.strTableName,'') as TableName,f.strPOSName, ifnull(d.strSettelmentDesc,'') as payMode "
		    + " ," + subTotal + ",IFNULL(a.dblDiscountPer,0)," + discountAmt + "," + taxAmt + " "
		    + " ," + settlementAmt + ",a.strUserCreated "
		    + " ,a.strUserEdited,a.dteDateCreated,a.dteDateEdited,a.strClientCode,a.strWaiterNo "
		    + " ,a.strCustomerCode," + deliveryCharges + ",ifnull(c.strRemark,''),ifnull(e.strCustomerName ,'NA') "
		    + " ," + tipAmt + ",'" + clsGlobalVarClass.gUserCode + "',a.strDiscountRemark,ifnull(h.strReasonName ,'NA')"
		    + ",a.intShiftCode," + roundOffAmt + ",a.intBillSeriesPaxNo,ifnull(i.dblAdvDeposite,0),ifnull(k.strAdvOrderTypeName,'') "
		    + " from tblbillhd  a "
		    + " left outer join  tbltablemaster b on a.strTableNo=b.strTableNo "
		    + " left outer join tblposmaster f on a.strPOSCode=f.strPOSCode "
		    + " left outer join tblbillsettlementdtl c on a.strBillNo=c.strBillNo and a.strClientCode=c.strClientCode  and date(a.dteBillDate)=date(c.dteBillDate)  "
		    + " left outer join tblsettelmenthd d on c.strSettlementCode=d.strSettelmentCode "
		    + " left outer join tblcustomermaster e on a.strCustomerCode=e.strCustomerCode "
		    + " left outer join tblreasonmaster h on a.strReasonCode=h.strReasonCode "
		    + " left outer join tbladvancereceipthd i on a.strAdvBookingNo=i.strAdvBookingNo "
		    + " LEFT OUTER JOIN tbladvbookbillhd j ON a.strAdvBookingNo=j.strAdvBookingNo "
		    + " left outer join tbladvanceordertypemaster k on j.strOrderType=k.strAdvOrderTypeCode "
		    + " where " + field + " between '" + dateFrom + "' and '" + dateTo + "' "
		    + "  ");

	    if (!pos.equals("All"))
	    {
		sbSqlBillWise.append(" and " + objUtility.funGetSelectedPOSCodeString("a.strPOSCode", selectedPOSCodeSet) + " ");
	    }
	    if (clsGlobalVarClass.gEnableShiftYN && (!cmbShift.getSelectedItem().toString().equalsIgnoreCase("All")))
	    {
		sbSqlBillWise.append(" AND a.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ");
	    }

	    if (!cmbOperator.getSelectedItem().equals("All"))
	    {
		sbSqlBillWise.append(" and  a.strUserCreated='" + cmbOperator.getSelectedItem().toString() + "' ");
	    }
	    if (!cmbPayMode.getSelectedItem().equals("All"))
	    {
		sbSqlBillWise.append(" and a.strSettelmentMode='" + settlementMode + "' ");
	    }
	    if (txtBillNofrom.getText().trim().length() > 0 && txtBillnoTo.getText().trim().length() > 0)
	    {
		sbSqlBillWise.append(" and a.strBillNo between '" + txtBillNofrom.getText() + "' and '" + txtBillnoTo.getText() + "'");
	    }
	    if (!cmbArea.getSelectedItem().equals("All"))
	    {
		sbSqlBillWise.append(" and a.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ");
	    }
	    if (!cmbOperationType.getSelectedItem().toString().equalsIgnoreCase("All"))
	    {
		sbSqlBillWise.append(" and a.strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ");
	    }
	    if (!txtCustomerCode.getText().equalsIgnoreCase(""))
	    {
		sbSqlBillWise.append(" and a.strCustomerCode='" + txtCustomerCode.getText() + "' ");

	    }
	    sbSqlBillWise.append(" order by date(a.dteBillDate),a.strBillNo desc ");

	    sbSqlBillWiseQFile.setLength(0);
	    sbSqlBillWiseQFile.append("select a.strBillNo,left(a.dteBillDate,10),left(right(a.dteDateCreated,8),5) as BillTime "
		    + " ,ifnull(b.strTableName,'') as TableName,f.strPOSName, ifnull(d.strSettelmentDesc,'') as payMode "
		    + " ," + subTotal + ",IFNULL(a.dblDiscountPer,0)," + discountAmt + "," + taxAmt + " "
		    + " ," + settlementAmt + ",a.strUserCreated "
		    + " ,a.strUserEdited,a.dteDateCreated,a.dteDateEdited,a.strClientCode,a.strWaiterNo "
		    + " ,a.strCustomerCode," + deliveryCharges + ",ifnull(c.strRemark,''),ifnull(e.strCustomerName ,'NA') "
		    + " ," + tipAmt + ",'" + clsGlobalVarClass.gUserCode + "',a.strDiscountRemark,ifnull(h.strReasonName ,'NA')"
		    + ",a.intShiftCode," + roundOffAmt + ",a.intBillSeriesPaxNo,ifnull(i.dblAdvDeposite,0),ifnull(k.strAdvOrderTypeName,'')  "
		    + " from tblqbillhd a left outer join  tbltablemaster b on a.strTableNo=b.strTableNo "
		    + " left outer join tblposmaster f on a.strPOSCode=f.strPOSCode "
		    + " left outer join tblqbillsettlementdtl c on a.strBillNo=c.strBillNo and a.strClientCode=c.strClientCode   and date(a.dteBillDate)=date(c.dteBillDate)  "
		    + " left outer join tblsettelmenthd d on c.strSettlementCode=d.strSettelmentCode "
		    + " left outer join tblcustomermaster e on a.strCustomerCode=e.strCustomerCode "
		    + " left outer join tblreasonmaster h on a.strReasonCode=h.strReasonCode "
		    + " left outer join tblqadvancereceipthd i on a.strAdvBookingNo=i.strAdvBookingNo "
		    + " LEFT OUTER JOIN tblqadvbookbillhd j ON a.strAdvBookingNo=j.strAdvBookingNo "
		    + " left outer join tbladvanceordertypemaster k on j.strOrderType=k.strAdvOrderTypeCode "
		    + " where " + field + " between '" + dateFrom + "' and '" + dateTo + "' "
		    + "  ");

	    if (!pos.equals("All"))
	    {
		sbSqlBillWiseQFile.append(" and " + objUtility.funGetSelectedPOSCodeString("a.strPOSCode", selectedPOSCodeSet) + " ");
	    }
	    if (clsGlobalVarClass.gEnableShiftYN && (!cmbShift.getSelectedItem().toString().equalsIgnoreCase("All")))
	    {
		sbSqlBillWiseQFile.append(" AND a.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ");
	    }
	    if (!cmbOperator.getSelectedItem().equals("All"))
	    {
		sbSqlBillWiseQFile.append(" and  a.strUserCreated='" + cmbOperator.getSelectedItem().toString() + "' ");
	    }
	    if (!cmbPayMode.getSelectedItem().equals("All"))
	    {
		//selectQuery+="   and a.strSettelmentMode='"+cmbPayMode.getSelectedItem().toString()+"' ";
		sbSqlBillWiseQFile.append(" and a.strSettelmentMode='" + settlementMode + "' ");
	    }
	    if (!cmbArea.getSelectedItem().equals("All"))
	    {
		sbSqlBillWiseQFile.append(" and a.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ");
	    }
	    if (txtBillNofrom.getText().trim().length() > 0 && txtBillnoTo.getText().trim().length() > 0)
	    {
		sbSqlBillWiseQFile.append(" and a.strBillNo between '" + txtBillNofrom.getText() + "' and '" + txtBillnoTo.getText() + "'");
	    }
	    if (!cmbOperationType.getSelectedItem().toString().equalsIgnoreCase("All"))
	    {
		sbSqlBillWiseQFile.append(" and a.strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ");
	    }
	    if (!txtCustomerCode.getText().equalsIgnoreCase(""))
	    {
		sbSqlBillWiseQFile.append(" and a.strCustomerCode='" + txtCustomerCode.getText() + "' ");

	    }
	    sbSqlBillWiseQFile.append(" order by date(a.dteBillDate),a.strBillNo desc ");

	    //System.out.println(sbSqlBillWise);
	    //System.out.println(sbSqlBillWiseQFile);
	    double totalDiscAmt = 0, totalSubTotal = 0, totalTaxAmt = 0, totalAdvAmt = 0, totalSettleAmt = 0, totalTipAmt = 0, totalRoundOffAmt = 0;
	    boolean flgRecords = false;
	    int totalPAX = 0;

	    Map<String, List<clsSalesFlashColumns>> hmBillWiseSales = new HashMap<String, List<clsSalesFlashColumns>>();
	    int seqNo = 1;
	    //for live data
	    ResultSet rsBillWiseSales = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlBillWise.toString());
	    while (rsBillWiseSales.next())
	    {
		List<clsSalesFlashColumns> arrListBillWiseSales = new ArrayList<clsSalesFlashColumns>();
		flgRecords = true;
		String[] spDate = rsBillWiseSales.getString(2).split("-");
		String billDate = spDate[2] + "-" + spDate[1] + "-" + spDate[0];//billDate

		clsSalesFlashColumns objSalesFlashColumns = new clsSalesFlashColumns();
		objSalesFlashColumns.setStrField1(rsBillWiseSales.getString(1));
		objSalesFlashColumns.setStrField2(billDate);
		objSalesFlashColumns.setStrField3(rsBillWiseSales.getString(3));
		objSalesFlashColumns.setStrField4(rsBillWiseSales.getString(4));
		if (clsGlobalVarClass.gCMSIntegrationYN)
		{
		    objSalesFlashColumns.setStrField5(rsBillWiseSales.getString(18));//Member Code
		}
		else
		{
		    objSalesFlashColumns.setStrField5(rsBillWiseSales.getString(21));//Cust Name
		}
		objSalesFlashColumns.setStrField6(rsBillWiseSales.getString(5));
		objSalesFlashColumns.setStrField7(rsBillWiseSales.getString(6));
		objSalesFlashColumns.setStrField8(rsBillWiseSales.getString(19));
		objSalesFlashColumns.setStrField9(rsBillWiseSales.getString(7));
		objSalesFlashColumns.setStrField10(rsBillWiseSales.getString(8));
		objSalesFlashColumns.setStrField11(rsBillWiseSales.getString(9));
		objSalesFlashColumns.setStrField12(rsBillWiseSales.getString(10));
		objSalesFlashColumns.setStrField13(rsBillWiseSales.getString(11));
		objSalesFlashColumns.setStrField14(rsBillWiseSales.getString(20));
		objSalesFlashColumns.setStrField15(rsBillWiseSales.getString(22));
		objSalesFlashColumns.setStrField16(rsBillWiseSales.getString(24));
		objSalesFlashColumns.setStrField17(rsBillWiseSales.getString(25));
		objSalesFlashColumns.setStrField18(rsBillWiseSales.getString(26));//shift
		objSalesFlashColumns.setStrField19(rsBillWiseSales.getString(27));//roundOff
		objSalesFlashColumns.setStrField20(rsBillWiseSales.getString(28));//intBillSeriesPaxNo
		objSalesFlashColumns.setStrField21(rsBillWiseSales.getString(29));//dblAdvDeposite
		objSalesFlashColumns.setStrField22(rsBillWiseSales.getString(30));//strAdvOrderTypeName

//                objSalesFlashColumns.setSeqNo(Integer.parseInt(billNo.split("-")[0]));
		objSalesFlashColumns.setSeqNo(seqNo++);

		if (null != hmBillWiseSales.get(rsBillWiseSales.getString(1) + "!" + billDate))
		{
		    arrListBillWiseSales = hmBillWiseSales.get(rsBillWiseSales.getString(1) + "!" + billDate);
		    objSalesFlashColumns.setStrField9("0");
		    objSalesFlashColumns.setStrField10("0");
		    objSalesFlashColumns.setStrField11("0");
		    objSalesFlashColumns.setStrField12("0");
		    objSalesFlashColumns.setStrField15("0");
		    objSalesFlashColumns.setStrField19("0");//roundoff
		    objSalesFlashColumns.setStrField20("0");//intBillSeriesPaxNo
		    objSalesFlashColumns.setStrField21("0");//dblAdvDeposite
		}
		arrListBillWiseSales.add(objSalesFlashColumns);
		hmBillWiseSales.put(rsBillWiseSales.getString(1) + "!" + billDate, arrListBillWiseSales);

		totalDiscAmt += Double.parseDouble(objSalesFlashColumns.getStrField11());
		totalSubTotal += Double.parseDouble(objSalesFlashColumns.getStrField9());
		totalTaxAmt += Double.parseDouble(objSalesFlashColumns.getStrField12());
		totalAdvAmt += Double.parseDouble(objSalesFlashColumns.getStrField21());
		totalSettleAmt += Double.parseDouble(objSalesFlashColumns.getStrField13());// Grand Total                
		totalTipAmt += Double.parseDouble(objSalesFlashColumns.getStrField15());// tip Amt  
		totalRoundOffAmt += Double.parseDouble(objSalesFlashColumns.getStrField19());// roundoff Amt  
		totalPAX += Integer.parseInt(objSalesFlashColumns.getStrField20());//intBillSeriesPaxNo

	    }
	    rsBillWiseSales.close();

	    //for qfile data
	    rsBillWiseSales = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlBillWiseQFile.toString());
	    while (rsBillWiseSales.next())
	    {
		List<clsSalesFlashColumns> arrListBillWiseSales = new ArrayList<clsSalesFlashColumns>();
		flgRecords = true;

//                String billNo1=rsBillWiseSales.getString(1);
//                String billNo=billNo1.substring(1, billNo1.length());
		String[] spDate = rsBillWiseSales.getString(2).split("-");
		String billDate = spDate[2] + "-" + spDate[1] + "-" + spDate[0];//billDate

		clsSalesFlashColumns objSalesFlashColumns = new clsSalesFlashColumns();
		objSalesFlashColumns.setStrField1(rsBillWiseSales.getString(1));
		objSalesFlashColumns.setStrField2(billDate);
		objSalesFlashColumns.setStrField3(rsBillWiseSales.getString(3));
		objSalesFlashColumns.setStrField4(rsBillWiseSales.getString(4));
		if (clsGlobalVarClass.gCMSIntegrationYN)
		{
		    objSalesFlashColumns.setStrField5(rsBillWiseSales.getString(18));//Member Code
		}
		else
		{
		    objSalesFlashColumns.setStrField5(rsBillWiseSales.getString(21));//Cust Name
		}
		objSalesFlashColumns.setStrField6(rsBillWiseSales.getString(5));
		objSalesFlashColumns.setStrField7(rsBillWiseSales.getString(6));
		objSalesFlashColumns.setStrField8(rsBillWiseSales.getString(19));
		objSalesFlashColumns.setStrField9(rsBillWiseSales.getString(7));
		objSalesFlashColumns.setStrField10(rsBillWiseSales.getString(8));
		objSalesFlashColumns.setStrField11(rsBillWiseSales.getString(9));
		objSalesFlashColumns.setStrField12(rsBillWiseSales.getString(10));
		objSalesFlashColumns.setStrField13(rsBillWiseSales.getString(11));
		objSalesFlashColumns.setStrField14(rsBillWiseSales.getString(20));
		objSalesFlashColumns.setStrField15(rsBillWiseSales.getString(22));
		objSalesFlashColumns.setStrField16(rsBillWiseSales.getString(24));
		objSalesFlashColumns.setStrField17(rsBillWiseSales.getString(25));
		objSalesFlashColumns.setStrField18(rsBillWiseSales.getString(26));//shift
		objSalesFlashColumns.setStrField19(rsBillWiseSales.getString(27));//roundOff
		objSalesFlashColumns.setStrField20(rsBillWiseSales.getString(28));//intBillSeriesPaxNo
		objSalesFlashColumns.setStrField21(rsBillWiseSales.getString(29));//dblAdvDeposite
		objSalesFlashColumns.setStrField22(rsBillWiseSales.getString(30));//strAdvOrderTypeName

//                objSalesFlashColumns.setSeqNo(Integer.parseInt(billNo.split("-")[0]));
		objSalesFlashColumns.setSeqNo(seqNo++);

		if (null != hmBillWiseSales.get(rsBillWiseSales.getString(1) + "!" + billDate))
		{
		    arrListBillWiseSales = hmBillWiseSales.get(rsBillWiseSales.getString(1) + "!" + billDate);
		    objSalesFlashColumns.setStrField9("0");
		    objSalesFlashColumns.setStrField10("0");
		    objSalesFlashColumns.setStrField11("0");
		    objSalesFlashColumns.setStrField12("0");
		    objSalesFlashColumns.setStrField15("0");
		    objSalesFlashColumns.setStrField19("0");//roundoff
		    objSalesFlashColumns.setStrField20("0");//intBillSeriesPaxNo
		    objSalesFlashColumns.setStrField21("0");//dblAdvDeposite
		}
		arrListBillWiseSales.add(objSalesFlashColumns);
		hmBillWiseSales.put(rsBillWiseSales.getString(1) + "!" + billDate, arrListBillWiseSales);

		totalDiscAmt += Double.parseDouble(objSalesFlashColumns.getStrField11());
		totalSubTotal += Double.parseDouble(objSalesFlashColumns.getStrField9());
		totalTaxAmt += Double.parseDouble(objSalesFlashColumns.getStrField12());
		totalAdvAmt += Double.parseDouble(objSalesFlashColumns.getStrField21());
		totalSettleAmt += Double.parseDouble(objSalesFlashColumns.getStrField13());// Grand Total 
		totalTipAmt += Double.parseDouble(objSalesFlashColumns.getStrField15());// tip Amt  
		totalRoundOffAmt += Double.parseDouble(objSalesFlashColumns.getStrField19());// roundoff Amt  
		totalPAX += Integer.parseInt(objSalesFlashColumns.getStrField20());//intBillSeriesPaxNo
	    }
	    rsBillWiseSales.close();

//            System.out.println("Tip Amount->" + totalTipAmt);
//        fill arrTempListBillWiseSales from hashmap of bill
	    List<clsSalesFlashColumns> arrTempListBillWiseSales = new ArrayList<clsSalesFlashColumns>();
	    for (Map.Entry<String, List<clsSalesFlashColumns>> entry : hmBillWiseSales.entrySet())
	    {
		for (clsSalesFlashColumns objSalesFlashColumns : entry.getValue())
		{
		    clsSalesFlashColumns objTempSalesFlashColumns = new clsSalesFlashColumns();
		    objTempSalesFlashColumns.setStrField1(objSalesFlashColumns.getStrField1());
		    objTempSalesFlashColumns.setStrField2(objSalesFlashColumns.getStrField2());
		    objTempSalesFlashColumns.setStrField3(objSalesFlashColumns.getStrField3());
		    objTempSalesFlashColumns.setStrField4(objSalesFlashColumns.getStrField4());
		    objTempSalesFlashColumns.setStrField5(objSalesFlashColumns.getStrField5());
		    objTempSalesFlashColumns.setStrField6(objSalesFlashColumns.getStrField6());
		    objTempSalesFlashColumns.setStrField7(objSalesFlashColumns.getStrField7());
		    objTempSalesFlashColumns.setStrField8(objSalesFlashColumns.getStrField8());
		    objTempSalesFlashColumns.setStrField9(objSalesFlashColumns.getStrField9());
		    objTempSalesFlashColumns.setStrField10(objSalesFlashColumns.getStrField10());
		    objTempSalesFlashColumns.setStrField11(objSalesFlashColumns.getStrField11());
		    objTempSalesFlashColumns.setStrField12(objSalesFlashColumns.getStrField12());
		    objTempSalesFlashColumns.setStrField13(objSalesFlashColumns.getStrField13());
		    objTempSalesFlashColumns.setStrField14(objSalesFlashColumns.getStrField14());
		    objTempSalesFlashColumns.setStrField15(objSalesFlashColumns.getStrField15());
		    objTempSalesFlashColumns.setStrField16(objSalesFlashColumns.getStrField16());
		    objTempSalesFlashColumns.setStrField17(objSalesFlashColumns.getStrField17());
		    objTempSalesFlashColumns.setStrField18(objSalesFlashColumns.getStrField18());
		    objTempSalesFlashColumns.setStrField19(objSalesFlashColumns.getStrField19());
		    objTempSalesFlashColumns.setStrField20(objSalesFlashColumns.getStrField20());
		    objTempSalesFlashColumns.setStrField21(objSalesFlashColumns.getStrField21());
		    objTempSalesFlashColumns.setStrField22(objSalesFlashColumns.getStrField22());

		    objTempSalesFlashColumns.setSeqNo(objSalesFlashColumns.getSeqNo());

		    arrTempListBillWiseSales.add(objTempSalesFlashColumns);
		}
	    }

	    //sort arrTempListBillWiseSales 
	    Collections.sort(arrTempListBillWiseSales, COMPARATOR);

	    // fill table from sorted arrTempListBillWiseSales  
	    for (clsSalesFlashColumns objSalesFlashColumns : arrTempListBillWiseSales)
	    {
		records[0] = objSalesFlashColumns.getStrField1();
		records[1] = objSalesFlashColumns.getStrField2();
		records[2] = objSalesFlashColumns.getStrField3();
		records[3] = objSalesFlashColumns.getStrField4();
		records[4] = objSalesFlashColumns.getStrField5();
		records[5] = objSalesFlashColumns.getStrField6();//pos
		records[6] = objSalesFlashColumns.getStrField18();//shift
		records[7] = objSalesFlashColumns.getStrField7();
		records[8] = gDecimalFormat.format(Double.parseDouble(objSalesFlashColumns.getStrField8()));
		records[9] = gDecimalFormat.format(Double.parseDouble(objSalesFlashColumns.getStrField9()));
		records[10] = gDecimalFormat.format(Double.parseDouble(objSalesFlashColumns.getStrField10()));
		records[11] = gDecimalFormat.format(Double.parseDouble(objSalesFlashColumns.getStrField11()));
		records[12] = gDecimalFormat.format(Double.parseDouble(objSalesFlashColumns.getStrField12()));
		records[13] = gDecimalFormat.format(Double.parseDouble(objSalesFlashColumns.getStrField21()));//advAmt 21
		records[14] = gDecimalFormat.format(Double.parseDouble(objSalesFlashColumns.getStrField13()));//sales amt
		records[15] = gDecimalFormat.format(Double.parseDouble(objSalesFlashColumns.getStrField19()));//roundOff
		records[16] = objSalesFlashColumns.getStrField14();
		records[17] = gDecimalFormat.format(Double.parseDouble(objSalesFlashColumns.getStrField15()));
		records[18] = objSalesFlashColumns.getStrField16();
		records[19] = objSalesFlashColumns.getStrField17();
		records[20] = objSalesFlashColumns.getStrField20();
		records[21] = objSalesFlashColumns.getStrField22();//strAdvOrderTypeName

		dm.addRow(records);
	    }

	    if (!flgRecords)
	    {
		dm.setRowCount(0);
		dm1.setRowCount(0);
	    }
	    Object[] ob1 =
	    {
		"Total", gDecimalFormat.format(totalSubTotal), "", gDecimalFormat.format(totalDiscAmt), gDecimalFormat.format(totalTaxAmt), gDecimalFormat.format(totalAdvAmt), gDecimalFormat.format(totalSettleAmt), gDecimalFormat.format(totalRoundOffAmt), "", gDecimalFormat.format(totalTipAmt), "", "", totalPAX
	    };
	    dm1.addRow(ob1);

	    tblTotal.setModel(dm1);
	    tblSales.setModel(dm);
	    tblSales.setRowHeight(25);
	    tblTotal.setRowHeight(40);

	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	    DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
	    leftRenderer.setHorizontalAlignment(JLabel.LEFT);

	    tblSales.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);//bill no
	    tblSales.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
	    tblSales.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);
	    tblSales.getColumnModel().getColumn(3).setCellRenderer(leftRenderer);
	    tblSales.getColumnModel().getColumn(4).setCellRenderer(leftRenderer);
	    tblSales.getColumnModel().getColumn(5).setCellRenderer(leftRenderer);//pos
	    tblSales.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);//shift
	    tblSales.getColumnModel().getColumn(7).setCellRenderer(leftRenderer);
	    tblSales.getColumnModel().getColumn(8).setCellRenderer(rightRenderer);
	    tblSales.getColumnModel().getColumn(9).setCellRenderer(rightRenderer);
	    tblSales.getColumnModel().getColumn(10).setCellRenderer(rightRenderer);
	    tblSales.getColumnModel().getColumn(11).setCellRenderer(rightRenderer);
	    tblSales.getColumnModel().getColumn(12).setCellRenderer(rightRenderer);//taxAmt
	    tblSales.getColumnModel().getColumn(13).setCellRenderer(rightRenderer);//advAmt
	    tblSales.getColumnModel().getColumn(14).setCellRenderer(rightRenderer);//salesAmt
	    tblSales.getColumnModel().getColumn(15).setCellRenderer(rightRenderer);
	    tblSales.getColumnModel().getColumn(16).setCellRenderer(leftRenderer);
	    tblSales.getColumnModel().getColumn(17).setCellRenderer(rightRenderer);//tip
	    tblSales.getColumnModel().getColumn(18).setCellRenderer(leftRenderer);
	    tblSales.getColumnModel().getColumn(19).setCellRenderer(leftRenderer);
	    tblSales.getColumnModel().getColumn(20).setCellRenderer(rightRenderer);//int billseries pax
	    tblSales.getColumnModel().getColumn(21).setCellRenderer(leftRenderer);//strAdvOrderTypeName

	    tblSales.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    tblSales.getColumnModel().getColumn(0).setPreferredWidth(80);
	    tblSales.getColumnModel().getColumn(1).setPreferredWidth(80);
	    tblSales.getColumnModel().getColumn(2).setPreferredWidth(60);
	    tblSales.getColumnModel().getColumn(3).setPreferredWidth(80);
	    tblSales.getColumnModel().getColumn(4).setPreferredWidth(80);
	    tblSales.getColumnModel().getColumn(5).setPreferredWidth(100);
	    tblSales.getColumnModel().getColumn(6).setPreferredWidth(50);
	    tblSales.getColumnModel().getColumn(7).setPreferredWidth(70);
	    tblSales.getColumnModel().getColumn(8).setPreferredWidth(60);
	    tblSales.getColumnModel().getColumn(9).setPreferredWidth(75);
	    tblSales.getColumnModel().getColumn(10).setPreferredWidth(60);
	    tblSales.getColumnModel().getColumn(11).setPreferredWidth(75);
	    tblSales.getColumnModel().getColumn(12).setPreferredWidth(100);//taxAmt
	    tblSales.getColumnModel().getColumn(13).setPreferredWidth(100);//advAmt
	    tblSales.getColumnModel().getColumn(14).setPreferredWidth(100);
	    tblSales.getColumnModel().getColumn(15).setPreferredWidth(60);
	    tblSales.getColumnModel().getColumn(16).setPreferredWidth(120);
	    tblSales.getColumnModel().getColumn(17).setPreferredWidth(50);
	    tblSales.getColumnModel().getColumn(18).setPreferredWidth(100);
	    tblSales.getColumnModel().getColumn(19).setPreferredWidth(100);
	    tblSales.getColumnModel().getColumn(20).setPreferredWidth(50);
	    tblSales.getColumnModel().getColumn(21).setPreferredWidth(100);

	    tblTotal.setSize(400, 400);
	    DefaultTableCellRenderer rightRenderer1 = new DefaultTableCellRenderer();
	    rightRenderer1.setHorizontalAlignment(JLabel.RIGHT);

	    tblTotal.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
	    tblTotal.getColumnModel().getColumn(1).setCellRenderer(rightRenderer1);
	    tblTotal.getColumnModel().getColumn(2).setCellRenderer(rightRenderer1);
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
	    tblTotal.getColumnModel().getColumn(0).setPreferredWidth(310);
	    tblTotal.getColumnModel().getColumn(1).setPreferredWidth(100);
	    tblTotal.getColumnModel().getColumn(2).setPreferredWidth(100);
	    tblTotal.getColumnModel().getColumn(3).setPreferredWidth(100);
	    tblTotal.getColumnModel().getColumn(4).setPreferredWidth(100);
	    tblTotal.getColumnModel().getColumn(5).setPreferredWidth(100);
	    tblTotal.getColumnModel().getColumn(6).setPreferredWidth(100);
	    tblTotal.getColumnModel().getColumn(7).setPreferredWidth(100);
	    tblTotal.getColumnModel().getColumn(8).setPreferredWidth(50);
	    tblTotal.getColumnModel().getColumn(9).setPreferredWidth(50);
	    tblTotal.getColumnModel().getColumn(10).setPreferredWidth(50);
	    tblTotal.getColumnModel().getColumn(11).setPreferredWidth(50);

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    sbSqlBillWise = null;
	    sbSqlBillWiseQFile = null;
	}
    }

    /**
     * Item Wise Report
     */
    private void funItemWise()
    {
	try
	{
	    reportName = "Item Wise Sales Report";
	    totalQty = new Double("0.00");
	    totalAmount = new BigDecimal("0.00");
	    temp = new BigDecimal("0.00");
	    temp1 = new BigDecimal("0.00");
	    dm = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    //all cells false
		    return false;
		}
	    };
	    // dm.addColumn("Item Code");
	    dm.addColumn("Item Name");
	    dm.addColumn("POS");
	    dm.addColumn("Quantity");
	    dm.addColumn("SubTotal");
	    dm.addColumn("Discount");
	    dm.addColumn("Sales Amount");
	    //dm.addColumn("Bill Date");

	    vSalesReportExcelColLength = new java.util.Vector();
	    // vSalesReportExcelColLength.add("6#Left"); //Item Code
	    vSalesReportExcelColLength.add("15#Left"); //Item Name
	    vSalesReportExcelColLength.add("6#Left"); //POS
	    vSalesReportExcelColLength.add("7#Right"); //Qty
	    vSalesReportExcelColLength.add("7#Right"); //Amt
	    vSalesReportExcelColLength.add("6#Right"); //subtotal
	    vSalesReportExcelColLength.add("6#Right"); //discount
	    vSalesReportExcelColLength.add("8#Left"); //discount

	    DefaultTableModel dm1 = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    //all cells false
		    return false;
		}
	    };

	    dm1.addColumn("");
	    dm1.addColumn("");
	    dm1.addColumn("Quantity");
	    dm1.addColumn("SubTotal");
	    dm1.addColumn("Discount");
	    dm1.addColumn("Sales Amount");
	    //dm1.addColumn("");

	    Date dt1 = dteFromDate.getDate();
	    Date dt2 = dteToDate.getDate();
	    if ((dt2.getTime() - dt1.getTime()) < 0)
	    {
		new frmOkPopUp(null, "Invalid date", "Error", 1).setVisible(true);
	    }
	    else
	    {
		fromDate = funGetFromDate();
		toDate = funGetToDate();

		funGetFromTime();
		funGetToTime();
		String DateFrom = null, field = null, DateTo = null;
		if (funGetFromTime() != null)
		{
		    DateFrom = fromDate + " " + funGetFromTime();
		    field = "b.dteBillDate";
		}
		else
		{
		    DateFrom = fromDate;
		    field = "date(b.dteBillDate)";
		}
		if (funGetToTime() != null)
		{
		    DateTo = toDate + " " + funGetToTime();
		}
		else
		{
		    DateTo = toDate;
		}

		records = new Object[8];
		String pos = funGetSelectedPosCode();
		String sqlFilters = "";

		String amount = "sum(a.dblAmount)";
		String discountAmt = "sum(a.dblDiscountAmt)";
		String netTotalAmt = "sum(a.dblAmount)-sum(a.dblDiscountAmt)";
		String taxAmt = "sum(a.dblTaxAmount)";
		String settlementAmt = "";
		String deliveryCharges = "";
		String tipAmt = "";
		String roundOffAmt = "";

		String mSubTotal = "sum(a.dblAmount)";
		String mDiscountAmt = "sum(a.dblDiscAmt)";
		String mNetTotalAmt = "sum(a.dblAmount)-sum(a.dblDiscAmt)";
		String mTaxAmt = "0";

		if (cmbCurrency.getSelectedItem().toString().equalsIgnoreCase("USD"))
		{
		    amount = "sum(a.dblAmount/b.dblUSDConverionRate)";
		    discountAmt = "sum(a.dblDiscountAmt/b.dblUSDConverionRate)";
		    netTotalAmt = "sum(a.dblAmount/b.dblUSDConverionRate)-sum(a.dblDiscountAmt/b.dblUSDConverionRate)";
		    taxAmt = "sum(a.dblTaxAmount/b.dblUSDConverionRate)";
		    settlementAmt = "";
		    deliveryCharges = "";
		    tipAmt = "";
		    roundOffAmt = "";

		    mSubTotal = "sum(a.dblAmount/b.dblUSDConverionRate)";
		    mDiscountAmt = "sum(a.dblDiscAmt/b.dblUSDConverionRate)";
		    mNetTotalAmt = "sum(a.dblAmount/b.dblUSDConverionRate)-sum(a.dblDiscAmt/b.dblUSDConverionRate)";
		    mTaxAmt = "0";
		}

		String sqlLive = "select a.strItemCode,a.strItemName,c.strPOSName"
			+ ",sum(a.dblQuantity)," + taxAmt + " "
			+ "," + netTotalAmt + ",'" + clsGlobalVarClass.gUserCode + "' "
			+ "," + amount + "," + discountAmt + ",DATE_FORMAT(date(b.dteBillDate),'%d-%m-%Y'),b.strPOSCode "
			+ "from tblbilldtl a,tblbillhd b,tblposmaster c "
			+ "where a.strBillNo=b.strBillNo "
			+ "AND DATE(a.dteBillDate)=DATE(b.dteBillDate)  "
			+ "and b.strPOSCode=c.strPosCode "
			+ "and a.strClientCode=b.strClientCode "
			+ "and " + field + " BETWEEN '" + DateFrom + "' AND '" + DateTo + "' ";

		String sqlQFile = "select a.strItemCode,a.strItemName,c.strPOSName"
			+ ",sum(a.dblQuantity)," + taxAmt + " "
			+ "," + netTotalAmt + ",'" + clsGlobalVarClass.gUserCode + "' "
			+ "," + amount + "," + discountAmt + ",DATE_FORMAT(date(b.dteBillDate),'%d-%m-%Y'),b.strPOSCode   "
			+ "from tblqbilldtl a,tblqbillhd b,tblposmaster c "
			+ "where a.strBillNo=b.strBillNo "
			+ "AND DATE(a.dteBillDate)=DATE(b.dteBillDate) "
			+ "and b.strPOSCode=c.strPosCode "
			+ "and a.strClientCode=b.strClientCode "
			+ "and " + field + " BETWEEN '" + DateFrom + "' AND '" + DateTo + "' ";

		String sqlModLive = "select a.strItemCode,a.strModifierName,c.strPOSName"
			+ " ,sum(a.dblQuantity),'0'," + mNetTotalAmt + ",'" + clsGlobalVarClass.gUserCode + "' "
			+ " ," + mSubTotal + "," + mDiscountAmt + ",DATE_FORMAT(date(b.dteBillDate),'%d-%m-%Y'),b.strPOSCode "
			+ " from tblbillmodifierdtl a,tblbillhd b,tblposmaster c,tblitemmaster d\n"
			+ " where a.strBillNo=b.strBillNo "
			+ " AND DATE(a.dteBillDate)=DATE(b.dteBillDate) "
			+ " and b.strPOSCode=c.strPosCode "
			+ " and a.strClientCode=b.strClientCode "
			+ " and left(a.strItemCode,7)=d.strItemCode "
			+ " AND a.dblamount>0  "
			+ " and " + field + " BETWEEN '" + DateFrom + "' AND '" + DateTo + "' ";

		String sqlModQFile = "select a.strItemCode,a.strModifierName,c.strPOSName"
			+ " ,sum(a.dblQuantity),'0'," + mNetTotalAmt + ",'" + clsGlobalVarClass.gUserCode + "' "
			+ " ," + mSubTotal + "," + mDiscountAmt + ",DATE_FORMAT(date(b.dteBillDate),'%d-%m-%Y'),b.strPOSCode "
			+ " from tblqbillmodifierdtl a,tblqbillhd b,tblposmaster c,tblitemmaster d\n"
			+ " where a.strBillNo=b.strBillNo "
			+ " AND DATE(a.dteBillDate)=DATE(b.dteBillDate) "
			+ " and b.strPOSCode=c.strPosCode "
			+ " and a.strClientCode=b.strClientCode "
			+ " and left(a.strItemCode,7)=d.strItemCode  "
			+ " AND a.dblamount>0 "
			+ " and " + field + " BETWEEN '" + DateFrom + "' AND '" + DateTo + "' ";

		if (!pos.equals("All") && !cmbOperator.getSelectedItem().equals("All"))
		{
		    sqlFilters += " AND " + objUtility.funGetSelectedPOSCodeString("b.strPOSCode", selectedPOSCodeSet) + " and b.strUserCreated='" + cmbOperator.getSelectedItem().toString() + "' ";
		}
		else if (!pos.equals("All") && cmbOperator.getSelectedItem().equals("All"))
		{
		    sqlFilters += " AND " + objUtility.funGetSelectedPOSCodeString("b.strPOSCode", selectedPOSCodeSet) + " ";
		}
		else if (pos.equals("All") && !cmbOperator.getSelectedItem().equals("All"))
		{
		    sqlFilters += " AND b.strUserCreated='" + cmbOperator.getSelectedItem().toString() + "' ";
		}
		if (clsGlobalVarClass.gEnableShiftYN && (!cmbShift.getSelectedItem().toString().equalsIgnoreCase("All")))
		{
		    sqlFilters += " AND b.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ";
		}
		if (txtBillNofrom.getText().trim().length() == 0 && txtBillnoTo.getText().trim().length() == 0)
		{
		}
		else
		{
		    sqlFilters += " and a.strbillno between '" + txtBillNofrom.getText() + "' "
			    + " and '" + txtBillnoTo.getText() + "'";
		}
		if (!cmbArea.getSelectedItem().equals("All"))
		{
		    sqlFilters += " and b.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ";
		}
		if (!cmbOperationType.getSelectedItem().toString().equalsIgnoreCase("All"))
		{
		    sqlFilters += " and b.strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ";
		}
		if (!txtCustomerCode.getText().equalsIgnoreCase(""))
		{
		    sqlFilters += " and b.strCustomerCode='" + txtCustomerCode.getText() + "' ";

		}
		sqlFilters += " group by a.strItemCode,c.strPOSName "
			+ " order by b.dteBillDate ";
		sqlLive = sqlLive + " " + sqlFilters;
		sqlQFile = sqlQFile + " " + sqlFilters;

		sqlModLive = sqlModLive + " " + sqlFilters;
		sqlModQFile = sqlModQFile + " " + sqlFilters;

		System.out.println(sqlModQFile);

		boolean flgRecords = false;
		subTotal = 0.00;
		discountTotal = 0.00;
		if (!flgRecords)
		{
		    dm.setRowCount(0);
		    dm1.setRowCount(0);
		}

		mapPOSItemDtl = new LinkedHashMap<>();

		//for item live
		ResultSet rsItemWiseSales = clsGlobalVarClass.dbMysql.executeResultSet(sqlLive);
		funGenerateItemWiseSales(rsItemWiseSales);

		//for item modiLive
//                rsItemWiseSales = clsGlobalVarClass.dbMysql.executeResultSet(sqlModLive);
//                funGenerateItemWiseSales(rsItemWiseSales);
		//for item Q
		rsItemWiseSales = clsGlobalVarClass.dbMysql.executeResultSet(sqlQFile);
		funGenerateItemWiseSales(rsItemWiseSales);

//                 //for item QModi
//                rsItemWiseSales = clsGlobalVarClass.dbMysql.executeResultSet(sqlModQFile);
//                funGenerateItemWiseSales(rsItemWiseSales);
		Set<Entry<String, Map<String, clsBillItemDtl>>> set = mapPOSItemDtl.entrySet();
		List<Entry<String, Map<String, clsBillItemDtl>>> list = new ArrayList<Entry<String, Map<String, clsBillItemDtl>>>(set);
		Collections.sort(list, new Comparator<Map.Entry<String, Map<String, clsBillItemDtl>>>()
		{

		    @Override
		    public int compare(Entry<String, Map<String, clsBillItemDtl>> o1, Entry<String, Map<String, clsBillItemDtl>> o2)
		    {

			Iterator<Entry<String, clsBillItemDtl>> it1 = o1.getValue().entrySet().iterator();
			Iterator<Entry<String, clsBillItemDtl>> it2 = o2.getValue().entrySet().iterator();

			if (it1.hasNext())
			{
			    if (it1.next().getValue().getItemCode().substring(0, 7).equalsIgnoreCase(it1.next().getValue().getItemCode().substring(0, 7)))
			    {
				return 0;
			    }
			    else
			    {
				return 1;
			    }
			}
			return 0;
		    }

		});

		DecimalFormat decFormat = new DecimalFormat("0");

		Iterator<Map.Entry<String, Map<String, clsBillItemDtl>>> posIterator = mapPOSItemDtl.entrySet().iterator();
		while (posIterator.hasNext())
		{
		    Map<String, clsBillItemDtl> mapItemDtl = posIterator.next().getValue();
		    Iterator<Map.Entry<String, clsBillItemDtl>> itemIterator = mapItemDtl.entrySet().iterator();
		    while (itemIterator.hasNext())
		    {
			clsBillItemDtl objBillItemDtl = itemIterator.next().getValue();

			//records[0] = rsItemWiseSales.getString(1);//itemCode
			records[0] = objBillItemDtl.getItemName();//itemName
			records[1] = objBillItemDtl.getPosName();//posName
			records[2] = decFormat.format(objBillItemDtl.getQuantity());//qty                        
			records[3] = gDecimalFormat.format(objBillItemDtl.getSubTotal());//sunTotal
			records[4] = gDecimalFormat.format(objBillItemDtl.getDiscountAmount());//discount
			records[5] = gDecimalFormat.format(objBillItemDtl.getAmount());//salesAmount
			//records[6] = objBillItemDtl.getBillDateTime();//date

			totalQty = totalQty + Double.parseDouble(records[2].toString());
			temp1 = new BigDecimal(objBillItemDtl.getAmount());
			totalAmount = totalAmount.add(temp1);
			subTotal = subTotal + objBillItemDtl.getSubTotal();
			discountTotal = discountTotal + objBillItemDtl.getDiscountAmount();

			dm.addRow(records);
		    }
		}

		Object[] ob1
			=
			{
			    "Total", "", decFormat.format(totalQty), gDecimalFormat.format(subTotal), gDecimalFormat.format(discountTotal), gDecimalFormat.format(totalAmount), ""
			};
		dm1.addRow(ob1);

		tblTotal.setModel(dm1);
		tblSales.setModel(dm);

		tblSales.setSize(400, 400);
		tblSales.setRowHeight(25);
		tblTotal.setRowHeight(40);

		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		tblSales.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
		tblSales.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		tblSales.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
		tblSales.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);

		tblSales.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		tblSales.getColumnModel().getColumn(0).setPreferredWidth(220);
		tblSales.getColumnModel().getColumn(2).setPreferredWidth(50);
		tblSales.getColumnModel().getColumn(3).setPreferredWidth(70);
		tblSales.getColumnModel().getColumn(4).setPreferredWidth(70);
		tblSales.getColumnModel().getColumn(5).setPreferredWidth(50);

		tblTotal.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
		tblTotal.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		tblTotal.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
		tblTotal.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);

		tblTotal.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		tblTotal.getColumnModel().getColumn(0).setPreferredWidth(220);
		tblTotal.getColumnModel().getColumn(2).setPreferredWidth(50);
		tblTotal.getColumnModel().getColumn(3).setPreferredWidth(70);
		tblTotal.getColumnModel().getColumn(4).setPreferredWidth(70);
		tblTotal.getColumnModel().getColumn(5).setPreferredWidth(50);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    /**
     * Item Wise Consumption Report
     */
    private void funItemWiseConsumption()
    {
	int sqlNo = 0;
	StringBuilder sbSql = new StringBuilder();
	StringBuilder sbSqlMod = new StringBuilder();
	StringBuilder sbFilters = new StringBuilder();
	ResultSet rsSalesMod;
	Map<String, clsItemWiseConsumption> hmItemWiseConsumption = new HashMap<String, clsItemWiseConsumption>();

	try
	{
	    reportName = "Item Wise Consumption Report";

	    Date dt1 = dteFromDate.getDate();
	    Date dt2 = dteToDate.getDate();
	    if ((dt2.getTime() - dt1.getTime()) < 0)
	    {
		new frmOkPopUp(null, "Invalid date", "Error", 1).setVisible(true);
	    }
	    else
	    {
		fromDate = funGetFromDate();
		toDate = funGetToDate();
		records = new Object[8];
		String pos = funGetSelectedPosCode();

		sbFilters.setLength(0);

		if (!pos.equals("All") && !cmbOperator.getSelectedItem().equals("All"))
		{
		    sbFilters.append(" AND a.strPOSCode = '" + pos + "' and a.strUserCreated='" + cmbOperator.getSelectedItem().toString() + "' ");
		}
		else if (!pos.equals("All") && cmbOperator.getSelectedItem().equals("All"))
		{
		    sbFilters.append(" AND a.strPOSCode = '" + pos + "' ");
		}
		else if (pos.equals("All") && !cmbOperator.getSelectedItem().equals("All"))
		{
		    sbFilters.append(" AND a.strUserCreated='" + cmbOperator.getSelectedItem().toString() + "' ");
		}
		if (txtBillNofrom.getText().trim().length() == 0 && txtBillnoTo.getText().trim().length() == 0)
		{
		}
		else
		{
		    sbFilters.append(" and a.strbillno between '" + txtBillNofrom.getText() + "' "
			    + " and '" + txtBillnoTo.getText() + "'");
		}
		if (clsGlobalVarClass.gEnableShiftYN && (!cmbShift.getSelectedItem().toString().equalsIgnoreCase("All")))
		{
		    sbFilters.append(" AND a.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ");
		}

		sbFilters.append(" group by b.strItemCode,e.strPOSName "
			+ " order by a.dteBillDate ");

		// Code for Sales Qty for bill detail and bill modifier live & q data
		// for Sales Qty for bill detail live data  
		sbSql.setLength(0);
		sbSql.append("select b.stritemcode,b.stritemname,sum(b.dblQuantity),sum(b.dblamount),b.dblRate"
			+ " ,e.strposname,b.dblDiscountAmt,g.strSubGroupName,h.strGroupName "
			+ " from tblbillhd a,tblbilldtl b, tblbillsettlementdtl c,tblsettelmenthd d,tblposmaster e"
			+ " ,tblitemmaster f,tblsubgrouphd g,tblgrouphd h "
			+ " where a.strBillNo=b.strBillNo and a.strBillNo=c.strBillNo and c.strSettlementCode=d.strSettelmentCode "
			+ " and a.strPOSCode=e.strPosCode and b.strItemCode=f.strItemCode and f.strSubGroupCode=g.strSubGroupCode "
			+ " and g.strGroupCode=h.strGroupCode and d.strSettelmentType!='Complementary' "
			+ " and date(a.dteBillDate) BETWEEN '" + fromDate + "' AND '" + toDate + "' ");
		sbSql.append(sbFilters);
		System.out.println(sbSql);

		ResultSet rsSales = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
		while (rsSales.next())
		{
		    clsItemWiseConsumption objItemWiseConsumption = null;
		    if (null != hmItemWiseConsumption.get(rsSales.getString(1)))
		    {
			objItemWiseConsumption = hmItemWiseConsumption.get(rsSales.getString(1));
			objItemWiseConsumption.setSaleQty(objItemWiseConsumption.getSaleQty() + rsSales.getDouble(3));
			objItemWiseConsumption.setSaleAmt(objItemWiseConsumption.getSaleAmt() + (rsSales.getDouble(4) - rsSales.getDouble(7)));
			objItemWiseConsumption.setSubTotal(objItemWiseConsumption.getSubTotal() + rsSales.getDouble(4));
		    }
		    else
		    {
			sqlNo++;
			objItemWiseConsumption = new clsItemWiseConsumption();
			objItemWiseConsumption.setItemCode(rsSales.getString(1));
			objItemWiseConsumption.setItemName(rsSales.getString(2));
			objItemWiseConsumption.setSubGroupName(rsSales.getString(8));
			objItemWiseConsumption.setGroupName(rsSales.getString(9));
			objItemWiseConsumption.setSaleQty(rsSales.getDouble(3));
			objItemWiseConsumption.setComplimentaryQty(0);
			objItemWiseConsumption.setNcQty(0);
			objItemWiseConsumption.setSubTotal(rsSales.getDouble(4));
			objItemWiseConsumption.setDiscAmt(rsSales.getDouble(7));
			objItemWiseConsumption.setSaleAmt(rsSales.getDouble(4) - rsSales.getDouble(7));
			objItemWiseConsumption.setPOSName(rsSales.getString(6));
			objItemWiseConsumption.setSeqNo(sqlNo);
		    }
		    if (null != objItemWiseConsumption)
		    {
			hmItemWiseConsumption.put(rsSales.getString(1), objItemWiseConsumption);
		    }

		    //for Sales Qty for bill modifier live data 
		    sbSqlMod.setLength(0);
		    sbSqlMod.append("select b.strItemCode,b.strModifierName,sum(b.dblQuantity),sum(b.dblamount),b.dblRate"
			    + " ,e.strposname,b.dblDiscAmt,g.strSubGroupName,h.strGroupName "
			    + " from tblbillhd a,tblbillmodifierdtl b, tblbillsettlementdtl c,tblsettelmenthd d,tblposmaster e"
			    + " ,tblitemmaster f,tblsubgrouphd g,tblgrouphd h "
			    + " where a.strBillNo=b.strBillNo and a.strBillNo=c.strBillNo and c.strSettlementCode=d.strSettelmentCode "
			    + " and a.strPOSCode=e.strPosCode and left(b.strItemCode,7)=f.strItemCode and f.strSubGroupCode=g.strSubGroupCode "
			    + " and g.strGroupCode=h.strGroupCode and d.strSettelmentType!='Complementary' "
			    + " and left(b.strItemCode,7)='" + rsSales.getString(1) + "' "
			    + " and date(a.dteBillDate) BETWEEN '" + fromDate + "' AND '" + toDate + "' ");
		    sbSqlMod.append(sbFilters);
		    System.out.println(sbSqlMod);

		    rsSalesMod = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlMod.toString());
		    while (rsSalesMod.next())
		    {
			// clsItemWiseConsumption objItemWiseConsumption=null;
			if (null != hmItemWiseConsumption.get(rsSalesMod.getString(1)))
			{
			    objItemWiseConsumption = hmItemWiseConsumption.get(rsSalesMod.getString(1));
			    objItemWiseConsumption.setSaleQty(objItemWiseConsumption.getSaleQty() + rsSalesMod.getDouble(3));
			    objItemWiseConsumption.setSaleAmt(objItemWiseConsumption.getSaleAmt() + (rsSalesMod.getDouble(4) - rsSalesMod.getDouble(7)));
			    objItemWiseConsumption.setSubTotal(objItemWiseConsumption.getSubTotal() + rsSalesMod.getDouble(4));
			}
			else
			{
			    sqlNo++;
			    objItemWiseConsumption = new clsItemWiseConsumption();
			    objItemWiseConsumption.setItemCode(rsSalesMod.getString(1));
			    objItemWiseConsumption.setItemName(rsSalesMod.getString(2));
			    objItemWiseConsumption.setSubGroupName(rsSalesMod.getString(8));
			    objItemWiseConsumption.setGroupName(rsSalesMod.getString(9));
			    objItemWiseConsumption.setSaleQty(rsSalesMod.getDouble(3));
			    objItemWiseConsumption.setComplimentaryQty(0);
			    objItemWiseConsumption.setNcQty(0);
			    objItemWiseConsumption.setSubTotal(rsSalesMod.getDouble(4));
			    objItemWiseConsumption.setDiscAmt(rsSalesMod.getDouble(7));
			    objItemWiseConsumption.setSaleAmt(rsSalesMod.getDouble(4) - rsSalesMod.getDouble(7));
			    objItemWiseConsumption.setPOSName(rsSalesMod.getString(6));
			    objItemWiseConsumption.setSeqNo(sqlNo);

			}
			if (null != objItemWiseConsumption)
			{
			    hmItemWiseConsumption.put(rsSalesMod.getString(1), objItemWiseConsumption);
			}

		    }
		    rsSalesMod.close();
		}
		rsSales.close();

		// for Sales Qty for bill detail q data 
		sbSql.setLength(0);
		sbSql.append("select b.stritemcode,b.stritemname,sum(b.dblQuantity),sum(b.dblamount),b.dblRate"
			+ " ,e.strposname,b.dblDiscountAmt,g.strSubGroupName,h.strGroupName "
			+ " from tblqbillhd a,tblqbilldtl b, tblqbillsettlementdtl c,tblsettelmenthd d,tblposmaster e "
			+ " ,tblitemmaster f,tblsubgrouphd g,tblgrouphd h "
			+ " where a.strBillNo=b.strBillNo and a.strBillNo=c.strBillNo and c.strSettlementCode=d.strSettelmentCode "
			+ " and a.strPOSCode=e.strPosCode and b.strItemCode=f.strItemCode and f.strSubGroupCode=g.strSubGroupCode "
			+ " and g.strGroupCode=h.strGroupCode and d.strSettelmentType!='Complementary' "
			+ " and date(a.dteBillDate) BETWEEN '" + fromDate + "' AND '" + toDate + "' ");
		sbSql.append(sbFilters);
		System.out.println(sbSql);

		rsSales = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
		while (rsSales.next())
		{
		    clsItemWiseConsumption objItemWiseConsumption = null;
		    if (null != hmItemWiseConsumption.get(rsSales.getString(1)))
		    {
			objItemWiseConsumption = hmItemWiseConsumption.get(rsSales.getString(1));
			objItemWiseConsumption.setComplimentaryQty(objItemWiseConsumption.getSaleQty() + rsSales.getDouble(3));
			objItemWiseConsumption.setSaleAmt(objItemWiseConsumption.getSaleAmt() + (rsSales.getDouble(4) - rsSales.getDouble(7)));
			objItemWiseConsumption.setSubTotal(objItemWiseConsumption.getSubTotal() + rsSales.getDouble(4));
		    }
		    else
		    {
			sqlNo++;
			objItemWiseConsumption = new clsItemWiseConsumption();
			objItemWiseConsumption.setItemCode(rsSales.getString(1));
			objItemWiseConsumption.setItemName(rsSales.getString(2));
			objItemWiseConsumption.setSubGroupName(rsSales.getString(8));
			objItemWiseConsumption.setGroupName(rsSales.getString(9));
			objItemWiseConsumption.setSaleQty(rsSales.getDouble(3));
			objItemWiseConsumption.setComplimentaryQty(0);
			objItemWiseConsumption.setNcQty(0);
			objItemWiseConsumption.setSubTotal(rsSales.getDouble(4));
			objItemWiseConsumption.setDiscAmt(rsSales.getDouble(7));
			objItemWiseConsumption.setSaleAmt(rsSales.getDouble(4) - rsSales.getDouble(7));
			objItemWiseConsumption.setPOSName(rsSales.getString(6));
			objItemWiseConsumption.setSeqNo(sqlNo);
		    }
		    if (null != objItemWiseConsumption)
		    {
			hmItemWiseConsumption.put(rsSales.getString(1), objItemWiseConsumption);
		    }

		    // Code for Sales Qty for modifier live & q data
		    sbSqlMod.setLength(0);
		    sbSqlMod.append("select b.strItemCode,b.strModifierName,sum(b.dblQuantity),sum(b.dblamount),b.dblRate"
			    + " ,e.strposname,b.dblDiscAmt,g.strSubGroupName,h.strGroupName "
			    + " from tblqbillhd a,tblqbillmodifierdtl b, tblqbillsettlementdtl c,tblsettelmenthd d,tblposmaster e "
			    + " ,tblitemmaster f,tblsubgrouphd g,tblgrouphd h "
			    + " where a.strBillNo=b.strBillNo and a.strBillNo=c.strBillNo and c.strSettlementCode=d.strSettelmentCode "
			    + " and a.strPOSCode=e.strPosCode and left(b.strItemCode,7)=f.strItemCode and f.strSubGroupCode=g.strSubGroupCode "
			    + " and g.strGroupCode=h.strGroupCode and d.strSettelmentType!='Complementary' "
			    + " and left(b.strItemCode,7)='" + rsSales.getString(1) + "' "
			    + " and date(a.dteBillDate) BETWEEN '" + fromDate + "' AND '" + toDate + "' ");
		    sbSqlMod.append(sbFilters);
		    System.out.println(sbSqlMod);

		    rsSalesMod = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlMod.toString());
		    while (rsSalesMod.next())
		    {

			if (null != hmItemWiseConsumption.get(rsSalesMod.getString(1)))
			{
			    objItemWiseConsumption = hmItemWiseConsumption.get(rsSalesMod.getString(1));
			    objItemWiseConsumption.setComplimentaryQty(objItemWiseConsumption.getSaleQty() + rsSalesMod.getDouble(3));
			    objItemWiseConsumption.setSaleAmt(objItemWiseConsumption.getSaleAmt() + (rsSalesMod.getDouble(4) - rsSalesMod.getDouble(7)));
			    objItemWiseConsumption.setSubTotal(objItemWiseConsumption.getSubTotal() + rsSalesMod.getDouble(4));
			}
			else
			{
			    sqlNo++;
			    objItemWiseConsumption = new clsItemWiseConsumption();
			    objItemWiseConsumption.setItemCode(rsSalesMod.getString(1));
			    objItemWiseConsumption.setItemName(rsSalesMod.getString(2));
			    objItemWiseConsumption.setSubGroupName(rsSalesMod.getString(8));
			    objItemWiseConsumption.setGroupName(rsSalesMod.getString(9));
			    objItemWiseConsumption.setSaleQty(rsSalesMod.getDouble(3));
			    objItemWiseConsumption.setComplimentaryQty(0);
			    objItemWiseConsumption.setNcQty(0);
			    objItemWiseConsumption.setSubTotal(rsSalesMod.getDouble(4));
			    objItemWiseConsumption.setDiscAmt(rsSalesMod.getDouble(7));
			    objItemWiseConsumption.setSaleAmt(rsSalesMod.getDouble(4) - rsSalesMod.getDouble(7));
			    objItemWiseConsumption.setPOSName(rsSalesMod.getString(6));
			    objItemWiseConsumption.setSeqNo(sqlNo);
			}
			if (null != objItemWiseConsumption)
			{
			    hmItemWiseConsumption.put(rsSalesMod.getString(1), objItemWiseConsumption);
			}
		    }
		    rsSalesMod.close();
		}
		rsSales.close();

		// Code for Complimentary Qty for live & q bill detail and bill modifier data   
		//for Complimentary Qty for live bill detail
		sbSql.setLength(0);
		sbSql.append("select b.stritemcode,b.stritemname,sum(b.dblQuantity),sum(b.dblamount),b.dblRate"
			+ " ,e.strposname,b.dblDiscountAmt,g.strSubGroupName,h.strGroupName "
			+ " from tblbillhd a,tblbilldtl b, tblbillsettlementdtl c,tblsettelmenthd d,tblposmaster e "
			+ " ,tblitemmaster f,tblsubgrouphd g,tblgrouphd h "
			+ " where a.strBillNo=b.strBillNo and a.strBillNo=c.strBillNo and c.strSettlementCode=d.strSettelmentCode "
			+ " and a.strPOSCode=e.strPosCode and b.strItemCode=f.strItemCode and f.strSubGroupCode=g.strSubGroupCode "
			+ " and g.strGroupCode=h.strGroupCode and d.strSettelmentType='Complementary' "
			+ " and date(a.dteBillDate) BETWEEN '" + fromDate + "' AND '" + toDate + "' ");
		sbSql.append(sbFilters);
		System.out.println(sbSql);

		ResultSet rsComplimentary = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
		while (rsComplimentary.next())
		{
		    clsItemWiseConsumption objItemWiseConsumption = null;
		    if (null != hmItemWiseConsumption.get(rsComplimentary.getString(1)))
		    {
			objItemWiseConsumption = hmItemWiseConsumption.get(rsComplimentary.getString(1));
			objItemWiseConsumption.setComplimentaryQty(objItemWiseConsumption.getComplimentaryQty() + rsComplimentary.getDouble(3));
			objItemWiseConsumption.setSaleAmt(objItemWiseConsumption.getSaleAmt() + (rsComplimentary.getDouble(4) - rsComplimentary.getDouble(7)));
			objItemWiseConsumption.setSubTotal(objItemWiseConsumption.getSubTotal() + rsComplimentary.getDouble(4));
			System.out.println("Old= " + rsComplimentary.getString(1) + objItemWiseConsumption.getComplimentaryQty());
		    }
		    else
		    {
			sqlNo++;
			objItemWiseConsumption = new clsItemWiseConsumption();
			objItemWiseConsumption.setItemCode(rsComplimentary.getString(1));
			objItemWiseConsumption.setItemName(rsComplimentary.getString(2));
			objItemWiseConsumption.setSubGroupName(rsComplimentary.getString(8));
			objItemWiseConsumption.setGroupName(rsComplimentary.getString(9));
			objItemWiseConsumption.setComplimentaryQty(rsComplimentary.getDouble(3));
			objItemWiseConsumption.setSaleQty(0);
			objItemWiseConsumption.setNcQty(0);
			objItemWiseConsumption.setSubTotal(rsComplimentary.getDouble(4));
			objItemWiseConsumption.setDiscAmt(rsComplimentary.getDouble(7));
			objItemWiseConsumption.setSaleAmt(rsComplimentary.getDouble(4) - rsComplimentary.getDouble(7));
			objItemWiseConsumption.setPOSName(rsComplimentary.getString(6));
			objItemWiseConsumption.setSeqNo(sqlNo);

			System.out.println("New= " + rsComplimentary.getString(1) + objItemWiseConsumption.getComplimentaryQty());
		    }
		    if (null != objItemWiseConsumption)
		    {
			hmItemWiseConsumption.put(rsComplimentary.getString(1), objItemWiseConsumption);
		    }

		    //for Complimentary Qty for live bill modifier
		    sbSqlMod.setLength(0);
		    sbSqlMod.append("select b.strItemCode,b.strModifierName,sum(b.dblQuantity),sum(b.dblamount),b.dblRate"
			    + " ,e.strposname,b.dblDiscAmt,g.strSubGroupName,h.strGroupName "
			    + " from tblbillhd a,tblbillmodifierdtl b, tblbillsettlementdtl c,tblsettelmenthd d,tblposmaster e "
			    + " ,tblitemmaster f,tblsubgrouphd g,tblgrouphd h "
			    + " where a.strBillNo=b.strBillNo and a.strBillNo=c.strBillNo and c.strSettlementCode=d.strSettelmentCode "
			    + " and a.strPOSCode=e.strPosCode and left(b.strItemCode,7)=f.strItemCode and f.strSubGroupCode=g.strSubGroupCode "
			    + " and g.strGroupCode=h.strGroupCode and d.strSettelmentType='Complementary' "
			    + " and left(b.strItemCode,7)='" + rsComplimentary.getString(1) + "' "
			    + " and date(a.dteBillDate) BETWEEN '" + fromDate + "' AND '" + toDate + "' ");
		    sbSqlMod.append(sbFilters);
		    System.out.println(sbSqlMod);

		    ResultSet rsModComplimentary = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlMod.toString());
		    while (rsModComplimentary.next())
		    {
			if (null != hmItemWiseConsumption.get(rsModComplimentary.getString(1)))
			{
			    objItemWiseConsumption = hmItemWiseConsumption.get(rsModComplimentary.getString(1));
			    objItemWiseConsumption.setComplimentaryQty(objItemWiseConsumption.getComplimentaryQty() + rsModComplimentary.getDouble(3));
			    objItemWiseConsumption.setSaleAmt(objItemWiseConsumption.getSaleAmt() + (rsModComplimentary.getDouble(4) - rsModComplimentary.getDouble(7)));
			    objItemWiseConsumption.setSubTotal(objItemWiseConsumption.getSubTotal() + rsModComplimentary.getDouble(4));
			    System.out.println("Old= " + rsModComplimentary.getString(1) + objItemWiseConsumption.getComplimentaryQty());
			}
			else
			{
			    sqlNo++;
			    objItemWiseConsumption = new clsItemWiseConsumption();
			    objItemWiseConsumption.setItemCode(rsModComplimentary.getString(1));
			    objItemWiseConsumption.setItemName(rsModComplimentary.getString(2));
			    objItemWiseConsumption.setSubGroupName(rsModComplimentary.getString(8));
			    objItemWiseConsumption.setGroupName(rsModComplimentary.getString(9));
			    objItemWiseConsumption.setComplimentaryQty(rsModComplimentary.getDouble(3));
			    objItemWiseConsumption.setSaleQty(0);
			    objItemWiseConsumption.setNcQty(0);
			    objItemWiseConsumption.setSubTotal(rsModComplimentary.getDouble(4));
			    objItemWiseConsumption.setDiscAmt(rsModComplimentary.getDouble(7));
			    objItemWiseConsumption.setSaleAmt(rsModComplimentary.getDouble(4) - rsModComplimentary.getDouble(7));
			    objItemWiseConsumption.setPOSName(rsModComplimentary.getString(6));
			    objItemWiseConsumption.setSeqNo(sqlNo);
			    System.out.println("New= " + rsModComplimentary.getString(1) + objItemWiseConsumption.getComplimentaryQty());
			}
			if (null != objItemWiseConsumption)
			{
			    hmItemWiseConsumption.put(rsModComplimentary.getString(1), objItemWiseConsumption);
			}
		    }
		    rsModComplimentary.close();
		}
		rsComplimentary.close();

		//for Complimentary Qty for q bill details
		sbSql.setLength(0);
		sbSql.append("select b.stritemcode,b.stritemname,sum(b.dblQuantity),sum(b.dblamount),b.dblRate"
			+ " ,e.strposname,b.dblDiscountAmt,g.strSubGroupName,h.strGroupName "
			+ " from tblqbillhd a,tblqbilldtl b, tblqbillsettlementdtl c,tblsettelmenthd d,tblposmaster e "
			+ " ,tblitemmaster f,tblsubgrouphd g,tblgrouphd h "
			+ " where a.strBillNo=b.strBillNo and a.strBillNo=c.strBillNo and c.strSettlementCode=d.strSettelmentCode "
			+ " and a.strPOSCode=e.strPosCode and b.strItemCode=f.strItemCode and f.strSubGroupCode=g.strSubGroupCode "
			+ " and g.strGroupCode=h.strGroupCode and d.strSettelmentType='Complementary' "
			+ " and date(a.dteBillDate) BETWEEN '" + fromDate + "' AND '" + toDate + "' ");
		sbSql.append(sbFilters);
		System.out.println(sbSql);

		rsComplimentary = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
		while (rsComplimentary.next())
		{
		    clsItemWiseConsumption objItemWiseConsumption = null;
		    if (null != hmItemWiseConsumption.get(rsComplimentary.getString(1)))
		    {
			objItemWiseConsumption = hmItemWiseConsumption.get(rsComplimentary.getString(1));
			objItemWiseConsumption.setComplimentaryQty(objItemWiseConsumption.getComplimentaryQty() + rsComplimentary.getDouble(3));
			objItemWiseConsumption.setSaleAmt(objItemWiseConsumption.getSaleAmt() + (rsComplimentary.getDouble(4) - rsComplimentary.getDouble(7)));
			objItemWiseConsumption.setSubTotal(objItemWiseConsumption.getSubTotal() + rsComplimentary.getDouble(4));
		    }
		    else
		    {
			sqlNo++;
			objItemWiseConsumption = new clsItemWiseConsumption();
			objItemWiseConsumption.setItemCode(rsComplimentary.getString(1));
			objItemWiseConsumption.setItemName(rsComplimentary.getString(2));
			objItemWiseConsumption.setSubGroupName(rsComplimentary.getString(8));
			objItemWiseConsumption.setGroupName(rsComplimentary.getString(9));
			objItemWiseConsumption.setComplimentaryQty(rsComplimentary.getDouble(3));
			objItemWiseConsumption.setSaleQty(0);
			objItemWiseConsumption.setNcQty(0);
			objItemWiseConsumption.setSubTotal(rsComplimentary.getDouble(4));
			objItemWiseConsumption.setDiscAmt(rsComplimentary.getDouble(7));
			objItemWiseConsumption.setSaleAmt(rsComplimentary.getDouble(4) - rsComplimentary.getDouble(7));
			objItemWiseConsumption.setPOSName(rsComplimentary.getString(6));
			objItemWiseConsumption.setSeqNo(sqlNo);
		    }
		    if (null != objItemWiseConsumption)
		    {
			hmItemWiseConsumption.put(rsComplimentary.getString(1), objItemWiseConsumption);
		    }

		    //for Complimentary Qty for q bill modifier 
		    sbSqlMod.setLength(0);
		    sbSqlMod.append("select b.strItemCode,b.strModifierName,sum(b.dblQuantity),sum(b.dblamount),b.dblRate"
			    + " ,e.strposname,b.dblDiscAmt,g.strSubGroupName,h.strGroupName "
			    + " from tblqbillhd a,tblqbillmodifierdtl b, tblqbillsettlementdtl c,tblsettelmenthd d,tblposmaster e "
			    + " ,tblitemmaster f,tblsubgrouphd g,tblgrouphd h "
			    + " where a.strBillNo=b.strBillNo and a.strBillNo=c.strBillNo and c.strSettlementCode=d.strSettelmentCode "
			    + " and a.strPOSCode=e.strPosCode and left(b.strItemCode,7)=f.strItemCode and f.strSubGroupCode=g.strSubGroupCode "
			    + " and g.strGroupCode=h.strGroupCode and d.strSettelmentType='Complementary' "
			    + " and left(b.strItemCode,7)='" + rsComplimentary.getString(1) + "'"
			    + " and date(a.dteBillDate) BETWEEN '" + fromDate + "' AND '" + toDate + "' ");
		    sbSqlMod.append(sbFilters);
		    System.out.println(sbSqlMod);

		    ResultSet rsModComplimentary = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlMod.toString());
		    while (rsModComplimentary.next())
		    {

			if (null != hmItemWiseConsumption.get(rsModComplimentary.getString(1)))
			{
			    objItemWiseConsumption = hmItemWiseConsumption.get(rsModComplimentary.getString(1));
			    objItemWiseConsumption.setComplimentaryQty(objItemWiseConsumption.getComplimentaryQty() + rsModComplimentary.getDouble(3));
			    objItemWiseConsumption.setSaleAmt(objItemWiseConsumption.getSaleAmt() + (rsModComplimentary.getDouble(4) - rsModComplimentary.getDouble(7)));
			    objItemWiseConsumption.setSubTotal(objItemWiseConsumption.getSubTotal() + rsModComplimentary.getDouble(4));
			}
			else
			{
			    sqlNo++;
			    objItemWiseConsumption = new clsItemWiseConsumption();
			    objItemWiseConsumption.setItemCode(rsModComplimentary.getString(1));
			    objItemWiseConsumption.setItemName(rsModComplimentary.getString(2));
			    objItemWiseConsumption.setSubGroupName(rsModComplimentary.getString(8));
			    objItemWiseConsumption.setGroupName(rsModComplimentary.getString(9));
			    objItemWiseConsumption.setComplimentaryQty(rsModComplimentary.getDouble(3));
			    objItemWiseConsumption.setSaleQty(0);
			    objItemWiseConsumption.setNcQty(0);
			    objItemWiseConsumption.setSubTotal(rsModComplimentary.getDouble(4));
			    objItemWiseConsumption.setDiscAmt(rsModComplimentary.getDouble(7));
			    objItemWiseConsumption.setSaleAmt(rsModComplimentary.getDouble(4) - rsModComplimentary.getDouble(7));
			    objItemWiseConsumption.setPOSName(rsModComplimentary.getString(6));
			    objItemWiseConsumption.setSeqNo(sqlNo);
			}
			if (null != objItemWiseConsumption)
			{
			    hmItemWiseConsumption.put(rsModComplimentary.getString(1), objItemWiseConsumption);
			}
		    }
		    rsModComplimentary.close();

		}
		rsComplimentary.close();

		// Code for NC Qty    
		sbSql.setLength(0);
		sbSql.append("select a.stritemcode,b.stritemname,sum(a.dblQuantity),sum(a.dblQuantity*a.dblRate)"
			+ ",a.dblRate, c.strposname,0 as DiscAmt,d.strSubGroupName,e.strGroupName "
			+ " from tblnonchargablekot a, tblitemmaster b, tblposmaster c,tblsubgrouphd d,tblgrouphd e "
			+ " where left(a.strItemCode,7)=b.strItemCode and a.strPOSCode=c.strPosCode and b.strSubGroupCode=d.strSubGroupCode "
			+ " and d.strGroupCode=e.strGroupCode "
			+ " and date(a.dteNCKOTDate) BETWEEN '" + fromDate + "' AND '" + toDate + "' ");
		if (!pos.equals("All"))
		{
		    sbSql.append(" AND a.strPOSCode = '" + pos + "' ");
		}
		sbSql.append(" group by a.strItemCode,c.strPOSName ");
		System.out.println(sbSql);

		ResultSet rsNCKOT = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
		while (rsNCKOT.next())
		{
		    clsItemWiseConsumption objItemWiseConsumption = null;
		    if (null != hmItemWiseConsumption.get(rsNCKOT.getString(1)))
		    {
			objItemWiseConsumption = hmItemWiseConsumption.get(rsNCKOT.getString(1));
			objItemWiseConsumption.setNcQty(objItemWiseConsumption.getNcQty() + rsNCKOT.getDouble(3));
			objItemWiseConsumption.setSaleAmt(objItemWiseConsumption.getSaleAmt() + (rsNCKOT.getDouble(4) - rsNCKOT.getDouble(7)));
			objItemWiseConsumption.setSubTotal(objItemWiseConsumption.getSubTotal() + rsNCKOT.getDouble(4));
		    }
		    else
		    {
			sqlNo++;
			objItemWiseConsumption = new clsItemWiseConsumption();
			objItemWiseConsumption.setItemCode(rsNCKOT.getString(1));
			objItemWiseConsumption.setItemName(rsNCKOT.getString(2));
			objItemWiseConsumption.setSubGroupName(rsNCKOT.getString(8));
			objItemWiseConsumption.setGroupName(rsNCKOT.getString(9));
			objItemWiseConsumption.setNcQty(rsNCKOT.getDouble(3));
			objItemWiseConsumption.setSaleQty(0);
			objItemWiseConsumption.setComplimentaryQty(0);
			objItemWiseConsumption.setSubTotal(rsNCKOT.getDouble(4));
			objItemWiseConsumption.setDiscAmt(rsNCKOT.getDouble(7));
			objItemWiseConsumption.setSaleAmt(rsNCKOT.getDouble(4) - rsNCKOT.getDouble(7));
			objItemWiseConsumption.setPOSName(rsNCKOT.getString(6));
			objItemWiseConsumption.setSeqNo(sqlNo);
		    }
		    if (null != objItemWiseConsumption)
		    {
			hmItemWiseConsumption.put(rsNCKOT.getString(1), objItemWiseConsumption);
		    }
		}
		rsNCKOT.close();

		dm = new DefaultTableModel()
		{
		    @Override
		    public boolean isCellEditable(int row, int column)
		    {
			//all cells false
			return false;
		    }
		};

		dm.addColumn("Group");
		dm.addColumn("Sub Group");
		dm.addColumn("Item Name");
		dm.addColumn("POS");
		dm.addColumn("Sale Qty");
		dm.addColumn("Complimentary Qty");
		dm.addColumn("NC Qty");
		dm.addColumn("SubTotal");
		dm.addColumn("Sales Amount");

		vSalesReportExcelColLength = new java.util.Vector();
		vSalesReportExcelColLength.add("10#Left"); //Group
		vSalesReportExcelColLength.add("10#Left"); //Sub Group
		vSalesReportExcelColLength.add("10#Left"); //Item Name
		vSalesReportExcelColLength.add("9#Left"); //POS
		vSalesReportExcelColLength.add("4#Right"); //Sale Qty
		vSalesReportExcelColLength.add("4#Right"); //Complimentary Qty
		vSalesReportExcelColLength.add("4#Right"); //NC Qty
		vSalesReportExcelColLength.add("7#Right"); //Sub Total
		vSalesReportExcelColLength.add("7#Right"); //Sales Amt

		DefaultTableModel dm1 = new DefaultTableModel()
		{
		    @Override
		    public boolean isCellEditable(int row, int column)
		    {
			//all cells false
			return false;
		    }
		};
		dm1.addColumn("Totals");
		dm1.addColumn("");
		dm1.addColumn("");
		dm1.addColumn("");
		dm1.addColumn("Sale Qty");
		dm1.addColumn("Compl Qty");
		dm1.addColumn("NC Qty");
		dm1.addColumn("SubTotal");
		dm1.addColumn("Sales Amount");

		double totalSaleQty = 0, totalComplimentaryQty = 0, totalNCQty = 0;
		double totalSaleAmt = 0, totalSubTotal = 0;

		List<clsItemWiseConsumption> list = new ArrayList<clsItemWiseConsumption>();
		for (Map.Entry<String, clsItemWiseConsumption> entry : hmItemWiseConsumption.entrySet())
		{
		    list.add(entry.getValue());
		}

		//sort list 
		Collections.sort(list, clsItemWiseConsumption.comparatorItemConsumptionColumnDtl);

		for (clsItemWiseConsumption objItemComp : list)
		{
		    Object[] arrObjRow =
		    {
			objItemComp.getGroupName(), objItemComp.getSubGroupName(), objItemComp.getItemName(), objItemComp.getPOSName(), objItemComp.getSaleQty(), objItemComp.getComplimentaryQty(), objItemComp.getNcQty(), objItemComp.getSubTotal(), objItemComp.getSaleAmt()
		    };
		    dm.addRow(arrObjRow);
		    totalSaleQty += objItemComp.getSaleQty();
		    totalComplimentaryQty += objItemComp.getComplimentaryQty();
		    totalNCQty += objItemComp.getNcQty();
		    totalSaleAmt += objItemComp.getSaleAmt();
		    totalSubTotal += objItemComp.getSubTotal();
		}

		/*
                 * for (Map.Entry<String, clsItemWiseConsumption> entry :
                 * hmItemWiseConsumption.entrySet()) {
                 * if(null!=entry.getValue()) {
                 * if(!entry.getValue().getItemName().isEmpty()) { Object[]
                 * arrObjRow={entry.getValue().getGroupName(),entry.getValue().getSubGroupName(),entry.getValue().getItemName()
                 * ,entry.getValue().getPOSName(),entry.getValue().getSaleQty(),entry.getValue().getComplimentaryQty()
                 * ,entry.getValue().getNcQty(),entry.getValue().getSubTotal(),entry.getValue().getSaleAmt()};
                 * dm.addRow(arrObjRow);
                 * totalSaleQty+=entry.getValue().getSaleQty();
                 * totalComplimentaryQty+=entry.getValue().getComplimentaryQty();
                 * totalNCQty+=entry.getValue().getNcQty();
                 * totalSaleAmt+=entry.getValue().getSaleAmt();
                 * totalSubTotal+=entry.getValue().getSubTotal(); } }
                 }
		 */
		Object[] arrObjTotalRow =
		{
		    "Totals", "", "", "", totalSaleQty, totalComplimentaryQty, totalNCQty, totalSubTotal, totalSaleAmt
		};
		dm1.addRow(arrObjTotalRow);

		tblSales.setModel(dm);
		tblTotal.setModel(dm1);

		tblSales.setSize(400, 400);
		tblSales.setRowHeight(25);
		tblTotal.setRowHeight(40);

		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

		tblSales.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		tblSales.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
		tblSales.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
		tblSales.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
		tblSales.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);
		tblSales.getColumnModel().getColumn(8).setCellRenderer(rightRenderer);

		tblSales.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		tblSales.getColumnModel().getColumn(0).setPreferredWidth(60);
		tblSales.getColumnModel().getColumn(1).setPreferredWidth(60);
		tblSales.getColumnModel().getColumn(2).setPreferredWidth(60);
		tblSales.getColumnModel().getColumn(3).setPreferredWidth(60);
		tblSales.getColumnModel().getColumn(4).setPreferredWidth(20);
		tblSales.getColumnModel().getColumn(5).setPreferredWidth(20);
		tblSales.getColumnModel().getColumn(6).setPreferredWidth(20);
		tblSales.getColumnModel().getColumn(7).setPreferredWidth(30);
		tblSales.getColumnModel().getColumn(8).setPreferredWidth(30);

		tblTotal.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		tblTotal.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
		tblTotal.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
		tblTotal.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
		tblTotal.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);
		tblTotal.getColumnModel().getColumn(8).setCellRenderer(rightRenderer);

		tblTotal.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		tblTotal.getColumnModel().getColumn(0).setPreferredWidth(60);
		tblTotal.getColumnModel().getColumn(1).setPreferredWidth(60);
		tblTotal.getColumnModel().getColumn(2).setPreferredWidth(60);
		tblTotal.getColumnModel().getColumn(3).setPreferredWidth(60);
		tblTotal.getColumnModel().getColumn(4).setPreferredWidth(20);
		tblTotal.getColumnModel().getColumn(5).setPreferredWidth(20);
		tblTotal.getColumnModel().getColumn(6).setPreferredWidth(20);
		tblTotal.getColumnModel().getColumn(7).setPreferredWidth(30);
		tblTotal.getColumnModel().getColumn(8).setPreferredWidth(30);

	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    /**
     * Tax Wise Sales Report
     */
    private void funTaxWiseSales()
    {
	StringBuilder sbSqlLive = new StringBuilder();
	StringBuilder sbSqlQFile = new StringBuilder();
	StringBuilder sbSqlFilters = new StringBuilder();
	try
	{
	    reportName = "Tax Wise Sales Report";
	    String prevBillNo = "";
	    double totalTax = 0, totalTaxableAmt = 0;
	    totalQty = new Double("0.00");
	    totalAmount = new BigDecimal("0.00");
	    temp = new BigDecimal("0.00");
	    temp1 = new BigDecimal("0.00");
	    dm = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    return false;
		}
	    };
	    dm.addColumn("BillNo");
	    dm.addColumn("BillDate");
	    dm.addColumn("Tax Code");
	    dm.addColumn("Tax Name");
	    dm.addColumn("Tax Percent");
	    dm.addColumn("Taxable Amount");
	    dm.addColumn("Tax Amount");

	    vSalesReportExcelColLength = new java.util.Vector();
	    vSalesReportExcelColLength.add("6#Left"); //billNo
	    vSalesReportExcelColLength.add("15#Left"); //date
	    vSalesReportExcelColLength.add("6#Right"); //taxcode
	    vSalesReportExcelColLength.add("6#Right"); //taxname
	    vSalesReportExcelColLength.add("6#Right"); //tax%
	    vSalesReportExcelColLength.add("6#Right"); //taxable amount
	    vSalesReportExcelColLength.add("6#Right"); //tax amount

	    DefaultTableModel dm1 = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    return false;
		}
	    };

	    dm1.addColumn("");
	    dm1.addColumn("TotalTaxable");
	    dm1.addColumn("TotalTax");
	    Date dt1 = dteFromDate.getDate();
	    Date dt2 = dteToDate.getDate();

	    if ((dt2.getTime() - dt1.getTime()) < 0)
	    {
		new frmOkPopUp(null, "Invalid date", "Error", 1).setVisible(true);
	    }
	    else
	    {
		fromDate = funGetFromDate();
		toDate = funGetToDate();
		records = new Object[7];
		String pos = funGetSelectedPosCode();

		sbSqlLive.setLength(0);
		sbSqlQFile.setLength(0);

		sbSqlLive.append("select a.strBillNo,date(a.dteBillDate),c.strTaxCode"
			+ " ,c.strTaxDesc,b.dblTaxableAmount,b.dblTaxAmount,c.dblPercent"
			+ " ,'" + clsGlobalVarClass.gUserCode + "' "
			+ " from tblbillhd a,tblbilltaxdtl b,tbltaxhd c "
			+ " where a.strBillNo=b.strBillNo  "
			+ " and date(a.dteBillDate)=date(b.dteBillDate) "
			+ " and b.strTaxCode=c.strTaxCode "
			+ " and a.strClientCode=b.strClientCode "
			+ " and b.strClientCode=c.strClientCode "
			+ " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' ");

		sbSqlQFile.append("select a.strBillNo,date(a.dteBillDate),c.strTaxCode"
			+ " ,c.strTaxDesc,b.dblTaxableAmount,b.dblTaxAmount,c.dblPercent "
			+ " ,'" + clsGlobalVarClass.gUserCode + "' "
			+ " from tblqbillhd a,tblqbilltaxdtl b,tbltaxhd c "
			+ " where a.strBillNo=b.strBillNo  "
			+ " and date(a.dteBillDate)=date(b.dteBillDate) "
			+ " and b.strTaxCode=c.strTaxCode "
			+ " and a.strClientCode=b.strClientCode "
			+ " and b.strClientCode=c.strClientCode "
			+ " and date(a.dteBillDate) between '" + fromDate + "' and '" + toDate + "' ");

		if (!pos.equals("All"))
		{
		    sbSqlLive.append(" and " + objUtility.funGetSelectedPOSCodeString("a.strPOSCode", selectedPOSCodeSet) + " ");
		    sbSqlQFile.append(" and " + objUtility.funGetSelectedPOSCodeString("a.strPOSCode", selectedPOSCodeSet) + " ");
		}
		if (clsGlobalVarClass.gEnableShiftYN && (!cmbShift.getSelectedItem().toString().equalsIgnoreCase("All")))
		{
		    sbSqlLive.append(" AND a.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ");
		    sbSqlQFile.append(" AND a.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ");
		}
		if (!cmbArea.getSelectedItem().equals("All"))
		{
		    sbSqlLive.append(" and a.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ");
		    sbSqlQFile.append(" and a.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ");
		}
		if (!cmbOperationType.getSelectedItem().toString().equalsIgnoreCase("All"))
		{
		    sbSqlLive.append(" and a.strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ");
		    sbSqlQFile.append(" and a.strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ");
		}
		if (!txtCustomerCode.getText().equalsIgnoreCase(""))
		{
		    sbSqlLive.append(" and a.strCustomerCode='" + txtCustomerCode.getText() + "' ");
		    sbSqlQFile.append(" and a.strCustomerCode='" + txtCustomerCode.getText() + "' ");
		}
		sbSqlLive.append(" order by a.strBillNo desc");
		sbSqlQFile.append(" order by a.strBillNo desc");

		sbSqlLive.append(sbSqlFilters);
		sbSqlQFile.append(sbSqlFilters);
		//System.out.println(sbSqlLive);
		//System.out.println(sbSqlQFile);
		boolean flgRecords = false;
		if (!flgRecords)
		{
		    dm.setRowCount(0);
		    dm1.setRowCount(0);
		}

		ResultSet saleSet = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlLive.toString());
		while (saleSet.next())
		{
		    flgRecords = true;
		    records[0] = saleSet.getString(1);
		    records[1] = saleSet.getString(2);
		    records[2] = saleSet.getString(3);
		    records[3] = saleSet.getString(4);
		    records[4] = gDecimalFormat.format(Double.parseDouble(saleSet.getString(7)));
		    records[5] = gDecimalFormat.format(Double.parseDouble(saleSet.getString(5)));
		    records[6] = gDecimalFormat.format(Double.parseDouble(saleSet.getString(6)));
		    totalTax = totalTax + Double.parseDouble(records[6].toString());
		    if (!prevBillNo.equals(records[0].toString()))
		    {
			totalTaxableAmt = totalTaxableAmt + Double.parseDouble(records[5].toString());
		    }
		    prevBillNo = records[0].toString();
		    dm.addRow(records);
		}
		saleSet.close();
		//for day end
		saleSet = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlQFile.toString());
		while (saleSet.next())
		{
		    flgRecords = true;
		    records[0] = saleSet.getString(1);
		    records[1] = saleSet.getString(2);
		    records[2] = saleSet.getString(3);
		    records[3] = saleSet.getString(4);
		    records[4] = gDecimalFormat.format(Double.parseDouble(saleSet.getString(7)));
		    records[5] = gDecimalFormat.format(Double.parseDouble(saleSet.getString(5)));
		    records[6] = gDecimalFormat.format(Double.parseDouble(saleSet.getString(6)));
		    totalTax = totalTax + Double.parseDouble(records[6].toString());
		    if (!prevBillNo.equals(records[0].toString()))
		    {
			totalTaxableAmt = totalTaxableAmt + Double.parseDouble(records[5].toString());
		    }
		    prevBillNo = records[0].toString();
		    dm.addRow(records);
		}
		saleSet.close();

		Object[] ob1
			=
			{
			    "Total", gDecimalFormat.format(totalTaxableAmt), gDecimalFormat.format(totalTax)
			};
		dm1.addRow(ob1);

		tblTotal.setModel(dm1);
		tblSales.setModel(dm);
		tblSales.setSize(400, 400);
		tblSales.setRowHeight(25);
		tblTotal.setRowHeight(40);
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

		tblSales.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
		tblSales.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
		tblSales.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
		tblSales.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblSales.getColumnModel().getColumn(0).setPreferredWidth(100);
		tblSales.getColumnModel().getColumn(1).setPreferredWidth(100);
		tblSales.getColumnModel().getColumn(2).setPreferredWidth(100);
		tblSales.getColumnModel().getColumn(3).setPreferredWidth(200);
		tblSales.getColumnModel().getColumn(4).setPreferredWidth(82);
		tblSales.getColumnModel().getColumn(5).setPreferredWidth(100);
		tblSales.getColumnModel().getColumn(6).setPreferredWidth(100);

		tblTotal.setSize(400, 400);
		DefaultTableCellRenderer rightRenderer1 = new DefaultTableCellRenderer();
		rightRenderer1.setHorizontalAlignment(JLabel.RIGHT);
		tblTotal.getColumnModel().getColumn(1).setCellRenderer(rightRenderer1);
		tblTotal.getColumnModel().getColumn(2).setCellRenderer(rightRenderer1);
		tblTotal.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblTotal.getColumnModel().getColumn(0).setPreferredWidth(590);
		tblTotal.getColumnModel().getColumn(1).setPreferredWidth(100);
		tblTotal.getColumnModel().getColumn(2).setPreferredWidth(100);
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    /**
     * Menu Wise With Modifier Report
     */
    private void funMenuWiseWithModifier()
    {
	StringBuilder sbSql = new StringBuilder();
	try
	{
	    reportName = "Menu_With_Modifier Wise Sales Report";
	    totalQty = new Double("0.00");
	    totalAmount = new BigDecimal("0.00");
	    temp = new BigDecimal("0.00");
	    temp1 = new BigDecimal("0.00");
	    dm = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    return false;
		}
	    };

	    dm.addColumn("Menu Name");
	    dm.addColumn("POS ");
	    dm.addColumn("Quantity");
	    dm.addColumn("Sales Amount");

	    vSalesReportExcelColLength = new java.util.Vector();
	    vSalesReportExcelColLength.add("6#Left");
	    vSalesReportExcelColLength.add("6#Left");
	    vSalesReportExcelColLength.add("6#Left");
	    vSalesReportExcelColLength.add("10#Left");

	    DefaultTableModel dm1 = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    return false;
		}
	    };

	    dm1.addColumn("");
	    dm1.addColumn("");
	    dm1.addColumn("Quantity");
	    dm1.addColumn("Sales Amount");
	    Date dt1 = dteFromDate.getDate();
	    Date dt2 = dteToDate.getDate();

	    if ((dt2.getTime() - dt1.getTime()) < 0)
	    {
		new frmOkPopUp(null, "Invalid date", "Error", 1).setVisible(true);
	    }
	    else
	    {
		DecimalFormat df = new DecimalFormat("#.00");
		DecimalFormat dfForNoDecimal = new DecimalFormat("0");
		fromDate = funGetFromDate();
		toDate = funGetToDate();
		records = new Object[5];

		sbSql.setLength(0);
		sbSql.append("select count(*) from vqbillhd where date(dteBillDate) between '" + fromDate + "' and '" + toDate + "'   ");
		if (clsGlobalVarClass.gEnableShiftYN && (!cmbShift.getSelectedItem().toString().equalsIgnoreCase("All")))
		{
		    sbSql.append(" AND intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ");
		}
		if (!cmbArea.getSelectedItem().equals("All"))
		{
		    sbSql.append(" and strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ");
		}
		if (!cmbOperationType.getSelectedItem().toString().equalsIgnoreCase("All"))
		{
		    sbSql.append(" and strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ");
		}
		ResultSet saleSet = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
		saleSet.next();
		int cnt = saleSet.getInt(1);
		saleSet.close();
		String pos = funGetSelectedPosCode();

		if (cnt > 0)
		{
		    sbSql.setLength(0);
		    sbSql.append("select d.strMenuName,e.strPosName,sum(b.dblQuantity),sum(b.dblAmount)-sum(b.dblDiscountAmt),sum(b.dblAmount),d.strMenuCode "
			    + " from tblbillhd a,tblbilldtl b,tblmenuitempricingdtl c,tblmenuhd d,tblposmaster e "
			    + " where a.strBillNo=b.strBillNo "
			    + " and date(a.dteBillDate)=date(b.dteBillDate) "
			    + " and b.strItemCode=c.strItemCode "
			    + " and c.strMenuCode=d.strMenuCode "
			    + " and a.strPOSCode=e.strPosCode "
			    + " and a.strPOSCode=c.strPosCode ");
		    if (clsGlobalVarClass.gAreaWisePricing.equals("Y"))
		    {
			sbSql.append(" and a.strAreaCode=c.strAreaCode ");
		    }
		    if (!pos.equals("All") && !cmbOperator.getSelectedItem().equals("All"))
		    {
			sbSql.append(" AND " + objUtility.funGetSelectedPOSCodeString("a.strPOSCode", selectedPOSCodeSet) + " and a.strUserCreated='" + cmbOperator.getSelectedItem().toString() + "'");
		    }
		    else if (!pos.equals("All") && cmbOperator.getSelectedItem().equals("All"))
		    {
			sbSql.append(" AND " + objUtility.funGetSelectedPOSCodeString("a.strPOSCode", selectedPOSCodeSet) + " ");
		    }
		    else if (pos.equals("All") && !cmbOperator.getSelectedItem().equals("All"))
		    {
			sbSql.append(" and a.strUserCreated='" + cmbOperator.getSelectedItem().toString() + "'");
		    }
		    if (clsGlobalVarClass.gEnableShiftYN && (!cmbShift.getSelectedItem().toString().equalsIgnoreCase("All")))
		    {
			sbSql.append(" AND a.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ");
		    }
		    if (!cmbArea.getSelectedItem().equals("All"))
		    {
			sbSql.append(" and a.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ");
		    }
		    if (!cmbOperationType.getSelectedItem().toString().equalsIgnoreCase("All"))
		    {
			sbSql.append(" and a.strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ");
		    }
		    if (!txtCustomerCode.getText().equalsIgnoreCase(""))
		    {
			sbSql.append(" and a.strCustomerCode='" + txtCustomerCode.getText() + "' ");
		    }
		    if (txtBillNofrom.getText().trim().length() == 0 && txtBillnoTo.getText().trim().length() == 0)
		    {
			sbSql.append(" and date( a.dteBillDate ) BETWEEN '" + fromDate + "' AND '" + toDate + "'"
				+ " group by c.strMenuCode,a.strPOSCode ");
		    }
		    else
		    {
			sbSql.append(" and date( a.dteBillDate ) BETWEEN '" + fromDate + "' AND '" + toDate + "' AND a.strBillNo between '" + txtBillNofrom.getText() + "' and '" + txtBillnoTo.getText() + "'"
				+ " group by c.strMenuCode,a.strPOSCode ");
		    }
		    saleSet = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
		    //System.out.println(sbSql.toString());

		    while (saleSet.next())
		    {
			StringBuilder sbSqlMod = new StringBuilder();
			sbSqlMod.setLength(0);
			sbSqlMod.append(" select d.strMenuName,e.strPosName,sum(b.dblQuantity) "
				+ " ,sum(b.dblAmount)-sum(b.dblDiscAmt),sum(b.dblAmount) "
				+ " from tblbillhd a,tblbillmodifierdtl b,tblmenuitempricingdtl c,tblmenuhd d,tblposmaster e "
				+ " where a.strBillNo=b.strBillNo "
				+ " and date(a.dteBillDate)=date(b.dteBillDate) "
				+ " and left(b.strItemCode,7)=c.strItemCode "
				+ " and c.strMenuCode=d.strMenuCode "
				+ " and a.strPOSCode=e.strPosCode "
				+ " and a.strPOSCode=c.strPosCode  "
				+ " and a.strAreaCode=c.strAreaCode "
				+ " and b.dblAmount>0 and c.strMenuCode='" + saleSet.getString(6) + "' "
				+ " and date(a.dteBillDate) BETWEEN '" + fromDate + "' and '" + toDate + "' "
			);
			if (clsGlobalVarClass.gEnableShiftYN && (!cmbShift.getSelectedItem().toString().equalsIgnoreCase("All")))
			{
			    sbSqlMod.append(" AND a.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ");
			}
			if (!cmbArea.getSelectedItem().equals("All"))
			{
			    sbSqlMod.append(" and a.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ");
			}
			if (!cmbOperationType.getSelectedItem().toString().equalsIgnoreCase("All"))
			{
			    sbSqlMod.append(" and a.strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ");
			}
			if (!txtCustomerCode.getText().equalsIgnoreCase(""))
			{
			    sbSqlMod.append(" and a.strCustomerCode='" + txtCustomerCode.getText() + "' ");
			}
			//System.out.println(sbSqlMod.toString());
			ResultSet rsModifier = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlMod.toString());
			double temp_Modifier_Qty = 0.00;
			double temp_Modifier_Amt = 0.00;
			while (rsModifier.next())
			{
			    temp_Modifier_Qty += rsModifier.getDouble(3);
			    temp_Modifier_Amt += rsModifier.getDouble(4);
			}
			records[0] = saleSet.getString(1);
			records[1] = saleSet.getString(2);
			records[2] = df.format(saleSet.getDouble(3) + temp_Modifier_Qty);
			records[3] = new BigDecimal(saleSet.getString(4)).add(new BigDecimal(String.valueOf(temp_Modifier_Amt)));
			temp_Modifier_Qty = 0.00;
			temp_Modifier_Amt = 0.00;
			totalQty = totalQty + new Double(records[2].toString());
			temp1 = temp1.add(new BigDecimal(records[3].toString()));

			dm.addRow(records);
		    }

		    sbSql.setLength(0);
		    sbSql.append("select d.strMenuName,e.strPosName,sum(b.dblQuantity),sum(b.dblAmount)-sum(b.dblDiscountAmt)"
			    + " ,sum(b.dblAmount),d.strMenuCode "
			    + " from tblqbillhd a,tblqbilldtl b,tblmenuitempricingdtl c,tblmenuhd d,tblposmaster e "
			    + " where a.strBillNo=b.strBillNo "
			    + " and date(a.dteBillDate)=date(b.dteBillDate) "
			    + " and b.strItemCode=c.strItemCode "
			    + " and c.strMenuCode=d.strMenuCode "
			    + " and a.strPOSCode=e.strPosCode "
			    + " and a.strPOSCode=c.strPosCode ");
		    if (clsGlobalVarClass.gAreaWisePricing.equals("Y"))
		    {
			sbSql.append(" and a.strAreaCode=c.strAreaCode ");
		    }
		    if (!pos.equals("All") && !cmbOperator.getSelectedItem().equals("All"))
		    {
			sbSql.append(" AND " + objUtility.funGetSelectedPOSCodeString("a.strPOSCode", selectedPOSCodeSet) + " and a.strUserCreated='" + cmbOperator.getSelectedItem().toString() + "'");
		    }
		    else if (!pos.equals("All") && cmbOperator.getSelectedItem().equals("All"))
		    {
			sbSql.append(" AND " + objUtility.funGetSelectedPOSCodeString("a.strPOSCode", selectedPOSCodeSet) + " ");
		    }
		    else if (pos.equals("All") && !cmbOperator.getSelectedItem().equals("All"))
		    {
			sbSql.append(" and a.strUserCreated='" + cmbOperator.getSelectedItem().toString() + "'");
		    }

		    if (clsGlobalVarClass.gEnableShiftYN && (!cmbShift.getSelectedItem().toString().equalsIgnoreCase("All")))
		    {
			sbSql.append(" AND a.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ");
		    }
		    if (!cmbArea.getSelectedItem().equals("All"))
		    {
			sbSql.append(" and a.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ");
		    }
		    if (!cmbOperationType.getSelectedItem().toString().equalsIgnoreCase("All"))
		    {
			sbSql.append(" and a.strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ");
		    }
		    if (!txtCustomerCode.getText().equalsIgnoreCase(""))
		    {
			sbSql.append(" and a.strCustomerCode='" + txtCustomerCode.getText() + "' ");
		    }
		    if (txtBillNofrom.getText().trim().length() == 0 && txtBillnoTo.getText().trim().length() == 0)
		    {
			sbSql.append(" and date( a.dteBillDate ) BETWEEN '" + fromDate + "' AND '" + toDate + "'"
				+ " group by c.strMenuCode,a.strPOSCode ");
		    }
		    else
		    {
			sbSql.append(" and date( a.dteBillDate ) BETWEEN '" + fromDate + "' AND '" + toDate + "' "
				+ " AND a.strBillNo between '" + txtBillNofrom.getText() + "' and '" + txtBillnoTo.getText() + "'"
				+ " group by c.strMenuCode,a.strPOSCode ");
		    }
		    saleSet = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
		    //System.out.println(sbSql.toString());

		    while (saleSet.next())
		    {
			StringBuilder sbSqlMod = new StringBuilder();
			sbSqlMod.setLength(0);
			sbSqlMod.append(" select d.strMenuName,e.strPosName,sum(b.dblQuantity) "
				+ " ,sum(b.dblAmount)-sum(b.dblDiscAmt),sum(b.dblAmount) "
				+ " from tblqbillhd a,tblqbillmodifierdtl b,tblmenuitempricingdtl c,tblmenuhd d,tblposmaster e "
				+ " where a.strBillNo=b.strBillNo  "
				+ " and date(a.dteBillDate)=date(b.dteBillDate) "
				+ " and left(b.strItemCode,7)=c.strItemCode "
				+ " and c.strMenuCode=d.strMenuCode "
				+ " and a.strPOSCode=e.strPosCode "
				+ " and a.strPOSCode=c.strPosCode "
				+ " and a.strAreaCode=c.strAreaCode "
				+ " and b.dblAmount>0 and c.strMenuCode='" + saleSet.getString(6) + "' "
				+ " and date(a.dteBillDate) BETWEEN '" + fromDate + "' and '" + toDate + "' "
			);
			if (clsGlobalVarClass.gEnableShiftYN && (!cmbShift.getSelectedItem().toString().equalsIgnoreCase("All")))
			{
			    sbSqlMod.append(" AND a.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ");
			}
			if (!cmbArea.getSelectedItem().equals("All"))
			{
			    sbSqlMod.append(" and a.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ");
			}
			if (!cmbOperationType.getSelectedItem().toString().equalsIgnoreCase("All"))
			{
			    sbSqlMod.append(" and a.strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ");
			}
			if (!txtCustomerCode.getText().equalsIgnoreCase(""))
			{
			    sbSqlMod.append(" and a.strCustomerCode='" + txtCustomerCode.getText() + "' ");
			}

			//System.out.println(sbSqlMod.toString());
			ResultSet rsModifier = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlMod.toString());
			double temp_Modifier_Qty = 0.00;
			double temp_Modifier_Amt = 0.00;
			while (rsModifier.next())
			{
			    temp_Modifier_Qty += rsModifier.getDouble(3);
			    temp_Modifier_Amt += rsModifier.getDouble(4);
			}
			records[0] = saleSet.getString(1);
			records[1] = saleSet.getString(2);
			records[2] = df.format(saleSet.getDouble(3) + temp_Modifier_Qty);
			records[3] = new BigDecimal(saleSet.getString(4)).add(new BigDecimal(String.valueOf(temp_Modifier_Amt)));
			temp_Modifier_Qty = 0.00;
			temp_Modifier_Amt = 0.00;
			totalQty = totalQty + new Double(records[2].toString());
			temp1 = temp1.add(new BigDecimal(records[3].toString()));

			dm.addRow(records);
		    }

		    Object[] objTotalRows =
		    {
			"Total", "", dfForNoDecimal.format(totalQty), df.format(temp1)
		    };
		    dm1.addRow(objTotalRows);
		}
		tblTotal.setModel(dm1);
		tblSales.setModel(dm);

		tblSales.setSize(400, 400);
		tblSales.setRowHeight(25);
		tblTotal.setRowHeight(40);
		tblTotal.setSize(400, 400);

		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

		tblSales.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
		tblSales.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);

		tblSales.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		tblSales.getColumnModel().getColumn(0).setPreferredWidth(200);

		tblTotal.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
		tblTotal.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);

		tblTotal.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		tblTotal.getColumnModel().getColumn(0).setPreferredWidth(200);

	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    /**
     * menu Wise
     */
    private void funMenuWise()
    {
	StringBuilder sbSqlLive = new StringBuilder();
	StringBuilder sbSqlQFile = new StringBuilder();
	StringBuilder sbSqlFilters = new StringBuilder();
	try
	{
	    sql = "";
	    reportName = "Menu Head Wise Sales Report";
	    totalQty = new Double("0.00");
	    totalAmount = new BigDecimal("0.00");
	    temp = new BigDecimal("0.00");
	    temp1 = new BigDecimal("0.00");
	    dm = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    //all cells false
		    return false;
		}
	    };
	    //dm.addColumn("Menu Code");
	    dm.addColumn("Menu Name");
	    dm.addColumn("POS");
	    dm.addColumn("Quantity");
	    dm.addColumn("SubTotal");
	    dm.addColumn("Sales Amount");
	    dm.addColumn("Discount");

	    vSalesReportExcelColLength = new java.util.Vector();
	    vSalesReportExcelColLength.add("6#Left");
	    vSalesReportExcelColLength.add("6#Left");
	    vSalesReportExcelColLength.add("6#Left");
	    vSalesReportExcelColLength.add("10#Left");
	    vSalesReportExcelColLength.add("10#Left");
	    vSalesReportExcelColLength.add("6#Left");

	    DefaultTableModel dm1 = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    //all cells false
		    return false;
		}
	    };

	    dm1.addColumn("");
	    dm1.addColumn("");
	    dm1.addColumn("Quantity");
	    dm1.addColumn("Sales Amount");
	    dm1.addColumn("SubTotal");
	    dm1.addColumn("Discount");
	    dm1.addColumn("");

	    Date dt1 = dteFromDate.getDate();
	    Date dt2 = dteToDate.getDate();

	    if ((dt2.getTime() - dt1.getTime()) < 0)
	    {
		new frmOkPopUp(null, "Invalid date", "Error", 1).setVisible(true);
	    }
	    else
	    {
		fromDate = funGetFromDate();
		toDate = funGetToDate();

		funGetFromTime();
		funGetToTime();
		String DateFrom = null, field = null, DateTo = null;
		if (funGetFromTime() != null)
		{
		    DateFrom = fromDate + " " + funGetFromTime();
		    field = "b.dteBillDate";
		}
		else
		{
		    DateFrom = fromDate;
		    field = "date(b.dteBillDate)";
		}
		if (funGetToTime() != null)
		{
		    DateTo = toDate + " " + funGetToTime();
		}
		else
		{
		    DateTo = toDate;
		}

		records = new Object[7];
		String pos = funGetSelectedPosCode();

		sbSqlLive.setLength(0);
		sbSqlQFile.setLength(0);
		sbSqlFilters.setLength(0);

		String netTotal = "sum(a.dblAmount)-sum(a.dblDiscountAmt)";
		String rate = "a.dblRate";
		String amount = "sum(a.dblAmount)";
		String discountAmt = "sum(a.dblDiscountAmt)";

		String mNetTotal = "sum(a.dblAmount)-sum(a.dblDiscAmt)";
		String mRate = "a.dblRate";
		String mAmount = "sum(a.dblAmount)";
		String mDiscountAmt = "sum(a.dblDiscAmt)";
		if (cmbCurrency.getSelectedItem().toString().equalsIgnoreCase("USD"))
		{
		    netTotal = "sum(a.dblAmount/b.dblUSDConverionRate)-sum(a.dblDiscountAmt/b.dblUSDConverionRate)";
		    rate = "a.dblRate/b.dblUSDConverionRate";
		    amount = "sum(a.dblAmount/b.dblUSDConverionRate)";
		    discountAmt = "sum(a.dblDiscountAmt/b.dblUSDConverionRate)";

		    mNetTotal = "sum(a.dblAmount/b.dblUSDConverionRate)-sum(a.dblDiscAmt/b.dblUSDConverionRate)";
		    mRate = "a.dblRate/b.dblUSDConverionRate";
		    mAmount = "sum(a.dblAmount/b.dblUSDConverionRate)";
		    mDiscountAmt = "sum(a.dblDiscAmt/b.dblUSDConverionRate)";
		}

		sbSqlQFile.append("SELECT  ifnull(d.strMenuCode,'ND'),ifnull(e.strMenuName,'ND'), sum(a.dblQuantity),\n"
			+ "" + netTotal + ",f.strPosName,'" + clsGlobalVarClass.gUserCode + "'," + rate + " ," + amount + "," + discountAmt + ",b.strPOSCode  "
			+ "FROM tblqbilldtl a\n"
			+ "left outer join tblqbillhd b on a.strBillNo=b.strBillNo and date(a.dteBillDate)=date(b.dteBillDate) and a.strClientCode=b.strClientCode "
			+ "left outer join tblposmaster f on b.strposcode=f.strposcode "
			+ "left outer join tblmenuitempricingdtl d on a.strItemCode = d.strItemCode "
			+ " and b.strposcode =d.strposcode ");
		if (clsGlobalVarClass.gAreaWisePricing.equals("Y"))
		{
		    sbSqlQFile.append("and b.strAreaCode= d.strAreaCode ");
		}
		sbSqlQFile.append("left outer join tblmenuhd e on d.strMenuCode= e.strMenuCode");
		sbSqlQFile.append(" where " + field + " BETWEEN '" + DateFrom + "' AND '" + DateTo + "' ");

		sbSqlLive.append("SELECT  ifnull(d.strMenuCode,'ND'),ifnull(e.strMenuName,'ND'), sum(a.dblQuantity), "
			+ "" + netTotal + ",f.strPosName,'" + clsGlobalVarClass.gUserCode + "'," + rate + " ," + amount + "," + discountAmt + ",b.strPOSCode  "
			+ " FROM tblbilldtl a "
			+ " left outer join tblbillhd b on a.strBillNo=b.strBillNo and date(a.dteBillDate)=date(b.dteBillDate) and a.strClientCode=b.strClientCode "
			+ " left outer join tblposmaster f on b.strposcode=f.strposcode "
			+ " left outer join tblmenuitempricingdtl d on a.strItemCode = d.strItemCode "
			+ " and b.strposcode =d.strposcode ");
		if (clsGlobalVarClass.gAreaWisePricing.equals("Y"))
		{
		    sbSqlLive.append("and b.strAreaCode= d.strAreaCode ");
		}
		sbSqlLive.append("left outer join tblmenuhd e on d.strMenuCode= e.strMenuCode");
		sbSqlLive.append(" where " + field + " BETWEEN '" + DateFrom + "' AND '" + DateTo + "' ");

		String sqlModLive = "SELECT  ifnull(d.strMenuCode,'ND'),ifnull(e.strMenuName,'ND'), sum(a.dblQuantity),\n"
			+ "" + mNetTotal + ",f.strPosName,'" + clsGlobalVarClass.gUserCode + "'," + mRate + " ," + mAmount + "," + mDiscountAmt + ",b.strPOSCode  "
			+ "FROM tblbillmodifierdtl a\n"
			+ "left outer join tblbillhd b on a.strBillNo=b.strBillNo and a.strClientCode=b.strClientCode "
			+ "left outer join tblposmaster f on b.strposcode=f.strposcode "
			+ "left outer join tblmenuitempricingdtl d on LEFT(a.strItemCode,7)= d.strItemCode "
			+ " and b.strposcode =d.strposcode ";
		if (clsGlobalVarClass.gAreaWisePricing.equals("Y"))
		{
		    sqlModLive += "and b.strAreaCode= d.strAreaCode ";
		}
		sqlModLive += "left outer join tblmenuhd e on d.strMenuCode= e.strMenuCode";
		sqlModLive += " where " + field + " BETWEEN '" + DateFrom + "' AND '" + DateTo + "' and a.dblAmount>0 ";

		String sqlModQFile = "SELECT  ifnull(d.strMenuCode,'ND'),ifnull(e.strMenuName,'ND'), sum(a.dblQuantity),\n"
			+ "" + mNetTotal + ",f.strPosName,'" + clsGlobalVarClass.gUserCode + "'," + mRate + " ," + mAmount + "," + mDiscountAmt + ",b.strPOSCode  "
			+ "FROM tblqbillmodifierdtl a\n"
			+ "left outer join tblqbillhd b on a.strBillNo=b.strBillNo and date(a.dteBillDate)=date(b.dteBillDate) and a.strClientCode=b.strClientCode "
			+ "left outer join tblposmaster f on b.strposcode=f.strposcode "
			+ "left outer join tblmenuitempricingdtl d on LEFT(a.strItemCode,7)= d.strItemCode "
			+ " and b.strposcode =d.strposcode ";

		if (clsGlobalVarClass.gAreaWisePricing.equals("Y"))
		{
		    sqlModQFile += "and b.strAreaCode= d.strAreaCode ";
		}
		sqlModQFile += "left outer join tblmenuhd e on d.strMenuCode= e.strMenuCode";
		sqlModQFile += " where " + field + " BETWEEN '" + DateFrom + "' AND '" + DateTo + "' and a.dblAmount>0  ";

		if (!pos.equals("All") && !cmbOperator.getSelectedItem().equals("All"))
		{
		    sbSqlFilters.append(" AND " + objUtility.funGetSelectedPOSCodeString("b.strPOSCode", selectedPOSCodeSet) + " and d.strUserCreated='" + cmbOperator.getSelectedItem().toString() + "'");
		}
		else if (!pos.equals("All") && cmbOperator.getSelectedItem().equals("All"))
		{
		    sbSqlFilters.append(" AND " + objUtility.funGetSelectedPOSCodeString("b.strPOSCode", selectedPOSCodeSet) + " ");
		}
		else if (pos.equals("All") && !cmbOperator.getSelectedItem().equals("All"))
		{
		    sbSqlFilters.append(" and b.strUserCreated='" + cmbOperator.getSelectedItem().toString() + "'");
		}
		if (txtBillNofrom.getText().trim().length() == 0 && txtBillnoTo.getText().trim().length() == 0)
		{
		    //sql_Filters+=" Group by b.strPoscode, d.strMenuCode,e.strMenuName";
		}
		else
		{
		    sbSqlFilters.append(" and b.strBillNo between '" + txtBillNofrom.getText() + "' and '" + txtBillnoTo.getText() + "' ");
		}
		if (clsGlobalVarClass.gEnableShiftYN && (!cmbShift.getSelectedItem().toString().equalsIgnoreCase("All")))
		{
		    sbSqlFilters.append(" AND b.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ");
		}
		if (!cmbArea.getSelectedItem().equals("All"))
		{
		    sbSqlFilters.append(" and b.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ");
		}
		if (!cmbOperationType.getSelectedItem().toString().equalsIgnoreCase("All"))
		{
		    sbSqlFilters.append(" and b.strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ");
		}
		if (!txtCustomerCode.getText().equalsIgnoreCase(""))
		{
		    sbSqlFilters.append(" and b.strCustomerCode='" + txtCustomerCode.getText() + "' ");

		}
		sbSqlFilters.append(" Group by b.strPoscode, d.strMenuCode,e.strMenuName");
		sbSqlFilters.append(" order by b.strPoscode, d.strMenuCode,e.strMenuName");

		sbSqlLive.append(sbSqlFilters);
		sbSqlQFile.append(sbSqlFilters);

		sqlModLive = sqlModLive + " " + sbSqlFilters.toString();
		sqlModQFile = sqlModQFile + " " + sbSqlFilters.toString();

		//System.out.println(sbSqlLive);
		//System.out.println(sbSqlQFile);
		//System.out.println(sqlModLive);
		//System.out.println(sqlModQFile);
		boolean flgRecords = false;
		subTotal = 0.00;
		discountTotal = 0.00;
		if (!flgRecords)
		{
		    dm.setRowCount(0);
		    dm1.setRowCount(0);
		}
		mapPOSMenuHeadDtl = new LinkedHashMap<>();

		ResultSet rsMenuHeadWiseSales = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlLive.toString());
		funGenerateMenuHeadWiseSales(rsMenuHeadWiseSales);
		rsMenuHeadWiseSales = clsGlobalVarClass.dbMysql.executeResultSet(sqlModLive);
		funGenerateMenuHeadWiseSales(rsMenuHeadWiseSales);
		rsMenuHeadWiseSales = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlQFile.toString());
		funGenerateMenuHeadWiseSales(rsMenuHeadWiseSales);
		rsMenuHeadWiseSales = clsGlobalVarClass.dbMysql.executeResultSet(sqlModQFile);
		funGenerateMenuHeadWiseSales(rsMenuHeadWiseSales);

		DecimalFormat decFormat = new DecimalFormat("0");

		Iterator<Map.Entry<String, Map<String, clsBillItemDtl>>> posIterator = mapPOSMenuHeadDtl.entrySet().iterator();
		while (posIterator.hasNext())
		{
		    Map<String, clsBillItemDtl> mapItemDtl = posIterator.next().getValue();
		    Iterator<Map.Entry<String, clsBillItemDtl>> itemIterator = mapItemDtl.entrySet().iterator();
		    while (itemIterator.hasNext())
		    {
			clsBillItemDtl objBillItemDtl = itemIterator.next().getValue();
			records[0] = objBillItemDtl.getMenuName();//menuName
			records[1] = objBillItemDtl.getPosName();//posName
			records[2] = decFormat.format(objBillItemDtl.getQuantity());//qty
			records[3] = gDecimalFormat.format(objBillItemDtl.getAmount());//salesAmt
			records[4] = gDecimalFormat.format(objBillItemDtl.getSubTotal());//subTotal
			records[5] = gDecimalFormat.format(objBillItemDtl.getDiscountAmount());//discAmt

			totalQty = totalQty + new Double(records[2].toString());
			temp1 = temp1.add(new BigDecimal(records[3].toString()));
			subTotal = subTotal + objBillItemDtl.getSubTotal();
			discountTotal = discountTotal + objBillItemDtl.getDiscountAmount();
			dm.addRow(records);
		    }
		}

		Object[] ob1 =
		{
		    "Total", "", decFormat.format(totalQty), gDecimalFormat.format(temp1), gDecimalFormat.format(subTotal), gDecimalFormat.format(discountTotal), ""
		};
		dm1.addRow(ob1);

		tblTotal.setModel(dm1);
		tblSales.setModel(dm);

		//to calculate sales in percent(%)
		dm.addColumn("Sales(%)");
		vSalesReportExcelColLength.add("6#Right"); //sales%                
		double totalSale = Double.parseDouble(tblTotal.getValueAt(0, 3).toString());

		for (int row = 0; row < tblSales.getRowCount(); row++)
		{
		    double saleAmt = Double.parseDouble(tblSales.getValueAt(row, 3).toString());
		    double salePer = (saleAmt / totalSale) * 100;
		    tblSales.setValueAt(gDecimalFormat.format(salePer), row, 6);
		}

		tblSales.setSize(400, 400);
		tblSales.setRowHeight(25);
		tblTotal.setSize(400, 400);
		tblTotal.setRowHeight(40);

		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		tblSales.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
		tblSales.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		tblSales.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
		tblSales.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
		tblSales.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
		tblSales.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tblSales.getColumnModel().getColumn(0).setPreferredWidth(200);

		tblTotal.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
		tblTotal.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		tblTotal.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
		tblTotal.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
		tblTotal.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
		tblTotal.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tblTotal.getColumnModel().getColumn(0).setPreferredWidth(200);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    sbSqlLive = null;
	    sbSqlQFile = null;
	    sbSqlFilters = null;
	}
    }

    /**
     * Group Wise Report
     */
    private void funGroupWise()
    {
	StringBuilder sbSqlLive = new StringBuilder();
	StringBuilder sbSqlQFile = new StringBuilder();
	StringBuilder sbSqlFilters = new StringBuilder();
	pnlGraph.setVisible(false);
	try
	{
	    sql = "";
	    reportName = "Group Wise Sales Report";
	    totalQty = new Double("0.00");
	    totalAmount = new BigDecimal("0.00");
	    temp = new BigDecimal("0.00");
	    temp1 = new BigDecimal("0.00");
	    dm = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    //all cells false
		    return false;
		}
	    };
	    if (chkConsolidatePOS.isSelected())
	    {
		dm.addColumn("Group Name");
		dm.addColumn("Quantity");
		dm.addColumn("SubTotal");
		dm.addColumn("Net Total");
		dm.addColumn("Discount");
	    }
	    else
	    {
		dm.addColumn("Group Name");
		dm.addColumn("POS");
		dm.addColumn("Quantity");
		dm.addColumn("SubTotal");
		dm.addColumn("Net Total");
		dm.addColumn("Discount");
		//dm.addColumn("Grand Total");
	    }

	    vSalesReportExcelColLength = new java.util.Vector();
	    vSalesReportExcelColLength.add("15#Left"); //Group Name
	    vSalesReportExcelColLength.add("6#Left"); //POS
	    vSalesReportExcelColLength.add("6#Right"); //Qty
	    vSalesReportExcelColLength.add("6#Right"); //Sales Amt
	    vSalesReportExcelColLength.add("6#Right"); //subtotal
	    vSalesReportExcelColLength.add("6#Right"); //discount

	    DefaultTableModel dm1 = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    //all cells false
		    return false;
		}
	    };
	    dm1.addColumn("");
	    dm1.addColumn("");
	    dm1.addColumn("Quantity");
	    dm1.addColumn("Sub Total");
	    dm1.addColumn("Net Total");
	    dm1.addColumn("Discount");
	    //dm1.addColumn("Grand Total");
	    dm1.addColumn("");

	    Date dt1 = dteFromDate.getDate();
	    Date dt2 = dteToDate.getDate();

	    if ((dt2.getTime() - dt1.getTime()) < 0)
	    {
		new frmOkPopUp(null, "Invalid date", "Error", 1).setVisible(true);
	    }
	    else
	    {
		fromDate = funGetFromDate();
		toDate = funGetToDate();

		funGetFromTime();
		funGetToTime();
		String DateFrom = null, field = null, DateTo = null;
		if (funGetFromTime() != null)
		{
		    DateFrom = fromDate + " " + funGetFromTime();
		    field = "a.dteBillDate";
		}
		else
		{
		    DateFrom = fromDate;
		    field = "date(a.dteBillDate)";
		}
		if (funGetToTime() != null)
		{
		    DateTo = toDate + " " + funGetToTime();
		}
		else
		{
		    DateTo = toDate;
		}

		String pos = funGetSelectedPosCode();

		sbSqlLive.setLength(0);
		sbSqlQFile.setLength(0);
		sbSqlFilters.setLength(0);

		sbSqlLive.append("SELECT c.strGroupCode,c.strGroupName,sum( b.dblQuantity),sum( b.dblAmount)-sum(b.dblDiscountAmt) "
			+ ",f.strPosName, '" + clsGlobalVarClass.gUserCode + "',b.dblRate ,sum(b.dblAmount) "
			+ ",sum(b.dblDiscountAmt),a.strPOSCode,sum( b.dblAmount)-sum(b.dblDiscountAmt)+sum(b.dblTaxAmount) "
			+ "FROM tblbillhd a,tblbilldtl b,tblgrouphd c,tblsubgrouphd d"
			+ ",tblitemmaster e,tblposmaster f "
			+ "where a.strBillNo=b.strBillNo "
			+ "and date(a.dteBillDate)=date(b.dteBillDate) "
			+ "and a.strPOSCode=f.strPOSCode "
			+ "and a.strClientCode=b.strClientCode "
			+ "and b.strItemCode=e.strItemCode "
			+ "and c.strGroupCode=d.strGroupCode and d.strSubGroupCode=e.strSubGroupCode ");

		sbSqlQFile.append("SELECT c.strGroupCode,c.strGroupName,sum( b.dblQuantity),sum( b.dblAmount)-sum(b.dblDiscountAmt) "
			+ ",f.strPosName, '" + clsGlobalVarClass.gUserCode + "',b.dblRate ,sum(b.dblAmount) "
			+ ",sum(b.dblDiscountAmt),a.strPOSCode,sum( b.dblAmount)-sum(b.dblDiscountAmt)+sum(b.dblTaxAmount) "
			+ "FROM tblqbillhd a,tblqbilldtl b,tblgrouphd c,tblsubgrouphd d"
			+ ",tblitemmaster e,tblposmaster f "
			+ "where a.strBillNo=b.strBillNo "
			+ "and date(a.dteBillDate)=date(b.dteBillDate) "
			+ "and a.strPOSCode=f.strPOSCode "
			+ "and a.strClientCode=b.strClientCode "
			+ "and b.strItemCode=e.strItemCode "
			+ "and c.strGroupCode=d.strGroupCode and d.strSubGroupCode=e.strSubGroupCode ");

		String sqlModLive = "select c.strGroupCode,c.strGroupName,sum(b.dblQuantity)"
			+ ",sum(b.dblAmount)-sum(b.dblDiscAmt),f.strPOSName,'" + clsGlobalVarClass.gUserCode + "','0'"
			+ ",sum(b.dblAmount),sum(b.dblDiscAmt),a.strPOSCode,sum(b.dblAmount)-sum(b.dblDiscAmt) "
			+ " from tblbillmodifierdtl b,tblbillhd a,tblposmaster f,tblitemmaster d"
			+ ",tblsubgrouphd e,tblgrouphd c "
			+ " where a.strBillNo=b.strBillNo "
			+ " and date(a.dteBillDate)=date(b.dteBillDate) "
			+ " and a.strPOSCode=f.strPosCode "
			+ " and a.strClientCode=b.strClientCode "
			+ " and LEFT(b.strItemCode,7)=d.strItemCode "
			+ " and d.strSubGroupCode=e.strSubGroupCode and e.strGroupCode=c.strGroupCode "
			+ " and b.dblamount>0 "
			+ " and " + field + " BETWEEN '" + DateFrom + "' AND '" + DateTo + "' ";

		String sqlModQFile = "select c.strGroupCode,c.strGroupName,sum(b.dblQuantity)"
			+ ",sum(b.dblAmount)-sum(b.dblDiscAmt),f.strPOSName,'" + clsGlobalVarClass.gUserCode + "'"
			+ ",'0' ,sum(b.dblAmount),sum(b.dblDiscAmt),a.strPOSCode,sum(b.dblAmount)-sum(b.dblDiscAmt) "
			+ " from tblqbillmodifierdtl b,tblqbillhd a,tblposmaster f,tblitemmaster d"
			+ ",tblsubgrouphd e,tblgrouphd c "
			+ " where a.strBillNo=b.strBillNo "
			+ " and date(a.dteBillDate)=date(b.dteBillDate) "
			+ " and a.strPOSCode=f.strPosCode "
			+ " and a.strClientCode=b.strClientCode "
			+ " and LEFT(b.strItemCode,7)=d.strItemCode "
			+ " and d.strSubGroupCode=e.strSubGroupCode "
			+ " and e.strGroupCode=c.strGroupCode "
			+ " and b.dblamount>0 "
			+ " and " + field + " BETWEEN '" + DateFrom + "' AND '" + DateTo + "' ";

		if (!pos.equals("All") && !cmbOperator.getSelectedItem().equals("All"))
		{
		    sbSqlFilters.append(" AND " + objUtility.funGetSelectedPOSCodeString("a.strPOSCode", selectedPOSCodeSet) + " and a.strUserCreated='" + cmbOperator.getSelectedItem().toString() + "' ");
		}
		else if (pos.equals("All") && !cmbOperator.getSelectedItem().equals("All"))
		{
		    sbSqlFilters.append(" and a.strUserCreated='" + cmbOperator.getSelectedItem().toString() + "'");
		}
		else if (!pos.equals("All") && cmbOperator.getSelectedItem().equals("All"))
		{
		    sbSqlFilters.append(" AND " + objUtility.funGetSelectedPOSCodeString("a.strPOSCode", selectedPOSCodeSet) + " ");
		}

		if (clsGlobalVarClass.gEnableShiftYN && (!cmbShift.getSelectedItem().toString().equalsIgnoreCase("All")))
		{
		    sbSqlFilters.append(" AND a.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ");
		}
		if (!cmbArea.getSelectedItem().equals("All"))
		{
		    sbSqlFilters.append(" and a.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ");
		}
		if (!cmbOperationType.getSelectedItem().toString().equalsIgnoreCase("All"))
		{
		    sbSqlFilters.append(" and a.strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ");
		}
		if (!txtCustomerCode.getText().equalsIgnoreCase(""))
		{
		    sbSqlFilters.append(" and a.strCustomerCode='" + txtCustomerCode.getText() + "' ");

		}
		if (chkConsolidatePOS.isSelected())
		{
		    if (txtBillNofrom.getText().trim().length() == 0 && txtBillnoTo.getText().trim().length() == 0)
		    {
			sbSqlFilters.append(" and " + field + " BETWEEN '" + DateFrom + "' AND '" + DateTo + "'"
				+ " GROUP BY c.strGroupCode, c.strGroupName ");
		    }
		    else
		    {
			sbSqlFilters.append(" WHERE " + field + " BETWEEN '" + DateFrom + "' AND '" + DateTo + "' "
				+ "and a.strBillNo between '" + txtBillNofrom.getText() + "' and '" + txtBillnoTo.getText() + "'"
				+ " GROUP BY c.strGroupCode, c.strGroupName ");
		    }
		}
		else
		{
		    if (txtBillNofrom.getText().trim().length() == 0 && txtBillnoTo.getText().trim().length() == 0)
		    {
			sbSqlFilters.append(" and " + field + " BETWEEN '" + DateFrom + "' AND '" + DateTo + "' "
				+ " GROUP BY c.strGroupCode, c.strGroupName, a.strPoscode ");
		    }
		    else
		    {
			sbSqlFilters.append(" WHERE " + field + " BETWEEN '" + DateFrom + "' AND '" + DateTo + "' "
				+ "and a.strBillNo between '" + txtBillNofrom.getText() + "' and '" + txtBillnoTo.getText() + "'"
				+ " GROUP BY c.strGroupCode, c.strGroupName, a.strPoscode ");
		    }
		}

		boolean flgRecords = false;

		sbSqlLive.append(sbSqlFilters);
		sbSqlQFile.append(sbSqlFilters);
		sqlModLive += " " + sbSqlFilters;
		sqlModQFile += " " + sbSqlFilters;

//                    System.out.println("live-->"+sbSqlLive);
//                    System.out.println("Q-->"+sbSqlQFile);
//                    System.out.println("liveModi-->"+sqlModLive);
//                    System.out.println("QModi-->"+sqlModQFile);
		DecimalFormat decFormat = new DecimalFormat("0");

		if (!flgRecords)
		{
		    dm.setRowCount(0);
		    dm1.setRowCount(0);
		}
		mapPOSDtlForGroupSubGroup = new LinkedHashMap<>();
		subTotal = 0.00;
		discountTotal = 0.00;

		ResultSet rsGroupWiseSales = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlLive.toString());
		funGenerateGroupWiseSales(rsGroupWiseSales);
		rsGroupWiseSales = clsGlobalVarClass.dbMysql.executeResultSet(sqlModLive);
		funGenerateGroupWiseSales(rsGroupWiseSales);
		rsGroupWiseSales = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlQFile.toString());
		funGenerateGroupWiseSales(rsGroupWiseSales);
		rsGroupWiseSales = clsGlobalVarClass.dbMysql.executeResultSet(sqlModQFile);
		funGenerateGroupWiseSales(rsGroupWiseSales);

		double totalSalesAmt = 0, totalGrandTotal = 0;

		if (cmbType.getSelectedItem().equals("Chart"))
		{
		    ArrayList<String> arrListGraphData = new ArrayList<>();
		    double totalAmt = 0;
		    Iterator<Map.Entry<String, List<Map<String, clsGroupSubGroupWiseSales>>>> it = mapPOSDtlForGroupSubGroup.entrySet().iterator();
		    while (it.hasNext())
		    {
			Map.Entry<String, List<Map<String, clsGroupSubGroupWiseSales>>> entry = it.next();
			String posCode = entry.getKey();
			List<Map<String, clsGroupSubGroupWiseSales>> listOfGroup = entry.getValue();
			String posName = "";
			String groupName = "";
			for (int i = 0; i < listOfGroup.size(); i++)
			{
			    double groupTotal = listOfGroup.get(i).entrySet().iterator().next().getValue().getSalesAmt();
			    posName = listOfGroup.get(i).entrySet().iterator().next().getValue().getPosName();
			    groupName = listOfGroup.get(i).entrySet().iterator().next().getValue().getGroupName();
			    arrListGraphData.add(posName + "#" + groupName + "#" + groupTotal);
			    totalAmt = totalAmt + groupTotal;
			}
		    }
		    if (chkConsolidatePOS.isSelected())
		    {
			funGenerateGraph("pieChart", arrListGraphData, totalAmt, true);
		    }
		    else
		    {
			funGenerateGraph("pieChart", arrListGraphData, totalAmt, false);
		    }
		    pnlSalesData.setVisible(false);
		    pnlSalesTotal.setVisible(false);
		    BufferedImage image = null;
		    image = ImageIO.read(new File("Graph"));
		    pnlGraph.setVisible(true);
		    pnlGraph.add(lblName);
		    pnlGraph.setBackground(Color.white);
		    lblName.setBackground(Color.white);
		    lblName.setIcon((new javax.swing.ImageIcon(image)));
		}
		else
		{
		    Iterator<Map.Entry<String, List<Map<String, clsGroupSubGroupWiseSales>>>> it = mapPOSDtlForGroupSubGroup.entrySet().iterator();
		    while (it.hasNext())
		    {
			Map.Entry<String, List<Map<String, clsGroupSubGroupWiseSales>>> entry = it.next();
			String posCode = entry.getKey();
			List<Map<String, clsGroupSubGroupWiseSales>> listOfGroup = entry.getValue();
			for (int i = 0; i < listOfGroup.size(); i++)
			{
			    if (chkConsolidatePOS.isSelected())
			    {
				/*
                                 * records[0] =
                                 * objGroupDtl.getGroupName();//groupName
                                 * records[1] = objGroupDtl.getQty();//qty
                                 * records[2] =
                                 * objGroupDtl.getSalesAmt();//salesAmount
                                 * records[3] =
                                 * objGroupDtl.getSubTotal();//subTotal
                                 * records[4] =
                                 * objGroupDtl.getDiscAmt();//discAmt
				 */

				clsGroupSubGroupWiseSales objGroupDtl = listOfGroup.get(i).entrySet().iterator().next().getValue();
				Object[] arrObjRows =
				{
				    objGroupDtl.getGroupName(), decFormat.format(objGroupDtl.getQty()), gDecimalFormat.format(objGroupDtl.getSalesAmt()), gDecimalFormat.format(objGroupDtl.getSubTotal()), gDecimalFormat.format(objGroupDtl.getDiscAmt())
				};

				totalQty = totalQty + objGroupDtl.getQty();
				totalSalesAmt += objGroupDtl.getSalesAmt();
				subTotal = subTotal + objGroupDtl.getSubTotal();
				discountTotal = discountTotal + objGroupDtl.getDiscAmt();
				totalGrandTotal += objGroupDtl.getGrandTotal();
				dm.addRow(arrObjRows);
			    }
			    else
			    {
				clsGroupSubGroupWiseSales objGroupDtl = listOfGroup.get(i).entrySet().iterator().next().getValue();
				/*
                                 * records[0] =
                                 * objGroupDtl.getGroupName();//groupName
                                 * records[1] =
                                 * objGroupDtl.getPosName();//posName records[2]
                                 * = objGroupDtl.getQty();//qty records[3] =
                                 * objGroupDtl.getSalesAmt();//salesAmount
                                 * records[4] =
                                 * objGroupDtl.getSubTotal();//subTotal
                                 * records[5] =
                                 * objGroupDtl.getDiscAmt();//discAmt
				 */

				Object[] arrObjRows =
				{
				    objGroupDtl.getGroupName(), objGroupDtl.getPosName(), decFormat.format(objGroupDtl.getQty()), gDecimalFormat.format(objGroupDtl.getSalesAmt()), gDecimalFormat.format(objGroupDtl.getSubTotal()), gDecimalFormat.format(objGroupDtl.getDiscAmt())
				};

				totalQty = totalQty + objGroupDtl.getQty();
				totalSalesAmt += objGroupDtl.getSalesAmt();
				subTotal = subTotal + objGroupDtl.getSubTotal();
				discountTotal = discountTotal + objGroupDtl.getDiscAmt();
				totalGrandTotal += objGroupDtl.getGrandTotal();
				dm.addRow(arrObjRows);
			    }
			}
		    }

		    Object[] ob1
			    =
			    {
				"Total", "", decFormat.format(totalQty), gDecimalFormat.format(totalSalesAmt), gDecimalFormat.format(subTotal), gDecimalFormat.format(discountTotal), ""
			    };
		    dm1.addRow(ob1);

		    tblTotal.setModel(dm1);
		    tblSales.setModel(dm);

		    //to calculate sales in percent(%)
//                    dm.addColumn("Disc(%)");
//                    vSalesReportExcelColLength.add("6#Right"); //sales%
		    dm.addColumn("Sales(%)");
		    vSalesReportExcelColLength.add("7#Right"); //sales%

		    double totalSale = Double.parseDouble(tblTotal.getValueAt(0, 3).toString());
		    //System.out.println("totalSale=" + totalSale);
		    for (int row = 0; row < tblSales.getRowCount(); row++)
		    {
			double saleAmt;
			if (chkConsolidatePOS.isSelected())
			{
			    saleAmt = Double.parseDouble(tblSales.getValueAt(row, 2).toString());
			    double salePer = (saleAmt / totalSale) * 100;
			    tblSales.setValueAt(gDecimalFormat.format(salePer), row, 5);

			}
			else
			{
			    saleAmt = Double.parseDouble(tblSales.getValueAt(row, 3).toString());
			    double salePer = (saleAmt / totalSale) * 100;
			    tblSales.setValueAt(gDecimalFormat.format(salePer), row, 6);
			}
		    }

		    tblSales.setSize(new Dimension(400, 400));
		    tblSales.setRowHeight(40);
		    tblTotal.setSize(new Dimension(400, 400));
		    tblTotal.setRowHeight(40);

		    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

		    tblSales.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
		    tblSales.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		    tblSales.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
		    tblSales.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
		    if (chkConsolidatePOS.isSelected() == false)
		    {
			tblSales.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
		    }
		    tblSales.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		    tblSales.getColumnModel().getColumn(0).setPreferredWidth(200);
		    DefaultTableCellRenderer rightRenderer1 = new DefaultTableCellRenderer();
		    rightRenderer1.setHorizontalAlignment(JLabel.RIGHT);

		    tblTotal.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
		    tblTotal.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		    tblTotal.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
		    tblTotal.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
		    if (chkConsolidatePOS.isSelected())
		    {
			tblTotal.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
		    }
		    tblTotal.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		    tblTotal.getColumnModel().getColumn(0).setPreferredWidth(200);
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    sbSqlLive = null;
	    sbSqlQFile = null;
	    sbSqlFilters = null;
	}
    }

    /**
     * Sub Group Wise Report
     */
    private void funSubGroupWise()
    {
	StringBuilder sbSqlLive = new StringBuilder();
	StringBuilder sbSqlQFile = new StringBuilder();
	StringBuilder sbSqlFilters = new StringBuilder();
	pnlGraph.setVisible(false);
	try
	{
	    sql = "";
	    reportName = "Sub Group Wise Sales Report";
	    totalQty = new Double("0.00");
	    totalAmount = new BigDecimal("0.00");
	    temp = new BigDecimal("0.00");

	    temp1 = new BigDecimal("0.00");
	    dm = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    return false;
		}
	    };
	    if (chkConsolidatePOS.isSelected())
	    {
		dm.addColumn("Sub Group Name");
		dm.addColumn("Quantity");
		dm.addColumn("SubTotal");
		dm.addColumn("Sales Amount");
		dm.addColumn("Discount");
	    }
	    else
	    {
		dm.addColumn("Sub Group Name");
		dm.addColumn("POS");
		dm.addColumn("Quantity");
		dm.addColumn("SubTotal");
		dm.addColumn("Sales Amount");
		dm.addColumn("Discount");
	    }
	    vSalesReportExcelColLength = new java.util.Vector();
	    vSalesReportExcelColLength.add("6#Left"); //SubGroup Code
	    vSalesReportExcelColLength.add("15#Left"); //SubGroup Name
	    vSalesReportExcelColLength.add("6#Left"); //POS
	    vSalesReportExcelColLength.add("6#Right"); //Qty
	    vSalesReportExcelColLength.add("6#Right"); //Sales Amt
	    vSalesReportExcelColLength.add("6#Right"); //subtotal
	    vSalesReportExcelColLength.add("6#Right"); //discount

	    DefaultTableModel dm1 = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    //all cells false
		    return false;
		}
	    };
	    dm1.addColumn("");
	    dm1.addColumn("");
	    dm1.addColumn("Quantity");
	    dm1.addColumn("Sales Amount");
	    dm1.addColumn("SubTotal");
	    dm1.addColumn("Discount");
	    dm1.addColumn("");

	    Date dt1 = dteFromDate.getDate();
	    Date dt2 = dteToDate.getDate();

	    if ((dt2.getTime() - dt1.getTime()) < 0)
	    {
		new frmOkPopUp(null, "Invalid date", "Error", 1).setVisible(true);
	    }
	    else
	    {
		fromDate = funGetFromDate();
		toDate = funGetToDate();

		funGetFromTime();
		funGetToTime();
		String DateFrom = null, field = null, DateTo = null;
		if (funGetFromTime() != null)
		{
		    DateFrom = fromDate + " " + funGetFromTime();
		    field = "a.dteBillDate";
		}
		else
		{
		    DateFrom = fromDate;
		    field = "date(a.dteBillDate)";
		}
		if (funGetToTime() != null)
		{
		    DateTo = toDate + " " + funGetToTime();
		}
		else
		{
		    DateTo = toDate;
		}

		records = new Object[7];
		//String sql_Filters="",sql_LiveBill="",sql_SubGroupWise="";
		String pos = funGetSelectedPosCode();

		sbSqlLive.setLength(0);
		sbSqlQFile.setLength(0);
		sbSqlFilters.setLength(0);

		sbSqlQFile.append("SELECT c.strSubGroupCode, c.strSubGroupName, sum( b.dblQuantity ) "
			+ " , sum( b.dblAmount )-sum(b.dblDiscountAmt), f.strPosName,'" + clsGlobalVarClass.gUserCode + "',b.dblRate ,sum(b.dblAmount),sum(b.dblDiscountAmt),a.strPOSCode"
			+ " from tblqbillhd a,tblqbilldtl b,tblsubgrouphd c,tblitemmaster d "
			+ " ,tblposmaster f "
			+ " where a.strBillNo=b.strBillNo "
			+ " and date(a.dteBillDate)=date(b.dteBillDate) "
			+ " and a.strPOSCode=f.strPOSCode "
			+ " and a.strClientCode=b.strClientCode "
			+ " and b.strItemCode=d.strItemCode "
			+ " and c.strSubGroupCode=d.strSubGroupCode ");

		sbSqlLive.append(" SELECT c.strSubGroupCode, c.strSubGroupName, sum( b.dblQuantity ) "
			+ " , sum( b.dblAmount )-sum(b.dblDiscountAmt), f.strPosName,'" + clsGlobalVarClass.gUserCode + "',b.dblRate ,sum(b.dblAmount),sum(b.dblDiscountAmt),a.strPOSCode"
			+ " from tblbillhd a,tblbilldtl b,tblsubgrouphd c,tblitemmaster d "
			+ " ,tblposmaster f "
			+ " where a.strBillNo=b.strBillNo "
			+ " and date(a.dteBillDate)=date(b.dteBillDate) "
			+ " and a.strPOSCode=f.strPOSCode "
			+ " and a.strClientCode=b.strClientCode "
			+ " and b.strItemCode=d.strItemCode "
			+ " and c.strSubGroupCode=d.strSubGroupCode ");

		String sqlModLive = "select c.strSubGroupCode,c.strSubGroupName"
			+ ",sum(b.dblQuantity),sum(b.dblAmount)-sum(b.dblDiscAmt),f.strPOSName"
			+ ",'" + clsGlobalVarClass.gUserCode + "','0' ,sum(b.dblAmount),sum(b.dblDiscAmt),a.strPOSCode "
			+ " from tblbillmodifierdtl b,tblbillhd a,tblposmaster f,tblitemmaster d"
			+ ",tblsubgrouphd c"
			+ " where a.strBillNo=b.strBillNo "
			+ " and date(a.dteBillDate)=date(b.dteBillDate) "
			+ " and a.strPOSCode=f.strPosCode "
			+ " and a.strClientCode=b.strClientCode "
			+ " and LEFT(b.strItemCode,7)=d.strItemCode "
			+ " and d.strSubGroupCode=c.strSubGroupCode "
			+ " and b.dblamount>0 "
			+ " and " + field + " BETWEEN '" + DateFrom + "' AND '" + DateTo + "' ";

		String sqlModQFile = "select c.strSubGroupCode,c.strSubGroupName"
			+ ",sum(b.dblQuantity),sum(b.dblAmount)-sum(b.dblDiscAmt),f.strPOSName"
			+ ",'" + clsGlobalVarClass.gUserCode + "','0' ,sum(b.dblAmount),sum(b.dblDiscAmt),a.strPOSCode "
			+ " from tblqbillmodifierdtl b,tblqbillhd a,tblposmaster f,tblitemmaster d"
			+ ",tblsubgrouphd c"
			+ " where a.strBillNo=b.strBillNo "
			+ " and date(a.dteBillDate)=date(b.dteBillDate) "
			+ " and a.strPOSCode=f.strPosCode "
			+ " and a.strClientCode=b.strClientCode "
			+ " and LEFT(b.strItemCode,7)=d.strItemCode "
			+ " and d.strSubGroupCode=c.strSubGroupCode "
			+ " and b.dblamount>0 "
			+ " and " + field + " BETWEEN '" + DateFrom + "' AND '" + DateTo + "' ";

		if (!pos.equals("All") && !cmbOperator.getSelectedItem().equals("All"))
		{
		    sbSqlFilters.append(" AND " + objUtility.funGetSelectedPOSCodeString("a.strPOSCode", selectedPOSCodeSet) + " and a.strUserCreated='" + cmbOperator.getSelectedItem().toString() + "'");
		}
		else if (!pos.equals("All") && cmbOperator.getSelectedItem().equals("All"))
		{
		    sbSqlFilters.append(" AND " + objUtility.funGetSelectedPOSCodeString("a.strPOSCode", selectedPOSCodeSet) + " ");
		}
		else if (pos.equals("All") && !cmbOperator.getSelectedItem().equals("All"))
		{
		    sbSqlFilters.append(" and a.strUserCreated='" + cmbOperator.getSelectedItem().toString() + "'");
		}
		if (clsGlobalVarClass.gEnableShiftYN && (!cmbShift.getSelectedItem().toString().equalsIgnoreCase("All")))
		{
		    sbSqlFilters.append(" AND a.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ");
		}
		if (!cmbArea.getSelectedItem().equals("All"))
		{
		    sbSqlFilters.append(" and a.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ");
		}
		if (!cmbOperationType.getSelectedItem().toString().equalsIgnoreCase("All"))
		{
		    sbSqlFilters.append(" and a.strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ");
		}
		if (!txtCustomerCode.getText().equalsIgnoreCase(""))
		{
		    sbSqlFilters.append(" and a.strCustomerCode='" + txtCustomerCode.getText() + "' ");

		}
		if (chkConsolidatePOS.isSelected())
		{
		    if (txtBillNofrom.getText().trim().length() == 0 && txtBillnoTo.getText().trim().length() == 0)
		    {
			sbSqlFilters.append(" and " + field + " BETWEEN '" + DateFrom + "' AND '" + DateTo + "' "
				+ " group by c.strSubGroupCode, c.strSubGroupName");
		    }
		    else
		    {
			sbSqlFilters.append(" and " + field + " BETWEEN '" + DateFrom + "' AND '" + DateTo + "' "
				+ " and a.strBillNo between '" + txtBillNofrom.getText() + "' and '" + txtBillnoTo.getText() + "' "
				+ " group by c.strSubGroupCode, c.strSubGroupName");
		    }
		}
		else
		{
		    if (txtBillNofrom.getText().trim().length() == 0 && txtBillnoTo.getText().trim().length() == 0)
		    {
			sbSqlFilters.append(" and " + field + " BETWEEN '" + DateFrom + "' AND '" + DateTo + "'"
				+ " group by c.strSubGroupCode, c.strSubGroupName, a.strPoscode ");
		    }
		    else
		    {
			sbSqlFilters.append(" and " + field + " BETWEEN '" + DateFrom + "' AND '" + DateTo + "' "
				+ " and a.strBillNo between '" + txtBillNofrom.getText() + "' and '" + txtBillnoTo.getText() + "' "
				+ " group by c.strSubGroupCode, c.strSubGroupName, a.strPoscode");
		    }
		}
		sbSqlLive.append(sbSqlFilters);
		sbSqlQFile.append(sbSqlFilters);
		sqlModLive += " " + sbSqlFilters;
		sqlModQFile += " " + sbSqlFilters;

//                System.out.println("live-->"+sbSqlLive);
//                System.out.println("Q-->"+sbSqlQFile);
//                System.out.println("liveModi-->"+sqlModLive);
//                System.out.println("QModi-->"+sqlModQFile);
		mapPOSDtlForGroupSubGroup = new LinkedHashMap<>();
		subTotal = 0.00;
		discountTotal = 0.00;

		DecimalFormat decFormat = new DecimalFormat("0");

		dm.setRowCount(0);
		dm1.setRowCount(0);

		ResultSet rsSubGroupWiseSales = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlLive.toString());
		funGenerateSubGroupWiseSales(rsSubGroupWiseSales);
		rsSubGroupWiseSales = clsGlobalVarClass.dbMysql.executeResultSet(sqlModLive);
		funGenerateSubGroupWiseSales(rsSubGroupWiseSales);
		rsSubGroupWiseSales = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlQFile.toString());
		funGenerateSubGroupWiseSales(rsSubGroupWiseSales);
		rsSubGroupWiseSales = clsGlobalVarClass.dbMysql.executeResultSet(sqlModQFile);
		funGenerateSubGroupWiseSales(rsSubGroupWiseSales);

		if (cmbType.getSelectedItem().equals("Chart"))
		{
		    ArrayList<String> arrListGraphData = new ArrayList<>();
		    double totalAmt = 0;
		    Iterator<Map.Entry<String, List<Map<String, clsGroupSubGroupWiseSales>>>> it = mapPOSDtlForGroupSubGroup.entrySet().iterator();
		    while (it.hasNext())
		    {
			Map.Entry<String, List<Map<String, clsGroupSubGroupWiseSales>>> entry = it.next();
			String posCode = entry.getKey();
			List<Map<String, clsGroupSubGroupWiseSales>> listOfGroup = entry.getValue();
			String posName = "";
			String groupName = "";
			for (int i = 0; i < listOfGroup.size(); i++)
			{
			    double groupTotal = listOfGroup.get(i).entrySet().iterator().next().getValue().getSalesAmt();
			    posName = listOfGroup.get(i).entrySet().iterator().next().getValue().getPosName();
			    groupName = listOfGroup.get(i).entrySet().iterator().next().getValue().getGroupName();

			    arrListGraphData.add(posName + "#" + groupName + "#" + groupTotal);

			    totalAmt = totalAmt + groupTotal;
			}
		    }
		    if (chkConsolidatePOS.isSelected())
		    {
			funGenerateGraph("pieChart", arrListGraphData, totalAmt, true);
		    }
		    else
		    {
			funGenerateGraph("pieChart", arrListGraphData, totalAmt, false);
		    }
		    pnlSalesData.setVisible(false);
		    pnlSalesTotal.setVisible(false);
		    BufferedImage image = null;

		    image = ImageIO.read(new File("Graph"));
		    pnlGraph.setVisible(true);
		    pnlGraph.add(lblName);
		    pnlGraph.setBackground(Color.white);
		    lblName.setBackground(Color.white);
		    lblName.setIcon((new javax.swing.ImageIcon(image)));

		}
		else
		{
		    Iterator<Map.Entry<String, List<Map<String, clsGroupSubGroupWiseSales>>>> it = mapPOSDtlForGroupSubGroup.entrySet().iterator();
		    while (it.hasNext())
		    {
			Map.Entry<String, List<Map<String, clsGroupSubGroupWiseSales>>> entry = it.next();
			String posCode = entry.getKey();
			List<Map<String, clsGroupSubGroupWiseSales>> listOfGroup = entry.getValue();
			for (int i = 0; i < listOfGroup.size(); i++)
			{
			    if (chkConsolidatePOS.isSelected())
			    {
				clsGroupSubGroupWiseSales objGroupDtl = listOfGroup.get(i).entrySet().iterator().next().getValue();
				records[0] = objGroupDtl.getGroupName();//groupName
				//records[1] = rsGroupWiseSales.getString(3);
				records[1] = decFormat.format(objGroupDtl.getQty());//qty
				records[2] = gDecimalFormat.format(objGroupDtl.getSalesAmt());//salesAmount
				records[3] = gDecimalFormat.format(objGroupDtl.getSubTotal());//subTotal
				records[4] = gDecimalFormat.format(objGroupDtl.getDiscAmt());//discAmt

				totalQty = totalQty + new Double(records[2].toString());
				temp1 = temp1.add(new BigDecimal(records[3].toString()));
				subTotal = subTotal + objGroupDtl.getSubTotal();
				discountTotal = discountTotal + objGroupDtl.getDiscAmt();
				dm.addRow(records);
			    }
			    else
			    {
				clsGroupSubGroupWiseSales objGroupDtl = listOfGroup.get(i).entrySet().iterator().next().getValue();
				records[0] = objGroupDtl.getGroupName();//groupName
				records[1] = objGroupDtl.getPosName();//posName
				records[2] = decFormat.format(objGroupDtl.getQty());//qty
				records[3] = gDecimalFormat.format(objGroupDtl.getSalesAmt());//salesAmount
				records[4] = gDecimalFormat.format(objGroupDtl.getSubTotal());//subTotal
				records[5] = gDecimalFormat.format(objGroupDtl.getDiscAmt());//discAmt

				totalQty = totalQty + new Double(records[2].toString());
				temp1 = temp1.add(new BigDecimal(records[3].toString()));
				subTotal = subTotal + objGroupDtl.getSubTotal();
				discountTotal = discountTotal + objGroupDtl.getDiscAmt();
				dm.addRow(records);
			    }
			}
		    }
		    Object[] ob1
			    =
			    {
				"Total", "", decFormat.format(totalQty), gDecimalFormat.format(temp1), gDecimalFormat.format(subTotal), gDecimalFormat.format(discountTotal), ""
			    };
		    dm1.addRow(ob1);

		    tblTotal.setModel(dm1);
		    tblSales.setModel(dm);

		    //to calculate sales in percent(%)
		    dm.addColumn("Sales(%)");
		    vSalesReportExcelColLength.add("6#Right"); //sales%

		    double totalSale = Double.parseDouble(tblTotal.getValueAt(0, 4).toString());
		    //System.out.println("totalSale=" + totalSale);
		    for (int row = 0; row < tblSales.getRowCount(); row++)
		    {
			if (chkConsolidatePOS.isSelected())
			{
			    double saleAmt = Double.parseDouble(tblSales.getValueAt(row, 3).toString());
			    double salePer = (saleAmt / totalSale) * 100;
			    tblSales.setValueAt(gDecimalFormat.format(salePer), row, 5);
			}
			else
			{
			    double saleAmt = Double.parseDouble(tblSales.getValueAt(row, 4).toString());
			    double salePer = (saleAmt / totalSale) * 100;
			    tblSales.setValueAt(gDecimalFormat.format(salePer), row, 6);
			}
		    }

		    tblSales.setSize(400, 400);
		    tblSales.setRowHeight(40);
		    tblTotal.setSize(400, 400);
		    tblTotal.setRowHeight(40);

		    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

		    tblSales.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
		    tblSales.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		    tblSales.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
		    tblSales.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
		    if (chkConsolidatePOS.isSelected() == false)
		    {
			tblSales.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
		    }
		    tblSales.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		    tblSales.getColumnModel().getColumn(0).setPreferredWidth(200);
		    tblTotal.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
		    tblTotal.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		    tblTotal.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
		    tblTotal.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
		    if (chkConsolidatePOS.isSelected() == false)
		    {
			tblTotal.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
		    }
		    tblTotal.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		    tblTotal.getColumnModel().getColumn(0).setPreferredWidth(200);
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    sbSqlLive = null;
	    sbSqlQFile = null;
	    sbSqlFilters = null;
	}
    }

    /**
     * Cost Center Wise Report
     */
    private void funCostCentreWise()
    {
	StringBuilder sbSqlLive = new StringBuilder();
	StringBuilder sbSqlQFile = new StringBuilder();
	StringBuilder sbSqlFilters = new StringBuilder();
	try
	{
	    sql = "";
	    reportName = "Cost Center Wise Sales Report";
	    totalQty = new Double("0.00");
	    totalAmount = new BigDecimal("0.00");
	    temp = new BigDecimal("0.00");
	    temp1 = new BigDecimal("0.00");
	    dm = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    //all cells false
		    return false;
		}
	    };

	    dm.addColumn("Cost Centre Name");
	    dm.addColumn("POS");
	    dm.addColumn("Quantity");
	    dm.addColumn("SubTotal");
	    dm.addColumn("Sales Amount");
	    dm.addColumn("Discount");
	    dm.addColumn("Sales(%)");

	    vSalesReportExcelColLength = new java.util.Vector();

	    vSalesReportExcelColLength.add("15#Left"); //Cost Center Name
	    vSalesReportExcelColLength.add("6#Left"); //POS
	    vSalesReportExcelColLength.add("6#Right"); //Qty
	    vSalesReportExcelColLength.add("6#Right"); //Sales Amt
	    vSalesReportExcelColLength.add("6#Right"); //subtotal
	    vSalesReportExcelColLength.add("6#Right"); //discount
	    vSalesReportExcelColLength.add("6#Right"); //sales%

	    DefaultTableModel dm1 = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    //all cells false
		    return false;
		}
	    };
	    dm1.addColumn("");
	    dm1.addColumn("");
	    dm1.addColumn("Quantity");
	    dm1.addColumn("Sales Amount");
	    dm1.addColumn("SubTotal");
	    dm1.addColumn("Discount");
	    dm1.addColumn("");

	    Date dt1 = dteFromDate.getDate();
	    Date dt2 = dteToDate.getDate();

	    if ((dt2.getTime() - dt1.getTime()) < 0)
	    {
		new frmOkPopUp(null, "Invalid date", "Error", 1).setVisible(true);
	    }
	    else
	    {
		fromDate = funGetFromDate();
		toDate = funGetToDate();

		funGetFromTime();
		funGetToTime();
		String DateFrom = null, field = null, DateTo = null;
		if (funGetFromTime() != null)
		{
		    DateFrom = fromDate + " " + funGetFromTime();
		    field = "d.dteBillDate";
		}
		else
		{
		    DateFrom = fromDate;
		    field = "date(d.dteBillDate)";
		}
		if (funGetToTime() != null)
		{
		    DateTo = toDate + " " + funGetToTime();
		}
		else
		{
		    DateTo = toDate;
		}

		records = new Object[7];
		//String sql_Filters="",sql_LiveBill="",sql_CostCenterWise="";
		String pos = funGetSelectedPosCode();

		sbSqlLive.setLength(0);
		sbSqlQFile.setLength(0);
		sbSqlFilters.setLength(0);

		// Live Sql
		sbSqlLive.append("SELECT ifnull(a.strCostCenterCode,'ND')"
			+ ", ifnull(a.strCostCenterName,'ND') ,sum( c.dblQuantity )"
			+ " ,sum( c.dblAmount )-sum(c.dblDiscountAmt), e.strPOSName,'" + clsGlobalVarClass.gUserCode + "' "
			+ ",c.dblRate  ,sum(c.dblAmount),sum(c.dblDiscountAmt),e.strPosCode  "
			+ " from tblbilldtl c "
			+ " left outer join tblbillhd d on c.strBillNo = d.strBillNo and date(c.dteBillDate)=date(d.dteBillDate) "
			+ " and c.strClientCode=d.strClientCode "
			+ " left outer join tblposmaster e on d.strPOSCode = e.strPOSCode "
			+ " left outer join tblmenuitempricingdtl b on b.strItemCode = c.strItemCode  "
			+ " and (b.strposcode =d.strposcode or b.strPosCode='All') and b.strHourlyPricing='NO' "
			+ " left outer join tblcostcentermaster a on a.strCostCenterCode = b.strCostCenterCode "
			+ " where " + field + " BETWEEN '" + DateFrom + "' AND '" + DateTo + "' ");
		if (clsGlobalVarClass.gAreaWisePricing.equals("Y"))
		{
		    sbSqlLive.append(" and d.strAreaCode=b.strAreaCode ");
		}

		// QFile Sql    
		sbSqlQFile.append("SELECT ifnull(a.strCostCenterCode,'ND')"
			+ ", ifnull(a.strCostCenterName,'ND') ,sum( c.dblQuantity )"
			+ " ,sum( c.dblAmount )-sum(c.dblDiscountAmt), e.strPOSName,'" + clsGlobalVarClass.gUserCode + "'"
			+ ",c.dblRate ,sum(c.dblAmount),sum(c.dblDiscountAmt),e.strPosCode "
			+ " from tblqbilldtl c "
			+ " left outer join tblqbillhd d on c.strBillNo = d.strBillNo and date(c.dteBillDate)=date(d.dteBillDate) "
			+ " and c.strClientCode=d.strClientCode "
			+ " left outer join tblposmaster e on d.strPOSCode = e.strPOSCode "
			+ " left outer join tblmenuitempricingdtl b on b.strItemCode = c.strItemCode "
			+ " and (b.strposcode =d.strposcode or b.strPosCode='All') and b.strHourlyPricing='NO' "
			+ " left outer join tblcostcentermaster a on a.strCostCenterCode = b.strCostCenterCode "
			+ " where " + field + " BETWEEN '" + DateFrom + "' AND '" + DateTo + "' ");
		if (clsGlobalVarClass.gAreaWisePricing.equals("Y"))
		{
		    sbSqlQFile.append(" and d.strAreaCode=b.strAreaCode ");
		}

		String sqlModLive = "SELECT ifnull(a.strCostCenterCode,'ND')"
			+ ", ifnull(a.strCostCenterName,'ND') ,sum( c.dblQuantity )"
			+ " ,sum(c.dblAmount)-sum(c.dblDiscAmt), e.strPOSName,'" + clsGlobalVarClass.gUserCode + "'"
			+ ",c.dblRate ,sum( c.dblAmount ),sum(c.dblDiscAmt),e.strPosCode "
			+ " from tblbillmodifierdtl c "
			+ " left outer join tblbillhd d on c.strBillNo = d.strBillNo and date(c.dteBillDate)=date(d.dteBillDate) "
			+ " and c.strClientCode=d.strClientCode "
			+ " left outer join tblposmaster e on d.strPOSCode = e.strPOSCode "
			+ " left outer join tblmenuitempricingdtl b on b.strItemCode =LEFT(c.strItemCode,7) "
			+ " and (b.strposcode =d.strposcode or b.strPosCode='All') and b.strHourlyPricing='NO' "
			+ " left outer join tblcostcentermaster a on a.strCostCenterCode = b.strCostCenterCode "
			+ " where " + field + " BETWEEN '" + DateFrom + "' AND '" + DateTo + "' "
			+ " and c.dblAmount>0";
		if (clsGlobalVarClass.gAreaWisePricing.equals("Y"))
		{
		    sqlModLive += " and d.strAreaCode=b.strAreaCode ";
		}

		String sqlModQFile = "SELECT ifnull(a.strCostCenterCode,'ND')"
			+ ", ifnull(a.strCostCenterName,'ND') ,sum( c.dblQuantity )"
			+ " ,sum(c.dblAmount)-sum(c.dblDiscAmt), e.strPOSName,'" + clsGlobalVarClass.gUserCode + "'"
			+ ",c.dblRate ,sum( c.dblAmount ),sum(c.dblDiscAmt),e.strPosCode "
			+ " from tblqbillmodifierdtl c "
			+ " left outer join tblqbillhd d on c.strBillNo = d.strBillNo and date(c.dteBillDate)=date(d.dteBillDate) "
			+ " and c.strClientCode=d.strClientCode "
			+ " left outer join tblposmaster e on d.strPOSCode = e.strPOSCode "
			+ " left outer join tblmenuitempricingdtl b on b.strItemCode =LEFT(c.strItemCode,7)  "
			+ " and (b.strposcode =d.strposcode or b.strPosCode='All') and b.strHourlyPricing='NO' "
			+ " left outer join tblcostcentermaster a on a.strCostCenterCode = b.strCostCenterCode "
			+ " where " + field + " BETWEEN '" + DateFrom + "' AND '" + DateTo + "' "
			+ " and c.dblAmount>0";
		if (clsGlobalVarClass.gAreaWisePricing.equals("Y"))
		{
		    sqlModQFile += " and d.strAreaCode=b.strAreaCode ";
		}

		if (!pos.equals("All") && !cmbOperator.getSelectedItem().equals("All"))
		{
		    sbSqlFilters.append(" AND " + objUtility.funGetSelectedPOSCodeString("d.strPOSCode", selectedPOSCodeSet) + " and d.strUserCreated='" + cmbOperator.getSelectedItem().toString() + "'");
		}
		else if (!pos.equals("All") && cmbOperator.getSelectedItem().equals("All"))
		{
		    sbSqlFilters.append(" AND " + objUtility.funGetSelectedPOSCodeString("d.strPOSCode", selectedPOSCodeSet) + " ");
		}
		else if (pos.equals("All") && !cmbOperator.getSelectedItem().equals("All"))
		{
		    sbSqlFilters.append(" and d.strUserCreated='" + cmbOperator.getSelectedItem().toString() + "'");
		}
		if (clsGlobalVarClass.gEnableShiftYN && (!cmbShift.getSelectedItem().toString().equalsIgnoreCase("All")))
		{
		    sbSqlFilters.append(" AND d.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ");
		}
		if (!cmbArea.getSelectedItem().equals("All"))
		{
		    sbSqlFilters.append(" and d.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ");
		}
		if (!cmbOperationType.getSelectedItem().toString().equalsIgnoreCase("All"))
		{
		    sbSqlFilters.append(" and d.strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ");
		}
		if (!txtCustomerCode.getText().equalsIgnoreCase(""))
		{
		    sbSqlFilters.append(" and d.strCustomerCode='" + txtCustomerCode.getText() + "' ");

		}
		if (txtBillNofrom.getText().trim().length() == 0 && txtBillnoTo.getText().trim().length() == 0)
		{
		    sbSqlFilters.append(" GROUP BY b.strCostCenterCode,a.strCostCenterName, e.strPOSName,c.dblRate");
		}
		else
		{
		    sbSqlFilters.append(" and d.strBillNo between '" + txtBillNofrom.getText() + "' and '" + txtBillnoTo.getText() + "' "
			    + "GROUP BY b.strCostCenterCode,a.strCostCenterName, e.strPOSName,c.dblRate");
		}

		sbSqlLive.append(sbSqlFilters);
		sbSqlQFile.append(sbSqlFilters);
		sqlModLive = sqlModLive + " " + sbSqlFilters.toString();
		sqlModQFile = sqlModQFile + " " + sbSqlFilters.toString();

//                System.out.println(sbSqlLive);
//                System.out.println(sbSqlQFile);
//                System.out.println(sqlModLive);
//                System.out.println(sqlModQFile);
		subTotal = 0.00;
		discountTotal = 0.00;
		double totalQty = 0, totalAmt = 0;

		mapPOSCostCenterWiseSales = new LinkedHashMap<String, Map<String, clsCommonBeanDtl>>();
		ResultSet rsCostCenterWise = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlLive.toString());
		funGenerateCostCenterWiseSales(rsCostCenterWise);
		rsCostCenterWise = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlQFile.toString());
		funGenerateCostCenterWiseSales(rsCostCenterWise);
		rsCostCenterWise = clsGlobalVarClass.dbMysql.executeResultSet(sqlModLive.toString());
		funGenerateCostCenterWiseSales(rsCostCenterWise);
		rsCostCenterWise = clsGlobalVarClass.dbMysql.executeResultSet(sqlModQFile.toString());
		funGenerateCostCenterWiseSales(rsCostCenterWise);

		DecimalFormat decFormat = new DecimalFormat("0");

		Iterator<Map.Entry<String, Map<String, clsCommonBeanDtl>>> posIterator = mapPOSCostCenterWiseSales.entrySet().iterator();
		while (posIterator.hasNext())
		{
		    Map<String, clsCommonBeanDtl> mapCCDtl = posIterator.next().getValue();
		    Iterator<Map.Entry<String, clsCommonBeanDtl>> ccIterator = mapCCDtl.entrySet().iterator();
		    while (ccIterator.hasNext())
		    {
			clsCommonBeanDtl objCCDtl = ccIterator.next().getValue();

			records[0] = objCCDtl.getCostCenterName();//ccName
			records[1] = objCCDtl.getPosName();//posName
			records[2] = decFormat.format(objCCDtl.getQty());//qty
			records[3] = gDecimalFormat.format(objCCDtl.getSaleAmount());//salesAmt
			records[4] = gDecimalFormat.format(objCCDtl.getSubTotal());//subTotal
			records[5] = gDecimalFormat.format(objCCDtl.getDiscAmount());//discAmt

			totalQty = totalQty + new Double(records[2].toString());
			totalAmt = totalAmt + new Double(records[3].toString());
			subTotal = subTotal + objCCDtl.getSubTotal();
			discountTotal = discountTotal + objCCDtl.getDiscAmount();
			//temp1=temp1.add(new BigDecimal(records[4].toString()));
			dm.addRow(records);
		    }
		}
		Object[] ob1 =
		{
		    "Total", "", decFormat.format(totalQty), gDecimalFormat.format(totalAmt), gDecimalFormat.format(subTotal), gDecimalFormat.format(discountTotal), ""
		};
		dm1.addRow(ob1);
		tblTotal.setModel(dm1);
		tblSales.setModel(dm);

		//to calculate sales in percent(%)
		double totalSale = Double.parseDouble(tblTotal.getValueAt(0, 3).toString());
		//System.out.println("totalSale=" + totalSale);
		for (int row = 0; row < tblSales.getRowCount(); row++)
		{
		    double saleAmt = Double.parseDouble(tblSales.getValueAt(row, 3).toString());
		    double salePer = (saleAmt / totalSale) * 100;
		    tblSales.setValueAt(gDecimalFormat.format(salePer), row, 6);
		}
		tblSales.setSize(400, 400);
		tblSales.setRowHeight(40);
		tblTotal.setRowHeight(40);
		tblTotal.setSize(400, 400);

		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

		tblSales.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
		tblSales.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		tblSales.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
		tblSales.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
		tblSales.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
		tblSales.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tblSales.getColumnModel().getColumn(0).setPreferredWidth(200);

		tblTotal.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
		tblTotal.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		tblTotal.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
		tblTotal.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
		tblTotal.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
		tblTotal.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tblTotal.getColumnModel().getColumn(0).setPreferredWidth(200);
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    sbSqlLive = null;
	    sbSqlQFile = null;
	    sbSqlFilters = null;
	}
    }

    /**
     * Operator Wise Report
     */
    private void funOperatorWise()
    {
	mapOperatorDtls = new HashMap<String, List<clsOperatorDtl>>();
	StringBuilder sbSqlLive = new StringBuilder();
	StringBuilder sbSqlQFile = new StringBuilder();
	StringBuilder sbSqlDisLive = new StringBuilder();
	StringBuilder sbSqlQDisFile = new StringBuilder();
	StringBuilder sbSqlFilters = new StringBuilder();
	StringBuilder sbSqlDisFilters = new StringBuilder();
	try
	{
	    sql = "";
	    reportName = "Operator Wise Sales Report";
	    totalAmount = new BigDecimal("0.00");
	    temp = new BigDecimal("0.00");
	    temp1 = new BigDecimal("0.00");
	    dm = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    //all cells false
		    return false;
		}
	    };
	    dm.addColumn("Operator Code");
	    dm.addColumn("Operator Name");
	    dm.addColumn("POS");
	    dm.addColumn("Payment Mode");
	    dm.addColumn("Disc Amount");
	    dm.addColumn("Sales Amount");

	    vSalesReportExcelColLength = new java.util.Vector();
	    vSalesReportExcelColLength.add("6#Left"); //Operator Code
	    vSalesReportExcelColLength.add("15#Left"); //Operator Name
	    vSalesReportExcelColLength.add("6#Left"); //POS
	    vSalesReportExcelColLength.add("6#Left"); //Pay Mode
	    vSalesReportExcelColLength.add("6#Right"); //Qty
	    vSalesReportExcelColLength.add("6#Right"); //Sales Amt

	    DefaultTableModel dm1 = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    //all cells false
		    return false;
		}
	    };
	    dm1.addColumn("");
	    dm1.addColumn("Disc Amount");
	    dm1.addColumn("Sales Amount");
	    Date dt1 = dteFromDate.getDate();
	    Date dt2 = dteToDate.getDate();

	    if ((dt2.getTime() - dt1.getTime()) < 0)
	    {
		new frmOkPopUp(null, "Invalid date", "Error", 1).setVisible(true);
	    }
	    else
	    {
		fromDate = funGetFromDate();
		toDate = funGetToDate();
		records = new Object[6];
		String pos = funGetSelectedPosCode();

		sbSqlLive.setLength(0);
		sbSqlQFile.setLength(0);
		sbSqlDisLive.setLength(0);
		sbSqlQDisFile.setLength(0);
		sbSqlFilters.setLength(0);
		sbSqlDisFilters.setLength(0);

		sbSqlLive.append(" SELECT a.strUserCode, a.strUserName, c.strPOSName,e.strSettelmentDesc "
			+ " ,sum(d.dblSettlementAmt),'SANGUINE',c.strPosCode, d.strSettlementCode "
			+ " FROM tbluserhd a "
			+ " INNER JOIN tblbillhd b ON a.strUserCode = b.strUserEdited "
			+ " inner join tblposmaster c on b.strPOSCode=c.strPOSCode "
			+ " inner join tblbillsettlementdtl d on b.strBillNo=d.strBillNo and date(b.dteBillDate)=date(d.dteBillDate) and b.strClientCode=d.strClientCode "
			+ " inner join tblsettelmenthd e on d.strSettlementCode=e.strSettelmentCode "
			+ " WHERE date( b.dteBillDate ) BETWEEN '" + fromDate + "' AND '" + toDate + "' ");

		sbSqlQFile.append(" SELECT a.strUserCode, a.strUserName, c.strPOSName,e.strSettelmentDesc "
			+ " ,sum(d.dblSettlementAmt),'SANGUINE',c.strPosCode, d.strSettlementCode "
			+ " FROM tbluserhd a "
			+ " INNER JOIN tblqbillhd b ON a.strUserCode = b.strUserEdited "
			+ " inner join tblposmaster c on b.strPOSCode=c.strPOSCode "
			+ " inner join tblqbillsettlementdtl d on b.strBillNo=d.strBillNo and date(b.dteBillDate)=date(d.dteBillDate) and b.strClientCode=d.strClientCode "
			+ " inner join tblsettelmenthd e on d.strSettlementCode=e.strSettelmentCode "
			+ " WHERE date( b.dteBillDate ) BETWEEN '" + fromDate + "' AND '" + toDate + "' ");

		if (!pos.equals("All") && !cmbOperator.getSelectedItem().equals("All"))
		{
		    sbSqlFilters.append(" AND b.strPOSCode = '" + pos + "' and b.strUserCreated='" + cmbOperator.getSelectedItem().toString() + "'");
		}
		else if (!pos.equals("All") && cmbOperator.getSelectedItem().equals("All"))
		{
		    sbSqlFilters.append(" AND b.strPOSCode = '" + pos + "'");
		}
		else if (pos.equals("All") && !cmbOperator.getSelectedItem().equals("All"))
		{
		    sbSqlFilters.append("  and b.strUserEdited='" + cmbOperator.getSelectedItem().toString() + "'");
		}
		if (clsGlobalVarClass.gEnableShiftYN && (!cmbShift.getSelectedItem().toString().equalsIgnoreCase("All")))
		{
		    sbSqlFilters.append(" AND b.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ");
		}
		if (!cmbArea.getSelectedItem().equals("All"))
		{
		    sbSqlFilters.append(" and b.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ");
		}
		if (!cmbOperationType.getSelectedItem().toString().equalsIgnoreCase("All"))
		{
		    sbSqlFilters.append(" and b.strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ");
		}
		sbSqlFilters.append(" GROUP BY a.strUserCode, b.strPosCode, d.strSettlementCode");

		sbSqlLive.append(sbSqlFilters);
		sbSqlQFile.append(sbSqlFilters);

		//System.out.println(sbSqlLive);
		//System.out.println(sbSqlQFile);
		//Map<String, Map<String, List<clsOperatorDtl>>> hmOperatorWiseSales = new HashMap<String, Map<String, List<clsOperatorDtl>>>();
		//Map<String, List<clsOperatorDtl>> hmOperatorWiseSales = new HashMap<String, List<clsOperatorDtl>>();
		//List<clsOperatorDtl> arrListOperatorWiseSales=new ArrayList<clsOperatorDtl>();
		Map<String, Map<String, clsOperatorDtl>> hmOperatorWiseSales = new HashMap<String, Map<String, clsOperatorDtl>>();
		Map<String, clsOperatorDtl> hmSettlementDtl = null;
		clsOperatorDtl objOperatorWiseSales = null;

		ResultSet rsOperator = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlLive.toString());
		while (rsOperator.next())
		{
		    if (hmOperatorWiseSales.containsKey(rsOperator.getString(1)))
		    {
			hmSettlementDtl = hmOperatorWiseSales.get(rsOperator.getString(1));
			if (hmSettlementDtl.containsKey(rsOperator.getString(8)))
			{
			    objOperatorWiseSales = hmSettlementDtl.get(rsOperator.getString(8));
			    objOperatorWiseSales.setSettleAmt(objOperatorWiseSales.getSettleAmt() + rsOperator.getDouble(5));
			}
			else
			{
			    objOperatorWiseSales = new clsOperatorDtl();
			    objOperatorWiseSales.setStrUserCode(rsOperator.getString(1));
			    objOperatorWiseSales.setStrUserName(rsOperator.getString(2));
			    objOperatorWiseSales.setStrPOSName(rsOperator.getString(3));
			    objOperatorWiseSales.setStrSettlementDesc(rsOperator.getString(4));
			    objOperatorWiseSales.setSettleAmt(rsOperator.getDouble(5));
			    objOperatorWiseSales.setStrPOSCode(rsOperator.getString(7));
			    objOperatorWiseSales.setDiscountAmt(0);
			}
			hmSettlementDtl.put(rsOperator.getString(8), objOperatorWiseSales);
		    }
		    else
		    {
			objOperatorWiseSales = new clsOperatorDtl();
			objOperatorWiseSales.setStrUserCode(rsOperator.getString(1));
			objOperatorWiseSales.setStrUserName(rsOperator.getString(2));
			objOperatorWiseSales.setStrPOSName(rsOperator.getString(3));
			objOperatorWiseSales.setStrSettlementDesc(rsOperator.getString(4));
			objOperatorWiseSales.setSettleAmt(rsOperator.getDouble(5));
			objOperatorWiseSales.setStrPOSCode(rsOperator.getString(7));
			objOperatorWiseSales.setDiscountAmt(0);

			hmSettlementDtl = new HashMap<String, clsOperatorDtl>();
			hmSettlementDtl.put(rsOperator.getString(8), objOperatorWiseSales);
		    }
		    hmOperatorWiseSales.put(rsOperator.getString(1), hmSettlementDtl);
		}
		rsOperator.close();

		rsOperator = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlQFile.toString());
		while (rsOperator.next())
		{
		    if (hmOperatorWiseSales.containsKey(rsOperator.getString(1)))
		    {
			hmSettlementDtl = hmOperatorWiseSales.get(rsOperator.getString(1));
			if (hmSettlementDtl.containsKey(rsOperator.getString(8)))
			{
			    objOperatorWiseSales = hmSettlementDtl.get(rsOperator.getString(8));
			    objOperatorWiseSales.setSettleAmt(objOperatorWiseSales.getSettleAmt() + rsOperator.getDouble(5));
			}
			else
			{
			    objOperatorWiseSales = new clsOperatorDtl();
			    objOperatorWiseSales.setStrUserCode(rsOperator.getString(1));
			    objOperatorWiseSales.setStrUserName(rsOperator.getString(2));
			    objOperatorWiseSales.setStrPOSName(rsOperator.getString(3));
			    objOperatorWiseSales.setStrSettlementDesc(rsOperator.getString(4));
			    objOperatorWiseSales.setSettleAmt(rsOperator.getDouble(5));
			    objOperatorWiseSales.setStrPOSCode(rsOperator.getString(7));
			    objOperatorWiseSales.setDiscountAmt(0);
			}
			hmSettlementDtl.put(rsOperator.getString(8), objOperatorWiseSales);
		    }
		    else
		    {
			objOperatorWiseSales = new clsOperatorDtl();
			objOperatorWiseSales.setStrUserCode(rsOperator.getString(1));
			objOperatorWiseSales.setStrUserName(rsOperator.getString(2));
			objOperatorWiseSales.setStrPOSName(rsOperator.getString(3));
			objOperatorWiseSales.setStrSettlementDesc(rsOperator.getString(4));
			objOperatorWiseSales.setSettleAmt(rsOperator.getDouble(5));
			objOperatorWiseSales.setStrPOSCode(rsOperator.getString(7));
			objOperatorWiseSales.setDiscountAmt(0);

			hmSettlementDtl = new HashMap<String, clsOperatorDtl>();
			hmSettlementDtl.put(rsOperator.getString(8), objOperatorWiseSales);
		    }
		    hmOperatorWiseSales.put(rsOperator.getString(1), hmSettlementDtl);
		}
		rsOperator.close();

		sbSqlDisLive.append("SELECT a.strUserCode, a.strUserName, c.strPOSName"
			+ " ,sum(b.dblDiscountAmt),'SANGUINE',c.strPosCode "
			+ " FROM tbluserhd a "
			+ " INNER JOIN tblbillhd b ON a.strUserCode = b.strUserEdited "
			+ " inner join tblposmaster c on b.strPOSCode=c.strPOSCode "
			+ " WHERE date( b.dteBillDate ) BETWEEN '" + fromDate + "' AND '" + toDate + "' ");

		sbSqlQDisFile.append("  SELECT a.strUserCode, a.strUserName, c.strPOSName "
			+ " ,sum(b.dblDiscountAmt),'SANGUINE',c.strPosCode "
			+ " FROM tbluserhd a "
			+ " INNER JOIN tblqbillhd b ON a.strUserCode = b.strUserEdited "
			+ " inner join tblposmaster c on b.strPOSCode=c.strPOSCode "
			+ " WHERE date( b.dteBillDate ) BETWEEN '" + fromDate + "' AND '" + toDate + "' ");

		if (!pos.equals("All") && !cmbOperator.getSelectedItem().equals("All"))
		{
		    sbSqlDisFilters.append(" AND b.strPOSCode = '" + pos + "' and b.strUserCreated='" + cmbOperator.getSelectedItem().toString() + "'");
		}
		else if (!pos.equals("All") && cmbOperator.getSelectedItem().equals("All"))
		{
		    sbSqlDisFilters.append(" AND b.strPOSCode = '" + pos + "'");
		}
		else if (pos.equals("All") && !cmbOperator.getSelectedItem().equals("All"))
		{
		    sbSqlDisFilters.append("  and b.strUserEdited='" + cmbOperator.getSelectedItem().toString() + "'");
		}
		if (clsGlobalVarClass.gEnableShiftYN && (!cmbShift.getSelectedItem().toString().equalsIgnoreCase("All")))
		{
		    sbSqlDisFilters.append(" AND b.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ");
		}
		if (!cmbArea.getSelectedItem().equals("All"))
		{
		    sbSqlDisFilters.append(" and b.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ");
		}
		if (!cmbOperationType.getSelectedItem().toString().equalsIgnoreCase("All"))
		{
		    sbSqlDisFilters.append(" and b.strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ");
		}
		sbSqlDisFilters.append(" GROUP BY a.strUserCode, b.strPosCode");

		sbSqlDisLive.append(sbSqlDisFilters);
		sbSqlQDisFile.append(sbSqlDisFilters);

		//System.out.println(sbSqlDisLive);
		//System.out.println(sbSqlQDisFile);
		ResultSet rsOperatorDis = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlDisLive.toString());
		while (rsOperatorDis.next())
		{
		    if (hmOperatorWiseSales.containsKey(rsOperatorDis.getString(1)))
		    {
			hmSettlementDtl = hmOperatorWiseSales.get(rsOperatorDis.getString(1));
			Set<String> setKeys = hmSettlementDtl.keySet();
			for (String keys : setKeys)
			{
			    objOperatorWiseSales = hmSettlementDtl.get(keys);
			    objOperatorWiseSales.setDiscountAmt(objOperatorWiseSales.getDiscountAmt() + rsOperatorDis.getDouble(4));
			    hmSettlementDtl.put(keys, objOperatorWiseSales);
			    break;
			}
			hmOperatorWiseSales.put(rsOperatorDis.getString(1), hmSettlementDtl);
		    }
		}
		rsOperatorDis.close();

		rsOperatorDis = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlQDisFile.toString());
		while (rsOperatorDis.next())
		{
		    if (hmOperatorWiseSales.containsKey(rsOperatorDis.getString(1)))
		    {
			hmSettlementDtl = hmOperatorWiseSales.get(rsOperatorDis.getString(1));
			Set<String> setKeys = hmSettlementDtl.keySet();
			for (String keys : setKeys)
			{
			    objOperatorWiseSales = hmSettlementDtl.get(keys);
			    objOperatorWiseSales.setDiscountAmt(objOperatorWiseSales.getDiscountAmt() + rsOperatorDis.getDouble(4));
			    hmSettlementDtl.put(keys, objOperatorWiseSales);
			    break;
			}
			hmOperatorWiseSales.put(rsOperatorDis.getString(1), hmSettlementDtl);
		    }
		}
		rsOperatorDis.close();

		double discAmt = 0, totalAmt = 0;
		Object[] arrObjTableRowData = new Object[6];
		for (Map.Entry<String, Map<String, clsOperatorDtl>> entry : hmOperatorWiseSales.entrySet())
		{
		    Map<String, clsOperatorDtl> hmOpSettlementDtl = entry.getValue();
		    for (Map.Entry<String, clsOperatorDtl> entryOp : hmOpSettlementDtl.entrySet())
		    {
			clsOperatorDtl objOperatorDtl = entryOp.getValue();
			arrObjTableRowData[0] = objOperatorDtl.getStrUserCode();//userCode
			arrObjTableRowData[1] = objOperatorDtl.getStrUserName();//userName
			arrObjTableRowData[2] = objOperatorDtl.getStrPOSName();//posName
			arrObjTableRowData[3] = objOperatorDtl.getStrSettlementDesc();//payMode
			arrObjTableRowData[4] = gDecimalFormat.format(objOperatorDtl.getDiscountAmt());//disc
			arrObjTableRowData[5] = gDecimalFormat.format(objOperatorDtl.getSettleAmt());//saleAmt
			discAmt += Double.parseDouble(arrObjTableRowData[4].toString());
			totalAmt += Double.parseDouble(arrObjTableRowData[5].toString());
			dm.addRow(arrObjTableRowData);
		    }
		}

		Object[] ob1 =
		{
		    "Total", gDecimalFormat.format(discAmt), gDecimalFormat.format(totalAmt)
		};
		dm1.addRow(ob1);

		tblTotal.setModel(dm1);
		tblSales.setModel(dm);
		tblSales.setSize(400, 400);
		tblSales.setRowHeight(40);
		tblTotal.setRowHeight(40);
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		tblSales.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
		tblSales.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
		tblSales.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblSales.getColumnModel().getColumn(0).setPreferredWidth(120);
		tblSales.getColumnModel().getColumn(1).setPreferredWidth(242);
		tblSales.getColumnModel().getColumn(2).setPreferredWidth(80);
		tblSales.getColumnModel().getColumn(3).setPreferredWidth(150);
		tblSales.getColumnModel().getColumn(4).setPreferredWidth(130);
		tblSales.getColumnModel().getColumn(5).setPreferredWidth(100);
		// tblSales.getColumnModel().getColumn(6).setPreferredWidth(100);
		tblTotal.setSize(400, 400);
		DefaultTableCellRenderer rightRenderer1 = new DefaultTableCellRenderer();
		rightRenderer1.setHorizontalAlignment(JLabel.RIGHT);
		tblTotal.getColumnModel().getColumn(1).setCellRenderer(rightRenderer1);
		tblTotal.getColumnModel().getColumn(2).setCellRenderer(rightRenderer1);
		tblTotal.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblTotal.getColumnModel().getColumn(0).setPreferredWidth(570);
		tblTotal.getColumnModel().getColumn(1).setPreferredWidth(100);
		tblTotal.getColumnModel().getColumn(2).setPreferredWidth(120);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    sbSqlLive = null;
	    sbSqlQFile = null;
	    sbSqlFilters = null;
	    sbSqlDisLive = null;
	    sbSqlQDisFile = null;
	    sbSqlDisFilters = null;
	}
    }

    /**
     * Settlement Wise Report
     */
    private void funSettlementWise()
    {
	StringBuilder sbLive = new StringBuilder();
	StringBuilder sbQFile = new StringBuilder();
	StringBuilder sbFilters = new StringBuilder();
	pnlGraph.setVisible(false);

	try
	{
	    sql = "";
	    reportName = "Settlement Wise Sales Report";
	    totalQty = new Double("0.00");
	    totalAmount = new BigDecimal("0.00");
	    temp = new BigDecimal("0.00");
	    temp1 = new BigDecimal("0.00");
	    dm = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    return false;
		}
	    };
	    if (chkConsolidatePOS.isSelected())
	    {
		//dm.addColumn("POS");
		dm.addColumn("Settlement Mode");
		dm.addColumn("Sales Amount");
		dm.addColumn("Sales(%)");
	    }
	    else
	    {
		dm.addColumn("POS");
		dm.addColumn("Settlement Mode");
		dm.addColumn("Sales Amount");
		dm.addColumn("Sales(%)");
	    }
	    vSalesReportExcelColLength = new java.util.Vector();
	    vSalesReportExcelColLength.add("6#Left"); //POS
	    vSalesReportExcelColLength.add("15#Left"); //Settlement Mode
	    vSalesReportExcelColLength.add("6#Right"); //Sales Amt
	    vSalesReportExcelColLength.add("6#Right"); //sales%

	    DefaultTableModel dm1 = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    return false;
		}
	    };
	    dm1.addColumn("");
	    dm1.addColumn("Sales Amount");
	    Date dt1 = dteFromDate.getDate();
	    Date dt2 = dteToDate.getDate();

	    if ((dt2.getTime() - dt1.getTime()) < 0)
	    {
		new frmOkPopUp(null, "Invalid date", "Error", 1).setVisible(true);
	    }
	    else
	    {
		fromDate = funGetFromDate();
		toDate = funGetToDate();
		records = new Object[4];

		funGetFromTime();
		funGetToTime();
		String DateFrom = null, field = null, DateTo = null;
		if (funGetFromTime() != null)
		{
		    DateFrom = fromDate + " " + funGetFromTime();
		    field = "c.dteBillDate";
		}
		else
		{
		    DateFrom = fromDate;
		    field = "date(c.dteBillDate)";
		}
		if (funGetToTime() != null)
		{
		    DateTo = toDate + " " + funGetToTime();
		}
		else
		{
		    DateTo = toDate;
		}

		String pos = funGetSelectedPosCode();

		sbLive.setLength(0);
		sbQFile.setLength(0);
		sbFilters.setLength(0);

		String settlementAmt = "IFNULL(SUM(a.dblSettlementAmt),0.00)";
		if (cmbCurrency.getSelectedItem().toString().equalsIgnoreCase("USD"))
		{
		    settlementAmt = "IFNULL(SUM(a.dblSettlementAmt/c.dblUSDConverionRate),0.00)";
		}

		sbLive.append("SELECT d.strPOSCode,b.strSettelmentCode, IFNULL(d.strPOSName,'') AS strPOSName, IFNULL(b.strSettelmentDesc,'') AS strSettelmentDesc "
			+ " ," + settlementAmt + " AS dblSettlementAmt,'" + clsGlobalVarClass.gUserCode + "'"
			+ " ,b.strSettelmentType "
			+ " from "
			+ " tblbillsettlementdtl a "
			+ " LEFT OUTER JOIN tblsettelmenthd b ON a.strSettlementCode=b.strSettelmentCode "
			+ " LEFT OUTER JOIN tblbillhd c on a.strBillNo=c.strBillNo and date(c.dteBillDate)=date(a.dteBillDate) and a.strClientCode=c.strClientCode "
			+ " LEFT OUTER JOIN tblposmaster d on c.strPOSCode=d.strPosCode "
			+ " WHERE " + field + " BETWEEN '" + DateFrom + "' AND '" + DateTo + "' "
			+ "AND a.dblSettlementAmt>0 ");

		sbQFile.append("SELECT d.strPOSCode,b.strSettelmentCode, IFNULL(d.strPOSName,'') AS strPOSName, IFNULL(b.strSettelmentDesc,'') AS strSettelmentDesc "
			+ " ," + settlementAmt + " AS dblSettlementAmt,'" + clsGlobalVarClass.gUserCode + "' "
			+ " ,b.strSettelmentType "
			+ " from "
			+ " tblqbillsettlementdtl a "
			+ " LEFT OUTER JOIN tblsettelmenthd b ON a.strSettlementCode=b.strSettelmentCode "
			+ " LEFT OUTER JOIN tblqbillhd c on a.strBillNo=c.strBillNo  and date(c.dteBillDate)=date(a.dteBillDate)  and a.strClientCode=c.strClientCode "
			+ " LEFT OUTER JOIN tblposmaster d on c.strPOSCode=d.strPosCode "
			+ " WHERE " + field + " BETWEEN '" + DateFrom + "' AND '" + DateTo + "' "
			+ " AND a.dblSettlementAmt>0 ");

		if (!pos.equals("All") && !cmbOperator.getSelectedItem().equals("All"))
		{
		    sbFilters.append("  AND " + objUtility.funGetSelectedPOSCodeString("d.strPOSCode", selectedPOSCodeSet) + " and c.strUserCreated='" + cmbOperator.getSelectedItem().toString() + "' ");
		}
		else if (!pos.equals("All") && cmbOperator.getSelectedItem().equals("All"))
		{
		    sbFilters.append(" AND  " + objUtility.funGetSelectedPOSCodeString("d.strPOSCode", selectedPOSCodeSet) + " ");
		}
		else if (pos.equals("All") && !cmbOperator.getSelectedItem().equals("All"))
		{
		    sbFilters.append("  and c.strUserCreated='" + cmbOperator.getSelectedItem().toString() + "'");
		}

		if (clsGlobalVarClass.gEnableShiftYN && (!cmbShift.getSelectedItem().toString().equalsIgnoreCase("All")))
		{
		    sbFilters.append(" AND c.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ");
		}
		if (txtBillNofrom.getText().trim().length() == 0 && txtBillnoTo.getText().trim().length() == 0)
		{
		}
		else
		{
		    sbFilters.append(" and a.strBillNo between '" + txtBillNofrom.getText() + "' and '" + txtBillnoTo.getText() + "'");
		}
		if (!cmbPayMode.getSelectedItem().toString().equalsIgnoreCase("All"))
		{
		    sbFilters.append(" and b.strSettelmentDesc='" + cmbPayMode.getSelectedItem().toString() + "' ");
		}
		if (!cmbArea.getSelectedItem().equals("All"))
		{
		    sbFilters.append(" and c.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ");
		}
		if (!cmbOperationType.getSelectedItem().toString().equalsIgnoreCase("All"))
		{
		    sbFilters.append(" and c.strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ");
		}
		if (!txtCustomerCode.getText().equalsIgnoreCase(""))
		{
		    sbFilters.append(" and a.strCustomerCode='" + txtCustomerCode.getText() + "' ");
		}

		if (chkConsolidatePOS.isSelected())
		{
		    sbFilters.append(" GROUP BY b.strSettelmentDesc ");
		}
		else
		{
		    sbFilters.append(" GROUP BY b.strSettelmentDesc, d.strPosCode");
		}

		sbLive.append(" ").append(sbFilters);
		sbQFile.append(" ").append(sbFilters);

//                System.out.println("Settlement Wise Report L=" + sbLive);
//                System.out.println("Settlement Wise Report Q=" + sbQFile);
		boolean flgRecords = false;
		if (!flgRecords)
		{
		    dm.setRowCount(0);
		    dm1.setRowCount(0);
		}

		mapPOSDtlForSettlement = new LinkedHashMap<String, List<Map<String, clsBillSettlementDtl>>>();
		//for live data
		ResultSet liveResultSet = clsGlobalVarClass.dbMysql.executeResultSet(sbLive.toString());
		funGenerateSettlementWiseSales(liveResultSet);
		//for day end data
		ResultSet qResultSet = clsGlobalVarClass.dbMysql.executeResultSet(sbQFile.toString());
		funGenerateSettlementWiseSales(qResultSet);

		if (cmbType.getSelectedItem().equals("Chart"))
		{
		    try
		    {
			pnlSalesData.setVisible(false);
			pnlSalesTotal.setVisible(false);

			ArrayList<String> arrListGraphData = new ArrayList<>();
			double totalAmt = 0;
			Iterator<Map.Entry<String, List<Map<String, clsBillSettlementDtl>>>> it = mapPOSDtlForSettlement.entrySet().iterator();
			while (it.hasNext())
			{
			    Map.Entry<String, List<Map<String, clsBillSettlementDtl>>> entry = it.next();
			    List<Map<String, clsBillSettlementDtl>> listOfSettlement = entry.getValue();
			    String posName = "";
			    String settName = "";
			    for (int i = 0; i < listOfSettlement.size(); i++)
			    {
				double settTotal = listOfSettlement.get(i).entrySet().iterator().next().getValue().getDblSettlementAmt();
				posName = listOfSettlement.get(i).entrySet().iterator().next().getValue().getPosName();
				settName = listOfSettlement.get(i).entrySet().iterator().next().getValue().getStrSettlementName();

				arrListGraphData.add(posName + "#" + settName + "#" + settTotal);

				totalAmt = totalAmt + settTotal;
			    }
			}

			if (chkConsolidatePOS.isSelected())
			{
			    funGenerateGraph("pieChart", arrListGraphData, totalAmt, true);
			}
			else
			{
			    funGenerateGraph("pieChart", arrListGraphData, totalAmt, false);
			}

			pnlSalesData.setVisible(false);
			pnlSalesTotal.setVisible(false);
			BufferedImage image = null;
			image = ImageIO.read(new File("Graph"));
			pnlGraph.setVisible(true);
			pnlGraph.setBackground(Color.white);
			lblName.setBackground(Color.white);
			pnlGraph.add(lblName);
			lblName.setIcon((new javax.swing.ImageIcon(image)));

		    }
		    catch (Exception ex)
		    {
			ex.printStackTrace();
		    }
		}
		else
		{
		    Iterator<Map.Entry<String, List<Map<String, clsBillSettlementDtl>>>> it = mapPOSDtlForSettlement.entrySet().iterator();
		    while (it.hasNext())
		    {
			Map.Entry<String, List<Map<String, clsBillSettlementDtl>>> entry = it.next();
			List<Map<String, clsBillSettlementDtl>> listOfSettelment = entry.getValue();
			for (int i = 0; i < listOfSettelment.size(); i++)
			{
			    clsBillSettlementDtl objSettlementDtl = listOfSettelment.get(i).entrySet().iterator().next().getValue();
			    if (chkConsolidatePOS.isSelected())
			    {
				//records[0] = saleSet.getString(1);
				records[0] = objSettlementDtl.getStrSettlementName();
				records[1] = objSettlementDtl.getDblSettlementAmt();
				if (!objSettlementDtl.getStrSettlementType().equals("Complementary"))
				{
				    temp1 = temp1.add(new BigDecimal(records[1].toString()));
				}
				dm.addRow(records);
			    }
			    else
			    {
				records[0] = objSettlementDtl.getPosName();
				records[1] = objSettlementDtl.getStrSettlementName();
				records[2] = gDecimalFormat.format(objSettlementDtl.getDblSettlementAmt());
				if (!objSettlementDtl.getStrSettlementType().equals("Complementary"))
				{
				    temp1 = temp1.add(new BigDecimal(records[2].toString()));
				}
				dm.addRow(records);
			    }
			}
		    }

		    Object[] ob1
			    =
			    {
				"Total", gDecimalFormat.format(temp1)
			    };
		    dm1.addRow(ob1);

		    tblTotal.setModel(dm1);
		    tblSales.setModel(dm);

		    double totalSale = 0;
		    if (tblTotal.getRowCount() > 0)
		    {
			totalSale = Double.parseDouble(tblTotal.getValueAt(0, 1).toString());
		    }

		    for (int row = 0; row < tblSales.getRowCount(); row++)
		    {
			double saleAmt;
			if (chkConsolidatePOS.isSelected())
			{
			    saleAmt = Double.parseDouble(tblSales.getValueAt(row, 1).toString());
			    salePer = (saleAmt / totalSale) * 100;
			    tblSales.setValueAt(gDecimalFormat.format(salePer), row, 2);
			}
			else
			{
			    saleAmt = Double.parseDouble(tblSales.getValueAt(row, 2).toString());
			    salePer = (saleAmt / totalSale) * 100;
			    tblSales.setValueAt(gDecimalFormat.format(salePer), row, 3);
			}
		    }

		    tblSales.setSize(400, 400);
		    tblSales.setRowHeight(40);
		    tblTotal.setRowHeight(40);
		    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		    tblSales.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
		    if (chkConsolidatePOS.isSelected() == false)
		    {
			tblSales.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		    }
		    tblSales.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		    tblTotal.setSize(400, 400);
		    DefaultTableCellRenderer rightRenderer1 = new DefaultTableCellRenderer();
		    rightRenderer1.setHorizontalAlignment(JLabel.RIGHT);
		    tblTotal.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
		    tblTotal.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}

    }

    /**
     * Home Delivery Wise Report
     */
    private void funHomeDeliveryWise()
    {
	StringBuilder sbSqlLive = new StringBuilder();
	StringBuilder sbSqlQFile = new StringBuilder();
	try
	{
	    sql = "";
	    reportName = "Home Delivery Wise Sales Report";
	    totalQty = new Double("0.00");
	    totalAmount = new BigDecimal("0.00");
	    temp = new BigDecimal("0.00");
	    temp1 = new BigDecimal("0.00");
	    BigDecimal sumDisc = new BigDecimal("0.00");
	    BigDecimal sumtax = new BigDecimal("0.00");
	    dm = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    return false;
		}
	    };

	    dm.addColumn("Bill No");
	    dm.addColumn("POS");
	    dm.addColumn("Date");
	    dm.addColumn("Settle Mode");
	    dm.addColumn("Delivery Charges");
	    dm.addColumn("Disc Amt");
	    dm.addColumn("Tax Amt");
	    dm.addColumn("Amount");
	    dm.addColumn("Customer Name");
	    dm.addColumn("Bulding");
	    dm.addColumn("Delv Boy");
	    dm.getDataVector().removeAllElements();

	    vSalesReportExcelColLength = new java.util.Vector();
	    vSalesReportExcelColLength.add("6#Left"); //Bill No
	    vSalesReportExcelColLength.add("6#Left"); //POS
	    vSalesReportExcelColLength.add("6#Left"); //Date
	    vSalesReportExcelColLength.add("10#Left"); //Settle Mode
	    vSalesReportExcelColLength.add("6#Right"); //Del Charges
	    vSalesReportExcelColLength.add("6#Right"); //Disc Amt
	    vSalesReportExcelColLength.add("6#Right"); //Tax Amt
	    vSalesReportExcelColLength.add("6#Right"); //Amt
	    vSalesReportExcelColLength.add("10#Right"); //Cust Name
	    vSalesReportExcelColLength.add("10#Right"); //Building
	    vSalesReportExcelColLength.add("10#Right"); //Del Boy

	    DefaultTableModel dm1 = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    //all cells false
		    return false;
		}
	    };
	    dm1.addColumn("");
	    dm1.addColumn("Disc Amt");
	    dm1.addColumn("Tax Amt");
	    dm1.addColumn("Amount");
	    dm1.addColumn("");
	    dm1.getDataVector().removeAllElements();
	    Date dt1 = dteFromDate.getDate();
	    Date dt2 = dteToDate.getDate();

	    if ((dt2.getTime() - dt1.getTime()) < 0)
	    {
		new frmOkPopUp(null, "Invalid date", "Error", 1).setVisible(true);
	    }
	    else
	    {
		fromDate = funGetFromDate();
		toDate = funGetToDate();
		records = new Object[11];

		sbSqlLive.setLength(0);
		sbSqlQFile.setLength(0);

		String pos = funGetSelectedPosCode();
		sbSqlLive.append("SELECT ifnull(a.strBillNo,''),ifnull(f.strPosName,''),ifnull(DATE_FORMAT(date(b.dteBillDate),'%d-%m-%Y'),''),ifnull(b.strSettelmentMode,'') "
			+ " ,ifnull(b.dblDeliveryCharges,0) ,ifnull(b.dblDiscountAmt,0),ifnull(b.dblTaxAmt,0),ifnull(b.dblGrandTotal,0) ,"
			+ " ifnull(c.strCustomerName,''),ifnull(e.strBuildingName,''),ifnull(d.strDPName,''),'" + clsGlobalVarClass.gUserCode + "' "
			+ " FROM tblhomedelivery a INNER JOIN tblbillhd b ON a.strBillNo = b.strBillNo "
			+ " INNER JOIN tblcustomermaster c ON a.strCustomerCode = c.strCustomerCode "
			+ " left OUTER Join tbldeliverypersonmaster d on a.strDPCode=d.strDPCode "
			+ " left OUTER Join tblbuildingmaster e on e.strBuildingCode=c.strBuldingCode"
			+ " left outer join tblposmaster f on b.strPOSCode=f.strPosCode "
			+ " WHERE date( b.dteBillDate ) BETWEEN '" + fromDate + "' AND '" + toDate + "'");
		if (!pos.equals("All"))
		{
		    sbSqlLive.append(" AND " + objUtility.funGetSelectedPOSCodeString("b.strPOSCode", selectedPOSCodeSet) + " ");
		}
		if (clsGlobalVarClass.gEnableShiftYN && (!cmbShift.getSelectedItem().toString().equalsIgnoreCase("All")))
		{
		    sbSqlLive.append(" AND b.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ");
		}
		if (!cmbArea.getSelectedItem().equals("All"))
		{
		    sbSqlLive.append(" and b.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ");
		}
		if (!cmbOperationType.getSelectedItem().toString().equalsIgnoreCase("All"))
		{
		    sbSqlLive.append(" and b.strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ");
		}
		sbSqlQFile.append("SELECT ifnull(a.strBillNo,''),ifnull(f.strPosName,''),ifnull(DATE_FORMAT(date(b.dteBillDate),'%d-%m-%Y'),''),ifnull(b.strSettelmentMode,'') "
			+ " ,ifnull(b.dblDeliveryCharges,0) ,ifnull(b.dblDiscountAmt,0),ifnull(b.dblTaxAmt,0),ifnull(b.dblGrandTotal,0) ,"
			+ " ifnull(c.strCustomerName,''),ifnull(e.strBuildingName,''),ifnull(d.strDPName,''),'" + clsGlobalVarClass.gUserCode + "' "
			+ " FROM tblhomedelivery a INNER JOIN tblqbillhd b ON a.strBillNo = b.strBillNo "
			+ " INNER JOIN tblcustomermaster c ON a.strCustomerCode = c.strCustomerCode "
			+ " left OUTER Join tbldeliverypersonmaster d on a.strDPCode=d.strDPCode "
			+ " left OUTER Join tblbuildingmaster e on e.strBuildingCode=c.strBuldingCode "
			+ " left outer join tblposmaster f on b.strPOSCode=f.strPosCode "
			+ " WHERE date( b.dteBillDate ) BETWEEN '" + fromDate + "' AND '" + toDate + "'");
		if (!pos.equals("All"))
		{
		    sbSqlQFile.append(" AND " + objUtility.funGetSelectedPOSCodeString("b.strPOSCode", selectedPOSCodeSet) + " ");
		}
		if (clsGlobalVarClass.gEnableShiftYN && (!cmbShift.getSelectedItem().toString().equalsIgnoreCase("All")))
		{
		    sbSqlQFile.append(" AND b.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ");
		}
		if (!cmbArea.getSelectedItem().equals("All"))
		{
		    sbSqlQFile.append(" and b.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ");
		}
		if (!cmbOperationType.getSelectedItem().toString().equalsIgnoreCase("All"))
		{
		    sbSqlLive.append(" and b.strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ");
		}
		if (!txtCustomerCode.getText().equalsIgnoreCase(""))
		{
		    sbSqlLive.append(" and a.strCustomerCode='" + txtCustomerCode.getText() + "' ");

		}

//                System.out.println(sbSqlLive);
//                System.out.println(sbSqlQFile);
		boolean flgResult = false;
		if (!flgResult)
		{
		    dm.setRowCount(0);
		    dm1.setRowCount(0);
		}

		DecimalFormat decFormat = new DecimalFormat("0");

		//for live data
		ResultSet rsHDWiseSales = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlLive.toString());
		while (rsHDWiseSales.next())
		{
		    flgResult = true;
		    records[0] = rsHDWiseSales.getString(1);//bilNo
		    records[1] = rsHDWiseSales.getString(2);//posName
		    records[2] = rsHDWiseSales.getString(3);//billDate
		    records[3] = rsHDWiseSales.getString(4);//settleMode
		    records[4] = gDecimalFormat.format(rsHDWiseSales.getDouble(5));//delCharges //Change getString to getDouble.. for Format Exception
		    records[5] = gDecimalFormat.format(rsHDWiseSales.getDouble(6));//disc
		    records[6] = gDecimalFormat.format(rsHDWiseSales.getDouble(7));//taxAmt
		    records[7] = gDecimalFormat.format(rsHDWiseSales.getDouble(8));//totalAmt
		    records[8] = rsHDWiseSales.getString(9);//custName
		    records[9] = rsHDWiseSales.getString(10);//address
		    records[10] = rsHDWiseSales.getString(11);//delBoy
		    sumDisc = sumDisc.add(new BigDecimal(records[5].toString()));
		    sumtax = sumtax.add(new BigDecimal(records[6].toString()));
		    temp1 = temp1.add(new BigDecimal(records[7].toString()));
		    dm.addRow(records);
		}
		rsHDWiseSales.close();
		//for day end data
		rsHDWiseSales = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlQFile.toString());
		while (rsHDWiseSales.next())
		{
		    flgResult = true;
		    records[0] = rsHDWiseSales.getString(1);//bilNo
		    records[1] = rsHDWiseSales.getString(2);//posName
		    records[2] = rsHDWiseSales.getString(3);//billDate
		    records[3] = rsHDWiseSales.getString(4);//settleMode
		    records[4] = gDecimalFormat.format(rsHDWiseSales.getDouble(5));//delCharges
		    records[5] = gDecimalFormat.format(rsHDWiseSales.getDouble(6));//disc
		    records[6] = gDecimalFormat.format(rsHDWiseSales.getDouble(7));//taxAmt
		    records[7] = gDecimalFormat.format(rsHDWiseSales.getDouble(8));//totalAmt
		    records[8] = rsHDWiseSales.getString(9);//custName
		    records[9] = rsHDWiseSales.getString(10);//address
		    records[10] = rsHDWiseSales.getString(11);//delBoy
		    sumDisc = sumDisc.add(new BigDecimal(records[5].toString()));
		    sumtax = sumtax.add(new BigDecimal(records[6].toString()));
		    temp1 = temp1.add(new BigDecimal(records[7].toString()));
		    dm.addRow(records);
		}
		rsHDWiseSales.close();

		Object[] ob1
			=
			{
			    "Total", gDecimalFormat.format(sumDisc), gDecimalFormat.format(sumtax), gDecimalFormat.format(temp1), ""
			};
		dm1.addRow(ob1);

		tblTotal.setModel(dm1);
		tblSales.setModel(dm);
		tblSales.setSize(400, 400);
		tblSales.setRowHeight(40);
		tblTotal.setRowHeight(40);
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		tblSales.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
		tblSales.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
		tblSales.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
		tblSales.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblSales.getColumnModel().getColumn(0).setPreferredWidth(70);
		tblSales.getColumnModel().getColumn(1).setPreferredWidth(70);
		tblSales.getColumnModel().getColumn(2).setPreferredWidth(152);
		tblSales.getColumnModel().getColumn(3).setPreferredWidth(80);
		tblSales.getColumnModel().getColumn(4).setPreferredWidth(150);
		tblSales.getColumnModel().getColumn(5).setPreferredWidth(60);
		tblSales.getColumnModel().getColumn(6).setPreferredWidth(60);
		tblSales.getColumnModel().getColumn(7).setPreferredWidth(100);
		tblSales.getColumnModel().getColumn(8).setPreferredWidth(150);
		tblSales.getColumnModel().getColumn(9).setPreferredWidth(70);
		tblSales.getColumnModel().getColumn(10).setPreferredWidth(70);

		tblTotal.setSize(400, 400);
		DefaultTableCellRenderer rightRenderer1 = new DefaultTableCellRenderer();
		rightRenderer1.setHorizontalAlignment(JLabel.RIGHT);
		tblTotal.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
		tblTotal.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
		tblTotal.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		tblTotal.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblTotal.getColumnModel().getColumn(0).setPreferredWidth(420);
		tblTotal.getColumnModel().getColumn(1).setPreferredWidth(62);
		tblTotal.getColumnModel().getColumn(2).setPreferredWidth(62);
		tblTotal.getColumnModel().getColumn(3).setPreferredWidth(62);
		tblTotal.getColumnModel().getColumn(4).setPreferredWidth(184);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    /**
     * Table Wise Sales Report
     */
    private void funTableWise()
    {
	try
	{
	    sql = "";
	    reportName = "Table Wise Sales Report";
	    totalQty = new Double("0.00");
	    totalAmount = new BigDecimal("0.00");
	    temp = new BigDecimal("0.00");
	    temp1 = new BigDecimal("0.00");
	    dm = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    return false;
		}
	    };
	    dm.addColumn("POS");
	    dm.addColumn("Table Name");
	    dm.addColumn("No. Of Bill");
	    dm.addColumn("Sales Amount");

	    vSalesReportExcelColLength = new java.util.Vector();
	    vSalesReportExcelColLength.add("6#Left"); //POS
	    vSalesReportExcelColLength.add("8#Left"); //Table
	    vSalesReportExcelColLength.add("6#Right"); //Amt
	    vSalesReportExcelColLength.add("6#Right"); //Amt

	    DefaultTableModel dm1 = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    return false;
		}
	    };
	    dm1.addColumn("");
	    dm1.addColumn("Sales Amount");
	    Date dt1 = dteFromDate.getDate();
	    Date dt2 = dteToDate.getDate();

	    if ((dt2.getTime() - dt1.getTime()) < 0)
	    {
		new frmOkPopUp(null, "Invalid date", "Error", 1).setVisible(true);
	    }
	    else
	    {
		fromDate = funGetFromDate();
		toDate = funGetToDate();
		records = new Object[4];
		String pos = funGetSelectedPosCode();

		String sqlQFile = "select c.strPOSName,b.strTableName,'0',SUM(d.dblSettlementAmt),count(*)"
			+ ",'" + clsGlobalVarClass.gPOSCode + "','" + clsGlobalVarClass.gUserCode + "','0' ,'ND','ND',a.strTableNo "
			+ " from tblqbillhd a,tbltablemaster b,tblposmaster c,tblqbillsettlementdtl d "
			+ " where date( a.dteBillDate ) BETWEEN '" + fromDate + "' AND '" + toDate + "' "
			+ " and a.strTableNo=b.strTableNo "
			+ " and a.strPOSCode=c.strPOSCode"
			+ " and a.strBillNo=d.strBillNo "
			+ " and date(a.dteBillDate)=date(d.dteBillDate) "
			+ " and a.strClientCode=d.strClientCode ";

		String sqlLiveTables = "select c.strPOSName,b.strTableName,'0',SUM(d.dblSettlementAmt),count(*)"
			+ ",'" + clsGlobalVarClass.gPOSCode + "','" + clsGlobalVarClass.gUserCode + "','0' ,'ND','ND',a.strTableNo "
			+ " from tblbillhd a,tbltablemaster b,tblposmaster c,tblbillsettlementdtl d "
			+ " where date( a.dteBillDate ) BETWEEN '" + fromDate + "' AND '" + toDate + "' "
			+ " and a.strTableNo=b.strTableNo "
			+ " and a.strPOSCode=c.strPOSCode "
			+ " and a.strBillNo=d.strBillNo "
			+ " and date(a.dteBillDate)=date(d.dteBillDate) "
			+ " and a.strClientCode=d.strClientCode ";

		if (!pos.equals("All"))
		{
		    sqlQFile += " AND " + objUtility.funGetSelectedPOSCodeString("a.strPOSCode", selectedPOSCodeSet) + " ";
		    sqlLiveTables += " AND " + objUtility.funGetSelectedPOSCodeString("a.strPOSCode", selectedPOSCodeSet) + " ";
		}
		if (clsGlobalVarClass.gEnableShiftYN && (!cmbShift.getSelectedItem().toString().equalsIgnoreCase("All")))
		{
		    sqlLiveTables += " AND a.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ";
		    sqlQFile += " AND a.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ";
		}
		if (!cmbArea.getSelectedItem().equals("All"))
		{
		    sqlLiveTables += " and a.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ";
		    sqlQFile += " and a.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ";
		}
		if (!cmbOperationType.getSelectedItem().toString().equalsIgnoreCase("All"))
		{
		    sqlLiveTables += " and a.strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ";
		    sqlQFile += " and a.strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ";
		}
		if (!txtCustomerCode.getText().equalsIgnoreCase(""))
		{
		    sqlLiveTables += " and a.strCustomerCode='" + txtCustomerCode.getText() + "' ";
		    sqlQFile += " and a.strCustomerCode='" + txtCustomerCode.getText() + "' ";
		}
		sqlQFile += " group by a.strTableNo ";
		sqlLiveTables += " group by a.strTableNo ";

//                System.out.println("live->"+sqlLiveTables);
//                System.out.println("Q->"+sqlQFile);
		boolean flgRecords = false;
		if (!flgRecords)
		{
		    dm.setRowCount(0);
		    dm1.setRowCount(0);
		}
		mapPOSTableWiseSales = new LinkedHashMap<String, Map<String, clsCommonBeanDtl>>();

		ResultSet rsTableWiseSales = clsGlobalVarClass.dbMysql.executeResultSet(sqlLiveTables.toString());
		funGenerateTableWiseSales(rsTableWiseSales);
		rsTableWiseSales = clsGlobalVarClass.dbMysql.executeResultSet(sqlQFile.toString());
		funGenerateTableWiseSales(rsTableWiseSales);

		DecimalFormat decFormat = new DecimalFormat("0");

		Iterator<Map.Entry<String, Map<String, clsCommonBeanDtl>>> posIterator = mapPOSTableWiseSales.entrySet().iterator();
		while (posIterator.hasNext())
		{
		    Map<String, clsCommonBeanDtl> mapTblDtl = posIterator.next().getValue();
		    Iterator<Map.Entry<String, clsCommonBeanDtl>> tblIterator = mapTblDtl.entrySet().iterator();
		    while (tblIterator.hasNext())
		    {
			clsCommonBeanDtl objTblDtl = tblIterator.next().getValue();
			records[0] = objTblDtl.getPosName();
			records[1] = objTblDtl.getTableName();
			records[2] = decFormat.format(objTblDtl.getNoOfBills());
			records[3] = gDecimalFormat.format(objTblDtl.getSaleAmount());
			temp1 = temp1.add(new BigDecimal(records[3].toString()));
			dm.addRow(records);
		    }
		}

		Object[] ob1 =
		{
		    "Total", gDecimalFormat.format(temp1)
		};
		dm1.addRow(ob1);

		tblTotal.setModel(dm1);
		tblSales.setModel(dm);
		tblSales.setSize(400, 400);
		tblSales.setRowHeight(40);
		tblTotal.setRowHeight(40);
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		tblSales.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
		tblSales.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		tblSales.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblSales.getColumnModel().getColumn(0).setPreferredWidth(260);
		tblSales.getColumnModel().getColumn(1).setPreferredWidth(250);
		tblSales.getColumnModel().getColumn(2).setPreferredWidth(140);
		tblSales.getColumnModel().getColumn(3).setPreferredWidth(142);
		tblTotal.setSize(400, 400);
		DefaultTableCellRenderer rightRenderer1 = new DefaultTableCellRenderer();
		rightRenderer1.setHorizontalAlignment(JLabel.RIGHT);
		tblTotal.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
		tblTotal.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblTotal.getColumnModel().getColumn(0).setPreferredWidth(630);
		tblTotal.getColumnModel().getColumn(1).setPreferredWidth(160);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    /**
     * Area Wise Report
     */
    private void funAreaWise()
    {
	try
	{
	    sql = "";
	    reportName = "Area Wise Sales Report";
	    totalQty = new Double("0.00");
	    totalAmount = new BigDecimal("0.00");
	    temp = new BigDecimal("0.00");
	    temp1 = new BigDecimal("0.00");
	    dm = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    return false;
		}
	    };
	    dm.addColumn("POS");
	    dm.addColumn("Area Name");
	    dm.addColumn("Sales Amount");

	    vSalesReportExcelColLength = new java.util.Vector();
	    vSalesReportExcelColLength.add("6#Left");
	    vSalesReportExcelColLength.add("6#Left");
	    vSalesReportExcelColLength.add("6#Left");

	    DefaultTableModel dm1 = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    return false;
		}
	    };
	    dm1.addColumn("");
	    dm1.addColumn("Sales Amount");
	    Date dt1 = dteFromDate.getDate();
	    Date dt2 = dteToDate.getDate();
	    if ((dt2.getTime() - dt1.getTime()) < 0)
	    {
		new frmOkPopUp(null, "Invalid date", "Error", 1).setVisible(true);
	    }
	    else
	    {
		fromDate = funGetFromDate();
		toDate = funGetToDate();
		funGetFromTime();
		funGetToTime();
		String DateFrom = null, field = null, DateTo = null;
		if (funGetFromTime() != null)
		{
		    DateFrom = fromDate + " " + funGetFromTime();
		    field = "a.dteBillDate";
		}
		else
		{
		    DateFrom = fromDate;
		    field = "date(a.dteBillDate)";
		}
		if (funGetToTime() != null)
		{
		    DateTo = toDate + " " + funGetToTime();
		}
		else
		{
		    DateTo = toDate;
		}

		records = new Object[3];
		String pos = funGetSelectedPosCode();

		String sqlQFile = "select d.strPosName,c.strAreaName,'0', SUM(b.dblSettlementAmt),'" + clsGlobalVarClass.gPOSCode + "' "
			+ " ,'" + clsGlobalVarClass.gUserCode + "','0','ND','ND',a.strPosCode,a.strAreaCode "
			+ " from tblqbillhd a,tblqbillsettlementdtl b,tblareamaster c,tblposmaster d "
			+ " where " + field + " between '" + DateFrom + "' and '" + DateTo + "' "
			+ " and a.strBillNo=b.strBillNo "
			+ " and date(a.dteBillDate)=date(b.dteBillDate) "
			+ " and a.strClientCode=b.strClientCode "
			+ " and a.strAreaCode=c.strAreaCode "
			+ " and a.strPOSCode=d.strPosCode ";

		String sqlLive = "select d.strPosName,c.strAreaName,'0', SUM(b.dblSettlementAmt),'" + clsGlobalVarClass.gPOSCode + "' "
			+ " ,'" + clsGlobalVarClass.gUserCode + "','0','ND','ND',a.strPosCode,a.strAreaCode "
			+ " from tblbillhd a,tblbillsettlementdtl b,tblareamaster c,tblposmaster d "
			+ " where " + field + " between '" + DateFrom + "' and '" + DateTo + "' "
			+ " and a.strBillNo=b.strBillNo "
			+ " and date(a.dteBillDate)=date(b.dteBillDate) "
			+ " and a.strClientCode=b.strClientCode "
			+ " and a.strAreaCode=c.strAreaCode and a.strPOSCode=d.strPosCode ";

		if (!pos.equals("All"))
		{
		    sqlQFile += " and a.strPOSCode = '" + pos + "' ";
		    sqlLive += " and  a.strPOSCode = '" + pos + "' ";
		}
		if (clsGlobalVarClass.gEnableShiftYN && (!cmbShift.getSelectedItem().toString().equalsIgnoreCase("All")))
		{
		    sqlQFile += " AND a.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ";
		    sqlLive += " AND a.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ";
		}
		if (!cmbArea.getSelectedItem().equals("All"))
		{
		    sqlLive += " and a.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ";
		    sqlQFile += " and a.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ";
		}
		if (!cmbOperationType.getSelectedItem().toString().equalsIgnoreCase("All"))
		{
		    sqlLive += " and a.strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ";
		    sqlQFile += " and a.strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ";
		}

		sqlQFile += " group by a.strAreaCode ";
		sqlLive += " group by a.strAreaCode ";

//                System.out.println("live->"+sqlLive);
//                System.out.println("q->"+sqlQFile);
		boolean flgRecords = false;
		if (!flgRecords)
		{
		    dm.setRowCount(0);
		    dm1.setRowCount(0);
		}

		mapPOSAreaWiseSales = new LinkedHashMap<String, Map<String, clsCommonBeanDtl>>();
		ResultSet rsAreaWise = clsGlobalVarClass.dbMysql.executeResultSet(sqlLive.toString());
		funGenerateAreaWiseSales(rsAreaWise);
		rsAreaWise = clsGlobalVarClass.dbMysql.executeResultSet(sqlQFile.toString());
		funGenerateAreaWiseSales(rsAreaWise);

		Iterator<Map.Entry<String, Map<String, clsCommonBeanDtl>>> posIterator = mapPOSAreaWiseSales.entrySet().iterator();
		while (posIterator.hasNext())
		{
		    Map<String, clsCommonBeanDtl> mapAreaDtl = posIterator.next().getValue();
		    Iterator<Map.Entry<String, clsCommonBeanDtl>> areaIterator = mapAreaDtl.entrySet().iterator();
		    while (areaIterator.hasNext())
		    {
			clsCommonBeanDtl objAreaDtl = areaIterator.next().getValue();

			records[0] = objAreaDtl.getPosName();
			records[1] = objAreaDtl.getAreaName();
			records[2] = gDecimalFormat.format(objAreaDtl.getSaleAmount());
			temp1 = temp1.add(new BigDecimal(records[2].toString()));
			dm.addRow(records);
		    }
		}

		Object[] ob1
			=
			{
			    "Total", gDecimalFormat.format(temp1)
			};
		dm1.addRow(ob1);

		tblTotal.setModel(dm1);
		tblSales.setModel(dm);
		tblSales.setSize(400, 400);
		tblSales.setRowHeight(40);
		tblTotal.setRowHeight(40);
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		tblSales.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
		tblSales.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblSales.getColumnModel().getColumn(0).setPreferredWidth(322);
		tblSales.getColumnModel().getColumn(1).setPreferredWidth(300);
		tblSales.getColumnModel().getColumn(2).setPreferredWidth(160);
		tblTotal.setSize(400, 400);
		DefaultTableCellRenderer rightRenderer1 = new DefaultTableCellRenderer();
		rightRenderer1.setHorizontalAlignment(JLabel.RIGHT);
		tblTotal.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
		tblTotal.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblTotal.getColumnModel().getColumn(0).setPreferredWidth(630);
		tblTotal.getColumnModel().getColumn(1).setPreferredWidth(160);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    /**
     * Waiter Wise Report
     */
    private void funWaiterWise()
    {
	StringBuilder sbSqlLive = new StringBuilder();
	StringBuilder sbSqlQFile = new StringBuilder();

	try
	{
	    sql = "";
	    reportName = "Waiter Wise Sales Report";
	    totalQty = new Double("0.00");
	    totalAmount = new BigDecimal("0.00");
	    temp = new BigDecimal("0.00");
	    temp1 = new BigDecimal("0.00");
	    dm = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    return false;
		}
	    };
	    dm.addColumn("POS");
	    dm.addColumn("Waiter Short Name");
	    dm.addColumn("Waiter Full Name");
	    dm.addColumn("No Of Bills");
	    dm.addColumn("Sales Amount");

	    vSalesReportExcelColLength = new java.util.Vector();
	    vSalesReportExcelColLength.add("6#Left"); //pos
	    vSalesReportExcelColLength.add("6#Left"); //shortname
	    vSalesReportExcelColLength.add("6#Left"); //fullName
	    vSalesReportExcelColLength.add("6#Left"); //Bills
	    vSalesReportExcelColLength.add("10#Left"); //salesAmount

	    DefaultTableModel dm1 = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    return false;
		}
	    };
	    dm1.addColumn("");
	    dm1.addColumn("Sales Amount");
	    Date dt1 = dteFromDate.getDate();
	    Date dt2 = dteToDate.getDate();

	    if ((dt2.getTime() - dt1.getTime()) < 0)
	    {
		new frmOkPopUp(null, "Invalid date", "Error", 1).setVisible(true);
	    }
	    else
	    {
		fromDate = funGetFromDate();
		toDate = funGetToDate();

		records = new Object[5];
		String pos = funGetSelectedPosCode();

		sbSqlLive.setLength(0);
		sbSqlQFile.setLength(0);

		sbSqlLive.append("select c.strPosName,b.strWShortName,b.strWFullName"
			+ ",SUM(d.dblSettlementAmt),count(*),'" + clsGlobalVarClass.gUserCode + "',b.strWaiterNo,c.strPosCode "
			+ " from tblbillhd a,tblwaitermaster b, tblposmaster c,tblbillsettlementdtl d "
			+ " where a.strWaiterNo=b.strWaiterNo "
			+ " and a.strPOSCode=c.strPosCode "
			+ " and date( a.dteBillDate ) BETWEEN '" + fromDate + "' AND '" + toDate + "' "
			+ " and a.strBillNo=d.strBillNo "
			+ " and date(a.dteBillDate)=date(d.dteBillDate) "
			+ " and a.strClientCode=d.strClientCode ");

		sbSqlQFile.append("select c.strPosName,b.strWShortName,b.strWFullName"
			+ ",SUM(d.dblSettlementAmt),count(*),'" + clsGlobalVarClass.gUserCode + "',b.strWaiterNo,c.strPosCode "
			+ " from tblqbillhd a,tblwaitermaster b, tblposmaster c,tblqbillsettlementdtl d "
			+ " where a.strWaiterNo=b.strWaiterNo "
			+ " and a.strPOSCode=c.strPosCode "
			+ " and date( a.dteBillDate ) BETWEEN '" + fromDate + "' AND '" + toDate + "'"
			+ " and a.strBillNo=d.strBillNo "
			+ " and date(a.dteBillDate)=date(d.dteBillDate) "
			+ " and a.strClientCode=d.strClientCode ");

		if (!pos.equals("All"))
		{
		    sbSqlLive.append(" and " + objUtility.funGetSelectedPOSCodeString("a.strPOSCode", selectedPOSCodeSet) + " ");

		    sbSqlQFile.append(" and " + objUtility.funGetSelectedPOSCodeString("a.strPOSCode", selectedPOSCodeSet) + " ");
		}
		if (clsGlobalVarClass.gEnableShiftYN && (!cmbShift.getSelectedItem().toString().equalsIgnoreCase("All")))
		{
		    sbSqlLive.append(" AND a.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ");
		    sbSqlQFile.append(" AND a.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ");
		}
		if (!cmbArea.getSelectedItem().equals("All"))
		{
		    sbSqlLive.append(" and a.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ");
		    sbSqlQFile.append(" and a.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ");
		}
		if (!cmbOperationType.getSelectedItem().toString().equalsIgnoreCase("All"))
		{
		    sbSqlLive.append(" and a.strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ");
		    sbSqlQFile.append(" and a.strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ");
		}
		if (!txtCustomerCode.getText().equalsIgnoreCase(""))
		{
		    sbSqlLive.append(" and a.strCustomerCode='" + txtCustomerCode.getText() + "' ");
		    sbSqlQFile.append(" and a.strCustomerCode='" + txtCustomerCode.getText() + "' ");
		}
		sbSqlLive.append(" group by a.strWaiterNo,a.strPOSCode");
		sbSqlLive.append(" order by b.strWFullName,a.strPOSCode");

		sbSqlQFile.append(" group by a.strWaiterNo,a.strPOSCode");
		sbSqlQFile.append(" order by b.strWFullName,a.strPOSCode");

		boolean flgRecords = false;
		if (!flgRecords)
		{
		    dm.setRowCount(0);
		    dm1.setRowCount(0);
		}

		mapPOSWaiterWiseSales = new LinkedHashMap<>();

		ResultSet rsWaiterWise = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlLive.toString());
		funGenerateWaiterWiseSales(rsWaiterWise);
		rsWaiterWise = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlQFile.toString());
		funGenerateWaiterWiseSales(rsWaiterWise);

		DecimalFormat decFormat = new DecimalFormat("0");

		Iterator<Map.Entry<String, Map<String, clsCommonBeanDtl>>> posIterator = mapPOSWaiterWiseSales.entrySet().iterator();
		while (posIterator.hasNext())
		{
		    Map<String, clsCommonBeanDtl> mapWaiterDtl = posIterator.next().getValue();
		    Iterator<Map.Entry<String, clsCommonBeanDtl>> itemIterator = mapWaiterDtl.entrySet().iterator();
		    while (itemIterator.hasNext())
		    {
			clsCommonBeanDtl objWaiterDtl = itemIterator.next().getValue();

			records[0] = objWaiterDtl.getPosName();
			records[1] = objWaiterDtl.getWaiterShortName();
			records[2] = objWaiterDtl.getWaiterFullName();
			records[3] = decFormat.format(objWaiterDtl.getNoOfBills());
			records[4] = gDecimalFormat.format(objWaiterDtl.getSaleAmount());
			temp1 = temp1.add(new BigDecimal(records[4].toString()));
			dm.addRow(records);
		    }
		}

		Object[] ob1
			=
			{
			    "Total", gDecimalFormat.format(temp1)
			};
		dm1.addRow(ob1);

		tblTotal.setModel(dm1);
		tblSales.setModel(dm);
		tblSales.setSize(400, 400);
		tblSales.setRowHeight(40);
		tblTotal.setRowHeight(40);
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		tblSales.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		tblSales.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
		tblSales.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblSales.getColumnModel().getColumn(0).setPreferredWidth(175);
		tblSales.getColumnModel().getColumn(1).setPreferredWidth(175);
		tblSales.getColumnModel().getColumn(2).setPreferredWidth(160);
		tblSales.getColumnModel().getColumn(3).setPreferredWidth(145);
		tblSales.getColumnModel().getColumn(4).setPreferredWidth(145);
		tblTotal.setSize(400, 400);
		DefaultTableCellRenderer rightRenderer1 = new DefaultTableCellRenderer();
		rightRenderer1.setHorizontalAlignment(JLabel.RIGHT);
		tblTotal.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
		tblTotal.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblTotal.getColumnModel().getColumn(0).setPreferredWidth(630);
		tblTotal.getColumnModel().getColumn(1).setPreferredWidth(160);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    sbSqlLive = null;
	    sbSqlQFile = null;
	}
    }

    /**
     * Delivery Boy Wise Report
     */
    private void funDeliveryBoyWiseReport()
    {
	StringBuilder sbSqlLiveBill = new StringBuilder();
	StringBuilder sbSqlQFileBill = new StringBuilder();

	try
	{
	    sql = "";
	    reportName = "Delivery Boy Wise Sales Report";
	    totalAmount = new BigDecimal("0.00");
	    temp = new BigDecimal("0.00");
	    temp1 = new BigDecimal("0.00");
	    dm = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    return false;
		}
	    };
	    dm.addColumn("Delivery Boy Name");
	    dm.addColumn("POS");
	    dm.addColumn("Sales Amount");
	    dm.addColumn("Delivery Charges");

	    vSalesReportExcelColLength = new java.util.Vector();
	    vSalesReportExcelColLength.add("6#Left"); //dbName
	    vSalesReportExcelColLength.add("6#Left"); //pos
	    vSalesReportExcelColLength.add("6#Left"); //salesAmount
	    vSalesReportExcelColLength.add("10#Left"); //delCharges

	    DefaultTableModel dm1 = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    return false;
		}
	    };
	    dm1.addColumn("");
	    dm1.addColumn("");
	    dm1.addColumn("Sales Amount");
	    dm1.addColumn("");
	    Date dt1 = dteFromDate.getDate();
	    Date dt2 = dteToDate.getDate();

	    if ((dt2.getTime() - dt1.getTime()) < 0)
	    {
		new frmOkPopUp(null, "Invalid date", "Error", 1).setVisible(true);
	    }
	    else
	    {
		fromDate = funGetFromDate();
		toDate = funGetToDate();
		records = new Object[6];
		String pos = funGetSelectedPosCode();
		sbSqlLiveBill.setLength(0);
		sbSqlQFileBill.setLength(0);

		sbSqlLiveBill.append("select a.strDPCode,b.strDPName,d.strPOSName"
			+ " ,sum(c.dblGrandTotal),sum(a.dblHomeDeliCharge),'" + clsGlobalVarClass.gUserCode + "',a.strPOSCode "
			+ " from tblhomedelivery a,tbldeliverypersonmaster b,tblbillhd c, tblposmaster d "
			+ " WHERE a.strBillNo=c.strBillNo "
			+ " and date(a.dteDate)=date(c.dteBillDate) "
			+ " and a.strDPCode=b.strDPCode "
			+ " and c.strPOSCode=d.strPOSCode ");

		sbSqlQFileBill.append("select a.strDPCode,b.strDPName,d.strPOSName"
			+ " ,sum(c.dblGrandTotal),sum(a.dblHomeDeliCharge),'" + clsGlobalVarClass.gUserCode + "',a.strPOSCode "
			+ " from tblhomedelivery a,tbldeliverypersonmaster b,tblqbillhd c, tblposmaster d "
			+ " WHERE a.strBillNo=c.strBillNo "
			+ " and date(a.dteDate)=date(c.dteBillDate) "
			+ " and a.strDPCode=b.strDPCode "
			+ " and c.strPOSCode=d.strPOSCode ");

		if (!pos.equals("All"))
		{
		    sbSqlLiveBill.append(" AND " + objUtility.funGetSelectedPOSCodeString("a.strPOSCode", selectedPOSCodeSet) + " ");
		    sbSqlQFileBill.append(" AND " + objUtility.funGetSelectedPOSCodeString("a.strPOSCode", selectedPOSCodeSet) + " ");
		}
		if (clsGlobalVarClass.gEnableShiftYN && (!cmbShift.getSelectedItem().toString().equalsIgnoreCase("All")))
		{
		    sbSqlLiveBill.append(" AND c.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ");
		    sbSqlQFileBill.append(" AND c.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ");
		}
		if (!cmbArea.getSelectedItem().equals("All"))
		{
		    sbSqlLiveBill.append(" and c.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ");
		    sbSqlQFileBill.append(" and c.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ");
		}
		if (!cmbOperationType.getSelectedItem().toString().equalsIgnoreCase("All"))
		{
		    sbSqlLiveBill.append(" and c.strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ");
		    sbSqlQFileBill.append(" and c.strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ");
		}
		if (!txtCustomerCode.getText().equalsIgnoreCase(""))
		{
		    sbSqlLiveBill.append(" and a.strCustomerCode='" + txtCustomerCode.getText() + "' ");
		    sbSqlQFileBill.append(" and a.strCustomerCode='" + txtCustomerCode.getText() + "' ");
		}
		sbSqlLiveBill.append(" and date(a.dteDate) BETWEEN '" + fromDate + "' AND '" + toDate + "'"
			+ " GROUP BY a.strDPCode");
		sbSqlQFileBill.append(" and date(a.dteDate) BETWEEN '" + fromDate + "' AND '" + toDate + "'"
			+ " GROUP BY a.strDPCode");

//                System.out.println("l->"+sbSqlLiveBill);
//                System.out.println("q->"+sbSqlQFileBill);
		double totalAmt = 0;
		boolean flgRecords = false;
		if (!flgRecords)
		{
		    dm.setRowCount(0);
		    dm1.setRowCount(0);
		}

		mapPOSDeliveryBoyWise = new LinkedHashMap<>();

		ResultSet rsDelBoyWaise = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlLiveBill.toString());
		funGenerateDelBoyWiseSales(rsDelBoyWaise);
		rsDelBoyWaise = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlQFileBill.toString());
		funGenerateDelBoyWiseSales(rsDelBoyWaise);

		DecimalFormat decFormat = new DecimalFormat("0");

		Iterator<Map.Entry<String, Map<String, clsCommonBeanDtl>>> posIterator = mapPOSDeliveryBoyWise.entrySet().iterator();
		while (posIterator.hasNext())
		{
		    Map<String, clsCommonBeanDtl> mapDBDtl = posIterator.next().getValue();
		    Iterator<Map.Entry<String, clsCommonBeanDtl>> itemIterator = mapDBDtl.entrySet().iterator();
		    while (itemIterator.hasNext())
		    {
			clsCommonBeanDtl objDBDtl = itemIterator.next().getValue();

			records[0] = objDBDtl.getDbName();
			records[1] = objDBDtl.getPosName();
			records[2] = gDecimalFormat.format(objDBDtl.getSaleAmount());
			records[3] = gDecimalFormat.format(objDBDtl.getDelCharges());

			totalAmt += Double.parseDouble(records[2].toString());
			dm.addRow(records);
		    }
		}

		Object[] ob1 =
		{
		    "Total", "", gDecimalFormat.format(totalAmt), ""
		};
		dm1.addRow(ob1);

		tblTotal.setModel(dm1);
		tblSales.setModel(dm);
		tblSales.setSize(new Dimension(400, 400));
		tblSales.setRowHeight(40);
		tblTotal.setSize(new Dimension(400, 400));
		tblTotal.setRowHeight(40);

		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

		tblSales.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
		tblSales.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		tblSales.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tblSales.getColumnModel().getColumn(0).setPreferredWidth(200);
		tblTotal.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
		tblTotal.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		tblTotal.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tblTotal.getColumnModel().getColumn(0).setPreferredWidth(200);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    sbSqlLiveBill = null;
	    sbSqlQFileBill = null;
	}
    }

    /**
     * Hour wise Report
     */
    private void funHourWiseReport()
    {
	try
	{
	    sql = "";
	    reportName = "Hourly Wise Sales Report";
	    totalAmount = new BigDecimal("0.00");
	    temp = new BigDecimal("0.00");
	    temp1 = new BigDecimal("0.00");
	    dm = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    return false;
		}
	    };
	    dm.addColumn("Date Range");
	    dm.addColumn("No. Of Bill");
	    dm.addColumn("Sales Amount");

	    vSalesReportExcelColLength = new java.util.Vector();
	    vSalesReportExcelColLength.add("6#Left");
	    vSalesReportExcelColLength.add("6#Left");
	    vSalesReportExcelColLength.add("6#Left");

	    DefaultTableModel dm1 = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    return false;
		}
	    };
	    dm1.addColumn("");
	    dm1.addColumn("");
	    dm1.addColumn("Sales Amount");
	    dm1.addColumn("");

	    Date dt1 = dteFromDate.getDate();
	    Date dt2 = dteToDate.getDate();

	    if ((dt2.getTime() - dt1.getTime()) < 0)
	    {
		new frmOkPopUp(null, "Invalid date", "Error", 1).setVisible(true);
	    }
	    else
	    {
		fromDate = funGetFromDate();
		toDate = funGetToDate();
		records = new Object[6];

		String pos = funGetSelectedPosCode();

		String sqlQFile = "select left(right(a.dteDateCreated,8),2),left(right(a.dteDateCreated,8),2) +1"
			+ ",count(*),sum(b.dblSettlementAmt),'" + clsGlobalVarClass.gPOSCode + "'  "
			+ ",'" + clsGlobalVarClass.gUserCode + "','0' ,'ND','ND'  \n"
			+ " from tblqbillhd a,tblqbillsettlementdtl b";

		String sqlLiveTables = "select left(right(a.dteDateCreated,8),2),left(right(a.dteDateCreated,8),2) +1"
			+ ",count(*),sum(b.dblSettlementAmt),'" + clsGlobalVarClass.gPOSCode + "'  "
			+ ",'" + clsGlobalVarClass.gUserCode + "','0' ,'ND','ND'  \n"
			+ " from tblbillhd a,tblbillsettlementdtl b ";

		sqlQFile += " WHERE a.strBillNo=b.strBillNo "
			+ " and date(a.dteBillDate)=date(b.dteBillDate) "
			+ " and a.strClientCode=b.strClientCode ";
		sqlLiveTables += " WHERE a.strBillNo=b.strBillNo and a.strClientCode=b.strClientCode";
		if (!pos.equals("All"))
		{
		    sqlQFile += " and " + objUtility.funGetSelectedPOSCodeString("a.strPOSCode", selectedPOSCodeSet) + " and date(a.dteBillDate) BETWEEN '" + fromDate + "' AND '" + toDate + "' ";
		    sqlLiveTables += " and " + objUtility.funGetSelectedPOSCodeString("a.strPOSCode", selectedPOSCodeSet) + " and date(a.dteBillDate) BETWEEN '" + fromDate + "' AND '" + toDate + "' ";
		}
		if (clsGlobalVarClass.gEnableShiftYN && (!cmbShift.getSelectedItem().toString().equalsIgnoreCase("All")))
		{
		    sqlQFile += " AND a.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' and a.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "'  ";
		    sqlLiveTables += " AND a.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' and a.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "'  ";
		}
		if (!cmbArea.getSelectedItem().equals("All"))
		{
		    sqlQFile += " and a.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ";
		    sqlLiveTables += " and a.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ";
		}
		if (!cmbOperationType.getSelectedItem().toString().equalsIgnoreCase("All"))
		{
		    sqlLiveTables += " and a.strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ";
		    sqlQFile += " and a.strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ";
		}
		if (!txtCustomerCode.getText().equalsIgnoreCase(""))
		{
		    sqlLiveTables += " and a.strCustomerCode='" + txtCustomerCode.getText() + "' ";
		    sqlQFile += " and a.strCustomerCode='" + txtCustomerCode.getText() + "' ";
		}

		sqlLiveTables += " and  date(a.dteBillDate) between '" + fromDate + "' AND '" + toDate + "' ";
		sqlQFile += " and  date(a.dteBillDate) between '" + fromDate + "' AND '" + toDate + "' ";

		sqlLiveTables += " Group By left(right(a.dteDateCreated,8),2)";
		sqlQFile += " Group By left(right(a.dteDateCreated,8),2)";

//                System.out.println("live->"+sqlLiveTables);
//                System.out.println("q->"+sqlQFile);
		boolean flgRecords = false;
		if (!flgRecords)
		{
		    dm.setRowCount(0);
		    dm1.setRowCount(0);
		}

		mapPOSHourlyWiseSales = new LinkedHashMap<String, Map<String, clsCommonBeanDtl>>();

		ResultSet rsHourWise = clsGlobalVarClass.dbMysql.executeResultSet(sqlLiveTables.toString());
		funGenerateHourlyWiseSales(rsHourWise);
		rsHourWise = clsGlobalVarClass.dbMysql.executeResultSet(sqlQFile.toString());
		funGenerateHourlyWiseSales(rsHourWise);

		DecimalFormat decFormat = new DecimalFormat("0");

		Iterator<Map.Entry<String, Map<String, clsCommonBeanDtl>>> posIterator = mapPOSHourlyWiseSales.entrySet().iterator();
		while (posIterator.hasNext())
		{
		    Map<String, clsCommonBeanDtl> mapHrlyDtl = posIterator.next().getValue();
		    Iterator<Map.Entry<String, clsCommonBeanDtl>> hrsIterator = mapHrlyDtl.entrySet().iterator();
		    while (hrsIterator.hasNext())
		    {
			clsCommonBeanDtl objHrsDtl = hrsIterator.next().getValue();

			records[0] = objHrsDtl.getStartHrs() + "-" + objHrsDtl.getEndHrs();
			records[1] = decFormat.format(objHrsDtl.getNoOfBills());
			records[2] = gDecimalFormat.format(objHrsDtl.getSaleAmount());
			temp1 = temp1.add(new BigDecimal(records[2].toString()));
			dm.addRow(records);
		    }
		}

		Object[] ob1
			=
			{
			    "Total", "", gDecimalFormat.format(temp1), ""
			};
		dm1.addRow(ob1);

		tblTotal.setModel(dm1);
		tblSales.setModel(dm);

		//to calculate sales in percent(%)
		dm.addColumn("Sales(%)");
		vSalesReportExcelColLength.add("6#Right"); //sales%                
		double totalSale = Double.parseDouble(tblTotal.getValueAt(0, 2).toString());
		for (int row = 0; row < tblSales.getRowCount(); row++)
		{
		    double saleAmt = Double.parseDouble(tblSales.getValueAt(row, 2).toString());
		    double salePer = (saleAmt / totalSale) * 100;

		    tblSales.setValueAt(gDecimalFormat.format(salePer), row, 3);
		}

		tblSales.setSize(400, 400);
		tblSales.setRowHeight(40);
		tblTotal.setRowHeight(40);
		tblTotal.setSize(400, 400);

		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

		tblSales.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
		tblSales.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		tblSales.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		DefaultTableCellRenderer rightRenderer1 = new DefaultTableCellRenderer();
		rightRenderer1.setHorizontalAlignment(JLabel.RIGHT);
		tblTotal.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
		tblTotal.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		tblTotal.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    /**
     * Hour wise Item Report
     */
    private void funHourWiseItemReport()
    {
	StringBuilder sbSqlLiveBill = new StringBuilder();
	StringBuilder sbSqlQFileBill = new StringBuilder();
	StringBuilder sbSqlModLiveBill = new StringBuilder();
	StringBuilder sbSqlQModFileBill = new StringBuilder();
	try
	{
	    sql = "";
	    reportName = "Hourly Wise Item Sales Report";
	    totalAmount = new BigDecimal("0.00");
	    temp = new BigDecimal("0.00");
	    temp1 = new BigDecimal("0.00");
	    dm = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    return false;
		}
	    };
	    dm.addColumn("Time Range");
	    dm.addColumn("Item Name");
	    dm.addColumn("QTY");
	    dm.addColumn("Item Amount");
	    dm.addColumn("Discount");

	    vSalesReportExcelColLength = new java.util.Vector();
	    vSalesReportExcelColLength.add("10#Left");
	    vSalesReportExcelColLength.add("10#Left");
	    vSalesReportExcelColLength.add("6#Right");
	    vSalesReportExcelColLength.add("5#Right");
	    vSalesReportExcelColLength.add("5#Right");

	    DefaultTableModel dm1 = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    //all cells false
		    return false;
		}
	    };
	    dm1.addColumn("");

	    dm1.addColumn("Total Amount");
	    dm1.addColumn("Total Discount");
	    Date dt1 = dteFromDate.getDate();
	    Date dt2 = dteToDate.getDate();

	    if ((dt2.getTime() - dt1.getTime()) < 0)
	    {
		new frmOkPopUp(null, "Invalid date", "Error", 1).setVisible(true);
	    }
	    else
	    {
		fromDate = funGetFromDate();
		toDate = funGetToDate();
		records = new Object[6];
		String pos = funGetSelectedPosCode();

		sbSqlLiveBill.setLength(0);
		sbSqlQFileBill.setLength(0);
		sbSqlModLiveBill.setLength(0);
		sbSqlQModFileBill.setLength(0);

		sbSqlLiveBill.append("select left(right(b.dteDateCreated,8),2)"
			+ " ,left(right(b.dteDateCreated,8),2)+1,a.strItemName,sum(a.dblQuantity),"
			+ " sum(a.dblAmount)-sum(a.dblDiscountAmt) as Total,"
			+ " sum(a.dblDiscountAmt) as Discount,'" + clsGlobalVarClass.gUserCode + "' "
			+ " from tblbilldtl a,tblbillhd b,tblitemmaster c "
			+ " where a.strBillNo=b.strBillNo "
			+ " and date(a.dteBillDate)=date(b.dteBillDate) "
			+ " and a.strClientCode=b.strClientCode "
			+ " and a.strItemCode=c.strItemCode "
			+ " and date(b.dteBillDate) BETWEEN '" + fromDate + "' AND '" + toDate + "' ");
		if (!pos.equals("All"))
		{
		    sbSqlLiveBill.append(" and " + objUtility.funGetSelectedPOSCodeString("strPOSCode", selectedPOSCodeSet) + " ");
		}
		if (clsGlobalVarClass.gEnableShiftYN && (!cmbShift.getSelectedItem().toString().equalsIgnoreCase("All")))
		{
		    sbSqlLiveBill.append(" AND b.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ");
		}
		if (!cmbArea.getSelectedItem().equals("All"))
		{
		    sbSqlLiveBill.append(" and b.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ");
		}
		if (!cmbOperationType.getSelectedItem().toString().equalsIgnoreCase("All"))
		{
		    sbSqlLiveBill.append(" and b.strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ");
		}
		if (!txtCustomerCode.getText().equalsIgnoreCase(""))
		{
		    sbSqlLiveBill.append(" and b.strCustomerCode='" + txtCustomerCode.getText() + "' ");

		}
		sbSqlLiveBill.append(" group by a.strItemName,LEFT(RIGHT(b.dteDateCreated,8),2),LEFT(RIGHT(b.dteDateCreated,8),2)+1 ");

		sbSqlQFileBill.append("select left(right(b.dteDateCreated,8),2)"
			+ " ,left(right(b.dteDateCreated,8),2)+1,a.strItemName,sum(a.dblQuantity),"
			+ " sum(a.dblAmount)-sum(a.dblDiscountAmt) as Total,"
			+ " sum(a.dblDiscountAmt) as Discount,'" + clsGlobalVarClass.gUserCode + "' "
			+ " from tblqbilldtl a,tblqbillhd b,tblitemmaster c "
			+ " where a.strBillNo=b.strBillNo "
			+ " and date(a.dteBillDate)=date(b.dteBillDate) "
			+ " and a.strClientCode=b.strClientCode "
			+ " and a.strItemCode=c.strItemCode "
			+ " and date(b.dteBillDate) BETWEEN '" + fromDate + "' AND '" + toDate + "' ");
		if (!pos.equals("All"))
		{
		    sbSqlQFileBill.append(" and " + objUtility.funGetSelectedPOSCodeString("strPOSCode", selectedPOSCodeSet) + " ");
		}
		if (clsGlobalVarClass.gEnableShiftYN && (!cmbShift.getSelectedItem().toString().equalsIgnoreCase("All")))
		{
		    sbSqlQFileBill.append(" AND b.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ");
		}
		if (!cmbArea.getSelectedItem().equals("All"))
		{
		    sbSqlQFileBill.append(" and b.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ");
		}
		if (!cmbOperationType.getSelectedItem().toString().equalsIgnoreCase("All"))
		{
		    sbSqlQFileBill.append(" and b.strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ");
		}
		if (!txtCustomerCode.getText().equalsIgnoreCase(""))
		{
		    sbSqlQFileBill.append(" and b.strCustomerCode='" + txtCustomerCode.getText() + "' ");

		}
		sbSqlQFileBill.append(" group by a.strItemName,LEFT(RIGHT(b.dteDateCreated,8),2),LEFT(RIGHT(b.dteDateCreated,8),2)+1 ");

		sbSqlModLiveBill.append("select left(right(b.dteDateCreated,8),2)"
			+ " , left(right(b.dteDateCreated,8),2)+1,a.strModifierName,sum(a.dblQuantity),"
			+ " sum(a.dblAmount)-sum(a.dblDiscAmt) as Total,"
			+ " sum(a.dblDiscAmt) as Discount,'" + clsGlobalVarClass.gUserCode + "' "
			+ " from tblbillmodifierdtl a,tblbillhd b,tblitemmaster c "
			+ " where a.strBillNo=b.strBillNo "
			+ " and date(a.dteBillDate)=date(b.dteBillDate) "
			+ " and a.strClientCode=b.strClientCode "
			+ " and Left(a.strItemCode,7)=c.strItemCode "
			+ " and date(b.dteBillDate) BETWEEN '" + fromDate + "' AND '" + toDate + "' ");
		if (!pos.equals("All"))
		{
		    sbSqlModLiveBill.append(" and " + objUtility.funGetSelectedPOSCodeString("strPOSCode", selectedPOSCodeSet) + " ");
		}
		if (clsGlobalVarClass.gEnableShiftYN && (!cmbShift.getSelectedItem().toString().equalsIgnoreCase("All")))
		{
		    sbSqlModLiveBill.append(" AND b.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ");
		}
		if (!cmbArea.getSelectedItem().equals("All"))
		{
		    sbSqlModLiveBill.append(" and b.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ");
		}
		if (!cmbOperationType.getSelectedItem().toString().equalsIgnoreCase("All"))
		{
		    sbSqlModLiveBill.append(" and b.strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ");
		}
		if (!txtCustomerCode.getText().equalsIgnoreCase(""))
		{
		    sbSqlModLiveBill.append(" and a.strCustomerCode='" + txtCustomerCode.getText() + "' ");

		}
		sbSqlModLiveBill.append(" group by a.strModifierName,LEFT(RIGHT(b.dteDateCreated,8),2),LEFT(RIGHT(b.dteDateCreated,8),2)+1 ");

		sbSqlQModFileBill.append("select left(right(b.dteDateCreated,8),2)"
			+ " , left(right(b.dteDateCreated,8),2)+1,a.strModifierName,sum(a.dblQuantity),"
			+ " sum(a.dblAmount)-sum(a.dblDiscAmt) as Total,"
			+ " sum(a.dblDiscAmt) as Discount,'" + clsGlobalVarClass.gUserCode + "' "
			+ " from tblqbillmodifierdtl a,tblqbillhd b,tblitemmaster c "
			+ " where a.strBillNo=b.strBillNo "
			+ " and date(a.dteBillDate)=date(b.dteBillDate) "
			+ " and a.strClientCode=b.strClientCode "
			+ " and Left(a.strItemCode,7)=c.strItemCode "
			+ " and date(b.dteBillDate) BETWEEN '" + fromDate + "' AND '" + toDate + "' ");
		if (!pos.equals("All"))
		{
		    sbSqlQModFileBill.append(" and " + objUtility.funGetSelectedPOSCodeString("strPOSCode", selectedPOSCodeSet) + " ");
		}
		if (clsGlobalVarClass.gEnableShiftYN && (!cmbShift.getSelectedItem().toString().equalsIgnoreCase("All")))
		{
		    sbSqlQModFileBill.append(" AND b.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ");
		}
		if (!cmbArea.getSelectedItem().equals("All"))
		{
		    sbSqlQModFileBill.append(" and b.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ");
		}
		if (!cmbOperationType.getSelectedItem().toString().equalsIgnoreCase("All"))
		{
		    sbSqlQModFileBill.append(" and b.strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ");
		}
		if (!txtCustomerCode.getText().equalsIgnoreCase(""))
		{
		    sbSqlQModFileBill.append(" and a.strCustomerCode='" + txtCustomerCode.getText() + "' ");

		}
		sbSqlQModFileBill.append(" group by a.strModifierName,LEFT(RIGHT(b.dteDateCreated,8),2),LEFT(RIGHT(b.dteDateCreated,8),2)+1 ");

		//System.out.println("live=" + sbSqlLiveBill);
		//System.out.println("q=" + sbSqlQFileBill);
		//System.out.println("Modlive=" + sbSqlModLiveBill);
		//System.out.println("qMod=" + sbSqlQModFileBill);
		boolean flgRecords = false;
		if (!flgRecords)
		{
		    dm.setRowCount(0);
		    dm1.setRowCount(0);
		}

		DecimalFormat decFormat = new DecimalFormat("0");

		ResultSet saleSet = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlLiveBill.toString());
		while (saleSet.next())
		{
		    flgRecords = true;
		    records[0] = saleSet.getString(1) + "-" + saleSet.getString(2);
		    records[1] = saleSet.getString(3);
		    records[2] = decFormat.format(Double.parseDouble(saleSet.getString(4)));
		    records[3] = gDecimalFormat.format(Double.parseDouble(saleSet.getString(5)));
		    records[4] = gDecimalFormat.format(Double.parseDouble(saleSet.getString(6)));
		    temp = temp.add(new BigDecimal(records[3].toString()));
		    temp1 = temp1.add(new BigDecimal(records[4].toString()));
		    dm.addRow(records);
		}
		saleSet.close();
		saleSet = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlQFileBill.toString());
		while (saleSet.next())
		{
		    flgRecords = true;
		    records[0] = saleSet.getString(1) + "-" + saleSet.getString(2);
		    records[1] = saleSet.getString(3);
		    records[2] = decFormat.format(Double.parseDouble(saleSet.getString(4)));
		    records[3] = gDecimalFormat.format(Double.parseDouble(saleSet.getString(5)));
		    records[4] = gDecimalFormat.format(Double.parseDouble(saleSet.getString(6)));
		    temp = temp.add(new BigDecimal(records[3].toString()));
		    temp1 = temp1.add(new BigDecimal(records[4].toString()));
		    dm.addRow(records);
		}
		saleSet.close();
		saleSet = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlModLiveBill.toString());
		while (saleSet.next())
		{
		    flgRecords = true;
		    records[0] = saleSet.getString(1) + "-" + saleSet.getString(2);
		    records[1] = saleSet.getString(3);
		    records[2] = decFormat.format(Double.parseDouble(saleSet.getString(4)));
		    records[3] = gDecimalFormat.format(Double.parseDouble(saleSet.getString(5)));
		    records[4] = gDecimalFormat.format(Double.parseDouble(saleSet.getString(6)));
		    temp = temp.add(new BigDecimal(records[3].toString()));
		    temp1 = temp1.add(new BigDecimal(records[4].toString()));
		    dm.addRow(records);
		}
		saleSet.close();
		saleSet = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlQModFileBill.toString());
		while (saleSet.next())
		{
		    flgRecords = true;
		    records[0] = saleSet.getString(1) + "-" + saleSet.getString(2);
		    records[1] = saleSet.getString(3);
		    records[2] = decFormat.format(Double.parseDouble(saleSet.getString(4)));
		    records[3] = gDecimalFormat.format(Double.parseDouble(saleSet.getString(5)));
		    records[4] = gDecimalFormat.format(Double.parseDouble(saleSet.getString(6)));
		    temp = temp.add(new BigDecimal(records[3].toString()));
		    temp1 = temp1.add(new BigDecimal(records[4].toString()));
		    dm.addRow(records);
		}

		Object[] ob1
			=
			{
			    "Total", gDecimalFormat.format(temp), gDecimalFormat.format(temp1)
			};
		dm1.addRow(ob1);

		tblTotal.setModel(dm1);
		tblSales.setModel(dm);
		tblSales.setSize(400, 400);
		tblSales.setRowHeight(40);
		tblTotal.setRowHeight(40);
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		tblSales.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
		tblSales.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		tblSales.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
		tblSales.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblSales.getColumnModel().getColumn(0).setPreferredWidth(250);
		tblSales.getColumnModel().getColumn(1).setPreferredWidth(200);
		tblSales.getColumnModel().getColumn(2).setPreferredWidth(125);
		tblSales.getColumnModel().getColumn(3).setPreferredWidth(125);
		tblSales.getColumnModel().getColumn(4).setPreferredWidth(125);

		tblTotal.setSize(400, 400);
		DefaultTableCellRenderer rightRenderer1 = new DefaultTableCellRenderer();
		rightRenderer1.setHorizontalAlignment(JLabel.RIGHT);
		tblTotal.getColumnModel().getColumn(2).setCellRenderer(rightRenderer1);
		tblTotal.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblTotal.getColumnModel().getColumn(0).setPreferredWidth(570);
		tblTotal.getColumnModel().getColumn(1).setPreferredWidth(100);
		tblTotal.getColumnModel().getColumn(2).setPreferredWidth(120);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    sbSqlLiveBill = null;
	    sbSqlQFileBill = null;
	}
    }

    /**
     * Bill wise Tips Report
     */
    private void funBillWiseTips()
    {
	try
	{
	    sql = "";
	    reportName = "Bill Wise Tips Sales Report";
	    double subTotal = 0;
	    exportFormName = "BillWiseTips";
	    totalAmount = new BigDecimal("0.00");
	    Disc = new BigDecimal("0.00");
	    temp = new BigDecimal("0.00");
	    temp1 = new BigDecimal("0.00");
	    fromDate = funGetFromDate();
	    toDate = funGetToDate();
	    funGetFromTime();
	    funGetToTime();
	    String DateFrom = null, field = null, DateTo = null;
	    if (funGetFromTime() != null)
	    {
		DateFrom = fromDate + " " + funGetFromTime();
		field = "dteBillDate";
	    }
	    else
	    {
		DateFrom = fromDate;
		field = "date(dteBillDate)";
	    }
	    if (funGetToTime() != null)
	    {
		DateTo = toDate + " " + funGetToTime();
	    }
	    else
	    {
		DateTo = toDate;
	    }

	    dm = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    //all cells false
		    return false;
		}
	    };
	    dm.addColumn("Bill No");
	    dm.addColumn("Date");
	    dm.addColumn("Bill Time");
	    dm.addColumn("POS Code");
	    dm.addColumn("Set Mode");
	    dm.addColumn("Disc %");
	    dm.addColumn("Disc Amt");
	    dm.addColumn("SubTotal");
	    dm.addColumn("Tax Amt");
	    dm.addColumn("Tip Amt");
	    dm.addColumn("Sales Amount");

	    vSalesReportExcelColLength = new java.util.Vector();
	    vSalesReportExcelColLength.add("6#Left");
	    vSalesReportExcelColLength.add("6#Left");
	    vSalesReportExcelColLength.add("6#Left");
	    vSalesReportExcelColLength.add("10#Left");
	    vSalesReportExcelColLength.add("10#Left");
	    vSalesReportExcelColLength.add("6#Left");
	    vSalesReportExcelColLength.add("10#Left");
	    vSalesReportExcelColLength.add("6#Right");
	    vSalesReportExcelColLength.add("6#Right");
	    vSalesReportExcelColLength.add("6#Right");
	    vSalesReportExcelColLength.add("6#Right");

	    DefaultTableModel dm1 = new DefaultTableModel();
	    dm1.addColumn("");
	    dm1.addColumn("Disc");
	    dm1.addColumn("SubTotal");
	    dm1.addColumn("Tax");
	    dm1.addColumn("Tip Amount");
	    dm1.addColumn("Sales Amount");
	    tblTotal.setModel(dm1);
	    records = new Object[11];
	    double tipAmountTotal = 0;
	    String pos = funGetSelectedPosCode();

	    sql = "select strBillNo,left(dteBillDate,10),left(right(dteDateCreated,8),5) as "
		    + "BillTime,strPOSCode,strSettelmentMode,dblDiscountPer,dblDiscountAmt,dblTaxAmt,"
		    + "dblSubTotal,dblTipAmount,dblGrandTotal,strUserCreated,strUserEdited,dteDateCreated,"
		    + "dteDateEdited,strClientCode,strTableNo,strWaiterNo,strCustomerCode from vqbillhd ";
	    if (!pos.equals("All") && !cmbOperator.getSelectedItem().equals("All"))
	    {
		sql += "where " + field + " between '" + DateFrom + "' and '" + DateTo + "' and " + objUtility.funGetSelectedPOSCodeString("strPOSCode", selectedPOSCodeSet) + " and strUserCreated='" + cmbOperator.getSelectedItem().toString() + "' and dblTipAmount>0";
	    }
	    else if (pos.equals("All") && !cmbOperator.getSelectedItem().equals("All"))
	    {
		sql += "where " + field + " between '" + DateFrom + "' and  strUserCreated='" + cmbOperator.getSelectedItem().toString() + "' and dblTipAmount>0";
	    }
	    else if (!pos.equals("All") && cmbOperator.getSelectedItem().equals("All"))
	    {
		sql += " where " + field + " between '" + DateFrom + "' and '" + DateTo + "' and " + objUtility.funGetSelectedPOSCodeString("strPOSCode", selectedPOSCodeSet) + " and dblTipAmount>0";
	    }
	    else if (pos.equals("All") && cmbOperator.getSelectedItem().equals("All"))
	    {
		sql += "where " + field + " between '" + DateFrom + "' and '" + DateTo + "'  and dblTipAmount>0";
	    }
	    if (clsGlobalVarClass.gEnableShiftYN && (!cmbShift.getSelectedItem().toString().equalsIgnoreCase("All")))
	    {
		sql += " AND intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ";
	    }
	    if (!cmbArea.getSelectedItem().equals("All"))
	    {
		sql += " and strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ";
	    }
	    if (!txtCustomerCode.getText().equalsIgnoreCase(""))
	    {
		sql += " and a.strCustomerCode='" + txtCustomerCode.getText() + "' ";

	    }
	    if (!cmbOperationType.getSelectedItem().toString().equalsIgnoreCase("All"))
	    {
		sql += " and strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ";
	    }
	    if (txtBillNofrom.getText().trim().length() == 0 && txtBillnoTo.getText().trim().length() == 0)
	    {
		sql += " order by strBillNo desc";
	    }
	    else
	    {
		sql += " and strBillNo between '" + txtBillNofrom.getText() + "' and '" + txtBillnoTo.getText() + "' order by strBillNo desc";
	    }

	    boolean flgRecords = false;
	    DecimalFormat decFormat = new DecimalFormat("0");

	    ResultSet saleSet = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (saleSet.next())
	    {
		flgRecords = true;
		records[0] = saleSet.getString(1);
		String tempBillDate = saleSet.getString(2);
		String[] spDate = tempBillDate.split("-");
		records[1] = spDate[2] + "-" + spDate[1] + "-" + spDate[0];
		records[2] = saleSet.getString(3);
		records[3] = saleSet.getString(4);
		records[4] = saleSet.getString(5);
		records[5] = gDecimalFormat.format(saleSet.getString(6));
		records[6] = gDecimalFormat.format(saleSet.getString(7));
		records[7] = gDecimalFormat.format(saleSet.getString(9));
		records[8] = gDecimalFormat.format(saleSet.getString(8));
		records[9] = gDecimalFormat.format(saleSet.getString(10));
		records[10] = gDecimalFormat.format(saleSet.getString(11));

		Disc = Disc.add(new BigDecimal(records[6].toString()));
		temp = temp.add(new BigDecimal(records[7].toString()));
		subTotal = subTotal + Double.parseDouble(records[8].toString());
		temp1 = temp1.add(new BigDecimal(records[10].toString()));
		tipAmountTotal = tipAmountTotal + Double.parseDouble(records[9].toString());
		dm.addRow(records);
	    }
	    saleSet.close();

	    Object[] ob1
		    =
		    {
			"Total", gDecimalFormat.format(Disc), gDecimalFormat.format(temp), gDecimalFormat.format(subTotal), gDecimalFormat.format(tipAmountTotal), gDecimalFormat.format(temp1)
		    };
	    dm1.addRow(ob1);

	    if (!flgRecords)
	    {
		dm.setRowCount(0);
		dm1.setRowCount(0);
	    }

	    tblTotal.setModel(dm1);
	    tblSales.setModel(dm);
	    tblSales.setRowHeight(25);
	    tblTotal.setRowHeight(40);
	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
	    tblSales.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
	    tblSales.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
	    tblSales.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);
	    tblSales.getColumnModel().getColumn(8).setCellRenderer(rightRenderer);
	    tblSales.getColumnModel().getColumn(9).setCellRenderer(rightRenderer);
	    tblSales.getColumnModel().getColumn(10).setCellRenderer(rightRenderer);
	    tblSales.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    tblSales.getColumnModel().getColumn(0).setPreferredWidth(80);
	    tblSales.getColumnModel().getColumn(1).setPreferredWidth(80);
	    tblSales.getColumnModel().getColumn(2).setPreferredWidth(60);
	    tblSales.getColumnModel().getColumn(3).setPreferredWidth(70);
	    tblSales.getColumnModel().getColumn(4).setPreferredWidth(72);
	    tblSales.getColumnModel().getColumn(5).setPreferredWidth(70);
	    tblSales.getColumnModel().getColumn(6).setPreferredWidth(70);
	    tblSales.getColumnModel().getColumn(7).setPreferredWidth(70);
	    tblSales.getColumnModel().getColumn(8).setPreferredWidth(70);
	    tblSales.getColumnModel().getColumn(9).setPreferredWidth(60);
	    tblSales.getColumnModel().getColumn(10).setPreferredWidth(90);
	    tblTotal.setSize(400, 400);
	    DefaultTableCellRenderer rightRenderer1 = new DefaultTableCellRenderer();
	    rightRenderer1.setHorizontalAlignment(JLabel.RIGHT);
	    tblTotal.getColumnModel().getColumn(1).setCellRenderer(rightRenderer1);
	    tblTotal.getColumnModel().getColumn(2).setCellRenderer(rightRenderer1);
	    tblTotal.getColumnModel().getColumn(3).setCellRenderer(rightRenderer1);
	    tblTotal.getColumnModel().getColumn(4).setCellRenderer(rightRenderer1);
	    tblTotal.getColumnModel().getColumn(5).setCellRenderer(rightRenderer1);
	    tblTotal.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    tblTotal.getColumnModel().getColumn(0).setPreferredWidth(430);
	    tblTotal.getColumnModel().getColumn(1).setPreferredWidth(70);
	    tblTotal.getColumnModel().getColumn(2).setPreferredWidth(70);
	    tblTotal.getColumnModel().getColumn(3).setPreferredWidth(70);
	    tblTotal.getColumnModel().getColumn(4).setPreferredWidth(60);
	    tblTotal.getColumnModel().getColumn(5).setPreferredWidth(90);

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    /**
     * Modifier Wise
     */
    private void funModifierWise()
    {
	StringBuilder sbSqlLiveBill = new StringBuilder();
	StringBuilder sbSqlQFileBill = new StringBuilder();
	StringBuilder sbSqlFilters = new StringBuilder();
	reportName = "Modifier Wise Sales Report";
	try
	{
	    sql = "";
	    totalQty = new Double("0.00");
	    totalAmount = new BigDecimal("0.00");
	    temp = new BigDecimal("0.00");
	    temp1 = new BigDecimal("0.00");
	    dm = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    //all cells false
		    return false;
		}
	    };
	    dm.addColumn("Modifier Name");
	    dm.addColumn("POS");
	    dm.addColumn("Quantity");
	    dm.addColumn("Sales Amount");

	    vSalesReportExcelColLength = new java.util.Vector();
	    vSalesReportExcelColLength.add("6#Left"); //Bill No
	    vSalesReportExcelColLength.add("6#Left"); //Bill Date
	    vSalesReportExcelColLength.add("6#Left"); //Bill Time
	    vSalesReportExcelColLength.add("10#Left"); //Table Name

	    DefaultTableModel dm1 = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    //all cells false
		    return false;
		}
	    };
	    dm1.addColumn("");
	    dm1.addColumn("");
	    dm1.addColumn("Quantity");
	    dm1.addColumn("Sales Amount");
	    Date dt1 = dteFromDate.getDate();
	    Date dt2 = dteToDate.getDate();
	    if ((dt2.getTime() - dt1.getTime()) < 0)
	    {
		new frmOkPopUp(null, "Invalid date", "Error", 1).setVisible(true);
	    }
	    else
	    {
		fromDate = funGetFromDate();
		toDate = funGetToDate();
		records = new Object[6];
		String pos = funGetSelectedPosCode();

		sbSqlLiveBill.setLength(0);
		sbSqlQFileBill.setLength(0);
		sbSqlFilters.setLength(0);

		sbSqlLiveBill.append("SELECT b.strModifierCode, b.strModifierName"
			+ " ,c.strPOSName, sum( b.dblQuantity ), sum( b.dblAmount )"
			+ ",'" + clsGlobalVarClass.gUserCode + "',a.strposcode "
			+ " FROM tblbillhd a, tblbillmodifierdtl b, tblposmaster c "
			+ " WHERE a.strbillno = b.strbillno "
			+ " and date(a.dteBillDate)=date(b.dteBillDate) "
			+ " and a.strClientCode=b.strClientCode "
			+ " and a.strposcode=c.strposcode "
			+ " and date( a.dteBillDate ) BETWEEN '" + fromDate + "' AND '" + toDate + "'");

		sbSqlQFileBill.append("SELECT b.strModifierCode, b.strModifierName"
			+ " ,c.strPOSName, sum( b.dblQuantity ), sum( b.dblAmount ) "
			+ ",'" + clsGlobalVarClass.gUserCode + "',a.strposcode "
			+ " FROM tblqbillhd a, tblqbillmodifierdtl b, tblposmaster c "
			+ " WHERE a.strbillno = b.strbillno "
			+ " and date(a.dteBillDate)=date(b.dteBillDate) "
			+ " and a.strClientCode=b.strClientCode "
			+ " and a.strposcode=c.strposcode "
			+ " and date( a.dteBillDate ) BETWEEN '" + fromDate + "' AND '" + toDate + "'");

		if (!pos.equals("All") && !cmbOperator.getSelectedItem().equals("All"))
		{
		    sbSqlFilters.append(" AND " + objUtility.funGetSelectedPOSCodeString("a.strPOSCode", selectedPOSCodeSet) + " and a.strUserCreated='" + cmbOperator.getSelectedItem().toString() + "' ");
		}
		else if (!pos.equals("All") && cmbOperator.getSelectedItem().equals("All"))
		{
		    sbSqlFilters.append(" AND " + objUtility.funGetSelectedPOSCodeString("a.strPOSCode", selectedPOSCodeSet) + " ");
		}
		else if (pos.equals("All") && !cmbOperator.getSelectedItem().equals("All"))
		{
		    sbSqlFilters.append(" AND a.strUserCreated='" + cmbOperator.getSelectedItem().toString() + "' ");
		}
		if (clsGlobalVarClass.gEnableShiftYN && (!cmbShift.getSelectedItem().toString().equalsIgnoreCase("All")))
		{
		    sbSqlFilters.append(" AND a.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ");
		}
		if (txtBillNofrom.getText().trim().length() == 0 && txtBillnoTo.getText().trim().length() == 0)
		{
		}
		else
		{
		    sbSqlFilters.append(" and a.strbillno between '" + txtBillNofrom.getText() + "' and '" + txtBillnoTo.getText() + "'");
		}
		if (!cmbArea.getSelectedItem().equals("All"))
		{
		    sbSqlFilters.append(" and a.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ");
		}
		if (!cmbOperationType.getSelectedItem().toString().equalsIgnoreCase("All"))
		{
		    sbSqlFilters.append(" and a.strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ");
		}
		if (!txtCustomerCode.getText().equalsIgnoreCase(""))
		{
		    sbSqlFilters.append(" and a.strCustomerCode='" + txtCustomerCode.getText() + "' ");

		}
		sbSqlFilters.append(" GROUP BY a.strposcode, b.strModifierCode, b.strModifierName ");

		sbSqlLiveBill.append(sbSqlFilters);
		sbSqlQFileBill.append(sbSqlFilters);

//                System.out.println("live="+sbSqlLiveBill);
//                System.out.println("q="+sbSqlQFileBill);
		boolean flgRecords = false;
		if (!flgRecords)
		{
		    dm.setRowCount(0);
		    dm1.setRowCount(0);
		}

		mapPOSModifierWiseSales = new LinkedHashMap<String, Map<String, clsCommonBeanDtl>>();

		ResultSet rsModiWiseWise = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlLiveBill.toString());
		funGenerateModifierWiseSales(rsModiWiseWise);
		rsModiWiseWise = clsGlobalVarClass.dbMysql.executeResultSet(sbSqlQFileBill.toString());
		funGenerateModifierWiseSales(rsModiWiseWise);

		DecimalFormat decFormat = new DecimalFormat("0");

		Iterator<Map.Entry<String, Map<String, clsCommonBeanDtl>>> posIterator = mapPOSModifierWiseSales.entrySet().iterator();
		while (posIterator.hasNext())
		{
		    Map<String, clsCommonBeanDtl> mapModiDtl = posIterator.next().getValue();
		    Iterator<Map.Entry<String, clsCommonBeanDtl>> modiIterator = mapModiDtl.entrySet().iterator();
		    while (modiIterator.hasNext())
		    {
			clsCommonBeanDtl objModiDtl = modiIterator.next().getValue();

			records[0] = objModiDtl.getModiName();
			records[1] = objModiDtl.getPosName();
			records[2] = decFormat.format(objModiDtl.getQty());
			records[3] = gDecimalFormat.format(objModiDtl.getSaleAmount());
			totalQty = totalQty + Double.parseDouble(records[2].toString());
			temp1 = new BigDecimal(objModiDtl.getSaleAmount());
			totalAmount = totalAmount.add(temp1);
			dm.addRow(records);
		    }
		}
		Object[] ob1
			=
			{
			    "Total", "", decFormat.format(totalQty), gDecimalFormat.format(totalAmount)
			};
		dm1.addRow(ob1);

		tblTotal.setModel(dm1);
		tblSales.setModel(dm);

		tblSales.setSize(400, 400);
		tblSales.setRowHeight(25);
		tblTotal.setRowHeight(40);

		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

		tblSales.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
		tblSales.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);

		tblSales.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		tblSales.getColumnModel().getColumn(0).setPreferredWidth(200);

		tblTotal.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
		tblTotal.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);

		tblTotal.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		tblTotal.getColumnModel().getColumn(0).setPreferredWidth(200);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    sbSqlLiveBill = null;
	    sbSqlQFileBill = null;
	    sbSqlFilters = null;
	}
    }

    /**
     * Customer Wise Bill Sales
     */
    private void funCustomerWiseBillSales()
    {
	StringBuilder sbSqlLiveBill = new StringBuilder();
	StringBuilder sbSqlQFileBill = new StringBuilder();
	StringBuilder sbSqlFilters = new StringBuilder();
	try
	{
	    sql = "";
	    exportFormName = "CustomerWiseBillSales";
	    fromDate = funGetFromDate();
	    toDate = funGetToDate();
	    funGetFromTime();
	    funGetToTime();
	    String DateFrom = null, field = null, DateTo = null;
	    if (funGetFromTime() != null)
	    {
		DateFrom = fromDate + " " + funGetFromTime();
		field = "dteBillDate";
	    }
	    else
	    {
		DateFrom = fromDate;
		field = "date(dteBillDate)";
	    }
	    if (funGetToTime() != null)
	    {
		DateTo = toDate + " " + funGetToTime();
	    }
	    else
	    {
		DateTo = toDate;
	    }

	    dm = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    //all cells false
		    return false;
		}
	    };

	    dm.addColumn("Customer Code");
	    dm.addColumn("Customer Name");
	    dm.addColumn("Bill No");
	    dm.addColumn("Sales Amount");

	    vSalesReportExcelColLength = new java.util.Vector();
	    vSalesReportExcelColLength.add("6#Left");
	    vSalesReportExcelColLength.add("6#Left");
	    vSalesReportExcelColLength.add("6#Left");
	    vSalesReportExcelColLength.add("10#Left");

	    DefaultTableModel dm1 = new DefaultTableModel();
	    dm1.addColumn("");
	    dm1.addColumn("");
	    dm1.addColumn("");
	    dm1.addColumn("Sales Amount");
	    tblTotal.setModel(dm1);
	    records = new Object[4];
	    String pos = funGetSelectedPosCode();

	    sbSqlLiveBill.setLength(0);
	    sbSqlQFileBill.setLength(0);
	    sbSqlFilters.setLength(0);

	    sbSqlLiveBill.append("select b.strCustomerCode,b.strCustomerName "
		    + " ,a.strBillNo,sum(a.dblGrandTotal),'" + clsGlobalVarClass.gUserCode + "' "
		    + " from tblbillhd a,tblcustomermaster b "
		    + " where a.strCustomerCode=b.strCustomerCode and a.strCustomerCode='" + txtCustomerCode.getText().trim() + "' "
		    + " and date(a.dteBillDate) between '" + DateFrom + "' and '" + DateTo + "'");

	    sbSqlQFileBill.append("select b.strCustomerCode,b.strCustomerName "
		    + " ,a.strBillNo,sum(a.dblGrandTotal),'" + clsGlobalVarClass.gUserCode + "' "
		    + " from tblqbillhd a,tblcustomermaster b "
		    + " where a.strCustomerCode=b.strCustomerCode and a.strCustomerCode='" + txtCustomerCode.getText().trim() + "' "
		    + " and date(a.dteBillDate) between '" + DateFrom + "' and '" + DateTo + "'");

	    if (!pos.equals("All"))
	    {
		sbSqlFilters.append(" and " + objUtility.funGetSelectedPOSCodeString("a.strPOSCode", selectedPOSCodeSet) + " ");
	    }
	    if (!cmbOperator.getSelectedItem().equals("All"))
	    {
		sbSqlFilters.append(" and  a.strUserCreated='" + cmbOperator.getSelectedItem().toString() + "' ");
	    }
	    if (!cmbPayMode.getSelectedItem().equals("All"))
	    {
		sbSqlFilters.append(" and a.strSettelmentMode='" + cmbPayMode.getSelectedItem().toString() + "' ");
	    }
	    if (txtBillNofrom.getText().trim().length() > 0 && txtBillnoTo.getText().trim().length() > 0)
	    {
		sbSqlFilters.append(" and a.strBillNo between '" + txtBillNofrom.getText() + "' and '" + txtBillnoTo.getText() + "'");
	    }
	    if (clsGlobalVarClass.gEnableShiftYN && (!cmbShift.getSelectedItem().toString().equalsIgnoreCase("All")))
	    {
		sbSqlFilters.append(" AND a.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ");
	    }
	    if (!cmbArea.getSelectedItem().equals("All"))
	    {
		sbSqlFilters.append(" and a.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ");
	    }
	    if (!cmbOperationType.getSelectedItem().toString().equalsIgnoreCase("All"))
	    {
		sbSqlFilters.append(" and a.strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ");
	    }
	    if (!txtCustomerCode.getText().equalsIgnoreCase(""))
	    {
		sbSqlFilters.append(" and a.strCustomerCode='" + txtCustomerCode.getText() + "' ");
	    }

	    sbSqlFilters.append(" group by a.strBillNo");

	    boolean flgRecords = false;
	    double grandTotal = 0;

	    sbSqlLiveBill.append(sbSqlFilters);
	    sbSqlQFileBill.append(sbSqlFilters);
	    //System.out.println(sbSqlLiveBill);
	    //System.out.println(sbSqlQFileBill);

	    DecimalFormat decFormat = new DecimalFormat("0");

	    clsSalesFlashReport obj = new clsSalesFlashReport();
	    obj.funProcessSalesFlashReport(sbSqlLiveBill.toString(), sbSqlQFileBill.toString(), "CustWiseBillSales");

	    sql = "select * from tbltempsalesflash1 where strUser='" + clsGlobalVarClass.gUserCode + "'";
	    ResultSet rsCustomerWise = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsCustomerWise.next())
	    {
		flgRecords = true;
		records[0] = rsCustomerWise.getString(1);//Cust Code
		records[1] = rsCustomerWise.getString(2);//Cust Name
		records[2] = decFormat.format(rsCustomerWise.getDouble(3)); //count
		records[3] = gDecimalFormat.format(rsCustomerWise.getDouble(4));//Grand Total
		grandTotal += Double.parseDouble(records[3].toString());
		dm.addRow(records);
	    }
	    rsCustomerWise.close();

	    if (!flgRecords)
	    {
		dm.setRowCount(0);
		dm1.setRowCount(0);
	    }
	    Object[] ob1
		    =
		    {
			"Total", "", "", gDecimalFormat.format(grandTotal)
		    };
	    dm1.addRow(ob1);

	    tblTotal.setModel(dm1);
	    tblSales.setModel(dm);
	    tblSales.setRowHeight(25);
	    tblTotal.setRowHeight(40);
	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	    centerRenderer.setHorizontalAlignment(JLabel.CENTER);

	    tblSales.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
	    tblSales.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
	    tblSales.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    tblSales.getColumnModel().getColumn(0).setPreferredWidth(196);
	    tblSales.getColumnModel().getColumn(1).setPreferredWidth(178);
	    tblSales.getColumnModel().getColumn(2).setPreferredWidth(195);
	    tblSales.getColumnModel().getColumn(3).setPreferredWidth(192);
	    tblTotal.setSize(400, 400);
	    DefaultTableCellRenderer rightRenderer1 = new DefaultTableCellRenderer();
	    rightRenderer1.setHorizontalAlignment(JLabel.RIGHT);
	    tblTotal.getColumnModel().getColumn(2).setCellRenderer(rightRenderer1);
	    tblTotal.getColumnModel().getColumn(3).setCellRenderer(rightRenderer1);
	    tblTotal.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    tblTotal.getColumnModel().getColumn(0).setPreferredWidth(192);
	    tblTotal.getColumnModel().getColumn(1).setPreferredWidth(190);
	    tblTotal.getColumnModel().getColumn(2).setPreferredWidth(200);
	    tblTotal.getColumnModel().getColumn(3).setPreferredWidth(200);

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    sbSqlLiveBill = null;
	    sbSqlQFileBill = null;
	    sbSqlFilters = null;
	}
    }

    /**
     * Customer Wise Sales
     */
    private void funCustomerWiseSales()
    {
	StringBuilder sbSqlLiveBill = new StringBuilder();
	StringBuilder sbSqlQFileBill = new StringBuilder();
	StringBuilder sbSqlFilters = new StringBuilder();
	try
	{
	    sql = "";
	    exportFormName = "CustomerWiseSales";
	    fromDate = funGetFromDate();
	    toDate = funGetToDate();
	    funGetFromTime();
	    funGetToTime();
	    String DateFrom = null, DateTo = null;
	    if (funGetFromTime() != null)
	    {
		DateFrom = fromDate + " " + funGetFromTime();
	    }
	    else
	    {
		DateFrom = fromDate;
	    }
	    if (funGetToTime() != null)
	    {
		DateTo = toDate + " " + funGetToTime();
	    }
	    else
	    {
		DateTo = toDate;
	    }

	    DefaultTableModel dmSales = new DefaultTableModel();
	    dmSales.addColumn("Customer Name");
	    dmSales.addColumn("Mobile No.");
	    dmSales.addColumn("Date Of Birth");
	    dmSales.addColumn("No Of Bills");
	    dmSales.addColumn("Sales Amount");

	    vSalesReportExcelColLength = new java.util.Vector();
	    vSalesReportExcelColLength.add("6#Left");
	    vSalesReportExcelColLength.add("6#Left");
	    vSalesReportExcelColLength.add("6#Left");
	    vSalesReportExcelColLength.add("6#Left");
	    vSalesReportExcelColLength.add("6#Left");

	    DefaultTableModel dm1 = new DefaultTableModel();
	    dm1.addColumn("");
	    dm1.addColumn("");
	    dm1.addColumn("");
	    dm1.addColumn("");
	    dm1.addColumn("Sales Amount");
	    tblTotal.setModel(dm1);
	    records = new Object[5];
	    String pos = funGetSelectedPosCode();

	    sbSqlLiveBill.setLength(0);
	    sbSqlQFileBill.setLength(0);
	    sbSqlFilters.setLength(0);

	    sbSqlLiveBill.append("select ifnull(b.strCustomerCode,'ND'),ifnull(b.strCustomerName,'ND')"
		    + ",ifnull(count(a.strBillNo),'0'),ifnull(sum(a.dblGrandTotal),'0.00')"
		    + ",'" + clsGlobalVarClass.gUserCode + "',b.longMobileNo,b.dteDOB "
		    + "from tblbillhd a,tblcustomermaster b "
		    + "where a.strCustomerCode=b.strCustomerCode "
		    + "and date(a.dteBillDate) between '" + DateFrom + "' and '" + DateTo + "'");

	    sbSqlQFileBill.append("select ifnull(b.strCustomerCode,'ND'),ifnull(b.strCustomerName,'ND')"
		    + ",ifnull(count(a.strBillNo),'0'),ifnull(sum(a.dblGrandTotal),'0.00')"
		    + ",'" + clsGlobalVarClass.gUserCode + "',b.longMobileNo,b.dteDOB "
		    + "from tblqbillhd a,tblcustomermaster b "
		    + "where a.strCustomerCode=b.strCustomerCode "
		    + "and date(a.dteBillDate) between '" + DateFrom + "' and '" + DateTo + "'");

	    if (!pos.equals("All"))
	    {
		sbSqlFilters.append(" and " + objUtility.funGetSelectedPOSCodeString("a.strPOSCode", selectedPOSCodeSet) + " ");
	    }
	    if (!cmbOperator.getSelectedItem().equals("All"))
	    {
		sbSqlFilters.append(" and  a.strUserCreated='" + cmbOperator.getSelectedItem().toString() + "' ");
	    }
	    if (!cmbPayMode.getSelectedItem().equals("All"))
	    {
		sbSqlFilters.append(" and a.strSettelmentMode='" + cmbPayMode.getSelectedItem().toString() + "' ");
	    }
	    if (txtBillNofrom.getText().trim().length() > 0 && txtBillnoTo.getText().trim().length() > 0)
	    {
		sbSqlFilters.append(" and a.strBillNo between '" + txtBillNofrom.getText() + "' and '" + txtBillnoTo.getText() + "'");
	    }
	    if (clsGlobalVarClass.gEnableShiftYN && (!cmbShift.getSelectedItem().toString().equalsIgnoreCase("All")))
	    {
		sbSqlFilters.append(" AND a.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ");
	    }
	    if (!cmbArea.getSelectedItem().equals("All"))
	    {
		sbSqlFilters.append(" and a.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ");
	    }
	    if (!cmbOperationType.getSelectedItem().toString().equalsIgnoreCase("All"))
	    {
		sbSqlFilters.append(" and a.strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ");
	    }
	    if (!txtCustomerCode.getText().equalsIgnoreCase(""))
	    {
		sbSqlFilters.append(" and a.strCustomerCode='" + txtCustomerCode.getText() + "' ");
	    }
	    sbSqlFilters.append(" GROUP BY b.strCustomerCode");
	    boolean flgRecords = false;
	    double grandTotal = 0;

	    sbSqlLiveBill.append(sbSqlFilters);
	    sbSqlQFileBill.append(sbSqlFilters);

	    //System.out.println(sbSqlLiveBill);
	    //System.out.println(sbSqlQFileBill);
	    DecimalFormat decFormat = new DecimalFormat("0");

	    clsSalesFlashReport obj = new clsSalesFlashReport();
	    obj.funProcessSalesFlashReport(sbSqlLiveBill.toString(), sbSqlQFileBill.toString(), "CustWiseBillSales");

	    int billCount = 0;
	    sql = "select a.strbillno,a.dtebilldate,sum(a.tmebilltime),sum(a.strtablename),a.strposcode,a.strpaymode  "
		    + "from tbltempsalesflash1 a "
		    + "where strUser='" + clsGlobalVarClass.gUserCode + "' "
		    + "group by a.strbillno ";
	    ResultSet rsCustomerWise = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsCustomerWise.next())
	    {
		flgRecords = true;

		records[0] = rsCustomerWise.getString(2);//Cust Name
		records[1] = rsCustomerWise.getString(5);//mobile no
		records[2] = rsCustomerWise.getString(6);//DOB
		records[3] = decFormat.format(rsCustomerWise.getDouble(3)); //count
		records[4] = gDecimalFormat.format(rsCustomerWise.getDouble(4));//Grand Total

		billCount += Integer.parseInt(records[3].toString());
		grandTotal += Double.parseDouble(records[4].toString());
		dmSales.addRow(records);
	    }
	    rsCustomerWise.close();

	    Object[] ob1
		    =
		    {
			"Total", "", "", decFormat.format(billCount), gDecimalFormat.format(grandTotal)
		    };
	    dm1.addRow(ob1);
	    if (!flgRecords)
	    {
		dm.setRowCount(0);
		dm1.setRowCount(0);
	    }

	    tblSales.setSize(400, 400);
	    tblSales.setRowHeight(25);
	    tblSales.setModel(dmSales);

	    tblTotal.setSize(400, 400);
	    tblTotal.setRowHeight(40);
	    tblTotal.setModel(dm1);

	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	    centerRenderer.setHorizontalAlignment(JLabel.CENTER);

	    tblSales.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
	    tblSales.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);

	    tblSales.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

	    tblSales.getColumnModel().getColumn(0).setPreferredWidth(200);

	    tblTotal.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
	    tblTotal.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);

	    tblTotal.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

	    tblTotal.getColumnModel().getColumn(0).setPreferredWidth(200);

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    sbSqlLiveBill = null;
	    sbSqlQFileBill = null;
	    sbSqlFilters = null;
	}
    }

    /**
     * Customer Wise Item Sales
     */
    private void funCustomerWiseItemSales()
    {
	StringBuilder sbSqlLiveBill = new StringBuilder();
	StringBuilder sbSqlQFileBill = new StringBuilder();
	StringBuilder sbSqlFilters = new StringBuilder();
	try
	{
	    sql = "";
	    exportFormName = "CustomerWiseItemSales";
	    fromDate = funGetFromDate();
	    toDate = funGetToDate();
	    funGetFromTime();
	    funGetToTime();
	    String DateFrom = null, field = null, DateTo = null;
	    if (funGetFromTime() != null)
	    {
		DateFrom = fromDate + " " + funGetFromTime();
		field = "dteBillDate";
	    }
	    else
	    {
		DateFrom = fromDate;
		field = "date(dteBillDate)";
	    }
	    if (funGetToTime() != null)
	    {
		DateTo = toDate + " " + funGetToTime();
	    }
	    else
	    {
		DateTo = toDate;
	    }

	    dm = new DefaultTableModel()
	    {
		@Override
		public boolean isCellEditable(int row, int column)
		{
		    return false;
		}
	    };

	    dm.addColumn("Bill No");
	    dm.addColumn("Bill Date");
	    dm.addColumn("Customer Code");
	    dm.addColumn("Customer Name");
	    dm.addColumn("Item Name");
	    dm.addColumn("Quantity");
	    dm.addColumn("Amount");

	    vSalesReportExcelColLength = new java.util.Vector();
	    vSalesReportExcelColLength.add("6#Left");
	    vSalesReportExcelColLength.add("6#Left");
	    vSalesReportExcelColLength.add("6#Left");
	    vSalesReportExcelColLength.add("10#Left");
	    vSalesReportExcelColLength.add("10#Left");
	    vSalesReportExcelColLength.add("6#Left");
	    vSalesReportExcelColLength.add("10#Left");

	    DefaultTableModel dm1 = new DefaultTableModel();
	    dm1.addColumn("Total");
	    dm1.addColumn("");
	    dm1.addColumn("");
	    dm1.addColumn("Sales Amount");
	    tblTotal.setModel(dm1);
	    records = new Object[7];
	    String pos = funGetSelectedPosCode();

	    sbSqlLiveBill.setLength(0);
	    sbSqlQFileBill.setLength(0);
	    sbSqlFilters.setLength(0);

	    sbSqlLiveBill.append("select a.strBillNo,date(a.dteBillDate)"
		    + ",c.strCustomerCode,c.strCustomerName,d.strItemName"
		    + ",sum(b.dblQuantity),sum(b.dblAmount),'" + clsGlobalVarClass.gUserCode + "' "
		    + "from tblbillhd a,tblbilldtl b,tblcustomermaster c,tblitemmaster d "
		    + "where a.strBillNo=b.strBillNo and a.strClientCode=b.strClientCode and a.strCustomerCode=c.strCustomerCode "
		    + "and b.strItemCode=d.strItemCode and a.strCustomerCode='" + txtCustomerCode.getText() + "'"
		    + "and date(a.dteBillDate) between '" + DateFrom + "' and '" + DateTo + "'");

	    sbSqlQFileBill.append("select a.strBillNo,date(a.dteBillDate)"
		    + ",c.strCustomerCode,c.strCustomerName,d.strItemName"
		    + ",sum(b.dblQuantity),sum(b.dblAmount),'" + clsGlobalVarClass.gUserCode + "' "
		    + "from tblqbillhd a,tblqbilldtl b,tblcustomermaster c,tblitemmaster d "
		    + "where a.strBillNo=b.strBillNo and a.strClientCode=b.strClientCode and a.strCustomerCode=c.strCustomerCode "
		    + "and b.strItemCode=d.strItemCode and a.strCustomerCode='" + txtCustomerCode.getText() + "'"
		    + "and date(a.dteBillDate) between '" + DateFrom + "' and '" + DateTo + "'");

	    if (!pos.equals("All"))
	    {
		sbSqlFilters.append(" and " + objUtility.funGetSelectedPOSCodeString("a.strPOSCode", selectedPOSCodeSet) + " ");
	    }
	    if (!cmbOperator.getSelectedItem().equals("All"))
	    {
		sbSqlFilters.append(" and  a.strUserCreated='" + cmbOperator.getSelectedItem().toString() + "' ");
	    }
	    if (!cmbPayMode.getSelectedItem().equals("All"))
	    {
		sbSqlFilters.append(" and a.strSettelmentMode='" + cmbPayMode.getSelectedItem().toString() + "' ");
	    }
	    if (txtBillNofrom.getText().trim().length() > 0 && txtBillnoTo.getText().trim().length() > 0)
	    {
		sbSqlFilters.append(" and a.strBillNo between '" + txtBillNofrom.getText() + "' and '" + txtBillnoTo.getText() + "'");
	    }
	    if (clsGlobalVarClass.gEnableShiftYN && (!cmbShift.getSelectedItem().toString().equalsIgnoreCase("All")))
	    {
		sbSqlFilters.append(" AND a.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ");
	    }
	    if (!cmbArea.getSelectedItem().equals("All"))
	    {
		sbSqlFilters.append(" and a.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ");
	    }
	    if (!cmbOperationType.getSelectedItem().toString().equalsIgnoreCase("All"))
	    {
		sbSqlFilters.append(" and a.strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ");
	    }
	    if (!txtCustomerCode.getText().equalsIgnoreCase(""))
	    {
		sbSqlFilters.append(" and a.strCustomerCode='" + txtCustomerCode.getText() + "' ");
	    }

	    sbSqlFilters.append(" group by a.strBillNo");

	    boolean flgRecords = false;
	    double qty = 0, amount = 0;

	    sbSqlLiveBill.append(sbSqlFilters);
	    sbSqlQFileBill.append(sbSqlFilters);
	    //System.out.println(sbSqlLiveBill);
	    //System.out.println(sbSqlQFileBill);

	    DecimalFormat decFormat = new DecimalFormat("0");

	    clsSalesFlashReport obj = new clsSalesFlashReport();
	    obj.funProcessSalesFlashReport(sbSqlLiveBill.toString(), sbSqlQFileBill.toString(), "CustWiseItemSales");

	    sql = "select * from tbltempsalesflash1 where strUser='" + clsGlobalVarClass.gUserCode + "'";
	    ResultSet rsCustomerWise = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsCustomerWise.next())
	    {
		flgRecords = true;
		records[0] = rsCustomerWise.getString(1);//Bill No
		String tempBillDate = rsCustomerWise.getString(2);
		String[] spDate = tempBillDate.split("-");
		records[1] = spDate[2] + "-" + spDate[1] + "-" + spDate[0];//Bill Date
		records[2] = rsCustomerWise.getString(3);//Cust Code
		records[3] = rsCustomerWise.getString(4);//Cust Name
		records[4] = rsCustomerWise.getString(5);//Item Name
		records[5] = decFormat.format(rsCustomerWise.getDouble(6));//Qty
		records[6] = gDecimalFormat.format(rsCustomerWise.getDouble(7));//Amount

		qty += Double.parseDouble(records[5].toString());
		amount += Double.parseDouble(records[6].toString());
		dm.addRow(records);
	    }
	    rsCustomerWise.close();
	    Object[] ob1
		    =
		    {
			"Total", "", decFormat.format(qty), gDecimalFormat.format(amount)
		    };
	    dm1.addRow(ob1);

	    if (!flgRecords)
	    {
		dm.setRowCount(0);
		dm1.setRowCount(0);
	    }

	    tblTotal.setModel(dm1);
	    tblSales.setModel(dm);
	    tblSales.setRowHeight(25);
	    tblTotal.setRowHeight(40);
	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
	    tblSales.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
	    tblSales.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);

	    tblSales.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    tblSales.getColumnModel().getColumn(0).setPreferredWidth(80);
	    tblSales.getColumnModel().getColumn(1).setPreferredWidth(80);
	    tblSales.getColumnModel().getColumn(2).setPreferredWidth(80);
	    tblSales.getColumnModel().getColumn(3).setPreferredWidth(200);
	    tblSales.getColumnModel().getColumn(4).setPreferredWidth(200);
	    tblSales.getColumnModel().getColumn(5).setPreferredWidth(70);
	    tblSales.getColumnModel().getColumn(6).setPreferredWidth(72);

	    tblTotal.setSize(400, 400);
	    DefaultTableCellRenderer rightRenderer1 = new DefaultTableCellRenderer();
	    rightRenderer1.setHorizontalAlignment(JLabel.RIGHT);
	    tblTotal.getColumnModel().getColumn(2).setCellRenderer(rightRenderer1);
	    tblTotal.getColumnModel().getColumn(3).setCellRenderer(rightRenderer1);
	    tblTotal.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    tblTotal.getColumnModel().getColumn(0).setPreferredWidth(420);
	    tblTotal.getColumnModel().getColumn(1).setPreferredWidth(230);
	    tblTotal.getColumnModel().getColumn(2).setPreferredWidth(70);
	    tblTotal.getColumnModel().getColumn(3).setPreferredWidth(70);

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    sbSqlLiveBill = null;
	    sbSqlQFileBill = null;
	    sbSqlFilters = null;
	}
    }

    private void funSalesReportButton1Clicked()
    {
	if (navigate == 0)
	{
	    exportFormName = "SettlementWise";

	    funSettlementWise();
	}
	else if (navigate == 1)
	{
	    exportFormName = "WaiterWise";
	    funWaiterWise();
	}
	else if (navigate == 2)
	{
	    exportFormName = "DayWise";
	    funDayWiseSales();
	}
	else if (navigate == 3)
	{
	    exportFormName = "MonthlySalesReport";
	    funMonthlySalesReport();
	}
    }

    private void funSalesReportButton2Clicked()
    {
	if (navigate == 0)
	{
	    exportFormName = "BillWise";
	    funBillWise();
	}
	if (navigate == 1)
	{
	    exportFormName = "DeliveryBoyWise";
	    funDeliveryBoyWiseReport();
	}
	if (navigate == 2)
	{
	    exportFormName = "TaxWise";
	    funTaxWiseSales();
	}
	/*
         * else if (navigate == 3) { exportFormName =
         * "ItemWiseConsumptionReport"; funItemWiseConsumption();
         }
	 */
    }

    private void funSalesReportButton3Clicked()
    {
	if (navigate == 0)
	{
	    exportFormName = "ItemWise";
	    funItemWise();
	}
	if (navigate == 1)
	{
	    exportFormName = "CostCenterWise";
	    funCostCentreWise();
	}
	if (navigate == 2)
	{
	    exportFormName = "BillWiseTip";
	    funBillWiseTips();
	}
    }

    private void funSalesReportButton4Clicked()
    {
	if (navigate == 0)
	{
	    exportFormName = "MenuWise";
	    funMenuWise();
	}
	if (navigate == 1)
	{
	    exportFormName = "HomeDeliveryWise";
	    funHomeDeliveryWise();
	}
	if (navigate == 2)
	{
	    exportFormName = "ModifierWise";
	    funModifierWise();
	}
    }

    private void funSalesReportButton5Clicked()
    {
	if (navigate == 0)
	{
	    exportFormName = "GroupWise";
	    funGroupWise();
	}
	if (navigate == 1)
	{
	    exportFormName = "TableWise";
	    funTableWise();
	}
	if (navigate == 2)
	{
	    exportFormName = "MenuHeadWiseWithModifier";
	    funMenuWiseWithModifier();
	}
    }

    private void funSalesReportButton6Clicked()
    {
	if (navigate == 0)
	{
	    exportFormName = "SubGroupWise";
	    funSubGroupWise();
	}
	if (navigate == 1)
	{
	    exportFormName = "HourWise";
	    funHourWiseReport();
	}
	if (navigate == 2)
	{
	    exportFormName = "HourWiseItems";
	    funHourWiseItemReport();
	}
    }

    private void funSalesReportButton7Clicked()
    {
	if (navigate == 0)
	{
	    exportFormName = "CustomerWiseSales";
	    if (cmbCustWiseReportType.getSelectedItem().toString().equalsIgnoreCase("Item Wise"))
	    {
		if (txtCustomerCode.getText().trim().length() == 0)
		{
		    JOptionPane.showMessageDialog(this, "Please Select Customer");
		    return;
		}
		funCustomerWiseItemSales();
	    }
	    else if (cmbCustWiseReportType.getSelectedItem().toString().equalsIgnoreCase("Customer Wise"))
	    {
		if (txtCustomerCode.getText().trim().length() == 0)
		{
		    JOptionPane.showMessageDialog(this, "Please Select Customer");
		    return;
		}
		funCustomerWiseBillSales();
	    }
	    else
	    {
		funCustomerWiseSales();
	    }
	}
	if (navigate == 1)
	{
	    exportFormName = "AreaWise";
	    funAreaWise();
	}
	if (navigate == 2)
	{
	    exportFormName = "OperatorWise";
	    funOperatorWise();
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

        pnlHeader = new javax.swing.JPanel();
        lblProductName = new javax.swing.JLabel();
        lblModuleName = new javax.swing.JLabel();
        lblformName = new javax.swing.JLabel();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 32767));
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        lblPosName = new javax.swing.JLabel();
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        lblUserCode = new javax.swing.JLabel();
        lblDate = new javax.swing.JLabel();
        lblHOSign = new javax.swing.JLabel();
        pnlBackGround = new JPanel() {      public void paintComponent(Graphics g) {        Image img = Toolkit.getDefaultToolkit().getImage(        getClass().getResource("/com/POSGlobal/images/imgBGJPOS.png"));        g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);        }      };  ;
        pnlMain = new javax.swing.JPanel();
        pnlSales = new javax.swing.JTabbedPane();
        pnlsalesDetail = new javax.swing.JPanel();
        pnlSalesTotal = new javax.swing.JScrollPane();
        tblTotal = new javax.swing.JTable();
        btnExport = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        btnReport5 = new javax.swing.JButton();
        btnReport1 = new javax.swing.JButton();
        btnReport3 = new javax.swing.JButton();
        btnReport2 = new javax.swing.JButton();
        btnReport4 = new javax.swing.JButton();
        btnPrevious = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        lblFromDate = new javax.swing.JLabel();
        cmbPosCode = new javax.swing.JComboBox();
        lblposname = new javax.swing.JLabel();
        dteFromDate = new com.toedter.calendar.JDateChooser();
        lblToDate = new javax.swing.JLabel();
        dteToDate = new com.toedter.calendar.JDateChooser();
        btnReport6 = new javax.swing.JButton();
        pnlSalesData = new javax.swing.JScrollPane();
        tblSales = new javax.swing.JTable();
        btnReport7 = new javax.swing.JButton();
        lblBackground1 = new javax.swing.JLabel();
        pnlGraph = new javax.swing.JPanel();
        lblName = new javax.swing.JLabel();
        pnlAdvanceFilter = new javax.swing.JPanel();
        lblFromBillNo = new javax.swing.JLabel();
        txtBillNofrom = new javax.swing.JTextField();
        txtBillnoTo = new javax.swing.JTextField();
        lbloperator = new javax.swing.JLabel();
        cmbOperator = new javax.swing.JComboBox();
        cmbhour = new javax.swing.JComboBox();
        cmbMinute = new javax.swing.JComboBox();
        cmbampmFrom = new javax.swing.JComboBox();
        cmbhour1 = new javax.swing.JComboBox();
        cmbMinute1 = new javax.swing.JComboBox();
        cmbampmTo = new javax.swing.JComboBox();
        lblToDateTime = new javax.swing.JLabel();
        lblTimeFromDate = new javax.swing.JLabel();
        lblToBillNo = new javax.swing.JLabel();
        lblpayMode = new javax.swing.JLabel();
        cmbPayMode = new javax.swing.JComboBox();
        lblCustomer = new javax.swing.JLabel();
        txtCustomerCode = new javax.swing.JTextField();
        lblCustomerName = new javax.swing.JLabel();
        cmbCustWiseReportType = new javax.swing.JComboBox();
        lblReportType = new javax.swing.JLabel();
        cmbType = new javax.swing.JComboBox();
        lblType = new javax.swing.JLabel();
        chkConsolidatePOS = new javax.swing.JCheckBox();
        lblShift = new javax.swing.JLabel();
        cmbShift = new javax.swing.JComboBox();
        lblArea = new javax.swing.JLabel();
        cmbArea = new javax.swing.JComboBox();
        cmbOperationType = new javax.swing.JComboBox();
        lblOperationType = new javax.swing.JLabel();
        lblCurrency = new javax.swing.JLabel();
        cmbCurrency = new javax.swing.JComboBox();

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

        pnlHeader.setBackground(new java.awt.Color(69, 164, 238));
        pnlHeader.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                pnlHeaderMouseClicked(evt);
            }
        });
        pnlHeader.setLayout(new javax.swing.BoxLayout(pnlHeader, javax.swing.BoxLayout.LINE_AXIS));

        lblProductName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblProductName.setForeground(new java.awt.Color(255, 255, 255));
        lblProductName.setText("SPOS -");
        pnlHeader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        pnlHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText(" - Point of Sales");
        lblformName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblformNameMouseClicked(evt);
            }
        });
        pnlHeader.add(lblformName);
        pnlHeader.add(filler4);
        pnlHeader.add(filler5);

        lblPosName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPosName.setForeground(new java.awt.Color(255, 255, 255));
        lblPosName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPosName.setMaximumSize(new java.awt.Dimension(321, 30));
        lblPosName.setMinimumSize(new java.awt.Dimension(321, 30));
        lblPosName.setPreferredSize(new java.awt.Dimension(321, 30));
        lblPosName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblPosNameMouseClicked(evt);
            }
        });
        pnlHeader.add(lblPosName);
        pnlHeader.add(filler6);

        lblUserCode.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblUserCode.setForeground(new java.awt.Color(255, 255, 255));
        lblUserCode.setMaximumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setMinimumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setPreferredSize(new java.awt.Dimension(90, 30));
        lblUserCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblUserCodeMouseClicked(evt);
            }
        });
        pnlHeader.add(lblUserCode);

        lblDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDate.setForeground(new java.awt.Color(255, 255, 255));
        lblDate.setMaximumSize(new java.awt.Dimension(192, 30));
        lblDate.setMinimumSize(new java.awt.Dimension(192, 30));
        lblDate.setPreferredSize(new java.awt.Dimension(192, 30));
        lblDate.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblDateMouseClicked(evt);
            }
        });
        pnlHeader.add(lblDate);

        lblHOSign.setMaximumSize(new java.awt.Dimension(34, 30));
        lblHOSign.setMinimumSize(new java.awt.Dimension(34, 30));
        lblHOSign.setPreferredSize(new java.awt.Dimension(34, 30));
        lblHOSign.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblHOSignMouseClicked(evt);
            }
        });
        pnlHeader.add(lblHOSign);

        getContentPane().add(pnlHeader, java.awt.BorderLayout.PAGE_START);

        pnlBackGround.setLayout(new java.awt.GridBagLayout());

        pnlMain.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        pnlMain.setMinimumSize(new java.awt.Dimension(800, 570));
        pnlMain.setOpaque(false);

        pnlsalesDetail.setBackground(new java.awt.Color(255, 255, 255));
        pnlsalesDetail.setPreferredSize(new java.awt.Dimension(800, 508));
        pnlsalesDetail.setLayout(null);

        tblTotal.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        tblTotal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null, null, null, null}
            },
            new String []
            {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblTotal.setRowHeight(25);
        pnlSalesTotal.setViewportView(tblTotal);

        pnlsalesDetail.add(pnlSalesTotal);
        pnlSalesTotal.setBounds(0, 390, 798, 70);

        btnExport.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnExport.setForeground(new java.awt.Color(255, 255, 255));
        btnExport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCommonButtonDark.png"))); // NOI18N
        btnExport.setText("EXPORT");
        btnExport.setToolTipText("Export");
        btnExport.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExport.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCommonButtonLight.png"))); // NOI18N
        btnExport.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnExportMouseClicked(evt);
            }
        });
        pnlsalesDetail.add(btnExport);
        btnExport.setBounds(620, 0, 80, 40);

        btnBack.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnBack.setForeground(new java.awt.Color(255, 255, 255));
        btnBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCommonButtonDark.png"))); // NOI18N
        btnBack.setText("CLOSE");
        btnBack.setToolTipText("Close Window");
        btnBack.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBack.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCommonButtonLight.png"))); // NOI18N
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
        pnlsalesDetail.add(btnBack);
        btnBack.setBounds(710, 0, 80, 40);

        btnReport5.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        btnReport5.setForeground(new java.awt.Color(255, 255, 255));
        btnReport5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgGroupWise.png"))); // NOI18N
        btnReport5.setToolTipText("View Report");
        btnReport5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReport5.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnReport5ActionPerformed(evt);
            }
        });
        pnlsalesDetail.add(btnReport5);
        btnReport5.setBounds(450, 460, 80, 80);

        btnReport1.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        btnReport1.setForeground(new java.awt.Color(255, 255, 255));
        btnReport1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgSettlementWise.png"))); // NOI18N
        btnReport1.setToolTipText("View Report");
        btnReport1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReport1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnReport1ActionPerformed(evt);
            }
        });
        pnlsalesDetail.add(btnReport1);
        btnReport1.setBounds(90, 460, 80, 80);

        btnReport3.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        btnReport3.setForeground(new java.awt.Color(255, 255, 255));
        btnReport3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgItemWise.png"))); // NOI18N
        btnReport3.setToolTipText("View Report");
        btnReport3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReport3.addAncestorListener(new javax.swing.event.AncestorListener()
        {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt)
            {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt)
            {
                btnReport3AncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt)
            {
            }
        });
        btnReport3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnReport3ActionPerformed(evt);
            }
        });
        pnlsalesDetail.add(btnReport3);
        btnReport3.setBounds(270, 460, 80, 80);

        btnReport2.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        btnReport2.setForeground(new java.awt.Color(255, 255, 255));
        btnReport2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgBillWise.png"))); // NOI18N
        btnReport2.setToolTipText("View Report");
        btnReport2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReport2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnReport2ActionPerformed(evt);
            }
        });
        pnlsalesDetail.add(btnReport2);
        btnReport2.setBounds(180, 460, 80, 80);

        btnReport4.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        btnReport4.setForeground(new java.awt.Color(255, 255, 255));
        btnReport4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgMenuHeadWise.png"))); // NOI18N
        btnReport4.setToolTipText("View Report");
        btnReport4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReport4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnReport4ActionPerformed(evt);
            }
        });
        pnlsalesDetail.add(btnReport4);
        btnReport4.setBounds(360, 460, 80, 80);

        btnPrevious.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        btnPrevious.setForeground(new java.awt.Color(255, 255, 255));
        btnPrevious.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgPrevFlash.png"))); // NOI18N
        btnPrevious.setToolTipText("Go Previous");
        btnPrevious.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrevious.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgPreviousReport.png"))); // NOI18N
        btnPrevious.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnPreviousMouseClicked(evt);
            }
        });
        pnlsalesDetail.add(btnPrevious);
        btnPrevious.setBounds(0, 460, 75, 80);

        btnNext.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        btnNext.setForeground(new java.awt.Color(255, 255, 255));
        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgNextFlash.png"))); // NOI18N
        btnNext.setToolTipText("Go Next");
        btnNext.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNext.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgNextReport.png"))); // NOI18N
        btnNext.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNextMouseClicked(evt);
            }
        });
        btnNext.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNextActionPerformed(evt);
            }
        });
        pnlsalesDetail.add(btnNext);
        btnNext.setBounds(720, 460, 75, 80);

        lblFromDate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblFromDate.setText("From Date :");
        pnlsalesDetail.add(lblFromDate);
        lblFromDate.setBounds(240, 0, 70, 29);

        cmbPosCode.setToolTipText("Select POS");
        cmbPosCode.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbPosCodeActionPerformed(evt);
            }
        });
        pnlsalesDetail.add(cmbPosCode);
        cmbPosCode.setBounds(80, 0, 150, 30);

        lblposname.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblposname.setText("POS Name :");
        pnlsalesDetail.add(lblposname);
        lblposname.setBounds(10, 0, 70, 30);

        dteFromDate.setToolTipText("Select From Date");
        dteFromDate.setPreferredSize(new java.awt.Dimension(119, 35));
        dteFromDate.addHierarchyListener(new java.awt.event.HierarchyListener()
        {
            public void hierarchyChanged(java.awt.event.HierarchyEvent evt)
            {
                dteFromDateHierarchyChanged(evt);
            }
        });
        dteFromDate.addPropertyChangeListener(new java.beans.PropertyChangeListener()
        {
            public void propertyChange(java.beans.PropertyChangeEvent evt)
            {
                dteFromDatePropertyChange(evt);
            }
        });
        pnlsalesDetail.add(dteFromDate);
        dteFromDate.setBounds(310, 0, 130, 30);

        lblToDate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblToDate.setText("To :");
        pnlsalesDetail.add(lblToDate);
        lblToDate.setBounds(450, 0, 30, 30);

        dteToDate.setToolTipText("Select To Date");
        dteToDate.setPreferredSize(new java.awt.Dimension(119, 35));
        dteToDate.addHierarchyListener(new java.awt.event.HierarchyListener()
        {
            public void hierarchyChanged(java.awt.event.HierarchyEvent evt)
            {
                dteToDateHierarchyChanged(evt);
            }
        });
        dteToDate.addPropertyChangeListener(new java.beans.PropertyChangeListener()
        {
            public void propertyChange(java.beans.PropertyChangeEvent evt)
            {
                dteToDatePropertyChange(evt);
            }
        });
        pnlsalesDetail.add(dteToDate);
        dteToDate.setBounds(480, 0, 130, 30);

        btnReport6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgSubGroupWise.png"))); // NOI18N
        btnReport6.setToolTipText("View Report");
        btnReport6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReport6.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnReport6ActionPerformed(evt);
            }
        });
        pnlsalesDetail.add(btnReport6);
        btnReport6.setBounds(540, 460, 80, 80);

        tblSales.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String []
            {
                "Bill No", "Date", "Operator", "Settlement Mode", "Discount", "Tax", "Grand Total"
            }
        ));
        tblSales.setFillsViewportHeight(true);
        tblSales.setRowHeight(25);
        tblSales.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblSalesMouseClicked(evt);
            }
        });
        pnlSalesData.setViewportView(tblSales);
        if (tblSales.getColumnModel().getColumnCount() > 0)
        {
            tblSales.getColumnModel().getColumn(0).setHeaderValue("Bill No");
            tblSales.getColumnModel().getColumn(1).setHeaderValue("Date");
            tblSales.getColumnModel().getColumn(2).setHeaderValue("Operator");
            tblSales.getColumnModel().getColumn(3).setHeaderValue("Settlement Mode");
            tblSales.getColumnModel().getColumn(4).setHeaderValue("Discount");
            tblSales.getColumnModel().getColumn(5).setHeaderValue("Tax");
            tblSales.getColumnModel().getColumn(6).setHeaderValue("Grand Total");
        }

        pnlsalesDetail.add(pnlSalesData);
        pnlSalesData.setBounds(0, 40, 800, 350);

        btnReport7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCustWise.png"))); // NOI18N
        btnReport7.setToolTipText("View Report");
        btnReport7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReport7.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnReport7ActionPerformed(evt);
            }
        });
        pnlsalesDetail.add(btnReport7);
        btnReport7.setBounds(630, 460, 80, 80);

        lblBackground1.setBackground(new java.awt.Color(255, 255, 255));
        lblBackground1.setOpaque(true);
        pnlsalesDetail.add(lblBackground1);
        lblBackground1.setBounds(0, -30, 800, 560);

        lblName.setText("                                                                                               ");

        javax.swing.GroupLayout pnlGraphLayout = new javax.swing.GroupLayout(pnlGraph);
        pnlGraph.setLayout(pnlGraphLayout);
        pnlGraphLayout.setHorizontalGroup(
            pnlGraphLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblName, javax.swing.GroupLayout.DEFAULT_SIZE, 820, Short.MAX_VALUE)
        );
        pnlGraphLayout.setVerticalGroup(
            pnlGraphLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
        );

        pnlsalesDetail.add(pnlGraph);
        pnlGraph.setBounds(0, 60, 820, 420);

        pnlSales.addTab("Data", pnlsalesDetail);

        pnlAdvanceFilter.setOpaque(false);
        pnlAdvanceFilter.setLayout(null);

        lblFromBillNo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblFromBillNo.setText("From Bill No.    :");
        pnlAdvanceFilter.add(lblFromBillNo);
        lblFromBillNo.setBounds(110, 200, 90, 32);

        txtBillNofrom.setToolTipText("Enter From Bill No");
        txtBillNofrom.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtBillNofromMouseClicked(evt);
            }
        });
        txtBillNofrom.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtBillNofromActionPerformed(evt);
            }
        });
        pnlAdvanceFilter.add(txtBillNofrom);
        txtBillNofrom.setBounds(200, 200, 180, 30);

        txtBillnoTo.setToolTipText("Enter To Bill No");
        txtBillnoTo.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtBillnoToMouseClicked(evt);
            }
        });
        pnlAdvanceFilter.add(txtBillnoTo);
        txtBillnoTo.setBounds(470, 200, 200, 30);

        lbloperator.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbloperator.setText("Operator        :");
        pnlAdvanceFilter.add(lbloperator);
        lbloperator.setBounds(110, 100, 90, 26);

        cmbOperator.setToolTipText("Select Operator");
        pnlAdvanceFilter.add(cmbOperator);
        cmbOperator.setBounds(200, 100, 180, 30);

        cmbhour.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbhour.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "HH", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));
        cmbhour.setToolTipText("Select HH");
        pnlAdvanceFilter.add(cmbhour);
        cmbhour.setBounds(200, 150, 50, 30);

        cmbMinute.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbMinute.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "MM", "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        cmbMinute.setToolTipText("Select MM");
        pnlAdvanceFilter.add(cmbMinute);
        cmbMinute.setBounds(260, 150, 60, 30);

        cmbampmFrom.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbampmFrom.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "AM", "PM" }));
        cmbampmFrom.setToolTipText("Select AM/PM");
        pnlAdvanceFilter.add(cmbampmFrom);
        cmbampmFrom.setBounds(330, 150, 50, 30);

        cmbhour1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbhour1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "HH", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));
        cmbhour1.setToolTipText("Select HH");
        pnlAdvanceFilter.add(cmbhour1);
        cmbhour1.setBounds(470, 150, 60, 30);

        cmbMinute1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbMinute1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "MM", "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        cmbMinute1.setToolTipText("Select MM");
        pnlAdvanceFilter.add(cmbMinute1);
        cmbMinute1.setBounds(540, 150, 60, 30);

        cmbampmTo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbampmTo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "AM", "PM" }));
        cmbampmTo.setToolTipText("Select AM/PM");
        pnlAdvanceFilter.add(cmbampmTo);
        cmbampmTo.setBounds(610, 150, 60, 30);

        lblToDateTime.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblToDateTime.setText("To Time    :");
        pnlAdvanceFilter.add(lblToDateTime);
        lblToDateTime.setBounds(400, 150, 70, 30);

        lblTimeFromDate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTimeFromDate.setText("Time From      :");
        pnlAdvanceFilter.add(lblTimeFromDate);
        lblTimeFromDate.setBounds(110, 150, 90, 30);

        lblToBillNo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblToBillNo.setText("To Bill No.  :");
        pnlAdvanceFilter.add(lblToBillNo);
        lblToBillNo.setBounds(400, 200, 70, 30);

        lblpayMode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblpayMode.setText("Pay Mode   :");
        pnlAdvanceFilter.add(lblpayMode);
        lblpayMode.setBounds(400, 100, 70, 30);

        cmbPayMode.setToolTipText("Select Pay Mode");
        pnlAdvanceFilter.add(cmbPayMode);
        cmbPayMode.setBounds(470, 100, 200, 30);

        lblCustomer.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCustomer.setText("Customer       :");
        pnlAdvanceFilter.add(lblCustomer);
        lblCustomer.setBounds(110, 300, 90, 30);

        txtCustomerCode.setToolTipText("Enter Customer");
        txtCustomerCode.setEnabled(false);
        txtCustomerCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtCustomerCodeMouseClicked(evt);
            }
        });
        pnlAdvanceFilter.add(txtCustomerCode);
        txtCustomerCode.setBounds(200, 300, 180, 30);
        pnlAdvanceFilter.add(lblCustomerName);
        lblCustomerName.setBounds(400, 300, 280, 30);

        cmbCustWiseReportType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbCustWiseReportType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Bill Wise", "Customer Wise", "Item Wise" }));
        cmbCustWiseReportType.setToolTipText("Select Report Type");
        pnlAdvanceFilter.add(cmbCustWiseReportType);
        cmbCustWiseReportType.setBounds(200, 250, 180, 30);

        lblReportType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblReportType.setText("Report Type   :");
        pnlAdvanceFilter.add(lblReportType);
        lblReportType.setBounds(110, 250, 90, 30);

        cmbType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Data", "Chart", " " }));
        cmbType.setToolTipText("Select Pay Mode");
        cmbType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbTypeActionPerformed(evt);
            }
        });
        pnlAdvanceFilter.add(cmbType);
        cmbType.setBounds(470, 250, 200, 30);

        lblType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblType.setText("Type         :");
        pnlAdvanceFilter.add(lblType);
        lblType.setBounds(400, 260, 70, 15);

        chkConsolidatePOS.setText("Consolidate POS");
        pnlAdvanceFilter.add(chkConsolidatePOS);
        chkConsolidatePOS.setBounds(470, 410, 140, 23);

        lblShift.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblShift.setText("Shift          :");
        pnlAdvanceFilter.add(lblShift);
        lblShift.setBounds(400, 350, 90, 26);

        cmbShift.setToolTipText("Select Operator");
        cmbShift.setOpaque(false);
        cmbShift.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbShiftActionPerformed(evt);
            }
        });
        pnlAdvanceFilter.add(cmbShift);
        cmbShift.setBounds(470, 350, 200, 30);

        lblArea.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblArea.setText("Area              :");
        pnlAdvanceFilter.add(lblArea);
        lblArea.setBounds(110, 350, 90, 26);

        cmbArea.setToolTipText("Select Operator");
        cmbArea.setOpaque(false);
        pnlAdvanceFilter.add(cmbArea);
        cmbArea.setBounds(200, 350, 180, 30);

        cmbOperationType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All", "DineIn", "DirectBiller", "HomeDelivery", "TakeAway" }));
        cmbOperationType.setToolTipText("Select POS");
        cmbOperationType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbOperationTypeActionPerformed(evt);
            }
        });
        pnlAdvanceFilter.add(cmbOperationType);
        cmbOperationType.setBounds(210, 410, 170, 30);

        lblOperationType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblOperationType.setText("Operation Type :");
        pnlAdvanceFilter.add(lblOperationType);
        lblOperationType.setBounds(110, 410, 94, 30);

        lblCurrency.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCurrency.setText("Currency          :");
        pnlAdvanceFilter.add(lblCurrency);
        lblCurrency.setBounds(110, 460, 92, 30);

        cmbCurrency.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "BASE", "USD" }));
        cmbCurrency.setToolTipText("Select POS");
        cmbCurrency.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbCurrencyActionPerformed(evt);
            }
        });
        pnlAdvanceFilter.add(cmbCurrency);
        cmbCurrency.setBounds(210, 460, 170, 30);

        pnlSales.addTab("Advance Filter", pnlAdvanceFilter);

        javax.swing.GroupLayout pnlMainLayout = new javax.swing.GroupLayout(pnlMain);
        pnlMain.setLayout(pnlMainLayout);
        pnlMainLayout.setHorizontalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMainLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(pnlSales, javax.swing.GroupLayout.PREFERRED_SIZE, 800, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlMainLayout.setVerticalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlSales, javax.swing.GroupLayout.PREFERRED_SIZE, 569, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pnlBackGround.add(pnlMain, new java.awt.GridBagConstraints());

        getContentPane().add(pnlBackGround, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Export File Report
     *
     * @param evt
     */
    private void btnExportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExportMouseClicked
	// TODO add your handling code here:
	try
	{
	    File theDir = new File(ExportReportPath);
	    File file = new File(ExportReportPath + File.separator + exportFormName + rDate + ".xls");
	    if (!theDir.exists())
	    {
		theDir.mkdir();
		funExportFile(tblSales, file);
		//sendMail();
	    }
	    else
	    {
		funExportFile(tblSales, file);
		//sendMail();
	    }
	}
	catch (Exception ex)
	{
	    ex.printStackTrace();
	}
    }//GEN-LAST:event_btnExportMouseClicked

    private void btnBackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBackMouseClicked
	// TODO add your handling code here:
	dispose();
    }//GEN-LAST:event_btnBackMouseClicked

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
	// TODO add your handling code here:
	dispose();
	clsGlobalVarClass.hmActiveForms.remove("Sales Report");
    }//GEN-LAST:event_btnBackActionPerformed

    /**
     * Previous button Events
     *
     * @param evt
     */
    private void btnPreviousMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPreviousMouseClicked
	// TODO add your handling code here:
	try
	{
	    if (btnPrevious.isEnabled())
	    {
		navigate--;
		if (navigate == 2)
		{
		    navigate--;
		}
	    }

	    if (navigate == 0)
	    {
		btnPrevious.setEnabled(false);
		btnReport1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgSettlementWise.png")));
		btnReport2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgBillWise.png")));
		btnReport3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgItemWise.png")));
		btnReport4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgMenuHeadWise.png")));
		btnReport5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgGroupWise.png")));
		btnReport6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgSubGroupWise.png")));
		btnReport7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCustWise.png")));
		btnNext.setEnabled(true);
	    }
	    if (navigate == 1)
	    {
		btnPrevious.setEnabled(true);
		btnReport1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgWaiterWise.png")));
		btnReport2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgDeliveryBoyWise.png")));
		btnReport3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCostCenterWise.png")));
		btnReport4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgHomeDeliveryWise.png")));
		btnReport5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgTableWise.png")));
		btnReport6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgHourlyWise.png")));
		btnReport7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgAreaWise.png")));

		btnNext.setEnabled(true);
		btnReport2.setVisible(true);
		btnReport3.setVisible(true);
		btnReport4.setVisible(true);
		btnReport5.setVisible(true);
		btnReport6.setVisible(true);
		btnReport7.setVisible(true);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }//GEN-LAST:event_btnPreviousMouseClicked
    /**
     * Next button Events
     *
     * @param evt
     */
    private void btnNextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNextMouseClicked
	// TODO add your handling code here:
	try
	{
	    if (btnNext.isEnabled())
	    {
		navigate++;
	    }
	    btnPrevious.setEnabled(true);
	    if (navigate == 1)
	    {
		btnReport1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgWaiterWise.png")));
		btnReport2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgDeliveryBoyWise.png")));
		btnReport3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCostCenterWise.png")));
		btnReport4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgHomeDeliveryWise.png")));
		btnReport5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgTableWise.png")));
		btnReport6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgHourlyWise.png")));
		btnReport7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgAreaWise.png")));
	    }
	    if (navigate == 2)
	    {
		btnReport1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgDayWiseSales.png")));
		btnReport2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgTaxWiseSales.png")));
		btnReport3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgTipreport.png")));
		btnReport4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgItemModifierWise.png")));
		btnReport5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgMenuHeadWiseWithModifier.png")));
		btnReport6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgItemHourlyWise.png")));
		btnReport7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgOperatorWise.png")));

//                btnNext.setEnabled(false);
	    }
	    if (navigate == 3)
	    {
		btnReport1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgMonthlySalesFlash.png")));
		btnReport2.setVisible(false);
		//btnReport2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgItemWiseConsumption.png")));                
		btnReport3.setVisible(false);
		btnReport4.setVisible(false);
		btnReport5.setVisible(false);
		btnReport6.setVisible(false);
		btnReport7.setVisible(false);

		btnNext.setEnabled(false);
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }//GEN-LAST:event_btnNextMouseClicked


    private void dteFromDateHierarchyChanged(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_dteFromDateHierarchyChanged

    }//GEN-LAST:event_dteFromDateHierarchyChanged

    private void dteFromDatePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dteFromDatePropertyChange

    }//GEN-LAST:event_dteFromDatePropertyChange

    private void dteToDateHierarchyChanged(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_dteToDateHierarchyChanged

    }//GEN-LAST:event_dteToDateHierarchyChanged

    private void dteToDatePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dteToDatePropertyChange

    }//GEN-LAST:event_dteToDatePropertyChange

    /**
     * Bill Reports
     *
     * @param evt
     */
    private void txtBillNofromMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtBillNofromMouseClicked
	// TODO add your handling code here:
	try
	{
	    objUtility.funCallForSearchForm("SalesReportBill");
	    new frmSearchFormDialog(null, true).setVisible(true);
	    if (clsGlobalVarClass.gSearchItemClicked)
	    {
		Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
		funSetDatatxtBillFrom(data);
		clsGlobalVarClass.gSearchItemClicked = false;
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtBillNofromMouseClicked

    private void txtBillNofromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBillNofromActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtBillNofromActionPerformed
    /**
     * Sales Report Bill
     *
     * @param evt
     */
    private void txtBillnoToMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtBillnoToMouseClicked
	// TODO add your handling code here:
	try
	{
	    objUtility.funCallForSearchForm("SalesReportBill");
	    new frmSearchFormDialog(null, true).setVisible(true);
	    if (clsGlobalVarClass.gSearchItemClicked)
	    {
		Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
		funSetDatatxtBillTo(data);
		clsGlobalVarClass.gSearchItemClicked = false;
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtBillnoToMouseClicked
    /**
     * get Customer Code
     *
     * @param evt
     */
    private void txtCustomerCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCustomerCodeMouseClicked
	// TODO add your handling code here:
	objUtility.funCallForSearchForm("CustomerMaster");
	new frmSearchFormDialog(this, true).setVisible(true);
	if (clsGlobalVarClass.gSearchItemClicked)
	{
	    Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
	    txtCustomerCode.setText(data[0].toString());
	    lblCustomerName.setText(data[1].toString());
	    clsGlobalVarClass.gSearchItemClicked = false;
	}
    }//GEN-LAST:event_txtCustomerCodeMouseClicked

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnNextActionPerformed
    {//GEN-HEADEREND:event_btnNextActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_btnNextActionPerformed

    private void tblSalesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSalesMouseClicked

	if (exportFormName.equalsIgnoreCase("BillWise"))
	{
	    int row = tblSales.getSelectedRow();
	    String billNo = tblSales.getValueAt(row, 0).toString();
	    String billDate = tblSales.getValueAt(row, 1).toString();
	    billDate = billDate.split("-")[2] + "-" + billDate.split("-")[1] + "-" + billDate.split("-")[0];
	    try
	    {
		if (evt.getClickCount() == 2)
		{
		    String POSCode = funGetSelectedPosCode(row);
		    objUtility.funPrintBill(billNo, "Sales Report", billDate, POSCode, "view");

		    /**
		     * save reprint audit
		     */
		    // objUtility2.funSaveReprintAudit("Reprint", "Bill", "", "Reprint the bill from sales flash", "", billNo, "");
		}
	    }
	    catch (Exception e)
	    {
		e.printStackTrace();
	    }
	    finally
	    {
	    }
	}
    }//GEN-LAST:event_tblSalesMouseClicked

    private void btnReport1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnReport1ActionPerformed
    {//GEN-HEADEREND:event_btnReport1ActionPerformed
	// TODO add your handling code here:
	funSalesReportButton1Clicked();
    }//GEN-LAST:event_btnReport1ActionPerformed

    private void btnReport2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnReport2ActionPerformed
    {//GEN-HEADEREND:event_btnReport2ActionPerformed
	// TODO add your handling code here:
	funSalesReportButton2Clicked();
    }//GEN-LAST:event_btnReport2ActionPerformed

    private void btnReport3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnReport3ActionPerformed
    {//GEN-HEADEREND:event_btnReport3ActionPerformed
	// TODO add your handling code here:
	funSalesReportButton3Clicked();
    }//GEN-LAST:event_btnReport3ActionPerformed

    private void btnReport4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnReport4ActionPerformed
    {//GEN-HEADEREND:event_btnReport4ActionPerformed
	// TODO add your handling code here:
	funSalesReportButton4Clicked();
    }//GEN-LAST:event_btnReport4ActionPerformed

    private void btnReport5ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnReport5ActionPerformed
    {//GEN-HEADEREND:event_btnReport5ActionPerformed
	// TODO add your handling code here:
	funSalesReportButton5Clicked();
    }//GEN-LAST:event_btnReport5ActionPerformed

    private void btnReport6ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnReport6ActionPerformed
    {//GEN-HEADEREND:event_btnReport6ActionPerformed
	// TODO add your handling code here:
	funSalesReportButton6Clicked();
    }//GEN-LAST:event_btnReport6ActionPerformed

    private void btnReport7ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnReport7ActionPerformed
    {//GEN-HEADEREND:event_btnReport7ActionPerformed
	// TODO add your handling code here:
	funSalesReportButton7Clicked();
    }//GEN-LAST:event_btnReport7ActionPerformed

    private void cmbTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTypeActionPerformed
	pnlSalesData.setVisible(true);
	pnlSalesTotal.setVisible(true);
	pnlGraph.setVisible(false);
    }//GEN-LAST:event_cmbTypeActionPerformed

    private void pnlHeaderMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_pnlHeaderMouseClicked
    {//GEN-HEADEREND:event_pnlHeaderMouseClicked
	// TODO add your handling code here:
	objUtility.funMinimizeWindow();
    }//GEN-LAST:event_pnlHeaderMouseClicked

    private void lblformNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblformNameMouseClicked
    {//GEN-HEADEREND:event_lblformNameMouseClicked
	// TODO add your handling code here:
	objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblformNameMouseClicked

    private void lblPosNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblPosNameMouseClicked
    {//GEN-HEADEREND:event_lblPosNameMouseClicked
	// TODO add your handling code here:
	objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblPosNameMouseClicked

    private void lblUserCodeMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblUserCodeMouseClicked
    {//GEN-HEADEREND:event_lblUserCodeMouseClicked
	// TODO add your handling code here:
	objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblUserCodeMouseClicked

    private void lblDateMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblDateMouseClicked
    {//GEN-HEADEREND:event_lblDateMouseClicked
	// TODO add your handling code here:
	objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblDateMouseClicked

    private void lblHOSignMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblHOSignMouseClicked
    {//GEN-HEADEREND:event_lblHOSignMouseClicked
	// TODO add your handling code here:
	objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblHOSignMouseClicked

    private void btnReport3AncestorAdded(javax.swing.event.AncestorEvent evt)//GEN-FIRST:event_btnReport3AncestorAdded
    {//GEN-HEADEREND:event_btnReport3AncestorAdded
	// TODO add your handling code here:
    }//GEN-LAST:event_btnReport3AncestorAdded

    private void cmbPosCodeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbPosCodeActionPerformed
    {//GEN-HEADEREND:event_cmbPosCodeActionPerformed

	try
	{
	    String posCode = funGetSelectedPosCode();

	    if (posCode.equalsIgnoreCase("Multiple"))
	    {
		frmMultiPOSSelection objMultiPOSSelection = new frmMultiPOSSelection(this);
		selectedPOSCodeSet = objMultiPOSSelection.funGetSelectedPOSCode();

	    }
	    else
	    {
		selectedPOSCodeSet.clear();
		selectedPOSCodeSet.add(posCode);

		fillShiftCombo();
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}

    }//GEN-LAST:event_cmbPosCodeActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
	// TODO add your handling code here:
	clsGlobalVarClass.hmActiveForms.remove("Sales Report");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
	// TODO add your handling code here:
	clsGlobalVarClass.hmActiveForms.remove("Sales Report");
    }//GEN-LAST:event_formWindowClosing

    private void cmbShiftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbShiftActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_cmbShiftActionPerformed

    private void cmbOperationTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbOperationTypeActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_cmbOperationTypeActionPerformed

    private void cmbCurrencyActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbCurrencyActionPerformed
    {//GEN-HEADEREND:event_cmbCurrencyActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_cmbCurrencyActionPerformed

    /**
     * method Print Bill
     *
     * @param billno
     * @param poscode
     * @param billDate
     */
    /*
     * private void funPrintBill(String billno, String poscode, String billDate)
     * { try { clsTextFileGeneratorForPrinting obj = new
     * clsTextFileGeneratorForPrinting(); if
     * (clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Format 1")) {
     * obj.funGenerateTextFileBillPrinting(billno, "reprint", "sales report",
     * "sale",billDate,"abc"); } else if
     * (clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Format 2")) {
     * obj.funGenerateTextFileBillPrintingForFormat2(billno, "reprint", "sales
     * report", "sale",billDate); } else if
     * (clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Format 3")) {
     * obj.funGenerateTextFileBillPrintingForFormat3(billno, "reprint", "sales
     * report", "sale",billDate); } else if
     * (clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Format 4")) {
     * obj.funGenerateTextFileBillPrintingForFormat4(billno, "reprint", "sales
     * report", "sale",billDate); } else if
     * (clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Format 5")) {
     * obj.funGenerateTextFileBillPrintingForFormat5(billno, "reprint", "sales
     * report", "sale",billDate); } else if
     * (clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Format 6")) {
     * obj.funGenerateTextFileBillPrintingForFormat6(billno, "reprint", "sales
     * report", "sale",billDate); } else if
     * (clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Format 7")) {
     * obj.funGenerateTextFileBillPrintingForFormat7(billno, "reprint", "sales
     * report", "sale",billDate); }
     *
     * }
     * catch (Exception ex) { StackTraceElement[] st = ex.getStackTrace();
     * ex.printStackTrace(); } finally { //
     * clsPrintBill.fun_DeleteFrom_tbltempprintbill(billno); }
     }
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnExport;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrevious;
    private javax.swing.JButton btnReport1;
    private javax.swing.JButton btnReport2;
    private javax.swing.JButton btnReport3;
    private javax.swing.JButton btnReport4;
    private javax.swing.JButton btnReport5;
    private javax.swing.JButton btnReport6;
    private javax.swing.JButton btnReport7;
    private javax.swing.JCheckBox chkConsolidatePOS;
    private javax.swing.JComboBox cmbArea;
    private javax.swing.JComboBox cmbCurrency;
    private javax.swing.JComboBox cmbCustWiseReportType;
    private javax.swing.JComboBox cmbMinute;
    private javax.swing.JComboBox cmbMinute1;
    private javax.swing.JComboBox cmbOperationType;
    private javax.swing.JComboBox cmbOperator;
    private javax.swing.JComboBox cmbPayMode;
    private javax.swing.JComboBox cmbPosCode;
    private javax.swing.JComboBox cmbShift;
    private javax.swing.JComboBox cmbType;
    private javax.swing.JComboBox cmbampmFrom;
    private javax.swing.JComboBox cmbampmTo;
    private javax.swing.JComboBox cmbhour;
    private javax.swing.JComboBox cmbhour1;
    private com.toedter.calendar.JDateChooser dteFromDate;
    private com.toedter.calendar.JDateChooser dteToDate;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblArea;
    private javax.swing.JLabel lblBackground1;
    private javax.swing.JLabel lblCurrency;
    private javax.swing.JLabel lblCustomer;
    private javax.swing.JLabel lblCustomerName;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFromBillNo;
    private javax.swing.JLabel lblFromDate;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblOperationType;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblReportType;
    private javax.swing.JLabel lblShift;
    private javax.swing.JLabel lblTimeFromDate;
    private javax.swing.JLabel lblToBillNo;
    private javax.swing.JLabel lblToDate;
    private javax.swing.JLabel lblToDateTime;
    private javax.swing.JLabel lblType;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JLabel lbloperator;
    private javax.swing.JLabel lblpayMode;
    private javax.swing.JLabel lblposname;
    private javax.swing.JPanel pnlAdvanceFilter;
    private javax.swing.JPanel pnlBackGround;
    private javax.swing.JPanel pnlGraph;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JTabbedPane pnlSales;
    private javax.swing.JScrollPane pnlSalesData;
    private javax.swing.JScrollPane pnlSalesTotal;
    private javax.swing.JPanel pnlsalesDetail;
    private javax.swing.JTable tblSales;
    private javax.swing.JTable tblTotal;
    private javax.swing.JTextField txtBillNofrom;
    private javax.swing.JTextField txtBillnoTo;
    private javax.swing.JTextField txtCustomerCode;
    // End of variables declaration//GEN-END:variables
/**
     * set Data to text Bill Form
     *
     * @param data
     *
     */
    void funSetDatatxtBillFrom(Object[] data)
    {
	txtBillNofrom.setText(data[0].toString());
    }

    /**
     * Set Data to text Bill To
     *
     * @param data
     */
    void funSetDatatxtBillTo(Object[] data)
    {
	txtBillnoTo.setText(data[0].toString());
    }

    /**
     * Export Files
     *
     * @param table
     * @param file
     */
    private void funExportFile(JTable table, File file)
    {
	try
	{
	    WritableWorkbook workbook1 = Workbook.createWorkbook(file);
	    WritableSheet sheet1 = workbook1.createSheet("First Sheet", 0);
	    TableModel model = table.getModel();
	    sheet1.addCell(new Label(0, 0, reportName));

	    for (int i = 0; i < model.getColumnCount(); i++)
	    {
		Label column = new Label(i, 1, model.getColumnName(i));
		int colLen = Integer.parseInt(vSalesReportExcelColLength.elementAt(i).toString().split("#")[0]);
		sheet1.setColumnView(i, model.getColumnName(i).toString().length() + colLen);
		sheet1.addCell(column);
	    }
	    int i = 0, j = 0;
	    int k = 0;

	    for (i = 3; i < model.getRowCount() + 3; i++)
	    {
		for (j = 0; j < model.getColumnCount(); j++)
		{
		    //System.out.println(model.getValueAt(k, j).toString()+"\tcol="+j);
		    int colLen = Integer.parseInt(vSalesReportExcelColLength.elementAt(j).toString().split("#")[0]);
		    Label row = new Label(j, i + 1, model.getValueAt(k, j).toString());
		    sheet1.setColumnView(j, model.getValueAt(k, j).toString().length() + colLen);
		    sheet1.addCell(row);
		}
		k++;
	    }
	    funAddLastOfExportReport(workbook1);
	    workbook1.write();
	    workbook1.close();
	    //Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + ExportReportPath + "/" + exportFormName + rDate + ".xls");

	    Desktop dt = Desktop.getDesktop();
	    dt.open(file);
	}
	catch (FileNotFoundException ex)
	{
	    JOptionPane.showMessageDialog(this, "File Not Found Invalid File Path!!!");
	    ex.printStackTrace();

	}
	catch (Exception ex)
	{
	    ex.printStackTrace();
	}
    }

    /**
     * Add Last Of Export Report
     *
     * @param workbook1
     */
    private void funAddLastOfExportReport(WritableWorkbook workbook1)
    {
	try
	{
	    int i = 0, j = 0, LastIndexReport = 0;
	    if (exportFormName.equals("BillWise"))
	    {
		LastIndexReport = 8;
	    }
	    else if (exportFormName.equals("OperatorWise"))
	    {
		LastIndexReport = 3;
	    }
	    else if (exportFormName.equals("DeliveryBoyWise"))
	    {
		LastIndexReport = 0;
	    }
	    else if (exportFormName.equals("SettlementWise"))
	    {
		LastIndexReport = 1;
	    }
	    else if (exportFormName.equals("SubGroupWise"))
	    {
		LastIndexReport = 0;
	    }
	    else if (exportFormName.equals("WaiterWise"))
	    {
		LastIndexReport = 2;
	    }
	    else if (exportFormName.equals("ItemWise"))
	    {
		LastIndexReport = 0;
	    }
	    else if (exportFormName.equals("CostCenterWise"))
	    {
		LastIndexReport = 0;
	    }
	    else if (exportFormName.equals("MenuWise"))
	    {
		LastIndexReport = 0;
	    }
	    else if (exportFormName.equals("GroupWise"))
	    {
		LastIndexReport = 0;
	    }
	    else if (exportFormName.equals("HourWise"))
	    {
		LastIndexReport = 0;
	    }
	    else if (exportFormName.equals("TableWise"))
	    {
		LastIndexReport = 1;
	    }
	    else if (exportFormName.equals("HomeDeliveryWise"))
	    {
		LastIndexReport = 4;
	    }
	    else if (exportFormName.equals("DeliveryBoyWise"))
	    {
		LastIndexReport = 0;
	    }
	    else if (exportFormName.equals("AreaWise"))
	    {
		LastIndexReport = 1;
	    }
	    else if (exportFormName.equals("DayWise"))
	    {
		LastIndexReport = 0;
	    }
	    else if (exportFormName.equals("HourWiseItems"))
	    {
		LastIndexReport = 2;
	    }
	    else if (exportFormName.equals("MonthlySalesReport"))
	    {
		LastIndexReport = 1;
	    }
	    else if (exportFormName.equals("TaxWise"))
	    {
		LastIndexReport = 4;
	    }

	    WritableSheet sheet2 = workbook1.getSheet(0);
	    int r = sheet2.getRows();
	    //System.out.println(r);
	    //System.out.println("Row Cnt="+tblTotal.getRowCount());
	    //System.out.println("Col Cnt="+tblTotal.getColumnCount());
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

    private void funMonthlySalesReport()
    {

	StringBuilder sb = new StringBuilder();
	StringBuilder sqlLiveData = new StringBuilder();
	StringBuilder sqlQData = new StringBuilder();
	StringBuilder sbFilters = new StringBuilder();
	pnlGraph.setVisible(false);
	reportName = "Monthly Sales Report";
	try
	{
	    fromDate = funGetFromDate();
	    toDate = funGetToDate();
	    Date dt1 = dteFromDate.getDate();
	    Date dt2 = dteToDate.getDate();

	    if ((dt2.getTime() - dt1.getTime()) < 0)
	    {
		new frmOkPopUp(null, "Invalid date", "Error", 1).setVisible(true);
	    }
	    else
	    {
		Date objDate = dteFromDate.getDate();
		objDate = dteToDate.getDate();
		records = new Object[4];
		String pos = funGetSelectedPosCode();
		dm = new DefaultTableModel()
		{
		    @Override
		    public boolean isCellEditable(int row, int column)
		    {
			//all cells false
			return false;
		    }
		};

		totalDm = new DefaultTableModel()
		{
		    @Override
		    public boolean isCellEditable(int row, int column)
		    {
			//all cells false
			return false;
		    }
		};
		dm.addColumn("Month");
		dm.addColumn("Year");
		dm.addColumn("Total Sale");

		vSalesReportExcelColLength = new java.util.Vector();
		vSalesReportExcelColLength.add("6#Left"); //month
		vSalesReportExcelColLength.add("6#Left"); //year
		vSalesReportExcelColLength.add("6#Left"); //salesAmount

		totalDm.addColumn("Total");
		totalDm.addColumn("TotalSale");
		sqlLiveData.setLength(0);
		sqlQData.setLength(0);
		sbFilters.setLength(0);
		String selectedPOSName = cmbPosCode.getSelectedItem().toString().trim();
		DecimalFormat decFormat = new DecimalFormat("0");

		sb.setLength(0);
		sb.append("select a.strPOSCode,c.strPOSName,monthname(date(a.dteBillDate)),year(date(a.dteBillDate)) "
			+ " from vqbillhdsettlementdtl a,tblsettelmenthd b,tblposmaster c "
			+ " where a.strSettlementCode=b.strSettelmentCode "
			+ " and a.strPOSCode=c.strPOSCode "
			+ " and date(dteBillDate) between '" + fromDate + "' and '" + toDate + "' ");
		if (!cmbPosCode.getSelectedItem().toString().trim().equals("All"))
		{
		    sb.append(" and a.strPOSCode='" + pos + "' ");
		}
		sb.append("  group by month(date(dteBillDate))"
			+ " order by year(date(dteBillDate)),month(date(dteBillDate)) ");

		List<clsCommonBeanDtl> arrListOfMonth = new ArrayList<>();

		ResultSet rsSales = clsGlobalVarClass.dbMysql.executeResultSet(sb.toString());
		while (rsSales.next())
		{
		    clsCommonBeanDtl objCommonBean = new clsCommonBeanDtl();
		    objCommonBean.setMonthName(rsSales.getString(3));
		    objCommonBean.setYear(rsSales.getString(4));
		    arrListOfMonth.add(objCommonBean);

		}
		rsSales.close();

		Double total = 0.0;

		for (int cnt = 0; cnt < arrListOfMonth.size(); cnt++)
		{
		    clsCommonBeanDtl objCommonBean = arrListOfMonth.get(cnt);
		    String billMonth = objCommonBean.getMonthName();
		    String billYear = objCommonBean.getYear();

		    sqlLiveData.append("SELECT c.strPOSName, MONTHNAME(DATE(a.dteBillDate)), YEAR(DATE(a.dteBillDate))"
			    + " ,ifnull(sum(d.dblSettlementAmt),'0'),ifnull(sum(a.dblGrandTotal),'0'),a.strPOSCode"
			    + " ,month(a.dteBillDate) "
			    + " FROM tblbillhd a,tblsettelmenthd b,tblposmaster c,tblbillsettlementdtl d "
			    + " WHERE d.strSettlementCode=b.strSettelmentCode AND a.strBillNo = d.strBillNo "
			    + " AND a.strPOSCode=c.strPOSCode and a.strClientCode=d.strClientCode "
			    + " and DATE(a.dteBillDate)=DATE(d.dteBillDate) "
			    + " and monthname(date(a.dteBillDate)) ='" + billMonth + "' and Year(a.dteBillDate)='" + billYear + "'");

		    sqlQData.append("SELECT c.strPOSName, MONTHNAME(DATE(a.dteBillDate)), YEAR(DATE(a.dteBillDate))"
			    + " ,ifnull(sum(d.dblSettlementAmt),'0'),ifnull(sum(a.dblGrandTotal),'0'),a.strPOSCode"
			    + " ,month(a.dteBillDate) "
			    + " FROM tblqbillhd a,tblsettelmenthd b,tblposmaster c,tblqbillsettlementdtl d\n"
			    + " WHERE d.strSettlementCode=b.strSettelmentCode AND a.strBillNo = d.strBillNo "
			    + " AND a.strPOSCode=c.strPOSCode and a.strClientCode=d.strClientCode "
			    + " and DATE(a.dteBillDate)=DATE(d.dteBillDate) "
			    + " and monthname(date(a.dteBillDate)) ='" + billMonth + "' and Year(a.dteBillDate)='" + billYear + "' ");

		    if (!pos.equals("All") && !cmbOperator.getSelectedItem().equals("All"))
		    {
			sbFilters.append(" AND " + objUtility.funGetSelectedPOSCodeString("a.strPOSCode", selectedPOSCodeSet) + " and a.strUserCreated='" + cmbOperator.getSelectedItem().toString() + "'");
		    }
		    else if (!pos.equals("All") && cmbOperator.getSelectedItem().equals("All"))
		    {
			sbFilters.append(" AND " + objUtility.funGetSelectedPOSCodeString("a.strPOSCode", selectedPOSCodeSet) + " ");
		    }
		    else if (pos.equals("All") && !cmbOperator.getSelectedItem().equals("All"))
		    {
			sbFilters.append("  and a.strUserCreated='" + cmbOperator.getSelectedItem().toString() + "'");
		    }
		    if (txtBillNofrom.getText().trim().length() == 0 && txtBillnoTo.getText().trim().length() == 0)
		    {
		    }
		    else
		    {
			sbFilters.append(" and a.strBillNo between '" + txtBillNofrom.getText() + "' and '" + txtBillnoTo.getText() + "'");
		    }
		    if (clsGlobalVarClass.gEnableShiftYN && (!cmbShift.getSelectedItem().toString().equalsIgnoreCase("All")))
		    {
			sbFilters.append(" AND a.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ");
		    }
		    if (!cmbArea.getSelectedItem().equals("All"))
		    {
			sbFilters.append(" and a.strAreaCode='" + mapAreaNameCode.get(cmbArea.getSelectedItem().toString()) + "' ");
		    }
		    if (!cmbOperationType.getSelectedItem().toString().equalsIgnoreCase("All"))
		    {
			sbFilters.append(" and a.strOperationType='" + cmbOperationType.getSelectedItem().toString() + "' ");
		    }
		    if (!txtCustomerCode.getText().equalsIgnoreCase(""))
		    {
			sbFilters.append(" and a.strCustomerCode='" + txtCustomerCode.getText() + "' ");

		    }

		    sbFilters.append(" GROUP BY  MONTHNAME(DATE(a.dteBillDate)) ");

		    sqlLiveData.append(" ").append(sbFilters);
		    sqlQData.append(" ").append(sbFilters);

		    mapPOSMonthWiseSales = new LinkedHashMap<String, Map<String, clsCommonBeanDtl>>();
		    ResultSet rsMonthWiseSales = clsGlobalVarClass.dbMysql.executeResultSet(sqlLiveData.toString());
		    funGenerateMonthWiseSales(rsMonthWiseSales);
		    rsMonthWiseSales = clsGlobalVarClass.dbMysql.executeResultSet(sqlQData.toString());
		    funGenerateMonthWiseSales(rsMonthWiseSales);
		    sqlLiveData.setLength(0);
		    sqlQData.setLength(0);
		    sbFilters.setLength(0);

		    if (cmbType.getSelectedItem().equals("Chart"))
		    {
			ArrayList<String> arrListGraphData = new ArrayList<>();
			double totalAmt = 0;

			Iterator<Map.Entry<String, Map<String, clsCommonBeanDtl>>> posIterator = mapPOSMonthWiseSales.entrySet().iterator();
			while (posIterator.hasNext())
			{
			    Map<String, clsCommonBeanDtl> mapMonthDtl = posIterator.next().getValue();
			    Iterator<Map.Entry<String, clsCommonBeanDtl>> monthIterator = mapMonthDtl.entrySet().iterator();
			    while (monthIterator.hasNext())
			    {
				clsCommonBeanDtl objMonthDtl = monthIterator.next().getValue();
				arrListGraphData.add(objMonthDtl.getPosName() + "#" + objMonthDtl.getMonthName() + "#" + objMonthDtl.getSaleAmount());
				totalAmt = totalAmt + objMonthDtl.getSaleAmount();
			    }
			}

			if (chkConsolidatePOS.isSelected())
			{
			    funGenerateGraph("barChart", arrListGraphData, totalAmt, true);
			}
			else
			{
			    funGenerateGraph("barChart", arrListGraphData, totalAmt, false);
			}
			pnlSalesData.setVisible(false);
			pnlSalesTotal.setVisible(false);
			BufferedImage image = null;

			image = ImageIO.read(new File("Graph"));
			lblName.setIcon((new javax.swing.ImageIcon(image)));
			pnlGraph.setVisible(true);
			pnlGraph.add(lblName);
			pnlGraph.setBackground(Color.white);
			lblName.setBackground(Color.white);
		    }
		    else
		    {
			Iterator<Map.Entry<String, Map<String, clsCommonBeanDtl>>> posIterator = mapPOSMonthWiseSales.entrySet().iterator();
			while (posIterator.hasNext())
			{
			    Map<String, clsCommonBeanDtl> mapMonthDtl = posIterator.next().getValue();
			    Iterator<Map.Entry<String, clsCommonBeanDtl>> monthIterator = mapMonthDtl.entrySet().iterator();
			    while (monthIterator.hasNext())
			    {
				clsCommonBeanDtl objMonthDtl = monthIterator.next().getValue();
				Object[] arrObjRecords = new Object[4];
				arrObjRecords[0] = objMonthDtl.getMonthName();//Monthname
				arrObjRecords[1] = objMonthDtl.getYear();//year
				arrObjRecords[2] = gDecimalFormat.format(objMonthDtl.getSaleAmount());//totalamt
				total += objMonthDtl.getSaleAmount();
				dm.addRow(arrObjRecords);
			    }
			}
		    }

		}

		if (!(cmbType.getSelectedItem().equals("Chart")))
		{
		    Object[] arrObjRecords1 = new Object[2];
		    for (int cntCol = 0; cntCol < tblTotal.getColumnCount(); cntCol++)
		    {
			arrObjRecords1[0] = "Total";
			arrObjRecords1[1] = total;
		    }
		    totalDm.addRow(arrObjRecords1);
		    tblSales.setModel(dm);
		    tblTotal.setModel(totalDm);
		    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		    if (cmbPayMode.getSelectedIndex() > 0)
		    {
			tblSales.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		    }
		    else
		    {
			tblSales.setAutoscrolls(true);
			tblSales.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		    }
		    tblSales.getColumnModel().getColumn(0).setPreferredWidth(270);
		    tblSales.getColumnModel().getColumn(1).setPreferredWidth(270);
		    tblSales.getColumnModel().getColumn(2).setPreferredWidth(270);

		    if (cmbPayMode.getSelectedIndex() > 0)
		    {
			tblTotal.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		    }
		    else
		    {
			tblTotal.setAutoscrolls(true);
			tblTotal.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		    }
		    tblTotal.getColumnModel().getColumn(0).setPreferredWidth(400);
		    tblTotal.getColumnModel().getColumn(1).setPreferredWidth(410);
		}

	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funGenerateGraph(String type, ArrayList<String> arrListGraphData, double total, boolean chkConsolidatePOS)
    {
	try
	{
	    funCreateTempFolder();
	    String filePath = System.getProperty("user.dir");
	    File fileGraphPath = new File(filePath + "/Temp/Temp_Graph.jpeg");
	    double sale = 0;

	    if (type == "pieChart")
	    {
		JFreeChart chart;
		DefaultPieDataset dataset = new DefaultPieDataset();
		for (int i = 0; i < arrListGraphData.size(); i++)
		{
		    String data = arrListGraphData.get(i);
		    double amt = Double.parseDouble(data.split("#")[2]);
		    String mode = data.split("#")[1];
		    String POSName = data.split("#")[0];

		    if (amt > 0)
		    {
			if (chkConsolidatePOS == true)
			{
			    sale = Math.rint((amt / total) * 100);
			    dataset.setValue(mode + "\n" + amt, sale);
			}
			else
			{
			    sale = Math.rint((amt / total) * 100);
			    dataset.setValue(POSName + "(" + mode + ")\n" + amt, amt);
			}
		    }
		    chart = ChartFactory.createPieChart(
			    " ", // chart title
			    dataset, // data
			    true, // include legend
			    true,
			    false);
		    int width = 800;
		    /*
                     * Width of the image
		     */

		    int height = 500;
		    /*
                     * Height of the image
		     */

		    File pieChartWrite = new File("Graph");
		    ChartUtilities.saveChartAsJPEG(pieChartWrite, chart, width, height);
		}
	    }
	    else
	    {

		JFreeChart barChart;
		DefaultCategoryDataset dataset = null;
		dataset = new DefaultCategoryDataset();

		for (int i = 0; i < arrListGraphData.size(); i++)
		{

		    String data = arrListGraphData.get(i);
		    double amt = Double.parseDouble(data.split("#")[2]);
		    String month = data.split("#")[1];

		    if (amt > 0)
		    {
			dataset.addValue(amt, month, month);
		    }
		}
		barChart = ChartFactory.createBarChart("", "", "Amount", dataset, PlotOrientation.VERTICAL, true, true, false);
		int width = 800;
		/*
                 * Width of the image
		 */

		int height = 400;
		/*
                 * Height of the image
		 */

		File BarChart = new File("Graph");
		ChartUtilities.saveChartAsJPEG(BarChart, barChart, width, height);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funCreateTempFolder()
    {
	try
	{
	    String filePath = System.getProperty("user.dir");
	    File file = new File(filePath + "/Temp");
	    if (!file.exists())
	    {
		file.mkdirs();
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funGenerateGroupWiseSales(ResultSet rsGroupWiseSales)
    {
	try
	{
	    boolean flgRecords = false;
	    while (rsGroupWiseSales.next())
	    {
		flgRecords = true;
		if (mapPOSDtlForGroupSubGroup.containsKey(rsGroupWiseSales.getString(10)))//posCode
		{
		    String posCode = rsGroupWiseSales.getString(10);
		    String groupCode = rsGroupWiseSales.getString(1);
		    List<Map<String, clsGroupSubGroupWiseSales>> listOfGroup = mapPOSDtlForGroupSubGroup.get(posCode);
		    boolean isGroupExists = false;
		    int groupIndex = 0;
		    for (int i = 0; i < listOfGroup.size(); i++)
		    {
			if (listOfGroup.get(i).containsKey(groupCode))
			{
			    isGroupExists = true;
			    groupIndex = i;
			    break;
			}
		    }
		    if (isGroupExists)
		    {
			Map<String, clsGroupSubGroupWiseSales> mapGroupCodeDtl = listOfGroup.get(groupIndex);
			clsGroupSubGroupWiseSales objGroupCodeDtl = mapGroupCodeDtl.get(groupCode);
			objGroupCodeDtl.setGroupCode(rsGroupWiseSales.getString(1));
			objGroupCodeDtl.setGroupName(rsGroupWiseSales.getString(2));
			objGroupCodeDtl.setPosName(rsGroupWiseSales.getString(5));
			objGroupCodeDtl.setQty(objGroupCodeDtl.getQty() + rsGroupWiseSales.getDouble(3));
			objGroupCodeDtl.setSubTotal(objGroupCodeDtl.getSubTotal() + rsGroupWiseSales.getDouble(4));
			objGroupCodeDtl.setSalesAmt(objGroupCodeDtl.getSalesAmt() + rsGroupWiseSales.getDouble(8));
			objGroupCodeDtl.setDiscAmt(objGroupCodeDtl.getDiscAmt() + rsGroupWiseSales.getDouble(9));
			objGroupCodeDtl.setGrandTotal(objGroupCodeDtl.getGrandTotal() + rsGroupWiseSales.getDouble(11));
		    }
		    else
		    {
			Map<String, clsGroupSubGroupWiseSales> mapGroupCodeDtl = new LinkedHashMap<>();
			clsGroupSubGroupWiseSales objGroupCodeDtl = new clsGroupSubGroupWiseSales(
				rsGroupWiseSales.getString(1), rsGroupWiseSales.getString(2), rsGroupWiseSales.getString(5), rsGroupWiseSales.getDouble(3), rsGroupWiseSales.getDouble(4), rsGroupWiseSales.getDouble(8), rsGroupWiseSales.getDouble(9), rsGroupWiseSales.getDouble(11));
			mapGroupCodeDtl.put(rsGroupWiseSales.getString(1), objGroupCodeDtl);
			listOfGroup.add(mapGroupCodeDtl);
		    }
		}
		else
		{
		    List<Map<String, clsGroupSubGroupWiseSales>> listOfGroupDtl = new ArrayList<>();
		    Map<String, clsGroupSubGroupWiseSales> mapGroupCodeDtl = new LinkedHashMap<>();
		    clsGroupSubGroupWiseSales objGroupCodeDtl = new clsGroupSubGroupWiseSales(
			    rsGroupWiseSales.getString(1), rsGroupWiseSales.getString(2), rsGroupWiseSales.getString(5), rsGroupWiseSales.getDouble(3), rsGroupWiseSales.getDouble(4), rsGroupWiseSales.getDouble(8), rsGroupWiseSales.getDouble(9), rsGroupWiseSales.getDouble(11));
		    mapGroupCodeDtl.put(rsGroupWiseSales.getString(1), objGroupCodeDtl);
		    listOfGroupDtl.add(mapGroupCodeDtl);
		    mapPOSDtlForGroupSubGroup.put(rsGroupWiseSales.getString(10), listOfGroupDtl);
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funGenerateSubGroupWiseSales(ResultSet rsSubGroupWiseSales)
    {
	try
	{
	    boolean flgRecords = false;
	    while (rsSubGroupWiseSales.next())
	    {
		flgRecords = true;
		if (mapPOSDtlForGroupSubGroup.containsKey(rsSubGroupWiseSales.getString(10)))//posCode
		{
		    String posCode = rsSubGroupWiseSales.getString(10);
		    String groupCode = rsSubGroupWiseSales.getString(1);
		    List<Map<String, clsGroupSubGroupWiseSales>> listOfGroup = mapPOSDtlForGroupSubGroup.get(posCode);

		    boolean isGroupExists = false;
		    int groupIndex = 0;
		    for (int i = 0; i < listOfGroup.size(); i++)
		    {
			if (listOfGroup.get(i).containsKey(groupCode))
			{
			    isGroupExists = true;
			    groupIndex = i;
			    break;
			}
		    }
		    if (isGroupExists)
		    {
			Map<String, clsGroupSubGroupWiseSales> mapGroupCodeDtl = listOfGroup.get(groupIndex);
			clsGroupSubGroupWiseSales objGroupCodeDtl = mapGroupCodeDtl.get(groupCode);
			objGroupCodeDtl.setGroupCode(rsSubGroupWiseSales.getString(1));
			objGroupCodeDtl.setGroupName(rsSubGroupWiseSales.getString(2));
			objGroupCodeDtl.setPosName(rsSubGroupWiseSales.getString(5));
			objGroupCodeDtl.setQty(objGroupCodeDtl.getQty() + rsSubGroupWiseSales.getDouble(3));
			objGroupCodeDtl.setSubTotal(objGroupCodeDtl.getSubTotal() + rsSubGroupWiseSales.getDouble(4));
			objGroupCodeDtl.setSalesAmt(objGroupCodeDtl.getSalesAmt() + rsSubGroupWiseSales.getDouble(8));
			objGroupCodeDtl.setDiscAmt(objGroupCodeDtl.getDiscAmt() + rsSubGroupWiseSales.getDouble(9));
			objGroupCodeDtl.setGrandTotal(objGroupCodeDtl.getGrandTotal() + 0.00);
		    }
		    else
		    {
			Map<String, clsGroupSubGroupWiseSales> mapGroupCodeDtl = new LinkedHashMap<>();
			clsGroupSubGroupWiseSales objGroupCodeDtl = new clsGroupSubGroupWiseSales(
				rsSubGroupWiseSales.getString(1), rsSubGroupWiseSales.getString(2), rsSubGroupWiseSales.getString(5), rsSubGroupWiseSales.getDouble(3), rsSubGroupWiseSales.getDouble(4), rsSubGroupWiseSales.getDouble(8), rsSubGroupWiseSales.getDouble(9), 0.00);
			mapGroupCodeDtl.put(rsSubGroupWiseSales.getString(1), objGroupCodeDtl);
			listOfGroup.add(mapGroupCodeDtl);
		    }
		}
		else
		{
		    List<Map<String, clsGroupSubGroupWiseSales>> listOfGroupDtl = new ArrayList<>();
		    Map<String, clsGroupSubGroupWiseSales> mapGroupCodeDtl = new LinkedHashMap<>();
		    clsGroupSubGroupWiseSales objGroupCodeDtl = new clsGroupSubGroupWiseSales(
			    rsSubGroupWiseSales.getString(1), rsSubGroupWiseSales.getString(2), rsSubGroupWiseSales.getString(5), rsSubGroupWiseSales.getDouble(3), rsSubGroupWiseSales.getDouble(4), rsSubGroupWiseSales.getDouble(8), rsSubGroupWiseSales.getDouble(9), 0.00);
		    mapGroupCodeDtl.put(rsSubGroupWiseSales.getString(1), objGroupCodeDtl);
		    listOfGroupDtl.add(mapGroupCodeDtl);
		    mapPOSDtlForGroupSubGroup.put(rsSubGroupWiseSales.getString(10), listOfGroupDtl);
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funGenerateSettlementWiseSales(ResultSet resultSet)
    {
	try
	{
	    while (resultSet.next())
	    {
		String posCode = resultSet.getString("strPOSCode");
		String posName = resultSet.getString("strPOSName");
		String settlementCode = resultSet.getString("strSettelmentCode");
		String settlementDesc = resultSet.getString("strSettelmentDesc");
		double settlementAmt = resultSet.getDouble("dblSettlementAmt");
		String settlementType = resultSet.getString("strSettelmentType");

		if (mapPOSDtlForSettlement.containsKey(posCode))
		{
		    List<Map<String, clsBillSettlementDtl>> listOfSettlement = mapPOSDtlForSettlement.get(posCode);
		    boolean isSettlementExists = false;
		    int settlementIndex = 0;
		    for (int i = 0; i < listOfSettlement.size(); i++)
		    {
			if (listOfSettlement.get(i).containsKey(settlementCode))
			{
			    isSettlementExists = true;
			    settlementIndex = i;
			    break;
			}
		    }
		    if (isSettlementExists)
		    {
			Map<String, clsBillSettlementDtl> mapSettlementCodeDtl = listOfSettlement.get(settlementIndex);
			clsBillSettlementDtl objBillSettlementDtl = mapSettlementCodeDtl.get(settlementCode);
			objBillSettlementDtl.setStrSettlementCode(settlementCode);
			objBillSettlementDtl.setDblSettlementAmt(objBillSettlementDtl.getDblSettlementAmt() + settlementAmt);
			objBillSettlementDtl.setPosName(posName);
		    }
		    else
		    {
			Map<String, clsBillSettlementDtl> mapSettlementCodeDtl = new LinkedHashMap<>();
			clsBillSettlementDtl objBillSettlementDtl = new clsBillSettlementDtl(settlementCode, settlementDesc, settlementAmt, posName, settlementType);
			mapSettlementCodeDtl.put(settlementCode, objBillSettlementDtl);
			listOfSettlement.add(mapSettlementCodeDtl);
		    }
		}
		else
		{
		    List<Map<String, clsBillSettlementDtl>> listOfSettelment = new ArrayList<>();
		    Map<String, clsBillSettlementDtl> mapSettlementCodeDtl = new LinkedHashMap<>();
		    clsBillSettlementDtl objBillSettlementDtl = new clsBillSettlementDtl(settlementCode, settlementDesc, settlementAmt, posName, settlementType);
		    mapSettlementCodeDtl.put(settlementCode, objBillSettlementDtl);
		    listOfSettelment.add(mapSettlementCodeDtl);
		    mapPOSDtlForSettlement.put(posCode, listOfSettelment);
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funGenerateItemWiseSales(ResultSet rsItemWiseSales)
    {
	try
	{
	    while (rsItemWiseSales.next())
	    {
		String itemCode = rsItemWiseSales.getString(1);//itemCode
		String itemName = rsItemWiseSales.getString(2);//itemName
		String posName = rsItemWiseSales.getString(3);//posName
		double qty = rsItemWiseSales.getDouble(4);//qty                
		double subTotal = rsItemWiseSales.getDouble(8);//sunTotal
		double discAmt = rsItemWiseSales.getDouble(9);//discount
		double salesAmt = rsItemWiseSales.getDouble(6);//salesAmount
		String date = rsItemWiseSales.getString(10);//date
		String posCode = rsItemWiseSales.getString(11);//posCode

		String compare = itemCode;
		if (itemCode.contains("M"))
		{
		    compare = itemName;
		}
		else
		{
		    compare = itemCode;
		}

		if (mapPOSItemDtl.containsKey(posCode))
		{
		    Map<String, clsBillItemDtl> mapItemDtl = mapPOSItemDtl.get(posCode);
		    if (mapItemDtl.containsKey(compare))
		    {
			clsBillItemDtl objItemDtl = mapItemDtl.get(compare);
			objItemDtl.setQuantity(objItemDtl.getQuantity() + qty);
			objItemDtl.setAmount(objItemDtl.getAmount() + salesAmt);
			objItemDtl.setSubTotal(objItemDtl.getSubTotal() + subTotal);
			objItemDtl.setDiscountAmount(objItemDtl.getDiscountAmount() + discAmt);
		    }
		    else
		    {
			clsBillItemDtl objItemDtl = new clsBillItemDtl(date, itemCode, itemName, qty, salesAmt, discAmt, posName, subTotal);
			mapItemDtl.put(compare, objItemDtl);
		    }
		}
		else
		{
		    Map<String, clsBillItemDtl> mapItemDtl = new LinkedHashMap<>();
		    clsBillItemDtl objItemDtl = new clsBillItemDtl(date, itemCode, itemName, qty, salesAmt, discAmt, posName, subTotal);
		    mapItemDtl.put(compare, objItemDtl);
		    mapPOSItemDtl.put(posCode, mapItemDtl);
		}

		if (!itemCode.contains("M"))
		{
		    funCreateModifierQuery(itemCode);
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funGenerateMenuHeadWiseSales(ResultSet rsMenuHeadWiseSales)
    {
	try
	{
	    while (rsMenuHeadWiseSales.next())
	    {
		String posCode = rsMenuHeadWiseSales.getString(10);//posCode
		String posName = rsMenuHeadWiseSales.getString(5);//posName
		String menuCode = rsMenuHeadWiseSales.getString(1);//menuCode
		String menuName = rsMenuHeadWiseSales.getString(2);//menuName                
		double qty = rsMenuHeadWiseSales.getDouble(3);//qty
		double salesAmt = rsMenuHeadWiseSales.getDouble(8);//salesAmt
		double subTotal = rsMenuHeadWiseSales.getDouble(4);//subTotal
		double discAmt = rsMenuHeadWiseSales.getDouble(9);//disc                 

		if (mapPOSMenuHeadDtl.containsKey(posCode))
		{
		    Map<String, clsBillItemDtl> mapItemDtl = mapPOSMenuHeadDtl.get(posCode);
		    if (mapItemDtl.containsKey(menuCode))
		    {
			clsBillItemDtl objItemDtl = mapItemDtl.get(menuCode);
			objItemDtl.setQuantity(objItemDtl.getQuantity() + qty);
			objItemDtl.setAmount(objItemDtl.getAmount() + salesAmt);
			objItemDtl.setSubTotal(objItemDtl.getSubTotal() + subTotal);
			objItemDtl.setDiscountAmount(objItemDtl.getDiscountAmount() + discAmt);
		    }
		    else
		    {
			clsBillItemDtl objItemDtl = new clsBillItemDtl(qty, salesAmt, discAmt, posName, subTotal, menuCode, menuName);
			mapItemDtl.put(menuCode, objItemDtl);
		    }
		}
		else
		{
		    Map<String, clsBillItemDtl> mapItemDtl = new LinkedHashMap<>();
		    clsBillItemDtl objItemDtl = new clsBillItemDtl(qty, salesAmt, discAmt, posName, subTotal, menuCode, menuName);
		    mapItemDtl.put(menuCode, objItemDtl);
		    mapPOSMenuHeadDtl.put(posCode, mapItemDtl);
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funGenerateWaiterWiseSales(ResultSet rsWaiterWise)
    {
	try
	{
	    while (rsWaiterWise.next())
	    {
		String waiterCode = rsWaiterWise.getString(7);//waiterNo
		String waiterShortName = rsWaiterWise.getString(2);//waiterShortName
		String waiterFullName = rsWaiterWise.getString(3);//waiterFullName
		String posCode = rsWaiterWise.getString(8);//posCode
		String posName = rsWaiterWise.getString(1);//posName
		double salesAmount = rsWaiterWise.getDouble(4);//salesAmount
		double noOfBills = rsWaiterWise.getDouble(5);//bills

		if (mapPOSWaiterWiseSales.containsKey(posCode))
		{
		    Map<String, clsCommonBeanDtl> mapWaiterDtl = mapPOSWaiterWiseSales.get(posCode);
		    if (mapWaiterDtl.containsKey(waiterCode))
		    {
			clsCommonBeanDtl objWaiterDtl = mapWaiterDtl.get(waiterCode);
			objWaiterDtl.setNoOfBills(objWaiterDtl.getNoOfBills() + noOfBills);
			objWaiterDtl.setSaleAmount(objWaiterDtl.getSaleAmount() + salesAmount);
		    }
		    else
		    {
			clsCommonBeanDtl objWaiterDtl = new clsCommonBeanDtl(posCode, posName, waiterCode, waiterShortName, waiterFullName, salesAmount, noOfBills);
			mapWaiterDtl.put(waiterCode, objWaiterDtl);
		    }
		}
		else
		{
		    Map<String, clsCommonBeanDtl> mapWaiterDtl = new LinkedHashMap<>();
		    clsCommonBeanDtl objWaiterDtl = new clsCommonBeanDtl(posCode, posName, waiterCode, waiterShortName, waiterFullName, salesAmount, noOfBills);
		    mapWaiterDtl.put(waiterCode, objWaiterDtl);
		    mapPOSWaiterWiseSales.put(posCode, mapWaiterDtl);
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funGenerateDelBoyWiseSales(ResultSet rsDelBoyWaise)
    {
	try
	{
	    while (rsDelBoyWaise.next())
	    {
		String dbCode = rsDelBoyWaise.getString(1);//dbCode
		String dbName = rsDelBoyWaise.getString(2);//dbName
		String posCode = rsDelBoyWaise.getString(7);//posCode
		String posName = rsDelBoyWaise.getString(3);//posName
		double salesAmount = rsDelBoyWaise.getDouble(4);//salesAmount
		double delCharges = rsDelBoyWaise.getDouble(5);//delCharges

		if (mapPOSDeliveryBoyWise.containsKey(posCode))
		{
		    Map<String, clsCommonBeanDtl> mapDBDtl = mapPOSDeliveryBoyWise.get(posCode);
		    if (mapDBDtl.containsKey(dbCode))
		    {
			clsCommonBeanDtl objDelBoyDtl = mapDBDtl.get(dbCode);
			objDelBoyDtl.setSaleAmount(objDelBoyDtl.getSaleAmount() + salesAmount);
			objDelBoyDtl.setDelCharges(objDelBoyDtl.getDelCharges() + delCharges);
		    }
		    else
		    {
			clsCommonBeanDtl objDBDtl = new clsCommonBeanDtl(posCode, posName, salesAmount, dbCode, dbName, delCharges);
			mapDBDtl.put(dbCode, objDBDtl);
		    }
		}
		else
		{
		    Map<String, clsCommonBeanDtl> mapDBDtl = new LinkedHashMap<>();
		    clsCommonBeanDtl objDBDtl = new clsCommonBeanDtl(posCode, posName, salesAmount, dbCode, dbName, delCharges);
		    mapDBDtl.put(dbCode, objDBDtl);
		    mapPOSDeliveryBoyWise.put(posCode, mapDBDtl);
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funGenerateCostCenterWiseSales(ResultSet rsCostCenterWise)
    {
	try
	{
	    while (rsCostCenterWise.next())
	    {
		String costCenterCode = rsCostCenterWise.getString(1);//ccCode
		String costCenterName = rsCostCenterWise.getString(2);//ccName                
		String posCode = rsCostCenterWise.getString(10);//posCode
		String posName = rsCostCenterWise.getString(5);//posName               
		double qty = rsCostCenterWise.getDouble(3);//qty
		double subTotal = rsCostCenterWise.getDouble(4);//subTotal
		double salesAmount = rsCostCenterWise.getDouble(8);//salesAmount
		double discAmt = rsCostCenterWise.getDouble(9);//disc

		if (mapPOSCostCenterWiseSales.containsKey(posCode))
		{
		    Map<String, clsCommonBeanDtl> mapCCDtl = mapPOSCostCenterWiseSales.get(posCode);
		    if (mapCCDtl.containsKey(costCenterCode))
		    {
			clsCommonBeanDtl objCCDtl = mapCCDtl.get(costCenterCode);

			objCCDtl.setQty(objCCDtl.getQty() + qty);
			objCCDtl.setSubTotal(objCCDtl.getSubTotal() + subTotal);
			objCCDtl.setSaleAmount(objCCDtl.getSaleAmount() + salesAmount);
			objCCDtl.setDiscAmount(objCCDtl.getDiscAmount() + discAmt);
		    }
		    else
		    {
			clsCommonBeanDtl objCCDtl = new clsCommonBeanDtl(posCode, posName, qty, salesAmount, subTotal, costCenterCode, costCenterName, discAmt);
			mapCCDtl.put(costCenterCode, objCCDtl);
		    }
		}
		else
		{
		    Map<String, clsCommonBeanDtl> mapCCDtl = new LinkedHashMap<>();
		    clsCommonBeanDtl objCCDtl = new clsCommonBeanDtl(posCode, posName, qty, salesAmount, subTotal, costCenterCode, costCenterName, discAmt);
		    mapCCDtl.put(costCenterCode, objCCDtl);

		    mapPOSCostCenterWiseSales.put(posCode, mapCCDtl);
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funGenerateTableWiseSales(ResultSet rsTableWiseSales)
    {
	try
	{
	    while (rsTableWiseSales.next())
	    {
		String posName = rsTableWiseSales.getString(1);//posName                               
		String tableName = rsTableWiseSales.getString(2);//tableName                
		double saleAmount = rsTableWiseSales.getDouble(4);//salesAmount
		double noOfBills = rsTableWiseSales.getDouble(5);//Bills
		String posCode = rsTableWiseSales.getString(6);//posCode
		String tableNo = rsTableWiseSales.getString(11);//tableNo                                

		if (mapPOSTableWiseSales.containsKey(posCode))
		{
		    Map<String, clsCommonBeanDtl> mapTblDtl = mapPOSTableWiseSales.get(posCode);
		    if (mapTblDtl.containsKey(tableNo))
		    {
			clsCommonBeanDtl objTblDtl = mapTblDtl.get(tableNo);
			objTblDtl.setNoOfBills(objTblDtl.getNoOfBills() + noOfBills);
			objTblDtl.setSaleAmount(objTblDtl.getSaleAmount() + saleAmount);
		    }
		    else
		    {
			clsCommonBeanDtl objTblDtl = new clsCommonBeanDtl(posCode, posName, saleAmount, tableNo, noOfBills, tableName);
			mapTblDtl.put(tableNo, objTblDtl);
		    }
		}
		else
		{
		    Map<String, clsCommonBeanDtl> mapTblDtl = new LinkedHashMap<>();
		    clsCommonBeanDtl objTblDtl = new clsCommonBeanDtl(posCode, posName, saleAmount, tableNo, noOfBills, tableName);
		    mapTblDtl.put(tableNo, objTblDtl);

		    mapPOSTableWiseSales.put(posCode, mapTblDtl);
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funGenerateHourlyWiseSales(ResultSet rsHourWise)
    {
	try
	{
	    while (rsHourWise.next())
	    {
		String startHour = rsHourWise.getString(1);//startHour
		String endHour = rsHourWise.getString(2);//endHour
		double noOfBills = rsHourWise.getDouble(3);
		double saleAmount = rsHourWise.getDouble(4);

		if (mapPOSHourlyWiseSales.containsKey(startHour))
		{
		    Map<String, clsCommonBeanDtl> mapHrlyDtl = mapPOSHourlyWiseSales.get(startHour);
		    if (mapHrlyDtl.containsKey(startHour))
		    {
			clsCommonBeanDtl objHrlyDtl = mapHrlyDtl.get(startHour);

			objHrlyDtl.setNoOfBills(objHrlyDtl.getNoOfBills() + noOfBills);
			objHrlyDtl.setSaleAmount(objHrlyDtl.getSaleAmount() + saleAmount);
		    }
		    else
		    {
			clsCommonBeanDtl objHrlyDtl = new clsCommonBeanDtl(saleAmount, startHour, endHour, noOfBills);
			mapHrlyDtl.put(startHour, objHrlyDtl);
		    }
		}
		else
		{
		    Map<String, clsCommonBeanDtl> mapHrlyDtl = new LinkedHashMap<>();
		    clsCommonBeanDtl objHrlyDtl = new clsCommonBeanDtl(saleAmount, startHour, endHour, noOfBills);
		    mapHrlyDtl.put(startHour, objHrlyDtl);
		    mapPOSHourlyWiseSales.put(startHour, mapHrlyDtl);
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funGenerateAreaWiseSales(ResultSet rsAreaWise)
    {
	try
	{
	    while (rsAreaWise.next())
	    {
		String posCode = rsAreaWise.getString(10);//posCode
		String areaCode = rsAreaWise.getString(11);//areaCode
		String posName = rsAreaWise.getString(1);//posName
		String areaName = rsAreaWise.getString(2);//areaName
		double saleAmount = rsAreaWise.getDouble(4);

		if (mapPOSAreaWiseSales.containsKey(posCode))
		{
		    Map<String, clsCommonBeanDtl> mapAreaDtl = mapPOSAreaWiseSales.get(posCode);
		    if (mapAreaDtl.containsKey(areaCode))
		    {
			clsCommonBeanDtl objAreaDtl = mapAreaDtl.get(areaCode);
			objAreaDtl.setSaleAmount(objAreaDtl.getSaleAmount() + saleAmount);
		    }
		    else
		    {
			clsCommonBeanDtl objAreaDtl = new clsCommonBeanDtl(posCode, posName, areaCode, areaName, saleAmount);
			mapAreaDtl.put(areaCode, objAreaDtl);
		    }
		}
		else
		{
		    Map<String, clsCommonBeanDtl> mapAreaDtl = new LinkedHashMap<>();
		    clsCommonBeanDtl objAreaDtl = new clsCommonBeanDtl(posCode, posName, areaCode, areaName, saleAmount);
		    mapAreaDtl.put(areaCode, objAreaDtl);
		    mapPOSAreaWiseSales.put(posCode, mapAreaDtl);
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funGenerateDayWiseSales(ResultSet rsDayWiseSales)
    {
	try
	{
	    while (rsDayWiseSales.next())
	    {
		String date = rsDayWiseSales.getString(1);//day
		double noOfBills = rsDayWiseSales.getDouble(2);
		double subTotal = rsDayWiseSales.getDouble(3);//subTotal
		double discAmount = rsDayWiseSales.getDouble(4);//disc
		double taxAmount = rsDayWiseSales.getDouble(5);//taxAmt
		double saleAmount = rsDayWiseSales.getDouble(6);//saleAmt

		if (mapPOSDayWiseSales.containsKey(date))
		{
		    clsCommonBeanDtl objDayDtl = mapPOSDayWiseSales.get(date);
		    objDayDtl.setNoOfBills(objDayDtl.getNoOfBills() + noOfBills);
		    objDayDtl.setSubTotal(objDayDtl.getSubTotal() + subTotal);
		    objDayDtl.setDiscAmount(objDayDtl.getDiscAmount() + discAmount);
		    objDayDtl.setTaxAmount(objDayDtl.getTaxAmount() + taxAmount);
		    objDayDtl.setSaleAmount(objDayDtl.getSaleAmount() + saleAmount);
		}
		else
		{
		    clsCommonBeanDtl objDayDtl = new clsCommonBeanDtl(saleAmount, subTotal, discAmount, noOfBills, taxAmount, date);
		    mapPOSDayWiseSales.put(date, objDayDtl);
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funGenerateModifierWiseSales(ResultSet rsModiWiseWise)
    {
	try
	{
	    while (rsModiWiseWise.next())
	    {
		String posCode = rsModiWiseWise.getString(7);//posCode
		String posName = rsModiWiseWise.getString(3);//posName
		String modiCode = rsModiWiseWise.getString(1);//modiCode
		String modiName = rsModiWiseWise.getString(2);//modiName
		double qty = rsModiWiseWise.getDouble(4);//qty
		double saleAmount = rsModiWiseWise.getDouble(5);//saleAmount

		if (mapPOSModifierWiseSales.containsKey(posCode))
		{
		    Map<String, clsCommonBeanDtl> mapModiDtl = mapPOSModifierWiseSales.get(posCode);
		    if (mapModiDtl.containsKey(modiName))
		    {
			clsCommonBeanDtl objModiDtl = mapModiDtl.get(modiName);
			objModiDtl.setQty(objModiDtl.getQty() + qty);
			objModiDtl.setSaleAmount(objModiDtl.getSaleAmount() + saleAmount);
		    }
		    else
		    {
			clsCommonBeanDtl objModiDtl = new clsCommonBeanDtl(posCode, posName, qty, saleAmount, modiCode, modiName);
			mapModiDtl.put(modiName, objModiDtl);
		    }
		}
		else
		{
		    Map<String, clsCommonBeanDtl> mapModiDtl = new LinkedHashMap<>();
		    clsCommonBeanDtl objModiDtl = new clsCommonBeanDtl(posCode, posName, qty, saleAmount, modiCode, modiName);
		    mapModiDtl.put(modiName, objModiDtl);
		    mapPOSModifierWiseSales.put(posCode, mapModiDtl);
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    /*
     * private void funGenerateOpearaterWiseSales(ResultSet rsOperatorWise) {
     * try { while (rsOperatorWise.next()) { String posCode =
     * rsOperatorWise.getString(8);//posCode String posName =
     * rsOperatorWise.getString(3);//posName String userCode =
     * rsOperatorWise.getString(1);//userCode String userName =
     * rsOperatorWise.getString(2);//userName String payMode =
     * rsOperatorWise.getString(4);//payMode double discAmount =
     * rsOperatorWise.getDouble(5);//discAmount double saleAmount =
     * rsOperatorWise.getDouble(6);//saleAmount
     *
     * if (mapPOSOperaterWiseSales.containsKey(posCode)) { Map<String,
     * Map<String, clsCommonBeanDtl>> mapUserDtl =
     * mapPOSOperaterWiseSales.get(posCode); if
     * (mapUserDtl.containsKey(userCode)) { Map<String, clsCommonBeanDtl>
     * mapPayDtl = mapUserDtl.get(userCode); if (mapPayDtl.containsKey(payMode))
     * { clsCommonBeanDtl objPayDtl = mapPayDtl.get(payMode);
     * objPayDtl.setDiscAmount(objPayDtl.getDiscAmount() + discAmount);
     * objPayDtl.setSaleAmount(objPayDtl.getSaleAmount() + saleAmount); } else {
     * clsCommonBeanDtl objPayDtl = new clsCommonBeanDtl(posCode, posName,
     * saleAmount, discAmount, userCode, userName, payMode);
     * mapPayDtl.put(payMode, objPayDtl); } } else { Map<String,
     * clsCommonBeanDtl> mapPayDtl = new HashMap<>(); clsCommonBeanDtl objPayDtl
     * = new clsCommonBeanDtl(posCode, posName, saleAmount, discAmount,
     * userCode, userName, payMode); mapPayDtl.put(payMode, objPayDtl);
     * mapUserDtl.put(userCode, mapPayDtl); } } else { Map<String, Map<String,
     * clsCommonBeanDtl>> mapUserDtl = new HashMap<>(); Map<String,
     * clsCommonBeanDtl> mapPayDtl = new HashMap<>(); clsCommonBeanDtl objPayDtl
     * = new clsCommonBeanDtl(posCode, posName, saleAmount, discAmount,
     * userCode, userName, payMode); mapPayDtl.put(payMode, objPayDtl);
     * mapUserDtl.put(userCode, mapPayDtl); mapPOSOperaterWiseSales.put(posCode,
     * mapUserDtl); } } } catch (Exception e) { e.printStackTrace(); }
     }
     */
    private void funGenerateOpearaterWiseSales(ResultSet rsOperatorWise)
    {
	String userCode = "";
	double settleAmt = 0;
	String settleType = "";
	try
	{

	    List<clsOperatorDtl> listOfOperatorDtl = new ArrayList<clsOperatorDtl>();
	    while (rsOperatorWise.next())
	    {
		userCode = rsOperatorWise.getString(1);
		settleType = rsOperatorWise.getString(4);
		clsOperatorDtl objOperatorDtl = new clsOperatorDtl();
		if (mapOperatorDtls.containsKey(userCode))//userCode
		{
		    listOfOperatorDtl = mapOperatorDtls.get(userCode);
		    for (int i = 0; i < listOfOperatorDtl.size(); i++)
		    {
			objOperatorDtl = listOfOperatorDtl.get(i);
			settleAmt = objOperatorDtl.getSettleAmt();
			settleType = objOperatorDtl.getStrSettlementDesc();
			if (settleType.equals(objOperatorDtl.getStrSettlementDesc()))
			{
			    objOperatorDtl.setStrSettlementDesc(settleType);
			    objOperatorDtl.setSettleAmt(settleAmt + rsOperatorWise.getDouble(5));
			    objOperatorDtl.setDiscountAmt(objOperatorDtl.getDiscountAmt() + 0);
			}

		    }
		}
		else
		{
		    listOfOperatorDtl = new ArrayList<clsOperatorDtl>();
		    objOperatorDtl.setStrUserCode(userCode);
		    objOperatorDtl.setStrUserName(rsOperatorWise.getString(2));
		    objOperatorDtl.setStrPOSName(rsOperatorWise.getString(3));
		    objOperatorDtl.setStrSettlementDesc(settleType);
		    objOperatorDtl.setDiscountAmt(0);
		    objOperatorDtl.setSettleAmt(rsOperatorWise.getDouble(5));
		    objOperatorDtl.setStrUser(rsOperatorWise.getString(6));
		    objOperatorDtl.setStrPOSCode(rsOperatorWise.getString(7));
		}

		listOfOperatorDtl.add(objOperatorDtl);
		mapOperatorDtls.put(rsOperatorWise.getString(1), listOfOperatorDtl);

	    }
	    userCode = "";
	    rsOperatorWise.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funCalculateDiscountForOpearaterWise(ResultSet rsOperatorWise)
    {

	try
	{
	    while (rsOperatorWise.next())
	    {
		for (Map.Entry<String, List<clsOperatorDtl>> entry : mapOperatorDtls.entrySet())
		{
		    List<clsOperatorDtl> listOfOperatorDtl = entry.getValue();
		    for (int i = 0; i < listOfOperatorDtl.size(); i++)
		    {
			clsOperatorDtl objOperatorDtl = listOfOperatorDtl.get(0);
			objOperatorDtl.setDiscountAmt(objOperatorDtl.getDiscountAmt() + rsOperatorWise.getDouble(4));
		    }
		}

	    }
	    rsOperatorWise.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funGenerateMonthWiseSales(ResultSet rsMonthWiseSales)
    {
	try
	{
	    while (rsMonthWiseSales.next())
	    {
		String posCode = rsMonthWiseSales.getString(6);//posCode
		String posName = rsMonthWiseSales.getString(1);//posName
		String monthCode = rsMonthWiseSales.getString(7);//monthCode
		String monthName = rsMonthWiseSales.getString(2);//monthName
		String year = rsMonthWiseSales.getString(3);//year
		double saleAmount = rsMonthWiseSales.getDouble(4);//saleAmount

		if (mapPOSMonthWiseSales.containsKey(year))
		{
		    Map<String, clsCommonBeanDtl> mapMonthDtl = mapPOSMonthWiseSales.get(year);
		    if (mapMonthDtl.containsKey(monthCode))
		    {
			clsCommonBeanDtl objMonthDtl = mapMonthDtl.get(monthCode);
			objMonthDtl.setSaleAmount(objMonthDtl.getSaleAmount() + saleAmount);
			mapMonthDtl.put(monthCode, objMonthDtl);
		    }
		    else
		    {
			clsCommonBeanDtl objMonthDtl = new clsCommonBeanDtl(saleAmount, posCode, posName, monthCode, monthName, year);
			mapMonthDtl.put(monthCode, objMonthDtl);
		    }
		}
		else
		{
		    Map<String, clsCommonBeanDtl> mapMonthDtl = new LinkedHashMap<>();
		    clsCommonBeanDtl objMonthDtl = new clsCommonBeanDtl(saleAmount, posCode, posName, monthCode, monthName, year);
		    mapMonthDtl.put(monthCode, objMonthDtl);
		    mapPOSMonthWiseSales.put(year, mapMonthDtl);
		}
	    }
	}
	catch (SQLException ex)
	{
	    ex.printStackTrace();
	}
    }

    private void funCreateModifierQuery(String itemCode)
    {

	String mSubTotal = "sum(a.dblAmount)";
	String mDiscountAmt = "sum(a.dblDiscAmt)";
	String mNetTotalAmt = "sum(a.dblAmount)-sum(a.dblDiscAmt)";
	String mTaxAmt = "0";

	if (cmbCurrency.getSelectedItem().toString().equalsIgnoreCase("USD"))
	{
	    mSubTotal = "sum(a.dblAmount/b.dblUSDConverionRate)";
	    mDiscountAmt = "sum(a.dblDiscAmt/b.dblUSDConverionRate)";
	    mNetTotalAmt = "sum(a.dblAmount/b.dblUSDConverionRate)-sum(a.dblDiscAmt/b.dblUSDConverionRate)";
	    mTaxAmt = "0";
	}

	try
	{
	    String sqlModLive = "select a.strItemCode,a.strModifierName,c.strPOSName"
		    + " ,sum(a.dblQuantity),'0'," + mNetTotalAmt + ",'" + clsGlobalVarClass.gUserCode + "' "
		    + " ," + mSubTotal + "," + mDiscountAmt + ",DATE_FORMAT(date(b.dteBillDate),'%d-%m-%Y'),b.strPOSCode  "
		    + "from tblbillmodifierdtl a,tblbillhd b,tblposmaster c\n"
		    + "where a.strBillNo=b.strBillNo and b.strPOSCode=c.strPosCode  \n"
		    + "and date( b.dteBillDate ) BETWEEN '" + fromDate + "' AND '" + toDate + "' "
		    + "and left(a.strItemCode,7)='" + itemCode + "' "
		    + "and a.dblAmount>0 ";

	    String pos = funGetSelectedPosCode();
	    String sqlFilters = "";
	    if (!pos.equals("All") && !cmbOperator.getSelectedItem().equals("All"))
	    {
		sqlFilters += " AND b.strPOSCode = '" + pos + "' and b.strUserCreated='" + cmbOperator.getSelectedItem().toString() + "' ";
	    }
	    else if (!pos.equals("All") && cmbOperator.getSelectedItem().equals("All"))
	    {
		sqlFilters += " AND b.strPOSCode = '" + pos + "' ";
	    }
	    else if (pos.equals("All") && !cmbOperator.getSelectedItem().equals("All"))
	    {
		sqlFilters += " AND b.strUserCreated='" + cmbOperator.getSelectedItem().toString() + "' ";
	    }
	    if (txtBillNofrom.getText().trim().length() == 0 && txtBillnoTo.getText().trim().length() == 0)
	    {

	    }
	    else
	    {
		sqlFilters += " and a.strbillno between '" + txtBillNofrom.getText() + "' "
			+ " and '" + txtBillnoTo.getText() + "'";
	    }
	    if (clsGlobalVarClass.gEnableShiftYN && (!cmbShift.getSelectedItem().toString().equalsIgnoreCase("All")))
	    {
		sqlFilters += " AND b.intShiftCode = '" + cmbShift.getSelectedItem().toString() + "' ";
	    }
	    sqlFilters += " group by a.strItemCode,a.strModifierName,c.strPOSName  "
		    + " order by b.dteBillDate ";

	    sqlModLive = sqlModLive + " " + sqlFilters;

	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sqlModLive.toString());
	    funGenerateItemWiseSales(rs);

	    //qmodifiers
	    String sqlModQFile = " select a.strItemCode,a.strModifierName,c.strPOSName"
		    + " ,sum(a.dblQuantity),'0'," + mNetTotalAmt + ",'" + clsGlobalVarClass.gUserCode + "' "
		    + " ," + mSubTotal + "," + mDiscountAmt + ",DATE_FORMAT(date(b.dteBillDate),'%d-%m-%Y'),b.strPOSCode "
		    + "from tblqbillmodifierdtl a,tblqbillhd b,tblposmaster c\n"
		    + "where a.strBillNo=b.strBillNo and b.strPOSCode=c.strPosCode  \n"
		    + "and date( b.dteBillDate ) BETWEEN '" + fromDate + "' AND '" + toDate + "' "
		    + "and left(a.strItemCode,7)='" + itemCode + "' "
		    + "and a.dblAmount>0  ";

	    sqlModQFile = sqlModQFile + " " + sqlFilters;

	    rs = clsGlobalVarClass.dbMysql.executeResultSet(sqlModQFile.toString());
	    funGenerateItemWiseSales(rs);

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void fillShiftCombo()
    {
	try
	{

	    if (clsGlobalVarClass.gEnableShiftYN)
	    {
		String pos = funGetSelectedPosCode();
		StringBuilder sqlShift = new StringBuilder();
		if (pos.equalsIgnoreCase("All"))
		{
		    sqlShift.append("select max(a.intShiftCode) from tblshiftmaster a group by a.intShiftCode ");
		}
		else
		{
		    sqlShift.append("select a.intShiftCode from tblshiftmaster a where a.strPOSCode='" + pos + "' ");
		}

		ResultSet rsShifts = clsGlobalVarClass.dbMysql.executeResultSet(sqlShift.toString());
		cmbShift.removeAllItems();

		cmbShift.addItem("All");
		while (rsShifts.next())
		{
		    cmbShift.addItem(rsShifts.getString(1));
		}
	    }
	    else
	    {
		lblShift.setVisible(false);
		cmbShift.setVisible(false);

	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private String funGetSelectedPosCode(int row)
    {
	String pos = "All";
	try
	{
	    String posName = tblSales.getValueAt(row, 5).toString();
	    ResultSet rsPOS = clsGlobalVarClass.dbMysql.executeResultSet("select a.strPosCode,a.strPosName from tblposmaster a where a.strPosName='" + posName + "' ");
	    if (rsPOS.next())
	    {
		pos = rsPOS.getString("strPosCode");
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    return pos;
	}
    }
}
