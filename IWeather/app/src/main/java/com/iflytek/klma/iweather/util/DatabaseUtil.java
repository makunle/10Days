package com.iflytek.klma.iweather.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.util.Log;

import com.iflytek.klma.iweather.db.Alarm;
import com.iflytek.klma.iweather.db.County;
import com.iflytek.klma.iweather.db.WeatherBookmark;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 数据库Util
 */

public class DatabaseUtil {
    private static final String TAG = "IWeather";

    private static DatabaseUtil instance = new DatabaseUtil();

    public static DatabaseUtil getInstance() {
        return instance;
    }

    private Context context;

    /**
     * 如果是apk安装后的首次运行，初始化数据库
     *
     * @param context
     */
    public void firstTimeInitDataBase(Context context) {
        this.context = context;
        String packagename = context.getPackageName();
        String dbFloder = "/data/data/" + packagename + "/databases/";
        String dbName = "iweather.db";
        String dbFilePath = dbFloder + dbName;
        if (!istDatabaseExist(dbFilePath)) {
            copyDatabase(dbFloder, dbName, context);
        }
    }

    /**
     * 判断数据库是否存在
     *
     * @param dbFilePath 数据库路径
     * @return
     */
    private boolean istDatabaseExist(String dbFilePath) {
        SQLiteDatabase db = null;
        try {

            db = SQLiteDatabase.openDatabase(dbFilePath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (Exception e) {
            return false;
        }
        if (db != null) {
            db.close();
        }
        return db != null;
    }

    /**
     * 拷贝assets/iweather.db -> /data/data/[packagename]/database/下
     *
     * @param dbFloder
     * @param dbName
     * @param context
     */
    private void copyDatabase(String dbFloder, String dbName, Context context) {
        File dir = new File(dbFloder);
        if (!dir.exists()) {
            dir.mkdir();
        }
        try {
            OutputStream os = new FileOutputStream(dbFloder + dbName);
            InputStream is = context.getAssets().open(dbName);
            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = is.read(buffer)) > 0) {
                os.write(buffer, 0, count);
                os.flush();
            }
            is.close();
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取热门城市
     *
     * @return
     */
    public List<County> getHotCounties() {
        List<County> counties = DataSupport.where("isHot = ? ", "1").find(County.class);
        return counties;
    }

    /**
     * * 添加一个新的County天气收藏
     *
     * @param countyName
     * @return 是否添加成功
     * @post DBChangeMsg
     */
    public boolean addWeatherBookMark(String countyName) {
        County county = getCountyByName(countyName);
        if (county == null) return false;
        WeatherBookmark weatherBookmark = getWeatherBookMarkByCountyName(countyName);
        if (weatherBookmark != null) {
            EventBus.getDefault().post(new DBChangeMsg(weatherBookmark.getId(), DBChangeMsg.SET, countyName));
            return true;
        }

        List<WeatherBookmark> bookmarks = getAllWeatherBookMark();
        int showOrder = 1;
        if (bookmarks.size() > 0) {
            showOrder = bookmarks.get(bookmarks.size() - 1).getShowOrder() + 1;
        }

        weatherBookmark = new WeatherBookmark();
        weatherBookmark.setCountyId(county.getId());
        weatherBookmark.setUpdateTime(0);
        weatherBookmark.setShowOrder(showOrder);
        weatherBookmark.save();

        EventBus.getDefault().post(new DBChangeMsg(weatherBookmark.getId(), DBChangeMsg.ADD, countyName));

        return true;
    }

    /**
     * 获取所有的天气收藏
     *
     * @return
     */
    public List<WeatherBookmark> getAllWeatherBookMark() {
        return DataSupport.order("showOrder asc").find(WeatherBookmark.class);
    }

    /**
     * 根据县名获取数据库县对象
     *
     * @param countyName
     * @return 如果为null表示未获取到
     */
    public County getCountyByName(String countyName) {
        County county = DataSupport.where("name = ?", countyName).findFirst(County.class);
        return county;
    }

    /**
     * 获取给定县名的天气收藏
     *
     * @param countyName
     * @return
     */
    public WeatherBookmark getWeatherBookMarkByCountyName(String countyName) {
        County county = getCountyByName(countyName);
        if (county == null) return null;

        WeatherBookmark weatherBookmark = DataSupport.where("countyId = ?", "" + county.getId()).findFirst(WeatherBookmark.class);
        return weatherBookmark;
    }

    /**
     * 通过bookmark的id获取缓存的天气json数据
     *
     * @param id
     * @return
     */
    public String getWeatherJsonFromBookmarkId(int id) {
        WeatherBookmark weatherBookmark = DataSupport.where("id = ?", String.valueOf(id)).findFirst(WeatherBookmark.class);
        if (weatherBookmark == null) return null;
        return weatherBookmark.getWeatherData();
    }

    public County getCountyByBookmarkId(int id) {
        WeatherBookmark wb = DataSupport.where("id = ?", String.valueOf(id)).findFirst(WeatherBookmark.class);
        if (wb == null) return null;
        return wb.getCounty();
    }

    /**
     * 通过id获取WeatherBookMark
     *
     * @param id
     * @return @Nullable
     */
    @Nullable
    public WeatherBookmark getWeatherBookmarkById(int id) {
        return DataSupport.where("id = ?", String.valueOf(id)).findFirst(WeatherBookmark.class);
    }

    public void updateWeatherBookMarkShowOrder(int id, int order) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("showOrder", order);
        DataSupport.update(WeatherBookmark.class, contentValues, id);
    }

    public boolean deleteWeatherBookMarkByCountyName(String countyName) {
        WeatherBookmark bookmark = getWeatherBookMarkByCountyName(countyName);
        if (bookmark == null) return false;
        bookmark.delete();
        EventBus.getDefault().post(new DBChangeMsg(bookmark.getId(), DBChangeMsg.DEL, countyName));

        List<Alarm> alarms = getAllAlarmByWeatherBookmarkId(bookmark.getId());
        for (Alarm alarm : alarms) {
            alarm.delete();
        }

        return true;
    }

    public List<Alarm> getAllAlarmByWeatherBookmarkId(int bookmarkId) {
        return DataSupport.where("weatherBookmarkId = ?", String.valueOf(bookmarkId)).order("alarmTime asc").find(Alarm.class);
    }

    /**
     * 添加一个alarm事件到数据库
     *
     * @param alarmTime
     * @param bookmarkId
     * @return
     * @post AlarmChangeMsg
     */
    public boolean addAlarm(long alarmTime, int bookmarkId) {
        if (getWeatherBookmarkById(bookmarkId) == null) return false;

        Alarm alarm = new Alarm();
        alarm.setAlarmTime(alarmTime);
        alarm.setWeatherBookmarkId(bookmarkId);
        alarm.save();

        AlarmChangeMsg changeMsg = new AlarmChangeMsg(alarm.getId(), AlarmChangeMsg.ADD, alarm.getAlarmTime(), alarm.isRepeat());
        EventBus.getDefault().post(changeMsg);
        return true;
    }

    /**
     * 通过id删除一个alarm
     *
     * @param alarmId
     * @return
     */
    public boolean deleteAlarmById(int alarmId) {
        Alarm alarm = DataSupport.where("id = ?", String.valueOf(alarmId)).findFirst(Alarm.class);
        if (alarm == null) return false;
        alarm.delete();
        EventBus.getDefault().post(new AlarmChangeMsg(alarmId, AlarmChangeMsg.DEL, alarm.getAlarmTime(), alarm.isRepeat()));
        return true;
    }

    public List<County> getAllCountyNameLike(String str) {
        return DataSupport.where("name like '%" + str + "%'").find(County.class);
    }

    public Alarm getNearestAlarm() {
        Date date = new Date();
        Alarm alarm = DataSupport.order("alarmTime asc").findFirst(Alarm.class);
        return alarm;
    }

    public Alarm getAlarmById(int alarmId) {
        List<Alarm> alarms = DataSupport.findAll(Alarm.class);
        return DataSupport.where("id = ?", String.valueOf(alarmId)).findFirst(Alarm.class);
    }

    /**
     * 执行alarm清理工作，没有设置repeat的过期alarm将被删除
     */
    public void cleanAlarm() {
        long now = Calendar.getInstance().getTimeInMillis();
        List<Alarm> alarms = DataSupport.where("alarmTime < ?", String.valueOf(now)).find(Alarm.class);
        for (Alarm alarm : alarms) {
            if (alarm.isRepeat()) { //设置重复的添加下一次alarm,按天循环
                addAlarm(alarm.getAlarmTime() + 1000 * 60 * 60 * 24, alarm.getWeatherBookmarkId());
            }
            EventBus.getDefault().post(new AlarmChangeMsg(alarm.getId(), AlarmChangeMsg.DEL, alarm.getAlarmTime(), alarm.isRepeat()));
            alarm.delete();
        }
    }
}
