package com.home.test180924.controller.interceptor;

import com.home.test180924.entity.enumForEntity.Status;
import com.home.test180924.repository.AccountRepository;
import com.home.test180924.service.jwt.TestJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Slf4j
@Component
public class LoginCheckInterceptor extends HandlerInterceptorAdapter { //HandlerInterceptor 인터페이스 상속해서 모든 메서드를 쓰기보단느 그냥 구현체 상속해서 쓰는게 낫다고 합니다..

    private TestJWT jwt;
    private AccountRepository accountRepository;

    public LoginCheckInterceptor(TestJWT jwt, AccountRepository accountRepository) {
        this.jwt = jwt;
        this.accountRepository = accountRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

      String token =request.getHeader("token");

      System.out.println("로그인 프리 핸들러~"+token);
      //현재 세션에 저장된 토큰이 유효하지 않으면 처리...

        String userEmail = (String)jwt.parseToken(token).get("subject");

        Optional checkExistingUser = Optional.ofNullable(accountRepository.findByEmail(userEmail));
        if(!checkExistingUser.isPresent()){
            System.out.println("없는 계정");
            response.sendError(401, "없는 계정입니다");
            return false;
        }



      if(!(boolean)jwt.parseToken(token).get("validToken")){ //유효하지 않은 토큰이면
          System.out.println("토큰이 유효하지 않음");
          response.sendError(401, "로그인하세요");
          return false;
      }

      if( jwt.parseToken(token).get("status").equals(Status.DISABLE.toString()) ){
            System.out.println("정지 먹은 회원..");
            response.sendError(403, "계정이 정지되었습니다");
            return false;
      }

        return true;
    }
}
