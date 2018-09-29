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
          System.out.println("로그인 상태가 아니면 홈으로 리다이렉트;"+request.getContextPath()+"/");
          response.sendRedirect(request.getContextPath()+"/");
          return false;
      }

//        HttpSession session = request.getSession(false);// 세션이 없으면 새로 생성하지 말고 그냥 null..

//        if(session!=null){
//            System.out.println("세션이 비지 않음");
//            Optional loginCheck = Optional.ofNullable(session.getAttribute("loginCheck"));
//            System.out.println("세션의 loginCheck 값이 "+loginCheck);
//            if(loginCheck.isPresent()){
//                if((boolean)loginCheck.get() == true){return true;} //로그인 상태라면 true 리턴
//            }
//        }

        //그리고 .. 글수정/삭제시에는 본인매칭해서 해야하는데 이부분은 인터셉터를 새로 만들어야 할 것 같음. ㄴ
        System.out.println("로그인 상태가 아니면 홈으로 리다이렉트;"+request.getContextPath()+"/");
        response.sendRedirect(request.getContextPath()+"/");
        return false;
    }
}
