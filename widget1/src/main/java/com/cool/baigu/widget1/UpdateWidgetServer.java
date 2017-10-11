package com.cool.baigu.widget1;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by baigu on 2017/10/10.
 */

public class UpdateWidgetServer extends Service {

    private Timer timer;
    private TimerTask timerTask;
    private AppWidgetManager appWidgetManager;
    int i1 = 12;
    int i2 = 231;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
//        Log.d("kaka", "onCreate: 更改widget服务开启");
//        appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
//        timer = new Timer();
//        timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                Log.d("kaka", "onCreate: 更改widget内容");
//                ComponentName componentName = new ComponentName(getApplication(), CustomWidget.class);
//                RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.example_appwidget);
//                remoteViews.setTextViewText(R.id.i1, "正在运行的软件" + String.valueOf(i1++));  //设置widget组件内容
//                remoteViews.setTextViewText(R.id.i2, "可用内存" + String.valueOf(i2++));  //设置widget组件内容
//                Intent intent = new Intent();
//                intent.setAction("kaka");
//                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                remoteViews.setOnClickPendingIntent(R.id.i3, pendingIntent);  //设置widget点击事件（发送广播）
//                appWidgetManager.updateAppWidget(componentName, remoteViews);
//
//            }
//        };
//        timer.schedule(timerTask, 0, 5000);

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.d("kaka", "onCreate: 更改widget服务关闭");


        super.onDestroy();
    }
}
