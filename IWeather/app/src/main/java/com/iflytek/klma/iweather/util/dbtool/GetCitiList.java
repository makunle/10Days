package com.iflytek.klma.iweather.util.dbtool;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iflytek.klma.iweather.db.City;
import com.iflytek.klma.iweather.db.County;
import com.iflytek.klma.iweather.db.Province;
import com.iflytek.klma.iweather.util.HttpUtil;
import com.iflytek.klma.iweather.util.dbtool.icity;
import com.iflytek.klma.iweather.util.dbtool.icounty;
import com.iflytek.klma.iweather.util.dbtool.iprovince;

import java.util.List;

/**
 * Created by makunle on 2017/7/29.
 */

public class GetCitiList {

    private static final String TAG = "IWeather";

    private static final String url = "http://guolin.tech/api/china/";

    public void makeList(){
        String json = HttpUtil.getResponse(url);
        List<iprovince> iprovinces = new Gson().fromJson(json, new TypeToken<List<iprovince>>(){}.getType());
        for(iprovince ip : iprovinces){
            Province province = new Province();
            province.setName(ip.getName());
            province.save();

            makeCities(province, url + "/" + ip.getId());
        }

        Log.d(TAG, "makeList over");
    }

    private void makeCities(Province province, String url) {
        String json = HttpUtil.getResponse(url);
        List<icity> icities = new Gson().fromJson(json, new TypeToken<List<icity>>(){}.getType());
        for(icity ic : icities){
            City city = new City();
            city.setName(ic.getName());
            city.setProvinceId(province.getId());
            city.save();

            makeCounties(city, url + "/" + ic.getId());
            city = null;
        }
    }

    private void makeCounties(City city, String url) {
        String json = HttpUtil.getResponse(url);
        List<icounty> icounties = new Gson().fromJson(json, new TypeToken<List<icounty>>(){}.getType());
        for(icounty ic : icounties){
            County county = new County();
            county.setName(ic.getName());
            county.setWeatherId(ic.getWeather_id());
            county.setCityId(city.getId());
            county.save();

            Log.d(TAG, county.getName() + " " + county.getWeatherId());
            county = null;
        }
    }
}
