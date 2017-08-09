package com.iflytek.mkl.imepracticedemo.permission;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2017/8/9.
 */

public class EmulatorNavigate implements Navigate {
    @Override
    public boolean doNavigate(Context context) {
        Intent localIntent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        localIntent.setClassName("com.google.android.packageinstaller",
                "com.android.packageinstaller.permission.ui.ManagePermissionsActivity");
        localIntent.putExtra("extra_pkgname", context.getPackageName());
        context.startActivity(localIntent);
        return true;
    }
}
