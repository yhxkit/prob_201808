package com.home.test180924.service.interfaces;

import com.home.test180924.controller.responseUtil.ResultMessage;
import com.home.test180924.entity.Account;
import com.home.test180924.entity.Comment;
import com.home.test180924.entity.CommentDto;

public interface CommentService {
    Iterable<Comment> commentListWithPage(int postIdx);
    void deleteByPostIdx(int postIdx);
    Comment reply(CommentDto commentDto, int postIdx);
    ResultMessage<Comment> edit(int postIdx, int commentIdx, CommentDto commentDto, String token);
    ResultMessage delete(int commentIdx, String token);
    boolean deleteAll(Iterable<Comment> userComments);
    Iterable<Comment> findByCommentWriter(String email);
}
