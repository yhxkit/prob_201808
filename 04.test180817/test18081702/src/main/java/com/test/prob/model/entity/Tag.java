package com.test.prob.model.entity;

import lombok.Data;

@Data
public class Tag {

	int toDoIdx;
	String tag;

	public Tag() { // 디폴트 생성자 없으면 오류...
	}

	public Tag(String tag) {
		super();
		this.tag = tag;
	}

	
	/*public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getToDoIdx() {
		return toDoIdx;
	}

	public void setToDoIdx(int toDoIdx) {
		this.toDoIdx = toDoIdx;
	}

	@Override
	public String toString() {
		return "Tag [tag=" + tag +"]";
	}
	*/
	
	
	
}
