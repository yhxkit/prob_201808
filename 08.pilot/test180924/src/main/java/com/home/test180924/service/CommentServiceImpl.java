package com.home.test180924.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.home.test180924.entity.Comment;
import com.home.test180924.repository.CommentRepository;
import com.home.test180924.repository.PostRepository;
import com.home.test180924.service.interfaces.CommentService;


@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Override
    public Iterable<Comment> commentListWithPage(int postIdx){
//      return commentRepository.findByBelongingPostPostIdxOrderByCommentIdxDesc(postIdx, PageRequest.of(0, 10)); //코멘트에 페이지를 주면 오류가 난다 ㅠ0ㅠ,,,
        return commentRepository.findByBelongingPostPostIdxOrderByCommentIdxDesc(postIdx);
    }

    @Override
    public void deleteByPostIdx(int postIdx){
        commentRepository.findByBelongingPostPostIdx(postIdx);
    }

    @Override
    public Comment reply(Comment comment, int postIdx) {
       comment.setBelongingPost(postRepository.findByPostIdx(postIdx));
       return commentRepository.save(comment);
    }


    @Override
    public  Comment edit(int postIdx, int commentIdx, Comment comment){
        comment.setCommentIdx(commentIdx);
        comment.setBelongingPost(postRepository.findByPostIdx(postIdx));
        return commentRepository.save(comment);
    }

    public void delete(int commentIdx){
        commentRepository.delete(commentRepository.findByCommentIdx(commentIdx));
    }


}
