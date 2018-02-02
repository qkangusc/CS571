package com.example.qiaokang.homework9;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.LogRecord;

/**
 * Created by Qiao Kang on 2017/11/21.
 */
public class AutocompleteAdapter extends ArrayAdapter implements Filterable {
    private ArrayList<String> myStock = new ArrayList<String>();
    private String AUTO_URL = "http://cambridge2-env.us-west-1.elasticbeanstalk.com/auto/";
    private ArrayList<String> temp;

    public AutocompleteAdapter(Context context, int resource) {
        super(context, resource);


    }

    @Override
    public int getCount() {
        if(myStock==null){
            return 0;
        }
        return myStock.size();
    }

   // @Override
    public String getItem(int position) {
        return myStock.get(position);
    }

    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if(constraint != null){
                    try{
                        //get data from the web
                        String term = constraint.toString();
                        temp = new ArrayList<String>();
                        temp = new Autocomplete().execute(term).get();
                    }catch (Exception e){
                        Log.d("HUS","EXCEPTION "+e);
                    }
                    filterResults.values =temp;
                    filterResults.count = temp.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                myStock = temp;
                if(results != null && results.count > 0){
                    notifyDataSetChanged();
                }else{
                    notifyDataSetInvalidated();
                }
            }
        };

        return myFilter;
    }



    private class Autocomplete extends AsyncTask<String,Void,ArrayList>{

        @Override
        protected ArrayList doInBackground(String... params) {
            try {
                String NEW_URL = AUTO_URL + URLEncoder.encode(params[0],"UTF-8");
                Log.d("HUS", "JSON RESPONSE URL " + NEW_URL);

                URL url = new URL(NEW_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                StringBuilder builder = new StringBuilder();
                while ((line = reader.readLine()) != null){
                    builder.append(line).append("\n");
                }

                //parse JSON and store it in the list
                String jsonString =  builder.toString();
                ArrayList countryList = new ArrayList<>();

                JSONArray jsonArray = new JSONArray(jsonString);
                Log.i("result:",jsonString);
                Map<String, String> map = new HashMap<String, String>();


                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonobj = jsonArray.getJSONObject(i);
                    String symbol =  jsonobj.getString("Symbol");
                    String name =  jsonobj.getString("Name");
                    String exchange =  jsonobj.getString("Exchange");
                    String info = symbol + "-" +  name + "(" + exchange +")";
                    map.put("symbol", symbol);
                    map.put("display", info);

                    //StockTicker country = new StockTicker();
                    countryList.add(map.get("display"));
                    if(i==4){
                        break;
                    }
                }

                //return the countryList
                return countryList;

            } catch (Exception e) {
                Log.d("HUS", "EXCEPTION " + e);
                return null;
            }
        }
    }
}

