package com.prob.pilot18091701.controller;

import com.prob.pilot18091701.entity.Comment;
import com.prob.pilot18091701.entity.Post;
import com.prob.pilot18091701.repository.CommentRepository;
import com.prob.pilot18091701.repository.PostRepository;
import com.prob.pilot18091701.service.CommentService;
import com.prob.pilot18091701.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class PostingController {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentService commentService;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostService postService;


    @GetMapping("/bbs")
    public Iterable<Post> getAllPosts(){
        log.info("bbs 입장..");
        return postRepository.findAll();
    }

    @PostMapping("/bbs/write")
    public Post write(Post post){
        log.info("포스트 작성 "+post);
        postRepository.save(post);
        return post;
    }

    @GetMapping("/bbs/{1}")
    public Post detail(@PathVariable("1") int postIdx){
        log.info("상세보기 "+postIdx);

        //그리고 코멘트들을 받아와야함
      return  postRepository.findByPostIdx(postIdx);
    }

    @PutMapping("/bbs/{1}")
    public Post edit(Post post, @PathVariable("1") int postIdx){
        log.info("포스트 수정 "+post);
        post.setPostIdx(postIdx);
        postRepository.save(post);
        return postRepository.findByPostIdx(postIdx);
    }


    @DeleteMapping("/bbs/{1}")
    public void delete(@PathVariable("1") int postIdx){
        log.info("포스트 삭제 "+postIdx);
        commentRepository.deleteByBelongingPostPostIdx(postIdx); //코멘트들도 같이 삭제
        postRepository.deleteByPostIdx(postIdx);

    }

//    @PostMapping("/bbs/comment")
//    public Comment reply(Comment comment){
//
//    }



}
