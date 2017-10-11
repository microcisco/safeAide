package com.cool.baigu.safeaide.dao.Impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cool.baigu.safeaide.dao.ITeleAttribution;
import com.cool.baigu.safeaide.utils.JdbcUtils;

/**
 * Created by baigu on 2017/9/13.
 */

public class TeleAttribution implements ITeleAttribution {
    private SQLiteDatabase db = null;

    public TeleAttribution(String path) {

        db = JdbcUtils.getDBByPath(path);
    }

    @Override
    public String getAddress4Phone(String id) {
        String address = null;
        Cursor cursor = db.rawQuery("SELECT location FROM data2 WHERE id=(SELECT outkey from data1 WHERE id=?)", new String[]{id});
        while (cursor.moveToNext()) {
            address = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return address;
    }

    @Override
    public String getAddress4Landline(String id) {
        String address = null;
        Cursor cursor = db.rawQuery("SELECT location FROM data2 WHERE area = " + id, null);

        while (cursor.moveToNext()) {
            address = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return address;
    }

}
