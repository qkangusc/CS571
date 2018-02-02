package com.example.qiaokang.homework9;

/**
 * Created by Qiao Kang on 2017/11/21.
 */
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.MessageDialog;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

import static com.example.qiaokang.homework9.R.drawable.down;
import static com.example.qiaokang.homework9.R.mipmap.empty;
import static com.example.qiaokang.homework9.R.mipmap.filled;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.facebook.internal.FacebookDialogFragment.TAG;


public class tab1  extends Fragment implements AdapterView.OnItemSelectedListener  {
    private ListView mListView;
    private WebView mWebView;
    private Button changeBTn;
    private TextView LeftTitle;
    Spinner indicator;
    WebInterface webInterface;
    int temp;
    String text;
    ImageView imag;
    String symbol;
    String responseURL="";
    String resLength;
    View view;
    ArrayList<Double> priceList = new ArrayList<Double>();
    ArrayList<Double> volumeList = new ArrayList<Double>();
    ArrayList<Double> openList = new ArrayList<Double>();
    ArrayList<Double> highList = new ArrayList<Double>();
    ArrayList<Double> lowList = new ArrayList<Double>();
    ArrayList<String> storage;
    ProgressBar progressBar,progressBar2;
    Button facebook, Fav;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    MessageDialog messageDialog;
    TextView alertMessage;


    Double last_Price;
    Double change, changepercent;
    Double open, close,high,low;
    int i = 0;
    String [] indicators = {"TIME_SERIES_DAILY", "SMA", "EMA", "STOCH", "RSI", "ADX", "CCI", "BBANDS", "MACD"};
    String[] dataName = {"Stock Symbol", "Last Price", "Change", "Timestamp", "Open", "Close", "Day's Range", "Volume"};

    public void change(){
         mWebView.setVisibility(View.INVISIBLE);
        //mWebView.setAlpha(0);
        if(text.equals("Price")){
            if(temp != 0){
                progressBar2.setVisibility(View.VISIBLE);
                mWebView.loadUrl("file:///android_asset/highchart.html");
                mWebView.setVisibility(View.VISIBLE);
            }
        }
        if(text.equals("SMA")){
            progressBar2.setVisibility(View.VISIBLE);
            mWebView.loadUrl("file:///android_asset/sma.html");
            mWebView.setVisibility(View.VISIBLE);
        }
        if(text.equals("EMA")){
            progressBar2.setVisibility(View.VISIBLE);
            mWebView.loadUrl("file:///android_asset/ema.html");
            mWebView.setVisibility(View.VISIBLE);
        }
        if(text.equals("STOCH")){
            progressBar2.setVisibility(View.VISIBLE);
            mWebView.loadUrl("file:///android_asset/stoch.html");
            mWebView.setVisibility(View.VISIBLE);
        }
        if(text.equals("RSI")){
            progressBar2.setVisibility(View.VISIBLE);
            mWebView.loadUrl("file:///android_asset/rsi.html");
            mWebView.setVisibility(View.VISIBLE);
        }
        if(text.equals("ADX")){
            progressBar2.setVisibility(View.VISIBLE);
            mWebView.loadUrl("file:///android_asset/adx.html");
            mWebView.setVisibility(View.VISIBLE);
        }
        if(text.equals("CCI")){
            progressBar2.setVisibility(View.VISIBLE);
            mWebView.loadUrl("file:///android_asset/cci.html");
            mWebView.setVisibility(View.VISIBLE);
        }
        if(text.equals("BBANDS")){
            progressBar2.setVisibility(View.VISIBLE);
            mWebView.loadUrl("file:///android_asset/bbands.html");
            mWebView.setVisibility(View.VISIBLE);
        }
        if(text.equals("MACD")){
            progressBar2.setVisibility(View.VISIBLE);
            mWebView.loadUrl("file:///android_asset/macd.html");
            mWebView.setVisibility(View.VISIBLE);
        }
        changeBTn.setEnabled(false);
    }
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1) {
                progressBar.setVisibility(View.INVISIBLE);
                progressBar2.setVisibility(View.INVISIBLE);
                mListView.setVisibility(View.INVISIBLE);
                changeBTn.setVisibility(View.INVISIBLE);
                indicator.setVisibility(View.INVISIBLE);
                LeftTitle.setVisibility(View.INVISIBLE);
                alertMessage.setVisibility(View.VISIBLE);
            }
            if(msg.what == 2){
                facebook.setEnabled(true);
                progressBar2.setVisibility(View.INVISIBLE);


            }
            super.handleMessage(msg);
        }
    };


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.fragment_main2, container, false);
            temp = 0;
           // storage = new ArrayList();
            callbackManager = CallbackManager.Factory.create();
            shareDialog = new ShareDialog(this);
            messageDialog = new MessageDialog(this);
            symbol = ((Main2Activity) getActivity()).getSymbol();

            mListView = (ListView) view.findViewById(R.id.listview);
            mWebView = (WebView) view.findViewById(R.id.webview);
            LeftTitle = (TextView)view.findViewById(R.id.indicator);
            changeBTn = (Button) view.findViewById(R.id.button);
            changeBTn.setEnabled(false);
            alertMessage = (TextView) view.findViewById(R.id.alert);
            alertMessage.setVisibility(View.INVISIBLE);

            facebook = (Button) view.findViewById(R.id.facebook);
            facebook.setEnabled(false);
            shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {
                    Toast.makeText(getActivity(), "Post Successful!!", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "send success");
                }

                @Override
                public void onCancel() {
                    Toast.makeText(getActivity(), "Post Cancelled!!", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "send cancelled");
                }

                @Override
                public void onError(FacebookException e) {
                    Log.e(TAG, "send error");
                }
            });

            facebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("!!!!!!","clicked!");
                        Log.d("url is", responseURL + "aa");
                        if (ShareDialog.canShow(ShareLinkContent.class)) {
                            ShareLinkContent content = new ShareLinkContent.Builder()
                                    .setContentUrl(Uri.parse(responseURL))
                                    .build();

                            shareDialog.show(content);
                        }
                }
            });
            Fav = (Button) view.findViewById(R.id.fav);

            String tempStr = getActivity().getSharedPreferences("FavoriteList", 0).getString("symbol", "");
            Log.i("current",tempStr);
            Log.i("current symbol",symbol);
            if(tempStr.contains(symbol)){
                Fav.setBackgroundResource(R.mipmap.filled);
                Fav.setTag(R.mipmap.filled);
            }
            else{
                Fav.setBackgroundResource(R.mipmap.empty);
                Fav.setTag(R.mipmap.empty);
            }
            Fav.setEnabled(false);
            Fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int resId = (Integer) v.getTag();
                    if(resId == R.mipmap.empty) {
                        v.setBackgroundResource(R.mipmap.filled);
                        v.setTag(R.mipmap.filled);
                        String tempStr = getActivity().getSharedPreferences("FavoriteList", 0).getString("symbol", "");

                        if(tempStr.equals("")){
                            storage = new ArrayList<String>();
                            storage.add(symbol);
                            Log.i("add",storage.toString());
                            SharedPreferences.Editor editor =  ((Main2Activity)getActivity()).getSharedPreferences("FavoriteList", 0).edit();
                            editor.putString("symbol", storage.toString());
                            editor.apply();
                        }
                        else {
                            tempStr = getActivity().getSharedPreferences("FavoriteList", 0).getString("symbol", "");
                            Log.i("temp1",tempStr);
                            tempStr = tempStr.replace("["," ");
                            tempStr = tempStr.replace("]"," ");
                            tempStr = tempStr.replace(" ","");
                            Log.i("temp",tempStr);
                            if(tempStr.contains(",")) {
                                storage = new ArrayList<String>(Arrays.asList(tempStr.split(",")));
                            }
                            else {
                                if(tempStr.equals("")) {
                                    storage = new ArrayList<String>();
                                }
                                else{
                                    storage = new ArrayList<String>();
                                    storage.add(tempStr);
                                }
                            }
                            Log.i("after",storage.toString());
                            Log.i("size",storage.size()+"");
                            Log.i("after remove",storage.toString());

                            storage.add(symbol);
                            Log.i("after add",storage.toString());
                            SharedPreferences.Editor editor =  ((Main2Activity)getActivity()).getSharedPreferences("FavoriteList", 0).edit();
                            editor.putString("symbol", storage.toString());
                            editor.apply();

                        }


                    }

                    else {
                        v.setBackgroundResource(R.mipmap.empty);
                        v.setTag(R.mipmap.empty);
                        String tempStr = getActivity().getSharedPreferences("FavoriteList", 0).getString("symbol", "");
                        Log.i("temp1",tempStr);
                        tempStr = tempStr.replace("["," ");
                        tempStr = tempStr.replace("]"," ");
                        tempStr = tempStr.replace(" ","");
                        Log.i("temp",tempStr);
                        if(tempStr.contains(",")) {
                            storage = new ArrayList<String>(Arrays.asList(tempStr.split(",")));
                        }
                        else{
                            storage = new ArrayList<String>(Arrays.asList(tempStr));
                        }
                        Log.i("after",storage.toString());
                        Log.i("size",storage.size()+"");
                        for(i = 0; i < storage.size();i++){
                            if(storage.get(i).contains(symbol)){
                                storage.remove(i);break;
                            }
                        }
                        Log.i("after remove",storage.toString());
                        SharedPreferences.Editor editor =  ((Main2Activity)getActivity()).getSharedPreferences("FavoriteList", 0).edit();
                        editor.putString("symbol", storage.toString());
                        editor.apply();

                    }
                }
            });


            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            imag = (ImageView) view.findViewById(R.id.Arrow);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            progressBar2 = (ProgressBar) view.findViewById(R.id.progressBar2);
            indicator = (Spinner) view.findViewById(R.id.indicators);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.indicators, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            indicator.setAdapter(adapter);
            indicator.setOnItemSelectedListener(this);


            sendHttpRequest(symbol);

        }
            return view;

    }
    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

       public void sendHttpRequest(final String symbol){
                  webInterface = new WebInterface(symbol);
                  mWebView.addJavascriptInterface(webInterface, "Android");
                   for (i = 0; i < indicators.length; i++) {
                       RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
                       final String URL = "http://cambridge2-env.us-west-1.elasticbeanstalk.com/json?symbol=" + symbol + "&indicators=" + indicators[i];
                       JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {


                           @Override
                           public void onResponse(JSONObject response) {

                               mWebView.loadUrl("file:///android_asset/highchart.html");


                               ArrayList<String> KEYS = new ArrayList<String>();
                               Iterator iterator1 = response.keys();
                               while (iterator1.hasNext()) {
                                   String key = (String) iterator1.next();
                                   KEYS.add(key);
                               }

                               //StringBuilder names = new StringBuilder();
                               try {
                                   if (KEYS.size()==2 && KEYS.get(1).equals("Time Series (Daily)")) {
                                      // mWebView.addJavascriptInterface(new WebInterface(response), "Android");
                                       JSONObject time_series_daily = response.getJSONObject("Time Series (Daily)");
                                       JSONObject meta_data = response.getJSONObject("Meta Data");

                                       Iterator iterator = time_series_daily.keys();
                                       while (iterator.hasNext()) {
                                           String key = (String) iterator.next();
                                           JSONObject value = time_series_daily.getJSONObject(key);
                                           String price = value.optString("4. close");
                                           String volume = value.getString("5. volume");
                                           String open = value.optString("1. open");
                                           String high = value.optString("2. high");
                                           String low = value.optString("3. low");

                                           priceList.add(Double.parseDouble(price));
                                           volumeList.add(Double.parseDouble(volume));
                                           openList.add(Double.parseDouble(open));
                                           highList.add(Double.parseDouble(high));
                                           lowList.add(Double.parseDouble(low));

                                       }
                                       double changeprice = priceList.get(0) - priceList.get(1);
                                       change = Math.round(changeprice * 100.0) / 100.0;
                                       changepercent = Math.round((change*100 / priceList.get(1)) * 100.0) / 100.0;
                                       last_Price = Math.round(priceList.get(1) * 100.0) / 100.0;
                                       open = Math.round(openList.get(0) * 100.0) / 100.0;
                                       close = Math.round(priceList.get(0) * 100.0) / 100.0;
                                       high = Math.round(highList.get(0) * 100.0) / 100.0;
                                       low = Math.round(lowList.get(0) * 100.0) / 100.0;
                                       String volume = NumberFormat.getNumberInstance(Locale.US).format(volumeList.get(0));
                                       String last_refresh = meta_data.getString("3. Last Refreshed");
                                    /* if(change < 0){
                                           imag.setImageDrawable(getResources().getDrawable(R.drawable.down));
                                       }*/

                                       String[] valueName = {symbol, last_Price.toString(), change + "(" + changepercent + "%)",
                                               last_refresh, open.toString(), close.toString(), low.toString()
                                               + " - " + high.toString(), volume};
                                       Table tableAdapter = new Table(getActivity(), dataName, valueName);
                                       progressBar.setVisibility(View.INVISIBLE);
                                       Fav.setEnabled(true);
                                       facebook.setEnabled(true);
                                       mListView.setAdapter(tableAdapter);

                                   }


                               } catch (JSONException e) {
                                   e.printStackTrace();
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

// add the request object to the queue to be executed
                       mRequestQueue.add(req);
                   }
               }
        public class WebInterface {
        String mString;
        /** Instantiate the interface and set the context */
        WebInterface(String c) {
            mString = c;
        }



        /** Show a toast from the web page */
        @JavascriptInterface
        public String passData() {
            Log.i("wqwqwqqwqwq","sent");
            return mString;

        }

        @JavascriptInterface
        public void setData(String url){
            responseURL = url;
            Log.i("wqwqwqwq",url);
            progressBar2.setVisibility(View.INVISIBLE);

            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    Message msg = mHandler.obtainMessage();
                    msg.what = 2;
                    msg.sendToTarget();
                }});
            thread.start();

        }

            @JavascriptInterface
            public void InvalidSymbol(String length){
                resLength = length;
                Log.i("data length",resLength);
                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        Message msg = mHandler.obtainMessage();
                        msg.what = 1;
                        msg.sendToTarget();
                    }});
                thread.start();
            }

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       text = parent.getItemAtPosition(position).toString();
        if(text.equals("Price")){
            if(temp == 1){
                changeBTn.setEnabled(true);
            }
        }
        if(text.equals("SMA")){
            temp = 1;
           changeBTn.setEnabled(true);
        }
        if(text.equals("EMA")){
            temp = 1;
            changeBTn.setEnabled(true);

        }
        if(text.equals("STOCH")){
            temp = 1;
            changeBTn.setEnabled(true);

        }
        if(text.equals("RSI")){
            temp = 1;
            changeBTn.setEnabled(true);

        }
        if(text.equals("ADX")){
            temp = 1;
            changeBTn.setEnabled(true);

        }
        if(text.equals("CCI")){
            temp = 1;
            changeBTn.setEnabled(true);

        }
        if(text.equals("BBANDS")){
            temp = 1;
            changeBTn.setEnabled(true);

        }
        if(text.equals("MACD")){
            temp = 1;
            changeBTn.setEnabled(true);

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    }

