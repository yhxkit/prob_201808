package com.home.test180924.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.home.test180924.entity.Account;

import java.util.List;


@Repository
public interface AccountRepository extends PagingAndSortingRepository<Account, String>{// CrudRepository<Account, String>{
    Account findByEmail(String email);
    Page<Account> findAll(Pageable pageable);
    Iterable<Account> findByEmailIgnoreCaseContainingOrderByAccountCreatedTimeDesc(String email);// , PageRequest pageRequest 오류남
    Page<Account> findByEmailIgnoreCaseContainingOrderByAccountCreatedTimeDesc(String email, PageRequest pageRequest);

    Page<Account> findByEmailIgnoreCaseContaining(String k, PageRequest p);
    Page<Account> findAllByEmailContaining(String email, PageRequest p);

    Account findByPostsPostIdx(int postIdx);


}
