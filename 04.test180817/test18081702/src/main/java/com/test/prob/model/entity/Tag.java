package com.test.prob.model.entity;

import lombok.Data;

@Data
public class Tag {

	int toDoIdx;
	String tag;

	public Tag() { // 디폴트 생성자 없으면 오류...
	}

	public Tag(String tag) { //toDoidx exclude 할수 있나염...
		this.tag = tag;
	}


	
	
}
