package com.iflytek.klma.httprequestframework;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        volley_Get();

        volley_Post();
    }

    private void volley_Post() {
        String url = "http://mobile.weather.com.cn/data/sk/101010100.html?_=1381891661455";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "request failed " +error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("cityid", "CN101220101");
                params.put("key", "7decd6786b9e47ba806484d665f685e6");
                return params;
            }
        };
        request.setTag("abcPost");
        VolleyUtil.getHttpQueue().add(request);
    }

    private void volley_Get() {
        String url = "http://mobile.weather.com.cn/data/sk/101010100.html?_=1381891661455";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "request failed" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        request.setTag("abcGet");
        VolleyUtil.getHttpQueue().add(request);
    }

    @Override
    protected void onStop() {
        super.onStop();
        VolleyUtil.getHttpQueue().cancelAll("abcPost");
        VolleyUtil.getHttpQueue().cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
    }
}
