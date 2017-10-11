package com.cool.baigu.safeaide.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cool.baigu.safeaide.R;

/**
 * Created by baigu on 2017/8/23.
 */

public class Setting_item extends RelativeLayout {
    public Setting_item(Context context) {
        this(context, null);
    }

    public Setting_item(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        iniData(attrs);
    }

    private void iniData(AttributeSet attrs) {
        String funcName = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "funcName");
        String funcStyle = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "funcStyle");

        TextView m_funcName = (TextView) findViewById(R.id.funcName);
        RelativeLayout m_funcStyle = (RelativeLayout) findViewById(R.id.funcStyle);

        if(funcName != null) {
            m_funcName.setText(  funcName  );
        }

        if(funcStyle != null) {
            switch (Integer.parseInt(funcStyle)) {
                case 1:
                    m_funcStyle.setBackgroundResource(R.drawable.rl_setting_first_select);
                    break;
                case 3:
                    m_funcStyle.setBackgroundResource(R.drawable.rl_setting_last_select);
                    break;
                default:
                    m_funcStyle.setBackgroundResource(R.drawable.rl_setting_middle_select);
                    break;
            }
        }

    }


    private void initView() {
        View.inflate(getContext(), R.layout.item_setting, this);
    }


}
