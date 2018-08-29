package com.test.prob.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data // 롬복
@Entity //JPA
@Table(name="taglist")
public class Tag {

	@Id
	int toDoIdx;
	int tagIdx;
	String tag;

	public Tag() { // 디폴트 생성자 없으면 오류...
	}

/*	public Tag(String tag) { //toDoidx exclude 할수 있나염...
		this.tag = tag;
	}*/


	
	
}
