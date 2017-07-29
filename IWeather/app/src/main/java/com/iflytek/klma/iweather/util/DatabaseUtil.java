package com.iflytek.klma.iweather.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.iflytek.klma.iweather.db.County;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * 数据库Util
 */

public class DatabaseUtil {
    private static final String TAG = "IWeather";

    private static  DatabaseUtil instance = new DatabaseUtil();

    public static DatabaseUtil getInstance(){
        return instance;
    }

    /**
     * 如果是apk安装后的首次运行，初始化数据库
     * @param context
     */
    public void firstTimeInitDataBase(Context context){
        String packagename = context.getPackageName();
        String dbFloder = "/data/data/"+packagename+"/databases/";
        String dbName = "iweather.db";
        String dbFilePath = dbFloder + dbName;
        if(!istDatabaseExist(dbFilePath)){
            copyDatabase(dbFloder, dbName, context);
        }
    }

    /**
     * 判断数据库是否存在
     * @param dbFilePath 数据库路径
     * @return
     */
    private boolean istDatabaseExist(String dbFilePath){
        SQLiteDatabase db = null;
        try{

            db = SQLiteDatabase.openDatabase(dbFilePath, null, SQLiteDatabase.OPEN_READONLY);
        }catch (Exception e){
            return false;
        }
        if(db != null){
            db.close();
        }
        return db != null;
    }

    /**
     * 拷贝assets/iweather.db -> /data/data/[packagename]/database/下
     * @param dbFloder
     * @param dbName
     * @param context
     */
    private void copyDatabase(String dbFloder, String dbName, Context context){
        File dir = new File(dbFloder);
        if(!dir.exists()){
            dir.mkdir();
        }
        try {
            OutputStream os = new FileOutputStream(dbFloder + dbName);
            InputStream is = context.getAssets().open(dbName);
            byte[] buffer = new byte[1024];
            int count = 0;
            while((count = is.read(buffer)) > 0){
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
     * @return
     */
    public List<County> getHotCounties(){
        List<County> counties = DataSupport.where("isHot = ? ", "1").find(County.class);
        return counties;
    }
}
