package com.test.prob.model;

import com.test.prob.model.entity.Tag;
import com.test.prob.model.entity.ToDo;

import java.util.List;
import java.util.Map;

public interface ListDao {

    List<ToDo> selectAll() throws Exception;
    List<ToDo> selectAllWithTag(String tag) throws Exception;
    List<Object> selectOne(int idx) throws Exception;
    void delOne(int idx) throws Exception;
    void insertOne(ToDo toDoBean) throws Exception;
    void editOne(ToDo toDoBean) throws Exception;

}
