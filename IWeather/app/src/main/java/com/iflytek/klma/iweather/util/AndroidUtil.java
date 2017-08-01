package com.iflytek.klma.iweather.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Administrator on 2017/8/1.
 */

public class AndroidUtil {
    /**
     * 检查是否有需要的对应权限，没有的话尝试申请
     * @param activity
     * @param permission
     * @param pomissionCode
     * @return
     */
    public static boolean checkAndGetPermission(Activity activity, String permission, int pomissionCode){
        if(ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, new String[]{permission}, pomissionCode);
            return false;
        }
        return true;
    }
}
