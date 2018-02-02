package com.example.qiaokang.homework9;

import java.util.Comparator;

/**
 * Created by Qiao Kang on 2017/11/29.
 */


    public class SortByPrice_Ascend implements Comparator<stockObj> {

        @Override
        public int  compare(stockObj o1, stockObj o2) {
            return (int)(o1.price) - (int)o2.price;
        }
}
