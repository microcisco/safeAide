package com.cool.baigu.safeaide.beans;

import android.graphics.drawable.Drawable;

/**
 * Created by baigu on 2017/9/21.
 */

public class AppProcessInfo {
    String name;        //名称
    Drawable icon;      //图标
    String packName;    //包名
    String size;        //占用空间
    long size1;        //占用空间
    String version;     //版本
    boolean sysApp;     //系统软件
    boolean isChecked;  //被选中

    public long getSize1() {
        return size1;
    }

    public void setSize1(long size1) {
        this.size1 = size1;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public AppProcessInfo(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isSysApp() {
        return sysApp;
    }

    public void setSysApp(boolean sysApp) {
        this.sysApp = sysApp;
    }


}
