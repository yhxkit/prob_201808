package com.home.test180924.service.validator;

import com.home.test180924.controller.responseUtil.ResultMessage;
import com.home.test180924.entity.Article;

import java.util.Optional;

public interface ArticleValidator {
    <T extends Article> ResultMessage emptyCheck(T article);
    <T extends Article>  ResultMessage articleExistencyCheck(T article);

}
