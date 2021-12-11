package com.foling.community.mapper;

import com.foling.community.model.Comment;
import com.foling.community.model.Question;

/**
 * @Author foling
 * @Date2021-08-01 22:38
 * @Version 1.0
 * @Other Be happy~
 **/
public interface CommentExtMapper {
    int incCommentCount(Comment comment);
}
