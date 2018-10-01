package com.home.test180924.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.home.test180924.entity.enumForEntity.Auth;
import com.home.test180924.entity.enumForEntity.Status;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter //data 안 쓴 이유는 스택오버플로우때문
@Setter
@Entity
public class Account {
	@Id
    private String email;
    private String password;
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    @CreationTimestamp
    private LocalDateTime accountCreatedTime;

    @Enumerated(EnumType.STRING)
    private Auth auth= Auth.USER;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ENABLE;




    @OneToMany(mappedBy = "postWriter")
    @JsonIgnore//이거 없으면 재귀적으로 돌면서 오류가 납니다
    private List<Post> posts;

    @OneToMany(mappedBy = "commentWriter")
    @JsonIgnore
    private List<Comment> comments;
//
//	public String getEmail() {
//		return email;
//	}
//
//	public void setEmail(String email) {
//		this.email = email;
//	}
//
//	public String getPassword() {
//		return password;
//	}
//
//	public void setPassword(String password) {
//		this.password = password;
//	}
//
//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public LocalDateTime getAccountCreatedTime() {
//		return accountCreatedTime;
//	}
//
//	public void setAccountCreatedTime(LocalDateTime accountCreatedTime) {
//		this.accountCreatedTime = accountCreatedTime;
//	}
//
//	public Auth getAuth() {
//		return auth;
//	}
//
//	public void setAuth(Auth auth) {
//		this.auth = auth;
//	}
//
//	public Status getStatus() {
//		return status;
//	}
//
//	public void setStatus(Status status) {
//		this.status = status;
//	}
//
//	public List<Post> getPosts() {
//		return posts;
//	}
//
//	public void setPosts(List<Post> posts) {
//		this.posts = posts;
//	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

    
    
    
    


}
