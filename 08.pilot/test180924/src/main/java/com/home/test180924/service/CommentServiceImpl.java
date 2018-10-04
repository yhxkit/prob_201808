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
//      return commentRepository.findByBelongingPostPostIdxOrderByCommentIdxDesc(postIdx, PageRequest.of(0, 10)); //코멘트에 페이지를 주면 오류가 난다 ㅠ0ㅠ,,,
        return commentRepository.findByBelongingPostPostIdxOrderByCommentIdxDesc(postIdx);
    }

    @Override
    public void deleteByPostIdx(int postIdx){
        Iterable<Comment> deletingComments = commentRepository.findByBelongingPostPostIdx(postIdx);
        commentRepository.deleteAll(deletingComments);

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

    public void delete(int commentIdx, String token){
        Account writer = accountRepository.findByCommentsCommentIdx(commentIdx);
        String commentWriterEmail = writer.getEmail();
            if(!writerCheckValidator.checkWriter(commentWriterEmail, token)){
                return;
            }

        commentRepository.delete(commentRepository.findByCommentIdx(commentIdx));
    }


}
