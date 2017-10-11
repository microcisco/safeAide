package com.cool.baigu.safeaide.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.cool.baigu.safeaide.beans.BlackContacts;
import com.cool.baigu.safeaide.dao.Impl.BlackListDAO;

import java.lang.reflect.Method;

import static android.content.ContentValues.TAG;
import static android.provider.Telephony.Sms.Intents.SMS_RECEIVED_ACTION;
import static android.telephony.SmsMessage.createFromPdu;

/**
 * Created by baigu on 2017/9/11.
 */

public class BlackListFillService extends Service {

    private InnerSmsReceive innerSmsReceive;
    private PhoneStateListener phoneStateListener;
    private TelephonyManager tm;
    private BlackListDAO blackListDAO;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate() {
        super.onCreate();
        blackListDAO = new BlackListDAO(getApplicationContext());
        //短信拦截
        innerSmsReceive = new InnerSmsReceive();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(Integer.MAX_VALUE);
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(innerSmsReceive, intentFilter);
        //电话拦截
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        //监听器
        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, final String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                switch (state) {
                    //空闲状态
                    case TelephonyManager.CALL_STATE_IDLE:
                        break;
                    //接通状态
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        break;
                    //响铃状态
                    case TelephonyManager.CALL_STATE_RINGING:
                        String s = blackListDAO.get(new BlackContacts(0, incomingNumber, ""));
                        if ("0".equals(s) || "1".equals(s)) {
                            endCall();
                            //数据库写入监听器
                            Uri uri = Uri.parse("content://call_log/calls");
                            getContentResolver().registerContentObserver(uri, true, new ContentObserver(new Handler()) {
                                @Override
                                public void onChange(boolean selfChange) {
                                    super.onChange(selfChange);
                                    deleteCallLog(incomingNumber);
                                }
                            });
                        }
                        break;
                }
            }
        };
        tm.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    /**
     * 删除通话记录
     * @param phoneStr
     * */
    private void deleteCallLog(String phoneStr) {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = Uri.parse("content://call_log/calls");
        contentResolver.delete(uri, "number=?", new String[]{phoneStr});
    }

    /**
     * 挂断电话
     */
    private void endCall() {
        try {
            Class<?> clzz = String.class.getClassLoader().loadClass("android.os.ServiceManager");
            Method getServiceMethod = clzz.getDeclaredMethod("getService", String.class);
            IBinder b = (IBinder) getServiceMethod.invoke(null, TELEPHONY_SERVICE);
            ITelephony iTelephony = ITelephony.Stub.asInterface(b);
            iTelephony.endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(innerSmsReceive);
        //取消电话状态监听
        tm.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        tm = null;
        phoneStateListener = null;
    }


    class InnerSmsReceive extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String format = intent.getStringExtra("format");
            //判断广播消息
            if (action.equals(SMS_RECEIVED_ACTION)) {
                Bundle bundle = intent.getExtras();
                //如果不为空
                if (bundle != null) {
                    //将pdus里面的内容转化成Object[]数组
                    Object pdusData[] = (Object[]) bundle.get("pdus");// pdus ：protocol data unit  ：
                    //解析短信
                    SmsMessage[] msg = new SmsMessage[pdusData.length];
                    for (int i = 0; i < msg.length; i++) {
                        byte pdus[] = (byte[]) pdusData[i];
                        msg[i] = createFromPdu(pdus, format);
                    }
                    StringBuffer content = new StringBuffer();//获取短信内容
                    StringBuffer phoneNumber = new StringBuffer();//获取地址
                    //分析短信具体参数
                    for (SmsMessage temp : msg) {
                        content.append(temp.getMessageBody());
                        phoneNumber.append(temp.getOriginatingAddress());
                    }
                    //根据黑名单过滤短信
                    String s = blackListDAO.get(new BlackContacts(0, phoneNumber.toString(), ""));
                    //根据model过滤
                    if ("0".equals(s) || "2".equals(s)) {
                        abortBroadcast();  //终止广播
                        Log.d(TAG, "onReceive: 被拦截");
                    }


                }
            }
        }
    }

}
