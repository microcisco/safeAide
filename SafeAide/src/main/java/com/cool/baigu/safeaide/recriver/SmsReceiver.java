package com.cool.baigu.safeaide.recriver;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import com.cool.baigu.safeaide.Constant;
import com.cool.baigu.safeaide.R;
import com.cool.baigu.safeaide.utils.SPUtils;

import static android.content.Context.DEVICE_POLICY_SERVICE;

/**
 * Created by baigu on 2017/9/4.
 */

public class SmsReceiver extends BroadcastReceiver {
    private static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @SuppressLint("NewApi")
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
                    msg[i] = SmsMessage.createFromPdu(pdus, format);
                }
                StringBuffer content = new StringBuffer();//获取短信内容
                StringBuffer phoneNumber = new StringBuffer();//获取地址
                //分析短信具体参数
                for (SmsMessage temp : msg) {
                    content.append(temp.getMessageBody());
                    phoneNumber.append(temp.getOriginatingAddress());
                }
                //可用于发命令执行相应的操作
                if ("#*location*#".equals(content.toString().trim())) {
                    if (!isOPen(context)) {
                        openGPS(context);
                    }
                    LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                    Criteria criteria = new Criteria();
                    criteria.setAccuracy(Criteria.ACCURACY_FINE);
                    criteria.setPowerRequirement(Criteria.POWER_HIGH);
                    String bestProvider = locationManager.getBestProvider(criteria, true);
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    Location lastKnownLocation = locationManager.getLastKnownLocation(bestProvider);
                    //给安全号码发送短信
                    SmsManager.getDefault().sendTextMessage(SPUtils.getString(context, Constant.SAFEPHONE), null, showLocation(lastKnownLocation), null, null);
                    abortBroadcast();//截断短信广播
                } else if ("#*alarm*#".equals(content.toString().trim())) {
                    MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.zm);
                    mediaPlayer.setVolume(1.0f, 1.0f);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                    abortBroadcast();//截断短信广播
                } else if ("#*wipe*#".equals(content.toString().trim())) {
                    DevicePolicyManager apm = (DevicePolicyManager) context.getSystemService(DEVICE_POLICY_SERVICE);
                    ComponentName componentName = new ComponentName(context, DevReceiver.class);
                    boolean b = apm.isAdminActive(componentName);
                    if (b) {
                        apm.wipeData(DevicePolicyManager.WIPE_RESET_PROTECTION_DATA);
                    }
                    abortBroadcast();//截断短信广播
                } else if ("#*lockscreen*#".equals(content.toString().trim())) {
                    DevicePolicyManager apm = (DevicePolicyManager) context.getSystemService(DEVICE_POLICY_SERVICE);
                    ComponentName componentName = new ComponentName(context, DevReceiver.class);
                    boolean b = apm.isAdminActive(componentName);
                    if(b) {
                        apm.lockNow();
                        apm.resetPassword("1111", 0);
                    }
                    abortBroadcast();//截断短信广播
                }

            }
        }
    }

    /**
     * 显示地理位置经度和纬度信息
     * @param location
     */
    private String showLocation(Location location){
        return "维度：" + location.getLatitude() +"\n"
                + "经度：" + location.getLongitude();
    }
    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     * @param context
     * @return true 表示开启
     */
    public static final boolean isOPen(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }

        return false;
    }
    /**
     * 强制帮用户打开GPS
     * @param context
     */
    public static final void openGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }
}
