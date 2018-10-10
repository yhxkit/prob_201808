package com.home.test180924.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.home.test180924.entity.Post;


@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Integer> {//CrudRepository<Post, Integer> {
    Post findByPostIdx(int postIdx);
    Page<Post> findAll(Pageable pageable);
    Iterable<Post> findByPostWriterEmailIgnoreCaseContainingOrderByPostIdxDesc(String keyword);
    Iterable<Post> findByTitleIgnoreCaseContainingOrderByPostIdxDesc(String keyword);

    // Iterable<Account> findByEmailIgnoreCaseContainingOrderByAccountCreatedTimeDesc(String email);// , PageRequest pageRequest 오류남
}

