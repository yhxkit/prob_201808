package com.home.test180924.controller;

import com.home.test180924.entity.*;
import com.home.test180924.service.interfaces.AccountService;
import com.home.test180924.service.validator.WriterCheckValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.home.test180924.controller.responseUtil.EntityForResponse;
import com.home.test180924.service.interfaces.CommentService;
import com.home.test180924.service.interfaces.PostService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class PostingController {

    private AccountService accountService;
    private CommentService commentService;
    private PostService postService;
    private EntityForResponse responseEntity;
    private WriterCheckValidator writerCheckValidator;


    public PostingController(CommentService commentService, PostService postService, EntityForResponse responseEntity, WriterCheckValidator writerCheckValidator, AccountService accountService) {
        this.commentService = commentService;
        this.postService = postService;
        this.responseEntity = responseEntity;
        this.writerCheckValidator = writerCheckValidator;
        this.accountService = accountService;
    }

    @GetMapping("/bbs")
    public Page<Post> getAllPosts(HttpServletRequest request){
        log.info("bbs 입장");

        int page=0;
        if(request.getParameter("page")!=null  ){
            if(Integer.parseInt(request.getParameter("page"))>0) {
                page = Integer.parseInt(request.getParameter("page")) - 1;
            }
        }

        Page<Post> posts = postService.bbsWithPage(page, Integer.parseInt(request.getParameter("elementsNumberForOnePage")));
        return posts;
    }

    @PostMapping("/bbs/write")
    public ResponseEntity write(HttpServletRequest request, @RequestBody PostDto post){ // postman으로 할때는 @RequestBody 빼야하고...
        log.info("포스트 작성 "+ post.getTitle()+" "+post.getPostContent());
        return responseEntity.get(postService.write(post));
    }

    @GetMapping("/bbs/{1}")
    public ResponseEntity<?> detail(@PathVariable("1") int postIdx){
        log.info("상세보기 "+postIdx);
        //일단 게시글을 열때는 디폴트로 코멘트 첫번째 페이지 받아오고, 나머지 코멘트 페이지는 ajax로
        List postDetailAndCommentList = new ArrayList(); //아님 프론트에서 맵값을 쓸 수 있다면 맵으로 따로 주던가..?
        postDetailAndCommentList.add(postService.detail(postIdx));
        postDetailAndCommentList.add( commentService.commentListWithPage(postIdx));

        return responseEntity.get(postDetailAndCommentList);
    }

    @PutMapping("/bbs/{1}")
    public ResponseEntity<?> editPost(@RequestBody PostDto postDto, HttpServletRequest request, @PathVariable("1") int postIdx){
        log.info("포스트 수정 "+postDto.getTitle()+postDto.getPostContent()+postDto.getPostIdx());
        HashMap resultMap = new HashMap();
        String token = request.getHeader("token");

        Account writer = accountService.findByPostsPostIdx(postIdx);
        String postWriterEmail = writer.getEmail();

        if(!writerCheckValidator.checkWriter(postWriterEmail, token)){
            resultMap.put("result", "fail");
            resultMap.put("message", "글쓴이가 아닙니다");
            return responseEntity.get(resultMap);
        }

        resultMap.put("result", "success");
        return responseEntity.get(postService.edit(postIdx, postDto));
    }


    @DeleteMapping("/bbs/{1}")
    public ResponseEntity<?> deletePost(@PathVariable("1") int postIdx, HttpServletRequest request){
        log.info("포스트 삭제 "+postIdx);
        HashMap resultMap = new HashMap();
        String token = request.getHeader("token");

        Account writer = accountService.findByPostsPostIdx(postIdx);
        String postWriterEmail = writer.getEmail();
        if(!writerCheckValidator.checkWriter(postWriterEmail, token)){
            resultMap.put("result", "fail");
            resultMap.put("message", "글쓴이가 아닙니다");
            return responseEntity.get(resultMap);
        }

        commentService.deleteByPostIdx(postIdx);//코멘트들도 같이 삭제
        postService.delete(postIdx);
        resultMap.put("result", "success");
        return responseEntity.get(resultMap);

    }

    @PostMapping("/bbs/{1}/comment")
    public ResponseEntity<?> reply(@RequestBody CommentDto commentDto ,@PathVariable("1") int postIdx, HttpServletRequest request ){
        log.info(postIdx+"번 포스트의 코멘트 작성 "+commentDto.getCommentContent() + commentDto.getCommentWriterEmail());
        return responseEntity.get(commentService.reply(commentDto, postIdx));
    }


    @PutMapping("/bbs/{1}/{2}")
    public ResponseEntity<?> editComment(@RequestBody CommentDto commentDto, @PathVariable("1") int postIdx, @PathVariable("2") int commentIdx){
        log.info(postIdx+"번 포스트의 "+commentDto.getCommentIdx()+" 코멘트 수정 "+ commentDto.getCommentContent());
        return responseEntity.get(commentService.edit(postIdx, commentIdx, commentDto));
    }

    @DeleteMapping("/bbs/{1}/{2}")
    public void deleteComment(@PathVariable("1") int postIdx, @PathVariable("2") int commentIdx, HttpServletRequest request){
        log.info(postIdx+"번 포스트의 코멘트 삭제 "+commentIdx);
        String token = request.getHeader("token");
        commentService.delete(commentIdx, token);

    }

    @PostMapping("/bbs/search")//getmapping은 검색어에 특문 들어가면 처리가 안돼서 포스트로..
    public Map searchPosts(@RequestBody PostDto postDto, HttpServletRequest request){

        String keyword = postDto.getTitle(); // 재사용할 것 같은데 검색용 객체를 따로 만드는게 좋지 않을까?
        log.info("검색 키워드 "+keyword);
        int page=0;
        if(request.getParameter("page")!=null){
            page = Integer.parseInt(request.getParameter("page"))-1;
        }

        Iterable<Post> posts =postService.findPostsWithPage(keyword, page, Integer.parseInt(request.getParameter("elementsNumberForOnePage")));
        Map postMap = new HashMap();
        postMap.put("posts", posts);

        return postMap;
    }


}
