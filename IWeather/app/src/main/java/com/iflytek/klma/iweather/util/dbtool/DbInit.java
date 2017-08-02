package com.iflytek.klma.iweather.util.dbtool;

import android.content.ContentValues;
import android.util.Log;

import com.iflytek.klma.iweather.db.County;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * 初始化以及手工操作数据库
 * 从和风天气拉取全国城市信息，存入数据库a，将a导出后，在之后的工程中同apk打包到一起
 */

public class DbInit {
    private static final String TAG = "IWeather";

    private static String[] hotCityList = {"北京","天津","上海","重庆",
            "沈阳","大连","长春","哈尔滨","郑州","武汉","长沙","广州",
            "合肥","深圳","南京","西安","无锡","常州","苏州","杭州",
            "宁波","济南","青岛","福州","厦门","成都","昆明"};

    public static void makeHotCity(){
//        List<County> counties = DataSupport.where("name = ?", "昆明").find(County.class);
//        for(County c : counties){
//            Log.d(TAG, c.getName() + " " + c.getWeatherId());
//            City city = DataSupport.where("id = ?", c.getCityId()+"").findFirst(City.class);
//            Log.d(TAG, city.getName());
//            Province province = DataSupport.where("id = ?",city.getProvinceId()+"").findFirst(Province.class);
//            Log.d(TAG, province.getName());
//        }

        ContentValues value = new ContentValues();
        value.put("isHot", "1");
        for(String str : hotCityList){
            DataSupport.updateAll(County.class, value, "name = ?", str);
        }

        List<County> counties = DataSupport.findAll(County.class);
        for(County c : counties){
            Log.d(TAG, c.getName() + " "+c.getId() +" "+c.isHot());
        }
//        Province p = new Province();
//        p.setName("pp");
//        p.save();

//        City ci = new City();
//        ci.setName("cii");
//        ci.setProvince(p);
//        ci.save();
//
//        County co = new County();
//        co.setName("coo");
//        co.setCity(ci);
//        co.save();
//
//        County coo = DataSupport.where("name = ?", "coo").findLast(County.class, true);
//        Log.d(TAG, "testDb: "+coo.getCity().getName());
    }

    /**
     * 使用和风天气api，获取城市列表，创建数据库
     */
    public static void initCityDb(){
        LitePal.getDatabase();
        new Thread(new Runnable() {
            @Override
            public void run() {
                new GetCitiList().makeList();
            }
        }).start();
    }

    private static iprovince currentProvince;
    private static icity currentCity;


}
