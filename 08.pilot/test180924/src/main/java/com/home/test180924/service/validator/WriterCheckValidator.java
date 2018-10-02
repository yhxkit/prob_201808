package com.home.test180924.service.validator;

import com.home.test180924.service.jwt.TestJWT;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class WriterCheckValidator {

    private TestJWT jwt;

    public WriterCheckValidator(TestJWT jwt) {
        this.jwt = jwt;
    }

    public boolean checkWriter(String writer, String token){
        Map tokenMap =  jwt.parseToken(token);
        String loginUserEmail =(String)tokenMap.get("subject");
        if(writer.equals(loginUserEmail)){
            return true;
        }
        return false;
    }



}
