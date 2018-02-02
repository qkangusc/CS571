package com.example.qiaokang.homework9;

import java.util.Comparator;

/**
 * Created by Qiao Kang on 2017/11/29.
 */


public class SortByChange_Descend implements Comparator<stockObj> {

    @Override
    public int compare(stockObj o1, stockObj o2) {

        String[] str1= o1.change.split("\\(");
        String[] str2= o2.change.split("\\(");
        String change1= str1[0];
        String change2 = str2[0];


        return (int)Double.parseDouble(change2) - (int)Double.parseDouble(change1);
    }
}
