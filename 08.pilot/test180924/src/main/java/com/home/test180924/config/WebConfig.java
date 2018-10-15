package com.home.test180924.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.home.test180924.controller.interceptor.CustomInterceptor;

@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private CustomInterceptor customInterceptor;

    public WebConfig( CustomInterceptor customInterceptor) {
        this.customInterceptor = customInterceptor;
    }
    

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	registry.addInterceptor(customInterceptor);
    }


}
