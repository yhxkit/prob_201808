package com.test.prob.repository;

import com.test.prob.model.entity.ToDo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ListRepository { // jpa data 써서 인터페이스로 교체해도 될 것 같고...

    @PersistenceContext
    private EntityManager em;

    public List<ToDo> findAll(){
        TypedQuery<ToDo> query = em.createQuery(
                "select t from ToDo t order by t.status, t.dateTo asc",
                ToDo.class);
        return query.getResultList();
    }

//    public List<ToDo> findByTagsTag(){
//
//    }

    public void delete(ToDo toDoBean){
        em.remove(toDoBean); //....?
    }


//    public ToDo save(ToDo toDoBean){
//
//    }

    public ToDo findOne(int toDoIdx){
        ToDo toDoBean = em.find(ToDo.class, toDoIdx);
        return toDoBean;
    }


    public void editOne(ToDo toDoBean){

    }

//find/save 등등

}
