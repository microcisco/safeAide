package com.cool.baigu.widget1;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    LinearLayout i1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        i1 = (LinearLayout)findViewById(R.id.i1);

        new Thread(new Runnable() {
            @Override
            public void run() {
                exec();
            }
        }).start();


    }

    /**
     * 执行操作
     */
    private void exec() {
        int index = 0;
        String str = "正在查杀36" + "安全卫士";

        for(int i = 0; i < 100; ++i) {
            final TextView textView = new TextView(MainActivity.this);
            textView.setText(str + String.valueOf(index++));
            textView.setTextSize(16);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    i1.addView(textView, 0);  //这里必须要有第二个参数，否则无法有滚动效果
                }
            });
            SystemClock.sleep(100);
        }
    }
}
