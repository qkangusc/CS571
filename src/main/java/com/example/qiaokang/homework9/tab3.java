package com.example.qiaokang.homework9;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.lang.reflect.Array;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

/**
 * Created by Qiao Kang on 2017/11/21.
 */

public class tab3 extends Fragment{
    private ListView mListView;
    private TextView alertMessage;
    String symbol;
    ArrayList link;
    ArrayList title;
    ArrayList author;
    ArrayList date;
    View rootView;



    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment3_main2, container, false);
            symbol = ((Main2Activity) getActivity()).getSymbol();
            mListView = (ListView) rootView.findViewById(R.id.listview);
            alertMessage = (TextView)rootView.findViewById(R.id.alert);
            alertMessage.setVisibility(View.VISIBLE);
            link = new ArrayList();
            title = new ArrayList();
            author = new ArrayList();
            date = new ArrayList();
            getNewsFeed();
        }
        return rootView;
    }

     public void getNewsFeed(){
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        final String URL = "http://cambridge2-env.us-west-1.elasticbeanstalk.com/json?symbol=" + symbol + "&indicators=News_Feeds";
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                if (response!=null){
                    Log.i("!","get it");
                    alertMessage.setVisibility(View.INVISIBLE);
                    try {
                        JSONObject rss = response.getJSONObject("rss");
                        JSONArray channel = rss.getJSONArray("channel");
                        //Log.i("######","start!!");


                        JSONObject element = channel.getJSONObject(0);
                        //Log.i("obj",element.toString());
                        JSONArray item = element.getJSONArray("item");

                        for (int i = 0; i < item.length(); i++) {
                            JSONObject content = item.getJSONObject(i);
                            JSONArray Link = content.getJSONArray("link");
                            JSONArray Title = content.getJSONArray("title");
                            JSONArray Author = content.getJSONArray("sa:author_name");
                            JSONArray Date = content.getJSONArray("pubDate");
                            if (Link.getString(0).contains("article")) {
                                link.add(Link.getString(0));
                                title.add(Title.getString(0));
                                author.add(Author.getString(0));
                                date.add(Date.getString(0));

                            }
                        }
                        News_Table tableAdapter = new News_Table(getActivity(), link, title, author, date);
                        mListView.setAdapter(tableAdapter);
                        mListView.setOnItemClickListener(new GetNews());


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
                /*else{
                    Log.i("!","none!");
                    alertMessage.setVisibility(View.VISIBLE);
                }*/
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



     class GetNews implements OnItemClickListener {
         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id){
             ViewGroup viewgroup = (ViewGroup) rootView;
             TextView textview = viewgroup.findViewById(R.id.title);
             Uri uri = Uri.parse(textview.getPrivateImeOptions() + "");
             Intent intent = new Intent(Intent.ACTION_VIEW);
             intent.setData(uri);
             startActivity(intent);
         }
     }
}
