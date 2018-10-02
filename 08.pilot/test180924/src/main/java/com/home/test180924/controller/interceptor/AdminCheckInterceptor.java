package com.home.test180924.controller.interceptor;

import com.home.test180924.entity.enumForEntity.Auth;
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
public class AdminCheckInterceptor extends HandlerInterceptorAdapter {


    private TestJWT jwt;

    public AdminCheckInterceptor(TestJWT jwt) {
        this.jwt = jwt;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token =request.getHeader("token");
        System.out.println("관리자 권한 프리 핸들러~"+ jwt.parseToken(token).get("scope"));
        System.out.println("필요 권한 "+Auth.ADMIN);
        System.out.println("유저 권한 "+jwt.parseToken(token).get("scope"));
        if(!jwt.parseToken(token).get("scope").equals(Auth.ADMIN.toString())){ // scope 가 ADMIN이 아니면

            response.sendError(402, "관리자 권한이 필요합니다");

            return false;
        }

        return true;

    }

}
