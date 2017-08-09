package com.iflytek.mkl.imepracticedemo.permission;

import android.content.Context;

/**
 * Created by Administrator on 2017/8/9.
 * 需要添加一个适配机型时，实现此接口
 */

public interface Navigate {
    boolean doNavigate(Context context);
}
