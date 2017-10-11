package com.cool.baigu.safeaide.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cool.baigu.safeaide.Constant;

/**
 * Created by baigu on 2017/9/7.
 */

public class JdbcUtils {
    private static final String sqlite_db_name = "blackList.db";
    private static final int sqlite_db_version = 1;

    //sqlite帮助类
    static class SQLiteHelper extends SQLiteOpenHelper {
        public SQLiteHelper(Context context) {
            super(context, sqlite_db_name, null, sqlite_db_version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table "+ Constant.BLACKNUMBERINFO + "(_id integer primary key autoincrement,phone varchar(20),mode varchar(2) )");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    /**
     * 获取黑名单数据库
     *
     * @param context
     * @return
     */
    public static SQLiteOpenHelper getSQLiteBlackListDB(Context context) {
        return new SQLiteHelper(context);
    }

    /**
     * 获取数据库
     *
     * */
    public static SQLiteDatabase getDBByPath(String path) {
        return SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
    }

}
