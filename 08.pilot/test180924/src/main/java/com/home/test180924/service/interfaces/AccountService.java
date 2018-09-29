package com.home.test180924.service.interfaces;

import org.springframework.data.domain.Sort;
import org.springframework.validation.Errors;

import com.home.test180924.entity.Account;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AccountService {

    boolean errorCheck(Errors errors, final String value);
    Object join(Account account, Errors errors);
    Object login(Account account, HttpServletRequest request, Errors errors);
    Account changePassword(Account account, Errors errors);
    Account changeStatus(Account account, Errors errors);
    void delete(Account account);
    List<Account> getAllUsers(int requiredUserListPage, int elementsNumberForOnePage);
//    Iterable<Account> findAccountsWithPage(String keyword);
    Iterable<Account> findAccountsWithPage(String keyword, int page, int elementsNumberForOnePage);

}
