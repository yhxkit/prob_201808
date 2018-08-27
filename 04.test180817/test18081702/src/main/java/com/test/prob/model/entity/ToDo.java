package com.test.prob.model.entity;

import java.time.LocalDate;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class ToDo {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ToDo.class);

     private int toDoIdx;
    @JsonFormat(pattern = "yyyy-MM-dd")
     private LocalDate dateFrom;
    @JsonFormat(pattern = "yyyy-MM-dd")
     private LocalDate dateTo;

     private String title;
     private boolean status;
    
    private String tags;

    public ToDo() { //디폴트 생성자가 없으면 오류
    }

    public ToDo(LocalDate dateFrom, LocalDate dateTo, String title, String tags) { //status exclude하고 false 줄 수 있나..?

        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.title = title;
        this.tags = tags.trim();

        this.status=false;

    }


    public void setDateFrom(LocalDate dateFrom) {

        Optional from = Optional.ofNullable(dateFrom);

        if (!from.isPresent()) {
            this.dateFrom = LocalDate.now();
        }else {
            this.dateFrom=(LocalDate) from.get();
        }


    }

    public void setDateFromStr(String dateFrom) {
        this.dateFrom = LocalDate.parse(dateFrom);
    }

    public void setDateToStr(String dateTo) { //입력시 localdate 타입으로 파싱 안됨... String 값을 받아염..
        this.dateTo = LocalDate.parse(dateTo);
    }


        public void setDateTo(LocalDate dateTo) {

            Optional to = Optional.ofNullable(dateTo);

            if (!to.isPresent()) {
                this.dateTo = LocalDate.now();
            }else {
                this.dateTo = (LocalDate) to.get();
            }
        }

    public  void setTitle(String title){
        Optional t= Optional.ofNullable(title.trim());

        if(!t.isPresent() || t.get().toString().equals("")) {
            this.title = "할 일을 설정하지 않았습니다";
        }else {
            this.title = title;
        }

    }


}
