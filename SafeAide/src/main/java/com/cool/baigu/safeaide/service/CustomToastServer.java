package com.cool.baigu.safeaide.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.cool.baigu.safeaide.Constant;
import com.cool.baigu.safeaide.R;
import com.cool.baigu.safeaide.utils.SPUtils;

/**
 * Created by baigu on 2017/9/14.
 */

public class CustomToastServer extends Service {

    private WindowManager mWM;
    private View view;
    private TextView address;
    private float preX;
    private float preY;
    private WindowManager.LayoutParams mParams;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String addressStr = intent.getStringExtra("address");

        if(address == null) {
            mWM = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            mParams = new WindowManager.LayoutParams();
            mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            mParams.format = PixelFormat.TRANSLUCENT;
//            mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            //设置显示优先级
            mParams.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
            mParams.setTitle("Toast");
//            mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
//                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
            mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;   //去掉禁止触摸flags
            //新加参数
            mParams.gravity = Gravity.LEFT + Gravity.RIGHT;    //设置默认位置为左上角
            //显示的view
            view = View.inflate(getApplicationContext(), R.layout.custom_toast, null);
            //根据配置文件显示不通背景
            ImageView imageView = (ImageView) view.findViewById(R.id.toastBG);
            int[] ss = new int[]{R.drawable.pkq, R.drawable.pkq1, R.drawable.pkq2};
            int anInt = SPUtils.getInt(getApplicationContext(), Constant.STYLEGUISHUDI);
            imageView.setImageResource(ss[anInt]);

            address = (TextView) view.findViewById(R.id.address);
            mWM.addView(view, mParams);

            //设置触摸事件
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action =event.getAction();
                    switch(action){
                        //当按下的时候
                        case (MotionEvent.ACTION_DOWN):
                            preX = event.getRawX();
                            preY = event.getRawY();
                            break;
                        //当按上的时候
                        case(MotionEvent.ACTION_UP):
                            break;
                        //当触摸的时候
                        case(MotionEvent.ACTION_MOVE):
                            float x = event.getRawX();
                            float y = event.getRawY();
                            float v1 = x - preX;
                            float v2 = y - preY;
                            mParams.x += v1;
                            mParams.y += v2;
                            preX = event.getRawX();
                            preY = event.getRawY();
                            mWM.updateViewLayout(view, mParams);
                            break;
                    }
                    return true;
                }
            });
        }
        address.setText(addressStr);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWM.removeView(view);

    }
}
