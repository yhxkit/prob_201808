package com.home.test180924.service.validator;


import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public interface WriterCheckValidator {

    boolean checkWriter(String writer, String token);
    HashMap<String, String> getResultMessageWithWriterCheck(String writer, String token);
}
