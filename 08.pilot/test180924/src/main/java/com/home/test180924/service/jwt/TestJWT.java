package com.home.test180924.service.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.DatatypeConverter;

import com.home.test180924.entity.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Slf4j
@Component
public class TestJWT {

	String secretKey = "SECRET"; // 앗.. 이거 어떻게 하지
	
	
    public String getJwt(Account account){
    	
    	Date date = new Date();
    	Date ex = new Date(date.getTime()+ (long)(1000*60*5));//5분 //(long)(1000*60*60*3)3시간

        String token = Jwts.builder()
        		.setSubject(account.getEmail())
        		.setHeaderParam("typ", "JWT")
        		.setIssuedAt(date)
        		.setExpiration(ex)
        		.claim("name", account.getName())
        		.claim("scope", account.getAuth())
        		.signWith(SignatureAlgorithm.HS512, secretKey)
        		.compact();

     log.debug("발행 토큰 "+token);
     return token;
    	
    }
    
    
    
    public Map parseToken(String token) {
    	
    	//This line will throw an exception if it is not a signed JWS (as expected)

		Map tokenMap = new HashMap();

        boolean validToken = false;
    	try {
    	
    	Claims claims = Jwts.parser()         
           .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
           .parseClaimsJws(token).getBody();

		tokenMap.put("subject",  claims.getSubject()); //userEmail
		tokenMap.put("name",  claims.get("name"));
		tokenMap.put("scope",  claims.get("scope"));
		tokenMap.put("issued", claims.getIssuedAt());
		tokenMap.put("expiration",  claims.getExpiration());

		validToken=true;

    	}catch (Exception e) {
    		System.out.println("유효하지 않은 토큰 "+e);
		}

		tokenMap.put("validToken",  validToken);
    	
    	return tokenMap;
    }
    
    
    
    
    

}
