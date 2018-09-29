package com.home.test180924.controller.interceptor;

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

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("관리자 권한 프리 핸들러~");

        HttpSession session = request.getSession(false);// 세션이 없으면 새로 생성하지 말고 그냥 null..
            if(session!=null){
                System.out.println("세션이 비지 않음");
                Optional authCheck = Optional.ofNullable(session.getAttribute("authCheck"));
                System.out.println("세션의 authCheck 값이 "+ authCheck);
                if(authCheck.isPresent()){
                    if((boolean)authCheck.get() == true){return true;} //관리자 맞으면 true
                }
            }

            System.out.println("세션이 null인 상태거나, 관리자가 아니면 홈으로 리다이렉트;"+request.getContextPath()+"/");
            response.sendRedirect(request.getContextPath()+"/");
            return false;
    }

}
