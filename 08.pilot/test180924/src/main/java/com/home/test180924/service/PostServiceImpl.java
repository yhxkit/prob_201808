package com.home.test180924.service;

import com.home.test180924.entity.Account;
import com.home.test180924.entity.PostDto;
import com.home.test180924.repository.AccountRepository;
import com.home.test180924.service.jwt.JWT;
import com.home.test180924.service.validator.WriterCheckValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.home.test180924.entity.Post;
import com.home.test180924.repository.PostRepository;
import com.home.test180924.service.interfaces.PostService;
import com.home.test180924.util.Paging;


@Slf4j
@Service
@Transactional
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private AccountRepository accountRepository;
    private Paging paging;
    private JWT jwt;
    private WriterCheckValidator writerCheckValidator;


    public PostServiceImpl(PostRepository postRepository, AccountRepository accountRepository, Paging paging, JWT jwt, WriterCheckValidator writerCheckValidator) {
        this.postRepository = postRepository;
        this.accountRepository=accountRepository;
        this.paging = paging;
        this.jwt = jwt;
        this.writerCheckValidator=writerCheckValidator;
    }


    @Override
    public Page<Post> bbsWithPage(int requiredBbsPage, int elementsNumberForOnePage){
        Page<Post> bbsList = postRepository.findAll(paging.elementsByPage(requiredBbsPage, elementsNumberForOnePage,  "postIdx"));
        return bbsList;
    }


    //findAll은 OrderBy가 안먹힘 ㅠㅠ
//    public List<Post> testPageBbs(int requiredBbsPage){
//        int pageSize =3; //페이지 사이즈 어떻게 정할지 생각해둬야하지 않을까?
//        Page<Post> posts =  postRepository.findAllOrderByPostIdxDesc(PageRequest.of(requiredBbsPage, pageSize));
//        List<Post> postList = new ArrayList<>();
//        postList.addAll(posts.getContent());
//        return postList;
//    }

    @Override
    public Post write(PostDto postDto){

        if(postDto.getTitle().trim().isEmpty()){
          log.debug("포스트의 타이틀은 null일 수 없음");
        }
        if(postDto.getPostContent().trim().isEmpty()){
            log.debug("포스트의 내용은 null일 수 없음");
        }


        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setPostContent(postDto.getPostContent());
        post.setPostWriter(accountRepository.findByEmail(postDto.getPostWriter()));
        postRepository.save(post);
        return postRepository.findByPostIdx(post.getPostIdx());
    }

    @Override
    public Post detail(int postIdx){
        return postRepository.findByPostIdx(postIdx);
    }

    @Override
    public Post edit(int postIdx, PostDto postDto){//, HttpServletResponse response){

        Post post =  postRepository.findByPostIdx(postIdx);
        post.setPostContent(postDto.getPostContent());
        post.setTitle(postDto.getTitle());

        return  postRepository.save(post);
    }

    @Override
    public void delete(int postIdx){

//        Account writer = accountRepository.findByPostsPostIdx(postIdx);
//        String postWriterEmail = writer.getEmail();
//        if(!writerCheckValidator.checkWriter(postWriterEmail, token)){
//            return;
//        }
        postRepository.delete(postRepository.findByPostIdx(postIdx));

    }


    @Override
    public Iterable<Post> findPostsWithPage(String keyword, int page, int elementsNumberForOnePage) {
        return postRepository.findByTitleIgnoreCaseContainingOrderByPostIdxDesc(keyword);
    }
}
