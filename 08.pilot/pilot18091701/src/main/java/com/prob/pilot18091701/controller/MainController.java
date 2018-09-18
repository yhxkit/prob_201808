package com.prob.pilot18091701.controller;

import com.prob.pilot18091701.entity.Account;
import com.prob.pilot18091701.repository.AccountRepository;
import com.prob.pilot18091701.repository.CommentRepository;
import com.prob.pilot18091701.repository.PostRepository;
import com.prob.pilot18091701.service.AccountService;
import com.prob.pilot18091701.service.CommentService;
import com.prob.pilot18091701.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MainController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountService accountService;


    @GetMapping("/")
    public String home() {
        return "home";
    }




    @PostMapping("/login")
    public Account login(Account account) {
        log.info("로그인 "+account.getPassword());
        return accountService.login(account);
    }

    @GetMapping
    public void logout(){
        log.info("로갓..");
    }


    @PostMapping("/join")
    public Iterable<Account> join(Account account) {
        log.info("가입 "+account);
        return accountService.join(account);

    }

    @PutMapping("/update")
    public Account update(Account account){
        log.info("정보 변경 "+account);

        return accountService.update(account);

    }





}
