package com.home.test180924.controller.interceptor;

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

    public LoginCheckInterceptor(TestJWT jwt) {
        this.jwt = jwt;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

      String token =request.getHeader("token");

      System.out.println("로그인 프리 핸들러~"+token);
      //현재 세션에 저장된 토큰이 유효하지 않으면 처리...

      if(!(boolean)jwt.parseToken(token).get("validToken")){ //유효하지 않은 토큰이면
          response.sendError(404, "로그인 해 ㅇㅅㅇ ");
          return false;
      }

        return true;
    }
}
