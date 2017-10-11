package com.cool.baigu.safeaide.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Xml;
import android.view.View;
import android.widget.Button;

import com.cool.baigu.safeaide.R;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by baigu on 2017/9/13.
 */

public class Tools extends Activity {
    Button smsBackup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tools);

        Button but = (Button) findViewById(R.id.but);  //号码归属地查询按钮
        Button but1 = (Button) findViewById(R.id.but1);  //常用号码查询按钮
        smsBackup = (Button) findViewById(R.id.smsBackup);
        //号码归属地查询
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Tools.this, Tool1.class);
                startActivity(intent);
            }
        });
        //常用号码查询
        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Tools.this, Tool2.class);
                startActivity(intent);
            }
        });
        //短信备份
        smsBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentResolver resolver = Tools.this.getContentResolver();
                Uri uri = Uri.parse("content://sms");
                Cursor cursor = resolver.query(uri, new String[]{"_id", "address", "body", "date", "type"}, null, null, null);

                try {
                    File f = new File(Environment.getExternalStorageDirectory(), "smsBackup.xml");
                    FileOutputStream fileOutputStream = new FileOutputStream(f);
                    XmlSerializer xmlSerializer = Xml.newSerializer();
                    xmlSerializer.setOutput(fileOutputStream, "utf-8");
                    xmlSerializer.startDocument("utf-8", true);
                    xmlSerializer.startTag(null, "content");
                    assert cursor != null;
                    while (cursor.moveToNext()) {
                        xmlSerializer.startTag(null, "elem");
                        xmlSerializer.attribute(null, "id", cursor.getString(cursor.getColumnIndex("_id")));

                        xmlSerializer.startTag(null, "address");
                        xmlSerializer.text(cursor.getString(cursor.getColumnIndex("address")));
                        xmlSerializer.endTag(null, "address");

                        xmlSerializer.startTag(null, "body");
                        xmlSerializer.text(cursor.getString(cursor.getColumnIndex("body")));
                        xmlSerializer.endTag(null, "body");

                        xmlSerializer.startTag(null, "date");
                        xmlSerializer.text(cursor.getString(cursor.getColumnIndex("date")));
                        xmlSerializer.endTag(null, "date");

                        xmlSerializer.startTag(null, "type");
                        xmlSerializer.text(cursor.getString(cursor.getColumnIndex("type")));
                        xmlSerializer.endTag(null, "type");

                        xmlSerializer.endTag(null, "elem");
                    }
                    xmlSerializer.endTag(null, "content");
                    xmlSerializer.endDocument();
                    fileOutputStream.close();

                    AlertDialog.Builder builder = new AlertDialog.Builder(Tools.this);
                    builder.setTitle("备份结果");
                    builder.setMessage("备份成功");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } catch (Exception e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Tools.this);
                    builder.setTitle("备份结果");
                    builder.setMessage("备份失败");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    e.printStackTrace();
                }

            }
        });

    }
}
