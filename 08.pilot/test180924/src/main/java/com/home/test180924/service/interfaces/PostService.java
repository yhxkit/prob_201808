package com.home.test180924.service.interfaces;


import java.util.List;

import com.home.test180924.controller.responseUtil.ResultMessage;
import com.home.test180924.entity.Post;
import com.home.test180924.entity.PostDto;
import org.springframework.data.domain.Page;

public interface PostService {

    Page<Post> bbsWithPage(int requiredBbsPage, int elementsNumberForOnePage);

    ResultMessage<Post> write(PostDto postDto);
    ResultMessage<Post> detail(int postIdx);
    ResultMessage<Post> edit(int postIdx, PostDto postDto, String token);

    ResultMessage delete(int postIdx, String token);
    void deleteAll(Iterable<Post> userPosts);
    
    Iterable<Post> findPostsWithPage(String keyword, int page, int elementsNumberForOnePage);
    Iterable<Post> findByWriter(String keyword);
}
