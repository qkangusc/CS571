package com.example.qiaokang.homework9;

/**
 * Created by Qiao Kang on 2017/11/29.
 */

public class stockObj {

    String name, change;
    double price;

    // Constructor
    public stockObj(String name, Double price, String change)
    {
        this.name = name;
        this.price = price;
        this.change = change;
    }
    public String toString()
    {
        return this.name + " " + this.price +
                " " + this.change;
    }
}
