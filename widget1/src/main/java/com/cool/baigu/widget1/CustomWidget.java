package com.cool.baigu.widget1;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by baigu on 2017/10/10.
 */

public class CustomWidget extends AppWidgetProvider {
    @Override
    public void onReceive(Context context, Intent intent) {    //创建或删除都会调用
        Log.d("kaka", "onReceive: ");
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) { //创建组件的时候调用
        Log.d("kaka", "onUpdate: ");
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {  //删除组件的时候调用
        Log.d("kaka", "onDeleted: ");
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) { //创建第一个组件的时候调用（从无到有）
        Log.d("kaka", "onEnabled: ");
        super.onEnabled(context);

        Intent service = new Intent(context, UpdateWidgetServer.class);
//        context.startService(service);

    }

    @Override
    public void onDisabled(Context context) {   //删除最后一个组件的时候调用（从有到无）
        Log.d("kaka", "onDisabled: ");
        super.onDisabled(context);
    }

}
