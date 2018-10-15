package com.home.test180924.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.home.test180924.controller.interceptor.CustomAnnotation;
@Controller
public class HomeController {

	@GetMapping("/")
    public String home() {
        return "home"; 
    }
	
	
}
