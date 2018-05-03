/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.POSGlobal.controller;

import java.awt.Component;
import java.awt.Color;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author sss
 */
    public class clsCustomTableCellRenderer extends DefaultTableCellRenderer 
    {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,boolean hasFocus, int row, int column) 
        {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            //System.out.println("COLOR="+value.toString());
            cell.setForeground( Color.blue );
            if(value.toString().startsWith("KT"))
            {
                //System.out.println("IN IF");
                cell.setForeground( Color.red );
                cell.setFont(new java.awt.Font("Trebuchet MS", 0, 16));
            }
            return cell;
        }
        
    }
