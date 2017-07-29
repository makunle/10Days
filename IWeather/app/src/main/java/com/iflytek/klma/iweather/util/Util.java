package com.iflytek.klma.iweather.util;

import com.iflytek.klma.iweather.db.City;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by makunle on 2017/7/29.
 */

public class Util {
    /**
     * 从数据库获取所有的热门城市列表
     * @return
     */
    public static List<City> getHotCity(){
        return DataSupport.findAll(City.class);
    }



}
