package com.home.test180924.entity;

import lombok.Data;

@Data
public class PostDto implements Article{

    private int postIdx;

    private String title;
    private String postContent;
    private String postWriter;
    
	
     
    
}
