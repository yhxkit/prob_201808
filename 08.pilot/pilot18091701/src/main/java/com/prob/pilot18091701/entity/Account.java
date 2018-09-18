package com.prob.pilot18091701.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Getter //data 안 쓴 이유는 스택오버플로우때문
@Setter
@Entity
public class Account {
	@Id
    private String email;
    private String password;
    private String name;

    @OneToMany(mappedBy = "writer")
    @JsonIgnore//이거 없으면 재귀적으로 돌면서 오류가 납니다
    private List<Post> posts;


    //auth, status
    
}
