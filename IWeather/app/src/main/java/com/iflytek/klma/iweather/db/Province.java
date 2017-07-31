package com.iflytek.klma.iweather.db;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by makunle on 2017/7/29.
 */

public class Province extends DataSupport {

    private int id;

    private String name;        //省名

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
}
