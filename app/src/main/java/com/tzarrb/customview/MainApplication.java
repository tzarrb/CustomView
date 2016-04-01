package com.tzarrb.customview;

import android.app.Application;

import com.tzarrb.customview.utils.CrashHandler;

/**
 * Created by dev on 16/3/29.
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //设置异常捕获
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());

    }
}
