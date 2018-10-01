package com.home.test180924.service.interfaces;


import java.util.List;

import com.home.test180924.entity.Post;
import com.home.test180924.entity.PostDto;

public interface PostService {

    List<Post> bbsWithPage(int requiredBbsPage, int elementsNumberForOnePage);
    Post write(PostDto postDto);
    Post detail(int postIdx);
    Post edit(int postIdx, PostDto postDto, String token);
    void delete(int postIdx, String token);
    long getTotalPost();

}
