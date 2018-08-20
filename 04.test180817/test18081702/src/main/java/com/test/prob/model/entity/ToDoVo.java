package com.test.prob.model.entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mysql.fabric.xmlrpc.base.Array;

public class ToDoVo {
	
	private int toDoIdx;

    private String dateFrom;
    private String dateTo;
    
    private String title;
    private boolean status;
    
    private String tags;

    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
    
    
    public ToDoVo() {//디폴트 생성자가 없으면 오류
    }
    
    public ToDoVo(Date dateFrom, Date dateTo, String title, String tags) {
        this.dateFrom = format.format(dateFrom);
        this.dateTo = format.format(dateTo);
        this.title = title;
        this.tags = tags;
        this.status=false;


    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = format.format(dateFrom);
    }
    
    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = format.format(dateTo);
    }
    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean getStatus() {
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
    
    

    @Override
    public String toString() {
        return "ToDoVo[" +
                "dateFrom=" + dateFrom +
                ", dateTo=" + dateTo +
                ", title=" + title  +
                ", status=" + status +
                ", tags=" + tags +
                  ", toDoIdx=" + toDoIdx +
                "]";
    }


}
