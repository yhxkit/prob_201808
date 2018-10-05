package com.home.test180924.service.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.springframework.stereotype.Component;

import com.home.test180924.entity.Account;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class
JWTImpl implements JWT{

	String secretKey = "SECRET"; // 앗.. 이거 어떻게 하지
	
	@Override
    public String getJwt(Account account){
    	
    	Date date = new Date();
    	Date ex = new Date(date.getTime()+ (long)(1000*60*10));//10분 //(long)(1000*60*60*3)3시간

        String token = Jwts.builder()
        		.setSubject(account.getEmail())
        		.setHeaderParam("typ", "JWT")
        		.setIssuedAt(date)
        		.setExpiration(ex)
        		.claim("name", account.getName())
				.claim("status", account.getStatus())
        		.claim("scope", account.getAuth())
        		.signWith(SignatureAlgorithm.HS512, secretKey)
        		.compact();

     log.info("발행 토큰 "+token);
     return token;
    	
    }
    
    
    @Override
    public HashMap<String, ?> parseToken(String token) {
    	
    	//This line will throw an exception if it is not a signed JWS (as expected)
		HashMap tokenMap = new HashMap();

        boolean validToken = false;
    	try {
    	
    	Claims claims = Jwts.parser()         
           .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
           .parseClaimsJws(token).getBody();

			log.debug(claims.getSubject()); //userEmail
			log.debug(claims.get("name").toString());
			log.debug(claims.get("scope").toString());
			log.debug(claims.get("status").toString());
			log.debug(claims.getIssuedAt().toString());
			log.debug(claims.getExpiration().toString());

			tokenMap.put("subject",  claims.getSubject()); //userEmail
			tokenMap.put("name",  claims.get("name"));
			tokenMap.put("scope",  claims.get("scope"));
			tokenMap.put("status",  claims.get("status"));
			tokenMap.put("issued", claims.getIssuedAt());
			tokenMap.put("expiration",  claims.getExpiration());

			validToken=true;

    	}catch (Exception e) {
    	    // ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException;
    		log.debug(token); //에러시 익셉션 별로 체크할 수 있게 정리...
    		log.info("유효하지 않은 토큰 "+e);
		}

		tokenMap.put("validToken",  validToken);
    	
    	return tokenMap;
    }
}
