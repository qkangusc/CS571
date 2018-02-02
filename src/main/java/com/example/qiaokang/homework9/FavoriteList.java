package com.example.qiaokang.homework9;

/**
 * Created by Qiao Kang on 2017/11/26.
 */
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class FavoriteList extends BaseAdapter{
    private ArrayList<String> symbol =new ArrayList<String>();
    private ArrayList<String> price = new ArrayList<String>();
    private ArrayList<String> change = new ArrayList<String>();

    LayoutInflater mInflater;


    public FavoriteList(Context c, ArrayList Symbol,ArrayList Price, ArrayList Change) {
        this.symbol = Symbol;
        this.price = Price;
        this.change = Change;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return symbol.size();
    }

    @Override
    public Object getItem(int i) {
        return symbol.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {


        View v = mInflater.inflate(R.layout.fav_list, null);
        TextView symbolData = (TextView) v.findViewById(R.id.symbol);
        TextView priceData = (TextView) v.findViewById(R.id.price);
        TextView changeData = (TextView) v.findViewById(R.id.change);



        symbolData.setText(symbol.get(i).toString());
        priceData.setText(price.get(i).toString());
        changeData.setText(change.get(i).toString());
        if(changeData.getText().toString().contains("-")){
           changeData.setTextColor(Color.RED);
        }
        else{
            changeData.setTextColor(Color.GREEN);
        }
        return v;

    }

}

