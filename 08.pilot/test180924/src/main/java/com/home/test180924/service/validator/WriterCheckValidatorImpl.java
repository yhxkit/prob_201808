package com.home.test180924.service.validator;

import com.home.test180924.controller.responseUtil.ResultMessage;
import com.home.test180924.entity.Account;
import com.home.test180924.entity.enumForEntity.Article;
import com.home.test180924.repository.AccountRepository;
import com.home.test180924.service.interfaces.AccountService;
import com.home.test180924.service.jwt.JWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class WriterCheckValidatorImpl implements WriterCheckValidator{

    private AccountRepository accountRepository;
    private JWT jwt;

    public WriterCheckValidatorImpl(JWT jwt, AccountRepository accountRepository) {
        this.jwt = jwt;
        this.accountRepository = accountRepository;
    }

    public boolean checkWriter(String writer, String token){
        Map tokenMap =  jwt.parseToken(token);
        String loginUserEmail =(String)tokenMap.get("subject");
        if(writer.equals(loginUserEmail)){
            return true;
        }
        log.debug("작성자 불일치!! 데이터 작성자 : "+writer+" / 데이터를 변경하려는 로그인 유저 : "+loginUserEmail);
        return false;
    }


    @Override
    public ResultMessage getResultMessageByWriterCheck(int elementIdx, Article article, String token) {
        ResultMessage resultMessage;
        Account writerAccount;

        if(article.equals(Article.COMMENT)){
            writerAccount = accountRepository.findByCommentsCommentIdx(elementIdx);
        }else{
            writerAccount = accountRepository.findByPostsPostIdx(elementIdx);
        }
        String writer = writerAccount.getEmail();

        if(checkWriter(writer, token)){
            resultMessage = new ResultMessage("success", "작성자 일치", false);
        }else{
            resultMessage = new ResultMessage("fail", "작성자가 아닙니다", true);
        }

        return resultMessage;
    }
}
