package com.home.test180924.controller;

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

@Slf4j
@RestController
public class AdminController {

    private AccountService accountService;
    private PostService postService;
    private CommentService commentService;
    private EntityForResponse responseEntity;


    public AdminController(AccountService accountService, PostService postService, CommentService commentService, EntityForResponse responseEntity) {
        this.accountService = accountService;
        this.postService = postService;
        this.commentService = commentService;
        this.responseEntity = responseEntity;
    }


    @GetMapping("/admin")
    public ResponseEntity allUsers(HttpServletRequest request){
        System.out.println("멤버 목록");
        int page=0;
        if(request.getParameter("page")!=null){
            page = Integer.parseInt(request.getParameter("page"))-1;
        }
        return responseEntity.get(accountService.getAllUsers(page, Integer.parseInt(request.getParameter("elementsNumberForOnePage"))));
    }

    @GetMapping("/admin/{1}")
    public ResponseEntity<?> findUsers(@PathVariable("1") String keyword, HttpServletRequest request){
        System.out.println("멤버 검색");
        int page=0;
        if(request.getParameter("page")!=null){
            page = Integer.parseInt(request.getParameter("page"))-1;
        }

//        return responseEntity.get(accountService.findAccountsWithPage(keyword));
        return responseEntity.get(accountService.findAccountsWithPage(keyword, page, Integer.parseInt(request.getParameter("elementsNumberForOnePage"))));
    }

    @PutMapping("/admin/{1}")
    public ResponseEntity changeUserStatus(Account account, Errors errors){ //@PathVariable("1") String userEmail, 안써..
        System.out.println("멤버 상태 변경?");
        return responseEntity.get(accountService.changeStatus(account, errors));
    }





}
