package com.home.test180924.controller;

import com.home.test180924.entity.AccountDto;
import com.home.test180924.entity.enumForEntity.Auth;
import com.home.test180924.service.jwt.JWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import com.home.test180924.controller.responseUtil.EntityForResponse;
import com.home.test180924.entity.Account;
import com.home.test180924.service.interfaces.AccountService;
import com.home.test180924.service.interfaces.CommentService;
import com.home.test180924.service.interfaces.PostService;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
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
    public Map findUsers(@RequestBody AccountDto accountDto, HttpServletRequest request){//@PathVariable("1") String keyword //어... 메일에 . 때문에 url에 문제 생겨서 get하고 rest로 하기 어려울 거 같아.
        String keyword =accountDto.getEmail();
        log.info("멤버 검색 "+keyword);
        int page=0;
        if(request.getParameter("page")!=null){
            page = Integer.parseInt(request.getParameter("page"))-1;
        }

        Iterable<Account> users =accountService.findAccountsWithPage(keyword, page, Integer.parseInt(request.getParameter("elementsNumberForOnePage")));
        long totalUser = accountService.getTotalUser();
        Map userMap = new HashMap();
        userMap.put("users", users);
        userMap.put("totalUser", ((Collection<Account>)users).size());

       // Page<Account> users =accountService.findAccountsWithPageTest(keyword, page, Integer.parseInt(request.getParameter("elementsNumberForOnePage")));

        return userMap;

    }

    @PutMapping("/admin/{1}")
    public ResponseEntity changeUserStatus(@RequestBody AccountDto accountDto, Errors errors){ //@PathVariable("1") String userEmail, 안써.. // 여기 이메일에는 . 들어가서 url로 받을 때 오류...;
        return responseEntity.get(accountService.changeStatus(accountDto, errors));
    }





}
