package com.test.prob.model.entity;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.mysql.fabric.xmlrpc.base.Array;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ToDo {

     private int toDoIdx;

     private LocalDate dateFrom;
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
        this.tags = tags;

        this.status=false;

    }


}
