package com.me.bui.videoapp.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.me.bui.videoapp.Config;
import com.me.bui.videoapp.model.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mao.bui on 5/13/2018.
 */
public class ListVideoLiveData extends MutableLiveData<List<Video>> {
    private static final String TAG =  ListVideoLiveData.class.getSimpleName();

    private Context mContext;
    private static List<Video> mVideoList = new ArrayList<Video>();

    public ListVideoLiveData(Context context, int type, String query) {
        this.mContext = context;
        String url = getURL(type, query);
        Log.e(TAG, "Query url : " + url);
        loaData(mContext, url);
    }

    private String  getURL (int type, String query) {
        Uri.Builder builder = Uri.parse(Config.BASE_URL_QUERY).buildUpon();

        switch (type) {
            case Config.TYPE_VIDEO_CHANNEL:
                builder.appendQueryParameter("part", "snippet,id");
                builder.appendQueryParameter("order", "date");
                builder.appendQueryParameter("maxResults", "20");
                builder.appendQueryParameter("channelId", query);
                break;
            case Config.TYPE_VIDEO_RELATE:
                builder.appendQueryParameter("part", "snippet");
                builder.appendQueryParameter("type", "video");
                builder.appendQueryParameter("relatedToVideoId", query);
                break;
        }
        return builder.build().toString();
    }
    public void loaData(Context context, String url) {
        mVideoList.clear();// delete old list.
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("items");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        JSONObject jsonVideoId = jsonObject1.getJSONObject("id");
                        JSONObject jsonsnippet = jsonObject1.getJSONObject("snippet");
                        JSONObject jsonObjectdefault = jsonsnippet.getJSONObject("thumbnails").getJSONObject("default");

                        String videoid = jsonVideoId.getString("videoId");
                        String url = jsonObjectdefault.getString("url");
                        String title = jsonsnippet.getString("title");
                        String description = jsonsnippet.getString("description");

                        Log.e(TAG, " New Video Id" + videoid);
                        mVideoList.add(new Video(videoid, title, description, url));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setValue(mVideoList);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }
}
