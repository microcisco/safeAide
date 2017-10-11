package com.cool.baigu.safeaide.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cool.baigu.safeaide.R;
import com.cool.baigu.safeaide.dao.Impl.TeleAttribution;
import com.cool.baigu.safeaide.utils.NumberUtil;
import com.cool.baigu.safeaide.utils.Utils;

/**
 * Created by baigu on 2017/9/13.
 */

public class Tool1 extends Activity {

    private TextView res;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tool1);

        final EditText input = (EditText) findViewById(R.id.input);  //输入框
        Button action = (Button) findViewById(R.id.action);    //查询按钮
        //显示的查询结果
        res = (TextView) findViewById(R.id.res);

        //点击查询
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = input.getText().toString();
                if (Utils.isEmpty(s)) {
                    Toast.makeText(getApplicationContext(), "您的输入为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                TeleAttribution attribution = new TeleAttribution(Tool1.this.getFilesDir().getAbsolutePath() + "/address.db");
                String address = null;
                //判断号码类型
                NumberUtil.Number number = NumberUtil.checkNumber(s);
                switch (number.getType()) {
                    //手机
                    case CELLPHONE:
                        address = attribution.getAddress4Phone(number.getCode());
                        break;
                    //座机
                    case FIXEDPHONE:
                        address = attribution.getAddress4Landline(number.getCode());
                        break;
                    //无效号码
                    case INVALIDPHONE:
                        Toast.makeText(getApplicationContext(), "请输入正确的号码", Toast.LENGTH_SHORT).show();
                        return;
                }
                res.setText("号码归属地为：" + address);
            }
        });
    }
}
