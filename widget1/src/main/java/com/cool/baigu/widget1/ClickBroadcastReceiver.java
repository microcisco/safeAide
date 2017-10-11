package com.cool.baigu.widget1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by baigu on 2017/10/10.
 */

public class ClickBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("kaka", "onReceive: 一键清理");


    }

}
