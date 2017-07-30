package com.iflytek.klma.iweather.util;

/**
 * EventBus 数据库增删事件消息
 */

public class DBChangeMsg {
    public static final int ADD = 1;
    public static final int DEL = 2;

    private int bookMarkId;  //对应的WeatherBookmark id
    private int type;           //事件类型

    public int getBookMarkId() {
        return bookMarkId;
    }

    public void setBookMarkId(int bookMarkId) {
        this.bookMarkId = bookMarkId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public DBChangeMsg(int bookMarkId, int type) {

        this.bookMarkId = bookMarkId;
        this.type = type;
    }
}
