package com.prob.pilot18091701.repository;

import com.prob.pilot18091701.entity.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, String>{
    Account findByEmail(String email);
}
