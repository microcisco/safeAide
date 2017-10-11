package com.cool.baigu.safeaide.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cool.baigu.safeaide.R;
import com.cool.baigu.safeaide.utils.ContactUtils;

import java.util.List;

/**
 * Created by baigu on 2017/8/25.
 */

public class SelectContact extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_contact);


        //获取联系人数据
        final List<ContactUtils.ContactInfo> contactInfos = ContactUtils.readContact(getApplicationContext());

        class ContactAdapter extends BaseAdapter {

            @Override
            public int getCount() {
                return contactInfos.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view;
                if (convertView == null) {
                    view = View.inflate(getApplicationContext(), R.layout.item_contact, null);

                } else {
                    view = convertView;
                }

                TextView q1 = (TextView) view.findViewById(R.id.q1);
                TextView q2 = (TextView) view.findViewById(R.id.q2);
                q1.setText(contactInfos.get(position).name);
                q2.setText(contactInfos.get(position).phone);

                return view;
            }
        }

        ListView contents = (ListView) findViewById(R.id.contents);
        contents.setAdapter(new ContactAdapter());

        contents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("phone", contactInfos.get(position).phone);
                setResult(0, intent);
                finish();

            }
        });

    }


}
