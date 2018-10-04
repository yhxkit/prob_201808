package com.home.test180924.service;


import com.home.test180924.entity.AccountDto;
import com.home.test180924.entity.enumForEntity.Status;
import com.home.test180924.service.jwt.JWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import com.home.test180924.entity.Account;
import com.home.test180924.repository.AccountRepository;
import com.home.test180924.service.interfaces.AccountService;
import com.home.test180924.service.validator.AccountValidator;
import com.home.test180924.util.Paging;
import com.home.test180924.util.PasswordEncryptUtil;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Slf4j
@Service
@Transactional
public class AccountServiceImpl implements AccountService {

	private AccountRepository accountRepository;
	private AccountValidator accountValidator;
	private PasswordEncryptUtil passwordEncryptUtil;
	private MessageSource messageSource;
	private Paging paging;
	private JWT jwt;

	//autowired 보다 생성자 방식이 선호된다고 함.. 코드는 길어지지만 테스트등에서 해당 객체를 생성해서 사용하고 싶을때, autowired로 주입받은 필드에 대해 테스트가 불가해서..
    public AccountServiceImpl(AccountRepository accountRepository, PasswordEncryptUtil passwordEncryptUtil, AccountValidator accountValidator, MessageSource messageSource,  Paging paging, JWT jwt) {
        this.accountRepository = accountRepository;
        this.passwordEncryptUtil = passwordEncryptUtil;
        this.accountValidator = accountValidator;
        this.messageSource = messageSource;
        this.paging = paging;
        this.jwt = jwt;
    }




    @Override
    public boolean join(Account account){
        account.setPassword(passwordEncryptUtil.encrypt(account.getPassword()));
        accountRepository.save(account);
        log.info("가입 "+account.getEmail());
        return true; //여기서 성공 실패를 반환할 필요가 있나?
	}


    @Override
	public HashMap login(Account account){

        HashMap resultMap = new HashMap();
		Account checkAccount = accountRepository.findByEmail(account.getEmail());

            if(checkAccount == null){
                resultMap.put("result", "fail");
                resultMap.put("message", "없는 계정입니다.");
                log.debug("계정없음");
                return  resultMap;
            }

            if(!checkAccount.getPassword().equals(passwordEncryptUtil.encrypt(account.getPassword()))){
                resultMap.put("result", "fail");
                resultMap.put("message", "비밀번호가 틀렸습니다.");
                log.debug("비번틀림");
                return  resultMap;
            }

        log.debug("가입된 계정은 "+checkAccount.getEmail());
        log.debug("들어온 패스"+passwordEncryptUtil.encrypt(account.getPassword()));
        log.debug("db 패스"+checkAccount.getPassword());

        resultMap.put("result", "success");
        resultMap.put("token", jwt.getJwt(checkAccount));
        resultMap.put("user", checkAccount);

        return  resultMap;
    }

    @Override
	public Account changePassword(Account account, Errors errors){ // 그냥 update 로 changeStatus 메서드를 통합하고, null 아닌...걸로 하면 권한 관련해서 status 변경에 문제가 있겠지..?
		Account checkAccount = accountRepository.findByEmail(account.getEmail());
        checkAccount.setPassword(passwordEncryptUtil.encrypt(account.getPassword()));
        accountRepository.save(checkAccount);
		return accountRepository.findByEmail(account.getEmail());
	}

	@Override
    public Account changeStatus(AccountDto accountDto, Errors errors){

        Account checkAccount = accountRepository.findByEmail(accountDto.getEmail());

        if(Status.ENABLE==checkAccount.getStatus()){
            checkAccount.setStatus(Status.DISABLE);
        }else if(Status.DISABLE==checkAccount.getStatus()){
            checkAccount.setStatus(Status.ENABLE);
        }

        accountRepository.save(checkAccount);
        return accountRepository.findByEmail(checkAccount.getEmail());
    }

    @Override
    public void delete(Account account){
        accountRepository.delete(account);
    }

    @Override
    public Page<Account> getAllUsers(int requiredUserListPage,int elementsNumberForOnePage){
        Page<Account> userList = accountRepository.findAll(paging.elementsByPage(requiredUserListPage, elementsNumberForOnePage, "accountCreatedTime"));
        return userList;
    }


    @Override
    public Iterable<Account> findAccountsWithPage(String keyword, int page, int elementsNumberForOnePage){
        return accountRepository.findAllByEmailContainingOrderByAccountCreatedTimeDesc(keyword);
    }


    @Override
    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    @Override
    public Account findByPostsPostIdx(int postIdx) {
        return accountRepository.findByPostsPostIdx(postIdx);
    }

    @Override
    public long getTotalUser() {
        long totalUser = accountRepository.count();
        log.debug(totalUser+"명의 유저 존재");
        return totalUser;
    }


/*
    @Override
    public boolean duplicatedCheck(String email, HttpServletResponse response, Errors errors) {
        Optional<Account> checkAccount = accountRepository.findById(email);

        if(checkAccount.isPresent()){
            try {
                response.sendError(406, "중복된 이메일입니다.");
            }catch (Exception e){
                log.debug(e.toString());
              }
            errors.rejectValue("email", "duplicatedEmail");
            return true;
        }
        return false;
    }
*/


    /*
        @Override
        public boolean errorCheck(Errors errors, final String value) {
            return errors.getAllErrors().stream().filter(error -> value.equals(error.getCode())).count() > 0;
        }
    */

}
