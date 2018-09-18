package com.prob.pilot18091701.service;


import com.prob.pilot18091701.entity.Account;
import com.prob.pilot18091701.repository.AccountRepository;
import com.prob.pilot18091701.util.AES256;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.OpInc;
import org.springframework.stereotype.Service;
import sun.security.krb5.internal.crypto.Aes128;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AccountService {
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	private AES256 aes256;


	public Iterable<Account> join(Account account){
		Optional<Account> checkAccount = accountRepository.findById(account.getEmail());
		if(!checkAccount.isPresent()){
			account.setPassword(aes256.encrypt(account.getPassword()));
			accountRepository.save(account);
		}
		return  accountRepository.findAll();
	}


	public  Account login(Account account){
		Account checkAccount = accountRepository.findByEmail(account.getEmail());

		log.info("들어온 패스"+aes256.encrypt(account.getPassword()));
		log.info("db 패스"+checkAccount.getPassword());

		if(checkAccount.getPassword().equals(aes256.encrypt(account.getPassword()))){
			return  checkAccount;
		}else{
			return null; //여기 어떻게 할지...
		}

	}

	public Account update(Account account){ //이거 가입하고 겹치는데..?
		Account checkAccount = accountRepository.findByEmail(account.getEmail()); //여기 조건 확인해야할듯
			account.setPassword(aes256.encrypt(account.getPassword()));
			accountRepository.save(account);
		return accountRepository.findByEmail(account.getEmail());


	}
	
}
