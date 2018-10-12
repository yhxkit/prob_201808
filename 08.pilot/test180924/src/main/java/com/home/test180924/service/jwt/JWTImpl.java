package com.home.test180924.service.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import com.home.test180924.entity.Account;

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

    	}catch (ExpiredJwtException e) {
			log.debug("ExpiredJwtException : "+token);
		}catch(UnsupportedJwtException e) {
			log.debug("UnsupportedJwtException : "+token);
		}catch(MalformedJwtException e) {
			log.debug("MalformedJwtException : "+token);
		}catch(SignatureException e) {
			log.debug("SignatureException : "+token);
		}catch(IllegalArgumentException e) {
			log.debug("IllegalArgumentException : "+token);
		}


		tokenMap.put("validToken",  validToken);
    	
    	return tokenMap;
    }
}
