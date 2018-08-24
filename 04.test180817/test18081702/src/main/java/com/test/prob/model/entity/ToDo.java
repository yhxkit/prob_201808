package com.test.prob.model.entity;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mysql.fabric.xmlrpc.base.Array;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class ToDo {

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
        this.tags = tags;

        this.status=false;

    }


}
