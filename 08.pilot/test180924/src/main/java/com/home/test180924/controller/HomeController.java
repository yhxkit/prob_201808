package com.home.test180924.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.home.test180924.controller.responseUtil.EntityForResponse;
import com.home.test180924.service.jwt.JWT;

@Controller
public class HomeController {
	private EntityForResponse responseEntity;
	  private JWT jwt;
	
    public HomeController(EntityForResponse responseEntity, JWT jwt) {
		this.responseEntity = responseEntity;
	    this.jwt = jwt;
	}


	@GetMapping("/")
    public String home() {
        return "home"; 
    }
	
	
}
