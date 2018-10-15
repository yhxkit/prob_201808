package com.home.test180924.controller;

import javax.servlet.http.HttpServletRequest;

import com.home.test180924.controller.responseUtil.ResultMessage;
import com.home.test180924.entity.AccountDto;
import com.home.test180924.entity.Comment;
import com.home.test180924.entity.Post;
import com.home.test180924.service.interfaces.CommentService;
import com.home.test180924.service.interfaces.PostService;
import com.home.test180924.service.validator.AccountValidator;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.home.test180924.controller.interceptor.CustomAnnotation;
import com.home.test180924.controller.interceptor.EnumForCustomInterceptor;
import com.home.test180924.controller.responseUtil.EntityForResponse;
import com.home.test180924.entity.Account;
import com.home.test180924.service.interfaces.AccountService;
import com.home.test180924.service.jwt.JWT;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@RestController
public class MainController {

    private AccountService accountService;
    private PostService postService;
    private CommentService commentService;
    private EntityForResponse responseEntity;
    private JWT jwt;
    private AccountValidator accountValidator;

    public MainController(AccountService accountService, CommentService commentService, PostService postService,EntityForResponse responseEntity, JWT jwt, AccountValidator accountValidator) {
        this.accountService = accountService;
        this.postService = postService;
        this.commentService = commentService;
        
        this.responseEntity = responseEntity;
        this.jwt = jwt;
        this.accountValidator = accountValidator;
    }


    @PostMapping("/login")
    public ResponseEntity<?>  login(Account account) {
    	log.debug("로그인 "+account.getEmail());
    	return responseEntity.get(accountService.login(account));
    }

    @PostMapping("/join")
    public ResponseEntity<?> join(Account account) {
        log.debug("가입 "+account);
       return responseEntity.get( accountService.join(account) );
    }

    @PostMapping("/checkEmail")
    public ResponseEntity<?> checkEmail(Account account) {
    	log.debug("메일 중복 체크 "+account.getEmail());
        return responseEntity.get(accountValidator.validate(account));
    }

    @CustomAnnotation(EnumForCustomInterceptor.LOGIN)
    @PostMapping("/myPage")
    public ResponseEntity mypage(HttpServletRequest request){
        String token = request.getHeader("token");
        Map userMap =jwt.parseToken(token);
        String email = (String)userMap.get("subject");

        return responseEntity.get( accountValidator.userExistencyCheck(email) );
    }

    @CustomAnnotation(EnumForCustomInterceptor.LOGIN)
    @DeleteMapping("/myPage/deleteAccount")
    public ResponseEntity withdraw(HttpServletRequest request){

        String token = request.getHeader("token");
        Map userMap = jwt.parseToken(token);
        String deletingUserEmail = (String)userMap.get("subject");

        ResultMessage resultMessage = accountValidator.userExistencyCheck(deletingUserEmail);
        if(resultMessage.isImmediateReturn()){
            return responseEntity.get(resultMessage);
        }

        Iterable<Post> userPosts = postService.findByWriter(deletingUserEmail);
        List<Integer> deletingPostsIdx  = StreamSupport.stream(userPosts.spliterator(), false).map(Post::getPostIdx).collect(Collectors.toList());
        deletingPostsIdx.stream().forEach(commentService::deleteByPostIdx); //게시글의 코멘트를 삭제

        postService.deleteAll(userPosts); //유저의 게시글 삭제
        Iterable<Comment> userComments = commentService.findByCommentWriter(deletingUserEmail);
        commentService.deleteAll(userComments); //유저의 코멘트 삭제
        accountService.delete(deletingUserEmail);
        return responseEntity.get(resultMessage);
        

    }

    @CustomAnnotation(EnumForCustomInterceptor.LOGIN)
    @PutMapping(value="/myPage/update", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity update(HttpServletRequest request, @RequestBody AccountDto accountDto){
        HashMap<String, String> resultMap = new HashMap<>();

        String token = request.getHeader("token");
        Map userMap = jwt.parseToken(token);
        String email =(String)userMap.get("subject");
        log.debug("정보 변경 "+email);

        String name = accountDto.getName();
        String password = accountDto.getPassword();

        return responseEntity.get(accountService.editNameAndPassword(email, name, password));

    }
    
    @CustomAnnotation(EnumForCustomInterceptor.LOGIN)
    @PostMapping(value="/myPage/update", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> startUpdate(HttpServletRequest request, @RequestBody AccountDto accountDto){

        String token =request.getHeader("token");
        Map userMap = jwt.parseToken(token);
        String email =(String)userMap.get("subject");
        log.info(email + " 정보 변경 > 패스워드로 본인 확인 : "+accountDto.getPassword());
        Account account = accountService.castAccountDtoToAccount(accountDto);
        account.setEmail(email);
        //login 쪽에서 사용하는 메서드 재사용하기 때문에 Dto를 굳이 account로 변환해서  
        
        return responseEntity.get(accountService.login(account));

    }



	
	
}

