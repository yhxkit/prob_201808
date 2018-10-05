package com.home.test180924.controller.interceptor;

import com.home.test180924.entity.enumForEntity.Auth;
import com.home.test180924.service.jwt.JWT;
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


    private JWT jwt;

    public AdminCheckInterceptor(JWT jwt) {
        this.jwt = jwt;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token =request.getHeader("token");
             log.debug("관리자 프리 핸들러");
        if(!jwt.parseToken(token).get("scope").equals(Auth.ADMIN.toString())){ // scope 가 ADMIN이 아니면
        	log.debug("관리자 페이지에 접근하려는 유저 "+jwt.parseToken(token).get("subject"));
            response.sendError(402, "관리자 권한이 필요합니다");
            return false;
        }

        return true;

    }

}
