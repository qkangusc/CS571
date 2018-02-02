package com.example.qiaokang.homework9;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Qiao Kang on 2017/11/21.
 */

public class tab2 extends Fragment {
    private WebView mWebView;
    private ProgressBar progressBar;
    private TextView alertMessage;
    String symbol;
    View rootView;
    String responseURL, resLength;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1) {
                progressBar.setVisibility(View.INVISIBLE);
                alertMessage.setVisibility(View.VISIBLE);
                //mWebView.setVisibility(View.INVISIBLE);

            }
            if(msg.what==2){
                progressBar.setVisibility(View.INVISIBLE);
                mWebView.setAlpha(1);
            }
            super.handleMessage(msg);
        }
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(rootView == null) {
            rootView = inflater.inflate(R.layout.fragment2_main2, container, false);
            mWebView = (WebView) rootView.findViewById(R.id.webview);
            progressBar = (ProgressBar)rootView.findViewById(R.id.progressBar);
            alertMessage = (TextView) rootView.findViewById(R.id.Alert);
            progressBar.setVisibility(View.VISIBLE);
            //mWebView.setVisibility(View.INVISIBLE);
            alertMessage.setVisibility(View.INVISIBLE);
            symbol = ((Main2Activity) getActivity()).getSymbol();
            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            mWebView.addJavascriptInterface(new WebInterface(symbol), "Android");
            mWebView.loadUrl("file:///android_asset/stock.html");

        }
        return rootView;
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
            return mString;
        }
        @JavascriptInterface
        public void setData(String url){
            responseURL = url;
            Log.i("wqwqwqwq",url);

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

}
