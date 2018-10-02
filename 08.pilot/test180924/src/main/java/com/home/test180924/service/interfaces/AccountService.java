package com.home.test180924.service.interfaces;

import com.home.test180924.entity.AccountDto;
import org.springframework.data.domain.Sort;
import org.springframework.validation.Errors;

import com.home.test180924.entity.Account;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AccountService {

    boolean errorCheck(Errors errors, final String value);
    void join(Account account, HttpServletResponse response, Errors errors);
    HashMap login(Account account, HttpServletResponse response, Errors errors);
    Account changePassword(Account account, Errors errors);
    Account changeStatus(AccountDto accountDto, Errors errors);
    void delete(Account account);
    List<Account> getAllUsers(int requiredUserListPage, int elementsNumberForOnePage);
    long getTotalUser();

//    Iterable<Account> findAccountsWithPage(String keyword);
    Iterable<Account> findAccountsWithPage(String keyword, int page, int elementsNumberForOnePage);

    boolean duplicatedCheck(String email, HttpServletResponse response, Errors errors);

}
