package com.home.test180924.service.validator;

import org.springframework.validation.Errors;

public interface AccountValidator {
    boolean supports(Class<?> clazz);
    void validate(Object target, Errors errors);
    boolean validate(Object target);
    boolean duplicatedCheck(String email);

}

