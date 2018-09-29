package com.home.test180924.config;


import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.home.test180924.controller.interceptor.AdminCheckInterceptor;
import com.home.test180924.controller.interceptor.LoginCheckInterceptor;

@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {


    private LoginCheckInterceptor loginCheckInterceptor;
    private AdminCheckInterceptor adminCheckInterceptor;

    public WebConfig(LoginCheckInterceptor loginCheckInterceptor, AdminCheckInterceptor adminCheckInterceptor) {
        this.loginCheckInterceptor = loginCheckInterceptor;
        this.adminCheckInterceptor = adminCheckInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(loginCheckInterceptor).addPathPatterns("/bbs/write", "/bbs/*/comment", "/logout", "/update", "/deleteAccount"); //해당 url은 로그인 되지 않았으면 접속 ㄴㄴ
        //rest로 해놔서 같은 url에 메서드만 다른건 어떻게 하지? 상세보기만 하게 하고 싶은데, 지금 상태로는 쓰기는 안되는데 수정/삭제까지 가능한 상태... 이거는 자기가 쓴 글만 되도록 하면 되나?

      //  registry.addInterceptor(adminCheckInterceptor).addPathPatterns("/admin/**");

    }


    @Bean
    public MessageSource messageSource(){

        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();

        YamlPropertiesFactoryBean bean = new YamlPropertiesFactoryBean();
        bean.setResources(new ClassPathResource("message/messages.yml"));
        messageSource.setCommonMessages(bean.getObject());

        messageSource.setDefaultEncoding("UTF-8");



        return messageSource;
    }





}
