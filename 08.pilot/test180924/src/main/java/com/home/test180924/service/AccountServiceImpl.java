package com.home.test180924.service;


import com.home.test180924.entity.AccountDto;
import com.home.test180924.entity.enumForEntity.Status;
import com.home.test180924.service.jwt.TestJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import com.home.test180924.entity.Account;
import com.home.test180924.entity.enumForEntity.Auth;
import com.home.test180924.repository.AccountRepository;
import com.home.test180924.service.interfaces.AccountService;
import com.home.test180924.service.validator.AccountValidator;
import com.home.test180924.util.Paging;
import com.home.test180924.util.PasswordEncryptUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
	private TestJWT jwt;

	//autowired 보다 생성자 방식이 선호된다고 함.. 코드는 길어지지만 테스트등에서 해당 객체를 생성해서 사용하고 싶을때, autowired로 주입받은 필드에 대해 테스트가 불가해서..
    public AccountServiceImpl(AccountRepository accountRepository, PasswordEncryptUtil passwordEncryptUtil, AccountValidator accountValidator, MessageSource messageSource,  Paging paging, TestJWT jwt) {
        this.accountRepository = accountRepository;
        this.passwordEncryptUtil = passwordEncryptUtil;
        this.accountValidator = accountValidator;
        this.messageSource = messageSource;
        this.paging = paging;
        this.jwt = jwt;
    }


    @Override
    public boolean errorCheck(Errors errors, final String value) {
//        errors.getAllErrors().stream().map(error -> { return error.getCode();}).forEach(System.out::println);
        return errors.getAllErrors().stream().filter(error -> value.equals(error.getCode())).count() > 0;
    }


    @Override
    public void join(Account account,  HttpServletResponse response, Errors errors){// Errors 는 커맨드 객체보다 반드시 뒤에 위치할것;

        accountValidator.validate(account, errors);

        try {

        if(errors.hasErrors()){
            if (errorCheck(errors, "invalidEmail")) {
                response.sendError(406, "이메일 형식을 지켜주세요");
             //   System.out.println(messageSource.getMessage("invalidEmail.email", new Object[1], Locale.getDefault()));
                return;
            } else if (errorCheck(errors, "required")) {
                response.sendError(406, "모든 항목을 채워주세요");
               // System.out.println(messageSource.getMessage("required", new Object[0], Locale.getDefault()));
                return;
            }
        }

        Optional<Account> checkAccount = accountRepository.findById(account.getEmail());//...?

        if(checkAccount.isPresent()){
            response.sendError(406, "중복된 이메일입니다.");
            errors.rejectValue("email", "duplicatedEmail");
//            System.out.println(messageSource.getMessage("duplicatedEmail", new Object[0], Locale.getDefault()));
            return;
        }
        }catch (Exception e){
            log.debug("가입 중 에러 반환 오류.."+e);
        }

        account.setPassword(passwordEncryptUtil.encrypt(account.getPassword()));
        accountRepository.save(account);

        log.info("가입됨"+account.getEmail());
        //return  account;
	}


    @Override
	public HashMap login(Account account, HttpServletResponse response, Errors errors){

		Account checkAccount = accountRepository.findByEmail(account.getEmail());

		try {
            if(checkAccount == null){
                response.sendError(500, "계정이 없습니다.> 근데 여기 상세하게 써주면 안될듯;");
               // errors.rejectValue("email", "NoneExistingAccount");
                log.debug("계정없음");
                return null;
            }

            if(!checkAccount.getPassword().equals(passwordEncryptUtil.encrypt(account.getPassword()))){
                //errors.rejectValue("email", "WrongPassword");
                response.sendError(500, "비번이 틀렸습니다> 근데 여기 상세하게 써주면 안될듯;");
                log.debug("비번틀림");
                return null;
            }
        }catch (Exception e){
		    log.debug(e.toString());
        }

        System.out.println("가입된 계정은 "+checkAccount.getEmail());
        System.out.println("들어온 패스"+passwordEncryptUtil.encrypt(account.getPassword()));
        System.out.println("db 패스"+checkAccount.getPassword());


        HashMap resultMap = new HashMap();
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
    public List<Account> getAllUsers(int requiredUserListPage,int elementsNumberForOnePage){
        Page<Account> userList = accountRepository.findAll(paging.elementsByPage(requiredUserListPage, elementsNumberForOnePage, "accountCreatedTime"));
        List<Account> resultList = new ArrayList<>();
        resultList.addAll(userList.getContent());

        return resultList;
    }


    @Override
    public Iterable<Account> findAccountsWithPage(String keyword, int page, int elementsNumberForOnePage){
//        return accountRepository.findByEmailIgnoreCaseContainingOrderByAccountIdx(keyword, PageRequest.of(0, 10)); //오류
//        return accountRepository.findByEmailIgnoreCaseContainingOrderByAccountCreatedTimeDesc(keyword);

    //   return accountRepository.findAllByEmailContaining(keyword, paging.elementsByPage(page, elementsNumberForOnePage, "accountCreatedTime") );
        return accountRepository.findAllByEmailContainingOrderByAccountCreatedTimeDesc(keyword);

    }

    @Override
    public long getTotalUser() {
        long totalUser = accountRepository.count();
        log.debug(totalUser+"명의 유저 존재");
        return totalUser;
    }

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
}
