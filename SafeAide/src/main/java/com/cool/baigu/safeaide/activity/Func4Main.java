package com.cool.baigu.safeaide.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cool.baigu.safeaide.Constant;
import com.cool.baigu.safeaide.R;
import com.cool.baigu.safeaide.utils.SPUtils;

import static com.cool.baigu.safeaide.Constant.SAFESTART;

/**
 * Created by baigu on 2017/8/25.
 */

public class Func4Main extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.func1_step_4);

        initEvent();
        //
        final CheckBox fdbh2 = (CheckBox)findViewById(R.id.fdbh2);
        TextView id = (TextView) findViewById(R.id.fdbhr);
        boolean b = SPUtils.getBoolean(getApplicationContext(), SAFESTART);
        fdbh2.setChecked(b);
        if(b) {
            id.setText("防盗保护已打开");
        } else {
            id.setText("防盗保护已关闭");
        }
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        final CheckBox fdbh2 = (CheckBox)findViewById(R.id.fdbh2);
        LinearLayout fdbh1 = (LinearLayout)findViewById(R.id.fdbh1);
        fdbh2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView id = (TextView) findViewById(R.id.fdbhr);
                if(fdbh2.isChecked()) {
                    SPUtils.setBoolean(getApplicationContext(), SAFESTART, true);
                    id.setText("防盗保护已打开");
                } else {
                    SPUtils.setBoolean(getApplicationContext(), SAFESTART, false);
                    id.setText("防盗保护已关闭");
                }
            }
        });
        fdbh1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView id = (TextView) findViewById(R.id.fdbhr);
                fdbh2.setChecked(!fdbh2.isChecked());
                if(fdbh2.isChecked()) {
                    SPUtils.setBoolean(getApplicationContext(), SAFESTART, true);
                    id.setText("防盗保护已打开");
                } else {
                    SPUtils.setBoolean(getApplicationContext(), SAFESTART, false);
                    id.setText("防盗保护已关闭");
                }
            }
        });


        ImageView pre = (ImageView) findViewById(R.id.p10);
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Func3Main.class);
                startActivity(intent);

                overridePendingTransition(R.anim.pre_in, R.anim.pre_out);
                finish();
            }
        });
        ImageView next = (ImageView) findViewById(R.id.p11);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtils.setBoolean(getApplicationContext(), Constant.FUN1FINISH, true);
                Intent intent = new Intent(getApplicationContext(), Func5Main.class);
                startActivity(intent);

                overridePendingTransition(R.anim.next_in, R.anim.next_out);
                finish();
            }
        });

    }

}
