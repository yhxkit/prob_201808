package com.test.prob.controller;

import com.test.prob.model.ListDao;
import com.test.prob.model.entity.TagVo;
import com.test.prob.model.entity.ToDoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@Controller 
public class ListController {

    @Autowired
    ListDao listDao;

    private static String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(){

        System.out.println("welcome...");

        return "home";
    }

    //입력 수정 등, 커맨드 객체로 받을 메서드의 Vo 파라미터 앞에 @requestBody = 415, @requestParam = 400 에러...

    @ResponseBody //뷰 없음
    @RequestMapping(value="/list",  method=RequestMethod.GET)
    public List<ToDoVo> getList() throws Exception{
        
        List<ToDoVo> test =listDao.selectAll();
        System.out.println(test);
        return test;
    }


    @ResponseBody //뷰 없음
    @RequestMapping(value="/searchWithTag/{tag}",  method=RequestMethod.GET)
    public List<ToDoVo> getListWithTag(@PathVariable String tag) throws Exception{
       // System.out.println("검색 체크 : "+tag);

        List<ToDoVo> test =listDao.selectAllWithTag(tag);
        System.out.println(test);
        return test;
    }





    @ResponseBody
    @RequestMapping(value="/add", method=RequestMethod.POST)
    public void addOne(ToDoVo toDoBean) throws Exception{
    	System.out.println("Insert data :"+toDoBean);

        System.out.println(new Date());

    	if(toDoBean.getDateFrom().equals("")||toDoBean.getDateFrom()==null){
    	    toDoBean.setDateFrom(today);
        }
        if(toDoBean.getDateTo().equals("")||toDoBean.getDateTo()==null){
            toDoBean.setDateTo(today);
        }

    	listDao.insertOne(toDoBean);
    	
    }

    @ResponseBody
    @RequestMapping(value="/{1}", method=RequestMethod.GET)
    public List<Object> detailOne(@PathVariable("1") int idx) throws Exception {
    	System.out.println("상세보기 "+idx);
    	return listDao.selectOne(idx);
  	
    }
    
    @ResponseBody
    @RequestMapping(value="/{1}", method=RequestMethod.DELETE)
    public void deleteOne(@PathVariable("1") int idx) throws Exception {    	
    	System.out.println("삭제 "+idx);
    	 listDao.delOne(idx);
  	
    }
    
    @ResponseBody //
    @RequestMapping(value="/{1}", method=RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE) //왜 인서트는 받아오면서 여기서는 못받아와서 이렇게 짜야하는지?
    public void editOne(@PathVariable("1") int idx, @RequestBody Map<String, Object> params) throws Exception {
    	//json으로 보내서 @RequestBody를 적용한 MAP 으로만 받아지고, ToDoVo 커맨드 객체는 먹히지 않는다...
    	
    	System.out.println("수정 "+idx);
    	System.out.println(params);

    	ToDoVo toDoBean = new ToDoVo();
    	toDoBean.setToDoIdx(idx);



    	toDoBean.setDateFrom((String)params.get("dateFrom"));
    	toDoBean.setDateTo((String)params.get("dateTo"));

        if(toDoBean.getDateFrom().equals("")||toDoBean.getDateFrom()==null){
            toDoBean.setDateFrom(today);
        }
        if(toDoBean.getDateTo().equals("")||toDoBean.getDateTo()==null){
            toDoBean.setDateTo(today);
        }

    	toDoBean.setTitle((String)params.get("title"));
    	toDoBean.setTags((String)params.get("tags"));
    	toDoBean.setStatus((boolean)params.get("status"));

    	System.out.println(toDoBean);
    	listDao.editOne(toDoBean);
  	
    }



}
