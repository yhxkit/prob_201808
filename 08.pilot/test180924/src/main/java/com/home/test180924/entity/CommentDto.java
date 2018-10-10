package com.home.test180924.entity;

import lombok.Data;

@Data
public class CommentDto {

	private int commentIdx;
	
	 private String commentContent;
	 private int belongingPostIdx;
	 private String commentWriterEmail;
	 
	 
}
