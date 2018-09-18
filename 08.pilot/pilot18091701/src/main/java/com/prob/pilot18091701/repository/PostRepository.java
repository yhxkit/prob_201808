package com.prob.pilot18091701.repository;

import com.prob.pilot18091701.entity.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PostRepository extends CrudRepository<Post, Integer> {
    Post findByPostIdx(int postIdx);
    void deleteByPostIdx(int postIdx);
}
