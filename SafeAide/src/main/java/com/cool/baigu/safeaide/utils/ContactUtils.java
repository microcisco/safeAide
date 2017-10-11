package com.cool.baigu.safeaide.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baigu on 2017/8/30.
 */

public class ContactUtils {
    public static List<ContactInfo> readContact(Context c) {
//        ContentResolver contentResolver = c.getContentResolver();
//        Uri url_data = Uri.parse("content://com.android.contacts/data");
//        Uri url_raw_contacts = Uri.parse("content://com.android.contacts/raw_contacts");
//        Cursor query = contentResolver.query(url_data, null, null, null, null);
//        assert query != null;
//        while (query.moveToNext()) {
//            String id = query.getString(0);
//            if (!Utils.isEmpty(id)) {
//                Cursor query1 = contentResolver.query(url_raw_contacts, new String[]{"mimetype,data1"}, "raw_contact_id=?", new String[]{id}, null);
//                assert query1 != null;
//                while (query1.moveToNext()) {
//                    String string0 = query1.getString(0);
//                    String string1 = query1.getString(1);
//                    Log.d(TAG, "readContact: 11" + string0);
//                    Log.d(TAG, "readContact: 222" + string0);
//                }
//            }
//        }
        //假数据
        ArrayList<ContactInfo> res = new ArrayList<>();
        res.add(new ContactInfo("小白", "123456"));
        res.add(new ContactInfo("小黑1", "987456"));
        res.add(new ContactInfo("小黑2", "987456"));
        res.add(new ContactInfo("小黑3", "987456"));
        res.add(new ContactInfo("小黑4", "987456"));
        res.add(new ContactInfo("小黑5", "987456"));
        res.add(new ContactInfo("小黑6", "987456"));
        res.add(new ContactInfo("小黑7", "987456"));
        res.add(new ContactInfo("小黑8", "987456"));
        res.add(new ContactInfo("小黑9", "987456"));
        res.add(new ContactInfo("小黑11", "987456"));
        res.add(new ContactInfo("小黑21", "987456"));
        res.add(new ContactInfo("小黑31", "987456"));
        res.add(new ContactInfo("小黑41", "987456"));
        res.add(new ContactInfo("小黑51", "987456"));
        res.add(new ContactInfo("小黑22", "987456"));
        res.add(new ContactInfo("小黑33", "987456"));
        res.add(new ContactInfo("小黑44", "987456"));
        res.add(new ContactInfo("小黑55", "987456"));

        return res;
    }


    public static class ContactInfo {
        public String name;
        public String phone;

        public ContactInfo(String name, String phone) {
            this.name = name;
            this.phone = phone;
        }
    }

}
