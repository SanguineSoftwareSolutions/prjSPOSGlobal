/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.POSGlobal.controller;

import com.POSGlobal.controller.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Jai chandra
 */
public class clsTDHOnItemDtl {
    
    private final List<subItemDtl> listSubItems=new ArrayList<>();
    public static Map<String, List<subItemDtl>> hm_ComboItemDtl = new HashMap<>();
    
    
    public  clsTDHOnItemDtl(){}
    
    
    
    public void funAddComboItemDtl(){
        try {
            hm_ComboItemDtl.clear();
            String tdhHD="select strItemCode,strTDHCode from tbltdhhd where strComboItemYN='Y' and strApplicable='Y'";
            ResultSet rstdhHD=clsGlobalVarClass.dbMysql.executeResultSet(tdhHD);
            
            while(rstdhHD.next()){
            String comboItemDtl="select a.strItemName,b.strSubItemCode,b.intSubItemQty,b.strDefaultYN from tblitemmaster a,tbltdhcomboitemdtl b "
                    + "where a.strItemCode=b.strSubItemCode and b.strItemCode='" + rstdhHD.getString(1) + "' and b.strTDHCode='"+rstdhHD.getString(2)+"'";
            ResultSet rscomboItemDtl=clsGlobalVarClass.dbMysql.executeResultSet(comboItemDtl);
            listSubItems.clear();
            while(rscomboItemDtl.next())
                {
                 subItemDtl ob=new subItemDtl(rscomboItemDtl.getString(2),rscomboItemDtl.getInt(3),rscomboItemDtl.getString(4),rscomboItemDtl.getString(1));
                 listSubItems.add(ob);
                }
            hm_ComboItemDtl.put(rstdhHD.getString(1), listSubItems);
            }
            rstdhHD.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
  
    
    public class subItemDtl{
        private String strSubItemCode;
        private int intSubItemQty;
        private String strDefaultYN;
        private String strSubItemName;
        
        public subItemDtl(String strSubItemCode,int intSubItemQty,String strDefaultYN, String strSubItemName){
            this.strSubItemCode=strSubItemCode;
            this.intSubItemQty=intSubItemQty;
            this.strDefaultYN=strDefaultYN;
            this.strSubItemName=strSubItemName;
        }

        /**
         * @return the strSubItemCode
         */
        public String getStrSubItemCode() {
            return strSubItemCode;
        }

        /**
         * @return the intSubItemQty
         */
        public double getIntSubItemQty() {
            return intSubItemQty;
        }

        /**
         * @return the strDefaultYN
         */
        public String getStrDefaultYN() {
            return strDefaultYN;
        }
       
        /**
         * @return the strDefaultYN
         */
        public String getstrSubItemName() {
            return strSubItemName;
        }
       
        
        
        
    }
}



