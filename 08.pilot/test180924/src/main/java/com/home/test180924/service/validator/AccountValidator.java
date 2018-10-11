package com.home.test180924.service.validator;

public interface AccountValidator {
    boolean supports(Class<?> clazz);
    boolean validate(Object target);
    boolean duplicatedCheck(String email);

}

