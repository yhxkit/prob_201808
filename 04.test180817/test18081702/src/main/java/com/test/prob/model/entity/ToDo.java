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

    public ToDo() {//디폴트 생성자가 없으면 오류
    }
    
    public ToDo(LocalDate dateFrom, LocalDate dateTo, String title, String tags) {

        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.title = title;
        this.tags = tags;
        this.status=false;

    }

/*
    public  LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }*/

/*    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    
    public void setStatus(int status) {
    	
    	if(status == 0) {  
    		this.status = false;
    	}else {
    		this.status = true;
    	}
      
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
    
	public int getToDoIdx() {
		return toDoIdx;
	}

	public void setToDoIdx(int toDoIdx) {
		this.toDoIdx = toDoIdx;
	}
    */
    
/*

    @Override
    public String toString() {
        return "ToDo[" +
                "dateFrom=" + dateFrom +
                ", dateTo=" + dateTo +
                ", title=" + title  +
                ", status=" + status +
                ", tags=" + tags +
                  ", toDoIdx=" + toDoIdx +
                "]";
}
*/


}
