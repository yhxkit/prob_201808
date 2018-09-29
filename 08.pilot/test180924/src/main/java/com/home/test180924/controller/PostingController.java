package com.home.test180924.controller;

import com.home.test180924.entity.PostDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.home.test180924.controller.responseUtil.EntityForResponse;
import com.home.test180924.entity.Comment;
import com.home.test180924.entity.Post;
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

    private CommentService commentService;
    private PostService postService;
    private EntityForResponse responseEntity;

    public PostingController(CommentService commentService, PostService postService, EntityForResponse responseEntity) {
        this.commentService = commentService;
        this.postService = postService;
        this.responseEntity = responseEntity;
    }

    @GetMapping("/bbs")
    public Map getAllPosts(HttpServletRequest request){
        System.out.println("bbs 입장");

        int page=0;
        if(request.getParameter("page")!=null  ){
            if(Integer.parseInt(request.getParameter("page"))>0) {
                page = Integer.parseInt(request.getParameter("page")) - 1;
            }
        }

        List<Post> posts = postService.bbsWithPage(page, Integer.parseInt(request.getParameter("elementsNumberForOnePage")));
        long totalPost = postService.getTotalPost();

        Map bbsMap = new HashMap();
        bbsMap.put("posts", posts);
        bbsMap.put("totalPost", totalPost);

        return bbsMap;// posts; //responseEntity.get(postService.bbsWithPage(page));
    }

    @PostMapping("/bbs/write")
    public ResponseEntity write(HttpServletRequest request, @RequestBody PostDto post){ // postman으로 할때는 @RequestBody 빼야하고...
        log.info("포스트 작성 "+ post.getTitle()+" "+post.getPostContent());
        return responseEntity.get(postService.write(post));
    }

    @GetMapping("/bbs/{1}")
    public ResponseEntity<?> detail(@PathVariable("1") int postIdx){
        System.out.println("상세보기 "+postIdx);
        //일단 게시글을 열때는 디폴트로 코멘트 첫번째 페이지 받아오고, 나머지 코멘트 페이지는 ajax로
        List tmp = new ArrayList(); //아님 프론트에서 맵값을 쓸 수 있다면 맵으로 따로 주던가..?
        tmp.add(postService.detail(postIdx));
        tmp.add( commentService.commentListWithPage(postIdx));

        return responseEntity.get(tmp);
    }

    @PutMapping("/bbs/{1}")
    public ResponseEntity editPost(@RequestBody PostDto post, HttpServletRequest request, @PathVariable("1") int postIdx){
        System.out.println("포스트 수정 "+post.getTitle()+post.getPostContent()+post.getPostIdx());
        String token = request.getHeader("token");

        return responseEntity.get(postService.edit(postIdx, post, token));
    }


    @DeleteMapping("/bbs/{1}")
    public void deletePost(@PathVariable("1") int postIdx, HttpServletRequest request){
        System.out.println("포스트 삭제 "+postIdx);
        String token = request.getHeader("token");
        postService.delete(postIdx, token);
        commentService.deleteByPostIdx(postIdx);//코멘트들도 같이 삭제

    }

    @PostMapping("/bbs/{1}/comment")
    public ResponseEntity<?> reply(Comment comment ,@PathVariable("1") int postIdx ){
        System.out.println(postIdx+"번 포스트의 코멘트 작성 "+comment);
        return responseEntity.get(commentService.reply(comment, postIdx));
    }


    @PutMapping("/bbs/{1}/{2}")
    public ResponseEntity<?> editComment(Comment comment, @PathVariable("1") int postIdx, @PathVariable("2") int commentIdx){
        System.out.println(postIdx+"번 포스트의 코멘트 수정 "+ comment);
        return responseEntity.get(commentService.edit(postIdx, commentIdx, comment));
    }

    @DeleteMapping("/bbs/{1}/{2}")
    public void deleteComment(@PathVariable("1") int postIdx, @PathVariable("2") int commentIdx){
        System.out.println(postIdx+"번 포스트의 코멘트 삭제 "+commentIdx);
        commentService.delete(commentIdx);

    }

    @GetMapping("/bbs/search")//
    public ResponseEntity<?> searchPosts(Post post){

        System.out.println("검색 들어옴 "+post.getPostContent()+" "
                +post.getTitle()+" "
                +post.getPostWriter());
        if(post.getPostWriter() != null){
            System.out.println(" 검색한 유저의 메일 "+post.getPostWriter().getEmail() + " 포스팅 갯수 "+post.getPostWriter().getPosts().size());
        }

        return responseEntity.get("검색 결과 보여주기");
    }


}
