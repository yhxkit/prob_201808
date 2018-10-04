package com.home.test180924.service.jwt;

import java.util.Map;

import com.home.test180924.entity.Account;

public interface JWT {
	String getJwt(Account account);
	Map parseToken(String token);
}
