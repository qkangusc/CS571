package com.example.qiaokang.homework9;


import java.util.Comparator;

/**
 * Created by Qiao Kang on 2017/11/29.
 */

public class SortBySymbol_Decrese implements Comparator<stockObj>{

    @Override
    public int compare(stockObj o1, stockObj o2) {
        return o2.name.compareTo(o1.name);
    }
}
