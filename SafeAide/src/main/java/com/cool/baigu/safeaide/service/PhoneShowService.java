package com.cool.baigu.safeaide.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.cool.baigu.safeaide.utils.Utils;

/**
 * Created by baiguangan on 2017/9/11
 */

public class PhoneShowService extends Service {

    private InnerTelReceive innerTelReceive;
    private PhoneStateListener listener;
    private TelephonyManager tm;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate() {
        super.onCreate();
        //电话广播
        innerTelReceive = new InnerTelReceive();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PHONE_STATE");
        intentFilter.addAction("android.intent.action.NEW_OUTGOING_CALL");
        registerReceiver(innerTelReceive, intentFilter);
        //来电监听
        tm = (TelephonyManager) getSystemService(Service.TELEPHONY_SERVICE);
        listener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                if (state == TelephonyManager.CALL_STATE_RINGING) {
                    Intent intent1 = new Intent(getApplicationContext(), CustomToastServer.class);
                    intent1.putExtra("address",  Utils.getAddress(getApplicationContext(), incomingNumber));
                    startService(intent1);
                }
                if (state == TelephonyManager.CALL_STATE_IDLE) {
                    Intent intent1 = new Intent(getApplicationContext(), CustomToastServer.class);
                    stopService(intent1);
                }
            }
        };
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消电话状态监听
        unregisterReceiver(innerTelReceive);
        //取消来电监听
        tm.listen(listener, PhoneStateListener.LISTEN_NONE);

    }


    class InnerTelReceive extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //如果是拨打电话
            if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
                String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);

                Intent intent1 = new Intent(context, CustomToastServer.class);
                intent1.putExtra("address",  Utils.getAddress(getApplicationContext(), phoneNumber));
                startService(intent1);

            }
        }
    }
}
