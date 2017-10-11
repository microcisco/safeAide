package com.cool.baigu.safeaide.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
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

public class Func3Main extends Activity {
    private EditText hh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.func1_step_3);

        initEvent();

        //初始化显示
        hh.setText(SPUtils.getString(getApplicationContext(), Constant.SAFEPHONE));
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        ImageView pre = (ImageView) findViewById(R.id.p10);
        ImageView next = (ImageView) findViewById(R.id.p11);
        TextView select_safe_code = (TextView) findViewById(R.id.select_safe_code);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.isEmpty(hh.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "安全号码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                //保存安全号码
                SPUtils.setString(getApplicationContext(), Constant.SAFEPHONE, hh.getText().toString());

                Intent intent = new Intent(getApplicationContext(), Func4Main.class);
                startActivity(intent);

                overridePendingTransition(R.anim.next_in, R.anim.next_out);
                finish();
            }
        });
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Func2Main.class);
                startActivity(intent);

                overridePendingTransition(R.anim.pre_in, R.anim.pre_out);
                finish();
            }
        });

        select_safe_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("content://contacts/people");
                Intent intent = new Intent(Intent.ACTION_PICK, uri);
                startActivityForResult(intent, 0);

                //自定义选择联系人界面
//                Intent intent = new Intent(getApplicationContext(), SelectContact.class);
//                startActivityForResult(intent, 1);


            }
        });

         hh = (EditText) findViewById(R.id.hh);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 0:
                if(data==null) { return; }
                //处理返回的data,获取选择的联系人信息
                Uri uri=data.getData();
                String[] contacts=getPhoneContacts(uri);

                assert contacts != null;
                hh.setText(contacts[1].replace("-", ""));

                break;

            case 1:
                String phone = data.getStringExtra("phone");
                hh.setText(phone);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private String[] getPhoneContacts(Uri uri){
        String[] contact=new String[2];
        //得到ContentResolver对象
        ContentResolver cr = getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor=cr.query(uri,null,null,null,null);
        if(cursor!=null) {
            cursor.moveToFirst();
            //取得联系人姓名
            int nameFieldColumnIndex=cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            contact[0]=cursor.getString(nameFieldColumnIndex);
            //取得电话号码
            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
            if(phone != null){
                phone.moveToFirst();
                contact[1] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
            assert phone != null;
            phone.close();
            cursor.close();
        } else {
            return null;
        }
        return contact;
    }


}
