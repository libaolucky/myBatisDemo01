package com.hp.dao;

import com.hp.bean.Person;

import java.util.List;

public interface PersonDao {
    // 全查
    List<Person> selectAll();
}
