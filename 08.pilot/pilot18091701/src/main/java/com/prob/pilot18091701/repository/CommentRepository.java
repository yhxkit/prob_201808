package com.prob.pilot18091701.repository;

import com.prob.pilot18091701.entity.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Integer> {
    void deleteByBelongingPostPostIdx(int postIdx);
}
