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
        if(jwt.parseToken(token).get("scope").equals(Auth.ADMIN)){ // scope 가 ADMIN이 아니면
            response.sendError(404, "권한이 없음 ㅇㅅㅇ ");

            return false;
        }

        return true;

    }

}
