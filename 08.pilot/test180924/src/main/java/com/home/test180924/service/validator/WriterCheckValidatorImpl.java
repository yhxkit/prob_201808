package com.home.test180924.service.validator;

import com.home.test180924.service.jwt.JWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class WriterCheckValidatorImpl implements WriterCheckValidator{

    private JWT jwt;

    public WriterCheckValidatorImpl(JWT jwt) {
        this.jwt = jwt;
    }

    @Override
    public boolean checkWriter(String writer, String token){
        Map tokenMap =  jwt.parseToken(token);
        String loginUserEmail =(String)tokenMap.get("subject");
        if(writer.equals(loginUserEmail)){
            return true;
        }
        log.debug("글쓴이 : "+writer+" / 데이터를 변경하려는 로그인 유저 : "+loginUserEmail);

        return false;
    }

}
