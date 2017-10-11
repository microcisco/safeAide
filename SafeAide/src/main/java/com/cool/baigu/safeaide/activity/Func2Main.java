package com.cool.baigu.safeaide.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cool.baigu.safeaide.Constant;
import com.cool.baigu.safeaide.R;
import com.cool.baigu.safeaide.utils.SPUtils;
import com.cool.baigu.safeaide.utils.Utils;

/**
 * Created by baigu on 2017/8/25.
 */

public class Func2Main extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.func1_step_2);

        initEvent();
        initShow();
    }

    /**
     * 初始化显示（回显）
     */
    private void initShow() {
        TextView tv_bind_sim = (TextView) findViewById(R.id.tv_bind_sim);
        boolean aBoolean = SPUtils.getBoolean(getApplicationContext(), Constant.BINDSIM);
        if (aBoolean) {

            Drawable nav_up=getResources().getDrawable(R.drawable.lock);
            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
            tv_bind_sim.setCompoundDrawables(null, null, nav_up, null);

        } else {

            Drawable nav_up=getResources().getDrawable(R.drawable.unlock);
            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
            tv_bind_sim.setCompoundDrawables(null, null, nav_up, null);

        }
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        ImageView pre = (ImageView) findViewById(R.id.p10);
        ImageView next = (ImageView) findViewById(R.id.p11);
        TextView tv_bind_sim = (TextView) findViewById(R.id.tv_bind_sim);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //没有点击绑定不执行下一步
                if(!SPUtils.getBoolean(getApplicationContext(), Constant.BINDSIM)) {
                    Toast.makeText(getApplicationContext(), "请先绑定sim卡", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getApplicationContext(), Func3Main.class);
                startActivity(intent);

                overridePendingTransition(R.anim.next_in, R.anim.next_out);
                finish();
            }
        });
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Func1Main.class);
                startActivity(intent);

                overridePendingTransition(R.anim.pre_in, R.anim.pre_out);
                finish();
            }
        });
        tv_bind_sim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView tv_bind_sim = (TextView) findViewById(R.id.tv_bind_sim);
                boolean aBoolean = SPUtils.getBoolean(getApplicationContext(), Constant.BINDSIM);
                aBoolean = !aBoolean;
                SPUtils.setBoolean(getApplicationContext(), Constant.BINDSIM, aBoolean);
                if (aBoolean) {

                    Drawable nav_up=getResources().getDrawable(R.drawable.lock);
                    nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                    tv_bind_sim.setCompoundDrawables(null, null, nav_up, null);

                    TelephonyManager systemService = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    String number = systemService.getSimSerialNumber();
                    if(!Utils.isEmpty(number)) {
                        SPUtils.setString(getApplicationContext(), Constant.SIM, number);
                    } else {
                        Toast.makeText(getApplicationContext(), "获取sim卡序列号失败",Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Drawable nav_up=getResources().getDrawable(R.drawable.unlock);
                    nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                    tv_bind_sim.setCompoundDrawables(null, null, nav_up, null);

                }


            }
        });


    }

}
