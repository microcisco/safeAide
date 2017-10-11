package com.cool.baigu.safeaide.dao;

/**
 * Created by baigu on 2017/9/7.
 */

public interface ITeleAttribution {
    String getAddress4Phone(String phone);
    String getAddress4Landline(String phone);
}
