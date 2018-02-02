package com.example.qiaokang.homework9;

/**
 * Created by Qiao Kang on 2017/11/26.
 */
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class News_Table extends BaseAdapter{
    ArrayList Link;
    ArrayList Title;
    ArrayList Author;
    ArrayList Date;
    /*String[] Title;
    String[] Author;
    String[] Date;*/
    LayoutInflater mInflater;


    public News_Table(Context c, ArrayList dataLink, ArrayList dataTitle, ArrayList dataAuthor, ArrayList dataDate) {
        this.Link = dataLink;
        this.Title = dataTitle;
        this.Author = dataAuthor;
        this.Date = dataDate;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return Title.size();
    }

    @Override
    public Object getItem(int i) {
        return Title.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {


        View v = mInflater.inflate(R.layout.news_feed, null);
        TextView titleData = (TextView) v.findViewById(R.id.title);
        TextView authorData = (TextView) v.findViewById(R.id.author);
        TextView dateData = (TextView) v.findViewById(R.id.date);

        titleData.setPrivateImeOptions(Link.get(i).toString());
        titleData.setText(Title.get(i).toString());
        authorData.setText(Author.get(i).toString());
        dateData.setText(Date.get(i).toString());
        return v;

    }

}
