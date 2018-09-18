package com.fast.kk.apkupdatelib.utils;

import android.app.Application;
import android.widget.Toast;

import java.lang.ref.WeakReference;

/**
 * Created by huanghao on 2018/3/28
 * Email:huanghao@xinshiyun.com
 */

public class ToastUtils {

    private static Toast toast = null;
    private static WeakReference<Application> app;


    public static void init(Application application){
        app = new WeakReference<>(application);
    }


    public static void toastShow(String text){
        if(toast==null){
            toast= Toast.makeText(app.get(), text, Toast.LENGTH_SHORT);
        }else{
            toast.setText(text);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    public static void toastLongShow(String text){
        if(toast==null){
            toast= Toast.makeText(app.get(), text, Toast.LENGTH_LONG);
        }else{
            toast.setText(text);
            toast.setDuration(Toast.LENGTH_LONG);
        }
        toast.show();
    }

}
