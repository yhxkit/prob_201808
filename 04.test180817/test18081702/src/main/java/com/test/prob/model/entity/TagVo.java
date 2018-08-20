package com.test.prob.model.entity;

public class TagVo {

	int toDoIdx;
	String tag;

	public TagVo() { // 디폴트 생성자 없으면 오류...
	}

	public TagVo(String tag) {
		super();
		this.tag = tag;
	}

	
	public String getTag() {
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
		return "TagVo [tag=" + tag +"]";
	}
	
	
	
	
}
