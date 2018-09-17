package com.fast.kk.apkupdatelib;

import android.app.Application;

/**
 * application
 */
public class ApkApplication extends Application {

    private static ApkApplication application;

    public static ApkApplication getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        ToastUtils.init(this);
    }

}
