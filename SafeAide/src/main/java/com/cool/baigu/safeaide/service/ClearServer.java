package com.cool.baigu.safeaide.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.cool.baigu.safeaide.R;

/**
 * Created by baigu on 2017/9/16.
 */

public class ClearServer extends Service {

    private WindowManager mWM;
    private ImageView imageView;
    private float eventRawX;
    private float eventRawY;
    private boolean autoAction = false;
    private WindowManager.LayoutParams params;
    private int heightPixels;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            params.y -= 20;
            mWM.updateViewLayout(imageView, params);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        heightPixels = metrics.heightPixels;

        mWM = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
        params.setTitle("Toast");
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        params.y = 0;
        params.x = 0;

        //显示的view
        imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(R.drawable.frame_anim);
        ((AnimationDrawable) imageView.getDrawable()).start();

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                DisplayMetrics metrics = getResources().getDisplayMetrics();
                final int heightPixels = metrics.heightPixels;
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if(autoAction) {
                            return false;

                        }
                        eventRawX = event.getRawX();
                        eventRawY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if(autoAction) {
                            return false;
                        }
                        //正常拖动
                        float v1 = event.getRawX() - eventRawX;
                        params.x += v1;
                        float v2 = event.getRawY() - eventRawY;
                        params.y += v2;
                        mWM.updateViewLayout(imageView, params);
                        eventRawX = event.getRawX();
                        eventRawY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if(autoAction) {
                            return false;
                        }
                        final float rawY = event.getRawY();
                        //发射火箭
                        if (rawY > heightPixels - v.getHeight() - 50) {
                            autoAction = true;

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    for (int i = 0; i < heightPixels  - 500; ) {
                                        i += 20;
                                        handler.sendEmptyMessage(0);
                                        SystemClock.sleep(100);
                                    }
                                    autoAction = false;
                                }
                            }).start();

                        }
                        break;
                }

                return true;
            }
        });

        mWM.addView(imageView, params);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWM.removeView(imageView);
    }
}
