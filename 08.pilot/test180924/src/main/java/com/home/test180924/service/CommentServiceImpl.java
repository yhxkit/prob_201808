package com.home.test180924.service;

import com.home.test180924.service.validator.WriterCheckValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.home.test180924.entity.Account;
import com.home.test180924.entity.Comment;
import com.home.test180924.entity.CommentDto;
import com.home.test180924.repository.AccountRepository;
import com.home.test180924.repository.CommentRepository;
import com.home.test180924.repository.PostRepository;
import com.home.test180924.service.interfaces.CommentService;

import java.util.List;


@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private AccountRepository accountRepository;
    private PostRepository postRepository;
    private WriterCheckValidator writerCheckValidator;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, AccountRepository accountRepository, WriterCheckValidator writerCheckValidator) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.accountRepository = accountRepository;
        this.writerCheckValidator = writerCheckValidator;
    }

    @Override
    public Iterable<Comment> commentListWithPage(int postIdx){
        return commentRepository.findByBelongingPostPostIdxOrderByCommentIdxDesc(postIdx);
    }

    @Override
    public void deleteByPostIdx(int postIdx){
        Iterable<Comment> deletingComments = commentRepository.findByBelongingPostPostIdx(postIdx);
        commentRepository.deleteAll(deletingComments);

    }

    @Override
    public boolean deleteAll(Iterable<Comment> userComments) {
        commentRepository.deleteAll(userComments);
        return true;
    }

    @Override
    public Iterable<Comment> findByCommentWriter(String email) {
        return commentRepository.findByCommentWriter_Email(email);
    }

    @Override
    public Comment reply(CommentDto commentDto, int postIdx) {
    		
    	Comment comment = new Comment();
    	comment.setBelongingPost(postRepository.findByPostIdx(postIdx));
    	
    	Account writer = accountRepository.findByEmail(commentDto.getCommentWriterEmail());
    	System.out.println(writer.getEmail()+writer.getName());
    	comment.setCommentWriter(writer);
    	comment.setCommentContent(commentDto.getCommentContent());
    	
       return commentRepository.save(comment);
    }


    @Override
    public  Comment edit(int postIdx, int commentIdx, CommentDto commentDto){

    	Comment origin = commentRepository.findByCommentIdx(commentIdx);
    	origin.setCommentContent(commentDto.getCommentContent());
    	
        return commentRepository.save(origin);
    }

    public boolean delete(int commentIdx){ //리턴이 필요한가...?
        commentRepository.delete(commentRepository.findByCommentIdx(commentIdx));
        return true;
    }


}
