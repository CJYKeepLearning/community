package com.foling.community.dto;

import com.foling.community.model.User;
import lombok.Data;

/**
 * @Author foling
 * @Date2021-08-01 8:34
 * @Version 1.0
 * @Other Be happy~
 **/
@Data
public class CommentDTO {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private String content;
    private User user;
    private Integer commentCount;
}
