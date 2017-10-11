package com.cool.baigu.safeaide.recriver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.cool.baigu.safeaide.Constant;
import com.cool.baigu.safeaide.utils.SPUtils;

import static android.content.ContentValues.TAG;

/**
 * Created by baigu on 2017/9/1.
 */

public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //重启检测sim卡是否变化
        if(SPUtils.getBoolean(context, Constant.BINDSIM)) {
            TelephonyManager systemService = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if(systemService.getSimSerialNumber().equals(SPUtils.getString(context, Constant.SIM))) {
                Toast.makeText(context, "369安全卫士正在守护着您的手机", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onReceive: 一致");
            } else {
                //给安全号码发送短信
                SmsManager.getDefault().sendTextMessage(SPUtils.getString(context, Constant.SAFEPHONE), null, "sim卡变了", null, null);
            }
        }
    }
}
