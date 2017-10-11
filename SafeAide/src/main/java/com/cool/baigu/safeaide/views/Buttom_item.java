package com.cool.baigu.safeaide.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cool.baigu.safeaide.R;

/**
 * Created by baigu on 2017/8/23.
 */

public class Buttom_item extends LinearLayout {


    public Buttom_item(Context context) {
        this(context, null);
    }

    public Buttom_item(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
        iniData(attrs);
    }

    private void iniData(AttributeSet attrs) {
        String tt = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "tt1");

        ImageView imageView1 = (ImageView) findViewById(R.id.t1);
        ImageView imageView2 = (ImageView) findViewById(R.id.t2);
        ImageView imageView3 = (ImageView) findViewById(R.id.t3);
        ImageView imageView4 = (ImageView) findViewById(R.id.t4);

        imageView1.setImageResource(R.drawable.call_locate_blue);
        imageView2.setImageResource(R.drawable.call_locate_blue);
        imageView3.setImageResource(R.drawable.call_locate_blue);
        imageView4.setImageResource(R.drawable.call_locate_blue);

        if (tt == null) {
            return;
        }
        switch (tt) {
            case "1":
                imageView1.setImageResource(R.drawable.call_locate_green);
                break;
            case "2":
                imageView2.setImageResource(R.drawable.call_locate_green);
                break;
            case "3":
                imageView3.setImageResource(R.drawable.call_locate_green);
                break;
            case "4":
                imageView4.setImageResource(R.drawable.call_locate_green);
                break;
        }

    }

    private void initView() {
        View.inflate(getContext(), R.layout.buttom, this);
    }

}
