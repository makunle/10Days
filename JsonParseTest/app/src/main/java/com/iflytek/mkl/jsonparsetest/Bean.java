package com.iflytek.mkl.jsonparsetest;

import java.util.List;

/**
 * Created by Administrator on 2017/7/28.
 */

public class Bean {

    /**
     * age : 25
     * name : hi
     * point : [1,2,3,4]
     */

    private String age;
    private String name;
    private List<Integer> point;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getPoint() {
        return point;
    }

    public void setPoint(List<Integer> point) {
        this.point = point;
    }
}
