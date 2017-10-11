package com.cool.baigu.safeaide.beans;

/**
 * Created by baigu on 2017/9/7.
 */

/**
 * 黑名单联系人对象
 */
public class BlackContacts {
    int _id;
    String phone;
    String mode;       //0:全部  1:电话  2:短信

    public BlackContacts(int _id, String phone, String mode) {
        this._id = _id;
        this.phone = phone;
        this.mode = mode;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
