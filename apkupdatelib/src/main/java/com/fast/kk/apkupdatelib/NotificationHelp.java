package com.fast.kk.apkupdatelib;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationHelp {

    private static final int NOTIFY_ID = 100;

    public static void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "apk_download";
            String channelName = "下载任务";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);

            channelId = "other_message";
            channelName = "通知消息";
            importance = NotificationManager.IMPORTANCE_DEFAULT;
            createNotificationChannel(channelId, channelName, importance);
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private static void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) ApkApplication.getInstance().getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }


    public static void notifyDownloading(int progress, int max, String fileName,int smallIcon) {
        NotificationManager manager = (NotificationManager)ApkApplication.getInstance().getSystemService(NOTIFICATION_SERVICE);

        Notification notification = new NotificationCompat.Builder(ApkApplication.getInstance(), "apk_download")
                .setContentTitle(fileName)
                .setContentText("正在下载:"+progress+"%")
                .setProgress(max,progress,false)
                .setOngoing(true)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(smallIcon)
//                .setLargeIcon(largeIcon)
                .setAutoCancel(false)
                .build();
        manager.notify(NOTIFY_ID, notification);

        if(progress == 100){
            manager.cancel(NOTIFY_ID);
        }
    }
}
