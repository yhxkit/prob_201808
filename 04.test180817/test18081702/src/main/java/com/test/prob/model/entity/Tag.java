
package com.test.prob.model.entity;

import com.sun.javafx.beans.IDProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.io.Serializable;

@Data // 롬복
//@Entity //JPA
//@SecondaryTable( //아니.. 이거 왜 어떤거엔 쓰고 어떤거에는 안써...
//		name = "jpataglist",
//		pkJoinColumns = @PrimaryKeyJoinColumn(name="toDoIdx", referencedColumnName = "toDoIdx")
//)
//@Table(name="taglist")
//@Embeddable
@Embeddable
public class Tag {

//	@Column(name="toDoIdx")
//	int toDoIdx;
//	@Column(name="tadIdx")
//	int tagIdx;

	//	@Id
//	private TagPK tagPk;
	String tag;

	public Tag() { // 디폴트 생성자 없으면 오류...
	}


/*	public Tag(String tag) { //toDoidx exclude 할수 있나염...
		this.tag = tag;
	}*/





}

