package com.cool.baigu.safeaide.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.cool.baigu.safeaide.R;

/**
 * Created by baigu on 2017/8/25.
 */

public class Func1Main extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.func1_step_1);

        initEvent();
    }


    /**
     * 初始化事件
     */
    private void initEvent() {
        ImageView next = (ImageView) findViewById(R.id.p11);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Func2Main.class);
                startActivity(intent);

                overridePendingTransition(R.anim.next_in, R.anim.next_out);
                finish();

            }
        });


    }

}
