package com.home.test180924.controller;

import com.home.test180924.entity.AccountDto;
import com.home.test180924.entity.Searching;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.home.test180924.controller.interceptor.CustomAnnotation;
import com.home.test180924.controller.interceptor.EnumForCustomInterceptor;
import com.home.test180924.controller.responseUtil.EntityForResponse;
import com.home.test180924.entity.Account;
import com.home.test180924.service.interfaces.AccountService;
@Slf4j
//@CustomAnnotation(EnumForCustomInterceptor.ADMIN)//어노테이션 설정을 TYPE_USE로 하면 되긴 하는데... 그럼 인터셉터에서 어떻게...
@RestController
public class AdminController {

    private AccountService accountService;
    private EntityForResponse responseEntity;


    public AdminController(AccountService accountService, EntityForResponse responseEntity) {
        this.accountService = accountService;
        this.responseEntity = responseEntity;
    }

    @CustomAnnotation(EnumForCustomInterceptor.ADMIN)
    @GetMapping("/admin")
    public Page allUsers(@RequestParam(value="page", required = false, defaultValue = "1") int page, @RequestParam(value="elementsNumberForOnePage") int elementsNumberForOnePage){
        log.info("관리자 페이지 "+page);
        Page<Account> users = accountService.getAllUsers(page-1,  elementsNumberForOnePage);
        return users;
    }
    
    @CustomAnnotation(EnumForCustomInterceptor.ADMIN)
    @PostMapping("/admin/search")
    public ResponseEntity<?> findUsers(@RequestBody AccountDto accountDto,  @RequestParam(value="page", required = false, defaultValue = "1") int page, @RequestParam(value="elementsNumberForOnePage") int elementsNumberForOnePage){
        String keyword =accountDto.getEmail();
        log.info("멤버 검색 "+keyword);
        Iterable<Account> users =accountService.findAccountsWithPage(keyword, page-1, elementsNumberForOnePage);
        return responseEntity.get(users);
    }

    @CustomAnnotation(EnumForCustomInterceptor.ADMIN)
    @PutMapping(value="/admin/{1}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> changeUserStatus(@RequestBody Searching searching){
        String userEmail = searching.getKeyword();
        String state = searching.getCategory();
        log.debug(userEmail+" 유저 권한/상태 변경 "+state);
        return responseEntity.get(accountService.changeUserState(userEmail, state));
    }





}
