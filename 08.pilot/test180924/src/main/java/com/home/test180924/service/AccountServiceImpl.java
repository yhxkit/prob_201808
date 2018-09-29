package com.home.test180924.service;


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
    public Object join(Account account, Errors errors){// Errors 는 커맨드 객체보다 반드시 뒤에 위치할것;

        accountValidator.validate(account, errors);

        if(errors.hasErrors()){
            if (errorCheck(errors, "invalidEmail")) {
                System.out.println(messageSource.getMessage("invalidEmail.email", new Object[1], Locale.getDefault()));
                return "to JoinForm for invalid email";

//            } else if (check(errors, "emailIsRequired")) { //이메일 외에도 필수항목 지정..
            } else if (errorCheck(errors, "required")) {
                System.out.println(messageSource.getMessage("required", new Object[0], Locale.getDefault()));
                return "to JoinForm for that an email is required";
            }
//            else {
//                errors.rejectValue("email", "somethingElse");
//                System.out.println(messageSource.getMessage("somethingElse.account.email", new Object[1], Locale.getDefault()));
//                return "JoinForm으로... ";
//            }
        }

        Optional<Account> checkAccount = accountRepository.findById(account.getEmail());//...?

        if(checkAccount.isPresent()){
            errors.rejectValue("email", "duplicatedEmail");
//            throw new AlreadyExistingAccountException("Email is duplicated "+account.getEmail()); //...
            System.out.println(messageSource.getMessage("duplicatedEmail", new Object[0], Locale.getDefault()));
            return "to JoinForm for duplicated email";
        }

//        account.setAuth(Auth.ADMIN); //관리자로 등록
        account.setPassword(passwordEncryptUtil.encrypt(account.getPassword()));
        accountRepository.save(account);
        return  account;
	}


    @Override
	public Object login(Account account, HttpServletRequest request, Errors errors){

		HttpSession session = request.getSession();
		Account checkAccount = accountRepository.findByEmail(account.getEmail());

		System.out.println("가입된 계정은 "+checkAccount);

		if(checkAccount == null){
		    errors.rejectValue("email", "NoneExistingAccount");
		    return "to login form: 그런 계정 없음.. 로그인 폼으로...";
		}

		if(!checkAccount.getPassword().equals(passwordEncryptUtil.encrypt(account.getPassword()))){
		    errors.rejectValue("email", "WrongPassword");
		    return "to login form : 비번 틀림.. 로그인 폼으로 ";
		}


        System.out.println("들어온 패스"+passwordEncryptUtil.encrypt(account.getPassword()));
        System.out.println("db 패스"+checkAccount.getPassword());

//        session.setMaxInactiveInterval(30*60);

//        session.setAttribute("loginCheck",true);
//        System.out.println(session.getAttribute("loginCheck").toString()+" 세션 로그인 상태 ");

//        if(checkAccount.getAuth()==Auth.ADMIN){
//            session.setAttribute("authCheck",true);
//        }


        Map resultMap = new HashMap();
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
    public Account changeStatus(Account account, Errors errors){
        Account checkAccount = accountRepository.findByEmail(account.getEmail());
        checkAccount.setStatus(account.getStatus());
        accountRepository.save(checkAccount);
        return accountRepository.findByEmail(account.getEmail());
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
    public Page<Account> findAccountsWithPage(String keyword, int page, int elementsNumberForOnePage){
//        return accountRepository.findByEmailIgnoreCaseContainingOrderByAccountIdx(keyword, PageRequest.of(0, 10)); //오류
//        return accountRepository.findByEmailIgnoreCaseContainingOrderByAccountCreatedTimeDesc(keyword);

       return accountRepository.findAllByEmailContaining(keyword, paging.elementsByPage(page, elementsNumberForOnePage, "accountCreatedTime") );


    }







}
