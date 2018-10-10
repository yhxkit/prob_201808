package com.home.test180924.service;

import com.home.test180924.entity.Account;
import com.home.test180924.entity.PostDto;
import com.home.test180924.repository.AccountRepository;
import com.home.test180924.service.jwt.JWT;
import com.home.test180924.service.validator.WriterCheckValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.home.test180924.entity.Post;
import com.home.test180924.repository.PostRepository;
import com.home.test180924.service.interfaces.PostService;
import com.home.test180924.util.Paging;


@Slf4j
@Service
@Transactional
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private AccountRepository accountRepository;
    private Paging paging;
    private JWT jwt;
    private WriterCheckValidator writerCheckValidator;


    public PostServiceImpl(PostRepository postRepository, AccountRepository accountRepository, Paging paging, JWT jwt, WriterCheckValidator writerCheckValidator) {
        this.postRepository = postRepository;
        this.accountRepository=accountRepository;
        this.paging = paging;
        this.jwt = jwt;
        this.writerCheckValidator=writerCheckValidator;
    }


    @Override
    public Page<Post> bbsWithPage(int requiredBbsPage, int elementsNumberForOnePage){
        Page<Post> bbsList = postRepository.findAll(paging.elementsByPage(requiredBbsPage, elementsNumberForOnePage,  "postIdx"));
        return bbsList;
    }


    @Override
    public Post write(PostDto postDto){

        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setPostContent(postDto.getPostContent());
        post.setPostWriter(accountRepository.findByEmail(postDto.getPostWriter()));
        postRepository.save(post);
        return postRepository.findByPostIdx(post.getPostIdx());
    }

    @Override
    public void deleteAll(Iterable<Post> userPosts){
        postRepository.deleteAll(userPosts);
    }


    @Override
    public Post detail(int postIdx){
        return postRepository.findByPostIdx(postIdx);
    }

    @Override
    public Post edit(int postIdx, PostDto postDto){//, HttpServletResponse response){

        Post post =  postRepository.findByPostIdx(postIdx);
        post.setPostContent(postDto.getPostContent());
        post.setTitle(postDto.getTitle());

        return  postRepository.save(post);
    }

    @Override
    public void delete(int postIdx){

        postRepository.delete(postRepository.findByPostIdx(postIdx));

    }


    @Override
    public Iterable<Post> findPostsWithPage(String keyword, int page, int elementsNumberForOnePage) {
        return postRepository.findByTitleIgnoreCaseContainingOrderByPostIdxDesc(keyword);
    }
    
    @Override
    public Iterable<Post> findByWriter(String keyword) {
    	return postRepository.findByPostWriterEmailIgnoreCaseContainingOrderByPostIdxDesc(keyword);
    }


    @Override
    public boolean emptyCheck(PostDto postDto) {
        if(postDto.getTitle()==null || "".equals(postDto.getTitle().trim())){
            log.debug("포스트 타이틀이 비어있음");
            return true;
        }
        if(postDto.getPostContent()==null || "".equals(postDto.getPostContent().trim())){
            log.debug("포스트 내용이 비어있음");
            return true;
        }
        return false;
    }
}
