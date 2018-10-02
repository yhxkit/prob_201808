package com.home.test180924.service.interfaces;

import com.home.test180924.entity.Comment;
import com.home.test180924.entity.CommentDto;

public interface CommentService {
    Iterable<Comment> commentListWithPage(int postIdx);
    void deleteByPostIdx(int postIdx);
    Comment reply(CommentDto commentDto, int postIdx);
    Comment edit(int postIdx, int commentIdx, Comment comment);
    void delete(int commentIdx, String token);
}
