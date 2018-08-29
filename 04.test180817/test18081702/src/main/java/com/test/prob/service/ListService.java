package com.test.prob.service;


import com.test.prob.model.entity.ToDo;
import com.test.prob.repository.ListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ListService {

    private ListRepository listRepository;

    @Autowired
    public void setListRepository(ListRepository listRepository) {
        this.listRepository = listRepository;
    }



    //조회기능이라서 Transactional 없이..
    public List<ToDo> getAllToDo() {
        List<ToDo> result = listRepository.findAll();
        return result;
    }

    public ToDo selectOne(int toDoIdx){
        return listRepository.findOne(toDoIdx);
    }


    @Transactional
    public void deleteOne(int toDoIdx){

        ToDo toDoBean = listRepository.findOne(toDoIdx);
        listRepository.delete(toDoBean);
    }







}
