package com.iflytek.klma.iweather.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 用于将
 */

public class DatabaseUtil {
    private static final String TAG = "IWeather";

    private Context context;
    private String packagename;
    private String DATABASE_PATH;
    private static final String dbName = "iweather.db";
    private String dbFilePath;

    public DatabaseUtil(Context context){
        this.context = context;
        packagename = context.getPackageName();
        DATABASE_PATH = "/data/data/"+packagename+"/databases/";
        dbFilePath = DATABASE_PATH + dbName;
    }
    /**
     * apk安装后首次运行时初始化数据库数据
     */
    public void firstTimeInitDataBase(){
        if(!istDatabaseExist()){
            copyDatabase();
        }
    }

    public boolean istDatabaseExist(){
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

    private void copyDatabase(){
        File dir = new File(DATABASE_PATH);
        if(!dir.exists()){
            dir.mkdir();
        }
        try {
            OutputStream os = new FileOutputStream(dbFilePath);
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
}
