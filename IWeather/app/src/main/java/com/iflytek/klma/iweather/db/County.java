package com.iflytek.klma.iweather.db;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * Created by makunle on 2017/7/29.
 */

public class County extends DataSupport {

    private int id;

    private String name;        //县名

    private String weatherId;   //和风天气查询id

    @Column(defaultValue = "False")
    private boolean isHot;      //是否为热点城市

    private int cityId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public boolean isHot() {
        return isHot;
    }

    public void setHot(boolean hot) {
        isHot = hot;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
