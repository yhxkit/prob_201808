package com.home.test180924.controller;

import com.home.test180924.entity.AccountDto;
import com.home.test180924.entity.enumForEntity.Auth;
import com.home.test180924.service.jwt.TestJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import com.home.test180924.controller.responseUtil.EntityForResponse;
import com.home.test180924.entity.Account;
import com.home.test180924.service.interfaces.AccountService;
import com.home.test180924.service.interfaces.CommentService;
import com.home.test180924.service.interfaces.PostService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class AdminController {

    private AccountService accountService;
    private PostService postService;
    private CommentService commentService;
    private EntityForResponse responseEntity;
    private TestJWT jwt;


    public AdminController(AccountService accountService, PostService postService, CommentService commentService, EntityForResponse responseEntity, TestJWT jwt) {
        this.accountService = accountService;
        this.postService = postService;
        this.commentService = commentService;
        this.responseEntity = responseEntity;
        this.jwt = jwt;
    }


    @GetMapping("/admin")
    public Map allUsers(HttpServletRequest request){

        System.out.println("멤버 목록");
        String token = request.getHeader("token");
        Map checkAdmin = jwt.parseToken(token);

        if(Auth.ADMIN==checkAdmin.get("scope")){
            log.debug("관리자 권한 있음");
        }else{
            log.debug("관리자 권한 없음");
            //없는 경우의 밸리데이션 처리~
        }

        int page=0;
        if(request.getParameter("page")!=null){
            page = Integer.parseInt(request.getParameter("page"))-1;
        }


        List<Account> users = accountService.getAllUsers(page,  Integer.parseInt(request.getParameter("elementsNumberForOnePage")));
        long totalUser = accountService.getTotalUser();
        Map userMap = new HashMap();
        userMap.put("users", users);
        userMap.put("totalUser", totalUser);

        return userMap;
    }

    @PostMapping("/admin/search")
    public Map findUsers(@RequestBody AccountDto accountDto, HttpServletRequest request){//@PathVariable("1") String keyword //어... 메일에 . 때문에 url에 문제 생겨서 get하고 rest로 하기 어려울 거 같아.
        String keyword =accountDto.getEmail();
        System.out.println("멤버 검색"+keyword);
        int page=0;
        if(request.getParameter("page")!=null){
            page = Integer.parseInt(request.getParameter("page"))-1;
        }

        Iterable<Account> users =accountService.findAccountsWithPage(keyword, page, Integer.parseInt(request.getParameter("elementsNumberForOnePage")));
        long totalUser = accountService.getTotalUser();
        Map userMap = new HashMap();
        userMap.put("users", users);
//        userMap.put("totalUser", users.);

        return userMap;

    }

    @PutMapping("/admin/{1}")
    public ResponseEntity changeUserStatus(@RequestBody AccountDto accountDto, HttpServletRequest request, Errors errors){ //@PathVariable("1") String userEmail, 안써.. // 여기 이메일에는 . 들어가서 url로 받을 때 오류...;
        String token =request.getHeader("token");
        Map checkAdmin = jwt.parseToken(token);
        if(Auth.ADMIN==checkAdmin.get("scope")){
            log.debug("관리자 권한 있음");
        }else{
            log.debug("관리자 권한 없음");
            //없는 경우의 밸리데이션 처리~
        }
        return responseEntity.get(accountService.changeStatus(accountDto, errors));
    }





}
