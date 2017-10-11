package com.cool.baigu.safeaide.dao.Impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cool.baigu.safeaide.dao.ITeleCommon;
import com.cool.baigu.safeaide.utils.JdbcUtils;

/**
 * Created by baigu on 2017/9/18.
 */

public class TeleCommon implements ITeleCommon {

    private final SQLiteDatabase db;

    public TeleCommon(String path) {
        db = JdbcUtils.getDBByPath(path);
    }

    @Override
    public int getBigNum() {
        int num = 0;
        Cursor cursor = db.rawQuery("SELECT count(*) as num FROM classlist", null);
        while (cursor.moveToNext()) {
            num = cursor.getInt(0);
        }
        cursor.close();
//        db.close();
        return num;
    }

    @Override
    public int getChildNum(int id) {
        int num = 0;
        Cursor cursor = db.rawQuery("SELECT count(*) as num FROM table" + id, null);
        while (cursor.moveToNext()) {
            num = cursor.getInt(0);
        }
        cursor.close();
//        db.close();
        return num;
    }

    @Override
    public String getBigName(int id) {
        int _id = 1;
        String resStr = "          ";
        Cursor cursor = db.rawQuery("SELECT `name` FROM classlist", null);
        while (cursor.moveToNext()) {
            if(_id == id) {
                resStr += cursor.getString(cursor.getColumnIndex("name"));
            }
            ++_id;
        }
        cursor.close();
//        db.close();
        return resStr;
    }

    @Override
    public String getChildItems(int id, int id2) {
        int _id = 1;
        String resStr = "";
        Cursor cursor = db.rawQuery("SELECT number ,`name` FROM table" + id, null);
        while (cursor.moveToNext()) {
            if(id2 == _id) {
                resStr += cursor.getString(cursor.getColumnIndex("name"));
                resStr += "\n";
                resStr += cursor.getString(cursor.getColumnIndex("number"));
            }
            ++_id;
        }
        cursor.close();
//        db.close();
        return resStr;
    }
}
