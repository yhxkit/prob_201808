package com.home.test180924.service.validator;


import javax.servlet.http.HttpServletResponse;

public interface WriterCheckValidator {

    boolean checkWriter(String writer, String token);

}
