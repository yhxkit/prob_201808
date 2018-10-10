package com.home.test180924.service.interfaces;

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

    boolean join(Account account);
    HashMap login(Account account);
    Account changeStatus(Account account);
    Account changeAuth(Account account);
    void delete(Account account);
    Page<Account> getAllUsers(int requiredUserListPage, int elementsNumberForOnePage);

    Iterable<Account> findAccountsWithPage(String keyword, int page, int elementsNumberForOnePage);

    Account findByEmail(String email);

    Account findByPostsPostIdx(int postIdx);
    Account findByCommentsCommentIdx(int commentIdx);

    Account editNameAndPassword(Account account);


}
