package com.home.test180924.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.home.test180924.service.validator.AccountValidator;
import com.home.test180924.util.PasswordEncryptUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import com.home.test180924.controller.responseUtil.EntityForResponse;
import com.home.test180924.entity.Account;
import com.home.test180924.service.interfaces.AccountService;
import com.home.test180924.service.jwt.JWT;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Optional;

@Slf4j
@RestController
public class MainController {

    private AccountService accountService;
    private EntityForResponse responseEntity;
    private JWT jwt;
    private AccountValidator accountValidator;
    private PasswordEncryptUtil passwordEncryptUtil;

    public MainController(AccountService accountService, EntityForResponse responseEntity, JWT jwt, AccountValidator accountValidator, PasswordEncryptUtil passwordEncryptUtil) {
        this.accountService = accountService;
        this.responseEntity = responseEntity;
        this.jwt = jwt;
        this.accountValidator = accountValidator;
        this.passwordEncryptUtil = passwordEncryptUtil;
    }


    @PostMapping("/login")
    public ResponseEntity<?>  login(Account account) {
    	log.info("로그인 "+account.getEmail());

        HashMap loginMap = accountService.login(account);

        if(loginMap.get("result").equals("fail")){
            return responseEntity.get(loginMap);
        }

        return responseEntity.get(loginMap);
    }


    @PostMapping("/join")
    public ResponseEntity<?> join(Account account) {
        log.info("가입 "+account);
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
        return responseEntity.get(resultMap);
    }


    @PostMapping("/checkEmail")
    public ResponseEntity<?> checkEmail(Account account) {
    	log.info("메일 중복 체크 "+account.getEmail());
        HashMap<String, String> resultMap = new HashMap<>();
        if( accountValidator.duplicatedCheck(account.getEmail()) ){
            resultMap.put("message", "이미 존재하는 계정입니다");
            return responseEntity.get(resultMap);
        }
            resultMap.put("message", "사용하셔도 좋은 메일입니다!");
            return  responseEntity.get(resultMap);

    }


//아래로 데이터 변경 확인 메시지 리턴 처리 안됐음 ㅠ
    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request){
        log.info("로그아웃 ");
        return responseEntity.get("로그아웃 됨..");
    }

    @DeleteMapping("/deleteAccount")
    public void withdraw(Account account){
        accountService.delete(account);
    }

    @PutMapping("/update")
    public Account update(Account account, Errors errors){
    	log.info("정보 변경 "+account);
        return accountService.changePassword(account, errors);
    }




	
	
	
}

