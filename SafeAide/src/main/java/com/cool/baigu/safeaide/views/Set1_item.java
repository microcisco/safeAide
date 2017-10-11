package com.cool.baigu.safeaide.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cool.baigu.safeaide.R;

/**
 * Created by baigu on 2017/8/23.
 */

public class Set1_item extends LinearLayout {


    public Set1_item(Context context) {
        this(context, null);
    }

    public Set1_item(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
        iniData(attrs);
    }

    private void iniData(AttributeSet attrs) {
        String tt = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "tt");

        TextView t1 = (TextView) findViewById(R.id.t1);

        if (tt != null) {
            t1.setText(tt);
        }

    }


    private void initView() {
        View.inflate(getContext(), R.layout.item_set, this);
    }


}
