/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSGlobal.controller;

import com.POSGlobal.controller.clsBillDtl;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author ajjim
 */

public class clsBillComparator implements Comparator<clsBillDtl>
{

    private List<Comparator<clsBillDtl>> listComparators;

    @SafeVarargs
    public clsBillComparator(Comparator<clsBillDtl>... comparators)
    {
        this.listComparators = Arrays.asList(comparators);
    }

    @Override
    public int compare(clsBillDtl o1, clsBillDtl o2)
    {
        for (Comparator<clsBillDtl> comparator : listComparators)
        {
            int result = comparator.compare(o1, o2);
            if (result != 0)
            {
                return result;
            }
        }
        return 0;
    }
}