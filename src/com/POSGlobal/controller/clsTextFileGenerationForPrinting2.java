///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.POSGlobal.controller;
//
//import com.POSGlobal.view.frmShowTextFile;
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.net.InetAddress;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.nio.file.StandardOpenOption;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.text.DecimalFormat;
//import java.text.NumberFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import javax.print.Doc;
//import javax.print.DocFlavor;
//import javax.print.DocPrintJob;
//import javax.print.PrintService;
//import javax.print.PrintServiceLookup;
//import javax.print.SimpleDoc;
//import javax.print.attribute.Attribute;
//import javax.print.attribute.DocAttributeSet;
//import javax.print.attribute.HashDocAttributeSet;
//import javax.print.attribute.HashPrintRequestAttributeSet;
//import javax.print.attribute.PrintRequestAttributeSet;
//import javax.print.attribute.PrintServiceAttributeSet;
//import javax.print.event.PrintJobEvent;
//import javax.print.event.PrintJobListener;
//import javax.swing.JOptionPane;
//
///**
// *
// * @author ajjim
// */
//public class clsTextFileGenerationForPrinting2
//{
//
//    clsUtility objUtility = new clsUtility();
//    private final String Line = "  --------------------------------------";
//    ResultSet rsBillPrint;
//    DecimalFormat decimalFormat = new DecimalFormat("######.##");
//    DecimalFormat decimalFormatFor2DecPoint = new DecimalFormat("0.00");
//    DecimalFormat decimalFormatFor3DecPoint = new DecimalFormat("0.000");
//    private String KOTType, sql;
//
//    public void funGenerateTextDayEndReport(String posCode, String billDate, String reprint, int shiftNo, String printYN)
//    {
//        try
//        {
//            String dashLinesFor42Chars = "  ----------------------------------------";
//            String billHd = "tblqbillhd";
//            String billDtl = "tblqbilldtl";
//            String billSettlementDtl = "tblqbillsettlementdtl";
//            String billTaxDtl = "tblqbilltaxdtl";
//            String billComplementaryDtl = "tblqbillcomplementrydtl";
//            funCreateTempFolder();
//            String filePath = System.getProperty("user.dir");
//            File Text_DayEndReport = new File(filePath + "/Temp/Temp_DayEndReport.txt");
//            FileWriter fstream_Report = new FileWriter(Text_DayEndReport);
//            BufferedWriter bufferedWriter = new BufferedWriter(fstream_Report);
//            boolean isReprint = false;
//            if ("reprint".equalsIgnoreCase(reprint))
//            {
//                isReprint = true;
//                funPrintBlankSpace("[DUPLICATE]", bufferedWriter);
//                bufferedWriter.write("[DUPLICATE]");
//                bufferedWriter.newLine();
//                billHd = "tblqbillhd";
//                billDtl = "tblqbilldtl";
//                billSettlementDtl = "tblqbillsettlementdtl";
//                billTaxDtl = "tblqbilltaxdtl";
//                billComplementaryDtl = "tblqbillcomplementrydtl";
//            }
//            if (clsGlobalVarClass.gEnableShiftYN)
//            {
//                funPrintBlankSpace("SHIFT END REPORT", bufferedWriter);
//                bufferedWriter.write("SHIFT END REPORT");
//            }
//            else
//            {
//                funPrintBlankSpace("DAY END REPORT", bufferedWriter);
//                bufferedWriter.write("DAY END REPORT");
//            }
//            bufferedWriter.newLine();
//            bufferedWriter.newLine();
//            String sqlDayEnd = "";
//            ResultSet rsDayend;
//            if (posCode.equals("All"))
//            {
//                sqlDayEnd = "select  'All' as POSCode,'All' as POSName,date(a.dtePOSDate),time(a.dteDayEndDateTime),sum(a.dblTotalSale), "
//                        + " sum(a.dblFloat),sum(a.dblCash),sum(a.dblAdvance),  sum(a.dblTransferIn),sum(a.dblTotalReceipt),sum(a.dblPayments), "
//                        + " sum(a.dblWithDrawal),sum(a.dblTransferOut),sum(a.dblTotalPay),  sum(a.dblCashInHand),sum(a.dblHDAmt), "
//                        + " sum(a.dblDiningAmt),sum(a.dblTakeAway),sum(a.dblNoOfBill),sum(a.dblNoOfVoidedBill), "
//                        + " sum(a.dblNoOfModifyBill),sum(a.dblRefund)  ,sum(a.dblTotalDiscount), "
//                        + " sum(a.intTotalPax),sum(a.intNoOfTakeAway),sum(a.intNoOfHomeDelivery),  "
//                        + " sum(a.strUserCreated),sum(a.strUserEdited), sum(a.intNoOfNCKOT),sum(a.intNoOfComplimentaryKOT), "
//                        + " sum(a.intNoOfVoidKOT),sum(dblUsedDebitCardBalance),sum(dblUnusedDebitCardBalance),sum(a.dblTipAmt)"
//                        + " ,sum(a.dblNoOfDiscountedBill) "
//                        + " from tbldayendprocess a  "
//                        + " where date(a.dtePOSDate)=? ";
//                PreparedStatement pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlDayEnd);
//                pst.setString(1, billDate);                
//                rsDayend = pst.executeQuery();
//            }
//            else
//            {
//                sqlDayEnd = "SELECT a.strPOSCode,b.strPosName, DATE(a.dtePOSDate), TIME(a.dteDayEndDateTime),sum(a.dblTotalSale),\n"
//                        + " sum(a.dblFloat),sum(a.dblCash),sum(a.dblAdvance),sum( a.dblTransferIn),sum(a.dblTotalReceipt),sum(a.dblPayments),\n"
//                        + " sum(a.dblWithDrawal),sum(a.dblTransferOut),sum(a.dblTotalPay),sum(a.dblCashInHand),sum(a.dblHDAmt),\n"
//                        + " sum(a.dblDiningAmt),sum(a.dblTakeAway),sum(a.dblNoOfBill),sum(a.dblNoOfVoidedBill),\n"
//                        + " sum(a.dblNoOfModifyBill),sum(a.dblRefund),sum(a.dblTotalDiscount),\n"
//                        + " sum(a.intTotalPax),sum(a.intNoOfTakeAway),sum(a.intNoOfHomeDelivery),\n"
//                        + " a.strUserCreated,a.strUserEdited, sum(a.intNoOfNCKOT),sum(a.intNoOfComplimentaryKOT)\n"
//                        + " ,sum(a.intNoOfVoidKOT),sum(a.dblUsedDebitCardBalance),sum(a.dblUnusedDebitCardBalance),sum(a.dblTipAmt),sum(a.dblNoOfDiscountedBill)\n"
//                        + "FROM tbldayendprocess a,tblposmaster b "
//                        + " where b.strPosCode=a.strPosCode "
//                        + " and a.strPOSCode=? and date(a.dtePOSDate)=? ;";
//                PreparedStatement pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlDayEnd);
//                pst.setString(1, posCode);
//                pst.setString(2, billDate);                
//                rsDayend = pst.executeQuery();
//            }
//            if (rsDayend.next())
//            {
//                //Header Part
//                bufferedWriter.write("  POS Code    :");
//                bufferedWriter.write(rsDayend.getString(1));
//                bufferedWriter.newLine();
//
//                bufferedWriter.write("  POS Name    :");
//                bufferedWriter.write(rsDayend.getString(2));
//                bufferedWriter.newLine();
//
//                if (clsGlobalVarClass.gEnableShiftYN)
//                {
//                    bufferedWriter.write("  SHIFT No.    :");
//                    bufferedWriter.write(String.valueOf(shiftNo));
//                    bufferedWriter.newLine();
//                }
//
//                bufferedWriter.write("  POS Date    :");
//                bufferedWriter.write(rsDayend.getString(3));
//                bufferedWriter.write(" " + rsDayend.getString(4));
//                bufferedWriter.newLine();
//
//                if (clsGlobalVarClass.gEnableShiftYN)
//                {
//                    bufferedWriter.write("  SHIFT End By  :");
//                }
//                else
//                {
//                    bufferedWriter.write("  Day End By  :");
//                }
//
//                bufferedWriter.write(rsDayend.getString(28));
//                bufferedWriter.newLine();
//                bufferedWriter.write(dashLinesFor42Chars);
//                bufferedWriter.newLine();
//                // End Of Header Part
//
//                //Start of Detail Part
//                funWriteTotal(" 1. HOME DELIVERY", rsDayend.getString(16), bufferedWriter, "");
//                bufferedWriter.newLine();
//
//                funWriteTotal(" 2. DINING", rsDayend.getString(17), bufferedWriter, "");
//                bufferedWriter.newLine();
//
//                funWriteTotal(" 3. TAKE AWAY", rsDayend.getString(18), bufferedWriter, "");
//                bufferedWriter.newLine();
//
//                bufferedWriter.write(dashLinesFor42Chars);
//                bufferedWriter.newLine();
//                funWriteTotal(" 4. TOTAL SALES", rsDayend.getString(5), bufferedWriter, "");
//                bufferedWriter.newLine();
//                bufferedWriter.write(dashLinesFor42Chars);
//                bufferedWriter.newLine();
//
//                funWriteTotal(" 5. DISCOUNT", rsDayend.getString(23), bufferedWriter, "");
//                bufferedWriter.newLine();
//
//                funWriteTotal(" 6. FLOAT", rsDayend.getString(6), bufferedWriter, "");
//                bufferedWriter.newLine();
//
//                funWriteTotal(" 7. CASH", rsDayend.getString(7), bufferedWriter, "");
//                bufferedWriter.newLine();
//
//                funWriteTotal(" 8. ADVANCE", rsDayend.getString(8), bufferedWriter, "");
//                bufferedWriter.newLine();
//
//                funWriteTotal(" 9. TRANSFER IN", rsDayend.getString(9), bufferedWriter, "");
//                bufferedWriter.newLine();
//
//                bufferedWriter.write(dashLinesFor42Chars);
//                bufferedWriter.newLine();
//                funWriteTotal("10. TOTAL RECEIPT", rsDayend.getString(10), bufferedWriter, "");
//                bufferedWriter.newLine();
//                bufferedWriter.write(dashLinesFor42Chars);
//                bufferedWriter.newLine();
//
//                funWriteTotal("11. PAYMENT", rsDayend.getString(11), bufferedWriter, "");
//                bufferedWriter.newLine();
//
//                funWriteTotal("12. WITHDRAWAL", rsDayend.getString(12), bufferedWriter, "");
//                bufferedWriter.newLine();
//
//                funWriteTotal("13. TRANSFER OUT", rsDayend.getString(13), bufferedWriter, "");
//                bufferedWriter.newLine();
//
//                funWriteTotal("14. REFUND", rsDayend.getString(22), bufferedWriter, "");
//                bufferedWriter.newLine();
//
//                bufferedWriter.write(dashLinesFor42Chars);
//                bufferedWriter.newLine();
//                funWriteTotal("15. TOTAL PAYMENTS", rsDayend.getString(14), bufferedWriter, "");
//                bufferedWriter.newLine();
//
//                bufferedWriter.write(dashLinesFor42Chars);
//                bufferedWriter.newLine();
//                funWriteTotal("16. CASH IN HAND", rsDayend.getString(15), bufferedWriter, "");
//                bufferedWriter.newLine();
//                bufferedWriter.write(dashLinesFor42Chars);
//                bufferedWriter.newLine();
//
//                funWriteTotal("17. No. OF BILLS", rsDayend.getString(19), bufferedWriter, "");
//                bufferedWriter.newLine();
//
//                funWriteTotal("18. No. OF VOIDED BILLS", rsDayend.getString(20), bufferedWriter, "");
//                bufferedWriter.newLine();
//
//                funWriteTotal("19. No. OF MODIFIED BILLS", rsDayend.getString(21), bufferedWriter, "");
//                bufferedWriter.newLine();
//
//                funWriteTotal("20. NO. OF PAX", rsDayend.getString(24), bufferedWriter, "");
//                bufferedWriter.newLine();
//
//                funWriteTotal("21. No. OF HOME DEL", rsDayend.getString(26), bufferedWriter, "");
//                bufferedWriter.newLine();
//
//                funWriteTotal("22. No. OF TAKE AWAY", rsDayend.getString(25), bufferedWriter, "");
//                bufferedWriter.newLine();
//
//                funWriteTotal("23. No. OF NC KOT", rsDayend.getString(29), bufferedWriter, "");
//                bufferedWriter.newLine();
//
//                funWriteTotal("24. No. OF COMPLIMENTARY BILLS", rsDayend.getString(30), bufferedWriter, "");
//                bufferedWriter.newLine();
//
//                funWriteTotal("25. No. OF DISCOUNTED BILLS", rsDayend.getString(35), bufferedWriter, "");
//                bufferedWriter.newLine();
//
//                funWriteTotal("26. No. OF VOID KOT", rsDayend.getString(31), bufferedWriter, "");
//                bufferedWriter.newLine();
//
//                funWriteTotal("27. Used Card Balance", rsDayend.getString(32), bufferedWriter, "");
//                bufferedWriter.newLine();
//
//                funWriteTotal("28. Unused Card Balance", rsDayend.getString(33), bufferedWriter, "");
//                bufferedWriter.newLine();
//                //End of Detail Part
//
//                NumberFormat formatter = new DecimalFormat("0.00");
//                //<< tip amount
//                bufferedWriter.write(dashLinesFor42Chars);
//                bufferedWriter.newLine();
//                funWriteTotal("29. Total Tip Amount", rsDayend.getString(34), bufferedWriter, "");
//                bufferedWriter.newLine();
//            //>> tip amount
//
//                //Start of Settlement Brkup
//                double totalAmt = 0.00;
//                bufferedWriter.write(dashLinesFor42Chars);
//                bufferedWriter.newLine();
//                funPrintBlankSpace("BILLING SETTLEMENT BREAK UP", bufferedWriter);
//                bufferedWriter.write("BILLING SETTLEMENT BREAK UP");
//                bufferedWriter.newLine();
//                bufferedWriter.write(dashLinesFor42Chars);
//                bufferedWriter.newLine();
//
//                String sql_SettelementBrkUP = "";
//                ResultSet rs_SettelementBrkUP;
//                if (posCode.equals("All"))
//                {
//                    if (clsGlobalVarClass.gDayEndReportForm.equals("DayEndReport"))
//                    {
//                        sql_SettelementBrkUP = "select c.strSettelmentDesc, SUM(b.dblSettlementAmt) "
//                                + " from  " + billHd + " a, " + billSettlementDtl + " b, tblsettelmenthd c  "
//                                + " where a.strBillNo = b.strBillNo "
//                                + " and date(a.dteBillDate)=date(b.dteBillDate) "
//                                + " and b.strSettlementCode = c.strSettelmentCode "
//                                + " and date(a.dteBillDate) = ? "                                
//                                + " and c.strSettelmentType<>'Complementary' "
//                                + " GROUP BY c.strSettelmentDesc;";
//                    }
//                    else
//                    {
//                        sql_SettelementBrkUP = "select c.strSettelmentDesc, SUM(b.dblSettlementAmt) "
//                                + " from  " + billHd + " a, " + billSettlementDtl + " b, tblsettelmenthd c  "
//                                + " where a.strBillNo = b.strBillNo "
//                                + " and date(a.dteBillDate)=date(b.dteBillDate) "
//                                + " and b.strSettlementCode = c.strSettelmentCode "
//                                + " and date(a.dteBillDate) = ? "                                
//                                + " and c.strSettelmentType<>'Complementary' "
//                                + " GROUP BY c.strSettelmentDesc;";
//                    }
//                    PreparedStatement pst_SettelementBrkUP = clsGlobalVarClass.conPrepareStatement.prepareStatement(sql_SettelementBrkUP);
//                    pst_SettelementBrkUP.setString(1, billDate);                    
//                    rs_SettelementBrkUP = pst_SettelementBrkUP.executeQuery();
//                }
//                else
//                {
//                    if (clsGlobalVarClass.gDayEndReportForm.equals("DayEndReport"))
//                    {
//                        sql_SettelementBrkUP = "select c.strSettelmentDesc, SUM(b.dblSettlementAmt) "
//                                + " from  " + billHd + " a, " + billSettlementDtl + " b, tblsettelmenthd c  "
//                                + " where a.strBillNo = b.strBillNo "
//                                + " and date(a.dteBillDate)=date(b.dteBillDate) "
//                                + " and b.strSettlementCode = c.strSettelmentCode "
//                                + " and a.strPOSCode=? "
//                                + " and date(a.dteBillDate) = ? "                                
//                                + " and c.strSettelmentType<>'Complementary' "
//                                + " GROUP BY c.strSettelmentDesc;";
//                    }
//                    else
//                    {
//                        sql_SettelementBrkUP = "select c.strSettelmentDesc, SUM(b.dblSettlementAmt) "
//                                + " from  " + billHd + " a, " + billSettlementDtl + " b, tblsettelmenthd c  "
//                                + " where a.strBillNo = b.strBillNo "
//                                + " and date(a.dteBillDate)=date(b.dteBillDate) "
//                                + " and b.strSettlementCode = c.strSettelmentCode "
//                                + " and a.strPOSCode=? "
//                                + " and date(a.dteBillDate) = ? "                                
//                                + " and c.strSettelmentType<>'Complementary' "
//                                + " GROUP BY c.strSettelmentDesc;";
//                    }
//                    //System.out.println(sql_SettelementBrkUP);
//                    PreparedStatement pst_SettelementBrkUP = clsGlobalVarClass.conPrepareStatement.prepareStatement(sql_SettelementBrkUP);
//                    pst_SettelementBrkUP.setString(1, posCode);
//                    pst_SettelementBrkUP.setString(2, billDate);                    
//                    rs_SettelementBrkUP = pst_SettelementBrkUP.executeQuery();
//                }
//                while (rs_SettelementBrkUP.next())
//                {
//                    totalAmt += rs_SettelementBrkUP.getDouble(2);
//                    funWriteTotal(rs_SettelementBrkUP.getString(1), rs_SettelementBrkUP.getString(2), bufferedWriter, "");
//                    bufferedWriter.newLine();
//                }
//                rs_SettelementBrkUP.close();
//
//                //for complementary sales
//                String sqlComplementarySales = "";
//                PreparedStatement psComplementarySales = null;
//                if (posCode.equalsIgnoreCase("All"))
//                {
//
//                    sqlComplementarySales = "select d.strSettelmentDesc, SUM(b.dblAmount) "
//                            + "from  " + billHd + " a," + billComplementaryDtl + " b," + billSettlementDtl + " c, tblsettelmenthd d "
//                            + "where a.strBillNo = b.strBillNo "
//                            + "and b.strBillNo=c.strBillNo "
//                            + "and date(a.dteBillDate)=date(b.dteBillDate) "
//                            + "and date(b.dteBillDate)=date(c.dteBillDate) "
//                            + "and c.strSettlementCode = d.strSettelmentCode "
//                            + "and date(a.dteBillDate) = ? "                           
//                            + "and d.strSettelmentType='Complementary' "
//                            + "GROUP BY d.strSettelmentDesc";
//
//                    psComplementarySales = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlComplementarySales);
//                    psComplementarySales.setString(1, billDate);                    
//
//                }
//                else
//                {
//
//                    sqlComplementarySales = "select d.strSettelmentDesc, SUM(b.dblAmount) "
//                            + "from  " + billHd + " a," + billComplementaryDtl + " b," + billSettlementDtl + " c, tblsettelmenthd d "
//                            + "where a.strBillNo = b.strBillNo "
//                            + "and b.strBillNo=c.strBillNo "
//                            + "and date(a.dteBillDate)=date(b.dteBillDate) "
//                            + "and date(b.dteBillDate)=date(c.dteBillDate) "
//                            + "and c.strSettlementCode = d.strSettelmentCode "
//                            + "and a.strPOSCode=?  "
//                            + "and date(a.dteBillDate) = ? "                            
//                            + "and d.strSettelmentType='Complementary' "
//                            + "GROUP BY d.strSettelmentDesc";
//                    psComplementarySales = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlComplementarySales);
//                    psComplementarySales.setString(1, posCode);
//                    psComplementarySales.setString(2, billDate);                    
//
//                }
//                ResultSet rsComplementarySales = psComplementarySales.executeQuery();
//                boolean complementarySales = true;
//                while (rsComplementarySales.next())
//                {
//                    if (complementarySales)
//                    {
//                        funWriteTotal("COMPLEMENTARY SALES", "", bufferedWriter, "");
//                        bufferedWriter.newLine();
//                        funWriteTotal("-------------------", "", bufferedWriter, "");
//                        bufferedWriter.newLine();
//                    }
//                    complementarySales = false;
//                    funWriteTotal(rsComplementarySales.getString(1), rsComplementarySales.getString(2), bufferedWriter, "");
//                    bufferedWriter.newLine();
//                }
//                rsComplementarySales.close();
//
//                bufferedWriter.write(dashLinesFor42Chars);
//                bufferedWriter.newLine();
//
//                funWriteTotal("   TOTAL", formatter.format(totalAmt), bufferedWriter, "");
//                bufferedWriter.newLine();
//                bufferedWriter.write(dashLinesFor42Chars);
//                bufferedWriter.newLine();
//                bufferedWriter.write("   TAX Des             Taxable   Tax Amt   ");
//                bufferedWriter.newLine();
//                bufferedWriter.write(dashLinesFor42Chars);
//                bufferedWriter.newLine();
//                double totalTableAmt = 0.00, totalTaxAmt = 0.00;
//                
//
//                StringBuilder sqlTaxBuilder = new StringBuilder();
//                sqlTaxBuilder.append("SELECT b.strTaxCode,c.strTaxDesc,sum(b.dblTaxableAmount) as dblTaxableAmount,sum(b.dblTaxAmount) as dblTaxAmount "
//                        + "FROM "+billHd+" a "
//                        + "INNER JOIN "+billTaxDtl+" b ON a.strBillNo = b.strBillNo and date(a.dteBillDate)=date(b.dteBillDate) "
//                        + "INNER JOIN tblTaxHd c ON b.strTaxCode = c.strTaxCode "
//                        + "LEFT OUTER JOIN tblposmaster d ON a.strposcode=d.strposcode "
//                        + "where date(a.dteBillDate) between '" + billDate + "' and '" + billDate + "' ");
//                if (!posCode.equalsIgnoreCase("All"))
//                {
//                    sqlTaxBuilder.append("and a.strPOSCode='" + posCode + "'  ");
//                }
//                if (clsGlobalVarClass.gEnableShiftYN && (!String.valueOf(shiftNo).equalsIgnoreCase("All")))
//                {
//                    sqlTaxBuilder.append("and a.intShiftCode='" + shiftNo + "' ");
//                }
//                sqlTaxBuilder.append("group by c.strTaxCode,c.strTaxDesc ");
//                ResultSet  rsTaxDtl = clsGlobalVarClass.dbMysql.executeResultSet(sqlTaxBuilder.toString());
//
//                while (rsTaxDtl.next())
//                {
//                    funWriteTextWithBlankLines("  " + rsTaxDtl.getString(2), 21, bufferedWriter);
//                    funWriteTextWithBlankLines(rsTaxDtl.getString(3), 10, bufferedWriter);
//                    funWriteTextWithBlankLines(rsTaxDtl.getString(4), 9, bufferedWriter);
//                    bufferedWriter.newLine();
//
//                    totalTableAmt += rsTaxDtl.getDouble(3);
//                    totalTaxAmt += rsTaxDtl.getDouble(4);
//                }
//                rsTaxDtl.close();
//
//                bufferedWriter.newLine();
//                bufferedWriter.write(dashLinesFor42Chars);
//                bufferedWriter.newLine();
//
//                funWriteTextWithBlankLines("  " + "Total Taxation", 21, bufferedWriter);
//                funWriteTextWithBlankLines("", 10, bufferedWriter);
//                funWriteTextWithBlankLines(decimalFormatFor2DecPoint.format(totalTaxAmt), 9, bufferedWriter);
//                bufferedWriter.newLine();
//                //End of Settlement Brkup
//            }
//            rsDayend.close();
//
//            //group wise subtotal
//            StringBuilder sqlBuilder = new StringBuilder();
//
//            Map<String, clsGroupSubGroupWiseSales> mapGroupWiseData = new HashMap<>();
//
//            //live group data
//            sqlBuilder.setLength(0);
//            sqlBuilder.append("SELECT c.strGroupCode,c.strGroupName, SUM(b.dblQuantity), SUM(b.dblAmount)- SUM(b.dblDiscountAmt),f.strPosName "
//                    + ", '" + clsGlobalVarClass.gUserCode + "',b.dblRate, SUM(b.dblAmount), SUM(b.dblDiscountAmt),a.strPOSCode "
//                    + ", SUM(b.dblAmount)- SUM(b.dblDiscountAmt)+ SUM(b.dblTaxAmount) "
//                    + "FROM tblbillhd a,tblbilldtl b,tblgrouphd c,tblsubgrouphd d,tblitemmaster e,tblposmaster f "
//                    + "WHERE a.strBillNo=b.strBillNo "
//                    + " and date(a.dteBillDate)=date(b.dteBillDate) "
//                    + "AND a.strPOSCode=f.strPOSCode "
//                    + "AND a.strClientCode=b.strClientCode "
//                    + "AND b.strItemCode=e.strItemCode "
//                    + "AND c.strGroupCode=d.strGroupCode "
//                    + "AND d.strSubGroupCode=e.strSubGroupCode "
//                    + "AND a.strPOSCode = '" + posCode + "' "
//                    + "AND DATE(a.dteBillDate)='" + billDate + "'"                    
//                    + "GROUP BY c.strGroupCode, c.strGroupName, a.strPoscode;");
//            ResultSet rsGroupData = clsGlobalVarClass.dbMysql.executeResultSet(sqlBuilder.toString());
//            while (rsGroupData.next())
//            {
//                String groupCode = rsGroupData.getString(1);//groupCode
//                String groupName = rsGroupData.getString(2);//groupCode
//                double netTotalPlusTax = rsGroupData.getDouble(11);//subTotal-disc+tax
//
//                if (mapGroupWiseData.containsKey(groupCode))
//                {
//                    clsGroupSubGroupWiseSales objGroupWiseSales = mapGroupWiseData.get(groupCode);
//                    objGroupWiseSales.setDblNetTotalPlusTax(objGroupWiseSales.getDblNetTotalPlusTax() + netTotalPlusTax);
//                }
//                else
//                {
//                    clsGroupSubGroupWiseSales objGroupWiseSales = new clsGroupSubGroupWiseSales();
//                    objGroupWiseSales.setGroupCode(groupCode);
//                    objGroupWiseSales.setGroupName(groupName);
//                    objGroupWiseSales.setDblNetTotalPlusTax(netTotalPlusTax);
//
//                    mapGroupWiseData.put(groupCode, objGroupWiseSales);
//                }
//            }
//            rsGroupData.close();
//            //live modifier group data
//            sqlBuilder.setLength(0);
//            sqlBuilder.append("SELECT c.strGroupCode,c.strGroupName, SUM(b.dblQuantity), SUM(b.dblAmount)- SUM(b.dblDiscAmt),f.strPOSName "
//                    + ",'" + clsGlobalVarClass.gUserCode + "','0', SUM(b.dblAmount), SUM(b.dblDiscAmt),a.strPOSCode, SUM(b.dblAmount)- SUM(b.dblDiscAmt) "
//                    + "FROM tblbillmodifierdtl b,tblbillhd a,tblposmaster f,tblitemmaster d,tblsubgrouphd e,tblgrouphd c "
//                    + "WHERE a.strBillNo=b.strBillNo "
//                    + " and date(a.dteBillDate)=date(b.dteBillDate) "
//                    + "AND a.strPOSCode=f.strPosCode "
//                    + "AND a.strClientCode=b.strClientCode  "
//                    + "AND LEFT(b.strItemCode,7)=d.strItemCode  "
//                    + "AND d.strSubGroupCode=e.strSubGroupCode "
//                    + "AND e.strGroupCode=c.strGroupCode  "
//                    + "AND b.dblamount>0 "
//                    + "AND a.strPOSCode = '" + posCode + "' "                    
//                    + "AND DATE(a.dteBillDate) = '" + billDate + "' "
//                    + "GROUP BY c.strGroupCode, c.strGroupName, a.strPoscode;");
//            rsGroupData = clsGlobalVarClass.dbMysql.executeResultSet(sqlBuilder.toString());
//            while (rsGroupData.next())
//            {
//                String groupCode = rsGroupData.getString(1);//groupCode
//                String groupName = rsGroupData.getString(2);//groupCode
//                double netTotalPlusTax = rsGroupData.getDouble(11);//subTotal-disc+tax
//
//                if (mapGroupWiseData.containsKey(groupCode))
//                {
//                    clsGroupSubGroupWiseSales objGroupWiseSales = mapGroupWiseData.get(groupCode);
//                    objGroupWiseSales.setDblNetTotalPlusTax(objGroupWiseSales.getDblNetTotalPlusTax() + netTotalPlusTax);
//                }
//                else
//                {
//                    clsGroupSubGroupWiseSales objGroupWiseSales = new clsGroupSubGroupWiseSales();
//                    objGroupWiseSales.setGroupCode(groupCode);
//                    objGroupWiseSales.setGroupName(groupName);
//                    objGroupWiseSales.setDblNetTotalPlusTax(netTotalPlusTax);
//
//                    mapGroupWiseData.put(groupCode, objGroupWiseSales);
//                }
//            }
//            rsGroupData.close();
//
//            //QFile group data
//            sqlBuilder.setLength(0);
//            sqlBuilder.append("SELECT c.strGroupCode,c.strGroupName, SUM(b.dblQuantity), SUM(b.dblAmount)- SUM(b.dblDiscountAmt),f.strPosName "
//                    + ", '" + clsGlobalVarClass.gUserCode + "',b.dblRate, SUM(b.dblAmount), SUM(b.dblDiscountAmt),a.strPOSCode "
//                    + ", SUM(b.dblAmount)- SUM(b.dblDiscountAmt)+ SUM(b.dblTaxAmount) "
//                    + "FROM tblqbillhd a,tblqbilldtl b,tblgrouphd c,tblsubgrouphd d,tblitemmaster e,tblposmaster f "
//                    + "WHERE a.strBillNo=b.strBillNo "
//                    + "and date(a.dteBillDate)=date(b.dteBillDate) "
//                    + "AND a.strPOSCode=f.strPOSCode "
//                    + "AND a.strClientCode=b.strClientCode "
//                    + "AND b.strItemCode=e.strItemCode "
//                    + "AND c.strGroupCode=d.strGroupCode "
//                    + "AND d.strSubGroupCode=e.strSubGroupCode "
//                    + "AND a.strPOSCode = '" + posCode + "' "
//                    + "AND DATE(a.dteBillDate)='" + billDate + "'"                    
//                    + "GROUP BY c.strGroupCode, c.strGroupName, a.strPoscode;");
//            rsGroupData = clsGlobalVarClass.dbMysql.executeResultSet(sqlBuilder.toString());
//            while (rsGroupData.next())
//            {
//                String groupCode = rsGroupData.getString(1);//groupCode
//                String groupName = rsGroupData.getString(2);//groupCode
//                double netTotalPlusTax = rsGroupData.getDouble(11);//subTotal-disc+tax
//
//                if (mapGroupWiseData.containsKey(groupCode))
//                {
//                    clsGroupSubGroupWiseSales objGroupWiseSales = mapGroupWiseData.get(groupCode);
//                    objGroupWiseSales.setDblNetTotalPlusTax(objGroupWiseSales.getDblNetTotalPlusTax() + netTotalPlusTax);
//                }
//                else
//                {
//                    clsGroupSubGroupWiseSales objGroupWiseSales = new clsGroupSubGroupWiseSales();
//                    objGroupWiseSales.setGroupCode(groupCode);
//                    objGroupWiseSales.setGroupName(groupName);
//                    objGroupWiseSales.setDblNetTotalPlusTax(netTotalPlusTax);
//
//                    mapGroupWiseData.put(groupCode, objGroupWiseSales);
//                }
//            }
//            rsGroupData.close();
//            //QFile modifier group data
//            sqlBuilder.setLength(0);
//            sqlBuilder.append("SELECT c.strGroupCode,c.strGroupName, SUM(b.dblQuantity), SUM(b.dblAmount)- SUM(b.dblDiscAmt),f.strPOSName "
//                    + ",'" + clsGlobalVarClass.gUserCode + "','0', SUM(b.dblAmount), SUM(b.dblDiscAmt),a.strPOSCode, SUM(b.dblAmount)- SUM(b.dblDiscAmt) "
//                    + "FROM tblqbillmodifierdtl b,tblqbillhd a,tblposmaster f,tblitemmaster d,tblsubgrouphd e,tblgrouphd c "
//                    + "WHERE a.strBillNo=b.strBillNo "
//                    + " and date(a.dteBillDate)=date(b.dteBillDate) "
//                    + "AND a.strPOSCode=f.strPosCode "
//                    + "AND a.strClientCode=b.strClientCode  "
//                    + "AND LEFT(b.strItemCode,7)=d.strItemCode  "
//                    + "AND d.strSubGroupCode=e.strSubGroupCode "
//                    + "AND e.strGroupCode=c.strGroupCode  "
//                    + "AND b.dblamount>0 "
//                    + "AND a.strPOSCode = '" + posCode + "' "                    
//                    + "AND DATE(a.dteBillDate) = '" + billDate + "' "
//                    + "GROUP BY c.strGroupCode, c.strGroupName, a.strPoscode;");
//            rsGroupData = clsGlobalVarClass.dbMysql.executeResultSet(sqlBuilder.toString());
//            while (rsGroupData.next())
//            {
//                String groupCode = rsGroupData.getString(1);//groupCode
//                String groupName = rsGroupData.getString(2);//groupCode
//                double netTotalPlusTax = rsGroupData.getDouble(11);//subTotal-disc+tax
//
//                if (mapGroupWiseData.containsKey(groupCode))
//                {
//                    clsGroupSubGroupWiseSales objGroupWiseSales = mapGroupWiseData.get(groupCode);
//                    objGroupWiseSales.setDblNetTotalPlusTax(objGroupWiseSales.getDblNetTotalPlusTax() + netTotalPlusTax);
//                }
//                else
//                {
//                    clsGroupSubGroupWiseSales objGroupWiseSales = new clsGroupSubGroupWiseSales();
//                    objGroupWiseSales.setGroupCode(groupCode);
//                    objGroupWiseSales.setGroupName(groupName);
//                    objGroupWiseSales.setDblNetTotalPlusTax(netTotalPlusTax);
//
//                    mapGroupWiseData.put(groupCode, objGroupWiseSales);
//                }
//            }
//            rsGroupData.close();
//
//            if (mapGroupWiseData.size() > 0)
//            {
//                bufferedWriter.newLine();
//                bufferedWriter.write(dashLinesFor42Chars);
//                bufferedWriter.newLine();
//                bufferedWriter.write("   Group                 Amount With Tax   ");
//                bufferedWriter.newLine();
//                bufferedWriter.write(dashLinesFor42Chars);
//                bufferedWriter.newLine();
//                for (clsGroupSubGroupWiseSales objGroupWiseSales : mapGroupWiseData.values())
//                {
////                    funWriteTextWithBlankLines("  " + objGroupWiseSales.getGroupName(), 21, ReportOut);
////                    funWriteTextWithBlankLines(String.valueOf(Math.rint(objGroupWiseSales.getDblNetTotalPlusTax())), 10, ReportOut);     
//                    funWriteTotal(objGroupWiseSales.getGroupName(), String.valueOf(Math.rint(objGroupWiseSales.getDblNetTotalPlusTax())), bufferedWriter, "");
//                    bufferedWriter.newLine();
//                }
//                bufferedWriter.write(dashLinesFor42Chars);
//            }
//
//            //        
//            bufferedWriter.newLine();
//            bufferedWriter.newLine();
//            bufferedWriter.newLine();
//            bufferedWriter.newLine();
//            if ("linux".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
//            {
//                bufferedWriter.write("V");//Linux
//            }
//            else if ("windows".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
//            {
//                if ("Inbuild".equalsIgnoreCase(clsPosConfigFile.gPrinterType))
//                {
//                    bufferedWriter.write("V");
//                }
//                else
//                {
//                    bufferedWriter.write("m");//windows
//                }
//            }
//
//            bufferedWriter.close();
//            fstream_Report.close();
//
//            if (clsGlobalVarClass.gShowBill)
//            {
//                funShowTextFile(Text_DayEndReport, "", clsGlobalVarClass.gBillPrintPrinterPort);
//            }
//            if (printYN.equalsIgnoreCase("Y"))
//            {
//                funPrintToPrinter(clsGlobalVarClass.gBillPrintPrinterPort, "", "dayend", "N", isReprint);
//            }
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    private void funCreateTempFolder()
//    {
//        try
//        {
//            String filePath = System.getProperty("user.dir");
//            File Text_KOT = new File(filePath + "/Temp");
//            if (!Text_KOT.exists())
//            {
//                Text_KOT.mkdirs();
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    private void funPrintBlankSpace(String printWord, BufferedWriter BWOut)
//    {
//        try
//        {
//            int wordSize = printWord.length();
//            int actualPrintingSize = clsGlobalVarClass.gColumnSize;
//            int availableBlankSpace = actualPrintingSize - wordSize;
//
//            int leftSideSpace = availableBlankSpace / 2;
//            if (leftSideSpace > 0)
//            {
//                for (int i = 0; i < leftSideSpace; i++)
//                {
//                    BWOut.write(" ");
//                }
//            }
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    private void funPrintToPrinter(String primaryPrinterName, String secPrinterName, String type, String printOnBothPrinters, boolean isReprint)
//    {
//        try
//        {
//            String reportname = "";
//            String fileName = "";
//            if (type.equalsIgnoreCase("kot") || type.equalsIgnoreCase("checkkot"))
//            {
//                fileName = "Temp/Temp_KOT.txt";
//                //fileName = "Temp/Temp_KOT.rtf";
//            }
//            else if (type.equalsIgnoreCase("ConsolidatedKOT"))
//            {
//                fileName = "Temp/Temp_KOT.txt";
//            }
//            else if (type.equalsIgnoreCase("dayend"))
//            {
//                fileName = "Temp/Temp_DayEndReport.txt";
//                reportname = "dayend";
//            }
//            else if (type.equalsIgnoreCase("Adv Receipt"))
//            {
//                reportname = "Adv Receipt";
//            }
//            else if (type.equalsIgnoreCase("ItemWiseKOT"))
//            {
//                fileName = "/Temp/" + fileName + ".txt";
//            }
//            else
//            {
//                fileName = "Temp/Temp_Bill.txt";
//                reportname = "bill";
//            }
//
//            if ("windows".equalsIgnoreCase(clsPosConfigFile.gPrintOS))//&& clsGlobalVarClass.gPrintType.equalsIgnoreCase("Text File")
//            {
//                if (type.equalsIgnoreCase("kot"))
//                {
//                    //System.out.println("G Print YN="+clsGlobalVarClass.gPrintKOTYN);
//                    if (clsGlobalVarClass.gPrintKOTYN)
//                    {
//                        funPrintKOTWindows(primaryPrinterName, secPrinterName, printOnBothPrinters);
//                        if (clsGlobalVarClass.gMultipleKOTPrint)
//                        {
//                            if (!isReprint)
//                            {
//                                funAppendDuplicate(fileName);
//                            }
//                            funPrintKOTWindows(primaryPrinterName, secPrinterName, printOnBothPrinters);
//                        }
//                    }
//                }
//                else if (type.equalsIgnoreCase("checkkot"))
//                {
//                    funPrintCheckKOTWindows(primaryPrinterName);
//                }
//                else if (type.equalsIgnoreCase("ConsolidatedKOT"))
//                {
//                    funPrintConsolidatedKOTWindows(primaryPrinterName);
//                }
//                else if (type.equalsIgnoreCase("ItemWiseKOT"))
//                {
//                    funPrintItemWiseKOT(primaryPrinterName, secPrinterName, fileName);
//                }
//                else
//                {
//                    funPrintBillWindows(reportname);
//                    //Avoid Muliple Bill Printing
//                    if (!type.equalsIgnoreCase("dayend"))
//                    {
//                        if (clsGlobalVarClass.gMultiBillPrint)
//                        {
//                            if (!isReprint)
//                            {
//                                funAppendDuplicate(fileName);
//                            }
//                            funPrintBillWindows(reportname);
//                        }
//                    }
//                }
//            }
//            else if ("linux".equalsIgnoreCase(clsPosConfigFile.gPrintOS) && clsGlobalVarClass.gPrintType.equalsIgnoreCase("Text File"))
//            {
//                if (type.equalsIgnoreCase("kot"))
//                {
//                    //System.out.println("G Print YN="+clsGlobalVarClass.gPrintKOTYN);
//                    if (clsGlobalVarClass.gPrintKOTYN)
//                    {
//                        Process process = Runtime.getRuntime().exec("lpr -P " + primaryPrinterName + " " + fileName, null);
//
//                        if (clsGlobalVarClass.gMultipleKOTPrint)
//                        {
//                            if (!isReprint)
//                            {
//                                funAppendDuplicate(fileName);
//                            }
//                            process = Runtime.getRuntime().exec("lpr -P " + primaryPrinterName + " " + fileName, null);
//                        }
//                    }
//                }
//                else if (type.equalsIgnoreCase("checkkot"))
//                {
//                    Process process = Runtime.getRuntime().exec("lpr -P " + primaryPrinterName + " " + fileName, null);
//                }
//                else if (type.equalsIgnoreCase("ConsolidatedKOT"))
//                {
//                    Process process = Runtime.getRuntime().exec("lpr -P " + primaryPrinterName + " " + fileName, null);
//                }
//                else if (type.equalsIgnoreCase("ItemWiseKOT"))
//                {
//                    Process process = Runtime.getRuntime().exec("lpr -P " + primaryPrinterName + " " + fileName, null);
//                }
//                else
//                {
//                    //Process process = Runtime.getRuntime().exec("lpr -P " + primaryPrinterName + " " + fileName, null);
//                    Process process = Runtime.getRuntime().exec("lpr -P " + clsGlobalVarClass.gBillPrintPrinterPort + " " + fileName, null);
//                    if (!type.equalsIgnoreCase("dayend"))
//                    {
//                        if (clsGlobalVarClass.gMultiBillPrint)
//                        {
//                            if (!isReprint)
//                            {
//                                funAppendDuplicate(fileName);
//                            }
//                            process = Runtime.getRuntime().exec("lpr -P " + clsGlobalVarClass.gBillPrintPrinterPort + " " + fileName, null);
//                        }
//                    }
//                }
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    private void funAppendDuplicate(String fileName)
//    {
//        try
//        {
//            File fileKOTPrint = new File(fileName);
////            RandomAccessFile f = new RandomAccessFile(fileKOTPrint, "rw");
////            f.seek(0); // to the beginning                  
////            BufferedWriter KotOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileKOTPrint), "UTF8"));            
////            funPrintBlankSpace("[DUPLICATE]", KotOut);            
////            KotOut.write("[DUPLICATE]");              
////            KotOut.newLine();            
////            KotOut.close();
////            f.close();                                    
//
//            String filePath = System.getProperty("user.dir");
//            filePath += "/Temp/Temp_KOT2.txt";
//            File fileKOTPrint2 = new File(filePath);
//            BufferedWriter KotOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileKOTPrint2), "UTF8"));
//            funPrintBlankSpace("[DUPLICATE]", KotOut);
//            KotOut.write("[DUPLICATE]");
//            KotOut.newLine();
//
//            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileKOTPrint)));
//            String line = null;
//            while ((line = br.readLine()) != null)
//            {
//                KotOut.write(line);
//                KotOut.newLine();
//            }
//            br.close();
//            KotOut.close();
//
//            String content = new String(Files.readAllBytes(Paths.get(filePath)));
//            Files.write(Paths.get(fileName), content.getBytes(), StandardOpenOption.CREATE);
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    private void funWriteTotal(String title, String total, BufferedWriter out, String format)
//    {
//        try
//        {
//            int counter = 0;
//            out.write("  ");
//            counter = counter + 2;
//            int length = title.length();
//            out.write(title);
//            counter = counter + length;
//            funWriteFormattedAmt(counter, total, out, format);
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    private void funWriteTextWithBlankLines(String text, int len, BufferedWriter out) throws Exception
//    {
//        int remLen = len - text.trim().length();
//        out.write(text);
//        for (int cn = 0; cn < remLen; cn++)
//        {
//            out.write(" ");
//        }
//    }
//
//    public void funShowTextFile(File file, String formName, String printerInfo)
//    {
//        try
//        {
//            String data = "";
//            FileReader fread = new FileReader(file);
//            //BufferedReader KOTIn = new BufferedReader(fread);
//            FileInputStream fis = new FileInputStream(file);
//            BufferedReader KOTIn = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
//            String line = "";
//            while ((line = KOTIn.readLine()) != null)
//            {
//                data = data + line + "\n";
//            }
//            String fileName = file.getName();
//            String name = "";
//            if (formName.trim().length() > 0)
//            {
//                name = formName;
//            }
//            if ("Temp_DayEndReport.txt".equalsIgnoreCase(fileName))
//            {
//                name = "DayEnd";
//            }
//            new frmShowTextFile(data, name, file, printerInfo).setVisible(true);
//            fread.close();
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    private void funPrintKOTWindows(String primaryPrinterName, String secPrinterName, String printOnBothPrinters)
//    {
//        String filePath = System.getProperty("user.dir");
//        String fileName = (filePath + "/Temp/Temp_KOT.txt");
//        //String fileName = (filePath + "/Temp/Temp_KOT.rtf");
//        try
//        {
//            int printerIndex = 0;
//            String printerStatus = "Not Found";
//            System.out.println("Primary Name=" + primaryPrinterName);
//            System.out.println("Sec Name=" + secPrinterName);
//
//            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
//            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
//            primaryPrinterName = primaryPrinterName.replaceAll("#", "\\\\");
//            secPrinterName = secPrinterName.replaceAll("#", "\\\\");
//
//            PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
//            for (int i = 0; i < printService.length; i++)
//            {
//                System.out.println("Service=" + printService[i].getName() + "\tPrim P=" + primaryPrinterName);
//                String printerServiceName = printService[i].getName();
//
//                if (primaryPrinterName.equalsIgnoreCase(printerServiceName))
//                {
//                    System.out.println("Printer=" + primaryPrinterName);
//                    printerIndex = i;
//                    printerStatus = "Found";
//                    break;
//                }
//            }
//
//            if (printerStatus.equals("Found"))
//            {
//                DocPrintJob job = printService[printerIndex].createPrintJob();
//                FileInputStream fis = new FileInputStream(fileName);
//                DocAttributeSet das = new HashDocAttributeSet();
//                Doc doc = new SimpleDoc(fis, flavor, das);
//                job.print(doc, pras);
//                String printerInfo = "";
//
//                PrintServiceAttributeSet att = printService[printerIndex].getAttributes();
//                for (Attribute a : att.toArray())
//                {
//                    String attributeName;
//                    String attributeValue;
//                    attributeName = a.getName();
//                    attributeValue = att.get(a.getClass()).toString();
//                    if (attributeName.trim().equalsIgnoreCase("queued-job-count"))
//                    {
//                        clsGlobalVarClass.gPrinterQueueStatus = attributeValue;
//                        printerInfo = primaryPrinterName + "!" + attributeValue;
//                        //System.out.println(attributeName + " : " + attributeValue);
//                    }
//                }
//                if (printOnBothPrinters.equals("Y"))
//                {
//                    funPrintOnSecPrinter(secPrinterName, fileName);
//                }
//            }
//            else
//            {
//                funPrintOnSecPrinter(secPrinterName, fileName);
//                //JOptionPane.showMessageDialog(null,primaryPrinterName+" Printer Not Found");
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            if (clsGlobalVarClass.gShowPrinterErrorMsg)
//            {
//                try
//                {
//                    funPrintOnSecPrinter(secPrinterName, fileName);
//                }
//                catch (Exception ex)
//                {
//                    JOptionPane.showMessageDialog(null, "Secondary Printer Error= " + ex.getMessage());
//                }
//                JOptionPane.showMessageDialog(null, e.getMessage(), "Error Code - TFG 01", JOptionPane.ERROR_MESSAGE);
//            }
//        }
//    }
//
//    private void funPrintOnSecPrinter(String secPrinterName, String fileName) throws Exception
//    {
//        String printerStatus = "Not Found";
//        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
//        DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
//        PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
//        int printerIndex = 0;
//        for (int i = 0; i < printService.length; i++)
//        {
//            System.out.println("Service=" + printService[i].getName() + "\tSec P=" + secPrinterName);
//            String printerServiceName = printService[i].getName();
//
//            if (secPrinterName.equalsIgnoreCase(printerServiceName))
//            {
//                System.out.println("Sec Printer=" + secPrinterName);
//                printerIndex = i;
//                printerStatus = "Found";
//                break;
//            }
//        }
//        if (printerStatus.equals("Found"))
//        {
//            String printerInfo = "";
//            DocPrintJob job = printService[printerIndex].createPrintJob();
//            FileInputStream fis = new FileInputStream(fileName);
//            DocAttributeSet das = new HashDocAttributeSet();
//            Doc doc = new SimpleDoc(fis, flavor, das);
//            job.addPrintJobListener(new clsTextFileGenerationForPrinting2.MyPrintJobListener());
//            job.print(doc, pras);
//
//            PrintServiceAttributeSet att = printService[printerIndex].getAttributes();
//            for (Attribute a : att.toArray())
//            {
//                String attributeName;
//                String attributeValue;
//                attributeName = a.getName();
//                attributeValue = att.get(a.getClass()).toString();
//                if (attributeName.trim().equalsIgnoreCase("queued-job-count"))
//                {
//                    clsGlobalVarClass.gPrinterQueueStatus = attributeValue;
//                    printerInfo = secPrinterName + "!" + attributeValue;
//                }
//                System.out.println(attributeName + " : " + attributeValue);
//            }
//            if (clsGlobalVarClass.gShowBill)
//            {
//                funShowTextFile(new File(fileName), "", printerInfo);
//            }
//        }
//        else
//        {
//            JOptionPane.showMessageDialog(null, secPrinterName + " Printer Not Found");
//        }
//    }
//
//    private void funPrintCheckKOTWindows(String printerName)
//    {
//        try
//        {
//            int printerIndex = 0;
//            String filePath = System.getProperty("user.dir");
//            String filename = (filePath + "/Temp/Temp_KOT.txt");
//            String billPrinterName = clsGlobalVarClass.gBillPrintPrinterPort;
//            billPrinterName = billPrinterName.replaceAll("#", "\\\\");
//            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
//            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
//            PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
//            for (int i = 0; i < printService.length; i++)
//            {
//                System.out.println("Sys=" + printService[i].getName() + "\tBill Printer=" + billPrinterName);
//                if (billPrinterName.equalsIgnoreCase(printService[i].getName()))
//                {
//                    System.out.println("Bill Printer Sel=" + billPrinterName);
//                    printerIndex = i;
//                    break;
//                }
//            }
//
//            DocPrintJob job = printService[printerIndex].createPrintJob();
//            FileInputStream fis = new FileInputStream(filename);
//            DocAttributeSet das = new HashDocAttributeSet();
//            Doc doc = new SimpleDoc(fis, flavor, das);
//            job.print(doc, pras);
//        }
//        catch (Exception e)
//        {
//            if (clsGlobalVarClass.gShowPrinterErrorMsg)
//            {
//                JOptionPane.showMessageDialog(null, e.getMessage(), "Error Code - TFG 01", JOptionPane.ERROR_MESSAGE);
//            }
//        }
//    }
//
//    private void funPrintItemWiseKOT(String primaryPrinterName, String secPrinterName, String fileName)
//    {
//        String filePath = System.getProperty("user.dir");
//        fileName = (filePath + "/Temp/" + fileName + ".txt");
//
//        try
//        {
//            String billPrinterName = clsGlobalVarClass.gBillPrintPrinterPort;
//
//            billPrinterName = billPrinterName.replaceAll("#", "\\\\");
//            int printerIndex = 0;
//            PrintRequestAttributeSet printerReqAtt = new HashPrintRequestAttributeSet();
//            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
//            PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, printerReqAtt);
//            for (int i = 0; i < printService.length; i++)
//            {
//                System.out.println("Sys=" + printService[i].getName() + "\tBill Printer=" + billPrinterName);
//                if (billPrinterName.equalsIgnoreCase(printService[i].getName()))
//                {
//                    System.out.println("ItemWise KOT Printer found=>" + billPrinterName);
//                    printerIndex = i;
//                    break;
//                }
//            }
//            //PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
//            //DocPrintJob job = defaultService.createPrintJob();
//            DocPrintJob job = printService[printerIndex].createPrintJob();
//            FileInputStream fis = new FileInputStream(fileName);
//
//            DocAttributeSet das = new HashDocAttributeSet();
//            Doc doc = new SimpleDoc(fis, flavor, das);
//            job.print(doc, printerReqAtt);
//            System.out.println("Print Job Sent->" + fileName);
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            if (clsGlobalVarClass.gShowPrinterErrorMsg)
//            {
//                JOptionPane.showMessageDialog(null, e.getMessage(), "Error Code - TFG 02", JOptionPane.ERROR_MESSAGE);
//            }
//        }
//    }
//
//    /**
//     * printBillWindows() method print to Default Printer. No Parameter required
//     */
//    private void funPrintBillWindows(String type)
//    {
//        try
//        {
//            //System.out.println("Print Bill");
//            String filePath = System.getProperty("user.dir");
//            String fileName = "";
//            String billPrinterNames[] = clsGlobalVarClass.gBillPrintPrinterPort.split(",");
//
//            for (int printer = 0; printer < billPrinterNames.length; printer++)
//            {
//                String billPrinterName = billPrinterNames[printer];
//
//                if (type.equalsIgnoreCase("bill"))
//                {
//                    fileName = (filePath + "/Temp/Temp_Bill.txt");
//                }
//                else if (type.equalsIgnoreCase("Adv Receipt"))
//                {
//                    fileName = (filePath + "/Temp/Temp_Bill.txt");
//                    billPrinterName = clsGlobalVarClass.gAdvReceiptPrinterPort;
//                }
//                else if (type.equalsIgnoreCase("dayend"))
//                {
//                    fileName = (filePath + "/Temp/Temp_DayEndReport.txt");
//                }
//
//                billPrinterName = billPrinterName.replaceAll("#", "\\\\");
//                int printerIndex = 0;
//                PrintRequestAttributeSet printerReqAtt = new HashPrintRequestAttributeSet();
//                DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
//                PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, printerReqAtt);
//                for (int i = 0; i < printService.length; i++)
//                {
//                    System.out.println("Sys=" + printService[i].getName() + "\tBill Printer=" + billPrinterName);
//                    if (billPrinterName.equalsIgnoreCase(printService[i].getName()))
//                    {
//                        System.out.println("Bill Printer Sel=" + billPrinterName);
//                        printerIndex = i;
//                        break;
//                    }
//                }
//                PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
//                //DocPrintJob job = defaultService.createPrintJob();
//                DocPrintJob job = printService[printerIndex].createPrintJob();
//                FileInputStream fis = new FileInputStream(fileName);
//                DocAttributeSet das = new HashDocAttributeSet();
//                Doc doc = new SimpleDoc(fis, flavor, das);
//                job.print(doc, printerReqAtt);
//            }
//            if (clsGlobalVarClass.gOpenCashDrawerAfterBillPrintYN)
//            {
//                objUtility.funInvokeSampleJasper();
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            if (clsGlobalVarClass.gShowPrinterErrorMsg)
//            {
//                JOptionPane.showMessageDialog(null, e.getMessage(), "Error Code - TFG 02", JOptionPane.ERROR_MESSAGE);
//            }
//        }
//    }
//    /*
//     * Please Do Not modify Space in this Function Ritesh 10 Sept 214
//     *
//     *
//     */
//
//    private void funWriteFormattedAmt(int counter, String Amount, BufferedWriter out, String format)
//    {
//        try
//        {
//            int space = 30;
//            if (format.equals("Format3"))
//            {
//                space = 29;
//            }
//            if (format.equals("Format4"))
//            {
//                space = 34;
//            }
//            if (format.equals("Format5"))
//            {
//                space = 29;
//            }
//            if (format.equals("Format6"))
//            {
//                space = 30;
//            }
//            if (format.equals("Format11"))
//            {
//                space = 12;
//            }
//            if (format.equals("Format13"))
//            {
//                space = 29;
//            }
//            int usedSpace = space - counter;
//            for (int i = 0; i < usedSpace; i++)
//            {
//                out.write(" ");
//            }
//            out.write("  ");
//            String tempAmount = Amount;
//
//            int length = tempAmount.length();
//            switch (length)
//            {
//                case 1:
//                    out.write("        " + tempAmount);//8
//                    break;
//                case 2:
//                    out.write("       " + tempAmount);//7
//                    break;
//                case 3:
//                    out.write("      " + tempAmount);//6
//                    break;
//                case 4:
//                    out.write("     " + tempAmount);//5
//                    break;
//                case 5:
//                    out.write("    " + tempAmount);//4
//                    break;
//                case 6:
//                    out.write("   " + tempAmount);//3
//                    break;
//                case 7:
//                    out.write("  " + tempAmount);//2
//                    break;
//                case 8:
//                    out.write(" " + tempAmount);//1
//                    break;
//                case 9:
//                    out.write(tempAmount);//0
//                    break;
//                default:
//                    out.write(tempAmount);//0
//                    break;
//            }
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    public void funConsolidatedKOTForMakeKOTTextFileGeneration(String tableNo, String kotNo)
//    {
//        try
//        {
//            funCreateTempFolder();
//            String filePath = System.getProperty("user.dir");
//            File Text_Check_KOT = new File(filePath + "/Temp/Temp_KOT.txt");
//            FileWriter fstream = new FileWriter(Text_Check_KOT);
//
//            DecimalFormat decimalFormat = new DecimalFormat("#.###");
//
//            BufferedWriter checkKotOut = new BufferedWriter(fstream);
//
//            funPrintBlankSpace("Consolidated KOT", checkKotOut);
//            checkKotOut.write("Consolidated KOT");
//            checkKotOut.newLine();
//            checkKotOut.write(Line);
//            checkKotOut.newLine();
//
//            PreparedStatement pst = null;
//            String sql_CheckKot = "select a.strItemName, sum(a.dblItemQuantity),b.strTableName,"
//                    + " TIME_FORMAT(time(a.dteDateCreated),'%h:%i'),ifnull(a.strWaiterNo,'') "
//                    + " from tblitemrtemp a,tbltablemaster b,tblitemmaster c "
//                    + " where a.strTableNo=b.strTableNo "
//                    + " and left(a.strItemCode,7)=c.strItemCode "
//                    + " and a.strTableNo=? "
//                    + " and a.strKOTNo=? "
//                    + " and a.strNCKotYN='N' "
//                    + " group by a.strItemCode,a.strItemName "
//                    + " order by a.strSerialNo";
//            System.out.println(sql_CheckKot);
//            pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sql_CheckKot);
//            pst.setString(1, tableNo);
//            pst.setString(2, kotNo);
//            ResultSet rs_checkKOT = pst.executeQuery();
//            boolean flag_first = true;
//            String waiterName = "";
//            while (rs_checkKOT.next())
//            {
//                if (flag_first)
//                {
//                    checkKotOut.write("  TABLE NAME : " + rs_checkKOT.getString(3));
//                    if ("null".equalsIgnoreCase(rs_checkKOT.getString(5)))
//                    {
//                    }
//                    else
//                    {
//                        if (rs_checkKOT.getString(5).length() > 0)
//                        {
//                            waiterName = funGetWaiterName(rs_checkKOT.getString(5));
//                        }
//                        checkKotOut.newLine();
//                        checkKotOut.write("  WAITER NAME: " + waiterName);
//                    }
//
//                    checkKotOut.newLine();
//                    checkKotOut.write("  TIME: " + rs_checkKOT.getString(4));
//                    checkKotOut.newLine();
//                    checkKotOut.write(Line);
//                    checkKotOut.newLine();
//                    checkKotOut.write("   QTY          ITEM NAME");
//                    checkKotOut.newLine();
//                    checkKotOut.write(Line);
//                    checkKotOut.newLine();
//
//                    String itemqty = decimalFormat.format(rs_checkKOT.getDouble(2));
//                    String kotItemName = rs_checkKOT.getString(1).trim().toUpperCase();
//
//                    int noOfCharsToBePrinted = 30;
//                    int noOfCharsToBePrintedX2 = noOfCharsToBePrinted * 2;
//                    int qtyWidth = 6;
//
//                    if (rs_checkKOT.getString(1).startsWith("-->"))
//                    {
//                        if (!clsGlobalVarClass.gPrintModQtyOnKOT)
//                        {
//                            if (kotItemName.length() <= noOfCharsToBePrinted)
//                            {
//                                checkKotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + kotItemName);
//                            }
//                            else
//                            {
//                                if (kotItemName.length() >= noOfCharsToBePrintedX2)
//                                {
//                                    checkKotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + kotItemName.substring(0, noOfCharsToBePrinted));
//                                    checkKotOut.newLine();
//                                    checkKotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + "" + kotItemName.substring(noOfCharsToBePrinted, noOfCharsToBePrintedX2).trim());
//
//                                    checkKotOut.newLine();
//                                    checkKotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + "" + kotItemName.substring(noOfCharsToBePrintedX2, kotItemName.length()).trim());
//                                }
//                                else
//                                {
//                                    checkKotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + kotItemName.substring(0, noOfCharsToBePrinted));
//                                    checkKotOut.newLine();
//                                    checkKotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + "" + kotItemName.substring(noOfCharsToBePrinted, kotItemName.length()).trim());
//                                }
//                            }
//                        }
//                        else
//                        {
//                            if (kotItemName.length() <= noOfCharsToBePrinted)
//                            {
//                                checkKotOut.write("   " + String.format("%-" + qtyWidth + "s", itemqty) + kotItemName);
//                            }
//                            else
//                            {
//                                if (kotItemName.length() >= noOfCharsToBePrintedX2)
//                                {
//                                    checkKotOut.write("   " + String.format("%-" + qtyWidth + "s", itemqty) + kotItemName.substring(0, noOfCharsToBePrinted));
//                                    checkKotOut.newLine();
//                                    checkKotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + "" + kotItemName.substring(noOfCharsToBePrinted, noOfCharsToBePrintedX2).trim());
//
//                                    checkKotOut.newLine();
//                                    checkKotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + "" + kotItemName.substring(noOfCharsToBePrintedX2, kotItemName.length()).trim());
//                                }
//                                else
//                                {
//                                    checkKotOut.write("   " + String.format("%-" + qtyWidth + "s", itemqty) + kotItemName.substring(0, noOfCharsToBePrinted));
//                                    checkKotOut.newLine();
//                                    checkKotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + "" + kotItemName.substring(noOfCharsToBePrinted, kotItemName.length()).trim());
//                                }
//                            }
//                        }
//
//                    }
//                    else
//                    {
//                        if (kotItemName.length() <= noOfCharsToBePrinted)
//                        {
//                            checkKotOut.write("   " + String.format("%-" + qtyWidth + "s", itemqty) + kotItemName);
//                        }
//                        else
//                        {
//                            if (kotItemName.length() >= noOfCharsToBePrintedX2)
//                            {
//                                checkKotOut.write("   " + String.format("%-" + qtyWidth + "s", itemqty) + kotItemName.substring(0, noOfCharsToBePrinted));
//                                checkKotOut.newLine();
//                                checkKotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + "" + kotItemName.substring(noOfCharsToBePrinted, noOfCharsToBePrintedX2).trim());
//
//                                checkKotOut.newLine();
//                                checkKotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + "" + kotItemName.substring(noOfCharsToBePrintedX2, kotItemName.length()).trim());
//                            }
//                            else
//                            {
//                                checkKotOut.write("   " + String.format("%-" + qtyWidth + "s", itemqty) + kotItemName.substring(0, noOfCharsToBePrinted));
//                                checkKotOut.newLine();
//                                checkKotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + "" + kotItemName.substring(noOfCharsToBePrinted, kotItemName.length()).trim());
//                            }
//                        }
//                    }
//
//                    flag_first = false;
//                }
//                else
//                {
//                    String itemqty = decimalFormat.format(rs_checkKOT.getDouble(2));
//                    String kotItemName = rs_checkKOT.getString(1).trim().toUpperCase();
//
//                    int noOfCharsToBePrinted = 30;
//                    int noOfCharsToBePrintedX2 = noOfCharsToBePrinted * 2;
//                    int qtyWidth = 6;
//
//                    if (rs_checkKOT.getString(1).startsWith("-->"))
//                    {
//                        if (!clsGlobalVarClass.gPrintModQtyOnKOT)
//                        {
//                            if (kotItemName.length() <= noOfCharsToBePrinted)
//                            {
//                                checkKotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + kotItemName);
//                            }
//                            else
//                            {
//                                if (kotItemName.length() >= noOfCharsToBePrintedX2)
//                                {
//                                    checkKotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + kotItemName.substring(0, noOfCharsToBePrinted));
//                                    checkKotOut.newLine();
//                                    checkKotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + "" + kotItemName.substring(noOfCharsToBePrinted, noOfCharsToBePrintedX2).trim());
//
//                                    checkKotOut.newLine();
//                                    checkKotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + "" + kotItemName.substring(noOfCharsToBePrintedX2, kotItemName.length()).trim());
//                                }
//                                else
//                                {
//                                    checkKotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + kotItemName.substring(0, noOfCharsToBePrinted));
//                                    checkKotOut.newLine();
//                                    checkKotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + "" + kotItemName.substring(noOfCharsToBePrinted, kotItemName.length()).trim());
//                                }
//                            }
//                        }
//                        else
//                        {
//                            if (kotItemName.length() <= noOfCharsToBePrinted)
//                            {
//                                checkKotOut.write("   " + String.format("%-" + qtyWidth + "s", itemqty) + kotItemName);
//                            }
//                            else
//                            {
//                                if (kotItemName.length() >= noOfCharsToBePrintedX2)
//                                {
//                                    checkKotOut.write("   " + String.format("%-" + qtyWidth + "s", itemqty) + kotItemName.substring(0, noOfCharsToBePrinted));
//                                    checkKotOut.newLine();
//                                    checkKotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + "" + kotItemName.substring(noOfCharsToBePrinted, noOfCharsToBePrintedX2).trim());
//
//                                    checkKotOut.newLine();
//                                    checkKotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + "" + kotItemName.substring(noOfCharsToBePrintedX2, kotItemName.length()).trim());
//                                }
//                                else
//                                {
//                                    checkKotOut.write("   " + String.format("%-" + qtyWidth + "s", itemqty) + kotItemName.substring(0, noOfCharsToBePrinted));
//                                    checkKotOut.newLine();
//                                    checkKotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + "" + kotItemName.substring(noOfCharsToBePrinted, kotItemName.length()).trim());
//                                }
//                            }
//                        }
//
//                    }
//                    else
//                    {
//                        if (kotItemName.length() <= noOfCharsToBePrinted)
//                        {
//                            checkKotOut.write("   " + String.format("%-" + qtyWidth + "s", itemqty) + kotItemName);
//                        }
//                        else
//                        {
//                            if (kotItemName.length() >= noOfCharsToBePrintedX2)
//                            {
//                                checkKotOut.write("   " + String.format("%-" + qtyWidth + "s", itemqty) + kotItemName.substring(0, noOfCharsToBePrinted));
//                                checkKotOut.newLine();
//                                checkKotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + "" + kotItemName.substring(noOfCharsToBePrinted, noOfCharsToBePrintedX2).trim());
//
//                                checkKotOut.newLine();
//                                checkKotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + "" + kotItemName.substring(noOfCharsToBePrintedX2, kotItemName.length()).trim());
//                            }
//                            else
//                            {
//                                checkKotOut.write("   " + String.format("%-" + qtyWidth + "s", itemqty) + kotItemName.substring(0, noOfCharsToBePrinted));
//                                checkKotOut.newLine();
//                                checkKotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + "" + kotItemName.substring(noOfCharsToBePrinted, kotItemName.length()).trim());
//                            }
//                        }
//                    }
//                }
//                checkKotOut.newLine();
//            }
//
//            checkKotOut.newLine();
//            checkKotOut.newLine();
//            checkKotOut.newLine();
//            checkKotOut.newLine();
//            checkKotOut.newLine();
//            //checkKotOut.write("m"); //windows
//
//            if ("linux".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
//            {
//                checkKotOut.write("V");//Linux
//            }
//            else if ("windows".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
//            {
//                if ("Inbuild".equalsIgnoreCase(clsPosConfigFile.gPrinterType))
//                {
//                    checkKotOut.write("V");
//                }
//                else
//                {
//                    checkKotOut.write("m");//windows
//                }
//            }
//            rs_checkKOT.close();
//            checkKotOut.close();
//            fstream.close();
//
////            if (clsGlobalVarClass.gShowBill)
////            {
////                funShowTextFile(Text_Check_KOT, "", "");
////            }
//            if (clsGlobalVarClass.gConsolidatedKOTPrinterPort.length() > 0)
//            {
//                funPrintToPrinter(clsGlobalVarClass.gConsolidatedKOTPrinterPort, "", "ConsolidatedKOT", "N", false);
//            }
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    private String funGetWaiterName(String waiterNo)
//    {
//        String waiterName = "";
//        try
//        {
//            ResultSet rsWaiterName = clsGlobalVarClass.dbMysql.executeResultSet("select a.strWaiterNo,a.strWShortName,a.strWFullName "
//                    + "from tblwaitermaster a "
//                    + "where a.strWaiterNo='" + waiterNo + "' ");
//            if (rsWaiterName.next())
//            {
//                waiterName = rsWaiterName.getString(2);
//            }
//            rsWaiterName.close();
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        finally
//        {
//            return waiterName;
//        }
//    }
//
//    void funConsolidatedKOTForDirectBillerTextFileGeneration(String billNo)
//    {
//        try
//        {
//            PreparedStatement pst = null;
//            DecimalFormat decimalFormat = new DecimalFormat("#.###");
//
//            funCreateTempFolder();
//            String filePath = System.getProperty("user.dir");
//            File Text_KOT = new File(filePath + "/Temp/Temp_KOT.txt");
//            FileWriter fstream = new FileWriter(Text_KOT);
//            //BufferedWriter KotOut = new BufferedWriter(fstream);
//            BufferedWriter KotOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Text_KOT), "UTF8"));
//            boolean isReprint = false;
//
//            isReprint = true;
//            funPrintBlankSpace("CONSOLIDATED KOT", KotOut);
//            KotOut.write("CONSOLIDATED KOT");
//            KotOut.newLine();
//
//            String sql_PrintHomeDelivery = "select strOperationType from tblbillhd where strBillNo=? ";
//            pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sql_PrintHomeDelivery);
//            pst.setString(1, billNo);
//            ResultSet rs_PrintHomeDelivery = pst.executeQuery();
//            String operationType = "";
//            if (rs_PrintHomeDelivery.next())
//            {
//                operationType = rs_PrintHomeDelivery.getString(1);
//            }
//            rs_PrintHomeDelivery.close();
//            if (operationType.equalsIgnoreCase("HomeDelivery"))
//            {
//                if (clsGlobalVarClass.gPrintHomeDeliveryYN)
//                {
//                    funPrintBlankSpace("Home Delivery", KotOut);
//                    KotOut.write("Home Delivery");
//                    KotOut.newLine();
//                }
//
//            }
//            else if (operationType.equalsIgnoreCase("TakeAway"))
//            {
//                funPrintBlankSpace("Take Away", KotOut);
//                KotOut.write("Take Away");
//                KotOut.newLine();
//            }
//
//            //KotOut.newLine();
//            funPrintBlankSpace(clsGlobalVarClass.gPOSName, KotOut);
//            KotOut.write(clsGlobalVarClass.gPOSName);
//            KotOut.newLine();
//
//            // KotOut.newLine();
//            funPrintBlankSpace("DIRECT BILLER", KotOut);
//            KotOut.write("DIRECT BILLER");
//            KotOut.newLine();
//            KotOut.write(Line);
//            KotOut.newLine();
//            KotOut.write("  BILL No: " + billNo);
//            KotOut.newLine();
//            KotOut.write(Line);
//
//            String sql_DirectKOT_Date = "select dteBillDate from tblbilldtl where strBillNo=? ";
//            pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sql_DirectKOT_Date);
//            pst.setString(1, billNo);
//            ResultSet rs_DirectKOT_Date = pst.executeQuery();
//            if (rs_DirectKOT_Date.next())
//            {
//                KotOut.newLine();
//                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a ");
//                KotOut.write("  DATE & TIME: " + dateTimeFormat.format(rs_DirectKOT_Date.getObject(1)));
//            }
//            rs_DirectKOT_Date.close();
//
//            KotOut.newLine();
//            KotOut.write(Line);
//            KotOut.newLine();
//            KotOut.write("  QTY        ITEM NAME  ");
//            KotOut.newLine();
//            KotOut.write(Line);
//
//            String itemName = "a.strItemName";
//            if (clsGlobalVarClass.gPrintShortNameOnKOT)
//            {
//                itemName = "d.strShortName";
//            }
//
//            String sql_DirectKOT_Items = "SELECT a.strItemCode,a.strItemName, SUM(a.dblQuantity) "
//                    + "FROM tblbilldtl a "
//                    + "WHERE a.strBillNo=?  "
//                    + "GROUP BY a.strItemCode "
//                    + "ORDER BY a.strSequenceNo;";
//            //System.out.println(sql_DirectKOT_Items);
//            String areaCode = clsGlobalVarClass.gDirectAreaCode;
//
//            pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sql_DirectKOT_Items);
//            pst.setString(1, billNo);
//            ResultSet rs_DirectKOT_Items = pst.executeQuery();
//            while (rs_DirectKOT_Items.next())
//            {
//                String kotItemName = rs_DirectKOT_Items.getString(2).toUpperCase();
//                KotOut.newLine();
//
//                String itemQty = String.valueOf(decimalFormat.format(rs_DirectKOT_Items.getDouble(3)));
//
//                KotOut.write("  " + itemQty + "      ");
//                if (kotItemName.length() <= 25)
//                {
//                    KotOut.write(kotItemName);
//                }
//                else
//                {
//                    KotOut.write(kotItemName.substring(0, 25));
//                    KotOut.newLine();
//                    KotOut.write("            " + kotItemName.substring(25, kotItemName.length()));
//                }
//                //following code called for modifier
//
//                String sql_Modifier = " select a.strModifierName,a.dblQuantity,ifnull(b.strDefaultModifier,'N'),a.strDefaultModifierDeselectedYN "
//                        + "from tblbillmodifierdtl a "
//                        + "left outer join tblitemmodofier b on left(a.strItemCode,7)=if(b.strItemCode='',a.strItemCode,b.strItemCode) "
//                        + "and a.strModifierCode=if(a.strModifierCode=null,'',b.strModifierCode) "
//                        + "where a.strBillNo=? and left(a.strItemCode,7)=? ";
//                //System.out.println(sql_Modifier);
//
//                pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sql_Modifier);
//                pst.setString(1, billNo);
//                pst.setString(2, rs_DirectKOT_Items.getString(1));
//                ResultSet rs_Modifier = pst.executeQuery();
//                while (rs_Modifier.next())
//                {
//                    String modiQty = String.valueOf(decimalFormat.format(rs_Modifier.getDouble(2)));
//
//                    if (!clsGlobalVarClass.gPrintModQtyOnKOT)//dont't print modifier qty
//                    {
//                        if (rs_Modifier.getString(3).equalsIgnoreCase("Y") && rs_Modifier.getString(4).equalsIgnoreCase("Y"))
//                        {
//                            KotOut.newLine();
//                            KotOut.write("        " + "No " + rs_Modifier.getString(1).toUpperCase());
//                        }
//                        else if (!rs_Modifier.getString(3).equalsIgnoreCase("Y"))
//                        {
//                            KotOut.newLine();
//                            KotOut.write("        " + rs_Modifier.getString(1).toUpperCase());
//                        }
//                    }
//                    else
//                    {
//                        if (rs_Modifier.getString(3).equalsIgnoreCase("Y") && rs_Modifier.getString(4).equalsIgnoreCase("Y"))
//                        {
//                            KotOut.newLine();
//                            KotOut.write("  " + modiQty + "      " + "No " + rs_Modifier.getString(1).toUpperCase());
//                        }
//                        else if (!rs_Modifier.getString(3).equalsIgnoreCase("Y"))
//                        {
//                            KotOut.newLine();
//                            KotOut.write("  " + modiQty + "      " + rs_Modifier.getString(1).toUpperCase());
//                        }
//                    }
//                }
//                rs_Modifier.close();
//            }
//
//            rs_DirectKOT_Items.close();
//            KotOut.newLine();
//            KotOut.write(Line);
//            KotOut.newLine();
//            KotOut.newLine();
//            KotOut.newLine();
//            KotOut.newLine();
//            KotOut.newLine();
//            KotOut.newLine();
//            if ("linux".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
//            {
//                KotOut.write("V");//Linux
//            }
//            else if ("windows".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
//            {
//                if ("Inbuild".equalsIgnoreCase(clsPosConfigFile.gPrinterType))
//                {
//                    KotOut.write("V");
//                }
//                else
//                {
//                    KotOut.write("m");//windows
//                }
//            }
//            // KotOut.write("m");
//            KotOut.close();
//            fstream.close();
//            pst.close();
//
////            if (clsGlobalVarClass.gShowBill)
////            {
////                funShowTextFile(Text_KOT, "", "Printer Info!2");
////            }           
//            if (clsGlobalVarClass.gConsolidatedKOTPrinterPort.length() > 0)
//            {
//                funPrintToPrinter(clsGlobalVarClass.gConsolidatedKOTPrinterPort, "", "ConsolidatedKOT", "N", false);
//            }
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    private void funPrintConsolidatedKOTWindows(String primaryPrinterName)
//    {
//
//        try
//        {
//            int printerIndex = 0;
//            String filePath = System.getProperty("user.dir");
//            String filename = (filePath + "/Temp/Temp_KOT.txt");
//            String consolidatedPrinter = primaryPrinterName;
//            consolidatedPrinter = consolidatedPrinter.replaceAll("#", "\\\\");
//            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
//            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
//            PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
//            for (int i = 0; i < printService.length; i++)
//            {
//                System.out.println("Sys=" + printService[i].getName() + "\tConsolidated Printer=" + consolidatedPrinter);
//                if (consolidatedPrinter.equalsIgnoreCase(printService[i].getName()))
//                {
//                    System.out.println("tConsolidated Printer Sel=" + consolidatedPrinter);
//                    printerIndex = i;
//                    break;
//                }
//            }
//
//            DocPrintJob job = printService[printerIndex].createPrintJob();
//            FileInputStream fis = new FileInputStream(filename);
//            DocAttributeSet das = new HashDocAttributeSet();
//            Doc doc = new SimpleDoc(fis, flavor, das);
//            job.print(doc, pras);
//        }
//        catch (Exception e)
//        {
//            if (clsGlobalVarClass.gShowPrinterErrorMsg)
//            {
//                JOptionPane.showMessageDialog(null, e.getMessage(), "Error Code - TFG 01", JOptionPane.ERROR_MESSAGE);
//            }
//        }
//
//    }
//
//    class MyPrintJobListener implements PrintJobListener
//    {
//
//        public void printDataTransferCompleted(PrintJobEvent pje)
//        {
//            System.out.println("printDataTransferCompleted");
//        }
//
//        public void printJobCanceled(PrintJobEvent pje)
//        {
//            System.out.println("The print job was cancelled");
//        }
//
//        public void printJobCompleted(PrintJobEvent pje)
//        {
//            System.out.println("The print job was completed");
//        }
//
//        public void printJobFailed(PrintJobEvent pje)
//        {
//            System.out.println("The print job has failed");
//        }
//
//        public void printJobNoMoreEvents(PrintJobEvent pje)
//        {
//        }
//
//        public void printJobRequiresAttention(PrintJobEvent pje)
//        {
//        }
//    }
//
//    /*
//     * Text Bill format 15 is same as text bill format5 with Table No instead of
//     * Table Name and give space to enter customer name
//     */
//    public void funGenerateTextFileBillPrintingForFormat15(String billNo, String reprint, String formName, String transType, String billDate, String POSCode, String viewORprint)
//    {
//        clsUtility objUtility = new clsUtility();
//        clsUtility2 objUtility2 = new clsUtility2();
//
//        String Linefor5 = "  --------------------------------------";
//        try
//        {
//            String user = "";
//            String billhd = null;
//            String billdtl = null;
//            String billModifierdtl = null;
//            String billSettlementdtl = null;
//            String billtaxdtl = null;
//            String billDscFrom = null;
//            String billPromoDtl = null;
//            String advBookBillHd = null;
//            String advBookBillDtl = null;
//            String advBookBillCharDtl = null;
//            String advReceiptHd = null;
//
//            boolean isCustomerPrint = false;
//
//            if (clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("HOPOS"))
//            {
//                billhd = "tblqbillhd";
//                billdtl = "tblqbilldtl";
//                billModifierdtl = "tblqbillmodifierdtl";
//                billSettlementdtl = "tblqbillsettlementdtl";
//                billtaxdtl = "tblqbilltaxdtl";
//                billDscFrom = "tblqbilldiscdtl";
//                billPromoDtl = "tblqbillpromotiondtl";
//
//                advBookBillHd = "tblqadvbookbillhd";
//                advBookBillDtl = "tblqadvbookbilldtl";
//                advBookBillCharDtl = "tblqadvbookbillchardtl";
//                advReceiptHd = "tblqadvancereceipthd";
//            }
//            else
//            {
//                if ("sales report".equalsIgnoreCase(formName))
//                {
//                    billhd = "tblbillhd";
//                    billdtl = "tblbilldtl";
//                    billModifierdtl = "tblbillmodifierdtl";
//                    billSettlementdtl = "tblbillsettlementdtl";
//                    billtaxdtl = "tblbilltaxdtl";
//                    billDscFrom = "tblbilldiscdtl";
//                    billPromoDtl = "tblbillpromotiondtl";
//                    advBookBillHd = "tbladvbookbillhd";
//                    advBookBillDtl = "tbladvbookbilldtl";
//                    advBookBillCharDtl = "tbladvbookbillchardtl";
//                    advReceiptHd = "tbladvancereceipthd";
//                    long dateDiff = new clsUtility().funCompareDate(billDate, objUtility.funGetPOSDateForTransaction());
//                    if (dateDiff > 0)
//                    {
//                        billhd = "tblqbillhd";
//                        billdtl = "tblqbilldtl";
//                        billModifierdtl = "tblqbillmodifierdtl";
//                        billSettlementdtl = "tblqbillsettlementdtl";
//                        billtaxdtl = "tblqbilltaxdtl";
//                        billDscFrom = "tblqbilldiscdtl";
//                        billPromoDtl = "tblqbillpromotiondtl";
//                        advBookBillHd = "tblqadvbookbillhd";
//                        advBookBillDtl = "tblqadvbookbilldtl";
//                        advBookBillCharDtl = "tblqadvbookbillchardtl";
//                        advReceiptHd = "tblqadvancereceipthd";
//                    }
//                    String sql = "select count(strBillNo) from tblbillhd where strBillNo='" + billNo + "' and strPOSCode='" + POSCode + "' ";
//                    ResultSet rsBillTable = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//                    rsBillTable.next();
//                    int billCnt = rsBillTable.getInt(1);
//                    if (billCnt == 0)
//                    {
//                        billhd = "tblqbillhd";
//                        billdtl = "tblqbilldtl";
//                        billModifierdtl = "tblqbillmodifierdtl";
//                        billSettlementdtl = "tblqbillsettlementdtl";
//                        billtaxdtl = "tblqbilltaxdtl";
//                        billDscFrom = "tblqbilldiscdtl";
//                        billPromoDtl = "tblqbillpromotiondtl";
//                        advBookBillHd = "tblqadvbookbillhd";
//                        advBookBillDtl = "tblqadvbookbilldtl";
//                        advBookBillCharDtl = "tblqadvbookbillchardtl";
//                        advReceiptHd = "tblqadvancereceipthd";
//                    }
//                }
//                else
//                {
//                    billhd = "tblbillhd";
//                    billdtl = "tblbilldtl";
//                    billModifierdtl = "tblbillmodifierdtl";
//                    billSettlementdtl = "tblbillsettlementdtl";
//                    billtaxdtl = "tblbilltaxdtl";
//                    billDscFrom = "tblbilldiscdtl";
//                    billPromoDtl = "tblbillpromotiondtl";
//                    advBookBillHd = "tbladvbookbillhd";
//                    advBookBillDtl = "tbladvbookbilldtl";
//                    advBookBillCharDtl = "tbladvbookbillchardtl";
//                    advReceiptHd = "tbladvancereceipthd";
//                }
//            }
//            PreparedStatement pst = null;
//            objUtility2.funCreateTempFolder();
//            String filePath = System.getProperty("user.dir");
//            File Text_Bill = new File(filePath + "/Temp/Temp_Bill.txt");
//            String subTotal = "";
//            String grandTotal = "";
//            String advAmount = "";
//            String deliveryCharge = "";
//            String customerCode = "";
//            String waiterName = "";
//            String tblName = "";
//            ResultSet rs_BillHD = null;
//            boolean flgComplimentaryBill = false;
//            StringBuilder sqlBillHeaderDtl = new StringBuilder();
//            sqlBillHeaderDtl.append("select ifnull(a.strTableNo,''),ifnull(a.strWaiterNo,''),a.dteBillDate,time(a.dteBillDate),a.dblDiscountAmt,a.dblSubTotal,"
//                    + "ifnull(a.strCustomerCode,''),a.dblGrandTotal,a.dblTaxAmt,ifnull(a.strReasonCode,''),ifnull(a.strRemarks,''),a.strUserCreated "
//                    + ",ifnull(dblDeliveryCharges,0.00),ifnull(i.dblAdvDeposite,0.00),a.dblDiscountPer,b.strPOSName,a.intPaxNo "
//                    + ",ifnull(c.strTableName,''),ifnull(d.strWShortName,''),ifnull(d.strWFullName,''),ifnull(l.strSettelmentType,''),ifnull(j.strReasonName,'') as voidedReason, "
//                    + "ifnull(g.strReasonName,''),ifnull(e.strCustomerName,''),ifnull(a.strAdvBookingNo,''),ifnull(h.strMessage,''),ifnull(h.strShape,''),ifnull(h.strNote,''),ifnull(a.dblTipAmount,0.00) "
//                    + ",a.strOperationType,ifnull(a.strTakeAwayRemarks,''),ifnull(e.longMobileNo,'')  "
//                    + "from " + billhd + " a "
//                    + "left outer join tblposmaster b on a.strPOSCode=b.strPosCode  "
//                    + "left outer join tbltablemaster c on a.strTableNo=c.strTableNo and a.strClientCode=c.strClientCode "
//                    + "left outer join tblwaitermaster d on a.strWaiterNo=d.strWaiterNo and a.strClientCode=d.strClientCode "
//                    + "left outer join tblcustomermaster e on a.strCustomerCode=e.strCustomerCode and a.strClientCode=e.strClientCode "
//                    + "left outer join tbldebitcardmaster f on a.strCardNo=f.strCardNo "
//                    + "left outer join tblreasonmaster g on a.strReasonCode=g.strReasonCode "
//                    + "left outer join " + advBookBillHd + " h on a.strAdvBookingNo=h.strAdvBookingNo and a.strClientCode=h.strClientCode "
//                    + "left outer join " + advReceiptHd + " i on h.strAdvBookingNo=i.strAdvBookingNo and a.strClientCode=i.strClientCode "
//                    + "left outer join tblvoidbillhd j on a.strBillNo=j.strBillNo and a.strPOSCode=j.strPosCode and a.strClientCode=j.strClientCode "
//                    + "left outer join " + billSettlementdtl + " k on a.strBillNo=k.strBillNo and a.strClientCode=k.strClientCode "
//                    + "left outer join tblsettelmenthd l on k.strSettlementCode=l.strSettelmentCode "
//                    + "where a.strBillNo=? and a.strPOSCode=? "
//                    + "group by a.strBillNo; ");
//            pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlBillHeaderDtl.toString());
//            pst.setString(1, billNo);
//            pst.setString(2, POSCode);
//            rs_BillHD = pst.executeQuery();
//            rs_BillHD.next();
//            if (rs_BillHD.getString(21).equals("Complementary"))
//            {
//                flgComplimentaryBill = true;
//            }
//            FileWriter fstream_bill = new FileWriter(Text_Bill);
//            BufferedWriter BillOut = new BufferedWriter(fstream_bill);
//            if (clsGlobalVarClass.gClientCode.equals("117.001"))
//            {
//                if (POSCode.equals("P01"))
//                {
//                    objUtility2.funPrintBlankSpace("THE PREM'S HOTEL", BillOut);
//                    BillOut.write("THE PREM'S HOTEL");
//                    BillOut.newLine();
//                }
//                else if (POSCode.equals("P02"))
//                {
//                    objUtility2.funPrintBlankSpace("SWIG", BillOut);
//                    BillOut.write("SWIG");
//                    BillOut.newLine();
//                }
//            }
//            boolean isReprint = false;
//            if ("reprint".equalsIgnoreCase(reprint))
//            {
//                isReprint = true;
//                objUtility2.funPrintBlankSpace("[DUPLICATE]", BillOut);
//                BillOut.write("[DUPLICATE]");
//                BillOut.newLine();
//            }
//            if (transType.equals("Void"))
//            {
//                objUtility2.funPrintBlankSpace("VOIDED BILL", BillOut);
//                BillOut.write("VOIDED BILL");
//                BillOut.newLine();
//            }
//            boolean flag_isHomeDelvBill = false;
//            String SQL_HomeDelivery = "select strBillNo,strCustomerCode,strDPCode,tmeTime,strCustAddressLine1 "
//                    + "from tblhomedelivery where strBillNo=? ;";
//            pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(SQL_HomeDelivery);
//            pst.setString(1, billNo);
//            ResultSet rs_HomeDelivery = pst.executeQuery();
//            if (rs_HomeDelivery.next())
//            {
//                flag_isHomeDelvBill = true;
//                isCustomerPrint = true;
//
//                customerCode = rs_HomeDelivery.getString(2);
//                if (clsGlobalVarClass.gPrintHomeDeliveryYN)
//                {
//                    objUtility2.funPrintBlankSpace("HOME DELIVERY", BillOut);
//                    BillOut.write("HOME DELIVERY");
//                    BillOut.newLine();
//                }
//
//                String SQL_CustomerDtl = "";
//                if (rs_HomeDelivery.getString(5).equals("Temporary"))
//                {
//                    SQL_CustomerDtl = "select a.strCustomerName,a.strTempAddress,a.strTempStreet"
//                            + " ,a.strTempLandmark,a.strBuildingName,a.strCity,a.intPinCode,a.longMobileNo "
//                            + " from tblcustomermaster a left outer join tblbuildingmaster b "
//                            + " on a.strBuldingCode=b.strBuildingCode "
//                            + " where a.strCustomerCode=? ;";
//                }
//                else if (rs_HomeDelivery.getString(5).equals("Office"))
//                {
//                    SQL_CustomerDtl = "select a.strCustomerName,a.strOfficeBuildingName,a.strOfficeStreetName"
//                            + ",a.strOfficeLandmark,a.strOfficeArea,a.strOfficeCity,a.strOfficePinCode,a.longMobileNo "
//                            + " from tblcustomermaster a "
//                            + " where a.strCustomerCode=? ";
//                }
//                else
//                {
//                    SQL_CustomerDtl = "select a.strCustomerName,a.strCustAddress,a.strStreetName"
//                            + " ,a.strLandmark,a.strBuildingName,a.strCity,a.intPinCode,a.longMobileNo "
//                            + " from tblcustomermaster a left outer join tblbuildingmaster b "
//                            + " on a.strBuldingCode=b.strBuildingCode "
//                            + " where a.strCustomerCode=? ;";
//                }
//                pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(SQL_CustomerDtl);
//                pst.setString(1, rs_HomeDelivery.getString(2));
//                ResultSet rs_CustomerDtl = pst.executeQuery();
//                while (rs_CustomerDtl.next())
//                {
//                    BillOut.write("  NAME      :" + rs_CustomerDtl.getString(1).toUpperCase());
//                    BillOut.newLine();
//                    // Building Name    
//                    String add = rs_CustomerDtl.getString(2);
//                    int strlen = add.length();
//                    String add1 = "";
//                    if (strlen < 28)
//                    {
//                        add1 = add.substring(0, strlen);
//                        BillOut.write("  ADDRESS1  :" + add1.toUpperCase().replaceAll("\n", " "));
//                        BillOut.newLine();
//                    }
//                    else
//                    {
//                        add1 = add.substring(0, 28);
//                        BillOut.write("  ADDRESS1  :" + add1.toUpperCase().replaceAll("\n", " "));
//                        BillOut.newLine();
//                    }
//                    for (int i = 28; i <= strlen;)
//                    {
//                        int end = 0;
//                        end = i + 28;
//                        if (strlen > end)
//                        {
//                            add1 = add.substring(i, end);
//                            i = end;
//                            BillOut.write("             " + add1.toUpperCase().replaceAll("\n", " "));
//                            BillOut.newLine();
//                        }
//                        else
//                        {
//                            add1 = add.substring(i, strlen);
//                            BillOut.write("             " + add1.toUpperCase().replaceAll("\n", " "));
//                            BillOut.newLine();
//                            i = strlen + 1;
//                        }
//                    }
//                    // Street Name    
//                    String street = rs_CustomerDtl.getString(3);
//                    String street1;
//                    int streetlen = street.length();
//                    for (int i = 0; i <= streetlen;)
//                    {
//                        int end = 0;
//                        end = i + 28;
//                        if (streetlen > end)
//                        {
//                            street1 = street.substring(i, end);
//                            BillOut.write("             " + street1.toUpperCase());
//                            BillOut.newLine();
//                            i = end;
//                        }
//                        else
//                        {
//                            street1 = street.substring(i, streetlen);
//                            BillOut.write("             " + street1.toUpperCase());
//                            BillOut.newLine();
//                            i = streetlen + 1;
//                        }
//                    }
//                    // Landmark Name    
//                    if (rs_CustomerDtl.getString(4).trim().length() > 0)
//                    {
//                        BillOut.write("             " + rs_CustomerDtl.getString(4).toUpperCase());
//                        BillOut.newLine();
//                    }
//                    // Area Name    
//                    if (rs_CustomerDtl.getString(5).trim().length() > 0)
//                    {
//                        BillOut.write("             " + rs_CustomerDtl.getString(5).toUpperCase());
//                        BillOut.newLine();
//                    }
//                    // City Name    
//                    if (rs_CustomerDtl.getString(6).trim().length() > 0)
//                    {
//                        BillOut.write("             " + rs_CustomerDtl.getString(6).toUpperCase());
//                        BillOut.newLine();
//                    }
//                    // Pin Code    
//                    if (rs_CustomerDtl.getString(7).trim().length() > 0)
//                    {
//                        BillOut.write("             " + rs_CustomerDtl.getString(7).toUpperCase());
//                        BillOut.newLine();
//                    }
//                    // Mobile No    
//                    BillOut.write("  MOBILE NO :" + rs_CustomerDtl.getString(8));
//                    BillOut.newLine();
//                }
//                rs_CustomerDtl.close();
//                if (null != rs_HomeDelivery.getString(3) && rs_HomeDelivery.getString(3).trim().length() > 0)
//                {
//                    String[] delBoys = rs_HomeDelivery.getString(3).split(",");
//                    StringBuilder strIN = new StringBuilder("(");
//                    for (int i = 0; i < delBoys.length; i++)
//                    {
//                        if (i == 0)
//                        {
//                            strIN.append("'" + delBoys[i] + "'");
//                        }
//                        else
//                        {
//                            strIN.append(",'" + delBoys[i] + "'");
//                        }
//                    }
//                    strIN.append(")");
//                    String SQL_DeliveryBoyDtl = "select strDPName from tbldeliverypersonmaster where strDPCode IN " + strIN + " ;";
//                    pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(SQL_DeliveryBoyDtl);
//                    ResultSet rs_DeliveryBoyDtl = pst.executeQuery();
//                    strIN.setLength(0);
//                    for (int i = 0; rs_DeliveryBoyDtl.next(); i++)
//                    {
//                        if (i == 0)
//                        {
//                            strIN.append(rs_DeliveryBoyDtl.getString(1).toUpperCase());
//                        }
//                        else
//                        {
//                            strIN.append("," + rs_DeliveryBoyDtl.getString(1).toUpperCase());
//                        }
//                    }
//                    BillOut.write("  DELV BOY  :" + strIN);
//                    BillOut.newLine();
//                    rs_DeliveryBoyDtl.close();
//                }
//                BillOut.write(Line);
//                BillOut.newLine();
//            }
//            else
//            {
//                if (rs_BillHD.getString(7).length() > 0)//customerCode
//                {
//                    BillOut.write("  NAME      :" + rs_BillHD.getString(24).toUpperCase());
//                    BillOut.newLine();
//                    // Mobile No    
//                    BillOut.write("  MOBILE NO :" + rs_BillHD.getString(32));
//                    BillOut.newLine();
//
//                    isCustomerPrint = true;
//                }
//            }
//            rs_HomeDelivery.close();
//            //print take away
//            int billPrintSize = 4;
//            if (rs_BillHD.getString(30).equals("TakeAway"))
//            {
//                objUtility2.funPrintBlankSpace("Take Away", BillOut);
//                BillOut.write("Take Away");
//                BillOut.newLine();
//            }
//            if (clsGlobalVarClass.gPrintTaxInvoice.equalsIgnoreCase("Y"))
//            {
//                objUtility2.funPrintBlankSpace("TAX INVOICE", BillOut);
//                BillOut.write("TAX INVOICE");
//                BillOut.newLine();
//            }
//            if (clsGlobalVarClass.gClientCode.equals("047.001") && POSCode.equals("P03"))
//            {
//                objUtility2.funPrintBlankSpace("SHRI SHAM CATERERS", BillOut);
//                BillOut.write("SHRI SHAM CATERERS");
//                BillOut.newLine();
//                String cAddr1 = "Flat No.7, Mon Amour,";
//                objUtility2.funPrintBlankSpace(cAddr1, BillOut);
//                BillOut.write(cAddr1.toUpperCase());
//                BillOut.newLine();
//                String cAddr2 = "Thorat Colony,Prabhat Road,";
//                objUtility2.funPrintBlankSpace(cAddr2, BillOut);
//                BillOut.write(cAddr2.toUpperCase());
//                BillOut.newLine();
//                String cAddr3 = " Erandwane, Pune 411 004.";
//                objUtility2.funPrintBlankSpace(cAddr3, BillOut);
//                BillOut.write(cAddr3.toUpperCase());
//                BillOut.newLine();
//                String cAddr4 = "Approved Caterers of";
//                objUtility2.funPrintBlankSpace(cAddr4, BillOut);
//                BillOut.write(cAddr4.toUpperCase());
//                BillOut.newLine();
//                String cAddr5 = "ROYAL CONNAUGHT BOAT CLUB";
//                objUtility2.funPrintBlankSpace(cAddr5, BillOut);
//                BillOut.write(cAddr5.toUpperCase());
//                BillOut.newLine();
//            }
//            else if (clsGlobalVarClass.gClientCode.equals("047.001") && POSCode.equals("P02"))
//            {
//                objUtility2.funPrintBlankSpace("SHRI SHAM CATERERS", BillOut);
//                BillOut.write("SHRI SHAM CATERERS");
//                BillOut.newLine();
//                String cAddr1 = "Flat No.7, Mon Amour,";
//                objUtility2.funPrintBlankSpace(cAddr1, BillOut);
//                BillOut.write(cAddr1.toUpperCase());
//                BillOut.newLine();
//                String cAddr2 = "Thorat Colony,Prabhat Road,";
//                objUtility2.funPrintBlankSpace(cAddr2, BillOut);
//                BillOut.write(cAddr2.toUpperCase());
//                BillOut.newLine();
//                String cAddr3 = " Erandwane, Pune 411 004.";
//                objUtility2.funPrintBlankSpace(cAddr3, BillOut);
//                BillOut.write(cAddr3.toUpperCase());
//                BillOut.newLine();
//                String cAddr4 = "Approved Caterers of";
//                objUtility2.funPrintBlankSpace(cAddr4, BillOut);
//                BillOut.write(cAddr4.toUpperCase());
//                BillOut.newLine();
//                String cAddr5 = "ROYAL CONNAUGHT BOAT CLUB";
//                objUtility2.funPrintBlankSpace(cAddr5, BillOut);
//                BillOut.write(cAddr5.toUpperCase());
//                BillOut.newLine();
//            }
//            else if (clsGlobalVarClass.gClientCode.equals("092.001") || clsGlobalVarClass.gClientCode.equals("092.002") || clsGlobalVarClass.gClientCode.equals("092.003"))//Shree Sound Pvt. Ltd.
//            {
//                objUtility2.funPrintBlankSpace("SSPL", BillOut);
//                BillOut.write("SSPL");
//                BillOut.newLine();
//                objUtility2.funPrintBlankSpace(clsGlobalVarClass.gClientAddress1, BillOut);
//                BillOut.write(clsGlobalVarClass.gClientAddress1.toUpperCase());
//                BillOut.newLine();
//                if (clsGlobalVarClass.gClientAddress2.trim().length() > 0)
//                {
//                    objUtility2.funPrintBlankSpace(clsGlobalVarClass.gClientAddress2, BillOut);
//                    BillOut.write(clsGlobalVarClass.gClientAddress2.toUpperCase());
//                    BillOut.newLine();
//                }
//                if (clsGlobalVarClass.gClientAddress3.trim().length() > 0)
//                {
//                    objUtility2.funPrintBlankSpace(clsGlobalVarClass.gClientAddress3, BillOut);
//                    BillOut.write(clsGlobalVarClass.gClientAddress3.toUpperCase());
//                    BillOut.newLine();
//                }
//                if (clsGlobalVarClass.gCityName.trim().length() > 0)
//                {
//                    objUtility2.funPrintBlankSpace(clsGlobalVarClass.gCityName, BillOut);
//                    BillOut.write(clsGlobalVarClass.gCityName.toUpperCase());
//                    BillOut.newLine();
//                }
//            }
//            else if (clsGlobalVarClass.gClientCode.equals("092.001") || clsGlobalVarClass.gClientCode.equals("092.002") || clsGlobalVarClass.gClientCode.equals("092.003"))//Shree Sound Pvt. Ltd.
//            {
//                objUtility2.funPrintBlankSpace("SSPL", BillOut);
//                BillOut.write("SSPL");
//                BillOut.newLine();
//                objUtility2.funPrintBlankSpace(clsGlobalVarClass.gClientAddress1, BillOut);
//                BillOut.write(clsGlobalVarClass.gClientAddress1.toUpperCase());
//                BillOut.newLine();
//                if (clsGlobalVarClass.gClientAddress2.trim().length() > 0)
//                {
//                    objUtility2.funPrintBlankSpace(clsGlobalVarClass.gClientAddress2, BillOut);
//                    BillOut.write(clsGlobalVarClass.gClientAddress2.toUpperCase());
//                    BillOut.newLine();
//                }
//                if (clsGlobalVarClass.gClientAddress3.trim().length() > 0)
//                {
//                    objUtility2.funPrintBlankSpace(clsGlobalVarClass.gClientAddress3, BillOut);
//                    BillOut.write(clsGlobalVarClass.gClientAddress3.toUpperCase());
//                    BillOut.newLine();
//                }
//                if (clsGlobalVarClass.gCityName.trim().length() > 0)
//                {
//                    objUtility2.funPrintBlankSpace(clsGlobalVarClass.gCityName, BillOut);
//                    BillOut.write(clsGlobalVarClass.gCityName.toUpperCase());
//                    BillOut.newLine();
//                }
//            }
//            else
//            {
//                objUtility2.funPrintBlankSpace(clsGlobalVarClass.gClientName, BillOut);
//                if (clsGlobalVarClass.gClientCode.equals("124.001"))
//                {
//                    BillOut.write(clsGlobalVarClass.gClientName);
//                }
//                else
//                {
//                    BillOut.write(clsGlobalVarClass.gClientName.toUpperCase());
//                }
//                BillOut.newLine();
//                objUtility2.funPrintBlankSpace(clsGlobalVarClass.gClientAddress1, BillOut);
//                BillOut.write(clsGlobalVarClass.gClientAddress1.toUpperCase());
//                BillOut.newLine();
//                if (clsGlobalVarClass.gClientAddress2.trim().length() > 0)
//                {
//                    objUtility2.funPrintBlankSpace(clsGlobalVarClass.gClientAddress2, BillOut);
//                    BillOut.write(clsGlobalVarClass.gClientAddress2.toUpperCase());
//                    BillOut.newLine();
//                }
//                if (clsGlobalVarClass.gClientAddress3.trim().length() > 0)
//                {
//                    objUtility2.funPrintBlankSpace(clsGlobalVarClass.gClientAddress3, BillOut);
//                    BillOut.write(clsGlobalVarClass.gClientAddress3.toUpperCase());
//                    BillOut.newLine();
//                }
//                if (clsGlobalVarClass.gCityName.trim().length() > 0)
//                {
//                    objUtility2.funPrintBlankSpace(clsGlobalVarClass.gCityName, BillOut);
//                    BillOut.write(clsGlobalVarClass.gCityName.toUpperCase());
//                    BillOut.newLine();
//                }
//            }
//            BillOut.write("  TEL NO.   :" + " ");
//            BillOut.write(String.valueOf(clsGlobalVarClass.gClientTelNo));
//            BillOut.newLine();
//            BillOut.write("  EMAIL ID  :" + " ");
//            BillOut.write(clsGlobalVarClass.gClientEmail);
//            BillOut.newLine();
//            tblName = rs_BillHD.getString(18);
//            if (tblName.length() > 0)
//            {
//                if (clsGlobalVarClass.gClientCode.equalsIgnoreCase("136.001"))//KINKI
//                {
//                    BillOut.write("  TABLE No   :");
//                }
//                else
//                {
//                    BillOut.write("  TABLE No. :" + "  ");
//                }
//                BillOut.write(tblName);
//                BillOut.newLine();
//            }
//            waiterName = rs_BillHD.getString(19);
//            if (waiterName.trim().length() > 0)
//            {
//                BillOut.write("  STEWARD   :" + "  ");
//                BillOut.write(waiterName);
//                BillOut.newLine();
//            }
//            BillOut.write(Linefor5);
//            BillOut.newLine();
//            BillOut.write("  POS         : ");
//            BillOut.write(rs_BillHD.getString(16));
//            BillOut.newLine();
//            BillOut.write("  BILL NO.    : ");
//            BillOut.write(billNo);
//            BillOut.newLine();
//            BillOut.write("  PAX NO.     : ");
//            BillOut.write(rs_BillHD.getString(17));
//            BillOut.newLine();
//            if (clsGlobalVarClass.gPrintTimeOnBillYN)
//            {
//                BillOut.write("  DATE & TIME : ");
//                SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy hh:mm a ");
//                BillOut.write(ft.format(rs_BillHD.getObject(3)));
//                BillOut.newLine();
//
//            }
//            else
//            {
//                BillOut.write("  DATE        : ");
//                SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy");
//                BillOut.write(ft.format(rs_BillHD.getObject(3)));
//                BillOut.newLine();
//            }
//            if (rs_BillHD.getString(11).trim().length() > 0 && !flgComplimentaryBill)
//            {
//                BillOut.write("  Remarks     : ");
//                BillOut.write(rs_BillHD.getString(11));
//                BillOut.newLine();
//            }
//            subTotal = rs_BillHD.getString(6);
//            grandTotal = rs_BillHD.getString(8);
//            user = rs_BillHD.getString(12);
//            deliveryCharge = rs_BillHD.getString(13);
//            advAmount = rs_BillHD.getString(14);
//            //print card available balance
//            String isSttled = "select a.strBillNo from " + billSettlementdtl + " a," + billhd + " b "
//                    + " where a.strBillNo=b.strBillNo and a.strClientCode=b.strClientCode "
//                    + " and a.strBillNo='" + billNo + "' and b.strPOSCode='" + POSCode + "' ";
//            ResultSet rsIsSettled = clsGlobalVarClass.dbMysql.executeResultSet(isSttled);
//            if (rsIsSettled.next())
//            {
//                rsIsSettled.close();
//                String availBal = "select a.strCardNo,(b.dblRedeemAmt)"
//                        + "from " + billhd + " a inner join tbldebitcardmaster b on a.strCardNo=b.strCardNo "
//                        + "where a.strBillNo='" + billNo + "' and a.strPOSCode='" + POSCode + "'; ";
//                ResultSet rsAvailBal = clsGlobalVarClass.dbMysql.executeResultSet(availBal);
//                if (rsAvailBal.next())
//                {
//                    BillOut.write("  Available Balance(" + rsAvailBal.getString(1) + "):" + rsAvailBal.getString(2));
//                    BillOut.newLine();
//                }
//            }
//            else
//            {
//                String availBal = "select a.strCardNo,(b.dblRedeemAmt-a.dblGrandTotal)"
//                        + "from " + billhd + " a inner join tbldebitcardmaster b on a.strCardNo=b.strCardNo "
//                        + "where a.strBillNo='" + billNo + "' and a.strPOSCode='" + POSCode + "'; ";
//                ResultSet rsAvailBal = clsGlobalVarClass.dbMysql.executeResultSet(availBal);
//                if (rsAvailBal.next())
//                {
//                    BillOut.write("  Available Balance(" + rsAvailBal.getString(1) + "):" + rsAvailBal.getString(2));
//                    BillOut.newLine();
//                }
//            }
//            //print card available balance
//            if (transType.equals("Void"))
//            {
//                BillOut.write("  Reason      :" + " " + rs_BillHD.getString(22));//voided reason
//                BillOut.newLine();
//            }
//            else if (flgComplimentaryBill)
//            {
//
//                BillOut.write("  Reason      :" + " " + rs_BillHD.getString(23));
//                BillOut.newLine();
//                BillOut.write("  Remark      :" + " " + rs_BillHD.getString(11));
//                BillOut.newLine();
//            }
//            if (clsGlobalVarClass.gCMSIntegrationYN)
//            {
//                BillOut.write("  Member Code : ");
//                BillOut.write(rs_BillHD.getString(7));
//                BillOut.newLine();
//                BillOut.write("  Member Name : ");
//                objUtility2.funWriteToTextMemberNameForFormat5(BillOut, rs_BillHD.getString(24), "Format5");
//                BillOut.newLine();
//                BillOut.write(Linefor5);
//            }
//            if (rs_BillHD.getString(25) != null && rs_BillHD.getString(25).length() > 0)
//            {
//                if (rs_BillHD.getString(26).length() > 0 || rs_BillHD.getString(27).length() > 0 || rs_BillHD.getString(28).length() > 0)
//                {
//                    BillOut.newLine();
//                    objUtility2.funPrintBlankSpace("ORDER DETAIL", BillOut);
//                    BillOut.write("ORDER DETAIL");
//                    BillOut.newLine();
//                    BillOut.write(Linefor5);
//                    BillOut.newLine();
//                }
//                StringBuilder strValue = new StringBuilder();
//                strValue.setLength(0);
//                if (rs_BillHD.getString(26).length() > 0)
//                {
//                    strValue.append(rs_BillHD.getString(26));
//                }
//                else
//                {
//                    strValue.append("");
//                }
//                int strlenMsg = strValue.length();
//                if (strlenMsg > 0)
//                {
//                    String msg1 = "";
//                    if (strlenMsg < 27)
//                    {
//                        msg1 = strValue.substring(0, strlenMsg);
//                        BillOut.write("  MESSAGE     :" + msg1);
//                        BillOut.newLine();
//                    }
//                    else
//                    {
//                        msg1 = strValue.substring(0, 27);
//                        BillOut.write("  MESSAGE     :" + msg1);;
//                        BillOut.newLine();
//                    }
//                    for (int i = 27; i <= strlenMsg; i++)
//                    {
//                        int endmsg = 0;
//                        endmsg = i + 27;
//                        if (strlenMsg > endmsg)
//                        {
//                            msg1 = strValue.substring(i, endmsg);
//                            i = endmsg;
//                            BillOut.write("               " + msg1);
//                            BillOut.newLine();
//                        }
//                        else
//                        {
//                            msg1 = strValue.substring(i, strlenMsg);
//                            BillOut.write("               " + msg1);
//                            BillOut.newLine();
//                            i = strlenMsg + 1;
//                        }
//                    }
//                }
//                strValue.setLength(0);
//                if (rs_BillHD.getString(27).length() > 0)//shape
//                {
//                    strValue.append(rs_BillHD.getString(27));
//                }
//                else
//                {
//                    strValue.append("");
//                }
//                int strlenShape = strValue.length();
//                if (strlenShape > 0)
//                {
//                    String shape1 = "";
//                    if (strlenShape < 27)
//                    {
//                        shape1 = strValue.substring(0, strlenShape);
//                        BillOut.write("  SHAPE       :" + shape1);
//                        BillOut.newLine();
//                    }
//                    else
//                    {
//                        shape1 = strValue.substring(0, 27);
//                        BillOut.write("  SHAPE       :" + shape1);
//                        BillOut.newLine();
//                    }
//                    for (int j = 27; j <= strlenShape; j++)
//                    {
//                        int endShape = 0;
//                        endShape = j + 27;
//                        if (strlenShape > endShape)
//                        {
//                            shape1 = strValue.substring(j, endShape);
//                            j = endShape;
//                            BillOut.write("               " + shape1);
//                            BillOut.newLine();
//                        }
//                        else
//                        {
//                            shape1 = strValue.substring(j, strlenShape);
//                            BillOut.write("               " + shape1);
//                            BillOut.newLine();
//                            j = strlenShape + 1;
//                        }
//                    }
//                }
//
//                strValue.setLength(0);
//                if (rs_BillHD.getString(28).length() > 0)//note
//                {
//                    strValue.append(rs_BillHD.getString(28));
//                }
//                else
//                {
//                    strValue.append("");
//                }
//                int strlenNote = strValue.length();
//                if (strlenNote > 0)
//                {
//                    String note1 = "";
//                    if (strlenNote < 27)
//                    {
//                        note1 = strValue.substring(0, strlenNote);
//                        BillOut.write("  NOTE        :" + note1);
//                        BillOut.newLine();
//                    }
//                    else
//                    {
//                        note1 = strValue.substring(0, 27);
//                        BillOut.write("  NOTE        :" + note1);
//                        BillOut.newLine();
//                    }
//                    for (int i = 27; i <= strlenNote; i++)
//                    {
//                        int endNote = 0;
//                        endNote = i + 27;
//                        if (strlenNote > endNote)
//                        {
//                            note1 = strValue.substring(i, endNote);
//                            i = endNote;
//                            BillOut.write("               " + note1);
//                            BillOut.newLine();
//                        }
//                        else
//                        {
//                            note1 = strValue.substring(i, strlenNote);
//                            BillOut.write("               " + note1);
//                            BillOut.newLine();
//                            i = strlenNote + 1;
//                        }
//                    }
//                }
//                if (rs_BillHD.getString(26).length() > 0 || rs_BillHD.getString(27).length() > 0 || rs_BillHD.getString(28).length() > 0)
//                {
//
//                    BillOut.write(Linefor5);
//                    BillOut.newLine();
//                }
//            }
//
//            if (!isCustomerPrint)
//            {
//                BillOut.write("  NAME        :-------------------------");//25 dashes
//                BillOut.newLine();
//                BillOut.write("  Address     :-------------------------");//25 dashes
//                BillOut.newLine();
////                BillOut.write("            --------------------------");//25 dashes
////                BillOut.newLine();
//
//            }
//
//            BillOut.write(Linefor5);
//            BillOut.newLine();
//            BillOut.write("     QTY ITEM NAME                  AMT");
//            BillOut.newLine();
//            BillOut.write(Linefor5);
//            BillOut.newLine();
//            String SQL_BillDtl = "select sum(a.dblQuantity),left(a.strItemName,22) as ItemLine1"
//                    + " ,MID(a.strItemName,23,LENGTH(a.strItemName)) as ItemLine2"
//                    + " ,sum(a.dblAmount),a.strItemCode,a.strKOTNo "
//                    + " from " + billdtl + " a," + billhd + " b "
//                    + " where a.strBillNo=b.strBillNo and a.strClientCode=b.strClientCode and a.strBillNo=? and b.strPOSCode=?  ";
//            if (!clsGlobalVarClass.gPrintTDHItemsInBill)
//            {
//                SQL_BillDtl += "and a.tdhYN='N' ";
//            }
//            if (!clsGlobalVarClass.gPrintOpenItemsOnBill)
//            {
//                SQL_BillDtl += "and a.dblAmount>0 ";
//            }
//            SQL_BillDtl += " group by a.strItemCode ";
//            pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(SQL_BillDtl);
//            pst.setString(1, billNo);
//            pst.setString(2, POSCode);
//            ResultSet rs_BillDtl = pst.executeQuery();
//            while (rs_BillDtl.next())
//            {
//                double saleQty = rs_BillDtl.getDouble(1);
//                String sqlPromoBills = "select dblQuantity from " + billPromoDtl + " "
//                        + " where strBillNo='" + billNo + "' and strItemCode='" + rs_BillDtl.getString(5) + "' "
//                        + " and strPromoType='ItemWise' ";
//                ResultSet rsPromoItems = clsGlobalVarClass.dbMysql.executeResultSet(sqlPromoBills);
//                if (rsPromoItems.next())
//                {
//                    saleQty -= rsPromoItems.getDouble(1);
//                }
//                rsPromoItems.close();
//                String qty = String.valueOf(saleQty);
//                if (qty.contains("."))
//                {
//                    String decVal = qty.substring(qty.length() - 2, qty.length());
//                    if (Double.parseDouble(decVal) == 0)
//                    {
//                        qty = qty.substring(0, qty.length() - 2);
//                    }
//                }
//                if (saleQty > 0)
//                {
//                    objUtility2.funPrintContentWithSpace("Right", qty, 8, BillOut);//Qty Print
//                    BillOut.write(" ");
//                    objUtility2.funPrintContentWithSpace("Left", rs_BillDtl.getString(2), 22, BillOut);//Item Name
//                    if (flgComplimentaryBill)
//                    {
//                        objUtility2.funPrintContentWithSpace("Right", "0.00", 9, BillOut);//Amount
//                    }
//                    else
//                    {
//                        objUtility2.funPrintContentWithSpace("Right", rs_BillDtl.getString(4), 9, BillOut);//Amount
//                    }
//                    BillOut.newLine();
//                    if (rs_BillDtl.getString(3).trim().length() > 0)
//                    {
//                        String line = rs_BillDtl.getString(3);
//                        if (line.length() > 22)
//                        {
//                            BillOut.write("         " + line.substring(0, 22));
//                            BillOut.newLine();
//
//                            BillOut.write("         " + line.substring(22, line.length()));
//                            BillOut.newLine();
//                        }
//                        else
//                        {
//                            BillOut.write("         " + line);
//                            BillOut.newLine();
//                        }
//                    }
//                    String sqlModifier = "select count(*) "
//                            + "from " + billModifierdtl + " where strBillNo=? and left(strItemCode,7)=? ";
//                    if (!clsGlobalVarClass.gPrintZeroAmtModifierOnBill)
//                    {
//                        sqlModifier += " and  dblAmount !=0.00 ";
//                    }
//                    pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlModifier);
//                    pst.setString(1, billNo);
//                    pst.setString(2, rs_BillDtl.getString(5));
//                    ResultSet rs_count = pst.executeQuery();
//                    rs_count.next();
//                    int cntRecord = rs_count.getInt(1);
//                    rs_count.close();
//                    if (cntRecord > 0)
//                    {
//                        sqlModifier = "select strModifierName,dblQuantity,dblAmount "
//                                + " from " + billModifierdtl + " "
//                                + " where strBillNo=? and left(strItemCode,7)=? ";
//                        if (!clsGlobalVarClass.gPrintZeroAmtModifierOnBill)
//                        {
//                            sqlModifier += " and  dblAmount !=0.00 ";
//                        }
//                        pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlModifier);
//                        pst.setString(1, billNo);
//                        pst.setString(2, rs_BillDtl.getString(5));
//                        ResultSet rs_modifierRecord = pst.executeQuery();
//                        while (rs_modifierRecord.next())
//                        {
//                            if (flgComplimentaryBill)
//                            {
//                                objUtility2.funWriteToTextformat5(BillOut, "", rs_modifierRecord.getString(1).toUpperCase(), "0.00", "Format5");
//                                BillOut.newLine();
//                            }
//                            else
//                            {
//                                objUtility2.funWriteToTextformat5(BillOut, "", rs_modifierRecord.getString(1).toUpperCase(), rs_modifierRecord.getString(3), "Format5");
//                                BillOut.newLine();
//                            }
//                        }
//                        rs_modifierRecord.close();
//                    }
//
//                    sql = "select b.strItemCode,b.dblWeight "
//                            + " from " + billhd + " a," + advBookBillDtl + " b "
//                            + " where a.strAdvBookingNo=b.strAdvBookingNo and a.strClientCode=b.strClientCode "
//                            + " and a.strBillNo='" + billNo + "' and b.strItemCode='" + rs_BillDtl.getString(5) + "' "
//                            + " and a.strPOSCode='" + POSCode + "' ";
//                    ResultSet rsWeight = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//                    while (rsWeight.next())
//                    {
//                        BillOut.write("     Weight");
//                        BillOut.write("     " + rsWeight.getDouble(2));
//                        BillOut.newLine();
//                    }
//                    rsWeight.close();
//                    sql = "select c.strCharName,b.strCharValues "
//                            + " from " + billhd + " a," + advBookBillCharDtl + " b,tblcharactersticsmaster c "
//                            + " where a.strAdvBookingNo=b.strAdvBookingNo and b.strCharCode=c.strCharCode "
//                            + " and a.strBillNo='" + billNo + "' and b.strItemCode='" + rs_BillDtl.getString(5) + "' "
//                            + " and a.strPOSCode='" + POSCode + "' and a.strClientCode=b.strClientCode ";
//                    ResultSet rsCharDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//                    while (rsCharDtl.next())
//                    {
//                        String charName = objUtility.funPrintTextWithAlignment(rsCharDtl.getString(1), 12, "Left");
//                        BillOut.write("     " + charName);
//                        String charVal = objUtility.funPrintTextWithAlignment(rsCharDtl.getString(2), 28, "Left");
//                        BillOut.write("     " + charVal);
//                        BillOut.newLine();
//                    }
//                    rsCharDtl.close();
//                }
//            }
//            rs_BillDtl.close();
//            funPrintPromoItemsInBill(billNo, BillOut, 4);  // Print Promotion Items in Bill for this billno.
//            BillOut.write(Linefor5);
//            BillOut.newLine();
//            if (clsGlobalVarClass.gPointsOnBillPrint)
//            {
//                String sqlCRMPoints = "select b.dblPoints from " + billhd + " a, tblcrmpoints b "
//                        + " where a.strBillNo=b.strBillNo and a.strClientCode=b.strClientCode "
//                        + " and a.strBillNo='" + billNo + "' and a.strPOSCode='" + POSCode + "' ";
//                ResultSet rsCRMPoints = clsGlobalVarClass.dbMysql.executeResultSet(sqlCRMPoints);
//                if (rsCRMPoints.next())
//                {
//                    funWriteTotal("POINTS ", rsCRMPoints.getString(1), BillOut, "Format5");
//                }
//                rsCRMPoints.close();
//                BillOut.newLine();
//            }
//            if (flgComplimentaryBill)
//            {
//                funWriteTotal("SUB TOTAL", "0.00", BillOut, "Format5");
//                BillOut.newLine();
//            }
//            else
//            {
//                funWriteTotal("SUB TOTAL", subTotal, BillOut, "Format5");
//                BillOut.newLine();
//            }
//            sql = "select a.dblDiscPer,a.dblDiscAmt,a.strDiscOnType,a.strDiscOnValue,b.strReasonName,a.strDiscRemarks "
//                    + " from " + billDscFrom + " a ,tblreasonmaster b," + billhd + " c "
//                    + " where  a.strDiscReasonCode=b.strReasonCode and a.strBillNo=c.strBillNo "
//                    + " and a.strClientCode=c.strClientCode and a.strBillNo='" + billNo + "' and c.strPOSCode='" + POSCode + "' ";
//            ResultSet rsDisc = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//            boolean flag = true;
//            while (rsDisc.next())
//            {
//                if (flag)
//                {
//                    flag = false;
//                    BillOut.write("  DISCOUNT");
//                    BillOut.newLine();
//                }
//                double dbl = Double.parseDouble(rsDisc.getString("dblDiscPer"));
//                String discText = String.format("%.1f", dbl) + "%" + " On " + rsDisc.getString("strDiscOnValue") + "";
//                if (discText.length() > 30)
//                {
//                    discText = discText.substring(0, 30);
//                }
//                else
//                {
//                    discText = String.format("%-30s", discText);
//                }
//                BillOut.write("  " + discText);
//                String discountOnItem = objUtility.funPrintTextWithAlignment(rsDisc.getString("dblDiscAmt"), 8, "Right");
//                BillOut.write(discountOnItem);
//                BillOut.newLine();
//                BillOut.write("  Reason  : ");
//                String discReason = objUtility.funPrintTextWithAlignment(rsDisc.getString(5), 20, "Left");
//                BillOut.write(discReason);
//                BillOut.newLine();
//                BillOut.write("  Remarks : ");
//                String discRemarks = objUtility.funPrintTextWithAlignment(rsDisc.getString(6), 20, "Left");
//                BillOut.write(discRemarks);
//                BillOut.newLine();
//
//            }
//            String sql_Tax = "select b.strTaxDesc,sum(a.dblTaxAmount) "
//                    + " from " + billtaxdtl + " a,tbltaxhd b," + billhd + " c "
//                    + " where a.strBillNo='" + billNo + "' "
//                    + " and a.strTaxCode=b.strTaxCode "
//                    + " and a.strBillNo=c.strBillNo "
//                    + " and a.strClientCode=c.strClientCode "
//                    + " and c.strPOSCode='" + POSCode + "' "
//                    + " and b.strTaxCalculation='Forward' "
//                    + " group by a.strTaxCode";
//            ResultSet rsTax = clsGlobalVarClass.dbMysql.executeResultSet(sql_Tax);
//            while (rsTax.next())
//            {
//                if (flgComplimentaryBill)
//                {
//                    funWriteTotal(rsTax.getString(1), "0.00", BillOut, "Format5");
//                    BillOut.newLine();
//                }
//                else
//                {
//                    funWriteTotal(rsTax.getString(1), rsTax.getString(2), BillOut, "Format5");
//                    BillOut.newLine();
//                }
//            }
//            if (deliveryCharge != null && deliveryCharge.trim().length() > 0 && !"0.00".equalsIgnoreCase(deliveryCharge))
//            {
//                funWriteTotal("DELV. CHARGE", deliveryCharge, BillOut, "Format5");
//                BillOut.newLine();
//            }
//            if (advAmount.trim().length() > 0 && !"0.00".equalsIgnoreCase(advAmount))
//            {
//                funWriteTotal("ADVANCE", advAmount, BillOut, "Format5");
//                BillOut.newLine();
//            }
//            BillOut.write(Linefor5);
//            BillOut.newLine();
//            if (flgComplimentaryBill)
//            {
//                funWriteTotal("TOTAL(ROUNDED)", "0.00", BillOut, "Format5");
//                BillOut.newLine();
//                BillOut.write(Linefor5);
//            }
//            else
//            {
//                funWriteTotal("TOTAL(ROUNDED)", grandTotal, BillOut, "Format5");
//                BillOut.newLine();
//                BillOut.write(Linefor5);
//            }
//
//            //print Grand total of other bill nos from bill series
//            if (clsGlobalVarClass.gEnableBillSeries)
//            {
//                String sqlPrintGT = "select a.strPrintGTOfOtherBills,b.strDtlBillNos,b.dblGrandTotal "
//                        + "from tblbillseries a,tblbillseriesbilldtl b "
//                        + "where (a.strPOSCode=b.strPOSCode or a.strPOSCode='All') "
//                        + "and a.strBillSeries=b.strBillSeries "
//                        + "and b.strHdBillNo='" + billNo + "' and b.strPOSCode='" + POSCode + "' ";
//                ResultSet rsPrintGTYN = clsGlobalVarClass.dbMysql.executeResultSet(sqlPrintGT);
//                double dblOtherBillsGT = 0.00;
//                if (rsPrintGTYN.next())
//                {
//                    if (rsPrintGTYN.getString(1).equalsIgnoreCase("Y"))
//                    {
//                        String billSeriesDtlBillNos = rsPrintGTYN.getString(2);
//                        String[] dtlBillSeriesBillNo = billSeriesDtlBillNos.split(",");
//                        dblOtherBillsGT += rsPrintGTYN.getDouble(3);
//                        if (dtlBillSeriesBillNo.length > 0)
//                        {
//                            for (int i = 0; i < dtlBillSeriesBillNo.length; i++)
//                            {
//                                sqlPrintGT = "select a.strHdBillNo,a.dblGrandTotal "
//                                        + "from tblbillseriesbilldtl a "
//                                        + "where a.strHdBillNo='" + dtlBillSeriesBillNo[i] + "' and a.strPOSCode='" + POSCode + "' ";
//                                ResultSet rsPrintGT = clsGlobalVarClass.dbMysql.executeResultSet(sqlPrintGT);
//                                if (rsPrintGT.next())
//                                {
//                                    BillOut.newLine();
//                                    funWriteTotal(dtlBillSeriesBillNo[i] + " TOTAL(ROUNDED)", rsPrintGT.getString(2), BillOut, "Format5");
//                                    dblOtherBillsGT += rsPrintGT.getDouble(2);
//                                    BillOut.newLine();
//                                }
//                            }
//                            BillOut.newLine();
//                            BillOut.write(Linefor5);
//                            BillOut.newLine();
//                            funWriteTotal("GRAND TOTAL(ROUNDED)", String.valueOf(dblOtherBillsGT), BillOut, "Format5");
//                            BillOut.newLine();
//                            BillOut.write(Linefor5);
//                            BillOut.newLine();
//                        }
//                    }
//                }
//            }
//
//            //settlement breakup part
//            String sqlSettlementBreakup = "select a.dblSettlementAmt, b.strSettelmentDesc, b.strSettelmentType "
//                    + " from " + billSettlementdtl + " a ,tblsettelmenthd b," + billhd + " c "
//                    + " where a.strBillNo=? and a.strBillNo=c.strBillNo and a.strClientCode=c.strClientCode "
//                    + " and a.strSettlementCode=b.strSettelmentCode and c.strPOSCode=? ";
//            pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlSettlementBreakup);
//            pst.setString(1, billNo);
//            pst.setString(2, POSCode);
//            boolean flgSettlement = false;
//            boolean creditSettlement = false;
//            ResultSet rsBillSettlement = pst.executeQuery();
//            while (rsBillSettlement.next())
//            {
//                if (flgComplimentaryBill)
//                {
//                    BillOut.newLine();
//                    funWriteTotal(rsBillSettlement.getString(2), "0.00", BillOut, "Format5");
//                }
//                else
//                {
//                    BillOut.newLine();
//                    funWriteTotal(rsBillSettlement.getString(2), rsBillSettlement.getString(1), BillOut, "Format5");
//                }
//                flgSettlement = true;
//                if (rsBillSettlement.getString(3).equals("Credit"))
//                {
//                    creditSettlement = true;
//                }
//            }
//            rsBillSettlement.close();
//
//            if (flgSettlement)
//            {
//                BillOut.newLine();
//                if (creditSettlement)
//                {
//                    funWriteTotal("Credit Remarks ", rs_BillHD.getString(11), BillOut, "Format5");
//                    BillOut.newLine();
//                    String custName = rs_BillHD.getString(24);
//                    if (!custName.isEmpty())
//                    {
//                        funWriteTotal("Customer " + custName, "", BillOut, "Format5");
//                    }
//                    BillOut.newLine();
//                    BillOut.write(Linefor5);
//                }
//            }
//
//            String sqlTenderAmt = "select sum(a.dblPaidAmt),sum(a.dblSettlementAmt),(sum(a.dblPaidAmt)-sum(a.dblSettlementAmt)) RefundAmt "
//                    + " from " + billSettlementdtl + " a," + billhd + " b "
//                    + " where a.strBillNo=b.strBillNo and a.strClientCode=b.strClientCode "
//                    + " and b.strBillNo='" + billNo + "' and b.strPOSCode='" + POSCode + "' "
//                    + " group by a.strBillNo";
//            ResultSet rsTenderAmt = clsGlobalVarClass.dbMysql.executeResultSet(sqlTenderAmt);
//            if (rsTenderAmt.next())
//            {
//                BillOut.newLine();
//                if (flgComplimentaryBill)
//                {
//                    funWriteTotal("PAID AMT", "0.00", BillOut, "Format5");
//                    BillOut.newLine();
//                }
//                else
//                {
//                    funWriteTotal("PAID AMT", rsTenderAmt.getString(1), BillOut, "Format5");
//                    BillOut.newLine();
//                    if (rsTenderAmt.getDouble(3) > 0)
//                    {
//                        funWriteTotal("REFUND AMT", rsTenderAmt.getString(3), BillOut, "Format5");
//                        BillOut.newLine();
//                    }
//                }
//                BillOut.write(Linefor5);
//            }
//            rsTenderAmt.close();
//
//            if (rs_BillHD.getDouble(29) > 0)
//            {
//                BillOut.newLine();
//                funWriteTotal("TIP AMT", rs_BillHD.getString(29), BillOut, "Format5");
//                BillOut.newLine();
//            }
//            if (flag_isHomeDelvBill)
//            {
//                BillOut.newLine();
//                String sql_count = "select count(*) from tblhomedelivery where strCustomerCode=?";
//                pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sql_count);
//                pst.setString(1, customerCode);
//                ResultSet rs_Count = pst.executeQuery();
//                rs_Count.next();
//                BillOut.write("  CUSTOMER COUNT : " + rs_Count.getString(1));
//                rs_Count.close();
//                BillOut.newLine();
//                BillOut.write(Linefor5);
//            }
//            BillOut.newLine();
//
//            funPrintServiceVatNo(BillOut, 4, billNo, billDate, billtaxdtl);
//
//            if (clsGlobalVarClass.gEnableBillSeries)
//            {
//                sql = "select b.strPrintInclusiveOfTaxOnBill "
//                        + " from tblbillseriesbilldtl a,tblbillseries b "
//                        + " where a.strBillSeries=b.strBillSeries and a.strHdBillNo='" + billNo + "' and a.strClientCode=b.strClientCode";
//                ResultSet rsBillSeries = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//                if (rsBillSeries.next())
//                {
//                    if (rsBillSeries.getString(1).equals("Y"))
//                    {
//                        BillOut.write(Line);
//                        BillOut.newLine();
//                        objUtility2.funPrintBlankSpace("(INCLUSIVE OF ALL TAXES)", BillOut);
//                        BillOut.write("(INCLUSIVE OF ALL TAXES)");
//                        BillOut.newLine();
//                    }
//                }
//                rsBillSeries.close();
//            }
//            else
//            {
//                if (clsGlobalVarClass.gPrintInclusiveOfAllTaxes.equalsIgnoreCase("Y"))
//                {
//                    BillOut.write(Line);
//                    BillOut.newLine();
//                    objUtility2.funPrintBlankSpace("(INCLUSIVE OF ALL TAXES)", BillOut);
//                    BillOut.write("(INCLUSIVE OF ALL TAXES)");
//                    BillOut.newLine();
//                }
//            }
//
//            int num = clsGlobalVarClass.gBillFooter.trim().length() / 30;
//            int num1 = clsGlobalVarClass.gBillFooter.trim().length() % 30;
//            int cnt1 = 0;
//            for (int cnt = 0; cnt < num; cnt++)
//            {
//                String footer = clsGlobalVarClass.gBillFooter.trim().substring(cnt1, (cnt1 + 30));
//                footer = footer.replaceAll("\n", "");
//                BillOut.write("     " + footer.trim());
//                BillOut.newLine();
//                cnt1 += 30;
//            }
//            BillOut.write("     " + clsGlobalVarClass.gBillFooter.trim().substring(cnt1, (cnt1 + num1)).trim());
//            BillOut.newLine();
//            objUtility2.funPrintBlankSpace(user, BillOut);
//            BillOut.write(user);
//            BillOut.newLine();
//            BillOut.newLine();
//            BillOut.newLine();
//            BillOut.newLine();
//            BillOut.newLine();
//
//            if (!clsGlobalVarClass.gOpenCashDrawerAfterBillPrintYN)
//            {
//                if ("linux".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
//                {
//                    BillOut.write("V");//Linux
//                }
//                else if ("windows".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
//                {
//                    if ("Inbuild".equalsIgnoreCase(clsPosConfigFile.gPrinterType))
//                    {
//                        BillOut.write("V");
//                    }
//                    else
//                    {
//                        BillOut.write("m");//windows
//                    }
//                }
//            }
//            rs_BillHD.close();
//            BillOut.close();
//            fstream_bill.close();
//            pst.close();
//
//            if (formName.equalsIgnoreCase("sales report"))
//            {
//                objUtility2.funShowTextFile(Text_Bill, formName, clsGlobalVarClass.gBillPrintPrinterPort);
//            }
//            else
//            {
//                if (clsGlobalVarClass.gShowBill)
//                {
//                    objUtility2.funShowTextFile(Text_Bill, formName, clsGlobalVarClass.gBillPrintPrinterPort);
//                }
//            }
//
//            if (!formName.equalsIgnoreCase("sales report"))
//            {
//                if (transType.equalsIgnoreCase("void"))
//                {
//                    if (clsGlobalVarClass.gPrintOnVoidBill)
//                    {
//                        if (!viewORprint.equalsIgnoreCase("view"))
//                        {
//                            funPrintToPrinter(clsGlobalVarClass.gBillPrintPrinterPort, "", "bill", "N", isReprint);
//                        }
//                    }
//                }
//                else
//                {
//                    if (!clsGlobalVarClass.flgReprintView)
//                    {
//                        if (!viewORprint.equalsIgnoreCase("view"))
//                        {
//                            funPrintToPrinter(clsGlobalVarClass.gBillPrintPrinterPort, "", "bill", "N", isReprint);
//                        }
//                    }
//                    else
//                    {
//                        clsGlobalVarClass.flgReprintView = false;
//                    }
//                }
//            }
//            //if (formName.equalsIgnoreCase("sales report"))
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    private int funPrintPromoItemsInBill(String billNo, BufferedWriter objBillOut, int billPrintSize) throws Exception
//    {
//        clsUtility objUtility = new clsUtility();
//        clsUtility2 objUtility2 = new clsUtility2();
//        String sqlBillPromoDtl = "select b.strItemName,a.dblQuantity,'0',dblRate "
//                + " from tblbillpromotiondtl a,tblitemmaster b "
//                + " where a.strItemCode=b.strItemCode and a.strBillNo='" + billNo + "' and a.strPromoType!='Discount' ";
//        ResultSet rsBillPromoItemDtl = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillPromoDtl);
//        while (rsBillPromoItemDtl.next())
//        {
//            if (billPrintSize == 4)
//            {
//                //funWriteToText(objBillOut, funReduceTo2DecimalPlaces(rsBillPromoItemDtl.getString(2)), rsBillPromoItemDtl.getString(1).toUpperCase(), "0", "Format1");
//                String qty = rsBillPromoItemDtl.getString(2);
//                if (qty.contains("."))
//                {
//                    String decVal = qty.substring(qty.length() - 1, qty.length());
//                    if (Double.parseDouble(decVal) == 0)
//                    {
//                        qty = qty.substring(0, qty.length() - 3);
//                    }
//                }
//                objBillOut.write(" ");
//                objBillOut.write(objUtility.funPrintTextWithAlignment(qty, 7, "Right"));
//                objBillOut.write(" ");
//                objBillOut.write(objUtility.funPrintTextWithAlignment(rsBillPromoItemDtl.getString(1).toUpperCase(), 20, "Left"));
//                objBillOut.write(objUtility.funPrintTextWithAlignment("0.00", 11, "Right"));
//
//                objBillOut.newLine();
//            }
//            else if (billPrintSize == 2)
//            {
//                if (rsBillPromoItemDtl.getString(1).toUpperCase().length() > 20)
//                {
//                    List listTextToPrint = funGetTextWithSpecifiedSize(rsBillPromoItemDtl.getString(1).toUpperCase(), 2);
//                    for (int cnt = 0; cnt < listTextToPrint.size(); cnt++)
//                    {
//                        objBillOut.write(objUtility.funPrintTextWithAlignment(listTextToPrint.get(cnt).toString(), 20, "Left"));
//                        objBillOut.newLine();
//                    }
//                }
//                else
//                {
//                    objBillOut.write(objUtility.funPrintTextWithAlignment(rsBillPromoItemDtl.getString(1).toUpperCase(), 20, "Left"));
//                    objBillOut.newLine();
//                }
//                //objBillOut.write(new clsUtility().funPrintTextWithAlignment(funReduceTo2DecimalPlaces(rsBillPromoItemDtl.getString(2)), 6, "Right"));
//                objBillOut.write(new clsUtility().funPrintTextWithAlignment(rsBillPromoItemDtl.getString(2), 6, "Right"));
//                objBillOut.write(new clsUtility().funPrintTextWithAlignment("  ", 7, "Right"));
//                objBillOut.write(new clsUtility().funPrintTextWithAlignment("0", 7, "Right"));
//                objBillOut.newLine();
//            }
//        }
//        rsBillPromoItemDtl.close();
//        objUtility = null;
//        return 1;
//    }
//
//    private List funGetTextWithSpecifiedSize(String text, int size)
//    {
//        List listText = new ArrayList();
//        try
//        {
//            System.out.println(text);
//            if (size == 2)
//            {
//                StringBuilder sbText = new StringBuilder();
//                StringBuilder sbTempText = new StringBuilder();
//                String[] arrTextToPrint = text.split(" ");
//                for (int cnt = 0; cnt < arrTextToPrint.length; cnt++)
//                {
//                    sbTempText.append(arrTextToPrint[cnt] + " ");
//                    if (sbTempText.length() > 20)
//                    {
//                        String tempText = sbText.substring(0, sbText.lastIndexOf(" "));
//                        System.out.println("Add To List " + tempText);
//                        if (!tempText.isEmpty())
//                        {
//                            listText.add(tempText);
//                        }
//                        sbText.setLength(0);
//                        sbTempText.setLength(0);
//
//                        sbTempText.append(arrTextToPrint[cnt] + " ");
//                        sbText.append(arrTextToPrint[cnt] + " ");
//                    }
//                    else
//                    {
//                        sbText.append(arrTextToPrint[cnt] + " ");
//                    }
//
//                    if ((cnt == arrTextToPrint.length - 1) && sbTempText.length() > 0)
//                    {
//                        listText.add(sbText);
//                    }
//                }
//            }
//            System.out.println("List= " + listText);
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        return listText;
//    }
//
//    public void funPrintServiceVatNo(BufferedWriter objBillOut, int billPrinteSize, String billNo, String billDate, String billTaxDtl) throws IOException
//    {
//        clsUtility objUtility = new clsUtility();
//        Map<String, String> mapBillNote = new HashMap<>();
//
//        try
//        {
//            String billNote = "";
//            String sql = "select a.strTaxCode,a.strTaxDesc,a.strBillNote "
//                    + "from tbltaxhd a," + billTaxDtl + " b "
//                    + "where a.strTaxCode=b.strTaxCode "
//                    + "and b.strBillNo='" + billNo + "' "
//                    + "and date(b.dteBillDate)='" + billDate + "' "
//                    + "order by a.strBillNote ";
//            ResultSet rsBillNote = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//            while (rsBillNote.next())
//            {
//                billNote = rsBillNote.getString(3).trim();
//                if (!billNote.isEmpty())
//                {
//                    mapBillNote.put(billNote, billNote);
//                }
////
////                if (billNote.length() > 0)
////                {
////                    objBillOut.write("  " + billNote);
////                    objBillOut.newLine();
////                }
//            }
//            rsBillNote.close();
//
//            for (String printBillNote : mapBillNote.values())
//            {
//                objBillOut.write("  " + printBillNote);
//                objBillOut.newLine();
//            }
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    public void funGenerateTextKOTForMakeKOTForMenuHeadWise(String tableNo, String costCenterCode, String areaCode, String KOTNO, String Reprint, String primaryPrinterName, String secondaryPrinterName, String costCenterName, String printYN, String NCKotYN, String labelOnKOT)
//    {
//        try
//        {
//            PreparedStatement pst = null;
//            funCreateTempFolder();
//            String filePath = System.getProperty("user.dir");
//            File fileKOTPrint = new File(filePath + "/Temp/Temp_KOT.txt");
//            FileWriter fstream = new FileWriter(fileKOTPrint);
//            BufferedWriter KotOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileKOTPrint), "UTF8"));
//
//            DecimalFormat decimalFormat = new DecimalFormat("#.###");
//
//            boolean isReprint = false;
//            if ("Reprint".equalsIgnoreCase(Reprint))
//            {
//                isReprint = true;
//                funPrintBlankSpace("[DUPLICATE]", KotOut);
//                KotOut.write("[DUPLICATE]");
//                KotOut.newLine();
//            }
//
//            if ("Y".equalsIgnoreCase(NCKotYN))
//            {
//                funPrintBlankSpace("NCKOT", KotOut);
//                KotOut.write("NCKOT");
//                KotOut.newLine();
//            }
//            else
//            {
//                funPrintBlankSpace(labelOnKOT, KotOut);//write KOT
//                KotOut.write(labelOnKOT);//write KOT
//                KotOut.newLine();
//            }
//            funPrintBlankSpace(clsGlobalVarClass.gPOSName, KotOut);
//            KotOut.write(clsGlobalVarClass.gPOSName);
//            KotOut.newLine();
//            funPrintBlankSpace(costCenterName, KotOut);
//            KotOut.write(costCenterName);
//            KotOut.newLine();
//
//            KOTType = "DINE";
//            if (null != clsGlobalVarClass.hmTakeAway.get(tableNo))
//            {
//                KOTType = "Take Away";
//            }
//            funPrintBlankSpace(KOTType, KotOut);
//            KotOut.write(KOTType);
//            KotOut.newLine();
//
//            if (clsGlobalVarClass.gCounterWise.equals("Yes"))
//            {
//                funPrintBlankSpace(clsGlobalVarClass.gCounterName, KotOut);
//                KotOut.write(clsGlobalVarClass.gCounterName);
//                KotOut.newLine();
//            }
//
//            KotOut.write(Line);
//            KotOut.newLine();
//            KotOut.write("  KOT NO     :");
//            KotOut.write(KOTNO + "  ");
//            KotOut.newLine();
//
//            String sqlKOTDtl = "select a.strWaiterNo,b.strTableName,b.intPaxNo,ifnull(c.strWShortName,''),a.dteDateCreated,TIME_FORMAT(TIME(a.dteDateCreated),'%h:%i')  "
//                    + " from tblitemrtemp a "
//                    + " left outer join tbltablemaster b on a.strTableNo=b.strTableNo "
//                    + " and b.strOperational='Y' "
//                    + " left outer join tblwaitermaster c on a.strWaiterNo=c.strWaiterNo "
//                    + " where a.strKOTNo=? and a.strTableNo=? group by a.strKOTNo ;";
//            pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlKOTDtl);
//            pst.setString(1, KOTNO);
//            pst.setString(2, tableNo);
//            ResultSet rsKOTDetails = pst.executeQuery();
//
//            String tableNoForConsolidatedKOT = tableNo;
//            String waiterNameForConsolidatedKOT = "";
//
//            if (rsKOTDetails.next())
//            {
//                if (clsGlobalVarClass.gClientCode.equalsIgnoreCase("171.001") || clsGlobalVarClass.gClientCode.equalsIgnoreCase("136.001"))//"136.001",KINKI  "171.001","CHINA GRILL-PIMPRI"
//                {
//                    KotOut.write("  TABLE No   :");
//                }
//                else
//                {
//                    KotOut.write("  TABLE NAME :");
//                }
//                KotOut.write(rsKOTDetails.getString(2) + "  ");
//                KotOut.write(" PAX   :");
//                KotOut.write(rsKOTDetails.getString(3));
//                KotOut.newLine();
//
//                if (!rsKOTDetails.getString(4).trim().isEmpty())
//                {
//                    waiterNameForConsolidatedKOT = rsKOTDetails.getString(4);
//                    KotOut.write("  WAITER NAME:" + "   " + rsKOTDetails.getString(4));
//                    KotOut.newLine();
//                }
//                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a ");
//                KotOut.write("  DATE & TIME:" + dateTimeFormat.format(rsKOTDetails.getObject(5)));
//            }
//            rsKOTDetails.close();
//
//            KotOut.newLine();
//            if ("Y".equalsIgnoreCase(NCKotYN))
//            {
//                String sql = "select a.strRemark from tblnonchargablekot a where a.strKOTNo='" + KOTNO + "' "
//                        + "group by a.strKOTNo ";
//                ResultSet rsRemark = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//                if (rsRemark.next() && rsRemark.getString(1).trim().length() > 0)
//                {
//                    KotOut.write("  Remark     :" + rsRemark.getString(1));
//                }
//            }
//
//            InetAddress ipAddress = InetAddress.getLocalHost();
//            String hostName = ipAddress.getHostName();
//            KotOut.newLine();
//            KotOut.write("  KOT From Computer:" + hostName);
//            KotOut.newLine();
//            KotOut.write("  KOT By User      :" + clsGlobalVarClass.gUserCode);
//            KotOut.newLine();
//
//            KotOut.write(Line);
//            KotOut.newLine();
//            KotOut.write("  QTY         ITEM NAME  ");
//            KotOut.newLine();
//            KotOut.write(Line);
//
//            // Code to Print KOT Item details    
//            String sqlKOTItems = "";
//
//            if (clsGlobalVarClass.gAreaWisePricing.equals("Y"))
//            {
//                sqlKOTItems = "SELECT LEFT(a.strItemCode,7),b.strItemName,a.dblItemQuantity,a.strKOTNo,a.strSerialNo,d.strShortName,e.strMenuName "
//                        + ",ifnull(f.strSubMenuHeadName,'')strSubMenuHeadName "
//                        + "FROM tblitemrtemp a "
//                        + "left outer join tblmenuitempricingdtl b on a.strItemCode=b.strItemCode "
//                        + "left outer join tblprintersetup c on b.strCostCenterCode=c.strCostCenterCode  "
//                        + "left outer join tblitemmaster d on a.strItemCode=d.strItemCode "
//                        + "left outer join tblmenuhd e on b.strMenuCode=e.strMenuCode  "
//                        + "left outer join tblsubmenuhead f on b.strSubMenuHeadCode=f.strSubMenuHeadCode  "
//                        + "WHERE a.strTableNo=?  "
//                        + "AND a.strKOTNo=?  "
//                        + "AND b.strCostCenterCode=?  "
//                        + "and (b.strPOSCode=? OR b.strPOSCode='All')   "
//                        + "AND b.strHourlyPricing='No' "
//                        + "and (b.strAreaCode IN (SELECT strAreaCode FROM tbltablemaster where strTableNo=? )) "
//                        + "and LEFT(a.strItemCode,7)=b.strItemCode and b.strHourlyPricing='No' "
//                        + "ORDER BY f.strSubMenuHeadCode ";
//            }
//            else
//            {
//                sqlKOTItems = "SELECT LEFT(a.strItemCode,7),b.strItemName,a.dblItemQuantity,a.strKOTNo,a.strSerialNo,d.strShortName,e.strMenuName "
//                        + ",ifnull(f.strSubMenuHeadName,'')strSubMenuHeadName "
//                        + "FROM tblitemrtemp a "
//                        + "left outer join tblmenuitempricingdtl b on a.strItemCode=b.strItemCode "
//                        + "left outer join tblprintersetup c on b.strCostCenterCode=c.strCostCenterCode  "
//                        + "left outer join tblitemmaster d on a.strItemCode=d.strItemCode "
//                        + "left outer join tblmenuhd e on b.strMenuCode=e.strMenuCode  "
//                        + "left outer join tblsubmenuhead f on b.strSubMenuHeadCode=f.strSubMenuHeadCode  "
//                        + "WHERE a.strTableNo=?  "
//                        + "AND a.strKOTNo=?  "
//                        + "AND b.strCostCenterCode=?  "
//                        + "and (b.strPOSCode=? OR b.strPOSCode='All')   "
//                        + "AND b.strHourlyPricing='No' "
//                        + "and (b.strAreaCode IN (SELECT strAreaCode FROM tbltablemaster where strTableNo=? ) OR b.strAreaCode ='" + areaCode + "') "
//                        + "and LEFT(a.strItemCode,7)=b.strItemCode and b.strHourlyPricing='No' "
//                        + "ORDER BY f.strSubMenuHeadCode ";
//            }
//            //System.out.println(sqlKOTItems);
//            PreparedStatement pstKOTItems = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlKOTItems);
//            pstKOTItems.setString(1, tableNo);
//            pstKOTItems.setString(2, KOTNO);
//            pstKOTItems.setString(3, costCenterCode);
//            pstKOTItems.setString(4, clsGlobalVarClass.gPOSCode);
//            pstKOTItems.setString(5, tableNo);
//            //pst_KOT_Items.setString(5, AreaCode);
//
//            int noOfCharsToBePrinted = 30;
//            int noOfCharsToBePrintedX2 = noOfCharsToBePrinted * 2;
//            int qtyWidth = 6;
//            int qtyLeftSpace = 3;
//            String printSubMenuHeadName = "";
//
//            ResultSet rsKOTItems = pstKOTItems.executeQuery();
//            while (rsKOTItems.next())
//            {
//                String rsSubMenuHead = rsKOTItems.getString(8).toUpperCase();
//                if (rsSubMenuHead.isEmpty())
//                {
//                    rsSubMenuHead = "Not Define";
//                }
//
//                if (printSubMenuHeadName.isEmpty())
//                {
//                    printSubMenuHeadName = rsSubMenuHead.toUpperCase();
//
//                    KotOut.newLine();
//                    KotOut.write("   " + printSubMenuHeadName.trim());
//
//                }
//                else if (printSubMenuHeadName.toUpperCase().equalsIgnoreCase(rsSubMenuHead.toUpperCase()))
//                {
//                    //do nothing
//                }
//                else
//                {
//                    KotOut.newLine();
//                    printSubMenuHeadName = rsSubMenuHead.toUpperCase();
//                    KotOut.newLine();
//                    KotOut.write("   " + printSubMenuHeadName.trim());
//                }
//
//                String kotItemName = rsKOTItems.getString(2);//full name
//                if (clsGlobalVarClass.gPrintShortNameOnKOT && !rsKOTItems.getString(6).trim().isEmpty())
//                {
//                    kotItemName = rsKOTItems.getString(6);//short name
//                }
//                KotOut.newLine();
//
//                String itemqty = decimalFormat.format(rsKOTItems.getDouble(3));
//
//                KotOut.write("   " + String.format("%-" + qtyWidth + "s", itemqty) + "");
//
//                if (kotItemName.length() <= noOfCharsToBePrinted)
//                {
//                    KotOut.write(kotItemName.trim());
//                }
//                else
//                {
//                    if (kotItemName.length() >= noOfCharsToBePrintedX2)
//                    {
//                        KotOut.write(kotItemName.substring(0, noOfCharsToBePrinted).trim());
//                        KotOut.newLine();
//                        KotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + "" + kotItemName.substring(noOfCharsToBePrinted, noOfCharsToBePrintedX2).trim());
//
//                        KotOut.newLine();
//                        KotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + "" + kotItemName.substring(noOfCharsToBePrintedX2, kotItemName.length()).trim());
//                    }
//                    else
//                    {
//                        KotOut.write(kotItemName.substring(0, noOfCharsToBePrinted).trim());
//                        KotOut.newLine();
//                        KotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + "" + kotItemName.substring(noOfCharsToBePrinted, kotItemName.length()).trim());
//                    }
//                }
//
//                boolean printDefaultModifier = true;
//
//                //print no default modifiers
//                String sqlModifier = "select a.strItemName,sum(a.dblItemQuantity) from tblitemrtemp a "
//                        + " where a.strItemCode like'" + rsKOTItems.getString(1) + "M%' and a.strKOTNo='" + KOTNO + "' "
//                        + " and strSerialNo like'" + rsKOTItems.getString(5) + ".%' "
//                        + " group by a.strItemCode,a.strItemName ";
//                //System.out.println(sqlModifier);
//                ResultSet rsModifierItems = clsGlobalVarClass.dbMysql.executeResultSet(sqlModifier);
//                while (rsModifierItems.next())
//                {
//                    printDefaultModifier = false;
//
//                    String modQty = decimalFormat.format(rsModifierItems.getDouble(2));//rsModifierItems.getString(2);
//                    int modQtyLength = modQty.length();
//
//                    String modifierName = rsModifierItems.getString(1);
//                    if (modifierName.startsWith("-->"))
//                    {
//                        if (modifierName.length() <= noOfCharsToBePrinted)
//                        {
//                            if (clsGlobalVarClass.gPrintModQtyOnKOT)
//                            {
//                                KotOut.newLine();
//                                KotOut.write("   " + String.format("%-" + qtyWidth + "s", modQty) + "" + modifierName.trim());
//                            }
//                            else
//                            {
//                                KotOut.newLine();
//                                KotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + "" + modifierName.trim());
//                            }
//                        }
//                        else
//                        {
//                            if (clsGlobalVarClass.gPrintModQtyOnKOT)
//                            {
//                                if (modifierName.length() >= noOfCharsToBePrintedX2)
//                                {
//                                    KotOut.newLine();
//                                    KotOut.write("   " + String.format("%-" + qtyWidth + "s", modQty) + "" + modifierName.substring(0, noOfCharsToBePrinted).trim());
//                                    KotOut.newLine();
//                                    KotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + "" + modifierName.substring(noOfCharsToBePrinted, noOfCharsToBePrintedX2).trim());
//                                    KotOut.newLine();
//                                    KotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + "" + modifierName.substring(noOfCharsToBePrintedX2, modifierName.length()).trim());
//                                }
//                                else
//                                {
//                                    KotOut.newLine();
//                                    KotOut.write("   " + String.format("%-" + qtyWidth + "s", modQty) + "" + modifierName.substring(0, noOfCharsToBePrinted).trim());
//                                    KotOut.newLine();
//                                    KotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + "" + modifierName.substring(noOfCharsToBePrinted, modifierName.length()).trim());
//                                }
//                            }
//                            else
//                            {
//                                if (modifierName.length() >= noOfCharsToBePrintedX2)
//                                {
//                                    KotOut.newLine();
//                                    KotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + "" + modifierName.substring(0, noOfCharsToBePrinted).trim());
//                                    KotOut.newLine();
//                                    KotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + "" + modifierName.substring(noOfCharsToBePrinted, noOfCharsToBePrintedX2).trim());
//                                    KotOut.newLine();
//                                    KotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + "" + modifierName.substring(noOfCharsToBePrintedX2, modifierName.length()).trim());
//                                }
//                                else
//                                {
//                                    KotOut.newLine();
//                                    KotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + "" + modifierName.substring(0, noOfCharsToBePrinted).trim());
//                                    KotOut.newLine();
//                                    KotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + "" + modifierName.substring(noOfCharsToBePrinted, modifierName.length()).trim());
//                                }
//                            }
//                        }
//
//                    }
//                }
//                rsModifierItems.close();
//
//                //print  default modifiers
//                if (printDefaultModifier && !(printSubMenuHeadName.equalsIgnoreCase("Buffet") || printSubMenuHeadName.equalsIgnoreCase("Not Define")))
//                {
//                    KotOut.newLine();
//                    KotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + "" + "-->No Message");
//
//                }
//
//                //print  5 blank lines after buffet item
//                if (printSubMenuHeadName.equalsIgnoreCase("Buffet") || printSubMenuHeadName.equalsIgnoreCase("Not Define"))
//                {
//                    KotOut.newLine();
//                    KotOut.write("         " + String.format("%-15s", "Indian Starter") + "" + String.format("%-15s", "Chinese Starter"));
//
//                    KotOut.newLine();
//                    KotOut.write("         " + String.format("%-15s", "1") + String.format("%-15s", "1"));
//                    KotOut.newLine();
//                    KotOut.write("         " + String.format("%-15s", "2") + String.format("%-15s", "2"));
//                    KotOut.newLine();
//                    KotOut.write("         " + String.format("%-15s", "3") + String.format("%-15s", "3"));
//                    KotOut.newLine();
//                    KotOut.write("         " + String.format("%-15s", "4") + String.format("%-15s", "4"));
//                    KotOut.newLine();
//                    KotOut.write("         " + String.format("%-15s", "5") + String.format("%-15s", "5"));
//                }
//
//                //print  5 blank lines after buffet item
////                if (printSubMenuHeadName.equalsIgnoreCase("Buffet") || printSubMenuHeadName.equalsIgnoreCase("Not Define"))
////                {
////                    KotOut.newLine();
////                    KotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + "" + "1");
////                    KotOut.newLine();
////                    KotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + "" + "2");
////                    KotOut.newLine();
////                    KotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + "" + "3");
////                    KotOut.newLine();
////                    KotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + "" + "4");
////                    KotOut.newLine();
////                    KotOut.write("   " + String.format("%-" + qtyWidth + "s", "") + "" + "5");
////                }
//            }
//            rsKOTItems.close();
//            pstKOTItems.close();
//
//            KotOut.newLine();
//            KotOut.write(Line);
//            for (int cntLines = 0; cntLines < Integer.parseInt(clsGlobalVarClass.gNoOfLinesInKOTPrint); cntLines++)
//            {
//                KotOut.newLine();
//            }
//            if ("linux".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
//            {
//                KotOut.write("V");//Linux
//            }
//            else if ("windows".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
//            {
//                if ("Inbuild".equalsIgnoreCase(clsPosConfigFile.gPrinterType))
//                {
//                    KotOut.write("V");
//                }
//                else
//                {
//                    KotOut.write("m");//windows
//                }
//            }
//
//            KotOut.close();
//            fstream.close();
//            pst.close();
//            //System.out.println("PP="+primaryPrinterName);
//            //System.out.println("SP="+secondaryPrinterName);
//            //System.out.println("PRINT="+printYN);
//
//            if ("Reprint".equalsIgnoreCase(Reprint))
//            {
//                funShowTextFile(fileKOTPrint, "", clsGlobalVarClass.gBillPrintPrinterPort);
//            }
//
//            if (clsGlobalVarClass.gShowBill)
//            {
//                funShowTextFile(fileKOTPrint, "", clsGlobalVarClass.gBillPrintPrinterPort);
//            }
//
//            if (printYN.equals("Y"))
//            {
//                sql = "select strPrintOnBothPrinters from tblcostcentermaster where strCostCenterCode='" + costCenterCode + "' ";
//                ResultSet rsCostCenter = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//                if (rsCostCenter.next())
//                {
//                    funPrintToPrinter(primaryPrinterName, secondaryPrinterName, "kot", rsCostCenter.getString(1), isReprint);
//                }
//                rsCostCenter.close();
//            }
//        }
//        catch (Exception e)
//        {
//            JOptionPane.showMessageDialog(null, "KOT Printing Error:" + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    //text 16
//    public void funGenerateTextFileBillPrintingForFormat16(String billNo, String reprint, String formName, String transType, String billDate, String POSCode, String viewORprint)
//    {
//        clsUtility objUtility = new clsUtility();
//        clsUtility2 objUtility2 = new clsUtility2();
//
//        String Linefor5 = "  --------------------------------------";
//        try
//        {
//            String user = "";
//            String billhd = null;
//            String billdtl = null;
//            String billModifierdtl = null;
//            String billSettlementdtl = null;
//            String billtaxdtl = null;
//            String billDscFrom = null;
//            String billPromoDtl = null;
//            String advBookBillHd = null;
//            String advBookBillDtl = null;
//            String advBookBillCharDtl = null;
//            String advReceiptHd = null;
//            if (clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("HOPOS"))
//            {
//                billhd = "tblqbillhd";
//                billdtl = "tblqbilldtl";
//                billModifierdtl = "tblqbillmodifierdtl";
//                billSettlementdtl = "tblqbillsettlementdtl";
//                billtaxdtl = "tblqbilltaxdtl";
//                billDscFrom = "tblqbilldiscdtl";
//                billPromoDtl = "tblqbillpromotiondtl";
//
//                advBookBillHd = "tblqadvbookbillhd";
//                advBookBillDtl = "tblqadvbookbilldtl";
//                advBookBillCharDtl = "tblqadvbookbillchardtl";
//                advReceiptHd = "tblqadvancereceipthd";
//            }
//            else
//            {
//                if ("sales report".equalsIgnoreCase(formName))
//                {
//                    billhd = "tblbillhd";
//                    billdtl = "tblbilldtl";
//                    billModifierdtl = "tblbillmodifierdtl";
//                    billSettlementdtl = "tblbillsettlementdtl";
//                    billtaxdtl = "tblbilltaxdtl";
//                    billDscFrom = "tblbilldiscdtl";
//                    billPromoDtl = "tblbillpromotiondtl";
//                    advBookBillHd = "tbladvbookbillhd";
//                    advBookBillDtl = "tbladvbookbilldtl";
//                    advBookBillCharDtl = "tbladvbookbillchardtl";
//                    advReceiptHd = "tbladvancereceipthd";
//                    long dateDiff = new clsUtility().funCompareDate(billDate, objUtility.funGetPOSDateForTransaction());
//                    if (dateDiff > 0)
//                    {
//                        billhd = "tblqbillhd";
//                        billdtl = "tblqbilldtl";
//                        billModifierdtl = "tblqbillmodifierdtl";
//                        billSettlementdtl = "tblqbillsettlementdtl";
//                        billtaxdtl = "tblqbilltaxdtl";
//                        billDscFrom = "tblqbilldiscdtl";
//                        billPromoDtl = "tblqbillpromotiondtl";
//                        advBookBillHd = "tblqadvbookbillhd";
//                        advBookBillDtl = "tblqadvbookbilldtl";
//                        advBookBillCharDtl = "tblqadvbookbillchardtl";
//                        advReceiptHd = "tblqadvancereceipthd";
//                    }
//                    String sql = "select count(strBillNo) from tblbillhd where strBillNo='" + billNo + "' and strPOSCode='" + POSCode + "' ";
//                    ResultSet rsBillTable = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//                    rsBillTable.next();
//                    int billCnt = rsBillTable.getInt(1);
//                    if (billCnt == 0)
//                    {
//                        billhd = "tblqbillhd";
//                        billdtl = "tblqbilldtl";
//                        billModifierdtl = "tblqbillmodifierdtl";
//                        billSettlementdtl = "tblqbillsettlementdtl";
//                        billtaxdtl = "tblqbilltaxdtl";
//                        billDscFrom = "tblqbilldiscdtl";
//                        billPromoDtl = "tblqbillpromotiondtl";
//                        advBookBillHd = "tblqadvbookbillhd";
//                        advBookBillDtl = "tblqadvbookbilldtl";
//                        advBookBillCharDtl = "tblqadvbookbillchardtl";
//                        advReceiptHd = "tblqadvancereceipthd";
//                    }
//                }
//                else
//                {
//                    billhd = "tblbillhd";
//                    billdtl = "tblbilldtl";
//                    billModifierdtl = "tblbillmodifierdtl";
//                    billSettlementdtl = "tblbillsettlementdtl";
//                    billtaxdtl = "tblbilltaxdtl";
//                    billDscFrom = "tblbilldiscdtl";
//                    billPromoDtl = "tblbillpromotiondtl";
//                    advBookBillHd = "tbladvbookbillhd";
//                    advBookBillDtl = "tbladvbookbilldtl";
//                    advBookBillCharDtl = "tbladvbookbillchardtl";
//                    advReceiptHd = "tbladvancereceipthd";
//                }
//            }
//            PreparedStatement pst = null;
//            funCreateTempFolder();
//            String filePath = System.getProperty("user.dir");
//            File Text_Bill = new File(filePath + "/Temp/Temp_Bill.txt");
//            String subTotal = "";
//            String grandTotal = "";
//            String advAmount = "";
//            String deliveryCharge = "";
//            String customerCode = "";
//            String waiterName = "";
//            String tblName = "";
//            ResultSet rs_BillHD = null;
//            boolean flgComplimentaryBill = false;
//            StringBuilder sqlBillHeaderDtl = new StringBuilder();
//            sqlBillHeaderDtl.append("select ifnull(a.strTableNo,''),ifnull(a.strWaiterNo,''),a.dteBillDate,time(a.dteBillDate),a.dblDiscountAmt,a.dblSubTotal,"
//                    + "ifnull(a.strCustomerCode,''),a.dblGrandTotal,a.dblTaxAmt,ifnull(a.strReasonCode,''),ifnull(a.strRemarks,''),a.strUserCreated "
//                    + ",ifnull(dblDeliveryCharges,0.00),ifnull(i.dblAdvDeposite,0.00),a.dblDiscountPer,b.strPOSName,a.intPaxNo "
//                    + ",ifnull(c.strTableName,''),ifnull(d.strWShortName,''),ifnull(d.strWFullName,''),ifnull(l.strSettelmentType,''),ifnull(j.strReasonName,'') as voidedReason, "
//                    + "ifnull(g.strReasonName,''),ifnull(e.strCustomerName,''),ifnull(a.strAdvBookingNo,''),ifnull(h.strMessage,''),ifnull(h.strShape,''),ifnull(h.strNote,''),ifnull(a.dblTipAmount,0.00) "
//                    + ",a.strOperationType,ifnull(a.strTakeAwayRemarks,''),ifnull(e.longMobileNo,'')  "
//                    + "from " + billhd + " a "
//                    + "left outer join tblposmaster b on a.strPOSCode=b.strPosCode  "
//                    + "left outer join tbltablemaster c on a.strTableNo=c.strTableNo and a.strClientCode=c.strClientCode "
//                    + "left outer join tblwaitermaster d on a.strWaiterNo=d.strWaiterNo and a.strClientCode=d.strClientCode "
//                    + "left outer join tblcustomermaster e on a.strCustomerCode=e.strCustomerCode and a.strClientCode=e.strClientCode "
//                    + "left outer join tbldebitcardmaster f on a.strCardNo=f.strCardNo "
//                    + "left outer join tblreasonmaster g on a.strReasonCode=g.strReasonCode "
//                    + "left outer join " + advBookBillHd + " h on a.strAdvBookingNo=h.strAdvBookingNo and a.strClientCode=h.strClientCode "
//                    + "left outer join " + advReceiptHd + " i on h.strAdvBookingNo=i.strAdvBookingNo and a.strClientCode=i.strClientCode "
//                    + "left outer join tblvoidbillhd j on a.strBillNo=j.strBillNo and a.strPOSCode=j.strPosCode and a.strClientCode=j.strClientCode "
//                    + "left outer join " + billSettlementdtl + " k on a.strBillNo=k.strBillNo and a.strClientCode=k.strClientCode "
//                    + "left outer join tblsettelmenthd l on k.strSettlementCode=l.strSettelmentCode "
//                    + "where a.strBillNo=? and a.strPOSCode=? "
//                    + "group by a.strBillNo; ");
//            pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlBillHeaderDtl.toString());
//            pst.setString(1, billNo);
//            pst.setString(2, POSCode);
//            rs_BillHD = pst.executeQuery();
//            rs_BillHD.next();
//            if (rs_BillHD.getString(21).equals("Complementary"))
//            {
//                flgComplimentaryBill = true;
//            }
//            FileWriter fstream_bill = new FileWriter(Text_Bill);
//            BufferedWriter BillOut = new BufferedWriter(fstream_bill);
//            if (clsGlobalVarClass.gClientCode.equals("117.001"))
//            {
//                if (POSCode.equals("P01"))
//                {
//                    funPrintBlankSpace("THE PREM'S HOTEL", BillOut);
//                    BillOut.write("THE PREM'S HOTEL");
//                    BillOut.newLine();
//                }
//                else if (POSCode.equals("P02"))
//                {
//                    funPrintBlankSpace("SWIG", BillOut);
//                    BillOut.write("SWIG");
//                    BillOut.newLine();
//                }
//            }
//            boolean isReprint = false;
//            if ("reprint".equalsIgnoreCase(reprint))
//            {
//                isReprint = true;
//                funPrintBlankSpace("[DUPLICATE]", BillOut);
//                BillOut.write("[DUPLICATE]");
//                BillOut.newLine();
//            }
//            if (transType.equals("Void"))
//            {
//                funPrintBlankSpace("VOIDED BILL", BillOut);
//                BillOut.write("VOIDED BILL");
//                BillOut.newLine();
//            }
//            boolean flag_isHomeDelvBill = false;
//            String SQL_HomeDelivery = "select strBillNo,strCustomerCode,strDPCode,tmeTime,strCustAddressLine1 "
//                    + "from tblhomedelivery where strBillNo=? ;";
//            pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(SQL_HomeDelivery);
//            pst.setString(1, billNo);
//            ResultSet rs_HomeDelivery = pst.executeQuery();
//            if (rs_HomeDelivery.next())
//            {
//                flag_isHomeDelvBill = true;
//                customerCode = rs_HomeDelivery.getString(2);
//                if (clsGlobalVarClass.gPrintHomeDeliveryYN)
//                {
//                    funPrintBlankSpace("HOME DELIVERY", BillOut);
//                    BillOut.write("HOME DELIVERY");
//                    BillOut.newLine();
//                }
//
//                String SQL_CustomerDtl = "";
//                if (rs_HomeDelivery.getString(5).equals("Temporary"))
//                {
//                    SQL_CustomerDtl = "select a.strCustomerName,a.strTempAddress,a.strTempStreet"
//                            + " ,a.strTempLandmark,a.strBuildingName,a.strCity,a.intPinCode,a.longMobileNo "
//                            + " from tblcustomermaster a left outer join tblbuildingmaster b "
//                            + " on a.strBuldingCode=b.strBuildingCode "
//                            + " where a.strCustomerCode=? ;";
//                }
//                else if (rs_HomeDelivery.getString(5).equals("Office"))
//                {
//                    SQL_CustomerDtl = "select a.strCustomerName,a.strOfficeBuildingName,a.strOfficeStreetName"
//                            + ",a.strOfficeLandmark,a.strOfficeArea,a.strOfficeCity,a.strOfficePinCode,a.longMobileNo "
//                            + " from tblcustomermaster a "
//                            + " where a.strCustomerCode=? ";
//                }
//                else
//                {
//                    SQL_CustomerDtl = "select a.strCustomerName,a.strCustAddress,a.strStreetName"
//                            + " ,a.strLandmark,a.strBuildingName,a.strCity,a.intPinCode,a.longMobileNo "
//                            + " from tblcustomermaster a left outer join tblbuildingmaster b "
//                            + " on a.strBuldingCode=b.strBuildingCode "
//                            + " where a.strCustomerCode=? ;";
//                }
//                pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(SQL_CustomerDtl);
//                pst.setString(1, rs_HomeDelivery.getString(2));
//                ResultSet rs_CustomerDtl = pst.executeQuery();
//                while (rs_CustomerDtl.next())
//                {
//                    BillOut.write("  NAME      :" + rs_CustomerDtl.getString(1).toUpperCase());
//                    BillOut.newLine();
//                    // Building Name    
//                    String add = rs_CustomerDtl.getString(2);
//                    int strlen = add.length();
//                    String add1 = "";
//                    if (strlen < 28)
//                    {
//                        add1 = add.substring(0, strlen);
//                        BillOut.write("  ADDRESS1  :" + add1.toUpperCase().replaceAll("\n", " "));
//                        BillOut.newLine();
//                    }
//                    else
//                    {
//                        add1 = add.substring(0, 28);
//                        BillOut.write("  ADDRESS1  :" + add1.toUpperCase().replaceAll("\n", " "));
//                        BillOut.newLine();
//                    }
//                    for (int i = 28; i <= strlen;)
//                    {
//                        int end = 0;
//                        end = i + 28;
//                        if (strlen > end)
//                        {
//                            add1 = add.substring(i, end);
//                            i = end;
//                            BillOut.write("             " + add1.toUpperCase().replaceAll("\n", " "));
//                            BillOut.newLine();
//                        }
//                        else
//                        {
//                            add1 = add.substring(i, strlen);
//                            BillOut.write("             " + add1.toUpperCase().replaceAll("\n", " "));
//                            BillOut.newLine();
//                            i = strlen + 1;
//                        }
//                    }
//                    // Street Name    
//                    String street = rs_CustomerDtl.getString(3);
//                    String street1;
//                    int streetlen = street.length();
//                    for (int i = 0; i <= streetlen;)
//                    {
//                        int end = 0;
//                        end = i + 28;
//                        if (streetlen > end)
//                        {
//                            street1 = street.substring(i, end);
//                            BillOut.write("             " + street1.toUpperCase());
//                            BillOut.newLine();
//                            i = end;
//                        }
//                        else
//                        {
//                            street1 = street.substring(i, streetlen);
//                            BillOut.write("             " + street1.toUpperCase());
//                            BillOut.newLine();
//                            i = streetlen + 1;
//                        }
//                    }
//                    // Landmark Name    
//                    if (rs_CustomerDtl.getString(4).trim().length() > 0)
//                    {
//                        BillOut.write("             " + rs_CustomerDtl.getString(4).toUpperCase());
//                        BillOut.newLine();
//                    }
//                    // Area Name    
//                    if (rs_CustomerDtl.getString(5).trim().length() > 0)
//                    {
//                        BillOut.write("             " + rs_CustomerDtl.getString(5).toUpperCase());
//                        BillOut.newLine();
//                    }
//                    // City Name    
//                    if (rs_CustomerDtl.getString(6).trim().length() > 0)
//                    {
//                        BillOut.write("             " + rs_CustomerDtl.getString(6).toUpperCase());
//                        BillOut.newLine();
//                    }
//                    // Pin Code    
//                    if (rs_CustomerDtl.getString(7).trim().length() > 0)
//                    {
//                        BillOut.write("             " + rs_CustomerDtl.getString(7).toUpperCase());
//                        BillOut.newLine();
//                    }
//                    // Mobile No    
//                    BillOut.write("  MOBILE NO :" + rs_CustomerDtl.getString(8));
//                    BillOut.newLine();
//                }
//                rs_CustomerDtl.close();
//                if (null != rs_HomeDelivery.getString(3) && rs_HomeDelivery.getString(3).trim().length() > 0)
//                {
//                    String[] delBoys = rs_HomeDelivery.getString(3).split(",");
//                    StringBuilder strIN = new StringBuilder("(");
//                    for (int i = 0; i < delBoys.length; i++)
//                    {
//                        if (i == 0)
//                        {
//                            strIN.append("'" + delBoys[i] + "'");
//                        }
//                        else
//                        {
//                            strIN.append(",'" + delBoys[i] + "'");
//                        }
//                    }
//                    strIN.append(")");
//                    String SQL_DeliveryBoyDtl = "select strDPName from tbldeliverypersonmaster where strDPCode IN " + strIN + " ;";
//                    pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(SQL_DeliveryBoyDtl);
//                    ResultSet rs_DeliveryBoyDtl = pst.executeQuery();
//                    strIN.setLength(0);
//                    for (int i = 0; rs_DeliveryBoyDtl.next(); i++)
//                    {
//                        if (i == 0)
//                        {
//                            strIN.append(rs_DeliveryBoyDtl.getString(1).toUpperCase());
//                        }
//                        else
//                        {
//                            strIN.append("," + rs_DeliveryBoyDtl.getString(1).toUpperCase());
//                        }
//                    }
//                    BillOut.write("  DELV BOY  :" + strIN);
//                    BillOut.newLine();
//                    rs_DeliveryBoyDtl.close();
//                }
//                BillOut.write(Line);
//                BillOut.newLine();
//            }
//            else
//            {
//                if (rs_BillHD.getString(7).length() > 0)//customerCode
//                {
//                    BillOut.write("  NAME      :" + rs_BillHD.getString(24).toUpperCase());
//                    BillOut.newLine();
//                    // Mobile No    
//                    BillOut.write("  MOBILE NO :" + rs_BillHD.getString(32));
//                    BillOut.newLine();
//                }
//            }
//            rs_HomeDelivery.close();
//            //print take away
//            int billPrintSize = 4;
//            if (rs_BillHD.getString(30).equals("TakeAway"))
//            {
//                funPrintBlankSpace("Take Away", BillOut);
//                BillOut.write("Take Away");
//                BillOut.newLine();
//            }
//            if (clsGlobalVarClass.gPrintTaxInvoice.equalsIgnoreCase("Y"))
//            {
//                funPrintBlankSpace("TAX INVOICE", BillOut);
//                BillOut.write("TAX INVOICE");
//                BillOut.newLine();
//            }
//            if (clsGlobalVarClass.gClientCode.equals("047.001") && POSCode.equals("P03"))
//            {
//                funPrintBlankSpace("SHRI SHAM CATERERS", BillOut);
//                BillOut.write("SHRI SHAM CATERERS");
//                BillOut.newLine();
//                String cAddr1 = "Flat No.7, Mon Amour,";
//                funPrintBlankSpace(cAddr1, BillOut);
//                BillOut.write(cAddr1.toUpperCase());
//                BillOut.newLine();
//                String cAddr2 = "Thorat Colony,Prabhat Road,";
//                funPrintBlankSpace(cAddr2, BillOut);
//                BillOut.write(cAddr2.toUpperCase());
//                BillOut.newLine();
//                String cAddr3 = " Erandwane, Pune 411 004.";
//                funPrintBlankSpace(cAddr3, BillOut);
//                BillOut.write(cAddr3.toUpperCase());
//                BillOut.newLine();
//                String cAddr4 = "Approved Caterers of";
//                funPrintBlankSpace(cAddr4, BillOut);
//                BillOut.write(cAddr4.toUpperCase());
//                BillOut.newLine();
//                String cAddr5 = "ROYAL CONNAUGHT BOAT CLUB";
//                funPrintBlankSpace(cAddr5, BillOut);
//                BillOut.write(cAddr5.toUpperCase());
//                BillOut.newLine();
//            }
//            else if (clsGlobalVarClass.gClientCode.equals("047.001") && POSCode.equals("P02"))
//            {
//                funPrintBlankSpace("SHRI SHAM CATERERS", BillOut);
//                BillOut.write("SHRI SHAM CATERERS");
//                BillOut.newLine();
//                String cAddr1 = "Flat No.7, Mon Amour,";
//                funPrintBlankSpace(cAddr1, BillOut);
//                BillOut.write(cAddr1.toUpperCase());
//                BillOut.newLine();
//                String cAddr2 = "Thorat Colony,Prabhat Road,";
//                funPrintBlankSpace(cAddr2, BillOut);
//                BillOut.write(cAddr2.toUpperCase());
//                BillOut.newLine();
//                String cAddr3 = " Erandwane, Pune 411 004.";
//                funPrintBlankSpace(cAddr3, BillOut);
//                BillOut.write(cAddr3.toUpperCase());
//                BillOut.newLine();
//                String cAddr4 = "Approved Caterers of";
//                funPrintBlankSpace(cAddr4, BillOut);
//                BillOut.write(cAddr4.toUpperCase());
//                BillOut.newLine();
//                String cAddr5 = "ROYAL CONNAUGHT BOAT CLUB";
//                funPrintBlankSpace(cAddr5, BillOut);
//                BillOut.write(cAddr5.toUpperCase());
//                BillOut.newLine();
//            }
//            else if (clsGlobalVarClass.gClientCode.equals("092.001") || clsGlobalVarClass.gClientCode.equals("092.002") || clsGlobalVarClass.gClientCode.equals("092.003"))//Shree Sound Pvt. Ltd.
//            {
//                funPrintBlankSpace("SSPL", BillOut);
//                BillOut.write("SSPL");
//                BillOut.newLine();
//                funPrintBlankSpace(clsGlobalVarClass.gClientAddress1, BillOut);
//                BillOut.write(clsGlobalVarClass.gClientAddress1.toUpperCase());
//                BillOut.newLine();
//                if (clsGlobalVarClass.gClientAddress2.trim().length() > 0)
//                {
//                    funPrintBlankSpace(clsGlobalVarClass.gClientAddress2, BillOut);
//                    BillOut.write(clsGlobalVarClass.gClientAddress2.toUpperCase());
//                    BillOut.newLine();
//                }
//                if (clsGlobalVarClass.gClientAddress3.trim().length() > 0)
//                {
//                    funPrintBlankSpace(clsGlobalVarClass.gClientAddress3, BillOut);
//                    BillOut.write(clsGlobalVarClass.gClientAddress3.toUpperCase());
//                    BillOut.newLine();
//                }
//                if (clsGlobalVarClass.gCityName.trim().length() > 0)
//                {
//                    funPrintBlankSpace(clsGlobalVarClass.gCityName, BillOut);
//                    BillOut.write(clsGlobalVarClass.gCityName.toUpperCase());
//                    BillOut.newLine();
//                }
//            }
//            else if (clsGlobalVarClass.gClientCode.equals("092.001") || clsGlobalVarClass.gClientCode.equals("092.002") || clsGlobalVarClass.gClientCode.equals("092.003"))//Shree Sound Pvt. Ltd.
//            {
//                funPrintBlankSpace("SSPL", BillOut);
//                BillOut.write("SSPL");
//                BillOut.newLine();
//                funPrintBlankSpace(clsGlobalVarClass.gClientAddress1, BillOut);
//                BillOut.write(clsGlobalVarClass.gClientAddress1.toUpperCase());
//                BillOut.newLine();
//                if (clsGlobalVarClass.gClientAddress2.trim().length() > 0)
//                {
//                    funPrintBlankSpace(clsGlobalVarClass.gClientAddress2, BillOut);
//                    BillOut.write(clsGlobalVarClass.gClientAddress2.toUpperCase());
//                    BillOut.newLine();
//                }
//                if (clsGlobalVarClass.gClientAddress3.trim().length() > 0)
//                {
//                    funPrintBlankSpace(clsGlobalVarClass.gClientAddress3, BillOut);
//                    BillOut.write(clsGlobalVarClass.gClientAddress3.toUpperCase());
//                    BillOut.newLine();
//                }
//                if (clsGlobalVarClass.gCityName.trim().length() > 0)
//                {
//                    funPrintBlankSpace(clsGlobalVarClass.gCityName, BillOut);
//                    BillOut.write(clsGlobalVarClass.gCityName.toUpperCase());
//                    BillOut.newLine();
//                }
//            }
//            else
//            {
//                funPrintBlankSpace(clsGlobalVarClass.gClientName, BillOut);
//                if (clsGlobalVarClass.gClientCode.equals("124.001"))
//                {
//                    BillOut.write(clsGlobalVarClass.gClientName);
//                }
//                else
//                {
//                    BillOut.write(clsGlobalVarClass.gClientName.toUpperCase());
//                }
//                BillOut.newLine();
//                funPrintBlankSpace(clsGlobalVarClass.gClientAddress1, BillOut);
//                BillOut.write(clsGlobalVarClass.gClientAddress1.toUpperCase());
//                BillOut.newLine();
//                if (clsGlobalVarClass.gClientAddress2.trim().length() > 0)
//                {
//                    funPrintBlankSpace(clsGlobalVarClass.gClientAddress2, BillOut);
//                    BillOut.write(clsGlobalVarClass.gClientAddress2.toUpperCase());
//                    BillOut.newLine();
//                }
//                if (clsGlobalVarClass.gClientAddress3.trim().length() > 0)
//                {
//                    funPrintBlankSpace(clsGlobalVarClass.gClientAddress3, BillOut);
//                    BillOut.write(clsGlobalVarClass.gClientAddress3.toUpperCase());
//                    BillOut.newLine();
//                }
//                if (clsGlobalVarClass.gCityName.trim().length() > 0)
//                {
//                    funPrintBlankSpace(clsGlobalVarClass.gCityName, BillOut);
//                    BillOut.write(clsGlobalVarClass.gCityName.toUpperCase());
//                    BillOut.newLine();
//                }
//            }
//            BillOut.write("  TEL NO.   :" + " ");
//            BillOut.write(String.valueOf(clsGlobalVarClass.gClientTelNo));
//            BillOut.newLine();
//            BillOut.write("  EMAIL ID  :" + " ");
//            BillOut.write(clsGlobalVarClass.gClientEmail);
//            BillOut.newLine();
//            tblName = rs_BillHD.getString(18);
//            if (tblName.length() > 0)
//            {
//                if (clsGlobalVarClass.gClientCode.equalsIgnoreCase("136.001"))//KINKI
//                {
//                    BillOut.write("  TABLE No   :");
//                }
//                else
//                {
//                    BillOut.write("  TABLE NAME:" + "  ");
//                }
//                BillOut.write(tblName);
//                BillOut.newLine();
//            }
//            waiterName = rs_BillHD.getString(19);
//            if (waiterName.trim().length() > 0)
//            {
//                BillOut.write("  STEWARD   :" + "  ");
//                BillOut.write(waiterName);
//                BillOut.newLine();
//            }
//            BillOut.write(Linefor5);
//            BillOut.newLine();
//            BillOut.write("  POS         : ");
//            BillOut.write(rs_BillHD.getString(16));
//            BillOut.newLine();
//            BillOut.write("  BILL NO.    : ");
//            BillOut.write(billNo);
//            BillOut.newLine();
//            BillOut.write("  PAX NO.     : ");
//            BillOut.write(rs_BillHD.getString(17));
//            BillOut.newLine();
//            if (clsGlobalVarClass.gPrintTimeOnBillYN)
//            {
//                BillOut.write("  DATE & TIME : ");
//                SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy hh:mm a ");
//                BillOut.write(ft.format(rs_BillHD.getObject(3)));
//                BillOut.newLine();
//
//            }
//            else
//            {
//                BillOut.write("  DATE        : ");
//                SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy");
//                BillOut.write(ft.format(rs_BillHD.getObject(3)));
//                BillOut.newLine();
//            }
//            if (rs_BillHD.getString(11).trim().length() > 0 && !flgComplimentaryBill)
//            {
//                BillOut.write("  Remarks     : ");
//                BillOut.write(rs_BillHD.getString(11));
//                BillOut.newLine();
//            }
//            subTotal = rs_BillHD.getString(6);
//            grandTotal = rs_BillHD.getString(8);
//            user = rs_BillHD.getString(12);
//            deliveryCharge = rs_BillHD.getString(13);
//            advAmount = rs_BillHD.getString(14);
//            //print card available balance
//            String isSttled = "select a.strBillNo from " + billSettlementdtl + " a," + billhd + " b "
//                    + " where a.strBillNo=b.strBillNo and a.strClientCode=b.strClientCode "
//                    + " and a.strBillNo='" + billNo + "' and b.strPOSCode='" + POSCode + "' ";
//            ResultSet rsIsSettled = clsGlobalVarClass.dbMysql.executeResultSet(isSttled);
//            if (rsIsSettled.next())
//            {
//                rsIsSettled.close();
//                String availBal = "select a.strCardNo,(b.dblRedeemAmt)"
//                        + "from " + billhd + " a inner join tbldebitcardmaster b on a.strCardNo=b.strCardNo "
//                        + "where a.strBillNo='" + billNo + "' and a.strPOSCode='" + POSCode + "'; ";
//                ResultSet rsAvailBal = clsGlobalVarClass.dbMysql.executeResultSet(availBal);
//                if (rsAvailBal.next())
//                {
//                    BillOut.write("  Available Balance(" + rsAvailBal.getString(1) + "):" + rsAvailBal.getString(2));
//                    BillOut.newLine();
//                }
//            }
//            else
//            {
//                String availBal = "select a.strCardNo,(b.dblRedeemAmt-a.dblGrandTotal)"
//                        + "from " + billhd + " a inner join tbldebitcardmaster b on a.strCardNo=b.strCardNo "
//                        + "where a.strBillNo='" + billNo + "' and a.strPOSCode='" + POSCode + "'; ";
//                ResultSet rsAvailBal = clsGlobalVarClass.dbMysql.executeResultSet(availBal);
//                if (rsAvailBal.next())
//                {
//                    BillOut.write("  Available Balance(" + rsAvailBal.getString(1) + "):" + rsAvailBal.getString(2));
//                    BillOut.newLine();
//                }
//            }
//            //print card available balance
//            if (transType.equals("Void"))
//            {
//                BillOut.write("  Reason      :" + " " + rs_BillHD.getString(22));//voided reason
//                BillOut.newLine();
//            }
//            else if (flgComplimentaryBill)
//            {
//
//                BillOut.write("  Reason      :" + " " + rs_BillHD.getString(23));
//                BillOut.newLine();
//                BillOut.write("  Remark      :" + " " + rs_BillHD.getString(11));
//                BillOut.newLine();
//            }
//            if (clsGlobalVarClass.gCMSIntegrationYN)
//            {
//                BillOut.write("  Member Code : ");
//                BillOut.write(rs_BillHD.getString(7));
//                BillOut.newLine();
//                BillOut.write("  Member Name : ");
//                objUtility2.funWriteToTextMemberNameForFormat5(BillOut, rs_BillHD.getString(24), "Format5");
//                BillOut.newLine();
//                BillOut.write(Linefor5);
//            }
//            if (rs_BillHD.getString(25) != null && rs_BillHD.getString(25).length() > 0)
//            {
//                if (rs_BillHD.getString(26).length() > 0 || rs_BillHD.getString(27).length() > 0 || rs_BillHD.getString(28).length() > 0)
//                {
//                    BillOut.newLine();
//                    funPrintBlankSpace("ORDER DETAIL", BillOut);
//                    BillOut.write("ORDER DETAIL");
//                    BillOut.newLine();
//                    BillOut.write(Linefor5);
//                    BillOut.newLine();
//                }
//                StringBuilder strValue = new StringBuilder();
//                strValue.setLength(0);
//                if (rs_BillHD.getString(26).length() > 0)
//                {
//                    strValue.append(rs_BillHD.getString(26));
//                }
//                else
//                {
//                    strValue.append("");
//                }
//                int strlenMsg = strValue.length();
//                if (strlenMsg > 0)
//                {
//                    String msg1 = "";
//                    if (strlenMsg < 27)
//                    {
//                        msg1 = strValue.substring(0, strlenMsg);
//                        BillOut.write("  MESSAGE     :" + msg1);
//                        BillOut.newLine();
//                    }
//                    else
//                    {
//                        msg1 = strValue.substring(0, 27);
//                        BillOut.write("  MESSAGE     :" + msg1);;
//                        BillOut.newLine();
//                    }
//                    for (int i = 27; i <= strlenMsg; i++)
//                    {
//                        int endmsg = 0;
//                        endmsg = i + 27;
//                        if (strlenMsg > endmsg)
//                        {
//                            msg1 = strValue.substring(i, endmsg);
//                            i = endmsg;
//                            BillOut.write("               " + msg1);
//                            BillOut.newLine();
//                        }
//                        else
//                        {
//                            msg1 = strValue.substring(i, strlenMsg);
//                            BillOut.write("               " + msg1);
//                            BillOut.newLine();
//                            i = strlenMsg + 1;
//                        }
//                    }
//                }
//                strValue.setLength(0);
//                if (rs_BillHD.getString(27).length() > 0)//shape
//                {
//                    strValue.append(rs_BillHD.getString(27));
//                }
//                else
//                {
//                    strValue.append("");
//                }
//                int strlenShape = strValue.length();
//                if (strlenShape > 0)
//                {
//                    String shape1 = "";
//                    if (strlenShape < 27)
//                    {
//                        shape1 = strValue.substring(0, strlenShape);
//                        BillOut.write("  SHAPE       :" + shape1);
//                        BillOut.newLine();
//                    }
//                    else
//                    {
//                        shape1 = strValue.substring(0, 27);
//                        BillOut.write("  SHAPE       :" + shape1);
//                        BillOut.newLine();
//                    }
//                    for (int j = 27; j <= strlenShape; j++)
//                    {
//                        int endShape = 0;
//                        endShape = j + 27;
//                        if (strlenShape > endShape)
//                        {
//                            shape1 = strValue.substring(j, endShape);
//                            j = endShape;
//                            BillOut.write("               " + shape1);
//                            BillOut.newLine();
//                        }
//                        else
//                        {
//                            shape1 = strValue.substring(j, strlenShape);
//                            BillOut.write("               " + shape1);
//                            BillOut.newLine();
//                            j = strlenShape + 1;
//                        }
//                    }
//                }
//
//                strValue.setLength(0);
//                if (rs_BillHD.getString(28).length() > 0)//note
//                {
//                    strValue.append(rs_BillHD.getString(28));
//                }
//                else
//                {
//                    strValue.append("");
//                }
//                int strlenNote = strValue.length();
//                if (strlenNote > 0)
//                {
//                    String note1 = "";
//                    if (strlenNote < 27)
//                    {
//                        note1 = strValue.substring(0, strlenNote);
//                        BillOut.write("  NOTE        :" + note1);
//                        BillOut.newLine();
//                    }
//                    else
//                    {
//                        note1 = strValue.substring(0, 27);
//                        BillOut.write("  NOTE        :" + note1);
//                        BillOut.newLine();
//                    }
//                    for (int i = 27; i <= strlenNote; i++)
//                    {
//                        int endNote = 0;
//                        endNote = i + 27;
//                        if (strlenNote > endNote)
//                        {
//                            note1 = strValue.substring(i, endNote);
//                            i = endNote;
//                            BillOut.write("               " + note1);
//                            BillOut.newLine();
//                        }
//                        else
//                        {
//                            note1 = strValue.substring(i, strlenNote);
//                            BillOut.write("               " + note1);
//                            BillOut.newLine();
//                            i = strlenNote + 1;
//                        }
//                    }
//                }
//                if (rs_BillHD.getString(26).length() > 0 || rs_BillHD.getString(27).length() > 0 || rs_BillHD.getString(28).length() > 0)
//                {
//
//                    BillOut.write(Linefor5);
//                    BillOut.newLine();
//                }
//            }
//
//            BillOut.write(Linefor5);
//            BillOut.newLine();
//            BillOut.write("     QTY ITEM NAME                  AMT");
//            BillOut.newLine();
//            BillOut.write(Linefor5);
//            BillOut.newLine();
//            String SQL_BillDtl = "select sum(a.dblQuantity),left(a.strItemName,22) as ItemLine1"
//                    + " ,MID(a.strItemName,23,LENGTH(a.strItemName)) as ItemLine2"
//                    + " ,sum(a.dblAmount),a.strItemCode,a.strKOTNo,c.strItemType "
//                    + " from " + billdtl + " a," + billhd + " b,tblitemmaster c "
//                    + " where a.strBillNo=b.strBillNo "
//                    + " and a.strClientCode=b.strClientCode "
//                    + " and a.strItemCode=c.strItemCode "
//                    + " and a.strBillNo=? "
//                    + " and b.strPOSCode=?  ";
//            if (!clsGlobalVarClass.gPrintTDHItemsInBill)
//            {
//                SQL_BillDtl += "and a.tdhYN='N' ";
//            }
//            if (!clsGlobalVarClass.gPrintOpenItemsOnBill)
//            {
//                SQL_BillDtl += "and a.dblAmount>0 ";
//            }
//            SQL_BillDtl += " group by a.strItemCode ";
//            pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(SQL_BillDtl);
//            pst.setString(1, billNo);
//            pst.setString(2, POSCode);
//            ResultSet rs_BillDtl = pst.executeQuery();
//            while (rs_BillDtl.next())
//            {
//
//                String itemLine1 = rs_BillDtl.getString(2);//item line 1
//                String itemLine2 = rs_BillDtl.getString(3);//item line 2
//                String itemType = rs_BillDtl.getString(7);//item Type
//                if (itemType.equalsIgnoreCase("LIQUER") || itemType.equalsIgnoreCase("LIQUOR"))
//                {
//                    itemLine1 = "BEVERAGES";
//                    itemLine2 = "";
//                }
//
//                double saleQty = rs_BillDtl.getDouble(1);
//                String sqlPromoBills = "select dblQuantity from " + billPromoDtl + " "
//                        + " where strBillNo='" + billNo + "' and strItemCode='" + rs_BillDtl.getString(5) + "' "
//                        + " and strPromoType='ItemWise' ";
//                ResultSet rsPromoItems = clsGlobalVarClass.dbMysql.executeResultSet(sqlPromoBills);
//                if (rsPromoItems.next())
//                {
//                    saleQty -= rsPromoItems.getDouble(1);
//                }
//                rsPromoItems.close();
//                String qty = String.valueOf(saleQty);
//                if (qty.contains("."))
//                {
//                    String decVal = qty.substring(qty.length() - 2, qty.length());
//                    if (Double.parseDouble(decVal) == 0)
//                    {
//                        qty = qty.substring(0, qty.length() - 2);
//                    }
//                }
//                if (saleQty > 0)
//                {
//                    objUtility2.funPrintContentWithSpace("Right", qty, 8, BillOut);//Qty Print
//                    BillOut.write(" ");
//                    objUtility2.funPrintContentWithSpace("Left", itemLine1, 22, BillOut);//Item Name
//                    if (flgComplimentaryBill)
//                    {
//                        objUtility2.funPrintContentWithSpace("Right", "0.00", 9, BillOut);//Amount
//                    }
//                    else
//                    {
//                        objUtility2.funPrintContentWithSpace("Right", rs_BillDtl.getString(4), 9, BillOut);//Amount
//                    }
//                    BillOut.newLine();
//                    if (itemLine2.trim().length() > 0)
//                    {
//                        String line = itemLine2;
//                        if (line.length() > 22)
//                        {
//                            BillOut.write("         " + line.substring(0, 22));
//                            BillOut.newLine();
//
//                            BillOut.write("         " + line.substring(22, line.length()));
//                            BillOut.newLine();
//                        }
//                        else
//                        {
//                            BillOut.write("         " + line);
//                            BillOut.newLine();
//                        }
//                    }
//                    String sqlModifier = "select count(*) "
//                            + "from " + billModifierdtl + " where strBillNo=? and left(strItemCode,7)=? ";
//                    if (!clsGlobalVarClass.gPrintZeroAmtModifierOnBill)
//                    {
//                        sqlModifier += " and  dblAmount !=0.00 ";
//                    }
//                    pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlModifier);
//                    pst.setString(1, billNo);
//                    pst.setString(2, rs_BillDtl.getString(5));
//                    ResultSet rs_count = pst.executeQuery();
//                    rs_count.next();
//                    int cntRecord = rs_count.getInt(1);
//                    rs_count.close();
//                    if (cntRecord > 0)
//                    {
//                        sqlModifier = "select strModifierName,dblQuantity,dblAmount "
//                                + " from " + billModifierdtl + " "
//                                + " where strBillNo=? and left(strItemCode,7)=? ";
//                        if (!clsGlobalVarClass.gPrintZeroAmtModifierOnBill)
//                        {
//                            sqlModifier += " and  dblAmount !=0.00 ";
//                        }
//                        pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlModifier);
//                        pst.setString(1, billNo);
//                        pst.setString(2, rs_BillDtl.getString(5));
//                        ResultSet rs_modifierRecord = pst.executeQuery();
//                        while (rs_modifierRecord.next())
//                        {
//                            if (flgComplimentaryBill)
//                            {
//                                objUtility2.funWriteToTextformat5(BillOut, "", rs_modifierRecord.getString(1).toUpperCase(), "0.00", "Format5");
//                                BillOut.newLine();
//                            }
//                            else
//                            {
//                                objUtility2.funWriteToTextformat5(BillOut, "", rs_modifierRecord.getString(1).toUpperCase(), rs_modifierRecord.getString(3), "Format5");
//                                BillOut.newLine();
//                            }
//                        }
//                        rs_modifierRecord.close();
//                    }
//
//                    sql = "select b.strItemCode,b.dblWeight "
//                            + " from " + billhd + " a," + advBookBillDtl + " b "
//                            + " where a.strAdvBookingNo=b.strAdvBookingNo and a.strClientCode=b.strClientCode "
//                            + " and a.strBillNo='" + billNo + "' and b.strItemCode='" + rs_BillDtl.getString(5) + "' "
//                            + " and a.strPOSCode='" + POSCode + "' ";
//                    ResultSet rsWeight = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//                    while (rsWeight.next())
//                    {
//                        BillOut.write("     Weight");
//                        BillOut.write("     " + rsWeight.getDouble(2));
//                        BillOut.newLine();
//                    }
//                    rsWeight.close();
//                    sql = "select c.strCharName,b.strCharValues "
//                            + " from " + billhd + " a," + advBookBillCharDtl + " b,tblcharactersticsmaster c "
//                            + " where a.strAdvBookingNo=b.strAdvBookingNo and b.strCharCode=c.strCharCode "
//                            + " and a.strBillNo='" + billNo + "' and b.strItemCode='" + rs_BillDtl.getString(5) + "' "
//                            + " and a.strPOSCode='" + POSCode + "' and a.strClientCode=b.strClientCode ";
//                    ResultSet rsCharDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//                    while (rsCharDtl.next())
//                    {
//                        String charName = objUtility.funPrintTextWithAlignment(rsCharDtl.getString(1), 12, "Left");
//                        BillOut.write("     " + charName);
//                        String charVal = objUtility.funPrintTextWithAlignment(rsCharDtl.getString(2), 28, "Left");
//                        BillOut.write("     " + charVal);
//                        BillOut.newLine();
//                    }
//                    rsCharDtl.close();
//                }
//            }
//            rs_BillDtl.close();
//            funPrintPromoItemsInBill(billNo, BillOut, 4);  // Print Promotion Items in Bill for this billno.
//            BillOut.write(Linefor5);
//            BillOut.newLine();
//            if (clsGlobalVarClass.gPointsOnBillPrint)
//            {
//                String sqlCRMPoints = "select b.dblPoints from " + billhd + " a, tblcrmpoints b "
//                        + " where a.strBillNo=b.strBillNo and a.strClientCode=b.strClientCode "
//                        + " and a.strBillNo='" + billNo + "' and a.strPOSCode='" + POSCode + "' ";
//                ResultSet rsCRMPoints = clsGlobalVarClass.dbMysql.executeResultSet(sqlCRMPoints);
//                if (rsCRMPoints.next())
//                {
//                    funWriteTotal("POINTS ", rsCRMPoints.getString(1), BillOut, "Format5");
//                }
//                rsCRMPoints.close();
//                BillOut.newLine();
//            }
//            if (flgComplimentaryBill)
//            {
//                funWriteTotal("SUB TOTAL", "0.00", BillOut, "Format5");
//                BillOut.newLine();
//            }
//            else
//            {
//                funWriteTotal("SUB TOTAL", subTotal, BillOut, "Format5");
//                BillOut.newLine();
//            }
//            sql = "select a.dblDiscPer,a.dblDiscAmt,a.strDiscOnType,a.strDiscOnValue,b.strReasonName,a.strDiscRemarks "
//                    + " from " + billDscFrom + " a ,tblreasonmaster b," + billhd + " c "
//                    + " where  a.strDiscReasonCode=b.strReasonCode and a.strBillNo=c.strBillNo "
//                    + " and a.strClientCode=c.strClientCode and a.strBillNo='" + billNo + "' and c.strPOSCode='" + POSCode + "' ";
//            ResultSet rsDisc = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//            boolean flag = true;
//            while (rsDisc.next())
//            {
//                if (flag)
//                {
//                    flag = false;
//                    BillOut.write("  DISCOUNT");
//                    BillOut.newLine();
//                }
//                double dbl = Double.parseDouble(rsDisc.getString("dblDiscPer"));
//                String discText = String.format("%.1f", dbl) + "%" + " On " + rsDisc.getString("strDiscOnValue") + "";
//                if (discText.length() > 30)
//                {
//                    discText = discText.substring(0, 30);
//                }
//                else
//                {
//                    discText = String.format("%-30s", discText);
//                }
//                BillOut.write("  " + discText);
//                String discountOnItem = objUtility.funPrintTextWithAlignment(rsDisc.getString("dblDiscAmt"), 8, "Right");
//                BillOut.write(discountOnItem);
//                BillOut.newLine();
//                BillOut.write("  Reason  : ");
//                String discReason = objUtility.funPrintTextWithAlignment(rsDisc.getString(5), 20, "Left");
//                BillOut.write(discReason);
//                BillOut.newLine();
//                BillOut.write("  Remarks : ");
//                String discRemarks = objUtility.funPrintTextWithAlignment(rsDisc.getString(6), 20, "Left");
//                BillOut.write(discRemarks);
//                BillOut.newLine();
//
//            }
//            String sql_Tax = "select b.strTaxDesc,sum(a.dblTaxAmount) "
//                    + " from " + billtaxdtl + " a,tbltaxhd b," + billhd + " c "
//                    + " where a.strBillNo='" + billNo + "' "
//                    + " and a.strTaxCode=b.strTaxCode "
//                    + " and a.strBillNo=c.strBillNo "
//                    + " and a.strClientCode=c.strClientCode "
//                    + " and c.strPOSCode='" + POSCode + "' "
//                    + " and b.strTaxCalculation='Forward' "
//                    + " group by a.strTaxCode";
//            ResultSet rsTax = clsGlobalVarClass.dbMysql.executeResultSet(sql_Tax);
//            while (rsTax.next())
//            {
//                if (flgComplimentaryBill)
//                {
//                    funWriteTotal(rsTax.getString(1), "0.00", BillOut, "Format5");
//                    BillOut.newLine();
//                }
//                else
//                {
//                    funWriteTotal(rsTax.getString(1), rsTax.getString(2), BillOut, "Format5");
//                    BillOut.newLine();
//                }
//            }
//            if (deliveryCharge != null && deliveryCharge.trim().length() > 0 && !"0.00".equalsIgnoreCase(deliveryCharge))
//            {
//                funWriteTotal("DELV. CHARGE", deliveryCharge, BillOut, "Format5");
//                BillOut.newLine();
//            }
//            if (advAmount.trim().length() > 0 && !"0.00".equalsIgnoreCase(advAmount))
//            {
//                funWriteTotal("ADVANCE", advAmount, BillOut, "Format5");
//                BillOut.newLine();
//            }
//            BillOut.write(Linefor5);
//            BillOut.newLine();
//            if (flgComplimentaryBill)
//            {
//                funWriteTotal("TOTAL(ROUNDED)", "0.00", BillOut, "Format5");
//                BillOut.newLine();
//                BillOut.write(Linefor5);
//            }
//            else
//            {
//                funWriteTotal("TOTAL(ROUNDED)", grandTotal, BillOut, "Format5");
//                BillOut.newLine();
//                BillOut.write(Linefor5);
//            }
//
//            //print Grand total of other bill nos from bill series
//            if (clsGlobalVarClass.gEnableBillSeries)
//            {
//                String sqlPrintGT = "select a.strPrintGTOfOtherBills,b.strDtlBillNos,b.dblGrandTotal "
//                        + "from tblbillseries a,tblbillseriesbilldtl b "
//                        + "where (a.strPOSCode=b.strPOSCode or a.strPOSCode='All') "
//                        + "and a.strBillSeries=b.strBillSeries "
//                        + "and b.strHdBillNo='" + billNo + "' and b.strPOSCode='" + POSCode + "' ";
//                ResultSet rsPrintGTYN = clsGlobalVarClass.dbMysql.executeResultSet(sqlPrintGT);
//                double dblOtherBillsGT = 0.00;
//                if (rsPrintGTYN.next())
//                {
//                    if (rsPrintGTYN.getString(1).equalsIgnoreCase("Y"))
//                    {
//                        String billSeriesDtlBillNos = rsPrintGTYN.getString(2);
//                        String[] dtlBillSeriesBillNo = billSeriesDtlBillNos.split(",");
//                        dblOtherBillsGT += rsPrintGTYN.getDouble(3);
//                        if (dtlBillSeriesBillNo.length > 0)
//                        {
//                            for (int i = 0; i < dtlBillSeriesBillNo.length; i++)
//                            {
//                                sqlPrintGT = "select a.strHdBillNo,a.dblGrandTotal "
//                                        + "from tblbillseriesbilldtl a "
//                                        + "where a.strHdBillNo='" + dtlBillSeriesBillNo[i] + "' and a.strPOSCode='" + POSCode + "' ";
//                                ResultSet rsPrintGT = clsGlobalVarClass.dbMysql.executeResultSet(sqlPrintGT);
//                                if (rsPrintGT.next())
//                                {
//                                    BillOut.newLine();
//                                    funWriteTotal(dtlBillSeriesBillNo[i] + " TOTAL(ROUNDED)", rsPrintGT.getString(2), BillOut, "Format5");
//                                    dblOtherBillsGT += rsPrintGT.getDouble(2);
//                                    BillOut.newLine();
//                                }
//                            }
//                            BillOut.newLine();
//                            BillOut.write(Linefor5);
//                            BillOut.newLine();
//                            funWriteTotal("GRAND TOTAL(ROUNDED)", String.valueOf(dblOtherBillsGT), BillOut, "Format5");
//                            BillOut.newLine();
//                            BillOut.write(Linefor5);
//                            BillOut.newLine();
//                        }
//                    }
//                }
//            }
//
//            //settlement breakup part
//            String sqlSettlementBreakup = "select a.dblSettlementAmt, b.strSettelmentDesc, b.strSettelmentType "
//                    + " from " + billSettlementdtl + " a ,tblsettelmenthd b," + billhd + " c "
//                    + " where a.strBillNo=? and a.strBillNo=c.strBillNo and a.strClientCode=c.strClientCode "
//                    + " and a.strSettlementCode=b.strSettelmentCode and c.strPOSCode=? ";
//            pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlSettlementBreakup);
//            pst.setString(1, billNo);
//            pst.setString(2, POSCode);
//            boolean flgSettlement = false;
//            boolean creditSettlement = false;
//            ResultSet rsBillSettlement = pst.executeQuery();
//            while (rsBillSettlement.next())
//            {
//                if (flgComplimentaryBill)
//                {
//                    BillOut.newLine();
//                    funWriteTotal(rsBillSettlement.getString(2), "0.00", BillOut, "Format5");
//                }
//                else
//                {
//                    BillOut.newLine();
//                    funWriteTotal(rsBillSettlement.getString(2), rsBillSettlement.getString(1), BillOut, "Format5");
//                }
//                flgSettlement = true;
//                if (rsBillSettlement.getString(3).equals("Credit"))
//                {
//                    creditSettlement = true;
//                }
//            }
//            rsBillSettlement.close();
//
//            if (flgSettlement)
//            {
//                BillOut.newLine();
//                if (creditSettlement)
//                {
//                    funWriteTotal("Credit Remarks ", rs_BillHD.getString(11), BillOut, "Format5");
//                    BillOut.newLine();
//                    String custName = rs_BillHD.getString(24);
//                    if (!custName.isEmpty())
//                    {
//                        funWriteTotal("Customer " + custName, "", BillOut, "Format5");
//                    }
//                    BillOut.newLine();
//                    BillOut.write(Linefor5);
//                }
//            }
//
//            String sqlTenderAmt = "select sum(a.dblPaidAmt),sum(a.dblSettlementAmt),(sum(a.dblPaidAmt)-sum(a.dblSettlementAmt)) RefundAmt "
//                    + " from " + billSettlementdtl + " a," + billhd + " b "
//                    + " where a.strBillNo=b.strBillNo and a.strClientCode=b.strClientCode "
//                    + " and b.strBillNo='" + billNo + "' and b.strPOSCode='" + POSCode + "' "
//                    + " group by a.strBillNo";
//            ResultSet rsTenderAmt = clsGlobalVarClass.dbMysql.executeResultSet(sqlTenderAmt);
//            if (rsTenderAmt.next())
//            {
//                BillOut.newLine();
//                if (flgComplimentaryBill)
//                {
//                    funWriteTotal("PAID AMT", "0.00", BillOut, "Format5");
//                    BillOut.newLine();
//                }
//                else
//                {
//                    funWriteTotal("PAID AMT", rsTenderAmt.getString(1), BillOut, "Format5");
//                    BillOut.newLine();
//                    if (rsTenderAmt.getDouble(3) > 0)
//                    {
//                        funWriteTotal("REFUND AMT", rsTenderAmt.getString(3), BillOut, "Format5");
//                        BillOut.newLine();
//                    }
//                }
//                BillOut.write(Linefor5);
//            }
//            rsTenderAmt.close();
//
//            if (rs_BillHD.getDouble(29) > 0)
//            {
//                BillOut.newLine();
//                funWriteTotal("TIP AMT", rs_BillHD.getString(29), BillOut, "Format5");
//                BillOut.newLine();
//            }
//            if (flag_isHomeDelvBill)
//            {
//                BillOut.newLine();
//                String sql_count = "select count(*) from tblhomedelivery where strCustomerCode=?";
//                pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sql_count);
//                pst.setString(1, customerCode);
//                ResultSet rs_Count = pst.executeQuery();
//                rs_Count.next();
//                BillOut.write("  CUSTOMER COUNT : " + rs_Count.getString(1));
//                rs_Count.close();
//                BillOut.newLine();
//                BillOut.write(Linefor5);
//            }
//            BillOut.newLine();
//
//            funPrintServiceVatNo(BillOut, 4, billNo, billDate, billtaxdtl);
//
//            if (clsGlobalVarClass.gEnableBillSeries)
//            {
//                sql = "select b.strPrintInclusiveOfTaxOnBill "
//                        + " from tblbillseriesbilldtl a,tblbillseries b "
//                        + " where a.strBillSeries=b.strBillSeries and a.strHdBillNo='" + billNo + "' and a.strClientCode=b.strClientCode";
//                ResultSet rsBillSeries = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//                if (rsBillSeries.next())
//                {
//                    if (rsBillSeries.getString(1).equals("Y"))
//                    {
//                        BillOut.write(Line);
//                        BillOut.newLine();
//                        funPrintBlankSpace("(INCLUSIVE OF ALL TAXES)", BillOut);
//                        BillOut.write("(INCLUSIVE OF ALL TAXES)");
//                        BillOut.newLine();
//                    }
//                }
//                rsBillSeries.close();
//            }
//            else
//            {
//                if (clsGlobalVarClass.gPrintInclusiveOfAllTaxes.equalsIgnoreCase("Y"))
//                {
//                    BillOut.write(Line);
//                    BillOut.newLine();
//                    funPrintBlankSpace("(INCLUSIVE OF ALL TAXES)", BillOut);
//                    BillOut.write("(INCLUSIVE OF ALL TAXES)");
//                    BillOut.newLine();
//                }
//            }
//
//            int num = clsGlobalVarClass.gBillFooter.trim().length() / 30;
//            int num1 = clsGlobalVarClass.gBillFooter.trim().length() % 30;
//            int cnt1 = 0;
//            for (int cnt = 0; cnt < num; cnt++)
//            {
//                String footer = clsGlobalVarClass.gBillFooter.trim().substring(cnt1, (cnt1 + 30));
//                footer = footer.replaceAll("\n", "");
//                BillOut.write("     " + footer.trim());
//                BillOut.newLine();
//                cnt1 += 30;
//            }
//            BillOut.write("     " + clsGlobalVarClass.gBillFooter.trim().substring(cnt1, (cnt1 + num1)).trim());
//            BillOut.newLine();
//            funPrintBlankSpace(user, BillOut);
//            BillOut.write(user);
//            BillOut.newLine();
//            BillOut.newLine();
//            BillOut.newLine();
//            BillOut.newLine();
//            BillOut.newLine();
//
//            if (!clsGlobalVarClass.gOpenCashDrawerAfterBillPrintYN)
//            {
//                if ("linux".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
//                {
//                    BillOut.write("V");//Linux
//                }
//                else if ("windows".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
//                {
//                    if ("Inbuild".equalsIgnoreCase(clsPosConfigFile.gPrinterType))
//                    {
//                        BillOut.write("V");
//                    }
//                    else
//                    {
//                        BillOut.write("m");//windows
//                    }
//                }
//            }
//            rs_BillHD.close();
//            BillOut.close();
//            fstream_bill.close();
//            pst.close();
//
//            if (formName.equalsIgnoreCase("sales report"))
//            {
//                funShowTextFile(Text_Bill, formName, clsGlobalVarClass.gBillPrintPrinterPort);
//            }
//            else
//            {
//                if (clsGlobalVarClass.gShowBill)
//                {
//                    funShowTextFile(Text_Bill, formName, clsGlobalVarClass.gBillPrintPrinterPort);
//                }
//            }
//
//            if (!formName.equalsIgnoreCase("sales report"))
//            {
//                if (transType.equalsIgnoreCase("void"))
//                {
//                    if (clsGlobalVarClass.gPrintOnVoidBill)
//                    {
//                        if (!viewORprint.equalsIgnoreCase("view"))
//                        {
//                            funPrintToPrinter(clsGlobalVarClass.gBillPrintPrinterPort, "", "bill", "N", isReprint);
//                        }
//                    }
//                }
//                else
//                {
//                    if (!clsGlobalVarClass.flgReprintView)
//                    {
//                        if (!viewORprint.equalsIgnoreCase("view"))
//                        {
//                            funPrintToPrinter(clsGlobalVarClass.gBillPrintPrinterPort, "", "bill", "N", isReprint);
//                        }
//                    }
//                    else
//                    {
//                        clsGlobalVarClass.flgReprintView = false;
//                    }
//                }
//            }
//            //if (formName.equalsIgnoreCase("sales report"))
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    /*
//     * Text Bill format 17 is same as text bill format5 with Table No instead of
//     * Table Name and print items it's group wise
//     */
//    public void funGenerateTextFileBillPrintingForFormat17(String billNo, String reprint, String formName, String transType, String billDate, String POSCode, String viewORprint)
//    {
//        clsUtility objUtility = new clsUtility();
//        clsUtility2 objUtility2 = new clsUtility2();
//
//        String Linefor5 = "  --------------------------------------";
//        try
//        {
//            String user = "";
//            String billhd = null;
//            String billdtl = null;
//            String billModifierdtl = null;
//            String billSettlementdtl = null;
//            String billtaxdtl = null;
//            String billDscFrom = null;
//            String billPromoDtl = null;
//            String advBookBillHd = null;
//            String advBookBillDtl = null;
//            String advBookBillCharDtl = null;
//            String advReceiptHd = null;
//
//            boolean isCustomerPrint = false;
//
//            if (clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("HOPOS"))
//            {
//                billhd = "tblqbillhd";
//                billdtl = "tblqbilldtl";
//                billModifierdtl = "tblqbillmodifierdtl";
//                billSettlementdtl = "tblqbillsettlementdtl";
//                billtaxdtl = "tblqbilltaxdtl";
//                billDscFrom = "tblqbilldiscdtl";
//                billPromoDtl = "tblqbillpromotiondtl";
//
//                advBookBillHd = "tblqadvbookbillhd";
//                advBookBillDtl = "tblqadvbookbilldtl";
//                advBookBillCharDtl = "tblqadvbookbillchardtl";
//                advReceiptHd = "tblqadvancereceipthd";
//            }
//            else
//            {
//                if ("sales report".equalsIgnoreCase(formName))
//                {
//                    billhd = "tblbillhd";
//                    billdtl = "tblbilldtl";
//                    billModifierdtl = "tblbillmodifierdtl";
//                    billSettlementdtl = "tblbillsettlementdtl";
//                    billtaxdtl = "tblbilltaxdtl";
//                    billDscFrom = "tblbilldiscdtl";
//                    billPromoDtl = "tblbillpromotiondtl";
//                    advBookBillHd = "tbladvbookbillhd";
//                    advBookBillDtl = "tbladvbookbilldtl";
//                    advBookBillCharDtl = "tbladvbookbillchardtl";
//                    advReceiptHd = "tbladvancereceipthd";
//                    long dateDiff = new clsUtility().funCompareDate(billDate, objUtility.funGetPOSDateForTransaction());
//                    if (dateDiff > 0)
//                    {
//                        billhd = "tblqbillhd";
//                        billdtl = "tblqbilldtl";
//                        billModifierdtl = "tblqbillmodifierdtl";
//                        billSettlementdtl = "tblqbillsettlementdtl";
//                        billtaxdtl = "tblqbilltaxdtl";
//                        billDscFrom = "tblqbilldiscdtl";
//                        billPromoDtl = "tblqbillpromotiondtl";
//                        advBookBillHd = "tblqadvbookbillhd";
//                        advBookBillDtl = "tblqadvbookbilldtl";
//                        advBookBillCharDtl = "tblqadvbookbillchardtl";
//                        advReceiptHd = "tblqadvancereceipthd";
//                    }
//                    String sql = "select count(strBillNo) from tblbillhd where strBillNo='" + billNo + "' and strPOSCode='" + POSCode + "' ";
//                    ResultSet rsBillTable = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//                    rsBillTable.next();
//                    int billCnt = rsBillTable.getInt(1);
//                    if (billCnt == 0)
//                    {
//                        billhd = "tblqbillhd";
//                        billdtl = "tblqbilldtl";
//                        billModifierdtl = "tblqbillmodifierdtl";
//                        billSettlementdtl = "tblqbillsettlementdtl";
//                        billtaxdtl = "tblqbilltaxdtl";
//                        billDscFrom = "tblqbilldiscdtl";
//                        billPromoDtl = "tblqbillpromotiondtl";
//                        advBookBillHd = "tblqadvbookbillhd";
//                        advBookBillDtl = "tblqadvbookbilldtl";
//                        advBookBillCharDtl = "tblqadvbookbillchardtl";
//                        advReceiptHd = "tblqadvancereceipthd";
//                    }
//                }
//                else
//                {
//                    billhd = "tblbillhd";
//                    billdtl = "tblbilldtl";
//                    billModifierdtl = "tblbillmodifierdtl";
//                    billSettlementdtl = "tblbillsettlementdtl";
//                    billtaxdtl = "tblbilltaxdtl";
//                    billDscFrom = "tblbilldiscdtl";
//                    billPromoDtl = "tblbillpromotiondtl";
//                    advBookBillHd = "tbladvbookbillhd";
//                    advBookBillDtl = "tbladvbookbilldtl";
//                    advBookBillCharDtl = "tbladvbookbillchardtl";
//                    advReceiptHd = "tbladvancereceipthd";
//                }
//            }
//            PreparedStatement pst = null;
//            objUtility2.funCreateTempFolder();
//            String filePath = System.getProperty("user.dir");
//            File Text_Bill = new File(filePath + "/Temp/Temp_Bill.txt");
//            String subTotal = "";
//            String grandTotal = "";
//            String advAmount = "";
//            String deliveryCharge = "";
//            String customerCode = "";
//            String waiterName = "";
//            String tblName = "";
//            ResultSet rs_BillHD = null;
//            boolean flgComplimentaryBill = false;
//            StringBuilder sqlBillHeaderDtl = new StringBuilder();
//            sqlBillHeaderDtl.append("select ifnull(a.strTableNo,''),ifnull(a.strWaiterNo,''),a.dteBillDate,time(a.dteBillDate),a.dblDiscountAmt,a.dblSubTotal,"
//                    + "ifnull(a.strCustomerCode,''),a.dblGrandTotal,a.dblTaxAmt,ifnull(a.strReasonCode,''),ifnull(a.strRemarks,''),a.strUserCreated "
//                    + ",ifnull(dblDeliveryCharges,0.00),ifnull(i.dblAdvDeposite,0.00),a.dblDiscountPer,b.strPOSName,a.intPaxNo "
//                    + ",ifnull(c.strTableName,''),ifnull(d.strWShortName,''),ifnull(d.strWFullName,''),ifnull(l.strSettelmentType,''),ifnull(j.strReasonName,'') as voidedReason, "
//                    + "ifnull(g.strReasonName,''),ifnull(e.strCustomerName,''),ifnull(a.strAdvBookingNo,''),ifnull(h.strMessage,''),ifnull(h.strShape,''),ifnull(h.strNote,''),ifnull(a.dblTipAmount,0.00) "
//                    + ",a.strOperationType,ifnull(a.strTakeAwayRemarks,''),ifnull(e.longMobileNo,'')  "
//                    + "from " + billhd + " a "
//                    + "left outer join tblposmaster b on a.strPOSCode=b.strPosCode  "
//                    + "left outer join tbltablemaster c on a.strTableNo=c.strTableNo and a.strClientCode=c.strClientCode "
//                    + "left outer join tblwaitermaster d on a.strWaiterNo=d.strWaiterNo and a.strClientCode=d.strClientCode "
//                    + "left outer join tblcustomermaster e on a.strCustomerCode=e.strCustomerCode and a.strClientCode=e.strClientCode "
//                    + "left outer join tbldebitcardmaster f on a.strCardNo=f.strCardNo "
//                    + "left outer join tblreasonmaster g on a.strReasonCode=g.strReasonCode "
//                    + "left outer join " + advBookBillHd + " h on a.strAdvBookingNo=h.strAdvBookingNo and a.strClientCode=h.strClientCode "
//                    + "left outer join " + advReceiptHd + " i on h.strAdvBookingNo=i.strAdvBookingNo and a.strClientCode=i.strClientCode "
//                    + "left outer join tblvoidbillhd j on a.strBillNo=j.strBillNo and a.strPOSCode=j.strPosCode and a.strClientCode=j.strClientCode "
//                    + "left outer join " + billSettlementdtl + " k on a.strBillNo=k.strBillNo and a.strClientCode=k.strClientCode "
//                    + "left outer join tblsettelmenthd l on k.strSettlementCode=l.strSettelmentCode "
//                    + "where a.strBillNo=? and a.strPOSCode=? "
//                    + "group by a.strBillNo; ");
//            pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlBillHeaderDtl.toString());
//            pst.setString(1, billNo);
//            pst.setString(2, POSCode);
//            rs_BillHD = pst.executeQuery();
//            rs_BillHD.next();
//            if (rs_BillHD.getString(21).equals("Complementary"))
//            {
//                flgComplimentaryBill = true;
//            }
//            FileWriter fstream_bill = new FileWriter(Text_Bill);
//            BufferedWriter BillOut = new BufferedWriter(fstream_bill);
//            if (clsGlobalVarClass.gClientCode.equals("117.001"))
//            {
//                if (POSCode.equals("P01"))
//                {
//                    objUtility2.funPrintBlankSpace("THE PREM'S HOTEL", BillOut);
//                    BillOut.write("THE PREM'S HOTEL");
//                    BillOut.newLine();
//                }
//                else if (POSCode.equals("P02"))
//                {
//                    objUtility2.funPrintBlankSpace("SWIG", BillOut);
//                    BillOut.write("SWIG");
//                    BillOut.newLine();
//                }
//            }
//            boolean isReprint = false;
//            if ("reprint".equalsIgnoreCase(reprint))
//            {
//                isReprint = true;
//                objUtility2.funPrintBlankSpace("[DUPLICATE]", BillOut);
//                BillOut.write("[DUPLICATE]");
//                BillOut.newLine();
//            }
//            if (transType.equals("Void"))
//            {
//                objUtility2.funPrintBlankSpace("VOIDED BILL", BillOut);
//                BillOut.write("VOIDED BILL");
//                BillOut.newLine();
//            }
//            boolean flag_isHomeDelvBill = false;
//            String SQL_HomeDelivery = "select strBillNo,strCustomerCode,strDPCode,tmeTime,strCustAddressLine1 "
//                    + "from tblhomedelivery where strBillNo=? ;";
//            pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(SQL_HomeDelivery);
//            pst.setString(1, billNo);
//            ResultSet rs_HomeDelivery = pst.executeQuery();
//            if (rs_HomeDelivery.next())
//            {
//                flag_isHomeDelvBill = true;
//                isCustomerPrint = true;
//
//                customerCode = rs_HomeDelivery.getString(2);
//                if (clsGlobalVarClass.gPrintHomeDeliveryYN)
//                {
//                    funPrintBlankSpace("HOME DELIVERY", BillOut);
//                    BillOut.write("HOME DELIVERY");
//                    BillOut.newLine();
//                }
//                String SQL_CustomerDtl = "";
//                if (rs_HomeDelivery.getString(5).equals("Temporary"))
//                {
//                    SQL_CustomerDtl = "select a.strCustomerName,a.strTempAddress,a.strTempStreet"
//                            + " ,a.strTempLandmark,a.strBuildingName,a.strCity,a.intPinCode,a.longMobileNo "
//                            + " from tblcustomermaster a left outer join tblbuildingmaster b "
//                            + " on a.strBuldingCode=b.strBuildingCode "
//                            + " where a.strCustomerCode=? ;";
//                }
//                else if (rs_HomeDelivery.getString(5).equals("Office"))
//                {
//                    SQL_CustomerDtl = "select a.strCustomerName,a.strOfficeBuildingName,a.strOfficeStreetName"
//                            + ",a.strOfficeLandmark,a.strOfficeArea,a.strOfficeCity,a.strOfficePinCode,a.longMobileNo "
//                            + " from tblcustomermaster a "
//                            + " where a.strCustomerCode=? ";
//                }
//                else
//                {
//                    SQL_CustomerDtl = "select a.strCustomerName,a.strCustAddress,a.strStreetName"
//                            + " ,a.strLandmark,a.strBuildingName,a.strCity,a.intPinCode,a.longMobileNo "
//                            + " from tblcustomermaster a left outer join tblbuildingmaster b "
//                            + " on a.strBuldingCode=b.strBuildingCode "
//                            + " where a.strCustomerCode=? ;";
//                }
//                pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(SQL_CustomerDtl);
//                pst.setString(1, rs_HomeDelivery.getString(2));
//                ResultSet rs_CustomerDtl = pst.executeQuery();
//                while (rs_CustomerDtl.next())
//                {
//                    BillOut.write("  NAME      :" + rs_CustomerDtl.getString(1).toUpperCase());
//                    BillOut.newLine();
//                    // Building Name    
//                    String add = rs_CustomerDtl.getString(2);
//                    int strlen = add.length();
//                    String add1 = "";
//                    if (strlen < 28)
//                    {
//                        add1 = add.substring(0, strlen);
//                        BillOut.write("  ADDRESS1  :" + add1.toUpperCase().replaceAll("\n", " "));
//                        BillOut.newLine();
//                    }
//                    else
//                    {
//                        add1 = add.substring(0, 28);
//                        BillOut.write("  ADDRESS1  :" + add1.toUpperCase().replaceAll("\n", " "));
//                        BillOut.newLine();
//                    }
//                    for (int i = 28; i <= strlen;)
//                    {
//                        int end = 0;
//                        end = i + 28;
//                        if (strlen > end)
//                        {
//                            add1 = add.substring(i, end);
//                            i = end;
//                            BillOut.write("             " + add1.toUpperCase().replaceAll("\n", " "));
//                            BillOut.newLine();
//                        }
//                        else
//                        {
//                            add1 = add.substring(i, strlen);
//                            BillOut.write("             " + add1.toUpperCase().replaceAll("\n", " "));
//                            BillOut.newLine();
//                            i = strlen + 1;
//                        }
//                    }
//                    // Street Name    
//                    String street = rs_CustomerDtl.getString(3);
//                    String street1;
//                    int streetlen = street.length();
//                    for (int i = 0; i <= streetlen;)
//                    {
//                        int end = 0;
//                        end = i + 28;
//                        if (streetlen > end)
//                        {
//                            street1 = street.substring(i, end);
//                            BillOut.write("             " + street1.toUpperCase());
//                            BillOut.newLine();
//                            i = end;
//                        }
//                        else
//                        {
//                            street1 = street.substring(i, streetlen);
//                            BillOut.write("             " + street1.toUpperCase());
//                            BillOut.newLine();
//                            i = streetlen + 1;
//                        }
//                    }
//                    // Landmark Name    
//                    if (rs_CustomerDtl.getString(4).trim().length() > 0)
//                    {
//                        BillOut.write("             " + rs_CustomerDtl.getString(4).toUpperCase());
//                        BillOut.newLine();
//                    }
//                    // Area Name    
//                    if (rs_CustomerDtl.getString(5).trim().length() > 0)
//                    {
//                        BillOut.write("             " + rs_CustomerDtl.getString(5).toUpperCase());
//                        BillOut.newLine();
//                    }
//                    // City Name    
//                    if (rs_CustomerDtl.getString(6).trim().length() > 0)
//                    {
//                        BillOut.write("             " + rs_CustomerDtl.getString(6).toUpperCase());
//                        BillOut.newLine();
//                    }
//                    // Pin Code    
//                    if (rs_CustomerDtl.getString(7).trim().length() > 0)
//                    {
//                        BillOut.write("             " + rs_CustomerDtl.getString(7).toUpperCase());
//                        BillOut.newLine();
//                    }
//                    // Mobile No    
//                    BillOut.write("  MOBILE NO :" + rs_CustomerDtl.getString(8));
//                    BillOut.newLine();
//                }
//                rs_CustomerDtl.close();
//                if (null != rs_HomeDelivery.getString(3) && rs_HomeDelivery.getString(3).trim().length() > 0)
//                {
//                    String[] delBoys = rs_HomeDelivery.getString(3).split(",");
//                    StringBuilder strIN = new StringBuilder("(");
//                    for (int i = 0; i < delBoys.length; i++)
//                    {
//                        if (i == 0)
//                        {
//                            strIN.append("'" + delBoys[i] + "'");
//                        }
//                        else
//                        {
//                            strIN.append(",'" + delBoys[i] + "'");
//                        }
//                    }
//                    strIN.append(")");
//                    String SQL_DeliveryBoyDtl = "select strDPName from tbldeliverypersonmaster where strDPCode IN " + strIN + " ;";
//                    pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(SQL_DeliveryBoyDtl);
//                    ResultSet rs_DeliveryBoyDtl = pst.executeQuery();
//                    strIN.setLength(0);
//                    for (int i = 0; rs_DeliveryBoyDtl.next(); i++)
//                    {
//                        if (i == 0)
//                        {
//                            strIN.append(rs_DeliveryBoyDtl.getString(1).toUpperCase());
//                        }
//                        else
//                        {
//                            strIN.append("," + rs_DeliveryBoyDtl.getString(1).toUpperCase());
//                        }
//                    }
//                    BillOut.write("  DELV BOY  :" + strIN);
//                    BillOut.newLine();
//                    rs_DeliveryBoyDtl.close();
//                }
//                BillOut.write(Line);
//                BillOut.newLine();
//            }
//            else
//            {
//                if (rs_BillHD.getString(7).length() > 0)//customerCode
//                {
//                    BillOut.write("  NAME      :" + rs_BillHD.getString(24).toUpperCase());
//                    BillOut.newLine();
//                    // Mobile No    
//                    BillOut.write("  MOBILE NO :" + rs_BillHD.getString(32));
//                    BillOut.newLine();
//
//                    isCustomerPrint = true;
//                }
//            }
//            rs_HomeDelivery.close();
//            //print take away
//            int billPrintSize = 4;
//            if (rs_BillHD.getString(30).equals("TakeAway"))
//            {
//                objUtility2.funPrintBlankSpace("Take Away", BillOut);
//                BillOut.write("Take Away");
//                BillOut.newLine();
//            }
//            if (clsGlobalVarClass.gPrintTaxInvoice.equalsIgnoreCase("Y"))
//            {
//                objUtility2.funPrintBlankSpace("TAX INVOICE", BillOut);
//                BillOut.write("TAX INVOICE");
//                BillOut.newLine();
//            }
//            if (clsGlobalVarClass.gClientCode.equals("047.001") && POSCode.equals("P03"))
//            {
//                objUtility2.funPrintBlankSpace("SHRI SHAM CATERERS", BillOut);
//                BillOut.write("SHRI SHAM CATERERS");
//                BillOut.newLine();
//                String cAddr1 = "Flat No.7, Mon Amour,";
//                objUtility2.funPrintBlankSpace(cAddr1, BillOut);
//                BillOut.write(cAddr1.toUpperCase());
//                BillOut.newLine();
//                String cAddr2 = "Thorat Colony,Prabhat Road,";
//                objUtility2.funPrintBlankSpace(cAddr2, BillOut);
//                BillOut.write(cAddr2.toUpperCase());
//                BillOut.newLine();
//                String cAddr3 = " Erandwane, Pune 411 004.";
//                objUtility2.funPrintBlankSpace(cAddr3, BillOut);
//                BillOut.write(cAddr3.toUpperCase());
//                BillOut.newLine();
//                String cAddr4 = "Approved Caterers of";
//                objUtility2.funPrintBlankSpace(cAddr4, BillOut);
//                BillOut.write(cAddr4.toUpperCase());
//                BillOut.newLine();
//                String cAddr5 = "ROYAL CONNAUGHT BOAT CLUB";
//                objUtility2.funPrintBlankSpace(cAddr5, BillOut);
//                BillOut.write(cAddr5.toUpperCase());
//                BillOut.newLine();
//            }
//            else if (clsGlobalVarClass.gClientCode.equals("047.001") && POSCode.equals("P02"))
//            {
//                objUtility2.funPrintBlankSpace("SHRI SHAM CATERERS", BillOut);
//                BillOut.write("SHRI SHAM CATERERS");
//                BillOut.newLine();
//                String cAddr1 = "Flat No.7, Mon Amour,";
//                objUtility2.funPrintBlankSpace(cAddr1, BillOut);
//                BillOut.write(cAddr1.toUpperCase());
//                BillOut.newLine();
//                String cAddr2 = "Thorat Colony,Prabhat Road,";
//                objUtility2.funPrintBlankSpace(cAddr2, BillOut);
//                BillOut.write(cAddr2.toUpperCase());
//                BillOut.newLine();
//                String cAddr3 = " Erandwane, Pune 411 004.";
//                objUtility2.funPrintBlankSpace(cAddr3, BillOut);
//                BillOut.write(cAddr3.toUpperCase());
//                BillOut.newLine();
//                String cAddr4 = "Approved Caterers of";
//                objUtility2.funPrintBlankSpace(cAddr4, BillOut);
//                BillOut.write(cAddr4.toUpperCase());
//                BillOut.newLine();
//                String cAddr5 = "ROYAL CONNAUGHT BOAT CLUB";
//                objUtility2.funPrintBlankSpace(cAddr5, BillOut);
//                BillOut.write(cAddr5.toUpperCase());
//                BillOut.newLine();
//            }
//            else if (clsGlobalVarClass.gClientCode.equals("092.001") || clsGlobalVarClass.gClientCode.equals("092.002") || clsGlobalVarClass.gClientCode.equals("092.003"))//Shree Sound Pvt. Ltd.
//            {
//                objUtility2.funPrintBlankSpace("SSPL", BillOut);
//                BillOut.write("SSPL");
//                BillOut.newLine();
//                objUtility2.funPrintBlankSpace(clsGlobalVarClass.gClientAddress1, BillOut);
//                BillOut.write(clsGlobalVarClass.gClientAddress1.toUpperCase());
//                BillOut.newLine();
//                if (clsGlobalVarClass.gClientAddress2.trim().length() > 0)
//                {
//                    objUtility2.funPrintBlankSpace(clsGlobalVarClass.gClientAddress2, BillOut);
//                    BillOut.write(clsGlobalVarClass.gClientAddress2.toUpperCase());
//                    BillOut.newLine();
//                }
//                if (clsGlobalVarClass.gClientAddress3.trim().length() > 0)
//                {
//                    objUtility2.funPrintBlankSpace(clsGlobalVarClass.gClientAddress3, BillOut);
//                    BillOut.write(clsGlobalVarClass.gClientAddress3.toUpperCase());
//                    BillOut.newLine();
//                }
//                if (clsGlobalVarClass.gCityName.trim().length() > 0)
//                {
//                    objUtility2.funPrintBlankSpace(clsGlobalVarClass.gCityName, BillOut);
//                    BillOut.write(clsGlobalVarClass.gCityName.toUpperCase());
//                    BillOut.newLine();
//                }
//            }
//            else if (clsGlobalVarClass.gClientCode.equals("092.001") || clsGlobalVarClass.gClientCode.equals("092.002") || clsGlobalVarClass.gClientCode.equals("092.003"))//Shree Sound Pvt. Ltd.
//            {
//                objUtility2.funPrintBlankSpace("SSPL", BillOut);
//                BillOut.write("SSPL");
//                BillOut.newLine();
//                objUtility2.funPrintBlankSpace(clsGlobalVarClass.gClientAddress1, BillOut);
//                BillOut.write(clsGlobalVarClass.gClientAddress1.toUpperCase());
//                BillOut.newLine();
//                if (clsGlobalVarClass.gClientAddress2.trim().length() > 0)
//                {
//                    objUtility2.funPrintBlankSpace(clsGlobalVarClass.gClientAddress2, BillOut);
//                    BillOut.write(clsGlobalVarClass.gClientAddress2.toUpperCase());
//                    BillOut.newLine();
//                }
//                if (clsGlobalVarClass.gClientAddress3.trim().length() > 0)
//                {
//                    objUtility2.funPrintBlankSpace(clsGlobalVarClass.gClientAddress3, BillOut);
//                    BillOut.write(clsGlobalVarClass.gClientAddress3.toUpperCase());
//                    BillOut.newLine();
//                }
//                if (clsGlobalVarClass.gCityName.trim().length() > 0)
//                {
//                    objUtility2.funPrintBlankSpace(clsGlobalVarClass.gCityName, BillOut);
//                    BillOut.write(clsGlobalVarClass.gCityName.toUpperCase());
//                    BillOut.newLine();
//                }
//            }
//            else
//            {
//                objUtility2.funPrintBlankSpace(clsGlobalVarClass.gClientName, BillOut);
//                if (clsGlobalVarClass.gClientCode.equals("124.001"))
//                {
//                    BillOut.write(clsGlobalVarClass.gClientName);
//                }
//                else
//                {
//                    BillOut.write(clsGlobalVarClass.gClientName.toUpperCase());
//                }
//                BillOut.newLine();
//                objUtility2.funPrintBlankSpace(clsGlobalVarClass.gClientAddress1, BillOut);
//                BillOut.write(clsGlobalVarClass.gClientAddress1.toUpperCase());
//                BillOut.newLine();
//                if (clsGlobalVarClass.gClientAddress2.trim().length() > 0)
//                {
//                    objUtility2.funPrintBlankSpace(clsGlobalVarClass.gClientAddress2, BillOut);
//                    BillOut.write(clsGlobalVarClass.gClientAddress2.toUpperCase());
//                    BillOut.newLine();
//                }
//                if (clsGlobalVarClass.gClientAddress3.trim().length() > 0)
//                {
//                    objUtility2.funPrintBlankSpace(clsGlobalVarClass.gClientAddress3, BillOut);
//                    BillOut.write(clsGlobalVarClass.gClientAddress3.toUpperCase());
//                    BillOut.newLine();
//                }
//                if (clsGlobalVarClass.gCityName.trim().length() > 0)
//                {
//                    objUtility2.funPrintBlankSpace(clsGlobalVarClass.gCityName, BillOut);
//                    BillOut.write(clsGlobalVarClass.gCityName.toUpperCase());
//                    BillOut.newLine();
//                }
//            }
//            BillOut.write("  TEL NO.   :" + " ");
//            BillOut.write(String.valueOf(clsGlobalVarClass.gClientTelNo));
//            BillOut.newLine();
//            BillOut.write("  EMAIL ID  :" + " ");
//            BillOut.write(clsGlobalVarClass.gClientEmail);
//            BillOut.newLine();
//            tblName = rs_BillHD.getString(18);
//            if (tblName.length() > 0)
//            {
//                if (clsGlobalVarClass.gClientCode.equalsIgnoreCase("136.001"))//KINKI
//                {
//                    BillOut.write("  TABLE No   :");
//                }
//                else
//                {
//                    BillOut.write("  TABLE No. :" + "  ");
//                }
//                BillOut.write(tblName);
//                BillOut.newLine();
//            }
//            waiterName = rs_BillHD.getString(19);
//            if (waiterName.trim().length() > 0)
//            {
//                BillOut.write("  STEWARD   :" + "  ");
//                BillOut.write(waiterName);
//                BillOut.newLine();
//            }
//            BillOut.write(Linefor5);
//            BillOut.newLine();
//            BillOut.write("  POS         : ");
//            BillOut.write(rs_BillHD.getString(16));
//            BillOut.newLine();
//            BillOut.write("  BILL NO.    : ");
//            BillOut.write(billNo);
//            BillOut.newLine();
//            BillOut.write("  PAX NO.     : ");
//            BillOut.write(rs_BillHD.getString(17));
//            BillOut.newLine();
//            if (clsGlobalVarClass.gPrintTimeOnBillYN)
//            {
//                BillOut.write("  DATE & TIME : ");
//                SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy hh:mm a ");
//                BillOut.write(ft.format(rs_BillHD.getObject(3)));
//                BillOut.newLine();
//
//            }
//            else
//            {
//                BillOut.write("  DATE        : ");
//                SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy");
//                BillOut.write(ft.format(rs_BillHD.getObject(3)));
//                BillOut.newLine();
//            }
//            if (rs_BillHD.getString(11).trim().length() > 0 && !flgComplimentaryBill)
//            {
//                BillOut.write("  Remarks     : ");
//                BillOut.write(rs_BillHD.getString(11));
//                BillOut.newLine();
//            }
//            subTotal = rs_BillHD.getString(6);
//            grandTotal = rs_BillHD.getString(8);
//            user = rs_BillHD.getString(12);
//            deliveryCharge = rs_BillHD.getString(13);
//            advAmount = rs_BillHD.getString(14);
//            //print card available balance
//            String isSttled = "select a.strBillNo from " + billSettlementdtl + " a," + billhd + " b "
//                    + " where a.strBillNo=b.strBillNo and a.strClientCode=b.strClientCode "
//                    + " and a.strBillNo='" + billNo + "' and b.strPOSCode='" + POSCode + "' ";
//            ResultSet rsIsSettled = clsGlobalVarClass.dbMysql.executeResultSet(isSttled);
//            if (rsIsSettled.next())
//            {
//                rsIsSettled.close();
//                String availBal = "select a.strCardNo,(b.dblRedeemAmt)"
//                        + "from " + billhd + " a inner join tbldebitcardmaster b on a.strCardNo=b.strCardNo "
//                        + "where a.strBillNo='" + billNo + "' and a.strPOSCode='" + POSCode + "'; ";
//                ResultSet rsAvailBal = clsGlobalVarClass.dbMysql.executeResultSet(availBal);
//                if (rsAvailBal.next())
//                {
//                    BillOut.write("  Available Balance(" + rsAvailBal.getString(1) + "):" + rsAvailBal.getString(2));
//                    BillOut.newLine();
//                }
//            }
//            else
//            {
//                String availBal = "select a.strCardNo,(b.dblRedeemAmt-a.dblGrandTotal)"
//                        + "from " + billhd + " a inner join tbldebitcardmaster b on a.strCardNo=b.strCardNo "
//                        + "where a.strBillNo='" + billNo + "' and a.strPOSCode='" + POSCode + "'; ";
//                ResultSet rsAvailBal = clsGlobalVarClass.dbMysql.executeResultSet(availBal);
//                if (rsAvailBal.next())
//                {
//                    BillOut.write("  Available Balance(" + rsAvailBal.getString(1) + "):" + rsAvailBal.getString(2));
//                    BillOut.newLine();
//                }
//            }
//            //print card available balance
//            if (transType.equals("Void"))
//            {
//                BillOut.write("  Reason      :" + " " + rs_BillHD.getString(22));//voided reason
//                BillOut.newLine();
//            }
//            else if (flgComplimentaryBill)
//            {
//
//                BillOut.write("  Reason      :" + " " + rs_BillHD.getString(23));
//                BillOut.newLine();
//                BillOut.write("  Remark      :" + " " + rs_BillHD.getString(11));
//                BillOut.newLine();
//            }
//            if (clsGlobalVarClass.gCMSIntegrationYN)
//            {
//                BillOut.write("  Member Code : ");
//                BillOut.write(rs_BillHD.getString(7));
//                BillOut.newLine();
//                BillOut.write("  Member Name : ");
//                objUtility2.funWriteToTextMemberNameForFormat5(BillOut, rs_BillHD.getString(24), "Format5");
//                BillOut.newLine();
//                BillOut.write(Linefor5);
//            }
//            if (rs_BillHD.getString(25) != null && rs_BillHD.getString(25).length() > 0)
//            {
//                if (rs_BillHD.getString(26).length() > 0 || rs_BillHD.getString(27).length() > 0 || rs_BillHD.getString(28).length() > 0)
//                {
//                    BillOut.newLine();
//                    objUtility2.funPrintBlankSpace("ORDER DETAIL", BillOut);
//                    BillOut.write("ORDER DETAIL");
//                    BillOut.newLine();
//                    BillOut.write(Linefor5);
//                    BillOut.newLine();
//                }
//                StringBuilder strValue = new StringBuilder();
//                strValue.setLength(0);
//                if (rs_BillHD.getString(26).length() > 0)
//                {
//                    strValue.append(rs_BillHD.getString(26));
//                }
//                else
//                {
//                    strValue.append("");
//                }
//                int strlenMsg = strValue.length();
//                if (strlenMsg > 0)
//                {
//                    String msg1 = "";
//                    if (strlenMsg < 27)
//                    {
//                        msg1 = strValue.substring(0, strlenMsg);
//                        BillOut.write("  MESSAGE     :" + msg1);
//                        BillOut.newLine();
//                    }
//                    else
//                    {
//                        msg1 = strValue.substring(0, 27);
//                        BillOut.write("  MESSAGE     :" + msg1);;
//                        BillOut.newLine();
//                    }
//                    for (int i = 27; i <= strlenMsg; i++)
//                    {
//                        int endmsg = 0;
//                        endmsg = i + 27;
//                        if (strlenMsg > endmsg)
//                        {
//                            msg1 = strValue.substring(i, endmsg);
//                            i = endmsg;
//                            BillOut.write("               " + msg1);
//                            BillOut.newLine();
//                        }
//                        else
//                        {
//                            msg1 = strValue.substring(i, strlenMsg);
//                            BillOut.write("               " + msg1);
//                            BillOut.newLine();
//                            i = strlenMsg + 1;
//                        }
//                    }
//                }
//                strValue.setLength(0);
//                if (rs_BillHD.getString(27).length() > 0)//shape
//                {
//                    strValue.append(rs_BillHD.getString(27));
//                }
//                else
//                {
//                    strValue.append("");
//                }
//                int strlenShape = strValue.length();
//                if (strlenShape > 0)
//                {
//                    String shape1 = "";
//                    if (strlenShape < 27)
//                    {
//                        shape1 = strValue.substring(0, strlenShape);
//                        BillOut.write("  SHAPE       :" + shape1);
//                        BillOut.newLine();
//                    }
//                    else
//                    {
//                        shape1 = strValue.substring(0, 27);
//                        BillOut.write("  SHAPE       :" + shape1);
//                        BillOut.newLine();
//                    }
//                    for (int j = 27; j <= strlenShape; j++)
//                    {
//                        int endShape = 0;
//                        endShape = j + 27;
//                        if (strlenShape > endShape)
//                        {
//                            shape1 = strValue.substring(j, endShape);
//                            j = endShape;
//                            BillOut.write("               " + shape1);
//                            BillOut.newLine();
//                        }
//                        else
//                        {
//                            shape1 = strValue.substring(j, strlenShape);
//                            BillOut.write("               " + shape1);
//                            BillOut.newLine();
//                            j = strlenShape + 1;
//                        }
//                    }
//                }
//
//                strValue.setLength(0);
//                if (rs_BillHD.getString(28).length() > 0)//note
//                {
//                    strValue.append(rs_BillHD.getString(28));
//                }
//                else
//                {
//                    strValue.append("");
//                }
//                int strlenNote = strValue.length();
//                if (strlenNote > 0)
//                {
//                    String note1 = "";
//                    if (strlenNote < 27)
//                    {
//                        note1 = strValue.substring(0, strlenNote);
//                        BillOut.write("  NOTE        :" + note1);
//                        BillOut.newLine();
//                    }
//                    else
//                    {
//                        note1 = strValue.substring(0, 27);
//                        BillOut.write("  NOTE        :" + note1);
//                        BillOut.newLine();
//                    }
//                    for (int i = 27; i <= strlenNote; i++)
//                    {
//                        int endNote = 0;
//                        endNote = i + 27;
//                        if (strlenNote > endNote)
//                        {
//                            note1 = strValue.substring(i, endNote);
//                            i = endNote;
//                            BillOut.write("               " + note1);
//                            BillOut.newLine();
//                        }
//                        else
//                        {
//                            note1 = strValue.substring(i, strlenNote);
//                            BillOut.write("               " + note1);
//                            BillOut.newLine();
//                            i = strlenNote + 1;
//                        }
//                    }
//                }
//                if (rs_BillHD.getString(26).length() > 0 || rs_BillHD.getString(27).length() > 0 || rs_BillHD.getString(28).length() > 0)
//                {
//
//                    BillOut.write(Linefor5);
//                    BillOut.newLine();
//                }
//            }
//
////            if (!isCustomerPrint)
////            {
////                BillOut.write("  NAME        :-------------------------");//25 dashes
////                BillOut.newLine();
////                BillOut.write("  Address     :-------------------------");//25 dashes
////                BillOut.newLine();
//////                BillOut.write("            --------------------------");//25 dashes
//////                BillOut.newLine();
////
////            }
//            BillOut.write(Linefor5);
//            BillOut.newLine();
//            BillOut.write("     QTY ITEM NAME                  AMT");
//            BillOut.newLine();
//            BillOut.write(Linefor5);
//            BillOut.newLine();
//            String SQL_BillDtl = "SELECT SUM(a.dblQuantity), "
//                    + "LEFT(a.strItemName,22) AS ItemLine1, MID(a.strItemName,23, LENGTH(a.strItemName)) AS ItemLine2, SUM(a.dblAmount),a.strItemCode "
//                    + ",a.strKOTNo,e.strGroupName,d.strSubGroupName "
//                    + "FROM " + billdtl + " a," + billhd + " b,tblitemmaster c,tblsubgrouphd d,tblgrouphd e "
//                    + "WHERE a.strBillNo=b.strBillNo  "
//                    + "and a.strItemCode=c.strItemCode "
//                    + "and c.strSubGroupCode=d.strSubGroupCode "
//                    + "and d.strGroupCode=e.strGroupCode "
//                    + "AND a.strClientCode=b.strClientCode "
//                    + " and a.strBillNo=? "
//                    + " and b.strPOSCode=?  ";
//            if (!clsGlobalVarClass.gPrintTDHItemsInBill)
//            {
//                SQL_BillDtl += "and a.tdhYN='N' ";
//            }
//            if (!clsGlobalVarClass.gPrintOpenItemsOnBill)
//            {
//                SQL_BillDtl += "and a.dblAmount>0 ";
//            }
//            SQL_BillDtl += " GROUP BY e.strGroupCode,a.strItemCode "
//                    + "order BY e.strGroupCode ";
//            pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(SQL_BillDtl);
//            pst.setString(1, billNo);
//            pst.setString(2, POSCode);
//            ResultSet rs_BillDtl = pst.executeQuery();
//
//            String printGroupName = "";
//            DecimalFormat decimalFormat = new DecimalFormat("0.00");
//            double groupTotal = 0.00;
//            while (rs_BillDtl.next())
//            {
//                String rsGroupName = rs_BillDtl.getString(7).toUpperCase();//groupName
//                if (printGroupName.isEmpty())
//                {
//                    //BillOut.newLine();
//                    BillOut.write("                  " + rsGroupName);
//                    BillOut.newLine();
//
//                    printGroupName = rsGroupName;
//                    groupTotal = rs_BillDtl.getDouble(4);//amt
//                }
//                else if (printGroupName.equalsIgnoreCase(rsGroupName))//
//                {
//                    groupTotal = groupTotal + rs_BillDtl.getDouble(4);//amt
//                }
//                else
//                {
//                    BillOut.newLine();
//                    BillOut.write(Linefor5);
//                    BillOut.newLine();
//                    funWriteTotal(printGroupName + " TOTAL", decimalFormat.format(groupTotal), BillOut, "Format5");
//                    BillOut.newLine();
//                    BillOut.write(Linefor5);
//
//                    groupTotal = 0.00;//amt
//                    BillOut.newLine();
//                    BillOut.write("                  " + rsGroupName);
//                    BillOut.newLine();
//
//                    groupTotal = groupTotal + rs_BillDtl.getDouble(4);//amt
//                    printGroupName = rsGroupName;
//                }
//
//                double saleQty = rs_BillDtl.getDouble(1);
//                String sqlPromoBills = "select dblQuantity from " + billPromoDtl + " "
//                        + " where strBillNo='" + billNo + "' and strItemCode='" + rs_BillDtl.getString(5) + "' "
//                        + " and strPromoType='ItemWise' ";
//                ResultSet rsPromoItems = clsGlobalVarClass.dbMysql.executeResultSet(sqlPromoBills);
//                if (rsPromoItems.next())
//                {
//                    saleQty -= rsPromoItems.getDouble(1);
//                }
//                rsPromoItems.close();
//                String qty = String.valueOf(saleQty);
//                if (qty.contains("."))
//                {
//                    String decVal = qty.substring(qty.length() - 2, qty.length());
//                    if (Double.parseDouble(decVal) == 0)
//                    {
//                        qty = qty.substring(0, qty.length() - 2);
//                    }
//                }
//                if (saleQty > 0)
//                {
//                    objUtility2.funPrintContentWithSpace("Right", qty, 8, BillOut);//Qty Print
//                    BillOut.write(" ");
//                    objUtility2.funPrintContentWithSpace("Left", rs_BillDtl.getString(2), 22, BillOut);//Item Name
//                    if (flgComplimentaryBill)
//                    {
//                        objUtility2.funPrintContentWithSpace("Right", "0.00", 9, BillOut);//Amount
//                    }
//                    else
//                    {
//                        objUtility2.funPrintContentWithSpace("Right", rs_BillDtl.getString(4), 9, BillOut);//Amount
//                    }
//                    BillOut.newLine();
//                    if (rs_BillDtl.getString(3).trim().length() > 0)
//                    {
//                        String line = rs_BillDtl.getString(3);
//                        if (line.length() > 22)
//                        {
//                            BillOut.write("         " + line.substring(0, 22));
//                            BillOut.newLine();
//
//                            BillOut.write("         " + line.substring(22, line.length()));
//                            BillOut.newLine();
//                        }
//                        else
//                        {
//                            BillOut.write("         " + line);
//                            BillOut.newLine();
//                        }
//                    }
//                    String sqlModifier = "select count(*) "
//                            + "from " + billModifierdtl + " where strBillNo=? and left(strItemCode,7)=? ";
//                    if (!clsGlobalVarClass.gPrintZeroAmtModifierOnBill)
//                    {
//                        sqlModifier += " and  dblAmount !=0.00 ";
//                    }
//                    pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlModifier);
//                    pst.setString(1, billNo);
//                    pst.setString(2, rs_BillDtl.getString(5));
//                    ResultSet rs_count = pst.executeQuery();
//                    rs_count.next();
//                    int cntRecord = rs_count.getInt(1);
//                    rs_count.close();
//                    if (cntRecord > 0)
//                    {
//                        sqlModifier = "select strModifierName,dblQuantity,dblAmount "
//                                + " from " + billModifierdtl + " "
//                                + " where strBillNo=? and left(strItemCode,7)=? ";
//                        if (!clsGlobalVarClass.gPrintZeroAmtModifierOnBill)
//                        {
//                            sqlModifier += " and  dblAmount !=0.00 ";
//                        }
//                        pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlModifier);
//                        pst.setString(1, billNo);
//                        pst.setString(2, rs_BillDtl.getString(5));
//                        ResultSet rs_modifierRecord = pst.executeQuery();
//                        while (rs_modifierRecord.next())
//                        {
//                            if (flgComplimentaryBill)
//                            {
//                                objUtility2.funWriteToTextformat5(BillOut, "", rs_modifierRecord.getString(1).toUpperCase(), "0.00", "Format5");
//                                BillOut.newLine();
//                            }
//                            else
//                            {
//                                objUtility2.funWriteToTextformat5(BillOut, "", rs_modifierRecord.getString(1).toUpperCase(), rs_modifierRecord.getString(3), "Format5");
//                                BillOut.newLine();
//                            }
//                            groupTotal = groupTotal + rs_modifierRecord.getDouble(3);//amt
//                        }
//                        rs_modifierRecord.close();
//                    }
//
//                    sql = "select b.strItemCode,b.dblWeight "
//                            + " from " + billhd + " a," + advBookBillDtl + " b "
//                            + " where a.strAdvBookingNo=b.strAdvBookingNo and a.strClientCode=b.strClientCode "
//                            + " and a.strBillNo='" + billNo + "' and b.strItemCode='" + rs_BillDtl.getString(5) + "' "
//                            + " and a.strPOSCode='" + POSCode + "' ";
//                    ResultSet rsWeight = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//                    while (rsWeight.next())
//                    {
//                        BillOut.write("     Weight");
//                        BillOut.write("     " + rsWeight.getDouble(2));
//                        BillOut.newLine();
//                    }
//                    rsWeight.close();
//                    sql = "select c.strCharName,b.strCharValues "
//                            + " from " + billhd + " a," + advBookBillCharDtl + " b,tblcharactersticsmaster c "
//                            + " where a.strAdvBookingNo=b.strAdvBookingNo and b.strCharCode=c.strCharCode "
//                            + " and a.strBillNo='" + billNo + "' and b.strItemCode='" + rs_BillDtl.getString(5) + "' "
//                            + " and a.strPOSCode='" + POSCode + "' and a.strClientCode=b.strClientCode ";
//                    ResultSet rsCharDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//                    while (rsCharDtl.next())
//                    {
//                        String charName = objUtility.funPrintTextWithAlignment(rsCharDtl.getString(1), 12, "Left");
//                        BillOut.write("     " + charName);
//                        String charVal = objUtility.funPrintTextWithAlignment(rsCharDtl.getString(2), 28, "Left");
//                        BillOut.write("     " + charVal);
//                        BillOut.newLine();
//                    }
//                    rsCharDtl.close();
//                }
//            }
//            rs_BillDtl.close();
//
//            BillOut.newLine();
//            BillOut.write(Linefor5);
//            BillOut.newLine();
//            funWriteTotal(printGroupName + " TOTAL", decimalFormat.format(groupTotal), BillOut, "Format5");
//            BillOut.newLine();
//
//            funPrintPromoItemsInBill(billNo, BillOut, 4);  // Print Promotion Items in Bill for this billno.
//            BillOut.write(Linefor5);
//            BillOut.newLine();
//            if (clsGlobalVarClass.gPointsOnBillPrint)
//            {
//                String sqlCRMPoints = "select b.dblPoints from " + billhd + " a, tblcrmpoints b "
//                        + " where a.strBillNo=b.strBillNo and a.strClientCode=b.strClientCode "
//                        + " and a.strBillNo='" + billNo + "' and a.strPOSCode='" + POSCode + "' ";
//                ResultSet rsCRMPoints = clsGlobalVarClass.dbMysql.executeResultSet(sqlCRMPoints);
//                if (rsCRMPoints.next())
//                {
//                    funWriteTotal("POINTS ", rsCRMPoints.getString(1), BillOut, "Format5");
//                }
//                rsCRMPoints.close();
//                BillOut.newLine();
//            }
//            if (flgComplimentaryBill)
//            {
//                funWriteTotal("SUB TOTAL", "0.00", BillOut, "Format5");
//                BillOut.newLine();
//            }
//            else
//            {
//                funWriteTotal("SUB TOTAL", subTotal, BillOut, "Format5");
//                BillOut.newLine();
//            }
//            sql = "select a.dblDiscPer,a.dblDiscAmt,a.strDiscOnType,a.strDiscOnValue,b.strReasonName,a.strDiscRemarks "
//                    + " from " + billDscFrom + " a ,tblreasonmaster b," + billhd + " c "
//                    + " where  a.strDiscReasonCode=b.strReasonCode and a.strBillNo=c.strBillNo "
//                    + " and a.strClientCode=c.strClientCode and a.strBillNo='" + billNo + "' and c.strPOSCode='" + POSCode + "' ";
//            ResultSet rsDisc = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//            boolean flag = true;
//            while (rsDisc.next())
//            {
//                if (flag)
//                {
//                    flag = false;
//                    BillOut.write("  DISCOUNT");
//                    BillOut.newLine();
//                }
//                double dbl = Double.parseDouble(rsDisc.getString("dblDiscPer"));
//                String discText = String.format("%.1f", dbl) + "%" + " On " + rsDisc.getString("strDiscOnValue") + "";
//                if (discText.length() > 30)
//                {
//                    discText = discText.substring(0, 30);
//                }
//                else
//                {
//                    discText = String.format("%-30s", discText);
//                }
//                BillOut.write("  " + discText);
//                String discountOnItem = objUtility.funPrintTextWithAlignment(rsDisc.getString("dblDiscAmt"), 8, "Right");
//                BillOut.write(discountOnItem);
//                BillOut.newLine();
//                BillOut.write("  Reason  : ");
//                String discReason = objUtility.funPrintTextWithAlignment(rsDisc.getString(5), 20, "Left");
//                BillOut.write(discReason);
//                BillOut.newLine();
//                BillOut.write("  Remarks : ");
//                String discRemarks = objUtility.funPrintTextWithAlignment(rsDisc.getString(6), 20, "Left");
//                BillOut.write(discRemarks);
//                BillOut.newLine();
//
//            }
//            String sql_Tax = "select b.strTaxDesc,sum(a.dblTaxAmount) "
//                    + " from " + billtaxdtl + " a,tbltaxhd b," + billhd + " c "
//                    + " where a.strBillNo='" + billNo + "' "
//                    + " and a.strTaxCode=b.strTaxCode "
//                    + " and a.strBillNo=c.strBillNo "
//                    + " and a.strClientCode=c.strClientCode "
//                    + " and c.strPOSCode='" + POSCode + "' "
//                    + " and b.strTaxCalculation='Forward' "
//                    + " group by a.strTaxCode";
//            ResultSet rsTax = clsGlobalVarClass.dbMysql.executeResultSet(sql_Tax);
//            while (rsTax.next())
//            {
//                if (flgComplimentaryBill)
//                {
//                    funWriteTotal(rsTax.getString(1), "0.00", BillOut, "Format5");
//                    BillOut.newLine();
//                }
//                else
//                {
//                    funWriteTotal(rsTax.getString(1), rsTax.getString(2), BillOut, "Format5");
//                    BillOut.newLine();
//                }
//            }
//            if (deliveryCharge != null && deliveryCharge.trim().length() > 0 && !"0.00".equalsIgnoreCase(deliveryCharge))
//            {
//                funWriteTotal("DELV. CHARGE", deliveryCharge, BillOut, "Format5");
//                BillOut.newLine();
//            }
//            if (advAmount.trim().length() > 0 && !"0.00".equalsIgnoreCase(advAmount))
//            {
//                funWriteTotal("ADVANCE", advAmount, BillOut, "Format5");
//                BillOut.newLine();
//            }
//            BillOut.write(Linefor5);
//            BillOut.newLine();
//            if (flgComplimentaryBill)
//            {
//                funWriteTotal("TOTAL(ROUNDED)", "0.00", BillOut, "Format5");
//                BillOut.newLine();
//                BillOut.write(Linefor5);
//            }
//            else
//            {
//                funWriteTotal("TOTAL(ROUNDED)", grandTotal, BillOut, "Format5");
//                BillOut.newLine();
//                BillOut.write(Linefor5);
//            }
//
//            //print Grand total of other bill nos from bill series
//            if (clsGlobalVarClass.gEnableBillSeries)
//            {
//                String sqlPrintGT = "select a.strPrintGTOfOtherBills,b.strDtlBillNos,b.dblGrandTotal "
//                        + "from tblbillseries a,tblbillseriesbilldtl b "
//                        + "where (a.strPOSCode=b.strPOSCode or a.strPOSCode='All') "
//                        + "and a.strBillSeries=b.strBillSeries "
//                        + "and b.strHdBillNo='" + billNo + "' and b.strPOSCode='" + POSCode + "' ";
//                ResultSet rsPrintGTYN = clsGlobalVarClass.dbMysql.executeResultSet(sqlPrintGT);
//                double dblOtherBillsGT = 0.00;
//                if (rsPrintGTYN.next())
//                {
//                    if (rsPrintGTYN.getString(1).equalsIgnoreCase("Y"))
//                    {
//                        String billSeriesDtlBillNos = rsPrintGTYN.getString(2);
//                        String[] dtlBillSeriesBillNo = billSeriesDtlBillNos.split(",");
//                        dblOtherBillsGT += rsPrintGTYN.getDouble(3);
//                        if (dtlBillSeriesBillNo.length > 0)
//                        {
//                            for (int i = 0; i < dtlBillSeriesBillNo.length; i++)
//                            {
//                                sqlPrintGT = "select a.strHdBillNo,a.dblGrandTotal "
//                                        + "from tblbillseriesbilldtl a "
//                                        + "where a.strHdBillNo='" + dtlBillSeriesBillNo[i] + "' and a.strPOSCode='" + POSCode + "' ";
//                                ResultSet rsPrintGT = clsGlobalVarClass.dbMysql.executeResultSet(sqlPrintGT);
//                                if (rsPrintGT.next())
//                                {
//                                    BillOut.newLine();
//                                    funWriteTotal(dtlBillSeriesBillNo[i] + " TOTAL(ROUNDED)", rsPrintGT.getString(2), BillOut, "Format5");
//                                    dblOtherBillsGT += rsPrintGT.getDouble(2);
//                                    BillOut.newLine();
//                                }
//                            }
//                            BillOut.newLine();
//                            BillOut.write(Linefor5);
//                            BillOut.newLine();
//                            funWriteTotal("GRAND TOTAL(ROUNDED)", String.valueOf(dblOtherBillsGT), BillOut, "Format5");
//                            BillOut.newLine();
//                            BillOut.write(Linefor5);
//                            BillOut.newLine();
//                        }
//                    }
//                }
//            }
//
//            //settlement breakup part
//            String sqlSettlementBreakup = "select a.dblSettlementAmt, b.strSettelmentDesc, b.strSettelmentType "
//                    + " from " + billSettlementdtl + " a ,tblsettelmenthd b," + billhd + " c "
//                    + " where a.strBillNo=? and a.strBillNo=c.strBillNo and a.strClientCode=c.strClientCode "
//                    + " and a.strSettlementCode=b.strSettelmentCode and c.strPOSCode=? ";
//            pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlSettlementBreakup);
//            pst.setString(1, billNo);
//            pst.setString(2, POSCode);
//            boolean flgSettlement = false;
//            boolean creditSettlement = false;
//            ResultSet rsBillSettlement = pst.executeQuery();
//            while (rsBillSettlement.next())
//            {
//                if (flgComplimentaryBill)
//                {
//                    BillOut.newLine();
//                    funWriteTotal(rsBillSettlement.getString(2), "0.00", BillOut, "Format5");
//                }
//                else
//                {
//                    BillOut.newLine();
//                    funWriteTotal(rsBillSettlement.getString(2), rsBillSettlement.getString(1), BillOut, "Format5");
//                }
//                flgSettlement = true;
//                if (rsBillSettlement.getString(3).equals("Credit"))
//                {
//                    creditSettlement = true;
//                }
//            }
//            rsBillSettlement.close();
//
//            if (flgSettlement)
//            {
//                BillOut.newLine();
//                if (creditSettlement)
//                {
//                    funWriteTotal("Credit Remarks ", rs_BillHD.getString(11), BillOut, "Format5");
//                    BillOut.newLine();
//                    String custName = rs_BillHD.getString(24);
//                    if (!custName.isEmpty())
//                    {
//                        funWriteTotal("Customer " + custName, "", BillOut, "Format5");
//                    }
//                    BillOut.newLine();
//                    BillOut.write(Linefor5);
//                }
//            }
//
//            String sqlTenderAmt = "select sum(a.dblPaidAmt),sum(a.dblSettlementAmt),(sum(a.dblPaidAmt)-sum(a.dblSettlementAmt)) RefundAmt "
//                    + " from " + billSettlementdtl + " a," + billhd + " b "
//                    + " where a.strBillNo=b.strBillNo and a.strClientCode=b.strClientCode "
//                    + " and b.strBillNo='" + billNo + "' and b.strPOSCode='" + POSCode + "' "
//                    + " group by a.strBillNo";
//            ResultSet rsTenderAmt = clsGlobalVarClass.dbMysql.executeResultSet(sqlTenderAmt);
//            if (rsTenderAmt.next())
//            {
//                BillOut.newLine();
//                if (flgComplimentaryBill)
//                {
//                    funWriteTotal("PAID AMT", "0.00", BillOut, "Format5");
//                    BillOut.newLine();
//                }
//                else
//                {
//                    funWriteTotal("PAID AMT", rsTenderAmt.getString(1), BillOut, "Format5");
//                    BillOut.newLine();
//                    if (rsTenderAmt.getDouble(3) > 0)
//                    {
//                        funWriteTotal("REFUND AMT", rsTenderAmt.getString(3), BillOut, "Format5");
//                        BillOut.newLine();
//                    }
//                }
//                BillOut.write(Linefor5);
//            }
//            rsTenderAmt.close();
//
//            if (rs_BillHD.getDouble(29) > 0)
//            {
//                BillOut.newLine();
//                funWriteTotal("TIP AMT", rs_BillHD.getString(29), BillOut, "Format5");
//                BillOut.newLine();
//            }
//            if (flag_isHomeDelvBill)
//            {
//                BillOut.newLine();
//                String sql_count = "select count(*) from tblhomedelivery where strCustomerCode=?";
//                pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sql_count);
//                pst.setString(1, customerCode);
//                ResultSet rs_Count = pst.executeQuery();
//                rs_Count.next();
//                BillOut.write("  CUSTOMER COUNT : " + rs_Count.getString(1));
//                rs_Count.close();
//                BillOut.newLine();
//                BillOut.write(Linefor5);
//            }
//            BillOut.newLine();
//
//            funPrintServiceVatNo(BillOut, 4, billNo, billDate, billtaxdtl);
//
//            if (clsGlobalVarClass.gEnableBillSeries)
//            {
//                sql = "select b.strPrintInclusiveOfTaxOnBill "
//                        + " from tblbillseriesbilldtl a,tblbillseries b "
//                        + " where a.strBillSeries=b.strBillSeries and a.strHdBillNo='" + billNo + "' and a.strClientCode=b.strClientCode";
//                ResultSet rsBillSeries = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//                if (rsBillSeries.next())
//                {
//                    if (rsBillSeries.getString(1).equals("Y"))
//                    {
//                        BillOut.write(Line);
//                        BillOut.newLine();
//                        objUtility2.funPrintBlankSpace("(INCLUSIVE OF ALL TAXES)", BillOut);
//                        BillOut.write("(INCLUSIVE OF ALL TAXES)");
//                        BillOut.newLine();
//                    }
//                }
//                rsBillSeries.close();
//            }
//            else
//            {
//                if (clsGlobalVarClass.gPrintInclusiveOfAllTaxes.equalsIgnoreCase("Y"))
//                {
//                    BillOut.write(Line);
//                    BillOut.newLine();
//                    objUtility2.funPrintBlankSpace("(INCLUSIVE OF ALL TAXES)", BillOut);
//                    BillOut.write("(INCLUSIVE OF ALL TAXES)");
//                    BillOut.newLine();
//                }
//            }
//
//            int num = clsGlobalVarClass.gBillFooter.trim().length() / 30;
//            int num1 = clsGlobalVarClass.gBillFooter.trim().length() % 30;
//            int cnt1 = 0;
//            for (int cnt = 0; cnt < num; cnt++)
//            {
//                String footer = clsGlobalVarClass.gBillFooter.trim().substring(cnt1, (cnt1 + 30));
//                footer = footer.replaceAll("\n", "");
//                BillOut.write("     " + footer.trim());
//                BillOut.newLine();
//                cnt1 += 30;
//            }
//            BillOut.write("     " + clsGlobalVarClass.gBillFooter.trim().substring(cnt1, (cnt1 + num1)).trim());
//            BillOut.newLine();
//            objUtility2.funPrintBlankSpace(user, BillOut);
//            BillOut.write(user);
//            BillOut.newLine();
//            BillOut.newLine();
//            BillOut.newLine();
//            BillOut.newLine();
//            BillOut.newLine();
//
//            if (!clsGlobalVarClass.gOpenCashDrawerAfterBillPrintYN)
//            {
//                if ("linux".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
//                {
//                    BillOut.write("V");//Linux
//                }
//                else if ("windows".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
//                {
//                    if ("Inbuild".equalsIgnoreCase(clsPosConfigFile.gPrinterType))
//                    {
//                        BillOut.write("V");
//                    }
//                    else
//                    {
//                        BillOut.write("m");//windows
//                    }
//                }
//            }
//            rs_BillHD.close();
//            BillOut.close();
//            fstream_bill.close();
//            pst.close();
//
//            if (formName.equalsIgnoreCase("sales report"))
//            {
//                objUtility2.funShowTextFile(Text_Bill, formName, clsGlobalVarClass.gBillPrintPrinterPort);
//            }
//            else
//            {
//                if (clsGlobalVarClass.gShowBill)
//                {
//                    objUtility2.funShowTextFile(Text_Bill, formName, clsGlobalVarClass.gBillPrintPrinterPort);
//                }
//            }
//
//            if (!formName.equalsIgnoreCase("sales report"))
//            {
//                if (transType.equalsIgnoreCase("void"))
//                {
//                    if (clsGlobalVarClass.gPrintOnVoidBill)
//                    {
//                        if (!viewORprint.equalsIgnoreCase("view"))
//                        {
//                            funPrintToPrinter(clsGlobalVarClass.gBillPrintPrinterPort, "", "bill", "N", isReprint);
//                        }
//                    }
//                }
//                else
//                {
//                    if (!clsGlobalVarClass.flgReprintView)
//                    {
//                        if (!viewORprint.equalsIgnoreCase("view"))
//                        {
//                            funPrintToPrinter(clsGlobalVarClass.gBillPrintPrinterPort, "", "bill", "N", isReprint);
//                        }
//                    }
//                    else
//                    {
//                        clsGlobalVarClass.flgReprintView = false;
//                    }
//                }
//            }
//            //if (formName.equalsIgnoreCase("sales report"))
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    public void funGenerateTextFileBillPrintingForStockTransferNote(String billNo, String reprint, String formName, String transType, String billDate, String POSCode, String viewORprint)
//    {
//        clsUtility objUtility = new clsUtility();
//        clsUtility2 objUtility2 = new clsUtility2();
//
//        String Linefor5 = "  --------------------------------------";
//        try
//        {
//            String user = "";
//            String billhd = null;
//            String billdtl = null;
//            String billModifierdtl = null;
//            String billSettlementdtl = null;
//            String billtaxdtl = null;
//            String billDscFrom = null;
//            String billPromoDtl = null;
//            String advBookBillHd = null;
//            String advBookBillDtl = null;
//            String advBookBillCharDtl = null;
//            String advReceiptHd = null;
//            if (clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("HOPOS"))
//            {
//                billhd = "tblqbillhd";
//                billdtl = "tblqbilldtl";
//                billModifierdtl = "tblqbillmodifierdtl";
//                billSettlementdtl = "tblqbillsettlementdtl";
//                billtaxdtl = "tblqbilltaxdtl";
//                billDscFrom = "tblqbilldiscdtl";
//                billPromoDtl = "tblqbillpromotiondtl";
//
//                advBookBillHd = "tblqadvbookbillhd";
//                advBookBillDtl = "tblqadvbookbilldtl";
//                advBookBillCharDtl = "tblqadvbookbillchardtl";
//                advReceiptHd = "tblqadvancereceipthd";
//            }
//            else
//            {
//                if ("sales report".equalsIgnoreCase(formName))
//                {
//                    billhd = "tblbillhd";
//                    billdtl = "tblbilldtl";
//                    billModifierdtl = "tblbillmodifierdtl";
//                    billSettlementdtl = "tblbillsettlementdtl";
//                    billtaxdtl = "tblbilltaxdtl";
//                    billDscFrom = "tblbilldiscdtl";
//                    billPromoDtl = "tblbillpromotiondtl";
//                    advBookBillHd = "tbladvbookbillhd";
//                    advBookBillDtl = "tbladvbookbilldtl";
//                    advBookBillCharDtl = "tbladvbookbillchardtl";
//                    advReceiptHd = "tbladvancereceipthd";
//                    long dateDiff = new clsUtility().funCompareDate(billDate, objUtility.funGetPOSDateForTransaction());
//                    if (dateDiff > 0)
//                    {
//                        billhd = "tblqbillhd";
//                        billdtl = "tblqbilldtl";
//                        billModifierdtl = "tblqbillmodifierdtl";
//                        billSettlementdtl = "tblqbillsettlementdtl";
//                        billtaxdtl = "tblqbilltaxdtl";
//                        billDscFrom = "tblqbilldiscdtl";
//                        billPromoDtl = "tblqbillpromotiondtl";
//                        advBookBillHd = "tblqadvbookbillhd";
//                        advBookBillDtl = "tblqadvbookbilldtl";
//                        advBookBillCharDtl = "tblqadvbookbillchardtl";
//                        advReceiptHd = "tblqadvancereceipthd";
//                    }
//                    String sql = "select count(strBillNo) from tblbillhd where strBillNo='" + billNo + "' and strPOSCode='" + POSCode + "' and date(dteBillDate)='" + billDate + "' ";
//                    ResultSet rsBillTable = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//                    rsBillTable.next();
//                    int billCnt = rsBillTable.getInt(1);
//                    if (billCnt == 0)
//                    {
//                        billhd = "tblqbillhd";
//                        billdtl = "tblqbilldtl";
//                        billModifierdtl = "tblqbillmodifierdtl";
//                        billSettlementdtl = "tblqbillsettlementdtl";
//                        billtaxdtl = "tblqbilltaxdtl";
//                        billDscFrom = "tblqbilldiscdtl";
//                        billPromoDtl = "tblqbillpromotiondtl";
//                        advBookBillHd = "tblqadvbookbillhd";
//                        advBookBillDtl = "tblqadvbookbilldtl";
//                        advBookBillCharDtl = "tblqadvbookbillchardtl";
//                        advReceiptHd = "tblqadvancereceipthd";
//                    }
//                }
//                else
//                {
//                    billhd = "tblbillhd";
//                    billdtl = "tblbilldtl";
//                    billModifierdtl = "tblbillmodifierdtl";
//                    billSettlementdtl = "tblbillsettlementdtl";
//                    billtaxdtl = "tblbilltaxdtl";
//                    billDscFrom = "tblbilldiscdtl";
//                    billPromoDtl = "tblbillpromotiondtl";
//                    advBookBillHd = "tbladvbookbillhd";
//                    advBookBillDtl = "tbladvbookbilldtl";
//                    advBookBillCharDtl = "tbladvbookbillchardtl";
//                    advReceiptHd = "tbladvancereceipthd";
//                }
//            }
//            PreparedStatement pst = null;
//            funCreateTempFolder();
//            String filePath = System.getProperty("user.dir");
//            File Text_Bill = new File(filePath + "/Temp/Temp_Bill.txt");
//            String subTotal = "";
//            String grandTotal = "";
//            String advAmount = "";
//            String deliveryCharge = "";
//            String customerCode = "";
//            String waiterName = "";
//            String tblName = "";
//            ResultSet rs_BillHD = null;
//            boolean flgComplimentaryBill = false;
//
//            billDate = billDate.split(" ")[0];
//
//            StringBuilder sqlBillHeaderDtl = new StringBuilder();
//            sqlBillHeaderDtl.append("select ifnull(a.strTableNo,''),ifnull(a.strWaiterNo,''),a.dteBillDate,time(a.dteBillDate),a.dblDiscountAmt,a.dblSubTotal,"
//                    + "ifnull(a.strCustomerCode,''),a.dblGrandTotal,a.dblTaxAmt,ifnull(a.strReasonCode,''),ifnull(a.strRemarks,''),a.strUserCreated "
//                    + ",ifnull(dblDeliveryCharges,0.00),ifnull(i.dblAdvDeposite,0.00),a.dblDiscountPer,b.strPOSName,a.intPaxNo "
//                    + ",ifnull(c.strTableName,''),ifnull(d.strWShortName,''),ifnull(d.strWFullName,''),ifnull(l.strSettelmentType,''),ifnull(j.strReasonName,'') as voidedReason, "
//                    + "ifnull(g.strReasonName,''),ifnull(e.strCustomerName,''),ifnull(a.strAdvBookingNo,''),ifnull(h.strMessage,''),ifnull(h.strShape,''),ifnull(h.strNote,''),ifnull(a.dblTipAmount,0.00) "
//                    + ",a.strOperationType,ifnull(a.strTakeAwayRemarks,''),ifnull(e.longMobileNo,'')  "
//                    + "from " + billhd + " a "
//                    + "left outer join tblposmaster b on a.strPOSCode=b.strPosCode  "
//                    + "left outer join tbltablemaster c on a.strTableNo=c.strTableNo and a.strClientCode=c.strClientCode "
//                    + "left outer join tblwaitermaster d on a.strWaiterNo=d.strWaiterNo and a.strClientCode=d.strClientCode "
//                    + "left outer join tblcustomermaster e on a.strCustomerCode=e.strCustomerCode and a.strClientCode=e.strClientCode "
//                    + "left outer join tbldebitcardmaster f on a.strCardNo=f.strCardNo "
//                    + "left outer join tblreasonmaster g on a.strReasonCode=g.strReasonCode "
//                    + "left outer join " + advBookBillHd + " h on a.strAdvBookingNo=h.strAdvBookingNo and a.strClientCode=h.strClientCode "
//                    + "left outer join " + advReceiptHd + " i on h.strAdvBookingNo=i.strAdvBookingNo and a.strClientCode=i.strClientCode "
//                    + "left outer join tblvoidbillhd j on a.strBillNo=j.strBillNo and a.strPOSCode=j.strPosCode and a.strClientCode=j.strClientCode "
//                    + "left outer join " + billSettlementdtl + " k on a.strBillNo=k.strBillNo and a.strClientCode=k.strClientCode AND DATE(a.dteBillDate)=DATE(k.dteBillDate) "
//                    + "left outer join tblsettelmenthd l on k.strSettlementCode=l.strSettelmentCode "
//                    + "where a.strBillNo=? "
//                    + "and a.strPOSCode=? "
//                    + "and date(a.dteBillDate)=? "
//                    + "group by a.strBillNo; ");
//            pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlBillHeaderDtl.toString());
//            pst.setString(1, billNo);
//            pst.setString(2, POSCode);
//            pst.setString(3, billDate);
//            rs_BillHD = pst.executeQuery();
//            rs_BillHD.next();
//            if (rs_BillHD.getString(21).equals("Complementary"))
//            {
//                flgComplimentaryBill = true;
//            }
//            FileWriter fstream_bill = new FileWriter(Text_Bill);
//            BufferedWriter BillOut = new BufferedWriter(fstream_bill);
//
//            boolean isReprint = false;
//            if ("reprint".equalsIgnoreCase(reprint))
//            {
//                isReprint = true;
//                funPrintBlankSpace("[DUPLICATE]", BillOut);
//                BillOut.write("[DUPLICATE]");
//                BillOut.newLine();
//            }
//            if (transType.equals("Void"))
//            {
//                funPrintBlankSpace("VOIDED BILL", BillOut);
//                BillOut.write("VOIDED BILL");
//                BillOut.newLine();
//            }
//            boolean flag_isHomeDelvBill = false;
//            String SQL_HomeDelivery = "select strBillNo,strCustomerCode,strDPCode,tmeTime,strCustAddressLine1 "
//                    + "from tblhomedelivery "
//                    + "where strBillNo=? "
//                    + "and date(dteDate)=? ;";
//            pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(SQL_HomeDelivery);
//            pst.setString(1, billNo);
//            pst.setString(2, billDate);
//            ResultSet rs_HomeDelivery = pst.executeQuery();
//            if (rs_HomeDelivery.next())
//            {
//                flag_isHomeDelvBill = true;
//                customerCode = rs_HomeDelivery.getString(2);
//                if (clsGlobalVarClass.gPrintHomeDeliveryYN)
//                {
//                    funPrintBlankSpace("HOME DELIVERY", BillOut);
//                    BillOut.write("HOME DELIVERY");
//                    BillOut.newLine();
//                }
//
//                String SQL_CustomerDtl = "";
//                if (rs_HomeDelivery.getString(5).equals("Temporary"))
//                {
//                    SQL_CustomerDtl = "select a.strCustomerName,a.strTempAddress,a.strTempStreet"
//                            + " ,a.strTempLandmark,a.strBuildingName,a.strCity,a.intPinCode,a.longMobileNo "
//                            + " from tblcustomermaster a left outer join tblbuildingmaster b "
//                            + " on a.strBuldingCode=b.strBuildingCode "
//                            + " where a.strCustomerCode=? ;";
//                }
//                else if (rs_HomeDelivery.getString(5).equals("Office"))
//                {
//                    SQL_CustomerDtl = "select a.strCustomerName,a.strOfficeBuildingName,a.strOfficeStreetName"
//                            + ",a.strOfficeLandmark,a.strOfficeArea,a.strOfficeCity,a.strOfficePinCode,a.longMobileNo "
//                            + " from tblcustomermaster a "
//                            + " where a.strCustomerCode=? ";
//                }
//                else
//                {
//                    SQL_CustomerDtl = "select a.strCustomerName,a.strCustAddress,a.strStreetName"
//                            + " ,a.strLandmark,a.strBuildingName,a.strCity,a.intPinCode,a.longMobileNo "
//                            + " from tblcustomermaster a left outer join tblbuildingmaster b "
//                            + " on a.strBuldingCode=b.strBuildingCode "
//                            + " where a.strCustomerCode=? ;";
//                }
//                pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(SQL_CustomerDtl);
//                pst.setString(1, rs_HomeDelivery.getString(2));
//                ResultSet rs_CustomerDtl = pst.executeQuery();
//                while (rs_CustomerDtl.next())
//                {
//                    BillOut.write("  TO        :" + rs_CustomerDtl.getString(1).toUpperCase());
//                    BillOut.newLine();
//                    // Building Name    
//                    String add = rs_CustomerDtl.getString(2);
//                    int strlen = add.length();
//                    String add1 = "";
//                    if (strlen < 28)
//                    {
//                        add1 = add.substring(0, strlen);
//                        BillOut.write("  ADDRESS1  :" + add1.toUpperCase().replaceAll("\n", " "));
//                        BillOut.newLine();
//                    }
//                    else
//                    {
//                        add1 = add.substring(0, 28);
//                        BillOut.write("  ADDRESS1  :" + add1.toUpperCase().replaceAll("\n", " "));
//                        BillOut.newLine();
//                    }
//                    for (int i = 28; i <= strlen;)
//                    {
//                        int end = 0;
//                        end = i + 28;
//                        if (strlen > end)
//                        {
//                            add1 = add.substring(i, end);
//                            i = end;
//                            BillOut.write("             " + add1.toUpperCase().replaceAll("\n", " "));
//                            BillOut.newLine();
//                        }
//                        else
//                        {
//                            add1 = add.substring(i, strlen);
//                            BillOut.write("             " + add1.toUpperCase().replaceAll("\n", " "));
//                            BillOut.newLine();
//                            i = strlen + 1;
//                        }
//                    }
//                    // Street Name    
//                    String street = rs_CustomerDtl.getString(3);
//                    String street1;
//                    int streetlen = street.length();
//                    for (int i = 0; i <= streetlen;)
//                    {
//                        int end = 0;
//                        end = i + 28;
//                        if (streetlen > end)
//                        {
//                            street1 = street.substring(i, end);
//                            BillOut.write("             " + street1.toUpperCase());
//                            BillOut.newLine();
//                            i = end;
//                        }
//                        else
//                        {
//                            street1 = street.substring(i, streetlen);
//                            BillOut.write("             " + street1.toUpperCase());
//                            BillOut.newLine();
//                            i = streetlen + 1;
//                        }
//                    }
//                    // Landmark Name    
//                    if (rs_CustomerDtl.getString(4).trim().length() > 0)
//                    {
//                        BillOut.write("             " + rs_CustomerDtl.getString(4).toUpperCase());
//                        BillOut.newLine();
//                    }
//                    // Area Name    
//                    if (rs_CustomerDtl.getString(5).trim().length() > 0)
//                    {
//                        BillOut.write("             " + rs_CustomerDtl.getString(5).toUpperCase());
//                        BillOut.newLine();
//                    }
//                    // City Name    
//                    if (rs_CustomerDtl.getString(6).trim().length() > 0)
//                    {
//                        BillOut.write("             " + rs_CustomerDtl.getString(6).toUpperCase());
//                        BillOut.newLine();
//                    }
//                    // Pin Code    
//                    if (rs_CustomerDtl.getString(7).trim().length() > 0)
//                    {
//                        BillOut.write("             " + rs_CustomerDtl.getString(7).toUpperCase());
//                        BillOut.newLine();
//                    }
//                    // Mobile No    
//                    // BillOut.write("  MOBILE NO :" + rs_CustomerDtl.getString(8));
//                    //BillOut.newLine();
//                }
//                rs_CustomerDtl.close();
//                if (null != rs_HomeDelivery.getString(3) && rs_HomeDelivery.getString(3).trim().length() > 0)
//                {
//                    String[] delBoys = rs_HomeDelivery.getString(3).split(",");
//                    StringBuilder strIN = new StringBuilder("(");
//                    for (int i = 0; i < delBoys.length; i++)
//                    {
//                        if (i == 0)
//                        {
//                            strIN.append("'" + delBoys[i] + "'");
//                        }
//                        else
//                        {
//                            strIN.append(",'" + delBoys[i] + "'");
//                        }
//                    }
//                    strIN.append(")");
//                    String SQL_DeliveryBoyDtl = "select strDPName from tbldeliverypersonmaster where strDPCode IN " + strIN + " ;";
//                    pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(SQL_DeliveryBoyDtl);
//                    ResultSet rs_DeliveryBoyDtl = pst.executeQuery();
//                    strIN.setLength(0);
//                    for (int i = 0; rs_DeliveryBoyDtl.next(); i++)
//                    {
//                        if (i == 0)
//                        {
//                            strIN.append(rs_DeliveryBoyDtl.getString(1).toUpperCase());
//                        }
//                        else
//                        {
//                            strIN.append("," + rs_DeliveryBoyDtl.getString(1).toUpperCase());
//                        }
//                    }
//                    BillOut.write("  DELV BOY  :" + strIN);
//                    BillOut.newLine();
//                    rs_DeliveryBoyDtl.close();
//                }
//                BillOut.write(Line);
//                BillOut.newLine();
//            }
//            else
//            {
//                if (rs_BillHD.getString(7).length() > 0)//customerCode
//                {
//                    String SQL_CustomerDtl = "select a.strCustomerName,a.strCustAddress,a.strStreetName"
//                            + " ,a.strLandmark,a.strBuildingName,a.strCity,a.intPinCode,a.longMobileNo "
//                            + " from tblcustomermaster a left outer join tblbuildingmaster b "
//                            + " on a.strBuldingCode=b.strBuildingCode "
//                            + " where a.strCustomerCode='" + rs_BillHD.getString(7) + "';";
//                    ResultSet rs_CustomerDtl = clsGlobalVarClass.dbMysql.executeResultSet(SQL_CustomerDtl);
//                    while (rs_CustomerDtl.next())
//                    {
//                        BillOut.write("  TO        :" + rs_CustomerDtl.getString(1).toUpperCase());
//                        BillOut.newLine();
//                        // Building Name    
//                        String add = rs_CustomerDtl.getString(2);
//                        int strlen = add.length();
//                        String add1 = "";
//                        if (strlen < 28)
//                        {
//                            add1 = add.substring(0, strlen);
//                            BillOut.write("  ADDRESS1  :" + add1.toUpperCase().replaceAll("\n", " "));
//                            BillOut.newLine();
//                        }
//                        else
//                        {
//                            add1 = add.substring(0, 28);
//                            BillOut.write("  ADDRESS1  :" + add1.toUpperCase().replaceAll("\n", " "));
//                            BillOut.newLine();
//                        }
//                        for (int i = 28; i <= strlen;)
//                        {
//                            int end = 0;
//                            end = i + 28;
//                            if (strlen > end)
//                            {
//                                add1 = add.substring(i, end);
//                                i = end;
//                                BillOut.write("             " + add1.toUpperCase().replaceAll("\n", " "));
//                                BillOut.newLine();
//                            }
//                            else
//                            {
//                                add1 = add.substring(i, strlen);
//                                BillOut.write("             " + add1.toUpperCase().replaceAll("\n", " "));
//                                BillOut.newLine();
//                                i = strlen + 1;
//                            }
//                        }
//                        // Street Name    
//                        String street = rs_CustomerDtl.getString(3);
//                        String street1;
//                        int streetlen = street.length();
//                        for (int i = 0; i <= streetlen;)
//                        {
//                            int end = 0;
//                            end = i + 28;
//                            if (streetlen > end)
//                            {
//                                street1 = street.substring(i, end);
//                                BillOut.write("             " + street1.toUpperCase());
//                                BillOut.newLine();
//                                i = end;
//                            }
//                            else
//                            {
//                                street1 = street.substring(i, streetlen);
//                                BillOut.write("             " + street1.toUpperCase());
//                                BillOut.newLine();
//                                i = streetlen + 1;
//                            }
//                        }
//                        // Landmark Name    
//                        if (rs_CustomerDtl.getString(4).trim().length() > 0)
//                        {
//                            BillOut.write("             " + rs_CustomerDtl.getString(4).toUpperCase());
//                            BillOut.newLine();
//                        }
//                        // Area Name    
//                        if (rs_CustomerDtl.getString(5).trim().length() > 0)
//                        {
//                            BillOut.write("             " + rs_CustomerDtl.getString(5).toUpperCase());
//                            BillOut.newLine();
//                        }
//                        // City Name    
//                        if (rs_CustomerDtl.getString(6).trim().length() > 0)
//                        {
//                            BillOut.write("             " + rs_CustomerDtl.getString(6).toUpperCase());
//                            BillOut.newLine();
//                        }
//                        // Pin Code    
//                        if (rs_CustomerDtl.getString(7).trim().length() > 0)
//                        {
//                            BillOut.write("             " + rs_CustomerDtl.getString(7).toUpperCase());
//                            BillOut.newLine();
//                        }
//                        // Mobile No    
//                        // BillOut.write("  MOBILE NO :" + rs_CustomerDtl.getString(8));
//                        // BillOut.newLine();
//                    }
//
//                    // Mobile No    
//                    //BillOut.write("  MOBILE NO :" + rs_BillHD.getString(32));
//                    BillOut.write(Line);
//                    BillOut.newLine();
//                }
//            }
//            rs_HomeDelivery.close();
//            //print take away
//            int billPrintSize = 4;
//            if (rs_BillHD.getString(30).equals("TakeAway"))
//            {
//                funPrintBlankSpace("Take Away", BillOut);
//                BillOut.write("Take Away");
//                BillOut.newLine();
//            }
//            if (clsGlobalVarClass.gPrintTaxInvoice.equalsIgnoreCase("Y"))
//            {
//                funPrintBlankSpace("STOCK TRANSFER NOTE", BillOut);
//                BillOut.write("STOCK TRANSFER NOTE");
//                BillOut.newLine();
//            }
//
//            funPrintBlankSpace(clsGlobalVarClass.gClientName, BillOut);
//
//            BillOut.write(clsGlobalVarClass.gClientName.toUpperCase());
//            BillOut.newLine();
//            funPrintBlankSpace(clsGlobalVarClass.gClientAddress1, BillOut);
//            BillOut.write(clsGlobalVarClass.gClientAddress1.toUpperCase());
//            BillOut.newLine();
//            if (clsGlobalVarClass.gClientAddress2.trim().length() > 0)
//            {
//                funPrintBlankSpace(clsGlobalVarClass.gClientAddress2, BillOut);
//                BillOut.write(clsGlobalVarClass.gClientAddress2.toUpperCase());
//                BillOut.newLine();
//            }
//            if (clsGlobalVarClass.gClientAddress3.trim().length() > 0)
//            {
//                funPrintBlankSpace(clsGlobalVarClass.gClientAddress3, BillOut);
//                BillOut.write(clsGlobalVarClass.gClientAddress3.toUpperCase());
//                BillOut.newLine();
//            }
//            if (clsGlobalVarClass.gCityName.trim().length() > 0)
//            {
//                funPrintBlankSpace(clsGlobalVarClass.gCityName, BillOut);
//                BillOut.write(clsGlobalVarClass.gCityName.toUpperCase());
//                BillOut.newLine();
//            }
//
//            BillOut.write("  TEL NO.   :" + " ");
//            BillOut.write(String.valueOf(clsGlobalVarClass.gClientTelNo));
//            BillOut.newLine();
//            BillOut.write("  EMAIL ID  :" + " ");
//            BillOut.write(clsGlobalVarClass.gClientEmail);
//            BillOut.newLine();
//            tblName = rs_BillHD.getString(18);
//            if (tblName.length() > 0)
//            {
//               // BillOut.write("  TABLE NAME:" + "  ");
//
//                //BillOut.write(tblName);
//                // BillOut.newLine();
//            }
//            waiterName = rs_BillHD.getString(19);
//            if (waiterName.trim().length() > 0)
//            {
////                BillOut.write("  STEWARD   :" + "  ");
////                BillOut.write(waiterName);
////                BillOut.newLine();
//            }
//            BillOut.write(Linefor5);
//            BillOut.newLine();
//            BillOut.write("  POS                 : ");
//            BillOut.write(rs_BillHD.getString(16));
//            BillOut.newLine();
//            BillOut.write("  STOCK TRANSFER NOTE : ");
//            BillOut.write(billNo);
//            BillOut.newLine();
//            //  BillOut.write("  PAX NO.     : ");
//            //BillOut.write(rs_BillHD.getString(17));
//            //BillOut.newLine();
//            if (clsGlobalVarClass.gPrintTimeOnBillYN)
//            {
//                BillOut.write("  DATE & TIME : ");
//                SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy hh:mm a ");
//                BillOut.write(ft.format(rs_BillHD.getObject(3)));
//                BillOut.newLine();
//
//            }
//            else
//            {
//                BillOut.write("  DATE        : ");
//                SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy");
//                BillOut.write(ft.format(rs_BillHD.getObject(3)));
//                BillOut.newLine();
//            }
//            if (rs_BillHD.getString(11).trim().length() > 0 && !flgComplimentaryBill)
//            {
//                BillOut.write("  Remarks     : ");
//                BillOut.write(rs_BillHD.getString(11));
//                BillOut.newLine();
//            }
//            subTotal = rs_BillHD.getString(6);
//            grandTotal = rs_BillHD.getString(8);
//            user = rs_BillHD.getString(12);
//            deliveryCharge = rs_BillHD.getString(13);
//            advAmount = rs_BillHD.getString(14);
//            //print card available balance
//            String isSttled = "select a.strBillNo from " + billSettlementdtl + " a," + billhd + " b "
//                    + "where a.strBillNo=b.strBillNo "
//                    + "and a.strClientCode=b.strClientCode "
//                    + "and a.strBillNo='" + billNo + "' "
//                    + "and b.strPOSCode='" + POSCode + "' "
//                    + "and date(a.dteBillDate)=date(b.dteBillDate) ";
//            ResultSet rsIsSettled = clsGlobalVarClass.dbMysql.executeResultSet(isSttled);
//            if (rsIsSettled.next())
//            {
//                rsIsSettled.close();
//                String availBal = "select a.strCardNo,(b.dblRedeemAmt)"
//                        + "from " + billhd + " a inner join tbldebitcardmaster b on a.strCardNo=b.strCardNo "
//                        + "where a.strBillNo='" + billNo + "' "
//                        + "and a.strPOSCode='" + POSCode + "' "
//                        + "and date(a.dteBillDate)='" + billDate + "' ";
//                ResultSet rsAvailBal = clsGlobalVarClass.dbMysql.executeResultSet(availBal);
//                if (rsAvailBal.next())
//                {
//                    BillOut.write("  Available Balance(" + rsAvailBal.getString(1) + "):" + rsAvailBal.getString(2));
//                    BillOut.newLine();
//                }
//            }
//            else
//            {
//                String availBal = "select a.strCardNo,(b.dblRedeemAmt-a.dblGrandTotal)"
//                        + "from " + billhd + " a inner join tbldebitcardmaster b on a.strCardNo=b.strCardNo "
//                        + "where a.strBillNo='" + billNo + "' "
//                        + "and a.strPOSCode='" + POSCode + "' "
//                        + "and date(a.dteBillDate)='" + billDate + "' ";
//                ResultSet rsAvailBal = clsGlobalVarClass.dbMysql.executeResultSet(availBal);
//                if (rsAvailBal.next())
//                {
//                    BillOut.write("  Available Balance(" + rsAvailBal.getString(1) + "):" + rsAvailBal.getString(2));
//                    BillOut.newLine();
//                }
//            }
//            //print card available balance
//            if (transType.equals("Void"))
//            {
//                BillOut.write("  Reason      :" + " " + rs_BillHD.getString(22));//voided reason
//                BillOut.newLine();
//            }
//            else if (flgComplimentaryBill)
//            {
//
//                BillOut.write("  Reason      :" + " " + rs_BillHD.getString(23));
//                BillOut.newLine();
//                BillOut.write("  Remark      :" + " " + rs_BillHD.getString(11));
//                BillOut.newLine();
//            }
//            if (clsGlobalVarClass.gCMSIntegrationYN)
//            {
//                BillOut.write("  Member Code : ");
//                BillOut.write(rs_BillHD.getString(7));
//                BillOut.newLine();
//                BillOut.write("  Member Name : ");
//                objUtility2.funWriteToTextMemberNameForFormat5(BillOut, rs_BillHD.getString(24), "Format5");
//                BillOut.newLine();
//                BillOut.write(Linefor5);
//            }
//            if (rs_BillHD.getString(25) != null && rs_BillHD.getString(25).length() > 0)
//            {
//                if (rs_BillHD.getString(26).length() > 0 || rs_BillHD.getString(27).length() > 0 || rs_BillHD.getString(28).length() > 0)
//                {
//                    BillOut.newLine();
//                    funPrintBlankSpace("ORDER DETAIL", BillOut);
//                    BillOut.write("ORDER DETAIL");
//                    BillOut.newLine();
//                    BillOut.write(Linefor5);
//                    BillOut.newLine();
//                }
//                StringBuilder strValue = new StringBuilder();
//                strValue.setLength(0);
//                if (rs_BillHD.getString(26).length() > 0)
//                {
//                    strValue.append(rs_BillHD.getString(26));
//                }
//                else
//                {
//                    strValue.append("");
//                }
//                int strlenMsg = strValue.length();
//                if (strlenMsg > 0)
//                {
//                    String msg1 = "";
//                    if (strlenMsg < 27)
//                    {
//                        msg1 = strValue.substring(0, strlenMsg);
//                        BillOut.write("  MESSAGE     :" + msg1);
//                        BillOut.newLine();
//                    }
//                    else
//                    {
//                        msg1 = strValue.substring(0, 27);
//                        BillOut.write("  MESSAGE     :" + msg1);;
//                        BillOut.newLine();
//                    }
//                    for (int i = 27; i <= strlenMsg; i++)
//                    {
//                        int endmsg = 0;
//                        endmsg = i + 27;
//                        if (strlenMsg > endmsg)
//                        {
//                            msg1 = strValue.substring(i, endmsg);
//                            i = endmsg;
//                            BillOut.write("               " + msg1);
//                            BillOut.newLine();
//                        }
//                        else
//                        {
//                            msg1 = strValue.substring(i, strlenMsg);
//                            BillOut.write("               " + msg1);
//                            BillOut.newLine();
//                            i = strlenMsg + 1;
//                        }
//                    }
//                }
//                strValue.setLength(0);
//                if (rs_BillHD.getString(27).length() > 0)//shape
//                {
//                    strValue.append(rs_BillHD.getString(27));
//                }
//                else
//                {
//                    strValue.append("");
//                }
//                int strlenShape = strValue.length();
//                if (strlenShape > 0)
//                {
//                    String shape1 = "";
//                    if (strlenShape < 27)
//                    {
//                        shape1 = strValue.substring(0, strlenShape);
//                        BillOut.write("  SHAPE       :" + shape1);
//                        BillOut.newLine();
//                    }
//                    else
//                    {
//                        shape1 = strValue.substring(0, 27);
//                        BillOut.write("  SHAPE       :" + shape1);
//                        BillOut.newLine();
//                    }
//                    for (int j = 27; j <= strlenShape; j++)
//                    {
//                        int endShape = 0;
//                        endShape = j + 27;
//                        if (strlenShape > endShape)
//                        {
//                            shape1 = strValue.substring(j, endShape);
//                            j = endShape;
//                            BillOut.write("               " + shape1);
//                            BillOut.newLine();
//                        }
//                        else
//                        {
//                            shape1 = strValue.substring(j, strlenShape);
//                            BillOut.write("               " + shape1);
//                            BillOut.newLine();
//                            j = strlenShape + 1;
//                        }
//                    }
//                }
//
//                strValue.setLength(0);
//                if (rs_BillHD.getString(28).length() > 0)//note
//                {
//                    strValue.append(rs_BillHD.getString(28));
//                }
//                else
//                {
//                    strValue.append("");
//                }
//                int strlenNote = strValue.length();
//                if (strlenNote > 0)
//                {
//                    String note1 = "";
//                    if (strlenNote < 27)
//                    {
//                        note1 = strValue.substring(0, strlenNote);
//                        BillOut.write("  NOTE        :" + note1);
//                        BillOut.newLine();
//                    }
//                    else
//                    {
//                        note1 = strValue.substring(0, 27);
//                        BillOut.write("  NOTE        :" + note1);
//                        BillOut.newLine();
//                    }
//                    for (int i = 27; i <= strlenNote; i++)
//                    {
//                        int endNote = 0;
//                        endNote = i + 27;
//                        if (strlenNote > endNote)
//                        {
//                            note1 = strValue.substring(i, endNote);
//                            i = endNote;
//                            BillOut.write("               " + note1);
//                            BillOut.newLine();
//                        }
//                        else
//                        {
//                            note1 = strValue.substring(i, strlenNote);
//                            BillOut.write("               " + note1);
//                            BillOut.newLine();
//                            i = strlenNote + 1;
//                        }
//                    }
//                }
//                if (rs_BillHD.getString(26).length() > 0 || rs_BillHD.getString(27).length() > 0 || rs_BillHD.getString(28).length() > 0)
//                {
//
//                    BillOut.write(Linefor5);
//                    BillOut.newLine();
//                }
//            }
//
//            BillOut.write(Linefor5);
//            BillOut.newLine();
//            BillOut.write("     KG. ITEM NAME                  AMT");
//            BillOut.newLine();
//            BillOut.write(Linefor5);
//            BillOut.newLine();
//            double dblTotalQty = 0;
//            String SQL_BillDtl = "select sum(a.dblQuantity),left(a.strItemName,22) as ItemLine1"
//                    + " ,MID(a.strItemName,23,LENGTH(a.strItemName)) as ItemLine2"
//                    + " ,sum(a.dblAmount),a.strItemCode,a.strKOTNo "
//                    + " from " + billdtl + " a," + billhd + " b "
//                    + " where a.strBillNo=b.strBillNo "
//                    + " and a.strClientCode=b.strClientCode "
//                    + " and date(a.dteBillDate)=date(b.dteBillDate) "
//                    + " and a.strBillNo=? "
//                    + " and b.strPOSCode=?  "
//                    + " and date(b.dteBillDate)=? ";
//            if (!clsGlobalVarClass.gPrintTDHItemsInBill)
//            {
//                SQL_BillDtl += "and a.tdhYN='N' ";
//            }
//            if (!clsGlobalVarClass.gPrintOpenItemsOnBill)
//            {
//                SQL_BillDtl += "and a.dblAmount>0 ";
//            }
//            SQL_BillDtl += " group by a.strItemCode ";
//            pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(SQL_BillDtl);
//            pst.setString(1, billNo);
//            pst.setString(2, POSCode);
//            pst.setString(3, billDate);
//            ResultSet rs_BillDtl = pst.executeQuery();
//            while (rs_BillDtl.next())
//            {
//                double saleQty = rs_BillDtl.getDouble(1);
//                String sqlPromoBills = "select dblQuantity from " + billPromoDtl + " "
//                        + " where strBillNo='" + billNo + "' "
//                        + " and strItemCode='" + rs_BillDtl.getString(5) + "' "
//                        + " and strPromoType='ItemWise' "
//                        + " and date(dteBillDate)='" + billDate + "' ";
//                ResultSet rsPromoItems = clsGlobalVarClass.dbMysql.executeResultSet(sqlPromoBills);
//                if (rsPromoItems.next())
//                {
//                    saleQty -= rsPromoItems.getDouble(1);
//                }
//                rsPromoItems.close();
//                String qty = String.valueOf(saleQty);
//                if (qty.contains("."))
//                {
//                    String decVal = qty.substring(qty.length() - 2, qty.length());
//                    if (Double.parseDouble(decVal) == 0)
//                    {
//                        qty = qty.substring(0, qty.length() - 2);
//                    }
//                }
//                if (saleQty > 0)
//                {
//                    dblTotalQty += Double.parseDouble(qty);
//                    objUtility2.funPrintContentWithSpace("Right", qty, 8, BillOut);//Qty Print
//                    BillOut.write(" ");
//                    objUtility2.funPrintContentWithSpace("Left", rs_BillDtl.getString(2), 22, BillOut);//Item Name
//                    if (flgComplimentaryBill)
//                    {
//                        objUtility2.funPrintContentWithSpace("Right", "0.00", 9, BillOut);//Amount
//                    }
//                    else
//                    {
//                        objUtility2.funPrintContentWithSpace("Right", rs_BillDtl.getString(4), 9, BillOut);//Amount
//                    }
//                    BillOut.newLine();
//                    if (rs_BillDtl.getString(3).trim().length() > 0)
//                    {
//                        String line = rs_BillDtl.getString(3);
//                        if (line.length() > 22)
//                        {
//                            BillOut.write("         " + line.substring(0, 22));
//                            BillOut.newLine();
//
//                            BillOut.write("         " + line.substring(22, line.length()));
//                            BillOut.newLine();
//                        }
//                        else
//                        {
//                            BillOut.write("         " + line);
//                            BillOut.newLine();
//                        }
//                    }
//                    String sqlModifier = "select count(*) "
//                            + "from " + billModifierdtl + " "
//                            + "where strBillNo=? "
//                            + "and left(strItemCode,7)=? "
//                            + "and date(dteBillDate)=? ";
//                    if (!clsGlobalVarClass.gPrintZeroAmtModifierOnBill)
//                    {
//                        sqlModifier += " and  dblAmount !=0.00 ";
//                    }
//                    pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlModifier);
//                    pst.setString(1, billNo);
//                    pst.setString(2, rs_BillDtl.getString(5));
//                    pst.setString(3, billDate);
//                    ResultSet rs_count = pst.executeQuery();
//                    rs_count.next();
//                    int cntRecord = rs_count.getInt(1);
//                    rs_count.close();
//                    if (cntRecord > 0)
//                    {
//                        sqlModifier = "select strModifierName,dblQuantity,dblAmount "
//                                + " from " + billModifierdtl + " "
//                                + " where strBillNo=? "
//                                + " and left(strItemCode,7)=? "
//                                + " and date(dteBillDate)=? ";
//                        if (!clsGlobalVarClass.gPrintZeroAmtModifierOnBill)
//                        {
//                            sqlModifier += " and  dblAmount !=0.00 ";
//                        }
//                        pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlModifier);
//                        pst.setString(1, billNo);
//                        pst.setString(2, rs_BillDtl.getString(5));
//                        pst.setString(3, billDate);
//                        ResultSet rs_modifierRecord = pst.executeQuery();
//                        while (rs_modifierRecord.next())
//                        {
//                            if (flgComplimentaryBill)
//                            {
//                                objUtility2.funWriteToTextformat5(BillOut, "", rs_modifierRecord.getString(1).toUpperCase(), "0.00", "Format5");
//                                BillOut.newLine();
//                            }
//                            else
//                            {
//                                objUtility2.funWriteToTextformat5(BillOut, "", rs_modifierRecord.getString(1).toUpperCase(), rs_modifierRecord.getString(3), "Format5");
//                                BillOut.newLine();
//                            }
//                        }
//                        rs_modifierRecord.close();
//                    }
//
//                    sql = "select b.strItemCode,b.dblWeight "
//                            + " from " + billhd + " a," + advBookBillDtl + " b "
//                            + " where a.strAdvBookingNo=b.strAdvBookingNo "
//                            + " and a.strClientCode=b.strClientCode "
//                            + " and a.strBillNo='" + billNo + "' "
//                            + " and b.strItemCode='" + rs_BillDtl.getString(5) + "' "
//                            + " and a.strPOSCode='" + POSCode + "' "
//                            + " and date(a.dteBillDate)='" + billDate + "' ";
//                    ResultSet rsWeight = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//                    while (rsWeight.next())
//                    {
//                        BillOut.write("     Weight");
//                        BillOut.write("     " + rsWeight.getDouble(2));
//                        BillOut.newLine();
//                    }
//                    rsWeight.close();
//                    sql = "select c.strCharName,b.strCharValues "
//                            + " from " + billhd + " a," + advBookBillCharDtl + " b,tblcharactersticsmaster c "
//                            + " where a.strAdvBookingNo=b.strAdvBookingNo "
//                            + " and b.strCharCode=c.strCharCode "
//                            + " and a.strBillNo='" + billNo + "' "
//                            + " and b.strItemCode='" + rs_BillDtl.getString(5) + "' "
//                            + " and a.strPOSCode='" + POSCode + "' "
//                            + " and date(a.dteBillDate)='" + billDate + "' "
//                            + " and a.strClientCode=b.strClientCode ";
//                    ResultSet rsCharDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//                    while (rsCharDtl.next())
//                    {
//                        String charName = objUtility.funPrintTextWithAlignment(rsCharDtl.getString(1), 12, "Left");
//                        BillOut.write("     " + charName);
//                        String charVal = objUtility.funPrintTextWithAlignment(rsCharDtl.getString(2), 28, "Left");
//                        BillOut.write("     " + charVal);
//                        BillOut.newLine();
//                    }
//                    rsCharDtl.close();
//                }
//            }
//            rs_BillDtl.close();
//            funPrintPromoItemsInBill(billNo, BillOut, 4);  // Print Promotion Items in Bill for this billno.
//            BillOut.write(Linefor5);
//            BillOut.newLine();
//            funWriteTotal("TOTAL", "", BillOut, "Format5");
//            BillOut.newLine();
//            String qty = String.valueOf(decimalFormat.format(dblTotalQty));
//
//            if (clsGlobalVarClass.gPointsOnBillPrint)
//            {
//                String sqlCRMPoints = "select b.dblPoints from " + billhd + " a, tblcrmpoints b "
//                        + " where a.strBillNo=b.strBillNo "
//                        + " and a.strClientCode=b.strClientCode "
//                        + " and a.strBillNo='" + billNo + "' "
//                        + " and a.strPOSCode='" + POSCode + "' "
//                        + " and date(a.dteBillDate)='" + billDate + "' ";
//                ResultSet rsCRMPoints = clsGlobalVarClass.dbMysql.executeResultSet(sqlCRMPoints);
//                if (rsCRMPoints.next())
//                {
//                    funWriteTotal("POINTS ", rsCRMPoints.getString(1), BillOut, "Format5");
//                }
//                rsCRMPoints.close();
//                BillOut.newLine();
//            }
//            if (flgComplimentaryBill)
//            {
//                objUtility2.funPrintContentWithSpace("Right", qty, 8, BillOut);//total Qty 
//                BillOut.write(" ");
//                objUtility2.funPrintContentWithSpace("Left", " ", 22, BillOut);//space
//                objUtility2.funPrintContentWithSpace("Right", "0.00", 9, BillOut);//Total
//                //funWriteTotal("     " + qty, "0.00", BillOut, "Format5");
//                BillOut.newLine();
//            }
//            else
//            {
//                objUtility2.funPrintContentWithSpace("Right", qty, 8, BillOut);//total Qty 
//                BillOut.write(" ");
//                objUtility2.funPrintContentWithSpace("Left", " ", 22, BillOut);//space
//                objUtility2.funPrintContentWithSpace("Right", subTotal, 9, BillOut);//Total
//                //funWriteTotal("     " + qty, subTotal, BillOut, "Format5");
//                BillOut.newLine();
//            }
//            sql = "select a.dblDiscPer,a.dblDiscAmt,a.strDiscOnType,a.strDiscOnValue,b.strReasonName,a.strDiscRemarks "
//                    + " from " + billDscFrom + " a ,tblreasonmaster b," + billhd + " c "
//                    + " where  a.strDiscReasonCode=b.strReasonCode "
//                    + " and a.strBillNo=c.strBillNo "
//                    + " and a.strClientCode=c.strClientCode "
//                    + " and date(a.dteBillDate)=date(c.dteBillDate) "
//                    + " and a.strBillNo='" + billNo + "' "
//                    + " and c.strPOSCode='" + POSCode + "' "
//                    + " and date(c.dteBillDate)='" + billDate + "' ";
//            ResultSet rsDisc = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//            boolean flag = true;
//            while (rsDisc.next())
//            {
//                if (flag)
//                {
//                    flag = false;
//                    BillOut.write("  DISCOUNT");
//                    BillOut.newLine();
//                }
//                double dbl = Double.parseDouble(rsDisc.getString("dblDiscPer"));
//                String dbl2 = decimalFormat.format(Math.rint(dbl));
////                String discText = String.format("%.1f", dbl) + "%" + " On " + rsDisc.getString("strDiscOnValue") + "";
//                String discText = dbl2 + "%" + " On " + rsDisc.getString("strDiscOnValue") + "";
//                if (discText.length() > 30)
//                {
//                    discText = discText.substring(0, 30);
//                }
//                else
//                {
//                    discText = String.format("%-30s", discText);
//                }
//                BillOut.write("  " + discText);
//                String discountOnItem = objUtility.funPrintTextWithAlignment(String.valueOf(decimalFormatFor2DecPoint.format(Math.rint(rsDisc.getDouble("dblDiscAmt")))), 8, "Right");
//                BillOut.write(discountOnItem);
//                BillOut.newLine();
//                BillOut.write("  Reason  : ");
//                String discReason = objUtility.funPrintTextWithAlignment(rsDisc.getString(5), 20, "Left");
//                BillOut.write(discReason);
//                BillOut.newLine();
//                BillOut.write("  Remarks : ");
//                String discRemarks = objUtility.funPrintTextWithAlignment(rsDisc.getString(6), 20, "Left");
//                BillOut.write(discRemarks);
//                BillOut.newLine();
//
//            }
//            String sql_Tax = "select b.strTaxDesc,sum(a.dblTaxAmount) "
//                    + " from " + billtaxdtl + " a,tbltaxhd b," + billhd + " c "
//                    + " where a.strBillNo='" + billNo + "' "
//                    + " and a.strTaxCode=b.strTaxCode "
//                    + " and a.strBillNo=c.strBillNo "
//                    + " and a.strClientCode=c.strClientCode "
//                    + " and date(a.dteBillDate)=date(c.dteBillDate) "
//                    + " and c.strPOSCode='" + POSCode + "' "
//                    + " and b.strTaxCalculation='Forward' "
//                    + " and date(c.dteBillDate)='" + billDate + "' "
//                    + " group by a.strTaxCode";
//            ResultSet rsTax = clsGlobalVarClass.dbMysql.executeResultSet(sql_Tax);
//            while (rsTax.next())
//            {
//                if (flgComplimentaryBill)
//                {
//                    funWriteTotal(rsTax.getString(1), "0.00", BillOut, "Format5");
//                    BillOut.newLine();
//                }
//                else
//                {
//                    funWriteTotal(rsTax.getString(1), rsTax.getString(2), BillOut, "Format5");
//                    BillOut.newLine();
//                }
//            }
//            if (deliveryCharge != null && deliveryCharge.trim().length() > 0 && !"0.00".equalsIgnoreCase(deliveryCharge))
//            {
//                funWriteTotal("DELV. CHARGE", deliveryCharge, BillOut, "Format5");
//                BillOut.newLine();
//            }
//            if (advAmount.trim().length() > 0 && !"0.00".equalsIgnoreCase(advAmount))
//            {
//                funWriteTotal("ADVANCE", advAmount, BillOut, "Format5");
//                BillOut.newLine();
//            }
//            BillOut.write(Linefor5);
//            BillOut.newLine();
//            if (flgComplimentaryBill)
//            {
//                funWriteTotal("TOTAL(ROUNDED)", "0.00", BillOut, "Format5");
//                BillOut.newLine();
//                BillOut.write(Linefor5);
//            }
//            else
//            {
//                funWriteTotal("TOTAL(ROUNDED)", grandTotal, BillOut, "Format5");
//                BillOut.newLine();
//                BillOut.write(Linefor5);
//            }
//
//            //print Grand total of other bill nos from bill series
//            if (clsGlobalVarClass.gEnableBillSeries)
//            {
//                String sqlPrintGT = "select a.strPrintGTOfOtherBills,b.strDtlBillNos,b.dblGrandTotal "
//                        + "from tblbillseries a,tblbillseriesbilldtl b "
//                        + "where (a.strPOSCode=b.strPOSCode or a.strPOSCode='All') "
//                        + "and a.strBillSeries=b.strBillSeries "
//                        + "and b.strHdBillNo='" + billNo + "' "
//                        + "and b.strPOSCode='" + POSCode + "' "
//                        + "and date(b.dteBillDate)='" + billDate + "' ";
//                ResultSet rsPrintGTYN = clsGlobalVarClass.dbMysql.executeResultSet(sqlPrintGT);
//                double dblOtherBillsGT = 0.00;
//                if (rsPrintGTYN.next())
//                {
//                    if (rsPrintGTYN.getString(1).equalsIgnoreCase("Y"))
//                    {
//                        String billSeriesDtlBillNos = rsPrintGTYN.getString(2);
//                        String[] dtlBillSeriesBillNo = billSeriesDtlBillNos.split(",");
//                        dblOtherBillsGT += rsPrintGTYN.getDouble(3);
//                        if (dtlBillSeriesBillNo.length > 0)
//                        {
//                            for (int i = 0; i < dtlBillSeriesBillNo.length; i++)
//                            {
//                                sqlPrintGT = "select a.strHdBillNo,a.dblGrandTotal "
//                                        + "from tblbillseriesbilldtl a "
//                                        + "where a.strHdBillNo='" + dtlBillSeriesBillNo[i] + "' "
//                                        + "and a.strPOSCode='" + POSCode + "' "
//                                        + "and date(a.dteBillDate)='" + billDate + "' ";
//                                ResultSet rsPrintGT = clsGlobalVarClass.dbMysql.executeResultSet(sqlPrintGT);
//                                if (rsPrintGT.next())
//                                {
//                                    BillOut.newLine();
//                                    funWriteTotal(dtlBillSeriesBillNo[i] + " TOTAL(ROUNDED)", rsPrintGT.getString(2), BillOut, "Format5");
//                                    dblOtherBillsGT += rsPrintGT.getDouble(2);
//                                    BillOut.newLine();
//                                }
//                            }
//                            BillOut.newLine();
//                            BillOut.write(Linefor5);
//                            BillOut.newLine();
//                            funWriteTotal("GRAND TOTAL(ROUNDED)", String.valueOf(dblOtherBillsGT), BillOut, "Format5");
//                            BillOut.newLine();
//                            BillOut.write(Linefor5);
//                            BillOut.newLine();
//                        }
//                    }
//                }
//            }
//
//            //settlement breakup part
//            String sqlSettlementBreakup = "select a.dblSettlementAmt, b.strSettelmentDesc, b.strSettelmentType "
//                    + " from " + billSettlementdtl + " a ,tblsettelmenthd b," + billhd + " c "
//                    + " where a.strBillNo=? "
//                    + " and a.strBillNo=c.strBillNo "
//                    + " and a.strClientCode=c.strClientCode "
//                    + " and a.strSettlementCode=b.strSettelmentCode "
//                    + " AND date(a.dteBillDate)=date(c.dteBillDate) "
//                    + " and c.strPOSCode=? "
//                    + " and date(c.dteBillDate)=? ";
//            pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlSettlementBreakup);
//            pst.setString(1, billNo);
//            pst.setString(2, POSCode);
//            pst.setString(3, billDate);
//            boolean flgSettlement = false;
//            boolean creditSettlement = false;
//            ResultSet rsBillSettlement = pst.executeQuery();
//            while (rsBillSettlement.next())
//            {
//                if (flgComplimentaryBill)
//                {
//                    BillOut.newLine();
//                    funWriteTotal(rsBillSettlement.getString(2), "0.00", BillOut, "Format5");
//                }
//                else
//                {
//                    BillOut.newLine();
//                    funWriteTotal(rsBillSettlement.getString(2), rsBillSettlement.getString(1), BillOut, "Format5");
//                }
//                flgSettlement = true;
//                if (rsBillSettlement.getString(3).equals("Credit"))
//                {
//                    creditSettlement = true;
//                }
//            }
//            rsBillSettlement.close();
//
//            if (flgSettlement)
//            {
//                BillOut.newLine();
//                if (creditSettlement)
//                {
//                    funWriteTotal("Credit Remarks ", rs_BillHD.getString(11), BillOut, "Format5");
//                    BillOut.newLine();
//                    String custName = rs_BillHD.getString(24);
//                    if (!custName.isEmpty())
//                    {
//                        // funWriteTotal("Customer " + custName, "", BillOut, "Format5");
//                    }
//                    // BillOut.newLine();
//                    // BillOut.write(Linefor5);
//                }
//            }
//
//            String sqlTenderAmt = "select sum(a.dblPaidAmt),sum(a.dblSettlementAmt),(sum(a.dblPaidAmt)-sum(a.dblSettlementAmt)) RefundAmt "
//                    + " from " + billSettlementdtl + " a," + billhd + " b "
//                    + " where a.strBillNo=b.strBillNo "
//                    + " and a.strClientCode=b.strClientCode "
//                    + " AND date(a.dteBillDate)=date(b.dteBillDate) "
//                    + " and b.strBillNo='" + billNo + "' "
//                    + " and b.strPOSCode='" + POSCode + "' "
//                    + " and date(b.dteBillDate)='" + billDate + "' "
//                    + " group by a.strBillNo";
//            ResultSet rsTenderAmt = clsGlobalVarClass.dbMysql.executeResultSet(sqlTenderAmt);
//            if (rsTenderAmt.next())
//            {
//                BillOut.newLine();
//                if (flgComplimentaryBill)
//                {
//                    funWriteTotal("PAID AMT", "0.00", BillOut, "Format5");
//                    BillOut.newLine();
//                }
//                else
//                {
//                    funWriteTotal("PAID AMT", rsTenderAmt.getString(1), BillOut, "Format5");
//                    BillOut.newLine();
//                    if (rsTenderAmt.getDouble(3) > 0)
//                    {
//                        funWriteTotal("REFUND AMT", rsTenderAmt.getString(3), BillOut, "Format5");
//                        BillOut.newLine();
//                    }
//                }
//                BillOut.write(Linefor5);
//            }
//            rsTenderAmt.close();
//
//            if (rs_BillHD.getDouble(29) > 0)
//            {
//                BillOut.newLine();
//                funWriteTotal("TIP AMT", rs_BillHD.getString(29), BillOut, "Format5");
//                BillOut.newLine();
//            }
//            if (flag_isHomeDelvBill)
//            {
//                BillOut.newLine();
//                String sql_count = "select count(*) from tblhomedelivery where strCustomerCode=?";
//                pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sql_count);
//                pst.setString(1, customerCode);
//                ResultSet rs_Count = pst.executeQuery();
//                rs_Count.next();
//                // BillOut.write("  CUSTOMER COUNT : " + rs_Count.getString(1));
//                rs_Count.close();
//                BillOut.newLine();
//                // BillOut.write(Linefor5);
//            }
//            BillOut.newLine();
//
//            /**
//             * print Tax Nos
//             */
//            funPrintServiceVatNo(BillOut, 4, billNo, billDate, billtaxdtl);
//
//            if (clsGlobalVarClass.gEnableBillSeries)
//            {
//                sql = "select b.strPrintInclusiveOfTaxOnBill "
//                        + " from tblbillseriesbilldtl a,tblbillseries b "
//                        + " where a.strBillSeries=b.strBillSeries "
//                        + " and a.strHdBillNo='" + billNo + "' "
//                        + " and a.strClientCode=b.strClientCode "
//                        + " and date(a.dteBillDate)='" + billDate + "' ";
//                ResultSet rsBillSeries = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//                if (rsBillSeries.next())
//                {
//                    if (rsBillSeries.getString(1).equals("Y"))
//                    {
//                        BillOut.write(Line);
//                        BillOut.newLine();
//                        funPrintBlankSpace("(INCLUSIVE OF ALL TAXES)", BillOut);
//                        BillOut.write("(INCLUSIVE OF ALL TAXES)");
//                        BillOut.newLine();
//                    }
//                }
//                rsBillSeries.close();
//            }
//            else
//            {
//                if (clsGlobalVarClass.gPrintInclusiveOfAllTaxes.equalsIgnoreCase("Y"))
//                {
//                    BillOut.write(Line);
//                    BillOut.newLine();
//                    funPrintBlankSpace("(INCLUSIVE OF ALL TAXES)", BillOut);
//                    BillOut.write("(INCLUSIVE OF ALL TAXES)");
//                    BillOut.newLine();
//                }
//            }
//
//            int num = clsGlobalVarClass.gBillFooter.trim().length() / 30;
//            int num1 = clsGlobalVarClass.gBillFooter.trim().length() % 30;
//            int cnt1 = 0;
//            for (int cnt = 0; cnt < num; cnt++)
//            {
//                String footer = clsGlobalVarClass.gBillFooter.trim().substring(cnt1, (cnt1 + 30));
//                footer = footer.replaceAll("\n", "");
//                BillOut.write("     " + footer.trim());
//                BillOut.newLine();
//                cnt1 += 30;
//            }
//            BillOut.write("     " + clsGlobalVarClass.gBillFooter.trim().substring(cnt1, (cnt1 + num1)).trim());
//            BillOut.newLine();
//            funPrintBlankSpace(user, BillOut);
//            BillOut.write(user);
//            BillOut.newLine();
//            BillOut.newLine();
//            BillOut.newLine();
//            BillOut.newLine();
//            BillOut.newLine();
//
//            if (!clsGlobalVarClass.gOpenCashDrawerAfterBillPrintYN)
//            {
//                if ("linux".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
//                {
//                    BillOut.write("V");//Linux
//                }
//                else if ("windows".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
//                {
//                    if ("Inbuild".equalsIgnoreCase(clsPosConfigFile.gPrinterType))
//                    {
//                        BillOut.write("V");
//                    }
//                    else
//                    {
//                        BillOut.write("m");//windows
//                    }
//                }
//            }
//            rs_BillHD.close();
//            BillOut.close();
//            fstream_bill.close();
//            pst.close();
//
//            if (formName.equalsIgnoreCase("sales report"))
//            {
//                funShowTextFile(Text_Bill, formName, clsGlobalVarClass.gBillPrintPrinterPort);
//            }
//            else
//            {
//                if (clsGlobalVarClass.gShowBill)
//                {
//                    funShowTextFile(Text_Bill, formName, clsGlobalVarClass.gBillPrintPrinterPort);
//                }
//            }
//
//            if (!formName.equalsIgnoreCase("sales report"))
//            {
//                if (transType.equalsIgnoreCase("void"))
//                {
//                    if (clsGlobalVarClass.gPrintOnVoidBill)
//                    {
//                        if (!viewORprint.equalsIgnoreCase("view"))
//                        {
//                            funPrintToPrinter(clsGlobalVarClass.gBillPrintPrinterPort, "", "bill", "N", isReprint);
//                        }
//                    }
//                }
//                else
//                {
//                    if (!clsGlobalVarClass.flgReprintView)
//                    {
//                        if (!viewORprint.equalsIgnoreCase("view"))
//                        {
//                            funPrintToPrinter(clsGlobalVarClass.gBillPrintPrinterPort, "", "bill", "N", isReprint);
//                        }
//                    }
//                    else
//                    {
//                        clsGlobalVarClass.flgReprintView = false;
//                    }
//                }
//            }
//            //if (formName.equalsIgnoreCase("sales report"))
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//
////for banana leaf and stationery bill 2
//    private void funWriteTotalStationery(String title, String total, BufferedWriter out, String format, String space)
//    {
//        try
//        {
//            int counter = 0;
//            out.write(space);
//            counter = counter + 2;
//            int length = title.length();
//            out.write(title);
//            counter = counter + length;
//            funWriteFormattedAmt(counter, total, out, format);
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
////for stationery bill to print blank lines
//    // currentLineCount-- current line number, totalLineCount -- total number of line in single page , topSpaceLineCount --no of line from actual quantity & items printing start,
//    //totalLinesForNextPage -- required blank line to contiouse print on next page ,
//
//    private int funWriteBlankLines(int currentLineCount, int totalLineCount, int topSpaceLineCount, int totalLinesForNextPage, BufferedWriter out)
//    {
//        try
//        {
//            if (currentLineCount == totalLineCount)
//            {
//                for (int i = 0; i < totalLinesForNextPage; i++)
//                {
//                    out.newLine();
//                }
//                currentLineCount = topSpaceLineCount;
//            }
//        }
//        catch (Exception e)
//        {
//
//        }
//        return currentLineCount;
//    }
//
//    public void funGenerateTextFileBillPrintingForStationery(String billNo, String reprint, String formName, String transType, String billDate, String POSCode, String viewORprint)
//    {
//        int lineCount = 0;
//        clsUtility objUtility = new clsUtility();
//        clsUtility2 objuUtility2 = new clsUtility2();
//        String Linefor5 = "--------------------------------------";
//        try
//        {
//            String user = "";
//            String billhd = null;
//            String billdtl = null;
//            String billModifierdtl = null;
//            String billSettlementdtl = null;
//            String billtaxdtl = null;
//            String billDscFrom = null;
//            String billPromoDtl = null;
//            String advBookBillHd = null;
//            String advBookBillDtl = null;
//            String advBookBillCharDtl = null;
//            String advReceiptHd = null;
//            if (clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("HOPOS"))
//            {
//                billhd = "tblqbillhd";
//                billdtl = "tblqbilldtl";
//                billModifierdtl = "tblqbillmodifierdtl";
//                billSettlementdtl = "tblqbillsettlementdtl";
//                billtaxdtl = "tblqbilltaxdtl";
//                billDscFrom = "tblqbilldiscdtl";
//                billPromoDtl = "tblqbillpromotiondtl";
//
//                advBookBillHd = "tblqadvbookbillhd";
//                advBookBillDtl = "tblqadvbookbilldtl";
//                advBookBillCharDtl = "tblqadvbookbillchardtl";
//                advReceiptHd = "tblqadvancereceipthd";
//            }
//            else
//            {
//                if ("sales report".equalsIgnoreCase(formName))
//                {
//                    billhd = "tblbillhd";
//                    billdtl = "tblbilldtl";
//                    billModifierdtl = "tblbillmodifierdtl";
//                    billSettlementdtl = "tblbillsettlementdtl";
//                    billtaxdtl = "tblbilltaxdtl";
//                    billDscFrom = "tblbilldiscdtl";
//                    billPromoDtl = "tblbillpromotiondtl";
//                    advBookBillHd = "tbladvbookbillhd";
//                    advBookBillDtl = "tbladvbookbilldtl";
//                    advBookBillCharDtl = "tbladvbookbillchardtl";
//                    advReceiptHd = "tbladvancereceipthd";
//                    long dateDiff = new clsUtility().funCompareDate(billDate, objUtility.funGetPOSDateForTransaction());
//                    if (dateDiff > 0)
//                    {
//                        billhd = "tblqbillhd";
//                        billdtl = "tblqbilldtl";
//                        billModifierdtl = "tblqbillmodifierdtl";
//                        billSettlementdtl = "tblqbillsettlementdtl";
//                        billtaxdtl = "tblqbilltaxdtl";
//                        billDscFrom = "tblqbilldiscdtl";
//                        billPromoDtl = "tblqbillpromotiondtl";
//                        advBookBillHd = "tblqadvbookbillhd";
//                        advBookBillDtl = "tblqadvbookbilldtl";
//                        advBookBillCharDtl = "tblqadvbookbillchardtl";
//                        advReceiptHd = "tblqadvancereceipthd";
//                    }
//                    String sql = "select count(strBillNo) from tblbillhd where strBillNo='" + billNo + "' and strPOSCode='" + POSCode + "' ";
//                    ResultSet rsBillTable = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//                    rsBillTable.next();
//                    int billCnt = rsBillTable.getInt(1);
//                    if (billCnt == 0)
//                    {
//                        billhd = "tblqbillhd";
//                        billdtl = "tblqbilldtl";
//                        billModifierdtl = "tblqbillmodifierdtl";
//                        billSettlementdtl = "tblqbillsettlementdtl";
//                        billtaxdtl = "tblqbilltaxdtl";
//                        billDscFrom = "tblqbilldiscdtl";
//                        billPromoDtl = "tblqbillpromotiondtl";
//                        advBookBillHd = "tblqadvbookbillhd";
//                        advBookBillDtl = "tblqadvbookbilldtl";
//                        advBookBillCharDtl = "tblqadvbookbillchardtl";
//                        advReceiptHd = "tblqadvancereceipthd";
//                    }
//                }
//                else
//                {
//                    billhd = "tblbillhd";
//                    billdtl = "tblbilldtl";
//                    billModifierdtl = "tblbillmodifierdtl";
//                    billSettlementdtl = "tblbillsettlementdtl";
//                    billtaxdtl = "tblbilltaxdtl";
//                    billDscFrom = "tblbilldiscdtl";
//                    billPromoDtl = "tblbillpromotiondtl";
//                    advBookBillHd = "tbladvbookbillhd";
//                    advBookBillDtl = "tbladvbookbilldtl";
//                    advBookBillCharDtl = "tbladvbookbillchardtl";
//                    advReceiptHd = "tbladvancereceipthd";
//                }
//            }
//            PreparedStatement pst = null;
//            funCreateTempFolder();
//            String filePath = System.getProperty("user.dir");
//            File Text_Bill = new File(filePath + "/Temp/Temp_Bill.txt");
//            String subTotal = "";
//            String grandTotal = "";
//            String advAmount = "";
//            String deliveryCharge = "";
//            String customerCode = "";
//            String waiterName = "";
//            String tblName = "";
//            ResultSet rs_BillHD = null;
//            boolean flgComplimentaryBill = false;
//            StringBuilder sqlBillHeaderDtl = new StringBuilder();
//            sqlBillHeaderDtl.append("select ifnull(a.strTableNo,''),ifnull(a.strWaiterNo,''),a.dteBillDate,time(a.dteBillDate),a.dblDiscountAmt,a.dblSubTotal,"
//                    + "ifnull(a.strCustomerCode,''),a.dblGrandTotal,a.dblTaxAmt,ifnull(a.strReasonCode,''),ifnull(a.strRemarks,''),a.strUserCreated "
//                    + ",ifnull(dblDeliveryCharges,0.00),ifnull(i.dblAdvDeposite,0.00),a.dblDiscountPer,b.strPOSName,a.intPaxNo "
//                    + ",ifnull(c.strTableName,''),ifnull(d.strWShortName,''),ifnull(d.strWFullName,''),ifnull(l.strSettelmentType,''),ifnull(j.strReasonName,'') as voidedReason, "
//                    + "ifnull(g.strReasonName,''),ifnull(e.strCustomerName,''),ifnull(a.strAdvBookingNo,''),ifnull(h.strMessage,''),ifnull(h.strShape,''),ifnull(h.strNote,''),ifnull(a.dblTipAmount,0.00) "
//                    + ",a.strOperationType,ifnull(a.strTakeAwayRemarks,''),ifnull(e.longMobileNo,'')  "
//                    + "from " + billhd + " a "
//                    + "left outer join tblposmaster b on a.strPOSCode=b.strPosCode  "
//                    + "left outer join tbltablemaster c on a.strTableNo=c.strTableNo and a.strClientCode=c.strClientCode "
//                    + "left outer join tblwaitermaster d on a.strWaiterNo=d.strWaiterNo and a.strClientCode=d.strClientCode "
//                    + "left outer join tblcustomermaster e on a.strCustomerCode=e.strCustomerCode and a.strClientCode=e.strClientCode "
//                    + "left outer join tbldebitcardmaster f on a.strCardNo=f.strCardNo "
//                    + "left outer join tblreasonmaster g on a.strReasonCode=g.strReasonCode "
//                    + "left outer join " + advBookBillHd + " h on a.strAdvBookingNo=h.strAdvBookingNo and a.strClientCode=h.strClientCode "
//                    + "left outer join " + advReceiptHd + " i on h.strAdvBookingNo=i.strAdvBookingNo and a.strClientCode=i.strClientCode "
//                    + "left outer join tblvoidbillhd j on a.strBillNo=j.strBillNo and a.strPOSCode=j.strPosCode and a.strClientCode=j.strClientCode "
//                    + "left outer join " + billSettlementdtl + " k on a.strBillNo=k.strBillNo and a.strClientCode=k.strClientCode "
//                    + "left outer join tblsettelmenthd l on k.strSettlementCode=l.strSettelmentCode "
//                    + "where a.strBillNo=? and a.strPOSCode=? "
//                    + "group by a.strBillNo; ");
//            pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlBillHeaderDtl.toString());
//            pst.setString(1, billNo);
//            pst.setString(2, POSCode);
//            rs_BillHD = pst.executeQuery();
//            rs_BillHD.next();
//            if (rs_BillHD.getString(21).equals("Complementary"))
//            {
//                flgComplimentaryBill = true;
//            }
//            FileWriter fstream_bill = new FileWriter(Text_Bill);
//            BufferedWriter BillOut = new BufferedWriter(fstream_bill);
//            BillOut.newLine();
//            lineCount++;
//            boolean isReprint = false;
//            if ("reprint".equalsIgnoreCase(reprint))
//            {
//                isReprint = true;
//                funPrintBlankSpace("[DUPLICATE]", BillOut);
//                BillOut.write("[DUPLICATE]");
//                BillOut.newLine();
//                lineCount++;
//            }
//            if (transType.equals("Void"))
//            {
//                funPrintBlankSpace("VOIDED BILL", BillOut);
//                BillOut.write("VOIDED BILL");
//                BillOut.newLine();
//                lineCount++;
//            }
//            boolean flag_isHomeDelvBill = false;
//            String SQL_HomeDelivery = "select strBillNo,strCustomerCode,strDPCode,tmeTime,strCustAddressLine1 "
//                    + "from tblhomedelivery where strBillNo=? ;";
//            pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(SQL_HomeDelivery);
//            pst.setString(1, billNo);
//            ResultSet rs_HomeDelivery = pst.executeQuery();
//            if (rs_HomeDelivery.next())
//            {
//                flag_isHomeDelvBill = true;
//                customerCode = rs_HomeDelivery.getString(2);
//
//                if (clsGlobalVarClass.gPrintHomeDeliveryYN)
//                {
//                    funPrintBlankSpace("HOME DELIVERY", BillOut);
//                    BillOut.write("HOME DELIVERY");
//                    BillOut.newLine();
//                    lineCount++;
//                }
//
//                String SQL_CustomerDtl = "";
//
//                if (null != rs_HomeDelivery.getString(3) && rs_HomeDelivery.getString(3).trim().length() > 0)
//                {
//                    String[] delBoys = rs_HomeDelivery.getString(3).split(",");
//                    StringBuilder strIN = new StringBuilder("(");
//                    for (int i = 0; i < delBoys.length; i++)
//                    {
//                        if (i == 0)
//                        {
//                            strIN.append("'" + delBoys[i] + "'");
//                        }
//                        else
//                        {
//                            strIN.append(",'" + delBoys[i] + "'");
//                        }
//                    }
//                    strIN.append(")");
//                    String SQL_DeliveryBoyDtl = "select strDPName from tbldeliverypersonmaster where strDPCode IN " + strIN + " ;";
//                    pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(SQL_DeliveryBoyDtl);
//                    ResultSet rs_DeliveryBoyDtl = pst.executeQuery();
//                    strIN.setLength(0);
//                    for (int i = 0; rs_DeliveryBoyDtl.next(); i++)
//                    {
//                        if (i == 0)
//                        {
//                            strIN.append(rs_DeliveryBoyDtl.getString(1).toUpperCase());
//                        }
//                        else
//                        {
//                            strIN.append("," + rs_DeliveryBoyDtl.getString(1).toUpperCase());
//                        }
//                    }
//                    BillOut.write("  DELV BOY  :" + strIN);
//                    BillOut.newLine();
//                    lineCount++;
//                    rs_DeliveryBoyDtl.close();
//                }
//                //BillOut.write(Line);
//                // BillOut.newLine();
//            }
//
//            rs_HomeDelivery.close();
//            //print take away
//            int billPrintSize = 4;
//            if (rs_BillHD.getString(30).equals("TakeAway"))
//            {
//                funPrintBlankSpace("Take Away", BillOut);
//                BillOut.write("Take Away");
//                BillOut.newLine();
//                lineCount++;
//            }
//            if (clsGlobalVarClass.gPrintTaxInvoice.equalsIgnoreCase("Y"))
//            {
//                funPrintBlankSpace("TAX INVOICE", BillOut);
//                BillOut.write("TAX INVOICE");
//                BillOut.newLine();
//                lineCount++;
//            }
//// Bill No   Date  Table No     waiter
//            //      BillOut.write("     QTY ITEM NAME                  AMT");
//
//            //  5 blank line and then .. Bill No line
//            int blankLine = 0;
//            if (lineCount < 7)
//            {
//                for (int i = 0; i < 7 - lineCount; i++)
//                {
//                    BillOut.newLine();
//                    blankLine++;
//                }
//            }
//            lineCount += blankLine;
//            BillOut.write(" " + billNo + "   ");
//            SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy");
//            BillOut.write(ft.format(rs_BillHD.getObject(3)));
//            tblName = rs_BillHD.getString(18);
//            BillOut.write("   " + tblName + "   ");
//            waiterName = rs_BillHD.getString(19);
//            BillOut.write(waiterName);
//            BillOut.newLine();
//            lineCount++;
//
//            subTotal = rs_BillHD.getString(6);
//            grandTotal = rs_BillHD.getString(8);
//            user = rs_BillHD.getString(12);
//            deliveryCharge = rs_BillHD.getString(13);
//            advAmount = rs_BillHD.getString(14);
//            //print card available balance
//
//            //print card available balance
//            if (transType.equals("Void"))
//            {
//                BillOut.write("  Reason      :" + " " + rs_BillHD.getString(22));//voided reason
//                BillOut.newLine();
//                lineCount++;
//            }
//            else if (flgComplimentaryBill)
//            {
//
//                BillOut.write("  Reason      :" + " " + rs_BillHD.getString(23));
//                BillOut.newLine();
//                lineCount++;
//                BillOut.write("  Remark      :" + " " + rs_BillHD.getString(11));
//                BillOut.newLine();
//                lineCount++;
//            }
//            if (clsGlobalVarClass.gCMSIntegrationYN)
//            {
//            }
//            if (rs_BillHD.getString(25) != null && rs_BillHD.getString(25).length() > 0)
//            {
//                if (rs_BillHD.getString(26).length() > 0 || rs_BillHD.getString(27).length() > 0 || rs_BillHD.getString(28).length() > 0)
//                {
////                    BillOut.newLine();
////                    funPrintBlankSpace("ORDER DETAIL", BillOut);
////                    BillOut.write("ORDER DETAIL");
////                    BillOut.newLine();
////                    BillOut.write(Linefor5);
////                    BillOut.newLine();
//                }
//                StringBuilder strValue = new StringBuilder();
//                strValue.setLength(0);
//                if (rs_BillHD.getString(26).length() > 0)
//                {
//                    strValue.append(rs_BillHD.getString(26));
//                }
//                else
//                {
//                    strValue.append("");
//                }
//                int strlenMsg = strValue.length();
//                strValue.setLength(0);
//                int strlenNote = strValue.length();
//                if (strlenNote > 0)
//                {
//                    String note1 = "";
//                    if (strlenNote < 27)
//                    {
//                        note1 = strValue.substring(0, strlenNote);
//                        BillOut.write("  NOTE        :" + note1);
//                        BillOut.newLine();
//                        lineCount++;
//                    }
//                    else
//                    {
//                        note1 = strValue.substring(0, 27);
////                        BillOut.write("  NOTE        :" + note1);
////                        BillOut.newLine();
//                    }
//                    for (int i = 27; i <= strlenNote; i++)
//                    {
//                        int endNote = 0;
//                        endNote = i + 27;
//                        if (strlenNote > endNote)
//                        {
//                            note1 = strValue.substring(i, endNote);
//                            i = endNote;
////                            BillOut.write("               " + note1);
////                            BillOut.newLine();
//                        }
//                        else
//                        {
//                            note1 = strValue.substring(i, strlenNote);
////                            BillOut.write("               " + note1);
////                            BillOut.newLine();
//                            i = strlenNote + 1;
//                        }
//                    }
//                }
//
//            }
//// need 3  blank line to print quantity 
//            BillOut.write(Linefor5);
//            blankLine = 0;
//            if (lineCount < 11)
//            {
//                for (int i = 0; i < 11 - lineCount; i++)
//                {
//                    BillOut.newLine();
//                    blankLine++;
//                }
//            }
//            lineCount += blankLine;
////here  total line count is 11
//
//            // BillOut.write("     QTY ITEM NAME                  AMT");
//            BillOut.newLine();
//            lineCount++;
//            String SQL_BillDtl = "select sum(a.dblQuantity),left(a.strItemName,22) as ItemLine1"
//                    + " ,MID(a.strItemName,23,LENGTH(a.strItemName)) as ItemLine2"
//                    + " ,sum(a.dblAmount),a.strItemCode,a.strKOTNo "
//                    + " from " + billdtl + " a," + billhd + " b "
//                    + " where a.strBillNo=b.strBillNo and a.strClientCode=b.strClientCode and a.strBillNo=? and b.strPOSCode=?  ";
//            if (!clsGlobalVarClass.gPrintTDHItemsInBill)
//            {
//                SQL_BillDtl += "and a.tdhYN='N' ";
//            }
//            if (!clsGlobalVarClass.gPrintOpenItemsOnBill)
//            {
//                SQL_BillDtl += "and a.dblAmount>0 ";
//            }
//
//            SQL_BillDtl += " group by a.strItemCode ";
//            pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(SQL_BillDtl);
//            pst.setString(1, billNo);
//            pst.setString(2, POSCode);
//            ResultSet rs_BillDtl = pst.executeQuery();
//            while (rs_BillDtl.next())
//            {
//                double saleQty = Double.parseDouble(rs_BillDtl.getString(1));
//                String sqlPromoBills = "select dblQuantity from " + billPromoDtl + " "
//                        + " where strBillNo='" + billNo + "' and strItemCode='" + rs_BillDtl.getString(5) + "' "
//                        + " and strPromoType='ItemWise' ";
//                ResultSet rsPromoItems = clsGlobalVarClass.dbMysql.executeResultSet(sqlPromoBills);
//                if (rsPromoItems.next())
//                {
//                    saleQty -= rsPromoItems.getDouble(1);
//                }
//                rsPromoItems.close();
//                String qty = String.valueOf(decimalFormatFor3DecPoint.format(saleQty));
////                if (qty.contains("."))
////                {
////                    String decVal = qty.substring(qty.length() - 2, qty.length());
////                    if (Double.parseDouble(decVal) == 0)
////                    {
////                        qty = qty.substring(0, qty.length() - 2);
////                    }
////                }
//                if (saleQty > 0)
//                {
//                    objuUtility2.funPrintContentWithSpace("Right", qty, 6, BillOut);//Qty Print
//                    BillOut.write(" ");
//                    objuUtility2.funPrintContentWithSpace("Left", rs_BillDtl.getString(2), 20, BillOut);//Item Name
//                    if (flgComplimentaryBill)
//                    {
//                        objuUtility2.funPrintContentWithSpace("Right", "0.00", 7, BillOut);//Amount
//                    }
//                    else
//                    {
//                        objuUtility2.funPrintContentWithSpace("Right", rs_BillDtl.getString(4), 7, BillOut);//Amount
//                    }
//                    BillOut.newLine();
//                    lineCount++;
//                    if (rs_BillDtl.getString(3).trim().length() > 0)
//                    {
//                        BillOut.write("       " + rs_BillDtl.getString(3));
//                        BillOut.newLine();
//                        lineCount++;
//                    }
//                    String sqlModifier = "select count(*) "
//                            + "from " + billModifierdtl + " where strBillNo=? and left(strItemCode,7)=? ";
//                    if (!clsGlobalVarClass.gPrintZeroAmtModifierOnBill)
//                    {
//                        sqlModifier += " and  dblAmount !=0.00 ";
//                    }
//                    pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlModifier);
//                    pst.setString(1, billNo);
//                    pst.setString(2, rs_BillDtl.getString(5));
//                    ResultSet rs_count = pst.executeQuery();
//                    rs_count.next();
//                    int cntRecord = rs_count.getInt(1);
//                    rs_count.close();
//                    if (cntRecord > 0)
//                    {
//                        sqlModifier = "select strModifierName,dblQuantity,dblAmount "
//                                + " from " + billModifierdtl + " "
//                                + " where strBillNo=? and left(strItemCode,7)=? ";
//                        if (!clsGlobalVarClass.gPrintZeroAmtModifierOnBill)
//                        {
//                            sqlModifier += " and  dblAmount !=0.00 ";
//                        }
//                        pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlModifier);
//                        pst.setString(1, billNo);
//                        pst.setString(2, rs_BillDtl.getString(5));
//                        ResultSet rs_modifierRecord = pst.executeQuery();
//                        while (rs_modifierRecord.next())
//                        {
//                            if (flgComplimentaryBill)
//                            {
//                                objuUtility2.funWriteToTextformat5(BillOut, "", rs_modifierRecord.getString(1).toUpperCase(), "0.00", "Format5");
//                                BillOut.newLine();
//                                lineCount++;
//                            }
//                            else
//                            {
//                                objuUtility2.funWriteToTextformat5(BillOut, "", rs_modifierRecord.getString(1).toUpperCase(), rs_modifierRecord.getString(3), "Format5");
//                                BillOut.newLine();
//                                lineCount++;
//                            }
//                        }
//                        rs_modifierRecord.close();
//                    }
//
//                    sql = "select b.strItemCode,b.dblWeight "
//                            + " from " + billhd + " a," + advBookBillDtl + " b "
//                            + " where a.strAdvBookingNo=b.strAdvBookingNo and a.strClientCode=b.strClientCode "
//                            + " and a.strBillNo='" + billNo + "' and b.strItemCode='" + rs_BillDtl.getString(5) + "' "
//                            + " and a.strPOSCode='" + POSCode + "' ";
//                    ResultSet rsWeight = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//                    while (rsWeight.next())
//                    {
//                        BillOut.write("   Weight");
//                        BillOut.write("   " + rsWeight.getDouble(2));
//                        BillOut.newLine();
//                        lineCount++;
//                    }
//                    rsWeight.close();
//                    sql = "select c.strCharName,b.strCharValues "
//                            + " from " + billhd + " a," + advBookBillCharDtl + " b,tblcharactersticsmaster c "
//                            + " where a.strAdvBookingNo=b.strAdvBookingNo and b.strCharCode=c.strCharCode "
//                            + " and a.strBillNo='" + billNo + "' and b.strItemCode='" + rs_BillDtl.getString(5) + "' "
//                            + " and a.strPOSCode='" + POSCode + "' and a.strClientCode=b.strClientCode ";
//                    ResultSet rsCharDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//                    while (rsCharDtl.next())
//                    {
//                        String charName = objUtility.funPrintTextWithAlignment(rsCharDtl.getString(1), 10, "Left");
//                        BillOut.write("   " + charName);
//                        String charVal = objUtility.funPrintTextWithAlignment(rsCharDtl.getString(2), 26, "Left");
//                        BillOut.write("   " + charVal);
//                        BillOut.newLine();
//                        lineCount++;
//                    }
//                    rsCharDtl.close();
//                }
//
//                //      here check no of items in bill.. if items greater than 7 ..then print next items on second page
//                if (lineCount > 28)
//                {
//                    for (int i = 0; i < 19; i++)
//                    {
//                        BillOut.newLine();
//                    }
//                    lineCount = 11;
//                }
//            }
//            rs_BillDtl.close();
//            funPrintPromoItemsInBill(billNo, BillOut, 4);  // Print Promotion Items in Bill for this billno.
//            BillOut.write(Linefor5);
//            BillOut.newLine();
//            lineCount++;
//            // currentLineCount-- current line number, totalLineCount -- total number of line in single page , topSpaceLineCount --no of line from actual item printing start,
//            //totalLinesForNextPage -- required blank line to contiouse print on next page ,BufferedWriter out
//            lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//            if (clsGlobalVarClass.gPointsOnBillPrint)
//            {
//                String sqlCRMPoints = "select b.dblPoints from " + billhd + " a, tblcrmpoints b "
//                        + " where a.strBillNo=b.strBillNo and a.strClientCode=b.strClientCode "
//                        + " and a.strBillNo='" + billNo + "' and a.strPOSCode='" + POSCode + "' ";
//                ResultSet rsCRMPoints = clsGlobalVarClass.dbMysql.executeResultSet(sqlCRMPoints);
//                if (rsCRMPoints.next())
//                {
//                    funWriteTotalStationery("POINTS ", rsCRMPoints.getString(1), BillOut, "Format5", "");
//                }
//                rsCRMPoints.close();
//                BillOut.newLine();
//                lineCount++;
//                lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//            }
////1
//            if (flgComplimentaryBill)
//            {
//                funWriteTotalStationery("SUB TOTAL", "0.00", BillOut, "Format5", "");
//                BillOut.newLine();
//                lineCount++;
//                lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//            }
//            else
//            {
//                funWriteTotalStationery("SUB TOTAL", subTotal, BillOut, "Format5", "");
//                BillOut.newLine();
//                lineCount++;
//                lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//            }
//            sql = "select a.dblDiscPer,a.dblDiscAmt,a.strDiscOnType,a.strDiscOnValue,b.strReasonName,a.strDiscRemarks "
//                    + " from " + billDscFrom + " a ,tblreasonmaster b," + billhd + " c "
//                    + " where  a.strDiscReasonCode=b.strReasonCode and a.strBillNo=c.strBillNo "
//                    + " and a.strClientCode=c.strClientCode and a.strBillNo='" + billNo + "' and c.strPOSCode='" + POSCode + "' ";
//            ResultSet rsDisc = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//            boolean flag = true;
//            while (rsDisc.next())
//            {
//                if (flag)
//                {
//                    flag = false;
//                    BillOut.write("  DISCOUNT");
//                    BillOut.newLine();
//                    lineCount++;
//                    lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                }
//                double dbl = Double.parseDouble(rsDisc.getString("dblDiscPer"));
//                String discText = String.format("%.1f", dbl) + "%" + " On " + rsDisc.getString("strDiscOnValue") + "";
//                if (discText.length() > 30)
//                {
//                    discText = discText.substring(0, 30);
//                }
//                else
//                {
//                    discText = String.format("%-30s", discText);
//                }
//                BillOut.write("  " + discText);
//                String discountOnItem = objUtility.funPrintTextWithAlignment(rsDisc.getString("dblDiscAmt"), 8, "Right");
//                BillOut.write(discountOnItem);
//                BillOut.newLine();
//                lineCount++;
//                lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                BillOut.write("  Reason  : ");
//                String discReason = objUtility.funPrintTextWithAlignment(rsDisc.getString(5), 20, "Left");
//                BillOut.write(discReason);
//                BillOut.newLine();
//                lineCount++;
//                lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                BillOut.write("  Remarks : ");
//                String discRemarks = objUtility.funPrintTextWithAlignment(rsDisc.getString(6), 20, "Left");
//                BillOut.write(discRemarks);
//                BillOut.newLine();
//                lineCount++;
//                lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
////5
//            }
//            String sql_Tax = "select b.strTaxDesc,sum(a.dblTaxAmount) "
//                    + " from " + billtaxdtl + " a,tbltaxhd b," + billhd + " c "
//                    + " where a.strBillNo='" + billNo + "' "
//                    + " and a.strTaxCode=b.strTaxCode "
//                    + " and a.strBillNo=c.strBillNo "
//                    + " and a.strClientCode=c.strClientCode "
//                    + " and c.strPOSCode='" + POSCode + "' "
//                    + " and b.strTaxCalculation='Forward' "
//                    + " group by a.strTaxCode";
//            ResultSet rsTax = clsGlobalVarClass.dbMysql.executeResultSet(sql_Tax);
//            while (rsTax.next())
//            {
//                if (flgComplimentaryBill)
//                {
//                    funWriteTotal(rsTax.getString(1), "0.00", BillOut, "Format5");
//                }
//                else
//                {
//                    funWriteTotalStationery(rsTax.getString(1), rsTax.getString(2), BillOut, "Format5", "");
//                }
//                BillOut.newLine();
//                lineCount++;
//                lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//            }
//            if (deliveryCharge != null && deliveryCharge.trim().length() > 0 && !"0.00".equalsIgnoreCase(deliveryCharge))
//            {
//                funWriteTotalStationery("DELV. CHARGE", deliveryCharge, BillOut, "Format5", "");
//                BillOut.newLine();
//                lineCount++;
//                lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//            }
//            if (advAmount.trim().length() > 0 && !"0.00".equalsIgnoreCase(advAmount))
//            {
//                funWriteTotalStationery("ADVANCE", advAmount, BillOut, "Format5", "");
//                BillOut.newLine();
//                lineCount++;
//                lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//            }
//            BillOut.write(Linefor5);
//            BillOut.newLine();
//            lineCount++;
//            lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//            if (flgComplimentaryBill)
//            {
//                funWriteTotalStationery("TOTAL(ROUNDED)", "0.00", BillOut, "Format5", "");
//            }
//            else
//            {
//                funWriteTotalStationery("TOTAL(ROUNDED)", grandTotal, BillOut, "Format5", "");
//            }
//            BillOut.newLine();
//            lineCount++;
//            lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//            BillOut.write(Linefor5);
////10
//            //print Grand total of other bill nos from bill series
//            if (clsGlobalVarClass.gEnableBillSeries)
//            {
//                String sqlPrintGT = "select a.strPrintGTOfOtherBills,b.strDtlBillNos,b.dblGrandTotal "
//                        + "from tblbillseries a,tblbillseriesbilldtl b "
//                        + "where (a.strPOSCode=b.strPOSCode or a.strPOSCode='All') "
//                        + "and a.strBillSeries=b.strBillSeries "
//                        + "and b.strHdBillNo='" + billNo + "' and b.strPOSCode='" + POSCode + "' ";
//                ResultSet rsPrintGTYN = clsGlobalVarClass.dbMysql.executeResultSet(sqlPrintGT);
//                double dblOtherBillsGT = 0.00;
//                if (rsPrintGTYN.next())
//                {
//                    if (rsPrintGTYN.getString(1).equalsIgnoreCase("Y"))
//                    {
//                        String billSeriesDtlBillNos = rsPrintGTYN.getString(2);
//                        String[] dtlBillSeriesBillNo = billSeriesDtlBillNos.split(",");
//                        dblOtherBillsGT += rsPrintGTYN.getDouble(3);
//                        if (dtlBillSeriesBillNo.length > 0)
//                        {
//                            for (int i = 0; i < dtlBillSeriesBillNo.length; i++)
//                            {
//                                sqlPrintGT = "select a.strHdBillNo,a.dblGrandTotal "
//                                        + "from tblbillseriesbilldtl a "
//                                        + "where a.strHdBillNo='" + dtlBillSeriesBillNo[i] + "' and a.strPOSCode='" + POSCode + "' ";
//                                ResultSet rsPrintGT = clsGlobalVarClass.dbMysql.executeResultSet(sqlPrintGT);
//                                if (rsPrintGT.next())
//                                {
//                                    BillOut.newLine();
//                                    lineCount++;
//                                    lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                                    funWriteTotalStationery(dtlBillSeriesBillNo[i] + " TOTAL(ROUNDED)", rsPrintGT.getString(2), BillOut, "Format5", "");
//                                    dblOtherBillsGT += rsPrintGT.getDouble(2);
//                                    BillOut.newLine();
//                                    lineCount++;
//                                    lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                                }
//                            }
//                            BillOut.write(Linefor5);
//                            BillOut.newLine();
//                            lineCount++;
//                            lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                            funWriteTotalStationery("GRAND TOTAL(ROUNDED)", String.valueOf(dblOtherBillsGT), BillOut, "Format5", "");
//                            BillOut.newLine();
//                            lineCount++;
//                            lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                            BillOut.write(Linefor5);
//                            BillOut.newLine();
//                            lineCount++;
//                            lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                        }
//                    }
//                }
//            }
////15
//            //settlement breakup part
//            String sqlSettlementBreakup = "select a.dblSettlementAmt, b.strSettelmentDesc, b.strSettelmentType "
//                    + " from " + billSettlementdtl + " a ,tblsettelmenthd b," + billhd + " c "
//                    + " where a.strBillNo=? and a.strBillNo=c.strBillNo and a.strClientCode=c.strClientCode "
//                    + " and a.strSettlementCode=b.strSettelmentCode and c.strPOSCode=? ";
//            pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlSettlementBreakup);
//            pst.setString(1, billNo);
//            pst.setString(2, POSCode);
//            boolean flgSettlement = false;
//            boolean creditSettlement = false;
//            ResultSet rsBillSettlement = pst.executeQuery();
//            while (rsBillSettlement.next())
//            {
//                if (flgComplimentaryBill)
//                {
//                    BillOut.newLine();
//                    funWriteTotalStationery(rsBillSettlement.getString(2), "0.00", BillOut, "Format5", "");
//                }
//                else
//                {
//                    BillOut.newLine();
//                    funWriteTotalStationery(rsBillSettlement.getString(2), rsBillSettlement.getString(1), BillOut, "Format5", "");
//                }
//                lineCount++;
//                lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                flgSettlement = true;
//                if (rsBillSettlement.getString(3).equals("Credit"))
//                {
//                    creditSettlement = true;
//                }
//            }
//            rsBillSettlement.close();
//
//            if (flgSettlement)
//            {
//                if (creditSettlement)
//                {
//                    BillOut.newLine();
//                    lineCount++;
//                    lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                    funWriteTotalStationery("Credit Remarks ", rs_BillHD.getString(11), BillOut, "Format5", "");
//                    BillOut.newLine();
//                    lineCount++;
//                    lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                    String custName = rs_BillHD.getString(24);
//                    if (!custName.isEmpty())
//                    {
//                        funWriteTotalStationery("Customer " + custName, "", BillOut, "Format5", "");
//                    }
//                    BillOut.newLine();
//                    lineCount++;
//                    lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                    BillOut.write(Linefor5);
//                }
//            }
////19
//            String sqlTenderAmt = "select sum(a.dblPaidAmt),sum(a.dblSettlementAmt),(sum(a.dblPaidAmt)-sum(a.dblSettlementAmt)) RefundAmt "
//                    + " from " + billSettlementdtl + " a," + billhd + " b "
//                    + " where a.strBillNo=b.strBillNo and a.strClientCode=b.strClientCode "
//                    + " and b.strBillNo='" + billNo + "' and b.strPOSCode='" + POSCode + "' "
//                    + " group by a.strBillNo";
//            ResultSet rsTenderAmt = clsGlobalVarClass.dbMysql.executeResultSet(sqlTenderAmt);
//            if (rsTenderAmt.next())
//            {
//                BillOut.newLine();
//                lineCount++;
//                lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                if (flgComplimentaryBill)
//                {
//                    funWriteTotalStationery("PAID AMT", "0.00", BillOut, "Format5", "");
//                    //BillOut.newLine();
//                }
//                else
//                {
//                    funWriteTotalStationery("PAID AMT", rsTenderAmt.getString(1), BillOut, "Format5", "");
//                    BillOut.newLine();
//                    lineCount++;
//                    lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                    if (rsTenderAmt.getDouble(3) > 0)
//                    {
//                        funWriteTotalStationery("REFUND AMT", rsTenderAmt.getString(3), BillOut, "Format5", "");
//                        //BillOut.newLine();
//                    }
//                }
//                BillOut.newLine();
//                lineCount++;
//                lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                BillOut.write(Linefor5);
//            }
//            rsTenderAmt.close();
//
//            if (rs_BillHD.getDouble(29) > 0)
//            {
//                BillOut.newLine();
//                lineCount++;
//                lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                funWriteTotalStationery("TIP AMT", rs_BillHD.getString(29), BillOut, "Format5", "");
//                //BillOut.newLine();
//            }
//            if (flag_isHomeDelvBill)
//            {
//                BillOut.newLine();
//                lineCount++;
//                lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                String sql_count = "select count(*) from tblhomedelivery where strCustomerCode=?";
//                pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sql_count);
//                pst.setString(1, customerCode);
//                ResultSet rs_Count = pst.executeQuery();
//                rs_Count.next();
//                BillOut.write("CUSTOMER COUNT : " + rs_Count.getString(1));
//                rs_Count.close();
//                BillOut.newLine();
//                lineCount++;
//                lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                BillOut.write(Linefor5);
//            }
//            BillOut.newLine();
//            lineCount++;
//            lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//
//            funPrintServiceVatNo(BillOut, 4, billNo, billDate, billtaxdtl);
//
//            if (clsGlobalVarClass.gEnableBillSeries)
//            {
//                sql = "select b.strPrintInclusiveOfTaxOnBill "
//                        + " from tblbillseriesbilldtl a,tblbillseries b "
//                        + " where a.strBillSeries=b.strBillSeries and a.strHdBillNo='" + billNo + "' and a.strClientCode=b.strClientCode";
//                ResultSet rsBillSeries = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//                if (rsBillSeries.next())
//                {
//                    if (rsBillSeries.getString(1).equals("Y"))
//                    {
//                        BillOut.write(Linefor5);
//                        BillOut.newLine();
//                        lineCount++;
//                        lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                        funPrintBlankSpace("(INCLUSIVE OF ALL TAXES)", BillOut);
//                        BillOut.write("(INCLUSIVE OF ALL TAXES)");
//                        BillOut.newLine();
//                        lineCount++;
//                        lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                    }
//                }
//                rsBillSeries.close();
//            }
//            else
//            {
//                if (clsGlobalVarClass.gPrintInclusiveOfAllTaxes.equalsIgnoreCase("Y"))
//                {
//                    BillOut.write(Linefor5);
//                    BillOut.newLine();
//                    lineCount++;
//                    lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                    funPrintBlankSpace("(INCLUSIVE OF ALL TAXES)", BillOut);
//                    BillOut.write("(INCLUSIVE OF ALL TAXES)");
//                    BillOut.newLine();
//                    lineCount++;
//                    lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                }
//            }
//
//            int num = clsGlobalVarClass.gBillFooter.trim().length() / 30;
//            int num1 = clsGlobalVarClass.gBillFooter.trim().length() % 30;
//            int cnt1 = 0;
//            for (int cnt = 0; cnt < num; cnt++)
//            {
//                String footer = clsGlobalVarClass.gBillFooter.trim().substring(cnt1, (cnt1 + 30));
//                footer = footer.replaceAll("\n", "");
//                BillOut.write("   " + footer.trim());
//                BillOut.newLine();
//                lineCount++;
//                lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                cnt1 += 30;
//            }
//            BillOut.write("   " + clsGlobalVarClass.gBillFooter.trim().substring(cnt1, (cnt1 + num1)).trim());
//            BillOut.newLine();
//            lineCount++;
//            lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//            funPrintBlankSpace(user, BillOut);
//            BillOut.write(user);
//            if (lineCount < 29)
//            {
//                for (int i = 0; i < 29 - lineCount; i++)
//                {
//                    BillOut.newLine();
//                }
//            }
//            if (!clsGlobalVarClass.gOpenCashDrawerAfterBillPrintYN)
//            {
//                if ("linux".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
//                {
//                    BillOut.write("V");//Linux
//                }
//                else if ("windows".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
//                {
//                    if ("Inbuild".equalsIgnoreCase(clsPosConfigFile.gPrinterType))
//                    {
//                        BillOut.write("V");
//                    }
//                    else
//                    {
//                        BillOut.write("m");//windows
//                    }
//                }
//            }
//            rs_BillHD.close();
//            BillOut.close();
//            fstream_bill.close();
//            pst.close();
//
//            if (formName.equalsIgnoreCase("sales report"))
//            {
//                funShowTextFile(Text_Bill, formName, clsGlobalVarClass.gBillPrintPrinterPort);
//            }
//            else
//            {
//                if (clsGlobalVarClass.gShowBill)
//                {
//                    funShowTextFile(Text_Bill, formName, clsGlobalVarClass.gBillPrintPrinterPort);
//                }
//            }
//
//            if (!formName.equalsIgnoreCase("sales report"))
//            {
//                if (transType.equalsIgnoreCase("void"))
//                {
//                    if (clsGlobalVarClass.gPrintOnVoidBill)
//                    {
//                        if (!viewORprint.equalsIgnoreCase("view"))
//                        {
//                            funPrintToPrinter(clsGlobalVarClass.gBillPrintPrinterPort, "", "bill", "N", isReprint);
//                        }
//                    }
//                }
//                else
//                {
//                    if (!clsGlobalVarClass.flgReprintView)
//                    {
//                        if (!viewORprint.equalsIgnoreCase("view"))
//                        {
//                            funPrintToPrinter(clsGlobalVarClass.gBillPrintPrinterPort, "", "bill", "N", isReprint);
//                        }
//                    }
//                    else
//                    {
//                        clsGlobalVarClass.flgReprintView = false;
//                    }
//                }
//            }
//            //if (formName.equalsIgnoreCase("sales report"))
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    public void funGenerateTextFileBillPrintingForStationery2(String billNo, String reprint, String formName, String transType, String billDate, String POSCode, String viewORprint)
//    {
//        int lineCount = 0;
//        clsUtility objUtility = new clsUtility();
//        clsUtility2 objuUtility2 = new clsUtility2();
//        String Linefor5 = " --------------------------------------";
//        try
//        {
//            String user = "";
//            String billhd = null;
//            String billdtl = null;
//            String billModifierdtl = null;
//            String billSettlementdtl = null;
//            String billtaxdtl = null;
//            String billDscFrom = null;
//            String billPromoDtl = null;
//            String advBookBillHd = null;
//            String advBookBillDtl = null;
//            String advBookBillCharDtl = null;
//            String advReceiptHd = null;
//            if (clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("HOPOS"))
//            {
//                billhd = "tblqbillhd";
//                billdtl = "tblqbilldtl";
//                billModifierdtl = "tblqbillmodifierdtl";
//                billSettlementdtl = "tblqbillsettlementdtl";
//                billtaxdtl = "tblqbilltaxdtl";
//                billDscFrom = "tblqbilldiscdtl";
//                billPromoDtl = "tblqbillpromotiondtl";
//
//                advBookBillHd = "tblqadvbookbillhd";
//                advBookBillDtl = "tblqadvbookbilldtl";
//                advBookBillCharDtl = "tblqadvbookbillchardtl";
//                advReceiptHd = "tblqadvancereceipthd";
//            }
//            else
//            {
//                if ("sales report".equalsIgnoreCase(formName))
//                {
//                    billhd = "tblbillhd";
//                    billdtl = "tblbilldtl";
//                    billModifierdtl = "tblbillmodifierdtl";
//                    billSettlementdtl = "tblbillsettlementdtl";
//                    billtaxdtl = "tblbilltaxdtl";
//                    billDscFrom = "tblbilldiscdtl";
//                    billPromoDtl = "tblbillpromotiondtl";
//                    advBookBillHd = "tbladvbookbillhd";
//                    advBookBillDtl = "tbladvbookbilldtl";
//                    advBookBillCharDtl = "tbladvbookbillchardtl";
//                    advReceiptHd = "tbladvancereceipthd";
//                    long dateDiff = new clsUtility().funCompareDate(billDate, objUtility.funGetPOSDateForTransaction());
//                    if (dateDiff > 0)
//                    {
//                        billhd = "tblqbillhd";
//                        billdtl = "tblqbilldtl";
//                        billModifierdtl = "tblqbillmodifierdtl";
//                        billSettlementdtl = "tblqbillsettlementdtl";
//                        billtaxdtl = "tblqbilltaxdtl";
//                        billDscFrom = "tblqbilldiscdtl";
//                        billPromoDtl = "tblqbillpromotiondtl";
//                        advBookBillHd = "tblqadvbookbillhd";
//                        advBookBillDtl = "tblqadvbookbilldtl";
//                        advBookBillCharDtl = "tblqadvbookbillchardtl";
//                        advReceiptHd = "tblqadvancereceipthd";
//                    }
//                    String sql = "select count(strBillNo) from tblbillhd where strBillNo='" + billNo + "' and strPOSCode='" + POSCode + "' ";
//                    ResultSet rsBillTable = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//                    rsBillTable.next();
//                    int billCnt = rsBillTable.getInt(1);
//                    if (billCnt == 0)
//                    {
//                        billhd = "tblqbillhd";
//                        billdtl = "tblqbilldtl";
//                        billModifierdtl = "tblqbillmodifierdtl";
//                        billSettlementdtl = "tblqbillsettlementdtl";
//                        billtaxdtl = "tblqbilltaxdtl";
//                        billDscFrom = "tblqbilldiscdtl";
//                        billPromoDtl = "tblqbillpromotiondtl";
//                        advBookBillHd = "tblqadvbookbillhd";
//                        advBookBillDtl = "tblqadvbookbilldtl";
//                        advBookBillCharDtl = "tblqadvbookbillchardtl";
//                        advReceiptHd = "tblqadvancereceipthd";
//                    }
//                }
//                else
//                {
//                    billhd = "tblbillhd";
//                    billdtl = "tblbilldtl";
//                    billModifierdtl = "tblbillmodifierdtl";
//                    billSettlementdtl = "tblbillsettlementdtl";
//                    billtaxdtl = "tblbilltaxdtl";
//                    billDscFrom = "tblbilldiscdtl";
//                    billPromoDtl = "tblbillpromotiondtl";
//                    advBookBillHd = "tbladvbookbillhd";
//                    advBookBillDtl = "tbladvbookbilldtl";
//                    advBookBillCharDtl = "tbladvbookbillchardtl";
//                    advReceiptHd = "tbladvancereceipthd";
//                }
//            }
//            PreparedStatement pst = null;
//            funCreateTempFolder();
//            String filePath = System.getProperty("user.dir");
//            File Text_Bill = new File(filePath + "/Temp/Temp_Bill.txt");
//            String subTotal = "";
//            String grandTotal = "";
//            String advAmount = "";
//            String deliveryCharge = "";
//            String customerCode = "";
//            String waiterName = "";
//            String tblName = "";
//            ResultSet rs_BillHD = null;
//            boolean flgComplimentaryBill = false;
//            StringBuilder sqlBillHeaderDtl = new StringBuilder();
//            sqlBillHeaderDtl.append("select ifnull(a.strTableNo,''),ifnull(a.strWaiterNo,''),a.dteBillDate,time(a.dteBillDate),a.dblDiscountAmt,a.dblSubTotal,"
//                    + "ifnull(a.strCustomerCode,''),a.dblGrandTotal,a.dblTaxAmt,ifnull(a.strReasonCode,''),ifnull(a.strRemarks,''),a.strUserCreated "
//                    + ",ifnull(dblDeliveryCharges,0.00),ifnull(i.dblAdvDeposite,0.00),a.dblDiscountPer,b.strPOSName,a.intPaxNo "
//                    + ",ifnull(c.strTableName,''),ifnull(d.strWShortName,''),ifnull(d.strWFullName,''),ifnull(l.strSettelmentType,''),ifnull(j.strReasonName,'') as voidedReason, "
//                    + "ifnull(g.strReasonName,''),ifnull(e.strCustomerName,''),ifnull(a.strAdvBookingNo,''),ifnull(h.strMessage,''),ifnull(h.strShape,''),ifnull(h.strNote,''),ifnull(a.dblTipAmount,0.00) "
//                    + ",a.strOperationType,ifnull(a.strTakeAwayRemarks,''),ifnull(e.longMobileNo,'')  "
//                    + "from " + billhd + " a "
//                    + "left outer join tblposmaster b on a.strPOSCode=b.strPosCode  "
//                    + "left outer join tbltablemaster c on a.strTableNo=c.strTableNo and a.strClientCode=c.strClientCode "
//                    + "left outer join tblwaitermaster d on a.strWaiterNo=d.strWaiterNo and a.strClientCode=d.strClientCode "
//                    + "left outer join tblcustomermaster e on a.strCustomerCode=e.strCustomerCode and a.strClientCode=e.strClientCode "
//                    + "left outer join tbldebitcardmaster f on a.strCardNo=f.strCardNo "
//                    + "left outer join tblreasonmaster g on a.strReasonCode=g.strReasonCode "
//                    + "left outer join " + advBookBillHd + " h on a.strAdvBookingNo=h.strAdvBookingNo and a.strClientCode=h.strClientCode "
//                    + "left outer join " + advReceiptHd + " i on h.strAdvBookingNo=i.strAdvBookingNo and a.strClientCode=i.strClientCode "
//                    + "left outer join tblvoidbillhd j on a.strBillNo=j.strBillNo and a.strPOSCode=j.strPosCode and a.strClientCode=j.strClientCode "
//                    + "left outer join " + billSettlementdtl + " k on a.strBillNo=k.strBillNo and a.strClientCode=k.strClientCode "
//                    + "left outer join tblsettelmenthd l on k.strSettlementCode=l.strSettelmentCode "
//                    + "where a.strBillNo=? and a.strPOSCode=? "
//                    + "group by a.strBillNo; ");
//            pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlBillHeaderDtl.toString());
//            pst.setString(1, billNo);
//            pst.setString(2, POSCode);
//            rs_BillHD = pst.executeQuery();
//            rs_BillHD.next();
//            if (rs_BillHD.getString(21).equals("Complementary"))
//            {
//                flgComplimentaryBill = true;
//            }
//            FileWriter fstream_bill = new FileWriter(Text_Bill);
//            BufferedWriter BillOut = new BufferedWriter(fstream_bill);
//            BillOut.newLine();
//            lineCount++;
//            boolean isReprint = false;
//            if ("reprint".equalsIgnoreCase(reprint))
//            {
//                isReprint = true;
//                funPrintBlankSpace("[DUPLICATE]", BillOut);
//                BillOut.write("[DUPLICATE]");
//                BillOut.newLine();
//                lineCount++;
//            }
//            if (transType.equals("Void"))
//            {
//                funPrintBlankSpace("VOIDED BILL", BillOut);
//                BillOut.write("VOIDED BILL");
//                BillOut.newLine();
//                lineCount++;
//            }
//            boolean flag_isHomeDelvBill = false;
//            String SQL_HomeDelivery = "select strBillNo,strCustomerCode,strDPCode,tmeTime,strCustAddressLine1 "
//                    + "from tblhomedelivery where strBillNo=? ;";
//            pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(SQL_HomeDelivery);
//            pst.setString(1, billNo);
//            ResultSet rs_HomeDelivery = pst.executeQuery();
//            if (rs_HomeDelivery.next())
//            {
//                flag_isHomeDelvBill = true;
//                customerCode = rs_HomeDelivery.getString(2);
//
//                if (clsGlobalVarClass.gPrintHomeDeliveryYN)
//                {
//                    funPrintBlankSpace("HOME DELIVERY", BillOut);
//                    BillOut.write("HOME DELIVERY");
//                    BillOut.newLine();
//                    lineCount++;
//                }
//
//                String SQL_CustomerDtl = "";
//
//                if (null != rs_HomeDelivery.getString(3) && rs_HomeDelivery.getString(3).trim().length() > 0)
//                {
//                    String[] delBoys = rs_HomeDelivery.getString(3).split(",");
//                    StringBuilder strIN = new StringBuilder("(");
//                    for (int i = 0; i < delBoys.length; i++)
//                    {
//                        if (i == 0)
//                        {
//                            strIN.append("'" + delBoys[i] + "'");
//                        }
//                        else
//                        {
//                            strIN.append(",'" + delBoys[i] + "'");
//                        }
//                    }
//                    strIN.append(")");
//                    String SQL_DeliveryBoyDtl = "select strDPName from tbldeliverypersonmaster where strDPCode IN " + strIN + " ;";
//                    pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(SQL_DeliveryBoyDtl);
//                    ResultSet rs_DeliveryBoyDtl = pst.executeQuery();
//                    strIN.setLength(0);
//                    for (int i = 0; rs_DeliveryBoyDtl.next(); i++)
//                    {
//                        if (i == 0)
//                        {
//                            strIN.append(rs_DeliveryBoyDtl.getString(1).toUpperCase());
//                        }
//                        else
//                        {
//                            strIN.append("," + rs_DeliveryBoyDtl.getString(1).toUpperCase());
//                        }
//                    }
//                    BillOut.write("  DELV BOY  :" + strIN);
//                    BillOut.newLine();
//                    lineCount++;
//                    rs_DeliveryBoyDtl.close();
//                }
//                //BillOut.write(Line);
//                // BillOut.newLine();
//            }
//
//            rs_HomeDelivery.close();
//            //print take away
//            int billPrintSize = 4;
//            if (rs_BillHD.getString(30).equals("TakeAway"))
//            {
//                funPrintBlankSpace("Take Away", BillOut);
//                BillOut.write("Take Away");
//                BillOut.newLine();
//                lineCount++;
//            }
//            if (clsGlobalVarClass.gPrintTaxInvoice.equalsIgnoreCase("Y"))
//            {
//                funPrintBlankSpace("TAX INVOICE", BillOut);
//                BillOut.write("TAX INVOICE");
//                BillOut.newLine();
//                lineCount++;
//            }
//// Bill No   Date  Table No     waiter
//            //      BillOut.write("     QTY ITEM NAME                  AMT");
//
//            //  5 blank line and then .. Bill No line
//            int blankLine = 0;
//            if (lineCount < 8)
//            {
//                for (int i = 0; i < 8 - lineCount; i++)
//                {
//                    BillOut.newLine();
//                    blankLine++;
//                }
//            }
//            lineCount += blankLine;
//            BillOut.write(" " + billNo + "   ");
//            SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy");
//            BillOut.write(ft.format(rs_BillHD.getObject(3)));
//            tblName = rs_BillHD.getString(18);
//            BillOut.write("   " + tblName + "   ");
//            waiterName = rs_BillHD.getString(19);
//            BillOut.write(waiterName);
//            BillOut.newLine();
//            lineCount++;
//
//            subTotal = rs_BillHD.getString(6);
//            grandTotal = rs_BillHD.getString(8);
//            user = rs_BillHD.getString(12);
//            deliveryCharge = rs_BillHD.getString(13);
//            advAmount = rs_BillHD.getString(14);
//            //print card available balance
//
//            //print card available balance
//            if (transType.equals("Void"))
//            {
//                BillOut.write("  Reason      :" + " " + rs_BillHD.getString(22));//voided reason
//                BillOut.newLine();
//                lineCount++;
//            }
//            else if (flgComplimentaryBill)
//            {
//
//                BillOut.write("  Reason      :" + " " + rs_BillHD.getString(23));
//                BillOut.newLine();
//                lineCount++;
//                BillOut.write("  Remark      :" + " " + rs_BillHD.getString(11));
//                BillOut.newLine();
//                lineCount++;
//            }
//            if (clsGlobalVarClass.gCMSIntegrationYN)
//            {
//            }
//            if (rs_BillHD.getString(25) != null && rs_BillHD.getString(25).length() > 0)
//            {
//                if (rs_BillHD.getString(26).length() > 0 || rs_BillHD.getString(27).length() > 0 || rs_BillHD.getString(28).length() > 0)
//                {
////                    BillOut.newLine();
////                    funPrintBlankSpace("ORDER DETAIL", BillOut);
////                    BillOut.write("ORDER DETAIL");
////                    BillOut.newLine();
////                    BillOut.write(Linefor5);
////                    BillOut.newLine();
//                }
//                StringBuilder strValue = new StringBuilder();
//                strValue.setLength(0);
//                if (rs_BillHD.getString(26).length() > 0)
//                {
//                    strValue.append(rs_BillHD.getString(26));
//                }
//                else
//                {
//                    strValue.append("");
//                }
//                int strlenMsg = strValue.length();
//                strValue.setLength(0);
//                int strlenNote = strValue.length();
//                if (strlenNote > 0)
//                {
//                    String note1 = "";
//                    if (strlenNote < 27)
//                    {
//                        note1 = strValue.substring(0, strlenNote);
//                        BillOut.write("  NOTE        :" + note1);
//                        BillOut.newLine();
//                        lineCount++;
//                    }
//                    else
//                    {
//                        note1 = strValue.substring(0, 27);
////                        BillOut.write("  NOTE        :" + note1);
////                        BillOut.newLine();
//                    }
//                    for (int i = 27; i <= strlenNote; i++)
//                    {
//                        int endNote = 0;
//                        endNote = i + 27;
//                        if (strlenNote > endNote)
//                        {
//                            note1 = strValue.substring(i, endNote);
//                            i = endNote;
////                            BillOut.write("               " + note1);
////                            BillOut.newLine();
//                        }
//                        else
//                        {
//                            note1 = strValue.substring(i, strlenNote);
////                            BillOut.write("               " + note1);
////                            BillOut.newLine();
//                            i = strlenNote + 1;
//                        }
//                    }
//                }
//
//            }
//// need 3  blank line to print quantity 
//            BillOut.write(Linefor5);
//            blankLine = 0;
//            if (lineCount < 11)
//            {
//                for (int i = 0; i < 11 - lineCount; i++)
//                {
//                    BillOut.newLine();
//                    blankLine++;
//                }
//            }
//            lineCount += blankLine;
////here  total line count is 11
//
//            // BillOut.write("     QTY ITEM NAME                  AMT");
//            BillOut.newLine();
//            lineCount++;
//            String SQL_BillDtl = "select sum(a.dblQuantity),left(a.strItemName,22) as ItemLine1"
//                    + " ,MID(a.strItemName,23,LENGTH(a.strItemName)) as ItemLine2"
//                    + " ,sum(a.dblAmount),a.strItemCode,a.strKOTNo "
//                    + " from " + billdtl + " a," + billhd + " b "
//                    + " where a.strBillNo=b.strBillNo and a.strClientCode=b.strClientCode and a.strBillNo=? and b.strPOSCode=?  ";
//            if (!clsGlobalVarClass.gPrintTDHItemsInBill)
//            {
//                SQL_BillDtl += "and a.tdhYN='N' ";
//            }
//            if (!clsGlobalVarClass.gPrintOpenItemsOnBill)
//            {
//                SQL_BillDtl += "and a.dblAmount>0 ";
//            }
//
//            SQL_BillDtl += " group by a.strItemCode ";
//            pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(SQL_BillDtl);
//            pst.setString(1, billNo);
//            pst.setString(2, POSCode);
//            ResultSet rs_BillDtl = pst.executeQuery();
//            while (rs_BillDtl.next())
//            {
//                double saleQty = Double.parseDouble(rs_BillDtl.getString(1));
//                String sqlPromoBills = "select dblQuantity from " + billPromoDtl + " "
//                        + " where strBillNo='" + billNo + "' and strItemCode='" + rs_BillDtl.getString(5) + "' "
//                        + " and strPromoType='ItemWise' ";
//                ResultSet rsPromoItems = clsGlobalVarClass.dbMysql.executeResultSet(sqlPromoBills);
//                if (rsPromoItems.next())
//                {
//                    saleQty -= rsPromoItems.getDouble(1);
//                }
//                rsPromoItems.close();
//                String qty = String.valueOf(decimalFormatFor3DecPoint.format(saleQty));
////                if (qty.contains("."))
////                {
////                    String decVal = qty.substring(qty.length() - 2, qty.length());
////                    if (Double.parseDouble(decVal) == 0)
////                    {
////                        qty = qty.substring(0, qty.length() - 2);
////                    }
////                }
//                if (saleQty > 0)
//                {
//                    objuUtility2.funPrintContentWithSpace("Right", qty, 6, BillOut);//Qty Print
//                    BillOut.write(" ");
//                    objuUtility2.funPrintContentWithSpace("Left", rs_BillDtl.getString(2), 20, BillOut);//Item Name
//                    if (flgComplimentaryBill)
//                    {
//                        objuUtility2.funPrintContentWithSpace("Right", "0.00", 7, BillOut);//Amount
//                    }
//                    else
//                    {
//                        objuUtility2.funPrintContentWithSpace("Right", rs_BillDtl.getString(4), 7, BillOut);//Amount
//                    }
//                    BillOut.newLine();
//                    lineCount++;
//                    if (rs_BillDtl.getString(3).trim().length() > 0)
//                    {
//                        BillOut.write("       " + rs_BillDtl.getString(3));
//                        BillOut.newLine();
//                        lineCount++;
//                    }
//                    String sqlModifier = "select count(*) "
//                            + "from " + billModifierdtl + " where strBillNo=? and left(strItemCode,7)=? ";
//                    if (!clsGlobalVarClass.gPrintZeroAmtModifierOnBill)
//                    {
//                        sqlModifier += " and  dblAmount !=0.00 ";
//                    }
//                    pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlModifier);
//                    pst.setString(1, billNo);
//                    pst.setString(2, rs_BillDtl.getString(5));
//                    ResultSet rs_count = pst.executeQuery();
//                    rs_count.next();
//                    int cntRecord = rs_count.getInt(1);
//                    rs_count.close();
//                    if (cntRecord > 0)
//                    {
//                        sqlModifier = "select strModifierName,dblQuantity,dblAmount "
//                                + " from " + billModifierdtl + " "
//                                + " where strBillNo=? and left(strItemCode,7)=? ";
//                        if (!clsGlobalVarClass.gPrintZeroAmtModifierOnBill)
//                        {
//                            sqlModifier += " and  dblAmount !=0.00 ";
//                        }
//                        pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlModifier);
//                        pst.setString(1, billNo);
//                        pst.setString(2, rs_BillDtl.getString(5));
//                        ResultSet rs_modifierRecord = pst.executeQuery();
//                        while (rs_modifierRecord.next())
//                        {
//                            if (flgComplimentaryBill)
//                            {
//                                objuUtility2.funWriteToTextformat5(BillOut, "", rs_modifierRecord.getString(1).toUpperCase(), "0.00", "Format5");
//                                BillOut.newLine();
//                                lineCount++;
//                            }
//                            else
//                            {
//                                objuUtility2.funWriteToTextformat5(BillOut, "", rs_modifierRecord.getString(1).toUpperCase(), rs_modifierRecord.getString(3), "Format5");
//                                BillOut.newLine();
//                                lineCount++;
//                            }
//                        }
//                        rs_modifierRecord.close();
//                    }
//
//                    sql = "select b.strItemCode,b.dblWeight "
//                            + " from " + billhd + " a," + advBookBillDtl + " b "
//                            + " where a.strAdvBookingNo=b.strAdvBookingNo and a.strClientCode=b.strClientCode "
//                            + " and a.strBillNo='" + billNo + "' and b.strItemCode='" + rs_BillDtl.getString(5) + "' "
//                            + " and a.strPOSCode='" + POSCode + "' ";
//                    ResultSet rsWeight = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//                    while (rsWeight.next())
//                    {
//                        BillOut.write("   Weight");
//                        BillOut.write("   " + rsWeight.getDouble(2));
//                        BillOut.newLine();
//                        lineCount++;
//                    }
//                    rsWeight.close();
//                    sql = "select c.strCharName,b.strCharValues "
//                            + " from " + billhd + " a," + advBookBillCharDtl + " b,tblcharactersticsmaster c "
//                            + " where a.strAdvBookingNo=b.strAdvBookingNo and b.strCharCode=c.strCharCode "
//                            + " and a.strBillNo='" + billNo + "' and b.strItemCode='" + rs_BillDtl.getString(5) + "' "
//                            + " and a.strPOSCode='" + POSCode + "' and a.strClientCode=b.strClientCode ";
//                    ResultSet rsCharDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//                    while (rsCharDtl.next())
//                    {
//                        String charName = objUtility.funPrintTextWithAlignment(rsCharDtl.getString(1), 10, "Left");
//                        BillOut.write("   " + charName);
//                        String charVal = objUtility.funPrintTextWithAlignment(rsCharDtl.getString(2), 26, "Left");
//                        BillOut.write("   " + charVal);
//                        BillOut.newLine();
//                        lineCount++;
//                    }
//                    rsCharDtl.close();
//                }
//
//                //      here check no of items in bill.. if items greater than 7 ..then print next items on second page
//                if (lineCount > 28)
//                {
//                    for (int i = 0; i < 19; i++)
//                    {
//                        BillOut.newLine();
//                    }
//                    lineCount = 11;
//                }
//            }
//            rs_BillDtl.close();
//            funPrintPromoItemsInBill(billNo, BillOut, 4);  // Print Promotion Items in Bill for this billno.
//            BillOut.write(Linefor5);
//            BillOut.newLine();
//            lineCount++;
//            lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//            if (clsGlobalVarClass.gPointsOnBillPrint)
//            {
//                String sqlCRMPoints = "select b.dblPoints from " + billhd + " a, tblcrmpoints b "
//                        + " where a.strBillNo=b.strBillNo and a.strClientCode=b.strClientCode "
//                        + " and a.strBillNo='" + billNo + "' and a.strPOSCode='" + POSCode + "' ";
//                ResultSet rsCRMPoints = clsGlobalVarClass.dbMysql.executeResultSet(sqlCRMPoints);
//                if (rsCRMPoints.next())
//                {
//                    funWriteTotalStationery("POINTS ", rsCRMPoints.getString(1), BillOut, "Format5", " ");
//                }
//                rsCRMPoints.close();
//                BillOut.newLine();
//                lineCount++;
//                lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                // currentLineCount-- current line number, totalLineCount -- total number of line in single page , topSpaceLineCount --no of line from actual quantity & items printing start,
//                //totalLinesForNextPage -- required blank line to contiouse print on next page ,
//            }
////1
//            if (flgComplimentaryBill)
//            {
//                funWriteTotalStationery("SUB TOTAL", "0.00", BillOut, "Format5", " ");
//                BillOut.newLine();
//                lineCount++;
//                lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//            }
//            else
//            {
//                funWriteTotalStationery("SUB TOTAL", subTotal, BillOut, "Format5", "");
//                BillOut.newLine();
//                lineCount++;
//                lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//            }
//            sql = "select a.dblDiscPer,a.dblDiscAmt,a.strDiscOnType,a.strDiscOnValue,b.strReasonName,a.strDiscRemarks "
//                    + " from " + billDscFrom + " a ,tblreasonmaster b," + billhd + " c "
//                    + " where  a.strDiscReasonCode=b.strReasonCode and a.strBillNo=c.strBillNo "
//                    + " and a.strClientCode=c.strClientCode and a.strBillNo='" + billNo + "' and c.strPOSCode='" + POSCode + "' ";
//            ResultSet rsDisc = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//            boolean flag = true;
//            while (rsDisc.next())
//            {
//                if (flag)
//                {
//                    flag = false;
//                    BillOut.write("  DISCOUNT");
//                    BillOut.newLine();
//                    lineCount++;
//                    lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                }
//                double dbl = Double.parseDouble(rsDisc.getString("dblDiscPer"));
//                String discText = String.format("%.1f", dbl) + "%" + " On " + rsDisc.getString("strDiscOnValue") + "";
//                if (discText.length() > 30)
//                {
//                    discText = discText.substring(0, 30);
//                }
//                else
//                {
//                    discText = String.format("%-30s", discText);
//                }
//                BillOut.write("  " + discText);
//                String discountOnItem = objUtility.funPrintTextWithAlignment(rsDisc.getString("dblDiscAmt"), 8, "Right");
//                BillOut.write(discountOnItem);
//                BillOut.newLine();
//                lineCount++;
//                lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                BillOut.write("  Reason  : ");
//                String discReason = objUtility.funPrintTextWithAlignment(rsDisc.getString(5), 20, "Left");
//                BillOut.write(discReason);
//                BillOut.newLine();
//                lineCount++;
//                lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                BillOut.write("  Remarks : ");
//                String discRemarks = objUtility.funPrintTextWithAlignment(rsDisc.getString(6), 20, "Left");
//                BillOut.write(discRemarks);
//                BillOut.newLine();
//                lineCount++;
//                lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
////5
//            }
//            String sql_Tax = "select b.strTaxDesc,sum(a.dblTaxAmount) "
//                    + " from " + billtaxdtl + " a,tbltaxhd b," + billhd + " c "
//                    + " where a.strBillNo='" + billNo + "' "
//                    + " and a.strTaxCode=b.strTaxCode "
//                    + " and a.strBillNo=c.strBillNo "
//                    + " and a.strClientCode=c.strClientCode "
//                    + " and c.strPOSCode='" + POSCode + "' "
//                    + " and b.strTaxCalculation='Forward' "
//                    + " group by a.strTaxCode";
//            ResultSet rsTax = clsGlobalVarClass.dbMysql.executeResultSet(sql_Tax);
//            while (rsTax.next())
//            {
//                if (flgComplimentaryBill)
//                {
//                    funWriteTotal(rsTax.getString(1), "0.00", BillOut, "Format5");
//                }
//                else
//                {
//                    funWriteTotalStationery(rsTax.getString(1), rsTax.getString(2), BillOut, "Format5", "");
//                }
//                BillOut.newLine();
//                lineCount++;
//                lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//            }
//            if (deliveryCharge != null && deliveryCharge.trim().length() > 0 && !"0.00".equalsIgnoreCase(deliveryCharge))
//            {
//                funWriteTotalStationery("DELV. CHARGE", deliveryCharge, BillOut, "Format5", "");
//                BillOut.newLine();
//                lineCount++;
//                lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//            }
//            if (advAmount.trim().length() > 0 && !"0.00".equalsIgnoreCase(advAmount))
//            {
//                funWriteTotalStationery("ADVANCE", advAmount, BillOut, "Format5", "");
//                BillOut.newLine();
//                lineCount++;
//                lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//            }
//            BillOut.write(Linefor5);
//            BillOut.newLine();
//            lineCount++;
//            lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//            if (flgComplimentaryBill)
//            {
//                funWriteTotalStationery("TOTAL(ROUNDED)", "0.00", BillOut, "Format5", "");
//            }
//            else
//            {
//                funWriteTotalStationery("TOTAL(ROUNDED)", grandTotal, BillOut, "Format5", "");
//            }
//            BillOut.newLine();
//            lineCount++;
//            lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//            BillOut.write(Linefor5);
////10
//            //print Grand total of other bill nos from bill series
//            if (clsGlobalVarClass.gEnableBillSeries)
//            {
//                String sqlPrintGT = "select a.strPrintGTOfOtherBills,b.strDtlBillNos,b.dblGrandTotal "
//                        + "from tblbillseries a,tblbillseriesbilldtl b "
//                        + "where (a.strPOSCode=b.strPOSCode or a.strPOSCode='All') "
//                        + "and a.strBillSeries=b.strBillSeries "
//                        + "and b.strHdBillNo='" + billNo + "' and b.strPOSCode='" + POSCode + "' ";
//                ResultSet rsPrintGTYN = clsGlobalVarClass.dbMysql.executeResultSet(sqlPrintGT);
//                double dblOtherBillsGT = 0.00;
//                if (rsPrintGTYN.next())
//                {
//                    if (rsPrintGTYN.getString(1).equalsIgnoreCase("Y"))
//                    {
//                        String billSeriesDtlBillNos = rsPrintGTYN.getString(2);
//                        String[] dtlBillSeriesBillNo = billSeriesDtlBillNos.split(",");
//                        dblOtherBillsGT += rsPrintGTYN.getDouble(3);
//                        if (dtlBillSeriesBillNo.length > 0)
//                        {
//                            for (int i = 0; i < dtlBillSeriesBillNo.length; i++)
//                            {
//                                sqlPrintGT = "select a.strHdBillNo,a.dblGrandTotal "
//                                        + "from tblbillseriesbilldtl a "
//                                        + "where a.strHdBillNo='" + dtlBillSeriesBillNo[i] + "' and a.strPOSCode='" + POSCode + "' ";
//                                ResultSet rsPrintGT = clsGlobalVarClass.dbMysql.executeResultSet(sqlPrintGT);
//                                if (rsPrintGT.next())
//                                {
//                                    BillOut.newLine();
//                                    lineCount++;
//                                    lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                                    funWriteTotalStationery(dtlBillSeriesBillNo[i] + " TOTAL(ROUNDED)", rsPrintGT.getString(2), BillOut, "Format5", "");
//                                    dblOtherBillsGT += rsPrintGT.getDouble(2);
//                                    BillOut.newLine();
//                                    lineCount++;
//                                    lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                                }
//                            }
//                            BillOut.write(Linefor5);
//                            BillOut.newLine();
//                            lineCount++;
//                            lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                            funWriteTotalStationery("GRAND TOTAL(ROUNDED)", String.valueOf(dblOtherBillsGT), BillOut, "Format5", "");
//                            BillOut.newLine();
//                            lineCount++;
//                            lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                            BillOut.write(Linefor5);
//                            BillOut.newLine();
//                            lineCount++;
//                            lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                        }
//                    }
//                }
//            }
////15
//            //settlement breakup part
//            String sqlSettlementBreakup = "select a.dblSettlementAmt, b.strSettelmentDesc, b.strSettelmentType "
//                    + " from " + billSettlementdtl + " a ,tblsettelmenthd b," + billhd + " c "
//                    + " where a.strBillNo=? and a.strBillNo=c.strBillNo and a.strClientCode=c.strClientCode "
//                    + " and a.strSettlementCode=b.strSettelmentCode and c.strPOSCode=? ";
//            pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlSettlementBreakup);
//            pst.setString(1, billNo);
//            pst.setString(2, POSCode);
//            boolean flgSettlement = false;
//            boolean creditSettlement = false;
//            ResultSet rsBillSettlement = pst.executeQuery();
//            while (rsBillSettlement.next())
//            {
//                if (flgComplimentaryBill)
//                {
//                    BillOut.newLine();
//                    funWriteTotalStationery(rsBillSettlement.getString(2), "0.00", BillOut, "Format5", "");
//                }
//                else
//                {
//                    BillOut.newLine();
//                    funWriteTotalStationery(rsBillSettlement.getString(2), rsBillSettlement.getString(1), BillOut, "Format5", "");
//                }
//                lineCount++;
//                lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                flgSettlement = true;
//                if (rsBillSettlement.getString(3).equals("Credit"))
//                {
//                    creditSettlement = true;
//                }
//            }
//            rsBillSettlement.close();
//
//            if (flgSettlement)
//            {
//                if (creditSettlement)
//                {
//                    BillOut.newLine();
//                    lineCount++;
//                    lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                    funWriteTotalStationery("Credit Remarks ", rs_BillHD.getString(11), BillOut, "Format5", "");
//                    BillOut.newLine();
//                    lineCount++;
//                    lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                    String custName = rs_BillHD.getString(24);
//                    if (!custName.isEmpty())
//                    {
//                        funWriteTotalStationery("Customer " + custName, "", BillOut, "Format5", "");
//                    }
//                    BillOut.newLine();
//                    lineCount++;
//                    lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                    BillOut.write(Linefor5);
//                }
//            }
////19
//            String sqlTenderAmt = "select sum(a.dblPaidAmt),sum(a.dblSettlementAmt),(sum(a.dblPaidAmt)-sum(a.dblSettlementAmt)) RefundAmt "
//                    + " from " + billSettlementdtl + " a," + billhd + " b "
//                    + " where a.strBillNo=b.strBillNo and a.strClientCode=b.strClientCode "
//                    + " and b.strBillNo='" + billNo + "' and b.strPOSCode='" + POSCode + "' "
//                    + " group by a.strBillNo";
//            ResultSet rsTenderAmt = clsGlobalVarClass.dbMysql.executeResultSet(sqlTenderAmt);
//            if (rsTenderAmt.next())
//            {
//                BillOut.newLine();
//                lineCount++;
//                lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                if (flgComplimentaryBill)
//                {
//                    funWriteTotalStationery("PAID AMT", "0.00", BillOut, "Format5", "");
//                    //BillOut.newLine();
//                }
//                else
//                {
//                    funWriteTotalStationery("PAID AMT", rsTenderAmt.getString(1), BillOut, "Format5", "");
//                    BillOut.newLine();
//                    lineCount++;
//                    lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                    if (rsTenderAmt.getDouble(3) > 0)
//                    {
//                        funWriteTotalStationery("REFUND AMT", rsTenderAmt.getString(3), BillOut, "Format5", "");
//                        //BillOut.newLine();
//                    }
//                }
//                BillOut.newLine();
//                lineCount++;
//                lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                BillOut.write(Linefor5);
//            }
//            rsTenderAmt.close();
//
//            if (rs_BillHD.getDouble(29) > 0)
//            {
//                BillOut.newLine();
//                lineCount++;
//                lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                funWriteTotalStationery("TIP AMT", rs_BillHD.getString(29), BillOut, "Format5", "");
//                //BillOut.newLine();
//            }
//            if (flag_isHomeDelvBill)
//            {
//                BillOut.newLine();
//                lineCount++;
//                lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                String sql_count = "select count(*) from tblhomedelivery where strCustomerCode=?";
//                pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sql_count);
//                pst.setString(1, customerCode);
//                ResultSet rs_Count = pst.executeQuery();
//                rs_Count.next();
//                BillOut.write("CUSTOMER COUNT : " + rs_Count.getString(1));
//                rs_Count.close();
//                BillOut.newLine();
//                lineCount++;
//                lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                BillOut.write(Linefor5);
//            }
//            BillOut.newLine();
//            lineCount++;
//            lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//
//            funPrintServiceVatNo(BillOut, 4, billNo, billDate, billtaxdtl);
//
//            if (clsGlobalVarClass.gEnableBillSeries)
//            {
//                sql = "select b.strPrintInclusiveOfTaxOnBill "
//                        + " from tblbillseriesbilldtl a,tblbillseries b "
//                        + " where a.strBillSeries=b.strBillSeries and a.strHdBillNo='" + billNo + "' and a.strClientCode=b.strClientCode";
//                ResultSet rsBillSeries = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//                if (rsBillSeries.next())
//                {
//                    if (rsBillSeries.getString(1).equals("Y"))
//                    {
//                        BillOut.write(Linefor5);
//                        BillOut.newLine();
//                        lineCount++;
//                        lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                        funPrintBlankSpace("(INCLUSIVE OF ALL TAXES)", BillOut);
//                        BillOut.write("(INCLUSIVE OF ALL TAXES)");
//                        BillOut.newLine();
//                        lineCount++;
//                        lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                    }
//                }
//                rsBillSeries.close();
//            }
//            else
//            {
//                if (clsGlobalVarClass.gPrintInclusiveOfAllTaxes.equalsIgnoreCase("Y"))
//                {
//                    BillOut.write(Linefor5);
//                    BillOut.newLine();
//                    lineCount++;
//                    lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                    funPrintBlankSpace("(INCLUSIVE OF ALL TAXES)", BillOut);
//                    BillOut.write("(INCLUSIVE OF ALL TAXES)");
//                    BillOut.newLine();
//                    lineCount++;
//                    lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                }
//            }
//
//            int num = clsGlobalVarClass.gBillFooter.trim().length() / 30;
//            int num1 = clsGlobalVarClass.gBillFooter.trim().length() % 30;
//            int cnt1 = 0;
//            for (int cnt = 0; cnt < num; cnt++)
//            {
//                String footer = clsGlobalVarClass.gBillFooter.trim().substring(cnt1, (cnt1 + 30));
//                footer = footer.replaceAll("\n", "");
//                BillOut.write("   " + footer.trim());
//                BillOut.newLine();
//                lineCount++;
//                lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//                cnt1 += 30;
//            }
//            BillOut.write("   " + clsGlobalVarClass.gBillFooter.trim().substring(cnt1, (cnt1 + num1)).trim());
//            BillOut.newLine();
//            lineCount++;
//            lineCount = funWriteBlankLines(lineCount, 28, 11, 19, BillOut);
//            funPrintBlankSpace(user, BillOut);
//            BillOut.write(user);
//            if (lineCount < 29)
//            {
//                for (int i = 0; i < 29 - lineCount; i++)
//                {
//                    BillOut.newLine();
//                }
//            }
//            if (!clsGlobalVarClass.gOpenCashDrawerAfterBillPrintYN)
//            {
//                if ("linux".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
//                {
//                    BillOut.write("V");//Linux
//                }
//                else if ("windows".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
//                {
//                    if ("Inbuild".equalsIgnoreCase(clsPosConfigFile.gPrinterType))
//                    {
//                        BillOut.write("V");
//                    }
//                    else
//                    {
//                        BillOut.write("m");//windows
//                    }
//                }
//            }
//            rs_BillHD.close();
//            BillOut.close();
//            fstream_bill.close();
//            pst.close();
//
//            if (formName.equalsIgnoreCase("sales report"))
//            {
//                funShowTextFile(Text_Bill, formName, clsGlobalVarClass.gBillPrintPrinterPort);
//            }
//            else
//            {
//                if (clsGlobalVarClass.gShowBill)
//                {
//                    funShowTextFile(Text_Bill, formName, clsGlobalVarClass.gBillPrintPrinterPort);
//                }
//            }
//
//            if (!formName.equalsIgnoreCase("sales report"))
//            {
//                if (transType.equalsIgnoreCase("void"))
//                {
//                    if (clsGlobalVarClass.gPrintOnVoidBill)
//                    {
//                        if (!viewORprint.equalsIgnoreCase("view"))
//                        {
//                            funPrintToPrinter(clsGlobalVarClass.gBillPrintPrinterPort, "", "bill", "N", isReprint);
//                        }
//                    }
//                }
//                else
//                {
//                    if (!clsGlobalVarClass.flgReprintView)
//                    {
//                        if (!viewORprint.equalsIgnoreCase("view"))
//                        {
//                            funPrintToPrinter(clsGlobalVarClass.gBillPrintPrinterPort, "", "bill", "N", isReprint);
//                        }
//                    }
//                    else
//                    {
//                        clsGlobalVarClass.flgReprintView = false;
//                    }
//                }
//            }
//            //if (formName.equalsIgnoreCase("sales report"))
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * text file bill printing format to merge modifier amount to item
//     *
//     * @param billNo
//     * @param reprint
//     * @param formName
//     * @param transType
//     * @param billDate
//     * @param POSCode
//     * @param viewORprint
//     */
//    public void funGenerateTextFileBillPrintingForFormat18(String billNo, String reprint, String formName, String transType, String billDate, String POSCode, String viewORprint)
//    {
//        clsUtility objUtility = new clsUtility();
//        clsUtility2 objUtility2 = new clsUtility2();
//        String Linefor5 = "  --------------------------------------";
//        try
//        {
//            String user = "";
//            String billhd = null;
//            String billdtl = null;
//            String billModifierdtl = null;
//            String billSettlementdtl = null;
//            String billtaxdtl = null;
//            String billDscFrom = null;
//            String billPromoDtl = null;
//            String billComplDtl = null;
//            String advBookBillHd = null;
//            String advBookBillDtl = null;
//            String advBookBillCharDtl = null;
//            String advReceiptHd = null;
//            if (clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("HOPOS"))
//            {
//                billhd = "tblqbillhd";
//                billdtl = "tblqbilldtl";
//                billModifierdtl = "tblqbillmodifierdtl";
//                billSettlementdtl = "tblqbillsettlementdtl";
//                billtaxdtl = "tblqbilltaxdtl";
//                billDscFrom = "tblqbilldiscdtl";
//                billPromoDtl = "tblqbillpromotiondtl";
//                billComplDtl = "tblqbillcomplementrydtl";
//
//                advBookBillHd = "tblqadvbookbillhd";
//                advBookBillDtl = "tblqadvbookbilldtl";
//                advBookBillCharDtl = "tblqadvbookbillchardtl";
//                advReceiptHd = "tblqadvancereceipthd";
//            }
//            else
//            {
//                if ("sales report".equalsIgnoreCase(formName))
//                {
//                    billhd = "tblbillhd";
//                    billdtl = "tblbilldtl";
//                    billModifierdtl = "tblbillmodifierdtl";
//                    billSettlementdtl = "tblbillsettlementdtl";
//                    billtaxdtl = "tblbilltaxdtl";
//                    billDscFrom = "tblbilldiscdtl";
//                    billPromoDtl = "tblbillpromotiondtl";
//                    billComplDtl = "tblbillcomplementrydtl";
//                    advBookBillHd = "tbladvbookbillhd";
//                    advBookBillDtl = "tbladvbookbilldtl";
//                    advBookBillCharDtl = "tbladvbookbillchardtl";
//                    advReceiptHd = "tbladvancereceipthd";
//                    long dateDiff = new clsUtility().funCompareDate(billDate, objUtility.funGetPOSDateForTransaction());
//                    if (dateDiff > 0)
//                    {
//                        billhd = "tblqbillhd";
//                        billdtl = "tblqbilldtl";
//                        billModifierdtl = "tblqbillmodifierdtl";
//                        billSettlementdtl = "tblqbillsettlementdtl";
//                        billtaxdtl = "tblqbilltaxdtl";
//                        billDscFrom = "tblqbilldiscdtl";
//                        billPromoDtl = "tblqbillpromotiondtl";
//                        billComplDtl = "tblqbillcomplementrydtl";
//                        advBookBillHd = "tblqadvbookbillhd";
//                        advBookBillDtl = "tblqadvbookbilldtl";
//                        advBookBillCharDtl = "tblqadvbookbillchardtl";
//                        advReceiptHd = "tblqadvancereceipthd";
//                    }
//                    String sql = "select count(strBillNo) from tblbillhd where strBillNo='" + billNo + "' and strPOSCode='" + POSCode + "' and date(dteBillDate)='" + billDate + "' ";
//                    ResultSet rsBillTable = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//                    rsBillTable.next();
//                    int billCnt = rsBillTable.getInt(1);
//                    if (billCnt == 0)
//                    {
//                        billhd = "tblqbillhd";
//                        billdtl = "tblqbilldtl";
//                        billModifierdtl = "tblqbillmodifierdtl";
//                        billSettlementdtl = "tblqbillsettlementdtl";
//                        billtaxdtl = "tblqbilltaxdtl";
//                        billDscFrom = "tblqbilldiscdtl";
//                        billPromoDtl = "tblqbillpromotiondtl";
//                        billComplDtl = "tblqbillcomplementrydtl";
//                        advBookBillHd = "tblqadvbookbillhd";
//                        advBookBillDtl = "tblqadvbookbilldtl";
//                        advBookBillCharDtl = "tblqadvbookbillchardtl";
//                        advReceiptHd = "tblqadvancereceipthd";
//                    }
//                }
//                else
//                {
//                    billhd = "tblbillhd";
//                    billdtl = "tblbilldtl";
//                    billModifierdtl = "tblbillmodifierdtl";
//                    billSettlementdtl = "tblbillsettlementdtl";
//                    billtaxdtl = "tblbilltaxdtl";
//                    billDscFrom = "tblbilldiscdtl";
//                    billPromoDtl = "tblbillpromotiondtl";
//                    billComplDtl = "tblbillcomplementrydtl";
//                    advBookBillHd = "tbladvbookbillhd";
//                    advBookBillDtl = "tbladvbookbilldtl";
//                    advBookBillCharDtl = "tbladvbookbillchardtl";
//                    advReceiptHd = "tbladvancereceipthd";
//                }
//            }
//            PreparedStatement pst = null;
//            funCreateTempFolder();
//            String filePath = System.getProperty("user.dir");
//            File Text_Bill = new File(filePath + "/Temp/Temp_Bill.txt");
//            String subTotal = "";
//            String grandTotal = "";
//            String advAmount = "";
//            String deliveryCharge = "";
//            String customerCode = "";
//            String waiterName = "";
//            String tblName = "";
//            ResultSet rs_BillHD = null;
//            boolean flgComplimentaryBill = false;
//
//            billDate = billDate.split(" ")[0];
//
//            StringBuilder sqlBillHeaderDtl = new StringBuilder();
//            sqlBillHeaderDtl.append("select ifnull(a.strTableNo,''),ifnull(a.strWaiterNo,''),a.dteBillDate,time(a.dteBillDate),a.dblDiscountAmt,a.dblSubTotal,"
//                    + "ifnull(a.strCustomerCode,''),a.dblGrandTotal,a.dblTaxAmt,ifnull(a.strReasonCode,''),ifnull(a.strRemarks,''),a.strUserCreated "
//                    + ",ifnull(dblDeliveryCharges,0.00),ifnull(i.dblAdvDeposite,0.00),a.dblDiscountPer,b.strPOSName,a.intPaxNo "
//                    + ",ifnull(c.strTableName,''),ifnull(d.strWShortName,''),ifnull(d.strWFullName,''),ifnull(l.strSettelmentType,''),ifnull(j.strReasonName,'') as voidedReason, "
//                    + "ifnull(g.strReasonName,''),ifnull(e.strCustomerName,''),ifnull(a.strAdvBookingNo,''),ifnull(h.strMessage,''),ifnull(h.strShape,''),ifnull(h.strNote,''),ifnull(a.dblTipAmount,0.00) "
//                    + ",a.strOperationType,ifnull(a.strTakeAwayRemarks,''),ifnull(e.longMobileNo,'')  "
//                    + "from " + billhd + " a "
//                    + "left outer join tblposmaster b on a.strPOSCode=b.strPosCode  "
//                    + "left outer join tbltablemaster c on a.strTableNo=c.strTableNo and a.strClientCode=c.strClientCode "
//                    + "left outer join tblwaitermaster d on a.strWaiterNo=d.strWaiterNo and a.strClientCode=d.strClientCode "
//                    + "left outer join tblcustomermaster e on a.strCustomerCode=e.strCustomerCode and a.strClientCode=e.strClientCode "
//                    + "left outer join tbldebitcardmaster f on a.strCardNo=f.strCardNo "
//                    + "left outer join tblreasonmaster g on a.strReasonCode=g.strReasonCode "
//                    + "left outer join " + advBookBillHd + " h on a.strAdvBookingNo=h.strAdvBookingNo and a.strClientCode=h.strClientCode "
//                    + "left outer join " + advReceiptHd + " i on h.strAdvBookingNo=i.strAdvBookingNo and a.strClientCode=i.strClientCode "
//                    + "left outer join tblvoidbillhd j on a.strBillNo=j.strBillNo and a.strPOSCode=j.strPosCode and a.strClientCode=j.strClientCode "
//                    + "left outer join " + billSettlementdtl + " k on a.strBillNo=k.strBillNo and a.strClientCode=k.strClientCode AND DATE(a.dteBillDate)=DATE(k.dteBillDate) "
//                    + "left outer join tblsettelmenthd l on k.strSettlementCode=l.strSettelmentCode "
//                    + "where a.strBillNo=? "
//                    + "and a.strPOSCode=? "
//                    + "and date(a.dteBillDate)=? "
//                    + "group by a.strBillNo; ");
//            pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlBillHeaderDtl.toString());
//            pst.setString(1, billNo);
//            pst.setString(2, POSCode);
//            pst.setString(3, billDate);
//            rs_BillHD = pst.executeQuery();
//            rs_BillHD.next();
//            if (rs_BillHD.getString(21).equals("Complementary"))
//            {
//                flgComplimentaryBill = true;
//            }
//            FileWriter fstream_bill = new FileWriter(Text_Bill);
//            BufferedWriter BillOut = new BufferedWriter(fstream_bill);
//            if (clsGlobalVarClass.gClientCode.equals("117.001"))
//            {
//                if (POSCode.equals("P01"))
//                {
//                    funPrintBlankSpace("THE PREM'S HOTEL", BillOut);
//                    BillOut.write("THE PREM'S HOTEL");
//                    BillOut.newLine();
//                }
//                else if (POSCode.equals("P02"))
//                {
//                    funPrintBlankSpace("SWIG", BillOut);
//                    BillOut.write("SWIG");
//                    BillOut.newLine();
//                }
//            }
//            boolean isReprint = false;
//            if ("reprint".equalsIgnoreCase(reprint))
//            {
//                isReprint = true;
//                funPrintBlankSpace("[DUPLICATE]", BillOut);
//                BillOut.write("[DUPLICATE]");
//                BillOut.newLine();
//            }
//            if (transType.equals("Void"))
//            {
//                funPrintBlankSpace("VOIDED BILL", BillOut);
//                BillOut.write("VOIDED BILL");
//                BillOut.newLine();
//            }
//            boolean flag_isHomeDelvBill = false;
//            String SQL_HomeDelivery = "select strBillNo,strCustomerCode,strDPCode,tmeTime,strCustAddressLine1 "
//                    + "from tblhomedelivery "
//                    + "where strBillNo=? "
//                    + "and date(dteDate)=? ;";
//            pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(SQL_HomeDelivery);
//            pst.setString(1, billNo);
//            pst.setString(2, billDate);
//            ResultSet rs_HomeDelivery = pst.executeQuery();
//            if (rs_HomeDelivery.next())
//            {
//                flag_isHomeDelvBill = true;
//                customerCode = rs_HomeDelivery.getString(2);
//                if (clsGlobalVarClass.gPrintHomeDeliveryYN)
//                {
//                    funPrintBlankSpace("HOME DELIVERY", BillOut);
//                    BillOut.write("HOME DELIVERY");
//                    BillOut.newLine();
//                }
//
//                String SQL_CustomerDtl = "";
//                if (rs_HomeDelivery.getString(5).equals("Temporary"))
//                {
//                    SQL_CustomerDtl = "select a.strCustomerName,a.strTempAddress,a.strTempStreet"
//                            + " ,a.strTempLandmark,a.strBuildingName,a.strCity,a.intPinCode,a.longMobileNo "
//                            + " from tblcustomermaster a left outer join tblbuildingmaster b "
//                            + " on a.strBuldingCode=b.strBuildingCode "
//                            + " where a.strCustomerCode=? ;";
//                }
//                else if (rs_HomeDelivery.getString(5).equals("Office"))
//                {
//                    SQL_CustomerDtl = "select a.strCustomerName,a.strOfficeBuildingName,a.strOfficeStreetName"
//                            + ",a.strOfficeLandmark,a.strOfficeArea,a.strOfficeCity,a.strOfficePinCode,a.longMobileNo "
//                            + " from tblcustomermaster a "
//                            + " where a.strCustomerCode=? ";
//                }
//                else
//                {
//                    SQL_CustomerDtl = "select a.strCustomerName,a.strCustAddress,a.strStreetName"
//                            + " ,a.strLandmark,a.strBuildingName,a.strCity,a.intPinCode,a.longMobileNo "
//                            + " from tblcustomermaster a left outer join tblbuildingmaster b "
//                            + " on a.strBuldingCode=b.strBuildingCode "
//                            + " where a.strCustomerCode=? ;";
//                }
//                pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(SQL_CustomerDtl);
//                pst.setString(1, rs_HomeDelivery.getString(2));
//                ResultSet rs_CustomerDtl = pst.executeQuery();
//                while (rs_CustomerDtl.next())
//                {
//                    BillOut.write("  NAME      :" + rs_CustomerDtl.getString(1).toUpperCase());
//                    BillOut.newLine();
//                    // Building Name    
//                    String add = rs_CustomerDtl.getString(2);
//                    int strlen = add.length();
//                    String add1 = "";
//                    if (strlen < 28)
//                    {
//                        add1 = add.substring(0, strlen);
//                        BillOut.write("  ADDRESS1  :" + add1.toUpperCase().replaceAll("\n", " "));
//                        BillOut.newLine();
//                    }
//                    else
//                    {
//                        add1 = add.substring(0, 28);
//                        BillOut.write("  ADDRESS1  :" + add1.toUpperCase().replaceAll("\n", " "));
//                        BillOut.newLine();
//                    }
//                    for (int i = 28; i <= strlen;)
//                    {
//                        int end = 0;
//                        end = i + 28;
//                        if (strlen > end)
//                        {
//                            add1 = add.substring(i, end);
//                            i = end;
//                            BillOut.write("             " + add1.toUpperCase().replaceAll("\n", " "));
//                            BillOut.newLine();
//                        }
//                        else
//                        {
//                            add1 = add.substring(i, strlen);
//                            BillOut.write("             " + add1.toUpperCase().replaceAll("\n", " "));
//                            BillOut.newLine();
//                            i = strlen + 1;
//                        }
//                    }
//                    // Street Name    
//                    String street = rs_CustomerDtl.getString(3);
//                    String street1;
//                    int streetlen = street.length();
//                    for (int i = 0; i <= streetlen;)
//                    {
//                        int end = 0;
//                        end = i + 28;
//                        if (streetlen > end)
//                        {
//                            street1 = street.substring(i, end);
//                            BillOut.write("             " + street1.toUpperCase());
//                            BillOut.newLine();
//                            i = end;
//                        }
//                        else
//                        {
//                            street1 = street.substring(i, streetlen);
//                            BillOut.write("             " + street1.toUpperCase());
//                            BillOut.newLine();
//                            i = streetlen + 1;
//                        }
//                    }
//                    // Landmark Name    
//                    if (rs_CustomerDtl.getString(4).trim().length() > 0)
//                    {
//                        BillOut.write("             " + rs_CustomerDtl.getString(4).toUpperCase());
//                        BillOut.newLine();
//                    }
//                    // Area Name    
//                    if (rs_CustomerDtl.getString(5).trim().length() > 0)
//                    {
//                        BillOut.write("             " + rs_CustomerDtl.getString(5).toUpperCase());
//                        BillOut.newLine();
//                    }
//                    // City Name    
//                    if (rs_CustomerDtl.getString(6).trim().length() > 0)
//                    {
//                        BillOut.write("             " + rs_CustomerDtl.getString(6).toUpperCase());
//                        BillOut.newLine();
//                    }
//                    // Pin Code    
//                    if (rs_CustomerDtl.getString(7).trim().length() > 0)
//                    {
//                        BillOut.write("             " + rs_CustomerDtl.getString(7).toUpperCase());
//                        BillOut.newLine();
//                    }
//                    // Mobile No    
//                    BillOut.write("  MOBILE NO :" + rs_CustomerDtl.getString(8));
//                    BillOut.newLine();
//                }
//                rs_CustomerDtl.close();
//                if (null != rs_HomeDelivery.getString(3) && rs_HomeDelivery.getString(3).trim().length() > 0)
//                {
//                    String[] delBoys = rs_HomeDelivery.getString(3).split(",");
//                    StringBuilder strIN = new StringBuilder("(");
//                    for (int i = 0; i < delBoys.length; i++)
//                    {
//                        if (i == 0)
//                        {
//                            strIN.append("'" + delBoys[i] + "'");
//                        }
//                        else
//                        {
//                            strIN.append(",'" + delBoys[i] + "'");
//                        }
//                    }
//                    strIN.append(")");
//                    String SQL_DeliveryBoyDtl = "select strDPName from tbldeliverypersonmaster where strDPCode IN " + strIN + " ;";
//                    pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(SQL_DeliveryBoyDtl);
//                    ResultSet rs_DeliveryBoyDtl = pst.executeQuery();
//                    strIN.setLength(0);
//                    for (int i = 0; rs_DeliveryBoyDtl.next(); i++)
//                    {
//                        if (i == 0)
//                        {
//                            strIN.append(rs_DeliveryBoyDtl.getString(1).toUpperCase());
//                        }
//                        else
//                        {
//                            strIN.append("," + rs_DeliveryBoyDtl.getString(1).toUpperCase());
//                        }
//                    }
//                    BillOut.write("  DELV BOY  :" + strIN);
//                    BillOut.newLine();
//                    rs_DeliveryBoyDtl.close();
//                }
//                BillOut.write(Line);
//                BillOut.newLine();
//            }
//            else
//            {
//                if (rs_BillHD.getString(7).length() > 0)//customerCode
//                {
//                    BillOut.write("  NAME      :" + rs_BillHD.getString(24).toUpperCase());
//                    BillOut.newLine();
//                    // Mobile No    
//                    BillOut.write("  MOBILE NO :" + rs_BillHD.getString(32));
//                    BillOut.newLine();
//                }
//            }
//            rs_HomeDelivery.close();
//            //print take away
//            int billPrintSize = 4;
//            if (rs_BillHD.getString(30).equals("TakeAway"))
//            {
//                funPrintBlankSpace("Take Away", BillOut);
//                BillOut.write("Take Away");
//                BillOut.newLine();
//            }
//            if (clsGlobalVarClass.gPrintTaxInvoice.equalsIgnoreCase("Y"))
//            {
//                funPrintBlankSpace("TAX INVOICE", BillOut);
//                BillOut.write("TAX INVOICE");
//                BillOut.newLine();
//            }
//            if (clsGlobalVarClass.gClientCode.equals("047.001") && POSCode.equals("P03"))
//            {
//                funPrintBlankSpace("SHRI SHAM CATERERS", BillOut);
//                BillOut.write("SHRI SHAM CATERERS");
//                BillOut.newLine();
//                String cAddr1 = "Flat No.7, Mon Amour,";
//                funPrintBlankSpace(cAddr1, BillOut);
//                BillOut.write(cAddr1.toUpperCase());
//                BillOut.newLine();
//                String cAddr2 = "Thorat Colony,Prabhat Road,";
//                funPrintBlankSpace(cAddr2, BillOut);
//                BillOut.write(cAddr2.toUpperCase());
//                BillOut.newLine();
//                String cAddr3 = " Erandwane, Pune 411 004.";
//                funPrintBlankSpace(cAddr3, BillOut);
//                BillOut.write(cAddr3.toUpperCase());
//                BillOut.newLine();
//                String cAddr4 = "Approved Caterers of";
//                funPrintBlankSpace(cAddr4, BillOut);
//                BillOut.write(cAddr4.toUpperCase());
//                BillOut.newLine();
//                String cAddr5 = "ROYAL CONNAUGHT BOAT CLUB";
//                funPrintBlankSpace(cAddr5, BillOut);
//                BillOut.write(cAddr5.toUpperCase());
//                BillOut.newLine();
//            }
//            else if (clsGlobalVarClass.gClientCode.equals("047.001") && POSCode.equals("P02"))
//            {
//                funPrintBlankSpace("SHRI SHAM CATERERS", BillOut);
//                BillOut.write("SHRI SHAM CATERERS");
//                BillOut.newLine();
//                String cAddr1 = "Flat No.7, Mon Amour,";
//                funPrintBlankSpace(cAddr1, BillOut);
//                BillOut.write(cAddr1.toUpperCase());
//                BillOut.newLine();
//                String cAddr2 = "Thorat Colony,Prabhat Road,";
//                funPrintBlankSpace(cAddr2, BillOut);
//                BillOut.write(cAddr2.toUpperCase());
//                BillOut.newLine();
//                String cAddr3 = " Erandwane, Pune 411 004.";
//                funPrintBlankSpace(cAddr3, BillOut);
//                BillOut.write(cAddr3.toUpperCase());
//                BillOut.newLine();
//                String cAddr4 = "Approved Caterers of";
//                funPrintBlankSpace(cAddr4, BillOut);
//                BillOut.write(cAddr4.toUpperCase());
//                BillOut.newLine();
//                String cAddr5 = "ROYAL CONNAUGHT BOAT CLUB";
//                funPrintBlankSpace(cAddr5, BillOut);
//                BillOut.write(cAddr5.toUpperCase());
//                BillOut.newLine();
//            }
//            else if (clsGlobalVarClass.gClientCode.equals("092.001") || clsGlobalVarClass.gClientCode.equals("092.002") || clsGlobalVarClass.gClientCode.equals("092.003"))//Shree Sound Pvt. Ltd.
//            {
//                funPrintBlankSpace("SSPL", BillOut);
//                BillOut.write("SSPL");
//                BillOut.newLine();
//                funPrintBlankSpace(clsGlobalVarClass.gClientAddress1, BillOut);
//                BillOut.write(clsGlobalVarClass.gClientAddress1.toUpperCase());
//                BillOut.newLine();
//                if (clsGlobalVarClass.gClientAddress2.trim().length() > 0)
//                {
//                    funPrintBlankSpace(clsGlobalVarClass.gClientAddress2, BillOut);
//                    BillOut.write(clsGlobalVarClass.gClientAddress2.toUpperCase());
//                    BillOut.newLine();
//                }
//                if (clsGlobalVarClass.gClientAddress3.trim().length() > 0)
//                {
//                    funPrintBlankSpace(clsGlobalVarClass.gClientAddress3, BillOut);
//                    BillOut.write(clsGlobalVarClass.gClientAddress3.toUpperCase());
//                    BillOut.newLine();
//                }
//                if (clsGlobalVarClass.gCityName.trim().length() > 0)
//                {
//                    funPrintBlankSpace(clsGlobalVarClass.gCityName, BillOut);
//                    BillOut.write(clsGlobalVarClass.gCityName.toUpperCase());
//                    BillOut.newLine();
//                }
//            }
//            else if (clsGlobalVarClass.gClientCode.equals("092.001") || clsGlobalVarClass.gClientCode.equals("092.002") || clsGlobalVarClass.gClientCode.equals("092.003"))//Shree Sound Pvt. Ltd.
//            {
//                funPrintBlankSpace("SSPL", BillOut);
//                BillOut.write("SSPL");
//                BillOut.newLine();
//                funPrintBlankSpace(clsGlobalVarClass.gClientAddress1, BillOut);
//                BillOut.write(clsGlobalVarClass.gClientAddress1.toUpperCase());
//                BillOut.newLine();
//                if (clsGlobalVarClass.gClientAddress2.trim().length() > 0)
//                {
//                    funPrintBlankSpace(clsGlobalVarClass.gClientAddress2, BillOut);
//                    BillOut.write(clsGlobalVarClass.gClientAddress2.toUpperCase());
//                    BillOut.newLine();
//                }
//                if (clsGlobalVarClass.gClientAddress3.trim().length() > 0)
//                {
//                    funPrintBlankSpace(clsGlobalVarClass.gClientAddress3, BillOut);
//                    BillOut.write(clsGlobalVarClass.gClientAddress3.toUpperCase());
//                    BillOut.newLine();
//                }
//                if (clsGlobalVarClass.gCityName.trim().length() > 0)
//                {
//                    funPrintBlankSpace(clsGlobalVarClass.gCityName, BillOut);
//                    BillOut.write(clsGlobalVarClass.gCityName.toUpperCase());
//                    BillOut.newLine();
//                }
//            }
//            else
//            {
//                funPrintBlankSpace(clsGlobalVarClass.gClientName, BillOut);
//                if (clsGlobalVarClass.gClientCode.equals("124.001"))
//                {
//                    BillOut.write(clsGlobalVarClass.gClientName);
//                }
//                else
//                {
//                    BillOut.write(clsGlobalVarClass.gClientName.toUpperCase());
//                }
//                BillOut.newLine();
//                funPrintBlankSpace(clsGlobalVarClass.gClientAddress1, BillOut);
//                BillOut.write(clsGlobalVarClass.gClientAddress1.toUpperCase());
//                BillOut.newLine();
//                if (clsGlobalVarClass.gClientAddress2.trim().length() > 0)
//                {
//                    funPrintBlankSpace(clsGlobalVarClass.gClientAddress2, BillOut);
//                    BillOut.write(clsGlobalVarClass.gClientAddress2.toUpperCase());
//                    BillOut.newLine();
//                }
//                if (clsGlobalVarClass.gClientAddress3.trim().length() > 0)
//                {
//                    funPrintBlankSpace(clsGlobalVarClass.gClientAddress3, BillOut);
//                    BillOut.write(clsGlobalVarClass.gClientAddress3.toUpperCase());
//                    BillOut.newLine();
//                }
//                if (clsGlobalVarClass.gCityName.trim().length() > 0)
//                {
//                    funPrintBlankSpace(clsGlobalVarClass.gCityName, BillOut);
//                    BillOut.write(clsGlobalVarClass.gCityName.toUpperCase());
//                    BillOut.newLine();
//                }
//            }
//            BillOut.write("  TEL NO.   :" + " ");
//            BillOut.write(String.valueOf(clsGlobalVarClass.gClientTelNo));
//            BillOut.newLine();
//            BillOut.write("  EMAIL ID  :" + " ");
//            BillOut.write(clsGlobalVarClass.gClientEmail);
//            BillOut.newLine();
//            tblName = rs_BillHD.getString(18);
//            if (tblName.length() > 0)
//            {
//                if (clsGlobalVarClass.gClientCode.equalsIgnoreCase("136.001"))//KINKI
//                {
//                    BillOut.write("  TABLE No   :");
//                }
//                else
//                {
//                    BillOut.write("  TABLE NAME:" + "  ");
//                }
//                BillOut.write(tblName);
//                BillOut.newLine();
//            }
//            waiterName = rs_BillHD.getString(19);
//            if (waiterName.trim().length() > 0)
//            {
//                BillOut.write("  STEWARD   :" + "  ");
//                BillOut.write(waiterName);
//                BillOut.newLine();
//            }
//            BillOut.write(Linefor5);
//            BillOut.newLine();
//            BillOut.write("  POS         : ");
//            BillOut.write(rs_BillHD.getString(16));
//            BillOut.newLine();
//            BillOut.write("  BILL NO.    : ");
//            BillOut.write(billNo);
//            BillOut.newLine();
//            BillOut.write("  PAX NO.     : ");
//            BillOut.write(rs_BillHD.getString(17));
//            BillOut.newLine();
//            if (clsGlobalVarClass.gPrintTimeOnBillYN)
//            {
//                BillOut.write("  DATE & TIME : ");
//                SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy hh:mm a ");
//                BillOut.write(ft.format(rs_BillHD.getObject(3)));
//                BillOut.newLine();
//
//            }
//            else
//            {
//                BillOut.write("  DATE        : ");
//                SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy");
//                BillOut.write(ft.format(rs_BillHD.getObject(3)));
//                BillOut.newLine();
//            }
//            if (rs_BillHD.getString(11).trim().length() > 0 && !flgComplimentaryBill)
//            {
//                BillOut.write("  Remarks     : ");
//                BillOut.write(rs_BillHD.getString(11));
//                BillOut.newLine();
//            }
//            subTotal = rs_BillHD.getString(6);
//            grandTotal = rs_BillHD.getString(8);
//            user = rs_BillHD.getString(12);
//            deliveryCharge = rs_BillHD.getString(13);
//            advAmount = rs_BillHD.getString(14);
//            //print card available balance
//            String isSttled = "select a.strBillNo from " + billSettlementdtl + " a," + billhd + " b "
//                    + "where a.strBillNo=b.strBillNo "
//                    + "and a.strClientCode=b.strClientCode "
//                    + "and a.strBillNo='" + billNo + "' "
//                    + "and b.strPOSCode='" + POSCode + "' "
//                    + "and date(a.dteBillDate)=date(b.dteBillDate) ";
//            ResultSet rsIsSettled = clsGlobalVarClass.dbMysql.executeResultSet(isSttled);
//            if (rsIsSettled.next())
//            {
//                rsIsSettled.close();
//                String availBal = "select a.strCardNo,(b.dblRedeemAmt)"
//                        + "from " + billhd + " a inner join tbldebitcardmaster b on a.strCardNo=b.strCardNo "
//                        + "where a.strBillNo='" + billNo + "' "
//                        + "and a.strPOSCode='" + POSCode + "' "
//                        + "and date(a.dteBillDate)='" + billDate + "' ";
//                ResultSet rsAvailBal = clsGlobalVarClass.dbMysql.executeResultSet(availBal);
//                if (rsAvailBal.next())
//                {
//                    BillOut.write("  Available Balance(" + rsAvailBal.getString(1) + "):" + rsAvailBal.getString(2));
//                    BillOut.newLine();
//                }
//            }
//            else
//            {
//                String availBal = "select a.strCardNo,(b.dblRedeemAmt-a.dblGrandTotal)"
//                        + "from " + billhd + " a inner join tbldebitcardmaster b on a.strCardNo=b.strCardNo "
//                        + "where a.strBillNo='" + billNo + "' "
//                        + "and a.strPOSCode='" + POSCode + "' "
//                        + "and date(a.dteBillDate)='" + billDate + "' ";
//                ResultSet rsAvailBal = clsGlobalVarClass.dbMysql.executeResultSet(availBal);
//                if (rsAvailBal.next())
//                {
//                    BillOut.write("  Available Balance(" + rsAvailBal.getString(1) + "):" + rsAvailBal.getString(2));
//                    BillOut.newLine();
//                }
//            }
//            //print card available balance
//            if (transType.equals("Void"))
//            {
//                BillOut.write("  Reason      :" + " " + rs_BillHD.getString(22));//voided reason
//                BillOut.newLine();
//            }
//            else if (flgComplimentaryBill)
//            {
//
//                BillOut.write("  Reason      :" + " " + rs_BillHD.getString(23));
//                BillOut.newLine();
//                BillOut.write("  Remark      :" + " " + rs_BillHD.getString(11));
//                BillOut.newLine();
//            }
//            if (clsGlobalVarClass.gCMSIntegrationYN)
//            {
//                BillOut.write("  Member Code : ");
//                BillOut.write(rs_BillHD.getString(7));
//                BillOut.newLine();
//                BillOut.write("  Member Name : ");
//                objUtility2.funWriteToTextMemberNameForFormat5(BillOut, rs_BillHD.getString(24), "Format5");
//                BillOut.newLine();
//                BillOut.write(Linefor5);
//            }
//            if (rs_BillHD.getString(25) != null && rs_BillHD.getString(25).length() > 0)
//            {
//                if (rs_BillHD.getString(26).length() > 0 || rs_BillHD.getString(27).length() > 0 || rs_BillHD.getString(28).length() > 0)
//                {
//                    BillOut.newLine();
//                    funPrintBlankSpace("ORDER DETAIL", BillOut);
//                    BillOut.write("ORDER DETAIL");
//                    BillOut.newLine();
//                    BillOut.write(Linefor5);
//                    BillOut.newLine();
//                }
//                StringBuilder strValue = new StringBuilder();
//                strValue.setLength(0);
//                if (rs_BillHD.getString(26).length() > 0)
//                {
//                    strValue.append(rs_BillHD.getString(26));
//                }
//                else
//                {
//                    strValue.append("");
//                }
//                int strlenMsg = strValue.length();
//                if (strlenMsg > 0)
//                {
//                    String msg1 = "";
//                    if (strlenMsg < 27)
//                    {
//                        msg1 = strValue.substring(0, strlenMsg);
//                        BillOut.write("  MESSAGE     :" + msg1);
//                        BillOut.newLine();
//                    }
//                    else
//                    {
//                        msg1 = strValue.substring(0, 27);
//                        BillOut.write("  MESSAGE     :" + msg1);;
//                        BillOut.newLine();
//                    }
//                    for (int i = 27; i <= strlenMsg; i++)
//                    {
//                        int endmsg = 0;
//                        endmsg = i + 27;
//                        if (strlenMsg > endmsg)
//                        {
//                            msg1 = strValue.substring(i, endmsg);
//                            i = endmsg;
//                            BillOut.write("               " + msg1);
//                            BillOut.newLine();
//                        }
//                        else
//                        {
//                            msg1 = strValue.substring(i, strlenMsg);
//                            BillOut.write("               " + msg1);
//                            BillOut.newLine();
//                            i = strlenMsg + 1;
//                        }
//                    }
//                }
//                strValue.setLength(0);
//                if (rs_BillHD.getString(27).length() > 0)//shape
//                {
//                    strValue.append(rs_BillHD.getString(27));
//                }
//                else
//                {
//                    strValue.append("");
//                }
//                int strlenShape = strValue.length();
//                if (strlenShape > 0)
//                {
//                    String shape1 = "";
//                    if (strlenShape < 27)
//                    {
//                        shape1 = strValue.substring(0, strlenShape);
//                        BillOut.write("  SHAPE       :" + shape1);
//                        BillOut.newLine();
//                    }
//                    else
//                    {
//                        shape1 = strValue.substring(0, 27);
//                        BillOut.write("  SHAPE       :" + shape1);
//                        BillOut.newLine();
//                    }
//                    for (int j = 27; j <= strlenShape; j++)
//                    {
//                        int endShape = 0;
//                        endShape = j + 27;
//                        if (strlenShape > endShape)
//                        {
//                            shape1 = strValue.substring(j, endShape);
//                            j = endShape;
//                            BillOut.write("               " + shape1);
//                            BillOut.newLine();
//                        }
//                        else
//                        {
//                            shape1 = strValue.substring(j, strlenShape);
//                            BillOut.write("               " + shape1);
//                            BillOut.newLine();
//                            j = strlenShape + 1;
//                        }
//                    }
//                }
//
//                strValue.setLength(0);
//                if (rs_BillHD.getString(28).length() > 0)//note
//                {
//                    strValue.append(rs_BillHD.getString(28));
//                }
//                else
//                {
//                    strValue.append("");
//                }
//                int strlenNote = strValue.length();
//                if (strlenNote > 0)
//                {
//                    String note1 = "";
//                    if (strlenNote < 27)
//                    {
//                        note1 = strValue.substring(0, strlenNote);
//                        BillOut.write("  NOTE        :" + note1);
//                        BillOut.newLine();
//                    }
//                    else
//                    {
//                        note1 = strValue.substring(0, 27);
//                        BillOut.write("  NOTE        :" + note1);
//                        BillOut.newLine();
//                    }
//                    for (int i = 27; i <= strlenNote; i++)
//                    {
//                        int endNote = 0;
//                        endNote = i + 27;
//                        if (strlenNote > endNote)
//                        {
//                            note1 = strValue.substring(i, endNote);
//                            i = endNote;
//                            BillOut.write("               " + note1);
//                            BillOut.newLine();
//                        }
//                        else
//                        {
//                            note1 = strValue.substring(i, strlenNote);
//                            BillOut.write("               " + note1);
//                            BillOut.newLine();
//                            i = strlenNote + 1;
//                        }
//                    }
//                }
//                if (rs_BillHD.getString(26).length() > 0 || rs_BillHD.getString(27).length() > 0 || rs_BillHD.getString(28).length() > 0)
//                {
//
//                    BillOut.write(Linefor5);
//                    BillOut.newLine();
//                }
//            }
//
//            Map<String, clsBillDtl> hmComplBillItemDtl = objUtility.funGetComplimetaryItems(billNo, billComplDtl, billhd, POSCode, billDate);
//            StringBuilder sbZeroAmtItems = new StringBuilder();
//
//            BillOut.write(Linefor5);
//            BillOut.newLine();
//            BillOut.write("     QTY ITEM NAME                  AMT");
//            BillOut.newLine();
//            BillOut.write(Linefor5);
//            BillOut.newLine();
//            String SQL_BillDtl = "select sum(a.dblQuantity),left(a.strItemName,22) as ItemLine1"
//                    + " ,MID(a.strItemName,23,LENGTH(a.strItemName)) as ItemLine2"
//                    + " ,sum(a.dblAmount),a.strItemCode,a.strKOTNo "
//                    + " from " + billdtl + " a," + billhd + " b,tblitemmaster c "
//                    + " where a.strBillNo=b.strBillNo and a.strClientCode=b.strClientCode "
//                    + " and a.strItemCode=c.strItemCode and date(a.dteBillDate)=date(b.dteBillDate) "
//                    + " and a.strBillNo=? "
//                    + " and b.strPOSCode=?  "
//                    + " and date(b.dteBillDate)=? ";
//            if (!clsGlobalVarClass.gPrintTDHItemsInBill)
//            {
//                SQL_BillDtl += "and a.tdhYN='N' ";
//            }
//            if (!clsGlobalVarClass.gPrintOpenItemsOnBill)
//            {
//                SQL_BillDtl += "and c.strOpenItem='N' ";
//            }
//            SQL_BillDtl += " group by a.strItemCode ";
//            pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(SQL_BillDtl);
//            pst.setString(1, billNo);
//            pst.setString(2, POSCode);
//            pst.setString(3, billDate);
//            ResultSet rs_BillDtl = pst.executeQuery();
//            while (rs_BillDtl.next())
//            {
//                double saleQty = rs_BillDtl.getDouble(1);//qty
//                double itemAmount = rs_BillDtl.getDouble(4);//amount
//
//                String sqlPromoBills = "select dblQuantity from " + billPromoDtl + " "
//                        + " where strBillNo='" + billNo + "' "
//                        + " and strItemCode='" + rs_BillDtl.getString(5) + "' "
//                        + " and strPromoType='ItemWise' "
//                        + " and date(dteBillDate)='" + billDate + "' ";
//                ResultSet rsPromoItems = clsGlobalVarClass.dbMysql.executeResultSet(sqlPromoBills);
//                if (rsPromoItems.next())
//                {
//                    saleQty -= rsPromoItems.getDouble(1);
//                }
//                rsPromoItems.close();
//                String qty = String.valueOf(saleQty);
//                if (qty.contains("."))
//                {
//                    String decVal = qty.substring(qty.length() - 2, qty.length());
//                    if (Double.parseDouble(decVal) == 0)
//                    {
//                        qty = qty.substring(0, qty.length() - 2);
//                    }
//                }
//                if (saleQty > 0)
//                {
//                    if (hmComplBillItemDtl.containsKey(rs_BillDtl.getString(5)))
//                    {
////                        double chargedQty = hmComplBillItemDtl.get(rs_BillDtl.getString(5)).getDblQuantity() - saleQty;
////                        if (chargedQty > 0)
////                        {
////                            qty = String.valueOf(chargedQty);
////                        }
//                        if (itemAmount == 0)
//                        {
//                            sbZeroAmtItems.append(",'" + rs_BillDtl.getString(5) + "'");
//                        }
//                    }
//
//                    /**
//                     * merging modifier amount to item
//                     */
//                    String sqlModifier = "select count(*) "
//                            + "from " + billModifierdtl + " "
//                            + "where strBillNo=? "
//                            + "and left(strItemCode,7)=? "
//                            + "and date(dteBillDate)=? ";
//                    if (!clsGlobalVarClass.gPrintZeroAmtModifierOnBill)
//                    {
//                        sqlModifier += " and  dblAmount !=0.00 ";
//                    }
//                    pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlModifier);
//                    pst.setString(1, billNo);
//                    pst.setString(2, rs_BillDtl.getString(5));
//                    pst.setString(3, billDate);
//                    ResultSet rs_count = pst.executeQuery();
//                    rs_count.next();
//                    int cntRecord = rs_count.getInt(1);
//                    rs_count.close();
//                    if (cntRecord > 0)
//                    {
//                        sqlModifier = "select strModifierName,dblQuantity,dblAmount "
//                                + " from " + billModifierdtl + " "
//                                + " where strBillNo=? "
//                                + " and left(strItemCode,7)=? "
//                                + " and date(dteBillDate)=? ";
//                        if (!clsGlobalVarClass.gPrintZeroAmtModifierOnBill)
//                        {
//                            sqlModifier += " and  dblAmount !=0.00 ";
//                        }
//                        pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlModifier);
//                        pst.setString(1, billNo);
//                        pst.setString(2, rs_BillDtl.getString(5));
//                        pst.setString(3, billDate);
//                        ResultSet rs_modifierRecord = pst.executeQuery();
//                        while (rs_modifierRecord.next())
//                        {
//                            /**
//                             * merged modifier amt
//                             */
//                            itemAmount += rs_modifierRecord.getDouble(3);
//
////                            if (flgComplimentaryBill)
////                            {
////                                objUtility2.funWriteToTextformat5(BillOut, "", rs_modifierRecord.getString(1).toUpperCase(), "0.00", "Format5");
////                                BillOut.newLine();
////                            }
////                            else
////                            {
////                                objUtility2.funWriteToTextformat5(BillOut, "", rs_modifierRecord.getString(1).toUpperCase(), rs_modifierRecord.getString(3), "Format5");
////                                BillOut.newLine();
////                            }
//                        }
//                        rs_modifierRecord.close();
//                    }
//
//                    objUtility2.funPrintContentWithSpace("Right", qty, 8, BillOut);//Qty Print
//                    BillOut.write(" ");
//                    objUtility2.funPrintContentWithSpace("Left", rs_BillDtl.getString(2), 22, BillOut);//Item Name
//                    if (flgComplimentaryBill)
//                    {
//                        objUtility2.funPrintContentWithSpace("Right", "0.00", 9, BillOut);//Amount
//                    }
//                    else
//                    {
//                        objUtility2.funPrintContentWithSpace("Right", decimalFormatFor2DecPoint.format(itemAmount), 9, BillOut);//Amount
//                    }
//                    BillOut.newLine();
//
//                    if (rs_BillDtl.getString(3).trim().length() > 0)
//                    {
//                        String line = rs_BillDtl.getString(3);
//                        if (line.length() > 22)
//                        {
//                            BillOut.write("         " + line.substring(0, 22));
//                            BillOut.newLine();
//
//                            BillOut.write("         " + line.substring(22, line.length()));
//                            BillOut.newLine();
//                        }
//                        else
//                        {
//                            BillOut.write("         " + line);
//                            BillOut.newLine();
//                        }
//                    }
//
//                    sql = "select b.strItemCode,b.dblWeight "
//                            + " from " + billhd + " a," + advBookBillDtl + " b "
//                            + " where a.strAdvBookingNo=b.strAdvBookingNo "
//                            + " and a.strClientCode=b.strClientCode "
//                            + " and a.strBillNo='" + billNo + "' "
//                            + " and b.strItemCode='" + rs_BillDtl.getString(5) + "' "
//                            + " and a.strPOSCode='" + POSCode + "' "
//                            + " and date(a.dteBillDate)='" + billDate + "' ";
//                    ResultSet rsWeight = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//                    while (rsWeight.next())
//                    {
//                        BillOut.write("     Weight");
//                        BillOut.write("     " + rsWeight.getDouble(2));
//                        BillOut.newLine();
//                    }
//                    rsWeight.close();
//                    sql = "select c.strCharName,b.strCharValues "
//                            + " from " + billhd + " a," + advBookBillCharDtl + " b,tblcharactersticsmaster c "
//                            + " where a.strAdvBookingNo=b.strAdvBookingNo "
//                            + " and b.strCharCode=c.strCharCode "
//                            + " and a.strBillNo='" + billNo + "' "
//                            + " and b.strItemCode='" + rs_BillDtl.getString(5) + "' "
//                            + " and a.strPOSCode='" + POSCode + "' "
//                            + " and date(a.dteBillDate)='" + billDate + "' "
//                            + " and a.strClientCode=b.strClientCode ";
//                    ResultSet rsCharDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//                    while (rsCharDtl.next())
//                    {
//                        String charName = objUtility.funPrintTextWithAlignment(rsCharDtl.getString(1), 12, "Left");
//                        BillOut.write("     " + charName);
//                        String charVal = objUtility.funPrintTextWithAlignment(rsCharDtl.getString(2), 28, "Left");
//                        BillOut.write("     " + charVal);
//                        BillOut.newLine();
//                    }
//                    rsCharDtl.close();
//                }
//            }
//            rs_BillDtl.close();
//
//            funPrintComplimentaryItemsInBill(billNo, BillOut, 4, POSCode, billDate, sbZeroAmtItems);
//
//            funPrintPromoItemsInBill(billNo, BillOut, 4);  // Print Promotion Items in Bill for this billno.
//            BillOut.write(Linefor5);
//            BillOut.newLine();
//            if (clsGlobalVarClass.gPointsOnBillPrint)
//            {
//                String sqlCRMPoints = "select b.dblPoints from " + billhd + " a, tblcrmpoints b "
//                        + " where a.strBillNo=b.strBillNo "
//                        + " and a.strClientCode=b.strClientCode "
//                        + " and a.strBillNo='" + billNo + "' "
//                        + " and a.strPOSCode='" + POSCode + "' "
//                        + " and date(a.dteBillDate)='" + billDate + "' ";
//                ResultSet rsCRMPoints = clsGlobalVarClass.dbMysql.executeResultSet(sqlCRMPoints);
//                if (rsCRMPoints.next())
//                {
//                    funWriteTotal("POINTS ", rsCRMPoints.getString(1), BillOut, "Format5");
//                }
//                rsCRMPoints.close();
//                BillOut.newLine();
//            }
//            if (flgComplimentaryBill)
//            {
//                funWriteTotal("SUB TOTAL", "0.00", BillOut, "Format5");
//                BillOut.newLine();
//            }
//            else
//            {
//                funWriteTotal("SUB TOTAL", subTotal, BillOut, "Format5");
//                BillOut.newLine();
//            }
//            sql = "select a.dblDiscPer,a.dblDiscAmt,a.strDiscOnType,a.strDiscOnValue,b.strReasonName,a.strDiscRemarks "
//                    + " from " + billDscFrom + " a ,tblreasonmaster b," + billhd + " c "
//                    + " where  a.strDiscReasonCode=b.strReasonCode "
//                    + " and a.strBillNo=c.strBillNo "
//                    + " and a.strClientCode=c.strClientCode "
//                    + " and date(a.dteBillDate)=date(c.dteBillDate) "
//                    + " and a.strBillNo='" + billNo + "' "
//                    + " and c.strPOSCode='" + POSCode + "' "
//                    + " and date(c.dteBillDate)='" + billDate + "' ";
//            ResultSet rsDisc = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//            boolean flag = true;
//            while (rsDisc.next())
//            {
//                if (flag)
//                {
//                    flag = false;
//                    BillOut.write("  DISCOUNT");
//                    BillOut.newLine();
//                }
//                double dbl = Double.parseDouble(rsDisc.getString("dblDiscPer"));
//                String dbl2 = decimalFormat.format(Math.rint(dbl));
////                String discText = String.format("%.1f", dbl) + "%" + " On " + rsDisc.getString("strDiscOnValue") + "";
//                String discText = dbl2 + "%" + " On " + rsDisc.getString("strDiscOnValue") + "";
//                if (discText.length() > 30)
//                {
//                    discText = discText.substring(0, 30);
//                }
//                else
//                {
//                    discText = String.format("%-30s", discText);
//                }
//                BillOut.write("  " + discText);
//                String discountOnItem = objUtility.funPrintTextWithAlignment(String.valueOf(decimalFormatFor2DecPoint.format(Math.rint(rsDisc.getDouble("dblDiscAmt")))), 8, "Right");
//                BillOut.write(discountOnItem);
//                BillOut.newLine();
//                BillOut.write("  Reason  : ");
//                String discReason = objUtility.funPrintTextWithAlignment(rsDisc.getString(5), 20, "Left");
//                BillOut.write(discReason);
//                BillOut.newLine();
//                BillOut.write("  Remarks : ");
//                String discRemarks = objUtility.funPrintTextWithAlignment(rsDisc.getString(6), 20, "Left");
//                BillOut.write(discRemarks);
//                BillOut.newLine();
//
//            }
//            String sql_Tax = "select b.strTaxDesc,sum(a.dblTaxAmount) "
//                    + " from " + billtaxdtl + " a,tbltaxhd b," + billhd + " c "
//                    + " where a.strBillNo='" + billNo + "' "
//                    + " and a.strTaxCode=b.strTaxCode "
//                    + " and a.strBillNo=c.strBillNo "
//                    + " and a.strClientCode=c.strClientCode "
//                    + " and date(a.dteBillDate)=date(c.dteBillDate) "
//                    + " and c.strPOSCode='" + POSCode + "' "
//                    + " and b.strTaxCalculation='Forward' "
//                    + " and date(c.dteBillDate)='" + billDate + "' "
//                    + " group by a.strTaxCode";
//            ResultSet rsTax = clsGlobalVarClass.dbMysql.executeResultSet(sql_Tax);
//            while (rsTax.next())
//            {
//                if (flgComplimentaryBill)
//                {
//                    funWriteTotal(rsTax.getString(1), "0.00", BillOut, "Format5");
//                    BillOut.newLine();
//                }
//                else
//                {
//                    funWriteTotal(rsTax.getString(1), rsTax.getString(2), BillOut, "Format5");
//                    BillOut.newLine();
//                }
//            }
//            if (deliveryCharge != null && deliveryCharge.trim().length() > 0 && !"0.00".equalsIgnoreCase(deliveryCharge))
//            {
//                funWriteTotal("DELV. CHARGE", deliveryCharge, BillOut, "Format5");
//                BillOut.newLine();
//            }
//            if (advAmount.trim().length() > 0 && !"0.00".equalsIgnoreCase(advAmount))
//            {
//                funWriteTotal("ADVANCE", advAmount, BillOut, "Format5");
//                BillOut.newLine();
//            }
//            BillOut.write(Linefor5);
//            BillOut.newLine();
//            if (flgComplimentaryBill)
//            {
//                funWriteTotal("TOTAL(ROUNDED)", "0.00", BillOut, "Format5");
//                BillOut.newLine();
//                BillOut.write(Linefor5);
//            }
//            else
//            {
//                funWriteTotal("TOTAL(ROUNDED)", grandTotal, BillOut, "Format5");
//                BillOut.newLine();
//                BillOut.write(Linefor5);
//            }
//
//            //print Grand total of other bill nos from bill series
//            if (clsGlobalVarClass.gEnableBillSeries)
//            {
//                String sqlPrintGT = "select a.strPrintGTOfOtherBills,b.strDtlBillNos,b.dblGrandTotal "
//                        + "from tblbillseries a,tblbillseriesbilldtl b "
//                        + "where (a.strPOSCode=b.strPOSCode or a.strPOSCode='All') "
//                        + "and a.strBillSeries=b.strBillSeries "
//                        + "and b.strHdBillNo='" + billNo + "' "
//                        + "and b.strPOSCode='" + POSCode + "' "
//                        + "and date(b.dteBillDate)='" + billDate + "' ";
//                ResultSet rsPrintGTYN = clsGlobalVarClass.dbMysql.executeResultSet(sqlPrintGT);
//                double dblOtherBillsGT = 0.00;
//                if (rsPrintGTYN.next())
//                {
//                    if (rsPrintGTYN.getString(1).equalsIgnoreCase("Y"))
//                    {
//                        String billSeriesDtlBillNos = rsPrintGTYN.getString(2);
//                        String[] dtlBillSeriesBillNo = billSeriesDtlBillNos.split(",");
//                        dblOtherBillsGT += rsPrintGTYN.getDouble(3);
//                        if (dtlBillSeriesBillNo.length > 0)
//                        {
//                            for (int i = 0; i < dtlBillSeriesBillNo.length; i++)
//                            {
//                                sqlPrintGT = "select a.strHdBillNo,a.dblGrandTotal "
//                                        + "from tblbillseriesbilldtl a "
//                                        + "where a.strHdBillNo='" + dtlBillSeriesBillNo[i] + "' "
//                                        + "and a.strPOSCode='" + POSCode + "' "
//                                        + "and date(a.dteBillDate)='" + billDate + "' ";
//                                ResultSet rsPrintGT = clsGlobalVarClass.dbMysql.executeResultSet(sqlPrintGT);
//                                if (rsPrintGT.next())
//                                {
//                                    BillOut.newLine();
//                                    funWriteTotal(dtlBillSeriesBillNo[i] + " TOTAL(ROUNDED)", rsPrintGT.getString(2), BillOut, "Format5");
//                                    dblOtherBillsGT += rsPrintGT.getDouble(2);
//                                    BillOut.newLine();
//                                }
//                            }
//                            BillOut.newLine();
//                            BillOut.write(Linefor5);
//                            BillOut.newLine();
//                            funWriteTotal("GRAND TOTAL(ROUNDED)", String.valueOf(dblOtherBillsGT), BillOut, "Format5");
//                            BillOut.newLine();
//                            BillOut.write(Linefor5);
//                            BillOut.newLine();
//                        }
//                    }
//                }
//            }
//
//            //settlement breakup part
//            String sqlSettlementBreakup = "select a.dblSettlementAmt, b.strSettelmentDesc, b.strSettelmentType "
//                    + " from " + billSettlementdtl + " a ,tblsettelmenthd b," + billhd + " c "
//                    + " where a.strBillNo=? "
//                    + " and a.strBillNo=c.strBillNo "
//                    + " and a.strClientCode=c.strClientCode "
//                    + " and a.strSettlementCode=b.strSettelmentCode "
//                    + " AND date(a.dteBillDate)=date(c.dteBillDate) "
//                    + " and c.strPOSCode=? "
//                    + " and date(c.dteBillDate)=? ";
//            pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlSettlementBreakup);
//            pst.setString(1, billNo);
//            pst.setString(2, POSCode);
//            pst.setString(3, billDate);
//            boolean flgSettlement = false;
//            boolean creditSettlement = false;
//            ResultSet rsBillSettlement = pst.executeQuery();
//            while (rsBillSettlement.next())
//            {
//                if (flgComplimentaryBill)
//                {
//                    BillOut.newLine();
//                    funWriteTotal(rsBillSettlement.getString(2), "0.00", BillOut, "Format5");
//                }
//                else
//                {
//                    BillOut.newLine();
//                    funWriteTotal(rsBillSettlement.getString(2), rsBillSettlement.getString(1), BillOut, "Format5");
//                }
//                flgSettlement = true;
//                if (rsBillSettlement.getString(3).equals("Credit"))
//                {
//                    creditSettlement = true;
//                }
//            }
//            rsBillSettlement.close();
//
//            if (flgSettlement)
//            {
//                BillOut.newLine();
//                if (creditSettlement)
//                {
//                    funWriteTotal("Credit Remarks ", rs_BillHD.getString(11), BillOut, "Format5");
//                    BillOut.newLine();
//                    String custName = rs_BillHD.getString(24);
//                    if (!custName.isEmpty())
//                    {
//                        funWriteTotal("Customer " + custName, "", BillOut, "Format5");
//                    }
//                    BillOut.newLine();
//                    BillOut.write(Linefor5);
//                }
//            }
//
//            String sqlTenderAmt = "select sum(a.dblPaidAmt),sum(a.dblSettlementAmt),(sum(a.dblPaidAmt)-sum(a.dblSettlementAmt)) RefundAmt "
//                    + " from " + billSettlementdtl + " a," + billhd + " b "
//                    + " where a.strBillNo=b.strBillNo "
//                    + " and a.strClientCode=b.strClientCode "
//                    + " AND date(a.dteBillDate)=date(b.dteBillDate) "
//                    + " and b.strBillNo='" + billNo + "' "
//                    + " and b.strPOSCode='" + POSCode + "' "
//                    + " and date(b.dteBillDate)='" + billDate + "' "
//                    + " group by a.strBillNo";
//            ResultSet rsTenderAmt = clsGlobalVarClass.dbMysql.executeResultSet(sqlTenderAmt);
//            if (rsTenderAmt.next())
//            {
//                BillOut.newLine();
//                if (flgComplimentaryBill)
//                {
//                    funWriteTotal("PAID AMT", "0.00", BillOut, "Format5");
//                    BillOut.newLine();
//                }
//                else
//                {
//                    funWriteTotal("PAID AMT", rsTenderAmt.getString(1), BillOut, "Format5");
//                    BillOut.newLine();
//                    if (rsTenderAmt.getDouble(3) > 0)
//                    {
//                        funWriteTotal("REFUND AMT", rsTenderAmt.getString(3), BillOut, "Format5");
//                        BillOut.newLine();
//                    }
//                }
//                BillOut.write(Linefor5);
//            }
//            rsTenderAmt.close();
//
//            if (rs_BillHD.getDouble(29) > 0)
//            {
//                BillOut.newLine();
//                funWriteTotal("TIP AMT", rs_BillHD.getString(29), BillOut, "Format5");
//                BillOut.newLine();
//            }
//            if (flag_isHomeDelvBill)
//            {
//                BillOut.newLine();
//                String sql_count = "select count(*) from tblhomedelivery where strCustomerCode=?";
//                pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sql_count);
//                pst.setString(1, customerCode);
//                ResultSet rs_Count = pst.executeQuery();
//                rs_Count.next();
//                BillOut.write("  CUSTOMER COUNT : " + rs_Count.getString(1));
//                rs_Count.close();
//                BillOut.newLine();
//                BillOut.write(Linefor5);
//            }
//            BillOut.newLine();
//
//            /**
//             * print Tax Nos
//             */
//            funPrintServiceVatNo(BillOut, 4, billNo, billDate, billtaxdtl);
//
//            if (clsGlobalVarClass.gEnableBillSeries)
//            {
//                sql = "select b.strPrintInclusiveOfTaxOnBill "
//                        + " from tblbillseriesbilldtl a,tblbillseries b "
//                        + " where a.strBillSeries=b.strBillSeries "
//                        + " and a.strHdBillNo='" + billNo + "' "
//                        + " and a.strClientCode=b.strClientCode "
//                        + " and date(a.dteBillDate)='" + billDate + "' ";
//                ResultSet rsBillSeries = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//                if (rsBillSeries.next())
//                {
//                    if (rsBillSeries.getString(1).equals("Y"))
//                    {
//                        BillOut.write(Line);
//                        BillOut.newLine();
//                        funPrintBlankSpace("(INCLUSIVE OF ALL TAXES)", BillOut);
//                        BillOut.write("(INCLUSIVE OF ALL TAXES)");
//                        BillOut.newLine();
//                    }
//                }
//                rsBillSeries.close();
//            }
//            else
//            {
//                if (clsGlobalVarClass.gPrintInclusiveOfAllTaxes.equalsIgnoreCase("Y"))
//                {
//                    BillOut.write(Line);
//                    BillOut.newLine();
//                    funPrintBlankSpace("(INCLUSIVE OF ALL TAXES)", BillOut);
//                    BillOut.write("(INCLUSIVE OF ALL TAXES)");
//                    BillOut.newLine();
//                }
//            }
//
//            int num = clsGlobalVarClass.gBillFooter.trim().length() / 30;
//            int num1 = clsGlobalVarClass.gBillFooter.trim().length() % 30;
//            int cnt1 = 0;
//            for (int cnt = 0; cnt < num; cnt++)
//            {
//                String footer = clsGlobalVarClass.gBillFooter.trim().substring(cnt1, (cnt1 + 30));
//                footer = footer.replaceAll("\n", "");
//                BillOut.write("     " + footer.trim());
//                BillOut.newLine();
//                cnt1 += 30;
//            }
//            BillOut.write("     " + clsGlobalVarClass.gBillFooter.trim().substring(cnt1, (cnt1 + num1)).trim());
//            BillOut.newLine();
//            funPrintBlankSpace(user, BillOut);
//            BillOut.write(user);
//            BillOut.newLine();
//            BillOut.newLine();
//            BillOut.newLine();
//            BillOut.newLine();
//            BillOut.newLine();
//
//            if (!clsGlobalVarClass.gOpenCashDrawerAfterBillPrintYN)
//            {
//                if ("linux".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
//                {
//                    BillOut.write("V");//Linux
//                }
//                else if ("windows".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
//                {
//                    if ("Inbuild".equalsIgnoreCase(clsPosConfigFile.gPrinterType))
//                    {
//                        BillOut.write("V");
//                    }
//                    else
//                    {
//                        BillOut.write("m");//windows
//                    }
//                }
//            }
//            rs_BillHD.close();
//            BillOut.close();
//            fstream_bill.close();
//            pst.close();
//
//            if (formName.equalsIgnoreCase("sales report"))
//            {
//                funShowTextFile(Text_Bill, formName, clsGlobalVarClass.gBillPrintPrinterPort);
//            }
//            else
//            {
//                if (clsGlobalVarClass.gShowBill)
//                {
//                    funShowTextFile(Text_Bill, formName, clsGlobalVarClass.gBillPrintPrinterPort);
//                }
//            }
//
//            if (!formName.equalsIgnoreCase("sales report"))
//            {
//                if (transType.equalsIgnoreCase("void"))
//                {
//                    if (clsGlobalVarClass.gPrintOnVoidBill)
//                    {
//                        if (!viewORprint.equalsIgnoreCase("view"))
//                        {
//                            funPrintToPrinter(clsGlobalVarClass.gBillPrintPrinterPort, "", "bill", "N", isReprint);
//                        }
//                    }
//                }
//                else
//                {
//                    if (!clsGlobalVarClass.flgReprintView)
//                    {
//                        if (!viewORprint.equalsIgnoreCase("view"))
//                        {
//                            funPrintToPrinter(clsGlobalVarClass.gBillPrintPrinterPort, "", "bill", "N", isReprint);
//                        }
//                    }
//                    else
//                    {
//                        clsGlobalVarClass.flgReprintView = false;
//                    }
//                }
//            }
//            //if (formName.equalsIgnoreCase("sales report"))
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    private int funPrintComplimentaryItemsInBill(String billNo, BufferedWriter objBillOut, int billPrintSize, String POSCode, String billDate, StringBuilder sbZeroAmtItems) throws Exception
//    {
//        if (sbZeroAmtItems.length() > 0)
//        {
//            sbZeroAmtItems = sbZeroAmtItems.delete(0, 1);
//        }
//        clsUtility objUtility = new clsUtility();
//        String sqlBillComplDtl = "select b.strItemName,b.dblQuantity "
//                + " from tblbillhd a,tblbillcomplementrydtl b "
//                + " where a.strBillNo=b.strBillNo and a.strClientCode=b.strClientCode "
//                + " and date(a.dteBillDate)=date(b.dteBillDate) and a.strBillNo='" + billNo + "' "
//                + " and a.strPOSCode='" + POSCode + "'  and date(a.dteBillDate)='" + billDate + "' "
//                + " and a.strClientCode='" + clsGlobalVarClass.gClientCode + "' ";
//        if (clsGlobalVarClass.gPrintOpenItemsOnBill)
//        {
//            if (sbZeroAmtItems.length() > 0)
//            {
//                sqlBillComplDtl = sqlBillComplDtl + " and b.strItemCode not in (" + sbZeroAmtItems + ") ";
//            }
//        }
//
//        ResultSet rsBillComplItemDtl = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillComplDtl);
//        while (rsBillComplItemDtl.next())
//        {
//            if (billPrintSize == 4)
//            {
//                //funWriteToText(objBillOut, funReduceTo2DecimalPlaces(rsBillPromoItemDtl.getString(2)), rsBillPromoItemDtl.getString(1).toUpperCase(), "0", "Format1");
//                String qty = rsBillComplItemDtl.getString(2);
//                if (qty.contains("."))
//                {
//                    String decVal = qty.substring(qty.length() - 1, qty.length());
//                    if (Double.parseDouble(decVal) == 0)
//                    {
//                        qty = qty.substring(0, qty.length() - 3);
//                    }
//                }
//                objBillOut.write(" ");
//                objBillOut.write(objUtility.funPrintTextWithAlignment(qty, 7, "Right"));
//                objBillOut.write(" ");
//                objBillOut.write(objUtility.funPrintTextWithAlignment(rsBillComplItemDtl.getString(1).toUpperCase(), 20, "Left"));
//                objBillOut.write(objUtility.funPrintTextWithAlignment("0.00", 11, "Right"));
//
//                objBillOut.newLine();
//            }
//            else if (billPrintSize == 2)
//            {
//                if (rsBillComplItemDtl.getString(1).toUpperCase().length() > 20)
//                {
//                    List listTextToPrint = funGetTextWithSpecifiedSize(rsBillComplItemDtl.getString(1).toUpperCase(), 2);
//                    for (int cnt = 0; cnt < listTextToPrint.size(); cnt++)
//                    {
//                        objBillOut.write(objUtility.funPrintTextWithAlignment(listTextToPrint.get(cnt).toString(), 20, "Left"));
//                        objBillOut.newLine();
//                    }
//                }
//                else
//                {
//                    objBillOut.write(objUtility.funPrintTextWithAlignment(rsBillComplItemDtl.getString(1).toUpperCase(), 20, "Left"));
//                    objBillOut.newLine();
//                }
//                //objBillOut.write(new clsUtility().funPrintTextWithAlignment(funReduceTo2DecimalPlaces(rsBillPromoItemDtl.getString(2)), 6, "Right"));
//                objBillOut.write(objUtility.funPrintTextWithAlignment(rsBillComplItemDtl.getString(2), 6, "Right"));
//                objBillOut.write(objUtility.funPrintTextWithAlignment("  ", 7, "Right"));
//                objBillOut.write(objUtility.funPrintTextWithAlignment("0", 7, "Right"));
//                objBillOut.newLine();
//            }
//        }
//        rsBillComplItemDtl.close();
//        objUtility = null;
//        return 1;
//    }
//
//}
