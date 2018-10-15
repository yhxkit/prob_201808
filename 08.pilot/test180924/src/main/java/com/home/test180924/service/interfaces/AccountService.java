package com.home.test180924.service.interfaces;

import com.home.test180924.controller.responseUtil.ResultMessage;
import com.home.test180924.entity.AccountDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.validation.Errors;

import com.home.test180924.entity.Account;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AccountService {
	
	
	Account castAccountDtoToAccount(AccountDto accountDto);

    ResultMessage<Account> join(Account account);
    ResultMessage<Account> login(Account account);
    ResultMessage<Account> changeUserState(String userEmail, String satateCategory);
    void delete(String email);
    
    Page<Account> getAllUsers(int requiredUserListPage, int elementsNumberForOnePage);
    Iterable<Account> findAccountsWithPage(String keyword, int page, int elementsNumberForOnePage);
    
    Account findByEmail(String email);
    Account findByPostsPostIdx(int postIdx);
    Account findByCommentsCommentIdx(int commentIdx);

    Account changeStatus(Account account);
    Account changeAuth(Account account);
    ResultMessage<Account> editNameAndPassword(String email, String newName, String newPassword);


}
