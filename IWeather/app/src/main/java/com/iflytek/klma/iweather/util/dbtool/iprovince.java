package com.iflytek.klma.iweather.util.dbtool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by makunle on 2017/7/29.
 */

public class iprovince {
    /**
     * id : 1
     * name : 北京
     */

    private int id;
    private String name;

    public List<icity> icities = new ArrayList<>();

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
