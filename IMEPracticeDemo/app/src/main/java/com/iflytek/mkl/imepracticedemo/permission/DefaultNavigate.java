package com.iflytek.mkl.imepracticedemo.permission;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

/**
 * Created by Administrator on 2017/8/9.
 */

public class DefaultNavigate implements Navigate {

    @Override
    public boolean doNavigate(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        context.startActivity(localIntent);
        return true;
    }
}
