package com.home.test180924.repository;

import com.home.test180924.entity.Account;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.home.test180924.entity.Comment;

@Repository
public interface CommentRepository extends PagingAndSortingRepository<Comment, Integer>{ // CrudRepository<Comment, Integer> {
    Iterable<Comment> findByBelongingPostPostIdxOrderByCommentIdxDesc(int postIdx);
    Iterable<Comment> findByBelongingPostPostIdx(int postIdx);
    Comment findByCommentIdx(int commentIdx);
    Iterable<Comment> findByCommentWriter_Email(String email);

    //    Iterable<Comment> findByBelongingPostPostIdx(int postIdx, PageRequest pageable);
        //Iterable<Comment> findByBelongingPostPostIdxOrderByCommentIdxDesc(int postIdx, PageRequest pageRequest); //오류남..
}
