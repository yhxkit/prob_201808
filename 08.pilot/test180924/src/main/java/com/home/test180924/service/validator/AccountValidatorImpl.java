package com.home.test180924.service.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.home.test180924.entity.Account;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AccountValidatorImpl implements AccountValidator  {
    private static final String emailRegExp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[Z-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private Pattern pattern = Pattern.compile(emailRegExp);

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

}
