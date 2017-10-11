package com.cool.baigu.safeaide.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cool.baigu.safeaide.Constant;
import com.cool.baigu.safeaide.R;
import com.cool.baigu.safeaide.utils.SPUtils;

/**
 * Created by baigu on 2017/8/25.
 */

public class Func5Main extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.func1_step_5);

        initView();
        initEvent();

    }

    private void initEvent() {
        TextView aq3 = (TextView) findViewById(R.id.aq3);
        aq3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtils.setBoolean(getApplicationContext(), Constant.FUN1FINISH, false);
                Intent intent = new Intent(getApplicationContext(), Func1Main.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * 初始化显示
     */
    private void initView() {
        TextView aq1 = (TextView)findViewById(R.id.aq1);
        ImageView aq2 = (ImageView) findViewById(R.id.aq2);
        aq1.setText(SPUtils.getString(getApplicationContext(), Constant.SAFEPHONE));
        if(SPUtils.getBoolean(getApplicationContext(), Constant.SAFESTART)) {
            aq2.setImageResource(R.drawable.lock);
        } else {
            aq2.setImageResource(R.drawable.unlock);
        }
    }

}
