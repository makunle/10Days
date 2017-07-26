package com.iflytek.klma.httprequestframework;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by makunle on 2017/7/26.
 */

public class VolleyUtil extends Application {

    public static RequestQueue requestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    public static RequestQueue getHttpQueue(){
        return requestQueue;
    }
}
