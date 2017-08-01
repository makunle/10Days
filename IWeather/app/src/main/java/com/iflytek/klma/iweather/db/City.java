package com.iflytek.klma.iweather.db;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by makunle on 2017/7/29.
 */

public class City extends DataSupport {

    private int id;

    private String name;            //市名称

    private int provinceId;

    public Province getProvince(){
        return DataSupport.where("id = ?",String.valueOf(provinceId)).findFirst(Province.class);
    }

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

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
