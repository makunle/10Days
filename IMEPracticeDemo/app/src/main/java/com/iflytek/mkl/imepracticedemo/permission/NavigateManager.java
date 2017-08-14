package com.iflytek.mkl.imepracticedemo.permission;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * app权限设置页跳转
 */

public class NavigateManager {
    private List<Navigate> navigateList = new ArrayList<>();
    private static Context context;
    private static NavigateManager instance = new NavigateManager();

    private NavigateManager(){}

    public synchronized static NavigateManager with(Context context){
        instance.context = context;
        instance.initList();
        return instance;
    }

    //在这里添加需要适配的机型
    private void initList() {
        navigateList.add(new MiuiNavigate());
//        navigateList.add(new EmulatorNavigate());
    }

    public void navigate(){
        for(Navigate navigate : navigateList){
            if(navigate.doNavigate(context)) return;
        }
        new DefaultNavigate().doNavigate(context);
    }
}
