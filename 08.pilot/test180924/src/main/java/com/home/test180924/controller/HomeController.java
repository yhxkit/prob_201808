package com.home.test180924.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.home.test180924.controller.responseUtil.EntityForResponse;
import com.home.test180924.entity.Account;
import com.home.test180924.service.jwt.TestJWT;

@Controller
public class HomeController {
	private EntityForResponse responseEntity;
	  private TestJWT jwt;
	
    public HomeController(EntityForResponse responseEntity, TestJWT jwt) {
		this.responseEntity = responseEntity;
	    this.jwt = jwt;
	}



	@GetMapping("/")
    public String home(HttpServletRequest request, HttpServletResponse response, Model model) {

        	System.out.println("홈..");

        return "home";
        	

    }
	
	
	@GetMapping("/home/test")
	public ResponseEntity<?> homet(HttpServletRequest req, HttpServletResponse res) {
		System.out.println("토큰 파싱..");
		return ResponseEntity.ok().build();
		
	}
	
}
