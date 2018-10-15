package com.home.test180924.service;

import com.home.test180924.controller.responseUtil.ResultMessage;
import com.home.test180924.entity.PostDto;
import com.home.test180924.entity.enumForEntity.Article;
import com.home.test180924.service.validator.ArticleValidator;
import com.home.test180924.service.validator.WriterCheckValidator;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.home.test180924.entity.Post;
import com.home.test180924.repository.AccountRepository;
import com.home.test180924.repository.CommentRepository;
import com.home.test180924.repository.PostRepository;
import com.home.test180924.service.interfaces.PostService;
import com.home.test180924.util.Paging;


@Slf4j
@Service
@Transactional
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private AccountRepository accountRepository;
    private CommentRepository commentRepository;
    private Paging paging;

    private WriterCheckValidator writerCheckValidator;
    private ArticleValidator articleValidator;


    public PostServiceImpl(PostRepository postRepository, AccountRepository accountRepository, CommentRepository commentRepository, Paging paging, WriterCheckValidator writerCheckValidator, ArticleValidator articleValidator) {
        this.postRepository = postRepository;
        this.accountRepository = accountRepository;
        this.commentRepository = commentRepository;
        this.paging = paging;
        this.writerCheckValidator=writerCheckValidator;
        this.articleValidator = articleValidator;
    }



    @Override
    public Page<Post> bbsWithPage(int requiredBbsPage, int elementsNumberForOnePage){
        Page<Post> bbsList = postRepository.findAll(paging.elementsByPage(requiredBbsPage, elementsNumberForOnePage,  "postIdx"));
        return bbsList;
    }


    @Override
    public ResultMessage<Post> write(PostDto postDto){
        ResultMessage resultMessage =  articleValidator.emptyCheck(postDto);
        if(resultMessage.isImmediateReturn()){
            return resultMessage;
        }

        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setPostContent(postDto.getPostContent());
        post.setPostWriter(accountRepository.findByEmail(postDto.getPostWriter()));
        postRepository.save(post);
        resultMessage.setData(postRepository.findByPostIdx(post.getPostIdx()));
        return resultMessage;
    }

    @Override
    public void deleteAll(Iterable<Post> userPosts){
        postRepository.deleteAll(userPosts);
    }

    @Override
    public ResultMessage<Post> detail(int postIdx){
        return articleValidator.articleExistencyCheck(postRepository.findByPostIdx(postIdx));
    }

    @Override
    public ResultMessage<Post> edit(int postIdx, PostDto postDto, String token){
        ResultMessage resultMessage = articleValidator.articleExistencyCheck(postRepository.findByPostIdx(postIdx));
        if(resultMessage.isImmediateReturn()){
            return resultMessage;
        }
        resultMessage = writerCheckValidator.getResultMessageByWriterCheck(postIdx, Article.POST, token);
        if(resultMessage.isImmediateReturn()){
            return resultMessage;
        }
        Post post =  postRepository.findByPostIdx(postIdx);
        post.setPostContent(postDto.getPostContent());
        post.setTitle(postDto.getTitle());
        resultMessage.setData(postRepository.save(post));
        return resultMessage;
    }

    @Override
    public ResultMessage delete(int postIdx, String token){
        ResultMessage resultMessage = articleValidator.articleExistencyCheck(postRepository.findByPostIdx(postIdx));
        if(resultMessage.isImmediateReturn()){
            return resultMessage;
        }
        resultMessage = writerCheckValidator.getResultMessageByWriterCheck(postIdx, Article.POST, token);
        if(resultMessage.isImmediateReturn()){
            return resultMessage;
        }
        commentRepository.deleteAll( commentRepository.findByBelongingPostPostIdx(postIdx) );//코멘트들도 같이 삭제
        postRepository.delete(postRepository.findByPostIdx(postIdx));
        return resultMessage;
    }

   
    

    @Override
    public Iterable<Post> findPostsWithPage(String keyword, int page, int elementsNumberForOnePage) {
        return postRepository.findByTitleIgnoreCaseContainingOrderByPostIdxDesc(keyword);
    }
    
    @Override
    public Iterable<Post> findByWriter(String keyword) {
    	return postRepository.findByPostWriterEmailIgnoreCaseContainingOrderByPostIdxDesc(keyword);
    }


}
