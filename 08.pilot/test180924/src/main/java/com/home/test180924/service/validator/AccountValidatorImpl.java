package com.home.test180924.service.validator;

import com.home.test180924.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.home.test180924.entity.Account;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class AccountValidatorImpl implements AccountValidator  {
    private static final String emailRegExp ="[0-9a-zA-Z][_0-9a-zA-Z-]*@[_0-9a-zA-Z-]+(\\.[_0-9a-zA-Z-]+){1,2}$";
    private Pattern pattern = Pattern.compile(emailRegExp);


    private AccountRepository accountRepository;

    public AccountValidatorImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Account.class.isAssignableFrom(clazz); //clazz 객체가 Account 객체로 변환 가능한지 확인
    }

    @Override
    public void validate(Object target, Errors errors) {
        Account account =(Account)target;
        if(account.getEmail()==null || account.getEmail().trim().isEmpty()){
        //    errors.rejectValue("email", "EmailIsRequired");
            errors.rejectValue("email", "required");
        } else {
            Matcher matcher = pattern.matcher(account.getEmail());
            if(!matcher.matches()){
                errors.rejectValue("email", "invalidEmail");
            }
        }

    }

    @Override
    public boolean validate(Object target) {
        Account account =(Account)target;
            if (account.getEmail() == null || account.getEmail().trim().isEmpty()) {
                return true;
            } else {
                Matcher matcher = pattern.matcher(account.getEmail());
                if (!matcher.matches()) {
                    return true;
                }
            }

        return false;
    }

    @Override
    public boolean duplicatedCheck(String email){
        Optional<Account> checkAccount = accountRepository.findById(email);
        if(checkAccount.isPresent()){
            return true;
        }
        return false;
    }

}
