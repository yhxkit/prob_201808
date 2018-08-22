package com.test.prob.controller;

import com.test.prob.model.ListDao;
import com.test.prob.model.entity.Tag;
import com.test.prob.model.entity.ToDo;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Log
@Controller 
public class ListController {


    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ListController.class);



    @Autowired
    ListDao listDao;


    @GetMapping("/")
    public String home(){
        log.info("welcome...");

        return "home";
    }

    //입력 수정 등, 커맨드 객체로 받을 메서드의 Vo 파라미터 앞에 @requestBody = 415, @requestParam = 400 에러...

    @ResponseBody //뷰 없음
   // @RequestMapping(value="/list",  method=RequestMethod.GET)
    @GetMapping("/list")
    public List<ToDo> getList() throws Exception{
        
        List<ToDo> test =listDao.selectAll();
        log.info(test.toString());
        return test;
    }


    @ResponseBody //뷰 없음
    @GetMapping("/searchWithTag/{tag}")
    public List<ToDo> getListWithTag(@PathVariable String tag) throws Exception{
       // System.out.println("검색 체크 : "+tag);

        List<ToDo> test =listDao.selectAllWithTag(tag);
        log.info(test.toString());
        return test;
    }



    @ResponseBody
    @PostMapping("/add")
    public void addOne(ToDo toDoBean) throws Exception{
        log.info("Insert data :"+toDoBean);

    	listDao.insertOne(toDoBean);
    	
    }

    @ResponseBody
    @GetMapping("/{1}")
    public List<Object> detailOne(@PathVariable("1") int idx) throws Exception {
        log.info("상세보기 "+idx);
    	return listDao.selectOne(idx);
  	
    }
    
    @ResponseBody
    @DeleteMapping("/{1}")
    public void deleteOne(@PathVariable("1") int idx) throws Exception {
        log.info("삭제 "+idx);
    	 listDao.delOne(idx);
  	
    }
    
    @ResponseBody //
    @PutMapping(value="/{1}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE) //왜 인서트는 받아오면서 여기서는 못받아와서 이렇게 짜야하는지?
    public void editOne(@PathVariable("1") int idx, @RequestBody Map<String, Object> params) throws Exception {
    	//json으로 보내서 @RequestBody를 적용한 MAP 으로만 받아지고, ToDo 커맨드 객체는 먹히지 않는다...

        log.info("수정 "+idx);
        log.info(params.toString());

    	ToDo toDoBean = new ToDo();
    	toDoBean.setToDoIdx(idx);

    	String[] dateFrom = params.get("dateFrom").toString().split("-"); // 여기 어떻게 하지 ㅠ..?
    	toDoBean.setDateFrom(LocalDate.of(Integer.parseInt(dateFrom[0]), Integer.parseInt(dateFrom[1]), Integer.parseInt(dateFrom[2])));

        String[] dateTo = params.get("dateTo").toString().split("-");
        toDoBean.setDateTo(LocalDate.of(Integer.parseInt(dateTo[0]), Integer.parseInt(dateTo[1]), Integer.parseInt(dateTo[2])));

    	toDoBean.setTitle((String)params.get("title"));
    	toDoBean.setTags((String)params.get("tags"));
    	toDoBean.setStatus((boolean)params.get("status"));

        log.info(toDoBean.toString());
    	listDao.editOne(toDoBean);
  	
    }


//    @ResponseBody //
//    @PutMapping(value="/{1}", consumes = MediaType.APPLICATION_JSON_VALUE) //??
//    public void editOne(@PathVariable("1") int idx, @RequestBody ToDo toDoBean) throws Exception {
//        //json으로 보내서 @RequestBody를 적용한 MAP으로만 받아지고, ToDo 커맨드 객체는 먹히지 않는다...
//
//        System.out.println("수정 "+idx);
//
//        System.out.println(toDoBean);
//        //listDao.editOne(toDoBean);

 //   }

}
