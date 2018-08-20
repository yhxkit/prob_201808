package com.test.prob.model;

import com.test.prob.model.entity.TagVo;
import com.test.prob.model.entity.ToDoVo;

import java.util.List;
import java.util.Map;

public interface ListDao {

    List<ToDoVo> selectAll() throws Exception;
    List<ToDoVo> selectAllWithTag(String tag) throws Exception;
    List<Object> selectOne(int idx) throws Exception;
    void delOne(int idx) throws Exception;
    void insertOne(ToDoVo toDoBean) throws Exception;
    void editOne(ToDoVo toDoBean) throws Exception;

}
