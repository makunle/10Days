package com.iflytek.klma.iweather.gson;

/**
 * Created by Administrator on 2017/8/3.
 */

public interface Weather {
    /**
     * 天气情况
     * @return 晴\多云\..
     */
    public String getInfo();

    /**
     * 获取当前气温
     * @return
     */
    public String getNowTemperature();

    /**
     * 最低温度
     * @return
     */
    public String getMinTemperature();

    /**
     * 最高温度
     * @return
     */
    public String getMaxTemperature();

    /**
     * 风向
     * @return
     */
    public String getWindDirect();

    /**
     * 风速等级
     * @return
     */
    public String getWindLevel();

    /**
     * 数据更新时间
     * @return
     */
    public String getUpdateTime();

    public String getCountyName();
}
