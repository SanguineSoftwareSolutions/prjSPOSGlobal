/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.POSGlobal.controller;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author Administrator
 */
public class clsFixedSizeText extends PlainDocument
{
    private int max = 10;

    public clsFixedSizeText(int max)
    {
        this.max = max;
    }

    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException
    {
        // check string being inserted does not exceed max length
        if (getLength()+str.length()>max)
        {
            // If it does, then truncate it
            str = str.substring(0, max - getLength());
        }
        super.insertString(offs, str, a);
    }
}