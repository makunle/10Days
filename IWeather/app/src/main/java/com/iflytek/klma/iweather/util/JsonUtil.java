package com.iflytek.klma.iweather.util;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.iflytek.klma.iweather.gson.HefengWeather;
import com.iflytek.klma.iweather.gson.IflyWeather;
import com.iflytek.klma.iweather.gson.Weather;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by makunle on 2017/7/30.
 */

public class JsonUtil {
    private static final String TAG = "IWeather";
    /**
     * 和风天气json -> object
     * @param json
     * @return 如果不成功返回null
     */
    public static Weather handleHefengJson(String json){
        if(TextUtils.isEmpty(json)) return null;
        try {
            HefengWeather weather = new Gson().fromJson(json, HefengWeather.class);
            if (weather != null && weather.isStatusOk()) return weather;
        }catch (Exception e){}
        return null;
    }

    /**
     * 语义理解天气 json -> object
     * @param json
     * @return
     */
    public static Weather handleIflyWeatherJson(String json){
        IflyWeather weather = new Gson().fromJson(json, IflyWeather.class);
        if(weather.getRc() != 0){
            Log.d(TAG, "handleIflyWeatherJson: is null");
            return null;
        }
        Log.d(TAG, "handleIflyWeatherJson: is not null");
        return weather;
    }

    public static String parseIatResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);

            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // 转写结果词，默认使用第一个结果
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
//				如果需要多候选结果，解析数组其他字段
//				for(int j = 0; j < items.length(); j++)
//				{
//					JSONObject obj = items.getJSONObject(j);
//					ret.append(obj.getString("w"));
//				}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }
}
