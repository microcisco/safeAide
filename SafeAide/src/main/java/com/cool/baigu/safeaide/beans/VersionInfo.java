package com.cool.baigu.safeaide.beans;

/**
 * Created by baigu on 2017/8/18.
 */

public class VersionInfo {
    public int version_code;
    public String version_name;
    public String version_des;
    public String down_url;
    public VersionInfo(int version_code, String version_name, String version_des, String down_url) {
        this.version_code = version_code;
        this.version_name = version_name;
        this.version_des = version_des;
        this.down_url = down_url;
    }
}
