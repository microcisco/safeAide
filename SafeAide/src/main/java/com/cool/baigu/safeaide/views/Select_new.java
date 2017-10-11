package com.cool.baigu.safeaide.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cool.baigu.safeaide.R;

/**
 * Created by baigu on 2017/9/14.
 */

public class Select_new extends RelativeLayout {

    private String t1;
    private String t2;

    public Select_new(Context context) {
        super(context);
    }

    public Select_new(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        assert attrs != null;
        t1 = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "t1");
        t2 = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "t2");

        initView(context);
    }

    private void initView(Context c) {
        View view = View.inflate(c, R.layout.select_backage_1, this);
        TextView q1 = (TextView) view.findViewById(R.id.q1);
        TextView q2 = (TextView) view.findViewById(R.id.q2);
        q1.setText(t1);
        q2.setText(t2);
    }
}
