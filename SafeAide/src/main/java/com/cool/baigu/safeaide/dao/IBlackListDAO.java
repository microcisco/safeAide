package com.cool.baigu.safeaide.dao;

import com.cool.baigu.safeaide.beans.BlackContacts;

import java.util.List;

/**
 * Created by baigu on 2017/9/7.
 */

public interface IBlackListDAO {
    boolean add(BlackContacts blackContacts);
    boolean remove(BlackContacts blackContacts);
    boolean update(BlackContacts blackContacts);
    String get(BlackContacts blackContacts);
    List<BlackContacts> getList();
}
