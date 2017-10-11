package com.cool.baigu.safeaide.dao.Impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cool.baigu.safeaide.Constant;
import com.cool.baigu.safeaide.beans.BlackContacts;
import com.cool.baigu.safeaide.dao.IBlackListDAO;
import com.cool.baigu.safeaide.utils.JdbcUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baigu on 2017/9/7.
 */

public class BlackListDAO implements IBlackListDAO {
    private SQLiteOpenHelper dbHelper = null;
    public BlackListDAO(Context context) {
        dbHelper = JdbcUtils.getSQLiteBlackListDB(context);
    }

    @Override
    public boolean add(BlackContacts blackContacts) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("phone", blackContacts.getPhone());
        contentValues.put("mode", blackContacts.getMode());
        long insert = database.insert(Constant.BLACKNUMBERINFO, null, contentValues);
        database.close();
        return insert != -1;
    }

    @Override
    public boolean remove(BlackContacts blackContacts) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String phone = blackContacts.getPhone();
        int i = database.delete(Constant.BLACKNUMBERINFO, "phone=?", new String[]{phone});
        database.close();
        return i > 0;
    }

    @Override
    public boolean update(BlackContacts blackContacts) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String phone = blackContacts.getPhone();
        ContentValues contentValues = new ContentValues();
        contentValues.put("mode", blackContacts.getMode());
        database.update(Constant.BLACKNUMBERINFO, contentValues, "phone=?", new String[]{phone});
        database.close();
        return false;
    }

    @Override
    public String get(BlackContacts blackContacts) {
        String mode = null;
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String phone = blackContacts.getPhone();
        Cursor query = database.query(Constant.BLACKNUMBERINFO, new String[]{"phone", "mode"}, "phone=?", new String[]{phone}, null, null, null);
        while (query.moveToNext()) {
            int index = query.getColumnIndex("mode");
            mode = query.getString(index);
        }
        query.close();
        database.close();
        return mode;
    }

    @Override
    public List<BlackContacts> getList() {
        ArrayList<BlackContacts> res = new ArrayList<>();

        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(Constant.BLACKNUMBERINFO, new String[]{"phone", "mode"}, "", new String[]{}, null, null, null);
        while (cursor.moveToNext()) {
//            SystemClock.sleep(1000);  //模拟加载延迟
            int modeIndex = cursor.getColumnIndex("mode");
            int phoneIndex = cursor.getColumnIndex("phone");
            String m = cursor.getString(modeIndex);
            String p = cursor.getString(phoneIndex);
            res.add(new BlackContacts(0, p, m));
        }
        cursor.close();
        database.close();
        return res;
    }


}
