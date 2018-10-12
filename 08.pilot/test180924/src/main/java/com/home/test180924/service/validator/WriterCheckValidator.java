package com.home.test180924.service.validator;


import com.home.test180924.controller.responseUtil.ResultMessage;
import com.home.test180924.entity.enumForEntity.Article;


public interface WriterCheckValidator {

    boolean checkWriter(String writer, String token);
    ResultMessage getResultMessageByWriterCheck(int elementIdx, Article article, String token);

}
