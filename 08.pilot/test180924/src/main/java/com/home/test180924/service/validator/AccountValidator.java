package com.home.test180924.service.validator;

import com.home.test180924.controller.responseUtil.ResultMessage;
import com.home.test180924.entity.Account;

public interface AccountValidator {
    ResultMessage validate(Account account);
    boolean duplicatedCheck(String email);

    ResultMessage userExistencyCheck(String email);
    ResultMessage loginPasswordCheck(Account account, String password);



}

