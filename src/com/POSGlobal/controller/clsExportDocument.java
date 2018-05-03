package com.POSGlobal.controller;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import javax.swing.JTable;

public class clsExportDocument
{

    public void funExportToPDF(JTable tblSalesData, JTable tblSalesTotals, String reportName) throws Exception
    {
        try
            {

                int rowSize = tblSalesData.getRowCount();
                int colSize = tblSalesData.getColumnCount();
                FileOutputStream file2 = new FileOutputStream(new File(clsPosConfigFile.exportReportPath+"/" +reportName+".pdf"));
                com.itextpdf.text.Document document = new com.itextpdf.text.Document();
                com.itextpdf.text.pdf.PdfWriter.getInstance(document, file2);
                document.open();//PDF document opened........			       
                
                //Inserting Image in PDF
                com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(System.getProperty("user.dir")+"/ReportImage/"+"imgClientImage.jpg");
                image.scaleAbsolute(75f,75f);//image width,height
                image.setAlignment(Element.ALIGN_CENTER);
                document.add(image);
                //Inserting Image in PDF
                Font headerFontBody = new Font(Font.FontFamily.COURIER,6, Font.BOLD);
                Font dataFontBody = new Font(Font.FontFamily.COURIER,6, Font.NORMAL);
                //Inserting tblHeader in PDF  
//                 com.itextpdf.text.pdf.PdfPTable tblHeader = new com.itextpdf.text.pdf.PdfPTable(colSize);
//                PdfPCell cell = new PdfPCell(new Paragraph(reportName));
//                //cell.setColspan(1);
//                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//                cell.setPadding(10.0f);
//                //cell.setBackgroundColor(new BaseColor(140, 221, 8));
//                tblHeader.addCell(cell);
//                document.add(tblHeader);
                
                document.add(new Paragraph("                "+reportName.toUpperCase()));
                document.add(Chunk.NEWLINE);   //Something like in HTML :-)
                //columns
                com.itextpdf.text.pdf.PdfPTable tblColumns = new com.itextpdf.text.pdf.PdfPTable(colSize);
                
                for (int c = 0; c < colSize; c++)
                {
                   PdfPCell cellDash=new PdfPCell(new Phrase(""));
                   cellDash.setBackgroundColor(new BaseColor(140, 221, 8));
                   tblColumns.addCell(cellDash);
                }
                for (int c = 0; c < colSize; c++)
                {
                    tblColumns.addCell(new Phrase(tblSalesData.getColumnName(c), headerFontBody));
                }
                for (int c = 0; c < colSize; c++)
                {
                    PdfPCell cellDash=new PdfPCell(new Phrase(""));
                    cellDash.setBackgroundColor(new BaseColor(140, 221, 8));
                    tblColumns.addCell(cellDash);
                }
                document.add(tblColumns);
                //columns

                //data
                com.itextpdf.text.pdf.PdfPTable tblData = new com.itextpdf.text.pdf.PdfPTable(colSize);
                for (int r = 0; r < rowSize; r++)
                {
                    for (int c = 0; c < colSize; c++)
                    {
                        String data="";
                        if(null==tblSalesData.getValueAt(r, c))
                        {
                            data="";
                        }
                        else
                        {
                            data=tblSalesData.getValueAt(r, c).toString();
                        }
                        tblData.addCell(new Phrase(data, dataFontBody));
                    }
                }
                for (int c = 0; c < colSize; c++)
                {
                   PdfPCell cellDash=new PdfPCell(new Phrase(""));
                   cellDash.setBackgroundColor(new BaseColor(140, 221, 8));
                   tblData.addCell(cellDash);
                }
                //tblData.setSpacingBefore(30.0f);       // Space Before tblData starts, like margin-top in CSS
                //tblData.setSpacingAfter(30.0f);        // Space After tblData starts, like margin-Bottom in CSS
                document.add(tblData);
                
                //data

                //totals
                
                int totalColmSize=tblSalesTotals.getColumnCount();
                com.itextpdf.text.pdf.PdfPTable tblTotals = new com.itextpdf.text.pdf.PdfPTable(colSize);
                for (int c = 0; c < totalColmSize; c++)
                {
                    tblTotals.addCell(new Phrase(tblSalesTotals.getValueAt(0, c).toString(), headerFontBody));
                }
                
                for (int c = 0; c < colSize; c++)
                {
                   PdfPCell cellDash=new PdfPCell(new Phrase(""));
                   cellDash.setBackgroundColor(new BaseColor(140, 221, 8));
                   tblTotals.addCell(cellDash);
                }
                document.add(tblTotals);
                
                //totals

                //Inserting List in PDF
//                List list = new List(true, 30);
//                list.add(new ListItem("Java4s"));
//                list.add(new ListItem("Php4s"));
//                list.add(new ListItem("Some Thing..."));
                 //document.add(list);            //In the new page we are going to add list
//
//                //Text formating in PDF
//                Chunk chunk = new Chunk("Welecome To Java4s Programming Blog...");
//                chunk.setUnderline(+1f, -2f);//1st co-ordinate is for line width,2nd is space between
//                Chunk chunk1 = new Chunk("Php4s.com");
//                chunk1.setUnderline(+4f, -8f);
//                chunk1.setBackground(new BaseColor(17, 46, 193));
                //Now Insert Every Thing Into PDF Document
                

                document.add(Chunk.NEWLINE);   //Something like in HTML :-)
                document.add(new Paragraph("Document Generated On - " + new Date().toString()+" By "+clsGlobalVarClass.gUserCode));
//                document.add(chunk);
//                document.add(chunk1);
                document.add(Chunk.NEWLINE);   //Something like in HTML :-)

                document.newPage();            //Opened new page
                
                document.close();
                file2.close();
                System.out.println("Pdf created successfully..");
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + clsPosConfigFile.exportReportPath+"/" +reportName+".pdf");

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
    }
    
        
}
