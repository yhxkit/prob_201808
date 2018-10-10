package com.home.test180924.controller;

import javax.servlet.http.HttpServletRequest;
import com.home.test180924.entity.AccountDto;
import com.home.test180924.entity.Comment;
import com.home.test180924.entity.Post;
import com.home.test180924.service.interfaces.CommentService;
import com.home.test180924.service.interfaces.PostService;
import com.home.test180924.service.validator.AccountValidator;
import com.home.test180924.util.PasswordEncryptUtil;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.home.test180924.controller.responseUtil.EntityForResponse;
import com.home.test180924.entity.Account;
import com.home.test180924.service.interfaces.AccountService;
import com.home.test180924.service.jwt.JWT;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    private PasswordEncryptUtil passwordEncryptUtil;

    public MainController(AccountService accountService, CommentService commentService, PostService postService,EntityForResponse responseEntity, JWT jwt, AccountValidator accountValidator, PasswordEncryptUtil passwordEncryptUtil) {
        this.accountService = accountService;
        this.postService = postService;
        this.commentService = commentService;
        this.responseEntity = responseEntity;
        this.jwt = jwt;
        this.accountValidator = accountValidator;
        this.passwordEncryptUtil = passwordEncryptUtil;
    }


    @PostMapping("/login")
    public ResponseEntity<?>  login(Account account) {
    	log.debug("로그인 "+account.getEmail());

        HashMap loginMap = accountService.login(account);

        if(loginMap.get("result").equals("fail")){
            return responseEntity.get(loginMap);
        }

        return responseEntity.get(loginMap);
    }


    @PostMapping("/join")
    public ResponseEntity<?> join(Account account) {
        log.debug("가입 "+account);
        HashMap<String, String> resultMap = new HashMap<>();

        if(accountValidator.validate(account)){
         //   resultMap.put("result", "fail");
            resultMap.put("message", "적합한 이메일 형식이 아닙니다");
            return responseEntity.get(resultMap);
        }

       if( accountValidator.duplicatedCheck(account.getEmail()) ){
          // resultMap.put("result", "fail");
           resultMap.put("message", "이미 존재하는 계정입니다");
           return responseEntity.get(resultMap);
       }

        accountService.join(account);
       // resultMap.put("result", "success");
        resultMap.put("message", account.getEmail()+"님, 가입을 축하합니다");
        log.debug(account.getEmail()+" 신규 가입");
        return responseEntity.get(resultMap);
    }


    @PostMapping("/checkEmail")
    public ResponseEntity<?> checkEmail(Account account) {
    	log.debug("메일 중복 체크 "+account.getEmail());
        HashMap<String, String> resultMap = new HashMap<>();
        if( accountValidator.duplicatedCheck(account.getEmail()) ){
            resultMap.put("message", "이미 존재하는 계정입니다");
            return responseEntity.get(resultMap);
        }
            resultMap.put("message", "사용하셔도 좋은 메일입니다!");
            return  responseEntity.get(resultMap);

    }

    @PostMapping("/myPage")
    public Account mypage(HttpServletRequest request){
        String token = request.getHeader("token");
        Map userMap =jwt.parseToken(token);
        String email = (String)userMap.get("subject");
        Account userAccount = accountService.findByEmail(email);

        return userAccount;
    }


    @DeleteMapping("/myPage/deleteAccount")
    public HashMap<String, String> withdraw(HttpServletRequest request){

        HashMap<String, String> resultMap = new HashMap<>();

        String token = request.getHeader("token");
        Map userMap = jwt.parseToken(token);
        String deletingUserEmail = (String)userMap.get("subject");

        Optional<Account> userAccount = Optional.ofNullable(accountService.findByEmail(deletingUserEmail));
        if(!userAccount.isPresent()){
            resultMap.put("result", "fail");
            resultMap.put("message", "없는 계정입니다.");
            return resultMap;
        }

        Iterable<Post> userPosts = postService.findByWriter(deletingUserEmail);
        List<Integer> deletingPostsIdx  = StreamSupport.stream(userPosts.spliterator(), false).map(Post::getPostIdx).collect(Collectors.toList());
        deletingPostsIdx.stream().forEach(commentService::deleteByPostIdx); //게시글의 코멘트를 삭제..

        postService.deleteAll(userPosts); //유저의 게시글 삭제...

        Iterable<Comment> userComments = commentService.findByCommentWriter(deletingUserEmail);
        commentService.deleteAll(userComments); //유저의 코멘트 삭제

        resultMap.put("result", "success");

        accountService.delete(userAccount.get());
        log.debug("계정 삭제 "+ deletingUserEmail);

        return resultMap;
    }


//    @PutMapping("/myPage/update") //put은 무조건 DTO 써야됨 ㅠ
//    public  void update(Account account, HttpServletRequest request){
//        log.info(""+account); //안됨!
//        log.info("바인딩 테스트 ");
//    }


    @PutMapping(value="/myPage/update", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public HashMap<String, String> update(HttpServletRequest request, @RequestBody AccountDto accountDto){
        HashMap<String, String> resultMap = new HashMap<>();

        String token = request.getHeader("token");
        Map userMap = jwt.parseToken(token);
        String email =(String)userMap.get("subject");
        log.debug("정보 변경 "+email);

        String name = accountDto.getName();
        String password = accountDto.getPassword();

        Optional<Account> userAccount = Optional.ofNullable( accountService.findByEmail(email));
        if(!userAccount.isPresent()){
            resultMap.put("result", "fail");
            resultMap.put("message", "없는 계정입니다.");
            return resultMap;
        }

        Account account = userAccount.get();
        account.setName(name);
        account.setPassword(password);

        accountService.editNameAndPassword(userAccount.get());
        log.debug("정보 변경 완료 "+ email);

        resultMap.put("result", "success");
        return resultMap;

    }

    @PostMapping(value="/myPage/update", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public HashMap<String, ?> startUpdate(HttpServletRequest request, @RequestBody AccountDto accountDto){

        String token =request.getHeader("token");
        Map userMap = jwt.parseToken(token);
        String email =(String)userMap.get("subject");
        log.info(email + "정보 변경 본인인지 패스워드 체크 "+accountDto.getPassword());
        Account account = new Account();
        account.setEmail(email);
        account.setPassword(accountDto.getPassword());
        HashMap<String, ?> resultMap = accountService.login(account);
        return resultMap;
    }




//아래로 구현 안됨
//    @GetMapping("/logout")
//    public ResponseEntity<?> logout(HttpServletRequest request){
//        log.debug("로그아웃..은 jwt destroy가 안돼서 그냥 웹 스토리지에서 지우기만 하는 걸로.. ");
//        return responseEntity.get("로그아웃 됨..");
//    }



	
	
}

