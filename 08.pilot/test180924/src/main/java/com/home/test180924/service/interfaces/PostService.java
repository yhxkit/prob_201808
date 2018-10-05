package com.home.test180924.service.interfaces;


import java.util.List;

import com.home.test180924.entity.Post;
import com.home.test180924.entity.PostDto;
import org.springframework.data.domain.Page;

public interface PostService {

    Page<Post> bbsWithPage(int requiredBbsPage, int elementsNumberForOnePage);
    Post write(PostDto postDto);
    Post detail(int postIdx);
    Post edit(int postIdx, PostDto postDto);
    void delete(int postIdx);
//    long getTotalPost();

    Iterable<Post> findPostsWithPage(String keyword, int page, int elementsNumberForOnePage);

    
    Iterable<Post> findByWriter(String keyword);
}
