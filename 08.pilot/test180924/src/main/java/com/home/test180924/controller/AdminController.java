package com.home.test180924.controller;

import com.home.test180924.entity.AccountDto;
import com.home.test180924.entity.Searching;
import com.home.test180924.entity.enumForEntity.Auth;
import com.home.test180924.entity.enumForEntity.UserInfoCategory;
import com.home.test180924.service.jwt.JWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import com.home.test180924.controller.responseUtil.EntityForResponse;
import com.home.test180924.entity.Account;
import com.home.test180924.service.interfaces.AccountService;
import com.home.test180924.service.interfaces.CommentService;
import com.home.test180924.service.interfaces.PostService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@RestController
public class AdminController {

    private AccountService accountService;
    private PostService postService;
    private CommentService commentService;
    private EntityForResponse responseEntity;
    private JWT jwt;


    public AdminController(AccountService accountService, PostService postService, CommentService commentService, EntityForResponse responseEntity, JWT jwt) {
        this.accountService = accountService;
        this.postService = postService;
        this.commentService = commentService;
        this.responseEntity = responseEntity;
        this.jwt = jwt;
    }


    @GetMapping("/admin")
    public Page allUsers(HttpServletRequest request){
        log.info("관리자 페이지 ");
        int page=0;
        if(request.getParameter("page")!=null){
            page = Integer.parseInt(request.getParameter("page"))-1;
        }
        Page<Account> users = accountService.getAllUsers(page,  Integer.parseInt(request.getParameter("elementsNumberForOnePage")));
        return users;
    }

    @PostMapping("/admin/search")
    public HashMap<String, Iterable> findUsers(@RequestBody AccountDto accountDto, HttpServletRequest request){//@PathVariable("1") String keyword //어... 메일에 . 때문에 url에 문제 생겨서 get하고 rest로 하기 어려울 거 같아.
        String keyword =accountDto.getEmail();
        log.info("멤버 검색 "+keyword);
        int page=0;
        if(request.getParameter("page")!=null){
            page = Integer.parseInt(request.getParameter("page"))-1;
        }

        Iterable<Account> users =accountService.findAccountsWithPage(keyword, page, Integer.parseInt(request.getParameter("elementsNumberForOnePage")));
        HashMap<String, Iterable> userMap = new HashMap();
        userMap.put("users", users);
       // Page<Account> users =accountService.findAccountsWithPageTest(keyword, page, Integer.parseInt(request.getParameter("elementsNumberForOnePage")));
        return userMap;
    }

//    @GetMapping("/admin/search") //겟으로 안한이유는.... page관련 파라미터도 있는데 굳이 전송할 데이터 uri에 일일이 파라미터로 지정하고 uri 인코딩해서 보내고 싶지 않았기 때문
//    public void findUsers(Account account, HttpServletRequest request){//@PathVariable("1") String keyword //어... 메일에 . 때문에 url에 문제 생겨서 get하고 rest로 하기 어려울 거 같아.
//        log.info("get 검색~ "+account);
//    }






    @PutMapping(value="/admin/{1}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public HashMap<String, String> changeUserStatus(@RequestBody Searching searching){
        String userEmail = searching.getKeyword();
        String state = searching.getCategory();
        log.debug(userEmail+" 유저 상태 변경 "+state);
        HashMap<String, String> resultMap = new HashMap<>();
        Optional<Account> userAccount = Optional.ofNullable(accountService.findByEmail(userEmail));


        if(!userAccount.isPresent()){
            log.debug("유저의 상태를 변경하려 했으나, 이미 탈퇴함..");
            resultMap.put("result", "fail");
            resultMap.put("message",  "없는 계정입니다");
            return resultMap;
        }
        if(UserInfoCategory.AUTH.toString().equals(state)){
            accountService.changeAuth(userAccount.get());
        }else if(UserInfoCategory.STATUS.toString().equals(state)){
            accountService.changeStatus(userAccount.get());
        }

        resultMap.put("result", "success");
        return resultMap;
    }





}
