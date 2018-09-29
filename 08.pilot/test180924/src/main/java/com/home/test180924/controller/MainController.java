package com.home.test180924.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.home.test180924.controller.responseUtil.EntityForResponse;
import com.home.test180924.entity.Account;
import com.home.test180924.service.interfaces.AccountService;
import com.home.test180924.service.jwt.TestJWT;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@RestController
public class MainController {




    private AccountService accountService;
    private EntityForResponse responseEntity;

    private TestJWT jwt;

    public MainController(AccountService accountService, EntityForResponse responseEntity, TestJWT jwt) {
        this.accountService = accountService;
        this.responseEntity = responseEntity;

        this.jwt = jwt;
    }



    @GetMapping("/")
    public ResponseEntity<?> home(HttpServletRequest request) {
//        HttpSession session = request.getSession();
//        session.setAttribute("loginCheck",false);
        return responseEntity.get("반환 문자열.");

    }
    

    @GetMapping("/test")
    public ResponseEntity<?> test(HttpServletRequest request) {
//        HttpSession session = request.getSession();
//        session.setAttribute("loginCheck",false);
        
        jwt.parseToken(request.getParameter("token"));
        return responseEntity.get("반환 문자열.");

    }
    
    
    

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request){
        System.out.println("로그아웃 ");
//        request.getSession().invalidate();
//        request.getSession().setAttribute("loginCheck",false);
        return responseEntity.get("로그아웃 됨..");
    }

    @PostMapping("/login") //생각해보니 로그인 할 때에만 responseEntity로 돌려주면 되는거 아닌가..? 그 외에는 따로 서버에서 헤더에 값을 줘야할 게 없는거같은데
    public ResponseEntity<?>  login(Account account, HttpServletRequest request, Errors errors) {
//        System.out.println("로그인 email "+account.getEmail());
//        System.out.println("로그인 pw "+account.getPassword());

        //이...이거 어떠캄...?ㅋㅋ
        HashMap loginMap = (HashMap)accountService.login(account, request, errors);
        String token = (String)loginMap.get("token");
        return  ResponseEntity.ok().header("Authorization", token).build();//responseEntity.get(accountService.login(account, request, errors));
    }


    @PostMapping("/join")
    public ResponseEntity<?> join(Account account, Errors errors) {
        System.out.println("가입 "+account);
        
        return  responseEntity.get(accountService.join(account, errors));
    }

    @DeleteMapping("/deleteAccount")
    public void withdraw(Account account, HttpServletRequest request){
//        System.out.println("탈퇴 "+account);
//        request.getSession().invalidate();
//        request.getSession().setAttribute("loginCheck",false);
        accountService.delete(account);
    }

    @PutMapping("/update")
    public Account update(Account account, Errors errors){
        System.out.println("정보 변경 "+account);
        return accountService.changePassword(account, errors);

    }




	
	
	
}

