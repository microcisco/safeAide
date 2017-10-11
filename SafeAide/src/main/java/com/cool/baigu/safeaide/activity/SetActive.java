package com.cool.baigu.safeaide.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cool.baigu.safeaide.Constant;
import com.cool.baigu.safeaide.R;
import com.cool.baigu.safeaide.service.BlackListFillService;
import com.cool.baigu.safeaide.service.PhoneShowService;
import com.cool.baigu.safeaide.utils.SPUtils;
import com.cool.baigu.safeaide.utils.Utils;
import com.cool.baigu.safeaide.views.Select_new;
import com.cool.baigu.safeaide.views.Setting_item;

/**
 * Created by baigu on 2017/8/23.
 */

public class SetActive extends Activity{
    private Setting_item id2;
    private Setting_item id3;
    private Select_new select_new;
    private AlertDialog dialog;
    private String[] strings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_setting);

        initShow();
        initEvent();
    }

    private void initShow() {
        strings = new String[]{"风格1", "风格2","风格3",};
        select_new = (Select_new) findViewById(R.id.ss1);
        boolean aBoolean = SPUtils.getBoolean(SetActive.this, Constant.CHECKUPDATE);

        ImageView imageView = (ImageView) (findViewById(R.id.s1)).findViewById(R.id.statue);
        if(aBoolean) {
            imageView.setImageResource(R.drawable.on);
        } else {
            imageView.setImageResource(R.drawable.off);
        }

        //回显选择的风格
        ((TextView) select_new.findViewById(R.id.q2)).setText(strings[SPUtils.getInt(getApplicationContext(), Constant.STYLEGUISHUDI)]);
    }

    private void initEvent() {
        Setting_item id1 = (Setting_item) findViewById(R.id.s1);
        id2 = (Setting_item) findViewById(R.id.s2);
        id3 = (Setting_item) findViewById(R.id.s3);
        Setting_item id4 = (Setting_item) findViewById(R.id.s4);

        //开启自动更新
        id1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean aBoolean = SPUtils.getBoolean(SetActive.this, Constant.CHECKUPDATE);
                aBoolean = !aBoolean;
                SPUtils.setBoolean(SetActive.this, Constant.CHECKUPDATE, aBoolean);
                ImageView imageView = (ImageView) (findViewById(R.id.s1)).findViewById(R.id.statue);
                if(aBoolean) {
                    imageView.setImageResource(R.drawable.on);
                } else {
                    imageView.setImageResource(R.drawable.off);
                }
            }
        });
        //开启黑名单拦截
        id2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean life = Utils.isLife(SetActive.this, "com.cool.baigu.safeaide.service.BlackListFillService");
                ImageView imageView = (ImageView) id2.findViewById(R.id.statue);
                if (!life) {    //开启服务
                    imageView.setImageResource(R.drawable.on);
                    Intent intent = new Intent(SetActive.this, BlackListFillService.class);
                    startService(intent);
                } else {        //关闭服务
                    imageView.setImageResource(R.drawable.off);
                    Intent intent = new Intent(SetActive.this, BlackListFillService.class);
                    stopService(intent);
                }
            }
        });
        //开启号码归属地显示
        id3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean life = Utils.isLife(SetActive.this, "com.cool.baigu.safeaide.service.PhoneShowService");
                ImageView imageView = (ImageView) id3.findViewById(R.id.statue);
                if (!life) {    //开启服务
                    imageView.setImageResource(R.drawable.on);
                    Intent intent = new Intent(SetActive.this, PhoneShowService.class);
                    startService(intent);
                } else {        //关闭服务
                    imageView.setImageResource(R.drawable.off);
                    Intent intent = new Intent(SetActive.this, PhoneShowService.class);
                    stopService(intent);
                }
            }
        });
        //开启程序锁看门狗
        id4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //归属地风格按钮
        select_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b = new AlertDialog.Builder(SetActive.this);
                b.setTitle("请选择风格");
                b.setSingleChoiceItems(strings, SPUtils.getInt(getApplicationContext(), Constant.STYLEGUISHUDI), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int hich) {
                        SPUtils.setInt(getApplicationContext(), Constant.STYLEGUISHUDI, hich);
                        ((TextView) select_new.findViewById(R.id.q2)).setText(strings[SPUtils.getInt(getApplicationContext(), Constant.STYLEGUISHUDI)]);
                        dialog.dismiss();
                    }
                });
                dialog = b.show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        boolean life = Utils.isLife(SetActive.this, "com.cool.baigu.safeaide.service.BlackListFillService");
        ImageView imageView = (ImageView) id2.findViewById(R.id.statue);
        if (life) {
            imageView.setImageResource(R.drawable.on);
        } else {
            imageView.setImageResource(R.drawable.off);
        }

        boolean life1 = Utils.isLife(SetActive.this, "com.cool.baigu.safeaide.service.PhoneShowService");
        ImageView imageView1 = (ImageView) id3.findViewById(R.id.statue);
        if (life1) {
            imageView1.setImageResource(R.drawable.on);
        } else {
            imageView1.setImageResource(R.drawable.off);
        }

    }
}
