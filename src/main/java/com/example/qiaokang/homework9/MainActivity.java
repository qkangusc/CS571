package com.example.qiaokang.homework9;


import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.PopupMenu;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Filter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.R.attr.description;
import static android.R.attr.indicatorEnd;
import static android.R.attr.name;
import static com.example.qiaokang.homework9.R.array.spinner1;
import static com.example.qiaokang.homework9.R.array.spinner2;
import static com.example.qiaokang.homework9.R.styleable.CompoundButton;
import static com.example.qiaokang.homework9.R.styleable.MenuGroup;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public static final int SHOW_RESPONSE = 0;
    SharedPreferences favoritelist;
    AutoCompleteTextView stockticker;
    TextView resultTextView;
    TextView getQuote;
    Switch autofresh;
    ListView favList;
    ProgressBar progressBar;
    Spinner spinner1,spinner2;
    int i = 0;
    int j = 0;
    int count = 0;
    int mark = 0;
    String input;
    Intent intent;
    String message;
    Double last_Price;
    Double change, changepercent;
    String [] str={""};
    Object obj;
    ArrayList<stockObj> ObjSorting;
    ArrayList<stockObj> defaultSorting;
    ArrayList<String> Fav_symbol;
    ArrayList<String> Fav_symbol1;
    ArrayList<String> price_adapter;
    ArrayList<String> change_adapter;
    ArrayList<String> price_adapter1;
    ArrayList<String> change_adapter1;
    ArrayList<Double> priceList;
    ArrayList<String> storage;
    TimerTask myTask;

    public void refresh(View view){
        if(!favoritelist.getString("symbol","").equals("")&&!favoritelist.getString("symbol","").equals("[]")) {
            sendHttpRequest();
        }

    }
    public void autofresh(View view){
        if(!favoritelist.getString("symbol","").equals("")&&!favoritelist.getString("symbol","").equals("[]")) {
            sendHttpRequest();
        }
    }
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1) {
                progressBar.setVisibility(View.VISIBLE);
            }
            super.handleMessage(msg);
        }
    };



    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fav_symbol = new ArrayList();
        Fav_symbol1 = new ArrayList();
        storage = new ArrayList<String>();
        priceList = new ArrayList<Double>();
        obj = new Object();
        mark = 0;
        ObjSorting= new ArrayList<stockObj>();
        defaultSorting = new ArrayList<stockObj>();
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        favList = (ListView)findViewById(R.id.FavoriteList);
        favoritelist =  getSharedPreferences("FavoriteList", 0);
        autofresh = (Switch)findViewById(R.id.autofresh);

            autofresh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Timer timer = new Timer();
                    if (!favoritelist.getString("symbol", "").equals("") && !favoritelist.getString("symbol", "").equals("[]")) {
                        if (isChecked) {

                            myTask = new TimerTask() {
                                @Override
                                public void run() {
                                    sendHttpRequest();
                                }
                            };
                            timer.schedule(myTask, 0l, 5000);


                        } else {
                            timer.cancel();
                            timer = null;
                            myTask.cancel();
                            myTask = null;


                        }
                    }
                }
            });




       //favoritelist.edit().clear().apply();
        if(favoritelist.getString("symbol","").equals("")||favoritelist.getString("symbol","").equals("[]")){
            progressBar.setVisibility(View.INVISIBLE);

        }
        Log.i("initial",favoritelist.getString("symbol", ""));


        //share不为空，开始操作
        if(!favoritelist.getString("symbol","").equals("")&&!favoritelist.getString("symbol","").equals("[]")) {
            progressBar.setVisibility(View.VISIBLE);
            String temp = getSharedPreferences("FavoriteList", 0).getString("symbol", "");
            if(temp.contains(",")) {
                str = temp.substring(1, temp.length() - 1).split(", ");
            }
            else{
                str[0] = temp.substring(1, temp.length() - 1);
            }
            Log.i("size",str.length+"");

            price_adapter = new ArrayList<String>();
            change_adapter = new ArrayList<String>();
            price_adapter1 = new ArrayList<String>();
            change_adapter1 = new ArrayList<String>();

            for(int k = 0 ; k < str.length; k++){
                Log.i("str",str[k]);
                Fav_symbol.add(str[k]);
                Fav_symbol1.add(str[k]); //备用，用于default
                price_adapter.add("aa");
                change_adapter.add("aa");
                price_adapter1.add("aa");   //备用
                change_adapter1.add("aa");  //备用
            }
            Log.i("Favsymbol",Fav_symbol.toString());
            for( i = 0; i < str.length;i++){
                    Log.i("length",str[i].length()+"");
                    RequestQueue mRequestQueue = Volley.newRequestQueue(this);
                    final String URL = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + str[i].replace(" ","") + "&interval=daily&time_period=10&series_type=open&apikey=39I7VDS18H1LP3XK";
                    JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("received","rcvedJSON");
                            Log.i("result",response.toString());
                            count = count + 1;
                            ArrayList<String> KEYS = new ArrayList<String>();
                            Iterator iterator1 = response.keys();
                            while (iterator1.hasNext()) {
                                String key = (String) iterator1.next();
                                KEYS.add(key);
                            }

                            try {
                                if (KEYS.size()==2 && KEYS.get(1).equals("Time Series (Daily)")) {
                                    JSONObject time_series_daily = response.getJSONObject("Time Series (Daily)");
                                    JSONObject meta_data = response.getJSONObject("Meta Data");
                                    String name = meta_data.getString("2. Symbol");
                                   // Log.i("name",name);

                                    Iterator iterator = time_series_daily.keys();
                                    while (iterator.hasNext()) {
                                        String key = (String) iterator.next();
                                        JSONObject value = time_series_daily.getJSONObject(key);
                                        String price = value.optString("4. close");
                                        //Log.i("price",price);
                                        priceList.add(Double.parseDouble(price));


                                    }
                                    double changeprice = priceList.get(0) - priceList.get(1);
                                    change = Math.round(changeprice * 100.0) / 100.0;
                                    changepercent = Math.round((change*100 / priceList.get(1)) * 100.0) / 100.0;
                                    last_Price = Math.round(priceList.get(1) * 100.0) / 100.0;
                                    ObjSorting.add(new stockObj(name,priceList.get(0),change + "("+ changepercent + "%)"));
                                    //defaultSorting.add(new stockObj(name,priceList.get(0),change + "("+ changepercent + "%)"));
                                     Log.i("obj",ObjSorting.toString());

                                    //找到和share的symbol对应的位置
                                for(j = 0; j < str.length;j++) {
                                    if (name.equals(str[j].replace(" ",""))) {
                                        Log.i("j",j+"");
                                        Log.i("ss",name);
                                        Log.i("llll",priceList.get(0).toString());
                                        price_adapter.set(j, priceList.get(0).toString());
                                        price_adapter1.set(j, priceList.get(0).toString());
                                        change_adapter.set(j, change + "("+ changepercent + "%)");
                                        change_adapter1.set(j, change + "("+ changepercent + "%)");
                                        priceList = new ArrayList<Double>();
                                        break;
                                    }
                                  }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //Log.i("count",count+"");
                            if(count == str.length) {
                                progressBar.setVisibility(View.INVISIBLE);
                                Log.i("rrrr","set");
                                FavoriteList Fav_adapter = new FavoriteList(getBaseContext(), Fav_symbol, price_adapter, change_adapter);
                                favList.setAdapter(Fav_adapter);
                                favList.setOnItemClickListener(new GetQuote());
                                favList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                    @Override
                                    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                                        PopupMenu popupMenu = new PopupMenu(MainActivity.this,favList);
                                        popupMenu.getMenuInflater().inflate(R.menu.fav_menu,popupMenu.getMenu());
                                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
                                            @Override
                                            public boolean onMenuItemClick(MenuItem item){
                                                if(item.getTitle().toString().equals("Yes")){
                                                    String message = favList.getItemAtPosition(position).toString();
                                                    Fav_symbol.clear();
                                                    price_adapter.clear();
                                                    change_adapter.clear();
                                                    for(int i = 0; i < ObjSorting.size();i++){
                                                        if(ObjSorting.get(i).name.equals(message)){
                                                            ObjSorting.remove(i);
                                                            break;
                                                        }
                                                    }
                                                    Log.i("after delete",ObjSorting.toString());
                                                    if(ObjSorting.size()!=0){
                                                        for(int i = 0; i < ObjSorting.size();i++){
                                                            storage.add(ObjSorting.get(i).name);
                                                        }
                                                    }
                                                   else{
                                                        storage = new ArrayList<String>();
                                                    }
                                                    SharedPreferences.Editor editor =  favoritelist.edit();
                                                    editor.putString("symbol", storage.toString());
                                                    editor.apply();
                                                    Log.i("shared after remove",favoritelist.getString("symbol",""));

                                                    for(int i = 0; i < ObjSorting.size();i++){
                                                        Fav_symbol.add(ObjSorting.get(i).name);
                                                        price_adapter.add(ObjSorting.get(i).price+"");
                                                        change_adapter.add(ObjSorting.get(i).change);
                                                    }
                                                    FavoriteList Fav_adapter = new FavoriteList(getBaseContext(), Fav_symbol, price_adapter, change_adapter);
                                                    favList.setAdapter(Fav_adapter);
                                                    favList.setOnItemClickListener(new GetQuote());
                                                }
                                                else{

                                                }
                                               // message = favList.getItemAtPosition(position).toString();
                                                Toast.makeText(MainActivity.this, ""+item.getTitle(), Toast.LENGTH_LONG).show();
                                                return true;
                                            }
                                        });
                                        popupMenu.show();
                                        return true;
                                    }
                                });
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.e("Error: ", error.getMessage());
                            Log.i("error", error.toString());
                        }
                    });
                    req.setRetryPolicy(new RetryPolicy() {
                        @Override
                        public int getCurrentTimeout() {
                            return 50000;
                        }

                        @Override
                        public int getCurrentRetryCount() {
                            return 50000;
                        }

                        @Override
                        public void retry(VolleyError error) throws VolleyError {

                        }
                    });

                    mRequestQueue.add(req);


            }

        }
        stockticker = (AutoCompleteTextView) findViewById(R.id.StockTicker);
        getQuote = (TextView) findViewById(R.id.getQuote);
        getQuote.setOnClickListener(this);
        final AutocompleteAdapter adapter = new AutocompleteAdapter(this,android.R.layout.simple_dropdown_item_1line);
        stockticker.setAdapter(adapter);
        stockticker.setThreshold(1);
        stockticker.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String countryName = adapter.getItem(position);
                String [] ss = countryName.split("-");
                stockticker.setText(countryName);
                input = ss[0];
            }
        });

        // hide action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //remove keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        spinner1 = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,R.array.spinner1, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(this);


        spinner2 = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.spinner2, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(this);



    }
    public void onCheckedChanged(android.widget.CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (!favoritelist.getString("symbol", "").equals("") && !favoritelist.getString("symbol", "").equals("[]")) {
                sendHttpRequest();
            } else {

            }
        }
    }


    private boolean isValid(String symbol) {
        String SYMBOL_PATTERN = "^\\s*$";

        Pattern pattern = Pattern.compile(SYMBOL_PATTERN);
        Matcher matcher = pattern.matcher(symbol);
        return matcher.matches();
    }
    @Override
    public void onClick(View v) {
       if (isValid(stockticker.getText().toString())) {
            Toast.makeText(this, "Please enter a stock name or symbol", Toast.LENGTH_SHORT).show();
        }
        else {
           Log.i("Stock", stockticker.getText().toString());
           intent = new Intent(this, Main2Activity.class);
           if(input!=null) {
              message = input;
           }
           else{
               message = stockticker.getText().toString().toUpperCase();
           }
           Bundle extras = new Bundle();
           extras.putString("EXTRA_SYMBOL", message);
           intent.putExtras(extras);


           startActivity(intent);
       }
    }

    public void clear(View view){
        stockticker.setText("");
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(spinner1.getSelectedItem().toString().equals("Default")&& mark==1){
            Log.i("current state","default");
            Log.i("defaultSorting",defaultSorting.toString());
          /*  Fav_symbol.clear();
            price_adapter.clear();
            change_adapter.clear();*/
          /*  for(int i = 0; i <defaultSorting.size();i++){*/
              /*  Fav_symbol.add(defaultSorting.get(i).name);
                price_adapter.add(defaultSorting.get(i).price+"");
                change_adapter.add(defaultSorting.get(i).change);*/

           // }
            FavoriteList Fav_adapter = new FavoriteList(getBaseContext(), Fav_symbol1, price_adapter1, change_adapter1);
            favList.setAdapter(Fav_adapter);
            favList.setOnItemClickListener(new GetQuote());
        }
        if(spinner1.getSelectedItem().toString().equals("Symbol")&&!favoritelist.getString("symbol","").equals("")&&!favoritelist.getString("symbol","").equals("[]")){
            mark = 1;
            if(spinner2.getSelectedItem().toString().equals("Ascending")) {
                Collections.sort(ObjSorting, new SortBySymbol());
                Log.i("after increasesort", ObjSorting.toString());
                Sorting();

            }
            else if(spinner2.getSelectedItem().toString().equals("Descending")){
                Collections.sort(ObjSorting, new SortBySymbol_Decrese());
                Log.i("after decresesort", ObjSorting.toString());
                Sorting();
            }

        }

        if(spinner1.getSelectedItem().toString().equals("Price")&&!favoritelist.getString("symbol","").equals("")&&!favoritelist.getString("symbol","").equals("[]")){
            Log.i("current state","Price");
            mark = 1;
            if(spinner2.getSelectedItem().toString().equals("Ascending")) {
                Collections.sort(ObjSorting, new SortByPrice_Ascend());
                Log.i("after pr increasesort", ObjSorting.toString());
                Sorting();

            }
            else if(spinner2.getSelectedItem().toString().equals("Descending")){
                Collections.sort(ObjSorting, new SortByPrice_Descend());
                Log.i("after pr decresesort", ObjSorting.toString());
                Sorting();
            }
        }
        if(spinner1.getSelectedItem().toString().equals("Change")&&!favoritelist.getString("symbol","").equals("")&&!favoritelist.getString("symbol","").equals("[]")){
            mark = 1;
            Log.i("current state","Change");
            Log.i("current Objsorting",ObjSorting.toString());
            if(spinner2.getSelectedItem().toString().equals("Ascending")) {
                Collections.sort(ObjSorting, new SortByChange_Ascend());
                Log.i("change increasesort", ObjSorting.toString());
                Sorting();

            }
            else if(spinner2.getSelectedItem().toString().equals("Descending")){
                Collections.sort(ObjSorting, new SortByChange_Descend());
                Log.i("change decresesort", ObjSorting.toString());
                Sorting();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void Sorting(){
        Fav_symbol.clear();
        price_adapter.clear();
        change_adapter.clear();
        for(int i = 0; i < ObjSorting.size();i++){
            Fav_symbol.add(ObjSorting.get(i).name);
            price_adapter.add(ObjSorting.get(i).price+"");
            change_adapter.add(ObjSorting.get(i).change);
            FavoriteList Fav_adapter = new FavoriteList(getBaseContext(), Fav_symbol, price_adapter, change_adapter);
            favList.setAdapter(Fav_adapter);
            favList.setOnItemClickListener(new GetQuote());
        }

    }
    class GetQuote implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            intent = new Intent(MainActivity.this, Main2Activity.class);
            message = favList.getItemAtPosition(position).toString();
            Log.i("message",message);
            Bundle extras = new Bundle();
            extras.putString("EXTRA_SYMBOL", message);
            intent.putExtras(extras);
            startActivity(intent);
        }
    }

   /* class Delete implements AdapterView.OnLongClickListener{
        @Override
        public boolean onLongClick(View v) {


            return false;
        }
    }*/
    public void sendHttpRequest(){
        count = 0;
        str = new String[] {""};
        Fav_symbol.clear();
        Fav_symbol1.clear();
        price_adapter = new ArrayList<String>();
        change_adapter = new ArrayList<String>();
        price_adapter1 = new ArrayList<String>();
        change_adapter1 = new ArrayList<String>();
        //Fav_symbol1 = new ArrayList<String>();


        priceList.clear();
        ObjSorting.clear();
        Log.i("refreshed ObjSort",ObjSorting.toString());
        String temp = getSharedPreferences("FavoriteList", 0).getString("symbol", "");
        if(temp.contains(",")) {
            str = temp.substring(1, temp.length() - 1).split(", ");
        }
        else{
            str[0] = temp.substring(1, temp.length() - 1);
        }
       // defaultSorting.clear();


        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                Message msg = mHandler.obtainMessage();
                msg.what = 1;
                msg.sendToTarget();
            }});
        thread.start();

        for(int k = 0 ; k < str.length; k++){
            Log.i("str",str[k]);
            Fav_symbol.add(str[k]);
            Fav_symbol1.add(str[k]);
            price_adapter.add("aa");
            change_adapter.add("aa");
            price_adapter1.add("aa");
            change_adapter1.add("aa");
        }
        Log.i("shre",favoritelist.getString("symbol",""));
        Log.i("Favsymbol",Fav_symbol.toString());
        Log.i("Favsymbol111",Fav_symbol1.toString());
        for( i = 0; i < str.length;i++){
            //Log.i("length",str[i].length()+"");
            RequestQueue mRequestQueue = Volley.newRequestQueue(this);
            final String URL = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + str[i].replace(" ","") + "&interval=daily&time_period=10&series_type=open&apikey=39I7VDS18H1LP3XK";
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.i("received","rcvedJSON");
                   // Log.i("result",response.toString());
                    count = count + 1;
                    ArrayList<String> KEYS = new ArrayList<String>();
                    Iterator iterator1 = response.keys();
                    while (iterator1.hasNext()) {
                        String key = (String) iterator1.next();
                        KEYS.add(key);
                    }

                    try {
                        if (KEYS.size()==2 && KEYS.get(1).equals("Time Series (Daily)")) {
                            JSONObject time_series_daily = response.getJSONObject("Time Series (Daily)");
                            JSONObject meta_data = response.getJSONObject("Meta Data");
                            String name = meta_data.getString("2. Symbol");
                            // Log.i("name",name);

                            Iterator iterator = time_series_daily.keys();
                            while (iterator.hasNext()) {
                                String key = (String) iterator.next();
                                JSONObject value = time_series_daily.getJSONObject(key);
                                String price = value.optString("4. close");
                                //Log.i("price",price);
                                priceList.add(Double.parseDouble(price));


                            }
                            double changeprice = priceList.get(0) - priceList.get(1);
                            change = Math.round(changeprice * 100.0) / 100.0;
                            changepercent = Math.round((change*100 / priceList.get(1)) * 100.0) / 100.0;
                            last_Price = Math.round(priceList.get(1) * 100.0) / 100.0;
                            ObjSorting.add(new stockObj(name,priceList.get(0),change + "("+ changepercent + "%)"));
                           // defaultSorting.add(new stockObj(name,priceList.get(0),change + "("+ changepercent + "%)"));

                            //Log.i("default",defaultSorting.toString());
                            for(j = 0; j < str.length;j++) {
                                if (name.equals(str[j].replace(" ",""))) {
                                    price_adapter.set(j, priceList.get(0).toString());
                                    price_adapter1.set(j, priceList.get(0).toString());
                                    change_adapter.set(j, change + "("+ changepercent + "%)");
                                    change_adapter1.set(j, change + "("+ changepercent + "%)");
                                    priceList = new ArrayList<Double>();
                                    break;
                                }
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(count == str.length) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.i("rrrr","set");
                        Log.i("obj",ObjSorting.toString());
                       /* Fav_symbol.clear();
                        price_adapter.clear();
                        change_adapter.clear();*/
                        for(int i = 0; i <ObjSorting.size();i++){
                            Fav_symbol.add(ObjSorting.get(i).name);
                            price_adapter.add(ObjSorting.get(i).price+"");
                            change_adapter.add(ObjSorting.get(i).change);
                        }
                        if(!favoritelist.getString("symbol","").equals("")&&!favoritelist.getString("symbol","").equals("[]")&&spinner1.getSelectedItem().toString().equals("Default")){
                            Log.i("mark",mark+"!");
                            FavoriteList Fav_adapter1 = new FavoriteList(getBaseContext(), Fav_symbol1, price_adapter1, change_adapter1);
                            favList.setAdapter(Fav_adapter1);
                            favList.setOnItemClickListener(new GetQuote());

                        }
                        else {
                            if (spinner1.getSelectedItem().toString().equals("Symbol") && !favoritelist.getString("symbol", "").equals("") && !favoritelist.getString("symbol", "").equals("[]")) {
                                mark = 1;
                                Log.i("mark", mark + "");
                                if (spinner2.getSelectedItem().toString().equals("Ascending")) {
                                    Collections.sort(ObjSorting, new SortBySymbol());
                                    Log.i("after increasesort", ObjSorting.toString());
                                    Sorting();

                                } else if (spinner2.getSelectedItem().toString().equals("Descending")) {
                                    Collections.sort(ObjSorting, new SortBySymbol_Decrese());
                                    Log.i("after decresesort", ObjSorting.toString());
                                    Sorting();
                                }

                            }

                            if (spinner1.getSelectedItem().toString().equals("Price") && !favoritelist.getString("symbol", "").equals("") && !favoritelist.getString("symbol", "").equals("[]")) {
                                mark = 1;
                                Log.i("mark", mark + "");

                                if (spinner2.getSelectedItem().toString().equals("Ascending")) {
                                    Collections.sort(ObjSorting, new SortByPrice_Ascend());
                                    Log.i("after increasesort", ObjSorting.toString());
                                    Sorting();

                                } else if (spinner2.getSelectedItem().toString().equals("Descending")) {
                                    Collections.sort(ObjSorting, new SortByPrice_Descend());
                                    Log.i("after decresesort", ObjSorting.toString());
                                    Sorting();
                                }
                            }
                            if (spinner1.getSelectedItem().toString().equals("Change") && !favoritelist.getString("symbol", "").equals("") && !favoritelist.getString("symbol", "").equals("[]")) {
                                mark = 1;
                                Log.i("mark", mark + "");
                                if (spinner2.getSelectedItem().toString().equals("Ascending")) {
                                    Collections.sort(ObjSorting, new SortByChange_Ascend());
                                    Log.i("after increasesort", ObjSorting.toString());
                                    Sorting();

                                } else if (spinner2.getSelectedItem().toString().equals("Descending")) {
                                    Collections.sort(ObjSorting, new SortByChange_Descend());
                                    Log.i("after decresesort", ObjSorting.toString());
                                    Sorting();
                                }
                            }
                            FavoriteList Fav_adapter = new FavoriteList(getBaseContext(), Fav_symbol, price_adapter, change_adapter);

                            favList.setAdapter(Fav_adapter);
                            favList.setOnItemClickListener(new GetQuote());
                        }

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                    Log.i("error", error.toString());
                }
            });
            req.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 50000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 50000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });
            mRequestQueue.add(req);
        }

    }


}
