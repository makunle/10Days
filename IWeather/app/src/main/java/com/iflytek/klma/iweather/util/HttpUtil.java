package com.iflytek.klma.iweather.util;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by makunle on 2017/7/29.
 */

public class HttpUtil {

    /**
     * 同步方式获取http数据，职能在非UI线程调用
     * @param url http链接
     * @return 获取的数据
     */
    public static String getResponse(String url){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 异步发起http请求
     * @param url http连接
     * @param callback 返回结果异步处理函数
     */
    public static void sendHttpRequest(String url, Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }
}
