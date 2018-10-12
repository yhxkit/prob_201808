package com.home.test180924.service;


import com.home.test180924.controller.responseUtil.ResultMessage;
import com.home.test180924.entity.enumForEntity.UserInfoCategory;
import com.home.test180924.service.jwt.JWT;
import com.home.test180924.service.validator.AccountValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.home.test180924.entity.Account;
import com.home.test180924.repository.AccountRepository;
import com.home.test180924.service.interfaces.AccountService;
import com.home.test180924.util.Paging;
import com.home.test180924.util.PasswordEncryptUtil;

import java.util.*;

@Slf4j
@Service
@Transactional
public class AccountServiceImpl implements AccountService {

	private AccountRepository accountRepository;
	private PasswordEncryptUtil passwordEncryptUtil;
	private Paging paging;
	private JWT jwt;
	private AccountValidator accountValidator;



	//autowired 보다 생성자 방식이 선호된다고 함.. 코드는 길어지지만 테스트등에서 해당 객체를 생성해서 사용하고 싶을때, autowired로 주입받은 필드에 대해 테스트가 불가해서..
    public AccountServiceImpl(AccountRepository accountRepository, PasswordEncryptUtil passwordEncryptUtil, Paging paging, JWT jwt, AccountValidator accountValidator) {
        this.accountRepository = accountRepository;
        this.passwordEncryptUtil = passwordEncryptUtil;
        this.paging = paging;
        this.jwt = jwt;
        this.accountValidator = accountValidator;
    }

    @Override
    public ResultMessage join(Account account){

        ResultMessage resultMessage = accountValidator.validate(account);
        if(resultMessage.isImmediateReturn()){
            return resultMessage;
        }

        resultMessage.setMessage("가입을 축하합니다~");
        account.setPassword(passwordEncryptUtil.encrypt(account.getPassword()));
        accountRepository.save(account);
        log.info("가입 완료 "+account.getEmail());
        return resultMessage;
	}


    @Override
	public ResultMessage<?> login(Account account){

        ResultMessage resultMessage = accountValidator.userExistencyCheck(account.getEmail());// 로그인 성공시 dat값 token 값 들어간 Map으로 변경할거라서 제네릭 안씀..
        if(resultMessage.isImmediateReturn()){
            log.debug("로그인 시도 (계정 없음)");
            return resultMessage;
        }

        Account checkAccount = (Account) resultMessage.getData();
        resultMessage = accountValidator.loginPasswordCheck(checkAccount, passwordEncryptUtil.encrypt(account.getPassword()));
        if(resultMessage.isImmediateReturn()){
            log.debug("로그인 시도 (비밀번호 틀림)");
            return resultMessage;
        }

        log.debug("로그인한 유저 : "+checkAccount.getEmail());

        Map<String, Object> loginSuccessResult = new HashMap();
        loginSuccessResult.put("token", jwt.getJwt(checkAccount));
        loginSuccessResult.put("user", checkAccount);
        resultMessage.setData(loginSuccessResult);

        return resultMessage;
    }

    @Override
    public Account changeAuth(Account account) {

        account.setAuth( account.getAuth().changeAuth() );
        log.info(account.getEmail()+"의 권한 변경 "+account.getAuth());
        accountRepository.save(account);
        return accountRepository.findByEmail(account.getEmail());
    }

    @Override
    public Account changeStatus(Account account){

        account.setStatus( account.getStatus().changeStatus() );
        log.info(account.getEmail()+"의 상태 변경 "+account.getStatus());
        accountRepository.save(account);
        return accountRepository.findByEmail(account.getEmail());
    }

    @Override
    public ResultMessage<Account> changeUserState(String userEmail, String state) {

        ResultMessage<Account> resultMessage = accountValidator.userExistencyCheck(userEmail);
        if(resultMessage.isImmediateReturn()){
            return resultMessage;
        }
        Account user = resultMessage.getData();

        if(UserInfoCategory.AUTH.toString().equals(state)){
            resultMessage.setData(changeAuth(user));
        }else if(UserInfoCategory.STATUS.toString().equals(state)) {
            resultMessage.setData(changeStatus(user));
        }

        return resultMessage;
    }

    @Override
    public void delete(String email){
        accountRepository.delete(accountRepository.findByEmail(email));
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
    public Account findByCommentsCommentIdx(int commentIdx) {
    	return accountRepository.findByCommentsCommentIdx(commentIdx);
    }

    @Override
    public ResultMessage editNameAndPassword(String email, String newName, String newPassword) {

        ResultMessage<Account> resultMessage = accountValidator.userExistencyCheck(email);
        if(resultMessage.isImmediateReturn()){
            return resultMessage;
        }

        Account account = resultMessage.getData();

        account.setName(newName);
        account.setPassword(passwordEncryptUtil.encrypt(newPassword));
        accountRepository.save(account);
        resultMessage.setData(account);
        log.debug("정보 변경 완료 "+ email);

        return resultMessage;
    }
}
