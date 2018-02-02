package com.example.qiaokang.homework9;

/**
 * Created by Qiao Kang on 2017/11/24.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
public class Table extends BaseAdapter {
    String[] Name;
    String[] Value;
    LayoutInflater myInflater;


    public Table(Context c, String[] dataName, String[] dataValue) {
        this.Name = dataName;
        this.Value = dataValue;
        myInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return Name.length;
    }

    @Override
    public Object getItem(int i) {
        return Name[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View v = myInflater.inflate(R.layout.my_listview, null);
        TextView nameData = (TextView) v.findViewById(R.id.Name);
        TextView valueData = (TextView) v.findViewById(R.id.Value);
        ImageView imag = (ImageView) v.findViewById(R.id.Arrow);
       // imag.setImageResource(R.drawable.up);

        nameData.setText(Name[i]);
        valueData.setText(Value[i]);
        if (Name[i] != "Change") {
            imag.setVisibility(View.INVISIBLE);
        }
        if (Name[i].equals("Change")&& Value[i].charAt(0)=='-') {
                imag.setImageResource(R.drawable.down);
        }

            return v;

    }
}