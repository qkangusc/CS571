package com.example.qiaokang.homework9;

/**
 * Created by Qiao Kang on 2017/11/21.
 */

public class StockTicker {
    private String symbol;
    private String name;
    private String exchange;

    public String getSymbol() {
        return symbol;
    }
    public String getName() {
        return name;
    }
    public String getExchange() {
        return exchange;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setExchange(String exchange) {
        this.exchange = exchange;
    }
}
