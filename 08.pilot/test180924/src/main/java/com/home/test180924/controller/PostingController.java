package com.home.test180924.controller;

import com.home.test180924.entity.*;
import com.home.test180924.entity.enumForEntity.PostCategory;
import com.home.test180924.service.interfaces.AccountService;
import com.home.test180924.service.validator.WriterCheckValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
        log.debug("bbs 입장");

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
    public ResponseEntity<?> write(HttpServletRequest request, @RequestBody PostDto postDto){ // postman으로 할때는 @RequestBody 빼야하고...
        log.debug("포스트 작성 "+ postDto.getTitle()+" "+postDto.getPostContent());
        HashMap<String, String> resultMap = new HashMap();
        if(postService.emptyCheck(postDto)){
            resultMap.put("result", "fail");
            resultMap.put("message", "타이틀과 내용을 모두 입력하세요");
            return responseEntity.get(resultMap);
        }
        return responseEntity.get(postService.write(postDto));

    }

    @GetMapping("/bbs/{1}")
    public ResponseEntity<?> detail(@PathVariable("1") int postIdx){
        log.debug("상세보기 "+postIdx);
        //일단 게시글을 열때는 디폴트로 코멘트 첫번째 페이지 받아오고, 나머지 코멘트 페이지는 ajax로
        List<Object> postDetailAndCommentList = new ArrayList(); 
        postDetailAndCommentList.add(postService.detail(postIdx));
        postDetailAndCommentList.add( commentService.commentListWithPage(postIdx));

        return responseEntity.get(postDetailAndCommentList);
    }

    @PutMapping("/bbs/{1}")
    public ResponseEntity<?> editPost(@RequestBody PostDto postDto, HttpServletRequest request, @PathVariable("1") int postIdx){
        log.debug("포스트 수정 "+postDto.getTitle()+" : "+postDto.getPostContent()+ "("+postDto.getPostIdx()+")");
        HashMap<String, String> resultMap = new HashMap();
        String token = request.getHeader("token");

        Account writer = accountService.findByPostsPostIdx(postIdx);
        String postWriterEmail = writer.getEmail();

        if(!writerCheckValidator.checkWriter(postWriterEmail, token)){
            resultMap.put("result", "fail");
            resultMap.put("message", "글쓴이가 아닙니다");
            return responseEntity.get(resultMap);
        }

        return responseEntity.get(postService.edit(postIdx, postDto));
    }


    @DeleteMapping("/bbs/{1}")
    public ResponseEntity<?> deletePost(@PathVariable("1") int postIdx, HttpServletRequest request){
        log.debug("포스트 삭제 "+postIdx);
        HashMap<String, String> resultMap = new HashMap();
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
        log.debug(postIdx+"번 포스트의 코멘트 작성 "+commentDto.getCommentContent() + ":" + commentDto.getCommentWriterEmail());
        return responseEntity.get(commentService.reply(commentDto, postIdx));
    }


    @PutMapping("/bbs/{1}/{2}")
    public ResponseEntity<?> editComment(@RequestBody CommentDto commentDto, @PathVariable("1") int postIdx, @PathVariable("2") int commentIdx, HttpServletRequest request){
        log.debug(postIdx+"번 포스트의 "+commentDto.getCommentIdx()+" 코멘트 수정 "+ commentDto.getCommentContent());
        
        HashMap<String, String> resultMap = new HashMap();
        String token = request.getHeader("token");
        Account writer = accountService.findByCommentsCommentIdx(commentIdx);
        String commentWriterEmail = writer.getEmail();
        
        if(!writerCheckValidator.checkWriter(commentWriterEmail, token)) {
        	resultMap.put("result", "fail");
            resultMap.put("message", "글쓴이가 아닙니다");
            return responseEntity.get(resultMap);
        }
        
        resultMap.put("result", "success");
        return responseEntity.get(commentService.edit(postIdx, commentIdx, commentDto));
    }

    @DeleteMapping("/bbs/{1}/{2}")
    public ResponseEntity<?> deleteComment(@PathVariable("1") int postIdx, @PathVariable("2") int commentIdx, HttpServletRequest request){
        log.debug(postIdx+"번 포스트의 코멘트 삭제 "+commentIdx);
        HashMap<String, String> resultMap = new HashMap();
        String token = request.getHeader("token");
        Account writer = accountService.findByCommentsCommentIdx(commentIdx);
        String commentWriterEmail = writer.getEmail();
        
        if(!writerCheckValidator.checkWriter(commentWriterEmail, token)) {
        	resultMap.put("result", "fail");
            resultMap.put("message", "글쓴이가 아닙니다");
            return responseEntity.get(resultMap);
        }
        resultMap.put("result", "success");
        commentService.delete(commentIdx);
        return responseEntity.get(resultMap);  

    }

    @PostMapping("/bbs/search")
    public Map<String, Post> searchPosts(@RequestBody Searching searching, HttpServletRequest request){
    	String keyword = searching.getKeyword(); 
    	String category =searching.getCategory();
    	Map searchMap = new HashMap();
    	
    	log.debug("검색 카테고리: "+category+", 검색 키워드 "+keyword);
    	
    	if(PostCategory.TITLE.toString().equals( category )) {
    	     log.debug("포스트 타이틀로 검색 키워드 "+keyword);
    	     int page=0;
    	     if(request.getParameter("page")!=null){
    	         page = Integer.parseInt(request.getParameter("page"))-1;
    	     }

    	     Iterable<Post> posts =postService.findPostsWithPage(keyword, page, Integer.parseInt(request.getParameter("elementsNumberForOnePage")));
    	     
    	     searchMap.put("posts", posts);
    	}else if(PostCategory.WRITER.toString().equals( category )) {
    		log.debug("포스트 작성자 이메일로 검색"+keyword);
//    		 int page=0;
//     	     if(request.getParameter("page")!=null){
//     	         page = Integer.parseInt(request.getParameter("page"))-1;
//     	     }

     	     Iterable<Post> posts =postService.findByWriter(keyword);
     	     searchMap.put("posts", posts);
    		
    	}
    	
    	return searchMap;

    }
    
    
  


}
