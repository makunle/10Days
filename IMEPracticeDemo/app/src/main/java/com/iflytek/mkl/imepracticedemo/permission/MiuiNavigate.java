package com.iflytek.mkl.imepracticedemo.permission;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Administrator on 2017/8/9.
 */

public class MiuiNavigate implements Navigate {

    @Override
    public boolean doNavigate(Context context) {
        if (!isMIUI()) return false;
        switch (miuiVersionName()) {
            case "V8": //V8
                forV8Navigate(context);
                break;
            case "V5":
            case "V6":
                forV5V6Navigate(context);
                break;
            default:

        }
        return true;
    }

    private static void forV8Navigate(Context context) {
        Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        localIntent.setClassName("com.miui.securitycenter",
                "com.miui.permcenter.permissions.PermissionsEditorActivity");
        localIntent.putExtra("extra_pkgname", context.getPackageName());
        context.startActivity(localIntent);
    }

    private static void forV5V6Navigate(Context context) {
        // 只兼容miui v5/v6 的应用权限设置页面，否则的话跳转应用设置页面（权限设置上一级页面）
        Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        localIntent.setClassName("com.miui.securitycenter",
                "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
        localIntent.putExtra("extra_pkgname", context.getPackageName());
        context.startActivity(localIntent);
    }

    // 检测MIUI
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";          //6
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";          //V8
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    private static boolean isMIUI() {
        String device = Build.MANUFACTURER;
        return (device.equals("Xiaomi"));
    }

    private static String miuiVersionName() {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(new File(Environment
                    .getRootDirectory(), "build.prop")));
            return (prop.getProperty(KEY_MIUI_VERSION_NAME));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
